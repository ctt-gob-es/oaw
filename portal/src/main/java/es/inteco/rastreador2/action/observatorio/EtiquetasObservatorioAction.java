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
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class EtiquetasObservatorioAction.
 */
public class EtiquetasObservatorioAction extends DispatchAction {

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
	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
		if (request.getParameter(Constants.RETURN_OBSERVATORY_RESULTS) != null) {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);
		} else {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBS_ETIQUETAS);
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
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try (Connection c = DataBaseManager.getConnection()) {

			String nombre = request.getParameter("nombre");

			if (!StringUtils.isEmpty(nombre)) {

				nombre = es.inteco.common.utils.StringUtils.corregirEncoding(nombre);
			}

			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

			final int numResult = EtiquetaDAO.countTags(c, nombre);

			response.setContentType("text/json");

			List<EtiquetaForm> listaEtiquetas = EtiquetaDAO.getTags(c);

			String jsonSeeds = new Gson().toJson(listaEtiquetas);

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
			Logger.putLog("Error: ", EtiquetasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		EtiquetaForm etiqueta = (EtiquetaForm) form;

		ActionErrors errors = etiqueta.validate(mapping, request);

		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.etiqueta.obligatorio"));
		} else {

			try (Connection c = DataBaseManager.getConnection()) {

				if (EtiquetaDAO.existsTag(c,etiqueta.getName())
						&& !etiqueta.getName().equalsIgnoreCase(etiqueta.getNombreAntiguo())) {
					response.setStatus(400);
					response.getWriter()
							.write(messageResources.getMessage("mensaje.error.nombre.etiqueta.duplicado"));

				} else {
					String clasificacion = request.getParameter("clasificacionaux");
					ClasificacionForm clasificacionEtiqueta = new ClasificacionForm();
					clasificacionEtiqueta.setId(clasificacion);
					etiqueta.setClasificacion(clasificacionEtiqueta);
					
					EtiquetaDAO.updateTag(c, etiqueta);
					response.getWriter().write(messageResources.getMessage("mensaje.exito.etiqueta.generada"));
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
		String clasificacion = request.getParameter("clasificacionaux");


		if (StringUtils.isNotEmpty(nombre) && (StringUtils.isNotEmpty(clasificacion))) {
			
			EtiquetaForm etiqueta = new EtiquetaForm();
			etiqueta.setName(nombre);
			
			ClasificacionForm clasificacionEtiqueta = new ClasificacionForm();
			clasificacionEtiqueta.setId(clasificacion);
			etiqueta.setClasificacion(clasificacionEtiqueta);
			
			
			try (Connection c = DataBaseManager.getConnection()) {

				if (EtiquetaDAO.existsTag(c, etiqueta.getName())) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.etiqueta.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					EtiquetaDAO.saveTag(c, etiqueta);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.etiqueta.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}

			} catch (Exception e) {
				Logger.putLog("Error: ", EtiquetasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
			
		} else {
			response.setStatus(400);
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.etiqueta.obligatorio")));
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
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		List<JsonMessage> errores = new ArrayList<>();

		String id = request.getParameter("idEtiqueta");

		if (id != null) {

			try (Connection c = DataBaseManager.getConnection()) {

				EtiquetaDAO.deleteTag(c, Integer.parseInt(id));
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.etiqueta.eliminada")));
				response.getWriter().write(new Gson().toJson(errores));

			} catch (Exception e) {
				Logger.putLog("Error: ", EtiquetasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
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
 * Obtiene un listado de todas las clasficaciones. La respuesta se genera como
 * un JSON
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
public ActionForward listClasificaciones(ActionMapping mapping, ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
	try (Connection c = DataBaseManager.getConnection()) {

		List<ClasificacionForm> listClasificaciones = EtiquetaDAO.getTagClassifications(c, Constants.NO_PAGINACION);

		String jsonClasificaciones = new Gson().toJson(listClasificaciones);

		PrintWriter pw = response.getWriter();
		pw.write(jsonClasificaciones);
		pw.flush();
		pw.close();

	} catch (Exception e) {
		Logger.putLog("Error: ", EtiquetasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
	}
	
	return null;
}

}
