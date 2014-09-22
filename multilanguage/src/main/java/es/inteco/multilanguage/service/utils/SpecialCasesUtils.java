package es.inteco.multilanguage.service.utils;

import es.inteco.common.logging.Logger;
import es.inteco.multilanguage.bean.AnalysisConfiguration;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.persistence.Language;
import es.inteco.multilanguage.persistence.LanguageFound;
import es.inteco.multilanguage.service.*;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class SpecialCasesUtils {
    public static void manageSpecialCase2(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) {
        try {
            Logger.putLog("Analizando caso especial 2: solo hay enlaces de cambio de idioma en la Home", SpecialCasesUtils.class, Logger.LOG_LEVEL_INFO);
            AnalyzeServiceSpecialCase2 service = new AnalyzeServiceSpecialCase2();
            service.analyze(analysisList, analysisConfiguration);

            for (Analysis analysis : analysisList) {
                AnalysisManager.update(analysis);
            }
        } catch (Exception e) {
            Logger.putLog("Error al intentar analizar el análisis como caso especial 2", SpecialCasesUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void manageSpecialCase3(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) {
        try {
            Logger.putLog("Analizando caso especial 3: todos los enlaces de cambio de idioma llevan a la Home", SpecialCasesUtils.class, Logger.LOG_LEVEL_INFO);
            AnalyzeServiceSpecialCase3 service = new AnalyzeServiceSpecialCase3();
            service.analyze(analysisList, analysisConfiguration);

            for (Analysis analysis : analysisList) {
                AnalysisManager.update(analysis);
            }
        } catch (Exception e) {
            Logger.putLog("Error al intentar analizar el análisis como caso especial 3", SpecialCasesUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void manageSpecialCase4(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) {
        try {
            Logger.putLog("solo hay enlaces de cambio de idioma en la Home (urls distintas)", SpecialCasesUtils.class, Logger.LOG_LEVEL_INFO);
            AnalyzeServiceSpecialCase4 service = new AnalyzeServiceSpecialCase4();
            service.analyze(analysisList, analysisConfiguration);

            for (Analysis analysis : analysisList) {
                AnalysisManager.update(analysis);
            }
        } catch (Exception e) {
            Logger.putLog("Error al intentar analizar el análisis como caso especial 4", SpecialCasesUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void manageSpecialCase5(List<Analysis> analysisList, AnalysisConfiguration analysisConfiguration) {
        try {
            Logger.putLog("solo hay enlaces de cambio de idioma en la Home (urls distintas)", SpecialCasesUtils.class, Logger.LOG_LEVEL_INFO);

            AnalyzeServiceSpecialCase5 service = new AnalyzeServiceSpecialCase5();
            service.analyze(analysisList, analysisConfiguration);

            for (Analysis analysis : analysisList) {
                AnalysisManager.update(analysis);
            }

        } catch (Exception e) {
            Logger.putLog("Error al intentar analizar el análisis como caso especial 4", SpecialCasesUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static String getCookie(LanguageFound switchLanguageHome) {
        if (switchLanguageHome.getLanguage().getName() != null) {
            Logger.putLog("Pidiendo la home en " + switchLanguageHome.getLanguage().getName().toLowerCase() + " para conseguir el cambio de idioma", AnalyzeServiceSpecialCase3.class, Logger.LOG_LEVEL_INFO);
        }
        StringBuilder result = new StringBuilder();
        try {
            HttpURLConnection connection = MultilanguageUtils.getConnection(new URL(switchLanguageHome.getHref()), null, null, false);
            connection.connect();

            Map<String, List<String>> headerFields = connection.getHeaderFields();
            List<String> cookies = headerFields.get("Set-Cookie");
            if (cookies != null) {
                for (String cookie : cookies) {
                    result.append(cookie).append(";");
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar la Cookie", SpecialCasesUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }

        return result.toString();
    }

    public static Analysis analyze(String url, Analysis analysis, String cookie, Language language) throws MalformedURLException, Exception {
        Logger.putLog("Pidiendo la URL " + url + " para hacer el reanálisis", SpecialCasesUtils.class, Logger.LOG_LEVEL_INFO);
        Document document = MultilanguageUtils.getDocument(null, new URL(url), cookie);
        if (document != null) {
            LanguageFound languageFound = new LanguageFound();
            languageFound.setHref(url);
            languageFound.setDeclarationLang(MultilanguageUtils.getDocumentLanguage(document));
            languageFound.setAnalysis(analysis);
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) document.getImplementation();
            LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
            lsSerializer.getDomConfig().setParameter("xml-declaration", false);
            try {
                languageFound.setContent(lsSerializer.writeToString(document));
            } catch (Exception e) {
                Logger.putLog("Error al guardar el contenido del documento. Caso 4.", SpecialCasesUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
            languageFound.setLanguage(language);
            languageFound.setLanguageSuspected(MultilanguageUtils.getSuspectedLanguagePage(document, languageFound.getLanguage().getId()));
            MultilanguageUtils.checkValencianoCatalanLanguage(languageFound);

            if (languageFound.getLanguageSuspected() != null) {
                Logger.putLog("Encontrado el lenguage " + languageFound.getLanguage().getName().toLowerCase() + " con lenguage declarado '" + languageFound.getDeclarationLang() + "' y texto en " + languageFound.getLanguageSuspected().getName().toLowerCase(), AnalyzeService.class, Logger.LOG_LEVEL_INFO);
            } else {
                Logger.putLog("Encontrado el lenguage " + languageFound.getLanguage().getName().toLowerCase() + " con lenguage declarado '" + languageFound.getDeclarationLang() + "'", AnalyzeService.class, Logger.LOG_LEVEL_INFO);
            }
            analysis.getLanguagesFound().add(languageFound);

        }
        return analysis;
    }
}
