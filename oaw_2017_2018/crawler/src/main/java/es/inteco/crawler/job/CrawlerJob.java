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
package es.inteco.crawler.job;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckerParser;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.gob.oaw.MailService;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.ignored.links.IgnoredLink;
import es.inteco.crawler.ignored.links.Utils;
import es.inteco.plugin.WebAnalayzer;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.RastreoDAO;
import es.inteco.utils.CrawlerDOMUtils;
import es.inteco.utils.CrawlerUtils;

/**
 * CrawlerJob. Clase para realizar el rastreo de URL.
 */
public class CrawlerJob implements InterruptableJob {

	/** Clave NOT_FILTERED_URIS_SECURITY_KEY. */
	private static final String NOT_FILTERED_URIS_SECURITY_KEY = "not.filtered.uris.security.key";

	/** Constante EMPTY_STRING. */
	private static final String EMPTY_STRING = "";

	/** Listado de dominios rastreados. */
	private final List<CrawledLink> crawlingDomains = new ArrayList<>();

	/**
	 * Listado de dominios auxiliares para completar los dominios rastreados.
	 */
	private final List<String> auxDomains = new ArrayList<>();

	/** Contenido de las URL rastreadas en MD5. */
	private final List<String> md5Content = new ArrayList<>();

	/** Listado de dominios rechazados. */
	private final List<String> rejectedDomains = new ArrayList<>();

	/** {@link MailService} */
	private final MailService mailService = new MailService();

	/** Solicitud de interrupción. */
	private boolean interrupt = false;

	/**
	 * Comprueba si una URL pertenece al dominio
	 *
	 * @param domain
	 *            Dominio
	 * @param url
	 *            URL a validar
	 * @return true, si la URL está fuera del dominio. false en caso contrario
	 */
	private boolean isOuterDomain(final String domain, final String url) {
		try {
			if (domain.equalsIgnoreCase(new URL(url).getHost())) {
				return false;
			}
		} catch (Exception e) {
			Logger.putLog("Error al obtener el dominio base de la URL", CrawlerJob.class, Logger.LOG_LEVEL_ERROR);
		}
		return true;
	}

