package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.core.util.DAOUtils;
import es.inteco.crawler.sexista.modules.analisis.dao.RastreoDao;
import es.inteco.crawler.sexista.modules.analisis.dto.AnalisisDto;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.commons.dto.RastreoDto;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import es.inteco.crawler.sexista.modules.commons.util.UtilProcessingText;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase RastreoService.
 *
 * @author jbernal
 */
public class RastreoService {

    /**
     * Logger.
     */
    private Logger log = Logger.getRootLogger();

    /**
     * Campo dao.
     */
    private RastreoDao dao = null;

    /**
     * Servicio de Resultados.
     */
    private ResultadoService resultadoService = null;


    /**
     * Obtiene el valor del campo log.<br>
     *
     * @return el campo log
     */
    public Logger getLog() {
        return log;
    }

    /**
     * Fija el valor para el campo log.<br>
     *
     * @param log el vlor de log a fijar
     */
    public void setLog(Logger log) {
        this.log = log;
    }

    /**
     * Obtiene el valor del campo dao.<br>
     *
     * @return el campo dao
     */
    public RastreoDao getDao() {
        return dao;
    }

    /**
     * Fija el valor para el campo dao.<br>
     *
     * @param dao el vlor de dao a fijar
     */
    public void setDao(RastreoDao dao) {
        this.dao = dao;
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
     * Fija el valor para el campo resultadoService.<br>
     *
     * @param resultadoService el vlor de resultadoService a fijar
     */
    public void setResultadoService(ResultadoService resultadoService) {
        this.resultadoService = resultadoService;
    }


    /**
     * Eliminamos el rastreo.
     *
     * @param rastreoDto - Dto con el idetificador del rastreo a eliminar
     * @param con        - Conexion a la BD
     * @throws BusinessException BusinessException
     */
    public void deleteRastreo(RastreoDto rastreoDto, Connection con) throws BusinessException {
        dao.delete(rastreoDto, con);
    }

    /**
     * Buscamos un rastreo.
     *
     * @param rastreoDto - Datos con los que se tiene que realizar la busqueda del rastreo
     * @param con        - Conexion a la BD
     * @return - Retornamos un listado de los rastreo encontrados
     * @throws BusinessException BusinessException
     */
    public List<RastreoDto> findRastreo(RastreoDto rastreoDto, Connection con)
            throws BusinessException {
        return dao.find(rastreoDto, con);
    }

    /**
     * Insertamos el rastreo si es necesario.
     *
     * @param rastreoDto - Datos del rastreo
     * @param con        - Conexion a la BD
     * @throws BusinessException BusinessException
     */
    public void insertRastreo(RastreoDto rastreoDto, Connection con) throws BusinessException {
        RastreoDto auxDto = new RastreoDto();

        //Anñadimos el id del rastreo al dto auxiliar
        auxDto.setIdRastreo(rastreoDto.getIdRastreo());

        //Buscamos el rastreo
        List<RastreoDto> lista = findRastreo(rastreoDto, con);

        //SI existe, actualizamos tiempos de rastreo
        if (null != lista && lista.size() == 1) {

            //Sumamos el tiempo(Tiempo que esta en BD mas el tiempo que hemos tardado en procesar de nuevo
            long tiempo = lista.get(0).getTiempo() + rastreoDto.getTiempoCrono();

            //Añadimos el nuevo tiempo al dto auxiliar
            auxDto.setTiempo(tiempo);

            //Actualizamos el registro con el nuevo tiempo
            dao.update(auxDto, con);

        } else {
            //SI NO, insertamos el nuevo rastreo
            dao.insert(rastreoDto, con);
        }
    }

    /**
     * M&eacute;todo procesarResultados.
     * Procesa los resultados obtenidos de la evaluación y los inserta
     * en las diferentes tablas de BD
     *
     * @param analisisDto      analisisDto
     * @param lstGroupTerminos Lista de terminos encontrados
     * @param lstTerminos      Lista de terminos encontrados
     * @throws BusinessException BusinessException
     */
    public void procesarResultados(Connection conn, AnalisisDto analisisDto,
                                   ArrayList<TerminoPositionDto> lstGroupTerminos, ArrayList<String> lstTerminos)
            throws BusinessException {
        ResultadoDto resultadoDto;

        try {
            //Comprobamos que contengan valor los listados
            if (null != lstGroupTerminos && null != lstTerminos) {
                //Creamos el objeto rastreoDto de insercion en rastreos
                RastreoDto rastreoDto = new RastreoDto();
                rastreoDto.setIdRastreo(analisisDto.getIdRastreo());
                rastreoDto.setUsuario(analisisDto.getUsuario());
                rastreoDto.setFecha(analisisDto.getFecha());
                // rastreoDto.setTiempo(crono.getTime());

                //Insertamos el rastreoDto
                insertRastreo(rastreoDto, conn);

                //SI no se encuentran terminos, insertamos con 'termino vacio'
                if (lstGroupTerminos.size() == 0) {
                    resultadoDto = new ResultadoDto();

                    //Cargamos los datos generales del resultado
                    resultadoDto.setIdRastreo(analisisDto.getIdRastreo());
                    resultadoDto.setUrlTermino(analisisDto.getUrl());

                    //Insertamos el resultado SIN termino ni contexto
                    resultadoService.insertResultado(resultadoDto, conn);
                }

                //POR CADA termino del listado lstGroupTerminos
                for (TerminoPositionDto terminoPositionDto : lstGroupTerminos) {

                    resultadoDto = new ResultadoDto();

                    //Cargamos los datos generales del resultado
                    resultadoDto.setIdRastreo(analisisDto.getIdRastreo());
                    resultadoDto.setUrlTermino(analisisDto.getUrl());
                    resultadoDto.setIdTermino(terminoPositionDto.getIdTermino());
                    resultadoDto.setInSingular(terminoPositionDto.isFoundInSingular());

                    //Obtenemos el contexto del termino para cada posicion dada
                    for (Integer posicion : terminoPositionDto.getPositions()) {

                        //Añadimos el contexto
                        resultadoDto.setContexto(UtilProcessingText.obtenerContexto(posicion, lstTerminos));

                        //Insertamos el resultado
                        resultadoService.insertResultado(resultadoDto, conn);
                    }
                }
            }
        } catch (BusinessException e) {
            getLog().error(e.getMessage());

            //Eliminamos los resultados pertenecientes al rastreo actual y a la url analizada.
            resultadoDto = new ResultadoDto();
            resultadoDto.setIdRastreo(analisisDto.getIdRastreo());
            resultadoDto.setUrlTermino(analisisDto.getUrl());

            //Aqui no le pasamos la conexion porque raramente se borrará
            resultadoService.deleteResultado(resultadoDto);

            //Volvemos a lanzar la excepcion para informar del error
            throw new BusinessException(e);
        } finally {
            DAOUtils.close(null, null, null);
        }
    }

}
