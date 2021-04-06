/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.gob.oaw.css;

import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Clase que simula un recurso CSS que representa una hoja de estilos completa. Esta clase contendrá los estilos de
 * un bloque style o una hoja de estilos enlazada bien mediante la etiqueta link o mediante la regla @import de CSS
 */
public class CSSStyleSheetResource implements CSSResource {


    /** The html element. */
    private final Element htmlElement;
    
    /** The content. */
    private final String content;
    
    /** The base URL. */
    private final String baseURL;

    /**
	 * Instancia un nuevo recurso que representa los estilos definidos mediante un bloque style o una etiqueta link.
	 *
	 * @param baseUrl     the base url
	 * @param htmlElement el elemento (Element) que utiliza el atributo style
	 */
    public CSSStyleSheetResource(final String baseUrl, final Element htmlElement) {
        this.baseURL = baseUrl;
        this.htmlElement = htmlElement;
        this.content = extractContent(htmlElement);
    }

    /**
	 * Extract content.
	 *
	 * @param htmlElement the html element
	 * @return the string
	 */
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

    /**
	 * Gets the HTML element.
	 *
	 * @return the HTML element
	 */
    @Override
    public Element getHTMLElement() {
        return htmlElement;
    }

    /**
	 * Gets the content.
	 *
	 * @return the content
	 */
    @Override
    public String getContent() {
        return content;
    }

    /**
	 * Checks if is inline.
	 *
	 * @return true, if is inline
	 */
    @Override
    public boolean isInline() {
        return false;
    }

    /**
	 * Gets the string source.
	 *
	 * @return the string source
	 */
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

    /**
	 * Checks if is imported.
	 *
	 * @return true, if is imported
	 */
    @Override
    public boolean isImported() {
        return "link".equalsIgnoreCase(htmlElement.getNodeName());
    }

    /**
	 * Hash code.
	 *
	 * @return the int
	 */
    @Override
    public int hashCode() {
        return getStringSource().hashCode();
    }

    /**
	 * Equals.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
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
