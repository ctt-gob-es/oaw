
/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckerParser;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.ignored.links.IgnoredLink;
import es.inteco.crawler.ignored.links.Utils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.utils.CrawlerDOMUtils;
import es.inteco.utils.CrawlerUtils;

/**
 * Created by mikunis on 26/11/14.
 */
public class TestCrawl {
	/** The Constant LOG. */
	public static final Log LOG = LogFactory.getLog("root");
	/** Constante EMPTY_STRING. */
	private static final String EMPTY_STRING = "";
	/** The crawling domains. */
	private final List<CrawledLink> crawlingDomains = new ArrayList<CrawledLink>();
	/** The aux domains. */
	private final List<String> auxDomains = new ArrayList<String>();
	/** The md 5 content. */
	private final List<String> md5Content = new ArrayList<String>();
	/** The rejected domains. */
	private final List<String> rejectedDomains = new ArrayList<String>();

	/**
	 * Sets the up class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		// Create initial context
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
		final InitialContext ic = new InitialContext();
		ic.createSubcontext("java:");
		ic.createSubcontext("java:/comp");
		ic.createSubcontext("java:/comp/env");
		ic.createSubcontext("java:/comp/env/jdbc");
		// Construct DataSource
		final MysqlConnectionPoolDataSource mysqlDataSource = new MysqlConnectionPoolDataSource();
		mysqlDataSource.setURL("jdbc:mysql://localhost:3306/OAW_DES?serverTimezone=Europe/Rome");
		mysqlDataSource.setUser("root");
		mysqlDataSource.setPassword("root");
		ic.bind("java:/comp/env/jdbc/oaw", mysqlDataSource);
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.ERROR);
		// System.setOut(new PrintStream(new File("/home/alvaro/Downloads/age-manual.txt")));
	}

	/**
	 * Checks if is outer domain.
	 *
	 * @param domain the domain
	 * @param url    the url
	 * @return true, if is outer domain
	 */
	private static boolean isOuterDomain(String domain, String url) {
		try {
			if (domain.equalsIgnoreCase(new URL(url).getHost())) {
				return false;
			}
		} catch (Exception e) {
			LOG.error("Error al obtener el dominio base de la URL");
		}
		return true;
	}

	/**
	 * Checks if is in the same directory.
	 *
	 * @param link    the link
	 * @param urlRoot the url root
	 * @return true, if is in the same directory
	 */
	private static boolean isInTheSameDirectory(String link, String urlRoot) {
		final String protocolRegExp = "https?://";
		final String urlRootDirectory = urlRoot.replaceAll(protocolRegExp, "").lastIndexOf("/") != -1
				? urlRoot.replaceAll(protocolRegExp, "").substring(0, urlRoot.replaceAll(protocolRegExp, "").lastIndexOf("/"))
				: urlRoot.replaceAll(protocolRegExp, "");
		final String linkDirectory = link.replaceAll(protocolRegExp, "").lastIndexOf("/") != -1 ? link.replaceAll(protocolRegExp, "").substring(0, link.replaceAll(protocolRegExp, "").lastIndexOf("/"))
				: link.replaceAll(protocolRegExp, "");
		return linkDirectory.contains(urlRootDirectory);
	}

