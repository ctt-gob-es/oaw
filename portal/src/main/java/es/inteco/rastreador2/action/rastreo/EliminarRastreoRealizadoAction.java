package es.inteco.rastreador2.action.rastreo;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.EliminarRastreosRealizadosForm;
import es.inteco.rastreador2.actionform.rastreo.RastreoEjecutadoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.RastreoUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class EliminarRastreoRealizadoAction extends Action {


    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {

        try {
            EliminarRastreosRealizadosForm rastreosSeleccionados = (EliminarRastreosRealizadosForm) form;
            ActionErrors errors = rastreosSeleccionados.validate(mapping, request);

            if (CrawlerUtils.hasAccess(request, "load.crawlers")) {
                Connection c = null;
                try {
                    PropertiesManager pmgr = new PropertiesManager();
                    c = DataBaseManager.getConnection();
                    if (request.getParameter(Constants.CONFIRMACION) == null) {
                        if (errors == null || errors.isEmpty()) {
                            List<RastreoEjecutadoForm> rastreoFormList = new ArrayList<>();
                            for (int i = 0; i < rastreosSeleccionados.getSelect().length; i++) {
                                rastreoFormList.add(RastreoDAO.cargarRastreoEjecutado(c, Long.parseLong(rastreosSeleccionados.getSelect()[i])));
                            }
                            request.setAttribute(Constants.RASTREO_LIST_FORM, rastreoFormList);
                            request.setAttribute(Constants.ID, request.getParameter(Constants.ID));
                            request.setAttribute(Constants.ID_RASTREO, request.getParameter(Constants.ID_RASTREO));
                            return mapping.findForward(Constants.CONFIRMACION_DELETE);
                        } else {
                            ActionForward forward = new ActionForward();
                            String path = mapping.getInput() + "?" + Constants.ID_RASTREO + "=" + request.getParameter(Constants.ID_RASTREO);
                            if (request.getParameter(Constants.PAG_PARAM) != null) {
                                path += "&" + Constants.PAG_PARAM + "=" + request.getParameter(Constants.PAG_PARAM);
                            }
                            forward.setPath(path);
                            forward.setRedirect(true);
                            saveErrors(request.getSession(), errors);
                            return (forward);
                        }
                    } else {
                        if (isCancelled(request)) {
                            return mapping.findForward(Constants.VOLVER);
                        } else {
                            for (int i = 0; i < rastreosSeleccionados.getSelect().length; i++) {
                                RastreoUtils.borrarArchivosAsociados(request, c, rastreosSeleccionados.getSelect()[i]);
                                RastreoDAO.borrarRastreoRealizado(c, Long.parseLong((rastreosSeleccionados.getSelect()[i])));
                            }
                            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.rastreo.realizado.eliminado");
                            String volver = pmgr.getValue("returnPaths.properties", "volver.carga.rastreo.realizado").replace("{0}", String.valueOf(request.getParameter(Constants.ID_RASTREO)));
                            request.setAttribute("mensajeExito", mensaje);
                            request.setAttribute("accionVolver", volver);
                            return mapping.findForward(Constants.EXITO);
                        }
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", EliminarRastreoRealizadoAction.class, Logger.LOG_LEVEL_ERROR, e);
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