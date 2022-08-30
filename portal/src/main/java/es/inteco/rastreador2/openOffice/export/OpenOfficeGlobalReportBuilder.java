package es.inteco.rastreador2.openOffice.export;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.doc.style.OdfStyle;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfParagraphProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableCellProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableColumnProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTextProperties;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.SeedForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ComplianceComparisonForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.PlantillaForm;
import es.inteco.rastreador2.dao.ambito.AmbitoDAO;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.plantilla.PlantillaDAO;
import es.inteco.rastreador2.dao.rastreo.GlobalReportStatistics;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.GraphicsUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;
import es.inteco.view.forms.AmbitViewListForm;
import es.inteco.view.forms.ComplexityViewListForm;

/**
 * The Class OpenOfficeGlobalReportBuilder.
 */
@SuppressWarnings("restriction")
public final class OpenOfficeGlobalReportBuilder {
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPLIANCE_AMBIT_PRIMARY_MARK_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPLIANCE_AMBIT_PRIMARY_MARK_TITLE = "observatory.graphic.global.puntuation.compliance.ambit.primary.mark.title";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_PRIMARY_MARK_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_PRIMARY_MARK_TITLE = "observatory.graphic.global.puntuation.allocation.ambit.primary.mark.title";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_MARK_NAME = "observatory.graphic.global.puntuation.compilance.ambit.mark.name";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPLIANCE_AMBIT_MARK_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPLIANCE_AMBIT_MARK_TITLE = "observatory.graphic.global.puntuation.compliance.ambit.mark.title";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_MARK_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_MARK_TITLE = "observatory.graphic.global.puntuation.allocation.ambit.mark.title";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_TITLE = "observatory.graphic.global.puntuation.compilance.complexitiviy.mark.title";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_TITLE = "observatory.graphic.global.puntuation.allocation.complexity.mark.title";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_TITLE = "observatory.graphic.verification.compilance.comparation.level.2.title";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_TITLE = "observatory.graphic.verification.compilance.comparation.level.1.title";
	/** The Constant OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME_TITLE. */
	private static final String OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME_TITLE = "observatory.graphic.compilance.level.allocation.name.title";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_PRIMARY_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_PRIMARY_MARK_NAME = "observatory.graphic.global.puntuation.compilance.ambit.primary.mark.name";
	/** The Constant CHART_OBSERVATORY_GRAPHIC_INTAV_COLORS. */
	private static final String CHART_OBSERVATORY_GRAPHIC_INTAV_COLORS = "chart.observatory.graphic.intav.colors";
	/** The Constant ODT_EXTENSION. */
	private static final String ODT_EXTENSION = ".odt";
	/** The Constant HEADER_NUM_SITIOS_WEB. */
	private static final String HEADER_NUM_SITIOS_WEB = "global.report.header.num.sitios.web";
	/** The Constant HEADER_ETIQUETA. */
	private static final String HEADER_ETIQUETA = "global.report.header.tags";
	/** The Constant HEADER_COMPLEJIDAD. */
	private static final String HEADER_COMPLEJIDAD = "global.report.header.complexitivity";
	/** The Constant HEADER_AMBITO. */
	private static final String HEADER_AMBITO = "global.report.header.ambit";
	/** The Constant TYPE_PUNTUACTION. */
	private static final String TYPE_PUNTUACTION = "PUNTUACTION";
	/** The Constant TYPE_COMPLIANCE. */
	private static final String TYPE_COMPLIANCE = "COMPLIANCE";
	/** The Constant TYPE_ALLOCATION. */
	private static final String TYPE_ALLOCATION = "ALLOCATION";
	/** The Constant STYLE_LFO3. */
	private static final String STYLE_LFO3 = "LFO3";
	/** The Constant FO_CLIP. */
	private static final String FO_CLIP = "fo:clip";
	/** The Constant STYLE_GRAPHIC_PROPERTIES. */
	private static final String STYLE_GRAPHIC_PROPERTIES = "style:graphic-properties";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_NAME. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_NAME = "observatory.graphic.verification.compilance.comparation.level.2.name";
	/** The Constant OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_NAME. */
	private static final String OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_NAME = "observatory.graphic.verification.compilance.comparation.level.1.name";
	/** The Constant TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK. */
	private static final String TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK = "tablascumplimientocomplejidad";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_NAME = "observatory.graphic.global.puntuation.compilance.complexitiviy.mark.name";
	/** The Constant TABLASCOMPLEJIDAD_BOOKMARK. */
	private static final String TABLASCOMPLEJIDAD_BOOKMARK = "tablascomplejidad";
	/** The Constant TABLAS_CUMPLIMIENTO_BOOKMARK. */
	private static final String TABLAS_CUMPLIMIENTO_BOOKMARK = "tablascumplimentoambito";
	/** The Constant TABLAS_CUMPLIMIENTO_PRINCIPALES_BOOKMARK. */
	private static final String TABLAS_CUMPLIMIENTO_PRINCIPALES_BOOKMARK = "tablacumpliminetoambitoprincipales";
	/** The Constant TABLAS_ADECUACION_BOOKMARK. */
	private static final String TABLAS_ADECUACION_BOOKMARK = "tablasadecuacionambito";
	/** The Constant TABLAS_ADECUACION_PRINCIPALES_BOOKMARK. */
	private static final String TABLAS_ADECUACION_PRINCIPALES_BOOKMARK = "tablaadecuacionambitoprincipales";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_NAME = "observatory.graphic.global.puntuation.allocation.complexity.mark.name";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_MARK_NAME = "observatory.graphic.global.puntuation.allocation.ambit.mark.name";
	/** The Constant OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_PRIMARY_MARK_NAME. */
	private static final String OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_PRIMARY_MARK_NAME = "observatory.graphic.global.puntuation.allocation.ambit.primary.mark.name";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_2. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_2 = "-4c1.t1.c4-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_2. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_2 = "-4c1.t1.c3-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_2. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_2 = "-4c1.t1.c2-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_1. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_1 = "-4c1.t1.b4-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_1. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_1 = "-4c1.t1.b2-";
	/** The Constant TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_1. */
	private static final String TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_1 = "-4c1.t1.b3-";
	/** The Constant OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME. */
	private static final String OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME = "observatory.graphic.compilance.level.allocation.name";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_2. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_2 = "-41.t1.c4-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_2. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_2 = "-41.t1.c3-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_2. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_2 = "-41.t1.c2-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_1. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_1 = "-41.t1.b4-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_1. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_1 = "-41.t1.b3-";
	/** The Constant TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_1. */
	private static final String TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_1 = "-41.t1.b2-";
	/** The Constant OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_NAME. */
	private static final String OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_NAME = "observatory.graphic.accessibility.level.allocation.name";
	/** The Constant PICTURES_ODT_PATH. */
	private static final String PICTURES_ODT_PATH = "Pictures/";
	/** The Constant XLINK_HREF. */
	private static final String XLINK_HREF = "xlink:href";
	/** The Constant TEXT_BOOKMARK_START_TEXT_NAME_S. */
	private static final String TEXT_BOOKMARK_START_TEXT_NAME_S = "//text:bookmark-start[@text:name='%s']";
	/** The Constant TEXT_TITLE. */
	private static final String TEXT_TITLE = "//text:title";
	/** The Constant HEADER_NO_CONFORME. */
	private static final String HEADER_NO_CONFORME = "resultados.anonimos.porc.portales.nc";
	/** The Constant HEADER_PARCIALMENTE_CONFORME. */
	private static final String HEADER_PARCIALMENTE_CONFORME = "resultados.anonimos.porc.portales.pc";
	/** The Constant HEADER_TOTALMENTE_CONFORME. */
	private static final String HEADER_TOTALMENTE_CONFORME = "resultados.anonimos.porc.portales.pc";
	/** The Constant HEADER_NO_VALIDO. */
	private static final String HEADER_NO_VALIDO = "resultados.anonimos.punt.portales.nv";
	/** The Constant HEADER_A. */
	private static final String HEADER_A = "resultados.anonimos.punt.portales.a";
	/** The Constant HEADER_AA. */
	private static final String HEADER_AA = "resultados.anonimos.punt.portales.aa";
	/** The Constant HEADER_NIVEL_DE_PRIORIDAD. */
	/** The Constant EMPTY_STRING. */
	private static final String EMPTY_STRING = "";
	/** The Constant JPG_EXTENSION. */
	private static final String JPG_EXTENSION = ".jpg";
	/** The Constant IMAGE_JPEG. */
	private static final String IMAGE_JPEG = "image/jpeg";
	/** The Constant CHART_EVOLUTION_MP_GREEN_COLOR. */
	private static final String CHART_EVOLUTION_MP_GREEN_COLOR = "chart.evolution.mp.green.color";
	/** The Constant GRAFICA_SIN_DATOS. */
	private static final String GRAFICA_SIN_DATOS = "grafica.sin.datos";
	/** The Constant BIG_DECIMAL_HUNDRED. */
	public static final BigDecimal BIG_DECIMAL_HUNDRED = BigDecimal.valueOf(100);
	/** The Constant FECHA_BOOKMARK. */
	private static final String FECHA_BOOKMARK = "[fecha]";
	/** The Constant TEXT_SPAN_NODE. */
	private static final String TEXT_SPAN_NODE = "text:span";
	/** The Constant TEXT_SECTION_NAME_S. */
	private static final String TEXT_SECTION_NAME_S = "//text:section[@text:name='%s']";
	/** The x. */
	private static int x = 0;
	/** The y. */
	private static int y = 0;
	static {
		PropertiesManager pmgr = new PropertiesManager();
		x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
		y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
	}
	/** The Constant messageResources. */
	final static MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);

	/**
	 * Builds the report.
	 *
	 * @param request        the request
	 * @param filePath       the file path
	 * @param graphicPath    the graphic path
	 * @param tagsToFilter   the tags to filter
	 * @param exObsIds       the ex obs ids
	 * @param reportTitle    the report title
	 * @param idBaseTemplate the id base template
	 * @return the odf text document
	 * @throws SQLException the SQL exception
	 * @throws Exception    the exception
	 */
	public static OdfTextDocument buildReport(final HttpServletRequest request, final String filePath, final String graphicPath, final String[] tagsToFilter, final String[] exObsIds,
			final String reportTitle, final Long idBaseTemplate) throws SQLException, Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		final String url = request.getRequestURL().toString();
		final String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
		final DatosForm userData = LoginDAO.getUserDataByName(DataBaseManager.getConnection(), request.getSession().getAttribute(Constants.USER).toString());
		final SimpleDateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					// Get all seeds from executed
					final List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(Constants.COMPLEXITY_SEGMENT_NONE, null, false, false, tagsToFilter, exObsIds);
					// Generate graphics
					generateGraphics(messageResources, filePath, true, tagsToFilter, exObsIds, pageExecutionList);
					// Load template
					final OdfTextDocument odt = getOdfTemplate(idBaseTemplate);
					final OdfFileDom odfFileContent = odt.getContentDom();
					final OdfFileDom odfStyles = odt.getStylesDom();
					// final List<ComplejidadForm> complexitivities = ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1);
					final List<ComplejidadForm> complexitivities = ComplejidadDAO.getComplejidadesObs(DataBaseManager.getConnection(), tagsToFilter, exObsIds);
					final List<AmbitoForm> ambits = AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1);
					replaceText(odt, odfFileContent, FECHA_BOOKMARK, df.format(new Date()));
					replaceText(odt, odfStyles, FECHA_BOOKMARK, df.format(new Date()), TEXT_SPAN_NODE);
					// Replace getGlobalReportStatistics section
					replaceGlobalReportStatisticsSection(graphicPath, pageExecutionList, messageResources, odt, odfFileContent, tagsToFilter, exObsIds);
					// Global sections
					replaceGlobalSection(graphicPath, pageExecutionList, messageResources, odt, odfFileContent, complexitivities, ambits, tagsToFilter, exObsIds);
					List<ObservatoryEvaluationForm> onlyPrimarys = new ArrayList<ObservatoryEvaluationForm>();
					// final List<AmbitoForm> primaryAmbits = AmbitoDAO.getAmbitosPrimarios(DataBaseManager.getConnection(), null, -1);
					final List<AmbitoForm> primaryAmbits = new ArrayList<>();
					for (int i = 0; i < exObsIds.length; i++) {
						AmbitoForm ambito = ObservatorioDAO.getAmbitByObservatoryExId(DataBaseManager.getConnection(), Long.parseLong(exObsIds[i]));
						if (ambito != null) {
							final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryPrinmayCategoriesAmbit(DataBaseManager.getConnection(), Long.parseLong(exObsIds[i]),
									Long.parseLong(ambito.getId()));
							if (categories != null && !categories.isEmpty()) {
								primaryAmbits.add(ambito);
								for (CategoriaForm cat : categories) {
									onlyPrimarys.addAll(filterObservatoriesByCategory(pageExecutionList, Long.parseLong(exObsIds[i]), Long.parseLong(cat.getId())));
								}
							}
						}
					}
