package es.inteco.rastreador2.action.exportacion;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.manager.exportation.database.DatabaseExportManager;

public class ExportarEntidadesAction extends Action {
	private static final String DOWNLOAD = "download";

	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Logger.putLog("Exportar entidades: ", ExportarEntidadesAction.class, Logger.LOG_LEVEL_ERROR);
		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_OTHER_OPTIONS);
		request.getSession().setAttribute(Constants.MENU, Constants.SUBMENU_EXPORTAR);
		String sAction = request.getParameter(Constants.ACTION);
		if (isCancelled(request)) {
			return (mapping.findForward(Constants.VOLVER));
		}
		if (sAction != null && sAction.equalsIgnoreCase(DOWNLOAD)) {
			Logger.putLog("Exportar entidades: Inicialización", ExportarEntidadesAction.class, Logger.LOG_LEVEL_ERROR);
			DatabaseExportManager exportarEntidadesManager = new DatabaseExportManager();
			Logger.putLog("Exportar entidades: Generando backup", ExportarEntidadesAction.class, Logger.LOG_LEVEL_ERROR);
			String result = exportarEntidadesManager.backup();
			Logger.putLog("Exportar entidades: Transformando resultado", ExportarEntidadesAction.class, Logger.LOG_LEVEL_ERROR);
			byte[] byteArray = result.getBytes(StandardCharsets.UTF_8);
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;filename=backup.json");
			response.getOutputStream().write(byteArray);
			response.flushBuffer();
			Logger.putLog("Exportar entidades: Devolviendo resultado", ExportarEntidadesAction.class, Logger.LOG_LEVEL_ERROR);
			return null;
		}
		return mapping.findForward(Constants.EXPORTAR_ENTIDADES);
	}
}
