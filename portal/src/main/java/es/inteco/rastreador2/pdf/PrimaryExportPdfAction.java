package es.inteco.rastreador2.pdf;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.datos.CSSDTO;
import es.inteco.intav.persistence.Analysis;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
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
import org.apache.tika.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.Constants.FILE;

public class PrimaryExportPdfAction extends Action {

    public final ActionForward execute(final ActionMapping mapping, final ActionForm form,
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
            if (pdfFile != null) {
                try {
                    CrawlerUtils.returnFile(response, pdfFile.getPath(), "application/pdf", false);
                } catch (Exception e) {
                    Logger.putLog("Exception al exportar el PDF", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
                    return mapping.findForward(Constants.ERROR);
                }
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
        return mapping.findForward(Constants.ERROR);
    }

    private boolean userHasAccess(final String user, final long idRastreo) {

        try (Connection c = DataBaseManager.getConnection()) {
            return user == null || RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user);
        } catch (Exception e) {
            Logger.putLog("Exception al comprobar permisos para exportar el PDF", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
            return false;
        }
    }

    private File buildPdf(long idObservatory, long idExecutionOb, long idRastreoRealizado, long idRastreo, boolean regenerate, final HttpServletRequest request) {
        final int countAnalisis = AnalisisDatos.countAnalysisByTracking(idRastreoRealizado);

        if (countAnalisis > 0) {
            try (Connection c = DataBaseManager.getConnection()) {
                final SemillaForm seed = SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, idRastreo));
                final File pdfFile = getReportFile(idObservatory, idExecutionOb, seed);
                // Si el pdf no ha sido creado lo creamos
                if (regenerate || !pdfFile.exists()) {
                    final long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();
                    PrimaryExportPdfUtils.exportToPdf(idRastreo, idRastreoRealizado, request, pdfFile.getPath(), seed.getNombre(), null, idExecutionOb, observatoryType);
                    final File sources = new File(pdfFile.getParentFile(), "sources.zip");
                    final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
                    writeSourceFiles(c, evaluationIds, pdfFile);
                    ZipUtils.generateZipFile(pdfFile.getParentFile().toString() + File.separator + "sources", pdfFile.getParentFile().toString() + File.separator + "sources.zip", true);
                    FileUtils.deleteDir(new File(pdfFile.getParent() + File.separator + "temp"));
                    FileUtils.deleteDir(new File(pdfFile.getParent() + File.separator + "sources"));
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

        try (Connection c = DataBaseManager.getConnection()) {
            final List<FulFilledCrawling> fulfilledCrawlings = ObservatorioDAO.getFulfilledCrawlingByObservatoryExecution(c, idExecutionOb);

            if (request.getParameter("reverse") != null && request.getParameter("reverse").equalsIgnoreCase(Boolean.TRUE.toString())) {
                Collections.reverse(fulfilledCrawlings);
            }

            int reportsToGenerate = 0;
            for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
                final File pdfFile = getReportFile(idObservatory, idExecutionOb, SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, fulfilledCrawling.getIdCrawling())));
                final File sources = new File(pdfFile.getParentFile(), "sources.zip");
                final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(fulfilledCrawling.getId());
                // Contabilizamos los informes que no están creados entre los portales para los que tenemos resultados (los portales no analizados no cuentan)
                if (!pdfFile.exists() || !sources.exists() && evaluationIds!=null && !evaluationIds.isEmpty()) {
                    reportsToGenerate++;
                }
            }
            if (reportsToGenerate < 5) {
                for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
                    buildPdf(idObservatory, idExecutionOb, fulfilledCrawling.getId(), fulfilledCrawling.getIdCrawling(), false, request);
                }
                final PropertiesManager pmgr = new PropertiesManager();
                return ZipUtils.pdfsZip(mapping, response, idObservatory, idExecutionOb, pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav"));
            } else {
                request.setAttribute("GENERATE_TIME", String.format("%d", TimeUnit.SECONDS.toMinutes(Math.max(60, reportsToGenerate * 2))));
                final DatosForm userData = LoginDAO.getUserDataByName(c, request.getSession().getAttribute(Constants.USER).toString());
                request.setAttribute("EMAIL", userData.getEmail());
                final PdfGeneratorThread pdfGeneratorThread = new PdfGeneratorThread(idObservatory, idExecutionOb, fulfilledCrawlings, userData.getEmail());
                pdfGeneratorThread.start();
                return mapping.findForward(Constants.GENERATE_ALL_REPORTS);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        }
    }

    private File getReportFile(final long idObservatory, final long idExecutionOb, final SemillaForm seed) {
        final PropertiesManager pmgr = new PropertiesManager();
        String dependOn = PDFUtils.formatSeedName(seed.getDependencia());
        if (dependOn == null || dependOn.isEmpty()) {
            dependOn = Constants.NO_DEPENDENCE;
        }
        final String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav") + idObservatory + File.separator + idExecutionOb + File.separator + dependOn + File.separator + PDFUtils.formatSeedName(seed.getNombre());
        return new File(path + File.separator + PDFUtils.formatSeedName(seed.getNombre()) + ".pdf");
    }

    private void writeSourceFiles(final Connection c, final List<Long> evaluationIds, final File pdfFile) throws IOException {
        int index = 1;
        for (Long evaluationId : evaluationIds) {
            final File pageSourcesDirectory = new File(pdfFile.getParentFile(), "sources/" + index);
            if (!pageSourcesDirectory.mkdirs()) {
                Logger.putLog("No se ha podido crear el directorio sources - " + pageSourcesDirectory.getAbsolutePath(), PdfGeneratorThread.class, Logger.LOG_LEVEL_ERROR);
            }
            try (PrintWriter fw = new PrintWriter(new FileWriter(new File(pageSourcesDirectory, "references.txt"), true))) {
                final Analysis analysis = AnalisisDatos.getAnalisisFromId(c, evaluationId);
                final File htmlTempFile = File.createTempFile("oaw_", "_" + getURLFileName(analysis.getUrl(), "html.html"), pageSourcesDirectory);
                fw.println(writeTempFile(htmlTempFile, analysis.getSource(), analysis.getUrl()));
                final List<CSSDTO> cssResourcesFromEvaluation = AnalisisDatos.getCSSResourcesFromEvaluation(evaluationId);
                for (CSSDTO cssdto : cssResourcesFromEvaluation) {
                    final File stylesheetTempFile = File.createTempFile("oaw_", "_" + getURLFileName(cssdto.getUrl(), "css.css"), pageSourcesDirectory);
                    fw.println(writeTempFile(stylesheetTempFile, cssdto.getCodigo(), cssdto.getUrl()));
                }
                index++;
                fw.flush();
            }
        }
    }

    private String writeTempFile(final File tempFile, final String source, final String url) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.print(source);
            writer.flush();
        }
        return tempFile.getName() + " --> " + url;
    }

    private String getURLFileName(final String url, final String defaultValue) {
        final String fileName = FilenameUtils.getName(FilenameUtils.normalize(url));
        return fileName.isEmpty() ? defaultValue : fileName;
    }

}