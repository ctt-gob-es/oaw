package es.inteco.rastreador2.lenox.action;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.modules.analisis.dto.ResultsByUrlDto;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.action.AnalysisFromCrawlerAction;
import es.inteco.rastreador2.lenox.dao.InformesDao;
import es.inteco.rastreador2.lenox.dto.DetalleDto;
import es.inteco.rastreador2.lenox.dto.RastreoExtDto;
import es.inteco.rastreador2.lenox.form.InformesSearchForm;
import es.inteco.rastreador2.lenox.service.InformesService;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase InformesDispatchAction.
 * Action de Informes
 *
 * @author psanchez
 */
public class InformesDispatchAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CrawlerUtils.hasAccess(request, "show.crawler.results")) {
            Connection c = null;
            try {
                c = DataBaseManager.getConnection();
                String user = (String) request.getSession().getAttribute(Constants.USER);
                long idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
                //Comprobamos que el usuario esta asociado con los resultados de los rastreos que quiere recuperar
                if (RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                    if (request.getParameter(Constants.ACCION).equals("resultsByUrl")) {
                        return getResultsByUrl(mapping, form, request, response);
                    } else if (request.getParameter(Constants.ACCION).equals("urlDetail")) {
                        return getUrlDetail(mapping, form, request, response);
                    }
                }
            } catch (Exception e) {
                Logger.putLog("Excepcion: ", AnalysisFromCrawlerAction.class, Logger.LOG_LEVEL_ERROR, e);
                return mapping.findForward(Constants.ERROR);
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }

        return null;
    }

    public ActionForward getResultsByUrl(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();
        Connection c = null;

        String Observatory = request.getParameter(Constants.ID_OBSERVATORIO);
        if (Observatory != null) {
            request.setAttribute(Constants.ID_OBSERVATORIO, Observatory);
        }

        try {
            c = DataBaseManager.getConnection();

            long idExecution = Long.parseLong(request.getParameter(Constants.ID));

            int numResults = InformesDao.countResultsByUrl(c, idExecution);
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            InformesService informesService = new InformesService();
            List<ResultsByUrlDto> results = informesService.getResultsByUrl(idExecution, (pagina - 1));
            request.setAttribute(Constants.RESULTS_BY_URL, results);
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResults, pagina));

            return mapping.findForward(Constants.RESULTS_BY_URL);
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", AnalysisFromCrawlerAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    public ActionForward getUrlDetail(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response) throws Exception {

        InformesSearchForm informesSearchForm = (InformesSearchForm) form;

        Connection c = null;
        Connection conn = null;
        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();


            conn = DataBaseManager.getConnection();
            long idExecution = Long.parseLong(request.getParameter(Constants.ID));
            String url = request.getParameter(Constants.URL);

            DetalleDto detalleDto = new DetalleDto();
            detalleDto.setUrl(url);
            detalleDto.setRastreo(new RastreoExtDto());
            detalleDto.getRastreo().setIdRastreo(idExecution);
            detalleDto.setGravedad(informesSearchForm.getPriority());

            InformesService informesService = new InformesService();

            int numResult = InformesDao.countDetail(conn, detalleDto);
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            DetalleDto detalleDtoObtenido = informesService.obtenerDetalle(detalleDto, (pagina - 1));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pmgr.getValue(CRAWLER_PROPERTIES, "lenox.report.pagination"), pagina, Constants.PAG_PARAM));

            try {
                detalleDtoObtenido.getRastreo().setEntidad(RastreoDAO.getFullfilledCrawlingEntityName(c, idExecution));
            } catch (Exception e) {
                Logger.putLog("Error al recuperar el nombre de la entidad", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            }

            request.setAttribute(Constants.DETALLE_RASTREO_LENOX, detalleDtoObtenido);
            request.setAttribute(Constants.LENOX_MAX_CONTEXT, pmgr.getValue(CRAWLER_PROPERTIES, "max.lenox.context"));

            return mapping.findForward(Constants.URL_DETAIL);

        } catch (Exception e) {
            Logger.putLog("Excepcion: ", AnalysisFromCrawlerAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(conn);
        }
    }
}
