package es.inteco.rastreador2.action.categoria;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.categoria.CargarCategoriasForm;
import es.inteco.rastreador2.dao.categorias.CategoriasDAO;
import es.inteco.rastreador2.utils.BorradorTerminos;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class CargarCategoriasAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // Marcamos el menú
        request.getSession().setAttribute(Constants.SUBMENU, Constants.MENU_CATEGORIES);

        try {
            if (CrawlerUtils.hasAccess(request, "load.categories")) {
                CargarCategoriasForm cargarCategoriasForm = (CargarCategoriasForm) form;
                BorradorTerminos.borra();

                Connection c = null;
                try {
                    c = DataBaseManager.getConnection();
                    int numResult = CategoriasDAO.countCategories(c);
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                    cargarCategoriasForm.setCats(CategoriasDAO.loadCategories(c, (pagina - 1)));
                    request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
                } catch (Exception e) {
                    Logger.putLog("Exception: ", CargarCategoriasAction.class, Logger.LOG_LEVEL_ERROR, e);
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