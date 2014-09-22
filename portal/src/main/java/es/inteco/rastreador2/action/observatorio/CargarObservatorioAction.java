package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class CargarObservatorioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        // Marcamos el menú
        request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
        request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);

        try {

            if (CrawlerUtils.hasAccess(request, "load.observatory")) {

                Connection c = null;
                try {
                    c = DataBaseManager.getConnection();

                    int numResult = ObservatorioDAO.countObservatories(c);
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                    CargarObservatorioForm cargarObservatorioForm = ObservatorioDAO.observatoryList(c, (pagina - 1));
                    request.setAttribute(Constants.CARGAR_OBSERVATORIO_FORM, cargarObservatorioForm);
                    request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
                } catch (Exception e) {
                    Logger.putLog("Exception: ", CargarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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