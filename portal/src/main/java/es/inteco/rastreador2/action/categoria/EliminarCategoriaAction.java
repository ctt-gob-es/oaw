package es.inteco.rastreador2.action.categoria;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.categoria.VerCategoriaForm;
import es.inteco.rastreador2.dao.categorias.CategoriasDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class EliminarCategoriaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "delete.category")) {
                VerCategoriaForm verCategoriaForm = (VerCategoriaForm) form;

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                String id_categoria = request.getParameter(Constants.ID_CATEGORIA);
                PropertiesManager pmgr = new PropertiesManager();

                Connection c = DataBaseManager.getConnection();

                try {
                    //comprobamos si se viene de confirmar o no
                    String confeli = request.getParameter(Constants.CONFIRMACION);
                    if (confeli == null || !confeli.equals(Constants.CONFIRMACION_SI)) {
                        //no se viene de confirmacion, asique mostramos la ventana de confirmacion pillamos el idcategoria
                        verCategoriaForm = CategoriasDAO.showCategories(c, verCategoriaForm, id_categoria);
                        request.setAttribute("verCategoriaForm", verCategoriaForm);
                        return mapping.findForward(Constants.EXITO_CONFIRMACION);

                    } else {
                        CategoriasDAO.deleteCategory(c, id_categoria);
                        String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.eliminada");
                        request.setAttribute("mensajeExito", mensaje);
                        String volver = pmgr.getValue("returnPaths.properties", "volver.cargar.categorias");
                        request.setAttribute("accionVolver", volver);
                        return mapping.findForward(Constants.EXITO);
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", EliminarCategoriaAction.class, Logger.LOG_LEVEL_ERROR, e);
                    throw new Exception(e);
                } finally {
                    DataBaseManager.closeConnection(c);
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