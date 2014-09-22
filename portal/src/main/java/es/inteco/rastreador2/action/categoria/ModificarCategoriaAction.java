package es.inteco.rastreador2.action.categoria;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.categoria.VerCategoriaForm;
import es.inteco.rastreador2.dao.categorias.CategoriasDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class ModificarCategoriaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Connection c = null;
        try {
            if (CrawlerUtils.hasAccess(request, "edit.category")) {
                VerCategoriaForm verCategoriaForm = (VerCategoriaForm) form;

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                String id_categoria = request.getParameter(Constants.ID_CATEGORIA);
                verCategoriaForm.setId_categoria(Integer.parseInt(id_categoria));
                PropertiesManager pmgr = new PropertiesManager();

                c = DataBaseManager.getConnection();

                int numResult = CategoriasDAO.countTerms(c, Integer.parseInt(id_categoria));
                int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
                verCategoriaForm.setVectorTerminos(CategoriasDAO.loadCategoryTerms(c, verCategoriaForm.getId_categoria(), (pagina - 1)));
                request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));

                //comprobamos si se viene de confirmar o no
                String confeli = request.getParameter(Constants.ACTION);
                if (confeli == null || !confeli.equals(Constants.ACCION_MODIFICAR)) {
                    //no se viene de modificar, asique mostramos el form pa modificar pillamos el idcategoria
                    CategoriasDAO.getCategory(c, verCategoriaForm);
                    return mapping.findForward(Constants.VOLVER);

                } else {
                    verCategoriaForm.setNombre_antiguo(request.getParameter(Constants.NOMBRE_ANTIGUO));
                    ActionErrors errors = verCategoriaForm.validate(mapping, request);

                    if (errors == null || errors.isEmpty()) {
                        CategoriasDAO.updateCategory(c, verCategoriaForm);
                        String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.editada");
                        String volver = pmgr.getValue("returnPaths.properties", "volver.cargar.categorias");
                        request.setAttribute("mensajeExito", mensaje);
                        request.setAttribute("accionVolver", volver);
                        return mapping.findForward(Constants.EXITO);
                    } else {
                        ActionForward forward = new ActionForward();
                        forward.setPath(mapping.getInput() + "?" + Constants.ID_CATEGORIA + "=" + id_categoria);
                        forward.setRedirect(true);
                        saveErrors(request.getSession(), errors);
                        return (forward);
                    }
                }
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