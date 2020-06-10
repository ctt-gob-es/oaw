/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.pdf.utils;

import com.itextpdf.text.Font;

/**
 * The Class SpecialChunk.
 */
public class SpecialChunk {
	/** The text. */
	private String text;
	/** The anchor. */
	private String anchor;
	/** The destination. */
	private boolean destination;
	/** The font. */
	private Font font;
	/** The external link. */
	private boolean externalLink;
	/** The type. */
	private String type;

	/**
	 * Instantiates a new special chunk.
	 *
	 * @param text        the text
	 * @param anchor      the anchor
	 * @param destination the destination
	 * @param font        the font
	 */
	public SpecialChunk(String text, String anchor, boolean destination, Font font) {
		this.text = text;
		this.anchor = anchor;
		this.destination = destination;
		this.font = font;
		this.externalLink = false;
	}

	/**
	 * Instantiates a new special chunk.
	 *
	 * @param text the text
	 * @param font the font
	 */
	public SpecialChunk(String text, Font font) {
		this.text = text;
		this.font = font;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the anchor.
	 *
	 * @return the anchor
	 */
	public String getAnchor() {
		return anchor;
	}

	/**
	 * Sets the anchor.
	 *
	 * @param anchor the new anchor
	 */
	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	/**
	 * Checks if is destination.
	 *
	 * @return true, if is destination
	 */
	public boolean isDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param destination the new destination
	 */
	public void setDestination(boolean destination) {
		this.destination = destination;
	}

	/**
	 * Gets the font.
	 *
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Sets the font.
	 *
	 * @param font the new font
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * Checks if is external link.
	 *
	 * @return true, if is external link
	 */
	public boolean isExternalLink() {
		return externalLink;
	}

	/**
	 * Sets the external link.
	 *
	 * @param externalLink the new external link
	 */
	public void setExternalLink(boolean externalLink) {
		this.externalLink = externalLink;
	}
}
