package es.inteco.rastreador2.pdf.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.pdf.PdfPTable;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSectionCat;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase la generación de los informes anónimos de resultados de la normativa observatorio UNE-2004 en PDF
 */
public class AnonymousResultPdfUNE2004Builder extends AnonymousResultPdfBuilder {
    public AnonymousResultPdfUNE2004Builder(final File fileOut, final long observatoryType) throws Exception {
        super(fileOut, observatoryType);
    }

    @Override
    public void generateGraphics(final HttpServletRequest request, final String filePath) throws Exception {
        ResultadosAnonimosObservatorioIntavUtils.generateGraphics(request, filePath, Constants.MINISTERIO_P, true);
    }

    protected void createIntroductionChapter(final MessageResources resources, final Font titleFont) throws Exception {
        Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.chapter1.title"), index, countSections++, numChapter, titleFont);
        createChapter1(resources, chapter);

        Section section1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter11.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection11(resources, section1);

        document.add(chapter);
    }

    private void createChapter1(final MessageResources resources, final Chapter chapter) {
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p1"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.1.p1.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        Paragraph p = PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p2"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.1.p2.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        p.add(new Phrase(resources.getMessage("ob.resAnon.intav.report.1.p2.m1"), ConstantsFont.paragraphUnderlinedFont));
        chapter.add(p);
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.1.p3"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.1.p3.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.1.p4"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.1.p5"), ConstantsFont.paragraphFont, chapter);
    }

