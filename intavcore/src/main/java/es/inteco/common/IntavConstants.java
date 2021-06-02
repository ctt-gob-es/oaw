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
 * The Interface IntavConstants.
 */
public interface IntavConstants {
	
	/** The Constant INTAV_PROPERTIES. */
	// ARCHIVOS DE PROPIEDADES
	public static final String INTAV_PROPERTIES = "intav.properties";
	
	/** The Constant CACHEINTAV_PROPERTIES. */
	public static final String CACHEINTAV_PROPERTIES = "cacheintav.properties";
	
	/** The Constant LANGUAGE_MAPPING. */
	public static final String LANGUAGE_MAPPING = "language.mapping";
	
	/** The Constant CHECK_PROPERTIES. */
	public static final String CHECK_PROPERTIES = "check.properties";
	
	/** The Constant LANGUAGE_LIST. */
	public static final String LANGUAGE_LIST = "languageList";
	
	/** The Constant NO_PAGINATION. */
	// CONSTANTES RELATIVAS AL CORE
	public static final int NO_PAGINATION = -1;
	
	/** The Constant ENGLISH_ABB. */
	public static final String ENGLISH_ABB = "en";
	
	/** The Constant FALSE. */
	public static final String FALSE = "false";
	
	/** The Constant TRUE. */
	public static final String TRUE = "true";
	
	/** The Constant HTML_REDIRECTION_OK. */
	public static final String HTML_REDIRECTION_OK = "200";
	
	/** The Constant HTML_REDIRECTION_MOVED. */
	public static final String HTML_REDIRECTION_MOVED = "301";
	
	/** The Constant HTML_REDIRECTION_FOUND. */
	public static final String HTML_REDIRECTION_FOUND = "302";
	
	/** The Constant HTML_REDIRECTION_UNAUTHORIZED. */
	public static final String HTML_REDIRECTION_UNAUTHORIZED = "401";
	
	/** The Constant ENTIDAD1. */
	public static final String ENTIDAD1 = "entidad1";
	
	/** The Constant EVALUATION. */
	public static final String EVALUATION = "evaluation";
	
	/** The Constant FILE. */
	public static final String FILE = "file";
	
	/** The Constant REFERER. */
	public static final String REFERER = "referer";
	
	/** The Constant LOCAL_FILE. */
	public static final String LOCAL_FILE = "local-file";
	
	/** The Constant ERROR. */
	public static final String ERROR = "error";
	
	/** The Constant NO_PERMISSION. */
	public static final String NO_PERMISSION = "noPermission";
	
	/** The Constant ERROR_PAGE. */
	public static final String ERROR_PAGE = "errorPage";
	
	/** The Constant CODE. */
	public static final String CODE = "code";
	
	/** The Constant NAME. */
	public static final String NAME = "name";
	
	/** The Constant PASSWORD. */
	public static final String PASSWORD = "pass";
	
	/** The Constant RETURN_PAGE. */
	public static final String RETURN_PAGE = "pagereturn";
	
	/** The Constant LANGUAGE. */
	public static final String LANGUAGE = "language";
	
	/** The Constant ACCEPT_LANGUAGE. */
	public static final String ACCEPT_LANGUAGE = "accept-language";
	
	/** The Constant ELEMENT. */
	public static final String ELEMENT = "element";
	
	/** The Constant LOW. */
	public static final String LOW = "low";
	
	/** The Constant MEDIUM. */
	public static final String MEDIUM = "medium";
	
	/** The Constant HIGH. */
	public static final String HIGH = "high";
	
	/** The Constant CAN_NOT_TELL. */
	public static final String CAN_NOT_TELL = "cannottell";
	
	/** The Constant NEXT_LEVEL. */
	public static final String NEXT_LEVEL = "nextlevel";
	
	/** The Constant PREVIOUS_LEVEL. */
	public static final String PREVIOUS_LEVEL = "previouslevel";
	
