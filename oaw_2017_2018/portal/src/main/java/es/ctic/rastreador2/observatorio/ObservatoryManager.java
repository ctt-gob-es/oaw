package es.ctic.rastreador2.observatorio;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.rastreador2.pdf.utils.RankingInfo;
import es.inteco.rastreador2.utils.ObservatoryUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase para manejar Observatorios
 *
 * @author miguel.garcia@fundacionctic.org
 */
public class ObservatoryManager {

    public ObservatoryManager() {
        // Inicializamos el evaluador si hace falta
        if (!EvaluatorUtility.isInitialized()) {
            try {
                EvaluatorUtility.initialize();
            } catch (Exception e) {
                Logger.putLog("Exception: ", ObservatoryManager.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    /**
     * Devuelve los análisis de las páginas correspondientes a la ejecución de un rastreo con la metodología del observatorio.
     *
     * @param idObservatoryExecution identificador correspondiente a una ejecución (iteración) de un observatorio
     * @param evaluationIds          identificadores de análisis de página que se quieren evaluar
     * @return una lista con las evaluaciones de las páginas correspondientes a la ejecución indicada
     */
    public List<ObservatoryEvaluationForm> getObservatoryEvaluationsFromObservatoryExecution(final long idObservatoryExecution, final List<Long> evaluationIds) {
        final List<ObservatoryEvaluationForm> evaluationList = new ArrayList<>(evaluationIds.size());
        try (Connection c = DataBaseManager.getConnection()) {
            final String methodology = ObservatorioDAO.getMethodology(c, idObservatoryExecution);
            final Evaluator evaluator = new Evaluator();
            for (Long id : evaluationIds) {
                try {
                    final Evaluation evaluation = evaluator.getAnalisisDB(c, id, EvaluatorUtils.getDocList(), false);
                    final ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, true);
                    evaluationList.add(evaluationForm);
                } catch (ParserConfigurationException e) {
                    Logger.putLog("Exception: ", ObservatoryManager.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ObservatoryManager.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return evaluationList;
    }

    public RankingInfo calculatePreviousRanking(final Long idObservatoryExecution, final SemillaForm currentSeed) {
        try (Connection c = DataBaseManager.getConnection()) {
            final Long previousObservatoryExecution = ObservatorioDAO.getPreviousObservatoryExecution(c, idObservatoryExecution);
            return calculateRanking(previousObservatoryExecution, currentSeed);
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }
    }

    /**
     * Calcula el ranking (posición a nivel global y en su segmento) de una semilla
     *
     * @param idObservatoryExecution el identificador de una iteracción de un observatorio
     * @param currentSeed            la semilla de la que hay que calcular su ranking en la iteracción
     * @return un objeto RankingInfo con la información de ranking
     */
    public RankingInfo calculateRanking(final Long idObservatoryExecution, final SemillaForm currentSeed) {
        try (Connection c = DataBaseManager.getConnection()) {
            List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, new SemillaForm(), idObservatoryExecution, (long) 0, Constants.NO_PAGINACION);
            if (seedsResults.isEmpty()) {
                return null;
            }
            final RankingInfo rankingInfo = new RankingInfo();
            rankingInfo.setGlobalSeedsNumber(seedsResults.size());
            rankingInfo.setCategorySeedsNumber(0);
            rankingInfo.setGlobalRank(1);
            rankingInfo.setCategoryRank(1);
            rankingInfo.setCategoria(currentSeed.getCategoria());
            final ObservatorioRealizadoForm observatoryRealizadoForm = ObservatorioDAO.getFulfilledObservatory(c, ObservatorioDAO.getObservatoryFormFromExecution(c, idObservatoryExecution).getId(), idObservatoryExecution);

            final PropertiesManager pmgr = new PropertiesManager();
            final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
            rankingInfo.setDate(df.format(observatoryRealizadoForm.getFecha()));

            seedsResults = ObservatoryUtils.setAvgScore(c, seedsResults, idObservatoryExecution);
            // Buscamos la puntuación concreta de la semilla
            for (ResultadoSemillaForm seedForm : seedsResults) {
                if (Long.parseLong(seedForm.getId()) == currentSeed.getId()) {
                    rankingInfo.setScore(new BigDecimal(seedForm.getScore()));
                }
            }

            // Miramos el ranking comparando con el resto de semillas
            for (ResultadoSemillaForm seedForm : seedsResults) {
                if (seedForm.getScore() != null) {
                    final BigDecimal seedFormScore = new BigDecimal(seedForm.getScore());
                    if (seedFormScore.compareTo(rankingInfo.getScore()) > 0) {
                        rankingInfo.incrementGlobalRank();
                    }
                    if (currentSeed.getCategoria().getId().equals(String.valueOf(seedForm.getIdCategory()))) {
                        rankingInfo.setCategorySeedsNumber(rankingInfo.getCategorySeedsNumber() + 1);
                        if (seedFormScore.compareTo(rankingInfo.getScore()) > 0) {
                            rankingInfo.incrementCategoryRank();
                        }
                    }
                }
            }
            return rankingInfo;
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return null;
    }

    /**
     * Obtiene el rastreo previo a un determinado rastreo de un observatorio
     *
     * @param idRastreo              id del rastreo del que se quiere obtener el rastreo previo
     * @param idObservatoryExecution id del observatorio al que pertenece el rastreo.
     * @return el id del rastreo o 0 si no existe
     */
    public long getPreviousIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(final Long idRastreo, final long idObservatoryExecution) {
        try (Connection c = DataBaseManager.getConnection()) {
            return RastreoDAO.getIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(c, idRastreo, idObservatoryExecution);
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return 0;
    }
}
