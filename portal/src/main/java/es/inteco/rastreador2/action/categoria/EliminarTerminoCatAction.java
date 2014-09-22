package es.inteco.rastreador2.action.categoria;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.categoria.NuevoTerminoCatForm;
import es.inteco.rastreador2.dao.categorias.CategoriasDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;

public class EliminarTerminoCatAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "delete.term")) {

                NuevoTerminoCatForm nuevoTerminoCatForm = (NuevoTerminoCatForm) form;

                HttpSession sesion = request.getSession();

                if (isCancelled(request)) {
                    String idcat = request.getParameter("idcat");
                    ActionForward oldForward = mapping.findForward(Constants.VOLVER_CARGA);
                    ActionForward newForward = new ActionForward();
                    newForward.setPath(oldForward.getPath() + "?idcat=" + idcat);
                    newForward.setRedirect(true);
                    return (newForward);
                }

                //recojemos el idcat y el idter
                String id_categoria = request.getParameter(Constants.ID_CATEGORIA);
                nuevoTerminoCatForm.setId_categoria(Integer.parseInt(id_categoria));
                String id_termino = request.getParameter(Constants.ID_TERMINO);
                nuevoTerminoCatForm.setId_termino(Integer.parseInt(id_termino));
                PropertiesManager pmgr = new PropertiesManager();

                Connection c = DataBaseManager.getConnection();
                try {
                    nuevoTerminoCatForm = CategoriasDAO.getTermDates(c, nuevoTerminoCatForm);
                    sesion.setAttribute("idcatTerminoCat", Integer.toString(nuevoTerminoCatForm.getId_categoria()));

                    //comprobamos de donde viene
                    String accionmod = request.getParameter(Constants.ACCION);
                    if (accionmod == null || accionmod.trim().isEmpty()) {
                        //viene de inicio hay que cargar datos ya estan cargados
                        return mapping.findForward(Constants.VOLVER);
                    }

                    CategoriasDAO.deleteTerm(c, nuevoTerminoCatForm);

                    String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.termino.categoria.eliminado");
                    String volver = pmgr.getValue("returnPaths.properties", "volver.modificar.categoria").replace("{0}", id_categoria);
                    request.setAttribute("mensajeExito", mensaje);
                    request.setAttribute("accionVolver", volver);
                    return mapping.findForward(Constants.EXITO);
                } catch (Exception e) {
                    Logger.putLog("Exception: ", EliminarTerminoCatAction.class, Logger.LOG_LEVEL_ERROR, e);
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