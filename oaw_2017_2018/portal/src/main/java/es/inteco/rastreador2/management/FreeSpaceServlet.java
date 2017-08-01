package es.inteco.rastreador2.management;

import es.inteco.common.logging.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.*;
import java.io.IOException;

public class FreeSpaceServlet extends GenericServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            CronTrigger cronTrigger = new CronTrigger("FreeSpaceJob", "FreeSpaceJobGroup", getInitParameter("cronExpression"));
            JobDetail jobDetail = new JobDetail("FreeSpaceJob", "FreeSpaceJobGroup", FreeSpaceJob.class);
            scheduler.scheduleJob(jobDetail, cronTrigger);
            scheduler.start();
            Logger.putLog("Se ha programado el job para la comprobación de espacio libre en disco", FreeSpaceServlet.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception e) {
            Logger.putLog("Se ha producido un error al intentar crear el job para la comprobación de espacio libre en disco", FreeSpaceServlet.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {

    }
}
