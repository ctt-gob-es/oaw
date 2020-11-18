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

import java.util.List;

/**
 * The Class CrawlerData.
 */
public class CrawlerData {
	/** The nombre rastreo. */
	private String nombreRastreo;
	/** The id crawling. */
	private long idCrawling;
	/** The id fulfilled crawling. */
	private long idFulfilledCrawling;
	/** The id cartridge. */
	private int idCartridge;
	/** The num hilos. */
	private int numHilos;
	/** The profundidad. */
	private int profundidad;
	/** The top N. */
	private int topN;
	/** The modo. */
	private boolean modo;
	/** The categorizacion. */
	private boolean categorizacion;
	/** The action script. */
	private boolean actionScript;
	/** The user. */
	private String user;
	/** The users mail. */
	private List<String> usersMail;
	/** The responsibles mail. */
	private List<String> responsiblesMail;
	/** The id customer account. */
	private long idCustomerAccount;
	/** The id observatory. */
	private long idObservatory;
	/** The pseudoaleatorio. */
	private boolean pseudoaleatorio;
	/** The language. */
	private String language;
	/** The cartuchos. */
	private String[] cartuchos;
	/** The urls. */
	private List<String> urls;
	/** The content. */
	private String content;
	/** The domains. */
	private List<String> domains;
	/** The exceptions. */
	private List<String> exceptions;
	/** The crawling list. */
	private List<String> crawlingList;
	/** The id guideline. */
	private long idGuideline;
	/** The fichero norma. */
	private String ficheroNorma;
	/** The exhaustive. */
	private boolean exhaustive;
	/** The test. */
	private boolean test;
	/** The acronimo. */
	private String acronimo;
	/** The in directory. */
	private boolean inDirectory;
	/** The check table page. */
	// Indicadores para saber si hemos encontrado urls de ciertos tipos
	private boolean checkTablePage;
	/** The check form page. */
	private boolean checkFormPage;
	/** The max intentos buscar tipos. */
	// Máximo número de intentos de comprobar tipos por cada URL
	private int maxIntentosBuscarTipos = 5;
	/** The extend timeout. Flag to indicate need to extend timeout */
	private boolean extendTimeout = false;
	/** The extended timeout value. */
	private int extendedTimeoutValue = 0;
	/** The is retry. */
	private boolean isRetry = false;
	/** The extended depth. */
	private int extendedDepth = 0;
	/** The extended width. */
	private int extendedWidth = 0;

	/**
	 * Checks if is in directory.
	 *
	 * @return true, if is in directory
	 */
	public boolean isInDirectory() {
		return inDirectory;
	}

	/**
	 * Sets the in directory.
	 *
	 * @param inDirectory the new in directory
	 */
	public void setInDirectory(boolean inDirectory) {
		this.inDirectory = inDirectory;
	}

	/**
	 * Gets the fichero norma.
	 *
	 * @return the fichero norma
	 */
	public String getFicheroNorma() {
		return ficheroNorma;
	}

	/**
	 * Sets the fichero norma.
	 *
	 * @param ficheroNorma the new fichero norma
	 */
	public void setFicheroNorma(String ficheroNorma) {
		this.ficheroNorma = ficheroNorma;
	}

	/**
	 * Gets the urls.
	 *
	 * @return the urls
	 */
	public List<String> getUrls() {
		return urls;
	}

	/**
	 * Sets the urls.
	 *
	 * @param urls the new urls
	 */
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	/**
	 * Gets the domains.
	 *
	 * @return the domains
	 */
	public List<String> getDomains() {
		return domains;
	}

	/**
	 * Sets the domains.
	 *
	 * @param domains the new domains
	 */
	public void setDomains(List<String> domains) {
		this.domains = domains;
	}

	/**
	 * Gets the exceptions.
	 *
	 * @return the exceptions
	 */
	public List<String> getExceptions() {
		return exceptions;
	}

	/**
	 * Sets the exceptions.
	 *
	 * @param exceptions the new exceptions
	 */
	public void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * Gets the crawling list.
	 *
	 * @return the crawling list
	 */
	public List<String> getCrawlingList() {
		return crawlingList;
	}

	/**
	 * Sets the crawling list.
	 *
	 * @param crawlingList the new crawling list
	 */
	public void setCrawlingList(List<String> crawlingList) {
		this.crawlingList = crawlingList;
	}

	/**
	 * Gets the id guideline.
	 *
	 * @return the id guideline
	 */
	public long getIdGuideline() {
		return idGuideline;
	}

	/**
	 * Sets the id guideline.
	 *
	 * @param idGuideline the new id guideline
	 */
	public void setIdGuideline(long idGuideline) {
		this.idGuideline = idGuideline;
	}

	/**
	 * Instantiates a new crawler data.
	 */
	public CrawlerData() {
		test = false;
	}

	/**
	 * Gets the cartuchos.
	 *
	 * @return the cartuchos
	 */
	public String[] getCartuchos() {
		return cartuchos;
	}

