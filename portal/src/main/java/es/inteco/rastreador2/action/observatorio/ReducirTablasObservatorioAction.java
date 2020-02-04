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
import es.inteco.rastreador2.actionform.etiquetas.ClasificacionForm;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ReducirTablasForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.dao.reducirtablas.ReducirTablasDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class EtiquetasObservatorioAction.
 */
public class ReducirTablasObservatorioAction extends DispatchAction {
	
	/**
	 * Remove tables.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward removeTables(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		List<JsonMessage> errores = new ArrayList<>();

		String idExObs = request.getParameter("idExObs");

		if (idExObs != null) {

			try (Connection c = DataBaseManager.getConnection()) {

				ReducirTablasDAO.eliminarTablas(c, Integer.parseInt(idExObs));
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.tabla.reducida")));
				response.getWriter().write(new Gson().toJson(errores));

			} catch (Exception e) {
				Logger.putLog("Error: ", ReducirTablasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * Load. Carga de la página.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	/*public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
		if (request.getParameter(Constants.RETURN_OBSERVATORY_RESULTS) != null) {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);
		} else {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBS_REDUCIRTABLAS);
		}

		return mapping.findForward(Constants.EXITO);
	}*/

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
	/*public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		try (Connection c = DataBaseManager.getConnection()) {

			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

			final int numResult = ReducirTablasDAO.countTablas(c);

			response.setContentType("text/json");

			List<ReducirTablasForm> listaReducirTablas = ReducirTablasDAO.getTablas(c);

			String jsonSeeds = new Gson().toJson(listaReducirTablas);

			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);

			String jsonPagination = new Gson().toJson(paginas);

			PrintWriter pw = response.getWriter();
			// pw.write(json);
			pw.write("{\"etiquetas\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult
					+ "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", ReducirTablasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return null;
	}*/

	/**
	 * Reduce.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	/*public ActionForward reduce(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		List<JsonMessage> errores = new ArrayList<>();

		String name = request.getParameter("nameTabla");

		if (name != null) {

			try (Connection c = DataBaseManager.getConnection()) {

				ReducirTablasDAO.reduceTabla(c, name);
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.tabla.reducida")));
				response.getWriter().write(new Gson().toJson(errores));

			} catch (Exception e) {
				Logger.putLog("Error: ", ReducirTablasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}

		} else {
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
		}

		return null;
	}*/

}
