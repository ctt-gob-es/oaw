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
package es.inteco.rastreador2.utils.export.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.util.MessageResources;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.AspectScoreForm;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySiteEvaluationForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.export.database.AspectScore;
import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.export.database.Page;
import es.inteco.rastreador2.dao.export.database.Site;
import es.inteco.rastreador2.dao.export.database.VerificationModality;
import es.inteco.rastreador2.dao.export.database.VerificationPage;
import es.inteco.rastreador2.dao.export.database.VerificationScore;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.intav.utils.IntavUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;
import es.inteco.rastreador2.utils.ResultadosPrimariosObservatorioIntavUtils;

/**
 * The Class DatabaseExportUtils.
 */
public final class DatabaseExportUtils {
	/**
	 * Instantiates a new database export utils.
	 */
	private DatabaseExportUtils() {
	}

	/**
	 * Gets the observatory info.
	 *
	 * @param messageResources the message resources
	 * @param idExecution      the id execution
	 * @param originAnnexes    the origin
	 * @return the observatory info
	 * @throws Exception the exception ResultadosAnonimosObservatorioUNEEN2019Utils
	 */
	public static Observatory getObservatoryInfo(final MessageResources messageResources, final Long idExecution, final boolean originAnnexes) throws Exception {
		return getObservatoryInfo(messageResources, idExecution, originAnnexes, null);
	}

	/**
	 * Gets the observatory info.
	 *
	 * @param messageResources the message resources
	 * @param idExecution      the id execution
	 * @param originAnnexes    the origin
	 * @param tagsToFilter     tags
	 * @return the observatory info
	 * @throws Exception the exception ResultadosAnonimosObservatorioUNEEN2019Utils
	 */
	public static Observatory getObservatoryInfo(final MessageResources messageResources, final Long idExecution, final boolean originAnnexes, String[] tagsToFilter) throws Exception {
		final Observatory observatory = new Observatory();
		observatory.setIdExecution(idExecution);
		final List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalResultData(String.valueOf(idExecution), Constants.COMPLEXITY_SEGMENT_NONE, null,
				false, tagsToFilter);
		// Número de portales por modalidad
		final Map<String, Integer> result = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(pageExecutionList);
		for (String key : result.keySet()) {
			if (key.equals(Constants.OBS_AA)) {
				observatory.setNumAA(result.get(Constants.OBS_AA));
			} else if (key.equals(Constants.OBS_A)) {
				observatory.setNumA(result.get(Constants.OBS_A));
			} else if (key.equals(Constants.OBS_NV)) {
				observatory.setNumNV(result.get(Constants.OBS_NV));
			}
		}
		// Porcentajes de cada modalidad en cada verificación
		final Map<String, BigDecimal> verificationAndModality = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		for (String key : verificationAndModality.keySet()) {
			if (!observatory.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")))
					&& !observatory.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")))) {
				VerificationModality verificationModality = new VerificationModality();
				verificationModality.setObservatory(observatory);
				if (key.contains(Constants.OBS_VALUE_RED_SUFFIX)) {
					verificationModality.setVerification(getVerificationId(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")));
					verificationModality.setFailPercentage(verificationAndModality.get(key));
					if (verificationAndModality.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX) != null) {
						verificationModality.setPassPercentage(verificationAndModality.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX));
					} else {
						verificationModality.setPassPercentage(BigDecimal.ZERO);
					}
				} else if (key.contains(Constants.OBS_VALUE_GREEN_SUFFIX)) {
					verificationModality.setVerification(getVerificationId(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")));
					verificationModality.setPassPercentage(verificationAndModality.get(key));
					if (verificationAndModality.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX) != null) {
						verificationModality.setFailPercentage(verificationAndModality.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX));
					} else {
						verificationModality.setFailPercentage(BigDecimal.ZERO);
					}
				}
				observatory.getVerificationModalityList().add(verificationModality);
			}
		}
		// Puntuación de cada verificación
		final Map<String, BigDecimal> verificationAndScore = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		for (String key : verificationAndScore.keySet()) {
			VerificationScore verificationScore = new VerificationScore();
			// verification.id.reg.exp = (\\d\\.\\d)
			verificationScore.setVerification(getVerificationId(key));
			verificationScore.setObservatory(observatory);
			if (verificationAndScore.get(key).intValue() != -1) {
				verificationScore.setScore(verificationAndScore.get(key));
			}
			observatory.getVerificationScoreList().add(verificationScore);
		}
		// Puntuaciones por aspectos
		final Map<String, BigDecimal> aspectAndScore = ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
		for (String key : aspectAndScore.keySet()) {
			AspectScore aspectScore = new AspectScore();
			aspectScore.setAspect(key);
			if (Objects.nonNull(aspectAndScore.get(key))) {
				aspectScore.setScore(aspectAndScore.get(key));
			} else {
				aspectScore.setScore(new BigDecimal(0));
			}
			aspectScore.setObservatory(observatory);
			observatory.getAspectScoreList().add(aspectScore);
		}
		return observatory;
	}

