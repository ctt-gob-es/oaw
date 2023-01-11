package es.inteco.rastreador2.action.exportacion;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
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
		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_OTHER_OPTIONS);
		request.getSession().setAttribute(Constants.MENU, Constants.SUBMENU_EXPORTAR);
		String sAction = request.getParameter(Constants.ACTION);
		if (isCancelled(request)) {
			return (mapping.findForward(Constants.VOLVER));
		}
		if (sAction != null && sAction.equalsIgnoreCase(DOWNLOAD)) {
			DatabaseExportManager exportarEntidadesManager = new DatabaseExportManager();
			String result = exportarEntidadesManager.backup();
			byte[] byteArrray = result.getBytes(StandardCharsets.UTF_8);
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;filename=backup.json");
			response.getOutputStream().write(byteArrray);
			response.flushBuffer();
			// return mapping.findForward(Constants.EXITO);
		}
		return mapping.findForward(Constants.EXPORTAR_ENTIDADES);
	}
}
