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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.logging.Logger;

/**
 * The Class CheckCode.
 */
public class CheckCode {
	/** The type. */
	private int type;
	/** The condition type. */
	private int conditionType;
	/** The function id. */
	private int functionId;
	/** The string node relation. */
	private String stringNodeRelation;
	/** The string language. */
	private String stringLanguage;
	/** The string function value. */
	private String stringFunctionValue;
	/** The string function element. */
	private String stringFunctionElement;
	/** The string function number. */
	private String stringFunctionNumber;
	/** The string function attribute 1. */
	private String stringFunctionAttribute1;
	/** The string function attribute 2. */
	private String stringFunctionAttribute2;
	/** The string function position. */
	private String stringFunctionPosition;
	/** The vector code. */
	@XmlTransient
	private List<CheckCode> vectorCode;
	/** The id analysis. */
	private Long idAnalysis;

	/**
	 * Instantiates a new check code.
	 */
	public CheckCode() {
		type = CheckFunctionConstants.CODE_TYPE_NOTSET;
		conditionType = CheckFunctionConstants.CONDITION_NOTSET;
		functionId = CheckFunctionConstants.FUNCTION_NOTSET;
		vectorCode = new ArrayList<>();
		stringNodeRelation = "";
		stringLanguage = "";
		stringFunctionValue = "";
		stringFunctionElement = "";
		stringFunctionNumber = "";
		stringFunctionAttribute1 = "";
		stringFunctionAttribute2 = "";
		stringFunctionPosition = "";
	}

	public String getStringNodeRelation() {
		return stringNodeRelation;
	}

	public void setStringNodeRelation(String stringNodeRelation) {
		this.stringNodeRelation = stringNodeRelation;
	}

	public String getStringLanguage() {
		return stringLanguage;
	}

	public void setStringLanguage(String stringLanguage) {
		this.stringLanguage = stringLanguage;
	}

	public String getStringFunctionValue() {
		return stringFunctionValue;
	}

	public void setStringFunctionValue(String stringFunctionValue) {
		this.stringFunctionValue = stringFunctionValue;
	}

	public String getStringFunctionElement() {
		return stringFunctionElement;
	}

	public void setStringFunctionElement(String stringFunctionElement) {
		this.stringFunctionElement = stringFunctionElement;
	}

	public String getStringFunctionNumber() {
		return stringFunctionNumber;
	}

	public void setStringFunctionNumber(String stringFunctionNumber) {
		this.stringFunctionNumber = stringFunctionNumber;
	}

	public String getStringFunctionAttribute1() {
		return stringFunctionAttribute1;
	}

	public void setStringFunctionAttribute1(String stringFunctionAttribute1) {
		this.stringFunctionAttribute1 = stringFunctionAttribute1;
	}

	public String getStringFunctionAttribute2() {
		return stringFunctionAttribute2;
	}

	public void setStringFunctionAttribute2(String stringFunctionAttribute2) {
		this.stringFunctionAttribute2 = stringFunctionAttribute2;
	}

	public String getStringFunctionPosition() {
		return stringFunctionPosition;
	}

