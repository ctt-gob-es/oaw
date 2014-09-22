package es.inteco.rastreador2.action.categoria;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.categoria.VerCategoriaForm;
import es.inteco.rastreador2.dao.categorias.CategoriasDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import es.inteco.rastreador2.utils.TerminoCatVer;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

public class VerCategoriaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "view.category")) {
                VerCategoriaForm verCategoriaForm = (VerCategoriaForm) form;

                //pillamos el idcategoria
                String id_categoria = request.getParameter(Constants.ID_CATEGORIA);
                verCategoriaForm.setId_categoria(Integer.parseInt(id_categoria));
                Connection c = DataBaseManager.getConnection();
                try {
                    verCategoriaForm = CategoriasDAO.getCategory(c, verCategoriaForm);
                    int numResult = CategoriasDAO.countTerms(c, Integer.parseInt(id_categoria));
                    int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                    List<TerminoCatVer> vectorTerminos = CategoriasDAO.loadCategoryTerms(c, Integer.parseInt(id_categoria), (pagina - 1));
                    request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));

                    verCategoriaForm.setVectorTerminos(vectorTerminos);
                } catch (Exception e) {
                    Logger.putLog("Exception: ", VerCategoriaAction.class, Logger.LOG_LEVEL_ERROR, e);
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
