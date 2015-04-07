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


import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class CheckCode {


    private int type;
    private int conditionType;
    private int functionId;
    private String stringNodeRelation;
    private String stringLanguage;
    private String stringFunctionValue;
    private String stringFunctionElement;
    private String stringFunctionNumber;
    private String stringFunctionAttribute1;
    private String stringFunctionAttribute2;
    private String stringFunctionPosition;
    private List<CheckCode> vectorCode;

    public CheckCode() {
        type = CheckFunctionConstants.CODE_TYPE_NOTSET;
        conditionType = CheckFunctionConstants.CONDITION_NOTSET;
        functionId = CheckFunctionConstants.FUNCTION_NOTSET;
        vectorCode = new ArrayList<CheckCode>();
        stringNodeRelation = "";
        stringLanguage = "";
        stringFunctionValue = "";
        stringFunctionElement = "";
        stringFunctionNumber = "";
        stringFunctionAttribute1 = "";
        stringFunctionAttribute2 = "";
        stringFunctionPosition = "";
    }

    public int getType() {
        return type;
    }

    public int getConditionType() {
        return conditionType;
    }

    public int getFunctionId() {
        return functionId;
    }

    public String getFunctionValue() {
        return stringFunctionValue;
    }

    public String getFunctionElement() {
        return stringFunctionElement;
    }

    public String getFunctionNumber() {
        return stringFunctionNumber;
    }

    public String getFunctionAttribute1() {
        return stringFunctionAttribute1;
    }

    public String getFunctionAttribute2() {
        return stringFunctionAttribute2;
    }

    public String getFunctionPosition() {
        return stringFunctionPosition;
    }

    public List<CheckCode> getVectorCode() {
        return vectorCode;
    }

    public String getNodeRelation() {
        return stringNodeRelation;
    }

    public String getLanguage() {
        return stringLanguage;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setConditionType(int conditionType) {
        this.conditionType = conditionType;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public void setFunctionValue(String stringFunctionValue) {
        this.stringFunctionValue = stringFunctionValue;
    }

    public void setFunctionElement(String stringFunctionElement) {
        this.stringFunctionElement = stringFunctionElement;
    }

    public void setFunctionNumber(String stringFunctionNumber) {
        this.stringFunctionNumber = stringFunctionNumber;
    }

    public void setFunctionAttribute1(String stringFunctionAttribute1) {
        this.stringFunctionAttribute1 = stringFunctionAttribute1;
    }

    public void setFunctionAttribute2(String stringFunctionAttribute2) {
        this.stringFunctionAttribute2 = stringFunctionAttribute2;
    }

    public void setFunctionPosition(String stringFunctionPosition) {
        this.stringFunctionPosition = stringFunctionPosition;
    }

    public void setVectorCode(List<CheckCode> vectorCode) {
        this.vectorCode = vectorCode;
    }

    public void setNodeRelation(String stringNodeRelation) {
        this.stringNodeRelation = stringNodeRelation;
    }

    public void setLanguage(String stringLanguage) {
        this.stringLanguage = stringLanguage;
    }

    public boolean create(Element elementCode) {
        if (elementCode.getNodeName().equalsIgnoreCase("function")) {
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

            if (stringCall.equals("not-valid-doctype")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_VALID_DOCTYPE;
            } else if (stringCall.equals("text-equals")) {
                functionId = CheckFunctionConstants.FUNCTION_TEXT_EQUALS;
            } else if (stringCall.equals("text-not-equals")) {
                functionId = CheckFunctionConstants.FUNCTION_TEXT_NOTEQUALS;
            } else if (stringCall.equals("attribute-exists")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_EXISTS;
            } else if (stringCall.equals("attribute-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_MISSING;
            } else if (stringCall.equals("attributes-same")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTES_SAME;
            } else if (stringCall.equals("attributes-not-same")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTES_NOT_SAME;
            } else if (stringCall.equals("element-count-greater-than")) {
                functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_GREATER;
            } else if (stringCall.equals("internal-element-count-greater-than")) {
                functionId = CheckFunctionConstants.FUNCTION_INTERNAL_ELEMENT_COUNT_GREATER;
            } else if (stringCall.equals("element-count-less-than")) {
                functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_LESS;
            } else if (stringCall.equals("element-count-equals")) {
                functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_EQUALS;
            } else if (stringCall.equals("element-count-not-equals")) {
                functionId = CheckFunctionConstants.FUNCTION_ELEMENT_COUNT_NOTEQUALS;
            } else if (stringCall.equals("attribute-null")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_NULL;
            } else if (stringCall.equals("characters-greater-than")) {
                functionId = CheckFunctionConstants.FUNCTION_CHARS_GREATER;
            } else if (stringCall.equals("characters-less-than")) {
                functionId = CheckFunctionConstants.FUNCTION_CHARS_LESS;
            } else if (stringCall.equals("link-greater-than")) {
                functionId = CheckFunctionConstants.FUNCTION_LINK_CHARS_GREATER;
            } else if (stringCall.equals("number-any")) {
                functionId = CheckFunctionConstants.FUNCTION_NUMBER_ANY;
            } else if (stringCall.equals("number-less-than")) {
                functionId = CheckFunctionConstants.FUNCTION_NUMBER_LESS_THAN;
            } else if (stringCall.equals("number-greater-than")) {
                functionId = CheckFunctionConstants.FUNCTION_NUMBER_GREATER_THAN;
            } else if (stringCall.equals("container")) {
                functionId = CheckFunctionConstants.FUNCTION_CONTAINER;
            } else if (stringCall.equals("container-not")) {
                functionId = CheckFunctionConstants.FUNCTION_NOTCONTAINER;
            } else if (stringCall.equals("text-link-equivalents-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_TEXT_LINK_EQUIVS_MISSING;
            } else if (stringCall.equals("label-not-associated")) {
                functionId = CheckFunctionConstants.FUNCTION_LABEL_NOT_ASSOCIATED;
            } else if (stringCall.equals("label-incorrectly-associated")) {
                functionId = CheckFunctionConstants.FUNCTION_LABEL_INCORRECTLY_ASSOCIATED;
            } else if (stringCall.equals("label-no-text")) {
                functionId = CheckFunctionConstants.FUNCTION_LABEL_NO_TEXT;
            } else if (stringCall.equals("metadata-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_METADATA_MISSING;
            } else if (stringCall.equals("d-link-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_DLINK_MISSING;
            } else if (stringCall.equals("next-heading-wrong")) {
                functionId = CheckFunctionConstants.FUNCTION_NEXT_HEADING_BAD;
            } else if (stringCall.equals("previous-heading-wrong")) {
                functionId = CheckFunctionConstants.FUNCTION_PREV_HEADING_BAD;
            } else if (stringCall.equals("duplicate-following-headers")) {
                functionId = CheckFunctionConstants.FUNCTION_DUPLICATE_FOLLOWING_HEADERS;
            } else if (stringCall.equals("incorrect-heading-structure")) {
                functionId = CheckFunctionConstants.FUNCTION_INCORRECT_HEADING_STRUCTURE;
            } else if (stringCall.equals("no-correct-document-structure")) {
                functionId = CheckFunctionConstants.FUNCTION_NO_CORRECT_DOCUMENT_STRUCTURE;
            } else if (stringCall.equals("headers-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_HEADERS_MISSING;
            } else if (stringCall.equals("headers-exist")) {
                functionId = CheckFunctionConstants.FUNCTION_HEADERS_EXIST;
            } else if (stringCall.equals("noscript-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_NOSCRIPT_MISSING;
            } else if (stringCall.equals("noframe-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_NOFRAME_MISSING;
            } else if (stringCall.equals("iframe-has-not-alternative")) {
                functionId = CheckFunctionConstants.FUNCTION_IFRAME_HAS_NOT_ALTERNATIVE;
            } else if (stringCall.equals("iframe-has-alternative")) {
                functionId = CheckFunctionConstants.FUNCTION_IFRAME_HAS_ALTERNATIVE;
            } else if (stringCall.equals("noembed-missing")) {
                functionId = CheckFunctionConstants.FUNCTION_NOEMBED_MISSING;
            } else if (stringCall.equals("row-count")) {
                functionId = CheckFunctionConstants.FUNCTION_ROW_COUNT;
            } else if (stringCall.equals("column-count")) {
                functionId = CheckFunctionConstants.FUNCTION_COL_COUNT;
            } else if (stringCall.equals("element-previous")) {
                functionId = CheckFunctionConstants.FUNCTION_ELEMENT_PREVIOUS;
            } else if (stringCall.equals("targets-same")) {
                functionId = CheckFunctionConstants.FUNCTION_TARGETS_SAME;
            } else if (stringCall.equals("html-content-not")) {
                functionId = CheckFunctionConstants.FUNCTION_HTML_CONTENT_NOT;
            } else if (stringCall.equals("has-language")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_LANGUAGE;
            } else if (stringCall.equals("not-valid-language")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_VALID_LANGUAGE;
            } else if (stringCall.equals("multi-radio-no-fieldset")) {
                functionId = CheckFunctionConstants.FUNCTION_MULTIRADIO_NOFIELDSET;
            } else if (stringCall.equals("multi-checkbox-no-fieldset")) {
                functionId = CheckFunctionConstants.FUNCTION_MULTICHECKBOX_NOFIELDSET;
            } else if (stringCall.equals("luminosity-contrast-ratio")) {
                functionId = CheckFunctionConstants.FUNCTION_LUMINOSITY_CONTRAST_RATIO;
            } else if (stringCall.equals("wai-ert-color-algorithm")) {
                functionId = CheckFunctionConstants.FUNCTION_ERT_COLOR_ALGORITHM;
            } else if (stringCall.equals("doctype-attribute-not-equal")) {
                functionId = CheckFunctionConstants.FUNCTION_DOCTYPE_ATTRIBUTE_NOT_EQUAL;
            } else if (stringCall.equals("validate")) {
                functionId = CheckFunctionConstants.FUNCTION_VALIDATE;
            } else if (stringCall.equals("validate-css")) {
                functionId = CheckFunctionConstants.FUNCTION_VALIDATE_CSS;
            } else if (stringCall.equals("table-type")) {
                functionId = CheckFunctionConstants.FUNCTION_TABLE_TYPE;
            } else if (stringCall.equals("missing-id-headers")) {
                functionId = CheckFunctionConstants.FUNCTION_MISSING_ID_HEADERS;
            } else if (stringCall.equals("missing-scope")) {
                functionId = CheckFunctionConstants.FUNCTION_MISSING_SCOPE;
            } else if (stringCall.equals("invalid-scope")) {
                functionId = CheckFunctionConstants.FUNCTION_INVALID_SCOPE;
            } else if (stringCall.equals("caption-summary-same")) {
                functionId = CheckFunctionConstants.FUNCTION_CAPTION_SUMMARY_SAME;
            } else if (stringCall.equals("is-only-blanks")) {
                functionId = CheckFunctionConstants.FUNCTION_IS_ONLY_BLANKS;
            } else if (stringCall.equals("is-empty-element")) {
                functionId = CheckFunctionConstants.FUNCTION_IS_EMPTY_ELEMENT;
            } else if (stringCall.equals("object-has-not-alternative")) {
                functionId = CheckFunctionConstants.FUNCTION_OBJECT_HAS_NOT_ALTERNATIVE;
            } else if (stringCall.equals("applet-has-not-alternative")) {
                functionId = CheckFunctionConstants.FUNCTION_APPLET_HAS_NOT_ALTERNATIVE;
            } else if (stringCall.equals("applet-has-alternative")) {
                functionId = CheckFunctionConstants.FUNCTION_APPLET_HAS_ALTERNATIVE;
            } else if (stringCall.equals("is-not-only-blanks")) {
                functionId = CheckFunctionConstants.FUNCTION_IS_NOT_ONLY_BLANKS;
            } else if (stringCall.equals("grammar-lang")) {
                functionId = CheckFunctionConstants.FUNCTION_GRAMMAR_LANG;
            } else if (stringCall.equals("not-all-labels")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_ALL_LABELS;
            } else if (stringCall.equals("not-valid-url")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_VALID_URL;
            } else if (stringCall.equals("table-heading-complex")) {
                functionId = CheckFunctionConstants.FUNCTION_TABLE_HEADING_COMPLEX;
            } else if (stringCall.equals("has-not-element-childs")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_NOT_ELEMENT_CHILDS;
            } else if (stringCall.equals("has-all-id-headers")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_ALL_ID_HEADERS;
            } else if (stringCall.equals("contains")) {
                functionId = CheckFunctionConstants.FUNCTION_CONTAINS;
            } else if (stringCall.equals("user-data-matchs")) {
                functionId = CheckFunctionConstants.FUNCTION_USER_DATA_MATCHS;
            } else if (stringCall.equals("not-user-data-matchs")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_USER_DATA_MATCHS;
            } else if (stringCall.equals("contains-not")) {
                functionId = CheckFunctionConstants.FUNCTION_CONTAINS_NOT;
            } else if (stringCall.equals("check-colors")) {
                functionId = CheckFunctionConstants.FUNCTION_CHECK_COLORS;
            } else if (stringCall.equals("has-element-into")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_ELEMENT_INTO;
            } else if (stringCall.equals("same-following-list")) {
                functionId = CheckFunctionConstants.FUNCTION_SAME_FOLLOWING_LIST;
            } else if (stringCall.equals("text-contain-general-quote")) {
                functionId = CheckFunctionConstants.FUNCTION_TEXT_CONTAIN_GENERAL_QUOTE;
            } else if (stringCall.equals("same-following-list-not")) {
                functionId = CheckFunctionConstants.FUNCTION_SAME_FOLLOWING_LIST_NOT;
            } else if (stringCall.equals("definition-list-construction")) {
                functionId = CheckFunctionConstants.FUNCTION_DEFINITION_LIST_CONSTRUCTION;
            } else if (stringCall.equals("all-elements-not-like-this")) {
                functionId = CheckFunctionConstants.FUNCTION_ALL_ELEMENTS_NOT_LIKE_THIS;
            } else if (stringCall.equals("same-element-not")) {
                functionId = CheckFunctionConstants.FUNCTION_SAME_ELEMENT_NOT;
            } else if (stringCall.equals("same-element")) {
                functionId = CheckFunctionConstants.FUNCTION_SAME_ELEMENT;
            } else if (stringCall.equals("has-nbsp-entities")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_NBSP_ENTITIES;
            } else if (stringCall.equals("not-is-only-blanks")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_IS_ONLY_BLANKS;
            } else if (stringCall.equals("link-same-page")) {
                functionId = CheckFunctionConstants.FUNCTION_LINK_SAME_PAGE;
            } else if (stringCall.equals("num-more-controls")) {
                functionId = CheckFunctionConstants.FUNCTION_NUM_MORE_CONTROLS;
            } else if (stringCall.equals("text-match")) {
                functionId = CheckFunctionConstants.FUNCTION_TEXT_MATCH;
            } else if (stringCall.equals("text-not-match")) {
                functionId = CheckFunctionConstants.FUNCTION_TEXT_NOT_MATCH;
            } else if (stringCall.equals("attribute-element-text-match")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_ELEMENT_TEXT_MATCH;
            } else if (stringCall.equals("attribute-element-text-not-match")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTE_ELEMENT_TEXT_NOT_MATCH;
            } else if (stringCall.equals("is-odd")) {
                functionId = CheckFunctionConstants.FUNCTION_IS_ODD;
            } else if (stringCall.equals("is-even")) {
                functionId = CheckFunctionConstants.FUNCTION_IS_EVEN;
            } else if (stringCall.equals("not-children-have-attribute")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_CHILDREN_HAVE_ATTRIBUTE;
            } else if (stringCall.equals("not-clear-language")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_CLEAR_LANGUAGE;
            } else if (stringCall.equals("has-not-enough-text")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_NOT_ENOUGH_TEXT;
            } else if (stringCall.equals("not-correct-heading")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_CORRECT_HEADING;
            } else if (stringCall.equals("has-not-section-link")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_NOT_SECTION_LINK;
            } else if (stringCall.equals("false-paragraph-list")) {
                functionId = CheckFunctionConstants.FUNCTION_FALSE_PARAGRAPH_LIST;
            } else if (stringCall.equals("false-paragraph-list")) {
                functionId = CheckFunctionConstants.FUNCTION_FALSE_PARAGRAPH_LIST;
            } else if (stringCall.equals("false-br-list")) {
                functionId = CheckFunctionConstants.FUNCTION_FALSE_BR_LIST;
            } else if (stringCall.equals("false-header-with-only-cell")) {
                functionId = CheckFunctionConstants.FUNCTION_FALSE_HEADER_WITH_ONLY_CELL;
            } else if (stringCall.equals("has-incorrect-tabindex")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_INCORRECT_TABINDEX;
            } else if (stringCall.equals("language-not-equals")) {
                functionId = CheckFunctionConstants.FUNCTION_LANGUAGE_NOT_EQUALS;
            } else if (stringCall.equals("empty-elements")) {
                functionId = CheckFunctionConstants.FUNCTION_EMPTY_ELEMENTS;
            } else if (stringCall.equals("elements-excessive-usage")) {
                functionId = CheckFunctionConstants.FUNCTION_ELEMENTS_EXCESSIVE_USAGE;
            } else if (stringCall.equals("attributes-excessive-usage")) {
                functionId = CheckFunctionConstants.FUNCTION_ATTRIBUTES_EXCESSIVE_USAGE;
            } else if (stringCall.equals("tabindex-excessive-usage")) {
                functionId = CheckFunctionConstants.FUNCTION_TABINDEX_EXCESSIVE_USAGE;
            } else if (stringCall.equals("element-percentage")) {
                functionId = CheckFunctionConstants.FUNCTION_ELEMENT_PERCENTAGE;
            } else if (stringCall.equals("correct-links")) {
                functionId = CheckFunctionConstants.FUNCTION_CORRECT_LINKS;
            } else if (stringCall.equals("child-element-characters-greater-than")) {
                functionId = CheckFunctionConstants.FUNCTION_CHILD_ELEMENT_CHARS_GREATER;
            } else if (stringCall.equals("child-element-characters-lesser-than")) {
                functionId = CheckFunctionConstants.FUNCTION_CHILD_ELEMENT_CHARS_LESSER;
            } else if (stringCall.equals("layout-table")) {
                functionId = CheckFunctionConstants.FUNCTION_LAYOUT_TABLE;
            } else if (stringCall.equals("layout-table-number")) {
                functionId = CheckFunctionConstants.FUNCTION_LAYOUT_TABLE_NUMBER;
            } else if (stringCall.equals("not-layout-table")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_LAYOUT_TABLE;
            } else if (stringCall.equals("not-external-url")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_EXTERNAL_URL;
            } else if (stringCall.equals("accessibility-declaration-not-contact")) {
                functionId = CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONTACT;
            } else if (stringCall.equals("accessibility-declaration-not-revision-date")) {
                functionId = CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_REVISION_DATE;
            } else if (stringCall.equals("accessibility-declaration-not-conformance-level")) {
                functionId = CheckFunctionConstants.FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONFORMANCE_LEVEL;
            } else if (stringCall.equals("has-complex-structure")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_COMPLEX_STRUCTURE;
            } else if (stringCall.equals("too-many-broken-links")) {
                functionId = CheckFunctionConstants.FUNCTION_TOO_MANY_BROKEN_LINKS;
            } else if (stringCall.equals("exist-attribute-value")) {
                functionId = CheckFunctionConstants.FUNCTION_EXIST_ATTRIBUTE_VALUE;
            } else if (stringCall.equals("not-exist-attribute-value")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_EXIST_ATTRIBUTE_VALUE;
            } else if (stringCall.equals("count-attribute-value-greater-than")) {
                functionId = CheckFunctionConstants.FUNCTION_COUNT_ATTRIBUTE_VALUE_GREATER_THAN;
            } else if (stringCall.equals("empty-section")) {
                functionId = CheckFunctionConstants.FUNCTION_EMPTY_SECTION;
            } else if (stringCall.equals("is-animated-gif")) {
                functionId = CheckFunctionConstants.FUNCTION_IS_ANIMATED_GIF;
            } else if (stringCall.equals("following-headers-without-content")) {
                functionId = CheckFunctionConstants.FUNCTION_FOLLOWING_HEADERS_WITHOUT_CONTENT;
            } else if (stringCall.equals("img-dimensions-less-than")) {
                functionId = CheckFunctionConstants.FUNCTION_IMG_DIMENSIONS_LESS_THAN;
            } else if (stringCall.equals("redundant-img-alt")) {
                functionId = CheckFunctionConstants.FUNCTION_REDUNDANT_IMG_ALT;
            } else if (stringCall.equals("table-columns")) {
                functionId = CheckFunctionConstants.FUNCTION_TABLE_COLUMNS;
            } else if (stringCall.equals("table-rows")) {
                functionId = CheckFunctionConstants.FUNCTION_TABLE_ROWS;
            } else if (stringCall.equals("has-validation-errors")) {
                functionId = CheckFunctionConstants.FUNCTION_HAS_VALIDATION_ERRORS;
            } else if (stringCall.equals("guess-language")) {
                functionId = CheckFunctionConstants.FUNCTION_GUESS_LANGUAGE;
            } else if (stringCall.equals("grouped-selection-buttons")) {
                functionId = CheckFunctionConstants.FUNCTION_GROUPED_SELECTION_BUTTONS;
            } else if (stringCall.equals("not-first-child")) {
                functionId = CheckFunctionConstants.FUNCTION_NOT_FIRST_CHILD;
            } else if (stringCall.equals("required-controls")) {
                functionId = CheckFunctionConstants.FUNCTION_REQUIRED_CONTROLS;
            } else if (stringCall.equals("css-generated-content")) {
                functionId = CheckFunctionConstants.FUNCTION_CSS_GENERATED_CONTENT;
            } else if (stringCall.equals("css-color-contrast")) {
                functionId = CheckFunctionConstants.FUNCTION_CSS_COLOR_CONTRAST;
            } else if (stringCall.equals("css-blink")) {
                functionId = CheckFunctionConstants.FUNCTION_CSS_BLINK;
            } else if (stringCall.equals("css-parseable")) {
                functionId = CheckFunctionConstants.FUNCTION_CSS_PARSEABLE;
            } else if (stringCall.equals("css-outline")) {
                functionId = CheckFunctionConstants.FUNCTION_CSS_OUTLINE;
            } else if (stringCall.equals("css-label-hidden")) {
                functionId = CheckFunctionConstants.FUNCTION_CSS_LABEL_HIDDEN;
            } else if (stringCall.equals("false-br-image-list")) {
                functionId = CheckFunctionConstants.FUNCTION_FALSE_BR_IMAGE_LIST;
            } else if (stringCall.equals("other-language")) {
                functionId = CheckFunctionConstants.FUNCTION_OTHER_LANGUAGE;
            } else if (stringCall.equals("current-language")) {
                functionId = CheckFunctionConstants.FUNCTION_CURRENT_LANGUAGE;
            } else {
                Logger.putLog("Warning: unknown function: " + stringCall, CheckCode.class, Logger.LOG_LEVEL_WARNING);
                return false;
            }
            return true;
        } else if (elementCode.getNodeName().equalsIgnoreCase("condition")) {
            type = CheckFunctionConstants.CODE_TYPE_CONDITION;

            String stringType = elementCode.getAttribute("type");
            if (stringType.equalsIgnoreCase("and")) {
                conditionType = CheckFunctionConstants.CONDITION_AND;
            } else if (stringType.equalsIgnoreCase("or")) {
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
        } else if (elementCode.getNodeName().equalsIgnoreCase("language")) {
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