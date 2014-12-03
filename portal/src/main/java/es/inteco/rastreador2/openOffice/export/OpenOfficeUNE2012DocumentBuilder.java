package es.inteco.rastreador2.openOffice.export;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils;
import org.apache.struts.util.LabelValueBean;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase encargada de construir el documento OpenOffice con los resultados del observatorio usando la metodología UNE 2012
 */
public class OpenOfficeUNE2012DocumentBuilder implements OpenOfficeDocumentBuilder {
    private final String executionId;
    private final String observatoryId;
    private final Long tipoObservatorio;

    private int numImg = 8;
    private int numSection = 5;

    public OpenOfficeUNE2012DocumentBuilder(String executionId, String observatoryId, Long tipoObservatorio) {
        this.executionId = executionId;
        this.observatoryId = observatoryId;
        this.tipoObservatorio = tipoObservatorio;
    }

    public OdfTextDocument buildDocument(HttpServletRequest request, String graphicPath, String date, boolean evolution, List<ObservatoryEvaluationForm> pageExecutionList, List<CategoriaForm> categories) throws Exception {
        try {
            ResultadosAnonimosObservatorioUNE2012Utils.generateGraphics(request, graphicPath, Constants.MINISTERIO_P, true);
        } catch (Throwable t) {
            Logger.putLog("Error ResultadosAnonimosObservatorioUNE2012Utils.generateGraphics", OpenOfficeUNE2012DocumentBuilder.class, Logger.LOG_LEVEL_ERROR);
        }

        final OdfTextDocument odt = getOdfTemplate();
        final OdfFileDom odfFileContent = odt.getContentDom();
        final OdfFileDom odfStyles = odt.getStylesDom();

        replaceText(odt, odfFileContent, "[fecha]", date);
        replaceText(odt, odfStyles, "[fecha]", date, "text:span");

        replaceSection41(request, odt, odfFileContent, graphicPath, pageExecutionList);
        replaceSection42(request, odt, odfFileContent, graphicPath, categories, pageExecutionList);
        replaceSection43(request, odt, odfFileContent, graphicPath, categories, pageExecutionList);
        replaceSection441(request, odt, odfFileContent, graphicPath, pageExecutionList);
        replaceSection442(request, odt, odfFileContent, graphicPath, pageExecutionList);
        replaceSection451(request, odt, odfFileContent, graphicPath, pageExecutionList);
        replaceSection452(request, odt, odfFileContent, graphicPath, pageExecutionList);
        replaceSection46(request, odt, odfFileContent, graphicPath, pageExecutionList);

        for (CategoriaForm category : categories) {
            List<ObservatoryEvaluationForm> pageExecutionListCat = ResultadosAnonimosObservatorioUNE2012Utils.getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList);
            replaceSectionCat1(request, odt, odfFileContent, graphicPath, category, pageExecutionListCat);
            replaceSectionCat2(request, odt, odfFileContent, graphicPath, category, pageExecutionListCat);
            replaceSectionCat31(request, odt, odfFileContent, graphicPath, category, pageExecutionListCat);
            replaceSectionCat32(request, odt, odfFileContent, graphicPath, category, pageExecutionListCat);
            replaceSectionCat41(request, odt, odfFileContent, graphicPath, category, pageExecutionListCat);
            replaceSectionCat42(request, odt, odfFileContent, graphicPath, category, pageExecutionListCat);
            replaceSectionCat5(request, odt, odfFileContent, graphicPath, category, pageExecutionListCat);
            numSection++;
        }

