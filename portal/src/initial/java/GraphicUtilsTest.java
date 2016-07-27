import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicsUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.rastreador2.utils.GraphicsUtils.TICK_LABEL_FONT;

/**
 * Clase para crear gráficas de forma local.
 */
public class GraphicUtilsTest {

    @Test
    public void testGraphicsUtils() throws Exception {
        GraphicsUtils.totalPageStr = "N\u00BA Portales: ";
        GraphicsUtils.totalPage = 18;
        final DefaultPieDataset dataSet = new DefaultPieDataset();

        dataSet.setValue("Parcial", Integer.valueOf(3));
        dataSet.setValue("Prioridad 1", Integer.valueOf(3));
        dataSet.setValue("Prioridad 1 y 2", Integer.valueOf(12));

        final PropertiesManager pmgr = new PropertiesManager();
        GraphicsUtils.createPieChart(dataSet, "Distribución del Nivel de Accesibilidad. Segmento I: Principales", "/home/mikunis/na_seg_1.jpg", "", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
    }

    @Test
    public void testGraphicsPsieChart() throws Exception {
        GraphicsUtils.totalPageStr = "N\u00BA Portales: ";
        GraphicsUtils.totalPage = 35 + 112 + 14;
        final DefaultPieDataset dataSet = new DefaultPieDataset();

        dataSet.setValue("Emperoran", Integer.valueOf(35));
        dataSet.setValue("Se mantienen", Integer.valueOf(112));
        dataSet.setValue("Mejoran", Integer.valueOf(14));


        final PropertiesManager pmgr = new PropertiesManager();
        GraphicsUtils.createPieChart(dataSet, "Distribución según evolución de la puntuación", "/home/mikunis/evol_1.jpg", "", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
    }

    @Test
    public void testComparacionSegmento() throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        dataSet.addValue(17, "No Válido", "Segmento I: Principales");
        dataSet.addValue(42, "No Válido", "Segmento II: Organismos");
        dataSet.addValue(52, "No Válido", "Segmento III: Temáticos");
        dataSet.addValue(57, "No Válido", "Segmento IV: Otros");
        dataSet.addValue(40, "No Válido", "Segmento V: Sedes");

        dataSet.addValue(17, "Prioridad 1", "Segmento I: Principales");
        dataSet.addValue(27, "Prioridad 1", "Segmento II: Organismos");
        dataSet.addValue(14, "Prioridad 1", "Segmento III: Temáticos");
        dataSet.addValue(20, "Prioridad 1", "Segmento IV: Otros");
        dataSet.addValue(21, "Prioridad 1", "Segmento V: Sedes");

        dataSet.addValue(66, "Prioridad 1 y 2", "Segmento I: Principales");
        dataSet.addValue(31, "Prioridad 1 y 2", "Segmento II: Organismos");
        dataSet.addValue(34, "Prioridad 1 y 2", "Segmento III: Temáticos");
        dataSet.addValue(23, "Prioridad 1 y 2", "Segmento IV: Otros");
        dataSet.addValue(39, "Prioridad 1 y 2", "Segmento V: Sedes");

        final PropertiesManager pmgr = new PropertiesManager();
        final String title = "Comparaci\u00F3n de Adecuaci\u00F3n por Segmento";
        final String rowTitle = "Puntuaci\u00F3n";
        final String noDataMess = "noData";

        final ChartForm chartForm = new ChartForm(title, "", rowTitle, dataSet, true, false, false, true, true, false, false, 765, 554, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
        GraphicsUtils.createStackedBarChart(chartForm, noDataMess, "/home/mikunis/adecuacion_seg.jpg");
    }

    @Test
    public void testEvaluacionNivelInformeAgregado() throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        final List<String> dates = Arrays.asList("8/6/2015", "1/9/2015", "15/11/2015", "23/1/2016", "7/6/2016", "10/12/2016");
        final List<String> verificaciones = Arrays.asList("1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5", "1.1.6", "1.1.7", "1.2.1", "1.2.2", "1.2.3");
        final Random random = new Random();

        for (String date : dates) {
            for (String verifacion : verificaciones) {
                dataSet.addValue(random.nextDouble() * 10, date, verifacion);
            }
        }

        final String title = "Evolución de la Puntuación Media por Verificación Nivel de Accesibilidad I";
        final String rowTitle = "Puntuaci\u00F3n";
        final String noDataMess = "noData";

        final ChartForm chartForm = new ChartForm("", "", "", dataSet, true, true, false, false, false, false, false, 765, 554, "{126,154,64}");
        chartForm.setFixedColorBars(true);

        final JFreeChart chart = ChartFactory.createBarChart(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(),
                chartForm.getDataSet(), PlotOrientation.VERTICAL, chartForm.isPrintLegend(), true, false);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage(noDataMess);
        plot.setNoDataMessagePaint(Color.RED);

        //Elimina la transparencia de las gráficas
        plot.setForegroundAlpha(1.0f);

        defineBarColor(plot, chartForm);

        final NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        createNumberAxis(plot, chartForm.isPercentage());

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
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
        domainAxis.setTickLabelFont(GraphicsUtils.TICK_LABEL_FONT);

        GraphicsUtils.saveChartToFile("/home/mikunis/evolucion_agregado.jpg", chart, 1465, 654);
    }

    @Test
    public void testEvolucionNivelConformidad() throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        final List<String> dates = Arrays.asList("8/6/2015", "1/9/2015", "15/11/2015", "23/1/2016", "7/6/2016", "10/12/2016");
        final List<String> conformidades = Arrays.asList("No Válido", "Prioridad 1", "Prioridad 1 y 2");
//        final Random random = new Random();
//
//        for (String conformidad: conformidades) {
//            for (String date: dates) {
//                dataSet.addValue(random.nextDouble()*10 ,conformidad, date);
//            }
//        }

        dataSet.addValue(17, "No Válido", "8/6/2015");
        dataSet.addValue(42, "No Válido", "1/9/2015");
        dataSet.addValue(52, "No Válido", "15/11/2015");
        dataSet.addValue(57, "No Válido", "23/1/2016");
        dataSet.addValue(40, "No Válido", "7/6/2016");

        dataSet.addValue(17, "Prioridad 1", "8/6/2015");
        dataSet.addValue(27, "Prioridad 1", "1/9/2015");
        dataSet.addValue(14, "Prioridad 1", "15/11/2015");
        dataSet.addValue(20, "Prioridad 1", "23/1/2016");
        dataSet.addValue(21, "Prioridad 1", "7/6/2016");

        dataSet.addValue(66, "Prioridad 1 y 2", "8/6/2015");
        dataSet.addValue(31, "Prioridad 1 y 2", "1/9/2015");
        dataSet.addValue(34, "Prioridad 1 y 2", "15/11/2015");
        dataSet.addValue(23, "Prioridad 1 y 2", "23/1/2016");
        dataSet.addValue(39, "Prioridad 1 y 2", "7/6/2016");

        final String noDataMess = "noData";

        final PropertiesManager pmgr = new PropertiesManager();
        final ChartForm chartForm = new ChartForm("", "", "", dataSet, true, false, false, true, true, false, false, 1265, 654, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));

