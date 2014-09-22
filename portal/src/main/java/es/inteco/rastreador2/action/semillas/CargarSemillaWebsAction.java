package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.rastreador2.actionform.semillas.CargarSemillaWebsForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CargarSemillaWebsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            ActionErrors errors = new ActionErrors();
            if (CrawlerUtils.hasAccess(request, "web.list.seed")) {

                CargarSemillaWebsForm cargarSemillaWebsForm = (CargarSemillaWebsForm) form;
                cargarSemillaWebsForm.setArchivo(request.getParameter(Constants.ARCHIVO_D));

                String urlsString = "";
                List<String> urls = new ArrayList<String>();

                if (request.getParameter(Constants.URLS_STRING) != null) {
                    try {
                        URL url = new URL(request.getParameter(Constants.NUEVO_TERMINO));
                        URLConnection ur = url.openConnection();
                        ur.connect();
                    } catch (Exception e) {
                        errors.add("errorObligatorios", new ActionMessage("url.invalida"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    }

                    urlsString = request.getParameter(Constants.URLS_STRING) + ";" + request.getParameter(Constants.NUEVO_TERMINO);

                    StringTokenizer st = new StringTokenizer(urlsString, ";");
                    while (st.hasMoreTokens()) {
                        String t = st.nextToken();
                        urls.add(t);
                    }
                }

                cargarSemillaWebsForm.setUrls(urls);
                cargarSemillaWebsForm.setUrls_string(urlsString);

                return mapping.findForward(Constants.EXITO);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

}