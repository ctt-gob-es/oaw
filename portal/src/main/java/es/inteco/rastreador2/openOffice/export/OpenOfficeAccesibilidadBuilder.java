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
package es.inteco.rastreador2.openOffice.export;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.doc.style.OdfStyle;
import org.odftoolkit.odfdom.dom.element.OdfStyleBase;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfParagraphProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableCellProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableColumnProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTextProperties;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.PlantillaForm;
import es.inteco.rastreador2.dao.ambito.AmbitoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.plantilla.PlantillaDAO;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioAccesibilidadUtils;

/**
 * Clase encargada de construir el documento OpenOffice con los resultados del observatorio usando la metodología UNE 2012 - VERSIÓN 2017.
 */
public class OpenOfficeAccesibilidadBuilder extends OpenOfficeDocumentBuilder {
	/** The Constant TEXT_TITLE. */
	private static final String TEXT_TITLE = "//text:title";
	/** The Constant JPG_EXTENSION. */
	private static final String JPG_EXTENSION = ".jpg";
	/** The Constant IMAGE_JPEG. */
	private static final String IMAGE_JPEG = "image/jpeg";
	/** The Constant EMPTY_STRING. */
	private static final String EMPTY_STRING = "";
	/** The Constant PICTURES_ODT_PATH. */
	private static final String PICTURES_ODT_PATH = "Pictures/";
	/** The Constant XLINK_HREF. */
	private static final String XLINK_HREF = "xlink:href";
	/** The Constant TEXT_BOOKMARK_START_TEXT_NAME_S. */
	private static final String TEXT_BOOKMARK_START_TEXT_NAME_S = "//text:bookmark-start[@text:name='%s']";
	/** The Constant OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME. */
	private static final String OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME = "observatory.graphic.modality.by.verification.level.1.name";
	/** The Constant EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA. */
	private static final String EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA = "EvolucionNivelCumplimientoCombinada";
	/** The Constant EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA_AMBITO. */
	private static final String EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA_AMBITO = "EvolucionNivelCumplimientoCombinadaAmbito";
	/** The Constant MIME_TYPE_JPG. */
	private static final String MIME_TYPE_JPG = "image/jpg";
	/** The Constant GLOBAL_RESULTS_PREFIX. */
	private static final String GLOBAL_RESULTS_PREFIX = "eg";
	/** The Constant EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS. */
	private static final String EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS = "export.open.office.graphic.noResults";
	/** The Constant TEXT_H_NODE. */
	private static final String TEXT_H_NODE = "text:h";
	/** The Constant TEXT_P_NODE. */
	private static final String TEXT_P_NODE = "text:p";
	/** The Constant TEXT_SPAN_NODE. */
	private static final String TEXT_SPAN_NODE = "text:span";
	/** The Constant REGEX_SPACES_1_MORE. */
	private static final String REGEX_SPACES_1_MORE = "\\s+";
	/** The Constant EVOLAMBITSECTION_BOOKMARK. */
	private static final String EVOLAMBITSECTION_BOOKMARK = "ambitsection";
	/** The Constant DRAW_IMAGE. */
	private static final String DRAW_IMAGE = "//draw:image";
	/** The Constant TEXT_STYLE_NAME. */
	private static final String TEXT_STYLE_NAME = "text:style-name";
	/** The Constant DOT. */
	private static final String DOT = ".";
	/** The Constant SLASH. */
	private static final String SLASH = "/";
	/** The Constant LEVEL_I_VERIFICATIONS. */
	private static final List<String> LEVEL_I_VERIFICATIONS = Arrays.asList(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_3_1_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_3_2_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_3_3_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_3_4_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_3_5_VERIFICATION);

	/**
	 * Instantiates a new open office UNE 2012b document builder.
	 *
	 * @param executionId      the execution id
	 * @param observatoryId    the observatory id
	 * @param tipoObservatorio the tipo observatorio
	 */
	public OpenOfficeAccesibilidadBuilder(final String executionId, final String observatoryId, final Long tipoObservatorio) {
		super(executionId, observatoryId, tipoObservatorio);
		numImg = 8;
		numSection = 5;
	}

