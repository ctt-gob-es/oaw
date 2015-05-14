package es.inteco.rastreador2.lenox.service;

import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.core.exception.BusinessException;
import es.inteco.crawler.sexista.modules.analisis.dto.ResultsByUrlDto;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.lenox.constants.CommonsConstants;
import es.inteco.rastreador2.lenox.dao.InformesDao;
import es.inteco.rastreador2.lenox.dto.DetalleDto;
import es.inteco.rastreador2.lenox.dto.TerminoDetalleDto;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase InformesService.
 * Servicio de Informes
 *
 * @author psanchez
 */

public class InformesService {

    /**
     * M&eacute;todo obtenerDetalle.
     * Obtiene el campo detalle a partir de un dto de búsqueda
     *
     * @param dto dto de búsqueda
     * @return DetalleDto
     * @throws BusinessException BusinessException
     */
    public DetalleDto obtenerDetalle(DetalleDto dto, int page) throws BusinessException {

        if (null == dto) {
            throw new BusinessException(CommonsConstants.ERROR_DETALLEDTO_NULO);
        }

        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();

            InformesDao dao = new InformesDao();

            dto.setRastreo(dao.searchRastreoDetalle(conn, dto));

            List<TerminoDetalleDto> detalleTerminos = new ArrayList<TerminoDetalleDto>();

            List<Integer> ids = dao.searchIdTerminosDetalle(conn, dto, page);
            for (Object o : ids) {
                try {
                    Integer i = (Integer) o;

                    TerminoDetalleDto tdto = new TerminoDetalleDto();

                    tdto.setIdTermino(i);
                    tdto.setIdRastreo(dto.getRastreo().getIdRastreo());
                    tdto = dao.searchTerminoDetalle(conn, tdto, dto.getUrl(), dto.getGravedad());
                    tdto.setContextos(dao.searchContextosTerminoDetalle(conn, tdto, dto.getUrl(), dto.getGravedad()));
                    tdto.setAlternativas(dao.searchAlternativasTerminoDetalle(conn, tdto));
                    detalleTerminos.add(tdto);
                } catch (Exception e) {
                    Logger.getLogger(InformesService.class).error("Error al obtener el detalle del término");
                }
            }

            dto.setDetalleTerminos(detalleTerminos);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }

        return dto;
    }

    public List<ResultsByUrlDto> getResultsByUrl(Long idRastreo, int pagina) throws BusinessException {
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();

            return new InformesDao().getResultsByUrl(conn, idRastreo, pagina);
        } catch (Exception e) {
            throw new BusinessException(e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }
}
