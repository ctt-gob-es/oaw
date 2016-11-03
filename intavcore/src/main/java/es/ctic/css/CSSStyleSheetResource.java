package es.ctic.css;

import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Clase que simula un recurso CSS que representa una hoja de estilos completa. Esta clase contendrá los estilos de
 * un bloque style o una hoja de estilos enlazada bien mediante la etiqueta link o mediante la regla @import de CSS
 */
public class CSSStyleSheetResource implements CSSResource {


    private final Element htmlElement;
    private final String content;
    private final String baseURL;

    /**
     * Instancia un nuevo recurso que representa los estilos definidos mediante un bloque style o una etiqueta link
     *
     * @param htmlElement el elemento (Element) que utiliza el atributo style
     */
    public CSSStyleSheetResource(final String baseUrl, final Element htmlElement) {
        this.baseURL = baseUrl;
        this.htmlElement = htmlElement;
        this.content = extractContent(htmlElement);
    }

    private String extractContent(final Element htmlElement) {
        if (htmlElement == null) {
            return "";
        } else if ("style".equalsIgnoreCase(htmlElement.getNodeName())) {
            return htmlElement.getTextContent();
        } else if ("link".equalsIgnoreCase(htmlElement.getNodeName())) {
            return htmlElement.getUserData("css") != null ? htmlElement.getUserData("css").toString() : "";
        } else {
            return "";
        }
    }

    @Override
    public Element getHTMLElement() {
        return htmlElement;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean isInline() {
        return false;
    }

    @Override
    public String getStringSource() {
        if ("style".equalsIgnoreCase(htmlElement.getNodeName())) {
            return "Bloque <style>";
        } else if ("link".equalsIgnoreCase(htmlElement.getNodeName())) {
            try {
                return new URL(new URL(baseURL), htmlElement.getAttribute("href")).toString();
            } catch (MalformedURLException e) {
                return htmlElement.getAttribute("href");
            }
        } else {
            return "";
        }
    }

    @Override
    public boolean isImported() {
        return "link".equalsIgnoreCase(htmlElement.getNodeName());
    }

    @Override
    public int hashCode() {
        return getStringSource().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CSSResource)) {
            return false;
        }

        CSSResource that = (CSSResource) o;

        return getStringSource().equals(that.getStringSource());
    }
}
