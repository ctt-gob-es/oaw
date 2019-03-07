/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
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

import org.apache.commons.lang.StringUtils;
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
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatorySiteEvaluationForm;

public final class GraphicsUtils {

	private static final String LABEL_SPACE = "    ";
	private static final Color RED_COLOR = new Color(225, 18, 13);
	private static final Color GREEN_COLOR = new Color(38, 187, 8);
	private static final Color BLUE_COLOR = new Color(15, 91, 255);
	private static final Font TITLE_FONT;
	private static final Font TICK_LABEL_FONT;
	private static final Font TICK_LABEL_FONT_SMALL;
	private static final Font ITEM_LABEL_FONT;
	private static final Font ITEM_LABEL_FONT_SMALL;
	private static final Font LEGEND_FONT;
	private static final Font NO_DATA_FONT;

	static {
		final PropertiesManager pmgr = new PropertiesManager();
		String fontFamily = "Arial";
		try {
			final Font robotoFont = Font.createFont(Font.TRUETYPE_FONT, new File(pmgr.getValue("pdf.properties", "path.pdf.font.monospaced")));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(robotoFont);
			fontFamily = "Roboto";
		} catch (Exception e) {
			Logger.putLog("No se ha podido cargar la fuente Roboto ubicada en " + pmgr.getValue("pdf.properties", "path.pdf.font.monospaced"), GraphicsUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		TITLE_FONT = new Font(fontFamily, Font.BOLD, 22);
		LEGEND_FONT = new Font(fontFamily, Font.PLAIN, 14);
		TICK_LABEL_FONT = new Font(fontFamily, Font.PLAIN, 14);
		TICK_LABEL_FONT_SMALL = new Font(fontFamily, Font.PLAIN, 12);
		ITEM_LABEL_FONT = new Font(fontFamily, Font.BOLD, 12);
		ITEM_LABEL_FONT_SMALL = new Font(fontFamily, Font.PLAIN, 10);
		NO_DATA_FONT = new Font(fontFamily, Font.BOLD, 30);
	}

	public enum GraphicFormatType {
		PNG, JPG
	}

	private GraphicsUtils() {
	}

	public static void createPieChart(DefaultPieDataset dataSet, String title, String sectionLabel, long total, String filePath, String noDataMessage, String colorsKey, int x, int y)
			throws IOException {
		JFreeChart chart = ChartFactory.createPieChart3D(title, dataSet, true, true, false);
		chart.getTitle().setFont(TITLE_FONT);
		formatLegend(chart);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);

		configNoDataMessage(plot);
		plot.setNoDataMessage(noDataMessage);

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

		plot.setLabelGenerator(new CustomLabelGenerator(sectionLabel, total));
		plot.setLegendLabelGenerator(new CustomLegendLabelGenerator());
		plot.setLabelFont(LEGEND_FONT);

		for (int i = 0; i < plot.getLegendItems().getItemCount(); i++) {
			LegendItem item = plot.getLegendItems().get(i);
			item.setLabelFont(LEGEND_FONT);
		}

		saveChartToFile(filePath, chart, x, y);
	}

	private static void configNoDataMessage(final Plot plot) {
		plot.setNoDataMessageFont(NO_DATA_FONT);
		plot.setNoDataMessagePaint(Color.RED);
	}

	// labelPosition true 45 grados false normal
	public static void createBarChart(Map<String, BigDecimal> result, String title, String rowTitle, String columnTitle, String color, boolean withLegend, boolean percentage, boolean labelRotated,
			String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws IOException {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, BigDecimal> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue(), "", parseLevelLabel(entry.getKey(), messageResources));
		}
		createBarChart(dataSet, title, rowTitle, columnTitle, color, withLegend, percentage, labelRotated, filePath, noDataMessage, messageResources, x, y);
	}

	// labelRotated true 45 grados false normal
	public static void createBarChart(DefaultCategoryDataset dataSet, String title, String rowTitle, String columnTitle, String color, boolean withLegend, boolean percentage, boolean labelRotated,
			String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws IOException {
		final ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, percentage, withLegend, labelRotated, false, x, y, color);
		createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
	}

