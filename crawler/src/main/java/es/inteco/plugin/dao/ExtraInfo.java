package es.inteco.plugin.dao;

/**
 * The Class ExtraInfo.
 */
public class ExtraInfo {

	/** The id ejecucion observatorio. */
	private int idEjecucionObservatorio;
	
	/** The nombre lista. */
	private String nombreLista;
	
	/** The url lista. */
	private String urlLista;

	/**
	 * Instantiates a new extra info.
	 *
	 * @param idEjecucionObservatorio the id ejecucion observatorio
	 * @param nombreLista             the nombre lista
	 * @param urlLista                the url lista
	 */
	public ExtraInfo(int idEjecucionObservatorio, String nombreLista, String urlLista) {
		super();
		this.idEjecucionObservatorio = idEjecucionObservatorio;
		this.nombreLista = nombreLista;
		this.urlLista = urlLista;
	}

	/**
	 * Gets the id ejecucion observatorio.
	 *
	 * @return the id ejecucion observatorio
	 */
	public int getIdEjecucionObservatorio() {
		return idEjecucionObservatorio;
	}

	/**
	 * Sets the id ejecucion observatorio.
	 *
	 * @param idEjecucionObservatorio the new id ejecucion observatorio
	 */
	public void setIdEjecucionObservatorio(int idEjecucionObservatorio) {
		this.idEjecucionObservatorio = idEjecucionObservatorio;
	}

	/**
	 * Gets the nombre lista.
	 *
	 * @return the nombre lista
	 */
	public String getNombreLista() {
		return nombreLista;
	}

	/**
	 * Sets the nombre lista.
	 *
	 * @param nombreLista the new nombre lista
	 */
	public void setNombreLista(String nombreLista) {
		this.nombreLista = nombreLista;
	}

	/**
	 * Gets the url lista.
	 *
	 * @return the url lista
	 */
	public String getUrlLista() {
		return urlLista;
	}

	/**
	 * Sets the url lista.
	 *
	 * @param urlLista the new url lista
	 */
	public void setUrlLista(String urlLista) {
		this.urlLista = urlLista;
	}

}
