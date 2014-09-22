package es.inteco.crawler.sexista.modules.analisis.filter;

import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dto.TerminoPositionDto;

import java.sql.Connection;
import java.util.List;

public interface FilterExcepcion {

    public void filterException(Connection conn, TerminoPositionDto dto, List<String> lstTerminos) throws BusinessException;
}
