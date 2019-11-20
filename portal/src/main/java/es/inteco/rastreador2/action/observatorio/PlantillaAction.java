package es.inteco.rastreador2.action.observatorio;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
import es.inteco.rastreador2.actionform.semillas.PlantillaForm;
import es.inteco.rastreador2.dao.plantilla.PlantillaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class PlantillaAction.
 */
public class PlantillaAction extends DispatchAction {
	/**
	 * Load.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
		if (request.getParameter(Constants.RETURN_OBSERVATORY_RESULTS) != null) {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);
		} else {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBS_PLANTILLAS);
		}
		return mapping.findForward(Constants.EXITO);
	}

	/**
	 * Search.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = PlantillaDAO.count(c);
			response.setContentType("text/json");
			List<PlantillaForm> listaDependencias = PlantillaDAO.findAll(c, (pagina - 1));
			String jsonSeeds = new Gson().toJson(listaDependencias);
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			pw.write("{\"plantillas\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Save.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		List<JsonMessage> errores = new ArrayList<>();
		PlantillaForm plantilla = (PlantillaForm) form;
		if (!StringUtils.isNotEmpty(plantilla.getNombre())) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.plantilla.obligatorio")));
		}
		if (plantilla.getFile() == null || StringUtils.isEmpty(plantilla.getFile().getFileName())) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.documento.plantilla.obligatorio")));
		}
		if (!plantilla.getFile().getFileName().endsWith(".odt")) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.documento.plantilla.formato")));
		}
		if (errores.size() == 0) {
			try (Connection c = DataBaseManager.getConnection()) {
				if (PlantillaDAO.existsPlantilla(c, plantilla)) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.plantilla.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					plantilla.setDocumento(plantilla.getFile().getFileData());
					PlantillaDAO.save(c, plantilla);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.plantilla.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", PlantillaAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		} else {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		}
		return null;
	}

	/**
	 * Update.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		List<JsonMessage> errores = new ArrayList<>();
		PlantillaForm plantilla = (PlantillaForm) form;
		if (StringUtils.isNotEmpty(plantilla.getNombre())) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.plantilla.obligatorio")));
		}
		if (plantilla.getDocumento() == null || plantilla.getDocumento().length == 0) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.documento.plantilla.obligatorio")));
		}
		if (errores.size() == 0) {
			try (Connection c = DataBaseManager.getConnection()) {
				if (PlantillaDAO.existsPlantilla(c, plantilla)) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.plantilla.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					PlantillaDAO.update(c, plantilla);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.plantilla.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", PlantillaAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		} else {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		}
		return null;
	}

	/**
	 * Delete.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		List<JsonMessage> errores = new ArrayList<>();
		String id = request.getParameter("idPlantilla");
		if (id != null) {
			try (Connection c = DataBaseManager.getConnection()) {
				PlantillaDAO.delete(c, Long.parseLong(id));
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

	/**
	 * Download.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward download(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		String id = request.getParameter("idPlantilla");
		if (id != null) {
			try (Connection c = DataBaseManager.getConnection()) {
				PlantillaForm plantilla = PlantillaDAO.findById(c, Long.parseLong(id));
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "filename=\"" + plantilla.getNombre() + ".odt\"");
				response.setContentLength(plantilla.getDocumento().length);
				OutputStream os = response.getOutputStream();
				os.write(plantilla.getDocumento(), 0, plantilla.getDocumento().length);
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
