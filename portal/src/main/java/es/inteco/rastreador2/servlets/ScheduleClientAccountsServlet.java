package es.inteco.rastreador2.servlets;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.common.Constants;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.cuentausuario.CuentaCliente;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.job.ExecuteScheduledCrawling;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class ScheduleClientAccountsServlet extends GenericServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, es.inteco.common.Constants.ALL_DATA, es.inteco.common.Constants.CLIENT_ACCOUNT_TYPE);
            // cuentasCliente.addAll(CuentaUsuarioDAO.getClientAccounts(c, es.inteco.common.Constants.ALL_DATA, es.inteco.common.Constants.OBSERVATORY_TYPE));
            if (cuentasCliente != null && !cuentasCliente.isEmpty()) {
                ScheduleJobs(cuentasCliente);
            }
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para los rastreos de las cuentas de cliente y observatorios", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    @Override
    public void service(ServletRequest arg0, ServletResponse arg1)
            throws ServletException, IOException {
    }

    public static void scheduleJob(long idAccount, int type) {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, idAccount, type);
            if (cuentasCliente != null && !cuentasCliente.isEmpty()) {
                ScheduleJobs(cuentasCliente);
            }
        } catch (Exception e) {
            Logger.putLog("Error al programar los jobs para la cuenta cliente " + idAccount, ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private static void ScheduleJobs(List<CuentaCliente> cuentasCliente) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        for (CuentaCliente cuentaCliente : cuentasCliente) {
            if (cuentaCliente.isActive()) {
                ScheduleJob(scheduler, cuentaCliente);
            }
        }
    }

    private static void ScheduleJob(Scheduler scheduler, CuentaCliente cuentaCliente) {
        Logger.putLog("Programando el job para la cuenta " + cuentaCliente.getNombre(), ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_INFO);

        try {
            final String jobName = Constants.EXECUTE_SCHEDULED_CRAWLING + "_" + cuentaCliente.getIdCuenta() + "_" + cuentaCliente.getDatosRastreo().getId_rastreo();
            final JobDetail jobDetail = new JobDetail(jobName, "ExecuteScheduledCrawling", ExecuteScheduledCrawling.class);

            final JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(Constants.CUENTA_CLIENTE, cuentaCliente);
            jobDetail.setJobDataMap(jobDataMap);

            final Trigger trigger;
            final String triggerName = Constants.EXECUTE_SCHEDULED_CRAWLING_TRIGGER + "_" + cuentaCliente.getIdCuenta() + "_" + cuentaCliente.getDatosRastreo().getId_rastreo();
            if (StringUtils.isNotEmpty(cuentaCliente.getPeriodicidad().getCronExpression())) {
                String cronExpression = CrawlerUtils.getCronExpression(cuentaCliente.getFecha(), cuentaCliente.getPeriodicidad().getCronExpression());
                Logger.putLog("Estableciendo expresión de cron: " + cronExpression, ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_INFO);
                trigger = new CronTrigger(triggerName, Constants.CRAWLER_JOB_TRIGGER_GROUP, cronExpression);
            } else {
                trigger = new SimpleTrigger(triggerName,
                        Constants.CRAWLER_JOB_TRIGGER_GROUP, cuentaCliente.getFecha(), new Date(Long.MAX_VALUE), SimpleTrigger.REPEAT_INDEFINITELY,
                        (long) cuentaCliente.getPeriodicidad().getDias() * 24 * 60 * 60 * 1000);
            }

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            Logger.putLog("Error al programar el job", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void deleteJobs(long idAccount, int type) {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            List<CuentaCliente> cuentasCliente = CuentaUsuarioDAO.getClientAccounts(c, idAccount, type);
            if (cuentasCliente != null && !cuentasCliente.isEmpty()) {
                deleteJobs(cuentasCliente);
            }
        } catch (Exception e) {
            Logger.putLog("Error al eliminar los jobs asociados a cuentas clientes borradas", ScheduleClientAccountsServlet.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    private static void deleteJobs(List<CuentaCliente> cuentasCliente) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        for (CuentaCliente cuentaCliente : cuentasCliente) {
            scheduler.deleteJob(Constants.EXECUTE_SCHEDULED_CRAWLING + "_" + cuentaCliente.getIdCuenta() + "_" + cuentaCliente.getDatosRastreo().getId_rastreo(),
                    "ExecuteScheduledCrawling");
        }
    }
}
