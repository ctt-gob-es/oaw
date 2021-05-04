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

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
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
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
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
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.AxisCrossBetween;
import org.apache.poi.xddf.usermodel.chart.AxisCrosses;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.BarDirection;
import org.apache.poi.xddf.usermodel.chart.BarGrouping;
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
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.util.MessageResources;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
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
import es.inteco.rastreador2.actionform.observatorio.RangeForm;
import es.inteco.rastreador2.actionform.observatorio.TemplateRangeForm;
import es.inteco.rastreador2.actionform.observatorio.UraSendResultForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.observatorio.RangeDAO;
import es.inteco.rastreador2.dao.observatorio.TemplateRangeDAO;
import es.inteco.rastreador2.dao.observatorio.UraSendResultDAO;
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
	private static final String[] ALL_WCAG_EM_POINTS = new String[] { "1.1.1", "1.2.1", "1.2.2", "1.2.3", "1.2.4", "1.2.5", "1.3.1", "1.3.2", "1.3.3", "1.3.4", "1.3.5", "1.4.1", "1.4.2", "1.4.3",
			"1.4.4", "1.4.5", "1.4.10", "1.4.11", "1.4.12", "1.4.13", "2.1.1", "2.1.2", "2.1.4", "2.2.1", "2.2.2", "2.3.1", "2.4.1", "2.4.2", "2.4.3", "2.4.4", "2.4.5", "2.4.6", "2.4.7", "2.5.1",
			"2.5.2", "2.5.3", "2.5.4", "3.1.1", "3.1.2", "3.2.1", "3.2.2", "3.2.3", "3.2.4", "3.3.1", "3.3.2", "3.3.3", "3.3.4", "4.1.1", "4.1.2", "4.1.3" };
	/** The Constant EVOL_CUMPLIMIENTO_ANT. */
	private static final String EVOL_CUMPLIMIENTO_ANT = "evol_cumplimiento_ant";
	/** The Constant EVOL_CUMPLIMIENTO_PRIMER. */
	private static final String EVOL_CUMPLIMIENTO_PRIMER = "evol_cumplimiento_primer";
	/** The Constant GREEN_OAW_HTML. */
	private static final String GREEN_OAW_HTML = "#008000";
	/** The Constant EVOL_ADECUACION_PRIMER. */
	private static final String EVOL_ADECUACION_PRIMER = "evol_adecuacion_primer";
	/** The Constant EVOL_PUNTUACION_PRIMER. */
	private static final String EVOL_PUNTUACION_PRIMER = "evol_puntuacion_primer";
	/** The Constant EVOL_ADECUACION_ANT. */
	private static final String EVOL_ADECUACION_ANT = "evol_adecuacion_ant";
	/** The Constant EVOL_PUNTUACION_ANT. */
	private static final String EVOL_PUNTUACION_ANT = "evol_puntuacion_ant";
	/** The Constant RANKING_3RD. */
	private static final String RANKING_3RD = "3ª";
	/** The Constant RANKING_2ND. */
	private static final String RANKING_2ND = "2ª";
	/** The Constant RANKING_1ST. */
	private static final String RANKING_1ST = "1ª";
	/** The Constant A4. */
	private static final String A4 = "A4";
	/** The Constant A3. */
	private static final String A3 = "A3";
	/** The Constant A2. */
	private static final String A2 = "A2";
	/** The Constant B1. */
	private static final String B1 = "B1";
	/** The Constant KEY_DATE_FORMAT_EVOLUTION. */
	private static final String KEY_DATE_FORMAT_EVOLUTION = "date.format.evolution";
	/** The Constant MUCH_WORST_COLOR. */
	private static final String RED_OAW_HTML = "#ff0000";
	/** The Constant A1. */
	private static final String A1 = "A1";
	/** The Constant COLUMN_TITLE_NO_CONFORMES. */
	private static final String COLUMN_TITLE_NO_CONFORMES = "% PC+NC";
	/** The Constant COLUMN_TITLE_NOTA_MEDIA_NC. */
	private static final String COLUMN_TITLE_NOTA_MEDIA_NC = "NOTA MEDIA NC";
	/** The Constant COLUMN_TITLE_PERCENT_NC. */
	private static final String COLUMN_TITLE_PERCENT_NC = "% NC";
	/** The Constant COLUMN_TITLE_TOTAL_PORTALES_NC. */
	private static final String COLUMN_TITLE_TOTAL_PORTALES_NC = "TOTAL PORTALES NC";
	/** The Constant COLUMN_TITLE_NOTA_MEDIA_PC. */
	private static final String COLUMN_TITLE_NOTA_MEDIA_PC = "NOTA MEDIA PC";
	/** The Constant COLUMN_TITLE_PERCENT_PC. */
	private static final String COLUMN_TITLE_PERCENT_PC = "% PC";
	/** The Constant COLUMN_TITLE_TOTAL_PORTALES_PC. */
	private static final String COLUMN_TITLE_TOTAL_PORTALES_PC = "TOTAL PORTALES PC";
	/** The Constant COLUMN_TITLE_NOTA_MEDIA_TC. */
	private static final String COLUMN_TITLE_NOTA_MEDIA_TC = "NOTA MEDIA TC";
	/** The Constant COLUMN_TITLE_PERCENT_TC. */
	private static final String COLUMN_TITLE_PERCENT_TC = "% TC";
	/** The Constant COLUMN_TITLE_TOTAL_PORTALES_TC. */
	private static final String COLUMN_TITLE_TOTAL_PORTALES_TC = "TOTAL PORTALES TC";
	/** The Constant COLUMN_TITLE_TOTAL_PORTALES. */
	private static final String COLUMN_TITLE_TOTAL_PORTALES = "TOTAL PORTALES";
	/** The Constant COLUMN_TITLE_NO_CUMPLEN. */
	private static final String COLUMN_TITLE_NO_CUMPLEN = "% NO CUMPLEN";
	/** The Constant COLUMN_TITLE_NOTA_MEDIA_NV. */
	private static final String COLUMN_TITLE_NOTA_MEDIA_NV = "NOTA MEDIA NV";
	/** The Constant COLUMN_TITLE_NV. */
	private static final String COLUMN_TITLE_NV = "% NO VÁLIDO";
	/** The Constant COLUMN_TITLE_TOTAL_PORTALES_NV. */
	private static final String COLUMN_TITLE_TOTAL_PORTALES_NV = "TOTAL PORTALES NO VÁLIDO";
	/** The Constant COLUMN_TITLE_NOTA_MEDIA_A. */
	private static final String COLUMN_TITLE_NOTA_MEDIA_A = "NOTA MEDIA A";
	/** The Constant COLUMN_TITLE_PERCENT_A. */
	private static final String COLUMN_TITLE_PERCENT_A = "% A";
	/** The Constant COLUMN_TITLE_TOTAL_PORTALES_A. */
	private static final String COLUMN_TITLE_TOTAL_PORTALES_A = "TOTAL PORTALES A";
	/** The Constant COLUMN_TITLE_NOTA_MEDIA_AA. */
	private static final String COLUMN_TITLE_NOTA_MEDIA_AA = "NOTA MEDIA AA";
	/** The Constant COLUMN_TITLE_AA. */
	private static final String COLUMN_TITLE_AA = "% AA";
	/** The Constant COLUMN_TITLE_TOTAL_PORTALES_AA. */
	private static final String COLUMN_TITLE_TOTAL_PORTALES_AA = "TOTAL PORTALES AA";
	/** The Constant COLUMN_TITLE_ORGANISMO. */
	private static final String COLUMN_TITLE_ORGANISMO = "ORGANISMO";
	/** The Constant REGEX_DOT. */
	private static final String REGEX_DOT = "\\.";
	/** The Constant YELLOW_OAW_HTML. */
	private static final String YELLOW_OAW_HTML = "#fee100";
	/** The Constant TC_PREFFIX. */
	private static final String TC_PREFFIX = "TC_";
	/** The Constant AA_PREFFIX. */
	private static final String AA_PREFFIX = "AA_";
	/** The Constant PC_PREFFIX. */
	private static final String PC_PREFFIX = "PC_";
	/** The Constant A_PREFFIX. */
	private static final String A_PREFFIX = "A_";
	/** The Constant NC_PREFFIX. */
	private static final String NC_PREFFIX = "NC_";
	/** The Constant NV_PREFFIX. */
	private static final String NV_PREFFIX = "NV_";
	/** The Constant COMPLIANCE_EVOLUTION_TITLE. */
	private static final String COMPLIANCE_EVOLUTION_TITLE = "Evolución de la Situación de cumplimiento estimada";
	/** The Constant ALLOCATION_EVOLUTION_TITLE. */
	private static final String ALLOCATION_EVOLUTION_TITLE = "Evolución del Nivel de adecuación estimado";
	/** The Constant COMPLIANCE_LEVEL_TITLE. */
	private static final String COMPLIANCE_LEVEL_TITLE = "Situación de cumplimiento estimada";
	/** The Constant ALLOCATION_LEVEL_TITLE. */
	private static final String ALLOCATION_LEVEL_TITLE = "Nivel de adecuación estimado";
	/** The Constant SHEET_IMPROVMENTS_TITLEMEJORAS. */
	private static final String SHEET_IMPROVMENTS_TITLE = "Mejoras";
	/** The Constant EVOLUCIÓN_SOBRE_ITERACIÓN_ANTERIOR_PARTE_FIJA. */
	private static final String PREVIOUS_ITERATION_FIXED_TITLE = "Evolución sobre iteración anterior. Parte fija";
	/** The Constant EVOLUCIÓN_SOBRE_PRIMERA_ITERACIÓN_PARTE_FIJA. */
	private static final String FIRST_ITERATION_FIXED_TITLE = "Evolución sobre primera iteración. Parte fija";
	/** The Constant EVOLUCIÓN_SOBRE_ITERACIÓN_ANTERIOR_TERMINOS_GLOBALES. */
	private static final String PREVIOUS_ITERATION_GLOBAL_TITLE = "Evolución sobre iteración anterior. Terminos globales";
	/** The Constant EVOLUCIÓN_SOBRE_PRIMERA_ITERACIÓN_TERMINOS_GLOBALES. */
	private static final String FIRST_ITERATION_GLOBAL_TITLE = "Evolución sobre primera iteración. Terminos globales";
	/** The Constant NÚMERO_DE_SITIOS_WEB. */
	private static final String NUMBER_OF_SITES = "Número de sitios web";
	/** The Constant PORCENTAJE_DE_SITIOS. */
	private static final String PERCENTAGE_OF_SITES = "Porcentaje de sitios";
	/** The Constant EVOLUTION_OF_THE_COMPLIANCE_SITUATION_TARGETED_FIXED_PART. */
	private static final String EVOLUTION_OF_THE_COMPLIANCE_SITUATION_TARGETED_FIXED_PART = "Evolución de la situación de cumplimiento estinada. Parte fija";
	/** The Constant EVOLUTION_OF_THE_ESTIMATED_ADEQUACY_LEVEL_FIXED_PART. */
	private static final String EVOLUTION_OF_THE_ESTIMATED_ADEQUACY_LEVEL_FIXED_PART = "Evolución del nivel de adecuación estimado. Parte fija";
	/** The Constant EVOLUTION_OF_THE_COMPLIANCE_SITUATION_INTENDED_TO_BE_IMPLEMENTED_IN_GLOBAL_TERMS. */
	private static final String EVOLUTION_OF_THE_COMPLIANCE_SITUATION_INTENDED_TO_BE_IMPLEMENTED_IN_GLOBAL_TERMS = "Evolución de la situación de cumplimiento estimada. Términos globales";
	/** The Constant EVOLUTION_OF_THE_ESTIMATED_ADEQUACY_LEVEL_IN_GLOBAL_TERMS. */
	private static final String EVOLUTION_OF_THE_ESTIMATED_ADEQUACY_LEVEL_IN_GLOBAL_TERMS = "Evolución del nivel de adecuación estimado. Términos globales";
	/** The Constant FILE_4_EVOLUTION_AND_PROGRESS_XLSX_NAME. */
	private static final String FILE_4_EVOLUTION_AND_PROGRESS_XLSX_NAME = "4. Evolutivo y Progreso de Observatorio.xlsx";
	/** The Constant EXPORT_ANNEX_PATH. */
	private static final String EXPORT_ANNEX_PATH = "export.annex.path";
	/** The Constant FILE_3_ITERATION_XLSX_NAME. */
	private static final String FILE_3_ITERATION_RANKING_XLSX_NAME = "3. Iteración Ranking.xlsx";
	/** The Constant FILE_2_EVOLUTION_XLSX_NAME. */
	private static final String FILE_2_ITERATION_XLSX_NAME = "2. Iteración SW.xlsx";
	/** The Constant FILE_1_EVOLUTION_XLSX_NAME. */
	private static final String FILE_1_EVOLUTION_XLSX_NAME = "1. Evolutivo SW.xlsx";
	/** The Constant SHEET_RANKING_COMPLIANCE_NAME. */
	private static final String SHEET_RANKING_COMPLIANCE_NAME = "Ranking Cumplimiento";
	/** The Constant SHEET_RANKING_ALLOCATION_NAME. */
	private static final String SHEET_RANKING_ALLOCATION_NAME = "Ranking Adecuación";
	/** The Constant SHEET_RESULTS_NAME. */
	private static final String SHEET_RESULTS_NAME = "Resultados";
	/** The Constant COMPLIANCE_TOTAL_LITERAL. */
	private static final String COMPLIANCE_TOTAL_LITERAL = "Plenamente conforme";
	/** The Constant COMPLIANCE_PARTIAL_LITERAL. */
	private static final String COMPLIANCE_PARTIAL_LITERAL = "Parcialmente conforme";
	/** The Constant COMPLIANCE_NOT_LITERAL. */
	private static final String COMPLIANCE_NOT_LITERAL = "No conforme";
	/** The Constant ALLOCATION_AA_LITERAL. */
	private static final String ALLOCATION_AA_LITERAL = "AA";
	/** The Constant ALLOCATION_A_LITERAL. */
	private static final String ALLOCATION_A_LITERAL = "A";
	/** The Constant ALLOCATION_NOT_VALID_LITERAL. */
	private static final String ALLOCATION_NOT_VALID_LITERAL = "No válido";
	/** The Constant RANGE_THIRD_COLUMN_RANKING. */
	private static final String RANGE_THIRD_COLUMN_RANKING = "I:I";
	/** The Constant RANGE_SECOND_COLUMN_RANKING. */
	private static final String RANGE_SECOND_COLUMN_RANKING = "F:F";
	/** The Constant RANGE_FIRST_COLUMN_RANKING. */
	private static final String RANGE_FIRST_COLUMN_RANKING = "C:C";
	/** The Constant RANGE_TOTAL_PORTALS_RANKING. */
	private static final String RANGE_TOTAL_PORTALS_RANKING = "M:M";
	/** The Constant EARL_INAPPLICABLE. */
	private static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	private static final String EARL_FAILED = "earl:failed";
	/** The message resources. */
	private static MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
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
	private static Map<SemillaForm, TreeMap<String, ScoreForm>> annexmap;
	/** The evaluation ids. */
	private static List<Long> evaluationIds;
	/** The observatory manager. */
	private static es.gob.oaw.rastreador2.observatorio.ObservatoryManager observatoryManager;
	/** The current evaluation page list. */
	private static List<ObservatoryEvaluationForm> currentEvaluationPageList;
	/** The website ranges. */
	private static List<RangeForm> websiteRanges = new ArrayList<>();
	/** The iteration ranges. */
	private static List<TemplateRangeForm> iterationRanges = new ArrayList<>();
	/** The script engine manager. */
	private static ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	/** The script engine. */
	private static ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

	/**
	 * Generate all annex.
	 *
	 * @param messageResources  the message resources
	 * @param idObs             the id obs
	 * @param idObsExecution    the id obs execution
	 * @param idOperation       the id operation
	 * @param tagsToFilter      the tags to filter
	 * @param tagsToFilterFixed the tags to filter fixed
	 * @param exObsIds          the ex obs ids
	 * @param comparision       the comparision
	 * @throws Exception the exception
	 */
	public static void generateAllAnnex(final MessageResources messageResources, final Long idObs, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter,
			final String[] tagsToFilterFixed, final String[] exObsIds, final List<ComparisionForm> comparision) throws Exception {
		Logger.putLog("Inicio de la generación de anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		Logger.putLog("Obteniendo información para la generación de anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		generateInfo(idObsExecution, exObsIds);
		Logger.putLog("Generando anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		createAnnexPaginas(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPaginasVerifications(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPaginasCriteria(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPortales(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPortalsVerification(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexPortalsCriteria(messageResources, idObsExecution, idOperation, tagsToFilter, exObsIds);
		createAnnexXLSX2(messageResources, idObsExecution, idOperation, tagsToFilter);
		createAnnexXLSX1_Evolution(messageResources, idObsExecution, idOperation, comparision, tagsToFilter);
		createAnnexXLSX_PerDependency(idOperation);
		createAnnexXLSXRanking(messageResources, idObsExecution, idOperation);
		createAnnexProgressEvolutionXLSX(messageResources, idObs, idObsExecution, idOperation, tagsToFilter, tagsToFilterFixed, exObsIds, comparision);
		Logger.putLog("Fin de la generación de anexos", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
	}

	/**
	 * Generate info.
	 *
	 * @param idObsExecution the id obs execution
	 * @param exObsIds       the ex obs ids
	 * @throws Exception    the exception
	 * @throws SQLException the SQL exception
	 */
	private static void generateInfo(final Long idObsExecution, final String[] exObsIds) throws Exception, SQLException {
		annexmap = createAnnexMap(idObsExecution, exObsIds);
		evaluationIds = AnalisisDatos.getEvaluationIdsFromExecutedObservatory(idObsExecution);
		observatoryManager = new es.gob.oaw.rastreador2.observatorio.ObservatoryManager();
		currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(idObsExecution, evaluationIds);
		Connection c = DataBaseManager.getConnection();
		// Fill all execution dates
		fillExecutionDates(idObsExecution, exObsIds, c);
		websiteRanges = RangeDAO.findAll(c);
		iterationRanges = TemplateRangeDAO.findAll(c, idObsExecution);
		DataBaseManager.closeConnection(c);
	}

	/**
	 * Fill execution dates.
	 *
	 * @param idObsExecution the id obs execution
	 * @param exObsIds       the ex obs ids
	 * @param c              the c
	 * @throws SQLException the SQL exception
	 */
	private static void fillExecutionDates(final Long idObsExecution, final String[] exObsIds, Connection c) throws SQLException {
		executionDates = new ArrayList<>();
		final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryFormFromExecution(c, idObsExecution);
		final ObservatorioRealizadoForm executedObservatory = ObservatorioDAO.getFulfilledObservatory(c, observatoryForm.getId(), idObsExecution);
		final List<ObservatorioRealizadoForm> observatoriesList = ObservatorioDAO.getFulfilledObservatories(c, observatoryForm.getId(), Constants.NO_PAGINACION, executedObservatory.getFecha(), false,
				exObsIds);
		for (ObservatorioRealizadoForm obsR : observatoriesList) {
			final String executionDateAux = obsR.getFecha().toString().substring(0, obsR.getFecha().toString().indexOf(" ")).replace("/", "_");
			executionDates.add(executionDateAux);
			executionDatesWithFormat_Valid.add(obsR.getFecha());
		}
	}

	/**
	 * Generate evolution data.
	 *
	 * @param messageResources the message resources
	 * @param idObs            the id obs
	 * @param idObsExecution   the id obs execution
	 * @param tagsToFilter     the tags to filter
	 * @param exObsIds         the ex obs ids
	 * @param comparision      the comparision
	 * @throws Exception the exception
	 */
	public static void generateEvolutionData(final MessageResources messageResources, final Long idObs, final Long idObsExecution, final String[] tagsToFilter, final String[] exObsIds,
			final List<ComparisionForm> comparision) throws Exception {
		// Returns a map of dependencies and values of evolution
		generateInfo(idObsExecution, exObsIds);
		List<UraSendResultForm> uraCustomList = new ArrayList<>();
		// Generate a list of UraSendResultForm with all URA information
		Map<DependenciaForm, List<SemillaForm>> mapSeedByDependencia = new HashMap<>();
		for (Map.Entry<SemillaForm, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
			final SemillaForm semillaForm = semillaEntry.getKey();
			if (semillaForm.getId() != 0 && semillaEntry.getValue().get(executionDatesWithFormat_Valid.get(executionDatesWithFormat_Valid.size() - 1).toString()) != null) {
				for (DependenciaForm dependencia : semillaForm.getDependencias()) {
					List<SemillaForm> seedsByDependency = mapSeedByDependencia.get(dependencia);
					if (seedsByDependency == null) {
						seedsByDependency = new ArrayList<SemillaForm>();
					}
					seedsByDependency.add(semillaForm);
					mapSeedByDependencia.put(dependencia, seedsByDependency);
				}
			}
		}
		// Loop into map
		for (Map.Entry<DependenciaForm, List<SemillaForm>> entry : mapSeedByDependencia.entrySet()) {
			// calculate
			BigDecimal sumLastScore = BigDecimal.ZERO;
			BigDecimal sumPreviousScore = BigDecimal.ZERO;
			for (SemillaForm semillaForm : entry.getValue()) {
				TreeMap<String, ScoreForm> scoreMap = annexmap.get(semillaForm);
				// last score
				BigDecimal lastScore = scoreMap.lastEntry().getValue().getTotalScore();
				BigDecimal previousScore = null;
				// prevoius
				if (comparision != null) {
					// Get previous date by tag
					for (ComparisionForm com : comparision) {
						for (EtiquetaForm label : semillaForm.getEtiquetas()) {
							if (com.getIdTag() == label.getId()) {
								for (Map.Entry<String, ScoreForm> scoreEntry : scoreMap.entrySet()) {
									if (scoreEntry.getKey().startsWith(com.getPrevious())) {
										previousScore = scoreEntry.getValue().getTotalScore();
										break;
									}
								}
							}
						}
					}
				} else {
					// Obtain submap to has esay access to penultimate value
					final NavigableMap<String, ScoreForm> subMap = scoreMap.subMap(scoreMap.firstEntry().getKey(), true, scoreMap.lastEntry().getKey(), false);
					if (!subMap.isEmpty()) {
						previousScore = subMap.lastEntry().getValue().getTotalScore();
					} else {
						previousScore = scoreMap.firstEntry().getValue().getTotalScore();
					}
				}
				if (lastScore != null) {
					sumLastScore = sumLastScore.add(lastScore);
				}
				if (previousScore != null) {
					sumPreviousScore = sumPreviousScore.add(previousScore);
				}
			}
			// Calculate mid
			BigDecimal midLastScores = sumLastScore.divide(new BigDecimal(entry.getValue().size()), RoundingMode.HALF_UP);
			BigDecimal midPreviousScores = sumPreviousScore.divide(new BigDecimal(entry.getValue().size()), RoundingMode.HALF_UP);
			BigDecimal diffMidScores = midLastScores.subtract(midPreviousScores);
			//
			UraSendResultForm uraCustom = new UraSendResultForm();
			uraCustom.setIdUra(entry.getKey().getId());
			uraCustom.setIdObservatoryExecution(idObsExecution);
			uraCustom.setRangeValue(diffMidScores.floatValue());
			uraCustom.setIdRange(0L); // prevents not matching range
			for (TemplateRangeForm range : iterationRanges) {
				String expression = generateRangeJsExpression(diffMidScores, range.getMinValueOperator(), range.getMaxValueOperator(), range.getMinValue(), range.getMaxValue());
				if ((boolean) scriptEngine.eval(expression)) {
					uraCustom.setIdRange(range.getId());
				}
			}
			uraCustomList.add(uraCustom);
		}
		Connection c = DataBaseManager.getConnection();
		// Save config
		ObservatorioDAO.saveConfig(c, idObsExecution, exObsIds, comparision);
		// Save new custom
		UraSendResultDAO.save(c, uraCustomList);
		DataBaseManager.closeConnection(c);
	}

	/**
	 * Generate range js expression.
	 *
	 * @param diffMidScores    the diff mid scores
	 * @param minValueOperator the min value operator
	 * @param maxValueOperator the max value operator
	 * @param minValue         the min value
	 * @param maxValue         the max value
	 * @return the string
	 */
	private static String generateRangeJsExpression(BigDecimal diffMidScores, final String minValueOperator, final String maxValueOperator, final Float minValue, final Float maxValue) {
		String expression = "";
		if (!StringUtils.isEmpty(minValueOperator) && !StringUtils.isEmpty(maxValueOperator)) {
			expression += minValue + "" + minValueOperator + "" + diffMidScores + " && " + diffMidScores + "" + maxValueOperator + "" + maxValue;
		} else if (!StringUtils.isEmpty(minValueOperator) && StringUtils.isEmpty(maxValueOperator)) {
			expression += minValue + "" + minValueOperator + "" + diffMidScores;
		} else if (StringUtils.isEmpty(minValueOperator) && !StringUtils.isEmpty(maxValueOperator)) {
			expression += diffMidScores + "" + maxValueOperator + "" + maxValue;
		}
		return expression;
	}

	/**
	 * Generate email annex.
	 *
	 * @param messageResources the message resources
	 * @param idObs            the id obs
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param exObsIds         the ex obs ids
	 * @param comparision      the comparision
	 * @param tagsToFilter     the tags to filter
	 * @throws Exception the exception
	 */
	public static void generateEmailAnnex(final MessageResources messageResources, final Long idObs, final Long idObsExecution, final Long idOperation, final String[] exObsIds,
			final List<ComparisionForm> comparision, final String[] tagsToFilter) throws Exception {
		generateInfo(idObsExecution, exObsIds);
		createAnnexXLSX1_Evolution(messageResources, idObsExecution, idOperation, comparision, tagsToFilter);
		createAnnexXLSX_PerDependency(idOperation);
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
		Logger.putLog("Generando anexo: " + FILE_3_ITERATION_RANKING_XLSX_NAME, AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		// Clone file 2
		final PropertiesManager pmgr = new PropertiesManager();
		final File originalWb = new File(pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_ANNEX_PATH) + idOperation + File.separator + FILE_2_ITERATION_XLSX_NAME);
		final FileOutputStream fos = new FileOutputStream(pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_ANNEX_PATH) + idOperation + File.separator + FILE_3_ITERATION_RANKING_XLSX_NAME);
		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(originalWb));
		final XSSFSheet resultSheet = wb.getSheet(SHEET_RESULTS_NAME);
		final String sheetnameAllocation = SHEET_RANKING_ALLOCATION_NAME;
		final String sheetnameCompliance = SHEET_RANKING_COMPLIANCE_NAME;
		final String[] columnNamesAllocation = new String[] { COLUMN_TITLE_ORGANISMO, COLUMN_TITLE_TOTAL_PORTALES_AA, COLUMN_TITLE_AA, COLUMN_TITLE_NOTA_MEDIA_AA, COLUMN_TITLE_TOTAL_PORTALES_A,
				COLUMN_TITLE_PERCENT_A, COLUMN_TITLE_NOTA_MEDIA_A, COLUMN_TITLE_TOTAL_PORTALES_NV, COLUMN_TITLE_NV, COLUMN_TITLE_NOTA_MEDIA_NV, COLUMN_TITLE_NO_CUMPLEN, COLUMN_TITLE_TOTAL_PORTALES };
		// In order left to right
		final String[] columnResultsAllocation = new String[] { "R", "Q", "P" };
		final String[] columnResultsCompliance = new String[] { "U", "T", "S" };
		final String[] columnNamesCompliance = new String[] { COLUMN_TITLE_ORGANISMO, COLUMN_TITLE_TOTAL_PORTALES_TC, COLUMN_TITLE_PERCENT_TC, COLUMN_TITLE_NOTA_MEDIA_TC,
				COLUMN_TITLE_TOTAL_PORTALES_PC, COLUMN_TITLE_PERCENT_PC, COLUMN_TITLE_NOTA_MEDIA_PC, COLUMN_TITLE_TOTAL_PORTALES_NC, COLUMN_TITLE_PERCENT_NC, COLUMN_TITLE_NOTA_MEDIA_NC,
				COLUMN_TITLE_NO_CONFORMES, COLUMN_TITLE_TOTAL_PORTALES };
		// Add table with filters
		CellReference ref = new CellReference(A1);
		CellReference topLeft = new CellReference(resultSheet.getRow(ref.getRow()).getCell(ref.getCol()));
		ref = new CellReference("U" + resultSheet.getLastRowNum());
		CellReference bottomRight = new CellReference(resultSheet.getRow(ref.getRow()).getCell(ref.getCol()));
		// Datatable requires at least 2 rows
		if (bottomRight.getRow() - topLeft.getRow() >= 2) {
			AreaReference tableArea = wb.getCreationHelper().createAreaReference(topLeft, bottomRight);
			XSSFTable dataTable = resultSheet.createTable(tableArea);
			dataTable.setDisplayName("TableResults");
			// this sets auto filters
			dataTable.getCTTable().addNewAutoFilter().setRef(tableArea.formatAsString());
		}
		XlsxUtils xlsxUtils = new XlsxUtils(wb);
		addRankingSheet(wb, resultSheet, sheetnameAllocation, columnNamesAllocation, columnResultsAllocation, "TableRankingA", xlsxUtils);
		addRankingSheet(wb, resultSheet, sheetnameCompliance, columnNamesCompliance, columnResultsCompliance, "TableRankingC", xlsxUtils);
		// Conditional formatting
		addConditionaFormatting(wb.getSheet(sheetnameAllocation));
		addConditionaFormatting(wb.getSheet(sheetnameCompliance));
		// Recalculate formulas
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		wb.write(fos);
		fos.close();
	}

	/**
	 * Creates the annex progress evolution XLSX.
	 *
	 * @param messageResources  the message resources
	 * @param idObs             the id obs
	 * @param idObsExecution    the id obs execution
	 * @param idOperation       the id operation
	 * @param tagsToFilter      the tags to filter
	 * @param tagsToFilterFixed the tags to filter fixed
	 * @param exObsIds          the ex obs ids
	 * @param comparision       the comparision
	 * @throws Exception the exception
	 */
	private static void createAnnexProgressEvolutionXLSX(final MessageResources messageResources, final Long idObs, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter,
			final String[] tagsToFilterFixed, final String[] exObsIds, final List<ComparisionForm> comparision) throws Exception {
		Logger.putLog("Generando anexo: " + FILE_4_EVOLUTION_AND_PROGRESS_XLSX_NAME, AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, FILE_4_EVOLUTION_AND_PROGRESS_XLSX_NAME)) {
			final XSSFWorkbook wb = new XSSFWorkbook();
			final XSSFSheet globalSheet = wb.createSheet("Global");
			// First sheet global part
			XlsxUtils xlsxUtils = new XlsxUtils(wb);
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioUNEEN2019Utils.resultEvolutionData(idObs, idObsExecution, tagsToFilter, exObsIds);
			generateGlobalProgressEvolutionSheet(globalSheet, messageResources, idObs, idObsExecution, idOperation, tagsToFilter, exObsIds, pageObservatoryMap, null,
					EVOLUTION_OF_THE_ESTIMATED_ADEQUACY_LEVEL_IN_GLOBAL_TERMS, EVOLUTION_OF_THE_COMPLIANCE_SITUATION_INTENDED_TO_BE_IMPLEMENTED_IN_GLOBAL_TERMS, null, null, false, 0, xlsxUtils);
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMapFixed = ResultadosAnonimosObservatorioUNEEN2019Utils.resultEvolutionData(idObs, idObsExecution, tagsToFilterFixed,
					exObsIds);
			generateGlobalProgressEvolutionSheet(globalSheet, messageResources, idObs, idObsExecution, idOperation, tagsToFilter, exObsIds, pageObservatoryMapFixed, null,
					EVOLUTION_OF_THE_ESTIMATED_ADEQUACY_LEVEL_FIXED_PART, EVOLUTION_OF_THE_COMPLIANCE_SITUATION_TARGETED_FIXED_PART, null, null, false, 40, xlsxUtils);
			// N sheets by segment
			final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, idObsExecution);
			for (CategoriaForm category : categories) {
				final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMapCat = ResultadosAnonimosObservatorioUNEEN2019Utils.resultEvolutionCategoryData(idObs, idObsExecution,
						Long.valueOf(category.getId()), tagsToFilter, exObsIds);
				final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMapCatFixed = ResultadosAnonimosObservatorioUNEEN2019Utils.resultEvolutionCategoryData(idObs, idObsExecution,
						Long.valueOf(category.getId()), tagsToFilterFixed, exObsIds);
				if (pageObservatoryMapCat != null) {
					String currentCategory = category.getName().substring(0, Math.min(category.getName().length(), 31));
					final XSSFSheet categorySheet = wb.createSheet(currentCategory);
					generateGlobalProgressEvolutionSheet(categorySheet, messageResources, idObs, idObsExecution, idOperation, tagsToFilter, exObsIds, pageObservatoryMapCat, pageObservatoryMapCatFixed,
							messageResources.getMessage("annex.xlsx.global.progress.allocation.segment.global.title", new String[] { category.getName() }),
							messageResources.getMessage("annex.xlsx.global.progress.compliance.segment.global.title", new String[] { category.getName() }),
							messageResources.getMessage("annex.xlsx.global.progress.allocation.segment.fixed.title", new String[] { category.getName() }),
							messageResources.getMessage("annex.xlsx.global.progress.compliance.segment.fixed.title", new String[] { category.getName() }), true, 0, xlsxUtils);
				}
			}
			// Final sheet comparision
			final Connection conn = DataBaseManager.getConnection();
			generateSummaryProgression(wb, conn, idOperation, comparision, tagsToFilterFixed);
			DataBaseManager.closeConnection(conn);
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			wb.write(writer);
			wb.close();
		} catch (Exception e) {
			Logger.putLog("Error al generar el anexo: " + FILE_4_EVOLUTION_AND_PROGRESS_XLSX_NAME, AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			throw e;
		}
	}

	/**
	 * Generate summary progression.
	 *
	 * @param wb                the wb
	 * @param conn              the conn
	 * @param idOperation       the id operation
	 * @param comparision       the comparision
	 * @param tagsToFilterFixed the tags to filter fixed
	 * @throws SQLException    the SQL exception
	 * @throws ScriptException the script exception
	 */
	private static void generateSummaryProgression(final XSSFWorkbook wb, final Connection conn, final Long idOperation, final List<ComparisionForm> comparision, final String[] tagsToFilterFixed)
			throws SQLException, ScriptException {
		// Loop to insert puntuation evolution compare with previous.
		// To select comparision column in comparision object, check if seed has tagId of comparision to select column by date
		SummaryEvolution globalSummaryFirst = new SummaryEvolution(websiteRanges);
		SummaryEvolution globalSummaryPrevious = new SummaryEvolution(websiteRanges);
		SummaryEvolution fixedSummaryFirst = new SummaryEvolution(websiteRanges);
		SummaryEvolution fixedSummaryPrevious = new SummaryEvolution(websiteRanges);
		for (Map.Entry<SemillaForm, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
			// Apply websites ranges
			countEvolution(conn, comparision, globalSummaryPrevious, semillaEntry, false, null);
			countEvolution(conn, comparision, globalSummaryFirst, semillaEntry, true, null);
			countEvolution(conn, comparision, fixedSummaryPrevious, semillaEntry, false, tagsToFilterFixed);
			countEvolution(conn, comparision, fixedSummaryFirst, semillaEntry, true, tagsToFilterFixed);
		}
		XSSFSheet improvementSheet = wb.createSheet(SHEET_IMPROVMENTS_TITLE);
		int currentRow = 10;
		generateSummaryData(globalSummaryFirst, improvementSheet, currentRow, FIRST_ITERATION_GLOBAL_TITLE);
		currentRow = 25;
		generateSummaryData(globalSummaryPrevious, improvementSheet, currentRow, PREVIOUS_ITERATION_GLOBAL_TITLE);
		currentRow = 40;
		generateSummaryData(fixedSummaryFirst, improvementSheet, currentRow, FIRST_ITERATION_FIXED_TITLE);
		currentRow = 55;
		generateSummaryData(fixedSummaryPrevious, improvementSheet, currentRow, PREVIOUS_ITERATION_FIXED_TITLE);
		// Add a legend with custom text
		XSSFDrawing draw = improvementSheet.createDrawingPatriarch();
		XSSFTextBox tb1 = draw.createTextbox(new XSSFClientAnchor(0, 0, 0, 0, 0, 0, 10, 6));
		tb1.setLineStyleColor(0, 0, 0);
		tb1.setLineWidth(1);
		Color col = Color.WHITE;
		tb1.setFillColor(col.getRed(), col.getGreen(), col.getBlue());
		StringBuilder sb = new StringBuilder("Los rangos en base a los cuales se ha calculado la evolución de la puntuación de los sitios web son los siguientes:");
		sb.append("\n");
		for (RangeForm range : websiteRanges) {
			sb.append(range.getName() + ": " + (range.getMinValue() != null ? range.getMinValue() : "") + " " + range.getMinValueOperator() + " x " + range.getMaxValueOperator() + " "
					+ (range.getMaxValue() != null ? range.getMaxValue() : "") + "\n");
		}
		// websiteRanges
		XSSFRichTextString address = new XSSFRichTextString(sb.toString());
		tb1.setText(address);
		CTTextCharacterProperties rpr = tb1.getCTShape().getTxBody().getPArray(0).getRArray(0).getRPr();
		rpr.setSz(1000); // 9 pt
		col = Color.BLACK;
		rpr.addNewSolidFill().addNewSrgbClr().setVal(new byte[] { (byte) col.getRed(), (byte) col.getGreen(), (byte) col.getBlue() });
	}

	/**
	 * Generate summary data.
	 *
	 * @param globalSummary    the global summary previous
	 * @param improvementSheet the improvement sheet
	 * @param currentRow       the current row
	 * @param title            the title
	 */
	private static void generateSummaryData(SummaryEvolution globalSummary, XSSFSheet improvementSheet, int currentRow, final String title) {
		Row r;
		Cell c;
		XlsxUtils xlsxUtils = new XlsxUtils(improvementSheet.getWorkbook());
		final CellStyle whiteCell = xlsxUtils.getCellStyleByName(XlsxUtils.WHITE_BACKGROUND_BLACK_BOLD10);
		final CellStyle headerStyle = xlsxUtils.getCellStyleByName(XlsxUtils.BLUE_BACKGROUND_BLACK_BOLD10_CENTER);
		final CellStyle headerStyleLeft = xlsxUtils.getCellStyleByName(XlsxUtils.BLUE_BACKGROUND_BLACK_BOLD10_LEFT);
		final CellStyle percentStyle = xlsxUtils.getCellStyleByName(XlsxUtils.NORMAL_PERCENT11_CENTER_STYLE);
		// If all values a 0, the graphic fails
		if (globalSummary.isAllZero()) {
			r = improvementSheet.createRow(currentRow);
			c = r.createCell(0);
			c.setCellValue(title);
			currentRow++;
			r = improvementSheet.createRow(currentRow);
			c = r.createCell(0);
			c.setCellValue("Sin resultados");
		} else {
			r = improvementSheet.createRow(currentRow);
			c = r.createCell(0);
			c.setCellValue(title);
			c.setCellStyle(whiteCell);
			int col = 1;
			for (Map.Entry<RangeForm, Integer> range : globalSummary.getRangeMaps().entrySet()) {
				c = r.createCell(col);
				col++;
			}
			improvementSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow, 0, col - 1));
			currentRow++;
			// Headers
			r = improvementSheet.createRow(currentRow);
			col = 1;
			for (Map.Entry<RangeForm, Integer> range : globalSummary.getRangeMaps().entrySet()) {
				c = r.createCell(col);
				c.setCellValue(range.getKey().getName());
				c.setCellStyle(headerStyle);
				improvementSheet.autoSizeColumn(col);
				col++;
			}
			currentRow++;
			// Percentages
			r = improvementSheet.createRow(currentRow);
			c = r.createCell(0);
			c.setCellValue(PERCENTAGE_OF_SITES);
			c.setCellStyle(headerStyleLeft);
			Integer total = 0;
			for (Map.Entry<RangeForm, Integer> range : globalSummary.getRangeMaps().entrySet()) {
				total += range.getValue();
			}
			col = 1;
			for (Map.Entry<RangeForm, Integer> range : globalSummary.getRangeMaps().entrySet()) {
				c = r.createCell(col);
				c.setCellValue((double) range.getValue() / (double) total);
				c.setCellStyle(percentStyle);
				col++;
			}
			currentRow++;
			// Number of sites
			r = improvementSheet.createRow(currentRow);
			c = r.createCell(0); // Percentaje
			c.setCellValue(NUMBER_OF_SITES);
			c.setCellStyle(headerStyleLeft);
			col = 1;
			for (Map.Entry<RangeForm, Integer> range : globalSummary.getRangeMaps().entrySet()) {
				c = r.createCell(col);
				c.setCellValue(range.getValue());
				improvementSheet.autoSizeColumn(col);
				col++;
			}
			// Graphic
			XSSFDrawing drawing = improvementSheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 7, currentRow - 2, 16, currentRow + 8);
			XSSFChart chart = drawing.createChart(anchor);
			chart.setTitleText(title);
			chart.setTitleOverlay(false);
			XDDFChartLegend legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.TOP_RIGHT);
			XDDFDataSource<String> labels = XDDFDataSourcesFactory.fromStringCellRange(improvementSheet, new CellRangeAddress(currentRow - 2, currentRow - 2, 1, globalSummary.getRangeMaps().size()));
			XDDFNumericalDataSource<Double> values = XDDFDataSourcesFactory.fromNumericCellRange(improvementSheet,
					new CellRangeAddress(currentRow - 1, currentRow - 1, 1, globalSummary.getRangeMaps().size()));
			XDDFChartData data = chart.createData(ChartTypes.PIE3D, null, null);
			data.setVaryColors(true);
			data.addSeries(labels, values);
			// show labels
			if (!chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).isSetDLbls()) {
				chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDLbls();
			}
			chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowLegendKey().setVal(false);
			chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowPercent().setVal(true);
			chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowLeaderLines().setVal(false);
			chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowVal().setVal(false);
			chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowCatName().setVal(false);
			chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowSerName().setVal(false);
			chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDLbls().addNewShowBubbleSize().setVal(false);
			// angle
			chart.getCTChart().addNewView3D().addNewRotX().setVal((byte) 25);
			// iterate ranges to apply colrs
			int idx = 0;
			for (Map.Entry<RangeForm, Integer> range : globalSummary.getRangeMaps().entrySet()) {
				chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(idx);
				chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDPtList().get(idx).addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(hex2Rgb(range.getKey().getColor()));
				idx++;
			}
			chart.plot(data);
			setRoundedCorners(chart, false);
		}
	}

	/**
	 * Count evolution.
	 *
	 * @param c                 the c
	 * @param comparision       the comparision
	 * @param globalSummary     the global summary
	 * @param semillaEntry      the semilla entry
	 * @param isFirst           the is first
	 * @param tagsToFilterFixed the tags to filter fixed
	 * @throws SQLException    the SQL exception
	 * @throws ScriptException the script exception
	 */
	private static void countEvolution(final Connection c, final List<ComparisionForm> comparision, SummaryEvolution globalSummary, Map.Entry<SemillaForm, TreeMap<String, ScoreForm>> semillaEntry,
			final boolean isFirst, final String[] tagsToFilterFixed) throws SQLException, ScriptException {
		final SemillaForm semillaForm = semillaEntry.getKey();
		Map<RangeForm, Integer> rangeMap = globalSummary.getRangeMaps();
		if (semillaForm != null && semillaForm.getId() != 0) {
			// last puntuiaction
			Map.Entry<String, ScoreForm> entry = semillaEntry.getValue().lastEntry();
			BigDecimal lastScore = entry.getValue().getTotalScore();
			if (comparision != null) {
				// Get previous date by tag
				for (ComparisionForm com : comparision) {
					// exclude if selected tags fixed
					if (tagsToFilterFixed != null && tagsToFilterFixed.length > 0) {
						if (!Arrays.asList(tagsToFilterFixed).contains(String.valueOf(com.getIdTag()))) {
							continue;
						}
					}
					for (EtiquetaForm label : semillaForm.getEtiquetas()) {
						if (tagsToFilterFixed != null && tagsToFilterFixed.length > 0 && !hasTags(semillaForm, tagsToFilterFixed)) {
							continue;
						}
						if (com.getIdTag() == label.getId()) {
							BigDecimal scoreComparision = BigDecimal.ZERO;
							final String key = isFirst ? com.getFirst() : com.getPrevious();
							for (Map.Entry<String, ScoreForm> entry2 : semillaEntry.getValue().entrySet()) {
								if (entry2.getKey().startsWith(key)) {
									scoreComparision = scoreComparision.add(entry2.getValue().getTotalScore());
									break;
								}
							}
							BigDecimal diffScore = lastScore.subtract(scoreComparision);
							for (RangeForm range : websiteRanges) {
								String expression = generateRangeJsExpression(diffScore, range.getMinValueOperator(), range.getMaxValueOperator(), range.getMinValue(), range.getMaxValue());
								if ((boolean) scriptEngine.eval(expression)) {
									Integer val = rangeMap.get(range);
									if (val != null) {
										val++;
									} else {
										val = 1;
									}
									rangeMap.put(range, val);
									break;
								}
							}
							break;
						}
					}
				}
			} else {
				boolean include = true;
				// exclude if selected tags fixed
				if (tagsToFilterFixed != null && tagsToFilterFixed.length > 0) {
					include = false;
					for (EtiquetaForm label : semillaForm.getEtiquetas()) {
						if (Arrays.asList(tagsToFilterFixed).contains(String.valueOf(label.getId()))) {
							include = true;
							break;
						}
					}
				}
				if (include) {
					BigDecimal scoreComparision;
					if (isFirst) {
						scoreComparision = semillaEntry.getValue().firstEntry().getValue().getTotalScore();
					} else {
						// Obtain submap to has esay access to penultimate value
						final NavigableMap<String, ScoreForm> subMap = semillaEntry.getValue().subMap(semillaEntry.getValue().firstEntry().getKey(), true, semillaEntry.getValue().lastEntry().getKey(),
								false);
						if (!subMap.isEmpty()) {
							scoreComparision = subMap.lastEntry().getValue().getTotalScore();
						} else {
							scoreComparision = semillaEntry.getValue().firstEntry().getValue().getTotalScore();
						}
					}
					BigDecimal diffScore = lastScore.subtract(scoreComparision);
					for (RangeForm range : websiteRanges) {
						String expression = generateRangeJsExpression(diffScore, range.getMinValueOperator(), range.getMaxValueOperator(), range.getMinValue(), range.getMaxValue());
						if ((boolean) scriptEngine.eval(expression)) {
							Integer val = rangeMap.get(range);
							if (val != null) {
								val++;
							} else {
								val = 1;
							}
							rangeMap.put(range, val);
						}
					}
				}
			}
		}
	}

	/**
	 * Generate global progress evolution sheet.
	 *
	 * @param sheet                        the sheet
	 * @param messageResources             the message resources
	 * @param idObs                        the id obs
	 * @param idObsExecution               the id obs execution
	 * @param idOperation                  the id operation
	 * @param tagsToFilter                 the tags to filter
	 * @param exObsIds                     the ex obs ids
	 * @param pageObservatoryMap           the page observatory map
	 * @param pageObservatoryMapFixed      the page observatory map fixed
	 * @param titleAllocationGraphicGlobal the title allocation graphic
	 * @param titleComplianceGraphicGlobal the title compliance graphic global
	 * @param titleAllocationGraphicFixed  the title allocation graphic fixed (only if generateFixedGraphics is true is required)
	 * @param titleComplianceGrpahicFixed  the title compliance grpahic fixed (only if generateFixedGraphics is true is required)
	 * @param generateFixedGraphics        the is segment
	 * @param initRow                      the init row
	 * @param xlsxUtils                    the xlsx utils
	 */
	private static void generateGlobalProgressEvolutionSheet(final XSSFSheet sheet, final MessageResources messageResources, final Long idObs, final Long idObsExecution, final Long idOperation,
			final String[] tagsToFilter, final String[] exObsIds, final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMapFixed, final String titleAllocationGraphicGlobal, final String titleComplianceGraphicGlobal,
			final String titleAllocationGraphicFixed, final String titleComplianceGrpahicFixed, final boolean generateFixedGraphics, final int initRow, final XlsxUtils xlsxUtils) {
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, KEY_DATE_FORMAT_EVOLUTION));
		final Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioUNEEN2019Utils.getEvolutionObservatoriesSitesByType(String.valueOf(idObs),
				String.valueOf(idObsExecution), pageObservatoryMap, exObsIds);
		int rowCount = initRow;
		int firstRow = initRow + 1;
		// Allocation
		// date + suiability
		final Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A, df);
		final Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA, df);
		final Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV, df);
		// Styles
		final CellStyle headerStyle = xlsxUtils.getCellStyleByName(XlsxUtils.BLUE_BACKGROUND_BLACK_BOLD10_CENTER);
		final CellStyle headerStyleLeft = xlsxUtils.getCellStyleByName(XlsxUtils.BLUE_BACKGROUND_WHITE_BOLD10_LEFT);
		final CellStyle percentBoldStyle = xlsxUtils.getCellStyleByName(XlsxUtils.BOLD_PERCENT11_CENTER_STYLE);
		final CellStyle percentStyle = xlsxUtils.getCellStyleByName(XlsxUtils.NORMAL_PERCENT11_CENTER_STYLE);
		// Table allcation and pie chart
		Row r;
		Cell c;
		// Headers
		r = sheet.createRow(rowCount);
		c = r.createCell(1);
		c.setCellValue(ALLOCATION_NOT_VALID_LITERAL);
		c.setCellStyle(headerStyle);
		c = r.createCell(2);
		c.setCellValue(ALLOCATION_A_LITERAL);
		c.setCellStyle(headerStyle);
		c = r.createCell(3);
		c.setCellValue(ALLOCATION_AA_LITERAL);
		c.setCellStyle(headerStyle);
		rowCount++;
		for (Entry<String, BigDecimal> entry : resultDataNV.entrySet()) {
			r = sheet.createRow(rowCount);
			c = r.createCell(0);
			c.setCellValue(entry.getKey());
			c.setCellStyle(headerStyleLeft);
			c = r.createCell(1);
			c.setCellValue(entry.getValue().doubleValue() / 100);
			c.setCellStyle(percentStyle);
			c = r.createCell(2);
			c.setCellValue(resultDataA.get(entry.getKey()).doubleValue() / 100);
			c.setCellStyle(percentStyle);
			c = r.createCell(3);
			c.setCellStyle(percentStyle);
			c.setCellValue(resultDataAA.get(entry.getKey()).doubleValue() / 100);
			rowCount++;
		}
		generateStackedBarGraphic(sheet, firstRow, rowCount, 1, 3, titleAllocationGraphicGlobal);
		rowCount += 15;
		// Compliance
		// date + suiability
		final Map<Date, Map<Long, Map<String, Integer>>> evolutionResultC = ResultadosAnonimosObservatorioUNEEN2019Utils.getEvolutionObservatoriesSitesByCompliance(String.valueOf(idObs),
				String.valueOf(idObsExecution), pageObservatoryMap, exObsIds);
		final Map<String, BigDecimal> resultDataPC = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteCompliance(evolutionResultC, Constants.OBS_COMPILANCE_PARTIAL, df);
		final Map<String, BigDecimal> resultDataTC = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteCompliance(evolutionResultC, Constants.OBS_COMPILANCE_FULL, df);
		final Map<String, BigDecimal> resultDataNC = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteCompliance(evolutionResultC, Constants.OBS_COMPILANCE_NONE, df);
		// Table compliance and pie chart (Global)
		// Headers
		r = sheet.createRow(rowCount);
		c = r.createCell(1);
		c.setCellValue(COMPLIANCE_NOT_LITERAL);
		c.setCellStyle(headerStyle);
		c = r.createCell(2);
		c.setCellValue(COMPLIANCE_PARTIAL_LITERAL);
		c.setCellStyle(headerStyle);
		c = r.createCell(3);
		c.setCellValue(COMPLIANCE_TOTAL_LITERAL);
		c.setCellStyle(headerStyle);
		rowCount++;
		firstRow = rowCount;
		for (Entry<String, BigDecimal> entry : resultDataNC.entrySet()) {
			r = sheet.createRow(rowCount);
			c = r.createCell(0);
			c.setCellValue(entry.getKey());
			c.setCellStyle(headerStyleLeft);
			c = r.createCell(1);
			c.setCellValue(entry.getValue().doubleValue() / 100);
			c.setCellStyle(percentStyle);
			c = r.createCell(2);
			c.setCellValue(resultDataPC.get(entry.getKey()).doubleValue() / 100);
			c.setCellStyle(percentStyle);
			c = r.createCell(3);
			c.setCellValue(resultDataTC.get(entry.getKey()).doubleValue() / 100);
			c.setCellStyle(percentStyle);
			rowCount++;
		}
		generateStackedBarGraphic(sheet, firstRow, rowCount, 1, 3, titleComplianceGraphicGlobal);
		// Generate 2 extra graphics for fixed part if required
		if (generateFixedGraphics) {
			generateGlobalProgressEvolutionSheet(sheet, messageResources, idObs, idObsExecution, idOperation, tagsToFilter, exObsIds, pageObservatoryMapFixed, null, titleAllocationGraphicFixed,
					titleComplianceGrpahicFixed, null, null, false, rowCount + 15, xlsxUtils);
		}
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
	}

	/**
	 * Generate stacked bar graphic.
	 *
	 * @param sheet       the sheet
	 * @param firstRow    the first row
	 * @param lastRow     the last row
	 * @param firstColumn the first column
	 * @param lastColumn  the last column
	 * @param title       the title
	 */
	private static void generateStackedBarGraphic(final XSSFSheet sheet, final int firstRow, final int lastRow, final int firstColumn, final int lastColumn, final String title) {
		XDDFDataSource date = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(firstRow, lastRow - 1, 0, 0));
		XDDFNumericalDataSource<Double> lowLevel = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, lastRow - 1, 1, 1));
		XDDFNumericalDataSource<Double> mediumLevel = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, lastRow - 1, 2, 2));
		XDDFNumericalDataSource<Double> highLevel = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(firstRow, lastRow - 1, 3, 3));
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		// Graphic position
		final int calculatedWidth = (lastColumn * 2) + ((lastRow - firstRow) * 2);
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, lastColumn * 2, firstRow,
				(lastRow - firstRow) > 0 ? (calculatedWidth < 7 ? (lastColumn * 2) + 7 : (lastColumn * 2) + calculatedWidth) : (lastColumn * 2) + 7, lastRow + 10);
		XSSFChart chart = drawing.createChart(anchor);
		chart.setTitleText(title);
		// set "the title overlays the plot area" to false explicitly
		((XSSFChart) chart).getCTChart().getTitle().addNewOverlay().setVal(false);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.RIGHT);
		// bar chart
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		// category axis crosses the value axis between the strokes and not midpoint the
		// strokes
		leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
		XDDFChartData.Series series1 = data.addSeries(date, lowLevel);
		series1.setTitle("lowLevel", new CellReference(sheet.getSheetName(), firstRow - 1, 1, true, true));
		XDDFChartData.Series series2 = data.addSeries(date, mediumLevel);
		series2.setTitle("mediumLevel", new CellReference(sheet.getSheetName(), firstRow - 1, 2, true, true));
		XDDFChartData.Series series3 = data.addSeries(date, highLevel);
		series3.setTitle("highLevel", new CellReference(sheet.getSheetName(), firstRow - 1, 3, true, true));
		chart.plot(data);
		XDDFBarChartData bar = (XDDFBarChartData) data;
		bar.setBarDirection(BarDirection.COL);
		// looking for "Stacked Bar Chart"? uncomment the following line
		bar.setBarGrouping(BarGrouping.STACKED);
		// correcting the overlap so bars really are stacked and not side by side
		chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal((byte) 100);
		// limits of bars
		chart.getCTChart().getPlotArea().getValAxArray(0).getScaling().addNewMax().setVal(1);// 100%
		chart.getCTChart().getPlotArea().getValAxArray(0).getScaling().addNewMin().setVal(0);
		solidFillSeries(data, 0, PresetColor.RED);
		solidFillSeries(data, 1, PresetColor.YELLOW);
		solidFillSeries(data, 2, PresetColor.GREEN);
		// add data labels
		for (int s = 0; s < 3; s++) {
			chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).addNewDLbls();
			chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(new byte[] { (byte) 255, (byte) 255, (byte) 255 });
			chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewDLblPos().setVal(org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos.CTR);
			chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowVal().setVal(true);
			chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowLegendKey().setVal(false);
			chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowCatName().setVal(false);
			chart.getCTChart().getPlotArea().getBarChartArray(0).getSerArray(s).getDLbls().addNewShowSerName().setVal(false);
		}
		setRoundedCorners(chart, false);
	}

	/**
	 * Sets the rounded corners.
	 *
	 * @param chart  the chart
	 * @param setVal the set val
	 */
	private static void setRoundedCorners(XSSFChart chart, boolean setVal) {
		if (chart.getCTChartSpace().getRoundedCorners() == null)
			chart.getCTChartSpace().addNewRoundedCorners();
		chart.getCTChartSpace().getRoundedCorners().setVal(setVal);
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
	 * @param xlsxUtils     the xlsx utils
	 */
	private static void addRankingSheet(XSSFWorkbook wb, final XSSFSheet resultSheet, final String sheetname, final String[] columnNames, final String[] columnResults, final String tableName,
			final XlsxUtils xlsxUtils) {
		final String resultColumnDependecy = "F";
		final XSSFDataFormat format = wb.createDataFormat();
		final XSSFSheet tmpSheet = wb.createSheet(sheetname);
		wb.setSheetOrder(sheetname, 0);
		XSSFRow row;
		XSSFCell cell;
		int rowIndex = 0;
		int columnIndex = 1;
		final CellStyle dataStyleBold = xlsxUtils.getCellStyleByName(XlsxUtils.BOLD_DATA11_CENTER_STYLE);
		final CellStyle dataStyleNormal = xlsxUtils.getCellStyleByName(XlsxUtils.WHITE_BACKGROUNT_NORMAL11_DECIMAL_CENTER);
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
			cell.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.BOLD_DATA11_LEFT_STYLE));
			rowIndex++;
			numOfDependencies++;
		}
		rowIndex = 1;
		try {
			for (int i = 0; i < numOfDependencies; i++) {
				int cellCount = 2;
				Row r = tmpSheet.getRow(rowIndex + i);
				/******** AA/TC **********/
				cellCount = generateColumnCount(columnResults, resultColumnDependecy, dataStyleBold, cellCount, r, 0);
				cellCount = generateColumnPercent(dataStyleBold, format, cellCount, r, RANGE_TOTAL_PORTALS_RANKING, RANGE_FIRST_COLUMN_RANKING, xlsxUtils);
				cellCount = generetaColumnAverage(columnResults, resultColumnDependecy, dataStyleNormal, format, cellCount, r, 0, RANGE_FIRST_COLUMN_RANKING);
				/******** A/PC **********/
				cellCount = generateColumnCount(columnResults, resultColumnDependecy, dataStyleBold, cellCount, r, 1);
				cellCount = generateColumnPercent(dataStyleBold, format, cellCount, r, RANGE_TOTAL_PORTALS_RANKING, RANGE_SECOND_COLUMN_RANKING, xlsxUtils);
				cellCount = generetaColumnAverage(columnResults, resultColumnDependecy, dataStyleNormal, format, cellCount, r, 1, RANGE_SECOND_COLUMN_RANKING);
				/******** NV/NC **********/
				cellCount = generateColumnCount(columnResults, resultColumnDependecy, dataStyleBold, cellCount, r, 2);
				cellCount = generateColumnPercent(dataStyleBold, format, cellCount, r, RANGE_TOTAL_PORTALS_RANKING, RANGE_THIRD_COLUMN_RANKING, xlsxUtils);
				cellCount = generetaColumnAverage(columnResults, resultColumnDependecy, dataStyleNormal, format, cellCount, r, 2, RANGE_THIRD_COLUMN_RANKING);
				/******** SUMS **********/
				c = r.createCell(cellCount);
				c.setCellFormula("J:J+G:G");
				c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.BOLD_PERCENT11_CENTER_STYLE));
				cellCount++;
				/***/
				c = r.createCell(cellCount);
				c.setCellStyle(dataStyleBold);
				c.setCellFormula("C:C+F:F+I:I");
				cellCount++;
			}
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
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.BLUE_BACKGROUND_WHITE_BOLD16));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.GREEN_BACKGROUND_WHITE_BOLD16));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.GREEN_BACKGROUND_WHITE_BOLD16));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.GREEN_BACKGROUND_WHITE_BOLD16));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.YELLOW_BACKGROUND_BLACK10));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.YELLOW_BACKGROUND_BLACK10));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.YELLOW_BACKGROUND_BLACK10));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.RED_BACKGROUNF_BLACK_BOLD10));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.RED_BACKGROUNF_BLACK_BOLD10));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.RED_BACKGROUNF_BLACK_BOLD10));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.ORANGE_BACKGROUND_BLACK_BOLD10));
			c = row.getCell(columnIndex++);
			c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.BLUE_BACKGROUND_BLACK_BOLD10));
			// Table with autofilter
			CellReference ref = new CellReference(B1);
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
			ref = new CellReference(A2);
			if (rankingSheet.getRow(ref.getRow()) != null) {
				c = rankingSheet.getRow(ref.getRow()).createCell(ref.getCol());
				c.setCellValue(RANKING_1ST);
				c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.FIRST_STYLE));
			}
			ref = new CellReference(A3);
			if (rankingSheet.getRow(ref.getRow()) != null) {
				c = rankingSheet.getRow(ref.getRow()).createCell(ref.getCol());
				c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.SECOND_STYLE));
				c.setCellValue(RANKING_2ND);
			}
			ref = new CellReference(A4);
			if (rankingSheet.getRow(ref.getRow()) != null) {
				c = rankingSheet.getRow(ref.getRow()).createCell(ref.getCol());
				c.setCellValue(RANKING_3RD);
				c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.THIRD_STYLE));
			}
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
	 * @param xlsxUtils             the xlsx utils
	 * @return the int
	 */
	private static int generateColumnPercent(CellStyle boldData11CenterStyle, XSSFDataFormat format, int cellCount, Row r, final String range1, final String range2, final XlsxUtils xlsxUtils) {
		Cell c;
		c = r.createCell(cellCount);
		c.setCellFormula("IF(" + range1 + "<>0," + range2 + "/" + range1 + ",0)");
		c.setCellStyle(xlsxUtils.getCellStyleByName(XlsxUtils.BOLD_PERCENT11_CENTER_STYLE));
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
			Logger.putLog("Generando anexo: anexo-portales-criterios.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			generateXmlPortal(messageResources, idObsExecution, tagsToFilter, c, writer, false, true, true);
		} catch (Exception e) {
			Logger.putLog("Error generar el anexo: anexo-portales-criterios.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
			Logger.putLog("Generando anexo: anexo-paginas.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			generateXmlPages(messageResources, idObsExecution, tagsToFilter, c, writer, false, false);
		} catch (Exception e) {
			Logger.putLog("Error al generar el anexo: anexo-paginas.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
			Logger.putLog("Generando anexo: anexo-paginas-verificaciones.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			generateXmlPages(messageResources, idObsExecution, tagsToFilter, c, writer, true, false);
		} catch (Exception e) {
			Logger.putLog("Error al generar el anexo: anexo-paginas-verificaciones", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
			Logger.putLog("Generando anexo: anexo-paginas-criterios.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			generateXmlPages(messageResources, idObsExecution, tagsToFilter, c, writer, false, true);
		} catch (Exception e) {
			Logger.putLog("Error al generar el anexo: anexo-paginas-criterios.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
										ObservatoryEvaluationForm evaluationForm = currentEvaluationPageList.stream()
												.filter(evaluation -> pageForm.getUrl().equals(evaluation.getUrl()) && evaluation.getSeed().getId().equals(String.valueOf(semillaForm.getId())))
												.findFirst().orElse(null);
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
//										for (WcagEmPointKey wcagEmPoint : WcagEmPointKey.values()) {
										for (String sWcagEmPoint : ALL_WCAG_EM_POINTS) {
											String compliance = "";
											WcagEmPointKey wcagEmPoint = WcagEmPointKey.findByPoint(sWcagEmPoint);
											if (wcagEmPoint != null) {
												// do what you want
												compliance = messageResources.getMessage("modality.pass");
												final ValidationDetails validationDetails = details.get(wcagEmPoint.getWcagEmId());
												if (validationDetails != null) {
													if (EARL_FAILED.equalsIgnoreCase(validationDetails.getResult())) {
														compliance = messageResources.getMessage("modality.fail");
													} else if (EARL_INAPPLICABLE.equalsIgnoreCase(validationDetails.getResult())) {
														compliance = messageResources.getMessage("resultados.anonimos.porc.portales.na");
													}
												} else {
													compliance = "N/T";
												}
											} else {
												compliance = "N/T";
											}
											writeTag(hd, "C_" + sWcagEmPoint.replace(".", "_"), compliance);
										}
									}
									hd.endElement(EMPTY_STRING, EMPTY_STRING, "pagina");
								}
							}
							hd.endElement(EMPTY_STRING, EMPTY_STRING, "paginas");
							if (criterias) {
								writeTag(hd, "nota", messageResources.getMessage("annex.xml.criteria.pages.note"));
							}
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
			Logger.putLog("Generando anexo: anexo-portales.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			generateXmlPortal(messageResources, idObsExecution, tagsToFilter, c, writer, false, false, false);
		} catch (Exception e) {
			Logger.putLog("Error al generar el anexo: anexo-portales.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
			Logger.putLog("Generando anexo: anexo-portales-verificaciones.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			generateXmlPortal(messageResources, idObsExecution, tagsToFilter, c, writer, true, true, false);
		} catch (Exception e) {
			Logger.putLog("Error al generar el anexo: anexo-portales-verificaciones.xml", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
		for (Map.Entry<SemillaForm, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
			final SemillaForm semillaForm = semillaEntry.getKey();
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
						for (String sWcagEmPoint : ALL_WCAG_EM_POINTS) {
							String compliance = "";
							WcagEmPointKey wcagEmPointKey = WcagEmPointKey.findByPoint(sWcagEmPoint);
							// Iterate WCAG Points
							if (wcagCompliance != null && !wcagCompliance.isEmpty()) {
								if (wcagEmPointKey != null) {
									// Iterate evl list to preserve order
									compliance = messageResources.getMessage("observatory.graphic.compilance.green");
									int countFailed = 0;
									int countNA = 0;
									for (ObservatoryEvaluationForm eval : currentEvaluationPageList) {
										Map<String, ValidationDetails> result = wcagCompliance.get(eval.getUrl());
										// if cointain current wcag rule
										if (result.containsKey(wcagEmPointKey.getWcagEmId())) {
											final String validationResult = result.get(wcagEmPointKey.getWcagEmId()).getResult();
											// if one of this has earl:failed, all result marked as failed
											// do what you want
											if (EARL_FAILED.equals(validationResult)) {
												countFailed++;
											} else if (EARL_INAPPLICABLE.equals(validationResult)) {
												countNA++;
											}
										}
									}
									if (countFailed > currentEvaluationPageList.size() / 10) {
										compliance = messageResources.getMessage("observatory.graphic.compilance.red");
									}
									if (countNA == currentEvaluationPageList.size()) {
										compliance = messageResources.getMessage("observatory.graphic.compilance.gray");
									}
								} else {
									compliance = "N/T";
								}
								writeTag(hd, "C_" + sWcagEmPoint.replace(".", "_"), compliance);
							}
						}
					}
					if (criterias) {
						writeTag(hd, "nota", messageResources.getMessage("annex.xml.criteria.note"));
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
	 * @param tagsToFilter     the tags to filter
	 * @throws Exception the exception
	 */
	public static void createAnnexXLSX2(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter) throws Exception {
		ColumnNames = new ArrayList<>();
		Logger.putLog("Generando anexo: " + FILE_2_ITERATION_XLSX_NAME, AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, FILE_2_ITERATION_XLSX_NAME)) {
			final ObservatoryForm observatoryForm = ObservatoryExportManager.getObservatory(idObsExecution);
			final String ObservatoryFormDate = observatoryForm.getDate().substring(0, 10);
			final String[] ColumnNames = new String[] { "id", "nombre", "namecat", "ambito", "complejidad", "depende_de", "semilla", "tematica", "distribucion", "recurrencia", "otros", "paginas",
					"puntuacion_" + ObservatoryFormDate, "adecuacion_" + ObservatoryFormDate, "cumplimiento_" + ObservatoryFormDate, NV_PREFFIX + ObservatoryFormDate, A_PREFFIX + ObservatoryFormDate,
					AA_PREFFIX + ObservatoryFormDate, NC_PREFFIX + ObservatoryFormDate, PC_PREFFIX + ObservatoryFormDate, TC_PREFFIX + ObservatoryFormDate };
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet(SHEET_RESULTS_NAME);
			XSSFRow row;
			XSSFCell cell;
			int rowIndex = 0;
			int columnIndex = 0;
			XlsxUtils xlsxUtils = new XlsxUtils(wb);
			final CellStyle headerStyle = xlsxUtils.getCellStyleByName(XlsxUtils.ROYAL_BLUE_BACKGROUND_WHITE10_FONT);
			final CellStyle shadowStyle = xlsxUtils.getCellStyleByName(XlsxUtils.PALE_BLUE_BACKGROUND_WHITE10_FONT);
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
					for (Map.Entry<SemillaForm, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
						final SemillaForm semillaForm = semillaEntry.getKey();
						if (categoryForm.getName().equals(semillaForm.getCategoria().getName()) && hasTags(semillaForm, tagsToFilter)) {
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
			Logger.putLog("Error al generar el anexo: " + FILE_2_ITERATION_XLSX_NAME, AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Checks for tags.
	 *
	 * @param semillaForm  the semilla form
	 * @param tagsToFilter the tags to filter
	 * @return true, if successful
	 */
	private static boolean hasTags(final SemillaForm semillaForm, final String[] tagsToFilter) {
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
		return hasTags;
	}

	/**
	 * Creates the XLSX evolution annex.
	 *
	 * @param messageResources the message resources
	 * @param idObsExecution   the id obs execution
	 * @param idOperation      the id operation
	 * @param comparision      the comparision
	 * @param tagsToFilter     the tags to filter
	 * @throws Exception the exception
	 */
	public static void createAnnexXLSX1_Evolution(final MessageResources messageResources, final Long idObsExecution, final Long idOperation, final List<ComparisionForm> comparision,
			final String[] tagsToFilter) throws Exception {
		dependencies = new ArrayList<>();
		Logger.putLog("Generando anexo: " + FILE_1_EVOLUTION_XLSX_NAME, AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
		try (Connection c = DataBaseManager.getConnection(); FileOutputStream writer = getFileOutputStream(idOperation, FILE_1_EVOLUTION_XLSX_NAME)) {
			XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet(SHEET_RESULTS_NAME);
			XSSFRow row;
			XSSFCell cell;
			final int numberOfFixedColumns = 12;
			int rowIndex = 0;
			int columnIndex = 0;
			// executionDates = new ArrayList<>();
			excelLines = new HashMap<>();
			ExcelLine excelLine;
			XlsxUtils xlsxUtils = new XlsxUtils(wb);
			final CellStyle headerStyle = xlsxUtils.getCellStyleByName(XlsxUtils.ROYAL_BLUE_BACKGROUND_WHITE10_FONT);
			final CellStyle shadowStyle = xlsxUtils.getCellStyleByName(XlsxUtils.PALE_BLUE_BACKGROUND_WHITE10_FONT);
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
			for (String executionDateAux : executionDates) {
				// Add header if it is not already created
				if (!ColumnNames.contains("puntuacion_" + executionDateAux)) {
					ColumnNames.add("puntuacion_" + executionDateAux);
				}
				// ADECUACIÓN
				// Add header if it is not already created
				if (!ColumnNames.contains("adecuacion_" + executionDateAux)) {
					ColumnNames.add("adecuacion_" + executionDateAux);
				}
				// CUMPLIMIENTO
				// Add header if it is not already created
				if (!ColumnNames.contains("cumplimiento_" + executionDateAux)) {
					ColumnNames.add("cumplimiento_" + executionDateAux);
				}
			}
			for (String executionDateAux : executionDates) {
				// Add header if it is not already created
				if (!ColumnNames.contains(NV_PREFFIX + executionDateAux)) {
					ColumnNames.add(NV_PREFFIX + executionDateAux);
				}
				if (!ColumnNames.contains(A_PREFFIX + executionDateAux)) {
					ColumnNames.add(A_PREFFIX + executionDateAux);
				}
				if (!ColumnNames.contains(AA_PREFFIX + executionDateAux)) {
					ColumnNames.add(AA_PREFFIX + executionDateAux);
				}
				if (!ColumnNames.contains(NC_PREFFIX + executionDateAux)) {
					ColumnNames.add(NC_PREFFIX + executionDateAux);
				}
				if (!ColumnNames.contains(PC_PREFFIX + executionDateAux)) {
					ColumnNames.add(PC_PREFFIX + executionDateAux);
				}
				if (!ColumnNames.contains(TC_PREFFIX + executionDateAux)) {
					ColumnNames.add(TC_PREFFIX + executionDateAux);
				}
			}
			ColumnNames.add(EVOL_PUNTUACION_ANT);
			ColumnNames.add(EVOL_ADECUACION_ANT);
			ColumnNames.add(EVOL_CUMPLIMIENTO_ANT);
			ColumnNames.add(EVOL_PUNTUACION_PRIMER);
			ColumnNames.add(EVOL_ADECUACION_PRIMER);
			ColumnNames.add(EVOL_CUMPLIMIENTO_PRIMER);
//			
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
			for (Map.Entry<SemillaForm, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
				final SemillaForm semillaForm = semillaEntry.getKey();
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
				for (Map.Entry<SemillaForm, TreeMap<String, ScoreForm>> semillaEntry : annexmap.entrySet()) {
					// Filter if not has tags
					final SemillaForm semillaForm = semillaEntry.getKey();
					String namecat = semillaForm.getCategoria().getName();
					// On each category iteration we filter the other categories.
					if (semillaForm.getId() != 0 && namecat.equals(currentCategory) && hasTags(semillaForm, tagsToFilter)) {
						row = sheet.createRow(rowIndex);
						columnIndex = 0;
						excelLine = new ExcelLine();
						excelLine.setRowIndex(rowIndex);
						// "id"
						String id = String.valueOf(semillaForm.getId());
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
						cell = row.createCell(ColumnNames.indexOf("id"));
						cell.setCellValue(id);
						cell.setCellStyle(shadowStyle);
						excelLine.setId(id);
						// "nombre"
						String name = semillaForm.getNombre();
						cell = row.createCell(ColumnNames.indexOf("nombre"));
						cell.setCellValue(name);
						cell.setCellStyle(shadowStyle);
						excelLine.setNombre(name);
						// "namecat"
						cell = row.createCell(ColumnNames.indexOf("namecat"));
						cell.setCellValue(namecat);
						cell.setCellStyle(shadowStyle);
						excelLine.setNamecat(namecat);
						// "ambito"
						String ambito = semillaForm.getAmbito().getName();
						cell = row.createCell(ColumnNames.indexOf("ambito"));
						cell.setCellValue(ambito);
						cell.setCellStyle(shadowStyle);
						excelLine.setAmbito(ambito);
						// "complejidad"
						String compl = semillaForm.getComplejidad().getName();
						cell = row.createCell(ColumnNames.indexOf("complejidad"));
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
						cell = row.createCell(ColumnNames.indexOf("depende_de"));
						cell.setCellValue(dependencias.toString());
						cell.setCellStyle(shadowStyle);
						excelLine.setDepende_de(dependencias.toString());
						// "semilla"
						String semilla = semillaForm.getListaUrls().get(0);
						cell = row.createCell(ColumnNames.indexOf("semilla"));
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
						cell = row.createCell(ColumnNames.indexOf("tematica"));
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
						cell = row.createCell(ColumnNames.indexOf("distribucion"));
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
						cell = row.createCell(ColumnNames.indexOf("recurrencia"));
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
						cell = row.createCell(ColumnNames.indexOf("otros"));
						cell.setCellStyle(shadowStyle);
						excelLine.setOtros(dataToInsert);
						// Páginas
						String pages = String.valueOf(ObservatorioDAO.getNumCrawls(c, idObsExecution, semillaForm.getId()));
						cell = row.createCell(ColumnNames.indexOf("paginas"));
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
							final String executionDateAux = entry.getKey().substring(0, entry.getKey().indexOf(" ")).replace("/", "_");
							// Execution dates must be exists as column
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
							// Set the first column based on header
							columnIndex = ColumnNames.indexOf("puntuacion_" + executionDateAux);
							cell = row.createCell(columnIndex++);
							cell.setCellType(CellType.NUMERIC);
							cell.setCellValue(score);
							cell.setCellStyle(shadowStyle);
							// ADECUACIÓN
							cell = row.createCell(columnIndex++);
							cell.setCellValue(adequacy);
							cell.setCellStyle(shadowStyle);
							// CUMPLIMIENTO
							cell = row.createCell(columnIndex++);
							cell.setCellValue(compliance);
							cell.setCellStyle(shadowStyle);
						}
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
								// Set the first column based on header
								columnIndex = ColumnNames.indexOf(NV_PREFFIX + date);
								cell = row.createCell(columnIndex++);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"No Válido\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "A_" + date
								cell = row.createCell(columnIndex++);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"A\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "AA_" + date
								cell = row.createCell(columnIndex++);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"AA\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								columnFirstLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 3) + (3 * executionDates.indexOf(date)));
								// "NC_" + date
								cell = row.createCell(columnIndex++);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"No conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "PC_" + date
								cell = row.createCell(columnIndex++);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"Parcialmente conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								// "TC_" + date
								cell = row.createCell(columnIndex++);
								cell.setCellType(CellType.NUMERIC);
								cell.setCellFormula("IF($" + columnFirstLetter + (rowIndex + 1) + "=\"Plenamente conforme\",$" + columnSecondLetter + (rowIndex + 1) + ",0)");
								cell.setCellStyle(shadowStyle);
								numberOfDate++;
							}
						}
						// Evol score previous iteration
						{
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "puntuacion", false);
							String columnSecondLetter = GetExcelColumnNameForNumber(numberOfFixedColumns + 1 + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.indexOf(EVOL_PUNTUACION_ANT));
							String formula = generateComparisionFormula(columnFirstLetter, columnSecondLetter);
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
						// Evol allocation previous iteration
						{
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "adecuacion", false);
							String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 2) + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.indexOf(EVOL_ADECUACION_ANT));
							String formula = "IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"No Válido\",0,IF($" + columnSecondLetter + "$2:$"
									+ columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"A\",1,3))-IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$419=\"No Válido\",0,IF($"
									+ columnFirstLetter + "$2:$" + columnFirstLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"A\",1,3))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
						// Evol compliance previous iteration
						{
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "cumplimiento", false);
							String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 3) + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.indexOf(EVOL_CUMPLIMIENTO_ANT));
							String formula = "IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"No conforme\",0,IF($" + columnSecondLetter
									+ "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Parcialmente conforme\",1,3))-IF($" + columnFirstLetter + "$2:$" + columnFirstLetter
									+ "$419=\"No conforme\",0,IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Parcialmente conforme\",1,3))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
						// Evol score first iteration
						{
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "puntuacion", true);
							String columnSecondLetter = GetExcelColumnNameForNumber(numberOfFixedColumns + 1 + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.indexOf(EVOL_PUNTUACION_PRIMER));
							String formula = generateComparisionFormula(columnFirstLetter, columnSecondLetter);
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
						// Evol allocation first iteration
						{
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "adecuacion", true);
							String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 2) + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.indexOf(EVOL_ADECUACION_PRIMER));
							String formula = "IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"No Válido\",0,IF($" + columnSecondLetter + "$2:$"
									+ columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"A\",1,3))-IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$419=\"No Válido\",0,IF($"
									+ columnFirstLetter + "$2:$" + columnFirstLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"A\",1,3))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
						// Evol compliance first iteration
						{
							String columnFirstLetter = GetFirstLetterPreviousExecution(comparision, semillaForm.getEtiquetas(), ColumnNames, "cumplimiento", true);
							String columnSecondLetter = GetExcelColumnNameForNumber((numberOfFixedColumns + 3) + (3 * executionDates.size() - 3));
							cell = row.createCell(ColumnNames.size() - 1);
							cell = row.createCell(ColumnNames.indexOf(EVOL_CUMPLIMIENTO_PRIMER));
							String formula = "IF($" + columnSecondLetter + "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"No conforme\",0,IF($" + columnSecondLetter
									+ "$2:$" + columnSecondLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Parcialmente conforme\",1,3))-IF($" + columnFirstLetter + "$2:$" + columnFirstLetter
									+ "$419=\"No conforme\",0,IF($" + columnFirstLetter + "$2:$" + columnFirstLetter + "$" + (annexmap.entrySet().size() + 1) + "=\"Parcialmente conforme\",1,3))";
							cell.setCellFormula(formula);
							cell.setCellStyle(shadowStyle);
						}
						excelLines.put(rowIndex, excelLine);
						rowIndex++;
					}
				}
			}
			int nextStartPos = InsertSummaryTable(sheet, rowIndex + 5, ColumnNames, headerStyle, shadowStyle);
			// Compliance
			nextStartPos = InsertSummaryTableCompliance(sheet, nextStartPos + 5, ColumnNames, headerStyle, shadowStyle);
			String title = "Datos de evolución de PUNTUACIÓN con respecto a la ITERACION ANTERIOR (Nº de sitios web por segmentos)";
			nextStartPos = InsertCategoriesTable(sheet, nextStartPos + 5, categories, headerStyle, shadowStyle, rowIndex, ColumnNames.indexOf(EVOL_PUNTUACION_ANT) + 1, title);
			title = "Datos de evolución de PUNTUACIÓN con respecto a la PRIMERA ITERACIÓN (Nº de sitios web por segmentos)";
			nextStartPos = InsertCategoriesTable(sheet, nextStartPos + 5, categories, headerStyle, shadowStyle, rowIndex, ColumnNames.indexOf(EVOL_PUNTUACION_PRIMER) + 1, title);
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
				// Starts 1 because always first row was header
				for (int i = 1; i < rowIndex; i++) {
					row = sheet.getRow(i);
					if (row != null) {
						cell = row.getCell(2);
						if (cell.getStringCellValue().equals(category)) {
							if (categoryFirstRow == 0) {
								categoryFirstRow = i;
							}
							categoryLastRow = i;
						}
					}
				}
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
			// Clear -1 cell values
			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				Row r = sheet.getRow(i);
				if (r != null) {
					for (int j = 0; j < r.getLastCellNum(); j++) {
						Cell cX = r.getCell(j);
						if (cX != null && CellType.NUMERIC.equals(cX.getCellType()) && cX.getNumericCellValue() == -1) {
							cX.setCellStyle(shadowStyle);
							cX.setBlank();
						}
					}
				}
			}
			wb.write(writer);
			wb.close();
		} catch (Exception e) {
			Logger.putLog("Error al generar el anexo: " + FILE_1_EVOLUTION_XLSX_NAME, AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Generate comparision formula.
	 *
	 * @param columnFirstLetter  the column first letter
	 * @param columnSecondLetter the column second letter
	 * @return the string
	 */
	private static String generateComparisionFormula(final String columnFirstLetter, final String columnSecondLetter) {
		final String substractColumnsResult = columnSecondLetter + ":" + columnSecondLetter + "-" + columnFirstLetter + ":" + columnFirstLetter;
		String formula = "IF(" + columnSecondLetter + ":" + columnSecondLetter + "=\"\",\"\",\"\")";
		if (websiteRanges != null && !websiteRanges.isEmpty()) {
			formula = "IF(" + columnSecondLetter + ":" + columnSecondLetter + "=\"\",\"\",_next_if_clause_)";
			int index = 0;
			for (RangeForm range : websiteRanges) {
				String rangetoString = "";
				String ifCaluse = "";
				// has superior range
				if (!StringUtils.isEmpty(range.getMinValueOperator()) && !StringUtils.isEmpty(range.getMaxValueOperator())) {
					rangetoString = "AND(" + range.getMinValue() + "" + range.getMinValueOperator() + "" + substractColumnsResult + "," + substractColumnsResult + "" + range.getMaxValueOperator() + ""
							+ range.getMaxValue() + ")";
				} else if (!StringUtils.isEmpty(range.getMinValueOperator()) && StringUtils.isEmpty(range.getMaxValueOperator())) {
					rangetoString = range.getMinValue() + "" + range.getMinValueOperator() + "" + substractColumnsResult;
				} else if (StringUtils.isEmpty(range.getMinValueOperator()) && !StringUtils.isEmpty(range.getMaxValueOperator())) {
					rangetoString = substractColumnsResult + "" + range.getMaxValueOperator() + "" + range.getMaxValue();
				}
				// is last iteration
				if (index == websiteRanges.size() - 1) {
					ifCaluse = "IF(" + rangetoString + ",\"" + range.getName() + "\",\"ERROR\")";
				} else {
					ifCaluse = "IF(" + rangetoString + ",\"" + range.getName() + "\",_next_if_clause_)";
				}
				formula = formula.replace("_next_if_clause_", ifCaluse);
				index++;
			}
		}
		return formula;
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
				if (!StringUtils.isEmpty(previousDate)) {
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
			Logger.putLog("Generando anexo: " + currentDependency + ".xlsx", AnnexUtils.class, Logger.LOG_LEVEL_ERROR);
			try (FileOutputStream writer = getFileOutputStream(idOperation, "/Dependencias/" + currentDependency + ".xlsx")) {
				XSSFWorkbook wb = new XSSFWorkbook();
				XSSFSheet sheet = wb.createSheet(SHEET_RESULTS_NAME);
				XSSFRow row;
				XSSFCell cell;
				int rowIndex = 0;
				int columnIndex = 0;
				XlsxUtils xlsxUtils = new XlsxUtils(wb);
				final CellStyle headerStyle = xlsxUtils.getCellStyleByName(XlsxUtils.ROYAL_BLUE_BACKGROUND_WHITE10_FONT);
				final CellStyle shadowStyle = xlsxUtils.getCellStyleByName(XlsxUtils.PALE_BLUE_BACKGROUND_WHITE10_FONT);
				row = sheet.createRow(rowIndex);
				// Remove evol_ columns
				for (int i = 0; i < ColumnNames.size() - 6; i++) {
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
						// Páginas
						cell = row.createCell(columnIndex++);
						cell.setCellValue(currentLine.getValue().getPaginas());
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
				XSSFSheet currentSheet3 = wb.createSheet("Iteración Global");
				if (rowIndex > 1) {
					InsertGraphIntoSheetByDependency(currentSheet, rowIndex, true, numberOfFixedColumns, false);
					InsertGraphIntoSheetByDependency(currentSheet, rowIndex, false, numberOfFixedColumns, false);
					InsertGraphIntoSheetByDependency(currentSheet2, rowIndex, true, numberOfFixedColumns, true);
					InsertGraphIntoSheetByDependency(currentSheet2, rowIndex, false, numberOfFixedColumns, true);
					InsertAgregatePieChar(currentSheet3, rowIndex, ColumnNames, xlsxUtils);
				}
				XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
				// Hide Id Column
				CTCol col = sheet.getCTWorksheet().getColsArray(0).addNewCol();
				col.setMin(1);
				col.setMax(1);
				col.setHidden(true);
				wb.write(writer);
				wb.close();
			} catch (Exception e) {
				Logger.putLog("Error al generar el anexo: " + currentDependency + ".xlsx", AnnexUtils.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * @param xlsxUtils     the xlsx utils
	 */
	private static void InsertAgregatePieChar(XSSFSheet currentSheet3, int rowIndex, List<String> columnNames, final XlsxUtils xlsxUtils) {
		int adecuationColumn = 0;
		for (int i = columnNames.size() - 1; i > 5; i--) {
			if (columnNames.get(i).contains("adecuacion") && !columnNames.get(i).contains("ant") && !columnNames.get(i).contains("primer")) {
				adecuationColumn = i + 1;
				// first apareance is last iteration
				break;
			}
		}
		CellStyle headerStyle = xlsxUtils.getCellStyleByName(XlsxUtils.ROYAL_BLUE_BACKGROUND_WHITE10_FONT);
		CellStyle percentCenterStyle = xlsxUtils.getCellStyleByName(XlsxUtils.NORMAL_PERCENT11_CENTER_STYLE);
		// ADECUACY
		// "Headers"
		XSSFRow row = currentSheet3.createRow(0);
		XSSFCell cell = row.createCell(1);
		cell.setCellValue("Número de sitios web");
		cell.setCellStyle(headerStyle);
		cell = row.createCell(2);
		cell.setCellValue("Porcentaje sobre el total");
		cell.setCellStyle(headerStyle);
		// "AA"
		row = currentSheet3.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue(ALLOCATION_AA_LITERAL);
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
		cell.setCellValue(ALLOCATION_A_LITERAL);
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
		cell.setCellValue(ALLOCATION_NOT_VALID_LITERAL);
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
		XDDFChartData data = chart.createData(ChartTypes.PIE3D, null, null);
		data.setVaryColors(true);
		data.addSeries(labels, values);
		chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(0);
		chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDPtList().get(0).addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(hex2Rgb(GREEN_OAW_HTML));
		chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(1);
		chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDPtList().get(1).addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(hex2Rgb(YELLOW_OAW_HTML));
		chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(2);
		chart.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDPtList().get(2).addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(hex2Rgb(RED_OAW_HTML));
		// angle getPie3DChartArray
		chart.getCTChart().addNewView3D().addNewRotX().setVal((byte) 25);
		chart.plot(data);
		setRoundedCorners(chart, false);
		// COMPLIANCE
		int complianceColumn = 0;
		for (int i = columnNames.size() - 1; i > 5; i--) {
			if (columnNames.get(i).contains("cumplimiento") && !columnNames.get(i).contains("ant") && !columnNames.get(i).contains("primer")) {
				complianceColumn = i + 1;
				// first is last
				break;
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
		cell.setCellValue(COMPLIANCE_TOTAL_LITERAL);
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
		cell.setCellValue(COMPLIANCE_PARTIAL_LITERAL);
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
		cell.setCellValue(COMPLIANCE_NOT_LITERAL);
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
		XDDFChartData data2 = chart2.createData(ChartTypes.PIE3D, null, null);
		data2.setVaryColors(true);
		data2.addSeries(labels2, values2);
		chart2.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(0);
		chart2.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDPtList().get(0).addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(hex2Rgb(GREEN_OAW_HTML));
		chart2.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(1);
		chart2.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDPtList().get(1).addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(hex2Rgb(YELLOW_OAW_HTML));
		chart2.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).addNewDPt().addNewIdx().setVal(2);
		chart2.getCTChart().getPlotArea().getPie3DChartArray(0).getSerArray(0).getDPtList().get(2).addNewSpPr().addNewSolidFill().addNewSrgbClr().setVal(hex2Rgb(RED_OAW_HTML));
		// angle getPie3DChartArray
		chart2.getCTChart().addNewView3D().addNewRotX().setVal((byte) 25);
		chart2.plot(data2);
		setRoundedCorners(chart2, false);
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
		String columnResumeNamePrevious = GetExcelColumnNameForNumber(ColumnNames.indexOf(EVOL_ADECUACION_ANT) + 1);
		String columnResumeNameFirst = GetExcelColumnNameForNumber(ColumnNames.indexOf(EVOL_ADECUACION_PRIMER) + 1);
		row = sheet.createRow(RowStartPosition);
		cell = row.createCell(0);
		cell.setCellValue("Datos de evolución de NIVEL DE ADECUACIÓN (Nº de sitios web");
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
		cell.setCellValue("De NV a A");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",1)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",1)");
		row = sheet.createRow(RowStartPosition + 2);
		cell = row.createCell(0);
		cell.setCellValue("De NV a AA");
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
		cell.setCellValue("De A a AA");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",2)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",2)");
		row = sheet.createRow(RowStartPosition + 4);
		cell = row.createCell(0);
		cell.setCellValue("De AA a A");
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
		cell.setCellValue("De AA a NV");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",-3)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",-3)");
		row = sheet.createRow(RowStartPosition + 6);
		cell = row.createCell(0);
		cell.setCellValue("De A a NV");
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
	 * Insert summary table compliance.
	 *
	 * @param sheet            the sheet
	 * @param RowStartPosition the row start position
	 * @param ColumnNames      the column names
	 * @param headerStyle      the header style
	 * @param shadowStyle      the shadow style
	 * @return the int
	 */
	private static int InsertSummaryTableCompliance(XSSFSheet sheet, int RowStartPosition, List<String> ColumnNames, CellStyle headerStyle, CellStyle shadowStyle) {
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
		String columnResumeNamePrevious = GetExcelColumnNameForNumber(ColumnNames.indexOf(EVOL_CUMPLIMIENTO_ANT) + 1);
		String columnResumeNameFirst = GetExcelColumnNameForNumber(ColumnNames.indexOf(EVOL_CUMPLIMIENTO_PRIMER) + 1);
		row = sheet.createRow(RowStartPosition);
		cell = row.createCell(0);
		cell.setCellValue("Datos de evolución SITUACIÓN DE CUMPLIMIENTO (Nº de sitios web)");
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
		cell.setCellValue("De NC a PC");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",1)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",1)");
		row = sheet.createRow(RowStartPosition + 2);
		cell = row.createCell(0);
		cell.setCellValue("De NC a TC");
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
		cell.setCellValue("De PC a TC");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",2)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",2)");
		row = sheet.createRow(RowStartPosition + 4);
		cell = row.createCell(0);
		cell.setCellValue("De TC a PC");
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
		cell.setCellValue("De TC a NC");
		cell = row.createCell(1);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNamePrevious + ":" + columnResumeNamePrevious + ",-3)");
		cell = row.createCell(2);
		cell.setCellStyle(shadowStyleCentered);
		cell.setCellFormula("COUNTIF(" + columnResumeNameFirst + ":" + columnResumeNameFirst + ",-3)");
		row = sheet.createRow(RowStartPosition + 6);
		cell = row.createCell(0);
		cell.setCellValue("De PC a NC");
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
		if (websiteRanges != null && !websiteRanges.isEmpty()) {
			int col = 1;
			for (RangeForm range : websiteRanges) {
				cell = row.createCell(col);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(range.getName());
				col++;
			}
		}
		String dataColumn = GetExcelColumnNameForNumber(columnSourceData);
		for (int i = 0; i < categories.size(); i++) {
			row = sheet.createRow(RowStartPosition + i + 2);
			cell = row.createCell(0);
			cell.setCellStyle(shadowStyle);
			cell.setCellValue(categories.get(i));
			if (websiteRanges != null && !websiteRanges.isEmpty()) {
				int col = 1;
				for (RangeForm range : websiteRanges) {
					cell = row.createCell(col);
					cell.setCellStyle(shadowStyleCentered);
					cell.setCellFormula(
							"COUNTIFS($C$2:$C$" + lastDataRow + ",\"" + categories.get(i) + "\",$" + dataColumn + "$2:$" + dataColumn + "$" + lastDataRow + ",\"" + range.getName() + "\")");
					col++;
				}
			}
		}
		// TOTAL row
		row = sheet.createRow(RowStartPosition + categories.size() + 2);
		cell = row.createCell(0);
		cell.setCellStyle(headerStyle);
		cell.setCellValue("TOTAL");
		if (websiteRanges != null && !websiteRanges.isEmpty()) {
			int col = 1;
			for (RangeForm range : websiteRanges) {
				cell = row.createCell(col);
				cell.setCellStyle(headerStyle);
				String column = GetExcelColumnNameForNumber(col + 1);
				cell.setCellFormula("SUM(" + column + "" + (RowStartPosition + 3) + ":" + column + "" + (RowStartPosition + categories.size() + 2) + ")");
				col++;
			}
			// Sum total portals
			String firstColumn = GetExcelColumnNameForNumber(2);
			String lastColumn = GetExcelColumnNameForNumber(websiteRanges.size() + 1);
			cell = row.createCell(websiteRanges.size() + 1);
			cell.setCellStyle(headerStyle);
			cell.setCellFormula("SUM(" + firstColumn + "" + (RowStartPosition + categories.size() + 3) + ":" + lastColumn + "" + (RowStartPosition + categories.size() + 3) + ")");
		}
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
			chart.setTitleText(isFirst ? ALLOCATION_LEVEL_TITLE : COMPLIANCE_LEVEL_TITLE);
			chart.setTitleOverlay(false);
			XDDFChartLegend legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.LEFT);
			XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
			XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
			bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
			bottomAxis.getOrAddTextProperties();
			leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
			// category axis crosses the value axis between the strokes and not midpoint the
			// strokes
			leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
			CTPlotArea plotArea = chart.getCTChart().getPlotArea();
			plotArea.getValAxArray()[0].addNewMajorGridlines();
			plotArea.getValAxArray(0).getScaling().addNewMax().setVal(10);
			plotArea.getValAxArray(0).getScaling().addNewMin().setVal(0);
			// Get agency names
			XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(wb.getSheetAt(0), new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, 1, 1));
			// First serie ("No válido" / "No Conforme")
			XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 15 : 18, isFirst ? 15 : 18));
			XDDFChartData.Series series1 = data.addSeries(agencies, values1);
			series1.setTitle(isFirst ? ALLOCATION_NOT_VALID_LITERAL : COMPLIANCE_NOT_LITERAL, null);
			// Second serie ("A" / "Parcialmente conforme")
			XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 16 : 19, isFirst ? 16 : 19));
			XDDFChartData.Series series2 = data.addSeries(agencies, values2);
			series2.setTitle(isFirst ? ALLOCATION_A_LITERAL : COMPLIANCE_PARTIAL_LITERAL, null);
			// Third serie ("AA" / "Plenamente conforme")
			XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow - 1, isFirst ? 17 : 20, isFirst ? 17 : 20));
			XDDFChartData.Series series3 = data.addSeries(agencies, values3);
			series3.setTitle(isFirst ? ALLOCATION_AA_LITERAL : COMPLIANCE_TOTAL_LITERAL, null);
			chart.plot(data);
			solidFillSeries(data, 0, PresetColor.RED);
			solidFillSeries(data, 1, XDDFColor.from(hex2Rgb(YELLOW_OAW_HTML)));
			solidFillSeries(data, 2, PresetColor.GREEN);
			XDDFBarChartData bar = (XDDFBarChartData) data;
			bar.setBarDirection(BarDirection.COL);
			bar.setBarGrouping(BarGrouping.STANDARD);
			chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal((byte) -100);
			// Rotate labels bottom axis
			CTTextBody text = chart.getCTChart().getPlotArea().getCatAxArray(0).getTxPr();
			int rotAngle = 0;
			int minus45Deg = (int) Math.round(-5400000 / 2d);
			rotAngle = minus45Deg;
			text.getBodyPr().setRot(rotAngle);
			setRoundedCorners(chart, false);
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
		chart.setTitleText(isFirst ? ALLOCATION_EVOLUTION_TITLE : COMPLIANCE_EVOLUTION_TITLE);
		chart.setTitleOverlay(false);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.LEFT);
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		bottomAxis.getOrAddTextProperties();
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		// category axis crosses the value axis between the strokes and not midpoint the
		// strokes
		leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		CTPlotArea plotArea = chart.getCTChart().getPlotArea();
		plotArea.getValAxArray()[0].addNewMajorGridlines();
		plotArea.getValAxArray(0).getScaling().addNewMax().setVal(10);
		plotArea.getValAxArray(0).getScaling().addNewMin().setVal(0);
		// Get agency names
		XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(wb.getSheetAt(0), new CellRangeAddress(categoryFirstRow, categoryLastRow, 1, 1));
		// Iterate through the executions
		int iteration = 0;
		for (String date : executionDates) {
			int firstSerieColumn = numberOfFixedColumns + (executionDates.size() * 3) + (6 * executionDates.indexOf(date));
			// First serie ("No válido" / "No Conforme")
			FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow, firstSerieColumn + (isFirst ? 0 : 3));
			XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow, firstSerieColumn + (isFirst ? 0 : 3), firstSerieColumn + (isFirst ? 0 : 3)));
			XDDFChartData.Series series1 = data.addSeries(agencies, values1);
			series1.setTitle((isFirst ? NV_PREFFIX : NC_PREFFIX) + date, null);
			solidFillSeries(data, iteration++, PresetColor.RED);
			// Second serie ("A" / "Parcialmente conforme")
			FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow, firstSerieColumn + (isFirst ? 1 : 4));
			XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow, firstSerieColumn + (isFirst ? 1 : 4), firstSerieColumn + (isFirst ? 1 : 4)));
			XDDFChartData.Series series2 = data.addSeries(agencies, values2);
			series2.setTitle((isFirst ? A_PREFFIX : PC_PREFFIX) + date, null);
			solidFillSeries(data, iteration++, XDDFColor.from(hex2Rgb(YELLOW_OAW_HTML)));
			// Third serie ("AA" / "Plenamente conforme")
			FillNullCellInRange(wb.getSheetAt(0), categoryFirstRow, categoryLastRow, firstSerieColumn + (isFirst ? 2 : 5));
			XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(wb.getSheetAt(0),
					new CellRangeAddress(categoryFirstRow, categoryLastRow, firstSerieColumn + (isFirst ? 2 : 5), firstSerieColumn + (isFirst ? 2 : 5)));
			XDDFChartData.Series series3 = data.addSeries(agencies, values3);
			series3.setTitle((isFirst ? AA_PREFFIX : TC_PREFFIX) + date, null);
			solidFillSeries(data, iteration++, PresetColor.GREEN);
		}
		chart.plot(data);
		XDDFBarChartData bar = (XDDFBarChartData) data;
		bar.setBarDirection(BarDirection.COL);
		bar.setBarGrouping(BarGrouping.STANDARD);
		chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal((byte) -100);
		// Rotate labels bottom axis
		CTTextBody text = chart.getCTChart().getPlotArea().getCatAxArray(0).getTxPr();
		int rotAngle = 0;
		int minus45Deg = (int) Math.round(-5400000 / 2d);
		rotAngle = minus45Deg;
		text.getBodyPr().setRot(rotAngle);
		setRoundedCorners(chart, false);
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
		chart.setTitleText(isFirst ? ALLOCATION_EVOLUTION_TITLE : COMPLIANCE_EVOLUTION_TITLE);
		chart.setTitleOverlay(false);
		XDDFChartLegend legend = chart.getOrAddLegend();
		legend.setPosition(LegendPosition.LEFT);
		XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
		XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
		XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
		bottomAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		bottomAxis.getOrAddTextProperties();
		leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
		// category axis crosses the value axis between the strokes and not midpoint the
		// strokes
		leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
		CTPlotArea plotArea = chart.getCTChart().getPlotArea();
		plotArea.getValAxArray()[0].addNewMajorGridlines();
		plotArea.getValAxArray(0).getScaling().addNewMax().setVal(10);
		plotArea.getValAxArray(0).getScaling().addNewMin().setVal(0);
		// Get agency names
		XDDFDataSource<String> agencies = XDDFDataSourcesFactory.fromStringCellRange(currentSheet.getWorkbook().getSheetAt(0), new CellRangeAddress(1, rowIndex - 1, 1, 1));
		// Iterate through the executions
		int i = 0;
		int iteration = 0;
		for (String date : executionDates) {
			if (!onlyLastIteration || (onlyLastIteration && i == (executionDates.size() - 1))) {
				int firstSerieColumn = numberOfFixedColumns + (executionDates.size() * 3) + (6 * executionDates.indexOf(date));
				// First serie ("No válido" / "No Conforme")
				FillNullCellInRange(currentSheet.getWorkbook().getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + (isFirst ? 0 : 3));
				XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet.getWorkbook().getSheetAt(0),
						new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + (isFirst ? 0 : 3), firstSerieColumn + (isFirst ? 0 : 3)));
				XDDFChartData.Series series1 = data.addSeries(agencies, values1);
				series1.setTitle((isFirst ? NV_PREFFIX : NC_PREFFIX) + date, null);
				solidFillSeries(data, iteration++, PresetColor.RED);
				// Second serie ("A" / "Parcialmente conforme")
				FillNullCellInRange(currentSheet.getWorkbook().getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + (isFirst ? 1 : 4));
				XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet.getWorkbook().getSheetAt(0),
						new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + (isFirst ? 1 : 4), firstSerieColumn + (isFirst ? 1 : 4)));
				XDDFChartData.Series series2 = data.addSeries(agencies, values2);
				series2.setTitle((isFirst ? A_PREFFIX : PC_PREFFIX) + date, null);
				solidFillSeries(data, iteration++, XDDFColor.from(hex2Rgb(YELLOW_OAW_HTML)));
				// Third serie ("AA" / "Plenamente conforme")
				FillNullCellInRange(currentSheet.getWorkbook().getSheetAt(0), 1, rowIndex - 1, firstSerieColumn + (isFirst ? 2 : 5));
				XDDFNumericalDataSource<Double> values3 = XDDFDataSourcesFactory.fromNumericCellRange(currentSheet.getWorkbook().getSheetAt(0),
						new CellRangeAddress(1, rowIndex - 1, firstSerieColumn + (isFirst ? 2 : 5), firstSerieColumn + (isFirst ? 2 : 5)));
				XDDFChartData.Series series3 = data.addSeries(agencies, values3);
				series3.setTitle((isFirst ? AA_PREFFIX : TC_PREFFIX) + date, null);
				solidFillSeries(data, iteration++, PresetColor.GREEN);
			}
			i++;
		}
		chart.plot(data);
		XDDFBarChartData bar = (XDDFBarChartData) data;
		bar.setBarDirection(BarDirection.COL);
		bar.setBarGrouping(BarGrouping.STANDARD);
		chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal((byte) -100);
		// Rotate labels bottom axis
		CTTextBody text = chart.getCTChart().getPlotArea().getCatAxArray(0).getTxPr();
		int rotAngle = 0;
		int minus45Deg = (int) Math.round(-5400000 / 2d);
		rotAngle = minus45Deg;
		text.getBodyPr().setRot(rotAngle);
		setRoundedCorners(chart, false);
	}

	/**
	 * Hex 2 rgb.
	 *
	 * @param colorStr the color str
	 * @return the byte[]
	 */
	private static byte[] hex2Rgb(String colorStr) {
		int r = Integer.valueOf(colorStr.substring(1, 3), 16);
		int g = Integer.valueOf(colorStr.substring(3, 5), 16);
		int b = Integer.valueOf(colorStr.substring(5, 7), 16);
		return new byte[] { (byte) r, (byte) g, (byte) b };
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
	 * Solid fill series.
	 *
	 * @param data  the data
	 * @param index the index
	 * @param color the color
	 */
	private static void solidFillSeries(XDDFChartData data, int index, XDDFColor color) {
		XDDFSolidFillProperties fill = new XDDFSolidFillProperties(color);
		XDDFChartData.Series series = data.getSeries().get(index);
		XDDFShapeProperties properties = series.getShapeProperties();
		if (properties == null) {
			properties = new XDDFShapeProperties();
		}
		properties.setFillProperties(fill);
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
		final File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_ANNEX_PATH) + idOperation + File.separator + filename);
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
		final File file = new File(pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_ANNEX_PATH) + idOperation + File.separator + filename);
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
	@SuppressWarnings("restriction")
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
	 * @param exObsIds       the ex obs ids
	 * @return the map
	 */
	private static Map<SemillaForm, TreeMap<String, ScoreForm>> createAnnexMap(final Long idObsExecution, final String[] exObsIds) {
		final Map<SemillaForm, TreeMap<String, ScoreForm>> seedMapFilled = new HashMap<>();
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
						List<VerificationScoreForm> verificationScoreList = new LinkedList<>();
						for (VerificationScoreForm verification : siteForm.getVerificationScoreList()) {
							VerificationScoreForm vsf = new VerificationScoreForm();
							BeanUtils.copyProperties(vsf, verification);
							verificationScoreList.add(vsf);
						}
						Collections.sort(verificationScoreList, new Comparator<VerificationScoreForm>() {
							@Override
							public int compare(VerificationScoreForm version1, VerificationScoreForm version2) {
								String[] v1 = version1.getVerification().split(REGEX_DOT);
								String[] v2 = version2.getVerification().split(REGEX_DOT);
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
			// Retrive seed form to prevent extra database querys in other methods
			for (Map.Entry<Long, TreeMap<String, ScoreForm>> semillaEntry : seedMap.entrySet()) {
				final SemillaForm semillaForm = SemillaDAO.getSeedById(c, semillaEntry.getKey());
				seedMapFilled.put(semillaForm, semillaEntry.getValue());
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
		return seedMapFilled;
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

	/**
	 * The Class SummaryEvolution.
	 */
	public static class SummaryEvolution {
		/** The range maps. */
		private Map<RangeForm, Integer> rangeMaps = new TreeMap<>();

		/**
		 * Instantiates a new summary evolution.
		 *
		 * @param websiteRanges the website ranges
		 */
		public SummaryEvolution(final List<RangeForm> websiteRanges) {
			// Init map
			for (RangeForm range : websiteRanges) {
				rangeMaps.put(range, 0);
			}
		}

		/**
		 * Gets the range maps.
		 *
		 * @return the range maps
		 */
		public Map<RangeForm, Integer> getRangeMaps() {
			return rangeMaps;
		}

		/**
		 * Sets the range maps.
		 *
		 * @param rangeMaps the range maps
		 */
		public void setRangeMaps(Map<RangeForm, Integer> rangeMaps) {
			this.rangeMaps = rangeMaps;
		}

		/**
		 * Checks if is all zero.
		 *
		 * @return true, if is all zero
		 */
		public boolean isAllZero() {
			boolean isAllZero = true;
			for (Map.Entry<RangeForm, Integer> entry : rangeMaps.entrySet()) {
				if (entry.getValue() != null && entry.getValue() != 0) {
					isAllZero = false;
					break;
				}
			}
			return isAllZero;
		}
	}
}