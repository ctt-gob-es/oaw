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
package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CargarCuentasUsuarioForm;
import es.inteco.rastreador2.actionform.cuentausuario.NuevaCuentaUsuarioForm;
import es.inteco.rastreador2.actionform.cuentausuario.RastreoClienteForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * The Class RastreoClienteAction.
 */
public class RastreoClienteAction extends Action {

    /**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "new.crawler")) {
                if (isCancelled(request)) {
                    return mapping.findForward(Constants.VOLVER_CARGA);
                }

                final RastreoClienteForm rastreoClienteForm = (RastreoClienteForm) form;
                //para saber si viene de inicio o del formulario
                final String accionFor = request.getParameter(Constants.ACCION_FOR);


                try (Connection c = DataBaseManager.getConnection()) {
                    CargarCuentasUsuarioForm cargarCuentasUsuarioForm = new CargarCuentasUsuarioForm();
                    // Cargamos las cuentas de cliente
                    cargarCuentasUsuarioForm = CuentaUsuarioDAO.userList(c, cargarCuentasUsuarioForm, Constants.NO_PAGINACION);
                    request.setAttribute(Constants.LISTADO_CUENTAS_CLIENTE, cargarCuentasUsuarioForm.getListadoCuentasUsuario());

                    // Cargamos los cartuchos
                    DatosForm userData = LoginDAO.getUserDataByName(c, (String) request.getSession().getAttribute(Constants.USER));
                    request.setAttribute(Constants.LISTADO_CARTUCHOS, LoginDAO.getUserCartridge(c, Long.valueOf(userData.getId())));

                    //Cargamos las normas
                    request.setAttribute(Constants.LISTADO_NORMAS, DAOUtils.getNormas(c, false));

                    if (accionFor != null) {
                        final ActionErrors errors = rastreoClienteForm.validate(mapping, request);
                        if (errors.isEmpty()) {
                            if (RastreoDAO.existeRastreo(c, rastreoClienteForm.getNombre())) {
                                errors.add("errorObligatorios", new ActionMessage("rastreo.duplicado"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.NUEVO_RASTREO_CLIENTE);
                            }

                            NuevaCuentaUsuarioForm cuentaUsuario = CuentaUsuarioDAO.getUserAccount(c, Long.valueOf(rastreoClienteForm.getIdCuenta()));

                            InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
                            insertarRastreoForm.setCuenta_cliente(Long.valueOf(rastreoClienteForm.getIdCuenta()));
                            insertarRastreoForm.setCartucho(rastreoClienteForm.getCartucho());
                            if (CartuchoDAO.isCartuchoAccesibilidad(c, Long.parseLong(insertarRastreoForm.getCartucho()))) {
                                insertarRastreoForm.setNormaAnalisis(rastreoClienteForm.getNormaAnalisis());
                            }
                            insertarRastreoForm.setCodigo(rastreoClienteForm.getNombre());
                            insertarRastreoForm = CrawlerUtils.insertarDatosAutomaticosCU(insertarRastreoForm, cuentaUsuario, "");
                            insertarRastreoForm.setId_semilla(cuentaUsuario.getIdSeed());
                            insertarRastreoForm.setId_lista_rastreable(cuentaUsuario.getIdCrawlableList());
                            insertarRastreoForm.setId_lista_no_rastreable(cuentaUsuario.getIdNoCrawlableList());
                            insertarRastreoForm.setActive(true);
                            RastreoDAO.insertarRastreo(c, insertarRastreoForm, false);

                            ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.rastreo.insertado", "volver.cargar.rastreos");
                            return mapping.findForward(Constants.EXITO);
                        } else {
                            ActionForward forward = new ActionForward();
                            forward.setPath(mapping.getInput());
                            forward.setRedirect(true);
                            saveErrors(request.getSession(), errors);
                            return forward;
                        }
                    }
                } catch (Exception e) {
                    Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", RastreoClienteAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                }
                return mapping.findForward(Constants.NUEVO_RASTREO_CLIENTE);
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

}