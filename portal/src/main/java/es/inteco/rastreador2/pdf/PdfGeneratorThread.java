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
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import es.inteco.utils.FileUtils;
import es.inteco.utils.MailUtils;
import org.apache.struts.util.PropertyMessageResources;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
        // TODO: Send correo electrónico
        final PropertiesManager pmgr = new PropertiesManager();

        String alertFromAddress = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.address");
        String alertFromName = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.name");
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
                if (evaluationIds!=null && !evaluationIds.isEmpty()) {
                    final es.ctic.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.ctic.rastreador2.observatorio.ObservatoryManager();
                    previousEvaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(observatoryManager.getPreviousIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(idRastreo, ObservatorioDAO.getPreviousObservatoryExecution(c, idObservatoryExecution)));
                    final long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();

                    PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(), idRastreoRealizado, evaluationIds, previousEvaluationIds, PropertyMessageResources.getMessageResources("ApplicationResources"), null, pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution, observatoryType);
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
        final String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav") + idObservatory + File.separator + idExecutionOb + File.separator + dependOn;
        return new File(path + File.separator + PDFUtils.formatSeedName(seed.getNombre()) + ".pdf");
    }
}
