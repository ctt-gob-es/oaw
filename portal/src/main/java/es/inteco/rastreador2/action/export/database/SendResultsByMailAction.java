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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.SemillasObservatorioAction;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.observatorio.TemplateRangeForm;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoric;
import es.inteco.rastreador2.actionform.observatorio.UraSendResultForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.dependencia.DependenciaDAO;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.observatorio.TemplateRangeDAO;
import es.inteco.rastreador2.dao.observatorio.UraSendResultDAO;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.manager.BaseManager;
import es.inteco.rastreador2.manager.ObservatoryExportManager;
import es.inteco.rastreador2.manager.exportation.database.DatabaseExportManager;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.SendResultsMailUtils;
import es.inteco.rastreador2.utils.export.database.DatabaseExportUtils;

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
						final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
						Connection connection = DataBaseManager.getConnection();
						// Get existing data
						final String[] exObsIds = ObservatorioDAO.getExObsIdsConfig(connection, idObsExecution);
						final List<ComparisionForm> comparision = ObservatorioDAO.getComparisionConfig(connection, idObsExecution);
						request.setAttribute("configExecutedObs", exObsIds);
						request.setAttribute("configComparision", comparision);
						request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(connection, idObservatory, -1, null, null));
						DataBaseManager.closeConnection(connection);
						return config(mapping, request);
					} else if (request.getParameter(Constants.ACTION).equals("finish")) {
						// go to step 2
						final Long idExObservatory = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
						/****/
						Connection connection = DataBaseManager.getConnection();
						Map<String, String> config = ObservatorioDAO.getConfigStep2(connection, idExObservatory);
						DataBaseManager.closeConnection(connection);
						request.setAttribute("emailSubject", config.get("emailSubject"));
						request.setAttribute("cco", config.get("cco"));
						/* request.setAttribute("tags", request.getParameter("tags")); */
						DataBaseManager.closeConnection(connection);
						return mapping.findForward(Constants.CONFIRM);
					} else if (request.getParameter(Constants.ACTION).equals(Constants.EXECUTE)) {
						final Long idCartucho = Long.valueOf(request.getParameter(Constants.ID_CARTUCHO));
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
						final boolean originAnnexes = true;
						/****/
						try (Connection c = DataBaseManager.getConnection()) {
							final ObservatorioRealizadoForm fulfilledObservatory = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, idExObservatory);
							if (CartuchoDAO.isCartuchoAccesibilidad(c, fulfilledObservatory.getCartucho().getId())) {
								final String application = CartuchoDAO.getApplication(c, idCartucho);
								final List<ObservatorioRealizadoForm> observatoriesList = ObservatorioDAO.getFulfilledObservatories(c, idObservatory, Constants.NO_PAGINACION,
										fulfilledObservatory.getFecha(), false, null);
								if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(application)) {
									for (ObservatorioRealizadoForm obsRealizado : observatoriesList) {
										if (ObservatoryExportManager.getObservatory(obsRealizado.getId()) == null) {
											exportResultadosAccesibilidad(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), idObservatory, c, obsRealizado,
													originAnnexes);
										}
									}
								} else {
									for (ObservatorioRealizadoForm obsRealizado : observatoriesList) {
										if (ObservatoryExportManager.getObservatory(obsRealizado.getId()) == null) {
											exportResultadosAccesibilidad(CrawlerUtils.getResources(request), idObservatory, c, obsRealizado, originAnnexes);
										}
									}
								}
							}
						} catch (Exception e) {
							Logger.putLog("Error al exportar los resultados del observatorio: ", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR, e);
							throw e;
						}
						/****/
