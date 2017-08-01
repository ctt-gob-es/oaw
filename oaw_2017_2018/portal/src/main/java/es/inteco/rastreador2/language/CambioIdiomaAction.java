package es.inteco.rastreador2.language;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class CambioIdiomaAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {

        String language = "language." + request.getParameter(Constants.LOCALE);

        PropertiesManager pmgr = new PropertiesManager();
        String firstLocaleParam = pmgr.getValue("language.properties", language);
        if (firstLocaleParam != null) {
            Locale newLocale = new Locale(firstLocaleParam);
            setLocale(request, newLocale);
        }

        if (request.getParameter(Constants.LOGIN_PARAM) != null) {
            return mapping.findForward(Constants.LOGIN);
        } else {
            return mapping.findForward(Constants.INDEX_ADMIN);
        }
    }


}
