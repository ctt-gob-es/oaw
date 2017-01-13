import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicsUtils;
import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase para crear gráficas de forma local.
 */
public class GraphicUtilsTest {

    private static final String[] LEVEL_I_VERIFICATIONS = new String[]{"1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5", "1.1.6", "1.1.7", "1.2.1", "1.2.2", "1.2.3"};
    private static final String[] LEVEL_II_VERIFICATIONS = new String[]{"2.1.1", "2.1.2", "2.1.3", "2.1.4", "2.1.5", "2.1.6", "2.1.7", "2.2.1", "2.2.2", "2.2.3"};
    private static final String[] ASPECTS = new String[]{"General", "Presentación", "Estructura", "Navegación", "Alternativas"};

    private PropertiesManager pmgr;
    private MessageResources messageResources;

    @Before
    public void init() {
        pmgr = new PropertiesManager();
        messageResources = MessageResources.getMessageResources("ApplicationResources");
    }

    @Test
    public void testGraphicsUtils() throws Exception {
        final String sectionLabel = "N\u00BA Portales: ";
        final int total = 18;
        final DefaultPieDataset dataSet = new DefaultPieDataset();

        dataSet.setValue("Parcial", Integer.valueOf(3));
        dataSet.setValue("Prioridad 1", Integer.valueOf(3));
        dataSet.setValue("Prioridad 1 y 2", Integer.valueOf(12));

        final PropertiesManager pmgr = new PropertiesManager();
        GraphicsUtils.createPieChart(dataSet, "Distribución del Nivel de Accesibilidad. Segmento I: Principales", sectionLabel, total, "/home/mikunis/na_seg_1.jpg", "", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
    }

    @Test
    public void testEvolutionAverageScore() throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final String color = "{225,18,13},{255,225,0},{38,187,8},{15,91,255}";

        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        dataSet.addValue(7.61, "", "18/09/15");
        dataSet.addValue(7.43, "", "23/11/15");
        dataSet.addValue(7.73, "", "22/08/16");
        dataSet.addValue(6.30, "", "17/10/16");
        final ChartForm observatoryGraphicsForm = new ChartForm("", "", "", dataSet, true, true, false, false, true, true, false, 580, 458, color);
        //final ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, false, false, true, false, true, true, true, x, y, colors.toString());
        observatoryGraphicsForm.setFixedColorBars(true);
        observatoryGraphicsForm.setFixedLegend(true);
        //observatoryGraphicsForm.setShowColumsLabels(true);
        GraphicsUtils.createStandardBarChart(observatoryGraphicsForm, "/home/mikunis/evolution_average_score.jpg", "", messageResources, false);
    }

