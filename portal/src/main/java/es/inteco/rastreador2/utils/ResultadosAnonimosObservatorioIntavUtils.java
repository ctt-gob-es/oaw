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
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.opensymphony.oscache.base.NeedsRefreshException;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySiteEvaluationForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.intav.form.SeedForm;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.view.forms.CategoryViewListForm;

/**
 * The Class ResultadosAnonimosObservatorioIntavUtils.
 */
public final class ResultadosAnonimosObservatorioIntavUtils {
	/**
	 * Instantiates a new resultados anonimos observatorio intav utils.
	 */
	private ResultadosAnonimosObservatorioIntavUtils() {
	}

	/** The x. */
	// GENERATE GRAPHIC METHODS
	static int x = 0;
	/** The y. */
	static int y = 0;
	static {
		PropertiesManager pmgr = new PropertiesManager();
		x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
		y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
	}

	/**
	 * Generate graphics.
	 *
	 * @param request    the request
	 * @param filePath   the file path
	 * @param type       the type
	 * @param regenerate the regenerate
	 * @throws Exception the exception
	 */
	public static void generateGraphics(HttpServletRequest request, String filePath, String type, boolean regenerate) throws Exception {
		Connection c = null;
		try {
			PropertiesManager pmgr = new PropertiesManager();
			String color = pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors");
			if (type != null && type.equals(Constants.MINISTERIO_P)) {
				color = pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color");
			}
			c = DataBaseManager.getConnection();
			// recuperamos las categorias del observatorio
			List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, Long.valueOf(request.getParameter(Constants.ID)));
			generateGlobalGraphics(request, filePath, categories, color, regenerate);
			// iteramos sobre ellas y genermos las gráficas
			for (CategoriaForm categoryForm : categories) {
				generateCategoryGraphics(request, categoryForm, filePath, color, regenerate);
			}
			generateEvolutionGraphics(request, filePath, color, regenerate);
		} catch (Exception e) {
			Logger.putLog("No se han generado las gráficas correctamente.", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DataBaseManager.closeConnection(c);
		}
	}

	/**
	 * Generate global graphics.
	 *
	 * @param request    the request
	 * @param filePath   the file path
	 * @param categories the categories
	 * @param color      the color
	 * @param regenerate the regenerate
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int generateGlobalGraphics(HttpServletRequest request, String filePath, List<CategoriaForm> categories, String color, boolean regenerate) throws Exception {
		String executionId = request.getParameter(Constants.ID);
		List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(executionId, Constants.COMPLEXITY_SEGMENT_NONE, null);
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final MessageResources messageResources = CrawlerUtils.getResources(request);
			String noDataMess = messageResources.getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");
			String title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.accessibility.level.allocation.title");
			String file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.accessibility.level.allocation.name") + ".jpg";
			getGlobalAccessibilityLevelAllocationSegmentGraphic(request, pageExecutionList, title, file, noDataMess, regenerate);
			title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.global.puntuation.allocation.segment.strached.title");
			file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.global.puntuation.allocation.segment.strached.name") + ".jpg";
			getGlobalMarkBySegmentGraphic(request, pageExecutionList, title, file, noDataMess, categories);
			title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.1.title");
			file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.1.name") + ".jpg";
			getModalityByVerificationLevelGraphic(request, pageExecutionList, title, file, noDataMess, Constants.OBS_PRIORITY_1, regenerate);
			title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.2.title");
			file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.2.name") + ".jpg";
			getModalityByVerificationLevelGraphic(request, pageExecutionList, title, file, noDataMess, Constants.OBS_PRIORITY_2, regenerate);
			file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.global.puntuation.allocation.segments.mark.name") + ".jpg";
			getGlobalMarkBySegmentsGroupGraphic(request, file, noDataMess, pageExecutionList, categories, regenerate);
			file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.aspect.mid.name") + ".jpg";
			title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.aspect.mid.title");
			getAspectMidsGraphic(request, file, noDataMess, pageExecutionList, color, title, regenerate);
			title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.title");
			file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg";
			getMidsComparationByVerificationLevelGraphic(request, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
			title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.title");
			file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg";
			getMidsComparationByVerificationLevelGraphic(request, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
			return Constants.OBSERVATORY_HAVE_RESULTS;
		} else {
			return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
		}
	}

	/**
	 * Generate category graphics.
	 *
	 * @param request    the request
	 * @param category   the category
	 * @param filePath   the file path
	 * @param color      the color
	 * @param regenerate the regenerate
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int generateCategoryGraphics(HttpServletRequest request, CategoriaForm category, String filePath, String color, boolean regenerate) throws Exception {
		Connection conn = null;
		try {
			conn = DataBaseManager.getConnection();
			final String idExecution = request.getParameter(Constants.ID);
			final MessageResources messageResources = CrawlerUtils.getResources(request);
			final String noDataMess = messageResources.getMessage(CrawlerUtils.getLocale(request), "grafica.sin.datos");
			final List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(idExecution, Long.parseLong(category.getId()), null, false);
			if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
				String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.title", category.getName());
				String file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.accessibility.level.allocation.segment.name", category.getOrden()) + ".jpg";
				getGlobalAccessibilityLevelAllocationSegmentGraphic(request, pageExecutionList, title, file, noDataMess, regenerate);
				title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mark.allocation.segment.title", category.getName());
				file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mark.allocation.segment.name", category.getOrden()) + ".jpg";
				List<ObservatorySiteEvaluationForm> result = getSitesListByLevel(pageExecutionList);
				getMarkAllocationLevelSegmentGraphic(request, title, file, noDataMess, result, false, regenerate);
				file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.aspect.mid.name") + category.getOrden() + ".jpg";
				title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.segment.aspect.mid.title", category.getName());
				getAspectMidsGraphic(request, file, noDataMess, pageExecutionList, color, title, regenerate);
				title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.cat.title", category.getName());
				file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.1.name") + category.getOrden() + ".jpg";
				getMidsComparationByVerificationLevelGraphic(request, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.cat.title", category.getName());
				file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.mid.comparation.level.2.name") + category.getOrden() + ".jpg";
				getMidsComparationByVerificationLevelGraphic(request, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.1.category.title", category.getName());
				file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.1.name") + category.getOrden() + ".jpg";
				getModalityByVerificationLevelGraphic(request, pageExecutionList, title, file, noDataMess, Constants.OBS_PRIORITY_1, regenerate);
				title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.2.category.title", category.getName());
				file = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.modality.by.verification.level.2.name") + category.getOrden() + ".jpg";
				getModalityByVerificationLevelGraphic(request, pageExecutionList, title, file, noDataMess, Constants.OBS_PRIORITY_2, regenerate);
				return Constants.OBSERVATORY_HAVE_RESULTS;
			} else {
				return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
			return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
		} finally {
			DataBaseManager.closeConnection(conn);
		}
	}

	/**
	 * Generate evolution graphics.
	 *
	 * @param request    the request
	 * @param filePath   the file path
	 * @param color      the color
	 * @param regenerate the regenerate
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int generateEvolutionGraphics(HttpServletRequest request, String filePath, String color, boolean regenerate) throws Exception {
		final String observatoryId = request.getParameter(Constants.ID_OBSERVATORIO);
		final String executionId = request.getParameter(Constants.ID);
		final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = resultEvolutionData(Long.valueOf(observatoryId), Long.valueOf(executionId));
		if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
			if (pageObservatoryMap.size() != 1) {
				final MessageResources messageResources = CrawlerUtils.getResources(request);
				String noDataMess = messageResources.getMessage("grafica.sin.datos");
				String title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.A.title");
				String file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(request, Constants.OBS_A, title, file, noDataMess, pageObservatoryMap, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.AA.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(request, Constants.OBS_AA, title, file, noDataMess, pageObservatoryMap, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.NV.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(request, Constants.OBS_NV, title, file, noDataMess, pageObservatoryMap, color, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg";
				getMidMarkEvolutionGraphic(request, noDataMess, file, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color, regenerate);
				Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<>();
				for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
					resultsByAspect.put(entry.getKey(), aspectMidsPuntuationGraphicData(CrawlerUtils.getResources(request), entry.getValue()));
				}
				getMidMarkAspectEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(request, Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				return Constants.OBSERVATORY_HAVE_RESULTS;
			} else {
				return Constants.OBSERVATORY_HAVE_ONE_RESULT;
			}
		} else {
			return Constants.OBSERVATORY_NOT_HAVE_RESULTS;
		}
	}

	/**
	 * Info global accessibility level.
	 *
	 * @param resources the resources
	 * @param result    the result
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<GraphicData> infoGlobalAccessibilityLevel(final MessageResources resources, final Map<String, Integer> result) throws Exception {
		final List<GraphicData> labelValueList = new ArrayList<>();
		final int totalPort = result.get(Constants.OBS_A) + result.get(Constants.OBS_AA) + result.get(Constants.OBS_NV);
		GraphicData labelValue;
		labelValue = new GraphicData();
		labelValue.setAdecuationLevel(resources.getMessage("resultados.anonimos.porc.portales.aa"));
		if (totalPort > 0) {
			labelValue.setRawPercentage(new BigDecimal(result.get(Constants.OBS_AA)).multiply(new BigDecimal(100)).divide(new BigDecimal(totalPort), 2, BigDecimal.ROUND_HALF_UP));
			labelValue.setPercentageP(String.valueOf(labelValue.getRawPercentage()).replace(".00", "") + "%");
		}
		labelValue.setNumberP(String.valueOf((new BigDecimal(result.get(Constants.OBS_AA)))));
		labelValueList.add(labelValue);
		labelValue = new GraphicData();
		labelValue.setAdecuationLevel(resources.getMessage("resultados.anonimos.porc.portales.a"));
		if (totalPort > 0) {
			labelValue.setRawPercentage(new BigDecimal(result.get(Constants.OBS_A)).multiply(new BigDecimal(100)).divide(new BigDecimal(totalPort), 2, BigDecimal.ROUND_HALF_UP));
			labelValue.setPercentageP(String.valueOf(labelValue.getRawPercentage()).replace(".00", "") + "%");
		}
		labelValue.setNumberP(String.valueOf((new BigDecimal(result.get(Constants.OBS_A)))));
		labelValueList.add(labelValue);
		labelValue = new GraphicData();
		labelValue.setAdecuationLevel(resources.getMessage("resultados.anonimos.porc.portales.nv"));
		if (totalPort > 0) {
			labelValue.setRawPercentage(new BigDecimal(result.get(Constants.OBS_NV)).multiply(new BigDecimal(100)).divide(new BigDecimal(totalPort), 2, BigDecimal.ROUND_HALF_UP));
			labelValue.setPercentageP(String.valueOf(labelValue.getRawPercentage()).replace(".00", "") + "%");
		}
		labelValue.setNumberP(String.valueOf((new BigDecimal(result.get(Constants.OBS_NV)))));
		labelValueList.add(labelValue);
		return labelValueList;
	}

	/**
	 * Info comparison by segment.
	 *
	 * @param resources the resources
	 * @param category  the category
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<LabelValueBean> infoComparisonBySegment(final MessageResources resources, Map<String, BigDecimal> category) throws Exception {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.punt.portales.aa"));
		if (category.get(Constants.OBS_AA) != null && category.get(Constants.OBS_AA).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(category.get(Constants.OBS_AA)).replace(".00", ""));
		} else if (category.get(Constants.OBS_AA) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.punt.portales.a"));
		if (category.get(Constants.OBS_A) != null && category.get(Constants.OBS_A).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(category.get(Constants.OBS_A)).replace(".00", ""));
		} else if (category.get(Constants.OBS_A) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.punt.portales.nv"));
		if (category.get(Constants.OBS_NV) != null && category.get(Constants.OBS_NV).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(category.get(Constants.OBS_NV)).replace(".00", ""));
		} else if (category.get(Constants.OBS_NV) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		return labelValueList;
	}

	/**
	 * Info comparison by segment puntuation.
	 *
	 * @param resources the resources
	 * @param result    the result
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<LabelValueBean> infoComparisonBySegmentPuntuation(final MessageResources resources, final Map<String, BigDecimal> result) throws Exception {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.punt.portales.aa"));
		labelValue.setValue(String.valueOf(result.get(Constants.OBS_AA)).replace(".00", ""));
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.punt.portales.a"));
		labelValue.setValue(String.valueOf(result.get(Constants.OBS_A)).replace(".00", ""));
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.punt.portales.nv"));
		labelValue.setValue(String.valueOf(result.get(Constants.OBS_NV)).replace(".00", ""));
		labelValueList.add(labelValue);
		return labelValueList;
	}

	/**
	 * Info aspect mids comparison.
	 *
	 * @param request the request
	 * @param result  the result
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<LabelValueBean> infoAspectMidsComparison(HttpServletRequest request, Map<String, BigDecimal> result) throws Exception {
		return infoAspectMidsComparison(CrawlerUtils.getResources(request), result);
	}

	/**
	 * Info aspect mids comparison.
	 *
	 * @param resources the resources
	 * @param result    the result
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<LabelValueBean> infoAspectMidsComparison(final MessageResources resources, Map<String, BigDecimal> result) throws Exception {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.general"));
		if (result.get(resources.getMessage("observatory.aspect.general")) != null && result.get(resources.getMessage("observatory.aspect.general")).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(resources.getMessage("observatory.aspect.general"))).replace(".00", ""));
		} else if (result.get(resources.getMessage("observatory.aspect.general")) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.presentacion"));
		if (result.get(resources.getMessage("observatory.aspect.presentation")) != null && result.get(resources.getMessage("observatory.aspect.presentation")).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(resources.getMessage("observatory.aspect.presentation"))).replace(".00", ""));
		} else if (result.get(resources.getMessage("observatory.aspect.presentation")) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.estructura"));
		if (result.get(resources.getMessage("observatory.aspect.structure")) != null && result.get(resources.getMessage("observatory.aspect.structure")).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(resources.getMessage("observatory.aspect.structure"))).replace(".00", ""));
		} else if (result.get(resources.getMessage("observatory.aspect.structure")) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.navegacion"));
		if (result.get(resources.getMessage("observatory.aspect.navigation")) != null && result.get(resources.getMessage("observatory.aspect.navigation")).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(resources.getMessage("observatory.aspect.navigation"))).replace(".00", ""));
		} else if (result.get(resources.getMessage("observatory.aspect.navigation")) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("resultados.anonimos.alternativa"));
		if (result.get(resources.getMessage("observatory.aspect.alternatives")) != null && result.get(resources.getMessage("observatory.aspect.alternatives")).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(resources.getMessage("observatory.aspect.alternatives"))).replace(".00", ""));
		} else if (result.get(resources.getMessage("observatory.aspect.alternatives")) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		return labelValueList;
	}

	/**
	 * Info mark allocation level segment.
	 *
	 * @param siteList the site list
	 * @return the list
	 */
	public static List<LabelValueBean> infoMarkAllocationLevelSegment(List<ObservatorySiteEvaluationForm> siteList) {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue;
		for (ObservatorySiteEvaluationForm siteForm : siteList) {
			labelValue = new LabelValueBean();
			if (siteForm.getAcronym() != null && !StringUtils.isEmpty(siteForm.getAcronym())) {
				labelValue.setLabel(siteForm.getAcronym() + " (" + siteForm.getName() + ")");
			} else {
				labelValue.setLabel(siteForm.getName());
			}
			labelValue.setValue(String.valueOf(siteForm.getScore()));
			labelValueList.add(labelValue);
		}
		return labelValueList;
	}

