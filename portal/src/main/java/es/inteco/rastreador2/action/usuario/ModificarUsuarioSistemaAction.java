package es.inteco.rastreador2.action.usuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm;
import es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm;
import es.inteco.rastreador2.actionform.usuario.ModificarUsuarioSistemaForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Arrays;

public class ModificarUsuarioSistemaAction extends Action {

    private Log log = LogFactory.getLog(ModificarUsuarioSistemaAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "edit.user")) {

                if (request.getAttribute(Constants.DE_PASS) == null && isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                } else {
                    String valem = request.getParameter(Constants.VALEMAIL);
                    ModificarUsuarioSistemaForm modificarUsuarioSistemaForm = (ModificarUsuarioSistemaForm) form;
                    loadInitialData(modificarUsuarioSistemaForm, request);
                    if (valem == null || valem.trim().equals("")) {
                        loadUserData(modificarUsuarioSistemaForm, request);
                        return mapping.findForward(Constants.VOLVER);
                    } else {
                        return editUser(mapping, modificarUsuarioSistemaForm, request);
                    }
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private ModificarUsuarioSistemaForm loadUserData(ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, HttpServletRequest request) throws Exception {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();

            //Comprobamos si estamos en inicio o se a pulsado al submit del formulario
            String id_usuario = request.getParameter(Constants.USER);
            modificarUsuarioSistemaForm.setIdUsuario(id_usuario);

            int userRolType = LoginDAO.getUserRolType(c, Long.valueOf(id_usuario));

            LoginDAO.getUserDatesToUpdate(c, modificarUsuarioSistemaForm, Long.valueOf(id_usuario), userRolType);

            return modificarUsuarioSistemaForm;
        } catch (Exception e) {
            log.error("Excepción genérica al crear un nuevo usuario");
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private ModificarUsuarioSistemaForm loadInitialData(ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, HttpServletRequest request) throws Exception {

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();

            modificarUsuarioSistemaForm.setCartuchosList(LoginDAO.getAllUserCartridge(c));
            String id_usuario = request.getParameter(Constants.USER);

            modificarUsuarioSistemaForm.setIdUsuario(id_usuario);
            int userRolType = LoginDAO.getUserRolType(c, Long.valueOf(id_usuario));

            modificarUsuarioSistemaForm.setRoles(LoginDAO.getAllUserRoles(c, userRolType));

            CargarCuentasUsuarioForm cargarCuentasUsuarioForm = new CargarCuentasUsuarioForm();
            modificarUsuarioSistemaForm.setCuenta_clienteV(CuentaUsuarioDAO.userList(c, cargarCuentasUsuarioForm, -1));

            CargarObservatorioForm cargarObservatorioForm = new CargarObservatorioForm();
            modificarUsuarioSistemaForm.setObservatorioV(ObservatorioDAO.userList(c, cargarObservatorioForm));

            return modificarUsuarioSistemaForm;

        } catch (Exception e) {
            log.error("Excepción genérica al crear un nuevo usuario");
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private ActionForward editUser(ActionMapping mapping, ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, HttpServletRequest request) throws Exception {
        Connection c = null;

        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();

            ActionErrors errors = modificarUsuarioSistemaForm.validate(mapping, request);

            if (errors != null && !errors.isEmpty()) {
                modificarUsuarioSistemaForm.setNombre_antiguo(request.getParameter(Constants.NOMBRE_ANTIGUO));
                if (modificarUsuarioSistemaForm.getSelectedCuentaCliente() != null && modificarUsuarioSistemaForm.getSelectedCuentaCliente().length != 0) {
                    modificarUsuarioSistemaForm.setCuentaCliente(Arrays.asList(modificarUsuarioSistemaForm.getSelectedCuentaCliente()));
                }
                if (modificarUsuarioSistemaForm.getSelectedObservatorio() != null && modificarUsuarioSistemaForm.getSelectedObservatorio().length != 0) {
                    modificarUsuarioSistemaForm.setObservatorio(Arrays.asList(modificarUsuarioSistemaForm.getSelectedObservatorio()));
                }
                loadInitialData(modificarUsuarioSistemaForm, request);
                saveErrors(request, errors);
                return (mapping.findForward(Constants.VOLVER));
            } else {
                if (errors == null) {
                    errors = new ActionErrors();
                }
                try {
                    //Comprobamos que el nombre usa caracteres correctos
                    ComprobadorCaracteres cc = new ComprobadorCaracteres(modificarUsuarioSistemaForm.getNombre());
                    boolean result = cc.isNombreValido();
                    if (!result) {
                        Logger.putLog("CARACTERES ILEGALES", ModificarUsuarioSistemaAction.class, Logger.LOG_LEVEL_INFO);
                        errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                        return mapping.findForward(Constants.VOLVER);
                    }
                    String nombre = cc.espacios();
                    modificarUsuarioSistemaForm.setNombre_antiguo(request.getParameter(Constants.NOMBRE_ANTIGUO));
                    if (!nombre.equals(modificarUsuarioSistemaForm.getNombre_antiguo()) && LoginDAO.existUser(c, nombre)) {
                        Logger.putLog("Usuario Duplicado", ModificarUsuarioSistemaAction.class, Logger.LOG_LEVEL_INFO);
                        errors.add("usuarioDuplicado", new ActionMessage("usuario.duplicado"));
                        return mapping.findForward(Constants.USUARIO_DUPLICADO);
                    }

                    LoginDAO.updateUser(c, modificarUsuarioSistemaForm);
                } catch (Exception e) {
                    Logger.putLog("Exception: ", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                }
            }
            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.usuario.editado");
            String volver = pmgr.getValue("returnPaths.properties", "volver.carga.usuarios");
            request.setAttribute("mensajeExito", mensaje);
            request.setAttribute("accionVolver", volver);
            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            log.error("Excepción genérica al crear un nuevo usuario");
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

}