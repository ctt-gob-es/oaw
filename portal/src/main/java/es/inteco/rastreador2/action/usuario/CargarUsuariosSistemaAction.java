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
import es.inteco.rastreador2.actionform.usuario.CargarUsuariosSistemaForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 * The Class CargarUsuariosSistemaAction.
 */
public class CargarUsuariosSistemaAction extends Action {

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
        // Marcamos el menú
        request.getSession().setAttribute(Constants.MENU, Constants.MENU_USERS);

        try {

            if (CrawlerUtils.hasAccess(request, "load.users")) {
                CargarUsuariosSistemaForm cargarUsuariosSistemaForm = (CargarUsuariosSistemaForm) form;
                String user = (String) request.getSession().getAttribute(Constants.USER);
                String pass = (String) request.getSession().getAttribute(Constants.PASS);
                cargarUsuariosSistemaForm.setUser(user);
                cargarUsuariosSistemaForm.setPass(pass);

                Connection c = null;
                try {
                    c = DataBaseManager.getConnection();
                    int numResult = LoginDAO.countUsers(c);
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                    cargarUsuariosSistemaForm.setListadoUsuarios(LoginDAO.userList(c, (pagina - 1)));
                    request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
                } catch (Exception e) {
                    Logger.putLog("Exception: ", CargarUsuariosSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                } finally {
                    DataBaseManager.closeConnection(c);
                }
                return mapping.findForward(Constants.EXITO);

            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }
}