    private void createSection11(final MessageResources resources, final Section section) {
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.11.p4"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.11.p4.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.11.p5"), ConstantsFont.paragraphFont, section);
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.11.p1"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.11.p1.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
    }

    @Override
    protected void createObjetiveChapter(final MessageResources resources, final Font titleFont) throws DocumentException {
        Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.chapter2.title"), index, countSections++, numChapter, titleFont);
        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.2.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.2.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.2.p2"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.2.p3"), ConstantsFont.paragraphFont, chapter);

        if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            //PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.2.p4.AGE"), ConstantsFont.paragraphFont, chapter);
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.2.p5.AGE"), ConstantsFont.paragraphFont, chapter);
        } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.2.p4.CCAA"), ConstantsFont.paragraphFont, chapter);
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.2.p5.CCAA"), ConstantsFont.paragraphFont, chapter);
        }

        document.add(chapter);
    }

    @Override
    protected void createMethodologyChapter(final MessageResources resources, final Font titleFont, final java.util.List<ObservatoryEvaluationForm> primaryReportPageList, final boolean isBasicService) throws Exception {
        Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.chapter3.title"), index, countSections++, numChapter, titleFont);

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.3.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.3.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.3.p2"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.3.p3"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.3.p4"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.3.p5"), ConstantsFont.paragraphFont, chapter);

        if (!isBasicService) {
            Section section1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter31.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
            if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
                createSection31(resources, section1, "AGE");
            } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
                createSection31(resources, section1, "CCAA");
            } else if (observatoryType == Constants.OBSERVATORY_TYPE_EELL) {
                createSection31(resources, section1, "EELL");
            }
        }
        Section section2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter32.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection32(resources, section2, primaryReportPageList, observatoryType, isBasicService);

        Section section3 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter33.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1, resources.getMessage("anchor.met.table"));
        createSection33(resources, section3);
        Section section31 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter331.title"), index, ConstantsFont.chapterTitleMPFont3L, section3, countSections++, 2);
        createSection331(resources, section31);
        Section section4 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter34.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection34(resources, section4);
        Section section41 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter341.title"), index, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
        createSection341(resources, section41);
        Section section42 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter342.title"), index, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
        createSection342(resources, section42);
        // Solo sale en el agregado
        if (primaryReportPageList == null) {
            Section section43 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter343.title"), index, ConstantsFont.chapterTitleMPFont3L, section4, countSections++, 2);
            createSection343(resources, section43);
        }

        document.add(chapter);
    }

    private void createSection31(final MessageResources resources, final Section section, final String variante) {
        final List list = new List();
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.31.p1." + variante), ConstantsFont.paragraphFont, section);

        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p3." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p2" + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p5." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p4" + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p7." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p6" + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p9." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p8" + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
            list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p11." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p10" + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

            if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p13." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p12." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p15." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p14." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p17." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p16." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
            }
        }

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    private void createSection32(final MessageResources resources, final Section section, final java.util.List<ObservatoryEvaluationForm> primaryReportPageList, final long observatoryType, final boolean isBasicService) {
        if (!isBasicService) {
            section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.32.p1"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.32.p1.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p3"), ConstantsFont.paragraphFont, section);
        } else {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p3.bs"), ConstantsFont.paragraphFont, section);
        }

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p4"), ConstantsFont.paragraphFont, section);

        final List list = new List();
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.32.p5"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.32.p5.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.32.p6"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.32.p6.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p7"), ConstantsFont.paragraphFont, section);

        PropertiesManager pmgr = new PropertiesManager();
        if (!isBasicService) {
            section.newPage();
        }

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.grafico.rastreo"), resources.getMessage("ob.resAnon.intav.report.32.img.alt"), 60);

        if (!isBasicService && observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p8"), ConstantsFont.paragraphFont, section);
            final List listp8 = new List();
            PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.32.p8.l1"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.32.p8.l2"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.32.p8.l3"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.32.p8.l4"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.32.p8.l5"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.32.p8.l6"), listp8, ConstantsFont.paragraphFont);
            PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.32.p8.l7"), listp8, ConstantsFont.paragraphFont);
            listp8.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
            section.add(listp8);
        }

        if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p10"), ConstantsFont.paragraphFont, section);
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p11"), ConstantsFont.paragraphFont, section);
        }

        section.newPage();
        if (primaryReportPageList != null) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.32.p9"), ConstantsFont.paragraphFont, section);
            section.add(addURLTable(resources, primaryReportPageList));
            section.newPage();
        }
    }

    private Section createSection33(MessageResources resources, Section section) throws BadElementException, IOException {
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.33.p1"), ConstantsFont.paragraphFont, section);
        List list = new List();
        ListItem item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p3"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p2")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);

        List list2 = new List();
        ListItem itemL2 = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p5"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p4")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        itemL2 = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p7"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p6")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        item.add(list2);

        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p9"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p8")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);

        list2 = new List();
        itemL2 = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p11"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p10")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        itemL2 = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p13"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p12")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list2.add(itemL2);
        list2.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        item.add(list2);

        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.33.p14"), ConstantsFont.paragraphFont, section);

        list = new List();

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p16"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p15")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p18"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p17")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p20"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p19")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p22"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p21")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p24"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p23")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.33.p26"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p25")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.33.p27"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p27.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.33.p28"), ConstantsFont.paragraphFont, section);

        final ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.33.p29.bold1"));
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.33.p29.bold2"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.33.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.33.p30"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.33.p30.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.33.p31"), ConstantsFont.paragraphFont, section);

        section.newPage();

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle1"), section, 400);
        section.add(createMethodologyTable1(resources));
        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.33.table.ppalTitle2"), section, 400);
        section.add(createMethodologyTable2(resources));

        return section;
    }

    private void createSection331(final MessageResources resources, Section section) throws BadElementException, IOException {
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.331.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.331.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.331.table.title"), section, 450);
        section.add(create331Table(resources));
    }

    private void createSection34(final MessageResources resources, Section section) {
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.34.p1"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.34.p1.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.34.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.34.p3"), ConstantsFont.paragraphFont, section);
    }

    private void createSection341(final MessageResources resources, Section section) {
        PropertiesManager pmgr = new PropertiesManager();

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.341.p1.bold"), resources.getMessage("anchor.PMP"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.341.p1"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMP.png", "PMP = SRV/VP*10", 80);

        List list = new List();

        ListItem item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p3"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p2")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p5"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p4")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p7"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p6")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        section.newPage();

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.341.p8.bold"), resources.getMessage("anchor.PMPO"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.341.p8"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMPO.png", "PMPO = SPMP/NP", 80);

        list = new List();

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p10"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p9")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p12"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p11")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p14"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p13")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.341.p15.bold"), resources.getMessage("anchor.PMV"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.341.p15"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMV.png", "PMV = SR/PP*10", 80);

        list = new List();

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p17"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p16")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p19"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p18")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p21"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p20")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.341.p22.bold"), resources.getMessage("anchor.PMNA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.341.p22"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMNA.png", " PMNA= SPMVN/VN", 80);

        list = new List();

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p24"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p23")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p26"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p25")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.341.p28"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p27")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.341.p29"), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.341.p29.bold")), ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));
    }

    private void createSection342(final MessageResources resources, Section section) {
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.342.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.342.p2"), ConstantsFont.paragraphFont, section);

        List list = new List();
        PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.342.p3"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.342.p4"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.342.p5"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.342.p6"), list, ConstantsFont.paragraphFont, false, true);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p7.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.342.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p8.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.342.p8"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        list = new List();
        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p9.bold"));
        ListItem item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p10.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p11.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p12.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.342.p12"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        list = new List();
        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p13.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p13"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p14.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p15.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p15"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p16.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.342.p16"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.342.p17"), ConstantsFont.paragraphFont, section);

        list = new List();
        PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.342.p18"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.342.p19"), list, ConstantsFont.paragraphFont, false, true);
        PDFUtils.addListItem(resources.getMessage("ob.resAnon.intav.report.342.p20"), list, ConstantsFont.paragraphFont, false, true);
        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.342.p21"), ConstantsFont.paragraphFont, section);

        PropertiesManager pmgr = new PropertiesManager();
        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "VNP.png", "VNP = SP/NP", 80);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p22.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p22"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p23.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p24.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.342.p25"), ConstantsFont.paragraphFont, section);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p26.bold1"));
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p26.bold2"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p27.bold1"));
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p27.bold2"));
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p27.bold3"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p27"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p28.bold1"));
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.342.p28.bold2"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.342.p28"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    private void createSection343(final MessageResources resources, Section section) {
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.46.p1"), ConstantsFont.paragraphFont, section);

        List list = new List();

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p8"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p6"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p10"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p4"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.46.p15"), ConstantsFont.paragraphFont, section);
        PdfPTable table = createVerificationTable(resources);
        table.setSpacingBefore(3 * ConstantsFont.SPACE_LINE);
        section.add(table);
        section.newPage();

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.46.p16"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.46.p17"), ConstantsFont.paragraphFont, section);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.46.p18.bold"), resources.getMessage("anchor.PMPA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.46.p18"), anchorMap, ConstantsFont.paragraphFont));

        PropertiesManager pmgr = new PropertiesManager();
        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMAP.png", "PMAP = SPMVA/VA", 75);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p19.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p19"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p20.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p20"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p21.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p21"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<Integer, SpecialChunk>();
        anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.46.p22.bold"), resources.getMessage("anchor.PMA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.46.p22"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMA.png", "PMA = SPMA/NP", 75);

        list = new List();

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p23.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p24.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        boldWords = new ArrayList<String>();
        boldWords.add(resources.getMessage("ob.resAnon.intav.report.46.p25.bold"));
        item = PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.46.p25"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    @Override
    protected void createGlobalResultsChapter(final MessageResources resources, Font titleFont, String graphicPath, String execution_id, java.util.List<CategoriaForm> categories) throws Exception {
        java.util.List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(execution_id, Constants.COMPLEXITY_SEGMENT_NONE, null);
        Map<String, Integer> result = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);

        Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.chapter4.title"), index, countSections++, numChapter, titleFont);
        createChapter4(resources, chapter);
        chapter.newPage();

        Section section1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter41.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1, resources.getMessage("anchor.4.1"));
        createSection41(resources, section1, graphicPath, result);

        Section section2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter42.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection42(resources, section2, graphicPath, execution_id, pageExecutionList, categories);
        if (categories.size() != 5) {
            chapter.newPage();
        }

        Section section3 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter43.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1, resources.getMessage("anchor.4.3"));
        createSection43(resources, section3, graphicPath, execution_id, pageExecutionList, categories);
        if (categories.size() != 5) {
            chapter.newPage();
        }

        Section section4 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter44.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1, resources.getMessage("anchor.PuMe"));
        createSection44(resources, section4, graphicPath, pageExecutionList);
        chapter.newPage();

        Section section5 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter45.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1, resources.getMessage("anchor.PoMo"));
        createSection45(resources, section5, graphicPath, pageExecutionList);
        chapter.newPage();

        Section section6 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapter46.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSection46(resources, section6, graphicPath, pageExecutionList);

        document.add(chapter);
    }

    private void createChapter4(final MessageResources resources, final Chapter chapter) {
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.4.p1"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.4.p2"), ConstantsFont.paragraphFont, chapter);
    }

    private void createSection41(final MessageResources resources, Section section, String graphicPath, Map<String, Integer> res) throws Exception {
        if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.41.p1.AGE"), ConstantsFont.paragraphFont, section);
        } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.41.p1.CCAA"), ConstantsFont.paragraphFont, section);
        }

        PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.accessibility.level.allocation.name") + ".jpg", resources.getMessage("ob.resAnon.intav.report.41.img.alt"), 75);

        java.util.List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(resources, res);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.41.tableTitle"), section, 380);
        float[] widths = {33f, 33f, 33f};
        PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.porc.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.num.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        for (GraphicData label : labelValueBean) {
            table.addCell(PDFUtils.createTableCell(label.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section.add(table);
    }

    private void createSection42(final MessageResources resources, Section section, String graphicPath, String id_execution,
                                 java.util.List<ObservatoryEvaluationForm> pageExecutionList, java.util.List<CategoriaForm> categories) throws Exception {
        Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageResultsBySegmentMap(id_execution, pageExecutionList, categories);
        PropertiesManager pmgr = new PropertiesManager();
        int numBarByGrapg = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.max.bar.graph"));
        int numGraph = categories.size() / numBarByGrapg;
        if (categories.size() % numBarByGrapg != 0) {
            numGraph++;
        }

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.42.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.42.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.42.p5"), ConstantsFont.paragraphFont, section);

        if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.42.p6"), ConstantsFont.paragraphFont, section);
        }

        for (int i = 1; i <= numGraph; i++) {
            PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.name") + i + ".jpg", resources.getMessage("ob.resAnon.intav.report.42.img.alt"), 75);
        }

        section.newPage();

        for (CategoriaForm category : categories) {
            PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.42.tableTitle", category.getName()), section, 380);
            java.util.List<LabelValueBean> results = ResultadosAnonimosObservatorioIntavUtils.infoComparisonBySegment(resources, res.get(category));
            for (LabelValueBean label : results) {
                label.setValue(label.getValue() + "%");
            }
            java.util.List<String> headers = new ArrayList<String>();
            headers.add(resources.getMessage("resultados.anonimos.level"));
            headers.add(resources.getMessage("ob.resAnon.intav.report.42.resT"));
            section.add(PDFUtils.createResultTable(results, headers));
        }
    }

    private void createSection43(final MessageResources resources, Section section, String graphicPath, String execution_id, java.util.List<ObservatoryEvaluationForm> pageExecutionList, java.util.List<CategoriaForm> categories) throws Exception {
        final Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioIntavUtils.calculateMidPuntuationResultsBySegmentMap(execution_id, pageExecutionList, categories);
        PropertiesManager pmgr = new PropertiesManager();
        int numBarByGrapg = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.max.bar.graph"));
        int numGraph = categories.size() / numBarByGrapg;
        if (categories.size() % numBarByGrapg != 0) {
            numGraph++;
        }

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.43.p1"), ConstantsFont.paragraphFont, section);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.43.p4.anchor"), resources.getMessage("anchor.PMPO"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.43.p4"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.43.p5"), ConstantsFont.paragraphFont, section);
        for (int i = 1; i <= numGraph; i++) {
            PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.name") + i + ".jpg", resources.getMessage("ob.resAnon.intav.report.43.img.alt"), 75);
        }

        section.newPage();

        for (CategoriaForm category : categories) {
            PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.43.tableTitle", category.getName()), section, 300);
            java.util.List<LabelValueBean> results = ResultadosAnonimosObservatorioIntavUtils.infoComparisonBySegmentPuntuation(resources, res.get(category));
            java.util.List<String> headers = new ArrayList<String>();
            headers.add(resources.getMessage("resultados.anonimos.level"));
            headers.add(resources.getMessage("ob.resAnon.intav.report.43.resT"));
            section.add(PDFUtils.createResultTable(results, headers));
        }
    }

    private void createSection44(final MessageResources resources, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
        final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.44.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.44.p2"), ConstantsFont.paragraphFont, section);

        final Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        final SpecialChunk anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.44.p7.anchor"), resources.getMessage("anchor.PMV"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.44.p7"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.44.p8"), ConstantsFont.paragraphFont, section);

        section.newPage();
        Section subSection = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.44.sub1"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection, graphicPath + resources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg", resources.getMessage("ob.resAnon.intav.report.44.img.alt"), 75);

        java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(resources, resultL1);
        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.44.tableTitle1"), subSection, 380);
        java.util.List<String> headers = new ArrayList<String>();
        headers.add(resources.getMessage("resultados.anonimos.punto.verification"));
        headers.add(resources.getMessage("resultados.anonimos.punt.media"));
        PdfPTable table = PDFUtils.createResultTable(labelsL1, headers);
        subSection.add(table);

        Section subSection2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.44.sub2"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection2, graphicPath + resources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg", resources.getMessage("ob.resAnon.intav.report.44.img.alt.2"), 75);

        java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(resources, resultL2);
        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.44.tableTitle2"), subSection2, 380);
        subSection2.add(PDFUtils.createResultTable(labelsL2, headers));
    }

    private void createSection45(final MessageResources resources, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        final Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
        final Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.45.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.45.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.45.p3"), ConstantsFont.paragraphFont, section);

        section.newPage();
        Section subSection = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.45.sub1"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection, graphicPath + resources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + ".jpg", resources.getMessage("ob.resAnon.intav.report.45.img1.alt"), 75);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.45.tableTitle1"), subSection, 380);
        section.add(PDFUtils.createTableMod(resources, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results1)));

        section.newPage();
        Section subSection2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.45.sub2"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection2, graphicPath + resources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + ".jpg", resources.getMessage("ob.resAnon.intav.report.45.img2.alt"), 75);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.45.tableTitle2"), subSection2, 380);
        section.add(PDFUtils.createTableMod(resources, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results2)));
    }

    private void createSection46(final MessageResources resources, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        final Map<String, BigDecimal> result = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(resources, pageExecutionList);

        final Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        final SpecialChunk anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.46.p12.ancla"), resources.getMessage("anchor.PMA"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.46.p12"), anchorMap, ConstantsFont.paragraphFont));
        PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.aspect.mid.name") + ".jpg", resources.getMessage("ob.resAnon.intav.report.46.img.alt"), 75);

        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoAspectMidsComparison(resources, result);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.46.tableTitle"), section, 380);

        java.util.List<String> headers = new ArrayList<String>();
        headers.add(resources.getMessage("resultados.anonimos.aspect"));
        headers.add(resources.getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    @Override
    protected void createCategoryResultsChapter(final MessageResources resources, Font titleFont, String graphicPath, String execution_id, CategoriaForm category) throws Exception {
        java.util.List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(execution_id, Long.parseLong(category.getId()), null);
        Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.chapterCat.title") + " " + category.getName().toUpperCase(), index, countSections++, numChapter, titleFont);

        createSectionCat(resources, chapter, category);

        Section section1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterCat1.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSectionCat1(resources, section1, graphicPath, execution_id, category);
        chapter.newPage();

        Section section2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterCat2.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSectionCat2(resources, section2, graphicPath, category);
        //chapter.newPage();

        Section section3 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterCat3.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSectionCat3(resources, section3, graphicPath, pageExecutionList, category);
        chapter.newPage();

        Section section4 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterCat4.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSectionCat4(resources, section4, graphicPath, pageExecutionList, category);
        chapter.newPage();

        Section section5 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterCat5.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        createSectionCat5(resources, section5, graphicPath, pageExecutionList, category);

        document.add(chapter);
    }

    private void createSectionCat(final MessageResources resources, final Section section, final CategoriaForm category) {
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat.p1", category.getName()), ConstantsFont.paragraphFont, section);
    }

    private void createSectionCat1(final MessageResources resources, Section section, String graphicPath, String execution_id, CategoriaForm category) throws Exception {
        try {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat1.p1"), ConstantsFont.paragraphFont, section);

            java.util.List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(execution_id, Long.parseLong(category.getId()), null);
            Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
            java.util.List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(resources, resultsMap);

            PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", category.getOrden()) + ".jpg", resources.getMessage("ob.resAnon.intav.report.Cat1.img.alt"), 75);

            PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Cat.tableTitle", category.getName()), section, 380);

            float[] widths = {33f, 33f, 33f};
            PdfPTable table = new PdfPTable(widths);
            table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.porc.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.num.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            for (GraphicData label : labelValueBean) {
                table.addCell(PDFUtils.createTableCell(label.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
                table.addCell(PDFUtils.createTableCell(label.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
                table.addCell(PDFUtils.createTableCell(label.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            }
            table.setSpacingBefore(ConstantsFont.SPACE_LINE);
            section.add(table);
        } catch (Exception e) {
            Logger.putLog("Fallo al crear la sección cat1 de la categoría: " + category.getId(), AnonymousResultExportPdfSectionCat.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private void createSectionCat2(final MessageResources resources, Section section, String graphicPath, CategoriaForm category) throws Exception {
        try {
            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat2.p1"), ConstantsFont.paragraphFont, section);

            Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
            SpecialChunk anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.Cat2.p12.anchor"), resources.getMessage("anchor.PMPO"), false, ConstantsFont.paragraphAnchorFont);
            anchorMap.put(1, anchor);
            section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.Cat2.p12"), anchorMap, ConstantsFont.paragraphFont));

            PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat2.p13"), ConstantsFont.paragraphFont, section);

            PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.mark.allocation.segment.name", category.getId()) + ".jpg", resources.getMessage("ob.resAnon.intav.report.Cat2.img.alt" + category.getName()), 85);
        } catch (Exception e) {
            Logger.putLog("Fallo al crear la sección cat2 de la categoría: " + category.getId(), AnonymousResultExportPdfSectionCat.class, Logger.LOG_LEVEL_ERROR, e);
        }

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat2.p15"), ConstantsFont.paragraphFont, section, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat2.p16"), ConstantsFont.paragraphFont, section, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat2.p17"), ConstantsFont.paragraphFont, section, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat2.p18"), ConstantsFont.paragraphFont, section, Element.ALIGN_JUSTIFIED, true, false);
    }

    private void createSectionCat3(final MessageResources resources, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList, CategoriaForm category) throws Exception {
        try {
            Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
            Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);

            ArrayList<String> boldWords = new ArrayList<String>();
            boldWords.add(resources.getMessage("ob.resAnon.intav.report.Cat.segment") + " " + category.getName());
            section.add(PDFUtils.createParagraphWithDiferentFormatWord(resources.getMessage("ob.resAnon.intav.report.Cat3.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.paragraphFont, true));

            Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
            SpecialChunk anchor = new SpecialChunk(resources.getMessage("ob.resAnon.intav.report.Cat3.p2.anchor"), resources.getMessage("anchor.PMV"), false, ConstantsFont.paragraphAnchorFont);
            anchorMap.put(1, anchor);
            section.add(PDFUtils.createParagraphAnchor(resources.getMessage("ob.resAnon.intav.report.Cat3.p2"), anchorMap, ConstantsFont.paragraphFont));

            section.newPage();

            Section subSection = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.Cat3.sub1"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
            PDFUtils.addImageToSection(subSection, graphicPath + resources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + category.getId() + ".jpg", resources.getMessage("ob.resAnon.intav.report.Cat3.img.alt", category.getName()), 75);

            java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(resources, resultL1);
            PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Cat3.tableTitle1", category.getName()), subSection, 400);

            java.util.List<String> headers = new ArrayList<String>();
            headers.add(resources.getMessage("resultados.anonimos.punto.verification"));
            headers.add(resources.getMessage("resultados.anonimos.punt.media"));
            PdfPTable table = PDFUtils.createResultTable(labelsL1, headers);
            subSection.add(table);

            Section subSection2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.Cat3.sub2"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
            PDFUtils.addImageToSection(subSection2, graphicPath + resources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + category.getId() + ".jpg", resources.getMessage("ob.resAnon.intav.report.Cat3.img.alt.2", category.getName()), 75);

            java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(resources, resultL2);
            PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Cat3.tableTitle2", category.getName()), subSection2, 400);
            subSection2.add(PDFUtils.createResultTable(labelsL2, headers));
        } catch (Exception e) {
            Logger.putLog("Fallo al crear la sección cat3 de la categoría: " + category.getId(), AnonymousResultExportPdfSectionCat.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private void createSectionCat4(final MessageResources resources, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList, CategoriaForm category) throws Exception {
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat4.p1"), ConstantsFont.paragraphFont, section, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat4.p2"), ConstantsFont.paragraphFont, section, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat4.p3"), ConstantsFont.paragraphFont, section, Element.ALIGN_JUSTIFIED, true, false);

        section.newPage();

        Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
        Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);

        Section subSection = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.Cat4.sub1"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection, graphicPath + resources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + category.getId() + ".jpg", resources.getMessage("ob.resAnon.intav.report.Cat4.img1.alt", category.getName()), 70);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Cat4.tableTitle1", category.getName()), subSection, 420);

        section.add(PDFUtils.createTableMod(resources, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results1)));
        section.newPage();

        Section subSection2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.Cat4.sub2"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection2, graphicPath + resources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + category.getId() + ".jpg", resources.getMessage("ob.resAnon.intav.report.Cat4.img2.alt", category.getName()), 70);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Cat4.tableTitle2", category.getName()), subSection2, 420);
        section.add(PDFUtils.createTableMod(resources, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results2)));
    }

    private void createSectionCat5(final MessageResources resources, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList, CategoriaForm category) throws Exception {
        Map<String, BigDecimal> result = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(resources, pageExecutionList);

        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat5.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Cat5.p2"), ConstantsFont.paragraphFont, section);

        PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.aspect.mid.name") + category.getId() + ".jpg", resources.getMessage("ob.resAnon.intav.report.Cat5.img.alt", category.getName()), 75);

        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoAspectMidsComparison(resources, result);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Cat5.tableTitle", category.getName()), section, 380);
        java.util.List<String> headers = new ArrayList<String>();
        headers.add(resources.getMessage("resultados.anonimos.aspect"));
        headers.add(resources.getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    @Override
    protected void createEvolutionResultsChapter(final MessageResources resources, Font titleFont, String graphicPath, String idExecution, String idObservatory) throws Exception {
        final Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioIntavUtils.resultEvolutionData(Long.valueOf(idObservatory), Long.valueOf(idExecution));
        final Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioIntavUtils.getEvolutionObservatoriesSitesByType(idObservatory, idExecution, pageObservatoryMap);
        final Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A);
        final Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA);
        final Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV);

        final Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.chapterEv.title"), index, countSections, numChapter, titleFont);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Ev.p1"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Ev.p2"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Ev.p3"), ConstantsFont.paragraphFont, chapter);
        chapter.newPage();

        final Section section1L1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv1.level1.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections, 1);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Ev1.p1"), ConstantsFont.paragraphFont, section1L1);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Ev1.p2"), ConstantsFont.paragraphFont, section1L1, Paragraph.ALIGN_JUSTIFIED, true, true);
        section1L1.newPage();

        final Section section1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv1.title"), index, ConstantsFont.chapterTitleMPFont3L, section1L1, countSections++, 2);
        createSectionEvolutionNivel(resources, section1, graphicPath, resultDataAA, resources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name"),
                resources.getMessage("ob.resAnon.intav.report.Ev1.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev1.ob.tableTitle"));
        section1L1.newPage();

        final Section section2 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv2.title"), index, ConstantsFont.chapterTitleMPFont3L, section1L1, countSections++, 2);
        createSectionEvolutionNivel(resources, section2, graphicPath, resultDataA, resources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name"),
                resources.getMessage("ob.resAnon.intav.report.Ev2.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev2.ob.tableTitle"));
        section1L1.newPage();

        final Section section3 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv3.title"), index, ConstantsFont.chapterTitleMPFont3L, section1L1, countSections++, 2);
        createSectionEvolutionNivel(resources, section3, graphicPath, resultDataNV, resources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name"),
                resources.getMessage("ob.resAnon.intav.report.Ev3.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev3.ob.tableTitle"));
        section1L1.newPage();

        final Section section4 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv4.title"), index, ConstantsFont.chapterTitleMPFont3L, chapter, countSections++, 2);
        createSectionEv4(resources, section4, graphicPath, pageObservatoryMap);
        section4.newPage();

        final Section section3L1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.verification.level1.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections, 1);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Ev5.p1"), ConstantsFont.paragraphFont, section3L1, Paragraph.ALIGN_JUSTIFIED, true, true);
        section3L1.newPage();

        final Section section5 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv5.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section5, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev5.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev5.ob.tableTitle"));
        section3L1.newPage();
        final Section section6 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv6.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section6, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev6.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev6.ob.tableTitle"));
        section3L1.newPage();
        final Section section7 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv7.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section7, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev7.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev7.ob.tableTitle"));
        section3L1.newPage();
        final Section section8 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv8.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section8, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev8.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev8.ob.tableTitle"));
        section3L1.newPage();
        final Section section9 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv9.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section9, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev9.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev9.ob.tableTitle"));
        section3L1.newPage();
        final Section section10 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv10.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section10, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev10.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev10.ob.tableTitle"));
        section3L1.newPage();
        final Section section11 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv11.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section11, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev11.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev11.ob.tableTitle"));
        section3L1.newPage();
        final Section section12 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv12.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section12, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev12.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev12.ob.tableTitle"));
        section3L1.newPage();
        final Section section13 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv13.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section13, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev13.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev13.ob.tableTitle"));
        section3L1.newPage();
        final Section section14 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv14.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section14, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev14.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev14.ob.tableTitle"));
        section3L1.newPage();
        final Section section15 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv15.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section15, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev15.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev15.ob.tableTitle"));
        section3L1.newPage();
        final Section section16 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv16.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section16, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev16.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev16.ob.tableTitle"));
        section3L1.newPage();
        final Section section17 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv17.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section17, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev17.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev17.ob.tableTitle"));
        section3L1.newPage();
        final Section section18 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv18.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section18, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev18.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev18.ob.tableTitle"));
        section3L1.newPage();
        final Section section19 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv19.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section19, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev19.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev19.ob.tableTitle"));
        section3L1.newPage();
        final Section section20 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv20.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section20, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev20.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev20.ob.tableTitle"));
        section3L1.newPage();
        final Section section21 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv21.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section21, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev21.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev21.ob.tableTitle"));
        section3L1.newPage();
        final Section section22 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv22.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section22, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev22.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev22.ob.tableTitle"));
        section3L1.newPage();
        final Section section23 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv23.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section23, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev23.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev23.ob.tableTitle"));
        section3L1.newPage();
        final Section section24 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv24.title"), index, ConstantsFont.chapterTitleMPFont3L, section3L1, countSections++, 2);
        createSectionEvolutionVerificacion(resources, section24, graphicPath, pageObservatoryMap,
                Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION,
                resources.getMessage("ob.resAnon.intav.report.Ev24.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev24.ob.tableTitle"));
        section3L1.newPage();

        final Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<Date, Map<String, BigDecimal>>();
        for (Map.Entry<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryEntry : pageObservatoryMap.entrySet()) {
            resultsByAspect.put(pageObservatoryEntry.getKey(), ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(resources, pageObservatoryEntry.getValue()));
        }
        final Section section4L1 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.aspect.level1.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections, 1);
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.Ev25.p1"), ConstantsFont.paragraphFont, section4L1, Paragraph.ALIGN_JUSTIFIED, true, true);
        section4L1.newPage();
        final Section section25 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv25.title"), index, ConstantsFont.chapterTitleMPFont3L, section4L1, countSections++, 2);
        createSectionEvolutionAspect(resources, section25, graphicPath, resultsByAspect,
                Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID,
                resources.getMessage("ob.resAnon.intav.report.Ev25.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev25.ob.tableTitle"));
        section4L1.newPage();
        final Section section26 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv26.title"), index, ConstantsFont.chapterTitleMPFont3L, section4L1, countSections++, 2);
        createSectionEvolutionAspect(resources, section26, graphicPath, resultsByAspect,
                Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID,
                resources.getMessage("ob.resAnon.intav.report.Ev26.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev26.ob.tableTitle"));
        section4L1.newPage();
        final Section section27 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv27.title"), index, ConstantsFont.chapterTitleMPFont3L, section4L1, countSections++, 2);
        createSectionEvolutionAspect(resources, section27, graphicPath, resultsByAspect,
                Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID,
                resources.getMessage("ob.resAnon.intav.report.Ev27.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev27.ob.tableTitle"));
        section4L1.newPage();
        final Section section28 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv28.title"), index, ConstantsFont.chapterTitleMPFont3L, section4L1, countSections++, 2);
        createSectionEvolutionAspect(resources, section28, graphicPath, resultsByAspect,
                Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID,
                resources.getMessage("ob.resAnon.intav.report.Ev28.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev28.ob.tableTitle"));
        section4L1.newPage();
        final Section section29 = PDFUtils.addSection(resources.getMessage("ob.resAnon.intav.report.chapterEv29.title"), index, ConstantsFont.chapterTitleMPFont3L, section4L1, countSections++, 2);
        createSectionEvolutionAspect(resources, section29, graphicPath, resultsByAspect,
                Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID,
                resources.getMessage("ob.resAnon.intav.report.Ev29.img.alt"),
                resources.getMessage("ob.resAnon.intav.report.Ev29.ob.tableTitle"));

        document.add(chapter);
    }

    private void createSectionEv4(final MessageResources resources, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg", resources.getMessage("ob.resAnon.intav.report.Ev4.img.alt"), 80);

        Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateEvolutionPuntuationDataSet(pageObservatoryMap);
        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoMidMarkEvolutionGraphic(resultData);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Ev4.ob.tableTitle"), section, 420);
        java.util.List<String> headers = new ArrayList<String>();
        headers.add(resources.getMessage("resultados.anonimos.date"));
        headers.add(resources.getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    @Override
    protected void createSummaryChapter(final MessageResources resources, final Font titleFont) throws Exception {
        Chapter chapter = PDFUtils.addChapterTitle(resources.getMessage("ob.resAnon.intav.report.summary.title"), index, countSections++, numChapter, titleFont);
        //Incluir las conclusiones
        document.add(chapter);
    }
}
