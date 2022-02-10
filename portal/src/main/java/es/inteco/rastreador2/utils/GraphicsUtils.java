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
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.TextAnchor;
import org.jfree.util.Rotation;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatorySiteEvaluationForm;

/**
 * The Class GraphicsUtils.
 */
public final class GraphicsUtils {
	/** The Constant LABEL_SPACE. */
	private static final String LABEL_SPACE = "    ";
	/** The Constant RED_COLOR. */
	private static final Color RED_COLOR = new Color(225, 18, 13);
	/** The Constant GREEN_COLOR. */
	private static final Color GREEN_COLOR = new Color(38, 187, 8);
	/** The Constant BLUE_COLOR. */
	private static final Color BLUE_COLOR = new Color(15, 91, 255);
	/** The Constant TITLE_FONT. */
	private static final Font TITLE_FONT;
	/** The Constant TICK_LABEL_FONT. */
	private static final Font TICK_LABEL_FONT;
	/** The Constant TICK_LABEL_FONT_SMALL. */
	private static final Font TICK_LABEL_FONT_SMALL;
	/** The Constant ITEM_LABEL_FONT. */
	private static final Font ITEM_LABEL_FONT;
	/** The Constant ITEM_LABEL_FONT_SMALL. */
	private static final Font ITEM_LABEL_FONT_SMALL;
	/** The Constant LEGEND_FONT. */
	private static final Font LEGEND_FONT;
	/** The Constant NO_DATA_FONT. */
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
		TITLE_FONT = new Font(fontFamily, Font.BOLD, 18);
		LEGEND_FONT = new Font(fontFamily, Font.PLAIN, 14);
		TICK_LABEL_FONT = new Font(fontFamily, Font.PLAIN, 14);
		TICK_LABEL_FONT_SMALL = new Font(fontFamily, Font.PLAIN, 12);
		ITEM_LABEL_FONT = new Font(fontFamily, Font.BOLD, 12);
		ITEM_LABEL_FONT_SMALL = new Font(fontFamily, Font.PLAIN, 10);
		NO_DATA_FONT = new Font(fontFamily, Font.BOLD, 30);
	}

	/**
	 * The Enum GraphicFormatType.
	 */
	public enum GraphicFormatType {
		/** The png. */
		PNG,
		/** The jpg. */
		JPG
	}

	/**
	 * Instantiates a new graphics utils.
	 */
	private GraphicsUtils() {
	}

	/**
	 * Creates the pie chart.
	 *
	 * @param dataSet       the data set
	 * @param title         the title
	 * @param sectionLabel  the section label
	 * @param total         the total
	 * @param filePath      the file path
	 * @param noDataMessage the no data message
	 * @param colorsKey     the colors key
	 * @param x             the x
	 * @param y             the y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Config no data message.
	 *
	 * @param plot the plot
	 */
	private static void configNoDataMessage(final Plot plot) {
		plot.setNoDataMessageFont(NO_DATA_FONT);
		plot.setNoDataMessagePaint(Color.RED);
	}

	/**
	 * Creates the bar chart.
	 *
	 * @param result           the result
	 * @param title            the title
	 * @param rowTitle         the row title
	 * @param columnTitle      the column title
	 * @param color            the color
	 * @param withLegend       the with legend
	 * @param percentage       the percentage
	 * @param labelRotated     the label rotated
	 * @param filePath         the file path
	 * @param noDataMessage    the no data message
	 * @param messageResources the message resources
	 * @param x                the x
	 * @param y                the y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// labelPosition true 45 grados false normal
	public static void createBarChart(Map<String, BigDecimal> result, String title, String rowTitle, String columnTitle, String color, boolean withLegend, boolean percentage, boolean labelRotated,
			String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws IOException {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, BigDecimal> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue(), "", parseLevelLabel(entry.getKey(), messageResources));
		}
		createBarChart(dataSet, title, rowTitle, columnTitle, color, withLegend, percentage, labelRotated, filePath, noDataMessage, messageResources, x, y);
	}

	/**
	 * Creates the bar chart.
	 *
	 * @param dataSet          the data set
	 * @param title            the title
	 * @param rowTitle         the row title
	 * @param columnTitle      the column title
	 * @param color            the color
	 * @param withLegend       the with legend
	 * @param percentage       the percentage
	 * @param labelRotated     the label rotated
	 * @param filePath         the file path
	 * @param noDataMessage    the no data message
	 * @param messageResources the message resources
	 * @param x                the x
	 * @param y                the y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	// labelRotated true 45 grados false normal
	public static void createBarChart(DefaultCategoryDataset dataSet, String title, String rowTitle, String columnTitle, String color, boolean withLegend, boolean percentage, boolean labelRotated,
			String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws IOException {
		final ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, percentage, withLegend, labelRotated, false, x, y, color);
		createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
	}

	/**
	 * Creates the series bar chart.
	 *
	 * @param observatoryGraphicsForm the observatory graphics form
	 * @param filePath                the file path
	 * @param noDataMessage           the no data message
	 * @param messageResources        the message resources
	 * @param withRange               the with range
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createSeriesBarChart(ChartForm observatoryGraphicsForm, String filePath, String noDataMessage, final MessageResources messageResources, boolean withRange) throws IOException {
		createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, withRange);
	}

	/**
	 * Creates the bar chart grouped.
	 *
	 * @param result           the result
	 * @param title            the title
	 * @param rowTitle         the row title
	 * @param columnTitle      the column title
	 * @param color            the color
	 * @param withLegend       the with legend
	 * @param percentage       the percentage
	 * @param labelRotated     the label rotated
	 * @param filePath         the file path
	 * @param noDataMessage    the no data message
	 * @param messageResources the message resources
	 * @param x                the x
	 * @param y                the y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createBarChartGrouped(Map<String, Map<String, BigDecimal>> result, String title, String rowTitle, String columnTitle, String color, boolean withLegend, boolean percentage,
			boolean labelRotated, final String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws IOException {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		// int v = 0;
		for (Map.Entry<String, Map<String, BigDecimal>> entry : result.entrySet()) {
			// Necesitamos reordenar los resputados para que el valor 1.10
			// vaya después de 1.9 y no de 1.1
			Map<String, BigDecimal> results = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String version1, String version2) {
					String[] v1 = version1.split("\\.");
					String[] v2 = version2.split("\\.");
					int major1 = major(v1);
					int major2 = major(v2);
					if (major1 == major2) {
						if (minor(v1) == minor(v2)) { // Devolvemos 1
														// aunque sean iguales
														// porque las claves lleva
														// asociado un subfijo que
														// aqui no tenemos en cuenta
							return 1;
						}
						return minor(v1).compareTo(minor(v2));
					}
					return major1 > major2 ? 1 : -1;
				}

				private int major(String[] version) {
					return Integer.parseInt(version[0].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")
							.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""));
				}

				private Integer minor(String[] version) {
					return version.length > 1 ? Integer.parseInt(version[1].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")
							.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "")) : 0;
				}
			});
			for (Map.Entry<String, BigDecimal> entryU : entry.getValue().entrySet()) {
				results.put(entryU.getKey(), entryU.getValue());
			}
			for (Map.Entry<String, BigDecimal> entryC : results.entrySet()) {
				final BigDecimal valueC = entryC.getValue();
				final String date = entry.getKey();
				final String verficationC = entryC.getKey();
				String verificacionPoint = "";
				String verificationText = "";
				if (verficationC != null) {
					if (verficationC.endsWith(Constants.OBS_VALUE_COMPILANCE_SUFFIX)) {
						verificacionPoint = verficationC.replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "");
						verificationText = Constants.OBS_COMPILANCE;
						dataSet.addValue(valueC, date + " " + verificationText, verificacionPoint);
					} else if (verficationC.endsWith(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX)) {
						verificacionPoint = verficationC.replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "");
						verificationText = Constants.OBS_COMPILANCE_NONE;
						try {
							if (dataSet.getValue(date + " " + verificationText, verificacionPoint) != null) {
								dataSet.addValue(((BigDecimal) dataSet.getValue(date + " " + verificationText, verificacionPoint)).add(valueC), date + " " + verificationText, verificacionPoint);
							} else {
								dataSet.addValue(valueC, date + " " + verificationText, verificacionPoint);
							}
						} catch (Exception e) {
							dataSet.addValue(valueC, date + " " + verificationText, verificacionPoint);
						}
					} else if (verficationC.endsWith(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX)) {
						verificacionPoint = verficationC.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "");
						verificationText = Constants.OBS_COMPILANCE_NA;
						try {
							if (dataSet.getValue(date + " " + verificationText, verificacionPoint) != null) {
								dataSet.addValue(((BigDecimal) dataSet.getValue(date + " " + verificationText, verificacionPoint)).add(valueC), date + " " + verificationText, verificacionPoint);
							} else {
								dataSet.addValue(valueC, date + " " + verificationText, verificacionPoint);
							}
						} catch (Exception e) {
							dataSet.addValue(valueC, date + " " + verificationText, verificacionPoint);
						}
					}
				}
			}
		}
		final ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, percentage, withLegend, labelRotated, false, x, y, color);
		createStandardGroupedBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
	}

	/**
	 * Creates the bar chart grouped modality.
	 *
	 * @param result           the result
	 * @param title            the title
	 * @param rowTitle         the row title
	 * @param columnTitle      the column title
	 * @param color            the color
	 * @param withLegend       the with legend
	 * @param percentage       the percentage
	 * @param labelRotated     the label rotated
	 * @param filePath         the file path
	 * @param noDataMessage    the no data message
	 * @param messageResources the message resources
	 * @param x                the x
	 * @param y                the y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createBarChartGroupedModality(Map<String, Map<String, BigDecimal>> result, String title, String rowTitle, String columnTitle, String color, boolean withLegend,
			boolean percentage, boolean labelRotated, final String filePath, String noDataMessage, final MessageResources messageResources, int x, int y) throws IOException {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		// int v = 0;
		for (Map.Entry<String, Map<String, BigDecimal>> entry : result.entrySet()) {
			// Necesitamos reordenar los resputados para que el valor 1.10
			// vaya después de 1.9 y no de 1.1
			Map<String, BigDecimal> results = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String version1, String version2) {
					String[] v1 = version1.split("\\.");
					String[] v2 = version2.split("\\.");
					int major1 = major(v1);
					int major2 = major(v2);
					if (major1 == major2) {
						if (minor(v1) == minor(v2)) { // Devolvemos 1
														// aunque sean iguales
														// porque las claves lleva
														// asociado un subfijo que
														// aqui no tenemos en cuenta
							return 1;
						}
						return minor(v1).compareTo(minor(v2));
					}
					return major1 > major2 ? 1 : -1;
				}

				private int major(String[] version) {
					return Integer.parseInt(version[0].replace(Constants.OBS_VALUE_GREEN_SUFFIX, "").replace(Constants.OBS_VALUE_RED_SUFFIX, ""));
				}

				private Integer minor(String[] version) {
					return version.length > 1 ? Integer.parseInt(version[1].replace(Constants.OBS_VALUE_GREEN_SUFFIX, "").replace(Constants.OBS_VALUE_RED_SUFFIX, "")) : 0;
				}
			});
			for (Map.Entry<String, BigDecimal> entryU : entry.getValue().entrySet()) {
				results.put(entryU.getKey(), entryU.getValue());
			}
			for (Map.Entry<String, BigDecimal> entryC : results.entrySet()) {
				final BigDecimal valueC = entryC.getValue();
				final String date = entry.getKey();
				final String verficationC = entryC.getKey();
				String verificacionPoint = "";
				String verificationText = "";
				if (verficationC != null) {
					if (verficationC.endsWith(Constants.OBS_VALUE_GREEN_SUFFIX)) {
						verificacionPoint = verficationC.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "");
						verificationText = "Pasa";
						dataSet.addValue(valueC, date, verificacionPoint);
					}
//						else {
//						verificacionPoint = verficationC.replace(Constants.OBS_VALUE_RED_SUFFIX, "");
//						verificationText = "Falla";
//						dataSet.addValue(valueC, date, verificacionPoint);
//					}
				}
			}
		}
		final ChartForm chartForm1 = new ChartForm(dataSet, true, true, false, percentage, false, false, false, x, y, color);
		chartForm1.setFixedColorBars(true);
		chartForm1.setShowColumsLabels(false);
		chartForm1.setTitle(title);
		GraphicsUtils.createStandardBarChart(chartForm1, filePath, "", messageResources, true);
//		final ChartForm observatoryGraphicsForm = new ChartForm(title, columnTitle, rowTitle, dataSet, true, false, false, percentage, withLegend, labelRotated, false, x, y, color);
//		createStandardBarChart(observatoryGraphicsForm, filePath, noDataMessage, messageResources, true);
	}

	/**
	 * Creates the bar 1 px chart.
	 *
	 * @param result            the result
	 * @param title             the title
	 * @param rowTitle          the row title
	 * @param columnTitle       the column title
	 * @param filePath          the file path
	 * @param noDataMessage     the no data message
	 * @param messageResources  the message resources
	 * @param x                 the x
	 * @param y                 the y
	 * @param showColumnsLabels the show columns labels
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Creates the bar page by level chart.
	 *
	 * @param result           the result
	 * @param title            the title
	 * @param rowTitle         the row title
	 * @param columnTitle      the column title
	 * @param filePath         the file path
	 * @param noDataMessage    the no data message
	 * @param messageResources the message resources
	 * @param x                the x
	 * @param y                the y
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
	 * Builds a comma (,) separated string with the required colors.
	 *
	 * @param colors a StringBuilder object where append the colors
	 * @param level  a String representing the suitability (conformance level)
	 * @return the required colors
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

	/**
	 * Creates the stacked bar chart.
	 *
	 * @param chartForm  the chart form
	 * @param noDataMess the no data mess
	 * @param filePath   the file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createStackedBarChart(final ChartForm chartForm, final String noDataMess, final String filePath) throws IOException {
		DefaultCategoryDataset dataset = chartForm.getDataSet();
		final JFreeChart chart = ChartFactory.createStackedBarChart3D(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(), dataset, PlotOrientation.VERTICAL,
				chartForm.isPrintLegend(), true, false);
		chart.getTitle().setFont(TITLE_FONT);
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

	/**
	 * Creates the grouped stacker bar chart.
	 *
	 * @param chartForm  the chart form
	 * @param noDataMess the no data mess
	 * @param filePath   the file path
	 * @param map        the map
	 * @param domainAxis the domain axis
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createGroupedStackerBarChart(final ChartForm chartForm, final String noDataMess, final String filePath, final KeyToGroupMap map, final SubCategoryAxis domainAxis)
			throws IOException {
		final JFreeChart chart = ChartFactory.createStackedBarChart3D(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(), chartForm.getDataSet(), PlotOrientation.VERTICAL,
				chartForm.isPrintLegend(), true, false);
		chart.getTitle().setFont(TITLE_FONT);
		formatLegend(chart);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setNoDataMessage(noDataMess);
		configNoDataMessage(plot);
		List<Paint> colors = getColors(chartForm.getColor());
		CategoryRenderer renderer = new CategoryRenderer(colors);
		renderer.setColor(plot, chartForm.getDataSet());
		GroupedStackedBarRenderer barRenderer = new GroupedStackedBarRenderer();
		barRenderer.setSeriesToGroupMap(map);
		barRenderer.setBaseItemLabelGenerator(new LabelGenerator(true));
		barRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		barRenderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
		barRenderer.setBaseItemLabelsVisible(true);
		barRenderer.setDrawBarOutline(true);
		barRenderer.setMaximumBarWidth(0.1);
		barRenderer.setBaseOutlinePaint(Color.BLACK);
		itemLabelColor(barRenderer, colors);
		domainAxis.setCategoryMargin(0.05);
		domainAxis.setUpperMargin(0.01);
		domainAxis.setLowerMargin(0.01);
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		domainAxis.setTickLabelFont(TICK_LABEL_FONT);
		plot.setDomainAxis(domainAxis);
		plot.setRenderer(barRenderer);
		saveChartToFile(filePath, chart, chartForm.getX(), chartForm.getY());
	}

	/**
	 * Creates the un grouped stacker bar chart.
	 *
	 * @param chartForm  the chart form
	 * @param noDataMess the no data mess
	 * @param filePath   the file path
	 * @param map        the map
	 * @param domainAxis the domain axis
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createUnGroupedStackerBarChart(final ChartForm chartForm, final String noDataMess, final String filePath, final KeyToGroupMap map, final SubCategoryAxis domainAxis)
			throws IOException {
		final JFreeChart chart = ChartFactory.createStackedBarChart3D(chartForm.getTitle(), chartForm.getColumnTitle(), chartForm.getRowTitle(), chartForm.getDataSet(), PlotOrientation.VERTICAL,
				chartForm.isPrintLegend(), true, false);
		chart.getTitle().setFont(TITLE_FONT);
		formatLegend(chart);
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setNoDataMessage(noDataMess);
		configNoDataMessage(plot);
		List<Paint> colors = getColors(chartForm.getColor());
		CategoryRenderer renderer = new CategoryRenderer(colors);
		renderer.setColor(plot, chartForm.getDataSet());
		GroupedStackedBarRenderer barRenderer = new GroupedStackedBarRenderer();
		barRenderer.setSeriesToGroupMap(map);
		barRenderer.setBaseItemLabelGenerator(new LabelGenerator(true));
		barRenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		barRenderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
		barRenderer.setBaseItemLabelsVisible(true);
		barRenderer.setDrawBarOutline(true);
		barRenderer.setMaximumBarWidth(0.1);
		barRenderer.setBaseOutlinePaint(Color.BLACK);
		/******************* stacked **/
		itemLabelColor(barRenderer, colors);
		domainAxis.setCategoryMargin(0.05);
		if (!chartForm.isRoundLabelPosition()) {
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		} else {
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		}
		/*
		 * 
		 * // domainAxis.setMaximumCategoryLabelLines(3);
		 * 
		 * domainAxis.setUpperMargin(0.01); domainAxis.setLowerMargin(0.01);
		 * 
		 * if (!chartForm.isRoundLabelPosition()) { domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD); } else { domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		 * } domainAxis.setTickLabelFont(TICK_LABEL_FONT);
		 * 
		 * // NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis(); // valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		 * 
		 * // createNumberAxis(plot, true);
		 * 
		 * 
		 * plot.setDomainAxis(domainAxis); plot.setRenderer(barRenderer);
		 * 
		 * saveChartToFile(filePath, chart, chartForm.getX(), chartForm.getY());
		 */
		domainAxis.setMaximumCategoryLabelLines(3);
		domainAxis.setUpperMargin(0.01);
		domainAxis.setLowerMargin(0.01);
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		domainAxis.setTickLabelFont(TICK_LABEL_FONT_SMALL);
		NumberAxis valueAxis = (NumberAxis) plot.getRangeAxis();
		valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		createNumberAxis(plot, true);
		saveChartToFile(filePath, chart, chartForm.getX(), chartForm.getY());
	}

	/**
	 * Item label color.
	 *
	 * @param barRenderer the bar renderer
	 * @param colors      the colors
	 */
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

	/**
	 * Creates the standard bar chart.
	 *
	 * @param observatoryGraphicsForm the observatory graphics form
	 * @param filePath                the file path
	 * @param noDataMess              the no data mess
	 * @param messageResources        the message resources
	 * @param withRange               the with range
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
		putValuesOnBars(chart, observatoryGraphicsForm);
		saveChartToFile(filePath, chart, observatoryGraphicsForm.getX(), observatoryGraphicsForm.getY());
	}

	/**
	 * Creates the standard grouped bar chart.
	 *
	 * @param observatoryGraphicsForm the observatory graphics form
	 * @param filePath                the file path
	 * @param noDataMess              the no data mess
	 * @param messageResources        the message resources
	 * @param withRange               the with range
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createStandardGroupedBarChart(ChartForm observatoryGraphicsForm, String filePath, String noDataMess, MessageResources messageResources, boolean withRange) throws IOException {
		final JFreeChart chart;
		if (observatoryGraphicsForm.isTridimensional()) {
			chart = ChartFactory.createStackedBarChart3D(observatoryGraphicsForm.getTitle(), observatoryGraphicsForm.getColumnTitle(), observatoryGraphicsForm.getRowTitle(),
					observatoryGraphicsForm.getDataSet(), PlotOrientation.VERTICAL, observatoryGraphicsForm.isPrintLegend(), true, false);
		} else {
			chart = ChartFactory.createStackedBarChart(observatoryGraphicsForm.getTitle(), observatoryGraphicsForm.getColumnTitle(), observatoryGraphicsForm.getRowTitle(),
					observatoryGraphicsForm.getDataSet(), PlotOrientation.VERTICAL, observatoryGraphicsForm.isPrintLegend(), true, false);
		}
		chart.getTitle().setFont(TITLE_FONT);
		if (observatoryGraphicsForm.isPrintLegend()) {
			formatLegend(chart);
		}
		CategoryPlot plot = chart.getCategoryPlot();
		plot.setNoDataMessage(noDataMess);
		configNoDataMessage(plot);
		plot.setBackgroundPaint(Color.WHITE);
		// Elimina la transparencia de las gráficas
		plot.setForegroundAlpha(1.0f);
		/**
		 * 
		 * 
		 */
		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setDrawBarOutline(true);
		renderer.setBaseOutlinePaint(Color.BLACK);
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setRenderAsPercentages(true);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		renderer.setBaseItemLabelFont(ITEM_LABEL_FONT);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setDrawBarOutline(true);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
		DefaultCategoryDataset dataset = observatoryGraphicsForm.getDataSet();
		/*
		 * 
		 */
		String dateText = dataset.getRowKeys().get(0).toString();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(dateText)) {
			if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE)) {
				dateText = dataset.getRowKeys().get(0).toString().replace(Constants.OBS_COMPILANCE, "");
			} else if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE_NONE)) {
				dateText = dataset.getRowKeys().get(0).toString().replace(Constants.OBS_COMPILANCE_NONE, "");
			} else if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE_NA)) {
				dateText = dataset.getRowKeys().get(0).toString().replace(Constants.OBS_COMPILANCE_NA, "");
			}
		}
		KeyToGroupMap map = new KeyToGroupMap(dateText);
		// Review keys
		for (int j = 0; j < dataset.getRowKeys().size(); j++) {
			dateText = dataset.getRowKeys().get(j).toString();
			if (!org.apache.commons.lang3.StringUtils.isEmpty(dateText)) {
				if (dateText.trim().endsWith(Constants.OBS_COMPILANCE)) {
					dateText = dataset.getRowKeys().get(j).toString().replace(Constants.OBS_COMPILANCE, "");
				} else if (dateText.trim().endsWith(Constants.OBS_COMPILANCE_NONE)) {
					dateText = dataset.getRowKeys().get(j).toString().replace(Constants.OBS_COMPILANCE_NONE, "");
				} else if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE_NA)) {
					dateText = dataset.getRowKeys().get(j).toString().replace(Constants.OBS_COMPILANCE_NA, "");
				}
			}
			map.mapKeyToGroup(dataset.getRowKeys().get(j).toString(), dateText);
		}
		renderer.setSeriesToGroupMap(map);
		renderer.setItemMargin(0.0);
		Paint p1 = new Color(220, 220, 220);// gray
		Paint p2 = new Color(225, 18, 13); // red
		Paint p3 = new Color(38, 187, 8);// green
		List<Paint> colors = new ArrayList<Paint>();
		colors.add(p1);
		colors.add(p2);
		colors.add(p3);
		for (int i = 0; i < 40; i += 3) {
			renderer.setSeriesPaint(i, p1);
		}
		for (int i = 1; i < 40; i += 3) {
			renderer.setSeriesPaint(i, p2);
		}
		for (int i = 2; i < 40; i += 3) {
			renderer.setSeriesPaint(i, p3);
		}
		SubCategoryAxis domainAxis = new SubCategoryAxis("Verificación / Fecha");
		domainAxis.setCategoryMargin(0.15);
		List<String> datesText = new ArrayList<>();
		for (int j = 0; j < dataset.getRowKeys().size(); j++) {
			dateText = "";
			if (dataset.getRowKeys().get(j) != null) {
				if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE)) {
					dateText = dataset.getRowKeys().get(0).toString().replace(Constants.OBS_COMPILANCE, "");
				} else if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE_NONE)) {
					dateText = dataset.getRowKeys().get(0).toString().replace(Constants.OBS_COMPILANCE_NONE, "");
				} else if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE_NA)) {
					dateText = dataset.getRowKeys().get(0).toString().replace(Constants.OBS_COMPILANCE_NA, "");
				}
			}
			dateText = dataset.getRowKeys().get(j).toString();
			if (!org.apache.commons.lang3.StringUtils.isEmpty(dateText)) {
				if (dateText.trim().endsWith(Constants.OBS_COMPILANCE)) {
					dateText = dataset.getRowKeys().get(j).toString().replace(Constants.OBS_COMPILANCE, "");
				} else if (dateText.trim().endsWith(Constants.OBS_COMPILANCE_NONE)) {
					dateText = dataset.getRowKeys().get(j).toString().replace(Constants.OBS_COMPILANCE_NONE, "");
				} else if (dataset.getRowKeys().get(0).toString().endsWith(Constants.OBS_COMPILANCE_NA)) {
					dateText = dataset.getRowKeys().get(j).toString().replace(Constants.OBS_COMPILANCE_NA, "");
				}
			}
			map.mapKeyToGroup(dataset.getRowKeys().get(j).toString(), dateText);
			if (!datesText.contains(dateText)) {
				datesText.add(dateText);
				// Hide subcategories
				// domainAxis.addSubCategory(dateText);
			}
		}
		domainAxis.setCategoryLabelPositionOffset(55);
		// apply the affine trasnform with a rotation and a translate
		AffineTransform trans = AffineTransform.getTranslateInstance(-15, 45);
		trans.concatenate(AffineTransform.getRotateInstance(-Math.PI / 4));
		domainAxis.setSubLabelFont((Font) domainAxis.getSubLabelFont().deriveFont(1, trans));
		plot.setDomainAxis(domainAxis);
		plot.setRenderer(renderer);
		final String[] legendLabels = new String[] { Constants.OBS_COMPILANCE_NA, Constants.OBS_COMPILANCE_NONE, Constants.OBS_COMPILANCE };
		if (observatoryGraphicsForm.isPrintLegend()) {
			plot.setFixedLegendItems(generateLegendG(colors, plot, legendLabels));
		}
		saveChartToFile(filePath, chart, observatoryGraphicsForm.getX(), observatoryGraphicsForm.getY());
	}

	/**
	 * The Class CstmStandardCategoryItemLabelGenerator.
	 */
	public class PercentajeItemLabelGenerator extends StandardCategoryItemLabelGenerator {
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -8008487243214911926L;

		/**
		 * Generate label.
		 *
		 * @param dataset the dataset
		 * @param row     the row
		 * @param column  the column
		 * @return the string
		 */
		@Override
		public String generateLabel(CategoryDataset dataset, int row, int column) {
			return String.format("%.0f%%", dataset.getValue(row, column).doubleValue());
		}
	}

	/**
	 * Parses the level label.
	 *
	 * @param key              the key
	 * @param messageResources the message resources
	 * @return the string
	 */
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

	/**
	 * Creates the number axis.
	 *
	 * @param plot       the plot
	 * @param percentage the percentage
	 */
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

	/**
	 * Creates the number axis without range.
	 *
	 * @param dataSet    the data set
	 * @param plot       the plot
	 * @param percentage the percentage
	 */
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

	/**
	 * Creates the domain axis.
	 *
	 * @param plot      the plot
	 * @param position  the position
	 * @param chartForm the chart form
	 */
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

	/**
	 * Define bar color.
	 *
	 * @param plot             the plot
	 * @param chartForm        the chart form
	 * @param messageResources the message resources
	 */
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

	/**
	 * Format legend.
	 *
	 * @param chart the chart
	 */
	private static void formatLegend(JFreeChart chart) {
		LegendTitle lt = chart.getLegend();
		if (lt != null) {
			lt.setItemFont(LEGEND_FONT);
			lt.setItemPaint(Color.black);
			lt.setPadding(10, 10, 10, 10);
		}
	}

	/**
	 * Generate legend.
	 *
	 * @param colors the colors
	 * @param plot   the plot
	 * @return the legend item collection
	 */
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

	/**
	 * Generate legend.
	 *
	 * @param colors       the colors
	 * @param plot         the plot
	 * @param legendLabels the legend labels
	 * @return the legend item collection
	 */
	private static LegendItemCollection generateLegendG(List<Paint> colors, CategoryPlot plot, final String[] legendLabels) {
		LegendItemCollection newLegend = new LegendItemCollection();
		Shape shape = new Rectangle(15, 15);
		BasicStroke stroke = new BasicStroke();
		for (int i = 0; i < legendLabels.length; i++) {
			LegendItem item = new LegendItem(legendLabels[i]);
			item = new LegendItem(item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, item.getLabel() + LABEL_SPACE, shape, colors.get(i), stroke,
					Color.black);
			newLegend.add(item);
		}
		return newLegend;
	}

	/**
	 * Generate one pixel legend.
	 *
	 * @param messageResources the message resources
	 * @return the legend item collection
	 */
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

	/**
	 * Put values on bars.
	 *
	 * @param chart     the chart
	 * @param chartForm the chart form
	 */
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
				// Si tiene más de 17 columnas
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
		barRenderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER, TextAnchor.BASELINE_CENTER, 0));
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
	private static void saveChartToFile(final String filePath, final JFreeChart chart, int x, int y) throws IOException {
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
			colors.add(new Color(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
		}
		return colors;
	}

	/**
	 * The Class PieRenderer.
	 */
	public static class PieRenderer {
		/** The color. */
		private Color[] color;

		/**
		 * Instantiates a new pie renderer.
		 *
		 * @param colors the colors
		 */
		public PieRenderer(Color[] colors) {
			this.color = Arrays.copyOf(colors, colors.length);
		}

		/**
		 * Sets the color.
		 *
		 * @param plot    the plot
		 * @param dataset the dataset
		 */
		public void setColor(PiePlot plot, DefaultPieDataset dataset) {
			List<Comparable> keys = dataset.getKeys();
			int aInt;
			for (int i = 0; i < keys.size(); i++) {
				aInt = i % this.color.length;
				plot.setSectionPaint(keys.get(i), this.color[aInt]);
			}
		}
	}

	/**
	 * The Class CustomLabelGenerator.
	 */
	public static class CustomLabelGenerator implements PieSectionLabelGenerator {
		/** The section label. */
		private final String sectionLabel;
		/** The total. */
		private final long total;

		/**
		 * Instantiates a new custom label generator.
		 *
		 * @param sectionLabel the section label
		 * @param total        the total
		 */
		public CustomLabelGenerator(final String sectionLabel, final long total) {
			this.sectionLabel = sectionLabel;
			this.total = total;
		}

		/**
		 * Generate section label.
		 *
		 * @param dataset the dataset
		 * @param key     the key
		 * @return the string
		 */
		public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
			final float p = (float) Math.pow(10, 2);
			if (!dataset.getValue(key).toString().equals("0")) {
				Float value = Float.valueOf(dataset.getValue(key).toString()) / (float) total * 100;
				value = value * p;
				float tmp = Math.round(value);
				value = tmp / p;
				return key + "\n" + sectionLabel + dataset.getValue(key).intValue() + "\n" + " (" + Math.round(value) + "%)";
			} else {
				return null;
			}
		}

		/**
		 * Generate attributed section label.
		 *
		 * @param arg0 the arg 0
		 * @param arg1 the arg 1
		 * @return the attributed string
		 */
		@Override
		public AttributedString generateAttributedSectionLabel(PieDataset arg0, Comparable arg1) {
			return null;
		}
	}

	/**
	 * The Class CustomLegendLabelGenerator.
	 */
	public static class CustomLegendLabelGenerator implements PieSectionLabelGenerator {
		/**
		 * Generate section label.
		 *
		 * @param dataset the dataset
		 * @param key     the key
		 * @return the string
		 */
		@Override
		public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
			return key + LABEL_SPACE;
		}

		/**
		 * Generate attributed section label.
		 *
		 * @param arg0 the arg 0
		 * @param arg1 the arg 1
		 * @return the attributed string
		 */
		@Override
		public AttributedString generateAttributedSectionLabel(PieDataset arg0, Comparable arg1) {
			return null;
		}
	}

	/**
	 * The Class LabelGenerator.
	 */
	public static class LabelGenerator implements CategoryItemLabelGenerator {
		/** The is percentage. */
		private boolean isPercentage;

		/**
		 * Instantiates a new label generator.
		 *
		 * @param isPercentage the is percentage
		 */
		public LabelGenerator(boolean isPercentage) {
			this.isPercentage = isPercentage;
		}

		/**
		 * Generate label.
		 *
		 * @param dataset  the dataset
		 * @param series   the series
		 * @param category the category
		 * @return the string
		 */
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

		/**
		 * Truncate decimal part.
		 *
		 * @param stringValue the string value
		 * @return the string
		 */
		private String truncateDecimalPart(final String stringValue) {
			if (stringValue.contains(".0") && stringValue.indexOf('.') + 2 == stringValue.length()) {
				return stringValue.replace(".0", "");
			} else if (stringValue.contains(".00") && stringValue.indexOf('.') + 3 == stringValue.length()) {
				return stringValue.replace(".00", "");
			} else {
				return stringValue;
			}
		}

		/**
		 * Generate column label.
		 *
		 * @param arg0 the arg 0
		 * @param arg1 the arg 1
		 * @return the string
		 */
		@Override
		public String generateColumnLabel(CategoryDataset arg0, int arg1) {
			return null;
		}

		/**
		 * Generate row label.
		 *
		 * @param arg0 the arg 0
		 * @param arg1 the arg 1
		 * @return the string
		 */
		@Override
		public String generateRowLabel(CategoryDataset arg0, int arg1) {
			return null;
		}
	}

	/**
	 * The Class CategoryRenderer.
	 */
	public static class CategoryRenderer {
		/** The color. */
		private List<Paint> color;

		/**
		 * Instantiates a new category renderer.
		 *
		 * @param colors the colors
		 */
		public CategoryRenderer(List<Paint> colors) {
			this.color = colors;
		}

		/**
		 * Sets the color.
		 *
		 * @param plot    the plot
		 * @param dataset the dataset
		 */
		public void setColor(CategoryPlot plot, DefaultCategoryDataset dataset) {
			List keys = dataset.getRowKeys();
			int aInt;
			for (int i = 0; i < keys.size(); i++) {
				aInt = i % this.color.size();
				plot.getRenderer().setSeriesPaint(i, this.color.get(aInt));
			}
		}
	}

	/**
	 * The Class CustomRenderer3D.
	 */
	public static class CustomRenderer3D extends BarRenderer3D {
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 5509573551140317879L;
		/** The colors. */
		private Paint[] colors;

		/**
		 * Instantiates a new custom renderer 3 D.
		 *
		 * @param colors the colors
		 */
		public CustomRenderer3D(final Paint[] colors) {
			this.colors = Arrays.copyOf(colors, colors.length);
		}

		/**
		 * Gets the item paint.
		 *
		 * @param row    the row
		 * @param column the column
		 * @return the item paint
		 */
		@Override
		public Paint getItemPaint(final int row, final int column) {
			return this.colors[column % this.colors.length];
		}
	}

	/**
	 * The Class CustomRenderer.
	 */
	public static class CustomRenderer extends BarRenderer {
		/** The colors. */
		private Paint[] colors;

		/**
		 * Instantiates a new custom renderer.
		 *
		 * @param colors the colors
		 */
		public CustomRenderer(final Paint[] colors) {
			this.colors = Arrays.copyOf(colors, colors.length);
		}

		/**
		 * Gets the item paint.
		 *
		 * @param row    the row
		 * @param column the column
		 * @return the item paint
		 */
		@Override
		public Paint getItemPaint(final int row, final int column) {
			return this.colors[column % this.colors.length];
		}
	}
}