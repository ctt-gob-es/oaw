package es.inteco.multilanguage.dao;

import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.persistence.Language;
import es.inteco.multilanguage.persistence.LanguageFound;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AnalysisDAO {

    /**
     * Devolver lista de análisis de una ejecución
     *
     * @param session
     * @param idCrawling
     * @param offset
     * @param numResults
     * @return
     */
    public static List<Analysis> getAnalysisByExecution(Session session, Long idCrawling, int offset, int numResults) {
        Criteria criteria = session.createCriteria(Analysis.class);
        criteria.add(Restrictions.eq("idCrawling", idCrawling));
        criteria.setFirstResult(offset);
        criteria.setMaxResults(numResults);

        List<Analysis> analysis = criteria.list();

        return analysis;
    }

    /**
     * Devolver lista de análisis de una ejecución
     *
     * @param session
     * @param idCrawling
     * @return
     */
    public static List<Analysis> getAnalysisByExecution(Session session, Long idCrawling) {
        Criteria criteria = session.createCriteria(Analysis.class);
        criteria.add(Restrictions.eq("idCrawling", idCrawling));
        criteria.addOrder(Order.asc("id"));

        List<Analysis> analysis = criteria.list();

        return analysis;
    }

    /**
     * Contar lista de análisis
     *
     * @param idCrawling
     * @return
     */
    public static int countAnalysisByExecution(Session session, Long idCrawling) {
        Criteria criteria = session.createCriteria(Analysis.class);
        criteria.add(Restrictions.eq("idCrawling", idCrawling));

        List<Analysis> analysis = criteria.list();

        return analysis.size();
    }

    /**
     * Devolver un análisis en concreto
     *
     * @param session
     * @param idAnalysis
     * @return
     */
    public static Analysis getAnalysis(Session session, Long idAnalysis) {
        Criteria criteria = session.createCriteria(Analysis.class);
        criteria.add(Restrictions.eq("id", idAnalysis));

        List<Analysis> analysisList = criteria.list();

        if (analysisList != null && !analysisList.isEmpty()) {
            return analysisList.get(0);
        } else {
            return null;
        }
    }

    /**
     * Devolver un listado de análisis relacionados con un rastreo
     *
     * @param session
     * @param listCrawlings
     * @return
     */
    public static List<Analysis> getObservatoryAnalysis(Session session, List<Long> listCrawlings) {
        Criteria criteria = session.createCriteria(Analysis.class);

        criteria.createAlias("languagesFound", "languagesFound", CriteriaSpecification.LEFT_JOIN);
        criteria.createAlias("languagesFound.language", "language", CriteriaSpecification.LEFT_JOIN);
        criteria.createAlias("languagesFound.languageSuspected", "languageSuspected", CriteriaSpecification.LEFT_JOIN);

        ProjectionList properties = Projections.projectionList();
        properties.add(Projections.property("id"));
        properties.add(Projections.property("idCrawling"));
        properties.add(Projections.property("date"));
        properties.add(Projections.property("url"));

        properties.add(Projections.property("languagesFound.href"));
        properties.add(Projections.property("languagesFound.declarationLang"));

        properties.add(Projections.property("language.id"));
        properties.add(Projections.property("language.name"));
        properties.add(Projections.property("language.text"));
        properties.add(Projections.property("language.code"));

        properties.add(Projections.property("languageSuspected.id"));
        properties.add(Projections.property("languageSuspected.name"));
        properties.add(Projections.property("languageSuspected.text"));
        properties.add(Projections.property("languageSuspected.code"));

        criteria.setProjection(properties);

        criteria.add(Restrictions.in("idCrawling", listCrawlings));
        criteria.addOrder(Order.asc("id"));

        List<Object[]> queryResults = criteria.list();

        List<Analysis> results = new ArrayList<>();
        Analysis analysis = null;
        Long lastId = (long) 0;
        for (Object[] data : queryResults) {
            int i = 0;

            if (!lastId.equals((Long) data[i])) {
                if (!lastId.equals((long) 0)) {
                    results.add(analysis);
                }
                lastId = (Long) data[i];
                analysis = new Analysis();
            }
            if (analysis == null) {
                analysis = new Analysis();
            }

            analysis.setId((Long) data[i++]);
            analysis.setIdCrawling((Long) data[i++]);
            analysis.setDate((Timestamp) data[i++]);
            analysis.setUrl((String) data[i++]);

            if (data[i] != null) {
                LanguageFound languageFound = new LanguageFound();
                languageFound.setHref((String) data[i++]);
                languageFound.setDeclarationLang((String) data[i++]);

                Language language = new Language();
                language.setId((Long) data[i++]);
                language.setName((String) data[i++]);
                language.setText((String) data[i++]);
                language.setCode((String) data[i++]);
                languageFound.setLanguage(language);

                Language languageSuspected = new Language();
                languageSuspected.setId((Long) data[i++]);
                languageSuspected.setName((String) data[i++]);
                languageSuspected.setText((String) data[i++]);
                languageSuspected.setCode((String) data[i]);
                languageFound.setLanguageSuspected(languageSuspected);

                analysis.getLanguagesFound().add(languageFound);
            }
        }

        // Anadimos tambien el ultimo
        if (analysis != null) {
            results.add(analysis);
        }
        return results;
    }
}
