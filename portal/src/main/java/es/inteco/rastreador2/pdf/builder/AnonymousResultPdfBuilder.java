package es.inteco.rastreador2.pdf.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.intav.utils.IntavUtils;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfAction;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSection3;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSection4;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.RankingInfo;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.*;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase base para la generación de los informes anónimos de resultados en PDF
 */
public abstract class AnonymousResultPdfBuilder {

    protected final long observatoryType;
    protected final Document document;
    protected final PdfWriter writer;

    protected int countSections;
    protected int numChapter;
    protected final IndexEvents index;

    protected AnonymousResultPdfBuilder(final File file, final long observatoryType) throws Exception {
        this.observatoryType = observatoryType;
        countSections = 1;
        numChapter = 1;

        document = new Document(PageSize.A4, 50, 50, 120, 72);
        writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
        writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));

        //Inicializamos el índice
        index = new IndexEvents();
        writer.setPageEvent(index);
        writer.setLinearPageMode();
    }

    public abstract void generateGraphics(MessageResources messageResources, String executionId, Long idExecutionObservatory, final String observatoryId, String filePath) throws Exception;

    public void buildDocument(final MessageResources resources, final java.util.List<ObservatoryEvaluationForm> primaryReportPageList, String graphicPath, String idObservatory, String idExecution, java.util.List<CategoriaForm> categories, boolean includeEvolution) throws Exception {
        Logger.putLog("Creando informe PDF", AnonymousResultPdfBuilder.class, Logger.LOG_LEVEL_INFO);
        final Font titleFont = ConstantsFont.CHAPTER_TITLE_MP_FONT;
        titleFont.setStyle(Font.BOLD);
        document.open();
        createIntroductionChapter(resources, titleFont);
        createObjetiveChapter(resources, titleFont);
        createMethodologyChapter(resources, titleFont, primaryReportPageList, false);
        createGlobalResultsChapter(resources, titleFont, graphicPath, idExecution, categories);
        createIntroductionChapter(resources, titleFont);

        for (CategoriaForm category : categories) {
            numChapter = numChapter + 1;
            createCategoryResultsChapter(resources, titleFont, graphicPath, idExecution, category);
        }

        if (includeEvolution) {
            numChapter = numChapter + 1;
            createEvolutionResultsChapter(resources, titleFont, graphicPath, idExecution, idObservatory);
        }

        numChapter = numChapter + 1;
        createSummaryChapter(resources, titleFont);

        //Creamos el index
        IndexUtils.createIndex(writer, document, resources.getMessage("pdf.accessibility.index.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT);
        ExportPageEventsObservatoryMP.setPrintFooter(true);

        if (document.isOpen()) {
            try {
                // ¿Hace falta este try?. En principio no se define ningún throws Exception para el método close
                document.close();
                writer.flush();
                writer.close();
            } catch (Exception e) {
                Logger.putLog("Error al cerrar el pdf", AnonymousResultExportPdfAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    protected abstract void createIntroductionChapter(MessageResources resources, Font titleFont) throws Exception;

    protected abstract void createObjetiveChapter(MessageResources resources, Font titleFont) throws DocumentException;

    protected abstract void createMethodologyChapter(MessageResources resources, Font titleFont, java.util.List<ObservatoryEvaluationForm> primaryReportPageList, boolean isBasicService) throws Exception;

    protected abstract void createGlobalResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, java.util.List<CategoriaForm> categories) throws Exception;

    protected abstract void createCategoryResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String execution_id, CategoriaForm category) throws Exception;

    protected abstract void createEvolutionResultsChapter(MessageResources resources, Font titleFont, String graphicPath, String idExecution, String idObservatory) throws Exception;

    protected abstract void createSummaryChapter(MessageResources resources, Font titleFont) throws Exception;


    /**
     * ****************************** UTILS ************************************
     */
    protected void createSectionEvolutionNivel(final MessageResources resources, Section section, String graphicPath, Map<String, BigDecimal> resultData, String image, String alt, String tableTitle) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + image + ".jpg", alt, 80);

        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoLevelEvolutionGraphic(resultData);

        PDFUtils.createTitleTable(tableTitle, section, 420);
        java.util.List<String> headers = new ArrayList<>();
        headers.add(resources.getMessage("resultados.anonimos.date"));
        headers.add(resources.getMessage("resultados.anonimos.porc.portales"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    protected void createSectionEvolutionVerificacion(final MessageResources resources, Section section, String graphicPath, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap,
                                                      String verificacion, String alt, String tableTitle) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", verificacion) + ".jpg", alt, 80);

        PDFUtils.createTitleTable(tableTitle, section, 420);
        createVerificationResults(resources, section, pageObservatoryMap, verificacion);
    }

    protected void createSectionEvolutionAspect(final MessageResources resources, Section section, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect, String aspect, String alt, String tableTitle) throws BadElementException, IOException {
        PDFUtils.addImageToSection(section, graphicPath + resources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", aspect) + ".jpg", alt, 80);

        PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.Ev.asp.tableTible", tableTitle), section, 420);
        createAspectResults(resources, section, resultsByAspect, aspect);
    }

    protected PdfPTable addURLTable(MessageResources resources, java.util.List<ObservatoryEvaluationForm> primaryReportPageList) {
        float[] widths = {30f, 70f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setWidthPercentage(100);

        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        int i = 1;
        for (ObservatoryEvaluationForm page : primaryReportPageList) {
            table.addCell(PDFUtils.createTableCell(resources.getMessage("observatory.graphic.score.by.page.label", i++), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
            table.addCell(PDFUtils.createLinkedTableCell(page.getUrl(), page.getUrl(),Color.WHITE, Element.ALIGN_LEFT, ConstantsFont.DEFAULT_PADDING));
        }

        return table;
    }

    protected PdfPTable createMethodologyTable1(final MessageResources resources) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setWidthPercentage(100);

        try {
            createMethodologyHaderTable(resources, table, resources.getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.111", "ob.resAnon.intav.report.33.table.111.name", "ob.resAnon.intav.report.33.table.111.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.111.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.111.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.111.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.112", "ob.resAnon.intav.report.33.table.112.name", "ob.resAnon.intav.report.33.table.112.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.112.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.112.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.112.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.113", "ob.resAnon.intav.report.33.table.113.name", "ob.resAnon.intav.report.33.table.113.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.113.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.113.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.113.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.114", "ob.resAnon.intav.report.33.table.114.name", "ob.resAnon.intav.report.33.table.114.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.114.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.114.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.114.modality"));

            createMethodologyHaderTable(resources, table, resources.getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.121", "ob.resAnon.intav.report.33.table.121.name", "ob.resAnon.intav.report.33.table.121.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.121.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.121.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.121.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.122", "ob.resAnon.intav.report.33.table.122.name", "ob.resAnon.intav.report.33.table.122.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.122.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.122.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.122.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.123", "ob.resAnon.intav.report.33.table.123.name", "ob.resAnon.intav.report.33.table.123.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.123.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.123.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.123.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.124", "ob.resAnon.intav.report.33.table.124.name", "ob.resAnon.intav.report.33.table.124.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.124.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.124.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.124.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.125", "ob.resAnon.intav.report.33.table.125.name", "ob.resAnon.intav.report.33.table.125.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.125.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.125.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.125.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.126", "ob.resAnon.intav.report.33.table.126.name", "ob.resAnon.intav.report.33.table.126.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.126.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.126.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.126.modality"));

        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdfSection3.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    protected PdfPTable createMethodologyTable2(final MessageResources resources) {
        float[] widths = {10f, 30f, 45f, 25f, 15f, 15f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setWidthPercentage(100);

        try {
            createMethodologyHaderTable(resources, table, resources.getMessage("ob.resAnon.intav.report.33.table.title1"));

            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.211", "ob.resAnon.intav.report.33.table.211.name", "ob.resAnon.intav.report.33.table.211.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.211.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.211.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.211.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.212", "ob.resAnon.intav.report.33.table.212.name", "ob.resAnon.intav.report.33.table.212.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.212.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.212.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.212.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.213", "ob.resAnon.intav.report.33.table.213.name", "ob.resAnon.intav.report.33.table.213.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.213.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.213.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.213.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.214", "ob.resAnon.intav.report.33.table.214.name", "ob.resAnon.intav.report.33.table.214.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.214.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.214.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.214.modality"));

            createMethodologyHaderTable(resources, table, resources.getMessage("ob.resAnon.intav.report.33.table.title2"));

            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.221", "ob.resAnon.intav.report.33.table.221.name", "ob.resAnon.intav.report.33.table.221.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.221.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.221.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.221.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.222", "ob.resAnon.intav.report.33.table.222.name", "ob.resAnon.intav.report.33.table.222.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.222.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.222.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.222.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.223", "ob.resAnon.intav.report.33.table.223.name", "ob.resAnon.intav.report.33.table.223.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.223.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.223.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.223.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.224", "ob.resAnon.intav.report.33.table.224.name", "ob.resAnon.intav.report.33.table.224.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.224.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.224.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.224.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.225", "ob.resAnon.intav.report.33.table.225.name", "ob.resAnon.intav.report.33.table.225.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.225.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.225.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.225.modality"));
            createMethodologyTableRow(resources, table, "ob.resAnon.intav.report.33.table.id.226", "ob.resAnon.intav.report.33.table.226.name", "ob.resAnon.intav.report.33.table.226.question",
                    createTextList(resources, "ob.resAnon.intav.report.33.table.226.answer"), createTextList(resources, "ob.resAnon.intav.report.33.table.226.value"), createImageList(resources, "ob.resAnon.intav.report.33.table.226.modality"));

        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 3.3", AnonymousResultExportPdfSection3.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return table;
    }

    protected void createMethodologyHaderTable(final MessageResources resources, PdfPTable table, String title) {
        table.addCell(PDFUtils.createColSpanTableCell(title, Constants.VERDE_C_MP, ConstantsFont.labelCellFont, 6, Element.ALIGN_LEFT));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.33.table.header1"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.33.table.header2"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.33.table.header3"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.33.table.header4"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.33.table.header5"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.33.table.header6"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    protected void createMethodologyTableRow(final MessageResources resources, PdfPTable table, String id, String name, String question, List answer, List value, List modality) {
        table.addCell(PDFUtils.createTableCell(resources.getMessage(id), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(resources.getMessage(name), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(resources.getMessage(question), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createListTableCell(answer, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(value, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(modality, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    protected List createTextList(final MessageResources resources, String text) {
        return createTextList(resources, text, Element.ALIGN_CENTER);
    }

    protected List createTextList(final MessageResources resources, String text, int align) {
        java.util.List<String> list = Arrays.asList(resources.getMessage(text).split(";"));
        List PDFlist = new List();
        for (String str : list) {
            PDFUtils.addListItem(str, PDFlist, ConstantsFont.noteCellFont, false, false, align);
        }
        if (align == Element.ALIGN_LEFT) {
            PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE / 5);
        }

        return PDFlist;
    }

    protected List createImageList(final MessageResources resources, String text) {
        PropertiesManager pmgr = new PropertiesManager();
        Image image;
        java.util.List<String> list = Arrays.asList(resources.getMessage(text).split(";"));
        List PDFlist = new List();
        for (String str : list) {
            if (str.equals("0")) {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.red"), resources.getMessage("ob.resAnon.intav.report.33.modality.0.alt"));
            } else {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.green"), resources.getMessage("ob.resAnon.intav.report.33.modality.1.alt"));
            }
            image.scalePercent(65);
            ListItem item = new ListItem(new Chunk(image, 0, 0));
            item.setListSymbol(new Chunk(""));
            PDFlist.add(item);
        }
        PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        return PDFlist;
    }

    protected PdfPTable create331Table(MessageResources resources) {
        float[] widths = {40f, 30f, 30f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setWidthPercentage(100);
        create331HaderTable(resources, table);
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.1.1", createTextList(resources, "ob.resAnon.intav.report.331.table.req.111"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.111"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.1.2", createTextList(resources, "ob.resAnon.intav.report.331.table.req.112"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.112"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.1.3", createTextList(resources, "ob.resAnon.intav.report.331.table.req.113"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.113"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.1.4", createTextList(resources, "ob.resAnon.intav.report.331.table.req.114"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.114"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.2.1", createTextList(resources, "ob.resAnon.intav.report.331.table.req.121"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.121"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.2.2", createTextList(resources, "ob.resAnon.intav.report.331.table.req.122"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.122"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.2.3", createTextList(resources, "ob.resAnon.intav.report.331.table.req.123"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.123"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.2.4", createTextList(resources, "ob.resAnon.intav.report.331.table.req.124"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.124"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.2.5", createTextList(resources, "ob.resAnon.intav.report.331.table.req.125"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.125"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.1.2.6", createTextList(resources, "ob.resAnon.intav.report.331.table.req.126"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.126"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.1.1", createTextList(resources, "ob.resAnon.intav.report.331.table.req.211"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.211"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.1.2", createTextList(resources, "ob.resAnon.intav.report.331.table.req.212"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.212"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.1.3", createTextList(resources, "ob.resAnon.intav.report.331.table.req.213"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.213"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.1.4", createTextList(resources, "ob.resAnon.intav.report.331.table.req.214"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.214"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.2.1", createTextList(resources, "ob.resAnon.intav.report.331.table.req.221"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.221"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.2.2", createTextList(resources, "ob.resAnon.intav.report.331.table.req.222"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.222"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.2.3", createTextList(resources, "ob.resAnon.intav.report.331.table.req.223"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.223"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.2.4", createTextList(resources, "ob.resAnon.intav.report.331.table.req.224"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.224"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.2.5", createTextList(resources, "ob.resAnon.intav.report.331.table.req.225"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.225"));
        create331TableRow(resources, table, "inteco.observatory.subgroup.2.2.6", createTextList(resources, "ob.resAnon.intav.report.331.table.req.226"), createTextList(resources, "ob.resAnon.intav.report.331.table.verP.226"));

        return table;
    }

    protected void create331HaderTable(final MessageResources resources, PdfPTable table) {
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.331.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.331.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.331.table.header3"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    protected void create331TableRow(final MessageResources resources, PdfPTable table, String verification, List req, List verP) {
        table.addCell(PDFUtils.createTableCell(resources.getMessage(verification), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 10, -1));
        table.addCell(PDFUtils.createListTableCell(req, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(verP, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    protected PdfPTable createVerificationTable(final MessageResources resources) {

        try {
            float[] widths = {0.15f, 0.65f, 0.20f};
            PdfPTable table = new PdfPTable(widths);
            int margin = 10;

            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.header3"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

            table.addCell(PDFUtils.createColSpanTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.2header1"), Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification111"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification111.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.alt"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification112"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification112.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.alt"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification113"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification113.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.alt"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification114"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification114.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification121"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification121.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification122"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification122.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.pre"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification123"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification123.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification124"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification124.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification125"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification125.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification126"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification126.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));

            table.addCell(PDFUtils.createColSpanTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.2header2"), Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification211"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification211.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification212"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification212.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification213"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification213.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification214"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification214.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification221"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification221.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification222"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification222.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.pre"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification223"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification223.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification224"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification224.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification225"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification225.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification226"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.verification226.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(resources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));

            table.setSpacingAfter(ConstantsFont.LINE_SPACE);

            return table;
        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 4.5", AnonymousResultExportPdfSection4.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

    protected void createVerificationResults(final MessageResources resources, Section section, Map<Date, java.util.List<ObservatoryEvaluationForm>> pageObservatoryMap, String verification) {
        Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(verification, pageObservatoryMap);
        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(resources, resultData);
        java.util.List<String> headers = new ArrayList<>();
        headers.add(resources.getMessage("resultados.anonimos.date"));
        headers.add(resources.getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    protected void createAspectResults(final MessageResources resources, Section section, Map<Date, Map<String, BigDecimal>> resultsByAspect, String aspect) {
        Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(aspect, resultsByAspect);
        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoMidMarkAspectEvolutionGraphic(resources, resultData);
        java.util.List<String> headers = new ArrayList<>();
        headers.add(resources.getMessage("resultados.anonimos.date"));
        headers.add(resources.getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

    protected int addObservatoryScoreSummary(HttpServletRequest request, Document document, IndexEvents index, java.util.List<ObservatoryEvaluationForm> evaList, int numChapter, int countSections, File file, RankingInfo rankingInfo) throws Exception {
        final MessageResources messageResources = CrawlerUtils.getResources(request);
        final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.resultados.resumen").toUpperCase(), index, countSections++, numChapter, ConstantsFont.CHAPTER_TITLE_MP_FONT);

        ArrayList<String> boldWord = new ArrayList<>();

        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p1"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWord = new ArrayList<>();
        boldWord.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p2.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p2"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWord = new ArrayList<>();
        boldWord.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p3.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.4.p3"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        ScoreForm scoreForm = IntavUtils.generateScores(messageResources, evaList);

        boldWord = new ArrayList<>();
        boldWord.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.nivel.adecuacion") + ": ");
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + scoreForm.getLevel(), boldWord, ConstantsFont.SUMMARY_SCORE_BOLD_FONT, ConstantsFont.SUMMARY_SCORE_FONT, true));
        boldWord = new ArrayList<>();
        boldWord.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.puntuacion.total") + ": ");
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + scoreForm.getTotalScore(), boldWord, ConstantsFont.SUMMARY_SCORE_BOLD_FONT, ConstantsFont.SUMMARY_SCORE_FONT, false));
        if (rankingInfo != null && rankingInfo.getGlobalSeedsNumber() > 1) {
            boldWord = new ArrayList<>();
            boldWord.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.posicion.global") + ": ");
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + rankingInfo.getGlobalRank() + " " + messageResources.getMessage("de.text", rankingInfo.getGlobalSeedsNumber()), boldWord, ConstantsFont.SUMMARY_SCORE_BOLD_FONT, ConstantsFont.SUMMARY_SCORE_FONT, false));

            boldWord = new ArrayList<>();
            boldWord.add(messageResources.getMessage(CrawlerUtils.getLocale(request), "observatorio.posicion.segmento", rankingInfo.getCategoria().getName()) + ": ");
            chapter.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + rankingInfo.getCategoryRank() + " " + messageResources.getMessage("de.text", rankingInfo.getCategorySeedsNumber()), boldWord, ConstantsFont.SUMMARY_SCORE_BOLD_FONT, ConstantsFont.SUMMARY_SCORE_FONT, false));

            chapter.add(Chunk.NEWLINE);
        }

        String noDataMess = messageResources.getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");
        addLevelAllocationResultsSummary(messageResources, chapter, file, evaList, noDataMess);

        Section section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion1"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections++, 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.41.p1"), ConstantsFont.PARAGRAPH, section);
        addMidsComparationByVerificationLevelGraphic(request, section, file, evaList, noDataMess, Constants.OBS_PRIORITY_1);
        section.add(createGlobalTable(CrawlerUtils.getResources(request), scoreForm, Constants.OBS_PRIORITY_1));

        section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion2"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections++, 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.42.p1"), ConstantsFont.PARAGRAPH, section);
        addMidsComparationByVerificationLevelGraphic(request, section, file, evaList, noDataMess, Constants.OBS_PRIORITY_2);
        section.add(createGlobalTable(CrawlerUtils.getResources(request), scoreForm, Constants.OBS_PRIORITY_2));

        PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuacion.pagina"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections++, 1);
        addResultsByPage(CrawlerUtils.getResources(request), chapter, file, evaList, noDataMess);

        document.add(chapter);
        return countSections;
    }

    protected int addObservatoryResultsSummary(HttpServletRequest request, Document document, IndexEvents index, java.util.List<ObservatoryEvaluationForm> evaList, int numChapter, int countSections) throws Exception {
        Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.primarios.res.verificacion").toUpperCase(), index, countSections++, numChapter, ConstantsFont.CHAPTER_TITLE_MP_FONT);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.5.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("resultados.primarios.5.p2"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
        countSections = addResultsByVerification(request, chapter, evaList, countSections, index);
        document.add(chapter);

        return countSections;
    }

    protected int addResultsByVerification(HttpServletRequest request, Chapter chapter, java.util.List<ObservatoryEvaluationForm> evaList, int countSections, IndexEvents index) {
        Map<String, java.util.List<LabelValueBean>> results = new TreeMap<>();

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

        for (Map.Entry<String, java.util.List<LabelValueBean>> entry : results.entrySet()) {
            String id = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), entry.getKey()).substring(0, 5);
            String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), entry.getKey()).substring(6) + " (" + id + ")";
            Section section = PDFUtils.createSection(title, index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections++, 1);
            section.add(createVerificationTable(request, entry.getValue()));
            chapter.newPage();
        }

        return countSections;
    }

    protected PdfPTable createVerificationTable(HttpServletRequest request, java.util.List<LabelValueBean> results) {
        float[] widths = {0.50f, 0.25f, 0.25f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.modalidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (LabelValueBean lvb : results) {
            int value = Integer.parseInt(lvb.getValue());
            table.addCell(PDFUtils.createTableCell(lvb.getLabel(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            if (value == Constants.OBS_VALUE_NOT_SCORE) {
                table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            } else if (value == Constants.OBS_VALUE_RED_ZERO || value == Constants.OBS_VALUE_GREEN_ZERO) {
                table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.cero.pasa"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            } else {
                table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.uno"), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, 0));
            }

            PdfPCell labelCell;
            if (value == Constants.OBS_VALUE_NOT_SCORE || value == Constants.OBS_VALUE_GREEN_ZERO || value == Constants.OBS_VALUE_GREEN_ONE) {
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

    protected PdfPTable createGlobalTable(final MessageResources messageResources, final ScoreForm scoreForm, final String level) {
        float[] widths = {0.7f, 0.3f};
        PdfPTable table = new PdfPTable(widths);
        int margin = 10;

        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        if (level.equals(Constants.OBS_PRIORITY_1)) {
            for (LabelValueBean label : scoreForm.getVerifications1()) {
                table.addCell(PDFUtils.createTableCell(label.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
                table.addCell(PDFUtils.createTableCell(label.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            }
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.1"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(scoreForm.getScoreLevel1().toString(), Constants.NARANJA_MP, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        } else if (level.equals(Constants.OBS_PRIORITY_2)) {
            for (LabelValueBean label : scoreForm.getVerifications2()) {
                table.addCell(PDFUtils.createTableCell(label.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
                table.addCell(PDFUtils.createTableCell(label.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            }
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.2"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(scoreForm.getScoreLevel2().toString(), Constants.NARANJA_MP, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setSpacingAfter(2 * ConstantsFont.LINE_SPACE);
        return table;
    }

    private void addResultsByPage(final MessageResources messageResources, final Chapter chapter, final File file, final java.util.List<ObservatoryEvaluationForm> evaList, final String noDataMess) throws Exception {
        final Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
        final SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("resultados.primarios.43.p1.anchor"), messageResources.getMessage("anchor.PMP"), false, ConstantsFont.PARAGRAPH_ANCHOR_FONT);
        anchorMap.put(1, anchor);
        chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.43.p1"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.43.p2"), ConstantsFont.PARAGRAPH, chapter);

        chapter.add(Chunk.NEWLINE);

        final String title = messageResources.getMessage("observatory.graphic.score.by.page.title");
        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test4.jpg";
        ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(messageResources, evaList, title, filePath, noDataMess);

        final Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(80);
        image.setAlignment(Element.ALIGN_CENTER);
        chapter.add(image);

        final float[] widths = {33f, 33f, 33f};
        final PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        int pageCounter = 0;
        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", ++pageCounter), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(String.valueOf(evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        chapter.add(table);
    }

    protected static void addLevelAllocationResultsSummary(final MessageResources messageResources, final Section section,
                                                         final File file, final java.util.List<ObservatoryEvaluationForm> evaList, final String noDataMess) throws Exception {
        final Map<String, Integer> result = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(evaList);
        final java.util.List<GraphicData> labelValueBeanList = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, result);

        final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test.jpg";

        final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
        ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, evaList, title, filePath, noDataMess);
        final Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        section.add(image);

        final float[] widths = {33f, 33f, 33f};
        final PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.num.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        for (GraphicData label : labelValueBeanList) {
            table.addCell(PDFUtils.createTableCell(label.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }
        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        section.add(table);
    }

    private static void addMidsComparationByVerificationLevelGraphic(HttpServletRequest request, Section section, File file, java.util.List<ObservatoryEvaluationForm> evaList, String noDataMess, String level) throws Exception {
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
        ResultadosAnonimosObservatorioIntavUtils.getMidsComparationByVerificationLevelGraphic(request, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), true);
        Image image = PDFUtils.createImage(filePath, null);
        image.scalePercent(60);
        image.setAlignment(Element.ALIGN_CENTER);
        section.add(image);
    }
}
