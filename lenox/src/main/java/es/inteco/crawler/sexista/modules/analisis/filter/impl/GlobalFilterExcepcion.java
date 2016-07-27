package es.inteco.crawler.sexista.modules.analisis.filter.impl;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dao.GlobalExcepcionDao;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.analisis.filter.FilterExcepcion;
import es.inteco.crawler.sexista.modules.analisis.filter.base.BaseFilterExcepcion;
import es.inteco.crawler.sexista.modules.commons.Constants.AnalyzeConstants;
import es.inteco.crawler.sexista.modules.commons.dto.GlobalExcepcionDto;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class GlobalFilterExcepcion extends BaseFilterExcepcion implements
        FilterExcepcion {

    /**
     * Campo globalExcepcionDao.
     */
    private GlobalExcepcionDao globalExcepcionDao;
    /**
     * Campo lstGlobalException.
     */
    private List<GlobalExcepcionDto> lstGlobalException;

    public void filterException(Connection conn, TerminoPositionDto terminoPositionDto,
                                List<String> lstTerminos) throws BusinessException {

        // Si no se aplican las excepciones globales, retornamos.
        if (!terminoPositionDto.isUsaGlobalException()) {
            return;
        }

        // Recuperamos las excepciones globales
        List<GlobalExcepcionDto> lstExcep = getGlobalException(conn);

        // Termino de la excepcion
        String exception;

        // Posicion de la excepcion
        Integer position;

        // Guardamos el listado en un listado, con las keys en mayusculas y sin
        // espacios finales
        ArrayList<String> lstBeforeExcepcion = new ArrayList<>();
        ArrayList<String> lstAfterExcepcion = new ArrayList<>();
        for (GlobalExcepcionDto dto : lstExcep) {
            exception = dto.getExcepcion().trim().toUpperCase();
            position = dto.getPosition();

            // Diferenciamos entre excepciones anteriores y posteriores
            if (position == AnalyzeConstants.WORD_EXCEPTION_BEFORE_POSITION) {
                lstBeforeExcepcion.add(exception);
            } else {
                lstAfterExcepcion.add(exception);
            }
        }

        checkPositionsArray(terminoPositionDto, lstTerminos,
                lstBeforeExcepcion, lstAfterExcepcion);
    }

    /**
     * Retornamos las excepciones globales.
     * <p/>
     * 1- Solo recuperamos las excepciones globales si no lo estan ya :
     * if(lstGlobalException == null){ : lstGlobalException =
     * globalExcepcionDao.find(); : }
     * <p/>
     * 2- Retornamos el listado de excepciones globales : return
     * lstGlobalException;
     *
     * @return ArrayList[GlobalExcepcionDto] - Con las excepciones que existen.
     * @throws BusinessException BusinessException
     */
    private List<GlobalExcepcionDto> getGlobalException(Connection conn)
            throws BusinessException {

        if (lstGlobalException == null) {
            lstGlobalException = this.getGlobalExcepcionDao().find(conn);
        }

        return lstGlobalException;
    }

    /**
     * @return the globalExcepcionDao
     */
    public GlobalExcepcionDao getGlobalExcepcionDao() {
        if (globalExcepcionDao == null) {
            globalExcepcionDao = new GlobalExcepcionDao();
        }
        return globalExcepcionDao;
    }

    /**
     * @param globalExcepcionDao the globalExcepcionDao to set
     */
    public void setGlobalExcepcionDao(GlobalExcepcionDao globalExcepcionDao) {
        this.globalExcepcionDao = globalExcepcionDao;
    }

    /**
     * @return the lstGlobalException
     */
    public List<GlobalExcepcionDto> getLstGlobalException() {
        return lstGlobalException;
    }

    /**
     * @param lstGlobalException the lstGlobalException to set
     */
    public void setLstGlobalException(List<GlobalExcepcionDto> lstGlobalException) {
        this.lstGlobalException = lstGlobalException;
    }

}
