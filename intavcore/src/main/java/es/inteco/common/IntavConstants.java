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

public interface IntavConstants {
	// ARCHIVOS DE PROPIEDADES
	public static final String INTAV_PROPERTIES = "intav.properties";
	public static final String CACHEINTAV_PROPERTIES = "cacheintav.properties";
	public static final String LANGUAGE_MAPPING = "language.mapping";
	public static final String CHECK_PROPERTIES = "check.properties";
	public static final String LANGUAGE_LIST = "languageList";
	// CONSTANTES RELATIVAS AL CORE
	public static final int NO_PAGINATION = -1;
	public static final String ENGLISH_ABB = "en";
	public static final String FALSE = "false";
	public static final String TRUE = "true";
	public static final String HTML_REDIRECTION_OK = "200";
	public static final String HTML_REDIRECTION_MOVED = "301";
	public static final String HTML_REDIRECTION_FOUND = "302";
	public static final String HTML_REDIRECTION_UNAUTHORIZED = "401";
	public static final String ENTIDAD1 = "entidad1";
	public static final String EVALUATION = "evaluation";
	public static final String FILE = "file";
	public static final String REFERER = "referer";
	public static final String LOCAL_FILE = "local-file";
	public static final String ERROR = "error";
	public static final String NO_PERMISSION = "noPermission";
	public static final String ERROR_PAGE = "errorPage";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String PASSWORD = "pass";
	public static final String RETURN_PAGE = "pagereturn";
	public static final String LANGUAGE = "language";
	public static final String ACCEPT_LANGUAGE = "accept-language";
	public static final String ELEMENT = "element";
	public static final String LOW = "low";
	public static final String MEDIUM = "medium";
	public static final String HIGH = "high";
	public static final String CAN_NOT_TELL = "cannottell";
	public static final String NEXT_LEVEL = "nextlevel";
	public static final String PREVIOUS_LEVEL = "previouslevel";
	public static final String POSITION = "pos";
	public static final String TRANSLATE_LANGUAGE = "translate_language";
	public static final String TRANSLATE = "translate";
	public static final String CURRENT_DOC = "current_doc";
	public static final String AUTHENTICATION = "authentication";
	public static final String FAILED_AUTHENTICATION = "failed";
	public static final String NONE_AUTHENTICATION = "none";
	public static final String BASIC_AUTHENTICATION = "basic";
	public static final String UNAVAILABLE_AUTHENTICATION = "unavailable";
	public static final String REALM_EQUAL_AUTHENTICATION = "realm=";
	public static final String REALM_AUTHENTICATION = "realm";
	public static final String REQUIRED_AUTHENTIFICATION = "required";
	public static final String DIAGNOSTIC = "diagnostic";
	public static final String REQUEST_LANGUAGE = "lang";
	public static final String GETTING_FROM_BD = "gettingFromBD";
	public static final int VALIDATION_ERROR_TYPE_ERROR = 1;
	public static final int VALIDATION_ERROR_TYPE_WARNING = 2;
	public static final String FAILED_GIFS_URL = "failedGifsUrl";
	// Constantes del validador HTML
	public static final String TAG_URI = "m:uri";
	public static final String TAG_LINE = "m:line";
	public static final String TAG_COL = "m:col";
	public static final String TAG_MESSAGE = "m:message";
	public static final String TAG_MESSAGE_ID = "m:messageid";
	public static final String TAG_ERROR_LIST = "m:errorlist";
	public static final String TAG_ERROR = "m:error";
	public static final String TAG_ERROR_COUNT = "m:errorcount";
	public static final String TAG_CONTEXT = "m:context";
	public static final String TAG_SKIPPED_STRING = "m:skippedstring";
	// CONSTANTES RELATIVAS AL FRONT
	public static final String ENTIDAD = "entidad";
	public static final String DOMINIO = "dominio";
	public static final String FECHA = "fecha";
	public static final String BUSQUEDA = "busqueda";
	public static final String CODIGO = "codigo";
	public static final String URL = "url";
	public static final String REFERER_FILE = "referer";
	public static final String NORMA = "norma";
	public static final String NIVEL = "nivel";
	public static final String GUIDELINES = "guidelines";
	public static final String UPLOAD_FILE = "upload-file";
	public static final String OUTPUT = "output";
	public static final String CHUNK = "chunk";
	public static final String ID = "id";
	public static final String ID_EVALUATION = "idEvaluation";
	public static final String ALL = "all";
	public static final String NONE = "none";
	public static final String MANY = "many";
	public static final String ANY = "any";
	public static final String SHOW_PROB = "ShowProb";
	public static final String PORB = "prob";
	public static final String TYPE = "type";
	public static final String GROUP = "group";
	public static final String PAG_PARAM = "p";
	public static final String LIST_ANALYSIS = "listAnalysis";
	public static final String LIST_ANALYSIS_BY_TRACKING = "listAnalysisByTracking";
	public static final String LIST_PAGE_LINKS = "listPageLinks";
	public static final String INTAV_HOME = "intavHome";
	public static final String CHECK_RESULTS = "checkResults";
	public static final String CHECK_RESULTS_WCAG2 = "checkResultsWCAG2";
	public static final String CHECK_RESULTS_DETAIL = "checkResultsDetail";
	public static final String CHECK_RESULTS_OBSERVATORY = "checkResultsObservatory";
	public static final String CHECK_RESULTS_OBSERVATORY_XML = "checkResultsObservatoryXml";
	public static final String CHECK_RESULTS_OBSERVATORY_HTML = "checkResultsObservatoryHtml";
	public static final String IS_OBSERVATORY_DEBUG_MODE = "isObservatoryDebugMode";
	public static final String EVALUATION_FORM = "evaluationForm";
	public static final String EVALUATIONS = "evaluations";
	public static final String OBSERVATORY_EVALUATIONS = "observatoryEvaluations";
	public static final String AVAILABLE_GUIDELINES = "availableGuidelines";
	public static final int TABINDEX_MANY = 0;
	public static final int TABINDEX_ALL = 1;
	public static final int TABINDEX_NONE = 2;
	public static final int TABINDEX_NO_ELEMENTS = 3;
	public static final String ACTION = "action";
	public static final String ANALYZE = "analyze";
	public static final String GET_DETAIL = "getDetail";
	public static final String GET_EVALUATION = "getEvaluation";
	public static final String RECOVER_EVALUATION = "recoverEvaluation";
	public static final String ARE_THERE_FRAMES = "areThereFrames";
	public static final String RESULTS_PAGINATION_INITIAL_VALUE = "resultsPaginationInitialValue";
	// CONSTANTES DOM
	public static final String PROBLEMA = "problem";
	public static final String PROBLEMS = "problems";
	public static final String WARNINGS = "warnings";
	public static final String INFORMATIONS = "informations";
	public static final String PROBLEM_TABLE = "problem-table";
	public static final String PROBLEM_TABLE_ROW = "problem-table-row";
	public static final String PROBLEM_TEST = "problem-test";
	public static final String PROBLEM_DETAILS = "problem-details";
	public static final String PROBLEM_REQUIREMNETS = "requirement";
	public static final String PROBLEM_CASE = "problem-case";
	public static final String TEST_PROCESS = "test-process";
	public static final String TEST_PROCESS_ITEM = "test-process-item";
	public static final String EXPECTED_RESULTS = "expected-result";
	public static final String DECISION = "decision";
	public static final String PRIORITY = "priority";
	public static final String DETAIL_TEXT = "detail-text";
	public static final String DETAIL_LINK = "detail-link";
	public static final String DETAIL_IMAGE = "detail-image";
	public static final String DETAIL_FORM = "detail-form";
	public static final String DETAIL_CODE = "detail-code";
	public static final String DETAIL_NOTE = "detail-note";
	public static final String DETAIL_TABLE = "detail-table";
	public static final String PREVIOUS_PROBLEM = "previous-problem";
	public static final String NEXT_PROBLEM = "next-problem";
	public static final String CHECK = "check";
	public static final String CHECK_ACCESIBILITY_FORM = "checkAccessibilityForm";
	public static final String ATTR_LIST = "list";
	public static final String ATTR_NAME_DISPLAY = "name-display";
	public static final String ATTR_ENTIDAD = "entidad";
	public static final String ATTR_NAME = "name";
	public static final String ATTR_NAME_ENCODED = "name-encoded";
	public static final String ATTR_TYPE = "type";
	public static final String ATTR_LINE = "line";
	public static final String ATTR_LINK = "link";
	public static final String ATTR_NUMBER = "number";
	public static final String ATTR_SERVLET = "servlet";
	public static final String ATTR_NEXT_DECISION = "next-decision";
	public static final String ATTR_TEST = "test";
	public static final String ATTR_CODE = "code";
	public static final String ATTR_VALUE = "value";
	public static final String ATTR_GROUP = "group";
	public static final String ATTR_DESCRIPTION = "description";
	public static final String ATTR_RATIONALE = "rationale";
	public static final String ATTR_ERROR = "error";
	public static final String ATTR_COLUMN = "column";
	public static final String PATH_USERFILE = "/servlet/UserFile";
	public static final String PATH_SHOWCHECK = "/servlet/ShowCheck?check=";
	public static final String PATH_DECISIONS = "/servlet/decisions";
	public static final String PATH_SUBMIT = "/servlet/Submit";
	public static final String PATH_SHOWPROB = "/servlet/ShowProb?prob=";
	public static final String PATH_LIST = "/servlet/List";
	public static final String LOCALE = "lang";
	public static final String MESSAGE_DELIMITER = "<|>";
	public static final String REGEXP_MESSAGE_FILTER_INCLUDE = "<\\|>(.*)<\\|>";
	public static final String REGEXP_MESSAGE_FILTER_EXCLUDE = "(<\\|>.*<\\|>)";
	public static final String GUIDELINE_TYPE_NORMAL = "normal";
	public static final String GUIDELINE_TYPE_OBSERVATORY = "observatory";
	public static final String BAD_PRACTICE = "badpractice";
	public static final String GOOD_PRACTICE = "goodpractice";
	public static final int OBS_VALUE_NOT_SCORE = 0;
	public static final int OBS_VALUE_RED_ZERO = 1;
	public static final int OBS_VALUE_GREEN_ZERO = 2;
	public static final int OBS_VALUE_GREEN_ONE = 3;
	public static final int OBS_VALUE_ASPECT_NOT_SCORE = -1;
	public static final int ALL_ELEMENTS = Integer.MAX_VALUE;
	public static final int STATUS_ERROR = 0;
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_EXECUTING = 2;
	public static final String STRICT = "strict";
	public static final String DEFAULT_ENCODING = "ISO-8859-1";
	public static final String ACCESSIBILITY_DECLARATION_DOCUMENT = "accessibilityDeclarationDocument";
	public static final String CHECKED_LINKS_CACHE_KEY = "checkedLinks_";
	public static final int HAS_CONTENT = 1;
	public static final int EQUAL_HEADER_TAG = 2;
	public static final int HAS_NOT_CONTENT = 0;
	public static final int IS_EMPTY = 1;
	public static final int IS_NOT_EMPTY = 0;
	public static final String GIF_READER = "gifReader";
	public static final String GIF_VERIFICATED = "gifVerificated";
	public static final String DOCTYPE_TRANSITIONAL = "transitional";
	public static final String DOCTYPE_STRICT = "strict";
	public static final String MARKUP_HTML = "HTML";
	public static final String MARKUP_XHTML = "XHTML";
	public static final String GUIDELINE_FILENAME_START_2012 = "observatorio-une-2012";
	public static final String GUIDELINE_FILENAME_START_2012_B = "observatorio-une-2012-b";
	public static final String GUIDELINE_FILENAME_START_2019 = "observatorio-une-en2019";
}
