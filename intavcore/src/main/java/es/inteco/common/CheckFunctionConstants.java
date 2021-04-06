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
package es.inteco.common;

/**
 * The Interface CheckFunctionConstants.
 */
public interface CheckFunctionConstants {

	/** The code type notset. */
	int CODE_TYPE_NOTSET = 0;
	
	/** The code type condition. */
	int CODE_TYPE_CONDITION = 1;
	
	/** The code type function. */
	int CODE_TYPE_FUNCTION = 2;
	
	/** The code type language. */
	int CODE_TYPE_LANGUAGE = 3;

	/** The condition notset. */
	int CONDITION_NOTSET = 0;
	
	/** The condition and. */
	int CONDITION_AND = 1;
	
	/** The condition or. */
	int CONDITION_OR = 2;

	/** The function notset. */
	int FUNCTION_NOTSET = 0;
	
	/** The function text equals. */
	int FUNCTION_TEXT_EQUALS = 1;
	
	/** The function text notequals. */
	int FUNCTION_TEXT_NOTEQUALS = 2;
	
	/** The function attribute exists. */
	int FUNCTION_ATTRIBUTE_EXISTS = 3;
	
	/** The function attribute missing. */
	int FUNCTION_ATTRIBUTE_MISSING = 4;
	
	/** The function attributes same. */
	int FUNCTION_ATTRIBUTES_SAME = 5;
	
	/** The function attribute notunique. */
	int FUNCTION_ATTRIBUTE_NOTUNIQUE = 6;
	
	/** The function element count greater. */
	int FUNCTION_ELEMENT_COUNT_GREATER = 7;
	
	/** The function element count less. */
	int FUNCTION_ELEMENT_COUNT_LESS = 8;
	
	/** The function element count equals. */
	int FUNCTION_ELEMENT_COUNT_EQUALS = 9;
	
	/** The function element count notequals. */
	int FUNCTION_ELEMENT_COUNT_NOTEQUALS = 10;
	
	/** The function attribute null. */
	int FUNCTION_ATTRIBUTE_NULL = 11;
	
	/** The function chars greater. */
	int FUNCTION_CHARS_GREATER = 12;
	
	/** The function chars less. */
	int FUNCTION_CHARS_LESS = 13;
	
	/** The function number any. */
	int FUNCTION_NUMBER_ANY = 14;
	
	/** The function number less than. */
	int FUNCTION_NUMBER_LESS_THAN = 15;
	
	/** The function number greater than. */
	int FUNCTION_NUMBER_GREATER_THAN = 16;
	
	/** The function container. */
	int FUNCTION_CONTAINER = 17;
	
	/** The function notcontainer. */
	int FUNCTION_NOTCONTAINER = 18;
	
	/** The function text link equivs missing. */
	int FUNCTION_TEXT_LINK_EQUIVS_MISSING = 19;
	
	/** The function label not associated. */
	int FUNCTION_LABEL_NOT_ASSOCIATED = 20;
	
	/** The function label no text. */
	int FUNCTION_LABEL_NO_TEXT = 21;
	
	/** The function dlink missing. */
	int FUNCTION_DLINK_MISSING = 22;
	
	/** The function next heading bad. */
	int FUNCTION_NEXT_HEADING_BAD = 23;
	
	/** The function prev heading bad. */
	int FUNCTION_PREV_HEADING_BAD = 24;
	
	/** The function noscript missing. */
	int FUNCTION_NOSCRIPT_MISSING = 26;
	
	/** The function noembed missing. */
	int FUNCTION_NOEMBED_MISSING = 27;
	
	/** The function altlinksame. */
	int FUNCTION_ALTLINKSAME = 28;
	
	/** The function row count. */
	int FUNCTION_ROW_COUNT = 29;
	
	/** The function col count. */
	int FUNCTION_COL_COUNT = 30;
	
	/** The function element previous. */
	int FUNCTION_ELEMENT_PREVIOUS = 31;
	
	/** The function targets same. */
	int FUNCTION_TARGETS_SAME = 32;
	
	/** The function html content not. */
	int FUNCTION_HTML_CONTENT_NOT = 33;
	
	/** The function has language. */
	int FUNCTION_HAS_LANGUAGE = 34;
	
	/** The function not valid language. */
	int FUNCTION_NOT_VALID_LANGUAGE = 35;
	
	/** The function multiradio nofieldset. */
	int FUNCTION_MULTIRADIO_NOFIELDSET = 36;
	
	/** The function multicheckbox nofieldset. */
	int FUNCTION_MULTICHECKBOX_NOFIELDSET = 37;
	