        GraphicsUtils.createStackedBarChart(chartForm, noDataMess, "/home/mikunis/evolucion_nivel_conformidad.jpg");
    }

    private void defineBarColor(final CategoryPlot plot, final ChartForm chartForm) {
        plot.setBackgroundPaint(Color.white);
        if (chartForm.isGridline()) {
            plot.setRangeGridlinePaint(Color.black);
        }

        java.util.List<Paint> colors;
        if (chartForm.getColor() != null && !chartForm.getColor().equals("")) {
            colors = getColors(chartForm.getColor());
        } else {
            colors = getColors("chart.observatory.graphic.intav.colors");
        }

        CategoryItemRenderer renderer = plot.getRenderer();

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
                rendererC = new GraphicsUtils.CustomRenderer3D(paintArray);
            } else {
                rendererC = new GraphicsUtils.CustomRenderer(paintArray);
            }
            plot.setRenderer(rendererC);
        }
    }


    @Test
    public void testEvaluacionPuntuacionMediaAspecto() throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        final List<String> dates = Arrays.asList("8/6/2015", "1/9/2015", "15/11/2015", "23/1/2016", "7/6/2016", "10/12/2016");
        final List<String> verificaciones = Arrays.asList("General", "Presentación", "Estructura", "Navegación", "Alternativas");
        final Random random = new Random();

        for (String date : dates) {
            for (String verifacion : verificaciones) {
                dataSet.addValue(random.nextDouble() * 10, date, verifacion);
            }
        }

        final String noDataMess = "noData";

        final ChartForm chartForm = new ChartForm("", "", "", dataSet, true, true, false, false, false, false, false, 1465, 654, "{126,154,64}");
        chartForm.setFixedColorBars(true);

        final JFreeChart chart = ChartFactory.createBarChart(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(),
                chartForm.getDataSet(), PlotOrientation.VERTICAL, chartForm.isPrintLegend(), true, false);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage(noDataMess);
        plot.setNoDataMessagePaint(Color.RED);

        //Elimina la transparencia de las gráficas
        plot.setForegroundAlpha(1.0f);

        defineBarColor(plot, chartForm);

        final NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        createNumberAxis(plot, chartForm.isPercentage());

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
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

        GraphicsUtils.saveChartToFile("/home/mikunis/evolucion_puntuacion_media_aspectos.jpg", chart, 1465, 654);
    }

    private List<Paint> getColors(String colorsProperty) {

        String regexp = "\\{(.*?),(.*?),(.*?)\\}";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(colorsProperty);
        List<Paint> colors = new ArrayList<Paint>();
        while (matcher.find()) {
            colors.add(new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
        }
        return colors;
    }

    private void createNumberAxis(CategoryPlot plot, boolean percentage) {
        NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();

        if (!percentage) {
            valueAxis.setTickUnit(new NumberTickUnit(1));
            valueAxis.setRangeWithMargins(new Range(0, 10));
        } else {
            valueAxis.setTickUnit(new NumberTickUnit(10));
            valueAxis.setRangeWithMargins(new Range(0, 100));
        }
        valueAxis.setLowerBound(0);
        valueAxis.setTickLabelFont(TICK_LABEL_FONT);
    }
}
