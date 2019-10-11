
/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
import org.junit.Test;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicsUtils;
import es.inteco.rastreador2.utils.GraphicsUtils.CategoryRenderer;
import es.inteco.rastreador2.utils.GraphicsUtils.GraphicFormatType;
import es.inteco.rastreador2.utils.GraphicsUtils.LabelGenerator;

/**
 * Clase para generar gráficos adicionales para los informes agregados usando la
 * librería de java usada para el resto de gráficos y mantener el aspecto del
 * documento.
 */
public class GraficosConclusionesTest {

	/** The Constant OBSERVATORIO_AGE. */
	private static final String OBSERVATORIO_AGE = "AGE";

	/** The Constant OBSERVATORIO_CCAA. */
	private static final String OBSERVATORIO_CCAA = "CCAA";

	/** The Constant OBSERVATORIO_EELL. */
	private static final String OBSERVATORIO_EELL = "EELL";

	/** The Constant LABEL_SPACE. */
	private static final String LABEL_SPACE = "    ";

	/** The df. */
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/** The string date. */
	private String stringDate;

	/** The font family. */
	private String fontFamily = "Arial";

	/** The Constant RED_COLOR. */
	private static final Color RED_COLOR = new Color(225, 18, 13);

	/** The Constant GREEN_COLOR. */
	private static final Color GREEN_COLOR = new Color(38, 187, 8);

	/** The Constant BLUE_COLOR. */
	private static final Color BLUE_COLOR = new Color(15, 91, 255);

