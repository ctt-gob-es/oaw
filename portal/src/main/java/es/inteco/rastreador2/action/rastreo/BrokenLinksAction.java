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
import es.inteco.common.logging.Logger;
import es.inteco.plugin.BrokenLinks;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.rastreo.BrokenLinksDAO;
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

public class BrokenLinksAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (CrawlerUtils.hasAccess(request, "show.crawler.results")) {
            Connection c = null;
            try {
                c = DataBaseManager.getConnection();
                final String user = (String) request.getSession().getAttribute(Constants.USER);
                final long idExecution = request.getParameter(Constants.ID) != null ? Long.parseLong(request.getParameter(Constants.ID)) : 0;
                final long idRastreo = request.getParameter(Constants.ID_RASTREO) != null ? Long.parseLong(request.getParameter(Constants.ID_RASTREO)) : 0;

                //Comprobamos que el usuario esta asociado con los resultados de los rastreos que quiere recuperar
                if (RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

                    List<BrokenLinks> brokenLinksList = BrokenLinksDAO.getBrokenLinks(c, idExecution, (pagina - 1));
                    Integer numResults = BrokenLinksDAO.getNumBrokenLinks(c, idExecution);
                    request.setAttribute(Constants.BROKEN_LINKS, brokenLinksList);
                    request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResults, pagina));
                } else {
                    return mapping.findForward(Constants.NO_PERMISSION);
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", BrokenLinksAction.class, Logger.LOG_LEVEL_ERROR, e);
                return mapping.findForward(Constants.ERROR);
            } finally {
                DataBaseManager.closeConnection(c);
            }

            return mapping.findForward(Constants.GET_BROKEN_LINKS);
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
    }

}
