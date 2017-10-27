package es.inteco.rastreador2.action.observatorio.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.bouncycastle.jce.provider.JCEStreamCipher.IDEA_OFB8;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.login.DatosForm;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.job.ExecuteScheduledObservatory;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.RastreoUtils;

public class RelanzarObservatorioThread extends Thread {

	private final String idObservatorio;
	private final String idEjecucionObservatorio;

	public RelanzarObservatorioThread(final String idObservatorio, final String idEjecucionObservatorio) {
		super("RelanzarObservatorioThread");
		this.idObservatorio = idObservatorio;
		this.idEjecucionObservatorio = idEjecucionObservatorio;
	}

	@Override
	public void run() {

		Connection c = null;

		try {
			c = DataBaseManager.getConnection();
			c.setAutoCommit(false);

			// TODO Recuperamos los rastreos pendentes de este
			// observatorio

			List<Long> pendindCrawlings = RastreoDAO.getPendingCrawlerFromSeedAndObservatory(c, Long.parseLong(idObservatorio), Long.parseLong(idEjecucionObservatorio));
			
			// TODO Cambiar el estado del observatorio a lanzado
			ObservatorioDAO.updateObservatoryStatus(c, Long.parseLong(idObservatorio), es.inteco.crawler.common.Constants.LAUNCHED_OBSERVATORY_STATUS);

			if (pendindCrawlings != null && !pendindCrawlings.isEmpty()) {

			

				for (Long idCrawling : pendindCrawlings) {

					// Borramos la cache
					CacheUtils.removeFromCache(Constants.OBSERVATORY_KEY_CACHE + idEjecucionObservatorio);

					// Si existe un rastreo antiguo, lo eliminamos
					Long idOldExecution = RastreoDAO.getExecutedCrawlerId(c, idCrawling, Long.parseLong(idEjecucionObservatorio));
					if (idOldExecution != null) {
						RastreoUtils.borrarArchivosAsociados(c, String.valueOf(idOldExecution));
						RastreoDAO.borrarRastreoRealizado(c, idOldExecution);
					}

					// Lanzamos el rastreo y recuperamos el id de
					// ejecución
					lanzarRastreo(c, String.valueOf(idCrawling));
					Long idNewExecution = Long.valueOf(RastreoDAO.getExecutedCrawling(c, idCrawling, RastreoDAO.getIdSeedByIdRastreo(c, idCrawling)).getId());
					RastreoDAO.setObservatoryExecutionToCrawlerExecution(c, Long.parseLong(idEjecucionObservatorio), idNewExecution);

					RastreoDAO.actualizarEstadoRastreo(c, idCrawling.intValue(), es.inteco.crawler.common.Constants.STATUS_FINALIZED);

					c.commit();
				}
				
			

			}
			
			
			ObservatorioDAO.updateObservatoryStatus(c, Long.parseLong(idEjecucionObservatorio), es.inteco.crawler.common.Constants.FINISHED_OBSERVATORY_STATUS);
			Logger.putLog("Finalizado el observatorio con id " + idEjecucionObservatorio, RelanzarObservatorioThread.class, Logger.LOG_LEVEL_INFO);
			c.commit();

		} catch (Exception e) {
			Logger.putLog("Error: ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			if (c != null) {
				try {
					c.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} 
	}

	/**
	 * Lanza un rastreo
	 * 
	 * @param c
	 * @param idCrawling
	 * @throws Exception
	 */
	private void lanzarRastreo(final Connection c, final String idCrawling) throws Exception {
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

		final Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, dcrForm, null, Long.valueOf(userData.getId()));

		final CrawlerJob crawlerJob = new CrawlerJob();
		crawlerJob.makeCrawl(CrawlerUtils.getCrawlerData(dcrForm, idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null));
	}
}
