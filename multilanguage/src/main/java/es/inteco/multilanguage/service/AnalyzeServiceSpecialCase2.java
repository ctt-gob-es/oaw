package es.inteco.multilanguage.service;

import es.inteco.multilanguage.bean.AnalysisConfiguration;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;

import java.util.List;

public class AnalyzeServiceSpecialCase2 {

    /**
     * Servicio de análisis especial para esos 3 ó 4 portales que solo tienen el cambio de idioma en la Home
     *
     * @param analysis
     * @param cookie
     * @return
     * @throws Exception
     */
    public List<Analysis> analyze(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) throws Exception {
        return MultilanguageUtils.getLanguagesFromHome(analysisList);
    }
}
