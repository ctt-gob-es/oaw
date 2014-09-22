package es.inteco.rastreador2.servlets;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.common.Constants;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.PeriodicidadForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.job.ExecuteScheduledObservatory;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class ScheduleObservatoryServlet extends GenericServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Logger.putLog("Programando los observatorios", ScheduleObservatoryServlet.class, Logger.LOG_LEVEL_INFO);

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            List<ObservatorioForm> observatories = ObservatorioDAO.getObservatoryList(c);

            for (ObservatorioForm observatory : observatories) {
                scheduleJob(observatory.getNombre(), observatory.getId(),
                        new Date(observatory.getFecha_inicio().getTime()), observatory.getPeriodicidadForm(), observatory.getCartucho().getId());
            }
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para los rastreos de las cuentas de cliente y observatorios", ScheduleObservatoryServlet.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    @Override
    public void service(ServletRequest arg0, ServletResponse arg1)
            throws ServletException, IOException {
    }

    public static void scheduleJob(String observatoryName, Long observatoryId, Date observatoryDate, PeriodicidadForm periodicidadForm, Long idCartridge) {
        Logger.putLog("Programando el job para el observatorio " + observatoryName, ScheduleObservatoryServlet.class, Logger.LOG_LEVEL_INFO);

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();

            JobDetail jobDetail = new JobDetail(Constants.EXECUTE_SCHEDULED_OBSERVATORY + "_" + observatoryId,
                    "ExecuteScheduledObservatory",
                    ExecuteScheduledObservatory.class);

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(Constants.OBSERVATORY_ID, observatoryId);
            jobDataMap.put(Constants.CARTRIDGE_ID, idCartridge);
            jobDetail.setJobDataMap(jobDataMap);

            final Trigger trigger;
            final String triggerName = Constants.EXECUTE_SCHEDULED_OBSERVATORY_TRIGGER + "_" + observatoryId;
            if (StringUtils.isNotEmpty(periodicidadForm.getCronExpression())) {
                String cronExpression = CrawlerUtils.getCronExpression(observatoryDate, periodicidadForm.getCronExpression());
                Logger.putLog("Estableciendo expresión de cron: " + cronExpression, ScheduleObservatoryServlet.class, Logger.LOG_LEVEL_INFO);
                trigger = new CronTrigger(triggerName, Constants.CRAWLER_JOB_TRIGGER_GROUP, cronExpression);
            } else {
                trigger = new SimpleTrigger(triggerName,
                        Constants.CRAWLER_JOB_TRIGGER_GROUP, observatoryDate, new Date(Long.MAX_VALUE), SimpleTrigger.REPEAT_INDEFINITELY,
                        (long) periodicidadForm.getDias() * 24 * 60 * 60 * 1000);
            }

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            Logger.putLog("Error al programar el job", ScheduleObservatoryServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void deleteJob(Long observatoryId) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        scheduler.deleteJob(Constants.EXECUTE_SCHEDULED_OBSERVATORY + "_" + observatoryId, "ExecuteScheduledObservatory");
    }

}