//						AnnexUtils.generateEvolutionData(CrawlerUtils.getResources(request), idObservatory, idExObservatory, tagsToFilter, exObsIds, comparision);
//						Connection connection = DataBaseManager.getConnection();
//						Map<String, String> config = ObservatorioDAO.getConfigStep2(connection, idExObservatory);
//						DataBaseManager.closeConnection(connection);
//						request.setAttribute("emailSubject", config.get("emailSubject"));
//						request.setAttribute("cco", config.get("cco"));
//						DataBaseManager.closeConnection(connection);
//						return mapping.findForward(Constants.CONFIRM);
						// Async
						Connection connection = DataBaseManager.getConnection();
						final DatosForm userData = LoginDAO.getUserDataByName(connection, request.getSession().getAttribute(Constants.USER).toString());
						final String url = request.getRequestURL().toString();
						final String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
						final SendMailCalculateResultsThread sendMailThread = new SendMailCalculateResultsThread(CrawlerUtils.getResources(request), idObservatory, idExObservatory, tagsToFilter,
								exObsIds, comparision, userData.getEmail(), baseURL);
						sendMailThread.start();
						DataBaseManager.closeConnection(connection);
						return mapping.findForward("calculateResultsByMailAsync");
					} else if (request.getParameter(Constants.ACTION).equals(Constants.CONFIRM)) {
						Connection connection = DataBaseManager.getConnection();
						request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(connection, idObservatory, -1, null, null));
						final DatosForm userData = LoginDAO.getUserDataByName(connection, request.getSession().getAttribute(Constants.USER).toString());
						final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
						final Long idObs = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
						final Long idCartucho = Long.valueOf(request.getParameter(Constants.ID_CARTUCHO));
						final String emailSubject = request.getParameter("emailSubject");
						final String cco = request.getParameter("cco");
						Long hasCustomTexts = 0L;
						/*
						 * String[] tagsToFilter = null; if (request.getParameter("tags") != null && !StringUtils.isEmpty(request.getParameter("tags"))) { tagsToFilter =
						 * request.getParameterValues("tags"); }
						 */
						if (!StringUtils.isEmpty(request.getParameter("customTextSelector"))) {
							hasCustomTexts = Long.parseLong(request.getParameter("customTextSelector"));
						}
						ObservatorioDAO.saveConfigStep2(connection, idObsExecution, emailSubject, cco, hasCustomTexts);
						// Some URAS has custom texts
						if (hasCustomTexts == 0L) {
							UraSendResultDAO.markHasCustomText(connection, true, null, idObsExecution);
						} else if (hasCustomTexts == 1L) {
							UraSendResultDAO.markHasCustomText(connection, false, null, idObsExecution);
							String[] urasIds = null;
							if (!StringUtils.isEmpty(request.getParameter("uras"))) {
								urasIds = new String[] { request.getParameter("uras") };
								if (urasIds != null) {
									UraSendResultDAO.markHasCustomText(connection, true, urasIds, idObsExecution);
								}
							}
						} else {
							UraSendResultDAO.markHasCustomText(connection, false, null, idObsExecution);
						}
						final SendMailThread sendMailThread = new SendMailThread(idObs, idObsExecution, idCartucho, emailSubject, cco, userData.getEmail());
						sendMailThread.start();
						DataBaseManager.closeConnection(connection);
						return mapping.findForward("sendResultsByMailAsync");
					} else if (request.getParameter(Constants.ACTION).equals("save")) {
						// Save email subject and cco??
						final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
						final String emailSubject = request.getParameter("emailSubject");
						final String cco = request.getParameter("cco");
						Long hasCustomTexts = 0L;
						if (!StringUtils.isEmpty(request.getParameter("customTextSelector"))) {
							hasCustomTexts = Long.parseLong(request.getParameter("customTextSelector"));
						}
						Connection connection = DataBaseManager.getConnection();
						ObservatorioDAO.saveConfigStep2(connection, idObsExecution, emailSubject, cco, hasCustomTexts);
						// Some URAS has custom texts
						if (hasCustomTexts == 0L) {
							UraSendResultDAO.markHasCustomText(connection, true, null, idObsExecution);
						} else if (hasCustomTexts == 1L) {
							UraSendResultDAO.markHasCustomText(connection, false, null, idObsExecution);
							String[] urasIds = null;
							if (!StringUtils.isEmpty(request.getParameter("uras"))) {
								urasIds = new String[] { request.getParameter("uras") };
								if (urasIds != null) {
									UraSendResultDAO.markHasCustomText(connection, true, urasIds, idObsExecution);
								}
							}
						} else {
							UraSendResultDAO.markHasCustomText(connection, false, null, idObsExecution);
						}
						DataBaseManager.closeConnection(connection);
						request.setAttribute("emailSubject", emailSubject);
						request.setAttribute("cco", cco);
						return mapping.findForward(Constants.CONFIRM);
					} else if (request.getParameter(Constants.ACTION).equals("results")) {
						Connection connection = DataBaseManager.getConnection();
						final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
						idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
						final List<UraSendHistoric> sendHistoric = UraSendResultDAO.getSendHistoric(connection, idObsExecution, idObservatory);
						Map<String, String> config = ObservatorioDAO.getConfigStep2(connection, idObsExecution);
						DataBaseManager.closeConnection(connection);
						request.setAttribute("emailSubject", config.get("emailSubject"));
						request.setAttribute("cco", config.get("cco"));
						request.setAttribute("results", sendHistoric);
						DataBaseManager.closeConnection(connection);
						return mapping.findForward("results");
					} else if (request.getParameter(Constants.ACTION).equals("observatoriesByTag")) {
						observatoriesByTag(mapping, form, request, response);
					} else if ("previewEmail".equalsIgnoreCase(request.getParameter(Constants.ACTION))) {
						previewEmail(mapping, form, request, response);
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
	 * Export resultados accesibilidad.
	 *
	 * @param messageResources     the message resources
	 * @param idObservatory        the id observatory
	 * @param c                    the c
	 * @param fulfilledObservatory the fulfilled observatory
	 * @throws Exception the exception
	 */
	private void exportResultadosAccesibilidad(final MessageResources messageResources, Long idObservatory, Connection c, ObservatorioRealizadoForm fulfilledObservatory, final boolean originAnnexes)
			throws Exception {
		Observatory observatory = DatabaseExportManager.getObservatory(fulfilledObservatory.getId());
		if (observatory == null) {
			Logger.putLog("Generando exportación", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR);
			// Información general de la ejecución del Observatorio
			observatory = DatabaseExportUtils.getObservatoryInfo(messageResources, fulfilledObservatory.getId(), originAnnexes);
			final List<CategoriaForm> categories = ObservatorioDAO.getObservatoryCategories(c, idObservatory);
			for (CategoriaForm categoriaForm : categories) {
				final Category category = DatabaseExportUtils.getCategoryInfo(messageResources, categoriaForm, observatory, originAnnexes);
				observatory.getCategoryList().add(category);
			}
			final ObservatorioRealizadoForm observatorioRealizadoForm = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, fulfilledObservatory.getId());
			observatory.setName(observatorioRealizadoForm.getObservatorio().getNombre());
			observatory.setDate(new Timestamp(observatorioRealizadoForm.getFecha().getTime()));
			BaseManager.save(observatory);
		} else {
			BaseManager.delete(observatory);
			exportResultadosAccesibilidad(messageResources, idObservatory, c, fulfilledObservatory, originAnnexes);
		}
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

	/**
	 * Preview email.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward previewEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final Long idExObs = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
			final Long idSend = Long.parseLong(request.getParameter("idSend"));
			response.setContentType("text/json");
			UraSendResultForm uraCustom = UraSendResultDAO.findById(c, idExObs, idSend);
			DependenciaForm ura = DependenciaDAO.findById(c, uraCustom.getUra().getId());
			final TemplateRangeForm template = TemplateRangeDAO.findById(c, idExObs, uraCustom.getRange().getId());
			String mailBody = SendResultsMailUtils.composeMailBody(ura, uraCustom, template, "xxxxxxxxx.zip", "examplePassword", calculateExpirationDate(c)).toString();
			PrintWriter pw = response.getWriter();
//			pw.write("{\"preview\": \"" + StringEscapeUtils.escapeHtml4(mailBody) + "\" }");
			pw.write("{\"preview\": " + new Gson().toJson(mailBody) + " }");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Calculate expiration date.
	 *
	 * @param c the c
	 * @return the calendar
	 * @throws SQLException the SQL exception
	 */
	private static Calendar calculateExpirationDate(Connection c) throws Exception {
		int days = ObservatorioDAO.getFileExpirationFromConfig(c);
		DataBaseManager.closeConnection(c);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, days);
		calendar.getTime();
		return calendar;
	}
}