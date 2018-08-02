package es.inteco.crawler.job;

import java.util.Date;

/**
 * The Class EstadoObservatorioAnalisisData.
 */
public class ObservatoryStatus {

	/** The id. */
	private Integer id;

	/** The id observatorio. */
	private Integer idObservatorio;

	/** The id ejecucion observatorio. */
	private Integer idEjecucionObservatorio;

	/** The nombre. */
	private String nombre;

	/** The url. */
	private String url;

	/** The total url. */
	private int totalUrl;

	/** The total url analizadas. */
	private int totalUrlAnalizadas;

	/** The porcentaje completado. */
	private float porcentajeCompletado;

	/** The ultima url analizada. */
	private String ultimaUrl;

	/** The url actual en analisis. */
	private String actualUrl;

	/** The fecha ultima url analizad. */
	private Date fechaUltimaUrl;

	/** The tiempo medio. */
	private float tiempoMedio;

	/** The tiempo acumulado. */
	private float tiempoAcumulado;

	/** The tiempo estimado miniutos. */
	private float tiempoEstimado;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the id observatorio.
	 *
	 * @return the idObservatorio
	 */
	public Integer getIdObservatorio() {
		return idObservatorio;
	}

	/**
	 * Sets the id observatorio.
	 *
	 * @param idObservatorio
	 *            the idObservatorio to set
	 */
	public void setIdObservatorio(Integer idObservatorio) {
		this.idObservatorio = idObservatorio;
	}

	/**
	 * Gets the id ejecucion observatorio.
	 *
	 * @return the idEjecucionObservatorio
	 */
	public Integer getIdEjecucionObservatorio() {
		return idEjecucionObservatorio;
	}

	/**
	 * Sets the id ejecucion observatorio.
	 *
	 * @param idEjecucionObservatorio
	 *            the idEjecucionObservatorio to set
	 */
	public void setIdEjecucionObservatorio(Integer idEjecucionObservatorio) {
		this.idEjecucionObservatorio = idEjecucionObservatorio;
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
	 * Gets the total url.
	 *
	 * @return the totalUrl
	 */
	public int getTotalUrl() {
		return totalUrl;
	}

	/**
	 * Sets the total url.
	 *
	 * @param totalUrl
	 *            the totalUrl to set
	 */
	public void setTotalUrl(int totalUrl) {
		this.totalUrl = totalUrl;
	}

	/**
	 * Gets the ultima url.
	 *
	 * @return the ultimaUrl
	 */
	public String getUltimaUrl() {
		return ultimaUrl;
	}

	/**
	 * Sets the ultima url.
	 *
	 * @param ultimaUrl
	 *            the ultimaUrl to set
	 */
	public void setUltimaUrl(String ultimaUrl) {
		this.ultimaUrl = ultimaUrl;
	}

	/**
	 * Gets the actual url.
	 *
	 * @return the actualUrl
	 */
	public String getActualUrl() {
		return actualUrl;
	}

	/**
	 * Sets the actual url.
	 *
	 * @param actualUrl
	 *            the actualUrl to set
	 */
	public void setActualUrl(String actualUrl) {
		this.actualUrl = actualUrl;
	}

	/**
	 * Gets the fecha ultima url.
	 *
	 * @return the fechaUltimaUrl
	 */
	public Date getFechaUltimaUrl() {
		return fechaUltimaUrl;
	}

	/**
	 * Sets the fecha ultima url.
	 *
	 * @param fechaUltimaUrl
	 *            the fechaUltimaUrl to set
	 */
	public void setFechaUltimaUrl(Date fechaUltimaUrl) {
		this.fechaUltimaUrl = fechaUltimaUrl;
	}

	/**
	 * Gets the tiempo medio.
	 *
	 * @return the tiempoMedio
	 */
	public float getTiempoMedio() {
		return tiempoMedio;
	}

	/**
	 * Sets the tiempo medio.
	 *
	 * @param tiempoMedio
	 *            the tiempoMedio to set
	 */
	public void setTiempoMedio(float tiempoMedio) {
		this.tiempoMedio = tiempoMedio;
	}

	/**
	 * Gets the total url analizadas.
	 *
	 * @return the total url analizadas
	 */
	public int getTotalUrlAnalizadas() {
		return totalUrlAnalizadas;
	}

	/**
	 * Sets the total url analizadas.
	 *
	 * @param totalUrlAnalizadas
	 *            the new total url analizadas
	 */
	public void setTotalUrlAnalizadas(int totalUrlAnalizadas) {
		this.totalUrlAnalizadas = totalUrlAnalizadas;
	}

	/**
	 * Gets the porcentaje completado.
	 *
	 * @return the porcentaje completado
	 */
	public float getPorcentajeCompletado() {
		return porcentajeCompletado;
	}

	/**
	 * Sets the porcentaje completado.
	 *
	 * @param porcentajeCompletado
	 *            the new porcentaje completado
	 */
	public void setPorcentajeCompletado(float porcentajeCompletado) {
		this.porcentajeCompletado = porcentajeCompletado;
	}

	/**
	 * Gets the tiempo acumulado.
	 *
	 * @return the tiempo acumulado
	 */
	public float getTiempoAcumulado() {
		return tiempoAcumulado;
	}

	/**
	 * Sets the tiempo acumulado.
	 *
	 * @param tiempoAcumulado
	 *            the new tiempo acumulado
	 */
	public void setTiempoAcumulado(float tiempoAcumulado) {
		this.tiempoAcumulado = tiempoAcumulado;
	}

	/**
	 * Gets the tiempo estimado.
	 *
	 * @return the tiempo estimado
	 */
	public float getTiempoEstimado() {
		return tiempoEstimado;
	}

	/**
	 * Sets the tiempo estimado.
	 *
	 * @param tiempoEstimado
	 *            the new tiempo estimado
	 */
	public void setTiempoEstimado(float tiempoEstimado) {
		this.tiempoEstimado = tiempoEstimado;
	}

}