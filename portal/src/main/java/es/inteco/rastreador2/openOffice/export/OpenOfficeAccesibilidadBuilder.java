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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.apache.commons.io.FileUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
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

import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
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
		tableStyleCellA1.setProperty(OdfTableCellProperties.BackgroundColor, "#7e9a40");
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
							Constants.MINISTERIO_P, true, tagsToFilter, tagsToFilterFixed);
					final List<AmbitoForm> ambits = AmbitoDAO.getAmbitos(DataBaseManager.getConnection(), null, -1);
					final OdfTextDocument odt = getOdfTemplateById(idBaseTemplate);
					final OdfFileDom odfFileContent = odt.getContentDom();
					final OdfFileDom odfStyles = odt.getStylesDom();
					replaceText(odt, odfFileContent, "[fecha]", date);
					replaceText(odt, odfStyles, "[fecha]", date, "text:span");
					replaceSectionGlobalCompilanceDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
					replaceSectionGlobalComplianceByAmbit(messageResources, odt, odfFileContent, graphicPath, ambits, pageExecutionList, executionId, tagsToFilter);
					replaceSectionModalityByVerificationLevel1(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
					replaceDocumentTitle(odt, odfFileContent, reportTitle); // Lists all files in folder
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
					mailsTo.add("alvaro.pelaez@ctic.es");
					mailService.sendMail(mailsTo, "Generación de informes completado", mailBody.toString(), true);
				} catch (Exception e) {
				}
			}
		});
		return null;
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
}
