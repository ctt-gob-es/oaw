package es.inteco.rastreador2.pdf;

import com.lowagie.text.Chapter;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public final class AnonymousResultExportPdfSection2 {

    private AnonymousResultExportPdfSection2() {
    }

    protected static void createChapter2(HttpServletRequest request, Chapter chapter, long observatoryType) {
        ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p1.bold"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        if (observatoryType == Constants.OBSERVATORY_TYPE_PRENSA) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p2.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p3.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p4.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p3"), ConstantsFont.PARAGRAPH, chapter);
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.PRENSA"), ConstantsFont.PARAGRAPH, chapter);
        } else {
            //PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p2"), ConstantsFont.PARAGRAPH, chapter);
            //PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p3"), ConstantsFont.PARAGRAPH, chapter);

            if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
                //PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p4.AGE"), ConstantsFont.PARAGRAPH, chapter);
                PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.AGE"), ConstantsFont.PARAGRAPH, chapter);
            } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
                PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p4.CCAA"), ConstantsFont.PARAGRAPH, chapter);
                PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.2.p5.CCAA"), ConstantsFont.PARAGRAPH, chapter);
            }
        }
    }

}
