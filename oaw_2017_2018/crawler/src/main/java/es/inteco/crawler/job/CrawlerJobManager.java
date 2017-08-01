package es.inteco.crawler.job;

import es.inteco.crawler.common.Constants;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

public final class CrawlerJobManager {
    private static Scheduler scheduler;

    private CrawlerJobManager() {
    }

    private static void init() throws Exception {
        if (scheduler == null) {
            final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();
        }
    }

    public static void startJob(CrawlerData crawlerData) throws Exception {
        init();
        final JobDetail jobDetail = new JobDetail(Constants.CRAWLER_JOB_NAME + "_" + crawlerData.getIdCrawling(), Constants.CRAWLER_JOB_GROUP, CrawlerJob.class);

        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(Constants.CRAWLER_DATA, crawlerData);

        jobDetail.setJobDataMap(jobDataMap);

        final Trigger trigger = new SimpleTrigger(Constants.CRAWLER_JOB_TRIGGER + "_" + crawlerData.getIdCrawling(), Constants.CRAWLER_JOB_TRIGGER_GROUP, new Date());

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static void endJob(long idJob) throws Exception {
        init();
        scheduler.interrupt(Constants.CRAWLER_JOB_NAME + "_" + idJob, Constants.CRAWLER_JOB_GROUP);
        scheduler.deleteJob(Constants.CRAWLER_JOB_NAME + "_" + idJob, Constants.CRAWLER_JOB_GROUP);
    }
}