	/** The Constant POSITION. */
	public static final String POSITION = "pos";
	
	/** The Constant TRANSLATE_LANGUAGE. */
	public static final String TRANSLATE_LANGUAGE = "translate_language";
	
	/** The Constant TRANSLATE. */
	public static final String TRANSLATE = "translate";
	
	/** The Constant CURRENT_DOC. */
	public static final String CURRENT_DOC = "current_doc";
	
	/** The Constant AUTHENTICATION. */
	public static final String AUTHENTICATION = "authentication";
	
	/** The Constant FAILED_AUTHENTICATION. */
	public static final String FAILED_AUTHENTICATION = "failed";
	
	/** The Constant NONE_AUTHENTICATION. */
	public static final String NONE_AUTHENTICATION = "none";
	
	/** The Constant BASIC_AUTHENTICATION. */
	public static final String BASIC_AUTHENTICATION = "basic";
	
	/** The Constant UNAVAILABLE_AUTHENTICATION. */
	public static final String UNAVAILABLE_AUTHENTICATION = "unavailable";
	
	/** The Constant REALM_EQUAL_AUTHENTICATION. */
	public static final String REALM_EQUAL_AUTHENTICATION = "realm=";
	
	/** The Constant REALM_AUTHENTICATION. */
	public static final String REALM_AUTHENTICATION = "realm";
	
	/** The Constant REQUIRED_AUTHENTIFICATION. */
	public static final String REQUIRED_AUTHENTIFICATION = "required";
	
	/** The Constant DIAGNOSTIC. */
	public static final String DIAGNOSTIC = "diagnostic";
	
	/** The Constant REQUEST_LANGUAGE. */
	public static final String REQUEST_LANGUAGE = "lang";
	
	/** The Constant GETTING_FROM_BD. */
	public static final String GETTING_FROM_BD = "gettingFromBD";
	
	/** The Constant VALIDATION_ERROR_TYPE_ERROR. */
	public static final int VALIDATION_ERROR_TYPE_ERROR = 1;
	
	/** The Constant VALIDATION_ERROR_TYPE_WARNING. */
	public static final int VALIDATION_ERROR_TYPE_WARNING = 2;
	
	/** The Constant FAILED_GIFS_URL. */
	public static final String FAILED_GIFS_URL = "failedGifsUrl";
	
	/** The Constant TAG_URI. */
	// Constantes del validador HTML
	public static final String TAG_URI = "m:uri";
	
	/** The Constant TAG_LINE. */
	public static final String TAG_LINE = "m:line";
	
	/** The Constant TAG_COL. */
	public static final String TAG_COL = "m:col";
	
	/** The Constant TAG_MESSAGE. */
	public static final String TAG_MESSAGE = "m:message";
	
	/** The Constant TAG_MESSAGE_ID. */
	public static final String TAG_MESSAGE_ID = "m:messageid";
	
	/** The Constant TAG_ERROR_LIST. */
	public static final String TAG_ERROR_LIST = "m:errorlist";
	
	/** The Constant TAG_ERROR. */
	public static final String TAG_ERROR = "m:error";
	
	/** The Constant TAG_ERROR_COUNT. */
	public static final String TAG_ERROR_COUNT = "m:errorcount";
	
	/** The Constant TAG_CONTEXT. */
	public static final String TAG_CONTEXT = "m:context";
	
	/** The Constant TAG_SKIPPED_STRING. */
	public static final String TAG_SKIPPED_STRING = "m:skippedstring";
	
	/** The Constant ENTIDAD. */
	// CONSTANTES RELATIVAS AL FRONT
	public static final String ENTIDAD = "entidad";
	
	/** The Constant DOMINIO. */
	public static final String DOMINIO = "dominio";
	
	/** The Constant FECHA. */
	public static final String FECHA = "fecha";
	
	/** The Constant BUSQUEDA. */
	public static final String BUSQUEDA = "busqueda";
	
	/** The Constant CODIGO. */
	public static final String CODIGO = "codigo";
	
