package es.inteco.rastreador2.action.cuentausuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class CargarCuentasUsuarioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // Marcamos el menú
        request.getSession().setAttribute(Constants.MENU, Constants.MENU_CLIENT);

        try {
            if (CrawlerUtils.hasAccess(request, "load.client.accounts")) {

                CargarCuentasUsuarioForm cargarCuentasUsuarioForm = (CargarCuentasUsuarioForm) form;
                String user = (String) request.getSession().getAttribute(Constants.USER);
                String pass = (String) request.getSession().getAttribute(Constants.PASS);
                cargarCuentasUsuarioForm.setUser(user);
                cargarCuentasUsuarioForm.setPass(pass);

                Connection c = null;
                try {
                    c = DataBaseManager.getConnection();
                    int numResult = CuentaUsuarioDAO.countAccounts(c);
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                    cargarCuentasUsuarioForm = CuentaUsuarioDAO.userList(c, cargarCuentasUsuarioForm, (pagina - 1));
                    request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
                } catch (Exception e) {
                    Logger.putLog("Exception: ", CargarCuentasUsuarioAction.class, Logger.LOG_LEVEL_ERROR, e);
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