package es.inteco.rastreador2.job;

import com.opensymphony.oscache.base.NeedsRefreshException;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;


public class DeleteTempDirJob implements StatefulJob {

    public static final Log LOG = LogFactory.getLog(DeleteTempDirJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        deleteFilesJob();
        //deleteObservatoryCacheJob();

    }

    public void deleteFilesJob() {
        Logger.putLog("Inicio del JOB que se encarga de borrar gráficas viejas", DeleteTempDirJob.class, Logger.LOG_LEVEL_INFO);
        PropertiesManager pmgr = new PropertiesManager();
        List<String> pathsToDelete = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();
            List<FulFilledCrawling> crawlings = RastreoDAO.getOldCrawlings(conn, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "num.days.old.charts")));

            if (crawlings != null && !crawlings.isEmpty()) {
                Logger.putLog("Se han encontrado rastreos viejos cuyas gráficas van a ser borradas", DeleteTempDirJob.class, Logger.LOG_LEVEL_INFO);
                String path = "";
                for (FulFilledCrawling crawling : crawlings) {
                    if (crawling.getIdCartridge() == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
                        //Se borran las gráficas de rastreo de INTAV
                        path = pmgr.getValue(CRAWLER_PROPERTIES, "path.general.intav.chart.files");
                        pathsToDelete.add(path + File.separator + crawling.getIdCrawling() + File.separator + crawling.getId());
                        //Se borran las gráficas del Observatorio de INTAV
                        path = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.intav.files");
                        pathsToDelete.add(path + File.separator + crawling.getIdObservatory() + File.separator + crawling.getIdFulfilledObservatory());
                        //Se borra el PDF de rastreo del observatorio INTAV
                        path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav");
                        pathsToDelete.add(path + crawling.getIdCrawling() + File.separator + crawling.getId());
                        //Se borra el PDF de resultaods primarios de INTAV
                        path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.intav");
                        pathsToDelete.add(path + crawling.getIdCrawling() + File.separator + crawling.getId());
                    } else if (crawling.getIdCartridge() == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
                        //Se borran las gráficas del Observatorio de LENOX
                        path = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.lenox.files");
                        pathsToDelete.add(path + File.separator + crawling.getIdObservatory() + File.separator + crawling.getIdFulfilledObservatory());
                        //Se borra el PDF de rastreo de LENOX
                        path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.lenox");
                        pathsToDelete.add(path + crawling.getIdCrawling() + File.separator + crawling.getId());
                    }
                }
                FileUtils.deleteDirs(pathsToDelete);
            } else {
                Logger.putLog("No se han encontrado rastreos viejos", DeleteTempDirJob.class, Logger.LOG_LEVEL_INFO);
            }
        } catch (Exception e) {
            Logger.putLog("Se ha producido un error al intentar borrar los directorios temporales", DeleteTempDirJob.class, Logger.LOG_LEVEL_INFO, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }

    public void deleteObservatoryCacheJob() {
        Logger.putLog("Inicio del JOB que se encarga de borrar la caché expirada del observatorio", DeleteTempDirJob.class, Logger.LOG_LEVEL_INFO);

        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();

            List<ObservatorioRealizadoForm> fulfilledObservatories = ObservatorioDAO.getAllFulfilledObservatories(conn);

            for (ObservatorioRealizadoForm fulfilledObservatory : fulfilledObservatories) {
                String keyCache = Constants.OBSERVATORY_KEY_CACHE + fulfilledObservatory.getId();
                try {
                    CacheUtils.getFromCache(keyCache);
                } catch (NeedsRefreshException e) {
                    Logger.putLog("La caché de la ejecución " + fulfilledObservatory.getId() + " ha caducado. Se va a proceder a borrar", DeleteTempDirJob.class, Logger.LOG_LEVEL_INFO);
                    CacheUtils.removeFromCache(keyCache);
                }
            }

        } catch (Exception e) {
            Logger.putLog("Se ha producido un error al intentar borrar los archivos expirados de caché", DeleteTempDirJob.class, Logger.LOG_LEVEL_INFO, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }

}
