package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatorySiteEvaluationForm;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.MessageResources;
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

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class GraphicsUtils {

    private static final String LABEL_SPACE = "    ";
    private static final Color RED_COLOR = new Color(225, 18, 13);
    private static final Color GREEN_COLOR = new Color(38, 187, 8);
    private static final Color BLUE_COLOR = new Color(15, 91, 255);
    public static String totalPageStr;
    public static long totalPage;
    private static Font TITLE_FONT;
    private static Font TICK_LABEL_FONT;
    private static Font ITEM_LABEL_FONT;
    private static Font LEGEND_FONT;
    private static Font NO_DATA_FONT;

    static {
        try {
            final PropertiesManager pmgr = new PropertiesManager();
            final Font robotoFont = Font.createFont(Font.TRUETYPE_FONT, new File(pmgr.getValue("pdf.properties", "path.pdf.font.monospaced")));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(robotoFont);
            TITLE_FONT = new Font("Roboto", Font.BOLD, 22);
            LEGEND_FONT = new Font("Roboto", Font.PLAIN, 14);
            TICK_LABEL_FONT = new Font("Roboto", Font.PLAIN, 14);
            ITEM_LABEL_FONT = new java.awt.Font("Roboto", Font.BOLD, 12);
            NO_DATA_FONT = new java.awt.Font("Roboto", Font.BOLD, 30);
        } catch (FontFormatException | IOException e) {
            TITLE_FONT  = new java.awt.Font("Arial", Font.BOLD, 22);
            LEGEND_FONT = new java.awt.Font("Arial", Font.PLAIN, 14);
            TICK_LABEL_FONT = new java.awt.Font("Arial", Font.PLAIN, 14);
            ITEM_LABEL_FONT = new java.awt.Font("Arial", Font.BOLD, 12);
            NO_DATA_FONT = new java.awt.Font("Arial", Font.BOLD, 30);
        }
    }

    public enum GraphicFormatType {
        PNG, JPG
    }

    private GraphicsUtils() {
    }

    public static void createPieChart(DefaultPieDataset dataSet, String title,
                                      String filePath, String noDataMessage, String colorsKey, int x, int y) throws Exception {

        JFreeChart chart = ChartFactory.createPieChart3D(title, dataSet, true, true, false);
        chart.getTitle().setFont(TITLE_FONT);
        formatLegend(chart);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);

        plot.setNoDataMessage(noDataMessage);
        plot.setNoDataMessageFont(NO_DATA_FONT);
        plot.setNoDataMessagePaint(Color.RED);

        final Color[] colors = getColors(colorsKey).toArray(new Color[0]);
        PieRenderer renderer = new PieRenderer(colors);
        renderer.setColor(plot, dataSet);

        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);

        plot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
        plot.setCircular(false);
        plot.setOutlineVisible(true);
        plot.setBaseSectionOutlinePaint(Color.BLACK);

        Shape shape = new Rectangle(15, 15);
        plot.setLegendItemShape(shape);

        plot.setLabelGenerator(new CustomLabelGenerator(false));
        plot.setLegendLabelGenerator(new CustomLabelGenerator(true));
        plot.setLabelFont(LEGEND_FONT);

        for (int i = 0; i < plot.getLegendItems().getItemCount(); i++) {
            LegendItem item = plot.getLegendItems().get(i);
            item.setLabelFont(LEGEND_FONT);
        }

        saveChartToFile(filePath, chart, x, y);
    }

    //labelPosition true 45 grados false normal
    public static void createBarChart(Map<String, BigDecimal> result, String title, String rowTitle, String columnTitle,
                                      String color, boolean withLegend, boolean percentage, boolean labelRotated,
                                      String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (Map.Entry<String, BigDecimal> entry : result.entrySet()) {
            dataSet.addValue(entry.getValue(), "", parseLevelLabel(entry.getKey(), messageResources));
        }
        createBarChart(dataSet, title, rowTitle, columnTitle, color, withLegend, percentage, labelRotated, filePath, noDataMessage, messageResources, x, y);
    }

    //labelRotated true 45 grados false normal
    public static void createBarChart(DefaultCategoryDataset dataSet, String title, String rowTitle, String columnTitle,
                                      String color, boolean withLegend, boolean percentage, boolean labelRotated,
                                      String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws Exception {
        final ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, percentage, withLegend, labelRotated, false, x, y, color);
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
    }

    public static void createSeriesBarChart(ChartForm observatoryGraphicsForm,
                                            String filePath, String noDataMessage, final MessageResources messageResources, boolean withRange) throws Exception {
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, withRange);
    }

    public static void createBar1PxChart(final List<ObservatorySiteEvaluationForm> result, String title, String rowTitle, String columnTitle,
                                         final String filePath, final String noDataMessage, final MessageResources messageResources, int x, int y, boolean showColumnsLabels) throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        final StringBuilder colors = new StringBuilder();
        for (ObservatorySiteEvaluationForm observatorySiteEvaluationForm : result) {
            getRequiredColors(colors, observatorySiteEvaluationForm.getLevel());

            if (observatorySiteEvaluationForm.getAcronym() != null && !StringUtils.isEmpty(observatorySiteEvaluationForm.getAcronym())) {
                dataSet.addValue(observatorySiteEvaluationForm.getScore(), "", observatorySiteEvaluationForm.getAcronym());
            } else {
                dataSet.addValue(observatorySiteEvaluationForm.getScore(), "", observatorySiteEvaluationForm.getName());
            }
        }

        final ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, false, false, true, false, true, true, true, x, y, colors.toString());
        observatoryGraphicsForm.setFixedColorBars(true);
        observatoryGraphicsForm.setFixedLegend(true);
        observatoryGraphicsForm.setShowColumsLabels(showColumnsLabels);
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
    }

    public static void createBarPageByLevelChart(List<ObservatoryEvaluationForm> result, String title, String rowTitle, String columnTitle,
                                                 String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        final StringBuilder colors = new StringBuilder();
        int i = 1;
        for (ObservatoryEvaluationForm observatoryEvaluationForm : result) {
            getRequiredColors(colors, ObservatoryUtils.pageSuitabilityLevel(observatoryEvaluationForm));

            dataSet.addValue(observatoryEvaluationForm.getScore().setScale(observatoryEvaluationForm.getScore().scale() - 1, BigDecimal.ROUND_UNNECESSARY), "", messageResources.getMessage("observatory.graphic.score.by.page.label", i++));
        }

        ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, false, true, true, true, x, y, colors.toString());
        observatoryGraphicsForm.setFixedLegend(true);
        observatoryGraphicsForm.setFixedColorBars(true);
        createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
    }

    /**
     * Builds a comma (,) separated string with the required colors
     * @param colors a StringBuilder object where append the colors
     * @param level a String representing the suitability (conformance level)
     */
    private static void getRequiredColors(final StringBuilder colors, final String level) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (Constants.OBS_NV.equalsIgnoreCase(level) ||
                Constants.OBS_PARCIAL.equalsIgnoreCase(level)) {
            colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.red.color")).append(",");
        }
        if (Constants.OBS_A.equalsIgnoreCase(level)) {
            colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.yellow.color")).append(",");
        }
        if (Constants.OBS_AA.equalsIgnoreCase(level)) {
            colors.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.green.color")).append(",");
        }
    }

    public static void createStackedBarChart(final ChartForm chartForm, final String noDataMess, final String filePath) throws IOException {
        final JFreeChart chart = ChartFactory.createStackedBarChart3D(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(), chartForm.getDataSet(),
                PlotOrientation.VERTICAL, chartForm.isPrintLegend(), true, false);

        formatLegend(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);

        plot.setNoDataMessage(noDataMess);
        plot.setNoDataMessageFont(NO_DATA_FONT);
        plot.setNoDataMessagePaint(Color.RED);

        List<Paint> colors = getColors(chartForm.getColor());

        CategoryRenderer renderer = new CategoryRenderer(colors);
        renderer.setColor(plot, chartForm.getDataSet());

        if (chartForm.isPrintLegend()) {
            plot.setFixedLegendItems(generateLegend(colors, plot));
        }

        BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setBaseItemLabelGenerator(new LabelGenerator(true));
        barRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        barRenderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
        barRenderer.setBaseItemLabelsVisible(true);
        barRenderer.setDrawBarOutline(true);
        barRenderer.setMaximumBarWidth(0.1);
        barRenderer.setBaseOutlinePaint(Color.BLACK);

        itemLabelColor(barRenderer, colors);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setMaximumCategoryLabelLines(3);
        domainAxis.setUpperMargin(0.01);
        domainAxis.setLowerMargin(0.01);

        if (!chartForm.isRoundLabelPosition()) {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        } else {
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        }
        domainAxis.setTickLabelFont(TICK_LABEL_FONT);

        NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        createNumberAxis(plot, true);

        saveChartToFile(filePath, chart, chartForm.getX(), chartForm.getY());

    }

    private static void itemLabelColor(BarRenderer barRenderer, List<Paint> colors) {
        int serie = 0;
        for (Paint color : colors) {
            if (color.equals(RED_COLOR) || color.equals(GREEN_COLOR) || color.equals(BLUE_COLOR)) {
                barRenderer.setSeriesItemLabelPaint(serie, Color.WHITE);
            } else {
                barRenderer.setSeriesItemLabelPaint(serie, Color.BLACK);
            }
            serie++;
        }
    }

    public static void createStandardBarChart(ChartForm observatoryGraphicsForm,
                                              String filePath, String noDataMess, MessageResources messageResources, boolean withRange) throws Exception {

        final JFreeChart chart;

        if (observatoryGraphicsForm.isTridimensional()) {
            chart = ChartFactory.createBarChart3D(observatoryGraphicsForm.getTitle(), observatoryGraphicsForm.getColumnTitle(), observatoryGraphicsForm.getRowTitle(),
                    observatoryGraphicsForm.getDataSet(), PlotOrientation.VERTICAL, observatoryGraphicsForm.isPrintLegend(), true, false);
        } else {
            chart = ChartFactory.createBarChart(observatoryGraphicsForm.getTitle(), observatoryGraphicsForm.getColumnTitle(), observatoryGraphicsForm.getRowTitle(),
                    observatoryGraphicsForm.getDataSet(), PlotOrientation.VERTICAL, observatoryGraphicsForm.isPrintLegend(), true, false);
        }
        chart.getTitle().setFont(TITLE_FONT);

        formatLegend(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage(noDataMess);
        plot.setNoDataMessageFont(NO_DATA_FONT);
        plot.setNoDataMessagePaint(Color.RED);

        //Elimina la transparencia de las gráficas
        plot.setForegroundAlpha(1.0f);

        defineBarColor(plot, observatoryGraphicsForm, messageResources);

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

        saveChartToFile(filePath, chart, observatoryGraphicsForm.getX(), observatoryGraphicsForm.getY());
    }

    public static String parseLevelLabel(final String key, final MessageResources messageResources) {
        if (key.equals(Constants.OBS_A)) {
            return messageResources.getMessage("observatory.graphics.level.A");
        } else if (key.equals(Constants.OBS_AA)) {
            return messageResources.getMessage("observatory.graphics.level.AA");
        } else if (key.equals(Constants.OBS_NV) || key.equals(Constants.OBS_PARCIAL)) {
            return messageResources.getMessage("observatory.graphics.level.Parcial");
        } else {
            return key;
        }
    }

    private static void createNumberAxis(final CategoryPlot plot, boolean percentage) {
        final NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();

        if (!percentage) {
            valueAxis.setTickUnit(new NumberTickUnit(1));
            valueAxis.setRangeWithMargins(new Range(0, 10));
        } else {
            valueAxis.setTickUnit(new NumberTickUnit(10));
            valueAxis.setRangeWithMargins(new Range(0, 100));
        }
        valueAxis.setTickLabelFont(TICK_LABEL_FONT);
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
        domainAxis.setTickLabelFont(TICK_LABEL_FONT);
        domainAxis.setUpperMargin(0.01);
        domainAxis.setLowerMargin(0.01);
    }

    private static void defineBarColor(final CategoryPlot plot, final ChartForm chartForm, final MessageResources messageResources) {
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

        CategoryItemRenderer renderer = plot.getRenderer();
        if (chartForm.isPrintLegend()) {
            if (chartForm.isFixedLegend()) {
                plot.setFixedLegendItems(generateOnePixelLegend(messageResources));
            } else {
                if (!chartForm.isOnePixelGraph()) {
                    plot.setFixedLegendItems(generateLegend(colors, plot));
                } else {
                    plot.setFixedLegendItems(generateOnePixelLegend(messageResources));
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
            lt.setItemFont(LEGEND_FONT);
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
            item = new LegendItem(item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, shape, colors.get(i), stroke, Color.black);
            newLegend.add(item);
        }
        return newLegend;
    }

    private static LegendItemCollection generateOnePixelLegend(final MessageResources messageResources) {

        PropertiesManager pmgr = new PropertiesManager();
        String[] labels = {parseLevelLabel(Constants.OBS_NV, messageResources), parseLevelLabel(Constants.OBS_A, messageResources), parseLevelLabel(Constants.OBS_AA, messageResources)};
        List<Paint> colors = getColors(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));

        //Se crea una leyenda fija
        LegendItemCollection legend = new LegendItemCollection();
        Shape shape = new Rectangle(15, 15);
        BasicStroke stroke = new BasicStroke();

        if (colors.size() == labels.length) {
            int i = 0;
            for (String label : labels) {
                LegendItem item = new LegendItem(label + LABEL_SPACE, label + LABEL_SPACE, label + LABEL_SPACE, label + LABEL_SPACE, shape, colors.get(i), stroke, Color.black);
                legend.add(item);
                i++;
            }
        }
        return legend;
    }

    private static void putValuesOnBars(final JFreeChart chart, final ChartForm chartForm) {
        final CategoryPlot plot = chart.getCategoryPlot();

        final BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
        barRenderer.setShadowVisible(false);

        if (chartForm.isOnePixelGraph()) {
            barRenderer.setMaximumBarWidth(0.005);
        } else if (chartForm.isShowColumsLabels()){
            barRenderer.setMaximumBarWidth(0.1);
            barRenderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
            barRenderer.setBaseItemLabelGenerator(new LabelGenerator(chartForm.isPercentage()));
            barRenderer.setBaseItemLabelsVisible(true);
            barRenderer.setDrawBarOutline(true);
            barRenderer.setBaseOutlinePaint(Color.GRAY);

            if (chartForm.isTridimensional()) {
                final CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
                renderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
                renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
                ((BarRenderer3D) renderer).setItemLabelAnchorOffset(10D);
            }
        }

        barRenderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
    }

    public static void saveChartToFile(final String filePath, final JFreeChart chart, int x, int y) throws IOException {
       saveChartToFile(filePath, chart, x, y, GraphicFormatType.JPG);
    }

    public static void saveChartToFile(final String filePath, final JFreeChart chart, final int x, final int y, final GraphicFormatType mimeType) throws IOException {
        final File file = new File(filePath);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            Logger.putLog("Error al crear el archivo con la imagen de la gráfica", GraphicsUtils.class, Logger.LOG_LEVEL_ERROR);
        }
        file.createNewFile();

        try (FileOutputStream out = new FileOutputStream(file)) {
            if (mimeType.equals(GraphicFormatType.JPG)) {
                ChartUtilities.writeChartAsJPEG(out, chart, x, y);
            } else {
                ChartUtilities.writeChartAsPNG(out, chart, x, y);
            }
        } catch (Exception e) {
            Logger.putLog("Error guardar el archivo con la imagen de la gráfica: " + e.getMessage(), GraphicsUtils.class, Logger.LOG_LEVEL_ERROR);
        }
    }


    private static List<Paint> getColors(final String colorsProperty) {
        String regexp = "\\{(.*?),(.*?),(.*?)\\}";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(colorsProperty);
        List<Paint> colors = new ArrayList<>();
        while (matcher.find()) {
            colors.add(new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
        }
        return colors;
    }

    public static class PieRenderer {
        private Color[] color;

        public PieRenderer(Color[] colors) {
            this.color = Arrays.copyOf(colors, colors.length);
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
                    Float value = ((Float.valueOf(dataset.getValue(key).toString()) / (float) totalPage) * 100);
                    value = value * p;
                    float tmp = Math.round(value);
                    value = tmp / p;
                    return key + "\n" + totalPageStr + dataset.getValue(key).toString() + "\n" + " (" + value.toString() + "%)";
                } else {
                    return null;
                }
            } else {
                return key + LABEL_SPACE;
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
            this.colors = Arrays.copyOf(colors, colors.length);
        }

        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

    public static class CustomRenderer extends BarRenderer {

        private Paint[] colors;

        public CustomRenderer(final Paint[] colors) {
            this.colors = Arrays.copyOf(colors, colors.length);;
        }

        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }

}