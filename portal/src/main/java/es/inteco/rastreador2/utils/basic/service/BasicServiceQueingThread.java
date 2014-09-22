package es.inteco.rastreador2.utils.basic.service;

import es.inteco.common.Constants;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;

public class BasicServiceQueingThread extends Thread {
    private BasicServiceForm basicServiceForm;

    public BasicServiceQueingThread() {
    }

    public BasicServiceQueingThread(BasicServiceForm basicServiceForm) {
        this.basicServiceForm = basicServiceForm;
    }

    @Override
    public void run() {
        if (BasicServiceConcurrenceSystem.passQueue()) {
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED);
            BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
            basicServiceThread.start();
        } else {
            // Enviar mensaje de error para advertir de que no ha podido superar la cola porque ha habido mucho tiempo de espera
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_RULED_OUT);
        }
    }
}
