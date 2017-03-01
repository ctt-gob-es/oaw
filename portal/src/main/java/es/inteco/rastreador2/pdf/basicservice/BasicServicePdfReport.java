package es.inteco.rastreador2.pdf.basicservice;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;

import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;

/**
 * Clase para generar el informe PDF del servicio de diagnóstico.
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class BasicServicePdfReport {

    private final AnonymousResultExportPdf pdfBuilder;
    private final MessageResources messageResources;

    public BasicServicePdfReport(final AnonymousResultExportPdf pdfBuilder) {
        this(MessageResources.getMessageResources("ApplicationResources"), pdfBuilder);
    }

    public BasicServicePdfReport(final MessageResources messageResources, final AnonymousResultExportPdf pdfBuilder) {
        this.messageResources = messageResources;
        this.pdfBuilder = pdfBuilder;
        this.pdfBuilder.setBasicService(true);
    }

    public void exportToPdf(final List<ObservatoryEvaluationForm> currentEvaluationPageList, final Map<Date, List<ObservatoryEvaluationForm>> historicoEvaluationPageList, final String generalExpPath) throws Exception {
        final File file = new File(generalExpPath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            if (!file.getParentFile().setReadable(true, false) || !file.getParentFile().setWritable(true, false)) {
                Logger.putLog(String.format("No se ha podido asignar permisos a los directorios de exportación a PDF %s", file.getParentFile().getAbsolutePath()), BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
            }
            Logger.putLog("No se ha podido crear los directorios para exportar a PDF", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
        }
        Logger.putLog("Exportando a PDF BasicServicePdfReport.exportToPdf", BasicServicePdfReport.class, Logger.LOG_LEVEL_DEBUG);

        // TODO: Add document metadata (author, creator, subject, title...)
        final Document document = new Document(PageSize.A4, 50, 50, 110, 72);
//        document.addAuthor("Ministerio de Hacienda y Función Pública");
//        document.addCreationDate();
//        document.addCreator("OAW - Observatorio de Accesibilidad Web");
        try (FileOutputStream outputFileStream = new FileOutputStream(file)) {
            try {
                final PdfWriter writer = PdfWriter.getInstance(document, outputFileStream);
                writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
                writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));

                final String crawlingDate = CrawlerUtils.formatDate(pdfBuilder.getBasicServiceForm().getDate());
                final String footerText = messageResources.getMessage("ob.resAnon.intav.report.foot", new String[]{pdfBuilder.getBasicServiceForm().getName(), crawlingDate});
                writer.setPageEvent(new ExportPageEventsObservatoryMP(footerText, crawlingDate));
                ExportPageEventsObservatoryMP.setPrintFooter(true);

                final PdfTocManager pdfTocManager = createPdfTocManager(writer);

                document.open();

                PDFUtils.addCoverPage(document, messageResources.getMessage("pdf.accessibility.title", new String[]{pdfBuilder.getBasicServiceForm().getName().toUpperCase(), pdfBuilder.getTitle()}), pdfBuilder.getBasicServiceForm().getAnalysisType()== BasicServiceAnalysisType.URL?pdfBuilder.getBasicServiceForm().getDomain():"", "Informe emitido bajo demanda.");

                pdfBuilder.createIntroductionChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
                pdfTocManager.addChapterCount();

                pdfBuilder.createObjetiveChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0);
                pdfTocManager.addChapterCount();

                // Resumen de las puntuaciones del Observatorio
                final List<ObservatoryEvaluationForm> previousEvaluation = getPreviousEvaluation(historicoEvaluationPageList);
                final BasicServiceObservatoryScorePdfSectionBuilder observatoryScoreSectionBuilder = new BasicServiceObservatoryScorePdfSectionBuilder(currentEvaluationPageList, previousEvaluation);
                observatoryScoreSectionBuilder.addObservatoryScoreSummary(pdfBuilder, messageResources, document, pdfTocManager, file);

                // Resumen de las puntuaciones del Observatorio
                final BasicServiceObservatoryResultsSummaryPdfSectionBuilder observatoryResultsSummarySectionBuilder = new BasicServiceObservatoryResultsSummaryPdfSectionBuilder(currentEvaluationPageList);
                observatoryResultsSummarySectionBuilder.addObservatoryResultsSummary(messageResources, document, pdfTocManager);

                // Evolución resultados servicio diagnóstico
                final Map<Date, List<ObservatoryEvaluationForm>> evolutionObservatoryEvaluation = new TreeMap<>(historicoEvaluationPageList);
                evolutionObservatoryEvaluation.put(pdfBuilder.getBasicServiceForm().getDate(), currentEvaluationPageList);
                final BasicServiceObservatoryEvolutionResultsPdfSectionBuilder observatoryEvolutionResultsSectionBuilder = new BasicServiceObservatoryEvolutionResultsPdfSectionBuilder(evolutionObservatoryEvaluation);
                observatoryEvolutionResultsSectionBuilder.addEvolutionResults(pdfBuilder, messageResources, document, pdfTocManager, file);

                // Resultados por página
                final BasicServicePageResultsPdfSectionBuilder observatoryPageResultsSectionBuilder = new BasicServicePageResultsPdfSectionBuilder(currentEvaluationPageList);
                observatoryPageResultsSectionBuilder.addPageResults(messageResources, document, pdfTocManager);

                pdfBuilder.createMethodologyChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0, pdfBuilder.isBasicService());

                //Ponemos la variable a true para que no se escriba el footer en el índice
                IndexUtils.createIndex(writer, document, messageResources.getMessage("pdf.accessibility.index.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT);
                ExportPageEventsObservatoryMP.setPrintFooter(true);
            } catch (DocumentException e) {
                Logger.putLog("Error al exportar a pdf", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            } catch (Exception e) {
                Logger.putLog("Excepción genérica al generar el pdf", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            } finally {
                if (document.isOpen()) {
                    document.close();
                }
            }
        }
    }

    private List<ObservatoryEvaluationForm> getPreviousEvaluation(final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationPageList) {
        final Iterator<List<ObservatoryEvaluationForm>> iterator = previousEvaluationPageList.values().iterator();
        List<ObservatoryEvaluationForm> previousEvaluation = null;
        while (iterator.hasNext()) {
            previousEvaluation = iterator.next();
        }
        return previousEvaluation;
    }

    private PdfTocManager createPdfTocManager(final PdfWriter writer) {
        final IndexEvents index = new IndexEvents();
        writer.setPageEvent(index);
        writer.setLinearPageMode();

        return new PdfTocManager(index);
    }



    /**
     * Crea una celda PdfPCell para una tabla del informa PDF con la evolución del nivel de accesibilidad.
     *
     * @param messageResources
     * @param currentLevel     String nivel de accesibilidad actual.
     * @param previousLevel    String nivel de accesibilidad de la iteración anterior.
     * @return una celda PdfPCell con una imagen que indica la evolución y una cadena con la misma información complementando la imagen.
     */
    private PdfPCell createEvolutionLevelCell(final MessageResources messageResources, final String currentLevel, final String previousLevel) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (currentLevel.equalsIgnoreCase(previousLevel)) {
            return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene"), "se mantiene", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
        } else {
            // Si los valores entre iteraciones han variado
            if (messageResources.getMessage("resultados.anonimos.num.portales.nv").equalsIgnoreCase(previousLevel) ||
                    messageResources.getMessage("resultados.anonimos.num.portales.parcial").equalsIgnoreCase(previousLevel)) {
                // Si el valor actual es distinto al anterior y el anterior era "No válido" (o "Parcial") entonces ha mejorado
                return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Mejora"), "mejora", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
            } else if (messageResources.getMessage("resultados.anonimos.num.portales.aa").equalsIgnoreCase(previousLevel)) {
                // Si el valor actual es distinto al anterior y el anterior era "Prioridad 1 y 2" entonces ha empeorado
                return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Empeora"), "empeora", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
            } else {
                // Si estamos en este punto el valor anterior era "Prioridad 1" y el actual es distinto
                if (messageResources.getMessage("resultados.anonimos.num.portales.aa").equalsIgnoreCase(currentLevel)) {
                    return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Mejora"), "mejora", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
                } else {
                    return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Empeora"), "empeora", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
                }
            }
        }
    }

}