	/**
	 * Crawl principales.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void crawlPrincipales() throws Exception {
		String[] urlsD = new String[] {};
		String[] urls = { "https://sede.aemet.gob.es" };
		for (String url : urls) {
			this.crawl(url, false);
		}
		for (String url : urlsD) {
			this.crawl(url, true);
		}
	}

	/**
	 * Test.
	 *
	 * @param url          the url
	 * @param enDirectorio the en directorio
	 * @throws Exception the exception
	 */
	public void crawl(String url, boolean enDirectorio) throws Exception {
		// System.setOut(new PrintStream(new File("/home/alvaro/Downloads/age-manual-" + RandomStringUtils.randomAlphabetic(10) + ".txt")));
//		System.setOut(new PrintStream(new File("/home/alvaro/Downloads/AGE-MANUAL/age-manual-" + url.replace("/", "_") + ".txt")));
		System.out.println(url);
		System.out.println("-------------------------------------------------------------------------------------------\n\n");
		final CrawlerData crawlerData = new CrawlerData();
		crawlerData.setUrls(Collections.singletonList(url));
		crawlerData.setProfundidad(5);
		crawlerData.setTopN(10);
		crawlerData.setPseudoaleatorio(true);
		crawlerData.setTest(true);
		crawlerData.setIdCrawling(-1);
		crawlerData.setInDirectory(enDirectorio);
		makeCrawl(crawlerData);
		for (CrawledLink cl : crawlingDomains) {
			System.out.println(cl.getUrl());
		}
		System.out.println("-------------------------------------------------------------------------------------------\n\n");
		System.out.println(crawlingDomains.size() + "\n\n");
		crawlingDomains.clear();
	}