	/**
	 * Gets the category info.
	 *
	 * @param messageResources the message resources
	 * @param categoriaForm    the categoria form
	 * @param observatory      the observatory
	 * @param originAnnexes    the origin
	 * @return the category info
	 * @throws Exception the exception
	 */
	public static Category getCategoryInfo(final MessageResources messageResources, final CategoriaForm categoriaForm, final Observatory observatory, final boolean originAnnexes) throws Exception {
		return getCategoryInfo(messageResources, categoriaForm, observatory, originAnnexes, null);
	}

	/**
	 * Gets the category info.
	 *
	 * @param messageResources the message resources
	 * @param categoriaForm    the categoria form
	 * @param observatory      the observatory
	 * @param originAnnexes    the origin
	 * @param tagsToFilter     tags
	 * @return the category info
	 * @throws Exception the exception
	 */
	public static Category getCategoryInfo(final MessageResources messageResources, final CategoriaForm categoriaForm, final Observatory observatory, final boolean originAnnexes,
			String[] tagsToFilter) throws Exception {
		final Category category = new Category();
		category.setName(categoriaForm.getName());
		category.setObservatory(observatory);
		category.setIdCrawlerCategory(Long.valueOf(categoriaForm.getId()));
		final List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioUNEEN2019Utils.getGlobalResultData(String.valueOf(observatory.getIdExecution()),
				Long.parseLong(categoriaForm.getId()), null, false, tagsToFilter);
		// Número de portales por modalidad
		final Map<String, Integer> result = ResultadosAnonimosObservatorioUNEEN2019Utils.getResultsBySiteLevel(pageExecutionList);
		for (String key : result.keySet()) {
			if (key.equals(Constants.OBS_AA)) {
				category.setNumAA(result.get(Constants.OBS_AA));
			} else if (key.equals(Constants.OBS_A)) {
				category.setNumA(result.get(Constants.OBS_A));
			} else if (key.equals(Constants.OBS_NV)) {
				category.setNumNV(result.get(Constants.OBS_NV));
			}
		}
		// Porcentajes de cada modalidad en cada verificación
		final Map<String, BigDecimal> verificationAndModality = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		for (String key : verificationAndModality.keySet()) {
			if (!category.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")))
					&& !category.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")))) {
				final VerificationModality verificationModality = new VerificationModality();
				verificationModality.setCategory(category);
				if (key.contains(Constants.OBS_VALUE_RED_SUFFIX)) {
					verificationModality.setVerification(getVerificationId(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")));
					verificationModality.setFailPercentage(verificationAndModality.get(key));
					if (verificationAndModality.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX) != null) {
						verificationModality.setPassPercentage(verificationAndModality.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX));
					} else {
						verificationModality.setPassPercentage(BigDecimal.ZERO);
					}
				} else if (key.contains(Constants.OBS_VALUE_GREEN_SUFFIX)) {
					verificationModality.setVerification(getVerificationId(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")));
					verificationModality.setPassPercentage(verificationAndModality.get(key));
					if (verificationAndModality.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX) != null) {
						verificationModality.setFailPercentage(verificationAndModality.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX));
					} else {
						verificationModality.setFailPercentage(BigDecimal.ZERO);
					}
				}
				category.getVerificationModalityList().add(verificationModality);
			}
		}
		// Puntuación de cada verificación
		final Map<String, BigDecimal> verificationAndScore = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_NONE);
		for (Map.Entry<String, BigDecimal> verificationScoreEntry : verificationAndScore.entrySet()) {
			final VerificationScore verificationScore = new VerificationScore();
			verificationScore.setVerification(getVerificationId(verificationScoreEntry.getKey()));
			verificationScore.setCategory(category);
			if (verificationScoreEntry.getValue().intValue() != -1) {
				verificationScore.setScore(verificationScoreEntry.getValue());
			}
			category.getVerificationScoreList().add(verificationScore);
		}
		// Puntuaciones por aspectos
		final Map<String, BigDecimal> aspectAndScore = ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
		for (String key : aspectAndScore.keySet()) {
			AspectScore aspectScore = new AspectScore();
			aspectScore.setAspect(key);
			if (Objects.nonNull(aspectAndScore.get(key))) {
				aspectScore.setScore(aspectAndScore.get(key));
			} else {
				aspectScore.setScore(new BigDecimal(0));
			}
			aspectScore.setCategory(category);
			category.getAspectScoreList().add(aspectScore);
		}
		// Puntuación de cada modalidad
		final List<CategoriaForm> categories = new ArrayList<>();
		categories.add(categoriaForm);
		final Map<CategoriaForm, Map<String, BigDecimal>> resultDataBySegment = ResultadosAnonimosObservatorioUNEEN2019Utils
				.calculateMidPuntuationResultsBySegmentMap(observatory.getIdExecution().toString(), pageExecutionList, categories, tagsToFilter);
		for (String key : resultDataBySegment.get(categoriaForm).keySet()) {
			if (key.equals(Constants.OBS_AA)) {
				category.setScoreAA(resultDataBySegment.get(categoriaForm).get(Constants.OBS_AA));
			} else if (key.equals(Constants.OBS_A)) {
				category.setScoreA(resultDataBySegment.get(categoriaForm).get(Constants.OBS_A));
			} else if (key.equals(Constants.OBS_NV)) {
				category.setScoreNV(resultDataBySegment.get(categoriaForm).get(Constants.OBS_NV));
			}
		}
		try {
			final List<ObservatorySiteEvaluationForm> observatorySiteEvaluations = ResultadosAnonimosObservatorioUNEEN2019Utils.getSitesListByLevel(pageExecutionList);
			for (ObservatorySiteEvaluationForm observatorySiteEvaluationForm : observatorySiteEvaluations) {
				final Site site = getSiteInfo(messageResources, observatorySiteEvaluationForm, category);
				// Logger.putLog(categoriaForm.getName() + " - site: " + site.getIdCrawlerSeed() + " - " + site.getName(), DatabaseExportUtils.class, Logger.LOG_LEVEL_ERROR);
				category.getSiteList().add(site);
			}
		} catch (Exception e) {
			Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return category;
	}

