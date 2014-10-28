package es.ctic.language;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

/**
 * Clase para extraer el texto de una página web excluyendo el texto de aquellas etiquetas que definan un idioma distinto al definido a nivel de página web (etiqueta html).
 */
public class ExtractTextHandler extends DefaultHandler {

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

    public ExtractTextHandler(final String language) {
        this(language, true);
    }

    public ExtractTextHandler(final String language, final boolean extractSameLanguage) {
        this.webpageLanguage = language;
        this.extractSameLanguage = extractSameLanguage;
        languages = new Stack<String>();
        languages.push(language);
        extractedText = new StringBuilder(200);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        final String lang = attributes.getValue("lang");
        languages.push(lang != null && !lang.isEmpty() ? lang : languages.peek() ); //webpageLanguage);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        languages.pop();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (extractSameLanguage) {
            if (webpageLanguage.equals(languages.peek())) {
                extractedText.append(ch, start, length);
            }
        } else {
            if (!webpageLanguage.equals(languages.peek())) {
                extractedText.append(ch, start, length);
            }
        }
    }

    public String getExtractedText() {
        return extractedText.toString();
    }

}
