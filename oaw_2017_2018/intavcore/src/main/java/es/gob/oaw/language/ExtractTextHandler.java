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
package es.gob.oaw.language;

import java.util.Arrays;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Clase para extraer el texto de una página web excluyendo el texto de aquellas
 * etiquetas que definan un idioma distinto al definido a nivel de página web
 * (etiqueta html).
 */
public class ExtractTextHandler extends DefaultHandler {

	private static final String WHITE_SPACE = " ";

	/**
	 * Idioma de la página web
	 */
	private final String webpageLanguage;

	/**
	 * Pila donde se irán guardando los idiomas de las etiquetas compuestas para
	 * saber si un determinado texto se debe incluir o excluir
	 */
	private final Stack<String> languages;

	/**
	 * Variable donde se irá guardando el texto extraido
	 */
	private final StringBuilder extractedText;

	private final boolean extractSameLanguage;

	private boolean skipCharacters;

	private String[] skipTags;

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

	public ExtractTextHandler(final String language, final boolean extractSameLanguage, String[] skipTags) {
		this.webpageLanguage = language;
		this.extractSameLanguage = extractSameLanguage;
		this.languages = new Stack<>();
		this.languages.push(language);
		this.extractedText = new StringBuilder(200);
		this.skipCharacters = false;
		this.skipTags = skipTags;
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

		// Omitir etiquetas indicadas en el constructor
		else if (this.skipTags != null && this.skipTags.length > 0) {
			if (Arrays.asList(skipTags).contains(localName.toLowerCase())) {
				skipCharacters = true;
			}
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
	 * Eliminamos las variantes idiomáticas para quedarnos únicamente con el
	 * idioma base (ej. en-us pasa a en)
	 *
	 * @param lang
	 *            la cadena que representa el idioma completo con las variantes
	 * @return una cadena que representa el idioma base
	 */
	private String normalizeLang(final String lang) {
		if (lang != null && lang.contains("-")) {
			return lang.substring(0, lang.indexOf('-'));
		}
		return lang;
	}

}
