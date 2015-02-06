/*
Copyright ©2006, University of Toronto. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a 
copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included 
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Adaptive Technology Resource Centre, University of Toronto
130 St. George St., Toronto, Ontario, Canada
Telephone: (416) 978-4360
*/

package ca.utoronto.atrc.tile.accessibilitychecker;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.CssValidationError;
import es.inteco.common.IntavConstants;
import es.inteco.common.ValidationError;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.cyberneko.html.HTMLConfiguration;
import es.inteco.intav.iana.IanaLanguages;
import es.inteco.intav.iana.IanaUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.apache.commons.codec.net.URLCodec;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EvaluatorUtility {
    // the evaluator
    private static Evaluator evaluator = null;

    //guideline
    private static Map<String, Guideline> guidelineMap = new HashMap<String, Guideline>();

    // initialization flag
    private static boolean initialized = false;

    // the collection of all the checks
    private static AllChecks allChecks = new AllChecks();

    private static IanaLanguages ianaLanguages = new IanaLanguages();

    private static int concurrentUsers = 0;

    private EvaluatorUtility() {
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static int getConcurrentUsers() {
        return concurrentUsers;
    }

    public static void setConcurrentUsers(int concurrentUsers) {
        EvaluatorUtility.concurrentUsers = concurrentUsers;
    }

    // initialize
    public static synchronized boolean initialize() throws Exception {
        if (initialized) {
            return true;
        }

        setSystemProperties();

        evaluator = new Evaluator();
        if (!loadAllChecks()) {
            return false;
        }

        if (!loadIanaLanguages()) {
            return false;
        }

        initialized = true;
        return true;
    }

    public static Evaluator getEvaluator() {
        return evaluator;
    }

    public static IanaLanguages getIanaLanguages() {
        return ianaLanguages;
    }

    // Returns the object that holds all the possible accessibility checks.
    public static AllChecks getAllChecks() {
        return allChecks;
    }

    // Returns the link text for the given element.
    public static String getLinkText(final Node node) {
        // get child text
        String textChild = getElementText(node);

        // get title attribute text
        String textTitle = ((Element) node).getAttribute("title");

        // check if there is an image within the element and get its alt text
        NodeList listImages = ((Element) node).getElementsByTagName("img");
        String textAlt = "";
        for (int x = 0; x < listImages.getLength(); x++) {
            Element elementImage = (Element) listImages.item(x);
            String stringTemp = elementImage.getAttribute("alt");
            // use the longest alt text for any image within the anchor
            if (stringTemp.length() > textAlt.length()) {
                textAlt = stringTemp;
            }
        }

        // return the longest string
        if (textChild.length() >= textTitle.length()) {
            if (textChild.length() >= textAlt.length()) {
                return textChild;
            } else {
                return textAlt;
            }
        } else {
            if (textTitle.length() >= textAlt.length()) {
                return textTitle;
            } else {
                return textAlt;
            }
        }
    }

    public static String getElementText(final Node node, final boolean getOnlyInlineTagsText) {
        List<String> inlineTags = null;
        if (getOnlyInlineTagsText) {
            PropertiesManager pmgr = new PropertiesManager();
            inlineTags = Arrays.asList(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "inline.tags.list").toUpperCase().split(";"));
        }

        final String text = getElementTextLoop(node, inlineTags);

        if (text.length() == 0) {
            return "";
        }

        // remove any tabs, lineends, multiple spaces, and leading/trailing whitespace
        final StringBuilder buffer2 = new StringBuilder(text.length());
        boolean space = false;
        for (int x = 0; x < text.length(); x++) {
            if (text.charAt(x) != '\t' && text.charAt(x) != '\n') {
                if (text.charAt(x) == ' ') {
                    if (space) {
                        continue;
                    }
                    space = true;
                } else {
                    space = false;
                }
                buffer2.append(text.charAt(x));
            }
        }
        return buffer2.toString().trim();
    }

    // returns the text that is contained by the given element
    public static String getElementText(Node node) {
        return getElementText(node, false);
    }

    // Recursive function that returns a string containing all the text within the node
    private static String getElementTextLoop(Node node, List<String> inlineTags) {
        try {
            StringBuilder buffer = new StringBuilder();

            NodeList childNodes = node.getChildNodes();
            for (int x = 0; x < childNodes.getLength(); x++) {
                Node nodeChild = childNodes.item(x);
                if (nodeChild == null) {
                    break;
                }
                if (inlineTags != null && nodeChild.getNodeType() == Node.ELEMENT_NODE &&
                        !inlineTags.contains(nodeChild.getNodeName().toUpperCase())) {
                    continue;
                }
                // comments within scripts are treated as 'text' nodes so ignore them
                if ((nodeChild.getNodeType() == Node.ELEMENT_NODE) &&
                        (nodeChild.getNodeName().equalsIgnoreCase("script"))) {
                    continue;
                }
                if (nodeChild.getNodeType() == Node.TEXT_NODE) {
                    buffer.append(nodeChild.getNodeValue());
                    if (inlineTags == null) {
                        //buffer.append(" ");
                    }
                }
                buffer.append(getElementTextLoop(nodeChild, inlineTags));
            }
            return buffer.toString();
        } catch (Exception e) {
            Logger.putLog("Exception: ", EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return "";
    }

    // returns the text that is contained by the given label element
    public static String getLabelText(final Node node) {
        final String text = getLabelTextLoop(node);
        if (text.length() == 0) {
            return "";
        }

        // remove any tabs, lineends, multiple spaces, and leading/trailing whitespace
        final StringBuilder buffer2 = new StringBuilder(text.length());
        boolean space = false;
        for (int x = 0; x < text.length(); x++) {
            if ((text.charAt(x) != '\t') && (text.charAt(x) != '\n')) {
                if (text.charAt(x) == ' ') {
                    if (space) {
                        continue;
                    }
                    space = true;
                } else {
                    space = false;
                }
                buffer2.append(text.charAt(x));
            }
        }
        return buffer2.toString().trim();
    }

    // Recursive function that returns a string containing all the text within the node.
    // Any text within a 'select' element is ignored.
    public static String getLabelTextLoop(final Node node) {
        final StringBuilder buffer = new StringBuilder();

        final NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            final Node nodeChild = childNodes.item(x);
            // comments within scripts are treated as 'text' nodes so ignore them
            if ((nodeChild.getNodeType() == Node.ELEMENT_NODE) &&
                    (nodeChild.getNodeName().equalsIgnoreCase("script"))) {
                continue;
            }
            if ((nodeChild.getNodeType() == Node.ELEMENT_NODE) &&
                    (nodeChild.getNodeName().equalsIgnoreCase("select"))) {
                continue;
            }
            if ((nodeChild.getNodeType() == Node.ELEMENT_NODE) &&
                    (nodeChild.getNodeName().equalsIgnoreCase("abbr") || nodeChild.getNodeName().equalsIgnoreCase("input"))) {
                buffer.append(((Element) nodeChild).getAttribute("title"));
                buffer.append(" ");
            }
            if (nodeChild.getNodeType() == Node.TEXT_NODE) {
                buffer.append(nodeChild.getNodeValue());
                buffer.append(" ");
            }
            buffer.append(getLabelTextLoop(nodeChild));
        }
        return buffer.toString();
    }

    // looks through the given node for first child instance of a given node
    // Returns the first instance of the node or null if not found.
    public static Node findNode(final Node parent, final String nameNode) {
        final NodeList childNodes = parent.getChildNodes();

        for (int x = 0; x < childNodes.getLength(); x++) {
            if (childNodes.item(x).getNodeName().compareToIgnoreCase(nameNode) == 0) {
                return childNodes.item(x);
            }

            Node node = findNode(childNodes.item(x), nameNode);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    // Loads all the checks
    private static boolean loadAllChecks() throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        // checks should only be loaded once but can be reloaded
        if (!allChecks.isEmpty()) {
            Logger.putLog("Note: Loading checks after they are already loaded.", EvaluatorUtility.class, Logger.LOG_LEVEL_INFO);
            // clear the old collection of guidelines
            allChecks.clear();
        }

        final InputStream inputStream = EvaluatorUtility.class.getClassLoader().getResourceAsStream(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "check.path"));
        loadChecksFile(inputStream);

        checkPrerequisites();

        return true;
    }


    private static void checkPrerequisites() {
        // check the prerequisites for each check to make sure they are OK
        final List<Check> checks = allChecks.getChecks();
        for (Check check : checks) {
            final List<Integer> prerequisites = check.getPrerequisites();
            for (Integer prerequisiteId : prerequisites) {
                final Check checkPrerequisite = allChecks.getCheck(prerequisiteId);
                if (checkPrerequisite == null) {
                    Logger.putLog("Warning: prerequisite check " + prerequisiteId + " on test " + check.getId() + " does not exist!", EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
                    continue;
                }
                final String stringPrereqTrigger = checkPrerequisite.getTriggerElement();
                if (stringPrereqTrigger == null) {
                    continue;
                }
                final String stringTrigger = check.getTriggerElement();
                if (stringTrigger == null) {
                    continue;
                }
            }
        }
    }

    // load a file that contains accessibility checks
    private static boolean loadChecksFile(final InputStream inputStream) {
        try {
            if (inputStream == null) {
                Logger.putLog("Error: Can't open checks file", EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
                return false;
            } else {
                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                final DocumentBuilder builder = factory.newDocumentBuilder();
                final Document docAllChecks = builder.parse(inputStream);

                final Element nodeRoot = docAllChecks.getDocumentElement();

                findChecksInMasterFile(nodeRoot);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
            return false;
        }
        return true;
    }


    private static void findChecksInMasterFile(final Element nodeRoot) {
        // find all the checks in the master file
        final NodeList childNodes = nodeRoot.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            if (childNodes.item(x).getNodeType() == Node.ELEMENT_NODE) {
                if (childNodes.item(x).getNodeName().equals("check")) {
                    // get the check ID number
                    final String stringId = ((Element) childNodes.item(x)).getAttribute("id");
                    if ((stringId == null) || (stringId.length() < 1)) {
                        Logger.putLog("Warning: Check has no ID in checks file ", EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
                        continue;
                    }
                    int idNumber;
                    try {
                        idNumber = Integer.parseInt(stringId);
                    } catch (NumberFormatException nfe) {
                        Logger.putLog("Warning: Check has bad ID (" + stringId + ") in checks file", EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
                        continue;
                    }

                    // have we already created this check?
                    Check check = allChecks.getCheck(idNumber);
                    if (check == null) {
                        // no, so create a new check
                        check = new Check();

                        // initialize the check and add it to the list of checks
                        if (check.initialize((Element) childNodes.item(x), idNumber)) {
                            allChecks.addCheck(check);
                        }
                    } else { // we already have the check
                        // add new language text to the check
                        check.setCheckText(childNodes.item(x));
                    }
                }
            }
        }
    }

    private static boolean loadIanaLanguages() throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        Logger.putLog("Cargando los códigos de lenguaje de IANA", EvaluatorUtility.class, Logger.LOG_LEVEL_INFO);
        final String ianaRegistries = IanaUtils.loadIanaRegistries(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "iana.lang.codes.url"));
        ianaLanguages.setLanguages(IanaUtils.getIanaList(ianaRegistries, pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "iana.language.type")));
        ianaLanguages.setRegions(IanaUtils.getIanaList(ianaRegistries, pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "iana.region.type")));
        ianaLanguages.setVariants(IanaUtils.getIanaList(ianaRegistries, pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "iana.variant.type")));

        return true;
    }

    // Returns the guideline from the guideline filename.
    public static Guideline loadGuideline(String filename) {
        // check for .xml extension
        int indexDotxml = filename.indexOf(".xml");
        if (indexDotxml == -1) {
            // doesn't have .xml extension so add it
            filename += ".xml";
        }

        if (guidelineMap.containsKey(filename)) {
            return guidelineMap.get(filename);
        } else {
            //ClassLoader cl = ClassLoader.getSystemClassLoader();
            InputStream inputStream = null;
            final Guideline guideline = new Guideline();
            try {
                inputStream = EvaluatorUtility.class.getClassLoader().getResourceAsStream("guidelines/" + filename);
                if (!guideline.initialize(inputStream, filename)) {
                    Logger.putLog("Error: guideline did not initialize: " + filename, EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
                    return null;
                } else {
                    guidelineMap.put(filename, guideline);
                    return guideline;
                }
            } catch (Exception e) {
                Logger.putLog("Excepción: ", EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
                return null;
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    Logger.putLog("Excepción: ", EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }

    private static void setSystemProperties() {
        final PropertiesManager pmgr = new PropertiesManager();
        if (pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "http.proxy.active").equalsIgnoreCase(Boolean.TRUE.toString())) {
            System.setProperty("http.proxyHost", pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "http.proxy.host"));
            System.setProperty("http.proxyPort", pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "http.proxy.port"));
            System.setProperty("https.proxyHost", pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "http.proxy.host"));
            System.setProperty("https.proxyPort", pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "http.proxy.port"));
        }
    }


    public static boolean isLanguageCode(final String stringGiven) {
        final String[] langArray = stringGiven.split("-");
        if (StringUtils.isEmpty(langArray[0])) {
            // No ha especificado lenguaje
            return false;
        } else if (langArray.length == 1 && ianaLanguages.getLanguages().contains(langArray[0].toLowerCase())) {
            return true;
        } else if (langArray.length == 2 && ianaLanguages.getLanguages().contains(langArray[0].toLowerCase()) &&
                (ianaLanguages.getRegions().contains(langArray[1].toUpperCase()) || ianaLanguages.getVariants().contains(langArray[1].toLowerCase()))) {
            return true;
        } else {
            return false;
        }
    }

    public static URL openUrl(String filename) {
        // make sure filename starts with 'http://'
        if (!filename.toLowerCase().startsWith("http://") && !filename.toLowerCase().startsWith("https://")) {
            filename = "http://" + filename;
        }

        try {
            URI srcUri = new URI(filename);
            URL srcUrl = srcUri.toURL();

//            HttpURLConnection httpConnection = EvaluatorUtils.getConnection(srcUrl.toString(), "GET", true);
//            httpConnection.connect();
            return srcUrl;
        } catch (Exception e) {
            Logger.putLog("Note: Can't open file: " + filename, EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }
    }

    // Loads the file and returns a Document object. Returns null if file can't be loaded.

    public static Document loadHtmlFile(final CheckAccessibility checkAccessibility, boolean htmlValidationNeeded, boolean cssValidationNeeded, String language, boolean fromCrawler) {
        try {
            final HttpURLConnection connection = EvaluatorUtils.getConnection(checkAccessibility.getUrl(), "GET", true);

            try {
                final long inicio = System.currentTimeMillis();
                // connect to the server
                connection.connect();
                final InputStream content = connection.getInputStream();

                final BufferedInputStream stream = new BufferedInputStream(content);

                // mark InputStream so we can restart it for validator
                if (stream.markSupported()) {
                    stream.mark(Integer.MAX_VALUE);
                }

                final String charset = EvaluatorUtils.getResponseCharset(connection, stream);

                long tiempo = System.currentTimeMillis() - inicio;
                // Registramos en el log el tiempo de la conexión
                Logger.putLog("Tiempo tardado en cargar el HTML remoto: " + tiempo + " milisegundos", Evaluator.class, Logger.LOG_LEVEL_INFO);
                return loadHtmlFile(stream, checkAccessibility, htmlValidationNeeded, cssValidationNeeded, language, fromCrawler, charset);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception e) {
            Logger.putLog("Exception: " + checkAccessibility.getUrl(), EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }
    }

    private static void setFeature(final CheckerParser parser, final String feature, final boolean value) {
        try {
            parser.setFeature(feature, value);
        } catch (SAXNotRecognizedException e) {
            throw new IllegalStateException("SAXNotRecognizedException thrown when setting " + feature + " to " + value);
        } catch (SAXNotSupportedException e) {
            throw new IllegalStateException("SAXNotSupportedException thrown when setting " + feature + " to " + value);
        }
    }

    public static Document loadHtmlFile(final InputStream inputStream, final CheckAccessibility checkAccessibility,
                                        boolean htmlValidationNeeded, boolean cssValidationNeeded, String language, boolean fromCrawler, String charset) {
        try {
            // create a DOM of the HTML file
            CheckerParser parser = new CheckerParser();
            Document doc = null;
            Element elementRoot = null;

            String inStr = StringUtils.getContentAsString(inputStream, charset);
            // Si se utiliza iframe como etiqueta simple (sin cuerpo) se produce problema al parsear, las eliminamos sin más
            inStr = inStr.replaceAll("(?i)<iframe [^>]*/>", "");
            for (int i = 0; i < 2 && (doc == null || elementRoot == null); i++) {
                parser.setFilename(checkAccessibility.getUrl());

                setFeature(parser, "http://xml.org/sax/features/namespaces", false);

                inStr = addFinalTags(inStr);
                InputStream newInputStream = new ByteArrayInputStream(inStr.getBytes(charset));
                if (newInputStream.markSupported()) {
                    newInputStream.mark(Integer.MAX_VALUE);
                }

                try {
                    // Reseteamos el stream para volver a analizarlo
                    newInputStream.reset();
                    InputSource inputSource = new InputSource(newInputStream);

                    parser.parse(inputSource);
                    doc = parser.getDocument();
                    // store the doctype status on the root element
                    elementRoot = doc.getDocumentElement();
                } catch (Exception e) {
                    parser = new CheckerParser(new HTMLConfiguration(true));
                    Logger.putLog("Error al parsear al documento. Se intentará de nuevo con el balanceo de etiquetas", EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
                }

            }

            if (!inStr.toUpperCase().contains("<HTML") && doc.getElementsByTagName("html") != null && doc.getElementsByTagName("html").getLength() > 0) {
                doc.getElementsByTagName("html").item(0).setUserData("realHtml", "false", null);
            }

            List<Node> nodeList = new ArrayList<Node>();
            EvaluatorUtils.generateNodeList(elementRoot, nodeList, 1000);

            if (parser.hasDoctype()) {
                elementRoot.setUserData("doctype", "true", null);
                elementRoot.setUserData("doctypeType", parser.getHtmlXhtml(), null);
                elementRoot.setUserData("doctypeVersion", parser.getHtmlVersion(), null);
                elementRoot.setUserData("doctypeLine", String.valueOf(parser.getDoctypeLine()), null);
                elementRoot.setUserData("doctypePublicId", parser.getDoctypePublicId(), null);
                elementRoot.setUserData("doctypeSystemId", parser.getDoctypeSystemId(), null);
                elementRoot.setUserData("doctypeSource", generateDoctypeSource(doc.getDoctype()), null);
            } else {
                elementRoot.setUserData("doctype", "false", null);
            }

            elementRoot.setUserData("url", checkAccessibility.getUrl(), null);

            // Aprovechamos para guardar el código fuente en una variable
            inputStream.reset();
            elementRoot.setUserData("source", StringUtils.getContentAsString(inputStream, charset), null);

            // count the number of rows and columns in each table
            // store the result on the node
            NodeList listTables = doc.getDocumentElement().getElementsByTagName("table");
            for (int x = 0; x < listTables.getLength(); x++) {
                int countRows = countElements((Element) listTables.item(x), "tr", "table", -1);
                listTables.item(x).setUserData("rows", countRows, null);

                // number of columns in the table
                // count the 'th' and 'td' elements in first row
                int maxCountCols = 0;
                NodeList listRows = ((Element) listTables.item(x)).getElementsByTagName("tr");
                if (listRows.getLength() > 0) {
                    for (int i = 0; i < listRows.getLength(); i++) {
                        int countCols = 0;
                        NodeList listCols = listRows.item(i).getChildNodes();
                        for (int y = 0; y < listCols.getLength(); y++) {
                            if ((listCols.item(y).getNodeName().equalsIgnoreCase("td")) ||
                                    (listCols.item(y).getNodeName().equalsIgnoreCase("th"))) {
                                countCols++;

                                // increment number of columns depending on 'colspan' attribute
                                String stringColspan = ((Element) listCols.item(y)).getAttribute("colspan");
                                try {
                                    int additionalCols = Integer.parseInt(stringColspan);
                                    countCols += additionalCols - 1;
                                } catch (NumberFormatException e) {
                                    Logger.putLog("Exception colspan no válido", EvaluatorUtility.class, Logger.LOG_LEVEL_DEBUG);
                                }
                            }
                        }
                        if (countCols > maxCountCols) {
                            maxCountCols = countCols;
                        }
                    }
                }
                listTables.item(x).setUserData("cols", maxCountCols, null);
            }

            // link map elements and area elements with their associated img/object elements
            NodeList listMaps = doc.getDocumentElement().getElementsByTagName("map");
            if (listMaps.getLength() > 0) {

                // create a list of all maps in the document
                for (int x = 0; x < listMaps.getLength(); x++) {
                    Element elementMap = (Element) listMaps.item(x);
                    String stringNameMap = "#" + elementMap.getAttribute("name");
                    if ( stringNameMap.length() == 0) {
                        continue;
                    }

                    // find the image or object for this map
                    NodeList listImages = doc.getDocumentElement().getElementsByTagName("img");
                    boolean bFoundIt = false;
                    String stringImageSource = "";
                    for (int y = 0; y < listImages.getLength(); y++) {
                        String stringUsemap = ((Element) listImages.item(y)).getAttribute("usemap");
                        if ((stringUsemap != null) && (stringUsemap.equals(stringNameMap))) {
                            stringImageSource = ((Element) listImages.item(y)).getAttribute("src");
                            bFoundIt = true;
                            break;
                        }
                    }

                    // look through objects if map not found in images
                    if (!bFoundIt) {
                        NodeList listObjects = doc.getDocumentElement().getElementsByTagName("object");
                        for (int y = 0; y < listObjects.getLength(); y++) {
                            String stringUsemap = ((Element) listObjects.item(y)).getAttribute("usemap");
                            if ((stringUsemap != null) && (stringUsemap.equals(stringNameMap))) {
                                stringImageSource = ((Element) listObjects.item(y)).getAttribute("data");
                                bFoundIt = true;
                                break;
                            }
                        }
                    }

                    // associate the image with the map (and map's area elements)
                    if (bFoundIt) {
                        elementMap.setUserData("map-src", stringImageSource, null);
                        // get list of area elements contained by map
                        NodeList listAreas = elementMap.getElementsByTagName("area");
                        for (int y = 0; y < listAreas.getLength(); y++) {
                            listAreas.item(y).setUserData("map-src", stringImageSource, null);
                        }
                    }
                }
            }
            // reread the file and validate it

            if (htmlValidationNeeded) {
                if (!inputStream.markSupported()) {
                    // can't reset stream, so can't validate, just return doc without validation
                    Logger.putLog("Warning: Can't validate file: " + checkAccessibility.getUrl(), EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
                } else {
                    // Validamos el documento
                    long time = System.currentTimeMillis();
                    elementRoot.setUserData("validationErrors", getValidationErrors(checkAccessibility, inputStream, charset), null);
                    Logger.putLog("Tiempo tardado en validar el HTML: " + (System.currentTimeMillis() - time) + " milisegundos", EvaluatorUtility.class, Logger.LOG_LEVEL_INFO);
                }
            }

            if (cssValidationNeeded) {
                // Validamos los CSS
                validateCss(doc, checkAccessibility, language);
            }

            // Cargamos los CSS
            loadCss(doc);

            // return the document
            return doc;
        } catch (Exception e) {
            Logger.putLog("Exception al evaluar " + checkAccessibility.getUrl() + ": ", EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }
    }

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

    private static void validateCss(final Document doc, final CheckAccessibility checkAccessibility, final String language) {
        final Element elementRoot = doc.getDocumentElement();
        final List<CssValidationError> cssValidationErrors = new ArrayList<CssValidationError>();
        long time = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(checkAccessibility.getUrl())) {
            cssValidationErrors.addAll(getCssValidationErrors(checkAccessibility.getUrl(), language));
        } else {
            cssValidationErrors.addAll(getCssValidationNotWorksMessage());

            final List<Element> styleSheets = getStyleSheets(doc);
            for (Element styleSheet : styleSheets) {
                cssValidationErrors.addAll(getCssValidationErrors(styleSheet.getAttribute("href"), language));
            }
        }
        Logger.putLog("Tiempo tardado en validar los CSS: " + (System.currentTimeMillis() - time) + " milisegundos", EvaluatorUtility.class, Logger.LOG_LEVEL_INFO);
        elementRoot.setUserData("cssValidationErrors", cssValidationErrors, null);
    }

    private static void loadCss(final Document doc) {
        final List<Element> styleSheets = getStyleSheets(doc);

        final Element elementRoot = doc.getDocumentElement();
        for (Element styleSheet : styleSheets) {
            loadStyleSheet(styleSheet, (String) elementRoot.getUserData("url"));
        }
    }

    private static void loadStyleSheet(final Element styleSheet, final String urlRoot) {
        try {
            final URL cssUrl;
            if (StringUtils.isNotEmpty(urlRoot)) {
                cssUrl = new URL(new URL(urlRoot), styleSheet.getAttribute("href"));
            } else {
                cssUrl = new URL(styleSheet.getAttribute("href"));
            }
            final HttpURLConnection connection = EvaluatorUtils.getConnection(cssUrl.toString(), "GET", true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                styleSheet.setUserData("css", StringUtils.getContentAsString(connection.getInputStream()), null);
            }
            connection.disconnect();
        } catch (Exception e) {
            Logger.putLog("Error al cargar la hoja de estilo localizada en " + styleSheet.getAttribute("href") + ": " + e.getMessage(), EvaluatorUtility.class, Logger.LOG_LEVEL_WARNING);
        }
    }

    private static List<Element> getStyleSheets(final Document doc) {
        final List<Element> styleSheets = new ArrayList<Element>();
        final NodeList links = doc.getElementsByTagName("link");
        for (int i = 0; i < links.getLength(); i++) {
            final Element link = (Element) links.item(i);
            if (link.hasAttribute("rel") && link.hasAttribute("type") &&
                    link.getAttribute("rel").equalsIgnoreCase("stylesheet") &&
                    link.getAttribute("type").equalsIgnoreCase("text/css")) {
                styleSheets.add(link);
            }
        }
        return styleSheets;
    }

    private static List<CssValidationError> getCssValidationNotWorksMessage() {
        final List<CssValidationError> cssValidationErrors = new ArrayList<CssValidationError>();
        final CssValidationError cssValidationError = new CssValidationError();
        final PropertiesManager pmgr = new PropertiesManager();
        final String urlHuman = pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.css.validator.human.submit.code");
        final String link = "<a href='" + urlHuman + "' title='Enlace externo'>Validador de hojas de estilo del W3C</a>";
        cssValidationError.setCode("En el modo de análisis de código fuente, la validación de las hojas de estilo solo funcionará si éstas se encuentran enlazadas desde algún servidor con acceso externo.");
        cssValidationError.setCode(cssValidationError.getCode() + " Si no dispone de esta posibilidad, puede validar su hoja de estilos mediante el  " + link);
        cssValidationError.setLine(1);
        cssValidationError.setSummary(true);

        cssValidationErrors.add(cssValidationError);

        return cssValidationErrors;
    }

    // Devuelve los errores de validación del código HTML
    public static List<ValidationError> getValidationErrors(CheckAccessibility checkAccessibility, InputStream inputStream, String charset) {
        List<ValidationError> validationErrors = new ArrayList<ValidationError>();

        try {
            // Reseteamos el inputStream a su estado inicial para poder convertirlo a texto
            inputStream.reset();

            String contents;
            if (checkAccessibility.isWebService() && StringUtils.isNotEmpty(checkAccessibility.getTemplateContent())) {
                contents = checkAccessibility.getTemplateContent();
            } else {
                contents = StringUtils.getContentAsString(inputStream, charset);
            }

            final Document document;
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                final PropertiesManager pmgr = new PropertiesManager();
                final HttpURLConnection connection = EvaluatorUtils.getConnection(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.validator"), "POST", true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

                final URLCodec codec = new URLCodec();
                writer.write("fragment=" + codec.encode(HTMLEntities.unhtmlAmpersand(HTMLEntities.htmlentities(contents))) + "&output=soap12");
                writer.flush();
                writer.close();

                connection.connect();

                document = builder.parse(connection.getInputStream());
            } catch (Exception e) {
                addValidationSummary(validationErrors, checkAccessibility.getUrl());
                throw e;
            }

            final NodeList errorNodes = document.getElementsByTagName("m:error");
            if (errorNodes.getLength() > 0) {
                final String[] lines = contents.split("\\n");
                for (int i = 0; i < errorNodes.getLength(); i++) {
                    final Node errorNode = errorNodes.item(i);
                    validationErrors.add(getValidationError(errorNode, lines, IntavConstants.VALIDATION_ERROR_TYPE_ERROR));
                }
            }

            // Corregimos el desajuste de una línea al meter el código en una plantilla HTML cuando viene del WS y es un fragmento de código
            if (checkAccessibility.isWebService() && StringUtils.isNotEmpty(checkAccessibility.getTemplateContent())) {
                for (ValidationError validationError : validationErrors) {
                    validationError.setLine(validationError.getLine() - 1);
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al validar el código HTML de " + checkAccessibility.getUrl(), EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return validationErrors;
    }


    private static void addValidationSummary(final List<ValidationError> validationErrors, final String filename) throws UnsupportedEncodingException {
        final ValidationError validationError = new ValidationError();
        final PropertiesManager pmgr = new PropertiesManager();
        final String urlHuman = pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.validator.human").replace("{0}", URLEncoder.encode(filename, "UTF-8"));
        final String link = "<a href='" + urlHuman + "' title='Enlace externo'>Analizador sintáctico del W3C</a>";

        validationError.setCode("Se ha producido un error al tratar de validar el código del HTML. Utilice la siguiente URL para verificar la sintaxis: " + link);
        validationError.setLine(1);
        validationError.setColumn(1);
        validationError.setNumErrors(0);
        validationError.setNumWarnings(0);
        validationError.setSummary(true);

        validationErrors.add(validationError);
    }

    // Este método por ahora no se va a usar, pero puede que más tarde se trabaje en él (no borrar)
    private static ValidationError getValidationError(final Node errorNode, final String[] lines, int type) {
        final ValidationError validationError = new ValidationError();
        validationError.setType(type);

        final NodeList errorInfo = errorNode.getChildNodes();
        for (int j = 0; j < errorInfo.getLength(); j++) {
            final Node nInfo = errorInfo.item(j);
            if (nInfo.getNodeName().equals(IntavConstants.TAG_COL)) {
                try {
                    validationError.setColumn(Integer.parseInt(nInfo.getTextContent()));
                } catch (Exception e) {
                    // No se hace nada, ya que el validador no tiene en cuenta la columna si es mayor de 80
                }
            } else if (nInfo.getNodeName().equals(IntavConstants.TAG_LINE)) {
                validationError.setLine(Integer.parseInt(nInfo.getTextContent()));
            } else if (nInfo.getNodeName().equals(IntavConstants.TAG_MESSAGE)) {
                validationError.setCode(IntavConstants.MESSAGE_DELIMITER + StringUtils.truncateText(nInfo.getTextContent(), 300) + IntavConstants.MESSAGE_DELIMITER);
            } else if (nInfo.getNodeName().equals(IntavConstants.TAG_MESSAGE_ID)) {
                validationError.setMessageId(nInfo.getTextContent());
            }
        }
        validationError.setSummary(false);

        try {
            String source = lines[validationError.getLine() - 1];
            if (source.length() > 500) {
                source = source.substring(0, 500);
            }
            validationError.setCode(validationError.getCode() + HTMLEntities.htmlAngleBrackets(source));
        } catch (Exception e) {
            // No hacemos nada, al validador a veces se le va la pinza
        }

        return validationError;
    }


    // Devuelve los errores de validación del código Css
    public static List<CssValidationError> getCssValidationErrors(final String filename, final String language) {
        final List<CssValidationError> cssValidationErrors = new ArrayList<CssValidationError>();
        final PropertiesManager pmgr = new PropertiesManager();

        if (StringUtils.isUrl(filename)) {
            try {
                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                final DocumentBuilder builder = factory.newDocumentBuilder();
                Document document;
                try {
                    final String url = pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.css.validator")
                            .replace("{0}", URLEncoder.encode(filename, "UTF-8"))
                            .replace("{1}", pmgr.getValue("language.mapping", language));

                    final HttpURLConnection connection = EvaluatorUtils.getConnection(url, "GET", true);
                    connection.connect();

                    document = builder.parse(StringUtils.fixBugInCssValidator(connection.getInputStream()));
                } catch (Exception e) {
                    addCssValidationSummary(cssValidationErrors, filename, language);
                    throw e;
                }

                final NodeList errorNodes = document.getElementsByTagName(IntavConstants.TAG_ERROR_LIST);

                String lastUri = "";

                for (int i = 0; i < errorNodes.getLength(); i++) {
                    final NodeList detailNodes = errorNodes.item(i).getChildNodes();
                    for (int j = 0; j < detailNodes.getLength(); j++) {
                        if (IntavConstants.TAG_URI.equalsIgnoreCase(detailNodes.item(j).getNodeName())) {
                            lastUri = detailNodes.item(j).getTextContent();
                        }
                        if (IntavConstants.TAG_ERROR.equalsIgnoreCase(detailNodes.item(j).getNodeName())) {
                            cssValidationErrors.add(getCssValidationError(detailNodes.item(j), lastUri));
                        }
                    }
                }

            } catch (Exception e) {
                Logger.putLog("Error al validar el código CSS de " + filename, EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }

        return cssValidationErrors;
    }

    private static CssValidationError getCssValidationError(final Node detailNode, final String cssUri) {
        final CssValidationError cssValidationError = new CssValidationError();

        final NodeList detailNodeChilds = detailNode.getChildNodes();
        for (int i = 0; i < detailNodeChilds.getLength(); i++) {
            if (IntavConstants.TAG_LINE.equals(detailNodeChilds.item(i).getNodeName())) {
                cssValidationError.setLine(Integer.parseInt(detailNodeChilds.item(i).getTextContent().trim()));
            } else if (IntavConstants.TAG_CONTEXT.equals(detailNodeChilds.item(i).getNodeName())) {
                cssValidationError.setContext(detailNodeChilds.item(i).getTextContent().trim());
            } else if (IntavConstants.TAG_SKIPPED_STRING.equals(detailNodeChilds.item(i).getNodeName())) {
                cssValidationError.setSkippedString(detailNodeChilds.item(i).getTextContent().trim());
            } else if (IntavConstants.TAG_MESSAGE.equals(detailNodeChilds.item(i).getNodeName())) {
                cssValidationError.setMessage(detailNodeChilds.item(i).getTextContent().trim());
            }
        }

        cssValidationError.setCode(IntavConstants.MESSAGE_DELIMITER + cssUri + ": " + cssValidationError.getMessage() + IntavConstants.MESSAGE_DELIMITER +
                " " + cssValidationError.getContext() + " { ..." + StringUtils.truncateText(cssValidationError.getSkippedString(), 300) + " ... }");

        cssValidationError.setSummary(false);

        return cssValidationError;
    }

    // Returns the number of elements within the given element.
    // Ignores any elements of the same type that are children of the given element.
    public static int countElements(final Element elementParent, final String nameElement, final String nameElementIgnore, int childLevel) {
        int count = 0;
        final NodeList listChildren = elementParent.getChildNodes();
        for (int x = 0; x < listChildren.getLength(); x++) {
            if (listChildren.item(x).getNodeType() == Node.ELEMENT_NODE) {
                // don't search through child elements that should be ignored
                if (nameElementIgnore != null && listChildren.item(x).getNodeName().equalsIgnoreCase(nameElementIgnore)) {
                    continue;
                }
                if (nameElement.equals("*") || nameElement.equalsIgnoreCase(listChildren.item(x).getNodeName())) {
                    count++;
                }
                if (childLevel == -1) {
                    count += countElements((Element) listChildren.item(x), nameElement, nameElementIgnore, childLevel);
                } else if (childLevel != 0) {
                    count += countElements((Element) listChildren.item(x), nameElement, nameElementIgnore, childLevel--);
                }
            }
        }
        return count;
    }

    private static void addCssValidationSummary(final List<CssValidationError> cssValidationErrors, final String filename, final String language) throws Exception {
        if ( filename!=null && language!=null ) {
            final CssValidationError cssValidationError = new CssValidationError();
            final PropertiesManager pmgr = new PropertiesManager();
            final String urlHuman = pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.css.validator.human")
                    .replace("{0}", URLEncoder.encode(filename, "UTF-8"))
                    .replace("{1}", pmgr.getValue("language.mapping", language));
            final String link = "<a href='" + urlHuman + "' title='Enlace externo'>Validador de hojas de estilo del W3C</a>";
            cssValidationError.setCode("Se ha producido un error al intentar validar el código CSS de la página. Utilice la siguiente URL para verificar la sintaxis: " + link);
            cssValidationError.setLine(1);
            cssValidationError.setSummary(true);

            cssValidationErrors.add(cssValidationError);
        }
    }

    public static String getLanguage(final HttpServletRequest inReq) {
        if (inReq.getSession().getAttribute(IntavConstants.LANGUAGE) == null) {
            final PropertiesManager pmgr = new PropertiesManager();
            final String language = pmgr.getValue(IntavConstants.LANGUAGE_MAPPING, inReq.getLocale().getLanguage());
            if (language != null) {
                inReq.getSession().setAttribute(IntavConstants.LANGUAGE, language);
            } else {
                inReq.getSession().setAttribute(IntavConstants.LANGUAGE, IntavConstants.ENGLISH_ABB);
            }
        }

        return (String) inReq.getSession().getAttribute(IntavConstants.LANGUAGE);
    }

    private static String generateDoctypeSource(final DocumentType docType) {
        String doctypeSource = null;

        if (docType != null) {
            if (docType.getSystemId() != null) {
                doctypeSource = "<!DOCTYPE {0} PUBLIC \"{1}\" \"{2}\">";
                doctypeSource = doctypeSource.replace("{0}", docType.getName().toUpperCase());
                doctypeSource = doctypeSource.replace("{1}", docType.getPublicId().toUpperCase());
                doctypeSource = doctypeSource.replace("{2}", docType.getSystemId());
            } else {
                doctypeSource = "<!DOCTYPE {0} PUBLIC \"{1}\">";
                doctypeSource = doctypeSource.replace("{0}", docType.getName().toUpperCase());
                doctypeSource = doctypeSource.replace("{1}", docType.getPublicId().toUpperCase());
            }
        }

        return doctypeSource;
    }

    public static String getAbsolute(final String stringRelative, String filenameURL) {
        // make sure the URL ends with a '/' if appropriate
        int slashLast = filenameURL.lastIndexOf('/') + 1;
        if (slashLast < 9) {
            filenameURL = filenameURL.concat("/");
        } else {
            if (slashLast != filenameURL.length()) {
                if (filenameURL.lastIndexOf('.') < slashLast) {
                    filenameURL = filenameURL.concat("/");
                }
            }
        }

        try {
            // trim stringRelative to make sure there is no whitespace at end of URL
            return new URI(filenameURL).resolve(stringRelative.trim()).toString();
        } catch (Exception e) {
            //Logger.putLog("Exception: ", EvaluatorUtility.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return "";
    }

    // Returns an attribute value that has been stripped of its session ID.
    public static String getAttributeNoSession(final Element element, final String attributeName) {
        final String stringValue = element.getAttribute(attributeName);
        if (stringValue.length() == 0) {
            return "";
        }

        int index = stringValue.indexOf(";jsession");
        if (index == -1) {
            return stringValue;
        }
        int indexEnd = stringValue.indexOf('?');
        if (indexEnd == -1) {
            return stringValue.substring(0, index);
        }

        return stringValue.substring(0, index) + stringValue.substring(indexEnd);
    }

}