	/**
	 * Comprueba si la lista de {@link CrawledLink} contiene una URL
	 *
	 * @param crawledLinks
	 *            Lista de {@link CrawledLink}.
	 * @param url
	 *            URL a validar.
	 * @return true, si la URL está en la lista. false en caso contrario.
	 */
	private boolean contains(final List<CrawledLink> crawledLinks, final String url) {
		for (CrawledLink crawledLink : crawledLinks) {
			if (crawledLink.getUrl().equals(url)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Comprueba si la URL está en el mismo directrorio que la raiz dada.
	 *
	 * @param link
	 *            the link
	 * @param urlRoot
	 *            the url root
	 * @return true, if is in the same directory
	 */
	private boolean isInTheSameDirectory(final String link, final String urlRoot) {
		final String protocolRegExp = "https?://";
		final String urlRootDirectory = urlRoot.replaceAll(protocolRegExp, EMPTY_STRING).lastIndexOf('/') != -1
				? urlRoot.replaceAll(protocolRegExp, EMPTY_STRING).substring(0, urlRoot.replaceAll(protocolRegExp, EMPTY_STRING).lastIndexOf('/'))
				: urlRoot.replaceAll(protocolRegExp, EMPTY_STRING);

		final String linkDirectory = link.replaceAll(protocolRegExp, EMPTY_STRING).lastIndexOf('/') != -1
				? link.replaceAll(protocolRegExp, EMPTY_STRING).substring(0, link.replaceAll(protocolRegExp, EMPTY_STRING).lastIndexOf('/'))
				: link.replaceAll(protocolRegExp, EMPTY_STRING);

		return linkDirectory.contains(urlRootDirectory);
	}

	/**
	 * Obtiene la lista de correos electrónicos de los administradores para
	 * enviar correos de aviso.
	 *
	 * @return una lista de cadenas que corresponden a los correos electrónicos
	 *         a los que hay que enviar el correo de aviso.
	 */
	private List<String> getAdministradoresMails() {
		final List<String> mails = new ArrayList<>();

		try (Connection c = DataBaseManager.getConnection();
				final PreparedStatement ps = c.prepareStatement("SELECT email FROM usuario u " + "LEFT JOIN usuario_rol ur ON (u.id_usuario = ur.usuario) " + "WHERE ur.id_rol = ?;")) {
			ps.setLong(1, 1);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					mails.add(rs.getString("email"));
				}
			}
		} catch (Exception e) {
			Logger.putLog("Error al obtener los correos de los administradores", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
		}
		// Ademas de avisar a los usuarios administrador se avisa a los correos
		// indicados en el fichero mail.properties
		final PropertiesManager pmg = new PropertiesManager();
		final String warningEmails = pmg.getValue(Constants.MAIL_PROPERTIES, "incomplete.crawler.emails");
		if (warningEmails != null) {
			mails.addAll(Arrays.asList(warningEmails.split(";")));
		}
		return mails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(final JobExecutionContext jobContext) throws JobExecutionException {
		final JobDataMap jobDataMap = jobContext.getJobDetail().getJobDataMap();
		final CrawlerData crawlerData = (CrawlerData) jobDataMap.get(Constants.CRAWLER_DATA);

		launchCrawler(crawlerData);
	}

	/**
	 * Lanza el crawler.
	 *
	 * @param crawlerData
	 *            the crawler data
	 */
	public void launchCrawler(final CrawlerData crawlerData) {
		try (Connection conn = DataBaseManager.getConnection()) {

			RastreoDAO.actualizarEstadoRastreo(conn, crawlerData.getIdCrawling(), Constants.STATUS_LAUNCHED);

			try {
				Logger.putLog("Iniciando el rastreo con id " + crawlerData.getIdCrawling(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);

				makeCrawl(crawlerData);

			} catch (Exception e) {
				Logger.putLog("Error al ejecutar el rastreo con id " + crawlerData.getIdCrawling(), CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);

				try {
					RastreoDAO.actualizarEstadoRastreo(conn, crawlerData.getIdCrawling(), es.inteco.crawler.common.Constants.STATUS_ERROR);
				} catch (Exception e2) {
					Logger.putLog("No se ha podido cambiar el estado del rastreo", CrawlerJob.class, Logger.LOG_LEVEL_ERROR);
				}

				// Intentamos enviar el error por correo
				final PropertiesManager pmgr = new PropertiesManager();

				mailService.sendMail((crawlerData.getUsersMail() != null && !crawlerData.getUsersMail().isEmpty()) ? crawlerData.getUsersMail() : getAdministradoresMails(),
						pmgr.getValue(Constants.MAIL_PROPERTIES, "error.mail.message.subject"), buildMensajeCorreo(pmgr.getValue(Constants.MAIL_PROPERTIES, "error.mail.message"), crawlerData));
			}

			endCrawling(DataBaseManager.getConnection(), crawlerData);

		} catch (Exception e) {
			Logger.putLog("Error al ejecutar el rastreo con id " + crawlerData.getIdCrawling(), CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Test crawler.
	 *
	 * @param crawlerData
	 *            the crawler data
	 * @return the list
	 */
	public List<CrawledLink> testCrawler(final CrawlerData crawlerData) {
		try {
			makeCrawl(crawlerData);
		} catch (Exception e) {
			Logger.putLog("Error al probar el rastreo.", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return crawlingDomains;
	}

	/**
	 * Lanza un análisis simple.
	 *
	 * @param crawlerData
	 *            the crawler data
	 * @return the list
	 */
	public List<CrawledLink> runSimpleAnalysis(final CrawlerData crawlerData) {
		final List<CrawledLink> simpleAnalysisiDomains = new ArrayList<>();
		final CrawledLink crawledLink = new CrawledLink(crawlerData.getNombreRastreo(), crawlerData.getContent(), 0, 0);
		simpleAnalysisiDomains.add(crawledLink);

		try {
			analyze(simpleAnalysisiDomains, crawlerData, EMPTY_STRING);
		} catch (Exception e) {
			Logger.putLog("Error al ejecutar el análisis simple", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return simpleAnalysisiDomains;
	}

	/**
	 * Finaliza el rastreo. Actualizando los datos en la base de datos.
	 *
	 * @param c
	 *            the c
	 * @param crawlerData
	 *            the crawler data
	 * @throws Exception
	 *             the exception
	 */
	private void endCrawling(final Connection c, final CrawlerData crawlerData) throws Exception {
		if (!interrupt) {
			final PropertiesManager pmgr = new PropertiesManager();
			Logger.putLog("Enviando el informe por correo electrónico", CrawlerJob.class, Logger.LOG_LEVEL_INFO);

			// Cambiamos el estado del rastreo a 'Finalizado'
			Logger.putLog("Cambiando el estado del rastreo " + crawlerData.getIdCrawling() + " a 'Finalizado' en la base de datos", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
			try {
				RastreoDAO.actualizarEstadoRastreo(c, crawlerData.getIdCrawling(), es.inteco.crawler.common.Constants.STATUS_FINALIZED);

				final int idCartucho = RastreoDAO.recuperarCartuchoPorRastreo(c, crawlerData.getIdCrawling());
				if (RastreoDAO.isCartuchoAccesibilidad(c, idCartucho)) {
					if (crawlerData.getUsersMail() != null && !crawlerData.getUsersMail().isEmpty()) {
						// Intentamos enviar los resultados del informe por
						// correo
						generatePDFFile(crawlerData);
						final String attachPath = pmgr.getValue("crawler.properties", "path.inteco.exports.intav") + crawlerData.getIdCrawling() + File.separator + crawlerData.getIdFulfilledCrawling()
								+ File.separator + crawlerData.getLanguage() + File.separator + pmgr.getValue("pdf.properties", "pdf.file.intav.name");

						mailService.sendMail((crawlerData.getUsersMail() != null && !crawlerData.getUsersMail().isEmpty()) ? crawlerData.getUsersMail() : getAdministradoresMails(),
								pmgr.getValue(Constants.MAIL_PROPERTIES, "mail.message.subject"), buildMensajeCorreo(pmgr.getValue(Constants.MAIL_PROPERTIES, "mail.message"), crawlerData), attachPath,
								"Informe.pdf");

					}

					if (crawlerData.getResponsiblesMail() != null && !crawlerData.getResponsiblesMail().isEmpty()) {
						// final URL url = new
						// URL(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES,
						// "pdf.executive.url.export").replace("{0}",
						// String.valueOf(crawlerData.getIdFulfilledCrawling()))
						// .replace("{1}",
						// String.valueOf(crawlerData.getIdCrawling()))
						// .replace("{2}",
						// pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES,
						// NOT_FILTERED_URIS_SECURITY_KEY)));
						//
						// mailService.sendMail(crawlerData.getResponsiblesMail(),
						// pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES,
						// "mail.message.subject"),
						// buildMensajeCorreo(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES,
						// "mail.message"), crawlerData), url, "Informe.pdf");
					}
				}
			} catch (Exception e) {
				Logger.putLog("Error al concluir el rastreo", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		} else {
			Logger.putLog("El rastreo " + crawlerData.getIdCrawling() + " ha sido detenido a petición del usuario", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
		}
	}

	/**
	 * Genera un PDF con la información del rastreo.
	 *
	 * @param crawlerData
	 *            the crawler data
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void generatePDFFile(final CrawlerData crawlerData) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();

		final String attachUrl = pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "pdf.url.export").replace("{0}", String.valueOf(crawlerData.getIdFulfilledCrawling()))
				.replace("{1}", String.valueOf(crawlerData.getIdCrawling())).replace("{2}", pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, NOT_FILTERED_URIS_SECURITY_KEY));

		Logger.putLog("Se va a pedir la url " + attachUrl.replace(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, NOT_FILTERED_URIS_SECURITY_KEY), "*******"), CrawlerJob.class,
				Logger.LOG_LEVEL_INFO);

		final HttpURLConnection connection = CrawlerUtils.getConnection(attachUrl, null, true);
		// Aumentamos el timeout porque puede que tarde en generarse
		connection.setConnectTimeout(20 * Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "crawler.timeout")));
		connection.setReadTimeout(20 * Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "crawler.timeout")));
		connection.connect();
		final int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			Logger.putLog("Generada con éxito la exportacón de la url " + attachUrl.replace(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, NOT_FILTERED_URIS_SECURITY_KEY), "*******"),
					CrawlerJob.class, Logger.LOG_LEVEL_INFO);
		} else {
			Logger.putLog("Error al pedir la url de la exportación " + attachUrl.replace(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, NOT_FILTERED_URIS_SECURITY_KEY), "*******"), CrawlerJob.class,
					Logger.LOG_LEVEL_ERROR);
		}
	}

	/**
	 * Compone un mensaje de correo electrónico.
	 *
	 * @param mensaje
	 *            the mensaje
	 * @param crawlerData
	 *            the crawler data
	 * @return the string
	 */
	private String buildMensajeCorreo(final String mensaje, final CrawlerData crawlerData) {
		final PropertiesManager pmgr = new PropertiesManager();

		return mensaje.replace("{0}", new SimpleDateFormat(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "crawler.date.format")).format(new Date())).replace("{1}", crawlerData.getUser())
				.replace("{2}", buildDominiosString(crawlerData.getDomains()));
	}

	/**
	 * Construye una cadena con los dominios de un crawler a partir de la lista
	 * de cadenas que representa los dominios.
	 *
	 * @param dominiosList
	 *            lista de cadenas de dominios pertenecientes a un crawler
	 * @return una cadena formada por la concatenación de cada una de las
	 *         cadenas de la lista
	 */
	private String buildDominiosString(final List<String> dominiosList) {
		final StringBuilder dominios = new StringBuilder();
		for (int i = 0; i < dominiosList.size(); i++) {
			if (dominiosList.size() == (i + 1)) {
				dominios.append(dominiosList.get(i));
			} else {
				dominios.append(dominiosList.get(i)).append(", ");
			}
		}
		return dominios.toString();
	}

	/**
	 * Inicia el rastreo de URL.
	 *
	 * @param crawlerData
	 *            the crawler data
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void makeCrawl(final CrawlerData crawlerData) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();

		final int maxNumRetries = Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "max.number.retries"));
		final int maxNumRedirections = Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "max.number.redirections"));
		final long timeRetry = Long.parseLong(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "time.retry"));

		List<IgnoredLink> ignoredLinks = null;

		String cookie = null;

		final int chosenDepth = crawlerData.getProfundidad();
		for (String url : crawlerData.getUrls()) {
			String domain = null;

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

						final InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
						final String textContent = CrawlerUtils.getTextContent(connection, markableInputStream);
						markableInputStream.close();
						final Document document = CrawlerDOMUtils.getDocument(textContent);

						final String metaRedirect = CrawlerDOMUtils.getMetaRedirect(url, document);
						if (StringUtils.isEmpty(metaRedirect)) {
							final String textContentHash = CrawlerUtils.getHash(textContent);

							// Si no está ya incluida en el rastreo
							if (!md5Content.contains(textContentHash)) {
								final CrawledLink crawledLink = new CrawledLink(url, textContent, numRetries, numRedirections);
								crawlingDomains.add(crawledLink);
								md5Content.add(textContentHash);
								Logger.putLog(String.format("Introducida la URL número %d: %s", crawlingDomains.size(), url), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
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
						if ((!interrupt) && (crawlingDomains.size() <= (chosenDepth * crawlerData.getTopN()))) {
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
		}

		// Avisa si se han rastreado menos páginas de las debidas y no es un
		// crawling de 'test'
		if (!crawlerData.isTest() && incompleteCrawl(crawlerData, chosenDepth)) {
			warnIncompleteCrawl(crawlerData);
		}

		// Llamamos al analizador
		// if (!crawlerData.isTest()) {
		analyze(crawlingDomains, crawlerData, cookie);
		// }
	}

	/**
	 * Comprueba si en el proceso de "crawling" se han obtenido menos páginas de
	 * las esperadas.
	 *
	 * @param crawlerData
	 *            la información CrawlerData del proceso
	 * @param crawlDepth
	 *            la profundidad
	 * @return true si no se han alcazado el número esperado de páginas o false
	 *         en caso contrario.
	 */
	private boolean incompleteCrawl(final CrawlerData crawlerData, final int crawlDepth) {
		if ((crawlerData.getUrls().size() != 1) || (crawlerData.getTopN() != 1 && crawlDepth != 1)) {
			if ((crawlingDomains.size() < crawlerData.getUrls().size()) || (crawlingDomains.size() < (crawlerData.getTopN() * crawlDepth + 1))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Envía un correo de aviso a los administradores de la aplicación si el
	 * proceso de "crawling" ha finalizado con menos páginas de las esperadas.
	 *
	 * @param crawlerData
	 *            la información CrawlerDate del proceso de crawling
	 */
	private void warnIncompleteCrawl(final CrawlerData crawlerData) {
		final PropertiesManager pmgr = new PropertiesManager();
		final List<String> mailTo = getAdministradoresMails();

		final String subject = MessageFormat.format(pmgr.getValue(Constants.MAIL_PROPERTIES, "incomplete.crawler.subject"), crawlerData.getNombreRastreo());
		final String text = MessageFormat.format(pmgr.getValue(Constants.MAIL_PROPERTIES, "incomplete.crawler.text"), crawlerData.getUrls().get(0), crawlingDomains.size());

		mailService.sendMail(mailTo, subject, text);
	}

	/**
	 * Analiza los datos recopilados.
	 *
	 * @param analyzeDomains
	 *            the analyze domains
	 * @param crawlerData
	 *            the crawler data
	 * @param cookie
	 *            the cookie
	 */
	private void analyze(final List<CrawledLink> analyzeDomains, final CrawlerData crawlerData, final String cookie) {
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "crawler.date.format"));

		int cont = 0;
		final WebAnalayzer webAnalyzer = new WebAnalayzer();
		for (CrawledLink crawledLink : analyzeDomains) {
			if (!interrupt && (crawlerData.getCartuchos() != null && crawlerData.getCartuchos().length > 0)) {
				final boolean isLast = (cont >= analyzeDomains.size() - 1);
				webAnalyzer.runCartuchos(crawledLink, df.format(new Date()), crawlerData, cookie, isLast);
			} else {
				// Si se pide interrupción, se abandonan los análisis
				break;
			}
			cont++;
		}
	}

	/**
	 * Comprueba si una URL es válida par añadir al rastreo.
	 *
	 * @param urlRoot
	 *            the url root
	 * @param domain
	 *            the domain
	 * @param urlLink
	 *            the url link
	 * @param crawlerData
	 *            the crawler data
	 * @return true, if is valid url
	 */
	private boolean isValidUrl(final String urlRoot, final String domain, final String urlLink, final CrawlerData crawlerData) {
		final PropertiesManager pmgr = new PropertiesManager();

		if (urlLink.length() < Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "link.chars.max.length"))) {
			if (crawlerData.isExhaustive() || !isOuterDomain(domain, urlLink)) {
				if (!crawlerData.isInDirectory() || isInTheSameDirectory(urlLink, urlRoot)) {
					if (!contains(crawlingDomains, urlLink)) {
						if (!rejectedDomains.contains(urlLink)) {
							if (crawlerData.getExceptions() == null || !CrawlerUtils.domainMatchs(crawlerData.getExceptions(), urlLink)) {
								if (crawlerData.getCrawlingList() == null || CrawlerUtils.domainMatchs(crawlerData.getCrawlingList(), urlLink)) {
									return true;
								} else {
									Logger.putLog(String.format("La URL %s ha sido rechazada por no estar incluida en la lista de dominio rastreable", urlLink), CrawlerJob.class,
											Logger.LOG_LEVEL_INFO);
								}
							} else {
								Logger.putLog(String.format("La URL %s ha sido rechazada por estar incluida en la lista de excepciones", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
							}
						} else {
							Logger.putLog(String.format("La URL %s ha sido rechazada por haber sido rechazada previamente", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
						}
					} else {
						Logger.putLog(String.format("La URL %s ha sido rechazada por estar incluida en el rastreo", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
					}
				} else {
					Logger.putLog(String.format("La URL %s ha sido rechazada por no estar en el mismo directorio pedido", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
				}
			} else {
				Logger.putLog(String.format("La URL %s ha sido rechazada por ser encontrarse fuera del dominio", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
			}
		} else {
			Logger.putLog(String.format("La URL %s ha sido rechazada por ser demasiado larga", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
		}

		return false;
	}

	/**
	 * Realiza el rastreo completo de una URL.
	 *
	 * @param domain
	 *            the domain
	 * @param rootUrl
	 *            the root url
	 * @param url
	 *            the url
	 * @param cookie
	 *            the cookie
	 * @param crawlerData
	 *            the crawler data
	 * @param ignoredLinks
	 *            the ignored links
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void makeCrawl(final String domain, final String rootUrl, final String url, final String cookie, final CrawlerData crawlerData, final List<IgnoredLink> ignoredLinks) throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final int unlimitedTopN = Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "amplitud.ilimitada.value"));
		if (crawlerData.getProfundidad() > 0 && !interrupt) {
			final List<CrawledLink> levelLinks = new ArrayList<>();
			final HttpURLConnection connection = CrawlerUtils.getConnection(url, domain, true);
			try {
				connection.setRequestProperty("Cookie", cookie);
				connection.connect();
				int responseCode = connection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					final InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
					final String textContent = CrawlerUtils.getTextContent(connection, markableInputStream);
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
	 * Comprueba si un link es válido para añadir a los links a analizar.
	 *
	 * @param rootUrl
	 *            the root url
	 * @param domain
	 *            the domain
	 * @param urlLink
	 *            the url link
	 * @param cookie
	 *            the cookie
	 * @param levelLinks
	 *            the level links
	 * @param crawlerData
	 *            the crawler data
	 * @param addAuxiliaryLinks
	 *            the add auxiliary links
	 * @param ignoredLinks
	 *            the ignored links
	 * @param checkIsFormPage
	 *            the check is form page
	 * @param checkIsTablePage
	 *            the check is table page
	 * @return true, if is link to add
	 * @throws Exception
	 *             the exception
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
		return

		isHtmlTextContent(domain, urlLink, cookie) && hasAccessToUrl(rootUrl, domain, urlLink, cookie, levelLinks, crawlerData, addAuxiliaryLinks, ignoredLinks);
	}

	/**
	 * Comprueba si en los dominios registrados por el crawler existe una URL
	 * del mismo directorio que la URL dada.
	 * 
	 * Se excluye la comprobación si el directorio de la URL es el raiz de la
	 * página.
	 *
	 * @param domain
	 *            Dominio de la página.
	 * @param urlLink
	 *            URL a comprobar
	 * @return true si ya existe otra URL del mismo directorio. false en caso
	 *         contrario.
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
	 * Devuelve el DOM de una URL.
	 *
	 * @param domain
	 *            the domain
	 * @param urlLink
	 *            the url link
	 * @param cookie
	 *            the cookie
	 * @return the document
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws Exception
	 *             the exception
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
	 * @param inStr
	 *            the in str
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
	 * Comprueba que la URL pedida sea de tipo text/html.
	 *
	 * @param refererUrl
	 *            cadena con la URL de la que proviene
	 * @param urlLink
	 *            cadena la URL de la página a comprobar
	 * @param cookie
	 *            cadena con las cookies enviadas por el servidor
	 * @return true si la URL corresponde a un recurso text/html o false en caso
	 *         contrario
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private boolean isHtmlTextContent(final String refererUrl, final String urlLink, final String cookie) throws IOException {
		final HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, refererUrl, true);
		connection.setRequestProperty("Cookie", cookie);
		connection.connect();
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			if (connection.getHeaderField("content-type") != null && connection.getHeaderField("content-type").contains("text/html")) {
				return true;
			} else {
				Logger.putLog(String.format("La url %s ha sido rechazada por no ser un documento de tipo text/html", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
				rejectedDomains.add(urlLink);
			}
		} else {
			Logger.putLog(String.format("La url %s ha sido rechazada por devolver el código %d", urlLink, responseCode), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
			rejectedDomains.add(urlLink);
		}
		connection.disconnect();
		return false;
	}

	/**
	 * Comprueba si se tiene acceso a una URL.
	 *
	 * @param rootUrl
	 *            the root url
	 * @param domain
	 *            the domain
	 * @param urlLink
	 *            the url link
	 * @param cookie
	 *            the cookie
	 * @param levelLinks
	 *            the level links
	 * @param crawlerData
	 *            the crawler data
	 * @param addAuxiliaryLinks
	 *            the add auxiliary links
	 * @param ignoredLinks
	 *            the ignored links
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean hasAccessToUrl(final String rootUrl, final String domain, final String urlLink, final String cookie, final List<CrawledLink> levelLinks, final CrawlerData crawlerData,
			final boolean addAuxiliaryLinks, final List<IgnoredLink> ignoredLinks) throws Exception {
		// Follow redirects en le primera conexión a la url
		HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, domain, true);
		connection.setRequestProperty("Cookie", cookie);
		int responseCode = Integer.MAX_VALUE;
		int numRetries = 0;
		int numRedirections = 0;

		final PropertiesManager pmgr = new PropertiesManager();
		final int maxNumRetries = Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "max.number.retries"));
		final int maxNumRedirections = Integer.parseInt(pmgr.getValue(Constants.CRAWLER_CORE_PROPERTIES, "max.number.redirections"));

		while ((responseCode >= HttpURLConnection.HTTP_MULT_CHOICE) && (numRetries < maxNumRetries) && (numRedirections < maxNumRedirections)) {
			final String connectedURL = connection.getURL().toString();
			connection.connect();
			responseCode = connection.getResponseCode();
			if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
				numRetries++;
			} else if (responseCode >= HttpURLConnection.HTTP_MULT_CHOICE && responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
				numRedirections++;
				connection = CrawlerUtils.followRedirection(cookie, new URL(connectedURL), connection.getHeaderField("location"));
			} else if (responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
				final InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
				final String remoteContent = CrawlerUtils.getTextContent(connection, markableInputStream);
				markableInputStream.close();
				if (!CrawlerUtils.isRss(remoteContent)) {
					final Document document = CrawlerDOMUtils.getDocument(remoteContent);
					final String metaRedirect = CrawlerDOMUtils.getMetaRedirect(connectedURL, document);
					if (StringUtils.isEmpty(metaRedirect)) {
						final String remoteContentHash = CrawlerUtils.getHash(remoteContent);
						if (isValidUrl(rootUrl, domain, connectedURL, crawlerData)) {
							if (!md5Content.contains(remoteContentHash)) {
								final CrawledLink crawledLink = new CrawledLink(connectedURL, remoteContent, numRetries, numRedirections);
								if (levelLinks != null) {
									levelLinks.add(crawledLink);
								}
								crawlingDomains.add(crawledLink);
								md5Content.add(remoteContentHash);
								Logger.putLog(String.format("Introducida la URL número %d: %s", crawlingDomains.size(), connectedURL), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
								return true;
							} else {
								Logger.putLog(String.format("La url %s ha sido rechazada por estar incluida en el rastreo", connectedURL), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
								rejectedDomains.add(connectedURL);
							}

							if (addAuxiliaryLinks) {
								final List<String> urlLinks = CrawlerDOMUtils.getDomLinks(document, ignoredLinks);
								for (String urlLinkAux : urlLinks) {
									try {
										urlLinkAux = CrawlerUtils.getAbsoluteUrl(document, connectedURL, CrawlerUtils.encodeUrl(urlLinkAux)).toString().replaceAll("\\.\\./", EMPTY_STRING);
										if (!auxDomains.contains(urlLinkAux)) {
											auxDomains.add(urlLinkAux);
										}
									} catch (Exception e) {
										Logger.putLog(String.format("La URL %s del enlace encontrado ha dado problemas de conexión: %s", connectedURL, e.getMessage()), CrawlerJob.class,
												Logger.LOG_LEVEL_INFO);
									}
								}
							}
						}
					} else {
						numRedirections++;
						connection = CrawlerUtils.followRedirection(cookie, new URL(connectedURL), metaRedirect);
						responseCode = Integer.MAX_VALUE;
					}
				} else {
					Logger.putLog(String.format("La url %s ha sido rechazada por tratarse de un RSS", connectedURL), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
				}
			} else {
				Logger.putLog(String.format("La url %s ha respondido con el código %d y no se le pasara al analizador", connectedURL, responseCode), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
			}
		}
		connection.disconnect();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.quartz.InterruptableJob#interrupt()
	 */
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		Logger.putLog("Se ha pedido una interrupción!!", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
		interrupt = true;
	}
}
