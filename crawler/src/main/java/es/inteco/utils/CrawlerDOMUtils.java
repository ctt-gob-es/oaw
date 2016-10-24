package es.inteco.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckerParser;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.ignored.links.IgnoredLink;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CrawlerDOMUtils {

    private CrawlerDOMUtils() {
    }

    public static List<Element> getElementsByTagName(Document document, String tag) {
        final List<Element> elements = new ArrayList<>();

        final NodeList allElements = document.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            final Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    public static List<Element> getElementsByTagName(final Element element, final String tag) {
        final List<Element> elements = new ArrayList<>();

        final NodeList allElements = element.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            final Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    public static boolean hasAttribute(final Element element, final String attributeName) {
        final NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            final Node attribute = attributes.item(i);
            if (attribute.getNodeName().equalsIgnoreCase(attributeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el valor de un atributo para un determinado elemento Element. La búsqueda se hace case insensitive. En caso
     * de existir múltiples atributos con el mismo nombre buscado no se garantiza cual se devuelve.
     *
     * @param element       el elemento Element
     * @param attributeName el nombre del atributo a buscar de forma case insensitive
     * @return el valor del atributo o un si no existe el atributo.
     */
    public static String getAttribute(final Element element, final String attributeName) {
        final NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            final Node attribute = attributes.item(i);
            if (attribute.getNodeName().equalsIgnoreCase(attributeName)) {
                return attribute.getTextContent();
            }
        }
        return null;
    }

    public static Document getDocument(final String textContent) throws Exception {
        CheckerParser parser = new CheckerParser();

        try {
            parser.parse(new InputSource(new StringReader(textContent)));
        } catch (Exception e) {
            parser = new CheckerParser(true);
            parser.parse(new InputSource(new StringReader(textContent)));
        }

        return parser.getDocument();
    }

    public static String serializeDocument(final Document document) {
        correctScriptTagSerialization(document);

        DOMImplementationLS domImplementationLS = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
        lsSerializer.getDomConfig().setParameter("xml-declaration", false);
        return (lsSerializer.writeToString(document));
    }

    private static void correctScriptTagSerialization(Document document) {
        List<Element> scripts = getElementsByTagName(document, "script");
        for (Element script : scripts) {
            if (script.getChildNodes().getLength() == 0 && StringUtils.isEmpty(script.getTextContent())) {
                script.appendChild(document.createTextNode(" "));
            }
        }
    }

    public static String serializeNodeList(NodeList nodeList) {
        final StringBuilder text = new StringBuilder();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                DOMImplementationLS domImplementationLS = (DOMImplementationLS) nodeList.item(i).getOwnerDocument().getImplementation();
                LSSerializer lsSerializer = domImplementationLS.createLSSerializer();
                lsSerializer.getDomConfig().setParameter("xml-declaration", false);
                lsSerializer.getDomConfig().setParameter("namespaces", false);
                text.append(lsSerializer.writeToString(nodeList.item(i)));
            } else if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
                text.append(nodeList.item(i).getTextContent());
            }
        }
        return text.toString();
    }

    public static String getOnlyBody(String textContent) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> regExps = Arrays.asList(pmgr.getValue("crawler.core.properties", "frame.source.reg.exp.matcher").split(";"));

        for (String regExp : regExps) {
            Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(textContent);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        if (!textContent.toLowerCase().contains("<html")) {
            return textContent;
        }

        return null;
    }

    public static String getMetaRedirect(String url, Document document) {
        List<Element> metas = getElementsByTagName(document, "meta");
        for (Element meta : metas) {
            if (hasAttribute(meta, "http-equiv") && getAttribute(meta, "http-equiv").equalsIgnoreCase("refresh") &&
                    hasAttribute(meta, "content") && getAttribute(meta, "content").contains("=")) {
                String redirection = getAttribute(meta, "content").substring(getAttribute(meta, "content").indexOf("=") + 1).trim();
                try {
                    // Probamos que se ha encontrado una dirección válida
                    URL remoteUrl = new URL(new URL(url), redirection);

                    if (!remoteUrl.toString().equals(url)) {
                        return redirection;
                    }
                } catch (Exception e) {
                    Logger.putLog("La redirección encontrada a " + redirection + " no es válida", CrawlerUtils.class, Logger.LOG_LEVEL_WARNING);
                }
            }
        }

        return null;
    }

    /**
     * Obtiene una lista de todos los enlaces (a y area) de tipo http (se ignoran, mailto:, javascript:, tel:,...)
     * de un documento DOM
     *
     * @param document    documento del que se extraerán los enlaces
     * @param ignoreLinks lista de enlaces que se ignorarán y no se incluirán en el resultado
     * @return una lista con todos los enlaces o la lista vacía si no existen
     */
    public static List<String> getDomLinks(final Document document, final List<IgnoredLink> ignoreLinks) {
        final List<String> results = new ArrayList<>();
        final List<Element> links = getElementsByTagName(document, "a");
        links.addAll(getElementsByTagName(document, "area"));

        for (Element link : links) {
            if (!CrawlerUtils.isSwitchLanguageLink(link, ignoreLinks)) {
                if (CrawlerUtils.isHTTPLink(link)) {
                    final String normalizedHref = getAttribute(link, "href").replaceAll("\n", "").toLowerCase().trim();
                    if (!results.contains(normalizedHref)) {
                        results.add(normalizedHref);
                    }
                }
            }
        }
        return results;
    }

    public static String getBaseUrl(final Document document) {
        final List<Element> bases = getElementsByTagName(document, "base");

        for (Element base : bases) {
            if (base.hasAttribute("href") && StringUtils.isNotEmpty(base.getAttribute("href"))) {
                return base.getAttribute("href");
            }
        }
        return null;
    }

}
