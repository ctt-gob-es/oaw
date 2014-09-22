package es.inteco.accesibilidad;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.Cartucho;

import java.util.Map;

/**
 * Implementación de un cartucho que analiza las urls, así como el contenido
 * de las páginas y clasificarlas como maliciosas o no.
 */
public class CartuchoAccesibilidad extends Cartucho {

    @Override
    public void analyzer(final Map<String, Object> datos) {
        Logger.putLog("Iniciando evaluación de accesibilidad desde el rastreador de la url: " + datos.get("url"), CartuchoAccesibilidad.class, Logger.LOG_LEVEL_INFO);

        PropertiesManager pmgr = new PropertiesManager();

        CheckAccessibility checkAccesibility = new CheckAccessibility();
        checkAccesibility.setEntity((String) datos.get("entity"));
        checkAccesibility.setGuideline(datos.get("guidelineFile").toString().substring(0, datos.get("guidelineFile").toString().lastIndexOf('.')));
        checkAccesibility.setGuidelineFile(datos.get("guidelineFile").toString());
        checkAccesibility.setLevel(pmgr.getValue("crawler.core.properties", "check.accessibility.default.level"));
        checkAccesibility.setUrl((String) datos.get("url"));
        checkAccesibility.setIdRastreo((Long) datos.get("idFulfilledCrawling"));
        checkAccesibility.setIdObservatory((Long) datos.get("idObservatory"));
        checkAccesibility.setContent((String) datos.get("contenido"));

        boolean isLast = (Boolean) datos.get("isLast");

        try {
            EvaluatorUtils.evaluateContent(checkAccesibility, pmgr.getValue("crawler.core.properties", "check.accessibility.default.language"));
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", CartuchoAccesibilidad.class, Logger.LOG_LEVEL_ERROR, e);
        }

        if (isLast) {
            CacheUtils.removeFromCache(IntavConstants.CHECKED_LINKS_CACHE_KEY + checkAccesibility.getIdRastreo());
        }
    }

    public void setConfig(final long idRastreo) {
        if (!EvaluatorUtility.isInitialized()) {
            try {
                EvaluatorUtility.initialize();
            } catch (Exception e) {
                Logger.putLog("Exception: ", CartuchoAccesibilidad.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

}
