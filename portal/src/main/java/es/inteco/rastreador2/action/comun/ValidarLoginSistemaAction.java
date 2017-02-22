package es.inteco.rastreador2.action.comun;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.comun.ValidarLoginSistemaForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.login.Role;
import es.inteco.rastreador2.dao.login.Usuario;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.EncryptUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ValidarLoginSistemaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            ValidarLoginSistemaForm vlsForm = (ValidarLoginSistemaForm) form;

            ActionErrors errors = vlsForm.validate(mapping, request);
            if (errors.isEmpty()) {
                //Se crea una sesión, y se almacena Id Sesión, Login y Pass
                request.getSession().setAttribute(Constants.ID_SESION, request.getSession().getId());
                //request.getSession().setAttribute(Constants.ARCHIVO_SEMILLA_SERVER, "");
                request.getSession().setAttribute(Constants.USER, vlsForm.getLoginUser());
                request.getSession().setAttribute(Constants.PASS, vlsForm.getLoginPass());

                String password = vlsForm.getLoginPass();
                String encriptado = request.getParameter(Constants.ENCRIPTADO);
                if ("true".equalsIgnoreCase(encriptado)) {
                    password = EncryptUtils.decrypt(vlsForm.getLoginPass());
                }

                Connection c = null;
                try {
                    c = DataBaseManager.getConnection();

                    //Si existe ese Usuario y esa Contraseña
                    Usuario usuario = LoginDAO.getRegisteredUser(c, vlsForm.getLoginUser(), password);
                    if (usuario == null) {
                        errors.add("errorLogin", new ActionMessage("login.fallido"));
                        saveErrors(request.getSession(), errors);
                        ActionForward forward = new ActionForward(mapping.findForward(Constants.NO_EXITO));
                        forward.setRedirect(true);
                        return forward;
                    } else {
                        usuario.setTipos(getRoleIdList(LoginDAO.getUserRoles(c, usuario.getId())));
                        request.getSession().setAttribute(Constants.ROLE, usuario.getTipos());
                    }

                } catch (SQLException e) {
                    Logger.putLog("EXCEPCIÓN 1 accediendo a la BD: ", ValidarLoginSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
                    return mapping.findForward(Constants.NO_EXITO);
                } finally {
                    DataBaseManager.closeConnection(c);
                }

                ActionForward forward = new ActionForward();
                if (request.getSession().getAttribute(Constants.URL) != null) {
                    String path = (String) request.getSession().getAttribute(Constants.URL);
                    path = removeAplicationName(path);
                    forward.setPath(path);
                } else {
                    forward = new ActionForward(mapping.findForward("observatoryMenu"));
                }
                forward.setRedirect(true);
                request.getSession().removeAttribute(Constants.URL);
                return forward;
            } else {
                saveErrors(request, errors);
                return mapping.findForward(Constants.VOLVER);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private String removeAplicationName(String path) {
        return path.substring((Constants.APLICATION_NAME.length()));
    }

    private List<String> getRoleIdList(List<Role> roleList) {
        List<String> roleIdList = new ArrayList<>();
        if (roleList != null) {
            for (Role role : roleList) {
                String roleId = role.getId().toString();
                roleIdList.add(roleId);
            }
        }
        return roleIdList;
    }

}