package es.inteco.rastreador2.pdf.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Guideline;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.*;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.ExportAction;
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
import java.sql.Connection;
import java.util.*;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class PrimaryExportPdfUtils {

    private PrimaryExportPdfUtils() {
    }

    public static void exportToPdf(final Long idExecution, final List<Long> evaluationIds, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
                                   final long idObservatoryExecution, final long observatoryType) throws Exception {

        AnonymousResultExportPdf builder = null;
        try(Connection c = DataBaseManager.getConnection()) {
            final FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(c, idExecution);
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

        exportToPdf(builder, idExecution, evaluationIds, request, generalExpPath, seed, content, idObservatoryExecution, observatoryType);
    }

    public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idExecution, final List<Long> evaluationIds, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
                                   final long idObservatoryExecution, final long observatoryType) throws Exception {
        final MessageResources messageResources = CrawlerUtils.getResources(request);
        final File file = new File(generalExpPath);
        file.setReadable(true, false);
        file.setWritable(true, false);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            file.getParentFile().setReadable(true, false);
            file.getParentFile().setWritable(true, false);
            Logger.putLog("Exception: No se ha podido crear los directorios al exportar a PDF", ExportAction.class, Logger.LOG_LEVEL_ERROR);
        }
        Logger.putLog("Exportando a PDF PrimaryExportPdfUtils.exportToPdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_DEBUG);
        final FileOutputStream fileOut = new FileOutputStream(file);
        // TODO: Add document metadata (author, creator, subject, title...)
        final Document document = new Document(PageSize.A4, 50, 50, 120, 72);


        try (Connection c= DataBaseManager.getConnection()) {
            // Inicializamos el evaluador si hace falta
            if (!EvaluatorUtility.isInitialized()) {
                try {
                    EvaluatorUtility.initialize();
                } catch (Exception e) {
                    Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }

            final List<ObservatoryEvaluationForm> evaList = new ArrayList<ObservatoryEvaluationForm>();
            Guideline guideline = null;
            for (Long id : evaluationIds) {
                final Evaluator evaluator = new Evaluator();
                final Evaluation evaluation = evaluator.getAnalisisDB(c, id, EvaluatorUtils.getDocList(), false);
                final String methodology = ObservatorioDAO.getMethodology(c, idObservatoryExecution);
                final ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, true);
                evaList.add(evaluationForm);

                if (guideline == null) {
                    guideline = EvaluatorUtility.loadGuideline(evaluation.getGuidelines().get(0));
                }
            }

            final FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(c, idExecution);

            final PdfWriter writer = PdfWriter.getInstance(document, fileOut);
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));

            final String dateStr = crawling != null ? crawling.getDate() : CrawlerUtils.formatDate(new Date());


            final boolean isBasicService = idExecution < 0;
            pdfBuilder.setBasicService(isBasicService);
            final String footerText = messageResources.getMessage("ob.resAnon.intav.report.foot", seed, dateStr);
            writer.setPageEvent(new ExportPageEventsObservatoryMP(footerText, dateStr, isBasicService));
            ExportPageEventsObservatoryMP.setLastPage(false);

            IndexEvents index = new IndexEvents();
            writer.setPageEvent(index);
            writer.setLinearPageMode();

            document.open();

            PDFUtils.addTitlePage(document, messageResources.getMessage("pdf.accessibility.title") + seed.toUpperCase(), pdfBuilder.getTitle(), ConstantsFont.documentTitleMPFont);

            int numChapter = 1;
            int countSections = 1;
            if (!isBasicService) {
                countSections = pdfBuilder.createIntroductionChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont);
                numChapter++;
            }
            countSections = pdfBuilder.createObjetiveChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, evaList, observatoryType);
            numChapter++;

            if (!isBasicService) {
                // Resumen de las puntuaciones del Observatorio
                final RankingInfo rankingInfo = crawling != null ? calculateRankings(c, idObservatoryExecution, crawling.getSeed()) : null;
                countSections = addObservatoryScoreSummary(pdfBuilder, messageResources, request, document, index, evaList, numChapter, countSections, file, rankingInfo);
                numChapter++;
            }

            // Resumen de las puntuaciones del Observatorio
            countSections = addObservatoryResultsSummary(messageResources, document, index, evaList, numChapter, countSections);
            numChapter++;

            int counter = 1;
            for (ObservatoryEvaluationForm evaluationForm : evaList) {
                final String evaluationTitle = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.label", counter);
                final Chapter chapter = PDFUtils.addChapterWithTitle(evaluationTitle, index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont, true, "anchor_resultados_page_" + counter);

                final String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
                final String url = evaluationForm.getUrl();
//                if (title != null) {
//                    final Font titleFont = title.length() > 70 ? ConstantsFont.descriptionFont : ConstantsFont.scoreFont;
//                    final Phrase p3 = PDFUtils.createPhrase(messageResources.getMessage("resultados.observatorio.vista.primaria.title") + ": ", ConstantsFont.scoreBoldFont);
//                    final Phrase p4 = PDFUtils.createPhrase(title, titleFont);
//                    final Paragraph titleParagraph = new Paragraph();
//                    titleParagraph.add(p3);
//                    titleParagraph.add(p4);
//                    titleParagraph.setSpacingBefore(ConstantsFont.SPACE_LINE);
//                    chapter.add(titleParagraph);
//                }
//
//                final Paragraph p = new Paragraph();
//                if (title != null && !(title.replace("...", "")).contains(url.replace("...", ""))) {
//                    final Phrase p1 = PDFUtils.createPhrase(messageResources.getMessage("resultados.observatorio.vista.primaria.url") + ": ", ConstantsFont.scoreBoldFont);
//                    final Phrase p2 = PDFUtils.createPhraseLink(url, url, ConstantsFont.descriptionFont);
//                    p.add(p1);
//                    p.add(p2);
//                }
//                chapter.add(p);
//
//                final ArrayList<String> boldWords = new ArrayList<>();
//                boldWords.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.media.pagina") + ": ");
//                chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0} " + evaluationForm.getScore(), boldWords, ConstantsFont.scoreBoldFont, ConstantsFont.scoreFont, true));
//
//                boldWords.clear();
//                boldWords.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.nivel.adecuacion") + ": ");
//                chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0} " + ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), boldWords, ConstantsFont.scoreBoldFont, ConstantsFont.scoreFont, false));

//                PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.pagina"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, false);

                final BigDecimal pmp = evaluationForm.getScore();
                final String validationLevel = ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm));
                final List pmpna = new ArrayList();
                for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                    pmpna.add(observatoryLevelForm.getScore());
                }
                
                chapter.add(createPaginaTableInfo(messageResources, title, url, pmp, validationLevel, pmpna));

                // Creación de las tablas de cada página
                for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                    final Paragraph levelTitle = new Paragraph(getPriorityName(messageResources, observatoryLevelForm.getName()), ConstantsFont.chapterTitleMPFont3L);
                    levelTitle.setSpacingBefore(10);
                    chapter.add(levelTitle);
                    //final Paragraph levelScore = new Paragraph(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.media.nivel.analisis") + ": " + observatoryLevelForm.getScore(), ConstantsFont.levelScoreFont);
                    //chapter.add(levelScore);
                    if (BigDecimal.TEN.compareTo(observatoryLevelForm.getScore()) != 0) {
                        chapter.add(createTable(observatoryLevelForm, messageResources));
                    } else {
                        PDFUtils.addParagraph("No se han detectado incidencias para este nivel", ConstantsFont.levelScoreFont, chapter, Paragraph.ALIGN_CENTER, false, false);
                    }
                }

                // Lista de verificaciones que fallan
