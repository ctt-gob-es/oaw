package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Locale;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class GraficasRastreoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "view.observatory.results")) {
                return getGraphic(request, response);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private ActionForward getGraphic(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String graphic = request.getParameter(Constants.GRAPHIC);
        String executionId = request.getParameter(Constants.ID);
        String crawlerId = request.getParameter(Constants.ID_RASTREO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        if (graphic != null) {
            PropertiesManager pmgr = new PropertiesManager();
            String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files") + File.separator + crawlerId + File.separator + executionId + File.separator + language.getLanguage() + File.separator;
            String title = "";
            if (graphic.equals(Constants.CRAWLER_GRAPHIC_TOTAL_RESULTS)) {
                title = getResources(request).getMessage(getLocale(request), "chart.intav.total.results");
            } else if (graphic.equals(Constants.CRAWLER_GRAPHIC_GLOBAL_RESULTS)) {
                title = getResources(request).getMessage(getLocale(request), "chart.intav.priority.warnings");
            }
            CrawlerUtils.returnFile(response, path + title + ".jpg", "image/jpeg", false);
        }

        return null;
    }

}