	/** The function luminosity contrast ratio. */
	int FUNCTION_LUMINOSITY_CONTRAST_RATIO = 38;
	
	/** The function ert color algorithm. */
	int FUNCTION_ERT_COLOR_ALGORITHM = 39;
	
	/** The function doctype attribute not equal. */
	int FUNCTION_DOCTYPE_ATTRIBUTE_NOT_EQUAL = 40;
	
	/** The function validate. */
	int FUNCTION_VALIDATE = 41;
	
	/** The function table type. */
	int FUNCTION_TABLE_TYPE = 43;
	
	/** The function missing id headers. */
	int FUNCTION_MISSING_ID_HEADERS = 44;
	
	/** The function missing scope. */
	int FUNCTION_MISSING_SCOPE = 45;
	
	/** The function caption summary same. */
	int FUNCTION_CAPTION_SUMMARY_SAME = 46;
	
	/** The function label not close. */
	int FUNCTION_LABEL_NOT_CLOSE = 48;
	
	/** The function submit labels different. */
	int FUNCTION_SUBMIT_LABELS_DIFFERENT = 49;
	
	/** The function is only blanks. */
	int FUNCTION_IS_ONLY_BLANKS = 50;
	
	/** The function not valid url. */
	int FUNCTION_NOT_VALID_URL = 51;
	
	/** The function object has not alternative. */
	int FUNCTION_OBJECT_HAS_NOT_ALTERNATIVE = 52;
	
	/** The function attributes not same. */
	int FUNCTION_ATTRIBUTES_NOT_SAME = 53;
	
	/** The function noframe missing. */
	int FUNCTION_NOFRAME_MISSING = 54;
	
	/** The function iframe has not alternative. */
	int FUNCTION_IFRAME_HAS_NOT_ALTERNATIVE = 55;
	
	/** The function grammar lang. */
	int FUNCTION_GRAMMAR_LANG = 56;
	
	/** The function incorrect heading structure. */
	int FUNCTION_INCORRECT_HEADING_STRUCTURE = 57;
	
	/** The function no correct document structure. */
	int FUNCTION_NO_CORRECT_DOCUMENT_STRUCTURE = 58;
	
	/** The function headers missing. */
	int FUNCTION_HEADERS_MISSING = 59;
	
	/** The function headers exist. */
	int FUNCTION_HEADERS_EXIST = 60;
	
	/** The function table heading complex. */
	int FUNCTION_TABLE_HEADING_COMPLEX = 61;
	
	/** The function has all id headers. */
	int FUNCTION_HAS_ALL_ID_HEADERS = 62;
	
	/** The function contains. */
	int FUNCTION_CONTAINS = 63;
	
	/** The function contains not. */
	int FUNCTION_CONTAINS_NOT = 64;
	
	/** The function check colors. */
	int FUNCTION_CHECK_COLORS = 65;
	
	/** The function all elements not like this. */
	int FUNCTION_ALL_ELEMENTS_NOT_LIKE_THIS = 66;
	
	/** The function definition list construction. */
	int FUNCTION_DEFINITION_LIST_CONSTRUCTION = 67;
	
	/** The function not valid doctype. */
	int FUNCTION_NOT_VALID_DOCTYPE = 68;
	
	/** The function has element into. */
	int FUNCTION_HAS_ELEMENT_INTO = 69;
	
	/** The function same following list. */
	int FUNCTION_SAME_FOLLOWING_LIST = 70;
	
	/** The function same following list not. */
	int FUNCTION_SAME_FOLLOWING_LIST_NOT = 71;
	
	/** The function text contain general quote. */
	int FUNCTION_TEXT_CONTAIN_GENERAL_QUOTE = 74;
	
	/** The function same element not. */
	int FUNCTION_SAME_ELEMENT_NOT = 75;
	
	/** The function same element. */
	int FUNCTION_SAME_ELEMENT = 76;
	
	/** The function has nbsp entities. */
	int FUNCTION_HAS_NBSP_ENTITIES = 77;
	
	/** The function not is only blanks. */
	int FUNCTION_NOT_IS_ONLY_BLANKS = 78;
	
	/** The function link same page. */
	int FUNCTION_LINK_SAME_PAGE = 79;
	
	/** The function metadata missing. */
	int FUNCTION_METADATA_MISSING = 80;
	
	/** The function not all labels. */
	int FUNCTION_NOT_ALL_LABELS = 81;
	
	/** The function label incorrectly associated. */
	int FUNCTION_LABEL_INCORRECTLY_ASSOCIATED = 82;
	
	/** The function text match. */
	int FUNCTION_TEXT_MATCH = 83;
	
