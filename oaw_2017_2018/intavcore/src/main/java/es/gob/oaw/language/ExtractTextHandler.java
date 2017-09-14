package es.gob.oaw.language;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

/**
 * Clase para extraer el texto de una página web excluyendo el texto de aquellas etiquetas que definan un idioma distinto al definido a nivel de página web (etiqueta html).
 */
public class ExtractTextHandler extends DefaultHandler {

    private static final String WHITE_SPACE = " ";

    /**
     * Idioma de la página web
     */
    private final String webpageLanguage;

    /**
     * Pila donde se irán guardando los idiomas de las etiquetas compuestas para saber si un determinado texto se debe incluir o excluir
     */
    private final Stack<String> languages;

    /**
     * Variable donde se irá guardando el texto extraido
     */
    private final StringBuilder extractedText;

    private final boolean extractSameLanguage;

    private boolean skipCharacters;

    public ExtractTextHandler(final String language) {
        this(language, true);
    }

    public ExtractTextHandler(final String language, final boolean extractSameLanguage) {
        this.webpageLanguage = language;
        this.extractSameLanguage = extractSameLanguage;
        this.languages = new Stack<>();
        this.languages.push(language);
        this.extractedText = new StringBuilder(200);
        this.skipCharacters = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        final String lang = normalizeLang(attributes.getValue("lang") != null ? attributes.getValue("lang") : attributes.getValue("xml:lang"));
        languages.push(lang != null && !lang.isEmpty() ? lang : languages.peek());
        if ("img".equalsIgnoreCase(localName) && attributes.getValue("alt") != null) {
            processText(attributes.getValue("alt").trim());
        } else if ("script".equalsIgnoreCase(localName) || "style".equalsIgnoreCase(localName)) {
            skipCharacters = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        languages.pop();
        if ("script".equalsIgnoreCase(localName) || "style".equalsIgnoreCase(localName)) {
            skipCharacters = false;
        }
        extractedText.append(WHITE_SPACE);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!skipCharacters) {
            processText(new String(ch, start, length).trim());
        }
    }

    public String getExtractedText() {
        return extractedText.toString();
    }

    private void processText(final String text) {
        if (!text.isEmpty()) {
            if (extractSameLanguage) {
                if (webpageLanguage.equals(languages.peek())) {
                    extractedText.append(text.trim());
                    extractedText.append(WHITE_SPACE);
                }
            } else {
                if (!webpageLanguage.equals(languages.peek())) {
                    extractedText.append(text.trim());
                    extractedText.append(WHITE_SPACE);
                }
            }
        }
    }

    /**
     * Eliminamos las variantes idiomáticas para quedarnos únicamente con el idioma base (ej. en-us pasa a en)
     *
     * @param lang la cadena que representa el idioma completo con las variantes
     * @return una cadena que representa el idioma base
     */
    private String normalizeLang(final String lang) {
        if (lang != null && lang.contains("-")) {
            return lang.substring(0, lang.indexOf('-'));
        }
        return lang;
    }

}
