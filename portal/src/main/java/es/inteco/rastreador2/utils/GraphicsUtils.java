package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatorySiteEvaluationForm;
import es.inteco.rastreador2.action.rastreo.ChartIntavAction;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class GraphicsUtils {

    public static String totalPageStr;
    public static long totalPage;
    private static Font legendFont = new java.awt.Font("Arial", Font.PLAIN, 14);
    private static Font pieToolTipFont = new Font("Arial", Font.PLAIN, 10);
    private static Font noDataMessageFont = new java.awt.Font("Arial", Font.BOLD, 30);
    private static String labelSpace = "    ";
    private static Color redColor = new Color(225, 18, 13);
    private static Color greenColor = new Color(38, 187, 8);
    private static Color blueColor = new Color(15, 91, 255);

    private GraphicsUtils() {
    }

    public static void createPieChart(DefaultPieDataset dataSet, String title,
                                      String filePath, String noDataMess, String colorsKey, int x, int y) throws Exception {

        JFreeChart chart = ChartFactory.createPieChart3D(title, dataSet, true, true, false);

        formatLegend(chart);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setBackgroundPaint(Color.white);

        plot.setNoDataMessage(noDataMess);
        plot.setNoDataMessageFont(noDataMessageFont);
        plot.setNoDataMessagePaint(Color.RED);

        final Color[] colors = getColors(colorsKey).toArray(new Color[0]);
        PieRenderer renderer = new PieRenderer(colors);
        renderer.setColor(plot, dataSet);

        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);

        plot.setLabelFont(pieToolTipFont);
        plot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
        plot.setCircular(false);
        plot.setOutlineVisible(true);
        plot.setBaseSectionOutlinePaint(Color.black);

        Shape shape = new Rectangle(15, 15);
        plot.setLegendItemShape(shape);

        plot.setLabelGenerator(new CustomLabelGenerator(false));
        plot.setLegendLabelGenerator(new CustomLabelGenerator(true));

        for (int i = 0; i < plot.getLegendItems().getItemCount(); i++) {
            LegendItem item = plot.getLegendItems().get(i);
            item.setLabelFont(legendFont);
        }

        printChart(filePath, chart, x, y);
    }

    //labelPosition true 45 grados false normal
    public static void createBarChart(DefaultCategoryDataset dataSet, String title, String rowTitle, String columnTitle,
                                      String color, boolean withLegend, boolean percentage, boolean labelPosition,
                                      String filePath, String noDataMess, HttpServletRequest request, int x, int y) throws Exception {

        ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, percentage, withLegend, labelPosition, false, x, y, color);
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMess, request, true);
    }

    //labelPosition true 45 grados false normal
    public static void createBarChart(Map<String, BigDecimal> result, String title, String rowTitle, String columnTitle,
                                      String color, boolean withLegend, boolean percentage, boolean labelPosition,
                                      String filePath, String noDataMess, HttpServletRequest request, int x, int y) throws Exception {
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (Map.Entry<String, BigDecimal> entry : result.entrySet()) {
            BigDecimal value = entry.getValue();
            dataSet.addValue(value, "", parseLevelLabel(entry.getKey(), request));
        }

        ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, percentage, withLegend, labelPosition, false, x, y, color);
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMess, request, true);
    }

    public static void createSeriesBarChart(ChartForm observatoryGraphicsForm,
                                            String filePath, String noDataMess, HttpServletRequest request, boolean withRange) throws Exception {
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMess, request, withRange);
    }

    public static void createBar1PxChart(List<ObservatorySiteEvaluationForm> result, String title, String rowTitle, String columnTitle,
                                         String filePath, String noDataMess, HttpServletRequest request, int x, int y, boolean showColLab) throws Exception {

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        PropertiesManager pmgr = new PropertiesManager();
        StringBuilder colors = new StringBuilder();
        for (ObservatorySiteEvaluationForm observatorySiteEvaluationForm : result) {
            if ("NV".equalsIgnoreCase(observatorySiteEvaluationForm.getLevel())) {
                colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.red.color")).append(",");
            }
            if ("A".equalsIgnoreCase(observatorySiteEvaluationForm.getLevel())) {
                colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.yellow.color")).append(",");
            }
            if ("AA".equalsIgnoreCase(observatorySiteEvaluationForm.getLevel())) {
                colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.green.color")).append(",");
            }

            if (observatorySiteEvaluationForm.getAcronym() != null && !StringUtils.isEmpty(observatorySiteEvaluationForm.getAcronym())) {
                dataSet.addValue(observatorySiteEvaluationForm.getScore(), "", observatorySiteEvaluationForm.getAcronym());
            } else {
                dataSet.addValue(observatorySiteEvaluationForm.getScore(), "", observatorySiteEvaluationForm.getName());
            }
        }

        ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, false, false, true, false, true, true, true, x, y, colors.toString());
        observatoryGraphicsForm.setFixedColorBars(true);
        observatoryGraphicsForm.setFixedLegend(true);
        observatoryGraphicsForm.setShowColumsLabels(showColLab);
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMess, request, true);
    }

    public static void createBarPageByLevelChart(List<ObservatoryEvaluationForm> result, String title, String rowTitle, String columnTitle,
                                                 String filePath, String noDataMess, HttpServletRequest request, int x, int y) throws Exception {

        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        PropertiesManager pmgr = new PropertiesManager();
        StringBuilder colors = new StringBuilder();
        int i = 1;
        for (ObservatoryEvaluationForm observatoryEvaluationForm : result) {
            if ("NV".equalsIgnoreCase(ObservatoryUtils.pageSuitabilityLevel(observatoryEvaluationForm))) {
                colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.red.color")).append(",");
            }
            if ("A".equalsIgnoreCase(ObservatoryUtils.pageSuitabilityLevel(observatoryEvaluationForm))) {
                colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.yellow.color")).append(",");
            }
            if ("AA".equalsIgnoreCase(ObservatoryUtils.pageSuitabilityLevel(observatoryEvaluationForm))) {
                colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.green.color")).append(",");
            }

            dataSet.addValue(observatoryEvaluationForm.getScore().setScale(observatoryEvaluationForm.getScore().scale() - 1), "", CrawlerUtils.getResources(request).getMessage("observatory.graphic.score.by.page.label", i++));
        }

        ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, false, true, true, true, x, y, colors.toString());
        observatoryGraphicsForm.setFixedLegend(true);
        observatoryGraphicsForm.setFixedColorBars(true);
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMess, request, true);
    }

    public static void createStackedBarChart(ChartForm chartForm, String noDataMess, String filePath) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();
        JFreeChart chart = ChartFactory.createStackedBarChart3D(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(), chartForm.getDataSet(),
                PlotOrientation.VERTICAL, chartForm.isLegend(), true, false);

        formatLegend(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        //plot.setForegroundAlpha(0.6f);

        plot.setNoDataMessage(noDataMess);
        plot.setNoDataMessageFont(noDataMessageFont);
        plot.setNoDataMessagePaint(Color.RED);

        List<Paint> colors = getColors(chartForm.getColor());

        CategoryRenderer renderer = new CategoryRenderer(colors);
        renderer.setColor(plot, chartForm.getDataSet());

        plot.setFixedLegendItems(generateLegend(colors, plot));

        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setBaseItemLabelGenerator(new LabelGenerator(true));
        barRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        barRenderer.setBaseItemLabelFont(new java.awt.Font("Arial", Font.BOLD, 12));
        barRenderer.setBaseItemLabelsVisible(true);
        barRenderer.setDrawBarOutline(true);
        barRenderer.setMaximumBarWidth(0.5);
        barRenderer.setBaseOutlinePaint(Color.BLACK);

        itemLabelColor(barRenderer, colors);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setMaximumCategoryLabelLines(3);
        domainAxis.setUpperMargin(0.01);
        domainAxis.setLowerMargin(0.01);


        if (!chartForm.isRoundLabelPosition()) {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        } else {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        }

        NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        createNumberAxis(plot, true);

        printChart(filePath, chart, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x")),
                Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y")));

    }

    private static void itemLabelColor(BarRenderer barRenderer, List<Paint> colors) {
        int serie = 0;
        for (Paint color : colors) {
            if (color.equals(redColor) || color.equals(greenColor) || color.equals(blueColor)) {
                barRenderer.setSeriesItemLabelPaint(serie, Color.white);
            } else {
                barRenderer.setSeriesItemLabelPaint(serie, Color.black);
            }
            serie++;
        }
    }

    public static void createStandardBarChart(ChartForm observatoryGraphicsForm,
                                              String filePath, String noDataMess, HttpServletRequest request, boolean withRange) throws Exception {

        JFreeChart chart;

        if (observatoryGraphicsForm.isTridimensional()) {
            chart = ChartFactory.createBarChart3D(observatoryGraphicsForm.getTitle(), observatoryGraphicsForm.getColumnTitle(), observatoryGraphicsForm.getRowTitle(),
                    observatoryGraphicsForm.getDataSet(), PlotOrientation.VERTICAL, observatoryGraphicsForm.isLegend(), true, false);
        } else {
            chart = ChartFactory.createBarChart(observatoryGraphicsForm.getTitle(), observatoryGraphicsForm.getColumnTitle(), observatoryGraphicsForm.getRowTitle(),
                    observatoryGraphicsForm.getDataSet(), PlotOrientation.VERTICAL, observatoryGraphicsForm.isLegend(), true, false);
        }

        formatLegend(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage(noDataMess);
        plot.setNoDataMessageFont(noDataMessageFont);
        plot.setNoDataMessagePaint(Color.RED);

        //Elimina la transparencia de las gráficas
        plot.setForegroundAlpha(1.0f);

        defineBarColor(plot, observatoryGraphicsForm, request);

        NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        if (withRange) {
            createNumberAxis(plot, observatoryGraphicsForm.isPercentage());
        } else {
            createNumberAxisWithoutRange(observatoryGraphicsForm.getDataSet(), plot, observatoryGraphicsForm.isPercentage());
        }

        if (!observatoryGraphicsForm.isRoundLabelPosition()) {
            createDomainAxis(plot, CategoryLabelPositions.STANDARD, observatoryGraphicsForm);
        } else {
            createDomainAxis(plot, CategoryLabelPositions.UP_45, observatoryGraphicsForm);
        }

        putValuesOnBars(chart, observatoryGraphicsForm);

        printChart(filePath, chart, observatoryGraphicsForm.getX(), observatoryGraphicsForm.getY());
    }

    public static String parseLevelLabel(String key, HttpServletRequest request) {
        if (key.equals(Constants.OBS_A)) {
            return CrawlerUtils.getResources(request).getMessage("observatory.graphics.level.A");
        } else if (key.equals(Constants.OBS_AA)) {
            return CrawlerUtils.getResources(request).getMessage("observatory.graphics.level.AA");
        } else if (key.equals(Constants.OBS_NV)) {
            return CrawlerUtils.getResources(request).getMessage("observatory.graphics.level.NV");
        } else {
            return key;
        }
    }

    private static void createNumberAxis(CategoryPlot plot, boolean percentage) {
        NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();

        if (!percentage) {
            valueAxis.setTickUnit(new NumberTickUnit(1));
            valueAxis.setRangeWithMargins(new Range(0, 10));
        } else {
            valueAxis.setTickUnit(new NumberTickUnit(10));
            valueAxis.setRangeWithMargins(new Range(0, 100));
        }
        valueAxis.setLowerBound(0);
    }

    private static void createNumberAxisWithoutRange(CategoryDataset dataSet, CategoryPlot plot, boolean percentage) {
        int maxValue = 0;
        for (int i = 0; i < dataSet.getColumnCount(); i++) {
            for (int j = 0; j < dataSet.getRowCount(); j++) {
                if ((dataSet.getValue(j, i) != null) && (dataSet.getValue(j, i).intValue() > maxValue)) {
                    maxValue = dataSet.getValue(j, i).intValue();
                }
            }
        }

        if (maxValue < 10) {
            createNumberAxis(plot, percentage);
        }

        NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
        valueAxis.setUpperMargin(0.2);
    }

    private static void createDomainAxis(CategoryPlot plot, CategoryLabelPositions position, ChartForm chartForm) {
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(position);
        domainAxis.setMaximumCategoryLabelLines(3);
        if (chartForm.isOnePixelGraph()) {
            if (chartForm.isShowColumsLabels()) {
                domainAxis.setVisible(true);
            } else {
                domainAxis.setTickMarksVisible(false);
                domainAxis.setAxisLineVisible(false);
                domainAxis.setTickLabelsVisible(false);
            }
        }
        domainAxis.setUpperMargin(0.01);
        domainAxis.setLowerMargin(0.01);
    }

    private static void defineBarColor(CategoryPlot plot, ChartForm chartForm, HttpServletRequest request) {

        plot.setBackgroundPaint(Color.white);
        if (chartForm.isGridline()) {
            plot.setRangeGridlinePaint(Color.black);
        }

        List<Paint> colors;
        if (chartForm.getColor() != null && !chartForm.getColor().equals("")) {
            colors = getColors(chartForm.getColor());
        } else {
            colors = getColors("chart.observatory.graphic.intav.colors");
        }

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        if (chartForm.isLegend()) {
            if (chartForm.isFixedLegend()) {
                plot.setFixedLegendItems(generateOnePixelLegend(request));
            } else {
                if (!chartForm.isOnePixelGraph()) {
                    plot.setFixedLegendItems(generateLegend(colors, plot));
                } else {
                    plot.setFixedLegendItems(generateOnePixelLegend(request));
                }
            }
        }

        if (!chartForm.isFixedColorBars()) {
            for (int i = 0; i < colors.size(); i++) {
                renderer.setSeriesPaint(i, colors.get(i));
            }
        } else {
            Paint[] paintArray = new Paint[colors.size()];
            int i = 0;
            for (Paint p : colors) {
                paintArray[i++] = p;
            }
            CategoryItemRenderer rendererC;
            if (chartForm.isTridimensional()) {
                rendererC = new CustomRenderer3D(paintArray);
            } else {
                rendererC = new CustomRenderer(paintArray);
            }
            plot.setRenderer(rendererC);
        }

    }

    private static void formatLegend(JFreeChart chart) {
        LegendTitle lt = chart.getLegend();
        if (lt != null) {
            lt.setItemFont(legendFont);
            lt.setItemPaint(Color.black);
            lt.setPadding(10, 10, 10, 10);
        }
    }

    private static LegendItemCollection generateLegend(List<Paint> colors, CategoryPlot plot) {

        LegendItemCollection newLegend = new LegendItemCollection();
        LegendItemCollection legend = plot.getLegendItems();
        Shape shape = new Rectangle(15, 15);
        BasicStroke stroke = new BasicStroke();

        for (int i = 0; i < legend.getItemCount(); i++) {
            LegendItem item = legend.get(i);
            item = new LegendItem(item.getLabel() + labelSpace, item.getLabel() + labelSpace, item.getLabel() + labelSpace, item.getLabel() + labelSpace, shape, colors.get(i), stroke, Color.black);
            newLegend.add(item);
        }
        return newLegend;
    }

    private static LegendItemCollection generateOnePixelLegend(HttpServletRequest request) {

        PropertiesManager pmgr = new PropertiesManager();
        String[] labels = {parseLevelLabel(Constants.OBS_NV, request), parseLevelLabel(Constants.OBS_A, request), parseLevelLabel(Constants.OBS_AA, request)};
        List<Paint> colors = getColors(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));

        //Se crea una leyenda fija
        LegendItemCollection legend = new LegendItemCollection();
        Shape shape = new Rectangle(15, 15);
        BasicStroke stroke = new BasicStroke();

        if (colors.size() == labels.length) {
            int i = 0;
            for (String label : labels) {
                LegendItem item = new LegendItem(label + labelSpace, label + labelSpace, label + labelSpace, label + labelSpace, shape, colors.get(i), stroke, Color.black);
                legend.add(item);
                i++;
            }
        }
        return legend;
    }

    private static void putValuesOnBars(JFreeChart chart, ChartForm chartForm) {

        CategoryPlot plot = chart.getCategoryPlot();

        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setShadowVisible(false);

        if (chartForm.isOnePixelGraph()) {
            barRenderer.setMaximumBarWidth(0.005);
        } else {
            barRenderer.setMaximumBarWidth(0.1);
            barRenderer.setBaseItemLabelFont(new java.awt.Font("Arial", Font.BOLD, 12));
            barRenderer.setBaseItemLabelGenerator(new LabelGenerator(chartForm.isPercentage()));
            barRenderer.setBaseItemLabelsVisible(true);
            barRenderer.setDrawBarOutline(true);
            barRenderer.setBaseOutlinePaint(Color.gray);

            if (chartForm.isTridimensional()) {
                CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
                renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
                ((BarRenderer3D) renderer).setItemLabelAnchorOffset(10D);
            }
        }

        barRenderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
    }

    private static void printChart(String filePath, JFreeChart chart, int x, int y) throws Exception {

        File file = new File(filePath);

        FileOutputStream fout = null;
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                Logger.putLog("Error al crear el archivo con la imagen de la gráfica", ChartIntavAction.class, Logger.LOG_LEVEL_ERROR);
            }
            fout = new FileOutputStream(file);
            ChartUtilities.writeChartAsJPEG(fout, chart, x, y);
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error cerrar el archivo con la imagen de la gráfica", ChartIntavAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    private static List<Paint> getColors(String colorsProperty) {

        String regexp = "\\{(.*?),(.*?),(.*?)\\}";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(colorsProperty);
        List<Paint> colors = new ArrayList<Paint>();
        while (matcher.find()) {
            colors.add(new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
        }
        return colors;
    }

    public static class PieRenderer {
        private Color[] color;

        public PieRenderer(Color[] color) {
            this.color = color;
        }

        public void setColor(PiePlot plot, DefaultPieDataset dataset) {
            List<Comparable> keys = dataset.getKeys();
            int aInt;

            for (int i = 0; i < keys.size(); i++) {
                aInt = i % this.color.length;
                plot.setSectionPaint(keys.get(i), this.color[aInt]);
            }
        }
    }

    public static class CustomLabelGenerator implements PieSectionLabelGenerator {

        private boolean legendLabel;

        public CustomLabelGenerator(boolean legendLabel) {
            this.legendLabel = legendLabel;
        }

        public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
            if (!legendLabel) {
                float p = (float) Math.pow(10, 2);
                if (!dataset.getValue(key).toString().equals("0")) {
                    Float value = ((Float.valueOf(dataset.getValue(key).toString()) / Float.valueOf(totalPage)) * 100);
                    value = value * p;
                    float tmp = Math.round(value);
                    value = tmp / p;
                    return key + "\n" + totalPageStr + dataset.getValue(key).toString() + "\n" + " (" + value.toString() + "%)";
                } else {
                    return null;
                }
            } else {
                return key + labelSpace;
            }
        }

        @Override
        public AttributedString generateAttributedSectionLabel(PieDataset arg0, Comparable arg1) {
            // TODO Auto-generated method stub
            return null;
        }
    }


    public static class LabelGenerator implements CategoryItemLabelGenerator {

        private boolean isPercentage;

        public LabelGenerator(boolean isPercentage) {
            this.isPercentage = isPercentage;
        }

        public String generateLabel(CategoryDataset dataset, int series, int category) {
            String result = null;

            if (isPercentage) {
                if ((dataset.getValue(series, category).toString().contains(".0")) && (
                        (dataset.getValue(series, category).toString().indexOf('.') + 2) == dataset.getValue(series, category).toString().length())) {
                    return dataset.getValue(series, category).toString().replace(".0", "") + "%";
                } else if ((dataset.getValue(series, category).toString().contains(".00")) && (
                        (dataset.getValue(series, category).toString().indexOf('.') + 3) == dataset.getValue(series, category).toString().length())) {
                    return dataset.getValue(series, category).toString().replace(".00", "") + "%";
                } else {
                    return dataset.getValue(series, category).toString() + "%";
                }
            } else {
                Number value = dataset.getValue(series, category);
                if (value != null && !(value.intValue() == -1)) {
                    if ((value.toString().contains(".0")) && (
                            (value.toString().indexOf('.') + 2) == value.toString().length())) {
                        return value.toString().replace(".0", "");
                    } else if ((value.toString().contains(".00")) && (
                            (value.toString().indexOf('.') + 3) == value.toString().length())) {
                        return value.toString().replace(".00", "");
                    } else {
                        return value.toString();
                    }
                } else if (value == null || value.intValue() == -1) {
                    return "NP";
                }
                return result;
            }
        }

        @Override
        public String generateColumnLabel(CategoryDataset arg0, int arg1) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String generateRowLabel(CategoryDataset arg0, int arg1) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public static class CategoryRenderer {
        private List<Paint> color;

        public CategoryRenderer(List<Paint> colors) {
            this.color = colors;
        }

        public void setColor(CategoryPlot plot, DefaultCategoryDataset dataset) {
            List keys = dataset.getRowKeys();
            int aInt;

            for (int i = 0; i < keys.size(); i++) {
                aInt = i % this.color.size();
                plot.getRenderer().setSeriesPaint(i, this.color.get(aInt));
            }
        }
    }

    public static class CustomRenderer3D extends BarRenderer3D {

        private Paint[] colors;

        public CustomRenderer3D(final Paint[] colors) {
            this.colors = colors;
        }

        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

    public static class CustomRenderer extends BarRenderer {

        private Paint[] colors;

        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

}