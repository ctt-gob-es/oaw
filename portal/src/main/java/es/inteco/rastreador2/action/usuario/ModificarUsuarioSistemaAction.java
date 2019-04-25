/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.action.usuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm;
import es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm;
import es.inteco.rastreador2.actionform.usuario.ModificarUsuarioSistemaForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.ComprobadorCaracteres;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.Arrays;

public class ModificarUsuarioSistemaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (CrawlerUtils.hasAccess(request, "edit.user")) {
            if (request.getAttribute(Constants.DE_PASS) == null && isCancelled(request)) {
                return (mapping.findForward(Constants.VOLVER_CARGA));
            } else {
                String valem = request.getParameter(Constants.VALEMAIL);
                try {
                    ModificarUsuarioSistemaForm modificarUsuarioSistemaForm = (ModificarUsuarioSistemaForm) form;
                    loadInitialData(modificarUsuarioSistemaForm, request);
                    if (valem == null || valem.trim().equals("")) {
                        loadUserData(modificarUsuarioSistemaForm, request);
                        return mapping.findForward(Constants.VOLVER);
                    } else {
                        return editUser(mapping, modificarUsuarioSistemaForm, request);
                    }
                } catch (Exception e) {
                    CrawlerUtils.warnAdministrators(e, this.getClass());
                    return mapping.findForward(Constants.ERROR_PAGE);
                }
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
    }

    private ModificarUsuarioSistemaForm loadUserData(ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, HttpServletRequest request) throws Exception {
        try (Connection c = DataBaseManager.getConnection()) {
            //Comprobamos si estamos en inicio o se a pulsado al submit del formulario
            String idUsuario = request.getParameter(Constants.USER);
            modificarUsuarioSistemaForm.setIdUsuario(idUsuario);

            int userRolType = LoginDAO.getUserRolType(c, Long.valueOf(idUsuario));

            LoginDAO.getUserDatesToUpdate(c, modificarUsuarioSistemaForm, Long.valueOf(idUsuario), userRolType);

            return modificarUsuarioSistemaForm;
        } catch (Exception e) {
            Logger.putLog("Excepción en loadUserData", ModificarUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private ModificarUsuarioSistemaForm loadInitialData(ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, HttpServletRequest request) throws Exception {
        try (Connection c = DataBaseManager.getConnection()) {
            modificarUsuarioSistemaForm.setCartuchosList(LoginDAO.getAllUserCartridge(c));
            final String idUsuario = request.getParameter(Constants.USER);

            modificarUsuarioSistemaForm.setIdUsuario(idUsuario);
            final int userRolType = LoginDAO.getUserRolType(c, Long.valueOf(idUsuario));

            modificarUsuarioSistemaForm.setRoles(LoginDAO.getAllUserRoles(c, userRolType));

            final CargarCuentasUsuarioForm cargarCuentasUsuarioForm = new CargarCuentasUsuarioForm();
            modificarUsuarioSistemaForm.setCuenta_clienteV(CuentaUsuarioDAO.userList(c, cargarCuentasUsuarioForm, -1));

            final CargarObservatorioForm cargarObservatorioForm = new CargarObservatorioForm();
            modificarUsuarioSistemaForm.setObservatorioV(ObservatorioDAO.userList(c, cargarObservatorioForm));

            return modificarUsuarioSistemaForm;
        } catch (Exception e) {
            Logger.putLog("Excepción en loadInitialData", ModificarUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    private ActionForward editUser(ActionMapping mapping, ModificarUsuarioSistemaForm modificarUsuarioSistemaForm, HttpServletRequest request) throws Exception {
        try {
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
                try (Connection c = DataBaseManager.getConnection()) {
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
            ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.usuario.editado", "volver.carga.usuarios");
            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            Logger.putLog("Excepción en editUser", ModificarUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        }
    }

}