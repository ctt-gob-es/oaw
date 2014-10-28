/*
Copyright ©2004, University of Toronto. All rights reserved.

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

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.cyberneko.html.HTMLConfiguration;
import es.inteco.cyberneko.html.parsers.DOMParser;
import org.apache.xerces.xni.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;

public class CheckerParser extends DOMParser {

    private XMLLocator locator;
    private boolean flagDoctype = false;
    private String stringHtmlXhtml = "";
    private String stringHtmlVersion = "";
    private String doctypePublicId = "";
    private String doctypeSystemId = "";
    private int lineDoctype = 0;
    private Node nodeImgWithLongdesc = null;
    private String longdescValue = "";
    private int headingLevel = 0;
    private int dlinkCounter = 0;
    private int noscriptCounter = 0;
    private int noembedCounter = 0;
    private int rowCounter = 0;
    private int cellCounter = 0;
    private int tableCounter = 0;
    private int formCounter = 0;
    private Node nodeScript = null;
    private Node nodeEmbed = null;
    private Node nodePreviousHeader = null;
    /**
     * Map que guarda para cada imagen (atributo src del tag img) sus dimensiones (ancho y alto)
     */
    private Map<String, Dimension> hashtableImages = new Hashtable<String, Dimension>();
    /**
     * Map que guarda el número de veces que aparece un elemento (una etiqueta)
     */
    private Map<String, Integer> hashtableElements = new Hashtable<String, Integer>();
    private String filename = "";
    private String base = "";
    private String formname = "";
    private List<String> vectorIDs = new ArrayList<String>();
    private List<String> vectorFors = new ArrayList<String>();
    private int position = 1;
    private boolean inHeading = false;

    public boolean hasDoctype() {
        return flagDoctype;
    }

    public int getDoctypeLine() {
        return lineDoctype;
    }

    public String getBase() {
        return base;
    }

    public void setFilename(String name) {
        filename = name;
    }

    public String getHtmlXhtml() {
        return stringHtmlXhtml;
    }

    public String getHtmlVersion() {
        return stringHtmlVersion;
    }

    public String getDoctypePublicId() {
        return doctypePublicId;
    }

    public String getDoctypeSystemId() {
        return doctypeSystemId;
    }

    public CheckerParser() {
        super();
        try {
            this.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        } catch (org.xml.sax.SAXException e) {
            Logger.putLog("Exception: ", CheckerParser.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public CheckerParser(HTMLConfiguration htmlConfiguration) {
        super(htmlConfiguration);
        try {
            this.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        } catch (org.xml.sax.SAXException e) {
            Logger.putLog("Exception: ", CheckerParser.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    /*   We override startElement callback  from DocumentHandler */
    public void startElement(QName elementQName, XMLAttributes attrList, Augmentations augs)
            throws XNIException {
        super.startElement(elementQName, attrList, augs);

        Node node = null;
        try {
            node = (Node) this.getProperty("http://apache.org/xml/properties/dom/current-element-node");
        } catch (org.xml.sax.SAXException ex) {
            Logger.putLog("Exception: ", CheckerParser.class, Logger.LOG_LEVEL_ERROR, ex);
        }
        // Logger.putLog(node.getClass().getName(), CheckerParser.class, Logger.LOG_LEVEL_INFO);
        if (node != null) {
            node.setUserData("startLine", String.valueOf(locator.getLineNumber()), null);
            node.setUserData("startColumn", String.valueOf(locator.getColumnNumber()), null);
        }

        // are we looking for a d-link?
        // (We're doing this here before the DOM structure is created because the d-link
        // must follow within 5 elements of the IMG.)

        if (dlinkCounter > 0) {
            if (node.getNodeName().equalsIgnoreCase("a")) {
                if (EvaluatorUtility.getAttributeNoSession((Element) node, "href").equalsIgnoreCase(longdescValue)) {
                    nodeImgWithLongdesc.setUserData("dlink", "true", null);
                    dlinkCounter = 0;
                    nodeImgWithLongdesc = null;
                } else {
                    dlinkCounter--;
                }
            } else {
                dlinkCounter--;
            }
        }

        // are we looking for a NOSCRIPT?
        // (We're doing this here before the DOM structure is created because the NOSCRIPT
        // must follow within 5 elements of the SCRIPT.)
        if (noscriptCounter > 0) {
            if (node.getNodeName().equalsIgnoreCase("noscript")) {
                nodeScript.setUserData("noscript", "true", null);
                noscriptCounter = 0;
                nodeScript = null;
            } else {
                noscriptCounter--;
            }
        }

        // are we looking for a NOEMBED?
        // (We're doing this here before the DOM structure is created because the NOEMBED
        // must follow within 5 elements of the EMBED.)
        if (noembedCounter > 0) {
            if (node.getNodeName().equalsIgnoreCase("noembed")) {
                nodeEmbed.setUserData("noembed", "true", null);
                noembedCounter = 0;
                nodeEmbed = null;
            } else {
                noembedCounter--;
            }
        }

        // check for non-unique ID attribute values
        // (This is done here because it's more efficient.)
        String stringIDvalue = attrList.getValue("id");
        if (stringIDvalue != null) {
            // check the vector to see if this already exists
            boolean bFoundIt = false;
            for (String vectorID : vectorIDs) {
                if (stringIDvalue.equalsIgnoreCase(vectorID)) {
                    node.setUserData("dupeid", "true", null);
                    bFoundIt = true;
                    break;
                }
            }
            if (!bFoundIt) {
                vectorIDs.add(stringIDvalue);
            }
        }

        // count the tables
        if (node.getNodeName().equalsIgnoreCase("table")) {
            tableCounter++;
            rowCounter = 0;
            cellCounter = 0;
        }

        // count the rows
        else if (node.getNodeName().equalsIgnoreCase("tr")) {
            rowCounter++;
        }

        // store the "action" attribute of the current form (needed for "input" elements)
        else if (node.getNodeName().equalsIgnoreCase("form")) {
            formCounter++;
            // the form identifier is the 'action' attribute
            formname = EvaluatorUtility.getAttributeNoSession((Element) node, "action");
            if ((formname == null) || (formname.length() == 0)) {
                // if no 'action' attribute then form name is form counter number
                formname = "form" + formCounter;
            }
        }

        // input element - associate with its form and its position in the document
        else if (node.getNodeName().equalsIgnoreCase("input")) {
            node.setUserData("form", formname, null);
            node.setUserData("pos", position, null);
            position++;
        }

        // select element - associate with its form and its position in the document
        else if (node.getNodeName().equalsIgnoreCase("select")) {
            node.setUserData("form", formname, null);
            node.setUserData("pos", position, null);
            position++;
        }

        // a element - associate with its position in the document
        else if (node.getNodeName().equalsIgnoreCase("a")) {
            node.setUserData("pos", position, null);
            position++;
        }

        // area area - associate with its position in the document
        else if (node.getNodeName().equalsIgnoreCase("area")) {
            node.setUserData("pos", position, null);
            position++;
        }

        // button element - associate with its position in the document
        else if (node.getNodeName().equalsIgnoreCase("button")) {
            node.setUserData("pos", position, null);
            position++;
        }

        // object element - associate with its position in the document
        else if (node.getNodeName().equalsIgnoreCase("object")) {
            node.setUserData("pos", position, null);
            position++;
        }
        // textarea element - associate its position in the document
        else if (node.getNodeName().equalsIgnoreCase("textarea")) {
            node.setUserData("pos", position, null);
            position++;
        }

        // label element - associate with its form
        else if (node.getNodeName().equalsIgnoreCase("label")) {
            node.setUserData("form", formname, null);

            // check for non-unique FOR attribute values
            // (This is done here because it's more efficient.)
            String stringForvalue = attrList.getValue("for");
            if (stringForvalue != null) {
                // check the vector to see if this already exists
                boolean bFoundIt = false;
                for (String vectorFor : vectorFors) {
                    if (stringForvalue.compareToIgnoreCase(vectorFor) == 0) {
                        node.setUserData("dupefor", "true", null);
                        bFoundIt = true;
                        break;
                    }
                }
                if (!bFoundIt) {
                    vectorFors.add(stringForvalue);
                }
            }
        }

        // store the 'base' (needed when loading images)
        else if (node.getNodeName().equalsIgnoreCase("base")) {
            base = EvaluatorUtility.getAttributeNoSession((Element) node, "href");
        }

        // look at heading levels here too
        // store the previous and next heading level on each header
        else if (node.getNodeName().equalsIgnoreCase("h1")) {
            node.setUserData("previouslevel", headingLevel, null);
            headingLevel = 1;
            inHeading = true;
            if (nodePreviousHeader != null) {
                nodePreviousHeader.setUserData("nextlevel", 1, null);
                nodePreviousHeader.setUserData("nextheader", node, null);
            }
            nodePreviousHeader = node;
        } else if (node.getNodeName().equalsIgnoreCase("h2")) {
            node.setUserData("previouslevel", headingLevel, null);
            headingLevel = 2;
            inHeading = true;
            if (nodePreviousHeader != null) {
                nodePreviousHeader.setUserData("nextlevel", 2, null);
                nodePreviousHeader.setUserData("nextheader", node, null);
            }
            nodePreviousHeader = node;
        } else if (node.getNodeName().equalsIgnoreCase("h3")) {
            node.setUserData("previouslevel", headingLevel, null);
            headingLevel = 3;
            inHeading = true;
            if (nodePreviousHeader != null) {
                nodePreviousHeader.setUserData("nextlevel", 3, null);
                nodePreviousHeader.setUserData("nextheader", node, null);
            }
            nodePreviousHeader = node;
        } else if (node.getNodeName().equalsIgnoreCase("h4")) {
            node.setUserData("previouslevel", headingLevel, null);
            headingLevel = 4;
            inHeading = true;
            if (nodePreviousHeader != null) {
                nodePreviousHeader.setUserData("nextlevel", 4, null);
                nodePreviousHeader.setUserData("nextheader", node, null);
            }
            nodePreviousHeader = node;
        } else if (node.getNodeName().equalsIgnoreCase("h5")) {
            node.setUserData("previouslevel", headingLevel, null);
            headingLevel = 5;
            inHeading = true;
            if (nodePreviousHeader != null) {
                nodePreviousHeader.setUserData("nextlevel", 5, null);
                nodePreviousHeader.setUserData("nextheader", node, null);
            }
            nodePreviousHeader = node;
        } else if (node.getNodeName().equalsIgnoreCase("h6")) {
            node.setUserData("previouslevel", headingLevel, null);
            headingLevel = 6;
            inHeading = true;
            if (nodePreviousHeader != null) {
                nodePreviousHeader.setUserData("nextlevel", 6, null);
                nodePreviousHeader.setUserData("nextheader", node, null);
            }
            nodePreviousHeader = node;
        }

        // is this an IMG?
        else if (node.getNodeName().equalsIgnoreCase("img")) {
            parseImg(node);
        }

        // is this an SCRIPT (and requires a NOSCRIPT)?
        else if (node.getNodeName().equalsIgnoreCase("script")) {
            noscriptCounter = 5; // noscript must occur within the next 5 elements
            node.setUserData("noscript", "false", null);
            nodeScript = node;
        }

        // is this an EMBED (and requires a NOEMBED)?
        else if (node.getNodeName().equalsIgnoreCase("embed")) {
            noembedCounter = 5; // noembed must occur within the next 5 elements
            node.setUserData("noembed", "false", null);
            nodeEmbed = node;
        }

        // count each element
        Integer intCount = hashtableElements.get(node.getNodeName());
        if (intCount == null) {
            intCount = 0;
        }
        intCount = intCount + 1;
        hashtableElements.put(node.getNodeName(), intCount);
        node.setUserData("count", intCount, null);

    } //startElement

    private void parseImg(Node node) {
        // does it require a d-link?
        if (((Element) node).getAttribute("longdesc").length() > 0) {
            nodeImgWithLongdesc = node;
            longdescValue = EvaluatorUtility.getAttributeNoSession((Element) node, "longdesc");
            dlinkCounter = 5; // dlink must occur within the next 5 elements
            node.setUserData("dlink", "false", null);
        }

        // get the width and height of the image
        int width = -1;
        int height = -1;

        // look for width and height attributes
        String stringWidth = ((Element) node).getAttribute("width").trim();
        String stringHeight = ((Element) node).getAttribute("height").trim();
        if (stringHeight.length() > 0) {
            try {
                height = Integer.parseInt(stringHeight);
            } catch (Exception e) {
            }
        }
        if (stringWidth.length() > 0) {
            try {
                width = Integer.parseInt(stringWidth);
            } catch (Exception e) {
            }
        }

        // was width and height specified by attributes?
        if ((width == -1) && (height == -1)) {
            // no, have we already loaded this image?
            String stringSrc = ((Element) node).getAttribute("src");
            if (stringSrc.length() > 0) {
                Dimension dimension = hashtableImages.get(stringSrc);
                if (dimension != null) {
                    node.setUserData("dimension", dimension, null);
                }
                // Inicialmente estaba anulada solamente la carga de la imagen y se calculaba la var stringUrl
                // se comenta completamente hasta decidir para que sirve este bloque de código
                else {
                    // don't try to load relative images for local files
                    if (StringUtils.isNotEmpty(filename) && filename.equals("temporary") &&
                            !stringSrc.startsWith("http")) {
                        // IMAGEN LOCAL
                    } else {
                        // image not loaded already so load it
                        try {
                            // is there a 'base' URL for the file?
                            final String stringUrl;
                            if (base.length() > 0) { // yes, image is relative to base
                                stringUrl = EvaluatorUtility.getAbsolute(stringSrc, base);
                            } else { // no, image is relative to filename
                                stringUrl = EvaluatorUtility.getAbsolute(stringSrc, filename);
                            }
                            //URL url = EvaluatorUtility.openUrl(stringUrl);
                            /*if (url == null){
                                // System.out.println ("Can't get URL for image: " + stringUrl + ", source: " + stringSrc);
							} else {*/
                            final BufferedImage image = ImageIO.read(new URL(stringUrl));
                            if (image != null) {
                                dimension = new Dimension(image.getWidth(), image.getHeight());
                                hashtableImages.put(stringSrc, dimension);
                                node.setUserData("dimension", dimension, null);
                            } else {
                                Logger.putLog("Can't get image, buffered image null: " + stringUrl, CheckerParser.class, Logger.LOG_LEVEL_INFO);
                                throw new Exception();
                            }
//							}
                        } catch (Exception e) {
                            // exception will be generated if image size can't be found
                            // System.out.println ("Exception in open URL: " + stringSrc);
                            //Dimension dimensionDefault = new Dimension(200, 200); // default size
                            //node.setUserData("dimension", dimensionDefault, null);
                        }
                    }
                }
            }
        } else {
            // width and height were specified by attributes store width and height on node
            node.setUserData("dimension", new Dimension(width, height), null);
        }
    }

    /* We override startDocument callback from DocumentHandler */
    public void startDocument(XMLLocator locator, String encoding,
                              NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        super.startDocument(locator, encoding, namespaceContext, augs);
        this.locator = locator;
        Node node = null;
        try {
            node = (Node) this.getProperty("http://apache.org/xml/properties/dom/current-element-node");
        } catch (org.xml.sax.SAXException ex) {
            Logger.putLog("Exception: ", CheckerParser.class, Logger.LOG_LEVEL_ERROR, ex);
        }

        if (node != null) {
            node.setUserData("startLine", String.valueOf(locator.getLineNumber()), null);
            node.setUserData("startColumn", String.valueOf(locator.getColumnNumber()), null);
        }

        stringHtmlXhtml = "html";
        stringHtmlVersion = "";
    } //startDocument

    // override doctypeDecl to find out if document has a doctype declaration
    // get the (x)html type and version here too
    public void doctypeDecl(java.lang.String rootElement,
                            java.lang.String publicId,
                            java.lang.String systemId,
                            org.apache.xerces.xni.Augmentations augs)
            throws org.apache.xerces.xni.XNIException {

        // store the systemId
        doctypePublicId = publicId;
        doctypeSystemId = systemId;

        if (publicId == null) {
            super.doctypeDecl(rootElement, publicId, systemId, augs);
            return;
        }

        // is this html or xhtml? and which version?
        boolean bFoundType = false;
        StringTokenizer stringTokenizer = new StringTokenizer(publicId, " ");
        while (stringTokenizer.hasMoreTokens()) {
            String stringTemp = stringTokenizer.nextToken();
            if (bFoundType) {
                stringHtmlVersion = stringTemp;
                break;
            } else {
                if (stringTemp.compareToIgnoreCase("xhtml") == 0) {
                    bFoundType = true;
                    stringHtmlXhtml = "xhtml";
                } else if (stringTemp.compareToIgnoreCase("html") == 0) {
                    bFoundType = true;
                    stringHtmlXhtml = "html";
                }
            }
        }

        super.doctypeDecl(rootElement, publicId, systemId, augs);

        // set the flag
        flagDoctype = true;

        // and line number
        lineDoctype = locator.getLineNumber();
    }

    @Override
    public void endElement(QName qName, Augmentations augmentations) throws XNIException {
        super.endElement(qName, augmentations);
        if (qName.rawname.equals("h1") || qName.rawname.equals("h2")
                || qName.rawname.equals("h3") || qName.rawname.equals("h4")
                || qName.rawname.equals("h5") || qName.rawname.equals("h6")) {
            inHeading = false;
        }
    }

    @Override
    public void characters(XMLString xmlString, Augmentations augmentations) throws XNIException {
        super.characters(xmlString, augmentations);
        if (nodePreviousHeader != null && !inHeading && !xmlString.toString().trim().isEmpty()) {
            nodePreviousHeader.setUserData("headerHasContents", true, null);
        }
    }
}
