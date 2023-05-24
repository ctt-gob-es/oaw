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
/*
Copyright ©2006, University of Toronto. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a 
copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Adaptive Technology Resource Centre, University of Toronto
130 St. George St., Toronto, Ontario, Canada
Telephone: (416) 978-4360
*/
package ca.utoronto.atrc.tile.accessibilitychecker;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.util.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.icu.text.SimpleDateFormat;

import es.gob.oaw.language.Diccionario;
import es.gob.oaw.language.ExtractTextHandler;
import es.gob.oaw.language.LanguageChecker;
import es.gob.oaw.utils.AccesibilityDeclarationCheckUtils;
import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.IntavConstants;
import es.inteco.common.ValidationError;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.flesch.FleschAdapter;
import es.inteco.flesch.FleschAnalyzer;
import es.inteco.flesch.FleschUtils;
import es.inteco.intav.dao.TAnalisisAccesibilidadDAO;
import es.inteco.intav.form.CheckedLinks;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;

/**
 * The Class Check.
 */
public class Check {
	/** The check ok code. */
	private int checkOkCode;
	/** The id. */
	private int id;
	/** The related with. */
	private int relatedWith;
	/** The status. */
	private String status;
	/** The note. */
	private String note;
	/** The confidence. */
	private int confidence;
	/** The first occurance only. */
	private boolean firstOccuranceOnly;
	/** The name map. */
	@XmlTransient
	private Map<String, Node> nameMap;
	/** The error hashtable. */
	@XmlTransient
	private Map<String, Node> errorHashtable;
	/** The rationale hashtable. */
	@XmlTransient
	private Map<String, Node> rationaleHashtable;
	/** The key element. */
	private String keyElement;
	/** The trigger element. */
	private String triggerElement;
	/** The language appropriate. */
	private String languageAppropriate;
	/** The prerequisites. */
	@XmlTransient
	private List<Integer> prerequisites;
	/** The vector code. */
	@XmlTransient
	private List<CheckCode> vectorCode;

	public Map<String, Node> getNameMap() {
		return nameMap;
	}

	public void setNameMap(Map<String, Node> nameMap) {
		this.nameMap = nameMap;
	}

	public Map<String, Node> getErrorHashtable() {
		return errorHashtable;
	}

	public void setErrorHashtable(Map<String, Node> errorHashtable) {
		this.errorHashtable = errorHashtable;
	}

	public Map<String, Node> getRationaleHashtable() {
		return rationaleHashtable;
	}

	public void setRationaleHashtable(Map<String, Node> rationaleHashtable) {
		this.rationaleHashtable = rationaleHashtable;
	}

	public String getLanguageAppropriate() {
		return languageAppropriate;
	}

	public String getAppropriateData() {
		return languageAppropriate;
	}

	public void setLanguageAppropriate(String languageAppropriate) {
		this.languageAppropriate = languageAppropriate;
	}