	/**
	 * Builds the document.
	 *
	 * @param request           the request
	 * @param graphicPath       the graphic path
	 * @param date              the date
	 * @param evolution         the evolution
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @return the odf text document
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.openOffice.export.OpenOfficeDocumentBuilder# buildDocument(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, boolean, java.util.List,
	 * java.util.List)
	 */
	public OdfTextDocument buildDocument(final HttpServletRequest request, final String graphicPath, final String date, final boolean evolution,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories) throws Exception {
		final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
		ResultadosAnonimosObservatorioAccesibilidadUtils.generateGraphics(messageResources, executionId, Long.parseLong(request.getParameter(Constants.ID)), observatoryId, graphicPath,
				Constants.MINISTERIO_P, true, null, null);
		final List<AmbitoForm> ambits = AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1);
		final OdfTextDocument odt = getOdfTemplate();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final OdfFileDom odfStyles = odt.getStylesDom();
		replaceText(odt, odfFileContent, "[fecha]", date);
		replaceText(odt, odfStyles, "[fecha]", date, "text:span");
		replaceSectionGlobalCompilanceDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionGlobalComplianceByAmbit(messageResources, odt, odfFileContent, graphicPath, ambits, pageExecutionList, executionId, null);
		replaceSectionModalityByVerificationLevel1(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		return odt;
	}

