package es.oaw.wcagem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.jfree.util.Log;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.gob.oaw.rastreador2.pdf.utils.CheckDescriptionsManager;
import es.inteco.common.Constants;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.intav.form.ProblemForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.PlantillaForm;
import es.inteco.rastreador2.dao.plantilla.PlantillaDAO;
import es.oaw.wcagem.enums.WcagEmPointKey;

public class WcagOdtUtils {
	private static final String[] codesWcag = { "9.1.1.1", "9.1.2.1", "9.1.2.2", "9.1.2.3", "9.1.2.5", "9.1.3.1", "9.1.3.2", "9.1.3.3", "9.1.3.4", "9.1.3.5", "9.1.4.1", "9.1.4.2", "9.1.4.3",
			"9.1.4.4", "9.1.4.5", "9.1.4.10", "9.1.4.11", "9.1.4.12", "9.1.4.13", "9.2.1.1", "9.2.1.2", "9.2.1.4", "9.2.2.1", "9.2.2.2", "9.2.3.1", "9.2.4.1", "9.2.4.2", "9.2.4.3", "9.2.4.4",
			"9.2.4.5", "9.2.4.6", "9.2.4.7", "9.2.5.1", "9.2.5.2", "9.2.5.3", "9.2.5.4", "9.3.1.1", "9.3.1.2", "9.3.2.1", "9.3.2.2", "9.3.2.4", "9.3.3.1", "9.3.3.2", "9.3.2.3", "9.3.3.2", "9.3.3.3",
			"9.3.3.4", "9.4.1.1", "9.4.1.2" };
	final static MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
	final static CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();

	private static File getOdtTemplate() throws Exception {
		PlantillaForm plantilla = PlantillaDAO.findByName(DataBaseManager.getConnection(), "hallazgostop");
		if (plantilla != null && plantilla.getDocumento() != null && plantilla.getDocumento().length > 0) {
			File f = File.createTempFile("tmp_template", ".odt");
			FileUtils.writeByteArrayToFile(f, plantilla.getDocumento());
			return f;
		}
		return null;
	}

	public static OdfTextDocument generateOdt(List<ObservatoryEvaluationForm> observatoryEvaluationForm) throws Exception {
		File file = getOdtTemplate();
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		OdfTextDocument odt = (OdfTextDocument) OdfDocument.loadDocument(fis);
		Map<String, Map<String, List<ResultReport>>> results = generateData(observatoryEvaluationForm);
		final OdfFileDom odfFileContent = odt.getContentDom();
		String newContent = "";
		for (String clave : results.keySet()) {
			Map<String, List<ResultReport>> examples = results.get(clave);
			List<ResultReport> errors = null;
			for (String site : examples.keySet()) {
				errors = examples.get(site);
				String ttt = "<text:p text:style-name=\"P87\" text:outline-level=\"1\">" + site + "</text:p>";
				Element newNode1 = createNewElement(ttt);
				appendNodeAtMarkerPosition(odt, odfFileContent, newNode1, "--" + clave + "--");
				for (ResultReport error : errors) {
					if (error.getSolution() == null || StringUtils.isBlank(error.getSolution())) {
						error.setSolution("-");
					}
					newContent = " <text:list text:style-name=\"L1\">\n" + "<text:list-item>\n" + "<text:p text:style-name=\"P87\" text:outline-level=\"2\">Título: " + error.getDescription()
							+ "</text:p>\n" + "</text:list-item>\n" + "<text:list-item>\n" + "<text:p text:style-name=\"P87\" text:outline-level=\"2\">Problema: " + error.getError() + "</text:p>\n"
							+ "</text:list-item>\n" + "<text:list-item>\n" + "<text:p text:style-name=\"P87\" text:outline-level=\"2\">Solución: " + error.getSolution() + "</text:p>\n"
							+ "</text:list-item>\n" + "</text:list>";
					Element newNode = createNewElement(newContent);
					appendNodeAtMarkerPosition(odt, odfFileContent, newNode, "--" + clave + "--");
					appendParagraphToMarker(odt, odfFileContent, "--" + clave + "--");
				}
			}
			if (errors == null || errors.size() == 0) {
				replaceText(odt, odfFileContent, "--" + clave + "--", "No se han encontrado problemas durante el análisis");
			} else {
				replaceText(odt, odfFileContent, "--" + clave + "--", "Problemas encontrados");
			}
		}
		for (String code : codesWcag) {
			replaceText(odt, odfFileContent, "--" + code + "--", "No se han encontrado problemas durante el análisis");
		}
		return odt;
	}

