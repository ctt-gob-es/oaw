package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.core.util.ConexionBBDD;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.lenox.dao.InformesDao;
import es.inteco.rastreador2.lenox.form.ObservatoryLenoxForm;
import org.apache.struts.util.LabelValueBean;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class ResultadosAnonimosObservatorioLenoxUtils {

    //GENERATE GRAPHIC METHODS
    static int x = 0;
    static int y = 0;

    static {
        PropertiesManager pmgr = new PropertiesManager();
        x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
        y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
    }

    private ResultadosAnonimosObservatorioLenoxUtils() {
    }

    public static int generateGlobalGraphics(HttpServletRequest request, String filePath) throws Exception {
        String execution_id = request.getParameter(Constants.ID);
        List<ObservatoryLenoxForm> pageExecutionList = getGlobalResultData(execution_id, Constants.COMPLEXITY_SEGMENT_NONE);
        List<ObservatoryLenoxForm> pageExecutionListSI = getGlobalResultData(execution_id, Constants.COMPLEXITY_SEGMENT_1);
        List<ObservatoryLenoxForm> pageExecutionListSII = getGlobalResultData(execution_id, Constants.COMPLEXITY_SEGMENT_2);
        List<ObservatoryLenoxForm> pageExecutionListSIII = getGlobalResultData(execution_id, Constants.COMPLEXITY_SEGMENT_3);

        if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
            String noDataMess = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");

            String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.title");
            String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.name") + ".jpg";
            getPercentagePriorityTermsGraphic(request, pageExecutionList, title, file, noDataMess);

            title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.percentage.priority.segment.terms.title");
            file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.percentage.priority.segment.terms.name") + ".jpg";
            getPercentagePrioritySegmentsTermsGraphic(request, pageExecutionListSI, pageExecutionListSII, pageExecutionListSIII, title, file, noDataMess);

            title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.number.priority.segment.terms.title");
            file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.number.priority.segment.terms.name") + ".jpg";
            getNumberPrioritySegmentsTermsGraphic(request, pageExecutionListSI, pageExecutionListSII, pageExecutionListSIII, title, file, noDataMess);

            return Constants.OBSERVATORY_HAVE_RESULTS;
        } else {
            return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
        }
    }

    public static int generateSegmentIGraphics(HttpServletRequest request, String filePath) throws Exception {
        String execution_id = request.getParameter(Constants.ID);
        List<ObservatoryLenoxForm> pageExecutionListSI = getGlobalResultData(execution_id, Constants.COMPLEXITY_SEGMENT_1);

        if (pageExecutionListSI != null && !pageExecutionListSI.isEmpty()) {
            String noDataMess = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");

            String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.s1.title");
            String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.percentage.priority.terms.s1.name") + ".jpg";
            getPercentagePriorityTermsGraphic(request, pageExecutionListSI, title, file, noDataMess);

            return Constants.OBSERVATORY_HAVE_RESULTS;
        } else {
            return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
        }
    }

    public static int generateEvolutionGraphics(HttpServletRequest request, String filePath) throws Exception {
        String execution_id = request.getParameter(Constants.ID);
        String observatory_id = request.getParameter(Constants.OBSERVATORY_ID);
        Map<Date, List<ObservatoryLenoxForm>> pageObservatoryMap = resultEvolutionData(Long.valueOf(observatory_id), Long.valueOf(execution_id));

        if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
            if (pageObservatoryMap.size() != 1) {
                String noDataMess = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");

                String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.high.term.priority.title");
                String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.high.term.priority.name") + ".jpg";
                getEvolutionTermPriorityGraphic(request, pageObservatoryMap, title, file, noDataMess, Constants.HIGH_LENOX_PRIORITY);

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.medium.term.priority.title");
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.medium.term.priority.name") + ".jpg";
                getEvolutionTermPriorityGraphic(request, pageObservatoryMap, title, file, noDataMess, Constants.MEDIUM_LENOX_PRIORITY);

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.low.term.priority.title");
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.low.term.priority.name") + ".jpg";
                getEvolutionTermPriorityGraphic(request, pageObservatoryMap, title, file, noDataMess, Constants.LOW_LENOX_PRIORITY);

                title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.percentage.priority.terms.title");
                file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.lenox.graphic.evolution.percentage.priority.terms.name") + ".jpg";
                getEvolutionPercentagePriorityTermGraphic(request, pageObservatoryMap, title, file, noDataMess);

                return Constants.OBSERVATORY_HAVE_ONE_RESULT;
            } else {
                return Constants.OBSERVATORY_HAVE_RESULTS;
            }
        } else {
            return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
        }
    }

    private static List<ObservatoryLenoxForm> getGlobalResultData(String execution_id, int complexity) throws Exception {
        Connection c = null;
        Connection conn = null;

        List<ObservatoryLenoxForm> lenoxResultList = new ArrayList<ObservatoryLenoxForm>();
        try {
            c = DataBaseManager.getConnection();
            conn = ConexionBBDD.conectar();
            List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(c, Long.parseLong(execution_id), complexity);
            if (listExecutionsIds != null && !listExecutionsIds.isEmpty()) {
                lenoxResultList = InformesDao.getObservatoryData(conn, listExecutionsIds);
                for (ObservatoryLenoxForm lenoxForm : lenoxResultList) {
                    lenoxForm.setTerminosAlta(InformesDao.countExecutionTermsByPriority(conn, lenoxForm.getIdExecution(), Constants.HIGH_LENOX_PRIORITY));
                    lenoxForm.setTerminosMedia(InformesDao.countExecutionTermsByPriority(conn, lenoxForm.getIdExecution(), Constants.MEDIUM_LENOX_PRIORITY));
                    lenoxForm.setTerminosBaja(InformesDao.countExecutionTermsByPriority(conn, lenoxForm.getIdExecution(), Constants.LOW_LENOX_PRIORITY));
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(conn);
        }

        return lenoxResultList;
    }

    private static void getEvolutionTermPriorityGraphic(HttpServletRequest request, Map<Date, List<ObservatoryLenoxForm>> pageObservatoryMap, String title, String filePath, String noDataMess, int type) throws Exception {
        Map<String, BigDecimal> resultData = getResultEvolutionData(pageObservatoryMap, type);
        File file = new File(filePath);

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        for (final Map.Entry<String, BigDecimal> resultDataEntry : resultData.entrySet()) {
            dataSet.addValue(resultDataEntry.getValue(), "", resultDataEntry.getKey());
        }

        PropertiesManager pmgr = new PropertiesManager();
        //Si no existe la gráfica, la creamos
        if (!file.exists()) {
            String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "resultados.anonimos.porcentaje.terminos");
            String columnTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.date");

            ChartForm chart = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, false, false, true, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors"));
            GraphicsUtils.createStandardBarChart(chart, filePath, noDataMess, request, false);

        }

        //Los incluimos en la request
        if (type == Constants.HIGH_LENOX_PRIORITY) {
            request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_EVOLUTION_HIGH_TERMS, infoLevelEvolutionGraphic(resultData));
        } else if (type == Constants.MEDIUM_LENOX_PRIORITY) {
            request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_EVOLUTION_MEDIUM_TERMS, infoLevelEvolutionGraphic(resultData));
        }
        if (type == Constants.LOW_LENOX_PRIORITY) {
            request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_EVOLUTION_LOW_TERMS, infoLevelEvolutionGraphic(resultData));
        }

    }

    private static Map<String, BigDecimal> getResultEvolutionData(Map<Date, List<ObservatoryLenoxForm>> pageObservatoryMap, int type) {

        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
        Map<String, BigDecimal> result = new TreeMap<String, BigDecimal>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                PropertiesManager pmgr = new PropertiesManager();
                DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
                try {
                    Date fecha1 = new Date(df.parse(o1).getTime());
                    Date fecha2 = new Date(df.parse(o2).getTime());
                    return fecha1.compareTo(fecha2);
                } catch (Exception e) {
                    Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
                }
                return 0;
            }

        });

        for (Map.Entry<Date, List<ObservatoryLenoxForm>> pageObservatoryEntry : pageObservatoryMap.entrySet()) {
            BigDecimal numTerms = BigDecimal.ZERO;
            for (ObservatoryLenoxForm lenoxForm : pageObservatoryEntry.getValue()) {
                if (type == Constants.HIGH_LENOX_PRIORITY) {
                    numTerms = numTerms.add(new BigDecimal(lenoxForm.getTerminosAlta()));
                } else if (type == Constants.MEDIUM_LENOX_PRIORITY) {
                    numTerms = numTerms.add(new BigDecimal(lenoxForm.getTerminosMedia()));
                } else if (type == Constants.LOW_LENOX_PRIORITY) {
                    numTerms = numTerms.add(new BigDecimal(lenoxForm.getTerminosBaja()));
                }
            }
            result.put(df.format(pageObservatoryEntry.getKey()), numTerms);
        }

        return result;
    }

    public static Map<Date, List<ObservatoryLenoxForm>> resultEvolutionData(Long observatory_id, Long execution_id) {
        Connection c = null;
        Map<Date, List<ObservatoryLenoxForm>> resultData = new HashMap<Date, List<ObservatoryLenoxForm>>();
        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();

            Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatory_id, execution_id, Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id")));
            for (Map.Entry<Long, Date> executedObservatoryEntry : executedObservatoryIdMap.entrySet()) {
                List<ObservatoryLenoxForm> pageList = getGlobalResultData(String.valueOf(executedObservatoryEntry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE);
                resultData.put(executedObservatoryEntry.getValue(), pageList);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return resultData;
    }

    private static void getEvolutionPercentagePriorityTermGraphic(HttpServletRequest request, Map<Date, List<ObservatoryLenoxForm>> pageObservatoryMap, String title, String filePath, String noDataMess) throws Exception {

    }

    private static void getPercentagePriorityTermsGraphic(HttpServletRequest request,
                                                          List<ObservatoryLenoxForm> pageExecutionList, String title, String filePath, String noDataMess) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        File file = new File(filePath);

        Map<String, BigDecimal> result = getPriorityTermsMap(pageExecutionList);

        if (!file.exists()) {
            GraphicsUtils.totalPageStr = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.term.number");
            GraphicsUtils.totalPage = result.get(Constants.HIGH_LENOX_PRIORITY_LABEL).add(result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL)).add(result.get(Constants.LOW_LENOX_PRIORITY_LABEL)).longValue();

            DefaultPieDataset dataSet = new DefaultPieDataset();

            dataSet.setValue(CrawlerUtils.getResources(request).getMessage("observatory.lenox.low.priority"), result.get(Constants.LOW_LENOX_PRIORITY_LABEL));
            dataSet.setValue(CrawlerUtils.getResources(request).getMessage("observatory.lenox.medium.priority"), result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL));
            dataSet.setValue(CrawlerUtils.getResources(request).getMessage("observatory.lenox.high.priority"), result.get(Constants.HIGH_LENOX_PRIORITY_LABEL));

            GraphicsUtils.createPieChart(dataSet, title, filePath, noDataMess, pmgr.getValue(CRAWLER_PROPERTIES, "chart.lenox.graphic.colors"), x, y);
        }

        request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_TERMS, infoPercentagePriorityTerms(request, result));
    }

    private static void getPercentagePrioritySegmentsTermsGraphic(HttpServletRequest request, List<ObservatoryLenoxForm> pageExecutionListSI,
                                                                  List<ObservatoryLenoxForm> pageExecutionListSII, List<ObservatoryLenoxForm> pageExecutionListSIII, String title, String filePath, String noDataMess) throws Exception {
        File file = new File(filePath);

        Map<String, Map<String, BigDecimal>> resultDataBySegment = new HashMap<String, Map<String, BigDecimal>>();
        resultDataBySegment.put(Constants.COMPLEXITY_SEGMENT_NAME_1, calculatePercentage(getPriorityTermsMap(pageExecutionListSI)));
        resultDataBySegment.put(Constants.COMPLEXITY_SEGMENT_NAME_2, calculatePercentage(getPriorityTermsMap(pageExecutionListSII)));
        resultDataBySegment.put(Constants.COMPLEXITY_SEGMENT_NAME_3, calculatePercentage(getPriorityTermsMap(pageExecutionListSIII)));

        PropertiesManager pmgr = new PropertiesManager();
        DefaultCategoryDataset dataSet = createStackedBarDataSet(resultDataBySegment);

        if (!file.exists()) {
            ChartForm chartForm = new ChartForm(title, "", "", dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.lenox.graphic.colors"));
            GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
        }

        request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG_TERMS_1, infoPercentagePrioritySegmentTerms(request, resultDataBySegment.get(Constants.COMPLEXITY_SEGMENT_NAME_1)));
        request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG_TERMS_2, infoPercentagePrioritySegmentTerms(request, resultDataBySegment.get(Constants.COMPLEXITY_SEGMENT_NAME_2)));
        request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_PERCENTAGE_SEG_TERMS_3, infoPercentagePrioritySegmentTerms(request, resultDataBySegment.get(Constants.COMPLEXITY_SEGMENT_NAME_3)));
    }

    private static DefaultCategoryDataset createStackedBarDataSet(Map<String, Map<String, BigDecimal>> result) {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_1).get(Constants.LOW_LENOX_PRIORITY_LABEL), Constants.LOW_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_1);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_1).get(Constants.MEDIUM_LENOX_PRIORITY_LABEL), Constants.MEDIUM_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_1);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_1).get(Constants.HIGH_LENOX_PRIORITY_LABEL), Constants.HIGH_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_1);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_2).get(Constants.LOW_LENOX_PRIORITY_LABEL), Constants.LOW_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_2);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_2).get(Constants.MEDIUM_LENOX_PRIORITY_LABEL), Constants.MEDIUM_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_2);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_2).get(Constants.HIGH_LENOX_PRIORITY_LABEL), Constants.HIGH_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_2);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_3).get(Constants.LOW_LENOX_PRIORITY_LABEL), Constants.LOW_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_3);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_3).get(Constants.MEDIUM_LENOX_PRIORITY_LABEL), Constants.MEDIUM_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_3);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_3).get(Constants.HIGH_LENOX_PRIORITY_LABEL), Constants.HIGH_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_3);

        return dataSet;
    }

    private static void getNumberPrioritySegmentsTermsGraphic(HttpServletRequest request, List<ObservatoryLenoxForm> pageExecutionListSI,
                                                              List<ObservatoryLenoxForm> pageExecutionListSII, List<ObservatoryLenoxForm> pageExecutionListSIII, String title, String filePath, String noDataMess) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        File file = new File(filePath);

        Map<String, Map<String, BigDecimal>> resultDataBySegment = new HashMap<String, Map<String, BigDecimal>>();
        resultDataBySegment.put(Constants.COMPLEXITY_SEGMENT_NAME_1, getPriorityTermsMap(pageExecutionListSI));
        resultDataBySegment.put(Constants.COMPLEXITY_SEGMENT_NAME_2, getPriorityTermsMap(pageExecutionListSII));
        resultDataBySegment.put(Constants.COMPLEXITY_SEGMENT_NAME_3, getPriorityTermsMap(pageExecutionListSIII));

        if (!file.exists()) {
            ChartForm observatoryGraphicsForm = new ChartForm(title, "", "", createGlobalSeriesDataSet(resultDataBySegment), true, true, false, false, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.lenox.graphic.colors"));
            GraphicsUtils.createSeriesBarChart(observatoryGraphicsForm, filePath, noDataMess, request, false);
        }

        request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_NUMBER_SEG_TERMS_1, infoNumberPrioritySegmentTerms(request, resultDataBySegment.get(Constants.COMPLEXITY_SEGMENT_NAME_1)));
        request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_NUMBER_SEG_TERMS_2, infoNumberPrioritySegmentTerms(request, resultDataBySegment.get(Constants.COMPLEXITY_SEGMENT_NAME_2)));
        request.setAttribute(Constants.OBSERVATORY_LENOX_GRAPHIC_GLOBAL_NUMBER_SEG_TERMS_3, infoNumberPrioritySegmentTerms(request, resultDataBySegment.get(Constants.COMPLEXITY_SEGMENT_NAME_3)));
    }

    private static Map<String, BigDecimal> getPriorityTermsMap(List<ObservatoryLenoxForm> segmentTerms) {
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();

        result.put(Constants.LOW_LENOX_PRIORITY_LABEL, BigDecimal.ZERO);
        result.put(Constants.MEDIUM_LENOX_PRIORITY_LABEL, BigDecimal.ZERO);
        result.put(Constants.HIGH_LENOX_PRIORITY_LABEL, BigDecimal.ZERO);


        for (ObservatoryLenoxForm lenoxForm : segmentTerms) {
            result.put(Constants.HIGH_LENOX_PRIORITY_LABEL, (result.get(Constants.HIGH_LENOX_PRIORITY_LABEL).add(new BigDecimal(lenoxForm.getTerminosAlta()))));
            result.put(Constants.MEDIUM_LENOX_PRIORITY_LABEL, (result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL).add(new BigDecimal(lenoxForm.getTerminosMedia()))));
            result.put(Constants.LOW_LENOX_PRIORITY_LABEL, (result.get(Constants.LOW_LENOX_PRIORITY_LABEL).add(new BigDecimal(lenoxForm.getTerminosBaja()))));
        }
        return result;
    }

    private static DefaultCategoryDataset createGlobalSeriesDataSet(Map<String, Map<String, BigDecimal>> result) {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_1).get(Constants.LOW_LENOX_PRIORITY_LABEL), Constants.LOW_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_1);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_1).get(Constants.MEDIUM_LENOX_PRIORITY_LABEL), Constants.MEDIUM_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_1);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_1).get(Constants.HIGH_LENOX_PRIORITY_LABEL), Constants.HIGH_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_1);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_2).get(Constants.LOW_LENOX_PRIORITY_LABEL), Constants.LOW_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_2);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_2).get(Constants.MEDIUM_LENOX_PRIORITY_LABEL), Constants.MEDIUM_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_2);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_2).get(Constants.HIGH_LENOX_PRIORITY_LABEL), Constants.HIGH_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_2);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_3).get(Constants.LOW_LENOX_PRIORITY_LABEL), Constants.LOW_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_3);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_3).get(Constants.MEDIUM_LENOX_PRIORITY_LABEL), Constants.MEDIUM_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_3);
        dataSet.addValue(result.get(Constants.COMPLEXITY_SEGMENT_NAME_3).get(Constants.HIGH_LENOX_PRIORITY_LABEL), Constants.HIGH_LENOX_PRIORITY_LABEL, Constants.COMPLEXITY_SEGMENT_NAME_3);

        return dataSet;
    }

    private static Map<String, BigDecimal> calculatePercentage(Map<String, BigDecimal> values) {
        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        for (String key : values.keySet()) {
            total = total.add(values.get(key));
        }
        for (String key : values.keySet()) {
            if (total.compareTo(BigDecimal.ZERO) != 0) {
                result.put(key, (values.get(key).divide(total, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))));
            } else {
                result.put(key, (BigDecimal.ZERO));
            }
        }

        return result;
    }

    //INCLUDE LABELS METHODS

    private static List<LabelValueBean> infoPercentagePriorityTerms(HttpServletRequest request, Map<String, BigDecimal> result) throws Exception {
        List<LabelValueBean> labelValueList = new ArrayList<LabelValueBean>();

        BigDecimal totalPort = result.get(Constants.HIGH_LENOX_PRIORITY_LABEL).add(result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL)).add(result.get(Constants.LOW_LENOX_PRIORITY_LABEL));
        LabelValueBean labelValue;

        labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("observatory.lenox.high.priority"));
        labelValue.setValue(String.valueOf((result.get(Constants.HIGH_LENOX_PRIORITY_LABEL).multiply(new BigDecimal(100)).divide(totalPort, 2, BigDecimal.ROUND_HALF_UP))).replace(".00", "") + "%");
        labelValueList.add(labelValue);

        labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("observatory.lenox.medium.priority"));
        labelValue.setValue(String.valueOf((result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL).multiply(new BigDecimal(100)).divide(totalPort, 2, BigDecimal.ROUND_HALF_UP))).replace(".00", "") + "%");
        labelValueList.add(labelValue);

        labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("observatory.lenox.low.priority"));
        labelValue.setValue(String.valueOf((result.get(Constants.LOW_LENOX_PRIORITY_LABEL).multiply(new BigDecimal(100)).divide(totalPort, 2, BigDecimal.ROUND_HALF_UP))).replace(".00", "") + "%");
        labelValueList.add(labelValue);

        return labelValueList;
    }

    private static List<LabelValueBean> infoPercentagePrioritySegmentTerms(HttpServletRequest request, Map<String, BigDecimal> result) throws Exception {
        List<LabelValueBean> labelValueList = new ArrayList<LabelValueBean>();

        LabelValueBean labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.high"));
        if (result.get(Constants.HIGH_LENOX_PRIORITY_LABEL) != null && result.get(Constants.HIGH_LENOX_PRIORITY_LABEL).compareTo(new BigDecimal(-1)) != 0) {
            labelValue.setValue(String.valueOf(result.get(Constants.HIGH_LENOX_PRIORITY_LABEL)).replace(".00", "") + "%");
        } else if (result.get(Constants.HIGH_LENOX_PRIORITY_LABEL) == null) {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
        } else {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
        }
        labelValueList.add(labelValue);

        labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.medium"));
        if (result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL) != null && result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL).compareTo(new BigDecimal(-1)) != 0) {
            labelValue.setValue(String.valueOf(result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL)).replace(".00", "") + "%");
        } else if (result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL) == null) {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
        } else {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
        }
        labelValueList.add(labelValue);

        labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.low"));
        if (result.get(Constants.LOW_LENOX_PRIORITY_LABEL) != null && result.get(Constants.LOW_LENOX_PRIORITY_LABEL).compareTo(new BigDecimal(-1)) != 0) {
            labelValue.setValue(String.valueOf(result.get(Constants.LOW_LENOX_PRIORITY_LABEL)).replace(".00", "") + "%");
        } else if (result.get(Constants.LOW_LENOX_PRIORITY_LABEL) == null) {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
        } else {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
        }
        labelValueList.add(labelValue);

        return labelValueList;
    }

    private static List<LabelValueBean> infoNumberPrioritySegmentTerms(HttpServletRequest request, Map<String, BigDecimal> result) throws Exception {
        List<LabelValueBean> labelValueList = new ArrayList<LabelValueBean>();

        LabelValueBean labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.high"));
        if (result.get(Constants.HIGH_LENOX_PRIORITY_LABEL) != null && result.get(Constants.HIGH_LENOX_PRIORITY_LABEL).compareTo(new BigDecimal(-1)) != 0) {
            labelValue.setValue(String.valueOf(result.get(Constants.HIGH_LENOX_PRIORITY_LABEL)).replace(".00", ""));
        } else if (result.get(Constants.HIGH_LENOX_PRIORITY_LABEL) == null) {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
        } else {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
        }
        labelValueList.add(labelValue);

        labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.medium"));
        if (result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL) != null && result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL).compareTo(new BigDecimal(-1)) != 0) {
            labelValue.setValue(String.valueOf(result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL)).replace(".00", ""));
        } else if (result.get(Constants.MEDIUM_LENOX_PRIORITY_LABEL) == null) {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
        } else {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
        }
        labelValueList.add(labelValue);

        labelValue = new LabelValueBean();
        labelValue.setLabel(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.low"));
        if (result.get(Constants.LOW_LENOX_PRIORITY_LABEL) != null && result.get(Constants.LOW_LENOX_PRIORITY_LABEL).compareTo(new BigDecimal(-1)) != 0) {
            labelValue.setValue(String.valueOf(result.get(Constants.LOW_LENOX_PRIORITY_LABEL)).replace(".00", ""));
        } else if (result.get(Constants.LOW_LENOX_PRIORITY_LABEL) == null) {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
        } else {
            labelValue.setValue(CrawlerUtils.getResources(request).getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
        }
        labelValueList.add(labelValue);

        return labelValueList;
    }

    private static List<LabelValueBean> infoLevelEvolutionGraphic(Map<String, BigDecimal> resultData) {
        List<LabelValueBean> labelValueList = new ArrayList<LabelValueBean>();
        LabelValueBean labelValue;

        for (String date : resultData.keySet()) {
            labelValue = new LabelValueBean();
            labelValue.setLabel(date);
            if ((String.valueOf(resultData.get(date)).contains(".0")) && (
                    (String.valueOf(resultData.get(date)).indexOf('.') + 2) == String.valueOf(resultData.get(date)).length())) {
                labelValue.setValue(String.valueOf(resultData.get(date)).replace(".0", ""));
            } else if ((String.valueOf(resultData.get(date)).contains(".00")) && (
                    (String.valueOf(resultData.get(date)).indexOf('.') + 3) == String.valueOf(resultData.get(date)).length())) {
                labelValue.setValue(String.valueOf(resultData.get(date)).replace(".00", ""));
            } else {
                labelValue.setValue(String.valueOf(resultData.get(date)));
            }
            labelValueList.add(labelValue);
        }

        return labelValueList;
    }

}