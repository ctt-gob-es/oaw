/*******************************************************************************
* Copyright (C) 2019 MPTFP, Ministerio de Política Territorial y Función Pública, 
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.dao.EstadoObservatorioDAO;
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
import es.inteco.rastreador2.actionform.observatorio.ComplianceComparisonForm;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.view.forms.CategoryViewListForm;
import es.inteco.view.forms.ComplexityViewListForm;

/**
 * Generadora de resultados para la nueva metodología de 2017 basada en UNE-2012.
 *
 * @author alvaro.pelaez
 */
public final class ResultadosAnonimosObservatorioUNEEN2019Utils {
	/** The Constant CHART_EVOLUTION_MP_GREEN_COLOR. */
	private static final String CHART_EVOLUTION_MP_GREEN_COLOR = "chart.evolution.mp.green.color";
	/** The Constant CHART_EVOLUTION_INTECO_RED_COLORS. */
	private static final String CHART_EVOLUTION_INTECO_RED_COLORS = "chart.evolution.inteco.red.colors";
	/** The Constant GRAFICA_SIN_DATOS. */
	private static final String GRAFICA_SIN_DATOS = "grafica.sin.datos";
	/** The Constant _00. */
	private static final String _00 = ".00";
	/** The Constant RESULTADOS_ANONIMOS_PUNT_PORTALES_PARCIAL. */
	private static final String RESULTADOS_ANONIMOS_PUNT_PORTALES_PARCIAL = "resultados.anonimos.punt.portales.parcial";
	/** The Constant RESULTADOS_ANONIMOS_PUNT_PORTALES_A. */
	private static final String RESULTADOS_ANONIMOS_PUNT_PORTALES_A = "resultados.anonimos.punt.portales.a";
	/** The Constant RESULTADOS_ANONIMOS_PUNT_PORTALES_AA. */
	private static final String RESULTADOS_ANONIMOS_PUNT_PORTALES_AA = "resultados.anonimos.punt.portales.aa";
	/** The Constant RESULTADOS_ANONIMOS_PUNT_PORTALES_TC. */
	private static final String RESULTADOS_ANONIMOS_PUNT_PORTALES_TC = "resultados.anonimos.punt.portales.tc";
	/** The Constant RESULTADOS_ANONIMOS_PUNT_PORTALES_PC. */
	private static final String RESULTADOS_ANONIMOS_PUNT_PORTALES_PC = "resultados.anonimos.punt.portales.pc";
	/** The Constant RESULTADOS_ANONIMOS_PUNT_PORTALES_NC. */
	private static final String RESULTADOS_ANONIMOS_PUNT_PORTALES_NC = "resultados.anonimos.punt.portales.nc";
	/** The Constant BIG_DECIMAL_HUNDRED. */
	public static final BigDecimal BIG_DECIMAL_HUNDRED = BigDecimal.valueOf(100);
	/** The x. */
	private static int x = 0;
	/** The y. */
	private static int y = 0;
	static {
		PropertiesManager pmgr = new PropertiesManager();
		x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
		y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
	}
	/** The Constant LEVEL_I_VERIFICATIONS. */
	private static final List<String> LEVEL_I_VERIFICATIONS = Arrays.asList(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION);
	/** The Constant LEVEL_II_VERIFICATIONS. */
	private static final List<String> LEVEL_II_VERIFICATIONS = Arrays.asList(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION,
			Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION);

	/**
	 * COnstructor.
	 */
	private ResultadosAnonimosObservatorioUNEEN2019Utils() {
	}

