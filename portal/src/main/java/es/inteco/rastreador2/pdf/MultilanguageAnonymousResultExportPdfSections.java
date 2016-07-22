package es.inteco.rastreador2.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.ConstantsFont;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MultilanguageAnonymousResultExportPdfSections {

    private MultilanguageAnonymousResultExportPdfSections() {
    }

    public static int createIntroductionChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont) throws DocumentException, IOException {
        Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter1.title"), index, countSections++, numChapter, titleFont);

        ArrayList<String> boldWords = new ArrayList<String>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.1.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.1.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.1.p2"), ConstantsFont.PARAGRAPH, chapter);

        document.add(chapter);

        return countSections;
    }

    public static int createObjetiveChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, long observatoryType) throws DocumentException {
        Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter2.title"), index, countSections++, numChapter, titleFont);
        document.add(chapter);

        return countSections;
    }

    public static int createMethodologyChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, List<AnalysisForm> analysisList, long observatoryType, boolean isBasicService) throws Exception {
        Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter3.title"), index, countSections++, numChapter, titleFont);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.3.p1"), ConstantsFont.PARAGRAPH, chapter);

        Section section1 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter31.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        MultilanguageAnonymousResultExportPdfSection3.createSection31(request, section1);

        Section section2 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter32.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        MultilanguageAnonymousResultExportPdfSection3.createSection32(request, section2, analysisList, observatoryType, isBasicService);

        Section section3 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.chapter33.title"), index, ConstantsFont.chapterTitleMPFont2L, chapter, countSections++, 1);
        MultilanguageAnonymousResultExportPdfSection3.createSection33(request, section3);

        Section section31 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.chapter331.title"), index, ConstantsFont.chapterTitleMPFont2L, section3, countSections++, 1);
        MultilanguageAnonymousResultExportPdfSection3.createSection331(request, section31);

        Section section32 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.chapter332.title"), index, ConstantsFont.chapterTitleMPFont2L, section3, countSections++, 1);
        MultilanguageAnonymousResultExportPdfSection3.createSection332(request, section32);

        Section section33 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.multilanguage.report.chapter333.title"), index, ConstantsFont.chapterTitleMPFont2L, section3, countSections++, 1);
        MultilanguageAnonymousResultExportPdfSection3.createSection333(request, section33);

        document.add(chapter);
        return countSections;
    }

}
