package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dao.TerminosDao;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.commons.dto.TerminoDto;
import es.inteco.crawler.sexista.modules.commons.util.UtilCalculateTerms;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Servicio encargado de buscar los terminos que aparecen en el texto y son susceptibles de ser sexistas.
 */
public class TerminosService {
    /**
     * Campo dao.
     */
    private TerminosDao dao = null;

    /**
     * Recuperamos los terminos compuestos de BBDD.
     * <p/>
     * 1- Invocamos el metodo del dao findCompuestos
     * : return dao.findCompuestos();
     *
     * @return ArrayList[TerminoDto] - Listado de terminos encontrados
     * @throws BusinessException BusinessException
     */
    public ArrayList<TerminoDto> findTerminosCompuestos(Connection conn) throws BusinessException {
        return dao.findCompuestos(conn);
    }

    /**
     * Buscamos el listado de terminos que aparezcan en el texto.
     * <p/>
     * 1- Definimos
     * : ArrayList[String] lstTerminosBusqueda = new ArrayList[String]();
     * <p/>
     * 2- PARA CADA, pasamos un array con el listado de terminos que queremos buscar
     * : for (TerminoPositionDto dto : lstGroupTerminos)
     * <p/>
     * 2.1- Añadimos el termino al listado de busqueda
     * : lstTerminosBusqueda.add(dto.getTermino());
     * <p/>
     * 2.2- Añadimos el singular del termino al listado de busqueda
     * : lstTerminosBusqueda.add(util.calculateSingular(dto.getTermino()));
     * <p/>
     * FIN PARA CADA (del punto 2)
     * <p/>
     * 3- SI el listado de terminos es mayor que 0, realizamos la consulta
     * <p/>
     * 3.1- Recuperamos los terminos susceptibles
     * : ArrayList[TerminoDto] lst = dao.find(lstTerminosBusqueda);
     * <p/>
     * 3.2- Retornamos los terminos encontrados
     * : return createPositionTerminosDto(lstGroupTerminos, lst);
     * <p/>
     * FIN SI (del punto 3)
     * <p/>
     * 4- Retornamos el listado vacio si no tenemos terminos que consultar
     * : return new ArrayList[TerminoPositionDto]();
     *
     * @param lstGroupTerminos - Listado del grupo de terminos a localizar
     * @return ArrayList[TerminoPositionDto] - Listado de terminos encontrados
     * @throws BusinessException BusinessException
     */
    public ArrayList<TerminoPositionDto> findTerminos(Connection conn, ArrayList<TerminoPositionDto> lstGroupTerminos)
            throws BusinessException {

        ArrayList<String> lstTerminosBusqueda = new ArrayList<>();

        //Pasamos un array con el listado de terminos que queremos buscar
        for (TerminoPositionDto dto : lstGroupTerminos) {
            if (!lstTerminosBusqueda.contains(dto.getTermino())) {
                //Añadimos el termino al listado de busqueda
                lstTerminosBusqueda.add(dto.getTermino());
                //Añadimos el singular del termino al listado de busqueda
                String enSingular = UtilCalculateTerms.calculateSingular(dto.getTermino());
                if (!dto.getTermino().equalsIgnoreCase(enSingular)) {
                    lstTerminosBusqueda.add(UtilCalculateTerms.calculateSingular(dto.getTermino()));
                }
            }
        }

        //Si el listado de terminos es mayor que 0, realizamos la consulta
        if (lstTerminosBusqueda.size() > 0) {

            //Recuperamos los terminos susceptibles
            ArrayList<TerminoDto> lst = dao.find(conn, lstTerminosBusqueda);

            //Retornamos los terminos encontrados
            return createPositionTerminosDto(lstGroupTerminos, lst);
        }

        //Retornamos el listado vacio si no tenemos terminos que consultar
        return new ArrayList<>();
    }

