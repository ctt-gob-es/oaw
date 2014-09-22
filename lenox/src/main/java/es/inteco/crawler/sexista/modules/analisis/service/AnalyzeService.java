package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.crawler.sexista.modules.analisis.dao.RastreoDao;
import es.inteco.crawler.sexista.modules.analisis.dao.ResultadoDao;
import es.inteco.crawler.sexista.modules.analisis.dao.TerminosDao;
import es.inteco.crawler.sexista.modules.analisis.dto.AnalisisDto;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import es.inteco.crawler.sexista.modules.commons.util.Crono;
import es.inteco.crawler.sexista.modules.commons.util.UtilProcessingText;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Servicio inicial de ejecucion del algoritmo.
 */
public class AnalyzeService {

    private static int concurrentUsers = 0;

    public static int getConcurrentUsers() {
        return concurrentUsers;
    }

    public static void setConcurrentUsers(int concurrentUsers) {
        AnalyzeService.concurrentUsers = concurrentUsers;
    }

    /**
     * Campo terminosService.
     */
    private TerminosService terminosService = null;

    /**
     * Campo exceptionService.
     */
    private ExceptionService exceptionService = null;

    /**
     * Campo rastreoService.
     */
    private RastreoService rastreoService = null;

    /**
     * Campo resultadoService.
     */
    private ResultadoService resultadoService = null;

    /**
     * Campo crono.
     */
    private Crono crono = null;

    /**
     * Listado de terminos sexistas encontrados.
     */
    private ArrayList<TerminoPositionDto> lstGroupTerminos;

    /**
     * Listado de terminos del texto troceado.
     */
    private ArrayList<String> lstTerminos;

    /**
     * Método de ejecución inicial del analisis.
     * <p/>
     * 1- Inicialiamos los objetos necesarios : this.init()
     * <p/>
     * 2- Inicializamos en contador de tiempo para la ejecucion :
     * crono.inicializa()
     * <p/>
     * 3- Realizamos el analisis del contenido :
     * this.procesarContenido(analisisDto.getContenido())
     * <p/>
     * 4- SI lstGroupTerminos y lstTerminos nos distintos de null
     * <p/>
     * 4.1- Creamos el objeto rastreoDto de inserción en rastreos : rastreoDto =
     * new RastreoDto();
     * <p/>
     * 4.2- Asignamos valores al objeto :
     * rastreoDto.setIdRastreo(analisisDto.getIdRastreo()); :
     * rastreoDto.setUsuario(analisisDto.getUsuario()); :
     * rastreoDto.setFecha(analisisDto.getFecha()); :
     * rastreoDto.setTiempo(crono.getTime());
     * <p/>
     * 4.3- Insertamos el rastreo : rastreoService.insertRastreo(rastreoDto);
     * <p/>
     * 4.4- SI no se encuentran terminos, insertamos con 'termino vacio' :
     * lstGroupTerminos.size() == 0
     * <p/>
     * 4.4.1- Incializamos objeto resultado : resultadoDto = new ResultadoDto();
     * <p/>
     * 4.4.2- Rellenamos con los valores :
     * resultadoDto.setIdRastreo(analisisDto.getIdRastreo()); :
     * resultadoDto.setUrlTermino(analisisDto.getUrl());
     * <p/>
     * 4.4.3- Insertamos el resultado SIN termino ni contexto :
     * resultadoService.insertResultado(resultadoDto);
     * <p/>
     * FIN SI (del punto 4.4)
     * <p/>
     * 4.5- PARA CADA termino del listado lstGroupTerminos
     * <p/>
     * 4.4.1- Incializamos objeto resultado : resultadoDto = new ResultadoDto();
     * <p/>
     * 4.4.2- Rellenamos con los valores :
     * resultadoDto.setIdRastreo(analisisDto.getIdRastreo()); :
     * resultadoDto.setUrlTermino(analisisDto.getUrl()); :
     * resultadoDto.setIdTermino(terminoPositionDto.getIdTermino());
     * <p/>
     * 4.4.3- PARA CADA posicion, obtenemos el contexto del termino : for
     * (Integer posicion : terminoPositionDto.getPositions()) {
     * <p/>
     * 4.4.3.1- Invocamos util.obtenerContexto para recuperar el contexto del
     * termino : resultadoDto.setContexto(util.obtenerContexto(posicion,
     * lstTerminos));
     * <p/>
     * 4.4.3.2- Insertamos el resultado :
     * resultadoService.insertResultado(resultadoDto);
     * <p/>
     * FIN PARA CADA (del punto 4.5)
     * <p/>
     * FIN SI (del punto 4) *
     *
     * @param analisisDto - Datos del analisis
     * @throws BusinessException BusinessException
     */
    public void analyze(AnalisisDto analisisDto, boolean save) throws BusinessException {
        Connection conn = null;

        try {
            conn = ConexionBBDD.conectar();

            // Inicializamos objetos
            this.init();

            // Inicializamos el periodo de tiempo a medir
            crono.inicializa();

            // Realizamos el analisis del del contenido (cargamos lstGroupTerminos y
            // lstTerminos)
            procesarContenido(conn, analisisDto.getContenido());

            // Insertamos en BD los resultados del analisis.
            if (save) {
                rastreoService.procesarResultados(conn, analisisDto, lstGroupTerminos, lstTerminos);
            }
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            ConexionBBDD.desconectar(conn);
        }
    }