	/**
	 * Sets the cartuchos.
	 *
	 * @param cartuchos the new cartuchos
	 */
	public void setCartuchos(String[] cartuchos) {
		this.cartuchos = cartuchos;
	}

	/**
	 * Gets the id crawling.
	 *
	 * @return the id crawling
	 */
	public long getIdCrawling() {
		return idCrawling;
	}

	/**
	 * Sets the id crawling.
	 *
	 * @param idCrawling the new id crawling
	 */
	public void setIdCrawling(long idCrawling) {
		this.idCrawling = idCrawling;
	}

	/**
	 * Gets the id fulfilled crawling.
	 *
	 * @return the id fulfilled crawling
	 */
	public long getIdFulfilledCrawling() {
		return idFulfilledCrawling;
	}

	/**
	 * Sets the id fulfilled crawling.
	 *
	 * @param idFulfilledCrawling the new id fulfilled crawling
	 */
	public void setIdFulfilledCrawling(long idFulfilledCrawling) {
		this.idFulfilledCrawling = idFulfilledCrawling;
	}

	/**
	 * Gets the id cartridge.
	 *
	 * @return the id cartridge
	 */
	public int getIdCartridge() {
		return idCartridge;
	}

	/**
	 * Sets the id cartridge.
	 *
	 * @param idCartridge the new id cartridge
	 */
	public void setIdCartridge(int idCartridge) {
		this.idCartridge = idCartridge;
	}

	/**
	 * Gets the num hilos.
	 *
	 * @return the num hilos
	 */
	public int getNumHilos() {
		return numHilos;
	}

	/**
	 * Sets the num hilos.
	 *
	 * @param numHilos the new num hilos
	 */
	public void setNumHilos(int numHilos) {
		this.numHilos = numHilos;
	}

	/**
	 * Gets the profundidad.
	 *
	 * @return the profundidad
	 */
	public int getProfundidad() {
		return profundidad;
	}

	/**
	 * Sets the profundidad.
	 *
	 * @param profundidad the new profundidad
	 */
	public void setProfundidad(int profundidad) {
		this.profundidad = profundidad;
	}

	/**
	 * Gets the top N.
	 *
	 * @return the top N
	 */
	public int getTopN() {
		return topN;
	}

	/**
	 * Sets the top N.
	 *
	 * @param topN the new top N
	 */
	public void setTopN(int topN) {
		this.topN = topN;
	}

	/**
	 * Checks if is modo.
	 *
	 * @return true, if is modo
	 */
	public boolean isModo() {
		return modo;
	}

	/**
	 * Sets the modo.
	 *
	 * @param modo the new modo
	 */
	public void setModo(boolean modo) {
		this.modo = modo;
	}

	/**
	 * Checks if is categorizacion.
	 *
	 * @return true, if is categorizacion
	 */
	public boolean isCategorizacion() {
		return categorizacion;
	}

	/**
	 * Sets the categorizacion.
	 *
	 * @param categorizacion the new categorizacion
	 */
	public void setCategorizacion(boolean categorizacion) {
		this.categorizacion = categorizacion;
	}

	/**
	 * Checks if is action script.
	 *
	 * @return true, if is action script
	 */
	public boolean isActionScript() {
		return actionScript;
	}

