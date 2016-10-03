package es.inteco.rastreador2.pdf.utils;

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
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.*;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.PrimaryExportPdfAction;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2004;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.utils.*;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.ConstantsFont.*;

public final class PrimaryExportPdfUtils {

    private PrimaryExportPdfUtils() {
    }

    public static void exportToPdf(final Long idRastreo, final Long idRastreoRealizado, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
                                   final long idObservatoryExecution, final long observatoryType) throws Exception {

        AnonymousResultExportPdf builder = null;
        try (Connection c = DataBaseManager.getConnection()) {
            final FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(c, idRastreoRealizado);
            final String application = CartuchoDAO.getApplication(c, Long.valueOf(crawling.getIdCartridge()));
            Logger.putLog("Normativa " + application, PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_INFO);
            if ("UNE-2012".equalsIgnoreCase(application)) {
                builder = new AnonymousResultExportPdfUNE2012();
            } else if ("UNE-2004".equalsIgnoreCase(application)) {
                builder = new AnonymousResultExportPdfUNE2004();
            }
        } catch (Exception e) {
            Logger.putLog("Error al preparar el builder de PDF", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
            builder = new AnonymousResultExportPdfUNE2004();
        }

        exportToPdf(builder, idRastreo, idRastreoRealizado, request, generalExpPath, seed, content, idObservatoryExecution, observatoryType);
    }

    public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idRastreo, final Long idRastreoRealizado, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
                                   final long idObservatoryExecution, final long observatoryType) throws Exception {
        final boolean isBasicService = idRastreoRealizado < 0;
        try (Connection c = DataBaseManager.getConnection()) {
            final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
            final List<Long> previousEvaluationIds;
            if (!isBasicService) {
                final es.ctic.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.ctic.rastreador2.observatorio.ObservatoryManager();
                previousEvaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(observatoryManager.getPreviousIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(idRastreo, ObservatorioDAO.getPreviousObservatoryExecution(c, idObservatoryExecution)));
            } else {
                previousEvaluationIds = Collections.emptyList();
            }

            exportToPdf(pdfBuilder, idRastreoRealizado, evaluationIds, previousEvaluationIds, request, generalExpPath, seed, content, idObservatoryExecution, observatoryType);
        }
    }

