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
package es.inteco.rastreador2.intav.action;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.AnalysisForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.utils.IntavUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class ResultsAction.
 */
public class ResultsAction extends Action {
	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (CrawlerUtils.hasAccess(request, "show.crawler.results")) {
			return results(mapping, request);
		} else {
			return mapping.findForward(Constants.NO_PERMISSION);
		}
	}

	/**
	 * Results.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 */
	@SuppressWarnings("deprecation")
	private ActionForward results(ActionMapping mapping, HttpServletRequest request) {
		Connection c = null;
		boolean originAnnexes = true;
		try {
			// Inicializamos el evaluador
			if (!EvaluatorUtility.isInitialized()) {
				EvaluatorUtility.initialize();
			}
			if (request.getParameter(Constants.OBSERVATORY_ID) != null) {
				request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.OBSERVATORY_ID));
			}
			PropertiesManager pmgr = new PropertiesManager();
			c = DataBaseManager.getConnection();
			String user = (String) request.getSession().getAttribute(Constants.USER);
			long idExecution = 0;
			if (request.getParameter(Constants.ID) != null) {
				idExecution = Long.parseLong(request.getParameter(Constants.ID));
			}
			long idRastreo = 0;
			if (request.getParameter(Constants.ID_RASTREO) != null) {
				idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
			}
			// Comprobamos que el usuario esta asociado con los resultados de
			// los rastreos que quiere recuperar
			if (RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
				int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
				int numResult = AnalisisDatos.countAnalysisByTracking(idExecution);
				List<AnalysisForm> analysisList = EvaluatorUtils.getFormList(AnalisisDatos.getAnalysisByTracking(idExecution, (pagina - 1), request, originAnnexes));
				for (AnalysisForm analyse : analysisList) {
					if ((analyse.getUrl() != null) && (analyse.getUrl().length() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string")))) {
						analyse.setUrlTitle(analyse.getUrl());
						analyse.setUrl(analyse.getUrl().substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string"))) + "...");
					}
					if (analyse.getEntity() != null && analyse.getEntity().length() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string"))) {
						analyse.setEntityTitle(analyse.getEntity());
						analyse.setEntity(analyse.getEntity().substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "break.characters.table.string"))) + "...");
					}
				}
				if ((analysisList.isEmpty())) {
					ActionErrors errors = new ActionErrors();
					errors.add("noResults", new ActionMessage("errors.noResults"));
					saveErrors(request, errors);
				}
				request.setAttribute(Constants.LIST_ANALYSIS, analysisList);
				request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
				request.setAttribute("numResult", numResult);
				request.setAttribute("currentPage", pagina);
				request.setAttribute("pageSize", pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
				if (request.getParameter(Constants.OBSERVATORY) != null) {
					// Parametro adicional para los textos en la JSP
					String aplicacion = CartuchoDAO.getApplicationFromExecutedObservatoryId(c, idExecution, idRastreo);
					MessageResources messageReources = MessageResources.getMessageResources("ApplicationResources");
					if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
						request.setAttribute("aplicacion", Constants.NORMATIVA_UNE_2012_B);
						messageReources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B);
					} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion)) {
						request.setAttribute("aplicacion", Constants.NORMATIVA_UNE_EN2019);
						messageReources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
					} else if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
						request.setAttribute("aplicacion", Constants.NORMATIVA_ACCESIBILIDAD);
						messageReources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
					}
					request.setAttribute(Constants.OBSERVATORY, request.getParameter(Constants.OBSERVATORY));
					request.setAttribute(Constants.SCORE, IntavUtils.calculateScore(request, idExecution, messageReources));
				}
			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultsAction.class, Logger.LOG_LEVEL_ERROR, e);
			return mapping.findForward(Constants.ERROR);
		} finally {
			DataBaseManager.closeConnection(c);
		}
		return mapping.findForward(Constants.LIST_ANALYSIS_BY_TRACKING);
	}
}
