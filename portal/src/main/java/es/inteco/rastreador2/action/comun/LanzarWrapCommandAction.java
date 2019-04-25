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
package es.inteco.rastreador2.action.comun;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.comun.LanzarWrapCommandForm;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class LanzarWrapCommandAction extends org.apache.struts.action.Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            ActionErrors errors = new ActionErrors();

            if (CrawlerUtils.hasAccess(request, "throw.crawler")) {

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                LanzarWrapCommandForm lanzarWrapCommandForm = (LanzarWrapCommandForm) form;
                HttpSession sesion = request.getSession();

                int vecesTimeout = 0;
                sesion.setAttribute(Constants.VECES_TIMEOUT, vecesTimeout);
                //VECES QUE SE REFRESCA LA PANTALLA AL LANZAR/PARAR UN RASTREO

                //VARIABLE VECES QUE SE BORRA
                int vecesBorrado = 0;
                sesion.setAttribute(Constants.VECES_BORRADO, vecesBorrado);


                if (request.getParameter(Constants.OPCION) != null) {
                    lanzarWrapCommandForm.setOpcion(request.getParameter(Constants.OPCION));
                    sesion.setAttribute(Constants.OPCION_INTERMEDIO, lanzarWrapCommandForm.getOpcion());
                } else {
                    lanzarWrapCommandForm.setOpcion((String) sesion.getAttribute(Constants.OPCION_INTERMEDIO));
                }

                //esto es comun a ambos
                if (request.getParameter(Constants.ID_RASTREO) != null) {
                    lanzarWrapCommandForm.setC_rastreo(request.getParameter(Constants.ID_RASTREO));
                    sesion.setAttribute(Constants.RASTREO_INTERMEDIO, lanzarWrapCommandForm.getC_rastreo());
                } else {
                    lanzarWrapCommandForm.setC_rastreo((String) sesion.getAttribute(Constants.RASTREO_INTERMEDIO));
                }

                try (Connection c = DataBaseManager.getConnection()) {
                    String user = (String) request.getSession().getAttribute(Constants.USER);

                    //Conseguimos el Id de Cartucho
                    DatosCartuchoRastreoForm datosCartuchoRastreoForm = RastreoDAO.cargarDatosCartuchoRastreo(c, lanzarWrapCommandForm.getC_rastreo());
                    lanzarWrapCommandForm.setRastreo(datosCartuchoRastreoForm.getNombre_rastreo());
                    lanzarWrapCommandForm.setCartucho(datosCartuchoRastreoForm.getNombre_cart());

                    //Comprobamos que el usuario esta asociado con el rastreo que quiere lanzar
                    if (RastreoDAO.crawlerCanBeThrow(c, user, datosCartuchoRastreoForm.getId_cartucho())) {
                        //SI ES LA PRIMERA VEZ QUE SE ENTRA
                        String destino;
                        if (request.getParameter(Constants.INTERMEDIO) == null) {
                            destino = Constants.EXITO_INTERMEDIO;
                            if (RastreoDAO.getNumActiveCrawlings(c, datosCartuchoRastreoForm.getId_cartucho()) >= datosCartuchoRastreoForm.getNumRastreos() &&
                                    lanzarWrapCommandForm.getOpcion().equals(Constants.OPCION_LANZAR)) {
                                ActionUtils.setSuccesActionAttributes(request, "mensaje.error.numMax.rastreo", "volver.cargar.rastreos");
                                return mapping.findForward(Constants.ERROR1);
                            }
                        } else {
                            destino = Constants.EXITO;
                            if (RastreoDAO.getNumActiveCrawlings(c, datosCartuchoRastreoForm.getId_cartucho()) >= datosCartuchoRastreoForm.getNumRastreos() &&
                                    lanzarWrapCommandForm.getOpcion().equals(Constants.OPCION_LANZAR)) {
                                errors.add("errorPassObligatorios", new ActionMessage("maximo.rastreos"));
                                saveErrors(request, errors);
                                return mapping.findForward(Constants.VOLVER);
                            } else if (lanzarWrapCommandForm.getOpcion().equals(Constants.OPCION_LANZAR)) {
                                //Modifica la fecha de lanzado del rastreo
                                RastreoDAO.actualizarFechaRastreo(c, datosCartuchoRastreoForm.getId_rastreo());
                            }
                        }

                        lanzarWrapCommandForm = cargarDatos(lanzarWrapCommandForm, request, sesion);
                        String fecha = RastreoDAO.cargarFechaRastreo(c, lanzarWrapCommandForm.getRastreo());
                        String fechaMod = fecha;
                        if (fecha.length() > 2 && fecha.endsWith(".0")) {
                            fechaMod = fecha.substring(0, fecha.length() - 2);
                        }
                        lanzarWrapCommandForm.setFecha(fechaMod);

                        //Si la opción es lanzar o parar
                        String comando = Constants.STOP;
                        String mensaje = Constants.PARAR1;
                        if (lanzarWrapCommandForm.getOpcion().equals(Constants.OPCION_LANZAR)) {
                            //comprobamos si hay 10 rastreos lanzados
                            comando = Constants.LAUNCH;
                            mensaje = Constants.LANZAR1;
                        }

                        lanzarWrapCommandForm.setComando(comando);
                        sesion.setAttribute(Constants.COMANDO, comando);
                        lanzarWrapCommandForm.setMensaje(mensaje);

                        if (!RastreoDAO.rastreoValidoParaUsuario(c, datosCartuchoRastreoForm.getId_rastreo(), lanzarWrapCommandForm.getUser())) {
                            ActionUtils.setSuccesActionAttributes(request, "mensaje.error.noPermisos", "volver.cargar.rastreos");
                            return mapping.findForward(Constants.NO_RASTREO_PERMISO);
                        }

                        // TODO: ¿Se puede eliminar esta condición?
                        if (destino.equals(Constants.EXITO_INTERMEDIO)) {
                            return mapping.findForward(destino);
                        }

                        return mapping.findForward(destino);
                    } else {
                        return mapping.findForward(Constants.NO_PERMISSION);
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", LanzarWrapCommandAction.class, Logger.LOG_LEVEL_ERROR, e);
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

    public LanzarWrapCommandForm cargarDatos(LanzarWrapCommandForm lanzarWrapCommandForm, HttpServletRequest request, HttpSession sesion) {
        String user = (String) request.getSession().getAttribute(Constants.USER);
        if (user != null) {
            sesion.setAttribute(Constants.USER_INTERMEDIO, user.trim());
        } else {
            user = (String) sesion.getAttribute(Constants.USER_INTERMEDIO);
        }

        lanzarWrapCommandForm.setUser(user);

        return lanzarWrapCommandForm;
    }
}