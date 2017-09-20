package es.inteco.rastreador2.action.observatorio;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.PageForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.dao.dependencia.DependenciaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class DependenciasObservatorioAction.
 */
public class DependenciasObservatorioAction extends DispatchAction {

	/**
	 * Load. Carga de la página.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU);
		if (request.getParameter(Constants.RETURN_OBSERVATORY_RESULTS) != null) {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);
		} else {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBS_DEPENDENCIAS);
		}

		return mapping.findForward(Constants.EXITO);
	}

	/**
	 * Search. Devuelve un JSON con los resultados de la búsqueda
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		try (Connection c = DataBaseManager.getConnection()) {

			String nombre = request.getParameter("nombre");

			if (!StringUtils.isEmpty(nombre)) {

				nombre = es.inteco.common.utils.StringUtils.corregirEncoding(nombre);
			}

			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

			final int numResult = DependenciaDAO.countDependencias(c, nombre);

			response.setContentType("text/json");

			List<DependenciaForm> listaDependencias = DependenciaDAO.getDependencias(c, nombre, (pagina - 1));

			String jsonSeeds = new Gson().toJson(listaDependencias);

			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);

			String jsonPagination = new Gson().toJson(paginas);

			PrintWriter pw = response.getWriter();
			// pw.write(json);
			pw.write("{\"dependencias\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return null;
	}

	/**
	 * Update.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		DependenciaForm dependencia = (DependenciaForm) form;

		ActionErrors errors = dependencia.validate(mapping, request);

		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.dependencia.obligatorio"));
		} else {

			try (Connection c = DataBaseManager.getConnection()) {

				if (DependenciaDAO.existsDependencia(c, dependencia) && !dependencia.getName().equalsIgnoreCase(dependencia.getNombreAntiguo())) {
					response.setStatus(400);
					response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.dependencia.duplicado"));

				} else {
					DependenciaDAO.update(c, dependencia);
					response.getWriter().write(messageResources.getMessage("mensaje.exito.dependencia.generada"));
				}

			} catch (Exception e) {
				Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}

		}
		return null;
	}

	/**
	 * Save.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		List<JsonMessage> errores = new ArrayList<>();

		String nombre = request.getParameter("nombre");

		if (StringUtils.isNotEmpty(nombre)) {

			DependenciaForm dependencia = new DependenciaForm();
			dependencia.setName(nombre);

			try (Connection c = DataBaseManager.getConnection()) {

				if (DependenciaDAO.existsDependencia(c, dependencia)) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.dependencia.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					DependenciaDAO.save(c, dependencia);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.dependencia.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}

			} catch (Exception e) {
				Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		} else {
			response.setStatus(400);
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.dependencia.obligatorio")));
			response.getWriter().write(new Gson().toJson(errores));
		}
		return null;
	}

	/**
	 * Delete.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		List<JsonMessage> errores = new ArrayList<>();

		String id = request.getParameter("idDependencia");

		if (id != null) {

			try (Connection c = DataBaseManager.getConnection()) {

				DependenciaDAO.delete(c, Long.parseLong(id));
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.dependencia.eliminada")));
				response.getWriter().write(new Gson().toJson(errores));

			} catch (Exception e) {
				Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}

		} else {
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
		}

		return null;
	}

}