	/**
	 * Replace section evolution suitability level.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @param prefix            the prefix
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionSuitabilityLevel(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList, String prefix) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioAccesibilidadUtils.getEvolutionObservatoriesSitesByType(observatoryId, executionId,
					pageExecutionList);
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			final Map<String, BigDecimal> resultDataFull = ResultadosAnonimosObservatorioAccesibilidadUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_ACCESIBILITY_FULL, df);
			final Map<String, BigDecimal> resultDataPartial = ResultadosAnonimosObservatorioAccesibilidadUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_ACCESIBILITY_PARTIAL,
					df);
			final Map<String, BigDecimal> resultDataNone = ResultadosAnonimosObservatorioAccesibilidadUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_ACCESIBILITY_NONE, df);
			final Map<String, BigDecimal> resultDataNA = ResultadosAnonimosObservatorioAccesibilidadUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_ACCESIBILITY_NA, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + JPG_EXTENSION, IMAGE_JPEG);
			replaceEvolutionTextCellTables(odt, odfFileContent, prefix + ".tc.1", resultDataFull, true);
			replaceEvolutionTextCellTables(odt, odfFileContent, prefix + ".tc.2", resultDataPartial, true);
			replaceEvolutionTextCellTables(odt, odfFileContent, prefix + ".tc.3", resultDataNone, true);
			replaceEvolutionTextCellTables(odt, odfFileContent, prefix + ".tc.4", resultDataNA, true);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
		}
		return numImg;
	}

	/**
	 * Replace section modality by verification level 1.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @throws Exception the exception
	 */
	private void replaceSectionModalityByVerificationLevel1(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String graphicName = messageResources.getMessage(OBSERVATORY_GRAPHIC_MODALITY_BY_VERIFICATION_LEVEL_1_NAME);
		replaceImageGeneric(odt, graphicPath + graphicName + JPG_EXTENSION, graphicName, IMAGE_JPEG);
		final Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioAccesibilidadUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
		final List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioAccesibilidadUtils.infoLevelVerificationModalityComparison(results1);
		replaceText(odt, odfFileContent, "--ag.acc.mod.p.1--", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "--ag.acc.mod.f.1--", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "--ag.acc.mod.p.2--", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "--ag.acc.mod.f.2--", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "--ag.acc.mod.p.3--", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "--ag.acc.mod.f.3--", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "--ag.acc.mod.p.4--", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "--ag.acc.mod.f.4--", res.get(3).getRedPercentage());
		try {
			replaceText(odt, odfFileContent, "--ag.acc.mod.p.5--", res.get(4).getGreenPercentage());
			replaceText(odt, odfFileContent, "--ag.acc.mod.f.5--", res.get(4).getRedPercentage());
		} catch (Exception e) {
			replaceText(odt, odfFileContent, "--ag.acc.mod.p.5--", "NA");
			replaceText(odt, odfFileContent, "--ag.acc.mod.f.5--", "NA");
		}
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
	 * @param executionId       the execution id
	 * @param tagsFilter        the tags filter
	 * @throws Exception the exception
	 */
	private static void replaceSectionGlobalComplianceByAmbit(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<AmbitoForm> ambits, final List<ObservatoryEvaluationForm> pageExecutionList, final String executionId, final String[] tagsFilter) throws Exception {
		final Map<Integer, List<AmbitoForm>> resultLists = ResultadosAnonimosObservatorioAccesibilidadUtils.createGraphicsMapAmbits(ambits);
		String prevImage = EMPTY_STRING;
		for (Integer i : resultLists.keySet()) {
			// DistribuccionNivelCumplimientoGlobal
			String grpahicName = messageResources.getMessage("observatory.graphic.global.puntuation.compilance.ambit.mark.name") + i;
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
		final Map<AmbitoForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioAccesibilidadUtils.calculatePercentageResultsByAmbitMap(executionId, pageExecutionList, ambits, tagsFilter);
		String header0 = "Ámbito";
		String columna1 = "Completa";
		String columna2 = "Parcial";
		String columna3 = "No válida";
		String columna4 = "Sin declaración";
		String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>" + messageResources.getMessage("global.report.compliance.ambit.table.title") + "</text:p>";
		Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablasacumplimientoambito");
		StringBuilder sb = generateTableRowAmbit("AllocationAmbit", header0, columna1, columna2, columna3, columna4, ambits, res, messageResources, true);
		Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablasacumplimientoambito");
		appendParagraphToMarker(odt, odfFileContent, "tablasacumplimientoambito");
		addTableStyles(odt);
	}

	/**
	 * Generate table row ambit.
	 *
	 * @param tableName        the table name
	 * @param header0          the header 0
	 * @param columna1         the columna 1
	 * @param columna2         the columna 2
	 * @param columna3         the columna 3
	 * @param columna4         the columna 4
	 * @param ambits           the ambits
	 * @param res              the res
	 * @param messageResources the message resources
	 * @param isPercentaje     the is percentaje
	 * @return the string builder
	 * @throws Exception the exception
	 */
	private static StringBuilder generateTableRowAmbit(final String tableName, final String header0, String columna1, String columna2, String columna3, String columna4, final List<AmbitoForm> ambits,
			final Map<AmbitoForm, Map<String, BigDecimal>> res, final MessageResources messageResources, final boolean isPercentaje) throws Exception {
		StringBuilder sb = generateTableHeader(columna1, columna2, columna3, columna4, header0, tableName);
		for (AmbitoForm ambit : ambits) {
			List<LabelValueBean> results = null;
			results = ResultadosAnonimosObservatorioAccesibilidadUtils.infoComparisonCompilancePuntuaction(messageResources, res.get(ambit));
			generateGroupRow(sb, ambit.getName(), results, isPercentaje);
		}
		sb.append("</table:table>");
		return sb;
	}

	/**
	 * Replace section global compilance distribution.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionGlobalCompilanceDistribution(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String grpahicName = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.name");
		replaceImageGeneric(odt, graphicPath + grpahicName + JPG_EXTENSION, grpahicName, IMAGE_JPEG);
		numImg++;
		Map<String, Integer> result = ResultadosAnonimosObservatorioAccesibilidadUtils.getResultsBySiteLevel(pageExecutionList);
		List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioAccesibilidadUtils.infoGlobalAccessibilityLevel(messageResources, result);
		replaceText(odt, odfFileContent, "--ag.acc.cp.per.1--", labelValueBean.get(0).getPercentageP());
		replaceText(odt, odfFileContent, "--ag.acc.cp.per.2--", labelValueBean.get(1).getPercentageP());
		replaceText(odt, odfFileContent, "--ag.acc.cp.per.3--", labelValueBean.get(2).getPercentageP());
		replaceText(odt, odfFileContent, "--ag.acc.cp.per.3--", labelValueBean.get(3).getPercentageP());
		replaceText(odt, odfFileContent, "--ag.acc.cp.num.1--", labelValueBean.get(0).getNumberP());
		replaceText(odt, odfFileContent, "--ag.acc.cp.num.2--", labelValueBean.get(1).getNumberP());
		replaceText(odt, odfFileContent, "--ag.acc.cp.num.3--", labelValueBean.get(2).getNumberP());
		replaceText(odt, odfFileContent, "--ag.acc.cp.num.4--", labelValueBean.get(3).getNumberP());
		return numImg;
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
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
		sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(3).getValue());
		if (isPercentaje)
			sb.append("%");
		sb.append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
	}

	/**
	 * Generate table header.
	 *
	 * @param columna1  the columna 1
	 * @param columna2  the columna 2
	 * @param columna3  the columna 3
	 * @param columna4  the columna 4
	 * @param header0   the header 0
	 * @param tableName the table name
	 * @return the string builder
	 */
	private static StringBuilder generateTableHeader(String columna1, String columna2, String columna3, String columna4, final String header0, final String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table:table table:name='Table_Allocation_").append("Segments").append("' table:style-name='TableGraphic'>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn3'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn4'/>");
		sb.append("<table:table-column table:style-name='TableGraphicColumn5'/>");
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
		sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
		sb.append("<text:p text:style-name='GraphicTableHeader'>").append(columna4).append("</text:p>");
		sb.append("</table:table-cell>");
		sb.append("</table:table-row>");
		return sb;
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplate() throws Exception {
		PlantillaForm plantilla = PlantillaDAO.findByName(DataBaseManager.getConnection(), "Generica_Accesibilidad");
		if (plantilla != null && plantilla.getDocumento() != null && plantilla.getDocumento().length > 0) {
			File f = File.createTempFile("tmp_base_template", ".odt");
			FileUtils.writeByteArrayToFile(f, plantilla.getDocumento());
			return (OdfTextDocument) OdfDocument.loadDocument(f);
		}
		return null;
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
	 * Append node at marker position.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param newNode        the new node
	 * @param markername     the markername
	 * @throws Exception the exception
	 */
	public static void appendNodeAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, Node newNode, String markername) throws Exception {
		XPath xpath = odt.getXPath();
		// Appends all elements
		NodeList nodeList = (NodeList) xpath.evaluate(String.format(TEXT_BOOKMARK_START_TEXT_NAME_S, markername), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(newNode), node.getParentNode());
		}
	}

	/**
	 * Gets the embeded id image.
	 *
	 * @param tipoObservatorio the tipo observatorio
	 * @param name             the name
	 * @return the embeded id image
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.inteco.rastreador2.openOffice.export.OpenOfficeDocumentBuilder# getEmbededIdImage(java.lang.Long, java.lang.String)
	 */
	@Override
	protected String getEmbededIdImage(final Long tipoObservatorio, final String name) {
		return OpenOfficeUNEEN2019ImageUtils.getEmbededIdImage(tipoObservatorio, name);
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
	 * Builds the document filtered.
	 *
	 * @param request               the request
	 * @param filePath              the file path
	 * @param graphicPath           the graphic path
	 * @param date                  the date
	 * @param evolution             the evolution
	 * @param pageExecutionList     the page execution list
	 * @param categories            the categories
	 * @param tagsToFilter          the tags to filter
	 * @param tagsToFilterFixed     the tags to filter fixed
	 * @param grpahicConditional    the grpahic conditional
	 * @param exObsIds              the ex obs ids
	 * @param idBaseTemplate        the id base template
	 * @param idSegmentTemplate     the id segment template
	 * @param idComplexityTemplate  the id complexity template
	 * @param idSegmentEvolTemplate the id segment evol template
	 * @param reportTitle           the report title
	 * @return the odf text document
	 * @throws Exception the exception
	 */
	@Override
	public OdfTextDocument buildDocumentFiltered(final HttpServletRequest request, final String filePath, final String graphicPath, final String date, final boolean evolution,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories, final String[] tagsToFilter, final String[] tagsToFilterFixed,
			final Map<String, Boolean> grpahicConditional, final String[] exObsIds, final Long idBaseTemplate, final Long idSegmentTemplate, final Long idComplexityTemplate,
			final Long idSegmentEvolTemplate, final String reportTitle) throws Exception {
		final String url = request.getRequestURL().toString();
		final String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
		final DatosForm userData = LoginDAO.getUserDataByName(DataBaseManager.getConnection(), request.getSession().getAttribute(Constants.USER).toString());
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
					ResultadosAnonimosObservatorioAccesibilidadUtils.generateGraphics(messageResources, executionId, Long.parseLong(request.getParameter(Constants.ID)), observatoryId, graphicPath,
							Constants.MINISTERIO_P, true, null, null);
					final List<AmbitoForm> ambits = AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1);
					final OdfTextDocument odt = getOdfTemplateById(idBaseTemplate);
					final OdfFileDom odfFileContent = odt.getContentDom();
					final OdfFileDom odfStyles = odt.getStylesDom();
					replaceText(odt, odfFileContent, "[fecha]", date);
					replaceText(odt, odfStyles, "[fecha]", date, "text:span");
					replaceSectionGlobalCompilanceDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
					replaceSectionGlobalComplianceByAmbit(messageResources, odt, odfFileContent, graphicPath, ambits, pageExecutionList, executionId, null);
					replaceSectionModalityByVerificationLevel1(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
					replaceDocumentTitle(odt, odfFileContent, reportTitle); // Lists all files in folder
					// Evolution section
					// Global
					final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioAccesibilidadUtils.resultEvolutionData(Long.valueOf(observatoryId),
							Long.valueOf(executionId));
					// Title
					String title = "Evolución del nivel de cumplimiento estimado: en términos generales";
					ResultadosAnonimosObservatorioAccesibilidadUtils.generateEvolutionSuitabilityChart(observatoryId, executionId, graphicPath + EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA + JPG_EXTENSION,
							pageObservatoryMap, title);
					replaceSectionEvolutionSuitabilityLevel(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap, GLOBAL_RESULTS_PREFIX);
					replaceImageGeneric(odt, graphicPath + EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA + JPG_EXTENSION, EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA, MIME_TYPE_JPG);
					// By ambit
					replaceEvolComplianceAmbitSection(graphicPath, pageExecutionList, messageResources, odt, odfFileContent);
					// By verification
					// Compliance by verification
					ResultadosAnonimosObservatorioAccesibilidadUtils.generateEvolutionComplianceByVerificationChart(messageResources,
							new String[] { graphicPath + "EvolucionCumplimientoVerificacion" + JPG_EXTENSION, }, new String[] { "Evolución de la modalidad de las verificaciones a nivel global." },
							pageObservatoryMap);
					replaceSectionComplianceByVerification(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap, "eg");
					replaceImageGeneric(odt, graphicPath + "EvolucionCumplimientoVerificacion" + JPG_EXTENSION, "EvolucionCumplimientoVerificacion", MIME_TYPE_JPG);
//										
					File folder = new File("/tmp");
					File fList[] = folder.listFiles();
					// Searchs .lck
					for (int i = 0; i < fList.length; i++) {
						File pes = fList[i];
						if (pes.getName().endsWith(".jpg") || pes.getName().endsWith(".odt")) {
							// and deletes
							pes.delete();
						}
					}
					odt.save(filePath);
					removeAttributeFromFile(filePath, "META-INF/manifest.xml", "manifest:file-entry", "manifest:size", "text/xml");
					odt.close();
					StringBuilder mailBody = new StringBuilder("El proceso de generación de informes ha finalizado. Puede descargarlo en el siguiente enlace: <br/>");
					StringBuilder linkUrl = new StringBuilder(baseURL);
					linkUrl.append("secure/exportOpenOfficeAction.do?action=downloadFile");
					linkUrl.append("&idExObs=").append(executionId);
					linkUrl.append("&id_observatorio=").append(observatoryId);
					final String filename = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
					linkUrl.append("&file=").append(filename);
					mailBody.append("<a href=\"").append(linkUrl.toString()).append("\">").append(filename).append("</a><br>");
					final MailService mailService = new MailService();
					List<String> mailsTo = new ArrayList<>();
					mailsTo.add(userData.getEmail());
//					try {
//						Connection c = DataBaseManager.getConnection();
//						List<DatosForm> adminData = LoginDAO.getAdminUsers(c);
//						DataBaseManager.closeConnection(c);
//						if (adminData != null && !adminData.isEmpty()) {
//							for (DatosForm data : adminData) {
//								mailsTo.add(data.getEmail());
//							}
//						}
//					} catch (Exception e) {
//						Logger.putLog("Error al cargar los emails de los admin", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
//					}
					mailService.sendMail(mailsTo, "Generación de informes completado", mailBody.toString(), true);
				} catch (Exception e) {
					Logger.putLog("Error", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
				}
			}
		});
		return null;
	}

