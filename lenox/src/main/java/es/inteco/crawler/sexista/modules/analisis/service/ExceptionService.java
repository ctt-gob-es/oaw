package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.analisis.filter.FilterExcepcion;
import es.inteco.crawler.sexista.modules.analisis.filter.impl.*;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * Servicio de excepciones.
 * <p/>
 * Controla el uso de las mismas
 */
public class ExceptionService {

    /**
     * Exception local
     */
    private FilterExcepcion localException;
    /**
     * Exception de categoría
     */
    private FilterExcepcion categoryException;
    /**
     * Exception de global
     */
    private FilterExcepcion globalException;
    /**
     * Exception de heurísticas
     */
    private FilterExcepcion heuristicException;
    /**
     * Exception de contexto
     */
    private FilterExcepcion contextException;

    /**
     * Filtramos los terminos que tenemos en el listado por las excepciones
     * posibles.
     * <p/>
     * En el caso de que un termino (que se encuentre en una determinada
     * posicion del contexto final) cumpla una excepcion, se eliminará del
     * listado (solo el de la posición que cumpla la excepcion)
     * <p/>
     * 1- PARA CADA termino (de lstGroupTerminos), comprobamos las excepciones
     * posibles : for (int index = 0; index < lstGroupTerminos.size(); index ++)
     * <p/>
     * 1.1- Filtramos las excepciones locales :
     * filterLocalException(terminoPositionDto, lstTerminos);
     * <p/>
     * 1.2- Filtramos las excepciones globales :
     * filterGlobalException(terminoPositionDto, lstTerminos);
     * <p/>
     * 1.3- Filtramos las excepciones de contexto :
     * filterContextException(terminoPositionDto, lstTerminos);
     * <p/>
     * 1.4- Si no quedan posiciones en el termino :
     * if(terminoPositionDto.getPositions().size() == 0)
     * <p/>
     * 1.4.1- Eliminamos el dto del listado :
     * lstGroupTerminos.remove(terminoPositionDto);
     * <p/>
     * 1.4.2- Decrementamos el contador del bucle : index--;
     * <p/>
     * FIN PARA CADA (del punto 1)
     * <p/>
     * 2- Retornamos el listado final : return lstGroupTerminos;
     *
     * @param lstTerminos      - Listado de String con los terminos del texto analizado
     * @param lstGroupTerminos - Listado de terminos con los datos del diccionario y la
     *                         posicion en el texto
     * @return ArrayList[TerminoPositionDto] - Listado de terminos sexistas
     * @throws BusinessException BusinessException
     */
    public ArrayList<TerminoPositionDto> filterException(
            Connection conn,
            ArrayList<String> lstTerminos,
            ArrayList<TerminoPositionDto> lstGroupTerminos)
            throws BusinessException {

        TerminoPositionDto terminoPositionDto = null;

        // Recorremos termino a termino, comprobando excepciones
        for (int index = 0; index < lstGroupTerminos.size(); index++) {

            terminoPositionDto = lstGroupTerminos.get(index);

            // Filtramos las excepciones locales
            this.getLocalException().filterException(conn, terminoPositionDto,
                    lstTerminos);

            // Filtramos las excepciones por categorías
            this.getCategoryException().filterException(conn, terminoPositionDto,
                    lstTerminos);

            // Filtramos las excepciones globales
            this.getGlobalException().filterException(conn, terminoPositionDto,
                    lstTerminos);

            //Filtramos las excepciones heuristicas
            this.getHeuristicException().filterException(conn, terminoPositionDto, lstTerminos);

            // NOTA: campo Usa global exception abarca mas ambito que sólo las
            // excepciones globales
            if (terminoPositionDto.isUsaGlobalException()) {
                // Filtramos las excepciones de contexto
                this.getContextException().filterException(conn, terminoPositionDto,
                        lstTerminos);
            }

            // Si no han quedado posiciones para el termino, eliminamos el dto
            // del listado
            if (terminoPositionDto.getPositions().size() == 0) {
                // Eliminamos el termino de listado
                lstGroupTerminos.remove(terminoPositionDto);
                // Retrasamos una posicion el indice de recorrido del listado
                index--;
            }
        }

        return lstGroupTerminos;
    }

    public FilterExcepcion getLocalException() {
        if (null == localException) {
            this.localException = new LocalFilterExcepcion();
        }
        return localException;
    }

    public void setLocalException(FilterExcepcion localException) {
        this.localException = localException;
    }

    public FilterExcepcion getCategoryException() {
        if (null == categoryException) {
            this.categoryException = new CategoryFilterExcepcion();
        }
        return categoryException;
    }

    public void setCategoryException(FilterExcepcion categoryException) {
        this.categoryException = categoryException;
    }

    public FilterExcepcion getGlobalException() {
        if (null == globalException) {
            this.globalException = new GlobalFilterExcepcion();
        }
        return globalException;
    }

    public void setGlobalException(FilterExcepcion globalException) {
        this.globalException = globalException;
    }

    public FilterExcepcion getContextException() {
        if (null == contextException) {
            this.contextException = new ContextFilterExcepcion();
        }
        return contextException;
    }

    public void setContextException(FilterExcepcion contextException) {
        this.contextException = contextException;
    }

    /**
     * Getter.
     *
     * @return the heuristicException
     */
    public FilterExcepcion getHeuristicException() {
        if (null == heuristicException) {
            this.heuristicException = new HeuristicFilterExcepcion();
        }
        return heuristicException;
    }

    /**
     * Setter.
     *
     * @param heuristicException the heuristicException to set
     */
    public void setHeuristicException(FilterExcepcion heuristicException) {
        this.heuristicException = heuristicException;
    }
}
