package es.inteco.multilanguage.manager;

import es.inteco.multilanguage.dao.AnalysisDAO;
import es.inteco.multilanguage.dao.LanguageDAO;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.form.LanguageForm;
import es.inteco.multilanguage.form.LanguageFoundForm;
import es.inteco.multilanguage.persistence.Analysis;
import es.inteco.multilanguage.persistence.Language;
import es.inteco.multilanguage.persistence.LanguageFound;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisManager extends BaseManager {

    /**
     * Devolver lista de análisis de una ejecución
     *
     * @param idCrawling
     * @param offset
     * @param numResults
     * @return
     */
    public static List<AnalysisForm> getAnalysisByExecution(Long idCrawling, int offset, int numResults) throws Exception {
        Session session = getSession();

        List<Analysis> analysisList = AnalysisDAO.getAnalysisByExecution(session, idCrawling, offset, numResults);
        List<AnalysisForm> analysisFormList = new ArrayList<AnalysisForm>();

        for (Analysis analysis : analysisList) {
            analysisFormList.add(MultilanguageUtils.getAnalysisForm(analysis));
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return analysisFormList;
    }

    /**
     * Contar lista de análisis
     *
     * @param idCrawling
     * @return
     */
    public static int countAnalysisByExecution(Long idCrawling) throws Exception {
        Session session = getSession();

        int numAnalysis = AnalysisDAO.countAnalysisByExecution(session, idCrawling);

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return numAnalysis;
    }

    /**
     * Devolver un análisis en concreto
     *
     * @param idCrawling
     * @param offset
     * @param numResults
     * @return
     */
    public static AnalysisForm getAnalysis(Long idAnalysis) throws Exception {
        Session session = getSession();

        Analysis analysis = AnalysisDAO.getAnalysis(session, idAnalysis);
        AnalysisForm analysisForm = MultilanguageUtils.getAnalysisForm(analysis);

        for (LanguageFoundForm languageFoundForm : analysisForm.getLanguagesFound()) {
            languageFoundForm = MultilanguageUtils.isCorrectLanguageFound(languageFoundForm);
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return analysisForm;
    }

    /**
     * Devolver un listado de análisis relacionados con un rastreo
     *
     * @param idCrawling
     * @param offset
     * @param numResults
     * @return
     */
    public static Map<Long, List<AnalysisForm>> getObservatoryAnalysis(Connection connection, List<Long> listCrawlings) throws Exception {
        Session session = getSession();

        List<Analysis> analysisList = AnalysisDAO.getObservatoryAnalysis(session, listCrawlings);
        Map<Long, List<AnalysisForm>> analysisFormMap = new HashMap<Long, List<AnalysisForm>>();

        for (Analysis analysis : analysisList) {
            List<AnalysisForm> analysisPages = new ArrayList<AnalysisForm>();
            if (analysisFormMap.get(analysis.getIdCrawling()) != null) {
                analysisPages = analysisFormMap.get(analysis.getIdCrawling());
                analysisPages.add(MultilanguageUtils.getAnalysisForm(analysis));
            } else {
                analysisPages.add(MultilanguageUtils.getAnalysisForm(analysis));
            }
            analysisFormMap.put(analysis.getIdCrawling(), analysisPages);
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return analysisFormMap;
    }

    public static List<AnalysisForm> getObservatoryAnalysisList(Long idCrawling) throws Exception {
        Session session = getSession();
        List<Long> idCrawlingList = new ArrayList<Long>();
        idCrawlingList.add(idCrawling);
        List<Analysis> analysisList = AnalysisDAO.getObservatoryAnalysis(session, idCrawlingList);
        List<AnalysisForm> analysisFormList = new ArrayList<AnalysisForm>();

        for (Analysis analysis : analysisList) {
            analysisFormList.add(MultilanguageUtils.getAnalysisForm(analysis));
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return analysisFormList;
    }


    public static List<LanguageForm> getLanguages(boolean onlyToAnalyze) throws Exception {
        Session session = getSession();

        List<LanguageForm> languageFormList = new ArrayList<LanguageForm>();
        List<Language> languageList = LanguageDAO.getLanguages(onlyToAnalyze);

        for (Language language : languageList) {
            LanguageForm languageForm = new LanguageForm();
            BeanUtils.copyProperties(languageForm, language);
            languageFormList.add(languageForm);
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return MultilanguageUtils.orderLanguagesForm(languageFormList);
    }

    public static LanguageForm getLanguage(long id) throws Exception {
        Session session = getSession();

        LanguageForm languageForm = new LanguageForm();
        Language language = LanguageDAO.getLanguage(id);

        BeanUtils.copyProperties(languageForm, language);

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return languageForm;
    }

    /**
     * Devolver lista de análisis de una ejecución sin pasarlos a Form
     *
     * @param idCrawling
     * @param offset
     * @param numResults
     * @return
     */
    public static List<Analysis> getAnalysisByExecution(Long idCrawling) throws Exception {
        Session session = getSession();

        List<Analysis> analysisList = AnalysisDAO.getAnalysisByExecution(session, idCrawling);

        for (Analysis analysis : analysisList) {
            for (LanguageFound languageFound : MultilanguageUtils.sortLanguagesFound(analysis.getLanguagesFound())) {
                // No hacemos nada, simplemente recorre la lista para recuperar la información como si fuera Fetch = EAGER
            }
        }

        session.flush();

        if (session.isOpen()) {
            session.close();
        }

        return analysisList;
    }

    public static List<AnalysisForm> getAnalysisFormByExecution(Long idCrawling) throws Exception {
        List<Analysis> analysisList = getAnalysisByExecution(idCrawling);
        List<AnalysisForm> analysisFormList = new ArrayList<AnalysisForm>();
        for (Analysis analysis : analysisList) {
            analysisFormList.add(MultilanguageUtils.getAnalysisForm(analysis));
        }
        return analysisFormList;
    }

}
