package es.inteco.rastreador2.action.usuario;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm;
import es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm;
import es.inteco.rastreador2.actionform.usuario.NuevoUsuarioSistemaForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class NuevoUsuarioSistemaAction extends Action {

    private Log log = LogFactory.getLog(NuevoUsuarioSistemaAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "new.user")) {
                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                } else {
                    if (request.getParameter(Constants.ACCION_RELOAD) == null || !request.getParameter(Constants.ACCION_RELOAD).equals(Constants.TRUE)) {
                        request.getSession().removeAttribute(Constants.NUEVO_USUARIO_SISTEMA_FORM);
                    }
                    NuevoUsuarioSistemaForm nuevoUsuarioSistemaForm = (NuevoUsuarioSistemaForm) form;
                    nuevoUsuarioSistemaForm = loadInitialData(nuevoUsuarioSistemaForm, request);

                    //Comprobamos si estamos en inicio o se a pulsado al submit del formulario
                    String esPrimera = request.getParameter(Constants.ES_PRIMERA);
                    if (esPrimera == null || esPrimera.trim().equals("")) {
                        return mapping.findForward(Constants.VOLVER);
                    } else {
                        return newSystemUser(mapping, nuevoUsuarioSistemaForm, request);
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

    private NuevoUsuarioSistemaForm loadInitialData(NuevoUsuarioSistemaForm nuevoUsuarioSistemaForm, HttpServletRequest request) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();

        try (Connection c = DataBaseManager.getConnection()) {

            String userRoleType = request.getParameter(Constants.ROLE_TYPE);

            if (userRoleType.equals(pmgr.getValue(CRAWLER_PROPERTIES, "role.type.client"))) {
                nuevoUsuarioSistemaForm.setRoles(LoginDAO.getAllUserRoles(c, Integer.parseInt(userRoleType)));
                CargarCuentasUsuarioForm cargarCuentasUsuarioForm = new CargarCuentasUsuarioForm();
                nuevoUsuarioSistemaForm.setCuenta_clienteV(CuentaUsuarioDAO.userList(c, cargarCuentasUsuarioForm, -1));
            } else if (userRoleType.equals(pmgr.getValue(CRAWLER_PROPERTIES, "role.type.observatory"))) {
                CargarObservatorioForm cargarObservatorioForm = new CargarObservatorioForm();
                nuevoUsuarioSistemaForm.setObservatorioV(ObservatorioDAO.userList(c, cargarObservatorioForm));
            } else {
                nuevoUsuarioSistemaForm.setRoles(LoginDAO.getAllUserRoles(c, Integer.parseInt(userRoleType)));
                nuevoUsuarioSistemaForm.setCartuchosList(LoginDAO.getAllUserCartridge(c));
                CargarCuentasUsuarioForm cargarCuentasUsuarioForm = new CargarCuentasUsuarioForm();
                nuevoUsuarioSistemaForm.setCuenta_clienteV(CuentaUsuarioDAO.userList(c, cargarCuentasUsuarioForm, -1));
            }

            return nuevoUsuarioSistemaForm;
        } catch (Exception e) {
            log.error("Excepción genérica al crear un nuevo usuario");
            throw e;
        }
    }

    private ActionForward newSystemUser(ActionMapping mapping, NuevoUsuarioSistemaForm nuevoUsuarioSistemaForm, HttpServletRequest request) throws Exception {
        try (Connection c = DataBaseManager.getConnection()) {
            ActionErrors errors = nuevoUsuarioSistemaForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                loadInitialData(nuevoUsuarioSistemaForm, request);
                return mapping.findForward(Constants.VOLVER);
            }
            //Comprobamos que las contraseñas introducidas sean iguales
            if (!nuevoUsuarioSistemaForm.getPassword().equals(nuevoUsuarioSistemaForm.getConfirmar_password())) {
                errors.add("errorDistintos", new ActionMessage("pass.distintos"));
                saveErrors(request, errors);
                return mapping.findForward(Constants.VOLVER);
            }
            //Comprobamos que el password usa caracteres correctos
            ComprobadorCaracteres cc = new ComprobadorCaracteres(nuevoUsuarioSistemaForm.getPassword());
            boolean result = cc.isNombreValido();
            if (!result) {
                errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                saveErrors(request, errors);
                return mapping.findForward(Constants.USUARIO_DUPLICADO);
            }

            //Comprobamos que el nombre de usuario usa caracteres correctos
            cc = new ComprobadorCaracteres(nuevoUsuarioSistemaForm.getNombre());
            result = cc.isNombreValido();
            if (!result) {
                errors.add("usuarioDuplicado", new ActionMessage("caracteres.prohibidos"));
                saveErrors(request, errors);
                return mapping.findForward(Constants.USUARIO_DUPLICADO);
            }

            String nombre = cc.espacios();

            if (LoginDAO.existUser(c, nombre)) {
                errors.add("usuarioDuplicado", new ActionMessage("usuario.duplicado"));
                saveErrors(request, errors);
                return mapping.findForward(Constants.USUARIO_DUPLICADO);
            }

            LoginDAO.insertUser(c, nuevoUsuarioSistemaForm);

            ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.crear.usuario", "volver.carga.usuarios");
            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            log.error("Excepción genérica al crear un nuevo usuario");
            throw new Exception(e);
        }
    }

}