package es.inteco.rastreador2.pdf.utils;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.database.export.SiteTranslationInformationForm;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.form.LanguageFoundForm;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ValidationLanguageForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.rastreador2.pdf.MultilanguageAnonymousResultExportPdfSections;
import es.inteco.rastreador2.pdf.PrimaryExportPdfAction;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMultilanguage;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioMultilanguageUtils;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.util.List;

public final class MultilanguagePrimaryExportPdfUtils {

    private MultilanguagePrimaryExportPdfUtils() {
    }

    public static void exportToPdf(Long idExecution, List<AnalysisForm> analysisList, HttpServletRequest request, String generalExpPath, String seed, long observatoryType) throws Exception {
        File file = new File(generalExpPath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Logger.putLog("Error al crear directorios en MultilanguagePrimaryExportPdfUtils ", ExportAction.class, Logger.LOG_LEVEL_ERROR);
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        Document document = new Document(PageSize.A4, 50, 50, 120, 72);

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();

            PdfWriter writer = PdfWriter.getInstance(document, fileOut);
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));

            FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(c, idExecution);

            String dateStr = crawling != null ? crawling.getDate() : CrawlerUtils.formatDate(new Date());

            boolean isBasicService = idExecution < 0;
            String footerText = CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.foot", seed, dateStr);
            writer.setPageEvent(new ExportPageEventsObservatoryMultilanguage(footerText, dateStr, isBasicService));
            ExportPageEventsObservatoryMultilanguage.setLastPage(false);

            IndexEvents index = new IndexEvents();
            writer.setPageEvent(index);
            writer.setLinearPageMode();

            document.open();

            PDFUtils.addTitlePage(document, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.multilanguage.title") + " " + seed.toUpperCase(), "", ConstantsFont.documentTitleMPFont, ConstantsFont.documentSubtitleMPFont);

            int numChapter = 1;
            int countSections = 1;

            countSections = MultilanguageAnonymousResultExportPdfSections.createIntroductionChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont);
            numChapter++;
            //countSections = MultilanguageAnonymousResultExportPdfSections.createObjetiveChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, observatoryType);
            //numChapter++;
            countSections = MultilanguageAnonymousResultExportPdfSections.createMethodologyChapter(request, index, document, countSections, numChapter, ConstantsFont.chapterTitleMPFont, analysisList, observatoryType, isBasicService);
            numChapter++;

            countSections = addObservatoryScoreSummary(request, document, index, analysisList, numChapter, countSections, file);
            numChapter++;

