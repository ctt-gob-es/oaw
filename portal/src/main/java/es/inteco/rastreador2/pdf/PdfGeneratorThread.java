package es.inteco.rastreador2.pdf;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.datos.CSSDTO;
import es.inteco.intav.persistence.Analysis;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import es.inteco.utils.FileUtils;
import es.inteco.utils.MailUtils;
import org.apache.struts.util.PropertyMessageResources;
import org.apache.tika.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Hilo para generar los pdfs de un observatorio de forma asíncrona
 */
public class PdfGeneratorThread extends Thread {

    private final Long idObservatory;
    private final long idObservatoryExecution;
    private final List<FulFilledCrawling> fulfilledCrawlings;
    private final String email;

    public PdfGeneratorThread(final Long idObservatory, final long idObservatoryExecution, final List<FulFilledCrawling> fulfilledCrawlings, final String email) {
        super("PdfGeneratorThread");
        this.idObservatory = idObservatory;
        this.fulfilledCrawlings = fulfilledCrawlings;
        this.idObservatoryExecution = idObservatoryExecution;
        this.email = email;
    }

    @Override
    public void run() {
        for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
            buildPdf(fulfilledCrawling.getId(), fulfilledCrawling.getIdCrawling());
        }
        final PropertiesManager pmgr = new PropertiesManager();
        final String alertFromAddress = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.address");
        final String alertFromName = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.name");
        MailUtils.sendSimpleMail(alertFromAddress, alertFromName, Collections.singletonList(email), "Generación de informes completado", "El proceso de generación de informes ha finalizado. Para descargar los informes vuelva a ejecutar la acción");

    }

    private void buildPdf(final long idRastreoRealizado, final long idRastreo) {
        try (Connection c = DataBaseManager.getConnection()) {
            final SemillaForm seed = SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, idRastreo));
            final File pdfFile = getReportFile(idObservatory, idObservatoryExecution, seed);
            // Si el pdf no ha sido creado lo creamos
            if (!pdfFile.exists()) {
                final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
                final List<Long> previousEvaluationIds;
                if (evaluationIds != null && !evaluationIds.isEmpty()) {
                    final es.ctic.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.ctic.rastreador2.observatorio.ObservatoryManager();
                    previousEvaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(observatoryManager.getPreviousIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(idRastreo, ObservatorioDAO.getPreviousObservatoryExecution(c, idObservatoryExecution)));
                    final long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();

                    PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(), idRastreoRealizado, evaluationIds, previousEvaluationIds, PropertyMessageResources.getMessageResources("ApplicationResources"), null, pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution, observatoryType);
                    int index = 1;
                    for (Long evaluationId : evaluationIds) {
                        final File pageSourcesDirectory = new File(pdfFile.getParentFile(), "sources/" + String.valueOf(index));
                        pageSourcesDirectory.mkdirs();
                        final PrintWriter fw = new PrintWriter(new FileWriter(new File(pageSourcesDirectory, "references.txt"), true));
                        final Analysis analysis = AnalisisDatos.getAnalisisFromId(c, evaluationId);
                        final File htmlTempFile = File.createTempFile("oaw_","_"+FilenameUtils.getName(FilenameUtils.normalize(analysis.getUrl())), pageSourcesDirectory);
                        try (PrintWriter writer = new PrintWriter(htmlTempFile)) {
                            writer.print(analysis.getSource());
                            writer.flush();
                        }
                        fw.println(htmlTempFile.getName() +" --> " +  analysis.getUrl());
                        final List<CSSDTO> cssResourcesFromEvaluation = AnalisisDatos.getCSSResourcesFromEvaluation(evaluationId);
                        for (CSSDTO cssdto : cssResourcesFromEvaluation) {
                            final File stylesheetTempFile = File.createTempFile("oaw_","_"+FilenameUtils.getName(FilenameUtils.normalize(cssdto.getUrl())), pageSourcesDirectory);
                            try (PrintWriter writer = new PrintWriter(stylesheetTempFile)) {
                                writer.print(cssdto.getCodigo());
                                writer.flush();
                            }
                            fw.println(stylesheetTempFile.getName() +" --> " +  cssdto.getUrl());
                        }
                        index++;
                        fw.flush();
                        fw.close();
                    }
                    FileUtils.deleteDir(new File(pdfFile.getParent() + File.separator + "temp"));
                }
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", PdfGeneratorThread.class, Logger.LOG_LEVEL_ERROR, e);
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
}
