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
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.CargarRastreosRealizadosSearchForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
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
import java.util.List;

public class CargarRastreosRealizadosAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        try {

            if (CrawlerUtils.hasAccess(request, "load.crawlers")) {

                Long idCrawling = Long.valueOf(request.getParameter(Constants.ID_RASTREO));
                String user = (String) request.getSession().getAttribute(Constants.USER);

                Connection c = null;
                //Para mostrar todos los Rastreos del Sistema
                try {
                    CargarRastreosRealizadosSearchForm searchForm = (CargarRastreosRealizadosSearchForm) form;
                    c = DataBaseManager.getConnection();

                    int numResult = RastreoDAO.countRastreosRealizados(c, idCrawling, searchForm);
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                    request.setAttribute(Constants.LISTADO_CARTUCHOS, LoginDAO.getAllUserCartridge(c));
                    request.setAttribute(Constants.LISTADO_SEMILLAS, SemillaDAO.getSeeds(c, Constants.ID_LISTA_SEMILLA));
                    List<FulFilledCrawling> crawlings = RastreoDAO.getFulfilledCrawlings(c, idCrawling, searchForm, null, (pagina - 1));
                    //Comprobamos que el usuario esta asociado con los rastreos realizados que quiere cargar
                    if (RastreoDAO.crawlerToUser(c, idCrawling, user) || RastreoDAO.crawlerToClientAccount(c, idCrawling, user)) {
                        request.setAttribute(Constants.CRAWLINGS_FORM, CrawlerUtils.getCrawlingsForm(crawlings));
                        request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
                    } else {
                        return mapping.findForward(Constants.NO_PERMISSION);
                    }

                } catch (Exception e) {
                    Logger.putLog("Exception: ", CargarRastreosRealizadosAction.class, Logger.LOG_LEVEL_ERROR, e);
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