package es.inteco.rastreador2.action.importacion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.rastreador2.actionform.importacion.SSPImportarForm;
import es.inteco.rastreador2.manager.importacion.database.DatabaseImportarManager;

public class SSPImportarAction extends Action {
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
		request.getSession().setAttribute(Constants.MENU, Constants.SUBMENU_IMPORTAR);
		SSPImportarForm importarEntidadForm = (SSPImportarForm) form;
		String sAction = request.getParameter(Constants.ACTION);
		if (sAction != null && sAction.equalsIgnoreCase("upload")) {
			ActionErrors errors = importarEntidadForm.validate(mapping, request);
			if (errors.isEmpty()) {
				DatabaseImportarManager importarEntidadManager = new DatabaseImportarManager();
				String filePath = getServlet().getServletContext().getRealPath("/");
				boolean uploadExito = importarEntidadManager.importData(importarEntidadForm.getFile());
				if (uploadExito) {
					return mapping.findForward(Constants.EXITO);
				} else {
					return mapping.findForward(Constants.ERROR);
				}
			} else
				return mapping.findForward(Constants.ERROR);
		}
		return mapping.findForward(Constants.ERROR);
	}
}
