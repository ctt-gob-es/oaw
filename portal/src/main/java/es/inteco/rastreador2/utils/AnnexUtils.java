/* ******************************************************************************
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
 ***************************************************************************** */
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFLineProperties;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.AxisTickLabelPosition;
import org.apache.poi.xddf.usermodel.chart.AxisTickMark;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.XDDFBarChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.export.database.form.CategoryForm;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.export.database.form.ObservatoryForm;
import es.inteco.rastreador2.export.database.form.PageForm;
import es.inteco.rastreador2.export.database.form.SiteForm;
import es.inteco.rastreador2.export.database.form.VerificationScoreForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.manager.ObservatoryExportManager;
import es.oaw.wcagem.WcagEmUtils;
import es.oaw.wcagem.enums.WcagEmPointKey;
import es.oaw.wcagem.util.ValidationDetails;

/**
 * The Class AnnexUtils.
 */
@SuppressWarnings("deprecation")
public final class AnnexUtils {
	/** The Constant TW_DECIMALS_FORMAT. */
	private static final String TWO_DECIMALS_FORMAT = "0.00";
	/** The Constant PERCENT_FORMAT. */
	private static final String PERCENT_FORMAT = "0.00%";
	/** The Constant RANGE_THIRD_COLUMN_RANKING. */
	private static final String RANGE_THIRD_COLUMN_RANKING = "I:I";
	/** The Constant RANGE_SECOND_COLUMN_RANKING. */
	private static final String RANGE_SECOND_COLUMN_RANKING = "F:F";
	/** The Constant RANGE_FIRST_COLUMN_RANKING. */
	private static final String RANGE_FIRST_COLUMN_RANKING = "C:C";
	/** The Constant RANGE_TOTAL_PORTALS_RANKING. */
	private static final String RANGE_TOTAL_PORTALS_RANKING = "M:M";
	/** The Constant COLOR_BLACK. */
	private static final short COLOR_BLACK = IndexedColors.BLACK.getIndex();
	/** The Constant COLOR_WHITE. */
	private static final short COLOR_WHITE = IndexedColors.WHITE.getIndex();
	/** The Constant ARIAL_FONT_NAME. */
	private static final String ARIAL_FONT_NAME = "Arial";
	/** The Constant EARL_INAPPLICABLE. */
	private static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	private static final String EARL_FAILED = "earl:failed";
	/**
	 * The Constant EMPTY_STRING.
	 */
	private static final String EMPTY_STRING = "";
	/**
	 * The Constant RESULTADOS_ELEMENT.
	 */
	private static final String RESULTADOS_ELEMENT = "resultados";
	/**
	 * The Constant NOMBRE_ELEMENT.
	 */
	private static final String NOMBRE_ELEMENT = "nombre";
	/**
	 * The Constant CATEGORIA_ELEMENT.
	 */
	private static final String CATEGORIA_ELEMENT = "categoria";
	/**
	 * The Constant DEPENDE_DE_ELEMENT.
	 */
	private static final String DEPENDE_DE_ELEMENT = "depende_de";
	/**
	 * The Constant PORTAL_ELEMENT.
	 */
	private static final String PORTAL_ELEMENT = "portal";
	/**
	 * Excel lines created by generation Evolution and reused generating PerDependency annex.
	 */
	private static HashMap<Integer, ExcelLine> excelLines;
	/**
	 * Column names list created by generation Evolution and reused generating PerDependency annex.
	 */
	private static List<String> ColumnNames;
	/**
	 * Execution dates list created by generation Evolution and reused generating PerDependency annex.
	 */
	private static List<String> executionDates = new ArrayList<>();
	/** The execution dates with format. */
	private static List<Date> executionDatesWithFormat = new ArrayList<>();
	/** The execution dates with format valid. */
	private static List<Date> executionDatesWithFormat_Valid = new ArrayList<>();
	/**
	 * Dependency names list created by generation Evolution and reused generating PerDependency annex.
	 */
	private static List<String> dependencies;
	/** The annexmap. */
	private static Map<Long, TreeMap<String, ScoreForm>> annexmap;
	/** The evaluation ids. */
	private static List<Long> evaluationIds;
	/** The observatory manager. */
	private static es.gob.oaw.rastreador2.observatorio.ObservatoryManager observatoryManager;
	/** The current evaluation page list. */
	private static List<ObservatoryEvaluationForm> currentEvaluationPageList;
	/** The bold percent 11 center style. */
	private static CellStyle boldPercent11CenterStyle;
	/** The white bold 16 font. */
	private static XSSFFont whiteBold16Font;
	/** The white bold 10 font. */
	private static XSSFFont whiteBold10Font;
	/** The black bold 10 font. */
	private static XSSFFont blackBold10Font;
	/** The black normal 11 font. */
	private static XSSFFont blackNormal11Font;
	/** The black bold 11 font. */
	private static XSSFFont blackBold11Font;
	/** The bold data 11 left style. */
	private static CellStyle boldData11LeftStyle;
	/** The bold data 11 center style. */
	private static CellStyle boldData11CenterStyle;
	/** The blue background white bold 16. */
	private static CellStyle blueBackgroundWhiteBold16;
	/** The green background white bold 16. */
	private static CellStyle greenBackgroundWhiteBold16;
	/** The yellow background black 10. */
	private static CellStyle yellowBackgroundBlack10;
	/** The red backgrounf black bold 10. */
	private static CellStyle redBackgrounfBlackBold10;
	/** The orange background black bold 10. */
	private static CellStyle orangeBackgroundBlackBold10;
	/** The blue background black bold 10. */
	private static CellStyle blueBackgroundBlackBold10;
	/** The light blue background black normal 11. */
	private static CellStyle lightBlueBackgroundBlackNormal11;
	/** The white backgrount normal 11 center. */
	private static CellStyle whiteBackgrountNormal11Center;
	/** The white backgrount normal 11 decimal center. */
	private static CellStyle whiteBackgrountNormal11DecimalCenter;

