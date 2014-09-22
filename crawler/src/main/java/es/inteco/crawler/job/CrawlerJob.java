package es.inteco.crawler.job;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.ignored.links.IgnoredLink;
import es.inteco.crawler.ignored.links.Utils;
import es.inteco.intav.utils.StringUtils;
import es.inteco.plugin.WebAnalayzer;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.RastreoDAO;
import es.inteco.utils.CrawlerDOMUtils;
import es.inteco.utils.CrawlerUtils;
import es.inteco.utils.MailUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CrawlerJob implements InterruptableJob {
    boolean interrupt = false;

    public static final Log LOG = LogFactory.getLog(CrawlerJob.class);

    List<CrawledLink> crawlingDomains = new ArrayList<CrawledLink>();
    List<String> auxDomains = new ArrayList<String>();
    List<String> md5Content = new ArrayList<String>();
    List<String> rejectedDomains = new ArrayList<String>();

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobContext.getJobDetail().getJobDataMap();
        CrawlerData crawlerData = (CrawlerData) jobDataMap.get(Constants.CRAWLER_DATA);

        launchCrawler(crawlerData);
    }

    public void launchCrawler(CrawlerData crawlerData) {
        Connection conn = null;

        try {
            conn = DataBaseManager.getConnection();
            RastreoDAO.actualizarEstadoRastreo(conn, crawlerData.getIdCrawling(), Constants.STATUS_LAUNCHED);

            try {
                Logger.putLog("Iniciando el rastreo con id " + crawlerData.getIdCrawling(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
                makeCrawl(crawlerData);
            } catch (Exception e) {
                Logger.putLog("Error al ejecutar el rastreo con id " + crawlerData.getIdCrawling(), CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);

                try {
                    RastreoDAO.actualizarEstadoRastreo(conn, crawlerData.getIdCrawling(), es.inteco.crawler.common.Constants.STATUS_ERROR);
                } catch (Exception e2) {
                    LOG.error("No se ha podido cambiar el estado del rastreo");
                }

                // Intentamos enviar el error por correo
                PropertiesManager pmgr = new PropertiesManager();
                MailUtils.sendMail(pmgr.getValue("crawler.core.properties", "mail.address.from"), pmgr.getValue("crawler.core.properties", "mail.address.from.name"),
                        crawlerData.getUsersMail(), pmgr.getValue("crawler.core.properties", "error.mail.message.subject"),
                        buildMensajeCorreo(pmgr.getValue("crawler.core.properties", "error.mail.message"), crawlerData), null, null, null, null, true);
            }

            endCrawling(conn, crawlerData);

        } catch (Exception e) {
            Logger.putLog("Error al ejecutar el rastreo con id " + crawlerData.getIdCrawling(), CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }

    public List<CrawledLink> testCrawler(CrawlerData crawlerData) {
        try {
            makeCrawl(crawlerData);
        } catch (Exception e) {
            Logger.putLog("Error al probar el rastreo.", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return crawlingDomains;
    }

    public List<CrawledLink> runSimpleAnalysis(CrawlerData crawlerData) {
        List<CrawledLink> crawlingDomains = new ArrayList<CrawledLink>();
        CrawledLink crawledLink = new CrawledLink(crawlerData.getNombreRastreo(), crawlerData.getContent(), 0, 0);
        crawlingDomains.add(crawledLink);

        try {
            analyze(crawlingDomains, crawlerData, "");
        } catch (Exception e) {
            Logger.putLog("Error al ejecutar el análisis simple", CrawlerJob.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return crawlingDomains;
    }

    private void endCrawling(Connection c, CrawlerData crawlerData) throws Exception {
        if (!interrupt) {
            PropertiesManager pmgr = new PropertiesManager();
            Logger.putLog("Enviando el informe por correo electrï¿½nico", CrawlerJob.class, Logger.LOG_LEVEL_INFO);

            // Cambiamos el estado del rastreo a 'Finalizado'
            LOG.info("Cambiando el estado del rastreo " + crawlerData.getIdCrawling() + " a 'Finalizado' en la base de datos");
            try {
                RastreoDAO.actualizarEstadoRastreo(c, crawlerData.getIdCrawling(), es.inteco.crawler.common.Constants.STATUS_FINALIZED);

                int idCartucho = RastreoDAO.recuperarCartuchoPorRastreo(c, crawlerData.getIdCrawling());
                if (idCartucho == Integer.parseInt(pmgr.getValue("crawler.core.properties", "cartridge.intav.id"))) {
                    if (crawlerData.getUsersMail() != null && !crawlerData.getUsersMail().isEmpty()) {
                        // Intentamos enviar los resultados del informe por correo
                        generatePDFFile(crawlerData);
                        String attachPath = pmgr.getValue("crawler.properties", "path.inteco.exports.intav") + crawlerData.getIdCrawling()
                                + File.separator + crawlerData.getIdFulfilledCrawling()
                                + File.separator + crawlerData.getLanguage()
                                + File.separator + pmgr.getValue("pdf.properties", "pdf.file.intav.name");

                        MailUtils.sendMail(pmgr.getValue("crawler.core.properties", "mail.address.from"), pmgr.getValue("crawler.core.properties", "mail.address.from.name"),
                                crawlerData.getUsersMail(), pmgr.getValue("crawler.core.properties", "mail.message.subject"),
                                buildMensajeCorreo(pmgr.getValue("crawler.core.properties", "mail.message"), crawlerData), attachPath, "Informe.pdf", null, null, false);
                    }

                    if (crawlerData.getResponsiblesMail() != null && !crawlerData.getResponsiblesMail().isEmpty()) {
                        URL url = new URL(pmgr.getValue("crawler.core.properties", "pdf.executive.url.export").replace("{0}", String.valueOf(crawlerData.getIdFulfilledCrawling()))
                                .replace("{1}", String.valueOf(crawlerData.getIdCrawling()))
                                .replace("{2}", pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key")));

                        MailUtils.sendExecutiveMail(pmgr.getValue("crawler.core.properties", "mail.address.from"), pmgr.getValue("crawler.core.properties", "mail.address.from.name"),
                                crawlerData.getResponsiblesMail(), pmgr.getValue("crawler.core.properties", "mail.message.subject"),
                                buildMensajeCorreo(pmgr.getValue("crawler.core.properties", "mail.message"), crawlerData), url, "Informe.pdf", null, null, false);
                    }
                }
            } catch (Exception e) {
                LOG.error("Error al concluir el rastreo");
                throw e;
            }
        } else {
            LOG.info("El rastreo " + crawlerData.getIdCrawling() + " ha sido detenido a petición del usuario");
        }
    }

    private void generatePDFFile(CrawlerData crawlerData) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();

        String attachUrl = pmgr.getValue("crawler.core.properties", "pdf.url.export").replace("{0}", String.valueOf(crawlerData.getIdFulfilledCrawling()))
                .replace("{1}", String.valueOf(crawlerData.getIdCrawling()))
                .replace("{2}", pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"));

        LOG.info("Se va a pedir la url " + attachUrl.replace(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"), "*******"));

        HttpURLConnection connection = CrawlerUtils.getConnection(attachUrl, null, true);
        // Aumentamos el timeout porque puede que tarde en generarse
        connection.setConnectTimeout(20 * Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
        connection.setReadTimeout(20 * Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            LOG.info("Generada con éxito la exportacón de la url " + attachUrl.replace(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"), "*******"));
        } else {
            LOG.error("Error al pedir la url de la exportación " + attachUrl.replace(pmgr.getValue("crawler.core.properties", "not.filtered.uris.security.key"), "*******"));
        }
    }

    private String buildMensajeCorreo(String mensaje, CrawlerData crawlerData) {
        PropertiesManager pmgr = new PropertiesManager();

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

    public void makeCrawl(CrawlerData crawlerData) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();

        int maxNumRetries = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.retries"));
        long timeRetry = Long.parseLong(pmgr.getValue("crawler.core.properties", "time.retry"));

        List<IgnoredLink> ignoredLinks = null;

        String cookie = null;

        int chosenDepth = crawlerData.getProfundidad();
        for (String url : crawlerData.getUrls()) {
            String domain = null;

            try {
                HttpURLConnection connection = CrawlerUtils.getConnection(url, null, false);
                int counter = 0;
                int numRedirections = 0;
                int maxNumRedirections = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.number.redirections"));
                int responseCode = Integer.MAX_VALUE;

                while ((counter < maxNumRetries) && (responseCode >= HttpURLConnection.HTTP_MULT_CHOICE) && (numRedirections < maxNumRedirections)) {
                    url = connection.getURL().toString();
                    connection.connect();
                    responseCode = connection.getResponseCode();
                    if (numRedirections == 0) {
                        cookie = CrawlerUtils.getCookie(connection);
                    }
                    if (responseCode < HttpURLConnection.HTTP_MULT_CHOICE && !contains(crawlingDomains, url) && !CrawlerUtils.isOpenDNSResponse(connection)) {
                        // Si hay redirecciones, puede que el dominio cambie
                        domain = connection.getURL().getHost();

                        InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
                        String textContent = CrawlerUtils.getTextContent(connection, markableInputStream);

                        Document document = CrawlerDOMUtils.getDocument(textContent);

                        String metaRedirect = CrawlerDOMUtils.getMetaRedirect(url, document);
                        if (StringUtils.isEmpty(metaRedirect)) {
                            String textContentHash = CrawlerUtils.getHash(textContent);
                            if (!md5Content.contains(textContentHash)) {
                                CrawledLink crawledLink = new CrawledLink(url, textContent, counter, numRedirections);
                                crawlingDomains.add(crawledLink);
                                md5Content.add(textContentHash);
                                LOG.info("Introducida la URL número " + crawlingDomains.size() + ": " + url);
                                if (crawlerData.getProfundidad() > 1 || crawlerData.getTopN() != 1) {
                                    // Si se trata de un observatorio, o la petición viene del servicio básico
                                    if (crawlerData.getIdObservatory() != 0 || crawlerData.getIdCrawling() < 0) {
                                        //Cogemos lista de idiomas para no coger enlaces de cambio de idioma
                                        ignoredLinks = Utils.getIgnoredLinks();
                                    }
                                    makeCrawl(domain, url, url, cookie, crawlerData, ignoredLinks);
                                }
                            } else {
                                LOG.info("La url " + url + " ha sido rechazada por estar incluida en el rastreo");
                                rejectedDomains.add(url);
                            }
                        } else {
                            numRedirections++;
                            connection = CrawlerUtils.followRedirection(connection, cookie, new URL(url), metaRedirect);
                            responseCode = Integer.MAX_VALUE;
                        }

                    } else if (responseCode >= HttpURLConnection.HTTP_MULT_CHOICE && responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                        numRedirections++;
                        connection = CrawlerUtils.followRedirection(connection, cookie, new URL(url), connection.getHeaderField("location"));
                    } else {
                        if (CrawlerUtils.isOpenDNSResponse(connection)) {
                            LOG.info("La URL solicitada ha provocado la respuesta del OpenDNS");
                            if ((counter < maxNumRetries - 1) && (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST)) {
                                Thread.sleep(timeRetry);
                            }
                            counter++;
                        } else if (contains(crawlingDomains, url)) {
                            LOG.info("La url " + url + " ha sido rechazada por estar incluida en el rastreo");
                            rejectedDomains.add(url);
                        } else {
                            LOG.info("No se ha podido acceder a la raiz del rastreo configurado " + url + " ya que ha respondido con el código " + responseCode);
                            if (counter < maxNumRetries - 1) {
                                Thread.sleep(timeRetry);
                            }
                            counter++;
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
                        LOG.info("Error al intentar introducir la url auxiliar " + auxDomain + ": " + e.getMessage());
                    }
                }

                Logger.putLog("Terminado el rastreo para " + url + ", se han recogido " + crawlingDomains.size() + " enlaces: ", CrawlerJob.class, Logger.LOG_LEVEL_INFO);
                for (CrawledLink crawledLink : crawlingDomains) {
                    Logger.putLog(crawledLink.getUrl(), CrawlerJob.class, Logger.LOG_LEVEL_INFO);
                }

            } catch (Exception e) {
                LOG.info("Error al rastrear el dominio " + url + ": " + e.getMessage());
            }
        }

        // Avisa si se han rastreado menos páginas de las debidas
        if (!crawlerData.isTest()) {
            if ((crawlerData.getUrls().size() != 1) || (crawlerData.getTopN() != 1 && chosenDepth != 1)) {
                if ((crawlingDomains.size() < crawlerData.getUrls().size()) || (crawlingDomains.size() < (crawlerData.getTopN() * chosenDepth + 1))) {
                    List<String> mailTo = Arrays.asList(pmgr.getValue("crawler.core.properties", "incomplete.crawler.warning.emails").split(";"));
                    String text = "El rastreo para " + crawlerData.getUrls().get(0) + " ha devuelto solo " + crawlingDomains.size() + " resultados";
                    MailUtils.sendMail("rastreador@inteco.es", "Rastreador Web de Inteco", mailTo, "Rastreo inacabado", text, null, null, null, null, true);
                }
            }
        }

        // Llamamos al analizador
        analyze(crawlingDomains, crawlerData, cookie);
    }

    private void analyze(List<CrawledLink> crawlingDomains, CrawlerData crawlerData, String cookie) throws Exception {
        WebAnalayzer webAnalayzer = new WebAnalayzer();

        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue("crawler.core.properties", "crawler.date.format"));

        int cont = 0;
        for (CrawledLink crawledLink : crawlingDomains) {
            if (!interrupt && (crawlerData.getCartuchos() != null && crawlerData.getCartuchos().length > 0)) {
                if (cont < crawlingDomains.size() - 1) {
                    webAnalayzer.runCartuchos(crawledLink, df.format(new Date()), crawlerData, cookie, false);
                } else {
                    webAnalayzer.runCartuchos(crawledLink, df.format(new Date()), crawlerData, cookie, true);
                }
            } else {
                // Si se pide interrupción, se abandonan los análisis
                break;
            }
            cont++;
        }
    }

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

    private void makeCrawl(String domain, String rootUrl, String url, String cookie, CrawlerData crawlerData, List<IgnoredLink> ignoredLinks) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        int unlimitedTopN = Integer.parseInt(pmgr.getValue("crawler.core.properties", "amplitud.ilimitada.value"));
        if (crawlerData.getProfundidad() > 0 && !interrupt) {
            List<CrawledLink> levelLinks = new ArrayList<CrawledLink>();
            HttpURLConnection connection = CrawlerUtils.getConnection(url, domain, true);
            try {
                connection.setRequestProperty("Cookie", cookie);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
                    String textContent = CrawlerUtils.getTextContent(connection, markableInputStream);
                    String textContentHash = CrawlerUtils.getHash(textContent);

                    if (!md5Content.contains(textContentHash)) {
                        md5Content.add(textContentHash);
                    }

                    connection.disconnect();

                    Document document = CrawlerDOMUtils.getDocument(textContent);
                    List<String> urlLinks = CrawlerDOMUtils.getDomLinks(document, ignoredLinks);

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
                            LOG.info("La URL " + urlLink + " del enlace encontrado ha dado problemas de conexión: " + e.getMessage());
                        }
                    }

                    crawlerData.setProfundidad(crawlerData.getProfundidad() - 1);
                    for (CrawledLink levelLink : levelLinks) {
                        makeCrawl(domain, rootUrl, levelLink.getUrl(), cookie, crawlerData, ignoredLinks);
                    }
                }
            } catch (Exception e) {
                LOG.info("La url " + url + " ha dado problemas de conexión: " + e.getMessage());
            }
        }
    }

    private boolean isLinkToAdd(String rootUrl, String domain, String urlLink, String cookie, List<CrawledLink> levelLinks, CrawlerData crawlerData, boolean addAuxiliaryLinks, List<IgnoredLink> ignoredLinks) throws Exception {
        return isHtmlTextContent(domain, urlLink, cookie) && hasAccessToUrl(rootUrl, domain, urlLink, cookie, levelLinks, crawlerData, addAuxiliaryLinks, ignoredLinks);
    }

    /**
     * El siguiente método comprueba que la URL pedida sea de tipo text/html.
     *
     * @param urlLink la URL de la página a comprobar
     * @return true si la cabecera content-type es text/html y false en caso contrario
     * @throws Exception
     */
    private boolean isHtmlTextContent(String domain, String urlLink, String cookie) throws Exception {
        HttpURLConnection connection = CrawlerUtils.getConnection(urlLink, domain, true);
        connection.setRequestProperty("Cookie", cookie);
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            if (connection.getHeaderField("content-type") != null &&
                    connection.getHeaderField("content-type").contains("text/html")) {
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

    private boolean hasAccessToUrl(String rootUrl, String domain, String urlLink, String cookie, List<CrawledLink> levelLinks, CrawlerData crawlerData, boolean addAuxiliaryLinks, List<IgnoredLink> ignoredLinks) throws Exception {
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
                connection = CrawlerUtils.followRedirection(connection, cookie, new URL(urlLink), connection.getHeaderField("location"));
            } else if (responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
                String remoteContent = CrawlerUtils.getTextContent(connection, markableInputStream);
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
                        connection = CrawlerUtils.followRedirection(connection, cookie, new URL(urlLink), metaRedirect);
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

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        LOG.info("Se ha pedido una interrupción!!");
        interrupt = true;
    }

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

    private static boolean contains(List<CrawledLink> crawledLinks, String url) {
        for (CrawledLink crawledLink : crawledLinks) {
            if (crawledLink.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInTheSameDirectory(String link, String urlRoot) {
        final String protocolRegExp = "https?://";
        final String urlRootDirectory = urlRoot.replaceAll(protocolRegExp, "").lastIndexOf("/") != -1 ?
                urlRoot.replaceAll(protocolRegExp, "").substring(0, urlRoot.replaceAll(protocolRegExp, "").lastIndexOf("/")) :
                urlRoot.replaceAll(protocolRegExp, "");

        final String linkDirectory = link.replaceAll(protocolRegExp, "").lastIndexOf("/") != -1 ?
                link.replaceAll(protocolRegExp, "").substring(0, link.replaceAll(protocolRegExp, "").lastIndexOf("/")) :
                link.replaceAll(protocolRegExp, "");

        return linkDirectory.contains(urlRootDirectory);
    }

}
