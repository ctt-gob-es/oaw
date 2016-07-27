package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.RastreoDAO;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class EliminarObservatorioRealizadoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "delete.observatory.execution")) {
                if (request.getParameter(Constants.ES_PRIMERA) != null) {
                    return mapping.findForward(Constants.CONFIRMACION_DELETE);
                } else {
                    return deleteFulfilledObservatory(mapping, request);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }
    }

    private ActionForward deleteFulfilledObservatory(ActionMapping mapping, HttpServletRequest request) {

        Long idExecution = Long.valueOf(request.getParameter(Constants.ID));
        Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));

        Connection c = null;
        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();

            try {
                // Borramos el observatorio realizado en la base de datos
                ObservatorioDAO.deteleFulfilledObservatory(c, idExecution);
                //Borra los archivos asociados a la ejecucion del observatorio
                borrarArchivosAsociados(request, c);
                // Borramos la caché de la ejecución del observatorio
                CacheUtils.removeFromCache(Constants.OBSERVATORY_KEY_CACHE + idExecution);
            } catch (Exception e) {
                Logger.putLog("Exception: ", EliminarObservatorioRealizadoAction.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }

            String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.observatorio.realizado.eliminado");
            String volver = "";
            if (request.getParameter(Constants.IS_PRIMARY).equals(Constants.FALSE)) {
                volver = pmgr.getValue("returnPaths.properties", "volver.lista.observatorios.realizados").replace("{0}", idObservatory.toString());
            } else {
                volver = pmgr.getValue("returnPaths.properties", "volver.lista.observatorios.realizados.primarios").replace("{0}", idObservatory.toString());
            }
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

    private void borrarArchivosAsociados(HttpServletRequest request, Connection c) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        String observatoryId = request.getParameter(Constants.ID_OBSERVATORIO);
        String executionId = request.getParameter(Constants.ID);

        if ((observatoryId != null) && (executionId != null)) {

            long cartucho = ObservatorioDAO.getCartridgeFromExecutedObservatoryId(c, Long.valueOf(executionId));
            List<Long> subsequentExecutions = ObservatorioDAO.getSubsequentObservatoryExecutionIds(c, Long.valueOf(observatoryId), Long.valueOf(executionId), cartucho);

            String chartPath = "";

            if (CartuchoDAO.isCartuchoAccesibilidad(c, cartucho)) {
                chartPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.intav.files");
            } else if (cartucho == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
                chartPath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.lenox.files");
            }
            //Borramos los archivos asociados a la ejecucion del observatorio
            FileUtils.deleteDir(new File(chartPath + File.separator + request.getParameter(Constants.ID_OBSERVATORIO) + File.separator + request.getParameter(Constants.ID)));

            //Borramos los archivos asociados a las ejecuciones posteriores del observatorio
            if (subsequentExecutions.size() != 0) {
                for (Long id : subsequentExecutions) {
                    FileUtils.deleteDir(new File(chartPath + File.separator + request.getParameter(Constants.ID_OBSERVATORIO) + File.separator + id));
                }
            }

            //Borramos la carpeta del rastreo si no tiene mas ejecuciones
            File rastreoDir = new File(chartPath + File.separator + request.getParameter(Constants.ID_OBSERVATORIO));
            if (rastreoDir.isDirectory() && (rastreoDir.list().length == 0)) {
                rastreoDir.delete();
            }
        }
    }
}