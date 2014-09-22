package es.inteco.multilanguage.service;

import es.inteco.multilanguage.dao.LanguageDAO;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.persistence.Language;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import org.w3c.dom.Document;

import java.util.List;

public class AnalyzeService {

    public Analysis analyze(Analysis analysis, String cookie) throws Exception {
        List<Language> languages = LanguageDAO.getLanguages(true);
        Document document = MultilanguageUtils.getDocument(analysis.getContent());
        analysis.getLanguagesFound().add(MultilanguageUtils.getDocumentLanguage(document, analysis, languages));

        // Añadimos todos los lenguajes encontrados, comprobando si son correctos o no
        analysis.getLanguagesFound().addAll(MultilanguageUtils.getLanguageLinks(document, languages, analysis, cookie));
        analysis.setLanguagesFound(MultilanguageUtils.deleteDuplicateLanguages(analysis.getLanguagesFound()));

        return analysis;
    }
}
