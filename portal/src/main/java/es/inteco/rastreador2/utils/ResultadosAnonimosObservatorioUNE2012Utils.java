/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.rastreador2.utils.GraphicsUtils.parseLevelLabel;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
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
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.view.forms.CategoryViewListForm;

/**
 * The Class ResultadosAnonimosObservatorioUNE2012Utils.
 */
public final class ResultadosAnonimosObservatorioUNE2012Utils {
	/** The Constant OBSERVATORY_ASPECT_ALTERNATIVES. */
	private static final String OBSERVATORY_ASPECT_ALTERNATIVES = "observatory.aspect.alternatives";
	/** The Constant OBSERVATORY_ASPECT_NAVIGATION. */
	private static final String OBSERVATORY_ASPECT_NAVIGATION = "observatory.aspect.navigation";
	/** The Constant OBSERVATORY_ASPECT_STRUCTURE. */
	private static final String OBSERVATORY_ASPECT_STRUCTURE = "observatory.aspect.structure";
	/** The Constant OBSERVATORY_ASPECT_PRESENTATION. */
	private static final String OBSERVATORY_ASPECT_PRESENTATION = "observatory.aspect.presentation";
	/** The Constant OBSERVATORY_ASPECT_GENERAL. */
	private static final String OBSERVATORY_ASPECT_GENERAL = "observatory.aspect.general";
	/** The Constant EXCEPTION. */
	private static final String EXCEPTION = "Exception: ";
	/** The Constant GRAFICA_SIN_DATOS. */
	private static final String GRAFICA_SIN_DATOS = "grafica.sin.datos";
	/** The Constant CHART_EVOLUTION_MP_GREEN_COLOR. */
	private static final String CHART_EVOLUTION_MP_GREEN_COLOR = "chart.evolution.mp.green.color";
	/** The Constant CHART_EVOLUTION_INTECO_RED_COLORS. */
	private static final String CHART_EVOLUTION_INTECO_RED_COLORS = "chart.evolution.inteco.red.colors";
	/** The Constant BIG_DECIMAL_HUNDRED. */
	public static final BigDecimal BIG_DECIMAL_HUNDRED = BigDecimal.valueOf(100);
	/** The x. */
	// GENERATE GRAPHIC METHODS
	private static int x = 0;
	/** The y. */
	private static int y = 0;
	static {
		PropertiesManager pmgr = new PropertiesManager();
		x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
		y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
	}

	/**
	 * Instantiates a new resultados anonimos observatorio UNE 2012 utils.
	 */
	private ResultadosAnonimosObservatorioUNE2012Utils() {
	}

