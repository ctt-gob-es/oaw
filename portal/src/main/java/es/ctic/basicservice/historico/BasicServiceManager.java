package es.ctic.basicservice.historico;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import com.opensymphony.oscache.base.NeedsRefreshException;
import es.ctic.rastreador2.observatorio.ObservatoryManager;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.SeedForm;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by mikunis on 1/10/17.
 */
public class BasicServiceManager {

    private final ObservatoryManager observatoryManager = new ObservatoryManager();

    public Map<Date, List<ObservatoryEvaluationForm>> resultEvolutionData(final Long idCrawling) {
        final Map<Date, List<ObservatoryEvaluationForm>> resultData = new TreeMap<>();

//        try (Connection c = DataBaseManager.getConnection()) {
//            final List<Long> evaluationIds = DiagnosisDAO.getEvaluationIds(idCrawling);
//            observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(-1, evaluationIds);
//            // cod_rastreo de tanalisis -> <cod_analisis> -> <Analysis> ->
//            final Map<Long, Date> executedObservatoryIdMap = ObservatorioDAO.getObservatoryExecutionIds(c, observatoryId, executionId, observatoryForm.getCartucho().getId());
//            for (Map.Entry<Long, Date> entry : executedObservatoryIdMap.entrySet()) {
//                final List<ObservatoryEvaluationForm> pageList = getGlobalResultData(String.valueOf(entry.getKey()), Constants.COMPLEXITY_SEGMENT_NONE, null);
//                resultData.put(entry.getValue(), pageList);
//            }
//        } catch (Exception e) {
//            Logger.putLog("Exception: ", BasicServiceManager.class, Logger.LOG_LEVEL_ERROR, e);
//        }

        return resultData;
    }

    private List<ObservatoryEvaluationForm> getGlobalResultData(final String executionId, final long categoryId, final List<ObservatoryEvaluationForm> pageExecutionList) throws Exception {
        return getGlobalResultData(executionId, categoryId, pageExecutionList, null);
    }

    private List<ObservatoryEvaluationForm> getGlobalResultData(final String executionId, final long categoryId, final List<ObservatoryEvaluationForm> pageExecutionList, final Long idCrawler) throws Exception {
        List<ObservatoryEvaluationForm> observatoryEvaluationList;
        try {
            observatoryEvaluationList = (List<ObservatoryEvaluationForm>) CacheUtils.getFromCache(Constants.OBSERVATORY_KEY_CACHE + executionId);
        } catch (NeedsRefreshException nre) {
            Logger.putLog("La cache con id " + Constants.OBSERVATORY_KEY_CACHE + executionId + " no está disponible, se va a regenerar", BasicServiceManager.class, Logger.LOG_LEVEL_INFO);
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
                        final ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false);
                        evaluationForm.setObservatoryExecutionId(Long.parseLong(executionId));
                        final FulfilledCrawlingForm ffCrawling = RastreoDAO.getFullfilledCrawlingExecution(c, evaluationForm.getCrawlerExecutionId());
                        if (ffCrawling != null) {
                            final SeedForm seedForm = new SeedForm();
                            seedForm.setId(String.valueOf(ffCrawling.getSeed().getId()));
                            seedForm.setAcronym(ffCrawling.getSeed().getAcronimo());
                            seedForm.setName(ffCrawling.getSeed().getNombre());
                            seedForm.setDependence(ffCrawling.getSeed().getDependencia());
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
                Logger.putLog("Error en getGlobalResultData", BasicServiceManager.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            CacheUtils.putInCacheForever(observatoryEvaluationList, Constants.OBSERVATORY_KEY_CACHE + executionId);
        }

        return filterObservatoriesByComplexity(observatoryEvaluationList, Long.parseLong(executionId), categoryId);
    }

    private List<ObservatoryEvaluationForm> filterObservatoriesByComplexity(final List<ObservatoryEvaluationForm> observatoryEvaluationList, final Long executionId, final long categoryId) throws Exception {
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
                Logger.putLog("Error al filtrar observatorios. ", BasicServiceManager.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
            return results;
        }
    }
}
