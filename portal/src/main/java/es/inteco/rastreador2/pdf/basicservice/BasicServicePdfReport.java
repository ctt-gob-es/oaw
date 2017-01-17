package es.inteco.rastreador2.pdf.basicservice;

import com.lowagie.text.*;
import com.lowagie.text.Font;
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
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

import static es.inteco.common.Constants.INTAV_PROPERTIES;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;
import static es.inteco.common.ConstantsFont.HALF_LINE_SPACE;

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
                final List<ObservatoryEvaluationForm> previousEvaluation = getPreviousEvaluation(previousEvaluationPageList);
                final BasicServiceObservatoryScorePdfSectionBuilder observatoryScoreSectionBuilder = new BasicServiceObservatoryScorePdfSectionBuilder(currentEvaluationPageList, previousEvaluation);
                observatoryScoreSectionBuilder.addObservatoryScoreSummary(pdfBuilder, messageResources, document, pdfTocManager, file);

                // Resumen de las puntuaciones del Observatorio
                final BasicServiceObservatoryResultsSummaryPdfSectionBuilder observatoryResultsSummarySectionBuilder = new BasicServiceObservatoryResultsSummaryPdfSectionBuilder(currentEvaluationPageList);
                observatoryResultsSummarySectionBuilder.addObservatoryResultsSummary(messageResources, document, pdfTocManager);

                // Evolución resultados servicio diagnóstico
                final Map<Date, List<ObservatoryEvaluationForm>> evolutionObservatoryEvaluation = new TreeMap<>(previousEvaluationPageList);
                evolutionObservatoryEvaluation.put(pdfBuilder.getBasicServiceForm().getDate(), currentEvaluationPageList);
                final BasicServiceObservatoryEvolutionResultsPdfSectionBuilder observatoryEvolutionResultsSectionBuilder = new BasicServiceObservatoryEvolutionResultsPdfSectionBuilder(evolutionObservatoryEvaluation);
                observatoryEvolutionResultsSectionBuilder.addEvolutionResults(pdfBuilder, messageResources, document, pdfTocManager, file);

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
                    chapter.add(createPaginaTableInfo(messageResources, title, url, puntuacionMediaPortal, validationLevel, puntuacionesMediasNivelAccesibilidad));

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

    private PdfPTable createPaginaTableInfo(final MessageResources messageResources, final String title, final String url, final BigDecimal puntuacionMedia, final String nivelAdecuacion, final List<BigDecimal> puntuacionesMediasNivel) {
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
