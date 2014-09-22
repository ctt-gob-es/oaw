package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class ListadoSemillaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        // Marcamos el menú
        request.getSession().setAttribute(Constants.MENU, Constants.MENU_SEEDS);
        request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_LISTADO_SEM);
        Connection c = null;
        try {
            if (CrawlerUtils.hasAccess(request, "list.seed")) {
                SemillaSearchForm searchForm = (SemillaSearchForm) form;

                PropertiesManager pmgr = new PropertiesManager();
                c = DataBaseManager.getConnection();

                if (ActionUtils.redirectToLogin(request)) {
                    return mapping.findForward(Constants.LOGIN);
                }

                int numResult = SemillaDAO.countSeedsChoose(c, Constants.ID_LISTA_SEMILLA, searchForm);
                int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                request.setAttribute(Constants.SEED_LIST, SemillaDAO.getSeedsChoose(c, (pagina - 1), searchForm));
                request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));

                String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.semilla.borrada");
                String volver = pmgr.getValue("returnPaths.properties", "volver.listado.semillas");
                request.setAttribute("mensajeExito", mensaje);
                request.setAttribute("accionVolver", volver);
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