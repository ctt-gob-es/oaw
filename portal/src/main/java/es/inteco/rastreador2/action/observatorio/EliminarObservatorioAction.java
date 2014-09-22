package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class EliminarObservatorioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        try {
            if (CrawlerUtils.hasAccess(request, "delete.observatory")) {
                String id_observatorio = request.getParameter(Constants.ID_OBSERVATORIO);
                if (request.getParameter(Constants.ES_PRIMERA) != null) {
                    return confirm(mapping, form, request, response);
                } else {
                    if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_SI)) {
                        return deleteObservatory(mapping, form, request, response, Long.parseLong(id_observatorio));
                    } else {
                        return mapping.findForward(Constants.VOLVER);
                    }
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
            request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
        } catch (Exception e) {
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.CONFIRMACION_DELETE);
    }

    private ActionForward deleteObservatory(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response, long id_observatorio) {

        Connection c = null;
        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();

            try {
                ObservatorioDAO.deteleObservatory(c, id_observatorio);
            } catch (Exception e) {
                Logger.putLog("Exception: ", EliminarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }

            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.observatorio.eliminado");
            String volver = pmgr.getValue("returnPaths.properties", "volver.carga.observatorio");
            request.setAttribute("mensajeExito", mensaje);
            request.setAttribute("accionVolver", volver);
            return mapping.findForward(Constants.EXITO);
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }
}