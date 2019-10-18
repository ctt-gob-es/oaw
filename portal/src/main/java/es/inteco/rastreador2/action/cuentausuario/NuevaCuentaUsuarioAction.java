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
package es.inteco.rastreador2.action.cuentausuario;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.NuevaCuentaUsuarioForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.CartuchoForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.servlets.ScheduleClientAccountsServlet;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class NuevaCuentaUsuarioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        try {
            if (CrawlerUtils.hasAccess(request, "new.client.account")) {
                final NuevaCuentaUsuarioForm nuevaCuentaUsuarioForm = (NuevaCuentaUsuarioForm) form;

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                try (Connection c = DataBaseManager.getConnection()) {

                    String esPrimera = request.getParameter(Constants.ES_PRIMERA);

                    nuevaCuentaUsuarioForm.setCartuchos(LoginDAO.getAllUserCartridge(c));
                    nuevaCuentaUsuarioForm.setPeriodicidadVector(DAOUtils.getRecurrence(c));

                    List<LenguajeForm> lenguajeFormList = DAOUtils.getLenguaje(c);
                    nuevaCuentaUsuarioForm.setLenguajeVector(new ArrayList<LenguajeForm>());
                    for (LenguajeForm lenguajeForm : lenguajeFormList) {
                        lenguajeForm.setName(getResources(request).getMessage(getLocale(request), lenguajeForm.getKeyName()));
                        nuevaCuentaUsuarioForm.getLenguajeVector().add(lenguajeForm);
                    }

                    request.setAttribute(Constants.MINUTES, CrawlerUtils.getMinutes());
                    request.setAttribute(Constants.HOURS, CrawlerUtils.getHours());

                    //Cargamos las normas
                    request.setAttribute(Constants.LISTADO_NORMAS, DAOUtils.getNormas(c, false));

                    if (esPrimera == null || esPrimera.trim().equals("")) {
                        return mapping.findForward(Constants.VOLVER);
                    } else {
                        ActionErrors errors = nuevaCuentaUsuarioForm.validate(mapping, request);

                        if (errors.isEmpty()) {
                            if (CuentaUsuarioDAO.existAccount(c, nuevaCuentaUsuarioForm.getNombre())) {
                                errors.add(Constants.USUARIO_DUPLICADO, new ActionMessage("cuenta.cliente.duplicada"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }

                            try {
                                nuevaCuentaUsuarioForm.setFecha(CrawlerUtils.getFechaInicio(nuevaCuentaUsuarioForm.getFechaInicio(),
                                        nuevaCuentaUsuarioForm.getHoraInicio(), nuevaCuentaUsuarioForm.getMinutoInicio()));
                            } catch (ParseException pe) {
                                errors.add(Constants.FORMATO_FECHA_INCORRECTO, new ActionMessage("formato.fecha.incorrecta"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }

                            //Guardamos la semilla
                            SemillaDAO.insertList(c, Constants.ID_LISTA_SEMILLA, nuevaCuentaUsuarioForm.getNombre() + "-Semilla", nuevaCuentaUsuarioForm.getDominio(), null, null, null, null);
                            Long idSeed = SemillaDAO.getIdList(c, nuevaCuentaUsuarioForm.getNombre() + "-Semilla", null);
                            nuevaCuentaUsuarioForm.setIdSeed(idSeed);

                            if (nuevaCuentaUsuarioForm.getListaRastreable() != null && !nuevaCuentaUsuarioForm.getListaRastreable().isEmpty()) {
                                //Guardamos la lista Rastreable
                                SemillaDAO.insertList(c, Constants.ID_LISTA_RASTREABLE, nuevaCuentaUsuarioForm.getNombre() + "-Rastreable", nuevaCuentaUsuarioForm.getListaRastreable(), null, null, null, null);
                                Long idCrawlableList = SemillaDAO.getIdList(c, nuevaCuentaUsuarioForm.getNombre() + "-Rastreable", null);
                                nuevaCuentaUsuarioForm.setIdCrawlableList(idCrawlableList);
                            }

                            if (nuevaCuentaUsuarioForm.getListaNoRastreable() != null && !nuevaCuentaUsuarioForm.getListaNoRastreable().isEmpty()) {
                                //Guardamos la lista No rastreable
                                SemillaDAO.insertList(c, Constants.ID_LISTA_NO_RASTREABLE, nuevaCuentaUsuarioForm.getNombre() + "-NoRastreable", nuevaCuentaUsuarioForm.getListaNoRastreable(), null, null, null, null);
                                Long idNoCrawlableList = SemillaDAO.getIdList(c, nuevaCuentaUsuarioForm.getNombre() + "-NoRastreable", null);
                                nuevaCuentaUsuarioForm.setIdNoCrawlableList(idNoCrawlableList);
                            }

                            Long idCuenta = CuentaUsuarioDAO.insertClientAccount(c, nuevaCuentaUsuarioForm);
                            if (idCuenta != null && !idCuenta.equals((long) 0)) {
                                InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
                                insertarRastreoForm.setCuenta_cliente(idCuenta);

                                //Obtenemos los datos de los Cartuchos seleccionados
                                ArrayList<CartuchoForm> cartuchosSelected = new ArrayList<>();
                                for (int i = 0; i < nuevaCuentaUsuarioForm.getCartuchos().size(); i++) {
                                    for (int j = 0; j < nuevaCuentaUsuarioForm.getCartuchosSelected().length; j++) {
                                        if (String.valueOf(nuevaCuentaUsuarioForm.getCartuchos().get(i).getId()).equals(
                                                nuevaCuentaUsuarioForm.getCartuchosSelected()[j])) {
                                            cartuchosSelected.add(nuevaCuentaUsuarioForm.getCartuchos().get(i));
                                        }
                                    }
                                }
                                insertarRastreoForm.setCartuchos(cartuchosSelected);

                                //Insertamos las referencias a los cartuchos comprobando si error
                                boolean hayError = false;
                                StringBuilder errorC = new StringBuilder();

                                for (CartuchoForm cartucho : cartuchosSelected) {
                                    //Activamos los enlaces rotos por defecto
                                    insertarRastreoForm.setNormaAnalisisEnlaces("1");
                                    insertarRastreoForm.setCartucho(String.valueOf(cartucho.getId()));
                                    insertarRastreoForm.setNormaAnalisis(nuevaCuentaUsuarioForm.getNormaAnalisis());
                                    insertarRastreoForm.setActive(true);
                                    // TODO: Comprobar por que el valor de retorno es String
                                    String cartuchoError = RastreoDAO.insertarRastreo(c, CrawlerUtils.insertarDatosAutomaticosCU(insertarRastreoForm, nuevaCuentaUsuarioForm, cartucho.getName()), true);
                                    if (!cartuchoError.equals("")) {
                                        if (!hayError) {
                                            errorC.append(cartuchoError);
                                        } else {
                                            errorC.append(",").append(cartuchoError);
                                        }
                                        hayError = true;
                                    }
                                }
                                if (hayError) {
                                    ActionUtils.setSuccesActionAttributes(request, "mensaje.error.cartucho", "volver.carga.cuentas.cliente");
                                    return mapping.findForward(Constants.EXITO);
                                }

                                if (nuevaCuentaUsuarioForm.isActivo()) {
                                    try {
                                        ScheduleClientAccountsServlet.scheduleJob(idCuenta, Constants.CLIENT_ACCOUNT_TYPE);
                                    } catch (Exception e) {
                                        ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.nueva.cuenta.error.job", "volver.carga.cuentas.cliente");
                                        return mapping.findForward(Constants.EXITO);
                                    }
                                }

                                ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.nueva.cuenta", "volver.carga.cuentas.cliente");
                                return mapping.findForward(Constants.EXITO);
                            } else {
                                errors.add("cuentaCliente", new ActionMessage("fallo.cuenta.cliente"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            }

                        } else {
                            saveErrors(request, errors);
                            return mapping.findForward(Constants.VOLVER);
                        }
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", NuevaCuentaUsuarioAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
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