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
import es.inteco.rastreador2.intav.utils.IntavUtils;
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
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
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
        } finally {
            DataBaseManager.closeConnection(c);
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
        final Document document = new Document(PageSize.A4, 50, 50, 120, 72);

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();

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
            countSections = pdfBuilder.createIntroductionChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, isBasicService);
            numChapter++;
            countSections = pdfBuilder.createObjetiveChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, observatoryType);
            numChapter++;
            countSections = pdfBuilder.createMethodologyChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, evaList, observatoryType, isBasicService);
            numChapter++;

            // Resumen de las puntuaciones del Observatorio
            final RankingInfo rankingInfo = crawling != null ? calculateRankings(c, idObservatoryExecution, crawling.getSeed()) : null;
            countSections = addObservatoryScoreSummary(pdfBuilder, messageResources, request, document, index, evaList, numChapter, countSections, file, rankingInfo);
            numChapter++;

            // Resumen de las puntuaciones del Observatorio
            countSections = addObservatoryResultsSummary(messageResources, document, index, evaList, numChapter, countSections);
            numChapter++;

            int counter = 1;
            for (ObservatoryEvaluationForm evaluationForm : evaList) {
                String evaluationTitle = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.label", counter);
                Chapter chapter = PDFUtils.addChapterTitle(evaluationTitle, index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);

                String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
                String url = evaluationForm.getUrl();

                final Paragraph p = new Paragraph();
                if (title != null && !(title.replace("...", "")).contains(url.replace("...", ""))) {
                    final Phrase p1 = PDFUtils.createPhrase(messageResources.getMessage("resultados.observatorio.vista.primaria.url") + ": ", ConstantsFont.scoreBoldFont);
                    final Phrase p2 = PDFUtils.createPhraseLink(url, url, ConstantsFont.scoreFont);
                    p.add(p1);
                    p.add(p2);
                }
                p.setSpacingBefore(ConstantsFont.SPACE_LINE);
                chapter.add(p);

                if (title != null) {
                    final Font titleFont = title.length() > 50 ? ConstantsFont.descriptionFont : ConstantsFont.scoreFont;
                    final Phrase p3 = PDFUtils.createPhrase(messageResources.getMessage("resultados.observatorio.vista.primaria.title") + ": ", ConstantsFont.scoreBoldFont);
                    final Phrase p4 = PDFUtils.createPhrase(title, titleFont);
                    Paragraph titleParagraph = new Paragraph();
                    titleParagraph.add(p3);
                    titleParagraph.add(p4);
                    chapter.add(titleParagraph);
                }

                final ArrayList<String> boldWords = new ArrayList<String>();
                boldWords.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.media.pagina") + ": ");
                chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0} " + evaluationForm.getScore(), boldWords, ConstantsFont.scoreBoldFont, ConstantsFont.scoreFont, true));

                boldWords.clear();
                boldWords.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.nivel.adecuacion") + ": ");
                chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0} " + ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), boldWords, ConstantsFont.scoreBoldFont, ConstantsFont.scoreFont, false));

                PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.pagina"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, false);

                for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                    Paragraph levelTitle = new Paragraph(getPriorityName(messageResources, observatoryLevelForm.getName()), ConstantsFont.levelTitleMPFont);
                    levelTitle.setSpacingBefore(10);
                    Section levelSection = chapter.addSection(levelTitle);
                    levelSection.setNumberDepth(0);
                    Paragraph levelScore = new Paragraph(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.media.nivel.analisis") + ": " + observatoryLevelForm.getScore(), ConstantsFont.levelScoreFont);
                    levelSection.add(levelScore);
                    chapter.add(createTable(observatoryLevelForm, messageResources));
                }

                // Lista de verificaciones que fallan
                final Map<String, List<String>> failedChecks = IntavUtils.getFailedChecks(request, evaluationForm, guideline);
                if (failedChecks.keySet().size() != 0) {
                    addFailedChecksSection(messageResources, failedChecks, chapter);
                }

                if (evaluationForm.getCrawlerExecutionId() < 0) {
                    addCheckCodes(messageResources, evaluationForm, chapter);
                } else {
                    SpecialChunk externalLink = new SpecialChunk("http://forja-ctt.administracionelectronica.gob.es/web/caccesibilidad", ConstantsFont.paragraphAnchorFont);
                    externalLink.setExternalLink(true);
                    externalLink.setAnchor("http://forja-ctt.administracionelectronica.gob.es/web/caccesibilidad");
                    Map<Integer, SpecialChunk> specialChunkMap = new HashMap<Integer, SpecialChunk>();
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
            if (document != null && document.isOpen()) {
                try {
                    document.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
            DataBaseManager.closeConnection(c);
        }
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
                Paragraph priorityTitle = new Paragraph(getPriorityName(messageResources, priority), ConstantsFont.levelTitleMPFont);
                priorityTitle.setSpacingBefore(10);
                Section prioritySection = chapter.addSection(priorityTitle);
                prioritySection.setNumberDepth(0);
                for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
                    if (hasProblems(level)) {
                        final Paragraph levelTitle = new Paragraph(getLevelName(messageResources, level), ConstantsFont.chapterTitleMPFont);
                        levelTitle.setSpacingBefore(10);
                        final Section levelSection = prioritySection.addSection(levelTitle);
                        levelSection.setNumberDepth(0);
                        for (ObservatorySubgroupForm verification : level.getSubgroups()) {
                            if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                                PDFUtils.addParagraph(messageResources.getMessage(verification.getDescription()), ConstantsFont.guidelineDescMPFont, levelSection);
                                for (ProblemForm problem : verification.getProblems()) {
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
                                    final Paragraph p = PDFUtils.createImageParagraphWithDiferentFont(image, description, font, StringUtils.removeHtmlTags(messageResources.getMessage(problem.getError())), ConstantsFont.strongDescriptionFont, Chunk.ALIGN_LEFT);
                                    levelSection.add(p);

                                    if (StringUtils.isNotEmpty(problem.getRationale()) && messageResources.isPresent(problem.getRationale())) {
                                        PDFUtils.addParagraphRationale(Arrays.asList(messageResources.getMessage(problem.getRationale()).split("<p>|</p>")), levelSection);
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
        final Chapter chapter = PDFUtils.addChapterTitle(messageResources.getMessage("observatorio.puntuacion.resultados.resumen").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);

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
        Chapter chapter = PDFUtils.addChapterTitle(messageResources.getMessage("resultados.primarios.res.verificacion").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);
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
        Map<String, List<LabelValueBean>> results = new TreeMap<String, List<LabelValueBean>>();

        int counter = 1;
        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
                for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
                    for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
                        if (!results.containsKey(subgroupForm.getDescription())) {
                            results.put(subgroupForm.getDescription(), new ArrayList<LabelValueBean>());
                        }
                        LabelValueBean lvb = new LabelValueBean();
                        lvb.setLabel(messageResources.getMessage("observatory.graphic.score.by.page.label", counter));
                        lvb.setValue(String.valueOf(subgroupForm.getValue()));
                        results.get(subgroupForm.getDescription()).add(lvb);
                    }
                }
            }
            counter++;
        }

        for (Map.Entry<String, List<LabelValueBean>> entry : results.entrySet()) {
            String id = messageResources.getMessage(entry.getKey()).substring(0, 5);
            String title = messageResources.getMessage(entry.getKey()).substring(6) + " (" + id + ")";
            Section section = PDFUtils.addSection(title, index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
            section.add(createVerificationTable(messageResources, entry.getValue()));
            chapter.newPage();
        }

        return countSections;
    }

    private static PdfPTable createVerificationTable(final MessageResources messageResources, final List<LabelValueBean> results) {
        float[] widths = {0.50f, 0.25f, 0.25f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.valor"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (LabelValueBean lvb : results) {
            int value = Integer.parseInt(lvb.getValue());
            table.addCell(PDFUtils.createTableCell(lvb.getLabel(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            if (value == Constants.OBS_VALUE_NOT_SCORE) {
                table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            } else if (value == Constants.OBS_VALUE_RED_ZERO || value == Constants.OBS_VALUE_GREEN_ZERO) {
                table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            } else {
                table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.valor.uno"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            }

            PdfPCell labelCell;
            if (value == Constants.OBS_VALUE_NOT_SCORE || value == Constants.OBS_VALUE_GREEN_ZERO || value == Constants.OBS_VALUE_GREEN_ONE) {
                labelCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"), Color.WHITE, ConstantsFont.descriptionFontGreen, Element.ALIGN_CENTER, 0);
            } else {
                labelCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla"), Color.WHITE, ConstantsFont.descriptionFontRed, Element.ALIGN_CENTER, 0);
            }
            labelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(labelCell);
        }

        return table;
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
                Image image;

                try {
                    // TODO: ¿Sirve de algo la imagen en este bloque?, no se usa después
                    if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.uno");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    } else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ZERO) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    } else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.red"));
                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla");
                    }
                    if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_NOT_SCORE) {
                        value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
                        image.scalePercent(72);
                        modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    }
                } catch (Exception e) {
                    Logger.putLog("Error al crear tabla, resultados primarios.", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
                }

                PdfPCell descriptionCell = new PdfPCell(new Paragraph(messageResources.getMessage(observatorySubgroupForm.getDescription()), ConstantsFont.descriptionFont));
                PdfPCell valueCell = PDFUtils.createTableCell(value, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0);
                PdfPCell labelCell;
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
