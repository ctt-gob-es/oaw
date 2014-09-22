package es.inteco.rastreador2.pdf;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.StringUtils;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.pdf.utils.MultilanguagePrimaryExportPdfUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.rastreador2.utils.AnnexUtils;
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

public class MultilanguagePrimaryExportPdfAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getSession().getAttribute(Constants.ROLE) == null || CrawlerUtils.hasAccess(request, "export.observatory")) {
                String action = request.getParameter(Constants.ACTION);
                if (StringUtils.isNotEmpty(action) && action.equals(Constants.EXPORT_ALL_PDFS)) {
                    return exportAllPdfs(mapping, form, request, response);
                } else if (StringUtils.isNotEmpty(action) && action.equals(Constants.GET_ANNEXES)) {
                    return getAnnexes(mapping, request, response);
                } else {
                    return listPdf(mapping, form, request, response);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    public ActionForward listPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) {
        PropertiesManager pmgr = new PropertiesManager();

        String user = (String) request.getSession().getAttribute(Constants.USER);
        long idObservatory = 0;
        if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
            idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
        }
        long idExecutionOb = 0;
        if (request.getParameter(Constants.ID_EX_OBS) != null) {
            idExecutionOb = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
        }
        long idExecution = 0;
        if (request.getParameter(Constants.ID) != null) {
            idExecution = Long.parseLong(request.getParameter(Constants.ID));
        }
        long idRastreo = 0;
        if (request.getParameter(Constants.ID_RASTREO) != null) {
            idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
        }

        String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.multilanguage")
                + idObservatory + File.separator + idExecutionOb;

        File checkFile = null;

        Connection c = null;

        try {
            List<AnalysisForm> analysisList = AnalysisManager.getAnalysisByExecution(idExecution, 0, Integer.MAX_VALUE);
            if (analysisList != null && !analysisList.isEmpty()) {
                c = DataBaseManager.getConnection();
                //Comprobamos que el usuario esta asociado con el rastreo que quiere exportar
                if (user == null || RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                    SemillaForm seed = SemillaDAO.getSeedById(c, RastreoDAO.getSeedByCrawler(c, idRastreo));
                    path += File.separator + PDFUtils.formatSeedName(seed.getDependencia());
                    checkFile = new File(path + File.separator + PDFUtils.formatSeedName(seed.getNombre()) + ".pdf");
                    // Si el pdf no ha sido creado lo creamos
                    if (request.getParameter(Constants.EXPORT_PDF_REGENERATE) != null || !checkFile.exists()) {
                        long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();
                        MultilanguagePrimaryExportPdfUtils.exportToPdf(idExecution, analysisList, request, checkFile.getPath(), seed.getNombre(), observatoryType);
                        FileUtils.deleteDir(new File(path + File.separator + "temp"));
                    }
                } else {
                    return mapping.findForward(Constants.NO_PERMISSION);
                }
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

        return null;
    }


    public ActionForward getAnnexes(ActionMapping mapping, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));

        Long idOperation = System.currentTimeMillis();

        AnnexUtils.createMultilanguageAnnexes(idObsExecution, idOperation);

        PropertiesManager pmgr = new PropertiesManager();
        String zipPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "anexos.zip";
        ZipUtils.generateZip(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation.toString(), zipPath, true);

        CrawlerUtils.returnFile(zipPath, response, "application/zip", true);

        // FileUtils.deleteDir(new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation));

        return null;
    }

    public ActionForward exportAllPdfs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response) {
        /* http://localhost:8080/oaw/secure/multilanguagePrimaryExportPdfAction.do?id_observatorio=8&idExObs=33&action=exportAllPdfs&reverse=true */
        long idExecutionOb = 0;
        if (request.getParameter(Constants.ID_EX_OBS) != null) {
            idExecutionOb = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
        }

        long idObservatory = 0;
        if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
            idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
        }

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
                String url = pmgr.getValue(CRAWLER_PROPERTIES, "exportPdf.multilanguage.request")
                        .replace("{0}", String.valueOf(fulfilledCrawling.getId()))
                        .replace("{1}", String.valueOf(idExecutionOb))
                        .replace("{2}", String.valueOf(fulfilledCrawling.getIdCrawling()))
                        .replace("{3}", String.valueOf(idObservatory))
                        .replace("{4}", pmgr.getValue(CRAWLER_CORE_PROPERTIES, "not.filtered.uris.security.key"));


                Logger.putLog("Pidiendo la URL: " + url, MultilanguagePrimaryExportPdfAction.class, Logger.LOG_LEVEL_INFO);
                HttpURLConnection urlConnection = es.inteco.utils.CrawlerUtils.getConnection(url, null, true);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Logger.putLog(++counter + " generados con éxito", MultilanguagePrimaryExportPdfAction.class, Logger.LOG_LEVEL_INFO);
                } else {
                    Logger.putLog("No se ha generado con éxito", MultilanguagePrimaryExportPdfAction.class, Logger.LOG_LEVEL_INFO);
                }

                urlConnection.disconnect();
            }

            return ZipUtils.pdfsZip(mapping, response, fulfilledCrawlings, idObservatory, idExecutionOb, pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.multilanguage"));
        } catch (Exception e) {
            Logger.putLog("Exception: ", MultilanguagePrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

}
