package es.inteco.rastreador2.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class AnonymousResultExportPdfSection3 {

    private AnonymousResultExportPdfSection3() {
    }

    protected static void createChapter3(HttpServletRequest request, Chapter chapter) {
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.3.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.3.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.3.p2"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.3.p3"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.3.p4"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.3.p5"), ConstantsFont.paragraphFont, chapter);
    }

    protected static void createSection31AGE(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p1.AGE"), ConstantsFont.paragraphFont, section);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p2.AGE"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p3.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p4.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p5.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p6.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p7.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p8.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p9.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p10.AGE"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p11.AGE"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    protected static void createSection31CCAA(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p1.CCAA"), ConstantsFont.paragraphFont, section);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p2.CCAA"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p3.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p4.CCAA"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p5.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p6.CCAA"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p7.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p8.CCAA"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p9.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p10.CCAA"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p11.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p12.CCAA"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p13.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p14.CCAA"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p15.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p16.CCAA"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p17.CCAA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    protected static void createSection31EELL(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p1.EELL"), ConstantsFont.paragraphFont, section);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p2.EELL"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p3.EELL"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p4.EELL"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p5.EELL"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p6.EELL"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p7.EELL"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p8.EELL"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p9.EELL"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    protected static void createSection31PRENSA(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.31.p1.PRENSA"), ConstantsFont.paragraphFont, section);
    }

    protected static void createSection32(HttpServletRequest request, Section section, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) {
        ArrayList<String> boldWords = new ArrayList<String>();
        if (!isBasicService) {
            boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1.bold"));
            section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p3"), ConstantsFont.paragraphFont, section);
        } else {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p3.bs"), ConstantsFont.paragraphFont, section);
        }

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p4"), ConstantsFont.paragraphFont, section);

        List list = new List();
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p5.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p6.bold"));
        list.add(PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p6"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p7"), ConstantsFont.paragraphFont, section);

        PropertiesManager pmgr = new PropertiesManager();
        if (!isBasicService) {
            section.newPage();
        }

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.grafico.rastreo"), CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.img.alt"), 60);

        if (!isBasicService && observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8"), ConstantsFont.paragraphFont, section);
            List listp8 = new List();
            PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8.l1"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8.l2"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8.l3"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8.l4"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8.l5"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8.l6"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p8.l7"), listp8, ConstantsFont.paragraphFont);
            listp8.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
            section.add(listp8);
        }

        if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p10"), ConstantsFont.paragraphFont, section);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p11"), ConstantsFont.paragraphFont, section);
        }

        section.newPage();
        if (primaryReportPageList != null) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p9"), ConstantsFont.paragraphFont, section);
            section.add(addURLTable(request, primaryReportPageList));
            section.newPage();
        }
    }

    protected static void createSection32PRENSA(HttpServletRequest request, Section section, java.util.List<ObservatoryEvaluationForm> primaryReportPageList) {
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1.PRENSA.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1.PRENSA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        section.newPage();
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p2.PRENSA"), ConstantsFont.paragraphFont, section);
        section.add(addURLTable(request, primaryReportPageList));
        section.newPage();
    }

    protected static Section createSection33(HttpServletRequest request, Section section, long observatoryType) throws BadElementException, IOException {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p1"), ConstantsFont.paragraphFont, section);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);

        List list2 = new List();
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p4"));
        ListItem itemL2 = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p6"));
        itemL2 = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        item.add(list2);

        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p8"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);

        list2 = new List();
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p10"));
        itemL2 = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p12"));
        itemL2 = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        item.add(list2);

        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p14"), ConstantsFont.paragraphFont, section);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p15"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p17"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p18"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p19"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p20"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p21"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p23"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p25"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p27.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p28"), ConstantsFont.paragraphFont, section);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p29.bold1"));
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p29.bold2"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p30.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p30"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        if (observatoryType == Constants.OBSERVATORY_TYPE_PRENSA) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p1.PRENSA"), ConstantsFont.paragraphFont, section);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p2.PRENSA"), ConstantsFont.paragraphFont, section);

            list = new List();

            boldWords = new ArrayList<String>();
            boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p3.PRENSA.bold"));
            item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p3.PRENSA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
            list.add(item);
            boldWords = new ArrayList<String>();
            boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p4.PRENSA.bold"));
            item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p4.PRENSA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
            list.add(item);
            boldWords = new ArrayList<String>();
            boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p5.PRENSA.bold"));
            item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p5.PRENSA"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
            list.add(item);

            list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
            section.add(list);

            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p6.PRENSA"), ConstantsFont.paragraphFont, section);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p7.PRENSA"), ConstantsFont.paragraphFont, section);

            Paragraph p = PDFUtils.addLinkParagraph("http://www.longtailvideo.com/support/jw-player/22/making-video-accessible", "http://www.longtailvideo.com/support/jw-player/22/making-video-accessible", ConstantsFont.paragraphAnchorFont);
            p.setSpacingBefore(ConstantsFont.SPACE_LINE);
            section.add(p);
        }

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p31"), ConstantsFont.paragraphFont, section);

        section.newPage();

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.ppalTitle1"), section, 400);
        section.add(createMethodologyTable1(request));
        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.ppalTitle2"), section, 400);
        section.add(createMethodologyTable2(request));

        return section;
    }

    protected static void createSection331(HttpServletRequest request, Section section) throws BadElementException, IOException {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.331.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.331.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.331.table.title"), section, 450);
        section.add(create331Table(request));
    }

    protected static void createSection34(HttpServletRequest request, Section section) {
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p1.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p3"), ConstantsFont.paragraphFont, section);
    }

    protected static void createSection341(HttpServletRequest request, Section section) {
        PropertiesManager pmgr = new PropertiesManager();

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p1.bold"), CrawlerUtils.getResources(request).getMessage("anchor.PMP"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p1"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMP.png", "PMP = SRV/VP*10", 80);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p4"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p6"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        section.newPage();

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p8.bold"), CrawlerUtils.getResources(request).getMessage("anchor.PMPO"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p8"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMPO.png", "PMPO = SPMP/NP", 80);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p9"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p11"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p12"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p13"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p15.bold"), CrawlerUtils.getResources(request).getMessage("anchor.PMV"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p15"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMV.png", "PMV = SR/PP*10", 80);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p16"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p17"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p18"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p19"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p20"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p21"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p22.bold"), CrawlerUtils.getResources(request).getMessage("anchor.PMNA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p22"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMNA.png", " PMNA= SPMVN/VN", 80);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p23"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p25"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p27"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p28"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p29.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.341.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
    }

    protected static void createSection342(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p2"), ConstantsFont.paragraphFont, section);

        List list = new List();
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p3"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p4"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p5"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p6"), list, ConstantsFont.paragraphFont, false, true);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p7.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p8.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p8"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        list = new List();
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p9.bold"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p10.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p11.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p12.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p12"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        list = new List();
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p13.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p14.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p15.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p15"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p16.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p17"), ConstantsFont.paragraphFont, section);

        list = new List();
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p18"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p19"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p20"), list, ConstantsFont.paragraphFont, false, true);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p21"), ConstantsFont.paragraphFont, section);

        PropertiesManager pmgr = new PropertiesManager();
        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "VNP.png", "VNP = SP/NP", 80);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p22.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p23.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p24.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p25"), ConstantsFont.paragraphFont, section);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p26.bold1"));
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p26.bold2"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p27.bold1"));
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p27.bold2"));
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p27.bold3"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p28.bold1"));
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p28.bold2"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.342.p28"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    protected static void createSection343(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p1"), ConstantsFont.paragraphFont, section);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p8"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p6"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p10"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p4"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p15"), ConstantsFont.paragraphFont, section);
        PdfPTable table = createVerificationTable(CrawlerUtils.getResources(request));
        table.setSpacingBefore(3 * ConstantsFont.SPACE_LINE);
        section.add(table);
        section.newPage();

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p16"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p17"), ConstantsFont.paragraphFont, section);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p18.bold"), CrawlerUtils.getResources(request).getMessage("anchor.PMPA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p18"), anchorMap, ConstantsFont.paragraphFont));

        PropertiesManager pmgr = new PropertiesManager();
        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMAP.png", "PMAP = SPMVA/VA", 75);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p19.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p19"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p20.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p20"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p21.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p21"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p22.bold"), CrawlerUtils.getResources(request).getMessage("anchor.PMA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p22"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMA.png", "PMA = SPMA/NP", 75);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p23.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p24.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p25.bold"));
        item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p25"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

    }

    protected static PdfPTable createVerificationTable(final MessageResources messageResources) {

        try {
            float[] widths = {0.15f, 0.65f, 0.20f};
            PdfPTable table = new PdfPTable(widths);
            int margin = 10;

            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.header1", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.header2", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.header3", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

            table.addCell(PDFUtils.createColSpanTableCell(messageResources, "ob.resAnon.intav.report.46.table.2header1", Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification111", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification111.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.alt", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification112", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification112.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.alt", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification113", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification113.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.alt", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification114", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification114.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.nav", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification121", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification121.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.gen", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification122", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification122.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.pre", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification123", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification123.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.est", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification124", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification124.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.gen", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification125", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification125.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.est", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification126", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification126.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.nav", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));

            table.addCell(PDFUtils.createColSpanTableCell(messageResources, "ob.resAnon.intav.report.46.table.2header2", Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification211", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification211.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.est", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification212", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification212.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.gen", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification213", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification213.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.nav", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification214", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification214.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.gen", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification221", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification221.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.est", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification222", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification222.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.pre", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification223", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification223.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.gen", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification224", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification224.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.nav", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification225", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification225.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.nav", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification226", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.verification226.name", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.aspect.est", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));

            table.setSpacingAfter(ConstantsFont.SPACE_LINE);

            return table;
        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 4.5", AnonymousResultExportPdfSection4.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

    private static PdfPTable create331Table(HttpServletRequest request) {
        float[] widths = {40f, 30f, 30f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 3);
        table.setWidthPercentage(100);
        create331HaderTable(request, table);
        create331TableRow(request, table, "inteco.observatory.subgroup.1.1.1", createTextList(request, "ob.resAnon.intav.report.331.table.req.111"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.111"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.1.2", createTextList(request, "ob.resAnon.intav.report.331.table.req.112"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.112"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.1.3", createTextList(request, "ob.resAnon.intav.report.331.table.req.113"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.113"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.1.4", createTextList(request, "ob.resAnon.intav.report.331.table.req.114"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.114"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.2.1", createTextList(request, "ob.resAnon.intav.report.331.table.req.121"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.121"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.2.2", createTextList(request, "ob.resAnon.intav.report.331.table.req.122"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.122"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.2.3", createTextList(request, "ob.resAnon.intav.report.331.table.req.123"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.123"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.2.4", createTextList(request, "ob.resAnon.intav.report.331.table.req.124"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.124"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.2.5", createTextList(request, "ob.resAnon.intav.report.331.table.req.125"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.125"));
        create331TableRow(request, table, "inteco.observatory.subgroup.1.2.6", createTextList(request, "ob.resAnon.intav.report.331.table.req.126"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.126"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.1.1", createTextList(request, "ob.resAnon.intav.report.331.table.req.211"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.211"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.1.2", createTextList(request, "ob.resAnon.intav.report.331.table.req.212"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.212"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.1.3", createTextList(request, "ob.resAnon.intav.report.331.table.req.213"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.213"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.1.4", createTextList(request, "ob.resAnon.intav.report.331.table.req.214"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.214"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.2.1", createTextList(request, "ob.resAnon.intav.report.331.table.req.221"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.221"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.2.2", createTextList(request, "ob.resAnon.intav.report.331.table.req.222"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.222"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.2.3", createTextList(request, "ob.resAnon.intav.report.331.table.req.223"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.223"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.2.4", createTextList(request, "ob.resAnon.intav.report.331.table.req.224"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.224"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.2.5", createTextList(request, "ob.resAnon.intav.report.331.table.req.225"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.225"));
        create331TableRow(request, table, "inteco.observatory.subgroup.2.2.6", createTextList(request, "ob.resAnon.intav.report.331.table.req.226"), createTextList(request, "ob.resAnon.intav.report.331.table.verP.226"));

        return table;
    }

    private static void create331HaderTable(HttpServletRequest request, PdfPTable table) {
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.331.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.331.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.331.table.header3"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    private static void create331TableRow(HttpServletRequest request, PdfPTable table, String verification, List req, List verP) {
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(verification), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 10, -1));
        table.addCell(PDFUtils.createListTableCell(req, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(verP, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    private static PdfPTable createMethodologyTable1(HttpServletRequest request) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 3);
        table.setWidthPercentage(100);

        try {
            createMethodologyHaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.111", "ob.resAnon.intav.report.33.table.111.name", "ob.resAnon.intav.report.33.table.111.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.111.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.111.value"), createImageList(request, "ob.resAnon.intav.report.33.table.111.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.112", "ob.resAnon.intav.report.33.table.112.name", "ob.resAnon.intav.report.33.table.112.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.112.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.112.value"), createImageList(request, "ob.resAnon.intav.report.33.table.112.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.113", "ob.resAnon.intav.report.33.table.113.name", "ob.resAnon.intav.report.33.table.113.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.113.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.113.value"), createImageList(request, "ob.resAnon.intav.report.33.table.113.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.114", "ob.resAnon.intav.report.33.table.114.name", "ob.resAnon.intav.report.33.table.114.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.114.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.114.value"), createImageList(request, "ob.resAnon.intav.report.33.table.114.modality"));

            createMethodologyHaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.121", "ob.resAnon.intav.report.33.table.121.name", "ob.resAnon.intav.report.33.table.121.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.121.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.121.value"), createImageList(request, "ob.resAnon.intav.report.33.table.121.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.122", "ob.resAnon.intav.report.33.table.122.name", "ob.resAnon.intav.report.33.table.122.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.122.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.122.value"), createImageList(request, "ob.resAnon.intav.report.33.table.122.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.123", "ob.resAnon.intav.report.33.table.123.name", "ob.resAnon.intav.report.33.table.123.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.123.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.123.value"), createImageList(request, "ob.resAnon.intav.report.33.table.123.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.124", "ob.resAnon.intav.report.33.table.124.name", "ob.resAnon.intav.report.33.table.124.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.124.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.124.value"), createImageList(request, "ob.resAnon.intav.report.33.table.124.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.125", "ob.resAnon.intav.report.33.table.125.name", "ob.resAnon.intav.report.33.table.125.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.125.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.125.value"), createImageList(request, "ob.resAnon.intav.report.33.table.125.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.126", "ob.resAnon.intav.report.33.table.126.name", "ob.resAnon.intav.report.33.table.126.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.126.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.126.value"), createImageList(request, "ob.resAnon.intav.report.33.table.126.modality"));

        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdfSection3.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    private static PdfPTable createMethodologyTable2(HttpServletRequest request) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 3);
        table.setWidthPercentage(100);

        try {
            createMethodologyHaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.211", "ob.resAnon.intav.report.33.table.211.name", "ob.resAnon.intav.report.33.table.211.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.211.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.211.value"), createImageList(request, "ob.resAnon.intav.report.33.table.211.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.212", "ob.resAnon.intav.report.33.table.212.name", "ob.resAnon.intav.report.33.table.212.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.212.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.212.value"), createImageList(request, "ob.resAnon.intav.report.33.table.212.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.213", "ob.resAnon.intav.report.33.table.213.name", "ob.resAnon.intav.report.33.table.213.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.213.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.213.value"), createImageList(request, "ob.resAnon.intav.report.33.table.213.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.214", "ob.resAnon.intav.report.33.table.214.name", "ob.resAnon.intav.report.33.table.214.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.214.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.214.value"), createImageList(request, "ob.resAnon.intav.report.33.table.214.modality"));

            createMethodologyHaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.221", "ob.resAnon.intav.report.33.table.221.name", "ob.resAnon.intav.report.33.table.221.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.221.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.221.value"), createImageList(request, "ob.resAnon.intav.report.33.table.221.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.222", "ob.resAnon.intav.report.33.table.222.name", "ob.resAnon.intav.report.33.table.222.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.222.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.222.value"), createImageList(request, "ob.resAnon.intav.report.33.table.222.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.223", "ob.resAnon.intav.report.33.table.223.name", "ob.resAnon.intav.report.33.table.223.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.223.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.223.value"), createImageList(request, "ob.resAnon.intav.report.33.table.223.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.224", "ob.resAnon.intav.report.33.table.224.name", "ob.resAnon.intav.report.33.table.224.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.224.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.224.value"), createImageList(request, "ob.resAnon.intav.report.33.table.224.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.225", "ob.resAnon.intav.report.33.table.225.name", "ob.resAnon.intav.report.33.table.225.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.225.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.225.value"), createImageList(request, "ob.resAnon.intav.report.33.table.225.modality"));
            createMethodologyTableRow(request, table, "ob.resAnon.intav.report.33.table.id.226", "ob.resAnon.intav.report.33.table.226.name", "ob.resAnon.intav.report.33.table.226.question",
                    createTextList(request, "ob.resAnon.intav.report.33.table.226.answer"), createTextList(request, "ob.resAnon.intav.report.33.table.226.value"), createImageList(request, "ob.resAnon.intav.report.33.table.226.modality"));

        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdfSection3.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    private static void createMethodologyHaderTable(HttpServletRequest request, PdfPTable table, String title) {
        table.addCell(PDFUtils.createColSpanTableCell(title, Constants.VERDE_C_MP, ConstantsFont.labelCellFont, 6, Element.ALIGN_LEFT));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header1"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header2"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header3"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header4"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header5"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header6"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    private static void createMethodologyTableRow(HttpServletRequest request, PdfPTable table, String id, String name, String question, List answer, List value, List modality) {
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(id), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(name), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(question), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createListTableCell(answer, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(value, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(modality, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    private static List createTextList(HttpServletRequest request, String text) {
        return createTextList(request, text, Element.ALIGN_CENTER);
    }

    private static List createTextList(HttpServletRequest request, String text, int align) {
        java.util.List<String> list = Arrays.asList(CrawlerUtils.getResources(request).getMessage(text).split(";"));
        List PDFlist = new List();
        for (String str : list) {
            PDFUtils.addListItem(str, PDFlist, ConstantsFont.noteCellFont, false, false, align);
        }
        if (align == Element.ALIGN_LEFT) {
            PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE / 5);
        }

        return PDFlist;
    }

    private static List createImageList(HttpServletRequest request, String text) {
        PropertiesManager pmgr = new PropertiesManager();
        Image image;
        java.util.List<String> list = Arrays.asList(CrawlerUtils.getResources(request).getMessage(text).split(";"));
        List PDFlist = new List();
        for (String str : list) {
            if (str.equals("0")) {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.red"), CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.modality.0.alt"));
            } else {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.green"), CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.modality.1.alt"));
            }
            image.scalePercent(65);
            ListItem item = new ListItem(new Chunk(image, 0, 0));
            item.setListSymbol(new Chunk(""));
            PDFlist.add(item);
        }
        PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        return PDFlist;
    }

    private static PdfPTable addURLTable(HttpServletRequest request, java.util.List<ObservatoryEvaluationForm> primaryReportPageList) {
        float[] widths = {30f, 70f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.setWidthPercentage(100);

        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        int i = 1;
        for (ObservatoryEvaluationForm page : primaryReportPageList) {
            table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("observatory.graphic.score.by.page.label", i++), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
            PdfPCell url = new PdfPCell(PDFUtils.addLinkParagraph(page.getUrl(), page.getUrl(), ConstantsFont.noteCellFont));
            table.addCell(url);
        }

        return table;
    }

}
