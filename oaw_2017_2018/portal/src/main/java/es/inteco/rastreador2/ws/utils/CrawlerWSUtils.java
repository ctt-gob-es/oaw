package es.inteco.rastreador2.ws.utils;

import es.inteco.common.Constants;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.basic.service.BasicServiceConcurrenceSystem;
import es.inteco.rastreador2.utils.basic.service.BasicServiceQueingThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

import java.util.Calendar;

public final class CrawlerWSUtils {

    private CrawlerWSUtils() {
    }

    public static Integer webCrawl(BasicServiceForm basicServiceForm) {
        if (!BasicServiceConcurrenceSystem.passConcurrence()) {
            if (basicServiceForm.getId() == 0) {
                basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_QUEUED));
            } else {
                BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_QUEUED);
            }

            // Ponemos el análisis en cola
            BasicServiceQueingThread basicServiceQueingThread = new BasicServiceQueingThread(basicServiceForm);
            basicServiceQueingThread.start();

            return es.inteco.rastreador2.ws.commons.Constants.CRAWL_QUEUED;
        } else {
            if (basicServiceForm.getId() == 0) {
                basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED));
            } else {
                BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED);
            }

            // Lanzamos el análisis
            BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
            basicServiceThread.start();

            return es.inteco.rastreador2.ws.commons.Constants.CRAWL_LAUNCHED;
        }
    }

    public static BasicServiceForm getBasicServiceForm(String url, int depth, int size, String guideline, String email, Calendar schedulingDate, String idUser) {
        BasicServiceForm basicServiceForm = new BasicServiceForm();

        basicServiceForm.setDomain(url);
        basicServiceForm.setAmplitud(String.valueOf(size));
        basicServiceForm.setProfundidad(String.valueOf(depth));
        basicServiceForm.setReport(guideline);
        basicServiceForm.setEmail(email);
        basicServiceForm.setUser(idUser);
        basicServiceForm.setLanguage("es");
        basicServiceForm.setSchedulingDate(schedulingDate != null ? schedulingDate.getTime() : null);

        return basicServiceForm;
    }

}