	protected static Element createNewElement(String newElement) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(newElement.getBytes())).getDocumentElement();
	}

	protected static void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText, final String nodeStr) throws XPathExpressionException {
		XPath xpath = odt.getXPath();
		DTMNodeList nodeList = (DTMNodeList) xpath.evaluate(String.format("//%s[contains(text(),'%s')]", nodeStr, oldText), odfFileContent, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			OdfElement node = (OdfElement) nodeList.item(i);
			node.setTextContent(node.getTextContent().replace(oldText, newText));
		}
	}

	private static void appendNodeAtMarkerPosition(final OdfTextDocument odt, final OdfFileDom odfFileContent, Node newNode, String markername) throws Exception {
		XPath xpath = odt.getXPath();
		NodeList nodeList = (NodeList) xpath.evaluate(String.format("//%s[contains(text(),'%s')]", "text:p", markername), odfFileContent, XPathConstants.NODESET);
		OdfElement node;
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = (OdfElement) nodeList.item(i);
			if (node.getParentNode() != null) {
				node.getParentNode().appendChild(odfFileContent.adoptNode(newNode));
			}
		}
	}

	private static void appendParagraphToMarker(final OdfTextDocument odt, final OdfFileDom odfFileContent, String marker) throws SAXException, IOException, ParserConfigurationException, Exception {
		Element p = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream("<text:p text:style-name=\"P\"/>".getBytes())).getDocumentElement();
		appendNodeAtMarkerPosition(odt, odfFileContent, p, marker);
	}

	protected static void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText) throws XPathExpressionException {
		if (oldText != null && newText != null) {
			replaceText(odt, odfFileContent, oldText, newText, "text:p");
		}
	}

	protected static Map generateData(List<ObservatoryEvaluationForm> observatoryEvaluationForms) {
		Map globalResultsMap = new HashMap<String, HashMap<String, List<ResultReport>>>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationForms) {
			List<ObservatoryLevelForm> groups = observatoryEvaluationForm.getGroups();
			for (ObservatoryLevelForm group : groups) {
				List<ObservatorySuitabilityForm> suitabilityGroups = group.getSuitabilityGroups();
				for (ObservatorySuitabilityForm suitabilityGroup : suitabilityGroups) {
					List<ObservatorySubgroupForm> subGroups = suitabilityGroup.getSubgroups();
					for (ObservatorySubgroupForm subGroup : subGroups) {
						List<ProblemForm> problems = subGroup.getProblems();
						for (ProblemForm problem : problems) {
							Log.info(problem);
							final String name = subGroup.getDescription()
									.substring(subGroup.getDescription().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
							String code = getWCAG2Code(name);
							if (code != null) {
								String codeReport = getWCAG2CodeReport(code);
								String errorMessage = checkDescriptionsManager.getString(problem.getError());
								String rationaleMessage = checkDescriptionsManager.getString(problem.getRationale());
								String title = messageResources.getMessage(subGroup.getDescription());
								String entity = observatoryEvaluationForm.getEntity();
								if (globalResultsMap.containsKey(codeReport)) {
									Map siteResultsMap = (Map) globalResultsMap.get(codeReport);
									List reportElements = new ArrayList();
									if (siteResultsMap.containsKey(observatoryEvaluationForm.getUrl())) {
										reportElements = (List) siteResultsMap.get(observatoryEvaluationForm.getUrl());
									}
									ResultReport result = new ResultReport();
									result.setError(errorMessage);
									result.setDescription(title);
									result.setSolution(rationaleMessage);
									reportElements.add(result);
									siteResultsMap.put(observatoryEvaluationForm.getUrl(), reportElements);
									globalResultsMap.put(codeReport, siteResultsMap);
								} else {
									List reportElements = new ArrayList<ResultReport>();
									ResultReport result = new ResultReport();
									result.setError(errorMessage);
									result.setDescription(title);
									result.setSolution(rationaleMessage);
									reportElements.add(result);
									Map siteResultsMap = new HashMap<String, List<ResultReport>>();
									siteResultsMap.put(observatoryEvaluationForm.getUrl(), reportElements);
									globalResultsMap.put(codeReport, siteResultsMap);
								}
							}
						}
					}
				}
			}
		}
		return globalResultsMap;
	}

	private static String getWCAG2Code(String value) {
		String code = null;
		switch (value) {
		case WcagEmUtils._1_1:
			code = WcagEmPointKey.WCAG_1_1_1.getWcagEmId();
			break;
		case WcagEmUtils._2_3:
			code = WcagEmPointKey.WCAG_1_4_10.getWcagEmId();
			break;
		case WcagEmUtils._1_12:
			code = WcagEmPointKey.WCAG_2_4_4.getWcagEmId();
			break;
		case WcagEmUtils._2_4:
			code = WcagEmPointKey.WCAG_2_4_5.getWcagEmId();
			break;
		case WcagEmUtils._1_7:
			code = WcagEmPointKey.WCAG_3_1_1.getWcagEmId();
			break;
		case WcagEmUtils._2_1:
			code = WcagEmPointKey.WCAG_3_1_2.getWcagEmId();
			break;
		case WcagEmUtils._2_6:
			code = WcagEmPointKey.WCAG_3_2_3.getWcagEmId();
			break;
		case WcagEmUtils._1_14:
			code = WcagEmPointKey.WCAG_4_1_1.getWcagEmId();
			break;
		case WcagEmUtils._1_8:
			code = WcagEmPointKey.WCAG_2_1_1.getWcagEmId();
			// WcagEmPointKey.WCAG_2_2_1.getWcagEmId();
			// WcagEmPointKey.WCAG_2_2_2.getWcagEmId();
			// WcagEmPointKey.WCAG_4_1_2.getWcagEmId();
			break;
		case WcagEmUtils._1_9:
			code = WcagEmPointKey.WCAG_2_5_3.getWcagEmId();
			// WcagEmPointKey.WCAG_2_5_3.getWcagEmId();
			// WcagEmPointKey.WCAG_1_3_1.getWcagEmId()
			// WcagEmPointKey.WCAG_4_1_2.getWcagEmId()
			break;
		case WcagEmUtils._1_11:
			code = WcagEmPointKey.WCAG_2_4_1.getWcagEmId();
			// WcagEmPointKey.WCAG_2_4_2.getWcagEmId();
			break;
		case WcagEmUtils._1_13:
			code = WcagEmPointKey.WCAG_3_2_1.getWcagEmId();
			// WcagEmPointKey.WCAG_3_2_2.getWcagEmId();
			break;
		case WcagEmUtils._2_2:
			code = WcagEmPointKey.WCAG_1_4_12.getWcagEmId();
			// WcagEmPointKey.WCAG_1_4_3.getWcagEmId()
			break;
		case WcagEmUtils._2_5:
			code = WcagEmPointKey.WCAG_1_3_5.getWcagEmId();
			// WcagEmPointKey.WCAG_1_3_4.getWcagEmId();
			// WcagEmPointKey.WCAG_1_3_5.getWcagEmId();
			// WcagEmPointKey.WCAG_2_4_3.getWcagEmId();
			// WcagEmPointKey.WCAG_2_4_7.getWcagEmId();
			break;
		case WcagEmUtils._1_10:
			code = WcagEmPointKey.WCAG_1_3_1.getWcagEmId();
			// WcagEmPointKey.WCAG_4_1_2.getWcagEmId();
			break;
		}
		return code;
	}

	private static String getWCAG2CodeReport(String value) {
		String code = "";
		switch (value) {
		case "WCAG2:non-text-content":
			code = "9.1.1.1";
			break;
		case "WCAG2:info-and-relationships":
			code = "9.1.3.1";
			break;
		case "WCAG2:orientation":
			code = "9.1.3.4";
			break;
		case "WCAG2:identify-input-purpose":
			code = "9.1.3.5";
			break;
		case "WCAG2:contrast-minimum":
			code = "9.1.4.3";
			break;
		case "WCAG2:reflow":
			code = "9.1.4.10";
			break;
		case "WCAG2:text-spacing":
			code = "9.1.4.12";
			break;
		case "WCAG2:keyboard":
			code = "9.2.1.1";
			break;
		case "WCAG2:timing-adjustable":
			code = "9.2.2.1";
			break;
		case "WCAG2:pause-stop-hide":
			code = "9.2.2.2";
			break;
		case "WCAG2:three-flashes-or-below-threshold":
			code = "9.2.3.1";
			break;
		case "WCAG2:bypass-blocks":
			code = "9.2.4.1";
			break;
		case "WCAG2:page-titled":
			code = "9.2.4.2";
			break;
		case "WCAG2:focus-order":
			code = "9.2.4.3";
			break;
		case "WCAG2:link-purpose-in-context":
			code = "9.2.4.4";
			break;
		case "WCAG2:multiple-ways":
			code = "9.2.4.5";
			break;
		case "WCAG2:focus-visible":
			code = "9.2.4.7";
			break;
		case "WCAG2:label-in-name":
			code = "9.2.5.3";
			break;
		case "WCAG2:language-of-page":
			code = "9.2.5.3";
			break;
		case "WCAG2:language-of-parts":
			code = "9.3.1.1";
			break;
		case "WCAG2:on-focus":
			code = "9.3.2.1";
			break;
		case "WCAG2:on-input":
			code = "9.3.3.2";
			break;
		case "WCAG2:consistent-navigation":
			code = "9.3.2.3";
			break;
		case "WCAG2:labels-or-instructions":
			code = "9.3.3.2";
			break;
		case "WCAG2:parsing":
			code = "9.4.1.1";
			break;
		case "WCAG2:name-role-value":
			code = "9.4.1.2";
			break;
		}
		return code;
	}
}
