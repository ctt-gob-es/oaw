/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.utils;

import static es.inteco.utils.CrawlerDOMUtils.getAttribute;
import static es.inteco.utils.CrawlerDOMUtils.hasAttribute;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.dao.ProxyDAO;
import es.inteco.crawler.dao.ProxyForm;
import es.inteco.crawler.ignored.links.IgnoredLink;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.RastreoDAO;

/**
 * The Class CrawlerUtils.
 */
public final class CrawlerUtils {
	/**
	 * Instantiates a new crawler utils.
	 */
	private CrawlerUtils() {
	}

	/**
	 * Domain matchs.
	 *
	 * @param domainList the domain list
	 * @param domain     the domain
	 * @return true, if successful
	 */
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

	/**
	 * Checks if is switch language link.
	 *
	 * @param link         the link
	 * @param ignoredLinks the ignored links
	 * @return true, if is switch language link
	 */
	public static boolean isSwitchLanguageLink(Element link, List<IgnoredLink> ignoredLinks) {
		if (ignoredLinks != null && hasAttribute(link, "href") && StringUtils.isNotEmpty(getAttribute(link, "href"))) {
			for (IgnoredLink ignoredLink : ignoredLinks) {
				if (matchsText(link, ignoredLink) || matchsImage(link, ignoredLink) || (link.getNodeName().equalsIgnoreCase("area") && matchsAlt(link, ignoredLink))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Matchs text.
	 *
	 * @param link        the link
	 * @param ignoredLink the ignored link
	 * @return true, if successful
	 */
	private static boolean matchsText(Element link, IgnoredLink ignoredLink) {
		return matchs(removeInlineTags(link.getTextContent()).trim(), ignoredLink.getText()) || (hasAttribute(link, "title") && matchs(getAttribute(link, "title").trim(), ignoredLink.getTitle()));
	}

	/**
	 * Matchs alt.
	 *
	 * @param link        the link
	 * @param ignoredLink the ignored link
	 * @return true, if successful
	 */
	private static boolean matchsAlt(Element link, IgnoredLink ignoredLink) {
		return hasAttribute(link, "alt") && matchs(getAttribute(link, "alt").trim(), ignoredLink.getText());
	}

	/**
	 * Removes the inline tags.
	 *
	 * @param content the content
	 * @return the string
	 */
	private static String removeInlineTags(String content) {
		PropertiesManager pmgr = new PropertiesManager();
		List<String> inlineTags = Arrays.asList(pmgr.getValue("crawler.core.properties", "inline.tags").split(";"));
		for (String tag : inlineTags) {
			content = Pattern.compile(String.format("</?%s +[^>]*>|</?%s>", tag, tag), Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");
		}
		return content;
	}

	/**
	 * Matchs image.
	 *
	 * @param link        the link
	 * @param ignoredLink the ignored link
	 * @return true, if successful
	 */
	private static boolean matchsImage(Element link, IgnoredLink ignoredLink) {
		List<Element> images = CrawlerDOMUtils.getElementsByTagName(link, "frame");
		if (images.size() == 1) {
			for (Element image : images) {
				if (matchs(image.getAttribute("alt"), ignoredLink.getText()) || (StringUtils.isEmpty(image.getAttribute("alt").trim()) && matchs(image.getAttribute("title"), ignoredLink.getText()))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Matchs.
	 *
	 * @param text   the text
	 * @param regExp the reg exp
	 * @return true, if successful
	 */
	public static boolean matchs(String text, String regExp) {
		Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

	/**
	 * Removes the html comments.
	 *
	 * @param textContent the text content
	 * @return the string
	 */
	public static String removeHtmlComments(String textContent) {
		return textContent.replaceAll("(?s)<!--.*?-->", "");
	}

	/**
	 * Gets the absolute url.
	 *
	 * @param document the document
	 * @param rootUrl  the root url
	 * @param urlLink  the url link
	 * @return the absolute url
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static URL getAbsoluteUrl(Document document, String rootUrl, String urlLink) throws MalformedURLException {
		String base = CrawlerDOMUtils.getBaseUrl(document);
		//
		try {
			URL baseValid = new URL(base);
		} catch (MalformedURLException e) {
			base = "";
		}
		return StringUtils.isEmpty(base) ? new URL(new URL(rootUrl), urlLink) : new URL(new URL(base), urlLink);
	}

	/**
	 * Adds the domains to list.
	 *
	 * @param seedsList     the seeds list
	 * @param getOnlyDomain the get only domain
	 * @param type          the type
	 * @return the list
	 */
	public static List<String> addDomainsToList(String seedsList, boolean getOnlyDomain, int type) {
		if (StringUtils.isNotEmpty(seedsList)) {
			final String[] seeds = seedsList.split("[;\n]");
			final List<String> domains = new ArrayList<>(seeds.length);
			for (int i = 0; i < seeds.length; i++) {
				if (type == Constants.ID_LISTA_SEMILLA && !seeds[i].startsWith("http://") && !seeds[i].startsWith("https://")) {
					seeds[i] = "http://" + seeds[i].trim();
				}
				if (getOnlyDomain) {
					domains.add(convertDomains(seeds[i].trim()));
				} else {
					domains.add(seeds[i].trim());
				}
			}
			return domains;
		} else {
			return null;
		}
	}

	/**
	 * Obtiene el host de una dirección url.
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

	/**
	 * Gets the hash.
	 *
	 * @param string the string
	 * @return the hash
	 */
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

	/**
	 * Checks for to be filtered uri.
	 *
	 * @param request the request
	 * @return true, if successful
	 */
	public static boolean hasToBeFilteredUri(HttpServletRequest request) {
		PropertiesManager pmgr = new PropertiesManager();
		List<String> notFilteredUris = Arrays.asList(pmgr.getValue("crawler.properties", "not.filtered.uris").split(";"));
		if (request.getParameter("key") != null && request.getParameter("key").equals(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"))
				&& containsUriFragment(notFilteredUris, request.getRequestURI())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Contains uri fragment.
	 *
	 * @param uriFragments the uri fragments
	 * @param uri          the uri
	 * @return true, if successful
	 */
	private static boolean containsUriFragment(List<String> uriFragments, String uri) {
		for (String uriFragment : uriFragments) {
			if (uri.contains(uriFragment)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Encode url.
	 *
	 * @param url the url
	 * @return the string
	 */
	public static String encodeUrl(String url) {
		String replaceAll = url.replaceAll("Ã¡", "á").replaceAll("Ã©", "é").replaceAll("Ã­", "í").replaceAll("Ã³", "ó").replaceAll("Ãº", "ú").replaceAll("Ã±", "ñ").replaceAll("Ã‘", "Ñ")
				.replaceAll(" ", "%20").replaceAll("Á", "%E1").replaceAll("É", "%C9").replaceAll("Í", "%CD").replaceAll("Ó", "%D3").replaceAll("Ú", "%DA").replaceAll("á", "%E1").replaceAll("é", "%E9")
				.replaceAll("í", "%ED").replaceAll("ó", "%F3").replaceAll("ú", "%FA").replaceAll("Ñ", "%D1").replaceAll("ñ", "%F1").replaceAll("&amp;", "&").replaceAll("Âº", "º")
				.replaceAll("º", "%BA").replaceAll("Âª", "ª").replaceAll("ª", "%AA");
		return replaceAll;
	}

	/**
	 * Gets the domains list.
	 *
	 * @param idCrawling    the id crawling
	 * @param type          the type
	 * @param getOnlyDomain the get only domain
	 * @return the domains list
	 */
	public static List<String> getDomainsList(final Long idCrawling, final int type, final boolean getOnlyDomain) {
		try (Connection conn = DataBaseManager.getConnection()) {
			return CrawlerUtils.addDomainsToList(RastreoDAO.getList(conn, idCrawling, type), getOnlyDomain, type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Gets the charset.
	 *
	 * @param connection          the connection
	 * @param markableInputStream the markable input stream
	 * @return the charset
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getCharset(HttpURLConnection connection, InputStream markableInputStream) throws IOException {
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

	/**
	 * Gets the charset with universal detector.
	 *
	 * @param markableInputStream the markable input stream
	 * @return the charset with universal detector
	 */
	private static String getCharsetWithUniversalDetector(final InputStream markableInputStream) {
		try {
			final UniversalDetector detector = new UniversalDetector(null);
			final byte[] buf = new byte[4096];
			int nread = markableInputStream.read(buf);
			while (nread > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
				nread = markableInputStream.read(buf);
			}
			detector.dataEnd();
			return detector.getDetectedCharset();
		} catch (Exception e) {
			Logger.putLog("Error al detectar la codificación con Universal Detector", CrawlerUtils.class, Logger.LOG_LEVEL_INFO);
			return null;
		}
	}

	/**
	 * Checks if is valid charset.
	 *
	 * @param charset the charset
	 * @return true, if is valid charset
	 */
	private static boolean isValidCharset(String charset) {
		try {
			byte[] test = new byte[10];
			new String(test, charset);
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	/**
	 * Gets the text content.
	 *
	 * @param connection          the connection
	 * @param markableInputStream the markable input stream
	 * @return the text content
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getTextContent(HttpURLConnection connection, InputStream markableInputStream) throws IOException {
		String textContent = StringUtils.getContentAsString(markableInputStream, getCharset(connection, markableInputStream));
		textContent = removeHtmlComments(textContent);
		return textContent;
	}

	/**
	 * Gets the markable input stream.
	 *
	 * @param connection the connection
	 * @return the markable input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static InputStream getMarkableInputStream(final HttpURLConnection connection) throws IOException {
		final InputStream content = connection.getInputStream();
		final BufferedInputStream stream = new BufferedInputStream(content);
		// mark InputStream so we can restart it for validator
		if (stream.markSupported()) {
			stream.mark(Integer.MAX_VALUE);
		}
		return stream;
	}

	/**
	 * Gets the connection.
	 *
	 * @param url             the url
	 * @param refererUrl      the referer url
	 * @param followRedirects the follow redirects
	 * @return the connection
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static HttpURLConnection getConnection(String url, String refererUrl, boolean followRedirects) throws IOException {
		final HttpURLConnection connection = generateConnection(url, refererUrl);
		// Omitimos la redirección y si detectamos una, actualizamos el
		// conector
		// Como se usa este método para las conexiones al servicio de
		// diagnóstico mediante la JSP se hace esta comprobación para siga la
		// redirreción
		if (!"BASIC_SERVICE_URL".equals(refererUrl)) {
			int status = connection.getResponseCode();
			connection.disconnect();
			if (status != HttpURLConnection.HTTP_OK && followRedirects) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {
					// Obtenemos la redirección
					String newUrl = connection.getHeaderField("Location");
					connection.disconnect();
					return getConnection(encodeUrl(newUrl), refererUrl, false);
				}
			}
		}
		return generateConnection(url, refererUrl);
	}

	/**
	 * Generar una conexión que pasa por el renderizador de páginas HTML.
	 *
	 * @param url        the url
	 * @param refererUrl the referer url
	 * @return the http URL connection
	 * @throws IOException           Signals that an I/O exception has occurred.
	 * @throws MalformedURLException the malformed URL exception
	 */
	public static HttpURLConnection generateRendererConnection(String url, String refererUrl) throws IOException, MalformedURLException {
		final PropertiesManager pmgr = new PropertiesManager();
		String proxyActive = "";
		String proxyHttpHost = "";
		String proxyHttpPort = "";
		try (Connection c = DataBaseManager.getConnection()) {
			ProxyForm proxy = ProxyDAO.getProxy(c);
			proxyActive = proxy.getStatus() > 0 ? "true" : "false";
			proxyHttpHost = proxy.getUrl();
			proxyHttpPort = proxy.getPort();
			DataBaseManager.closeConnection(c);
		} catch (Exception e) {
			Logger.putLog("Error: ", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		HttpURLConnection connection = null;
		// Aplicar el proxy menos a la URL del servicio de diagnótico ya que este
		// método también es usado por al JSP de conexión
		if (applyProxy(url, refererUrl, proxyActive, proxyHttpHost, proxyHttpPort)) {
			try {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHttpHost, Integer.parseInt(proxyHttpPort)));
				Logger.putLog("Conectando con la URL: " + url + " - Aplicando proxy: " + proxyHttpHost + ":" + proxyHttpPort, CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
				connection = (HttpURLConnection) new URL(url).openConnection(proxy);
			} catch (NumberFormatException e) {
				Logger.putLog("Error al crear el proxy: " + proxyHttpHost + ":" + proxyHttpPort, CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
			}
		} else {
			Logger.putLog("Conectando con la URL: " + url, CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
			connection = (HttpURLConnection) new URL(url).openConnection();
		}
		if (connection instanceof HttpsURLConnection) {
			final HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
			httpsConnection.setSSLSocketFactory(getNaiveSSLSocketFactory());
			httpsConnection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String s, SSLSession sslSession) {
					return s.equalsIgnoreCase(sslSession.getPeerHost());
				}
			});
		}
		connection.setInstanceFollowRedirects(false);
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

	/**
	 * Apply proxy.
	 *
	 * @param url           the url
	 * @param refererUrl    the referer url
	 * @param proxyActive   the proxy active
	 * @param proxyHttpHost the proxy http host
	 * @param proxyHttpPort the proxy http port
	 * @return true, if successful
	 */
	private static boolean applyProxy(String url, String refererUrl, String proxyActive, String proxyHttpHost, String proxyHttpPort) {
		final PropertiesManager pmgr = new PropertiesManager();
		return "true".equals(proxyActive) && proxyHttpHost != null && proxyHttpPort != null && !"BASIC_SERVICE_URL".equals(refererUrl) && url != null && !url.isEmpty()
				&& !url.toLowerCase().startsWith("javascript") && !url.toLowerCase().startsWith("mailto") && !url.toLowerCase().startsWith("tel") && !url.toLowerCase().endsWith(".pdf")
				&& !url.toLowerCase().endsWith(".doc") && !url.toLowerCase().endsWith(".epub") && !url.toLowerCase().endsWith(".xml") && !url.toLowerCase().endsWith(".xls")
				&& !url.toLowerCase().endsWith(".wsdl") && !url.toLowerCase().endsWith(".css") && !url.toLowerCase().endsWith(".png") && !url.toLowerCase().endsWith(".jpeg")
				&& !url.toLowerCase().endsWith(".jpg") && !url.toLowerCase().endsWith(".bmp") && !url.toLowerCase().endsWith(".gif") && !url.toLowerCase().endsWith(".svg")
				&& !url.toLowerCase().endsWith(".7z") && !url.toLowerCase().endsWith(".rar") && !url.toLowerCase().endsWith(".tar.gz") && !url.toLowerCase().endsWith(".zip")
				&& !url.toLowerCase().endsWith(".tar.xz") && !url.toLowerCase().endsWith(".war") && !url.toLowerCase().endsWith(".jar") && !url.toLowerCase().endsWith(".tar")
				&& !url.toLowerCase().endsWith(".webm") && !url.equalsIgnoreCase(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.validator")) && !url.toLowerCase().contains(".css")
				&& !url.toLowerCase().contains(".scss") && !url.toLowerCase().contains(".pdf") && !url.toLowerCase().contains(".xml") && !url.toLowerCase().contains(".wsdl");
	}

	/**
	 * Generate connection.
	 *
	 * @param url        the url
	 * @param refererUrl the referer url
	 * @return the http URL connection
	 * @throws IOException           Signals that an I/O exception has occurred.
	 * @throws MalformedURLException the malformed URL exception
	 */
	private static HttpURLConnection generateConnection(String url, String refererUrl) throws IOException, MalformedURLException {
		final PropertiesManager pmgr = new PropertiesManager();
		HttpURLConnection connection = null;
		Logger.putLog("Conectando con la URL: " + url, CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
		connection = (HttpURLConnection) new URL(url).openConnection();
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
		connection.setInstanceFollowRedirects(false);
		connection.setConnectTimeout(Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
		connection.setReadTimeout(Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
		connection.addRequestProperty("Accept", pmgr.getValue("crawler.core.properties", "method.accept.header"));
		connection.addRequestProperty("Accept-Language", pmgr.getValue("crawler.core.properties", "method.accept.language.header"));
		connection.addRequestProperty("User-Agent", pmgr.getValue("crawler.core.properties", "method.user.agent.header"));
		if (refererUrl != null) {
			connection.addRequestProperty("Referer", refererUrl);
		}
		return connection;
//		return generateRendererConnection(url, refererUrl);
	}

	/**
	 * Gets the naive SSL socket factory.
	 *
	 * @return the naive SSL socket factory
	 */
	private static SSLSocketFactory getNaiveSSLSocketFactory() {
		// Create a trust manager that does not validate certificate chains
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		// Install the all-trusting trust manager
		try {
			// Modificamos el protocolo SSL para solucionar la conexión con
			// algunos páginas que no son accesibles con SSL/TSL (v1)
			final SSLContext sc = SSLContext.getInstance("TLSv1.2");
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
			Logger.putLog("Excepción: ", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Follow redirection.
	 *
	 * @param cookie     the cookie
	 * @param url        the url
	 * @param redirectTo the redirect to
	 * @return the http URL connection
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static HttpURLConnection followRedirection(final String cookie, final URL url, final String redirectTo) throws IOException {
		final URL metaRedirection = new URL(url, redirectTo);
		final HttpURLConnection connection = getConnection(metaRedirection.toString(), url.toString(), false);
		if (!StringUtils.isEmpty(cookie)) {
			connection.setRequestProperty("Cookie", cookie);
		}
		Logger.putLog(String.format("Siguiendo la redirección de %s a %s", url, metaRedirection), CrawlerUtils.class, Logger.LOG_LEVEL_INFO);
		return connection;
	}

	/**
	 * Gets the cookie.
	 *
	 * @param connection the connection
	 * @return the cookie
	 */
	public static String getCookie(final HttpURLConnection connection) {
		// Cogemos la lista de cookies, teniendo en cuenta que el parametro
		// set-cookie no es sensible a mayusculas o minusculas
		final StringBuilder headerText = new StringBuilder();
		try {
			final Map<String, List<String>> headerFields = connection.getHeaderFields();
			final List<String> headers = new ArrayList<>();
			if (headerFields != null && !headerFields.isEmpty()) {
				for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
					if ("SET-COOKIE".equalsIgnoreCase(entry.getKey())) {
						headers.addAll(entry.getValue());
					}
				}
			}
			for (String header : headers) {
				if (header.contains(";")) {
					if (!header.substring(0, header.indexOf(';')).toLowerCase().endsWith("deleted")) {
						headerText.append(header.substring(0, header.indexOf(';'))).append("; ");
					}
				} else {
					headerText.append(header).append("; ");
				}
			}
		} catch (IllegalArgumentException ie) {
			Logger.putLog("Error reading cookie", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR, ie);
		}
		return headerText.toString();
	}

	/**
	 * Comprueba si la conexión se ha realizado a la página de OpenDNS.
	 *
	 * @param connection la conexión que se quiere comprobar
	 * @return true si la conexión se ha realizado a la página de OpenDNS o false en caso contrario.
	 */
	public static boolean isOpenDNSResponse(final HttpURLConnection connection) {
		return connection.getHeaderField("Server") != null && connection.getHeaderField("Server").toLowerCase().contains("opendns");
	}

	/**
	 * Comprueba si un contenido es un RSS.
	 *
	 * @param content una cadena con un contenido textual
	 * @return true si corresponde a un RSS o false en caso contrario
	 */
	public static boolean isRss(final String content) {
		return !content.toLowerCase().contains("</html>") && content.toLowerCase().contains("</rss>");
	}

	/**
	 * Comprueba si un enlace es de tipo http: o usa otro protocolo (mailto:, javascript:,...)
	 * 
	 * @param link enlace a comprobar
	 * @return true si el enlace es de tipo http o false en caso contrario.
	 */
	public static boolean isHTTPLink(final Element link) {
		return link.hasAttribute("href") && !link.getAttribute("href").toLowerCase().startsWith("javascript:") && !link.getAttribute("href").toLowerCase().startsWith("mailto:")
				&& !link.getAttribute("href").toLowerCase().startsWith("javascript") && !link.getAttribute("href").toLowerCase().startsWith("tel:");
	}
}
