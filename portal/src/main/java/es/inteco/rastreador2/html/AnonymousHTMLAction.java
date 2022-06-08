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
package es.inteco.rastreador2.html;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.imp.xml.result.ImportSectionSetFromXml;
import es.inteco.rastreador2.imp.xml.result.SectionForm;
import es.inteco.rastreador2.imp.xml.result.SectionSetForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;

/**
 * The Class AnonymousHTMLAction.
 */
public class AnonymousHTMLAction extends Action {
	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		PropertiesManager pmgr = new PropertiesManager();
		String requestType = request.getParameter(Constants.REQUEST_TYPE);
		boolean originAnnexes = true;
		request.setAttribute(Constants.OBSERVATORY_DATE, request.getParameter(Constants.OBSERVATORY_DATE));
		request.setAttribute(Constants.OBSERVATORY_NAME, URLDecoder.decode(request.getParameter(Constants.OBSERVATORY_NAME), "ISO-8859-1"));
		request.setAttribute(Constants.OBSERVATORY_T, request.getParameter(Constants.OBSERVATORY_T));
		String noDataMess = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");
		String filePath = pmgr.getValue(CRAWLER_PROPERTIES, "html.path.graphics").replace("{0}", request.getParameter(Constants.ID));
		java.util.List<ObservatoryEvaluationForm> pageExecutionList;
		if (requestType.contains(Constants.SEGMENT_RESULTS_1) || requestType.contains(Constants.SEGMENT_RESULTS_3) || requestType.contains(Constants.SEGMENT_RESULTS_4)
				|| requestType.contains(Constants.SEGMENT_RESULTS_5)) {
			pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(request.getParameter(Constants.ID), Long.parseLong(request.getParameter(Constants.ID_CATEGORIA)), null,
					originAnnexes);
		} else {
			pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(request.getParameter(Constants.ID), Constants.COMPLEXITY_SEGMENT_NONE, null, originAnnexes);
		}
		Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = new HashMap<>();
		if (requestType.equals(Constants.EVOLUTION_RESULTS1) || requestType.equals(Constants.EVOLUTION_RESULTS2) || requestType.equals(Constants.EVOLUTION_RESULTS3)
				|| requestType.equals(Constants.EVOLUTION_RESULTS4)) {
			pageObservatoryMap = ResultadosAnonimosObservatorioIntavUtils.resultEvolutionData(Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO)),
					Long.valueOf(request.getParameter(Constants.ID)));
		}
		List<CategoriaForm> categories = new ArrayList<>();
		String categoryName = "";
		Connection conn = null;
		try {
			conn = DataBaseManager.getConnection();
			categories = ObservatorioDAO.getExecutionObservatoryCategories(conn, Long.valueOf(request.getParameter(Constants.ID)));
			if (request.getParameter(Constants.ID_CATEGORIA) != null) {
				categoryName = ObservatorioDAO.getCategoryById(conn, Long.valueOf(request.getParameter(Constants.ID_CATEGORIA))).getName();
				if (categoryName == null) {
					categoryName = "";
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al recuperar las categorias.", AnonymousHTMLAction.class, Logger.LOG_LEVEL_ERROR, e);
		} finally {
			DataBaseManager.closeConnection(conn);
		}
		request.setAttribute(Constants.CATEGORIES_LIST, categories);
		String conclusionPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.conclusion.xml") + request.getParameter(Constants.ID_OBSERVATORIO) + File.separator + request.getParameter(Constants.ID)
				+ File.separator + pmgr.getValue(CRAWLER_PROPERTIES, "name.conclusion.xml");
		String introductionPath;
		if (Integer.parseInt(request.getParameter(Constants.OBSERVATORY_T)) == Constants.OBSERVATORY_TYPE_EELL) {
			introductionPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.introduction.EELL.xml");
		} else if (Integer.parseInt(request.getParameter(Constants.OBSERVATORY_T)) == Constants.OBSERVATORY_TYPE_CCAA) {
			introductionPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.introduction.CCAA.xml");
		} else {
			introductionPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.introduction.AGE.xml");
		}
		SectionSetForm sectionSet = new SectionSetForm();
		if (requestType.equals(Constants.INTRODUCTION) || requestType.equals(Constants.OBJECTIVE) || requestType.equals(Constants.METHODOLOGY) || requestType.equals(Constants.METHODOLOGY_SUB1)
				|| requestType.equals(Constants.METHODOLOGY_SUB2) || requestType.equals(Constants.METHODOLOGY_SUB3) || requestType.equals(Constants.METHODOLOGY_SUB4)) {
			sectionSet = ImportSectionSetFromXml.createSectionSetForm(request, introductionPath, null, true);
		} else if (requestType.equals(Constants.CONCLUSION) || requestType.equals(Constants.SEGMENT_CONCLUSION) || requestType.equals(Constants.EVOLUTION_CONCLUSION)) {
			sectionSet = ImportSectionSetFromXml.createSectionSetForm(request, conclusionPath, pmgr.getValue(CRAWLER_PROPERTIES, "path.conclusion.xml.default"), true);
		}
		if (requestType.equals(Constants.INTRODUCTION)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 1);
				if (section != null) {
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_INTRODUCTION);
					request.setAttribute(Constants.HTML_SUBMENU, "");
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.OBJECTIVE)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 3);
				if (section != null) {
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_SUBMENU, "");
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_OBJECTIVE);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.METHODOLOGY)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 4);
				if (section != null) {
					section.setFinalSectionToPaint(4);
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_SUBMENU, "");
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_METHODOLOGY);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.METHODOLOGY_SUB1)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 5);
				if (section != null) {
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_METHODOLOGY);
					request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_SUBMENU_METHODOLOGY_1);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.METHODOLOGY_SUB2)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 6);
				if (section != null) {
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_METHODOLOGY);
					request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_SUBMENU_METHODOLOGY_2);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.METHODOLOGY_SUB3)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 7);
				if (section != null) {
					section.setFinalSectionToPaint(10);
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_METHODOLOGY);
					request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_SUBMENU_METHODOLOGY_3);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.METHODOLOGY_SUB4)) {
			if (sectionSet != null) {
				SectionForm section = returnSection(sectionSet, 11);
				if (section != null && section.getObjectList() != null) {
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_METHODOLOGY);
					request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_SUBMENU_METHODOLOGY_4);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.GLOBAL_RESULTS)) {
			return createGlobalResults(request, mapping, noDataMess, pageExecutionList, filePath);
		} else if (requestType.equals(Constants.GLOBAL_RESULTS2)) {
			return createGlobalResults2(request, mapping, noDataMess, pageExecutionList, filePath, categories);
		} else if (requestType.equals(Constants.GLOBAL_RESULTS3)) {
			return createGlobalResults3(request, mapping, noDataMess, pageExecutionList, filePath, categories);
		} else if (requestType.equals(Constants.GLOBAL_RESULTS4)) {
			return createGlobalResults4(request, mapping, noDataMess, pageExecutionList, filePath);
		} else if (requestType.equals(Constants.GLOBAL_RESULTS5)) {
			return createGlobalResults5(request, mapping, noDataMess, pageExecutionList, filePath);
		} else if (requestType.equals(Constants.GLOBAL_RESULTS6)) {
			return createGlobalResults6(request, mapping, noDataMess, pageExecutionList, filePath);
		} else if (requestType.contains(Constants.SEGMENT_RESULTS_1)) {
			return createSegmentResults1(request, mapping, pageExecutionList, categoryName);
		} else if (requestType.contains(Constants.SEGMENT_RESULTS_2)) {
			return createSegmentResults2(request, mapping, categoryName);
		} else if (requestType.contains(Constants.SEGMENT_RESULTS_3)) {
			return createSegmentResults3(request, mapping, pageExecutionList, categoryName);
		} else if (requestType.contains(Constants.SEGMENT_RESULTS_4)) {
			return createSegmentResults4(request, mapping, pageExecutionList, categoryName);
		} else if (requestType.contains(Constants.SEGMENT_RESULTS_5)) {
			return createSegmentResults5(request, mapping, pageExecutionList, categoryName);
		} else if (requestType.equals(Constants.CONCLUSION)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 1);
				if (section != null && section.getObjectList() != null) {
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_CONCLUSION);
					request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_SUBMENU_GLOBAL_CONCLUSION);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.SEGMENT_CONCLUSION)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 2);
				if (section != null) {
					section.setFinalSectionToPaint(3 + categories.size());
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_CONCLUSION);
					request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_SUBMENU_SEGMENT_CONCLUSION);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.EVOLUTION_CONCLUSION)) {
			if (sectionSet != null && sectionSet.getSectionList() != null) {
				SectionForm section = returnSection(sectionSet, 3 + categories.size());
				if (section != null) {
					request.setAttribute(Constants.SECTION_FORM, section);
					request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_CONCLUSION);
					request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_SUBMENU_EVOLUTION_CONCLUSION);
					return mapping.findForward(Constants.INDEX_FORWARD);
				}
			}
		} else if (requestType.equals(Constants.EVOLUTION_RESULTS)) {
			request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_EVOLUTION);
			return mapping.findForward(Constants.EVOLUTION_RESULTS_FORWARD);
		} else if (requestType.equals(Constants.EVOLUTION_RESULTS1)) {
			return createEvolutionResults1(request, mapping, pageObservatoryMap);
		} else if (requestType.equals(Constants.EVOLUTION_RESULTS2)) {
			return createEvolutionResults2(request, mapping, pageObservatoryMap);
		} else if (requestType.equals(Constants.EVOLUTION_RESULTS3)) {
			return createEvolutionResults3(request, mapping, pageObservatoryMap);
		} else if (requestType.equals(Constants.EVOLUTION_RESULTS4)) {
			return createEvolutionResults4(request, mapping, pageObservatoryMap);
		}
		return null;
	}

	/**
	 * Return section.
	 *
	 * @param section    the section
	 * @param sectionNum the section num
	 * @return the section form
	 */
	private SectionForm returnSection(SectionForm section, int sectionNum) {
		for (Object object : section.getObjectList()) {
			if (object instanceof es.inteco.rastreador2.imp.xml.result.SectionForm) {
				if (((SectionForm) object).getSectionNumber() == sectionNum) {
					return (SectionForm) object;
				} else {
					SectionForm sec = returnSection(((SectionForm) object), sectionNum);
					if (sec != null) {
						return sec;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Return section.
	 *
	 * @param introduction the introduction
	 * @param sectionNum   the section num
	 * @return the section form
	 */
	private SectionForm returnSection(SectionSetForm introduction, int sectionNum) {
		for (SectionForm section : introduction.getSectionList()) {
			if (section.getSectionNumber() == sectionNum) {
				return section;
			}
			SectionForm sec = returnSection(section, sectionNum);
			if (sec != null) {
				return sec;
			}
		}
		return null;
	}

	/**
	 * Creates the global results.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param filePath          the file path
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createGlobalResults(HttpServletRequest request, ActionMapping mapping, String noDataMess, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String filePath)
			throws Exception {
		String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.accessibility.level.allocation.title");
		String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.accessibility.level.allocation.name") + ".jpg";
		ResultadosAnonimosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(request, pageExecutionList, title, file, noDataMess, true);
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_GLOBAL_RESULTS_1);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_GLOBAL_RESULTS);
		return mapping.findForward(Constants.GLOBAL_RESULTS_FORWARD);
	}

	/**
	 * Creates the global results 2.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param filePath          the file path
	 * @param categories        the categories
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createGlobalResults2(HttpServletRequest request, ActionMapping mapping, String noDataMess, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String filePath,
			List<CategoriaForm> categories) throws Exception {
		String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.global.puntuation.allocation.segments.mark.name") + ".jpg";
		ResultadosAnonimosObservatorioIntavUtils.getGlobalMarkBySegmentsGroupGraphic(request, file, noDataMess, pageExecutionList, categories, true);
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_GLOBAL_RESULTS_2);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_GLOBAL_RESULTS);
		return mapping.findForward(Constants.GLOBAL_RESULTS2_FORWARD);
	}

	/**
	 * Creates the global results 3.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param filePath          the file path
	 * @param categories        the categories
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createGlobalResults3(HttpServletRequest request, ActionMapping mapping, String noDataMess, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String filePath,
			List<CategoriaForm> categories) throws Exception {
		String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.global.puntuation.allocation.segment.strached.title");
		String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.global.puntuation.allocation.segment.strached.name") + ".jpg";
		ResultadosAnonimosObservatorioIntavUtils.getGlobalMarkBySegmentGraphic(request, pageExecutionList, title, file, noDataMess, categories);
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_GLOBAL_RESULTS_3);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_GLOBAL_RESULTS);
		return mapping.findForward(Constants.GLOBAL_RESULTS3_FORWARD);
	}

	/**
	 * Creates the global results 4.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param filePath          the file path
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createGlobalResults4(HttpServletRequest request, ActionMapping mapping, String noDataMess, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String filePath)
			throws Exception {
		PropertiesManager pmgr = new PropertiesManager();
		String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.title");
		String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg";
		ResultadosAnonimosObservatorioIntavUtils.getMidsComparationByVerificationLevelGraphic(request, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList,
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), true);
		title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.title");
		file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg";
		ResultadosAnonimosObservatorioIntavUtils.getMidsComparationByVerificationLevelGraphic(request, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList,
				pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), true);
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_GLOBAL_RESULTS_4);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_GLOBAL_RESULTS);
		return mapping.findForward(Constants.GLOBAL_RESULTS4_FORWARD);
	}

	/**
	 * Creates the global results 5.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param filePath          the file path
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createGlobalResults5(HttpServletRequest request, ActionMapping mapping, String noDataMess, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String filePath)
			throws Exception {
		String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.1.title");
		String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.1.name") + ".jpg";
		ResultadosAnonimosObservatorioIntavUtils.getModalityByVerificationLevelGraphic(request, pageExecutionList, title, file, noDataMess, Constants.OBS_PRIORITY_1, true);
		title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.2.title");
		file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.2.name") + ".jpg";
		ResultadosAnonimosObservatorioIntavUtils.getModalityByVerificationLevelGraphic(request, pageExecutionList, title, file, noDataMess, Constants.OBS_PRIORITY_2, true);
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_GLOBAL_RESULTS_5);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_GLOBAL_RESULTS);
		return mapping.findForward(Constants.GLOBAL_RESULTS5_FORWARD);
	}

	/**
	 * Creates the global results 6.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param filePath          the file path
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createGlobalResults6(HttpServletRequest request, ActionMapping mapping, String noDataMess, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String filePath)
			throws Exception {
		PropertiesManager pmgr = new PropertiesManager();
		String file = filePath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.aspect.mid.name") + ".jpg";
		String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.aspect.mid.title");
		ResultadosAnonimosObservatorioIntavUtils.getAspectMidsGraphic(request, file, noDataMess, pageExecutionList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), title, true);
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_GLOBAL_RESULTS_6);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_GLOBAL_RESULTS);
		return mapping.findForward(Constants.GLOBAL_RESULTS6_FORWARD);
	}

	/**
	 * Creates the segment results 1.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param pageExecutionList the page execution list
	 * @param categoryName      the category name
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createSegmentResults1(HttpServletRequest request, ActionMapping mapping, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String categoryName) throws Exception {
		Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG, ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(CrawlerUtils.getResources(request), resultsMap));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_SEGMENT_RESULTS_1);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_SEGMENT_RESULTS);
		request.setAttribute(Constants.CATEGORY_NAME, categoryName);
		return mapping.findForward(Constants.SEGMENT_RESULTS_FORWARD_1);
	}

	/**
	 * Creates the segment results 2.
	 *
	 * @param request      the request
	 * @param mapping      the mapping
	 * @param categoryName the category name
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createSegmentResults2(HttpServletRequest request, ActionMapping mapping, String categoryName) throws Exception {
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_SEGMENT_RESULTS_2);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_SEGMENT_RESULTS);
		request.setAttribute(Constants.CATEGORY_NAME, categoryName);
		return mapping.findForward(Constants.SEGMENT_RESULTS_FORWARD_2);
	}

	/**
	 * Creates the segment results 3.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param pageExecutionList the page execution list
	 * @param categoryName      the category name
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createSegmentResults3(HttpServletRequest request, ActionMapping mapping, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String categoryName) throws Exception {
		Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
		Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI,
				ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(CrawlerUtils.getResources(request), resultL1));
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII,
				ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(CrawlerUtils.getResources(request), resultL2));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_SEGMENT_RESULTS_3);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_SEGMENT_RESULTS);
		request.setAttribute(Constants.CATEGORY_NAME, categoryName);
		return mapping.findForward(Constants.SEGMENT_RESULTS_FORWARD_3);
	}

	/**
	 * Creates the segment results 4.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param pageExecutionList the page execution list
	 * @param categoryName      the category name
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createSegmentResults4(HttpServletRequest request, ActionMapping mapping, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String categoryName) throws Exception {
		Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
		Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results1));
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results2));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_SEGMENT_RESULTS_4);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_SEGMENT_RESULTS);
		request.setAttribute(Constants.ID_CATEGORIA, request.getParameter(Constants.ID_CATEGORIA));
		request.setAttribute(Constants.CATEGORY_NAME, categoryName);
		return mapping.findForward(Constants.SEGMENT_RESULTS_FORWARD_4);
	}

	/**
	 * Creates the segment results 5.
	 *
	 * @param request           the request
	 * @param mapping           the mapping
	 * @param pageExecutionList the page execution list
	 * @param categoryName      the category name
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createSegmentResults5(HttpServletRequest request, ActionMapping mapping, java.util.List<ObservatoryEvaluationForm> pageExecutionList, String categoryName) throws Exception {
		Map<String, BigDecimal> result = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(CrawlerUtils.getResources(request), pageExecutionList);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA, ResultadosAnonimosObservatorioIntavUtils.infoAspectMidsComparison(request, result));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_SEGMENT_RESULTS_5);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_SEGMENT_RESULTS);
		request.setAttribute(Constants.ID_CATEGORIA, request.getParameter(Constants.ID_CATEGORIA));
		request.setAttribute(Constants.CATEGORY_NAME, categoryName);
		return mapping.findForward(Constants.SEGMENT_RESULTS_FORWARD_5);
	}

	/**
	 * Creates the evolution results 1.
	 *
	 * @param request            the request
	 * @param mapping            the mapping
	 * @param pageObservatoryMap the page observatory map
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createEvolutionResults1(HttpServletRequest request, ActionMapping mapping, Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws Exception {
		Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioIntavUtils.getEvolutionObservatoriesSitesByType(request.getParameter(Constants.ID_OBSERVATORIO),
				request.getParameter(Constants.ID), pageObservatoryMap);
		Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A);
		Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA);
		Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_A, ResultadosAnonimosObservatorioIntavUtils.infoLevelEvolutionGraphic(resultDataA));
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AA, ResultadosAnonimosObservatorioIntavUtils.infoLevelEvolutionGraphic(resultDataAA));
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_NV, ResultadosAnonimosObservatorioIntavUtils.infoLevelEvolutionGraphic(resultDataNV));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_EVOLUTION_RESULTS_1);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_EVOLUTION);
		return mapping.findForward(Constants.EVOLUTION_RESULTS_FORWARD1);
	}

	/**
	 * Creates the evolution results 2.
	 *
	 * @param request            the request
	 * @param mapping            the mapping
	 * @param pageObservatoryMap the page observatory map
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createEvolutionResults2(HttpServletRequest request, ActionMapping mapping, Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws Exception {
		Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateEvolutionPuntuationDataSet(pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_MID_PUNT, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkEvolutionGraphic(resultData));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_EVOLUTION_RESULTS_2);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_EVOLUTION);
		return mapping.findForward(Constants.EVOLUTION_RESULTS_FORWARD2);
	}

	/**
	 * Creates the evolution results 3.
	 *
	 * @param request            the request
	 * @param mapping            the mapping
	 * @param pageObservatoryMap the page observatory map
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createEvolutionResults3(HttpServletRequest request, ActionMapping mapping, Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws Exception {
		Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION,
				pageObservatoryMap);
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V223, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V224, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V225, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateVerificationEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION, pageObservatoryMap);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V226, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_EVOLUTION_RESULTS_3);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_EVOLUTION);
		return mapping.findForward(Constants.EVOLUTION_RESULTS_FORWARD3);
	}

	/**
	 * Creates the evolution results 4.
	 *
	 * @param request            the request
	 * @param mapping            the mapping
	 * @param pageObservatoryMap the page observatory map
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward createEvolutionResults4(HttpServletRequest request, ActionMapping mapping, Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws Exception {
		Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<>();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(CrawlerUtils.getResources(request), entry.getValue()));
		}
		Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL, resultsByAspect);
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AG, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE, resultsByAspect);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AAL, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION, resultsByAspect);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AP, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE, resultsByAspect);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AE, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		resultData = ResultadosAnonimosObservatorioIntavUtils.calculateAspectEvolutionPuntuationDataSet(Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION, resultsByAspect);
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AN, ResultadosAnonimosObservatorioIntavUtils.infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		request.setAttribute(Constants.HTML_SUBMENU, Constants.HTML_MENU_EVOLUTION_RESULTS_4);
		request.setAttribute(Constants.HTML_MENU, Constants.HTML_MENU_EVOLUTION);
		return mapping.findForward(Constants.EVOLUTION_RESULTS_FORWARD4);
	}
}
