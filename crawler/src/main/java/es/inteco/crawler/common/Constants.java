package es.inteco.crawler.common;

public interface Constants {
    public static final int STATUS_NOT_LAUNCHED = 1;
    public static final int STATUS_LAUNCHED = 2;
    public static final int STATUS_STOPPED = 3;
    public static final int STATUS_FINALIZED = 4;
    public static final int STATUS_ERROR = 5;

    public static final String CRAWLER_JOB_NAME = "crawlerJob";
    public static final String CRAWLER_JOB_GROUP = "crawlerJobGroup";
    public static final String CRAWLER_JOB_TRIGGER = "crawlerJobTrigger";
    public static final String CRAWLER_JOB_TRIGGER_GROUP = "crawlerJobTriggerGroup";
    public static final String DEPTH = "depth";
    public static final String THREADS = "threads";
    public static final String TOPN = "topN";
    public static final String CAT = "cat";
    public static final String AS = "as";
    public static final String TEXT = "text";
    public static final String ID_CRAWLING = "idCrawling";
    public static final String ID_FULFILLED_CRAWLING = "idFulfilledCrawling";
    public static final String USER = "user";

    public static final String CRAWLER_DATA = "crawlerData";
    public static final String CUENTA_CLIENTE = "cuentaCliente";
    public static final String OBSERVATORY_ID = "observatoryId";
    public static final String OBSERVATORY = "observatory";
    public static final String CARTRIDGE_ID = "cartridgeId";

    public static final String EXECUTE_SCHEDULED_CRAWLING = "executeScheduledCrawling";
    public static final String EXECUTE_SCHEDULED_CRAWLING_TRIGGER = "executeScheduledCrawlingTrigger";
    public static final String SCHED_CRAWLER_JOB_NAME = "schedCrawlerJob";
    public static final String SCHED_CRAWLER_JOB_TRIGGER = "schedCrawlerJobTrigger";

    public static final String EXECUTE_SCHEDULED_OBSERVATORY = "executeScheduledObservatory";
    public static final String EXECUTE_SCHEDULED_OBSERVATORY_TRIGGER = "executeScheduledObservatoryTrigger";

    public static final int ID_LISTA_SEMILLA = 1;
    public static final int ID_LISTA_RASTREABLE = 2;
    public static final int ID_LISTA_NO_RASTREABLE = 3;
    public static final int ID_LISTA_OBSERVATORIO = 4;

    public static final int FINISHED_OBSERVATORY_STATUS = 0;
    public static final int LAUNCHED_OBSERVATORY_STATUS = 1;
    public static final int ERROR_OBSERVATORY_STATUS = 2;

    public static final int OBS_FINALIZED_STATUS = 0;
    public static final int OBS_LAUNCHED_STATUS = 1;
    public static final int OBS_ERROR_STATUS = 2;

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String XML_IGNORED_LINKS = "ignored_links";
    public static final String XML_LINK = "link";
    public static final String XML_TEXT = "text";
    public static final String XML_TITLE = "title";

}