    @Test
    public void testGraphicsGlobalEvolution() throws Exception {
        final int empeoran = 28;
        final int mantienen = 105;
        final int mejoran =   28;

        final String sectionLabel = "N\u00BA Portales: ";
        final int total = empeoran + mantienen + mejoran;

        final DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("Empeoran", Integer.valueOf(empeoran));
        dataSet.setValue("Se mantienen", Integer.valueOf(mantienen));
        dataSet.setValue("Mejoran", Integer.valueOf(mejoran));


        final PropertiesManager pmgr = new PropertiesManager();
        GraphicsUtils.createPieChart(dataSet, "", sectionLabel, total, "/home/mikunis/evol_1.jpg", "", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
    }

    @Test
    public void testComparacionSegmento() throws Exception {
        // Comparacion de Adecuacion por Segmento
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        dataSet.addValue(72, "Parcial", "Diputaciones Provinciales");
        dataSet.addValue(72, "Parcial", "Ayuntamiento de capital");
        dataSet.addValue(88, "Parcial", "Municipios más poblados");
        dataSet.addValue(96, "Parcial", "Municipios 2000-5000 Habit.");

        dataSet.addValue(15, "Prioridad 1", "Diputaciones Provinciales");
        dataSet.addValue(14, "Prioridad 1", "Ayuntamiento de capital");
        dataSet.addValue(8, "Prioridad 1", "Municipios más poblados");
        dataSet.addValue(4, "Prioridad 1", "Municipios 2000-5000 Habit.");

        dataSet.addValue(13, "Prioridad 1 y 2", "Diputaciones Provinciales");
        dataSet.addValue(14, "Prioridad 1 y 2", "Ayuntamiento de capital");
        dataSet.addValue(4, "Prioridad 1 y 2", "Municipios más poblados");
        dataSet.addValue(0, "Prioridad 1 y 2", "Municipios 2000-5000 Habit.");

        final PropertiesManager pmgr = new PropertiesManager();
        final String noDataMess = "noData";

        //final ChartForm chartForm = new ChartForm("", "", "", dataSet, true, false, false, true, true, false, false, 580, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
        final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 580, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
        //final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 980, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
        GraphicsUtils.createStackedBarChart(chartForm, noDataMess, "/home/mikunis/adecuacion_seg.jpg");
    }

    @Test
    public void testEvaluacionNivelInformeAgregado() throws Exception {
        final Map<String, Map<String, Double>> data = new LinkedHashMap<>();

        populateEvolutionObservatoryData(data, "12/10/15", new double[]{6.20,
                4.60,
                6.90,
                6.40,
                5.90,
                7.80,
                7.70,
                8.80,
                9.30,
                4.20}, LEVEL_I_VERIFICATIONS);

        populateEvolutionObservatoryData(data, "01/12/15", new double[]{5.90,
                4.50,
                7.10,
                6.80,
                5.80,
                7.70,
                7.60,
                8.80,
                9.40,
                1.60}, LEVEL_I_VERIFICATIONS);

        populateEvolutionObservatoryData(data, "26/08/16", new double[]{5.90,
                4.40,
                7,
                6.70,
                5.80,
                7.70,
                7.80,
                8.70,
                9.50,
                1.70}, LEVEL_I_VERIFICATIONS);

        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (Map.Entry<String, Map<String, Double>> observatoryEntry : data.entrySet()) {
            for (Map.Entry<String, Double> columnsValues : observatoryEntry.getValue().entrySet()) {
                dataSet.addValue(columnsValues.getValue(), observatoryEntry.getKey(), columnsValues.getKey());
            }
        }

        final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
        chartForm.setFixedColorBars(true);
        chartForm.setShowColumsLabels(false);
        GraphicsUtils.createStandardBarChart(chartForm, "/home/mikunis/evolucion_agregado_level_i.jpg", "", messageResources, true);

        data.clear();

        populateEvolutionObservatoryData(data, "12/10/15", new double[]{8.10,
                10,
                4.40,
                7.80,
                4.40,
                9.90,
                4,
                8.60,
                9.40,
                5.40}, LEVEL_II_VERIFICATIONS);

        populateEvolutionObservatoryData(data, "01/12/15", new double[]{8.10,
                10,
                4.30,
                8.10,
                4,
                9.90,
                3.60,
                8.40,
                9.40,
                4.80}, LEVEL_II_VERIFICATIONS);

        populateEvolutionObservatoryData(data, "26/08/16", new double[]{8.10,
                10,
                4.80,
                8.40,
                4,
                9.90,
                3.80,
                8.50,
                9.30,
                8.10}, LEVEL_II_VERIFICATIONS);

        dataSet.clear();
        for (Map.Entry<String, Map<String, Double>> observatoryEntry : data.entrySet()) {
            for (Map.Entry<String, Double> columnsValues : observatoryEntry.getValue().entrySet()) {
                dataSet.addValue(columnsValues.getValue(), observatoryEntry.getKey(), columnsValues.getKey());
            }
        }
        chartForm.setDataSet(dataSet);
        GraphicsUtils.createStandardBarChart(chartForm, "/home/mikunis/evolucion_agregado_level_ii.jpg", "", messageResources, true);
    }

    private void populateEvolutionObservatoryData(final Map<String, Map<String, Double>> data, final String observatoryDate, final double[] values, final String[] columnsKeys) {
        assert values.length == columnsKeys.length;
        final Map<String, Double> columnsValues = new LinkedHashMap<>();
        for (int i = 0; i < columnsKeys.length; i++) {
            columnsValues.put(columnsKeys[i], values[i]);
        }
        data.put(observatoryDate, columnsValues);
    }

    @Test
    public void testEvolucionNivelConformidad() throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

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

        final ChartForm chartForm = new ChartForm("", "", "", dataSet, true, false, false, true, true, false, false, 1265, 654, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
        GraphicsUtils.createStackedBarChart(chartForm, noDataMess, "/home/mikunis/evolucion_nivel_conformidad.jpg");
    }

    @Test
    public void testEvaluacionPuntuacionMediaAspecto() throws Exception {
        final Map<String, Map<String, Double>> data = new LinkedHashMap<>();
        populateEvolutionObservatoryData(data, "12/10/15", new double[]{7.00, 8.60, 5.60, 7.70, 6.20}, ASPECTS);
        populateEvolutionObservatoryData(data, "01/12/15", new double[]{6.50, 8.60, 5.70, 7.50, 5.90}, ASPECTS);
        populateEvolutionObservatoryData(data, "26/08/16", new double[]{6.60, 8.60, 5.70, 8.10, 5.90}, ASPECTS);

        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (Map.Entry<String, Map<String, Double>> observatoryEntry : data.entrySet()) {
            for (Map.Entry<String, Double> columnsValues : observatoryEntry.getValue().entrySet()) {
                dataSet.addValue(columnsValues.getValue(), observatoryEntry.getKey(), columnsValues.getKey());
            }
        }

        final ChartForm chartForm = new ChartForm(dataSet, true, false, false, false, false, false, false, 1565, 684, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
        chartForm.setFixedColorBars(true);
        chartForm.setShowColumsLabels(false);

        GraphicsUtils.createStandardBarChart(chartForm, "/home/mikunis/evolucion_puntuacion_media_aspectos.jpg", "", messageResources, true);
    }

    @Test
    public void testModalityVerificationSegment() throws Exception {
        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        final int[] passPercentages = new int[]{
                78,
                100,
                45,
                78,
                33,
                98,
                30,
                79,
                90,
                73
        };
        for (int i = 0; i < LEVEL_II_VERIFICATIONS.length; i++) {
            dataSet.addValue(passPercentages[i], "Modalidad pasa", LEVEL_II_VERIFICATIONS[i]);
            dataSet.addValue(100 - passPercentages[i], "Modalidad falla", LEVEL_II_VERIFICATIONS[i]);
        }

        final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 580, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.modality.colors"));
        GraphicsUtils.createStackedBarChart(chartForm, "", "/home/mikunis/modality_verification_level_ii_segment_iv.jpg");
    }

}
