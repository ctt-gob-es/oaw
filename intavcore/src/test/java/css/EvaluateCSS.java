package css;

import ca.utoronto.atrc.tile.accessibilitychecker.CheckCode;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.*;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.writer.CSSWriterSettings;
import cz.vutbr.web.css.*;
import cz.vutbr.web.domassign.Analyzer;
import cz.vutbr.web.domassign.MultiMap;
import cz.vutbr.web.domassign.StyleMap;
import es.ctic.css.CSSProblem;
import es.ctic.css.checks.CSSPropertyValueDocumentHandler;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.helpers.ParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EvaluateCSS {

    @Test
    public void testCSS() {
        final String css = ".main { font-size: 14px}";
        final CascadingStyleSheet cascadingStyleSheet = CSSReader.readFromString(css, ECSSVersion.CSS30);
        Assert.assertNotNull(cascadingStyleSheet);
        for (CSSStyleRule cssStyleRule : cascadingStyleSheet.getAllStyleRules()) {
            System.out.print(cssStyleRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 1));
            for (CSSDeclaration cssDeclaration : cssStyleRule.getAllDeclarations()) {
                System.out.print(cssDeclaration.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 1));
            }
        }
    }

    @Test
    public void testInvalidCSS() {
        final String css = ".main { font-size: 14px; .sidebar { font-size: 12px}";
        Assert.assertFalse(CSSReader.isValidCSS(css, ECSSVersion.CSS30));
        final CascadingStyleSheet cascadingStyleSheet = CSSReader.readFromString(css, ECSSVersion.CSS30);
        Assert.assertNull(cascadingStyleSheet);
    }

    @Test
    public void testInlineCSS() {
        final String css = "font-size: 14px";
        final CSSDeclarationList cssDeclarationList = CSSReaderDeclarationList.readFromString(css, ECSSVersion.CSS30);
        Assert.assertNotNull(cssDeclarationList);
        for (CSSDeclaration cssDeclaration : cssDeclarationList.getAllDeclarations()) {
            System.out.print(cssDeclaration.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 1));
        }
    }

    @Test
    public void testInlineInvalidCSS() {
        final String css = "fontsize 14px ";
        Assert.assertFalse(CSSReaderDeclarationList.isValidCSS(css, ECSSVersion.CSS30));
        final CSSDeclarationList cssDeclarationList = CSSReaderDeclarationList.readFromString(css, ECSSVersion.CSS30);
        Assert.assertNull(cssDeclarationList);
    }

    @Test
    public void testSACCSS() throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        final String css = ".main:before {background-color: rgba(255, 255, 255, 0.85); font-size: 14px; content: \"Lorem: \";}";
        System.setProperty("org.w3c.css.sac.parser", "com.steadystate.css.parser.SACParserCSS3");

        ParserFactory pf = new ParserFactory();
        Parser parser = pf.makeParser();
        Assert.assertNotNull(parser);
        CheckCode checkCode = new CheckCode();
        checkCode.setFunctionValue("content");
        parser.setDocumentHandler(new CSSPropertyValueDocumentHandler(checkCode));
        final InputSource is = new InputSource();
        //is.setURI("http://www.fundacionctic.org/sites/all/themes/ctic/css/html-reset.css");
        is.setCharacterStream(new java.io.StringReader(css));
        //parser.parseStyleDeclaration(is);
        parser.parseStyleSheet(is);
    }

    @Test
    public void testjStyleParser() throws MalformedURLException {
        final CheckAccessibility checkAccessibility = new CheckAccessibility();
        checkAccessibility.setEntity("Tests unitarios");
        checkAccessibility.setGuideline("observatorio-2.0");
        checkAccessibility.setGuidelineFile("observatorio-2.0.xml");
        checkAccessibility.setLevel("aa");
        checkAccessibility.setUrl("http://www.fundacionctic.org");
        checkAccessibility.setIdRastreo(0); // 0 - Indica análisis suelto (sin crawling y sin guardar datos en la BD de observatorio)
        checkAccessibility.setWebService(false);
        //get the element style
        Document doc = EvaluatorUtility.loadHtmlFile(checkAccessibility, false, false, "es", false);
        StyleMap map = CSSFactory.assignDOM(doc, "UTF-8", new URL("http://www.fundacionctic.org"), new MediaSpecAll(), true);
        final NodeList paragraphs = doc.getElementsByTagName("h2");
        for (int i = 0; i < paragraphs.getLength(); i++) {
            NodeData style = map.get((Element) paragraphs.item(i));
            //get the type of the assigned value
            CSSProperty.Color color = style.getProperty("color",true);
            if (CSSProperty.Color.INHERIT == color) {
                System.out.println("color=inherit");
            } else if (CSSProperty.Color.TRANSPARENT == color) {
                System.out.println("color=transparent");
            } else if (CSSProperty.Color.color == color) {
                System.out.println("color= " + style.getValue(TermColor.class, "color").toString());
            } else {
                System.out.println("## unknown ##");
            }

            CSSProperty.FontSize font = style.getProperty("font-size",true);
            if (CSSProperty.FontSize.length == font) {
                System.out.println("font-size= " + style.getValue(TermLength.class, "font-size").toString());
            }

            CSSProperty.BackgroundColor backgroundColor = style.getProperty("background-color",true);
            if (CSSProperty.BackgroundColor.color == backgroundColor) {
                System.out.println("background-color= " + style.getValue(TermColor.class, "background-color").toString());
            }

//            CSSProperty backgroundColor = style.getProperty("font-size",true);
//            if (CSSProperty.Color.INHERIT == backgroundColor) {
//                System.out.println("background-color=inherit");
//            }  else if (CSSProperty.Color.TRANSPARENT == backgroundColor) {
//                System.out.println("background-color=transparent");
//            } else if (CSSProperty.Color.color == backgroundColor) {
//                System.out.println("background-color= " + style.getValue(TermColor.class, "background-color").toString());
//            }  else {
//                System.out.println("## unknown ##"+ backgroundColor.toString());
//            }
//            System.out.println(style);
        }
    }

    @Test
    public void testjStyleParserLabel() throws MalformedURLException {
        final CheckAccessibility checkAccessibility = new CheckAccessibility();
        checkAccessibility.setEntity("Tests unitarios");
        checkAccessibility.setGuideline("observatorio-2.0");
        checkAccessibility.setGuidelineFile("observatorio-2.0.xml");
        checkAccessibility.setLevel("aa");
        checkAccessibility.setUrl("http://www.fundacionctic.org/contacto");
        checkAccessibility.setIdRastreo(0); // 0 - Indica análisis suelto (sin crawling y sin guardar datos en la BD de observatorio)
        checkAccessibility.setWebService(false);
        //get the element style
        Document doc = EvaluatorUtility.loadHtmlFile(checkAccessibility, false, false, "es", false);
        StyleMap map = CSSFactory.assignDOM(doc, "UTF-8", new URL("http://www.fundacionctic.org"), new MediaSpecAll(), true);

        final NodeList paragraphs = doc.getElementsByTagName("label");
        for (int i = 0; i < paragraphs.getLength(); i++) {
            NodeData style = map.get((Element) paragraphs.item(i));
            System.out.println(style.getSourceDeclaration("left").getSource().toString());
            //get the type of the assigned value
            CSSProperty.Color color = style.getProperty("color",true);
            if (CSSProperty.Color.INHERIT == color) {
                System.out.println("color=inherit");
            } else if (CSSProperty.Color.TRANSPARENT == color) {
                System.out.println("color=transparent");
            } else if (CSSProperty.Color.color == color) {
                System.out.println("color= " + style.getValue(TermColor.class, "color").toString());
            } else {
                System.out.println("## unknown ##");
            }

            CSSProperty.FontSize font = style.getProperty("font-size",true);
            if (CSSProperty.FontSize.length == font) {
                System.out.println("font-size= " + style.getValue(TermLength.class, "font-size").toString());
            }

            CSSProperty.BackgroundColor backgroundColor = style.getProperty("background-color",true);
            if (CSSProperty.BackgroundColor.color == backgroundColor) {
                System.out.println("background-color= " + style.getValue(TermColor.class, "background-color").toString());
            }
            System.out.println(style);
        }
    }

    private static List<CSSProblem> functionCSSGeneratedContent(CheckCode checkCode, Node node) {
        final List<CSSProblem> cssProblems = new ArrayList<CSSProblem>();
        final NodeList linkedStyleSheets = node.getOwnerDocument().getDocumentElement().getElementsByTagName("link");
        if (linkedStyleSheets != null && linkedStyleSheets.getLength() > 0) {
            for (int i = 0; i < linkedStyleSheets.getLength(); i++) {
                System.out.println("Existen linkedStyleSheets pero no está implementado el proceso");
                final String href = ((Element) linkedStyleSheets.item(i)).getAttribute("href");
                System.out.println(href);
                try {
                    HttpURLConnection connection = EvaluatorUtils.getConnection(href, "GET", true);
                    connection.connect();
                    final String cssContent;
                    int responseCode = connection.getResponseCode();
                    if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                        cssContent = StringUtils.getContentAsString(connection.getInputStream());
                    } else {
                        continue;
                    }
                    connection.disconnect();
                    final CascadingStyleSheet cascadingStyleSheet = CSSReader.readFromString(cssContent, ECSSVersion.CSS30);
                    if (cascadingStyleSheet != null) {
                        for (CSSStyleRule cssStyleRule : cascadingStyleSheet.getAllStyleRules()) {
                            for (CSSSelector cssSelector : cssStyleRule.getAllSelectors()) {
                                for (ICSSSelectorMember icssSelectorMember : cssSelector.getAllMembers()) {
                                    if (icssSelectorMember instanceof CSSSelectorSimpleMember) {
                                        final CSSSelectorSimpleMember cssSelectorSimpleMember = (CSSSelectorSimpleMember) icssSelectorMember;
                                        if (cssSelectorSimpleMember.isPseudo()) {
                                            if (cssSelectorSimpleMember.getValue().equalsIgnoreCase(":before") || cssSelectorSimpleMember.getValue().equalsIgnoreCase(":after")) {
                                                for (CSSDeclaration cssDeclaration : cssStyleRule.getAllDeclarations()) {
                                                    if ("content".equalsIgnoreCase(cssDeclaration.getProperty())) {
                                                        final String expression = cssDeclaration.getExpression().getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                                                        // La expression va entrecomillada porque el valor tiene que ser una cadena por lo que comprobamos la longitud contra +2 (los dos caracteres de comillas)
                                                        final int allowedNumChars = Integer.parseInt(checkCode.getFunctionNumber()) + 2;
                                                        if (expression.length() >= allowedNumChars) {
                                                            // TODO: Crear una incidencia con los datos necesarios
                                                            // y añadirla a la lista de errores
                                                            cssProblems.add(null);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("ERROR al parsear la CSS");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        final NodeList embeddedStyleSheets = node.getOwnerDocument().getDocumentElement().getElementsByTagName("style");
        if (embeddedStyleSheets != null && embeddedStyleSheets.getLength() > 0) {
            for (int i = 0; i < embeddedStyleSheets.getLength(); i++) {
                final CascadingStyleSheet cascadingStyleSheet = CSSReader.readFromString(embeddedStyleSheets.item(i).getTextContent(), ECSSVersion.CSS30);
                if (cascadingStyleSheet != null) {
                    for (CSSStyleRule cssStyleRule : cascadingStyleSheet.getAllStyleRules()) {
                        for (CSSSelector cssSelector : cssStyleRule.getAllSelectors()) {
                            for (ICSSSelectorMember icssSelectorMember : cssSelector.getAllMembers()) {
                                if (icssSelectorMember instanceof CSSSelectorSimpleMember) {
                                    final CSSSelectorSimpleMember cssSelectorSimpleMember = (CSSSelectorSimpleMember) icssSelectorMember;
                                    if (cssSelectorSimpleMember.isPseudo()) {
                                        if (cssSelectorSimpleMember.getValue().equalsIgnoreCase(":before") || cssSelectorSimpleMember.getValue().equalsIgnoreCase(":after")) {
                                            for (CSSDeclaration cssDeclaration : cssStyleRule.getAllDeclarations()) {
                                                if ("content".equalsIgnoreCase(cssDeclaration.getProperty())) {
                                                    final String expression = cssDeclaration.getExpression().getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                                                    // La expression va entrecomillada porque el valor tiene que ser una cadena por lo que comprobamos la longitud contra +2 (los dos caracteres de comillas)
                                                    final int allowedNumChars = Integer.parseInt(checkCode.getFunctionNumber()) + 2;
                                                    if (expression.length() >= allowedNumChars) {
                                                        // TODO: Crear una incidencia con los datos necesarios
                                                        // y añadirla a la lista de errores
                                                        cssProblems.add(null);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("ERROR al parsear la CSS");
                }
            }
        }
        return cssProblems;
    }


    @Test
    public void testjStyleParserConcept() throws MalformedURLException {
        final CheckAccessibility checkAccessibility = new CheckAccessibility();
        checkAccessibility.setEntity("Tests unitarios");
        checkAccessibility.setGuideline("observatorio-2.0");
        checkAccessibility.setGuidelineFile("observatorio-2.0.xml");
        checkAccessibility.setLevel("aa");
        checkAccessibility.setUrl("http://www2.fundacionctic.org");
        checkAccessibility.setIdRastreo(0); // 0 - Indica análisis suelto (sin crawling y sin guardar datos en la BD de observatorio)
        checkAccessibility.setWebService(false);
        //get the element style
        Document doc = EvaluatorUtility.loadHtmlFile(checkAccessibility, false, false, "es", false);

        final StyleSheet styleSheet = CSSFactory.getUsedStyles(doc, "utf-8", new URL("http://www2.fundacionctic.org"), new MediaSpecAll());

        final Analyzer analyzer = new Analyzer(styleSheet);
        final StyleMap styleMap = analyzer.evaluateDOM(doc, new MediaSpecAll(), true);
        /*final Element mainDiv = doc.getElementById("main");
        final NodeData nodeData = styleMap.get(mainDiv);
        System.out.println(Arrays.toString(nodeData.getPropertyNames().toArray()));
        for(String propertyName: nodeData.getPropertyNames()) {
            System.out.println(propertyName+": "+ nodeData.getProperty(propertyName));
        }*/

        final NodeList h3Elements = doc.getElementsByTagName("h3");
        for (int i = 0; i < h3Elements.getLength(); i++) {
            System.out.println("////////////////////");
            Element h3Element = (Element) h3Elements.item(i);
            final NodeData nodeData = styleMap.get(h3Element);
            for (String propertyName : nodeData.getPropertyNames()) {
                CSSProperty cssProperty = nodeData.getProperty(propertyName);
                if (cssProperty == CSSProperty.FontSize.length) {
                    Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);

                    TermLength mtop = nodeData.getValue(TermLength.class, propertyName, true);
                    System.out.println("FONT-SIZE: " + mtop);
                } else if (cssProperty == CSSProperty.Color.color) {
                    Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);

                    TermColor mtop = nodeData.getValue(TermColor.class, propertyName, true);
                    System.out.println("Color: " + mtop);
                } else if (cssProperty == CSSProperty.BackgroundColor.color) {
                    Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);

                    TermColor mtop = nodeData.getValue(TermColor.class, propertyName, true);
                    System.out.println(propertyName + ": " + mtop);
                }
            }
        }

        System.out.println("PORTALES H3");
        final Element portalesDiv = doc.getElementById("block-views-portales-home-subdestacados-es");
        final Element portalesH3 = (Element) portalesDiv.getElementsByTagName("h3").item(0);
        final NodeData nodeData = styleMap.get(portalesH3);
        for (String propertyName : nodeData.getPropertyNames()) {
            CSSProperty cssProperty = nodeData.getProperty(propertyName);
            if (cssProperty == CSSProperty.FontSize.length) {
                Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);

                TermLength mtop = nodeData.getValue(TermLength.class, propertyName, true);
                System.out.println("FONT-SIZE: " + mtop);
            } else if (cssProperty == CSSProperty.Color.color) {
                Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);

                TermColor mtop = nodeData.getValue(TermColor.class, propertyName, true);
                System.out.println("Color: " + mtop);
            } else if (cssProperty == CSSProperty.BackgroundColor.color) {
                Declaration declaration = nodeData.getSourceDeclaration(propertyName, true);

                TermColor mtop = nodeData.getValue(TermColor.class, propertyName, true);
                System.out.println(propertyName + ": " + mtop);
            }
        }
    }
}
