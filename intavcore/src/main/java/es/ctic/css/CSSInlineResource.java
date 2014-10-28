package es.ctic.css;

import org.w3c.dom.Element;

/**
 * Clase que representa un recurso CSS definido en línea sobre un elemento HTML mediante el atributo style
 */
public class CSSInlineResource implements CSSResource {


    private final Element htmlElement;

    /**
     * Instancia un nuevo recurso que representa el estilo en línea de una etiqueta HTML
     *
     * @param htmlElement el elemento (Element) que utiliza el atributo style
     */
    public CSSInlineResource(final Element htmlElement) {
        this.htmlElement = htmlElement;
    }

    @Override
    public Element getHTMLElement() {
        return htmlElement;
    }

    @Override
    public String getContent() {
        return htmlElement.getAttribute("style");
    }

    @Override
    public boolean isInline() {
        return true;
    }

    @Override
    public boolean isImported() {
        return false;
    }

}
