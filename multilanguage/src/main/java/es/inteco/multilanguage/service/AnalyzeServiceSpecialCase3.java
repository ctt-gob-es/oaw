package es.inteco.multilanguage.service;

import es.inteco.multilanguage.bean.AnalysisConfiguration;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.persistence.LanguageFound;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.multilanguage.service.utils.SpecialCasesUtils;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeServiceSpecialCase3 {

    /**
     * Servicio de análisis especial para esos 3 ó 4 portales que solo tienen el cambio de idioma en la Home
     *
     * @param analysis
     * @param cookie
     * @return
     * @throws Exception
     */

    public List<Analysis> analyze(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) throws Exception {
        for (Analysis analysis : analysisList) {
            List<LanguageFound> languagesFoundList = new ArrayList<LanguageFound>();
            languagesFoundList.addAll(analysis.getLanguagesFound());

            for (LanguageFound language : analysis.getLanguagesFound()) {
                AnalysisManager.delete(language);
            }

            analysis.getLanguagesFound().clear();
            for (LanguageFound language : languagesFoundList) {
                String cookie = SpecialCasesUtils.getCookie(language);
                analysis = SpecialCasesUtils.analyze(analysis.getUrl(), analysis, cookie, language.getLanguage());
            }
            analysis.setLanguagesFound(MultilanguageUtils.deleteDuplicateLanguages(analysis.getLanguagesFound()));
        }
        return analysisList;
    }
}