	/**
	 * Sets the action script.
	 *
	 * @param actionScript the new action script
	 */
	public void setActionScript(boolean actionScript) {
		this.actionScript = actionScript;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the users mail.
	 *
	 * @return the users mail
	 */
	public List<String> getUsersMail() {
		return usersMail;
	}

	/**
	 * Sets the users mail.
	 *
	 * @param usersMail the new users mail
	 */
	public void setUsersMail(List<String> usersMail) {
		this.usersMail = usersMail;
	}

	/**
	 * Gets the id customer account.
	 *
	 * @return the id customer account
	 */
	public long getIdCustomerAccount() {
		return idCustomerAccount;
	}

	/**
	 * Sets the id customer account.
	 *
	 * @param idCustomerAccount the new id customer account
	 */
	public void setIdCustomerAccount(long idCustomerAccount) {
		this.idCustomerAccount = idCustomerAccount;
	}

	/**
	 * Gets the id observatory.
	 *
	 * @return the id observatory
	 */
	public long getIdObservatory() {
		return idObservatory;
	}

	/**
	 * Sets the id observatory.
	 *
	 * @param idObservatory the new id observatory
	 */
	public void setIdObservatory(long idObservatory) {
		this.idObservatory = idObservatory;
	}

	/**
	 * Checks if is pseudoaleatorio.
	 *
	 * @return true, if is pseudoaleatorio
	 */
	public boolean isPseudoaleatorio() {
		return pseudoaleatorio;
	}

	/**
	 * Sets the pseudoaleatorio.
	 *
	 * @param pseudoaleatorio the new pseudoaleatorio
	 */
	public void setPseudoaleatorio(boolean pseudoaleatorio) {
		this.pseudoaleatorio = pseudoaleatorio;
	}

	/**
	 * Gets the nombre rastreo.
	 *
	 * @return the nombre rastreo
	 */
	public String getNombreRastreo() {
		return nombreRastreo;
	}

	/**
	 * Sets the nombre rastreo.
	 *
	 * @param nombreRastreo the new nombre rastreo
	 */
	public void setNombreRastreo(String nombreRastreo) {
		this.nombreRastreo = nombreRastreo;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language.
	 *
	 * @param language the new language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Checks if is exhaustive.
	 *
	 * @return true, if is exhaustive
	 */
	public boolean isExhaustive() {
		return exhaustive;
	}

	/**
	 * Sets the exhaustive.
	 *
	 * @param exhaustive the new exhaustive
	 */
	public void setExhaustive(boolean exhaustive) {
		this.exhaustive = exhaustive;
	}

	/**
	 * Checks if is test.
	 *
	 * @return true, if is test
	 */
	public boolean isTest() {
		return test;
	}

	/**
	 * Sets the test.
	 *
	 * @param test the new test
	 */
	public void setTest(boolean test) {
		this.test = test;
	}

	/**
	 * Gets the responsibles mail.
	 *
	 * @return the responsibles mail
	 */
	public List<String> getResponsiblesMail() {
		return responsiblesMail;
	}

	/**
	 * Sets the responsibles mail.
	 *
	 * @param responsiblesMail the new responsibles mail
	 */
	public void setResponsiblesMail(List<String> responsiblesMail) {
		this.responsiblesMail = responsiblesMail;
	}

	/**
	 * Gets the acronimo.
	 *
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * Sets the acronimo.
	 *
	 * @param acronimo the new acronimo
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Checks if is check table page.
	 *
	 * @return the checkTablePage
	 */
	public boolean isCheckTablePage() {
		return checkTablePage;
	}

	/**
	 * Sets the check table page.
	 *
	 * @param checkTablePage the checkTablePage to set
	 */
	public void setCheckTablePage(boolean checkTablePage) {
		this.checkTablePage = checkTablePage;
	}

	/**
	 * Checks if is check form page.
	 *
	 * @return the checkFormPage
	 */
	public boolean isCheckFormPage() {
		return checkFormPage;
	}

	/**
	 * Sets the check form page.
	 *
	 * @param checkFormPage the checkFormPage to set
	 */
	public void setCheckFormPage(boolean checkFormPage) {
		this.checkFormPage = checkFormPage;
	}

	/**
	 * Gets the max intentos buscar tipos.
	 *
	 * @return the maxIntentosBuscarTipos
	 */
	public int getMaxIntentosBuscarTipos() {
		return maxIntentosBuscarTipos;
	}

	/**
	 * Sets the max intentos buscar tipos.
	 *
	 * @param maxIntentosBuscarTipos the maxIntentosBuscarTipos to set
	 */
	public void setMaxIntentosBuscarTipos(int maxIntentosBuscarTipos) {
		this.maxIntentosBuscarTipos = maxIntentosBuscarTipos;
	}

	/**
	 * Checks if is retry.
	 *
	 * @return the isRetry
	 */
	public boolean isRetry() {
		return isRetry;
	}

	/**
	 * Sets the retry.
	 *
	 * @param isRetry the isRetry to set
	 */
	public void setRetry(boolean isRetry) {
		this.isRetry = isRetry;
	}

	/**
	 * Checks if is extend timeout.
	 *
	 * @return the extendTimeout
	 */
	public boolean isExtendTimeout() {
		return extendTimeout;
	}

	/**
	 * Sets the extend timeout.
	 *
	 * @param extendTimeout the extendTimeout to set
	 */
	public void setExtendTimeout(boolean extendTimeout) {
		this.extendTimeout = extendTimeout;
	}

	/**
	 * Gets the extended timeout value.
	 *
	 * @return the extendedTimeoutValue
	 */
	public int getExtendedTimeoutValue() {
		return extendedTimeoutValue;
	}

	/**
	 * Sets the extended timeout value.
	 *
	 * @param extendedTimeoutValue the extendedTimeoutValue to set
	 */
	public void setExtendedTimeoutValue(int extendedTimeoutValue) {
		this.extendedTimeoutValue = extendedTimeoutValue;
	}

	/**
	 * Gets the extended depth.
	 *
	 * @return the extendedDepth
	 */
	public int getExtendedDepth() {
		return extendedDepth;
	}

	/**
	 * Sets the extended depth.
	 *
	 * @param extendedDepth the extendedDepth to set
	 */
	public void setExtendedDepth(int extendedDepth) {
		this.extendedDepth = extendedDepth;
	}

	/**
	 * Gets the extended width.
	 *
	 * @return the extendedWidth
	 */
	public int getExtendedWidth() {
		return extendedWidth;
	}

	/**
	 * Sets the extended width.
	 *
	 * @param extendedWidth the extendedWidth to set
	 */
	public void setExtendedWidth(int extendedWidth) {
		this.extendedWidth = extendedWidth;
	}
}