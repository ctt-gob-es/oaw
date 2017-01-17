package es.inteco.rastreador2.pdf.basicservice;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.events.IndexEvents;
import es.ctic.rastreador2.pdf.utils.CheckDescriptionsManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.*;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.rastreador2.utils.*;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static es.inteco.common.Constants.*;
import static es.inteco.common.ConstantsFont.*;
import static es.inteco.rastreador2.openOffice.export.OpenOfficeUNE2012DocumentBuilder.LEVEL_II_VERIFICATIONS;
import static es.inteco.rastreador2.openOffice.export.OpenOfficeUNE2012DocumentBuilder.LEVEL_I_VERIFICATIONS;
import static es.inteco.rastreador2.utils.GraphicsUtils.parseLevelLabel;
import static es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint;

/**
 * Clase para generar el informe PDF del servicio de diagnóstico.
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class BasicServicePdfReport {

    private final AnonymousResultExportPdfUNE2012 pdfBuilder;
    private final MessageResources messageResources;

    public BasicServicePdfReport(final AnonymousResultExportPdfUNE2012 pdfBuilder) {
        this(MessageResources.getMessageResources("ApplicationResources"), pdfBuilder);
    }

    public BasicServicePdfReport(final MessageResources messageResources, final AnonymousResultExportPdfUNE2012 pdfBuilder) {
        this.messageResources = messageResources;
        this.pdfBuilder = pdfBuilder;
        this.pdfBuilder.setBasicService(true);
    }

    public void exportToPdf(final List<ObservatoryEvaluationForm> currentEvaluationPageList, final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationPageList, final String generalExpPath) throws Exception {
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

                PDFUtils.addCoverPage(document, messageResources.getMessage("pdf.accessibility.title", new String[]{pdfBuilder.getBasicServiceForm().getName().toUpperCase(), pdfBuilder.getTitle()}), pdfBuilder.getBasicServiceForm().getDomain(), "Informe emitido bajo demanda.");

                pdfBuilder.createIntroductionChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
                pdfTocManager.addChapterCount();

                pdfBuilder.createObjetiveChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0);
                pdfTocManager.addChapterCount();

                // Resumen de las puntuaciones del Observatorio
                final Iterator<List<ObservatoryEvaluationForm>> iterator = previousEvaluationPageList.values().iterator();
                List<ObservatoryEvaluationForm> previousEvaluation = null;
                while (iterator.hasNext()) {
                    previousEvaluation = iterator.next();
                }

                final BasicServiceObservatoryScorePdfSectionBuilder observatoryScoreSectionBuilder = new BasicServiceObservatoryScorePdfSectionBuilder(currentEvaluationPageList, previousEvaluation);
                observatoryScoreSectionBuilder.addObservatoryScoreSummary(pdfBuilder, messageResources, document, pdfTocManager, file);
                pdfTocManager.addChapterCount();

                // Resumen de las puntuaciones del Observatorio
                final BasicServiceObservatoryResultsSummaryPdfSectionBuilder observatoryResultsSummarySectionBuilder = new BasicServiceObservatoryResultsSummaryPdfSectionBuilder(currentEvaluationPageList);
                observatoryResultsSummarySectionBuilder.addObservatoryResultsSummary(messageResources, document, pdfTocManager);
                pdfTocManager.addChapterCount();

                // Evolución resultados servicio diagnóstico
                final Map<Date, List<ObservatoryEvaluationForm>> tmp = new TreeMap<>(previousEvaluationPageList);
                tmp.put(pdfBuilder.getBasicServiceForm().getDate(), currentEvaluationPageList);
                if (tmp.size() > 1) {
                    addEvolutionResults(messageResources, document, pdfTocManager, file, tmp);
                    pdfTocManager.addChapterCount();
                }

                int counter = 1;
                for (ObservatoryEvaluationForm evaluationForm : currentEvaluationPageList) {
                    final String evaluationTitle = messageResources.getMessage("observatory.graphic.score.by.page.label", counter);
                    final Chapter chapter = PDFUtils.createChapterWithTitle(evaluationTitle, pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter(), ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resultados_page_" + counter);
                    final String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
                    final String url = evaluationForm.getUrl();

                    final BigDecimal puntuacionMediaPortal = evaluationForm.getScore();
                    final String validationLevel = ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm));
                    final List<BigDecimal> puntuacionesMediasNivelAccesibilidad = new ArrayList<>();
                    for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                        puntuacionesMediasNivelAccesibilidad.add(observatoryLevelForm.getScore());
                    }
                    chapter.add(createPaginaTableInfo(messageResources, title, url, puntuacionMediaPortal, validationLevel, puntuacionesMediasNivelAccesibilidad, pdfTocManager.getNumSection()));

                    // Creación de las tablas resumen de resultado por verificación de cada página
                    for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                        final Paragraph levelTitle = new Paragraph(getPriorityName(messageResources, observatoryLevelForm.getName()), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L);
                        levelTitle.setSpacingBefore(HALF_LINE_SPACE);
                        chapter.add(levelTitle);
                        chapter.add(createPaginaTableVerificationSummary(messageResources, observatoryLevelForm));
                    }

                    addCheckCodes(messageResources, evaluationForm, chapter, pdfBuilder.isBasicService());

                    document.add(chapter);
                    pdfTocManager.addChapterCount();
                    counter++;
                }

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

    private PdfTocManager createPdfTocManager(final PdfWriter writer) {
        final IndexEvents index = new IndexEvents();
        writer.setPageEvent(index);
        writer.setLinearPageMode();

        return new PdfTocManager(index);
    }

    private void addEvolutionResults(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, final File file, final Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList) throws DocumentException {
        final Chapter chapter = PDFUtils.createChapterWithTitle("Evolución Resultados", pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
        evolutionAverageScore(messageResources, pdfTocManager, file, evaluationPageList, chapter);
        chapter.newPage();

        evolutionConformanceLevel(messageResources, pdfTocManager, file, evaluationPageList, chapter);
        chapter.newPage();

        evolutionAverageScoreByVerification(messageResources, pdfTocManager, file, evaluationPageList, chapter);

        document.add(chapter);
    }

    private void evolutionAverageScore(MessageResources messageResources, PdfTocManager pdfTocManager, File file, Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Chapter chapter) {
        final Section section = PDFUtils.createSection("Evolución Puntuacion Media del Portal", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.getNumSection(), 1);
        final ArrayList<String> boldWord = new ArrayList<>();
        boldWord.add("Puntuación Media alcanzada por el portal");
        final String evolution_1_1 = "La siguiente gráfica muestra la {0} en los distintos observatorios " +
                "realizados hasta la fecha. Éste es un valor que permite observar de forma general si la accesibilidad del sitio " +
                "web tiende a mejorar o empeorar en el tiempo.";
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(evolution_1_1, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        final PropertiesManager pmgr = new PropertiesManager();
        final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

        final Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateEvolutionPuntuationDataSet(evaluationPageList, df);

        final StringBuilder color = new StringBuilder(50);
        for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
            final ScoreForm score = pdfBuilder.generateScores(messageResources, observatoryEvaluationForms.getValue());
            if (score.getLevel().equals(messageResources.getMessage("resultados.anonimos.num.portales.aa"))) {
                color.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.green.color"));
            } else if (score.getLevel().equals(messageResources.getMessage("resultados.anonimos.num.portales.a"))) {
                color.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.yellow.color"));
            } else {
                color.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.red.color"));
            }
            color.append(',');
        }

        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg";

        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
            dataSet.addValue(entry.getValue(), "", parseLevelLabel(entry.getKey(), messageResources));
        }
        final ChartForm observatoryGraphicsForm = new ChartForm("", "", "", dataSet, true, true, false, false, true, true, false, 580, 580, color.toString());
        observatoryGraphicsForm.setFixedColorBars(true);
        observatoryGraphicsForm.setFixedLegend(true);
        try {
            GraphicsUtils.createStandardBarChart(observatoryGraphicsForm, filePath, "", messageResources, false);
            final Image image = PDFUtils.createImage(filePath, null);
            if (image != null) {
                image.scalePercent(60);
                image.setAlignment(Element.ALIGN_CENTER);
                image.setSpacingBefore(ConstantsFont.LINE_SPACE);
                image.setSpacingAfter(ConstantsFont.LINE_SPACE);
                section.add(image);
            }
        } catch (IOException e) {
            Logger.putLog("Error al intentar crear la imagen " + filePath, BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
        }

        final float[] columnWidths = new float[]{0.5f, 0.5f};
        final PdfPTable tablaRankings = new PdfPTable(columnWidths);
        tablaRankings.setSpacingBefore(LINE_SPACE);
        tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
        tablaRankings.setWidthPercentage(80);
        tablaRankings.setHeaderRows(1);

        tablaRankings.addCell(PDFUtils.createTableCell("Fecha", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        tablaRankings.addCell(PDFUtils.createTableCell("Puntuación Media", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        tablaRankings.completeRow();

        for (Map.Entry<String, BigDecimal> dateEvaluationScoreEntry : resultData.entrySet()) {
            tablaRankings.addCell(PDFUtils.createTableCell(dateEvaluationScoreEntry.getKey(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            tablaRankings.addCell(PDFUtils.createTableCell(dateEvaluationScoreEntry.getValue().toPlainString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            tablaRankings.completeRow();
        }

        section.add(tablaRankings);
    }

    private void evolutionConformanceLevel(MessageResources messageResources, PdfTocManager pdfTocManager, File file, Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Chapter chapter) {
        final Section section = PDFUtils.createSection("Evolución de la distribución del nivel de accesibilidad", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.getNumSection(), 1);

        final ArrayList<String> boldWord = new ArrayList<>();
        boldWord.add("distribución de las páginas del portal en los tres Niveles de Conformidad");
        final String paragraph_1 = "Mediante las siguientes gráficas se muestra la evolución que ha sufrido la {0} definidos en el observatorio: Prioridad 1 y 2 (nivel óptimo), Prioridad 1 y Parcial.";
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(paragraph_1, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        boldWord.clear();
        final String paragraph_2 = "La tendencia ideal a observar en esta evolución sería que el porcentaje de Prioridad 1 y 2 esté siempre en aumento, mientras que el nivel Parcial debería decrecer aproximándose lo máximo posible al valor 0. Si aumenta el porcentaje de Prioridad 1 lo ideal es que sea únicamente a costa de un descenso del nivel Parcial y no de un descenso del nivel de Prioridad 1 y 2.";
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(paragraph_2, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));


        final PropertiesManager pmgr = new PropertiesManager();
        final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

        final Map<String, Map<String, BigDecimal>> result = new TreeMap<>();
        for (Map.Entry<Date, List<ObservatoryEvaluationForm>> dateEvaluationPageListEntry : evaluationPageList.entrySet()) {
            final Map<String, List<ObservatoryEvaluationForm>> globalResult = ResultadosAnonimosObservatorioUNE2012Utils.getPagesByType(dateEvaluationPageListEntry.getValue());
            result.put(df.format(dateEvaluationPageListEntry.getKey()), calculatePercentage(globalResult));
        }

        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (Map.Entry<String, Map<String, BigDecimal>> evolutionSuitabilityEntry : result.entrySet()) {
            dataSet.addValue(evolutionSuitabilityEntry.getValue().get(OBS_NV), parseLevelLabel(OBS_NV, messageResources), evolutionSuitabilityEntry.getKey());
            dataSet.addValue(evolutionSuitabilityEntry.getValue().get(OBS_A), parseLevelLabel(OBS_A, messageResources), evolutionSuitabilityEntry.getKey());
            dataSet.addValue(evolutionSuitabilityEntry.getValue().get(OBS_AA), parseLevelLabel(OBS_AA, messageResources), evolutionSuitabilityEntry.getKey());
        }

        final String noDataMess = "noData";
        int x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
        int y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
        final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, true, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "EvolucionNivelConformidadCombinada.jpg";
        try {
            GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
            final Image image = PDFUtils.createImage(filePath, null);
            if (image != null) {
                image.scalePercent(60);
                image.setAlignment(Element.ALIGN_CENTER);
                image.setSpacingBefore(ConstantsFont.LINE_SPACE);
                image.setSpacingAfter(ConstantsFont.LINE_SPACE);
                section.add(image);
            }
        } catch (IOException e) {
            Logger.putLog("Error al intentar crear la imagen " + filePath, BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
        }

        final float[] columnWidths = new float[result.size() + 1];
        Arrays.fill(columnWidths, 1f / (result.size() + 1));
        final PdfPTable tablaRankings = new PdfPTable(columnWidths);
        tablaRankings.setSpacingBefore(LINE_SPACE);
        tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
        tablaRankings.setWidthPercentage(100);
        tablaRankings.setHeaderRows(1);

        tablaRankings.addCell(PDFUtils.createEmptyTableCell());
        for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
            tablaRankings.addCell(PDFUtils.createTableCell(stringMapEntry.getKey(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }
        tablaRankings.completeRow();

        tablaRankings.addCell(PDFUtils.createTableCell("Prioridad 1 y 2", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
            tablaRankings.addCell(PDFUtils.createTableCell(stringMapEntry.getValue().get(Constants.OBS_AA).toString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }
        tablaRankings.completeRow();

        tablaRankings.addCell(PDFUtils.createTableCell("Prioridad 1", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
            tablaRankings.addCell(PDFUtils.createTableCell(stringMapEntry.getValue().get(Constants.OBS_A).toString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }
        tablaRankings.completeRow();

        tablaRankings.addCell(PDFUtils.createTableCell("Parcial", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
            tablaRankings.addCell(PDFUtils.createTableCell(stringMapEntry.getValue().get(Constants.OBS_NV).toString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }
        tablaRankings.completeRow();

        section.add(tablaRankings);
    }

    private void evolutionAverageScoreByVerification(MessageResources messageResources, PdfTocManager pdfTocManager, File file, Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Chapter chapter) {
        final Section section = PDFUtils.createSection("Evolución de la Puntuación Media por Verificación", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.getNumSection(), 1);

        final ArrayList<String> boldWord = new ArrayList<>();
        final String paragraph_1 = "En los apartados siguientes se presenta la variación sufrida por la Puntuación Media de las veinte verificaciones del observatorio.";
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(paragraph_1, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        final String paragraph_2 = "Mediante estas gráficas se puede obtener de forma rápida una conclusión sobre en qué requisitos de accesibilidad se está mejorando, así como aquellos en los que los problemas detectados han ido en aumento, y de esa forma poder focalizar los esfuerzos más fácilmente.";
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(paragraph_2, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        evolutionAverageScoreByVerificationByLevel1(messageResources, pdfTocManager, file, evaluationPageList, section);
        //section.newPage();
        evolutionAverageScoreByVerificationByLevel2(messageResources, pdfTocManager, file, evaluationPageList, section);
    }

    private void evolutionAverageScoreByVerificationByLevel1(MessageResources messageResources, PdfTocManager pdfTocManager, File file, Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Section section) {

        final Section subSection = PDFUtils.createSection("Nivel de Accesibilidad I", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, section, pdfTocManager.getNumSection(), 2);

        final DefaultCategoryDataset dataSet = createEvolutionAverageScoreByVerificationDataSet(evaluationPageList, LEVEL_I_VERIFICATIONS);

        addEvolutionAverageScoreByVerificationByLevelChart(dataSet, file, "EvolucionPuntuacionMediaVerificacionNAICombinada.jpg", subSection);

        final PropertiesManager pmgr = new PropertiesManager();
        final DateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

        createEvolutionBarDateLegendTable(evaluationPageList, subSection, dateFormat);

        final Map<Date, ScoreForm> scores = new TreeMap<>();
        final PdfPTable tablaRankings = createTablaRankings(evaluationPageList, scores, dateFormat);

        for (int i = 0; i < 10; i++) {
            tablaRankings.addCell(PDFUtils.createTableCell(scores.values().iterator().next().getVerifications1().get(i).getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
            for (Map.Entry<Date, ScoreForm> dateScoreFormEntry : scores.entrySet()) {
                tablaRankings.addCell(PDFUtils.createTableCell(dateScoreFormEntry.getValue().getVerifications1().get(i).getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            }
            tablaRankings.completeRow();
        }
        subSection.add(tablaRankings);
    }

    private void evolutionAverageScoreByVerificationByLevel2(MessageResources messageResources, PdfTocManager pdfTocManager, File file, Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Section section) {
        final PropertiesManager pmgr = new PropertiesManager();
        final Section subSection = PDFUtils.createSection("Nivel de Accesibilidad II", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, section, pdfTocManager.getNumSection(), 2);

        final DefaultCategoryDataset dataSet = createEvolutionAverageScoreByVerificationDataSet(evaluationPageList, LEVEL_II_VERIFICATIONS);

        addEvolutionAverageScoreByVerificationByLevelChart(dataSet, file, "EvolucionPuntuacionMediaVerificacionNAIICombinada.jpg", subSection);

        final DateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

        createEvolutionBarDateLegendTable(evaluationPageList, subSection, dateFormat);

        final Map<Date, ScoreForm> scores = new TreeMap<>();
        final PdfPTable tablaRankings = createTablaRankings(evaluationPageList, scores, dateFormat);

        for (int i = 0; i < 10; i++) {
            tablaRankings.addCell(PDFUtils.createTableCell(scores.values().iterator().next().getVerifications2().get(i).getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
            for (Map.Entry<Date, ScoreForm> dateScoreFormEntry : scores.entrySet()) {
                tablaRankings.addCell(PDFUtils.createTableCell(dateScoreFormEntry.getValue().getVerifications2().get(i).getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            }
            tablaRankings.completeRow();
        }
        subSection.add(tablaRankings);
    }

    private DefaultCategoryDataset createEvolutionAverageScoreByVerificationDataSet(final Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, final List<String> verifications) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : evaluationPageList.entrySet()) {
            final Map<String, BigDecimal> resultsByVerification = getVerificationResultsByPoint(entry.getValue(), Constants.OBS_PRIORITY_NONE);
            for (String verification : verifications) {
                //Para un observatorio en concreto recuperamos la puntuación de una verificación
                final BigDecimal value = resultsByVerification.get(verification);
                dataset.addValue(value, entry.getKey().getTime(), verification);
            }
        }
        return dataset;
    }

    private void addEvolutionAverageScoreByVerificationByLevelChart(DefaultCategoryDataset dataSet, final File path, final String filename, Section subSection) {
        final PropertiesManager pmgr = new PropertiesManager();
        final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, true, 800, 400, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
        chartForm.setFixedColorBars(true);
        chartForm.setShowColumsLabels(false);

        final String filePath = path.getParentFile().getPath() + File.separator + "temp" + File.separator + filename;
        try {
            GraphicsUtils.createStandardBarChart(chartForm, filePath, "", messageResources, true);
            final Image image = PDFUtils.createImage(filePath, null);
            if (image != null) {
                image.scalePercent(60);
                image.setAlignment(Element.ALIGN_CENTER);
                image.setSpacingBefore(ConstantsFont.LINE_SPACE);
                image.setSpacingAfter(ConstantsFont.LINE_SPACE);
                subSection.add(image);
            }
        } catch (IOException e) {
            Logger.putLog("Error al intentar crear la imagen " + filePath, BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
        }
    }

    private PdfPTable createTablaRankings(Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, final Map<Date, ScoreForm> scores, final DateFormat dateFormat) {
        final float[] columnWidths = new float[evaluationPageList.size() + 1];
        columnWidths[0] = 0.55f;
        Arrays.fill(columnWidths, 1, columnWidths.length, 0.45f / evaluationPageList.size());
        final PdfPTable tablaRankings = new PdfPTable(columnWidths);
        tablaRankings.setSpacingBefore(HALF_LINE_SPACE);
        tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
        tablaRankings.setWidthPercentage(85);
        tablaRankings.setHeaderRows(1);

        tablaRankings.addCell(PDFUtils.createTableCell("Verificación", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));

        for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
            tablaRankings.addCell(PDFUtils.createTableCell(dateFormat.format(observatoryEvaluationForms.getKey()), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }
        tablaRankings.completeRow();


        for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
            scores.put(observatoryEvaluationForms.getKey(), pdfBuilder.generateScores(messageResources, observatoryEvaluationForms.getValue()));
        }
        return tablaRankings;
    }

    private void createEvolutionBarDateLegendTable(Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Section subSection, DateFormat dateFormat) {
        final float[] tablaFechasWidths = new float[evaluationPageList.size() + 1];
        tablaFechasWidths[0] = 0.16f;
        Arrays.fill(tablaFechasWidths, 1, tablaFechasWidths.length, 0.28f);

        final PdfPTable tablaFechas = new PdfPTable(tablaFechasWidths);
        tablaFechas.setSpacingBefore(HALF_LINE_SPACE);
        tablaFechas.setSpacingAfter(HALF_LINE_SPACE);
        tablaFechas.setWidthPercentage(85);
        tablaFechas.setHeaderRows(1);
        tablaFechas.addCell(PDFUtils.createEmptyTableCell());
        for (int i = 1; i < tablaFechasWidths.length; i++) {
            tablaFechas.addCell(PDFUtils.createTableCell(i + "ª Barra", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }

        tablaFechas.completeRow();
        tablaFechas.addCell(PDFUtils.createTableCell("Fecha", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
            tablaFechas.addCell(PDFUtils.createTableCell(dateFormat.format(observatoryEvaluationForms.getKey()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }
        tablaFechas.completeRow();
        subSection.add(tablaFechas);
    }

    private Map<String, BigDecimal> calculatePercentage(final Map<String, List<ObservatoryEvaluationForm>> values) {
        final Map<String, Integer> count = new LinkedHashMap<>();

        count.put(OBS_NV, values.get(OBS_NV).size());
        count.put(OBS_A, values.get(OBS_A).size());
        count.put(OBS_AA, values.get(OBS_AA).size());

        return ResultadosAnonimosObservatorioUNE2012Utils.calculatePercentage(count);
    }

    private String getPriorityName(final MessageResources messageResources, final String name) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (name.equals(pmgr.getValue(INTAV_PROPERTIES, "priority.1"))) {
            return messageResources.getMessage("observatorio.nivel.analisis.1");
        } else if (name.equals(pmgr.getValue(INTAV_PROPERTIES, "priority.2"))) {
            return messageResources.getMessage("observatorio.nivel.analisis.2");
        } else {
            return null;
        }
    }

    private PdfPTable createPaginaTableInfo(final MessageResources messageResources, final String title, final String url, final BigDecimal puntuacionMedia, final String nivelAdecuacion, final List<BigDecimal> puntuacionesMediasNivel, int countSections) {
        final float[] widths = {0.22f, 0.78f};
        final PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(0);

        // Titulo
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.title"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell(title, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));

        // URL
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createLinkedTableCell(url, url, Color.WHITE, Element.ALIGN_LEFT, DEFAULT_PADDING));

        // Puntuación Media Página
        table.addCell(PDFUtils.createTableCell("Puntuación Media", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell(puntuacionMedia.toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));

        // Nivel de Adecuación (a.k.a. Modalidad)
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell(nivelAdecuacion, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));

        // Puntuaciones Medias Nivel Accesibilidad
        int contador = 1;
        for (BigDecimal puntuacionMediaNivel : puntuacionesMediasNivel) {
            // Puntuación Media Nivel Accesibilidad
            table.addCell(PDFUtils.createTableCell("Puntuación Media\nNivel de Análisis " + contador++, Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
            table.addCell(PDFUtils.createTableCell(puntuacionMediaNivel.toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
        }

        return table;
    }

    private PdfPTable createPaginaTableVerificationSummary(final MessageResources messageResources, final ObservatoryLevelForm observatoryLevelForm) {
        final float[] widths = {0.60f, 0.20f, 0.20f};
        final PdfPTable table = new PdfPTable(widths);

        table.setSpacingBefore(10);
        table.setSpacingAfter(5);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.valor"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
                String value = null;
                String modality = null;
                Font fuente = null;
                final int observatorySubgroupFormValue = observatorySubgroupForm.getValue();
                try {
                    if (observatorySubgroupFormValue == Constants.OBS_VALUE_GREEN_ONE) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.uno");
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                        fuente = ConstantsFont.descriptionFont;
                    } else if (observatorySubgroupFormValue == Constants.OBS_VALUE_GREEN_ZERO) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                        fuente = ConstantsFont.WARNING_FONT;
                    } else if (observatorySubgroupFormValue == Constants.OBS_VALUE_RED_ZERO) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla");
                        fuente = ConstantsFont.descriptionFontRed;
                    } else if (observatorySubgroupFormValue == Constants.OBS_VALUE_NOT_SCORE) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua");
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                        fuente = ConstantsFont.descriptionFont;
                    }
                } catch (Exception e) {
                    Logger.putLog("Error al crear tabla, resultados primarios.", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR, e);
                }

                final PdfPCell descriptionCell = new PdfPCell(new Paragraph(messageResources.getMessage(observatorySubgroupForm.getDescription()), ConstantsFont.descriptionFont));
                final PdfPCell valueCell = PDFUtils.createTableCell(value, Color.WHITE, fuente, Element.ALIGN_CENTER, 0);
                final PdfPCell modalityCell;
                if (modality != null && modality.equals(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"))) {
                    modalityCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"), Color.WHITE, fuente, Element.ALIGN_CENTER, 0);
                } else {
                    modalityCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla"), Color.WHITE, fuente, Element.ALIGN_CENTER, 0);
                }
                modalityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                modalityCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                table.addCell(descriptionCell);
                table.addCell(valueCell);
                table.addCell(modalityCell);
            }
        }

        return table;
    }

    private void addCheckCodes(final MessageResources messageResources, final ObservatoryEvaluationForm evaluationForm, final Chapter chapter, boolean isBasicService) throws BadElementException, IOException {
        final PropertiesManager pmgr = new PropertiesManager();

        for (ObservatoryLevelForm priority : evaluationForm.getGroups()) {
            if (hasProblems(priority)) {
                final Section prioritySection = PDFUtils.createSection(getPriorityName(messageResources, priority), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, 1, 0);
                for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
                    if (hasProblems(level)) {
                        final Section levelSection = PDFUtils.createSection(getLevelName(messageResources, level), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, prioritySection, 1, 0);
                        for (ObservatorySubgroupForm verification : level.getSubgroups()) {
                            if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                                for (ProblemForm problem : verification.getProblems()) {
                                    final PdfPTable tablaVerificacionProblema = new PdfPTable(new float[]{0.13f, 0.87f});
                                    tablaVerificacionProblema.setSpacingBefore(10);
                                    tablaVerificacionProblema.setWidthPercentage(100);
                                    levelSection.add(tablaVerificacionProblema);

                                    final PdfPCell verificacion = PDFUtils.createTableCell(messageResources.getMessage(verification.getDescription()), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
                                    verificacion.setColspan(2);
                                    tablaVerificacionProblema.addCell(verificacion);

                                    final String problema;
                                    final com.lowagie.text.Font font;
                                    if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
                                        problema = messageResources.getMessage("pdf.accessibility.bs.warning");
                                        font = ConstantsFont.WARNING_FONT;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
                                        problema = messageResources.getMessage("pdf.accessibility.bs.problem");
                                        font = ConstantsFont.PROBLEM_FONT;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
                                        problema = messageResources.getMessage("pdf.accessibility.bs.info");
                                        font = ConstantsFont.CANNOTTELL_FONT;
                                    } else {
                                        problema = "";
                                        font = ConstantsFont.CANNOTTELL_FONT;
                                    }
                                    final PdfPCell celdaProblema = PDFUtils.createTableCell(problema, Color.WHITE, font, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
                                    celdaProblema.setBorder(0);
                                    celdaProblema.setVerticalAlignment(Element.ALIGN_TOP);
                                    tablaVerificacionProblema.addCell(celdaProblema);

                                    final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
                                    final PdfPCell comprobacion = PDFUtils.createTableCell(StringUtils.removeHtmlTags(checkDescriptionsManager.getErrorMessage(problem.getCheck())), Color.WHITE, ConstantsFont.strongDescriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
                                    comprobacion.setBorder(0);
                                    comprobacion.setVerticalAlignment(Element.ALIGN_TOP);
                                    tablaVerificacionProblema.addCell(comprobacion);

                                    if (isBasicService) {
                                        final String rationaleMessage = checkDescriptionsManager.getRationaleMessage(problem.getCheck());
                                        if (rationaleMessage != null && StringUtils.isNotEmpty(rationaleMessage)) {
                                            final Paragraph rationale = new Paragraph();
                                            boolean isFirst = true;
                                            for (String phraseText : Arrays.asList(rationaleMessage.split("<p>|</p>"))) {
                                                if (isFirst) {
                                                    if (StringUtils.isNotEmpty(phraseText)) {
                                                        rationale.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.descriptionFont));
                                                    }
                                                    isFirst = false;
                                                } else {
                                                    rationale.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.descriptionFont));
                                                }
                                            }

                                            tablaVerificacionProblema.addCell(PDFUtils.createEmptyTableCell());

                                            final PdfPCell celdaRationale = new PdfPCell(rationale);
                                            celdaRationale.setBorder(0);
                                            celdaRationale.setBackgroundColor(Color.WHITE);
                                            celdaRationale.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                            celdaRationale.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            celdaRationale.setPadding(DEFAULT_PADDING);
                                            tablaVerificacionProblema.addCell(celdaRationale);
                                        }

                                        BasicServiceExport.addSpecificProblems(messageResources, levelSection, problem.getSpecificProblems());
                                    }

                                    if (problem.getCheck().equals("232") || //pmgr.getValue("check.properties", "doc.valida.especif")) ||
                                            EvaluatorUtils.isCssValidationCheck(Integer.parseInt(problem.getCheck()))) {
                                        BasicServiceExport.addW3CCopyright(levelSection, problem.getCheck());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean hasProblems(final ObservatoryLevelForm priority) {
        for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm verification : level.getSubgroups()) {
                if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasProblems(final ObservatorySuitabilityForm level) {
        for (ObservatorySubgroupForm verification : level.getSubgroups()) {
            if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private String getLevelName(final MessageResources messageResources, final ObservatorySuitabilityForm level) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (level.getName().equals(pmgr.getValue(INTAV_PROPERTIES, "priority.1.level").toUpperCase())) {
            return messageResources.getMessage("first.level.bs");
        } else if (level.getName().equals(pmgr.getValue(INTAV_PROPERTIES, "priority.2.level").toUpperCase())) {
            return messageResources.getMessage("second.level.bs");
        } else {
            return "";
        }
    }

    private String getPriorityName(final MessageResources messageResources, final ObservatoryLevelForm priority) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (priority.getName().equals(pmgr.getValue(INTAV_PROPERTIES, "priority.1"))) {
            return messageResources.getMessage("priority1.bs");
        } else if (priority.getName().equals(pmgr.getValue(INTAV_PROPERTIES, "priority.2"))) {
            return messageResources.getMessage("priority2.bs");
        } else {
            return "";
        }
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
