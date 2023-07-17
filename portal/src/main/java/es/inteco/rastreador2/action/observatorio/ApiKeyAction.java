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
import es.inteco.rastreador2.actionform.semillas.PlantillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.export.database.ApiKeyDAO;
import es.inteco.rastreador2.dao.plantilla.PlantillaDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.dao.utils.export.database.HibernateUtil;
import es.inteco.rastreador2.export.database.form.ApiKeyForm;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.google.gson.Gson;
import com.google.protobuf.Api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.sql.Connection;
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
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = ApiKeyDAO.getApiKeySize(session);
			response.setContentType("text/json");
			List<ApiKeyForm> listaApiKeys = ApiKeyDAO.getApiKeyForms(session);
			String jsonSeeds = new Gson().toJson(listaApiKeys);
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			pw.write("{\"apiKeys\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", ApiKeyAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

}