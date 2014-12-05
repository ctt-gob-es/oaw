package es.inteco.rastreador2.multilanguage.action;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.multilanguage.form.AnalysisForm;
import es.inteco.multilanguage.form.LanguageForm;
import es.inteco.multilanguage.form.LanguageFoundForm;
import es.inteco.multilanguage.manager.AnalysisManager;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.semillas.SeedCategoriesAction;
import es.inteco.rastreador2.intav.action.AnalysisFromCrawlerAction;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class MultilanguageAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (CrawlerUtils.hasAccess(request, "show.crawler.results")) {
                String action = request.getParameter(Constants.ACTION);
                if (action.equals(Constants.LIST_ANALYSIS)) {
                    return getAnalysisList(mapping, request);
                } else if (action.equals(Constants.GET_DETAIL)) {
                    return getAnalysis(mapping, request);
                } else if (action.equals(Constants.ACTION_GET_HTML_SOURCE)) {
                    return getHtmlSource(request, response);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, SeedCategoriesAction.class);
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        return null;
    }

    /**
     * Devuelve el listado de análisis paginado
     *
     * @param mapping
     * @param request
     * @return
     * @throws Exception
     */
    private ActionForward getAnalysisList(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Long idExecution = Long.valueOf(request.getParameter(Constants.ID));

        PropertiesManager pmgr = new PropertiesManager();
        int numResults = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
        int page = Pagination.getPage(request, Constants.PAG_PARAM);

        List<AnalysisForm> analysisList = AnalysisManager.getAnalysisByExecution(idExecution, numResults * (page - 1), numResults);
        for (AnalysisForm analyse : analysisList) {
            if ((analyse.getUrl() != null) && analyse.getUrl().length() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string.multilang"))) {
                analyse.setUrlTitle(analyse.getUrl());
                analyse.setUrl(analyse.getUrl().substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string.multilang"))) + "...");
            }
        }
        request.setAttribute(Constants.LIST_ANALYSIS, analysisList);
        request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, AnalysisManager.countAnalysisByExecution(idExecution), page));

        return mapping.findForward(Constants.LIST_ANALYSIS_BY_TRACKING);
    }

    /**
     * Devuelve un análisis en concreto
     *
     * @param mapping
     * @param request
     * @return
     * @throws Exception
     */
    private ActionForward getAnalysis(ActionMapping mapping, HttpServletRequest request) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();
        Long idAnalysis = Long.valueOf(request.getParameter(Constants.CODE));

        AnalysisForm analysisForm = AnalysisManager.getAnalysis(idAnalysis);
        for (LanguageFoundForm languageFoundForm : analysisForm.getLanguagesFound()) {
            if ((languageFoundForm.getHref() != null) && languageFoundForm.getHref().length() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string.multilang"))) {
                languageFoundForm.setHrefTitle(languageFoundForm.getHref());
                languageFoundForm.setHref(languageFoundForm.getHref().substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string.multilang"))) + "...");
            }
        }
        request.setAttribute(Constants.ANALYSIS, analysisForm);
        request.setAttribute(Constants.MULTILANGUAJE_NOT_FOUND_LANG, MultilanguageUtils.orderLanguagesForm(notFoundLanguages(analysisForm.getLanguagesFound())));

        return mapping.findForward(Constants.ANALYSIS_DETAIL);
    }

    /**
     * Devuelve la lista de lenguajes que no encontrados en la página
     *
     * @param foundLanguages
     * @return
     * @throws Exception
     */

    private static List<LanguageForm> notFoundLanguages(List<LanguageFoundForm> foundLanguages) throws Exception {
        List<LanguageForm> notFoundLanguagesList = new ArrayList<LanguageForm>();
        List<LanguageForm> foundLanguagesList = new ArrayList<LanguageForm>();

        for (LanguageFoundForm language : foundLanguages) {
            foundLanguagesList.add(language.getLanguage());
        }

        List<LanguageForm> languages = AnalysisManager.getLanguages(true);
        for (LanguageForm languageForm : languages) {
            if (!foundLanguagesList.contains(languageForm)) {
                notFoundLanguagesList.add(languageForm);
            }
        }

        return notFoundLanguagesList;
    }

    private ActionForward getHtmlSource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection conn = null;
        try {
            if (request.getParameter(Constants.CODE) != null) {
                long id = Long.parseLong(request.getParameter(Constants.CODE));

                conn = DataBaseManager.getConnection();
                AnalysisForm analysis = AnalysisManager.getAnalysis(id);

                LanguageFoundForm languageForm = new LanguageFoundForm();
                for (LanguageFoundForm language : analysis.getLanguagesFound()) {
                    if (language.getId().equals(request.getParameter(Constants.ID_LANG))) {
                        languageForm = language;
                    }
                }

                if (languageForm.getContent() != null) {
                    CrawlerUtils.returnText(new String(languageForm.getContent().getBytes("UTF-8")), response, false);
                }
            }

        } catch (Exception e) {
            Logger.putLog("Excepcion: ", AnalysisFromCrawlerAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(conn);
        }
        return null;
    }

}