	public void setStringFunctionPosition(String stringFunctionPosition) {
		this.stringFunctionPosition = stringFunctionPosition;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the condition type.
	 *
	 * @return the condition type
	 */
	public int getConditionType() {
		return conditionType;
	}

	/**
	 * Gets the function id.
	 *
	 * @return the function id
	 */
	public int getFunctionId() {
		return functionId;
	}

	/**
	 * Gets the function value.
	 *
	 * @return the function value
	 */
	public String getFunctionValue() {
		return stringFunctionValue;
	}

	/**
	 * Gets the function element.
	 *
	 * @return the function element
	 */
	public String getFunctionElement() {
		return stringFunctionElement;
	}

	/**
	 * Gets the function number.
	 *
	 * @return the function number
	 */
	public String getFunctionNumber() {
		return stringFunctionNumber;
	}

	/**
	 * Gets the function attribute 1.
	 *
	 * @return the function attribute 1
	 */
	public String getFunctionAttribute1() {
		return stringFunctionAttribute1;
	}

	/**
	 * Gets the function attribute 2.
	 *
	 * @return the function attribute 2
	 */
	public String getFunctionAttribute2() {
		return stringFunctionAttribute2;
	}

	/**
	 * Gets the function position.
	 *
	 * @return the function position
	 */
	public String getFunctionPosition() {
		return stringFunctionPosition;
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
	 * Gets the node relation.
	 *
	 * @return the node relation
	 */
	public String getNodeRelation() {
		return stringNodeRelation;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return stringLanguage;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Sets the condition type.
	 *
	 * @param conditionType the new condition type
	 */
	public void setConditionType(int conditionType) {
		this.conditionType = conditionType;
	}

	/**
	 * Sets the function id.
	 *
	 * @param functionId the new function id
	 */
	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	/**
	 * Sets the function value.
	 *
	 * @param stringFunctionValue the new function value
	 */
	public void setFunctionValue(String stringFunctionValue) {
		this.stringFunctionValue = stringFunctionValue;
	}

	/**
	 * Sets the function element.
	 *
	 * @param stringFunctionElement the new function element
	 */
	public void setFunctionElement(String stringFunctionElement) {
		this.stringFunctionElement = stringFunctionElement;
	}

	/**
	 * Sets the function number.
	 *
	 * @param stringFunctionNumber the new function number
	 */
	public void setFunctionNumber(String stringFunctionNumber) {
		this.stringFunctionNumber = stringFunctionNumber;
	}

	/**
	 * Sets the function attribute 1.
	 *
	 * @param stringFunctionAttribute1 the new function attribute 1
	 */
	public void setFunctionAttribute1(String stringFunctionAttribute1) {
		this.stringFunctionAttribute1 = stringFunctionAttribute1;
	}

	/**
	 * Sets the function attribute 2.
	 *
	 * @param stringFunctionAttribute2 the new function attribute 2
	 */
	public void setFunctionAttribute2(String stringFunctionAttribute2) {
		this.stringFunctionAttribute2 = stringFunctionAttribute2;
	}

	/**
	 * Sets the function position.
	 *
	 * @param stringFunctionPosition the new function position
	 */
	public void setFunctionPosition(String stringFunctionPosition) {
		this.stringFunctionPosition = stringFunctionPosition;
	}

	/**
	 * Sets the vector code.
	 *
	 * @param vectorCode the new vector code
	 */
	public void setVectorCode(List<CheckCode> vectorCode) {
		this.vectorCode = vectorCode;
	}

	/**
	 * Sets the node relation.
	 *
	 * @param stringNodeRelation the new node relation
	 */
	public void setNodeRelation(String stringNodeRelation) {
		this.stringNodeRelation = stringNodeRelation;
	}

	/**
	 * Sets the language.
	 *
	 * @param stringLanguage the new language
	 */
	public void setLanguage(String stringLanguage) {
		this.stringLanguage = stringLanguage;
	}

	/**
	 * Gets the id analysis.
	 *
	 * @return the id analysis
	 */
	public Long getIdAnalysis() {
		return idAnalysis;
	}

	/**
	 * Sets the id analysis.
	 *
	 * @param idAnalysis the new id analysis
	 */
	public void setIdAnalysis(Long idAnalysis) {
		this.idAnalysis = idAnalysis;
	}

	/**
	 * Creates the.
	 *
	 * @param elementCode the element code
	 * @return true, if successful
	 */
	public boolean create(Element elementCode) {
		if ("function".equalsIgnoreCase(elementCode.getNodeName())) {
			type = CheckFunctionConstants.CODE_TYPE_FUNCTION;
			String stringNodeAttribute = elementCode.getAttribute("node").trim();
			if (stringNodeAttribute.length() == 0) {
				Logger.putLog("Warning: empty 'node' attribute in function.", CheckCode.class, Logger.LOG_LEVEL_WARNING);
				return false;
			}
			stringNodeRelation = stringNodeAttribute;
			stringFunctionValue = elementCode.getAttribute("value").trim();
			stringFunctionElement = elementCode.getAttribute("element").trim();
			stringFunctionNumber = elementCode.getAttribute("number").trim();
			stringFunctionAttribute1 = elementCode.getAttribute("attribute1").trim();
			stringFunctionAttribute2 = elementCode.getAttribute("attribute2").trim();
			stringFunctionPosition = elementCode.getAttribute("position").trim();
			String stringCall = elementCode.getAttribute("call").toLowerCase();
			if (stringCall.length() == 0) {
				Logger.putLog("Warning: empty 'call' attribute in function.", CheckCode.class, Logger.LOG_LEVEL_WARNING);
				return false;
			}
			if ("not-valid-doctype".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_VALID_DOCTYPE;
			} else if ("text-equals".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_EQUALS;
			} else if ("text-not-equals".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_NOTEQUALS;
			} else if ("attribute-exists".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_EXISTS;
			} else if ("attribute-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_MISSING;
			} else if ("attributes-same".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTES_SAME;
			} else if ("attributes-not-same".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTES_NOT_SAME;
			} else if ("element-count-greater-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_GREATER;
			} else if ("internal-element-count-greater-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_INTERNAL_ELEMENT_COUNT_GREATER;
			} else if ("element-count-less-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_LESS;
			} else if ("element-count-equals".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_EQUALS;
			} else if ("element-count-not-equals".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_NOTEQUALS;
			} else if ("attribute-null".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_NULL;
			} else if ("characters-greater-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CHARS_GREATER;
			} else if ("characters-less-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CHARS_LESS;
			} else if ("link-greater-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LINK_CHARS_GREATER;
			} else if ("number-any".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NUMBER_ANY;
			} else if ("number-less-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NUMBER_LESS_THAN;
			} else if ("number-greater-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NUMBER_GREATER_THAN;
			} else if ("container".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CONTAINER;
			} else if ("container-not".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOTCONTAINER;
			} else if ("text-link-equivalents-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_LINK_EQUIVS_MISSING;
			} else if ("label-not-associated".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LABEL_NOT_ASSOCIATED;
			} else if ("label-incorrectly-associated".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LABEL_INCORRECTLY_ASSOCIATED;
			} else if ("label-no-text".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LABEL_NO_TEXT;
			} else if ("metadata-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_METADATA_MISSING;
			} else if ("d-link-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_DLINK_MISSING;
			} else if ("next-heading-wrong".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NEXT_HEADING_BAD;
			} else if ("previous-heading-wrong".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_PREV_HEADING_BAD;
			} else if ("duplicate-following-headers".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_DUPLICATE_FOLLOWING_HEADERS;
			} else if ("incorrect-heading-structure".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_INCORRECT_HEADING_STRUCTURE;
			} else if ("no-correct-document-structure".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NO_CORRECT_DOCUMENT_STRUCTURE;
			} else if ("headers-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HEADERS_MISSING;
			} else if ("headers-exist".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HEADERS_EXIST;
			} else if ("noscript-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOSCRIPT_MISSING;
			} else if ("noframe-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOFRAME_MISSING;
			} else if ("iframe-has-not-alternative".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IFRAME_HAS_NOT_ALTERNATIVE;
			} else if ("iframe-has-alternative".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IFRAME_HAS_ALTERNATIVE;
			} else if ("noembed-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOEMBED_MISSING;
			} else if ("row-count".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ROW_COUNT;
			} else if ("column-count".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_COL_COUNT;
			} else if ("element-previous".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENT_PREVIOUS;
			} else if ("targets-same".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TARGETS_SAME;
			} else if ("html-content-not".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HTML_CONTENT_NOT;
			} else if ("has-language".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_LANGUAGE;
			} else if ("not-valid-language".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_VALID_LANGUAGE;
			} else if ("multi-radio-no-fieldset".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_MULTIRADIO_NOFIELDSET;
			} else if ("multi-checkbox-no-fieldset".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_MULTICHECKBOX_NOFIELDSET;
			} else if ("luminosity-contrast-ratio".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LUMINOSITY_CONTRAST_RATIO;
			} else if ("wai-ert-color-algorithm".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ERT_COLOR_ALGORITHM;
			} else if ("doctype-attribute-not-equal".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_DOCTYPE_ATTRIBUTE_NOT_EQUAL;
			} else if ("validate".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_VALIDATE;
			} else if ("validate-css".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_VALIDATE_CSS;
			} else if ("table-type".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TABLE_TYPE;
			} else if ("missing-id-headers".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_MISSING_ID_HEADERS;
			} else if ("missing-scope".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_MISSING_SCOPE;
			} else if ("invalid-scope".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_INVALID_SCOPE;
			} else if ("caption-summary-same".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CAPTION_SUMMARY_SAME;
			} else if ("is-only-blanks".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IS_ONLY_BLANKS;
			} else if ("is-empty-element".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IS_EMPTY_ELEMENT;
			} else if ("object-has-not-alternative".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_OBJECT_HAS_NOT_ALTERNATIVE;
			} else if ("applet-has-not-alternative".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_APPLET_HAS_NOT_ALTERNATIVE;
			} else if ("applet-has-alternative".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_APPLET_HAS_ALTERNATIVE;
			} else if ("is-not-only-blanks".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IS_NOT_ONLY_BLANKS;
			} else if ("grammar-lang".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_GRAMMAR_LANG;
			} else if ("not-all-labels".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_ALL_LABELS;
			} else if ("not-valid-url".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_VALID_URL;
			} else if ("table-complex".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TABLE_COMPLEX;
			} else if ("table-heading-complex".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TABLE_HEADING_COMPLEX;
			} else if ("has-not-element-childs".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_NOT_ELEMENT_CHILDS;
			} else if ("has-all-id-headers".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_ALL_ID_HEADERS;
			} else if ("contains".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CONTAINS;
			} else if ("user-data-matchs".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_USER_DATA_MATCHS;
			} else if ("not-user-data-matchs".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_USER_DATA_MATCHS;
			} else if ("contains-not".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CONTAINS_NOT;
			} else if ("check-colors".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CHECK_COLORS;
			} else if ("has-element-into".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_ELEMENT_INTO;
			} else if ("same-following-list".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SAME_FOLLOWING_LIST;
			} else if ("text-contain-general-quote".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_CONTAIN_GENERAL_QUOTE;
			} else if ("same-following-list-not".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SAME_FOLLOWING_LIST_NOT;
			} else if ("definition-list-construction".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_DEFINITION_LIST_CONSTRUCTION;
			} else if ("all-elements-not-like-this".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ALL_ELEMENTS_NOT_LIKE_THIS;
			} else if ("same-element-not".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SAME_ELEMENT_NOT;
			} else if ("same-element".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SAME_ELEMENT;
			} else if ("has-nbsp-entities".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_NBSP_ENTITIES;
			} else if ("not-is-only-blanks".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_IS_ONLY_BLANKS;
			} else if ("link-same-page".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LINK_SAME_PAGE;
			} else if ("num-more-controls".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NUM_MORE_CONTROLS;
			} else if ("text-match".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_MATCH;
			} else if ("text-not-match".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_NOT_MATCH;
			} else if ("attribute-element-text-match".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_ELEMENT_TEXT_MATCH;
			} else if ("attribute-element-text-not-match".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_ELEMENT_TEXT_NOT_MATCH;
			} else if ("is-odd".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IS_ODD;
			} else if ("is-even".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IS_EVEN;
			} else if ("not-children-have-attribute".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_CHILDREN_HAVE_ATTRIBUTE;
			} else if ("not-clear-language".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_CLEAR_LANGUAGE;
			} else if ("has-not-enough-text".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_NOT_ENOUGH_TEXT;
			} else if ("not-correct-heading".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_CORRECT_HEADING;
			} else if ("has-not-section-link".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_NOT_SECTION_LINK;
			} else if ("false-paragraph-list".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FALSE_PARAGRAPH_LIST;
			} else if ("false-br-list".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FALSE_BR_LIST;
			} else if ("false-header-with-only-cell".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FALSE_HEADER_WITH_ONLY_CELL;
			} else if ("has-incorrect-tabindex".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_INCORRECT_TABINDEX;
			} else if ("language-not-equals".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LANGUAGE_NOT_EQUALS;
			} else if ("empty-elements".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_EMPTY_ELEMENTS;
			} else if ("elements-excessive-usage".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENTS_EXCESSIVE_USAGE;
			} else if ("attributes-excessive-usage".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTES_EXCESSIVE_USAGE;
			} else if ("tabindex-excessive-usage".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TABINDEX_EXCESSIVE_USAGE;
			} else if ("element-percentage".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENT_PERCENTAGE;
			} else if ("correct-links".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CORRECT_LINKS;
			} else if ("child-element-characters-greater-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CHILD_ELEMENT_CHARS_GREATER;
			} else if ("child-element-characters-lesser-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CHILD_ELEMENT_CHARS_LESSER;
			} else if ("layout-table".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LAYOUT_TABLE;
			} else if ("layout-table-number".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LAYOUT_TABLE_NUMBER;
			} else if ("not-layout-table".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_LAYOUT_TABLE;
			} else if ("not-external-url".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_EXTERNAL_URL;
			} else if ("accessibility-declaration-not-contact".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONTACT;
			} else if ("accessibility-declaration-not-revision-date".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_REVISION_DATE;
			} else if ("accessibility-declaration-not-conformance-level".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONFORMANCE_LEVEL;
			} else if ("has-complex-structure".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_COMPLEX_STRUCTURE;
			} else if ("too-many-broken-links".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TOO_MANY_BROKEN_LINKS;
			} else if ("exist-attribute-value".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_EXIST_ATTRIBUTE_VALUE;
			} else if ("not-exist-attribute-value".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_EXIST_ATTRIBUTE_VALUE;
			} else if ("count-attribute-value-greater-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_COUNT_ATTRIBUTE_VALUE_GREATER_THAN;
			} else if ("empty-section".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_EMPTY_SECTION;
			} else if ("is-animated-gif".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IS_ANIMATED_GIF;
			} else if ("following-headers-without-content".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FOLLOWING_HEADERS_WITHOUT_CONTENT;
			} else if ("img-dimensions-less-than".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_IMG_DIMENSIONS_LESS_THAN;
			} else if ("redundant-img-alt".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_REDUNDANT_IMG_ALT;
			} else if ("table-columns".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TABLE_COLUMNS;
			} else if ("table-rows".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TABLE_ROWS;
			} else if ("has-validation-errors".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_VALIDATION_ERRORS;
			} else if ("guess-language".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_GUESS_LANGUAGE;
			} else if ("grouped-selection-buttons".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_GROUPED_SELECTION_BUTTONS;
			} else if ("not-first-child".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_NOT_FIRST_CHILD;
			} else if ("required-controls".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_REQUIRED_CONTROLS;
			} else if ("css-generated-content".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CSS_GENERATED_CONTENT;
			} else if ("css-color-contrast".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CSS_COLOR_CONTRAST;
			} else if ("css-blink".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CSS_BLINK;
			} else if ("css-parseable".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CSS_PARSEABLE;
			} else if ("css-outline".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CSS_OUTLINE;
			} else if ("css-label-hidden".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CSS_LABEL_HIDDEN;
			} else if ("false-br-image-list".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FALSE_BR_IMAGE_LIST;
			} else if ("other-language".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_OTHER_LANGUAGE;
			} else if ("current-language".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_CURRENT_LANGUAGE;
			} else if ("table-heading-blank".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TABLE_HEADING_BLANK;
			} else if ("title-not-contains".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TITLE_NOT_CONTAINS;
			} else if ("previous-sibling-no-header".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_PREV_HEADING_BAD;
			} else if ("aria-labelledby-ids-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ARIA_LABELLEDBY_REFERENCED;
			} else if ("attribute-length".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_LENGHT;
			} else if ("attribute-labelledby-length".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_LABELEDBY_LENGHT;
			} else if ("headers-wai-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HEADERS_WAI_MISSING;
			} else if ("headers-wai-level-1-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HEADERS_WAI_LEVEL_1_MISSING;
			} else if ("following-wai-headers-without-content".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FOLLOWING_WAI_HEADERS_WITHOUT_CONTENT;
			} else if ("skip-wai-headers-level".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SKIP_WAI_HEADERS_LEVEL;
			} else if ("element-count-attribute-value".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_ATTRIBUTE_VALUE;
			} else if ("accessibility-contact-form".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ACCESSIBILITY_CONTACT_FORM;
			} else if ("aria-describedby-ids-missing".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ARIA_DESCRIBEDBY_REFERENCED;
			} else if ("text-match-properties".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_MATCH_PROPERTIES;
			} else if ("text-not-match-properties".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_NOT_MATCH_PROPERTIES;
			} else if ("text-not-equals-properties".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_TEXT_NOT_EQUALS_PROPERTIES;
			} else if ("attributes-are-equals".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTTES_EQUALS;
			} else if ("label-match-arial-value".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_LABEL_MATCH_ARIAL_VALUE;
			} else if ("force-spacing-css".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FORCE_SPACING_CSS;
			} else if ("has-responsive-tags".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_RESPONSIVE_TAGS;
			} else if ("force-trnasform".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_FORCE_TRANSFORM;
			} else if ("autocomplete-valid".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_AUTOCOMPLETE_VALID;
			} else if ("has-section".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_HAS_SECTION;
			} else if ("section-has-text".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SECTION_HAS_TEXT;
			} else if ("section-has-mailto".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SECTION_HAS_MAILTO;
			} else if ("section-has-element".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SECTION_HAS_ELEMENT;
			} else if ("accesibility-date-year".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_ACCESIBILITY_YEAR;
			} else if ("section-has-date".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SECTION_HAS_DATE;
			} else if ("section-has-phone".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_SECTION_HAS_PHONE;
			} else if ("empty-table".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_EMPTY_TABLE_70;
			} else if ("more-header-than-fieldset".equals(stringCall)) {
				functionId = CheckFunctionConstants.FUNCTION_MORE_HEADERS_THAN_FIELDSETS;
			} else {
				Logger.putLog("Warning: unknown function: " + stringCall, CheckCode.class, Logger.LOG_LEVEL_WARNING);
				return false;
			}
			return true;
		} else if ("condition".equalsIgnoreCase(elementCode.getNodeName())) {
			type = CheckFunctionConstants.CODE_TYPE_CONDITION;
			String stringType = elementCode.getAttribute("type");
			if ("and".equalsIgnoreCase(stringType)) {
				conditionType = CheckFunctionConstants.CONDITION_AND;
			} else if ("or".equalsIgnoreCase(stringType)) {
				conditionType = CheckFunctionConstants.CONDITION_OR;
			} else {
				Logger.putLog("Warning: invalid condition type: " + stringType, CheckCode.class, Logger.LOG_LEVEL_WARNING);
				return false;
			}
			// get all the functions contained by the condition
			NodeList listFunctions = elementCode.getChildNodes();
			for (int x = 0; x < listFunctions.getLength(); x++) {
				if (listFunctions.item(x).getNodeType() == Node.ELEMENT_NODE) {
					CheckCode checkCode = new CheckCode();
					if (checkCode.create((Element) listFunctions.item(x))) {
						vectorCode.add(checkCode);
					}
				}
			}
			return true;
		} else if ("language".equalsIgnoreCase(elementCode.getNodeName())) {
			type = CheckFunctionConstants.CODE_TYPE_LANGUAGE;
			stringLanguage = elementCode.getAttribute("value");
			// get all the functions contained by the language
			NodeList listFunctions = elementCode.getChildNodes();
			for (int x = 0; x < listFunctions.getLength(); x++) {
				if (listFunctions.item(x).getNodeType() == Node.ELEMENT_NODE) {
					CheckCode checkCode = new CheckCode();
					if (checkCode.create((Element) listFunctions.item(x))) {
						vectorCode.add(checkCode);
					}
				}
			}
			return true;
		}
		return false;
	}
}