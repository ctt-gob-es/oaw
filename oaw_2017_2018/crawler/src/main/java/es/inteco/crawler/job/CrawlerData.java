package es.inteco.crawler.job;

import java.util.List;

public class CrawlerData {
    private String nombreRastreo;
    private long idCrawling;
    private long idFulfilledCrawling;
    private int idCartridge;
    private int numHilos;
    private int profundidad;
    private int topN;
    private boolean modo;
    private boolean categorizacion;
    private boolean actionScript;
    private String user;
    private List<String> usersMail;
    private List<String> responsiblesMail;
    private long idCustomerAccount;
    private long idObservatory;
    private boolean pseudoaleatorio;
    private String language;
    private String[] cartuchos;
    private List<String> urls;
    private String content;
    private List<String> domains;
    private List<String> exceptions;
    private List<String> crawlingList;
    private long idGuideline;
    private String ficheroNorma;
    private boolean exhaustive;
    private boolean test;
    private String acronimo;
    private boolean inDirectory;
    
    //TODO 2017 Indicadores para saber si hemos encontrado urls de ciertos tipos
    private boolean checkTablePage;
    private boolean checkFormPage;
    
    private int maxIntentosBuscarTipos = 15;

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }

    public String getFicheroNorma() {
        return ficheroNorma;
    }

    public void setFicheroNorma(String ficheroNorma) {
        this.ficheroNorma = ficheroNorma;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<String> exceptions) {
        this.exceptions = exceptions;
    }

    public List<String> getCrawlingList() {
        return crawlingList;
    }

    public void setCrawlingList(List<String> crawlingList) {
        this.crawlingList = crawlingList;
    }

    public long getIdGuideline() {
        return idGuideline;
    }

    public void setIdGuideline(long idGuideline) {
        this.idGuideline = idGuideline;
    }

    public CrawlerData() {
        test = false;
    }

    public String[] getCartuchos() {
        return cartuchos;
    }

    public void setCartuchos(String[] cartuchos) {
        this.cartuchos = cartuchos;
    }

    public long getIdCrawling() {
        return idCrawling;
    }

    public void setIdCrawling(long idCrawling) {
        this.idCrawling = idCrawling;
    }

    public long getIdFulfilledCrawling() {
        return idFulfilledCrawling;
    }

    public void setIdFulfilledCrawling(long idFulfilledCrawling) {
        this.idFulfilledCrawling = idFulfilledCrawling;
    }

    public int getIdCartridge() {
        return idCartridge;
    }

    public void setIdCartridge(int idCartridge) {
        this.idCartridge = idCartridge;
    }

    public int getNumHilos() {
        return numHilos;
    }

    public void setNumHilos(int numHilos) {
        this.numHilos = numHilos;
    }

    public int getProfundidad() {
        return profundidad;
    }

    public void setProfundidad(int profundidad) {
        this.profundidad = profundidad;
    }

    public int getTopN() {
        return topN;
    }

    public void setTopN(int topN) {
        this.topN = topN;
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }

    public boolean isCategorizacion() {
        return categorizacion;
    }

    public void setCategorizacion(boolean categorizacion) {
        this.categorizacion = categorizacion;
    }

    public boolean isActionScript() {
        return actionScript;
    }

    public void setActionScript(boolean actionScript) {
        this.actionScript = actionScript;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getUsersMail() {
        return usersMail;
    }

    public void setUsersMail(List<String> usersMail) {
        this.usersMail = usersMail;
    }

    public long getIdCustomerAccount() {
        return idCustomerAccount;
    }

    public void setIdCustomerAccount(long idCustomerAccount) {
        this.idCustomerAccount = idCustomerAccount;
    }

    public long getIdObservatory() {
        return idObservatory;
    }

    public void setIdObservatory(long idObservatory) {
        this.idObservatory = idObservatory;
    }

    public boolean isPseudoaleatorio() {
        return pseudoaleatorio;
    }

    public void setPseudoaleatorio(boolean pseudoaleatorio) {
        this.pseudoaleatorio = pseudoaleatorio;
    }

    public String getNombreRastreo() {
        return nombreRastreo;
    }

    public void setNombreRastreo(String nombreRastreo) {
        this.nombreRastreo = nombreRastreo;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isExhaustive() {
        return exhaustive;
    }

    public void setExhaustive(boolean exhaustive) {
        this.exhaustive = exhaustive;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public List<String> getResponsiblesMail() {
        return responsiblesMail;
    }

    public void setResponsiblesMail(List<String> responsiblesMail) {
        this.responsiblesMail = responsiblesMail;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	/**
	 * @return the checkTablePage
	 */
	public boolean isCheckTablePage() {
		return checkTablePage;
	}

	/**
	 * @param checkTablePage the checkTablePage to set
	 */
	public void setCheckTablePage(boolean checkTablePage) {
		this.checkTablePage = checkTablePage;
	}

	/**
	 * @return the checkFormPage
	 */
	public boolean isCheckFormPage() {
		return checkFormPage;
	}

	/**
	 * @param checkFormPage the checkFormPage to set
	 */
	public void setCheckFormPage(boolean checkFormPage) {
		this.checkFormPage = checkFormPage;
	}

	/**
	 * @return the maxIntentosBuscarTipos
	 */
	public int getMaxIntentosBuscarTipos() {
		return maxIntentosBuscarTipos;
	}

	/**
	 * @param maxIntentosBuscarTipos the maxIntentosBuscarTipos to set
	 */
	public void setMaxIntentosBuscarTipos(int maxIntentosBuscarTipos) {
		this.maxIntentosBuscarTipos = maxIntentosBuscarTipos;
	}
    
    
}