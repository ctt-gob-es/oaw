package es.inteco.rastreador2.action.export.database;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.database.export.ExportMultilanguageUtils;
import es.inteco.multilanguage.manager.ObservatoryManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.manager.BaseManager;
import es.inteco.rastreador2.manager.export.database.DatabaseExportManager;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.export.database.DatabaseExportUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class DatabaseExportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        if (CrawlerUtils.hasAccess(request, "export.observatory.results")) {
            try {
                if (request.getParameter(Constants.ACTION) != null) {
                    if (request.getParameter(Constants.ACTION).equals(Constants.EXPORT)) {
                        return export(mapping, form, request, response);
                    } else if (request.getParameter(Constants.ACTION).equals(Constants.CONFIRM)) {
                        return confirm(mapping, form, request, response);
                    }
                }
            } catch (Exception e) {
                CrawlerUtils.warnAdministrators(e, this.getClass());
                return mapping.findForward(Constants.ERROR_PAGE);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }

        return null;
    }

    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();

            List<ObservatorioRealizadoForm> fulfilledObservatories = ObservatorioDAO.getFulfilledObservatories(c, idObservatory, Constants.NO_PAGINACION, null);

            for (ObservatorioRealizadoForm fulfilledObservatory : fulfilledObservatories) {
                PropertiesManager pmgr = new PropertiesManager();

                if (String.valueOf(fulfilledObservatory.getCartucho().getId()).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
                    Observatory observatory = DatabaseExportManager.getObservatory(fulfilledObservatory.getId());
                    if (observatory == null) {
                        // Información general de la ejecución del Observatorio
                        observatory = DatabaseExportUtils.getObservatoryInfo(request, fulfilledObservatory.getId());

                        List<CategoriaForm> categories = ObservatorioDAO.getObservatoryCategories(c, idObservatory);
                        for (CategoriaForm categoriaForm : categories) {
                            Category category = DatabaseExportUtils.getCategoryInfo(request, categoriaForm, observatory);
                            observatory.getCategoryList().add(category);
                        }

                        ObservatorioRealizadoForm observatorioRealizadoForm = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, fulfilledObservatory.getId());

                        observatory.setName(observatorioRealizadoForm.getObservatorio().getNombre());
                        observatory.setDate(new Timestamp(observatorioRealizadoForm.getFecha().getTime()));

                        BaseManager.save(observatory);
                    }
                } else if (String.valueOf(fulfilledObservatory.getCartucho().getId()).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
                    es.inteco.multilanguage.persistence.Observatory observatory = ObservatoryManager.getMultilanguageObservatory(fulfilledObservatory.getId());

                    if (observatory == null) {
                        List<FulFilledCrawling> seedsResults = ObservatorioDAO.getFulfilledCrawlingByObservatoryExecution(c, fulfilledObservatory.getId());
                        Map<Long, List<Long>> crawlerIdsMap = new HashMap<Long, List<Long>>();
                        Map<Long, String> categoryNames = new HashMap<Long, String>();
                        Map<Long, String> siteNames = new HashMap<Long, String>();
                        for (FulFilledCrawling crawler : seedsResults) {
                            List<Long> crawlerIds = new ArrayList<Long>();
                            if (crawlerIdsMap.get(Long.valueOf(crawler.getSeed().getCategoria().getId())) != null) {
                                crawlerIds = crawlerIdsMap.get(Long.valueOf(crawler.getSeed().getCategoria().getId()));
                            }
                            crawlerIds.add(Long.valueOf(crawler.getId()));
                            crawlerIdsMap.put(Long.valueOf(crawler.getSeed().getCategoria().getId()), crawlerIds);

                            //Guardamos los nombres de las categorias y portales para mostrarlos en las tablas de exportación del multilingüismo
                            siteNames.put(Long.valueOf(crawler.getId()), crawler.getSeed().getNombre());
                            if (categoryNames.get(Long.valueOf(crawler.getSeed().getCategoria().getId())) == null) {
                                categoryNames.put(Long.valueOf(crawler.getSeed().getCategoria().getId()), crawler.getSeed().getCategoria().getName());
                            }
                        }
                        ExportMultilanguageUtils.exportObservatoryMultilanguageInfo(crawlerIdsMap, categoryNames, siteNames, fulfilledObservatory.getId(), fulfilledObservatory.getObservatorio().getNombre(), fulfilledObservatory.getFecha());
                    }
                }
            }

        } catch (Exception e) {
            Logger.putLog("Error al exportar los resultados del observatorio: ", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        PropertiesManager pmgr = new PropertiesManager();
        String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.observatorio.resultados.exportados");
        String volver = pmgr.getValue("returnPaths.properties", "volver.carga.observatorio");
        request.setAttribute("mensajeExito", mensaje);
        request.setAttribute("accionVolver", volver);
        return mapping.findForward(Constants.EXITO);
    }

    public ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
            request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.CONFIRM);
    }
}