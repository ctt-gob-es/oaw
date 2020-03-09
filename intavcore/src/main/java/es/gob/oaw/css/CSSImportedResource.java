package es.gob.oaw.css;

import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.w3c.dom.Element;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Clase para representar un recurso CSS que se ha incluido mediante @import
 */
public class CSSImportedResource implements CSSResource {

    private final String content;
    private String source;

    public CSSImportedResource(final String baseUrl, final String importUrl) {
        try {
            source = new URL(new URL(baseUrl), importUrl).toString();
        } catch (MalformedURLException e) {
            source = importUrl;
        }
        content = loadStyleSheet(baseUrl,importUrl);
    }

    @Override
    public Element getHTMLElement() {
        return null;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean isInline() {
        return false;
    }

    @Override
    public String getStringSource() {
        return source;
    }

    @Override
    public boolean isImported() {
        return true;
    }

    @Override
    public int hashCode() {
        return source.hashCode();
    }

    private String loadStyleSheet(final String baseUrl, final String importUrl) {
        try {
            final URL cssUrl;
            if (URI.create(importUrl).isAbsolute()) {
                cssUrl = new URL(importUrl);
            } else {
                cssUrl = new URL(new URL(baseUrl), importUrl);
            }
            final HttpURLConnection connection = EvaluatorUtils.getConnection(cssUrl.toString(), "GET", true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                return StringUtils.getContentAsString(connection.getInputStream());
            }
        } catch (Exception e) {
            Logger.putLog("Error al cargar la hoja de estilo importada " + importUrl, EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING, e);
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CSSResource)) {
            return false;
        }

        CSSResource that = (CSSResource) o;

        return source.equals(that.getStringSource());
    }
}
