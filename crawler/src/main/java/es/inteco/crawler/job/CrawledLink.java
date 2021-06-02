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
package es.inteco.crawler.job;

/**
 * The Class CrawledLink.
 */
public class CrawledLink {
	
	/** The url. */
	private String url;
	
	/** The source. */
	private String source;
	
	/** The hash. */
	private String hash;
	
	/** The num retries. */
	private int numRetries;
	
	/** The num redirections. */
	private int numRedirections;
	
	/** The charset. */
	private String charset;

	/**
	 * Instantiates a new crawled link.
	 *
	 * @param url             the url
	 * @param source          the source
	 * @param numRetries      the num retries
	 * @param numRedirections the num redirections
	 */
	public CrawledLink(String url, String source, int numRetries, int numRedirections) {
		this.url = url;
		this.source = source;
		this.numRedirections = numRedirections;
		this.numRetries = numRetries;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Sets the hash.
	 *
	 * @param hash the new hash
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * Gets the num retries.
	 *
	 * @return the num retries
	 */
	public int getNumRetries() {
		return numRetries;
	}

	/**
	 * Sets the num retries.
	 *
	 * @param numRetries the new num retries
	 */
	public void setNumRetries(int numRetries) {
		this.numRetries = numRetries;
	}

	/**
	 * Gets the num redirections.
	 *
	 * @return the num redirections
	 */
	public int getNumRedirections() {
		return numRedirections;
	}

	/**
	 * Sets the num redirections.
	 *
	 * @param numRedirections the new num redirections
	 */
	public void setNumRedirections(int numRedirections) {
		this.numRedirections = numRedirections;

	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return url != null ? url.hashCode() : 0;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CrawledLink && ((CrawledLink) obj).getUrl().equals(this.url);
	}

	/**
	 * Gets the charset.
	 *
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Sets the charset.
	 *
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

}