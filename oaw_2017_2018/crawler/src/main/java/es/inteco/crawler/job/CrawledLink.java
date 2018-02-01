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

public class CrawledLink {
	private String url;
	private String source;
	private String hash;
	private int numRetries;
	private int numRedirections;
	private String charset;

	public CrawledLink(String url, String source, int numRetries, int numRedirections) {
		this.url = url;
		this.source = source;
		this.numRedirections = numRedirections;
		this.numRetries = numRetries;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getNumRetries() {
		return numRetries;
	}

	public void setNumRetries(int numRetries) {
		this.numRetries = numRetries;
	}

	public int getNumRedirections() {
		return numRedirections;
	}

	public void setNumRedirections(int numRedirections) {
		this.numRedirections = numRedirections;

	}

	@Override
	public int hashCode() {
		return url != null ? url.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof CrawledLink && ((CrawledLink) obj).getUrl().equals(this.url);
	}

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset
	 *            the charset to set
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

}