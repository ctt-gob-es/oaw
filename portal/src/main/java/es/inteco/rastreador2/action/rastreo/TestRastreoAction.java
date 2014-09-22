package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class TestRastreoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        try {
            if (CrawlerUtils.hasAccess(request, "test.crawler")) {
                String action = request.getParameter(Constants.ACTION);
                if (action != null) {
                    if (action.equals(Constants.LOAD_FORM)) {
                        return loadForm(mapping);
                    } else if (action.equals(Constants.LAUNCH_TEST)) {
                        return launchTest(mapping, form, request);
                    }
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        return null;
    }

    private ActionForward loadForm(ActionMapping mapping) {
        return mapping.findForward(Constants.RASTREO_TEST);
    }

    private ActionForward launchTest(ActionMapping mapping, ActionForm form, HttpServletRequest request) {
        if (!isCancelled(request)) {
            InsertarRastreoForm insertarRastreoForm = (InsertarRastreoForm) form;

            ActionErrors errors = insertarRastreoForm.validate(mapping, request);

            if (errors != null && !errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                ActionForward forward = mapping.getInputForward();
                forward.setRedirect(true);
                return forward;
            }

            CrawlerJob crawlerJob = new CrawlerJob();
            request.setAttribute(Constants.RASTREO_TEST_RESULTS, crawlerJob.testCrawler(createCrawlerData(insertarRastreoForm)));

            return mapping.findForward(Constants.RASTREO_TEST_RESULTS);
        } else {
            return mapping.findForward(Constants.CRAWLINGS_MENU);
        }
    }

    private CrawlerData createCrawlerData(InsertarRastreoForm insertarRastreoForm) {
        final CrawlerData crawlerData = new CrawlerData();
        crawlerData.setProfundidad(insertarRastreoForm.getProfundidad());
        crawlerData.setPseudoaleatorio(insertarRastreoForm.isPseudoAleatorio());
        crawlerData.setExhaustive(insertarRastreoForm.isExhaustive());
        crawlerData.setInDirectory(insertarRastreoForm.isInDirectory());
        crawlerData.setDomains(es.inteco.utils.CrawlerUtils.addDomainsToList(insertarRastreoForm.getSemilla().trim(), true, Constants.ID_LISTA_SEMILLA));
        crawlerData.setExceptions(es.inteco.utils.CrawlerUtils.addDomainsToList(insertarRastreoForm.getListaNoRastreable().trim(), false, Constants.ID_LISTA_NO_RASTREABLE));
        crawlerData.setCrawlingList(es.inteco.utils.CrawlerUtils.addDomainsToList(insertarRastreoForm.getListaRastreable().trim(), false, Constants.ID_LISTA_RASTREABLE));
        crawlerData.setTopN((int) insertarRastreoForm.getTopN());
        crawlerData.setNombreRastreo("test");
        crawlerData.setUser("test");
        List<String> urls = new ArrayList<String>();
        urls.add(insertarRastreoForm.getSemilla());
        crawlerData.setTest(true);
        crawlerData.setUrls(urls);

        return crawlerData;
    }

}