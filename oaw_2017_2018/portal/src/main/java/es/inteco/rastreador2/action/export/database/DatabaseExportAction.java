package es.inteco.rastreador2.action.export.database;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.manager.BaseManager;
import es.inteco.rastreador2.manager.export.database.DatabaseExportManager;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.AnnexUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.export.database.DatabaseExportUtils;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
                        //return
                        export(mapping, request);
                        getAnnexes(mapping, request, response);
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

    private ActionForward export(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
        final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
        final Long idExObservatory = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));

        try (Connection c = DataBaseManager.getConnection()) {
            final ObservatorioRealizadoForm fulfilledObservatory = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, idExObservatory);

            if (CartuchoDAO.isCartuchoAccesibilidad(c, fulfilledObservatory.getCartucho().getId())) {
                exportResultadosAccesibilidad(CrawlerUtils.getResources(request), idObservatory, c, fulfilledObservatory);
            } else {
                return mapping.findForward(Constants.ERROR);
            }

        } catch (Exception e) {
            Logger.putLog("Error al exportar los resultados del observatorio: ", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.observatorio.resultados.exportados", "volver.carga.observatorio");
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
        } else {
            BaseManager.delete(observatory);
            exportResultadosAccesibilidad(messageResources, idObservatory, c, fulfilledObservatory);
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


    private ActionForward getAnnexes(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
            final Long idOperation = System.currentTimeMillis();

            AnnexUtils.createAnnexPaginas(CrawlerUtils.getResources(request), idObsExecution, idOperation);
            AnnexUtils.createAnnexPortales(CrawlerUtils.getResources(request), idObsExecution, idOperation);

            final PropertiesManager pmgr = new PropertiesManager();
            final String exportPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path");
            final String zipPath = exportPath + idOperation + File.separator + "anexos.zip";
            ZipUtils.generateZipFile(exportPath + idOperation.toString(), zipPath, true);

            CrawlerUtils.returnFile(response, zipPath, "application/zip", true);

            FileUtils.deleteDir(new File(exportPath + idOperation));

            return null;
        } catch (Exception e) {
            Logger.putLog("Exception generando los anexos.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            final ActionMessages errors = new ActionMessages();
            errors.add("usuarioDuplicado", new ActionMessage("data.export"));
            saveErrors(request, errors);
            //return getFulfilledObservatories(mapping, request);
            return mapping.findForward(Constants.ERROR);
        }
    }
}