package es.inteco.rastreador2.servlets;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.job.DeleteTempDirJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.*;
import java.io.IOException;

public class DeleteTempDirServlet extends GenericServlet {

    private Scheduler scheduler;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);

        try {
            final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();

            final CronTrigger cronTrigger = new CronTrigger("DeleteFilesJob", "DeleteFilesJobGroup", getInitParameter("cronExpression"));
            final JobDetail jobDetail = new JobDetail("DeleteFilesJob", "DeleteFilesJobGroup", DeleteTempDirJob.class);
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
            Logger.putLog("Se ha programado el job para el borrado de directorios temporales", DeleteTempDirServlet.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception e) {
            Logger.putLog("Se ha producido un error al intentar crear el job para el borrado de directorios temporales", DeleteTempDirServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void destroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            Logger.putLog("FALLO al finalizar los Jobs (puede producir fallos de memoria)", DeleteTempDirServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
        super.destroy();
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    }

}