    /**
     * Inicio del procesado del contenido de texto.
     * <p/>
     * 1- Recuperamos los terminos compuestos : ArrayList[TerminoDto]
     * lstTerminosCompuestos = terminosService.findTerminosCompuestos();
     * <p/>
     * 2- Sepatamos el texto por 'terminos' y las almacenamos en un array :
     * lstTerminos = util.splitText(texto, lstTerminosCompuestos);
     * <p/>
     * 3- Agrupamos los terminos, indicando las posiciones donde aparece :
     * lstGroupTerminos = util.groupingTerms(lstTerminos);
     * <p/>
     * 4- Seleccionamos los terminos contenidos en este array que son
     * susceptibles de ser sexistas. : lstGroupTerminos =
     * terminosService.findTerminos(lstGroupTerminos);
     * <p/>
     * 5- Eliminamos del array aquellos terminos que cumplan alguna excepcion :
     * lstGroupTerminos = exceptionService.filterException(lstTerminos,
     * lstGroupTerminos);
     *
     * @param texto - Texto a procesar
     * @throws BusinessException BusinessException
     */
    protected void procesarContenido(Connection conn, String texto) throws BusinessException {

        // Recuperamos los terminos compuestos
        ArrayList<TerminoDto> lstTerminosCompuestos = terminosService.findTerminosCompuestos(conn);

        // 1- Separamos el texto por palabras y las almacenamos de forma
        // consecutiva en un array.
        lstTerminos = UtilProcessingText.splitText(texto, lstTerminosCompuestos);

        // 2- Agrupamos las distintas palabras que tenemos, indicando el termino
        // en sí y un array con las posiciones donde aparece.
        lstGroupTerminos = UtilProcessingText.groupingTerms(lstTerminos);

        // 3- Seleccionamos los terminos contenidos en este array que son
        // susceptibles de ser sexistas.
        lstGroupTerminos = terminosService.findTerminos(conn, lstGroupTerminos);

        // 4- Eliminamos del array aquellos terminos que cumplan alguna
        // excepcion
        lstGroupTerminos = exceptionService.filterException(conn, lstTerminos, lstGroupTerminos);
    }

