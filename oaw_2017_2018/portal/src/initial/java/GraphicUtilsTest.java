import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.Before;
import org.junit.Test;

import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicsUtils;

/**
 * Clase para crear gráficas de forma local.
 */
public class GraphicUtilsTest {

	private static final String OBSERVATORIO_AGE = "AGE";
	private static final String OBSERVATORIO_CCAA = "CCAA";
	private static final String OBSERVATORIO_EELL = "EELL";

	private static final String AGE_SEDES = "Sedes";
	private static final String AGE_OTROS = "Otros";
	private static final String AGE_TEMATICOS = "Temáticos";
	private static final String AGE_ORGANISMOS = "Organismos";
	private static final String AGE_PRINCIPALES = "Principales";
	private static final String PRIORIDAD_1_Y_2 = "Prioridad 1 y 2";
	private static final String PRIORIDAD_1 = "Prioridad 1";
	private static final String PARCIAL = "Parcial";
	private static final String EELL_MUNICIPIOS_2000_5000_HABIT = "Municipios 2000-5000 Habit.";
	private static final String EELL_MUNICIPIOS_MAS_POBLADOS = "Municipios más poblados";
	private static final String EELL_AYUNTAMIENTO_DE_CAPITAL = "Ayuntamiento de capital";
	private static final String EELL_DIPUTACIONES_PROVINCIALES = "Diputaciones Provinciales";
	private static final String[] LEVEL_I_VERIFICATIONS = new String[] { "1.1.1", "1.1.2", "1.1.3", "1.1.4", "1.1.5", "1.1.6", "1.1.7", "1.2.1", "1.2.2", "1.2.3" };
	private static final String[] LEVEL_II_VERIFICATIONS = new String[] { "2.1.1", "2.1.2", "2.1.3", "2.1.4", "2.1.5", "2.1.6", "2.1.7", "2.2.1", "2.2.2", "2.2.3" };
	private static final String[] ASPECTS = new String[] { "General", "Presentación", "Estructura", "Navegación", "Alternativas" };

	private PropertiesManager pmgr;
	private MessageResources messageResources;

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	private String stringDate;

	private String observatorio;

