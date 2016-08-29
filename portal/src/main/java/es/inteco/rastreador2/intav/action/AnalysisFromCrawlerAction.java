package es.inteco.rastreador2.intav.action;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import com.opensymphony.oscache.base.NeedsRefreshException;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.*;
import es.inteco.intav.persistence.Analysis;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.intav.utils.IntavUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Locale;

public class AnalysisFromCrawlerAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        if (CrawlerUtils.hasAccess(request, "show.crawler.results")) {
            if (request.getParameter(Constants.ACTION) != null) {
                if (request.getParameter(Constants.ACTION).equals(Constants.ACTION_GET_EVALUATION)) {
                    return resultsToShow(mapping, request);
                } else if (request.getParameter(Constants.ACTION).equals(Constants.ACTION_GET_DETAIL)) {
                    return getDetail(mapping, request);
                } else if (request.getParameter(Constants.ACTION).equals(Constants.ACTION_RECOVER_EVALUATION)) {
                    return recoverEvaluation(mapping, request);
                } else if (request.getParameter(Constants.ACTION).equals(Constants.ACTION_GET_HTML_SOURCE)) {
                    return getHtmlSource(request, response);
                }
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
        return null;
    }

    private void getResultData(final HttpServletRequest request) throws Exception {
        try (final Connection c = DataBaseManager.getConnection()) {
            // Inicializamos el evaluador
            if (!EvaluatorUtility.isInitialized()) {
                EvaluatorUtility.initialize();
            }
            if (request.getParameter(Constants.OBSERVATORY_ID) != null) {
                request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.OBSERVATORY_ID));
            }