//					final List<AmbitoForm> primaryAmbits = AmbitoDAO.getAmbitosPrimarios(DataBaseManager.getConnection(), null, -1);
					replaceAmbitPrimarySection(graphicPath, onlyPrimarys, messageResources, odt, odfFileContent, complexitivities, primaryAmbits, tagsToFilter, exObsIds);
					finishDocumentConfiguration(odt, odfFileContent, reportTitle);
					// Lists all files in folder
					File folder = new File("/tmp");
					File fList[] = folder.listFiles();
					// Searchs .lck
					for (int i = 0; i < fList.length; i++) {
						File pes = fList[i];
						if (pes.getName().endsWith(JPG_EXTENSION) || pes.getName().endsWith(ODT_EXTENSION)) {
							// and deletes
							pes.delete();
						}
					}
					String odtPath = filePath + "global_report.odt";
					odt.save(odtPath);
					removeAttributeFromFile(odtPath, "META-INF/manifest.xml", "manifest:file-entry", "manifest:size", "text/xml");
					odt.close();
					StringBuilder mailBody = new StringBuilder("El proceso del informe global informes ha finalizado. Puede descargarlo en el siguiente enlace: <br/>");
					StringBuilder linkUrl = new StringBuilder(baseURL);
					linkUrl.append("secure/NuevoInformeGlobalObservatorio.do?action=downloadFile");
					final String filename = odtPath.substring(odtPath.lastIndexOf(File.separator) + 1);
					linkUrl.append("&file=").append(filename);
					mailBody.append("<a href=\"").append(linkUrl.toString()).append("\">").append(filename).append("</a><br>");
					final MailService mailService = new MailService();
					List<String> mailsTo = new ArrayList<>();
					mailsTo.add(userData.getEmail());
					mailService.sendMail(mailsTo, "Generación de informe global completado", mailBody.toString(), true);
				} catch (Exception e) {
					Logger.putLog("Error", OpenOfficeGlobalReportBuilder.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		});
		return null;
	}

	/**
	 * Replace global report statistics section.
	 *
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param tagsToFiler       the tags to filer
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceGlobalReportStatisticsSection(final String graphicPath, final List<ObservatoryEvaluationForm> pageExecutionList, final MessageResources messageResources,
			final OdfTextDocument odt, final OdfFileDom odfFileContent, String[] tagsToFiler, final String[] exObsIds) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			// Dates of observatories
			generateDatesSummaryTable(odt, odfFileContent, tagsToFiler, exObsIds, c);
			// Todo total
			generateClasificationStatiticsTableGlobal(odt, odfFileContent, tagsToFiler, exObsIds, c, "Discapacidad");
			// Generate Distribution
			generateClasificationStatiticsTableAmbit(odt, odfFileContent, tagsToFiler, exObsIds, c);
			// By complex
			generateClasificationStatiticsTableComplex(odt, odfFileContent, tagsToFiler, exObsIds, c);
			// Temática
			generateClasificationStatiticsTable(odt, odfFileContent, tagsToFiler, exObsIds, c, 1);
			// Distribución geográfica
			generateClasificationStatiticsTable(odt, odfFileContent, tagsToFiler, exObsIds, c, 2);
			// Recurrencia
			generateClasificationStatiticsTable(odt, odfFileContent, tagsToFiler, exObsIds, c, 3);
			// Otros
			generateClasificationStatiticsTable(odt, odfFileContent, tagsToFiler, exObsIds, c, 4);
		} catch (SQLException e) {
			Logger.putLog("Error en getGlobalResultData", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Generate dates summary table.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param tagsToFiler    the tags to filer
	 * @param exObsIds       the ex obs ids
	 * @param c              the c
	 * @throws Exception                    the exception
	 * @throws SAXException                 the SAX exception
	 * @throws IOException                  Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	private static void generateDatesSummaryTable(final OdfTextDocument odt, final OdfFileDom odfFileContent, String[] tagsToFiler, final String[] exObsIds, Connection c)
			throws Exception, SAXException, IOException, ParserConfigurationException {
		List<ObservatorioRealizadoForm> list = ObservatorioDAO.getObserbatoriesDates(c, exObsIds);
		if (list != null && !list.isEmpty()) {
			String header0 = messageResources.getMessage(HEADER_AMBITO);
			String header1 = "Fecha del seguimiento";
			// For each clasification a table
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Fecha de realización del seguimiento</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascriterios");
			StringBuilder sb = generateObservatoriesDatesTable(header0, header1, list);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascriterios");
			appendParagraphToMarker(odt, odfFileContent, "tablascriterios");
		}
	}

	/**
	 * Generate observatories dates table.
	 *
	 * @param header0 the header 0
	 * @param header1 the header 1
	 * @param obs     the obs
	 * @return the string builder
	 */
	private static StringBuilder generateObservatoriesDatesTable(final String header0, final String header1, final List<ObservatorioRealizadoForm> obs) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table:table table:name='Table_Statics_").append("Segments").append("' table:style-name='TableGraphic'>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
		// Header row
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header0).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		// Rows
		for (int i = 0; i < obs.size(); i++) {
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(obs.get(i).getAmbito()).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(obs.get(i).getFechaStr());
			sb.append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
		}
		sb.append("</table:table>");
		return sb;
	}

	/**
	 * Generate clasification statitics table.
	 *
	 * @param odt             the odt
	 * @param odfFileContent  the odf file content
	 * @param tagsToFiler     the tags to filer
	 * @param exObsIds        the ex obs ids
	 * @param c               the c
	 * @param idClasificacion the id clasificacion
	 * @throws Exception                    the exception
	 * @throws SAXException                 the SAX exception
	 * @throws IOException                  Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	private static void generateClasificationStatiticsTable(final OdfTextDocument odt, final OdfFileDom odfFileContent, String[] tagsToFiler, final String[] exObsIds, Connection c,
			int idClasificacion) throws Exception, SAXException, IOException, ParserConfigurationException {
		List<GlobalReportStatistics> gre = RastreoDAO.getGlobalReportStatistics(c, tagsToFiler, exObsIds, idClasificacion);
		if (gre != null && !gre.isEmpty()) {
			String header0 = messageResources.getMessage(HEADER_ETIQUETA);
			String header1 = messageResources.getMessage(HEADER_NUM_SITIOS_WEB);
			// For each clasification a table
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>"
					+ messageResources.getMessage("global.report.criteria.table.title", gre.get(0).getNombreClasificacion()) + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascriterios");
			StringBuilder sb = generateStatisticsTable(header0, header1, gre);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascriterios");
			appendParagraphToMarker(odt, odfFileContent, "tablascriterios");
		}
	}

	/**
	 * Generate clasification statitics table.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param tagsToFiler    the tags to filer
	 * @param exObsIds       the ex obs ids
	 * @param c              the c
	 * @param tagName        the tag name
	 * @throws Exception                    the exception
	 * @throws SAXException                 the SAX exception
	 * @throws IOException                  Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	private static void generateClasificationStatiticsTableGlobal(final OdfTextDocument odt, final OdfFileDom odfFileContent, String[] tagsToFiler, final String[] exObsIds, Connection c,
			String tagName) throws Exception, SAXException, IOException, ParserConfigurationException {
		List<GlobalReportStatistics> gre1 = RastreoDAO.getGlobalReportStatisticsByAmbit(c, tagsToFiler, exObsIds);
		List<GlobalReportStatistics> gre2 = RastreoDAO.getGlobalReportStatisticsByTag(c, tagsToFiler, exObsIds, tagName);
		String header0 = "";
		String header1 = messageResources.getMessage(HEADER_NUM_SITIOS_WEB);
		// For each clasification a table
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + "Número de sitios web objeto del seguimiento simplificado" + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascriterios");
		// StringBuilder sb = generateStatisticsTable(header0, header1, gre);
		StringBuilder sb = new StringBuilder();
		sb.append("<table:table table:name='Table_Statics_").append("Global").append("' table:style-name='TableGraphic'>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
		// Header row
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header0).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		// Rows
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append("Total de sitios web").append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		int total = 0;
		if (gre1 != null && !gre1.isEmpty()) {
			for (GlobalReportStatistics gre : gre1) {
				total += gre.getCount();
			}
		}
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(total);
		sb.append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		/*
		 * sb.append("<table:table-row>"); sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		 * sb.append("<text:p text:style-name='GraphicTableCenter'>").append("Incluidos por las asociaciones de discapacidad").append("</text:p>"); sb.append("</table:table-cell>");
		 * sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>"); if (gre2 != null && !gre2.isEmpty()) {
		 * sb.append("<text:p text:style-name='GraphicTableCenter'>").append(gre2.get(0).getCount()); } else { sb.append("<text:p text:style-name='GraphicTableCenter'>").append(0); }
		 * sb.append("</text:p>"); sb.append("</table:table-cell>"); sb.append("</table:table-row>");
		 */
		sb.append("</table:table>");
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascriterios");
		appendParagraphToMarker(odt, odfFileContent, "tablascriterios");
	}
	//

	/**
	 * Generate clasification statitics table ambit.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param tagsToFiler    the tags to filer
	 * @param exObsIds       the ex obs ids
	 * @param c              the c
	 * @throws Exception                    the exception
	 * @throws SAXException                 the SAX exception
	 * @throws IOException                  Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	private static void generateClasificationStatiticsTableAmbit(final OdfTextDocument odt, final OdfFileDom odfFileContent, String[] tagsToFiler, final String[] exObsIds, Connection c)
			throws Exception, SAXException, IOException, ParserConfigurationException {
		List<GlobalReportStatistics> gre = RastreoDAO.getGlobalReportStatisticsByAmbit(c, tagsToFiler, exObsIds);
		if (gre != null && !gre.isEmpty()) {
			String header0 = messageResources.getMessage(HEADER_AMBITO);
			String header1 = messageResources.getMessage(HEADER_NUM_SITIOS_WEB);
			// For each clasification a table
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + "Número de sitios web por ámbito" + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascriterios");
			StringBuilder sb = generateStatisticsTable(header0, header1, gre);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascriterios");
			appendParagraphToMarker(odt, odfFileContent, "tablascriterios");
		}
	}

	/**
	 * Generate clasification statitics table complex.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param tagsToFiler    the tags to filer
	 * @param exObsIds       the ex obs ids
	 * @param c              the c
	 * @throws Exception                    the exception
	 * @throws SAXException                 the SAX exception
	 * @throws IOException                  Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	private static void generateClasificationStatiticsTableComplex(final OdfTextDocument odt, final OdfFileDom odfFileContent, String[] tagsToFiler, final String[] exObsIds, Connection c)
			throws Exception, SAXException, IOException, ParserConfigurationException {
		List<GlobalReportStatistics> gre = RastreoDAO.getGlobalReportStatisticsByComplex(c, tagsToFiler, exObsIds);
		if (gre != null && !gre.isEmpty()) {
			String header0 = messageResources.getMessage(HEADER_COMPLEJIDAD);
			String header1 = messageResources.getMessage(HEADER_NUM_SITIOS_WEB);
			// For each clasification a table
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + "Número de sitios web por complejidad" + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascriterios");
			StringBuilder sb = generateStatisticsTable(header0, header1, gre);
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascriterios");
			appendParagraphToMarker(odt, odfFileContent, "tablascriterios");
		}
	}

	/**
	 * Generate statistics table.
	 *
	 * @param header0    the header 0
	 * @param header1    the header 1
	 * @param statistics the statistics
	 * @return the string builder
	 */
	private static StringBuilder generateStatisticsTable(final String header0, final String header1, final List<GlobalReportStatistics> statistics) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table:table table:name='Table_Statics_").append("Segments").append("' table:style-name='TableGraphic'>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
		// Header row
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header0).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		// Rows
		for (int i = 0; i < statistics.size(); i++) {
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(statistics.get(i).getNombre()).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(statistics.get(i).getCount());
			sb.append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
		}
		sb.append("</table:table>");
		return sb;
	}

	/**
	 * Gets the global result data.
	 *
	 * @param filterId           the category id
	 * @param pageExecutionList  the page execution list
	 * @param isComplexityFilter the is complexity filter
	 * @param isAmbitFilter      the is ambit filter
	 * @param tagsFilter         the tags filter
	 * @param exObsIds           the ex obs ids
	 * @return the global result data
	 * @throws Exception the exception
	 */
	private static List<ObservatoryEvaluationForm> getGlobalResultData(final long filterId, final List<ObservatoryEvaluationForm> pageExecutionList, boolean isComplexityFilter, boolean isAmbitFilter,
			String[] tagsFilter, String[] exObsIds) throws Exception {
		List<ObservatoryEvaluationForm> observatoryEvaluationList;
		try (Connection c = DataBaseManager.getConnection()) {
			observatoryEvaluationList = new ArrayList<>();
			final List<Long> listAnalysis = new ArrayList<>();
			List<Long> listExecutionsIds = new ArrayList<>();
			for (String executionId : exObsIds) {
				List<ObservatoryEvaluationForm> tmpList = new ArrayList<>();
				// Filter by tags
				if (tagsFilter != null && tagsFilter.length > 0) {
					listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIdsMatchTags(c, Long.parseLong(executionId), tagsFilter);
				} else {
					listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(c, Long.parseLong(executionId), Constants.COMPLEXITY_SEGMENT_NONE);
				}
				if (pageExecutionList == null) {
					for (Long idExecution : listExecutionsIds) {
						listAnalysis.addAll(AnalisisDatos.getAnalysisIdsByTracking(c, idExecution));
					}
					// Inicializamos el evaluador
					if (!EvaluatorUtility.isInitialized()) {
						EvaluatorUtility.initialize();
					}
					final Evaluator evaluator = new Evaluator();
					for (Long idAnalysis : listAnalysis) {
						final Evaluation evaluation = evaluator.getObservatoryAnalisisDB(c, idAnalysis, EvaluatorUtils.getDocList());
						final String methodology = ObservatorioDAO.getMethodology(c, Long.parseLong(executionId));
						final ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false, true);
						evaluationForm.setObservatoryExecutionId(Long.parseLong(executionId));
						final FulfilledCrawlingForm ffCrawling = RastreoDAO.getFullfilledCrawlingExecution(c, evaluationForm.getCrawlerExecutionId());
						if (ffCrawling != null) {
							final SeedForm seedForm = new SeedForm();
							seedForm.setId(String.valueOf(ffCrawling.getSeed().getId()));
							seedForm.setAcronym(ffCrawling.getSeed().getAcronimo());
							seedForm.setName(ffCrawling.getSeed().getNombre());
							// Multidependencia
							seedForm.setCategory(ffCrawling.getSeed().getCategoria().getName());
							evaluationForm.setSeed(seedForm);
						}
						tmpList.add(evaluationForm);
					}
				} else {
					for (ObservatoryEvaluationForm observatory : pageExecutionList) {
						if (listExecutionsIds.contains(observatory.getCrawlerExecutionId())) {
							tmpList.add(observatory);
						}
					}
				}
				if (isAmbitFilter) {
					observatoryEvaluationList.addAll(filterObservatoriesByAmbit(tmpList, Long.parseLong(executionId), filterId));
				} else if (isComplexityFilter) {
					observatoryEvaluationList.addAll(filterObservatoriesByComplexity(tmpList, Long.parseLong(executionId), filterId));
				} else {
					observatoryEvaluationList.addAll(tmpList);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error en getGlobalResultData", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return observatoryEvaluationList;
	}

	/**
	 * Filter observatories by complexity.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @param executionId               the execution id
	 * @param complexityId              the complexity id
	 * @return the list
	 * @throws Exception the exception
	 */
	private static List<ObservatoryEvaluationForm> filterObservatoriesByComplexity(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId, final Long complexityId)
			throws Exception {
		if (complexityId == Constants.COMPLEXITY_SEGMENT_NONE) {
			return observatoryEvaluationList;
		} else {
			final List<ObservatoryEvaluationForm> results = new ArrayList<>();
			try (Connection conn = DataBaseManager.getConnection()) {
				final List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIdsComplexity(conn, executionId, complexityId);
				for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
					if (listExecutionsIds.contains(observatoryEvaluationForm.getCrawlerExecutionId())) {
						results.add(observatoryEvaluationForm);
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al filtrar observatorios por complejidad. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			return results;
		}
	}

	/**
	 * Filter observatories by complexity.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @param executionId               the execution id
	 * @param categoryId                the category id
	 * @return the list
	 * @throws Exception the exception
	 */
	private static List<ObservatoryEvaluationForm> filterObservatoriesByCategory(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId, final long categoryId)
			throws Exception {
		if (categoryId == Constants.COMPLEXITY_SEGMENT_NONE) {
			return observatoryEvaluationList;
		} else {
			final List<ObservatoryEvaluationForm> results = new ArrayList<>();
			try (Connection conn = DataBaseManager.getConnection()) {
				final List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, executionId, categoryId);
				for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
					if (listExecutionsIds.contains(observatoryEvaluationForm.getCrawlerExecutionId())) {
						results.add(observatoryEvaluationForm);
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al filtrar observatorios. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			return results;
		}
	}

	/**
	 * Filter observatories by ambit.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @param executionId               the execution id
	 * @param ambitId                   the ambit id
	 * @return the list
	 * @throws Exception the exception
	 */
	private static List<ObservatoryEvaluationForm> filterObservatoriesByAmbit(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId, final Long ambitId)
			throws Exception {
		if (ambitId == 0) {
			return observatoryEvaluationList;
		} else {
			final List<ObservatoryEvaluationForm> results = new ArrayList<>();
			try (Connection conn = DataBaseManager.getConnection()) {
				final List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIdsAmbit(conn, executionId, ambitId);
				for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
					if (listExecutionsIds.contains(observatoryEvaluationForm.getCrawlerExecutionId())) {
						results.add(observatoryEvaluationForm);
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al filtrar observatorios por complejidad. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			return results;
		}
	}

	/**
	 * Generate graphics.
	 *
	 * @param messageResources  the message resources
	 * @param filePath          the file path
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @param pageExecutionList the page execution list
	 * @throws Exception the exception
	 */
	private static void generateGraphics(final MessageResources messageResources, final String filePath, final boolean regenerate, final String[] tagsFilter, final String[] exObsIds,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final PropertiesManager pmgr = new PropertiesManager();
			String color = pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR);
			// Gráficos globales
			generateGlobalGraphics(messageResources, filePath, color, regenerate, tagsFilter, pageExecutionList, exObsIds);
		} catch (Exception e) {
			Logger.putLog("No se han generado las gráficas correctamente.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Genera los gráficos globales del observatorio.
	 *
	 * @param messageResources  Message resources para los textos parametrizados
	 * @param filePath          Ruta para guardar los temporales
	 * @param color             Color del gráfico
	 * @param regenerate        Indica si hay que regenerar el gráfico o no.
	 * @param tagsFilter        the tags filter
	 * @param pageExecutionList the page execution list
	 * @param exObsIds          the ex obs ids
	 * @return Mapa de gráficos
	 * @throws Exception the exception
	 */
	private static Map<String, Object> generateGlobalGraphics(final MessageResources messageResources, final String filePath, final String color, final boolean regenerate, final String[] tagsFilter,
			final List<ObservatoryEvaluationForm> pageExecutionList, final String[] exObsIds) throws Exception {
		final Map<String, Object> globalGraphics = new HashMap<>();
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
			// List<ComplejidadForm> complejidades = ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1);
			final List<ComplejidadForm> complejidades = ComplejidadDAO.getComplejidadesObs(DataBaseManager.getConnection(), tagsFilter, exObsIds);
			List<AmbitoForm> ambitos = AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1);
			// Distribución del nivel de adecuación estimado global
			String file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_NAME) + JPG_EXTENSION;
			String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.title");
			ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, pageExecutionList, globalGraphics, title, file, noDataMess, regenerate);
			// Distribución de la situación de cumplimiento estimada global
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME) + JPG_EXTENSION;
			ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalCompilanceGraphic(messageResources, pageExecutionList, globalGraphics, title, file, noDataMess, regenerate);
			// Comparación de la conformidad de las verificaciones
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_NAME) + JPG_EXTENSION;
			ResultadosAnonimosObservatorioUNEEN2019Utils.getCompilanceComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_1, title, file, noDataMess,
					pageExecutionList, color, regenerate);
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_NAME) + JPG_EXTENSION;
			ResultadosAnonimosObservatorioUNEEN2019Utils.getCompilanceComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_2, title, file, noDataMess,
					pageExecutionList, color, regenerate);
			// Comparación del nivel de adecuación estimado por complejidad
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_NAME) + JPG_EXTENSION;
			getGloballAllocationByComplexity(messageResources, globalGraphics, file, noDataMess, pageExecutionList, complejidades, regenerate, tagsFilter, title, exObsIds);
			// Comparación de la situación de cumplimiento estimada por complejidad
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_NAME) + JPG_EXTENSION;
			getGlobalCompilanceByComplexitivity(messageResources, globalGraphics, file, noDataMess, pageExecutionList, complejidades, regenerate, tagsFilter, title, exObsIds);
			// ALLOCATION BY AMBIT
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_MARK_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_MARK_NAME) + JPG_EXTENSION;
			getGloballAllocationByAmbit(messageResources, globalGraphics, file, noDataMess, pageExecutionList, ambitos, regenerate, tagsFilter, title, exObsIds);
			// Compliance by ambit
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPLIANCE_AMBIT_MARK_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_MARK_NAME) + JPG_EXTENSION;
			getGloballComplianceByAmbit(messageResources, globalGraphics, file, noDataMess, pageExecutionList, ambitos, regenerate, tagsFilter, title, exObsIds);
			/**
			 * Primarys
			 */
			final List<ObservatoryEvaluationForm> onlyPrimarys = new ArrayList<ObservatoryEvaluationForm>();
			final List<AmbitoForm> primaryAmbits = new ArrayList<>();
			for (int i = 0; i < exObsIds.length; i++) {
				AmbitoForm ambito = ObservatorioDAO.getAmbitByObservatoryExId(DataBaseManager.getConnection(), Long.parseLong(exObsIds[i]));
				if (ambito != null) {
					final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryPrinmayCategoriesAmbit(DataBaseManager.getConnection(), Long.parseLong(exObsIds[i]),
							Long.parseLong(ambito.getId()));
					if (categories != null && !categories.isEmpty()) {
						primaryAmbits.add(ambito);
						for (CategoriaForm cat : categories) {
							onlyPrimarys.addAll(filterObservatoriesByCategory(pageExecutionList, Long.parseLong(exObsIds[i]), Long.parseLong(cat.getId())));
						}
					}
				}
			}
			// final List<AmbitoForm> primaryAmbits = AmbitoDAO.getAmbitosPrimarios(DataBaseManager.getConnection(), null, -1);
			// ALLOCATION BY AMBIT
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_PRIMARY_MARK_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_PRIMARY_MARK_NAME) + JPG_EXTENSION;
			getGloballAllocationByAmbit(messageResources, globalGraphics, file, noDataMess, onlyPrimarys, primaryAmbits, regenerate, tagsFilter, title, exObsIds);
			// Compliance by ambit
			title = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPLIANCE_AMBIT_PRIMARY_MARK_TITLE);
			file = filePath + messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_PRIMARY_MARK_NAME) + JPG_EXTENSION;
			getGloballComplianceByAmbit(messageResources, globalGraphics, file, noDataMess, onlyPrimarys, primaryAmbits, regenerate, tagsFilter, title, exObsIds);
		}
		return globalGraphics;
	}

	/**
	 * Gets the global mark by complexity group graphic.
	 *
	 * @param messageResources  the message resources
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param complexities      the complexities
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @param exObsIds          the ex obs ids
	 * @return the global mark by complexity group graphic
	 * @throws Exception the exception
	 */
	public static void getGloballAllocationByComplexity(final MessageResources messageResources, Map<String, Object> globalGraphics, final String filePath, final String noDataMess,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<ComplejidadForm> complexities, final boolean regenerate, String[] tagsFilter, final String title,
			final String[] exObsIds) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexities);
		final List<ComplexityViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageResultsByComplexityMap(pageExecutionList, resultLists.get(i), tagsFilter, exObsIds);
			final DefaultCategoryDataset dataSet = ResultadosAnonimosObservatorioUNEEN2019Utils.createDataSetComplexity(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, CHART_OBSERVATORY_GRAPHIC_INTAV_COLORS));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			}
			// Incluimos los resultados en la request
			for (ComplejidadForm category : resultLists.get(i)) {
				final ComplexityViewListForm categoryView = new ComplexityViewListForm(category,
						ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonAllocationPuntuation(messageResources, resultsBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CPS_GRAPH, resultLists.size());
	}

	/**
	 * Gets the globall allocation by ambit.
	 *
	 * @param messageResources  the message resources
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param ambits            the ambits
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @param exObsIds          the ex obs ids
	 * @return the globall allocation by ambit
	 * @throws Exception the exception
	 */
	public static void getGloballAllocationByAmbit(final MessageResources messageResources, Map<String, Object> globalGraphics, final String filePath, final String noDataMess,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<AmbitoForm> ambits, final boolean regenerate, String[] tagsFilter, final String title, final String[] exObsIds)
			throws Exception {
		final Map<Integer, List<AmbitoForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapAmbits(ambits);
		final List<AmbitViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			final Map<AmbitoForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageAllocationResultsByAmbitMap(pageExecutionList, ambits, tagsFilter, exObsIds);
			final DefaultCategoryDataset dataSet = ResultadosAnonimosObservatorioUNEEN2019Utils.createDataSetAmbit(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, CHART_OBSERVATORY_GRAPHIC_INTAV_COLORS));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			}
			// Incluimos los resultados en la request
			for (AmbitoForm ambit : resultLists.get(i)) {
				final AmbitViewListForm categoryView = new AmbitViewListForm(ambit,
						ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonAllocationPuntuation(messageResources, resultsBySegment.get(ambit)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CPS_GRAPH, resultLists.size());
	}

	/**
	 * Calculate percentage results by complexity map.
	 *
	 * @param pageExecutionList the page execution list
	 * @param complexities      the complexities
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<ComplejidadForm, Map<String, BigDecimal>> calculatePercentageResultsByComplexityMap(final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<ComplejidadForm> complexities, final String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<ComplejidadForm>() {
			@Override
			public int compare(ComplejidadForm o1, ComplejidadForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (ComplejidadForm complexity : complexities) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(Long.parseLong(complexity.getId()), pageExecutionList, true, false, tagsFilter, exObsIds);
			resultsBySegment.put(complexity, ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentage(ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Gets the global compilance by complexitivity.
	 *
	 * @param messageResources  the message resources
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param complexities      the complexities
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @param exObsIds          the ex obs ids
	 * @return the global compilance by complexitivity
	 * @throws Exception the exception
	 */
	public static void getGlobalCompilanceByComplexitivity(final MessageResources messageResources, Map<String, Object> globalGraphics, final String filePath, final String noDataMess,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<ComplejidadForm> complexities, final boolean regenerate, String[] tagsFilter, final String title,
			final String[] exObsIds) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexities);
		final List<ComplexityViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageCompilanceResultsByComplexitivityMap(pageExecutionList, resultLists.get(i), tagsFilter, exObsIds);
			final DefaultCategoryDataset dataSet = ResultadosAnonimosObservatorioUNEEN2019Utils.createDataSetComplexityCompilance(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, CHART_OBSERVATORY_GRAPHIC_INTAV_COLORS));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			}
			// Incluimos los resultados en la request
			for (ComplejidadForm complexitivity : resultLists.get(i)) {
				final ComplexityViewListForm categoryView = new ComplexityViewListForm(complexitivity,
						ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonCompilancePuntuaction(messageResources, resultsBySegment.get(complexitivity)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPC, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CMPC_GRAPH, resultLists.size());
	}

	/**
	 * Gets the globall compliance by ambit.
	 *
	 * @param messageResources  the message resources
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param ambits            the ambits
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @param exObsIds          the ex obs ids
	 * @return the globall compliance by ambit
	 * @throws Exception the exception
	 */
	public static void getGloballComplianceByAmbit(final MessageResources messageResources, Map<String, Object> globalGraphics, final String filePath, final String noDataMess,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<AmbitoForm> ambits, final boolean regenerate, String[] tagsFilter, final String title, final String[] exObsIds)
			throws Exception {
		final Map<Integer, List<AmbitoForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapAmbits(ambits);
		final List<AmbitViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			final Map<AmbitoForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageCompilanceResultsByAmbitMap(pageExecutionList, resultLists.get(i), tagsFilter, exObsIds);
			final DefaultCategoryDataset dataSet = ResultadosAnonimosObservatorioUNEEN2019Utils.createDataSetAmbitCompilance(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, CHART_OBSERVATORY_GRAPHIC_INTAV_COLORS));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(JPG_EXTENSION)) + i + JPG_EXTENSION);
			}
			// Incluimos los resultados en la request
			for (AmbitoForm ambit : resultLists.get(i)) {
				final AmbitViewListForm categoryView = new AmbitViewListForm(ambit,
						ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonCompilancePuntuaction(messageResources, resultsBySegment.get(ambit)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPC, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CMPC_GRAPH, resultLists.size());
	}

	/**
	 * Calculate percentage compilance results by complexitivity map.
	 *
	 * @param pageExecutionList the page execution list
	 * @param completivities    the completivities
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<ComplejidadForm, Map<String, BigDecimal>> calculatePercentageCompilanceResultsByComplexitivityMap(final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<ComplejidadForm> completivities, String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<ComplejidadForm>() {
			@Override
			public int compare(ComplejidadForm o1, ComplejidadForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (ComplejidadForm complexitivity : completivities) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(Long.parseLong(complexitivity.getId()), pageExecutionList, true, false, tagsFilter, exObsIds);
			resultsBySegment.put(complexitivity,
					ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentage(ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteCompilance(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Calculate percentage allocation results by ambit map.
	 *
	 * @param pageExecutionList the page execution list
	 * @param ambits            the ambits
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<AmbitoForm, Map<String, BigDecimal>> calculatePercentageAllocationResultsByAmbitMap(final List<ObservatoryEvaluationForm> pageExecutionList, final List<AmbitoForm> ambits,
			String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<AmbitoForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<AmbitoForm>() {
			@Override
			public int compare(AmbitoForm o1, AmbitoForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (AmbitoForm ambit : ambits) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(Long.parseLong(ambit.getId()), pageExecutionList, false, true, tagsFilter, exObsIds);
			resultsBySegment.put(ambit, ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentage(ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Calculate percentage compilance results by ambit map.
	 *
	 * @param pageExecutionList the page execution list
	 * @param ambits            the ambits
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<AmbitoForm, Map<String, BigDecimal>> calculatePercentageCompilanceResultsByAmbitMap(final List<ObservatoryEvaluationForm> pageExecutionList, final List<AmbitoForm> ambits,
			String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<AmbitoForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<AmbitoForm>() {
			@Override
			public int compare(AmbitoForm o1, AmbitoForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (AmbitoForm ambit : ambits) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(Long.parseLong(ambit.getId()), pageExecutionList, false, true, tagsFilter, exObsIds);
			resultsBySegment.put(ambit, ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentage(ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteCompilance(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Replace global section.
	 *
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param complexitivities  the complexitivities
	 * @param ambits            the ambits
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceGlobalSection(final String graphicPath, final List<ObservatoryEvaluationForm> pageExecutionList, final MessageResources messageResources, final OdfTextDocument odt,
			final OdfFileDom odfFileContent, List<ComplejidadForm> complexitivities, final List<AmbitoForm> ambits, String[] tagsFilter, final String[] exObsIds) throws Exception {
		replaceSectionGlobalAccesibilityDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionGlobalCompilanceDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionCompilanceByVerificationLevel1(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionCompilanceByVerificationLevel2(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionComparisionPuntuactionAllocationComplexity(messageResources, odt, odfFileContent, graphicPath, complexitivities, pageExecutionList, tagsFilter, exObsIds);
		replaceSectionComparisionPercentajeCompilanceComplexitivy(messageResources, odt, odfFileContent, graphicPath, complexitivities, pageExecutionList, tagsFilter, exObsIds);
		replaceSectionComparisionPuntuactionAllocationAmbit(messageResources, odt, odfFileContent, graphicPath, ambits, pageExecutionList, tagsFilter, exObsIds);
		replaceSectionComparisionPercentajeCompilanceAmbit(messageResources, odt, odfFileContent, graphicPath, ambits, pageExecutionList, tagsFilter, exObsIds);
	}

	/**
	 * Replace ambit primary section.
	 *
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param complexitivities  the complexitivities
	 * @param ambits            the ambits
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceAmbitPrimarySection(final String graphicPath, final List<ObservatoryEvaluationForm> pageExecutionList, final MessageResources messageResources,
			final OdfTextDocument odt, final OdfFileDom odfFileContent, List<ComplejidadForm> complexitivities, final List<AmbitoForm> ambits, String[] tagsFilter, final String[] exObsIds)
			throws Exception {
		// AMBIT PRINCIPALES
		replaceSectionComparisionPuntuactionAllocationAmbitPrimary(messageResources, odt, odfFileContent, graphicPath, ambits, pageExecutionList, tagsFilter, exObsIds);
		replaceSectionComparisionPercentajeCompilanceAmbitPrimary(messageResources, odt, odfFileContent, graphicPath, ambits, pageExecutionList, tagsFilter, exObsIds);
	}

	/**
	 * Replace global section for allocation distribution.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private static void replaceSectionGlobalAccesibilityDistribution(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_NAME);
		replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
		Map<String, Integer> result = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(pageExecutionList);
		List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalAccessibilityLevel(messageResources, result);
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_1, labelValueBean.get(0).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_1, labelValueBean.get(1).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_1, labelValueBean.get(2).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_1_CELL_2, labelValueBean.get(0).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_2_CELL_2, labelValueBean.get(1).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_ACCESIBILITY_DISTRIBUTION_CELL_ROW_3_CELL_2, labelValueBean.get(2).getNumberP());
	}

	/**
	 * Replace global section for compliance distribution.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private static void replaceSectionGlobalCompilanceDistribution(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_COMPILANCE_LEVEL_ALLOCATION_NAME);
		replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
		Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		final Map<String, Integer> resultCompilance = ResultadosAnonimosObservatorioUNEEN2019Utils.getSityesByCompliance(results);
		List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalCompilanceLevel(messageResources, resultCompilance);
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_1, labelValueBean.get(1).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_1, labelValueBean.get(0).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_1, labelValueBean.get(2).getPercentageP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_1_CELL_2, labelValueBean.get(0).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_2_CELL_2, labelValueBean.get(1).getNumberP());
		replaceText(odt, odfFileContent, TABLE_GLOBAL_COMPLIANCE_DISTRIBUTION_CELL_ROW_3_CELL_2, labelValueBean.get(2).getNumberP());
	}

	/**
	 * Replace section compilance by verification level 1.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @throws Exception the exception
	 */
	private static void replaceSectionCompilanceByVerificationLevel1(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_1_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_1);
		final List<ComplianceComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils
				.infoLevelVerificationCompilanceComparison(ResultadosAnonimosObservatorioUNEEN2019Utils.generatePercentajesCompilanceVerification(results));
		replaceText(odt, odfFileContent, "-451c.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d2-", res.get(0).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d3-", res.get(1).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d4-", res.get(2).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d5-", res.get(3).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d6-", res.get(4).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c7-", res.get(5).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d7-", res.get(5).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b8-", res.get(6).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c8-", res.get(6).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d8-", res.get(6).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b9-", res.get(7).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c9-", res.get(7).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d9-", res.get(7).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b10-", res.get(8).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c10-", res.get(8).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d10-", res.get(8).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b11-", res.get(9).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c11-", res.get(9).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d11-", res.get(9).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b12-", res.get(10).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c12-", res.get(10).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d12-", res.get(10).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b13-", res.get(11).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c13-", res.get(11).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d13-", res.get(11).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b14-", res.get(12).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c14-", res.get(12).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d14-", res.get(12).getGrayPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.b15-", res.get(13).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.c15-", res.get(13).getRedPercentage());
		replaceText(odt, odfFileContent, "-451c.t1.d15-", res.get(13).getGrayPercentage());
	}

	/**
	 * Replace section compilance by verification level 2.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @throws Exception the exception
	 */
	private static void replaceSectionCompilanceByVerificationLevel2(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_VERIFICATION_COMPILANCE_COMPARATION_LEVEL_2_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_2);
		final List<ComplianceComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils
				.infoLevelVerificationCompilanceComparison(ResultadosAnonimosObservatorioUNEEN2019Utils.generatePercentajesCompilanceVerification(results));
		replaceText(odt, odfFileContent, "-452c.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.d2-", res.get(0).getGrayPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.d3-", res.get(1).getGrayPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.d4-", res.get(2).getGrayPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.d5-", res.get(3).getGrayPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.d6-", res.get(4).getGrayPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.c7-", res.get(5).getRedPercentage());
		replaceText(odt, odfFileContent, "-452c.t1.d7-", res.get(5).getGrayPercentage());
	}

	/**
	 * Replace section comparision puntuaction allocation ambit.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param ambits            the ambits
	 * @param pageExecutionList the page execution list
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceSectionComparisionPuntuactionAllocationAmbit(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<AmbitoForm> ambits, final List<ObservatoryEvaluationForm> pageExecutionList, String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<Integer, List<AmbitoForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapAmbits(ambits);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		// Table
		final Map<AmbitoForm, Map<String, BigDecimal>> res = calculatePercentageAllocationResultsByAmbitMap(pageExecutionList, AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1),
				tagsFilter, exObsIds);
		String columna1 = messageResources.getMessage(HEADER_AA);
		String columna2 = messageResources.getMessage(HEADER_A);
		String columna3 = messageResources.getMessage(HEADER_NO_VALIDO);
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + messageResources.getMessage("global.report.allocation.ambit.table.title") + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLAS_ADECUACION_BOOKMARK);
		StringBuilder sb = generateTableRowAmbit("AllocationAmbit", messageResources.getMessage(HEADER_AMBITO), columna1, columna2, columna3, ambits, res, messageResources, TYPE_ALLOCATION, true);
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLAS_ADECUACION_BOOKMARK);
		appendParagraphToMarker(odt, odfFileContent, TABLAS_ADECUACION_BOOKMARK);
		addTableStyles(odt);
	}

	/**
	 * Replace section comparision puntuaction allocation ambit primary.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param ambits            the ambits
	 * @param pageExecutionList the page execution list
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceSectionComparisionPuntuactionAllocationAmbitPrimary(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<AmbitoForm> ambits, final List<ObservatoryEvaluationForm> pageExecutionList, String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<Integer, List<AmbitoForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapAmbits(ambits);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_AMBIT_PRIMARY_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		// Table
		final Map<AmbitoForm, Map<String, BigDecimal>> res = calculatePercentageAllocationResultsByAmbitMap(pageExecutionList, AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1),
				tagsFilter, exObsIds);
		String columna1 = messageResources.getMessage(HEADER_AA);
		String columna2 = messageResources.getMessage(HEADER_A);
		String columna3 = messageResources.getMessage(HEADER_NO_VALIDO);
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + messageResources.getMessage("global.report.allocation.ambit.primary.table.title") + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLAS_ADECUACION_PRINCIPALES_BOOKMARK);
		StringBuilder sb = generateTableRowAmbit("AllocationAmbitPrinary", messageResources.getMessage(HEADER_AMBITO), columna1, columna2, columna3, ambits, res, messageResources, TYPE_ALLOCATION,
				true);
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLAS_ADECUACION_PRINCIPALES_BOOKMARK);
		appendParagraphToMarker(odt, odfFileContent, TABLAS_ADECUACION_PRINCIPALES_BOOKMARK);
		addTableStyles(odt);
	}

	/**
	 * Replace section comparision puntuaction allocation complexity.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param complexities      the complexities
	 * @param pageExecutionList the page execution list
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceSectionComparisionPuntuactionAllocationComplexity(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<ComplejidadForm> complexities, final List<ObservatoryEvaluationForm> pageExecutionList, String[] tagsFilter, final String[] exObsIds)
			throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexities);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_ALLOCATION_COMPLEXITY_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<ComplejidadForm, Map<String, BigDecimal>> res = calculatePercentageResultsByComplexityMap(pageExecutionList,
				ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1), tagsFilter, exObsIds);
		String columna1 = messageResources.getMessage(HEADER_AA);
		String columna2 = messageResources.getMessage(HEADER_A);
		String columna3 = messageResources.getMessage(HEADER_NO_VALIDO);
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + messageResources.getMessage("global.report.allocation.complexitivity.table.title") + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASCOMPLEJIDAD_BOOKMARK);
		StringBuilder sb = generateTableRowComplexity("AllocationComplexity", messageResources.getMessage(HEADER_COMPLEJIDAD), columna1, columna2, columna3, complexities, res, messageResources,
				TYPE_ALLOCATION, true);
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASCOMPLEJIDAD_BOOKMARK);
		appendParagraphToMarker(odt, odfFileContent, TABLASCOMPLEJIDAD_BOOKMARK);
		addTableStyles(odt);
	}

	/**
	 * Replace section comparision percentaje compilance complexitivy.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param complexitivities  the complexitivities
	 * @param pageExecutionList the page execution list
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceSectionComparisionPercentajeCompilanceComplexitivy(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<ComplejidadForm> complexitivities, final List<ObservatoryEvaluationForm> pageExecutionList, String[] tagsFilter, final String[] exObsIds)
			throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexitivities);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_COMPLEXITIVIY_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<ComplejidadForm, Map<String, BigDecimal>> res = calculatePercentageCompilanceResultsByComplexitivityMap(pageExecutionList,
				ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1), tagsFilter, exObsIds);
		String columna1 = messageResources.getMessage(HEADER_TOTALMENTE_CONFORME);
		String columna2 = messageResources.getMessage(HEADER_PARCIALMENTE_CONFORME);
		String columna3 = messageResources.getMessage(HEADER_NO_CONFORME);
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + messageResources.getMessage("global.report.compliance.complexitivity.table.title") + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK);
		StringBuilder sb = generateTableRowComplexity("CompilanceComplexitivy", messageResources.getMessage(HEADER_COMPLEJIDAD), columna1, columna2, columna3, complexitivities, res, messageResources,
				TYPE_COMPLIANCE, true);
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK);
		appendParagraphToMarker(odt, odfFileContent, TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK);
		addTableStyles(odt);
	}

	/**
	 * Replace section comparision percentaje compilance ambit.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param ambits            the ambits
	 * @param pageExecutionList the page execution list
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceSectionComparisionPercentajeCompilanceAmbit(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<AmbitoForm> ambits, final List<ObservatoryEvaluationForm> pageExecutionList, String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<Integer, List<AmbitoForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapAmbits(ambits);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<AmbitoForm, Map<String, BigDecimal>> res = calculatePercentageCompilanceResultsByAmbitMap(pageExecutionList, AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1),
				tagsFilter, exObsIds);
		String columna1 = messageResources.getMessage(HEADER_TOTALMENTE_CONFORME);
		String columna2 = messageResources.getMessage(HEADER_PARCIALMENTE_CONFORME);
		String columna3 = messageResources.getMessage(HEADER_NO_CONFORME);
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + messageResources.getMessage("global.report.compliance.ambit.table.title") + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLAS_CUMPLIMIENTO_BOOKMARK);
		StringBuilder sb = generateTableRowAmbit("CompilanceAmbit", messageResources.getMessage(HEADER_AMBITO), columna1, columna2, columna3, ambits, res, messageResources, TYPE_COMPLIANCE, true);
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLAS_CUMPLIMIENTO_BOOKMARK);
		appendParagraphToMarker(odt, odfFileContent, TABLAS_CUMPLIMIENTO_BOOKMARK);
		addTableStyles(odt);
	}

	/**
	 * Replace section comparision percentaje compilance ambit.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param ambits            the ambits
	 * @param pageExecutionList the page execution list
	 * @param tagsFilter        the tags filter
	 * @param exObsIds          the ex obs ids
	 * @throws Exception the exception
	 */
	private static void replaceSectionComparisionPercentajeCompilanceAmbitPrimary(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<AmbitoForm> ambits, final List<ObservatoryEvaluationForm> pageExecutionList, String[] tagsFilter, final String[] exObsIds) throws Exception {
		final Map<Integer, List<AmbitoForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapAmbits(ambits);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_GLOBAL_PUNTUATION_COMPILANCE_AMBIT_PRIMARY_MARK_NAME) + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG, prevImage);
			}
		}
		final Map<AmbitoForm, Map<String, BigDecimal>> res = calculatePercentageCompilanceResultsByAmbitMap(pageExecutionList, AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1),
				tagsFilter, exObsIds);
		String columna1 = messageResources.getMessage(HEADER_TOTALMENTE_CONFORME);
		String columna2 = messageResources.getMessage(HEADER_PARCIALMENTE_CONFORME);
		String columna3 = messageResources.getMessage(HEADER_NO_CONFORME);
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + messageResources.getMessage("global.report.compliance.ambit.primary.table.title") + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, TABLAS_CUMPLIMIENTO_PRINCIPALES_BOOKMARK);
		StringBuilder sb = generateTableRowAmbit("CompilanceAmbitPrimary", messageResources.getMessage(HEADER_AMBITO), columna1, columna2, columna3, ambits, res, messageResources, TYPE_COMPLIANCE,
				true);
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, TABLAS_CUMPLIMIENTO_PRINCIPALES_BOOKMARK);
		appendParagraphToMarker(odt, odfFileContent, TABLAS_CUMPLIMIENTO_PRINCIPALES_BOOKMARK);
		addTableStyles(odt);
	}

	/**
	 * Gets the odf template.
	 *
	 * @param idBaseTemplate the id base template
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private static OdfTextDocument getOdfTemplate(final Long idBaseTemplate) throws Exception {
		PlantillaForm plantilla = PlantillaDAO.findById(DataBaseManager.getConnection(), idBaseTemplate);
		if (plantilla != null && plantilla.getDocumento() != null && plantilla.getDocumento().length > 0) {
			File f = File.createTempFile("tmp_base_template", ODT_EXTENSION);
			FileUtils.writeByteArrayToFile(f, plantilla.getDocumento());
			return (OdfTextDocument) OdfDocument.loadDocument(f);
		}
		return null;
	}

	/**
	 * Replace text.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param oldText        the old text
	 * @param newText        the new text
	 * @param nodeStr        the node str
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText, final String nodeStr) throws XPathExpressionException {
		XPath xpath = odt.getXPath();
		DTMNodeList nodeList = (DTMNodeList) xpath.evaluate(String.format("//%s[contains(text(),'%s')]", nodeStr, oldText), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.setTextContent(node.getTextContent().replace(oldText, newText));
		}
	}

	/**
	 * Replace text.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param oldText        the old text
	 * @param newText        the new text
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText) throws XPathExpressionException {
		replaceText(odt, odfFileContent, oldText, newText, "text:p");
	}

	/**
	 * Replace img.
	 *
	 * @param odt          the odt
	 * @param filePath     the file path
	 * @param imageNameOdt the image name odt
	 * @param mymeType     the myme type
	 * @throws Exception the exception
	 */
	protected static void replaceImageGeneric(final OdfTextDocument odt, final String filePath, final String imageNameOdt, final String mymeType) throws Exception {
		final File f = new File(filePath);
		final XPath xpath = odt.getXPath();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//draw:frame[@draw:name = '%s']/draw:image", imageNameOdt), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			final OdfElement node = (OdfElement) nodeList.item(i);
			// Replace href with filename
			String value = PICTURES_ODT_PATH + f.getName();
			node.setAttribute(XLINK_HREF, value);
			// Replace parent (draw:frame) name with filename
			((OdfElement) node.getParentNode()).setAttribute("draw:name", f.getName());
			insertFileInsideODTFile(odt, value, f, mymeType);
		}
	}

	/**
	 * Inserts a new file into the odt file package.
	 *
	 * @param odt         OdfTextDocument the ODT text document file to insert into
	 * @param odtFileName String the filename (full path) to use when inserting the new file
	 * @param newFile     File the file to insert
	 * @param mimeType    String the mime type of the file
	 */
	private static void insertFileInsideODTFile(final OdfTextDocument odt, final String odtFileName, final File newFile, final String mimeType) {
		if (newFile.exists()) {
			try (FileInputStream fin = new FileInputStream(newFile)) {
				odt.getPackage().insert(fin, odtFileName, mimeType);
			} catch (Exception e) {
				Logger.putLog("Error al intentar reemplazar una imagen en el documento OpenOffice: " + e.getMessage(), ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR);
			}
		}
	}

	/**
	 * Append node at marker position.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param newNode        the new node
	 * @param markername     the markername
	 * @throws Exception the exception
	 */
	private static void appendNodeAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, Node newNode, String markername) throws Exception {
		XPath xpath = odt.getXPath();
		// Appends all elements
		NodeList nodeList = (NodeList) xpath.evaluate(String.format(TEXT_BOOKMARK_START_TEXT_NAME_S, markername), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(newNode), node.getParentNode());
		}
	}

	/**
	 * Generate table row complexity.
	 *
	 * @param tableName        the table name
	 * @param header0          the header 0
	 * @param columna1         the columna 1
	 * @param columna2         the columna 2
	 * @param columna3         the columna 3
	 * @param complexities     the complexities
	 * @param res              the res
	 * @param messageResources the message resources
	 * @param resultsType      the results type
	 * @param isPercentaje     the is percentaje
	 * @return the string builder
	 * @throws Exception the exception
	 */
	private static StringBuilder generateTableRowComplexity(final String tableName, final String header0, String columna1, String columna2, String columna3, final List<ComplejidadForm> complexities,
			final Map<ComplejidadForm, Map<String, BigDecimal>> res, final MessageResources messageResources, final String resultsType, final boolean isPercentaje) throws Exception {
		StringBuilder sb = generateTableHeader(columna1, columna2, columna3, header0, tableName);
		for (ComplejidadForm complex : complexities) {
			List<LabelValueBean> results = null;
			switch (resultsType) {
			case TYPE_COMPLIANCE:
				results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonCompilancePuntuaction(messageResources, res.get(complex));
				break;
			case TYPE_ALLOCATION:
				results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisionAllocation(messageResources, res.get(complex));
				break;
			case TYPE_PUNTUACTION:
				results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonAllocationPuntuation(messageResources, res.get(complex));
				break;
			}
			generateGroupRow(sb, complex.getName(), results, isPercentaje);
		}
		sb.append("</table:table>");
		return sb;
	}

	/**
	 * Generate table row ambit.
	 *
	 * @param tableName        the table name
	 * @param header0          the header 0
	 * @param columna1         the columna 1
	 * @param columna2         the columna 2
	 * @param columna3         the columna 3
	 * @param ambits           the ambits
	 * @param res              the res
	 * @param messageResources the message resources
	 * @param resultsType      the results type
	 * @param isPercentaje     the is percentaje
	 * @return the string builder
	 * @throws Exception the exception
	 */
	private static StringBuilder generateTableRowAmbit(final String tableName, final String header0, String columna1, String columna2, String columna3, final List<AmbitoForm> ambits,
			final Map<AmbitoForm, Map<String, BigDecimal>> res, final MessageResources messageResources, final String resultsType, final boolean isPercentaje) throws Exception {
		StringBuilder sb = generateTableHeader(columna1, columna2, columna3, header0, tableName);
		ambits.sort(new Comparator<AmbitoForm>() {
			@Override
			public int compare(AmbitoForm o1, AmbitoForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (AmbitoForm ambit : ambits) {
			List<LabelValueBean> results = null;
			switch (resultsType) {
			case TYPE_COMPLIANCE:
				results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonCompilancePuntuaction(messageResources, res.get(ambit));
				break;
			case TYPE_ALLOCATION:
				results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisionAllocation(messageResources, res.get(ambit));
				break;
			case TYPE_PUNTUACTION:
				results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonAllocationPuntuation(messageResources, res.get(ambit));
				break;
			}
			generateGroupRow(sb, ambit.getName(), results, isPercentaje);
		}
		sb.append("</table:table>");
		return sb;
	}

	/**
	 * Generate table header.
	 *
	 * @param columna1  the columna 1
	 * @param columna2  the columna 2
	 * @param columna3  the columna 3
	 * @param header0   the header 0
	 * @param tableName the table name
	 * @return the string builder
	 */
	private static StringBuilder generateTableHeader(String columna1, String columna2, String columna3, final String header0, final String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table:table table:name='Table_").append(tableName).append("' table:style-name='TableGraphic'>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn3'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn4'/>");
		// Header row
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header0).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(columna1).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(columna2).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(columna3).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		return sb;
	}

	/**
	 * Generate group row.
	 *
	 * @param sb           the sb
	 * @param name         the name
	 * @param results      the results
	 * @param isPercentaje the is percentaje
	 */
	private static void generateGroupRow(StringBuilder sb, final String name, List<LabelValueBean> results, final boolean isPercentaje) {
		sb.append("<table:table-row>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(name).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue());
		if (isPercentaje)
			sb.append("%");
		sb.append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue());
		if (isPercentaje)
			sb.append("%");
		sb.append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue());
		if (isPercentaje)
			sb.append("%");
		sb.append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
	}

	/**
	 * Adds the image next.
	 *
	 * @param odt           the odt
	 * @param filePath      the file path
	 * @param imageNameOdt  the image name odt
	 * @param mymeType      the myme type
	 * @param prevImageName the prev image name
	 * @throws Exception the exception
	 */
	protected static void addImageNext(final OdfTextDocument odt, final String filePath, final String imageNameOdt, final String mymeType, final String prevImageName) throws Exception {
		final File f = new File(filePath);
		final XPath xpath = odt.getXPath();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//draw:frame[@draw:name = '%s']/draw:image", prevImageName + JPG_EXTENSION), odfFileContent, XPathConstants.NODESET);
		String newImageDOm = "<draw:frame draw:style-name='fr4' draw:name='" + imageNameOdt
				+ "' text:anchor-type='as-char' svg:y='0mm' svg:width='149.45mm' style:rel-width='scale' svg:height='120.21mm' style:rel-height='scale' draw:z-index='118'>"
				+ "<draw:image xlink:href='Pictures/" + f.getName() + "' xlink:type='simple' xlink:show='embed' xlink:actuate='onLoad'/>" + "<svg:desc> </svg:desc>" + "</draw:frame>";
		Element newIMage = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(newImageDOm.getBytes())).getDocumentElement();
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(newIMage), node.getParentNode().getNextSibling());
			insertFileInsideODTFile(odt, PICTURES_ODT_PATH + f.getName(), f, mymeType);
		}
	}

	/**
	 * Append paragraph to marker.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param marker         the marker
	 * @throws SAXException                 the SAX exception
	 * @throws IOException                  Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws Exception                    the exception
	 */
	private static void appendParagraphToMarker(final OdfTextDocument odt, final OdfFileDom odfFileContent, String marker) throws SAXException, IOException, ParserConfigurationException, Exception {
		Element p = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream("<text:p text:style-name=\"P\"/>".getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, p, marker);
	}

	/**
	 * Adds the table styles.
	 *
	 * @param odt the odt
	 * @throws Exception the exception
	 */
	private static void addTableStyles(final OdfTextDocument odt) throws Exception {
		OdfOfficeAutomaticStyles styles = odt.getContentDom().getOrCreateAutomaticStyles();
		OdfStyle tableStyle = styles.newStyle(OdfStyleFamily.Table);
		tableStyle.setAttribute("style:name", "TableGraphic");
		tableStyle.setProperty(OdfTableProperties.MarginLeft, "0mm");
		tableStyle.setProperty(OdfTableProperties.RelWidth, "100%");
		tableStyle.setProperty(OdfTableProperties.Width, "160mm");
		tableStyle.setProperty(OdfTableProperties.Align, "left");
		OdfStyle tableStyleColumn1 = styles.newStyle(OdfStyleFamily.TableColumn);
		tableStyleColumn1.setAttribute("style:name", "TableGraphicColumn1");
		tableStyleColumn1.setProperty(OdfTableColumnProperties.ColumnWidth, "76.5mm");
		tableStyleColumn1.setProperty(OdfTableColumnProperties.RelColumnWidth, "4367");
		OdfStyle tableStyleColumn2 = styles.newStyle(OdfStyleFamily.TableColumn);
		tableStyleColumn2.setAttribute("style:name", "TableGraphicColumn2");
		tableStyleColumn2.setProperty(OdfTableColumnProperties.ColumnWidth, "83.5mm");
		tableStyleColumn2.setProperty(OdfTableColumnProperties.RelColumnWidth, "4765");
		OdfStyle tableStyleCellA1 = styles.newStyle(OdfStyleFamily.TableCell);
		tableStyleCellA1.setAttribute("style:name", "TableGraphicCellBgGreen");
		tableStyleCellA1.setProperty(OdfTableCellProperties.BackgroundColor, "#657A33");
		tableStyleCellA1.setProperty(OdfTableCellProperties.Border, "0.5pt solid #000000");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingBottom, "0mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingLeft, "1mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingRight, "0mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.PaddingTop, "0mm");
		tableStyleCellA1.setProperty(OdfTableCellProperties.VerticalAlign, "middle");
		OdfStyle tableStyleCellA2 = styles.newStyle(OdfStyleFamily.TableCell);
		tableStyleCellA2.setAttribute("style:name", "TableGraphicCellBgWhite");
		tableStyleCellA2.setProperty(OdfTableCellProperties.BackgroundColor, "#ffffff");
		tableStyleCellA2.setProperty(OdfTableCellProperties.Border, "0.5pt solid #000000");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingBottom, "0mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingLeft, "1mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingRight, "0mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.PaddingTop, "0mm");
		tableStyleCellA2.setProperty(OdfTableCellProperties.VerticalAlign, "middle");
		OdfStyle graphicTableHeader = styles.newStyle(OdfStyleFamily.Paragraph);
		graphicTableHeader.setAttribute("style:name", "GraphicTableHeader");
		graphicTableHeader.setProperty(OdfTextProperties.Color, "#ffffff");
		graphicTableHeader.setProperty(OdfTextProperties.FontName, "Arial");
		graphicTableHeader.setProperty(OdfTextProperties.FontNameComplex, "Times New Roman");
		graphicTableHeader.setProperty(OdfTextProperties.FontSizeAsian, "11pt");
		graphicTableHeader.setProperty(OdfTextProperties.FontSizeComplex, "10pt");
		graphicTableHeader.setProperty(OdfTextProperties.FontWeight, "bold");
		graphicTableHeader.setProperty(OdfTextProperties.FontWeightComplex, "bold");
		graphicTableHeader.setProperty(OdfParagraphProperties.TextAlign, "center");
		graphicTableHeader.setProperty(OdfParagraphProperties.JustifySingleWord, "false");
		OdfStyle graphicTableCenter = styles.newStyle(OdfStyleFamily.Paragraph);
		graphicTableCenter.setAttribute("style:name", "GraphicTableCenter");
		graphicTableCenter.setProperty(OdfTextProperties.Color, "#000000");
		graphicTableCenter.setProperty(OdfTextProperties.FontName, "Arial");
		graphicTableCenter.setProperty(OdfTextProperties.FontNameComplex, "Times New Roman");
		graphicTableCenter.setProperty(OdfTextProperties.FontSizeAsian, "11pt");
		graphicTableCenter.setProperty(OdfTextProperties.FontSizeComplex, "10pt");
		graphicTableCenter.setProperty(OdfTextProperties.FontWeight, "normal");
		graphicTableCenter.setProperty(OdfTextProperties.FontWeightComplex, "normal");
		graphicTableCenter.setProperty(OdfParagraphProperties.TextAlign, "center");
		graphicTableCenter.setProperty(OdfParagraphProperties.JustifySingleWord, "false");
	}

	/**
	 * Finish document configuration.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param reportTitle    the report title
	 * @throws Exception the exception
	 */
	private static void finishDocumentConfiguration(final OdfTextDocument odt, final OdfFileDom odfFileContent, String reportTitle) throws Exception {
		// Fix crop images (fo:clip attribute) after merge several documents
		NodeList nodeList = odt.getContentDom().getElementsByTagName(STYLE_GRAPHIC_PROPERTIES);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.removeAttribute(FO_CLIP);
		}
		// Update title
		replaceDocumentTitle(odt, odfFileContent, reportTitle);
		// Add generated tables styles
		addTableStyles(odt);
		addHeaderStyles(odt);
		addTableHeaderStyles(odt);
		// Remove all marks (visual)
		removeElement(odt, odfFileContent, TABLASCUMPLIMIENTOCOMPLEJIDAD_BOOKMARK);
		removeElement(odt, odfFileContent, TABLASCOMPLEJIDAD_BOOKMARK);
		removeElement(odt, odfFileContent, TABLAS_ADECUACION_BOOKMARK);
		removeElement(odt, odfFileContent, TABLAS_ADECUACION_PRINCIPALES_BOOKMARK);
		removeElement(odt, odfFileContent, TABLAS_CUMPLIMIENTO_BOOKMARK);
		removeElement(odt, odfFileContent, TABLAS_CUMPLIMIENTO_PRINCIPALES_BOOKMARK);
	}

	/**
	 * Replace document title in metadata and in text content.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param newTitle       the new title
	 * @throws Exception the exception
	 */
	private static void replaceDocumentTitle(final OdfTextDocument odt, final OdfFileDom odfFileContent, String newTitle) throws Exception {
		if (!org.apache.commons.lang3.StringUtils.isEmpty(newTitle)) {
			XPath xpath = odt.getXPath();
			NodeList nodeList = (NodeList) xpath.evaluate(TEXT_TITLE, odfFileContent, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				OdfElement node = (OdfElement) nodeList.item(i);
				node.setTextContent(newTitle);
			}
			odt.getOfficeMetadata().setTitle(newTitle);
		}
	}

	/**
	 * Adds the header styles.
	 *
	 * @param odt the odt
	 * @throws Exception the exception
	 */
	private static void addHeaderStyles(final OdfTextDocument odt) throws Exception {
		OdfOfficeAutomaticStyles styles = odt.getContentDom().getOrCreateAutomaticStyles();
		OdfStyle paragraphStyleH2 = styles.newStyle(OdfStyleFamily.Paragraph);
		paragraphStyleH2.setAttribute("style:name", "HeaderStyle2");
		paragraphStyleH2.setAttribute("style:parent-style-name", "H2");
		paragraphStyleH2.setAttribute("style:list-style-name", STYLE_LFO3);
		OdfStyle paragraphStyleH3 = styles.newStyle(OdfStyleFamily.Paragraph);
		paragraphStyleH3.setAttribute("style:name", "HeaderStyle3");
		paragraphStyleH3.setAttribute("style:parent-style-name", "H3");
		paragraphStyleH3.setAttribute("style:list-style-name", STYLE_LFO3);
	}

	/**
	 * Adds the table header styles.
	 *
	 * @param odt the odt
	 * @throws Exception the exception
	 */
	private static void addTableHeaderStyles(final OdfTextDocument odt) throws Exception {
		OdfOfficeAutomaticStyles styles = odt.getContentDom().getOrCreateAutomaticStyles();
		OdfStyle paragraphStyleH2 = styles.newStyle(OdfStyleFamily.Paragraph);
		paragraphStyleH2.setAttribute("style:name", "PTableTitle");
		paragraphStyleH2.setAttribute("style:parent-style-name", "Titulo_tablas");
		paragraphStyleH2.setProperty(OdfParagraphProperties.MarginTop, "4.23mm");
		paragraphStyleH2.setProperty(OdfParagraphProperties.MarginBottom, "0mm");
		paragraphStyleH2.setProperty(OdfParagraphProperties.LineHeight, "130%");
		paragraphStyleH2.setProperty(OdfParagraphProperties.TextAlign, "center");
		paragraphStyleH2.setProperty(OdfTextProperties.Color, "#000000");
		paragraphStyleH2.setProperty(OdfTextProperties.FontSize, "11pt");
		paragraphStyleH2.setProperty(OdfTextProperties.FontWeight, "bold");
		paragraphStyleH2.setProperty(OdfTextProperties.FontSize, "11pt");
		paragraphStyleH2.setProperty(OdfTextProperties.FontSizeAsian, "10pt");
		paragraphStyleH2.setProperty(OdfTextProperties.FontSizeComplex, "10pt");
		paragraphStyleH2.setProperty(OdfTextProperties.FontWeightComplex, "bold");
	}

	/**
	 * Removes the attribute from file.
	 *
	 * @param doc       the doc
	 * @param xmlFile   the xml file
	 * @param node      the node
	 * @param attribute the attribute
	 * @param mymeType  the myme type
	 * @throws Exception the exception
	 */
	private static void removeAttributeFromFile(final String doc, final String xmlFile, final String node, final String attribute, final String mymeType) throws Exception {
		final OdfPackage odfPackageNew = OdfPackage.loadPackage(doc);
		final Document packageDocument = odfPackageNew.getDom(xmlFile);
		final NodeList nodeList = packageDocument.getElementsByTagName(node);
		for (int i = 0; i < nodeList.getLength(); i++) {
			((Element) nodeList.item(i)).removeAttribute(attribute);
		}
		odfPackageNew.insert(packageDocument, xmlFile, mymeType);
		odfPackageNew.save(doc);
		odfPackageNew.close();
	}

	/**
	 * Removes the section in an document.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param sectionName    the section name
	 * @throws XPathExpressionException the x path expression exception
	 */
	private static void removeElement(final OdfTextDocument odt, final OdfFileDom odfFileContent, String sectionName) throws XPathExpressionException {
		XPath xpath = odt.getXPath();
		NodeList nodeList = (NodeList) xpath.evaluate(String.format(TEXT_SECTION_NAME_S, sectionName), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().removeChild(node);
		}
	}
}
