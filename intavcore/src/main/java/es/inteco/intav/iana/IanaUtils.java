package es.inteco.intav.iana;

import es.inteco.intav.utils.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IanaUtils {

    public static String loadIanaRegistries(String url) throws Exception {
        URLConnection connection = new URL(url).openConnection();
        connection.connect();
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return StringUtils.getContentAsString(httpConnection.getInputStream());
            } else {
                return null;
            }
        } else {
            return StringUtils.getContentAsString(connection.getInputStream());
        }
    }

    public static List<String> getIanaList(String ianaRegistries, String type) {
        List<String> languages = new ArrayList<String>();
        Pattern pattern = Pattern.compile(String.format("type: %s\nsubtag: (.*?)\n", type), Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ianaRegistries);
        while (matcher.find()) {
            languages.add(matcher.group(1));
        }
        return languages;
    }

}
