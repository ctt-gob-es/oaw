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
package es.inteco.rastreador2.intav.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Guideline;
import ca.utoronto.atrc.tile.accessibilitychecker.GuidelineGroup;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.intav.action.AnalysisFromCrawlerAction;
import es.inteco.rastreador2.intav.action.ResultsAction;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;

/**
 * The Class IntavUtils.
 */
public final class IntavUtils {
	/** The result L 1. */
	private static Map<String, BigDecimal> resultL1;
	/** The result L 2. */
	private static Map<String, BigDecimal> resultL2;

	/**
	 * Instantiates a new intav utils.
	 */
	private IntavUtils() {
	}

	/**
	 * Calculate score.
	 *
	 * @param request         the request
	 * @param idExecution     the id execution
	 * @param messageReources the message reources
	 * @return the score form
	 */
	public static ScoreForm calculateScore(HttpServletRequest request, long idExecution, MessageResources messageReources) {
		Connection conn = null;
		try {
			conn = DataBaseManager.getConnection();
			// Inicializamos el evaluador
			if (!EvaluatorUtility.isInitialized()) {
				EvaluatorUtility.initialize();
			}
			final List<Long> listAnalysis = AnalisisDatos.getAnalysisIdsByTracking(conn, idExecution);
			return generateScores(request, conn, listAnalysis, messageReources);
		} catch (Exception e) {
			Logger.putLog("Error al intentar calcular la puntuación del observatorio", ResultsAction.class, Logger.LOG_LEVEL_ERROR, e);
			return null;
		} finally {
			DataBaseManager.closeConnection(conn);
		}
	}

