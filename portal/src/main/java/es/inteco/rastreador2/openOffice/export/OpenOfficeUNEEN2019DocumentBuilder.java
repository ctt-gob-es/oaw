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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.doc.style.OdfStyle;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfTableCellProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableColumnProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTableProperties;
import org.odftoolkit.odfdom.dom.style.props.OdfTextProperties;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;

/**
 * Clase encargada de construir el documento OpenOffice con los resultados del observatorio usando la metodología UNE 2012 - VERSIÓN 2017.
 */
public class OpenOfficeUNEEN2019DocumentBuilder extends OpenOfficeDocumentBuilder {
	// Reorganización de las verificaciones
	/** The Constant LEVEL_I_VERIFICATIONS. */
	public static final List<String> LEVEL_I_VERIFICATIONS = Arrays.asList(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION);
	/** The Constant LEVEL_II_VERIFICATIONS. */
	public static final List<String> LEVEL_II_VERIFICATIONS = Arrays.asList(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION);

	/**
	 * Instantiates a new open office UNE 2012b document builder.
	 *
	 * @param executionId      the execution id
	 * @param observatoryId    the observatory id
	 * @param tipoObservatorio the tipo observatorio
	 */
	public OpenOfficeUNEEN2019DocumentBuilder(final String executionId, final String observatoryId, final Long tipoObservatorio) {
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
		final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
		ResultadosAnonimosObservatorioUNEEN2019Utils.generateGraphics(messageResources, executionId, Long.parseLong(request.getParameter(Constants.ID)), observatoryId, graphicPath,
				Constants.MINISTERIO_P, true);
		final OdfTextDocument odt = getOdfTemplate();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final OdfFileDom odfStyles = odt.getStylesDom();
		List<ComplejidadForm> complexitivities = ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1);
		replaceText(odt, odfFileContent, "[fecha]", date);
		replaceText(odt, odfStyles, "[fecha]", date, "text:span");
		// Global sections
		replaceSectionGlobalAccesibilityDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionGlobalCompilanceDistribution(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSectionComparisionPuntuactionAllocationSegment(messageResources, odt, odfFileContent, graphicPath, categories, pageExecutionList);
		replaceSectionComparisionPuntuactionAllocationComplexity(messageResources, odt, odfFileContent, graphicPath, complexitivities, pageExecutionList);
		replaceSectionComparisionPuntuactionCompilanceSegment(messageResources, odt, odfFileContent, graphicPath, categories, pageExecutionList);
		replaceSectionComparisionPuntuactionCompilanceComplexitivy(messageResources, odt, odfFileContent, graphicPath, complexitivities, pageExecutionList);
		/**
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		replaceSection43(messageResources, odt, odfFileContent, graphicPath, categories, pageExecutionList);
		replaceSection441(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSection442(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSection451(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSection452(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		replaceSection46(messageResources, odt, odfFileContent, graphicPath, pageExecutionList);
		// Generate categories document and merge with parent
		for (CategoriaForm category : categories) {
			OdfTextDocument odtCategory = getOdfTemplateCategories();
			OdfFileDom odfFileContentCategory = odtCategory.getContentDom();
			// TODO Modificar para que coja la plantilla de segmentos, haga los reemplazos e introduzca en el documento padre
			final List<ObservatoryEvaluationForm> pageExecutionListCat = ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalResultData(executionId, Long.parseLong(category.getId()),
					pageExecutionList, false);
			String graphicSuffix = "_".concat(category.getName().replaceAll("\\s+", ""));
			replaceSectionCat1(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
			replaceSectionCat2(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
			replaceSectionCat31(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
			replaceSectionCat32(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
			replaceSectionCat41(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
			replaceSectionCat42(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
			replaceSectionCat5(messageResources, odtCategory, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
			// TODO Delete
			replaceText(odtCategory, odfFileContentCategory, "--nombresegmento--", category.getName(), "text:span");
			replaceText(odtCategory, odfFileContentCategory, "--nombresegmento--", category.getName(), "text:p");
			replaceText(odtCategory, odfFileContentCategory, "--nombresegmento--", category.getName(), "text:h");
			// Add all DOM from create document to base doc
			String path = "/tmp/segmento_" + graphicSuffix + "_" + new Date().getTime() + ".odt";
			odtCategory.save(path);
			appendAtMarkerPosition(odt, odfFileContent, odtCategory, odfFileContentCategory, "categorysection");
			// mergeStylesToPrimaryDoc(odt, odtCategory);
			// mergeFontTypesToPrimaryDoc(odt, odtCategory);
			mergePictures(odt, odtCategory, graphicPath);
			odtCategory.close();
		}
		// TODO Generate complexities
		for (ComplejidadForm complejidad : ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1)) {
			OdfTextDocument odtComplexity = getOdfTemplateComplexities();
			OdfFileDom odfFileContentCategory = odtComplexity.getContentDom();
			// TODO Modificar para que coja la plantilla de segmentos, haga los reemplazos e introduzca en el documento padre
			final List<ObservatoryEvaluationForm> pageExecutionListCat = ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalResultData(executionId, Long.parseLong(complejidad.getId()),
					pageExecutionList, true);
			String graphicSuffix = "_".concat(complejidad.getName().replaceAll("\\s+", ""));
			if (pageExecutionListCat != null && !pageExecutionListCat.isEmpty()) {
				replaceSectionCat1(messageResources, odtComplexity, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCat2(messageResources, odtComplexity, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCat31(messageResources, odtComplexity, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCat32(messageResources, odtComplexity, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCat41(messageResources, odtComplexity, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCat42(messageResources, odtComplexity, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				replaceSectionCat5(messageResources, odtComplexity, odfFileContentCategory, graphicPath, graphicSuffix, pageExecutionListCat);
				// TODO Delete
				replaceText(odtComplexity, odfFileContentCategory, "--nombrecomplejidad--", complejidad.getName(), "text:span");
				replaceText(odtComplexity, odfFileContentCategory, "--nombrecomplejidad--", complejidad.getName(), "text:p");
				replaceText(odtComplexity, odfFileContentCategory, "--nombrecomplejidad--", complejidad.getName(), "text:h");
				odtComplexity.save("/tmp/complejidad_" + graphicSuffix + "_" + new Date().getTime() + ".odt");
				// Add all DOM from create document to base doc
				appendAtMarkerPosition(odt, odfFileContent, odtComplexity, odfFileContentCategory, "complexitysection");
				mergeStylesToPrimaryDoc(odt, odtComplexity);
				// mergeFontTypesToPrimaryDoc(odt, odtCategory);
				mergePictures(odt, odtComplexity, graphicPath);
			}
		}
		// TODO Fix crop images (fo:clip attribute)
		NodeList nodeList = odt.getContentDom().getElementsByTagName("style:graphic-properties");
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.removeAttribute("fo:clip");
		}
		// Evolution
		if (evolution) {
			if (tipoObservatorio == Constants.OBSERVATORY_TYPE_EELL) {
				numSection = 10;
			}
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioUNEEN2019Utils.resultEvolutionData(Long.valueOf(observatoryId),
					Long.valueOf(executionId));
			final Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<>();
			for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
				resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, entry.getValue()));
			}
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionSuitabilityChart(observatoryId, executionId, graphicPath + "EvolucionNivelConformidadCombinada.jpg", pageObservatoryMap);
			replaceSectionEvolutionSuitabilityLevel(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap);
			replaceImg(odt, graphicPath + "EvolucionNivelConformidadCombinada.jpg", "image/jpg");
			replaceSectionEvolutionAverageScore(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap);
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionAverageScoreByVerificationChartSplit(messageResources,
					new String[] { graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit1.jpg", graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit2.jpg" },
					pageObservatoryMap, LEVEL_I_VERIFICATIONS);
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionAverageScoreByVerificationChart(messageResources, graphicPath + "EvolucionPuntuacionMediaVerificacionNAIICombinada.jpg",
					pageObservatoryMap, LEVEL_II_VERIFICATIONS);
			replaceSectionEvolutionScoreByVerification(messageResources, odt, odfFileContent, graphicPath, pageObservatoryMap);
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit1.jpg", "image/jpg");
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaVerificacionNAICombinadaSplit2.jpg", "image/jpg");
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaVerificacionNAIICombinada.jpg", "image/jpg");
			ResultadosAnonimosObservatorioUNEEN2019Utils.generateEvolutionAverageScoreByAspectChart(messageResources, graphicPath + "EvolucionPuntuacionMediaAspectoCombinada.jpg", pageObservatoryMap);
			replaceSectionEvolutionScoreByAspect(messageResources, odt, odfFileContent, graphicPath, resultsByAspect);
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaAspectoCombinada.jpg", "image/jpg");
			numSection++;
			replaceImg(odt, graphicPath + "EvolucionPuntuacionMediaSegmentoCombinada.jpg", "image/jpg");
		}
		return odt;
	}

	/**
	 * Append end document.
	 *
	 * @param odt                    the odt
	 * @param odfFileContent         the odf file content
	 * @param odtCategory            the odt category
	 * @param odfFileContentCategory the odf file content category
	 * @throws Exception the exception
	 */
	private void appendEndDocument(final OdfTextDocument odt, final OdfFileDom odfFileContent, OdfTextDocument odtCategory, OdfFileDom odfFileContentCategory) throws Exception {
		XPath xpath = odt.getXPath();
		XPath xpath2 = odtCategory.getXPath();
		// Appends al elements
		DTMNodeList nodeList = (DTMNodeList) xpath.evaluate("//office:body", odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			DTMNodeList nodeList2 = (DTMNodeList) xpath2.evaluate("//office:body", odfFileContentCategory, XPathConstants.NODESET);
			for (int j = 0; j < nodeList2.getLength(); j++) {
				OdfElement node2 = (OdfElement) nodeList2.item(j);
				for (int h = 0; h < node2.getChildNodes().getLength(); h++) {
					OdfElement firstDocImportedNode = (OdfElement) odfFileContent.importNode(node2.getChildNodes().item(h), true);
					node.appendChild(firstDocImportedNode);
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
	private void appendAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, OdfTextDocument odtCategory, OdfFileDom odfFileContentCategory, String markername)
			throws Exception {
		XPath xpath = odt.getXPath();
		XPath xpath2 = odtCategory.getXPath();
		// Appends all elements
		NodeList nodeList = (NodeList) xpath.evaluate(String.format("//text:bookmark-start[@text:name='%s']", markername), odfFileContent, XPathConstants.NODESET);
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
	 * Append node at marker position.
	 *
	 * @param odt            the odt
	 * @param odfFileContent the odf file content
	 * @param newNode        the new node
	 * @param markername     the markername
	 * @throws Exception the exception
	 */
	private void appendNodeAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, Node newNode, String markername) throws Exception {
		XPath xpath = odt.getXPath();
		// Appends all elements
		NodeList nodeList = (NodeList) xpath.evaluate(String.format("//text:bookmark-start[@text:name='%s']", markername), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(newNode), node.getParentNode());
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
		final NodeList nodeList = (NodeList) xpath.evaluate("//draw:image", odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			String attribute = node.getAttribute("xlink:href");
			if (!StringUtils.isEmpty(attribute) && attribute.startsWith("Pictures/")) {
				byte[] b = odtSource.getPackage().getBytes(attribute);
				String tmpName = attribute.substring(attribute.lastIndexOf("/") + 1);
				// String filePath = graphicPath + "merge/merge_" + tmpName;
				File createTempFile = File.createTempFile(tmpName.substring(0, tmpName.lastIndexOf(".")), ".jpg");
				try (FileOutputStream fos = new FileOutputStream(createTempFile)) {
					fos.write(b);
					insertFileInsideODTFile(odt, attribute, createTempFile, "image/jpg");
				}
			}
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
	private void insertFileInsideODTFile(final OdfTextDocument odt, final String odtFileName, final File newFile, final String mimeType) {
		if (newFile.exists()) {
			try (FileInputStream fin = new FileInputStream(newFile)) {
				odt.getPackage().insert(fin, odtFileName, mimeType);
			} catch (Exception e) {
				Logger.putLog("Error al intentar reemplazar una imagen en el documento OpenOffice: " + e.getMessage(), ExportOpenOfficeAction.class, Logger.LOG_LEVEL_ERROR);
			}
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
		return OpenOfficeUNE2012BImageUtils.getEmbededIdImage(tipoObservatorio, name);
	}

	/**
	 * Replace section 41.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionGlobalAccesibilityDistribution(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		String grpahicName = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.name");
		replaceImageGeneric(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg");
		numImg++;
		Map<String, Integer> result = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(pageExecutionList);
		List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalAccessibilityLevel(messageResources, result);
		replaceText(odt, odfFileContent, "-41.t1.b3-", labelValueBean.get(1).getPercentageP());
		replaceText(odt, odfFileContent, "-41.t1.b2-", labelValueBean.get(0).getPercentageP());
		replaceText(odt, odfFileContent, "-41.t1.b4-", labelValueBean.get(2).getPercentageP());
		replaceText(odt, odfFileContent, "-41.t1.c2-", labelValueBean.get(0).getNumberP());
		replaceText(odt, odfFileContent, "-41.t1.c3-", labelValueBean.get(1).getNumberP());
		replaceText(odt, odfFileContent, "-41.t1.c4-", labelValueBean.get(2).getNumberP());
		return numImg;
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
		String grpahicName = messageResources.getMessage("observatory.graphic.compilance.level.allocation.name");
		replaceImageGeneric(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg");
		numImg++;
		Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		final Map<String, Integer> resultCompilance = new TreeMap<>();
		// Process results
		int totalC = 0;
		int totalPC = 0;
		int totalNC = 0;
		for (Map.Entry<Long, Map<String, BigDecimal>> result : results.entrySet()) {
			int countC = 0;
			int countNC = 0;
			for (Map.Entry<String, BigDecimal> verificationResult : result.getValue().entrySet()) {
				if (verificationResult.getValue().compareTo(new BigDecimal(9)) >= 0) {
					countC++;
				} else {
					countNC++;
				}
			}
			if (countC == result.getValue().size()) {
				totalC++;
			} else if (countC > countNC) {
				totalPC++;
			} else {
				totalNC++;
			}
		}
		resultCompilance.put(Constants.OBS_COMPILANCE_NONE, totalNC);
		resultCompilance.put(Constants.OBS_COMPILANCE_PARTIAL, totalPC);
		resultCompilance.put(Constants.OBS_COMPILANCE_FULL, totalC);
		List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalCompilanceLevel(messageResources, resultCompilance);
		replaceText(odt, odfFileContent, "-4c1.t1.b3-", labelValueBean.get(1).getPercentageP());
		replaceText(odt, odfFileContent, "-4c1.t1.b2-", labelValueBean.get(0).getPercentageP());
		replaceText(odt, odfFileContent, "-4c1.t1.b4-", labelValueBean.get(2).getPercentageP());
		replaceText(odt, odfFileContent, "-4c1.t1.c2-", labelValueBean.get(0).getNumberP());
		replaceText(odt, odfFileContent, "-4c1.t1.c3-", labelValueBean.get(1).getNumberP());
		replaceText(odt, odfFileContent, "-4c1.t1.c4-", labelValueBean.get(2).getNumberP());
		return numImg;
	}

	/**
	 * Replace section 42.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param categories        the categories
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionComparisionPuntuactionAllocationSegment(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<CategoriaForm> categories, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMap(categories);
		String prevImage = "";
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.name") + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg");
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg", prevImage);
			}
			numImg++;
		}
		final Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageResultsBySegmentMap(executionId, pageExecutionList, categories);
		// TODO Incluir la tabla a mano
		// add table
		String header1 = "Nivel de prioridad";
		String header2 = "Porcentaje de portales";
		String columna1 = "AA";
		String columna2 = "A";
		String columna3 = "No válido";
		for (CategoriaForm category : categories) {
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de adecuación: " + category.getName() + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablassegmento");
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonBySegment(messageResources, res.get(category));
			StringBuilder sb = new StringBuilder();
			sb.append("<table:table table:name='Table_Allocation_").append(category.getName()).append("' table:style-name='TableGraphic'>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
			// Header row
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 1
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 2
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 3
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			sb.append("</table:table>");
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablassegmento");
			appendParagraphToMarker(odt, odfFileContent, "tablassegmento");
			// TODO Estilos de las tablas
			addTableStyles(odt);
		}
		return numImg;
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
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionComparisionPuntuactionAllocationComplexity(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ComplejidadForm> complexities, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexities);
		String prevImage = "";
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage("observatory.graphic.global.puntuation.allocation.complexity.mark.name") + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg");
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg", prevImage);
			}
			numImg++;
		}
		final Map<ComplejidadForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageResultsByComplexityMap(executionId, pageExecutionList,
				ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1));
		// TODO Incluir la tabla a mano
		// add table
		String header1 = "Nivel de prioridad";
		String header2 = "Porcentaje de portales";
		String columna1 = "AA";
		String columna2 = "A";
		String columna3 = "No válido";
		for (ComplejidadForm complejidad : complexities) {
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de adecuación: " + complejidad.getName() + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascomplejidad");
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonBySegment(messageResources, res.get(complejidad));
			StringBuilder sb = new StringBuilder();
			sb.append("<table:table table:name='Table_Allocation_").append(complejidad.getName()).append("' table:style-name='TableGraphic'>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
			// Header row
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 1
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 2
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 3
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			sb.append("</table:table>");
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascomplejidad");
			appendParagraphToMarker(odt, odfFileContent, "tablascomplejidad");
			// TODO Estilos de las tablas
			addTableStyles(odt);
		}
		return numImg;
	}

	/**
	 * Replace section comparision puntuaction compilance segment.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param categories        the categories
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionComparisionPuntuactionCompilanceSegment(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<CategoriaForm> categories, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMap(categories);
		String prevImage = "";
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage("observatory.graphic.global.puntuation.compilance.segments.mark.name") + i; // Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg");
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg", prevImage);
			}
			numImg++;
		}
		final Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageCompilanceResultsBySegmentMap(executionId, pageExecutionList,
				categories);
		// TODO Incluir la tabla a mano
		// add table
		String header1 = "Nivel de conformidad";
		String header2 = "Porcentaje de portales";
		String columna1 = "Totalmente conforme";
		String columna2 = "Parcialmente conforme";
		String columna3 = "No conforme";
		for (CategoriaForm category : categories) {
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de cumplimiento: " + category.getName() + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascumplimientosegmento");
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonBySegmentCompilance(messageResources, res.get(category));
			StringBuilder sb = new StringBuilder();
			sb.append("<table:table table:name='Table_Allocation_").append(category.getName()).append("' table:style-name='TableGraphic'>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
			// Header row
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 1
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 2
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 3
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			sb.append("</table:table>");
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascumplimientosegmento");
			appendParagraphToMarker(odt, odfFileContent, "tablascumplimientosegmento");
			// TODO Estilos de las tablas
			addTableStyles(odt);
		}
		return numImg;
	}

	/**
	 * Replace section comparision puntuaction compilance complexitivy.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param complexitivities  the complexitivities
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionComparisionPuntuactionCompilanceComplexitivy(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent,
			final String graphicPath, final List<ComplejidadForm> complexitivities, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMapComplexities(complexitivities);
		String prevImage = "";
		for (Integer i : resultLists.keySet()) {
			String grpahicName = messageResources.getMessage("observatory.graphic.global.puntuation.compilance.complexitiviy.mark.name") + i;
			// Si es la primera
			if (i == 1) {
				replaceImageGeneric(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg");
				prevImage = grpahicName;
			} else {
				// El resto
				addImageNext(odt, graphicPath + grpahicName + ".jpg", grpahicName, "image/jpeg", prevImage);
			}
			numImg++;
		}
		final Map<ComplejidadForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageCompilanceResultsByComplexitivityMap(executionId, pageExecutionList,
				ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1));
		// TODO Incluir la tabla a mano
		// add table
		String header1 = "Nivel de conformidad";
		String header2 = "Porcentaje de portales";
		String columna1 = "Totalmente conforme";
		String columna2 = "Parcialmente conforme";
		String columna3 = "No conforme";
		for (ComplejidadForm complejidad : complexitivities) {
			String stringTitle = "<text:p text:style-name=\"Titulo_5f_tablas\"><text:soft-page-break/>Porcentaje de cumplimiento: " + complejidad.getName() + "</text:p>";
			Element title = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(stringTitle.getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, title, "tablascumplimientocomplejidad");
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonBySegment(messageResources, res.get(complejidad));
			StringBuilder sb = new StringBuilder();
			sb.append("<table:table table:name='Table_Allocation_").append(complejidad.getName()).append("' table:style-name='TableGraphic'>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn1'/>");
			sb.append("<table:table-column table:style-name='TableGraphicColumn2'/>");
			// Header row
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgGreen'>");
			sb.append("<text:p text:style-name='GraphicTableHeader'>").append(header2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 1
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna1).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(0).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 2
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna2).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(1).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			// Row 3
			sb.append("<table:table-row>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(columna3).append("</text:p>");
			sb.append("</table:table-cell>");
			sb.append("<table:table-cell office:value-type='string' table:style-name='TableGraphicCellBgWhite'>");
			sb.append("<text:p text:style-name='GraphicTableCenter'>").append(results.get(2).getValue()).append("%</text:p>");
			sb.append("</table:table-cell>");
			sb.append("</table:table-row>");
			sb.append("</table:table>");
			Element node = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes())).getDocumentElement();
			appendNodeAtMarkerPosition(odt, odfFileContent, node, "tablascumplimientocomplejidad");
			appendParagraphToMarker(odt, odfFileContent, "tablascumplimientocomplejidad");
			// TODO Estilos de las tablas
			addTableStyles(odt);
		}
		return numImg;
	}

	/**
	 * Adds the table styles.
	 *
	 * @param odt the odt
	 * @throws Exception the exception
	 */
	private void addTableStyles(final OdfTextDocument odt) throws Exception {
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
		OdfStyle graphicTableCenter = styles.newStyle(OdfStyleFamily.Paragraph);
		graphicTableCenter.setAttribute("style:name", "GraphicTableCenter");
		graphicTableCenter.setProperty(OdfTextProperties.Color, "#000000");
		graphicTableCenter.setProperty(OdfTextProperties.FontName, "Arial");
		graphicTableCenter.setProperty(OdfTextProperties.FontNameComplex, "Times New Roman");
		graphicTableCenter.setProperty(OdfTextProperties.FontSizeAsian, "11pt");
		graphicTableCenter.setProperty(OdfTextProperties.FontSizeComplex, "10pt");
		graphicTableCenter.setProperty(OdfTextProperties.FontWeight, "normal");
		graphicTableCenter.setProperty(OdfTextProperties.FontWeightComplex, "normal");
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
	private void appendParagraphToMarker(final OdfTextDocument odt, final OdfFileDom odfFileContent, String marker) throws SAXException, IOException, ParserConfigurationException, Exception {
		Element p = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream("<text:p text:style-name=\"P\"/>".getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, p, marker);
	}

	/**
	 * Replace section 43.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param categories        the categories
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSection43(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, final List<CategoriaForm> categories,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = ResultadosAnonimosObservatorioUNEEN2019Utils.createGraphicsMap(categories);
		for (Integer i : resultLists.keySet()) {
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.name") + i + ".jpg", "image/jpeg");
			numImg++;
		}
		final Map<CategoriaForm, Map<String, BigDecimal>> res = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateMidPuntuationResultsBySegmentMap(executionId, pageExecutionList, categories);
		int tableNum = 1;
		for (CategoriaForm category : categories) {
			List<LabelValueBean> results = ResultadosAnonimosObservatorioUNEEN2019Utils.infoComparisonBySegmentPuntuation(messageResources, res.get(category));
			replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b2-", results.get(0).getValue());
			replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b3-", results.get(1).getValue());
			replaceText(odt, odfFileContent, "-43.t" + tableNum + ".b4-", results.get(2).getValue());
			++tableNum;
		}
		return numImg;
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSection441(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg", "image/jpeg");
		numImg++;
		final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
		final List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
		replaceText(odt, odfFileContent, "-441.t1.b2-", labelsL1.get(0).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b3-", labelsL1.get(1).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b4-", labelsL1.get(2).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b5-", labelsL1.get(3).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b6-", labelsL1.get(4).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b7-", labelsL1.get(5).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b8-", labelsL1.get(6).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b9-", labelsL1.get(7).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b10-", labelsL1.get(8).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b11-", labelsL1.get(9).getValue());
		// Nuevos
		replaceText(odt, odfFileContent, "-441.t1.b12-", labelsL1.get(10).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b13-", labelsL1.get(11).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b14-", labelsL1.get(12).getValue());
		replaceText(odt, odfFileContent, "-441.t1.b15-", labelsL1.get(13).getValue());
		return numImg;
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSection442(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg", "image/jpeg");
		numImg++;
		final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
		final List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
		replaceText(odt, odfFileContent, "-442.t1.b2-", labelsL2.get(0).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b3-", labelsL2.get(1).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b4-", labelsL2.get(2).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b5-", labelsL2.get(3).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b6-", labelsL2.get(4).getValue());
		replaceText(odt, odfFileContent, "-442.t1.b7-", labelsL2.get(5).getValue());
		return numImg;
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSection451(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + ".jpg", "image/jpeg");
		numImg++;
		final Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
		final List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results1);
		replaceText(odt, odfFileContent, "-451.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c7-", res.get(5).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b8-", res.get(6).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c8-", res.get(6).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b9-", res.get(7).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c9-", res.get(7).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b10-", res.get(8).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c10-", res.get(8).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b11-", res.get(9).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c11-", res.get(9).getRedPercentage());
		// Nuevos
		replaceText(odt, odfFileContent, "-451.t1.b12-", res.get(10).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c12-", res.get(10).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b13-", res.get(11).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c13-", res.get(11).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b14-", res.get(12).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c14-", res.get(12).getRedPercentage());
		replaceText(odt, odfFileContent, "-451.t1.b15-", res.get(13).getGreenPercentage());
		replaceText(odt, odfFileContent, "-451.t1.c15-", res.get(13).getRedPercentage());
		return numImg;
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSection452(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + ".jpg", "image/jpeg");
		numImg++;
		final Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
		final List<ModalityComparisonForm> res = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results2);
		replaceText(odt, odfFileContent, "-452.t1.b2-", res.get(0).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c2-", res.get(0).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b3-", res.get(1).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c3-", res.get(1).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b4-", res.get(2).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c4-", res.get(2).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b5-", res.get(3).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c5-", res.get(3).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b6-", res.get(4).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c6-", res.get(4).getRedPercentage());
		replaceText(odt, odfFileContent, "-452.t1.b7-", res.get(5).getGreenPercentage());
		replaceText(odt, odfFileContent, "-452.t1.c7-", res.get(5).getRedPercentage());
		return numImg;
	}

	/**
	 * Replace section 46.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSection46(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.aspect.mid.name") + ".jpg", "image/jpeg");
		numImg++;
		final Map<String, BigDecimal> result = ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
		final List<LabelValueBean> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoAspectMidsComparison(messageResources, result);
		replaceText(odt, odfFileContent, "-46.t1.b2-", labels.get(0).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b3-", labels.get(1).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b4-", labels.get(2).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b5-", labels.get(3).getValue());
		replaceText(odt, odfFileContent, "-46.t1.b6-", labels.get(4).getValue());
		return numImg;
	}

	/**
	 * Replace section cat 1.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCat1(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(pageExecutionList);
			final List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioUNEEN2019Utils.infoGlobalAccessibilityLevel(messageResources, resultsMap);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", graphicSuffix) + ".jpg",
					messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", ""), "image/jpeg");
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "1.t1.b2-", labelValueBean.get(0).getPercentageP());
			replaceText(odt, odfFileContent, "-" + numSection + "1.t1.c2-", labelValueBean.get(0).getNumberP());
			replaceText(odt, odfFileContent, "-" + numSection + "1.t1.b3-", labelValueBean.get(1).getPercentageP());
			replaceText(odt, odfFileContent, "-" + numSection + "1.t1.c3-", labelValueBean.get(1).getNumberP());
			replaceText(odt, odfFileContent, "-" + numSection + "1.t1.b4-", labelValueBean.get(2).getPercentageP());
			replaceText(odt, odfFileContent, "-" + numSection + "1.t1.c4-", labelValueBean.get(2).getNumberP());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section cat 2.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCat2(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.mark.allocation.segment.name", graphicSuffix) + ".jpg",
					messageResources.getMessage("observatory.graphic.mark.allocation.segment.name", ""), "image/jpeg");
			numImg++;
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCat31(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, final String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
		if (!pageExecutionList.isEmpty()) {
			final List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + graphicSuffix + ".jpg",
					messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name"), "image/jpeg");
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b2-", labelsL1.get(0).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b3-", labelsL1.get(1).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b4-", labelsL1.get(2).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b5-", labelsL1.get(3).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b6-", labelsL1.get(4).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b7-", labelsL1.get(5).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b8-", labelsL1.get(6).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b9-", labelsL1.get(7).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b10-", labelsL1.get(8).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b11-", labelsL1.get(9).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b12-", labelsL1.get(10).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b13-", labelsL1.get(11).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b14-", labelsL1.get(12).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "31.t1.b15-", labelsL1.get(13).getValue());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCat32(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, final String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		final Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
		if (!pageExecutionList.isEmpty()) {
			final List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + graphicSuffix + ".jpg",
					messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name"), "image/jpeg");
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b2-", labelsL2.get(0).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b3-", labelsL2.get(1).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b4-", labelsL2.get(2).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b5-", labelsL2.get(3).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b6-", labelsL2.get(4).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "32.t1.b7-", labelsL2.get(5).getValue());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 1 : 14 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCat41(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, final String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
			final List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results1);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + graphicSuffix + ".jpg",
					messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name"), "image/jpeg");
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b2-", labels.get(0).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c2-", labels.get(0).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b3-", labels.get(1).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c3-", labels.get(1).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b4-", labels.get(2).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c4-", labels.get(2).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b5-", labels.get(3).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c5-", labels.get(3).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b6-", labels.get(4).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c6-", labels.get(4).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b7-", labels.get(5).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c7-", labels.get(5).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b8-", labels.get(6).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c8-", labels.get(6).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b9-", labels.get(7).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c9-", labels.get(7).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b10-", labels.get(8).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c10-", labels.get(8).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b11-", labels.get(9).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c11-", labels.get(9).getRedPercentage());
			// nUEVOS
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b12-", labels.get(10).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c12-", labels.get(10).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b13-", labels.get(11).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c13-", labels.get(11).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b14-", labels.get(12).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c14-", labels.get(12).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.b15-", labels.get(13).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "41.t1.c15-", labels.get(13).getRedPercentage());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
		}
		return numImg;
	}

	/**
	 * Prioridad 2 : 6 niveles.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCat42(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, final String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
			final List<ModalityComparisonForm> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelVerificationModalityComparison(results2);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + graphicSuffix + ".jpg",
					messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name"), "image/jpeg");
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b2-", labels.get(0).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c2-", labels.get(0).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b3-", labels.get(1).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c3-", labels.get(1).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b4-", labels.get(2).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c4-", labels.get(2).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b5-", labels.get(3).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c5-", labels.get(3).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b6-", labels.get(4).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c6-", labels.get(4).getRedPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.b7-", labels.get(5).getGreenPercentage());
			replaceText(odt, odfFileContent, "-" + numSection + "42.t1.c7-", labels.get(5).getRedPercentage());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section evolution suitability level.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionSuitabilityLevel(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioUNEEN2019Utils.getEvolutionObservatoriesSitesByType(observatoryId, executionId,
					pageExecutionList);
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			final Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A, df);
			final Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA, df);
			final Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioUNEEN2019Utils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "1.t1", resultDataAA, true);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "2.t1", resultDataA, true);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "3.t1", resultDataNV, true);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
			numImg++;
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section evolution average score.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionAverageScore(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateEvolutionPuntuationDataSet(pageExecutionList);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "4.t1", resultData);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
			numImg++;
		}
		return numImg;
	}

	/**
	 * Replace section evolution score by verification.
	 *
	 * @param messageResources   the message resources
	 * @param odt                the odt
	 * @param odfFileContent     the odf file content
	 * @param graphicPath        the graphic path
	 * @param pageObservatoryMap the page observatory map
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionScoreByVerification(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws Exception {
		if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
			// TABLA 1
			Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION,
					pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.1") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "5.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.2") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "6.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.3") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "7.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.4") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "8.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.5") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "9.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.6") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "10.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.7") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "11.t1", resultData);
			// Reorganización de los datos
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.8") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "12.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.9") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "13.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.10") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "14.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.11") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "15.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.12") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "16.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.13") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "17.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "1.14") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "18.t1", resultData);
			// TABLA 2
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "19.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.2") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "20.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.3") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "21.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.4") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "22.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.5") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "23.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION, pageObservatoryMap);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", "2.6") + ".jpg", "image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "24.t1", resultData);
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			for (int i = 5; i < 25; i++) {
				replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
				numImg++;
			}
		}
		return numImg;
	}

	/**
	 * Replace section evolution score by aspect.
	 *
	 * @param messageResources the message resources
	 * @param odt              the odt
	 * @param odfFileContent   the odf file content
	 * @param graphicPath      the graphic path
	 * @param resultsByAspect  the results by aspect
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionScoreByAspect(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, Map<String, BigDecimal>> resultsByAspect) throws Exception {
		if (resultsByAspect != null && !resultsByAspect.isEmpty()) {
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.general"),
					resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID) + ".jpg",
					"image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "25.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.presentation"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID) + ".jpg",
					"image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "26.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.structure"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID) + ".jpg",
					"image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "27.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.navigation"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID) + ".jpg",
					"image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "28.t1", resultData);
			resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateAspectEvolutionPuntuationDataSet(messageResources.getMessage("observatory.aspect.alternatives"), resultsByAspect, df);
			replaceImg(odt, graphicPath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID) + ".jpg",
					"image/jpeg");
			numImg++;
			replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "29.t1", resultData);
		} else {
			PropertiesManager pmgr = new PropertiesManager();
			for (int i = 25; i < 30; i++) {
				replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
				numImg++;
			}
		}
		return numImg;
	}

	/**
	 * Replace section evolution score by segment.
	 *
	 * @param messageResources the message resources
	 * @param odt              the odt
	 * @param odfFileContent   the odf file content
	 * @param graphicPath      the graphic path
	 * @param resultsByAspect  the results by aspect
	 * @param categories       the categories
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionEvolutionScoreBySegment(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath,
			final Map<Date, Map<String, BigDecimal>> resultsByAspect, final List<CategoriaForm> categories) throws Exception {
		if (resultsByAspect != null && !resultsByAspect.isEmpty()) {
			final PropertiesManager pmgr = new PropertiesManager();
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
			int n = 25;
			for (CategoriaForm category : categories) {
				Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateSegmentEvolutionPuntuationDataSet(category.getName(), resultsByAspect, df);
				replaceImg(odt, graphicPath + category.getName() + ".jpg", "image/jpeg");
				numImg++;
				replaceEvolutionTextCellTables(odt, odfFileContent, numSection + "" + n + ".t1", resultData);
				n++;
			}
		} else {
			PropertiesManager pmgr = new PropertiesManager();
			for (int i = 25; i < 30; i++) {
				replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
				numImg++;
			}
		}
		return numImg;
	}

	/**
	 * Replace section cat 5.
	 *
	 * @param messageResources  the message resources
	 * @param odt               the odt
	 * @param odfFileContent    the odf file content
	 * @param graphicPath       the graphic path
	 * @param graphicSuffix     the graphic suffix
	 * @param pageExecutionList the page execution list
	 * @return the int
	 * @throws Exception the exception
	 */
	private int replaceSectionCat5(final MessageResources messageResources, final OdfTextDocument odt, final OdfFileDom odfFileContent, final String graphicPath, final String graphicSuffix,
			final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final Map<String, BigDecimal> result = ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
			final List<LabelValueBean> labels = ResultadosAnonimosObservatorioUNEEN2019Utils.infoAspectMidsComparison(messageResources, result);
			replaceImageGeneric(odt, graphicPath + messageResources.getMessage("observatory.graphic.aspect.mid.name") + graphicSuffix + ".jpg",
					messageResources.getMessage("observatory.graphic.aspect.mid.name"), "image/jpeg");
			numImg++;
			replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b2-", labels.get(0).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b3-", labels.get(1).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b4-", labels.get(2).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b5-", labels.get(3).getValue());
			replaceText(odt, odfFileContent, "-" + numSection + "5.t1.b6-", labels.get(4).getValue());
		} else {
			final PropertiesManager pmgr = new PropertiesManager();
			replaceImg(odt, pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.graphic.noResults"), "image/jpeg");
			numImg++;
		}
		return numImg;
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplate() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.common"));
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplateCategories() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.common.category"));
	}

	/**
	 * Gets the odf template.
	 *
	 * @return the odf template
	 * @throws Exception the exception
	 */
	private OdfTextDocument getOdfTemplateComplexities() throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		return (OdfTextDocument) OdfDocument.loadDocument(pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office.template.common.complexities"));
	}

	/**
	 * Merge styles to primary doc.
	 *
	 * @param primaryDoc   the primary doc
	 * @param secondaryDoc the secondary doc
	 * @throws Exception the exception
	 */
	private static void mergeStylesToPrimaryDoc(OdfTextDocument primaryDoc, OdfTextDocument secondaryDoc) throws Exception {
		OdfFileDom primaryContentDom = primaryDoc.getContentDom();
		OdfOfficeAutomaticStyles primaryDocAutomaticStyles = primaryDoc.getContentDom().getAutomaticStyles();
		OdfOfficeAutomaticStyles secondaryDocAutomaticStyles = secondaryDoc.getContentDom().getAutomaticStyles();
		// Adopt style nodes from secondary doc
		for (int i = 0; i < secondaryDocAutomaticStyles.getLength(); i++) {
			Node style = secondaryDocAutomaticStyles.item(i).cloneNode(true);
			if (style.hasAttributes()) {
				NamedNodeMap attributes = style.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node a = attributes.item(j);
					if (a.getLocalName().equals("name")) {
						a.setNodeValue(a.getNodeValue() + "");
					}
				}
			}
			if (style.hasChildNodes()) {
				updateNodeChildrenStyleNames(style, "", "name");
			}
			primaryDocAutomaticStyles.appendChild(primaryContentDom.adoptNode(style));
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
	 * Update node children style names.
	 *
	 * @param n                  the n
	 * @param stringToAddToStyle the string to add to style
	 * @param nodeLocalName      the node local name
	 */
	private static void updateNodeChildrenStyleNames(Node n, String stringToAddToStyle, String nodeLocalName) {
		NodeList childNodes = n.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node currentChild = childNodes.item(i);
			if (currentChild.hasAttributes()) {
				NamedNodeMap attributes = currentChild.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node a = attributes.item(j);
					if (a.getLocalName().equals(nodeLocalName)) {
						a.setNodeValue(a.getNodeValue() + stringToAddToStyle);
					}
				}
			}
			if (currentChild.hasChildNodes()) {
				updateNodeChildrenStyleNames(currentChild, stringToAddToStyle, nodeLocalName);
			}
		}
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
	protected void replaceImageGeneric(final OdfTextDocument odt, final String filePath, final String imageNameOdt, final String mymeType) throws Exception {
		final File f = new File(filePath);
		final XPath xpath = odt.getXPath();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//draw:frame[@draw:name = '%s']/draw:image", imageNameOdt), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			final OdfElement node = (OdfElement) nodeList.item(i);
			final String embededName = node.getAttribute("xlink:href").trim();
			// Replace href
			String value = "Pictures/" + f.getName();
			node.setAttribute("xlink:href", value);
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
	protected void addImageNext(final OdfTextDocument odt, final String filePath, final String imageNameOdt, final String mymeType, final String prevImageName) throws Exception {
		final File f = new File(filePath);
		final XPath xpath = odt.getXPath();
		final OdfFileDom odfFileContent = odt.getContentDom();
		final NodeList nodeList = (NodeList) xpath.evaluate(String.format("//draw:frame[@draw:name = '%s']/draw:image", prevImageName), odfFileContent, XPathConstants.NODESET);
		String newImageDOm = "<draw:frame draw:style-name='fr4' draw:name='" + imageNameOdt
				+ "' text:anchor-type='as-char' svg:y='0mm' svg:width='149.45mm' style:rel-width='scale' svg:height='120.21mm' style:rel-height='scale' draw:z-index='118'>"
				+ "<draw:image xlink:href='Pictures/" + f.getName() + "' xlink:type='simple' xlink:show='embed' xlink:actuate='onLoad'/>" + "<svg:desc> </svg:desc>" + "</draw:frame>";
		Element newIMage = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(newImageDOm.getBytes())).getDocumentElement();
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.getParentNode().getParentNode().insertBefore(odfFileContent.adoptNode(newIMage), node.getParentNode().getNextSibling());
			insertFileInsideODTFile(odt, "Pictures/" + f.getName(), f, mymeType);
		}
	}
}
