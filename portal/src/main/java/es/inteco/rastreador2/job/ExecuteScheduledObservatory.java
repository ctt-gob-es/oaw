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
package es.inteco.rastreador2.job;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.Constants.MAIL_PROPERTIES;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.quartz.UnableToInterruptJobException;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.gob.oaw.MailException;
import es.gob.oaw.MailService;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.dao.EstadoObservatorioDAO;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.action.observatorio.utils.RelanzarObservatorioThread;
import es.inteco.rastreador2.actionform.cuentausuario.CuentaCliente;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.RastreoUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;

/**
 * The Class ExecuteScheduledObservatory.
 */
public class ExecuteScheduledObservatory implements StatefulJob, InterruptableJob {
	/** The interrupted. */
	private boolean interrupted = false;
	/** The crawler job. */
	private CrawlerJob crawlerJob;
	/** The observatory id. */
	private Long observatoryId;

	/**
	 * Execute.
	 *
	 * @param context the context
	 * @throws JobExecutionException the job execution exception
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		final Long cartridgeId = (Long) jobDataMap.get(Constants.CARTRIDGE_ID);
		observatoryId = (Long) jobDataMap.get(Constants.OBSERVATORY_ID);
		Logger.putLog("Lanzando la ejecución del observatorio con id " + observatoryId, ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_INFO);
		final PropertiesManager pmgr = new PropertiesManager();
		String url = "";
		try {
			Connection c = DataBaseManager.getConnection();
			// Si se ha editado la categoría de semillas para añadir más, se
			// añaden ahora.
			createNewCrawlings(c, observatoryId);
			Long idFulfilledObservatory = RastreoDAO.addFulfilledObservatory(c, observatoryId, cartridgeId);
			try {
				List<CuentaCliente> observatories = CuentaUsuarioDAO.getClientAccounts(c, observatoryId, es.inteco.common.Constants.OBSERVATORY_TYPE);
				boolean isFirst = true;
				int counter = 0;
				ObservatorioDAO.updateObservatoryStatus(c, idFulfilledObservatory, Constants.LAUNCHED_OBSERVATORY_STATUS);
				for (CuentaCliente observatory : observatories) {
					if (!interrupted) {
						// Volvemos a generarla conexión por si tarda mucho el
						// proceso de rastreo y se ha cerrado
						c = DataBaseManager.getConnection();
						try {
							Logger.putLog("Lanzando observatorio para la semilla número " + (++counter), ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_INFO);
							Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, observatory.getDatosRastreo(), idFulfilledObservatory, (long) 1);
							observatory.getDatosRastreo().setCartuchos(CartuchoDAO.getNombreCartucho(observatory.getDatosRastreo().getId_rastreo()));
							observatory.getDatosRastreo()
									.setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_OBSERVATORIO, false));
							observatory.getDatosRastreo()
									.setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_OBSERVATORIO, true));
							observatory.getDatosRastreo()
									.setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
							observatory.getDatosRastreo()
									.setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));
							observatory.getDatosRastreo().setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) observatory.getDatosRastreo().getId_rastreo()));
							if (CartuchoDAO.isCartuchoAccesibilidad(c, observatory.getDatosRastreo().getId_cartucho())) {
								String ficheroNorma = CrawlerUtils.getFicheroNorma(observatory.getDatosRastreo().getId_guideline());
								observatory.getDatosRastreo().setFicheroNorma(ficheroNorma);
							}
							CrawlerData crawlerData = CrawlerUtils.getCrawlerData(observatory.getDatosRastreo(), idFulfilledCrawling,
									pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null);
							url = crawlerData.getUrls().get(0);
							RastreoDAO.actualizarFechaRastreo(c, crawlerData.getIdCrawling());
							if (isFirst) {
								if (CartuchoDAO.isCartuchoAccesibilidad(c, observatory.getDatosRastreo().getId_cartucho())) {
									String ficheroNorma = CrawlerUtils.getFicheroNorma(observatory.getDatosRastreo().getId_guideline());
									saveMethodology(c, idFulfilledObservatory, ficheroNorma);
								}
								isFirst = false;
							}
							DataBaseManager.closeConnection(c);
							crawlerJob = new CrawlerJob();
							crawlerJob.launchCrawler(crawlerData);
							// SchedulingUtils.start(crawlerData, crawlerJob );
						} catch (Exception e) {
							Logger.putLog("Se ha producido una Excepcion. ", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_WARNING, e);
						} catch (OutOfMemoryError e) {
							Logger.putLog("Se ha producido un OutOfMemory. ", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_WARNING);
							administratorErrorMail(e, url);
						}
					} else {
						c = DataBaseManager.getConnection();
						ObservatorioDAO.updateObservatoryStatus(c, idFulfilledObservatory, Constants.STOPPED_OBSERVATORY_STATUS);
						DataBaseManager.closeConnection(c);
						break;
					}
				}
				// Ends observatory execution
				if (!interrupted) {
					if (c != null && c.isClosed()) {
						c = DataBaseManager.getConnection();
					}
					// Relaunch not crawled seeds
					if (ObservatorioDAO.getAutorelaunchFromConfig(c) == 1) {
						List<Long> finishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis = ObservatorioDAO.getFinishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis(c, observatoryId,
								idFulfilledObservatory);
						// Get seed less pages umbral
						List<Long> lessThreshbold = ObservatorioDAO.getFinishCrawlerIdsFromSeedAndObservatoryWithLessResultsThreshold(c, idFulfilledObservatory, null, null);
						List<Long> allToRelaunch = new ArrayList<Long>();
						allToRelaunch.addAll(finishCrawlerIdsFromSeedAndObservatoryWithoutAnalisis);
						allToRelaunch.addAll(lessThreshbold);
						relaunchUnfinished(allToRelaunch, observatoryId, idFulfilledObservatory);
					}
					// Mark as finished
					ObservatorioDAO.updateObservatoryStatus(c, idFulfilledObservatory, Constants.FINISHED_OBSERVATORY_STATUS);
					// Generate cache add observatory ends
					ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idFulfilledObservatory), 0, null);
					final List<ResultadoSemillaFullForm> seedsResults2 = ObservatorioDAO.getResultSeedsFullFromObservatory(c, new SemillaForm(), idFulfilledObservatory, 0L, -1);
					ObservatoryUtils.setAvgScore2(c, seedsResults2, idFulfilledObservatory);
					Logger.putLog("Finalizado el observatorio con id " + observatoryId, ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_INFO);
					DataBaseManager.closeConnection(c);
				}
			} catch (SQLException e) {
				c = DataBaseManager.getConnection();
				ObservatorioDAO.updateObservatoryStatus(c, idFulfilledObservatory, Constants.ERROR_OBSERVATORY_STATUS);
				DataBaseManager.closeConnection(c);
			}
		} catch (Exception e) {
			Logger.putLog("Error ejecutar los rastreos programados del observatorio", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Relaunch unfinished.
	 *
	 * @param pendindCrawlings        the pendind crawlings
	 * @param idObservatorio          the id observatorio
	 * @param idEjecucionObservatorio the id ejecucion observatorio
	 */
	private void relaunchUnfinished(final List<Long> pendindCrawlings, final Long idObservatorio, final Long idEjecucionObservatorio) {
		Connection c = null;
		try {
			c = DataBaseManager.getConnection();
			c.setAutoCommit(false);
			EstadoObservatorioDAO.deleteEstado(c, idObservatorio.intValue(), idEjecucionObservatorio.intValue());
			if (pendindCrawlings != null && !pendindCrawlings.isEmpty()) {
				Logger.putLog("OBSERVATORIO FINALIZADO - RELANZAMIENTO AUTOMÁTICO DE " + pendindCrawlings.size() + " RASTREOS", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_INFO);
				for (Long idCrawling : pendindCrawlings) {
					if (!interrupted) {
						try {
							RastreoDAO.actualizarEstadoRastreo(c, idCrawling.intValue(), es.inteco.crawler.common.Constants.STATUS_LAUNCHED);
							// Borramos la cache
							CacheUtils.removeFromCache(es.inteco.common.Constants.OBSERVATORY_KEY_CACHE + idEjecucionObservatorio);
							// Si existe un rastreo antiguo, lo eliminamos
							Long idOldExecution = RastreoDAO.getExecutedCrawlerId(c, idCrawling, idEjecucionObservatorio);
							if (idOldExecution != null) {
								RastreoUtils.borrarArchivosAsociados(c, String.valueOf(idOldExecution));
								RastreoDAO.borrarRastreoRealizado(c, idOldExecution);
							}
							c.commit();
							// Lanzamos el rastreo y recuperamos el id de
							// ejecución
							lanzarRastreo(String.valueOf(idCrawling), idObservatorio, idEjecucionObservatorio);
							// Por si tarda mucho en acabar el rastreo, volvemos a
							// inicializar una conexion
							c = DataBaseManager.getConnection();
							c.setAutoCommit(false);
							Long idNewExecution = Long.valueOf(RastreoDAO.getExecutedCrawling(c, idCrawling, RastreoDAO.getIdSeedByIdRastreo(c, idCrawling)).getId());
							RastreoDAO.setObservatoryExecutionToCrawlerExecution(c, idEjecucionObservatorio, idNewExecution);
							RastreoDAO.actualizarEstadoRastreo(c, idCrawling.intValue(), es.inteco.crawler.common.Constants.STATUS_FINALIZED);
							// Calculate scores
							ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idEjecucionObservatorio), 0, null);
							final List<ResultadoSemillaFullForm> seedsResults2 = ObservatorioDAO.getResultSeedsFullFromObservatory(c, new SemillaForm(), idEjecucionObservatorio, 0L, -1);
							ObservatoryUtils.setAvgScore2(c, seedsResults2, idEjecucionObservatorio);
							c.commit();
						} catch (Exception e) {
							Logger.putLog("Error al relanzar el rastreo  " + idCrawling, ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
						}
					} else {
						Logger.putLog("Relanzamiento parado" + idCrawling, ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR);
					}
				}
			} else {
				Logger.putLog("No se han encontrado rastreos que relanzar", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_INFO);
			}
			if (!interrupted) {
				ObservatorioDAO.updateObservatoryStatus(DataBaseManager.getConnection(), idEjecucionObservatorio, es.inteco.crawler.common.Constants.FINISHED_OBSERVATORY_STATUS);
			} else {
				ObservatorioDAO.updateObservatoryStatus(DataBaseManager.getConnection(), idEjecucionObservatorio, es.inteco.crawler.common.Constants.STOPPED_OBSERVATORY_STATUS);
			}
			Logger.putLog("Finalizado el observatorio con id " + idEjecucionObservatorio, RelanzarObservatorioThread.class, Logger.LOG_LEVEL_INFO);
			c.commit();
		} catch (Exception e) {
			Logger.putLog("Error al relanzar el observatorio ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			if (c != null) {
				try {
					c.rollback();
				} catch (SQLException e1) {
					Logger.putLog("Error al realizar rollback", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		} finally {
			if (c != null) {
				try {
					c.rollback();
					DataBaseManager.closeConnection(c);
				} catch (SQLException e1) {
					Logger.putLog("Error al realizar rollback", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e1);
				}
			}
		}
	}

	/**
	 * Lanza un rastreo.
	 *
	 * @param idCrawling              the id crawling
	 * @param idObservatorio          the id observatorio
	 * @param idEjecucionObservatorio the id ejecucion observatorio
	 * @throws Exception the exception
	 */
	private void lanzarRastreo(final String idCrawling, final Long idObservatorio, final Long idEjecucionObservatorio) throws Exception {
		Logger.putLog("Realanzado el rastreo " + idCrawling, ResultadosObservatorioAction.class, Logger.LOG_LEVEL_INFO);
		Connection c = DataBaseManager.getConnection();
		final PropertiesManager pmgr = new PropertiesManager();
		final DatosCartuchoRastreoForm dcrForm = RastreoDAO.cargarDatosCartuchoRastreo(c, idCrawling);
		dcrForm.setCartuchos(CartuchoDAO.getNombreCartucho(dcrForm.getId_rastreo()));
		// Cargamos los dominios introducidos en el archivo de semillas
		final int typeDomains = dcrForm.getIdObservatory() == 0 ? Constants.ID_LISTA_SEMILLA : es.inteco.common.Constants.ID_LISTA_SEMILLA_OBSERVATORIO;
		// todo ADD ID OBS TO CREATE AN IDENTIFICABLE GROUPNAME
		dcrForm.setIdObservatory(idObservatorio);
		dcrForm.setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, false));
		dcrForm.setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), typeDomains, true));
		dcrForm.setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
		dcrForm.setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) dcrForm.getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));
		dcrForm.setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) dcrForm.getId_rastreo()));
		if (CartuchoDAO.isCartuchoAccesibilidad(c, dcrForm.getId_cartucho())) {
			dcrForm.setFicheroNorma(CrawlerUtils.getFicheroNorma(dcrForm.getId_guideline()));
		}
		final DatosForm userData = LoginDAO.getUserDataByName(c, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"));
		final Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, dcrForm, idEjecucionObservatorio, Long.valueOf(userData.getId()));
		final CrawlerData crawlerData = CrawlerUtils.getCrawlerData(dcrForm, idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null);
		// set extend timeout
		crawlerData.setExtendTimeout(true);
		crawlerData.setExtendedTimeoutValue(ObservatorioDAO.getTimeoutFromConfig(c));
		crawlerData.setExtendedDepth(ObservatorioDAO.getDepthFromConfig(c));
		crawlerData.setExtendedWidth(ObservatorioDAO.getWidthFromConfig(c));
		crawlerJob = new CrawlerJob();
		crawlerJob.launchCrawler(crawlerData);
		DataBaseManager.closeConnection(c);
	}

	/**
	 * Administrator error mail.
	 *
	 * @param exception the exception
	 * @param url       the url
	 * @throws SQLException the SQL exception
	 */
	private void administratorErrorMail(Throwable exception, String url) throws SQLException {
		PropertiesManager pmgr = new PropertiesManager();
		List<String> adminMails = DAOUtils.getMailsByRol(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.administrator.id")));
		String alertSubject = pmgr.getValue(MAIL_PROPERTIES, "alert.from.subject");
		String alertText = pmgr.getValue(MAIL_PROPERTIES, "warning.administrator.message") + exception.getMessage();
		if (url != null && !url.isEmpty()) {
			alertText += "\n" + pmgr.getValue(CRAWLER_PROPERTIES, "url.administrator.message") + url;
		}
		alertText += "\n\n" + pmgr.getValue(MAIL_PROPERTIES, "warning.administrator.signature");
		MailService mailService = new MailService();
		try {
			mailService.sendMail(adminMails, alertSubject, alertText);
		} catch (MailException e) {
			Logger.putLog("Fallo al enviar el correo", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Creates the new crawlings.
	 *
	 * @param conn          the conn
	 * @param observatoryId the observatory id
	 */
	private void createNewCrawlings(final Connection conn, final long observatoryId) {
		try {
			final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(conn, observatoryId);
			final List<SemillaForm> totalSeedsAdded = new ArrayList<>();
			if (observatorioForm.getCategoria() != null) {
				for (String categoria : observatorioForm.getCategoria()) {
					String[] tags = {};
					if (!org.apache.commons.lang3.StringUtils.isEmpty(observatorioForm.getTagsString())) {
						tags = observatorioForm.getTagsString().split(",");
					}
					totalSeedsAdded.addAll(SemillaDAO.getSeedsObservatory(conn, Long.parseLong(categoria), -1, tags));
				}
			}
			ObservatorioDAO.deleteSeedAssociation(conn, observatoryId);
			ObservatorioDAO.updateCrawlers(conn, observatoryId, totalSeedsAdded);
		} catch (Exception e) {
			Logger.putLog("Error añadir los rastreos nuevos al observatorio", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Save methodology.
	 *
	 * @param c               the c
	 * @param idExecution     the id execution
	 * @param methodologyFile the methodology file
	 */
	private void saveMethodology(final Connection c, Long idExecution, String methodologyFile) {
		try {
			if (!EvaluatorUtility.isInitialized()) {
				EvaluatorUtility.initialize();
			}
			final String methodology = EvaluatorUtils.serializeGuidelineToXml(EvaluatorUtility.loadGuideline(methodologyFile));
			ObservatorioDAO.saveMethodology(c, idExecution, methodology);
		} catch (Exception e) {
			Logger.putLog("Error al guardar la metodología en base de datos", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Interrupt.
	 *
	 * @throws UnableToInterruptJobException the unable to interrupt job exception
	 */
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		this.interrupted = true;
		this.crawlerJob.interrupt();
		Logger.putLog("Lanzando la ejecución del observatorio con id " + observatoryId, ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_INFO);
	}
}
