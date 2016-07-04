package es.inteco.rastreador2.job;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CuentaCliente;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.utils.MailUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ExecuteScheduledObservatory implements StatefulJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        final Long observatoryId = (Long) jobDataMap.get(Constants.OBSERVATORY_ID);
        final Long cartridgeId = (Long) jobDataMap.get(Constants.CARTRIDGE_ID);

        Logger.putLog("Lanzando la ejecución del observatorio con id " + observatoryId, ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_INFO);

        final PropertiesManager pmgr = new PropertiesManager();

        Connection c = null;
        String url = "";
        try {
            c = DataBaseManager.getConnection();

            // Si se ha editado la categoría de semillas para añadir más, se añaden ahora.
            createNewCrawlings(c, observatoryId);

            Long idFulfilledObservatory = RastreoDAO.addFulfilledObservatory(c, observatoryId, cartridgeId);

            try {
                List<CuentaCliente> observatories = CuentaUsuarioDAO.getClientAccounts(c, observatoryId, es.inteco.common.Constants.OBSERVATORY_TYPE);

                boolean isFirst = true;
                int counter = 0;
                ObservatorioDAO.updateObservatoryStatus(c, idFulfilledObservatory, Constants.LAUNCHED_OBSERVATORY_STATUS);
                for (CuentaCliente observatory : observatories) {
                    try {
                        Logger.putLog("Lanzando observatorio para la semilla número " + (++counter), ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_INFO);
                        Long idFulfilledCrawling = RastreoDAO.addFulfilledCrawling(c, observatory.getDatosRastreo(), idFulfilledObservatory, (long) 1);

                        observatory.getDatosRastreo().setCartuchos(CartuchoDAO.getNombreCartucho(observatory.getDatosRastreo().getId_rastreo()));
                        observatory.getDatosRastreo().setUrls(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_OBSERVATORIO, false));
                        observatory.getDatosRastreo().setDomains(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_OBSERVATORIO, true));
                        observatory.getDatosRastreo().setExceptions(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_NO_RASTREABLE, false));
                        observatory.getDatosRastreo().setCrawlingList(es.inteco.utils.CrawlerUtils.getDomainsList((long) observatory.getDatosRastreo().getId_rastreo(), Constants.ID_LISTA_RASTREABLE, false));
                        observatory.getDatosRastreo().setId_guideline(es.inteco.plugin.dao.RastreoDAO.recuperarIdNorma(c, (long) observatory.getDatosRastreo().getId_rastreo()));
                        if (CartuchoDAO.isCartuchoAccesibilidad(c, observatory.getDatosRastreo().getId_cartucho())) {
                            String ficheroNorma = CrawlerUtils.getFicheroNorma(observatory.getDatosRastreo().getId_guideline());
                            observatory.getDatosRastreo().setFicheroNorma(ficheroNorma);
                        }

                        CrawlerData crawlerData = CrawlerUtils.getCrawlerData(observatory.getDatosRastreo(),
                                idFulfilledCrawling, pmgr.getValue(CRAWLER_PROPERTIES, "scheduled.crawlings.user.name"), null);
                        url = crawlerData.getUrls().get(0);
                        RastreoDAO.updateRastreo(c, crawlerData.getIdCrawling());


                        if (isFirst) {
                            if (CartuchoDAO.isCartuchoAccesibilidad(c, observatory.getDatosRastreo().getId_cartucho())) {
                                String ficheroNorma = CrawlerUtils.getFicheroNorma(observatory.getDatosRastreo().getId_guideline());
                                saveMethodology(idFulfilledObservatory, ficheroNorma);
                            }
                            isFirst = false;
                        }

                        SchedulingUtils.start(crawlerData);
                    } catch (Exception e) {
                        Logger.putLog("Se ha producido una Excepcion. ", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_WARNING, e);
                    } catch (OutOfMemoryError e) {
                        Logger.putLog("Se ha producido un OutOfMemory. ", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_WARNING);
                        administratorErrorMail(e, url);
                    }
                }
                ObservatorioDAO.updateObservatoryStatus(c, idFulfilledObservatory, Constants.FINISHED_OBSERVATORY_STATUS);
                Logger.putLog("Finalizado el observatorio con id " + observatoryId, ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_INFO);
            } catch (SQLException e) {
                ObservatorioDAO.updateObservatoryStatus(c, idFulfilledObservatory, Constants.ERROR_OBSERVATORY_STATUS);
            }
        } catch (Exception e) {
            Logger.putLog("Error ejecutar los rastreos programados del observatorio", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private void administratorErrorMail(Throwable exception, String url) throws SQLException {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> adminMails = DAOUtils.getMailsByRol(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.administrator.id")));
        String alertSubject = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.subject");
        String alertText = pmgr.getValue(CRAWLER_PROPERTIES, "warning.administrator.message") + exception.getMessage();
        if (url != null && !url.isEmpty()) {
            alertText += "\n" + pmgr.getValue(CRAWLER_PROPERTIES, "url.administrator.message") + url;
        }
        String alertFromAddress = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.address");
        String alertFromName = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.name");

        MailUtils.sendSimpleMail(alertFromAddress, alertFromName, adminMails, alertSubject, alertText);
    }

    private void createNewCrawlings(final Connection conn, final long observatoryId) {
        try {
            final ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(conn, observatoryId);

            final List<SemillaForm> totalSeedsAdded = new ArrayList<SemillaForm>();
            if (observatorioForm.getCategoria() != null) {
                for (String categoria : observatorioForm.getCategoria()) {
                    totalSeedsAdded.addAll(SemillaDAO.getSeedsByCategory(conn, Long.parseLong(categoria), es.inteco.common.Constants.NO_PAGINACION, new SemillaForm()));
                }
                ObservatorioDAO.insertNewCrawlers(conn, observatoryId, totalSeedsAdded);
            }
        } catch (Exception e) {
            Logger.putLog("Error añadir los rastreos nuevos al observatorio", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private void saveMethodology(Long idExecution, String methodologyFile) {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            if (!EvaluatorUtility.isInitialized()) {
                EvaluatorUtility.initialize();
            }
            String methodology = EvaluatorUtils.serializeGuidelineToXml(EvaluatorUtility.loadGuideline(methodologyFile));
            ObservatorioDAO.saveMethodology(c, idExecution, methodology);
        } catch (Exception e) {
            Logger.putLog("Error al guardar la metodología en base de datos", ExecuteScheduledObservatory.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

}
