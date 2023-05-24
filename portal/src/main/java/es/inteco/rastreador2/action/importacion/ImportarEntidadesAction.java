package es.inteco.rastreador2.action.importacion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.rastreador2.actionform.importation.ImportEntitiesForm;
import es.inteco.rastreador2.actionform.importation.ImportEntitiesResultForm;
import es.inteco.rastreador2.manager.importation.database.DatabaseImportManager;

public class ImportarEntidadesAction extends Action {
	private static final String UPLOAD = "upload";
	private static final String RESULTS = "results";

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
		ImportEntitiesForm importarEntidadesForm = (ImportEntitiesForm) form;
		String sAction = request.getParameter(Constants.ACTION);
		if (isCancelled(request)) {
			return (mapping.findForward(Constants.VOLVER));
		}
		if (sAction != null && sAction.equalsIgnoreCase(UPLOAD)) {
			ActionErrors errors = importarEntidadesForm.validate(mapping, request);
			if (errors.isEmpty()) {
				DatabaseImportManager importarEntidadesManager = new DatabaseImportManager();
				ImportEntitiesResultForm importarEntidadesResultado = importarEntidadesManager.importData(importarEntidadesForm.getFile());
				request.setAttribute(RESULTS, importarEntidadesResultado);
				if (importarEntidadesResultado.isValidImport()) {
					return mapping.findForward(Constants.EXITO);
				} else {
					return mapping.findForward(Constants.ERROR);
				}
			} else
				return mapping.findForward(Constants.ERROR);
		}
		return mapping.findForward(Constants.IMPORTAR_ENTIDADES);
	}
}
