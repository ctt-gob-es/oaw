/**
 * Modificación del DOMParser de CyberNeko que utiliza el HTMLConfiguration para INTECO
 */

package es.inteco.cyberneko.html.parsers;

import es.inteco.cyberneko.html.HTMLConfiguration;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XNIException;
import org.cyberneko.html.xercesbridge.XercesBridge;

/**
 * A DOM parser for HTML documents.
 *
 * @author Andy Clark
 * @version $Id: DOMParser.java,v 1.5 2005/02/14 03:56:54 andyc Exp $
 */
public class DOMParser
        /***/
        extends org.apache.xerces.parsers.DOMParser {
    /***
     // NOTE: It would be better to extend from AbstractDOMParser but
     //       most users will find it easier if the API is just like the
     //       Xerces DOM parser. By extending directly from DOMParser,
     //       users can register SAX error handlers, entity resolvers,
     //       and the like. -Ac
     extends org.apache.xerces.parsers.AbstractDOMParser {
     /***/

    //
    // Constructors
    //

    /**
     * Default constructor.
     */
    public DOMParser() {
        super(new HTMLConfiguration());
        /*** extending DOMParser ***/
        try {
            setProperty("http://apache.org/xml/properties/dom/document-class-name",
                    "org.apache.html.dom.HTMLDocumentImpl");
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not recognized");
        } catch (org.xml.sax.SAXNotSupportedException e) {
            throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not supported");
        }
        /*** extending AbstractDOMParser ***
         fConfiguration.setProperty("http://apache.org/xml/properties/dom/document-class-name",
         "org.apache.html.dom.HTMLDocumentImpl");
         /***/
    } // <init>()

    public DOMParser(HTMLConfiguration htmlConfiguration) {
        super(htmlConfiguration);
        try {
            setProperty("http://apache.org/xml/properties/dom/document-class-name",
                    "org.apache.html.dom.HTMLDocumentImpl");
        } catch (org.xml.sax.SAXNotRecognizedException e) {
            throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not recognized");
        } catch (org.xml.sax.SAXNotSupportedException e) {
            throw new RuntimeException("http://apache.org/xml/properties/dom/document-class-name property not supported");
        }
    }

    //
    // XMLDocumentHandler methods
    //

    /**
     * Doctype declaration.
     */
    public void doctypeDecl(String root, String pubid, String sysid, Augmentations augs) throws XNIException {

        // NOTE: Xerces HTML DOM implementation (up to and including
        //       2.5.0) throws a heirarchy request error exception 
        //       when a doctype node is appended to the tree. So, 
        //       don't insert this node into the tree for those 
        //       versions... -Ac

        final String version = XercesBridge.getInstance().getVersion();
        boolean okay = true;
        if (version.startsWith("Xerces-J 2.")) {
            okay = getParserSubVersion() > 5;
        }
        // REVISIT: As soon as XML4J is updated with the latest code
        //          from Xerces, then this needs to be updated to
        //          check XML4J's version. -Ac
        else if (version.startsWith("XML4J")) {
            okay = false;
        }

        // if okay, insert doctype; otherwise, don't risk it
        if (okay) {
            super.doctypeDecl(root, pubid, sysid, augs);
        }

    } // doctypeDecl(String,String,String,Augmentations)

    //
    // Private static methods
    //

    /**
     * Returns the parser's sub-version number.
     */
    private static int getParserSubVersion() {
        try {
            final String version = XercesBridge.getInstance().getVersion();
            int index1 = version.indexOf('.') + 1;
            int index2 = version.indexOf('.', index1);
            if (index2 == -1) {
                index2 = version.length();
            }
            return Integer.parseInt(version.substring(index1, index2));
        } catch (Exception e) {
            return -1;
        }
    } // getParserSubVersion():int

} // class DOMParser