	/** The function text not match. */
	int FUNCTION_TEXT_NOT_MATCH = 84;
	
	/** The function is odd. */
	int FUNCTION_IS_ODD = 85;
	
	/** The function is even. */
	int FUNCTION_IS_EVEN = 86;
	
	/** The function validate css. */
	int FUNCTION_VALIDATE_CSS = 87;
	
	/** The function has not element childs. */
	int FUNCTION_HAS_NOT_ELEMENT_CHILDS = 88;
	
	/** The function user data matchs. */
	int FUNCTION_USER_DATA_MATCHS = 89;
	
	/** The function not user data matchs. */
	int FUNCTION_NOT_USER_DATA_MATCHS = 90;
	
	/** The function is not only blanks. */
	int FUNCTION_IS_NOT_ONLY_BLANKS = 91;
	
	/** The function not children have attribute. */
	int FUNCTION_NOT_CHILDREN_HAVE_ATTRIBUTE = 92;
	
	/** The function num more controls. */
	int FUNCTION_NUM_MORE_CONTROLS = 93;
	
	/** The function not clear language. */
	int FUNCTION_NOT_CLEAR_LANGUAGE = 94;
	
	/** The function has not section link. */
	int FUNCTION_HAS_NOT_SECTION_LINK = 95;
	
	/** The function not correct heading. */
	int FUNCTION_NOT_CORRECT_HEADING = 96;
	
	/** The function false paragraph list. */
	int FUNCTION_FALSE_PARAGRAPH_LIST = 97;
	
	/** The function false br list. */
	int FUNCTION_FALSE_BR_LIST = 98;
	
	/** The function false header with only cell. */
	int FUNCTION_FALSE_HEADER_WITH_ONLY_CELL = 99;
	
	/** The function is empty element. */
	int FUNCTION_IS_EMPTY_ELEMENT = 100;
	
	/** The function has incorrect tabindex. */
	int FUNCTION_HAS_INCORRECT_TABINDEX = 101;
	
	/** The function duplicate following headers. */
	int FUNCTION_DUPLICATE_FOLLOWING_HEADERS = 102;
	
	/** The function attribute element text match. */
	int FUNCTION_ATTRIBUTE_ELEMENT_TEXT_MATCH = 103;
	
	/** The function has not enough text. */
	int FUNCTION_HAS_NOT_ENOUGH_TEXT = 104;
	
	/** The function language not equals. */
	int FUNCTION_LANGUAGE_NOT_EQUALS = 105;
	
	/** The function empty elements. */
	int FUNCTION_EMPTY_ELEMENTS = 106;
	
	/** The function elements excessive usage. */
	int FUNCTION_ELEMENTS_EXCESSIVE_USAGE = 107;
	
	/** The function attributes excessive usage. */
	int FUNCTION_ATTRIBUTES_EXCESSIVE_USAGE = 108;
	
	/** The function element percentage. */
	int FUNCTION_ELEMENT_PERCENTAGE = 109;
	
	/** The function correct links. */
	int FUNCTION_CORRECT_LINKS = 110;
	
	/** The function child element chars greater. */
	int FUNCTION_CHILD_ELEMENT_CHARS_GREATER = 111;
	
	/** The function child element chars lesser. */
	int FUNCTION_CHILD_ELEMENT_CHARS_LESSER = 136;
	
	/** The function layout table. */
	int FUNCTION_LAYOUT_TABLE = 112;
	
	/** The function layout table number. */
	int FUNCTION_LAYOUT_TABLE_NUMBER = 113;
	
	/** The function not layout table. */
	int FUNCTION_NOT_LAYOUT_TABLE = 114;
	
	/** The function internal element count greater. */
	int FUNCTION_INTERNAL_ELEMENT_COUNT_GREATER = 115;
	
	/** The function not external url. */
	int FUNCTION_NOT_EXTERNAL_URL = 116;
	
	/** The function accessibility declaration no contact. */
	int FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONTACT = 117;
	
	/** The function accessibility declaration no revision date. */
	int FUNCTION_ACCESSIBILITY_DECLARATION_NO_REVISION_DATE = 118;
	
	/** The function has complex structure. */
	int FUNCTION_HAS_COMPLEX_STRUCTURE = 119;
	
	/** The function too many broken links. */
	int FUNCTION_TOO_MANY_BROKEN_LINKS = 120;
	
	/** The function applet has not alternative. */
	int FUNCTION_APPLET_HAS_NOT_ALTERNATIVE = 121;
	
	/** The function exist attribute value. */
	int FUNCTION_EXIST_ATTRIBUTE_VALUE = 122;
	
