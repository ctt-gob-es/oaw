package es.inteco.rastreador2.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.pdf.PdfPTable;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.CrawlerUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class MultilanguageAnonymousResultExportPdfSection3 {

    private MultilanguageAnonymousResultExportPdfSection3() {
    }

    protected static void createSection31(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p1.AGE"), ConstantsFont.PARAGRAPH, section);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p2.AGE"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p3.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p4.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p5.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p6.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p7.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p8.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p9.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p10.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p11.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    protected static void createSection32(HttpServletRequest request, Section section, java.util.List<AnalysisForm> analysisList, long observatoryType, boolean isBasicService) {
        ArrayList<String> boldWords = new ArrayList<>();
        if (!isBasicService) {
            boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1.bold"));
            section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p3"), ConstantsFont.PARAGRAPH, section);
        } else {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p3.bs"), ConstantsFont.PARAGRAPH, section);
        }

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p4"), ConstantsFont.PARAGRAPH, section);

        List list = new List();
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p5.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p6.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p7"), ConstantsFont.PARAGRAPH, section);

        PropertiesManager pmgr = new PropertiesManager();
        if (!isBasicService) {
            section.newPage();
        }
        Image imgGr = PDFUtils.createImage(pmgr.getValue("pdf.properties", "path.grafico.rastreo"), CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.img.alt"));
        imgGr.scalePercent(60);
        imgGr.setAlignment(Image.ALIGN_CENTER);
        section.add(imgGr);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p10"), ConstantsFont.PARAGRAPH, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p11"), ConstantsFont.PARAGRAPH, section);

        section.newPage();
        if (analysisList != null && !analysisList.isEmpty()) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p9"), ConstantsFont.PARAGRAPH, section);
            section.add(addURLTable(request, analysisList));
            section.newPage();
        }
    }

    protected static void createSection33(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.33.p1"), ConstantsFont.PARAGRAPH, section);

        List list = new List();
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.33.p2"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.33.p3"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.33.p4"), list, ConstantsFont.PARAGRAPH);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.33.p5"), ConstantsFont.PARAGRAPH, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.33.p6"), ConstantsFont.PARAGRAPH, section);
    }

    protected static void createSection331(HttpServletRequest request, Section section) {
        ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p1.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p2"), ConstantsFont.PARAGRAPH, section);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p3"), ConstantsFont.PARAGRAPH, section);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p4"), ConstantsFont.PARAGRAPH, section);

        List list = new List();

        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p5.bold"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p6.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p7.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p8.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p8"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p9.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p10.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p11.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p12"), ConstantsFont.PARAGRAPH, section);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.331.p13"), ConstantsFont.PARAGRAPH, section);
    }

    protected static void createSection332(HttpServletRequest request, Section section) {
        ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p1.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p2"), ConstantsFont.PARAGRAPH, section);

        List list = new List();

        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p3.bold"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p4.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p4"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p5.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p6.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p7.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p8.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p8"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);
        boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p9.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p10"), ConstantsFont.PARAGRAPH, section);

        list = new List();
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p11"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p12"), list, ConstantsFont.PARAGRAPH);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
        SpecialChunk anchor = new SpecialChunk("http://www.rfc-editor.org/rfc/bcp/bcp47.txt", "http://www.rfc-editor.org/rfc/bcp/bcp47.txt", true, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p13"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p14"), ConstantsFont.PARAGRAPH, section);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p15"), ConstantsFont.paragraphAnchorFont, section);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.332.p16"), ConstantsFont.paragraphAnchorFont, section);
    }

    protected static void createSection333(HttpServletRequest request, Section section) {
        ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p1.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p2"), ConstantsFont.PARAGRAPH, section);

        Paragraph p = new Paragraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p3.1"), ConstantsFont.PARAGRAPH);
        p.setSpacingBefore(ConstantsFont.LINE_SPACE);
        p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        Phrase ph = new Phrase(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p3.2"), ConstantsFont.paragraphUnderlinedFont);
        p.add(ph);
        ph = new Phrase(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p3.3"), ConstantsFont.PARAGRAPH);
        p.add(ph);
        section.add(p);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p4"), ConstantsFont.PARAGRAPH, section);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p5"), ConstantsFont.PARAGRAPH, section);

        List list = new List();
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p6"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p7"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p8"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p9"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p10"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p11"), list, ConstantsFont.PARAGRAPH);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p12"), list, ConstantsFont.PARAGRAPH);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.333.p13"), ConstantsFont.PARAGRAPH, section);
    }

    private static PdfPTable addURLTable(HttpServletRequest request, java.util.List<AnalysisForm> analysisList) {
        float[] widths = {30f, 70f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setWidthPercentage(100);

        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        int i = 1;
        for (AnalysisForm analysisForm : analysisList) {
            table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("observatory.graphic.score.by.page.label", i++), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
            table.addCell(PDFUtils.createLinkedTableCell(analysisForm.getUrl(), analysisForm.getUrl(),Color.WHITE, Element.ALIGN_LEFT, 0));
        }

        return table;
    }
}
