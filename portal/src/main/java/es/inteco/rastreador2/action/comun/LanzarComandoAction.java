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
import es.inteco.crawler.job.CrawlerJobManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.comun.LanzarComandoForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * The Class LanzarComandoAction.
 */
public class LanzarComandoAction extends Action {

    /**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "throw.crawler")) {
                LanzarComandoForm lanzarComandoForm = (LanzarComandoForm) form;

                HttpSession sesion = request.getSession();

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                String estadoAntiguo = (String) sesion.getAttribute(Constants.ESTADO_ANTERIOR);

                if (estadoAntiguo == null) {
                    estadoAntiguo = "";
                }
                lanzarComandoForm.setEstado_antiguo(estadoAntiguo);

                String rastreo;
                if (request.getParameter(Constants.RASTREO) != null) {
                    rastreo = request.getParameter(Constants.RASTREO);
                    sesion.setAttribute(Constants.RASTREO, rastreo);
                } else if (sesion.getAttribute(Constants.RASTREO) != null) {
                    rastreo = (String) sesion.getAttribute(Constants.RASTREO);
                } else {
                    rastreo = (String) sesion.getAttribute(Constants.RASTREO_INTERMEDIO);
                }
                lanzarComandoForm.setRastreo(rastreo);


                String comando;
                String user;
                String pass;
                if (request.getParameter(Constants.COMANDO) != null) {
                    comando = request.getParameter(Constants.COMANDO);
                    user = request.getParameter(Constants.COM_USER);
                    pass = request.getParameter(Constants.COM_PASS);
                    sesion.setAttribute(Constants.COMANDO, comando);
                    sesion.setAttribute(Constants.COM_USER, user);
                    sesion.setAttribute(Constants.COM_PASS, pass);
                } else {
                    comando = (String) sesion.getAttribute(Constants.COMANDO);
                    user = (String) sesion.getAttribute(Constants.COM_USER);
                    pass = (String) sesion.getAttribute(Constants.COM_PASS);
                }


                lanzarComandoForm.setUser(user);
                lanzarComandoForm.setPass(pass);
                lanzarComandoForm.setComando(comando);

                //COMPROBAMOS TIMEOUT
                int vecesTimeout = (Integer) sesion.getAttribute(Constants.VECES_TIMEOUT);
                if (vecesTimeout > 50) {
                    ActionUtils.setSuccesActionAttributes(request, "mensaje.error.tMaxEspera.superado", "volver.cargar.rastreos");
                    return mapping.findForward(Constants.ERROR_TIMEOUT);
                } else {
                    vecesTimeout++;
                    sesion.setAttribute(Constants.VECES_TIMEOUT, vecesTimeout);
                }

                ActionMessages messages = new ActionMessages();
                if (control(lanzarComandoForm, user)) {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("rastreo.detenido.con.exito"));
                } else {
                    messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("rastreo.lanzado.con.exito"));
                }
                saveMessages(request.getSession(), messages);

                ActionForward forward = new ActionForward(mapping.findForward(Constants.EXITO_SI));
                forward.setRedirect(true);
                return forward;
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

    }

    /**
	 * Control.
	 *
	 * @param lanzarComandoForm the lanzar comando form
	 * @param user              the user
	 * @return true, if successful
	 */
    public boolean control(LanzarComandoForm lanzarComandoForm, String user) {
        boolean control = false;

        try (Connection c = DataBaseManager.getConnection()) {
            //Si el comando está LAUNCH
            if (lanzarComandoForm.getComando().equals(Constants.LAUNCH)) {
                DatosCartuchoRastreoForm dcrForm = RastreoDAO.cargarDatosCartuchoRastreo(c, lanzarComandoForm.getRastreo());
                dcrForm.setCartuchos(CartuchoDAO.getNombreCartucho(dcrForm.getId_rastreo()));

                // Cargamos los dominios introducidos en el archivo de semillas
                int typeDomains = dcrForm.getIdObservatory() == 0 ? Constants.ID_LISTA_SEMILLA : Constants.ID_LISTA_SEMILLA_OBSERVATORIO;
                dcrForm.setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, false));

                dcrForm.setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, true));
                dcrForm.setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
                dcrForm.setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));

                dcrForm.setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) dcrForm.getId_rastreo()));

                if (CartuchoDAO.isCartuchoAccesibilidad(c, dcrForm.getId_cartucho())) {
                    dcrForm.setFicheroNorma(CrawlerUtils.getFicheroNorma(dcrForm.getId_guideline()));
                }

                DatosForm userData = LoginDAO.getUserDataByName(c, user);

                Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, dcrForm, null, Long.valueOf(userData.getId()));

                ArrayList<String> mailTo = new ArrayList<>();
                mailTo.add(userData.getEmail());

                CrawlerJobManager.startJob(CrawlerUtils.getCrawlerData(dcrForm, idFulfilledCrawling, user, mailTo));

                //Si el comando está STOP
            } else {
                DatosCartuchoRastreoForm dcrForm = RastreoDAO.cargarDatosCartuchoRastreo(c, lanzarComandoForm.getRastreo());
                RastreoDAO.actualizarEstadoRastreo(c, dcrForm.getId_rastreo(), Constants.STATUS_STOPPED);
                CrawlerJobManager.endJob(dcrForm.getId_rastreo());

                control = true;
            }
        } catch (Exception e) {
            Logger.putLog("Excepcion ", LanzarComandoAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return control;
    }

}