	/** The function iframe has alternative. */
	int FUNCTION_IFRAME_HAS_ALTERNATIVE = 123;
	
	/** The function not exist attribute value. */
	int FUNCTION_NOT_EXIST_ATTRIBUTE_VALUE = 124;
	
	/** The function empty section. */
	int FUNCTION_EMPTY_SECTION = 125;
	
	/** The function count attribute value greater than. */
	int FUNCTION_COUNT_ATTRIBUTE_VALUE_GREATER_THAN = 126;
	
	/** The function child contain. */
	int FUNCTION_CHILD_CONTAIN = 127;
	
	/** The function is animated gif. */
	int FUNCTION_IS_ANIMATED_GIF = 128;
	
	/** The function applet has alternative. */
	int FUNCTION_APPLET_HAS_ALTERNATIVE = 129;
	
	/** The function attribute element text not match. */
	int FUNCTION_ATTRIBUTE_ELEMENT_TEXT_NOT_MATCH = 130;
	
	/** The function following headers without content. */
	int FUNCTION_FOLLOWING_HEADERS_WITHOUT_CONTENT = 131;
	
	/** The function img dimensions less than. */
	int FUNCTION_IMG_DIMENSIONS_LESS_THAN = 132;
	
	/** The function redundant img alt. */
	int FUNCTION_REDUNDANT_IMG_ALT = 133;
	
	/** The function table columns. */
	int FUNCTION_TABLE_COLUMNS = 134;
	
	/** The function table rows. */
	int FUNCTION_TABLE_ROWS = 135;
	
	/** The function has validation errors. */
	int FUNCTION_HAS_VALIDATION_ERRORS = 137;
	
	/** The function guess language. */
	int FUNCTION_GUESS_LANGUAGE = 138;
	
	/** The function grouped selection buttons. */
	int FUNCTION_GROUPED_SELECTION_BUTTONS = 139;
	
	/** The function not first child. */
	int FUNCTION_NOT_FIRST_CHILD = 140;
	
	/** The function required controls. */
	int FUNCTION_REQUIRED_CONTROLS = 141;
	
	/** The function css generated content. */
	int FUNCTION_CSS_GENERATED_CONTENT = 142;
	
	/** The function css color contrast. */
	int FUNCTION_CSS_COLOR_CONTRAST = 143;
	
	/** The function css blink. */
	int FUNCTION_CSS_BLINK = 144;
	
	/** The function css parseable. */
	int FUNCTION_CSS_PARSEABLE = 145;
	
	/** The function css outline. */
	int FUNCTION_CSS_OUTLINE = 146;
	
	/** The function false br image list. */
	int FUNCTION_FALSE_BR_IMAGE_LIST = 147;
	
	/** The function other language. */
	int FUNCTION_OTHER_LANGUAGE = 148;
	
	/** The function css label hidden. */
	int FUNCTION_CSS_LABEL_HIDDEN = 149;
	
	/** The function current language. */
	int FUNCTION_CURRENT_LANGUAGE = 150;
	
	/** The function invalid scope. */
	int FUNCTION_INVALID_SCOPE = 151;
	
	/** The function accessibility declaration no conformance level. */
	int FUNCTION_ACCESSIBILITY_DECLARATION_NO_CONFORMANCE_LEVEL = 152;
	
	/** The function tabindex excessive usage. */
	int FUNCTION_TABINDEX_EXCESSIVE_USAGE = 153;
	
	/** The function link chars greater. */
	int FUNCTION_LINK_CHARS_GREATER = 154;
	
	/** The function table heading blank. */
	int FUNCTION_TABLE_HEADING_BLANK = 155;
	
	/** The function title not contains. */
	int FUNCTION_TITLE_NOT_CONTAINS = 156;
	
	/** The function table complex. */
	int FUNCTION_TABLE_COMPLEX = 157;

	/** The function aria labelledby referenced. */
	// Nuevos códigos de funciones une-2012-b
	int FUNCTION_ARIA_LABELLEDBY_REFERENCED = 158;
	
	/** The function attribute lenght. */
	int FUNCTION_ATTRIBUTE_LENGHT = 159;
	
	/** The function attribute labeledby lenght. */
	int FUNCTION_ATTRIBUTE_LABELEDBY_LENGHT = 160;
	
	/** The function headers wai missing. */
	int FUNCTION_HEADERS_WAI_MISSING = 161;
	
	/** The function headers wai level 1 missing. */
	int FUNCTION_HEADERS_WAI_LEVEL_1_MISSING = 162;
	
	/** The function following wai headers without content. */
	int FUNCTION_FOLLOWING_WAI_HEADERS_WITHOUT_CONTENT = 170;
	
