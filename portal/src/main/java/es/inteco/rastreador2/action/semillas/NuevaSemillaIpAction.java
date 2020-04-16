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
package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.NuevaSemillaIpForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GeneraRango;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class NuevaSemillaIpAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // Marcamos el menú
        request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_IP);
        Connection c = null;

        try {
            c = DataBaseManager.getConnection();

            if (CrawlerUtils.hasAccess(request, "ip.range.seed")) {
                NuevaSemillaIpForm nuevaSemillaIpForm = (NuevaSemillaIpForm) form;

                if (isCancelled(request)) {
                    if (request.getParameter(Constants.BOTON_SEMILLA_IP) != null) {
                        return (mapping.findForward(Constants.VOLVER_CARGA_MENU));
                    } else {
                        return (mapping.findForward(Constants.VOLVER));
                    }
                }

                request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));

                //comprobamos de donde viene
                String accionForm = request.getParameter(Constants.ACCION);
                if (accionForm == null || accionForm.trim().equals("")) {
                    return mapping.findForward(Constants.VOLVER);
                }

                ActionErrors errors = nuevaSemillaIpForm.validate(mapping, request);

                if (SemillaDAO.existSeed(c, nuevaSemillaIpForm.getNombreSemilla(), Constants.ID_LISTA_ALL)) {
                    errors.add("nombreDuplicado", new ActionMessage("mensaje.error.nombre.semilla.duplicado"));
                    saveErrors(request, errors);

                    return mapping.findForward(Constants.VOLVER);
                }

                if (nuevaSemillaIpForm.getPuerto2().equals("")) {
                    nuevaSemillaIpForm.setPuerto2("-1");
                }
                if (nuevaSemillaIpForm.getPuerto3().equals("")) {
                    nuevaSemillaIpForm.setPuerto3("-1");
                }

                if (errors.isEmpty()) {

                    List<Integer> puertos = new ArrayList<>();
                    if (!nuevaSemillaIpForm.getPuerto1().equals("-1")) {
                        puertos.add(Integer.parseInt(nuevaSemillaIpForm.getPuerto1()));
                    }
                    if (!nuevaSemillaIpForm.getPuerto2().equals("-1")) {
                        puertos.add(Integer.parseInt(nuevaSemillaIpForm.getPuerto2()));
                    }
                    if (!nuevaSemillaIpForm.getPuerto3().equals("-1")) {
                        puertos.add(Integer.parseInt(nuevaSemillaIpForm.getPuerto3()));
                    }

                    int dimension = puertos.size();
                    int[] arrayPuertos = new int[dimension];
                    int conta = 0;
                    for (Integer puerto : puertos) {
                        arrayPuertos[conta] = puerto;
                        conta++;
                    }

                    boolean ipValida = comprobarIp(nuevaSemillaIpForm.getIpInicial1(), nuevaSemillaIpForm.getIpFinal1());
                    if (ipValida) {
                        ipValida = comprobarIp(nuevaSemillaIpForm.getIpInicial2(), nuevaSemillaIpForm.getIpFinal2());
                    }
                    if (ipValida) {
                        ipValida = comprobarIp(nuevaSemillaIpForm.getIpInicial3(), nuevaSemillaIpForm.getIpFinal3());
                    }
                    if (ipValida) {
                        ipValida = comprobarIp(nuevaSemillaIpForm.getIpInicial4(), nuevaSemillaIpForm.getIpFinal4());
                    }


                    if (!ipValida) {
                        errors.add("errorObligatorios", new ActionMessage("ip.mayor"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    }

                    String ip1 = nuevaSemillaIpForm.getIpInicial1() + "." + nuevaSemillaIpForm.getIpInicial2() + "." + nuevaSemillaIpForm.getIpInicial3() + "." + nuevaSemillaIpForm.getIpInicial4();
                    String ip2 = nuevaSemillaIpForm.getIpFinal1() + "." + nuevaSemillaIpForm.getIpFinal2() + "." + nuevaSemillaIpForm.getIpFinal3() + "." + nuevaSemillaIpForm.getIpFinal4();

                    String listaUrls = "";
                    listaUrls = GeneraRango.getRango(arrayPuertos, ip1, ip2, listaUrls);

                    SemillaDAO.insertList(c, Constants.ID_LISTA_SEMILLA, nuevaSemillaIpForm.getNombreSemilla(), listaUrls, nuevaSemillaIpForm.getCategoria().getId(), null, null, null, null, null);

                    ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.semilla.generada", "volver.nueva.semilla.ip");
                    return mapping.findForward(Constants.EXITO);

                } else {
                    ActionForward forward = new ActionForward();
                    forward.setPath(mapping.getInput());
                    forward.setRedirect(true);
                    saveErrors(request.getSession(), errors);
                    return (forward);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    public static boolean comprobarIp(String ipIni, String ipFin) {
        return Integer.parseInt(ipIni) <= Integer.parseInt(ipFin);
    }
}