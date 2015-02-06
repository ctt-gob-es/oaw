package es.inteco.rastreador2.pdf;

import com.lowagie.text.Chapter;
import com.lowagie.text.Element;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPTable;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class AnonymousResultExportPdfSection4 {

    private AnonymousResultExportPdfSection4() {
    }

    protected static void createChapter4(HttpServletRequest request, Chapter chapter) {
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.4.p1"), ConstantsFont.paragraphFont, chapter);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.4.p2"), ConstantsFont.paragraphFont, chapter);
    }

    protected static void createSection41(HttpServletRequest request, Section section, String graphicPath, Map<String, Integer> res, long observatoryType) throws Exception {

        if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.41.p1.AGE"), ConstantsFont.paragraphFont, section);
        } else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.41.p1.CCAA"), ConstantsFont.paragraphFont, section);
        }

        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accessibility.level.allocation.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.41.img.alt"), 75);

        java.util.List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(request, res);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.41.tableTitle"), section, 380);
        float[] widths = {33f, 33f, 33f};
        PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.porc.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        for (GraphicData label : labelValueBean) {
            table.addCell(PDFUtils.createTableCell(label.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(label.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }
        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        section.add(table);

        //section.add(AnonymousResultExportPdfVariableText.createVTextS41(request, res));
    }

    protected static void createSection42(HttpServletRequest request, Section section, String graphicPath, String id_execution,
                                          java.util.List<ObservatoryEvaluationForm> pageExecutionList, java.util.List<CategoriaForm> categories, long observatoryType) throws Exception {

        Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageResultsBySegmentMap(id_execution, pageExecutionList, categories);
        PropertiesManager pmgr = new PropertiesManager();
        int numBarByGrapg = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.max.bar.graph"));
        int numGraph = categories.size() / numBarByGrapg;
        if (categories.size() % numBarByGrapg != 0) {
            numGraph++;
        }

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.42.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.42.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.42.p5"), ConstantsFont.paragraphFont, section);

        if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
            PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.42.p6"), ConstantsFont.paragraphFont, section);
        }

        for (int i = 1; i <= numGraph; i++) {
            PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.name") + i + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.42.img.alt"), 75);
        }

        section.newPage();

        for (CategoriaForm category : categories) {
            PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.42.tableTitle", category.getName()), section, 380);
            java.util.List<LabelValueBean> results = ResultadosAnonimosObservatorioIntavUtils.infoComparisonBySegment(request, res.get(category));
            for (LabelValueBean label : results) {
                label.setValue(label.getValue() + "%");
            }
            java.util.List<String> headers = new ArrayList<String>();
            headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.level"));
            headers.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.42.resT"));
            section.add(PDFUtils.createResultTable(results, headers));
        }
        //Los textos variables para esta seccion ya no tienen mucho sentido
        //AnonymousResultExportPdfVariableText.createVTextS42(request, res, section);

    }

    protected static void createSection43(HttpServletRequest request, Section section, String graphicPath, String execution_id,
                                          String observatory_id, java.util.List<ObservatoryEvaluationForm> pageExecutionList, java.util.List<CategoriaForm> categories) throws Exception {

        Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioIntavUtils.calculateMidPuntuationResultsBySegmentMap(execution_id, pageExecutionList, categories);
        PropertiesManager pmgr = new PropertiesManager();
        int numBarByGrapg = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.max.bar.graph"));
        int numGraph = categories.size() / numBarByGrapg;
        if (categories.size() % numBarByGrapg != 0) {
            numGraph++;
        }

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.43.p1"), ConstantsFont.paragraphFont, section);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.43.p4.anchor"), CrawlerUtils.getResources(request).getMessage("anchor.PMPO"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.43.p4"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.43.p5"), ConstantsFont.paragraphFont, section);
        for (int i = 1; i <= numGraph; i++) {
            PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.name") + i + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.43.img.alt"), 75);
        }

        section.newPage();

        //Los textos variables para esta seccion ya no tienen mucho sentido
        //AnonymousResultExportPdfVariableText.createVTextS43(request, res, section);
        final MessageResources resources = CrawlerUtils.getResources(request);
        for (CategoriaForm category : categories) {
            PDFUtils.createTitleTable(resources.getMessage("ob.resAnon.intav.report.43.tableTitle", category.getName()), section, 300);
            java.util.List<LabelValueBean> results = ResultadosAnonimosObservatorioIntavUtils.infoComparisonBySegmentPuntuation(resources, res.get(category));
            java.util.List<String> headers = new ArrayList<String>();
            headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.level"));
            headers.add(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.43.resT"));
            section.add(PDFUtils.createResultTable(results, headers));
        }
    }

    protected static void createSection44(HttpServletRequest request, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {

        Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
        Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.p2"), ConstantsFont.paragraphFont, section);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.p7.anchor"), CrawlerUtils.getResources(request).getMessage("anchor.PMV"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.p7"), anchorMap, ConstantsFont.paragraphFont));

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.p8"), ConstantsFont.paragraphFont, section);

        section.newPage();
        Section subSection = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.sub1"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.img.alt"), 75);

        java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(request, resultL1);
        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.tableTitle1"), subSection, 380);
        java.util.List<String> headers = new ArrayList<String>();
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.punto.verification"));
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.punt.media"));
        PdfPTable table = PDFUtils.createResultTable(labelsL1, headers);
        subSection.add(table);

        Section subSection2 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.sub2"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection2, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.img.alt.2"), 75);

        java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(request, resultL2);
        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.44.tableTitle2"), subSection2, 380);
        subSection2.add(PDFUtils.createResultTable(labelsL2, headers));

    }

    protected static void createSection45(HttpServletRequest request, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {

        Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
        Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);

        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.p1"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.p2"), ConstantsFont.paragraphFont, section);
        PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.p3"), ConstantsFont.paragraphFont, section);

        section.newPage();
        Section subSection = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.sub1"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.1.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.img1.alt"), 75);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.tableTitle1"), subSection, 380);
        section.add(PDFUtils.createTableMod(request, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results1)));

        section.newPage();
        Section subSection2 = PDFUtils.addSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.sub2"), null, ConstantsFont.chapterTitleMPFont3L, section, -1, 2);
        PDFUtils.addImageToSection(subSection2, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.2.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.img2.alt"), 75);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.45.tableTitle2"), subSection2, 380);
        section.add(PDFUtils.createTableMod(request, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results2)));
    }

    protected static void createSection46(HttpServletRequest request, Section section, String graphicPath, java.util.List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {

        Map<String, BigDecimal> result = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(request, pageExecutionList);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<Integer, SpecialChunk>();
        SpecialChunk anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p12.ancla"), CrawlerUtils.getResources(request).getMessage("anchor.PMA"), false, ConstantsFont.paragraphAnchorFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.p12"), anchorMap, ConstantsFont.paragraphFont));
        PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.aspect.mid.name") + ".jpg", CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.img.alt"), 75);

        java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoAspectMidsComparison(request, result);
        //AnonymousResultExportPdfVariableText.createVText425(request, labels, section);

        PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.tableTitle"), section, 380);

        //section.add(PDFUtils.addResultsList(request, labels, CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.46.resT")));
        java.util.List<String> headers = new ArrayList<String>();
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.aspect"));
        headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.punt.media"));
        section.add(PDFUtils.createResultTable(labels, headers));
    }

}