    /**
     * Inicializaciones de objetos necesarios. crono = new Crono();
     * <p/>
     * terminosService = new TerminosService(); terminosService.setDao(new
     * TerminosDao());
     * <p/>
     * exceptionService = new ExceptionService();
     * exceptionService.setGlobalExcepcionDao(new GlobalExcepcionDao());
     * exceptionService.setLocalExcepcionDao(new LocalExcepcionDao());
     * <p/>
     * rastreoService = new RastreoService(); rastreoService.setDao(new
     * RastreoDao());
     * <p/>
     * resultadoService = new ResultadoService(); resultadoService.setDao(new
     * ResultadoDao());
     * <p/>
     * this.setUtil(new UtilProcessingText());
     * this.setTerminosService(terminosService);
     * this.setExceptionService(exceptionService);
     */
    private void init() {

        crono = new Crono();

        terminosService = new TerminosService();
        terminosService.setDao(new TerminosDao());

        exceptionService = new ExceptionService();

        this.setTerminosService(terminosService);
        this.setExceptionService(exceptionService);

        resultadoService = new ResultadoService();
        resultadoService.setDao(ResultadoDao.getInstancia());

        rastreoService = new RastreoService();
        rastreoService.setDao(RastreoDao.getInstancia());
        rastreoService.setResultadoService(resultadoService);

        this.setRastreoService(rastreoService);
        this.setResultadoService(resultadoService);

    }

    /**
     * Obtiene el valor del campo terminosService.<br>
     *
     * @return el campo terminosService
     */
    public TerminosService getTerminosService() {
        return terminosService;
    }

    /**
     * Obtiene el valor del campo exceptionService.<br>
     *
     * @return el campo exceptionService
     */
    public ExceptionService getExceptionService() {
        return exceptionService;
    }

    /**
     * Obtiene el valor del campo rastreoService.<br>
     *
     * @return el campo rastreoService
     */
    public RastreoService getRastreoService() {
        return rastreoService;
    }

    /**
     * Obtiene el valor del campo resultadoService.<br>
     *
     * @return el campo resultadoService
     */
    public ResultadoService getResultadoService() {
        return resultadoService;
    }

    /**
     * Obtiene el valor del campo crono.<br>
     *
     * @return el campo crono
     */
    public Crono getCrono() {
        return crono;
    }

    /**
     * Obtiene el valor del campo lstGroupTerminos.<br>
     *
     * @return el campo lstGroupTerminos
     */
    public ArrayList<TerminoPositionDto> getLstGroupTerminos() {
        return lstGroupTerminos;
    }

    /**
     * Obtiene el valor del campo lstTerminos.<br>
     *
     * @return el campo lstTerminos
     */
    public ArrayList<String> getLstTerminos() {
        return lstTerminos;
    }

    /**
     * Fija el valor para el campo terminosService.<br>
     *
     * @param terminosService el vlor de terminosService a fijar
     */
    public void setTerminosService(TerminosService terminosService) {
        this.terminosService = terminosService;
    }

    /**
     * Fija el valor para el campo exceptionService.<br>
     *
     * @param exceptionService el vlor de exceptionService a fijar
     */
    public void setExceptionService(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    /**
     * Fija el valor para el campo rastreoService.<br>
     *
     * @param rastreoService el vlor de rastreoService a fijar
     */
    public void setRastreoService(RastreoService rastreoService) {
        this.rastreoService = rastreoService;
    }

    /**
     * Fija el valor para el campo resultadoService.<br>
     *
     * @param resultadoService el vlor de resultadoService a fijar
     */
    public void setResultadoService(ResultadoService resultadoService) {
        this.resultadoService = resultadoService;
    }

    /**
     * Fija el valor para el campo crono.<br>
     *
     * @param crono el vlor de crono a fijar
     */
    public void setCrono(Crono crono) {
        this.crono = crono;
    }

    /**
     * Fija el valor para el campo lstGroupTerminos.<br>
     *
     * @param lstGroupTerminos el vlor de lstGroupTerminos a fijar
     */
    public void setLstGroupTerminos(
            ArrayList<TerminoPositionDto> lstGroupTerminos) {
        this.lstGroupTerminos = lstGroupTerminos;
    }

    /**
     * Fija el valor para el campo lstTerminos.<br>
     *
     * @param lstTerminos el vlor de lstTerminos a fijar
     */
    public void setLstTerminos(ArrayList<String> lstTerminos) {
        this.lstTerminos = lstTerminos;
    }

}
