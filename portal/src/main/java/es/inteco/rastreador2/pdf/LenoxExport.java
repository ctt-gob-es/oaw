package es.inteco.rastreador2.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.modules.analisis.dto.ResultsByUrlDto;
import es.inteco.crawler.sexista.modules.commons.dto.ResultadoDto;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.RastreoEjecutadoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.lenox.dto.DetalleDto;
import es.inteco.rastreador2.lenox.dto.RastreoExtDto;
import es.inteco.rastreador2.lenox.dto.SugerenciaDto;
import es.inteco.rastreador2.lenox.dto.TerminoDetalleDto;
import es.inteco.rastreador2.lenox.service.InformesService;
import es.inteco.rastreador2.pdf.template.ExportPageEvents;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicsUtils;
import es.inteco.utils.FileUtils;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class LenoxExport {

    static int x = 0;
    static int y = 0;
    static String color = "";

    static {
        PropertiesManager pmgr = new PropertiesManager();

        x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.x"));
        y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.y"));

        color = pmgr.getValue(CRAWLER_PROPERTIES, "chart.lenox.graphic.colors");
    }

    private LenoxExport() {
    }

    public static void exportLenoxToPdf(List<ResultsByUrlDto> results, HttpServletRequest request, String generalExpPath) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();

        String pathExports = generalExpPath + File.separator + pmgr.getValue("pdf.properties", "pdf.file.lenox.name");

        File file = new File(pathExports);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        Document document = new Document(PageSize.A4, 50, 50, 120, 72);

        String globalPath = generalExpPath + File.separator + "temp" + File.separator;

        Connection conn = null;
        Connection c = null;
        try {
            conn = DataBaseManager.getConnection();

            //Recuperamos los resultados
            c = DataBaseManager.getConnection();

            List<DetalleDto> detailAnalyseList = getLenoxResults(request, c, results);

            PdfWriter writer = PdfWriter.getInstance(document, fileOut);
            writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
            // writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));
            writer.setPageEvent(new ExportPageEvents(CrawlerUtils.getResources(request).getMessage("pdf.lenox.foot.text", detailAnalyseList.get(0).getRastreo().getEntidad()), detailAnalyseList.get(0).getRastreo().getFecha()));

            IndexEvents index = new IndexEvents();
            writer.setPageEvent(index);
            writer.setLinearPageMode();

            document.open();

            PDFUtils.addTitlePage(document, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.title"),
                    detailAnalyseList.get(0).getRastreo().getEntidad(), ConstantsFont.documentTitleFont);

            Chapter chapter1 = globalLenoxChapter(detailAnalyseList, request, index, globalPath);
            document.add(chapter1);

            int numChapter = 2;

            List<DetalleDto> urlsNoLocatedTerms = new ArrayList<DetalleDto>();

            for (DetalleDto analyse : detailAnalyseList) {
                if (analyse.getDetalleTerminos() != null && !analyse.getDetalleTerminos().isEmpty()) {
                    Chunk chunk = new Chunk(analyse.getUrl().toUpperCase());
                    chunk.setLocalDestination(Constants.ANCLA_PDF + (numChapter - 1));
                    Paragraph cTitle = new Paragraph("", ConstantsFont.chapterTitleFont);
                    cTitle.add(chunk);
                    cTitle.setSpacingAfter(2 * ConstantsFont.SPACE_LINE);
                    Chapter chapter = new Chapter(cTitle, numChapter);
                    chapter.setNumberDepth(0);
                    cTitle.add(index.create(" ", analyse.getUrl().toUpperCase()));

                    com.lowagie.text.List summary = addLenoxSummary(analyse, request, false);
                    chapter.add(summary);

                    List<DetalleDto> listOneAnalyse = new ArrayList<DetalleDto>();
                    listOneAnalyse.add(analyse);
                    generateLenoxGraphic(listOneAnalyse, request, CrawlerUtils.getResources(request).getMessage("pdf.lenox.global.indicator"), globalPath);
                    addImage(request, globalPath, chapter);

                    chapter.newPage();

                    boolean spaceTerms = false;
                    for (TerminoDetalleDto termino : analyse.getDetalleTerminos()) {
                        Paragraph head = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.term") + ": ", ConstantsFont.titleLenoxFont);
                        Paragraph value = new Paragraph(termino.getNombre(), ConstantsFont.valueLenoxFont);
                        Paragraph p = new Paragraph(head);
                        p.add(value);
                        p.setSpacingAfter(ConstantsFont.SPACE_LINE);
                        if (spaceTerms) {
                            p.setSpacingBefore(3 * ConstantsFont.SPACE_LINE);
                        }
                        spaceTerms = true;

                        chapter.add(p);

                        head = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.num.appearance") + ": ", ConstantsFont.titleLenoxFont);
                        value = new Paragraph(termino.getNumOcurrencias().toString(), ConstantsFont.valueLenoxFont);
                        if (termino.getContextos().size() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "max.lenox.context"))) {
                            value.add(CrawlerUtils.getResources(request).getMessage("lenox.max.context.info", pmgr.getValue(CRAWLER_PROPERTIES, "max.lenox.context")));
                        }
                        p = new Paragraph(head);
                        p.add(value);
                        p.setSpacingAfter(ConstantsFont.SPACE_LINE);
                        chapter.add(p);

                        addLenoxContext(chapter, termino.getContextos(), request);
                        addLenoxSuggestions(chapter, termino.getAlternativas(), request);
                    }
                    document.add(chapter);
                    numChapter++;
                } else {
                    urlsNoLocatedTerms.add(analyse);
                }
            }

            if (urlsNoLocatedTerms != null && !urlsNoLocatedTerms.isEmpty()) {
                document.add(createUnlocatedTermsUrl(urlsNoLocatedTerms, request, index, numChapter));
            }

            FileUtils.removeFile(globalPath);

            ExportPageEvents.setLastPage(true);
            IndexUtils.createIndex(writer, document, request, index, false, ConstantsFont.chapterTitleFont);
            ExportPageEvents.setLastPage(false);

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
            DataBaseManager.closeConnection(conn);
            DataBaseManager.closeConnection(c);
        }
    }

    private static Chapter createUnlocatedTermsUrl(List<DetalleDto> detailAnalyseList, HttpServletRequest request,
                                                   IndexEvents index, int numChapter) throws Exception {

        Chunk chunk = new Chunk(CrawlerUtils.getResources(request).getMessage("pdf.lenox.unlocated.url.terms").toUpperCase() + ":");
        chunk.setLocalDestination(Constants.ANCLA_PDF + (numChapter - 1));
        Paragraph cTitle = new Paragraph("", ConstantsFont.chapterTitleFont);
        cTitle.add(chunk);
        cTitle.setSpacingAfter(2 * ConstantsFont.SPACE_LINE);
        Chapter chapter = new Chapter(cTitle, numChapter);
        chapter.setNumberDepth(0);
        cTitle.add(index.create(" ", CrawlerUtils.getResources(request).getMessage("pdf.lenox.unlocated.url.terms").toUpperCase()));

        com.lowagie.text.List summary = new com.lowagie.text.List();
        PropertiesManager pmgr = new PropertiesManager();
        Image img = Image.getInstance(pmgr.getValue("pdf.properties", "path.lenox.pdf.list"));
        img.setAlt("");

        for (DetalleDto detail : detailAnalyseList) {
            Paragraph text = new Paragraph(detail.getUrl(), ConstantsFont.valueLenoxFont);
            ListItem textItem = new ListItem(text);
            textItem.setListSymbol(new Chunk(img, -5, 0));
            summary.add(textItem);
        }
        chapter.add(summary);

        return chapter;
    }

    private static List<DetalleDto> getLenoxResults(HttpServletRequest request, Connection c, List<ResultsByUrlDto> results) throws Exception {
        long idExecution = Long.parseLong(request.getParameter(Constants.ID));
        List<DetalleDto> detailAnalyseList = new ArrayList<DetalleDto>();

        for (ResultsByUrlDto analyse : results) {
            DetalleDto detalleDto = new DetalleDto();
            detalleDto.setUrl(analyse.getUrl());
            detalleDto.setRastreo(new RastreoExtDto());
            detalleDto.getRastreo().setIdRastreo(idExecution);
            RastreoEjecutadoForm rastreo = RastreoDAO.cargarRastreoEjecutado(c, idExecution);

            if (analyse.getNumTerms() != 0) {
                InformesService informesService = new InformesService();
                DetalleDto detalleDtoObtenido = informesService.obtenerDetalle(detalleDto, Constants.NO_PAGINACION);
                detalleDtoObtenido.getRastreo().setEntidad(rastreo.getNombre_rastreo());
                detalleDtoObtenido.getRastreo().setFecha(rastreo.getFecha());
                detailAnalyseList.add(detalleDtoObtenido);
            } else {
                detalleDto.getRastreo().setEntidad(rastreo.getNombre_rastreo());
                detalleDto.getRastreo().setFecha(rastreo.getFecha());
                detailAnalyseList.add(detalleDto);
            }
        }
        return detailAnalyseList;
    }

    private static Chapter globalLenoxChapter(List<DetalleDto> detailAnalyseList, HttpServletRequest request,
                                              IndexEvents index, String globalPath) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();
        BaseFont arial = BaseFont.createFont(pmgr.getValue("pdf.properties", "path.pdf.font"), BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        Chunk chunkCh1 = new Chunk(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.index.global.summary"));
        chunkCh1.setLocalDestination(Constants.ANCLA_PDF + "0");
        Paragraph cTitleCh1 = new Paragraph("", new Font(arial, 18, Font.UNDERLINE, Constants.ROJO_INTECO));
        cTitleCh1.add(chunkCh1);
        cTitleCh1.setSpacingAfter(2 * ConstantsFont.SPACE_LINE);

        Chapter chapter1 = new Chapter(cTitleCh1, 1);
        chapter1.setNumberDepth(0);
        cTitleCh1.add(index.create(" ", CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.index.global.summary")));

        Paragraph entity = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.entity") + detailAnalyseList.get(0).getRastreo().getEntidad(), new Font(arial, 16, Font.BOLD, Color.black));
        entity.setSpacingAfter(ConstantsFont.SPACE_LINE);
        chapter1.add(entity);

        int locatedTerm = 0;
        int totalTerm = 0;
        int highPriorityTerm = 0;
        int mediumPriorityTerm = 0;
        int lowPriorityTerm = 0;

        for (DetalleDto analyse : detailAnalyseList) {
            locatedTerm += analyse.getRastreo().getNumTerminosLocalizados();
            totalTerm += analyse.getRastreo().getNumTerminosOcurrentes();
            highPriorityTerm += analyse.getRastreo().getNumTerminosPrioridadAlta();
            mediumPriorityTerm += analyse.getRastreo().getNumTerminosPrioridadMedia();
            lowPriorityTerm += analyse.getRastreo().getNumTerminosPrioridadBaja();
        }

        DetalleDto detalle = new DetalleDto();
        RastreoExtDto rastreo = new RastreoExtDto();

        rastreo.setNumTerminosLocalizados(locatedTerm);
        rastreo.setNumTerminosOcurrentes(totalTerm);
        rastreo.setNumTerminosPrioridadAlta(highPriorityTerm);
        rastreo.setNumTerminosPrioridadMedia(mediumPriorityTerm);
        rastreo.setNumTerminosPrioridadBaja(lowPriorityTerm);
        rastreo.setFecha(detailAnalyseList.get(0).getRastreo().getFecha());

        detalle.setRastreo(rastreo);

        chapter1.add(addLenoxSummary(detalle, request, true));

        generateLenoxGraphic(detailAnalyseList, request, CrawlerUtils.getResources(request).getMessage("pdf.lenox.global.indicator"), globalPath);
        addImage(request, globalPath, chapter1);

        return chapter1;
    }


    private static Chapter addImage(HttpServletRequest request, String globalPath, Chapter chapter) {
        Image globalImgGp = null;
        try {
            globalImgGp = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.lenox.global.indicator") + ".jpg");
            globalImgGp.setAlt(CrawlerUtils.getResources(request).getMessage("pdf.lenox.global.indicator"));
        } catch (Exception e) {
            Logger.putLog("Exception", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
        chapter.add(globalImgGp);
        return chapter;
    }

    private static void generateLenoxGraphic(List<DetalleDto> detailAnalyseList, HttpServletRequest request, String tableName, String tempPath) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        DefaultPieDataset dataSet = new DefaultPieDataset();

        for (DetalleDto detalle : detailAnalyseList) {
            if (result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.high")) == null) {
                result.put(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.high"), detalle.getRastreo().getNumTerminosPrioridadAlta());
            } else {
                result.put(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.high"), result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.high")) + detalle.getRastreo().getNumTerminosPrioridadAlta());
            }

            if (result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.medium")) == null) {
                result.put(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.medium"), detalle.getRastreo().getNumTerminosPrioridadMedia());
            } else {
                result.put(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.medium"), result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.medium")) + detalle.getRastreo().getNumTerminosPrioridadMedia());
            }

            if (result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.low")) == null) {
                result.put(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.low"), detalle.getRastreo().getNumTerminosPrioridadBaja());
            } else {
                result.put(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.low"), result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.low")) + detalle.getRastreo().getNumTerminosPrioridadBaja());
            }
        }

        for (Map.Entry<String, Integer> resultEntry : result.entrySet()) {
            dataSet.setValue(resultEntry.getKey(), resultEntry.getValue());
        }

        GraphicsUtils.totalPageStr = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.term");
        GraphicsUtils.totalPage = result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.high")) + result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.medium")) + result.get(CrawlerUtils.getResources(request).getMessage("pdf.lenox.graphic.low"));

        try {
            printLenoxChart(tableName, tempPath, dataSet, request);
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static com.lowagie.text.List addLenoxSummary(DetalleDto analyse, HttpServletRequest request, boolean isGlobal) throws Exception {
        com.lowagie.text.List summary = new com.lowagie.text.List();

        PropertiesManager pmgr = new PropertiesManager();
        Image img = Image.getInstance(pmgr.getValue("pdf.properties", "path.lenox.pdf.list"));
        img.setAlt("");

        if (isGlobal) {
            DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.complet.format"));
            Date fecha = df.parse(analyse.getRastreo().getFecha());
            Paragraph title = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.date") + ": ", ConstantsFont.titleLenoxFont);
            df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
            Paragraph value = new Paragraph(df.format(fecha), ConstantsFont.valueLenoxFont);
            Phrase phrase = new Phrase(title);
            phrase.add(value);
            ListItem date = new ListItem(phrase);
            date.setListSymbol(new Chunk(img, -5, 0));
            summary.add(date);
        }

        Paragraph title = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.located.terms") + ": ", ConstantsFont.titleLenoxFont);
        Paragraph value = new Paragraph(analyse.getRastreo().getNumTerminosLocalizados().toString(), ConstantsFont.valueLenoxFont);
        Phrase phrase = new Phrase(title);
        phrase.add(value);
        ListItem locatedTerms = new ListItem(phrase);

        title = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.total.terms") + ": ", ConstantsFont.titleLenoxFont);
        value = new Paragraph(analyse.getRastreo().getNumTerminosOcurrentes().toString(), ConstantsFont.valueLenoxFont);
        phrase = new Phrase(title);
        phrase.add(value);
        ListItem totalTerms = new ListItem(phrase);

        title = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.high.priority.terms") + ": ", ConstantsFont.titleLenoxFont);
        value = new Paragraph(analyse.getRastreo().getNumTerminosPrioridadAlta().toString(), ConstantsFont.valueLenoxFont);
        phrase = new Phrase(title);
        phrase.add(value);
        ListItem highPriorityTerms = new ListItem(phrase);

        title = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.medium.priority.terms") + ": ", ConstantsFont.titleLenoxFont);
        value = new Paragraph(analyse.getRastreo().getNumTerminosPrioridadMedia().toString(), ConstantsFont.valueLenoxFont);
        phrase = new Phrase(title);
        phrase.add(value);
        ListItem mediumPriorityTerms = new ListItem(phrase);

        title = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.low.priority.terms") + ": ", ConstantsFont.titleLenoxFont);
        value = new Paragraph(analyse.getRastreo().getNumTerminosPrioridadBaja().toString(), ConstantsFont.valueLenoxFont);
        phrase = new Phrase(title);
        phrase.add(value);
        ListItem lowPriorityTerms = new ListItem(phrase);

        locatedTerms.setListSymbol(new Chunk(img, -5, 0));
        totalTerms.setListSymbol(new Chunk(img, -5, 0));
        highPriorityTerms.setListSymbol(new Chunk(img, -5, 0));
        mediumPriorityTerms.setListSymbol(new Chunk(img, -5, 0));
        lowPriorityTerms.setListSymbol(new Chunk(img, -5, 0));
        lowPriorityTerms.setSpacingAfter(3 * ConstantsFont.SPACE_LINE);

        summary.add(locatedTerms);
        summary.add(totalTerms);
        summary.add(highPriorityTerms);
        summary.add(mediumPriorityTerms);
        summary.add(lowPriorityTerms);

        return summary;
    }

    private static void addLenoxContext(Chapter chapter, List<ResultadoDto> results, HttpServletRequest request) {

        int i = 0;
        PropertiesManager pmgr = new PropertiesManager();

        float[] widths = {0.15f, 0.85f};
        PdfPTable table = new PdfPTable(widths);
        table.setSpacingAfter(ConstantsFont.SPACE_LINE);
        PdfPCell labelCell = new PdfPCell(new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.priority"), ConstantsFont.labelCellFont));
        labelCell.setBackgroundColor(Constants.ROJO_INTECO);
        table.addCell(labelCell);

        labelCell = new PdfPCell(new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.context"), ConstantsFont.labelCellFont));
        labelCell.setBackgroundColor(Constants.ROJO_INTECO);
        table.addCell(labelCell);

        for (ResultadoDto context : results) {
            if (i < Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "max.lenox.context"))) {
                String priority = "";
                if (Integer.parseInt(context.getGravedad()) == Constants.HIGH_LENOX_PRIORITY) {
                    priority = CrawlerUtils.getResources(request).getMessage("lenox.crawling.priority.high");
                } else if (Integer.parseInt(context.getGravedad()) == Constants.MEDIUM_LENOX_PRIORITY) {
                    priority = CrawlerUtils.getResources(request).getMessage("lenox.crawling.priority.medium");
                } else if (Integer.parseInt(context.getGravedad()) == Constants.LOW_LENOX_PRIORITY) {
                    priority = CrawlerUtils.getResources(request).getMessage("lenox.crawling.priority.low");
                }

                table.addCell(new Paragraph(priority, ConstantsFont.priorityCellFont));
                table.addCell(parseContext(context.getContexto()));
                i++;
            }
        }
        chapter.add(table);
    }

    private static Paragraph parseContext(String context) {

        Paragraph finalContext = new Paragraph();
        Phrase p = null;

        String[] arrayText = context.split("</{0,1}strong>");

        for (int i = 0; i < arrayText.length; i++) {
            if (i % 2 != 0) {
                p = new Phrase(arrayText[i], ConstantsFont.lenoxContextRedFont);
            } else {
                p = new Phrase(arrayText[i], ConstantsFont.lenoxContextBlackFont);
            }
            finalContext.add(p);
        }
        return finalContext;
    }

    private static void addLenoxSuggestions(Chapter chapter, List<SugerenciaDto> results, HttpServletRequest request) throws Exception {

        Paragraph head = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.lenox.suggestions") + ": ", ConstantsFont.titleLenoxFont);
        Paragraph p = new Paragraph(head);
        p.setSpacingAfter(ConstantsFont.SPACE_LINE);
        chapter.add(p);

        com.lowagie.text.List summary = new com.lowagie.text.List();
        PropertiesManager pmgr = new PropertiesManager();
        Image img = Image.getInstance(pmgr.getValue("pdf.properties", "path.lenox.pdf.list"));
        img.setAlt("");

        for (SugerenciaDto suggestion : results) {
            Paragraph value = new Paragraph(suggestion.getAlternativa() + " : " + suggestion.getDescripcion(), ConstantsFont.valueLenoxFont);
            Paragraph pg = new Paragraph(value);
            ListItem suggestionItem = new ListItem(pg);
            suggestionItem.setListSymbol(new Chunk(img, -5, 0));
            summary.add(suggestionItem);
        }

        chapter.add(summary);
    }

    private static void printLenoxChart(String chartName, String chartsPath, DefaultPieDataset dataSet,
                                        HttpServletRequest request) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        String file = chartsPath + chartName.replaceAll("https*://", "")
                .replaceAll(pmgr.getValue(CRAWLER_PROPERTIES, "path.chart.file.replace.from"),
                        pmgr.getValue(CRAWLER_PROPERTIES, "path.chart.file.replace.to")) + ".jpg";

        GraphicsUtils.createPieChart(dataSet, chartName, file, CrawlerUtils.getResources(request).getMessage("grafica.sin.datos"), color, x, y);
    }
}