//                final Map<String, List<String>> failedChecks = IntavUtils.getFailedChecks(request, evaluationForm, guideline);
//                if (failedChecks.keySet().size() != 0) {
//                    addFailedChecksSection(messageResources, failedChecks, chapter);
//                }

                if (isBasicService) {
                    addCheckCodes(messageResources, evaluationForm, chapter);
                } else {
                    final SpecialChunk externalLink = new SpecialChunk("http://forja-ctt.administracionelectronica.gob.es/web/caccesibilidad", ConstantsFont.paragraphAnchorFont);
                    externalLink.setExternalLink(true);
                    externalLink.setAnchor("http://forja-ctt.administracionelectronica.gob.es/web/caccesibilidad");
                    final Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
                    specialChunkMap.put(1, externalLink);
                    chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.errores.mas.info"), specialChunkMap, ConstantsFont.paragraphFont));
                }

                document.add(chapter);
                numChapter++;
                counter++;
            }

            if (!StringUtils.isEmpty(content)) {
                // Añadir el código fuente analizado
                pdfBuilder.createContentChapter(request, document, content, index, numChapter, countSections);
            }

            countSections = pdfBuilder.createMethodologyChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, evaList, observatoryType, isBasicService);
            numChapter++;

            //Ponemos la variable a true para que no se escriba el footer en el índice
            IndexUtils.createIndex(writer, document, request, index, false, ConstantsFont.chapterTitleMPFont);
            ExportPageEventsObservatoryMP.setLastPage(false);
        } catch (DocumentException e) {
            Logger.putLog("Error al exportar a pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } catch (Exception e) {
            Logger.putLog("Excepción genérica al generar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            if (document.isOpen()) {
                try {
                    document.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }

    private static PdfPTable createPaginaTableInfo(final MessageResources messageResources, final String title, final String url, final BigDecimal pmp, final String na, final List<BigDecimal> pmpnas) {
        float[] widths = {0.25f, 0.75f};
        final PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(0);

        // Titulo
        table.addCell(PDFUtils.createTableCell(messageResources, "resultados.observatorio.vista.primaria.title", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, 0, -1));
        table.addCell(PDFUtils.createTableCell(title, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, 5, -1));
        // URL
        table.addCell(PDFUtils.createTableCell(messageResources, "resultados.observatorio.vista.primaria.url", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, 0, -1));
        final PdfPCell urlCell = new PdfPCell(new Phrase(new Chunk(url, ConstantsFont.descriptionFont).setAnchor(url)));
        urlCell.setBackgroundColor(Color.WHITE);
        urlCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        urlCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        urlCell.setPaddingLeft(5);
        table.addCell(urlCell);
        // Puntuación Media Página
//        table.addCell(PDFUtils.createTableCell(messageResources, "observatorio.puntuacion.media.pagina", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, 0, -1));
        table.addCell(PDFUtils.createTableCell("Puntuación Media", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, 0, -1));
        table.addCell(PDFUtils.createTableCell(pmp.toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, 5, -1));
        // Nivel de Adecuación (a.k.a. Modalidad)
        table.addCell(PDFUtils.createTableCell(messageResources, "observatorio.nivel.adecuacion", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, 0, -1));
        table.addCell(PDFUtils.createTableCell(na, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, 5, -1));
        int contador = 1;
        for(BigDecimal pmpna: pmpnas) {
            // Puntuación Media Página Nivel Accesibilidad
            table.addCell(PDFUtils.createTableCell("PM Nivel Accesibilidad " +  contador++, Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, 0, -1));
            table.addCell(PDFUtils.createTableCell(pmpna.toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, 5, -1));
        }
        return table;
    }

    private static void addCheckCodes(final MessageResources messageResources, final ObservatoryEvaluationForm evaluationForm, final Chapter chapter) throws BadElementException, IOException {
        final PropertiesManager pmgr = new PropertiesManager();

        final Image imgProblemA = Image.getInstance(pmgr.getValue("pdf.properties", "path.problem"));
        final Image imgWarnings = Image.getInstance(pmgr.getValue("pdf.properties", "path.warnings"));
        final Image imgInfos = Image.getInstance(pmgr.getValue("pdf.properties", "path.infos"));

        imgProblemA.setAlt("");
        imgWarnings.setAlt("");
        imgInfos.setAlt("");

        imgProblemA.scalePercent(60);
        imgWarnings.scalePercent(60);
        imgInfos.scalePercent(60);

        for (ObservatoryLevelForm priority : evaluationForm.getGroups()) {
            if (hasProblems(priority)) {
//                final Paragraph priorityTitle = new Paragraph(getPriorityName(messageResources, priority), ConstantsFont.levelTitleMPFont);
//                priorityTitle.setSpacingBefore(10);
//                final Section prioritySection = chapter.addSection(priorityTitle);
                final Section prioritySection = PDFUtils.addSection(getPriorityName(messageResources, priority), null, ConstantsFont.chapterTitleMPFont2L, chapter, 1, 1);
                prioritySection.setNumberDepth(0);
                for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
                    if (hasProblems(level)) {
                        final Paragraph levelTitle = new Paragraph(getLevelName(messageResources, level), ConstantsFont.chapterTitleMPFont3L);
                        levelTitle.setSpacingBefore(10);
                        final Section levelSection = prioritySection.addSection(levelTitle);
                        levelSection.setNumberDepth(0);
                        for (ObservatorySubgroupForm verification : level.getSubgroups()) {
                            if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
//                                final Paragraph tituloVerificacion = new Paragraph(messageResources.getMessage(verification.getDescription()), ConstantsFont.summaryScoreFont);
//                                tituloVerificacion.setSpacingAfter(0);
//                                tituloVerificacion.setSpacingBefore(10);
//                                tituloVerificacion.setAlignment(Paragraph.ALIGN_JUSTIFIED);
//                                levelSection.add(tituloVerificacion);
                                for (ProblemForm problem : verification.getProblems()) {
                                    final PdfPTable prueba = new PdfPTable(new float[] {0.15f, 0.85f});
                                    prueba.setSpacingBefore(10);
                                    prueba.setWidthPercentage(100);
                                    PdfPCell tituloPrueba = PDFUtils.createTableCell(messageResources.getMessage(verification.getDescription()),Color.WHITE, ConstantsFont.summaryScoreFont, Element.ALIGN_LEFT, 5, -1);
                                    tituloPrueba.setColspan(2);
                                    prueba.addCell(tituloPrueba);
                                    levelSection.add(prueba);

                                    String description = "";
                                    Image image = null;
                                    com.lowagie.text.Font font = null;
                                    if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
                                        description += messageResources.getMessage("pdf.accessibility.bs.warning") + ": ";
                                        image = imgWarnings;
                                        font = ConstantsFont.warningFont;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
                                        description += messageResources.getMessage("pdf.accessibility.bs.problem") + ": ";
                                        image = imgProblemA;
                                        font = ConstantsFont.problemFont;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
                                        description += messageResources.getMessage("pdf.accessibility.bs.info") + ": ";
                                        image = imgInfos;
                                        font = ConstantsFont.cannottellFont;
                                    }
                                    PdfPCell tipoPrueba = PDFUtils.createTableCell(description,Color.WHITE, font, Element.ALIGN_LEFT, 5, -1);
                                    prueba.addCell(tipoPrueba);
                                    PdfPCell comprobacionPrueba = PDFUtils.createTableCell(StringUtils.removeHtmlTags(messageResources.getMessage(problem.getError())),Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, 5, -1);
                                    prueba.addCell(comprobacionPrueba);
//                                    final Paragraph p = PDFUtils.createImageParagraphWithDiferentFont(image, description, font, StringUtils.removeHtmlTags(messageResources.getMessage(problem.getError())), ConstantsFont.descriptionFont, Chunk.ALIGN_LEFT);
//                                    p.setSpacingBefore(5);
//                                    p.setSpacingAfter(10);
//                                    p.setKeepTogether(true);
//                                    levelSection.add(p);

                                    if (StringUtils.isNotEmpty(problem.getRationale()) && messageResources.isPresent(problem.getRationale())) {
                                        //PDFUtils.addParagraphRationale(Arrays.asList(messageResources.getMessage(problem.getRationale()).split("<p>|</p>")), levelSection);
                                        final Paragraph paragraph = new Paragraph();
                                        boolean isFirst = true;
                                        for (String phraseText : Arrays.asList(messageResources.getMessage(problem.getRationale()).split("<p>|</p>"))) {
                                            if (isFirst) {
                                                if (StringUtils.isNotEmpty(phraseText)) {
                                                    paragraph.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.moreInfoFont));
                                                }
                                                isFirst = false;
                                            } else {
                                                paragraph.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.moreInfoFont));
                                            }
                                        }

                                        PdfPCell labelCell = new PdfPCell(paragraph);
                                        labelCell.setBackgroundColor(new Color(245, 245, 245));
                                        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        labelCell.setPadding(5f);
                                        labelCell.setColspan(2);
                                        prueba.addCell(labelCell);
                                        //table.setSpacingBefore(ConstantsFont.SPACE_LINE / 2);
                                    }
                                    BasicServiceExport.addSpecificProblems(messageResources, levelSection, problem.getSpecificProblems());


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

    private static void addFailedChecksSection(final MessageResources messageResources, final Map<String, List<String>> failedChecks, final Chapter chapter) {
        final com.lowagie.text.List list = new com.lowagie.text.List();
        for (Map.Entry<String, List<String>> entry : failedChecks.entrySet()) {
            PDFUtils.addListItem(entry.getKey(), list, ConstantsFont.paragraphBoldFont, false, false);
            final com.lowagie.text.List sublist = new com.lowagie.text.List();
            for (String check : entry.getValue()) {
                PDFUtils.addListItem(StringUtils.removeHtmlTags(check), sublist, ConstantsFont.paragraphFont, false, true);
            }
            sublist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
            list.add(sublist);
        }
        final Paragraph verificationsTitle = new Paragraph(messageResources.getMessage("resultados.observatorio.vista.primaria.errores.por.verificacion"), ConstantsFont.levelTitleMPFont);
        verificationsTitle.setSpacingAfter(ConstantsFont.SPACE_LINE);
        final Section verificationsSection = chapter.addSection(verificationsTitle);
        verificationsSection.setNumberDepth(0);
        verificationsSection.add(list);
    }

    private static int addObservatoryScoreSummary(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final HttpServletRequest request, Document document, IndexEvents index, final List<ObservatoryEvaluationForm> evaList, int numChapter, int countSections, final File file, final RankingInfo rankingInfo) throws Exception {
        final Chapter chapter = PDFUtils.addChapterWithTitle(messageResources.getMessage("observatorio.puntuacion.resultados.resumen").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);

        final ArrayList<String> boldWord = new ArrayList<String>();
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p1"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWord.clear();
        boldWord.add(messageResources.getMessage("resultados.primarios.4.p2.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p2"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWord.clear();
        boldWord.add(messageResources.getMessage("resultados.primarios.4.p3.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p3"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        final ScoreForm scoreForm = pdfBuilder.generateScores(messageResources, evaList);

        boldWord.clear();
        boldWord.add(messageResources.getMessage("observatorio.nivel.adecuacion") + ": ");
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + scoreForm.getLevel(), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, true));

        boldWord.clear();
        boldWord.add(messageResources.getMessage("observatorio.puntuacion.total") + ": ");
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + scoreForm.getTotalScore(), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, false));
        if (rankingInfo != null && rankingInfo.getGlobalSeedsNumber() > 1) {
            boldWord.clear();
            boldWord.add(messageResources.getMessage("observatorio.posicion.global") + ": ");
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + rankingInfo.getGlobalRank() + " " + messageResources.getMessage("de.text", rankingInfo.getGlobalSeedsNumber()), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, false));

            boldWord.clear();
            boldWord.add(messageResources.getMessage("observatorio.posicion.segmento", rankingInfo.getCategoria().getName()) + ": ");
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + rankingInfo.getCategoryRank() + " " + messageResources.getMessage("de.text", rankingInfo.getCategorySeedsNumber()), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, false));

            chapter.add(Chunk.NEWLINE);
        }

        final String noDataMess = messageResources.getMessage("grafica.sin.datos");
        addLevelAllocationResultsSummary(messageResources, chapter, file, evaList, noDataMess);

        Section section = PDFUtils.addSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion1"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.41.p1"), ConstantsFont.paragraphFont, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, request, section, file, evaList, noDataMess, Constants.OBS_PRIORITY_1);
        section.add(createGlobalTable(messageResources, scoreForm, Constants.OBS_PRIORITY_1));

        section = PDFUtils.addSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion2"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.42.p1"), ConstantsFont.paragraphFont, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, request, section, file, evaList, noDataMess, Constants.OBS_PRIORITY_2);
        section.add(createGlobalTable(messageResources, scoreForm, Constants.OBS_PRIORITY_2));

        PDFUtils.addSection(messageResources.getMessage("resultados.primarios.puntuacion.pagina"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        addResultsByPage(messageResources, chapter, file, evaList, noDataMess);

        document.add(chapter);
        return countSections;
    }

    private static int addObservatoryResultsSummary(final MessageResources messageResources, Document document, IndexEvents index, List<ObservatoryEvaluationForm> evaList, int numChapter, int countSections) throws Exception {
        Chapter chapter = PDFUtils.addChapterWithTitle(messageResources.getMessage("resultados.primarios.res.verificacion").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p1"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p2"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, false);
        countSections = addResultsByVerification(messageResources, chapter, evaList, countSections, index);
        document.add(chapter);

        return countSections;
    }

    private static void addLevelAllocationResultsSummary(final MessageResources messageResources, final Section section,
                                                         final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess) throws Exception {
        final Map<String, Integer> result = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(evaList);
        final List<GraphicData> labelValueBeanList = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, result);

        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test.jpg";
        final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
        ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, evaList, title, filePath, noDataMess);
        final Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        section.add(image);

        final float[] widths = {33f, 33f, 33f};
        final PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.num.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        for (GraphicData label : labelValueBeanList) {
            table.addCell(PDFUtils.createTableCell(label.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section.add(table);
    }

    private static void addMidsComparationByVerificationLevelGraphic(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final HttpServletRequest request, final Section section, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess, final String level) throws Exception {
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
        pdfBuilder.getMidsComparationByVerificationLevelGraphic(request, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), true);
        final Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        section.add(image);
    }

    private static void addResultsByPage(final MessageResources messageResources, final Chapter chapter, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess) throws Exception {
        final Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        final SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("resultados.primarios.43.p1.anchor"), messageResources.getMessage("anchor.PMP"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.43.p1"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.43.p2"), ConstantsFont.paragraphFont, chapter);

        chapter.add(Chunk.NEWLINE);

        final String title = messageResources.getMessage("observatory.graphic.score.by.page.title");
        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test4.jpg";
        ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(messageResources, evaList, title, filePath, noDataMess);

        final Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(80);
        image.setAlignment(Element.ALIGN_CENTER);
        chapter.add(image);

        float[] widths = {33f, 33f, 33f};
        final PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        int pageCounter = 0;
        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", ++pageCounter), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(String.valueOf(evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        chapter.add(table);
    }

    private static int addResultsByVerification(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, int countSections, final IndexEvents index) {
        int counter = 1;
        ObservatoryLevelForm observatoryLevelForm = evaList.get(0).getGroups().get(0);
        final Section section = PDFUtils.addSection("Tabla resumen de resultados " + getPriorityName(messageResources, observatoryLevelForm.getName()), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        final PdfPTable table = createTableHeader(messageResources, observatoryLevelForm.getSuitabilityGroups());

        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(counter), 2, ' ')), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + counter));
            for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(0).getSuitabilityGroups()){
                for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                    table.addCell(createValorModalidadCell(subgroupForm.getValue()));
                }
            }
            table.addCell(PDFUtils.createTableCell(evaluationForm.getGroups().get(0).getScore().toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_RIGHT,0));
            counter++;
        }
        section.add(table);
        section.add(buildResumenResultadosLeyendaTable(messageResources, observatoryLevelForm));

        if (evaList.size()>3) {
            chapter.newPage();
        }
        counter = 1;
        observatoryLevelForm = evaList.get(0).getGroups().get(1);
        final Section section2 = PDFUtils.addSection("Tabla resumen de resultados " + getPriorityName(messageResources, observatoryLevelForm.getName()), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        final PdfPTable table2 = createTableHeader(messageResources, observatoryLevelForm.getSuitabilityGroups());

        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table2.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(counter), 2, ' ')), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + counter));
            for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(1).getSuitabilityGroups()){
                for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                    table2.addCell(createValorModalidadCell(subgroupForm.getValue()));
                }
            }
            table2.addCell(PDFUtils.createTableCell(evaluationForm.getGroups().get(1).getScore().toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_RIGHT,0));
            counter++;
        }
        section2.add(table2);
        section2.add(buildResumenResultadosLeyendaTable(messageResources, observatoryLevelForm));

        return countSections;
    }

    private static PdfPTable buildResumenResultadosLeyendaTable(final MessageResources messageResources, final ObservatoryLevelForm evaList) {
        final PdfPTable table = new PdfPTable(new float[] {0.40f, 0.60f});
        table.setWidthPercentage(65);
        table.setKeepTogether(true);
        final com.lowagie.text.List leyendaValoresResultados = new com.lowagie.text.List(false, false);

        ListItem aux = new ListItem("Valor No Puntua", ConstantsFont.moreInfoFont);
        aux.setListSymbol(new Chunk("-: "));
        aux.setIndentationLeft(0, true);
        leyendaValoresResultados.add(aux);

        aux = new ListItem("Valor 1", ConstantsFont.moreInfoFont);
        aux.setListSymbol(new Chunk("1: "));
        aux.setIndentationLeft(0, true);
        leyendaValoresResultados.add(aux);

        aux = new ListItem("Valor 0", ConstantsFont.moreInfoFont);
        aux.setListSymbol(new Chunk("0: "));
        aux.setIndentationLeft(0, true);
        leyendaValoresResultados.add(aux);

        aux = new ListItem("Modalidad PASA", ConstantsFont.moreInfoFont);
        aux.setListSymbol(new Chunk("P: "));
        aux.setIndentationLeft(0, true);
        leyendaValoresResultados.add(aux);

        aux = new ListItem("Modalidad FALLA", ConstantsFont.moreInfoFont);
        aux.setListSymbol(new Chunk("F: "));
        aux.setIndentationLeft(0, true);
        leyendaValoresResultados.add(aux);

        final PdfPCell leyendaValoresResultadosTableCell = PDFUtils.createListTableCell(leyendaValoresResultados, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
        leyendaValoresResultadosTableCell.setBorder(0);
        table.addCell(leyendaValoresResultadosTableCell);
        final com.lowagie.text.List leyenda2 = new com.lowagie.text.List(false, false);
        for (ObservatorySuitabilityForm suitabilityForm : evaList.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                final String checkId = messageResources.getMessage(subgroupForm.getDescription()).substring(0, 6);
                final String checkDescription = messageResources.getMessage(subgroupForm.getDescription()).substring(6);
                final ListItem listItem = new ListItem(checkDescription, ConstantsFont.moreInfoFont);
                listItem.setListSymbol(new Chunk(checkId));
                leyenda2.add(listItem);
            }
            final PdfPCell listTableCell = PDFUtils.createListTableCell(leyenda2, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
            listTableCell.setBorder(0);
            table.addCell(listTableCell);
        }
        return table;
    }

    private static PdfPTable createTableHeader(final MessageResources messageResources, final List<ObservatorySuitabilityForm> suitabilityGroups) {
        final float[] widths = {0.34f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f};
        final PdfPTable table = new PdfPTable(widths);
        table.setKeepTogether(true);
        table.setWidthPercentage(95);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (ObservatorySuitabilityForm suitabilityForm : suitabilityGroups) {
            for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                table.addCell(PDFUtils.createTableCell(messageResources.getMessage(subgroupForm.getDescription()).substring(0, 6), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            }
        }
        table.addCell(PDFUtils.createTableCell("PM", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        return table;
    }

    private static PdfPCell createValorModalidadCell(int value) {
        final String valor;
        if (value == Constants.OBS_VALUE_NOT_SCORE) {
            valor = "-";
        } else if (value == Constants.OBS_VALUE_RED_ZERO || value == Constants.OBS_VALUE_GREEN_ZERO) {
            valor = "0";
        } else {
            valor = "1";
        }

        final String modalidad;
        final Font fuente;
        if (value == Constants.OBS_VALUE_NOT_SCORE || value == Constants.OBS_VALUE_GREEN_ZERO || value == Constants.OBS_VALUE_GREEN_ONE) {
            modalidad = "P";
            fuente = ConstantsFont.descriptionFont;
        } else {
            modalidad = "F";
            fuente = ConstantsFont.strongDescriptionFont;
        }
        final Color backgroundColor;
        if (value ==  Constants.OBS_VALUE_RED_ZERO) {
            backgroundColor = Constants.COLOR_RESULTADO_0_FALLA;
        } else if ( value == Constants.OBS_VALUE_GREEN_ZERO) {
            backgroundColor = Constants.COLOR_RESULTADO_0_PASA;
        } else {
            backgroundColor = Constants.COLOR_RESULTADO_1_PASA;
        }
        final PdfPCell labelCell = PDFUtils.createTableCell(valor +" "+ modalidad, backgroundColor, fuente, Element.ALIGN_CENTER, 0);
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

    private static PdfPTable createTable(final ObservatoryLevelForm observatoryLevelForm, final MessageResources messageResources) {
        PropertiesManager pmgr = new PropertiesManager();

        float[] widths = {0.60f, 0.20f, 0.20f};
        PdfPTable table = new PdfPTable(widths);

        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.addCell(PDFUtils.createTableCell(messageResources, "resultados.observatorio.vista.primaria.verificacion", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources, "resultados.observatorio.vista.primaria.valor", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources, "resultados.observatorio.vista.primaria.modalidad", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
                String value = null;
                String modality = null;
//                Image image;
                final int observatorySubgroupFormValue = observatorySubgroupForm.getValue();
                try {
                    // TODO: ¿Sirve de algo la imagen en este bloque?, no se usa después
                    if (observatorySubgroupFormValue == Constants.OBS_VALUE_GREEN_ONE) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.uno");
//                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
//                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    } else if (observatorySubgroupFormValue == Constants.OBS_VALUE_GREEN_ZERO) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
//                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
//                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    } else if (observatorySubgroupFormValue == Constants.OBS_VALUE_RED_ZERO) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
//                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.red"));
//                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla");
                    } else if (observatorySubgroupFormValue == Constants.OBS_VALUE_NOT_SCORE) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua");
//                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
//                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    }
                } catch (Exception e) {
                    Logger.putLog("Error al crear tabla, resultados primarios.", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
                }

                if (observatorySubgroupFormValue != Constants.OBS_VALUE_GREEN_ONE && observatorySubgroupFormValue != Constants.OBS_VALUE_NOT_SCORE) {
                    final PdfPCell descriptionCell = new PdfPCell(new Paragraph(messageResources.getMessage(observatorySubgroupForm.getDescription()), ConstantsFont.descriptionFont));
                    final PdfPCell valueCell = PDFUtils.createTableCell(value, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0);
                    final PdfPCell labelCell;
                    if (modality != null && modality.equals(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"))) {
                        labelCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"), Color.WHITE, ConstantsFont.descriptionFontGreen, Element.ALIGN_CENTER, 0);
                    } else {
                        labelCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla"), Color.WHITE, ConstantsFont.descriptionFontRed, Element.ALIGN_CENTER, 0);
                    }
                    labelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table.addCell(descriptionCell);
                    table.addCell(valueCell);
                    table.addCell(labelCell);
                }
            }
        }

        return table;
    }

    private static PdfPTable createGlobalTable(final MessageResources messageResources, final ScoreForm scoreForm, final String level) {
        float[] widths = {0.7f, 0.3f};
        PdfPTable table = new PdfPTable(widths);
        int margin = 10;

        table.addCell(PDFUtils.createTableCell(messageResources, "resultados.observatorio.vista.primaria.verificacion", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources, "resultados.observatorio.vista.primaria.puntuacion.media", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        if (level.equals(Constants.OBS_PRIORITY_1)) {
            for (LabelValueBean label : scoreForm.getVerifications1()) {
                table.addCell(PDFUtils.createTableCell(label.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
                table.addCell(PDFUtils.createTableCell(label.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            }
            table.addCell(PDFUtils.createTableCell(messageResources, "observatorio.puntuacion.nivel.1", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(scoreForm.getScoreLevel1().toString(), Constants.NARANJA_MP, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        } else if (level.equals(Constants.OBS_PRIORITY_2)) {
            for (LabelValueBean label : scoreForm.getVerifications2()) {
                table.addCell(PDFUtils.createTableCell(label.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
                table.addCell(PDFUtils.createTableCell(label.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            }
            table.addCell(PDFUtils.createTableCell(messageResources, "observatorio.puntuacion.nivel.2", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(scoreForm.getScoreLevel2().toString(), Constants.NARANJA_MP, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.setSpacingAfter(2 * ConstantsFont.SPACE_LINE);
        return table;
    }

    private static RankingInfo calculateRankings(final Connection c, final Long idObservatoryExecution, final SemillaForm currentSeed) throws Exception {
        List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, new SemillaForm(), idObservatoryExecution, (long) 0, Constants.NO_PAGINACION);
        final RankingInfo rankingInfo = new RankingInfo();
        rankingInfo.setGlobalSeedsNumber(seedsResults.size());
        rankingInfo.setCategorySeedsNumber(0);
        rankingInfo.setGlobalRank(1);
        rankingInfo.setCategoryRank(1);
        rankingInfo.setCategoria(currentSeed.getCategoria());

        try {
            seedsResults = ObservatoryUtils.setAvgScore(c, seedsResults, idObservatoryExecution);
            BigDecimal currentSeedScore = BigDecimal.ZERO;
            // Buscamos la puntuación concreta de la semilla
            for (ResultadoSemillaForm seedForm : seedsResults) {
                if (Long.parseLong(seedForm.getId()) == currentSeed.getId()) {
                    currentSeedScore = new BigDecimal(seedForm.getScore());
                }
            }

            // Miramos el ranking comparando con el resto de semillas
            for (ResultadoSemillaForm seedForm : seedsResults) {
                if (seedForm.getScore() != null) {
                    final BigDecimal seedFormScore = new BigDecimal(seedForm.getScore());
                    if (seedFormScore.compareTo(currentSeedScore) == 1) {
                        rankingInfo.incrementGlobalRank();
                    }
                    if (currentSeed.getCategoria().getId().equals(String.valueOf(seedForm.getIdCategory()))) {
                        rankingInfo.setCategorySeedsNumber(rankingInfo.getCategorySeedsNumber() + 1);
                        if (seedFormScore.compareTo(currentSeedScore) == 1) {
                            rankingInfo.incrementCategoryRank();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return rankingInfo;
    }

}
