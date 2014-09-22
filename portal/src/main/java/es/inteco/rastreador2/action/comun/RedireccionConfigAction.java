package es.inteco.rastreador2.action.comun;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.comun.RedireccionConfigForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class RedireccionConfigAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "all.roles")) {
                RedireccionConfigForm redireccionConfigForm = (RedireccionConfigForm) form;

                HttpSession sesion = request.getSession();
                String user = (String) sesion.getAttribute(Constants.USER);
                redireccionConfigForm.setUser(user);
                Connection c = null;
                try {
                    c = DataBaseManager.getConnection();
                    redireccionConfigForm = LoginDAO.loadUserData(c, user, redireccionConfigForm);
                    redireccionConfigForm.setProyecto(CartuchoDAO.getApplication(c, redireccionConfigForm.getId_cartucho()));
                } catch (Exception e) {
                    Logger.putLog("Exception: ", RedireccionConfigAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                } finally {
                    DataBaseManager.closeConnection(c);
                }
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
