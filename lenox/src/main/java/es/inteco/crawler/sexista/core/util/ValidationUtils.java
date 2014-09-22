package es.inteco.crawler.sexista.core.util;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import java.net.HttpURLConnection;

public class ValidationUtils {

    public static ActionErrors validateUrl(String urlStr, HttpURLConnection connection) {
        ActionErrors errors = new ActionErrors();
        try {
            int responseCode = connection.getResponseCode();

            if (responseCode > HttpURLConnection.HTTP_BAD_REQUEST) {
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.unavailable.url"));
            } else if (!connection.getHeaderField("content-type").contains("text/html")) {
                errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.not.html.document"));
            }
        } catch (Exception e) {
            Logger.getLogger(ValidationUtils.class).error("Error al acceder a " + urlStr);
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.accessing.url"));
        }
        return errors;
    }
}
