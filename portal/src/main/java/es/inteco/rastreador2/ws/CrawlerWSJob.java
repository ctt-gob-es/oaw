package es.inteco.rastreador2.ws;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.ws.commons.Constants;
import es.inteco.rastreador2.ws.utils.CrawlerWSUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

public class CrawlerWSJob implements StatefulJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        BasicServiceForm basicServiceForm = (BasicServiceForm) jobDataMap.get(Constants.BASIC_SERVICE_FORM);

        Logger.putLog("Lanzando el rastreo programado para la URL " + basicServiceForm.getDomain(), CrawlerWSJob.class, Logger.LOG_LEVEL_INFO);
        CrawlerWSUtils.webCrawl(basicServiceForm);
    }

}