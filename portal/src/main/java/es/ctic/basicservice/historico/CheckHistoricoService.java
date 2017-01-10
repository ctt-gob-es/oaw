package es.ctic.basicservice.historico;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

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
    public List<BasicServiceResultado> getHistoricoResultados(final String url) {
        // Añadir comprobación de que existe registro en tanalisis para que no salgan análisis antiguos que estén registrados en basic_service pero no haya datos en tanalisis
        if (url != null && url.length() > 0) {
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
    public boolean isAnalysisOfUrl(final String idAnalysis, final String url) {
        final List<BasicServiceResultado> historicoResultados = getHistoricoResultados(url);
        for (BasicServiceResultado basicServiceResultado : historicoResultados) {
            if (basicServiceResultado.getId().equals(idAnalysis)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Borra un análisis
     *
     * @param name             nombre del análisis (nombre del observatorio o url si proviene del servicio de diagnóstico)
     * @param analysisToDelete identificador del rastreo
     */
    public void deleteAnalysis(final String name, final String analysisToDelete) {
        try (Connection conn = DataBaseManager.getConnection()) {
            DiagnosisDAO.deleteAnalysis(conn, name, Long.parseLong(analysisToDelete));
        } catch (Exception e) {
            Logger.putLog(String.format("No se ha podido borrar el análisis %s asociado a %s", analysisToDelete, name), CheckHistoricoService.class, Logger.LOG_LEVEL_WARNING, e);
        }
    }
}
