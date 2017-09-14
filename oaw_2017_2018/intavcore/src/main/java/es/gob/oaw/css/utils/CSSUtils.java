package es.gob.oaw.css.utils;

import ca.utoronto.atrc.tile.accessibilitychecker.Check;
import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Problem;
import es.gob.oaw.css.CSSAnalyzer;
import es.gob.oaw.css.CSSProblem;
import es.gob.oaw.css.CSSResource;
import es.gob.oaw.css.checks.*;
import es.inteco.common.CheckFunctionConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.ccil.cowan.tagsoup.Schema;
import org.ccil.cowan.tagsoup.XMLWriter;
import org.dom4j.DocumentHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

/**
 * Clase con métodos de utilidad para evaluar las comprobaciones de CSS
 */
public final class CSSUtils {

    private CSSUtils() {
    }

    /**
     * Método que evalua un check de CSS
     *
     * @param rootElement  elemento raíz del documento
     * @param checkCode    parámetros de la comprobación (función y parámetros de configuración) @see Checkcode
     * @param cssResources lista con los recursos CSS que incluye la página @see CSSResource
     * @return una lista de los problemas encontrados @see CSSProblem
     */
    public static List<CSSProblem> evaluate(final Element rootElement, final CheckCode checkCode, final List<CSSResource> cssResources) {
        try {
            final CSSAnalyzer cssAnalyzer = getCSSAnalyzer(rootElement, checkCode);
            return cssAnalyzer.evaluate(convertToDom4j(rootElement), cssResources);
        } catch (Exception e) {
            Logger.putLog("No se ha podido instanciar CSSAnalyzer", CSSUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return Collections.emptyList();
    }

    private static org.dom4j.Document convertToDom4j(final Element element) {
        final org.dom4j.Document dom4jDocument;
        if (element != null) {
            try {
                final String html = convertDocumentToString(element);
                final String xhtml = cleanHTML(html);

                dom4jDocument = DocumentHelper.parseText(xhtml);
            } catch (Exception e) {
                Logger.putLog("No se ha podido convertir el documento a dom4j", CSSUtils.class, Logger.LOG_LEVEL_WARNING, e);
                return null;
            }
        } else {
            dom4jDocument = null;
        }
        return dom4jDocument;
    }

    private static String cleanHTML(final String html) {
        final Schema theSchema = new HTMLSchema();
        final XMLReader xmlReader = new Parser();
        try {
            xmlReader.setProperty(Parser.schemaProperty, theSchema);
            xmlReader.setFeature(Parser.defaultAttributesFeature, false);
            xmlReader.setFeature(Parser.ignorableWhitespaceFeature, true);
        } catch (Exception e) {
            Logger.putLog("No se ha podido configurar el parseador de xml de tagsoup", CSSUtils.class, Logger.LOG_LEVEL_WARNING, e);
        }

        final StringWriter writer = new StringWriter();
        final XMLWriter xmlWriter = new XMLWriter(writer);
        xmlWriter.setOutputProperty(XMLWriter.ENCODING, "utf-8");
        xmlReader.setContentHandler(xmlWriter);

        final InputSource inputSource = new InputSource();
        final Reader reader = new StringReader(html);
        inputSource.setCharacterStream(reader);
        try {
            xmlReader.parse(inputSource);
        } catch (Exception e) {
            Logger.putLog("No se ha podido parsear mediante tagsoup", CSSUtils.class, Logger.LOG_LEVEL_WARNING, e);
        }
        // Se elimina el namespace porque si no da problemas la expresión XPATH
        return writer.toString().trim().replace("xmlns=\"http://www.w3.org/1999/xhtml\"", "");
    }

    private static String convertDocumentToString(final Element element) {
        try {
            final TransformerFactory transFactory = TransformerFactory.newInstance();
            final Transformer transformer = transFactory.newTransformer();
            final StringWriter buffer = new StringWriter();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(element), new StreamResult(buffer));
            return buffer.toString();
        } catch (TransformerException e) {
            Logger.putLog("No se ha podido convertir el document a cadena", CSSUtils.class, Logger.LOG_LEVEL_WARNING, e);
        }
        return "";
    }

    /**
     * Método para obtener la instancia encargada de realizar la comprobación
     *
     * @param node nodo sobre el que se está aplicando la comprobación (debería ser siempre el nodo raíz HTML)
     * @param checkCode objeto CheckCode con la información de la comprobación
     * @return una instancia de CSSAnalyzer que evaluará una comprobación de CSS
     * @throws InstantiationException
     */
    private static CSSAnalyzer getCSSAnalyzer(final Node node, final CheckCode checkCode) throws InstantiationException {
        switch (checkCode.getFunctionId()) {
            case CheckFunctionConstants.FUNCTION_CSS_GENERATED_CONTENT:
                return new CSSGeneratedContentDocumentHandler(!checkCode.getFunctionNumber().isEmpty()?Integer.parseInt(checkCode.getFunctionNumber()):1);
            case CheckFunctionConstants.FUNCTION_CSS_COLOR_CONTRAST:
                return new CSSColorContrastDocumentHandler();
            case CheckFunctionConstants.FUNCTION_CSS_BLINK:
                return new CSSBlinkDocumentHandler();
            case CheckFunctionConstants.FUNCTION_CSS_PARSEABLE:
                return new CSSParseableDocumentHandler();
            case CheckFunctionConstants.FUNCTION_CSS_OUTLINE:
                return new CSSOutlineDocumentHandler();
            case CheckFunctionConstants.FUNCTION_CSS_LABEL_HIDDEN:
                return new CSSLabelHiddenStyleParser(node.getOwnerDocument());
            default:
                Logger.putLog("Warning: unknown function id: " + checkCode.getFunctionId(), CSSUtils.class, Logger.LOG_LEVEL_WARNING);
                throw new InstantiationException("Error: unknown CSS function id: " + checkCode.getFunctionId());
        }
    }

    /**
     * Método que transforma un problema de CSS (CSSProblem) a un problema de accesibilidad (Problem)
     *
     * @param cssProblem un problema encontrado en una CSS
     * @param check      comprobacion Check asociada al problema
     * @param evaluation evaluación Evaluation que se está realizando
     * @return un problema Problem
     */
    public static Problem getProblemFromCSS(final CSSProblem cssProblem, final Check check, final Evaluation evaluation) {
        final PropertiesManager properties = new PropertiesManager();
        final SimpleDateFormat format = new SimpleDateFormat(properties.getValue("intav.properties", "complet.date.format.ymd"));

        final Problem problem = new Problem();
        problem.setDate(format.format(cssProblem.getDate()));
        problem.setCheck(check);
        problem.setLineNumber(cssProblem.getLineNumber());
        problem.setColumnNumber(cssProblem.getColumnNumber());
        problem.setSummary(false);

        // Diferenciar si hay elemento asignado en CSSProblem y entonces usamos el elemento en caso contrario crear un elemento ficticio
        if (cssProblem.getElement() != null) {
            problem.setNode(cssProblem.getElement());
        } else {
            // Creamos un elemento ficticio
            final Element problemTextNode = evaluation.getHtmlDoc().createElement("problem-text");
            problemTextNode.setTextContent(cssProblem.getTextContent());
            problem.setNode(problemTextNode);
        }
        evaluation.addProblem(problem);

        return problem;
    }

}