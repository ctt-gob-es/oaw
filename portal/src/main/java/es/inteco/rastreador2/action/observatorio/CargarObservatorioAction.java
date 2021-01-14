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

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.CargarObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.observatorio.form.ExtraConfigurationForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class CargarObservatorioAction.
 */
public class CargarObservatorioAction extends Action {
	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter(Constants.ACTION);
		if (action != null) {
			if ("update".equalsIgnoreCase(action)) {
				List<ExtraConfigurationForm> extraConfig = new ArrayList<>();
				try {
					ExtraConfigurationForm config = new ExtraConfigurationForm();
					config.setKey(request.getParameter("key"));
					config.setValue(request.getParameter("value"));
					extraConfig.add(config);
					ObservatorioDAO.saveExtraConfiguration(DataBaseManager.getConnection(), extraConfig);
					response.setStatus(200);
				} catch (Exception e) {
					Logger.putLog("Error: ", CargarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return null;
			} else if ("loadConfig".equalsIgnoreCase(action)) {
				try {
					List<ExtraConfigurationForm> listConfigs = ObservatorioDAO.loadExtraConfiguration(DataBaseManager.getConnection());
					response.setContentType("text/json");
					String jsonConfigs = new Gson().toJson(listConfigs);
					PrintWriter pw = response.getWriter();
					pw.write("{\"configs\": " + jsonConfigs.toString() + ",\"paginador\": {\"total\":" + listConfigs.size() + "}, \"paginas\": 1}");
					pw.flush();
					pw.close();
				} catch (Exception e) {
					Logger.putLog("Error: ", CargarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return null;
			}
//			else if ("saveConfig".equalsIgnoreCase(action)) {
//				String[] keys = request.getParameterValues("key");
//				String[] values = request.getParameterValues("value");
//				List<ExtraConfigurationForm> extraConfig = new ArrayList<>();
//				if (keys != null) {
//					for (int i = 0; i < keys.length; i++) {
//						ExtraConfigurationForm config = new ExtraConfigurationForm();
//						config.setKey(keys[i]);
//						config.setValue(values[i]);
//						extraConfig.add(config);
//					}
//				}
//				return null;
//			} 
			else if ("getExtraConfig".equalsIgnoreCase(action)) {
				try {
					List<ExtraConfigurationForm> listConfigs = ObservatorioDAO.getExtraConfiguration(DataBaseManager.getConnection(), request.getParameter("key"));
					response.setContentType("text/json");
					String jsonConfigs = new Gson().toJson(listConfigs);
					PrintWriter pw = response.getWriter();
					pw.write("{\"configs\": " + jsonConfigs.toString() + ",\"paginador\": {\"total\":" + listConfigs.size() + "}, \"paginas\": 1}");
					pw.flush();
					pw.close();
				} catch (Exception e) {
					Logger.putLog("Error: ", CargarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return null;
			} else {
				return mapping.findForward(Constants.ERROR_PAGE);
			}
		} else {
			// Marcamos el menú
			request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
			request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_OBSERVATORIO);
			try {
				if (CrawlerUtils.hasAccess(request, "load.observatory")) {
					Connection c = null;
					try {
						c = DataBaseManager.getConnection();
						int numResult = ObservatorioDAO.countObservatories(c);
						int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
						CargarObservatorioForm cargarObservatorioForm = ObservatorioDAO.observatoryList(c, (pagina - 1));
						request.setAttribute(Constants.CARGAR_OBSERVATORIO_FORM, cargarObservatorioForm);
						request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
					} catch (Exception e) {
						Logger.putLog("Exception: ", CargarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
						throw new Exception(e);
					} finally {
						DataBaseManager.closeConnection(c);
					}
					return mapping.findForward(Constants.EXITO);
				} else {
					return mapping.findForward(Constants.NO_PERMISSION);
				}
			} catch (Exception e) {
				CrawlerUtils.warnAdministrators(e, this.getClass());
				return mapping.findForward(Constants.ERROR_PAGE);
			}
		}
	}
}