            int counter = 1;
            for (AnalysisForm analysisForm : analysisList) {
                String evaluationTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.score.by.page.label", counter);
                Chapter chapter = PDFUtils.addChapterTitle(evaluationTitle, index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);

                Phrase p1 = PDFUtils.createPhrase(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.url") + ": ", ConstantsFont.scoreBoldFont);
                Phrase p2 = PDFUtils.createPhraseLink(analysisForm.getUrl(), analysisForm.getUrl(), ConstantsFont.scoreFont);
                Paragraph p = new Paragraph();
                p.add(p1);
                p.add(p2);
                p.setSpacingBefore(ConstantsFont.SPACE_LINE);
                chapter.add(p);

                chapter.add(createTable(analysisForm));

                document.add(chapter);
                numChapter++;
                counter++;
            }

            //Ponemos la variable a true para que no se escriba el footer en el índice
            IndexUtils.createIndex(writer, document, request, index, false, ConstantsFont.chapterTitleMPFont);
            ExportPageEventsObservatoryMultilanguage.setLastPage(false);
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

    private static PdfPTable createTable(AnalysisForm analysisForm) {
        PropertiesManager pmgr = new PropertiesManager();
        float[] widths = {0.1f, 0.4f, 0.2f, 0.2f, 0.1f};
        PdfPTable table = new PdfPTable(widths);

        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        table.addCell(PDFUtils.createTableCell("Lengua", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell("Destino", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell("Lenguaje declarado", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell("Lenguaje traducido", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell("Correcto", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (LanguageFoundForm languageFoundForm : analysisForm.getLanguagesFound()) {
            table.addCell(PDFUtils.createTableCell(languageFoundForm.getLanguage().getName(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));

            PdfPCell url = new PdfPCell(PDFUtils.addLinkParagraph(languageFoundForm.getHref(), languageFoundForm.getHref(), ConstantsFont.noteCellFont));
            table.addCell(url);

            table.addCell(PDFUtils.createTableCell(languageFoundForm.getDeclarationLang(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            if (languageFoundForm.getLanguageSuspected() != null) {
                table.addCell(PDFUtils.createTableCell(languageFoundForm.getLanguageSuspected().getName(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            } else {
                table.addCell(PDFUtils.createTableCell("", Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            }

            Image image = null;

            try {
                if (languageFoundForm.isCorrect()) {
                    image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.green"));
                } else {
                    image = Image.getInstance(pmgr.getValue("pdf.properties", "path.mode.red"));
                }
                image.scalePercent(72);
            } catch (Exception e) {
                Logger.putLog("Error al crear tabla, resultados primarios.", PrimaryExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
            }

            PdfPCell labelCell = new PdfPCell();
            labelCell.addElement(PDFUtils.createImageTextParagraph(image, null, ConstantsFont.descriptionFontRed));
            labelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(labelCell);
        }

        return table;
    }

    private static int addObservatoryScoreSummary(HttpServletRequest request, Document document, IndexEvents index, List<AnalysisForm> analysisList, int numChapter, int countSections, File file) throws Exception {
        List<SiteTranslationInformationForm> infoFormList = MultilanguageUtils.getPortalTraductionInformation(analysisList, true);
        PropertiesManager pmgr = new PropertiesManager();
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.resultados.resumen").toUpperCase(), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);
        chapter.add(Chunk.NEWLINE);

        String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global");
        Section section1 = PDFUtils.addSection(title, index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test.jpg";
        List<LabelValueBean> languagesGraphicResults = ResultadosAnonimosObservatorioMultilanguageUtils.getLanguagesGraphic(request, title, filePath, analysisList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
        Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section1.add(image);
        section1.add(createResultsTable2Columns(request, languagesGraphicResults));
        section1.newPage();

        title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked");
        Section section2 = PDFUtils.addSection(title, index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test2.jpg";
        List<ValidationLanguageForm> languagesValidity = ResultadosAnonimosObservatorioMultilanguageUtils.getLanguagesValidityGraphic(request, title, filePath, infoFormList, Constants.MULTILANGUAGE_TOTAL_VALIDATION);
        image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section2.add(image);
        section2.add(createResultsTable3Columns(request, languagesValidity));
        section2.newPage();

        title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.declaration");
        Section section3 = PDFUtils.addSection(title, index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test3.jpg";
        languagesValidity = ResultadosAnonimosObservatorioMultilanguageUtils.getLanguagesValidityGraphic(request, title, filePath, infoFormList, Constants.MULTILANGUAGE_DECLARATION_VALIDATION);
        image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section3.add(image);
        section3.add(createResultsTable3Columns(request, languagesValidity));
        section3.newPage();

        title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.multilanguage.global.stacked.translation");

        filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test4.jpg";
        Section section4 = PDFUtils.addSection(title, index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        languagesValidity = ResultadosAnonimosObservatorioMultilanguageUtils.getLanguagesValidityGraphic(request, title, filePath, infoFormList, Constants.MULTILANGUAGE_TRANSLATION_VALIDATION);
        image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        image.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section4.add(image);
        section4.add(createResultsTable3Columns(request, languagesValidity));
        section4.newPage();
        document.add(chapter);

        return countSections;
    }

    private static PdfPTable createResultsTable3Columns(HttpServletRequest request, List<ValidationLanguageForm> languagesValidity) {
        float[] widths2 = {2f, 4f, 4f};
        PdfPTable table = new PdfPTable(widths2);
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.language"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.porc.paginas.valid"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.porc.paginas.invalid"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        for (ValidationLanguageForm validity : languagesValidity) {
            table.addCell(PDFUtils.createTableCell(validity.getLanguage(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(validity.getGreenPercentage(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(validity.getRedPercentage(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);

        return table;
    }

    private static PdfPTable createResultsTable2Columns(HttpServletRequest request, List<LabelValueBean> results) {
        float[] widths = {5f, 5f};
        PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.language"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        for (LabelValueBean lvb : results) {
            table.addCell(PDFUtils.createTableCell(lvb.getLabel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(lvb.getValue(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);

        return table;
    }
}
