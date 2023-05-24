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

import java.awt.Color;

import org.apache.struts.util.MessageResources;

import com.itextpdf.text.BaseColor;

/**
 * The Interface Constants.
 */
public interface Constants {
	/** The message resources. */
	MessageResources messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
	/** The crawler properties. */
	// ARCHIVOS DE PROPIEDADES
	String CRAWLER_PROPERTIES = "crawler.properties";
	/** The crawler core properties. */
	String CRAWLER_CORE_PROPERTIES = "crawler.core.properties";
	/** The intav properties. */
	String INTAV_PROPERTIES = "intav.properties";
	/** The pdf properties. */
	String PDF_PROPERTIES = "pdf.properties";
	/** The basic service properties. */
	String BASIC_SERVICE_PROPERTIES = "basic.service.properties";
	/** The mail properties. */
	String MAIL_PROPERTIES = "mail.properties";
	/** The true. */
	String TRUE = "true";
	/** The false. */
	String FALSE = "false";
	/** The locale. */
	String LOCALE = "lang";
	/** The login param. */
	String LOGIN_PARAM = "log";
	/** The language list. */
	String LANGUAGE_LIST = "languageList";
	/** The load seeds forward. */
	// FORWARD
	String LOAD_SEEDS_FORWARD = "observatorySeed";
	/** The upload conclusion form. */
	String UPLOAD_CONCLUSION_FORM = "uploadConclusionForm";
	/** The login. */
	String LOGIN = "loginSistema";
	/** The exito. */
	String EXITO = "exito";
	/** The exito2. */
	String EXITO2 = "exito2";
	/** The nueva semilla forward. */
	String NUEVA_SEMILLA_FORWARD = "nuevaSemilla";
	/** The nuevo certificado. */
	String NUEVO_CERTIFICADO = "nuevoCertificado";
	/** The anadir semilla. */
	String ANADIR_SEMILLA = "anadirSemillas";
	/** The exito mod. */
	String EXITO_MOD = "exitoMod";
	/** The exito si. */
	String EXITO_SI = "exitoSi";
	/** The exito ver. */
	String EXITO_VER = "exitoVer";
	/** The exito intermedio. */
	String EXITO_INTERMEDIO = "exitoIntermedio";
	/** The no exito. */
	String NO_EXITO = "noExito";
	/** The exito eliminar. */
	String EXITO_ELIMINAR = "exitoEliminar";
	/** The indice graficas. */
	String INDICE_GRAFICAS = "indiceGraficas";
	/** The list analysis by tracking. */
	String LIST_ANALYSIS_BY_TRACKING = "listAnalysisByTracking";
	/** The check results. */
	String CHECK_RESULTS = "checkResults";
	/** The check observatory results. */
	String CHECK_OBSERVATORY_RESULTS = "checkObservatoryResults";
	/** The get broken links. */
	String GET_BROKEN_LINKS = "getBrokenLinks";
	/** The certificate form. */
	String CERTIFICATE_FORM = "certificateForm";
	/** The index admin. */
	String INDEX_ADMIN = "indexAdmin";
	/** The get seeds. */
	String GET_SEEDS = "getSeeds";
	/** The regenerate results. */
	String REGENERATE_RESULTS = "regenerateResults";
	/** The stop crawl. */
	String STOP_CRAWL = "stop";
	/** The add sedd obs. */
	String ADD_SEDD_OBS = "addSeed";
	/** The get seed ambits. */
	String GET_SEED_AMBITS = "getSeedAmbits";
	/** The seed ambits. */
	String SEED_AMBITS = "seedAmbits";
	/** The delete ambit. */
	String DELETE_AMBIT = "deleteAmbit";
	/** The delete ambit confirmation. */
	String DELETE_AMBIT_CONFIRMATION = "deleteAmbitConfirmation";
	/** The new seed ambit. */
	String NEW_SEED_AMBIT = "newSeedAmbit";
	/** The seed ambit form. */
	String SEED_AMBIT_FORM = "seedAmbitForm";
	/** The add seed ambit. */
	String ADD_SEED_AMBIT = "addSeedAmbit";
	/** The edit seed ambit. */
	String EDIT_SEED_AMBIT = "editSeedAmbit";
	/** The update seed ambit. */
	String UPDATE_SEED_AMBIT = "updateSeedAmbit";
	/** The view seed ambit. */
	String VIEW_SEED_AMBIT = "viewSeedCategory";
	/** The delete ambit seed confirmation. */
	String DELETE_AMBIT_SEED_CONFIRMATION = "deleteAmbitSeedConfirmation";
	/** The delete ambit seed. */
	String DELETE_AMBIT_SEED = "deleteAmbitSeed";
	/** The edit ambit seed. */
	String EDIT_AMBIT_SEED = "editAmbitSeed";
	/** The new ambit seed. */
	String NEW_AMBIT_SEED = "newAmbitSeed";
	/** The ambito form. */
	String AMBITO_FORM = "AmbitoForm";
	/** The clasificacion form. */
	String CLASIFICACION_FORM = "ClasificacionForm";
	/** The add ambit seed. */
	String ADD_AMBIT_SEED = "addAmbitSeed";
	/** The update ambit seed. */
	String UPDATE_AMBIT_SEED = "updateAmbitSeed";
	/** The ambit seed form. */
	String AMBIT_SEED_FORM = "ambitSeedForm";
	/** The get ambit seeds file. */
	String GET_AMBIT_SEEDS_FILE = "getAmbitSeedsFile";
	/** The get seed complexities. */
	String GET_SEED_COMPLEXITIES = "getSeedComplexities";
	/** The seed complexities. */
	String SEED_COMPLEXITIES = "seedComplexities";
	/** The delete complexity. */
	String DELETE_COMPLEXITY = "deleteComplexity";
	/** The delete complexity confirmation. */
	String DELETE_COMPLEXITY_CONFIRMATION = "deleteComplexityConfirmation";
	/** The new seed complexity. */
	String NEW_SEED_COMPLEXITY = "newSeedComplexity";
	/** The seed complexity form. */
	String SEED_COMPLEXITY_FORM = "seedComplexityForm";
	/** The add seed complexity. */
	String ADD_SEED_COMPLEXITY = "addSeedComplexity";
	/** The edit seed complexity. */
	String EDIT_SEED_COMPLEXITY = "editSeedComplexity";
	/** The update seed complexity. */
	String UPDATE_SEED_COMPLEXITY = "updateSeedComplexity";
	/** The view seed complexity. */
	String VIEW_SEED_COMPLEXITY = "viewSeedComplexity";
	/** The delete complexity seed confirmation. */
	String DELETE_COMPLEXITY_SEED_CONFIRMATION = "deleteComplexitySeedConfirmation";
	/** The delete complexity seed. */
	String DELETE_COMPLEXITY_SEED = "deleteComplexitySeed";
	/** The edit complexity seed. */
	String EDIT_COMPLEXITY_SEED = "editComplexitySeed";
	/** The new complexity seed. */
	String NEW_COMPLEXITY_SEED = "newComplexitySeed";
	/** The complejidad form. */
	String COMPLEJIDAD_FORM = "ComplejidadForm";
	/** The add complexity seed. */
	String ADD_COMPLEXITY_SEED = "addComplexitySeed";
	/** The update complexity seed. */
	String UPDATE_COMPLEXITY_SEED = "updateComplexitySeed";
	/** The complexity seed form. */
	String COMPLEXITY_SEED_FORM = "complexitySeedForm";
	/** The get complexity seeds file. */
	String GET_COMPLEXITY_SEEDS_FILE = "getComplexitySeedsFile";
	/** The get seed categories. */
	String GET_SEED_CATEGORIES = "getSeedCategories";
	/** The seed categories. */
	String SEED_CATEGORIES = "seedCategories";
	/** The delete category. */
	String DELETE_CATEGORY = "deleteCategory";
	/** The delete category confirmation. */
	String DELETE_CATEGORY_CONFIRMATION = "deleteCategoryConfirmation";
	/** The new seed category. */
	String NEW_SEED_CATEGORY = "newSeedCategory";
	/** The seed category form. */
	String SEED_CATEGORY_FORM = "seedCategoryForm";
	/** The add seed category. */
	String ADD_SEED_CATEGORY = "addSeedCategory";
	/** The edit seed category. */
	String EDIT_SEED_CATEGORY = "editSeedCategory";
	/** The update seed category. */
	String UPDATE_SEED_CATEGORY = "updateSeedCategory";
	/** The view seed category. */
	String VIEW_SEED_CATEGORY = "viewSeedCategory";
	/** The delete category seed confirmation. */
	String DELETE_CATEGORY_SEED_CONFIRMATION = "deleteCategorySeedConfirmation";
	/** The delete category seed. */
	String DELETE_CATEGORY_SEED = "deleteCategorySeed";
	/** The edit category seed. */
	String EDIT_CATEGORY_SEED = "editCategorySeed";
	/** The new category seed. */
	String NEW_CATEGORY_SEED = "newCategorySeed";
	/** The categoria form. */
	String CATEGORIA_FORM = "CategoriaForm";
	/** The add category seed. */
	String ADD_CATEGORY_SEED = "addCategorySeed";
	/** The update category seed. */
	String UPDATE_CATEGORY_SEED = "updateCategorySeed";
	/** The category seed form. */
	String CATEGORY_SEED_FORM = "categorySeedForm";
	/** The get category seeds file. */
	String GET_CATEGORY_SEEDS_FILE_XML = "getCategorySeedsFileXml";
	/** Action to export category seeds to an excel file. */
	String GET_CATEGORY_SEEDS_FILE_XLSX = "getCategorySeedsFileXlsx";
	/** The load form. */
	String LOAD_FORM = "loadForm";
	/** The rastreo test. */
	String RASTREO_TEST = "rastreoTest";
	/** The launch test. */
	String LAUNCH_TEST = "launchTest";
	/** The rastreo test results. */
	String RASTREO_TEST_RESULTS = "rastreoTestResults";
	/** The crawlings menu. */
	String CRAWLINGS_MENU = "crawlingsMenu";
	/** The section form. */
	String SECTION_FORM = "sectionForm";
	/** The generate all reports. */
	String GENERATE_ALL_REPORTS = "generateAllReports";
	/** The generate report async. */
	String GENERATE_REPORT_ASYNC = "generateReportAsync";
	/** The get seed clasifications. */
	String GET_SEED_CLASIFICATIONS = "getSeedTags";
	/** The seed tag. */
	String SEED_TAG = "seedTags";
	/** The delete tag. */
	String DELETE_TAG = "deleteTagt";
	/** The delete tag confirmation. */
	String DELETE_TAG_CONFIRMATION = "deleteTagConfirmation";
	/** The new seed tag. */
	String NEW_SEED_TAG = "newSeedTag";
	/** The seed tag form. */
	String SEED_TAG_FORM = "seedTagForm";
	/** The add seed tag. */
	String ADD_SEED_TAG = "addSeedTag";
	/** The edit seed tag. */
	String EDIT_SEED_TAG = "editSeedTag";
	/** The update seed tag. */
	String UPDATE_SEED_TAG = "updateSeedTag";
	/** The view seed tag. */
	String VIEW_SEED_TAG = "viewSeedTag";
	/** The delete tag seed confirmation. */
	String DELETE_TAG_SEED_CONFIRMATION = "deleteTagSeedConfirmation";
	/** The delete tag seed. */
	String DELETE_TAG_SEED = "deleteTagSeed";
	/** The edit tag seed. */
	String EDIT_TAG_SEED = "editTagSeed";
	/** The new tag seed. */
	String NEW_TAG_SEED = "newTagSeed";
	/** The etiqueta form. */
	String ETIQUETA_FORM = "EtiquetaForm";
	/** The add tag seed. */
	String ADD_TAG_SEED = "addTagSeed";
	/** The update tag seed. */
	String UPDATE_TAG_SEED = "updateTagSeed";
	/** The xml lista. */
	// XML semillas
	String XML_LISTA = "lista";
	/** The xml id. */
	String XML_ID = "id";
	/** The xml semilla. */
	String XML_SEMILLA = "semilla";
	/** The xml nombre. */
	String XML_NOMBRE = "nombre";
	/** The xml urls. */
	String XML_URLS = "urls";
	/** The xml url. */
	String XML_URL = "url";
	/** The xml dependencia. */
	String XML_DEPENDENCIA = "depende_de";
	/** The xml acronimo. */
	String XML_ACRONIMO = "acronimo";
	/** The xml activa. */
	String XML_ACTIVA = "activa";
	/** The xml eliminada. */
	String XML_ELIMINADA = "eliminada";
	/** The xml in directory. */
	String XML_IN_DIRECTORY = "en_directorio";
	/** The xml segmento. */
	String XML_SEGMENTO = "segmento";
	/** The xml ambito. */
	String XML_AMBITO = "ambito";
	/** The xml complejidad. */
	String XML_COMPLEJIDAD = "complejidad";
	/** The xml observaciones. */
	String XML_OBSERVACIONES = "observaciones";
	/** The xml etiquetas. */
	String XML_ETIQUETAS = "etiquetas";
	/** The xml etiquetas tematica. */
	String XML_ETIQUETAS_TEMATICA = "tematica";
	/** The xml etiquetas distribuccion. */
	String XML_ETIQUETAS_DISTRIBUCCION = "distribucion";
	/** The xml etiquetas recurrencia. */
	String XML_ETIQUETAS_RECURRENCIA = "recurrencia";
	/** The xml etiquetas otros. */
	String XML_ETIQUETAS_OTROS = "otros";
	/** The error. */
	String ERROR = "error";
	/** The error1. */
	String ERROR1 = "error1";
	/** The error timeout. */
	String ERROR_TIMEOUT = "errorTimeout";
	/** The show category list. */
	String SHOW_CATEGORY_LIST = "categoryList";
	/** The no permission. */
	String NO_PERMISSION = "no_permission";
	/** The error page. */
	String ERROR_PAGE = "errorPage";
	/** The volver. */
	String VOLVER = "volver";
	/** The volver carga. */
	String VOLVER_CARGA = "volverCarga";
	/** The volver carga menu. */
	String VOLVER_CARGA_MENU = "volverCargaMenu";
	/** The volver carga sistema. */
	String VOLVER_CARGA_SISTEMA = "volverCargaSistema";
	/** The volver carga cliente. */
	String VOLVER_CARGA_CLIENTE = "volverCargaCliente";
	/** The volver carga observatorio. */
	String VOLVER_CARGA_OBSERVATORIO = "volverCargaObservatorio";
	/** The ventana confirmacion. */
	String VENTANA_CONFIRMACION = "ventanaConfirmacion";
	/** The login crawler. */
	String LOGIN_CRAWLER = "loginCrawler";
	/** The no rastreo permiso. */
	String NO_RASTREO_PERMISO = "noRastreoPermiso";
	/** The no cartucho no create. */
	String NO_CARTUCHO_NO_CREATE = "noCartuchoNoCreate";
	/** The forward semilla. */
	String FORWARD_SEMILLA = "semilla";
	/** The cargar semillas observatorio. */
	String CARGAR_SEMILLAS_OBSERVATORIO = "cargarSemillasObservatorio";
	/** The get fulfilled observatories. */
	String GET_FULFILLED_OBSERVATORIES = "getFulfilledObservatories";
	/** The fulfilled observatories. */
	String FULFILLED_OBSERVATORIES = "fulfilledObservatories";
	/** The get annexes. */
	String GET_ANNEXES = "getAnnexes";
	/** The crawler graphic total results. */
	String CRAWLER_GRAPHIC_TOTAL_RESULTS = "totalResults";
	/** The crawler graphic global results. */
	String CRAWLER_GRAPHIC_GLOBAL_RESULTS = "globalResults";
	/** The crawler graphic global results list n1. */
	String CRAWLER_GRAPHIC_GLOBAL_RESULTS_LIST_N1 = "gResultsList1";
	/** The crawler graphic global results list n2. */
	String CRAWLER_GRAPHIC_GLOBAL_RESULTS_LIST_N2 = "gResultsList2";
	/** The crawler graphic total results list. */
	String CRAWLER_GRAPHIC_TOTAL_RESULTS_LIST = "tResultsList";
	/** The type observatory. */
	String TYPE_OBSERVATORY = "Otype";
	/** The observatory graphic. */
	String OBSERVATORY_GRAPHIC = "getObservatoryGraphic";
	/** The observatory have results. */
	int OBSERVATORY_HAVE_RESULTS = 1;
	/** The observatory have one result. */
	int OBSERVATORY_HAVE_ONE_RESULT = 2;
	/** The observatory not have results. */
	int OBSERVATORY_NOT_HAVE_RESULTS = 0;
	/** The observatory results. */
	String OBSERVATORY_RESULTS = "existRes";
	/** The graphic. */
	String GRAPHIC = "graphic";
	/** The observatory graphic regenerate. */
	String OBSERVATORY_GRAPHIC_REGENERATE = "reg";
	/** The graphic type. */
	String GRAPHIC_TYPE = "type";
	/** The graphic verification. */
	String GRAPHIC_VERIFICATION = "verification";
	/** The graphic aspect. */
	String GRAPHIC_ASPECT = "aspect";
	/** The observatory graphic pdf report. */
	String OBSERVATORY_GRAPHIC_PDF_REPORT = "pdfReport";
	/** The observatory graphic global allocation. */
	String OBSERVATORY_GRAPHIC_GLOBAL_ALLOCATION = "globalAllocation";
	/** The observatory graphic global compliance. */
	String OBSERVATORY_GRAPHIC_GLOBAL_COMPLIANCE = "globalCompliance";
	/** The observatory graphic segments mark. */
	String OBSERVATORY_GRAPHIC_SEGMENTS_MARK = "segmentsMark";
	/** The observatory graphic segments cmp mark. */
	String OBSERVATORY_GRAPHIC_SEGMENTS_CMP_MARK = "segmentsCMPMark";
	/** The observatory graphic ambit mark. */
	String OBSERVATORY_GRAPHIC_AMBIT_MARK = "ambitMark";
	/** The observatory graphic group segment mark. */
	String OBSERVATORY_GRAPHIC_GROUP_SEGMENT_MARK = "groupSegmentMark";
	/** The observatory graphic global accessibility allocation. */
	String OBSERVATORY_GRAPHIC_GLOBAL_ACCESSIBILITY_ALLOCATION = "globalAccessAllocation";
	/** The observatory graphic accessibility level allocation s. */
	String OBSERVATORY_GRAPHIC_ACCESSIBILITY_LEVEL_ALLOCATION_S = "accLevelAllocationS";
	/** The observatory graphic mid verification n1 s. */
	String OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1_S = "midVerificationN1S";
	/** The observatory graphic mid verification n2 s. */
	String OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2_S = "midVerificationN2S";
	/** The observatory graphic modality verification n1 s. */
	String OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1_S = "modalityVerificationN1S";
	/** The observatory graphic modality verification n2 s. */
	String OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2_S = "modalityVerificationN2S";
	/** The observatory graphic mark allocation s. */
	String OBSERVATORY_GRAPHIC_MARK_ALLOCATION_S = "markAllocationS";
	/** The observatory graphic mid aspect. */
	String OBSERVATORY_GRAPHIC_MID_ASPECT = "midAspect";
	/** The observatory graphic mid verification n1. */
	String OBSERVATORY_GRAPHIC_MID_VERIFICATION_N1 = "midVerificationN1";
	/** The observatory graphic mid verification n2. */
	String OBSERVATORY_GRAPHIC_MID_VERIFICATION_N2 = "midVerificationN2";
	/** The observatory graphic evolution approval level a. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_A = "evolutionApprovalLevelA";
	/** The observatory graphic evolution approval level aa. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_AA = "evolutionApprovalLevelAA";
	/** The observatory graphic evolution approval level nv. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_APPROVAL_LEVEL_NV = "evolutionApprovalLevelNV";
	/** The observatory graphic evolution mid mark. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_MID_MARK = "midMark";
	/** The observatory graphic modality verification n1. */
	String OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N1 = "modalityVerificationN1";
	/** The observatory graphic modality verification n2. */
	String OBSERVATORY_GRAPHIC_MODALITY_VERIFICATION_N2 = "modalityVerificationN2";
	/** The observatory graphic evolution verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_VERIFICATION = "verification";
	/** The observatory graphic initial. */
	String OBSERVATORY_GRAPHIC_INITIAL = "initial";
	/** The observatory graphic rgenerate. */
	String OBSERVATORY_GRAPHIC_RGENERATE = "regenerateGraphic";
	/** The observatory graphic global. */
	String OBSERVATORY_GRAPHIC_GLOBAL = "global";
	/** The observatory graphic global data list dag. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG = "globalDataListDAG";
	/** The observatory graphic global data list dcg. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DCG = "globalDataListDCG";
	/** The observatory graphic global data list cas. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS = "globalDataListCAS";
	/** The observatory graphic global data list casc. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CASC = "globalDataListCASC";
	/** The observatory graphic global data list cps. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS = "globalDataListCPS";
	/** The observatory graphic global data list cpcx. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPCX = "globalDataListCPCX";
	/** The observatory graphic global data list cmps. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPS = "globalDataListCMPS";
	/** The observatory graphic global data list cmps ambit. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPS_AMBIT = "globalDataListCMPSAMBIT";
	/** The observatory graphic global data list cmpc. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPC = "globalDataListCMPC";
	/** The observatory graphic global data list cma. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA = "globalDataListCMA";
	/** The observatory graphic global data list cmvi. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI = "globalDataListCMVI";
	/** The observatory graphic global data list cmvii. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII = "globalDataListCMVII";
	/** The observatory graphic global data list modality verification i. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I = "globalDataListModalityVerificationI";
	/** The observatory graphic global data list modality verification ii. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II = "globalDataListModalityVerificationII";
	/** The observatory graphic global data list compilance verification i. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_COMPILANCE_VERIFICATION_I = "globalDataListCompilanceVerificationI";
	/** The observatory graphic global data list compilance verification ii. */
	String OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_COMPILANCE_VERIFICATION_II = "globalDataListCompilanceVerificationII";
	/** The observatory graphic segment data list dp. */
	String OBSERVATORY_GRAPHIC_SEGMENT_DATA_LIST_DP = "segmentDataListDP";
	/** The observatory graphic comparative. */
	String OBSERVATORY_GRAPHIC_COMPARATIVE = "comp";
	/** The observatory graphic categories. */
	String OBSERVATORY_GRAPHIC_CATEGORIES = "cat";
	/** The observatory num cas graph. */
	String OBSERVATORY_NUM_CAS_GRAPH = "numGraphCAS";
	/** The observatory num casc graph. */
	String OBSERVATORY_NUM_CASC_GRAPH = "numGraphCASC";
	/** The observatory num cps graph. */
	String OBSERVATORY_NUM_CPS_GRAPH = "numGraphCPS";
	/** The observatory num cpcx graph. */
	String OBSERVATORY_NUM_CPCX_GRAPH = "numGraphCPCX";
	/** The observatory num cmps graph. */
	String OBSERVATORY_NUM_CMPS_GRAPH = "numGraphCMPS";
	/** The observatory num cmps ambit graph. */
	String OBSERVATORY_NUM_CMPS_AMBIT_GRAPH = "numGraphCMPSAMBIT";
	/** The observatory num cmpc graph. */
	String OBSERVATORY_NUM_CMPC_GRAPH = "numGraphCMPC";
	/** The observatory num graph. */
	String OBSERVATORY_NUM_GRAPH = "numGraph";
	/** The observatory graphic evolution 111 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION = "1.1.1";
	/** The observatory graphic evolution 112 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION = "1.1.2";
	/** The observatory graphic evolution 113 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION = "1.1.3";
	/** The observatory graphic evolution 114 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION = "1.1.4";
	/** The observatory graphic evolution 115 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_115_VERIFICATION = "1.1.5";
	/** The observatory graphic evolution 116 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_116_VERIFICATION = "1.1.6";
	/** The observatory graphic evolution 117 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_117_VERIFICATION = "1.1.7";
	/** The observatory graphic evolution 121 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION = "1.2.1";
	/** The observatory graphic evolution 122 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION = "1.2.2";
	/** The observatory graphic evolution 123 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION = "1.2.3";
	/** The observatory graphic evolution 124 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION = "1.2.4";
	/** The observatory graphic evolution 125 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION = "1.2.5";
	/** The observatory graphic evolution 126 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION = "1.2.6";
	/** The observatory graphic evolution 211 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION = "2.1.1";
	/** The observatory graphic evolution 212 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION = "2.1.2";
	/** The observatory graphic evolution 213 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION = "2.1.3";
	/** The observatory graphic evolution 214 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION = "2.1.4";
	/** The observatory graphic evolution 215 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_215_VERIFICATION = "2.1.5";
	/** The observatory graphic evolution 216 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_216_VERIFICATION = "2.1.6";
	/** The observatory graphic evolution 217 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_217_VERIFICATION = "2.1.7";
	/** The observatory graphic evolution 221 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION = "2.2.1";
	/** The observatory graphic evolution 222 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION = "2.2.2";
	/** The observatory graphic evolution 223 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION = "2.2.3";
	/** The observatory graphic evolution 224 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION = "2.2.4";
	/** The observatory graphic evolution 225 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION = "2.2.5";
	/** The observatory graphic evolution 226 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION = "2.2.6";
	/** The observatory graphic evolution data list a. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_A = "evolutionDataListA";
	/** The observatory graphic evolution data list aa. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AA = "evolutionDataListAA";
	/** The observatory graphic evolution data list nv. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_NV = "evolutionDataListNV";
	/** The observatory graphic evolution data list mid punt. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_MID_PUNT = "evolutionDataListMidPunt";
	/** The observatory graphic evolution data list v111. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111 = "evolutionDataList111";
	/** The observatory graphic evolution data list v112. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112 = "evolutionDataList112";
	/** The observatory graphic evolution data list v113. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113 = "evolutionDataList113";
	/** The observatory graphic evolution data list v114. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114 = "evolutionDataList114";
	/** The observatory graphic evolution data list v115. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V115 = "evolutionDataList115";
	/** The observatory graphic evolution data list v116. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V116 = "evolutionDataList116";
	/** The observatory graphic evolution data list v117. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V117 = "evolutionDataList117";
	/** The observatory graphic evolution data list v121. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121 = "evolutionDataList121";
	/** The observatory graphic evolution data list v122. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122 = "evolutionDataList122";
	/** The observatory graphic evolution data list v123. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123 = "evolutionDataList123";
	/** The observatory graphic evolution data list v124. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124 = "evolutionDataList124";
	/** The observatory graphic evolution data list v125. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125 = "evolutionDataList125";
	/** The observatory graphic evolution data list v126. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126 = "evolutionDataList126";
	/** The observatory graphic evolution data list v211. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211 = "evolutionDataList211";
	/** The observatory graphic evolution data list v212. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212 = "evolutionDataList212";
	/** The observatory graphic evolution data list v213. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213 = "evolutionDataList213";
	/** The observatory graphic evolution data list v214. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214 = "evolutionDataList214";
	/** The observatory graphic evolution data list v215. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V215 = "evolutionDataList215";
	/** The observatory graphic evolution data list v216. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V216 = "evolutionDataList216";
	/** The observatory graphic evolution data list v217. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V217 = "evolutionDataList217";
	/** The observatory graphic evolution data list v221. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221 = "evolutionDataList221";
	/** The observatory graphic evolution data list v222. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222 = "evolutionDataList222";
	/** The observatory graphic evolution data list v223. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V223 = "evolutionDataList223";
	/** The observatory graphic evolution data list v224. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V224 = "evolutionDataList224";
	/** The observatory graphic evolution data list v225. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V225 = "evolutionDataList225";
	/** The observatory graphic evolution data list v226. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V226 = "evolutionDataList226";
	/** The observatory graphic evolution data list ag. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AG = "evolutionDataListAG";
	/** The observatory graphic evolution data list aal. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AAL = "evolutionDataListAAL";
	/** The observatory graphic evolution data list ap. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AP = "evolutionDataListAP";
	/** The observatory graphic evolution data list ae. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AE = "evolutionDataListAE";
	/** The observatory graphic evolution data list an. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AN = "evolutionDataListAN";
	/** The observatory graphic global forward. */
	String OBSERVATORY_GRAPHIC_GLOBAL_FORWARD = "getGlobalGraphics";
	/** The observatory graphic segment forward. */
	String OBSERVATORY_GRAPHIC_SEGMENT_FORWARD = "getSegmentGraphics";
	/** The observatory graphic evolution forward. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_FORWARD = "getEvolutionGraphics";
	/** The observatory graphic aspect general id. */
	String OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID = "1";
	/** The observatory graphic aspect alternative id. */
	String OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID = "2";
	/** The observatory graphic aspect presentation id. */
	String OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID = "3";
	/** The observatory graphic aspect structure id. */
	String OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID = "4";
	/** The observatory graphic aspect navigation id. */
	String OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID = "5";
	/** The observatory graphic aspect general. */
	String OBSERVATORY_GRAPHIC_ASPECT_GENERAL = "General";
	/** The observatory graphic aspect alternative. */
	String OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE = "Alternativas";
	/** The observatory graphic aspect presentation. */
	String OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION = "Presentación";
	/** The observatory graphic aspect structure. */
	String OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE = "Estructura";
	/** The observatory graphic aspect navigation. */
	String OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION = "Navegación";
	/** The obs pagination result from. */
	String OBS_PAGINATION_RESULT_FROM = "resultFrom";
	/** The obs pagination resultna from. */
	String OBS_PAGINATION_RESULTNA_FROM = "resultFromNA";
	/** The obs pagination. */
	String OBS_PAGINATION = "pagination";
	/** The semilla ok nuevo. */
	String SEMILLA_OK_NUEVO = "semillaOKNuevo";
	/** The semilla ok modificar. */
	String SEMILLA_OK_MODIFICAR = "semillaOKModificar";
	/** The semilla eliminada. */
	String SEMILLA_ELIMINADA = "semillaEliminada";
	/** The semilla editada. */
	String SEMILLA_EDITADA = "semillaEditada";
	/** The usuario duplicado. */
	String USUARIO_DUPLICADO = "usuarioDuplicado";
	/** The formato fecha incorrecto. */
	String FORMATO_FECHA_INCORRECTO = "formatoFechaIncorrecto";
	/** The nuevo rastreo cliente. */
	String NUEVO_RASTREO_CLIENTE = "nuevoRastreoCliente";
	/** The export pdf type. */
	String EXPORT_PDF_TYPE = "exportType";
	/** The export pdf intav. */
	String EXPORT_PDF_INTAV = "intavPDF";
	/** The export pdf intav simple. */
	String EXPORT_PDF_INTAV_SIMPLE = "intavSimplePDF";
	/** The export pdf lenox. */
	String EXPORT_PDF_LENOX = "lenoxPDF";
	/** The export pdf regenerate. */
	String EXPORT_PDF_REGENERATE = "regeneratePDF";
	/** The export all pdfs. */
	String EXPORT_ALL_PDFS = "exportAllPdfs";
	/** The export all pdfs email. */
	String EXPORT_ALL_PDFS_EMAIL = "exportAllPdfsMail";
	/** The pdf error forward. */
	String PDF_ERROR_FORWARD = "verRastreosRealizados";
	/** The rojo inteco. */
	Color ROJO_INTECO = new Color(225, 18, 13);
	/** The rosa inteco. */
	Color ROSA_INTECO = new Color(255, 225, 225);
	/** The naranja mp. */
	Color NARANJA_MP = new Color(245, 164, 55);
	/** The verde o mp. */
	Color VERDE_O_MP = new Color(106, 130, 54);
	/** The verde c mp. */
	Color VERDE_C_MP = new Color(126, 154, 64);
	/** The marrron c nc. */
	Color MARRRON_C_NC = new Color(196, 89, 17);
	/** The color resultado 0 falla. */
	Color COLOR_RESULTADO_0_FALLA = new Color(225, 18, 13);
	// public static final Color COLOR_RESULTADO_0_PASA = new Color(247, 150,
	/** The color resultado 0 pasa. */
	// 70);
	Color COLOR_RESULTADO_0_PASA = NARANJA_MP;// ConstantsFont.WARNING_COLOR;
	/** The color resultado 1 pasa. */
	Color COLOR_RESULTADO_1_PASA = Color.WHITE;
	/** The gris muy claro. */
	Color GRIS_MUY_CLARO = new Color(245, 245, 245);
	/** The no dependence. */
	// ATRIBUTES
	String NO_DEPENDENCE = "no_dependence";
	/** The upload page. */
	String UPLOAD_PAGE = "upLoadPage";
	/** The upload file. */
	String UPLOAD_FILE = "upLoadFile";
	/** The aplication name. */
	String APLICATION_NAME = "/oaw";
	/** The user. */
	String USER = "user";
	/** The pass. */
	String PASS = "pass";
	/** The administrador. */
	String ADMINISTRADOR = "Administrador";
	/** The usuario. */
	String USUARIO = "usuario";
	/** The action. */
	String ACTION = "action";
	/** The accion. */
	String ACCION = "accion";
	/** The accion anadir. */
	String ACCION_ANADIR = "anadir";
	/** The accion aceptar. */
	String ACCION_ACEPTAR = "aceptar";
	/** The accion de observatorio. */
	String ACCION_DE_OBSERVATORIO = "deObservatorio";
	/** The accion subir. */
	String ACCION_SUBIR = "subir";
	/** The accion modificar. */
	String ACCION_MODIFICAR = "modificar";
	/** The accion reload. */
	String ACCION_RELOAD = "reload";
	/** The accion instalar. */
	String ACCION_INSTALAR = "instalar";
	/** The accion desinstalar. */
	String ACCION_DESINSTALAR = "desinstalar";
	/** The accion borrar. */
	String ACCION_BORRAR = "borrar";
	/** The accion borrar ejecucion. */
	String ACCION_BORRAR_EJECUCION = "borrarEx";
	/** The accion lanzar ejecucion. */
	String ACCION_LANZAR_EJECUCION = "lanzarEjecucion";
	/** The forward throw seed confirmation. */
	String FORWARD_THROW_SEED_CONFIRMATION = "throwSeedconfirmation";
	/** The accion confirmacion borrar. */
	String ACCION_CONFIRMACION_BORRAR = "confirmacion";
	/** The accion confirmacion borrar ex seed. */
	String ACCION_CONFIRMACION_BORRAR_EX_SEED = "confirmacionExSeed";
	/** The accion mostrar lista resultados. */
	String ACCION_MOSTRAR_LISTA_RESULTADOS = "listaResultados";
	/** The accion seed detail. */
	String ACCION_SEED_DETAIL = "seedDetailAction";
	/** The accion export all. */
	String ACCION_EXPORT_ALL = "exportAllSeeds";
	/** The accion import all. */
	String ACCION_IMPORT_ALL = "loadSeedsFile";
	/** The accion separate seed. */
	String ACCION_SEPARATE_SEED = "separateSeed";
	/** The action get evaluation. */
	String ACTION_GET_EVALUATION = "getEvaluation";
	/** The action get detail. */
	String ACTION_GET_DETAIL = "getDetail";
	/** The action recover evaluation. */
	String ACTION_RECOVER_EVALUATION = "recoverEvaluation";
	/** The action get html source. */
	String ACTION_GET_HTML_SOURCE = "getHtmlSource";
	/** The forward recover evaluation. */
	String FORWARD_RECOVER_EVALUATION = "showAnalysisFromCrawlerRecover";
	/** The accion edit seed. */
	String ACCION_EDIT_SEED = "editSeed";
	/** The forward observatory edit seed. */
	String FORWARD_OBSERVATORY_EDIT_SEED = "editObservatorySeed";
	/** The export all pdf forward. */
	String EXPORT_ALL_PDF_FORWARD = "exportAllPdfs";
	/** The export all pdf forward multilanguage. */
	String EXPORT_ALL_PDF_FORWARD_MULTILANGUAGE = "exportAllPdfsM";
	/** The export all pdf forward mail. */
	String EXPORT_ALL_PDF_FORWARD_MAIL = "exportAllPdfsMail";
	/** The accion add seed. */
	String ACCION_ADD_SEED = "addSeed";
	/** The seed detail. */
	String SEED_DETAIL = "seedDetail";
	/** The username. */
	String USERNAME = "username";
	/** The usuario modificar. */
	String USUARIO_MODIFICAR = "usuarioModificar";
	/** The configurador. */
	String CONFIGURADOR = "Configurador";
	/** The visualizador. */
	String VISUALIZADOR = "Visualizador";
	/** The valemail. */
	String VALEMAIL = "valEmail";
	/** The role. */
	String ROLE = "role";
	/** The role type. */
	String ROLE_TYPE = "roleType";
	/** The file cartucho. */
	String FILE_CARTUCHO = "cartucho";
	/** The cartucho. */
	String CARTUCHO = "cartucho";
	/** The param control. */
	String PARAM_CONTROL = "paramControl";
	/** The rastreos. */
	String RASTREOS = "rastreos";
	/** The rastreos realizados. */
	String RASTREOS_REALIZADOS = "rastreosRealizados";
	/** The rastreos cliente. */
	String RASTREOS_CLIENTE = "rastreosCliente";
	/** The hilos. */
	String HILOS = "hilos";
	/** The confirmacion. */
	String CONFIRMACION = "confirmacion";
	/** The confirmacion2. */
	String CONFIRMACION2 = "confirmacion2";
	/** The edit seed. */
	String EDIT_SEED = "editSeed";
	/** The confirmacion si. */
	String CONFIRMACION_SI = "si";
	/** The category name. */
	String CATEGORY_NAME = "namecat";
	/** The id categoria. */
	String ID_CATEGORIA = "idcat";
	/** The id ambito. */
	String ID_AMBITO = "idamb";
	/** The id complejidad. */
	String ID_COMPLEJIDAD = "idcom";
	/** The id etiqueta. */
	String ID_ETIQUETA = "ideti";
	/** The id clasificacion. */
	String ID_CLASIFICACION = "idclas";
	/** The id tematica. */
	String ID_TEMATICA = "idtem";
	/** The id distribucion. */
	String ID_DISTRIBUCION = "iddis";
	/** The id recurrencia. */
	String ID_RECURRENCIA = "idrec";
	/** The id cartucho. */
	String ID_CARTUCHO = "idCartucho";
	/** The id termino. */
	String ID_TERMINO = "idter";
	/** The id categoria termino. */
	String ID_CATEGORIA_TERMINO = "idcatTerminoCat";
	/** The id termino categoria. */
	String ID_TERMINO_CATEGORIA = "idcatTerminoCat";
	/** The nombre antiguo. */
	String NOMBRE_ANTIGUO = "nombreAntiguo";
	/** The action for. */
	String ACTION_FOR = "actionFor";
	/** The accion for. */
	String ACCION_FOR = "accionFor";
	/** The estado anterior. */
	String ESTADO_ANTERIOR = "estado_anterior";
	/** The rastreo. */
	String RASTREO = "rastreo";
	/** The rastreo list form. */
	String RASTREO_LIST_FORM = "rastreoForm";
	/** The rastreos realizados form. */
	String RASTREOS_REALIZADOS_FORM = "rrForm";
	/** The rastreo intermedio. */
	String RASTREO_INTERMEDIO = "rastreoIntermedio";
	/** The comando. */
	String COMANDO = "comando";
	/** The com user. */
	String COM_USER = "user";
	/** The com pass. */
	String COM_PASS = "pass";
	/** The veces timeout. */
	String VECES_TIMEOUT = "vecesTimeout";
	/** The veces borrado. */
	String VECES_BORRADO = "vecesBorrado";
	/** The launch. */
	String LAUNCH = "LAUNCH";
	/** The lanzar1. */
	String LANZAR1 = "LANZAR";
	/** The stop. */
	String STOP = "STOP";
	/** The parar1. */
	String PARAR1 = "PARA";
	/** The lanzar. */
	String LANZAR = "Lanzar ";
	/** The lanzado. */
	String LANZADO = "LANZANDO ";
	/** The stopped. */
	String STOPPED = "stopped";
	/** The terminado. */
	String TERMINADO = "terminado";
	/** The on. */
	String ON = "on";
	/** The parar. */
	String PARAR = "Parar ";
	/** The parando. */
	String PARANDO = "PARANDO ";
	/** The opcion. */
	String OPCION = "opcion";
	/** The opcion intermedio. */
	String OPCION_INTERMEDIO = "opcionIntermedio";
	/** The intermedio. */
	String INTERMEDIO = "intermedio";
	/** The opcion lanzar. */
	String OPCION_LANZAR = "lanzar";
	/** The estado. */
	String ESTADO = "estado";
	/** The estado intermedio. */
	String ESTADO_INTERMEDIO = "estadoIntermedio";
	/** The estado terminado. */
	String ESTADO_TERMINADO = "TERMINADO";
	/** The estado parado. */
	String ESTADO_PARADO = "PARADO";
	/** The user intermedio. */
	String USER_INTERMEDIO = "userIntermedio";
	/** The pass intermedio. */
	String PASS_INTERMEDIO = "passIntermedio";
	/** The opcion estadistica. */
	String OPCION_ESTADISTICA = "estadistica";
	/** The id sesion. */
	String ID_SESION = "idSesion";
	/** The encriptado. */
	String ENCRIPTADO = "encriptado";
	/** The opcion ampliar. */
	String OPCION_AMPLIAR = "opcion_ampliar";
	/** The cod rastreo. */
	String COD_RASTREO = "cod_rastreo";
	/** The user ampliar. */
	String USER_AMPLIAR = "user_ampliar";
	/** The pass ampliar. */
	String PASS_AMPLIAR = "pass_ampliar";
	/** The ruta. */
	String RUTA = "ruta";
	/** The tar1. */
	String TAR1 = "tar1";
	/** The id rastreo semilla. */
	String ID_RASTREO_SEMILLA = "idRastreoSemilla";
	/** The tipo lista. */
	String TIPO_LISTA = "tipoLista";
	/** The lista no. */
	String LISTA_NO = "listaNo";
	/** The archivo lista no. */
	String ARCHIVO_LISTA_NO = "archivoListaNo";
	/** The lista rast. */
	String LISTA_RAST = "listaRast";
	/** The archivo lista rast. */
	String ARCHIVO_LISTA_RAST = "archivoListaRast";
	/** The id rastreo. */
	String ID_RASTREO = "idrastreo";
	/** The ruta aleatoria. */
	String RUTA_ALEATORIA = "rutaAleatoria";
	/** The fecha. */
	String FECHA = "fecha";
	/** The hora. */
	String HORA = "hora";
	/** The no. */
	String NO = "NO";
	/** The si. */
	String SI = "SÍ";
	/** The conf no. */
	String CONF_NO = "no";
	/** The conf si. */
	String CONF_SI = "si";
	/** The categorizacion vector. */
	String CATEGORIZACION_VECTOR = "categorizacionVector";
	/** The action script. */
	String ACTION_SCRIPT = "actionScript";
	/** The todos. */
	String TODOS = "Todos";
	/** The cartuchos vector. */
	String CARTUCHOS_VECTOR = "cartuchosVector";
	/** The tipos vector. */
	String TIPOS_VECTOR = "tiposVector";
	/** The ambitos vector. */
	String AMBITOS_VECTOR = "ambitosVector";
	/** The hilos vector. */
	String HILOS_VECTOR = "hilosVector";
	/** The modos vector. */
	String MODOS_VECTOR = "modosVector";
	/** The profundidades vector. */
	String PROFUNDIDADES_VECTOR = "profundidadesVector";
	/** The topn vector. */
	String TOPN_VECTOR = "topnVector";
	/** The alertas vector. */
	String ALERTAS_VECTOR = "alertasVector";
	/** The codigo. */
	String CODIGO = "codigo";
	/** The html. */
	String HTML = "html";
	/** The htm. */
	String HTM = "htm";
	/** The asp. */
	String ASP = "asp";
	/** The jsp. */
	String JSP = "jsp";
	/** The pdf. */
	String PDF = "pdf";
	/** The txt. */
	String TXT = "txt";
	/** The php. */
	String PHP = "php";
	/** The url semilla. */
	String URL_SEMILLA = "url_semilla";
	/** The umbral alarma. */
	String UMBRAL_ALARMA = "umbralAlarma";
	/** The white list. */
	String WHITE_LIST = "whiteList";
	/** The editar semilla. */
	String EDITAR_SEMILLA = "editarSemilla";
	/** The editar lista no. */
	String EDITAR_LISTA_NO = "editarListaNo";
	/** The editar lista rast. */
	String EDITAR_LISTA_RAST = "editarListaRast";
	/** The local lista no. */
	String LOCAL_LISTA_NO = "localListaNo";
	/** The local lista rast. */
	String LOCAL_LISTA_RAST = "localListaRast";
	/** The no disponible. */
	String NO_DISPONIBLE = "NO DISPONIBLE";
	/** The nombre antiguo2. */
	String NOMBRE_ANTIGUO2 = "nombre_antiguo";
	/** The rastreo antiguo. */
	String RASTREO_ANTIGUO = "rastreo_antiguo";
	/** The tipo. */
	String TIPO = "tipo";
	/** The nuevo. */
	String NUEVO = "nuevo";
	/** The tipo subir semilla. */
	String TIPO_SUBIR_SEMILLA = "tipoSubirSemilla";
	/** The ruta para volver. */
	String RUTA_PARA_VOLVER = "rutaParaVolver";
	/** The archivo d. */
	String ARCHIVO_D = "archivo_d";
	/** The urls string. */
	String URLS_STRING = "urls_string";
	/** The nuevo termino. */
	String NUEVO_TERMINO = "nuevoTermino";
	/** The segunda. */
	String SEGUNDA = "segunda";
	/** The semilla. */
	String SEMILLA = "semilla";
	/** The cargar semilla. */
	String CARGAR_SEMILLA = "Cargar Semilla";
	/** The control. */
	String CONTROL = "control";
	/** The query. */
	String QUERY = "query";
	/** The paginas. */
	String PAGINAS = "paginas";
	/** The archivo. */
	String ARCHIVO = "archivo";
	/** The aceptar sobreescribir. */
	String ACEPTAR_SOBREESCRIBIR = "aceptarSobreescribir";
	/** The modificar rastreo jsp. */
	String MODIFICAR_RASTREO_JSP = "modificarRastreo.jsp";
	/** The nuevo rastreo jsp. */
	String NUEVO_RASTREO_JSP = "nuevoRastreo.jsp";
	/** The lista no rast may. */
	String LISTA_NO_RAST_MAY = "Lista no Rastreable";
	/** The lista no rast min. */
	String LISTA_NO_RAST_MIN = "Lista No Rastreable";
	/** The lista rast may. */
	String LISTA_RAST_MAY = "Lista Rastrebale";
	/** The lista rast min. */
	String LISTA_RAST_MIN = "Lista Rastreable";
	/** The lista semillas. */
	String LISTA_SEMILLAS = "listaSemillas";
	/** The de menu. */
	String DE_MENU = "deMenu";
	/** The de cuenta. */
	String DE_CUENTA = "deCuenta";
	/** The rol conf. */
	String ROL_CONF = "conf";
	/** The de pass. */
	String DE_PASS = "dePass";
	/** The rol visor. */
	String ROL_VISOR = "visor";
	/** The rol admin. */
	String ROL_ADMIN = "admin";
	/** The check. */
	String CHECK = "check";
	/** The if admin. */
	String IF_ADMIN = "ifadmin";
	/** The if visor. */
	String IF_VISOR = "ifvisor";
	/** The if config. */
	String IF_CONFIG = "ifconfig";
	/** The if config admin. */
	String IF_CONFIG_ADMIN = "ifConfigAdmin";
	/** The crawlings form. */
	String CRAWLINGS_FORM = "crawlingsForm";
	/** The cargar cartucho form. */
	String CARGAR_CARTUCHO_FORM = "CargarCartuchosForm";
	/** The nuevo usuario sistema form. */
	String NUEVO_USUARIO_SISTEMA_FORM = "NuevoUsuarioSistemaForm";
	/** The cargar categoria form. */
	String CARGAR_CATEGORIA_FORM = "CargarCategoriasForm";
	/** The cargar cuenta usuario form. */
	String CARGAR_CUENTA_USUARIO_FORM = "CargarCuentasUsuarioForm";
	/** The cargar observatorio form. */
	String CARGAR_OBSERVATORIO_FORM = "CargarObservatorioForm";
	/** The nuevo observatorio form. */
	String NUEVO_OBSERVATORIO_FORM = "NuevoObservatorioForm";
	/** The modificar observatorio form. */
	String MODIFICAR_OBSERVATORIO_FORM = "ModificarObservatorioForm";
	/** The cargar rastreos form. */
	String CARGAR_RASTREOS_FORM = "CargarRastreosForm";
	/** The cargar usuarios sistema form. */
	String CARGAR_USUARIOS_SISTEMA_FORM = "CargarUsuariosSistemaForm";
	/** The id usuario. */
	String ID_USUARIO = "idUsuario";
	/** The id. */
	String ID = "id";
	/** The id check. */
	String ID_CHECK = "idCheck";
	/** The code. */
	String CODE = "code";
	/** The get regenerate. */
	String GET_REGENERATE = "getRegenerate";
	/** The es primera. */
	String ES_PRIMERA = "esPrimera";
	/** The listado cuentas cliente. */
	String LISTADO_CUENTAS_CLIENTE = "listadoCuentasCliente";
	/** The listado cartuchos. */
	String LISTADO_CARTUCHOS = "listadoCartuchos";
	/** The listado semillas. */
	String LISTADO_SEMILLAS = "listadoSemillas";
	/** The listado normas. */
	String LISTADO_NORMAS = "listadoNormas";
	/** The minutes. */
	String MINUTES = "minutes";
	/** The hours. */
	String HOURS = "hours";
	/** The list analysis. */
	String LIST_ANALYSIS = "listAnalysis";
	/** The evaluation form. */
	String EVALUATION_FORM = "evaluationForm";
	/** The failed checks. */
	String FAILED_CHECKS = "failedChecks";
	/** The menu. */
	String MENU = "menu";
	/** The submenu importar entidad. */
	String SUBMENU_IMPORTAR = "menuImportar";
	/** The submenu exportar entidad. */
	String SUBMENU_EXPORTAR = "menuExportar";
	/** The submenu. */
	String SUBMENU = "submenu";
	/** The menu password. */
	String MENU_PASSWORD = "menuPassword";
	/** The menu users. */
	String MENU_USERS = "menuUsers";
	/** The menu client. */
	String MENU_CLIENT = "menuClient";
	/** The menu inteco obs. */
	String MENU_INTECO_OBS = "menuObservatory";
	/** The menu other options. */
	String MENU_OTHER_OPTIONS = "menuOtherOptions";
	/** The menu servicio diagnostico. */
	String MENU_SERVICIO_DIAGNOSTICO = "menuServicioDiagnostico";
	/** The menu crawlings. */
	String MENU_CRAWLINGS = "menuCrawlings";
	/** The menu seeds. */
	String MENU_SEEDS = "menuSeeds";
	/** The menu client crawlings. */
	String MENU_CLIENT_CRAWLINGS = "menuClientCrawlings";
	/** The menu client crawlings account. */
	String MENU_CLIENT_CRAWLINGS_ACCOUNT = "menuClientCrawlingsAccount";
	/** The menu certificates. */
	String MENU_CERTIFICATES = "menuCertificates";
	/** The submenu ip. */
	String SUBMENU_IP = "submenuIp";
	/** The submenu google. */
	String SUBMENU_GOOGLE = "submenuGoogle";
	/** The submenu listado sem. */
	String SUBMENU_LISTADO_SEM = "submenuListadoSem";
	/** The submenu categories. */
	String SUBMENU_CATEGORIES = "submenuCategories";
	/** The submenu obs semilla. */
	String SUBMENU_OBS_SEMILLA = "submenuSemillaObs";
	/** The submenu obs dependencias. */
	String SUBMENU_OBS_DEPENDENCIAS = "submenuDependenciasObs";
	/** The submenu obs complejidades. */
	String SUBMENU_OBS_COMPLEJIDADES = "submenuComplejidadesObs";
	/** The submenu obs plantillas. */
	String SUBMENU_OBS_PLANTILLAS = "submenuPlantillassObs";
	/** The submenu obs reducirtablas. */
	String SUBMENU_OBS_REDUCIRTABLAS = "submenuReducirTablasObs";
	/** The submenu obs etiquetas. */
	String SUBMENU_OBS_ETIQUETAS = "submenuEtiquetasObs";
	/** The submenu obs ranges. */
	String SUBMENU_OBS_RANGES = "submenuRangesObs";
	/** The submenu observatorio. */
	String SUBMENU_OBSERVATORIO = "submenuObservatorio";
	/** The submenu servicio diagnostico. */
	String SUBMENU_SERVICIO_DIAGNOSTICO = "submenuServicioDiagnostico";
	/** The submenu conectividad. */
	String SUBMENU_CONECTIVIDAD = "submenuConectividad";
	/** The url. */
	String URL = "url";
	/** The broken links. */
	String BROKEN_LINKS = "brokenLinks";
	/** The alias. */
	String ALIAS = "alias";
	/** The certificates. */
	String CERTIFICATES = "certificates";
	/** The categories list. */
	String CATEGORIES_LIST = "categoriesList";
	/** The dependencies list. */
	String DEPENDENCIES_LIST = "dependenciesList";
	/** The ambits list. */
	String AMBITS_LIST = "ambitsList";
	/** The complexities list. */
	String COMPLEXITIES_LIST = "complexitiesList";
	/** The ver cuenta usuario form. */
	String VER_CUENTA_USUARIO_FORM = "VerCuentaUsuarioForm";
	/** The ver observatorio form. */
	String VER_OBSERVATORIO_FORM = "VerObservatorioForm";
	/** The list accounts. */
	String LIST_ACCOUNTS = "listAccounts";
	/** The get detail. */
	String GET_DETAIL = "getDetail";
	/** The return observatory results. */
	String RETURN_OBSERVATORY_RESULTS = "observatoryResults";
	/** The load certificate form. */
	String LOAD_CERTIFICATE_FORM = "loadCertificateForm";
	/** The upload certificate. */
	String UPLOAD_CERTIFICATE = "uploadCertificate";
	/** The delete certificate. */
	String DELETE_CERTIFICATE = "deleteCertificate";
	/** The certificate alias. */
	String CERTIFICATE_ALIAS = "certificateAlias";
	/** The num certificates. */
	String NUM_CERTIFICATES = "numCertificates";
	/** The delete confirmation. */
	String DELETE_CONFIRMATION = "deleteConfirmation";
	/** The status not launched. */
	int STATUS_NOT_LAUNCHED = 1;
	/** The status launched. */
	int STATUS_LAUNCHED = 2;
	/** The status stopped. */
	int STATUS_STOPPED = 3;
	/** The status finalized. */
	int STATUS_FINALIZED = 4;
	/** The all data. */
	long ALL_DATA = 0;
	/** The client account type. */
	int CLIENT_ACCOUNT_TYPE = 0;
	/** The observatory type. */
	int OBSERVATORY_TYPE = 1;
	/** The list page links. */
	String LIST_PAGE_LINKS = "listPageLinks";
	/** The list page links2. */
	String LIST_PAGE_LINKS2 = "listPageLinks2";
	/** The pag param. */
	String PAG_PARAM = "p";
	/** The pag param2. */
	String PAG_PARAM2 = "p2";
	/** The no paginacion. */
	int NO_PAGINACION = -1;
	/** The begin. */
	String BEGIN = "begin";
	/** The page number. */
	String PAGE_NUMBER = "numPag";
	/** The pagination current style. */
	String PAGINATION_CURRENT_STYLE = "current";
	/** The pagination style. */
	String PAGINATION_STYLE = "pagination";
	/** The pagination pf style. */
	String PAGINATION_PF_STYLE = "paginationPF";
	/** The es cliente. */
	String ES_CLIENTE = "esCliente";
	/** The is client. */
	String IS_CLIENT = "isCliente";
	/** The is primary. */
	String IS_PRIMARY = "isPrimary";
	/** The es observatorio. */
	String ES_OBSERVATORIO = "esObserv";
	/** The id cuenta. */
	String ID_CUENTA = "id_cuenta";
	/** The id observatorio. */
	String ID_OBSERVATORIO = "id_observatorio";
	/** The application. */
	String APPLICATION = "application";
	/** The id ex obs. */
	String ID_EX_OBS = "idExObs";
	/** The cuenta eliminar. */
	String CUENTA_ELIMINAR = "eliminarCuenta";
	/** The ancla pdf. */
	String ANCLA_PDF = "anclaPdf";
	/** The boton semilla google. */
	String BOTON_SEMILLA_GOOGLE = "botonSemillaGoogle";
	/** The boton semilla ip. */
	String BOTON_SEMILLA_IP = "botonSemillaIp";
	/** The boton semilla web. */
	String BOTON_SEMILLA_WEB = "botonSemillaWeb";
	/** The inicial. */
	String INICIAL = "inicial";
	/** The id lista all. */
	int ID_LISTA_ALL = -1;
	/** The id lista semilla. */
	int ID_LISTA_SEMILLA = 1;
	/** The id lista rastreable. */
	int ID_LISTA_RASTREABLE = 2;
	/** The id lista no rastreable. */
	int ID_LISTA_NO_RASTREABLE = 3;
	/** The id lista semilla observatorio. */
	int ID_LISTA_SEMILLA_OBSERVATORIO = 4;
	/** The id observatory guideline. */
	String ID_OBSERVATORY_GUIDELINE = "4";
	/** The observatory graphic intav. */
	String OBSERVATORY_GRAPHIC_INTAV = "getObservatoryGraphicIntav";
	/** The get seed results forward. */
	String GET_SEED_RESULTS_FORWARD = "resultadosObservatorioSemillas";
	/** The seed list. */
	String SEED_LIST = "seedList";
	/** The observatory seed list. */
	String OBSERVATORY_SEED_LIST = "observatorySeedList";
	/** The observatory form. */
	String OBSERVATORY_FORM = "observatoryForm";
	/** The observatory seed form. */
	String OBSERVATORY_SEED_FORM = "SemillaObservatorioForm";
	/** The observatory id. */
	String OBSERVATORY_ID = "id_observatorio";
	/** The observatory. */
	String OBSERVATORY = "observatorio";
	/** The load observatory mod forward. */
	String LOAD_OBSERVATORY_MOD_FORWARD = "loadObsMod";
	/** The volver mod semilla. */
	String VOLVER_MOD_SEMILLA = "modSeemilla";
	/** The semilla form. */
	String SEMILLA_FORM = "SemillaForm";
	/** The semilla search form. */
	String SEMILLA_SEARCH_FORM = "SemillaSearchForm";
	/** The id semilla. */
	String ID_SEMILLA = "idSemilla";
	/** The is new. */
	String IS_NEW = "isNew";
	/** The config. */
	String CONFIG = "config";
	/** The edit. */
	String EDIT = "edit";
	/** The config form. */
	String CONFIG_FORM = "configForm";
	/** The config c form. */
	String CONFIG_C_FORM = "ConfigCForm";
	/** The config d form. */
	String CONFIG_D_FORM = "ConfigDForm";
	/** The submit edit. */
	String SUBMIT_EDIT = "submitEdit";
	/** The terms. */
	String TERMS = "terms";
	/** The delete terms. */
	String DELETE_TERMS = "deleteTerms";
	/** The id hit. */
	String ID_HIT = "idHit";
	/** The header. */
	String HEADER = "header";
	/** The title. */
	String TITLE = "title";
	/** The info. */
	String INFO = "info";
	/** The caption. */
	String CAPTION = "caption";
	/** The confirmacion eliminar term. */
	String CONFIRMACION_ELIMINAR_TERM = "eliminaTerminoConf";
	/** The confirmacion delete. */
	String CONFIRMACION_DELETE = "confirmacionDelete";
	/** The confirmacion relanzar. */
	String CONFIRMACION_RELANZAR = "confirmacionRelanzar";
	/** The confirmacion importar. */
	String CONFIRMACION_IMPORTAR = "confirmacionImportar";
	/** The configurar filtros agregados. */
	String CONFIGURAR_FILTROS_AGREGADOS = "configurarFiltrosAgregados";
	/** The configurar filtros agregados acc. */
	String CONFIGURAR_FILTROS_AGREGADOS_ACC = "configurarFiltrosAgregadosAcc";
	/** The term. */
	String TERM = "term";
	/** The malware term form. */
	String MALWARE_TERM_FORM = "MalwareTermForm";
	/** The save term. */
	String SAVE_TERM = "saveTerm";
	/** The edit term. */
	String EDIT_TERM = "editTerm";
	/** The is update. */
	String IS_UPDATE = "isUpdate";
	/** The load. */
	String LOAD = "load";
	/** The multimedia file. */
	String MULTIMEDIA_FILE = "MultimediaFile";
	/** The type list file. */
	String TYPE_LIST_FILE = "typeListFile";
	/** The type list file bl. */
	String TYPE_LIST_FILE_BL = "bl";
	/** The type list file wl. */
	String TYPE_LIST_FILE_WL = "wl";
	/** The list file. */
	String LIST_FILE = "listFile";
	/** The edit file form. */
	String EDIT_FILE_FORM = "EditFileForm";
	/** The file content list. */
	String FILE_CONTENT_LIST = "fileContentList";
	/** The show file. */
	String SHOW_FILE = "showFile";
	/** The file. */
	String FILE = "file";
	/** The complexity segment none. */
	int COMPLEXITY_SEGMENT_NONE = 0;
	/** The score. */
	String SCORE = "score";
	/** The obs value not score. */
	int OBS_VALUE_NOT_SCORE = 0;
	/** The obs value red zero. */
	int OBS_VALUE_RED_ZERO = 1;
	/** The obs value green zero. */
	int OBS_VALUE_GREEN_ZERO = 2;
	/** The obs value green one. */
	int OBS_VALUE_GREEN_ONE = 3;
	/** The obs value green suffix. */
	String OBS_VALUE_GREEN_SUFFIX = "_green";
	/** The obs value red suffix. */
	String OBS_VALUE_RED_SUFFIX = "_red";
	/** The obs value no compilance suffix. */
	String OBS_VALUE_NO_COMPILANCE_SUFFIX = "_compilanceN";
	/** The obs value no apply compliance suffix. */
	String OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX = "_compilanceA";
	/** The obs value compilance suffix. */
	String OBS_VALUE_COMPILANCE_SUFFIX = "_compilanceS";
	/** The obs a. */
	String OBS_A = "A";
	/** The obs aa. */
	String OBS_AA = "AA";
	/** The obs nv. */
	String OBS_NV = "NV";
	/** The obs parcial. */
	String OBS_PARCIAL = "PARCIAL";
	/** The obs a label. */
	String OBS_A_LABEL = "A";
	/** The obs aa label. */
	String OBS_AA_LABEL = "AA";
	/** The obs nv label. */
	String OBS_NV_LABEL = "No Válido";
	/** The obs type none. */
	String OBS_TYPE_NONE = "None";
	/** The obs n1. */
	String OBS_N1 = "Nivel 1";
	/** The obs n2. */
	String OBS_N2 = "Nivel 2";
	/** The obs priority 1. */
	String OBS_PRIORITY_1 = "Priority 1";
	/** The obs priority 2. */
	String OBS_PRIORITY_2 = "Priority 2";
	/** The obs priority none. */
	String OBS_PRIORITY_NONE = "none";
	/** The obs compilance. */
	String OBS_COMPILANCE = "Conforme";
	/** The obs compilance none. */
	String OBS_COMPILANCE_NONE = messageResources.getMessage("resultados.anonimos.porc.portales.nc");
	/** The obs compilance partial. */
	String OBS_COMPILANCE_PARTIAL = messageResources.getMessage("resultados.anonimos.porc.portales.pc");;
	/** The obs compilance full. */
	String OBS_COMPILANCE_FULL = messageResources.getMessage("resultados.anonimos.porc.portales.tc");
	/** The obs compilance na. */
	String OBS_COMPILANCE_NA = messageResources.getMessage("resultados.anonimos.porc.portales.na");
	/** The obs accesibility full. */
	String OBS_ACCESIBILITY_FULL = "Completo";
	/** The obs accesibility partial. */
	String OBS_ACCESIBILITY_PARTIAL = "Parcial";
	/** The obs accesibility none. */
	String OBS_ACCESIBILITY_NONE = "No válido";
	/** The obs accesibility na. */
	String OBS_ACCESIBILITY_NA = "Sin declaración";
	/** The add observatory seed list. */
	String ADD_OBSERVATORY_SEED_LIST = "addSeedList";
	/** The other observatory seed list. */
	String OTHER_OBSERVATORY_SEED_LIST = "otherSeedList";
	/** The results pagination initial value. */
	String RESULTS_PAGINATION_INITIAL_VALUE = "resultsPaginationInitialValue";
	/** The check results detail. */
	String CHECK_RESULTS_DETAIL = "checkResultsDetail";
	/** The problema. */
	String PROBLEMA = "problem";
	/** The status error. */
	int STATUS_ERROR = 0;
	/** The status success. */
	int STATUS_SUCCESS = 1;
	/** The status executing. */
	int STATUS_EXECUTING = 2;
	/** The global score. */
	String GLOBAL_SCORE = "globalScore";
	/** The level score. */
	String LEVEL_SCORE = "levelScore";
	/** The suitability score. */
	String SUITABILITY_SCORE = "suitabilityScore";
	/** The observatory key cache. */
	String OBSERVATORY_KEY_CACHE = "observatoryKeyCache";
	/** The ministerio p. */
	String MINISTERIO_P = "MP";
	/** The observatory type age. */
	int OBSERVATORY_TYPE_AGE = 1;
	/** The observatory type ccaa. */
	int OBSERVATORY_TYPE_CCAA = 2;
	/** The observatory type eell. */
	int OBSERVATORY_TYPE_EELL = 3;
	/** The observatory type prensa. */
	int OBSERVATORY_TYPE_PRENSA = 4;
	/** The param url. */
	// Servicio básico de validación
	String PARAM_URL = "url";
	/** The param email. */
	String PARAM_EMAIL = "correo";
	/** The param width. */
	String PARAM_WIDTH = "amplitud";
	/** The param depth. */
	String PARAM_DEPTH = "profundidad";
	/** The param report. */
	String PARAM_REPORT = "informe";
	/** The param user. */
	String PARAM_USER = "usuario";
	/** The param content. */
	String PARAM_CONTENT = "content";
	/** The param complexity. */
	String PARAM_COMPLEXITY = "complexity";
	/** The param depth report. */
	String PARAM_DEPTH_REPORT = "depthReport";
	/** The param in directory. */
	String PARAM_IN_DIRECTORY = "inDirectory";
	/** The execute. */
	String EXECUTE = "execute";
	/** The report observatory. */
	String REPORT_OBSERVATORY = "observatorio-1";
	/** The report observatory 1 nobroken. */
	String REPORT_OBSERVATORY_1_NOBROKEN = "observatorio-1-nobroken";
	/** The report observatory 2. */
	String REPORT_OBSERVATORY_2 = "observatorio-2";
	/** The report observatory 2 nobroken. */
	String REPORT_OBSERVATORY_2_NOBROKEN = "observatorio-2-nobroken";
	/** The report observatory 3. */
	String REPORT_OBSERVATORY_3 = "observatorio-3";
	/** The report observatory 3 nobroken. */
	String REPORT_OBSERVATORY_3_NOBROKEN = "observatorio-3-nobroken";
	/** The report observatory 4. */
	String REPORT_OBSERVATORY_4 = "observatorio-4";
	/** The report observatory 4 nobroken. */
	String REPORT_OBSERVATORY_4_NOBROKEN = "observatorio-4-nobroken";
	/** The report observatory 5. */
	String REPORT_OBSERVATORY_5 = "observatorio-5";
	/** The report observatory 5 nobroken. */
	String REPORT_OBSERVATORY_5_NOBROKEN = "observatorio-5-nobroken";
	/** The report une. */
	String REPORT_UNE = "une";
	/** The report observatory file. */
	String REPORT_OBSERVATORY_FILE = "observatorio-inteco-1-0";
	/** The report une file. */
	String REPORT_UNE_FILE = "une-139803";
	/** The report wcag 1 file. */
	String REPORT_WCAG_1_FILE = "wcag-1-0";
	/** The report wcag 2 file. */
	String REPORT_WCAG_2_FILE = "wcag-2-0";
	/** The basic service status launched. */
	String BASIC_SERVICE_STATUS_LAUNCHED = "launched";
	/** The basic service status queued. */
	String BASIC_SERVICE_STATUS_QUEUED = "queued";
	/** The basic service status scheduled. */
	String BASIC_SERVICE_STATUS_SCHEDULED = "scheduled";
	/** The basic service status missing params. */
	String BASIC_SERVICE_STATUS_MISSING_PARAMS = "missing_params";
	/** The basic service status finished. */
	String BASIC_SERVICE_STATUS_FINISHED = "finished";
	/** The basic service status error. */
	String BASIC_SERVICE_STATUS_ERROR = "error";
	/** The basic service status ruled out. */
	String BASIC_SERVICE_STATUS_RULED_OUT = "ruled_out";
	/** The basic service status not crawled. */
	String BASIC_SERVICE_STATUS_NOT_CRAWLED = "not_crawled";
	/** The basic service status error sending email. */
	String BASIC_SERVICE_STATUS_ERROR_SENDING_EMAIL = "error_sending_email";
	/** The basic service status http request error. */
	String BASIC_SERVICE_STATUS_HTTP_REQUEST_ERROR = "http_request_error";
	/** The request type. */
	// HTML
	String REQUEST_TYPE = "type";
	/** The introduction. */
	String INTRODUCTION = "intr";
	/** The objective. */
	String OBJECTIVE = "obj";
	/** The methodology. */
	String METHODOLOGY = "met";
	/** The methodology sub1. */
	String METHODOLOGY_SUB1 = "metSub1";
	/** The methodology sub2. */
	String METHODOLOGY_SUB2 = "metSub2";
	/** The methodology sub3. */
	String METHODOLOGY_SUB3 = "metSub3";
	/** The methodology sub4. */
	String METHODOLOGY_SUB4 = "metSub4";
	/** The global results. */
	String GLOBAL_RESULTS = "glob";
	/** The global results2. */
	String GLOBAL_RESULTS2 = "glob2";
	/** The global results3. */
	String GLOBAL_RESULTS3 = "glob3";
	/** The global results4. */
	String GLOBAL_RESULTS4 = "glob4";
	/** The global results5. */
	String GLOBAL_RESULTS5 = "glob5";
	/** The global results6. */
	String GLOBAL_RESULTS6 = "glob6";
	/** The segment results 1. */
	String SEGMENT_RESULTS_1 = "seg1";
	/** The segment results 2. */
	String SEGMENT_RESULTS_2 = "seg2";
	/** The segment results 3. */
	String SEGMENT_RESULTS_3 = "seg3";
	/** The segment results 4. */
	String SEGMENT_RESULTS_4 = "seg4";
	/** The segment results 5. */
	String SEGMENT_RESULTS_5 = "seg5";
	/** The evolution results. */
	String EVOLUTION_RESULTS = "evol";
	/** The evolution results1. */
	String EVOLUTION_RESULTS1 = "evol1";
	/** The evolution results2. */
	String EVOLUTION_RESULTS2 = "evol2";
	/** The evolution results3. */
	String EVOLUTION_RESULTS3 = "evol3";
	/** The evolution results4. */
	String EVOLUTION_RESULTS4 = "evol4";
	/** The conclusion. */
	String CONCLUSION = "conc";
	/** The segment conclusion. */
	String SEGMENT_CONCLUSION = "segConc";
	/** The evolution conclusion. */
	String EVOLUTION_CONCLUSION = "evConc";
	/** The introduction file. */
	String INTRODUCTION_FILE = "index.html";
	/** The objective file. */
	String OBJECTIVE_FILE = "objective.html";
	/** The methodology file. */
	String METHODOLOGY_FILE = "methodology.html";
	/** The methodology sub1 file. */
	String METHODOLOGY_SUB1_FILE = "methodology1.html";
	/** The methodology sub2 file. */
	String METHODOLOGY_SUB2_FILE = "methodology2.html";
	/** The methodology sub3 file. */
	String METHODOLOGY_SUB3_FILE = "methodology3.html";
	/** The methodology sub4 file. */
	String METHODOLOGY_SUB4_FILE = "methodology4.html";
	/** The global results file. */
	String GLOBAL_RESULTS_FILE = "global_result.html";
	/** The global results2 file. */
	String GLOBAL_RESULTS2_FILE = "global_result2.html";
	/** The global results3 file. */
	String GLOBAL_RESULTS3_FILE = "global_result3.html";
	/** The global results4 file. */
	String GLOBAL_RESULTS4_FILE = "global_result4.html";
	/** The global results5 file. */
	String GLOBAL_RESULTS5_FILE = "global_result5.html";
	/** The global results6 file. */
	String GLOBAL_RESULTS6_FILE = "global_result6.html";
	/** The segment results file 1. */
	String SEGMENT_RESULTS_FILE_1 = "segment{0}_1.html";
	/** The segment results file 2. */
	String SEGMENT_RESULTS_FILE_2 = "segment{0}_2.html";
	/** The segment results file 3. */
	String SEGMENT_RESULTS_FILE_3 = "segment{0}_3.html";
	/** The segment results file 4. */
	String SEGMENT_RESULTS_FILE_4 = "segment{0}_4.html";
	/** The segment results file 5. */
	String SEGMENT_RESULTS_FILE_5 = "segment{0}_5.html";
	/** The evolution results file. */
	String EVOLUTION_RESULTS_FILE = "evolution_result.html";
	/** The evolution results file 1. */
	String EVOLUTION_RESULTS_FILE_1 = "evolution_result1.html";
	/** The evolution results file 2. */
	String EVOLUTION_RESULTS_FILE_2 = "evolution_result2.html";
	/** The evolution results file 3. */
	String EVOLUTION_RESULTS_FILE_3 = "evolution_result3.html";
	/** The evolution results file 4. */
	String EVOLUTION_RESULTS_FILE_4 = "evolution_result4.html";
	/** The conclusion file. */
	String CONCLUSION_FILE = "conclusion.html";
	/** The segment conclusion file. */
	String SEGMENT_CONCLUSION_FILE = "segmentConclusion.html";
	/** The evolution conclusion file. */
	String EVOLUTION_CONCLUSION_FILE = "evolutionConclusion.html";
	/** The zip file. */
	String ZIP_FILE = "htmlZip.zip";
	/** The zip pdf file. */
	String ZIP_PDF_FILE = "pdfZip.zip";
	/** The index forward. */
	String INDEX_FORWARD = "indexHTML";
	/** The global results forward. */
	String GLOBAL_RESULTS_FORWARD = "globalResultHTML";
	/** The global results2 forward. */
	String GLOBAL_RESULTS2_FORWARD = "globalResultHTML2";
	/** The global results3 forward. */
	String GLOBAL_RESULTS3_FORWARD = "globalResultHTML3";
	/** The global results4 forward. */
	String GLOBAL_RESULTS4_FORWARD = "globalResultHTML4";
	/** The global results5 forward. */
	String GLOBAL_RESULTS5_FORWARD = "globalResultHTML5";
	/** The global results6 forward. */
	String GLOBAL_RESULTS6_FORWARD = "globalResultHTML6";
	/** The segment results forward 1. */
	String SEGMENT_RESULTS_FORWARD_1 = "segmentResultHTML1";
	/** The segment results forward 2. */
	String SEGMENT_RESULTS_FORWARD_2 = "segmentResultHTML2";
	/** The segment results forward 3. */
	String SEGMENT_RESULTS_FORWARD_3 = "segmentResultHTML3";
	/** The segment results forward 4. */
	String SEGMENT_RESULTS_FORWARD_4 = "segmentResultHTML4";
	/** The segment results forward 5. */
	String SEGMENT_RESULTS_FORWARD_5 = "segmentResultHTML5";
	/** The evolution results forward. */
	String EVOLUTION_RESULTS_FORWARD = "evolutionResultHTML";
	/** The evolution results forward1. */
	String EVOLUTION_RESULTS_FORWARD1 = "evolutionResultHTML1";
	/** The evolution results forward2. */
	String EVOLUTION_RESULTS_FORWARD2 = "evolutionResultHTML2";
	/** The evolution results forward3. */
	String EVOLUTION_RESULTS_FORWARD3 = "evolutionResultHTML3";
	/** The evolution results forward4. */
	String EVOLUTION_RESULTS_FORWARD4 = "evolutionResultHTML4";
	/** The observatory name. */
	String OBSERVATORY_NAME = "obsName";
	/** The observatory date. */
	String OBSERVATORY_DATE = "obsDate";
	/** The observatory evolution. */
	String OBSERVATORY_EVOLUTION = "obsEv";
	/** The observatory segments. */
	String OBSERVATORY_SEGMENTS = "obsSeg";
	/** The observatory t. */
	String OBSERVATORY_T = "obsType";
	/** The html menu. */
	String HTML_MENU = "menu";
	/** The html submenu. */
	String HTML_SUBMENU = "subMenu";
	/** The html menu introduction. */
	String HTML_MENU_INTRODUCTION = "1";
	/** The html menu objective. */
	String HTML_MENU_OBJECTIVE = "2";
	/** The html menu methodology. */
	String HTML_MENU_METHODOLOGY = "3";
	/** The html submenu methodology 1. */
	String HTML_SUBMENU_METHODOLOGY_1 = "3.1";
	/** The html submenu methodology 2. */
	String HTML_SUBMENU_METHODOLOGY_2 = "3.2";
	/** The html submenu methodology 3. */
	String HTML_SUBMENU_METHODOLOGY_3 = "3.3";
	/** The html submenu methodology 4. */
	String HTML_SUBMENU_METHODOLOGY_4 = "3.4";
	/** The html menu global results. */
	String HTML_MENU_GLOBAL_RESULTS = "4";
	/** The html menu global results 1. */
	String HTML_MENU_GLOBAL_RESULTS_1 = "4.1";
	/** The html menu global results 2. */
	String HTML_MENU_GLOBAL_RESULTS_2 = "4.2";
	/** The html menu global results 3. */
	String HTML_MENU_GLOBAL_RESULTS_3 = "4.3";
	/** The html menu global results 4. */
	String HTML_MENU_GLOBAL_RESULTS_4 = "4.4";
	/** The html menu global results 5. */
	String HTML_MENU_GLOBAL_RESULTS_5 = "4.5";
	/** The html menu global results 6. */
	String HTML_MENU_GLOBAL_RESULTS_6 = "4.6";
	/** The html menu segment results. */
	String HTML_MENU_SEGMENT_RESULTS = "5";
	/** The html menu segment results 1. */
	String HTML_MENU_SEGMENT_RESULTS_1 = "5.1";
	/** The html menu segment results 2. */
	String HTML_MENU_SEGMENT_RESULTS_2 = "5.2";
	/** The html menu segment results 3. */
	String HTML_MENU_SEGMENT_RESULTS_3 = "5.3";
	/** The html menu segment results 4. */
	String HTML_MENU_SEGMENT_RESULTS_4 = "5.4";
	/** The html menu segment results 5. */
	String HTML_MENU_SEGMENT_RESULTS_5 = "5.5";
	/** The html menu evolution. */
	String HTML_MENU_EVOLUTION = "6";
	/** The html menu evolution results 1. */
	String HTML_MENU_EVOLUTION_RESULTS_1 = "6.1";
	/** The html menu evolution results 2. */
	String HTML_MENU_EVOLUTION_RESULTS_2 = "6.2";
	/** The html menu evolution results 3. */
	String HTML_MENU_EVOLUTION_RESULTS_3 = "6.3";
	/** The html menu evolution results 4. */
	String HTML_MENU_EVOLUTION_RESULTS_4 = "6.4";
	/** The html menu conclusion. */
	String HTML_MENU_CONCLUSION = "7";
	/** The html submenu global conclusion. */
	String HTML_SUBMENU_GLOBAL_CONCLUSION = "7.1";
	/** The html submenu segment conclusion. */
	String HTML_SUBMENU_SEGMENT_CONCLUSION = "7.2";
	/** The html submenu evolution conclusion. */
	String HTML_SUBMENU_EVOLUTION_CONCLUSION = "7.3";
	/** The default encoding. */
	String DEFAULT_ENCODING = "UTF-8";
	/** The export. */
	String EXPORT = "export";
	/** The confirm. */
	String CONFIRM = "confirm";
	/** The object type paragraph. */
	String OBJECT_TYPE_PARAGRAPH = "paragraph";
	/** The object type section. */
	String OBJECT_TYPE_SECTION = "section";
	/** The wcag 2. */
	String WCAG_2 = "WCAG 2.0";
	/** The wcag 1. */
	String WCAG_1 = "WCAG 1.0";
	/** The observatory graphic evolution 1 1 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION = "1.1";
	/** The observatory graphic evolution 1 2 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION = "1.2";
	/** The observatory graphic evolution 1 3 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION = "1.3";
	/** The observatory graphic evolution 1 4 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION = "1.4";
	/** The observatory graphic evolution 1 5 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION = "1.5";
	/** The observatory graphic evolution 1 6 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION = "1.6";
	/** The observatory graphic evolution 1 7 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION = "1.7";
	/** The observatory graphic evolution 1 8 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION = "1.8";
	/** The observatory graphic evolution 1 9 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION = "1.9";
	/** The observatory graphic evolution 1 10 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION = "1.10";
	/** The observatory graphic evolution 1 11 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION = "1.11";
	/** The observatory graphic evolution 1 12 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION = "1.12";
	/** The observatory graphic evolution 1 13 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION = "1.13";
	/** The observatory graphic evolution 1 14 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION = "1.14";
	/** The observatory graphic evolution 2 1 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION = "2.1";
	/** The observatory graphic evolution 2 2 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION = "2.2";
	/** The observatory graphic evolution 2 3 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION = "2.3";
	/** The observatory graphic evolution 2 4 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION = "2.4";
	/** The observatory graphic evolution 2 5 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION = "2.5";
	/** The observatory graphic evolution 2 6 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION = "2.6";
	/** The observatory graphic evolution 3 1 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_3_1_VERIFICATION = "1";
	/** The observatory graphic evolution 3 2 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_3_2_VERIFICATION = "2";
	/** The observatory graphic evolution 3 3 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_3_3_VERIFICATION = "3";
	/** The observatory graphic evolution 3 4 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_3_4_VERIFICATION = "4";
	/** The observatory graphic evolution 3 5 verification. */
	String OBSERVATORY_GRAPHIC_EVOLUTION_3_5_VERIFICATION = "5";
	/** The normativa une 2012. */
	String NORMATIVA_UNE_2012 = "UNE-2012";
	/** The normativa une 2012 b. */
	String NORMATIVA_UNE_2012_B = "UNE-2012-B";
	/** The normativa une en2019. */
	String NORMATIVA_UNE_EN2019 = "UNE-EN301549:2019";
	/** The normativa accesibilidad. */
	String NORMATIVA_ACCESIBILIDAD = "Accesibilidad";
	/** The message resources 2012 b. */
	String MESSAGE_RESOURCES_2012_B = "ApplicationResources_xx_XX_2012B";
	/** The message resources une en2019. */
	String MESSAGE_RESOURCES_UNE_EN2019 = "ApplicationResources_xx_XX_2019";
	/** The message resources accesibilidad. */
	String MESSAGE_RESOURCES_ACCESIBILIDAD = "ApplicationResources_xx_XX_ACC";
	/** The check global modality grpahics. */
	String CHECK_GLOBAL_MODALITY_GRPAHICS = "checkGlobalModalityGrpahics";
	/** The check global aspects grpahics. */
	String CHECK_GLOBAL_ASPECTS_GRPAHICS = "checkGlobalAspectsGrpahics";
	/** The check segment modality grpahics. */
	String CHECK_SEGMENT_MODALITY_GRPAHICS = "checkSegmentModalityGrpahics";
	/** The check segment pmv grpahics. */
	String CHECK_SEGMENT_PMV_GRPAHICS = "checkSegmentPMVGrpahics";
	/** The check segment aspects grpahics. */
	String CHECK_SEGMENT_ASPECTS_GRPAHICS = "checkSegmentAspectsGrpahics";
	/** The check evo compliance verification grpahics. */
	String CHECK_EVO_COMPLIANCE_VERIFICATION_GRPAHICS = "checkEvoComplianceVerificationGrpahics";
	/** The check evo aspects grpahics. */
	String CHECK_EVO_ASPECTS_GRPAHICS = "checkEvoAspectsGrpahics";
	/** The id base template. */
	String ID_BASE_TEMPLATE = "idPlantillaBase";
	/** The id segment template. */
	String ID_SEGMENT_TEMPLATE = "idPlantillaSegmento";
	/** The id complexity template. */
	String ID_COMPLEXITY_TEMPLATE = "idPlantillaComplejidad";
	/** The id segment evol template. */
	String ID_SEGMENT_EVOL_TEMPLATE = "idPlantillaEvolSegmento";
	/** The id report title. */
	String ID_REPORT_TITLE = "reportTitle";
	/** The observatorio une 2012 version 2 sin enlaces rotos. */
	String OBSERVATORIO_UNE_2012_VERSION_2_SIN_ENLACES_ROTOS = "Observatorio UNE 2012 (versión 2)(sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS. */
	String OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS = "Observatorio UNE 2012 (antigua) (sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS. */
	String OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS = "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_ACCESIBILIDAD. */
	String OBSERVATORIO_ACCESIBILIDAD = "Comprobaciones Accesibilidad (beta)";
	/** The Constant OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS. */
	String OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS = "Comprobaciones Accesibilidad (beta)";
	/** The Constant OBSERVATORIO_UNE_EN2019. */
	String OBSERVATORIO_UNE_EN2019 = "Seguimiento Simplificado UNE-EN 301549:2022";
	/** The Constant OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS. */
	String OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS = "Seguimiento Simplificado UNE-EN301549:2019(sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2012_VERSION_2. */
	String OBSERVATORIO_UNE_2012_VERSION_2 = "Observatorio UNE 2012 (versión 2)";
	/** The Constant OBSERVATORIO_UNE_2012_ANTIGUA. */
	String OBSERVATORIO_UNE_2012_ANTIGUA = "Observatorio UNE 2012 (antigua)";
	/** The observatorio une 2004. */
	String OBSERVATORIO_UNE_2004 = "Observatorio UNE 2004";
	/** The bc rojo inteco. */
	// NEW BASE COLOR
	BaseColor BC_ROJO_INTECO = new BaseColor(225, 18, 13);
	/** The bc rosa inteco. */
	BaseColor BC_ROSA_INTECO = new BaseColor(255, 225, 225);
	/** The bc naranja mp. */
	BaseColor BC_NARANJA_MP = new BaseColor(245, 164, 55);
	/** The bc verde o mp. */
	BaseColor BC_VERDE_O_MP = new BaseColor(106, 130, 54);
	/** The bc verde c mp. */
	BaseColor BC_VERDE_C_MP = new BaseColor(126, 154, 64);
	/** The bc marrron c nc. */
	BaseColor BC_MARRRON_C_NC = new BaseColor(196, 89, 17);
	/** The bc gris muy claro. */
	BaseColor BC_GRIS_MUY_CLARO = new BaseColor(245, 245, 245);
	/** The observatory extra configuration list. */
	String OBSERVATORY_EXTRA_CONFIGURATION_LIST = "extraConfigurationList";
	/** Import entities */
	String IMPORTAR_ENTIDADES = "importarEntidades";
	/** Export entities */
	String EXPORTAR_ENTIDADES = "exportarEntidades";
}