	/**
	 * Info level I verification mids comparison.
	 *
	 * @param resources the resources
	 * @param result    the result
	 * @return the list
	 */
	public static List<LabelValueBean> infoLevelIVerificationMidsComparison(final MessageResources resources, final Map<String, BigDecimal> result) {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.1.1"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.1.2"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.1.3"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.1.4"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.2.1"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.2.2"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.2.3"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.2.4"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.2.5"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.1.2.6"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		return labelValueList;
	}

	/**
	 * Info level II verification mids comparison.
	 *
	 * @param resources the resources
	 * @param result    the result
	 * @return the list
	 */
	public static List<LabelValueBean> infoLevelIIVerificationMidsComparison(final MessageResources resources, final Map<String, BigDecimal> result) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.1.1"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.1.2"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.1.3"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.1.4"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.2.1"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.2.2"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.2.3"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.2.4"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.2.5"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		labelValue = new LabelValueBean();
		labelValue.setLabel(resources.getMessage("inteco.observatory.subgroup.2.2.6"));
		if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION) != null && result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION).compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION)).replace(".00", ""));
		} else if (result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION) == null) {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		labelValueList.add(labelValue);
		return labelValueList;
	}

	/**
	 * Info level evolution graphic.
	 *
	 * @param resultData the result data
	 * @return the list
	 */
	public static List<LabelValueBean> infoLevelEvolutionGraphic(final Map<String, BigDecimal> resultData) {
		return infoEvolutionGraphic(resultData, "%");
	}

	/**
	 * Info mid mark evolution graphic.
	 *
	 * @param resultData the result data
	 * @return the list
	 */
	public static List<LabelValueBean> infoMidMarkEvolutionGraphic(final Map<String, BigDecimal> resultData) {
		return infoEvolutionGraphic(resultData, "");
	}

	/**
	 * Info evolution graphic.
	 *
	 * @param resultData the result data
	 * @param suffix     the suffix
	 * @return the list
	 */
	private static List<LabelValueBean> infoEvolutionGraphic(final Map<String, BigDecimal> resultData, final String suffix) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
			final LabelValueBean labelValue = new LabelValueBean();
			labelValue.setLabel(entry.getKey());
			labelValue.setValue(String.valueOf(entry.getValue()).replace(".00", "") + suffix);
			labelValueList.add(labelValue);
		}
		return labelValueList;
	}

	// RESULTS METHODS
	/**
	 * Gets the mids comparation by verification level graphic.
	 *
	 * @param request           the request
	 * @param level             the level
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the mids comparation by verification level graphic
	 * @throws Exception the exception
	 */
	public static void getMidsComparationByVerificationLevelGraphic(HttpServletRequest request, String level, String title, String filePath, String noDataMess,
			List<ObservatoryEvaluationForm> pageExecutionList, String color, boolean regenerate) throws Exception {
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		final File file = new File(filePath);
		final Map<String, BigDecimal> result = getVerificationResultsByPoint(pageExecutionList, level);
		// Incluimos los resultados en la request
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI, infoLevelIVerificationMidsComparison(messageResources, result));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII, infoLevelIIVerificationMidsComparison(messageResources, result));
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			final String rowTitle = messageResources.getMessage("observatory.graphic.mid.puntuation");
			final String columnTitle = messageResources.getMessage("observatory.graphic.verification.points");
			GraphicsUtils.createBarChart(result, title, rowTitle, columnTitle, color, false, false, false, filePath, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the modality by verification level graphic.
	 *
	 * @param request           the request
	 * @param pageExecutionList the page execution list
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param level             the level
	 * @param regenerate        the regenerate
	 * @return the modality by verification level graphic
	 * @throws Exception the exception
	 */
	public static void getModalityByVerificationLevelGraphic(HttpServletRequest request, List<ObservatoryEvaluationForm> pageExecutionList, String title, String filePath, String noDataMess,
			String level, boolean regenerate) throws Exception {
		File file = new File(filePath);
		Map<String, BigDecimal> results = getVerificationResultsByPointAndModality(pageExecutionList, level);
		DefaultCategoryDataset dataSet = createStackedBarDataSetForModality(results, CrawlerUtils.getResources(request));
		// Incluimos los resultados en la request
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I, infoLevelVerificationModalityComparison(results));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II, infoLevelVerificationModalityComparison(results));
		}
		if (!file.exists() || regenerate) {
			PropertiesManager pmgr = new PropertiesManager();
			String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.verification.points");
			String colTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.percentage");
			ChartForm chartForm = new ChartForm(title, colTitle, rowTitle, dataSet, true, false, false, true, true, false, false, x, y,
					pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.modality.colors"));
			GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
		}
	}

	/**
	 * Info level verification modality comparison.
	 *
	 * @param results the results
	 * @return the list
	 */
	public static List<ModalityComparisonForm> infoLevelVerificationModalityComparison(final Map<String, BigDecimal> results) {
		final List<ModalityComparisonForm> modalityComparisonList = new ArrayList<>();
		for (String key : results.keySet()) {
			if (!modalityComparisonList.contains(new ModalityComparisonForm(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")))
					&& !modalityComparisonList.contains(new ModalityComparisonForm(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")))) {
				final ModalityComparisonForm modalityComparisonForm = new ModalityComparisonForm();
				if (key.contains(Constants.OBS_VALUE_GREEN_SUFFIX)) {
					modalityComparisonForm.setVerification(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""));
					modalityComparisonForm.setGreenPercentage(results.get(key).toString().replace(".00", "") + "%");
					if (results.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX) != null) {
						modalityComparisonForm.setRedPercentage(results.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX).toString().replace(".00", "") + "%");
					} else {
						modalityComparisonForm.setRedPercentage("0%");
					}
				} else if (key.contains(Constants.OBS_VALUE_RED_SUFFIX)) {
					modalityComparisonForm.setVerification(key.replace(Constants.OBS_VALUE_RED_SUFFIX, ""));
					modalityComparisonForm.setRedPercentage(results.get(key).toString().replace(".00", "") + "%");
					if (results.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX) != null) {
						modalityComparisonForm.setGreenPercentage(results.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX).toString().replace(".00", "") + "%");
					} else {
						modalityComparisonForm.setGreenPercentage("0%");
					}
				}
				modalityComparisonList.add(modalityComparisonForm);
			}
		}
		return modalityComparisonList;
	}

	/**
	 * Gets the global mark by segments group graphic.
	 *
	 * @param request           the request
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @param regenerate        the regenerate
	 * @return the global mark by segments group graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalMarkBySegmentsGroupGraphic(HttpServletRequest request, String filePath, String noDataMess, List<ObservatoryEvaluationForm> pageExecutionList,
			List<CategoriaForm> categories, boolean regenerate) throws Exception {
		final String executionId = request.getParameter(Constants.ID);
		final Map<Integer, List<CategoriaForm>> resultLists = createGraphicsMap(categories);
		final List<CategoryViewListForm> categoriesLabels = new ArrayList<>();
		final MessageResources resources = CrawlerUtils.getResources(request);
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageResultsBySegmentMap(executionId, pageExecutionList, resultLists.get(i));
			final DefaultCategoryDataset dataSet = createDataSet(resultsBySegment, CrawlerUtils.getResources(request));
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				String title = resources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.global.puntuation.allocation.segments.mark.title");
				String rowTitle = resources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.puntuation");
				ChartForm chartForm = new ChartForm(title, "", rowTitle, dataSet, true, false, false, true, true, false, false, x, y,
						pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			}
			// Incluimos los resultados en la request
			for (CategoriaForm category : resultLists.get(i)) {
				CategoryViewListForm categoryView = new CategoryViewListForm(category, infoComparisonBySegmentPuntuation(resources, resultsBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS, categoriesLabels);
		request.setAttribute(Constants.OBSERVATORY_NUM_CPS_GRAPH, resultLists.size());
	}

	/**
	 * Creates the data set.
	 *
	 * @param result           the result
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	private static DefaultCategoryDataset createDataSet(final Map<CategoriaForm, Map<String, BigDecimal>> result, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<CategoriaForm, Map<String, BigDecimal>> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue().get(Constants.OBS_NV), GraphicsUtils.parseLevelLabel(Constants.OBS_NV, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_A), GraphicsUtils.parseLevelLabel(Constants.OBS_A, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_AA), GraphicsUtils.parseLevelLabel(Constants.OBS_AA, messageResources), entry.getKey().getName());
		}
		return dataSet;
	}

	/**
	 * Creates the stacked bar data set for modality.
	 *
	 * @param results          the results
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	private static DefaultCategoryDataset createStackedBarDataSetForModality(final Map<String, BigDecimal> results, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, BigDecimal> entry : results.entrySet()) {
			if (entry.getKey().contains(Constants.OBS_VALUE_RED_SUFFIX)) {
				dataSet.addValue(entry.getValue(), messageResources.getMessage("observatory.graphic.modality.red"),
						entry.getKey().replace(Constants.OBS_VALUE_RED_SUFFIX, "").substring(entry.getKey().replace(Constants.OBS_VALUE_RED_SUFFIX, "").length() - 5));
			} else if (entry.getKey().contains(Constants.OBS_VALUE_GREEN_SUFFIX)) {
				dataSet.addValue(entry.getValue(), messageResources.getMessage("observatory.graphic.modality.green"),
						entry.getKey().replace(Constants.OBS_VALUE_GREEN_SUFFIX, "").substring(entry.getKey().replace(Constants.OBS_VALUE_GREEN_SUFFIX, "").length() - 5));
			}
		}
		return dataSet;
	}

	/**
	 * Gets the mid mark aspect evolution graphic.
	 *
	 * @param request         the request
	 * @param aspect          the aspect
	 * @param noDataMess      the no data mess
	 * @param filePath        the file path
	 * @param resultsByAspect the results by aspect
	 * @param color           the color
	 * @param regenerate      the regenerate
	 * @return the mid mark aspect evolution graphic
	 * @throws Exception the exception
	 */
	public static void getMidMarkAspectEvolutionGraphic(HttpServletRequest request, String aspect, String noDataMess, String filePath, Map<Date, Map<String, BigDecimal>> resultsByAspect, String color,
			boolean regenerate) throws Exception {
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		final String fileName = filePath + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.evolution.aspect.mid.puntuation.name", aspect) + ".jpg";
		File file = new File(fileName);
		String aspectStr = "";
		if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.general");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.alternatives");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.structure");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.navigation");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.presentation");
		}
		// Recuperamos los resultados
		final Map<String, BigDecimal> resultData = calculateAspectEvolutionPuntuationDataSet(aspectStr, resultsByAspect);
		// Incluimos los resultados en la request
		if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AG, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AAL, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AE, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AN, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AP, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			String rowTitle = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mid.puntuation");
			String columnTitle = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.date");
			String title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.evolution.aspect.mid.puntuation", aspectStr);
			GraphicsUtils.createBarChart(resultData, title, rowTitle, columnTitle, color, false, false, true, fileName, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the mid mark verification evolution graphic.
	 *
	 * @param request      the request
	 * @param verification the verification
	 * @param noDataMess   the no data mess
	 * @param filePath     the file path
	 * @param result       the result
	 * @param color        the color
	 * @param regenerate   the regenerate
	 * @return the mid mark verification evolution graphic
	 * @throws Exception the exception
	 */
	public static void getMidMarkVerificationEvolutionGraphic(HttpServletRequest request, String verification, String noDataMess, String filePath, Map<Date, List<ObservatoryEvaluationForm>> result,
			String color, boolean regenerate) throws Exception {
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		String fileName = filePath + File.separator + messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.evolution.verification.mid.puntuation.name", verification)
				+ ".jpg";
		File file = new File(fileName);
		// Recuperamos los resultados
		Map<String, BigDecimal> resultData = calculateVerificationEvolutionPuntuationDataSet(verification, result);
		// Los incluimos en la request
		if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V223, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V224, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V225, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V226, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			String rowTitle = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mid.puntuation");
			String columnTitle = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.date");
			String title = messageResources.getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.evolution.verification.mid.puntuation", verification);
			GraphicsUtils.createBarChart(resultData, title, rowTitle, columnTitle, color, false, false, true, fileName, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the mid mark evolution graphic.
	 *
	 * @param request           the request
	 * @param noDataMess        the no data mess
	 * @param filePath          the file path
	 * @param observatoryResult the observatory result
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the mid mark evolution graphic
	 * @throws Exception the exception
	 */
	public static void getMidMarkEvolutionGraphic(HttpServletRequest request, String noDataMess, String filePath, Map<Date, List<ObservatoryEvaluationForm>> observatoryResult, String color,
			boolean regenerate) throws Exception {
		// Recuperamos los resultados
		Map<String, BigDecimal> resultData = calculateEvolutionPuntuationDataSet(observatoryResult);
		// Los incluimos en la request
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_MID_PUNT, infoMidMarkEvolutionGraphic(resultData));
		File file = new File(filePath);
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.mid.puntuation");
			String columnTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.date");
			String title = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.evolution.mid.puntuation");
			GraphicsUtils.createBarChart(resultData, title, rowTitle, columnTitle, color, false, false, true, filePath, noDataMess, CrawlerUtils.getResources(request), x, y);
		}
	}

	/**
	 * Gets the approval level evolution graphic.
	 *
	 * @param request           the request
	 * @param type              the type
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param observatoryResult the observatory result
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the approval level evolution graphic
	 * @throws Exception the exception
	 */
	public static void getApprovalLevelEvolutionGraphic(HttpServletRequest request, String type, String title, String filePath, String noDataMess,
			Map<Date, List<ObservatoryEvaluationForm>> observatoryResult, String color, boolean regenerate) throws Exception {
		File file = new File(filePath);
		Map<Date, Map<Long, Map<String, Integer>>> result = getEvolutionObservatoriesSitesByType(request.getParameter(Constants.ID_OBSERVATORIO), request.getParameter(Constants.ID),
				observatoryResult);
		Map<String, BigDecimal> resultData = calculatePercentageApprovalSiteLevel(result, type);
		// Los incluimos en la request
		if (type.equals(Constants.OBS_A)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_A, infoLevelEvolutionGraphic(resultData));
		} else if (type.equals(Constants.OBS_AA)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AA, infoLevelEvolutionGraphic(resultData));
		}
		if (type.equals(Constants.OBS_NV)) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_NV, infoLevelEvolutionGraphic(resultData));
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.pages");
			String columnTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.date");
			GraphicsUtils.createBarChart(resultData, title, rowTitle, columnTitle, color, false, true, true, filePath, noDataMess, CrawlerUtils.getResources(request), x, y);
		}
	}

	/**
	 * Gets the mark allocation level segment graphic.
	 *
	 * @param request           the request
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param siteExecutionList the site execution list
	 * @param showColLab        the show col lab
	 * @param regenerate        the regenerate
	 * @return the mark allocation level segment graphic
	 * @throws Exception the exception
	 */
	public static void getMarkAllocationLevelSegmentGraphic(HttpServletRequest request, String title, String filePath, String noDataMess, List<ObservatorySiteEvaluationForm> siteExecutionList,
			boolean showColLab, boolean regenerate) throws Exception {
		final File file = new File(filePath);
		List<ObservatorySiteEvaluationForm> result2 = createOrderFormLevel(siteExecutionList);
		// Los incluimos en la request
		if (showColLab) {
			request.setAttribute(Constants.OBSERVATORY_GRAPHIC_SEGMENT_DATA_LIST_DP, infoMarkAllocationLevelSegment(result2));
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.puntuation");
			String columnTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.portales");
			GraphicsUtils.createBar1PxChart(result2, title, rowTitle, columnTitle, filePath, noDataMess, CrawlerUtils.getResources(request), x, y, showColLab);
		}
	}

	/**
	 * Gets the aspect mids graphic.
	 *
	 * @param request           the request
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param color             the color
	 * @param title             the title
	 * @param regenerate        the regenerate
	 * @return the aspect mids graphic
	 * @throws Exception the exception
	 */
	public static void getAspectMidsGraphic(HttpServletRequest request, String filePath, String noDataMess, List<ObservatoryEvaluationForm> pageExecutionList, String color, String title,
			boolean regenerate) throws Exception {
		final Map<String, BigDecimal> result = aspectMidsPuntuationGraphicData(CrawlerUtils.getResources(request), pageExecutionList);
		// Los incluimos en la request
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA, infoAspectMidsComparison(request, result));
		// Si no existe la grafica, la creamos
		File file = new File(filePath);
		if (!file.exists() || regenerate) {
			String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.puntuation");
			String columnTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.aspects");
			GraphicsUtils.createBarChart(result, title, rowTitle, columnTitle, color, false, false, false, filePath, noDataMess, CrawlerUtils.getResources(request), x, y);
		}
	}

	/**
	 * Info mid mark verification evolution graphic.
	 *
	 * @param resources  the resources
	 * @param resultData the result data
	 * @return the list
	 */
	public static List<LabelValueBean> infoMidMarkVerificationEvolutionGraphic(final MessageResources resources, Map<String, BigDecimal> resultData) {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue;
		for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
			labelValue = new LabelValueBean();
			labelValue.setLabel(entry.getKey());
			if (entry.getValue() != null && entry.getValue().compareTo(new BigDecimal(-1)) != 0) {
				labelValue.setValue(String.valueOf(entry.getValue()));
			} else if (entry.getValue() == null) {
				labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
			} else {
				labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
			}
			labelValueList.add(labelValue);
		}
		return labelValueList;
	}

	/**
	 * Info mid mark aspect evolution graphic.
	 *
	 * @param resources  the resources
	 * @param resultData the result data
	 * @return the list
	 */
	public static List<LabelValueBean> infoMidMarkAspectEvolutionGraphic(final MessageResources resources, Map<String, BigDecimal> resultData) {
		List<LabelValueBean> labelValueList = new ArrayList<>();
		LabelValueBean labelValue;
		for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
			labelValue = new LabelValueBean();
			labelValue.setLabel(entry.getKey());
			if (entry.getValue() != null && entry.getValue().compareTo(new BigDecimal(-1)) != 0) {
				labelValue.setValue(String.valueOf(entry.getValue()));
			} else if (entry.getValue() == null) {
				labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
			} else {
				labelValue.setValue(resources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
			}
			labelValueList.add(labelValue);
		}
		return labelValueList;
	}

	/**
	 * Gets the global result data if is not a annexes generation
	 *
	 * @param executionId       the execution id
	 * @param categoryId        the category id
	 * @param pageExecutionList the page execution list
	 * @return the global result data
	 * @throws Exception the exception
	 */
	public static List<ObservatoryEvaluationForm> getGlobalResultData(String executionId, long categoryId, List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		return getGlobalResultData(executionId, categoryId, pageExecutionList, null, false);
	}

	/**
	 * Gets the global result data during annexes generation
	 *
	 * @param executionId       the execution id
	 * @param categoryId        the category id
	 * @param pageExecutionList the page execution list
	 * @param originAnnexes
	 * @return the global result data
	 * @throws Exception the exception
	 */
	public static List<ObservatoryEvaluationForm> getGlobalResultData(String executionId, long categoryId, List<ObservatoryEvaluationForm> pageExecutionList, boolean originAnnexes) throws Exception {
		return getGlobalResultData(executionId, categoryId, pageExecutionList, null, originAnnexes);
	}

	/**
	 * Gets the global result data.
	 *
	 * @param executionId       the execution id
	 * @param categoryId        the category id
	 * @param pageExecutionList the page execution list
	 * @param idCrawler         the id crawler
	 * @return the global result data
	 * @throws Exception the exception
	 */
	public static List<ObservatoryEvaluationForm> getGlobalResultData(String executionId, long categoryId, List<ObservatoryEvaluationForm> pageExecutionList, Long idCrawler, boolean originAnnexes)
			throws Exception {
		List<ObservatoryEvaluationForm> observatoryEvaluationList = null;
		Connection conn = null;
		try {
			observatoryEvaluationList = (List<ObservatoryEvaluationForm>) CacheUtils.getFromCache(Constants.OBSERVATORY_KEY_CACHE + executionId);
		} catch (NeedsRefreshException nre) {
			Logger.putLog("La cache con id " + Constants.OBSERVATORY_KEY_CACHE + executionId + " no está disponible, se va a regenerar", ResultadosAnonimosObservatorioIntavUtils.class,
					Logger.LOG_LEVEL_WARNING);
			try {
				observatoryEvaluationList = new ArrayList<>();
				List<Long> listAnalysis = new ArrayList<>();
				conn = DataBaseManager.getConnection();
				List<Long> listExecutionsIds = new ArrayList<>();
				if (idCrawler == null) {
					listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, Long.parseLong(executionId), Constants.COMPLEXITY_SEGMENT_NONE);
				} else {
					listExecutionsIds.add(idCrawler);
				}
				if (pageExecutionList == null) {
					for (Long idExecution : listExecutionsIds) {
						listAnalysis.addAll(AnalisisDatos.getAnalysisIdsByTracking(conn, idExecution));
					}
					// Inicializamos el evaluador
					if (!EvaluatorUtility.isInitialized()) {
						EvaluatorUtility.initialize();
					}
					final Evaluator evaluator = new Evaluator();
					int i = 1;
					for (Long idAnalysis : listAnalysis) {
						// Logger.putLog(" i= " + i + "/" + listAnalysis.size() + "(" + idAnalysis + ")", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR);
						i++;
						final Evaluation evaluation = evaluator.getObservatoryAnalisisDB(conn, idAnalysis, EvaluatorUtils.getDocList(), originAnnexes);
						final String methodology = ObservatorioDAO.getMethodology(conn, Long.parseLong(executionId));
						RastreoDAO.getFullfilledCrawlingExecution(conn, Long.parseLong(executionId));
						String aplicacion = CartuchoDAO.getApplicationFromAnalisisId(conn, idAnalysis);
						// Only in NORMATIVA UNE EN2019, warnings points 0.5
						final ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false,
								Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion) ? true : false);
						evaluationForm.setObservatoryExecutionId(Long.parseLong(executionId));
						final FulfilledCrawlingForm ffCrawling = RastreoDAO.getFullfilledCrawlingExecution(conn, evaluationForm.getCrawlerExecutionId());
						if (ffCrawling != null) {
							final SeedForm seedForm = new SeedForm();
							seedForm.setId(String.valueOf(ffCrawling.getSeed().getId()));
							seedForm.setAcronym(ffCrawling.getSeed().getAcronimo());
							seedForm.setName(ffCrawling.getSeed().getNombre());
							// Multidependencia
							seedForm.setCategory(ffCrawling.getSeed().getCategoria().getName());
							evaluationForm.setSeed(seedForm);
						}
						observatoryEvaluationList.add(evaluationForm);
						evaluationForm.getScore();
					}
				} else {
					for (ObservatoryEvaluationForm observatory : pageExecutionList) {
						if (listExecutionsIds.contains(observatory.getCrawlerExecutionId())) {
							observatoryEvaluationList.add(observatory);
						}
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error en getGlobalResultData", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			} finally {
				DataBaseManager.closeConnection(conn);
			}
			CacheUtils.putInCacheForever(observatoryEvaluationList, Constants.OBSERVATORY_KEY_CACHE + executionId);
		}
		return filterObservatoriesByComplexity(observatoryEvaluationList, Long.parseLong(executionId), categoryId);
	}

	/**
	 * Gets the global result data.
	 *
	 * @param executionId       the execution id
	 * @param idFullfilledCrawl the id fullfilled crawl
	 * @return the global result data
	 * @throws Exception the exception
	 */
	public static ObservatoryEvaluationForm getCrawlResultData(String executionId, Long idFullfilledCrawl) throws Exception {
		ObservatoryEvaluationForm observatoryEvaluationForm = null;
		Connection c = null;
		Connection conn = null;
		try {
			List<Long> listAnalysis = new ArrayList<>();
			c = DataBaseManager.getConnection();
			conn = DataBaseManager.getConnection();
			listAnalysis.addAll(AnalisisDatos.getAnalysisIdsByTracking(conn, idFullfilledCrawl));
			// Inicializamos el evaluador
			if (!EvaluatorUtility.isInitialized()) {
				EvaluatorUtility.initialize();
			}
			final Evaluator evaluator = new Evaluator();
			for (Long idAnalysis : listAnalysis) {
				final Evaluation evaluation = evaluator.getObservatoryAnalisisDB(conn, idAnalysis, EvaluatorUtils.getDocList());
				final String methodology = ObservatorioDAO.getMethodology(c, Long.parseLong(executionId));
				RastreoDAO.getFullfilledCrawlingExecution(c, Long.parseLong(executionId));
				String aplicacion = CartuchoDAO.getApplicationFromAnalisisId(c, idAnalysis);
				// Only in NORMATIVA UNE EN2019, warnings points 0.5
				observatoryEvaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false,
						Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion) ? true : false);
				observatoryEvaluationForm.setObservatoryExecutionId(Long.parseLong(executionId));
				final FulfilledCrawlingForm ffCrawling = RastreoDAO.getFullfilledCrawlingExecution(c, observatoryEvaluationForm.getCrawlerExecutionId());
				if (ffCrawling != null) {
					final SeedForm seedForm = new SeedForm();
					seedForm.setId(String.valueOf(ffCrawling.getSeed().getId()));
					seedForm.setAcronym(ffCrawling.getSeed().getAcronimo());
					seedForm.setName(ffCrawling.getSeed().getNombre());
					// Multidependencia
					seedForm.setCategory(ffCrawling.getSeed().getCategoria().getName());
					observatoryEvaluationForm.setSeed(seedForm);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error en getGlobalResultData", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DataBaseManager.closeConnection(c);
			DataBaseManager.closeConnection(conn);
		}
		return observatoryEvaluationForm;
	}

	/**
	 * Filter observatories by complexity.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @param executionId               the execution id
	 * @param categoryId                the category id
	 * @return the list
	 * @throws Exception the exception
	 */
	private static List<ObservatoryEvaluationForm> filterObservatoriesByComplexity(List<ObservatoryEvaluationForm> observatoryEvaluationList, Long executionId, long categoryId) throws Exception {
		List<ObservatoryEvaluationForm> results = new ArrayList<>();
		if (categoryId == Constants.COMPLEXITY_SEGMENT_NONE) {
			results = observatoryEvaluationList;
		} else {
			Connection conn = null;
			try {
				conn = DataBaseManager.getConnection();
				List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, executionId, categoryId);
				for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
					if (listExecutionsIds.contains(observatoryEvaluationForm.getCrawlerExecutionId())) {
						results.add(observatoryEvaluationForm);
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al filtrar observatorios. ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			} finally {
				DataBaseManager.closeConnection(conn);
			}
		}
		return results;
	}

	/**
	 * Calculate aspect evolution puntuation data set.
	 *
	 * @param aspect          the aspect
	 * @param resultsByAspect the results by aspect
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateAspectEvolutionPuntuationDataSet(String aspect, Map<Date, Map<String, BigDecimal>> resultsByAspect) {
		Map<String, BigDecimal> resultData = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				PropertiesManager pmgr = new PropertiesManager();
				DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
				try {
					Date fecha1 = new Date(df.parse(o1).getTime());
					Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		PropertiesManager pmgr = new PropertiesManager();
		DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		for (Map.Entry<Date, Map<String, BigDecimal>> entry : resultsByAspect.entrySet()) {
			resultData.put(df.format(entry.getKey()), entry.getValue().get(aspect));
		}
		return resultData;
	}

	/**
	 * Calculate verification evolution puntuation data set.
	 *
	 * @param verification the verification
	 * @param result       the result
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateVerificationEvolutionPuntuationDataSet(String verification, Map<Date, List<ObservatoryEvaluationForm>> result) {
		TreeMap<String, BigDecimal> resultData = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				PropertiesManager pmgr = new PropertiesManager();
				DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
				try {
					Date fecha1 = new Date(df.parse(o1).getTime());
					Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		PropertiesManager pmgr = new PropertiesManager();
		DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : result.entrySet()) {
			// Para un observatorio en concreto recuperamos la puntuación de una
			// verificación
			BigDecimal value = getVerificationResultsByPoint(entry.getValue(), Constants.OBS_PRIORITY_NONE).get(verification);
			resultData.put(df.format(entry.getKey()), value);
		}
		return resultData;
	}

	/**
	 * Calculate evolution puntuation data set.
	 *
	 * @param result the result
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateEvolutionPuntuationDataSet(Map<Date, List<ObservatoryEvaluationForm>> result) {
		TreeMap<String, BigDecimal> resultData = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				PropertiesManager pmgr = new PropertiesManager();
				DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
				try {
					Date fecha1 = new Date(df.parse(o1).getTime());
					Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		PropertiesManager pmgr = new PropertiesManager();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : result.entrySet()) {
			int numPages = entry.getValue().size();
			BigDecimal value = BigDecimal.ZERO;
			if ((entry.getValue()) != null && !(entry.getValue()).isEmpty()) {
				for (ObservatoryEvaluationForm observatoryEvaluationForm : entry.getValue()) {
					value = value.add(observatoryEvaluationForm.getScore());
				}
				value = value.divide(new BigDecimal(numPages), 2, BigDecimal.ROUND_HALF_UP);
			}
			DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
			resultData.put(df.format(entry.getKey()), value);
		}
		return resultData;
	}

	/**
	 * Calculate percentage approval site level.
	 *
	 * @param result the result
	 * @param type   the type
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculatePercentageApprovalSiteLevel(Map<Date, Map<Long, Map<String, Integer>>> result, String type) {
		PropertiesManager pmgr = new PropertiesManager();
		DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
		TreeMap<String, BigDecimal> percentagesMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				PropertiesManager pmgr = new PropertiesManager();
				DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
				try {
					Date fecha1 = new Date(df.parse(o1).getTime());
					Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		for (Map.Entry<Date, Map<Long, Map<String, Integer>>> dateMapEntry : result.entrySet()) {
			int numSites = dateMapEntry.getValue().size();
			int numSitesType = 0;
			for (Map.Entry<Long, Map<String, Integer>> longMapEntry : dateMapEntry.getValue().entrySet()) {
				String portalLevel = siteLevel(dateMapEntry.getValue(), longMapEntry.getKey());
				if (portalLevel.equals(type)) {
					numSitesType++;
				}
			}
			BigDecimal percentage = BigDecimal.ZERO;
			if (numSitesType != 0) {
				percentage = (new BigDecimal(numSitesType)).divide(new BigDecimal(numSites), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}
			percentagesMap.put(df.format(dateMapEntry.getKey()), percentage);
		}
		return percentagesMap;
	}

	/**
	 * Gets the evolution observatories sites by type.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param result        the result
	 * @return the evolution observatories sites by type
	 */
	public static Map<Date, Map<Long, Map<String, Integer>>> getEvolutionObservatoriesSitesByType(String observatoryId, String executionId, Map<Date, List<ObservatoryEvaluationForm>> result) {
		final Map<Date, Map<Long, Map<String, Integer>>> resultData = new HashMap<>();
		Connection c = null;
		try {
			c = DataBaseManager.getConnection();
			final PropertiesManager pmgr = new PropertiesManager();
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, Long.parseLong(observatoryId), Long.parseLong(executionId),
					Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id")));
			for (Map.Entry<Long, Date> longDateEntry : executedObservatoryIdMap.entrySet()) {
				final List<ObservatoryEvaluationForm> pageList = result.get(longDateEntry.getValue());
				final Map<Long, Map<String, Integer>> sites = getSitesByType(pageList);
				resultData.put(longDateEntry.getValue(), sites);
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
		} finally {
			DataBaseManager.closeConnection(c);
		}
		return resultData;
	}

	/**
	 * Result evolution data.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @return the map
	 */
	public static Map<Date, List<ObservatoryEvaluationForm>> resultEvolutionData(Long observatoryId, Long executionId) {
		Connection c = null;
		Map<Date, List<ObservatoryEvaluationForm>> resultData = new HashMap<>();
		try {
			PropertiesManager pmgr = new PropertiesManager();
			c = DataBaseManager.getConnection();
			Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatoryId, executionId,
					Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id")));
			for (Map.Entry<Long, Date> entry : executedObservatoryIdMap.entrySet()) {
				List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE, null);
				resultData.put(entry.getValue(), pageList);
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
		} finally {
			DataBaseManager.closeConnection(c);
		}
		return resultData;
	}

	/**
	 * Calculate percentage.
	 *
	 * @param values the values
	 * @return the map
	 */
	private static Map<String, BigDecimal> calculatePercentage(final Map<String, Integer> values) {
		int total = 0;
		final Map<String, BigDecimal> result = new HashMap<>();
		for (Map.Entry<String, Integer> stringIntegerEntry : values.entrySet()) {
			total += stringIntegerEntry.getValue();
		}
		BigDecimal totalPercentage = BigDecimal.ZERO;
		String fitResultKey = "";
		for (Map.Entry<String, Integer> entry : values.entrySet()) {
			if (total != 0) {
				result.put(entry.getKey(), (new BigDecimal(entry.getValue())).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			} else {
				result.put(entry.getKey(), (BigDecimal.ZERO));
			}
			totalPercentage = totalPercentage.add(result.get(entry.getKey()));
			fitResultKey = entry.getKey();
		}
		// ajustamos el resultado por si se pasa de 100 a causa del redondeo
		if (totalPercentage.compareTo(new BigDecimal(100)) != 0) {
			result.put(fitResultKey, result.get(fitResultKey).subtract(totalPercentage.subtract(new BigDecimal(100))));
		}
		return result;
	}

	/**
	 * Gets the sites list by level.
	 *
	 * @param pages the pages
	 * @return the sites list by level
	 */
	public static List<ObservatorySiteEvaluationForm> getSitesListByLevel(final List<ObservatoryEvaluationForm> pages) {
		final List<ObservatorySiteEvaluationForm> siteList = new ArrayList<>();
		try {
			Map<Long, ObservatorySiteEvaluationForm> siteMap = new HashMap<>();
			for (ObservatoryEvaluationForm page : pages) {
				List<ObservatoryEvaluationForm> pagesL = new ArrayList<>();
				ObservatorySiteEvaluationForm site = new ObservatorySiteEvaluationForm();
				if (siteMap.get(page.getCrawlerExecutionId()) != null) {
					site = siteMap.get(page.getCrawlerExecutionId());
					pagesL = siteMap.get(page.getCrawlerExecutionId()).getPages();
					site.setScore(site.getScore().add(page.getScore()));
				} else {
					site.setName(page.getSeed().getName());
					site.setId(page.getCrawlerExecutionId());
					site.setScore(page.getScore());
				}
				site.setAcronym(page.getSeed().getAcronym());
				site.setIdSeed(Long.valueOf(page.getSeed().getId()));
				pagesL.add(page);
				site.setPages(pagesL);
				siteMap.put(page.getCrawlerExecutionId(), site);
			}
			for (Map.Entry<Long, ObservatorySiteEvaluationForm> siteEntry : siteMap.entrySet()) {
				ObservatorySiteEvaluationForm observatorySite = siteEntry.getValue();
				observatorySite.setScore(observatorySite.getScore().divide(new BigDecimal(observatorySite.getPages().size()), 2, BigDecimal.ROUND_HALF_UP));
				observatorySite.setLevel(siteLevel(getSitesByType(observatorySite.getPages()), siteEntry.getKey()));
				observatorySite.setName(siteEntry.getValue().getName());
				siteList.add(siteEntry.getValue());
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return siteList;
	}

	/**
	 * Site level.
	 *
	 * @param portalInformation the portal information
	 * @param idSite            the id site
	 * @return the string
	 */
	private static String siteLevel(final Map<Long, Map<String, Integer>> portalInformation, final Long idSite) {
		final Map<String, Integer> pageType = portalInformation.get(idSite);
		final Integer numPages = pageType.get(Constants.OBS_A) + pageType.get(Constants.OBS_AA) + pageType.get(Constants.OBS_NV);
		final BigDecimal value = ((new BigDecimal(pageType.get(Constants.OBS_A)).multiply(new BigDecimal(5))).add(new BigDecimal(pageType.get(Constants.OBS_AA)).multiply(BigDecimal.TEN)))
				.divide(new BigDecimal(numPages), 2, BigDecimal.ROUND_HALF_UP);
		if (value.compareTo(new BigDecimal(8)) >= 0) {
			return Constants.OBS_AA;
		} else if (value.compareTo(new BigDecimal("3.5")) <= 0) {
			return Constants.OBS_NV;
		} else {
			return Constants.OBS_A;
		}
	}

	/**
	 * Gets the global accessibility level allocation segment graphic.
	 *
	 * @param request           the request
	 * @param pageExecutionList the page execution list
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param regenerate        the regenerate
	 * @return the global accessibility level allocation segment graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalAccessibilityLevelAllocationSegmentGraphic(HttpServletRequest request, List<ObservatoryEvaluationForm> pageExecutionList, String title, String filePath,
			String noDataMess, boolean regenerate) throws Exception {
		PropertiesManager pmgr = new PropertiesManager();
		File file = new File(filePath);
		Map<String, Integer> result = getResultsBySiteLevel(pageExecutionList);
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		if (!file.exists() || regenerate) {
			final int total = result.get(Constants.OBS_A) + result.get(Constants.OBS_AA) + result.get(Constants.OBS_NV);
			DefaultPieDataset dataSet = new DefaultPieDataset();
			dataSet.setValue(GraphicsUtils.parseLevelLabel(Constants.OBS_NV, messageResources), result.get(Constants.OBS_NV));
			dataSet.setValue(GraphicsUtils.parseLevelLabel(Constants.OBS_A, messageResources), result.get(Constants.OBS_A));
			dataSet.setValue(GraphicsUtils.parseLevelLabel(Constants.OBS_AA, messageResources), result.get(Constants.OBS_AA));
			GraphicsUtils.createPieChart(dataSet, title, messageResources.getMessage("observatory.graphic.site.number"), total, filePath, noDataMess,
					pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), x, y);
		}
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG, infoGlobalAccessibilityLevel(messageResources, result));
	}

	/**
	 * Creates the graphics map.
	 *
	 * @param categories the categories
	 * @return the map
	 */
	public static Map<Integer, List<CategoriaForm>> createGraphicsMap(final List<CategoriaForm> categories) {
		final Map<Integer, List<CategoriaForm>> resultLists = new TreeMap<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int numBarByGrapg = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.max.bar.graph"));
		int keyMap = 1;
		for (int i = 1; i <= categories.size(); i++) {
			List<CategoriaForm> list = new ArrayList<>();
			if (resultLists.get(keyMap) != null) {
				list = resultLists.get(keyMap);
			}
			list.add(categories.get(i - 1));
			resultLists.put(keyMap, list);
			if ((i >= numBarByGrapg) && (i % numBarByGrapg == 0) && (categories.size() != i + 1)) {
				keyMap++;
			}
		}
		return resultLists;
	}

	/**
	 * Gets the global mark by segment graphic.
	 *
	 * @param request           the request
	 * @param pageExecutionList the page execution list
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param categories        the categories
	 * @return the global mark by segment graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalMarkBySegmentGraphic(HttpServletRequest request, List<ObservatoryEvaluationForm> pageExecutionList, String title, String filePath, String noDataMess,
			List<CategoriaForm> categories) throws Exception {
		PropertiesManager pmgr = new PropertiesManager();
		String executionId = request.getParameter(Constants.ID);
		Map<Integer, List<CategoriaForm>> resultLists = createGraphicsMap(categories);
		List<CategoryViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			Map<CategoriaForm, Map<String, BigDecimal>> resultDataBySegment = calculateMidPuntuationResultsBySegmentMap(executionId, pageExecutionList, resultLists.get(i));
			if (!file.exists()) {
				String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.graphic.puntuation");
				ChartForm observatoryGraphicsForm = new ChartForm(title, "", rowTitle, createDataSet(resultDataBySegment, CrawlerUtils.getResources(request)), true, true, false, false, true, false,
						false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				GraphicsUtils.createSeriesBarChart(observatoryGraphicsForm, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg", noDataMess, CrawlerUtils.getResources(request), true);
			}
			for (CategoriaForm category : resultLists.get(i)) {
				CategoryViewListForm categoryView = new CategoryViewListForm(category, infoComparisonBySegment(CrawlerUtils.getResources(request), resultDataBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		request.setAttribute(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS, categoriesLabels);
		request.setAttribute(Constants.OBSERVATORY_NUM_CAS_GRAPH, resultLists.size());
	}

	/**
	 * Gets the results by site level.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @return the results by site level
	 */
	public static Map<String, Integer> getResultsBySiteLevel(final List<ObservatoryEvaluationForm> observatoryEvaluationList) {
		final Map<String, Integer> globalResult = new HashMap<>();
		globalResult.put(Constants.OBS_NV, 0);
		globalResult.put(Constants.OBS_A, 0);
		globalResult.put(Constants.OBS_AA, 0);
		final Map<Long, Map<String, Integer>> globalResultBySiteType = getSitesByType(observatoryEvaluationList);
		for (Long idSite : globalResultBySiteType.keySet()) {
			final Map<String, Integer> pageType = globalResultBySiteType.get(idSite);
			final Integer numPages = pageType.get(Constants.OBS_A) + pageType.get(Constants.OBS_AA) + pageType.get(Constants.OBS_NV);
			final BigDecimal value = ((new BigDecimal(pageType.get(Constants.OBS_A)).multiply(new BigDecimal(5))).add(new BigDecimal(pageType.get(Constants.OBS_AA)).multiply(BigDecimal.TEN)))
					.divide(new BigDecimal(numPages), 2, BigDecimal.ROUND_HALF_UP);
			if (value.compareTo(new BigDecimal(8)) >= 0) {
				globalResult.put(Constants.OBS_AA, globalResult.get(Constants.OBS_AA) + 1);
			} else if (value.compareTo(new BigDecimal("3.5")) <= 0) {
				globalResult.put(Constants.OBS_NV, globalResult.get(Constants.OBS_NV) + 1);
			} else {
				globalResult.put(Constants.OBS_A, globalResult.get(Constants.OBS_A) + 1);
			}
		}
		return globalResult;
	}

	/**
	 * Aspect mids puntuation graphic data.
	 *
	 * @param resources  the resources
	 * @param resultData the result data
	 * @return the map
	 */
	public static Map<String, BigDecimal> aspectMidsPuntuationGraphicData(final MessageResources resources, List<ObservatoryEvaluationForm> resultData) {
		Map<String, List<LabelValueBean>> globalResult = new HashMap<>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
			for (ObservatoryLevelForm levelForm : observatoryEvaluationForm.getGroups()) {
				for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
					for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
						if (!globalResult.containsKey(subgroupForm.getAspect())) {
							globalResult.put(subgroupForm.getAspect(), new ArrayList<LabelValueBean>());
						}
						LabelValueBean lvb = new LabelValueBean();
						lvb.setLabel(subgroupForm.getDescription());
						lvb.setValue(String.valueOf(subgroupForm.getValue()));
						globalResult.get(subgroupForm.getAspect()).add(lvb);
					}
				}
			}
		}
		Map<String, BigDecimal> results = new HashMap<>();
		for (Map.Entry<String, List<LabelValueBean>> globalResultEntry : globalResult.entrySet()) {
			String aspect = resources.getMessage(globalResultEntry.getKey());
			// Recorremos las verificaciones de cada aspecto
			Map<String, List<BigDecimal>> partialResultsMap = new HashMap<>();
			for (LabelValueBean lvb : globalResultEntry.getValue()) {
				int value = Integer.parseInt(lvb.getValue());
				if (!partialResultsMap.containsKey(lvb.getLabel())) {
					partialResultsMap.put(lvb.getLabel(), new ArrayList<BigDecimal>());
				}
				if (value == Constants.OBS_VALUE_GREEN_ONE) {
					partialResultsMap.get(lvb.getLabel()).add(BigDecimal.ONE);
				} else if (value == Constants.OBS_VALUE_GREEN_ZERO || value == Constants.OBS_VALUE_RED_ZERO) {
					partialResultsMap.get(lvb.getLabel()).add(BigDecimal.ZERO);
				}
			}
			// Hemos recorrido las verificaciones del aspecto, ahora calculamos la media de
			// cada una
			Map<String, BigDecimal> verificationsMap = new HashMap<>();
			for (String verificationKey : partialResultsMap.keySet()) {
				List<BigDecimal> verificationsList = partialResultsMap.get(verificationKey);
				if (!verificationsList.isEmpty()) {
					if (!verificationsMap.containsKey(verificationKey)) {
						verificationsMap.put(verificationKey, BigDecimal.ZERO);
					}
					for (BigDecimal value : verificationsList) {
						verificationsMap.put(verificationKey, verificationsMap.get(verificationKey).add(value));
					}
					verificationsMap.put(verificationKey, verificationsMap.get(verificationKey).divide(new BigDecimal(verificationsList.size()), 2, BigDecimal.ROUND_HALF_UP));
				}
			}
			// Teniendo las medias de verificaciones de un aspecto, pasamos a calcular el
			// valor medio de ese aspecto
			if (!results.containsKey(aspect)) {
				results.put(aspect, BigDecimal.ZERO);
			}
			for (String verificationKey : verificationsMap.keySet()) {
				results.put(aspect, results.get(aspect).add(verificationsMap.get(verificationKey)));
			}
			if (verificationsMap.size() > 0) {
				results.put(aspect, results.get(aspect).divide(new BigDecimal(verificationsMap.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN));
			}
		}
		return results;
	}

	/**
	 * Bar graphic from mid puntuation segment data.
	 *
	 * @param categoryList the category list
	 * @return the map
	 */
	public static Map<String, BigDecimal> barGraphicFromMidPuntuationSegmentData(List<ObservatorySiteEvaluationForm> categoryList) {
		final Map<String, BigDecimal> globalResult = new HashMap<>();
		BigDecimal countA = BigDecimal.ZERO;
		BigDecimal countAA = BigDecimal.ZERO;
		BigDecimal countNV = BigDecimal.ZERO;
		globalResult.put(Constants.OBS_NV, BigDecimal.ZERO);
		globalResult.put(Constants.OBS_A, BigDecimal.ZERO);
		globalResult.put(Constants.OBS_AA, BigDecimal.ZERO);
		for (ObservatorySiteEvaluationForm observatorySite : categoryList) {
			if (observatorySite.getLevel().equals(Constants.OBS_A)) {
				countA = countA.add(BigDecimal.ONE);
				globalResult.put(Constants.OBS_A, globalResult.get(Constants.OBS_A).add(observatorySite.getScore()));
			}
			if (observatorySite.getLevel().equals(Constants.OBS_AA)) {
				countAA = countAA.add(BigDecimal.ONE);
				globalResult.put(Constants.OBS_AA, globalResult.get(Constants.OBS_AA).add(observatorySite.getScore()));
			}
			if (observatorySite.getLevel().equals(Constants.OBS_NV)) {
				countNV = countNV.add(BigDecimal.ONE);
				globalResult.put(Constants.OBS_NV, globalResult.get(Constants.OBS_NV).add(observatorySite.getScore()));
			}
		}
		if (!countA.equals(BigDecimal.ZERO)) {
			globalResult.put(Constants.OBS_A, globalResult.get(Constants.OBS_A).divide(countA, 2, BigDecimal.ROUND_HALF_UP));
		}
		if (!countAA.equals(BigDecimal.ZERO)) {
			globalResult.put(Constants.OBS_AA, globalResult.get(Constants.OBS_AA).divide(countAA, 2, BigDecimal.ROUND_HALF_UP));
		}
		if (!countNV.equals(BigDecimal.ZERO)) {
			globalResult.put(Constants.OBS_NV, globalResult.get(Constants.OBS_NV).divide(countNV, 2, BigDecimal.ROUND_HALF_UP));
		}
		return globalResult;
	}

	/**
	 * Gets the verification results by point.
	 *
	 * @param resultData the result data
	 * @param level      the level
	 * @return the verification results by point
	 */
	public static Map<String, BigDecimal> getVerificationResultsByPoint(List<ObservatoryEvaluationForm> resultData, String level) {
		Map<String, BigDecimal> results = new TreeMap<>();
		Map<String, Integer> numPoint = new HashMap<>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
			for (ObservatoryLevelForm observatoryLevelForm : observatoryEvaluationForm.getGroups()) {
				if (level.equals(Constants.OBS_PRIORITY_NONE) || observatoryLevelForm.getName().equals(level)) {
					for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
						for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
							// Se comprueba si puntúa o no puntúa
							if (observatorySubgroupForm.getValue() != Constants.OBS_VALUE_NOT_SCORE) {
								// Si puntúa, se isNombreValido si se le da un 0 o un 1
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
									// Si le damos un 1, lo añadimos a la puntuación e incrementamos el número
									// de puntos que han puntuado
									if (results.get(observatorySubgroupForm.getDescription()) == null) {
										results.put(observatorySubgroupForm.getDescription(), new BigDecimal(1));
										numPoint.put(observatorySubgroupForm.getDescription(), 1);
									} else {
										results.put(observatorySubgroupForm.getDescription(), results.get(observatorySubgroupForm.getDescription()).add(new BigDecimal(1)));
										if (numPoint.get(observatorySubgroupForm.getDescription()) == -1) {
											numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 2);
										} else {
											numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
										}
									}
								} else if (observatorySubgroupForm.getDescription() != null && observatorySubgroupForm.getDescription().startsWith("minhap.observatory.5_0")
										&& observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ZERO) {
									// Si le damos un 1, lo añadimos a la puntuación e incrementamos el número
									// de puntos que han puntuado
									if (results.get(observatorySubgroupForm.getDescription()) == null) {
										results.put(observatorySubgroupForm.getDescription(), new BigDecimal(0.5));
										numPoint.put(observatorySubgroupForm.getDescription(), 1);
									} else {
										results.put(observatorySubgroupForm.getDescription(), results.get(observatorySubgroupForm.getDescription()).add(new BigDecimal(0.5)));
										if (numPoint.get(observatorySubgroupForm.getDescription()) == -1) {
											numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 2);
										} else {
											numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
										}
									}
								} else {
									// Si le damos un 0 solamente incrementamos el número de puntos
									if (results.get(observatorySubgroupForm.getDescription()) == null) {
										results.put(observatorySubgroupForm.getDescription(), new BigDecimal(0));
										numPoint.put(observatorySubgroupForm.getDescription(), 1);
									} else {
										numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
									}
								}
							} else {
								if (results.get(observatorySubgroupForm.getDescription()) == null) {
									results.put(observatorySubgroupForm.getDescription(), new BigDecimal(0));
									numPoint.put(observatorySubgroupForm.getDescription(), -1);
								}
							}
						}
					}
				}
			}
		}
		// Cambiamos las claves por el nombre y calculamos la media
		final Map<String, BigDecimal> verificationResultsByPoint = new TreeMap<>();
		for (Map.Entry<String, BigDecimal> resultEntry : results.entrySet()) {
			String name = resultEntry.getKey().substring(resultEntry.getKey().length() - 5);
			BigDecimal value;
			if (numPoint.get(resultEntry.getKey()) != -1 && numPoint.get(resultEntry.getKey()) != 0) {
				value = resultEntry.getValue().divide(BigDecimal.valueOf(numPoint.get(resultEntry.getKey())), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN);
			} else if (numPoint.get(resultEntry.getKey()) == -1) {
				value = new BigDecimal(-1);
			} else {
				value = BigDecimal.ZERO;
			}
			verificationResultsByPoint.put(name, value);
		}
		return verificationResultsByPoint;
	}

	/**
	 * Gets the verification results by point and modality.
	 *
	 * @param resultData the result data
	 * @param level      the level
	 * @return the verification results by point and modality
	 */
	public static Map<String, BigDecimal> getVerificationResultsByPointAndModality(final List<ObservatoryEvaluationForm> resultData, final String level) {
		final Map<String, BigDecimal> results = new TreeMap<>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
			for (ObservatoryLevelForm observatoryLevelForm : observatoryEvaluationForm.getGroups()) {
				if (level.equals(Constants.OBS_PRIORITY_NONE) || observatoryLevelForm.getName().equals(level)) {
					for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
						for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
							if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE || observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ZERO
									|| observatorySubgroupForm.getValue() == Constants.OBS_VALUE_NOT_SCORE) {
								if (results.containsKey(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_GREEN_SUFFIX)) {
									results.put(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_GREEN_SUFFIX,
											results.get(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_GREEN_SUFFIX).add(BigDecimal.ONE));
								} else {
									results.put(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_GREEN_SUFFIX, BigDecimal.ONE);
								}
							} else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
								if (results.containsKey(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_RED_SUFFIX)) {
									results.put(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_RED_SUFFIX,
											results.get(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_RED_SUFFIX).add(BigDecimal.ONE));
								} else {
									results.put(observatorySubgroupForm.getDescription() + Constants.OBS_VALUE_RED_SUFFIX, BigDecimal.ONE);
								}
							}
						}
					}
				}
			}
		}
		for (Map.Entry<String, BigDecimal> stringBigDecimalEntry : results.entrySet()) {
			results.put(stringBigDecimalEntry.getKey(), stringBigDecimalEntry.getValue().divide(new BigDecimal(resultData.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
		}
		return results;
	}

	/**
	 * Creates the order form level.
	 *
	 * @param results the results
	 * @return the list
	 */
	public static List<ObservatorySiteEvaluationForm> createOrderFormLevel(List<ObservatorySiteEvaluationForm> results) {
		Collections.sort(results, new Comparator<ObservatorySiteEvaluationForm>() {
			@Override
			public int compare(ObservatorySiteEvaluationForm o1, ObservatorySiteEvaluationForm o2) {
				return o2.getScore().compareTo(o1.getScore());
			}
		});
		return results;
	}

	/**
	 * Gets the pages by type.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @return the pages by type
	 */
	public static Map<String, List<ObservatoryEvaluationForm>> getPagesByType(List<ObservatoryEvaluationForm> observatoryEvaluationList) {
		final Map<String, List<ObservatoryEvaluationForm>> globalResult = new HashMap<>();
		globalResult.put(Constants.OBS_NV, new ArrayList<ObservatoryEvaluationForm>());
		globalResult.put(Constants.OBS_A, new ArrayList<ObservatoryEvaluationForm>());
		globalResult.put(Constants.OBS_AA, new ArrayList<ObservatoryEvaluationForm>());
		final PropertiesManager pmgr = new PropertiesManager();
		int maxFails = 0;
		// Recuperamos el cartucho asociado al analsis
		try (Connection c = DataBaseManager.getConnection()) {
			String aplicacion = CartuchoDAO.getApplicationFromAnalisisId(c, observatoryEvaluationList.get(0).getIdAnalysis());
			if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
			} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion)) {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
			} else if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
			} else {
				maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number"));
			}
			DataBaseManager.closeConnection(c);
		} catch (Exception e) {
			maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number"));
		}
		// Se recorren las páginas de cada observatorio
		for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
			boolean isA = true;
			boolean isAA = true;
			// Se recorren los niveles de análisis
			for (ObservatoryLevelForm observatoryLevel : observatoryEvaluationForm.getGroups()) {
				// Se recorren los niveles de acecuación
				for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevel.getSuitabilityGroups()) {
					int numZeroRed = 0;
					if (observatorySuitabilityForm.getName().equals(Constants.OBS_A)) {
						if ((observatoryLevel.getName().equals(Constants.OBS_N1)) || (isA)) {
							for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
									numZeroRed = numZeroRed + 1;
								}
							}
							if (numZeroRed > maxFails) {
								isA = false;
							}
						}
					} else if (observatorySuitabilityForm.getName().equals(Constants.OBS_AA) && isA) {
						if ((observatoryLevel.getName().equals(Constants.OBS_N1)) || (isAA)) {
							for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
									numZeroRed = numZeroRed + 1;
								}
							}
							if (numZeroRed > maxFails) {
								isAA = false;
							}
						}
					}
				}
			}
			if (isA && isAA) {
				final List<ObservatoryEvaluationForm> globalResult2 = globalResult.get(Constants.OBS_AA);
				globalResult2.add(observatoryEvaluationForm);
				globalResult.put(Constants.OBS_AA, globalResult2);
			} else if (isA) {
				final List<ObservatoryEvaluationForm> globalResult2 = globalResult.get(Constants.OBS_A);
				globalResult2.add(observatoryEvaluationForm);
				globalResult.put(Constants.OBS_A, globalResult2);
			} else {
				final List<ObservatoryEvaluationForm> globalResult2 = globalResult.get(Constants.OBS_NV);
				globalResult2.add(observatoryEvaluationForm);
				globalResult.put(Constants.OBS_NV, globalResult2);
			}
		}
		return globalResult;
	}

	/**
	 * Gets the sites by type.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @return the sites by type
	 */
	private static Map<Long, Map<String, Integer>> getSitesByType(List<ObservatoryEvaluationForm> observatoryEvaluationList) {
		final Map<String, List<ObservatoryEvaluationForm>> pagesByType = getPagesByType(observatoryEvaluationList);
		final Map<Long, Map<String, Integer>> sitesByType = new HashMap<>();
		for (String key : pagesByType.keySet()) {
			for (ObservatoryEvaluationForm observatoryEvaluationForm : pagesByType.get(key)) {
				if (sitesByType.get(observatoryEvaluationForm.getCrawlerExecutionId()) != null) {
					Map<String, Integer> value = sitesByType.get(observatoryEvaluationForm.getCrawlerExecutionId());
					value.put(key, value.get(key) + 1);
					sitesByType.put(observatoryEvaluationForm.getCrawlerExecutionId(), value);
				} else {
					final Map<String, Integer> initialValues = new HashMap<>();
					if (key.equals(Constants.OBS_NV)) {
						initialValues.put(Constants.OBS_NV, 1);
					} else {
						initialValues.put(Constants.OBS_NV, 0);
					}
					if (key.equals(Constants.OBS_A)) {
						initialValues.put(Constants.OBS_A, 1);
					} else {
						initialValues.put(Constants.OBS_A, 0);
					}
					if (key.equals(Constants.OBS_AA)) {
						initialValues.put(Constants.OBS_AA, 1);
					} else {
						initialValues.put(Constants.OBS_AA, 0);
					}
					sitesByType.put(observatoryEvaluationForm.getCrawlerExecutionId(), initialValues);
				}
			}
		}
		return sitesByType;
	}

	/**
	 * Calculate percentage results by segment map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @return the map
	 * @throws Exception the exception
	 */
	// Cálculo de resultados
	public static Map<CategoriaForm, Map<String, BigDecimal>> calculatePercentageResultsBySegmentMap(String executionId, List<ObservatoryEvaluationForm> pageExecutionList,
			List<CategoriaForm> categories) throws Exception {
		Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<CategoriaForm>() {
			@Override
			public int compare(CategoriaForm o1, CategoriaForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (CategoriaForm category : categories) {
			List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList);
			resultsBySegment.put(category, calculatePercentage(getResultsBySiteLevel(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Calculate mid puntuation results by segment map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<CategoriaForm, Map<String, BigDecimal>> calculateMidPuntuationResultsBySegmentMap(String executionId, List<ObservatoryEvaluationForm> pageExecutionList,
			List<CategoriaForm> categories) throws Exception {
		Connection conn = null;
		try {
			conn = DataBaseManager.getConnection();
			final Map<CategoriaForm, Map<String, BigDecimal>> resultDataBySegment = new TreeMap<>(new Comparator<CategoriaForm>() {
				@Override
				public int compare(CategoriaForm o1, CategoriaForm o2) {
					return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
				}
			});
			for (CategoriaForm category : categories) {
				final List<ObservatorySiteEvaluationForm> categoryList = getSitesListByLevel(getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList));
				resultDataBySegment.put(category, barGraphicFromMidPuntuationSegmentData(categoryList));
			}
			return resultDataBySegment;
		} catch (Exception e) {
			Logger.putLog("Error al recuperar datos de la BBDD.", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DataBaseManager.closeConnection(conn);
		}
	}
}