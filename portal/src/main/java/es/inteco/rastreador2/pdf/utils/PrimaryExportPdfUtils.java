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

    public static void exportToPdf(Long idExecution, List<Long> evaluationIds, HttpServletRequest request, String generalExpPath, String seed, String content,
                                   long idObservatoryExecution, long observatoryType) throws Exception {

        AnonymousResultExportPdf builder = null;
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(c, idExecution);
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
                                    long idObservatoryExecution, long observatoryType) throws Exception {
        File file = new File(generalExpPath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Logger.putLog("Exception: No se ha podido crear los directorios al exportar a PDF", ExportAction.class, Logger.LOG_LEVEL_ERROR);
        }
        Logger.putLog("Exportando a PDF PrimaryExportPdfUtils.exportToPdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_DEBUG);
        FileOutputStream fileOut = new FileOutputStream(file);
        Document document = new Document(PageSize.A4, 50, 50, 120, 72);

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

            List<ObservatoryEvaluationForm> evaList = new ArrayList<ObservatoryEvaluationForm>();
            Guideline guideline = null;
            for (Long id : evaluationIds) {
                Evaluator evaluator = new Evaluator();
                Evaluation evaluation = evaluator.getAnalisisDB(c, id, EvaluatorUtils.getDocList(), false);
                String methodology = ObservatorioDAO.getMethodology(c, idObservatoryExecution);
                ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, true);
                evaList.add(evaluationForm);

                if (guideline == null) {
                    guideline = EvaluatorUtility.loadGuideline(evaluation.getGuidelines().get(0));
                }
            }
            
    		/*if(pmgr.getValue(CRAWLER_PROPERTIES, "debug.checks").equals(Boolean.TRUE.toString())) {
                ResultadosAnonimosObservatorioIntavUtils.debugChecks(request, evaList);
    		}*/

            FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(c, idExecution);

            PdfWriter writer = PdfWriter.getInstance(document, fileOut);
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));

            String dateStr = crawling != null ? crawling.getDate() : CrawlerUtils.formatDate(new Date());

            final boolean isBasicService = idExecution < 0;
            String footerText = CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.foot", seed, dateStr);
            writer.setPageEvent(new ExportPageEventsObservatoryMP(footerText, dateStr, isBasicService));
            ExportPageEventsObservatoryMP.setLastPage(false);

            IndexEvents index = new IndexEvents();
            writer.setPageEvent(index);
            writer.setLinearPageMode();

            document.open();

            PDFUtils.addTitlePage(document, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.title") + seed.toUpperCase(), pdfBuilder.getTitle(), ConstantsFont.documentTitleMPFont, ConstantsFont.documentSubtitleMPFont);

            int numChapter = 1;
            int countSections = 1;
            countSections = pdfBuilder.createIntroductionChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, isBasicService);
            numChapter++;
            countSections = pdfBuilder.createObjetiveChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, observatoryType);
            numChapter++;
            countSections = pdfBuilder.createMethodologyChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, evaList, observatoryType, isBasicService);
            numChapter++;

            // Resumen de las puntuaciones del Observatorio
            RankingInfo rankingInfo = crawling != null ? calculateRankings(c, idObservatoryExecution, crawling.getSeed()) : null;
            countSections = addObservatoryScoreSummary(pdfBuilder, request, document, index, evaList, numChapter, countSections, file, rankingInfo);
            numChapter++;

            // Resumen de las puntuaciones del Observatorio
            countSections = addObservatoryResultsSummary(pdfBuilder, request, document, index, evaList, numChapter, countSections);
            numChapter++;

            int counter = 1;
            for (ObservatoryEvaluationForm evaluationForm : evaList) {
                String evaluationTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.label", counter);
                Chapter chapter = PDFUtils.addChapterTitle(evaluationTitle, index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);


                String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
                String url = evaluationForm.getUrl();

                Paragraph p = new Paragraph();
                if (title != null && !(title.replace("...", "")).contains(url.replace("...", ""))) {
                    Phrase p1 = PDFUtils.createPhrase(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.url") + ": ", ConstantsFont.scoreBoldFont);
                    Phrase p2 = PDFUtils.createPhraseLink(url, url, ConstantsFont.scoreFont);
                    p.add(p1);
                    p.add(p2);

                }
                p.setSpacingBefore(ConstantsFont.SPACE_LINE);
                chapter.add(p);

                if (title != null) {
                    Font titleFont = title.length() > 50 ? ConstantsFont.descriptionFont : ConstantsFont.scoreFont;
                    Phrase p3 = PDFUtils.createPhrase(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.title") + ": ", ConstantsFont.scoreBoldFont);
                    Phrase p4 = PDFUtils.createPhrase(title, titleFont);
                    Paragraph titleParagraph = new Paragraph();
                    titleParagraph.add(p3);
                    titleParagraph.add(p4);
                    chapter.add(titleParagraph);
                }

                ArrayList<String> boldWords;

                boldWords = new ArrayList<String>();
                boldWords.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.media.pagina") + ": ");
                chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0} " + evaluationForm.getScore(), boldWords, ConstantsFont.scoreBoldFont, ConstantsFont.scoreFont, true));

                boldWords = new ArrayList<String>();
                boldWords.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.nivel.adecuacion") + ": ");
                chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0} " + ObservatoryUtils.getValidationLevel(request, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), boldWords, ConstantsFont.scoreBoldFont, ConstantsFont.scoreFont, false));

                PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.pagina"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, false);

                for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                    Paragraph levelTitle = new Paragraph(getPriorityName(request, observatoryLevelForm.getName()), ConstantsFont.levelTitleMPFont);
                    levelTitle.setSpacingBefore(10);
                    Section levelSection = chapter.addSection(levelTitle);
                    levelSection.setNumberDepth(0);
                    Paragraph levelScore = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.media.nivel.analisis") + ": " + observatoryLevelForm.getScore(), ConstantsFont.levelScoreFont);
                    levelSection.add(levelScore);
                    chapter.add(createTable(observatoryLevelForm, request));
                }

                // Lista de verificaciones que fallan
                Map<String, List<String>> failedChecks = IntavUtils.getFailedChecks(request, evaluationForm, guideline);
                if (failedChecks.keySet().size() != 0) {
                    addFailedChecksSection(request, failedChecks, chapter);
                }

                if (evaluationForm.getCrawlerExecutionId() < 0) {
                    addCheckCodes(request, evaluationForm, chapter);
                } else {
                    SpecialChunk externalLink = new SpecialChunk("http://forja-ctt.administracionelectronica.gob.es/web/caccesibilidad", ConstantsFont.paragraphAnchorFont);
                    externalLink.setExternalLink(true);
                    externalLink.setAnchor("http://forja-ctt.administracionelectronica.gob.es/web/caccesibilidad");
                    Map<Integer, SpecialChunk> specialChunkMap = new HashMap<Integer, SpecialChunk>();
                    specialChunkMap.put(1, externalLink);
                    chapter.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("resultados.primarios.errores.mas.info"), specialChunkMap, ConstantsFont.paragraphFont));
                }

                document.add(chapter);
                numChapter++;
                counter++;
            }

            if (!StringUtils.isEmpty(content)) {
                // Añadir el código fuente analizado
                countSections = pdfBuilder.createContentChapter(request, document, content, index, numChapter, countSections);
                numChapter++;
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

    private static void addCheckCodes(HttpServletRequest request, ObservatoryEvaluationForm evaluationForm, Chapter chapter) throws BadElementException, IOException {
        PropertiesManager pmgr = new PropertiesManager();

        Image imgProblemA = Image.getInstance(pmgr.getValue("pdf.properties", "path.problem"));
        Image imgWarnings = Image.getInstance(pmgr.getValue("pdf.properties", "path.warnings"));
        Image imgInfos = Image.getInstance(pmgr.getValue("pdf.properties", "path.infos"));

        imgProblemA.setAlt("");
        imgWarnings.setAlt("");
        imgInfos.setAlt("");

        imgProblemA.scalePercent(60);
        imgWarnings.scalePercent(60);
        imgInfos.scalePercent(60);

        for (ObservatoryLevelForm priority : evaluationForm.getGroups()) {
            if (hasProblems(priority)) {
                Paragraph priorityTitle = new Paragraph(getPriorityName(request, priority), ConstantsFont.levelTitleMPFont);
                priorityTitle.setSpacingBefore(10);
                Section prioritySection = chapter.addSection(priorityTitle);
                prioritySection.setNumberDepth(0);
                for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
                    if (hasProblems(level)) {
                        Paragraph levelTitle = new Paragraph(getLevelName(request, level), ConstantsFont.chapterTitleMPFont);
                        levelTitle.setSpacingBefore(10);
                        Section levelSection = prioritySection.addSection(levelTitle);
                        levelSection.setNumberDepth(0);
                        for (ObservatorySubgroupForm verification : level.getSubgroups()) {
                            if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                                PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage(verification.getDescription()), ConstantsFont.guidelineDescMPFont, levelSection);
                                for (ProblemForm problem : verification.getProblems()) {
                                    String description = "";
                                    Image image = null;
                                    com.lowagie.text.Font font = null;
                                    if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
                                        description += CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.warning") + ": ";
                                        image = imgWarnings;
                                        font = ConstantsFont.warningFont;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
                                        description += CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.problem") + ": ";
                                        image = imgProblemA;
                                        font = ConstantsFont.problemFont;
                                    } else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
                                        description += CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.info") + ": ";
                                        image = imgInfos;
                                        font = ConstantsFont.cannottellFont;
                                    }
                                    Paragraph p = PDFUtils.createImageParagraphWithDiferentFont(image, description, font, StringUtils.removeHtmlTags(CrawlerUtils.getResources(request).getMessage(problem.getError())), ConstantsFont.strongDescriptionFont, Chunk.ALIGN_LEFT);
                                    levelSection.add(p);

                                    if (StringUtils.isNotEmpty(problem.getRationale()) && CrawlerUtils.getResources(request).isPresent(problem.getRationale())) {
                                        PDFUtils.addParagraphRationale(Arrays.asList(CrawlerUtils.getResources(request).getMessage(problem.getRationale()).split("<p>|</p>")), levelSection);
                                    }

                                    BasicServiceExport.addSpecificProblems(request, levelSection, problem.getSpecificProblems());

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

    private static boolean hasProblems(ObservatoryLevelForm priority) {
        for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm verification : level.getSubgroups()) {
                if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean hasProblems(ObservatorySuitabilityForm level) {
        for (ObservatorySubgroupForm verification : level.getSubgroups()) {
            if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private static String getPriorityName(HttpServletRequest request, ObservatoryLevelForm priority) {
        PropertiesManager pmgr = new PropertiesManager();
        if (priority.getName().equals(pmgr.getValue("intav.properties", "priority.1"))) {
            return CrawlerUtils.getResources(request).getMessage("priority1.bs");
        } else if (priority.getName().equals(pmgr.getValue("intav.properties", "priority.2"))) {
            return CrawlerUtils.getResources(request).getMessage("priority2.bs");
        } else {
            return "";
        }
    }

    private static String getLevelName(HttpServletRequest request, ObservatorySuitabilityForm level) {
        PropertiesManager pmgr = new PropertiesManager();
        if (level.getName().equals(pmgr.getValue("intav.properties", "priority.1.level").toUpperCase())) {
            return CrawlerUtils.getResources(request).getMessage("first.level.bs");
        } else if (level.getName().equals(pmgr.getValue("intav.properties", "priority.2.level").toUpperCase())) {
            return CrawlerUtils.getResources(request).getMessage("second.level.bs");
        } else {
            return "";
        }
    }

    private static void addFailedChecksSection(HttpServletRequest request, Map<String, List<String>> failedChecks, Chapter chapter) {
        com.lowagie.text.List list = new com.lowagie.text.List();
        for (Map.Entry<String, List<String>> entry : failedChecks.entrySet()) {
            PDFUtils.addListItem(entry.getKey(), list, ConstantsFont.paragraphBoldFont, false, false);
            com.lowagie.text.List sublist = new com.lowagie.text.List();
            for (String check : entry.getValue()) {
                PDFUtils.addListItem(StringUtils.removeHtmlTags(check), sublist, ConstantsFont.paragraphFont, false, true);
            }
            sublist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
            list.add(sublist);
        }
        Paragraph verificationsTitle = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.observatorio.vista.primaria.errores.por.verificacion"), ConstantsFont.levelTitleMPFont);
        verificationsTitle.setSpacingAfter(ConstantsFont.SPACE_LINE);
        Section verificationsSection = chapter.addSection(verificationsTitle);
        verificationsSection.setNumberDepth(0);
        verificationsSection.add(list);
    }

    private static int addObservatoryScoreSummary(AnonymousResultExportPdf pdfBuilder, HttpServletRequest request, Document document, IndexEvents index, List<ObservatoryEvaluationForm> evaList, int numChapter, int countSections, File file, RankingInfo rankingInfo) throws Exception {
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.resultados.resumen").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);

        ArrayList<String> boldWord = new ArrayList<String>();

        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p1"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWord = new ArrayList<String>();
        boldWord.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p2.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p2"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWord = new ArrayList<String>();
        boldWord.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p3.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p3"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        ScoreForm scoreForm = pdfBuilder.generateScores(request, evaList);

        boldWord = new ArrayList<String>();
        boldWord.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.nivel.adecuacion") + ": ");
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + scoreForm.getLevel(), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, true));
        boldWord = new ArrayList<String>();
        boldWord.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.total") + ": ");
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + scoreForm.getTotalScore(), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, false));
        if (rankingInfo != null && rankingInfo.getGlobalSeedsNumber() > 1) {
            boldWord = new ArrayList<String>();
            boldWord.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.posicion.global") + ": ");
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + rankingInfo.getGlobalRank() + " " + CrawlerUtils.getResources(request).getMessage("de.text", rankingInfo.getGlobalSeedsNumber()), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, false));

            boldWord = new ArrayList<String>();
            boldWord.add(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.posicion.segmento", rankingInfo.getCategoria().getName()) + ": ");
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + rankingInfo.getCategoryRank() + " " + CrawlerUtils.getResources(request).getMessage("de.text", rankingInfo.getCategorySeedsNumber()), boldWord, ConstantsFont.summaryScoreBoldFont, ConstantsFont.summaryScoreFont, false));

            chapter.add(Chunk.NEWLINE);
        }

        String noDataMess = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");
        addLevelAllocationResultsSummary(request, chapter, file, evaList, noDataMess);
        //chapter.newPage();

        Section section = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("resultados.primarios.puntuaciones.verificacion1"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.41.p1"), ConstantsFont.paragraphFont, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder,request, section, file, evaList, noDataMess, Constants.OBS_PRIORITY_1);
        section.add(createGlobalTable(request, scoreForm, Constants.OBS_PRIORITY_1));
        //chapter.newPage();

        section = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("resultados.primarios.puntuaciones.verificacion2"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.42.p1"), ConstantsFont.paragraphFont, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder,request, section, file, evaList, noDataMess, Constants.OBS_PRIORITY_2);
        section.add(createGlobalTable(request, scoreForm, Constants.OBS_PRIORITY_2));

        PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("resultados.primarios.puntuacion.pagina"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        addResultsByPage(request, chapter, file, evaList, noDataMess);

        document.add(chapter);
        return countSections;
    }

    private static int addObservatoryResultsSummary(AnonymousResultExportPdf pdfBuilder, HttpServletRequest request, Document document, IndexEvents index, List<ObservatoryEvaluationForm> evaList, int numChapter, int countSections) throws Exception {
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.res.verificacion").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.5.p1"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.5.p2"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, false);
        countSections = addResultsByVerification(request, chapter, evaList, countSections, index);
        document.add(chapter);

        return countSections;
    }

    private static void addLevelAllocationResultsSummary(HttpServletRequest request, Section section,
                                                         File file, List<ObservatoryEvaluationForm> evaList, String noDataMess) throws Exception {

        Map<String, Integer> result = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(evaList);
        List<GraphicData> labelValueBeanList = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(request, result);

        String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test.jpg";
        String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.accessibility.level.allocation.by.page.title");
        ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(request, evaList, title, filePath, noDataMess);
        Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        section.add(image);

        float[] widths = {33f, 33f, 33f};
        PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.primarios.num.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        for (GraphicData label : labelValueBeanList) {
            table.addCell(PDFUtils.createTableCell(label.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section.add(table);
    }

    private static void addMidsComparationByVerificationLevelGraphic(AnonymousResultExportPdf pdfBuilder, HttpServletRequest request, Section section, File file, List<ObservatoryEvaluationForm> evaList, String noDataMess, String level) throws Exception {
        String title;
        String filePath;
        if (level.equals(Constants.OBS_PRIORITY_1)) {
            title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.title");
            filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test2.jpg";
        } else { //if (level.equals(Constants.OBS_PRIORITY_2)) {
            title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.title");
            filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test3.jpg";
        }
        PropertiesManager pmgr = new PropertiesManager();
        pdfBuilder.getMidsComparationByVerificationLevelGraphic(request, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), true);
        Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        section.add(image);
    }

    private static void addResultsByPage(HttpServletRequest request, Chapter chapter, File file, List<ObservatoryEvaluationForm> evaList, String noDataMess) throws Exception {
        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("resultados.primarios.43.p1.anchor"), CrawlerUtils.getResources(request).getMessage("anchor.PMP"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        chapter.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("resultados.primarios.43.p1"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.43.p2"), ConstantsFont.paragraphFont, chapter);

        chapter.add(Chunk.NEWLINE);

        String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.title");
        String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test4.jpg";
        ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(request, evaList, title, filePath, noDataMess);

        Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(80);
        image.setAlignment(Element.ALIGN_CENTER);
        chapter.add(image);

        // chapter.newPage();

        float[] widths = {33f, 33f, 33f};
        PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        int pageCounter = 0;
        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.label", ++pageCounter), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(String.valueOf(evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(ObservatoryUtils.getValidationLevel(request, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        chapter.add(table);
    }

    private static int addResultsByVerification(HttpServletRequest request, Chapter chapter, List<ObservatoryEvaluationForm> evaList, int countSections, IndexEvents index) {
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
                        lvb.setLabel(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.label", counter));
                        lvb.setValue(String.valueOf(subgroupForm.getValue()));
                        results.get(subgroupForm.getDescription()).add(lvb);
                    }
                }
            }
            counter++;
        }

        for (Map.Entry<String, List<LabelValueBean>> entry : results.entrySet()) {
            String id = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), entry.getKey()).substring(0, 5);
            String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), entry.getKey()).substring(6) + " (" + id + ")";
            Section section = PDFUtils.addSection(title, index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
            section.add(createVerificationTable(request, entry.getValue()));
            chapter.newPage();
        }

        return countSections;
    }

    private static PdfPTable createVerificationTable(HttpServletRequest request, List<LabelValueBean> results) {
        float[] widths = {0.50f, 0.25f, 0.25f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (LabelValueBean lvb : results) {
            int value = Integer.parseInt(lvb.getValue());
            table.addCell(PDFUtils.createTableCell(lvb.getLabel(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            if (value == Constants.OBS_VALUE_NOT_SCORE) {
                table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            } else if (value == Constants.OBS_VALUE_RED_ZERO || value == Constants.OBS_VALUE_GREEN_ZERO) {
                table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.cero"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            } else {
                table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.uno"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            }

			/*PropertiesManager pmgr = new PropertiesManager();
            Image image = null;
			try{
				if(value == Constants.OBS_VALUE_NOT_SCORE || value == Constants.OBS_VALUE_GREEN_ZERO || value == Constants.OBS_VALUE_GREEN_ONE) {
					image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
				} else {
					image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.red"));
				}
			}catch (Exception e) {
				Logger.putLog("Error al incluir imagen de modalidad", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_WARNING);
			}*/

            PdfPCell labelCell;
            if (value == Constants.OBS_VALUE_NOT_SCORE || value == Constants.OBS_VALUE_GREEN_ZERO || value == Constants.OBS_VALUE_GREEN_ONE) {
                //labelCell.addElement(PDFUtils.createImageTextParagraph(request, image, " " + getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"), ConstantsFont.descriptionFontGreen));
                labelCell = PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"), Color.WHITE, ConstantsFont.descriptionFontGreen, Element.ALIGN_CENTER, 0);
            } else {
                labelCell = PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.falla"), Color.WHITE, ConstantsFont.descriptionFontRed, Element.ALIGN_CENTER, 0);
            }
            labelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(labelCell);
        }

        return table;
    }

    private static String getPriorityName(HttpServletRequest request, String name) {
        PropertiesManager pmgr = new PropertiesManager();
        if (name.equals(pmgr.getValue("intav.properties", "priority.1"))) {
            return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.nivel.analisis.1");
        } else if (name.equals(pmgr.getValue("intav.properties", "priority.2"))) {
            return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.nivel.analisis.2");
        } else {
            return null;
        }
    }

    private static PdfPTable createTable(ObservatoryLevelForm observatoryLevelForm, HttpServletRequest request) {
        PropertiesManager pmgr = new PropertiesManager();

        float[] widths = {0.60f, 0.20f, 0.20f};
        PdfPTable table = new PdfPTable(widths);

        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.addCell(PDFUtils.createTableCell(request, "resultados.observatorio.vista.primaria.verificacion", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(request, "resultados.observatorio.vista.primaria.valor", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(request, "resultados.observatorio.vista.primaria.modalidad", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
            for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
                String value = null;
                String modality = null;
                Image image;

                try {
                    // TODO: ¿Sirve de algo la imagen en este bloque?, no se usa después
                    if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
                        value = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.observatorio.vista.primaria.valor.uno");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
                        image.scalePercent(72);
                        modality = CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    } else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ZERO) {
                        value = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.observatorio.vista.primaria.valor.cero");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
                        image.scalePercent(72);
                        modality = CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    } else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
                        value = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.observatorio.vista.primaria.valor.cero");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.red"));
                        image.scalePercent(72);
                        modality = CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.falla");
                    }
                    if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_NOT_SCORE) {
                        value = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.observatorio.vista.primaria.valor.noPuntua");
                        image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
                        image.scalePercent(72);
                        modality = CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
                    }
                } catch (Exception e) {
                    Logger.putLog("Error al crear tabla, resultados primarios.", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
                }

                PdfPCell descriptionCell = new PdfPCell(new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), observatorySubgroupForm.getDescription()), ConstantsFont.descriptionFont));
                PdfPCell valueCell = PDFUtils.createTableCell(value, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0);
                PdfPCell labelCell;
                if (modality != null && modality.equals(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"))) {
                    labelCell = PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"), Color.WHITE, ConstantsFont.descriptionFontGreen, Element.ALIGN_CENTER, 0);
                } else {
                    labelCell = PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad.falla"), Color.WHITE, ConstantsFont.descriptionFontRed, Element.ALIGN_CENTER, 0);
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

    private static PdfPTable createGlobalTable(HttpServletRequest request, ScoreForm scoreForm, String level) {
        float[] widths = {0.7f, 0.3f};
        PdfPTable table = new PdfPTable(widths);
        int margin = 10;

        table.addCell(PDFUtils.createTableCell(request, "resultados.observatorio.vista.primaria.verificacion", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(request, "resultados.observatorio.vista.primaria.puntuacion.media", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        if (level.equals(Constants.OBS_PRIORITY_1)) {
            for (LabelValueBean label : scoreForm.getVerifications1()) {
                table.addCell(PDFUtils.createTableCell(label.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
                table.addCell(PDFUtils.createTableCell(label.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            }
            table.addCell(PDFUtils.createTableCell(request, "observatorio.puntuacion.nivel.1", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(scoreForm.getScoreLevel1().toString(), Constants.NARANJA_MP, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        } else if (level.equals(Constants.OBS_PRIORITY_2)) {
            for (LabelValueBean label : scoreForm.getVerifications2()) {
                table.addCell(PDFUtils.createTableCell(label.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
                table.addCell(PDFUtils.createTableCell(label.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            }
            table.addCell(PDFUtils.createTableCell(request, "observatorio.puntuacion.nivel.2", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(scoreForm.getScoreLevel2().toString(), Constants.NARANJA_MP, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.setSpacingAfter(2 * ConstantsFont.SPACE_LINE);
        return table;
    }

    private static RankingInfo calculateRankings(Connection c, Long idObservatoryExecution, SemillaForm currentSeed) throws Exception {
        RankingInfo rankingInfo = new RankingInfo();
        List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, new SemillaForm(), idObservatoryExecution, (long) 0, Constants.NO_PAGINACION);
        rankingInfo.setGlobalSeedsNumber(seedsResults.size());
        rankingInfo.setCategorySeedsNumber(0);
        rankingInfo.setGlobalRank(1);
        rankingInfo.setCategoryRank(1);
        rankingInfo.setCategoria(currentSeed.getCategoria());

        try {
            seedsResults = ObservatoryUtils.setAvgScore(c, seedsResults, idObservatoryExecution);
            BigDecimal scoreSeed = BigDecimal.ZERO;
            // Buscamos la puntuación concreta de la semilla
            for (ResultadoSemillaForm seedForm : seedsResults) {
                if (Long.parseLong(seedForm.getId()) == currentSeed.getId()) {
                    scoreSeed = new BigDecimal(seedForm.getScore());
                }
            }

            // Miramos el ranking comparando con el resto de semillas
            for (ResultadoSemillaForm seedForm : seedsResults) {
                if (seedForm.getScore() != null) {
                    if (new BigDecimal(seedForm.getScore()).compareTo(scoreSeed) == 1) {
                        rankingInfo.setGlobalRank(rankingInfo.getGlobalRank() + 1);
                    }
                    if (currentSeed.getCategoria().getId().equals(String.valueOf(seedForm.getIdCategory()))) {
                        rankingInfo.setCategorySeedsNumber(rankingInfo.getCategorySeedsNumber() + 1);
                        if (new BigDecimal(seedForm.getScore()).compareTo(scoreSeed) == 1) {
                            rankingInfo.setCategoryRank(rankingInfo.getCategoryRank() + 1);
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