            if (request.getParameter(Constants.CODE) != null) {
                long id = Long.parseLong(request.getParameter(Constants.CODE));

                Evaluator evaluator = new Evaluator();
                Evaluation evaluation = evaluator.getAnalisisDB(c, id, EvaluatorUtils.getDocList(), false);

                Locale locale = (Locale) request.getSession().getAttribute("org.apache.struts.action.LOCALE");
                MessageResources messageResources = getResources(request);

                if (request.getParameter(Constants.OBSERVATORY) != null) {
                    long idExObs = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
                    String methodology = ObservatorioDAO.getMethodology(c, idExObs);
                    ObservatoryEvaluationForm evaluationForm = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, methodology, false);
                    evaluationForm.setSource(BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), true));
                    setDescription(evaluationForm, messageResources, locale);
                    request.setAttribute(Constants.FAILED_CHECKS, IntavUtils.getFailedChecks(request, evaluationForm, EvaluatorUtility.loadGuideline(evaluation.getGuidelines().get(0))));
                    request.setAttribute(Constants.EVALUATION_FORM, evaluationForm);
                } else {
                    EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, EvaluatorUtility.getLanguage(request));
                    evaluationForm.setSource(BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), true));
                    setDescription(evaluationForm, messageResources, locale);
                    request.setAttribute(Constants.EVALUATION_FORM, evaluationForm);
                    CacheUtils.putInCache(evaluationForm, request.getSession().getId());
                }
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", AnalysisFromCrawlerAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private ActionForward resultsToShow(ActionMapping mapping, HttpServletRequest request) {
        try {
            request.setAttribute(Constants.CODE, request.getParameter(Constants.CODE));
            getResultData(request);
            if (request.getParameter(Constants.OBSERVATORY) != null) {
                return mapping.findForward(Constants.CHECK_OBSERVATORY_RESULTS);
            } else {
                return mapping.findForward(Constants.CHECK_RESULTS);
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion: ", AnalysisFromCrawlerAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        }
    }

    private void setDescription(EvaluationForm evaluationForm, MessageResources messageResources, Locale locale) {
        for (PriorityForm priority : evaluationForm.getPriorities()) {
            for (GuidelineForm guideline : priority.getGuidelines()) {
                guideline.setDescription(messageResources.getMessage(locale, guideline.getDescription()));
                if (guideline.getDescription().indexOf('.') != -1) {
                    guideline.setGuidelineId(guideline.getDescription().substring(0, guideline.getDescription().indexOf('.')));
                    guideline.setDescription(guideline.getDescription().substring(guideline.getDescription().indexOf('.')));
                }
                for (PautaForm pauta : guideline.getPautas()) {
                    pauta.setName(messageResources.getMessage(locale, pauta.getName()));
                    if (pauta.getName().indexOf(' ') != -1) {
                        pauta.setPautaId(pauta.getName().substring(0, pauta.getName().indexOf(' ')));
                        pauta.setName(pauta.getName().substring(pauta.getName().indexOf(' ')));
                    }
                }
            }
        }
    }

    private void setDescription(ObservatoryEvaluationForm evaluationForm, MessageResources messageResources, Locale locale) {
        for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
            for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
                for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
                    observatorySubgroupForm.setDescription(messageResources.getMessage(locale, observatorySubgroupForm.getDescription()));
                    if (observatorySubgroupForm.getDescription().indexOf(' ') != -1) {
                        observatorySubgroupForm.setGuidelineId(observatorySubgroupForm.getDescription().substring(0, observatorySubgroupForm.getDescription().indexOf(' ')));
                        observatorySubgroupForm.setDescription(observatorySubgroupForm.getDescription().substring(observatorySubgroupForm.getDescription().indexOf(' ')));
                    }
                }
            }
        }
    }

    public ActionForward getDetail(ActionMapping mapping, HttpServletRequest request) throws Exception {

        String idCheck = request.getParameter(Constants.ID_CHECK);
        request.setAttribute(Constants.ID_RASTREO, request.getParameter(Constants.ID_RASTREO));
        request.setAttribute(Constants.ID, request.getParameter(Constants.ID));
        request.setAttribute(Constants.CODE, request.getParameter(Constants.CODE));

        EvaluationForm evaluation;
        try {
            evaluation = (EvaluationForm) CacheUtils.getFromCache(request.getSession().getId());
        } catch (NeedsRefreshException e) {
            if (request.getParameter(Constants.CODE) != null && request.getParameter(Constants.ID_RASTREO) != null) {
                getResultData(request);
                evaluation = (EvaluationForm) CacheUtils.getFromCache(request.getSession().getId());
            } else {
                ActionErrors errors = new ActionErrors();
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.session.expired"));
                return mapping.findForward(Constants.INDEX_ADMIN);
            }

        }

        int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
        if (request.getParameter(Constants.PAG_PARAM) != null) {
            PropertiesManager pmgr = new PropertiesManager();
            int pagination = Integer.parseInt(pmgr.getValue("intav.properties", "pagination.check.results"));
            request.setAttribute(Constants.RESULTS_PAGINATION_INITIAL_VALUE, String.valueOf(pagination * (pagina - 1)));
        }

        for (PriorityForm priority : evaluation.getPriorities()) {
            for (GuidelineForm guideline : priority.getGuidelines()) {
                for (PautaForm pautaForm : guideline.getPautas()) {
                    for (ProblemForm problem : pautaForm.getProblems()) {
                        if (problem.getCheck().equals(idCheck)) {
                            pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                            request.setAttribute(Constants.PROBLEMA, problem);
                            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, problem.getSpecificProblems().size(), pagina));
                            break;
                        }
                    }
                }
            }
        }

        return mapping.findForward(Constants.CHECK_RESULTS_DETAIL);
    }

    private ActionForward getEvaluationCall(ActionMapping mapping, HttpServletRequest request) throws Exception {

        if (request.getParameter(Constants.CODE) != null && request.getParameter(Constants.ID_RASTREO) != null) {
            return resultsToShow(mapping, request);
        } else {
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.session.expired"));
            return mapping.findForward(Constants.INDEX_ADMIN);
        }
    }

    private ActionForward recoverEvaluation(ActionMapping mapping, HttpServletRequest request) throws Exception {
        request.setAttribute(Constants.CODE, request.getParameter(Constants.CODE));

        try {
            EvaluationForm evaluation = (EvaluationForm) CacheUtils.getFromCache(request.getSession().getId());
            request.setAttribute(Constants.EVALUATION_FORM, evaluation);
        } catch (NeedsRefreshException e) {
            return getEvaluationCall(mapping, request);
        }
        return mapping.findForward(Constants.CHECK_RESULTS);
    }

    private ActionForward getHtmlSource(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Connection conn = null;
        try {
            if (request.getParameter(Constants.CODE) != null) {
                long id = Long.parseLong(request.getParameter(Constants.CODE));

                conn = DataBaseManager.getConnection();
                Analysis analysis = AnalisisDatos.getAnalisisFromId(conn, id);

                CrawlerUtils.returnText(response, new String(analysis.getSource().getBytes(CrawlerUtils.getCharset(analysis.getSource()))), true);
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
