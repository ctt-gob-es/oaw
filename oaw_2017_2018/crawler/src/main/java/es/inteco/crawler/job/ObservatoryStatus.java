package es.inteco.crawler.job;

import java.util.Date;

/**
 * The Class EstadoObservatorioAnalisisData.
 */
public class ObservatoryStatus {

	
	private Integer id;
	
	private Integer idObservatorio;
	
	private Integer idEjecucionObservatorio;
	
	/** The nombre. */
	private String nombre;

	/** The url. */
	private String url;

	/** The total url. */
	private int totalUrl;

	/** The ultima url analizada. */
	private String ultimaUrl;

	/** The url actual en analisis. */
	private String actualUrl;

	/** The fecha ultima url analizad. */
	private Date fechaUltimaUrl;

	/** The tiempo medio. */
	private float tiempoMedio;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the idObservatorio
	 */
	public Integer getIdObservatorio() {
		return idObservatorio;
	}

	/**
	 * @param idObservatorio the idObservatorio to set
	 */
	public void setIdObservatorio(Integer idObservatorio) {
		this.idObservatorio = idObservatorio;
	}

	/**
	 * @return the idEjecucionObservatorio
	 */
	public Integer getIdEjecucionObservatorio() {
		return idEjecucionObservatorio;
	}

	/**
	 * @param idEjecucionObservatorio the idEjecucionObservatorio to set
	 */
	public void setIdEjecucionObservatorio(Integer idEjecucionObservatorio) {
		this.idEjecucionObservatorio = idEjecucionObservatorio;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the totalUrl
	 */
	public int getTotalUrl() {
		return totalUrl;
	}

	/**
	 * @param totalUrl the totalUrl to set
	 */
	public void setTotalUrl(int totalUrl) {
		this.totalUrl = totalUrl;
	}

	/**
	 * @return the ultimaUrl
	 */
	public String getUltimaUrl() {
		return ultimaUrl;
	}

	/**
	 * @param ultimaUrl the ultimaUrl to set
	 */
	public void setUltimaUrl(String ultimaUrl) {
		this.ultimaUrl = ultimaUrl;
	}

	/**
	 * @return the actualUrl
	 */
	public String getActualUrl() {
		return actualUrl;
	}

	/**
	 * @param actualUrl the actualUrl to set
	 */
	public void setActualUrl(String actualUrl) {
		this.actualUrl = actualUrl;
	}

	/**
	 * @return the fechaUltimaUrl
	 */
	public Date getFechaUltimaUrl() {
		return fechaUltimaUrl;
	}

	/**
	 * @param fechaUltimaUrl the fechaUltimaUrl to set
	 */
	public void setFechaUltimaUrl(Date fechaUltimaUrl) {
		this.fechaUltimaUrl = fechaUltimaUrl;
	}

	/**
	 * @return the tiempoMedio
	 */
	public float getTiempoMedio() {
		return tiempoMedio;
	}

	/**
	 * @param tiempoMedio the tiempoMedio to set
	 */
	public void setTiempoMedio(float tiempoMedio) {
		this.tiempoMedio = tiempoMedio;
	}


}