package ca.utoronto.atrc.tile.accessibilitychecker;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Clase para generar una expresión XPath de un nodo.
 */
public class XPathGenerator {

    /**
     * Obtiene la expresión XPath que identifica al nodo (Node).
     *
     * @param node el nodo del que obtener la expresión XPath.
     * @return una cadena que representa la expresión xPath que identifica al nodo.
     */
    public String getXpath(final Node node) {
        String expression;
        String attribute = "";

        if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
            attribute = "/@" + node.getNodeName();
            expression = ((Attr) node).getOwnerElement().getNodeName();
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            expression = "text()";
        } else {
            expression = node.getNodeName();
        }

        String suffix = getXpathSuffix(node);
        expression = expression.concat(suffix);
        expression = expression.concat(attribute);

        String fullExpression = "/" + getXpathLoop(node, expression);
        return fullExpression.toLowerCase();
    }

    // Recursive function that creates the Xpath expression
    private String getXpathLoop(final Node node, final String expression) {
        final Node nodeParent = node.getParentNode();
        if (nodeParent == null || nodeParent.getNodeType() == Node.DOCUMENT_NODE) {
            return expression;
        }

        String stringParentName;
        if (nodeParent.getNodeType() == Node.TEXT_NODE) {
            stringParentName = "text()";
        } else {
            stringParentName = nodeParent.getNodeName();
        }
        stringParentName = stringParentName.concat(getXpathSuffix(nodeParent));

        String newExpression = stringParentName + "/" + expression;
        return getXpathLoop(nodeParent, newExpression);
    }

    private String getXpathSuffix(final Node node) {
        final Node nodeParent = node.getParentNode();
        if (nodeParent == null) {
            return "";
        }

        if ("html".equalsIgnoreCase(node.getNodeName())) {
            return "";
        }

        final NodeList childNodes = nodeParent.getChildNodes();
        int count = 0; // number of elements that are the same as the given element
        int index = 1; // the index of the given element amongst all same elements
        for (int x = 0; x < childNodes.getLength(); x++) {
            if (childNodes.item(x) == node) {
                index = count + 1;
                count++;
            } else if (childNodes.item(x).getNodeName().compareToIgnoreCase(node.getNodeName()) == 0) {
                count++;
            }
        }

        if (count <= 1) {
            return "";
        }

        return String.format("[%s]", Integer.toString(index));
    }
}
