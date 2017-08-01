package es.inteco.rastreador2.utils.basic.service;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

import java.util.Random;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class BasicServiceConcurrenceSystem extends Thread {

    private static int concurrentUsers = 0;

    public static int getConcurrentUsers() {
        return concurrentUsers;
    }

    public static void setConcurrentUsers(int concurrentUsers) {
        BasicServiceConcurrenceSystem.concurrentUsers = concurrentUsers;
    }

    public synchronized static void incrementConcurrentUsers() {
        concurrentUsers++;
    }

    public synchronized static void decrementConcurrentUsers() {
        concurrentUsers--;
    }

    public static boolean passConcurrence() {
        PropertiesManager pmgr = new PropertiesManager();
        int concurrentUsers = BasicServiceConcurrenceSystem.getConcurrentUsers();
        return concurrentUsers < Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.max.num.concurrent.users"));
    }

    public static boolean passQueue() {
        PropertiesManager pmgr = new PropertiesManager();
        int counter = 0;
        while (BasicServiceConcurrenceSystem.getConcurrentUsers() >= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.max.num.concurrent.users"))) {
            try {
                Logger.putLog("Hay demasiados usuarios concurrentes, se va a esperar para atender la solicitud del usuario", BasicServiceConcurrenceSystem.class, Logger.LOG_LEVEL_INFO);

                if (counter >= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.num.retries.check.concurrence"))) {
                    Logger.putLog("Se va a desatender la solicitud debido a que hay demasiados usuarios concurrentes", BasicServiceConcurrenceSystem.class, Logger.LOG_LEVEL_INFO);
                    return false;
                }

                Random r = new Random(System.currentTimeMillis());
                int max = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.time.retry.check.concurrence"));
                int min = max / 2;
                Thread.sleep(min + r.nextInt(min));
                counter++;
            } catch (Exception e) {
                Logger.putLog("Exception: ", BasicServiceConcurrenceSystem.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        return true;
    }

}