	/**
	 * Replace section compliance by verification.
	 *
	 * @param messageResources   the message resources
	 * @param odt                the odt
	 * @param odfFileContent     the odf file content
	 * @param graphicPath        the graphic path
	 * @param pageObservatoryMap the page observatory map
	 * @param prefix             the prefix
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionComplianceByVerification(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, String prefix) throws Exception {
		numSection = 10;
		if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
			// Verications level I
			Map<String, Map<String, BigDecimal>> resultData = ResultadosAnonimosObservatorioAccesibilidadUtils.calculateVerificationEvolutionComplianceDataSetDetailed(LEVEL_I_VERIFICATIONS,
					pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1") + JPG_EXTENSION, IMAGE_JPEG);
			numImg++;
			replaceEvolutionComplianceModalityTextCellTables(odt, odfFileContent, prefix + ".t5.", resultData, true);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			for (int i = 5; i < 25; i++) {
				replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, EXPORT_OPEN_OFFICE_GRAPHIC_NO_RESULTS), IMAGE_JPEG);
				numImg++;
			}
		}
		return numImg;
	}

	/**
	 * Replace evolution compliance text cell tables.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param rowId          the row id
	 * @param resultData     the result data
	 * @param isPercentValue the is percent value
	 * @throws XPathExpressionException the x path expression exception
	 */
	protected void replaceEvolutionComplianceModalityTextCellTables(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String rowId,
			final Map<String, Map<String, BigDecimal>> resultData, final boolean isPercentValue) throws XPathExpressionException {
		int index = 2;
		for (Map.Entry<String, Map<String, BigDecimal>> entry : resultData.entrySet()) {
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
					return Integer.parseInt(version[0].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""));
				}

