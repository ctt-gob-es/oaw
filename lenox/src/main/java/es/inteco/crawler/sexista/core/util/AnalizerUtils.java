package es.inteco.crawler.sexista.core.util;


import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.modules.analisis.service.AnalyzeService;
import es.inteco.crawler.sexista.modules.commons.Constants.AnalyzeConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalizerUtils {

    public static String getContent(String url, HttpURLConnection connection) throws Exception {
        InputStream markableInputStream = new BufferedInputStream(connection.getInputStream());

        markableInputStream.mark(Integer.MAX_VALUE);

        String content = getContentAsString(markableInputStream, getResponseCharset(connection, markableInputStream));

        List<String> frames = getFrames(content, url);

        content = getContentFromHtml(content);

        for (String frame : frames) {
            content += getContent(frame, connection);
        }

        return content;
    }

    private static List<String> getFrames(String source, String url) {
        List<String> frames = new ArrayList<>();

        Properties properties = ConfigUtil.getConfiguracion("lenox.properties");

        Pattern pattern = Pattern.compile(properties.getProperty("frame.reg.exp"), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(source);
        String frame = null;
        while (matcher.find()) {
            try {
                frame = matcher.group(1).replaceAll("\n", "");
                if (!frames.contains(frame)) {
                    frames.add(new URL(new URL(url), frame).toString());
                }
            } catch (Exception e) {
                Logger.getLogger(AnalizerUtils.class).info("No se ha podido añadir el frame " + frame);
            }
        }

        return frames;
    }

    public static String getContentFromHtml(String htmlSource) {
        String content;

        content = HTMLEntities.unhtmlentities(htmlSource).trim();

        content = removeInlineTags(content);

        content = getTextFromDOM(content);

        return content;
    }

    private static String removeHtml(String content) {
        return content.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ");
    }

    // Convierte un inputStream en un string
    private static String getContentAsString(InputStream in, String charset) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n, charset));
        }

        return out.toString();
    }

    // Convierte un inputStream en un string
    private static String getContentAsString(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }

        return out.toString();
    }

    private static String removeInlineTags(String content) {

        Properties properties = ConfigUtil.getConfiguracion("lenox.properties");

        List<String> inlineTags = Arrays.asList(properties.getProperty("inline.tags").split(";"));

        for (String tag : inlineTags) {
            content = Pattern.compile("</{0,1}" + tag + " +[^>]*>|</{0,1}" + tag + ">", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");
        }

        return content;
    }

    private static String getTextFromDOM(String content) {
        StringBuilder result = new StringBuilder();
        try {
            InputSource inputSource = new InputSource(new StringReader(content));

            DOMParser parser = new DOMParser(new HTMLConfiguration());
            parser.parse(inputSource);

            Document document = parser.getDocument();

            Properties properties = ConfigUtil.getConfiguracion("lenox.properties");

            List<String> ignoredTags = Arrays.asList(properties.getProperty("ignored.tags").split(";"));

            List<Node> nodes = new ArrayList<>();
            nodes = generateTextNodeList(document.getDocumentElement(), nodes, Integer.MAX_VALUE, ignoredTags);

            List<String> attributes = Arrays.asList(properties.getProperty("text.attributes").split(";"));

            for (Node node : nodes) {
                String text = "";
                if (node.getNodeType() == Node.TEXT_NODE) {
                    text = node.getTextContent().trim();

                } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    for (String attribute : attributes) {
                        if (((Element) node).hasAttribute(attribute)) {
                            text = ((Element) node).getAttribute(attribute).trim();
                        }
                    }
                }
                if (StringUtils.isNotEmpty(text)) {
                    result.append(text).append(".\n");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AnalizerUtils.class).error("Error al generar el texto del documento a partir de su árbol DOM: " + e.getMessage());
            return removeHtml(content);
        }

        return result.toString();
    }

    // Genera recursivamente una lista de nodos del documento
    private static List<Node> generateTextNodeList(Node node, List<Node> nodeList, int maxNumElements, List<String> ignoredTags) {
        if ((node != null) && (nodeList.size() <= maxNumElements)) {
            if ((!ignoredTags.contains(node.getNodeName().toUpperCase()) &&
                    ((node.getNodeType() == Node.ELEMENT_NODE) || (node.getNodeType() == Node.DOCUMENT_NODE) ||
                            (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) || node.getNodeType() == Node.TEXT_NODE))) {
                for (int x = 0; x < node.getChildNodes().getLength(); x++) {
                    generateTextNodeList(node.getChildNodes().item(x), nodeList, maxNumElements, ignoredTags);
                }
                if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
                    nodeList.add(node);
                }
            }
        }
        return nodeList;
    }

    private static String getResponseCharset(HttpURLConnection connection, InputStream markableInputStream) throws Exception {
        String charset = AnalyzeConstants.DEFAULT_ENCODING;
        boolean found = false;

        // Buscamos primero en las cabeceras de la respuesta
        try {
            String header = connection.getHeaderField("Content-type");
            String charsetValue = header.substring(header.indexOf("charset"));
            charsetValue = charsetValue.substring(charsetValue.indexOf('=') + 1);
            if (StringUtils.isNotEmpty(charsetValue)) {
                charset = charsetValue;
                // found = true;
            }
        } catch (Exception e) {
            // found = false;
        }


        // Si no lo hemos encontrado en las cabeceras, intentaremos buscarlo en la etiqueta <meta> correspondiente
        if (!found) {
            String regexp = "<meta.*charset=(.*?)\"";
            Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

            Matcher matcher = pattern.matcher(getContentAsString(markableInputStream));
            if (matcher.find()) {
                charset = matcher.group(1);
                found = true;
            }

            // Reseteamos el InputStream para poder leerlo de nuevo más tarde
            markableInputStream.reset();
        }

        if (found && !isValidCharset(charset)) {
            charset = AnalyzeConstants.DEFAULT_ENCODING;
        }

        return charset;
    }

    private static boolean isValidCharset(String charset) {
        try {
            byte[] test = new byte[10];
            new String(test, charset);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static HttpURLConnection getConnection(String url, String method, boolean followRedirects) throws Exception {
        Properties properties = ConfigUtil.getConfiguracion("lenox.properties");
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(followRedirects);
        connection.setRequestMethod(method);
        connection.setConnectTimeout(Integer.parseInt(properties.getProperty("connection.timeout")));
        connection.setReadTimeout(Integer.parseInt(properties.getProperty("connection.timeout")));
        connection.addRequestProperty("Accept", properties.getProperty("method.accept.header"));
        connection.addRequestProperty("Accept-Language", properties.getProperty("method.accept.language.header"));
        connection.addRequestProperty("User-Agent", properties.getProperty("method.user.agent.header"));
        return connection;
    }

    public static boolean passConcurrence() {
        Properties properties = ConfigUtil.getConfiguracion("lenox.properties");
        int concurrentUsers = AnalyzeService.getConcurrentUsers();
        int counter = 0;
        while (concurrentUsers >= Integer.parseInt(properties.getProperty("max.num.concurrent.users"))) {
            try {
                Logger.getRootLogger().info("Hay demasiados usuarios concurrentes, se va a esperar para atender la solicitud del usuario");
                concurrentUsers = AnalyzeService.getConcurrentUsers();

                if (counter >= Integer.parseInt(properties.getProperty("num.retries.check.concurrence"))) {
                    Logger.getRootLogger().info("Se va a desatender la solicitud debido a que hay demasiados usuarios concurrentes");
                    return false;
                }
                Thread.sleep(Integer.parseInt(properties.getProperty("time.retry.check.concurrence")));
                counter++;
            } catch (Exception e) {
                Logger.getRootLogger().error(e.getMessage());
            }
        }
        return true;
    }

    public static ActionErrors validateFile(FormFile file) {
        ActionErrors errors = new ActionErrors();
        PropertiesManager pmgr = new PropertiesManager();

        int maxLength = Integer.parseInt(pmgr.getValue("lenox.properties", "page.content.max.length"));
        if (file.getFileSize() >= maxLength) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.file.content.too.large"));
        } else if (!file.getContentType().equals("text/html") && !file.getContentType().equals("text/plain")) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.file.content.type.not.accepted"));
        }

        return errors;
    }
}