    public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idRastreoRealizado, final List<Long> evaluationIds, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
                                   final long idObservatoryExecution, final long observatoryType) throws Exception {
        exportToPdf(pdfBuilder, idRastreoRealizado, evaluationIds, Collections.<Long>emptyList(), request, generalExpPath, seed, content, idObservatoryExecution, observatoryType);
    }

    public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idRastreoRealizado, final List<Long> evaluationIds, final List<Long> previousEvaluationIds, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
                                   final long idObservatoryExecution, final long observatoryType) throws Exception {
        exportToPdf(pdfBuilder, idRastreoRealizado, evaluationIds, previousEvaluationIds, CrawlerUtils.getResources(request), request, generalExpPath, seed, content, idObservatoryExecution, observatoryType);
    }

    public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idRastreoRealizado, final List<Long> evaluationIds, final List<Long> previousEvaluationIds, final MessageResources messageResources, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
                                   final long idObservatoryExecution, final long observatoryType) throws Exception {
        final File file = new File(generalExpPath);
        file.setReadable(true, false);
        file.setWritable(true, false);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            file.getParentFile().setReadable(true, false);
            file.getParentFile().setWritable(true, false);
            Logger.putLog("Exception: No se ha podido crear los directorios al exportar a PDF", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR);
        }
        Logger.putLog("Exportando a PDF PrimaryExportPdfUtils.exportToPdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_DEBUG);
        final FileOutputStream outputFileStream = new FileOutputStream(file);
        // TODO: Add document metadata (author, creator, subject, title...)
        final Document document = new Document(PageSize.A4, 50, 50, 110, 72);

        try (Connection connection = DataBaseManager.getConnection()) {
            final FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(connection, idRastreoRealizado);

            final es.ctic.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.ctic.rastreador2.observatorio.ObservatoryManager();

            final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(idObservatoryExecution, evaluationIds);
            final List<ObservatoryEvaluationForm> previousEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(idObservatoryExecution, previousEvaluationIds);

            final PdfWriter writer = PdfWriter.getInstance(document, outputFileStream);
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));

            final String crawlingDate = crawling != null ? crawling.getDate() : CrawlerUtils.formatDate(new Date());

            final String footerText = messageResources.getMessage("ob.resAnon.intav.report.foot", new String[]{seed, crawlingDate});
            writer.setPageEvent(new ExportPageEventsObservatoryMP(footerText, crawlingDate, pdfBuilder.isBasicService()));
            ExportPageEventsObservatoryMP.setPrintFooter(true);

            final IndexEvents index = new IndexEvents();
            writer.setPageEvent(index);
            writer.setLinearPageMode();

            document.open();

            if (pdfBuilder.isBasicService()) {
                PDFUtils.addCoverPage(document, messageResources.getMessage("pdf.accessibility.title", new String[]{seed.toUpperCase(), pdfBuilder.getTitle()}), request.getParameter("url"), "Informe emitido bajo demanda.");
            } else {
                PDFUtils.addCoverPage(document, messageResources.getMessage("pdf.accessibility.title", new String[]{seed.toUpperCase(), pdfBuilder.getTitle()}), currentEvaluationPageList.get(0).getUrl());
            }

            int numChapter = 1;
            int countSections = 1;

            countSections = pdfBuilder.createIntroductionChapter(messageResources, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont);
            numChapter++;

            countSections = pdfBuilder.createObjetiveChapter(messageResources, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, currentEvaluationPageList, observatoryType);
            numChapter++;

            // Resumen de las puntuaciones del Observatorio
            final RankingInfo rankingActual = crawling != null ? observatoryManager.calculateRanking(idObservatoryExecution, crawling.getSeed()) : null;
            final RankingInfo rankingPrevio = crawling != null ? observatoryManager.calculatePreviousRanking(idObservatoryExecution, crawling.getSeed()) : null;
            countSections = addObservatoryScoreSummary(pdfBuilder, messageResources, document, index, currentEvaluationPageList, previousEvaluationPageList, numChapter, countSections, file, rankingActual, rankingPrevio);

            numChapter++;

            // Resumen de las puntuaciones del Observatorio
            countSections = addObservatoryResultsSummary(messageResources, document, index, currentEvaluationPageList, numChapter, countSections);
            numChapter++;

            int counter = 1;
            for (ObservatoryEvaluationForm evaluationForm : currentEvaluationPageList) {
                final String evaluationTitle = messageResources.getMessage("observatory.graphic.score.by.page.label", counter);
                final Chapter chapter = PDFUtils.createChapterWithTitle(evaluationTitle, index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont, true, "anchor_resultados_page_" + counter);
                final String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
                final String url = evaluationForm.getUrl();

                final BigDecimal pmp = evaluationForm.getScore();
                final String validationLevel = ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm));
                final List<BigDecimal> pmpnas = new ArrayList<>();
                for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                    pmpnas.add(observatoryLevelForm.getScore());
                }
                chapter.add(createPaginaTableInfo(messageResources, title, url, pmp, validationLevel, pmpnas, countSections));

                // Creación de las tablas resumen de resultado por verificación de cada página
                for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                    final Paragraph levelTitle = new Paragraph(getPriorityName(messageResources, observatoryLevelForm.getName()), ConstantsFont.chapterTitleMPFont3L);
                    levelTitle.setSpacingBefore(HALF_LINE_SPACE);
                    chapter.add(levelTitle);
                    chapter.add(createPaginaTableVerificationSummary(messageResources, observatoryLevelForm));
                }

                addCheckCodes(messageResources, evaluationForm, chapter, pdfBuilder.isBasicService());

                if (!pdfBuilder.isBasicService()) {
                    final SpecialChunk externalLink = new SpecialChunk(messageResources.getMessage("observatory.servicio.diagnostico.url"), ConstantsFont.ANCHOR_FONT);
                    externalLink.setExternalLink(true);
                    externalLink.setAnchor(messageResources.getMessage("observatory.servicio.diagnostico.url"));
                    final Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
                    specialChunkMap.put(1, externalLink);
                    chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.errores.mas.info"), specialChunkMap, ConstantsFont.PARAGRAPH));
                }

                document.add(chapter);
                numChapter++;
                counter++;
            }

            countSections = pdfBuilder.createMethodologyChapter(messageResources, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, currentEvaluationPageList, observatoryType, pdfBuilder.isBasicService());

            //Ponemos la variable a true para que no se escriba el footer en el índice
            IndexUtils.createIndex(writer, document, messageResources, index, ConstantsFont.chapterTitleMPFont);
            ExportPageEventsObservatoryMP.setPrintFooter(true);
        } catch (DocumentException e) {
            Logger.putLog("Error al exportar a pdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } catch (Exception e) {
            Logger.putLog("Excepción genérica al generar el pdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            if (document.isOpen()) {
                try {
                    document.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el pdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
            outputFileStream.close();
        }
    }

    private static PdfPTable createPaginaTableInfo(final MessageResources messageResources, final String title, final String url, final BigDecimal puntuacionMedia, final String nivelAdecuacion, final List<BigDecimal> puntuacionesMediasNivel, int countSections) {
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

    private static void addCheckCodes(final MessageResources messageResources, final ObservatoryEvaluationForm evaluationForm, final Chapter chapter, boolean isBasicService) throws BadElementException, IOException {
        final PropertiesManager pmgr = new PropertiesManager();

        for (ObservatoryLevelForm priority : evaluationForm.getGroups()) {
            if (hasProblems(priority)) {
                final Section prioritySection = PDFUtils.createSection(getPriorityName(messageResources, priority), null, ConstantsFont.chapterTitleMPFont2L, chapter, 1, 0);
                for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
                    if (hasProblems(level)) {
                        final Paragraph levelTitle = new Paragraph(getLevelName(messageResources, level), ConstantsFont.chapterTitleMPFont3L);
                        levelTitle.setSpacingBefore(10);
                        final Section levelSection = PDFUtils.createSection(getLevelName(messageResources, level), null, ConstantsFont.chapterTitleMPFont3L, prioritySection, 1, 0);
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
                                        font = ConstantsFont.warningFont;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
                                        problema = messageResources.getMessage("pdf.accessibility.bs.problem");
                                        font = ConstantsFont.problemFont;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
                                        problema = messageResources.getMessage("pdf.accessibility.bs.info");
                                        font = ConstantsFont.cannottellFont;
                                    } else {
                                        problema = "";
                                        font = ConstantsFont.cannottellFont;
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

                                    if (problem.getCheck().equals(pmgr.getValue("check.properties", "doc.valida.especif")) ||
                                            problem.getCheck().equals(pmgr.getValue("check.properties", "css.valida.especif"))) {
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

    private static boolean hasProblems(final ObservatoryLevelForm priority) {
        for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm verification : level.getSubgroups()) {
                if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean hasProblems(final ObservatorySuitabilityForm level) {
        for (ObservatorySubgroupForm verification : level.getSubgroups()) {
            if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private static String getPriorityName(final MessageResources messageResources, final ObservatoryLevelForm priority) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (priority.getName().equals(pmgr.getValue("intav.properties", "priority.1"))) {
            return messageResources.getMessage("priority1.bs");
        } else if (priority.getName().equals(pmgr.getValue("intav.properties", "priority.2"))) {
            return messageResources.getMessage("priority2.bs");
        } else {
            return "";
        }
    }

    private static String getLevelName(final MessageResources messageResources, final ObservatorySuitabilityForm level) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (level.getName().equals(pmgr.getValue("intav.properties", "priority.1.level").toUpperCase())) {
            return messageResources.getMessage("first.level.bs");
        } else if (level.getName().equals(pmgr.getValue("intav.properties", "priority.2.level").toUpperCase())) {
            return messageResources.getMessage("second.level.bs");
        } else {
            return "";
        }
    }

    private static int addObservatoryScoreSummary(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, Document document, IndexEvents index, final List<ObservatoryEvaluationForm> currentEvaluationPageList, List<ObservatoryEvaluationForm> previousEvaluationPageList, int numChapter, int countSections, final File file, final RankingInfo rankingActual, final RankingInfo rankingPrevio) throws Exception {
        final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("observatorio.puntuacion.resultados.resumen").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);
        final ArrayList<String> boldWord = new ArrayList<>();
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p1"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWord.clear();
        boldWord.add(messageResources.getMessage("resultados.primarios.4.p2.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p2"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWord.clear();
        boldWord.add(messageResources.getMessage("resultados.primarios.4.p3.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p3"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        final ScoreForm currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
        final ScoreForm previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);

        //// TABLA PUNTUCAION OBSERVATORIO
        final float[] columnWidths;
        if (rankingPrevio != null) {
            columnWidths = new float[]{0.42f, 0.20f, 0.20f, 0.18f};
        } else {
            columnWidths = new float[]{0.6f, 0.4f};
        }

        final PdfPTable tablaRankings = new PdfPTable(columnWidths);
        tablaRankings.setSpacingBefore(LINE_SPACE);
        tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
        tablaRankings.setWidthPercentage(90);
        tablaRankings.setHeaderRows(1);

        tablaRankings.addCell(PDFUtils.createEmptyTableCell());

        if (pdfBuilder.isBasicService()) {
            tablaRankings.addCell(PDFUtils.createTableCell("Resultado", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        } else {
            tablaRankings.addCell(PDFUtils.createTableCell("Resultado\n" + rankingActual.getDate(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }
        if (rankingPrevio != null) {
            tablaRankings.addCell(PDFUtils.createTableCell("Resultado\n" + rankingPrevio.getDate(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            tablaRankings.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }

        tablaRankings.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
        tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        if (rankingPrevio != null) {
            tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getLevel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            tablaRankings.addCell(createEvolutionLevelCell(messageResources, currentScore.getLevel(), previousScore.getLevel()));
        }
        tablaRankings.completeRow();

        tablaRankings.addCell(PDFUtils.createTableCell("Puntuación Media del Portal", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
        tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        if (rankingPrevio != null) {
            tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(currentScore.getTotalScore(), previousScore.getTotalScore()), String.valueOf(currentScore.getTotalScore().subtract(previousScore.getTotalScore()).toPlainString()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
        }
        tablaRankings.completeRow();

        if (rankingActual != null) {
            tablaRankings.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.posicion.global"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
            tablaRankings.addCell(PDFUtils.createTableCell(rankingActual.getGlobalRank() + " \n(" + messageResources.getMessage("de.text", rankingActual.getGlobalSeedsNumber()) + ")", Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            if (rankingPrevio != null) {
                tablaRankings.addCell(PDFUtils.createTableCell(rankingPrevio.getGlobalRank() + " \n(" + messageResources.getMessage("de.text", rankingPrevio.getGlobalSeedsNumber()) + ")", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
                tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(rankingActual.getGlobalRank(), rankingPrevio.getGlobalRank(), true), String.valueOf(rankingPrevio.getGlobalRank() - rankingActual.getGlobalRank()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
            }
            tablaRankings.completeRow();

            tablaRankings.addCell(PDFUtils.createTableCell("Posición en " + rankingActual.getCategoria().getName(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
            tablaRankings.addCell(PDFUtils.createTableCell(rankingActual.getCategoryRank() + " \n(" + messageResources.getMessage("de.text", rankingActual.getCategorySeedsNumber()) + ")", Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            if (rankingPrevio != null) {
                tablaRankings.addCell(PDFUtils.createTableCell(rankingPrevio.getCategoryRank() + " \n(" + messageResources.getMessage("de.text", rankingPrevio.getCategorySeedsNumber()) + ")", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
                tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(rankingActual.getCategoryRank(), rankingPrevio.getCategoryRank(), true), String.valueOf(rankingPrevio.getCategoryRank() - rankingActual.getCategoryRank()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
            }
            tablaRankings.completeRow();
        }

        chapter.add(tablaRankings);

        final String noDataMess = messageResources.getMessage("grafica.sin.datos");
        // Obtener lista resultados iteración anterior
        addLevelAllocationResultsSummary(messageResources, chapter, file, currentEvaluationPageList, previousEvaluationPageList, noDataMess, pdfBuilder.isBasicService());

        Section section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion1"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.41.p1"), ConstantsFont.PARAGRAPH, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_1);
        section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, rankingPrevio != null ? previousScore : null, Constants.OBS_PRIORITY_1, pdfBuilder.isBasicService()));

        section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion2"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.42.p1"), ConstantsFont.PARAGRAPH, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_2);
        section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, rankingPrevio != null ? previousScore : null, Constants.OBS_PRIORITY_2, pdfBuilder.isBasicService()));

        PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuacion.pagina"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        addResultsByPage(messageResources, chapter, file, currentEvaluationPageList, noDataMess);

        document.add(chapter);
        return countSections;
    }

    /**
     * Crea una celda PdfPCell para una tabla del informa PDF con la evolución del nivel de accesibilidad.
     *
     * @param messageResources
     * @param currentLevel     String nivel de accesibilidad actual.
     * @param previousLevel    String nivel de accesibilidad de la iteración anterior.
     * @return una celda PdfPCell con una imagen que indica la evolución y una cadena con la misma información complementando la imagen.
     */
    private static PdfPCell createEvolutionLevelCell(final MessageResources messageResources, final String currentLevel, final String previousLevel) {
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

    private static Image getEvolutionImage(final int actualValue, final int previousValue, boolean invertedEvolution) {
        if (invertedEvolution) {
            return getEvolutionImage(previousValue, actualValue);
        } else {
            return getEvolutionImage(actualValue, previousValue);
        }
    }

    private static Image getEvolutionImage(final BigDecimal actualValue, final BigDecimal previousValue) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (actualValue.compareTo(previousValue) > 0) {
            return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Aumenta");
        } else if (actualValue.compareTo(previousValue) < 0) {
            return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Disminuye");
        } else {
            return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene");
        }
    }

    private static Image getEvolutionImage(final int actualValue, final int previousValue) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (actualValue > previousValue) {
            return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Aumenta");
        } else if (actualValue < previousValue) {
            return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Disminuye");
        } else {
            return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene");
        }
    }

    private static int addObservatoryResultsSummary(final MessageResources messageResources, Document document, IndexEvents index, List<ObservatoryEvaluationForm> evaList, int numChapter, int countSections) throws Exception {
        Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("resultados.primarios.res.verificacion").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p2"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
        countSections = addResultsByVerification(messageResources, chapter, evaList, countSections, index);
        document.add(chapter);

        return countSections;
    }

    private static void addLevelAllocationResultsSummary(final MessageResources messageResources, final Section section, final File file,
                                                         final List<ObservatoryEvaluationForm> currentEvaluationPageList, final List<ObservatoryEvaluationForm> previousEvaluationPageList,
                                                         final String noDataMess, final boolean isBasicService) throws Exception {
        final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);
        final Map<String, Integer> previousResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(previousEvaluationPageList);

        final List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);
        final List<GraphicData> previousGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, previousResultsByLevel);

        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test.jpg";
        final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
        ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, currentEvaluationPageList, title, filePath, noDataMess);
        final Image image = PDFUtils.createImage(filePath, null);
        if (image != null) {
            image.scalePercent(60);
            image.setAlignment(Element.ALIGN_CENTER);
            image.setSpacingAfter(ConstantsFont.LINE_SPACE);
            section.add(image);
        }

        final float[] columnsWidth;
        if (!previousEvaluationPageList.isEmpty()) {
            columnsWidth = new float[]{30f, 25f, 30f, 15f};
        } else {
            columnsWidth = new float[]{35f, 30f, 35f};
        }
        final PdfPTable table = new PdfPTable(columnsWidth);
        table.setWidthPercentage(90);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.num.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        if (!previousEvaluationPageList.isEmpty()) {
            table.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        }
        table.completeRow();

        final ListIterator<GraphicData> actualDataIterator = currentGlobalAccessibilityLevel.listIterator();
        final ListIterator<GraphicData> previousDataIterator = previousGlobalAccessibilityLevel.listIterator();

        assert currentGlobalAccessibilityLevel.size() == previousGlobalAccessibilityLevel.size();

        while (actualDataIterator.hasNext()) {
            final GraphicData actualData = actualDataIterator.next();
            final GraphicData previousData = previousDataIterator.hasNext() ? previousDataIterator.next() : actualData;
            table.addCell(PDFUtils.createTableCell(actualData.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(actualData.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(actualData.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            if (!previousEvaluationPageList.isEmpty()) {
                try {
                    table.addCell(PDFUtils.createTableCell(getEvolutionImage(actualData.getRawPercentage(), previousData.getRawPercentage()), new DecimalFormat("+0.00;-0.00").format(actualData.getRawPercentage().subtract(previousData.getRawPercentage())) + "%", Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
                } catch (NumberFormatException nfe) {
                    table.addCell(PDFUtils.createTableCell("Errror", Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
                }
            }
            table.completeRow();
        }
        if (!isBasicService) {
            table.setSpacingBefore(ConstantsFont.LINE_SPACE * 2f);
        } else {
            table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
        }
        section.add(table);
    }

    private static void addMidsComparationByVerificationLevelGraphic(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final Section section, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess, final String level) throws Exception {
        final String title;
        final String filePath;
        if (level.equals(Constants.OBS_PRIORITY_1)) {
            title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.title");
            filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test2.jpg";
        } else { //if (level.equals(Constants.OBS_PRIORITY_2)) {
            title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.title");
            filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test3.jpg";
        }
        final PropertiesManager pmgr = new PropertiesManager();
        pdfBuilder.getMidsComparationByVerificationLevelGraphic(messageResources, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), true);
        final Image image = PDFUtils.createImage(filePath, null);
        if (image != null) {
            image.scalePercent(60);
            section.add(image);
        }
    }

    private static void addResultsByPage(final MessageResources messageResources, final Chapter chapter, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess) throws Exception {
        final Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
        final SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("resultados.primarios.43.p1.anchor"), messageResources.getMessage("anchor.PMP"), false, ConstantsFont.ANCHOR_FONT);
        anchorMap.put(1, anchor);
        chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.43.p1"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.43.p2"), ConstantsFont.PARAGRAPH, chapter);

        chapter.add(Chunk.NEWLINE);

        final String title = messageResources.getMessage("observatory.graphic.score.by.page.title");
        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test4.jpg";
        ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(messageResources, evaList, title, filePath, noDataMess);

        final Image image = PDFUtils.createImage(filePath, null);
        if (image != null) {
            image.scalePercent(70);
            image.setAlignment(Element.ALIGN_CENTER);
            chapter.add(image);
        }

        final float[] widths = {33f, 33f, 33f};
        final PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.setHeaderRows(1);
        table.setKeepTogether(true);

        int pageCounter = 1;
        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table.addCell(PDFUtils.createLinkedTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", pageCounter), evaluationForm.getUrl(), Color.white, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(String.valueOf(evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1, RoundingMode.UNNECESSARY)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            pageCounter++;
        }

        table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
        table.setSpacingAfter(0);
        chapter.add(table);
    }

    private static int addResultsByVerification(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, int countSections, final IndexEvents index) {
        countSections = createTablaResumenResultadosPorNivel(messageResources, chapter, evaList, 0, countSections, index);

        if (evaList.size() > 2 && evaList.size() < 17) {
            chapter.newPage();
        }

        countSections = createTablaResumenResultadosPorNivel(messageResources, chapter, evaList, 1, countSections, index);

        return countSections;
    }


    private static int createTablaResumenResultadosPorNivel(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final int groupIndex, int countSections, final IndexEvents index) {
        final ObservatoryLevelForm observatoryLevelForm = evaList.get(0).getGroups().get(groupIndex);
        final Section section = PDFUtils.createSection("Tabla resumen de resultados " + getPriorityName(messageResources, observatoryLevelForm.getName()), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        final PdfPTable table = createTablaResumenResultadosPorNivelHeader(messageResources, observatoryLevelForm.getSuitabilityGroups());

        int contadorPagina = 1;
        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(contadorPagina), 2, ' ')), Color.WHITE, ConstantsFont.ANCHOR_FONT, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + contadorPagina));
            for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(groupIndex).getSuitabilityGroups()) {
                for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                    table.addCell(createTablaResumenResultadosPorNivelCeldaValorModalidad(subgroupForm.getValue()));
                }
            }
            contadorPagina++;
        }
        section.add(table);
        section.add(createTablaResumenResultadosPorNivelLeyenda(messageResources, observatoryLevelForm));

        return countSections;
    }

    private static PdfPTable createTablaResumenResultadosPorNivelHeader(final MessageResources messageResources, final List<ObservatorySuitabilityForm> suitabilityGroups) {
        final float[] widths = {0.30f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f};
        final PdfPTable table = new PdfPTable(widths);
        table.setKeepTogether(true);
        table.setWidthPercentage(95);
        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setSpacingAfter(ConstantsFont.THIRD_LINE_SPACE);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));

        for (ObservatorySuitabilityForm suitabilityForm : suitabilityGroups) {
            for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                table.addCell(PDFUtils.createTableCell(messageResources.getMessage(subgroupForm.getDescription()).substring(0, 6), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            }
        }
        return table;
    }

    private static PdfPTable createTablaResumenResultadosPorNivelLeyenda(final MessageResources messageResources, final ObservatoryLevelForm evaList) {
        final PdfPTable table = new PdfPTable(new float[]{0.40f, 0.60f});
        table.setSpacingBefore(0);
        table.setWidthPercentage(65);
        table.setKeepTogether(true);

        final com.lowagie.text.List leyendaValoresResultados = new com.lowagie.text.List(false, false);
        leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Valor No Puntua", "-:"));
        leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Valor 1", "1:"));
        leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Valor 0", "0:"));
        leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Modalidad PASA", "P:"));
        leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Modalidad FALLA", "F:"));

        final PdfPCell leyendaValoresResultadosTableCell = PDFUtils.createListTableCell(leyendaValoresResultados, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
        leyendaValoresResultadosTableCell.setBorder(0);
        table.addCell(leyendaValoresResultadosTableCell);
        final com.lowagie.text.List leyenda = new com.lowagie.text.List(false, false);
        for (ObservatorySuitabilityForm suitabilityForm : evaList.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                final String checkId = messageResources.getMessage(subgroupForm.getDescription()).substring(0, 6);
                final String checkDescription = messageResources.getMessage(subgroupForm.getDescription()).substring(6);
                final ListItem listItem = new ListItem(checkDescription, ConstantsFont.moreInfoFont);
                listItem.setListSymbol(new Chunk(checkId));
                leyenda.add(listItem);
            }
            final PdfPCell listTableCell = PDFUtils.createListTableCell(leyenda, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
            listTableCell.setBorder(0);
            table.addCell(listTableCell);
        }
        return table;
    }

    private static PdfPCell createTablaResumenResultadosPorNivelCeldaValorModalidad(final int value) {
        final String valor;
        final String modalidad;
        final Font fuente;
        final Color backgroundColor;

        switch (value) {
            case Constants.OBS_VALUE_NOT_SCORE:
                valor = "-";
                modalidad = "P";
                fuente = ConstantsFont.descriptionFont;
                backgroundColor = Constants.COLOR_RESULTADO_1_PASA;
                break;
            case Constants.OBS_VALUE_RED_ZERO:
                valor = "0";
                modalidad = "F";
                fuente = ConstantsFont.labelHeaderCellFont;
                backgroundColor = Constants.COLOR_RESULTADO_0_FALLA;
                break;
            case Constants.OBS_VALUE_GREEN_ZERO:
                valor = "0";
                modalidad = "P";
                fuente = ConstantsFont.labelHeaderCellFont;
                backgroundColor = Constants.COLOR_RESULTADO_0_PASA;
                break;
            case Constants.OBS_VALUE_GREEN_ONE:
                valor = "1";
                modalidad = "P";
                fuente = ConstantsFont.descriptionFont;
                backgroundColor = Constants.COLOR_RESULTADO_1_PASA;
                break;
            default:
                valor = "-";
                modalidad = "-";
                fuente = ConstantsFont.descriptionFont;
                backgroundColor = Constants.COLOR_RESULTADO_1_PASA;
                break;
        }

        final PdfPCell labelCell = PDFUtils.createTableCell(valor + " " + modalidad, backgroundColor, fuente, Element.ALIGN_CENTER, 0, -1);
        labelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return labelCell;
    }

    private static String getPriorityName(final MessageResources messageResources, final String name) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (name.equals(pmgr.getValue("intav.properties", "priority.1"))) {
            return messageResources.getMessage("observatorio.nivel.analisis.1");
        } else if (name.equals(pmgr.getValue("intav.properties", "priority.2"))) {
            return messageResources.getMessage("observatorio.nivel.analisis.2");
        } else {
            return null;
        }
    }

    private static PdfPTable createPaginaTableVerificationSummary(final MessageResources messageResources, final ObservatoryLevelForm observatoryLevelForm) {
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
                        fuente = ConstantsFont.warningFont;
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
                    Logger.putLog("Error al crear tabla, resultados primarios.", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
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

    private static PdfPTable createObservatoryVerificationScoreTable(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level, boolean basicService) {
        if (!basicService && previousScore != null) {
            return createObservatoryVerificationScoreTableWithEvolution(messageResources, actualScore, previousScore, level);
        } else {
            return createObservatoryVerificationScoreTableNoEvolution(messageResources, actualScore, level);
        }
    }

    private static PdfPTable createObservatoryVerificationScoreTableNoEvolution(final MessageResources messageResources, final ScoreForm actualScore, final String level) {
        final float[] columnsWidths = new float[]{0.65f, 0.35f};
        final PdfPTable table = new PdfPTable(columnsWidths);

        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.setHeaderRows(1);

        if (level.equals(Constants.OBS_PRIORITY_1)) {
            for (LabelValueBean actualLabelValueBean : actualScore.getVerifications1()) {
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            }
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.1"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel1().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        } else if (level.equals(Constants.OBS_PRIORITY_2)) {
            for (LabelValueBean actualLabelValueBean : actualScore.getVerifications2()) {
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            }
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.2"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel2().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        }

        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setSpacingAfter(0);
        return table;
    }

    private static PdfPTable createObservatoryVerificationScoreTableWithEvolution(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level) {
        final float[] columnsWidths = new float[]{0.55f, 0.25f, 0.20f};
        final PdfPTable table = new PdfPTable(columnsWidths);

        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.setHeaderRows(1);

        if (level.equals(Constants.OBS_PRIORITY_1)) {
            final ListIterator<LabelValueBean> actualScoreIterator = actualScore.getVerifications1().listIterator();
            final ListIterator<LabelValueBean> previousScoreIterator = previousScore.getVerifications1().listIterator();
            while (actualScoreIterator.hasNext()) {
                final LabelValueBean actualLabelValueBean = actualScoreIterator.next();
                final LabelValueBean previousLabelValueBean = previousScoreIterator.next();
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
                table.addCell(createEvolutionDifferenceCellValue(actualLabelValueBean.getValue(), previousLabelValueBean.getValue(), Color.WHITE));
            }
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.1"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel1().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            table.addCell(createEvolutionDifferenceCellValue(actualScore.getScoreLevel1(), previousScore.getScoreLevel1(), Color.WHITE));
        } else if (level.equals(Constants.OBS_PRIORITY_2)) {
            final ListIterator<LabelValueBean> actualScoreIterator = actualScore.getVerifications2().listIterator();
            final ListIterator<LabelValueBean> previousScoreIterator = previousScore.getVerifications2().listIterator();
            while (actualScoreIterator.hasNext()) {
                final LabelValueBean actualLabelValueBean = actualScoreIterator.next();
                final LabelValueBean previousLabelValueBean = previousScoreIterator.next();
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
                table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
                table.addCell(createEvolutionDifferenceCellValue(actualLabelValueBean.getValue(), previousLabelValueBean.getValue(), Color.WHITE));
            }
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.2"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel2().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
            table.addCell(createEvolutionDifferenceCellValue(actualScore.getScoreLevel2(), previousScore.getScoreLevel2(), Color.WHITE));
        }

        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setSpacingAfter(0);
        return table;
    }

    private static PdfPCell createEvolutionDifferenceCellValue(final String actualValue, final String previousValue, final Color color) {
        try {
            return createEvolutionDifferenceCellValue(new BigDecimal(actualValue), new BigDecimal(previousValue), color);
        } catch (NumberFormatException nfe) {
            return createEvolutionDifferenceCellValue(BigDecimal.ZERO, BigDecimal.ZERO, color);
        }
    }

    private static PdfPCell createEvolutionDifferenceCellValue(final BigDecimal actualValue, final BigDecimal previousValue, final Color color) {
        return PDFUtils.createTableCell(getEvolutionImage(actualValue, previousValue), new DecimalFormat("+0.00;-0.00").format(actualValue.subtract(previousValue)), color, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1);
    }

}
