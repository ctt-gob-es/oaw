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
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * The Class EliminarSemillaAction.
 */
public class EliminarSemillaAction extends Action {

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
        if (CrawlerUtils.hasAccess(request, "delete.seed")) {
            if (isCancelled(request)) {
                return (mapping.findForward(Constants.VOLVER_CARGA));
            }

            try (Connection c = DataBaseManager.getConnection()) {
                String idSemilla = "";
                if (request.getParameter(Constants.SEGUNDA) == null) {
                    if (request.getParameter(Constants.SEMILLA) != null) {
                        idSemilla = request.getParameter(Constants.SEMILLA);
                        SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
                        request.setAttribute(Constants.SEMILLA_FORM, semillaForm);
                    }
                    return mapping.findForward(Constants.EXITO);
                } else {
                    if (request.getParameter(Constants.SEMILLA) != null) {
                        idSemilla = request.getParameter(Constants.SEMILLA);
                    }

                    SemillaDAO.deleteSeed(c, Long.parseLong(idSemilla));

                    ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.semilla.borrada", "volver.listado.semillas");
                    return mapping.findForward(Constants.SEMILLA_ELIMINADA);
                }
            } catch (Exception e) {
                CrawlerUtils.warnAdministrators(e, this.getClass());
                return mapping.findForward(Constants.ERROR_PAGE);
            }
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
    }

}