	public void setCheckOkCode(int checkOkCode) {
		this.checkOkCode = checkOkCode;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setConfidence(int confidence) {
		this.confidence = confidence;
	}

	public void setFirstOccuranceOnly(boolean firstOccuranceOnly) {
		this.firstOccuranceOnly = firstOccuranceOnly;
	}

	public void setKeyElement(String keyElement) {
		this.keyElement = keyElement;
	}

	public void setTriggerElement(String triggerElement) {
		this.triggerElement = triggerElement;
	}

	public void setPrerequisites(List<Integer> prerequisites) {
		this.prerequisites = prerequisites;
	}

	public void setVectorCode(List<CheckCode> vectorCode) {
		this.vectorCode = vectorCode;
	}

	/**
	 * Instantiates a new check.
	 */
	public Check() {
		checkOkCode = CheckFunctionConstants.CHECK_STATUS_UNINITIALIZED;
		id = -1;
		status = "";
		confidence = CheckFunctionConstants.CONFIDENCE_NOT_SET;
		nameMap = new HashMap<>();
		errorHashtable = new Hashtable<>();
		languageAppropriate = IntavConstants.ENGLISH_ABB;
		firstOccuranceOnly = false;
		prerequisites = new ArrayList<>();
		keyElement = null;
		triggerElement = null;
		vectorCode = new ArrayList<>();
		rationaleHashtable = new Hashtable<>();
	}

	/**
	 * Gets the check ok code.
	 *
	 * @return the check ok code
	 */
	public int getCheckOkCode() {
		return checkOkCode;
	}

	/**
	 * Gets the related with.
	 *
	 * @return the related with
	 */
	public int getRelatedWith() {
		return relatedWith;
	}

	/**
	 * Sets the related with.
	 *
	 * @param relatedWith the new related with
	 */
	public void setRelatedWith(int relatedWith) {
		this.relatedWith = relatedWith;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Gets the confidence.
	 *
	 * @return the confidence
	 */
	public int getConfidence() {
		return confidence;
	}

	/**
	 * Gets the confidence string.
	 *
	 * @return the confidence string
	 */
	public String getConfidenceString() {
		if ((confidence == CheckFunctionConstants.CONFIDENCE_NOT_SET) || (confidence == CheckFunctionConstants.CONFIDENCE_LOW)) {
			return IntavConstants.LOW;
		} else if (confidence == CheckFunctionConstants.CONFIDENCE_MEDIUM) {
			return IntavConstants.MEDIUM;
		} else if (confidence == CheckFunctionConstants.CONFIDENCE_HIGH) {
			return IntavConstants.HIGH;
		} else {
			return IntavConstants.CAN_NOT_TELL;
		}
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public Node getError() {
		return errorHashtable.get(IntavConstants.ENGLISH_ABB);
	}

	/**
	 * Gets the error string.
	 *
	 * @return the error string
	 */
	public String getErrorString() {
		Node nodeError = getError();
		if (nodeError == null) {
			return "";
		}
		return EvaluatorUtility.getElementText(nodeError);
	}

	/**
	 * Gets the rationale.
	 *
	 * @return the rationale
	 */
	public Node getRationale() {
		return rationaleHashtable.get(IntavConstants.ENGLISH_ABB);
	}

	/**
	 * Sets the rationale text.
	 *
	 * @param text     the text
	 * @param language the language
	 */
	public void setRationaleText(String text, String language) {
		Node nodeRationale = rationaleHashtable.get(language);
		if (nodeRationale != null) {
			// remove all the child nodes (the current text)
			Node nodeChild = nodeRationale.getFirstChild();
			while (nodeChild != null) {
				Node nodeTemp = nodeChild;
				nodeChild = nodeChild.getNextSibling();
				nodeRationale.removeChild(nodeTemp);
			}
			// create a new text node to hold the new text
			Node newTextNode = nodeRationale.getOwnerDocument().createTextNode(text);
			nodeRationale.appendChild(newTextNode);
		}
	}

	/**
	 * Gets the rationale string.
	 *
	 * @return the rationale string
	 */
	public String getRationaleString() {
		Node nodeDescription = getRationale();
		if (nodeDescription == null) {
			return "";
		}
		return EvaluatorUtility.getElementText(nodeDescription);
	}

	/**
	 * Gets the trigger element.
	 *
	 * @return the trigger element
	 */
	public String getTriggerElement() {
		return triggerElement;
	}

	/**
	 * Gets the key element.
	 *
	 * @return the key element
	 */
	public String getKeyElement() {
		return keyElement;
	}

	/**
	 * Gets the prerequisites.
	 *
	 * @return the prerequisites
	 */
	public List<Integer> getPrerequisites() {
		return prerequisites;
	}

	/**
	 * Checks if is first occurance only.
	 *
	 * @return true, if is first occurance only
	 */
	public boolean isFirstOccuranceOnly() {
		return firstOccuranceOnly;
	}

	/**
	 * Checks if is prerequisite.
	 *
	 * @param idGiven the id given
	 * @return true, if is prerequisite
	 */
	// return true if the given check ID is a prerequisite for this check
	public boolean isPrerequisite(int idGiven) {
		for (Integer prerequisite : prerequisites) {
			if (prerequisite == idGiven) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Prerequisites OK.
	 *
	 * @param vectorChecksRun the vector checks run
	 * @return true, if successful
	 */
	// return true if required prerequisites for this check have been run
	public boolean prerequisitesOK(List<Integer> vectorChecksRun) {
		for (Integer prerequisite : prerequisites) {
			boolean prerun = false;
			for (Integer runId : vectorChecksRun) {
				if (prerequisite.equals(runId)) {
					prerun = true;
					break;
				}
			}
			if (!prerun) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates the vector functions.
	 *
	 * @return the list
	 */
	// Crea una lista de funciones basadas en el lenguage requerido
	private List<CheckCode> createVectorFunctions() {
		List<CheckCode> vectorFunctions = new ArrayList<>();
		for (CheckCode checkCode : vectorCode) {
			if (checkCode.getType() == CheckFunctionConstants.CODE_TYPE_LANGUAGE) {
				if (checkCode.getLanguage().equals(languageAppropriate)) {
					List<CheckCode> vectorLanguageCode = checkCode.getVectorCode();
					for (CheckCode aVectorLanguageCode : vectorLanguageCode) {
						vectorFunctions.add(aVectorLanguageCode);
					}
				}
			} else { // must be condition or function
				vectorFunctions.add(checkCode);
			}
		}
		return vectorFunctions;
	}

	// Devuelve verdadero si el elemento no tiene problemas de accesibilidad
	/**
	 * Do evaluation.
	 *
	 * @param elementGiven the element given
	 * @param idAnalysis   the id analysis
	 * @return true, if successful
	 * @throws AccessibilityError the accessibility error
	 */
	// Lanza una excepción si hay un problema de accesibilidad
	public boolean doEvaluation(Element elementGiven, final Long idAnalysis) throws AccessibilityError {
		// Crea una lista de funciones basadas en el lenguage requerido
		List<CheckCode> vectorFunctions = createVectorFunctions();
		// Ejecuta las funciones
		try {
			for (CheckCode checkCode : vectorFunctions) {
				// Se ejecutan todas las comprobaciones que no sean de CSS
				// Las comprobaciones de CSS se ejecutan posteriormente en el
				// método Evaluator.performEvaluation
				if (!"css".equalsIgnoreCase(triggerElement)) {
					// Add to checkcode the idAnalysys
					checkCode.setIdAnalysis(idAnalysis);
					if (evaluateCode(checkCode, elementGiven) != CheckFunctionConstants.CODE_RESULT_PROBLEM) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Warning: Exception caught in Check.doEvaluation", Check.class, Logger.LOG_LEVEL_WARNING, e);
			return false;
		}
		throw new AccessibilityError(AccessibilityError.FAIL, elementGiven);
	}

	/**
	 * Evaluate function.
	 *
	 * @param checkCode    the check code
	 * @param elementGiven the element given
	 * @return the int
	 */
	// Evalúa un nodo en concreto
	private int evaluateFunction(CheckCode checkCode, Element elementGiven) {
		// get the node referenced by the 'node' parameter
		Node nodeNode = null;
		String stringNodeAttribute = checkCode.getNodeRelation();
		if (".".equals(stringNodeAttribute)) {
			nodeNode = elementGiven;
		} else if (stringNodeAttribute.charAt(0) == '@') {
			nodeNode = elementGiven.getAttributeNode(stringNodeAttribute.substring(1));
		} else if ("parent".equals(stringNodeAttribute)) {
			nodeNode = elementGiven.getParentNode();
		} else if ("./li[1]".equals(stringNodeAttribute)) {
			// first list item
			NodeList listItems = elementGiven.getElementsByTagName("li");
			if (listItems.getLength() > 0) {
				nodeNode = listItems.item(0);
			}
		} else if ("./dt[1]".equals(stringNodeAttribute)) {
			// first list item
			NodeList listItems = elementGiven.getElementsByTagName("dt");
			if (listItems.getLength() > 0) {
				nodeNode = listItems.item(0);
			}
		} else if ("./img[1]".equals(stringNodeAttribute)) {
			// first img child
			NodeList childItems = elementGiven.getChildNodes();
			if (childItems.getLength() > 0) {
				for (int i = 0; i < childItems.getLength(); i++) {
					if ("img".equalsIgnoreCase(childItems.item(i).getNodeName())) {
						nodeNode = childItems.item(i);
						break;
					}
				}
			} else {
				return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
			}
		} else if (".//link/@rel".equals(stringNodeAttribute)) {
			// all link elements that have a 'rel' attribute
			NodeList listNodes = elementGiven.getElementsByTagName("link");
			if (listNodes.getLength() == 0) {
				return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
			}
			if (checkCode.getFunctionId() == CheckFunctionConstants.FUNCTION_TEXT_EQUALS) {
				for (int x = 0; x < listNodes.getLength(); x++) {
					Element elementTemp = (Element) listNodes.item(x);
					String stringTemp = elementTemp.getAttribute("rel");
					if (stringTemp.length() == 0) {
						continue;
					}
					if (!"stylesheet".equalsIgnoreCase(stringTemp) && !"alternate".equalsIgnoreCase(stringTemp)) {
						return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
					}
				}
				return CheckFunctionConstants.CODE_RESULT_PROBLEM;
			} else if (checkCode.getFunctionId() == CheckFunctionConstants.FUNCTION_TEXT_NOTEQUALS) {
				for (int x = 0; x < listNodes.getLength(); x++) {
					Element elementTemp = (Element) listNodes.item(x);
					String stringTemp = elementTemp.getAttribute("rel");
					if (stringTemp.length() == 0) {
						continue;
					}
					if ("alternate".equalsIgnoreCase(stringTemp)) {
						return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
					}
				}
				return CheckFunctionConstants.CODE_RESULT_PROBLEM;
			}
		} else if (".//img/@alt".equals(stringNodeAttribute)) {
			// all img elements that have an 'alt' attribute
			// return true if there is no alt text in all img elements
			NodeList listNodes = elementGiven.getElementsByTagName("img");
			if (listNodes.getLength() == 0) {
				return CheckFunctionConstants.CODE_RESULT_PROBLEM;
			}
			for (int x = 0; x < listNodes.getLength(); x++) {
				Element elementTemp = (Element) listNodes.item(x);
				String stringTemp = elementTemp.getAttribute("alt").trim();
				if (stringTemp.length() > 0) {
					return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
				}
			}
			return CheckFunctionConstants.CODE_RESULT_PROBLEM;
		} else {
			Logger.putLog("unknown node: " + stringNodeAttribute, Check.class, Logger.LOG_LEVEL_INFO);
			return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
		}
		return processCode(checkCode, nodeNode, elementGiven) ? CheckFunctionConstants.CODE_RESULT_PROBLEM : CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
	}

	// Evalua una condición 'and' entre varios elementos
	// Por ejemplo, para comprobar el atributo 'alt' de una imagen, comprueba
	// que su altura sea mayor que 'x' Y
	// su anchura sea mayor que 'y' Y que el atributo 'alt' tenga más de 'z'
	/**
	 * Evaluate condition and.
	 *
	 * @param checkCode    the check code
	 * @param elementGiven the element given
	 * @return the int
	 */
	// caracteres
	private int evaluateConditionAnd(CheckCode checkCode, Element elementGiven) {
		List<CheckCode> vectorCodeFunctions = checkCode.getVectorCode();
		boolean foundProblem = false;
		for (CheckCode vectorCodeFunction : vectorCodeFunctions) {
			int result = evaluateCode(vectorCodeFunction, elementGiven);
			if (result == CheckFunctionConstants.CODE_RESULT_NOPROBLEM) {
				return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
			}
			if (result == CheckFunctionConstants.CODE_RESULT_PROBLEM) {
				foundProblem = true;
			}
		}
		return foundProblem ? CheckFunctionConstants.CODE_RESULT_PROBLEM : CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
	}

	/**
	 * Evaluate condition or.
	 *
	 * @param checkCode    the check code
	 * @param elementGiven the element given
	 * @return the int
	 */
	// Evalua una condición 'or' entre varios elementos
	private int evaluateConditionOr(CheckCode checkCode, Element elementGiven) {
		List<CheckCode> vectorCodeFunctions = checkCode.getVectorCode();
		for (CheckCode vectorCodeFunction : vectorCodeFunctions) {
			if (evaluateCode(vectorCodeFunction, elementGiven) == CheckFunctionConstants.CODE_RESULT_PROBLEM) {
				return CheckFunctionConstants.CODE_RESULT_PROBLEM;
			}
		}
		return CheckFunctionConstants.CODE_RESULT_NOPROBLEM;
	}

	/**
	 * Evaluate condition.
	 *
	 * @param checkCode    the check code
	 * @param elementGiven the element given
	 * @return the int
	 */
	// Evalua una condición entre varios nodos
	private int evaluateCondition(CheckCode checkCode, Element elementGiven) {
		// all the functions within the 'and' must detect a problem to return a
		// problem
		if (checkCode.getConditionType() == CheckFunctionConstants.CONDITION_AND) {
			return evaluateConditionAnd(checkCode, elementGiven);
		} else if (checkCode.getConditionType() == CheckFunctionConstants.CONDITION_OR) {
			return evaluateConditionOr(checkCode, elementGiven);
		} else {
			Logger.putLog("Warning: check " + id + " has invalid condition type: " + checkCode.getType(), Check.class, Logger.LOG_LEVEL_WARNING);
			return CheckFunctionConstants.CODE_RESULT_IGNORE;
		}
	}

	// Método recursivo para evaluar funciones y condiciones
	/**
	 * Evaluate code.
	 *
	 * @param checkCode    the check code
	 * @param elementGiven the element given
	 * @return the int
	 */
	// Devuelve un código con el resultado de la evaluación
	private int evaluateCode(CheckCode checkCode, Element elementGiven) {
		// Hemos llegado a un nodo y hay que evaluarlo
		if (checkCode.getType() == CheckFunctionConstants.CODE_TYPE_FUNCTION) {
			return evaluateFunction(checkCode, elementGiven);
		} else if (checkCode.getType() == CheckFunctionConstants.CODE_TYPE_CONDITION) {
			return evaluateCondition(checkCode, elementGiven);
		} else if (checkCode.getType() == CheckFunctionConstants.CODE_TYPE_LANGUAGE) {
			if (checkCode.getLanguage().equals(languageAppropriate)) {
				List<CheckCode> vectorLanguageCode = checkCode.getVectorCode();
				for (CheckCode aVectorLanguageCode : vectorLanguageCode) {
					int result = evaluateCode(aVectorLanguageCode, elementGiven);
					if (result != CheckFunctionConstants.CODE_RESULT_IGNORE) {
						return result;
					}
				}
			}
			return CheckFunctionConstants.CODE_RESULT_IGNORE;
		} else {
			Logger.putLog("Warning: Invalid checkCode type: " + checkCode.getType(), Check.class, Logger.LOG_LEVEL_WARNING);
			return CheckFunctionConstants.CODE_RESULT_IGNORE;
		}
	}

	/**
	 * Process code.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Devuelve verdadero si hay un problema de accesibilidad
	private boolean processCode(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		switch (checkCode.getFunctionId()) {
		case CheckFunctionConstants.FUNCTION_TEXT_EQUALS:
			return functionTextEquals(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_NOTEQUALS:
			return functionTextNotEquals(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTE_EXISTS:
			return functionAttributeExists(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTE_MISSING:
			return functionAttributeMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTES_SAME:
			return functionAttributesSame(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTES_NOT_SAME:
			return functionAttributesNotSame(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_GREATER:
			return functionElementCountGreaterThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_INTERNAL_ELEMENT_COUNT_GREATER:
			return functionInternalElementCountGreaterThan(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_GREATER_THAN);
		case CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_LESS:
			return functionElementCountLessThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_EQUALS:
			return functionElementCountEquals(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_NOTEQUALS:
			return functionElementCountNotEquals(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTE_NULL:
			return functionAttributeNull(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CHARS_GREATER:
			return functionCharactersGreaterThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CHARS_LESS:
			return functionCharactersLessThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LINK_CHARS_GREATER:
			return functionLinkCharactersGreaterThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_ALL_LABELS:
			return functionNotAllLabels(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NUMBER_ANY:
			return functionNumberAny(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NUMBER_LESS_THAN:
			return functionNumberLessThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NUMBER_GREATER_THAN:
			return functionNumberGreaterThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CONTAINER:
			return functionContainer(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOTCONTAINER:
			return functionContainerNot(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_METADATA_MISSING:
			return functionMetadataMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_LINK_EQUIVS_MISSING:
			return functionTextLinkEquivMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LABEL_NOT_ASSOCIATED:
			return functionLabelNotAssociated(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LABEL_INCORRECTLY_ASSOCIATED:
			return functionLabelIncorrectlyAssociated(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LABEL_NO_TEXT:
			return functionLabelNoText(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_DLINK_MISSING:
			return functionDLinkMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NEXT_HEADING_BAD:
			return functionNextHeadingWrong(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_PREV_HEADING_BAD:
			return functionPreviousHeadingWrong(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_DUPLICATE_FOLLOWING_HEADERS:
			return functionDuplicateFollowingHeaders(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_INCORRECT_HEADING_STRUCTURE:
			return functionIncorrectHeaderStructure(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NO_CORRECT_DOCUMENT_STRUCTURE:
			return functionNoCorrectDocumentStructure(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HEADERS_MISSING:
			return functionHeadersMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HEADERS_EXIST:
			return functionHeadersExist(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOSCRIPT_MISSING:
			return functionNoscriptMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOFRAME_MISSING:
			return functionNoframeMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IFRAME_HAS_NOT_ALTERNATIVE:
			return functionIFrameHasNotAlternative(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IFRAME_HAS_ALTERNATIVE:
			return functionIFrameHasAlternative(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOEMBED_MISSING:
			return functionNoembedMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ROW_COUNT:
			return CheckTables.functionRowCount(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_COL_COUNT:
			return CheckTables.functionColumnCount(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ELEMENT_PREVIOUS:
			return functionElementPrevious(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TARGETS_SAME:
			return functionTargetsSame(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HTML_CONTENT_NOT:
			return functionHtmlContentNot(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_LANGUAGE:
			return functionHasLanguage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_VALID_LANGUAGE:
			return functionNotValidLanguage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_MULTIRADIO_NOFIELDSET:
			return functionMultiRadioNoFieldset(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_MULTICHECKBOX_NOFIELDSET:
			return functionMultiCheckboxNoFieldset(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LUMINOSITY_CONTRAST_RATIO:
			return functionLuminosityContrastRatio(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ERT_COLOR_ALGORITHM:
			return functionColorContrastWaiErt(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_DOCTYPE_ATTRIBUTE_NOT_EQUAL:
			return functionDoctypeAttributeNotEqual(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_VALIDATE:
			return !functionValidate(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_VALIDATE_CSS:
			return !functionValidateCss(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TABLE_TYPE:
			return CheckTables.functionTableType(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_MISSING_ID_HEADERS:
			return CheckTables.functionMissingIdHeaders(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_MISSING_SCOPE:
			return functionMissingScope(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_INVALID_SCOPE:
			return functionInvalidScope(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_NOT_ELEMENT_CHILDS:
			return !hasElementChilds(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CAPTION_SUMMARY_SAME:
			return functionCaptionSummarySame(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IS_ONLY_BLANKS:
			return functionIsOnlyBlanks(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IS_EMPTY_ELEMENT:
			return functionIsEmptyElement(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IS_NOT_ONLY_BLANKS:
			return !functionIsOnlyBlanks(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_IS_ONLY_BLANKS:
			return functionNotIsOnlyBlanks(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_VALID_URL:
			return functionNotValidUrl(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_EXTERNAL_URL:
			return functionNotExternalUrl(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_OBJECT_HAS_NOT_ALTERNATIVE:
			return functionObjectHasNotAlternative(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_APPLET_HAS_NOT_ALTERNATIVE:
			return functionAppletHasNotAlternative(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_APPLET_HAS_ALTERNATIVE:
			return functionAppletHasAlternative(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_GRAMMAR_LANG:
			return functionLangGrammar(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TABLE_HEADING_COMPLEX:
			return CheckTables.functionTableHeadingComplex(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TABLE_COMPLEX:
			return CheckTables.functionTableComplex(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_ALL_ID_HEADERS:
			return !CheckTables.functionMissingIdHeaders(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CONTAINS:
			return functionContains(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CONTAINS_NOT:
			return functionContainsNot(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ALL_ELEMENTS_NOT_LIKE_THIS:
			return functionAllElementsNotLikeThis(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_DEFINITION_LIST_CONSTRUCTION:
			return functionDefinitionListConstruction(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CHECK_COLORS:
			return functionCheckColors(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_VALID_DOCTYPE:
			return !functionCheckValidDoctype(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_ELEMENT_INTO:
			return functionHasElementInto(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SAME_FOLLOWING_LIST:
			return functionSameFollowingList(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SAME_FOLLOWING_LIST_NOT:
			return functionSameFollowingListNot(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_CONTAIN_GENERAL_QUOTE:
			return functionTextContainGeneralQuote(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SAME_ELEMENT_NOT:
			return functionSameElementNot(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SAME_ELEMENT:
			return functionSameElement(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_NBSP_ENTITIES:
			return functionHasNbspEntities(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LINK_SAME_PAGE:
			return functionLinkSamePage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_MATCH:
			return functionTextMatch(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_NOT_MATCH:
			return functionTextNotMatch(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTE_ELEMENT_TEXT_MATCH:
			return functionAttributeElementTextMatch(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTE_ELEMENT_TEXT_NOT_MATCH:
			return !functionAttributeElementTextMatch(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NUM_MORE_CONTROLS:
			return functionNumMoreControls(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IS_ODD:
			return functionIsOdd(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IS_EVEN:
			return functionIsEven(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_USER_DATA_MATCHS:
			return functionUserDataMatchs(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_USER_DATA_MATCHS:
			return functionNotUserDataMatchs(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_CHILDREN_HAVE_ATTRIBUTE:
			return functionNotChildrenHaveAttribute(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_CLEAR_LANGUAGE:
			return functionNotClearLanguage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_NOT_ENOUGH_TEXT:
			return functionHasNotEnoughText(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_NOT_SECTION_LINK:
			return functionHasNotSectionLink(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_CORRECT_HEADING:
			return !CheckTables.functionCorrectHeading(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_FALSE_PARAGRAPH_LIST:
			return functionFalseParagraphList(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_FALSE_BR_LIST:
			return functionFalseBrList(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_FALSE_HEADER_WITH_ONLY_CELL:
			return CheckTables.functionHeaderWithOnlyCell(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_INCORRECT_TABINDEX:
			return functionHasIncorrectTabindex(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LANGUAGE_NOT_EQUALS:
			return functionLanguageNotEquals(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_EMPTY_ELEMENTS:
			return functionEmptyElements(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ELEMENTS_EXCESSIVE_USAGE:
			return functionElementsUsage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTES_EXCESSIVE_USAGE:
			return functionAttributesExcessiveUsage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TABINDEX_EXCESSIVE_USAGE:
			return functionTabIndexExcessiveUsage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ELEMENT_PERCENTAGE:
			return functionElementPercentage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CORRECT_LINKS:
			return functionCorrectLinks(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CHILD_ELEMENT_CHARS_GREATER:
			return functionChildElementCharactersGreaterThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CHILD_ELEMENT_CHARS_LESSER:
			return !functionChildElementCharactersGreaterThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LAYOUT_TABLE:
			return functionLayoutTable(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_LAYOUT_TABLE:
			return !functionLayoutTable(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LAYOUT_TABLE_NUMBER:
			return functionLayoutTableNumber(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONTACT:
			return functionAccessibilityDeclarationNoContact(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_REVISION_DATE:
			return functionAccessibilityDeclarationNoRevisionDate(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONFORMANCE_LEVEL:
			return functionAccessibilityDeclarationNoConformanceLevel(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_COMPLEX_STRUCTURE:
			return functionHasComplexStructure(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TOO_MANY_BROKEN_LINKS:
			return functionTooManyBrokenLinks(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_EXIST_ATTRIBUTE_VALUE:
			return functionExistAttributeValue(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_EXIST_ATTRIBUTE_VALUE:
			return functionNotExistAttributeValue(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_EMPTY_SECTION:
			return functionEmptySection(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_COUNT_ATTRIBUTE_VALUE_GREATER_THAN:
			return functionCountAttributeValueGreaterThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IS_ANIMATED_GIF:
			return functionIsAnimatedGif(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_FOLLOWING_HEADERS_WITHOUT_CONTENT:
			return functionFollowingHeadersWithoutContent(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_IMG_DIMENSIONS_LESS_THAN:
			return functionImgDimensionsLessThan(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_REDUNDANT_IMG_ALT:
			return functionRedundantImgAlt(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_VALIDATION_ERRORS:
			return functionHasValidationErrors(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_GUESS_LANGUAGE:
			return functionGuessLanguage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_GROUPED_SELECTION_BUTTONS:
			return functionGroupedRadioButtons(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_NOT_FIRST_CHILD:
			return functionNotFirstChild(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_REQUIRED_CONTROLS:
			return functionRequiredControls(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_FALSE_BR_IMAGE_LIST:
			return functionFalseBrImageList(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_OTHER_LANGUAGE:
			return functionOtherLanguage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_CURRENT_LANGUAGE:
			return functionCurrentLanguage(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TABLE_HEADING_BLANK:
			return CheckTables.functionTableHeadingBlank(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TITLE_NOT_CONTAINS:
			return functionTitleNotContains(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ARIA_LABELLEDBY_REFERENCED:
			return functionAriaLabelledbyReferences(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ARIA_DESCRIBEDBY_REFERENCED:
			return functionAriaDescribedbyReferences(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTE_LENGHT:
			return attributeLength(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTE_LABELEDBY_LENGHT:
			return attributeLengthLabeledBy(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HEADERS_WAI_MISSING:
			return functionWAIHeadersMissing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HEADERS_WAI_LEVEL_1_MISSING:
			return functionWAIHeadersLevel1Missing(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_FOLLOWING_WAI_HEADERS_WITHOUT_CONTENT:
			return functionFollowingWAIHeadersWithoutContent(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SKIP_WAI_HEADERS_LEVEL:
			return skipWaiHeadersLevel(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_ATTRIBUTE_VALUE:
			return functionElementCountAttributeValue(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ACCESSIBILITY_CONTACT_FORM:
			return functionAccessibilityContactForm(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_MATCH_PROPERTIES:
			return functionTextMatchProperties(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_NOT_MATCH_PROPERTIES:
			return !functionTextMatchProperties(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_TEXT_NOT_EQUALS_PROPERTIES:
			return !functionTextEqualsProperties(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ATTRIBUTTES_EQUALS:
			return functionAttributesAreEquals(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_LABEL_MATCH_ARIAL_VALUE:
			return functionLabelMatchArialValue(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_AUTOCOMPLETE_VALID:
			return !functionAutocompleteValid(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_HAS_SECTION:
			return !functionAccessibilityHasSection(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SECTION_HAS_TEXT:
			return !functionSectionHasText(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SECTION_HAS_MAILTO:
			return functionSectionMailto(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SECTION_HAS_ELEMENT:
			return functionSectionHasElement(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_ACCESIBILITY_YEAR:
			return functionAccesibilityYear(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SECTION_HAS_DATE:
			return !functionSectionHasRegex(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_SECTION_HAS_PHONE:
			return !functionSectionHasRegex(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_EMPTY_TABLE_70:
			return functionEmptyTable(checkCode, nodeNode, elementGiven);
		case CheckFunctionConstants.FUNCTION_MORE_HEADERS_THAN_FIELDSETS:
			return functionCompareHeaderAndFieldset(checkCode, nodeNode, elementGiven);
		default:
			Logger.putLog("Warning: unknown function ID:" + checkCode.getFunctionId(), Check.class, Logger.LOG_LEVEL_WARNING);
			break;
		}
		return false;
	}

	/**
	 * Function title not contains.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	public static boolean functionTitleNotContains(final CheckCode checkCode, final Node nodeNode, final Element elementGiven) {
		String key = checkCode.getFunctionValue();
		// Modificada para recuperar la expresión de un fichero
		PropertiesManager pm = new PropertiesManager();
		String regexp = pm.getValue("check.patterns.properties", key);
		final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		final NodeList titles = elementGiven.getOwnerDocument().getDocumentElement().getElementsByTagName("title");
		for (int i = 0; i < titles.getLength(); i++) {
			final Element title = (Element) titles.item(i);
			if (pattern.matcher(title.getTextContent()).find()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Function link characters greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionLinkCharactersGreaterThan(final CheckCode checkCode, final Node nodeNode, final Element elementGiven) {
		// Se usa la función getLabelText porque está preparada para extraer las
		// alternativas de las imágenes
		final String linkLabelText = EvaluatorUtility.getLabelText(elementGiven);
		final int maxChars = Integer.parseInt(checkCode.getFunctionValue());
		return linkLabelText.length() > maxChars;
	}

	/**
	 * Actualziada para que recupera la expresión regurlar de fichero de propiedades.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionRequiredControls(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList inputs = elementGiven.getElementsByTagName("input");
		int filteredControls = 0;
		for (int i = 0; i < inputs.getLength(); i++) {
			if (inputs.item(i) instanceof Element) {
				final Element element = (Element) inputs.item(i);
				// Solo se contabilizan los input de tipo text
				if (element.getAttribute("type").isEmpty() || "text".equals(element.getAttribute("type"))) {
					filteredControls++;
				}
			}
		}
		final int minInputs = Integer.parseInt(checkCode.getFunctionNumber());
		if (filteredControls > minInputs) {
			final String formText = EvaluatorUtility.getLabelText(elementGiven);
			// Recupera la expresión regular de un fichero de propiedades
			PropertiesManager pm = new PropertiesManager();
			String regexp = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
			Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			final Matcher matcher = pattern.matcher(formText);
			boolean foundRequiredText = matcher.find();
			if (!foundRequiredText && elementGiven.getParentNode() != null) {
				final String parentText = EvaluatorUtility.getLabelText(elementGiven.getParentNode());
				foundRequiredText = pattern.matcher(parentText).find();
			}
			return !foundRequiredText;
		}
		return false;
	}

	/**
	 * Function not first child.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotFirstChild(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Node firstChild = elementGiven.getFirstChild();
		if (firstChild == null) {
			return true;
		} else {
			Node currentNode = firstChild;
			while (currentNode.getNodeType() == Node.TEXT_NODE && currentNode.getTextContent().trim().isEmpty()) {
				currentNode = currentNode.getNextSibling();
			}
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				final List allowedTags = Arrays.asList(checkCode.getFunctionAttribute1().split(";"));
				if (allowedTags.contains(currentNode.getNodeName().toLowerCase())) {
					boolean isFirstChild = CheckUtils.isElementTagName(CheckUtils.getFirstChildElement((Element) currentNode), checkCode.getFunctionValue());
					boolean isNextSibling = CheckUtils.isElementTagName(CheckUtils.getFirstSiblingElement((Element) currentNode), checkCode.getFunctionValue());
					return !isFirstChild && !isNextSibling;
				} else {
					return !CheckUtils.isElementTagName(currentNode, checkCode.getFunctionValue());
				}
			}
			return true;
		}
	}

	/**
	 * Function grouped radio buttons.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionGroupedRadioButtons(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Map<String, List<Node>> radioGroups = new HashMap<>();
		final NodeList listInputs = elementGiven.getElementsByTagName("input");
		// Extraemos todos los input tipo radio y los guardamos agrupados en el
		// map radioGroups (key es el name de los controles)
		for (int i = 0; i < listInputs.getLength(); i++) {
			final Element elementInput = (Element) listInputs.item(i);
			if (elementInput.getAttribute("type").equalsIgnoreCase(checkCode.getFunctionValue())) {
				final String stringName = elementInput.getAttribute("name");
				if (!radioGroups.containsKey(stringName)) {
					final List<Node> lista = new ArrayList<>();
					radioGroups.put(stringName, lista);
				}
				radioGroups.get(stringName).add(elementInput);
			}
		}
		final int allowedUngrouped = Integer.parseInt(checkCode.getFunctionNumber());
		for (Map.Entry<String, List<Node>> radioGroupEntry : radioGroups.entrySet()) {
			if (radioGroupEntry.getValue().size() > allowedUngrouped) {
				// En algunos casos, se agrupan elementos que no tiene name mezclados de varios fieldet por lo que vamos adicionalmente a contar cuantos no están en un fieldset además del tamaño del
				// grupo
				int countNotInFieldSet = 0;
				// Comprobar si están agrupados
				for (Node node : radioGroupEntry.getValue()) {
					final Node ancestor = getAncestor(node, Arrays.asList("FIELDSET", "FORM"));
					if (ancestor == null || !"FIELDSET".equalsIgnoreCase(ancestor.getNodeName())) {
						// Si el ancestor no es un fieldset se incrementa el contador
						countNotInFieldSet++;
						// Si se ha superado el máximo se devuelve error
						if (countNotInFieldSet > allowedUngrouped) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the ancestor.
	 *
	 * @param node     the node
	 * @param stopTags the stop tags
	 * @return the ancestor
	 */
	private Node getAncestor(Node node, List<String> stopTags) {
		final Node parent = node.getParentNode();
		if (parent == null) {
			return null;
		} else if (stopTags.contains(parent.getNodeName())) {
			return parent;
		} else {
			return getAncestor(parent, stopTags);
		}
	}

	/**
	 * Function guess language.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionGuessLanguage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Document document = elementGiven.getOwnerDocument();
		final String languageCode = extractLanguageCode(getLanguage(elementGiven, false));
		if (!languageCode.isEmpty()) {
			final ExtractTextHandler extractTextHandler = new ExtractTextHandler(languageCode);
			try {
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new SAXResult(extractTextHandler));
				final String extractedText = extractTextHandler.getExtractedText();
				// Caracteres mínimos para comprobar el idioma (menos caracteres
				// da resultados poco fiables)
				if (extractedText.length() < 50) {
					return false;
				}
				final LanguageChecker languageChecker = new LanguageChecker(languageCode);
				// Si son distintos hay que devolver true para indicar que es
				// fallo
				return !languageChecker.isExpectedLanguage(extractedText);
			} catch (TransformerException e) {
				Logger.putLog("Error al intentar extraer el texto en functionGuessLanguage", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Eliminamos las variantes idiomáticas para quedarnos únicamente con el idioma base (ej. en-us pasa a en)
	 *
	 * @param lang la cadena que representa el idioma completo con las variantes
	 * @return una cadena que representa el idioma base
	 */
	private String extractLanguageCode(final String lang) {
		if (lang == null) {
			return "";
		} else if (lang.contains("-")) {
			return lang.substring(0, lang.indexOf('-'));
		} else {
			return lang;
		}
	}

	/**
	 * Function other language.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionOtherLanguage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String expectedLanguage = extractLanguageCode(getLanguage(elementGiven, false));
		final Document document = elementGiven.getOwnerDocument();
		if (expectedLanguage != null && !expectedLanguage.isEmpty()) {
			int maxNumber;
			try {
				maxNumber = checkCode.getFunctionNumber().isEmpty() ? 4 : Integer.parseInt(checkCode.getFunctionNumber());
			} catch (NumberFormatException nfe) {
				maxNumber = 4;
			}
			final ExtractTextHandler extractTextHandler = new ExtractTextHandler("en", false, new String[] { "abbr", "acronym" });
			try {
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new SAXResult(extractTextHandler));
				final String extractedText = extractTextHandler.getExtractedText();
				final String[] words = extractedText.toLowerCase().split("\\s+");
				final List<String> enWords = new ArrayList<>();
				for (String word : words) {
					if (Diccionario.containsWord("en", word) && !enWords.contains(word)) {
						enWords.add(word);
					}
				}
				document.setUserData("en_words", enWords, null);
				return enWords.size() > maxNumber;
			} catch (TransformerException e) {
				Logger.putLog("Error al intentar extraer el texto en functionOtherLanguage", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Function current language.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCurrentLanguage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String expectedLanguage = checkCode.getFunctionValue();
		Element startElement = elementGiven;
		// Si el enlace tiene un único hijo asignamos ese hijo como punto de
		// comienzo
		if (elementGiven.getChildNodes().getLength() == 1 && elementGiven.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
			startElement = (Element) elementGiven.getFirstChild();
		}
		String language = getLanguage(startElement, false);
		if (language != null) {
			// Desde el punto de comienzo y recorriendo hacia el nodo padre
			// paramos en el primer nodo que defina un idioma
			// si recorremos el arbol DOM completo y no se definió entonces
			// language es vacio
			Node parent = elementGiven.getParentNode();
			while (parent != null && language.isEmpty()) {
				if (parent.getNodeType() == Node.ELEMENT_NODE) {
					language = getLanguage((Element) parent, false);
				}
				parent = parent.getParentNode();
			}
			return !language.startsWith(expectedLanguage);
		} else {
			return true;
		}
	}

	/**
	 * Function has validation errors.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasValidationErrors(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		final List<ValidationError> vectorValidationErrors = (List<ValidationError>) elementRoot.getUserData("validationErrors");
		// Si indicamos un número en el parámetro number indica el número de
		// errores permitidos, si no se indica se considera 0 (no se permite
		// ningún error)
		final int maxErrors = checkCode.getFunctionNumber().isEmpty() ? 0 : Integer.parseInt(checkCode.getFunctionNumber());
		final String messageId = checkCode.getFunctionValue();
		if (messageId.isEmpty()) {
			return vectorValidationErrors == null || vectorValidationErrors.size() <= maxErrors;
		} else {
			int count = 0;
			for (ValidationError validationError : vectorValidationErrors) {
				if (messageId.equals(validationError.getMessageId())) {
					validationError.setMessageId(String.valueOf(id));
					count++;
				}
			}
			// Si el número de errores es mayor que el número de errores
			// permitidos entonces es fallo (true)
			return count > maxErrors;
		}
	}

	/**
	 * Function redundant img alt.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionRedundantImgAlt(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList nodeList = elementGiven.getElementsByTagName("img");
		final String linkText = StringUtils.normalizeWhiteSpaces(EvaluatorUtility.getElementText(elementGiven)).trim();
		if (nodeList.getLength() != 0 && !linkText.trim().isEmpty()) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				final String altText = ((Element) nodeList.item(i)).getAttribute("alt").trim();
				if (!altText.isEmpty() && altText.equalsIgnoreCase(linkText)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function img dimensions less than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionImgDimensionsLessThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode != null) {
			// get the width and height of the image
			final int width = extractImageDimension(((Element) nodeNode).getAttribute("width"));
			final int height = extractImageDimension(((Element) nodeNode).getAttribute("height"));
			final Dimension dimension;
			// Si no existen ambas dimensiones cargar la imagen
			if (width == -1 && height == -1) {
				final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
				dimension = loadImage(elementRoot, elementGiven.getAttribute("src"));
			} else {
				dimension = new Dimension(width, height);
			}
			if (dimension != null) {
				final String imageWidth = checkCode.getFunctionAttribute1();
				final String imageHeight = checkCode.getFunctionAttribute2();
				if (!imageWidth.isEmpty() || !imageHeight.isEmpty()) {
					boolean dimensionsLessThan = false;
					if (!imageWidth.isEmpty() && dimension.getWidth() != -1) {
						dimensionsLessThan |= dimension.width < Integer.valueOf(imageWidth);
					}
					if (!imageHeight.isEmpty() && dimension.getHeight() != -1) {
						dimensionsLessThan |= dimension.height < Integer.valueOf(imageHeight);
					}
					return dimensionsLessThan;
				} else {
					// Error no se ha proporcionado el límite de ancho o alto
					return false;
				}
			}
		}
		// Si no tenemos las dimensiones de la imagen no se produce ningún
		// problema
		return false;
	}

	/**
	 * Load image.
	 *
	 * @param elementRoot the element root
	 * @param srcImg      the src img
	 * @return the dimension
	 */
	private Dimension loadImage(final Element elementRoot, final String srcImg) {
		try {
			final String baseUrl = CheckUtils.getBaseUrl(elementRoot);
			final URL url = baseUrl != null ? new URL(baseUrl) : new URL((String) elementRoot.getUserData("url"));
			final URL urlImage = new URL(url, srcImg);
			final BufferedImage image = ImageIO.read(urlImage);
			if (image != null) {
				return new Dimension(image.getWidth(), image.getHeight());
			} else {
				Logger.putLog("Unknown image format: " + urlImage, CheckerParser.class, Logger.LOG_LEVEL_INFO);
				return null;
			}
		} catch (IOException e) {
			Logger.putLog("Exception loading image", CheckerParser.class, Logger.LOG_LEVEL_INFO, e);
			return null;
		} catch (Throwable t) {
			Logger.putLog(String.format("Throwable %s", t.getMessage()), CheckerParser.class, Logger.LOG_LEVEL_INFO);
			return null;
		}
	}

	/**
	 * Extract image dimension.
	 *
	 * @param value the value
	 * @return the int
	 */
	private int extractImageDimension(final String value) {
		if (value != null && !value.isEmpty()) {
			try {
				return Integer.parseInt(value.trim());
			} catch (NumberFormatException e) {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Function only one child.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionOnlyOneChild(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return elementGiven.getElementsByTagName(checkCode.getFunctionAttribute1()).getLength() == 1;
	}

	/**
	 * Function more than one child element.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionMoreThanOneChildElement(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getElementsByTagName(checkCode.getFunctionElement());
		for (int i = 0; i < nodeList.getLength(); i++) {
			int numFirstElement = ((Element) nodeList.item(i)).getElementsByTagName(checkCode.getFunctionAttribute1()).getLength();
			int numSecondElement = ((Element) nodeList.item(i)).getElementsByTagName(checkCode.getFunctionAttribute2()).getLength();
			if (numFirstElement + numSecondElement > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Count nodes with text.
	 *
	 * @param nodeList the node list
	 * @return the int
	 */
	private int countNodesWithText(NodeList nodeList) {
		int cellsWithText = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			if ((node.getTextContent() != null)
					&& (StringUtils.isNotEmpty(node.getTextContent()) && (!StringUtils.isOnlyBlanks(node.getTextContent()) && (!StringUtils.isOnlyWhiteChars(node.getTextContent()))))) {
				cellsWithText++;
			} else if (((Element) node).getElementsByTagName("img") != null) {
				NodeList imgList = ((Element) node).getElementsByTagName("img");
				for (int j = 0; j < imgList.getLength(); j++) {
					Node item = imgList.item(j);
					if (cellWithText(item)) {
						cellsWithText++;
						break;
					}
				}
			} else if (cellWithText(node)) {
				cellsWithText++;
				break;
			}
		}
		return cellsWithText;
	}

	/**
	 * Comprueba si un nodo tiene atributos alt, title, aria-label o aria-describedby correctos.
	 * 
	 * @param item Nodo.
	 * @return true si alguno de los atributos mencionados tiene valor, false en caso contrario.
	 */
	private boolean cellWithText(Node item) {
		String alt = ((Element) item).getAttribute("alt");
		String title = ((Element) item).getAttribute("title");
		String ariaLabel = ((Element) item).getAttribute("aria-label");
		String ariaDescribedby = ((Element) item).getAttribute("aria-describedby");
		if (alt != null && StringUtils.isNotEmpty(alt) && !StringUtils.isOnlyBlanks(alt) && !StringUtils.isOnlyWhiteChars(alt)) {
			return true;
		} else if (title != null && StringUtils.isNotEmpty(title) && !StringUtils.isOnlyBlanks(title) && !StringUtils.isOnlyWhiteChars(title)) {
			return true;
		} else if (ariaLabel != null && StringUtils.isNotEmpty(ariaLabel) && !StringUtils.isOnlyBlanks(ariaLabel) && !StringUtils.isOnlyWhiteChars(ariaLabel)) {
			return true;
		} else if (ariaDescribedby != null && StringUtils.isNotEmpty(ariaDescribedby) && !StringUtils.isOnlyBlanks(ariaDescribedby) && !StringUtils.isOnlyWhiteChars(ariaDescribedby)) {
			return true;
		}
		return false;
	}

	/**
	 * Function text cells percentage greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextCellsPercentageGreaterThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		BigDecimal totalCells = BigDecimal.ZERO;
		BigDecimal cellsWithText = BigDecimal.ZERO;
		if (checkCode.getFunctionAttribute1() != null && !StringUtils.isEmpty(checkCode.getFunctionAttribute1())) {
			NodeList nodeList = elementGiven.getElementsByTagName(checkCode.getFunctionAttribute1());
			totalCells = totalCells.add(new BigDecimal(nodeList.getLength()));
			cellsWithText = cellsWithText.add(new BigDecimal(countNodesWithText(nodeList)));
		}
		if (checkCode.getFunctionAttribute2() != null && !StringUtils.isEmpty(checkCode.getFunctionAttribute2())) {
			NodeList nodeList = elementGiven.getElementsByTagName(checkCode.getFunctionAttribute2());
			totalCells = totalCells.add(new BigDecimal(nodeList.getLength()));
			cellsWithText = cellsWithText.add(new BigDecimal(countNodesWithText(nodeList)));
		}
		return cellsWithText.divide(totalCells, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).compareTo(new BigDecimal(checkCode.getFunctionNumber())) >= 0;
	}

	/**
	 * Function empty table.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	protected boolean functionEmptyTable(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		checkCode.setFunctionNumber("30");
		checkCode.setFunctionAttribute1("td");
		checkCode.setFunctionAttribute2("th");
		if (!functionTextCellsPercentageGreaterThan(checkCode, nodeNode, elementGiven)) {
			return true;
		}
		return false;
	}

	/**
	 * Function layout table.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si una tabla es o no de maquetacion
	protected boolean functionLayoutTable(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		checkCode.setFunctionElement("table");
		String maxLength;
		try {
			Integer.parseInt(checkCode.getFunctionAttribute1());
			maxLength = checkCode.getFunctionAttribute1();
		} catch (NumberFormatException nfe) {
			maxLength = "400";
		}
		if (functionContains(checkCode, nodeNode, elementGiven)) {
			// Una tabla que contiene otra tabla
			return true;
		} else if ("presentation".equalsIgnoreCase(elementGiven.getAttribute("role"))) {
			/*
			 * Añadido sobre la comprobacion original de inteco ¿duplicar la comprobacion?
			 */
			return true;
		} else {
			checkCode.setFunctionAttribute1("tr");
			if (functionOnlyOneChild(checkCode, nodeNode, elementGiven)) {
				// Tabla con una sola fila (Si solo tiene una celda ya falla
				// este o el siguiente)
				return true;
			} else {
				checkCode.setFunctionElement("tr");
				checkCode.setFunctionAttribute1("th");
				checkCode.setFunctionAttribute2("td");
				if (!functionMoreThanOneChildElement(checkCode, nodeNode, elementGiven)) {
					// Tabla con una sola columna
					return true;
				} else {
					checkCode.setFunctionNumber("70");
					checkCode.setFunctionAttribute1("td");
					checkCode.setFunctionAttribute2("th");
					if (!functionTextCellsPercentageGreaterThan(checkCode, nodeNode, elementGiven)) {
						// Tabla con menos del 70% de celdas con texto
						return true;
					} else {
						checkCode.setFunctionElement("td");
						checkCode.setFunctionValue(maxLength);
						if (functionChildElementCharactersGreaterThan(checkCode, nodeNode, elementGiven)) {
							// Tabla con tds que contienen mas de 400 caracteres
							return true;
						} else {
							checkCode.setFunctionElement("th");
							checkCode.setFunctionValue(maxLength);
							if (functionChildElementCharactersGreaterThan(checkCode, nodeNode, elementGiven)) {
								// Tabla con ths que contienen mas de 400
								// caracteres
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Function layout table number.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si hay mas de x tablas de maquetación en la página
	protected boolean functionLayoutTableNumber(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getElementsByTagName("table");
		int count = 0;
		String numCode = checkCode.getFunctionNumber();
		for (int i = 0; i < nodeList.getLength(); i++) {
			checkCode.setFunctionElement("table");
			// checkCode.setFunctionNumber(numCode);
			if (functionLayoutTable(checkCode, nodeNode, (Element) nodeList.item(i))) {
				count++;
			}
		}
		return count > Integer.parseInt(numCode);
	}

	/**
	 * Function has language.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento tiene código de lenguaje
	private boolean functionHasLanguage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		String stringLanguage = null;
		if (!StringUtils.isEmpty(checkCode.getFunctionAttribute1()) && checkCode.getFunctionAttribute1().equals("noSevere")) {
			stringLanguage = getLanguage(elementGiven, false);
		} else {
			stringLanguage = getLanguage(elementGiven, true);
		}
		return stringLanguage == null || stringLanguage.length() == 0;
	}

	/**
	 * Function language not equals.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionLanguageNotEquals(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean result = !functionLanguageEquals(checkCode, nodeNode, elementGiven);
		if (result && this.id == 22 && checkCode.getFunctionValue().equals("en")) {
			String url = (String) elementGiven.getOwnerDocument().getDocumentElement().getUserData("url");
			Logger.putLog("La página " + url + " no tiene idioma español ni inglés, así que no se analizará el lenguaje claro y sencillo", Check.class, Logger.LOG_LEVEL_INFO);
		}
		return result;
	}

	/**
	 * Function empty elements.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionEmptyElements(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodes = elementGiven.getElementsByTagName(checkCode.getFunctionElement());
		int maxNumber = Integer.parseInt(checkCode.getFunctionNumber());
		int counter = 0;
		for (int i = 0; i < nodes.getLength(); i++) {
			if (functionIsOnlyBlanks(checkCode, nodes.item(i), elementGiven) || countCharacters(nodeNode, true) < 0) {
				counter++;
			}
		}
		return counter > maxNumber;
	}

	/**
	 * Function elements usage.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionElementsUsage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final List<String> elementsList = Arrays.asList(checkCode.getFunctionElement().split(";"));
		final int limit = Integer.parseInt(checkCode.getFunctionNumber());
		final String compare = checkCode.getFunctionPosition().isEmpty() ? "greater" : checkCode.getFunctionPosition();
		int counter = 0;
		for (String element : elementsList) {
			counter += elementGiven.getElementsByTagName(element).getLength();
			// Si viene informado attribute1, tenemos que excluir del
			// total las etiqietas que estén dentro de estas exclusiones
			if (!StringUtils.isEmpty(checkCode.getFunctionAttribute1())) {
				final List<String> elementsExclude = Arrays.asList(checkCode.getFunctionAttribute1().split(";"));
				for (String exclude : elementsExclude) {
					NodeList excludeNodes = elementGiven.getElementsByTagName(exclude);
					for (int i = 0; i < excludeNodes.getLength(); i++) {
						Node tmp = excludeNodes.item(i);
						if (Node.ELEMENT_NODE == tmp.getNodeType()) {
							counter -= ((Element) tmp).getElementsByTagName(element).getLength();
						}
					}
				}
			}
		}
		if ("greater".equalsIgnoreCase(compare)) {
			// Si la comparación es mayor damos un error si el número de
			// elementos es mayor que el valor indicado
			return counter > limit;
		} else {
			// Si la comparación es menor damos un error si el número de
			// elementos es menor que el valor indicado
			return limit > counter;
		}
	}

	/**
	 * Function attributes excessive usage.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributesExcessiveUsage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		List<String> attributesList = Arrays.asList(checkCode.getFunctionElement().split(";"));
		int maxNumber = Integer.parseInt(checkCode.getFunctionNumber());
		String source = (String) elementGiven.getOwnerDocument().getDocumentElement().getUserData("source");
		PropertiesManager pmgr = new PropertiesManager();
		int counter = 0;
		for (String attribute : attributesList) {
			Pattern pattern = Pattern.compile(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "attributes.reg.exp.matcher").replace("ATTRIBUTE", attribute), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher matcher = pattern.matcher(source);
			while (matcher.find()) {
				counter++;
			}
		}
		return counter > maxNumber;
	}

	/**
	 * Function tab index excessive usage.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTabIndexExcessiveUsage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final int maxNumber = Integer.parseInt(checkCode.getFunctionNumber());
		int counter = 0;
		final NodeList nodeList = elementGiven.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				final Element element = (Element) node;
				if (element.hasAttribute("tabindex")) {
					try {
						final int tabindex = Integer.parseInt(element.getAttribute("tabindex").trim());
						if (tabindex > 0) {
							counter++;
						}
					} catch (NumberFormatException nfe) {
					}
				}
			}
		}
		return counter > maxNumber;
	}

	/**
	 * Function language equals.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el lenguaje del elemento es igual al que se le pasa
	private boolean functionLanguageEquals(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String stringLanguage;
		if (!StringUtils.isEmpty(checkCode.getFunctionAttribute1()) && checkCode.getFunctionAttribute1().equals("noSevere")) {
			stringLanguage = getLanguage(elementGiven, false);
		} else {
			stringLanguage = getLanguage(elementGiven, true);
		}
		return StringUtils.isNotEmpty(stringLanguage) && stringLanguage.startsWith(checkCode.getFunctionValue());
	}

	/**
	 * Function object has not alternative.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento object tiene alternativa
	private boolean functionObjectHasNotAlternative(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionObjectHasAlternative(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function applet has not alternative.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento applet tiene alternativa
	private boolean functionAppletHasNotAlternative(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionAppletHasAlternative(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function object has alternative.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento object tiene alternativa
	private boolean functionObjectHasAlternative(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getChildNodes();
		if (nodeList != null) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeName().equals("#text")) {
					if (elementGiven.getTextContent() != null && !elementGiven.getTextContent().trim().equals("")) {
						return true;
					}
				} else if (!nodeList.item(i).getNodeName().equalsIgnoreCase("param")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function applet has alternative.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento applet tiene alternativa
	private boolean functionAppletHasAlternative(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (!elementGiven.getAttribute("alt").isEmpty()) {
			return true;
		} else {
			NodeList nodeList = elementGiven.getChildNodes();
			if (nodeList != null) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					if (nodeList.item(i).getNodeName().equals("#text")) {
						if (elementGiven.getTextContent() != null && !elementGiven.getTextContent().trim().equals("")) {
							return true;
						}
					} else if (!nodeList.item(i).getNodeName().equalsIgnoreCase("param")) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Function I frame has not alternative.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento iframe tiene alternativa
	private boolean functionIFrameHasNotAlternative(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionIFrameHasAlternative(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function I frame has alternative.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento iframe tiene alternativa
	private boolean functionIFrameHasAlternative(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getChildNodes();
		if (nodeList != null) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
					if (elementGiven.getTextContent() != null && !elementGiven.getTextContent().trim().equals("")) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function not valid language.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el elemento tiene código de lenguaje válido
	private boolean functionNotValidLanguage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final List<String> languageList = getLanguageList(elementGiven);
		for (String language : languageList) {
			if (!EvaluatorUtility.isLanguageCode(language)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the language list.
	 *
	 * @param element the element
	 * @return the language list
	 */
	private List<String> getLanguageList(Element element) {
		final List<String> languages = new ArrayList<>();
		// is doc HTML or XHTML? and which version?
		final Element elementRoot = element.getOwnerDocument().getDocumentElement();
		final String hasDoctype = (String) elementRoot.getUserData("doctype");
		if (hasDoctype.equals(IntavConstants.FALSE)) {
			// no doctype so assume it's HTML
			if (element.hasAttribute("lang")) {
				languages.add(element.getAttribute("lang"));
			}
		} else { // has doctype (html/xhtml type and version in parser)
			final String doctypeType = (String) elementRoot.getUserData("doctypeType");
			if ((doctypeType != null) && (doctypeType.equals("xhtml"))) {
				// an XHTML page
				if (element.hasAttribute("xml:lang")) {
					languages.add(element.getAttribute("xml:lang"));
				}
				if (element.hasAttribute("lang")) {
					languages.add(element.getAttribute("lang"));
				}
			} else { // it's an HTML page
				if (element.hasAttribute("lang")) {
					languages.add(element.getAttribute("lang"));
				}
			}
		}
		return languages;
	}

	// Devuelve el lenguaje de un elemento. Si está en modo severo, para xhtml
	// devolverá el atributo xml:lang
	/**
	 * Gets the language.
	 *
	 * @param elementHtml the element html
	 * @param severe      the severe
	 * @return the language
	 */
	// Si está en modo no severo, siempre devolverá el atributo lang.
	private String getLanguage(Element elementHtml, boolean severe) {
		final Element elementRoot = elementHtml.getOwnerDocument().getDocumentElement();
		// is doc HTML or XHTML? and which version?
		final String hasDoctype = (String) elementRoot.getUserData("doctype");
		if (hasDoctype.equals(IntavConstants.FALSE)) {
			// no doctype so assume it's HTML
			return elementHtml.hasAttribute("lang") ? elementHtml.getAttribute("lang") : elementHtml.getAttribute("xml:lang");
		} else { // has doctype (html/xhtml type and version in parser)
			String doctypeType = (String) elementRoot.getUserData("doctypeType");
			if (doctypeType != null && doctypeType.equals("html")) {
				// Html solo tiene que tener "lang" pero dejamos tambien
				// xml:lang como 'fallback'
				return elementHtml.hasAttribute("lang") ? elementHtml.getAttribute("lang") : elementHtml.getAttribute("xml:lang");
			} else if ((doctypeType != null) && doctypeType.equals("xhtml")) {
				String doctypeTypeVersion = (String) elementRoot.getUserData("doctypeVersion");
				if (doctypeTypeVersion != null && doctypeTypeVersion.equals("1.0")) {
					if (severe) {
						// XHTML 1.0 en modo severo, debe de tener ambos
						// atributos
						if (StringUtils.isNotEmpty(elementHtml.getAttribute("lang")) && StringUtils.isNotEmpty(elementHtml.getAttribute("xml:lang"))) {
							return elementHtml.getAttribute("lang");
						}
					} else {
						// XHTML 1.0 en modo no severo, solo hace falta que
						// tenga uno de los atributos
						if (elementHtml.hasAttribute("lang")) {
							return elementHtml.getAttribute("lang");
						} else if (elementHtml.hasAttribute("xml:lang")) {
							return elementHtml.getAttribute("xml:lang");
						}
					}
				} else {
					// XHTML > 1.0 en modo no severo, tiene que tener xml:lang
					return elementHtml.getAttribute("xml:lang");
				}
			}
		}
		return null;
	}

	// Note: The actual check for missing noscript happens in the parser so that
	// it can be
	/**
	 * Function noscript missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// done before the DOM structure is created.
	private boolean functionNoscriptMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String stringNoscriptStatus = (String) elementGiven.getUserData("noscript");
		if (stringNoscriptStatus == null) {
			return true;
		}
		if (stringNoscriptStatus.equals(IntavConstants.TRUE)) {
			return false;
		}
		return true;
	}

	// Note: The actual check for missing noscript happens in the parser so that
	// it can be
	/**
	 * Function noframe missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// done before the DOM structure is created.
	private boolean functionNoframeMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList noframes = elementGiven.getOwnerDocument().getElementsByTagName("noframes");
		if (noframes != null && noframes.getLength() > 0) {
			for (int i = 0; i < noframes.getLength(); i++) {
				Element noframe = (Element) noframes.item(i);
				for (int j = 0; j < noframe.getChildNodes().getLength(); j++) {
					Node noframeChild = noframe.getChildNodes().item(j);
					if (StringUtils.isNotEmpty(EvaluatorUtils.serializeXmlElement(noframeChild).trim())) {
						return false;
					}
				}
			}
		}
		return true;
	}

	// Note: Part of the checking for missing noembed happens in the parser so
	// that it
	/**
	 * Function noembed missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// can be done before the DOM structure is created.
	private boolean functionNoembedMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// is there a noembed following the embed?
		String stringNoembedStatus = (String) elementGiven.getUserData("noembed");
		if ((stringNoembedStatus != null) && (stringNoembedStatus.equals(IntavConstants.TRUE))) {
			return false; // noembed follows the embed so no error
		}
		// is there a noembed within the embed?
		NodeList listNoembeds = elementGiven.getElementsByTagName("noembed");
		return listNoembeds.getLength() == 0;
	}

	/**
	 * Function attribute null.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributeNull(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		String stringValue = nodeNode.getNodeValue();
		return stringValue.length() == 0;
	}

	// Comprueba si hay dos o mas encabezados del mismo nivel con el mismo texto
	/**
	 * Function duplicate following headers.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// seguidos
	private boolean functionDuplicateFollowingHeaders(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getOwnerDocument().getElementsByTagName(elementGiven.getNodeName());
		for (int j = 0; j < nodeList.getLength(); j++) {
			Element node1 = (Element) nodeList.item(j);
			if (node1 != elementGiven) {
				if (elementGiven.getUserData(IntavConstants.PREVIOUS_LEVEL) != null) {
					final String previousHeaderLevel = getPreviousLevelHeaderNode(elementGiven.getNodeName());
					// buscamos el "padre" de cada elemento a comparar
					final Node previousElementGivenNode = getPreviousLevelNode(elementGiven, previousHeaderLevel);
					final Node previousNode1Node = getPreviousLevelNode(node1, previousHeaderLevel);
					// Miramos si es el mismo
					if ((previousElementGivenNode != null) && (previousNode1Node != null) && (previousElementGivenNode == previousNode1Node)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function following headers without content.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionFollowingHeadersWithoutContent(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// Si no hay contenido entre dos headers
		if (elementGiven.getUserData("headerHasContents") == null) {
			if (elementGiven.getUserData(IntavConstants.NEXT_LEVEL) != null) {
				final int thisLevel = Integer.parseInt(elementGiven.getNodeName().substring(1));
				final int nextLevel = (Integer) elementGiven.getUserData(IntavConstants.NEXT_LEVEL);
				return thisLevel >= nextLevel;
			} else {
				return true;
			}
		} else {
			// Hay contenido
			return false;
		}
	}

	/**
	 * Gets the previous level sibling node.
	 *
	 * @param nodeGiven           the node given
	 * @param previousHeaderLevel the previous header level
	 * @return the previous level sibling node
	 */
	private Node getPreviousLevelSiblingNode(Node nodeGiven, String previousHeaderLevel) {
		// Buscamos el header de nivel anterior en los hermanos
		Node nodeSibling = nodeGiven.getPreviousSibling();
		while (nodeSibling != null) {
			if (nodeSibling.getNodeName().equalsIgnoreCase(previousHeaderLevel)) {
				return nodeSibling;
			} else {
				nodeSibling = nodeSibling.getPreviousSibling();
			}
		}
		return null;
	}

	/**
	 * Gets the previous level node.
	 *
	 * @param elementGiven        the element given
	 * @param previousHeaderLevel the previous header level
	 * @return the previous level node
	 */
	private Node getPreviousLevelNode(Element elementGiven, String previousHeaderLevel) {
		// Comprobamos los hermanos del nodo dado
		final Node node = getPreviousLevelSiblingNode(elementGiven, previousHeaderLevel);
		if (node != null) {
			return node;
		} else {
			// Si no encontramos el nivel anterior en los hermanos del nodo
			// miramos los hermanos del padre
			Node nodeParent = elementGiven.getParentNode();
			while (nodeParent != null) {
				if (getPreviousLevelSiblingNode(nodeParent, previousHeaderLevel) != null) {
					return nodeParent;
				} else {
					nodeParent = nodeParent.getParentNode();
				}
			}
		}
		return null;
	}

	// Note: I set the previous header level in the parser so that it can be
	/**
	 * Function next heading wrong.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// done before the DOM structure is created.
	private boolean functionNextHeadingWrong(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		try {
			String stringHeading = elementGiven.getNodeName().trim();
			int thisHeading = Integer.parseInt(stringHeading.substring(1));
			int nextHeading = (Integer) elementGiven.getUserData(IntavConstants.NEXT_LEVEL);
			if (nextHeading == 0) { // no next heading
				return false;
			}
			if (nextHeading > (thisHeading + 1)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	// Note: I set the previous header level in the parser so that it can be
	/**
	 * Function previous heading wrong.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// done before the DOM structure is created.
	private boolean functionPreviousHeadingWrong(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		try {
			final int previousHeading = (Integer) elementGiven.getUserData(IntavConstants.PREVIOUS_LEVEL);
			// if no previous heading return false
			return previousHeading != 0 && CheckUtils.compareHeadingsLevel((Element) elementGiven.getUserData("prevheader"), elementGiven) > 1;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Function incorrect header structure.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba que la estructura de encabezados del documento sea correcta
	private boolean functionIncorrectHeaderStructure(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean headerNestingIncorrect = false;
		// Para todos los niveles de encabezados y mientras la estructura no sea
		// incorrecta
		for (int i = 1; i < 7 && !headerNestingIncorrect; i++) {
			// Obtenemos los encabezados
			final NodeList headers = elementGiven.getElementsByTagName("h" + i);
			if (headers != null && headers.getLength() > 0) {
				for (int j = 0; j < headers.getLength() && !headerNestingIncorrect; j++) {
					// Comprobamos si mantiene orden respecto al anterior
					headerNestingIncorrect |= functionPreviousHeadingWrong(checkCode, nodeNode, (Element) headers.item(j));
				}
			}
		}
		return headerNestingIncorrect;
	}

	/**
	 * Gets the previous level header node.
	 *
	 * @param header the header
	 * @return the previous level header node
	 */
	private String getPreviousLevelHeaderNode(String header) {
		int headerNum = Integer.parseInt(header.toLowerCase().replace("h", "")) - 1;
		return "h" + headerNum;
	}

	/**
	 * Function no correct document structure.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba que la estructura de encabezados del documento no sea correcta
	private boolean functionNoCorrectDocumentStructure(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionIncorrectHeaderStructure(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function headers missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el documento tiene o no encabezados
	private boolean functionHeadersMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		for (int i = 1; i < 7; i++) {
			if (!EvaluatorUtils.getElementsByTagName(elementGiven, "h" + i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Function headers exist.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Comprueba si el documento tiene o no encabezados
	private boolean functionHeadersExist(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		for (int i = 1; i < 7; i++) {
			final NodeList headers = elementGiven.getElementsByTagName("h" + i);
			if (headers != null && headers.getLength() > 0) {
				return false;
			}
		}
		return true;
	}

	// Note: The actual check for missing d-link happens in the parser so that
	// it can be
	/**
	 * Function D link missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// done before the DOM structure is created.
	private boolean functionDLinkMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String stringDlinkStatus = (String) elementGiven.getUserData("dlink");
		if (stringDlinkStatus == null) {
			return true;
		}
		return !stringDlinkStatus.equals(IntavConstants.TRUE);
	}

	/**
	 * Function label not associated.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if there is no label associated with the control.
	private boolean functionLabelNotAssociated(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// check if the control has a title attribute
		if (elementGiven.hasAttribute("title") && !StringUtils.normalizeWhiteSpaces(elementGiven.getAttribute("title")).trim().isEmpty()) {
			return false;
		}
		if (elementGiven.hasAttribute("aria-label") && !StringUtils.normalizeWhiteSpaces(elementGiven.getAttribute("aria-label")).trim().isEmpty()) {
			return false;
		}
		// Correción para tener en cuenta que puede haber varios ids en el
		// atributo
		if (elementGiven.hasAttribute("aria-labelledby")) {
			String[] ids = elementGiven.getAttribute("aria-labelledby").split("\\s");
			for (String id : ids) {
				Element labelledBy = elementGiven.getOwnerDocument().getElementById(id);
				if (labelledBy != null && !StringUtils.normalizeWhiteSpaces(labelledBy.getTextContent()).trim().isEmpty()) {
					return false;
				}
			}
		}
		// check if the input element is contained by a label element
		Element elementParent = DOMUtil.getParent(elementGiven);
		while (elementParent != null) {
			if ("label".equalsIgnoreCase(elementParent.getNodeName())) {
				return false;
			}
			elementParent = DOMUtil.getParent(elementParent);
		}
		// check if the control has an associated label using 'for' and 'id'
		// attributes
		// get the 'id' attribute of the control
		final String stringId = elementGiven.getAttribute("id");
		if (stringId.length() == 0) {
			// control has no 'id' attribute so can't have an associated label
			return true;
		}
		final Document document = elementGiven.getOwnerDocument();
		if (document != null) {
			final NodeList listLabels = document.getElementsByTagName("label");
			// look for a label that has a 'for' attribute value matching the
			// control's id
			int cont = 0;
			for (int x = 0; x < listLabels.getLength(); x++) {
				final Element element = (Element) listLabels.item(x);
				if (element.getAttribute("for").equalsIgnoreCase(stringId) && !StringUtils.normalizeWhiteSpaces(element.getTextContent()).trim().isEmpty()) {
					// found an associated label
					cont++;
				}
			}
			// i es cero devolvemos true porque no hay label, si hay
			// más de una está bien
			return cont == 0;
		} else {
			return false;
		}
	}

	/**
	 * Function label no text.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if label associated with control has no text.
	private boolean functionLabelNoText(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// check if the control has a title attribute
		if (elementGiven.hasAttribute("title")) {
			// check if the title contains text
			final String stringTitle = elementGiven.getAttribute("title");
			if (stringTitle.trim().length() > 0) {
				return false;
			}
		}
		// check if the input element is contained by a label element
		Element elementParent = DOMUtil.getParent(elementGiven);
		while (elementParent != null) {
			if ("label".equalsIgnoreCase(elementParent.getNodeName())) {
				final String stringLabelText = EvaluatorUtility.getLabelText(elementParent);
				if (stringLabelText.length() > 0) {
					return false;
				}
				break;
			}
			elementParent = DOMUtil.getParent(elementParent);
		}
		// check if the control has an associated label using 'for' and 'id'
		// attributes
		// get the 'id' attribute of the control
		final String stringId = elementGiven.getAttribute("id");
		if (stringId.length() == 0) {
			// control has no 'id' attribute so can't have an associated label
			return true;
		}
		// get a list of label elements in the document
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		NodeList listLabels = elementRoot.getElementsByTagName("label");
		// look for a label that has a 'for' attribute value matching the
		// control's id
		for (int x = 0; x < listLabels.getLength(); x++) {
			if (((Element) listLabels.item(x)).getAttribute("for").equalsIgnoreCase(stringId)) {
				// found an associated label, get its text
				Element elementLabel = (Element) listLabels.item(x);
				String stringLabelText = EvaluatorUtility.getElementText(elementLabel);
				if (stringLabelText.length() > 0) {
					return false;
				}
				// no text in label, check if label contains an image with alt
				// text
				NodeList listImages = elementLabel.getElementsByTagName("img");
				for (int y = 0; y < listImages.getLength(); y++) {
					Element elementImage = (Element) listImages.item(y);
					if (elementImage.hasAttribute("alt")) {
						String stringAlt = elementImage.getAttribute("alt").trim();
						if (stringAlt.length() > 0) {
							return false; // label contains an image with alt
											// text
						}
					}
				}
				break;
			}
		}
		return true;
	}

	/**
	 * Function text link equiv missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextLinkEquivMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// find the map that corresponds to this image
		// first, get the name of the map
		String usemapName = elementGiven.getAttribute("usemap");
		if (usemapName != null) {
			usemapName = usemapName.substring(1, usemapName.length());
		}
		// get all the 'map' elements
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		NodeList listMaps = elementRoot.getElementsByTagName("map");
		for (int x = 0; x < listMaps.getLength(); x++) {
			if (((Element) listMaps.item(x)).getAttribute("name").equalsIgnoreCase(usemapName)) {
				// found the map, now get all the area elements
				NodeList listAreas = ((Element) listMaps.item(x)).getElementsByTagName("area");
				// get all the 'a' elements in the document
				NodeList listAs = elementRoot.getElementsByTagName("a");
				// are there 'a' elements for each 'area'?
				for (int y = 0; y < listAreas.getLength(); y++) {
					String hrefArea = ((Element) listAreas.item(y)).getAttribute("href");
					boolean foundIt = false;
					for (int z = 0; z < listAs.getLength(); z++) {
						String hrefA = ((Element) listAs.item(z)).getAttribute("href");
						if (hrefArea.equalsIgnoreCase(hrefA)) {
							foundIt = true;
							break;
						}
					}
					if (!foundIt) {
						return true;
					}
				}
				break;
			}
		}
		return false;
	}

	/**
	 * Function number any.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNumberAny(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		String string = "";
		if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
			string = EvaluatorUtility.getElementText(nodeNode);
		} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			string = nodeNode.getNodeValue().trim();
		}
		// ignore anything after the semicolon
		int indexSemicolon = string.indexOf(';');
		if (indexSemicolon != -1) {
			string = string.substring(0, indexSemicolon);
		}
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {
			Logger.putLog("Exception: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return false;
	}

	/**
	 * Function definition list construction.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionDefinitionListConstruction(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getChildNodes();
		List<String> exceptions = Collections.emptyList();
		if (checkCode.getFunctionValue() != null && !checkCode.getFunctionValue().equals("")) {
			exceptions = Arrays.asList(checkCode.getFunctionValue().split(";"));
		}
		boolean isDt = false;
		boolean dtHasDd = false;
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					// divs are allowed
//					if (nodeList.item(i).getNodeName().equalsIgnoreCase("div")) {
//						// TODO Recursively??
//						// continue;
//						return functionDefinitionListConstruction(checkCode, null, (Element) nodeList.item(i));
//					} else
					if (nodeList.item(i).getNodeName().equalsIgnoreCase("dd")) {
						if (!isDt) {
							if (!dtHasDd) {
								return true; // Si el primer elemento es dd
												// Error
							} // Si dtHasDd es true y isDt false es porque son
								// dd seguidos, OK
						} else { // Si dt es igual a true y nos encontramos un
									// dd, OK
							isDt = false;
							dtHasDd = true;
						}
					} else if (nodeList.item(i).getNodeName().equalsIgnoreCase("dt")) {
						if (!isDt) { // Si isDt es false es que el anterior es
										// un dd
							isDt = true;
							dtHasDd = false;
						}
						// else -> dos dt seguidos se permiten
					} else if (!exceptions.contains(nodeList.item(i).getNodeName().toLowerCase())) {
						return true;
					}
				}
			}
			if (isDt) { // Si acaba en Dt, Error
				return true;
			}
		} else { // Si no tiene nodos, Error
			return true;
		}
		return false;
	}

	/**
	 * Function all elements not like this.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAllElementsNotLikeThis(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getChildNodes();
		List<String> exceptions = Collections.emptyList();
		if (checkCode.getFunctionValue() != null && !checkCode.getFunctionValue().equals("")) {
			exceptions = Arrays.asList(checkCode.getFunctionValue().split(";"));
		}
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 1; i < nodeList.getLength(); i++) {
				final Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (!node.getNodeName().equalsIgnoreCase(checkCode.getFunctionElement())) {
						if (!exceptions.contains(node.getNodeName().toLowerCase())) {
							return true;
						}
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}

	/**
	 * Function container.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionContainer(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		if (nodeNode.getNodeType() != Node.ELEMENT_NODE) {
			Logger.putLog("Warning: check " + id + " has invalid 'node' attribute.", Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		String nameTargetParent = checkCode.getFunctionElement();
		if (nameTargetParent.length() == 0) {
			Logger.putLog("Warning: check " + id + " has invalid 'element' attribute.", Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		// check for any parent
		Element elementParent = DOMUtil.getParent((Element) nodeNode);
		int value;
		if (checkCode.getFunctionValue() != null && StringUtils.isNotEmpty(checkCode.getFunctionValue())) {
			try {
				value = Integer.parseInt(checkCode.getFunctionValue());
			} catch (NumberFormatException nfe) {
				value = Integer.MAX_VALUE;
			}
		} else {
			value = Integer.MAX_VALUE;
		}
		while (elementParent != null && value != 0) {
			if (nameTargetParent.equalsIgnoreCase(elementParent.getNodeName())) {
				return true;
			}
			elementParent = DOMUtil.getParent(elementParent);
			value--;
		}
		return false;
	}

	/**
	 * Function contains.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionContains(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		if (nodeNode.getNodeType() != Node.ELEMENT_NODE) {
			Logger.putLog("Warning: check " + id + " has invalid 'node' attribute.", Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		String nameTargetChild = checkCode.getFunctionElement();
		if (nameTargetChild.length() == 0) {
			Logger.putLog("Warning: check " + id + " has invalid 'element' attribute.", Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		// check for any child
		NodeList nodeList = elementGiven.getChildNodes();
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == elementGiven.getNodeType()) {
					if (nodeList.item(i).getNodeName().equalsIgnoreCase(nameTargetChild)) {
						return true;
					} else {
						if (functionContains(checkCode, nodeNode, (Element) nodeList.item(i))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Function contains not.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionContainsNot(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionContains(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function container not.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionContainerNot(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionContainer(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function check colors.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCheckColors(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NamedNodeMap attributes = elementGiven.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			if (hasColor(attributes.item(i), checkCode.getFunctionValue())) {
				return true;
			}
		}
		NodeList nodeList = elementGiven.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.TEXT_NODE && hasColor(nodeList.item(i), checkCode.getFunctionValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks for color.
	 *
	 * @param node  the node
	 * @param color the color
	 * @return true, if successful
	 */
	private boolean hasColor(Node node, String color) {
		Pattern pattern = Pattern.compile(color, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(node.getTextContent());
		return matcher.find();
	}

	/**
	 * Function has element into.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasElementInto(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		if (nodeNode.getNodeType() != Node.ELEMENT_NODE) {
			Logger.putLog("Warning: check " + id + " has invalid 'node' attribute.", Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		final String nameTargetChild1 = checkCode.getFunctionAttribute1();
		final String nameTargetChild2 = checkCode.getFunctionAttribute2();
		if (nameTargetChild1.length() == 0 || nameTargetChild2.length() == 0) {
			Logger.putLog("Warning: check " + id + " has invalid 'element' attribute.", Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		// check for any child
		final NodeList nodeList = elementGiven.getChildNodes();
		if (nodeList != null && nodeList.getLength() > 0) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					if (nodeList.item(i).getNodeName().equalsIgnoreCase(nameTargetChild1)) {
						if (EvaluatorUtility.countElements((Element) nodeNode, checkCode.getFunctionValue(), null, 1) != 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Function same following list.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSameFollowingList(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		if (nodeNode.getNodeType() != Node.ELEMENT_NODE) {
			Logger.putLog("Warning: check " + id + " has invalid 'node' attribute.", Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		Node brother = elementGiven.getNextSibling();
		while (brother != null && brother.getNodeType() != Node.ELEMENT_NODE) {
			brother = brother.getNextSibling();
		}
		String element = "li";
		if ("dl".equalsIgnoreCase(elementGiven.getTagName())) {
			element = "dt";
		}
		if ((brother != null) && (brother.getNodeName().equalsIgnoreCase(elementGiven.getTagName()))) {
			if ((EvaluatorUtility.countElements((Element) brother, "ol", null, 1) == 0) && (EvaluatorUtility.countElements((Element) brother, "ul", null, 1) == 0)
					&& (EvaluatorUtility.countElements((Element) brother, "dl", null, 1) == 0) && (EvaluatorUtility.countElements((Element) brother, element, null, 1) == 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Function same following list not.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSameFollowingListNot(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionSameFollowingList(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function text contain general quote.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextContainGeneralQuote(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
				if (nodeList.item(i).getTextContent().lastIndexOf("\"") != -1) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function same element.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSameElement(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return elementGiven.getNodeName().equalsIgnoreCase(checkCode.getFunctionElement());
	}

	/**
	 * Function same element not.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSameElementNot(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionSameElement(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function number compare.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @param compType     the comp type
	 * @return true, if successful
	 */
	private boolean functionNumberCompare(CheckCode checkCode, Node nodeNode, Element elementGiven, int compType) {
		// special case the image elements because width and height are set in
		// parser
		if ("img".equalsIgnoreCase(elementGiven.getNodeName())) {
			String stringNodeAttribute = checkCode.getNodeRelation();
			if ("@width".equalsIgnoreCase(stringNodeAttribute)) {
				Dimension dimension = (Dimension) elementGiven.getUserData("dimension");
				if (dimension != null) {
					try {
						int number = Integer.parseInt(checkCode.getFunctionValue());
						if (compType == CheckFunctionConstants.COMPARE_LESS_THAN) {
							return dimension.width < number;
						} else {
							return dimension.width > number;
						}
					} catch (Exception e) {
						Logger.putLog("Exception : ", Check.class, Logger.LOG_LEVEL_ERROR, e);
						return false;
					}
				}
			} else if ("@height".equalsIgnoreCase(stringNodeAttribute)) {
				Dimension dimension = (Dimension) elementGiven.getUserData("dimension");
				if (dimension != null) {
					try {
						int number = Integer.parseInt(checkCode.getFunctionValue());
						if (compType == CheckFunctionConstants.COMPARE_LESS_THAN) {
							return dimension.height < number;
						} else {
							return dimension.height > number;
						}
					} catch (Exception e) {
						Logger.putLog("Exception : ", Check.class, Logger.LOG_LEVEL_ERROR, e);
						return false;
					}
				}
			}
		}
		if (nodeNode == null) {
			return false;
		}
		String string = "";
		if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
			string = EvaluatorUtility.getElementText(nodeNode);
		} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			string = nodeNode.getNodeValue().trim();
		}
		try {
			int number1;
			if ("@content".equalsIgnoreCase(checkCode.getNodeRelation())) {
				number1 = Integer.parseInt(string.split(";")[0]);
			} else {
				number1 = Integer.parseInt(string);
			}
			int number2 = Integer.parseInt(checkCode.getFunctionValue());
			if (compType == CheckFunctionConstants.COMPARE_LESS_THAN) {
				return number1 < number2;
			} else {
				return number1 > number2;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Function number less than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNumberLessThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionNumberCompare(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_LESS_THAN);
	}

	/**
	 * Function number greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNumberGreaterThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionNumberCompare(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_GREATER_THAN);
	}

	/**
	 * Function characters compare.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @param compType     the comp type
	 * @return true, if successful
	 */
	private boolean functionCharactersCompare(CheckCode checkCode, Node nodeNode, Element elementGiven, int compType) {
		if (nodeNode == null) {
			return false;
		}
		try {
			int number = Integer.parseInt(checkCode.getFunctionValue());
			int numCharacters = 0;
			if (StringUtils.isNotEmpty(checkCode.getFunctionAttribute1()) && "true".equalsIgnoreCase(checkCode.getFunctionAttribute1())) {
				numCharacters = countCharacters(nodeNode, true);
			} else {
				numCharacters = countCharacters(nodeNode);
			}
			if (compType == CheckFunctionConstants.COMPARE_GREATER_THAN && numCharacters > number) {
				return true;
			} else if (compType == CheckFunctionConstants.COMPARE_LESS_THAN && numCharacters < number) {
				return true;
			}
		} catch (Exception e) {
			Logger.putLog("Exception : ", Check.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return false;
	}

	/**
	 * Function characters greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCharactersGreaterThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionCharactersCompare(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_GREATER_THAN);
	}

	/**
	 * Function characters less than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCharactersLessThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionCharactersCompare(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_LESS_THAN);
	}

	/**
	 * Count characters.
	 *
	 * @param node                  the node
	 * @param getOnlyInlineTagsText the get only inline tags text
	 * @return the int
	 */
	private int countCharacters(Node node, boolean getOnlyInlineTagsText) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return EvaluatorUtility.getElementText(node, getOnlyInlineTagsText).length();
		} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
			return StringUtils.normalizeWhiteSpaces(node.getNodeValue()).trim().length();
		}
		return 0;
	}

	/**
	 * Count characters.
	 *
	 * @param node the node
	 * @return the int
	 */
	private int countCharacters(Node node) {
		return countCharacters(node, false);
	}

	/**
	 * Function not is only blanks.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotIsOnlyBlanks(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionIsOnlyBlanks(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function is only blanks.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionIsOnlyBlanks(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		String nodeText = "";
		if (nodeNode != null) {
			if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
				nodeText = EvaluatorUtility.getElementText(nodeNode);
			} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				nodeText = nodeNode.getNodeValue();
			}
			return StringUtils.isOnlyBlanks(nodeText);
		} else {
			return false;
		}
	}

	/**
	 * Function is empty element.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionIsEmptyElement(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList elements = elementGiven.getElementsByTagName(checkCode.getFunctionElement());
		for (int i = 0; i < elements.getLength(); i++) {
			final String text = EvaluatorUtility.getElementText(elements.item(i));
			if (StringUtils.isNotEmpty(text) && !StringUtils.isOnlyBlanks(text)) {
				// Si el error consiste en que todos los elementos estén vacíos
				// y se encuentra uno
				// que no lo está, se devuelve resultado correcto
				if (checkCode.getFunctionValue().equalsIgnoreCase(IntavConstants.ALL)) {
					return false;
				}
			} else if (checkCode.getFunctionValue().equalsIgnoreCase(IntavConstants.ANY)) {
				// Si se busca cualquier elemento vacío, se devuelve el error
				return true;
			}
		}
		return checkCode.getFunctionValue().equalsIgnoreCase(IntavConstants.ALL);
	}

	/**
	 * Function not valid url.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotValidUrl(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionValidUrl(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function valid url.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionValidUrl(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		return CheckUtils.isValidUrl(elementRoot, nodeNode);
	}

	/**
	 * Function too many broken links.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTooManyBrokenLinks(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData("domainLinks") == null && elementRoot.getUserData("externalLinks") == null) {
			((CheckedLinks) elementRoot.getUserData("checkedLinks")).setCheckedLinks(new ArrayList<String>());
			final List<Element> domainLinks = new LinkedList<>();
			final List<Element> externalLinks = new LinkedList<>();
			final String url = (String) elementRoot.getUserData("url");
			final NodeList links = elementGiven.getElementsByTagName("A");
			for (int i = 0; i < links.getLength(); i++) {
				final Element link = (Element) links.item(i);
				if (StringUtils.isNotEmpty(link.getAttribute("href"))) {
					if (!CheckUtils.isValidUrl(elementRoot, link.getAttributeNode("href"))) {
						try {
							if (CheckUtils.checkLinkInDomain(url, link.getAttribute("href"))) {
								domainLinks.add(link);
							} else {
								externalLinks.add(link);
							}
						} catch (IOException e) {
							if (link.getAttribute("href").contains(url)) {
								domainLinks.add(link);
							} else {
								externalLinks.add(link);
							}
						}
					}
				}
			}
			elementRoot.setUserData("domainLinks", domainLinks, null);
			elementRoot.setUserData("externalLinks", externalLinks, null);
		}
		final String scope = checkCode.getFunctionAttribute1();
		final int maxNumBrokenLinks = Integer.parseInt(checkCode.getFunctionNumber());
		int cont = 0;
		try {
			if (elementRoot.getUserData("domainLinks") != null && elementRoot.getUserData("externalLinks") != null) {
				if (scope.isEmpty()) {
					cont = ((List) elementRoot.getUserData("domainLinks")).size() + ((List) elementRoot.getUserData("externalLinks")).size();
				} else if ("domain".equals(scope)) {
					cont = ((List) elementRoot.getUserData("domainLinks")).size();
				} else if ("external".equals(scope)) {
					cont = ((List) elementRoot.getUserData("externalLinks")).size();
				}
			}
		} catch (Exception e) {
		}
		return cont > maxNumBrokenLinks;
	}

	/**
	 * Function count attribute value greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCountAttributeValueGreaterThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getElementsByTagName(checkCode.getFunctionAttribute1());
		int count = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			if (element.hasAttribute(checkCode.getFunctionAttribute2()) && element.getAttributeNode(checkCode.getFunctionAttribute2()).toString().equalsIgnoreCase(checkCode.getFunctionValue())) {
				count++;
			}
		}
		return count > Integer.parseInt(checkCode.getFunctionNumber());
	}

	/**
	 * Function not exist attribute value.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotExistAttributeValue(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionExistAttributeValue(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function exist attribute value.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionExistAttributeValue(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList nodeList = elementGiven.getElementsByTagName(checkCode.getFunctionElement());
		if (nodeList == null || nodeList.getLength() == 0) {
			return false;
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (((Element) nodeList.item(i)).getAttribute(checkCode.getFunctionAttribute1()) != null
					&& ((Element) nodeList.item(i)).getAttribute(checkCode.getFunctionAttribute1()).equalsIgnoreCase(checkCode.getFunctionValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Function not external url.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotExternalUrl(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionExternalUrl(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function external url.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionExternalUrl(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		try {
			URL documentUrl = CheckUtils.getBaseUrl(elementRoot) != null ? new URL(CheckUtils.getBaseUrl(elementRoot)) : new URL((String) elementRoot.getUserData("url"));
			URL remoteUrl = new URL(documentUrl, nodeNode.getTextContent());
			return !remoteUrl.getHost().equals(documentUrl.getHost());
		} catch (Exception e) {
			// Si es una excepción, no podemos decir que sea enlace externo
			return false;
		}
	}

	/**
	 * Function link same page.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionLinkSamePage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		try {
			if (elementGiven.getAttribute(checkCode.getFunctionValue()) != null && StringUtils.isNotEmpty(elementGiven.getAttribute(checkCode.getFunctionValue()))) {
				URL documentUrl = CheckUtils.getBaseUrl(elementRoot) != null ? new URL(CheckUtils.getBaseUrl(elementRoot)) : new URL((String) elementRoot.getUserData("url"));
				URL remoteUrl = new URL(documentUrl, elementGiven.getAttribute(checkCode.getFunctionValue()));
				if (documentUrl.toString().replaceAll("/$", "").equals(remoteUrl.toString().replaceAll("/$", ""))) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Function text not match.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextNotMatch(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionTextMatch(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function text match.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextMatch(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		String regexp = checkCode.getFunctionValue();
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		String texto = "";
		if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
			texto = DOMUtil.getChildText(nodeNode).toLowerCase();
		} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			texto = nodeNode.getNodeValue().toLowerCase();
		}
		Matcher matcher = pattern.matcher(texto);
		return matcher.find();
	}

	/**
	 * Función que realiza la misma comprobación que functionTextMatch sólo que la expresión regular la va a recuperar de un fichero de propiedades.
	 * 
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextMatchProperties(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		String key = checkCode.getFunctionValue();
		// Recupera la expresión regular de un fichero de propiedades
		PropertiesManager pm = new PropertiesManager();
		String regexp = pm.getValue("check.patterns.properties", key);
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		String texto = "";
		if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
			texto = DOMUtil.getChildText(nodeNode).toLowerCase();
		} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			texto = nodeNode.getNodeValue().toLowerCase();
		}
		Matcher matcher = pattern.matcher(texto);
		return matcher.find();
	}

	/**
	 * Actualizada la función para que recupere las expresiones regulares de propiedades.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributeElementTextMatch(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		String element = checkCode.getFunctionElement();
		String attribute = checkCode.getFunctionAttribute1();
		NodeList nodeList = elementGiven.getElementsByTagName(element);
		StringBuilder attributeText = new StringBuilder();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element node = (Element) nodeList.item(i);
			if (node.hasAttribute(attribute)) {
				attributeText.append(" ");
				attributeText.append(node.getAttribute(attribute));
			}
		}
		// Recupera de fichero de propiedades
		PropertiesManager pm = new PropertiesManager();
		String regexp = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		return StringUtils.textMatchs(attributeText.toString(), regexp);
	}

	/**
	 * Function internal element count greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @param compType     the comp type
	 * @return true, if successful
	 */
	private boolean functionInternalElementCountGreaterThan(CheckCode checkCode, Node nodeNode, Element elementGiven, int compType) {
		NodeList nodeList = elementGiven.getChildNodes();
		boolean found = false;
		int numBr = 0;
		int i = 0;
		while (nodeList.getLength() > numBr && !found) {
			if (nodeList.item(i) != null) {
				if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).getNodeName().equalsIgnoreCase(checkCode.getFunctionValue())) {
					numBr++;
				} else {
					found = true;
				}
				i++;
			}
		}
		if (numBr != nodeList.getLength()) {
			i = nodeList.getLength() - 1;
			found = false;
			while (i >= 0 && !found) {
				if (nodeList.item(i) != null) {
					if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE && nodeList.item(i).getNodeName().equalsIgnoreCase(checkCode.getFunctionValue())) {
						numBr++;
					} else {
						found = true;
					}
					i--;
				}
			}
		}
		return (elementGiven.getElementsByTagName("br").getLength() - numBr) >= Integer.parseInt(checkCode.getFunctionNumber());
	}

	/**
	 * Function element count compare.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @param compType     the comp type
	 * @return true, if successful
	 */
	private boolean functionElementCountCompare(CheckCode checkCode, Node nodeNode, Element elementGiven, int compType) {
		if (nodeNode == null) {
			return false;
		}
		// If node is ul or ol and childs is li skip this verification
		try {
			int childLevel = -1;
			if (checkCode.getFunctionAttribute1() != null && !checkCode.getFunctionAttribute1().equals("")) {
				childLevel = Integer.parseInt(checkCode.getFunctionAttribute1());
			}
			int numElements = EvaluatorUtility.countElements((Element) nodeNode, checkCode.getFunctionValue(), checkCode.getFunctionAttribute2(), childLevel);
			int numLimit = Integer.parseInt(checkCode.getFunctionNumber());
			if (compType == CheckFunctionConstants.COMPARE_GREATER_THAN && numElements > numLimit) {
				return true;
			} else if (compType == CheckFunctionConstants.COMPARE_LESS_THAN && numElements < numLimit) {
				return true;
			} else if (compType == CheckFunctionConstants.COMPARE_EQUAL && numElements == numLimit) {
				return true;
			}
		} catch (Exception e) {
			Logger.putLog("Exception : ", Check.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return false;
	}

	/**
	 * Function element count greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionElementCountGreaterThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionElementCountCompare(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_GREATER_THAN);
	}

	/**
	 * Function element count less than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionElementCountLessThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionElementCountCompare(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_LESS_THAN);
	}

	/**
	 * Function element count equals.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionElementCountEquals(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return functionElementCountCompare(checkCode, nodeNode, elementGiven, CheckFunctionConstants.COMPARE_EQUAL);
	}

	/**
	 * Function element count not equals.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionElementCountNotEquals(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionElementCountEquals(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function attribute missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributeMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionAttributeExists(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function attribute exists.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributeExists(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return elementGiven.hasAttribute(checkCode.getFunctionValue());
	}

	/**
	 * Function attributes same.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributesSame(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		String stringAtt1 = ((Element) nodeNode).getAttribute(checkCode.getFunctionAttribute1());
		String stringAtt2 = ((Element) nodeNode).getAttribute(checkCode.getFunctionAttribute2());
		if ((stringAtt1.length() == 0) || (stringAtt2.length() == 0)) {
			return false;
		}
		return stringAtt1.equalsIgnoreCase(stringAtt2);
	}

	/**
	 * Function attributes not same.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributesNotSame(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		String stringAtt1 = ((Element) nodeNode).getAttribute(checkCode.getFunctionAttribute1());
		String stringAtt2 = ((Element) nodeNode).getAttribute(checkCode.getFunctionAttribute2());
		if ((stringAtt1.length() == 0) || (stringAtt2.length() == 0)) {
			return true;
		}
		return !stringAtt1.equalsIgnoreCase(stringAtt2);
	}

	/**
	 * Function text not equals.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextNotEquals(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionTextEquals(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function text equals.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextEquals(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		// get the 2 strings that are compared
		// convert them both to lower case
		String string1 = checkCode.getFunctionValue().toLowerCase();
		// special case the string "nbsp" - convert to "&nbsp;"
		if (string1.equals("nbsp")) {
			string1 = "&nbsp;";
		}
		String string2 = "";
		if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
			string2 = DOMUtil.getChildText(nodeNode).toLowerCase().trim();
		} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			string2 = nodeNode.getNodeValue().toLowerCase().trim();
		}
		// position of compare string
		final String stringPosition = checkCode.getFunctionPosition();
		if (stringPosition.isEmpty()) {
			return string1.equalsIgnoreCase(string2);
		} else if ("anywhere".equalsIgnoreCase(stringPosition)) {
			return string2.contains(string1);
		} else if ("end".equalsIgnoreCase(stringPosition)) {
			return string2.endsWith(string1);
		} else if ("start".equalsIgnoreCase(stringPosition)) {
			return string2.startsWith(string1);
		} else {
			Logger.putLog("Warning: check " + id + " has invalid 'position' attribute: " + stringPosition, Check.class, Logger.LOG_LEVEL_WARNING);
		}
		return false;
	}

	/**
	 * Comprueba que un texto no es igual a uno de los recuperados de un fichero de properties.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTextEqualsProperties(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		// Recupera la expresión regular de un fichero de propiedades
		PropertiesManager pm = new PropertiesManager();
		String[] strings = pm.getValue("check.patterns.properties", checkCode.getFunctionValue()).split("\\|");
		boolean textEquals = false;
		for (int i = 0; i < strings.length; i++) {
			String string1 = strings[i].toLowerCase();
			// special case the string "nbsp" - convert to "&nbsp;"
			if (string1.equals("nbsp")) {
				string1 = "&nbsp;";
			}
			String string2 = "";
			if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
				string2 = DOMUtil.getChildText(nodeNode).toLowerCase().trim();
			} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				string2 = nodeNode.getNodeValue().toLowerCase().trim();
			}
			// position of compare string
			final String stringPosition = checkCode.getFunctionPosition();
			if (stringPosition.isEmpty()) {
				textEquals = string1.equalsIgnoreCase(string2);
			} else if ("anywhere".equalsIgnoreCase(stringPosition)) {
				textEquals = string2.contains(string1);
			} else if ("end".equalsIgnoreCase(stringPosition)) {
				textEquals = string2.endsWith(string1);
			} else if ("start".equalsIgnoreCase(stringPosition)) {
				textEquals = string2.startsWith(string1);
			} else {
				Logger.putLog("Warning: check " + id + " has invalid 'position' attribute: " + stringPosition, Check.class, Logger.LOG_LEVEL_WARNING);
			}
			// Si alguno lo es abortamos
			if (textEquals) {
				break;
			}
		}
		return textEquals;
	}

	/**
	 * Function element previous.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionElementPrevious(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		// Obtener el nodo previo
		Node nodePrevious = EvaluatorUtils.getPreviousNode(elementGiven);
		return nodePrevious != null && nodePrevious.getNodeType() == Node.ELEMENT_NODE && nodePrevious.getNodeName().equalsIgnoreCase(checkCode.getFunctionValue());
	}

	/**
	 * Function targets same.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionTargetsSame(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		// Obtener el elemento previo
		Element elementPrevious = EvaluatorUtils.getPreviousElement(elementGiven, false);
		if (elementPrevious != null && elementPrevious.getNodeName().equalsIgnoreCase("a")) {
			if (StringUtils.isNotEmpty(elementGiven.getAttribute("href")) && StringUtils.isNotEmpty(elementPrevious.getAttribute("href"))) {
				Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
				try {
					URL documentUrl = CheckUtils.getBaseUrl(elementRoot) != null ? new URL(CheckUtils.getBaseUrl(elementRoot)) : new URL((String) elementRoot.getUserData("url"));
					URL currentUrl = new URL(documentUrl, elementGiven.getAttribute("href"));
					URL prevUrl = new URL(documentUrl, elementPrevious.getAttribute("href"));
					if (currentUrl.toString().equals(prevUrl.toString())) {
						return true;
					}
				} catch (Exception e) {
					// Si por alguna razón no podemos crear las URL,
					// compararemos los HREF a pelo
					if (elementGiven.getAttribute("href").equals(elementPrevious.getAttribute("href"))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Function html content not.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHtmlContentNot(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return nodeNode != null && !functionHtmlContent(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function html content.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHtmlContent(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (nodeNode == null) {
			return false;
		}
		String string = "";
		if (nodeNode.getNodeType() == Node.ELEMENT_NODE) {
			string = DOMUtil.getChildText(nodeNode).toLowerCase();
		} else if (nodeNode.getNodeType() == Node.ATTRIBUTE_NODE) {
			string = nodeNode.getNodeValue().toLowerCase();
		}
		return isHtmlContent(string);
	}

	// Check if the form has multiple radio buttons with the same 'name'
	// attribute and is
	/**
	 * Function multi radio no fieldset.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// missing both fieldset and legend elements.
	private boolean functionMultiRadioNoFieldset(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// check if the form has both fieldset and legend elements
		NodeList listFieldsets = elementGiven.getElementsByTagName("fieldset");
		if ((listFieldsets != null) && (listFieldsets.getLength() > 0)) {
			// form contains 'fieldset' now check for 'legend'
			NodeList listLegends = elementGiven.getElementsByTagName("legend");
			if ((listLegends != null) && (listLegends.getLength() > 0)) {
				return false; // form contains both fieldset and legend
			}
		}
		// Form does not contain both fieldset and legend.
		// Check for radio buttons (input element with type attribute value of
		// 'radio')
		// that have the same 'name' attribute value.
		return existsMultipleSelectionButtons(elementGiven, "radio");
	}

	/**
	 * Function multi checkbox no fieldset.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionMultiCheckboxNoFieldset(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// check if the form has both fieldset and legend elements
		NodeList listFieldsets = elementGiven.getElementsByTagName("fieldset");
		if ((listFieldsets != null) && (listFieldsets.getLength() > 0)) {
			// form contains 'fieldset' now check for 'legend'
			NodeList listLegends = elementGiven.getElementsByTagName("legend");
			if ((listLegends != null) && (listLegends.getLength() > 0)) {
				return false; // form contains both fieldset and legend
			}
		}
		// Form does not contain both fieldset and legend.
		// Check for checkbox buttons (input element with type attribute value
		// of 'checkbox')
		// that have the same 'name' attribute value.
		return existsMultipleSelectionButtons(elementGiven, "checkbox");
	}

	/**
	 * Exists multiple selection buttons.
	 *
	 * @param element the element
	 * @param type    the type
	 * @return true, if successful
	 */
	private boolean existsMultipleSelectionButtons(final Element element, final String type) {
		if (type != null) {
			final NodeList listInputs = element.getElementsByTagName("input");
			for (int x = 0; x < listInputs.getLength(); x++) {
				final Element elementInput = (Element) listInputs.item(x);
				if (type.equalsIgnoreCase(elementInput.getAttribute("type"))) {
					final String stringName = elementInput.getAttribute("name");
					for (int y = x + 1; y < listInputs.getLength(); y++) {
						final Element elementInput2 = (Element) listInputs.item(y);
						if (type.equalsIgnoreCase(elementInput2.getAttribute("type"))) {
							if (elementInput.getAttribute("name").equalsIgnoreCase(stringName)) {
								return true; // found input buttons with same
												// name
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if is html content.
	 *
	 * @param string the string
	 * @return true, if is html content
	 */
	public static boolean isHtmlContent(final String string) {
		if (string.endsWith(".html")) {
			return true;
		} else if (string.endsWith(".htm")) {
			return true;
		} else if (string.endsWith(".shtml")) {
			return true;
		} else if (string.endsWith(".shtm")) {
			return true;
		} else if (string.endsWith(".cfm")) {
			return true;
		} else if (string.endsWith(".cfml")) {
			return true;
		} else if (string.endsWith(".asp")) {
			return true;
		} else if (string.endsWith(".cgi")) {
			return true;
		} else if (string.endsWith(".cl")) {
			return true;
		}
		return false;
	}

	/**
	 * Function luminosity contrast ratio.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if there is an accessibility problem.
	private boolean functionLuminosityContrastRatio(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// does the given element have the required attributes?
		if (!elementGiven.hasAttribute(checkCode.getFunctionAttribute1())) {
			return false; // attribute missing so no accessibility problem
		}
		if (!elementGiven.hasAttribute(checkCode.getFunctionAttribute2())) {
			return false; // attribute missing so no accessibility problem
		}
		// calculate the luminosity ratio between the 2 attribute values
		String stringColor1 = elementGiven.getAttribute(checkCode.getFunctionAttribute1());
		ColorValues colorValue1 = new ColorValues(stringColor1);
		if (!colorValue1.getValid()) {
			return false;
		}
		String stringColor2 = elementGiven.getAttribute(checkCode.getFunctionAttribute2());
		ColorValues colorValue2 = new ColorValues(stringColor2);
		if (!colorValue2.getValid()) {
			return false;
		}
		double linearR1 = colorValue1.getRed() / 255d;
		double linearG1 = colorValue1.getGreen() / 255d;
		double linearB1 = colorValue1.getBlue() / 255d;
		double lum1 = ((Math.pow(linearR1, 2.2)) * 0.2126d) + ((Math.pow(linearG1, 2.2)) * 0.7152d) + ((Math.pow(linearB1, 2.2)) * 0.0722d) + .05d;
		double linearR2 = colorValue2.getRed() / 255d;
		double linearG2 = colorValue2.getGreen() / 255d;
		double linearB2 = colorValue2.getBlue() / 255d;
		double lum2 = ((Math.pow(linearR2, 2.2)) * 0.2126d) + ((Math.pow(linearG2, 2.2)) * 0.7152d) + ((Math.pow(linearB2, 2.2)) * 0.0722d) + .05d;
		double ratio = Math.max(lum1, lum2) / Math.min(lum1, lum2);
		// round the ratio to 2 decimal places
		long factor = (long) Math.pow(10, 2);
		// Shift the decimal the correct number of places
		// to the right.
		double val = ratio * factor;
		// Round to the nearest integer.
		long tmp = Math.round(val);
		// Shift the decimal the correct number of places back to the left.
		float ratio2 = (float) tmp / factor;
		return ratio2 < 4.99;
	}

	// Checks if the colors pass the WAI ERT color contrast threshold.
	/**
	 * Function color contrast wai ert.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if there is an accessibility problem.
	private boolean functionColorContrastWaiErt(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// does the given element have the required attributes?
		if (!elementGiven.hasAttribute(checkCode.getFunctionAttribute1())) {
			return false; // attribute missing so no accessibility problem
		}
		if (!elementGiven.hasAttribute(checkCode.getFunctionAttribute2())) {
			return false; // attribute missing so no accessibility problem
		}
		// calculate the brightness difference
		String stringColor1 = elementGiven.getAttribute(checkCode.getFunctionAttribute1());
		ColorValues colorValue1 = new ColorValues(stringColor1);
		if (!colorValue1.getValid()) {
			return false;
		}
		String stringColor2 = elementGiven.getAttribute(checkCode.getFunctionAttribute2());
		ColorValues colorValue2 = new ColorValues(stringColor2);
		if (!colorValue2.getValid()) {
			return false;
		}
		int brightness1 = ((colorValue1.getRed() * 299) + (colorValue1.getGreen() * 587) + (colorValue1.getBlue() * 114)) / 1000;
		int brightness2 = ((colorValue2.getRed() * 299) + (colorValue2.getGreen() * 587) + (colorValue2.getBlue() * 114)) / 1000;
		int difference = 0;
		if (brightness1 > brightness2) {
			difference = brightness1 - brightness2;
		} else {
			difference = brightness2 - brightness1;
		}
		if (difference < 125) {
			return true;
		}
		// calculate the color difference
		difference = 0;
		// red
		if (colorValue1.getRed() > colorValue2.getRed()) {
			difference = colorValue1.getRed() - colorValue2.getRed();
		} else {
			difference = colorValue2.getRed() - colorValue1.getRed();
		}
		// green
		if (colorValue1.getGreen() > colorValue2.getGreen()) {
			difference += colorValue1.getGreen() - colorValue2.getGreen();
		} else {
			difference += colorValue2.getGreen() - colorValue1.getGreen();
		}
		// blue
		if (colorValue1.getBlue() > colorValue2.getBlue()) {
			difference += colorValue1.getBlue() - colorValue2.getBlue();
		} else {
			difference += colorValue2.getBlue() - colorValue1.getBlue();
		}
		return difference <= 499;
	}

	// Checks if there is a doctype declaration and it is strict.
	/**
	 * Function doctype attribute not equal.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if there is an accessibility problem.
	private boolean functionDoctypeAttributeNotEqual(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// does the document have a doctype?
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		String hasDoctype = (String) elementRoot.getUserData("doctype");
		if (!hasDoctype.equals(IntavConstants.TRUE)) {
			// no doctype, must have strict so this is an error
			return true;
		}
		// is the doctype system ID strict?
		String stringDoctype = (String) elementRoot.getUserData(checkCode.getFunctionElement());
		String checkDoctype = checkCode.getFunctionValue();
		if (stringDoctype != null) {
			if (stringDoctype.equalsIgnoreCase(checkDoctype)) {
				return false;
			}
		}
		// not strict, return error
		return true;
	}

	// Checks if the document validates to its doctype declaration.
	/**
	 * Function validate.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if document does not validate.
	private boolean functionValidate(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		List vectorValidationErrors = (List) elementRoot.getUserData("validationErrors");
		int numErrors = Integer.parseInt(checkCode.getFunctionNumber());
		return vectorValidationErrors == null || countDistinctValidationErrors(vectorValidationErrors) <= numErrors;
	}

	/**
	 * Count distinct validation errors.
	 *
	 * @param vectorValidationErrors the vector validation errors
	 * @return the int
	 */
	private int countDistinctValidationErrors(List<ValidationError> vectorValidationErrors) {
		List<String> distinctErrors = new ArrayList<>();
		for (ValidationError validationError : vectorValidationErrors) {
			if (validationError.getMessageId() != null && !distinctErrors.contains(validationError.getMessageId())) {
				distinctErrors.add(validationError.getMessageId());
			}
		}
		return distinctErrors.size();
	}

	// Comprueba si las hojas de estilo están validadas
	/**
	 * Function validate css.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if document does not validate.
	private boolean functionValidateCss(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		List<Object> vectorValidationErrors = (List) elementRoot.getUserData("cssValidationErrors");
		int numErrors = Integer.parseInt(checkCode.getFunctionNumber());
		return vectorValidationErrors == null || vectorValidationErrors.size() <= numErrors;
	}

	// Checks if the given table has one row of headers and one column of
	// headers
	// but lacks SCOPE attributes.
	/**
	 * Function missing scope.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if the given table contains this error.
	private boolean functionMissingScope(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// check if the table has more than one row/column of headers
		int countThRow = 0;
		boolean thCol = false;
		boolean bUsesScope = false;
		boolean bMissingScope = false;
		NodeList listRows = elementGiven.getElementsByTagName("tr");
		for (int x = 0; x < listRows.getLength(); x++) {
			Element elementRow = (Element) listRows.item(x);
			NodeList listTh = elementRow.getElementsByTagName("th");
			if (listTh.getLength() > 1) {
				countThRow++;
			} else if (listTh.getLength() == 1) {
				thCol = true;
			}
			// check if there are SCOPE attributes on all of the TH elements
			for (int c = 0; c < listTh.getLength(); c++) {
				final Element elementTh = (Element) listTh.item(c);
				if (elementTh.hasAttribute("scope")) {
					final String scope = elementTh.getAttribute("scope");
					if ("col".equalsIgnoreCase(scope) || "row".equalsIgnoreCase(scope) || "colgroup".equalsIgnoreCase(scope) || "rowgroup".equalsIgnoreCase(scope)) {
						bUsesScope = true;
						break;
					} else {
						bUsesScope = false;
					}
				} else {
					bMissingScope = true;
				}
			}
		}
		if (countThRow == 1 && thCol) {
			// has 1 row and 1 column of headers
			return !(bUsesScope && !bMissingScope);
		}
		// does not contain 1 row of headers and 1 column of headers
		return false; // no problem
	}

	// Checks if the given table has one row of headers and one column of
	// headers
	// but lacks SCOPE attributes.
	/**
	 * Function invalid scope.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if the given table contains this error.
	private boolean functionInvalidScope(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// check if the table has more than one row/column of headers
		if (elementGiven.hasAttribute("scope")) {
			final String scope = elementGiven.getAttribute("scope");
			return !("col".equalsIgnoreCase(scope) || "row".equalsIgnoreCase(scope) || "colgroup".equalsIgnoreCase(scope) || "rowgroup".equalsIgnoreCase(scope));
		}
		return false; // no problem
	}

	// Checks if the caption and summary text are the same.
	/**
	 * Function caption summary same.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	// Returns true if they are the same, false if different (or don't exist).
	private boolean functionCaptionSummarySame(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// does table have a summary?
		if (!elementGiven.hasAttribute("summary")) {
			return false; // no summary
		}
		// does table have a caption?
		NodeList listCaption = elementGiven.getElementsByTagName("caption");
		if (listCaption.getLength() == 0) {
			return false; // no caption
		}
		String stringSummary = elementGiven.getAttribute("summary").trim();
		if (stringSummary.length() == 0) {
			return false; // no problem if both are empty
		}
		// String stringCaption =
		// DOMUtil.getChildText(listCaption.item(0)).trim();
		String stringCaption = EvaluatorUtility.getElementText(listCaption.item(0), true);
		return stringSummary.equalsIgnoreCase(stringCaption);
	}

	/**
	 * Initialize.
	 *
	 * @param nodeXml  the node xml
	 * @param idNumber the id number
	 * @return true, if successful
	 */
	public boolean initialize(Element nodeXml, int idNumber) {
		id = idNumber;
		if (!setCheckText(nodeXml)) {
			Logger.putLog("text bad, check: " + id, Check.class, Logger.LOG_LEVEL_WARNING);
			return false;
		}
		status = nodeXml.getAttribute("status");
		if (!nodeXml.hasAttribute("confidence")) {
			Logger.putLog("Warning: Check does not have confidence: " + id, Check.class, Logger.LOG_LEVEL_WARNING);
		} else {
			String stringConfidence = nodeXml.getAttribute("confidence");
			if ("medium".equalsIgnoreCase(stringConfidence)) {
				confidence = CheckFunctionConstants.CONFIDENCE_MEDIUM;
			} else if ("high".equalsIgnoreCase(stringConfidence)) {
				confidence = CheckFunctionConstants.CONFIDENCE_HIGH;
			} else if ("cannottell".equalsIgnoreCase(stringConfidence)) {
				confidence = CheckFunctionConstants.CONFIDENCE_CANNOTTELL;
			} else {
				Logger.putLog("Warning: Check has invalid confidence: " + stringConfidence, Check.class, Logger.LOG_LEVEL_WARNING);
			}
		}
		if (nodeXml.hasAttribute("note")) {
			note = nodeXml.getAttribute("note");
		}
		if (!nodeXml.hasAttribute("key")) {
			Logger.putLog("Warning: Check does not have key attribute: " + id, Check.class, Logger.LOG_LEVEL_WARNING);
		} else {
			keyElement = nodeXml.getAttribute("key");
		}
		if (nodeXml.hasAttribute("occurrence")) {
			if (nodeXml.getAttribute("occurrence").equalsIgnoreCase("first")) {
				firstOccuranceOnly = true;
			}
		}
		NodeList childNodes = nodeXml.getChildNodes();
		for (int x = 0; x < childNodes.getLength(); x++) {
			// prerequisites
			if (childNodes.item(x).getNodeName().equalsIgnoreCase("prerequisite")) {
				if (((Element) childNodes.item(x)).hasAttribute("id")) {
					String stringPrereqId = ((Element) childNodes.item(x)).getAttribute("id");
					try {
						Integer newint = Integer.valueOf(stringPrereqId);
						prerequisites.add(newint);
					} catch (NumberFormatException nfe) {
						Logger.putLog("Prerequisite ID is invalid: " + stringPrereqId, Check.class, Logger.LOG_LEVEL_WARNING);
					}
				}
			}
			// machine code section
			else if (childNodes.item(x).getNodeName().equalsIgnoreCase("machine")) {
				createCode((Element) childNodes.item(x));
			}
		}
		checkOkCode = CheckFunctionConstants.CHECK_STATUS_OK;
		return true;
	}

	/**
	 * Creates the code.
	 *
	 * @param elementCodeGiven the element code given
	 * @return true, if successful
	 */
	private boolean createCode(Element elementCodeGiven) {
		// trigger element
		NodeList listTriggers = elementCodeGiven.getElementsByTagName("trigger");
		if (listTriggers.getLength() == 0) {
			Logger.putLog("Error: check '" + id + "' code has no trigger!", Check.class, Logger.LOG_LEVEL_INFO);
			return false;
		}
		Element elementTrigger = (Element) listTriggers.item(0);
		String triggerElementTemp = elementTrigger.getAttribute("element");
		if ((triggerElementTemp == null) || (triggerElementTemp.length() == 0)) {
			Logger.putLog("Error: check'" + id + "' trigger has invalid element name.", Check.class, Logger.LOG_LEVEL_INFO);
			return false;
		}
		triggerElement = triggerElementTemp.toLowerCase();
		Node nodeCode = elementCodeGiven.getFirstChild();
		while (nodeCode != null) {
			if (nodeCode.getNodeType() == Node.ELEMENT_NODE) {
				CheckCode checkCode = new CheckCode();
				if (checkCode.create((Element) nodeCode)) {
					vectorCode.add(checkCode);
				}
			}
			nodeCode = nodeCode.getNextSibling();
		}
		return true;
	}

	/**
	 * Sets the check text.
	 *
	 * @param nodeXml the node xml
	 * @return true, if successful
	 */
	public boolean setCheckText(Node nodeXml) {
		// these must be present on each check
		String language = "en";
		boolean foundCheckName = false;
		boolean foundCheckError = false;
		NodeList childNodes = nodeXml.getChildNodes();
		for (int x = 0; x < childNodes.getLength(); x++) {
			// name
			if (childNodes.item(x).getNodeName().equalsIgnoreCase("name")) {
				foundCheckName = true;
				nameMap.put(language, childNodes.item(x));
			}
			// error
			else if (childNodes.item(x).getNodeName().equalsIgnoreCase("error")) {
				foundCheckError = true;
				errorHashtable.put(language, childNodes.item(x));
			}
			// description
			else if (childNodes.item(x).getNodeName().equalsIgnoreCase("rationale")) {
				rationaleHashtable.put(language, childNodes.item(x));
			}
		}
		if (!foundCheckName) {
			Logger.putLog("Error: check #" + id + " language: " + language + " has no 'name'!", Check.class, Logger.LOG_LEVEL_INFO);
		}
		if (!foundCheckError) {
			Logger.putLog("Error: check #" + id + " language: " + language + " has no 'error'!", Check.class, Logger.LOG_LEVEL_INFO);
		}
		return foundCheckName && foundCheckError;
	}

	/**
	 * Sets the appropriate data.
	 *
	 * @param languageCode the new appropriate data
	 */
	public void setAppropriateData(String languageCode) {
		languageAppropriate = languageCode;
	}

	/**
	 * Function lang grammar.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionLangGrammar(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		String documentGrammar = (String) elementRoot.getUserData("doctypeType");
		if (documentGrammar != null) {
			if (documentGrammar.equals(checkCode.getFunctionValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Function check valid doctype.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCheckValidDoctype(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		final String doctypeSource = (String) elementRoot.getUserData("doctypeSource");
		if (doctypeSource != null) {
			final PropertiesManager pmgr = new PropertiesManager();
			final List<String> validDoctypes = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "valid.doctypes").split(";"));
			for (String validDoctype : validDoctypes) {
				if (validDoctype.equalsIgnoreCase(doctypeSource)) {
					return true;
				}
			}
			return false;
		} else {
			// Comprobación específica para HTML5
			final DocumentType docType = elementGiven.getOwnerDocument().getDoctype();
			if (docType != null) {
				return "html".equalsIgnoreCase(docType.getName());
			}
		}
		return false;
	}

	/**
	 * Function has nbsp entities.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasNbspEntities(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final int numRepetitions = Integer.parseInt(checkCode.getFunctionValue());
		final NodeList nodeList = elementGiven.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
				if (nodeList.item(i).getTextContent() != null) {
					if (StringUtils.hasNbspRepetitions(nodeList.item(i).getTextContent(), numRepetitions)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Function metadata missing.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionMetadataMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList metadataNodes = elementGiven.getElementsByTagName("meta");
		for (int i = 0; i < metadataNodes.getLength(); i++) {
			if ((((Element) metadataNodes.item(i)).getAttribute("name").equalsIgnoreCase(checkCode.getFunctionValue())
					|| ((Element) metadataNodes.item(i)).getAttribute("http-equiv").equalsIgnoreCase(checkCode.getFunctionValue()))
					&& StringUtils.isNotEmpty(((Element) metadataNodes.item(i)).getAttribute("content"))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Function not all labels.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotAllLabels(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final int numLabels = elementGiven.getElementsByTagName("label").getLength();
		final int numControls = elementGiven.getElementsByTagName("select").getLength() + elementGiven.getElementsByTagName("textarea").getLength() + getNumDataInputs(elementGiven);
		return numLabels < numControls;
	}

	/**
	 * Gets the num data inputs.
	 *
	 * @param form the form
	 * @return the num data inputs
	 */
	private int getNumDataInputs(Element form) {
		final NodeList inputs = form.getElementsByTagName("input");
		final List<String> dataInputs = Arrays.asList("checkbox", "file", "password", "radio", "text", "");
		int cont = 0;
		for (int i = 0; i < inputs.getLength(); i++) {
			if (dataInputs.contains(((Element) inputs.item(i)).getAttribute("type"))) {
				cont++;
			}
		}
		return cont;
	}

	/**
	 * Gets the num data inputs exclude check radio.
	 *
	 * @param form the form
	 * @return the num data inputs exclude check radio
	 */
	private int getNumDataInputsExcludeCheckRadio(Element form) {
		final NodeList inputs = form.getElementsByTagName("input");
		final List<String> dataInputs = Arrays.asList("file", "password", "text", "");
		int cont = 0;
		for (int i = 0; i < inputs.getLength(); i++) {
			if (dataInputs.contains(((Element) inputs.item(i)).getAttribute("type"))) {
				cont++;
			}
		}
		return cont;
	}

	/**
	 * Function label incorrectly associated.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionLabelIncorrectlyAssociated(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String attributeFor = elementGiven.getAttribute("for");
		final Document document = elementGiven.getOwnerDocument();
		if (document != null) {
			if (controlsNumIds(document.getElementsByTagName("input"), attributeFor) + controlsNumIds(document.getElementsByTagName("select"), attributeFor)
					+ controlsNumIds(document.getElementsByTagName("textarea"), attributeFor) + controlsNumIds(document.getElementsByTagName("button"), attributeFor)
					+ controlsNumIds(document.getElementsByTagName("output"), attributeFor) + controlsNumIds(document.getElementsByTagName("meter"), attributeFor)
					+ controlsNumIds(document.getElementsByTagName("progess"), attributeFor) == 1) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Controls num ids.
	 *
	 * @param controls     the controls
	 * @param attributeFor the attribute for
	 * @return the int
	 */
	private int controlsNumIds(NodeList controls, String attributeFor) {
		int cont = 0;
		for (int i = 0; i < controls.getLength(); i++) {
			if (((Element) controls.item(i)).getAttribute("id").equalsIgnoreCase(attributeFor)) {
				cont++;
			}
		}
		return cont;
	}

	/**
	 * Function num more controls.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNumMoreControls(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final int numControls = elementGiven.getElementsByTagName("select").getLength() + elementGiven.getElementsByTagName("textarea").getLength() + getNumDataInputsExcludeCheckRadio(elementGiven);
		return numControls > Integer.parseInt(checkCode.getFunctionNumber());
	}

	/**
	 * Function is even.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionIsEven(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionIsOdd(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function is odd.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionIsOdd(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		int numElements = EvaluatorUtility.countElements((Element) nodeNode, checkCode.getFunctionAttribute1(), "", -1);
		int numElements2 = 0;
		if (checkCode.getFunctionAttribute2() != null) {
			numElements2 = EvaluatorUtility.countElements((Element) nodeNode, checkCode.getFunctionAttribute2(), "", -1);
		}
		return ((numElements + numElements2) % 2) == 0;
	}

	/**
	 * Checks for element childs.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean hasElementChilds(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList childNodes = elementGiven.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function not user data matchs.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotUserDataMatchs(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionUserDataMatchs(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function user data matchs.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionUserDataMatchs(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String userDataValue = (String) elementGiven.getUserData(checkCode.getFunctionElement());
		if (userDataValue != null) {
			final String regexp = checkCode.getFunctionValue();
			final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			final Matcher matcher = pattern.matcher(userDataValue);
			return matcher.find();
		} else {
			return false;
		}
	}

	/**
	 * Function not children have attribute.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotChildrenHaveAttribute(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionChildrenHaveAttribute(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function children have attribute.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionChildrenHaveAttribute(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final boolean severe = (checkCode.getFunctionAttribute2() == null) || (!checkCode.getFunctionAttribute2().equalsIgnoreCase("noSevere"));
		final NodeList elements = elementGiven.getElementsByTagName(checkCode.getFunctionElement());
		if (elements != null && elements.getLength() > 0) {
			for (int i = 0; i < elements.getLength(); i++) {
				final Element element = (Element) elements.item(i);
				if (severe && !element.hasAttribute(checkCode.getFunctionAttribute1())) {
					return false;
				} else if (element.getNodeName().equalsIgnoreCase(checkCode.getFunctionElement()) && element.hasAttribute(checkCode.getFunctionAttribute1())) {
					final String attributeText = element.getAttribute(checkCode.getFunctionAttribute1());
					if (StringUtils.isEmpty(attributeText) || StringUtils.isOnlyBlanks(attributeText)) {
						return false;
					}
				}
			}
		} else if (severe) {
			return false;
		}
		return true;
	}

	/**
	 * Function not clear language.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionNotClearLanguage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		try {
			final String source = (String) elementGiven.getOwnerDocument().getDocumentElement().getUserData("source");
			final String fleschText = FleschUtils.getContentFromHtml(source);
			final FleschAnalyzer fleschAnalyzer = FleschAdapter.getFleschAnalyzer(getLanguage(elementGiven, false));
			final double fleschValue = fleschAnalyzer.calculateFleschValue(fleschAnalyzer.countSyllables(fleschText), fleschAnalyzer.countWords(fleschText), fleschAnalyzer.countPhrases(fleschText));
			final int readabilityLevel = fleschAnalyzer.getReadabilityLevel(fleschValue);
			if (readabilityLevel < Integer.parseInt(checkCode.getFunctionValue())) {
				return true;
			}
		} catch (Exception e) {
			Logger.putLog("Error al analizar la legibilidad del texto", Check.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return false;
	}

	/**
	 * Function has not enough text.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasNotEnoughText(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		return !functionHasEnoughText(checkCode, nodeNode, elementGiven);
	}

	/**
	 * Function has enough text.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasEnoughText(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final PropertiesManager pmgr = new PropertiesManager();
		try {
			final String source = (String) elementGiven.getOwnerDocument().getDocumentElement().getUserData("source");
			final String fleschText = FleschUtils.getContentFromHtml(source);
			if (fleschText.length() >= Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "minimun.document.chars.for.flesch"))) {
				return true;
			}
		} catch (Exception e) {
			Logger.putLog("Error al comprobar si la longitud del texto es suficiente para realizar el análisis Flesch", Check.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return false;
	}

	/**
	 * Function has not section link.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasNotSectionLink(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("A");
		// Se recupera la expresión regular del fichero de propiedades
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		return AccesibilityDeclarationCheckUtils.getSectionLink(links, regex).isEmpty();
	}

	/**
	 * Function accessibility declaration no contact.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAccessibilityDeclarationNoContact(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		PropertiesManager pm = new PropertiesManager();
		String key = checkCode.getFunctionValue();
		String regexp = pm.getValue("check.patterns.properties", key);
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regexp);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		String key1 = checkCode.getFunctionAttribute1();
		String key2 = checkCode.getFunctionAttribute2();
		String regexp1 = pm.getValue("check.patterns.properties", key1);
		String regexp2 = pm.getValue("check.patterns.properties", key2);
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				// Recuperar de fichero las expresiones regulares
				return !AccesibilityDeclarationCheckUtils.hasContact(elementGiven.getOwnerDocument(), regexp1, regexp2);
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasContact = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						hasContact |= AccesibilityDeclarationCheckUtils.hasContact(document, regexp1, regexp2);
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return !hasContact;
		}
	}

	/**
	 * Function accessibility declaration no revision date.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAccessibilityDeclarationNoRevisionDate(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// Recuperar de fichero las expresiones regulares
		PropertiesManager pm = new PropertiesManager();
		String key = checkCode.getFunctionValue();
		String regexp = pm.getValue("check.patterns.properties", key);
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regexp);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		String key1 = checkCode.getFunctionAttribute1();
		String regexp1 = pm.getValue("check.patterns.properties", key1);
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				return !AccesibilityDeclarationCheckUtils.hasRevisionDate(elementGiven.getOwnerDocument(), regexp1);
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasDate = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						hasDate |= AccesibilityDeclarationCheckUtils.hasRevisionDate(document, regexp1);
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return !hasDate;
		}
	}

	/**
	 * Comprueba que la página de accesibilidad contiene la declaración del nivel de conformidad alcanzado, basándose en la búsqueda de patrones. Actualizado para recuperar los patrones de fichero de
	 * propiedades.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAccessibilityDeclarationNoConformanceLevel(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regex);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			Logger.putLog("No hay enlaces: ", Check.class, Logger.LOG_LEVEL_ERROR);
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				return !AccesibilityDeclarationCheckUtils.hasConformanceLevel(elementGiven.getOwnerDocument());
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasConformance = false;
			for (Element accessibilityLink : accessibilityLinks) {
				if (!accessibilityLink.getAttribute("href").toLowerCase().startsWith("javascript") && !accessibilityLink.getAttribute("href").toLowerCase().startsWith("mailto")) {
					try {
						final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
						if (document != null) {
							hasConformance |= AccesibilityDeclarationCheckUtils.hasConformanceLevel(document);
						}
					} catch (Exception e) {
						Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
					}
				}
			}
			return !hasConformance;
		}
	}

	/**
	 * Gets the accesibility document.
	 *
	 * @param elementRoot the element root
	 * @param href        the href
	 * @return the accesibility document
	 * @throws Exception the exception
	 */
	private Document getAccesibilityDocument(final Element elementRoot, final String href) throws Exception {
		try {
			final URI u = new URI(href);
			if (u.isAbsolute()) {
				final Document document;
				if (((HashMap<String, Document>) elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT)).get(href) == null) {
					Logger.putLog("Accediendo a la declaración de accesibilidad en " + href, Check.class, Logger.LOG_LEVEL_INFO);
					document = CheckUtils.getRemoteDocument(href, href, 0);
					((HashMap<String, Document>) elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT)).put(href, document);
				} else {
					document = ((HashMap<String, Document>) elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT)).get(href);
				}
				return document;
			} else {
				return getRelativeDocument(elementRoot, href);
			}
		} catch (Exception e) {
			return getRelativeDocument(elementRoot, href);
		}
	}

	/**
	 * Document to HTML.
	 *
	 * @param document the document
	 * @return the string
	 */
	private String documentToHTML(final Document document) {
		try {
			DOMSource domSource = new DOMSource(document);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the relative document.
	 *
	 * @param elementRoot the element root
	 * @param href        the href
	 * @return the relative document
	 * @throws MalformedURLException the malformed URL exception
	 * @throws IOException           Signals that an I/O exception has occurred.
	 * @throws SAXException          the SAX exception
	 */
	private Document getRelativeDocument(final Element elementRoot, final String href) throws MalformedURLException, IOException, SAXException {
		// final URL documentUrl = CheckUtils.getBaseUrl(elementRoot) != null ? new URL(CheckUtils.getBaseUrl(elementRoot)) : new URL((String) elementRoot.getUserData("url"));
		final URL documentUrl = CheckUtils.getBaseUrl(elementRoot) != null ? new URL(CheckUtils.getBaseUrl(elementRoot)) : new URL(new URL((String) elementRoot.getUserData("url")), href);
		final String remoteUrlStr = new URL(documentUrl, href).toString();
		final Document document;
		if (((HashMap<String, Document>) elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT)).get(remoteUrlStr) == null) {
			Logger.putLog("Accediendo a la declaración de accesibilidad en " + remoteUrlStr, Check.class, Logger.LOG_LEVEL_INFO);
			document = CheckUtils.getRemoteDocument(documentUrl.toString(), remoteUrlStr, 0);
			((HashMap<String, Document>) elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT)).put(remoteUrlStr, document);
		} else {
			document = ((HashMap<String, Document>) elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT)).get(remoteUrlStr);
		}
		return document;
	}

	/**
	 * Function false paragraph list.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionFalseParagraphList(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Pattern pattern = Pattern.compile(checkCode.getFunctionValue(), Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		final int number = Integer.parseInt(checkCode.getFunctionNumber());
		final String elementName = checkCode.getFunctionElement().isEmpty() ? "p" : checkCode.getFunctionElement();
		String previousMatch = null;
		boolean first = true;
		Element checkedElement = elementGiven;
		for (int i = 0; i < number; i++) {
			if (checkedElement != null && checkedElement.getNodeName().equalsIgnoreCase(elementName) && !checkedElement.getParentNode().getNodeName().equalsIgnoreCase("ol")) {
				final String text = EvaluatorUtility.getElementText(checkedElement);
				final Matcher matcher = pattern.matcher(text);
				if (!matcher.find()) {
					return false;
				} else if ("sorted".equals(checkCode.getFunctionAttribute1())) {
					String match = matcher.group(1);
					if (!first) {
						// Para verificar si la lista es ordenada, se comprueba
						// que, a partir del
						// segundo elemento, cada elemento sea igual al anterior
						// más uno. En caso contrario
						// se devuelve falso.
						if ("char".equals(checkCode.getFunctionAttribute2())) {
							if (match.charAt(0) - previousMatch.charAt(0) != 1) {
								return false;
							}
						} else if ("roman".equals(checkCode.getFunctionAttribute2())) {
							match = matcher.group(0);
							if (EvaluatorUtils.romanToDecimal(match) - EvaluatorUtils.romanToDecimal(previousMatch) <= 0) {
								return false;
							}
						} else {
							int order = Integer.parseInt(match);
							int oldOrder = Integer.parseInt(previousMatch);
							if (order != oldOrder + 1) {
								return false;
							}
						}
					} else {
						first = false;
					}
					previousMatch = match;
				}
			} else {
				return false;
			}
			checkedElement = EvaluatorUtils.getNextElement(checkedElement, false);
		}
		return true;
	}

	/**
	 * Function has complex structure.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasComplexStructure(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList paragraphs = elementGiven.getElementsByTagName("p");
		int cont = 0;
		for (int i = 0; i < paragraphs.getLength(); i++) {
			if (paragraphs.item(i).getTextContent().length() >= 80) {
				cont++;
			}
		}
		return cont >= Integer.parseInt(checkCode.getFunctionNumber());
	}

	/**
	 * Buscamos listas falsas de la forma "*uno<br/>
	 * *dos<br/>
	 * *tres" o "<br/>
	 * *uno<br/>
	 * *dos<br/>
	 * *tres". Para ello, buscamos los elementos <br/>
	 * hacia atrás y comprobamos que el texto que les sucede coincida con la expresión regular para ver si tienen formato artificial de lista. En el caso del último elemento, buscaremos el texto que
	 * precede al último elemento <br/>
	 * analizado, ya que el primer elemento de la lista falsa no tiene por qué tener un <br/>
	 * previo.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionFalseBrList(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final Pattern pattern = Pattern.compile(checkCode.getFunctionValue(), Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		final int number = Integer.parseInt(checkCode.getFunctionNumber());
		final PropertiesManager pmgr = new PropertiesManager();
		final List<String> inlineTags = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "inline.tags.list").split(";"));
		boolean first = true;
		int order = 0;
		int oldOrder = 0;
		Element checkedElement = elementGiven;
		for (int i = 0; i < number; i++) {
			if (i < number - 1) {
				// Resto de elementos
				if (checkedElement != null && "br".equalsIgnoreCase(checkedElement.getNodeName())) {
					if (!CheckUtils.isFalseBrNode(checkedElement, inlineTags, pattern, false)) {
						return false;
					} else if (checkCode.getFunctionAttribute1().equals("sorted")) {
						order = getCurrentOrder(CheckUtils.getElementText(checkedElement, false, inlineTags), checkCode.getFunctionValue());
						if (!first) {
							if (order != oldOrder - 1) {
								return false;
							}
						} else {
							first = false;
						}
						oldOrder = order;
					}
				} else {
					return false;
				}
				if (i < number - 2) {
					checkedElement = EvaluatorUtils.getPreviousElement(checkedElement, true);
				}
			} else {
				if (!CheckUtils.isFalseBrNode(checkedElement, inlineTags, pattern, true)) {
					return false;
				} else if (checkCode.getFunctionAttribute1().equals("sorted")) {
					order = getCurrentOrder(CheckUtils.getElementText(checkedElement, true, inlineTags), checkCode.getFunctionValue());
					if (!first) {
						if (order != oldOrder - 1) {
							return false;
						}
					} else {
						first = false;
					}
					oldOrder = order;
				}
			}
		}
		return true;
	}

	/**
	 * Buscamos listas falsas de la forma "<img /> uno<br/>
	 * <img /> dos<br/>
	 * <img /> tres" o "<br/>
	 * <img /> uno<br/>
	 * <img /> dos<br/>
	 * <img /> tres". Para ello, buscamos los elementos <br/>
	 * hacia atrás y comprobamos que el texto que les sucede coincida con la expresión regular para ver si tienen formato artificial de lista. En el caso del último elemento, buscaremos el texto que
	 * precede al último elemento <br/>
	 * analizado, ya que el primer elemento de la lista falsa no tiene por qué tener un <br/>
	 * previo.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true si se detecta un uso incorrecto de br e imágenes (problema de accesibilidad), false en caso contrario
	 */
	private boolean functionFalseBrImageList(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final int number = Integer.parseInt(checkCode.getFunctionNumber());
		Element checkedElement = elementGiven;
		int brs = 1;
		if (checkedElement != null) {
			for (int i = 0; i < number; i++) {
				if (!checkBrImage(checkCode, checkedElement)) {
					return false;
				} else {
					checkedElement = EvaluatorUtils.getPreviousElement(checkedElement, false);
					while (checkedElement != null && !"br".equalsIgnoreCase(checkedElement.getNodeName())) {
						checkedElement = EvaluatorUtils.getPreviousElement(checkedElement, false);
					}
					if (checkedElement == null) {
						checkedElement = (Element) elementGiven.getParentNode();
						final Element firstChild = EvaluatorUtils.getFirstElement(checkedElement, false);
						if ("img".equalsIgnoreCase(firstChild.getNodeName()) && functionImgDimensionsLessThan(checkCode, firstChild, firstChild)) {
							brs++;
						}
						// Comprobamos si se ha alcanzado el número de brs
						// necesarios para considerarlo como error
						return brs == number;
					} else {
						brs++;
					}
				}
			}
		}
		// Comprobamos si se ha alcanzado el número de brs necesarios para
		// considerarlo como error
		return brs == number;
	}

	/**
	 * Check br image.
	 *
	 * @param checkCode      the check code
	 * @param checkedElement the checked element
	 * @return true, if successful
	 */
	private boolean checkBrImage(final CheckCode checkCode, final Element checkedElement) {
		if (checkedElement != null) {
			final Element nextElement = "br".equalsIgnoreCase(checkedElement.getNodeName()) ? EvaluatorUtils.getNextElement(checkedElement, false)
					: EvaluatorUtils.getFirstElement(checkedElement, false);
			return nextElement != null && "img".equalsIgnoreCase(nextElement.getNodeName()) && functionImgDimensionsLessThan(checkCode, nextElement, nextElement);
		} else {
			return false;
		}
	}

	/**
	 * Gets the current order.
	 *
	 * @param text       the text
	 * @param patternStr the pattern str
	 * @return the current order
	 */
	private int getCurrentOrder(final String text, final String patternStr) {
		final Pattern pattern = Pattern.compile(patternStr, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		} else {
			return Integer.MIN_VALUE;
		}
	}

	/**
	 * Function has incorrect tabindex.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionHasIncorrectTabindex(final CheckCode checkCode, final Node nodeNode, final Element elementGiven) {
		final List<Element> elementList = new ArrayList<>();
		elementList.addAll(createElementList(elementGiven.getElementsByTagName("a")));
		elementList.addAll(createElementList(elementGiven.getElementsByTagName("area")));
		elementList.addAll(createElementList(elementGiven.getElementsByTagName("button")));
		elementList.addAll(createElementList(elementGiven.getElementsByTagName("input")));
		elementList.addAll(createElementList(elementGiven.getElementsByTagName("object")));
		elementList.addAll(createElementList(elementGiven.getElementsByTagName("select")));
		elementList.addAll(createElementList(elementGiven.getElementsByTagName("textarea")));
		if (checkCode.getFunctionValue().equalsIgnoreCase(IntavConstants.NONE)) {
			if (functionTabindexAtributte(elementList) == IntavConstants.TABINDEX_NONE) {
				return true;
			}
		} else if (checkCode.getFunctionValue().equalsIgnoreCase(IntavConstants.MANY)) {
			if (functionTabindexAtributte(elementList) == IntavConstants.TABINDEX_MANY) {
				return true;
			}
		} else if (checkCode.getFunctionValue().equalsIgnoreCase(IntavConstants.ALL)) {
			if (functionTabindexAtributte(elementList) == IntavConstants.TABINDEX_ALL) {
				if (!areTabindexOrder(elementList)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Are tabindex order.
	 *
	 * @param elementList the element list
	 * @return true, if successful
	 */
	private boolean areTabindexOrder(List<Element> elementList) {
		final List<Element> ordererList = orderElementsByPosition(elementList);
		int previousTabindex = 0;
		for (Element element : ordererList) {
			if (!(previousTabindex == ((Integer.parseInt(element.getAttribute("tabindex"))) - 1))) {
				return false;
			}
			previousTabindex++;
		}
		return true;
	}

	/**
	 * Order elements by position.
	 *
	 * @param elementList the element list
	 * @return the list
	 */
	private List<Element> orderElementsByPosition(List<Element> elementList) {
		Collections.sort(elementList, new Comparator<Element>() {
			public int compare(Element o1, Element o2) {
				return ((Integer) o1.getUserData(IntavConstants.POSITION)).compareTo(((Integer) o2.getUserData(IntavConstants.POSITION)));
			}
		});
		return elementList;
	}

	/**
	 * Creates the element list.
	 *
	 * @param nodeList the node list
	 * @return the list
	 */
	private List<Element> createElementList(NodeList nodeList) {
		final List<Element> elementList = new ArrayList<>();
		if (nodeList != null) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				elementList.add((Element) nodeList.item(i));
			}
		}
		return elementList;
	}

	/**
	 * Function tabindex atributte.
	 *
	 * @param nodeList the node list
	 * @return the int
	 */
	// La lista tiene elementos con tabindex o no
	private int functionTabindexAtributte(List<Element> nodeList) {
		int countYes = 0;
		int countNo = 0;
		if (nodeList != null && !nodeList.isEmpty()) {
			for (Element element : nodeList) {
				if (element.hasAttribute("tabindex")) {
					countYes++;
				} else {
					countNo++;
				}
			}
			if (countYes == 0 && countNo != 0) {
				return IntavConstants.TABINDEX_NONE;
			} else if (countYes != 0 && countNo == 0) {
				return IntavConstants.TABINDEX_ALL;
			}
			return IntavConstants.TABINDEX_MANY;
		} else {
			return IntavConstants.TABINDEX_NO_ELEMENTS;
		}
	}

	/**
	 * Function correct links.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCorrectLinks(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList elementList = elementGiven.getElementsByTagName("a");
		List<Node> elementHrefList = new ArrayList<>();
		for (int i = 0; i < elementList.getLength(); i++) {
			if (elementList.item(i).getAttributes().getNamedItem("href") != null) {
				elementHrefList.add(elementList.item(i));
			}
		}
		Map<String, List<DestinationPosition>> linksMap = new HashMap<>();
		for (int i = 0; i < elementHrefList.size(); i++) {
			List<DestinationPosition> destination = new ArrayList<>();
			String linkText = EvaluatorUtils.getLinkText((Element) elementHrefList.get(i));
			if (StringUtils.isNotEmpty(linkText) && !StringUtils.textMatchs(linkText, checkCode.getFunctionAttribute1())) {
				if (linksMap.get(linkText) != null) {
					destination = linksMap.get(linkText);
				}
				DestinationPosition dp = new DestinationPosition(elementHrefList.get(i).getAttributes().getNamedItem("href").getNodeValue(), i);
				dp.setDestination(dp.getDestination().replaceAll("/$", "").replaceAll("^\\./", ""));
				if (!destination.contains(dp)) {
					destination.add(dp);
					linksMap.put(linkText, destination);
				}
			}
		}
		for (Map.Entry<String, List<DestinationPosition>> linksEntry : linksMap.entrySet()) {
			for (DestinationPosition dp : linksEntry.getValue()) {
				if (dp.getPosition() > 0) {
					String previousDestination = elementHrefList.get(dp.getPosition() - 1).getAttributes().getNamedItem("href").getNodeValue();
					if (!previousDestination.equals(dp.getDestination())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Function element percentage.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionElementPercentage(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		NodeList elementList = elementGiven.getElementsByTagName(checkCode.getFunctionAttribute1());
		int attrEmptyNum = 0;
		if (elementList != null && elementList.getLength() > 0) {
			for (int i = 0; i < elementList.getLength(); i++) {
				if (elementList.item(i).getAttributes().getNamedItem(checkCode.getFunctionAttribute2()) != null
						&& StringUtils.isEmpty(elementList.item(i).getAttributes().getNamedItem(checkCode.getFunctionAttribute2()).getNodeValue().trim())) {
					attrEmptyNum++;
				}
			}
			BigDecimal percentage = (new BigDecimal(attrEmptyNum)).divide(new BigDecimal(elementList.getLength()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			if (percentage.compareTo(new BigDecimal(checkCode.getFunctionNumber())) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Function child element characters greater than.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionChildElementCharactersGreaterThan(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList elementList = elementGiven.getElementsByTagName(checkCode.getFunctionElement());
		final int charactersNumber = Integer.parseInt(checkCode.getFunctionValue());
		if (elementList != null && elementList.getLength() > 0) {
			for (int i = 0; i < elementList.getLength(); i++) {
				String text = elementList.item(i).getTextContent();
				text = Pattern.compile("[\\n\\r\\t ]{2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(text).replaceAll(" ");
				if (text.length() > charactersNumber) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Function empty section.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionEmptySection(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// Comprobamos si tiene contenido en los hermanos e hijos
		Node nextSimbling = elementGiven.getNextSibling();
		int isEmptyContent = CheckUtils.isEmptyDescendentContent(nextSimbling, elementGiven);
		// Comprobamos si el contenido está en los hermanos del padre
		if (isEmptyContent == IntavConstants.IS_EMPTY) {
			Node parentNode = elementGiven.getParentNode();
			while (parentNode != null) {
				Node parentSimblingNode = parentNode.getNextSibling();
				while (parentSimblingNode != null) {
					isEmptyContent = CheckUtils.isEmptyDescendentContent(parentSimblingNode, elementGiven);
					if (isEmptyContent == IntavConstants.EQUAL_HEADER_TAG) {
						return true;
					} else if (isEmptyContent == IntavConstants.IS_NOT_EMPTY) {
						return false;
					}
					parentSimblingNode = parentSimblingNode.getNextSibling();
				}
				parentNode = parentNode.getParentNode();
			}
			return true;
		} else {
			return isEmptyContent == IntavConstants.EQUAL_HEADER_TAG;
		}
	}

	/**
	 * Function is animated gif.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionIsAnimatedGif(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		URL urlImage = null;
		try {
			URL url = CheckUtils.getBaseUrl(elementRoot) != null ? new URL(CheckUtils.getBaseUrl(elementRoot)) : new URL((String) elementRoot.getUserData("url"));
			urlImage = new URL(url, elementGiven.getAttribute("src"));
			if (elementRoot.getUserData(IntavConstants.FAILED_GIFS_URL) != null && ((ArrayList<String>) elementRoot.getUserData(IntavConstants.FAILED_GIFS_URL)).contains(urlImage.toString())) {
				return false;
			} else {
				ImageReader reader = CheckUtils.getImageReader(elementGiven, urlImage);
				if (reader != null && reader.getNumImages(true) > 1) {
					return true;
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al verificar si es un gif animado: " + e.getMessage(), Check.class, Logger.LOG_LEVEL_ERROR);
			if (elementRoot.getUserData(IntavConstants.FAILED_GIFS_URL) == null) {
				elementRoot.setUserData(IntavConstants.FAILED_GIFS_URL, new ArrayList<String>(), null);
			}
			((ArrayList<String>) elementRoot.getUserData(IntavConstants.FAILED_GIFS_URL)).add(elementGiven.getAttribute("src"));
		}
		return false;
	}

	/**
	 * ***************************************************************************.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	/******************************************************************************/
	/***************
	 * NUEVAS FUNCIONES METODOLOGIA 2012 V2017
	 **********************/
	/******************************************************************************/
	/******************************************************************************/
	/**
	 * Comprobación de la existencia de encabezados marcados WAI-ARIA
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir si no hay encabezados. false en caso de encontrar algún encabezado WAI-ARIA
	 */
	private boolean functionWAIHeadersMissing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean fail = true;
		// Todos los elementos del DOM
		NodeList allElements = elementGiven.getElementsByTagName("*");
		for (int i = 0; i < allElements.getLength(); i++) {
			Element item = (Element) allElements.item(i);
			if (item.hasAttributes()) {
				for (int j = 0; j < item.getAttributes().getLength(); j++) {
					if ("role".equals(item.getAttributes().item(j).getNodeName()) && "heading".equals(item.getAttributes().item(j).getNodeValue())) {
						fail = false;
						break;
					}
				}
			}
		}
		return fail;
	}

	/**
	 * Comprueba si existe encabezado marcado WAI-ARIA de primer nivel.
	 *
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir si existe encabezado de primer nivel. false en caso de encontrar encabezado de nivel 1 WAI-ARIA
	 */
	private boolean functionWAIHeadersLevel1Missing(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean fail = true;
		// Todos los elementos del DOM
		NodeList allElements = elementGiven.getElementsByTagName("*");
		for (int i = 0; i < allElements.getLength(); i++) {
			Element item = (Element) allElements.item(i);
			if (item.hasAttributes()) {
				Node roleAttribute = item.getAttributes().getNamedItem("role");
				Node ariaLevelAttribute = item.getAttributes().getNamedItem("aria-level");
				if (roleAttribute != null && "heading".equals(roleAttribute.getNodeValue()) && ariaLevelAttribute != null && "1".equals(ariaLevelAttribute.getNodeValue())) {
					fail = false;
					break;
				}
			}
		}
		return fail;
	}

	/**
	 * Comprueba si un elemento con el atributo "aria-labelledby" hace referencia a un elemento existente en el documento.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir no se encuentra el elelemento con el id del atributo aria-labelledby. false en caso de encontrar el elemento con el id.
	 */
	private boolean functionAriaLabelledbyReferences(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String ariaLabelledby = elementGiven.getAttribute("aria-labelledby");
		final Document document = elementGiven.getOwnerDocument();
		String[] references = ariaLabelledby.split("\\s");
		boolean fail = true;
		for (int i = 0; i < references.length; i++) {
			Element labelledBy = document.getElementById(references[i]);
			if (labelledBy != null && !StringUtils.normalizeWhiteSpaces(labelledBy.getTextContent()).trim().isEmpty()) {
				fail = false;
				break;
			}
		}
		return fail;
	}

	/**
	 * Comprueba si un elemento con el atributo "aria-describedby" hace referencia a un elemento existente en el documento.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir no se encuentra el elelemento con el id del atributo aria-labelledby. false en caso de encontrar el elemento con el id.
	 */
	private boolean functionAriaDescribedbyReferences(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final String ariaLabelledby = elementGiven.getAttribute("aria-describedby");
		final Document document = elementGiven.getOwnerDocument();
		String[] references = ariaLabelledby.split("\\s");
		boolean fail = true;
		for (int i = 0; i < references.length; i++) {
			Element labelledBy = document.getElementById(references[i]);
			if (labelledBy != null && !StringUtils.normalizeWhiteSpaces(labelledBy.getTextContent()).trim().isEmpty()) {
				fail = false;
				break;
			}
		}
		return fail;
	}

	/**
	 * Comprueba si elemento dado tiene una longitud mayor de 150.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir si la longitud es superior a 150. false en caso contrario.
	 */
	private boolean attributeLength(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean fail = true;
		if (nodeNode == null) {
			return false;
		}
		String stringValue = nodeNode.getNodeValue();
		fail = stringValue.length() > 150;
		return fail;
	}

	/**
	 * Comprueba si el contenido del elemento al que hace referencia el elemento "aria-labelledby" tiene una longiutud superior a 150 caracteres.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir si la longitud es superior a 150. false en caso contrario.
	 */
	private boolean attributeLengthLabeledBy(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean fail = false;
		final String ariaLabelledby = elementGiven.getAttribute("aria-labelledby");
		final Document document = elementGiven.getOwnerDocument();
		String[] references = ariaLabelledby.split("\\s");
		for (int i = 0; i < references.length; i++) {
			Element elementById = document.getElementById(references[i]);
			if (elementById != null) {
				String textContent = getTextContent(elementById);
				if (textContent != null && textContent.length() > 150) {
					fail = true;
					break;
				}
			}
		}
		return fail;
	}

	/**
	 * Comprueba que existe contenido a continación de un encabezado WAI-ARIA y que no lo sigue otro encabezado del mismo nive sin contenido de por medio.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir, si el siguiente elemento es otro encabezado WAI. false en caso contrario.
	 */
	private boolean functionFollowingWAIHeadersWithoutContent(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean fail = false;
		Element nextSibling = EvaluatorUtils.getNextElement(elementGiven, false);
		int ariaLevel = Integer.parseInt(elementGiven.getAttribute("aria-level"));
		if (nextSibling != null && "heading".equals(nextSibling.getAttribute("role")) && nextSibling.getAttribute("aria-level") != null
				&& ariaLevel == Integer.parseInt(nextSibling.getAttribute("aria-level"))) {
			fail = true;
		}
		return fail;
	}

	/**
	 * Comprueba que los encabezados WAI-ARIA que no se saltan niveles.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir, si no se sigue la secuencia. false en caso contrario.
	 */
	private boolean skipWaiHeadersLevel(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		boolean failPrevious = false;
		boolean failNext = false;
		// Comprobar que se sigue el orden
		// Nivel de aria del anterior
		int ariaLevel = Integer.parseInt(elementGiven.getAttribute("aria-level"));
		// Comprobamos que el anterior es del nivel inferior
		// Comprobamos que no existe uno igual o de otro nivel superior
		Element current = elementGiven;
		while ((current = EvaluatorUtils.getPreviousElement(current, false)) != null) {
			// Si el encabezado anterior no es del mismo nivel o de una menos
			// entonces falla (p.e: 2-->2 ok, 2-->4 ko)
			if ("heading".equals(current.getAttribute("role"))) {
				if (current.getAttribute("aria-level") != null && ariaLevel != Integer.parseInt(current.getAttribute("aria-level"))
						&& ariaLevel - 1 != Integer.parseInt(current.getAttribute("aria-level"))) {
					failPrevious = true;
				}
				break;
			}
		}
		// Comprobamos que el siguiente (si existe) es del nivel superior
		current = elementGiven;
		while ((current = EvaluatorUtils.getNextElement(current, false)) != null) {
			// Si el encabezado siguiente no es del mismo nivel o de uno
			// superior
			// entonces falla (p.e: 2-->2 ok, 2-->4 ko)
			if ("heading".equals(current.getAttribute("role"))) {
				if (current.getAttribute("aria-level") != null && ariaLevel != Integer.parseInt(current.getAttribute("aria-level"))
						&& ariaLevel + 1 != Integer.parseInt(current.getAttribute("aria-level"))) {
					failNext = true;
				}
				break; // Ya encontramos el siguiente encabezado y no seguimos
			}
		}
		return (failPrevious || failNext);
	}

	/**
	 * Cuenta el número de elementos existentes con un atributo determinado y comprueba si se supera el máximo indicado.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir, si se supera el número elementos. false en caso contrario
	 */
	private boolean functionElementCountAttributeValue(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final int limit = Integer.parseInt(checkCode.getFunctionNumber());
		final String compare = checkCode.getFunctionPosition().isEmpty() ? "greater" : checkCode.getFunctionPosition();
		int counter = 0;
		// Todos los hijos
		NodeList nodeList = elementGiven.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			if (element.hasAttribute(checkCode.getFunctionAttribute1())) {
				// Si está indicado el attribute2 se comprueba además que el
				// atributo tiene el valor indicado en este campo
				if (!StringUtils.isEmpty(checkCode.getFunctionAttribute2()) && checkCode.getFunctionAttribute2().equals(element.getAttribute(checkCode.getFunctionAttribute1()))) {
					counter++;
				} else {
					counter++;
				}
			}
		}
		if ("greater".equalsIgnoreCase(compare)) {
			// Si la comparación es mayor damos un error si el número de
			// elementos es mayor que el valor indicado
			return counter > limit;
		} else {
			// Si la comparación es menor damos un error si el número de
			// elementos es menor que el valor indicado
			return limit > counter;
		}
	}

	/**
	 * Comprueba si en un documento existe un formulario de contacto. Ampliación de la comprobación de forma de contacto para ver si la página de accesibilidad lo tiene ella misma incluido.
	 * 
	 * @param checkCode    Información del check
	 * @param nodeNode     Nodo
	 * @param elementGiven Elemento
	 * @return true si falla, es decir, si no se consigue detectar el formulario. False en caso contrario.
	 */
	private boolean functionAccessibilityContactForm(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, checkCode.getFunctionValue());
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				return !AccesibilityDeclarationCheckUtils.hasContactExtended(elementGiven.getOwnerDocument(), checkCode.getFunctionAttribute1());
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasContact = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						hasContact |= AccesibilityDeclarationCheckUtils.hasContactExtended(document, checkCode.getFunctionAttribute1());
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return !hasContact;
		}
	}

	/**
	 * Devuelve el texto de un elemento y sus hijos.
	 *
	 * @param element the element
	 * @return the text content
	 */
	private String getTextContent(Element element) {
		NodeList nl = element.getChildNodes();
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			switch (node.getNodeType()) {
			// Si es un elemento, extraemos su texto. P.e.:
			// <strong>texto</strong> --> texto
			case Node.ELEMENT_NODE:
				Element nodeElement = (Element) node;
				if (nodeElement.hasAttribute("alt")) {
					content.append(nodeElement.getAttribute("alt"));
				}
				if (nodeElement.hasAttribute("title")) {
					content.append(nodeElement.getAttribute("title"));
				}
				content.append(getTextContent(nodeElement));
				break;
			case Node.TEXT_NODE:
				content.append(node.getNodeValue());
				break;
			}
		}
		return content.toString().trim();
	}

	/**
	 * Gets the vector code.
	 *
	 * @return the vector code
	 */
	public List<CheckCode> getVectorCode() {
		return vectorCode;
	}

	/**
	 * 
	 * Function attributes are equals.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAttributesAreEquals(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		elementGiven.getAttribute("content");
		String content = elementGiven.getAttributes().getNamedItem("content").getTextContent();
		if (content != null && content.contains("initial-scale") && content.contains("maximum-scale")) {
			String[] attributes = content.split(",");
			String initialValue = "";
			String maxValue = "";
			for (String attribute : attributes) {
				if (attribute.contains("initial-scale")) {
					String[] a1 = attribute.split("=");
					initialValue = a1[1].trim();
				}
				if (attribute.contains("maximum-scale")) {
					String[] a1 = attribute.split("=");
					maxValue = a1[1].trim();
				}
			}
			return !StringUtils.isEmpty(initialValue) && !StringUtils.isEmpty(maxValue) && initialValue.equals(maxValue);
		}
		return false;
	}

	/**
	 * Function label match arial value.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if exists label and text not match
	 */
	private boolean functionLabelMatchArialValue(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (elementGiven.hasAttribute("aria-label") && StringUtils.normalizeWhiteSpaces(elementGiven.getAttribute("aria-label")).trim().isEmpty()) {
			return false;
		} else {
			String arialValue = StringUtils.normalizeWhiteSpaces(elementGiven.getAttribute("aria-label")).trim();
			// check if the input element is contained by a label element
			Element elementParent = DOMUtil.getParent(elementGiven);
			while (elementParent != null) {
				if ("label".equalsIgnoreCase(elementParent.getNodeName())) {
					return false;
				}
				elementParent = DOMUtil.getParent(elementParent);
			}
			// check if the control has an associated label using 'for' and 'id'
			// attributes
			// get the 'id' attribute of the control
			final String stringId = elementGiven.getAttribute("id");
			if (stringId.length() == 0) {
				// control has no 'id' attribute so can't have an associated label
				return false;
			}
			final Document document = elementGiven.getOwnerDocument();
			if (document != null) {
				final NodeList listLabels = document.getElementsByTagName("label");
				// look for a label that has a 'for' attribute value matching the
				// control's id
				for (int x = 0; x < listLabels.getLength(); x++) {
					final Element element = (Element) listLabels.item(x);
					if (element.getAttribute("for").equalsIgnoreCase(stringId) && !StringUtils.normalizeWhiteSpaces(element.getTextContent()).trim().isEmpty()) {
						// found an associated label
						// return (!StringUtils.isEmpty(element.getTextContent()) && !arialValue.contains(StringUtils.normalizeWhiteSpaces(element.getTextContent())));
						return (!StringUtils.isEmpty(element.getTextContent()) && !StringUtils.isEmpty(arialValue)
								&& !arialValue.toLowerCase().contains(StringUtils.normalizeWhiteSpaces(element.getTextContent().toLowerCase())));
					}
				}
				return false;
			} else {
				return false;
			}
		}
	}

	/**
	 * Function autocomplete valid.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAutocompleteValid(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		if (elementGiven.hasAttribute("autocomplete") && StringUtils.normalizeWhiteSpaces(elementGiven.getAttribute("autocomplete")).trim().isEmpty()) {
			return false;
		} else {
			String autocompleteValue = StringUtils.normalizeWhiteSpaces(elementGiven.getAttribute("autocomplete")).trim();
			PropertiesManager pm = new PropertiesManager();
			String[] regexAutocomplete = pm.getValue("check.patterns.properties", "check.481.regex.pattern").split(",");
			// Grid properties
			for (String stringPattern : regexAutocomplete) {
				try {
					Pattern patterAutocomplete = Pattern.compile(stringPattern.replaceAll("\\s+", ""), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					if (patterAutocomplete.matcher(autocompleteValue).find()) {
						return true;
					}
				} catch (Exception e) {
					Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		}
		return false;
	}

	/**
	 * Function accessibility has section.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAccessibilityHasSection(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regex);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				return AccesibilityDeclarationCheckUtils.hasSection(elementGiven.getOwnerDocument(), pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasSection = false;
			// Save accesibility links
			try {
				TAnalisisAccesibilidadDAO.insert(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLinks);
			} catch (Exception e1) {
				Logger.putLog("Error al guardar los links de accesibilidad: ", Check.class, Logger.LOG_LEVEL_ERROR, e1);
			}
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						// Save html
						TAnalisisAccesibilidadDAO.saveDocument(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLink, documentToHTML(document));
						final boolean hasSection2 = AccesibilityDeclarationCheckUtils.hasSection(document, pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
						if (hasSection2) {
							TAnalisisAccesibilidadDAO.incrementCheckOk(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLink);
						}
						hasSection |= hasSection2;
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return hasSection;
		}
	}

	/**
	 * Function section has text.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSectionHasText(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regex);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(), pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
				String textSection = "";
				for (int i = 0; i < elements.size(); i++) {
					textSection += getText(elements.get(i));
				}
				String[] regexAutocomplete = pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2()).split(",");
				for (String stringPattern : regexAutocomplete) {
					try {
						Pattern patterAutocomplete = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
						if (patterAutocomplete.matcher(textSection).find()) {
							return true;
						}
					} catch (Exception e) {
						Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
					}
				}
				return !false;
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasContact = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(),
								pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
						String textSection = "";
						for (int i = 0; i < elements.size(); i++) {
							textSection += getText(elements.get(i));
						}
						String[] regexAutocomplete = pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2()).split(",");
						for (String stringPattern : regexAutocomplete) {
							try {
								Pattern patterAutocomplete = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
								if (patterAutocomplete.matcher(textSection).find()) {
									TAnalisisAccesibilidadDAO.incrementCheckOk(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLink);
									hasContact |= true;
								}
							} catch (Exception e) {
								Logger.putLog("Error al procesar el patrón" + stringPattern, AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
							}
						}
						return true;
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return hasContact;
		}
	}

	/**
	 * Function section has date.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSectionHasRegex(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regex);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(), pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
				String textSection = "";
				for (int i = 0; i < elements.size(); i++) {
					textSection += getText(elements.get(i));
				}
				try {
					Pattern patterAutocomplete = Pattern.compile(pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2()), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
					if (patterAutocomplete.matcher(textSection).find()) {
						return true;
					}
				} catch (Exception e) {
					Logger.putLog("Error al procesar el patrón" + pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2()), AccesibilityDeclarationCheckUtils.class,
							Logger.LOG_LEVEL_ERROR, e);
				}
				return !false;
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasContact = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(),
								pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
						String textSection = "";
						for (int i = 0; i < elements.size(); i++) {
							textSection += getText(elements.get(i));
						}
						try {
							Pattern patterAutocomplete = Pattern.compile(pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2()), Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
							if (patterAutocomplete.matcher(textSection).find()) {
								TAnalisisAccesibilidadDAO.incrementCheckOk(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLink);
								hasContact |= true;
							}
						} catch (Exception e) {
							Logger.putLog("Error al procesar el patrón" + pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2()), AccesibilityDeclarationCheckUtils.class,
									Logger.LOG_LEVEL_ERROR, e);
						}
						return !false;
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return !hasContact;
		}
	}

	/**
	 * Function section mailto.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSectionMailto(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regex);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(), pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
				for (int j = 0; j < elements.size(); j++) {
					Element section = (Element) elements.get(j);
					// Get links ins section
					final NodeList linksC = section.getElementsByTagName("a");
					for (int i = 0; i < linksC.getLength(); i++) {
						try {
							Element link = (Element) linksC.item(i);
							if (link.hasAttribute("href") && !link.getAttribute("href").toLowerCase().startsWith("mailto")) {
								return true;
							}
						} catch (Exception e) {
							Logger.putLog("Error al procesar el patrón" + "mailto", AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
						}
					}
				}
				return false;
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasContact = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(),
								pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
						for (int j = 0; j < elements.size(); j++) {
							Element section = (Element) elements.get(j);
							// Get links ins section
							final NodeList linksC = section.getElementsByTagName("a");
							for (int i = 0; i < linksC.getLength(); i++) {
								try {
									Element link = (Element) linksC.item(i);
									if (link.hasAttribute("href") && !link.getAttribute("href").toLowerCase().startsWith("mailto")) {
										TAnalisisAccesibilidadDAO.incrementCheckOk(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLink);
										hasContact |= true;
									}
								} catch (Exception e) {
									Logger.putLog("Error al procesar el patrón" + "mailto", AccesibilityDeclarationCheckUtils.class, Logger.LOG_LEVEL_ERROR, e);
								}
							}
						}
						return false;
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return !hasContact;
		}
	}

	/**
	 * Function section has form.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionSectionHasElement(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regex);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(), pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
				for (int j = 0; j < elements.size(); j++) {
					Element section = (Element) elements.get(j);
					// Get links ins section
					final NodeList linksC = section.getElementsByTagName(checkCode.getFunctionAttribute2());
					if (linksC.getLength() > 0) {
						return true;
					}
				}
				return false;
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasContact = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(),
								pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1()));
						for (int j = 0; j < elements.size(); j++) {
							Element section = (Element) elements.get(j); // Get links ins section
							final NodeList linksC = section.getElementsByTagName(checkCode.getFunctionAttribute2());
							if (linksC.getLength() > 0) {
								TAnalisisAccesibilidadDAO.incrementCheckOk(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLink);
								hasContact |= true;
							}
						}
						return false;
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return !hasContact;
		}
	}

	/**
	 * Function accesibility year.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionAccesibilityYear(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		final NodeList links = elementGiven.getOwnerDocument().getElementsByTagName("a");
		PropertiesManager pm = new PropertiesManager();
		String regex = pm.getValue("check.patterns.properties", checkCode.getFunctionValue());
		final List<Element> accessibilityLinks = AccesibilityDeclarationCheckUtils.getSectionLink(links, regex);
		final Element elementRoot = elementGiven.getOwnerDocument().getDocumentElement();
		if (elementRoot.getUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT) == null) {
			elementRoot.setUserData(IntavConstants.ACCESSIBILITY_DECLARATION_DOCUMENT, new HashMap<String, Document>(), null);
		}
		if (accessibilityLinks.isEmpty()) {
			// Si no hay enlaces es porque estamos en la página de accesibilidad
			// (en caso contrario falla la comprobacion 126 y no se ejecuta
			// esta)
			try {
				List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(), pm.getValue("check.patterns.properties", checkCode.getFunctionValue()));
				String textSection = "";
				for (int i = 0; i < elements.size(); i++) {
					textSection += getText(elements.get(i));
				}
				String regexDatePrepared = pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1());
				String regexDateReview = pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2());
				Pattern patternPrepared = Pattern.compile(regexDatePrepared, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
				Pattern patternReview = Pattern.compile(regexDateReview, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
				if (patternPrepared.matcher(textSection).find() && patternReview.matcher(textSection).find()) {
					// Get years
					String preparedDate = printMatches(textSection, regexDatePrepared);
					String reviewDate = printMatches(textSection, regexDateReview);
					String preparedYear = printMatches(preparedDate, "\\d{4}");
					String reviewYear = printMatches(reviewDate, "\\d{4}");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
					return sdf.parse(reviewYear).compareTo(sdf.parse(preparedYear)) > 1;
				}
				return false;
			} catch (Exception e) {
				Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return false;
		} else {
			boolean hasContact = false;
			for (Element accessibilityLink : accessibilityLinks) {
				try {
					final Document document = getAccesibilityDocument(elementRoot, accessibilityLink.getAttribute("href"));
					if (document != null) {
						List<Element> elements = AccesibilityDeclarationCheckUtils.getSection(elementGiven.getOwnerDocument(), pm.getValue("check.patterns.properties", checkCode.getFunctionValue()));
						for (int j = 0; j < elements.size(); j++) {
							Element section = (Element) elements.get(j); // Get dates
							String sectionText = getText(section);
							String regexDatePrepared = pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute1());
							String regexDateReview = pm.getValue("check.patterns.properties", checkCode.getFunctionAttribute2());
							Pattern patternPrepared = Pattern.compile(regexDatePrepared, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
							Pattern patternReview = Pattern.compile(regexDateReview, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
							if (patternPrepared.matcher(sectionText).find() && patternReview.matcher(sectionText).find()) {
								// Get years
								String preparedDate = printMatches(sectionText, regexDatePrepared);
								String reviewDate = printMatches(sectionText, regexDatePrepared);
								String preparedYear = printMatches(preparedDate, "\\d{4}");
								String reviewYear = printMatches(reviewDate, "\\d{4}");
								SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
								final boolean dateComparision = sdf.parse(reviewYear).compareTo(sdf.parse(preparedYear)) > 2;
								if (dateComparision) {
									TAnalisisAccesibilidadDAO.incrementCheckOk(DataBaseManager.getConnection(), checkCode.getIdAnalysis(), accessibilityLink);
								}
								hasContact |= dateComparision;
							}
						}
						return false;
					}
				} catch (Exception e) {
					Logger.putLog("Excepción: ", Check.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return !hasContact;
		}
	}

	/**
	 * Function compare header and fieldset.
	 *
	 * @param checkCode    the check code
	 * @param nodeNode     the node node
	 * @param elementGiven the element given
	 * @return true, if successful
	 */
	private boolean functionCompareHeaderAndFieldset(CheckCode checkCode, Node nodeNode, Element elementGiven) {
		// h1,h2,h3,h4,h5,h5
		//
		final List<String> headers = Arrays.asList(new String[] { "h1", "h2", "h3", "h4", "h5", "h6" });
		int numHeaders = 0;
		int numFieldset = 0;
		for (String element : headers) {
			numHeaders += elementGiven.getElementsByTagName(element).getLength();
		}
		numFieldset += elementGiven.getElementsByTagName("fieldset").getLength();
		return numHeaders > numFieldset;
	}

	/**
	 * Prints the matches.
	 *
	 * @param text  the text
	 * @param regex the regex
	 * @return the string
	 */
	public static String printMatches(String text, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		// Check all occurrences
		if (matcher.find()) {
			System.out.print("Start index: " + matcher.start());
			System.out.print(" End index: " + matcher.end());
			System.out.println(" Found: " + matcher.group());
			return matcher.group();
		}
		return "";
	}

	/**
	 * Iterate nodes.
	 *
	 * @param node the node
	 * @return the text
	 */
	private String getText(Node node) {
		// System.out.println("Node: " + node.getNodeName());
		String nodeText = "";
		if (node.hasChildNodes()) {
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node currentode = nodeList.item(i);
				// System.out.println(currentode.getNodeName());
				nodeText += getText(currentode);
			}
		} else {
			nodeText = node.getNodeValue();
		}
		return nodeText;
	}
}
