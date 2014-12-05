package es.inteco.rastreador2.pdf;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
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
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_CORE_PROPERTIES;
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class PrimaryExportPdfAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession().getAttribute(Constants.ROLE) == null || CrawlerUtils.hasAccess(request, "export.observatory")) {
            String action = request.getParameter(Constants.ACTION);
            if (StringUtils.isNotEmpty(action) && action.equals(Constants.EXPORT_ALL_PDFS)) {
                return exportAllPdfs(mapping, request, response);
            } else {
                return listPdf(mapping, request, response);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
    }

    private ActionForward listPdf(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        PropertiesManager pmgr = new PropertiesManager();

        final String user = (String) request.getSession().getAttribute(Constants.USER);
        final long idObservatory = request.getParameter(Constants.ID_OBSERVATORIO) != null?Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)):0;
        final long idExecutionOb = request.getParameter(Constants.ID_EX_OBS) != null?Long.parseLong(request.getParameter(Constants.ID_EX_OBS)):0;
        final long idExecution = request.getParameter(Constants.ID) != null?Long.parseLong(request.getParameter(Constants.ID)):0;
        final long idRastreo = request.getParameter(Constants.ID_RASTREO) != null?Long.parseLong(request.getParameter(Constants.ID_RASTREO)):0;
        String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav") + idObservatory + File.separator + idExecutionOb;
        File checkFile = null;

        Connection c = null;
        List<Long> evaluationIds = AnalisisDatos.getEvaluationIds(idExecution);
        if (evaluationIds != null && !evaluationIds.isEmpty()) {
            try {
                c = DataBaseManager.getConnection();
                //Comprobamos que el usuario esta asociado con el rastreo que quiere exportar
                if (user == null || RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                    SemillaForm seed = SemillaDAO.getSeedById(c, RastreoDAO.getSeedByCrawler(c, idRastreo));
                    String dependOn = PDFUtils.formatSeedName(seed.getDependencia());
                    if (dependOn == null || dependOn.isEmpty()) {
                        dependOn = Constants.NO_DEPENDENCE;
                    }
                    path += File.separator + dependOn;
                    checkFile = new File(path + File.separator + PDFUtils.formatSeedName(seed.getNombre()) + ".pdf");
                    // Si el pdf no ha sido creado lo creamos
                    if (request.getParameter(Constants.EXPORT_PDF_REGENERATE) != null || !checkFile.exists()) {
                        long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();
                        PrimaryExportPdfUtils.exportToPdf(idExecution, evaluationIds, request, checkFile.getPath(), seed.getNombre(), null, idExecutionOb, observatoryType);
                        FileUtils.deleteDir(new File(path + File.separator + "temp"));
                    }
                } else {
                    return mapping.findForward(Constants.NO_PERMISSION);
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
                return mapping.findForward(Constants.ERROR);
            } finally {
                DataBaseManager.closeConnection(c);
            }

            try {
                CrawlerUtils.returnFile(checkFile.getPath(), response, "application/pdf", false);
            } catch (Exception e) {
                Logger.putLog("Exception al devolver el PDF", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }

        return null;
    }

    private ActionForward exportAllPdfs(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        /* http://localhost:8080/oaw/secure/primaryExportPdfAction.do?id_observatorio=8&idExObs=33&action=exportAllPdfs */
        final long idExecutionOb = request.getParameter(Constants.ID_EX_OBS) != null?Long.parseLong(request.getParameter(Constants.ID_EX_OBS)):0;
        final long idObservatory = request.getParameter(Constants.ID_OBSERVATORIO) != null?Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)):0;

        PropertiesManager pmgr = new PropertiesManager();
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            List<FulFilledCrawling> fulfilledCrawlings = ObservatorioDAO.getFulfilledCrawlingByObservatoryExecution(c, idExecutionOb);

            if (request.getParameter("reverse") != null && request.getParameter("reverse").equalsIgnoreCase(Boolean.TRUE.toString())) {
                Collections.reverse(fulfilledCrawlings);
            }

            int counter = 0;
            for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
                String url = pmgr.getValue(CRAWLER_PROPERTIES, "exportPdf.request")
                        .replace("{0}", String.valueOf(fulfilledCrawling.getId()))
                        .replace("{1}", String.valueOf(idExecutionOb))
                        .replace("{2}", String.valueOf(fulfilledCrawling.getIdCrawling()))
                        .replace("{3}", String.valueOf(idObservatory))
                        .replace("{4}", pmgr.getValue(CRAWLER_CORE_PROPERTIES, "not.filtered.uris.security.key"));


                Logger.putLog("Pidiendo la URL: " + url, PrimaryExportPdfAction.class, Logger.LOG_LEVEL_INFO);
                HttpURLConnection urlConnection = es.inteco.utils.CrawlerUtils.getConnection(url, null, true);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Logger.putLog(++counter + " generados con éxito", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_INFO);
                } else {
                    Logger.putLog("FALLO no se ha generado con éxito la exportación a pdf de "+ fulfilledCrawling.getSeed().getNombre(), PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR);
                }

                urlConnection.disconnect();
            }

            return ZipUtils.pdfsZip(mapping, response, fulfilledCrawlings, idObservatory, idExecutionOb, pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav"));
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

}
