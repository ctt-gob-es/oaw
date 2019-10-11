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

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.*;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.export.database.*;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.intav.utils.IntavUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosPrimariosObservatorioIntavUtils;
import org.apache.struts.util.MessageResources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class DatabaseExportUtils {

    private DatabaseExportUtils() {
    }

    public static Observatory getObservatoryInfo(final MessageResources messageResources, final Long idExecution) throws Exception {
        final Observatory observatory = new Observatory();

        observatory.setIdExecution(idExecution);

        final List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idExecution), Constants.COMPLEXITY_SEGMENT_NONE, null);

        // Número de portales por modalidad
        final Map<String, Integer> result = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
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
        final Map<String, BigDecimal> verificationAndModality = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_NONE);
        for (String key : verificationAndModality.keySet()) {
            if (!observatory.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""))) &&
                    !observatory.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")))) {
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
        final Map<String, BigDecimal> verificationAndScore = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_NONE);
        for (String key : verificationAndScore.keySet()) {
            VerificationScore verificationScore = new VerificationScore();
            verificationScore.setVerification(getVerificationId(key));
            verificationScore.setObservatory(observatory);
            if (verificationAndScore.get(key).intValue() != -1) {
                verificationScore.setScore(verificationAndScore.get(key));
            }
            observatory.getVerificationScoreList().add(verificationScore);
        }

        // Puntuaciones por aspectos
        final Map<String, BigDecimal> aspectAndScore = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
        for (String key : aspectAndScore.keySet()) {
            AspectScore aspectScore = new AspectScore();
            aspectScore.setAspect(key);
            aspectScore.setScore(aspectAndScore.get(key));
            aspectScore.setObservatory(observatory);
            observatory.getAspectScoreList().add(aspectScore);
        }

        return observatory;
    }

    public static Category getCategoryInfo(final MessageResources messageResources, final CategoriaForm categoriaForm, final Observatory observatory) throws Exception {
        final Category category = new Category();

        category.setName(categoriaForm.getName());
        category.setObservatory(observatory);
        category.setIdCrawlerCategory(Long.valueOf(categoriaForm.getId()));

        final List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(observatory.getIdExecution()), Long.parseLong(categoriaForm.getId()), null);

        // Número de portales por modalidad
        final Map<String, Integer> result = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
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
        final Map<String, BigDecimal> verificationAndModality = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_NONE);
        for (String key : verificationAndModality.keySet()) {
            if (!category.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""))) &&
                    !category.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")))) {
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
        final Map<String, BigDecimal> verificationAndScore = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_NONE);
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
        final Map<String, BigDecimal> aspectAndScore = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(messageResources, pageExecutionList);
        for (String key : aspectAndScore.keySet()) {
            AspectScore aspectScore = new AspectScore();
            aspectScore.setAspect(key);
            aspectScore.setScore(aspectAndScore.get(key));
            aspectScore.setCategory(category);
            category.getAspectScoreList().add(aspectScore);
        }

        // Puntuación de cada modalidad
        final List<CategoriaForm> categories = new ArrayList<>();
        categories.add(categoriaForm);
        final Map<CategoriaForm, Map<String, BigDecimal>> resultDataBySegment = ResultadosAnonimosObservatorioIntavUtils.calculateMidPuntuationResultsBySegmentMap(observatory.getIdExecution().toString(), pageExecutionList, categories);
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
            final List<ObservatorySiteEvaluationForm> observatorySiteEvaluations = ResultadosAnonimosObservatorioIntavUtils.getSitesListByLevel(pageExecutionList);
            for (ObservatorySiteEvaluationForm observatorySiteEvaluationForm : observatorySiteEvaluations) {
                final Site site = getSiteInfo(messageResources, observatorySiteEvaluationForm, category);
                category.getSiteList().add(site);
            }
        } catch (Exception e) {
            Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return category;
    }

    public static Site getSiteInfo(final MessageResources messagesResources, ObservatorySiteEvaluationForm observatorySiteEvaluationForm, Category category) throws Exception {
        final Site site = new Site();

        site.setCategory(category);
        site.setName(observatorySiteEvaluationForm.getName());
        site.setScore(observatorySiteEvaluationForm.getScore());
        site.setLevel(observatorySiteEvaluationForm.getLevel());
        site.setIdCrawlerSeed(observatorySiteEvaluationForm.getIdSeed());


        final ScoreForm scoreForm = IntavUtils.generateScores(messagesResources, observatorySiteEvaluationForm.getPages());
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
        final Map<String, BigDecimal> verificationAndScore = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(observatorySiteEvaluationForm.getPages(), Constants.OBS_PRIORITY_NONE);
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
        final Map<String, BigDecimal> verificationAndModality = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(observatorySiteEvaluationForm.getPages(), Constants.OBS_PRIORITY_NONE);
        for (String key : verificationAndModality.keySet()) {
            if (!site.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_GREEN_SUFFIX, ""))) &&
                    !site.getVerificationModalityList().contains(new VerificationModality(key.replace(Constants.OBS_VALUE_RED_SUFFIX, "")))) {
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
        final Map<String, BigDecimal> aspectAndScore = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(messagesResources, observatorySiteEvaluationForm.getPages());
        for (String key : aspectAndScore.keySet()) {
            AspectScore aspectScore = new AspectScore();
            aspectScore.setAspect(key);
            aspectScore.setScore(aspectAndScore.get(key));
            aspectScore.setSite(site);
            site.getAspectScoreList().add(aspectScore);
        }

        for (ObservatoryEvaluationForm observatoryEvaluationForm : observatorySiteEvaluationForm.getPages()) {
            Page page = getPageInfo(messagesResources, observatoryEvaluationForm, site);
            site.getPageList().add(page);
        }

        return site;
    }

    public static Page getPageInfo(final MessageResources messageResources, ObservatoryEvaluationForm observatoryEvaluationForm, Site site) {
        Page page = new Page();

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
            aspectScore.setScore(aspectScoreForm.getScore());
            aspectScore.setPage(page);
            page.getAspectScoreList().add(aspectScore);
        }

        return page;
    }

    private static String getVerificationId(final String verification) {
        final PropertiesManager pmgr = new PropertiesManager();
        final Pattern pattern = Pattern.compile(pmgr.getValue(CRAWLER_PROPERTIES, "verification.id.reg.exp"), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(verification);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return verification;
        }
    }

}