	/** The Constant URL. */
	public static final String URL = "url";
	
	/** The Constant REFERER_FILE. */
	public static final String REFERER_FILE = "referer";
	
	/** The Constant NORMA. */
	public static final String NORMA = "norma";
	
	/** The Constant NIVEL. */
	public static final String NIVEL = "nivel";
	
	/** The Constant GUIDELINES. */
	public static final String GUIDELINES = "guidelines";
	
	/** The Constant UPLOAD_FILE. */
	public static final String UPLOAD_FILE = "upload-file";
	
	/** The Constant OUTPUT. */
	public static final String OUTPUT = "output";
	
	/** The Constant CHUNK. */
	public static final String CHUNK = "chunk";
	
	/** The Constant ID. */
	public static final String ID = "id";
	
	/** The Constant ID_EVALUATION. */
	public static final String ID_EVALUATION = "idEvaluation";
	
	/** The Constant ALL. */
	public static final String ALL = "all";
	
	/** The Constant NONE. */
	public static final String NONE = "none";
	
	/** The Constant MANY. */
	public static final String MANY = "many";
	
	/** The Constant ANY. */
	public static final String ANY = "any";
	
	/** The Constant SHOW_PROB. */
	public static final String SHOW_PROB = "ShowProb";
	
	/** The Constant PORB. */
	public static final String PORB = "prob";
	
	/** The Constant TYPE. */
	public static final String TYPE = "type";
	
	/** The Constant GROUP. */
	public static final String GROUP = "group";
	
	/** The Constant PAG_PARAM. */
	public static final String PAG_PARAM = "p";
	
	/** The Constant LIST_ANALYSIS. */
	public static final String LIST_ANALYSIS = "listAnalysis";
	
	/** The Constant LIST_ANALYSIS_BY_TRACKING. */
	public static final String LIST_ANALYSIS_BY_TRACKING = "listAnalysisByTracking";
	
	/** The Constant LIST_PAGE_LINKS. */
	public static final String LIST_PAGE_LINKS = "listPageLinks";
	
	/** The Constant INTAV_HOME. */
	public static final String INTAV_HOME = "intavHome";
	
	/** The Constant CHECK_RESULTS. */
	public static final String CHECK_RESULTS = "checkResults";
	
	/** The Constant CHECK_RESULTS_WCAG2. */
	public static final String CHECK_RESULTS_WCAG2 = "checkResultsWCAG2";
	
	/** The Constant CHECK_RESULTS_DETAIL. */
	public static final String CHECK_RESULTS_DETAIL = "checkResultsDetail";
	
	/** The Constant CHECK_RESULTS_OBSERVATORY. */
	public static final String CHECK_RESULTS_OBSERVATORY = "checkResultsObservatory";
	
	/** The Constant CHECK_RESULTS_OBSERVATORY_XML. */
	public static final String CHECK_RESULTS_OBSERVATORY_XML = "checkResultsObservatoryXml";
	
	/** The Constant CHECK_RESULTS_OBSERVATORY_HTML. */
	public static final String CHECK_RESULTS_OBSERVATORY_HTML = "checkResultsObservatoryHtml";
	
	/** The Constant IS_OBSERVATORY_DEBUG_MODE. */
	public static final String IS_OBSERVATORY_DEBUG_MODE = "isObservatoryDebugMode";
	
	/** The Constant EVALUATION_FORM. */
	public static final String EVALUATION_FORM = "evaluationForm";
	
	/** The Constant EVALUATIONS. */
	public static final String EVALUATIONS = "evaluations";
	
	/** The Constant OBSERVATORY_EVALUATIONS. */
	public static final String OBSERVATORY_EVALUATIONS = "observatoryEvaluations";
	
	/** The Constant AVAILABLE_GUIDELINES. */
	public static final String AVAILABLE_GUIDELINES = "availableGuidelines";
	
	/** The Constant TABINDEX_MANY. */
	public static final int TABINDEX_MANY = 0;
	
