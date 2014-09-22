package es.inteco.plugin;

import es.inteco.common.logging.Logger;
import es.inteco.multilanguage.bean.AnalysisConfiguration;
import es.inteco.multilanguage.common.Constants;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.service.AnalyzeService;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.multilanguage.service.utils.SpecialCasesUtils;
import org.hibernate.HibernateException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MultilanguagePlugin extends Cartucho {

    @Override
    public void analyzer(Map<String, Object> datos) {
        Logger.putLog("Iniciando el análisis de multilingüismo para la url " + (String) datos.get("url"), MultilanguagePlugin.class, Logger.LOG_LEVEL_INFO);
        Analysis analysis = new Analysis();
        analysis.setIdCrawling((Long) datos.get("idFulfilledCrawling"));
        analysis.setContent((String) datos.get("contenido"));
        analysis.setUrl((String) datos.get("url"));
        analysis.setStatus(Constants.STATUS_STARTED);
        analysis.setDate(new Date());

        boolean isLast = (Boolean) datos.get("isLast");
        try {

            analysis = (Analysis) AnalysisManager.save(analysis);

            AnalysisConfiguration analysisConfiguration = MultilanguageUtils.loadConfiguration((String) datos.get("acronimo"));

            AnalyzeService service = null;
            if (analysisConfiguration == null || analysisConfiguration.getType() == null || analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_1) {
                service = new AnalyzeService();
                // Analizamos y guardamos
                service.analyze(analysis, (String) datos.get("cookie"));
                analysis.setStatus(Constants.STATUS_FINISHED);
            } else if (analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_2 || analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_3 || analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_5) {
                service = new AnalyzeService();
                // Analizamos y guardamos
                service.analyze(analysis, (String) datos.get("cookie"));
                analysis.setStatus(Constants.STATUS_TO_REANALYZE);
            }

            AnalysisManager.update(analysis);

            if (isLast) {
                List<Analysis> analysisList = AnalysisManager.getAnalysisByExecution(analysis.getIdCrawling());
                if (analysisConfiguration != null && analysisConfiguration.getType() != null) {
                    if (analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_2) {
                        SpecialCasesUtils.manageSpecialCase2(analysisList, analysisConfiguration);
                    } else if (analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_3) {
                        SpecialCasesUtils.manageSpecialCase3(analysisList, analysisConfiguration);
                    } else if (analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_4) {
                        SpecialCasesUtils.manageSpecialCase4(analysisList, analysisConfiguration);
                    } else if (analysisConfiguration.getType() == Constants.ANALYSIS_TYPE_5) {
                        SpecialCasesUtils.manageSpecialCase5(analysisList, analysisConfiguration);
                    }
                }

            }
        } catch (HibernateException he) {
            Logger.putLog("Error al almacenar los datos del análisis", MultilanguagePlugin.class, Logger.LOG_LEVEL_ERROR, he);
        } catch (Exception e) {
            Logger.putLog("Error al realizar el análisis de multilingüismo", MultilanguagePlugin.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void setConfig(long idRastreo) {
        Logger.putLog("Aplicando configuración al cartucho de multilingüismo", MultilanguagePlugin.class, Logger.LOG_LEVEL_INFO);
    }

}
