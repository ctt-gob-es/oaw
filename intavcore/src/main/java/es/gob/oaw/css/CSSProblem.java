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

import java.util.Date;

/**
 * The Class CSSProblem.
 */
public class CSSProblem {
    
    /** The date. */
    private Date date;
    
    /** The line number. */
    private int lineNumber;
    
    /** The column number. */
    private int columnNumber;
    
    /** The text content. */
    private String textContent;
    
    /** The selector. */
    private String selector;
    
    /** The element. */
    private Element element;

    /**
	 * Gets the element.
	 *
	 * @return the element
	 */
    public Element getElement() {
        return element;
    }

    /**
	 * Sets the element.
	 *
	 * @param element the new element
	 */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
	 * Gets the date.
	 *
	 * @return the date
	 */
    public Date getDate() {
        return date;
    }

    /**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
	 * Gets the line number.
	 *
	 * @return the line number
	 */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
	 * Sets the line number.
	 *
	 * @param lineNumber the new line number
	 */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
	 * Gets the column number.
	 *
	 * @return the column number
	 */
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
	 * Sets the column number.
	 *
	 * @param columnNumber the new column number
	 */
    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    /**
	 * Gets the text content.
	 *
	 * @return the text content
	 */
    public String getTextContent() {
        return textContent;
    }

    /**
	 * Sets the text content.
	 *
	 * @param textContent the new text content
	 */
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    /**
	 * Gets the selector.
	 *
	 * @return the selector
	 */
    public String getSelector() {
        return selector;
    }

    /**
	 * Sets the selector.
	 *
	 * @param selector the new selector
	 */
    public void setSelector(String selector) {
        this.selector = selector;
    }
}
