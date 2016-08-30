package es.inteco.crawler.job;

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
import es.inteco.utils.MailUtils;
import org.quartz.*;
import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CrawlerJob implements InterruptableJob {
    private final List<CrawledLink> crawlingDomains = new ArrayList<>();
    private final List<String> auxDomains = new ArrayList<>();
    private final List<String> md5Content = new ArrayList<>();
    private final List<String> rejectedDomains = new ArrayList<>();
    private boolean interrupt = false;

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

    private boolean contains(final List<CrawledLink> crawledLinks, final String url) {
        for (CrawledLink crawledLink : crawledLinks) {
            if (crawledLink.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInTheSameDirectory(final String link, final String urlRoot) {
        final String protocolRegExp = "https?://";
        final String urlRootDirectory = urlRoot.replaceAll(protocolRegExp, "").lastIndexOf("/") != -1 ?
                urlRoot.replaceAll(protocolRegExp, "").substring(0, urlRoot.replaceAll(protocolRegExp, "").lastIndexOf("/")) :
                urlRoot.replaceAll(protocolRegExp, "");

        final String linkDirectory = link.replaceAll(protocolRegExp, "").lastIndexOf("/") != -1 ?
                link.replaceAll(protocolRegExp, "").substring(0, link.replaceAll(protocolRegExp, "").lastIndexOf("/")) :
                link.replaceAll(protocolRegExp, "");

        return linkDirectory.contains(urlRootDirectory);
    }

    /**
     * Obtiene la lista de correos electrónicos de los administradores para enviar correos de aviso.
     *
     * @return una lista de cadenas que corresponden a los correos electrónicos a los que hay que enviar el correo de aviso.
     */
    private List<String> getAdministradoresMails() {
        final List<String> mails = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try (final Connection c = DataBaseManager.getConnection()) {
            if (c != null) {
                ps = c.prepareStatement("SELECT email FROM usuario u " +
                        "LEFT JOIN usuario_rol ur ON (u.id_usuario = ur.usuario) " +
                        "WHERE ur.id_rol = ?;");
                ps.setLong(1, 1);

                rs = ps.executeQuery();

                while (rs.next()) {
                    mails.add(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            Logger.putLog("Error al obtener los correos de los administradores", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                Logger.putLog("Error al cerrar el objeto PreparedStatement", RastreoDAO.class, Logger.LOG_LEVEL_WARNING, e);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.putLog("Error al cerrar el objeto PreparedStatement", RastreoDAO.class, Logger.LOG_LEVEL_WARNING, e);
            }
        }
        // Ademas de avisar a los usuarios administrador se avisa a los correos indicados en el fichero crawler.core.properties
        final PropertiesManager pmg = new PropertiesManager();
        final String warningEmails = pmg.getValue("crawler.core.properties", "incomplete.crawler.warning.emails");
        if (warningEmails != null) {
            mails.addAll(Arrays.asList(warningEmails.split(";")));
        }
        return mails;
    }

    @Override
    public void execute(final JobExecutionContext jobContext) throws JobExecutionException {
        final JobDataMap jobDataMap = jobContext.getJobDetail().getJobDataMap();
        final CrawlerData crawlerData = (CrawlerData) jobDataMap.get(Constants.CRAWLER_DATA);

        launchCrawler(crawlerData);
    }

    public void launchCrawler(final CrawlerData crawlerData) {
        try (final Connection conn = DataBaseManager.getConnection()) {

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
                MailUtils.sendMail(pmgr.getValue("crawler.core.properties", "mail.address.from"), pmgr.getValue("crawler.core.properties", "mail.address.from.name"),
                        crawlerData.getUsersMail(), pmgr.getValue("crawler.core.properties", "error.mail.message.subject"),
                        buildMensajeCorreo(pmgr.getValue("crawler.core.properties", "error.mail.message"), crawlerData), null, null, null, null, true);
            }

            endCrawling(conn, crawlerData);

        } catch (Exception e) {
            Logger.putLog("Error al ejecutar el rastreo con id " + crawlerData.getIdCrawling(), CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public List<CrawledLink> testCrawler(final CrawlerData crawlerData) {
        try {
            makeCrawl(crawlerData);
        } catch (Exception e) {
            Logger.putLog("Error al probar el rastreo.", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return crawlingDomains;
    }

    public List<CrawledLink> runSimpleAnalysis(final CrawlerData crawlerData) {
        final List<CrawledLink> simpleAnalysisiDomains = new ArrayList<>();
        final CrawledLink crawledLink = new CrawledLink(crawlerData.getNombreRastreo(), crawlerData.getContent(), 0, 0);
        simpleAnalysisiDomains.add(crawledLink);

        try {
            analyze(simpleAnalysisiDomains, crawlerData, "");
        } catch (Exception e) {
            Logger.putLog("Error al ejecutar el análisis simple", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return simpleAnalysisiDomains;
    }

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
                        // Intentamos enviar los resultados del informe por correo
                        generatePDFFile(crawlerData);
                        final String attachPath = pmgr.getValue("crawler.properties", "path.inteco.exports.intav") + crawlerData.getIdCrawling()
                                + File.separator + crawlerData.getIdFulfilledCrawling()
                                + File.separator + crawlerData.getLanguage()
                                + File.separator + pmgr.getValue("pdf.properties", "pdf.file.intav.name");

                        MailUtils.sendMail(pmgr.getValue("crawler.core.properties", "mail.address.from"), pmgr.getValue("crawler.core.properties", "mail.address.from.name"),
                                crawlerData.getUsersMail(), pmgr.getValue("crawler.core.properties", "mail.message.subject"),
                                buildMensajeCorreo(pmgr.getValue("crawler.core.properties", "mail.message"), crawlerData), attachPath, "Informe.pdf", null, null, false);
                    }

                    if (crawlerData.getResponsiblesMail() != null && !crawlerData.getResponsiblesMail().isEmpty()) {
                        final URL url = new URL(pmgr.getValue("crawler.core.properties", "pdf.executive.url.export").replace("{0}", String.valueOf(crawlerData.getIdFulfilledCrawling()))
                                .replace("{1}", String.valueOf(crawlerData.getIdCrawling()))
                                .replace("{2}", pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key")));

                        MailUtils.sendExecutiveMail(pmgr.getValue("crawler.core.properties", "mail.address.from"), pmgr.getValue("crawler.core.properties", "mail.address.from.name"),
                                crawlerData.getResponsiblesMail(), pmgr.getValue("crawler.core.properties", "mail.message.subject"),
                                buildMensajeCorreo(pmgr.getValue("crawler.core.properties", "mail.message"), crawlerData), url, "Informe.pdf", null, null, false);
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

    private void generatePDFFile(final CrawlerData crawlerData) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();

        final String attachUrl = pmgr.getValue("crawler.core.properties", "pdf.url.export").replace("{0}", String.valueOf(crawlerData.getIdFulfilledCrawling()))
                .replace("{1}", String.valueOf(crawlerData.getIdCrawling()))
                .replace("{2}", pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"));

        Logger.putLog("Se va a pedir la url " + attachUrl.replace(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"), "*******"), CrawlerJob.class, Logger.LOG_LEVEL_INFO);

        final HttpURLConnection connection = CrawlerUtils.getConnection(attachUrl, null, true);
        // Aumentamos el timeout porque puede que tarde en generarse
        connection.setConnectTimeout(20 * Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
        connection.setReadTimeout(20 * Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
        connection.connect();
        final int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Logger.putLog("Generada con éxito la exportacón de la url " + attachUrl.replace(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"), "*******"), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
        } else {
            Logger.putLog("Error al pedir la url de la exportación " + attachUrl.replace(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"), "*******"), CrawlerJob.class, Logger.LOG_LEVEL_ERROR);
        }
    }

    private String buildMensajeCorreo(final String mensaje, final CrawlerData crawlerData) {
        final PropertiesManager pmgr = new PropertiesManager();

        return mensaje
                .replace("{0}", new SimpleDateFormat(pmgr.getValue("crawler.core.properties", "crawler.date.format")).format(new Date()))
                .replace("{1}", crawlerData.getUser())
                .replace("{2}", buildDominiosString(crawlerData.getDomains()));
    }

    /**
     * Construye una cadena con los dominios de un crawler a partir de la lista de cadenas que representa los dominios
     *
     * @param dominiosList lista de cadenas de dominios pertenecientes a un crawler
     * @return una cadena formada por la concatenación de cada una de las cadenas de la lista
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

    public void makeCrawl(final CrawlerData crawlerData) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();

        final int maxNumRetries = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.retries"));
        final int maxNumRedirections = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.redirections"));
        final long timeRetry = Long.parseLong(pmgr.getValue("crawler.core.properties", "time.retry"));

        List<IgnoredLink> ignoredLinks = null;

        String cookie = null;

        final int chosenDepth = crawlerData.getProfundidad();
        for (String url : crawlerData.getUrls()) {
            String domain = null;

            try {
                HttpURLConnection connection = CrawlerUtils.getConnection(url, null, false);
                // Initial connection has greater timeout limit than other connections
                connection.setConnectTimeout(connection.getConnectTimeout()*2);
                connection.setReadTimeout(connection.getReadTimeout()*2);

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
                        String textContent = CrawlerUtils.getTextContent(connection, markableInputStream);
                        markableInputStream.close();
                        // Si se utiliza iframe como etiqueta simple (sin cuerpo) se produce problema al parsear, las eliminamos sin más
                        textContent = textContent.replaceAll("(?i)<iframe [^>]*/>", "");
                        final Document document = CrawlerDOMUtils.getDocument(textContent);

                        final String metaRedirect = CrawlerDOMUtils.getMetaRedirect(url, document);
                        if (StringUtils.isEmpty(metaRedirect)) {
                            final String textContentHash = CrawlerUtils.getHash(textContent);
                            if (!md5Content.contains(textContentHash)) {
                                final CrawledLink crawledLink = new CrawledLink(url, textContent, numRetries, numRedirections);
                                crawlingDomains.add(crawledLink);
                                md5Content.add(textContentHash);
                                Logger.putLog(String.format("Introducida la URL número %d: %s", crawlingDomains.size(), url), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
                                if (crawlerData.getProfundidad() > 1 || crawlerData.getTopN() != 1) {
                                    // Si se trata de un observatorio, o la petición viene del servicio básico
                                    if (crawlerData.getIdObservatory() != 0 || crawlerData.getIdCrawling() < 0) {
                                        //Cogemos lista de idiomas para no coger enlaces de cambio de idioma
                                        ignoredLinks = Utils.getIgnoredLinks();
                                    }
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
                            Logger.putLog(String.format("No se ha podido acceder a la raiz del rastreo configurado %s ya que ha respondido con el código %d", url, responseCode), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
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
                                isLinkToAdd(url, domain, auxDomain, cookie, null, crawlerData, false, ignoredLinks);
                            }
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        Logger.putLog("Error al intentar introducir la url auxiliar " + auxDomain + ": " + e.getMessage(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
                    }
                }

                Logger.putLog("Terminado el rastreo para " + url + ", se han recogido " + crawlingDomains.size() + " enlaces: ", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
                for (CrawledLink crawledLink : crawlingDomains) {
                    Logger.putLog(crawledLink.getUrl(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
                }

            } catch (Exception e) {
                Logger.putLog("Error al rastrear el dominio " + url + ": " + e.getMessage(), CrawlerJob.class, Logger.LOG_LEVEL_INFO, e);
            }
        }

        // Avisa si se han rastreado menos páginas de las debidas
        if (incompleteCrawl(crawlerData, chosenDepth)) {
            warnIncompleteCrawl(crawlerData);
        }

        // Llamamos al analizador
        analyze(crawlingDomains, crawlerData, cookie);
    }

    /**
     * Comprueba si en el proceso de "crawling" se han obtenido menos páginas de las esperadas
     * @param crawlerData la información CrawlerData del proceso
     * @param crawlDepth la profundidad
     * @return true si no se han alcazado el número esperado de páginas o false en caso contrario.
     */
    private boolean incompleteCrawl(final CrawlerData crawlerData, final int crawlDepth) {
        if (!crawlerData.isTest()) {
            if ((crawlerData.getUrls().size() != 1) || (crawlerData.getTopN() != 1 && crawlDepth != 1)) {
                if ((crawlingDomains.size() < crawlerData.getUrls().size()) || (crawlingDomains.size() < (crawlerData.getTopN() * crawlDepth + 1))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Envía un correo de aviso a los administradores de la aplicación si el proceso de "crawling" ha finalizado con menos páginas de las esperadas.
     * @param crawlerData la información CrawlerDate del proceso de crawling
     */
    private void warnIncompleteCrawl(final CrawlerData crawlerData) {
        final PropertiesManager pmgr = new PropertiesManager();
        final List<String> mailTo = getAdministradoresMails();
        final String text = String.format("El rastreo para %s ha devuelto solo %d resultados", crawlerData.getUrls().get(0), crawlingDomains.size());

        MailUtils.sendMail(pmgr.getValue("crawler.core.properties", "mail.address.from"), "Rastreador Web de MINHAP", mailTo, "Rastreo inacabado", text, null, null, null, null, true);
    }

    private void analyze(final List<CrawledLink> analyzeDomains, final CrawlerData crawlerData, final String cookie) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final DateFormat df = new SimpleDateFormat(pmgr.getValue("crawler.core.properties", "crawler.date.format"));

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

    private boolean isValidUrl(final String urlRoot, final String domain, final String urlLink, final CrawlerData crawlerData) {
        final PropertiesManager pmgr = new PropertiesManager();

        if (urlLink.length() < Integer.parseInt(pmgr.getValue("crawler.core.properties", "link.chars.max.length"))) {
            if (crawlerData.isExhaustive() || !isOuterDomain(domain, urlLink)) {
                if (!crawlerData.isInDirectory() || isInTheSameDirectory(urlLink, urlRoot)) {
                    if (!contains(crawlingDomains, urlLink)) {
                        if (!rejectedDomains.contains(urlLink)) {
                            if (crawlerData.getExceptions() == null || !CrawlerUtils.domainMatchs(crawlerData.getExceptions(), urlLink)) {
                                if (crawlerData.getCrawlingList() == null || CrawlerUtils.domainMatchs(crawlerData.getCrawlingList(), urlLink)) {
                                    return true;
                                } else {
                                    Logger.putLog(String.format("La URL %s ha sido rechazada por no estar incluida en la lista de dominio rastreable", urlLink), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
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

    private void makeCrawl(final String domain, final String rootUrl, final String url, final String cookie, final CrawlerData crawlerData, final List<IgnoredLink> ignoredLinks) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final int unlimitedTopN = Integer.parseInt(pmgr.getValue("crawler.core.properties", "amplitud.ilimitada.value"));
        if (crawlerData.getProfundidad() > 0 && !interrupt) {
            final List<CrawledLink> levelLinks = new ArrayList<>();
            final HttpURLConnection connection = CrawlerUtils.getConnection(url, domain, true);
            try {
                connection.setRequestProperty("Cookie", cookie);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    final InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
                    String textContent = CrawlerUtils.getTextContent(connection, markableInputStream);
                    markableInputStream.close();
                    final String textContentHash = CrawlerUtils.getHash(textContent);

                    if (!md5Content.contains(textContentHash)) {
                        md5Content.add(textContentHash);
                    }

                    connection.disconnect();
                    // Si se utiliza iframe como etiqueta simple (sin cuerpo) se produce problema al parsear, las eliminamos sin más
                    textContent = textContent.replaceAll("(?i)<iframe [^>]*/>", "");
                    final Document document = CrawlerDOMUtils.getDocument(textContent);
                    final List<String> urlLinks = CrawlerDOMUtils.getDomLinks(document, ignoredLinks);

                    if (crawlerData.isPseudoaleatorio()) {
                        Collections.shuffle(urlLinks, new Random(System.currentTimeMillis()));
                    }

                    int cont = 0;
                    for (String urlLink : urlLinks) {
                        try {
                            urlLink = CrawlerUtils.getAbsoluteUrl(document, url, CrawlerUtils.encodeUrl(urlLink)).toString().replaceAll("\\.\\./", "");

                            if (isValidUrl(rootUrl, domain, urlLink, crawlerData)) {
                                if ((crawlerData.getTopN() == unlimitedTopN) || (cont < crawlerData.getTopN())) {
                                    if (isLinkToAdd(rootUrl, domain, urlLink, cookie, levelLinks, crawlerData, true, ignoredLinks)) {
                                        cont++;
                                    }
                                } else if (!auxDomains.contains(urlLink)) {
                                    auxDomains.add(urlLink);
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

    private boolean isLinkToAdd(String rootUrl, String domain, String urlLink, String cookie, List<CrawledLink> levelLinks, CrawlerData crawlerData, boolean addAuxiliaryLinks, List<IgnoredLink> ignoredLinks) throws Exception {
        return isHtmlTextContent(domain, urlLink, cookie) && hasAccessToUrl(rootUrl, domain, urlLink, cookie, levelLinks, crawlerData, addAuxiliaryLinks, ignoredLinks);
    }

    /**
     * Comprueba que la URL pedida sea de tipo text/html.
     *
     * @param refererUrl cadena con la URL de la que proviene
     * @param urlLink    cadena la URL de la página a comprobar
     * @param cookie     cadena con las cookies enviadas por el servidor
     * @return true si la URL corresponde a un recurso text/html o false en caso contrario
     * @throws Exception
     */
    private boolean isHtmlTextContent(final String refererUrl, final String urlLink, final String cookie) throws Exception {
        final HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, refererUrl, true);
        connection.setRequestProperty("Cookie", cookie);
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            if (connection.getHeaderField("content-type") != null &&
                    connection.getHeaderField("content-type").contains("text/html")) {
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

    private boolean hasAccessToUrl(final String rootUrl, final String domain, final String urlLink, final String cookie, final List<CrawledLink> levelLinks, final CrawlerData crawlerData, final boolean addAuxiliaryLinks, final List<IgnoredLink> ignoredLinks) throws Exception {
        HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, domain, false);
        connection.setRequestProperty("Cookie", cookie);
        int responseCode = Integer.MAX_VALUE;
        int numRetries = 0;
        int numRedirections = 0;

        final PropertiesManager pmgr = new PropertiesManager();
        final int maxNumRetries = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.retries"));
        final int maxNumRedirections = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.redirections"));

        while ((responseCode >= HttpURLConnection.HTTP_MULT_CHOICE) && (numRetries < maxNumRetries) && (numRedirections < maxNumRedirections)) {
            final String connectedURL = connection.getURL().toString();
            connection.connect();
            responseCode = connection.getResponseCode();
            if (responseCode > HttpURLConnection.HTTP_BAD_REQUEST) {
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
                                        urlLinkAux = CrawlerUtils.getAbsoluteUrl(document, connectedURL, CrawlerUtils.encodeUrl(urlLinkAux)).toString().replaceAll("\\.\\./", "");
                                        if (!auxDomains.contains(urlLinkAux)) {
                                            auxDomains.add(urlLinkAux);
                                        }
                                    } catch (Exception e) {
                                        Logger.putLog(String.format("La URL %s del enlace encontrado ha dado problemas de conexión: %s", connectedURL, e.getMessage()), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
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

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        Logger.putLog("Se ha pedido una interrupción!!", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
        interrupt = true;
    }
}
