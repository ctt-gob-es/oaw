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

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.crawler.job.CrawlerJobManager;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.rastreador2.utils.AnnexUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.Pagination;
import es.inteco.rastreador2.utils.RastreoUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.utils.FileUtils;

/**
 * The Class ResultadosObservatorioAction.
 */
public class ResultadosObservatorioAction extends Action {
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
			if (CrawlerUtils.hasAccess(request, "view.observatory.results")) {
				String action = request.getParameter(Constants.ACTION);
				if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
					request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
				}
				if (action != null) {
					if (action.equalsIgnoreCase(Constants.GET_SEEDS)) {
						request.setAttribute(Constants.ID_CARTUCHO, request.getParameter(Constants.ID_CARTUCHO));
						return getSeeds(mapping, form, request);
					} else if (action.equals(Constants.ACCION_BORRAR)) {
						return deleteCrawlerSeed(mapping, request);
					} else if (action.equals(Constants.ACCION_LANZAR_EJECUCION)) {
						return throwCrawlerSeedExecution(mapping, request);
					} else if (action.equals(Constants.ACCION_CONFIRMACION_BORRAR)) {
						return deleteSeedConfirmation(mapping, request);
					} else if (action.equals(Constants.GET_FULFILLED_OBSERVATORIES)) {
						request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
						return getFulfilledObservatories(mapping, request);
					} else if (action.equals(Constants.GET_ANNEXES)) {
						return getAnnexes(mapping, request, response);
					} else if (action.equals(Constants.ACCION_CONFIRMACION_BORRAR_EX_SEED)) {
						return showDeleteSeedExecution(request, mapping);
					} else if (action.equals(Constants.ACCION_BORRAR_EJECUCION)) {
						return deleteObservatoryExecutedSeed(request, mapping);
					} else if (action.equalsIgnoreCase(Constants.REGENERATE_RESULTS)) {
						request.setAttribute(Constants.ID_CARTUCHO, request.getParameter(Constants.ID_CARTUCHO));
						return regenerateResults(mapping, form, request);
					} else if (action.equalsIgnoreCase(Constants.STOP_CRAWL)) {
						if (request.getParameter(Constants.CONFIRMACION) != null && request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_SI)) {
							request.setAttribute(Constants.ID_CARTUCHO, request.getParameter(Constants.ID_CARTUCHO));
							return stop(mapping, form, request);
						} else {
							ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(DataBaseManager.getConnection(), Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
							request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
							request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
							request.setAttribute(Constants.ID_EX_OBS, request.getParameter(Constants.ID_EX_OBS));
							return mapping.findForward("confirmarPararObservatorio");
						}
					} else if (action.equalsIgnoreCase(Constants.ADD_SEDD_OBS)) {
						request.setAttribute(Constants.ID_CARTUCHO, request.getParameter(Constants.ID_CARTUCHO));
						return addSeed(mapping, form, request);
					}
				}
			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			CrawlerUtils.warnAdministrators(e, this.getClass());
			return mapping.findForward(Constants.ERROR_PAGE);
		}
		return null;
	}

	/**
	 * Delete observatory executed seed.
	 *
	 * @param request the request
	 * @param mapping the mapping
	 * @return the action forward
	 */
	private ActionForward deleteObservatoryExecutedSeed(final HttpServletRequest request, final ActionMapping mapping) {
		try (Connection c = DataBaseManager.getConnection()) {
			RastreoDAO.borrarRastreoRealizado(c, Long.parseLong(request.getParameter(Constants.ID)));
			PropertiesManager pmgr = new PropertiesManager();
			String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.semilla.ejecutada.eliminada");
			String volver = pmgr.getValue("returnPaths.properties", "volver.lista.observatorios.realizados.primarios.seeds").replace("{0}", request.getParameter(Constants.ID_OBSERVATORIO))
					.replace("{1}", request.getParameter(Constants.ID_EX_OBS)).replace("{2}", request.getParameter(Constants.ID_CARTUCHO));
			request.setAttribute("mensajeExito", mensaje);
			request.setAttribute("accionVolver", volver);
			return mapping.findForward(Constants.EXITO2);
		} catch (Exception e) {
			Logger.putLog("Excepcion al borrar la ejecucion de la semilla del Observatorio.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return mapping.findForward(Constants.ERROR_PAGE);
	}

	/**
	 * Show delete seed execution.
	 *
	 * @param request the request
	 * @param mapping the mapping
	 * @return the action forward
	 */
	private ActionForward showDeleteSeedExecution(final HttpServletRequest request, final ActionMapping mapping) {
		try (Connection c = DataBaseManager.getConnection()) {
			request.setAttribute(Constants.SEMILLA_FORM, SemillaDAO.getSeedById(c, Long.parseLong(request.getParameter(Constants.ID_SEMILLA))));
			return mapping.findForward(Constants.CONFIRMACION_DELETE);
		} catch (Exception e) {
			Logger.putLog("Exceción al recuperar los datos de la semilla.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return mapping.findForward(Constants.ERROR_PAGE);
	}

	/**
	 * Delete seed confirmation.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward deleteSeedConfirmation(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		if (request.getParameter(Constants.OBSERVATORY_ID) != null) {
			request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.OBSERVATORY_ID));
		}
		final String idSemilla = request.getParameter(Constants.SEMILLA);
		try (Connection c = DataBaseManager.getConnection()) {
			final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
			request.setAttribute(Constants.OBSERVATORY_SEED_FORM, semillaForm);
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return mapping.findForward(Constants.CONFIRMACION2);
	}

	/**
	 * Delete crawler seed.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward deleteCrawlerSeed(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		final String idSemilla = request.getParameter(Constants.SEMILLA);
		final String confirmacion = request.getParameter(Constants.CONFIRMACION);
		try (Connection c = DataBaseManager.getConnection()) {
			if (confirmacion.equals(Constants.CONF_SI)) {
				SemillaDAO.deleteObservatorySeed(c, Long.parseLong(idSemilla), Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
				ActionMessages messages = new ActionMessages();
				messages.add("semillaEliminada", new ActionMessage("mensaje.exito.semilla.observatorio.eliminada"));
				saveErrors(request, messages);
			}
			request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.ID_OBSERVATORIO));
		} catch (Exception e) {
			Logger.putLog("Error: ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return new ActionForward(mapping.findForward(Constants.GET_SEED_RESULTS_FORWARD));
	}

	/**
	 * Throw crawler seed execution.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward throwCrawlerSeedExecution(ActionMapping mapping, HttpServletRequest request) throws Exception {
		Connection c = null;
		ActionForward forward = null;
		try {
			c = DataBaseManager.getConnection();
			c.setAutoCommit(false);
			if (request.getParameter(Constants.OBSERVATORY_ID) != null) {
				request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.OBSERVATORY_ID));
			}
			if (request.getParameter(Constants.ID_EX_OBS) != null) {
				request.setAttribute(Constants.ID_EX_OBS, request.getParameter(Constants.ID_EX_OBS));
			}
			if (request.getParameter(Constants.CONFIRMACION) != null) {
				if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_SI)) {
					// Recuperamos el id del rastreo
					Long idCrawling = RastreoDAO.getCrawlerFromSeedAndObservatory(c, Long.parseLong(request.getParameter(Constants.ID_SEMILLA)),
							Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
					Long idOldExecution = RastreoDAO.getExecutedCrawlerId(c, idCrawling, Long.parseLong(request.getParameter(Constants.ID_EX_OBS)));
					// Borramos la cache
					CacheUtils.removeFromCache(Constants.OBSERVATORY_KEY_CACHE + request.getParameter(Constants.ID_EX_OBS));
					// Lanzamos el rastreo y recuperamos el id de ejecución
					lanzarRastreo(c, String.valueOf(idCrawling), request.getParameter(Constants.ID_EX_OBS));
					Long idNewExecution = Long.valueOf(RastreoDAO.getExecutedCrawling(c, idCrawling, Long.valueOf(request.getParameter(Constants.ID_SEMILLA))).getId());
					// Borramos el rastreo ejecutado antiguo
					RastreoUtils.borrarArchivosAsociados(c, String.valueOf(idOldExecution));
					RastreoDAO.borrarRastreoRealizado(c, idOldExecution);
					// Le asignamos el nuevo a la ejecución del observatorio
					RastreoDAO.setObservatoryExecutionToCrawlerExecution(c, Long.parseLong(request.getParameter(Constants.ID_EX_OBS)), idNewExecution);
					forward = new ActionForward(mapping.findForward(Constants.GET_SEED_RESULTS_FORWARD));
					String path = forward.getPath() + "&" + Constants.ID_EX_OBS + "=" + request.getParameter(Constants.ID_EX_OBS) + "&" + Constants.ID_OBSERVATORIO + "="
							+ request.getParameter(Constants.ID_OBSERVATORIO) + "&" + Constants.ID_CARTUCHO + "=" + request.getParameter(Constants.ID_CARTUCHO);
					forward.setPath(path);
					forward.setRedirect(true);
					c.commit();
				} else if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_NO)) {
					forward = new ActionForward(mapping.findForward(Constants.GET_SEED_RESULTS_FORWARD));
				}
			} else {
				String idSeed = request.getParameter(Constants.ID_SEMILLA);
				request.setAttribute(Constants.OBSERVATORY_SEED_FORM, SemillaDAO.getSeedById(c, Long.parseLong(idSeed)));
				forward = new ActionForward(mapping.findForward(Constants.FORWARD_THROW_SEED_CONFIRMATION));
				c.commit();
			}
			request.setAttribute(Constants.OBSERVATORY_ID, request.getParameter(Constants.ID_OBSERVATORIO));
		} catch (Exception e) {
			Logger.putLog("Error: ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			if (c != null) {
				c.rollback();
			}
			throw e;
		} finally {
			DataBaseManager.closeConnection(c);
		}
		return forward;
	}

	/**
	 * Lanzar rastreo.
	 *
	 * @param c          the c
	 * @param idCrawling the id crawling
	 * @param idExObs    the id ex obs
	 * @throws Exception the exception
	 */
	private void lanzarRastreo(final Connection c, final String idCrawling, final String idExObs) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		final DatosCartuchoRastreoForm dcrForm = RastreoDAO.cargarDatosCartuchoRastreo(c, idCrawling);
		dcrForm.setCartuchos(CartuchoDAO.getNombreCartucho(dcrForm.getId_rastreo()));
		// Cargamos los dominios introducidos en el archivo de semillas
		final int typeDomains = dcrForm.getIdObservatory() == 0 ? Constants.ID_LISTA_SEMILLA : Constants.ID_LISTA_SEMILLA_OBSERVATORIO;
		dcrForm.setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, false));
		dcrForm.setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, true));
		dcrForm.setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
		dcrForm.setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));
		dcrForm.setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) dcrForm.getId_rastreo()));
		if (CartuchoDAO.isCartuchoAccesibilidad(c, dcrForm.getId_cartucho())) {
			dcrForm.setFicheroNorma(CrawlerUtils.getFicheroNorma(dcrForm.getId_guideline()));
		}
		final DatosForm userData = LoginDAO.getUserDataByName(c, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"));
		final Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, dcrForm, Long.parseLong(idExObs), Long.valueOf(userData.getId()));
		final CrawlerJob crawlerJob = new CrawlerJob();
		crawlerJob.makeCrawl(CrawlerUtils.getCrawlerData(dcrForm, idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null));
		// CrawlerJobManager.startJob(CrawlerUtils.getCrawlerData(dcrForm, idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null));
		// Calculate scores
		ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idExObs), 0, null);
		// Recuperar sólo el ejectado
		final List<ResultadoSemillaFullForm> seedsResults2 = ObservatorioDAO.getResultSeedsFullFromObservatory(c, new SemillaForm(), Long.parseLong(idExObs), 0L, -1);
		for (ResultadoSemillaFullForm seedsResult : seedsResults2) {
			if (seedsResult.getIdFulfilledCrawling().equals(idFulfilledCrawling.toString())) {
				List<ResultadoSemillaFullForm> tmp = new ArrayList<ResultadoSemillaFullForm>();
				tmp.add(seedsResult);
				ObservatoryUtils.setAvgScore2(c, tmp, Long.parseLong(idExObs));
				break;
			}
		}
	}

	/**
	 * Gets the seeds.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the seeds
	 * @throws Exception the exception
	 */
	private ActionForward getSeeds(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
		final SemillaForm semillaForm = (SemillaForm) form;
		final Long idObservatoryExecution = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
		try (Connection c = DataBaseManager.getConnection()) {
			final PropertiesManager pmgr = new PropertiesManager();
			final int numResultA = ObservatorioDAO.countResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE);
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			// Obtenemos las semillas de esa página del listado
			final List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE, pagina - 1);
			// Calculamos la puntuación media de cada semilla y la guardamos en sesion
			// request.setAttribute(Constants.OBSERVATORY_SEED_LIST, ObservatoryUtils.setAvgScore(c, seedsResults, idObservatoryExecution));
			// TOTO La vamos a recuperar de base de datos...
			request.setAttribute(Constants.OBSERVATORY_SEED_LIST, seedsResults);
			request.setAttribute(Constants.LIST_PAGE_LINKS,
					Pagination.createPagination(request, numResultA, pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"), pagina, Constants.PAG_PARAM));
		} catch (Exception e) {
			Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw new Exception(e);
		}
		return mapping.findForward(Constants.OBSERVATORY_SEED_LIST);
	}

	/**
	 * Gets the fulfilled observatories.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the fulfilled observatories
	 * @throws Exception the exception
	 */
	public ActionForward getFulfilledObservatories(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		final Long observatoryId = Long.valueOf(request.getParameter(Constants.OBSERVATORY_ID));
		// Para mostrar todos los Rastreos del Sistema
		try (Connection c = DataBaseManager.getConnection()) {
			final int numResult = ObservatorioDAO.countFulfilledObservatories(c, observatoryId);
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(c, observatoryId, (pagina - 1), null, null));
			request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
		} catch (Exception e) {
			Logger.putLog("Exception: ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return mapping.findForward(Constants.GET_FULFILLED_OBSERVATORIES);
	}

	/**
	 * Gets the annexes.
	 *
	 * @param mapping  the mapping
	 * @param request  the request
	 * @param response the response
	 * @return the annexes
	 * @throws Exception the exception
	 */
	private ActionForward getAnnexes(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		try {
			final Long idObsExecution = Long.valueOf(request.getParameter(Constants.ID_EX_OBS));
			final Long idCartucho = Long.valueOf(request.getParameter(Constants.ID_CARTUCHO));
			final Long idOperation = System.currentTimeMillis();
			MessageResources resources = CrawlerUtils.getResources(request);
			final String application = CartuchoDAO.getApplication(DataBaseManager.getConnection(), idCartucho);
			if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(application)) {
				resources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
			}
			AnnexUtils.createAnnexPaginas(resources, idObsExecution, idOperation, null, null);
			AnnexUtils.createAnnexPortales(resources, idObsExecution, idOperation, null, null);
			AnnexUtils.createAnnexXLSX2(resources, idObsExecution, idOperation, null);
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
			return getFulfilledObservatories(mapping, request);
		}
	}

	/**
	 * Regenerate results.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward regenerateResults(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
		final SemillaForm semillaForm = (SemillaForm) form;
		final Long idObservatoryExecution = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
		try (Connection c = DataBaseManager.getConnection()) {
			final PropertiesManager pmgr = new PropertiesManager();
			final int numResultA = ObservatorioDAO.countResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE);
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			// Obtenemos las semillas de esa página del listado
			final List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE, pagina - 1);
			// Calculamos la puntuación media de cada semilla y la guardamos en sesion
			final List<ResultadoSemillaFullForm> seedsResults2 = ObservatorioDAO.getResultSeedsFullFromObservatory(c, new SemillaForm(), idObservatoryExecution, 0L, -1);
			request.setAttribute(Constants.OBSERVATORY_SEED_LIST, ObservatoryUtils.setAvgScore2(c, seedsResults2, idObservatoryExecution));
			// TOTO La vamos a recuperar de base de datos...
			request.setAttribute(Constants.OBSERVATORY_SEED_LIST, seedsResults);
			request.setAttribute(Constants.LIST_PAGE_LINKS,
					Pagination.createPagination(request, numResultA, pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"), pagina, Constants.PAG_PARAM));
		} catch (Exception e) {
			Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw new Exception(e);
		}
		return mapping.findForward(Constants.OBSERVATORY_SEED_LIST);
	}

	/**
	 * Stop.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward stop(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
		CrawlerJobManager.endJob(Long.parseLong(request.getParameter(Constants.ID_EX_OBS)), Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
		try (Connection c = DataBaseManager.getConnection()) {
			ObservatorioDAO.updateObservatoryStatus(c, Long.parseLong(request.getParameter(Constants.ID_EX_OBS)), es.inteco.crawler.common.Constants.STOPPED_OBSERVATORY_STATUS);
			DataBaseManager.closeConnection(c);
		}
		final PropertiesManager pmgr = new PropertiesManager();
		request.setAttribute("mensajeExito", getResources(request).getMessage("observatory.stop.success.message"));
		final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		request.setAttribute("accionVolver", pmgr.getValue("returnPaths.properties", "volver.lista.observatorios.realizados.primarios").replace("{0}", idObservatory.toString()));
		return mapping.findForward(Constants.EXITO2);
	}

	/**
	 * Adds the seed.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward addSeed(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
		final Long idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
		final Long idExObs = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));
		final Long idSeed = Long.parseLong(request.getParameter(Constants.ID_SEMILLA));
		final Long idCartucho = Long.parseLong(request.getParameter(Constants.ID_CARTUCHO));
		try (Connection c = DataBaseManager.getConnection()) {
			ObservatorioDAO.addSeedObservatory(c, idObservatory, idExObs, idSeed, idCartucho);
			ObservatorioDAO.addSeedObservatory(c, idObservatory, idExObs, idSeed, idCartucho);
		} catch (Exception e) {
			Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw new Exception(e);
		}
		return mapping.findForward(Constants.OBSERVATORY_SEED_LIST);
	}
}
