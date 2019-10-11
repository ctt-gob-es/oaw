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

import ca.utoronto.atrc.tile.accessibilitychecker.*;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.intav.action.AnalysisFromCrawlerAction;
import es.inteco.rastreador2.intav.action.ResultsAction;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class IntavUtils {

    private IntavUtils() {
    }

    public static ScoreForm calculateScore(HttpServletRequest request, long idExecution) {
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();
            // Inicializamos el evaluador
            if (!EvaluatorUtility.isInitialized()) {
                EvaluatorUtility.initialize();
            }

            final List<Long> listAnalysis = AnalisisDatos.getAnalysisIdsByTracking(conn, idExecution);

            return generateScores(request, conn, listAnalysis);
        } catch (Exception e) {
            Logger.putLog("Error al intentar calcular la puntuación del observatorio", ResultsAction.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }

    private static ScoreForm generateScores(HttpServletRequest request, Connection conn, List<Long> listAnalysis) throws Exception {
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
                ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false,false);
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
        scoreForm.setLevel(getValidationLevel(scoreForm, CrawlerUtils.getResources(request)));

        return scoreForm;
    }

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

    private static boolean thereIsBiggerError(Integer idFailed, Map<Integer, Integer> relatedChecks, List<Integer> failedChecks) {
        for (Map.Entry<Integer, Integer> relatedCheck : relatedChecks.entrySet()) {
            if (failedChecks.contains(relatedCheck.getKey()) && idFailed.equals(relatedCheck.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static String getValidationLevel(final ScoreForm scoreForm, final MessageResources messageResources) {
        if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal(8)) >= 0) {
            return messageResources.getMessage("resultados.anonimos.num.portales.aa");
        } else if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal("3.5")) <= 0) {
            return messageResources.getMessage("resultados.anonimos.num.portales.nv");
        } else {
            return messageResources.getMessage("resultados.anonimos.num.portales.a");
        }
    }

}