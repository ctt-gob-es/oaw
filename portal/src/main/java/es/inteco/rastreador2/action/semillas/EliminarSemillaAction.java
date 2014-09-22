package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class EliminarSemillaAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Connection c = null;
        try {
            if (CrawlerUtils.hasAccess(request, "delete.seed")) {
                PropertiesManager pmgr = new PropertiesManager();
                c = DataBaseManager.getConnection();

                if (isCancelled(request)) {
                    return (mapping.findForward(Constants.VOLVER_CARGA));
                }

                String idSemilla = "";
                if (request.getParameter(Constants.SEGUNDA) == null) {
                    if (request.getParameter(Constants.SEMILLA) != null) {
                        idSemilla = request.getParameter(Constants.SEMILLA);
                        SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
                        request.setAttribute(Constants.SEMILLA_FORM, semillaForm);
                    }
                    return mapping.findForward(Constants.EXITO);
                } else {
                    if (request.getParameter(Constants.SEMILLA) != null) {
                        idSemilla = request.getParameter(Constants.SEMILLA);
                    }


                    SemillaDAO.deleteSeed(c, Long.parseLong(idSemilla));
                    String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.semilla.borrada");
                    request.setAttribute("mensajeExito", mensaje);
                    String volver = pmgr.getValue("returnPaths.properties", "volver.listado.semillas");
                    request.setAttribute("accionVolver", volver);
                    return mapping.findForward(Constants.SEMILLA_ELIMINADA);
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