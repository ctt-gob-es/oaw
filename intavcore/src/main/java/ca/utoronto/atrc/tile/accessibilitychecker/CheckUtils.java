package ca.utoronto.atrc.tile.accessibilitychecker;

import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.CheckedLinks;
import es.inteco.intav.utils.EvaluatorUtils;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CheckUtils {

    private CheckUtils() {
    }

    /**
     * Comprueba si un nodo DOM Node es de tipo Element y con un determinado nombre
     *
     * @param node el nodo DOM a comprobar
     * @param name el nombre que debe coincidir si el nodo es de tipo Element
     * @return true si el nodo es Element y su coincide con el indicado o false en caso contrario
     */
    public static boolean isElementTagName(final Node node, final String name) {
        if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
            return node.getNodeName().equalsIgnoreCase(name);
        } else {
            return false;
        }
    }

    /**
     * Obtiene el primer nodo de tipo Element que es hijo de un determinado nodo Element
     *
     * @param element el nodo Element del que obtener el primer hijo de tipo Element
     * @return el nodo Element si se encuentra o null en caso contrario
     */
    public static Element getFirstChildElement(final Element element) {
        Node node = element.getFirstChild();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }

        if (node != null) {
            return (Element) node;
        } else {
            return null;
        }
    }

    /**
     * Obtiene el primer nodo de tipo Element que es hermano de un determinado nodo Element
     *
     * @param element el nodo Element del que obtener el primer hermano de tipo Element
     * @return el nodo Element si se encuentra o null en caso contrario
     */
    public static Element getFirstSiblingElement(final Element element) {
        Node node = element.getNextSibling();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }

        if (node != null) {
            return (Element) node;
        } else {
            return null;
        }
    }

    public static List<Element> getSectionLink(final NodeList links, final String sectionRegExp) {
        final Set<String> includedLinks = new HashSet<>();
        final List<Element> linksFound = new ArrayList<>();
        for (int i = 0; i < links.getLength(); i++) {
            final Element link = (Element) links.item(i);
            final String href = link.getAttribute("href").toLowerCase();
            if (link.hasAttribute("href") && !link.getAttribute("href").toLowerCase().startsWith("javascript") && !link.getAttribute("href").toLowerCase().startsWith("mailto")) {
                if (StringUtils.isNotEmpty(link.getTextContent())) {
                    if (StringUtils.textMatchs(link.getTextContent().trim(), sectionRegExp) && includedLinks.add(href)) {
                        linksFound.add(link);
                    }
                }

                if (link.hasAttribute("title")) {
                    if (StringUtils.textMatchs(link.getAttribute("title").trim(), sectionRegExp) && includedLinks.add(href)) {
                        linksFound.add(link);
                    }
                }

                final NodeList imgs = link.getElementsByTagName("img");
                for (int j = 0; j < imgs.getLength(); j++) {
                    final Element img = (Element) imgs.item(j);
                    if (img.hasAttribute("alt")) {
                        if (StringUtils.textMatchs(img.getAttribute("alt").trim(), sectionRegExp) && includedLinks.add(href)) {
                            linksFound.add(link);
                        }
                    }
                }
            }
        }

        return linksFound;
    }

    /**
     * Comprueba si un documento HTML tiene forma de contacto en la página de declaración de accesibilidad aplicando una serie de patrones sobre los enlaces y texto.
     *
     * @param document      documento HTML en formato DOM a analizar
     * @param contactRegExp expresión regular para detectar enlaces a sección de contacto (contactar,contacte,...)
     * @param emailRegExp   expresión regular para detectar si se incluye una dirección de correo directamente en el contenido (contacto@portal.es)
     * @return true si se ha detectado una forma de contacto, false en caso contrario
     */
    public static boolean hasContact(final Document document, final String contactRegExp, final String emailRegExp) {
        // Texto de correo electrónico en el texto normal
        final Pattern patternEmail = Pattern.compile(emailRegExp, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        final Matcher matcher = patternEmail.matcher(getDocumentText(document));
        if (matcher.find()) {
            // Hemos encontrado una dirección de correo electrónico en la página
            return true;
        }

        // Enlaces a la sección de contacto
        final List<String> contactTexts = Arrays.asList(contactRegExp.split("\\|"));
        final List<Element> links     = EvaluatorUtils.getElementsByTagName(document, "a");
        for (Element link : links) {
            final String linkText = link.getTextContent().toLowerCase().trim();
            final String linkTitle = link.getAttribute("title").toLowerCase().trim();
            for (String contactText : contactTexts) {
                if (linkText.contains(contactText) || linkTitle.contains(contactText)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Comprueba si un documento HTML tiene la fecha de la última revisión en la página de declaración de accesibilidad aplicando una serie de patrones de fecha sobre el texto.
     *
     * @param document   documento DOM HTML sobre el que buscar la fecha de la última revisión
     * @param dateRegExp expresión regular que identifica un formato de fecha
     * @return true si se ha detectado la fecha de la última revisión, false en caso contrario
     */
    public static boolean hasRevisionDate(final Document document, final String dateRegExp) {
        final Pattern pattern = Pattern.compile(dateRegExp, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(getDocumentText(document));

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

    public static Document getRemoteDocument(final String documentUrlStr, final String remoteUrlStr) throws IOException, SAXException {
        try {
            final HttpURLConnection connection = EvaluatorUtils.getConnection(remoteUrlStr, "GET", true);
            connection.setRequestProperty("referer", documentUrlStr);
            connection.connect();
            final int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                final DOMParser parser = new CheckerParser(true);
                parser.parse(new InputSource(connection.getInputStream()));
                return parser.getDocument();
            } else {
                return null;
            }
        } catch (RuntimeException t) {
            return null;
        }
    }

    public static String getElementText(Node checkedNode, boolean backward, final List<String> inlineTags) {
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
    public static boolean isFalseBrNode(final Element checkedElement, final List<String> inlineTags, final Pattern pattern, boolean backward) {
        final String text = getElementText(checkedElement, backward, inlineTags);

        final Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static String getBaseUrl(final Element documentElement) {
        final NodeList bases = documentElement.getElementsByTagName("base");

        for (int i = bases.getLength() - 1; i >= 0; i--) {
            final Element base = (Element) bases.item(i);
            if (base.hasAttribute("href") && StringUtils.isNotEmpty(base.getAttribute("href"))) {
                return base.getAttribute("href");
            }
        }

        return null;
    }

    public static boolean isValidUrl(final Element elementRoot, final Node nodeNode) {
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

            if (!remoteUrl.getProtocol().startsWith("http")) {
                return true;
            }

            // Consideramos que cualquier enlace a W3C (Internacional o España) o al portal TAW funcionan siempre además
            // de enlaces a las redes sociales más famosas (twitter, facebook, flickr, tuenti,...).
            if ("jigsaw.w3.org".equals(remoteUrl.getHost())
                    || "validator.w3.org".equals(remoteUrl.getHost())
                    || "www.w3.org".equals(remoteUrl.getHost())
                    || "www.w3c.es".equals(remoteUrl.getHost())
                    || "www.tawdis.net".equals(remoteUrl.getHost())
                    || "twitter.com".equals(remoteUrl.getHost())
                    || "www.facebook.com".equals(remoteUrl.getHost())
                    || "www.flickr.com".equals(remoteUrl.getHost())
                    || "www.tuenti.com".equals(remoteUrl.getHost())
                    || "plus.google.com".equals(remoteUrl.getHost())) {
                return true;
            }

            final PropertiesManager pmgr = new PropertiesManager();
            final List<String> allowedPorts = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "broken.links.allowed.ports").split(";"));

            if (allowedPorts.contains(String.valueOf(remoteUrl.getPort()))) {
                checkedLinks = (CheckedLinks) elementRoot.getUserData("checkedLinks");
                if (checkedLinks == null) {
                    checkedLinks = new CheckedLinks();
                    elementRoot.setUserData("checkedLinks", checkedLinks, null);
                }
                if (checkedLinks.getCheckedLinks().contains(remoteUrl.toString())) {
                    // Los enlaces ya verificados los damos por buenos, no puntúan mal varias veces
                    return true;
                } else if (!checkedLinks.getBrokenLinks().contains(remoteUrl.toString()) && !checkedLinks.getAvailablelinks().contains(remoteUrl.toString())) {
                    Logger.putLog("Verificando que existe la URL " + nodeNode.getTextContent() + " --> " + remoteUrl.toString(), Check.class, Logger.LOG_LEVEL_DEBUG);
                    final HttpURLConnection connection = EvaluatorUtils.getConnection(remoteUrl.toString(), "GET", true);
                    if (documentUrl != null) {
                        connection.setRequestProperty("referer", documentUrl.toString());
                    }
                    connection.connect();
                    final int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        checkedLinks.getBrokenLinks().add(remoteUrl.toString());
                        Logger.putLog("Encontrado enlace roto: " + nodeNode.getTextContent() + " --> " + remoteUrl.toString(), Check.class, Logger.LOG_LEVEL_DEBUG);
                        return false;
                    } else {
                        checkedLinks.getAvailablelinks().add(remoteUrl.toString());
                        return true;
                    }
                } else {
                    if (checkedLinks.getBrokenLinks().contains(remoteUrl.toString())) {
                        Logger.putLog("Encontrado enlace roto: " + nodeNode.getTextContent() + " --> " + remoteUrl.toString(), Check.class, Logger.LOG_LEVEL_DEBUG);
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
        } catch (MalformedURLException e) {
            // Si la url no está bien formada porque usa protocolos no http (javascript:, tel:, mailto:...) se considera igualmente válida
            return true;
        } catch (Exception e) {
            Logger.putLog("Error al verificar si el elemento " + remoteUrl + " está roto:" + e.getMessage(), CheckUtils.class, Logger.LOG_LEVEL_WARNING);
            return false;
        } finally {
            if (remoteUrl != null && checkedLinks != null) {
                checkedLinks.getCheckedLinks().add(remoteUrl.toString());
            }
        }
        return true;
    }

    private static boolean isAbsolute(final String url) {
        return url.startsWith("http");
    }

    private static String encodeUrl(String path) throws UnsupportedEncodingException {
        path = path.replaceAll("[ \\+]", "%20");
        String[] pathArray = path.split("[:\\./?&=#(%20)]");
        for (String aPathArray : pathArray) {
            path = path.replaceAll(aPathArray, URLEncoder.encode(aPathArray, "UTF-8"));
        }

        return path;
    }

    public static boolean hasContent(final Node node) {
        final PropertiesManager pmgr = new PropertiesManager();
        final List<String> elements = Arrays.asList(pmgr.getValue("intav.properties", "content.tags").split(";"));
        if (node.getNodeType() == Node.TEXT_NODE && StringUtils.isNotEmpty(node.getTextContent()) && !StringUtils.isOnlyBlanks(node.getTextContent())) {
            return true;
        } else if (node.getNodeType() == Node.ELEMENT_NODE) {
            if (elements.contains(node.getNodeName().toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPreviousHeader(final String str1, final String str2) {
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

    public static int isEmptyDescendentContent(Node node, final Element elementGiven) {
        final PropertiesManager pmgr = new PropertiesManager();
        final List<String> noContentTags = Arrays.asList(pmgr.getValue("intav.properties", "ignored.tags").split(";"));
        while (node != null) {
            if (!node.getNodeName().equalsIgnoreCase(elementGiven.getNodeName()) && !isPreviousHeader(node.getNodeName(), elementGiven.getNodeName())) {
                //Si tiene contenido devuelves false
                if (node.getNodeType() == Node.TEXT_NODE) {
                    if (StringUtils.isNotEmpty(node.getTextContent()) && !StringUtils.isOnlyBlanks(node.getTextContent())) {
                        return IntavConstants.IS_NOT_EMPTY;
                    }
                } else if (!noContentTags.contains(node.getNodeName().toUpperCase())) {
                    if (CheckUtils.hasContent(node)) {
                        return IntavConstants.IS_NOT_EMPTY;
                    } else {
                        int hasChildContent = CheckUtils.hasChildContent(node, elementGiven);
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
            node = node.getNextSibling();
        }
        return IntavConstants.IS_EMPTY;
    }

    public static int hasChildContent(final Node node, final Element elementGiven) {
        final NodeList nodeList = node.getChildNodes();
        final PropertiesManager pmgr = new PropertiesManager();
        final List<String> elements = Arrays.asList(pmgr.getValue("intav.properties", "ignored.tags").split(";"));
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

    public static ImageReader getImageReader(final Element img, final URL url) throws IOException {
        if (img.getUserData(IntavConstants.GIF_VERIFICATED) == null && img.getUserData(IntavConstants.GIF_READER) == null) {
            img.setUserData(IntavConstants.GIF_VERIFICATED, Boolean.TRUE, null);

            Logger.putLog("Descargando la imagen " + url + " para analizar su contenido", CheckUtils.class, Logger.LOG_LEVEL_INFO);
            final HttpURLConnection connection = EvaluatorUtils.getConnection(url.toString(), "GET", true);
            final PropertiesManager pmgr = new PropertiesManager();
            connection.setConnectTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "gif.connection.timeout")));
            connection.setReadTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "gif.connection.timeout")));
            ImageInputStream imageInputStream = new MemoryCacheImageInputStream(connection.getInputStream());

            final java.util.Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
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
            index = domain.lastIndexOf('.', index - 1);
            if (index != -1) {
                domain = domain.substring(index);
            } else {
                // Si la url comenzó directamente con el dominio de 2º nivel
                // http://algo.es le anteponemos el caracter '.' para indicar
                // comienzo de dominio
                domain = "." + domain;
            }
        }

        String newHost;
        if (URI.create(link).isAbsolute()) {
            newHost = new URL(link).getHost();
        } else {
            newHost = new URL(new URL(url), link).getHost();
        }

        index = newHost.lastIndexOf('.');

        if (index != -1) {
            index = newHost.lastIndexOf('.', index - 1);
            if (index != -1) {
                newHost = newHost.substring(index);
            } else {
                newHost = "." + newHost;
            }
        }
        return newHost.equalsIgnoreCase(domain);
    }

    /**
     * Comprueba si un documento HTML tiene declaración de conformidad de accesibilidad aplicando una serie de patrones sobre los enlaces e imagenes
     *
     * @param document documento HTML sobre el que buscar la declaración de conformidad de accesibilidad
     * @return true si se ha detectado una declaración de conformidad, false en caso contrario
     */
    public static boolean hasConformanceLevel(final Document document) {
        /*
        “Nivel .* A”, “Nivel .* AA”, “Nivel .* AAA” (.* por si se incluye algún texto adicional como “Nivel de Accesibilidad AA”, “Nivel de Conformidad AA”, etc.).-->
        Un texto con los patrones “doble A”, “triple AAA”, “prioridad X” (con x = 1, 2 o 3).
        Iconos de conformidad del W3C identificándolos buscando patrones similares a los anteriores en su texto alternativo o, en caso de ser enlaces, reconociendo las URLs de las páginas de conformidad del W3C.
         */
        final NodeList enlaces = document.getElementsByTagName("a");
        for (int i = 0; i < enlaces.getLength(); i++) {
            final Element tag = (Element) enlaces.item(i);
            if (tag.getAttribute("href") != null) {
                final String href = tag.getAttribute("href");
                if (WCAG1A.equalsIgnoreCase(href) || WCAG2A.equalsIgnoreCase(href)) {
                    return true;
                } else if (WCAG1AA.equalsIgnoreCase(href) || WCAG2AA.equalsIgnoreCase(href)) {
                    return true;
                } else if (WCAG1AAA.equalsIgnoreCase(href) || WCAG2AAA.equalsIgnoreCase(href)) {
                    return true;
                }
            }
        }
        final NodeList images = document.getElementsByTagName("img");
        for (int i = 0; i < images.getLength(); i++) {
            final Element tag = (Element) images.item(i);

            if (tag.getAttribute("src") != null) {
                final String src = tag.getAttribute("src");
                if (src.contains(SRC1AAA) || src.contains(TAW1AAA) || src.contains(TAW2AAA) || src.contains(SRC2AAA)) {
                    return true;
                } else if (src.contains(SRC1AA) || src.contains(TAW1AA) || src.contains(TAW2AA) || src.contains(SRC2AA)) {
                    return true;
                } else if (src.contains(SRC1A) || src.contains(TAW1A) || src.contains(TAW2A) || src.contains(SRC2A)) {
                    return true;
                }
            }
            if (tag.getAttribute("alt") != null) {
                final String alt = tag.getAttribute("alt");
                for (Pattern pattern : ALT_A) {
                    if (pattern.matcher(alt).find()) {
                        return true;
                    }
                }
                for (Pattern pattern : ALT_AA) {
                    if (pattern.matcher(alt).find()) {
                        return true;
                    }
                }
                for (Pattern pattern : ALT_AAA) {
                    if (pattern.matcher(alt).find()) {
                        return true;
                    }
                }
            }
        }
        final String text = getDocumentText(document);
        for (Pattern pattern : ALT_A) {
            if (pattern.matcher(text).find()) {
                return true;
            }
        }
        for (Pattern pattern : ALT_AA) {
            if (pattern.matcher(text).find()) {
                return true;
            }
        }
        for (Pattern pattern : ALT_AAA) {
            if (pattern.matcher(text).find()) {
                return true;
            }
        }

        return false;
    }

    // Patrones usados para la función hasConformanceLevel
    private static final String WCAG1A = "http://www.w3.org/WAI/WCAG1A-Conformance";
    private static final String WCAG1AA = "http://www.w3.org/WAI/WCAG1AA-Conformance";
    private static final String WCAG1AAA = "http://www.w3.org/WAI/WCAG1AAA-Conformance";

    private static final String WCAG2A = "http://www.w3.org/WAI/WCAG2A-Conformance";
    private static final String WCAG2AA = "http://www.w3.org/WAI/WCAG2AA-Conformance";
    private static final String WCAG2AAA = "http://www.w3.org/WAI/WCAG2AAA-Conformance";

    private static final String TAW1A = "taw_1_A.png";
    private static final String TAW2A = "taw_2_A.png";
    private static final String TAW1AA = "taw_1_AA.png";
    private static final String TAW2AA = "taw_2_AA.png";
    private static final String TAW1AAA = "taw_1_AAA.png";
    private static final String TAW2AAA = "taw_2_AAA.png";

    private static final String SRC1A = "wcag1A";
    private static final String SRC1AA = "wcag1AA";
    private static final String SRC1AAA = "wcag1AAA";

    private static final String SRC2A = "wcag2A";
    private static final String SRC2AA = "wcag2AA";
    private static final String SRC2AAA = "wcag2AAA";

    private static final Pattern[] ALT_A = new Pattern[]{
            Pattern.compile("\\blevel\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivel\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bwcag\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\baccesibilidad\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bprioridad\\s+1\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformidad\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
    };

    private static final Pattern[] ALT_AA = new Pattern[]{
            Pattern.compile("\\blevel\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\blevel\\s+double(\\s+|-)a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivel\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivel\\s+doble(\\s+|-)a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bwcag\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\baccesibilidad\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bprioridad\\s+2\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformidad\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformidad\\s+doble\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
    };

    private static final Pattern[] ALT_AAA = new Pattern[]{
            Pattern.compile("\\blevel\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\blevel\\s+triple(\\s+|-)a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivel\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivel\\s+triple(\\s|-)+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bwcag\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\baccesibilidad\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bprioridad\\s+3\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformidad\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformidad\\s+triple\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
    };
}
