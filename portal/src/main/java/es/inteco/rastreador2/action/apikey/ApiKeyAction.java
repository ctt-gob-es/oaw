/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.action.apikey;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.intav.form.PageForm;
import es.inteco.rastreador2.dao.apikey.ApiKey;
import es.inteco.rastreador2.export.database.form.ApiKeyForm;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.manager.ApiKeyManager;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class ListadoSemillaAction.
 */
public class ApiKeyAction extends DispatchAction {
	public ActionForward load(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
		if (request.getParameter(Constants.RETURN_OBSERVATORY_RESULTS) != null) {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);
		} else {
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_LISTADO_APIKEYS);
		}
		return mapping.findForward(Constants.EXITO);
	}

	/**
	 * list.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
		final int numResult = ApiKeyManager.getNumApiKeys();
		response.setContentType("text/json");
		List<ApiKeyForm> listaApiKeys = ApiKeyManager.getApiKeys();
		String jsonSeeds = new Gson().toJson(listaApiKeys);
		// Paginacion
		List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
		String jsonPagination = new Gson().toJson(paginas);
		PrintWriter pw = response.getWriter();
		pw.write("{\"apiKeys\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
		pw.flush();
		pw.close();
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
			ApiKey apiKey = new ApiKey();
			apiKey.setId(new Long(id));
			ApiKeyManager.delete(apiKey);
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.etiqueta.eliminada")));
			response.getWriter().write(new Gson().toJson(errores));
		} else {
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
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
		ApiKeyForm apiKeyForm = (ApiKeyForm) form;
		ActionErrors errors = apiKeyForm.validate(mapping, request);
		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.etiqueta.obligatorio"));
		} else {
			ApiKey apiKey = new ApiKey();
			BeanUtils.copyProperties(apiKey, apiKeyForm);
			if (ApiKeyManager.existsApiKey(apiKey.getName())) {
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.etiqueta.duplicado"));
			} else {
				ApiKeyManager.update(apiKey);
				response.getWriter().write(messageResources.getMessage("mensaje.exito.apikey.generada"));
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
		String name = request.getParameter("nombre");
		String description = request.getParameter("descripcion");
		String type = request.getParameter("tipo");
		if (StringUtils.isNotEmpty(name) && (StringUtils.isNotEmpty(type))) {
			ApiKey apiKey = new ApiKey();
			apiKey.setApiKey(ApiKeyManager.generateApiKey());
			apiKey.setName(name);
			apiKey.setDescription(description);
			apiKey.setType(type);
			if (ApiKeyManager.existsApiKey(apiKey.getName())) {
				response.setStatus(400);
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.etiqueta.duplicado")));
				response.getWriter().write(new Gson().toJson(errores));
			} else {
				ApiKeyManager.save(apiKey);
				errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.etiqueta.generada")));
				response.getWriter().write(new Gson().toJson(errores));
			}
		} else {
			response.setStatus(400);
			errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.etiqueta.obligatorio")));
			response.getWriter().write(new Gson().toJson(errores));
		}
		return null;
	}
}