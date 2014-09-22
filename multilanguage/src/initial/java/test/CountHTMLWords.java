package es.inteco.multilanguage.test;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.multilanguage.logging.Logger;
import es.inteco.multilanguage.properties.PropertiesManager;
import es.inteco.multilanguage.service.utils.MultilanguageUtils;
import es.inteco.multilanguage.service.utils.StringUtils;
import org.apache.xerces.parsers.DOMParser;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountHTMLWords {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        File dir = new File("/home/veronicademata/Escritorio/sources/");
        String[] dirArray = dir.list();
        BigDecimal numPalTotal = new BigDecimal(0);
        int count = 0;
        for (int i = 0; i < dirArray.length; i++) {
            try {
                FileInputStream fstream = new FileInputStream("/home/veronicademata/Escritorio/sources/" + dirArray[i]);
                String content = StringUtils.getContentAsString(fstream);
                String contenido = getUrlContent(content);
                int numPal = countWords(contenido);
                numPalTotal = numPalTotal.add(new BigDecimal(numPal));
                System.out.println(count++ + "-" + dirArray[i] + " : " + numPal);
                System.out.println("MEDIA: " + numPalTotal.divide(new BigDecimal(count), 0, BigDecimal.ROUND_HALF_UP));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Fallo: " + dirArray[i]);
            }
        }
        System.out.println("Número de palabras: " + numPalTotal);
        System.out.println("Número de páginas: " + count);
        System.out.println("MEDIA: " + numPalTotal.divide(new BigDecimal(count)));
    }

    private static int countWords(String contenido) {
        String[] words = contenido.split("[^a-zA-Z_0-9áéíóúÁÉÍÓÚñÑ]");
        int count = 0;
        for (int i = 0; i < words.length; i++) {
            if (!words[i].trim().equals("")) {
                count++;
            }
        }
        return count;
    }

    private static String getUrlContent(String contenido) throws SAXException, IOException {
        InputSource inputSource = new InputSource(new StringReader(contenido));
        DOMParser parser = new DOMParser(new HTMLConfiguration());
        parser.parse(inputSource);

        Document document = parser.getDocument();
        return getTextFromDOM(document);
    }


    private static String getTextFromDOM(Document document) {
        StringBuffer result = new StringBuffer();
        try {
            PropertiesManager pmgr = new PropertiesManager();

            List<String> ignoredTags = Arrays.asList(pmgr.getValue("multilanguage.properties", "ignored.tags").split(";"));
            List<Node> nodes = new ArrayList<Node>();
            nodes = generateTextNodeList(document.getDocumentElement(), nodes, Integer.MAX_VALUE, ignoredTags);

            List<String> attributes = Arrays.asList(pmgr.getValue("multilanguage.properties", "text.attributes").split(";"));

            for (Node node : nodes) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    for (String attribute : attributes) {
                        if (element.hasAttribute(attribute)) {
                            result.append(element.getAttribute(attribute).trim() + ".\n");
                        }
                    }
                    if (StringUtils.isNotEmpty(element.getTextContent())) {
                        NodeList children = element.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) {
                            if (children.item(i).getNodeType() == Node.TEXT_NODE) {
                                result.append(children.item(i).getTextContent() + " ");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al generar el texto del documento a partir de su árbol DOM", MultilanguageUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return HTMLEntities.unhtmlentities(result.toString());
    }

    private static List<Node> generateTextNodeList(Node node, List<Node> nodeList, int maxNumElements, List<String> ignoredTags) {
        if ((node != null) && (nodeList.size() <= maxNumElements)) {
            if ((((node.getNodeType() == Node.ELEMENT_NODE) || (node.getNodeType() == Node.DOCUMENT_NODE) ||
                    (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) || node.getNodeType() == Node.TEXT_NODE))) {
                for (int x = 0; x < node.getChildNodes().getLength(); x++) {
                    generateTextNodeList(node.getChildNodes().item(x), nodeList, maxNumElements, ignoredTags);
                }
                if (node.getNodeType() == Node.TEXT_NODE || (node.getNodeType() == Node.ELEMENT_NODE && !ignoredTags.contains(node.getNodeName().toUpperCase()))) {
                    nodeList.add(node);
                }
            }
        }
        return nodeList;
    }
}
