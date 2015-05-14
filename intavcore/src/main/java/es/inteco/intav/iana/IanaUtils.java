package es.inteco.intav.iana;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IanaUtils {

    private IanaUtils() {
    }

    public static String loadIanaRegistries(final String url) {
        InputStream inputStream = null;
        String content = null;
        try {
            inputStream = getInputStream(url);
            content = StringUtils.getContentAsString(inputStream);
        } catch (Exception e) {
            Logger.putLog("Error en la carga de Iana", IanaUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la conexión en la carga de Iana", IanaUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        }
        return content;
    }

    private static InputStream getInputStream(final String url) throws Exception {
        // Si la url empieza por / suponemos que es un recurso interno
        if (url.startsWith("/")) {
            return IanaUtils.class.getResourceAsStream(url);
        } else {
            final URLConnection connection = new URL(url).openConnection();
            connection.connect();
            if (connection instanceof HttpURLConnection) {
                final HttpURLConnection httpConnection = (HttpURLConnection) connection;
                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return httpConnection.getInputStream();
                } else {
                    throw new Exception(String.format("La conexión a Iana mediante HTTP a devuelto el código HTTP %d que es distinto de OK (200)", httpConnection.getResponseCode()));
                }
            } else {
                return connection.getInputStream();
            }
        }
    }

    public static List<String> getIanaList(final String ianaRegistries, final String type) {
        final List<String> languages = new ArrayList<String>();
        final Pattern pattern = Pattern.compile(String.format("type: %s\nsubtag: (.*?)\n", type), Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(ianaRegistries);
        while (matcher.find()) {
            languages.add(matcher.group(1));
        }
        return languages;
    }

}