	/**
	 * Gennerar graficos.
	 */
	@Test
	public void gennerarGraficos() {
		try {
			// stringDate = df.format(new Date()) + "/" + OBSERVATORIO_AGE;
			// graficoEvolucionIteracion("age-anterior", 102, 35, 237);
			// graficoEvolucionIteracion("age-primera", 142, 38, 164);

//			 stringDate = df.format(new Date()) + "/" + OBSERVATORIO_CCAA;
//			 graficoEvolucionIteracion("ccaa-anterior", 45, 11, 95);
//			 graficoEvolucionIteracion("ccaa-primera", 46, 11, 94);

			stringDate = df.format(new Date()) + "/" + OBSERVATORIO_EELL;
			graficoEvolucionIteracion("eell-anterior", 47, 16, 133);
			graficoEvolucionIteracion("eell-primera", 57, 24, 115);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Generar evol segmentos.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void generarEvolSegmentos() throws IOException {

//		// CCAA
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 21, 26, 53),
//				new Datos("Noviembre 2018", 32, 26, 42), new Datos("Junio 2019", 25, 30, 45) }, "Principales", "CCAA");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 39, 22, 39),
//				new Datos("Noviembre 2018", 31, 32, 37), new Datos("Junio 2019", 42, 32, 26) }, "Boletines", "CCAA");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 84, 16, 0), new Datos("Noviembre 2018", 79, 16, 5),
//				new Datos("Junio 2019", 57, 32, 11) }, "Parlamentos", "CCAA");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 52, 32, 16),
//				new Datos("Noviembre 2018", 45, 35, 20), new Datos("Junio 2019", 37, 26, 37) }, "Sedes", "CCAA");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 42, 26, 32),
//				new Datos("Noviembre 2018", 42, 16, 42), new Datos("Junio 2019", 26, 26, 47) }, "Educación", "CCAA");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 32, 21, 47),
//				new Datos("Noviembre 2018", 31, 11, 58), new Datos("Junio 2019", 26, 37, 37) }, "Empleo", "CCAA");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 58, 42, 0),
//				new Datos("Noviembre 2018", 63, 26, 11), new Datos("Junio 2019", 57, 32, 11) }, "Salud", "CCAA");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 31, 32, 37),
//				new Datos("Noviembre 2018", 29, 41, 29), new Datos("Junio 2019", 33, 22, 44) }, "Tributos", "CCAA");
//
//		// EELL
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 49, 14, 37),
//				new Datos("Noviembre 2018", 45, 18, 37), new Datos("Junio 2019", 42, 22, 36) }, "Diputaciones", "EELL");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 44, 28, 28),
//				new Datos("Noviembre 2018", 45, 20, 35), new Datos("Junio 2019", 47, 16, 37) }, "Aytos", "EELL");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 84, 8, 8), new Datos("Noviembre 2018", 78, 12, 10),
//				new Datos("Junio 2019", 72, 14, 14) }, "Poblados", "EELL");
//		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 71, 21, 8), new Datos("Noviembre 2018", 71, 22, 6),
//				new Datos("Junio 2019", 83, 10, 6) }, "2000-5000", "EELL");

		// AGE
		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 0,22,78),
				new Datos("Noviembre 2018", 14,9,77), new Datos("Junio 2019", 9,5,86) }, "Principales", "AGE");
		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 32,18,49),
				new Datos("Noviembre 2018", 33,15,52), new Datos("Junio 2019", 26,16,58) }, "Organismos", "AGE");
		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 26,8,66), new Datos("Noviembre 2018", 20,15,64),
				new Datos("Junio 2019", 23,9,68) }, "Temáticos", "AGE");
		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 41,18,41), new Datos("Noviembre 2018", 44,12,44),
				new Datos("Junio 2019", 44,6,50) }, "Otros", "AGE");
		generarEvolSegmentoCCAA(new Datos[] { new Datos("Mayo 2018", 24,11,65), new Datos("Noviembre 2018", 23,11,66),
				new Datos("Junio 2019", 24,9,67) }, "Sedes", "AGE");

	}

	/**
	 * Generar evol segmento CCAA.
	 *
	 * @param datos        the datos
	 * @param segmento     the segmento
	 * @param observatorio the observatorio
	 * @throws IOException Signals that an I/O exception has occurred.
	 */

	public void generarEvolSegmentoCCAA(Datos[] datos, String segmento, String observatorio) throws IOException {

		final DefaultCategoryDataset dataSet = createDataset(datos);

		final String noDataMess = "noData";

		final PropertiesManager pmgr = new PropertiesManager();
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, 580, 458,
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		// chartForm.setTitle(segmento);

		createStackedBarChart(chartForm, noDataMess, "/home/alvaro/Documents/minhafp/2018_2019/observatorios mayo 2019/"
				+ observatorio + "_" + segmento + "_evol.jpg");
	}

	/**
	 * Grafico evolucion iteracion.
	 *
	 * @param iteracion the iteracion
	 * @param mejoran   the mejoran
	 * @param empeoran  the empeoran
	 * @param mantienen the mantienen
	 * @throws Exception the exception
	 */
	private void graficoEvolucionIteracion(String iteracion, int mejoran, int empeoran, int mantienen)
			throws Exception {

		final String sectionLabel = "N\u00BA Portales: ";
		final int total = empeoran + mantienen + mejoran;

		final DefaultPieDataset dataSet = new DefaultPieDataset();
		dataSet.setValue("Empeoran", Integer.valueOf(empeoran));
		dataSet.setValue("Se mantienen", Integer.valueOf(mantienen));
		dataSet.setValue("Mejoran", Integer.valueOf(mejoran));

		final PropertiesManager pmgr = new PropertiesManager();
		GraphicsUtils.createPieChart(dataSet, "", sectionLabel, total,
				"/home/alvaro/Documents/minhafp/2018_2019/observatorios mayo 2019/eell/oaw-graficas-" + stringDate
						+ "/evol_" + iteracion + ".jpg",
				"", pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), 565, 464);
	}

	/**
	 * Creates a sample dataset.
	 *
	 * @param datos the datos
	 * @return A sample dataset.
	 */

	private DefaultCategoryDataset createDataset(Datos[] datos) {
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		for (int i = 0; i < datos.length; i++) {
			result.addValue(datos[i].getNv(), "Parcial", datos[i].getFecha());
			result.addValue(datos[i].getP1(), "Prioridad 1", datos[i].getFecha());
			result.addValue(datos[i].getP2(), "Prioridad 1 y 2", datos[i].getFecha());
		}

		return result;
	}

	/**
	 * Creates the dataset.
	 *
	 * @return the default category dataset
	 */
	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		result.addValue(35, "Parcial", "Fecha 1");
		result.addValue(40, "Prioridad 1", "Fecha 1");
		result.addValue(25, "Prioridad 1 y 2", "Fecha 1");

		result.addValue(10, "Parcial", "Fecha 2");
		result.addValue(20, "Prioridad 1", "Fecha 2");
		result.addValue(70, "Prioridad 1 y 2", "Fecha 2");

		result.addValue(17, "Parcial", "Fecha 3");
		result.addValue(43, "Prioridad 1", "Fecha 3");
		result.addValue(40, "Prioridad 1 y 2", "Fecha 3");

		return result;
	}

	/**
	 * Creates the stacked bar chart.
	 *
	 * @param chartForm  the chart form
	 * @param noDataMess the no data mess
	 * @param filePath   the file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createStackedBarChart(final ChartForm chartForm, final String noDataMess, final String filePath)
			throws IOException {
		final JFreeChart chart = ChartFactory.createStackedBarChart3D(chartForm.getTitle(), chartForm.getColumnTitle(),
				chartForm.getRowTitle(), chartForm.getDataSet(), PlotOrientation.VERTICAL, chartForm.isPrintLegend(),
				true, false);

		formatLegend(chart);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);

		plot.setNoDataMessage(noDataMess);
		configNoDataMessage(plot);

		List<Paint> colors = getColors(chartForm.getColor());

		CategoryRenderer renderer = new CategoryRenderer(colors);
		renderer.setColor(plot, chartForm.getDataSet());

		if (chartForm.isPrintLegend()) {
			plot.setFixedLegendItems(generateLegend(colors, plot));
		}

		BarRenderer barRenderer = (BarRenderer) plot.getRenderer();
		barRenderer.setBaseItemLabelGenerator(new LabelGenerator(true));
		barRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		barRenderer.setBaseItemLabelFont(new Font(fontFamily, Font.BOLD, 12));
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
		domainAxis.setTickLabelFont(new Font(fontFamily, Font.PLAIN, 14));

		NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
		valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		createNumberAxis(plot, true);

		saveChartToFile(filePath, chart, chartForm.getX(), chartForm.getY());

	}

	/**
	 * Save chart to file.
	 *
	 * @param filePath the file path
	 * @param chart    the chart
	 * @param x        the x
	 * @param y        the y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void saveChartToFile(final String filePath, final JFreeChart chart, int x, int y) throws IOException {
		saveChartToFile(filePath, chart, x, y, GraphicFormatType.JPG);
	}

	/**
	 * Save chart to file.
	 *
	 * @param filePath the file path
	 * @param chart    the chart
	 * @param x        the x
	 * @param y        the y
	 * @param mimeType the mime type
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void saveChartToFile(final String filePath, final JFreeChart chart, final int x, final int y,
			final GraphicFormatType mimeType) throws IOException {
		final File file = new File(filePath);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("Error al crear el archivo con la imagen de la gráfica", GraphicsUtils.class,
					Logger.LOG_LEVEL_ERROR);
		}
		file.createNewFile();

		try (FileOutputStream out = new FileOutputStream(file)) {
			if (mimeType.equals(GraphicFormatType.JPG)) {
				ChartUtilities.writeChartAsJPEG(out, chart, x, y);
			} else {
				ChartUtilities.writeChartAsPNG(out, chart, x, y);
			}
		}
	}

	/**
	 * Gets the colors.
	 *
	 * @param colorsProperty the colors property
	 * @return the colors
	 */
	private static List<Paint> getColors(final String colorsProperty) {
		String regexp = "\\{(.*?),(.*?),(.*?)\\}";
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(colorsProperty);
		List<Paint> colors = new ArrayList<>();
		while (matcher.find()) {
			colors.add(new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
					Integer.parseInt(matcher.group(3))));
		}
		return colors;
	}

	/**
	 * Format legend.
	 *
	 * @param chart the chart
	 */
	private void formatLegend(JFreeChart chart) {
		LegendTitle lt = chart.getLegend();

		if (lt != null) {
			lt.setItemFont(new Font(fontFamily, Font.PLAIN, 14));
			lt.setItemPaint(Color.black);
			lt.setPadding(10, 10, 10, 10);
		}
	}

	/**
	 * Config no data message.
	 *
	 * @param plot the plot
	 */
	private void configNoDataMessage(final Plot plot) {
		plot.setNoDataMessageFont(new Font(fontFamily, Font.BOLD, 30));
		plot.setNoDataMessagePaint(Color.RED);
	}

	/**
	 * Generate legend.
	 *
	 * @param colors the colors
	 * @param plot   the plot
	 * @return the legend item collection
	 */
	private LegendItemCollection generateLegend(List<Paint> colors, CategoryPlot plot) {
		LegendItemCollection newLegend = new LegendItemCollection();
		LegendItemCollection legend = plot.getLegendItems();
		Shape shape = new Rectangle(15, 15);
		BasicStroke stroke = new BasicStroke();

		for (int i = 0; i < legend.getItemCount(); i++) {
			LegendItem item = legend.get(i);
			item = new LegendItem(item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE,
					item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, shape, colors.get(i % 3), stroke,
					Color.black);
			newLegend.add(item);
		}
		return newLegend;
	}

	/**
	 * Item label color.
	 *
	 * @param barRenderer the bar renderer
	 * @param colors      the colors
	 */
	private void itemLabelColor(BarRenderer barRenderer, List<Paint> colors) {
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

	/**
	 * Creates the number axis.
	 *
	 * @param plot       the plot
	 * @param percentage the percentage
	 */
	private void createNumberAxis(final CategoryPlot plot, boolean percentage) {
		final NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();

		if (!percentage) {
			valueAxis.setTickUnit(new NumberTickUnit(1));
			valueAxis.setRangeWithMargins(new Range(0, 10));
		} else {
			valueAxis.setTickUnit(new NumberTickUnit(10));
			valueAxis.setRangeWithMargins(new Range(0, 100));
		}
		valueAxis.setTickLabelFont(new Font(fontFamily, Font.PLAIN, 14));
		valueAxis.setLowerBound(0);
	}

	/**
	 * Generate evolution average score by verification chart.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// TODO Mejorar la compresión de estos gráficos
	@Test
	public void generateEvolutionAverageScoreByVerificationChart() throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();

		// EELL
//		dataSet.addValue(6.40, "03/05/18", "1.1");
//		dataSet.addValue(6.60, "19/11/18", "1.1");
//		dataSet.addValue(6.85, "18/06/18", "1.1");
//		
//		dataSet.addValue(4.60, "03/05/18", "1.2");
//		dataSet.addValue(4.80, "19/11/18", "1.2");
//		dataSet.addValue(5.40, "18/06/18", "1.2");
//		
//		dataSet.addValue(7.60, "03/05/18", "1.3");
//		dataSet.addValue(7.80, "19/11/18", "1.3");
//		dataSet.addValue(8.00, "18/06/18", "1.3");
//		
//		dataSet.addValue(6.80, "03/05/18", "1.4");
//		dataSet.addValue(6.80, "19/11/18", "1.4");
//		dataSet.addValue(7.30, "18/06/18", "1.4");
//		
//		dataSet.addValue(6.40, "03/05/18", "1.5");
//		dataSet.addValue(6.40, "19/11/18", "1.5");
//		dataSet.addValue(6.45, "18/06/18", "1.5");
//		
//		dataSet.addValue(8.00, "03/05/18", "1.6");
//		dataSet.addValue(8.00, "19/11/18", "1.6");
//		dataSet.addValue(7.65, "18/06/18", "1.6");
//		
//		dataSet.addValue(8.20, "03/05/18", "1.7");
//		dataSet.addValue(8.40, "19/11/18", "1.7");
//		dataSet.addValue(8.45, "18/06/18", "1.7");

		// CCAA
//		dataSet.addValue(7.90, "10/05/18", "1.1");
//		dataSet.addValue(7.70, "05/11/18", "1.1");
//		dataSet.addValue(7.80, "18/06/18", "1.1");
//
//		dataSet.addValue(6.60, "10/05/18", "1.2");
//		dataSet.addValue(6.50, "05/11/18", "1.2");
//		dataSet.addValue(6.40, "18/06/18", "1.2");
//
//		dataSet.addValue(7.50, "10/05/18", "1.3");
//		dataSet.addValue(8.00, "05/11/18", "1.3");
//		dataSet.addValue(8.00, "18/06/18", "1.3");
//
//		dataSet.addValue(8.10, "10/05/18", "1.4");
//		dataSet.addValue(8.20, "05/11/18", "1.4");
//		dataSet.addValue(8.20, "18/06/18", "1.4");
//
//		dataSet.addValue(7.10, "10/05/18", "1.5");
//		dataSet.addValue(7.00, "05/11/18", "1.5");
//		dataSet.addValue(7.50, "18/06/18", "1.5");
//
//		dataSet.addValue(8.10, "10/05/18", "1.6");
//		dataSet.addValue(7.80, "05/11/18", "1.6");
//		dataSet.addValue(8.00, "18/06/18", "1.6");
//
//		dataSet.addValue(9.20, "10/05/18", "1.7");
//		dataSet.addValue(8.90, "05/11/18", "1.7");
//		dataSet.addValue(9.30, "18/06/18", "1.7");

		// AGE
		dataSet.addValue(7.90, "10/05/18", "1.1");
		dataSet.addValue(7.90, "05/11/18", "1.1");
		dataSet.addValue(8.20, "18/06/18", "1.1");

		dataSet.addValue(7.40, "10/05/18", "1.2");
		dataSet.addValue(7.40, "05/11/18", "1.2");
		dataSet.addValue(7.60, "18/06/18", "1.2");

		dataSet.addValue(8.60, "10/05/18", "1.3");
		dataSet.addValue(8.70, "05/11/18", "1.3");
		dataSet.addValue(8.50, "18/06/18", "1.3");

		dataSet.addValue(7.70, "10/05/18", "1.4");
		dataSet.addValue(7.70, "05/11/18", "1.4");
		dataSet.addValue(7.40, "18/06/18", "1.4");

		dataSet.addValue(8.40, "10/05/18", "1.5");
		dataSet.addValue(8.50, "05/11/18", "1.5");
		dataSet.addValue(8.70, "18/06/18", "1.5");

		dataSet.addValue(8.80, "10/05/18", "1.6");
		dataSet.addValue(8.80, "05/11/18", "1.6");
		dataSet.addValue(8.80, "18/06/18", "1.6");

		dataSet.addValue(9.40, "10/05/18", "1.7");
		dataSet.addValue(9.40, "05/11/18", "1.7");
		dataSet.addValue(9.50, "18/06/18", "1.7");

		final DefaultCategoryDataset dataSet2 = new DefaultCategoryDataset();

//		EELL		
//		dataSet2.addValue(8.40, "03/05/18", "1.8");
//		dataSet2.addValue(8.50, "19/11/18", "1.8");
//		dataSet2.addValue(8.90, "18/06/18", "1.8");
//		
//		dataSet2.addValue(4.60, "03/05/18", "1.9");
//		dataSet2.addValue(4.90, "19/11/18", "1.9");
//		dataSet2.addValue(5.75, "18/06/18", "1.9");
//		
//		dataSet2.addValue(8.70, "03/05/18", "1.10");
//		dataSet2.addValue(8.80, "19/11/18", "1.10");
//		dataSet2.addValue(8.75, "18/06/18", "1.10");
//		
//		dataSet2.addValue(8.80, "03/05/18", "1.11");
//		dataSet2.addValue(9.10, "19/11/18", "1.11");
//		dataSet2.addValue(9.10, "18/06/18", "1.11");
//		
//		dataSet2.addValue(3.70, "03/05/18", "1.12");
//		dataSet2.addValue(3.90, "19/11/18", "1.12");
//		dataSet2.addValue(4.45, "18/06/18", "1.12");
//		
//		dataSet2.addValue(9.90, "03/05/18", "1.13");
//		dataSet2.addValue(9.90, "19/11/18", "1.13");
//		dataSet2.addValue(9.90, "18/06/18", "1.13");
//		
//		dataSet2.addValue(5.20, "03/05/18", "1.14");
//		dataSet2.addValue(5.20, "19/11/18", "1.14");
//		dataSet2.addValue(5.15, "18/06/18", "1.14");

		// CCAA
//		dataSet2.addValue(9.00, "10/05/18", "1.8");
//		dataSet2.addValue(8.90, "05/11/18", "1.8");
//		dataSet2.addValue(9.20, "18/06/18", "1.8");
//
//		dataSet2.addValue(7.00, "10/05/18", "1.9");
//		dataSet2.addValue(6.90, "05/11/18", "1.9");
//		dataSet2.addValue(6.90, "18/06/18", "1.9");
//
//		dataSet2.addValue(8.60, "10/05/18", "1.10");
//		dataSet2.addValue(8.60, "05/11/18", "1.10");
//		dataSet2.addValue(8.80, "18/06/18", "1.10");
//
//		dataSet2.addValue(8.50, "10/05/18", "1.11");
//		dataSet2.addValue(8.80, "05/11/18", "1.11");
//		dataSet2.addValue(8.30, "18/06/18", "1.11");
//
//		dataSet2.addValue(5.40, "10/05/18", "1.12");
//		dataSet2.addValue(5.30, "05/11/18", "1.12");
//		dataSet2.addValue(6.00, "18/06/18", "1.12");
//
//		dataSet2.addValue(10.00, "10/05/18", "1.13");
//		dataSet2.addValue(10.00, "05/11/18", "1.13");
//		dataSet2.addValue(10.00, "18/06/18", "1.13");
//
//		dataSet2.addValue(5.00, "10/05/18", "1.14");
//		dataSet2.addValue(5.50, "05/11/18", "1.14");
//		dataSet2.addValue(5.80, "18/06/18", "1.14");

		// AGE
		dataSet2.addValue(9.30, "10/05/18", "1.8");
		dataSet2.addValue(9.30, "05/11/18", "1.8");
		dataSet2.addValue(9.50, "18/06/18", "1.8");

		dataSet2.addValue(7.50, "10/05/18", "1.9");
		dataSet2.addValue(7.80, "05/11/18", "1.9");
		dataSet2.addValue(8.10, "18/06/18", "1.9");

		dataSet2.addValue(8.20, "10/05/18", "1.10");
		dataSet2.addValue(8.20, "05/11/18", "1.10");
		dataSet2.addValue(8.50, "18/06/18", "1.10");

		dataSet2.addValue(9.10, "10/05/18", "1.11");
		dataSet2.addValue(9.00, "05/11/18", "1.11");
		dataSet2.addValue(9.20, "18/06/18", "1.11");

		dataSet2.addValue(6.60, "10/05/18", "1.12");
		dataSet2.addValue(6.80, "05/11/18", "1.12");
		dataSet2.addValue(7.10, "18/06/18", "1.12");

		dataSet2.addValue(9.90, "10/05/18", "1.13");
		dataSet2.addValue(10.00, "05/11/18", "1.13");
		dataSet2.addValue(10.00, "18/06/18", "1.13");

		dataSet2.addValue(6.80, "10/05/18", "1.14");
		dataSet2.addValue(6.90, "05/11/18", "1.14");
		dataSet2.addValue(7.00, "18/06/18", "1.14");

		final DefaultCategoryDataset dataSet3 = new DefaultCategoryDataset();
//CCAA		
//		dataSet3.addValue(9.30, "10/05/18", "2.1");
//		dataSet3.addValue(9.40, "05/11/18", "2.1");
//		dataSet3.addValue(10.00, "18/06/18", "2.1");
//
//		dataSet3.addValue(8.40, "10/05/18", "2.2");
//		dataSet3.addValue(8.20, "05/11/18", "2.2");
//		dataSet3.addValue(8.50, "18/06/18", "2.2");
//
//		dataSet3.addValue(3.50, "10/05/18", "2.3");
//		dataSet3.addValue(3.90, "05/11/18", "2.3");
//		dataSet3.addValue(5.60, "18/06/18", "2.3");
//
//		dataSet3.addValue(9.00, "10/05/18", "2.4");
//		dataSet3.addValue(9.20, "05/11/18", "2.4");
//		dataSet3.addValue(9.30, "18/06/18", "2.4");
//
//		dataSet3.addValue(8.80, "10/05/18", "2.5");
//		dataSet3.addValue(8.90, "05/11/18", "2.5");
//		dataSet3.addValue(10.00, "18/06/18", "2.5");
//
//		dataSet3.addValue(6.50, "10/05/18", "2.6");
//		dataSet3.addValue(6.60, "05/11/18", "2.6");
//		dataSet3.addValue(9.70, "18/06/18", "2.6");

		// AGE

		dataSet3.addValue(8.70, "10/05/18", "2.1");
		dataSet3.addValue(8.80, "05/11/18", "2.1");
		dataSet3.addValue(8.90, "18/06/18", "2.1");

		dataSet3.addValue(8.90, "10/05/18", "2.2");
		dataSet3.addValue(9.10, "05/11/18", "2.2");
		dataSet3.addValue(9.10, "18/06/18", "2.2");

		dataSet3.addValue(6.20, "10/05/18", "2.3");
		dataSet3.addValue(6.20, "05/11/18", "2.3");
		dataSet3.addValue(7.20, "18/06/18", "2.3");

		dataSet3.addValue(8.20, "10/05/18", "2.4");
		dataSet3.addValue(8.20, "05/11/18", "2.4");
		dataSet3.addValue(9.20, "18/06/18", "2.4");

		dataSet3.addValue(9.20, "10/05/18", "2.5");
		dataSet3.addValue(9.20, "05/11/18", "2.5");
		dataSet3.addValue(9.20, "18/06/18", "2.5");

		dataSet3.addValue(7.20, "10/05/18", "2.6");
		dataSet3.addValue(6.70, "05/11/18", "2.6");
		dataSet3.addValue(9.40, "18/06/18", "2.6");

		final MessageResources messageResources = MessageResources
				.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B);

		String value = pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color");
		final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, false, 1465, 654,
				"{126,154,64}");
		chartForm.setFixedColorBars(true);

		GraphicsUtils.createStandardBarChart(chartForm,
				"/home/alvaro/Documents/minhafp/2018_2019/observatorios mayo 2019/age/8-3-1.jpg", "", messageResources,
				true);

		final ChartForm chartForm2 = new ChartForm(dataSet2, true, true, false, false, false, false, false, 1465, 654,
				"{126,154,64}");
		chartForm2.setFixedColorBars(true);

		GraphicsUtils.createStandardBarChart(chartForm2,
				"/home/alvaro/Documents/minhafp/2018_2019/observatorios mayo 2019/age/8-3-2.jpg", "", messageResources,
				true);

		final ChartForm chartForm3 = new ChartForm(dataSet3, true, true, false, false, false, false, false, 1465, 654,
				"{126,154,64}");
		chartForm3.setFixedColorBars(true);

		GraphicsUtils.createStandardBarChart(chartForm3,
				"/home/alvaro/Documents/minhafp/2018_2019/observatorios mayo 2019/age/8-3-3.jpg", "", messageResources,
				true);

	}

	/**
	 * The Class Datos.
	 */
	public class Datos {

		/** The fecha. */
		private String fecha;

		/** The nv. */
		private int nv;

		/** The p 1. */
		private int p1;

		/** The p 2. */
		private int p2;

		/**
		 * Instantiates a new datos.
		 *
		 * @param fecha the fecha
		 * @param nv    the nv
		 * @param p1    the p 1
		 * @param p2    the p 2
		 */
		public Datos(String fecha, int nv, int p1, int p2) {
			super();
			this.fecha = fecha;
			this.nv = nv;
			this.p1 = p1;
			this.p2 = p2;
		}

		/**
		 * Gets the fecha.
		 *
		 * @return the fecha
		 */
		public String getFecha() {
			return fecha;
		}

		/**
		 * Sets the fecha.
		 *
		 * @param fecha the new fecha
		 */
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}

		/**
		 * Gets the nv.
		 *
		 * @return the nv
		 */
		public int getNv() {
			return nv;
		}

		/**
		 * Sets the nv.
		 *
		 * @param nv the new nv
		 */
		public void setNv(int nv) {
			this.nv = nv;
		}

		/**
		 * Gets the p1.
		 *
		 * @return the p1
		 */
		public int getP1() {
			return p1;
		}

		/**
		 * Sets the p1.
		 *
		 * @param p1 the new p1
		 */
		public void setP1(int p1) {
			this.p1 = p1;
		}

		/**
		 * Gets the p2.
		 *
		 * @return the p2
		 */
		public int getP2() {
			return p2;
		}

		/**
		 * Sets the p2.
		 *
		 * @param p2 the new p2
		 */
		public void setP2(int p2) {
			this.p2 = p2;
		}

	}
}
