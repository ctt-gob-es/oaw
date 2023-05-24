package es.oaw.wcagem;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;
import org.odftoolkit.odfdom.OdfElement;
import org.odftoolkit.odfdom.OdfFileDom;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

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
	private static final String[] reportCodes = { "9.1.1.1", "9.1.2.1", "9.1.2.2", "9.1.2.3", "9.1.2.5", "9.1.3.1", "9.1.3.2", "9.1.3.3", "9.1.3.4", "9.1.3.5", "9.1.4.1", "9.1.4.2", "9.1.4.3",
			"9.1.4.4", "9.1.4.5", "9.1.4.10", "9.1.4.11", "9.1.4.12", "9.1.4.13", "9.2.1.1", "9.2.1.2", "9.2.1.4", "9.2.2.1", "9.2.2.2", "9.2.3.1", "9.2.4.1", "9.2.4.2", "9.2.4.3", "9.2.4.4",
			"9.2.4.5", "9.2.4.6", "9.2.4.7", "9.2.5.1", "9.2.5.2", "9.2.5.3", "9.2.5.4", "9.3.1.1", "9.3.1.2", "9.3.2.1", "9.3.2.2", "9.3.2.4", "9.3.3.1", "9.3.3.2", "9.3.2.3", "9.3.3.2", "9.3.3.3",
			"9.3.3.4", "9.4.1.1", "9.4.1.2", "9.4.1.3" };
	private static final MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
	private static final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
	private static final String templateName = "hallazgos";
	private static final String OK = "No se han encontrado problemas durante el análisis de este punto";
	private static final String NO_OK = "Se han encontrado los siguientes problemas en el análisis";

	/**
	 * Generate odt report "hallazgos"
	 * 
	 * @param observatoryEvaluationForm Observatory evaluation data
	 * @return OdfTextDocument report
	 * @throws Exception
	 */
	public static OdfTextDocument generateOdtReport(List<ObservatoryEvaluationForm> observatoryEvaluationForm) throws Exception {
		File file = getOdtTemplate();
		if (Objects.isNull(file)) {
			return null;
		}
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		OdfTextDocument odtDocument = (OdfTextDocument) OdfDocument.loadDocument(fis);
		final OdfFileDom odfFileContent = odtDocument.getContentDom();
		Map<String, Map<String, List<AnalysisResult>>> results = generateDataMap(observatoryEvaluationForm);
		for (String reportCode : results.keySet()) {
			fillReport(reportCode, results, odtDocument, odfFileContent);
		}
		for (String reportCode : reportCodes) {
			replaceText(odtDocument, odfFileContent, searchCodeFormat(reportCode), OK);
		}
		return odtDocument;
	}

	/**
	 * Fill report
	 * 
	 * @param reportCode
	 * @param results
	 * @param odtDocument
	 * @param odfFileContent
	 * @throws Exception
	 */
	private static void fillReport(String reportCode, Map<String, Map<String, List<AnalysisResult>>> results, OdfTextDocument odtDocument, OdfFileDom odfFileContent) throws Exception {
		Map<String, List<AnalysisResult>> sites = results.get(reportCode);
		List<AnalysisResult> errors = null;
		for (String site : sites.keySet()) {
			errors = sites.get(site);
			createHeader(odtDocument, odfFileContent, reportCode, site);
			for (AnalysisResult error : errors) {
				createError(odtDocument, odfFileContent, reportCode, error);
			}
		}
		if (errors == null || errors.size() == 0) {
			replaceText(odtDocument, odfFileContent, searchCodeFormat(reportCode), OK);
		} else {
			replaceText(odtDocument, odfFileContent, searchCodeFormat(reportCode), NO_OK);
		}
	}

	private static void createHeader(OdfTextDocument odtDocument, OdfFileDom odfFileContent, String reportCode, String site) throws Exception {
		String header = "<text:p text:style-name=\"P87\" text:outline-level=\"1\">" + site + "</text:p>";
		Element newNode1 = createNewElement(header);
		appendNodeAtMarkerPosition(odtDocument, odfFileContent, newNode1, searchCodeFormat(reportCode));
	};

	private static void createError(OdfTextDocument odtDocument, OdfFileDom odfFileContent, String reportCode, AnalysisResult error) throws Exception {
		if (error.getSolution() == null || StringUtils.isBlank(error.getSolution()) || StringUtils.isEmpty(error.getSolution())) {
			error.setSolution("-");
		}
		String template = " <text:list text:style-name=\"L1\">\n" + "<text:list-item>\n" + "<text:p text:style-name=\"P87\" text:outline-level=\"2\">Título: " + cleanOldCodes(error.getDescription())
				+ "</text:p>\n" + "</text:list-item>\n" + "<text:list-item>\n" + "<text:p text:style-name=\"P87\" text:outline-level=\"2\">Problema: " + error.getError() + "</text:p>\n"
				+ "</text:list-item>\n" + "<text:list-item>\n" + "<text:p text:style-name=\"P87\" text:outline-level=\"2\">Solución: " + error.getSolution() + "</text:p>\n" + "</text:list-item>\n"
				+ "</text:list>";
		Element newNode = createNewElement(template);
		appendNodeAtMarkerPosition(odtDocument, odfFileContent, newNode, searchCodeFormat(reportCode));
		appendParagraphToMarker(odtDocument, odfFileContent, searchCodeFormat(reportCode));
	};

	private static Map<String, Map<String, List<AnalysisResult>>> generateDataMap(List<ObservatoryEvaluationForm> observatoryEvaluationForms) {
		Map<String, Map<String, List<AnalysisResult>>> globalResultsMap = new HashMap<>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationForms) {
			List<ObservatoryLevelForm> groups = observatoryEvaluationForm.getGroups();
			for (ObservatoryLevelForm group : groups) {
				List<ObservatorySuitabilityForm> suitabilityGroups = group.getSuitabilityGroups();
				for (ObservatorySuitabilityForm suitabilityGroup : suitabilityGroups) {
					List<ObservatorySubgroupForm> subGroups = suitabilityGroup.getSubgroups();
					for (ObservatorySubgroupForm subGroup : subGroups) {
						fillAnalysisResults(subGroup, observatoryEvaluationForm, globalResultsMap);
					}
				}
			}
		}
		return globalResultsMap;
	}

	private static void fillAnalysisResults(ObservatorySubgroupForm subGroup, ObservatoryEvaluationForm observatoryEvaluationForm, Map<String, Map<String, List<AnalysisResult>>> globalResultsMap) {
		List<ProblemForm> problems = subGroup.getProblems();
		for (ProblemForm problem : problems) {
			final String name = subGroup.getDescription().substring(subGroup.getDescription().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
			List<String> codes = getWCAGCodes(name);
			for (String code : codes) {
				if (code != null) {
					String codeReport = getWCAG2CodeReport(code);
					String title = messageResources.getMessage(subGroup.getDescription());
					String errorMessage = checkDescriptionsManager.getString(problem.getError());
					String solution = cleanHtmlLabels(checkDescriptionsManager.getString(problem.getRationale()));
					if (globalResultsMap.containsKey(codeReport)) {
						existingCodeReport(codeReport, title, errorMessage, solution, observatoryEvaluationForm, globalResultsMap);
					} else {
						noExistingCodeReport(codeReport, title, errorMessage, solution, observatoryEvaluationForm, globalResultsMap);
					}
				}
			}
		}
	}

	private static void existingCodeReport(String codeReport, String title, String errorMessage, String solution, ObservatoryEvaluationForm observatoryEvaluationForm,
			Map<String, Map<String, List<AnalysisResult>>> globalResultsMap) {
		Map<String, List<AnalysisResult>> siteResultsMap = globalResultsMap.get(codeReport);
		List<AnalysisResult> reportElements = new ArrayList<>();
		if (siteResultsMap.containsKey(observatoryEvaluationForm.getUrl())) {
			reportElements = siteResultsMap.get(observatoryEvaluationForm.getUrl());
		}
		reportElements.add(fillResult(errorMessage, title, solution));
		siteResultsMap.put(observatoryEvaluationForm.getUrl(), reportElements);
		globalResultsMap.put(codeReport, siteResultsMap);
	}

	private static void noExistingCodeReport(String codeReport, String title, String errorMessage, String solution, ObservatoryEvaluationForm observatoryEvaluationForm,
			Map<String, Map<String, List<AnalysisResult>>> globalResultsMap) {
		List<AnalysisResult> reportElements = new ArrayList<>();
		reportElements.add(fillResult(errorMessage, title, solution));
		Map<String, List<AnalysisResult>> siteResultsMap = new HashMap<String, List<AnalysisResult>>();
		siteResultsMap.put(observatoryEvaluationForm.getUrl(), reportElements);
		globalResultsMap.put(codeReport, siteResultsMap);
	}

	private static File getOdtTemplate() throws Exception {
		PlantillaForm plantilla = PlantillaDAO.findByName(DataBaseManager.getConnection(), templateName);
		if (plantilla != null && plantilla.getDocumento() != null && plantilla.getDocumento().length > 0) {
			File f = File.createTempFile("tmp_template", ".odt");
			FileUtils.writeByteArrayToFile(f, plantilla.getDocumento());
			return f;
		}
		return null;
	}

	private static Element createNewElement(String newElement) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(newElement.getBytes())).getDocumentElement();
	}

	private static void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText, final String nodeStr) throws XPathExpressionException {
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

	private static void replaceText(final OdfTextDocument odt, final OdfFileDom odfFileContent, final String oldText, final String newText) throws XPathExpressionException {
		if (oldText != null && newText != null) {
			replaceText(odt, odfFileContent, oldText, newText, "text:p");
		}
	}

	private static List<String> getWCAGCodes(String value) {
		List<String> codes = Arrays.asList();
		switch (value) {
		case WcagEmUtils._1_1:
			codes = Arrays.asList(WcagEmPointKey.WCAG_1_1_1.getWcagEmId());
			break;
		case WcagEmUtils._2_3:
			codes = Arrays.asList(WcagEmPointKey.WCAG_1_4_10.getWcagEmId());
			break;
		case WcagEmUtils._1_12:
			codes = Arrays.asList(WcagEmPointKey.WCAG_2_4_4.getWcagEmId());
			break;
		case WcagEmUtils._2_4:
			codes = Arrays.asList(WcagEmPointKey.WCAG_2_4_5.getWcagEmId());
			break;
		case WcagEmUtils._1_7:
			codes = Arrays.asList(WcagEmPointKey.WCAG_3_1_1.getWcagEmId());
			break;
		case WcagEmUtils._2_1:
			codes = Arrays.asList(WcagEmPointKey.WCAG_3_1_2.getWcagEmId());
			break;
		case WcagEmUtils._2_6:
			codes = Arrays.asList(WcagEmPointKey.WCAG_3_2_3.getWcagEmId());
			break;
		case WcagEmUtils._1_14:
			codes = Arrays.asList(WcagEmPointKey.WCAG_4_1_1.getWcagEmId());
			break;
		case WcagEmUtils._1_8:
			codes = Arrays.asList(WcagEmPointKey.WCAG_2_1_1.getWcagEmId(), WcagEmPointKey.WCAG_2_2_1.getWcagEmId(), WcagEmPointKey.WCAG_2_2_2.getWcagEmId(), WcagEmPointKey.WCAG_4_1_2.getWcagEmId());
			break;
		case WcagEmUtils._1_9:
			codes = Arrays.asList(WcagEmPointKey.WCAG_2_5_3.getWcagEmId(), WcagEmPointKey.WCAG_2_5_3.getWcagEmId(), WcagEmPointKey.WCAG_1_3_1.getWcagEmId(), WcagEmPointKey.WCAG_4_1_2.getWcagEmId());
			break;
		case WcagEmUtils._1_11:
			codes = Arrays.asList(WcagEmPointKey.WCAG_2_4_1.getWcagEmId(), WcagEmPointKey.WCAG_2_4_2.getWcagEmId());
			break;
		case WcagEmUtils._1_13:
			codes = Arrays.asList(WcagEmPointKey.WCAG_3_2_1.getWcagEmId(), WcagEmPointKey.WCAG_3_2_2.getWcagEmId());
			break;
		case WcagEmUtils._2_2:
			codes = Arrays.asList(WcagEmPointKey.WCAG_1_4_12.getWcagEmId(), WcagEmPointKey.WCAG_1_4_3.getWcagEmId());
			break;
		case WcagEmUtils._2_5:
			codes = Arrays.asList(WcagEmPointKey.WCAG_1_3_5.getWcagEmId(), WcagEmPointKey.WCAG_1_3_4.getWcagEmId(), WcagEmPointKey.WCAG_1_3_5.getWcagEmId(), WcagEmPointKey.WCAG_2_4_3.getWcagEmId(),
					WcagEmPointKey.WCAG_2_4_7.getWcagEmId());
			break;
		case WcagEmUtils._1_10:
			codes = Arrays.asList(WcagEmPointKey.WCAG_1_3_1.getWcagEmId(), WcagEmPointKey.WCAG_4_1_2.getWcagEmId());
			break;
		}
		return codes;
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
		default:
			code = "-";
			break;
		}
		return code;
	}

	private static String searchCodeFormat(String code) {
		return "--" + code + "--";
	}

	private static String cleanOldCodes(String title) {
		if (title != null) {
			String[] parts = title.trim().split(" ");
			if (parts.length > 0) {
				return title.replace(parts[0], "").trim();
			} else {
				return title;
			}
		}
		return "-";
	}

	private static AnalysisResult fillResult(String errorMessage, String title, String solution) {
		AnalysisResult result = new AnalysisResult();
		result.setError(errorMessage);
		result.setDescription(title);
		result.setSolution(solution);
		return result;
	}

	private static String cleanHtmlLabels(String message) {
		if (message != null) {
			return message.replaceAll("<[^>]*>", "").trim();
		} else {
			return "-";
		}
	}
}