	/**
	 * Generate graphics.
	 *
	 * @param messageResources       the message resources
	 * @param executionId            the execution id
	 * @param idExecutionObservatory the id execution observatory
	 * @param observatoryId          the observatory id
	 * @param filePath               the file path
	 * @param type                   the type
	 * @param regenerate             the regenerate
	 * @throws Exception the exception
	 */
	public static void generateGraphics(final MessageResources messageResources, String executionId, final Long idExecutionObservatory, final String observatoryId, final String filePath,
			final String type, final boolean regenerate) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final PropertiesManager pmgr = new PropertiesManager();
			String color = pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_INTECO_RED_COLORS);
			if (type != null && type.equals(Constants.MINISTERIO_P)) {
				color = pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR);
			}
			// recuperamos las categorias del observatorio
			final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, idExecutionObservatory);
			generateGlobalGraphics(messageResources, executionId, filePath, categories, color, regenerate);
			// iteramos sobre ellas y genermos las gráficas
			for (CategoriaForm categoryForm : categories) {
				generateCategoryGraphics(messageResources, executionId, categoryForm, filePath, color, regenerate);
			}
			generateEvolutionGraphics(messageResources, observatoryId, executionId, filePath, color, regenerate);
		} catch (Exception e) {
			Logger.putLog("No se han generado las gráficas correctamente.", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Generate global graphics.
	 *
	 * @param messageResources the message resources
	 * @param executionId      the execution id
	 * @param filePath         the file path
	 * @param categories       the categories
	 * @param color            the color
	 * @param regenerate       the regenerate
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateGlobalGraphics(final MessageResources messageResources, final String executionId, final String filePath, final List<CategoriaForm> categories,
			final String color, final boolean regenerate) throws Exception {
		final List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(executionId, Constants.COMPLEXITY_SEGMENT_NONE, null);
		final Map<String, Object> globalGraphics = new HashMap<>();
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
			String file = filePath + messageResources.getMessage("observatory.graphic.accessibility.level.allocation.name") + ".jpg";
			getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, pageExecutionList, globalGraphics, "", file, noDataMess, regenerate);
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.name") + ".jpg";
			getGlobalMarkBySegmentGraphic(messageResources, executionId, pageExecutionList, globalGraphics, "", file, noDataMess, categories);
			file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + ".jpg";
			getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, globalGraphics, "", file, noDataMess, Constants.OBS_PRIORITY_1, regenerate);
			file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + ".jpg";
			getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, globalGraphics, "", file, noDataMess, Constants.OBS_PRIORITY_2, regenerate);
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.name") + ".jpg";
			getGlobalMarkBySegmentsGroupGraphic(messageResources, executionId, globalGraphics, file, noDataMess, pageExecutionList, categories, regenerate);
			file = filePath + messageResources.getMessage("observatory.graphic.aspect.mid.name") + ".jpg";
			getAspectMidsGraphic(messageResources, globalGraphics, file, noDataMess, pageExecutionList, color, "", regenerate);
			file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg";
			getMidsComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_1, "", file, noDataMess, pageExecutionList, color, regenerate);
			file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg";
			getMidsComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_2, "", file, noDataMess, pageExecutionList, color, regenerate);
		}
		return globalGraphics;
	}

	/**
	 * Generate category graphics.
	 *
	 * @param messageResources the message resources
	 * @param idExecution      the id execution
	 * @param category         the category
	 * @param filePath         the file path
	 * @param color            the color
	 * @param regenerate       the regenerate
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateCategoryGraphics(final MessageResources messageResources, final String idExecution, final CategoriaForm category, final String filePath,
			final String color, final boolean regenerate) throws Exception {
		final Map<String, Object> categoryGraphics = new HashMap<>();
		try {
			final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
			final List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(idExecution, Long.parseLong(category.getId()), null);
			if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
				String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.title", category.getName());
				String file = filePath + messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", category.getOrden()) + ".jpg";
				getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, regenerate);
				title = messageResources.getMessage("observatory.graphic.mark.allocation.segment.title", category.getName());
				file = filePath + messageResources.getMessage("observatory.graphic.mark.allocation.segment.name", category.getOrden()) + ".jpg";
				List<ObservatorySiteEvaluationForm> result = getSitesListByLevel(pageExecutionList);
				getMarkAllocationLevelSegmentGraphic(messageResources, title, file, noDataMess, result, false, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.aspect.mid.name") + category.getOrden() + ".jpg";
				title = messageResources.getMessage("observatory.graphic.segment.aspect.mid.title", category.getName());
				getAspectMidsGraphic(messageResources, categoryGraphics, file, noDataMess, pageExecutionList, color, title, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + category.getOrden() + ".jpg";
				getMidsComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_1, "", file, noDataMess, pageExecutionList, color, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + category.getOrden() + ".jpg";
				getMidsComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_2, "", file, noDataMess, pageExecutionList, color, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + category.getOrden() + ".jpg";
				getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, categoryGraphics, "", file, noDataMess, Constants.OBS_PRIORITY_1, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + category.getOrden() + ".jpg";
				getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, categoryGraphics, "", file, noDataMess, Constants.OBS_PRIORITY_2, regenerate);
			}
			return categoryGraphics;
		} catch (Exception e) {
			Logger.putLog(EXCEPTION, ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
			return categoryGraphics;
		}
	}

	/**
	 * Generate evolution graphics.
	 *
	 * @param messageResources the message resources
	 * @param observatoryId    the observatory id
	 * @param executionId      the execution id
	 * @param filePath         the file path
	 * @param color            the color
	 * @param regenerate       the regenerate
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateEvolutionGraphics(MessageResources messageResources, String observatoryId, final String executionId, String filePath, String color, boolean regenerate)
			throws Exception {
		final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = resultEvolutionData(Long.valueOf(observatoryId), Long.valueOf(executionId));
		final Map<String, Object> evolutionGraphics = new HashMap<>();
		if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
			if (pageObservatoryMap.size() != 1) {
				final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
				String title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.A.title");
				String file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_A, title, file, noDataMess, pageObservatoryMap, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.AA.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_AA, title, file, noDataMess, pageObservatoryMap, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.NV.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_NV, title, file, noDataMess, pageObservatoryMap, color, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg";
				getMidMarkEvolutionGraphic(messageResources, evolutionGraphics, noDataMess, file, pageObservatoryMap, color, regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_115_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_116_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_117_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_215_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_216_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_217_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<>();
				for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
					resultsByAspect.put(entry.getKey(), aspectMidsPuntuationGraphicData(messageResources, entry.getValue()));
				}
				getMidMarkAspectEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
			}
		}
		return evolutionGraphics;
	}

	/**
	 * Info global accessibility level.
	 *
	 * @param messageResources the message resources
	 * @param result           the result
	 * @return the list
	 */
	public static List<GraphicData> infoGlobalAccessibilityLevel(final MessageResources messageResources, final Map<String, Integer> result) {
		final List<GraphicData> labelValueList = new ArrayList<>();
		final int totalPort = result.get(Constants.OBS_A) + result.get(Constants.OBS_AA) + result.get(Constants.OBS_NV);
		labelValueList.add(infoGlobalAccessibilityLevelGraphicData(messageResources.getMessage("resultados.anonimos.porc.portales.aa"), result.get(Constants.OBS_AA), totalPort));
		labelValueList.add(infoGlobalAccessibilityLevelGraphicData(messageResources.getMessage("resultados.anonimos.porc.portales.a"), result.get(Constants.OBS_A), totalPort));
		labelValueList.add(infoGlobalAccessibilityLevelGraphicData(messageResources.getMessage("resultados.anonimos.porc.portales.parcial"), result.get(Constants.OBS_NV), totalPort));
		return labelValueList;
	}

	/**
	 * Info global accessibility level graphic data.
	 *
	 * @param title     the title
	 * @param result    the result
	 * @param totalPort the total port
	 * @return the graphic data
	 */
	private static GraphicData infoGlobalAccessibilityLevelGraphicData(final String title, final Integer result, final int totalPort) {
		final GraphicData labelValue = new GraphicData();
		labelValue.setAdecuationLevel(title);
		if (totalPort > 0) {
			labelValue.setPercentageP(String.valueOf(new BigDecimal(result).multiply(BIG_DECIMAL_HUNDRED).divide(new BigDecimal(totalPort), 2, BigDecimal.ROUND_HALF_UP)).replace(".00", "") + "%");
		}
		labelValue.setNumberP(String.valueOf(new BigDecimal(result)));
		return labelValue;
	}

	/**
	 * Info comparison by segment.
	 *
	 * @param messageResources the message resources
	 * @param category         the category
	 * @return the list
	 */
	public static List<LabelValueBean> infoComparisonBySegment(final MessageResources messageResources, final Map<String, BigDecimal> category) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.punt.portales.aa"), category.get(Constants.OBS_AA)));
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.punt.portales.a"), category.get(Constants.OBS_A)));
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.punt.portales.parcial"), category.get(Constants.OBS_NV)));
		return labelValueList;
	}

	/**
	 * Info comparison by segment puntuation.
	 *
	 * @param messageResources the message resources
	 * @param result           the result
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<LabelValueBean> infoComparisonBySegmentPuntuation(final MessageResources messageResources, final Map<String, BigDecimal> result) throws Exception {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(new LabelValueBean(messageResources.getMessage("resultados.anonimos.punt.portales.aa"), String.valueOf(result.get(Constants.OBS_AA)).replace(".00", "")));
		labelValueList.add(new LabelValueBean(messageResources.getMessage("resultados.anonimos.punt.portales.a"), String.valueOf(result.get(Constants.OBS_A)).replace(".00", "")));
		labelValueList.add(new LabelValueBean(messageResources.getMessage("resultados.anonimos.punt.portales.parcial"), String.valueOf(result.get(Constants.OBS_NV)).replace(".00", "")));
		return labelValueList;
	}

	/**
	 * Info aspect mids comparison.
	 *
	 * @param messageResources the message resources
	 * @param result           the result
	 * @return the list
	 */
	public static List<LabelValueBean> infoAspectMidsComparison(final MessageResources messageResources, final Map<String, BigDecimal> result) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.general"), result.get(messageResources.getMessage(OBSERVATORY_ASPECT_GENERAL))));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.presentacion"), result.get(messageResources.getMessage(OBSERVATORY_ASPECT_PRESENTATION))));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.estructura"), result.get(messageResources.getMessage(OBSERVATORY_ASPECT_STRUCTURE))));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.navegacion"), result.get(messageResources.getMessage(OBSERVATORY_ASPECT_NAVIGATION))));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.alternativa"), result.get(messageResources.getMessage(OBSERVATORY_ASPECT_ALTERNATIVES))));
		return labelValueList;
	}

	/**
	 * Devuelve para un bean formado por la pareja cadena:valor. Donde valor es una cadena que representa un número (la puntuación alcanzada) o las cadenas especiales para "no puntua" o no "hay
	 * resultado"
	 *
	 * @param messageResources the message resources
	 * @param label            the label
	 * @param result           the result
	 * @return the label value bean
	 */
	private static LabelValueBean createLabelValueScore(final MessageResources messageResources, final String label, final BigDecimal result) {
		final LabelValueBean labelValue = new LabelValueBean();
		labelValue.setLabel(label);
		if (result != null && result.compareTo(new BigDecimal(-1)) != 0) {
			labelValue.setValue(String.valueOf(result).replace(".00", ""));
		} else if (result == null) {
			labelValue.setValue(messageResources.getMessage("resultados.observatorio.vista.primaria.valor.noResultado"));
		} else {
			labelValue.setValue(messageResources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua"));
		}
		return labelValue;
	}

	/**
	 * Info mark allocation level segment.
	 *
	 * @param siteList the site list
	 * @return the list
	 */
	public static List<LabelValueBean> infoMarkAllocationLevelSegment(final List<ObservatorySiteEvaluationForm> siteList) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
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
	 * @param messageResources the message resources
	 * @param result           the result
	 * @return the list
	 */
	public static List<LabelValueBean> infoLevelIVerificationMidsComparison(final MessageResources messageResources, final Map<String, BigDecimal> result) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.1.1"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.1.2"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.1.3"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.1.4"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.1.5"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_115_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.1.6"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_116_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.1.7"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_117_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.2.1"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.2.2"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.1.2.3"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION)));
		return labelValueList;
	}

	/**
	 * Info level II verification mids comparison.
	 *
	 * @param messageResources the message resources
	 * @param result           the result
	 * @return the list
	 */
	public static List<LabelValueBean> infoLevelIIVerificationMidsComparison(final MessageResources messageResources, final Map<String, BigDecimal> result) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.1.1"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.1.2"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.1.3"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.1.4"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.1.5"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_215_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.1.6"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_216_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.1.7"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_217_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.2.1"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.2.2"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.2_0.subgroup.2.2.3"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION)));
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
	 * @param messageResources  the message resources
	 * @param globalGraphics    the global graphics
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
	public static void getMidsComparationByVerificationLevelGraphic(final MessageResources messageResources, Map<String, Object> globalGraphics, final String level, final String title,
			final String filePath, final String noDataMess, final List<ObservatoryEvaluationForm> pageExecutionList, final String color, final boolean regenerate) throws Exception {
		final File file = new File(filePath);
		final Map<String, BigDecimal> result = getVerificationResultsByPoint(pageExecutionList, level);
		// Incluimos los resultados en la request
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVI, infoLevelIVerificationMidsComparison(messageResources, result));
			infoLevelIVerificationMidsComparison(messageResources, result);
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMVII, infoLevelIIVerificationMidsComparison(messageResources, result));
			infoLevelIIVerificationMidsComparison(messageResources, result);
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBarChart(result, title, "", "", color, false, false, false, filePath, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the modality by verification level graphic.
	 *
	 * @param messageResources  the message resources
	 * @param pageExecutionList the page execution list
	 * @param globalGraphics    the global graphics
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param level             the level
	 * @param regenerate        the regenerate
	 * @return the modality by verification level graphic
	 * @throws Exception the exception
	 */
	public static void getModalityByVerificationLevelGraphic(final MessageResources messageResources, final List<ObservatoryEvaluationForm> pageExecutionList, Map<String, Object> globalGraphics,
			final String title, final String filePath, final String noDataMess, final String level, final boolean regenerate) throws Exception {
		final File file = new File(filePath);
		final Map<String, BigDecimal> results = getVerificationResultsByPointAndModality(pageExecutionList, level);
		final DefaultCategoryDataset dataSet = createStackedBarDataSetForModality(results, messageResources);
		// Incluimos los resultados en la request
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_I, infoLevelVerificationModalityComparison(results));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_MODALITY_VERIFICATION_II, infoLevelVerificationModalityComparison(results));
		}
		if (!file.exists() || regenerate) {
			final PropertiesManager pmgr = new PropertiesManager();
			final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.modality.colors"));
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
	 * @param messageResources  the message resources
	 * @param executionId       the execution id
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @param regenerate        the regenerate
	 * @return the global mark by segments group graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalMarkBySegmentsGroupGraphic(final MessageResources messageResources, final String executionId, Map<String, Object> globalGraphics, final String filePath,
			final String noDataMess, final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories, final boolean regenerate) throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = createGraphicsMap(categories);
		final List<CategoryViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageResultsBySegmentMap(executionId, pageExecutionList, resultLists.get(i));
			final DefaultCategoryDataset dataSet = createDataSet(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			}
			// Incluimos los resultados en la request
			for (CategoriaForm category : resultLists.get(i)) {
				final CategoryViewListForm categoryView = new CategoryViewListForm(category, infoComparisonBySegmentPuntuation(messageResources, resultsBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CPS_GRAPH, resultLists.size());
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
			dataSet.addValue(entry.getValue().get(Constants.OBS_NV), parseLevelLabel(Constants.OBS_NV, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_A), parseLevelLabel(Constants.OBS_A, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_AA), parseLevelLabel(Constants.OBS_AA, messageResources), entry.getKey().getName());
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
	 * @param messageResources  the message resources
	 * @param evolutionGraphics the evolution graphics
	 * @param aspect            the aspect
	 * @param noDataMess        the no data mess
	 * @param filePath          the file path
	 * @param resultsByAspect   the results by aspect
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the mid mark aspect evolution graphic
	 * @throws Exception the exception
	 */
	public static void getMidMarkAspectEvolutionGraphic(final MessageResources messageResources, Map<String, Object> evolutionGraphics, final String aspect, final String noDataMess,
			final String filePath, final Map<Date, Map<String, BigDecimal>> resultsByAspect, final String color, final boolean regenerate) throws Exception {
		final String fileName = filePath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", aspect) + ".jpg";
		final File file = new File(fileName);
		final String aspectStr;
		if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID)) {
			aspectStr = messageResources.getMessage(OBSERVATORY_ASPECT_GENERAL);
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID)) {
			aspectStr = messageResources.getMessage(OBSERVATORY_ASPECT_ALTERNATIVES);
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID)) {
			aspectStr = messageResources.getMessage(OBSERVATORY_ASPECT_STRUCTURE);
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID)) {
			aspectStr = messageResources.getMessage(OBSERVATORY_ASPECT_NAVIGATION);
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID)) {
			aspectStr = messageResources.getMessage(OBSERVATORY_ASPECT_PRESENTATION);
		} else {
			aspectStr = "";
		}
		// Recuperamos los resultados
		final Map<String, BigDecimal> resultData = calculateAspectEvolutionPuntuationDataSet(aspectStr, resultsByAspect);
		// Incluimos los resultados en la request
		if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AG, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AAL, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AE, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AN, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AP, infoMidMarkAspectEvolutionGraphic(messageResources, resultData));
		}
		infoMidMarkAspectEvolutionGraphic(messageResources, resultData);
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBarChart(resultData, "", "", "", color, false, false, true, fileName, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the mid mark verification evolution graphic.
	 *
	 * @param messageResources  the message resources
	 * @param evolutionGraphics the evolution graphics
	 * @param verification      the verification
	 * @param noDataMess        the no data mess
	 * @param filePath          the file path
	 * @param result            the result
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the mid mark verification evolution graphic
	 * @throws Exception the exception
	 */
	public static void getMidMarkVerificationEvolutionGraphic(final MessageResources messageResources, Map<String, Object> evolutionGraphics, final String verification, final String noDataMess,
			final String filePath, final Map<Date, List<ObservatoryEvaluationForm>> result, final String color, final boolean regenerate) throws Exception {
		final String fileName = filePath + File.separator + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", verification) + ".jpg";
		final File file = new File(fileName);
		// Recuperamos los resultados
		final Map<String, BigDecimal> resultData = calculateVerificationEvolutionPuntuationDataSet(verification, result);
		// Los incluimos en la request
		if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_111_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_112_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_113_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_114_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_115_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V115, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_116_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V116, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_117_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V117, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_121_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_122_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_123_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_211_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_212_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_213_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_214_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_215_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V215, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_216_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V216, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_217_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V217, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_221_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_222_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_223_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V223, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_224_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V224, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_225_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V225, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_226_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V226, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		}
		infoMidMarkVerificationEvolutionGraphic(messageResources, resultData);
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBarChart(resultData, "", "", "", color, false, false, true, fileName, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the mid mark evolution graphic.
	 *
	 * @param messageResources  the message resources
	 * @param evolutionGraphics the evolution graphics
	 * @param noDataMess        the no data mess
	 * @param filePath          the file path
	 * @param observatoryResult the observatory result
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the mid mark evolution graphic
	 * @throws Exception the exception
	 */
	public static void getMidMarkEvolutionGraphic(final MessageResources messageResources, Map<String, Object> evolutionGraphics, final String noDataMess, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> observatoryResult, final String color, final boolean regenerate) throws Exception {
		// Recuperamos los resultados
		final Map<String, BigDecimal> resultData = calculateEvolutionPuntuationDataSet(observatoryResult);
		// Los incluimos en la request
		evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_MID_PUNT, infoMidMarkEvolutionGraphic(resultData));
		infoMidMarkEvolutionGraphic(resultData);
		final File file = new File(filePath);
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBarChart(resultData, "", "", "", color, false, false, true, filePath, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the approval level evolution graphic.
	 *
	 * @param messageResources  the message resources
	 * @param observatoryId     the observatory id
	 * @param executionId       the execution id
	 * @param evolutionGraphics the evolution graphics
	 * @param suitabilityLevel  the suitability level
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param observatoryResult the observatory result
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the approval level evolution graphic
	 * @throws Exception the exception
	 */
	public static void getApprovalLevelEvolutionGraphic(final MessageResources messageResources, final String observatoryId, final String executionId, Map<String, Object> evolutionGraphics,
			final String suitabilityLevel, final String title, final String filePath, final String noDataMess, final Map<Date, List<ObservatoryEvaluationForm>> observatoryResult, final String color,
			final boolean regenerate) throws Exception {
		final File file = new File(filePath);
		final Map<Date, Map<Long, Map<String, Integer>>> result = getEvolutionObservatoriesSitesByType(observatoryId, executionId, observatoryResult);
		final Map<String, BigDecimal> resultData = calculatePercentageApprovalSiteLevel(result, suitabilityLevel);
		// Los incluimos en la request
		if (suitabilityLevel.equals(Constants.OBS_A)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_A, infoLevelEvolutionGraphic(resultData));
			infoLevelEvolutionGraphic(resultData);
		} else if (suitabilityLevel.equals(Constants.OBS_AA)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_AA, infoLevelEvolutionGraphic(resultData));
			infoLevelEvolutionGraphic(resultData);
		}
		if (suitabilityLevel.equals(Constants.OBS_NV) || suitabilityLevel.equals(Constants.OBS_PARCIAL)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_NV, infoLevelEvolutionGraphic(resultData));
			infoLevelEvolutionGraphic(resultData);
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBarChart(resultData, "", "", "", color, false, true, true, filePath, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Gets the mark allocation level segment graphic.
	 *
	 * @param messageResources  the message resources
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param siteExecutionList the site execution list
	 * @param showColLab        the show col lab
	 * @param regenerate        the regenerate
	 * @return the mark allocation level segment graphic
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getMarkAllocationLevelSegmentGraphic(final MessageResources messageResources, final String title, final String filePath, final String noDataMess,
			final List<ObservatorySiteEvaluationForm> siteExecutionList, final boolean showColLab, final boolean regenerate) throws IOException {
		final File file = new File(filePath);
		final List<ObservatorySiteEvaluationForm> result2 = createOrderFormLevel(siteExecutionList);
		// Los incluimos en la request
		if (showColLab) {
			// request.setAttribute(Constants.OBSERVATORY_GRAPHIC_SEGMENT_DATA_LIST_DP,
			// infoMarkAllocationLevelSegment(result2));
			infoMarkAllocationLevelSegment(result2);
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBar1PxChart(result2, "", "", "", filePath, noDataMess, messageResources, x, y, showColLab);
		}
	}

	/**
	 * Gets the aspect mids graphic.
	 *
	 * @param messageResources  the message resources
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param color             the color
	 * @param title             the title
	 * @param regenerate        the regenerate
	 * @return the aspect mids graphic
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getAspectMidsGraphic(final MessageResources messageResources, Map<String, Object> globalGraphics, final String filePath, final String noDataMess,
			final List<ObservatoryEvaluationForm> pageExecutionList, final String color, final String title, final boolean regenerate) throws IOException {
		final Map<String, BigDecimal> result = aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
		// Los incluimos en la request
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMA, infoAspectMidsComparison(messageResources, result));
		infoAspectMidsComparison(messageResources, result);
		// Si no existe la grafica, la creamos
		final File file = new File(filePath);
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBarChart(result, "", "", "", color, false, false, false, filePath, noDataMess, messageResources, x, y);
		}
	}

	/**
	 * Info mid mark verification evolution graphic.
	 *
	 * @param messageResources the message resources
	 * @param resultData       the result data
	 * @return the list
	 */
	public static List<LabelValueBean> infoMidMarkVerificationEvolutionGraphic(final MessageResources messageResources, final Map<String, BigDecimal> resultData) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
			labelValueList.add(createLabelValueScore(messageResources, entry.getKey(), entry.getValue()));
		}
		return labelValueList;
	}

	/**
	 * Info mid mark aspect evolution graphic.
	 *
	 * @param messageResources the message resources
	 * @param resultData       the result data
	 * @return the list
	 */
	public static List<LabelValueBean> infoMidMarkAspectEvolutionGraphic(final MessageResources messageResources, final Map<String, BigDecimal> resultData) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
			labelValueList.add(createLabelValueScore(messageResources, entry.getKey(), entry.getValue()));
		}
		return labelValueList;
	}

	/**
	 * Gets the global result data.
	 *
	 * @param executionId       the execution id
	 * @param categoryId        the category id
	 * @param pageExecutionList the page execution list
	 * @return the global result data
	 * @throws Exception the exception
	 */
	public static List<ObservatoryEvaluationForm> getGlobalResultData(final String executionId, final long categoryId, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
		return getGlobalResultData(executionId, categoryId, pageExecutionList, null);
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
	public static List<ObservatoryEvaluationForm> getGlobalResultData(final String executionId, final long categoryId, final List<ObservatoryEvaluationForm> pageExecutionList, final Long idCrawler)
			throws Exception {
		List<ObservatoryEvaluationForm> observatoryEvaluationList;
		try {
			observatoryEvaluationList = (List<ObservatoryEvaluationForm>) CacheUtils.getFromCache(Constants.OBSERVATORY_KEY_CACHE + executionId);
		} catch (NeedsRefreshException nre) {
			Logger.putLog("La cache con id " + Constants.OBSERVATORY_KEY_CACHE + executionId + " no está disponible, se va a regenerar", ResultadosAnonimosObservatorioUNE2012Utils.class,
					Logger.LOG_LEVEL_WARNING);
			try (Connection c = DataBaseManager.getConnection()) {
				observatoryEvaluationList = new ArrayList<>();
				final List<Long> listAnalysis = new ArrayList<>();
				List<Long> listExecutionsIds = new ArrayList<>();
				if (idCrawler == null) {
					listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(c, Long.parseLong(executionId), Constants.COMPLEXITY_SEGMENT_NONE);
				} else {
					listExecutionsIds.add(idCrawler);
				}
				if (pageExecutionList == null) {
					for (Long idExecution : listExecutionsIds) {
						listAnalysis.addAll(AnalisisDatos.getAnalysisIdsByTracking(c, idExecution));
					}
					// Inicializamos el evaluador
					if (!EvaluatorUtility.isInitialized()) {
						EvaluatorUtility.initialize();
					}
					final Evaluator evaluator = new Evaluator();
					for (Long idAnalysis : listAnalysis) {
						final Evaluation evaluation = evaluator.getObservatoryAnalisisDB(c, idAnalysis, EvaluatorUtils.getDocList());
						final String methodology = ObservatorioDAO.getMethodology(c, Long.parseLong(executionId));
						final ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false, false);
						evaluationForm.setObservatoryExecutionId(Long.parseLong(executionId));
						final FulfilledCrawlingForm ffCrawling = RastreoDAO.getFullfilledCrawlingExecution(c, evaluationForm.getCrawlerExecutionId());
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
					}
				} else {
					for (ObservatoryEvaluationForm observatory : pageExecutionList) {
						if (listExecutionsIds.contains(observatory.getCrawlerExecutionId())) {
							observatoryEvaluationList.add(observatory);
						}
					}
				}
			} catch (SQLException e) {
				Logger.putLog("Error en getGlobalResultData", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			CacheUtils.putInCacheForever(observatoryEvaluationList, Constants.OBSERVATORY_KEY_CACHE + executionId);
		}
		return filterObservatoriesByComplexity(observatoryEvaluationList, Long.parseLong(executionId), categoryId);
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
	private static List<ObservatoryEvaluationForm> filterObservatoriesByComplexity(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId, final long categoryId)
			throws Exception {
		if (categoryId == Constants.COMPLEXITY_SEGMENT_NONE) {
			return observatoryEvaluationList;
		} else {
			final List<ObservatoryEvaluationForm> results = new ArrayList<>();
			try (Connection conn = DataBaseManager.getConnection()) {
				final List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, executionId, categoryId);
				for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
					if (listExecutionsIds.contains(observatoryEvaluationForm.getCrawlerExecutionId())) {
						results.add(observatoryEvaluationForm);
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al filtrar observatorios. ", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			return results;
		}
	}

	/**
	 * Calculate aspect evolution puntuation data set.
	 *
	 * @param aspect          the aspect
	 * @param resultsByAspect the results by aspect
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateAspectEvolutionPuntuationDataSet(final String aspect, final Map<Date, Map<String, BigDecimal>> resultsByAspect) {
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		return calculateAspectEvolutionPuntuationDataSet(aspect, resultsByAspect, df);
	}

	/**
	 * Calculate aspect evolution puntuation data set.
	 *
	 * @param aspect          the aspect
	 * @param resultsByAspect the results by aspect
	 * @param dateFormat      the date format
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateAspectEvolutionPuntuationDataSet(final String aspect, final Map<Date, Map<String, BigDecimal>> resultsByAspect, final DateFormat dateFormat) {
		final Map<String, BigDecimal> resultData = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				final PropertiesManager pmgr = new PropertiesManager();
				final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
				try {
					final Date fecha1 = new Date(df.parse(o1).getTime());
					final Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución.", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		for (Map.Entry<Date, Map<String, BigDecimal>> entry : resultsByAspect.entrySet()) {
			resultData.put(dateFormat.format(entry.getKey()), entry.getValue().get(aspect));
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
	public static Map<String, BigDecimal> calculateVerificationEvolutionPuntuationDataSet(final String verification, final Map<Date, List<ObservatoryEvaluationForm>> result) {
		final TreeMap<String, BigDecimal> resultData = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				final PropertiesManager pmgr = new PropertiesManager();
				final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
				try {
					final Date fecha1 = new Date(df.parse(o1).getTime());
					final Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución.", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : result.entrySet()) {
			// Para un observatorio en concreto recuperamos la puntuación de una
			// verificación
			final BigDecimal value = getVerificationResultsByPoint(entry.getValue(), Constants.OBS_PRIORITY_NONE).get(verification);
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
	public static Map<String, BigDecimal> calculateEvolutionPuntuationDataSet(final Map<Date, List<ObservatoryEvaluationForm>> result) {
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		return calculateEvolutionPuntuationDataSet(result, df);
	}

	/**
	 * Calculate evolution puntuation data set.
	 *
	 * @param result the result
	 * @param df     the df
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateEvolutionPuntuationDataSet(final Map<Date, List<ObservatoryEvaluationForm>> result, final DateFormat df) {
		final TreeMap<String, BigDecimal> resultData = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				try {
					final Date fecha1 = new Date(df.parse(o1).getTime());
					final Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : result.entrySet()) {
			BigDecimal value = BigDecimal.ZERO;
			if ((entry.getValue()) != null && !(entry.getValue()).isEmpty()) {
				for (ObservatoryEvaluationForm observatoryEvaluationForm : entry.getValue()) {
					value = value.add(observatoryEvaluationForm.getScore());
				}
				final int numPages = entry.getValue().size();
				value = value.divide(new BigDecimal(numPages), 2, BigDecimal.ROUND_HALF_UP);
			}
			resultData.put(df.format(entry.getKey()), value);
		}
		return resultData;
	}

	/**
	 * Calculate percentage approval site level.
	 *
	 * @param result           the result
	 * @param suitabilityLevel the suitability level
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculatePercentageApprovalSiteLevel(final Map<Date, Map<Long, Map<String, Integer>>> result, final String suitabilityLevel) {
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
		return calculatePercentageApprovalSiteLevel(result, suitabilityLevel, df);
	}

	/**
	 * Calculate percentage approval site level.
	 *
	 * @param result           the result
	 * @param suitabilityLevel the suitability level
	 * @param dateFormat       the date format
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculatePercentageApprovalSiteLevel(final Map<Date, Map<Long, Map<String, Integer>>> result, final String suitabilityLevel, final DateFormat dateFormat) {
		final TreeMap<String, BigDecimal> percentagesMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				try {
					final Date fecha1 = new Date(dateFormat.parse(o1).getTime());
					final Date fecha2 = new Date(dateFormat.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		for (Map.Entry<Date, Map<Long, Map<String, Integer>>> dateMapEntry : result.entrySet()) {
			int numSitesType = 0;
			for (Map.Entry<Long, Map<String, Integer>> longMapEntry : dateMapEntry.getValue().entrySet()) {
				final String portalLevel = siteLevel(longMapEntry.getValue());
				if (portalLevel.equals(suitabilityLevel)) {
					numSitesType++;
				}
			}
			BigDecimal percentage = BigDecimal.ZERO;
			if (numSitesType != 0) {
				final int numSites = dateMapEntry.getValue().size();
				percentage = (new BigDecimal(numSitesType)).divide(new BigDecimal(numSites), 2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_HUNDRED);
			}
			percentagesMap.put(dateFormat.format(dateMapEntry.getKey()), percentage);
		}
		return percentagesMap;
	}
//    public static Map<Date, Map<Long, Map<String, Integer>>> getEvolutionObservatoriesSitesByType(final String observatoryId, final String executionId, final Map<Date, List<ObservatoryEvaluationForm>> result) {
////        final String observatoryId = request.getParameter(Constants.ID_OBSERVATORIO);
////        final String executionId = request.getParameter(Constants.ID);
//        return getEvolutionObservatoriesSitesByType(observatoryId, executionId, result);
//    }

	/**
	 * Gets the evolution observatories sites by type.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param result        the result
	 * @return the evolution observatories sites by type
	 */
	public static Map<Date, Map<Long, Map<String, Integer>>> getEvolutionObservatoriesSitesByType(final String observatoryId, final String executionId,
			final Map<Date, List<ObservatoryEvaluationForm>> result) {
		final Map<Date, Map<Long, Map<String, Integer>>> resultData = new HashMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, Long.parseLong(observatoryId));
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, Long.parseLong(observatoryId), Long.parseLong(executionId),
					observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> longDateEntry : executedObservatoryIdMap.entrySet()) {
				final List<ObservatoryEvaluationForm> pageList = result.get(longDateEntry.getValue());
				final Map<Long, Map<String, Integer>> sites = getSitesByType(pageList);
				resultData.put(longDateEntry.getValue(), sites);
			}
		} catch (Exception e) {
			Logger.putLog(EXCEPTION, ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
	public static Map<Date, List<ObservatoryEvaluationForm>> resultEvolutionData(final Long observatoryId, final Long executionId) {
		final Map<Date, List<ObservatoryEvaluationForm>> resultData = new TreeMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, observatoryId);
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatoryId, executionId, observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> entry : executedObservatoryIdMap.entrySet()) {
				final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE, null);
				resultData.put(entry.getValue(), pageList);
			}
		} catch (Exception e) {
			Logger.putLog(EXCEPTION, ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return resultData;
	}

	/**
	 * Calculate percentage.
	 *
	 * @param values the values
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculatePercentage(final Map<String, Integer> values) {
		int total = 0;
		final Map<String, BigDecimal> result = new HashMap<>();
		for (Map.Entry<String, Integer> stringIntegerEntry : values.entrySet()) {
			total += stringIntegerEntry.getValue();
		}
		BigDecimal totalPercentage = BigDecimal.ZERO;
		String fitResultKey = "";
		for (Map.Entry<String, Integer> entry : values.entrySet()) {
			if (total != 0) {
				result.put(entry.getKey(), new BigDecimal(entry.getValue()).divide(new BigDecimal(total), 2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_HUNDRED));
			} else {
				result.put(entry.getKey(), BigDecimal.ZERO);
			}
			totalPercentage = totalPercentage.add(result.get(entry.getKey()));
			fitResultKey = entry.getKey();
		}
		// ajustamos el resultado por si se pasa de 100 a causa del redondeo
		if (totalPercentage.compareTo(BIG_DECIMAL_HUNDRED) != 0) {
			result.put(fitResultKey, result.get(fitResultKey).subtract(totalPercentage.subtract(BIG_DECIMAL_HUNDRED)));
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
			final Map<Long, ObservatorySiteEvaluationForm> siteMap = new HashMap<>();
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
				final ObservatorySiteEvaluationForm observatorySite = siteEntry.getValue();
				observatorySite.setScore(observatorySite.getScore().divide(new BigDecimal(observatorySite.getPages().size()), 2, BigDecimal.ROUND_HALF_UP));
				observatorySite.setLevel(siteLevel(getSitesByType(observatorySite.getPages()).get(siteEntry.getKey())));
				observatorySite.setName(siteEntry.getValue().getName());
				siteList.add(siteEntry.getValue());
			}
		} catch (Exception e) {
			Logger.putLog(EXCEPTION, ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return siteList;
	}

	/**
	 * Site level.
	 *
	 * @param pageType the page type
	 * @return the string
	 */
	public static String siteLevel(final Map<String, Integer> pageType) {
		// final Map<String, Integer> pageType = portalInformation.get(idSite);
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
	 * @param messageResources  the message resources
	 * @param pageExecutionList the page execution list
	 * @param graphics          the graphics
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param regenerate        the regenerate
	 * @return the global accessibility level allocation segment graphic
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getGlobalAccessibilityLevelAllocationSegmentGraphic(final MessageResources messageResources, final List<ObservatoryEvaluationForm> pageExecutionList,
			final Map<String, Object> graphics, final String title, final String filePath, final String noDataMess, final boolean regenerate) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final File file = new File(filePath);
		final Map<String, Integer> result = getResultsBySiteLevel(pageExecutionList);
		if (!file.exists() || regenerate) {
			final int total = result.get(Constants.OBS_A) + result.get(Constants.OBS_AA) + result.get(Constants.OBS_NV);
			final DefaultPieDataset dataSet = new DefaultPieDataset();
			dataSet.setValue(parseLevelLabel(Constants.OBS_PARCIAL, messageResources), result.get(Constants.OBS_NV));
			dataSet.setValue(parseLevelLabel(Constants.OBS_A, messageResources), result.get(Constants.OBS_A));
			dataSet.setValue(parseLevelLabel(Constants.OBS_AA, messageResources), result.get(Constants.OBS_AA));
			GraphicsUtils.createPieChart(dataSet, "", messageResources.getMessage("observatory.graphic.site.number"), total, filePath, noDataMess,
					pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), x, y);
		}
		graphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG, infoGlobalAccessibilityLevel(messageResources, result));
		infoGlobalAccessibilityLevel(messageResources, result);
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
			final List<CategoriaForm> list;
			if (resultLists.get(keyMap) != null) {
				list = resultLists.get(keyMap);
			} else {
				list = new ArrayList<>();
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
	 * @param messageResources  the message resources
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param globalGraphics    the global graphics
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param categories        the categories
	 * @return the global mark by segment graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalMarkBySegmentGraphic(final MessageResources messageResources, final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			Map<String, Object> globalGraphics, final String title, final String filePath, final String noDataMess, final List<CategoriaForm> categories) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		final Map<Integer, List<CategoriaForm>> resultLists = createGraphicsMap(categories);
		final List<CategoryViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<CategoriaForm, Map<String, BigDecimal>> resultDataBySegment = calculateMidPuntuationResultsBySegmentMap(executionId, pageExecutionList, resultLists.get(i));
			if (!file.exists()) {
				final ChartForm observatoryGraphicsForm = new ChartForm(createDataSet(resultDataBySegment, messageResources), true, true, false, false, true, false, false, x, y,
						pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				GraphicsUtils.createSeriesBarChart(observatoryGraphicsForm, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg", noDataMess, messageResources, true);
			}
			for (CategoriaForm category : resultLists.get(i)) {
				final CategoryViewListForm categoryView = new CategoryViewListForm(category, infoComparisonBySegment(messageResources, resultDataBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CAS_GRAPH, resultLists.size());
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
		for (Map.Entry<Long, Map<String, Integer>> longMapEntry : globalResultBySiteType.entrySet()) {
			final Map<String, Integer> pageType = longMapEntry.getValue();
			final Integer numPages = pageType.get(Constants.OBS_A) + pageType.get(Constants.OBS_AA) + pageType.get(Constants.OBS_NV);
			final BigDecimal value = new BigDecimal(pageType.get(Constants.OBS_A)).multiply(new BigDecimal(5)).add(new BigDecimal(pageType.get(Constants.OBS_AA)).multiply(BigDecimal.TEN))
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
	 * @param messageResources the message resources
	 * @param resultData       the result data
	 * @return the map
	 */
	public static Map<String, BigDecimal> aspectMidsPuntuationGraphicData(final MessageResources messageResources, final List<ObservatoryEvaluationForm> resultData) {
		final Map<String, List<LabelValueBean>> globalResult = new HashMap<>();
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
		final Map<String, BigDecimal> results = new HashMap<>();
		for (Map.Entry<String, List<LabelValueBean>> globalResultEntry : globalResult.entrySet()) {
			final String aspect = messageResources.getMessage(globalResultEntry.getKey());
			// Recorremos las verificaciones de cada aspecto
			final Map<String, List<BigDecimal>> partialResultsMap = new HashMap<>();
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
			final Map<String, BigDecimal> verificationsMap = new HashMap<>();
			for (Map.Entry<String, List<BigDecimal>> stringListEntry : partialResultsMap.entrySet()) {
				final List<BigDecimal> verificationsList = stringListEntry.getValue();
				final String verificationKey = stringListEntry.getKey();
				if (!verificationsList.isEmpty()) {
					BigDecimal suma = BigDecimal.ZERO;
					for (BigDecimal value : verificationsList) {
						suma = suma.add(value);
					}
					final BigDecimal media = suma.divide(new BigDecimal(verificationsList.size()), 2, BigDecimal.ROUND_HALF_UP);
					verificationsMap.put(verificationKey, media);
				}
			}
			// Teniendo las medias de verificaciones de un aspecto, pasamos a calcular el
			// valor medio de ese aspecto
			if (!results.containsKey(aspect)) {
				results.put(aspect, BigDecimal.ZERO);
			}
			for (Map.Entry<String, BigDecimal> stringBigDecimalEntry : verificationsMap.entrySet()) {
				results.put(aspect, results.get(aspect).add(stringBigDecimalEntry.getValue()));
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
	public static Map<String, BigDecimal> barGraphicFromMidPuntuationSegmentData(final List<ObservatorySiteEvaluationForm> categoryList) {
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
	public static Map<String, BigDecimal> getVerificationResultsByPoint(final List<ObservatoryEvaluationForm> resultData, final String level) {
		final Map<String, Integer> results = new TreeMap<>();
		final Map<String, Integer> numPoint = new LinkedHashMap<>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
			for (ObservatoryLevelForm observatoryLevelForm : observatoryEvaluationForm.getGroups()) {
				if (level.equals(Constants.OBS_PRIORITY_NONE) || observatoryLevelForm.getName().equals(level)) {
					for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
						for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
							// Se comprueba si puntúa o no puntúa
							if (observatorySubgroupForm.getValue() != Constants.OBS_VALUE_NOT_SCORE) {
								if (results.get(observatorySubgroupForm.getDescription()) == null) {
									results.put(observatorySubgroupForm.getDescription(), 0);
									numPoint.put(observatorySubgroupForm.getDescription(), 0);
								}
								// Si puntúa, se isNombreValido si se le da un 0 o un 1
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
									// Si le damos un 1, lo añadimos a la puntuación e incrementamos el número de
									// puntos que han puntuado
									results.put(observatorySubgroupForm.getDescription(), results.get(observatorySubgroupForm.getDescription()) + 1);
									if (numPoint.get(observatorySubgroupForm.getDescription()) == -1) {
										numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 2);
									} else {
										numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
									}
								} else {
									// Si le damos un 0 solamente incrementamos el número de puntos
									numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
								}
							} else if (results.get(observatorySubgroupForm.getDescription()) == null) {
								results.put(observatorySubgroupForm.getDescription(), 0);
								numPoint.put(observatorySubgroupForm.getDescription(), -1);
							}
						}
					}
				}
			}
		}
		// Cambiamos las claves por el nombre y calculamos la media
		final Map<String, BigDecimal> verificationResultsByPoint = new TreeMap<>();
		for (Map.Entry<String, Integer> resultEntry : results.entrySet()) {
			final String name = resultEntry.getKey().substring(resultEntry.getKey().length() - 5);
			final BigDecimal value;
			if (numPoint.get(resultEntry.getKey()) != -1 && numPoint.get(resultEntry.getKey()) != 0) {
				value = BigDecimal.valueOf(resultEntry.getValue()).divide(BigDecimal.valueOf(numPoint.get(resultEntry.getKey())), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN);
			} else if (numPoint.get(resultEntry.getKey()) == -1) {
				value = BigDecimal.valueOf(-1);
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
			results.put(stringBigDecimalEntry.getKey(), stringBigDecimalEntry.getValue().divide(new BigDecimal(resultData.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_HUNDRED));
		}
		return results;
	}

	/**
	 * Creates the order form level.
	 *
	 * @param results the results
	 * @return the list
	 */
	public static List<ObservatorySiteEvaluationForm> createOrderFormLevel(final List<ObservatorySiteEvaluationForm> results) {
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
	public static Map<String, List<ObservatoryEvaluationForm>> getPagesByType(final List<ObservatoryEvaluationForm> observatoryEvaluationList) {
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
	public static Map<Long, Map<String, Integer>> getSitesByType(final List<ObservatoryEvaluationForm> observatoryEvaluationList) {
		final Map<String, List<ObservatoryEvaluationForm>> pagesByType = getPagesByType(observatoryEvaluationList);
		final Map<Long, Map<String, Integer>> sitesByType = new HashMap<>();
		for (String key : pagesByType.keySet()) {
			for (ObservatoryEvaluationForm observatoryEvaluationForm : pagesByType.get(key)) {
				if (sitesByType.get(observatoryEvaluationForm.getCrawlerExecutionId()) != null) {
					final Map<String, Integer> value = sitesByType.get(observatoryEvaluationForm.getCrawlerExecutionId());
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
	public static Map<CategoriaForm, Map<String, BigDecimal>> calculatePercentageResultsBySegmentMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<CategoriaForm> categories) throws Exception {
		final Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<CategoriaForm>() {
			@Override
			public int compare(CategoriaForm o1, CategoriaForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (CategoriaForm category : categories) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList);
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
	public static Map<CategoriaForm, Map<String, BigDecimal>> calculateMidPuntuationResultsBySegmentMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<CategoriaForm> categories) throws Exception {
		try {
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
			Logger.putLog("Error al recuperar datos de la BBDD.", ResultadosAnonimosObservatorioUNE2012Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Generate evolution suitability chart.
	 *
	 * @param observatoryId      the observatory id
	 * @param executionId        the execution id
	 * @param filePath           the file path
	 * @param pageObservatoryMap the page observatory map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionSuitabilityChart(final String observatoryId, final String executionId, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws IOException {
		final Map<String, Map<String, BigDecimal>> evolutionSuitabilityDatePercentMap = new LinkedHashMap<>();
		final Map<Date, Map<Long, Map<String, Integer>>> result = getEvolutionObservatoriesSitesByType(observatoryId, executionId, pageObservatoryMap);
		evolutionSuitabilityDatePercentMap.put("Parcial", calculatePercentageApprovalSiteLevel(result, Constants.OBS_NV));
		evolutionSuitabilityDatePercentMap.put("Prioridad 1", calculatePercentageApprovalSiteLevel(result, Constants.OBS_A));
		evolutionSuitabilityDatePercentMap.put("Prioridad 1 y 2", calculatePercentageApprovalSiteLevel(result, Constants.OBS_AA));
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Map<String, BigDecimal>> evolutionSuitabilityEntry : evolutionSuitabilityDatePercentMap.entrySet()) {
			for (Map.Entry<String, BigDecimal> datePercentEntry : evolutionSuitabilityEntry.getValue().entrySet()) {
				dataSet.addValue(datePercentEntry.getValue(), evolutionSuitabilityEntry.getKey(), datePercentEntry.getKey());
			}
		}
		final String noDataMess = "noData";
		final PropertiesManager pmgr = new PropertiesManager();
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
	}

	/**
	 * Generate the evolution chart of the average score grouped by verification. This is a landscape chart where for each verification there is a column with the score achieved in each observatory
	 * execution.
	 *
	 * @param messageResources   MessageResources needed for GraphicsUtils utility class
	 * @param filePath           String with the full path (filename included) where the chart will be saved as jpg file.
	 * @param pageObservatoryMap a Map where each entrey is keyed by observatory date and the value is a list of the page evaluations
	 * @param verifications      list of verifications to include on the chart
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionAverageScoreByVerificationChart(final MessageResources messageResources, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, final List<String> verifications) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			final Map<String, BigDecimal> resultsByVerification = getVerificationResultsByPoint(entry.getValue(), Constants.OBS_PRIORITY_NONE);
			for (String verification : verifications) {
				// Para un observatorio en concreto recuperamos la puntuación de una
				// verificación
				final BigDecimal value = resultsByVerification.get(verification);
				dataSet.addValue(value, entry.getKey().getTime(), verification);
			}
		}
		final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm.setFixedColorBars(true);
		chartForm.setShowColumsLabels(false);
		GraphicsUtils.createStandardBarChart(chartForm, filePath, "", messageResources, true);
	}

	/**
	 * Generate the evolution char of the average score grouped by aspects. This is a landscape chart where for each aspect there is a column with the score achieved in each observatory execution.
	 *
	 * @param messageResources   MessageResources needed for GraphicsUtils utility class
	 * @param filePath           String with the full path (filename included) where the chart will be saved as jpg file.
	 * @param pageObservatoryMap a Map where each entrey is keyed by observatory date and the value is a list of the page evaluations
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionAverageScoreByAspectChart(final MessageResources messageResources, final String filePath, final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap)
			throws IOException {
		final Map<Date, Map<String, BigDecimal>> resultsByAspect = new LinkedHashMap<>();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioUNE2012Utils.aspectMidsPuntuationGraphicData(messageResources, entry.getValue()));
		}
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<Date, Map<String, BigDecimal>> dateAspectValueEntry : resultsByAspect.entrySet()) {
			// Todas las puntuaciones por aspecto se muestran en este orden
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage(OBSERVATORY_ASPECT_GENERAL)), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage(OBSERVATORY_ASPECT_GENERAL));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage(OBSERVATORY_ASPECT_PRESENTATION)), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage(OBSERVATORY_ASPECT_PRESENTATION));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage(OBSERVATORY_ASPECT_STRUCTURE)), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage(OBSERVATORY_ASPECT_STRUCTURE));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage(OBSERVATORY_ASPECT_NAVIGATION)), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage(OBSERVATORY_ASPECT_NAVIGATION));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage(OBSERVATORY_ASPECT_ALTERNATIVES)), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage(OBSERVATORY_ASPECT_ALTERNATIVES));
		}
		final PropertiesManager pmgr = new PropertiesManager();
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, false, false, false, false, 1565, 684, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm.setFixedColorBars(true);
		chartForm.setShowColumsLabels(false);
		GraphicsUtils.createStandardBarChart(chartForm, filePath, "", messageResources, true);
	}
}