	/**
	 * Gets the site info.
	 *
	 * @param messagesResources             the messages resources
	 * @param observatorySiteEvaluationForm the observatory site evaluation form
	 * @param category                      the category
	 * @return the site info
	 * @throws Exception the exception
	 */
	public static Site getSiteInfo(final MessageResources messagesResources, ObservatorySiteEvaluationForm observatorySiteEvaluationForm, Category category) throws Exception {
		final Site site = new Site();
		site.setCategory(category);
		site.setName(observatorySiteEvaluationForm.getName());
		site.setScore(observatorySiteEvaluationForm.getScore());
		site.setLevel(observatorySiteEvaluationForm.getLevel());
		site.setIdCrawlerSeed(observatorySiteEvaluationForm.getIdSeed());
		ScoreForm scoreForm = null;
		final Connection connection = DataBaseManager.getConnection();
		final String application = CartuchoDAO.getApplicationFromCrawlerExceutionId(connection, observatorySiteEvaluationForm.getId());
		DataBaseManager.closeConnection(connection);
		if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(application)) {
			scoreForm = IntavUtils.generateScoresAccesibility(messagesResources, observatorySiteEvaluationForm.getPages());
		} else {
			scoreForm = IntavUtils.generateScores(messagesResources, observatorySiteEvaluationForm.getPages());
		}
		site.setScoreLevel1(scoreForm.getScoreLevel1());
		site.setScoreLevel2(scoreForm.getScoreLevel2());
		final Map<String, Integer> resultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(observatorySiteEvaluationForm.getPages());
		for (String key : resultsByLevel.keySet()) {
			if (key.equals(Constants.OBS_AA)) {
				site.setNumAA(resultsByLevel.get(Constants.OBS_AA));
			} else if (key.equals(Constants.OBS_A)) {
				site.setNumA(resultsByLevel.get(Constants.OBS_A));
			} else if (key.equals(Constants.OBS_NV)) {
				site.setNumNV(resultsByLevel.get(Constants.OBS_NV));
			}
		}
		// Puntuación de cada verificación
		final Map<String, BigDecimal> verificationAndScore = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(observatorySiteEvaluationForm.getPages(),
				Constants.OBS_PRIORITY_NONE);
		for (String key : verificationAndScore.keySet()) {
			VerificationScore verificationScore = new VerificationScore();
			verificationScore.setVerification(getVerificationId(key));
			verificationScore.setSite(site);
			if (verificationAndScore.get(key).intValue() != -1) {
				verificationScore.setScore(verificationAndScore.get(key));
			}
			site.getVerificationScoreList().add(verificationScore);
		}
		// Porcentajes de cada modalidad en cada verificación
		final Map<String, BigDecimal> verificationAndModality = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndModality(observatorySiteEvaluationForm.getPages(),
				Constants.OBS_PRIORITY_NONE);
		for (String key : verificationAndModality.keySet()) {
			if (!site.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")))
					&& !site.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")))) {
				VerificationModality verificationModality = new VerificationModality();
				verificationModality.setSite(site);
				if (key.contains(Constants.OBS_VALUE_RED_SUFFIX)) {
					verificationModality.setVerification(getVerificationId(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")));
					verificationModality.setFailPercentage(verificationAndModality.get(key));
					if (verificationAndModality.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX) != null) {
						verificationModality.setPassPercentage(verificationAndModality.get(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "") + Constants.OBS_VALUE_GREEN_SUFFIX));
					} else {
						verificationModality.setPassPercentage(BigDecimal.ZERO);
					}
				} else if (key.contains(Constants.OBS_VALUE_GREEN_SUFFIX)) {
					verificationModality.setVerification(getVerificationId(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "")));
					verificationModality.setPassPercentage(verificationAndModality.get(key));
					if (verificationAndModality.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX) != null) {
						verificationModality.setFailPercentage(verificationAndModality.get(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, "") + Constants.OBS_VALUE_RED_SUFFIX));
					} else {
						verificationModality.setFailPercentage(BigDecimal.ZERO);
					}
				}
				site.getVerificationModalityList().add(verificationModality);
			}
		}
		// Puntuaciones por aspectos
		final Map<String, BigDecimal> aspectAndScore = ResultadosAnonimosObservatorioUNEEN2019Utils.aspectMidsPuntuationGraphicData(messagesResources, observatorySiteEvaluationForm.getPages());
		for (String key : aspectAndScore.keySet()) {
			AspectScore aspectScore = new AspectScore();
			aspectScore.setAspect(key);
			if (Objects.nonNull(aspectAndScore.get(key))) {
				aspectScore.setScore(aspectAndScore.get(key));
			} else {
				aspectScore.setScore(new BigDecimal(0));
			}
			aspectScore.setSite(site);
			site.getAspectScoreList().add(aspectScore);
		}
		for (ObservatoryEvaluationForm observatoryEvaluationForm : observatorySiteEvaluationForm.getPages()) {
			Page page = getPageInfo(messagesResources, observatoryEvaluationForm, site);
			site.getPageList().add(page);
		}
		// Compliance to export
		if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(application)) {
			site.setCompliance(IntavUtils.getValidationLevelAccesibility(observatorySiteEvaluationForm.getPages(), messagesResources));
		} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(application)) {
			Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(observatorySiteEvaluationForm.getPages(),
					Constants.OBS_PRIORITY_NONE);
			Map<Long, String> calculatedCompliance = null;
			calculatedCompliance = calculateCrawlingCompliance(results);
			site.setCompliance(calculatedCompliance.get(observatorySiteEvaluationForm.getPages().get(0).getCrawlerExecutionId()));
		}
		return site;
	}

	/**
	 * Gets the page info.
	 *
	 * @param messageResources          the message resources
	 * @param observatoryEvaluationForm the observatory evaluation form
	 * @param site                      the site
	 * @return the page info
	 */
	public static Page getPageInfo(final MessageResources messageResources, ObservatoryEvaluationForm observatoryEvaluationForm, Site site) {
		Page page = new Page();
		page.setScoreLevel1(BigDecimal.ZERO);
		page.setScoreLevel2(BigDecimal.ZERO);
		page.setSite(site);
		page.setUrl(observatoryEvaluationForm.getUrl());
		page.setScore(observatoryEvaluationForm.getScore());
		page.setLevel(ObservatoryUtils.pageSuitabilityLevel(observatoryEvaluationForm));
		for (ObservatoryLevelForm observatoryLevelForm : observatoryEvaluationForm.getGroups()) {
			if (observatoryLevelForm.getName().equals("Priority 1")) {
				page.setScoreLevel1(observatoryLevelForm.getScore());
			} else if (observatoryLevelForm.getName().equals("Priority 2")) {
				page.setScoreLevel2(observatoryLevelForm.getScore());
			}
			for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
				for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
					Float value = null;
					String modality = null;
					if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
						value = 1f;
						modality = "Pasa";
					} else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ZERO) {
						value = Float.parseFloat(messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero.pasa"));
						modality = "Pasa";
					} else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_RED_ZERO) {
						value = 0f;
						modality = "Falla";
					} else if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_NOT_SCORE) {
						modality = "Pasa";
					}
					VerificationPage verificationPage = new VerificationPage();
					verificationPage.setVerification(getVerificationId(observatorySubgroupForm.getDescription()));
					verificationPage.setModality(modality);
					verificationPage.setValue(value);
					verificationPage.setPage(page);
					page.getVerificationPageList().add(verificationPage);
				}
			}
		}
		for (AspectScoreForm aspectScoreForm : observatoryEvaluationForm.getAspects()) {
			AspectScore aspectScore = new AspectScore();
			aspectScore.setAspect(messageResources.getMessage(aspectScoreForm.getName()));
			if (Objects.nonNull(aspectScoreForm.getScore())) {
				aspectScore.setScore(aspectScoreForm.getScore());
			} else {
				aspectScore.setScore(new BigDecimal(0));
			}
			aspectScore.setPage(page);
			page.getAspectScoreList().add(aspectScore);
		}
		return page;
	}

	/**
	 * Gets the verification id.
	 *
	 * @param verification the verification
	 * @return the verification id
	 */
	private static String getVerificationId(final String verification) {
		final PropertiesManager pmgr = new PropertiesManager();
		// final Pattern pattern = Pattern.compile(pmgr.getValue(CRAWLER_PROPERTIES, "verification.id.reg.exp"), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		final Pattern pattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+)|(\\d+\\.\\d+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		final Matcher matcher = pattern.matcher(verification);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return verification;
		}
	}

	/**
	 * Calculate crawling compliance.
	 *
	 * @param results the results
	 * @return the map
	 */
	private static Map<Long, String> calculateCrawlingCompliance(Map<Long, Map<String, BigDecimal>> results) {
		final Map<Long, String> resultCompilance = new TreeMap<>();
		// Process results
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
				resultCompilance.put(result.getKey(), Constants.OBS_COMPILANCE_FULL);
			} else if ((countC + countNA) > countNC) {
				resultCompilance.put(result.getKey(), Constants.OBS_COMPILANCE_PARTIAL);
			} else {
				resultCompilance.put(result.getKey(), Constants.OBS_COMPILANCE_NONE);
			}
		}
		return resultCompilance;
	}
}