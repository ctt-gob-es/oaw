package es.inteco.rastreador2.openOffice.export;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import org.apache.struts.util.LabelValueBean;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.Constants.PDF_PROPERTIES;

public final class ExportOpenOfficeUtils {

    private ExportOpenOfficeUtils() {
    }

    public static void createOpenOfficeDocument(HttpServletRequest request, String filePath, String graphicPath, String date, Long obsType, int numObs) {
        try {
            PropertiesManager pmgr = new PropertiesManager();
            OdfTextDocument odt;
            if (obsType == Constants.OBSERVATORY_TYPE_AGE) {
                odt = (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.AGE"));
            } else if (obsType == Constants.OBSERVATORY_TYPE_CCAA) {
                odt = (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.CCAA"));
            } else if (obsType == Constants.OBSERVATORY_TYPE_EELL) {
                odt = (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.EELL"));
            } else {
                odt = (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.AGE"));
            }

            if (numObs < Integer.parseInt(pmgr.getValue(PDF_PROPERTIES, "pdf.anonymous.results.pdf.min.obser"))) {
                addDocumentFields(request, odt, graphicPath, date, false);
            } else {
                addDocumentFields(request, odt, graphicPath, date, true);
            }

            odt.save(filePath);

            removeAttributeFromFile(filePath, "META-INF/manifest.xml", "manifest:file-entry", "manifest:size", "text/xml");

            odt.close();
        } catch (Exception e) {
            Logger.putLog("Error al exportar los resultados a OpenOffice", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

    }

    private static void addDocumentFields(HttpServletRequest request, OdfTextDocument odt, String graphicPath, String date, boolean evolution) throws Exception {
        OdfFileDom odfFileContent = odt.getContentDom();
        OdfFileDom odfStyles = odt.getStylesDom();
        int numImg = 8;

        List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(request.getParameter(Constants.ID), Constants.COMPLEXITY_SEGMENT_NONE, null);
        Connection c = null;

        try {
            c = DataBaseManager.getConnection();
            List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, Long.valueOf(request.getParameter(Constants.ID)));

            replaceText(odt, odfFileContent, "[fecha]", date);
            replaceText(odt, odfStyles, "[fecha]", date, "text:span");

            numImg = replaceSection41(request, odt, odfFileContent, graphicPath, pageExecutionList, numImg);
            numImg = replaceSection42(request, odt, odfFileContent, graphicPath, categories, pageExecutionList, numImg);
            numImg = replaceSection43(request, odt, odfFileContent, graphicPath, categories, pageExecutionList, numImg);
            numImg = replaceSection441(request, odt, odfFileContent, graphicPath, pageExecutionList, numImg);
            numImg = replaceSection442(request, odt, odfFileContent, graphicPath, pageExecutionList, numImg);
            numImg = replaceSection451(request, odt, odfFileContent, graphicPath, pageExecutionList, numImg);
            numImg = replaceSection452(request, odt, odfFileContent, graphicPath, pageExecutionList, numImg);
            numImg = replaceSection46(request, odt, odfFileContent, graphicPath, pageExecutionList, numImg);

            int numSection = 5;
            for (CategoriaForm category : categories) {
                List<ObservatoryEvaluationForm> pageExecutionListCat = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(request.getParameter(Constants.ID), Long.parseLong(category.getId()), null);
                numImg = replaceSectionCat1(request, odt, odfFileContent, graphicPath, category, numSection, pageExecutionListCat, numImg);
                numImg = replaceSectionCat2(request, odt, odfFileContent, graphicPath, category, numSection, pageExecutionListCat, numImg);
                numImg = replaceSectionCat31(request, odt, odfFileContent, graphicPath, category, numSection, pageExecutionListCat, numImg);
                numImg = replaceSectionCat32(request, odt, odfFileContent, graphicPath, category, numSection, pageExecutionListCat, numImg);
                numImg = replaceSectionCat41(request, odt, odfFileContent, graphicPath, category, numSection, pageExecutionListCat, numImg);
                numImg = replaceSectionCat42(request, odt, odfFileContent, graphicPath, category, numSection, pageExecutionListCat, numImg);
                numImg = replaceSectionCat5(request, odt, odfFileContent, graphicPath, category, numSection, pageExecutionListCat, numImg);
                ++numSection;
            }

            if (evolution) {
                Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioIntavUtils.resultEvolutionData(Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO)), Long.valueOf(request.getParameter(Constants.ID)));
                Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<Date, Map<String, BigDecimal>>();
                for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
                    resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(request, entry.getValue()));
                }
                numImg = replaceSectionEv1(request, odt, odfFileContent, graphicPath, pageObservatoryMap, numImg, numSection);
                numImg = replaceSectionEv2(request, odt, odfFileContent, graphicPath, pageObservatoryMap, numImg, numSection);
                numImg = replaceSectionEv3(request, odt, odfFileContent, graphicPath, pageObservatoryMap, numImg, numSection);
                numImg = replaceSectionEv4(request, odt, odfFileContent, graphicPath, resultsByAspect, numImg, numSection);
            }

        } catch (Exception e) {
            Logger.putLog("Error al recuperar categorias", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

    }

    private static int replaceSection41(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accessibility.level.allocation.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;
        Map<String, Integer> result = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
        List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(request, result);
        replaceText(odt, odfFileContent, "-41.t1.b2-", labelValueBean.get(0).getPercentageP());
        replaceText(odt, odfFileContent, "-41.t1.b3-", labelValueBean.get(1).getPercentageP());
        replaceText(odt, odfFileContent, "-41.t1.b4-", labelValueBean.get(2).getPercentageP());
        replaceText(odt, odfFileContent, "-41.t1.c2-", labelValueBean.get(0).getNumberP());
        replaceText(odt, odfFileContent, "-41.t1.c3-", labelValueBean.get(1).getNumberP());
        replaceText(odt, odfFileContent, "-41.t1.c4-", labelValueBean.get(2).getNumberP());

        return numImg;
    }

    private static int replaceSection42(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<CategoriaForm> categories, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioIntavUtils.createGraphicsMap(categories);
        for (Integer i : resultLists.keySet()) {
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.name") + i + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }
        Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageResultsBySegmentMap(request.getParameter(Constants.ID), pageExecutionList, categories);
        int tableNum = 1;
        for (CategoriaForm category : categories) {
            java.util.List<LabelValueBean> results = ResultadosAnonimosObservatorioIntavUtils.infoComparisonBySegment(request, res.get(category));
            replaceText(odt, odfFileContent, "-42.t" + tableNum + ".b2-", results.get(0).getValue() + "%");
            replaceText(odt, odfFileContent, "-42.t" + tableNum + ".b3-", results.get(1).getValue() + "%");
            replaceText(odt, odfFileContent, "-42.t" + tableNum + ".b4-", results.get(2).getValue() + "%");
            ++tableNum;
        }

        return numImg;
    }

    private static int replaceSection43(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<CategoriaForm> categories, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioIntavUtils.createGraphicsMap(categories);
        for (Integer i : resultLists.keySet()) {
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.name") + i + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
        }
        Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioIntavUtils.calculateMidPuntuationResultsBySegmentMap(request.getParameter(Constants.ID), pageExecutionList, categories);
        int tableNum = 1;
        for (CategoriaForm category : categories) {
            List<LabelValueBean> results = ResultadosAnonimosObservatorioIntavUtils.infoComparisonBySegmentPuntuation(request, res.get(category));
            replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b2-", results.get(0).getValue());
            replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b3-", results.get(1).getValue());
            replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b4-", results.get(2).getValue());
            ++tableNum;
        }

        return numImg;
    }

    private static int replaceSection441(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
        List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(request, resultL1);

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

    private static int replaceSection442(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
        List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(request, resultL2);

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

    private static int replaceSection451(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.1.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
        List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results1);

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

    private static int replaceSection452(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.modality.by.verification.level.2.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
        List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results2);

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

    private static int replaceSection46(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.aspect.mid.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
        numImg++;

        Map<String, BigDecimal> result = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(request, pageExecutionList);
        List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoAspectMidsComparison(request, result);

        replaceText(odt, odfFileContent, "-46.t1.b2-", labels.get(0).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b3-", labels.get(1).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b4-", labels.get(2).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b5-", labels.get(3).getValue());
        replaceText(odt, odfFileContent, "-46.t1.b6-", labels.get(4).getValue());

        return numImg;
    }

    private static int replaceSectionCat1(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, int numSection, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {

        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
            java.util.List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(request, resultsMap);

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

    private static int replaceSectionCat2(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, int numSection, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
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

    private static int replaceSectionCat31(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, int numSection, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);

        if (!pageExecutionList.isEmpty()) {
            List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(request, resultL1);

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

    private static int replaceSectionCat32(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, int numSection, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);

        if (!pageExecutionList.isEmpty()) {
            List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(request, resultL2);

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

    private static int replaceSectionCat41(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, int numSection, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
            List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results1);
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

    private static int replaceSectionCat42(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, int numSection, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
            List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results2);
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

    private static int replaceSectionEv1(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList, int numImg, int numSection) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioIntavUtils.getEvolutionObservatoriesSitesByType(request, pageExecutionList);
            Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A);
            Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA);
            Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV);

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            int index = 2;
            for (Map.Entry<String, BigDecimal> entry : resultDataAA.entrySet()) {
                replaceText(odt, odfFileContent, "-" + numSection + "1.t1.a" + index + "-", entry.getKey());
                replaceText(odt, odfFileContent, "-" + numSection + "1.t1.b" + index++ + "-", entry.getValue().toString().replace(".00", "") + "%");
            }

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            index = 2;
            for (Map.Entry<String, BigDecimal> entry : resultDataA.entrySet()) {
                replaceText(odt, odfFileContent, "-" + numSection + "2.t1.a" + index + "-", entry.getKey());
                replaceText(odt, odfFileContent, "-" + numSection + "2.t1.b" + index++ + "-", entry.getValue().toString().replace(".00", "") + "%");
            }

            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            index = 2;
            for (Map.Entry<String, BigDecimal> entry : resultDataNV.entrySet()) {
                replaceText(odt, odfFileContent, "-" + numSection + "3.t1.a" + index + "-", entry.getKey());
                replaceText(odt, odfFileContent, "-" + numSection + "3.t1.b" + index++ + "-", entry.getValue().toString().replace(".00", "") + "%");
            }
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

    private static int replaceSectionEv2(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList, int numImg, int numSection) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateEvolutionPuntuationDataSet(pageExecutionList);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;

            replaceTextCellTables(odt, odfFileContent, numSection + "4.t1", resultData);
        } else {
            PropertiesManager pmgr = new PropertiesManager();
            replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            numImg++;
        }
        return numImg;
    }

    private static int replaceSectionEv3(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, int numImg, int numSection) throws Exception {
        if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
            Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "5.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "6.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "7.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1.4") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "8.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "9.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "10.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "11.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.4") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "12.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.5") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "13.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2.6") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "14.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "15.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "16.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "17.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.1.4") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "18.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.1") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "19.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.2") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "20.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.3") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "21.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.4") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "22.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.5") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "23.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION, pageObservatoryMap);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2.6") + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "24.t1", resultData);
        } else {
            PropertiesManager pmgr = new PropertiesManager();

            for (int i = 5; i < 25; i++) {
                replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
                numImg++;
            }
        }
        return numImg;
    }

    private static int replaceSectionEv4(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, Map<Date, Map<String, BigDecimal>> resultsByAspect, int numImg, int numSection) throws Exception {
        if (resultsByAspect != null && !resultsByAspect.isEmpty()) {
            Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.general"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "25.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.presentation"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "26.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.structure"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "27.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.navigation"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "28.t1", resultData);

            resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(CrawlerUtils.getResources(request).getMessage("observatory.aspect.alternatives"), resultsByAspect);
            replaceImg(odt, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID) + ".jpg", "media/image" + numImg + ".jpeg", "image/jpeg");
            numImg++;
            replaceTextCellTables(odt, odfFileContent, numSection + "28.t1", resultData);
        } else {
            PropertiesManager pmgr = new PropertiesManager();

            for (int i = 25; i < 30; i++) {
                replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "media/image" + numImg + ".jpeg", "image/jpeg");
                numImg++;
            }
        }
        return numImg;
    }

    private static int replaceSectionCat5(HttpServletRequest request, OdfTextDocument odt, OdfFileDom odfFileContent, String graphicPath, CategoriaForm category, int numSection, List<ObservatoryEvaluationForm> pageExecutionList, int numImg) throws Exception {
        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            Map<String, BigDecimal> result = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(request, pageExecutionList);
            List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoAspectMidsComparison(request, result);

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

    private static void replaceText(OdfTextDocument odt, OdfFileDom odfFileContent, String oldText, String newText, String nodeStr) throws XPathExpressionException {
        XPath xpath = odt.getXPath();
        DTMNodeList nodeList = (DTMNodeList) xpath.evaluate(String.format("//%s[contains(text(),'%s')]", nodeStr, oldText), odfFileContent, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            OdfElement node = (OdfElement) nodeList.item(i);
            node.setTextContent(node.getTextContent().replace(oldText, newText));
        }
    }

    private static void replaceText(OdfTextDocument odt, OdfFileDom odfFileContent, String oldText, String newText) throws XPathExpressionException {
        replaceText(odt, odfFileContent, oldText, newText, "text:p");
    }

    private static void replaceTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId, final Map<String, BigDecimal> resultData) throws XPathExpressionException {
        for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
            replaceText(odt, odfFileContent, "-" + rowId + ".a1-", entry.getKey());
            replaceText(odt, odfFileContent, "-" + rowId + ".b1-", entry.getValue().toString().replace(".00", ""));
        }
    }

    private static void replaceImg(OdfTextDocument odt, String newImagePath, String newImageName, String mymeType) throws Exception {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(newImagePath);
            odt.getPackage().insert(fin, newImageName, mymeType);
        } catch (Exception e) {
            Logger.putLog("Error al intentar reemplazar una imagen en el documento OpenOffice", ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
    }

    private static void removeAttributeFromFile(String doc, String xmlFile, String node, String attribute, String mymeType) throws Exception {
        OdfPackage odfPackageNew = OdfPackage.loadPackage(doc);
        Document packageDocument = odfPackageNew.getDom(xmlFile);

        NodeList nodeList = packageDocument.getElementsByTagName(node);
        for (int i = 0; i < nodeList.getLength(); i++) {
            ((Element) nodeList.item(i)).removeAttribute(attribute);
        }
        odfPackageNew.insert(packageDocument, xmlFile, mymeType);
        odfPackageNew.save(doc);
        odfPackageNew.close();
    }

}