	/** The Constant TABINDEX_ALL. */
	public static final int TABINDEX_ALL = 1;
	
	/** The Constant TABINDEX_NONE. */
	public static final int TABINDEX_NONE = 2;
	
	/** The Constant TABINDEX_NO_ELEMENTS. */
	public static final int TABINDEX_NO_ELEMENTS = 3;
	
	/** The Constant ACTION. */
	public static final String ACTION = "action";
	
	/** The Constant ANALYZE. */
	public static final String ANALYZE = "analyze";
	
	/** The Constant GET_DETAIL. */
	public static final String GET_DETAIL = "getDetail";
	
	/** The Constant GET_EVALUATION. */
	public static final String GET_EVALUATION = "getEvaluation";
	
	/** The Constant RECOVER_EVALUATION. */
	public static final String RECOVER_EVALUATION = "recoverEvaluation";
	
	/** The Constant ARE_THERE_FRAMES. */
	public static final String ARE_THERE_FRAMES = "areThereFrames";
	
	/** The Constant RESULTS_PAGINATION_INITIAL_VALUE. */
	public static final String RESULTS_PAGINATION_INITIAL_VALUE = "resultsPaginationInitialValue";
	
	/** The Constant PROBLEMA. */
	// CONSTANTES DOM
	public static final String PROBLEMA = "problem";
	
	/** The Constant PROBLEMS. */
	public static final String PROBLEMS = "problems";
	
	/** The Constant WARNINGS. */
	public static final String WARNINGS = "warnings";
	
	/** The Constant INFORMATIONS. */
	public static final String INFORMATIONS = "informations";
	
	/** The Constant PROBLEM_TABLE. */
	public static final String PROBLEM_TABLE = "problem-table";
	
	/** The Constant PROBLEM_TABLE_ROW. */
	public static final String PROBLEM_TABLE_ROW = "problem-table-row";
	
	/** The Constant PROBLEM_TEST. */
	public static final String PROBLEM_TEST = "problem-test";
	
	/** The Constant PROBLEM_DETAILS. */
	public static final String PROBLEM_DETAILS = "problem-details";
	
	/** The Constant PROBLEM_REQUIREMNETS. */
	public static final String PROBLEM_REQUIREMNETS = "requirement";
	
	/** The Constant PROBLEM_CASE. */
	public static final String PROBLEM_CASE = "problem-case";
	
	/** The Constant TEST_PROCESS. */
	public static final String TEST_PROCESS = "test-process";
	
	/** The Constant TEST_PROCESS_ITEM. */
	public static final String TEST_PROCESS_ITEM = "test-process-item";
	
	/** The Constant EXPECTED_RESULTS. */
	public static final String EXPECTED_RESULTS = "expected-result";
	
	/** The Constant DECISION. */
	public static final String DECISION = "decision";
	
	/** The Constant PRIORITY. */
	public static final String PRIORITY = "priority";
	
	/** The Constant DETAIL_TEXT. */
	public static final String DETAIL_TEXT = "detail-text";
	
	/** The Constant DETAIL_LINK. */
	public static final String DETAIL_LINK = "detail-link";
	
	/** The Constant DETAIL_IMAGE. */
	public static final String DETAIL_IMAGE = "detail-image";
	
	/** The Constant DETAIL_FORM. */
	public static final String DETAIL_FORM = "detail-form";
	
	/** The Constant DETAIL_CODE. */
	public static final String DETAIL_CODE = "detail-code";
	
	/** The Constant DETAIL_NOTE. */
	public static final String DETAIL_NOTE = "detail-note";
	
	/** The Constant DETAIL_TABLE. */
	public static final String DETAIL_TABLE = "detail-table";
	
	/** The Constant PREVIOUS_PROBLEM. */
	public static final String PREVIOUS_PROBLEM = "previous-problem";
	
	/** The Constant NEXT_PROBLEM. */
	public static final String NEXT_PROBLEM = "next-problem";
	
