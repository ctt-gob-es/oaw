package es.inteco.rastreador2.action.usuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.cuentausuario.EliminarCuentaUsuarioAction;
import es.inteco.rastreador2.actionform.usuario.EliminarUsuarioSistemaForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class EliminarUsuarioSistemaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "delete.user")) {

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER));
                }

                EliminarUsuarioSistemaForm eliminarUsuarioSistemaForm = (EliminarUsuarioSistemaForm) form;

                //comprobamos de donde viene
                Connection c = null;

                if (request.getParameter(Constants.ID_USUARIO) == null) {
                    //no viene del submit de eliminar, hay que mostrar la pantalla de confiramcion de eliminado
                    String id_usuario = request.getParameter(Constants.USER);

                    try {
                        c = DataBaseManager.getConnection();
                        eliminarUsuarioSistemaForm = LoginDAO.getDeleteUser(c, Long.valueOf(id_usuario), eliminarUsuarioSistemaForm);
                    } catch (Exception e) {
                        Logger.putLog("Exception: ", EliminarCuentaUsuarioAction.class, Logger.LOG_LEVEL_ERROR, e);
                        throw new Exception(e);
                    } finally {
                        DataBaseManager.closeConnection(c);
                    }
                    return mapping.findForward(Constants.EXITO_ELIMINAR);

                } else {
                    Long idUser = Long.valueOf(request.getParameter(Constants.ID_USUARIO));
                    try {
                        c = DataBaseManager.getConnection();
                        LoginDAO.deleteUser(c, idUser);
                    } catch (Exception e) {
                        Logger.putLog("Exception: ", EliminarUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
                        throw new Exception(e);
                    } finally {
                        DataBaseManager.closeConnection(c);
                    }

                    PropertiesManager pmgr = new PropertiesManager();
                    String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.usuario.eliminado");
                    String volver = pmgr.getValue("returnPaths.properties", "volver.carga.usuarios");
                    request.setAttribute("mensajeExito", mensaje);
                    request.setAttribute("accionVolver", volver);
                    return mapping.findForward(Constants.EXITO);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }
}