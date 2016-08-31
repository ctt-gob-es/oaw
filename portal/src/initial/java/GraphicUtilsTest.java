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

    private final String[] LEVEL_I_VERIFICATIONS = new String[]{"1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5", "1.1.6", "1.1.7", "1.2.1", "1.2.2", "1.2.3"};
    private final String[] LEVEL_II_VERIFICATIONS = new String[]{"2.1.1", "2.1.2", "2.1.3", "2.1.4", "2.1.5", "2.1.6", "2.1.7", "2.2.1", "2.2.2", "2.2.3"};
    private final String[] ASPECTS = new String[]{"General", "Presentación", "Estructura", "Navegación", "Alternativas"};

    private PropertiesManager pmgr;
    private MessageResources messageResources;

    @Before
    public void init() {
        pmgr = new PropertiesManager();
        messageResources = MessageResources.getMessageResources("ApplicationResources");
    }

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
    public void testEvolutionAverageScore() throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final String color = pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color");

        final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        dataSet.addValue(7.61, "", "18/09/15");
        dataSet.addValue(7.43, "", "23/11/15");
        dataSet.addValue(7.73, "", "22/08/16");
        final ChartForm observatoryGraphicsForm = new ChartForm("", "", "", dataSet, true, false, false, false, false, true, false, 580, 458, color);
        observatoryGraphicsForm.setFixedColorBars(true);
        observatoryGraphicsForm.setOnePixelGraph(false);
        GraphicsUtils.createStandardBarChart(observatoryGraphicsForm, "/home/mikunis/evolution_average_score.jpg", "", messageResources, false);
    }

    @Test
    public void testGraphicsGlobalEvolution() throws Exception {
        GraphicsUtils.totalPageStr = "N\u00BA Portales: ";
        final int empeoran = 20;
        final int mantienen = 94;
        final int mejoran = 32;
        GraphicsUtils.totalPage = empeoran + mantienen + mejoran;

        final DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("Empeoran", Integer.valueOf(empeoran));
        dataSet.setValue("Se mantienen", Integer.valueOf(mantienen));
        dataSet.setValue("Mejoran", Integer.valueOf(mejoran));


        final PropertiesManager pmgr = new PropertiesManager();
        GraphicsUtils.createPieChart(dataSet, "", "/home/mikunis/evol_1.jpg", "", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
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
        final Map<String, Map<String, Double>> data = new HashMap<>();
        populateEvolutionObservatoryData(data, "16/09/15", new double[]{7.20, 6.80, 7.90, 7.30, 7.90, 8.80, 8.80, 7.80, 9.40, 7.10}, LEVEL_I_VERIFICATIONS);
        populateEvolutionObservatoryData(data, "27/11/15", new double[]{7.40, 7.10, 8.10, 7.90, 8.00, 8.80, 8.90, 7.90, 9.50, 4.20}, LEVEL_I_VERIFICATIONS);
        populateEvolutionObservatoryData(data, "19/08/16", new double[]{7.70, 7.40, 8.40, 7.50, 8.20, 9.00, 9.20, 8.30, 9.60, 4.70}, LEVEL_I_VERIFICATIONS);

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
        populateEvolutionObservatoryData(data, "16/09/15", new double[]{8.80, 10.00, 5.90, 8.60, 6.70, 10.00, 5.50, 8.60, 9.70, 6.10}, LEVEL_II_VERIFICATIONS);
        populateEvolutionObservatoryData(data, "27/11/15", new double[]{8.90, 10.00, 6.10, 8.80, 6.70, 10.00, 5.60, 8.70, 9.70, 6.40}, LEVEL_II_VERIFICATIONS);
        populateEvolutionObservatoryData(data, "19/08/16", new double[]{9.10, 10.00, 6.10, 8.90, 6.80, 9.90, 6.00, 8.70, 9.60, 8.90}, LEVEL_II_VERIFICATIONS);

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

        final ChartForm chartForm = new ChartForm("", "", "", dataSet, true, false, false, true, true, false, false, 1265, 654, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
        GraphicsUtils.createStandardBarChart(chartForm, "/home/mikunis/evolucion_nivel_conformidad.jpg", noDataMess, messageResources, false);
    }

    @Test
    public void testEvaluacionPuntuacionMediaAspecto() throws Exception {
        final Map<String, Map<String, Double>> data = new HashMap<>();
        populateEvolutionObservatoryData(data, "16/09/15", new double[]{7.90, 9.10, 7.20, 8.40, 7.20}, ASPECTS);
        populateEvolutionObservatoryData(data, "27/11/15", new double[]{7.50, 9.20, 7.40, 8.50, 7.40}, ASPECTS);
        populateEvolutionObservatoryData(data, "19/08/16", new double[]{7.80, 9.30, 7.50, 8.90, 7.70}, ASPECTS);

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
        final int[] passPercentages = new int[]{95,
                100,
                82,
                86,
                71,
                100,
                66,
                84,
                99,
                94,


        };
        for (int i = 0; i < LEVEL_I_VERIFICATIONS.length; i++) {
            dataSet.addValue(passPercentages[i], "Modalidad pasa", LEVEL_I_VERIFICATIONS[i]);
            dataSet.addValue(100 - passPercentages[i], "Modalidad falla", LEVEL_I_VERIFICATIONS[i]);
        }

        final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 580, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.modality.colors"));
        GraphicsUtils.createStackedBarChart(chartForm, "", "/home/mikunis/modality_verification_level_ii_segment.jpg");
    }

}