	/** The Constant CHECK. */
	public static final String CHECK = "check";
	
	/** The Constant CHECK_ACCESIBILITY_FORM. */
	public static final String CHECK_ACCESIBILITY_FORM = "checkAccessibilityForm";
	
	/** The Constant ATTR_LIST. */
	public static final String ATTR_LIST = "list";
	
	/** The Constant ATTR_NAME_DISPLAY. */
	public static final String ATTR_NAME_DISPLAY = "name-display";
	
	/** The Constant ATTR_ENTIDAD. */
	public static final String ATTR_ENTIDAD = "entidad";
	
	/** The Constant ATTR_NAME. */
	public static final String ATTR_NAME = "name";
	
	/** The Constant ATTR_NAME_ENCODED. */
	public static final String ATTR_NAME_ENCODED = "name-encoded";
	
	/** The Constant ATTR_TYPE. */
	public static final String ATTR_TYPE = "type";
	
	/** The Constant ATTR_LINE. */
	public static final String ATTR_LINE = "line";
	
	/** The Constant ATTR_LINK. */
	public static final String ATTR_LINK = "link";
	
	/** The Constant ATTR_NUMBER. */
	public static final String ATTR_NUMBER = "number";
	
	/** The Constant ATTR_SERVLET. */
	public static final String ATTR_SERVLET = "servlet";
	
	/** The Constant ATTR_NEXT_DECISION. */
	public static final String ATTR_NEXT_DECISION = "next-decision";
	
	/** The Constant ATTR_TEST. */
	public static final String ATTR_TEST = "test";
	
	/** The Constant ATTR_CODE. */
	public static final String ATTR_CODE = "code";
	
	/** The Constant ATTR_VALUE. */
	public static final String ATTR_VALUE = "value";
	
	/** The Constant ATTR_GROUP. */
	public static final String ATTR_GROUP = "group";
	
	/** The Constant ATTR_DESCRIPTION. */
	public static final String ATTR_DESCRIPTION = "description";
	
	/** The Constant ATTR_RATIONALE. */
	public static final String ATTR_RATIONALE = "rationale";
	
	/** The Constant ATTR_ERROR. */
	public static final String ATTR_ERROR = "error";
	
	/** The Constant ATTR_COLUMN. */
	public static final String ATTR_COLUMN = "column";
	
	/** The Constant PATH_USERFILE. */
	public static final String PATH_USERFILE = "/servlet/UserFile";
	
	/** The Constant PATH_SHOWCHECK. */
	public static final String PATH_SHOWCHECK = "/servlet/ShowCheck?check=";
	
	/** The Constant PATH_DECISIONS. */
	public static final String PATH_DECISIONS = "/servlet/decisions";
	
	/** The Constant PATH_SUBMIT. */
	public static final String PATH_SUBMIT = "/servlet/Submit";
	
	/** The Constant PATH_SHOWPROB. */
	public static final String PATH_SHOWPROB = "/servlet/ShowProb?prob=";
	
	/** The Constant PATH_LIST. */
	public static final String PATH_LIST = "/servlet/List";
	
	/** The Constant LOCALE. */
	public static final String LOCALE = "lang";
	
	/** The Constant MESSAGE_DELIMITER. */
	public static final String MESSAGE_DELIMITER = "<|>";
	
	/** The Constant REGEXP_MESSAGE_FILTER_INCLUDE. */
	public static final String REGEXP_MESSAGE_FILTER_INCLUDE = "<\\|>(.*)<\\|>";
	
	/** The Constant REGEXP_MESSAGE_FILTER_EXCLUDE. */
	public static final String REGEXP_MESSAGE_FILTER_EXCLUDE = "(<\\|>.*<\\|>)";
	
	/** The Constant GUIDELINE_TYPE_NORMAL. */
	public static final String GUIDELINE_TYPE_NORMAL = "normal";
	
