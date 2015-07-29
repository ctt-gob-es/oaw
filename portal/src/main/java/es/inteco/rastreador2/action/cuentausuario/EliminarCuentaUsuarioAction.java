package es.inteco.rastreador2.action.cuentausuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.usuario.EliminarUsuarioSistemaAction;
import es.inteco.rastreador2.actionform.cuentausuario.EliminarCuentaUsuarioForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.servlets.ScheduleClientAccountsServlet;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class EliminarCuentaUsuarioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        try {

            if (CrawlerUtils.hasAccess(request, "delete.client.account")) {

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER));
                }

                EliminarCuentaUsuarioForm eliminarCuentaUsuarioForm = (EliminarCuentaUsuarioForm) form;
                //comprobamos de donde viene
                Connection c = null;
                Connection con = null;
                if (request.getParameter(Constants.CUENTA_ELIMINAR) == null) {
                    //no viene del submit de eliminar, hay que mostrar la pantalla de confiramcion de eliminado
                    String id_cuenta = request.getParameter(Constants.ID_CUENTA);

                    try {
                        c = DataBaseManager.getConnection();
                        con = DataBaseManager.getConnection();
                        eliminarCuentaUsuarioForm = CuentaUsuarioDAO.getDeleteUserAccounts(c, Long.valueOf(id_cuenta), eliminarCuentaUsuarioForm);
                        eliminarCuentaUsuarioForm.setNormaAnalisisSt(RastreoDAO.getNombreNorma(con, Long.parseLong(eliminarCuentaUsuarioForm.getNormaAnalisis())));
                    } catch (Exception e) {
                        Logger.putLog("Exception: ", EliminarUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
                        throw new Exception(e);
                    } finally {
                        DataBaseManager.closeConnection(c);
                        DataBaseManager.closeConnection(con);
                    }
                    return mapping.findForward(Constants.EXITO_ELIMINAR);

                } else {
                    Long id_cuenta = Long.valueOf(request.getParameter(Constants.ID_CUENTA));
                    try {
                        c = DataBaseManager.getConnection();

                        // Eliminamos los rastreos programados
                        ScheduleClientAccountsServlet.deleteJobs(id_cuenta, Constants.CLIENT_ACCOUNT_TYPE);

                        // Eliminamos los datos de la base de datos
                        CuentaUsuarioDAO.deleteUserAccount(c, id_cuenta, eliminarCuentaUsuarioForm);
                    } catch (Exception e) {
                        Logger.putLog("Exception: ", EliminarCuentaUsuarioAction.class, Logger.LOG_LEVEL_ERROR, e);
                        throw new Exception(e);
                    } finally {
                        DataBaseManager.closeConnection(c);
                    }

                    PropertiesManager pmgr = new PropertiesManager();
                    String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.cuenta.usuario.eliminado");
                    String volver = pmgr.getValue("returnPaths.properties", "volver.carga.cuentas.cliente");
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