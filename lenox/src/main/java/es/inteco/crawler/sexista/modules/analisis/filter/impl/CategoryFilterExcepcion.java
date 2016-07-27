package es.inteco.crawler.sexista.modules.analisis.filter.impl;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dao.CategoryExcepcionDao;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;
import es.inteco.crawler.sexista.modules.analisis.filter.FilterExcepcion;
import es.inteco.crawler.sexista.modules.analisis.filter.base.BaseFilterExcepcion;
import es.inteco.crawler.sexista.modules.commons.Constants.AnalyzeConstants;
import es.inteco.crawler.sexista.modules.commons.dto.CategoryExcepcionDto;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CategoryFilterExcepcion extends BaseFilterExcepcion implements
        FilterExcepcion {

    /**
     * Campo categoryExcepcionDao.
     */
    private CategoryExcepcionDao categoryExcepcionDao;

    public void filterException(Connection conn, TerminoPositionDto terminoPositionDto,
                                List<String> lstTerminos) throws BusinessException {

        // Recuperamos las excepciones de category para un termino en particular
        List<CategoryExcepcionDto> lstExcep = this
                .getCategoryExcepcionDao().find(conn,
                        terminoPositionDto.getIdCategoria());

        // Termino de la excepcion
        String exception;

        // Posicion de la excepcion
        Integer position;

        // Guardamos el listado en un listado, con las keys en mayusculas y sin
        // espacios finales
        List<String> lstBeforeExcepcion = new ArrayList<>();
        List<String> lstAfterExcepcion = new ArrayList<>();

        for (CategoryExcepcionDto dto : lstExcep) {
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
     * @return the categoryExcepcionDao
     */
    public CategoryExcepcionDao getCategoryExcepcionDao() {
        if (categoryExcepcionDao == null) {
            categoryExcepcionDao = new CategoryExcepcionDao();
        }
        return categoryExcepcionDao;
    }

    /**
     * @param categoryExcepcionDao the categoryExcepcionDao to set
     */
    public void setCategoryExcepcionDao(CategoryExcepcionDao categoryExcepcionDao) {
        this.categoryExcepcionDao = categoryExcepcionDao;
    }

}