	/**
	 * Generate scores.
	 *
	 * @param request         the request
	 * @param conn            the conn
	 * @param listAnalysis    the list analysis
	 * @param messageReources the message reources
	 * @return the score form
	 * @throws Exception the exception
	 */
	private static ScoreForm generateScores(HttpServletRequest request, Connection conn, List<Long> listAnalysis, MessageResources messageReources) throws Exception {
		ScoreForm scoreForm = new ScoreForm();
		int suitabilityGroups = 0;
		long idExObs = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
		Connection c = null;
		try {
			c = DataBaseManager.getConnection();
			Evaluator evaluator = new Evaluator();
			for (Long idAnalysis : listAnalysis) {
				Evaluation evaluation = evaluator.getAnalisisDB(conn, idAnalysis, EvaluatorUtils.getDocList(), true);
				String methodology = ObservatorioDAO.getMethodology(c, idExObs);
				String aplicacion = CartuchoDAO.getApplicationFromAnalisisId(c, idAnalysis);
				boolean pointWarning = Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion) ? true : false;
				ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false, pointWarning);
				scoreForm.setTotalScore(scoreForm.getTotalScore().add(evaluationForm.getScore()));
				String pageSuitabilityLevel = ObservatoryUtils.pageSuitabilityLevel(evaluationForm);
				if (pageSuitabilityLevel.equals(Constants.OBS_AA)) {
					scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(BigDecimal.TEN));
				} else if (pageSuitabilityLevel.equals(Constants.OBS_A)) {
					scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(new BigDecimal(5)));
				}
				for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
					suitabilityGroups = levelForm.getSuitabilityGroups().size();
					if (levelForm.getName().equalsIgnoreCase("priority 1")) {
						scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().add(levelForm.getScore()));
					} else if (levelForm.getName().equalsIgnoreCase("priority 2")) {
						scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().add(levelForm.getScore()));
					}
					for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
						if (suitabilityForm.getName().equalsIgnoreCase("A")) {
							scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().add(suitabilityForm.getScore()));
						} else if (suitabilityForm.getName().equalsIgnoreCase("AA")) {
							scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().add(suitabilityForm.getScore()));
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Excepcion: ", AnalysisFromCrawlerAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DataBaseManager.closeConnection(c);
		}
		if (listAnalysis.size() != 0) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().divide(new BigDecimal(listAnalysis.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().divide(new BigDecimal(listAnalysis.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().divide(new BigDecimal(listAnalysis.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().divide(new BigDecimal(listAnalysis.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().divide(new BigDecimal(listAnalysis.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().divide(new BigDecimal(listAnalysis.size()), 2, BigDecimal.ROUND_HALF_UP));
		}
		// El nivel de validación del portal
		// scoreForm.setLevel(getValidationLevel(scoreForm, CrawlerUtils.getResources(request)));
		scoreForm.setLevel(getValidationLevel(scoreForm, messageReources));
		return scoreForm;
	}

	/**
	 * Generate scores.
	 *
	 * @param messageResources the message resources
	 * @param evaList          the eva list
	 * @return the score form
	 * @throws Exception the exception
	 */
	public static ScoreForm generateScores(final MessageResources messageResources, List<ObservatoryEvaluationForm> evaList) throws Exception {
		final ScoreForm scoreForm = new ScoreForm();
		int suitabilityGroups = 0;
		for (ObservatoryEvaluationForm evaluationForm : evaList) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().add(evaluationForm.getScore()));
			String pageSuitabilityLevel = ObservatoryUtils.pageSuitabilityLevel(evaluationForm);
			if (pageSuitabilityLevel.equals(Constants.OBS_AA)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(BigDecimal.TEN));
			} else if (pageSuitabilityLevel.equals(Constants.OBS_A)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(new BigDecimal(5)));
			}
			for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
				suitabilityGroups = levelForm.getSuitabilityGroups().size();
				if (levelForm.getName().equalsIgnoreCase("priority 1")) {
					scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().add(levelForm.getScore()));
				} else if (levelForm.getName().equalsIgnoreCase("priority 2")) {
					scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().add(levelForm.getScore()));
				}
				for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
					if (suitabilityForm.getName().equalsIgnoreCase("A")) {
						scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().add(suitabilityForm.getScore()));
					} else if (suitabilityForm.getName().equalsIgnoreCase("AA")) {
						scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().add(suitabilityForm.getScore()));
					}
				}
			}
		}
		Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
		Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
		List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
		List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
		scoreForm.setVerifications1(labelsL1);
		scoreForm.setVerifications2(labelsL2);
		if (!evaList.isEmpty()) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
		}
		// El nivel de validación del portal
		scoreForm.setLevel(getValidationLevel(scoreForm, messageResources));
		return scoreForm;
	}

	/**
	 * Generate scores accesibity.
	 *
	 * @param messageResources the message resources
	 * @param evaList          the eva list
	 * @return the score form
	 * @throws Exception the exception
	 */
	public static ScoreForm generateScoresAccesibility(final MessageResources messageResources, List<ObservatoryEvaluationForm> evaList) throws Exception {
		final ScoreForm scoreForm = new ScoreForm();
		int suitabilityGroups = 0;
		for (ObservatoryEvaluationForm evaluationForm : evaList) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().add(evaluationForm.getScore()));
			String pageSuitabilityLevel = ObservatoryUtils.pageSuitabilityLevel(evaluationForm);
			if (pageSuitabilityLevel.equals(Constants.OBS_AA)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(BigDecimal.TEN));
			} else if (pageSuitabilityLevel.equals(Constants.OBS_A)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(new BigDecimal(5)));
			}
			for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
				suitabilityGroups = levelForm.getSuitabilityGroups().size();
				if (levelForm.getName().equalsIgnoreCase("priority 1")) {
					scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().add(levelForm.getScore()));
				} else if (levelForm.getName().equalsIgnoreCase("priority 2")) {
					scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().add(levelForm.getScore()));
				}
				for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
					if (suitabilityForm.getName().equalsIgnoreCase("A")) {
						scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().add(suitabilityForm.getScore()));
					} else if (suitabilityForm.getName().equalsIgnoreCase("AA")) {
						scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().add(suitabilityForm.getScore()));
					}
				}
			}
		}
		Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
		Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
		List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
		List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
		scoreForm.setVerifications1(labelsL1);
		scoreForm.setVerifications2(labelsL2);
		if (!evaList.isEmpty()) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
		}
		// El nivel de validación del portal
		scoreForm.setLevel(getValidationLevelAccesibility(evaList, messageResources));
		return scoreForm;
	}

	/**
	 * Gets the failed checks.
	 *
	 * @param request          the request
	 * @param evaluationForm   the evaluation form
	 * @param guideline        the guideline
	 * @param messageResources the message resources
	 * @return the failed checks
	 */
	public static Map<String, List<String>> getFailedChecks(HttpServletRequest request, ObservatoryEvaluationForm evaluationForm, Guideline guideline, MessageResources messageResources) {
		Map<String, List<String>> failedChecks = new TreeMap<>();
		for (Integer idFailed : evaluationForm.getChecksFailed()) {
			for (int i = 0; i < guideline.getGroups().size(); i++) {
				GuidelineGroup levelGroup = guideline.getGroups().get(i);
				for (int j = 0; j < levelGroup.getGroupsVector().size(); j++) {
					GuidelineGroup suitabilityGroup = levelGroup.getGroupsVector().get(j);
					for (int k = 0; k < suitabilityGroup.getGroupsVector().size(); k++) {
						GuidelineGroup subgroup = suitabilityGroup.getGroupsVector().get(k);
						if (subgroup.containsCheck(idFailed) && !thereIsBiggerError(idFailed, subgroup.getRelatedChecks(), evaluationForm.getChecksFailed())) {
							String subgroupName = messageResources.getMessage(CrawlerUtils.getLocale(request), subgroup.getName());
							if (!failedChecks.containsKey(subgroupName)) {
								failedChecks.put(subgroupName, new ArrayList<String>());
							}
							String key = "check.{0}.error".replace("{0}", idFailed.toString());
							if (CrawlerUtils.getResources(request).isPresent(key)) {
								String text = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), key);
								if (subgroup.getOnlyWarningChecks().contains(idFailed)) {
									text += " " + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.check.modality.text.green");
								} else {
									text += " " + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "observatory.check.modality.text.red");
								}
								failedChecks.get(subgroupName).add(text);
							}
						}
					}
				}
			}
		}
		return failedChecks;
	}

	/**
	 * There is bigger error.
	 *
	 * @param idFailed      the id failed
	 * @param relatedChecks the related checks
	 * @param failedChecks  the failed checks
	 * @return true, if successful
	 */
	private static boolean thereIsBiggerError(Integer idFailed, Map<Integer, Integer> relatedChecks, List<Integer> failedChecks) {
		for (Map.Entry<Integer, Integer> relatedCheck : relatedChecks.entrySet()) {
			if (failedChecks.contains(relatedCheck.getKey()) && idFailed.equals(relatedCheck.getValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the validation level.
	 *
	 * @param scoreForm        the score form
	 * @param messageResources the message resources
	 * @return the validation level
	 */
	public static String getValidationLevel(final ScoreForm scoreForm, final MessageResources messageResources) {
		if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal(8)) >= 0) {
			return messageResources.getMessage("resultados.anonimos.num.portales.aa");
		} else if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal("3.5")) <= 0) {
			return messageResources.getMessage("resultados.anonimos.num.portales.nv");
		} else {
			return messageResources.getMessage("resultados.anonimos.num.portales.a");
		}
	}

	/**
	 * Gets the validation level accesibility.
	 *
	 * @param resultData       the result data
	 * @param messageResources the message resources
	 * @return the validation level accesibility
	 */
	public static String getValidationLevelAccesibility(List<ObservatoryEvaluationForm> resultData, final MessageResources messageResources) {
		boolean greenv1 = false;
		boolean greenv2 = false;
		boolean greenv3 = false;
		boolean greenv4 = false;
		Boolean greenv5 = null;
		for (ObservatoryEvaluationForm observatoryEvaluationForm : resultData) {
			for (ObservatoryLevelForm observatoryLevelForm : observatoryEvaluationForm.getGroups()) {
				if (Constants.OBS_PRIORITY_1.equals(observatoryLevelForm.getName())) {
					for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
						for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
							// Se comprueba si puntúa o no puntúa
							if (observatorySubgroupForm.getValue() != Constants.OBS_VALUE_NOT_SCORE) {
								// Si puntúa, se isNombreValido si se le da un 0 o un 1
								if (observatorySubgroupForm.getValue() == Constants.OBS_VALUE_GREEN_ONE) {
									switch (observatorySubgroupForm.getDescription()) {
									case "minhap.observatory.5_0.subgroup.3.1":
										greenv1 = true;
										break;
									case "minhap.observatory.5_0.subgroup.3.2":
										greenv2 = true;
										break;
									case "minhap.observatory.5_0.subgroup.3.3":
										greenv3 = true;
										break;
									case "minhap.observatory.5_0.subgroup.3.4":
										greenv4 = true;
										break;
									case "minhap.observatory.5_0.subgroup.3.5":
										greenv5 = true;
										break;
									}
								}
							} else {
							}
						}
					}
				}
			}
		}
		if (greenv1 && greenv2 && greenv3 && greenv4 && ((greenv5 != null && true) || greenv5 == null)) {
			return Constants.OBS_ACCESIBILITY_FULL;
		} else if (greenv1) {
			if (!greenv2) {
				return Constants.OBS_ACCESIBILITY_NONE;
			} else {
				return Constants.OBS_ACCESIBILITY_PARTIAL;
			}
		} else {
			return Constants.OBS_ACCESIBILITY_NA;
		}
	}

	/**
	 * Generate scores 2.
	 *
	 * @param messageResources the message resources
	 * @param evaList          the eva list
	 * @return the score form
	 */
	public static ScoreForm generateScores2(final MessageResources messageResources, final java.util.List<ObservatoryEvaluationForm> evaList) {
		final ScoreForm scoreForm = new ScoreForm();
		int suitabilityGroups = 0;
		BigDecimal totalScore = new BigDecimal(0);
		for (ObservatoryEvaluationForm evaluationForm : evaList) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().add(evaluationForm.getScore()));
			// Codigo duplicado en IntavUtils
			final String pageSuitabilityLevel = ObservatoryUtils.pageSuitabilityLevel(evaluationForm);
			if (pageSuitabilityLevel.equals(Constants.OBS_AA)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(BigDecimal.TEN));
			} else if (pageSuitabilityLevel.equals(Constants.OBS_A)) {
				scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(new BigDecimal(5)));
			}
			for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
				suitabilityGroups = levelForm.getSuitabilityGroups().size();
				if (levelForm.getName().equalsIgnoreCase("priority 1")) {
					scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().add(levelForm.getScore()));
				} else if (levelForm.getName().equalsIgnoreCase("priority 2")) {
					scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().add(levelForm.getScore()));
				}
				for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
					if (suitabilityForm.getName().equalsIgnoreCase("A")) {
						scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().add(suitabilityForm.getScore()));
					} else if (suitabilityForm.getName().equalsIgnoreCase("AA")) {
						scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().add(suitabilityForm.getScore()));
					}
				}
			}
		}
		generateScoresVerificacion(messageResources, scoreForm, evaList);
		Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(evaList, Constants.OBS_PRIORITY_NONE);
		Map<Long, String> calculatedCompliance = calculateCrawlingCompliance(results);
		/**
		 * for (ResultadoSemillaForm seedResult : seedsResults) { seedResult.setCompliance(calculatedCompliance.get(Long.parseLong(seedResult.getIdFulfilledCrawling()))); }
		 */
		if (!evaList.isEmpty()) {
			scoreForm.setTotalScore(scoreForm.getTotalScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			// Calculate mid from score verificatrion
			BigDecimal sumL1 = new BigDecimal(0);
			int countNA = 0;
			for (Entry<String, BigDecimal> entry : resultL1.entrySet()) {
				if (entry.getValue().compareTo(new BigDecimal(0)) < 0) {
					countNA++;
				} else {
					sumL1 = sumL1.add(entry.getValue());
				}
			}
			scoreForm.setScoreLevelA(sumL1.divide(new BigDecimal(resultL1.size() - countNA), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevel1(sumL1.divide(new BigDecimal(resultL1.size() - countNA), 2, BigDecimal.ROUND_HALF_UP));
			// Calculate mid from score verificatrion
			BigDecimal sumL2 = new BigDecimal(0);
			countNA = 0;
			for (Entry<String, BigDecimal> entry : resultL2.entrySet()) {
				if (entry.getValue().compareTo(new BigDecimal(0)) < 0) {
					countNA++;
				} else {
					sumL2 = sumL2.add(entry.getValue());
				}
			}
			scoreForm.setScoreLevel2(sumL2.divide(new BigDecimal(resultL2.size() - countNA), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setScoreLevelAA(sumL2.divide(new BigDecimal(resultL2.size() - countNA), 2, BigDecimal.ROUND_HALF_UP));
			scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
			// REVIEW Calculated compliance
			scoreForm.setCompliance(calculatedCompliance.get(evaList.get(0).getCrawlerExecutionId()));
		}
		// El nivel de validación del portal
		scoreForm.setLevel(getValidationLevel(scoreForm, messageResources));
		return scoreForm;
	}

	/**
	 * Generate scores verificacion.
	 *
	 * @param messageResources the message resources
	 * @param scoreForm        the score form
	 * @param evaList          the eva list
	 */
	protected static void generateScoresVerificacion(MessageResources messageResources, ScoreForm scoreForm, java.util.List<ObservatoryEvaluationForm> evaList) {
		resultL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_1);
		resultL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPoint(evaList, Constants.OBS_PRIORITY_2);
		final java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
		final java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioUNEEN2019Utils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
		scoreForm.setVerifications1(labelsL1);
		scoreForm.setVerifications2(labelsL2);
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