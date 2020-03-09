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
package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.SubirSemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class SubirSemillaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        Connection c = null;
        try {
            if (CrawlerUtils.hasAccess(request, "edit.crawler")) {
                SubirSemillaForm subirSemillaForm = (SubirSemillaForm) form;

                if (request.getParameter(Constants.ID_RASTREO) != null) {
                    request.setAttribute(Constants.ID_RASTREO, request.getParameter(Constants.ID_RASTREO));
                }

                if (request.getParameter(Constants.ID_SEMILLA) != null) {
                    if (request.getParameter(Constants.IS_NEW) != null && request.getParameter(Constants.IS_NEW).equals("true")) {
                        subirSemillaForm.setSemilla(request.getParameter(Constants.ID_SEMILLA));
                        request.setAttribute(Constants.ID_SEMILLA, request.getParameter(Constants.ID_SEMILLA));
                        return mapping.findForward(Constants.SEMILLA_OK_NUEVO);
                    } else if (request.getParameter(Constants.IS_NEW) != null && request.getParameter(Constants.IS_NEW).equals("false")) {
                        subirSemillaForm.setSemilla(request.getParameter(Constants.ID_SEMILLA));
                        request.setAttribute(Constants.ID_SEMILLA, request.getParameter(Constants.ID_SEMILLA));
                        return mapping.findForward(Constants.SEMILLA_OK_MODIFICAR);
                    }
                }

                c = DataBaseManager.getConnection();

                int numResult = SemillaDAO.countSeedsChoose(c, Constants.ID_LISTA_SEMILLA, new SemillaSearchForm());
                int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                request.setAttribute(Constants.SEED_LIST, SemillaDAO.getSeedsChoose(c, (pagina - 1), new SemillaSearchForm()));
                request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));

                request.setAttribute(Constants.IS_NEW, request.getParameter(Constants.IS_NEW));
                return mapping.findForward(Constants.EXITO);
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

}