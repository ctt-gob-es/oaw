import es.ctic.mail.MailService;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.ignored.links.IgnoredLink;
import es.inteco.crawler.ignored.links.Utils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.utils.CrawlerDOMUtils;
import es.inteco.utils.CrawlerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by mikunis on 26/11/14.
 */
public class TestCrawl {

    public static final Log LOG = LogFactory.getLog("root");

    private final List<CrawledLink> crawlingDomains = new ArrayList<CrawledLink>();
    private final List<String> auxDomains = new ArrayList<String>();
    private final List<String> md5Content = new ArrayList<String>();
    private final List<String> rejectedDomains = new ArrayList<String>();
    private final MailService mailService = new MailService();

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

    @Test
    public void test() throws Exception {
        final CrawlerData crawlerData = new CrawlerData();
//        crawlerData.setUrls(Collections.singletonList("http://www.mjusticia.gob.es/cs/Satellite/Portal/es/inicio"));
//        crawlerData.setUrls(Collections.singletonList("http://www.aytoalamedasagra.com/"));
//        crawlerData.setUrls(Collections.singletonList("http://www.ayuntamientodeoyon.com/es/"));
//        crawlerData.setProfundidad(4);
//        crawlerData.setTopN(4);
//        crawlerData.setPseudoaleatorio(true);
//        crawlerData.setTest(true);
//        crawlerData.setIdCrawling(-1);
//        makeCrawl(crawlerData);
//
//        for (CrawledLink cl: crawlingDomains) {
//            System.out.println(cl.getUrl());
//        }
//        Assert.assertEquals(17, crawlingDomains.size());
    }

    private void makeCrawl(CrawlerData crawlerData) throws Exception {
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
                            connection = CrawlerUtils.followRedirection(cookie, new URL(url), metaRedirect);
                            responseCode = Integer.MAX_VALUE;
                        }

                    } else if (responseCode >= HttpURLConnection.HTTP_MULT_CHOICE && responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                        numRedirections++;
                        connection = CrawlerUtils.followRedirection(cookie, new URL(url), connection.getHeaderField("location"));
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
                        if ((crawlingDomains.size() <= (chosenDepth * crawlerData.getTopN()))) {
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
                    final List<String> mailTo = Collections.<String>emptyList();//Arrays.asList(pmgr.getValue("crawler.core.properties", "incomplete.crawler.warning.emails").split(";"));
                    final String text = "El rastreo para " + crawlerData.getUrls().get(0) + " ha devuelto solo " + crawlingDomains.size() + " resultados";

                    mailService.sendMail(mailTo, "Rastreo inacabado", text);
                }
            }
        }
    }

    private boolean contains(List<CrawledLink> crawledLinks, String url) {
        for (CrawledLink crawledLink : crawledLinks) {
            if (crawledLink.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLinkToAdd(String rootUrl, String domain, String urlLink, String cookie, List<CrawledLink> levelLinks, CrawlerData crawlerData, boolean addAuxiliaryLinks, List<IgnoredLink> ignoredLinks) throws Exception {
        return isHtmlTextContent(domain, urlLink, cookie) && hasAccessToUrl(rootUrl, domain, urlLink, cookie, levelLinks, crawlerData, addAuxiliaryLinks, ignoredLinks);
    }

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
                connection = CrawlerUtils.followRedirection(cookie, new URL(urlLink), connection.getHeaderField("location"));
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

    private void makeCrawl(String domain, String rootUrl, String url, String cookie, CrawlerData crawlerData, List<IgnoredLink> ignoredLinks) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        int unlimitedTopN = Integer.parseInt(pmgr.getValue("crawler.core.properties", "amplitud.ilimitada.value"));
        if (crawlerData.getProfundidad() > 0) {
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
                    // Si se utiliza iframe como etiqueta simple (sin cuerpo) se produce problema al parsear, las eliminamos sin más
                    textContent = textContent.replaceAll("<iframe [^>]*/>", "");
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
}
