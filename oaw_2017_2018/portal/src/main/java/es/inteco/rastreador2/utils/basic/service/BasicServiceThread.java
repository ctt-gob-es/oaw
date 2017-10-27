package es.inteco.rastreador2.utils.basic.service;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.utils.CrawlerUtils;
import org.apache.commons.codec.net.URLCodec;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class BasicServiceThread extends Thread {

    private final BasicServiceForm basicServiceForm;

    public BasicServiceThread(BasicServiceForm basicServiceForm) {
        this.basicServiceForm = basicServiceForm;
    }

    @Override
    public void run() {
        try {
            final PropertiesManager pmgr = new PropertiesManager();
            final String url = pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.url");

            BasicServiceConcurrenceSystem.incrementConcurrentUsers();
            final HttpURLConnection connection = CrawlerUtils.getConnection(url, "BASIC_SERVICE_URL", true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

            final URLCodec codec = new URLCodec();
             String postRequest = pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.post.request")
                    .replace("{0}", StringUtils.isNotEmpty(basicServiceForm.getDomain()) ? URLEncoder.encode(basicServiceForm.getDomain(), "UTF-8") : "")
                    .replace("{1}", basicServiceForm.getEmail())
                    .replace("{2}", String.valueOf(basicServiceForm.getProfundidad()))
                    .replace("{3}", String.valueOf(basicServiceForm.getAmplitud()))
                    .replace("{4}", basicServiceForm.getReport())
                    .replace("{5}", basicServiceForm.getUser())
                    .replace("{6}", Constants.EXECUTE)
                    .replace("{7}", String.valueOf(basicServiceForm.getId()))
                    .replace("{8}", StringUtils.isNotEmpty(basicServiceForm.getContent()) ? codec.encode(basicServiceForm.getContent()) : "")
                    .replace("{9}", String.valueOf(basicServiceForm.isInDirectory()));
            if (basicServiceForm.isRegisterAnalysis()) {
                postRequest += "&registerAnalysis=true";
            }
            if (basicServiceForm.isDeleteOldAnalysis()) {
                postRequest += "&analysisToDelete=" + basicServiceForm.getAnalysisToDelete();
            }
            writer.write(postRequest);
            writer.flush();
            writer.close();

            connection.connect();
            Logger.putLog("Pidiendo la URL " + url, BasicServiceThread.class, Logger.LOG_LEVEL_INFO);
            Logger.putLog("URL EXECUTE " + postRequest, BasicServiceThread.class, Logger.LOG_LEVEL_ERROR);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_HTTP_REQUEST_ERROR);
            }
            connection.disconnect();
        } catch (SocketTimeoutException ste) {
            Logger.putLog("SocketTimeoutException (es normal): " + ste.getMessage(), BasicServiceThread.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception e) {
            Logger.putLog("Excepción: ", BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            BasicServiceConcurrenceSystem.decrementConcurrentUsers();
        }
    }
}
