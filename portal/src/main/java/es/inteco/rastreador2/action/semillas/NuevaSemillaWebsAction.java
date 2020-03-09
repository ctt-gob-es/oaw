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
import es.inteco.rastreador2.actionform.semillas.NuevaSemillaWebsForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class NuevaSemillaWebsAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        try (Connection c = DataBaseManager.getConnection()) {
            request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        if (request.getParameter(Constants.INICIAL) == null && request.getAttribute(Constants.INICIAL) == null) {
            try (Connection c = DataBaseManager.getConnection()) {
                if (CrawlerUtils.hasAccess(request, "web.list.seed")) {
                    NuevaSemillaWebsForm nuevaSemillaWebsForm = (NuevaSemillaWebsForm) form;
                    if (isCancelled(request)) {
                        return (mapping.findForward(Constants.VOLVER));
                    }

                    ActionErrors errors = nuevaSemillaWebsForm.validate(mapping, request);
                    if (errors.isEmpty()) {
                        if (SemillaDAO.existSeed(c, nuevaSemillaWebsForm.getNombreSemilla(), Constants.ID_LISTA_ALL)) {
                            errors.add("nombreDuplicado", new ActionMessage("mensaje.error.nombre.semilla.duplicado"));
                            saveErrors(request, errors);
                            return mapping.findForward(Constants.NUEVA_SEMILLA_FORWARD);
                        }

                        List<String> validUrls = SeedUtils.getValidUrls(nuevaSemillaWebsForm.getTa1(), true);
                        if (validUrls.size() == 0) {
                            errors.add("usuarioDuplicado", new ActionMessage("unaUrl.valida"));
                            saveErrors(request, errors);
                            return mapping.findForward(Constants.NUEVA_SEMILLA_FORWARD);
                        }

                        SemillaDAO.insertList(c, Constants.ID_LISTA_SEMILLA, nuevaSemillaWebsForm.getNombreSemilla(), SeedUtils.getSeedUrlsForDatabase(validUrls), nuevaSemillaWebsForm.getCategoria().getId(), null, null, null, null, null);

                        ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.semilla.generada", "volver.listado.semillas");
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
            }
        } else {
            return mapping.findForward(Constants.NUEVA_SEMILLA_FORWARD);
        }
    }

}