    /**
     * El metodo retorna un listado de TerminoPositionDto, que contiene los datos del termino susceptible de ser sexista (dato recuperado
     * de la tabla 'termino' de BBDD) y un parametro adicional (de tipo listado) que nos indica las posiciones en las que podemos encontrarlo
     * en el listado principal.
     * <p/>
     * 1- Definimos
     * : String termino = "";
     * : String auxTermino = "";
     * : ArrayList[TerminoPositionDto] lstGroupTerms = new ArrayList[TerminoPositionDto]();
     * <p/>
     * 2- PARA CADA, recorremos el listado de terminos encontrados
     * : for (TerminoDto terminoDto : lstTerminos)
     * <p/>
     * 2.1- El termino susceptible de ser sexista
     * : termino = terminoDto.getTermino().trim();
     * <p/>
     * 2.2- PARA CADA, Recorremos el listado donde se indica las posiciones que ocupan cada termino
     * : for (TerminoPositionDto terminoPositionDto : lstGroupTerminos) {
     * <p/>
     * 2.2.1- Recuperamos el terminos para comprobar cual es el que estamos tratando
     * : auxTermino = terminoPositionDto.getTermino().trim();
     * <p/>
     * 2.2.2- SI nos encontramos ante el mismo termino, le asignamos los valores del termino
     * : if(termino.toUpperCase().equals(auxTermino) || util.calculatePlural(termino).toUpperCase().equals(auxTermino))
     * <p/>
     * 2.2.2.1- Seteamos los valores
     * : terminoPositionDto.setIdTermino(terminoDto.getIdTermino());
     * : terminoPositionDto.setTermino(terminoDto.getTermino());
     * : terminoPositionDto.setUsaGlobalException(terminoDto.isUsaGlobalException());
     * : terminoPositionDto.setFemenino(terminoDto.getFemenino());
     * : terminoPositionDto.setPlural(terminoDto.getPlural());
     * : terminoPositionDto.setFemeninoPlural(terminoDto.getFemeninoPlural());
     * <p/>
     * 2.2.2.2- Añadimos el termino con su posicion en un listado
     * : lstGroupTerms.add(terminoPositionDto);
     * <p/>
     * FIN SI (del punto 2.2.2)
     * <p/>
     * FIN PARA CADA (del punto 2.2)
     * <p/>
     * FIN PARA CADA (del punto 2)
     * <p/>
     * 3- Retornamos el listado de terminos agrupados
     * : return lstGroupTerms;
     *
     * @param lstGroupTerminos - Listado de terminos y posiciones de cada uno los que aparecen en el texto
     * @param lstTerminos      - Listado de terminos encontrados en la base de datos que son susceptibles de ser sexistas
     * @return ArrayList[TerminoPositionDto], con el listado de terminosDto (con todos los dato cargados) y sus posiciones en el texto.
     */
    private ArrayList<TerminoPositionDto> createPositionTerminosDto(
            ArrayList<TerminoPositionDto> lstGroupTerminos, ArrayList<TerminoDto> lstTerminos) {

        String termino = "";
        String auxTermino = "";

        ArrayList<TerminoPositionDto> lstGroupTerms = new ArrayList<>();

        //Recorremos el listado de terminos encontrados
        for (TerminoDto terminoDto : lstTerminos) {

            //El termino susceptible de ser sexista
            termino = terminoDto.getTermino().trim();

            //Recorremos el listado donde se indica las posiciones que ocupan cada termino
            for (TerminoPositionDto terminoPositionDto : lstGroupTerminos) {

                //Recuperamos el terminos para comprobar cual es el que estamos tratando
                auxTermino = terminoPositionDto.getTermino().trim();

                //Si nos encontramos ante el mismo termino, le asignamos los valores del termino
                if (termino.toUpperCase().equals(auxTermino)
                        || UtilCalculateTerms.calculatePlural(termino).toUpperCase().equals(auxTermino)) {

                    terminoPositionDto.setIdTermino(terminoDto.getIdTermino());
                    terminoPositionDto.setTermino(terminoDto.getTermino());
                    terminoPositionDto.setUsaGlobalException(terminoDto.isUsaGlobalException());
                    terminoPositionDto.setFemenino(terminoDto.getFemenino());
                    terminoPositionDto.setPlural(terminoDto.getPlural());
                    terminoPositionDto.setFemeninoPlural(terminoDto.getFemeninoPlural());
                    terminoPositionDto.setFormaUnica(terminoDto.isFormaUnica());
                    terminoPositionDto.setPrioridadPlural(terminoDto.getPrioridadPlural());
                    terminoPositionDto.setPrioridadSingular(terminoDto.getPrioridadSingular());

                    //Indicamos si el termino lo encontramos en singular
                    terminoPositionDto.setFoundInSingular(termino.toUpperCase().equals(auxTermino));
                    terminoPositionDto.setIdCategoria(terminoDto.getIdCategoria());

                    //Añadimos el termino con su posicion en un listado
                    lstGroupTerms.add(terminoPositionDto);
                }
            }

        }

        return lstGroupTerms;
    }

    /**
     * Obtiene el valor del campo dao.<br>
     *
     * @return el campo dao
     */
    public TerminosDao getDao() {
        return dao;
    }

    /**
     * Fija el valor para el campo dao.<br>
     *
     * @param dao el vlor de dao a fijar
     */
    public void setDao(TerminosDao dao) {
        this.dao = dao;
    }


}