	/**
	 * Generar los gráficos del observatorio.
	 *
	 * @param messageResources       Message resources para los textos parametrizados
	 * @param executionId            Id del rastreo
	 * @param idExecutionObservatory Id de la ejecucion del observartorio
	 * @param observatoryId          Id del observatorio
	 * @param filePath               Ruta para guardar los temporales
	 * @param type                   Tipo de grafico. Si es MP cambia el color del gráfico
	 * @param regenerate             Indica si hay que regenerar el gráfico o no.
	 * @param tagsFilter             the tags filter
	 * @param tagsFilterFixed        the tags filter fixed
	 * @param exObsIds               the ex obs ids
	 * @throws Exception Excepción lanzada
	 */
	public static void generateGraphics(final MessageResources messageResources, String executionId, final Long idExecutionObservatory, final String observatoryId, final String filePath,
			final String type, final boolean regenerate, String[] tagsFilter, final String[] tagsFilterFixed, String[] exObsIds) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final PropertiesManager pmgr = new PropertiesManager();
			String color = pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_INTECO_RED_COLORS);
			if (type != null && type.equals(Constants.MINISTERIO_P)) {
				color = pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR);
			}
			final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, idExecutionObservatory);
			// Only complexitivities in obs
			final List<ComplejidadForm> complejidades = ComplejidadDAO.getComplejidadesObs(DataBaseManager.getConnection(), tagsFilter, exObsIds);
			// Gráficos globales
			generateGlobalGraphics(messageResources, executionId, filePath, categories, color, regenerate, tagsFilter);
			// Gráficos por segmento
			for (CategoriaForm categoryForm : categories) {
				generateCategoryGraphics(messageResources, executionId, categoryForm, filePath, color, regenerate, tagsFilter);
			}
			// Gráficos por complejidad
			for (ComplejidadForm complejidad : complejidades) {
				generateComplexityGraphics(messageResources, executionId, complejidad, filePath, color, regenerate, tagsFilter);
			}
			if (exObsIds != null && exObsIds.length > 1) {
				generateEvolutionGraphics(messageResources, observatoryId, executionId, filePath, color, regenerate, tagsFilter, exObsIds);
				generateEvolutionGraphicsFixed(messageResources, observatoryId, executionId, filePath, color, regenerate, tagsFilter, tagsFilterFixed, exObsIds);
			}
		} catch (Exception e) {
			Logger.putLog("No se han generado las gráficas correctamente.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Genera los gráficos globales del observatorio.
	 *
	 * @param messageResources Message resources para los textos parametrizados
	 * @param executionId      Id del rastreo
	 * @param filePath         Ruta para guardar los temporales
	 * @param categories       Categorias para el gráfico global
	 * @param color            Color del gráfico
	 * @param regenerate       Indica si hay que regenerar el gráfico o no.
	 * @param tagsFilter       the tags filter
	 * @return Mapa de gráficos
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateGlobalGraphics(final MessageResources messageResources, final String executionId, final String filePath, final List<CategoriaForm> categories,
			final String color, final boolean regenerate, String[] tagsFilter) throws Exception {
		final List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(executionId, Constants.COMPLEXITY_SEGMENT_NONE, null, false, tagsFilter);
		final Map<String, Object> globalGraphics = new HashMap<>();
		if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
			final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
			// List<ComplejidadForm> complejidades = ComplejidadDAO.getComplejidades(DataBaseManager.getConnection(), null, -1);
			final List<ComplejidadForm> complejidades = ComplejidadDAO.getComplejidadesObs(DataBaseManager.getConnection(), tagsFilter, new String[] { executionId });
			// Adecuación global
			String file = filePath + messageResources.getMessage("observatory.graphic.accessibility.level.allocation.name") + ".jpg";
			String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.title");
			getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, pageExecutionList, globalGraphics, title, file, noDataMess, regenerate);
			// Cumplimiento global
			title = messageResources.getMessage("observatory.graphic.compilance.level.allocation.name.title");
			file = filePath + messageResources.getMessage("observatory.graphic.compilance.level.allocation.name") + ".jpg";
			getGlobalCompilanceGraphic(messageResources, pageExecutionList, globalGraphics, title, file, noDataMess, regenerate);
			// Gráfico nivel de cumplimiento global
			title = messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.title");
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segment.strached.name") + ".jpg";
			getGlobalMarkBySegmentGraphic(messageResources, executionId, pageExecutionList, globalGraphics, title, file, noDataMess, categories, regenerate, tagsFilter);
			// comparación adecuación segmento
			title = messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.title");
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.allocation.segments.mark.name") + ".jpg";
			getGlobalAllocationBySegment(messageResources, executionId, globalGraphics, file, noDataMess, pageExecutionList, categories, regenerate, tagsFilter, title);
			// Comparación adecuación complejidad
			title = messageResources.getMessage("observatory.graphic.global.puntuation.allocation.complexity.mark.title");
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.allocation.complexity.mark.name") + ".jpg";
			getGloballAllocationByComplexity(messageResources, executionId, globalGraphics, file, noDataMess, pageExecutionList, complejidades, regenerate, tagsFilter, title);
			// Comparación cumplimiento por segmento
			title = messageResources.getMessage("observatory.graphic.global.puntuation.compilance.segments.mark.title");
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.compilance.segments.mark.name") + ".jpg";
			getGlobalCompilanceBySegment(messageResources, executionId, globalGraphics, file, noDataMess, pageExecutionList, categories, regenerate, tagsFilter, title);
			// Comparación complimiento por complejidad
			title = messageResources.getMessage("observatory.graphic.global.puntuation.compilance.complexitiviy.mark.title");
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.compilance.complexitiviy.mark.name") + ".jpg";
			getGlobalCompilanceByComplexitivity(messageResources, executionId, globalGraphics, file, noDataMess, pageExecutionList, complejidades, regenerate, tagsFilter, title);
			// Comparación de la puntuación por complejidad
			title = messageResources.getMessage("observatory.graphic.global.puntuation.allocation.complexitiviy.strached.title");
			file = filePath + messageResources.getMessage("observatory.graphic.global.puntuation.allocation.complexitiviy.strached.name") + ".jpg";
			getGlobalMarkByComplexitivityGraphic(messageResources, executionId, pageExecutionList, globalGraphics, title, file, noDataMess, complejidades, regenerate, tagsFilter);
			// Comparación puntuación por verificación
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.title");
			file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + ".jpg";
			getMidsComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.title");
			file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + ".jpg";
			getMidsComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
			// Comparación modalidad por verificación
			title = messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.title");
			file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + ".jpg";
			getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, globalGraphics, title, file, noDataMess, Constants.OBS_PRIORITY_1, regenerate);
			title = messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.title");
			file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + ".jpg";
			getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, globalGraphics, title, file, noDataMess, Constants.OBS_PRIORITY_2, regenerate);
			// Comparación de conformidad por verificación
			title = messageResources.getMessage("observatory.graphic.verification.compilance.comparation.level.1.title");
			file = filePath + messageResources.getMessage("observatory.graphic.verification.compilance.comparation.level.1.name") + ".jpg";
			getCompilanceComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
			title = messageResources.getMessage("observatory.graphic.verification.compilance.comparation.level.2.title");
			file = filePath + messageResources.getMessage("observatory.graphic.verification.compilance.comparation.level.2.name") + ".jpg";
			getCompilanceComparationByVerificationLevelGraphic(messageResources, globalGraphics, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
			// Analítica orientada a aspectos
			title = messageResources.getMessage("observatory.graphic.aspect.mid.title");
			file = filePath + messageResources.getMessage("observatory.graphic.aspect.mid.name") + ".jpg";
			getAspectMidsGraphic(messageResources, globalGraphics, file, noDataMess, pageExecutionList, color, title, regenerate);
		}
		return globalGraphics;
	}

	/**
	 * Genera el gráfico de una categoria.
	 *
	 * @param messageResources Message resources para los textos parametrizados
	 * @param idExecution      the id execution
	 * @param category         Categoria a generar
	 * @param filePath         Ruta para guardar los temporales
	 * @param color            Color del gráfico
	 * @param regenerate       Indica si hay que regenerar el gráfico o no.
	 * @param tagsFilter       the tags filter
	 * @return Mapa de gráficos
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateCategoryGraphics(final MessageResources messageResources, final String idExecution, final CategoriaForm category, final String filePath,
			final String color, final boolean regenerate, String[] tagsFilter) throws Exception {
		final Map<String, Object> categoryGraphics = new HashMap<>();
		try {
			final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
			String id = category.getId();
			String name = category.getName();
			String graphicSuffix = "_".concat(name.replaceAll("\\s+", ""));
			final List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(idExecution, Long.parseLong(id), null, false, tagsFilter);
			if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
				String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.title", name);
				String file = filePath + messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", graphicSuffix) + ".jpg";
				getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, regenerate);
				title = messageResources.getMessage("observatory.graphic.accessibility.level.compilance.segment.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.accessibility.level.compilance.segment.name", graphicSuffix) + ".jpg";
				getSegmentCompilanceGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, regenerate);
				title = messageResources.getMessage("observatory.graphic.mark.allocation.segment.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.mark.allocation.segment.name", graphicSuffix) + ".jpg";
				List<ObservatorySiteEvaluationForm> result = getSitesListByLevel(pageExecutionList);
				getMarkAllocationLevelSegmentGraphic(messageResources, title, file, noDataMess, result, false, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.aspect.mid.name") + graphicSuffix + ".jpg";
				title = messageResources.getMessage("observatory.graphic.segment.aspect.mid.title", name);
				getAspectMidsGraphic(messageResources, categoryGraphics, file, noDataMess, pageExecutionList, color, title, regenerate);
				//
				title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.cat.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + graphicSuffix + ".jpg";
				getMidsComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.cat.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + graphicSuffix + ".jpg";
				getMidsComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.category.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + graphicSuffix + ".jpg";
				getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, Constants.OBS_PRIORITY_1, regenerate);
				title = messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.category.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + graphicSuffix + ".jpg";
				getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, Constants.OBS_PRIORITY_2, regenerate);
				title = messageResources.getMessage("observatory.graphic.compilance.by.verification.level.1.category.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.compilance.by.verification.level.1.name") + graphicSuffix + ".jpg";
				getCompilanceComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.compilance.by.verification.level.2.category.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.compilance.by.verification.level.2.name") + graphicSuffix + ".jpg";
				getCompilanceComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
			}
			return categoryGraphics;
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			return categoryGraphics;
		}
	}

	/**
	 * Generate complexity graphics.
	 *
	 * @param messageResources the message resources
	 * @param idExecution      the id execution
	 * @param complejidad      the complejidad
	 * @param filePath         the file path
	 * @param color            the color
	 * @param regenerate       the regenerate
	 * @param tagsFilter       the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateComplexityGraphics(final MessageResources messageResources, final String idExecution, final ComplejidadForm complejidad, final String filePath,
			final String color, final boolean regenerate, String[] tagsFilter) throws Exception {
		final Map<String, Object> categoryGraphics = new HashMap<>();
		try {
			final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
			String id = complejidad.getId();
			String name = complejidad.getName();
			String graphicSuffix = "_".concat(name.replaceAll("\\s+", ""));
			final List<ObservatoryEvaluationForm> pageExecutionList = getGlobalResultData(idExecution, Long.parseLong(id), null, true, tagsFilter);
			if (pageExecutionList != null && !pageExecutionList.isEmpty()) {
				String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.complex.title", name);
				String file = filePath + messageResources.getMessage("observatory.graphic.accessibility.level.allocation.segment.name", graphicSuffix) + ".jpg";
				getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, regenerate);
				title = messageResources.getMessage("observatory.graphic.accessibility.level.compliance.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.accessibility.level.compilance.segment.name", graphicSuffix) + ".jpg";
				getSegmentCompilanceGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, regenerate);
				title = messageResources.getMessage("observatory.graphic.mark.allocation.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.mark.allocation.segment.name", graphicSuffix) + ".jpg";
				List<ObservatorySiteEvaluationForm> result = getSitesListByLevel(pageExecutionList);
				getMarkAllocationLevelSegmentGraphic(messageResources, title, file, noDataMess, result, false, regenerate);
				file = filePath + messageResources.getMessage("observatory.graphic.aspect.mid.name") + graphicSuffix + ".jpg";
				title = messageResources.getMessage("observatory.graphic.segment.aspect.mid.title.complex", name);
				getAspectMidsGraphic(messageResources, categoryGraphics, file, noDataMess, pageExecutionList, color, title, regenerate);
				title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + graphicSuffix + ".jpg";
				getMidsComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + graphicSuffix + ".jpg";
				getMidsComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + graphicSuffix + ".jpg";
				getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, Constants.OBS_PRIORITY_1, regenerate);
				title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + graphicSuffix + ".jpg";
				getModalityByVerificationLevelGraphic(messageResources, pageExecutionList, categoryGraphics, title, file, noDataMess, Constants.OBS_PRIORITY_2, regenerate);
				title = messageResources.getMessage("observatory.graphic.compilance.by.verification.level.1.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.compilance.by.verification.level.1.name") + graphicSuffix + ".jpg";
				getCompilanceComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_1, title, file, noDataMess, pageExecutionList, color, regenerate);
				title = messageResources.getMessage("observatory.graphic.compilance.by.verification.level.2.complex.title", name);
				file = filePath + messageResources.getMessage("observatory.graphic.compilance.by.verification.level.2.name") + graphicSuffix + ".jpg";
				getCompilanceComparationByVerificationLevelGraphic(messageResources, categoryGraphics, Constants.OBS_PRIORITY_2, title, file, noDataMess, pageExecutionList, color, regenerate);
			}
			return categoryGraphics;
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			return categoryGraphics;
		}
	}

	/**
	 * Genera el gráfico de evolución del observatorio.
	 *
	 * @param messageResources the message resources
	 * @param observatoryId    the observatory id
	 * @param executionId      the execution id
	 * @param filePath         the file path
	 * @param color            the color
	 * @param regenerate       the regenerate
	 * @param tagsFilter       the tags filter
	 * @param exObsIds         the ex obs ids
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateEvolutionGraphics(MessageResources messageResources, String observatoryId, final String executionId, String filePath, String color, boolean regenerate,
			String[] tagsFilter, String[] exObsIds) throws Exception {
		final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = resultEvolutionData(Long.valueOf(observatoryId), Long.valueOf(executionId), tagsFilter, exObsIds);
		final Map<String, Object> evolutionGraphics = new HashMap<>();
		if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
			if (pageObservatoryMap.size() != 1) {
				final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
				String title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.A.title");
				String file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_A, title, file, noDataMess, pageObservatoryMap, color, regenerate,
						exObsIds);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.AA.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_AA, title, file, noDataMess, pageObservatoryMap, color, regenerate,
						exObsIds);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.NV.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + ".jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_NV, title, file, noDataMess, pageObservatoryMap, color, regenerate,
						exObsIds);
				title = messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.title.general");
				file = filePath + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg";
				getMidMarkEvolutionGraphic(messageResources, evolutionGraphics, noDataMess, file, pageObservatoryMap, color, regenerate, title);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
						regenerate);
				getMidMarkVerificationEvolutionGraphic(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION, noDataMess, filePath, pageObservatoryMap, color,
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
	 * Generate evolution graphics fixed.
	 *
	 * @param messageResources the message resources
	 * @param observatoryId    the observatory id
	 * @param executionId      the execution id
	 * @param filePath         the file path
	 * @param color            the color
	 * @param regenerate       the regenerate
	 * @param tagsFilter       the tags filter
	 * @param tagsFilterFixed  the tags filter fixed
	 * @param exObsIds         the ex obs ids
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<String, Object> generateEvolutionGraphicsFixed(MessageResources messageResources, String observatoryId, final String executionId, String filePath, String color,
			boolean regenerate, String[] tagsFilter, final String[] tagsFilterFixed, String[] exObsIds) throws Exception {
		final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioUNEEN2019Utils.resultEvolutionData(Long.valueOf(observatoryId), Long.valueOf(executionId),
				tagsFilterFixed, exObsIds);
		final Map<String, Object> evolutionGraphics = new HashMap<>();
		if (pageObservatoryMap != null && !pageObservatoryMap.isEmpty()) {
			if (pageObservatoryMap.size() != 1) {
				final String noDataMess = messageResources.getMessage(GRAFICA_SIN_DATOS);
				String title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.A.title");
				String file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.A.name") + "Fijos.jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_A, title, file, noDataMess, pageObservatoryMap, color, regenerate,
						exObsIds);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.AA.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.AA.name") + "Fijos.jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_AA, title, file, noDataMess, pageObservatoryMap, color, regenerate,
						exObsIds);
				title = messageResources.getMessage("observatory.graphic.accessibility.evolution.approval.NV.title");
				file = filePath + messageResources.getMessage("observatory.graphic.accesibility.evolution.approval.NV.name") + "Fijos.jpg";
				getApprovalLevelEvolutionGraphic(messageResources, observatoryId, executionId, evolutionGraphics, Constants.OBS_NV, title, file, noDataMess, pageObservatoryMap, color, regenerate,
						exObsIds);
				file = filePath + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + "Fijos.jpg";
				title = messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.title.fixed");
				getMidMarkEvolutionGraphic(messageResources, evolutionGraphics, noDataMess, file, pageObservatoryMap, color, regenerate, title);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				getMidMarkVerificationEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION, noDataMess, filePath, pageObservatoryMap,
						color, regenerate);
				Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<>();
				for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
					resultsByAspect.put(entry.getKey(), aspectMidsPuntuationGraphicData(messageResources, entry.getValue()));
				}
				getMidMarkAspectEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID, noDataMess, filePath, resultsByAspect, color,
						regenerate);
				getMidMarkAspectEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_GENERAL_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
				getMidMarkAspectEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID, noDataMess, filePath, resultsByAspect, color,
						regenerate);
				getMidMarkAspectEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID, noDataMess, filePath, resultsByAspect, color,
						regenerate);
				getMidMarkAspectEvolutionGraphicFixed(messageResources, evolutionGraphics, Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID, noDataMess, filePath, resultsByAspect, color, regenerate);
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
	 * Info global compilance level.
	 *
	 * @param messageResources the message resources
	 * @param result           the result
	 * @return the list
	 */
	public static List<GraphicData> infoGlobalCompilanceLevel(final MessageResources messageResources, final Map<String, Integer> result) {
		final List<GraphicData> labelValueList = new ArrayList<>();
		final int totalPort = result.get(Constants.OBS_COMPILANCE_FULL) + result.get(Constants.OBS_COMPILANCE_NONE) + result.get(Constants.OBS_COMPILANCE_PARTIAL);
		labelValueList.add(infoGlobalAccessibilityLevelGraphicData(messageResources.getMessage("resultados.anonimos.porc.portales.nc"), result.get(Constants.OBS_COMPILANCE_FULL), totalPort));
		labelValueList.add(infoGlobalAccessibilityLevelGraphicData(messageResources.getMessage("resultados.anonimos.porc.portales.pc"), result.get(Constants.OBS_COMPILANCE_PARTIAL), totalPort));
		labelValueList.add(infoGlobalAccessibilityLevelGraphicData(messageResources.getMessage("resultados.anonimos.porc.portales.tc"), result.get(Constants.OBS_COMPILANCE_NONE), totalPort));
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
			labelValue.setPercentageP(String.valueOf(new BigDecimal(result).multiply(BIG_DECIMAL_HUNDRED).divide(new BigDecimal(totalPort), 2, BigDecimal.ROUND_HALF_UP)).replace(_00, "") + "%");
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
	public static List<LabelValueBean> infoComparisionAllocation(final MessageResources messageResources, final Map<String, BigDecimal> category) {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_AA), category.get(Constants.OBS_AA)));
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_A), category.get(Constants.OBS_A)));
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_PARCIAL), category.get(Constants.OBS_NV)));
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
	public static List<LabelValueBean> infoComparisonAllocationPuntuation(final MessageResources messageResources, final Map<String, BigDecimal> result) throws Exception {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(new LabelValueBean(messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_AA), String.valueOf(result.get(Constants.OBS_AA)).replace(_00, "")));
		labelValueList.add(new LabelValueBean(messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_A), String.valueOf(result.get(Constants.OBS_A)).replace(_00, "")));
		labelValueList.add(new LabelValueBean(messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_PARCIAL), String.valueOf(result.get(Constants.OBS_NV)).replace(_00, "")));
		return labelValueList;
	}

	/**
	 * Info comparison by segment compilance.
	 *
	 * @param messageResources the message resources
	 * @param result           the result
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<LabelValueBean> infoComparisonCompilancePuntuaction(final MessageResources messageResources, final Map<String, BigDecimal> result) throws Exception {
		final List<LabelValueBean> labelValueList = new ArrayList<>();
		labelValueList.add(new LabelValueBean(messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_NC), String.valueOf(result.get(Constants.OBS_COMPILANCE_FULL)).replace(_00, "")));
		labelValueList.add(new LabelValueBean(messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_PC), String.valueOf(result.get(Constants.OBS_COMPILANCE_PARTIAL)).replace(_00, "")));
		labelValueList.add(new LabelValueBean(messageResources.getMessage(RESULTADOS_ANONIMOS_PUNT_PORTALES_TC), String.valueOf(result.get(Constants.OBS_COMPILANCE_NONE)).replace(_00, "")));
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
		labelValueList.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.general"), result.get(messageResources.getMessage("observatory.aspect.general"))));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.presentacion"), result.get(messageResources.getMessage("observatory.aspect.presentation"))));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.estructura"), result.get(messageResources.getMessage("observatory.aspect.structure"))));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.navegacion"), result.get(messageResources.getMessage("observatory.aspect.navigation"))));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("resultados.anonimos.alternativa"), result.get(messageResources.getMessage("observatory.aspect.alternatives"))));
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
			labelValue.setValue(String.valueOf(result).replace(_00, ""));
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
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.1"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.2"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.3"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.4"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.5"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.6"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.7"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.8"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.9"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.10"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.11"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.12"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.13"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION)));
		labelValueList.add(
				createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.1.14"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION)));
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
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.2.1"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.2.2"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.2.3"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.2.4"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.2.5"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION)));
		labelValueList
				.add(createLabelValueScore(messageResources, messageResources.getMessage("minhap.observatory.5_0.subgroup.2.6"), result.get(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION)));
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
			labelValue.setValue(String.valueOf(entry.getValue()).replace(_00, "") + suffix);
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
	 * Gets the compilance comparation by verification level graphic.
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
	 * @return the compilance comparation by verification level graphic
	 * @throws Exception the exception
	 */
	public static void getCompilanceComparationByVerificationLevelGraphic(final MessageResources messageResources, Map<String, Object> globalGraphics, final String level, final String title,
			final String filePath, final String noDataMess, final List<ObservatoryEvaluationForm> pageExecutionList, final String color, final boolean regenerate) throws Exception {
		final File file = new File(filePath);
		final Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(pageExecutionList, level);
		final DefaultCategoryDataset dataSet = createStackedBarDataSetForCompilance(results, messageResources);
		// Incluimos los resultados en la request
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_COMPILANCE_VERIFICATION_I, infoLevelVerificationCompilanceComparison(generatePercentajesCompilanceVerification(results)));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_COMPILANCE_VERIFICATION_II,
					infoLevelVerificationCompilanceComparison(generatePercentajesCompilanceVerification(results)));
		}
		if (!file.exists() || regenerate) {
			final PropertiesManager pmgr = new PropertiesManager();
			final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.compilance.colors"));
			chartForm.setTitle(title);
			GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
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
			chartForm.setTitle(title);
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
					modalityComparisonForm.setGreenPercentage(results.get(key).toString().replace(_00, "") + "%");
					if (results.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX) != null) {
						modalityComparisonForm.setRedPercentage(results.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setRedPercentage("0%");
					}
				} else if (key.contains(Constants.OBS_VALUE_RED_SUFFIX)) {
					modalityComparisonForm.setVerification(key.replace(Constants.OBS_VALUE_RED_SUFFIX, ""));
					modalityComparisonForm.setRedPercentage(results.get(key).toString().replace(_00, "") + "%");
					if (results.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX) != null) {
						modalityComparisonForm.setGreenPercentage(results.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setGreenPercentage("0%");
					}
				}
				modalityComparisonList.add(modalityComparisonForm);
			}
		}
		// Necesitamos implementar un orden específico para que p.e.
		// 1.10 vaya después de 1.9 y no de 1.
		Collections.sort(modalityComparisonList, new Comparator<ModalityComparisonForm>() {
			@Override
			public int compare(ModalityComparisonForm version1, ModalityComparisonForm version2) {
				String[] v1 = version1.getVerification().substring(version1.getVerification().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length()).split("\\.");
				String[] v2 = version2.getVerification().substring(version2.getVerification().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length()).split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[0]);
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(version[1]) : 0;
			}
		});
		return modalityComparisonList;
	}

	/**
	 * Info level verification compilance comparison.
	 *
	 * @param results the results
	 * @return the list
	 */
	public static List<ComplianceComparisonForm> infoLevelVerificationCompilanceComparison(final Map<String, BigDecimal> results) {
		final List<ComplianceComparisonForm> modalityComparisonList = new ArrayList<>();
		for (String key : results.keySet()) {
			if (!modalityComparisonList.contains(new ComplianceComparisonForm(key.replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")))
					&& !modalityComparisonList.contains(new ComplianceComparisonForm(key.replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "")))
					&& !modalityComparisonList.contains(new ComplianceComparisonForm(key.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "")))) {
				final ComplianceComparisonForm modalityComparisonForm = new ComplianceComparisonForm();
				if (key.contains(Constants.OBS_VALUE_COMPILANCE_SUFFIX)) {
					modalityComparisonForm.setVerification(key.replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, ""));
					modalityComparisonForm.setGreenPercentage(results.get(key).toString().replace(_00, "") + "%");
					// Null other percentajes
					if (results.get(key.replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX) != null) {
						modalityComparisonForm
								.setRedPercentage(results.get(key.replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setRedPercentage("0%");
					}
					if (results.get(key.replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX) != null) {
						modalityComparisonForm.setGrayPercentage(
								results.get(key.replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setGrayPercentage("0%");
					}
				} else if (key.contains(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX)) {
					modalityComparisonForm.setVerification(key.replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, ""));
					modalityComparisonForm.setRedPercentage(results.get(key).toString().replace(_00, "") + "%");
					if (results.get(key.replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_COMPILANCE_SUFFIX) != null) {
						modalityComparisonForm
								.setGreenPercentage(results.get(key.replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_COMPILANCE_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setGreenPercentage("0%");
					}
					if (results.get(key.replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX) != null) {
						modalityComparisonForm.setGrayPercentage(
								results.get(key.replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setGrayPercentage("0%");
					}
				} else if (key.contains(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX)) {
					modalityComparisonForm.setVerification(key.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""));
					modalityComparisonForm.setGrayPercentage(results.get(key).toString().replace(_00, "") + "%");
					if (results.get(key.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "") + Constants.OBS_VALUE_COMPILANCE_SUFFIX) != null) {
						modalityComparisonForm.setGreenPercentage(
								results.get(key.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "") + Constants.OBS_VALUE_COMPILANCE_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setGreenPercentage("0%");
					}
					if (results.get(key.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX) != null) {
						modalityComparisonForm.setRedPercentage(
								results.get(key.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, "") + Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX).toString().replace(_00, "") + "%");
					} else {
						modalityComparisonForm.setRedPercentage("0%");
					}
				}
				modalityComparisonList.add(modalityComparisonForm);
			}
		}
		// Necesitamos implementar un orden específico para que p.e.
		// 1.10 vaya después de 1.9 y no de 1.
		Collections.sort(modalityComparisonList, new Comparator<ComplianceComparisonForm>() {
			@Override
			public int compare(ComplianceComparisonForm version1, ComplianceComparisonForm version2) {
				String[] v1 = version1.getVerification().split("\\.");
				String[] v2 = version2.getVerification().split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[0]);
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(version[1]) : 0;
			}
		});
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
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @return the global mark by segments group graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalAllocationBySegment(final MessageResources messageResources, final String executionId, Map<String, Object> globalGraphics, final String filePath,
			final String noDataMess, final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories, final boolean regenerate, String[] tagsFilter, final String title)
			throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = createGraphicsMap(categories);
		final List<CategoryViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageResultsBySegmentMap(executionId, pageExecutionList, resultLists.get(i), tagsFilter);
			final DefaultCategoryDataset dataSet = createDataSet(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			}
			// Incluimos los resultados en la request
			for (CategoriaForm category : resultLists.get(i)) {
				final CategoryViewListForm categoryView = new CategoryViewListForm(category, infoComparisonAllocationPuntuation(messageResources, resultsBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPS, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CPS_GRAPH, resultLists.size());
	}

	/**
	 * Gets the global compilance by segment.
	 *
	 * @param messageResources  the message resources
	 * @param executionId       the execution id
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @return the global compilance by segment
	 * @throws Exception the exception
	 */
	public static void getGlobalCompilanceBySegment(final MessageResources messageResources, final String executionId, Map<String, Object> globalGraphics, final String filePath,
			final String noDataMess, final List<ObservatoryEvaluationForm> pageExecutionList, final List<CategoriaForm> categories, final boolean regenerate, String[] tagsFilter, final String title)
			throws Exception {
		final Map<Integer, List<CategoriaForm>> resultLists = createGraphicsMap(categories);
		final List<CategoryViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageCompilanceResultsBySegmentMap(executionId, pageExecutionList, resultLists.get(i), tagsFilter);
			final DefaultCategoryDataset dataSet = createCompilanceDataSet(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			}
			// Incluimos los resultados en la request
			for (CategoriaForm category : resultLists.get(i)) {
				final CategoryViewListForm categoryView = new CategoryViewListForm(category, infoComparisonCompilancePuntuaction(messageResources, resultsBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPS, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CMPS_GRAPH, resultLists.size());
	}

	/**
	 * Gets the global compilance by complexitivity.
	 *
	 * @param messageResources  the message resources
	 * @param executionId       the execution id
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param complexities      the complexities
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @return the global compilance by complexitivity
	 * @throws Exception the exception
	 */
	public static void getGlobalCompilanceByComplexitivity(final MessageResources messageResources, final String executionId, Map<String, Object> globalGraphics, final String filePath,
			final String noDataMess, final List<ObservatoryEvaluationForm> pageExecutionList, final List<ComplejidadForm> complexities, final boolean regenerate, String[] tagsFilter,
			final String title) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = createGraphicsMapComplexities(complexities);
		final List<ComplexityViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageCompilanceResultsByComplexitivityMap(executionId, pageExecutionList, resultLists.get(i),
					tagsFilter);
			final DefaultCategoryDataset dataSet = createDataSetComplexityCompilance(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			}
			// Incluimos los resultados en la request
			for (ComplejidadForm complexitivity : resultLists.get(i)) {
				final ComplexityViewListForm categoryView = new ComplexityViewListForm(complexitivity, infoComparisonCompilancePuntuaction(messageResources, resultsBySegment.get(complexitivity)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CMPC, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CMPC_GRAPH, resultLists.size());
	}

	/**
	 * Gets the global mark by complexity group graphic.
	 *
	 * @param messageResources  the message resources
	 * @param executionId       the execution id
	 * @param globalGraphics    the global graphics
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param pageExecutionList the page execution list
	 * @param complexities      the complexities
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @param title             the title
	 * @return the global mark by complexity group graphic
	 * @throws Exception the exception
	 */
	public static void getGloballAllocationByComplexity(final MessageResources messageResources, final String executionId, Map<String, Object> globalGraphics, final String filePath,
			final String noDataMess, final List<ObservatoryEvaluationForm> pageExecutionList, final List<ComplejidadForm> complexities, final boolean regenerate, String[] tagsFilter,
			final String title) throws Exception {
		final Map<Integer, List<ComplejidadForm>> resultLists = createGraphicsMapComplexities(complexities);
		final List<ComplexityViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = calculatePercentageResultsByComplexityMap(executionId, pageExecutionList, resultLists.get(i), tagsFilter);
			final DefaultCategoryDataset dataSet = createDataSetComplexity(resultsBySegment, messageResources);
			final PropertiesManager pmgr = new PropertiesManager();
			// Si la gráfica no existe, la creamos
			if (!file.exists() || regenerate) {
				final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				chartForm.setTitle(title);
				GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			}
			// Incluimos los resultados en la request
			for (ComplejidadForm category : resultLists.get(i)) {
				final ComplexityViewListForm categoryView = new ComplexityViewListForm(category, infoComparisonAllocationPuntuation(messageResources, resultsBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CPCX, categoriesLabels);
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
	 * Creates the compilance data set.
	 *
	 * @param result           the result
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	private static DefaultCategoryDataset createCompilanceDataSet(final Map<CategoriaForm, Map<String, BigDecimal>> result, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<CategoriaForm, Map<String, BigDecimal>> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_NONE), parseLevelLabel(Constants.OBS_COMPILANCE_NONE, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_PARTIAL), parseLevelLabel(Constants.OBS_COMPILANCE_PARTIAL, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_FULL), parseLevelLabel(Constants.OBS_COMPILANCE_FULL, messageResources), entry.getKey().getName());
		}
		return dataSet;
	}

	/**
	 * Creates the data set complexity.
	 *
	 * @param result           the result
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	public static DefaultCategoryDataset createDataSetComplexity(final Map<ComplejidadForm, Map<String, BigDecimal>> result, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<ComplejidadForm, Map<String, BigDecimal>> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue().get(Constants.OBS_NV), parseLevelLabel(Constants.OBS_NV, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_A), parseLevelLabel(Constants.OBS_A, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_AA), parseLevelLabel(Constants.OBS_AA, messageResources), entry.getKey().getName());
		}
		return dataSet;
	}

	/**
	 * Creates the data set ambit.
	 *
	 * @param result           the result
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	public static DefaultCategoryDataset createDataSetAmbit(final Map<AmbitoForm, Map<String, BigDecimal>> result, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<AmbitoForm, Map<String, BigDecimal>> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue().get(Constants.OBS_NV), parseLevelLabel(Constants.OBS_NV, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_A), parseLevelLabel(Constants.OBS_A, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_AA), parseLevelLabel(Constants.OBS_AA, messageResources), entry.getKey().getName());
		}
		return dataSet;
	}

	/**
	 * Creates the data set complexity compilance.
	 *
	 * @param result           the result
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	public static DefaultCategoryDataset createDataSetComplexityCompilance(final Map<ComplejidadForm, Map<String, BigDecimal>> result, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<ComplejidadForm, Map<String, BigDecimal>> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_NONE), parseLevelLabel(Constants.OBS_COMPILANCE_NONE, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_PARTIAL), parseLevelLabel(Constants.OBS_COMPILANCE_PARTIAL, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_FULL), parseLevelLabel(Constants.OBS_COMPILANCE_FULL, messageResources), entry.getKey().getName());
		}
		return dataSet;
	}

	/**
	 * Creates the data set ambit compilance.
	 *
	 * @param result           the result
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	public static DefaultCategoryDataset createDataSetAmbitCompilance(final Map<AmbitoForm, Map<String, BigDecimal>> result, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<AmbitoForm, Map<String, BigDecimal>> entry : result.entrySet()) {
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_NONE), parseLevelLabel(Constants.OBS_COMPILANCE_NONE, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_PARTIAL), parseLevelLabel(Constants.OBS_COMPILANCE_PARTIAL, messageResources), entry.getKey().getName());
			dataSet.addValue(entry.getValue().get(Constants.OBS_COMPILANCE_FULL), parseLevelLabel(Constants.OBS_COMPILANCE_FULL, messageResources), entry.getKey().getName());
		}
		return dataSet;
	}

	/**
	 * Creates the stacked bar data set for modality.
	 *
	 * @param unorderedResults the unordered results
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	private static DefaultCategoryDataset createStackedBarDataSetForModality(final Map<String, BigDecimal> unorderedResults, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		// Necesitamos reordenar los resputados para que el valor 1.10
		// vaya después de 1.9 y no de 1.1
		Map<String, BigDecimal> results = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String version1, String version2) {
				String[] v1 = version1.split("\\.");
				String[] v2 = version2.split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					if (minor(v1) == minor(v2)) { // Devolvemos 1
													// aunque sean iguales
													// porque las claves lleva
													// asociado un subfijo que
													// aqui no tenemos en cuenta
						return 1;
					}
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[4].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""));
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(version[5].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")) : 0;
			}
		});
		for (Map.Entry<String, BigDecimal> entry : unorderedResults.entrySet()) {
			results.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, BigDecimal> entry : results.entrySet()) {
			if (entry.getKey().contains(Constants.OBS_VALUE_RED_SUFFIX)) {
				dataSet.addValue(entry.getValue(), messageResources.getMessage("observatory.graphic.modality.red"), entry.getKey().replace(Constants.OBS_VALUE_RED_SUFFIX, "")
						.substring(entry.getKey().replace(Constants.OBS_VALUE_RED_SUFFIX, "").indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length()));
			} else if (entry.getKey().contains(Constants.OBS_VALUE_GREEN_SUFFIX)) {
				dataSet.addValue(entry.getValue(), messageResources.getMessage("observatory.graphic.modality.green"), entry.getKey().replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")
						.substring(entry.getKey().replace(Constants.OBS_VALUE_GREEN_SUFFIX, "").indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length()));
			}
		}
		return dataSet;
	}

	/**
	 * Creates the stacked bar data set for compilance.
	 *
	 * @param results          the results
	 * @param messageResources the message resources
	 * @return the default category dataset
	 */
	private static DefaultCategoryDataset createStackedBarDataSetForCompilance(final Map<Long, Map<String, BigDecimal>> results, final MessageResources messageResources) {
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		Map<String, BigDecimal> resultsOrdered = generatePercentajesCompilanceVerification(results);
		Map<String, BigDecimal> resultsOrderedReordered = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String version1, String version2) {
				String[] v1 = version1.split("\\.");
				String[] v2 = version2.split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					if (minor(v1) == minor(v2)) { // Devolvemos 1
													// aunque sean iguales
													// porque las claves lleva
													// asociado un subfijo que
													// aqui no tenemos en cuenta
						return 1;
					}
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[0].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")
						.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""));
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(
						version[1].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""))
						: 0;
			}
		});
		for (Map.Entry<String, BigDecimal> entryO : resultsOrdered.entrySet()) {
			resultsOrderedReordered.put(entryO.getKey(), entryO.getValue());
		}
		for (Map.Entry<String, BigDecimal> entry : resultsOrderedReordered.entrySet()) {
			if (entry.getKey().contains(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX)) {
				dataSet.addValue(entry.getValue(), messageResources.getMessage("observatory.graphic.compilance.red"), entry.getKey().replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, ""));
			} else if (entry.getKey().contains(Constants.OBS_VALUE_COMPILANCE_SUFFIX)) {
				dataSet.addValue(entry.getValue(), messageResources.getMessage("observatory.graphic.compilance.green"), entry.getKey().replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, ""));
			} else if (entry.getKey().contains(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX)) {
				dataSet.addValue(entry.getValue(), messageResources.getMessage("observatory.graphic.compilance.gray"), entry.getKey().replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""));
			}
		}
		return dataSet;
	}

	/**
	 * Generate percentajes compilance verification.
	 *
	 * @param results the results
	 * @return the map
	 */
	public static Map<String, BigDecimal> generatePercentajesCompilanceVerification(final Map<Long, Map<String, BigDecimal>> results) {
		Map<String, BigDecimal> resultsC = new TreeMap<>();
		Map<String, BigDecimal> resultsOrdered = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String version1, String version2) {
				String[] v1 = version1.split("\\.");
				String[] v2 = version2.split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					if (minor(v1) == minor(v2)) { // Devolvemos 1
													// aunque sean iguales
													// porque las claves lleva
													// asociado un subfijo que
													// aqui no tenemos en cuenta
						return 1;
					}
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[0].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")
						.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""));
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(
						version[1].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""))
						: 0;
			}
		});
		// Process results
		for (Map.Entry<Long, Map<String, BigDecimal>> result : results.entrySet()) {
			for (Map.Entry<String, BigDecimal> verificationResult : result.getValue().entrySet()) {
				String keyCompilance = verificationResult.getKey().concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX);
				String keyNoCompilance = verificationResult.getKey().concat(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX);
				String keyNoApply = verificationResult.getKey().concat(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX);
				// Add all posbible values???
				if (!resultsC.containsKey(keyCompilance)) {
					resultsC.put(keyCompilance, new BigDecimal(0));
				}
				if (!resultsC.containsKey(keyNoCompilance)) {
					resultsC.put(keyNoCompilance, new BigDecimal(0));
				}
				if (!resultsC.containsKey(keyNoApply)) {
					resultsC.put(keyNoApply, new BigDecimal(0));
				}
				if (verificationResult.getValue().compareTo(new BigDecimal(9)) >= 0) {
					// If exists +1
					if (resultsC.containsKey(keyCompilance)) {
						resultsC.put(keyCompilance, resultsC.get(keyCompilance).add(new BigDecimal(1)));
					} else {
						resultsC.put(keyCompilance, new BigDecimal(1));
					}
				} else if (verificationResult.getValue().compareTo(new BigDecimal(0)) >= 0) {
					// If exists +1
					if (resultsC.containsKey(keyNoCompilance)) {
						resultsC.put(keyNoCompilance, resultsC.get(keyNoCompilance).add(new BigDecimal(1)));
					} else {
						resultsC.put(keyNoCompilance, new BigDecimal(1));
					}
				} else {
					// If exists +1
					if (resultsC.containsKey(keyNoApply)) {
						resultsC.put(keyNoApply, resultsC.get(keyNoApply).add(new BigDecimal(1)));
					} else {
						resultsC.put(keyNoApply, new BigDecimal(1));
					}
				}
			}
		}
		for (Map.Entry<String, BigDecimal> entry : resultsC.entrySet()) {
			resultsOrdered.put(entry.getKey(), entry.getValue().divide(new BigDecimal(results.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_HUNDRED));
		}
		Map<String, BigDecimal> resultsP = new TreeMap<>();
		for (Map.Entry<String, BigDecimal> entry : resultsOrdered.entrySet()) {
			resultsP.put(entry.getKey(), entry.getValue());
		}
		return resultsP;
	}

	/**
	 * Generate percentajes compilance verification.
	 *
	 * @param results the results
	 * @return the map
	 */
	public static Map<String, BigDecimal> generatePercentajesConformanceVerification(final Map<Long, Map<String, BigDecimal>> results) {
		Map<String, BigDecimal> resultsC = new TreeMap<>();
		Map<String, BigDecimal> resultsOrdered = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String version1, String version2) {
				String[] v1 = version1.split("\\.");
				String[] v2 = version2.split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					if (minor(v1) == minor(v2)) { // Devolvemos 1
													// aunque sean iguales
													// porque las claves lleva
													// asociado un subfijo que
													// aqui no tenemos en cuenta
						return 1;
					}
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[0].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "")
						.replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""));
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(
						version[1].replace(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_COMPILANCE_SUFFIX, "").replace(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX, ""))
						: 0;
			}
		});
		// Process results
		for (Map.Entry<Long, Map<String, BigDecimal>> result : results.entrySet()) {
			for (Map.Entry<String, BigDecimal> verificationResult : result.getValue().entrySet()) {
				String keyCompilance = verificationResult.getKey().concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX);
				String keyNoCompilance = verificationResult.getKey().concat(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX);
				// Add all posbible values???
				if (!resultsC.containsKey(keyCompilance)) {
					resultsC.put(keyCompilance, new BigDecimal(0));
				}
				if (!resultsC.containsKey(keyNoCompilance)) {
					resultsC.put(keyNoCompilance, new BigDecimal(0));
				}
				if (verificationResult.getValue().compareTo(new BigDecimal(9)) >= 0) {
					// If exists +1
					if (resultsC.containsKey(keyCompilance)) {
						resultsC.put(keyCompilance, resultsC.get(keyCompilance).add(new BigDecimal(1)));
					} else {
						resultsC.put(keyCompilance, new BigDecimal(1));
					}
				} else if (verificationResult.getValue().compareTo(new BigDecimal(0)) >= 0) {
					// If exists +1
					if (resultsC.containsKey(keyNoCompilance)) {
						resultsC.put(keyNoCompilance, resultsC.get(keyNoCompilance).add(new BigDecimal(1)));
					} else {
						resultsC.put(keyNoCompilance, new BigDecimal(1));
					}
				}
			}
		}
		for (Map.Entry<String, BigDecimal> entry : resultsC.entrySet()) {
			resultsOrdered.put(entry.getKey(), entry.getValue().divide(new BigDecimal(results.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_HUNDRED));
		}
		Map<String, BigDecimal> resultsP = new TreeMap<>();
		for (Map.Entry<String, BigDecimal> entry : resultsOrdered.entrySet()) {
			resultsP.put(entry.getKey(), entry.getValue());
		}
		return resultsP;
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
			aspectStr = messageResources.getMessage("observatory.aspect.general");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_ALTERNATIVE_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.alternatives");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_STRUCTURE_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.structure");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_NAVIGATION_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.navigation");
		} else if (aspect.equals(Constants.OBSERVATORY_GRAPHIC_ASPECT_PRESENTATION_ID)) {
			aspectStr = messageResources.getMessage("observatory.aspect.presentation");
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
	 * Gets the mid mark aspect evolution graphic fixed.
	 *
	 * @param messageResources  the message resources
	 * @param evolutionGraphics the evolution graphics
	 * @param aspect            the aspect
	 * @param noDataMess        the no data mess
	 * @param filePath          the file path
	 * @param resultsByAspect   the results by aspect
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the mid mark aspect evolution graphic fixed
	 * @throws Exception the exception
	 */
	public static void getMidMarkAspectEvolutionGraphicFixed(final MessageResources messageResources, Map<String, Object> evolutionGraphics, final String aspect, final String noDataMess,
			final String filePath, final Map<Date, Map<String, BigDecimal>> resultsByAspect, final String color, final boolean regenerate) throws Exception {
		final String fileName = filePath + messageResources.getMessage("observatory.graphic.evolution.aspect.mid.puntuation.name", aspect) + "Fijos.jpg";
		final File file = new File(fileName);
		final String aspectStr;
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
		if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V115, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V116, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V117, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V215, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V216, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V217, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION)) {
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
	 * Gets the mid mark verification evolution graphic fixed.
	 *
	 * @param messageResources  the message resources
	 * @param evolutionGraphics the evolution graphics
	 * @param verification      the verification
	 * @param noDataMess        the no data mess
	 * @param filePath          the file path
	 * @param result            the result
	 * @param color             the color
	 * @param regenerate        the regenerate
	 * @return the mid mark verification evolution graphic fixed
	 * @throws Exception the exception
	 */
	public static void getMidMarkVerificationEvolutionGraphicFixed(final MessageResources messageResources, Map<String, Object> evolutionGraphics, final String verification, final String noDataMess,
			final String filePath, final Map<Date, List<ObservatoryEvaluationForm>> result, final String color, final boolean regenerate) throws Exception {
		final String fileName = filePath + File.separator + messageResources.getMessage("observatory.graphic.evolution.verification.mid.puntuation.name", verification) + "Fijos.jpg";
		final File file = new File(fileName);
		// Recuperamos los resultados
		final Map<String, BigDecimal> resultData = calculateVerificationEvolutionPuntuationDataSet(verification, result);
		// Los incluimos en la request
		if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_1_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V111, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_2_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V112, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_3_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V113, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_4_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V114, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_5_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V115, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_6_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V116, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_7_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V117, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_1_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V121, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_2_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V122, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_3_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V123, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_124_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V124, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_125_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V125, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_126_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V126, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_8_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V211, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_9_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V212, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_10_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V213, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_11_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V214, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_12_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V215, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_13_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V216, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_1_14_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V217, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_4_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V221, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_5_VERIFICATION)) {
			evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_V222, infoMidMarkVerificationEvolutionGraphic(messageResources, resultData));
		} else if (verification.equals(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_2_6_VERIFICATION)) {
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
	 * @param title             the title
	 * @return the mid mark evolution graphic
	 * @throws Exception the exception
	 */
	public static void getMidMarkEvolutionGraphic(final MessageResources messageResources, Map<String, Object> evolutionGraphics, final String noDataMess, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> observatoryResult, final String color, final boolean regenerate, final String title) throws Exception {
		// Recuperamos los resultados
		final Map<String, BigDecimal> resultData = calculateEvolutionPuntuationDataSet(observatoryResult);
		// Los incluimos en la request
		evolutionGraphics.put(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_DATA_LIST_MID_PUNT, infoMidMarkEvolutionGraphic(resultData));
		infoMidMarkEvolutionGraphic(resultData);
		final File file = new File(filePath);
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBarChart(resultData, title, "", "", color, false, false, true, filePath, noDataMess, messageResources, x, y);
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
	 * @param exObsIds          the ex obs ids
	 * @return the approval level evolution graphic
	 * @throws Exception the exception
	 */
	public static void getApprovalLevelEvolutionGraphic(final MessageResources messageResources, final String observatoryId, final String executionId, Map<String, Object> evolutionGraphics,
			final String suitabilityLevel, final String title, final String filePath, final String noDataMess, final Map<Date, List<ObservatoryEvaluationForm>> observatoryResult, final String color,
			final boolean regenerate, String[] exObsIds) throws Exception {
		final File file = new File(filePath);
		final Map<Date, Map<Long, Map<String, Integer>>> result = getEvolutionObservatoriesSitesByType(observatoryId, executionId, observatoryResult, exObsIds);
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
			infoMarkAllocationLevelSegment(result2);
		}
		// Si no existe la gráfica, la creamos
		if (!file.exists() || regenerate) {
			GraphicsUtils.createBar1PxChart(result2, title, "", "", filePath, noDataMess, messageResources, x, y, showColLab);
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
			GraphicsUtils.createBarChart(result, title, "", "", color, false, false, false, filePath, noDataMess, messageResources, x, y);
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
	 * @param executionId        the execution id
	 * @param categoryId         the category id
	 * @param pageExecutionList  the page execution list
	 * @param isComplexityFilter the is complexity filter
	 * @param tagsFilter         Tags filter
	 * @return the global result data
	 * @throws Exception the exception
	 */
	public static List<ObservatoryEvaluationForm> getGlobalResultData(final String executionId, final long categoryId, final List<ObservatoryEvaluationForm> pageExecutionList,
			boolean isComplexityFilter, String[] tagsFilter) throws Exception {
		return getGlobalResultData(executionId, categoryId, pageExecutionList, null, isComplexityFilter, tagsFilter, true);
	}

	/**
	 * Gets the global result data.
	 *
	 * @param executionId        the execution id
	 * @param categoryId         the category id
	 * @param pageExecutionList  the page execution list
	 * @param idCrawler          the id crawler
	 * @param isComplexityFilter the is complexity filter
	 * @param tagsFilter         the tags filter
	 * @return the global result data
	 * @throws Exception the exception
	 */
	public static List<ObservatoryEvaluationForm> getGlobalResultData(final String executionId, final long categoryId, final List<ObservatoryEvaluationForm> pageExecutionList, final Long idCrawler,
			boolean isComplexityFilter, String[] tagsFilter, final boolean originAnnexes) throws Exception {
		List<ObservatoryEvaluationForm> observatoryEvaluationList;
		Logger.putLog("La cache con id " + Constants.OBSERVATORY_KEY_CACHE + executionId + " no está disponible, se va a regenerar", ResultadosAnonimosObservatorioUNEEN2019Utils.class,
				Logger.LOG_LEVEL_WARNING);
		try (Connection c = DataBaseManager.getConnection()) {
			observatoryEvaluationList = new ArrayList<>();
			final List<Long> listAnalysis = new ArrayList<>();
			List<Long> listExecutionsIds = new ArrayList<>();
			if (idCrawler == null) {
				// Filter by tags
				if (tagsFilter != null && tagsFilter.length > 0) {
					listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIdsMatchTags(c, Long.parseLong(executionId), tagsFilter);
				} else {
					listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(c, Long.parseLong(executionId), Constants.COMPLEXITY_SEGMENT_NONE);
				}
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
					final Evaluation evaluation = evaluator.getObservatoryAnalisisDB(c, idAnalysis, EvaluatorUtils.getDocList(), originAnnexes);
					final String methodology = ObservatorioDAO.getMethodology(c, Long.parseLong(executionId));
					final ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false, true);
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
			Logger.putLog("Error en getGlobalResultData", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		CacheUtils.putInCacheForever(observatoryEvaluationList, Constants.OBSERVATORY_KEY_CACHE + executionId);
		// Filter by category or complexity
		if (!isComplexityFilter) {
			return filterObservatoriesByCategory(observatoryEvaluationList, Long.parseLong(executionId), categoryId);
		} else {
			return filterObservatoriesByComplexity(observatoryEvaluationList, Long.parseLong(executionId), categoryId);
		}
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
	private static List<ObservatoryEvaluationForm> filterObservatoriesByCategory(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId, final long categoryId)
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
				Logger.putLog("Error al filtrar observatorios. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			return results;
		}
	}

	/**
	 * Filter observatories by complexity.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @param executionId               the execution id
	 * @return the list
	 * @throws Exception the exception
	 */
	public static List<ObservatoryEvaluationForm> filterObservatoriesByFixed(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId) throws Exception {
		final List<ObservatoryEvaluationForm> results = new ArrayList<>();
		try (Connection conn = DataBaseManager.getConnection()) {
			final List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIds(conn, executionId, Constants.COMPLEXITY_SEGMENT_NONE);
			for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
				if (listExecutionsIds.contains(observatoryEvaluationForm.getCrawlerExecutionId())) {
					results.add(observatoryEvaluationForm);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al filtrar observatorios. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Filter observatories by complexity.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @param executionId               the execution id
	 * @param complexityId              the complexity id
	 * @return the list
	 * @throws Exception the exception
	 */
	private static List<ObservatoryEvaluationForm> filterObservatoriesByComplexity(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId, final Long complexityId)
			throws Exception {
		if (complexityId == Constants.COMPLEXITY_SEGMENT_NONE) {
			return observatoryEvaluationList;
		} else {
			final List<ObservatoryEvaluationForm> results = new ArrayList<>();
			try (Connection conn = DataBaseManager.getConnection()) {
				final List<Long> listExecutionsIds = RastreoDAO.getExecutionObservatoryCrawlerIdsComplexity(conn, executionId, complexityId);
				for (ObservatoryEvaluationForm observatoryEvaluationForm : observatoryEvaluationList) {
					if (listExecutionsIds.contains(observatoryEvaluationForm.getCrawlerExecutionId())) {
						results.add(observatoryEvaluationForm);
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al filtrar observatorios por complejidad. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
					Logger.putLog("Error al ordenar fechas de evolución.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * Calculate segment evolution puntuation data set.
	 *
	 * @param aspect          the aspect
	 * @param resultsByAspect the results by aspect
	 * @param dateFormat      the date format
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateSegmentEvolutionPuntuationDataSet(final String aspect, final Map<Date, Map<String, BigDecimal>> resultsByAspect, final DateFormat dateFormat) {
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
					Logger.putLog("Error al ordenar fechas de evolución.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
					Logger.putLog("Error al ordenar fechas de evolución.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * Calculate verification evolution compliance data set.
	 *
	 * @param verification the verification
	 * @param result       the result
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculateVerificationEvolutionComplianceDataSet(final String verification, final Map<Date, List<ObservatoryEvaluationForm>> result) {
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
					Logger.putLog("Error al ordenar fechas de evolución.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : result.entrySet()) {
			// Para un observatorio en concreto recuperamos la puntuación de una
			// verificación
			final Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(entry.getValue(), Constants.OBS_PRIORITY_NONE);
			final BigDecimal value = generatePercentajesCompilanceVerification(results).get(verification.concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX));
			if (value != null) {
				resultData.put(df.format(entry.getKey()), value);
			} else {
				resultData.put(df.format(entry.getKey()), BigDecimal.ZERO);
			}
		}
		return resultData;
	}

	/**
	 * Calculate verification evolution compliance data set.
	 *
	 * @param verifications the verifications
	 * @param result        the result
	 * @return the map
	 */
	public static Map<String, Map<String, BigDecimal>> calculateVerificationEvolutionComplianceDataSetDetailed(final List<String> verifications,
			final Map<Date, List<ObservatoryEvaluationForm>> result) {
		final TreeMap<String, Map<String, BigDecimal>> resultData = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				final PropertiesManager pmgr = new PropertiesManager();
				final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
				try {
					final Date fecha1 = new Date(df.parse(o1).getTime());
					final Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.evolution"));
//		TreeMap<String, BigDecimal> resultC = new TreeMap<>();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : result.entrySet()) {
			TreeMap<String, BigDecimal> resultC = new TreeMap<>();
			for (String verification : verifications) {
				// Para un observatorio en concreto recuperamos la puntuación de una
				// verificación
				final Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(entry.getValue(), Constants.OBS_PRIORITY_NONE);
				// C
				final Map<String, BigDecimal> generatePercentajesCompilanceVerification = generatePercentajesCompilanceVerification(results);
				BigDecimal value = generatePercentajesCompilanceVerification.get(verification.concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX));
				if (value != null) {
					resultC.put(verification.concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX), value);
				} else {
					resultC.put(verification.concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX), BigDecimal.ZERO);
				}
				// NC
				value = generatePercentajesCompilanceVerification.get(verification.concat(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX));
				if (value != null) {
					resultC.put(verification.concat(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX), value);
				} else {
					resultC.put(verification.concat(Constants.OBS_VALUE_NO_COMPILANCE_SUFFIX), BigDecimal.ZERO);
				}
				// NA
				value = generatePercentajesCompilanceVerification.get(verification.concat(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX));
				if (value != null) {
					resultC.put(verification.concat(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX), value);
				} else {
					resultC.put(verification.concat(Constants.OBS_VALUE_NO_APPLY_COMPLIANCE_SUFFIX), BigDecimal.ZERO);
				}
				resultData.put(df.format(entry.getKey()), resultC);
			}
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
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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

	/**
	 * Calculate percentage approval site compliance.
	 *
	 * @param result           the result
	 * @param suitabilityLevel the suitability level
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculatePercentageApprovalSiteCompliance(final Map<Date, Map<Long, Map<String, Integer>>> result, final String suitabilityLevel) {
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
		return calculatePercentageApprovalSiteCompliance(result, suitabilityLevel, df);
	}

	/**
	 * Calculate percentage approval site compliance.
	 *
	 * @param result           the result
	 * @param suitabilityLevel the suitability level
	 * @param dateFormat       the date format
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculatePercentageApprovalSiteCompliance(final Map<Date, Map<Long, Map<String, Integer>>> result, final String suitabilityLevel,
			final DateFormat dateFormat) {
		final TreeMap<String, BigDecimal> percentagesMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				try {
					final Date fecha1 = new Date(dateFormat.parse(o1).getTime());
					final Date fecha2 = new Date(dateFormat.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		for (Map.Entry<Date, Map<Long, Map<String, Integer>>> dateMapEntry : result.entrySet()) {
			BigDecimal percentage = BigDecimal.ZERO;
			for (Map.Entry<Long, Map<String, Integer>> longMapEntry : dateMapEntry.getValue().entrySet()) {
				int totalSites = longMapEntry.getValue().get(Constants.OBS_COMPILANCE_FULL) + longMapEntry.getValue().get(Constants.OBS_COMPILANCE_PARTIAL)
						+ longMapEntry.getValue().get(Constants.OBS_COMPILANCE_NONE);
				if (totalSites > 0) {
					int numSites = longMapEntry.getValue().get(suitabilityLevel);
					if (numSites > 0) {
						percentage = (new BigDecimal(numSites)).divide(new BigDecimal(totalSites), 2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_HUNDRED);
					}
				}
			}
			percentagesMap.put(dateFormat.format(dateMapEntry.getKey()), percentage);
		}
		return percentagesMap;
	}

	/**
	 * Calculate percentage approval site level.
	 *
	 * @param result           the result
	 * @param suitabilityLevel the suitability level
	 * @param dateFormat       the date format
	 * @return the map
	 */
	public static Map<String, BigDecimal> calculatePercentageApprovalSiteLevel2(final Map<Date, Map<Long, Map<String, Integer>>> result, final String suitabilityLevel, final DateFormat dateFormat) {
		final TreeMap<String, BigDecimal> percentagesMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				try {
					final Date fecha1 = new Date(dateFormat.parse(o1).getTime());
					final Date fecha2 = new Date(dateFormat.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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

	/**
	 * Gets the evolution observatories sites by type.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param result        the result
	 * @param exObsIds      the ex obs ids
	 * @return the evolution observatories sites by type
	 */
	public static Map<Date, Map<Long, Map<String, Integer>>> getEvolutionObservatoriesSitesByType(final String observatoryId, final String executionId,
			final Map<Date, List<ObservatoryEvaluationForm>> result, String[] exObsIds) {
		final Map<Date, Map<Long, Map<String, Integer>>> resultData = new HashMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, Long.parseLong(observatoryId));
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, Long.parseLong(observatoryId), Long.parseLong(executionId),
					observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> longDateEntry : executedObservatoryIdMap.entrySet()) {
				if (exObsIds != null) {
					List<String> list = Arrays.asList(exObsIds);
					// Only add selected observatories
					if (list.contains((String.valueOf(longDateEntry.getKey())))) {
						final List<ObservatoryEvaluationForm> pageList = result.get(longDateEntry.getValue());
						final Map<Long, Map<String, Integer>> sites = getSitesByType(pageList);
						resultData.put(longDateEntry.getValue(), sites);
					}
				} else {
					final List<ObservatoryEvaluationForm> pageList = result.get(longDateEntry.getValue());
					final Map<Long, Map<String, Integer>> sites = getSitesByType(pageList);
					resultData.put(longDateEntry.getValue(), sites);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return resultData;
	}

	/**
	 * Gets the evolution observatories sites by compliance.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param result        the result
	 * @param exObsIds      the ex obs ids
	 * @return the evolution observatories sites by compliance
	 */
	public static Map<Date, Map<Long, Map<String, Integer>>> getEvolutionObservatoriesSitesByCompliance(final String observatoryId, final String executionId,
			final Map<Date, List<ObservatoryEvaluationForm>> result, String[] exObsIds) {
		final Map<Date, Map<Long, Map<String, Integer>>> resultData = new HashMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, Long.parseLong(observatoryId));
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, Long.parseLong(observatoryId), Long.parseLong(executionId),
					observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> longDateEntry : executedObservatoryIdMap.entrySet()) {
				if (exObsIds != null) {
					List<String> list = Arrays.asList(exObsIds);
					// Only add selected observatories
					if (list.contains((String.valueOf(longDateEntry.getKey())))) {
						final List<ObservatoryEvaluationForm> pageList = result.get(longDateEntry.getValue());
						final Map<Long, Map<String, Integer>> sites = new TreeMap<>();
						sites.put(longDateEntry.getKey(), getSityesByCompliance(getVerificationResultsByPointAndCrawl(pageList, Constants.OBS_PRIORITY_NONE)));
						resultData.put(longDateEntry.getValue(), sites);
					}
				} else {
					final List<ObservatoryEvaluationForm> pageList = result.get(longDateEntry.getValue());
					final Map<Long, Map<String, Integer>> sites = new TreeMap<>();
					sites.put(longDateEntry.getKey(), getSityesByCompliance(getVerificationResultsByPointAndCrawl(pageList, Constants.OBS_PRIORITY_NONE)));
					resultData.put(longDateEntry.getValue(), sites);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return resultData;
	}

	/**
	 * Result evolution data.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param tagsFilter    the tags filter
	 * @param exObsIds      the ex obs ids
	 * @return the map -1026.t1.b2-
	 */
	public static Map<Date, List<ObservatoryEvaluationForm>> resultEvolutionData(final Long observatoryId, final Long executionId, String[] tagsFilter, String[] exObsIds) {
		final Map<Date, List<ObservatoryEvaluationForm>> resultData = new TreeMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, observatoryId);
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatoryId, executionId, observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> entry : executedObservatoryIdMap.entrySet()) {
				if (exObsIds != null) {
					List<String> list = Arrays.asList(exObsIds);
					// Only add selected observatories
					if (list.contains((String.valueOf(entry.getKey())))) {
						String[] executionTags = getExecutionTags(entry.getKey());
						if (Objects.isNull(executionTags)) {
							executionTags = tagsFilter;
						}
						final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE, null, false, executionTags);
						resultData.put(entry.getValue(), pageList);
					}
				} else {
					final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE, null, false, tagsFilter);
					resultData.put(entry.getValue(), pageList);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return resultData;
	}

	/**
	 * Result evolution data.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param categoryId    the category id
	 * @param tagsFilter    the tags filter
	 * @param exObsIds      the ex obs ids
	 * @return the map
	 */
	public static Map<Date, List<ObservatoryEvaluationForm>> resultEvolutionCategoryData(final Long observatoryId, final Long executionId, final Long categoryId, String[] tagsFilter,
			String[] exObsIds) {
		final Map<Date, List<ObservatoryEvaluationForm>> resultData = new TreeMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, observatoryId);
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatoryId, executionId, observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> entry : executedObservatoryIdMap.entrySet()) {
				if (exObsIds != null) {
					List<String> list = Arrays.asList(exObsIds);
					// Only add selected observatories
					if (list.contains((String.valueOf(entry.getKey())))) {
						final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), categoryId, null, false, tagsFilter);
						resultData.put(entry.getValue(), pageList);
					}
				} else {
					final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), categoryId, null, false, tagsFilter);
					resultData.put(entry.getValue(), pageList);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return resultData;
	}

	/**
	 * Result evolution data.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param tagsFilter    the tags filter
	 * @param exObsIds      the ex obs ids
	 * @return the map
	 */
	public static Map<Date, List<ObservatoryEvaluationForm>> resultEvolutionDataComplexity(final Long observatoryId, final Long executionId, String[] tagsFilter, String[] exObsIds) {
		final Map<Date, List<ObservatoryEvaluationForm>> resultData = new TreeMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, observatoryId);
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatoryId, executionId, observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> entry : executedObservatoryIdMap.entrySet()) {
				if (exObsIds != null) {
					List<String> list = Arrays.asList(exObsIds);
					// Only add selected observatories
					if (list.contains((String.valueOf(entry.getKey())))) {
						final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE, null, true, tagsFilter);
						resultData.put(entry.getValue(), pageList);
					}
				} else {
					final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE, null, true, tagsFilter);
					resultData.put(entry.getValue(), pageList);
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return resultData;
	}

	/**
	 * Result evolution data.
	 *
	 * @param observatoryId the observatory id
	 * @param executionId   the execution id
	 * @param categoryId    the category id
	 * @param tagsFilter    the tags filter
	 * @return the map
	 */
	public static Map<Date, List<ObservatoryEvaluationForm>> resultEvolutionCategoryDataComplexity(final Long observatoryId, final Long executionId, final Long categoryId, String[] tagsFilter) {
		final Map<Date, List<ObservatoryEvaluationForm>> resultData = new TreeMap<>();
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, observatoryId);
			final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatoryId, executionId, observatoryForm.getCartucho().getId());
			for (Map.Entry<Long, Date> entry : executedObservatoryIdMap.entrySet()) {
				final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), categoryId, null, true, tagsFilter);
				resultData.put(entry.getValue(), pageList);
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
			// Si nos pasamos de 100, restamos 1 al valor actual
			if (totalPercentage.compareTo(BIG_DECIMAL_HUNDRED) > 0) {
				BigDecimal subtract = result.get(fitResultKey).subtract(totalPercentage.subtract(BIG_DECIMAL_HUNDRED));
				// Correción por si ocurre que el valor de ajuste es cero para
				// que no se devuelvan negativos
				result.put(fitResultKey, subtract.compareTo(BigDecimal.ZERO) > 0 ? subtract : BigDecimal.ZERO);
			}
		}
		return result;
	}

	/**
	 * Gets the sityes by compliance.
	 *
	 * @param results the results
	 * @return the sityes by compliance
	 */
	public static Map<String, Integer> getSityesByCompliance(Map<Long, Map<String, BigDecimal>> results) {
		final Map<String, Integer> resultCompilance = new TreeMap<>();
		// Process results
		int totalC = 0;
		int totalPC = 0;
		int totalNC = 0;
		for (Map.Entry<Long, Map<String, BigDecimal>> result : results.entrySet()) {
			int countC = 0;
			int countNC = 0;
			int countNA = 0;
			for (Map.Entry<String, BigDecimal> verificationResult : result.getValue().entrySet()) {
				if (verificationResult.getValue().compareTo(new BigDecimal(9)) >= 0) {
					countC++;
				} else if (verificationResult.getValue().compareTo(new BigDecimal(0)) >= 0) {
					countNC++;
				} else {
					countNA++;
				}
			}
			if ((countC + countNA) == result.getValue().size()) {
				totalC++;
			} else if (countC > countNC) {
				totalPC++;
			} else {
				totalNC++;
			}
		}
		resultCompilance.put(Constants.OBS_COMPILANCE_NONE, totalNC);
		resultCompilance.put(Constants.OBS_COMPILANCE_PARTIAL, totalPC);
		resultCompilance.put(Constants.OBS_COMPILANCE_FULL, totalC);
		return resultCompilance;
	}

	/**
	 * 
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
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
			GraphicsUtils.createPieChart(dataSet, title, messageResources.getMessage("observatory.graphic.site.number"), total, filePath, noDataMess,
					pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), x, y);
		}
		graphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG, infoGlobalAccessibilityLevel(messageResources, result));
		infoGlobalAccessibilityLevel(messageResources, result);
	}

	/**
	 * Gets the global compilance graphic.
	 *
	 * @param messageResources  the message resources
	 * @param pageExecutionList the page execution list
	 * @param graphics          the graphics
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param regenerate        the regenerate
	 * @return the global compilance graphic
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getGlobalCompilanceGraphic(final MessageResources messageResources, final List<ObservatoryEvaluationForm> pageExecutionList, final Map<String, Object> graphics,
			final String title, final String filePath, final String noDataMess, final boolean regenerate) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final File file = new File(filePath);
		Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		final Map<String, Integer> resultCompilance = getComplinaceMap(results);
		if (!file.exists() || regenerate) {
			// Process results
			final int total = results.size();
			final DefaultPieDataset dataSet = new DefaultPieDataset();
//			dataSet.setValue(Constants.OBS_COMPILANCE_NONE, totalNC);
//			dataSet.setValue(Constants.OBS_COMPILANCE_PARTIAL, totalPC);
//			dataSet.setValue(Constants.OBS_COMPILANCE_FULL, totalC);
			dataSet.setValue(Constants.OBS_COMPILANCE_NONE, resultCompilance.get(Constants.OBS_COMPILANCE_NONE));
			dataSet.setValue(Constants.OBS_COMPILANCE_PARTIAL, resultCompilance.get(Constants.OBS_COMPILANCE_PARTIAL));
			dataSet.setValue(Constants.OBS_COMPILANCE_FULL, resultCompilance.get(Constants.OBS_COMPILANCE_FULL));
			GraphicsUtils.createPieChart(dataSet, title, messageResources.getMessage("observatory.graphic.site.number"), total, filePath, noDataMess,
					pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), x, y);
		}
		graphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DCG, infoGlobalCompilanceLevel(messageResources, resultCompilance));
		infoGlobalCompilanceLevel(messageResources, resultCompilance);
	}

	/**
	 * Gets the complinace map.
	 *
	 * @param results the results
	 * @return the complinace map
	 */
	public static Map<String, Integer> getComplinaceMap(Map<Long, Map<String, BigDecimal>> results) {
		final Map<String, Integer> resultCompilance = new TreeMap<>();
		int totalC = 0;
		int totalPC = 0;
		int totalNC = 0;
		for (Map.Entry<Long, Map<String, BigDecimal>> result : results.entrySet()) {
			int countC = 0;
			int countNC = 0;
			int countNA = 0;
			for (Map.Entry<String, BigDecimal> verificationResult : result.getValue().entrySet()) {
				if (verificationResult.getValue().compareTo(new BigDecimal(9)) >= 0) {
					countC++;
				} else if (verificationResult.getValue().compareTo(new BigDecimal(0)) >= 0) {
					countNC++;
				} else {
					countNA++;
				}
			}
			if ((countC + countNA) == result.getValue().size()) {
				totalC++;
			} else if (countC > countNC) {
				totalPC++;
			} else {
				totalNC++;
			}
		}
		resultCompilance.put(Constants.OBS_COMPILANCE_NONE, totalNC);
		resultCompilance.put(Constants.OBS_COMPILANCE_PARTIAL, totalPC);
		resultCompilance.put(Constants.OBS_COMPILANCE_FULL, totalC);
		return resultCompilance;
	}

	/**
	 * Gets the segment compilance graphic.
	 *
	 * @param messageResources  the message resources
	 * @param pageExecutionList the page execution list
	 * @param graphics          the graphics
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param regenerate        the regenerate
	 * @return the segment compilance graphic
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void getSegmentCompilanceGraphic(final MessageResources messageResources, final List<ObservatoryEvaluationForm> pageExecutionList, final Map<String, Object> graphics,
			final String title, final String filePath, final String noDataMess, final boolean regenerate) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final File file = new File(filePath);
		Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		final Map<String, Integer> resultCompilance = new TreeMap<>();
		if (!file.exists() || regenerate) {
			// Process results
			final int total = results.size();
			int totalC = 0;
			int totalPC = 0;
			int totalNC = 0;
			for (Map.Entry<Long, Map<String, BigDecimal>> result : results.entrySet()) {
				int countC = 0;
				int countNC = 0;
				int countNA = 0;
				for (Map.Entry<String, BigDecimal> verificationResult : result.getValue().entrySet()) {
					if (verificationResult.getValue().compareTo(new BigDecimal(9)) >= 0) {
						countC++;
					} else if (verificationResult.getValue().compareTo(new BigDecimal(0)) >= 0) {
						countNC++;
					} else {
						countNA++;
					}
				}
				if ((countC + countNA) == result.getValue().size()) {
					totalC++;
				} else if ((countC + countNA) > countNC) {
					totalPC++;
				} else {
					totalNC++;
				}
			}
			resultCompilance.put(Constants.OBS_COMPILANCE_NONE, totalNC);
			resultCompilance.put(Constants.OBS_COMPILANCE_PARTIAL, totalPC);
			resultCompilance.put(Constants.OBS_COMPILANCE_FULL, totalC);
			final DefaultPieDataset dataSet = new DefaultPieDataset();
			dataSet.setValue(Constants.OBS_COMPILANCE_NONE, totalNC);
			dataSet.setValue(Constants.OBS_COMPILANCE_PARTIAL, totalPC);
			dataSet.setValue(Constants.OBS_COMPILANCE_FULL, totalC);
			GraphicsUtils.createPieChart(dataSet, title, messageResources.getMessage("observatory.graphic.site.number"), total, filePath, noDataMess,
					pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"), x, y);
		}
		graphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_DAG, infoGlobalCompilanceLevel(messageResources, resultCompilance));
		infoGlobalCompilanceLevel(messageResources, resultCompilance);
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
	 * Creates the graphics map complexities.
	 *
	 * @param complexities the complexities
	 * @return the map
	 */
	public static Map<Integer, List<ComplejidadForm>> createGraphicsMapComplexities(final List<ComplejidadForm> complexities) {
		final Map<Integer, List<ComplejidadForm>> resultLists = new TreeMap<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int numBarByGrapg = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.max.bar.graph"));
		int keyMap = 1;
		for (int i = 1; i <= complexities.size(); i++) {
			final List<ComplejidadForm> list;
			if (resultLists.get(keyMap) != null) {
				list = resultLists.get(keyMap);
			} else {
				list = new ArrayList<>();
			}
			list.add(complexities.get(i - 1));
			resultLists.put(keyMap, list);
			if ((i >= numBarByGrapg) && (i % numBarByGrapg == 0) && (complexities.size() != i + 1)) {
				keyMap++;
			}
		}
		return resultLists;
	}

	/**
	 * Creates the graphics map ambits.
	 *
	 * @param ambits the ambits
	 * @return the map
	 */
	public static Map<Integer, List<AmbitoForm>> createGraphicsMapAmbits(final List<AmbitoForm> ambits) {
		final Map<Integer, List<AmbitoForm>> resultLists = new TreeMap<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final int numBarByGrapg = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.max.bar.graph"));
		int keyMap = 1;
		for (int i = 1; i <= ambits.size(); i++) {
			final List<AmbitoForm> list;
			if (resultLists.get(keyMap) != null) {
				list = resultLists.get(keyMap);
			} else {
				list = new ArrayList<>();
			}
			list.add(ambits.get(i - 1));
			resultLists.put(keyMap, list);
			if ((i >= numBarByGrapg) && (i % numBarByGrapg == 0) && (ambits.size() != i + 1)) {
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
	 * @param tagsFilter        the tags filter
	 * @return the global mark by segment graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalMarkBySegmentGraphic(final MessageResources messageResources, final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			Map<String, Object> globalGraphics, final String title, final String filePath, final String noDataMess, final List<CategoriaForm> categories, final boolean regenerate, String[] tagsFilter)
			throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		final Map<Integer, List<CategoriaForm>> resultLists = createGraphicsMap(categories);
		final List<CategoryViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<CategoriaForm, Map<String, BigDecimal>> resultDataBySegment = calculateMidPuntuationResultsBySegmentMap(executionId, pageExecutionList, resultLists.get(i), tagsFilter);
			if (!file.exists() || regenerate) {
				final ChartForm observatoryGraphicsForm = new ChartForm(createDataSet(resultDataBySegment, messageResources), true, true, false, false, true, false, false, x, y,
						pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				observatoryGraphicsForm.setTitle(title);
				GraphicsUtils.createSeriesBarChart(observatoryGraphicsForm, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg", noDataMess, messageResources, true);
			}
			for (CategoriaForm category : resultLists.get(i)) {
				final CategoryViewListForm categoryView = new CategoryViewListForm(category, infoComparisionAllocation(messageResources, resultDataBySegment.get(category)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CAS, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CAS_GRAPH, resultLists.size());
	}

	/**
	 * Gets the global mark by complexitivity graphic.
	 *
	 * @param messageResources  the message resources
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param globalGraphics    the global graphics
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param complexitivities  the complexitivities
	 * @param regenerate        the regenerate
	 * @param tagsFilter        the tags filter
	 * @return the global mark by complexitivity graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalMarkByComplexitivityGraphic(final MessageResources messageResources, final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			Map<String, Object> globalGraphics, final String title, final String filePath, final String noDataMess, final List<ComplejidadForm> complexitivities, final boolean regenerate,
			String[] tagsFilter) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		final Map<Integer, List<ComplejidadForm>> resultLists = createGraphicsMapComplexities(complexitivities);
		final List<ComplexityViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultDataBySegment = calculateMidPuntuationResultsByComplexitivityMap(executionId, pageExecutionList, resultLists.get(i), tagsFilter);
			if (!file.exists() || regenerate) {
				final ChartForm observatoryGraphicsForm = new ChartForm(createDataSetComplexity(resultDataBySegment, messageResources), true, true, false, false, true, false, false, x, y,
						pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				observatoryGraphicsForm.setTitle(title);
				GraphicsUtils.createSeriesBarChart(observatoryGraphicsForm, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg", noDataMess, messageResources, true);
			}
			for (ComplejidadForm complexitivity : resultLists.get(i)) {
				final ComplexityViewListForm categoryView = new ComplexityViewListForm(complexitivity, infoComparisionAllocation(messageResources, resultDataBySegment.get(complexitivity)));
				categoriesLabels.add(categoryView);
			}
		}
		globalGraphics.put(Constants.OBSERVATORY_GRAPHIC_GLOBAL_DATA_LIST_CASC, categoriesLabels);
		globalGraphics.put(Constants.OBSERVATORY_NUM_CASC_GRAPH, resultLists.size());
	}

	/**
	 * Gets the global mark by complexity graphic.
	 *
	 * @param messageResources  the message resources
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param globalGraphics    the global graphics
	 * @param title             the title
	 * @param filePath          the file path
	 * @param noDataMess        the no data mess
	 * @param complexities      the complexities
	 * @param tagsFilter        the tags filter
	 * @return the global mark by complexity graphic
	 * @throws Exception the exception
	 */
	public static void getGlobalMarkByComplexityGraphic(final MessageResources messageResources, final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			Map<String, Object> globalGraphics, final String title, final String filePath, final String noDataMess, final List<ComplejidadForm> complexities, String[] tagsFilter) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		final Map<Integer, List<ComplejidadForm>> resultLists = createGraphicsMapComplexities(complexities);
		final List<ComplexityViewListForm> categoriesLabels = new ArrayList<>();
		for (int i = 1; i <= resultLists.size(); i++) {
			final File file = new File(filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg");
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultDataBySegment = calculateMidPuntuationResultsByComplecityMap(executionId, pageExecutionList, resultLists.get(i), tagsFilter);
			if (!file.exists()) {
				final ChartForm observatoryGraphicsForm = new ChartForm(createDataSetComplexity(resultDataBySegment, messageResources), true, true, false, false, true, false, false, x, y,
						pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
				GraphicsUtils.createSeriesBarChart(observatoryGraphicsForm, filePath.substring(0, filePath.indexOf(".jpg")) + i + ".jpg", noDataMess, messageResources, true);
			}
			for (ComplejidadForm complexity : resultLists.get(i)) {
				final ComplexityViewListForm categoryView = new ComplexityViewListForm(complexity, infoComparisionAllocation(messageResources, resultDataBySegment.get(complexity)));
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
	 * @throws IOException
	 */
	public static Map<String, Integer> getResultsBySiteLevel(final List<ObservatoryEvaluationForm> observatoryEvaluationList) throws IOException {
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
	 * Gets the compilance results by site level.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @return the compilance results by site level
	 */
	public static Map<String, Integer> getResultsBySiteCompilance(final List<ObservatoryEvaluationForm> observatoryEvaluationList) {
		final Map<String, Integer> resultCompilance = new TreeMap<>();
		resultCompilance.put(Constants.OBS_COMPILANCE_NONE, 0);
		resultCompilance.put(Constants.OBS_COMPILANCE_PARTIAL, 0);
		resultCompilance.put(Constants.OBS_COMPILANCE_FULL, 0);
		// Resultados del segmento (ya vienen filtrados)
		Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(observatoryEvaluationList, Constants.OBS_PRIORITY_NONE);
		return getComplinaceMap(results);
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
				} else if (value == Constants.OBS_VALUE_RED_ZERO) {
					partialResultsMap.get(lvb.getLabel()).add(BigDecimal.ZERO);
				} else if (value == Constants.OBS_VALUE_GREEN_ZERO) {
					// 0.5PASA specific for this cartidge
					partialResultsMap.get(lvb.getLabel()).add(new BigDecimal(0.5d));
				}
			}
			// Hemos recorrido las verificaciones del aspecto, ahora calculamos
			// la media de cada una
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
			// Teniendo las medias de verificaciones de un aspecto, pasamos a
			// calcular el valor medio de ese aspecto
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
		final Map<String, BigDecimal> resultsSorted = new LinkedHashMap<String, BigDecimal>();
		resultsSorted.put(messageResources.getMessage("resultados.anonimos.general"), results.get(messageResources.getMessage("resultados.anonimos.general")));
		resultsSorted.put(messageResources.getMessage("resultados.anonimos.presentacion"), results.get(messageResources.getMessage("resultados.anonimos.presentacion")));
		resultsSorted.put(messageResources.getMessage("resultados.anonimos.estructura"), results.get(messageResources.getMessage("resultados.anonimos.estructura")));
		resultsSorted.put(messageResources.getMessage("resultados.anonimos.navegacion"), results.get(messageResources.getMessage("resultados.anonimos.navegacion")));
		resultsSorted.put(messageResources.getMessage("resultados.anonimos.alternativa"), results.get(messageResources.getMessage("resultados.anonimos.alternativa")));
		return resultsSorted;
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
		final Map<String, BigDecimal> results = new TreeMap<>();
		final Map<String, Integer> numPoint = new LinkedHashMap<>();
		for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
			for (ObservatoryLevelForm observatoryLevelForm : observatoryEvaluationForm.getGroups()) {
				if (level.equals(Constants.OBS_PRIORITY_NONE) || observatoryLevelForm.getName().equals(level)) {
					for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
						for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
							// Se comprueba si puntúa o no puntúa
							if (observatorySubgroupForm.getValue() != Constants.OBS_VALUE_NOT_SCORE) {
								if (results.get(observatorySubgroupForm.getDescription()) == null) {
									results.put(observatorySubgroupForm.getDescription(), new BigDecimal(0));
									numPoint.put(observatorySubgroupForm.getDescription(), 0);
								}
								// Si puntúa, se isNombreValido si se le da un 0
								// o un 1
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ZERO) {// 0.5
									// Si le damos un 1, lo añadimos a la
									// puntuación e incrementamos el número de
									// puntos que han puntuado
									results.put(observatorySubgroupForm.getDescription(), results.get(observatorySubgroupForm.getDescription()).add(new BigDecimal(0.5)));
									if (numPoint.get(observatorySubgroupForm.getDescription()) == -1) {
										numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 2);
									} else {
										numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
									}
								} else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
									// Si le damos un 1, lo añadimos a la
									// puntuación e incrementamos el número de
									// puntos que han puntuado
									results.put(observatorySubgroupForm.getDescription(), results.get(observatorySubgroupForm.getDescription()).add(new BigDecimal(1)));
									if (numPoint.get(observatorySubgroupForm.getDescription()) == -1) {
										numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 2);
									} else {
										numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
									}
								} else {
									// Si le damos un 0 solamente incrementamos
									// el número de puntos
									numPoint.put(observatorySubgroupForm.getDescription(), numPoint.get(observatorySubgroupForm.getDescription()) + 1);
								}
							} else if (results.get(observatorySubgroupForm.getDescription()) == null) {
								results.put(observatorySubgroupForm.getDescription(), new BigDecimal(0));
								numPoint.put(observatorySubgroupForm.getDescription(), -1);
							}
						}
					}
				}
			}
		}
		// Cambiamos las claves por el nombre y calculamos la media
		// Necesitamos implementar un orden específico para que p.e.
		// 1.10 vaya después de 1.9 y no de 1.
		final Map<String, BigDecimal> verificationResultsByPoint = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String version1, String version2) {
				String[] v1 = version1.split("\\.");
				String[] v2 = version2.split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[0]);
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(version[1]) : 0;
			}
		});
		for (Map.Entry<String, BigDecimal> resultEntry : results.entrySet()) {
			// Generar las claves con minhap.observatory.5_0.subgroup.
			final String name = resultEntry.getKey().substring(resultEntry.getKey().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
			final BigDecimal value;
			if (numPoint.get(resultEntry.getKey()) != -1 && numPoint.get(resultEntry.getKey()) != 0) {
				value = resultEntry.getValue().divide(BigDecimal.valueOf(numPoint.get(resultEntry.getKey())), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN);
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
		Map<String, BigDecimal> orderedResults = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String version1, String version2) {
				String[] v1 = version1.split("\\.");
				String[] v2 = version2.split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					if (minor(v1) == minor(v2)) { // Devolvemos 1
													// aunque sean iguales
													// porque las claves lleva
													// asociado un subfijo que
													// aqui no tenemos en cuenta
						return 1;
					}
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[4].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""));
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(version[5].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")) : 0;
			}
		});
		for (Map.Entry<String, BigDecimal> entry : results.entrySet()) {
			orderedResults.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, BigDecimal> stringBigDecimalEntry : orderedResults.entrySet()) {
			results.put(stringBigDecimalEntry.getKey(), stringBigDecimalEntry.getValue().divide(new BigDecimal(resultData.size()), 2, BigDecimal.ROUND_HALF_UP).multiply(BIG_DECIMAL_HUNDRED));
		}
		return results;
	}

	/**
	 * Gets the compilance verification results by point.
	 *
	 * @param resultData the result data
	 * @param level      the level
	 * @return the compilance verification results by point
	 */
	public static Map<String, BigDecimal> getCompilanceVerificationResultsByPoint(final List<ObservatoryEvaluationForm> resultData, final String level) {
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
		Map<String, BigDecimal> orderedResults = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String version1, String version2) {
				String[] v1 = version1.split("\\.");
				String[] v2 = version2.split("\\.");
				int major1 = major(v1);
				int major2 = major(v2);
				if (major1 == major2) {
					if (minor(v1) == minor(v2)) { // Devolvemos 1
													// aunque sean iguales
													// porque las claves lleva
													// asociado un subfijo que
													// aqui no tenemos en cuenta
						return 1;
					}
					return minor(v1).compareTo(minor(v2));
				}
				return major1 > major2 ? 1 : -1;
			}

			private int major(String[] version) {
				return Integer.parseInt(version[4].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""));
			}

			private Integer minor(String[] version) {
				return version.length > 1 ? Integer.parseInt(version[5].replace(Constants.OBS_VALUE_RED_SUFFIX, "").replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")) : 0;
			}
		});
		for (Map.Entry<String, BigDecimal> entry : results.entrySet()) {
			orderedResults.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, BigDecimal> stringBigDecimalEntry : orderedResults.entrySet()) {
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
		final int maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
		// Se recorren las páginas de cada observatorio
		if (observatoryEvaluationList != null && !observatoryEvaluationList.isEmpty()) {
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
								// Reduce 1 fail max on AA
								if (numZeroRed > (maxFails - 1)) {
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
		}
		return globalResult;
	}

	/**
	 * Gets the page puntuation.
	 *
	 * @param observatoryEvaluationList the observatory evaluation list
	 * @return the page puntuation
	 */
	public static Map<String, List<ObservatoryEvaluationForm>> getPagePuntuation(final List<ObservatoryEvaluationForm> observatoryEvaluationList) {
		final Map<String, List<ObservatoryEvaluationForm>> globalResult = new HashMap<>();
		globalResult.put(Constants.OBS_NV, new ArrayList<ObservatoryEvaluationForm>());
		globalResult.put(Constants.OBS_A, new ArrayList<ObservatoryEvaluationForm>());
		globalResult.put(Constants.OBS_AA, new ArrayList<ObservatoryEvaluationForm>());
		final PropertiesManager pmgr = new PropertiesManager();
		final int maxFails = Integer.parseInt(pmgr.getValue("intav.properties", "observatory.zero.red.max.number.2017"));
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
	 * @throws IOException
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
	 * Gets the verification results by point and crawl.
	 *
	 * @param resultData the result data
	 * @param level      the level
	 * @return the verification results by point and crawl
	 */
	public static Map<Long, Map<String, BigDecimal>> getVerificationResultsByPointAndCrawl(final List<ObservatoryEvaluationForm> resultData, final String level) {
		final Map<Long, Map<String, BigDecimal>> groupedResults = new TreeMap<>();
		final Map<Long, Map<String, Integer>> groupedNumPoint = new LinkedHashMap<>();
		if (resultData != null && !resultData.isEmpty()) {
			for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
				if (!groupedResults.containsKey(observatoryEvaluationForm.getCrawlerExecutionId())) {
					groupedResults.put(observatoryEvaluationForm.getCrawlerExecutionId(), new TreeMap<String, BigDecimal>());
				}
				if (!groupedNumPoint.containsKey(observatoryEvaluationForm.getCrawlerExecutionId())) {
					groupedNumPoint.put(observatoryEvaluationForm.getCrawlerExecutionId(), new TreeMap<String, Integer>());
				}
				for (ObservatoryLevelForm observatoryLevelForm : observatoryEvaluationForm.getGroups()) {
					if (level.equals(Constants.OBS_PRIORITY_NONE) || observatoryLevelForm.getName().equals(level)) {
						for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
							for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
								// Se comprueba si puntúa o no puntúa
								Map<String, BigDecimal> currentResults = groupedResults.get(observatoryEvaluationForm.getCrawlerExecutionId());
								Map<String, Integer> currentNumPoint = groupedNumPoint.get(observatoryEvaluationForm.getCrawlerExecutionId());
								// Se comprueba si puntúa o no puntúa
								if (observatorySubgroupForm.getValue() != Constants.OBS_VALUE_NOT_SCORE) {
									if (currentResults.get(observatorySubgroupForm.getDescription()) == null) {
										currentResults.put(observatorySubgroupForm.getDescription(), new BigDecimal(0));
										currentNumPoint.put(observatorySubgroupForm.getDescription(), 0);
									}
									// Si puntúa, se isNombreValido si se le da un 0
									// o un 1
									// Si puntúa, se isNombreValido si se le da un 0
									// o un 1
									if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ZERO) {// 0.5
										// Si le damos un 1, lo añadimos a la
										// puntuación e incrementamos el número de
										// puntos que han puntuado
										currentResults.put(observatorySubgroupForm.getDescription(), currentResults.get(observatorySubgroupForm.getDescription()).add(new BigDecimal(0.5)));
										if (currentNumPoint.get(observatorySubgroupForm.getDescription()) == -1) {
											currentNumPoint.put(observatorySubgroupForm.getDescription(), currentNumPoint.get(observatorySubgroupForm.getDescription()) + 2);
										} else {
											currentNumPoint.put(observatorySubgroupForm.getDescription(), currentNumPoint.get(observatorySubgroupForm.getDescription()) + 1);
										}
									} else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
										// Si le damos un 1, lo añadimos a la
										// puntuación e incrementamos el número de
										// puntos que han puntuado
										currentResults.put(observatorySubgroupForm.getDescription(), currentResults.get(observatorySubgroupForm.getDescription()).add(new BigDecimal(1)));
										if (currentNumPoint.get(observatorySubgroupForm.getDescription()) == -1) {
											currentNumPoint.put(observatorySubgroupForm.getDescription(), currentNumPoint.get(observatorySubgroupForm.getDescription()) + 2);
										} else {
											currentNumPoint.put(observatorySubgroupForm.getDescription(), currentNumPoint.get(observatorySubgroupForm.getDescription()) + 1);
										}
									} else {
										// Si le damos un 0 solamente incrementamos
										// el número de puntos
										currentNumPoint.put(observatorySubgroupForm.getDescription(), currentNumPoint.get(observatorySubgroupForm.getDescription()) + 1);
									}
								} else if (currentResults.get(observatorySubgroupForm.getDescription()) == null) {
									currentResults.put(observatorySubgroupForm.getDescription(), new BigDecimal(0));
									currentNumPoint.put(observatorySubgroupForm.getDescription(), -1);
								}
							}
						}
					}
				}
			}
		}
		final Map<Long, Map<String, BigDecimal>> verificationResultsByPointPage = new TreeMap<>();
		for (Map.Entry<Long, Map<String, BigDecimal>> resultEntry : groupedResults.entrySet()) {
			// Cambiamos las claves por el nombre y calculamos la media
			// Necesitamos implementar un orden específico para que p.e.
			// 1.10 vaya después de 1.9 y no de 1.
			final Map<String, BigDecimal> verificationResultsByPoint = new TreeMap<>(new Comparator<String>() {
				@Override
				public int compare(String version1, String version2) {
					String[] v1 = version1.split("\\.");
					String[] v2 = version2.split("\\.");
					int major1 = major(v1);
					int major2 = major(v2);
					if (major1 == major2) {
						return minor(v1).compareTo(minor(v2));
					}
					return major1 > major2 ? 1 : -1;
				}

				private int major(String[] version) {
					int r;
					try {
						r = Integer.parseInt(version[0]);
					} catch (NumberFormatException ex) {
						r = 0;
					}
					return r;
				}

				private Integer minor(String[] version) {
					return version.length > 1 ? Integer.parseInt(version[1]) : 0;
				}
			});
			for (Map.Entry<String, BigDecimal> resultEntry2 : resultEntry.getValue().entrySet()) {
				// Generar las claves con minhap.observatory.5_0.subgroup.
				final String name = resultEntry2.getKey().substring(resultEntry2.getKey().indexOf("minhap.observatory.5_0.subgroup.") + "minhap.observatory.5_0.subgroup.".length());
				final BigDecimal value;
				Map<String, Integer> numPoint = groupedNumPoint.get(resultEntry.getKey());
				if (numPoint.get(resultEntry2.getKey()) != -1 && numPoint.get(resultEntry2.getKey()) != 0) {
					value = resultEntry2.getValue().divide(BigDecimal.valueOf(numPoint.get(resultEntry2.getKey())), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN);
				} else if (numPoint.get(resultEntry2.getKey()) == -1) {
					value = BigDecimal.valueOf(-1);
				} else {
					value = BigDecimal.ZERO;
				}
				verificationResultsByPoint.put(name, value);
			}
			verificationResultsByPointPage.put(resultEntry.getKey(), verificationResultsByPoint);
		}
		return verificationResultsByPointPage;
	}

	/**
	 * Calculate percentage results by segment map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @param tagsFilter        the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	// Cálculo de resultados
	public static Map<CategoriaForm, Map<String, BigDecimal>> calculatePercentageResultsBySegmentMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<CategoriaForm> categories, String[] tagsFilter) throws Exception {
		final Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<CategoriaForm>() {
			@Override
			public int compare(CategoriaForm o1, CategoriaForm o2) {
				return (Long.valueOf(o1.getOrden()).compareTo(Long.valueOf(o2.getOrden())));
//				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (CategoriaForm category : categories) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList, false, tagsFilter);
			resultsBySegment.put(category, calculatePercentage(getResultsBySiteLevel(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Calculate percentage results by complexity map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param complexities      the complexities
	 * @param tagsFilter        the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<ComplejidadForm, Map<String, BigDecimal>> calculatePercentageResultsByComplexityMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<ComplejidadForm> complexities, String[] tagsFilter) throws Exception {
		final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<ComplejidadForm>() {
			@Override
			public int compare(ComplejidadForm o1, ComplejidadForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (ComplejidadForm complexity : complexities) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(executionId, Long.parseLong(complexity.getId()), pageExecutionList, true, tagsFilter);
			resultsBySegment.put(complexity, calculatePercentage(getResultsBySiteLevel(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Calculate percentage compilance results by segment map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @param tagsFilter        the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<CategoriaForm, Map<String, BigDecimal>> calculatePercentageCompilanceResultsBySegmentMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<CategoriaForm> categories, String[] tagsFilter) throws Exception {
		final Map<CategoriaForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<CategoriaForm>() {
			@Override
			public int compare(CategoriaForm o1, CategoriaForm o2) {
				return (Long.valueOf(o1.getOrden()).compareTo(Long.valueOf(o2.getOrden())));
//				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (CategoriaForm category : categories) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList, false, tagsFilter);
			resultsBySegment.put(category, calculatePercentage(getResultsBySiteCompilance(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Calculate percentage compilance results by complexitivity map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param completivities    the completivities
	 * @param tagsFilter        the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<ComplejidadForm, Map<String, BigDecimal>> calculatePercentageCompilanceResultsByComplexitivityMap(final String executionId,
			final List<ObservatoryEvaluationForm> pageExecutionList, final List<ComplejidadForm> completivities, String[] tagsFilter) throws Exception {
		final Map<ComplejidadForm, Map<String, BigDecimal>> resultsBySegment = new TreeMap<>(new Comparator<ComplejidadForm>() {
			@Override
			public int compare(ComplejidadForm o1, ComplejidadForm o2) {
				return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
			}
		});
		for (ComplejidadForm complexitivity : completivities) {
			final List<ObservatoryEvaluationForm> resultDataSegment = getGlobalResultData(executionId, Long.parseLong(complexitivity.getId()), pageExecutionList, true, tagsFilter);
			resultsBySegment.put(complexitivity, calculatePercentage(getResultsBySiteCompilance(resultDataSegment)));
		}
		return resultsBySegment;
	}

	/**
	 * Calculate mid puntuation results by segment map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param categories        the categories
	 * @param tagsFilter        the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<CategoriaForm, Map<String, BigDecimal>> calculateMidPuntuationResultsBySegmentMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<CategoriaForm> categories, String[] tagsFilter) throws Exception {
		try {
			final Map<CategoriaForm, Map<String, BigDecimal>> resultDataBySegment = new TreeMap<>(new Comparator<CategoriaForm>() {
				@Override
				public int compare(CategoriaForm o1, CategoriaForm o2) {
					return (Long.valueOf(o1.getOrden()).compareTo(Long.valueOf(o2.getOrden())));
//					return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
				}
			});
			for (CategoriaForm category : categories) {
				final List<ObservatorySiteEvaluationForm> categoryList = getSitesListByLevel(getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList, false, tagsFilter));
				resultDataBySegment.put(category, barGraphicFromMidPuntuationSegmentData(categoryList));
			}
			return resultDataBySegment;
		} catch (Exception e) {
			Logger.putLog("Error al recuperar datos de la BBDD.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Calculate mid puntuation results by segment map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param complexitivities  the complexitivities
	 * @param tagsFilter        the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<ComplejidadForm, Map<String, BigDecimal>> calculateMidPuntuationResultsByComplexitivityMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<ComplejidadForm> complexitivities, String[] tagsFilter) throws Exception {
		try {
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultDataBySegment = new TreeMap<>(new Comparator<ComplejidadForm>() {
				@Override
				public int compare(ComplejidadForm o1, ComplejidadForm o2) {
					return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
				}
			});
			for (ComplejidadForm category : complexitivities) {
				final List<ObservatorySiteEvaluationForm> categoryList = getSitesListByLevel(getGlobalResultData(executionId, Long.parseLong(category.getId()), pageExecutionList, true, tagsFilter));
				resultDataBySegment.put(category, barGraphicFromMidPuntuationSegmentData(categoryList));
			}
			return resultDataBySegment;
		} catch (Exception e) {
			Logger.putLog("Error al recuperar datos de la BBDD.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Calculate mid puntuation results by complecity map.
	 *
	 * @param executionId       the execution id
	 * @param pageExecutionList the page execution list
	 * @param complexities      the complexities
	 * @param tagsFilter        the tags filter
	 * @return the map
	 * @throws Exception the exception
	 */
	public static Map<ComplejidadForm, Map<String, BigDecimal>> calculateMidPuntuationResultsByComplecityMap(final String executionId, final List<ObservatoryEvaluationForm> pageExecutionList,
			final List<ComplejidadForm> complexities, String[] tagsFilter) throws Exception {
		try {
			final Map<ComplejidadForm, Map<String, BigDecimal>> resultDataBySegment = new TreeMap<>(new Comparator<ComplejidadForm>() {
				@Override
				public int compare(ComplejidadForm o1, ComplejidadForm o2) {
					return (Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())));
				}
			});
			for (ComplejidadForm complexity : complexities) {
				final List<ObservatorySiteEvaluationForm> categoryList = getSitesListByLevel(getGlobalResultData(executionId, Long.parseLong(complexity.getId()), pageExecutionList, true, tagsFilter));
				resultDataBySegment.put(complexity, barGraphicFromMidPuntuationSegmentData(categoryList));
			}
			return resultDataBySegment;
		} catch (Exception e) {
			Logger.putLog("Error al recuperar datos de la BBDD.", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * @param exObsIds           the ex obs ids
	 * @param title              the title
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionSuitabilityChart(final String observatoryId, final String executionId, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, String[] exObsIds, final String title) throws IOException {
		final Map<String, Map<String, BigDecimal>> evolutionSuitabilityDatePercentMap = new LinkedHashMap<>();
		final Map<Date, Map<Long, Map<String, Integer>>> result = getEvolutionObservatoriesSitesByType(observatoryId, executionId, pageObservatoryMap, exObsIds);
		evolutionSuitabilityDatePercentMap.put(Constants.OBS_NV_LABEL, calculatePercentageApprovalSiteLevel(result, Constants.OBS_NV));
		evolutionSuitabilityDatePercentMap.put(Constants.OBS_A_LABEL, calculatePercentageApprovalSiteLevel(result, Constants.OBS_A));
		evolutionSuitabilityDatePercentMap.put(Constants.OBS_AA_LABEL, calculatePercentageApprovalSiteLevel(result, Constants.OBS_AA));
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Map<String, BigDecimal>> evolutionSuitabilityEntry : evolutionSuitabilityDatePercentMap.entrySet()) {
			for (Map.Entry<String, BigDecimal> datePercentEntry : evolutionSuitabilityEntry.getValue().entrySet()) {
				dataSet.addValue(datePercentEntry.getValue(), evolutionSuitabilityEntry.getKey(), datePercentEntry.getKey());
			}
		}
		final String noDataMess = "noData";
		final PropertiesManager pmgr = new PropertiesManager();
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		chartForm.setTitle(title);
		GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
	}

	/**
	 * Generate evolution compliance chart.
	 *
	 * @param observatoryId      the observatory id
	 * @param executionId        the execution id
	 * @param filePath           the file path
	 * @param pageObservatoryMap the page observatory map
	 * @param exObsIds           the ex obs ids
	 * @param title              the title
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionComplianceChart(final String observatoryId, final String executionId, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, String[] exObsIds, String title) throws IOException {
		final Map<String, Map<String, BigDecimal>> evolutionSuitabilityDatePercentMap = new LinkedHashMap<>();
		final Map<Date, Map<Long, Map<String, Integer>>> result = getEvolutionObservatoriesSitesByCompliance(observatoryId, executionId, pageObservatoryMap, exObsIds);
		evolutionSuitabilityDatePercentMap.put(Constants.OBS_COMPILANCE_NONE, calculatePercentageApprovalSiteCompliance(result, Constants.OBS_COMPILANCE_NONE));
		evolutionSuitabilityDatePercentMap.put(Constants.OBS_COMPILANCE_PARTIAL, calculatePercentageApprovalSiteCompliance(result, Constants.OBS_COMPILANCE_PARTIAL));
		evolutionSuitabilityDatePercentMap.put(Constants.OBS_COMPILANCE_FULL, calculatePercentageApprovalSiteCompliance(result, Constants.OBS_COMPILANCE_FULL));
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Map<String, BigDecimal>> evolutionSuitabilityEntry : evolutionSuitabilityDatePercentMap.entrySet()) {
			for (Map.Entry<String, BigDecimal> datePercentEntry : evolutionSuitabilityEntry.getValue().entrySet()) {
				dataSet.addValue(datePercentEntry.getValue(), evolutionSuitabilityEntry.getKey(), datePercentEntry.getKey());
			}
		}
		final String noDataMess = "noData";
		final PropertiesManager pmgr = new PropertiesManager();
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		chartForm.setTitle(title);
		GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
	}

	/**
	 * Generate evolution by segment suitability chart.
	 *
	 * @param observatoryId      the observatory id
	 * @param executionId        the execution id
	 * @param filePath           the file path
	 * @param pageObservatoryMap the page observatory map
	 * @param categories         the categories
	 * @param exObsIds           the ex obs ids
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionBySegmentSuitabilityChart(final String observatoryId, final String executionId, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, final List<CategoriaForm> categories, String[] exObsIds) throws IOException {
		// Filtrar por segmento
		Map<String, Map<Date, List<ObservatoryEvaluationForm>>> pageObservatoryMapBySegment = new LinkedHashMap<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
		String defaultGroup = null;
		for (Date date : pageObservatoryMap.keySet()) {
			if (defaultGroup == null) {
				defaultGroup = df.format(date);
			}
			for (ObservatoryEvaluationForm obs : pageObservatoryMap.get(date)) {
				String categoryName = obs.getSeed().getCategory();
				if (!pageObservatoryMapBySegment.containsKey(categoryName)) {
					Map<Date, List<ObservatoryEvaluationForm>> tmp = new LinkedHashMap<>();
					List<ObservatoryEvaluationForm> obsList = new ArrayList<>();
					obsList.add(obs);
					tmp.put(date, obsList);
					pageObservatoryMapBySegment.put(categoryName, tmp);
				} else {
					Map<Date, List<ObservatoryEvaluationForm>> tmp = pageObservatoryMapBySegment.get(categoryName);
					if (!tmp.containsKey(date)) {
						List<ObservatoryEvaluationForm> obsList = new ArrayList<>();
						obsList.add(obs);
						tmp.put(date, obsList);
						pageObservatoryMapBySegment.put(categoryName, tmp);
					} else {
						List<ObservatoryEvaluationForm> obsList = tmp.get(date);
						obsList.add(obs);
						tmp.put(date, obsList);
						pageObservatoryMapBySegment.put(categoryName, tmp);
					}
				}
			}
		}
		// Calcular porcentajes por segmento
		final Map<String, Map<String, Map<String, BigDecimal>>> evolutionSuitabilityDatePercentMapBySegment = new LinkedHashMap<>();
		for (CategoriaForm category : categories) {
			final Map<Date, Map<Long, Map<String, Integer>>> result = getEvolutionObservatoriesSitesByType(observatoryId, executionId, pageObservatoryMapBySegment.get(category.getName()), exObsIds);
			Map<String, Map<String, BigDecimal>> evolutionSuitabilityDatePercentMap = null;
			if (!evolutionSuitabilityDatePercentMapBySegment.containsKey(category.getName())) {
				evolutionSuitabilityDatePercentMap = new LinkedHashMap<>();
			} else {
				evolutionSuitabilityDatePercentMap = evolutionSuitabilityDatePercentMapBySegment.get(category.getName());
			}
			evolutionSuitabilityDatePercentMap.put("Parcial", calculatePercentageApprovalSiteLevel(result, Constants.OBS_NV));
			evolutionSuitabilityDatePercentMap.put("Prioridad 1", calculatePercentageApprovalSiteLevel(result, Constants.OBS_A));
			evolutionSuitabilityDatePercentMap.put("Prioridad 1 y 2", calculatePercentageApprovalSiteLevel(result, Constants.OBS_AA));
			evolutionSuitabilityDatePercentMapBySegment.put(category.getName(), evolutionSuitabilityDatePercentMap);
		}
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		KeyToGroupMap map = new KeyToGroupMap(defaultGroup);
		List<String> axis = new ArrayList<>();
		for (CategoriaForm category : categories) {
			for (Map.Entry<String, Map<String, BigDecimal>> entryBySegment : evolutionSuitabilityDatePercentMapBySegment.get(category.getName()).entrySet()) {
				for (Map.Entry<String, BigDecimal> entryByDate : entryBySegment.getValue().entrySet()) {
					BigDecimal value = entryByDate.getValue();
					String rowKey = entryByDate.getKey() + " " + entryBySegment.getKey();
					String columnKey = category.getName();
					/******************* Grouped **/
					dataSet.addValue(value, rowKey, columnKey);
					/******************* Unrouped **/
					// Key group
					String key = entryByDate.getKey() + " " + entryBySegment.getKey();
					String group = entryByDate.getKey();
					map.mapKeyToGroup(key, group);
					// Axis
					if (!axis.contains(group)) {
						axis.add(group);
					}
				}
			}
		}
		SubCategoryAxis domainAxis = new SubCategoryAxis("Fecha / Segmento");
		for (int i = 0; i < axis.size(); i++) {
			domainAxis.addSubCategory(axis.get(i));
			domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		}
		final String noDataMess = "noData";
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, false, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		GraphicsUtils.createGroupedStackerBarChart(chartForm, noDataMess, filePath, map, domainAxis);
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
	 * @param title              the title
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionAverageScoreByVerificationChart(final MessageResources messageResources, final String filePath,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, final List<String> verifications, final String title) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			final Map<String, BigDecimal> resultsByVerification = getVerificationResultsByPoint(entry.getValue(), Constants.OBS_PRIORITY_NONE);
			for (String verification : verifications) {
				// Para un observatorio en concreto recuperamos la puntuación de
				// una verificación
				final BigDecimal value = resultsByVerification.get(verification);
				dataSet.addValue(value, entry.getKey().getTime(), verification);
			}
		}
		final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm.setFixedColorBars(true);
		chartForm.setShowColumsLabels(false);
		chartForm.setTitle(title);
		GraphicsUtils.createStandardBarChart(chartForm, filePath, "", messageResources, true);
	}

	/**
	 * Generate evolution average score by verification chart split.
	 *
	 * @param messageResources   the message resources
	 * @param filePaths          the file paths
	 * @param pageObservatoryMap the page observatory map
	 * @param verifications      the verifications
	 * @param title              the title
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionAverageScoreByVerificationChartSplit(final MessageResources messageResources, final String[] filePaths,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, final List<String> verifications, final String title) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final DefaultCategoryDataset dataSet1 = new DefaultCategoryDataset();
		final DefaultCategoryDataset dataSet2 = new DefaultCategoryDataset();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			final Map<String, BigDecimal> resultsByVerification = getVerificationResultsByPoint(entry.getValue(), Constants.OBS_PRIORITY_NONE);
			int v = 0;
			for (String verification : verifications) {
				// Para un observatorio en concreto recuperamos la puntuación de
				// una verificación
				final BigDecimal value = resultsByVerification.get(verification);
				if (v < 7) {
					dataSet1.addValue(value, entry.getKey().getTime(), verification);
				} else {
					dataSet2.addValue(value, entry.getKey().getTime(), verification);
				}
				v++;
			}
		}
		final ChartForm chartForm1 = new ChartForm(dataSet1, true, true, false, false, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm1.setFixedColorBars(true);
		chartForm1.setShowColumsLabels(false);
		chartForm1.setTitle(title);
		GraphicsUtils.createStandardBarChart(chartForm1, filePaths[0], "", messageResources, true);
		final ChartForm chartForm2 = new ChartForm(dataSet2, true, true, false, false, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm2.setFixedColorBars(true);
		chartForm2.setShowColumsLabels(false);
		chartForm2.setTitle(title);
		GraphicsUtils.createStandardBarChart(chartForm2, filePaths[1], "", messageResources, true);
	}

	/**
	 * Generate evolution compliance by verification chart split.
	 *
	 * @param messageResources   the message resources
	 * @param filePaths          the file paths
	 * @param pageObservatoryMap the page observatory map
	 * @param verifications      the verifications
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionComplianceByVerificationChartSplit(final MessageResources messageResources, final String[] filePaths,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, final List<String> verifications) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final DefaultCategoryDataset dataSet1 = new DefaultCategoryDataset();
		final DefaultCategoryDataset dataSet2 = new DefaultCategoryDataset();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			final Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(entry.getValue(), Constants.OBS_PRIORITY_NONE);
			Map<String, BigDecimal> percentajes = generatePercentajesCompilanceVerification(results);
			int v = 0;
			for (String verification : verifications) {
				// Para un observatorio en concreto recuperamos la puntuación de
				// una verificación
				final BigDecimal value = percentajes.get(verification.concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX));
				if (v < 7) {
					dataSet1.addValue(value, entry.getKey().getTime(), verification);
				} else {
					dataSet2.addValue(value, entry.getKey().getTime(), verification);
				}
				v++;
			}
		}
		final ChartForm chartForm1 = new ChartForm(dataSet1, true, true, false, true, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm1.setFixedColorBars(true);
		chartForm1.setShowColumsLabels(false);
		GraphicsUtils.createStandardBarChart(chartForm1, filePaths[0], "", messageResources, true);
		final ChartForm chartForm2 = new ChartForm(dataSet2, true, true, false, true, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm2.setFixedColorBars(true);
		chartForm2.setShowColumsLabels(false);
		GraphicsUtils.createStandardBarChart(chartForm2, filePaths[1], "", messageResources, true);
	}

	/**
	 * Generate evolution compliance by verification chart split.
	 *
	 * @param messageResources   the message resources
	 * @param filePaths          the file paths
	 * @param titles             the titles
	 * @param pageObservatoryMap the page observatory map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionComplianceByVerificationChartSplitGrouped(final MessageResources messageResources, final String[] filePaths, final String[] titles,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		Map<String, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionComplianceDataSetDetailed(LEVEL_I_VERIFICATIONS.subList(0, 7),
				pageObservatoryMap);
		GraphicsUtils.createBarChartGrouped(results, titles[0], "", "", pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR), true, true, true, filePaths[0], "", messageResources, 1465,
				654);
		results = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionComplianceDataSetDetailed(LEVEL_I_VERIFICATIONS.subList(7, 14), pageObservatoryMap);
		GraphicsUtils.createBarChartGrouped(results, titles[0], "", "", pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR), true, true, true, filePaths[1], "", messageResources, 1465,
				654);
		results = ResultadosAnonimosObservatorioUNEEN2019Utils.calculateVerificationEvolutionComplianceDataSetDetailed(LEVEL_II_VERIFICATIONS, pageObservatoryMap);
		GraphicsUtils.createBarChartGrouped(results, titles[1], "", "", pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR), true, true, true, filePaths[2], "", messageResources, 1465,
				654);
	}

	/**
	 * Generate evolution compliance by verification chart.
	 *
	 * @param messageResources   the message resources
	 * @param filePaths          the file paths
	 * @param pageObservatoryMap the page observatory map
	 * @param verifications      the verifications
	 * @param title              the title
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionComplianceByVerificationChart(final MessageResources messageResources, final String[] filePaths,
			final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap, final List<String> verifications, final String title) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final DefaultCategoryDataset dataSet1 = new DefaultCategoryDataset();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			final Map<Long, Map<String, BigDecimal>> results = getVerificationResultsByPointAndCrawl(entry.getValue(), Constants.OBS_PRIORITY_NONE);
			Map<String, BigDecimal> percentajes = generatePercentajesCompilanceVerification(results);
			for (String verification : verifications) {
				// Para un observatorio en concreto recuperamos la puntuación de
				// una verificación
				final BigDecimal value = percentajes.get(verification.concat(Constants.OBS_VALUE_COMPILANCE_SUFFIX));
				dataSet1.addValue(value, entry.getKey().getTime(), verification);
			}
		}
		final ChartForm chartForm1 = new ChartForm(dataSet1, true, true, false, true, false, false, false, 1465, 654, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm1.setFixedColorBars(true);
		chartForm1.setShowColumsLabels(false);
		chartForm1.setTitle(title);
		GraphicsUtils.createStandardBarChart(chartForm1, filePaths[0], "", messageResources, true);
	}

	/**
	 * Generate the evolution char of the average score grouped by aspects. This is a landscape chart where for each aspect there is a column with the score achieved in each observatory execution.
	 *
	 * @param messageResources   MessageResources needed for GraphicsUtils utility class
	 * @param filePath           String with the full path (filename included) where the chart will be saved as jpg file.
	 * @param pageObservatoryMap a Map where each entrey is keyed by observatory date and the value is a list of the page evaluations
	 * @param title              the title
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionAverageScoreByAspectChart(final MessageResources messageResources, final String filePath, final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap,
			final String title) throws IOException {
		final Map<Date, Map<String, BigDecimal>> resultsByAspect = new LinkedHashMap<>();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, entry.getValue()));
		}
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<Date, Map<String, BigDecimal>> dateAspectValueEntry : resultsByAspect.entrySet()) {
			// Todas las puntuaciones por aspecto se muestran en este orden
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage("observatory.aspect.general")), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage("observatory.aspect.general"));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage("observatory.aspect.presentation")), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage("observatory.aspect.presentation"));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage("observatory.aspect.structure")), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage("observatory.aspect.structure"));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage("observatory.aspect.navigation")), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage("observatory.aspect.navigation"));
			dataSet.addValue(dateAspectValueEntry.getValue().get(messageResources.getMessage("observatory.aspect.alternatives")), dateAspectValueEntry.getKey().getTime(),
					messageResources.getMessage("observatory.aspect.alternatives"));
		}
		final PropertiesManager pmgr = new PropertiesManager();
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, false, false, false, false, 1565, 684, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm.setFixedColorBars(true);
		chartForm.setShowColumsLabels(false);
		chartForm.setTitle(title);
		GraphicsUtils.createStandardBarChart(chartForm, filePath, "", messageResources, true);
	}

	/**
	 * Generate evolution average score by segment chart.
	 *
	 * @param messageResources   the message resources
	 * @param filePath           the file path
	 * @param pageObservatoryMap the page observatory map
	 * @param categories         the categories
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void generateEvolutionAverageScoreBySegmentChart(final MessageResources messageResources, final String filePath, final Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap,
			final List<CategoriaForm> categories) throws IOException {
		final Map<Date, Map<String, BigDecimal>> resultsByAspect = new LinkedHashMap<>();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : pageObservatoryMap.entrySet()) {
			resultsByAspect.put(entry.getKey(), ResultadosAnonimosObservatorioUNEEN2019Utils.segmentMidsPuntuationGraphicData(messageResources, entry.getValue()));
		}
		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<Date, Map<String, BigDecimal>> dateAspectValueEntry : resultsByAspect.entrySet()) {
			for (CategoriaForm category : categories) {
				dataSet.addValue(dateAspectValueEntry.getValue().get(category.getName()), dateAspectValueEntry.getKey().getTime(), category.getName());
			}
		}
		final PropertiesManager pmgr = new PropertiesManager();
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, false, false, false, false, 1565, 684, pmgr.getValue(CRAWLER_PROPERTIES, CHART_EVOLUTION_MP_GREEN_COLOR));
		chartForm.setFixedColorBars(false);
		chartForm.setShowColumsLabels(false);
		GraphicsUtils.createStandardBarChart(chartForm, filePath, "", messageResources, true);
	}

	/**
	 * Aspect mids puntuation graphic data.
	 *
	 * @param messageResources the message resources
	 * @param resultData       the result data
	 * @return the map
	 */
	public static Map<String, BigDecimal> segmentMidsPuntuationGraphicData(final MessageResources messageResources, final List<ObservatoryEvaluationForm> resultData) {
		final Map<String, List<LabelValueBean>> globalResult = new HashMap<>();
		// Recorremos todas las evaluaciones y construimos un mapa con los segmentos y
		// las puntuaciones de la evaluación
		for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
			String category = observatoryEvaluationForm.getSeed().getCategory();
			if (!globalResult.containsKey(category)) {
				globalResult.put(category, new ArrayList<LabelValueBean>());
			}
			LabelValueBean lvb = new LabelValueBean();
			lvb.setLabel(category);
			lvb.setValue(String.valueOf(observatoryEvaluationForm.getScore()));
			globalResult.get(category).add(lvb);
		}
		final Map<String, BigDecimal> results = new HashMap<>();
		for (Map.Entry<String, List<LabelValueBean>> globalResultEntry : globalResult.entrySet()) {
			final String category = globalResultEntry.getKey();
			// Recorremos las verificaciones de cada aspecto
			final Map<String, List<BigDecimal>> partialResultsMap = new HashMap<>();
			for (LabelValueBean lvb : globalResultEntry.getValue()) {
				if (!partialResultsMap.containsKey(lvb.getLabel())) {
					partialResultsMap.put(lvb.getLabel(), new ArrayList<BigDecimal>());
				}
				partialResultsMap.get(lvb.getLabel()).add(new BigDecimal(lvb.getValue()));
			}
			// Hemos recorrido las verificaciones del aspecto, ahora calculamos
			// la media de cada una
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
			// Teniendo las medias de verificaciones de un aspecto, pasamos a
			// calcular el valor medio de ese aspecto
			if (!results.containsKey(category)) {
				results.put(category, BigDecimal.ZERO);
			}
			for (Map.Entry<String, BigDecimal> stringBigDecimalEntry : verificationsMap.entrySet()) {
				results.put(category, results.get(category).add(stringBigDecimalEntry.getValue()));
			}
			if (verificationsMap.size() > 0) {
				results.put(category, results.get(category).divide(new BigDecimal(verificationsMap.size()), 2, BigDecimal.ROUND_HALF_UP));
			}
		}
		return results;
	}

	/**
	 * Get execution tags
	 * 
	 * @param executionId
	 * @return Execution tags
	 */
	private static String[] getExecutionTags(Long executionId) {
		Connection c = null;
		String[] executionTags = null;
		try {
			final Connection connection = DataBaseManager.getConnection();
			c = connection;
			c.setAutoCommit(false);
			// Borramos de la tabla de estado
			executionTags = EstadoObservatorioDAO.getExecutionTags(connection, executionId);
		} catch (Exception e) {
			Logger.putLog("Error al obtener las etiquetas de ejecución ", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
			if (c != null) {
				try {
					c.rollback();
				} catch (SQLException e1) {
					Logger.putLog("Error al realizar rollback", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		} finally {
			if (c != null) {
				try {
					c.rollback();
					DataBaseManager.closeConnection(c);
				} catch (SQLException e1) {
					Logger.putLog("Error al realizar rollback", ResultadosAnonimosObservatorioUNEEN2019Utils.class, Logger.LOG_LEVEL_ERROR, e1);
				}
			}
		}
		return executionTags;
	}
}