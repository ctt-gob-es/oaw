package es.inteco.rastreador2.pdf;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class PrimaryExportPdfAction extends Action {

    public ActionForward execute(final ActionMapping mapping, final ActionForm form,
                                 final HttpServletRequest request, final HttpServletResponse response) {
        if (request.getSession().getAttribute(Constants.ROLE) == null || CrawlerUtils.hasAccess(request, "export.observatory")) {
            final String action = request.getParameter(Constants.ACTION);
            if (Constants.EXPORT_ALL_PDFS.equals(action)) {
                return exportAllPdfs(mapping, request, response);
            } else {
                return exportSinglePdf(mapping, request, response);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
    }

    private ActionForward exportSinglePdf(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response) {
        // Url de invocacion: http://localhost:8080/oaw/secure/primaryExportPdfAction.do?id={0}&idExObs={1}&idrastreo={2}&id_observatorio={3}&observatorio=si&key={4}
        final String user = (String) request.getSession().getAttribute(Constants.USER);
        final long idRastreo = request.getParameter(Constants.ID_RASTREO) != null ? Long.parseLong(request.getParameter(Constants.ID_RASTREO)) : 0;

        if (userHasAccess(user, idRastreo)) {
            final long idObservatory = request.getParameter(Constants.ID_OBSERVATORIO) != null ? Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)) : 0;
            final long idExecutionOb = request.getParameter(Constants.ID_EX_OBS) != null ? Long.parseLong(request.getParameter(Constants.ID_EX_OBS)) : 0;
            final long idRastreoRealizado = request.getParameter(Constants.ID) != null ? Long.parseLong(request.getParameter(Constants.ID)) : 0;
            final boolean regenerate = Boolean.parseBoolean(request.getParameter(Constants.EXPORT_PDF_REGENERATE));

            final File pdfFile = buildPdf(idObservatory, idExecutionOb, idRastreoRealizado, idRastreo, regenerate, request);
            try {
                CrawlerUtils.returnFile(response, pdfFile.getPath(), "application/pdf", false);
            } catch (Exception e) {
                Logger.putLog("Exception al exportar el PDF", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
                return mapping.findForward(Constants.ERROR);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
        return null;
    }

    private boolean userHasAccess(final String user, final long idRastreo) {

        try (final Connection c = DataBaseManager.getConnection()) {
            return user == null || RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user);
        } catch (Exception e) {
            Logger.putLog("Exception al comprobar permisos para exportar el PDF", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
            return false;
        }
    }

    private File buildPdf(long idObservatory, long idExecutionOb, long idRastreoRealizado, long idRastreo, boolean regenerate, final HttpServletRequest request) {
        final PropertiesManager pmgr = new PropertiesManager();
        final int countAnalisis = AnalisisDatos.countAnalysisByTracking(idRastreoRealizado);

        if (countAnalisis > 0) {
            try (Connection c = DataBaseManager.getConnection()) {
                final SemillaForm seed = SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, idRastreo));
                String dependOn = PDFUtils.formatSeedName(seed.getDependencia());
                if (dependOn == null || dependOn.isEmpty()) {
                    dependOn = Constants.NO_DEPENDENCE;
                }
                final String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav") + idObservatory + File.separator + idExecutionOb + File.separator + dependOn;
                final File pdfFile = new File(path + File.separator + PDFUtils.formatSeedName(seed.getNombre()) + ".pdf");
                // Si el pdf no ha sido creado lo creamos
                if (regenerate || !pdfFile.exists()) {
                    final long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();
                    PrimaryExportPdfUtils.exportToPdf(idRastreo, idRastreoRealizado, request, pdfFile.getPath(), seed.getNombre(), null, idExecutionOb, observatoryType);
                    FileUtils.deleteDir(new File(path + File.separator + "temp"));
                }
                return pdfFile;
            } catch (Exception e) {
                Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        return null;
    }

    private ActionForward exportAllPdfs(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response) {
        // Url de invocacion: http://localhost:8080/oaw/secure/primaryExportPdfAction.do?id_observatorio=8&idExObs=33&action=exportAllPdfs
        final long idExecutionOb = request.getParameter(Constants.ID_EX_OBS) != null ? Long.parseLong(request.getParameter(Constants.ID_EX_OBS)) : 0;
        final long idObservatory = request.getParameter(Constants.ID_OBSERVATORIO) != null ? Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)) : 0;
        final PropertiesManager pmgr = new PropertiesManager();

        try (final Connection c = DataBaseManager.getConnection()) {
            final List<FulFilledCrawling> fulfilledCrawlings = ObservatorioDAO.getFulfilledCrawlingByObservatoryExecution(c, idExecutionOb);

            if (request.getParameter("reverse") != null && request.getParameter("reverse").equalsIgnoreCase(Boolean.TRUE.toString())) {
                Collections.reverse(fulfilledCrawlings);
            }

            for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
                buildPdf(idObservatory, idExecutionOb, fulfilledCrawling.getId(), fulfilledCrawling.getIdCrawling(), false, request);
            }

            return ZipUtils.pdfsZip(mapping, response, idObservatory, idExecutionOb, pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav"));
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        }
    }

}