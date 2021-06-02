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

/**
 * Clase que representa un recurso CSS definido en línea sobre un elemento HTML mediante el atributo style.
 */
public class CSSInlineResource implements CSSResource {


    /** The html element. */
    private final Element htmlElement;

    /**
	 * Instancia un nuevo recurso que representa el estilo en línea de una etiqueta HTML.
	 *
	 * @param htmlElement el elemento (Element) que utiliza el atributo style
	 */
    public CSSInlineResource(final Element htmlElement) {
        this.htmlElement = htmlElement;
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
        return htmlElement.getAttribute("style");
    }

    /**
	 * Checks if is inline.
	 *
	 * @return true, if is inline
	 */
    @Override
    public boolean isInline() {
        return true;
    }

    /**
	 * Gets the string source.
	 *
	 * @return the string source
	 */
    @Override
    public String getStringSource() {
        return "Atributo style " + htmlElement.getLocalName();
    }

    /**
	 * Checks if is imported.
	 *
	 * @return true, if is imported
	 */
    @Override
    public boolean isImported() {
        return false;
    }

}
