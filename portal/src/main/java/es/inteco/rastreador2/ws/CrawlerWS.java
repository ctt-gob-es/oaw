package es.inteco.rastreador2.ws;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.rastreador2.ws.commons.Constants;
import es.inteco.rastreador2.ws.utils.CrawlerWSUtils;

import java.util.Calendar;

public class CrawlerWS {

    public Integer webCrawl(String url, int depth, int size, String guideline, String email, Calendar schedulingDate, String idUser) {
        BasicServiceForm basicServiceForm = CrawlerWSUtils.getBasicServiceForm(url, depth, size, guideline, email, schedulingDate, idUser);

        if (schedulingDate == null) {
            return CrawlerWSUtils.webCrawl(basicServiceForm);
        } else {
            try {
                if (schedulingDate.after(Calendar.getInstance())) {
                    BasicServiceUtils.scheduleBasicServiceJob(basicServiceForm);

                    return Constants.CRAWL_QUEUED;
                } else {
                    return Constants.CRAWL_SCHEDULING_PAST_DATE;
                }
            } catch (Exception e) {
                Logger.putLog("Error al programar el rastreo: ", CrawlerWS.class, Logger.LOG_LEVEL_ERROR, e);
                return Constants.CRAWL_SCHEDULING_ERROR;
            }


        }
    }

}