        if (evolution) {
            Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioUNE2012Utils.resultEvolutionData(Long.valueOf(observatoryId), Long.valueOf(executionId));
            Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<Date, Map<String, BigDecimal>>();
            for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
                resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioUNE2012Utils.aspectMidsPuntuationGraphicData(request, entry.getValue()));
            }
            replaceSectionEv1(request, odt, odfFileContent, graphicPath, pageObservatoryMap);
            replaceSectionEv2(request, odt, odfFileContent, graphicPath, pageObservatoryMap);
            replaceSectionEv3(request, odt, odfFileContent, graphicPath, pageObservatoryMap);
            replaceSectionEv4(request, odt, odfFileContent, graphicPath, resultsByAspect);
        }
        return odt;
    }

    private int replaceSection41(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accessibility.level.allocation.name") + ".jpg", "Pictures/1000000000000235000001C632D687EB.png", "image/jpeg");

        numImg++;
        Map<String, Integer> result = ResultadosAnonimosObservatorioUNE2012Utils.getResultsBySiteLevel(pageExecutionList);
        List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNE2012Utils.infoGlobalAccessibilityLevel(request, result);
        replaceText(odt, odfFileContent, "-41.t1.b2-", labelValueBean.get(0).getPercentageP());
        replaceText(odt, odfFileContent, "-41.t1.b3-", labelValueBean.get(1).getPercentageP());
        replaceText(odt, odfFileContent, "-41.t1.b4-", labelValueBean.get(2).getPercentageP());
        replaceText(odt, odfFileContent, "-41.t1.c2-", labelValueBean.get(0).getNumberP());
        replaceText(odt, odfFileContent, "-41.t1.c3-", labelValueBean.get(1).getNumberP());
        replaceText(odt, odfFileContent, "-41.t1.c4-", labelValueBean.get(2).getNumberP());

        return numImg;
    }

    private int replaceSection42(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<CategoriaForm> categories, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNE2012Utils.createGraphicsMap(categories);
        for (Integer i : resultLists.keySet()) {
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.name") + i + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }
        Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNE2012Utils.calculatePercentageResultsBySegmentMap(executionId, pageExecutionList, categories);
        int tableNum = 1;
        for (CategoriaForm category : categories) {
            List<LabelValueBean> results = ResultadosAnonimosObservatorioUNE2012Utils.infoComparisonBySegment(request, res.get(category));
            replaceText(odt, odfFileContent, "-42.t" + tableNum + ".b2-", results.get(0).getValue() + "%");
            replaceText(odt, odfFileContent, "-42.t" + tableNum + ".b3-", results.get(1).getValue() + "%");
            replaceText(odt, odfFileContent, "-42.t" + tableNum + ".b4-", results.get(2).getValue() + "%");
            ++tableNum;
        }

        return numImg;
    }

    private int replaceSection43(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<CategoriaForm> categories, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNE2012Utils.createGraphicsMap(categories);
        for (Integer i : resultLists.keySet()) {
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.name") + i + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }
        Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNE2012Utils.calculateMidPuntuationResultsBySegmentMap(executionId, pageExecutionList, categories);
        int tableNum = 1;
        for (CategoriaForm category : categories) {
            List<LabelValueBean> results = ResultadosAnonimosObservatorioUNE2012Utils.infoComparisonBySegmentPuntuation(request, res.get(category));
            replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b2-", results.get(0).getValue());
            replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b3-", results.get(1).getValue());
            replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b4-", results.get(2).getValue());
            ++tableNum;
        }

        return numImg;
    }

    private int replaceSection441(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
        List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIVerificationMidsComparison(request, resultL1);

        replaceText(odt, odfFileContent, "-441.t1.b2-", labelsL1.get(0).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b3-", labelsL1.get(1).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b4-", labelsL1.get(2).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b5-", labelsL1.get(3).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b6-", labelsL1.get(4).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b7-", labelsL1.get(5).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b8-", labelsL1.get(6).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b9-", labelsL1.get(7).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b10-", labelsL1.get(8).getValue());
        replaceText(odt, odfFileContent, "-441.t1.b11-", labelsL1.get(9).getValue());

        return numImg;
    }

    private int replaceSection442(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
        List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIIVerificationMidsComparison(request, resultL2);

        replaceText(odt, odfFileContent, "-442.t1.b2-", labelsL2.get(0).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b3-", labelsL2.get(1).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b4-", labelsL2.get(2).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b5-", labelsL2.get(3).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b6-", labelsL2.get(4).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b7-", labelsL2.get(5).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b8-", labelsL2.get(6).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b9-", labelsL2.get(7).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b10-", labelsL2.get(8).getValue());
        replaceText(odt, odfFileContent, "-442.t1.b11-", labelsL2.get(9).getValue());

        return numImg;
    }

    private int replaceSection451(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.1.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
        List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelVerificationModalityComparison(results1);

        replaceText(odt, odfFileContent, "-451.t1.b2-", res.get(0).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c2-", res.get(0).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b3-", res.get(1).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c3-", res.get(1).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b4-", res.get(2).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c4-", res.get(2).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b5-", res.get(3).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c5-", res.get(3).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b6-", res.get(4).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c6-", res.get(4).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b7-", res.get(5).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c7-", res.get(5).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b8-", res.get(6).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c8-", res.get(6).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b9-", res.get(7).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c9-", res.get(7).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b10-", res.get(8).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c10-", res.get(8).getRedPercentage());
        replaceText(odt, odfFileContent, "-451.t1.b11-", res.get(9).getGreenPercentage());
        replaceText(odt, odfFileContent, "-451.t1.c11-", res.get(9).getRedPercentage());

        return numImg;
    }

    private int replaceSection452(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.2.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
        List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelVerificationModalityComparison(results2);

        replaceText(odt, odfFileContent, "-452.t1.b2-", res.get(0).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c2-", res.get(0).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b3-", res.get(1).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c3-", res.get(1).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b4-", res.get(2).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c4-", res.get(2).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b5-", res.get(3).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c5-", res.get(3).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b6-", res.get(4).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c6-", res.get(4).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b7-", res.get(5).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c7-", res.get(5).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b8-", res.get(6).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c8-", res.get(6).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b9-", res.get(7).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c9-", res.get(7).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b10-", res.get(8).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c10-", res.get(8).getRedPercentage());
        replaceText(odt, odfFileContent, "-452.t1.b11-", res.get(9).getGreenPercentage());
        replaceText(odt, odfFileContent, "-452.t1.c11-", res.get(9).getRedPercentage());

        return numImg;
    }

    private int replaceSection46(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.aspect.mid.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> result = ResultadosAnonimosObservatorioUNE2012Utils.aspectMidsPuntuationGraphicData(request, pageExecutionList);
        List<LabelValueBean> labels = ResultadosAnonimosObservatorioUNE2012Utils.infoAspectMidsComparison(request, result);

        replaceText(odt, odfFileContent, "-46.t1.b2-", labels.get(0).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b3-", labels.get(1).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b4-", labels.get(2).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b5-", labels.get(3).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b6-", labels.get(4).getValue());

        return numImg;
    }

    private int replaceSectionCat1(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {

        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioUNE2012Utils.getResultsBySiteLevel(pageExecutionList);
            List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNE2012Utils.infoGlobalAccessibilityLevel(request, resultsMap);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accessibility.level.allocation.segment.name", category.getId()) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;

            replaceText(odt, odfFileContent, "-" + numSection + "1.t1.b2-", labelValueBean.get(0).getPercentageP());
            replaceText(odt, odfFileContent, "-" + numSection + "1.t1.c2-", labelValueBean.get(0).getNumberP());
            replaceText(odt, odfFileContent, "-" + numSection + "1.t1.b3-", labelValueBean.get(1).getPercentageP());
            replaceText(odt, odfFileContent, "-" + numSection + "1.t1.c3-", labelValueBean.get(1).getNumberP());
            replaceText(odt, odfFileContent, "-" + numSection + "1.t1.b4-", labelValueBean.get(2).getPercentageP());
            replaceText(odt, odfFileContent, "-" + numSection + "1.t1.c4-", labelValueBean.get(2).getNumberP());
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }

        return numImg;
    }

    private int replaceSectionCat2(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.mark.allocation.segment.name", category.getId()) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }

        return numImg;
    }

    private int replaceSectionCat31(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);

        if (!pageExecutionList.isEmpty()) {
            List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIVerificationMidsComparison(request, resultL1);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + category.getId() + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b2-", labelsL1.get(0).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b3-", labelsL1.get(1).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b4-", labelsL1.get(2).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b5-", labelsL1.get(3).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b6-", labelsL1.get(4).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b7-", labelsL1.get(5).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b8-", labelsL1.get(6).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b9-", labelsL1.get(7).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b10-", labelsL1.get(8).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b11-", labelsL1.get(9).getValue());
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }

        return numImg;
    }

    private int replaceSectionCat32(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);

        if (!pageExecutionList.isEmpty()) {
            List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelIIVerificationMidsComparison(request, resultL2);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + category.getId() + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b2-", labelsL2.get(0).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b3-", labelsL2.get(1).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b4-", labelsL2.get(2).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b5-", labelsL2.get(3).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b6-", labelsL2.get(4).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b7-", labelsL2.get(5).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b8-", labelsL2.get(6).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b9-", labelsL2.get(7).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b10-", labelsL2.get(8).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b11-", labelsL2.get(9).getValue());
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }

        return numImg;
    }

    private int replaceSectionCat41(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
            List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelVerificationModalityComparison(results1);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.1.name") + category.getId() + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b2-", labels.get(0).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c2-", labels.get(0).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b3-", labels.get(1).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c3-", labels.get(1).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b4-", labels.get(2).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c4-", labels.get(2).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b5-", labels.get(3).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c5-", labels.get(3).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b6-", labels.get(4).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c6-", labels.get(4).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b7-", labels.get(5).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c7-", labels.get(5).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b8-", labels.get(6).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c8-", labels.get(6).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b9-", labels.get(7).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c9-", labels.get(7).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b10-", labels.get(8).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c10-", labels.get(8).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b11-", labels.get(9).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c11-", labels.get(9).getRedPercentage());
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }

        return numImg;
    }

    private int replaceSectionCat42(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
            List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNE2012Utils.infoLevelVerificationModalityComparison(results2);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.2.name") + category.getId() + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b2-", labels.get(0).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c2-", labels.get(0).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b3-", labels.get(1).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c3-", labels.get(1).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b4-", labels.get(2).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c4-", labels.get(2).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b5-", labels.get(3).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c5-", labels.get(3).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b6-", labels.get(4).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c6-", labels.get(4).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b7-", labels.get(5).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c7-", labels.get(5).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b8-", labels.get(6).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c8-", labels.get(6).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b9-", labels.get(7).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c9-", labels.get(7).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b10-", labels.get(8).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c10-", labels.get(8).getRedPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b11-", labels.get(9).getGreenPercentage());
            replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c11-", labels.get(9).getRedPercentage());
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }

        return numImg;
    }

    private int replaceSectionEv1(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            final Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioUNE2012Utils.getEvolutionObservatoriesSitesByType(request, pageExecutionList);
            final Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioUNE2012Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A);
            final Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioUNE2012Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA);
            final Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioUNE2012Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt,odfFileContent,numSection +"1.t1",resultDataAA, true);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt,odfFileContent,numSection +"2.t1",resultDataA, true);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt,odfFileContent,numSection +"3.t1",resultDataNV, true);
        } else {
            PropertiesManager pmgr = new PropertiesManager();

            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;

            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            numImg++;

            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            numImg++;
        }
        return numImg;
    }

    private int replaceSectionEv2(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateEvolutionPuntuationDataSet(pageExecutionList);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;

            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "4.t1", resultData);
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            numImg++;
        }
        return numImg;
    }

    private int replaceSectionEv3(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws Exception {
        if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
            Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "5.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "6.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "7.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.4") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "8.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_115_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.5") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "9.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_116_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.6") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "10.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_117_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.7") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "11.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "12.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "13.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "14.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "15.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "16.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "17.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.4") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "18.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_215_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.5") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "19.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_216_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.6") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "20.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_217_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.7") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "21.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "22.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "23.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "24.t1", resultData);
        } else {
            PropertiesManager pmgr = new PropertiesManager();

            for (int i = 5; i < 25; i++) {
                replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
                numImg++;
            }
        }
        return numImg;
    }

    private int replaceSectionEv4(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect) throws Exception {
        if (resultsByAspect != null && !resultsByAspect.isEmpty()) {
            Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.general"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "25.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.presentation"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "26.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.structure"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "27.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.navigation"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "28.t1", resultData);

            resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.alternatives"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "29.t1", resultData);
        } else {
            PropertiesManager pmgr = new PropertiesManager();

            for (int i = 25; i < 30; i++) {
                replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
                numImg++;
            }
        }
        return numImg;
    }

    private int replaceSectionCat5(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> result = ResultadosAnonimosObservatorioUNE2012Utils.aspectMidsPuntuationGraphicData(request, pageExecutionList);
            List<LabelValueBean> labels = ResultadosAnonimosObservatorioUNE2012Utils.infoAspectMidsComparison(request, result);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.aspect.mid.name") + category.getId() + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b2-", labels.get(0).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b3-", labels.get(1).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b4-", labels.get(2).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b5-", labels.get(3).getValue());
            replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b6-", labels.get(4).getValue());
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }

        return numImg;
    }

    private void replaceText(OdfTextDocument odt, OdfFileDom odfFileContent, String oldText, String newText, String nodeStr) throws XPathExpressionException {
        XPath xpath = odt.getXPath();
        DTMNodeList nodeList = (DTMNodeList) xpath.evaluate(String.format("//%s[contains(text(),'%s')]", nodeStr, oldText), odfFileContent, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            OdfElement node = (OdfElement) nodeList.item(i);
            node.setTextContent(node.getTextContent().replace(oldText, newText));
        }
    }

    private void replaceText(OdfTextDocument odt, OdfFileDom odfFileContent, String oldText, String newText) throws XPathExpressionException {
        replaceText(odt, odfFileContent, oldText, newText, "text:p");
    }

    private void replaceTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData) throws XPathExpressionException {
        for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
            replaceText(odt, odfFileContent, "-" + rowId + ".a1-", entry.getKey());
            replaceText(odt, odfFileContent, "-" + rowId + ".b1-", entry.getValue().toString().replace(".00", ""));
        }
    }

    private void replaceEvolutionTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData) throws XPathExpressionException {
        replaceEvolutionTextCellTables(odt,odfFileContent,rowId,resultData,false);
    }

    private void replaceEvolutionTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData, final boolean percentValue) throws XPathExpressionException {
        int index = 2;
        for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
            replaceText(odt, odfFileContent, "-"+rowId+".a" + index + "-", entry.getKey());
            replaceText(odt, odfFileContent, "-"+rowId+".b" + index++ + "-", entry.getValue().toString().replace(".00", "") + (percentValue?"%":""));
        }
        // Para el resto de la tabla borramos los placeholders para que al menos las celdas salgan vacías
        while (index<=6) {
            replaceText(odt, odfFileContent, "-"+rowId+".a" + index + "-", "");
            replaceText(odt, odfFileContent, "-"+rowId+".b" + index++ + "-", "");
        }
    }

    private void replaceImg(final OdfTextDocument odt, final String newImagePath, final String newImageName, final String mymeType) throws Exception {
        final File f = new File(newImagePath);
        final String embededName = OpenOfficeUNE2012ImageUtils.getEmbededIdImage(tipoObservatorio, f.getName());
        FileInputStream fin = null;

        try {
            fin = new FileInputStream(newImagePath);
            odt.getPackage().insert(fin, "Pictures/" + embededName, mymeType);
        } catch (Exception e) {
            Logger.putLog("Error al intentar reemplazar una imagen en el documento OpenOffice", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
    }

    private OdfTextDocument getOdfTemplate() throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        if (tipoObservatorio == Constants.OBSERVATORY_TYPE_AGE) {
            return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.une.2012.AGE"));
        } else if (tipoObservatorio == Constants.OBSERVATORY_TYPE_CCAA) {
            return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.une.2012.CCAA"));
        } else if (tipoObservatorio == Constants.OBSERVATORY_TYPE_EELL) {
            return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.une.2012.EELL"));
        } else {
            return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.une.2012.AGE"));
        }
    }

}
