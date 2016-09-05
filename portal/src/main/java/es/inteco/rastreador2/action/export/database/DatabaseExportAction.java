package es.inteco.rastreador2.action.export.database;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.manager.BaseManager;
import es.inteco.rastreador2.manager.export.database.DatabaseExportManager;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.export.database.DatabaseExportUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class DatabaseExportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (CrawlerUtils.hasAccess(request, "export.observatory.results")) {
            try {
                if (request.getParameter(Constants.ACTION) != null) {
                    if (request.getParameter(Constants.ACTION).equals(Constants.EXPORT)) {
                        return export(mapping, request);
                    } else if (request.getParameter(Constants.ACTION).equals(Constants.CONFIRM)) {
                        return confirm(mapping, request);
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

    private ActionForward export(ActionMapping mapping, HttpServletRequest request) throws Exception {
        final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));

        try (Connection c = DataBaseManager.getConnection()) {
            final List<ObservatorioRealizadoForm> fulfilledObservatories = ObservatorioDAO.getFulfilledObservatories(c, idObservatory, Constants.NO_PAGINACION, null);

            for (ObservatorioRealizadoForm fulfilledObservatory : fulfilledObservatories) {
                final PropertiesManager pmgr = new PropertiesManager();

                if (CartuchoDAO.isCartuchoAccesibilidad(c, fulfilledObservatory.getCartucho().getId())) {
                    exportResultadosAccesibilidad(CrawlerUtils.getResources(request), idObservatory, c, fulfilledObservatory);
                } else if (String.valueOf(fulfilledObservatory.getCartucho().getId()).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
                    return mapping.findForward(Constants.ERROR);
                }
            }

        } catch (Exception e) {
            Logger.putLog("Error al exportar los resultados del observatorio: ", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        PropertiesManager pmgr = new PropertiesManager();
        String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.observatorio.resultados.exportados");
        String volver = pmgr.getValue("returnPaths.properties", "volver.carga.observatorio");
        request.setAttribute("mensajeExito", mensaje);
        request.setAttribute("accionVolver", volver);
        return mapping.findForward(Constants.EXITO);
    }

    private void exportResultadosAccesibilidad(final MessageResources messageResources, Long idObservatory, Connection c, ObservatorioRealizadoForm fulfilledObservatory) throws Exception {
        Observatory observatory = DatabaseExportManager.getObservatory(fulfilledObservatory.getId());
        if (observatory == null) {
            Logger.putLog("Generando exportación", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR);
            // Información general de la ejecución del Observatorio
            observatory = DatabaseExportUtils.getObservatoryInfo(messageResources, fulfilledObservatory.getId());

            final List<CategoriaForm> categories = ObservatorioDAO.getObservatoryCategories(c, idObservatory);
            for (CategoriaForm categoriaForm : categories) {
                final Category category = DatabaseExportUtils.getCategoryInfo(messageResources, categoriaForm, observatory);
                observatory.getCategoryList().add(category);
            }

            final ObservatorioRealizadoForm observatorioRealizadoForm = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, fulfilledObservatory.getId());

            observatory.setName(observatorioRealizadoForm.getObservatorio().getNombre());
            observatory.setDate(new Timestamp(observatorioRealizadoForm.getFecha().getTime()));

            BaseManager.save(observatory);
        }
    }

    private ActionForward confirm(ActionMapping mapping, HttpServletRequest request) throws Exception {
        final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));

        try (Connection c = DataBaseManager.getConnection()) {
            final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
            request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
        } catch (Exception e) {
            Logger.putLog("Error en la confirmación para exportar los resultados del observatorio: ", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return mapping.findForward(Constants.CONFIRM);
    }

}