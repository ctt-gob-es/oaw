package es.inteco.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckerParser;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.ignored.links.IgnoredLink;
import es.inteco.cyberneko.html.HTMLConfiguration;
import es.inteco.common.utils.StringUtils;
import org.w3c.dom.*;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerDOMUtils {

    public static List<Element> getElementsByTagName(Document document, String tag) {
        List<Element> elements = new ArrayList<Element>();

        NodeList allElements = document.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    public static List<Element> getElementsByTagName(Element element, String tag) {
        List<Element> elements = new ArrayList<Element>();

        NodeList allElements = element.getElementsByTagName("*");
        for (int i = 0; i < allElements.getLength(); i++) {
            Element tagElement = (Element) allElements.item(i);
            if (tagElement.getNodeName().equalsIgnoreCase(tag)) {
                elements.add(tagElement);
            }
        }

        return elements;
    }

    public static boolean hasAttribute(Element element, String attributeName) {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            if (attribute.getNodeName().equalsIgnoreCase(attributeName)) {
                return true;
            }
        }
        return false;
    }

    public static String getAttribute(Element element, String attributeName) {
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            if (attribute.getNodeName().equalsIgnoreCase(attributeName)) {
                return attribute.getTextContent();
            }
        }
        return null;
    }

    public static Document getDocument(String textContent) throws Exception {
        CheckerParser parser = new CheckerParser(new HTMLConfiguration());

        try {
            parser.parse(new InputSource(new StringReader(textContent)));
        } catch (Exception e) {
            parser = new CheckerParser(new HTMLConfiguration(true));
            parser.parse(new InputSource(new StringReader(textContent)));
        }

        return parser.getDocument();
    }

    private static String getFrameContent(String url) throws Exception {
        HttpURLConnection connection = CrawlerUtils.getConnection(url, null, false);
        InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
        String textContent = StringUtils.getContentAsString(markableInputStream, CrawlerUtils.getCharset(connection, markableInputStream));

        textContent = CrawlerUtils.removeHtmlComments(textContent);

        textContent = getOnlyBody(textContent);

        return textContent;
    }

    public static String getFramesSource(String rootUrl, String textContent) throws Exception {
        String framesSource = "";
        Document document = getDocument(textContent);
        List<Element> frames = getElementsByTagName(document, "frame");

        for (Element frame : frames) {
            try {
                if (hasAttribute(frame, "src") && StringUtils.isNotEmpty(getAttribute(frame, "src"))) {
                    String frameUrl = new URL(new URL(rootUrl), getAttribute(frame, "src")).toString();
                    String frameSource = "<!-- Código HTML del frame localizado en " + frameUrl + " -->\n\n" +
                            getFrameContent(frameUrl) +
                            "<!-- Fin del Código HTML del frame localizado en " + frameUrl + " -->\n\n";

                    frameSource = createAbsoluteHrefs(frameSource, frameUrl);

                    framesSource += frameSource;
                }
            } catch (Exception e) {
                Logger.putLog("Error al recuperar el contenido del FRAME: " + e.getMessage(), CrawlerUtils.class, Logger.LOG_LEVEL_INFO);
            }
        }

        return framesSource;
    }

    public static String appendFramesSource(String textContent, String framesSource) throws Exception {
        Document document = getDocument(textContent);

        List<Element> noframes = getElementsByTagName(document, "noframes");
        if (noframes.size() == 0) {
            // Si no tiene NOFRAMES, lo creamos y lo añadimos al final del documento
            framesSource = "<NOFRAMES>\n<BODY>\n" + framesSource + "</BODY>\n</NOFRAMES>\n";
            Document frameDocument = getDocument(framesSource);
            NodeList childNodes = document.getChildNodes();
            for (int i = childNodes.getLength() - 1; i > 0; i--) {
                if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    addFrameDocument((Element) childNodes.item(i), frameDocument);
                    break;
                }
            }
        } else {
            Element noframe = noframes.get(0);
            NodeList bodies = noframe.getElementsByTagName("BODY");
            if (bodies.getLength() == 0) {
                // Si tiene NOFRAMES sin BODY, lo creamos y lo metemos dentro
                framesSource = "<BODY>\n" + serializeNodeList(noframe.getChildNodes()) + framesSource + "</BODY>\n";

                Node child = noframe.getFirstChild();
                while (child != null) {
                    Node childToRemove = child;
                    child = child.getNextSibling();
                    noframe.removeChild(childToRemove);
                }

                Document frameDocument = getDocument(framesSource);
                addFrameDocument(noframe, frameDocument);
            } else {
                // Si tiene NOFRAMES con BODY, lo metemos dentro
                Element body = (Element) bodies.item(0);
                Document frameDocument = getDocument(framesSource);
                addFrameDocument(body, frameDocument);
            }
        }

        return serializeDocument(document);
    }

    private static void addFrameDocument(Element element, Document frameDocument) {
        NodeList frameChildren = frameDocument.getChildNodes();

        for (int i = 0; i < frameChildren.getLength(); i++) {
            if (frameChildren.item(i).getNodeType() == Node.ELEMENT_NODE || frameChildren.item(i).getNodeType() == Node.TEXT_NODE) {
                element.appendChild(element.getOwnerDocument().importNode(frameChildren.item(i), true));
            }
        }
    }

    private static void addFrameDocumentSibling(Element parentElement, Element element, Document frameDocument) {
        NodeList frameChildren = frameDocument.getChildNodes();

        for (int i = 0; i < frameChildren.getLength(); i++) {
            if (frameChildren.item(i).getNodeType() == Node.ELEMENT_NODE || frameChildren.item(i).getNodeType() == Node.TEXT_NODE) {
                parentElement.insertBefore(element.getOwnerDocument().importNode(frameChildren.item(i), true), element);
            }
        }
    }

    public static String appendIframesSource(String rootUrl, String textContent) throws Exception {
        Document document = getDocument(textContent);
        List<Element> iframes = getElementsByTagName(document, "iframe");
        if (iframes.size() > 0) {
            for (Element iframe : iframes) {
                try {
                    if (hasAttribute(iframe, "src") && StringUtils.isNotEmpty(getAttribute(iframe, "src"))) {
                        if (!getAttribute(iframe, "src").equals("#")) {
                            String frameUrl = new URL(new URL(rootUrl), getAttribute(iframe, "src")).toString();
                            String frameSource = "<!-- Código HTML del iframe localizado en " + frameUrl + " -->\n\n" +
                                    getFrameContent(frameUrl) +
                                    "<!-- Fin del Código HTML del iframe localizado en " + frameUrl + " -->\n\n";

                            frameSource = createAbsoluteHrefs(frameSource, frameUrl);

                            Document frameDocument = getDocument(frameSource);

                            addFrameDocumentSibling((Element) iframe.getParentNode(), iframe, frameDocument);
                        }
                    }
                } catch (Exception e) {
                    Logger.putLog("Error al recuperar el contenido del IFRAME: " + e.getMessage(), CrawlerUtils.class, Logger.LOG_LEVEL_INFO);
                }
            }

            return serializeDocument(document);
        } else {
            return textContent;
        }


    }

    public static String createAbsoluteHrefs(String textContent, String frameUrl) throws Exception {
        Document frameDocument = getDocument(textContent);
        List<Element> links = getElementsByTagName(frameDocument, "a");
        for (Element link : links) {
            if (hasAttribute(link, "href")) {
                try {
                    link.setAttribute("href", new URL(new URL(frameUrl), getAttribute(link, "href")).toString());
                } catch (Exception e) {
                    Logger.putLog("Error al crear la URL absoluta de " + getAttribute(link, "href") + " y " + frameUrl, CrawlerUtils.class, Logger.LOG_LEVEL_WARNING);
                }
            }
        }
        PropertiesManager pmgr = new PropertiesManager();
        int maxNumElements = Integer.parseInt(pmgr.getValue("crawler.core.properties", "max.num.descendants.to.serialize"));
        if (frameDocument.getElementsByTagName("*").getLength() < maxNumElements) {
            return serializeDocument(frameDocument);
        } else {
            return null;
        }
    }

    private static String serializeDocument(Document document) {
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

    private static String serializeNodeList(NodeList nodeList) {
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

    private static String getOnlyBody(String textContent) {
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

    public static List<String> getDomLinks(Document document, List<IgnoredLink> ignoredLinks) {
        List<String> results = new ArrayList<String>();
        List<Element> links = getElementsByTagName(document, "a");
        links.addAll(getElementsByTagName(document, "area"));

        for (Element link : links) {
            if (hasAttribute(link, "href") && StringUtils.isNotEmpty(getAttribute(link, "href"))
                    && !CrawlerUtils.isSwitchLanguageLink(link, ignoredLinks)) {
                if (!getAttribute(link, "href").contains("#") && !getAttribute(link, "href").contains("mailto:")) {
                    if (!results.contains(getAttribute(link, "href").replaceAll("\n", ""))) {
                        results.add(getAttribute(link, "href").replaceAll("\n", ""));
                    }
                }
            }
        }
        return results;
    }

    public static String getBaseUrl(Document document) {
        List<Element> bases = getElementsByTagName(document, "base");

        for (Element base : bases) {
            if (base.hasAttribute("href") && StringUtils.isNotEmpty(base.getAttribute("href"))) {
                return base.getAttribute("href");
            }
        }
        return null;
    }

}
