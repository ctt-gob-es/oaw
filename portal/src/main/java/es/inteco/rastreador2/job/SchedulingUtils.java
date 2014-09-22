package es.inteco.rastreador2.job;

import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;

public final class SchedulingUtils {

    private SchedulingUtils() {
    }

    /*public static void start(CrawlerData crawlerData) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		
		JobDetail jobDetail = new JobDetail(Constants.SCHED_CRAWLER_JOB_NAME + "_" + crawlerData.getIdCrawling(), Constants.CRAWLER_JOB_GROUP, CrawlerJob.class);
		
		JobDataMap jobDataMap2 = new JobDataMap();
		jobDataMap2.put(Constants.CRAWLER_DATA, crawlerData);
		jobDetail.setJobDataMap(jobDataMap2);
		
		Trigger trigger = new SimpleTrigger(Constants.SCHED_CRAWLER_JOB_TRIGGER + "_" + crawlerData.getIdCrawling(), Constants.CRAWLER_JOB_TRIGGER_GROUP, new java.util.Date());
		scheduler.scheduleJob(jobDetail, trigger);
		scheduler.start();
	}*/

    public static void start(CrawlerData crawlerData) throws Exception {
        CrawlerJob crawlerJob = new CrawlerJob();
        crawlerJob.launchCrawler(crawlerData);
    }

}
