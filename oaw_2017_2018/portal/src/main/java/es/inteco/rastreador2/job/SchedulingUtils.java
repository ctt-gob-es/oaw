package es.inteco.rastreador2.job;

import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;

public final class SchedulingUtils {

    private SchedulingUtils() {
    }

    public static void start(final CrawlerData crawlerData) throws Exception {
        final CrawlerJob crawlerJob = new CrawlerJob();
        crawlerJob.launchCrawler(crawlerData);
    }

}
