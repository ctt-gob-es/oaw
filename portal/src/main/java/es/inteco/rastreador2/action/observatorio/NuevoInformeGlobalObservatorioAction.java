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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.plantilla.PlantillaDAO;
import es.inteco.rastreador2.openOffice.export.ExportOpenOfficeUtils;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.rastreador2.utils.CrawlerUtils;

/**
 * The Class NuevoObservatorioAction.
 */
public class NuevoInformeGlobalObservatorioAction extends Action {
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
		try {
			if (CrawlerUtils.hasAccess(request, "new.observatory")) {
				String action = request.getParameter(Constants.ACTION);
				if (action.equalsIgnoreCase("downloadFile")) {
					final String filename = request.getParameter("file");
					try (Connection c = DataBaseManager.getConnection()) {
						final PropertiesManager pmgr = new PropertiesManager();
						String filePath = pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office") + filename;
						CrawlerUtils.returnFile(response, filePath, "application/vnd.oasis.opendocument.text", false);
					} catch (Exception e) {
						Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
						return mapping.findForward(Constants.ERROR);
					}
					return null;
				} else if (action.equalsIgnoreCase("config")) {
					Connection c = DataBaseManager.getConnection();
					request.setAttribute("plantillas", PlantillaDAO.findAll(c, -1));
					request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getExecutedObservarories(c));
					DataBaseManager.closeConnection(c);
					return mapping.findForward(Constants.EXITO);
				} else if (action.equalsIgnoreCase("report")) {
					// Evol executions ids
					String[] exObsIds = request.getParameterValues("evol");
					String[] tagsToFilter = null;
					if (request.getParameter("tags") != null && !StringUtils.isEmpty(request.getParameter("tags"))) {
						tagsToFilter = request.getParameter("tags").split(",");
					}
					String reportTitle = "";
					if (request.getParameter(Constants.ID_REPORT_TITLE) != null) {
						reportTitle = request.getParameter(Constants.ID_REPORT_TITLE);
					}
					Long idBaseTemplate = null;
					if (request.getParameter(Constants.ID_BASE_TEMPLATE) != null) {
						idBaseTemplate = Long.parseLong(request.getParameter(Constants.ID_BASE_TEMPLATE));
					}
					final PropertiesManager pmgr = new PropertiesManager();
					final String basePath = pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office");
					try (Connection c = DataBaseManager.getConnection()) {
						ExportOpenOfficeUtils.createOpenOfficeDocumentglobal(request, basePath, basePath, tagsToFilter, exObsIds, reportTitle, idBaseTemplate);
						// FileUtils.deleteDir(new File(graphicPath));
						final DatosForm userData = LoginDAO.getUserDataByName(c, request.getSession().getAttribute(Constants.USER).toString());
						request.setAttribute("EMAIL", userData.getEmail());
						return mapping.findForward(Constants.GENERATE_REPORT_ASYNC);
					} catch (Exception e) {
						Logger.putLog("Error al exportar a pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
						return mapping.findForward(Constants.ERROR_PAGE);
					}
				} else {
					return mapping.findForward(Constants.EXITO);
				}
			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			CrawlerUtils.warnAdministrators(e, this.getClass());
			return mapping.findForward(Constants.ERROR_PAGE);
		}
	}
}