	/** The Constant GUIDELINE_TYPE_OBSERVATORY. */
	public static final String GUIDELINE_TYPE_OBSERVATORY = "observatory";
	
	/** The Constant BAD_PRACTICE. */
	public static final String BAD_PRACTICE = "badpractice";
	
	/** The Constant GOOD_PRACTICE. */
	public static final String GOOD_PRACTICE = "goodpractice";
	
	/** The Constant OBS_VALUE_NOT_SCORE. */
	public static final int OBS_VALUE_NOT_SCORE = 0;
	
	/** The Constant OBS_VALUE_RED_ZERO. */
	public static final int OBS_VALUE_RED_ZERO = 1;
	
	/** The Constant OBS_VALUE_GREEN_ZERO. */
	public static final int OBS_VALUE_GREEN_ZERO = 2;
	
	/** The Constant OBS_VALUE_GREEN_ONE. */
	public static final int OBS_VALUE_GREEN_ONE = 3;
	
	/** The Constant OBS_VALUE_ASPECT_NOT_SCORE. */
	public static final int OBS_VALUE_ASPECT_NOT_SCORE = -1;
	
	/** The Constant ALL_ELEMENTS. */
	public static final int ALL_ELEMENTS = Integer.MAX_VALUE;
	
	/** The Constant STATUS_ERROR. */
	public static final int STATUS_ERROR = 0;
	
	/** The Constant STATUS_SUCCESS. */
	public static final int STATUS_SUCCESS = 1;
	
	/** The Constant STATUS_EXECUTING. */
	public static final int STATUS_EXECUTING = 2;
	
	/** The Constant STRICT. */
	public static final String STRICT = "strict";
	
	/** The Constant DEFAULT_ENCODING. */
	public static final String DEFAULT_ENCODING = "ISO-8859-1";
	
	/** The Constant ACCESSIBILITY_DECLARATION_DOCUMENT. */
	public static final String ACCESSIBILITY_DECLARATION_DOCUMENT = "accessibilityDeclarationDocument";
	
	/** The Constant CHECKED_LINKS_CACHE_KEY. */
	public static final String CHECKED_LINKS_CACHE_KEY = "checkedLinks_";
	
	/** The Constant HAS_CONTENT. */
	public static final int HAS_CONTENT = 1;
	
	/** The Constant EQUAL_HEADER_TAG. */
	public static final int EQUAL_HEADER_TAG = 2;
	
	/** The Constant HAS_NOT_CONTENT. */
	public static final int HAS_NOT_CONTENT = 0;
	
	/** The Constant IS_EMPTY. */
	public static final int IS_EMPTY = 1;
	
	/** The Constant IS_NOT_EMPTY. */
	public static final int IS_NOT_EMPTY = 0;
	
	/** The Constant GIF_READER. */
	public static final String GIF_READER = "gifReader";
	
	/** The Constant GIF_VERIFICATED. */
	public static final String GIF_VERIFICATED = "gifVerificated";
	
	/** The Constant DOCTYPE_TRANSITIONAL. */
	public static final String DOCTYPE_TRANSITIONAL = "transitional";
	
	/** The Constant DOCTYPE_STRICT. */
	public static final String DOCTYPE_STRICT = "strict";
	
	/** The Constant MARKUP_HTML. */
	public static final String MARKUP_HTML = "HTML";
	
	/** The Constant MARKUP_XHTML. */
	public static final String MARKUP_XHTML = "XHTML";
	
	/** The Constant GUIDELINE_FILENAME_START_2012. */
	public static final String GUIDELINE_FILENAME_START_2012 = "observatorio-une-2012";
	
	/** The Constant GUIDELINE_FILENAME_START_2012_B. */
	public static final String GUIDELINE_FILENAME_START_2012_B = "observatorio-une-2012-b";
	
	/** The Constant GUIDELINE_FILENAME_START_2019. */
	public static final String GUIDELINE_FILENAME_START_2019 = "observatorio-une-en2019";
}