	/**
	 * Generate all annex.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @param comparision      the comparision
	 * @param firstThreshold   the first threshold
	 * @param secondThreshold  the second threshold
	 * @throws Exception the exception
	 */
	public static void generateAllAnnex(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter, final String[] exObsIds,
			final List<ComparisionForm> comparision, double firstThreshold, double secondThreshold) throws Exception {
		annexmap = createAnnexMap(idObsExecution, tagsToFilter, exObsIds);
		evaluationIds = AnalisisDatos.getEvaluationIdsFromExecutedObservatory(idObsExecution);
		observatoryManager = new es.gob.oaw.rastreador2.observatorio.ObservatoryManager();
		currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(idObsExecution, evaluationIds);
		createAnnexPaginas(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPaginasVerifications(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPaginasCriteria(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPortales(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPortalsVerification(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPortalsCriteria(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexXLSX2(messageResources, idObsExecution, idOperation);
		createAnnexXLSX1_Evolution(messageResources, idObsExecution, idOperation, comparision, firstThreshold, secondThreshold);
		createAnnexXLSX_PerDependency(idOperation);
		createAnnexXLSXRanking(messageResources, idObsExecution, idOperation);
		createComparativeSuitabilitieXLSX(messageResources, idObsExecution, idOperation);
	}

	/**
	 * Creates the annex XLSX ranking.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @throws Exception the exception
	 */
	public static void createAnnexXLSXRanking(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
		// Clone file 2
		final PropertiesManager pmgr = new PropertiesManager();
		final File originalWb = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "2. Iteración SW.xlsx");
		final FileOutputStream fos = new FileOutputStream(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + "3. Iteración Ranking.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(originalWb));
		final XSSFSheet resultSheet = wb.getSheet("Resultados");
		final String sheetnameAllocation = "Ranking Adecuación";
		final String sheetnameCompliance = "Ranking Cumplimiento";
		final String[] columnNamesAllocation = new String[] { "ORGANISMO", "TOTAL PORTALES AA", "% AA", "NOTA MEDIA AA", "TOTAL PORTALES A", "% A", "NOTA MEDIA A", "TOTAL PORTALES NO VÁLIDO",
				"% NO VÁLIDO", "NOTA MEDIA NV", "% NO CUMPLEN", "TOTAL PORTALES" };
		// In order left to right
		final String[] columnResultsAllocation = new String[] { "R", "P", "Q" };
		final String[] columnResultsCompliance = new String[] { "U", "T", "S" };
		final String[] columnNamesCompliance = new String[] { "ORGANISMO", "TOTAL PORTALES TC", "% TC", "NOTA MEDIA TC", "TOTAL PORTALES PC", "% PC", "NOTA MEDIA PC", "TOTAL PORTALES NC", "% NC",
				"NOTA MEDIA NC", "% NO CONFORMES", "TOTAL PORTALES" };
		// Add table with filters
		CellReference ref = new CellReference("A1");
		CellReference topLeft = new CellReference(resultSheet.getRow(ref.getRow()).getCell(ref.getCol()));
		ref = new CellReference("U" + resultSheet.getLastRowNum());
		CellReference bottomRight = new CellReference(resultSheet.getRow(ref.getRow()).getCell(ref.getCol()));
		// Datatable requires at least 2 rows
		if (bottomRight.getRow() - topLeft.getRow() >= 2) {
			AreaReference tableArea = wb.getCreationHelper().createAreaReference(topLeft, bottomRight);
			XSSFTable dataTable = resultSheet.createTable(tableArea);
			dataTable.setDisplayName("Tabla1");
			// this sets auto filters
			dataTable.getCTTable().addNewAutoFilter().setRef(tableArea.formatAsString());
		}
		addRankingSheet(wb, resultSheet, sheetnameAllocation, columnNamesAllocation, columnResultsAllocation, "TablaRankingA");
		addRankingSheet(wb, resultSheet, sheetnameCompliance, columnNamesCompliance, columnResultsCompliance, "TablaRankingC");
		// Conditional formatting
		addConditionaFormatting(wb.getSheet(sheetnameAllocation));
		addConditionaFormatting(wb.getSheet(sheetnameCompliance));
		// Recalculate formulas
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		wb.write(fos);
		fos.close();
	}

	/**
	 * Adds the conditiona formatting.
	 *
	 * @param sheet the sheet
	 */
	private static void addConditionaFormatting(final XSSFSheet sheet) {
		SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
		ConditionalFormattingRule rule = sheetCF.createConditionalFormattingRule(ComparisonOperator.GE, "0.5", null);
		PatternFormatting fill = rule.createPatternFormatting();
		fill.setFillBackgroundColor(IndexedColors.ROSE.index);
		fill.setFillPattern(PatternFormatting.SOLID_FOREGROUND);
		ConditionalFormattingRule[] cfRules = new ConditionalFormattingRule[] { rule };
		CellRangeAddress[] regions = new CellRangeAddress[] { CellRangeAddress.valueOf("J2:J5000"), CellRangeAddress.valueOf("L2:L5000") };
		sheetCF.addConditionalFormatting(regions, cfRules);
	}

	/**
	 * Adds the ranking sheet.
	 *
	 * @param wb            the wb
	 * @param resultSheet   the result sheet
	 * @param sheetname     the sheetname
	 * @param columnNames   the column names
	 * @param columnResults the column results
	 * @param tableName     the table name
	 */
	private static void addRankingSheet(XSSFWorkbook wb, final XSSFSheet resultSheet, final String sheetname, final String[] columnNames, final String[] columnResults, final String tableName) {
		final String resultColumnDependecy = "F";
		final XSSFDataFormat format = wb.createDataFormat();
		final XSSFSheet tmpSheet = wb.createSheet(sheetname);
		final CreationHelper createHelper = wb.getCreationHelper();
		wb.setSheetOrder(sheetname, 0);
		XSSFRow row;
		XSSFCell cell;
		int rowIndex = 0;
		int columnIndex = 1;
		initFonts(wb);
		initStyles(wb, createHelper);
		// Reset
		columnIndex = 1;
		Cell c = null;
		// Fill B with distinct depende D
		List<String> dependencies = getValues(resultColumnDependecy, resultSheet);
		// Fill other columns
		rowIndex = 1;
		int numOfDependencies = 0;
		for (String dependency : dependencies) {
			row = tmpSheet.createRow(rowIndex);
			cell = row.createCell(1);
			cell.setCellValue(dependency);
			cell.setCellStyle(boldData11LeftStyle);
			rowIndex++;
			numOfDependencies++;
		}
		rowIndex = 1;
		try {
			for (int i = 0; i < numOfDependencies; i++) {
				int cellCount = 2;
				Row r = tmpSheet.getRow(rowIndex + i);
				/******** AA/TC **********/
				cellCount = generateColumnCount(columnResults, resultColumnDependecy, boldData11CenterStyle, cellCount, r, 0);
				cellCount = generateColumnPercent(boldData11CenterStyle, format, cellCount, r, RANGE_TOTAL_PORTALS_RANKING, RANGE_FIRST_COLUMN_RANKING);
				cellCount = generetaColumnAverage(columnResults, resultColumnDependecy, whiteBackgrountNormal11DecimalCenter, format, cellCount, r, 0, RANGE_FIRST_COLUMN_RANKING);
				/******** A/PC **********/
				cellCount = generateColumnCount(columnResults, resultColumnDependecy, boldData11CenterStyle, cellCount, r, 1);
				cellCount = generateColumnPercent(boldData11CenterStyle, format, cellCount, r, RANGE_TOTAL_PORTALS_RANKING, RANGE_SECOND_COLUMN_RANKING);
				cellCount = generetaColumnAverage(columnResults, resultColumnDependecy, whiteBackgrountNormal11DecimalCenter, format, cellCount, r, 1, RANGE_SECOND_COLUMN_RANKING);
				/******** NV/NC **********/
				cellCount = generateColumnCount(columnResults, resultColumnDependecy, boldData11CenterStyle, cellCount, r, 2);
				cellCount = generateColumnPercent(boldData11CenterStyle, format, cellCount, r, RANGE_TOTAL_PORTALS_RANKING, RANGE_THIRD_COLUMN_RANKING);
				cellCount = generetaColumnAverage(columnResults, resultColumnDependecy, whiteBackgrountNormal11DecimalCenter, format, cellCount, r, 1, RANGE_THIRD_COLUMN_RANKING);
				/******** SUMS **********/
				c = r.createCell(cellCount);
				c.setCellFormula("J:J+G:G");
				c.setCellStyle(boldPercent11CenterStyle);
				cellCount++;
				/***/
				c = r.createCell(cellCount);
				c.setCellStyle(boldData11CenterStyle);
				c.setCellFormula("C:C+F:F+I:I");
				cellCount++;
			}
//			evaluateAllFormulaCellsSheet(tmpSheet, wb.getCreationHelper().createFormulaEvaluator());
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			// Sort rows
			XSSFSheet rankingSheet = sortSheet(wb, tmpSheet, sheetname, 2);
			// Reset
			columnIndex = 1;
			// Add headers
			int headerRowIndex = 0;
			row = rankingSheet.createRow(headerRowIndex);
			for (String name : columnNames) {
				cell = row.createCell(columnIndex);
				cell.setCellValue(name);
				columnIndex++;
			}
			// Reset
			columnIndex = 1;
			// Set header styles
			c = row.getCell(columnIndex++);
			c.setCellStyle(blueBackgroundWhiteBold16);
			c = row.getCell(columnIndex++);
			c.setCellStyle(greenBackgroundWhiteBold16);
			c = row.getCell(columnIndex++);
			c.setCellStyle(greenBackgroundWhiteBold16);
			c = row.getCell(columnIndex++);
			c.setCellStyle(greenBackgroundWhiteBold16);
			c = row.getCell(columnIndex++);
			c.setCellStyle(yellowBackgroundBlack10);
			c = row.getCell(columnIndex++);
			c.setCellStyle(yellowBackgroundBlack10);
			c = row.getCell(columnIndex++);
			c.setCellStyle(yellowBackgroundBlack10);
			c = row.getCell(columnIndex++);
			c.setCellStyle(redBackgrounfBlackBold10);
			c = row.getCell(columnIndex++);
			c.setCellStyle(redBackgrounfBlackBold10);
			c = row.getCell(columnIndex++);
			c.setCellStyle(redBackgrounfBlackBold10);
			c = row.getCell(columnIndex++);
			c.setCellStyle(orangeBackgroundBlackBold10);
			c = row.getCell(columnIndex++);
			c.setCellStyle(blueBackgroundBlackBold10);
			// Table with autofilter
			CellReference ref = new CellReference("B1");
			CellReference topLeft = new CellReference(rankingSheet.getRow(ref.getRow()).getCell(ref.getCol()));
			ref = new CellReference("M" + (numOfDependencies + 1));
			CellReference bottomRight = new CellReference(rankingSheet.getRow(ref.getRow()).getCell(ref.getCol()));
			// Datatable requires at least 2 rows
			if (bottomRight.getRow() - topLeft.getRow() >= 2) {
				AreaReference tableArea = wb.getCreationHelper().createAreaReference(topLeft, bottomRight);
				XSSFTable dataTableRanking = rankingSheet.createTable(tableArea);
				dataTableRanking.setDisplayName(tableName);
				// Autofilter
				dataTableRanking.getCTTable().addNewAutoFilter().setRef(tableArea.formatAsString());
			}
			// Set poduim cells
			ref = new CellReference("A2");
			c = rankingSheet.getRow(ref.getRow()).createCell(ref.getCol());
			c.setCellValue("1ª");
			CellStyle firstStyle = wb.createCellStyle();
			firstStyle.setWrapText(true);
			firstStyle.setAlignment(HorizontalAlignment.CENTER);
			firstStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			firstStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
			firstStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			firstStyle.setFont(blackBold10Font);
			c.setCellStyle(firstStyle);
			ref = new CellReference("A3");
			c = rankingSheet.getRow(ref.getRow()).createCell(ref.getCol());
			CellStyle secondStyle = wb.createCellStyle();
			secondStyle.setWrapText(true);
			secondStyle.setAlignment(HorizontalAlignment.CENTER);
			secondStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			secondStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			secondStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			secondStyle.setFont(blackBold10Font);
			c.setCellStyle(secondStyle);
			c.setCellValue("2ª");
			ref = new CellReference("A4");
			c = rankingSheet.getRow(ref.getRow()).createCell(ref.getCol());
			c.setCellValue("3ª");
			CellStyle thirdStyle = wb.createCellStyle();
			thirdStyle.setWrapText(true);
			thirdStyle.setAlignment(HorizontalAlignment.CENTER);
			thirdStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			thirdStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());
			thirdStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			thirdStyle.setFont(blackBold10Font);
			c.setCellStyle(thirdStyle);
			// Set auto size
			columnIndex = 1;
			for (int i = 0; i < columnNames.length; i++) {
				rankingSheet.autoSizeColumn(columnIndex);
				columnIndex++;
			}
		} catch (Exception e) {
			Logger.putLog("Error generating 3. Iteración Ranking.xlsx", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Inits the styles.
	 *
	 * @param wb           the wb
	 * @param createHelper the create helper
	 */
	private static void initStyles(XSSFWorkbook wb, final CreationHelper createHelper) {
		boldData11LeftStyle = wb.createCellStyle();
		boldData11LeftStyle.setWrapText(true);
		boldData11LeftStyle.setAlignment(HorizontalAlignment.LEFT);
		boldData11LeftStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		boldData11LeftStyle.setFillForegroundColor(COLOR_WHITE);
		boldData11LeftStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		boldData11LeftStyle.setFont(blackBold11Font);
		boldData11CenterStyle = wb.createCellStyle();
		boldData11CenterStyle.setWrapText(true);
		boldData11CenterStyle.setAlignment(HorizontalAlignment.CENTER);
		boldData11CenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		boldData11CenterStyle.setFillForegroundColor(COLOR_WHITE);
		boldData11CenterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		boldData11CenterStyle.setFont(blackBold11Font);
		boldPercent11CenterStyle = wb.createCellStyle();
		boldPercent11CenterStyle.setWrapText(true);
		boldPercent11CenterStyle.setAlignment(HorizontalAlignment.CENTER);
		boldPercent11CenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		boldPercent11CenterStyle.setFillForegroundColor(COLOR_WHITE);
		boldPercent11CenterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		boldPercent11CenterStyle.setFont(blackBold11Font);
		boldPercent11CenterStyle.setDataFormat(createHelper.createDataFormat().getFormat(PERCENT_FORMAT));
		blueBackgroundWhiteBold16 = wb.createCellStyle();
		blueBackgroundWhiteBold16.setWrapText(true);
		blueBackgroundWhiteBold16.setAlignment(HorizontalAlignment.CENTER);
		blueBackgroundWhiteBold16.setVerticalAlignment(VerticalAlignment.CENTER);
		blueBackgroundWhiteBold16.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		blueBackgroundWhiteBold16.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		blueBackgroundWhiteBold16.setFont(whiteBold16Font);
		greenBackgroundWhiteBold16 = wb.createCellStyle();
		greenBackgroundWhiteBold16.setWrapText(true);
		greenBackgroundWhiteBold16.setAlignment(HorizontalAlignment.CENTER);
		greenBackgroundWhiteBold16.setVerticalAlignment(VerticalAlignment.CENTER);
		greenBackgroundWhiteBold16.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		greenBackgroundWhiteBold16.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		greenBackgroundWhiteBold16.setFont(whiteBold10Font);
		yellowBackgroundBlack10 = wb.createCellStyle();
		yellowBackgroundBlack10.setWrapText(true);
		yellowBackgroundBlack10.setAlignment(HorizontalAlignment.CENTER);
		yellowBackgroundBlack10.setVerticalAlignment(VerticalAlignment.CENTER);
		yellowBackgroundBlack10.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		yellowBackgroundBlack10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		yellowBackgroundBlack10.setFont(blackBold10Font);
		redBackgrounfBlackBold10 = wb.createCellStyle();
		redBackgrounfBlackBold10.setWrapText(true);
		redBackgrounfBlackBold10.setAlignment(HorizontalAlignment.CENTER);
		redBackgrounfBlackBold10.setVerticalAlignment(VerticalAlignment.CENTER);
		redBackgrounfBlackBold10.setFillForegroundColor(IndexedColors.RED.getIndex());
		redBackgrounfBlackBold10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		redBackgrounfBlackBold10.setFont(blackBold10Font);
		orangeBackgroundBlackBold10 = wb.createCellStyle();
		orangeBackgroundBlackBold10.setWrapText(true);
		orangeBackgroundBlackBold10.setAlignment(HorizontalAlignment.CENTER);
		orangeBackgroundBlackBold10.setVerticalAlignment(VerticalAlignment.CENTER);
		orangeBackgroundBlackBold10.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		orangeBackgroundBlackBold10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		orangeBackgroundBlackBold10.setFont(blackBold10Font);
		blueBackgroundBlackBold10 = wb.createCellStyle();
		blueBackgroundBlackBold10.setWrapText(true);
		blueBackgroundBlackBold10.setAlignment(HorizontalAlignment.CENTER);
		blueBackgroundBlackBold10.setVerticalAlignment(VerticalAlignment.CENTER);
		blueBackgroundBlackBold10.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		blueBackgroundBlackBold10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		blueBackgroundBlackBold10.setFont(blackBold10Font);
		lightBlueBackgroundBlackNormal11 = wb.createCellStyle();
		lightBlueBackgroundBlackNormal11.setWrapText(true);
		lightBlueBackgroundBlackNormal11.setAlignment(HorizontalAlignment.CENTER);
		lightBlueBackgroundBlackNormal11.setVerticalAlignment(VerticalAlignment.CENTER);
		lightBlueBackgroundBlackNormal11.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		lightBlueBackgroundBlackNormal11.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		lightBlueBackgroundBlackNormal11.setFont(blackNormal11Font);
		whiteBackgrountNormal11Center = wb.createCellStyle();
		whiteBackgrountNormal11Center.setWrapText(true);
		whiteBackgrountNormal11Center.setAlignment(HorizontalAlignment.CENTER);
		whiteBackgrountNormal11Center.setVerticalAlignment(VerticalAlignment.CENTER);
		whiteBackgrountNormal11Center.setFillForegroundColor(COLOR_WHITE);
		whiteBackgrountNormal11Center.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		whiteBackgrountNormal11Center.setFont(blackNormal11Font);
		whiteBackgrountNormal11DecimalCenter = wb.createCellStyle();
		whiteBackgrountNormal11DecimalCenter.setWrapText(true);
		whiteBackgrountNormal11DecimalCenter.setAlignment(HorizontalAlignment.CENTER);
		whiteBackgrountNormal11DecimalCenter.setVerticalAlignment(VerticalAlignment.CENTER);
		whiteBackgrountNormal11DecimalCenter.setFillForegroundColor(COLOR_WHITE);
		whiteBackgrountNormal11DecimalCenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		whiteBackgrountNormal11DecimalCenter.setFont(blackNormal11Font);
		whiteBackgrountNormal11DecimalCenter.setDataFormat(createHelper.createDataFormat().getFormat(TWO_DECIMALS_FORMAT));
	}

	/**
	 * Inits the fonts.
	 *
	 * @param wb the wb
	 */
	private static void initFonts(XSSFWorkbook wb) {
		whiteBold16Font = wb.createFont();
		whiteBold16Font.setFontHeightInPoints((short) 16);
		whiteBold16Font.setFontName(ARIAL_FONT_NAME);
		whiteBold16Font.setColor(COLOR_WHITE);
		whiteBold16Font.setBold(true);
		whiteBold16Font.setItalic(false);
		whiteBold10Font = wb.createFont();
		whiteBold10Font.setFontHeightInPoints((short) 10.5);
		whiteBold10Font.setFontName(ARIAL_FONT_NAME);
		whiteBold10Font.setColor(COLOR_WHITE);
		whiteBold10Font.setBold(true);
		whiteBold10Font.setItalic(false);
		blackBold10Font = wb.createFont();
		blackBold10Font.setFontHeightInPoints((short) 10.5);
		blackBold10Font.setFontName(ARIAL_FONT_NAME);
		blackBold10Font.setColor(COLOR_BLACK);
		blackBold10Font.setBold(true);
		blackBold10Font.setItalic(false);
		blackNormal11Font = wb.createFont();
		blackNormal11Font.setFontHeightInPoints((short) 11);
		blackNormal11Font.setFontName(ARIAL_FONT_NAME);
		blackNormal11Font.setColor(COLOR_BLACK);
		blackNormal11Font.setBold(false);
		blackNormal11Font.setItalic(false);
		blackBold11Font = wb.createFont();
		blackBold11Font.setFontHeightInPoints((short) 11);
		blackBold11Font.setFontName(ARIAL_FONT_NAME);
		blackBold11Font.setColor(COLOR_BLACK);
		blackBold11Font.setBold(true);
		blackBold11Font.setItalic(false);
	}

	/**
	 * Gets the cell value as string.
	 *
	 * @param wb   the wb
	 * @param cell the cell
	 * @return the cell value as string
	 */
	private static String getCellValueAsString(final XSSFWorkbook wb, final Cell cell) {
		switch (cell.getCellType()) {
		case BLANK:
			return cell.getStringCellValue();
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case ERROR:
			return String.valueOf(cell.getErrorCellValue());
		case FORMULA:
			return getCellValueAsString(wb.getCreationHelper().createFormulaEvaluator().evaluate(cell));
		case NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case STRING:
			return String.valueOf(cell.getRichStringCellValue());
		default:
			return "";
		}
	}

	/**
	 * Gets the cell value as string.
	 *
	 * @param cellvalue the cellvalue
	 * @return the cell value as string
	 */
	private static String getCellValueAsString(final CellValue cellvalue) {
		switch (cellvalue.getCellType()) {
		case BLANK:
			return cellvalue.getStringValue();
		case BOOLEAN:
			return String.valueOf(cellvalue.getBooleanValue());
		case ERROR:
			return String.valueOf(cellvalue.getErrorValue());
		case NUMERIC:
			return String.valueOf(cellvalue.getNumberValue());
		case STRING:
			return String.valueOf(cellvalue.getStringValue());
		default:
			return "";
		}
	}

	/**
	 * Sort sheet.
	 *
	 * @param workbook        the workbook
	 * @param sheet           the sheet
	 * @param sheetName       the sheet name
	 * @param columnIndexSort the column index sort (0 based)
	 * @return the XSSF sheet
	 */
	public static XSSFSheet sortSheet(final XSSFWorkbook workbook, final XSSFSheet sheet, final String sheetName, final int columnIndexSort) {
		// Get all original rows
		List<Row> rows = getAllRows(sheet);
		// Sort descending by column index passed
		Collections.sort(rows, new Comparator<Row>() {
			@Override
			public int compare(Row row1, Row row2) {
				float floatRow2 = Float.parseFloat(getCellValueAsString(workbook, row2.getCell(columnIndexSort)));
				float floatRow1 = Float.parseFloat(getCellValueAsString(workbook, row1.getCell(columnIndexSort)));
				return Float.compare(floatRow2, floatRow1);
			}
		});
		// Remove original sheet
		workbook.removeSheetAt(workbook.getSheetIndex(sheet));
		// Create new sheet and add rows sorted
		XSSFSheet newSheet = workbook.createSheet(sheetName);
		workbook.setSheetOrder(sheetName, 0);
		addRows(workbook, newSheet, rows);
		return newSheet;
	}

	/**
	 * Adds the rows.
	 *
	 * @param workbook the workbook
	 * @param sheet    the sheet
	 * @param rows     the rows
	 */
	private static void addRows(final XSSFWorkbook workbook, final XSSFSheet sheet, final List<Row> rows) {
		for (int i = 0; i < rows.size(); i++) {
			Row newRow = sheet.createRow(i + 1);
			Row sourceRow = rows.get(i);
			// Loop through source columns to add to new row
			for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
				// Grab a copy of the old/new cell
				Cell oldCell = sourceRow.getCell(j);
				Cell newCell = newRow.createCell(j);
				// If the old cell is null jump to next cell
				if (oldCell == null) {
					newCell = null;
					continue;
				}
				// Copy style from old cell and apply to new cell
				CellStyle newCellStyle = workbook.createCellStyle();
				newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
				// Set the cell data value
				switch (oldCell.getCellType()) {
				case BLANK:
					newCell.setCellValue(oldCell.getStringCellValue());
					break;
				case BOOLEAN:
					newCell.setCellValue(oldCell.getBooleanCellValue());
					break;
				case ERROR:
					newCell.setCellErrorValue(oldCell.getErrorCellValue());
					break;
				case FORMULA:
					newCell.setCellFormula(oldCell.getCellFormula());
					break;
				case NUMERIC:
					newCell.setCellValue(oldCell.getNumericCellValue());
					break;
				case STRING:
					newCell.setCellValue(oldCell.getRichStringCellValue());
					break;
				default:
					newCell.setCellValue("");
					break;
				}
				newCell.setCellStyle(newCellStyle);
			}
			// If there are are any merged regions in the source row, copy to new row
			for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
				CellRangeAddress cellRangeAddress = sheet.getMergedRegion(j);
				if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
					CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(), (newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
							cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
					sheet.addMergedRegion(newCellRangeAddress);
				}
			}
		}
	}

	/**
	 * Removes the all rows.
	 *
	 * @param sheet the sheet
	 * @return the all rows
	 */
	private static List<Row> getAllRows(XSSFSheet sheet) {
		List<Row> rows = new ArrayList<>();
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			rows.add(sheet.getRow(i + 1));
		}
		return rows;
	}

	/**
	 * Genereta column average.
	 *
	 * @param columnResults                 the column results
	 * @param resultColumnDependecy         the result column dependecy
	 * @param whiteBackgrountNormal11Center the white backgrount normal 11 center
	 * @param format                        the format
	 * @param cellCount                     the cell count
	 * @param r                             the r
	 * @param resultCount                   the result count
	 * @param range1                        the range 1
	 * @return the int
	 */
	private static int generetaColumnAverage(final String[] columnResults, final String resultColumnDependecy, CellStyle whiteBackgrountNormal11Center, XSSFDataFormat format, int cellCount, Row r,
			int resultCount, final String range1) {
		Cell c;
		c = r.createCell(cellCount);
		c.setCellFormula("IF(" + range1 + ">0,SUMIFS(Resultados!$" + columnResults[resultCount] + ":$" + columnResults[resultCount] + ",Resultados!$" + resultColumnDependecy + ":$"
				+ resultColumnDependecy + ",\"*\"&B:B&\"*\")/" + range1 + ",0)");
		c.setCellStyle(whiteBackgrountNormal11Center);
		cellCount++;
		return cellCount;
	}

	/**
	 * Generate column percent.
	 *
	 * @param boldData11CenterStyle the bold data 11 center style
	 * @param format                the format
	 * @param cellCount             the cell count
	 * @param r                     the r
	 * @param range1                the range 1
	 * @param range2                the range 2
	 * @return the int
	 */
	private static int generateColumnPercent(CellStyle boldData11CenterStyle, XSSFDataFormat format, int cellCount, Row r, final String range1, final String range2) {
		Cell c;
		c = r.createCell(cellCount);
		c.setCellFormula("IF(" + range1 + "<>0," + range2 + "/" + range1 + ",0)");
		c.setCellStyle(boldPercent11CenterStyle);
		cellCount++;
		return cellCount;
	}

	/**
	 * Generate column count.
	 *
	 * @param columnResults         the column results
	 * @param resultColumnDependecy the result column dependecy
	 * @param boldData11CenterStyle the bold data 11 center style
	 * @param cellCount             the cell count
	 * @param r                     the r
	 * @param resultCount           the result count
	 * @return the int
	 */
	private static int generateColumnCount(final String[] columnResults, final String resultColumnDependecy, CellStyle boldData11CenterStyle, int cellCount, Row r, int resultCount) {
		Cell c;
		c = r.createCell(cellCount);
		c.setCellStyle(boldData11CenterStyle);
		c.setCellFormula("COUNTIFS(Resultados!$" + resultColumnDependecy + ":$" + resultColumnDependecy + ",\"*\"&B:B&\"*\",Resultados!$" + columnResults[resultCount] + ":$"
				+ columnResults[resultCount] + ",\">0\")");
		cellCount++;
		return cellCount;
	}

	/**
	 * Gets the values.
	 *
	 * @param column the column
	 * @param sheet  the sheet
	 * @return the values
	 */
	private static List<String> getValues(final String column, final XSSFSheet sheet) {
		List<String> listAll = new ArrayList<>();
		int row = 2;
		boolean stop = false;
		while (!stop) {
			CellReference ref = new CellReference(column + "" + row);
			Row r = sheet.getRow(ref.getRow());
			if (r == null) {
				stop = true;
			} else {
				Cell c = r.getCell(ref.getCol());
				final String stringCellValue = c.getStringCellValue();
				String[] splited = stringCellValue.split("\\n");
				for (int i = 0; i < splited.length; i++) {
					if (!listAll.contains(splited[i])) {
						listAll.add(splited[i]);
					}
				}
				row++;
			}
		}
		Collections.sort(listAll);
		return listAll;
	}

	/**
	 * Creates the annex criteria portal.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @throws Exception the exception
	 */
	public static void createAnnexPortalsCriteria(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter, final String[] exObsIds)
			throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo-portales-criterios.xml")) {
			generateXmlPortal(messageResources, idObsExecution, tagsToFilter, c, writer, false, true, true);
		} catch (Exception e) {
			Logger.putLog("Error al crear el XML de resultado portales", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the annex paginas.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @throws Exception the exception
	 */
	public static void createAnnexPaginas(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter, final String[] exObsIds)
			throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo-paginas.xml")) {
			generateXmlPages(messageResources, idObsExecution, tagsToFilter, c, writer, false, false);
		} catch (Exception e) {
			Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the annex paginas.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @throws Exception the exception
	 */
	public static void createAnnexPaginasVerifications(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter, final String[] exObsIds)
			throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo-paginas-verificaciones.xml")) {
			generateXmlPages(messageResources, idObsExecution, tagsToFilter, c, writer, true, false);
		} catch (Exception e) {
			Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the annex paginas criteria.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @throws Exception the exception
	 */
	public static void createAnnexPaginasCriteria(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter, final String[] exObsIds)
			throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo-paginas-criterios.xml")) {
			generateXmlPages(messageResources, idObsExecution, tagsToFilter, c, writer, false, true);
		} catch (Exception e) {
			Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Generate xml pages.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param tagsToFilter     the tags to filter
	 * @param c                the c
	 * @param writer           the writer
	 * @param verifications    the verifications
	 * @param criterias        the criterias
	 * @throws IOException  Signals that an I/O exception has occurred.
	 * @throws SAXException the SAX exception
	 * @throws Exception    the exception
	 * @throws SQLException the SQL exception
	 */
	private static void generateXmlPages(final MessageResources messageResources, final Long idObsExecution, final String[] tagsToFilter, Connection c, FileWriter writer, final boolean verifications,
			final boolean criterias) throws IOException, SAXException, Exception, SQLException {
		final ContentHandler hd = getContentHandler(writer);
		hd.startDocument();
		hd.startElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT, null);
		final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);
		for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
			if (categoryForm != null) {
				for (SiteForm siteForm : categoryForm.getSiteFormList()) {
					if (siteForm != null) {
						final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(siteForm.getIdCrawlerSeed()));
						// Filter by tags
						List<String> tagList = null;
						if (tagsToFilter != null) {
							tagList = Arrays.asList(tagsToFilter);
						}
						boolean hasTags = tagList != null ? false : true;
						if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty() && tagList != null) {
							for (EtiquetaForm tag : semillaForm.getEtiquetas()) {
								if (tagList.contains(String.valueOf(tag.getId()))) {
									hasTags = true;
									break;
								}
							}
						}
						if (hasTags) {
							hd.startElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT, null);
							writeTag(hd, Constants.XML_ID, String.valueOf(semillaForm.getId()));
							writeTag(hd, NOMBRE_ELEMENT, siteForm.getName());
							writeTag(hd, CATEGORIA_ELEMENT, semillaForm.getCategoria().getName());
							// Multidependencia
							StringBuilder dependencias = new StringBuilder();
							if (semillaForm.getDependencias() != null) {
								for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
									dependencias.append(semillaForm.getDependencias().get(i).getName());
									if (i < semillaForm.getDependencias().size() - 1) {
										dependencias.append("\n");
									}
								}
							}
							writeTag(hd, Constants.XML_AMBITO, semillaForm.getAmbito().getName());
							writeTag(hd, Constants.XML_COMPLEJIDAD, semillaForm.getComplejidad().getName());
							writeTag(hd, DEPENDE_DE_ELEMENT, dependencias.toString());
							writeTag(hd, "semilla", semillaForm.getListaUrls().get(0));
							// Seed tags
							List<EtiquetaForm> etiquetas = semillaForm.getEtiquetas();
							List<EtiquetaForm> tagsDistribucion = new ArrayList<>(); // id=2
							List<EtiquetaForm> tagsTematica = new ArrayList<>();// id=1
							List<EtiquetaForm> tagsRecurrencia = new ArrayList<>();// id=3
							List<EtiquetaForm> tagsOtros = new ArrayList<>();// id=4
							if (etiquetas != null && !etiquetas.isEmpty()) {
								for (EtiquetaForm tmp : etiquetas) {
									if (tmp.getClasificacion() != null) {
										switch (tmp.getClasificacion().getId()) {
										case "1":
											tagsTematica.add(tmp);
											break;
										case "2":
											tagsDistribucion.add(tmp);
											break;
										case "3":
											tagsRecurrencia.add(tmp);
											break;
										case "4":
											tagsOtros.add(tmp);
											break;
										default:
											break;
										}
									}
								}
							}
							// 1
							hd.startElement("", "", Constants.XML_ETIQUETAS_TEMATICA, null);
							if (!tagsTematica.isEmpty()) {
								for (int i = 0; i < tagsTematica.size(); i++) {
									hd.characters(tagsTematica.get(i).getName().toCharArray(), 0, tagsTematica.get(i).getName().length());
									if (i < tagsTematica.size() - 1) {
										hd.characters("\n".toCharArray(), 0, "\n".length());
									}
								}
							}
							hd.endElement("", "", Constants.XML_ETIQUETAS_TEMATICA);
							// 2
							hd.startElement("", "", Constants.XML_ETIQUETAS_DISTRIBUCCION, null);
							if (!tagsDistribucion.isEmpty()) {
								for (int i = 0; i < tagsDistribucion.size(); i++) {
									hd.characters(tagsDistribucion.get(i).getName().toCharArray(), 0, tagsDistribucion.get(i).getName().length());
									if (i < tagsDistribucion.size() - 1) {
										hd.characters("\n".toCharArray(), 0, "\n".length());
									}
								}
							}
							hd.endElement("", "", Constants.XML_ETIQUETAS_DISTRIBUCCION);
							// 3
							hd.startElement("", "", Constants.XML_ETIQUETAS_RECURRENCIA, null);
							if (!tagsRecurrencia.isEmpty()) {
								for (int i = 0; i < tagsRecurrencia.size(); i++) {
									hd.characters(tagsRecurrencia.get(i).getName().toCharArray(), 0, tagsRecurrencia.get(i).getName().length());
									if (i < tagsRecurrencia.size() - 1) {
										hd.characters("\n".toCharArray(), 0, "\n".length());
									}
								}
							}
							hd.endElement("", "", Constants.XML_ETIQUETAS_RECURRENCIA);
							// 4
							hd.startElement("", "", Constants.XML_ETIQUETAS_OTROS, null);
							if (!tagsOtros.isEmpty()) {
								for (int i = 0; i < tagsOtros.size(); i++) {
									hd.characters(tagsOtros.get(i).getName().toCharArray(), 0, tagsOtros.get(i).getName().length());
									if (i < tagsOtros.size() - 1) {
										hd.characters("\n".toCharArray(), 0, "\n".length());
									}
								}
							}
							hd.endElement("", "", Constants.XML_ETIQUETAS_OTROS);
							// Num crawls
							writeTag(hd, "paginas", String.valueOf(ObservatorioDAO.getNumCrawls(c, idObsExecution, semillaForm.getId())));
							hd.startElement(EMPTY_STRING, EMPTY_STRING, "paginas", null);
							Map<String, Map<String, ValidationDetails>> wcagCompliance = null;
							// Only generate this info once
							if (criterias) {
								final List<Long> analysisIdsByTracking = AnalisisDatos.getEvaluationIdsFromExecutedObservatoryAndIdSeed(idObsExecution, Long.valueOf(siteForm.getIdCrawlerSeed()));
								final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
								// This map store, the url and a map with everi wcag automatic validation an result
								wcagCompliance = WcagEmUtils.generateEquivalenceMap(currentEvaluationPageList);
							}
							for (PageForm pageForm : siteForm.getPageList()) {
								if (pageForm != null) {
									hd.startElement(EMPTY_STRING, EMPTY_STRING, "pagina", null);
									writeTag(hd, "url", pageForm.getUrl());
									writeTag(hd, "puntuacion", pageForm.getScore());
									writeTag(hd, "adecuacion", ObservatoryUtils.getValidationLevel(messageResources, pageForm.getLevel()));
									// OAW Verifications
									if (verifications) {
										ObservatoryEvaluationForm evaluationForm = currentEvaluationPageList.stream().filter(evaluation -> pageForm.getUrl().equals(evaluation.getUrl())).findAny()
												.orElse(null);
										if (evaluationForm != null) {
											for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(0).getSuitabilityGroups()) {
												int i = 1;
												for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
													writeTag(hd, "V_1_" + i, getModality(subgroupForm.getValue(), messageResources));
													i++;
												}
											}
											for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(1).getSuitabilityGroups()) {
												int i = 1;
												for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
													writeTag(hd, "V_2_" + i, getModality(subgroupForm.getValue(), messageResources));
													i++;
												}
											}
										}
									}
									// WCAG Criterias
									if (criterias) {
										Map<String, ValidationDetails> details = wcagCompliance.get(pageForm.getUrl());
										for (WcagEmPointKey wcagEmPoint : WcagEmPointKey.values()) {
											// do what you want
											String compliance = messageResources.getMessage("modality.pass");
											if (EARL_FAILED.equalsIgnoreCase(details.get(wcagEmPoint.getWcagEmId()).getResult())) {
												compliance = messageResources.getMessage("modality.fail");
											} else if (EARL_INAPPLICABLE.equalsIgnoreCase(details.get(wcagEmPoint.getWcagEmId()).getResult())) {
												compliance = messageResources.getMessage("resultados.anonimos.porc.portales.na");
											}
											writeTag(hd, "C_" + wcagEmPoint.getWcagPoint().replace(".", "_"), compliance);
										}
									}
									hd.endElement(EMPTY_STRING, EMPTY_STRING, "pagina");
								}
							}
							hd.endElement(EMPTY_STRING, EMPTY_STRING, "paginas");
							hd.endElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT);
						}
					}
				}
			}
		}
		hd.endElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT);
		hd.endDocument();
	}

	/**
	 * Gets the modality.
	 *
	 * @param result           the result
	 * @param messageResources the message resources
	 * @return the modality
	 */
	private static String getModality(final int result, MessageResources messageResources) {
		final String value;
		switch (result) {
		case Constants.OBS_VALUE_NOT_SCORE:
			value = "-P";
			break;
		case Constants.OBS_VALUE_RED_ZERO:
			value = "0F";
			break;
		case Constants.OBS_VALUE_GREEN_ZERO:
			value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero.pasa") + "P";
			break;
		case Constants.OBS_VALUE_GREEN_ONE:
			value = "1P";
			break;
		default:
			value = "-";
			break;
		}
		return value;
	}

	/**
	 * Creates the annex portales.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @throws Exception the exception
	 */
	public static void createAnnexPortales(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter, final String[] exObsIds)
			throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo-portales.xml")) {
			generateXmlPortal(messageResources, idObsExecution, tagsToFilter, c, writer, false, false, false);
		} catch (Exception e) {
			Logger.putLog("Error al crear el XML de resultado portales", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the annex portals verification.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @throws Exception the exception
	 */
	public static void createAnnexPortalsVerification(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter, final String[] exObsIds)
			throws Exception {
		try (Connection c = DataBaseManager.getConnection(); FileWriter writer = getFileWriter(idOperation, "anexo-portales-verificaciones.xml")) {
			generateXmlPortal(messageResources, idObsExecution, tagsToFilter, c, writer, true, true, false);
		} catch (Exception e) {
			Logger.putLog("Error al crear el XML de resultado portales", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Generate xml portal.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param tagsToFilter     the tags to filter
	 * @param c                the c
	 * @param writer           the writer
	 * @param verifications    the verifications
	 * @param onlyLast         the only last
	 * @param criterias        the criterias
	 * @throws SQLException the SQL exception
	 * @throws SAXException the SAX exception
	 * @throws IOException  Signals that an I/O exception has occurred.
	 */
	private static void generateXmlPortal(final MessageResources messageResources, final Long idObsExecution, final String[] tagsToFilter, Connection c, FileWriter writer, final boolean verifications,
			final boolean onlyLast, final boolean criterias) throws SQLException, SAXException, IOException {
		final ContentHandler hd = getContentHandler(writer);
		hd.startDocument();
		hd.startElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT, null);
		for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
			final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
			if (semillaForm.getId() != 0) {
				// Filter by tags
				List<String> tagList = null;
				if (tagsToFilter != null) {
					tagList = Arrays.asList(tagsToFilter);
				}
				boolean hasTags = tagList != null ? false : true;
				if (semillaForm.getEtiquetas() != null && !semillaForm.getEtiquetas().isEmpty() && tagList != null) {
					for (EtiquetaForm tag : semillaForm.getEtiquetas()) {
						if (tagList.contains(String.valueOf(tag.getId()))) {
							hasTags = true;
							break;
						}
					}
				}
				if (hasTags) {
					hd.startElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT, null);
					writeTag(hd, Constants.XML_ID, String.valueOf(semillaForm.getId()));
					writeTag(hd, NOMBRE_ELEMENT, semillaForm.getNombre());
					writeTag(hd, CATEGORIA_ELEMENT, semillaForm.getCategoria().getName());
					// Multidependencia
					StringBuilder dependencias = new StringBuilder();
					if (semillaForm.getDependencias() != null) {
						for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
							dependencias.append(semillaForm.getDependencias().get(i).getName());
							if (i < semillaForm.getDependencias().size() - 1) {
								dependencias.append("\n");
							}
						}
					}
					// ambit
					writeTag(hd, Constants.XML_AMBITO, semillaForm.getAmbito().getName());
					writeTag(hd, Constants.XML_COMPLEJIDAD, semillaForm.getComplejidad().getName());
					writeTag(hd, DEPENDE_DE_ELEMENT, dependencias.toString());
					writeTag(hd, "semilla", semillaForm.getListaUrls().get(0));
					// Seed tags
					List<EtiquetaForm> etiquetas = semillaForm.getEtiquetas();
					List<EtiquetaForm> tagsDistribucion = new ArrayList<>(); // id=2
					List<EtiquetaForm> tagsTematica = new ArrayList<>();// id=1
					List<EtiquetaForm> tagsRecurrencia = new ArrayList<>();// id=3
					List<EtiquetaForm> tagsOtros = new ArrayList<>();// id=4
					if (etiquetas != null && !etiquetas.isEmpty()) {
						for (EtiquetaForm tmp : etiquetas) {
							if (tmp.getClasificacion() != null) {
								switch (tmp.getClasificacion().getId()) {
								case "1":
									tagsTematica.add(tmp);
									break;
								case "2":
									tagsDistribucion.add(tmp);
									break;
								case "3":
									tagsRecurrencia.add(tmp);
									break;
								case "4":
									tagsOtros.add(tmp);
									break;
								default:
									break;
								}
							}
						}
					}
					// 1
					hd.startElement("", "", Constants.XML_ETIQUETAS_TEMATICA, null);
					if (!tagsTematica.isEmpty()) {
						for (int i = 0; i < tagsTematica.size(); i++) {
							hd.characters(tagsTematica.get(i).getName().toCharArray(), 0, tagsTematica.get(i).getName().length());
							if (i < tagsTematica.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_TEMATICA);
					// 2
					hd.startElement("", "", Constants.XML_ETIQUETAS_DISTRIBUCCION, null);
					if (!tagsDistribucion.isEmpty()) {
						for (int i = 0; i < tagsDistribucion.size(); i++) {
							hd.characters(tagsDistribucion.get(i).getName().toCharArray(), 0, tagsDistribucion.get(i).getName().length());
							if (i < tagsDistribucion.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_DISTRIBUCCION);
					// 3
					hd.startElement("", "", Constants.XML_ETIQUETAS_RECURRENCIA, null);
					if (!tagsRecurrencia.isEmpty()) {
						for (int i = 0; i < tagsRecurrencia.size(); i++) {
							hd.characters(tagsRecurrencia.get(i).getName().toCharArray(), 0, tagsRecurrencia.get(i).getName().length());
							if (i < tagsRecurrencia.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_RECURRENCIA);
					// 4
					hd.startElement("", "", Constants.XML_ETIQUETAS_OTROS, null);
					if (!tagsOtros.isEmpty()) {
						for (int i = 0; i < tagsOtros.size(); i++) {
							hd.characters(tagsOtros.get(i).getName().toCharArray(), 0, tagsOtros.get(i).getName().length());
							if (i < tagsOtros.size() - 1) {
								hd.characters("\n".toCharArray(), 0, "\n".length());
							}
						}
					}
					hd.endElement("", "", Constants.XML_ETIQUETAS_OTROS);
					// Num crawls
					writeTag(hd, "paginas", String.valueOf(ObservatorioDAO.getNumCrawls(c, idObsExecution, semillaForm.getId())));
					// Scores
					if (onlyLast) {
						Map.Entry<String, ScoreForm> entry = semillaEntry.getValue().lastEntry();
						final String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
						writeTag(hd, "puntuacion_" + executionDateAux, entry.getValue().getTotalScore().toString());
						writeTag(hd, "adecuacion_" + executionDateAux, changeLevelName(entry.getValue().getLevel(), messageResources));
						writeTag(hd, "cumplimiento_" + executionDateAux, entry.getValue().getCompliance());
					} else {
						for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
							final String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
							writeTag(hd, "puntuacion_" + executionDateAux, entry.getValue().getTotalScore().toString());
							writeTag(hd, "adecuacion_" + executionDateAux, changeLevelName(entry.getValue().getLevel(), messageResources));
							writeTag(hd, "cumplimiento_" + executionDateAux, entry.getValue().getCompliance());
						}
					}
					if (verifications) {
						Map.Entry<String, ScoreForm> entry = semillaEntry.getValue().lastEntry();
						for (VerificationScoreForm verification : entry.getValue().getVerificationScoreList()) {
							writeTag(hd, "V_" + verification.getVerification().replace(".", "_"), evaluateCompliance(verification.getScore()));
						}
					}
					if (criterias) {
						final List<Long> analysisIdsByTracking = AnalisisDatos.getEvaluationIdsFromExecutedObservatoryAndIdSeed(idObsExecution, semillaForm.getId());
						final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
						Map<String, Map<String, ValidationDetails>> wcagCompliance = WcagEmUtils.generateEquivalenceMap(currentEvaluationPageList);
						for (WcagEmPointKey wcagEmPointKey : WcagEmPointKey.values()) {
							// Iterate WCAG Points
							if (wcagCompliance != null && !wcagCompliance.isEmpty()) {
								// Iterate evl list to preserve order
								String compliance = messageResources.getMessage("observatory.graphic.compilance.green");
								for (ObservatoryEvaluationForm eval : currentEvaluationPageList) {
									Map<String, ValidationDetails> result = wcagCompliance.get(eval.getUrl());
									// if cointain current wcag rule
									if (result.containsKey(wcagEmPointKey.getWcagEmId())) {
										final String validationResult = result.get(wcagEmPointKey.getWcagEmId()).getResult();
										// if one of this has earl:failed, all result marked as failed
										// do what you want
										if (EARL_FAILED.equals(validationResult)) {
											compliance = messageResources.getMessage("observatory.graphic.compilance.red");
										} else if (EARL_INAPPLICABLE.equals(validationResult)) {
											compliance = messageResources.getMessage("observatory.graphic.compilance.gray");
										}
									}
								}
								writeTag(hd, "C_" + wcagEmPointKey.getWcagPoint().replace(".", "_"), compliance);
							}
						}
					}
					hd.endElement(EMPTY_STRING, EMPTY_STRING, PORTAL_ELEMENT);
				}
			}
		}
		hd.endElement(EMPTY_STRING, EMPTY_STRING, RESULTADOS_ELEMENT);
		hd.endDocument();
	}

	/**
	 * Evaluate compliance.
	 *
	 * @param value the value
	 * @return the string
	 */
	private static String evaluateCompliance(final LabelValueBean value) {
		try {
			BigDecimal bigDecimal = new BigDecimal(value.getValue());
			if (bigDecimal.compareTo(new BigDecimal(9)) >= 0) {
				return "C";
			} else if (bigDecimal.compareTo(new BigDecimal(0)) >= 0) {
				return "NC";
			} else {
				return "NA";
			}
		} catch (NumberFormatException e) {
			return "NA";
		}
	}

	/**
	 * Evaluate compliance.
	 *
	 * @param value the value
	 * @return the string
	 */
	private static String evaluateCompliance(final BigDecimal value) {
		if (value != null) {
			try {
				if (value.compareTo(new BigDecimal(9)) >= 0) {
					return "C";
				} else if (value.compareTo(new BigDecimal(0)) >= 0) {
					return "NC";
				} else {
					return "NA";
				}
			} catch (NumberFormatException e) {
				return "NA";
			}
		}
		return "NA";
	}

	/**
	 * Creates the XLSX annex.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @throws Exception the exception
	 */
	public static void createAnnexXLSX2(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
		ColumnNames = new ArrayList<>();
		try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "2. Iteración SW.xlsx")) {
			final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);
			final String ObservatoryFormDate = observatoryForm.getDate().substring(0, 10);
			final String[] ColumnNames = new String[] { "id", "nombre", "namecat", "ambito", "complejidad", "depende_de", "semilla", "tematica", "distribucion", "recurrencia", "otros", "paginas",
					"puntuacion_" + ObservatoryFormDate, "adecuacion_" + ObservatoryFormDate, "cumplimiento_" + ObservatoryFormDate, "NV_" + ObservatoryFormDate, "A_" + ObservatoryFormDate,
					"AA_" + ObservatoryFormDate, "NC_" + ObservatoryFormDate, "PC_" + ObservatoryFormDate, "TC_" + ObservatoryFormDate };
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Resultados");
			XSSFRow row;
			XSSFCell cell;
			int rowIndex = 0;
			int columnIndex = 0;
			// create header cell style
			CellStyle headerStyle = wb.createCellStyle();
			headerStyle.setWrapText(true);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// create light shadow cell style
			CellStyle shadowStyle = wb.createCellStyle();
			shadowStyle.setWrapText(true);
			shadowStyle.setAlignment(HorizontalAlignment.LEFT);
			shadowStyle.setVerticalAlignment(VerticalAlignment.TOP);
			shadowStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			shadowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// Add headers
			row = sheet.createRow(rowIndex);
			for (String name : ColumnNames) {
				cell = row.createCell(columnIndex);
				cell.setCellValue(name);
				cell.setCellStyle(headerStyle);
				columnIndex++;
			}
			// The sheet already has headers, so we start in the second row.
			rowIndex++;
			int categoryStarts;
			for (CategoryForm categoryForm : observatoryForm.getCategoryFormList()) {
				categoryStarts = rowIndex;
				if (categoryForm != null) {
					for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
						final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
						if (categoryForm.getName().equals(semillaForm.getCategoria().getName())) {
							// Multidependence
							StringBuilder dependencias = new StringBuilder();
							if (semillaForm.getDependencias() != null) {
								for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
									dependencias.append(semillaForm.getDependencias().get(i).getName());
									if (i < semillaForm.getDependencias().size() - 1) {
										dependencias.append("\n");
									}
								}
							}
							row = sheet.createRow(rowIndex);
							int excelRowNumber = rowIndex + 1;
							// "id"
							cell = row.createCell(0);
							cell.setCellValue(String.valueOf(semillaForm.getId()));
							cell.setCellStyle(shadowStyle);
							// "nombre"
							cell = row.createCell(1);
							cell.setCellValue(semillaForm.getNombre());
							cell.setCellStyle(shadowStyle);
							// "namecat"
							cell = row.createCell(2);
							cell.setCellValue(categoryForm.getName());
							cell.setCellStyle(shadowStyle);
							// "ambito"
							cell = row.createCell(3);
							cell.setCellValue(semillaForm.getAmbito().getName());
							cell.setCellStyle(shadowStyle);
							// "complejidad"
							cell = row.createCell(4);
							cell.setCellValue(semillaForm.getComplejidad().getName());
							cell.setCellStyle(shadowStyle);
							// "depende_de"
							cell = row.createCell(5);
							cell.setCellValue(dependencias.toString());
							cell.setCellStyle(shadowStyle);
							// "semilla"
							cell = row.createCell(6);
							cell.setCellValue(semillaForm.getListaUrls().get(0));
							cell.setCellStyle(shadowStyle);
							// Seed tags
							List<EtiquetaForm> etiquetas = semillaForm.getEtiquetas();
							List<EtiquetaForm> tagsDistribucion = new ArrayList<>(); // id=2
							List<EtiquetaForm> tagsTematica = new ArrayList<>();// id=1
							List<EtiquetaForm> tagsRecurrencia = new ArrayList<>();// id=3
							List<EtiquetaForm> tagsOtros = new ArrayList<>();// id=4
							if (etiquetas != null && !etiquetas.isEmpty()) {
								for (EtiquetaForm tmp : etiquetas) {
									if (tmp.getClasificacion() != null) {
										switch (tmp.getClasificacion().getId()) {
										case "1":
											tagsTematica.add(tmp);
											break;
										case "2":
											tagsDistribucion.add(tmp);
											break;
										case "3":
											tagsRecurrencia.add(tmp);
											break;
										case "4":
											tagsOtros.add(tmp);
											break;
										default:
											break;
										}
									}
								}
							}
							// "tematica"
							String dataToInsert = "";
							if (!tagsTematica.isEmpty()) {
								for (int i = 0; i < tagsTematica.size(); i++) {
									dataToInsert += tagsTematica.get(i).getName();
									if (i < tagsDistribucion.size() - 1) {
										dataToInsert += "\n";
									}
								}
							}
							cell = row.createCell(7);
							cell.setCellValue(dataToInsert);
							cell.setCellStyle(shadowStyle);
							// "distribucion"
							dataToInsert = "";
							if (!tagsDistribucion.isEmpty()) {
								for (int i = 0; i < tagsDistribucion.size(); i++) {
									dataToInsert += tagsDistribucion.get(i).getName();
									if (i < tagsDistribucion.size() - 1) {
										dataToInsert += "\n";
									}
								}
							}
							cell = row.createCell(8);
							cell.setCellValue(dataToInsert);
							cell.setCellStyle(shadowStyle);
							// "Recurrencia"
							dataToInsert = "";
							if (!tagsRecurrencia.isEmpty()) {
								for (int i = 0; i < tagsRecurrencia.size(); i++) {
									dataToInsert += tagsRecurrencia.get(i).getName();
									if (i < tagsRecurrencia.size() - 1) {
										dataToInsert += "\n";
									}
								}
							}
							cell = row.createCell(9);
							cell.setCellValue(dataToInsert);
							cell.setCellStyle(shadowStyle);
							// Otros
							dataToInsert = "";
							if (!tagsOtros.isEmpty()) {
								for (int i = 0; i < tagsOtros.size(); i++) {
									dataToInsert += tagsOtros.get(i).getName();
									if (i < tagsOtros.size() - 1) {
										dataToInsert += "\n";
									}
								}
							}
							cell = row.createCell(10);
							cell.setCellValue(dataToInsert);
							cell.setCellStyle(shadowStyle);
							// Páginas
							cell = row.createCell(11);
							cell.setCellValue(String.valueOf(ObservatorioDAO.getNumCrawls(c, idObsExecution, semillaForm.getId())));
							cell.setCellStyle(shadowStyle);
							Map.Entry<String, ScoreForm> entry = semillaEntry.getValue().lastEntry();
							// "puntuacion_" + date
							cell = row.createCell(12);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(Double.parseDouble(entry.getValue().getTotalScore().toString()));
							cell.setCellStyle(shadowStyle);
							// "adecuacion_" + date
							cell = row.createCell(13);
							cell.setCellValue(changeLevelName(entry.getValue().getLevel(), messageResources));
							cell.setCellStyle(shadowStyle);
							// "cumplimiento_" + date
							cell = row.createCell(14);
							cell.setCellValue(entry.getValue().getCompliance());
							cell.setCellStyle(shadowStyle);
							// "NV_" + date
							cell = row.createCell(15);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellFormula("IF($N" + excelRowNumber + "=\"No Válido\",$M" + excelRowNumber + ",0)");
							cell.setCellStyle(shadowStyle);
							// "A_" + date
							cell = row.createCell(16);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellFormula("IF($N" + excelRowNumber + "=\"A\",$M" + excelRowNumber + ",0)");
							cell.setCellStyle(shadowStyle);
							// "AA_" + date
							cell = row.createCell(17);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellFormula("IF($N" + excelRowNumber + "=\"AA\",$M" + excelRowNumber + ",0)");
							cell.setCellStyle(shadowStyle);
							// "NC_" + date
							cell = row.createCell(18);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellFormula("IF($O" + excelRowNumber + "=\"No conforme\",$M" + excelRowNumber + ",0)");
							cell.setCellStyle(shadowStyle);
							// "PC_" + date
							cell = row.createCell(19);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellFormula("IF($O" + excelRowNumber + "=\"Parcialmente conforme\",$M" + excelRowNumber + ",0)");
							cell.setCellStyle(shadowStyle);
							// "TC_" + date
							cell = row.createCell(20);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellFormula("IF($O" + excelRowNumber + "=\"Plenamente conforme\",$M" + excelRowNumber + ",0)");
							cell.setCellStyle(shadowStyle);
							rowIndex++;
						}
					}
					// Increase width of columns to match content
					for (int i = 0; i < ColumnNames.length; i++) {
						sheet.autoSizeColumn(i);
					}
					// Create graph into the Category sheet
					if (categoryForm.getSiteFormList().size() > 0) {
						/*
						 * Excel allows sheet names up to 31 chars in length but other applications (such as OpenOffice) allow more. Some versions of Excel crash with names longer than 31 chars,
						 * others - truncate such names to 31 character.
						 */
						String currentCategory = categoryForm.getName().substring(0, Math.min(categoryForm.getName().length(), 31));
						if (wb.getSheet(currentCategory) == null) {
							wb.createSheet(currentCategory);
							InsertGraphIntoSheetByCategory(wb, wb.getSheet(currentCategory), categoryStarts, rowIndex, true);
							InsertGraphIntoSheetByCategory(wb, wb.getSheet(currentCategory), categoryStarts, rowIndex, false);
						}
					}
				}
			}
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			wb.write(writer);
			wb.close();
		} catch (Exception e) {
			Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Creates the XLSX evolution annex.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param comparision      the comparision
	 * @param firstThreshold   the first threshold
	 * @param secondThreshold  the second threshold
	 * @throws Exception the exception
	 */
	public static void createAnnexXLSX1_Evolution(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final List<ComparisionForm> comparision,
			double firstThreshold, double secondThreshold) throws Exception {
		dependencies = new ArrayList<>();
		try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "1. Evolutivo SW.xlsx")) {
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet("Resultados");
			XSSFRow row;
			XSSFCell cell;
			final int numberOfFixedColumns = 12;
			int rowIndex = 0;
			int columnIndex = 0;
			executionDates = new ArrayList<>();
			excelLines = new HashMap<>();
			ExcelLine excelLine;
			// create header cell style
			CellStyle headerStyle = wb.createCellStyle();
			headerStyle.setWrapText(true);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// create light shadow cell style
			CellStyle shadowStyle = wb.createCellStyle();
			shadowStyle.setWrapText(true);
			shadowStyle.setAlignment(HorizontalAlignment.LEFT);
			shadowStyle.setVerticalAlignment(VerticalAlignment.TOP);
			shadowStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
			shadowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			// Add headers without values
			ColumnNames = new ArrayList<>();
			ColumnNames.add("id");
			ColumnNames.add("nombre");
			ColumnNames.add("namecat");
			ColumnNames.add("ambito");
			ColumnNames.add("complejidad");
			ColumnNames.add("depende_de");
			ColumnNames.add("semilla");
			ColumnNames.add("tematica");
			ColumnNames.add("distribucion");
			ColumnNames.add("recurrencia");
			ColumnNames.add("otros");
			ColumnNames.add("paginas");
			row = sheet.createRow(rowIndex);
			for (String name : ColumnNames) {
				cell = row.createCell(columnIndex);
				cell.setCellValue(name);
				cell.setCellStyle(headerStyle);
				columnIndex++;
			}
			rowIndex++;
			/*
			 * Category names list created by generation Evolution and reused generating PerDependency annex.
			 */
			List<String> categories = new ArrayList<>();
			for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
				final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
				String namecat = semillaForm.getCategoria().getName();
				if (semillaForm.getId() != 0) {
					if (!categories.contains(namecat))
						categories.add(namecat);
				}
			}
			// Sort all category names
			Collections.sort(categories);
			// Loop to insert fixed values
			for (String currentCategory : categories) {
				for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
					final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
					String namecat = semillaForm.getCategoria().getName();
					// On each category iteration we filter the other categories.
					if (semillaForm.getId() != 0 && namecat.equals(currentCategory)) {
						row = sheet.createRow(rowIndex);
						columnIndex = 0;
						excelLine = new ExcelLine();
						excelLine.setRowIndex(rowIndex);
						// "id"
						String id = String.valueOf(semillaForm.getId());
						cell = row.createCell(columnIndex++);
						cell.setCellValue(id);
						cell.setCellStyle(shadowStyle);
						excelLine.setId(id);
						// "nombre"
						String name = semillaForm.getNombre();
						cell = row.createCell(columnIndex++);
						cell.setCellValue(name);
						cell.setCellStyle(shadowStyle);
						excelLine.setNombre(name);
						// "namecat"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(namecat);
						cell.setCellStyle(shadowStyle);
						excelLine.setNamecat(namecat);
						// "ambito"
						String ambito = semillaForm.getAmbito().getName();
						cell = row.createCell(columnIndex++);
						cell.setCellValue(ambito);
						cell.setCellStyle(shadowStyle);
						excelLine.setAmbito(ambito);
						// "complejidad"
						String compl = semillaForm.getComplejidad().getName();
						cell = row.createCell(columnIndex++);
						cell.setCellValue(compl);
						cell.setCellStyle(shadowStyle);
						excelLine.setComplejidad(compl);
						// "depende_de"
						// Multidependencia
						StringBuilder dependencias = new StringBuilder();
						if (semillaForm.getDependencias() != null) {
							for (int i = 0; i < semillaForm.getDependencias().size(); i++) {
								// Store all dependencies globally (we will use it in other files generation
								if (!dependencies.contains(semillaForm.getDependencias().get(i).getName()))
									dependencies.add(semillaForm.getDependencias().get(i).getName());
								dependencias.append(semillaForm.getDependencias().get(i).getName());
								if (i < semillaForm.getDependencias().size() - 1) {
									dependencias.append("\n");
								}
							}
						}
						cell = row.createCell(columnIndex++);
						cell.setCellValue(dependencias.toString());
						cell.setCellStyle(shadowStyle);
						excelLine.setDepende_de(dependencias.toString());
						// "semilla"
						String semilla = semillaForm.getListaUrls().get(0);
						cell = row.createCell(columnIndex++);
						cell.setCellValue(semilla);
						cell.setCellStyle(shadowStyle);
						excelLine.setSemilla(semilla);
						// Seed tags
						List<EtiquetaForm> etiquetas = semillaForm.getEtiquetas();
						List<EtiquetaForm> tagsDistribucion = new ArrayList<>(); // id=2
						List<EtiquetaForm> tagsTematica = new ArrayList<>();// id=1
						List<EtiquetaForm> tagsRecurrencia = new ArrayList<>();// id=3
						List<EtiquetaForm> tagsOtros = new ArrayList<>();// id=4
						if (etiquetas != null && !etiquetas.isEmpty()) {
							for (EtiquetaForm tmp : etiquetas) {
								if (tmp.getClasificacion() != null) {
									switch (tmp.getClasificacion().getId()) {
									case "1":
										tagsTematica.add(tmp);
										break;
									case "2":
										tagsDistribucion.add(tmp);
										break;
									case "3":
										tagsRecurrencia.add(tmp);
										break;
									case "4":
										tagsOtros.add(tmp);
										break;
									default:
										break;
									}
								}
							}
						}
						// "tematica"
						String dataToInsert = "";
						if (!tagsTematica.isEmpty()) {
							for (int i = 0; i < tagsTematica.size(); i++) {
								dataToInsert += tagsTematica.get(i).getName();
								if (i < tagsDistribucion.size() - 1) {
									dataToInsert += "\n";
								}
							}
						}
						cell = row.createCell(columnIndex++);
						cell.setCellValue(dataToInsert);
						cell.setCellStyle(shadowStyle);
						excelLine.setTematica(dataToInsert);
						// "distribucion"
						dataToInsert = "";
						if (!tagsDistribucion.isEmpty()) {
							for (int i = 0; i < tagsDistribucion.size(); i++) {
								dataToInsert += tagsDistribucion.get(i).getName();
								if (i < tagsDistribucion.size() - 1) {
									dataToInsert += "\n";
								}
							}
						}
						cell = row.createCell(columnIndex++);
						cell.setCellValue(dataToInsert);
						cell.setCellStyle(shadowStyle);
						excelLine.setDistribucion(dataToInsert);
						// "Recurrencia"
						dataToInsert = "";
						if (!tagsRecurrencia.isEmpty()) {
							for (int i = 0; i < tagsRecurrencia.size(); i++) {
								dataToInsert += tagsRecurrencia.get(i).getName();
								if (i < tagsRecurrencia.size() - 1) {
									dataToInsert += "\n";
								}
							}
						}
						cell = row.createCell(columnIndex++);
						cell.setCellValue(dataToInsert);
						cell.setCellStyle(shadowStyle);
						excelLine.setRecurrencia(dataToInsert);
						// Otros
						dataToInsert = "";
						if (!tagsOtros.isEmpty()) {
							for (int i = 0; i < tagsOtros.size(); i++) {
								dataToInsert += tagsOtros.get(i).getName();
								if (i < tagsOtros.size() - 1) {
									dataToInsert += "\n";
								}
							}
						}
						cell = row.createCell(columnIndex++);
						cell.setCellStyle(shadowStyle);
						excelLine.setOtros(dataToInsert);
						// Páginas
						String pages = String.valueOf(ObservatorioDAO.getNumCrawls(c, idObsExecution, semillaForm.getId()));
						cell = row.createCell(columnIndex++);
						cell.setCellValue(pages);
						cell.setCellStyle(shadowStyle);
						excelLine.setPaginas(pages);
						// ***************************
						// * EXECUTION VALUES *
						// ***************************
						executionDatesWithFormat = new ArrayList<>();
						// Get all execution dates in DateTime format
						for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
							executionDatesWithFormat.add(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(entry.getKey().substring(0, 19)));
						}
						for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
							// If there is an newer execution the same day, then ignore current one.
							boolean newerFound = false;
							Date currentExecutionDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(entry.getKey().substring(0, 19));
							for (Date d : executionDatesWithFormat) {
								if (d.getDay() == currentExecutionDate.getDay() && d.getMonth() == currentExecutionDate.getMonth() && d.getYear() == currentExecutionDate.getYear()) {
									if (d.getHours() > currentExecutionDate.getHours() || (d.getHours() == currentExecutionDate.getHours() && d.getMinutes() > currentExecutionDate.getMinutes())
											|| (d.getHours() == currentExecutionDate.getHours() && d.getMinutes() == currentExecutionDate.getMinutes()
													&& d.getSeconds() > currentExecutionDate.getSeconds())) {
										newerFound = true;
										break;
									}
								}
							}
							if (newerFound) {
								continue;
							}
							final String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
							if (!executionDates.contains(executionDateAux)) {
								executionDates.add(executionDateAux);
								executionDatesWithFormat_Valid.add(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(entry.getKey().substring(0, 19)));
							}
							double score = Double.parseDouble(entry.getValue().getTotalScore().toString());
							String adequacy = changeLevelName(entry.getValue().getLevel(), messageResources);
							String compliance = entry.getValue().getCompliance();
							ExcelExecution execution = new ExcelExecution();
							execution.setDate(executionDateAux);
							execution.setScore(score);
							execution.setAdequacy(adequacy);
							execution.setCompliance(compliance);
							excelLine.addExecution(execution);
							// PUNTUACIÓN
							// Add header if it is not already created
							if (!ColumnNames.contains("puntuacion_" + executionDateAux)) {
								ColumnNames.add("puntuacion_" + executionDateAux);
								XSSFRow headerRow = sheet.getRow(0);
								XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
								cellInHeader.setCellValue("puntuacion_" + executionDateAux);
								cellInHeader.setCellStyle(headerStyle);
							}
							cell = row.createCell(columnIndex++);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(score);
							cell.setCellStyle(shadowStyle);
							// ADECUACIÓN
							// Add header if it is not already created
							if (!ColumnNames.contains("adecuacion_" + executionDateAux)) {
								ColumnNames.add("adecuacion_" + executionDateAux);
								XSSFRow headerRow = sheet.getRow(0);
								XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
								cellInHeader.setCellValue("adecuacion_" + executionDateAux);
								cellInHeader.setCellStyle(headerStyle);
							}
							cell = row.createCell(columnIndex++);
							cell.setCellValue(adequacy);
							cell.setCellStyle(shadowStyle);
							// CUMPLIMIENTO
							// Add header if it is not already created
							if (!ColumnNames.contains("cumplimiento_" + executionDateAux)) {
								ColumnNames.add("cumplimiento_" + executionDateAux);
								XSSFRow headerRow = sheet.getRow(0);
								XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
								cellInHeader.setCellValue("cumplimiento_" + executionDateAux);
								cellInHeader.setCellStyle(headerStyle);
							}
							cell = row.createCell(columnIndex++);
							cell.setCellValue(compliance);
							cell.setCellStyle(shadowStyle);
						}
						excelLines.put(rowIndex, excelLine);
						rowIndex++;
					}
				}
			}
			// Loop to insert executions values.
			rowIndex = 0;
			for (String currentCategory : categories) {
				for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
					final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
					// On each category iteration we filter the other categories.
					if (semillaForm.getId() != 0 && semillaForm.getCategoria().getName().equals(currentCategory)) {
						rowIndex++;
						int numberOfDate = 0;
						for (Map.Entry<String, ScoreForm> entry : semillaEntry.getValue().entrySet()) {
							final String date = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
							// Previously we ignore the minor date of the day when there is a day with more than one executions.
							// Now we also ignore it to keep coherence.
							if (executionDatesWithFormat_Valid.contains(new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(entry.getKey().substring(0, 19)))) {
								row = sheet.getRow(rowIndex);
								String columnFirstLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 2) + (3 * executionDates.indexOf(date)));
								String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 1) + (3 * executionDates.indexOf(date)));
								// "NV_" + date
								// Add header if it is not already created
								if (!ColumnNames.contains("NV_" + date)) {
									ColumnNames.add("NV_" + date);
									XSSFRow headerRow = sheet.getRow(0);
									XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
									cellInHeader.setCellValue("NV_" + date);
									cellInHeader.setCellStyle(headerStyle);
								}
								cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate));
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"No Válido\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "A_" + date
								// Add header if it is not already created
								if (!ColumnNames.contains("A_" + date)) {
									ColumnNames.add("A_" + date);
									XSSFRow headerRow = sheet.getRow(0);
									XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
									cellInHeader.setCellValue("A_" + date);
									cellInHeader.setCellStyle(headerStyle);
								}
								cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 1);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"A\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "AA_" + date
								// Add header if it is not already created
								if (!ColumnNames.contains("AA_" + date)) {
									ColumnNames.add("AA_" + date);
									XSSFRow headerRow = sheet.getRow(0);
									XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
									cellInHeader.setCellValue("AA_" + date);
									cellInHeader.setCellStyle(headerStyle);
								}
								cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 2);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"AA\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								columnFirstLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 3) + (3 * executionDates.indexOf(date)));
								// "NC_" + date
								// Add header if it is not already created
								if (!ColumnNames.contains("NC_" + date)) {
									ColumnNames.add("NC_" + date);
									XSSFRow headerRow = sheet.getRow(0);
									XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
									cellInHeader.setCellValue("NC_" + date);
									cellInHeader.setCellStyle(headerStyle);
								}
								cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 3);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"No conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "PC_" + date
								// Add header if it is not already created
								if (!ColumnNames.contains("PC_" + date)) {
									ColumnNames.add("PC_" + date);
									XSSFRow headerRow = sheet.getRow(0);
									XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
									cellInHeader.setCellValue("PC_" + date);
									cellInHeader.setCellStyle(headerStyle);
								}
								cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 4);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"Parcialmente conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "TC_" + date
								// Add header if it is not already created
								if (!ColumnNames.contains("TC_" + date)) {
									ColumnNames.add("TC_" + date);
									XSSFRow headerRow = sheet.getRow(0);
									XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
									cellInHeader.setCellValue("TC_" + date);
									cellInHeader.setCellStyle(headerStyle);
								}
								cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 5);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"Plenamente conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								numberOfDate++;
							}
						}
					}
				}
			}
			// Loop to insert puntuation evolution compare with previous.
			// To select comparision column in comparision object, check if seed has tagId of comparision to select column by date
			ColumnNames.add("evol_puntuacion_ant");
			XSSFRow headerRow = sheet.getRow(0);
			XSSFCell cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
			cellInHeader.setCellValue("evol_puntuacion_ant");
			cellInHeader.setCellStyle(headerStyle);
			rowIndex = 1;
			for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
				final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
				// On each category iteration we filter the other categories.
				if (semillaForm.getId() != 0) {
					row = sheet.getRow(rowIndex);
					if (row != null) {
						// Discard rows without the last execution
						XSSFCell tmpCell = row.getCell(ColumnNames.size() - 3);
						if (tmpCell != null && !tmpCell.getCellFormula().equals("")) {
							List<EtiquetaForm> tags = semillaForm.getEtiquetas();
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "puntuacion", false);
							String columnSecondLetter = GetExcelColumnNameForNumber(numberOfFixedColumns + 1 + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.size() - 1);
							String formula = "IF(" + columnSecondLetter + ":" + columnSecondLetter + "=\"\",\"\",IF((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":"
									+ columnFirstLetter + ")<=-" + secondThreshold + ",\"EMPEORA MUCHO\",IF(AND((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":"
									+ columnFirstLetter + ")>-" + secondThreshold + ",(" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<-"
									+ firstThreshold + "),\"EMPEORA\",IF(AND((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")>-"
									+ firstThreshold + ",(" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<" + firstThreshold
									+ "),\"SE MANTIENE\",IF(AND((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")>" + firstThreshold + ",("
									+ columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<" + secondThreshold + "),\"MEJORA\",\"MEJORA MUCHO\")))))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
					}
				}
				rowIndex++;
			}
			// Loop to insert adecuation evolution compare with previous
			ColumnNames.add("evol_adecuacion_ant");
			headerRow = sheet.getRow(0);
			cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
			cellInHeader.setCellValue("evol_adecuacion_ant");
			cellInHeader.setCellStyle(headerStyle);
			rowIndex = 1;
			for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
				final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
				// On each category iteration we filter the other categories.
				if (semillaForm.getId() != 0) {
					row = sheet.getRow(rowIndex);
					if (row != null) {
						// Discard rows without the last execution
						XSSFCell tmpCell = row.getCell(ColumnNames.size() - 3);
						if (tmpCell != null && !tmpCell.getCellFormula().equals("")) {
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "adecuacion", false);
							String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 2) + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.size() - 1);
							String formula = "IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"No Válido\",0,IF($" + columnSecondLetter + "$2:$"
									+ columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Prioridad 1\",1,3))-IF($" + columnFirstLetter + "$2:$" + columnFirstLetter
									+ "$419=\"No Válido\",0,IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Prioridad 1\",1,3))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
					}
				}
				rowIndex++;
			}
			// Loop to insert puntuation evolution compare with first
			// To select comparision column in comparision object, check if seed has tagId of comparision to select column by date
			ColumnNames.add("evol_puntuacion_primer");
			headerRow = sheet.getRow(0);
			cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
			cellInHeader.setCellValue("evol_puntuacion_primer");
			cellInHeader.setCellStyle(headerStyle);
			rowIndex = 1;
			for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
				final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
				// On each category iteration we filter the other categories.
				if (semillaForm.getId() != 0) {
					row = sheet.getRow(rowIndex);
					if (row != null) {
						// Discard rows without the last execution
						XSSFCell tmpCell = row.getCell(ColumnNames.size() - 3);
						if (tmpCell != null && !tmpCell.getCellFormula().equals("")) {
							List<EtiquetaForm> tags = semillaForm.getEtiquetas();
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "puntuacion", true);
							String columnSecondLetter = GetExcelColumnNameForNumber(numberOfFixedColumns + 1 + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.size() - 1);
							String formula = "IF(" + columnSecondLetter + ":" + columnSecondLetter + "=\"\",\"\",IF((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":"
									+ columnFirstLetter + ")<=-" + secondThreshold + ",\"EMPEORA MUCHO\",IF(AND((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":"
									+ columnFirstLetter + ")>-" + secondThreshold + ",(" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<-"
									+ firstThreshold + "),\"EMPEORA\",IF(AND((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")>-"
									+ firstThreshold + ",(" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<" + firstThreshold
									+ "),\"SE MANTIENE\",IF(AND((" + columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")>" + firstThreshold + ",("
									+ columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter + ")<" + secondThreshold + "),\"MEJORA\",\"MEJORA MUCHO\")))))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
					}
				}
				rowIndex++;
			}
			// Loop to insert adecuation evolution compare with first .
			ColumnNames.add("evol_adecuacion_ant");
			headerRow = sheet.getRow(0);
			cellInHeader = headerRow.createCell(ColumnNames.size() - 1);
			cellInHeader.setCellValue("evol_adecuacion_ant");
			cellInHeader.setCellStyle(headerStyle);
			rowIndex = 1;
			for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
				final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
				// On each category iteration we filter the other categories.
				if (semillaForm.getId() != 0) {
					row = sheet.getRow(rowIndex);
					if (row != null) {
						// Discard rows without the last execution
						XSSFCell tmpCell = row.getCell(ColumnNames.size() - 3);
						if (tmpCell != null && !tmpCell.getCellFormula().equals("")) {
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "adecuacion", true);
							String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 2) + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.size() - 1);
							String formula = "IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"No Válido\",0,IF($" + columnSecondLetter + "$2:$"
									+ columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Prioridad 1\",1,3))-IF($" + columnFirstLetter + "$2:$" + columnFirstLetter
									+ "$419=\"No Válido\",0,IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Prioridad 1\",1,3))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
					}
				}
				rowIndex++;
			}
			int nextStartPos = InsertSummaryTable(sheet, rowIndex + 5, ColumnNames, headerStyle, shadowStyle);
			String title = "Datos de evolución anterior iteración de portales por segmento. Diferencias de " + firstThreshold + " y " + secondThreshold + " puntos.";
			nextStartPos = InsertCategoriesTable(sheet, nextStartPos + 5, categories, headerStyle, shadowStyle, rowIndex, ColumnNames.size() - 3, title);
			title = "Datos de evolución primera iteración de portales por segmento. Diferencias de " + firstThreshold + " y " + secondThreshold + " puntos.";
			nextStartPos = InsertCategoriesTable(sheet, nextStartPos + 5, categories, headerStyle, shadowStyle, rowIndex, ColumnNames.size() - 3, title);
			// Insert graph sheets per category
			for (String category : categories) {
				/*
				 * Excel allows sheet names up to 31 chars in length but other applications (such as OpenOffice) allow more. Some versions of Excel crash with names longer than 31 chars, others -
				 * truncate such names to 31 character.
				 */
				String categorySheetName = category.substring(0, Math.min(category.length(), 31));
				// Search category initial and final row.
				int categoryFirstRow = 0;
				int categoryLastRow = 0;
				for (int i = 0; i < rowIndex; i++) {
					row = sheet.getRow(i);
					if (row != null) {
						cell = row.getCell(2);
						if (categoryFirstRow == 0 && cell.getStringCellValue().equals(category))
							categoryFirstRow = i;
						if (categoryLastRow == 0 && !cell.getStringCellValue().equals(category) && categoryFirstRow != 0)
							categoryLastRow = i;
					}
				}
				if (categories.indexOf(category) == categories.size() - 1)
					categoryLastRow = rowIndex;
				if (wb.getSheet(categorySheetName) == null && categoryFirstRow != 0 && categoryLastRow != 0) {
					wb.createSheet(categorySheetName);
					XSSFSheet currentSheet = wb.getSheet(categorySheetName);
					InsertGraphIntoSheetByEvolution(wb, currentSheet, categoryFirstRow, categoryLastRow, true, numberOfFixedColumns);
					InsertGraphIntoSheetByEvolution(wb, currentSheet, categoryFirstRow, categoryLastRow, false, numberOfFixedColumns);
				}
			}
			// Increase width of columns to match content
			for (int i = 0; i < ColumnNames.size(); i++) {
				sheet.autoSizeColumn(i);
			}
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			wb.write(writer);
			wb.close();
		} catch (Exception e) {
			Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the first letter previous execution.
	 *
	 * @param comparision      the comparision
	 * @param labels           the labels
	 * @param columnNames      the column names
	 * @param columnName       the column name
	 * @param compareWithFirst the compare with first
	 * @return the string
	 */
	private static String GetFirstLetterPreviousExecution(List<ComparisionForm> comparision, List<EtiquetaForm> labels, List<String> columnNames, String columnName, boolean compareWithFirst) {
		String previousDate = "";
		String columnToReturn = "";
		if (comparision != null) {
			// Get previous date by tag
			for (ComparisionForm com : comparision) {
				for (EtiquetaForm label : labels) {
					if (com.getIdTag() == label.getId()) {
						previousDate = compareWithFirst ? com.getFirst() : com.getPrevious();
						break;
					}
				}
				if (previousDate != "") {
					int i = 1;
					for (String s : columnNames) {
						if (s.contains(columnName) && s.contains(previousDate)) {
							columnToReturn = GetExcelColumnNameForNumber(i);
							break;
						}
						i++;
					}
					break;
				}
			}
		}
		// When we can't find the previous by tag, then we search for the Fisrt or penultimate execution.
		if (columnToReturn == "") {
			boolean lastFound = false;
			int last = 1;
			for (int i = columnNames.size() - 1; i >= 10; i--) {
				if (columnNames.get(i).contains(columnName) && !columnNames.get(i).contains("evol")) {
					if (!lastFound) {
						last = i;
						lastFound = true;
					} else {
						columnToReturn = GetExcelColumnNameForNumber(i + 1);
						if (!compareWithFirst) {
							break;
						}
					}
				}
			}
			// This code cover the case of only one execution.
			if (columnToReturn == "") {
				columnToReturn = GetExcelColumnNameForNumber(last + 1);
				;
			}
		}
		return columnToReturn;
	}

	/**
	 * Creates the XLSX evolution annex per dependency. NEEDS THE EXECUTION OF createAnnexXLSX_Evolution METHOD PREVIOULY TO CONSTRUCT THE DATA DICTIONARY FROM DATABASE INFO.
	 *
	 * @param idOperation the id operation
	 * @throws Exception the exception
	 */
	public static void createAnnexXLSX_PerDependency(final Long idOperation) throws Exception {
		final int numberOfFixedColumns = 12;
		// Iterate through dependencies to create each file
		for (String currentDependency : dependencies) {
			try (FileOutputStream writer = getFileOutputStream(idOperation, "/Dependencias/" + currentDependency + ".xlsx")) {
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet("Resultados");
				XSSFRow row;
				XSSFCell cell;
				int rowIndex = 0;
				int columnIndex = 0;
				// create header cell style
				CellStyle headerStyle = wb.createCellStyle();
				headerStyle.setWrapText(true);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
				headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				// create light shadow cell style
				CellStyle shadowStyle = wb.createCellStyle();
				shadowStyle.setWrapText(true);
				shadowStyle.setAlignment(HorizontalAlignment.LEFT);
				shadowStyle.setVerticalAlignment(VerticalAlignment.TOP);
				shadowStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
				shadowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				row = sheet.createRow(rowIndex);
				for (int i = 0; i < ColumnNames.size() - 2; i++) {
					cell = row.createCell(columnIndex);
					cell.setCellValue(ColumnNames.get(i));
					cell.setCellStyle(headerStyle);
					columnIndex++;
				}
				rowIndex++;
				for (Map.Entry<Integer, ExcelLine> currentLine : excelLines.entrySet()) {
					// On each dependency iteration we filter other dependencies.
					if (currentLine.getValue().getDepende_de().contains(currentDependency)) {
						row = sheet.createRow(rowIndex);
						columnIndex = 0;
						// "id"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getId());
						cell.setCellStyle(shadowStyle);
						// "nombre"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getNombre());
						cell.setCellStyle(shadowStyle);
						// "namecat"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getNamecat());
						cell.setCellStyle(shadowStyle);
						// "ambito"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getAmbito());
						cell.setCellStyle(shadowStyle);
						// "complejidad"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getComplejidad());
						cell.setCellStyle(shadowStyle);
						// "depende_de"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getDepende_de());
						cell.setCellStyle(shadowStyle);
						// "semilla"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getSemilla());
						cell.setCellStyle(shadowStyle);
						// "tematica"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getTematica());
						cell.setCellStyle(shadowStyle);
						// "semilla"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getSemilla());
						cell.setCellStyle(shadowStyle);
						// "distribucion"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getDistribucion());
						cell.setCellStyle(shadowStyle);
						// "recurrencia"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getRecurrencia());
						cell.setCellStyle(shadowStyle);
						// "otros"
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getOtros());
						cell.setCellStyle(shadowStyle);
						for (String date : executionDates) {
							// Puntuación
							cell = row.createCell(columnIndex++);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellStyle(shadowStyle);
							if (currentLine.getValue().HasDate(date)) {
								cell.setCellValue(currentLine.getValue().GetExecutionByDate(date).getScore());
							}
							// Adecuación
							cell = row.createCell(columnIndex++);
							cell.setCellStyle(shadowStyle);
							if (currentLine.getValue().HasDate(date)) {
								cell.setCellValue(currentLine.getValue().GetExecutionByDate(date).getAdequacy());
							}
							// Cumplimiento
							cell = row.createCell(columnIndex++);
							cell.setCellStyle(shadowStyle);
							if (currentLine.getValue().HasDate(date)) {
								cell.setCellValue(currentLine.getValue().GetExecutionByDate(date).getCompliance());
							}
						}
						rowIndex++;
					}
				}
				// Insert NV, A and AA columns for each execution.
				for (int i = 1; i < rowIndex; i++) {
					row = sheet.getRow(i);
					for (int numberOfDate = 0; numberOfDate < executionDates.size(); numberOfDate++) {
						String columnFirstLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 2) + (3 * numberOfDate));
						String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 1) + (3 * numberOfDate));
						// "NV_" + date
						cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate));
						cell.setCellType(CellType.NUMERIC);
						cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"No Válido\",$" + columnSecondLetter + (i + 1) + ",0)");
						cell.setCellStyle(shadowStyle);
						// "A_" + date
						cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 1);
						cell.setCellType(CellType.NUMERIC);
						cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"A\",$" + columnSecondLetter + (i + 1) + ",0)");
						cell.setCellStyle(shadowStyle);
						// "AA_" + date
						cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 2);
						cell.setCellType(CellType.NUMERIC);
						cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"AA\",$" + columnSecondLetter + (i + 1) + ",0)");
						cell.setCellStyle(shadowStyle);
						columnFirstLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 2) + (3 * numberOfDate) + 1);
						// "NC_" + date
						cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 3);
						cell.setCellType(CellType.NUMERIC);
						cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"No conforme\",$" + columnSecondLetter + (i + 1) + ",0)");
						cell.setCellStyle(shadowStyle);
						// "PC_" + date
						cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 4);
						cell.setCellType(CellType.NUMERIC);
						cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"Parcialmente conforme\",$" + columnSecondLetter + (i + 1) + ",0)");
						cell.setCellStyle(shadowStyle);
						// "PC_" + date
						cell = row.createCell(numberOfFixedColumns + (3 * executionDates.size()) + (6 * numberOfDate) + 5);
						cell.setCellType(CellType.NUMERIC);
						cell.setCellFormula("IF($" + columnFirstLetter + (i + 1) + "=\"Plenamente conforme\",$" + columnSecondLetter + (i + 1) + ",0)");
						cell.setCellStyle(shadowStyle);
					}
				}
				// Increase width of columns to match content
				for (int i = 0; i < ColumnNames.size(); i++) {
					sheet.autoSizeColumn(i);
				}
				XSSFSheet currentSheet = wb.createSheet("Evolución SW");
				XSSFSheet currentSheet2 = wb.createSheet("Iteración SW");
				XSSFSheet currentSheet3 = wb.createSheet("Evolución Agregada");
				if (rowIndex > 1) {
					InsertGraphIntoSheetByDependency(currentSheet, rowIndex, true, numberOfFixedColumns, false);
					InsertGraphIntoSheetByDependency(currentSheet, rowIndex, false, numberOfFixedColumns, false);
					InsertGraphIntoSheetByDependency(currentSheet2, rowIndex, true, numberOfFixedColumns, true);
					InsertGraphIntoSheetByDependency(currentSheet2, rowIndex, false, numberOfFixedColumns, true);
					InsertAgregatePieChar(currentSheet3, rowIndex, ColumnNames, headerStyle, shadowStyle);
				}
				XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
				wb.write(writer);
				wb.close();
			} catch (Exception e) {
				Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
	}

	/**
	 * Insert agregate pie char.
	 *
	 * @param currentSheet3 the current sheet 3
	 * @param rowIndex      the row index
	 * @param columnNames   the column names
	 * @param headerStyle   the header style
	 * @param shadowStyle   the shadow style
	 */
	private static void InsertAgregatePieChar(XSSFSheet currentSheet3, int rowIndex, List<String> columnNames, CellStyle headerStyle, CellStyle shadowStyle) {
		CreationHelper createHelper = currentSheet3.getWorkbook().getCreationHelper();
		CellStyle percentCenterStyle = currentSheet3.getWorkbook().createCellStyle();
		percentCenterStyle.setWrapText(true);
		percentCenterStyle.setAlignment(HorizontalAlignment.CENTER);
		percentCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		percentCenterStyle.setDataFormat(createHelper.createDataFormat().getFormat(PERCENT_FORMAT));
		int adecuationColumn = 0;
		for (int i = columnNames.size() - 1; i > 5; i--) {
			if (columnNames.get(i).contains("adecuacion") && !columnNames.get(i).contains("ant")) {
				adecuationColumn = i + 1;
			}
		}
		// ADECUACY
		// "Headers"
		XSSFRow row = currentSheet3.createRow(0);
		XSSFCell cell = row.createCell(1);
		cell.setCellValue("Número de páginas.");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(2);
		cell.setCellValue("Porcentaje sobre el total.");
		cell.setCellStyle(headerStyle);
		// "AA"
		row = currentSheet3.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("AA");
		cell.setCellStyle(headerStyle);
		// Number of AA
		cell = row.createCell(1);
		cell.setCellFormula("COUNTIF(Resultados!" + GetExcelColumnNameForNumber(adecuationColumn) + "2:" + GetExcelColumnNameForNumber(adecuationColumn) + rowIndex + ",\"AA\")");
		// Percent of AA
		cell = row.createCell(2);
		cell.setCellFormula("B2/" + (rowIndex - 1));
		cell.setCellStyle(percentCenterStyle);
		// "A"
		row = currentSheet3.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue("A");
		cell.setCellStyle(headerStyle);
		// Number of A
		cell = row.createCell(1);
		cell.setCellFormula("COUNTIF(Resultados!" + GetExcelColumnNameForNumber(adecuationColumn) + "2:" + GetExcelColumnNameForNumber(adecuationColumn) + rowIndex + ",\"A\")");
		// Percent of A
		cell = row.createCell(2);
		cell.setCellFormula("B3/" + (rowIndex - 1));
		cell.setCellStyle(percentCenterStyle);
		// "No Válido"
		row = currentSheet3.createRow(3);
		cell = row.createCell(0);
		cell.setCellValue("No Válido");
		cell.setCellStyle(headerStyle);
		// Number of No Válido
		cell = row.createCell(1);
		cell.setCellFormula("COUNTIF(Resultados!" + GetExcelColumnNameForNumber(adecuationColumn) + "2:" + GetExcelColumnNameForNumber(adecuationColumn) + rowIndex + ",\"No Válido\")");
		// Percent of No Válido
		cell = row.createCell(2);
		cell.setCellFormula("B4/" + (rowIndex - 1));
		cell.setCellStyle(percentCenterStyle);
		currentSheet3.autoSizeColumn(0);
		currentSheet3.autoSizeColumn(1);
		currentSheet3.autoSizeColumn(2);
		XSSFDrawing drawing = currentSheet3.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 1, 8, 9, 20);
		XSSFChart chart = drawing.createChart(anchor);
		chart.setTitleText("Nivel de adecuación estimado global");
		chart.setTitleOverlay(false);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.TOP_RIGHT);
		XDDFDataSource<String> labels = XDDFDataSourcesFactory.fromStringCellRange(currentSheet3, new CellRangeAddress(1, 3, 0, 0));
		XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet3, new CellRangeAddress(1, 3, 1, 1));
		XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
		data.setVaryColors(true);
		data.addSeries(labels, values);
		chart.plot(data);
		// COMPLIANCE
		int complianceColumn = 0;
		for (int i = columnNames.size() - 1; i > 5; i--) {
			if (columnNames.get(i).contains("cumplimiento") && !columnNames.get(i).contains("ant")) {
				complianceColumn = i + 1;
			}
		}
		// "Headers"
		row = currentSheet3.createRow(25);
		cell = row.createCell(1);
		cell.setCellValue("Número de páginas.");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(2);
		cell.setCellValue("Porcentaje sobre el total.");
		cell.setCellStyle(headerStyle);
		// "Plenamente conforme"
		row = currentSheet3.createRow(26);
		cell = row.createCell(0);
		cell.setCellValue("Plenamente conforme");
		cell.setCellStyle(headerStyle);
		// Number of Plenamente conforme
		cell = row.createCell(1);
		cell.setCellFormula("COUNTIF(Resultados!" + GetExcelColumnNameForNumber(complianceColumn) + "2:" + GetExcelColumnNameForNumber(complianceColumn) + rowIndex + ",\"Plenamente conforme\")");
		// Percent of Plenamente conforme
		cell = row.createCell(2);
		cell.setCellFormula("B27/" + (rowIndex - 1));
		cell.setCellStyle(percentCenterStyle);
		// "Parcialmente conforme"
		row = currentSheet3.createRow(27);
		cell = row.createCell(0);
		cell.setCellValue("Parcialmente conforme");
		cell.setCellStyle(headerStyle);
		// Number of Parcialmente conforme
		cell = row.createCell(1);
		cell.setCellFormula("COUNTIF(Resultados!" + GetExcelColumnNameForNumber(complianceColumn) + "2:" + GetExcelColumnNameForNumber(complianceColumn) + rowIndex + ",\"Parcialmente conforme\")");
		// Percent of Parcialmente conforme
		cell = row.createCell(2);
		cell.setCellFormula("B28/" + (rowIndex - 1));
		cell.setCellStyle(percentCenterStyle);
		// "No conforme"
		row = currentSheet3.createRow(28);
		cell = row.createCell(0);
		cell.setCellValue("No conforme");
		cell.setCellStyle(headerStyle);
		// Number of No conforme
		cell = row.createCell(1);
		cell.setCellFormula("COUNTIF(Resultados!" + GetExcelColumnNameForNumber(complianceColumn) + "2:" + GetExcelColumnNameForNumber(complianceColumn) + rowIndex + ",\"No conforme\")");
		// Percent of No conforme
		cell = row.createCell(2);
		cell.setCellFormula("B29/" + (rowIndex - 1));
		cell.setCellStyle(percentCenterStyle);
		currentSheet3.autoSizeColumn(0);
		currentSheet3.autoSizeColumn(1);
		currentSheet3.autoSizeColumn(2);
		XSSFDrawing drawing2 = currentSheet3.createDrawingPatriarch();
		XSSFClientAnchor anchor2 = drawing2.createAnchor(0, 0, 0, 0, 1, 32, 9, 44);
		XSSFChart chart2 = drawing2.createChart(anchor2);
		chart2.setTitleText("Situación de cumplimiento estimada global");
		chart2.setTitleOverlay(false);
		XDDFChartLegend legend2 = chart2.getOrAddLegend();
		legend2.setPosition(LegendPosition.TOP_RIGHT);
		XDDFDataSource<String> labels2 = XDDFDataSourcesFactory.fromStringCellRange(currentSheet3, new CellRangeAddress(26, 28, 0, 0));
		XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet3, new CellRangeAddress(26, 28, 1, 1));
		XDDFChartData data2 = chart2.createData(ChartTypes.PIE, null, null);
		data2.setVaryColors(true);
		data2.addSeries(labels2, values2);
		chart2.plot(data2);
	}

	/**
	 * Fill null cell in range.
	 *
	 * @param sheetAt          the sheet at
	 * @param categoryFirstRow the category first row
	 * @param categoryLastRow  the category last row
	 * @param firstSerieColumn the first serie column
	 */
	private static void FillNullCellInRange(XSSFSheet sheetAt, int categoryFirstRow, int categoryLastRow, int firstSerieColumn) {
		for (int i = categoryFirstRow; i <= categoryLastRow; i++) {
			XSSFRow row = sheetAt.getRow(i);
			if (row != null) {
				XSSFCell cell = row.getCell(firstSerieColumn);
				if (cell == null) {
					XSSFCell c = row.createCell(firstSerieColumn);
					c.setCellValue(-1);
				}
			}
		}
	}

	/**
	 * Insert summary table.
	 *
	 * @param sheet            the sheet
	 * @param RowStartPosition the row start position
	 * @param ColumnNames      the column names
	 * @param headerStyle      the header style
	 * @param shadowStyle      the shadow style
	 * @return the int
	 */
	private static int InsertSummaryTable(XSSFSheet sheet, int RowStartPosition, List<String> ColumnNames, CellStyle headerStyle, CellStyle shadowStyle) {
		// create light shadow cell style CENTERED
		CellStyle shadowStyleCentered = sheet.getWorkbook().createCellStyle();
		shadowStyleCentered.setWrapText(true);
		shadowStyleCentered.setAlignment(HorizontalAlignment.CENTER);
		shadowStyleCentered.setVerticalAlignment(VerticalAlignment.CENTER);
		shadowStyleCentered.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		shadowStyleCentered.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFCell cell;
		XSSFRow row;
		// Insert Summary table.
		String columnResumeNamePrevious = GetExcelColumnNameForNumber(ColumnNames.size() - 2);
		String columnResumeNameFirst = GetExcelColumnNameForNumber(ColumnNames.size());
		row = sheet.createRow(RowStartPosition);
		cell = row.createCell(0);
		cell.setCellValue("Datos de evolución de portales por nivel de adecuación");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(1);
		cell.setCellValue("Anterior");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(2);
		cell.setCellValue("Primera");
		cell.setCellStyle(headerStyle);
		row = sheet.createRow(RowStartPosition + 1);
		cell = row.createCell(0);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("De NV a P1");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",1)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",1)");
		row = sheet.createRow(RowStartPosition + 2);
		cell = row.createCell(0);
		cell.setCellValue("De NV a P2");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",3)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",3)");
		row = sheet.createRow(RowStartPosition + 3);
		cell = row.createCell(0);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("De P1 a P2");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",2)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",2)");
		row = sheet.createRow(RowStartPosition + 4);
		cell = row.createCell(0);
		cell.setCellValue("De P2 a P1");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",-2)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",-2)");
		row = sheet.createRow(RowStartPosition + 5);
		cell = row.createCell(0);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("De P2 a NV");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",-3)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",-3)");
		row = sheet.createRow(RowStartPosition + 6);
		cell = row.createCell(0);
		cell.setCellValue("De P1 a NV");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",-1)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",-1)");
		row = sheet.createRow(RowStartPosition + 7);
		cell = row.createCell(0);
		cell.setCellValue("Total cambian: ");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(1);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(C" + (RowStartPosition + 2) + ":C" + (RowStartPosition + 7) + ")");
		cell = row.createCell(2);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(C" + (RowStartPosition + 2) + ":C" + (RowStartPosition + 7) + ")");
		return RowStartPosition + 7;
	}

	/**
	 * Insert categories table.
	 *
	 * @param sheet            the sheet
	 * @param RowStartPosition the row start position
	 * @param categories       the categories
	 * @param headerStyle      the header style
	 * @param shadowStyle      the shadow style
	 * @param lastDataRow      the last data row
	 * @param columnSourceData the column source data
	 * @param title            the title
	 * @return the int
	 */
	private static int InsertCategoriesTable(XSSFSheet sheet, int RowStartPosition, List<String> categories, CellStyle headerStyle, CellStyle shadowStyle, int lastDataRow, int columnSourceData,
			String title) {
		// create light shadow cell style CENTERED
		CellStyle shadowStyleCentered = sheet.getWorkbook().createCellStyle();
		shadowStyleCentered.setWrapText(true);
		shadowStyleCentered.setAlignment(HorizontalAlignment.CENTER);
		shadowStyleCentered.setVerticalAlignment(VerticalAlignment.CENTER);
		shadowStyleCentered.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		shadowStyleCentered.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFCell cell;
		XSSFRow row;
		// Insert Summary table.
		row = sheet.createRow(RowStartPosition);
		cell = row.createCell(0);
		cell.setCellValue(title);
		cell.setCellStyle(headerStyle);
		row = sheet.createRow(RowStartPosition + 1);
		cell = row.createCell(0);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Segmento");
		cell = row.createCell(1);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Empeoran mucho");
		cell = row.createCell(2);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Empeoran");
		cell = row.createCell(3);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Se mantiene");
		cell = row.createCell(4);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Mejoran");
		cell = row.createCell(5);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("Mejoran mucho");
		String dataColumn = GetExcelColumnNameForNumber(columnSourceData);
		for (int i = 0; i < categories.size(); i++) {
			row = sheet.createRow(RowStartPosition + i + 2);
			cell = row.createCell(0);
			cell.setCellStyle(shadowStyle);
			cell.setCellValue(categories.get(i));
			cell = row.createCell(1);
			cell.setCellStyle(shadowStyleCentered);
			cell.setCellFormula("COUNTIFS($C$2:$C$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"EMPEORA MUCHO\")");
			cell = row.createCell(2);
			cell.setCellStyle(shadowStyleCentered);
			cell.setCellFormula("COUNTIFS($C$2:$C$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"EMPEORA\")");
			cell = row.createCell(3);
			cell.setCellStyle(shadowStyleCentered);
			cell.setCellFormula("COUNTIFS($C$2:$C$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"SE MANTIENE\")");
			cell = row.createCell(4);
			cell.setCellStyle(shadowStyleCentered);
			cell.setCellFormula("COUNTIFS($C$2:$C$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"MEJORA\")");
			cell = row.createCell(5);
			cell.setCellStyle(shadowStyleCentered);
			cell.setCellFormula("COUNTIFS($C$2:$C$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"MEJORA MUCHO\")");
		}
		// TOTAL row
		row = sheet.createRow(RowStartPosition + categories.size() + 2);
		cell = row.createCell(0);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("TOTAL");
		cell = row.createCell(1);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(B" + (RowStartPosition + 3) + ":B" + (RowStartPosition + categories.size() + 2) + ")");
		cell = row.createCell(2);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(C" + (RowStartPosition + 3) + ":C" + (RowStartPosition + categories.size() + 2) + ")");
		cell = row.createCell(3);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(D" + (RowStartPosition + 3) + ":D" + (RowStartPosition + categories.size() + 2) + ")");
		cell = row.createCell(4);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(E" + (RowStartPosition + 3) + ":E" + (RowStartPosition + categories.size() + 2) + ")");
		cell = row.createCell(5);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(F" + (RowStartPosition + 3) + ":F" + (RowStartPosition + categories.size() + 2) + ")");
		cell = row.createCell(6);
		cell.setCellStyle(headerStyle);
		cell.setCellFormula("SUM(B" + (RowStartPosition + categories.size() + 3) + ":F" + (RowStartPosition + categories.size() + 3) + ")");
		return RowStartPosition + categories.size() + 1;
	}

	/**
	 * Gets the excel column name for number.
	 *
	 * @param number the number
	 * @return the string
	 */
	public static String GetExcelColumnNameForNumber(int number) {
		StringBuilder sb = new StringBuilder();
		int num = number - 1;
		while (num >= 0) {
			int numChar = (num % 26) + 65;
			sb.append((char) numChar);
			num = (num / 26) - 1;
		}
		return sb.reverse().toString();
	}

	/**
	 * Insert graph into sheet by category.
	 *
	 * @param wb               the wb
	 * @param sheet            the sheet
	 * @param categoryFirstRow the category first row
	 * @param categoryLastRow  the category last row
	 * @param isFirst          the is first
	 */
	private static void InsertGraphIntoSheetByCategory(XSSFWorkbook wb, XSSFSheet sheet, int categoryFirstRow, int categoryLastRow, boolean isFirst) {
		if (sheet != null) {
			XSSFDrawing drawing = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, isFirst ? 4 : 45, Math.max(categoryLastRow - categoryFirstRow, 16), isFirst ? 40 : 85);
			XSSFChart chart = drawing.createChart(anchor);
			chart.setTitleText(isFirst ? "Nivel de adecuación estimado" : "Situación de cumplimiento estimada");
			chart.setTitleOverlay(false);
			XDDFChartLegend legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.LEFT);
			XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
			XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
			bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
			leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
			bottomAxis.setTickLabelPosition(AxisTickLabelPosition.LOW);
			bottomAxis.setMajorTickMark(AxisTickMark.NONE);
			bottomAxis.setPosition(AxisPosition.RIGHT);
			CTPlotArea plotArea = chart.getCTChart().getPlotArea();
			plotArea.getValAxArray()[0].addNewMajorGridlines();
			// Get agency names
			XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(wb.getSheetAt(0), new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, 1, 1));
			// First serie ("No válido" / "No Conforme")
			XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 15 : 18, isFirst ? 15 : 18));
			XDDFChartData.Series series1 = data.addSeries(agencies, values1);
			series1.setTitle(isFirst ? "No Válido" : "No Conforme", null);
			// Set series color
			XDDFShapeProperties properties1 = series1.getShapeProperties();
			if (properties1 == null) {
				properties1 = new XDDFShapeProperties();
			}
			properties1.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED)));
			series1.setShapeProperties(properties1);
			// Second serie ("A" / "Parcialmente conforme")
			XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 16 : 19, isFirst ? 16 : 19));
			XDDFChartData.Series series2 = data.addSeries(agencies, values2);
			series2.setTitle(isFirst ? "A" : "Parcialmente conforme", null);
			// Set series color
			XDDFShapeProperties properties2 = series2.getShapeProperties();
			if (properties2 == null) {
				properties2 = new XDDFShapeProperties();
			}
			properties2.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.YELLOW)));
			series2.setShapeProperties(properties2);
			// Third serie ("AA" / "Plenamente conforme")
			XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 17 : 20, isFirst ? 17 : 20));
			XDDFChartData.Series series3 = data.addSeries(agencies, values3);
			series3.setTitle(isFirst ? "AA" : "Plenamente Conforme", null);
			// Set series color
			XDDFShapeProperties properties3 = series3.getShapeProperties();
			if (properties3 == null) {
				properties3 = new XDDFShapeProperties();
			}
			properties3.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN)));
			series3.setShapeProperties(properties3);
			chart.plot(data);
			XDDFBarChartData bar = (XDDFBarChartData) data;
			bar.setBarDirection(BarDirection.COL);
		}
	}

	/**
	 * Insert graph into sheet by evolution.
	 *
	 * @param wb                   the wb
	 * @param currentSheet         the current sheet
	 * @param categoryFirstRow     the category first row
	 * @param categoryLastRow      the category last row
	 * @param isFirst              the is first
	 * @param numberOfFixedColumns the number of fixed columns
	 */
	private static void InsertGraphIntoSheetByEvolution(XSSFWorkbook wb, XSSFSheet currentSheet, int categoryFirstRow, int categoryLastRow, boolean isFirst, int numberOfFixedColumns) {
		XSSFDrawing drawing = currentSheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, isFirst ? 4 : 45, Math.max(categoryLastRow - categoryFirstRow, 16), isFirst ? 40 : 85);
		XSSFChart chart = drawing.createChart(anchor);
		chart.setTitleText(isFirst ? "Evolución de la adecuación" : "Evolución del cumplimiento estimado");
		chart.setTitleOverlay(false);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.LEFT);
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		bottomAxis.setTickLabelPosition(AxisTickLabelPosition.LOW);
		bottomAxis.setMajorTickMark(AxisTickMark.NONE);
		bottomAxis.setPosition(AxisPosition.RIGHT);
		CTPlotArea plotArea = chart.getCTChart().getPlotArea();
		plotArea.getValAxArray()[0].addNewMajorGridlines();
		plotArea.getValAxArray(0).getScaling().addNewMax().setVal(10);
		plotArea.getValAxArray(0).getScaling().addNewMin().setVal(0);
		// Get agency names
		XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(wb.getSheetAt(0), new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, 1, 1));
		// Iterate through the executions
		for (String date : executionDates) {
			int firstSerieColumn = numberOfFixedColumns + (executionDates.size() * 3) + (6 * executionDates.indexOf(date));
			// First serie ("No válido" / "No Conforme")
			FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 0 : 3));
			XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 0 : 3), firstSerieColumn + (isFirst ? 0 : 3)));
			XDDFChartData.Series series1 = data.addSeries(agencies, values1);
			series1.setTitle((isFirst ? "NV_" : "NC_") + date, null);
			// Set series color
			XDDFShapeProperties properties1 = series1.getShapeProperties();
			if (properties1 == null) {
				properties1 = new XDDFShapeProperties();
			}
			properties1.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED)));
			series1.setShapeProperties(properties1);
			// Second serie ("A" / "Parcialmente conforme")
			FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 1 : 4));
			XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 1 : 4), firstSerieColumn + (isFirst ? 1 : 4)));
			XDDFChartData.Series series2 = data.addSeries(agencies, values2);
			series2.setTitle((isFirst ? "A_" : "PC_") + date, null);
			// Set series color
			XDDFShapeProperties properties2 = series2.getShapeProperties();
			if (properties2 == null) {
				properties2 = new XDDFShapeProperties();
			}
			properties2.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.YELLOW)));
			series2.setShapeProperties(properties2);
			// Third serie ("AA" / "Plenamente conforme")
			FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 2 : 5));
			XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, firstSerieColumn + (isFirst ? 2 : 5), firstSerieColumn + (isFirst ? 2 : 5)));
			XDDFChartData.Series series3 = data.addSeries(agencies, values3);
			series3.setTitle((isFirst ? "AA_" : "TC_") + date, null);
			// Set series color
			XDDFShapeProperties properties3 = series3.getShapeProperties();
			if (properties3 == null) {
				properties3 = new XDDFShapeProperties();
			}
			properties3.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN)));
			series3.setShapeProperties(properties3);
		}
		chart.plot(data);
		XDDFBarChartData bar = (XDDFBarChartData) data;
		bar.setBarDirection(BarDirection.COL);
	}

	/**
	 * Insert graph into sheet by dependency.
	 *
	 * @param currentSheet         the current sheet
	 * @param rowIndex             the row index
	 * @param isFirst              the is first
	 * @param numberOfFixedColumns the number of fixed columns
	 * @param onlyLastIteration    the only last iteration
	 */
	private static void InsertGraphIntoSheetByDependency(XSSFSheet currentSheet, int rowIndex, boolean isFirst, int numberOfFixedColumns, boolean onlyLastIteration) {
		XSSFDrawing drawing = currentSheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, isFirst ? 4 : 45, Math.max(rowIndex, 16), isFirst ? 40 : 85);
		XSSFChart chart = drawing.createChart(anchor);
		chart.setTitleText(isFirst ? "Evolución de la adecuación" : "Evolución del cumplimiento estimado");
		chart.setTitleOverlay(false);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.LEFT);
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		bottomAxis.setTickLabelPosition(AxisTickLabelPosition.LOW);
		bottomAxis.setMajorTickMark(AxisTickMark.NONE);
		bottomAxis.setPosition(AxisPosition.RIGHT);
		CTPlotArea plotArea = chart.getCTChart().getPlotArea();
		plotArea.getValAxArray()[0].addNewMajorGridlines();
		// Get agency names
		XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(currentSheet.getWorkbook().getSheetAt(0), new CellRangeAddress(1, rowIndex - 1, 1, 1));
		// Iterate through the executions
		int i = 0;
		for (String date : executionDates) {
			if (!onlyLastIteration || (onlyLastIteration && i == (executionDates.size() - 1))) {
				int firstSerieColumn = numberOfFixedColumns + (executionDates.size() * 3) + (6 * executionDates.indexOf(date));
				// First serie ("No válido" / "No Conforme")
				FillNullCellInRange(currentSheet.getWorkbook().getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + (isFirst ? 0 : 3));
				XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet.getWorkbook().getSheetAt(0),
						new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + (isFirst ? 0 : 3), firstSerieColumn + (isFirst ? 0 : 3)));
				XDDFChartData.Series series1 = data.addSeries(agencies, values1);
				series1.setTitle((isFirst ? "NV_" : "NC_") + date, null);
				// Set series color
				XDDFShapeProperties properties1 = series1.getShapeProperties();
				if (properties1 == null) {
					properties1 = new XDDFShapeProperties();
				}
				properties1.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED)));
				series1.setShapeProperties(properties1);
				// Second serie ("A" / "Parcialmente conforme")
				FillNullCellInRange(currentSheet.getWorkbook().getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + (isFirst ? 1 : 4));
				XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet.getWorkbook().getSheetAt(0),
						new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + (isFirst ? 1 : 4), firstSerieColumn + (isFirst ? 1 : 4)));
				XDDFChartData.Series series2 = data.addSeries(agencies, values2);
				series2.setTitle((isFirst ? "A_" : "PC_") + date, null);
				// Set series color
				XDDFShapeProperties properties2 = series2.getShapeProperties();
				if (properties2 == null) {
					properties2 = new XDDFShapeProperties();
				}
				properties2.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.YELLOW)));
				series2.setShapeProperties(properties2);
				// Third serie ("AA" / "Plenamente conforme")
				FillNullCellInRange(currentSheet.getWorkbook().getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + (isFirst ? 2 : 5));
				XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet.getWorkbook().getSheetAt(0),
						new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + (isFirst ? 2 : 5), firstSerieColumn + (isFirst ? 2 : 5)));
				XDDFChartData.Series series3 = data.addSeries(agencies, values3);
				series3.setTitle((isFirst ? "AA_" : "TC_") + date, null);
				// Set series color
				XDDFShapeProperties properties3 = series3.getShapeProperties();
				if (properties3 == null) {
					properties3 = new XDDFShapeProperties();
				}
				properties3.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(PresetColor.GREEN)));
				series3.setShapeProperties(properties3);
			}
			i++;
		}
		chart.plot(data);
		XDDFBarChartData bar = (XDDFBarChartData) data;
		bar.setBarDirection(BarDirection.COL);
	}

	/**
	 * Creates the XLSX annex.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @throws Exception the exception
	 */
	public static void createComparativeSuitabilitieXLSX(final MessageResources messageResources, final Long idObsExecution, final Long idOperation) throws Exception {
		/*
		 * try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, "suitabilities.xlsx")) { final String[] ColumnNames = new String[]{"Grupo",
		 * "Observatorio", "Parcial", "Prioridad 1", "Prioridad 1 y 2"}; final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);
		 * 
		 * XSSFWorkbook wb = new XSSFWorkbook(); XSSFSheet sheet = wb.createSheet("Global");
		 * 
		 * //create default cell style (aligned top left and allow line wrapping) CellStyle defaultStyle = wb.createCellStyle(); defaultStyle.setWrapText(true);
		 * defaultStyle.setAlignment(HorizontalAlignment.LEFT); defaultStyle.setVerticalAlignment(VerticalAlignment.TOP);
		 * 
		 * int rowIndex = 0; int columnIndex = 0; XSSFRow row; XSSFCell cell;
		 * 
		 * Object[][] tableData = { {"Global", "AGE Mayo 2018", 28, 13, 59}, {"Global", "CCAA Mayo 2018", 45, 27, 28}, {"Global", "EELL Mayo 2018", 62, 18, 20}, {"Principales", "AGE Mayo 2018", 0 ,
		 * 22, 78}, {"Principales", "CCAA Mayo 2018", 21, 26, 53}, {"Principales", "EELL Mayo 2018", 44, 28, 28}, {"Global", "AGE Noviembre 2018", 27, 14, 59}, {"Global", "CCAA Noviembre 2018", 45,
		 * 25, 30}, {"Global", "EELL Noviembre 2018", 60, 18, 22}, {"Principales", "AGE Noviembre 2018", 14, 9, 77}, {"Principales", "CCAA Noviembre 2018", 32, 26, 42}, {"Principales",
		 * "EELL Noviembre 2018", 45, 20, 35}, {"Global", "AGE Junio 2019", 25, 11, 64}, {"Global", "CCAA Junio 2019", 38, 30, 32}, {"Global", "EELL Junio 2019", 61, 16, 23}, {"Principales",
		 * "AGE Junio 2019", 9 , 5, 86}, {"Principales", "CCAA Junio 2019", 25, 30, 45}, {"Principales", "EELL Junio 2019", 47, 16, 37} };
		 * 
		 * // Add headers row = sheet.createRow(rowIndex); for (String name : ColumnNames ) { cell = row.createCell(columnIndex); cell.setCellValue(name); columnIndex++; }
		 * 
		 * // [TEMPORAL] Add info manually rowIndex=0; for (Object[] dataLine : tableData) { row = sheet.createRow(++rowIndex);
		 * 
		 * columnIndex = 0; for (Object field : dataLine) { cell = row.createCell(columnIndex); if (field instanceof String) { cell.setCellValue((String) field); } else if (field instanceof Integer) {
		 * cell.setCellValue((Integer) field); } columnIndex++; } }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * // determine the type of the category axis from it's first category value (value in A2 in this case) XDDFDataSource dataSource = null; CellType type = CellType.ERROR; row = sheet.getRow(1);
		 * if (row != null) { cell = row.getCell(0); if (cell != null) { dataSource = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 10, 1, 1)); } } if (dataSource != null)
		 * { // if no type of category axis found, don't create a chart at all XDDFNumericalDataSource<Double> high = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 10, 2,
		 * 2)); XDDFNumericalDataSource<Double> medium = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 10, 3, 3)); XDDFNumericalDataSource<Double> low =
		 * XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 10, 4, 4));
		 * 
		 * XSSFDrawing drawing = sheet.createDrawingPatriarch(); XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 6, 0, 16, 20);
		 * 
		 * XSSFChart chart = drawing.createChart(anchor); XDDFChartLegend legend = chart.getOrAddLegend(); legend.setPosition(LegendPosition.RIGHT);
		 * 
		 * // bar chart
		 * 
		 * XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT); leftAxis.setTitle("Number of defects");
		 * leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		 * 
		 * // category axis crosses the value axis between the strokes and not midpoint the strokes leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		 * 
		 * XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis); XDDFChartData.Series series1 = data.addSeries(dataSource, high); series1.setTitle("Parcial", new
		 * CellReference(sheet.getSheetName(), 0, 2, true, true)); XDDFChartData.Series series2 = data.addSeries(dataSource, medium); series2.setTitle("Prioridad 1", new
		 * CellReference(sheet.getSheetName(), 0, 3, true, true)); XDDFChartData.Series series3 = data.addSeries(dataSource, low); series3.setTitle("Prioridad 1 y 2", new
		 * CellReference(sheet.getSheetName(), 0, 4, true, true)); chart.plot(data);
		 * 
		 * XDDFBarChartData bar = (XDDFBarChartData) data; bar.setBarDirection(BarDirection.COL);
		 * 
		 * // looking for "Stacked Bar Chart"? uncomment the following line bar.setBarGrouping(BarGrouping.STACKED);
		 * 
		 * // correcting the overlap so bars really are stacked and not side by side chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal((byte)100);
		 * 
		 * solidFillSeries(data, 0, PresetColor.ORANGE); solidFillSeries(data, 1, PresetColor.YELLOW); solidFillSeries(data, 2, PresetColor.GREEN);
		 * 
		 * // add data labels for (int s = 0 ; s < 3; s++) { chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).addNewDLbls(); //
		 * chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls() // .addNewDLblPos().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos.CTR);
		 * chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowVal().setVal(true);
		 * chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowLegendKey().setVal(false);
		 * chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowCatName().setVal(false);
		 * chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowSerName().setVal(false); }
		 * 
		 * // line chart
		 * 
		 * // axis must be there but must not be visible bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM); bottomAxis.setVisible(false); leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		 * leftAxis.setVisible(false);
		 * 
		 * // set correct cross axis bottomAxis.crossAxis(leftAxis); leftAxis.crossAxis(bottomAxis);
		 * 
		 * data = chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
		 * 
		 * chart.plot(data); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * XSSFFormulaEvaluator.evaluateAllFormulaCells(wb); wb.write(writer); wb.close(); } catch (Exception e) { Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e); throw e; }
		 */
	}

	/**
	 * Solid fill series.
	 *
	 * @param data  the data
	 * @param index the index
	 * @param color the color
	 */
	private static void solidFillSeries(XDDFChartData data, int index, PresetColor color) {
		XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
		XDDFChartData.Series series = data.getSeries().get(index);
		XDDFShapeProperties properties = series.getShapeProperties();
		if (properties == null) {
			properties = new XDDFShapeProperties();
		}
		properties.setFillProperties(fill);
		series.setShapeProperties(properties);
	}

	/**
	 * Solid line series.
	 *
	 * @param data  the data
	 * @param index the index
	 * @param color the color
	 */
	private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
		XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
		XDDFLineProperties line = new XDDFLineProperties();
		line.setFillProperties(fill);
		XDDFChartData.Series series = data.getSeries().get(index);
		XDDFShapeProperties properties = series.getShapeProperties();
		if (properties == null) {
			properties = new XDDFShapeProperties();
		}
		properties.setLineProperties(line);
		series.setShapeProperties(properties);
	}

	/**
	 * Gets the file writer.
	 *
	 * @param idOperation the id operation
	 * @param filename    the filename
	 * @return the file writer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static FileWriter getFileWriter(final Long idOperation, final String filename) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + filename);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("No se ha podido crear los directorios al exportar los anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		return new FileWriter(file);
	}

	/**
	 * Gets the file writer.
	 *
	 * @param idOperation the id operation
	 * @param filename    the filename
	 * @return the file writer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static FileOutputStream getFileOutputStream(final Long idOperation, final String filename) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path") + idOperation + File.separator + filename);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("No se ha podido crear los directorios al exportar los anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		return new FileOutputStream(file);
	}

	/**
	 * Gets the content handler.
	 *
	 * @param writer the writer
	 * @return the content handler
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static ContentHandler getContentHandler(final FileWriter writer) throws IOException {
		final XMLSerializer serializer = new XMLSerializer(writer, new OutputFormat("XML", "UTF-8", true));
		return serializer.asContentHandler();
	}

	/**
	 * Write tag.
	 *
	 * @param contentHandler the content handler
	 * @param tagName        the tag name
	 * @param text           the text
	 * @throws SAXException the SAX exception
	 */
	private static void writeTag(final ContentHandler contentHandler, final String tagName, final String text) throws SAXException {
		contentHandler.startElement(EMPTY_STRING, EMPTY_STRING, tagName, null);
		if (text != null) {
			contentHandler.characters(text.toCharArray(), 0, text.length());
		}
		contentHandler.endElement(EMPTY_STRING, EMPTY_STRING, tagName);
	}

	/**
	 * Creates the annex map.
	 *
	 * @param idObsExecution the id obs execution
	 * @param tagsToFilter   the tags to filter
	 * @param exObsIds       the ex obs ids
	 * @return the map
	 */
	private static Map<Long, TreeMap<String, ScoreForm>> createAnnexMap(final Long idObsExecution, final String[] tagsToFilter, final String[] exObsIds) {
		final Map<Long, TreeMap<String, ScoreForm>> seedMap = new HashMap<>();
		Connection c = null;
		try {
			c = DataBaseManager.getConnection();
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryFormFromExecution(c, idObsExecution);
			final ObservatorioRealizadoForm executedObservatory = ObservatorioDAO.getFulfilledObservatory(c, observatoryForm.getId(), idObsExecution);
			// Filter by idObsEx
			final List<ObservatorioRealizadoForm> observatoriesList = ObservatorioDAO.getFulfilledObservatories(c, observatoryForm.getId(), Constants.NO_PAGINACION, executedObservatory.getFecha(),
					false, exObsIds);
			final List<ObservatoryForm> observatoryFormList = new ArrayList<>();
			for (ObservatorioRealizadoForm orForm : observatoriesList) {
				final ObservatoryForm observatory = ObservatoryExportManager.getObservatory(orForm.getId());
				if (observatory != null) {
					observatoryFormList.add(observatory);
				}
			}
			for (ObservatoryForm observatory : observatoryFormList) {
				for (CategoryForm category : observatory.getCategoryFormList()) {
					for (SiteForm siteForm : category.getSiteFormList()) {
						final ScoreForm scoreForm = new ScoreForm();
						scoreForm.setLevel(siteForm.getLevel());
						scoreForm.setTotalScore(new BigDecimal(siteForm.getScore()));
						TreeMap<String, ScoreForm> seedInfo = new TreeMap<>();
						if (seedMap.get(Long.valueOf(siteForm.getIdCrawlerSeed())) != null) {
							seedInfo = seedMap.get(Long.valueOf(siteForm.getIdCrawlerSeed()));
						}
						seedInfo.put(observatory.getDate(), scoreForm);
						seedMap.put(Long.valueOf(siteForm.getIdCrawlerSeed()), seedInfo);
						scoreForm.setCompliance(siteForm.getCompliance());
						// scoreForm.setVerificationScoreList(siteForm.getVerificationScoreList());
						List<VerificationScoreForm> verificationScoreList = new LinkedList<>();
						for (VerificationScoreForm verification : siteForm.getVerificationScoreList()) {
							VerificationScoreForm vsf = new VerificationScoreForm();
							BeanUtils.copyProperties(vsf, verification);
							verificationScoreList.add(vsf);
						}
						Collections.sort(verificationScoreList, new Comparator<VerificationScoreForm>() {
							@Override
							public int compare(VerificationScoreForm version1, VerificationScoreForm version2) {
								String[] v1 = version1.getVerification().split("\\.");
								String[] v2 = version2.getVerification().split("\\.");
								int major1 = major(v1);
								int major2 = major(v2);
								if (major1 == major2) {
									return minor(v1).compareTo(minor(v2));
								}
								return major1 > major2 ? 1 : -1;
							}

							private int major(String[] version) {
								return Integer.parseInt(version[0]);
							}

							private Integer minor(String[] version) {
								return version.length > 1 ? Integer.parseInt(version[1]) : 0;
							}
						});
						scoreForm.setVerificationScoreList(verificationScoreList);
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al recuperar las semillas del Observatorio al crear el anexo", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
		} finally {
			try {
				DataBaseManager.closeConnection(c);
			} catch (Exception e) {
				Logger.putLog("Excepción", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			}
		}
		return seedMap;
	}

	/**
	 * Change level name.
	 *
	 * @param name             the name
	 * @param messageResources the message resources
	 * @return the string
	 */
	private static String changeLevelName(final String name, final MessageResources messageResources) {
		if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.aa.old"))) {
			return messageResources.getMessage("resultados.anonimos.num.portales.aa");
		} else if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.nv.old"))) {
			return messageResources.getMessage("resultados.anonimos.num.portales.nv");
		} else if (name.equalsIgnoreCase(messageResources.getMessage("resultados.anonimos.num.portales.a.old"))) {
			return messageResources.getMessage("resultados.anonimos.num.portales.a");
		} else {
			return EMPTY_STRING;
		}
	}

	/**
	 * The Class ExcelLine.
	 */
	private static class ExcelLine {
		/** The id. */
		private String id;
		/** The nombre. */
		private String nombre;
		/** The namecat. */
		private String namecat;
		/** The ambito. */
		private String ambito;
		/** The depende de. */
		private String complejidad;
		/** The depende de. */
		private String depende_de;
		/** The semilla. */
		private String semilla;
		/** The tematica. */
		private String tematica;
		/** The distribucion. */
		private String distribucion;
		/** The recurrencia. */
		private String recurrencia;
		/** The otros. */
		private String otros;
		/** The row paginas. */
		private String paginas;
		/** The row index. */
		private Integer rowIndex;
		/** The executions. */
		private List<ExcelExecution> executions = new ArrayList<>();

		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * Sets the id.
		 *
		 * @param id the new id
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * Gets the nombre.
		 *
		 * @return the nombre
		 */
		public String getNombre() {
			return nombre;
		}

		/**
		 * Sets the nombre.
		 *
		 * @param nombre the new nombre
		 */
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		/**
		 * Gets the namecat.
		 *
		 * @return the namecat
		 */
		public String getNamecat() {
			return namecat;
		}

		/**
		 * Sets the namecat.
		 *
		 * @param namecat the new namecat
		 */
		public void setNamecat(String namecat) {
			this.namecat = namecat;
		}

		/**
		 * Gets the ambito.
		 *
		 * @return the ambito
		 */
		public String getAmbito() {
			return ambito;
		}

		/**
		 * Sets the ambito.
		 *
		 * @param ambito the new ambito
		 */
		public void setAmbito(String ambito) {
			this.ambito = ambito;
		}

		/**
		 * Gets the complejidad.
		 *
		 * @return the complejidad
		 */
		public String getComplejidad() {
			return complejidad;
		}

		/**
		 * Sets the complejidad.
		 *
		 * @param complejidad the new complejidad
		 */
		public void setComplejidad(String complejidad) {
			this.complejidad = complejidad;
		}

		/**
		 * Gets the depende de.
		 *
		 * @return the depende de
		 */
		public String getDepende_de() {
			return depende_de;
		}

		/**
		 * Sets the depende de.
		 *
		 * @param depende_de the new depende de
		 */
		public void setDepende_de(String depende_de) {
			this.depende_de = depende_de;
		}

		/**
		 * Gets the semilla.
		 *
		 * @return the semilla
		 */
		public String getSemilla() {
			return semilla;
		}

		/**
		 * Sets the semilla.
		 *
		 * @param semilla the new semilla
		 */
		public void setSemilla(String semilla) {
			this.semilla = semilla;
		}

		/**
		 * Gets the tematica.
		 *
		 * @return the tematica
		 */
		public String getTematica() {
			return tematica;
		}

		/**
		 * Sets the tematica.
		 *
		 * @param tematica the new tematica
		 */
		public void setTematica(String tematica) {
			this.tematica = tematica;
		}

		/**
		 * Gets the distribucion.
		 *
		 * @return the distribucion
		 */
		public String getDistribucion() {
			return distribucion;
		}

		/**
		 * Sets the distribucion.
		 *
		 * @param distribucion the new distribucion
		 */
		public void setDistribucion(String distribucion) {
			this.distribucion = distribucion;
		}

		/**
		 * Gets the recurrencia.
		 *
		 * @return the recurrencia
		 */
		public String getRecurrencia() {
			return recurrencia;
		}

		/**
		 * Sets the recurrencia.
		 *
		 * @param recurrencia the new recurrencia
		 */
		public void setRecurrencia(String recurrencia) {
			this.recurrencia = recurrencia;
		}

		/**
		 * Gets the otros.
		 *
		 * @return the otros
		 */
		public String getOtros() {
			return otros;
		}

		/**
		 * Sets the otros.
		 *
		 * @param otros the new otros
		 */
		public void setOtros(String otros) {
			this.otros = otros;
		}

		/**
		 * Gets the paginas.
		 *
		 * @return the paginas
		 */
		public String getPaginas() {
			return paginas;
		}

		/**
		 * Sets the paginas.
		 *
		 * @param paginas the new paginas
		 */
		public void setPaginas(String paginas) {
			this.paginas = paginas;
		}

		/**
		 * Gets the executions.
		 *
		 * @return the executions
		 */
		public List<ExcelExecution> getExecutions() {
			return executions;
		}

		/**
		 * Checks for date.
		 *
		 * @param dateToSearch the date to search
		 * @return true, if successful
		 */
		public boolean HasDate(String dateToSearch) {
			for (ExcelExecution e : executions) {
				if (e.date.equals(dateToSearch)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Gets the execution by date.
		 *
		 * @param dateToSearch the date to search
		 * @return the excel execution
		 */
		public ExcelExecution GetExecutionByDate(String dateToSearch) {
			for (ExcelExecution e : executions) {
				if (e.date.equals(dateToSearch)) {
					return e;
				}
			}
			return null;
		}

		/**
		 * Adds the execution.
		 *
		 * @param execution the execution
		 */
		public void addExecution(ExcelExecution execution) {
			this.executions.add(execution);
		}

		/**
		 * Gets the row index.
		 *
		 * @return the row index
		 */
		public Integer getRowIndex() {
			return rowIndex;
		}

		/**
		 * Sets the row index.
		 *
		 * @param rowIndex the new row index
		 */
		public void setRowIndex(Integer rowIndex) {
			this.rowIndex = rowIndex;
		}
	}

	/**
	 * The Class ExcelExecution.
	 */
	private static class ExcelExecution {
		/** The date. */
		private String date;
		/** The score. */
		private Double score;
		/** The adequacy. */
		private String adequacy;
		/** The compliance. */
		private String compliance;

		/**
		 * Gets the date.
		 *
		 * @return the date
		 */
		public String getDate() {
			return date;
		}

		/**
		 * Sets the date.
		 *
		 * @param date the new date
		 */
		public void setDate(String date) {
			this.date = date;
		}

		/**
		 * Gets the score.
		 *
		 * @return the score
		 */
		public Double getScore() {
			return score;
		}

		/**
		 * Sets the score.
		 *
		 * @param score the new score
		 */
		public void setScore(Double score) {
			this.score = score;
		}

		/**
		 * Gets the adequacy.
		 *
		 * @return the adequacy
		 */
		public String getAdequacy() {
			return adequacy;
		}

		/**
		 * Sets the adequacy.
		 *
		 * @param adequacy the new adequacy
		 */
		public void setAdequacy(String adequacy) {
			this.adequacy = adequacy;
		}

		/**
		 * Gets the compliance.
		 *
		 * @return the compliance
		 */
		public String getCompliance() {
			return compliance;
		}

		/**
		 * Sets the compliance.
		 *
		 * @param compliance the new compliance
		 */
		public void setCompliance(String compliance) {
			this.compliance = compliance;
		}
	}
}