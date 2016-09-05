package es.inteco.utils;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.ignored.links.IgnoredLink;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.RastreoDAO;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.utils.CrawlerDOMUtils.getAttribute;
import static es.inteco.utils.CrawlerDOMUtils.hasAttribute;

public final class CrawlerUtils {

    private CrawlerUtils() {
    }

    public static boolean domainMatchs(List<String> domainList, String domain) {
        boolean hasMatched = false;
        for (String domainRegExp : domainList) {
            Pattern pattern = Pattern.compile(domainRegExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

            Matcher matcher = pattern.matcher(domain);
            if (matcher.find()) {
                hasMatched = true;
                break;
            }
        }

        return hasMatched;
    }

    public static boolean isSwitchLanguageLink(Element link, List<IgnoredLink> ignoredLinks) {
        if (ignoredLinks != null && hasAttribute(link, "href") && StringUtils.isNotEmpty(getAttribute(link, "href"))) {
            for (IgnoredLink ignoredLink : ignoredLinks) {
                if (matchsText(link, ignoredLink) || matchsImage(link, ignoredLink) || (link.getNodeName().equalsIgnoreCase("AREA") && matchsAlt(link, ignoredLink))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean matchsText(Element link, IgnoredLink ignoredLink) {
        return matchs(removeInlineTags(link.getTextContent()).trim(), ignoredLink.getText())
                || (hasAttribute(link, "title") && matchs(getAttribute(link, "title").trim(), ignoredLink.getTitle()));
    }

    private static boolean matchsAlt(Element link, IgnoredLink ignoredLink) {
        return hasAttribute(link, "alt") && matchs(getAttribute(link, "alt").trim(), ignoredLink.getText());
    }

    private static String removeInlineTags(String content) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> inlineTags = Arrays.asList(pmgr.getValue("crawler.core.properties", "inline.tags").split(";"));

        for (String tag : inlineTags) {
            content = Pattern.compile(String.format("</?%s +[^>]*>|</?%s>", tag, tag), Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");
        }

        return content;
    }

    private static boolean matchsImage(Element link, IgnoredLink ignoredLink) {
        List<Element> images = CrawlerDOMUtils.getElementsByTagName(link, "frame");

        if (images.size() == 1) {
            for (Element image : images) {
                if (matchs(image.getAttribute("alt"), ignoredLink.getText())
                        || (StringUtils.isEmpty(image.getAttribute("alt").trim()) && matchs(image.getAttribute("title"), ignoredLink.getText()))) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean matchs(String text, String regExp) {
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static String removeHtmlComments(String textContent) {
        return textContent.replaceAll("(?s)<!--.*?-->", "");
    }

    public static URL getAbsoluteUrl(Document document, String rootUrl, String urlLink) throws Exception {
        String base = CrawlerDOMUtils.getBaseUrl(document);
        return StringUtils.isEmpty(base) ? new URL(new URL(rootUrl), urlLink) : new URL(new URL(base), urlLink);
    }

    public static List<String> addDomainsToList(String seedsList, boolean getOnlyDomain, int type) {
        if (StringUtils.isNotEmpty(seedsList)) {
            final String[] seeds = seedsList.split(";");
            final List<String> domains = new ArrayList<>(seeds.length);
            for (int i = 0; i < seeds.length; i++) {
                if (type == Constants.ID_LISTA_SEMILLA && !seeds[i].startsWith("http://") && !seeds[i].startsWith("https://")) {
                    seeds[i] = "http://" + seeds[i];
                }
                if (getOnlyDomain) {
                    domains.add(convertDomains(seeds[i]));
                } else {
                    domains.add(seeds[i]);
                }
            }

            return domains;
        } else {
            return null;
        }
    }

    /**
     * Obtiene el host de una dirección url
     *
     * @param domain cadena que representa una url
     * @return el host de la url o vacío si no la cadena no representa una url válida
     */
    public static String convertDomains(final String domain) {
        try {
            final URL domainUrl = new URL(domain);

            return domainUrl.getHost();
        } catch (MalformedURLException e) {
            Logger.putLog("Error al obtener el dominio base de la URL", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
        }

        return "";
    }

    public static String getHash(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(string.getBytes());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (Exception e) {
            Logger.putLog("Error al obtener la codificación MD5 de una cadena de texto", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
            return null;
        }
    }

    public static boolean hasToBeFilteredUri(HttpServletRequest request) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> notFilteredUris = Arrays.asList(pmgr.getValue("crawler.properties", "not.filtered.uris").split(";"));

        if (request.getParameter("key") != null
                && request.getParameter("key").equals(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"))
                && containsUriFragment(notFilteredUris, request.getRequestURI())) {
            return false;
        } else {
            return true;
        }
    }

    private static boolean containsUriFragment(List<String> uriFragments, String uri) {
        for (String uriFragment : uriFragments) {
            if (uri.contains(uriFragment)) {
                return true;
            }
        }
        return false;
    }

    public static String encodeUrl(String url) {
        return url.replaceAll(" ", "%20").replaceAll("Á", "%E1").replaceAll("É", "%C9").replaceAll("Í", "%CD")
                .replaceAll("Ó", "%D3").replaceAll("Ú", "%DA").replaceAll("á", "%E1").replaceAll("é", "%E9")
                .replaceAll("í", "%ED").replaceAll("ó", "%F3").replaceAll("ú", "%FA")
                .replaceAll("Ñ", "%D1").replaceAll("ñ", "%F1").replaceAll("&amp;", "&");
    }

    public static List<String> getDomainsList(final Long idCrawling, final int type, final boolean getOnlyDomain) {
        try (Connection conn = DataBaseManager.getConnection()) {
            return CrawlerUtils.addDomainsToList(RastreoDAO.getList(conn, idCrawling, type), getOnlyDomain, type);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCharset(HttpURLConnection connection, InputStream markableInputStream) throws Exception {
        String charset = Constants.DEFAULT_CHARSET;
        boolean found = false;

        // Buscamos primero en las cabeceras de la respuesta
        try {
            String header = connection.getHeaderField("Content-type");
            String charsetValue = header.substring(header.indexOf("charset"));
            charsetValue = charsetValue.substring(charsetValue.indexOf('=') + 1);
            if (StringUtils.isNotEmpty(charsetValue)) {
                charset = charsetValue;
                found = true;
            }
        } catch (Exception e) {
            // found = false;
        }

        if (!found || !isValidCharset(charset)) {
            charset = getCharsetWithUniversalDetector(markableInputStream);
            markableInputStream.reset();
        }

        if (found && !isValidCharset(charset)) {
            charset = Constants.DEFAULT_CHARSET;
        }

        return charset;
    }

    private static String getCharsetWithUniversalDetector(InputStream markableInputStream) {
        try {
            UniversalDetector detector = new UniversalDetector(null);
            byte[] buf = new byte[4096];

            int nread;
            while ((nread = markableInputStream.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();

            return detector.getDetectedCharset();
        } catch (Exception e) {
            Logger.putLog("Error al detectar la codificación con Universal Detector", CrawlerUtils.class, Logger.LOG_LEVEL_INFO);
            return null;
        }
    }

    private static boolean isValidCharset(String charset) {
        try {
            byte[] test = new byte[10];
            new String(test, charset);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static String getTextContent(HttpURLConnection connection, InputStream markableInputStream) throws Exception {
        String textContent = StringUtils.getContentAsString(markableInputStream, getCharset(connection, markableInputStream));

        textContent = removeHtmlComments(textContent);

        return textContent;
    }

    public static InputStream getMarkableInputStream(final HttpURLConnection connection) throws Exception {
        final InputStream content = connection.getInputStream();

        final BufferedInputStream stream = new BufferedInputStream(content);

        // mark InputStream so we can restart it for validator
        if (stream.markSupported()) {
            stream.mark(Integer.MAX_VALUE);
        }
        return stream;
    }

    public static HttpURLConnection getConnection(String url, String refererUrl, boolean followRedirects) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (connection instanceof HttpsURLConnection) {
            final HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
            httpsConnection.setSSLSocketFactory(getNaiveSSLSocketFactory());
            httpsConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        }
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setConnectTimeout(Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
        connection.setReadTimeout(Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
        connection.addRequestProperty("Accept", pmgr.getValue("crawler.core.properties", "method.accept.header"));
        connection.addRequestProperty("Accept-Language", pmgr.getValue("crawler.core.properties", "method.accept.language.header"));
        connection.addRequestProperty("User-Agent", pmgr.getValue("crawler.core.properties", "method.user.agent.header"));
        if (refererUrl != null) {
            connection.addRequestProperty("Referer", refererUrl);
        }
        return connection;
    }

    private static SSLSocketFactory getNaiveSSLSocketFactory() {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        try {
            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            return sc.getSocketFactory();
        } catch (Exception e) {
            Logger.putLog("Excepción: ", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }


    public static HttpURLConnection followRedirection(final String cookie, final URL url, final String redirectTo) throws Exception {
        final URL metaRedirection = new URL(url, redirectTo);
        final HttpURLConnection connection = getConnection(metaRedirection.toString(), url.toString(), false);
        connection.setRequestProperty("Cookie", cookie);
        Logger.putLog(String.format("Siguiendo la redirección de %s a %s", url, metaRedirection), CrawlerUtils.class, Logger.LOG_LEVEL_INFO);
        return connection;
    }

    public static String getCookie(final HttpURLConnection connection) {
        // Cogemos la lista de cookies, teniendo en cuenta que el parametro set-cookie no es sensible a mayusculas o minusculas
        final Map<String, List<String>> headerFields = connection.getHeaderFields();
        final List<String> headers = new ArrayList<>();
        if (headerFields != null && !headerFields.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                if ("SET-COOKIE".equalsIgnoreCase(entry.getKey())) {
                    headers.addAll(entry.getValue());
                }
            }
        }

        final StringBuilder headerText = new StringBuilder();
        for (String header : headers) {
            if (header.contains(";")) {
                if (!header.substring(0, header.indexOf(';')).toLowerCase().endsWith("deleted")) {
                    headerText.append(header.substring(0, header.indexOf(';'))).append("; ");
                }
            } else {
                headerText.append(header).append("; ");
            }
        }

        return headerText.toString();
    }

    /**
     * Comprueba si la conexión se ha realizado a la página de OpenDNS
     *
     * @param connection la conexión que se quiere comprobar
     * @return true si la conexión se ha realizado a la página de OpenDNS o false en caso contrario.
     */
    public static boolean isOpenDNSResponse(final HttpURLConnection connection) {
        return connection.getHeaderField("Server") != null && connection.getHeaderField("Server").toLowerCase().contains("opendns");
    }

    /**
     * Comprueba si un contenido es un RSS
     *
     * @param content una cadena con un contenido textual
     * @return true si corresponde a un RSS o false en caso contrario
     */
    public static boolean isRss(final String content) {
        return !content.toLowerCase().contains("</html>") && content.toLowerCase().contains("</rss>");
    }

    /**
     * Comprueba si un enlace es de tipo http: o usa otro protocolo (mailto:, javascript:,...)
     * @param link enlace a comprobar
     * @return true si el enlace es de tipo http o false en caso contrario.
     */
    public static boolean isHTTPLink(final Element link) {
        return getAttribute(link, "href").startsWith("http");
    }
}
