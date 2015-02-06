package es.inteco.rastreador2.pdf;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.*;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryBS;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicsUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.utils.FileUtils;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class BasicServiceExport {

    private static final PropertiesManager PMGR;
    private static final int SPACE_LINE = 15;
    private static final int X;
    private static final int Y;
    private static final String COLOR;

    static {
        PMGR = new PropertiesManager();

        X = Integer.parseInt(PMGR.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.x"));
        Y = Integer.parseInt(PMGR.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.y"));

        COLOR = PMGR.getValue(CRAWLER_PROPERTIES, "chart.pdf.intav.colors");
    }

    private BasicServiceExport() {
    }

    public static void generatePDF(HttpServletRequest request, BasicServiceForm basicServiceForm, List<Long> evaluationIds, Long idCrawling, String pdfPath) throws Exception {
        try {
            if (evaluationIds != null && !evaluationIds.isEmpty()) {
                createPdf(request, evaluationIds, basicServiceForm, idCrawling, pdfPath);
            } else {
                Logger.putLog("No existen analisis en la base de datos de INTAV para la entidad: " + basicServiceForm.getName(), BasicServiceExport.class, Logger.LOG_LEVEL_WARNING);
            }
        } catch (Exception e) {
            Logger.putLog("Excepción al generar el PDF para la entidad: " + basicServiceForm.getName(), BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static Map<String, List<EvaluationForm>> getResultData(List<Long> evaluationIds, String language) throws Exception {
        Map<String, List<EvaluationForm>> evolutionMap = new HashMap<String, List<EvaluationForm>>();

        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();

            // Inicializamos el evaluador si hace falta
            if (!EvaluatorUtility.isInitialized()) {
                try {
                    EvaluatorUtility.initialize();
                } catch (Exception e) {
                    Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }

            List<EvaluationForm> evaList = new ArrayList<EvaluationForm>();
            for (Long id : evaluationIds) {
                Evaluator evaluator = new Evaluator();
                Evaluation evaluation = evaluator.getAnalisisDB(conn, id, EvaluatorUtils.getDocList(), false);
                EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, language);
                evaList.add(evaluationForm);
            }

            GregorianCalendar calendar = new GregorianCalendar();
            Date d = calendar.getTime();
            DateFormat df = new SimpleDateFormat(PMGR.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
            evolutionMap.put(df.format(d), evaList);

            return evolutionMap;
        } catch (Exception e) {
            Logger.putLog("Excepción genérica al generar el pdf", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }

    public static String compressReport(final String reportFile) {
        final String reportCompressFile;
        if (reportFile.endsWith(".pdf")) {
            reportCompressFile = reportFile.substring(0, reportFile.length() - 4) + ".zip";
        } else {
            reportCompressFile = reportFile + ".zip";
        }

        final byte[] buffer = new byte[1024];
        ZipOutputStream zos = null;
        BufferedInputStream in = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(reportCompressFile));
            in = new BufferedInputStream(new FileInputStream(reportFile));

            ZipEntry ze = new ZipEntry(new File(reportFile).getName());
            zos.putNextEntry(ze);

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            zos.closeEntry();
        } catch (Exception e) {
            Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            if ( in!=null ) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
            if ( zos!=null ) {
                try {
                    zos.close();
                } catch (IOException e) {
                    Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
        Logger.putLog("PDF comprimido a ZIP correctamente", BasicServiceExport.class, Logger.LOG_LEVEL_INFO);
        return reportCompressFile;
    }

    public static void createPdf(HttpServletRequest request, List<Long> evaluationIds, BasicServiceForm basicServiceForm, Long idCrawling, String pdfPath) throws Exception {
        Map<String, List<EvaluationForm>> resultData = getResultData(evaluationIds, basicServiceForm.getLanguage());

        File file = new File(pdfPath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Logger.putLog("No se han podido crear los directorios para la exportación en PDF", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR);
        }

        List<EvaluationForm> evaList = new ArrayList<EvaluationForm>();
        String fechaInforme = "";
        for (Map.Entry<String, List<EvaluationForm>> entry : resultData.entrySet()) {
            fechaInforme = entry.getKey();
            evaList = entry.getValue();
        }

        Document document = new Document(PageSize.A4, 50, 50, 120, 72);
        String chartsTempPath = new File(pdfPath).getParent() + File.separator + "temp" + File.separator;

        try {
            FileOutputStream fileOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(document, fileOut);
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            SimpleDateFormat sdf = new SimpleDateFormat(PMGR.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
            writer.setPageEvent(new ExportPageEventsObservatoryBS(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.foot.text") + basicServiceForm.getName().toUpperCase() + " (" + sdf.format(new Date()) + ")", sdf.format(new Date())));
            ExportPageEventsObservatoryBS.setLastPage(false);

            IndexEvents index = new IndexEvents();
            writer.setPageEvent(index);
            writer.setLinearPageMode();

            document.open();

            PDFUtils.addTitlePage(document, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.bs.title") + basicServiceForm.getName().toUpperCase(), CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.subtitle"), ConstantsFont.documentTitleMPFont, ConstantsFont.documentSubtitleMPFont);

            int numChapter = 1;
            int countSections = 1;

            countSections = createIntroductionChapter(request, document, evaList, index, numChapter, countSections);
            numChapter++;

            countSections = createGlobalChapter(request, document, evaList, chartsTempPath, index, fechaInforme, numChapter, countSections);
            numChapter++;

            Image imgProblemA = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.problem"));
            Image imgWarnings = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.warnings"));
            Image imgInfos = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.infos"));

            imgProblemA.setAlt("");
            imgWarnings.setAlt("");
            imgInfos.setAlt("");

            imgProblemA.scalePercent(60);
            imgWarnings.scalePercent(60);
            imgInfos.scalePercent(60);

            int counter = 1;
            for (EvaluationForm evaluationForm : evaList) {
                String chapterTitle = CrawlerUtils.getResources(request).getMessage("basic.service.url.title", counter++);
                Chapter chapter = PDFUtils.addChapterTitle(chapterTitle.toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont, false);

                String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
                String url = evaluationForm.getUrl();
                Paragraph urlParagraph = new Paragraph();
                if (title != null && !(title.replace("...", "")).contains(url.replace("...", ""))) {
                    Phrase p1 = PDFUtils.createPhrase(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.url") + ": ", ConstantsFont.scoreBoldFont);
                    Font urlFont = url.length() > 50 ? ConstantsFont.descriptionFont : ConstantsFont.scoreFont;
                    String urlTitle = url.length() > 75 ? url.substring(0, 75) + "..." : url;
                    Phrase p2 = PDFUtils.createPhraseLink(urlTitle, url, urlFont);
                    urlParagraph.add(p1);
                    urlParagraph.add(p2);
                }
                urlParagraph.setSpacingBefore(ConstantsFont.SPACE_LINE);
                chapter.add(urlParagraph);

                if (title != null) {
                    Font titleFont = title.length() > 50 ? ConstantsFont.descriptionFont : ConstantsFont.scoreFont;
                    Phrase p3 = PDFUtils.createPhrase(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.title") + ": "+ title, ConstantsFont.scoreBoldFont);
                    //Phrase p4 = PDFUtils.createPhrase(title, titleFont);
                    //p3.add(PDFUtils.createPhrase(title, titleFont));
                    Paragraph titleParagraph = new Paragraph();
                    titleParagraph.add(p3);
                    //titleParagraph.add(p4);
                    chapter.add(titleParagraph);
                }

                com.lowagie.text.List summaryPriorities1 = addSummary(request, evaluationForm.getPriorities());
                chapter.add(summaryPriorities1);

                generateGraphic(request, evaluationForm.getPriorities(), CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.priority.incidence"), chartsTempPath);
                Image imgGp = PDFUtils.createImage(chartsTempPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.priority.incidence") + ".jpg", CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.priority.incidence"));

                if (imgGp != null) {
                    chapter.add(imgGp);
                }
                chapter.newPage();

                int totalProblems = 0;
                for (PriorityForm priority : evaluationForm.getPriorities()) {
                    totalProblems = priority.getNumInfos() + priority.getNumProblems() + priority.getNumWarnings();
                }
                if (totalProblems > 0) {
                    chapter.add(createProblemsIndex(request, evaluationForm.getPriorities()));
                }

                chapter.newPage();

                for (PriorityForm priority : evaluationForm.getPriorities()) {
                    Section section = PDFUtils.addSection(getPriorityName(request, priority), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
                    for (GuidelineForm guideline : priority.getGuidelines()) {
                        for (PautaForm pautaForm : guideline.getPautas()) {
                            Section subSection = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage(pautaForm.getName()), null, ConstantsFont.guidelineDescMPFont, section, -1, 1);
                            subSection.setNumberDepth(0);
                            for (ProblemForm problem : pautaForm.getProblems()) {
                                String description = "";
                                Image image = null;
                                com.lowagie.text.Font font = null;
                                if (problem.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
                                    description += CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.warning") + ": ";
                                    image = imgWarnings;
                                    font = ConstantsFont.warningFont;
                                } else if (problem.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
                                    description += CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.problem") + ": ";
                                    image = imgProblemA;
                                    font = ConstantsFont.problemFont;
                                } else if (problem.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
                                    description += CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.info") + ": ";
                                    image = imgInfos;
                                    font = ConstantsFont.cannottellFont;
                                }
                                Paragraph p = PDFUtils.createImageParagraphWithDiferentFont(image, description, font, StringUtils.removeHtmlTags(CrawlerUtils.getResources(request).getMessage(problem.getError())), ConstantsFont.strongDescriptionFont, Chunk.ALIGN_LEFT);
                                subSection.add(p);

                                if (StringUtils.isNotEmpty(problem.getRationale())) {
                                    PDFUtils.addParagraphRationale(Arrays.asList(CrawlerUtils.getResources(request).getMessage(problem.getRationale()).split("<p>|</p>")), subSection);
                                }

                                addSpecificProblems(request, subSection, problem.getSpecificProblems());

                                if (problem.getCheck().equals(PMGR.getValue("check.properties", "doc.valida.especif")) ||
                                        problem.getCheck().equals(PMGR.getValue("check.properties", "css.valida.especif"))) {
                                    addW3CCopyright(subSection, problem.getCheck());
                                }
                            }
                        }
                    }
                }
                document.add(chapter);
                numChapter++;
            }

			/*Chapter evolutionChapter = evolutionChapter(request, chartsTempPath, index, basicServiceForm.getLanguage(), path, DiagnosisDAO.getEvaluationIds(basicServiceForm.getName(), idCrawling) , numChapter, basicServiceForm.getName());
            if (evolutionChapter !=  null)    {
	        	document.add(evolutionChapter);
	        }*/

            if (basicServiceForm.isContentAnalysis()) {
                // Añadir el código fuente analizado
                countSections = createContentChapter(request, document, basicServiceForm.getContent(), index, numChapter, countSections);
                numChapter++;
            }

            IndexUtils.createIndex(writer, document, request, index, false, ConstantsFont.chapterTitleMPFont);
            ExportPageEventsObservatoryBS.setLastPage(false);

            FileUtils.removeFile(chartsTempPath);
            Logger.putLog("PDF generado correctamente.", BasicServiceExport.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception e) {
            Logger.putLog("Excepción genérica al generar el pdf", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            if (document != null && document.isOpen()) {
                try {
                    document.close();
                } catch (Exception e) {
                    Logger.putLog("Error al cerrar el pdf", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }

    private static List<PriorityForm> createPriorityList(List<EvaluationForm> evaList) {
        PriorityForm priorityFormA = new PriorityForm();
        priorityFormA.setPriorityName(PMGR.getValue("intav.properties", ("priority.1.name")));
        PriorityForm priorityFormAA = new PriorityForm();
        priorityFormAA.setPriorityName(PMGR.getValue("intav.properties", ("priority.2.name")));

        for (EvaluationForm evaluationForm : evaList) {
            for (PriorityForm priority : evaluationForm.getPriorities()) {
                if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.1.name")))) {
                    priorityFormA.setNumProblems(priorityFormA.getNumProblems() + priority.getNumProblems());
                    priorityFormA.setNumWarnings(priorityFormA.getNumWarnings() + priority.getNumWarnings());
                    priorityFormA.setNumInfos(priorityFormA.getNumInfos() + priority.getNumInfos());
                }
                if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.2.name")))) {
                    priorityFormAA.setNumProblems(priorityFormAA.getNumProblems() + priority.getNumProblems());
                    priorityFormAA.setNumWarnings(priorityFormAA.getNumWarnings() + priority.getNumWarnings());
                    priorityFormAA.setNumInfos(priorityFormAA.getNumInfos() + priority.getNumInfos());
                }
            }
        }

        List<PriorityForm> prioList = new ArrayList<PriorityForm>();
        prioList.add(priorityFormA);
        prioList.add(priorityFormAA);

        return prioList;
    }

    private static com.lowagie.text.List createProblemsIndex(HttpServletRequest request, List<PriorityForm> priorityList) throws BadElementException, IOException {
        final com.lowagie.text.List priorityTextList = new com.lowagie.text.List(false, 10);
        for (PriorityForm priority : priorityList) {
            final com.lowagie.text.List problemList = new com.lowagie.text.List(false, 10);
            final com.lowagie.text.List warningList = new com.lowagie.text.List(false, 10);
            final com.lowagie.text.List infoList = new com.lowagie.text.List(false, 10);
            for (GuidelineForm guideline : priority.getGuidelines()) {
                boolean hasProblem = false;
                boolean hasWarning = false;
                boolean hasInfos = false;
                for (PautaForm pautaForm : guideline.getPautas()) {
                    for (ProblemForm problemForm : pautaForm.getProblems()) {
                        if (problemForm.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high")) && !hasProblem) {
                            problemList.add(PDFUtils.createListItem(CrawlerUtils.getResources(request).getMessage(pautaForm.getName()), ConstantsFont.paragraphFont, ConstantsFont.listSymbol2, false));
                            hasProblem = true;
                        }
                        if (problemForm.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium")) && !hasWarning) {
                            warningList.add(PDFUtils.createListItem(CrawlerUtils.getResources(request).getMessage(pautaForm.getName()), ConstantsFont.paragraphFont, ConstantsFont.listSymbol2, false));
                            hasWarning = true;
                        }
                        if (problemForm.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell")) && !hasInfos) {
                            infoList.add(PDFUtils.createListItem(CrawlerUtils.getResources(request).getMessage(pautaForm.getName()), ConstantsFont.paragraphFont, ConstantsFont.listSymbol2, false));
                            hasInfos = true;
                        }
                    }
                }
            }

            final ListItem priorityItem = PDFUtils.createListItem(CrawlerUtils.getResources(request).getMessage(priority.getPriorityName().replace(" ", "").toLowerCase() + ".bs"), ConstantsFont.paragraphBoldTitleFont, ConstantsFont.listSymbol1, true);
            priorityItem.add(createProblemList(request, problemList, warningList, infoList, priority));
            priorityTextList.add(priorityItem);
        }
        return priorityTextList;
    }

    private static com.lowagie.text.List createProblemList(HttpServletRequest request, com.lowagie.text.List problemList, com.lowagie.text.List warningList, com.lowagie.text.List infoList, PriorityForm priority) throws BadElementException, IOException {
        com.lowagie.text.List pwiList = new com.lowagie.text.List();

        Image imgProblemA = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.problem"));
        Image imgWarnings = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.warnings"));
        Image imgInfos = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.infos"));

        imgProblemA.setAlt("");
        imgWarnings.setAlt("");
        imgInfos.setAlt("");

        imgProblemA.scalePercent(60);
        imgWarnings.scalePercent(60);
        imgInfos.scalePercent(60);

        ListItem problemItem = PDFUtils.createListItem(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.problems") + ": " + priority.getNumProblems() + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.ver.point", problemList.size()), ConstantsFont.problemFont, new Chunk(imgProblemA, 0, 0), false);
        ListItem warningItem = PDFUtils.createListItem(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.warnings") + ": " + priority.getNumWarnings() + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.ver.point", warningList.size()), ConstantsFont.warningFont, new Chunk(imgWarnings, 0, 0), false);
        ListItem infoItem = PDFUtils.createListItem(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.infos") + ": " + priority.getNumInfos() + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.ver.point", infoList.size()), ConstantsFont.cannottellFont, new Chunk(imgInfos, 0, 0), false);

        problemItem.setSpacingBefore(8);
        warningItem.setSpacingBefore(8);
        infoItem.setSpacingBefore(8);

        problemItem.add(problemList);
        warningItem.add(warningList);
        infoItem.add(infoList);

        pwiList.add(problemItem);
        pwiList.add(warningItem);
        pwiList.add(infoItem);
        pwiList.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);

        return pwiList;
    }

    private static int createIntroductionChapter(HttpServletRequest request, Document d, List<EvaluationForm> evaList, IndexEvents index, int numChapter, int countSections) throws Exception {
        int numSections = countSections;
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.index.introduction"), index, numSections++, numChapter, ConstantsFont.chapterTitleMPFont);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.p1"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.p2"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.p3"), ConstantsFont.paragraphFont, chapter);

        numSections = createSection11(request, index, chapter, numSections, evaList);
        numSections++;
        numSections = createSection12(request, index, chapter, numSections);
        numSections++;
        numSections = createSection13(request, index, chapter, numSections);
        numSections++;

        d.add(chapter);
        return numSections;

    }

    private static int createSection11(HttpServletRequest request, IndexEvents index, Chapter chapter, int countSections, List<EvaluationForm> evalFormList) throws Exception {
        Section section = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.index.introduction.1"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections, 1);

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.1.p1.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.1.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.1.p2"), ConstantsFont.paragraphFont, section);

        section.add(addURLTable(request, evalFormList));

        return countSections;
    }

    public static PdfPTable addURLTable(HttpServletRequest request, java.util.List<EvaluationForm> primaryReportPageList) {
        float[] widths = {30f, 70f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.setWidthPercentage(100);

        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.1.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.1.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        int i = 1;
        for (EvaluationForm page : primaryReportPageList) {
            table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("observatory.graphic.score.by.page.label", i++), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
            PdfPCell url = new PdfPCell(PDFUtils.addLinkParagraph(page.getUrl(), page.getUrl(), ConstantsFont.noteCellFont));
            table.addCell(url);
        }

        return table;
    }

    private static int createSection12(HttpServletRequest request, IndexEvents index, Chapter chapter, int countSections) throws Exception {
        Section section = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.index.introduction.2"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections, 1);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p1"), ConstantsFont.paragraphFont, section);

        com.lowagie.text.List list = new com.lowagie.text.List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p2.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p2"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p3.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p4.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p4"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.2.p5"), ConstantsFont.paragraphFont, section);

        return countSections;
    }

    private static int createSection13(HttpServletRequest request, IndexEvents index, Chapter chapter, int countSections) throws Exception {
        int numSection = countSections;
        Section section = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.index.introduction.3"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections, 1);

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.3.p1.bold1"));
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.3.p1.bold2"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.3.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        Section section1 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.index.introduction.31"), index, ConstantsFont.chapterTitleMPFont3L, section, countSections, 2);
        numSection++;
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.31.p1"), ConstantsFont.paragraphFont, section1);

        Section section2 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.index.introduction.32"), index, ConstantsFont.chapterTitleMPFont3L, section, countSections, 2);
        numSection++;
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p2"), ConstantsFont.paragraphFont, section2);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p3"), ConstantsFont.paragraphFont, section2);

        com.lowagie.text.List list = new com.lowagie.text.List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p4.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p4"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p5.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p6.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p7.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p8.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("basic.service.introduction.32.p8"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        return numSection;
    }

    private static int createGlobalChapter(HttpServletRequest request, Document d, List<EvaluationForm> evaList,
                                           String globalPath, IndexEvents index, String fechaInforme, int numChapter, int countSections) throws Exception {
        int numSections = countSections;
        Chapter chapter1 = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.index.global.summary"), index, numSections++, numChapter, ConstantsFont.chapterTitleMPFont);

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.entity"));
        chapter1.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + EvaluatorUtils.getEntityName(evaList), boldWords, ConstantsFont.paragraphBoldTitleFont, ConstantsFont.paragraphTitleFont, true));
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.domain"));
        chapter1.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + evaList.get(0).getUrl(), boldWords, ConstantsFont.paragraphBoldTitleFont, ConstantsFont.paragraphTitleFont, false));
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.date"));
        chapter1.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + fechaInforme, boldWords, ConstantsFont.paragraphBoldTitleFont, ConstantsFont.paragraphTitleFont, false));

        List<PriorityForm> prioList = createPriorityList(evaList);

        com.lowagie.text.List summaryPriorities = addSummary(request, prioList);
        chapter1.add(summaryPriorities);

        generateGraphic(request, prioList, CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.global.priority.incidence"), globalPath);
        Image globalImgGp = PDFUtils.createImage(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.global.priority.incidence") + ".jpg", CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.global.priority.incidence"));
        if (globalImgGp != null) {
            chapter1.add(globalImgGp);
        }

        d.add(chapter1);

        return numSections;
    }

    private static int createContentChapter(HttpServletRequest request, Document d, String contents, IndexEvents index, int numChapter, int countSections) throws Exception {
        int numSections = countSections;
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage("basic.service.content.title"), index, numSections++, numChapter, ConstantsFont.chapterTitleMPFont);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.content.p1"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, true);
        PDFUtils.addParagraphCode(HTMLEntities.unhtmlAngleBrackets(contents), "", chapter);
        d.add(chapter);

        return numSections;
    }

    private static com.lowagie.text.List addSummary(HttpServletRequest request, List<PriorityForm> prioList) throws BadElementException, IOException {
        com.lowagie.text.List summaryPriorities = new com.lowagie.text.List();

        int count = prioList.size();

        for (PriorityForm priority : prioList) {
            ListItem priorityList = new ListItem(getPriorityName(request, priority), ConstantsFont.summaryTitleFont);
            priorityList.setSpacingBefore(8);
            priorityList.setListSymbol(new Chunk());
            summaryPriorities.add(priorityList);
            com.lowagie.text.List summaryStatistics = new com.lowagie.text.List();
            summaryStatistics.setIndentationLeft(2 * ConstantsFont.IDENTATION_LEFT_SPACE);

            ListItem problemList = new ListItem(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.problems") + ": " + priority.getNumProblems(), ConstantsFont.summaryFontProblem);
            ListItem warningsList = new ListItem(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.warnings") + ": " + priority.getNumWarnings(), ConstantsFont.summaryFontWarning);
            ListItem infosList = new ListItem(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.infos") + ": " + priority.getNumInfos(), ConstantsFont.summaryFontCannottell);

            Image imgProblemA = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.problem"));
            Image imgWarnings = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.warnings"));
            Image imgInfos = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.infos"));

            imgProblemA.setAlt("");
            imgWarnings.setAlt("");
            imgInfos.setAlt("");

            imgProblemA.scalePercent(60);
            imgWarnings.scalePercent(60);
            imgInfos.scalePercent(60);

            problemList.setListSymbol(new Chunk(imgProblemA, 0, 0));
            problemList.setSpacingBefore(8);
            warningsList.setListSymbol(new Chunk(imgWarnings, 0, 0));
            warningsList.setSpacingBefore(8);
            infosList.setListSymbol(new Chunk(imgInfos, 0, 0));
            infosList.setSpacingBefore(8);

            count--;
            if (count == 0) {
                infosList.setSpacingAfter(SPACE_LINE);
            }

            summaryStatistics.add(problemList);
            summaryStatistics.add(warningsList);
            summaryStatistics.add(infosList);

            summaryPriorities.add(summaryStatistics);
        }
        return summaryPriorities;
    }

    private static String getPriorityName(HttpServletRequest request, PriorityForm priority) {
        if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", "priority.1.name"))) {
            return CrawlerUtils.getResources(request).getMessage("first.level.incidents.bs");
        } else if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", "priority.2.name"))) {
            return CrawlerUtils.getResources(request).getMessage("second.level.incidents.bs");
        } else {
            return CrawlerUtils.getResources(request).getMessage("third.level.incidents.bs");
        }
    }

    private static void generateGraphic(HttpServletRequest request, List<PriorityForm> priorityFormList, String tableName, String tempPath) {
        DefaultCategoryDataset priorityDataSet = new DefaultCategoryDataset();

        for (PriorityForm priority : priorityFormList) {
            if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.1.name")))) {
                if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
                    priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage("chart.problems.bs"), CrawlerUtils.getResources(request).getMessage("first.level.incidents.export.bs"));
                    priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage("chart.warnings.bs"), CrawlerUtils.getResources(request).getMessage("first.level.incidents.export.bs"));
                    priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage("chart.infos.bs"), CrawlerUtils.getResources(request).getMessage("first.level.incidents.export.bs"));
                }
            } else if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.2.name")))) {
                if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
                    priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage("chart.problems.bs"), CrawlerUtils.getResources(request).getMessage("second.level.incidents.export.bs"));
                    priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage("chart.warnings.bs"), CrawlerUtils.getResources(request).getMessage("second.level.incidents.export.bs"));
                    priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage("chart.infos.bs"), CrawlerUtils.getResources(request).getMessage("second.level.incidents.export.bs"));
                }
            } else if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.3.name")))) {
                if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
                    priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage("chart.problems.bs"), CrawlerUtils.getResources(request).getMessage("third.level.incidents.export.bs"));
                    priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage("chart.warnings.bs"), CrawlerUtils.getResources(request).getMessage("third.level.incidents.export.bs"));
                    priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage("chart.infos.bs"), CrawlerUtils.getResources(request).getMessage("third.level.incidents.export.bs"));
                }
            }
        }
        try {
            String rowTitle = CrawlerUtils.getResources(request).getMessage("chart.intav.bs.rowTitle");
            ChartForm chart = new ChartForm(tableName, "", rowTitle, priorityDataSet, true, true, false, false, true, false, false, X, Y, COLOR);
            GraphicsUtils.createSeriesBarChart(chart, tempPath + tableName + ".jpg", "", request, false);
        } catch (Exception e) {
            Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void addSpecificProblems(HttpServletRequest request, Section subSubSection, List<SpecificProblemForm> specificProblems) {
        int maxNumErrors = Integer.parseInt(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"));
        for (SpecificProblemForm specificProblem : specificProblems) {
            maxNumErrors--;
            if (specificProblem.getCode() != null) {
                StringBuilder code = new StringBuilder();
                for (int i = 0; i < specificProblem.getCode().size(); i++) {
                    code.append(specificProblem.getCode().get(i)).append("\n");

                }
                PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.line", specificProblem.getLine(), specificProblem.getColumn()), ConstantsFont.codeCellFont, subSubSection, Element.ALIGN_LEFT, true, false);
                PDFUtils.addParagraphCode(HTMLEntities.unhtmlAngleBrackets(code.toString()), specificProblem.getMessage(), subSubSection);
            } else if (specificProblem.getNote() != null) {
                String linkCode = getMatch(specificProblem.getNote().get(0), "(<a.*?</a>)");
                String paragraphText = specificProblem.getNote().get(0).replace(linkCode, "");

                String linkHref = getMatch(specificProblem.getNote().get(0), "href='(.*?)'");

                Paragraph p = new Paragraph(paragraphText, ConstantsFont.noteCellFont);
                Anchor anchor = new Anchor(getMatch(specificProblem.getNote().get(0), "<a.*?>(.*?)</a>"), ConstantsFont.noteAnchorCellFont);
                anchor.setReference(linkHref);
                p.add(anchor);
                subSubSection.add(p);
            }

            if (maxNumErrors < 0) {
                if (specificProblems.size() > Integer.parseInt(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"))) {
                    String[] arguments = new String[2];
                    arguments[0] = PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number");
                    arguments[1] = String.valueOf(specificProblems.size());
                    Paragraph p = new Paragraph(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.bs.num.errors.summary", arguments), ConstantsFont.moreInfoFont);
                    p.setAlignment(Paragraph.ALIGN_RIGHT);
                    subSubSection.add(p);
                }

                break;
            }
        }
    }

    private static String getMatch(String text, String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

    public static void addW3CCopyright(Section subSubSection, String check) {
        Paragraph p = new Paragraph();
        p.setAlignment(Paragraph.ALIGN_RIGHT);
        Anchor anchor = null;
        if (check.equals(PMGR.getValue("check.properties", "doc.valida.especif"))) {
            anchor = new Anchor(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright"), ConstantsFont.moreInfoFont);
            anchor.setReference(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
        } else if (check.equals(PMGR.getValue("check.properties", "css.valida.especif"))) {
            anchor = new Anchor(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.css.copyright"), ConstantsFont.moreInfoFont);
            anchor.setReference(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
        }
        p.add(anchor);
        subSubSection.add(p);
    }

}
