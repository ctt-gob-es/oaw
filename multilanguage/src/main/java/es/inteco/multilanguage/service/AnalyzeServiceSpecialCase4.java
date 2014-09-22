package es.inteco.multilanguage.service;

import es.inteco.multilanguage.bean.AnalysisConfiguration;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;

import java.util.List;

public class AnalyzeServiceSpecialCase4 {

    public List<Analysis> analyze(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) throws Exception {
        return MultilanguageUtils.getUrlsFromXml(analysisList, analysisConfiguration);
    }
}
