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
package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
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
 * The Class EliminarObservatorioAction.
 */
public class EliminarObservatorioAction extends Action {

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
            if (CrawlerUtils.hasAccess(request, "delete.observatory")) {
                String idObservatorio = request.getParameter(Constants.ID_OBSERVATORIO);
                if (request.getParameter(Constants.ES_PRIMERA) != null) {
                    return confirm(mapping, request);
                } else {
                    if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_SI)) {
                        return deleteObservatory(mapping, request, Long.parseLong(idObservatorio));
                    } else {
                        return mapping.findForward(Constants.VOLVER);
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

    /**
	 * Confirm.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
    private ActionForward confirm(ActionMapping mapping, HttpServletRequest request) throws Exception {
        final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
        try (Connection c = DataBaseManager.getConnection()) {
            ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
            request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
        }

        return mapping.findForward(Constants.CONFIRMACION_DELETE);
    }

    /**
	 * Delete observatory.
	 *
	 * @param mapping        the mapping
	 * @param request        the request
	 * @param idObservatorio the id observatorio
	 * @return the action forward
	 */
    private ActionForward deleteObservatory(ActionMapping mapping, HttpServletRequest request, long idObservatorio) {
        try (Connection c = DataBaseManager.getConnection()) {
            try {
                ObservatorioDAO.deteleObservatory(c, idObservatorio);
            } catch (Exception e) {
                Logger.putLog("Exception: ", EliminarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }

            ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.observatorio.eliminado", "volver.carga.observatorio");
            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }
}