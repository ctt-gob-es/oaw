package es.gob.oaw.utils;

import es.inteco.common.IntavConstants;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase con métodos de ayuda para las comprobaciones relacionadas con la sección de accesibilidad
 */
public final class AccesibilityDeclarationCheckUtils {

    private AccesibilityDeclarationCheckUtils() {
    }

    /**
     * Filtra una lista de enlaces que pueden ser a la página de declaración de accesibilidad.
     *
     * @param links         lista de nodos que representan enlaces.
     * @param sectionRegExp patrones que pueden indicar que el destino es la página de accesibilidad.
     * @return una lista de enlaces que pueden ser a la página de declaración de accesibilidad.
     */
    public static List<Element> getSectionLink(final NodeList links, final String sectionRegExp) {
        final Set<String> includedLinks = new HashSet<>();
        final List<Element> linksFound = new ArrayList<>();
        for (int i = 0; i < links.getLength(); i++) {
            final Element link = (Element) links.item(i);
            final String href = link.getAttribute("href").toLowerCase();
            if (link.hasAttribute("href") && !link.getAttribute("href").toLowerCase().startsWith("javascript") && !link.getAttribute("href").toLowerCase().startsWith("mailto")) {
                if (StringUtils.isNotEmpty(link.getTextContent()) && StringUtils.textMatchs(link.getTextContent().trim(), sectionRegExp) && includedLinks.add(href)) {
                    linksFound.add(link);
                }

                if (link.hasAttribute("title") && StringUtils.textMatchs(link.getAttribute("title").trim(), sectionRegExp) && includedLinks.add(href)) {
                    linksFound.add(link);
                }

                final NodeList imgs = link.getElementsByTagName("img");
                for (int j = 0; j < imgs.getLength(); j++) {
                    final Element img = (Element) imgs.item(j);
                    if (img.hasAttribute("alt") && StringUtils.textMatchs(img.getAttribute("alt").trim(), sectionRegExp) && includedLinks.add(href)) {
                        linksFound.add(link);
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
        final List<Element> links = EvaluatorUtils.getElementsByTagName(document, "a");
        for (Element link : links) {
            final String linkText = link.getTextContent().toLowerCase().trim();
            final String linkTitle = link.getAttribute("title").toLowerCase().trim();
            for (String contactText : contactTexts) {
                if (linkText.contains(contactText) || linkTitle.contains(contactText)) {
                    return true;
                }
            }
        }
        
        //TODO 2017 Texto de contacto en formularios de la página
        final List<Element> forms = EvaluatorUtils.getElementsByTagName(document, "form");
        
        // TODO 2017 Texto de contacto en label de formularios
        
        // TODO 2017 Texto de contacto en botones de formularios
        
        // TODO 2017 Texto de contacto en fieldset
        

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
                if (isLevelConformancePattern(alt)) {
                    return true;
                }
            }
        }

        final String text = getDocumentText(document);

        return isLevelConformancePattern(text);

    }

    /**
     * Comprueba si un texto se ajusta a los patrones habituales (nivel A, nivel doble A...) de definición del nivel de conformidad de accesibilidad.
     *
     * @param text el texto a comprobar.
     * @return true si se ha detectado un patrón o false en caso contrario.
     */
    private static boolean isLevelConformancePattern(final String text) {
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

    /**
     * Obtiene el texto de un documento DOM.
     *
     * @param document documento.
     * @return una cadena que contiene el texto del documento.
     */
    private static String getDocumentText(final Document document) {
        final List<Node> nodeList = EvaluatorUtils.generateNodeList(EvaluatorUtils.getHtmlElement(document), new ArrayList<Node>(), IntavConstants.ALL_ELEMENTS);
        final StringBuilder documentText = new StringBuilder();
        for (Node node : nodeList) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                documentText.append(StringUtils.normalizeWhiteSpaces(node.getTextContent().trim()));
                documentText.append(" ");
            }
        }
        return documentText.toString();
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
            Pattern.compile("\\bnivell?\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bwcag\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\baccesibilidad\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bprioridad\\s+1\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformi(dad|tat)\\s+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bA\\s+\\(?simple\\s+A\\)?\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
    };

    private static final Pattern[] ALT_AA = new Pattern[]{
            Pattern.compile("\\blevel\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\blevel\\s+double(\\s+|-)a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivell?\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivell?\\s+doble(\\s+|-)a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bwcag\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\baccesibilidad\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bprioridad\\s+2\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformi(dad|tat)\\s+aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformi(dad|tat)\\s+.?doble(-|\\s+)a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bAA\\s+\\(?doble\\s+A\\)?\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
    };

    private static final Pattern[] ALT_AAA = new Pattern[]{
            Pattern.compile("\\blevel\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\blevel\\s+triple(\\s+|-)a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivell?\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bnivell?\\s+triple(\\s|-)+a\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bwcag\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\baccesibilidad\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bprioridad\\s+3\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformi(dad|tat)\\s+aaa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bconformi(dad|tat)\\s+.?triple(-|\\s+)aa\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
            Pattern.compile("\\bAAA\\s+\\(?triple\\s+A\\)?\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE),
    };
}
