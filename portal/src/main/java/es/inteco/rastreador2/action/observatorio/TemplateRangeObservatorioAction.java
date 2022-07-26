/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.inteco.rastreador2.action.observatorio;

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
import es.inteco.rastreador2.actionform.observatorio.TemplateRangeForm;
import es.inteco.rastreador2.dao.observatorio.TemplateRangeDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class EtiquetasObservatorioAction.
 */
public class TemplateRangeObservatorioAction extends DispatchAction {
	private static double RANGE_MIN_VALUE = -99.99;
	private static double RANGE_MAX_VALUE = 99.99;

	/**
	 * Load. Carga de la página.
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
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBS_RANGES);
		}
		return mapping.findForward(Constants.EXITO);
	}

	/**
	 * Search. Devuelve un JSON con los resultados de la búsqueda
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward all(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			String nombre = request.getParameter("nombre");
			Long idExObs = Long.parseLong(request.getParameter("idExObs"));
			if (!StringUtils.isEmpty(nombre)) {
				nombre = es.inteco.common.utils.StringUtils.corregirEncoding(nombre);
			}
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = TemplateRangeDAO.count(c, nombre, idExObs);
			response.setContentType("text/json");
			List<TemplateRangeForm> listaEtiquetas = TemplateRangeDAO.findAll(c, idExObs);
			String jsonSeeds = new Gson().toJson(listaEtiquetas);
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			// pw.write(json);
			pw.write("{\"templates\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", TemplateRangeObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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
		TemplateRangeForm range = (TemplateRangeForm) form;
		validateNewRange(errores, range);
		if (errores != null && errores.size() > 0) {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		} else {
			try (Connection c = DataBaseManager.getConnection()) {
				TemplateRangeDAO.update(c, range);
				response.getWriter().write(messageResources.getMessage("mensaje.exito.range.generada"));
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
		TemplateRangeForm range = (TemplateRangeForm) form;
		validateNewRange(errores, range);
		if (errores != null && errores.size() > 0) {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		} else {
			try (Connection c = DataBaseManager.getConnection()) {
				if (TemplateRangeDAO.exists(c, range)) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.range.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					TemplateRangeDAO.save(c, range);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.range.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", TemplateRangeObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
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
		String id = request.getParameter("id");
		if (id != null) {
			try (Connection c = DataBaseManager.getConnection()) {
				TemplateRangeDAO.delete(c, Long.parseLong(id));
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.range.eliminada")));
				response.getWriter().write(new Gson().toJson(errores));
			} catch (Exception e) {
				Logger.putLog("Error: ", TemplateRangeObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * Validate new range
	 * 
	 * @param errores List of errors
	 * @param range   Range
	 */
	private void validateNewRange(List<JsonMessage> errores, TemplateRangeForm range) {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		if (range == null) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.generico")));
		}
		if (StringUtils.isEmpty(range.getName())) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.range.obligatorio")));
		}
		if (range.getMinValue() == range.getMaxValue() || (range.getMinValue() > RANGE_MAX_VALUE) || (range.getMinValue() < RANGE_MIN_VALUE) || (range.getMaxValue() > RANGE_MAX_VALUE)
				|| (range.getMaxValue() < RANGE_MIN_VALUE) || Float.isInfinite(range.getMinValue()) || Float.isInfinite(range.getMaxValue())) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.range.incorrecto")));
		}
		if (StringUtils.isEmpty(range.getTemplate())) {
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.plantilla.obligatorio")));
		}
	}
}