				private Integer minor(String[] version) {
					return version.length > 1 ? Integer.parseInt(version[1].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")) : 0;
				}
			});
			for (Map.Entry<String, BigDecimal> entryU : entry.getValue().entrySet()) {
				results.put(entryU.getKey(), entryU.getValue());
			}
			for (Map.Entry<String, BigDecimal> resultC : results.entrySet()) {
				// Cabecera
				String vCount = resultC.getKey().substring(resultC.getKey().lastIndexOf(".") + 1, resultC.getKey().indexOf("_"));
				replaceText(odt, odfFileContent, "-" + rowId + vCount + ".a" + index + "-", entry.getKey());
				final String oldTextC = "-" + rowId + vCount + ".b" + index + ".p-";
				final String oldTextNC = "-" + rowId + vCount + ".b" + index + ".f-";
				// final String oldTextNA = "-" + rowId + vCount + ".b" + index + ".na-";
				if (resultC.getKey().endsWith(Constants.OBS_VALUE_GREEN_SUFFIX)) {
					replaceText(odt, odfFileContent, oldTextC, getCellValue(resultC.getValue(), isPercentValue));
				} else if (resultC.getKey().endsWith(Constants.OBS_VALUE_RED_SUFFIX)) {
					replaceText(odt, odfFileContent, oldTextNC, getCellValue(resultC.getValue(), isPercentValue));
				}
			}
			index++;
		}
		// Para el resto de la tabla borramos los placeholders para que al menos las celdas salgan vacías
		while (index <= 7) {
			for (int i = 0; i <= 14; i++) {
				replaceText(odt, odfFileContent, "-" + rowId + i + ".a" + index + "-", "");
				replaceText(odt, odfFileContent, "-" + rowId + i + ".b" + index + ".p-", "");
				replaceText(odt, odfFileContent, "-" + rowId + i + ".b" + index + ".f-", "");
			}
			index++;
		}
	}

	/**
	 * Replace evol compliance ambit section.
	 *
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @throws Exception                the exception
	 * @throws XPathExpressionException the x path expression exception
	 */
	private void replaceEvolComplianceAmbitSection(final String graphicPath, final List<ObservatoryEvaluationForm> pageExecutionList, final MessageResources messageResources,
			final OdfTextDocument odt, final OdfFileDom odfFileContent) throws Exception, XPathExpressionException {
		final List<AmbitoForm> ambits = AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1);
		for (AmbitoForm ambit : ambits) {
			String graphicSuffix = "_EVOL_".concat(ambit.getName().replaceAll(REGEX_SPACES_1_MORE, EMPTY_STRING));
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMapAmbit = ResultadosAnonimosObservatorioAccesibilidadUtils.resultEvolutionAmbitData(Long.valueOf(observatoryId),
					Long.valueOf(executionId), Long.valueOf(ambit.getId()));
			OdfTextDocument odtCategory = getOdfTemplateAmbit();
			OdfFileDom odfFileContentCategory = odtCategory.getContentDom();
//			String title = messageResources.getMessage("Evolución de la situación de cumplimiento estimada: " + ambit.getName());
			String title = "Evolución del nivel de cumplimiento estimado: " + ambit.getName();
			ResultadosAnonimosObservatorioAccesibilidadUtils.generateEvolutionSuitabilityChart(observatoryId, executionId,
					graphicPath + EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA_AMBITO + "_" + ambit.getName() + JPG_EXTENSION, pageObservatoryMapAmbit, title);
			replaceSectionEvolutionSuitabilityLevel(messageResources, odtCategory, odfFileContentCategory, graphicPath, pageObservatoryMapAmbit, GLOBAL_RESULTS_PREFIX);
			replaceImageGeneric(odtCategory, graphicPath + EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA_AMBITO + "_" + ambit.getName() + JPG_EXTENSION, EVOLUCION_NIVEL_CUMPLIMIENTO_COMBINADA_AMBITO,
					MIME_TYPE_JPG);
			// Replace titles with group name
			replaceText(odtCategory, odfFileContentCategory, "--nombreambito--", ambit.getName(), TEXT_SPAN_NODE);
			replaceText(odtCategory, odfFileContentCategory, "--nombreambito--", ambit.getName(), TEXT_P_NODE);
			replaceText(odtCategory, odfFileContentCategory, "--nombreambito--", ambit.getName(), TEXT_H_NODE);
			// Rename documentStyles names to avoid conflicts
			renameStyles(odtCategory, odfFileContentCategory, graphicSuffix);
			appendsNodeAndChildsAtMarkerPosition(odt, odfFileContent, odtCategory, odfFileContentCategory, EVOLAMBITSECTION_BOOKMARK);
			mergePictures(odt, odtCategory, graphicPath);
			odtCategory.close();
			mergeStylesToPrimaryDoc(odt, odtCategory, graphicSuffix);
			mergeFontTypesToPrimaryDoc(odt, odtCategory);
			// forceContinueNumbering(odt, odfFileContent);
			odtCategory = null;
			odfFileContentCategory = null;
			System.gc();
		}
	}

	/**
	 * Merge font types to primary doc.
	 *
	 * @param primaryDoc   the primary doc
	 * @param secondaryDoc the secondary doc
	 * @throws Exception the exception
	 */
	private static void mergeFontTypesToPrimaryDoc(OdfTextDocument primaryDoc, OdfTextDocument secondaryDoc) throws Exception {
		// Insert referenced font types that are not in the primary document you are merging into
		NodeList sdDomNodes = secondaryDoc.getContentDom().getChildNodes().item(0).getChildNodes();
		NodeList pdDomNodes = primaryDoc.getContentDom().getChildNodes().item(0).getChildNodes();
		OdfFileDom primaryContentDom = primaryDoc.getContentDom();
		Node sdFontNode = null;
		Node pdFontNode = null;
		for (int i = 0; i < sdDomNodes.getLength(); i++) {
			if (sdDomNodes.item(i).getNodeName().equals("office:font-face-decls")) {
				sdFontNode = sdDomNodes.item(i);
				break;
			}
		}
		for (int i = 0; i < pdDomNodes.getLength(); i++) {
			Node n = pdDomNodes.item(i);
			if (n.getNodeName().equals("office:font-face-decls")) {
				pdFontNode = pdDomNodes.item(i);
				break;
			}
		}
		if (sdFontNode != null && pdFontNode != null) {
			NodeList sdFontNodeChildList = sdFontNode.getChildNodes();
			NodeList pdFontNodeChildList = pdFontNode.getChildNodes();
			List<String> fontNames = new ArrayList<String>();
			// Get list of existing fonts in primary doc
			for (int i = 0; i < pdFontNodeChildList.getLength(); i++) {
				NamedNodeMap attributes = pdFontNodeChildList.item(i).getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					if (attributes.item(j).getLocalName().equals("name")) {
						fontNames.add(attributes.item(j).getNodeValue());
					}
				}
			}
			// Check each font in the secondary doc to make sure it gets added if the primary doesn't have it
			for (int i = 0; i < sdFontNodeChildList.getLength(); i++) {
				Node fontNode = sdFontNodeChildList.item(i).cloneNode(true);
				NamedNodeMap attributes = fontNode.getAttributes();
				String fontName = "";
				for (int j = 0; j < attributes.getLength(); j++) {
					if (attributes.item(j).getLocalName().equals("name")) {
						fontName = attributes.item(j).getNodeValue();
						break;
					}
				}
				if (!fontName.equals("") && !fontNames.contains(fontName)) {
					pdFontNode.appendChild(primaryContentDom.adoptNode(fontNode));
				}
			}
		}
	}

	/**
	 * Merge styles to primary doc.
	 *
	 * @param primaryDoc   the primary doc
	 * @param secondaryDoc the secondary doc
	 * @param suffix       the suffix
	 * @throws Exception the exception
	 */
	private static void mergeStylesToPrimaryDoc(OdfTextDocument primaryDoc, OdfTextDocument secondaryDoc, String suffix) throws Exception {
		OdfFileDom primaryContentDom = primaryDoc.getContentDom();
		OdfOfficeAutomaticStyles primaryDocAutomaticStyles = primaryDoc.getContentDom().getAutomaticStyles();
		OdfOfficeAutomaticStyles secondaryDocAutomaticStyles = secondaryDoc.getContentDom().getAutomaticStyles();
		// Adopt style nodes from secondary doc
		for (int i = 0; i < secondaryDocAutomaticStyles.getLength(); i++) {
			Node style = secondaryDocAutomaticStyles.item(i).cloneNode(true);
			primaryDocAutomaticStyles.appendChild(primaryContentDom.adoptNode(style));
		}
	}

	/**
	 * Merge pictures.
	 *
	 * @param odt         the odt
	 * @param odtSource   the odt source
	 * @param graphicPath the graphic path
	 * @throws Exception the exception
	 */
	protected void mergePictures(final OdfTextDocument odt, final OdfTextDocument odtSource, String graphicPath) throws Exception {
		final XPath xpath = odtSource.getXPath();
		final OdfFileDom odfFileContent = odtSource.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(DRAW_IMAGE, odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			String attribute = node.getAttribute(XLINK_HREF);
			if (!StringUtils.isEmpty(attribute) && attribute.startsWith(PICTURES_ODT_PATH)) {
				byte[] b = odtSource.getPackage().getBytes(attribute);
				String tmpName = attribute.substring(attribute.lastIndexOf(SLASH) + 1);
				File createTempFile = File.createTempFile(tmpName.substring(0, tmpName.lastIndexOf(DOT)), JPG_EXTENSION);
				try (FileOutputStream fos = new FileOutputStream(createTempFile)) {
					fos.write(b);
					insertFileInsideODTFile(odt, attribute, createTempFile, MIME_TYPE_JPG);
				}
			}
		}
	}

	/**
	 * Append at marker position.
	 *
	 * @param odt                    the odt
	 * @param odfFileContent         the odf file content
	 * @param odtCategory            the odt category
	 * @param odfFileContentCategory the odf file content category
	 * @param markername             the markername
	 * @throws Exception the exception
	 */
	@SuppressWarnings("all")
	private void appendsNodeAndChildsAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, OdfTextDocument odtCategory, OdfFileDom odfFileContentCategory, String markername)
			throws Exception {
		XPath xpath = odt.getXPath();
		XPath xpath2 = odtCategory.getXPath();
		// Appends all elements
		NodeList nodeList = (NodeList) xpath.evaluate(String.format(TEXT_BOOKMARK_START_TEXT_NAME_S, markername), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			NodeList nodeList2 = odtCategory.getOfficeBody().getFirstChild().getChildNodes();
			for (int o = 0; o < nodeList2.getLength(); o++) {
				Node cloneNode = nodeList2.item(o).cloneNode(true);
				node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(cloneNode), node.getParentNode());
			}
		}
	}

	/**
	 * Rename styles.
	 *
	 * @param doc            the doc
	 * @param odfFileContent the odf file content
	 * @param suffix         the suffix
	 * @throws Exception the exception
	 */
	private static void renameStyles(final OdfTextDocument doc, final OdfFileDom odfFileContent, String suffix) throws Exception {
		OdfOfficeAutomaticStyles docStyles = odfFileContent.getAutomaticStyles();
		// Adopt style nodes from secondary doc
		for (int i = 0; i < docStyles.getLength(); i++) {
			OdfStyleBase style = (OdfStyleBase) docStyles.item(i);
			if (style.hasAttributes() && "paragraph".equals(style.getAttribute("style:family")) && ("h1".equalsIgnoreCase(style.getAttribute("style:parent-style-name"))
					|| "h2".equalsIgnoreCase(style.getAttribute("style:parent-style-name")) || "h3".equalsIgnoreCase(style.getAttribute("style:parent-style-name")))) {
				String prevousName = style.getAttribute("style:name");
				String newName = prevousName + suffix;
				style.setAttribute("style:name", newName);
				final XPath xpath = doc.getXPath();
				final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//text:h[@text:style-name = '%s']", prevousName), odfFileContent, XPathConstants.NODESET);
				// Make the change on the selected nodes
				for (int idx = 0; idx < nodeList.getLength(); idx++) {
					OdfElement node = (OdfElement) nodeList.item(idx);
					node.setAttribute(TEXT_STYLE_NAME, newName);
				}
			}
		}
	}

	/**
	 * Replace document title in metadata and in text content.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param newTitle       the new title
	 * @throws Exception the exception
	 */
	private void replaceDocumentTitle(final OdfTextDocument odt, final OdfFileDom odfFileContent, String newTitle) throws Exception {
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
	 * Gets the odf template.
	 *
	 * @param idBaseTemplate the id base template
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplateById(final Long idBaseTemplate) throws Exception {
		PlantillaForm plantilla = PlantillaDAO.findById(DataBaseManager.getConnection(), idBaseTemplate);
		if (plantilla != null && plantilla.getDocumento() != null && plantilla.getDocumento().length > 0) {
			File f = File.createTempFile("tmp_base_template", ".odt");
			FileUtils.writeByteArrayToFile(f, plantilla.getDocumento());
			return (OdfTextDocument) OdfDocument.loadDocument(f);
		}
		return null;
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplateAmbit() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.common.accesibility.evol.ambit"));
	}
}