	/** The function skip wai headers level. */
	int FUNCTION_SKIP_WAI_HEADERS_LEVEL = 171;
	
	/** The function element count attribute value. */
	int FUNCTION_ELEMENT_COUNT_ATTRIBUTE_VALUE = 172;
	
	/** The function accessibility contact form. */
	int FUNCTION_ACCESSIBILITY_CONTACT_FORM = 173;
	
	/** The function aria describedby referenced. */
	int FUNCTION_ARIA_DESCRIBEDBY_REFERENCED = 174;

	/** The function text match properties. */
	int FUNCTION_TEXT_MATCH_PROPERTIES = 175;
	
	/** The function text not match properties. */
	int FUNCTION_TEXT_NOT_MATCH_PROPERTIES = 176;
	
	/** The function text not equals properties. */
	int FUNCTION_TEXT_NOT_EQUALS_PROPERTIES = 177;

	/** The function attributtes equals. */
	int FUNCTION_ATTRIBUTTES_EQUALS = 178;
	
	/** The function label match arial value. */
	int FUNCTION_LABEL_MATCH_ARIAL_VALUE = 179;
	
	/** The function force spacing css. */
	int FUNCTION_FORCE_SPACING_CSS = 180;
	
	/** The function has responsive tags. */
	int FUNCTION_HAS_RESPONSIVE_TAGS = 181;
	
	/** The function force transform. */
	int FUNCTION_FORCE_TRANSFORM = 182;
	
	/** The function autocomplete valid. */
	int FUNCTION_AUTOCOMPLETE_VALID = 183;
	
	/** The function has section. */
	int FUNCTION_HAS_SECTION = 184;
	
	/** The function section has text. */
	int FUNCTION_SECTION_HAS_TEXT = 185;
	
	/** The function section has mailto. */
	int FUNCTION_SECTION_HAS_MAILTO = 186;
	
	/** The function section has element. */
	int FUNCTION_SECTION_HAS_ELEMENT = 187;
	
	/** The function accesibility year. */
	int FUNCTION_ACCESIBILITY_YEAR = 188;
	
	/** The function section has date. */
	int FUNCTION_SECTION_HAS_DATE = 189;
	
	/** The function section has phone. */
	int FUNCTION_SECTION_HAS_PHONE = 190;
	
	/** The function empty table 70. */
	int FUNCTION_EMPTY_TABLE_70 = 191;
	
	/** The function more headers than fieldsets. */
	int FUNCTION_MORE_HEADERS_THAN_FIELDSETS = 192;

	/** The check status ok. */
	// check codes
	int CHECK_STATUS_OK = 1;
	
	/** The check status prerequisite not print. */
	int CHECK_STATUS_PREREQUISITE_NOT_PRINT = 2;
	
	/** The check status uninitialized. */
	int CHECK_STATUS_UNINITIALIZED = 0;
	
	/** The check status bad function. */
	int CHECK_STATUS_BAD_FUNCTION = -1;
	
	/** The check status bad params. */
	int CHECK_STATUS_BAD_PARAMS = -2;
	
	/** The check status bad trigger. */
	int CHECK_STATUS_BAD_TRIGGER = -3;
	
	/** The check status bad function init. */
	int CHECK_STATUS_BAD_FUNCTION_INIT = -4;
	
	/** The check status bad algorithm. */
	int CHECK_STATUS_BAD_ALGORITHM = -5;
	
	/** The check status bad test. */
	int CHECK_STATUS_BAD_TEST = -6;
	
	/** The check status bad id. */
	int CHECK_STATUS_BAD_ID = -7;

	/** The confidence not set. */
	int CONFIDENCE_NOT_SET = 0;
	
	/** The confidence low. */
	int CONFIDENCE_LOW = 1;
	
	/** The confidence medium. */
	int CONFIDENCE_MEDIUM = 2;
	
	/** The confidence high. */
	int CONFIDENCE_HIGH = 3;
	
	/** The confidence cannottell. */
	int CONFIDENCE_CANNOTTELL = 4;

	/** The code result ignore. */
	int CODE_RESULT_IGNORE = 0;
	
	/** The code result problem. */
	int CODE_RESULT_PROBLEM = 1;
	
	/** The code result noproblem. */
	int CODE_RESULT_NOPROBLEM = 2;

	/** The compare equal. */
	int COMPARE_EQUAL = 0;
	
	/** The compare less than. */
	int COMPARE_LESS_THAN = 1;
	
	/** The compare greater than. */
	int COMPARE_GREATER_THAN = 2;

}
