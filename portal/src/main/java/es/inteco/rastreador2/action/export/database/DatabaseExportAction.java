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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.action.observatorio.SemillasObservatorioAction;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.dao.export.database.Category;
import es.inteco.rastreador2.dao.export.database.Observatory;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.manager.BaseManager;
import es.inteco.rastreador2.manager.ObservatoryExportManager;
import es.inteco.rastreador2.manager.export.database.DatabaseExportManager;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.AnnexUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.export.database.DatabaseExportUtils;
import es.inteco.utils.FileUtils;

/**
 * The Class DatabaseExportAction.
 */
public class DatabaseExportAction extends Action {
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
					if (request.getParameter(Constants.ACTION).equals(Constants.EXPORT)) {
						String[] tagsToFilter = null;
						if (request.getParameter("tags") != null && !StringUtils.isEmpty(request.getParameter("tags"))) {
							tagsToFilter = request.getParameter("tags").split(",");
						}
						// Evol executions ids
						String[] exObsIds = request.getParameterValues("evol");
						if (exObsIds == null) {
							exObsIds = new String[] { request.getParameter(Constants.ID_EX_OBS) };
						}

						// Get classification thresholds
						Connection connection = DataBaseManager.getConnection();
						double firstThreshold = ObservatorioDAO.getFirstClassificationThresholdFromConfig(connection);
						double secondThreshold = ObservatorioDAO.getSecondClassificationThresholdFromConfig(connection);
						DataBaseManager.closeConnection(connection);

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
						// Export all??
						export(mapping, request);
						getAnnexes(mapping, request, response, tagsToFilter, exObsIds, comparision, firstThreshold, secondThreshold);
					} else if (request.getParameter(Constants.ACTION).equals(Constants.CONFIRM)) {
						Connection connection = DataBaseManager.getConnection();
						request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(connection, idObservatory, -1, null, null));
						DataBaseManager.closeConnection(connection);
						return confirm(mapping, request);
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
	 * Export.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward export(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		final Long idExObservatory = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
		final Long idCartucho = Long.valueOf(request.getParameter(Constants.ID_CARTUCHO));
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioRealizadoForm fulfilledObservatory = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, idExObservatory);
			if (CartuchoDAO.isCartuchoAccesibilidad(c, fulfilledObservatory.getCartucho().getId())) {
				final String application = CartuchoDAO.getApplication(DataBaseManager.getConnection(), idCartucho);
				final List<ObservatorioRealizadoForm> observatoriesList = ObservatorioDAO.getFulfilledObservatories(c, idObservatory, Constants.NO_PAGINACION, fulfilledObservatory.getFecha(), false,
						null);
				if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(application)) {
					for (ObservatorioRealizadoForm obsRealizado : observatoriesList) {
						if (ObservatoryExportManager.getObservatory(obsRealizado.getId()) == null) {
							exportResultadosAccesibilidad(PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), idObservatory, c, obsRealizado);
						}
					}
				} else {
					for (ObservatorioRealizadoForm obsRealizado : observatoriesList) {
						if (ObservatoryExportManager.getObservatory(obsRealizado.getId()) == null) {
							exportResultadosAccesibilidad(CrawlerUtils.getResources(request), idObservatory, c, obsRealizado);
						}
					}
				}
			} else {
				return mapping.findForward(Constants.ERROR);
			}
		} catch (Exception e) {
			Logger.putLog("Error al exportar los resultados del observatorio: ", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.observatorio.resultados.exportados", "volver.carga.observatorio");
		return mapping.findForward(Constants.EXITO);
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
	private void exportResultadosAccesibilidad(final MessageResources messageResources, Long idObservatory, Connection c, ObservatorioRealizadoForm fulfilledObservatory) throws Exception {
		Observatory observatory = DatabaseExportManager.getObservatory(fulfilledObservatory.getId());
		if (observatory == null) {
			Logger.putLog("Generando exportación", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR);
			// Información general de la ejecución del Observatorio
			observatory = DatabaseExportUtils.getObservatoryInfo(messageResources, fulfilledObservatory.getId());
			final List<CategoriaForm> categories = ObservatorioDAO.getObservatoryCategories(c, idObservatory);
			for (CategoriaForm categoriaForm : categories) {
				final Category category = DatabaseExportUtils.getCategoryInfo(messageResources, categoriaForm, observatory);
				observatory.getCategoryList().add(category);
			}
			final ObservatorioRealizadoForm observatorioRealizadoForm = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, fulfilledObservatory.getId());
			observatory.setName(observatorioRealizadoForm.getObservatorio().getNombre());
			observatory.setDate(new Timestamp(observatorioRealizadoForm.getFecha().getTime()));
			BaseManager.save(observatory);
		} else {
			BaseManager.delete(observatory);
			exportResultadosAccesibilidad(messageResources, idObservatory, c, fulfilledObservatory);
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
	private ActionForward confirm(ActionMapping mapping, HttpServletRequest request) throws Exception {
		final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		try (Connection c = DataBaseManager.getConnection()) {
			final List<EtiquetaForm> tagList = EtiquetaDAO.getAllEtiquetasClasification(c, 3);
			final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
			request.setAttribute("tagList", tagList);
		} catch (Exception e) {
			Logger.putLog("Error en la confirmación para exportar los resultados del observatorio: ", DatabaseExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return mapping.findForward(Constants.CONFIRM);
	}

	/**
	 * Gets the annexes.
	 *
	 * @param mapping      the mapping
	 * @param request      the request
	 * @param response     the response
	 * @param tagsToFilter the tags to filter
	 * @param exObsIds     the ex obs ids
	 * @param comparision  the comparision TODO Apply
	 * @return the annexes
	 * @throws Exception the exception
	 */
	private ActionForward getAnnexes(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response, final String[] tagsToFilter, final String[] exObsIds,
			final List<ComparisionForm> comparision, double firstThreshold, double secondThreshold) throws Exception {
		try {
			final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
			final Long idOperation = System.currentTimeMillis();
			final Long idCartucho = Long.valueOf(request.getParameter(Constants.ID_CARTUCHO));
			MessageResources resources = CrawlerUtils.getResources(request);
			final String application = CartuchoDAO.getApplication(DataBaseManager.getConnection(), idCartucho);
			if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(application)) {
				resources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
			} else if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(application)) {
				resources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
			}
			AnnexUtils.generateAllAnnex(resources, idObsExecution, idOperation, tagsToFilter, exObsIds, comparision, firstThreshold, secondThreshold);
			final PropertiesManager pmgr = new PropertiesManager();
			final String exportPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path");
			final String zipPath = exportPath + idOperation + File.separator + "anexos.zip";
			ZipUtils.generateZipFile(exportPath + idOperation.toString(), zipPath, true);
			CrawlerUtils.returnFile(response, zipPath, "application/zip", true);
			FileUtils.deleteDir(new File(exportPath + idOperation));
			return null;
		} catch (Exception e) {
			Logger.putLog("Exception generando los anexos.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			final ActionMessages errors = new ActionMessages();
			errors.add("usuarioDuplicado", new ActionMessage("data.export"));
			saveErrors(request, errors);
			// return getFulfilledObservatories(mapping, request);
			return mapping.findForward(Constants.ERROR);
		}
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