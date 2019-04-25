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
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class VerCuentaClienteAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        try {
            if (CrawlerUtils.hasAccess(request, "view.client.account")) {
                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                String action = request.getParameter(Constants.ACCION);
                if (action != null) {
                    if (action.equals(Constants.LIST_ACCOUNTS)) {
                        return listAccounts(mapping, request);
                    } else if (action.equals(Constants.GET_DETAIL)) {
                        return getDetail(mapping, request);
                    }
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        return null;
    }

    private ActionForward listAccounts(ActionMapping mapping, HttpServletRequest request) throws Exception {
        request.getSession().setAttribute(Constants.MENU, Constants.MENU_CLIENT_CRAWLINGS_ACCOUNT);

        try (Connection conn = DataBaseManager.getConnection()) {
            request.setAttribute(Constants.LIST_ACCOUNTS, CuentaUsuarioDAO.getAccountsFromUser(conn, (String) request.getSession().getAttribute(Constants.USER)));
        } catch (Exception e) {
            Logger.putLog("Exception", CuentaClienteUtils.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return mapping.findForward(Constants.LIST_ACCOUNTS);
    }

    private ActionForward getDetail(ActionMapping mapping, HttpServletRequest request) throws Exception {
        long idCuenta = 0;
        if (request.getParameter(Constants.ID_CUENTA) != null) {
            idCuenta = Long.parseLong(request.getParameter(Constants.ID_CUENTA));
        }

        request.setAttribute(Constants.VER_CUENTA_USUARIO_FORM, CuentaClienteUtils.getCuentaUsuarioForm(idCuenta));

        return mapping.findForward(Constants.EXITO);
    }
}
