package es.inteco.rastreador2.pdf.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSection4;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public abstract class AnonymousResultExportPdf {

    protected boolean basicService = false;

    public abstract int createIntroductionChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont) throws Exception;

    public abstract int createObjetiveChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, List<ObservatoryEvaluationForm> evaList, long observatoryType) throws DocumentException;

    public abstract int createMethodologyChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) throws Exception;

    public abstract int createContentChapter(HttpServletRequest request, Document d, String contents, IndexEvents index, int numChapter, int countSections) throws Exception;

    public abstract void getMidsComparationByVerificationLevelGraphic(HttpServletRequest request, String level, String title, String filePath, String noDataMess, List<ObservatoryEvaluationForm> evaList, String value, boolean b) throws Exception;

    public abstract ScoreForm generateScores(MessageResources messageResources, List<ObservatoryEvaluationForm> evaList) throws Exception;

    public abstract String getTitle();

    public boolean isBasicService() {
        return basicService;
    }

    public void setBasicService(boolean basicService) {
        this.basicService = basicService;
    }

    /**
     * Métodos de utilidad
     */
    protected PdfPTable createVerificationTable(final MessageResources messageResources) {
        try {
            final float[] widths = {0.15f, 0.65f, 0.20f};
            final PdfPTable table = new PdfPTable(widths);
            final int margin = 10;

            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.header1", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.header2", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources, "ob.resAnon.intav.report.46.table.header3", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

            table.addCell(PDFUtils.createColSpanTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.2header1"), Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

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

            table.addCell(PDFUtils.createColSpanTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.2header2"), Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

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

    protected void createMethodologyHeaderTable(final HttpServletRequest request, final PdfPTable table, final String title) {
        table.addCell(PDFUtils.createColSpanTableCell(title, Constants.VERDE_C_MP, ConstantsFont.labelCellFont, 6, Element.ALIGN_LEFT));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header1"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header2"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header3"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header4"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header5"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.table.header6"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    protected void createMethodologyTableRow(final HttpServletRequest request, final PdfPTable table, final String id, final String name, final String question, final com.lowagie.text.List answer, final com.lowagie.text.List value, final com.lowagie.text.List modality) {
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(id), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(name), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage(question), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createListTableCell(answer, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(value, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(modality, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    protected com.lowagie.text.List createTextList(final HttpServletRequest request, final String text) {
        return createTextList(request, text, Element.ALIGN_CENTER);
    }

    protected com.lowagie.text.List createTextList(final HttpServletRequest request, final String text, final int align) {
        final java.util.List<String> list = Arrays.asList(CrawlerUtils.getResources(request).getMessage(text).split(";"));
        final com.lowagie.text.List PDFlist = new com.lowagie.text.List();
        for (String str : list) {
            PDFUtils.addListItem(str, PDFlist, ConstantsFont.noteCellFont, false, false, align);
        }
        if (align == Element.ALIGN_LEFT) {
            PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE / 5);
        }

        return PDFlist;
    }

    protected com.lowagie.text.List createImageList(final HttpServletRequest request, final String text) {
        final PropertiesManager pmgr = new PropertiesManager();
        final java.util.List<String> list = Arrays.asList(CrawlerUtils.getResources(request).getMessage(text).split(";"));
        final com.lowagie.text.List PDFlist = new com.lowagie.text.List();
        for (String str : list) {
            final com.lowagie.text.Image image;
            if (str.equals("0")) {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.red"), CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.modality.0.alt"));
            } else {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.green"), CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.33.modality.1.alt"));
            }
            image.scalePercent(65);
            final ListItem item = new ListItem(new Chunk(image, 0, 0));
            item.setListSymbol(new Chunk(""));
            PDFlist.add(item);
        }
        PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        return PDFlist;
    }

    protected PdfPTable addURLTable(final MessageResources messageResources, final java.util.List<ObservatoryEvaluationForm> primaryReportPageList) {
        final float[] widths = {30f, 70f};
        final PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.setWidthPercentage(100);

        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        int i = 1;
        for (ObservatoryEvaluationForm page : primaryReportPageList) {

            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(i++), 2, ' ')), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
            PdfPCell url = new PdfPCell(PDFUtils.addLinkParagraph(page.getUrl(), page.getUrl(), ConstantsFont.noteCellFont));
            table.addCell(url);
        }

        return table;
    }

    protected String getValidationLevel(final ScoreForm scoreForm, final MessageResources messageResources) {
        if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal(8)) >= 0) {
            return messageResources.getMessage("resultados.anonimos.num.portales.aa");
        } else if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal("3.5")) <= 0) {
            return messageResources.getMessage("resultados.anonimos.num.portales.nv");
        } else {
            return messageResources.getMessage("resultados.anonimos.num.portales.a");
        }
    }


}