	/**
	 * Inicia el rastreo de URL.
	 *
	 * @param crawlerData the crawler data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void makeCrawl(final CrawlerData crawlerData) throws IOException {
		// Cookies
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		final int maxNumRetries = 3;
		final int maxNumRedirections = 15;
		final long timeRetry = 20000;
		List<IgnoredLink> ignoredLinks = null;
		String cookie = null;
		final int chosenDepth = crawlerData.getProfundidad();
		for (String url : crawlerData.getUrls()) {
			String domain = null;
			if (url != null && !url.isEmpty() && !url.toLowerCase().startsWith("javascript") && !url.toLowerCase().startsWith("mailto") && !url.toLowerCase().startsWith("tel")
					&& !url.toLowerCase().endsWith(".pdf") && !url.toLowerCase().endsWith(".doc") && !url.endsWith(".epub") && !url.endsWith(".xml") && !url.endsWith(".xls")
					&& !url.endsWith(".wsdl")) {
				try {
					HttpURLConnection connection = CrawlerUtils.getConnection(url, null, false);
					// Para la conexión inicial aumentamos los tiempos de espera
					connection.setConnectTimeout(connection.getConnectTimeout() * 2);
					connection.setReadTimeout(connection.getReadTimeout() * 2);
					int numRetries = 0;
					int numRedirections = 0;
					int responseCode = Integer.MAX_VALUE;
					while ((numRetries < maxNumRetries) && (responseCode >= HttpURLConnection.HTTP_MULT_CHOICE) && (numRedirections < maxNumRedirections)) {
						url = connection.getURL().toString();
						connection.connect();
						responseCode = connection.getResponseCode();
						if (numRedirections == 0) {
							cookie = CrawlerUtils.getCookie(connection);
						}
						if (responseCode < HttpURLConnection.HTTP_MULT_CHOICE && !contains(crawlingDomains, url) && !CrawlerUtils.isOpenDNSResponse(connection)) {
							// Si hay redirecciones, puede que el dominio cambie
							domain = connection.getURL().getHost();
							final InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(CrawlerUtils.generateRendererConnection(url, domain));
							// Generate renderer connection (applies proxy config)
							final String textContent = CrawlerUtils.getTextContent(CrawlerUtils.generateRendererConnection(url, domain), markableInputStream);
							markableInputStream.reset();
							// Recuerar el charset
							final String charset = CrawlerUtils.getCharset(connection, markableInputStream);
							markableInputStream.close();
							final Document document = CrawlerDOMUtils.getDocument(textContent);
							final String metaRedirect = CrawlerDOMUtils.getMetaRedirect(url, document);
							if (checkIfContentIsNotHTML(textContent)) {
								Logger.putLog(String.format("La url %s ha sido rechazada por ser un RSS", url), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
								rejectedDomains.add(url);
							} else if (StringUtils.isEmpty(metaRedirect)) {
								final String textContentHash = CrawlerUtils.getHash(textContent);
								// Si no está ya incluida en el rastreo
								if (!md5Content.contains(textContentHash)) {
									final CrawledLink crawledLink = new CrawledLink(url, textContent, numRetries, numRedirections);
									// Propagar el charset
									crawledLink.setCharset(charset);
									crawlingDomains.add(crawledLink);
									md5Content.add(textContentHash);
									Logger.putLog(String.format("Introducida la URL número %d: %s", crawlingDomains.size(), url), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
									// System.out.println(String.format("Introducida la URL número %d: %s", crawlingDomains.size(), url));
									if (crawlerData.getProfundidad() > 1 || crawlerData.getTopN() != 1) {
										// Si se trata de un observatorio, o la
										// petición viene del servicio básico
										if (crawlerData.getIdObservatory() != 0 || crawlerData.getIdCrawling() < 0) {
											// Cogemos lista de idiomas para no
											// coger enlaces de cambio de idioma
											ignoredLinks = Utils.getIgnoredLinks();
										}
										crawlerData.setCheckFormPage(true);
										crawlerData.setCheckTablePage(true);
										makeCrawl(domain, url, url, cookie, crawlerData, ignoredLinks);
									}
								} else {
									Logger.putLog(String.format("La url %s ha sido rechazada por estar incluida en el rastreo", url), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
									rejectedDomains.add(url);
								}
							} else {
								numRedirections++;
								connection = CrawlerUtils.followRedirection(cookie, new URL(url), metaRedirect);
								responseCode = Integer.MAX_VALUE;
							}
						} else if (responseCode >= HttpURLConnection.HTTP_MULT_CHOICE && responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
							numRedirections++;
							connection = CrawlerUtils.followRedirection(cookie, new URL(url), connection.getHeaderField("location"));
						} else {
							if (CrawlerUtils.isOpenDNSResponse(connection)) {
								Logger.putLog("La URL solicitada ha provocado la respuesta del OpenDNS", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
								if ((numRetries < maxNumRetries - 1) && (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST)) {
									Thread.sleep(timeRetry);
								}
								numRetries++;
							} else if (contains(crawlingDomains, url)) {
								Logger.putLog(String.format("La url %s ha sido rechazada por estar incluida en el rastreo", url), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
								rejectedDomains.add(url);
							} else {
								Logger.putLog(String.format("No se ha podido acceder a la raiz del rastreo configurado %s ya que ha respondido con el código %d", url, responseCode), CrawlerJob.class,
										Logger.LOG_LEVEL_INFO);
								if (numRetries < maxNumRetries - 1) {
									Thread.sleep(timeRetry);
								}
								numRetries++;
							}
						}
					}
					connection.disconnect();
					Collections.reverse(auxDomains);
					for (String auxDomain : auxDomains) {
						try {
							if ((crawlingDomains.size() <= (chosenDepth * crawlerData.getTopN()))) {
								if (!contains(crawlingDomains, auxDomain) && !rejectedDomains.contains(auxDomain)) {
									isLinkToAdd(url, domain, auxDomain, cookie, null, crawlerData, false, ignoredLinks, false, false);
								}
							} else {
								break;
							}
						} catch (Exception e) {
							Logger.putLog(String.format("Error al intentar introducir la url auxiliar %s: %s", auxDomain, e.getMessage()), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
						}
					}
					Logger.putLog(String.format("Terminado el rastreo para %s, se han recogido %d enlaces: ", url, crawlingDomains.size()), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
					for (CrawledLink crawledLink : crawlingDomains) {
						Logger.putLog(crawledLink.getUrl(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
					}
				} catch (Exception e) {
					Logger.putLog(String.format("Error al rastrear el dominio %s: %s", url, e.getMessage()), CrawlerJob.class, Logger.LOG_LEVEL_INFO, e);
				}
			} else {
				Logger.putLog("Rechazada la url " + url, CrawlerJob.class, Logger.LOG_LEVEL_INFO);
			}
		}
	}

	/**
	 * Contains.
	 *
	 * @param crawledLinks the crawled links
	 * @param url          the url
	 * @return true, if successful
	 */
	private boolean contains(List<CrawledLink> crawledLinks, String url) {
		for (CrawledLink crawledLink : crawledLinks) {
			if (crawledLink.getUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is link to add.
	 *
	 * @param rootUrl           the root url
	 * @param domain            the domain
	 * @param urlLink           the url link
	 * @param cookie            the cookie
	 * @param levelLinks        the level links
	 * @param crawlerData       the crawler data
	 * @param addAuxiliaryLinks the add auxiliary links
	 * @param ignoredLinks      the ignored links
	 * @return true, if is link to add
	 * @throws Exception the exception
	 */
	private boolean isLinkToAdd(String rootUrl, String domain, String urlLink, String cookie, List<CrawledLink> levelLinks, CrawlerData crawlerData, boolean addAuxiliaryLinks,
			List<IgnoredLink> ignoredLinks) throws Exception {
		return isHtmlTextContent(domain, urlLink, cookie) && hasAccessToUrl(rootUrl, domain, urlLink, cookie, levelLinks, crawlerData, addAuxiliaryLinks, ignoredLinks);
	}

	/**
	 * Checks if is html text content.
	 *
	 * @param domain  the domain
	 * @param urlLink the url link
	 * @param cookie  the cookie
	 * @return true, if is html text content
	 * @throws Exception the exception
	 */
	private boolean isHtmlTextContent(String domain, String urlLink, String cookie) throws Exception {
		HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, domain, true);
		connection.setRequestProperty("Cookie", cookie);
		connection.connect();
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			if (connection.getHeaderField("content-type") != null && connection.getHeaderField("content-type").contains("text/html")) {
				return true;
			} else {
				LOG.info("La url " + urlLink + " ha sido rechazada por no ser un documento de tipo text/html");
				rejectedDomains.add(urlLink);
			}
		} else {
			LOG.info("La url " + urlLink + " ha sido rechazada por devolver el código " + responseCode);
			rejectedDomains.add(urlLink);
		}
		connection.disconnect();
		return false;
	}

	/**
	 * Checks for access to url.
	 *
	 * @param rootUrl           the root url
	 * @param domain            the domain
	 * @param urlLink           the url link
	 * @param cookie            the cookie
	 * @param levelLinks        the level links
	 * @param crawlerData       the crawler data
	 * @param addAuxiliaryLinks the add auxiliary links
	 * @param ignoredLinks      the ignored links
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean hasAccessToUrl(String rootUrl, String domain, String urlLink, String cookie, List<CrawledLink> levelLinks, CrawlerData crawlerData, boolean addAuxiliaryLinks,
			List<IgnoredLink> ignoredLinks) throws Exception {
		HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, domain, false);
		connection.setRequestProperty("Cookie", cookie);
		int responseCode = Integer.MAX_VALUE;
		int numRetries = 0;
		int numRedirections = 0;
		PropertiesManager pmgr = new PropertiesManager();
		int maxNumRetries = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.retries"));
		int maxNumRedirections = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.redirections"));
		while ((responseCode >= HttpURLConnection.HTTP_MULT_CHOICE) && (numRetries < maxNumRetries) && (numRedirections < maxNumRedirections)) {
			urlLink = connection.getURL().toString();
			connection.connect();
			responseCode = connection.getResponseCode();
			if (responseCode > HttpURLConnection.HTTP_BAD_REQUEST) {
				numRetries++;
			} else if (responseCode >= HttpURLConnection.HTTP_MULT_CHOICE && responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
				numRedirections++;
				connection = CrawlerUtils.followRedirection(cookie, new URL(urlLink), connection.getHeaderField("location"));
			} else if (responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
				InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
				// String remoteContent = CrawlerUtils.getTextContent(connection,
				// markableInputStream);
				final String remoteContent = CrawlerUtils.getTextContent(CrawlerUtils.generateRendererConnection(urlLink, domain), markableInputStream);
				if (!CrawlerUtils.isRss(remoteContent)) {
					Document document = CrawlerDOMUtils.getDocument(remoteContent);
					String metaRedirect = CrawlerDOMUtils.getMetaRedirect(urlLink, document);
					if (StringUtils.isEmpty(metaRedirect)) {
						String remoteContentHash = CrawlerUtils.getHash(remoteContent);
						if (isValidUrl(rootUrl, domain, urlLink, crawlerData)) {
							if (!md5Content.contains(remoteContentHash)) {
								CrawledLink crawledLink = new CrawledLink(urlLink, remoteContent, numRetries, numRedirections);
								if (levelLinks != null) {
									levelLinks.add(crawledLink);
								}
								crawlingDomains.add(crawledLink);
								md5Content.add(remoteContentHash);
								LOG.info("Introducida la URL número " + crawlingDomains.size() + ": " + urlLink);
								// System.out.println("Introducida la URL número " + crawlingDomains.size() + ": " + urlLink);
								// System.out.println(String.format("Introducida la URL número %d: %s", crawlingDomains.size(), urlLink));
								return true;
							} else {
								LOG.info("La url " + urlLink + " ha sido rechazada por estar incluida en el rastreo");
								rejectedDomains.add(urlLink);
							}
							if (addAuxiliaryLinks) {
								List<String> urlLinks = CrawlerDOMUtils.getDomLinks(document, ignoredLinks);
								for (String urlLinkAux : urlLinks) {
									try {
										urlLinkAux = CrawlerUtils.getAbsoluteUrl(document, urlLink, CrawlerUtils.encodeUrl(urlLinkAux)).toString().replaceAll("\\.\\./", "");
										if (!auxDomains.contains(urlLinkAux)) {
											auxDomains.add(urlLinkAux);
										}
									} catch (Exception e) {
										LOG.info("La URL " + urlLink + " del enlace encontrado ha dado problemas de conexión: " + e.getMessage());
									}
								}
							}
						}
					} else {
						numRedirections++;
						connection = CrawlerUtils.followRedirection(cookie, new URL(urlLink), metaRedirect);
						responseCode = Integer.MAX_VALUE;
					}
				} else {
					LOG.info("La url " + urlLink + " ha sido rechazada por tratarse de un RSS");
				}
			} else {
				LOG.info("La url " + urlLink + " ha respondido con el código " + responseCode + " y no se le pasara al analizador");
			}
			connection.disconnect();
		}
		return false;
	}

	/**
	 * Realiza el rastreo completo de una URL.
	 *
	 * @param domain       the domain
	 * @param rootUrl      the root url
	 * @param url          the url
	 * @param cookie       the cookie
	 * @param crawlerData  the crawler data
	 * @param ignoredLinks the ignored links
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void makeCrawl(final String domain, final String rootUrl, final String url, final String cookie, final CrawlerData crawlerData, final List<IgnoredLink> ignoredLinks) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final int unlimitedTopN = Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "amplitud.ilimitada.value"));
		if (crawlerData.getProfundidad() > 0) {
			final List<CrawledLink> levelLinks = new ArrayList<>();
			final HttpURLConnection connection = CrawlerUtils.getConnection(url, domain, true);
			try {
				connection.setRequestProperty("Cookie", cookie);
				connection.connect();
				int responseCode = connection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					final InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(CrawlerUtils.generateRendererConnection(url, domain));
					// final String textContent = CrawlerUtils.getTextContent(connection,
					// markableInputStream);
					final String textContent = CrawlerUtils.getTextContent(CrawlerUtils.generateRendererConnection(url, domain), markableInputStream);
					markableInputStream.close();
					final String textContentHash = CrawlerUtils.getHash(textContent);
					if (!md5Content.contains(textContentHash)) {
						md5Content.add(textContentHash);
					}
					final Document document = CrawlerDOMUtils.getDocument(textContent);
					final List<String> urlLinks = CrawlerDOMUtils.getDomLinks(document, ignoredLinks);
					if (crawlerData.isPseudoaleatorio()) {
						Collections.shuffle(urlLinks, new Random(System.currentTimeMillis()));
					}
					int maxIntentosBuscarTipos = 0;
					int cont = 0;
					for (String urlLink : urlLinks) {
						try {
							final String absoluteUrlLink = CrawlerUtils.getAbsoluteUrl(document, url, CrawlerUtils.encodeUrl(urlLink)).toString().replaceAll("\\.\\./", EMPTY_STRING);
							if (isValidUrl(rootUrl, domain, absoluteUrlLink, crawlerData)) {
								if ((crawlerData.getTopN() == unlimitedTopN) || ((cont < crawlerData.getTopN()) && maxIntentosBuscarTipos < crawlerData.getMaxIntentosBuscarTipos())) {
									if (isLinkToAdd(rootUrl, domain, absoluteUrlLink, cookie, levelLinks, crawlerData, true, ignoredLinks, crawlerData.isCheckFormPage(),
											crawlerData.isCheckTablePage())) {
										cont++;
									} else if (!auxDomains.contains(absoluteUrlLink)) {
										// La guardamos como auxiliar
										// para que
										// la finalizar el reastreo se puedan
										// incluir si no llegamos al número de
										// URL requeridas en el rastreo y
										// aumentamos el contador de número
										// máximo de búsquedas
										// de tipos para salir de este bucle
										auxDomains.add(absoluteUrlLink);
										maxIntentosBuscarTipos++;
									}
								} else if (!auxDomains.contains(absoluteUrlLink)) {
									auxDomains.add(absoluteUrlLink);
									cont++;
								}
							}
						} catch (Exception e) {
							Logger.putLog("La URL " + urlLink + " del enlace encontrado ha dado problemas de conexión: " + e.getMessage(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
						}
					}
					crawlerData.setProfundidad(crawlerData.getProfundidad() - 1);
					for (CrawledLink levelLink : levelLinks) {
						makeCrawl(domain, rootUrl, levelLink.getUrl(), cookie, crawlerData, ignoredLinks);
					}
				}
			} catch (Exception e) {
				Logger.putLog("La url " + url + " ha dado problemas de conexión: " + e.getMessage(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
			}
		}
	}

	/**
	 * Checks if is valid url.
	 *
	 * @param urlRoot     the url root
	 * @param domain      the domain
	 * @param urlLink     the url link
	 * @param crawlerData the crawler data
	 * @return true, if is valid url
	 */
	private boolean isValidUrl(String urlRoot, String domain, String urlLink, CrawlerData crawlerData) {
		PropertiesManager pmgr = new PropertiesManager();
		if (urlLink.length() < Integer.parseInt(pmgr.getValue("crawler.core.properties", "link.chars.max.length"))) {
			if (crawlerData.isExhaustive() || !isOuterDomain(domain, urlLink)) {
				if (!crawlerData.isInDirectory() || isInTheSameDirectory(urlLink, urlRoot)) {
					if (!contains(crawlingDomains, urlLink)) {
						if (!rejectedDomains.contains(urlLink)) {
							if (crawlerData.getExceptions() == null || !CrawlerUtils.domainMatchs(crawlerData.getExceptions(), urlLink)) {
								if (crawlerData.getCrawlingList() == null || CrawlerUtils.domainMatchs(crawlerData.getCrawlingList(), urlLink)) {
									return true;
								} else {
									LOG.info("La URL " + urlLink + " ha sido rechazada por no estar incluida en la lista de dominio rastreable");
								}
							} else {
								LOG.info("La URL " + urlLink + " ha sido rechazada por estar incluida en la lista de excepciones");
							}
						} else {
							LOG.info("La URL " + urlLink + " ha sido rechazada por haber sido rechazada previamente");
						}
					} else {
						LOG.info("La URL " + urlLink + " ha sido rechazada por estar incluida en el rastreo");
					}
				} else {
					LOG.info("La URL " + urlLink + " ha sido rechazada por no estar en el mismo directorio pedido");
				}
			} else {
				LOG.info("La URL " + urlLink + " ha sido rechazada por ser encontrarse fuera del dominio");
			}
		} else {
			LOG.info("La URL " + urlLink + " ha sido rechazada por ser demasiado larga");
		}
		return false;
	}

	/**
	 * Comprueba si en los dominios registrados por el crawler existe una URL del mismo directorio que la URL dada.
	 * 
	 * Se excluye la comprobación si el directorio de la URL es el raiz de la página.
	 *
	 * @param domain  Dominio de la página.
	 * @param urlLink URL a comprobar
	 * @return true si ya existe otra URL del mismo directorio. false en caso contrario.
	 */
	private boolean checkIsSameDirectory(String domain, String urlLink) {
		boolean crawledLinkSameDirectory = false;
		// Es un directorio del que ya tenemos alguna página
		// Si es la raiz va a descartar todo
		// URLs en directorio
		if (urlLink.lastIndexOf("/") > 0) {
			// String directorio = urlLink.substring(0,
			// urlLink.lastIndexOf("/") + 1);
			String part1 = urlLink.substring(0, urlLink.indexOf(domain) + domain.length());
			String dirTmp = urlLink.substring(urlLink.indexOf(part1) + part1.length());
			String dirTmp2 = dirTmp.indexOf("/") != -1 ? dirTmp.substring(dirTmp.indexOf("/") + 1) : "";
			String directorio = dirTmp2 != "" && dirTmp2.indexOf("/") != -1 ? dirTmp2.substring(0, dirTmp2.indexOf("/")) : "";
			// Si el patrón está en la lista este de descarta temporalmente
			// Si el directorio no la raiz
			if (!StringUtils.isEmpty(directorio)) {
				for (CrawledLink link : crawlingDomains) {
					if (link != null && !StringUtils.isEmpty(link.getUrl()) && link.getUrl().startsWith(directorio)) {
						crawledLinkSameDirectory = true;
						Logger.putLog("*******  La URL " + urlLink + " se ha descartado inicialmente por existir otras en el mismo directorio.", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
						break;
					}
				}
			}
		}
		return crawledLinkSameDirectory;
	}

	/**
	 * Comprueba si un link es válido para añadir a los links a analizar.
	 *
	 * @param rootUrl           the root url
	 * @param domain            the domain
	 * @param urlLink           the url link
	 * @param cookie            the cookie
	 * @param levelLinks        the level links
	 * @param crawlerData       the crawler data
	 * @param addAuxiliaryLinks the add auxiliary links
	 * @param ignoredLinks      the ignored links
	 * @param checkIsFormPage   the check is form page
	 * @param checkIsTablePage  the check is table page
	 * @return true, if is link to add
	 * @throws Exception the exception
	 */
	private boolean isLinkToAdd(String rootUrl, String domain, String urlLink, String cookie, List<CrawledLink> levelLinks, CrawlerData crawlerData, boolean addAuxiliaryLinks,
			List<IgnoredLink> ignoredLinks, boolean checkIsFormPage, boolean checkIsTablePage) throws Exception {
		boolean ckecks = checkIsFormPage && checkIsTablePage;
		boolean isFormPage = false;
		boolean isTablePage = false;
		// Comprobar si es necesario buscar de tipos indicados
		if (checkIsFormPage || checkIsTablePage) {
			Document doc = loadDocumentFromURL(domain, urlLink, cookie);
			if (checkIsFormPage && doc != null && doc.getElementsByTagName("form") != null && doc.getElementsByTagName("form").getLength() != 0) {
				isTablePage = true;
				// Quitamos el flag ya que ya hemos encontrado al menos una
				// página de este tipo
				crawlerData.setCheckFormPage(false);
			}
			if (checkIsTablePage && doc != null && doc.getElementsByTagName("table") != null && doc.getElementsByTagName("table").getLength() != 0) {
				isTablePage = true;
				// Quitamos el flag ya que ya hemos encontrado al menos una
				// página de este tipo
				crawlerData.setCheckTablePage(false);
			}
		}
		// Ya existe otra en el mismo directorio entonces directamente
		// respondemos falso para que se guarde como auxiliar
		if (checkIsSameDirectory(domain, urlLink)) {
			return false;
		}
		// No es un directorio repetido en el directorio pero aún no encontramos
		// páginas con los
		// tipos indicados respondemos falso para que se guarde como auxiliar
		else if (ckecks && (!isFormPage || !isTablePage)) {
			return false;
		}
		// En caso contrario (no hay otra del mismo directorio y pasó los
		// checks) seguimos con las comprobaciones
		return isHtmlTextContent(domain, urlLink, cookie) && hasAccessToUrl(rootUrl, domain, urlLink, cookie, levelLinks, crawlerData, addAuxiliaryLinks, ignoredLinks);
	}

	/**
	 * Devuelve el DOM de una URL.
	 *
	 * @param domain  the domain
	 * @param urlLink the url link
	 * @param cookie  the cookie
	 * @return the document
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws Exception   the exception
	 */
	private Document loadDocumentFromURL(String domain, String urlLink, String cookie) throws IOException, Exception {
		Document doc = null;
		final HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, domain, true);
		connection.setRequestProperty("Cookie", cookie);
		connection.connect();
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			if (connection.getHeaderField("content-type") != null && connection.getHeaderField("content-type").contains("text/html")) {
				CheckAccessibility checkAccessibility = new CheckAccessibility();
				checkAccessibility.setUrl(urlLink);
				CheckerParser parser = new CheckerParser();
				String content = StringUtils.getContentAsString(connection.getInputStream(), "UTF-8");
				for (int i = 0; i < 2 && doc == null; i++) {
					content = addFinalTags(content);
					final InputStream newInputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
					if (newInputStream.markSupported()) {
						newInputStream.mark(Integer.MAX_VALUE);
					}
					try {
						// Reseteamos el stream para volver a analizarlo
						newInputStream.reset();
						final InputSource inputSource = new InputSource(newInputStream);
						parser.parse(inputSource);
						doc = parser.getDocument();
					} catch (Exception e) {
						parser = new CheckerParser(true);
						Logger.putLog("Error al parsear al documento. Se intentará de nuevo con el balanceo de etiquetas", EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
					}
				}
			}
		} else {
			Logger.putLog(String.format("La url %s ha sido rechazada por devolver el código %d", urlLink, responseCode), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
			rejectedDomains.add(urlLink);
		}
		connection.disconnect();
		return doc;
	}

	/**
	 * Cierra etiquetas mal cerradas para que el parseador no de error.
	 *
	 * @param inStr the in str
	 * @return the string
	 */
	private static String addFinalTags(String inStr) {
		final PropertiesManager pmgr = new PropertiesManager();
		final String regExpMatcher = pmgr.getValue("intav.properties", "incompleted.tags.reg.exp.matcher");
		final String[] tags = pmgr.getValue("intav.properties", "incompleted.tags").split(";");
		for (String tag : tags) {
			final Pattern pattern = Pattern.compile(regExpMatcher.replace("@", tag), Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(inStr);
			String matchedTag;
			while (matcher.find()) {
				matchedTag = matcher.group(1);
				inStr = inStr.replace(matchedTag, matchedTag.subSequence(0, matchedTag.length() - 1) + "/>");
			}
		}
		return inStr;
	}

	/**
	 * Check if content is not an HTML
	 * 
	 * This is mandatory if this page is passed by JS renderer that sometimes returns XML or other content no HTML as HTML header.
	 *
	 * @param textContent the text content
	 * @return true, if successful
	 */
	private boolean checkIfContentIsNotHTML(final String textContent) {
		return textContent.contains("</rss>");
	}
}