	@Before
	public void init() {

		observatorio = OBSERVATORIO_AGE;

		pmgr = new PropertiesManager();
		messageResources = MessageResources.getMessageResources("ApplicationResources");
		stringDate = observatorio + "-" + df.format(new Date());
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
		GraphicsUtils.createPieChart(dataSet, "Distribución del Nivel de Accesibilidad. Segmento I: Principales", sectionLabel, total,
				"/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/na_seg_1.jpg", "", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
	}

	@Test
	public void testEvolutionAverageScore() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		final String color = "{225,18,13},{255,225,0},{38,187,8},{15,91,255}";

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		dataSet.addValue(7.80, "", "18/09/15");
		dataSet.addValue(7.43, "", "23/11/15");
		dataSet.addValue(7.73, "", "22/08/16");
		dataSet.addValue(6.30, "", "17/10/16");
		dataSet.addValue(6.30, "", "27/10/17");
		final ChartForm observatoryGraphicsForm = new ChartForm("", "", "", dataSet, true, true, false, false, true, true, true, 580, 458, color);
		// final ChartForm observatoryGraphicsForm = new ChartForm(title,
		// columnTitle, rowTitle, dataSet, false, false, true, false, true,
		// true, true, x, y, colors.toString());
		observatoryGraphicsForm.setFixedColorBars(true);
		observatoryGraphicsForm.setFixedLegend(true);
		// observatoryGraphicsForm.setShowColumsLabels(true);
		GraphicsUtils.createStandardBarChart(observatoryGraphicsForm, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/nevolution_average_score.jpg", "", messageResources, true);
	}
	
	@Test	
	public void testEvolutionGraphic() throws Exception {


		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		
		dataSet.addValue(7.80, "18/09/15", "1.1.1");
		dataSet.addValue(7.43, "18/09/16", "1.1.1");
		dataSet.addValue(7.43, "18/09/17", "1.1.1");
		dataSet.addValue(7.73, "18/09/15", "1.1.2");
		dataSet.addValue(6.30, "18/09/16", "1.1.2");
		dataSet.addValue(6.30, "18/09/17", "1.1.2");

		final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, true, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
		chartForm.setFixedColorBars(false);
		chartForm.setShowColumsLabels(false);

		GraphicsUtils.createStandardBarChart(chartForm, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/nevolution_score.jpg", "", messageResources, true);
	}

	@Test
	public void testGraphicsGlobalEvolution() throws Exception {
		final int mejoran = 102;
		final int mantienen = 237;
		final int empeoran = 35;

		final String sectionLabel = "N\u00BA Portales: ";
		final int total = empeoran + mantienen + mejoran;

		final DefaultPieDataset dataSet = new DefaultPieDataset();
		dataSet.setValue("Empeoran", Integer.valueOf(empeoran));
		dataSet.setValue("Se mantienen", Integer.valueOf(mantienen));
		dataSet.setValue("Mejoran", Integer.valueOf(mejoran));

		final PropertiesManager pmgr = new PropertiesManager();
		GraphicsUtils.createPieChart(dataSet, "", sectionLabel, total, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/evol_anterior.jpg", "",
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
	}

	@Test
	public void testGraphicsGlobalEvolutionPri() throws Exception {
		final int mejoran = 206;
		final int mantienen = 98;
		final int empeoran = 70;

		final String sectionLabel = "N\u00BA Portales: ";
		final int total = empeoran + mantienen + mejoran;

		final DefaultPieDataset dataSet = new DefaultPieDataset();
		dataSet.setValue("Empeoran", Integer.valueOf(empeoran));
		dataSet.setValue("Se mantienen", Integer.valueOf(mantienen));
		dataSet.setValue("Mejoran", Integer.valueOf(mejoran));

		final PropertiesManager pmgr = new PropertiesManager();
		GraphicsUtils.createPieChart(dataSet, "", sectionLabel, total, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/evol_primera.jpg", "",
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
	}

	@Test
	public void testComparacionSegmento() throws Exception {
		// Comparacion de Adecuacion por Segmento

		DefaultCategoryDataset dataSet = null;

		switch (observatorio) {
		case OBSERVATORIO_AGE:
			dataSet = generateDataSetSegmentosAGE();
			break;
		case OBSERVATORIO_EELL:
			dataSet = generateDataSetSegmentosEELL();
			break;

		default:
			break;
		}

		final PropertiesManager pmgr = new PropertiesManager();
		final String noDataMess = "noData";
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 580, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		GraphicsUtils.createStackedBarChart(chartForm, noDataMess, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/adecuacion_seg.jpg");
	}

	private DefaultCategoryDataset generateDataSetSegmentosAGE() {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		dataSet.addValue(72, PARCIAL, AGE_PRINCIPALES);
		dataSet.addValue(72, PARCIAL, AGE_ORGANISMOS);
		dataSet.addValue(88, PARCIAL, AGE_TEMATICOS);
		dataSet.addValue(96, PARCIAL, AGE_OTROS);
		dataSet.addValue(96, PARCIAL, AGE_SEDES);

		dataSet.addValue(72, PRIORIDAD_1, AGE_PRINCIPALES);
		dataSet.addValue(72, PRIORIDAD_1, AGE_ORGANISMOS);
		dataSet.addValue(88, PRIORIDAD_1, AGE_TEMATICOS);
		dataSet.addValue(96, PRIORIDAD_1, AGE_OTROS);
		dataSet.addValue(96, PRIORIDAD_1, AGE_SEDES);

		dataSet.addValue(72, PRIORIDAD_1_Y_2, AGE_PRINCIPALES);
		dataSet.addValue(72, PRIORIDAD_1_Y_2, AGE_ORGANISMOS);
		dataSet.addValue(88, PRIORIDAD_1_Y_2, AGE_TEMATICOS);
		dataSet.addValue(96, PRIORIDAD_1_Y_2, AGE_OTROS);
		dataSet.addValue(96, PRIORIDAD_1_Y_2, AGE_SEDES);

		return dataSet;
	}

	private DefaultCategoryDataset generateDataSetSegmentosEELL() {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		dataSet.addValue(72, PARCIAL, EELL_DIPUTACIONES_PROVINCIALES);
		dataSet.addValue(72, PARCIAL, EELL_AYUNTAMIENTO_DE_CAPITAL);
		dataSet.addValue(88, PARCIAL, EELL_MUNICIPIOS_MAS_POBLADOS);
		dataSet.addValue(96, PARCIAL, EELL_MUNICIPIOS_2000_5000_HABIT);

		dataSet.addValue(15, PRIORIDAD_1, EELL_DIPUTACIONES_PROVINCIALES);
		dataSet.addValue(14, PRIORIDAD_1, EELL_AYUNTAMIENTO_DE_CAPITAL);
		dataSet.addValue(8, PRIORIDAD_1, EELL_MUNICIPIOS_MAS_POBLADOS);
		dataSet.addValue(4, PRIORIDAD_1, EELL_MUNICIPIOS_2000_5000_HABIT);

		dataSet.addValue(13, PRIORIDAD_1_Y_2, EELL_DIPUTACIONES_PROVINCIALES);
		dataSet.addValue(14, PRIORIDAD_1_Y_2, EELL_AYUNTAMIENTO_DE_CAPITAL);
		dataSet.addValue(4, PRIORIDAD_1_Y_2, EELL_MUNICIPIOS_MAS_POBLADOS);
		dataSet.addValue(0, PRIORIDAD_1_Y_2, EELL_MUNICIPIOS_2000_5000_HABIT);
		return dataSet;
	}

	@Test
	public void testEvaluacionNivelInformeAgregado() throws Exception {
		final Map<String, Map<String, Double>> data = new LinkedHashMap<>();

		populateEvolutionObservatoryData(data, "12/10/15", new double[] { 6.20, 4.60, 6.90, 6.40, 5.90, 7.80, 7.70, 8.80, 9.30, 4.20 }, LEVEL_I_VERIFICATIONS);

		populateEvolutionObservatoryData(data, "01/12/15", new double[] { 5.90, 4.50, 7.10, 6.80, 5.80, 7.70, 7.60, 8.80, 9.40, 1.60 }, LEVEL_I_VERIFICATIONS);

		populateEvolutionObservatoryData(data, "26/08/16", new double[] { 5.90, 4.40, 7, 6.70, 5.80, 7.70, 7.80, 8.70, 9.50, 1.70 }, LEVEL_I_VERIFICATIONS);

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Map<String, Double>> observatoryEntry : data.entrySet()) {
			for (Map.Entry<String, Double> columnsValues : observatoryEntry.getValue().entrySet()) {
				dataSet.addValue(columnsValues.getValue(), observatoryEntry.getKey(), columnsValues.getKey());
			}
		}

		final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
		chartForm.setFixedColorBars(true);
		chartForm.setShowColumsLabels(false);
		GraphicsUtils.createStandardBarChart(chartForm, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/evolucion_agregado_level_i.jpg", "", messageResources, true);

		data.clear();

		populateEvolutionObservatoryData(data, "12/10/15", new double[] { 8.10, 10, 4.40, 7.80, 4.40, 9.90, 4, 8.60, 9.40, 5.40 }, LEVEL_II_VERIFICATIONS);

		populateEvolutionObservatoryData(data, "01/12/15", new double[] { 8.10, 10, 4.30, 8.10, 4, 9.90, 3.60, 8.40, 9.40, 4.80 }, LEVEL_II_VERIFICATIONS);

		populateEvolutionObservatoryData(data, "26/08/16", new double[] { 8.10, 10, 4.80, 8.40, 4, 9.90, 3.80, 8.50, 9.30, 8.10 }, LEVEL_II_VERIFICATIONS);

		dataSet.clear();
		for (Map.Entry<String, Map<String, Double>> observatoryEntry : data.entrySet()) {
			for (Map.Entry<String, Double> columnsValues : observatoryEntry.getValue().entrySet()) {
				dataSet.addValue(columnsValues.getValue(), observatoryEntry.getKey(), columnsValues.getKey());
			}
		}
		chartForm.setDataSet(dataSet);
		GraphicsUtils.createStandardBarChart(chartForm, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/evolucion_agregado_level_ii.jpg", "", messageResources, true);
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

		dataSet.addValue(17, PRIORIDAD_1, "8/6/2015");
		dataSet.addValue(27, PRIORIDAD_1, "1/9/2015");
		dataSet.addValue(14, PRIORIDAD_1, "15/11/2015");
		dataSet.addValue(20, PRIORIDAD_1, "23/1/2016");
		dataSet.addValue(21, PRIORIDAD_1, "7/6/2016");

		dataSet.addValue(66, PRIORIDAD_1_Y_2, "8/6/2015");
		dataSet.addValue(31, PRIORIDAD_1_Y_2, "1/9/2015");
		dataSet.addValue(34, PRIORIDAD_1_Y_2, "15/11/2015");
		dataSet.addValue(23, PRIORIDAD_1_Y_2, "23/1/2016");
		dataSet.addValue(39, PRIORIDAD_1_Y_2, "7/6/2016");

		final String noDataMess = "noData";

		final ChartForm chartForm = new ChartForm("", "", "", dataSet, true, false, false, true, true, false, false, 1265, 654,
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		GraphicsUtils.createStackedBarChart(chartForm, noDataMess, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/evolucion_nivel_conformidad.jpg");
	}

	@Test
	public void testEvaluacionPuntuacionMediaAspecto() throws Exception {
		final Map<String, Map<String, Double>> data = new LinkedHashMap<>();
		populateEvolutionObservatoryData(data, "12/10/15", new double[] { 7.00, 8.60, 5.60, 7.70, 6.20 }, ASPECTS);
		populateEvolutionObservatoryData(data, "01/12/15", new double[] { 6.50, 8.60, 5.70, 7.50, 5.90 }, ASPECTS);
		populateEvolutionObservatoryData(data, "26/08/16", new double[] { 6.60, 8.60, 5.70, 8.10, 5.90 }, ASPECTS);

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Map<String, Double>> observatoryEntry : data.entrySet()) {
			for (Map.Entry<String, Double> columnsValues : observatoryEntry.getValue().entrySet()) {
				dataSet.addValue(columnsValues.getValue(), observatoryEntry.getKey(), columnsValues.getKey());
			}
		}

		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, false, false, false, false, 1565, 684, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
		chartForm.setFixedColorBars(true);
		chartForm.setShowColumsLabels(false);

		GraphicsUtils.createStandardBarChart(chartForm, "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/evolucion_puntuacion_media_aspectos.jpg", "", messageResources, true);
	}

	@Test
	public void testModalityVerificationSegment() throws Exception {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		final int[] passPercentages = new int[] { 78, 100, 45, 78, 33, 98, 30, 79, 90, 73 };
		for (int i = 0; i < LEVEL_II_VERIFICATIONS.length; i++) {
			dataSet.addValue(passPercentages[i], "Modalidad pasa", LEVEL_II_VERIFICATIONS[i]);
			dataSet.addValue(100 - passPercentages[i], "Modalidad falla", LEVEL_II_VERIFICATIONS[i]);
		}

		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 580, 458, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.modality.colors"));
		GraphicsUtils.createStackedBarChart(chartForm, "", "/home/alvaro/Desktop/oaw-graficas-" + stringDate + "/modality_verification_level_ii_segment_iv.jpg");
	}
	
	

}
