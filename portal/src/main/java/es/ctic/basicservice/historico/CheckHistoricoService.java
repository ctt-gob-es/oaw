package es.ctic.basicservice.historico;

import es.ctic.rastreador2.observatorio.ObservatoryManager;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

/**
 * Clase para manejar el histórico de resultados del servicio de diagnostico
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class CheckHistoricoService {

    /**
     * Obtiene el historico de resultados previos asociado a una url
     *
     * @param url la url de la que buscar el histórico de resultados asociados
     * @return una lista con un máximo de 3 resultados del servicio de diagnostico asociado a la url
     */
    public final List<BasicServiceResultado> getHistoricoResultados(final String url) {
        if (url != null && url.trim().length() > 0) {
            return DiagnosisDAO.getHistoricoResultados(url);
        }
        return Collections.emptyList();
    }

    /**
     * Comprueba si un identificador de analisis está asociado a una determinada url
     *
     * @param idAnalysis el identificador del analisis
     * @param url        la url
     * @return true si para esa url hay un analisis con ese identificador o false en caso contrario
     */
    public final boolean isAnalysisOfUrl(final String idAnalysis, final String url) {
        final List<BasicServiceResultado> historicoResultados = getHistoricoResultados(url);
        for (BasicServiceResultado basicServiceResultado : historicoResultados) {
            if (basicServiceResultado.getId().equals(idAnalysis)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Obtiene el histórico de resultados de una petición al servicio de diagnóstico
     *
     * @param basicServiceForm la petición al servicio de diagnóstico
     * @return un Map ordenado por fecha de resultado y para cada entrada una lista con los resultados de cada página
     */
    public Map<Date, List<ObservatoryEvaluationForm>> getHistoricoResultadosOfBasicService(final BasicServiceForm basicServiceForm) {
        final ObservatoryManager observatoryManager = new ObservatoryManager();
        final PropertiesManager pmgr = new PropertiesManager();
        final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = new TreeMap<>();
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

        if (basicServiceForm.isRegisterAnalysis()) {
            final List<BasicServiceResultado> historicoResultados = getHistoricoResultados(basicServiceForm.getDomain());
            for (BasicServiceResultado historicoResultado : historicoResultados) {
                if (!historicoResultado.getId().equals(String.valueOf(basicServiceForm.getId()))) {
                    try {
                        final List<Long> analysisIds = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), -1 * Long.parseLong(historicoResultado.getId()));
                        previousEvaluationsPageList.put(dateFormat.parse(historicoResultado.getDate()), observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIds));
                    } catch (Exception e) {
                        Logger.putLog(String.format("Error al obtener el historico de resultado del historico %s", historicoResultado.getId()), CheckHistoricoService.class, Logger.LOG_LEVEL_ERROR, e);
                    }

                }
            }
        }
        return previousEvaluationsPageList;
    }

    /**
     * Borra un análisis
     *
     * @param name             nombre del análisis (nombre del observatorio o url si proviene del servicio de diagnóstico)
     * @param analysisToDelete identificador del rastreo
     */
    public final void deleteAnalysis(final String name, final String analysisToDelete) {
        try (Connection conn = DataBaseManager.getConnection()) {
            conn.setAutoCommit(false);
            // Los identificadores de rastreo del servicio de diagnóstico son negativos
            final long idAnalisis = Math.abs(Long.parseLong(analysisToDelete)) * -1;
            try {
                DiagnosisDAO.deleteAnalysis(conn, name, idAnalisis);
                DiagnosisDAO.deregisterResult(conn, Long.parseLong(analysisToDelete));
            } catch (SQLException sqlException) {
                conn.rollback();
                throw sqlException;
            }
            conn.commit();
        } catch (Exception e) {
            Logger.putLog(String.format("No se ha podido borrar el análisis %s asociado a %s", analysisToDelete, name), CheckHistoricoService.class, Logger.LOG_LEVEL_WARNING, e);
        }
    }
}
