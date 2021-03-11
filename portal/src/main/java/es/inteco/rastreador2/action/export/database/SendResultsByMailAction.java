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
package es.inteco.rastreador2.action.export.database;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.SemillasObservatorioAction;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.utils.AnnexUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;

/**
 * The Class DatabaseExportAction.
 */
public class SendResultsByMailAction extends Action {
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
		long idObservatory = 0;
		if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
			idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
		}
		if (CrawlerUtils.hasAccess(request, "export.observatory.results")) {
			try {
				if (request.getParameter(Constants.ACTION) != null) {
					if (request.getParameter(Constants.ACTION).equals(Constants.CONFIG)) {
						Connection connection = DataBaseManager.getConnection();
						request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(connection, idObservatory, -1, null, null));
						DataBaseManager.closeConnection(connection);
						return config(mapping, request);
					} else if (request.getParameter(Constants.ACTION).equals(Constants.EXECUTE)) {
						String[] tagsToFilter = null;
						if (request.getParameter("tags") != null && !StringUtils.isEmpty(request.getParameter("tags"))) {
							tagsToFilter = request.getParameterValues("tags");
						}
						// Evol executions ids
						String[] exObsIds = request.getParameterValues("evol");
						if (exObsIds == null) {
							exObsIds = new String[] { request.getParameter(Constants.ID_EX_OBS) };
						}
						// if has tags (ids) check if has request params like fisrt_{idtag}, previous_{idtag}
						List<ComparisionForm> comparision = null;
						if (tagsToFilter != null && tagsToFilter.length > 0) {
							comparision = new ArrayList<>();
							for (String tagId : tagsToFilter) {
								ComparisionForm c = new ComparisionForm();
								c.setIdTag(Integer.parseInt(tagId));
								c.setFirst(request.getParameter("first_" + tagId));
								c.setPrevious(request.getParameter("previous_" + tagId));
								comparision.add(c);
							}
						}
						final Long idExObservatory = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
						AnnexUtils.generateEvolutionData(CrawlerUtils.getResources(request), idObservatory, idExObservatory, tagsToFilter, exObsIds, comparision);
						return mapping.findForward(Constants.CONFIRM);
					} else if (request.getParameter(Constants.ACTION).equals(Constants.CONFIRM)) {
						Connection connection = DataBaseManager.getConnection();
						request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(connection, idObservatory, -1, null, null));
						DataBaseManager.closeConnection(connection);
						final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
						final Long idObs = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
						final Long idCartucho = Long.valueOf(request.getParameter(Constants.ID_CARTUCHO));
						final String emailSubject = request.getParameter("emailSubject");
//						SendResultsMailUtils.generateAndSendData(idObs, idCartucho, idObsExecution);
						final SendMailThread sendMailThread = new SendMailThread(idObs, idObsExecution, idCartucho, emailSubject);
						sendMailThread.start();
						return mapping.findForward("sendResultsByMailAsync");
					} else if (request.getParameter(Constants.ACTION).equals("observatoriesByTag")) {
						observatoriesByTag(mapping, form, request, response);
					}
				}
			} catch (Exception e) {
				CrawlerUtils.warnAdministrators(e, this.getClass());
				return mapping.findForward(Constants.ERROR_PAGE);
			}
		} else {
			return mapping.findForward(Constants.NO_PERMISSION);
		}
		return null;
	}

	/**
	 * Confirm.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward config(ActionMapping mapping, HttpServletRequest request) throws Exception {
		final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		try (Connection c = DataBaseManager.getConnection()) {
			final List<EtiquetaForm> tagList = EtiquetaDAO.getAllEtiquetasClasification(c, 3);
			final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
			request.setAttribute("tagList", tagList);
		} catch (Exception e) {
			Logger.putLog("Error en la confirmación para exportar los resultados del observatorio: ", SendResultsByMailAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return mapping.findForward(Constants.CONFIG);
	}

	/**
	 * Observatories by tag.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward observatoriesByTag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
		String tagId = request.getParameter("tagId");
		try (Connection c = DataBaseManager.getConnection()) {
			String jsonObservatories = new Gson().toJson(ObservatorioDAO.getFulfilledObservatoriesByTag(c, idObservatory, -1, null, false, null, tagId));
			PrintWriter pw = response.getWriter();
			pw.write(jsonObservatories);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}
}