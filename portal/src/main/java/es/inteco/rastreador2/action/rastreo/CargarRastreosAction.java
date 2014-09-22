package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.CargarRastreosForm;
import es.inteco.rastreador2.actionform.rastreo.CargarRastreosSearchForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class CargarRastreosAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // Marcamos el menú
        request.getSession().setAttribute(Constants.MENU, Constants.MENU_CRAWLINGS);

        try {
            if (CrawlerUtils.hasAccess(request, "load.crawlers")) {
                HttpSession sesion = request.getSession();

                Connection c = null;
                //Para mostrar todos los Rastreos del Sistema
                try {
                    CargarRastreosSearchForm searchForm = (CargarRastreosSearchForm) form;

                    c = DataBaseManager.getConnection();

                    int numResult = RastreoDAO.countRastreo(c, (String) sesion.getAttribute(Constants.USER), searchForm);
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                    CargarRastreosForm cargarRastreosForm = RastreoDAO.getLoadCrawlingForm(c, (String) sesion.getAttribute(Constants.USER), searchForm, (pagina - 1));
                    request.setAttribute(Constants.CARGAR_RASTREOS_FORM, cargarRastreosForm);
                    request.setAttribute(Constants.LISTADO_CARTUCHOS, LoginDAO.getAllUserCartridge(c));
                    request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
                } catch (Exception e) {
                    Logger.putLog("Exception: ", CargarRastreosAction.class, Logger.LOG_LEVEL_ERROR, e);
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