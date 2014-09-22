package es.inteco.rastreador2.action.comun;

import es.inteco.common.Constants;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SalirSesionSistemaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession().invalidate();

            // Recargamos la lista de lenguajes
            request.getSession().setAttribute(Constants.LANGUAGE_LIST, CrawlerUtils.getLanguages());

            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

}