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
package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.PageForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.export.database.ApiKeyDAO;
import es.inteco.rastreador2.dao.utils.export.database.HibernateUtil;
import es.inteco.rastreador2.export.database.form.ApiKeyForm;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = ApiKeyDAO.getApiKeySize(session);
			response.setContentType("text/json");
			List<ApiKeyForm> listaApiKeys = ApiKeyDAO.getApiKeyForms(session);
			String jsonApiKeys = new Gson().toJson(listaApiKeys);
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			pw.write("{\"apiKeys\": " + jsonApiKeys.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
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
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
		if (id != null) {
				ApiKeyDAO.deleteApiKey(session, Long.parseLong(id));
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
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
		ApiKeyForm apiKeyForm = (ApiKeyForm) form;
		ActionErrors errors = apiKeyForm.validate(mapping, request);
		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.etiqueta.obligatorio"));
		} else {
				if (ApiKeyDAO.existsApiKey(session, apiKeyForm.getName())) {
					response.setStatus(400);
					response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.etiqueta.duplicado"));
				} else {
					ApiKeyDAO.updateApiKey(session, apiKeyForm);
					response.getWriter().write(messageResources.getMessage("mensaje.exito.etiqueta.generada"));
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
			ApiKeyForm apiKey = new ApiKeyForm();
			apiKey.setName(name);
			apiKey.setDescription(description);
			apiKey.setType(type);
			SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        	Session session = sessionFactory.openSession();
				if (ApiKeyDAO.existsApiKey(session, name)) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.etiqueta.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					ApiKeyDAO.saveApiKey(session, apiKey);
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