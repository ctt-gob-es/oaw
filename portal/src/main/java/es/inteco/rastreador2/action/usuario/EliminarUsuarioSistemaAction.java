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
import es.inteco.rastreador2.action.cuentausuario.EliminarCuentaUsuarioAction;
import es.inteco.rastreador2.actionform.usuario.EliminarUsuarioSistemaForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class EliminarUsuarioSistemaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "delete.user")) {

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER));
                }

                EliminarUsuarioSistemaForm eliminarUsuarioSistemaForm = (EliminarUsuarioSistemaForm) form;

                //comprobamos de donde viene
                if (request.getParameter(Constants.ID_USUARIO) == null) {
                    //no viene del submit de eliminar, hay que mostrar la pantalla de confiramcion de eliminado
                    String idUsuario = request.getParameter(Constants.USER);

                    try (Connection c = DataBaseManager.getConnection()) {
                        eliminarUsuarioSistemaForm = LoginDAO.getDeleteUser(c, Long.valueOf(idUsuario), eliminarUsuarioSistemaForm);
                    } catch (Exception e) {
                        Logger.putLog("Exception: ", EliminarCuentaUsuarioAction.class, Logger.LOG_LEVEL_ERROR, e);
                        throw new Exception(e);
                    }
                    return mapping.findForward(Constants.EXITO_ELIMINAR);
                } else {
                    try (Connection c = DataBaseManager.getConnection()) {
                        Long idUser = Long.valueOf(request.getParameter(Constants.ID_USUARIO));
                        LoginDAO.deleteUser(c, idUser);
                    } catch (Exception e) {
                        Logger.putLog("Exception: ", EliminarUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
                        throw new Exception(e);
                    }

                    ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.usuario.eliminado", "volver.carga.usuarios");
                    return mapping.findForward(Constants.EXITO);
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