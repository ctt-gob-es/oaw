package es.inteco.crawler.job;

/**
 * The Class EstadoObservatoriosTiemposData.
 */
public class ObservatorySummaryTimes {

	/** The tiempo. */
	private int tiempo;

	/** The url. */
	private String url;

	/** The nombre. */
	private String nombre;

	/**
	 * Instantiates a new estado observatorio analisis data.
	 *
	 * @param tiempo
	 *            the tiempo
	 * @param url
	 *            the url
	 * @param nombre
	 *            the nombre
	 */
	public ObservatorySummaryTimes(int tiempo, String url, String nombre) {
		super();
		this.tiempo = tiempo;
		this.url = url;
		this.nombre = nombre;
	}

	/**
	 * Gets the tiempo.
	 *
	 * @return the tiempo
	 */
	public int getTiempo() {
		return tiempo;
	}

	/**
	 * Sets the tiempo.
	 *
	 * @param tiempo
	 *            the tiempo to set
	 */
	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
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
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
