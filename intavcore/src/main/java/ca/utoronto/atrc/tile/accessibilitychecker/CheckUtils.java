package ca.utoronto.atrc.tile.accessibilitychecker;

import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.cyberneko.html.HTMLConfiguration;
import es.inteco.intav.form.CheckedLinks;
import es.inteco.intav.utils.EvaluatorUtils;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CheckUtils {

    private CheckUtils() {
    }

    public static List<Element> getSectionLink(NodeList links, String sectionRegExp) {
        List<Element> linksFound = new ArrayList<Element>();
        for (int i = 0; i < links.getLength(); i++) {
            Element link = (Element) links.item(i);
            if (link.hasAttribute("href")) {
                if (StringUtils.isNotEmpty(link.getTextContent())) {
                    if (StringUtils.textMatchs(link.getTextContent().trim(), sectionRegExp)) {
                        linksFound.add(link);
                    }
                }

                if (link.hasAttribute("title")) {
                    if (StringUtils.textMatchs(link.getAttribute("title").trim(), sectionRegExp)) {
                        linksFound.add(link);
                    }
                }

                NodeList imgs = link.getElementsByTagName("img");
                for (int j = 0; j < imgs.getLength(); j++) {
                    Element img = (Element) imgs.item(j);
                    if (img.hasAttribute("alt")) {
                        if (StringUtils.textMatchs(img.getAttribute("alt").trim(), sectionRegExp)) {
                            linksFound.add(link);
                        }
                    }
                }
            }
        }
        return linksFound;
    }

    public static boolean hasContact(Document document, String contactRegExp, String emailRegExp) throws Exception {
        // Texto de correo electrónico en el texto normal
        Pattern pattern = Pattern.compile(emailRegExp, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(getDocumentText(document));
        if (matcher.find()) {
            // Hemos encontrado una dirección de correo electrónico en la página
            return true;
        }

        // Enlaces a la sección de contacto
        List<String> contactTexts = Arrays.asList(contactRegExp.split("\\|"));
        NodeList links = document.getElementsByTagName("a");

        for (int i = 0; i < links.getLength(); i++) {
            Element link = (Element) links.item(i);
            String linkText = link.getTextContent().toLowerCase().trim();
            String linkTitle = link.getAttribute("title").toLowerCase().trim();
            for (String contactText : contactTexts) {
                if (linkText.toUpperCase().contains(contactText.toUpperCase()) || linkTitle.toUpperCase().contains(contactText.toUpperCase())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean hasRevisionDate(Document document, String dateRegExp) throws Exception {
        // Texto de correo electrónico en el texto normal
        Pattern pattern = Pattern.compile(dateRegExp, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(getDocumentText(document));

        // Hemos encontrado una dirección de correo electrónico en la página
        return matcher.find();
    }

    public static String getDocumentText(final Document document) {
        final List<Node> nodeList = EvaluatorUtils.generateNodeList(EvaluatorUtils.getHtmlElement(document), new ArrayList<Node>(), IntavConstants.ALL_ELEMENTS);
        final StringBuilder documentText = new StringBuilder();
        for (Node node : nodeList) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                documentText.append(node.getTextContent().trim());
                documentText.append(" ");
            }
        }
        return documentText.toString();
    }

    public static Document getRemoteDocument(String documentUrlStr, String remoteUrlStr) throws Exception {
        HttpURLConnection connection = EvaluatorUtils.getConnection(remoteUrlStr, "GET", true);
        connection.setRequestProperty("referer", documentUrlStr);
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            DOMParser parser = new DOMParser(new HTMLConfiguration());
            parser.parse(new InputSource(connection.getInputStream()));
            return parser.getDocument();
        } else {
            return null;
        }
    }

    public static String getElementText(Node checkedNode, boolean backward, List<String> inlineTags) {
        String text = "";

        while (checkedNode != null && (StringUtils.isEmpty(text) || StringUtils.isOnlyBlanks(text))) {
            if (backward) {
                checkedNode = checkedNode.getPreviousSibling();
            } else {
                checkedNode = checkedNode.getNextSibling();
            }

            if (checkedNode != null && checkedNode.getTextContent() != null && (checkedNode.getNodeType() == Node.TEXT_NODE ||
                    (checkedNode.getNodeType() == Node.ELEMENT_NODE && inlineTags.contains(checkedNode.getNodeName().toUpperCase())))) {
                text = checkedNode.getTextContent().trim();
            }
        }

        return text;
    }

    /**
     * Se mira que el anterior o el siguiente nodo al elemento verificado cumpla la expresión regular para
     * ser un posible candidato a elemento falso de lista. Hay que tener en cuenta el caso siguiente:
     * "<br/> <strong>*uno</strong>": con espacios en blanco y una etiqueta en línea se añadiría un nodo
     * de tipo texto vacío que habría que ignorar.
     *
     * @param checkedElement
     * @param inlineTags
     * @param pattern
     * @param backward
     * @return
     */
    public static boolean isFalseBrNode(Element checkedElement, List<String> inlineTags, Pattern pattern, boolean backward) {
        String text = getElementText(checkedElement, backward, inlineTags);

        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static String getBaseUrl(Element documentElement) {
        NodeList bases = documentElement.getElementsByTagName("base");

        for (int i = bases.getLength() - 1; i >= 0; i--) {
            Element base = (Element) bases.item(i);
            if (base.hasAttribute("href") && StringUtils.isNotEmpty(base.getAttribute("href"))) {
                return base.getAttribute("href");
            }
        }

        return null;
    }

    public static boolean isValidUrl(Element elementRoot, Node nodeNode) {
        URL remoteUrl = null;
        URL documentUrl = null;
        CheckedLinks checkedLinks = null;
        try {
            if (!isAbsolute(nodeNode.getTextContent().trim())) {
                documentUrl = CheckUtils.getBaseUrl(elementRoot) != null ? new URL(CheckUtils.getBaseUrl(elementRoot)) : new URL((String) elementRoot.getUserData("url"));
                remoteUrl = new URL(documentUrl, encodeUrl(nodeNode.getTextContent().trim()));
            } else {
                remoteUrl = new URL(encodeUrl(nodeNode.getTextContent().trim()));
            }

            PropertiesManager pmgr = new PropertiesManager();
            List<String> allowedPorts = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "broken.links.allowed.ports").split(";"));

            if (allowedPorts.contains(String.valueOf(remoteUrl.getPort()))) {
                checkedLinks = (CheckedLinks) elementRoot.getUserData("checkedLinks");
                if (checkedLinks != null && checkedLinks.getCheckedLinks().contains(remoteUrl.toString())) {
                    // Los enlaces ya verificados los damos por buenos, no puntúan mal varias veces
                    return true;
                } else if (checkedLinks == null ||
                        (!checkedLinks.getBrokenLinks().contains(remoteUrl.toString()) && !checkedLinks.getAvailablelinks().contains(remoteUrl.toString()))) {
                    Logger.putLog("Verificando que existe la URL " + nodeNode.getTextContent() + " --> " + remoteUrl.toString(), Check.class, Logger.LOG_LEVEL_INFO);
                    HttpURLConnection connection = EvaluatorUtils.getConnection(remoteUrl.toString(), "GET", true);
                    if (documentUrl != null) {
                        connection.setRequestProperty("referer", documentUrl.toString());
                    }
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        checkedLinks.getBrokenLinks().add(remoteUrl.toString());
                        Logger.putLog("Encontrado enlace roto: " + nodeNode.getTextContent() + " --> " + remoteUrl.toString(), Check.class, Logger.LOG_LEVEL_INFO);
                        return false;
                    } else {
                        if (checkedLinks != null) {
                            checkedLinks.getAvailablelinks().add(remoteUrl.toString());
                        }
                        return true;
                    }
                } else {
                    if (checkedLinks.getBrokenLinks().contains(remoteUrl.toString())) {
                        Logger.putLog("Encontrado enlace roto: " + nodeNode.getTextContent() + " --> " + remoteUrl.toString(), Check.class, Logger.LOG_LEVEL_INFO);
                        return false;
                    } else if (checkedLinks.getAvailablelinks().contains(remoteUrl.toString())) {
                        return true;
                    } else {
                        return true;
                    }
                }
            }
        } catch (UnknownHostException e) {
            // Si no se puede conectar porque no se reconoce el Host la url no es válida
            return false;
        } catch (Exception e) {
            Logger.putLog("Error al verificar si el elemento " + remoteUrl + " está roto:" + e.getMessage(), CheckUtils.class, Logger.LOG_LEVEL_WARNING);
        } finally {
            if (checkedLinks != null && remoteUrl != null) {
                checkedLinks.getCheckedLinks().add(remoteUrl.toString());
            }
        }
        return true;
    }

    private static boolean isAbsolute(String url) {
        return url.startsWith("http");
    }

    private static String encodeUrl(String path) throws Exception {
        path = path.replaceAll("[ \\+]", "%20");
        String[] pathArray = path.split("[:\\./?&=#(%20)]");
        for (String aPathArray : pathArray) {
            path = path.replaceAll(aPathArray, URLEncoder.encode(aPathArray, "UTF-8"));
        }

        return path;
    }

    public static boolean hasContent(Node node) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> elements = Arrays.asList(pmgr.getValue("intav.properties", "content.tags").split(";"));
        if (node.getNodeType() == Node.TEXT_NODE && StringUtils.isNotEmpty(node.getTextContent()) && !StringUtils.isOnlyBlanks(node.getTextContent())) {
            return true;
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (elements.contains(node.getNodeName().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPreviousHeader(String str1, String str2) {
        try {
            if (!str2.substring(0, 1).equalsIgnoreCase("h")) {
                return false;
            } else if (Long.valueOf(str2.substring(1, str2.length())) != null && Long.valueOf(str2.substring(1, str2.length())) > Long.valueOf(str1.substring(1, str1.length()))) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static int isEmptyDescendentContent(Node nextSimbling, Element elementGiven) {
        PropertiesManager pmgr = new PropertiesManager();
        List<String> noContentTags = Arrays.asList(pmgr.getValue("intav.properties", "ignored.tags").split(";"));
        while (nextSimbling != null) {
            if (!nextSimbling.getNodeName().equalsIgnoreCase(elementGiven.getNodeName()) && !isPreviousHeader(nextSimbling.getNodeName(), elementGiven.getNodeName())) {
                //Si tiene contenido devuelves false
                if (nextSimbling.getNodeType() == Node.TEXT_NODE) {
                    if (StringUtils.isNotEmpty(nextSimbling.getTextContent()) && !StringUtils.isOnlyBlanks(nextSimbling.getTextContent())) {
                        return IntavConstants.IS_NOT_EMPTY;
                    }
                } else if (!noContentTags.contains(nextSimbling.getNodeName().toUpperCase())) {
                    if (CheckUtils.hasContent(nextSimbling)) {
                        return IntavConstants.IS_NOT_EMPTY;
                    } else {
                        int hasChildContent = CheckUtils.hasChildContent(nextSimbling, elementGiven);
                        if (hasChildContent == IntavConstants.HAS_CONTENT) {
                            return IntavConstants.IS_NOT_EMPTY;
                        } else if (hasChildContent == IntavConstants.EQUAL_HEADER_TAG) {
                            return IntavConstants.EQUAL_HEADER_TAG;
                        }
                    }
                }
            } else {
                return IntavConstants.EQUAL_HEADER_TAG;
            }
            nextSimbling = nextSimbling.getNextSibling();
        }
        return IntavConstants.IS_EMPTY;
    }

    public static int hasChildContent(Node node, Element elementGiven) {
        NodeList nodeList = node.getChildNodes();
        PropertiesManager pmgr = new PropertiesManager();
        List<String> elements = Arrays.asList(pmgr.getValue("intav.properties", "ignored.tags").split(";"));
        if (node.getNodeName().equalsIgnoreCase(elementGiven.getNodeName()) && isPreviousHeader(node.getNodeName(), elementGiven.getNodeName())) {
            return IntavConstants.EQUAL_HEADER_TAG;
        }
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equalsIgnoreCase(elementGiven.getNodeName()) || isPreviousHeader(node.getNodeName(), elementGiven.getNodeName())) {
                return IntavConstants.EQUAL_HEADER_TAG;
            } else if (!elements.contains(nodeList.item(i).getNodeName().toUpperCase())) {
                if (CheckUtils.hasContent(nodeList.item(i))) {
                    return IntavConstants.HAS_CONTENT;
                } else {
                    int hasContentChild = hasChildContent(nodeList.item(i), elementGiven);
                    if (hasContentChild == IntavConstants.HAS_CONTENT) {
                        return IntavConstants.HAS_CONTENT;
                    } else if (hasContentChild == IntavConstants.EQUAL_HEADER_TAG) {
                        return IntavConstants.EQUAL_HEADER_TAG;
                    }
                }
            }
        }
        return IntavConstants.HAS_NOT_CONTENT;
    }

    public static ImageReader getImageReader(Element img, URL url) throws Exception {
        if (img.getUserData(IntavConstants.GIF_VERIFICATED) == null && img.getUserData(IntavConstants.GIF_READER) == null) {
            img.setUserData(IntavConstants.GIF_VERIFICATED, Boolean.TRUE, null);

            Logger.putLog("Descargando la imagen " + url + " para analizar su contenido", CheckUtils.class, Logger.LOG_LEVEL_INFO);
            HttpURLConnection connection = EvaluatorUtils.getConnection(url.toString(), "GET", true);
            PropertiesManager pmgr = new PropertiesManager();
            connection.setConnectTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "gif.connection.timeout")));
            connection.setReadTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "gif.connection.timeout")));
            ImageInputStream imageInputStream = new MemoryCacheImageInputStream(connection.getInputStream());

            java.util.Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(imageInputStream);
                img.setUserData(IntavConstants.GIF_READER, reader, null);
                return reader;
            } else {
                return null;
            }
        } else {
            return (ImageReader) img.getUserData(IntavConstants.GIF_READER);
        }
    }

    /**
     * Método para determinar si un enlace pertenece al mismo dominio o no que la página.
     * Se realiza una comprobación básica sobre la sintaxis de la url. No se comprueba si mediante redirección se sale o no del dominio
     *
     * @param url  la url de la página
     * @param link atributo href no vacío de un enlace (etiqueta a)
     * @return true si el enlace link pertenece al mismo dominio que el enlace url false en caso contrario
     */
    public static boolean checkLinkInDomain(final String url, final String link) throws MalformedURLException {
        String domain = new URL(url).getHost();
        int index = domain.lastIndexOf('.');
        if (index != -1) {
            // Comprobamos si la url tiene dominios superiores a nivel 2
            // (ej http://www.algo.es o sólo http://algo.es)
            if ((index = domain.lastIndexOf('.', index - 1)) != -1) {
                domain = domain.substring(index);
            } else {
                // Si la url comenzó directamente con el dominio de 2º nivel
                // http://algo.es
                // le anteponemos el caracter '.' para indicar comienzo de
                // dominio
                domain = "." + domain;
            }
        }

        String newHost = new URL(link).getHost();
        index = newHost.lastIndexOf('.');
        if (index != -1) {
            if ((index = newHost.lastIndexOf('.', index - 1)) != -1) {
                newHost = newHost.substring(index);
            } else {
                newHost = "." + newHost;
            }
        }
        return newHost.equalsIgnoreCase(domain);
    }
}
