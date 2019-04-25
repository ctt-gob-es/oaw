package es.inteco.plugin.dao;

public class ExtraInfo {

	private int idEjecucionObservatorio;
	private String nombreLista;
	private String urlLista;

	public ExtraInfo(int idEjecucionObservatorio, String nombreLista, String urlLista) {
		super();
		this.idEjecucionObservatorio = idEjecucionObservatorio;
		this.nombreLista = nombreLista;
		this.urlLista = urlLista;
	}

	public int getIdEjecucionObservatorio() {
		return idEjecucionObservatorio;
	}

	public void setIdEjecucionObservatorio(int idEjecucionObservatorio) {
		this.idEjecucionObservatorio = idEjecucionObservatorio;
	}

	public String getNombreLista() {
		return nombreLista;
	}

	public void setNombreLista(String nombreLista) {
		this.nombreLista = nombreLista;
	}

	public String getUrlLista() {
		return urlLista;
	}

	public void setUrlLista(String urlLista) {
		this.urlLista = urlLista;
	}

}
