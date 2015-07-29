package es.inteco.rastreador2.pdf.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class AnonymousResultExportPdfUNE2012 extends AnonymousResultExportPdf {

    @Override
    public int createIntroductionChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, boolean isBasicService) throws Exception {
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter1.title"), index, countSections++, numChapter, titleFont);
        createChapter1(request, chapter, isBasicService);

        Section section1 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter11.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection11(request, section1);

        document.add(chapter);

        return countSections;
    }

    protected void createChapter1(HttpServletRequest request, Chapter chapter, boolean isBasicService) {
        final MessageResources resources = CrawlerUtils.getResources(request);
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.1.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.1.p2.bold"));
        Paragraph p = PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p2"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        Phrase ph = new Phrase(resources.getMessage("ob.resAnon.intav.report.1.p2.m1"), ConstantsFont.paragraphUnderlinedFont);
        p.add(ph);
        chapter.add(p);
        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.1.p3.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        if ( isBasicService) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.1.p4"), ConstantsFont.paragraphFont, chapter);
        }
    }

    protected void createSection11(HttpServletRequest request, Section section) {
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.11.p4.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.11.p4"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.11.p5"), ConstantsFont.paragraphFont, section);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.11.p1.bold"));
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.11.p1"),ConstantsFont.paragraphFont, section);
    }

    @Override
    public int createObjetiveChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, long observatoryType) throws DocumentException {
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter2.title"), index, countSections++, numChapter, titleFont);
        createChapter2(request, chapter, observatoryType);
        document.add(chapter);

        return countSections;
    }

    protected void createChapter2(HttpServletRequest request, Chapter chapter, long observatoryType) {
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.AGE"), ConstantsFont.paragraphFont, chapter);
        } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p4.CCAA"), ConstantsFont.paragraphFont, chapter);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.CCAA"), ConstantsFont.paragraphFont, chapter);
        }
    }

    @Override
    public int createMethodologyChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) throws Exception {
        final MessageResources resources = CrawlerUtils.getResources(request);
        Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.chapter3.title"), index, countSections++, numChapter, titleFont);

        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.3.p1"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.3.p1.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.3.p2"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("une2012.resAnon.intav.report.3.p3"), ConstantsFont.paragraphFont, chapter);

        if (!isBasicService) {
            Section section1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter31.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
            if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
                createSection31(resources, section1, observatoryType, "AGE");
            } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
                createSection31(resources, section1, observatoryType, "CCAA");
            } else if (observatoryType == Constants.OBSERVATORY_TYPE_EELL) {
                createSection31(resources, section1, observatoryType, "EELL");
            }
        }

        Section section2 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter32.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection32(request, section2, primaryReportPageList, observatoryType, isBasicService);

        Section section3 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter33.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1, CrawlerUtils.getResources(request).getMessage("anchor.met.table"));
        createSection33(request, section3, observatoryType);
        Section section31 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.chapter331.title"), index, ConstantsFont.chapterTitleMPFont3L, section3, countSections++, 2);
        createSection331(request, section31);
        Section section4 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter34.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection34(request, section4);
        Section section41 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter341.title"), index, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
        createSection341(request, section41);
        Section section42 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter342.title"), index, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
        createSection342(request, section42);
        // Solo sale en el agregado
        if (primaryReportPageList == null) {
            Section section43 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter343.title"), index, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
            createSection343(request, section43);
        }

        document.add(chapter);
        return countSections;
    }

    private void createSection31(final MessageResources resources, final Section section, final long observatoryType, final String variante) {
        final com.lowagie.text.List list = new com.lowagie.text.List();
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.31.p1." + variante), ConstantsFont.paragraphFont, section);

        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p3." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p2." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p5." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p4." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p7." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p6." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p9." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p8." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
            list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p11." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p10." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

            if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p13." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p12." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p15." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p14." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p17." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p16." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
            }
        }

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    protected void createSection32(HttpServletRequest request, Section section, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) {
        ArrayList<String> boldWords = new ArrayList<String>();
        if (!isBasicService) {
            boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1.bold"));
            section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p3"), ConstantsFont.paragraphFont, section);
        } else {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p3.bs"), ConstantsFont.paragraphFont, section);
        }

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.32.p4"), ConstantsFont.paragraphFont, section);

        com.lowagie.text.List list = new com.lowagie.text.List();
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
            com.lowagie.text.List listp8 = new com.lowagie.text.List();
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

    protected Section createSection33(HttpServletRequest request, Section section, long observatoryType) throws BadElementException, IOException {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p1"), ConstantsFont.paragraphFont, section);

        com.lowagie.text.List list = new com.lowagie.text.List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);

        com.lowagie.text.List list2 = new com.lowagie.text.List();
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

        list2 = new com.lowagie.text.List();
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p10"));
        itemL2 = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.33.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p12"));
        itemL2 = PDFUtils.addMixFormatListItem(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.33.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        item.add(list2);

        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p14"), ConstantsFont.paragraphFont, section);

        list = new com.lowagie.text.List();

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
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.33.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p30.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.33.p30"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.p31"), ConstantsFont.paragraphFont, section);

        section.newPage();

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.ppalTitle1"), section, 400);
        section.add(createMethodologyTable1(request));

        section.newPage();

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.ppalTitle2"), section, 400);
        section.add(createMethodologyTable2(request));

        return section;
    }

    protected PdfPTable createMethodologyTable1(HttpServletRequest request) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 3);
        table.setWidthPercentage(100);

        try {
            createMethodologyHeaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.111", "une2012.resAnon.intav.report.33.table.111.name", "une2012.resAnon.intav.report.33.table.111.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.111.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.111.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.111.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.112", "une2012.resAnon.intav.report.33.table.112.name", "une2012.resAnon.intav.report.33.table.112.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.112.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.112.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.112.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.113", "une2012.resAnon.intav.report.33.table.113.name", "une2012.resAnon.intav.report.33.table.113.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.113.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.113.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.113.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.114", "une2012.resAnon.intav.report.33.table.114.name", "une2012.resAnon.intav.report.33.table.114.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.114.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.114.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.114.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.115", "une2012.resAnon.intav.report.33.table.115.name", "une2012.resAnon.intav.report.33.table.115.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.115.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.115.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.115.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.116", "une2012.resAnon.intav.report.33.table.116.name", "une2012.resAnon.intav.report.33.table.116.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.116.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.116.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.116.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.117", "une2012.resAnon.intav.report.33.table.117.name", "une2012.resAnon.intav.report.33.table.117.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.117.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.117.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.117.modality"));

            createMethodologyHeaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.121", "une2012.resAnon.intav.report.33.table.121.name", "une2012.resAnon.intav.report.33.table.121.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.121.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.121.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.121.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.122", "une2012.resAnon.intav.report.33.table.122.name", "une2012.resAnon.intav.report.33.table.122.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.122.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.122.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.122.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.123", "une2012.resAnon.intav.report.33.table.123.name", "une2012.resAnon.intav.report.33.table.123.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.123.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.123.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.123.modality"));
        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    protected PdfPTable createMethodologyTable2(HttpServletRequest request) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 3);
        table.setWidthPercentage(100);

        try {
            createMethodologyHeaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.211", "une2012.resAnon.intav.report.33.table.211.name", "une2012.resAnon.intav.report.33.table.211.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.211.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.211.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.211.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.212", "une2012.resAnon.intav.report.33.table.212.name", "une2012.resAnon.intav.report.33.table.212.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.212.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.212.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.212.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.213", "une2012.resAnon.intav.report.33.table.213.name", "une2012.resAnon.intav.report.33.table.213.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.213.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.213.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.213.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.214", "une2012.resAnon.intav.report.33.table.214.name", "une2012.resAnon.intav.report.33.table.214.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.214.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.214.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.214.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.215", "une2012.resAnon.intav.report.33.table.215.name", "une2012.resAnon.intav.report.33.table.215.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.215.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.215.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.215.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.216", "une2012.resAnon.intav.report.33.table.216.name", "une2012.resAnon.intav.report.33.table.216.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.216.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.216.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.216.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.217", "une2012.resAnon.intav.report.33.table.217.name", "une2012.resAnon.intav.report.33.table.217.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.217.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.217.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.217.modality"));

            createMethodologyHeaderTable(request, table, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.221", "une2012.resAnon.intav.report.33.table.221.name", "une2012.resAnon.intav.report.33.table.221.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.221.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.221.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.221.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.222", "une2012.resAnon.intav.report.33.table.222.name", "une2012.resAnon.intav.report.33.table.222.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.222.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.222.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.222.modality"));
            createMethodologyTableRow(request, table, "une2012.resAnon.intav.report.33.table.id.223", "une2012.resAnon.intav.report.33.table.223.name", "une2012.resAnon.intav.report.33.table.223.question",
                    createTextList(request, "une2012.resAnon.intav.report.33.table.223.answer"), createTextList(request, "une2012.resAnon.intav.report.33.table.223.value"), createImageList(request, "une2012.resAnon.intav.report.33.table.223.modality"));

        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdf.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    private void createSection331(HttpServletRequest request, Section section) throws BadElementException, IOException {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.331.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.331.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.331.table.title"), section, 450);
        section.add(create331Table(request));
    }

    private PdfPTable create331Table(HttpServletRequest request) {
        float[] widths = {40f, 40f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 3);
        table.setWidthPercentage(100);
        create331HaderTable(request, table);
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.1.1", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.111"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.1.2", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.112"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.1.3", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.113"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.1.4", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.114"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.1.5", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.115"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.1.6", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.116"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.1.7", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.117"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.2.1", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.121"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.2.2", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.122"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.1.2.3", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.123"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.1.1", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.211"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.1.2", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.212"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.1.3", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.213"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.1.4", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.214"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.1.5", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.215"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.1.6", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.216"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.1.7", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.217"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.2.1", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.221"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.2.2", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.222"));
        create331TableRow(request, table, "minhap.observatory.2_0.subgroup.2.2.3", createTextList(request, "une2012.resAnon.intav.report.331.table.verP.223"));

        return table;
    }

    protected void create331HaderTable(HttpServletRequest request, PdfPTable table) {
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.331.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.331.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    protected void create331TableRow(HttpServletRequest request, PdfPTable table, String verification, com.lowagie.text.List verP) {
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(verification), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 10, -1));
        table.addCell(PDFUtils.createListTableCell(verP, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    protected void createSection34(HttpServletRequest request, Section section) {
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p1.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.34.p3"), ConstantsFont.paragraphFont, section);
    }

    protected void createSection341(HttpServletRequest request, Section section) {
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

    protected void createSection342(HttpServletRequest request, Section section) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.342.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.342.p2"), ConstantsFont.paragraphFont, section);

        List list = new List();
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.342.p3"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.342.p4"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.342.p5"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(CrawlerUtils.getResources(request).getMessage("une2012.resAnon.intav.report.342.p6"), list, ConstantsFont.paragraphFont, false, true);
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

    protected void createSection343(HttpServletRequest request, Section section) {
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
        PdfPTable table = createVerificationTable(request);
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

    @Override
    public int createContentChapter(HttpServletRequest request, Document d, String contents, IndexEvents index, int numChapter, int countSections) throws Exception {
        Chapter chapter = PDFUtils.addChapterTitle(CrawlerUtils.getResources(request).getMessage("basic.service.content.title"), index, countSections++, numChapter, ConstantsFont.chapterTitleMPFont);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.content.p1"), ConstantsFont.paragraphFont, chapter, Element.ALIGN_JUSTIFIED, true, true);
        PDFUtils.addParagraphCode(HTMLEntities.unhtmlAngleBrackets(contents), "", chapter);
        d.add(chapter);

        return countSections;
    }

    @Override
    public void getMidsComparationByVerificationLevelGraphic(HttpServletRequest request, String level, String title, String filePath, String noDataMess, java.util.List<ObservatoryEvaluationForm> evaList, String value, boolean regenerate) throws Exception {
        ResultadosAnonimosObservatorioUNE2012Utils.getMidsComparationByVerificationLevelGraphic(request, level, title, filePath, noDataMess, evaList, value, regenerate);
    }

    @Override
    public ScoreForm generateScores(final HttpServletRequest request, final java.util.List<ObservatoryEvaluationForm> evaList) throws Exception {
        final ScoreForm scoreForm = new ScoreForm();

        int suitabilityGroups = 0;

        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            scoreForm.setTotalScore(scoreForm.getTotalScore().add(evaluationForm.getScore()));

            final String pageSuitabilityLevel = ObservatoryUtils.pageSuitabilityLevel(evaluationForm);
            if (pageSuitabilityLevel.equals(Constants.OBS_AA)) {
                scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(BigDecimal.TEN));
            } else if (pageSuitabilityLevel.equals(Constants.OBS_A)) {
                scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(new BigDecimal(5)));
            }

            for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
                suitabilityGroups = levelForm.getSuitabilityGroups().size();
                if (levelForm.getName().equalsIgnoreCase("priority 1")) {
                    scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().add(levelForm.getScore()));
                } else if (levelForm.getName().equalsIgnoreCase("priority 2")) {
                    scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().add(levelForm.getScore()));
                }
                for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
                    if (suitabilityForm.getName().equalsIgnoreCase("A")) {
                        scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().add(suitabilityForm.getScore()));
                    } else if (suitabilityForm.getName().equalsIgnoreCase("AA")) {
                        scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().add(suitabilityForm.getScore()));
                    }
                }
            }
        }

        final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
        final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
        final java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIVerificationMidsComparison(request, resultL1);
        final java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIIVerificationMidsComparison(request, resultL2);
        scoreForm.setVerifications1(labelsL1);
        scoreForm.setVerifications2(labelsL2);

        if (!evaList.isEmpty()) {
            scoreForm.setTotalScore(scoreForm.getTotalScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
        }

        // El nivel de validación del portal
        scoreForm.setLevel(getValidationLevel(scoreForm, request));

        return scoreForm;
    }

    @Override
    public String getTitle() {
        return "UNE 139803:2012";
    }

    protected String getValidationLevel(final ScoreForm scoreForm, final HttpServletRequest request) {
        if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal(8)) >= 0) {
            return CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.aa");
        } else if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal("3.5")) <= 0) {
            return CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.parcial");
        } else {
            return CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales.a");
        }
    }

}