	public static void createSeriesBarChart(ChartForm observatoryGraphicsForm, String filePath, String noDataMessage, final MessageResources messageResources, boolean withRange) throws IOException {
		createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, withRange);
	}

	public static void createBar1PxChart(final List<ObservatorySiteEvaluationForm> result, String title, String rowTitle, String columnTitle, final String filePath, final String noDataMessage,
			final MessageResources messageResources, int x, int y, boolean showColumnsLabels) throws IOException {
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

	public static void createBarPageByLevelChart(List<ObservatoryEvaluationForm> result, String title, String rowTitle, String columnTitle, String filePath, String noDataMessage,
			final MessageResources messageResources, int x, int y) throws IOException {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		final StringBuilder colors = new StringBuilder();
		int i = 1;
		for (ObservatoryEvaluationForm observatoryEvaluationForm : result) {
			getRequiredColors(colors, ObservatoryUtils.pageSuitabilityLevel(observatoryEvaluationForm));

			dataSet.addValue(observatoryEvaluationForm.getScore().setScale(observatoryEvaluationForm.getScore().scale() - 1, BigDecimal.ROUND_UNNECESSARY), "",
					messageResources.getMessage("observatory.graphic.score.by.page.label", i++));
		}

		ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, false, true, true, true, x, y, colors.toString());
		observatoryGraphicsForm.setFixedLegend(true);
		observatoryGraphicsForm.setFixedColorBars(true);
		createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
	}

	/**
	 * Builds a comma (,) separated string with the required colors
	 *
	 * @param colors
	 *            a StringBuilder object where append the colors
	 * @param level
	 *            a String representing the suitability (conformance level)
	 */
	public static void getRequiredColors(final StringBuilder colors, final String level) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (Constants.OBS_NV.equalsIgnoreCase(level) || Constants.OBS_PARCIAL.equalsIgnoreCase(level)) {
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
		final JFreeChart chart = ChartFactory.createStackedBarChart3D(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(), chartForm.getDataSet(), PlotOrientation.VERTICAL,
				chartForm.isPrintLegend(), true, false);

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

	public static void createStandardBarChart(ChartForm observatoryGraphicsForm, String filePath, String noDataMess, MessageResources messageResources, boolean withRange) throws IOException {

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
		configNoDataMessage(plot);

		// Elimina la transparencia de las gráficas
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

		if (chartForm.getDataSet().getColumnCount() > 17) {
			domainAxis.setTickLabelFont(TICK_LABEL_FONT_SMALL);
		} else {
			domainAxis.setTickLabelFont(TICK_LABEL_FONT);
		}

		domainAxis.setUpperMargin(0.01);
		domainAxis.setLowerMargin(0.01);
	}

	private static void defineBarColor(final CategoryPlot plot, final ChartForm chartForm, final MessageResources messageResources) {
		plot.setBackgroundPaint(Color.white);
		if (chartForm.isGridline()) {
			plot.setRangeGridlinePaint(Color.black);
		}

		final List<Paint> colors;
		if (chartForm.getColor() != null && !chartForm.getColor().equals("")) {
			colors = getColors(chartForm.getColor());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			colors = getColors(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		}

		final CategoryItemRenderer renderer = plot.getRenderer();
		if (chartForm.isPrintLegend()) {
			if (chartForm.isFixedLegend() || chartForm.isOnePixelGraph()) {
				plot.setFixedLegendItems(generateOnePixelLegend(messageResources));
			} else {
				plot.setFixedLegendItems(generateLegend(colors, plot));
			}
		}

		if (!chartForm.isFixedColorBars()) {
			for (int i = 0; i < colors.size(); i++) {
				renderer.setSeriesPaint(i, colors.get(i));
			}
		} else {
			final Paint[] paintArray = colors.toArray(new Paint[colors.size()]);
			final CategoryItemRenderer rendererC;
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
			item = new LegendItem(item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, shape, colors.get(i), stroke,
					Color.black);
			newLegend.add(item);
		}
		return newLegend;
	}

	private static LegendItemCollection generateOnePixelLegend(final MessageResources messageResources) {
		PropertiesManager pmgr = new PropertiesManager();
		String[] labels = { parseLevelLabel(Constants.OBS_NV, messageResources), parseLevelLabel(Constants.OBS_A, messageResources), parseLevelLabel(Constants.OBS_AA, messageResources) };
		List<Paint> colors = getColors(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));

		// Se crea una leyenda fija
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
		} else if (chartForm.isShowColumsLabels()) {
			barRenderer.setMaximumBarWidth(0.1);
			barRenderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
			barRenderer.setBaseItemLabelGenerator(new LabelGenerator(chartForm.isPercentage()));
			barRenderer.setBaseItemLabelsVisible(true);
			barRenderer.setDrawBarOutline(true);
			barRenderer.setBaseOutlinePaint(Color.GRAY);

			if (chartForm.isTridimensional()) {
				final CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();

				// TODO Si tiene más de 17 columnas
				if (chartForm.getDataSet().getColumnCount() > 17) {
					renderer.setBaseItemLabelFont(ITEM_LABEL_FONT_SMALL);
					renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER, TextAnchor.BASELINE_CENTER, 0));
				} else {

					renderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
					renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
				}

				((BarRenderer3D) renderer).setItemLabelAnchorOffset(10d);

			}
		}

		// barRenderer.setBaseNegativeItemLabelPosition(new
		// ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
		// TextAnchor.BASELINE_LEFT));
		barRenderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER, TextAnchor.BASELINE_CENTER, 0));
	}

	private static void saveChartToFile(final String filePath, final JFreeChart chart, int x, int y) throws IOException {
		saveChartToFile(filePath, chart, x, y, GraphicFormatType.JPG);
	}

	private static void saveChartToFile(final String filePath, final JFreeChart chart, final int x, final int y, final GraphicFormatType mimeType) throws IOException {
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

		private final String sectionLabel;
		private final long total;

		public CustomLabelGenerator(final String sectionLabel, final long total) {
			this.sectionLabel = sectionLabel;
			this.total = total;
		}

		public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
			final float p = (float) Math.pow(10, 2);
			if (!dataset.getValue(key).toString().equals("0")) {
				Float value = Float.valueOf(dataset.getValue(key).toString()) / (float) total * 100;
				value = value * p;
				float tmp = Math.round(value);
				value = tmp / p;
				return key + "\n" + sectionLabel + dataset.getValue(key).toString() + "\n" + " (" + value.toString() + "%)";
			} else {
				return null;
			}

		}

		@Override
		public AttributedString generateAttributedSectionLabel(PieDataset arg0, Comparable arg1) {
			return null;
		}
	}

	public static class CustomLegendLabelGenerator implements PieSectionLabelGenerator {

		@Override
		public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
			return key + LABEL_SPACE;
		}

		@Override
		public AttributedString generateAttributedSectionLabel(PieDataset arg0, Comparable arg1) {
			return null;
		}
	}

	public static class LabelGenerator implements CategoryItemLabelGenerator {

		private boolean isPercentage;

		public LabelGenerator(boolean isPercentage) {
			this.isPercentage = isPercentage;
		}

		public String generateLabel(CategoryDataset dataset, int series, int category) {
			if (isPercentage) {
				final String stringValue = dataset.getValue(series, category).toString();
				return truncateDecimalPart(stringValue) + "%";
			} else {
				final Number value = dataset.getValue(series, category);
				if (value != null && value.intValue() != -1) {
					final String stringValue = value.toString();
					return truncateDecimalPart(stringValue);
				} else { // if (value == null || value.intValue() == -1) {
					return "NP";
				}
			}
		}

		private String truncateDecimalPart(final String stringValue) {
			if (stringValue.contains(".0") && stringValue.indexOf('.') + 2 == stringValue.length()) {
				return stringValue.replace(".0", "");
			} else if (stringValue.contains(".00") && stringValue.indexOf('.') + 3 == stringValue.length()) {
				return stringValue.replace(".00", "");
			} else {
				return stringValue;
			}
		}

		@Override
		public String generateColumnLabel(CategoryDataset arg0, int arg1) {
			return null;
		}

		@Override
		public String generateRowLabel(CategoryDataset arg0, int arg1) {
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

		@Override
		public Paint getItemPaint(final int row, final int column) {
			return this.colors[column % this.colors.length];
		}
	}

	public static class CustomRenderer extends BarRenderer {

		private Paint[] colors;

		public CustomRenderer(final Paint[] colors) {
			this.colors = Arrays.copyOf(colors, colors.length);
		}

		@Override
		public Paint getItemPaint(final int row, final int column) {
			return this.colors[column % this.colors.length];
		}
	}

}