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
import es.inteco.rastreador2.actionform.observatorio.UraSendResultForm;
import es.inteco.rastreador2.dao.observatorio.UraSendResultDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class EtiquetasObservatorioAction.
 */
public class UraCustomTextObservatorioAction extends DispatchAction {
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
			Long idExObs = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = UraSendResultDAO.count(c, idExObs);
			response.setContentType("text/json");
			List<UraSendResultForm> list = UraSendResultDAO.findAll(c, idExObs, true);
			String jsonSeeds = new Gson().toJson(list);
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			pw.write("{\"uras\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", UraCustomTextObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Find.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward find(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final Long idExObs = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			String[] urasIds = null;
			if (request.getParameter("uras") != null && !StringUtils.isEmpty(request.getParameter("uras"))) {
				urasIds = request.getParameter("uras").split(",");
			}
			final int numResult = UraSendResultDAO.count(c, idExObs, urasIds);
			response.setContentType("text/json");
			List<UraSendResultForm> list = UraSendResultDAO.findByIds(c, idExObs, urasIds, true);
			String jsonSeeds = new Gson().toJson(list);
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			pw.write("{\"uras\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", UraCustomTextObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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
		UraSendResultForm range = (UraSendResultForm) form;
		try (Connection c = DataBaseManager.getConnection()) {
			UraSendResultDAO.update(c, range);
			response.getWriter().write(messageResources.getMessage("mensaje.exito.range.generada"));
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
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
				UraSendResultDAO.delete(c, Long.parseLong(id));
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.range.eliminada")));
				response.getWriter().write(new Gson().toJson(errores));
			} catch (Exception e) {
				Logger.putLog("Error: ", UraCustomTextObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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
