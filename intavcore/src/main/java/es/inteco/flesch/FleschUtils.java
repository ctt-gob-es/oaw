package es.inteco.flesch;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.StringUtils;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FleschUtils {

    public static String getContentFromHtml(String htmlSource) {
        String initContent;
        String content;
        try {
            initContent = HTMLEntities.unhtmlentities(htmlSource).trim();
        } catch (Exception e) {
            Logger.putLog("Error al resolver las entidades HTML del código fuente: " + e.getMessage(), FleschUtils.class, Logger.LOG_LEVEL_INFO);
            initContent = htmlSource;
        }
        content = removeTags(initContent);
        content += getAttributesTextFromDOM(initContent);
        content = filterPhrases(content);
        return content;
    }

    private static String removeTags(String content) {
        content = Pattern.compile("<script[^>]*>(.*?)</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll("");
        content = Pattern.compile("<style[^>]*>(.*?)</style>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll("");
        content = Pattern.compile("(</th>)|(</td>)|(</option>)|(</legend>)|(</h1>)|(</h2>)|(</h3>)|(</h4>)|(</h5>)|(</h6>)" +
                "|(</dt>)|(</dd>)|(</caption>)|(</blockquote>)|(<br>)|(<br/>)|(<br />)|(</li>)|(</p>)|(</div>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(".\n");
        content = Pattern.compile("</{0,1}[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");
        content = Pattern.compile(" {2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(" ");
        content = Pattern.compile("[\\n\\r\\t ]{2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(".\n");
        return content;
    }

    private static String getAttributesTextFromDOM(String content) {
        StringBuilder result = new StringBuilder();
        try {
            InputSource inputSource = new InputSource(new StringReader(content));

            DOMParser parser = new DOMParser(new HTMLConfiguration());
            parser.parse(inputSource);

            Document document = parser.getDocument();

            PropertiesManager pmgr = new PropertiesManager();

            List<Node> nodes = new ArrayList<Node>();
            nodes = generateTextNodeList(document.getDocumentElement(), nodes, Integer.MAX_VALUE);

            List<String> attributes = Arrays.asList(pmgr.getValue("intav.properties", "text.attributes").split(";"));

            for (Node node : nodes) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    for (String attribute : attributes) {
                        if (((Element) node).hasAttribute(attribute)) {
                            result.append(((Element) node).getAttribute(attribute).trim()).append(".\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al generar el texto del documento a partir de su árbol DOM: " + e.getMessage(), FleschUtils.class, Logger.LOG_LEVEL_ERROR);
        }

        return result.toString();
    }

    public static int countWords(String text) {
        String[] words = text.split("[^a-zA-ZáéíóúàèìòùÁÉÍÓÚÀÈÌÒÙñÑ]");

        int count = 0;
        for (String word : words) {
            if (word != null && word.trim().length() > 0) {
                count++;
            }
        }

        return count;
    }

    // Genera recursivamente una lista de nodos del documento
    private static List<Node> generateTextNodeList(Node node, List<Node> nodeList, int maxNumElements) {
        if ((node != null) && (nodeList.size() <= maxNumElements)) {
            if ((((node.getNodeType() == Node.ELEMENT_NODE) || (node.getNodeType() == Node.DOCUMENT_NODE) ||
                    (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) || node.getNodeType() == Node.TEXT_NODE))) {
                for (int x = 0; x < node.getChildNodes().getLength(); x++) {
                    generateTextNodeList(node.getChildNodes().item(x), nodeList, maxNumElements);
                }
                if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.ELEMENT_NODE) {
                    nodeList.add(node);
                }
            }
        }
        return nodeList;
    }

    private static String filterPhrases(String content) {
        StringBuilder filterContent = new StringBuilder();
        PropertiesManager pmgr = new PropertiesManager();
        content = Pattern.compile("[\\.]{2,}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(content).replaceAll(".");
        List<String> contentList = Arrays.asList(content.split(".\n"));
        for (String splittedContent : contentList) {
            if ((StringUtils.isNotEmpty(splittedContent)) && (countWords(splittedContent) >= Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "minimun.phrase.length.for.flesch")))) {
                filterContent.append(splittedContent).append(".\n");
            }
        }
        return filterContent.toString();
    }
}
