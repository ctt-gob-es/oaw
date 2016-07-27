package es.inteco.crawler.sexista.modules.analisis.filter.impl;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dao.LocalExcepcionDao;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.analisis.filter.FilterExcepcion;
import es.inteco.crawler.sexista.modules.analisis.filter.base.BaseFilterExcepcion;
import es.inteco.crawler.sexista.modules.commons.Constants.AnalyzeConstants;
import es.inteco.crawler.sexista.modules.commons.dto.LocalExcepcionDto;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LocalFilterExcepcion extends BaseFilterExcepcion implements
        FilterExcepcion {

    /**
     * Campo localExcepcionDao.
     */
    private LocalExcepcionDao localExcepcionDao;

    public void filterException(Connection conn, TerminoPositionDto terminoPositionDto,
                                List<String> lstTerminos) throws BusinessException {

        // Recuperamos las excepciones locales para un termino en particular
        List<LocalExcepcionDto> lstExcep = this.getLocalExcepcionDao()
                .find(conn, terminoPositionDto.getIdTermino());

        // Termino de la excepcion
        String exception;

        // Posicion de la excepcion
        Integer position;

        // Guardamos el listado en un listado, con las keys en mayusculas y sin
        // espacios finales
        List<String> lstBeforeExcepcion = new ArrayList<>();
        List<String> lstAfterExcepcion = new ArrayList<>();

        for (LocalExcepcionDto dto : lstExcep) {
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
     * @return the localExcepcionDao
     */
    public LocalExcepcionDao getLocalExcepcionDao() {
        if (localExcepcionDao == null) {
            localExcepcionDao = new LocalExcepcionDao();
        }
        return localExcepcionDao;
    }

    /**
     * @param localExcepcionDao the localExcepcionDao to set
     */
    public void setLocalExcepcionDao(LocalExcepcionDao localExcepcionDao) {
        this.localExcepcionDao = localExcepcionDao;
    }

}
