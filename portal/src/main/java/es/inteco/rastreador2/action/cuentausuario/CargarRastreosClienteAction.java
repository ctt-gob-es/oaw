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
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

/**
 * The Class CargarRastreosClienteAction.
 */
public class CargarRastreosClienteAction extends Action {

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

        request.getSession().setAttribute(Constants.MENU, Constants.MENU_CLIENT_CRAWLINGS);

        try {

            if (CrawlerUtils.hasAccess(request, "load.crawlers.client")) {

                //inicializamos esta variable para evitar incongruencias
                //sesion.setAttribute(Constants.ARCHIVO_SEMILLA_SERVER, "");

                if (request.getParameter(Constants.ACCION) != null) {
                    if (request.getParameter(Constants.ACCION).equals(Constants.RASTREOS)) {
                        return loadClientCrawlings(mapping, request);
                    } else if (request.getParameter(Constants.ACCION).equals(Constants.RASTREOS_REALIZADOS)) {
                        return loadClientFulfilledCrawlings(mapping, request);
                    }
                }
                return null;
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    /**
	 * Load client crawlings.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
    private ActionForward loadClientCrawlings(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Connection c = null;
        //Para mostrar todos los Rastreos del Sistema
        try {
            c = DataBaseManager.getConnection();

            DatosForm userData = LoginDAO.getUserDataByName(c, (String) request.getSession().getAttribute(Constants.USER));
            int numResult = CuentaUsuarioDAO.getNumClientCrawlings(c, Long.valueOf(userData.getId()));
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
            request.setAttribute(Constants.RASTREOS, CuentaUsuarioDAO.getClientCrawlings(c, Long.valueOf(userData.getId()), (pagina - 1)));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
        } catch (Exception e) {
            Logger.putLog("Exception: ", CargarRastreosClienteAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        return mapping.findForward(Constants.RASTREOS_CLIENTE);
    }

    /**
	 * Load client fulfilled crawlings.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
    private ActionForward loadClientFulfilledCrawlings(ActionMapping mapping, HttpServletRequest request) throws Exception {
        Long idCrawling = Long.valueOf(request.getParameter(Constants.ID_RASTREO));
        String user = (String) request.getSession().getAttribute(Constants.USER);

        Connection c = null;
        //Para mostrar todos los Rastreos del Sistema
        try {
            c = DataBaseManager.getConnection();

            int numResult = CuentaUsuarioDAO.getNumClientFulfilledCrawlings(c, idCrawling);
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            List<FulFilledCrawling> crawlings = CuentaUsuarioDAO.getFulfilledCrawlings(c, idCrawling, (pagina - 1));
            if (RastreoDAO.crawlerToClientAccount(c, idCrawling, user)) {
                request.setAttribute(Constants.CRAWLINGS_FORM, CrawlerUtils.getCrawlingsForm(crawlings));
                request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", CargarRastreosClienteAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.RASTREOS_REALIZADOS);
    }

}