package es.inteco.rastreador2.actionform.observatorio;

import java.util.List;

import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;

public class ResultadoSemillaFullForm extends ResultadoSemillaForm {

	private String nombre;
	private String nombre_antiguo;
	private List<String> listaUrls;
	private String listaUrlsString;
	private String acronimo;
	private boolean activa;
	private String activaStr;
	private CategoriaForm categoria;
	private boolean inDirectory;
	private String inDirectoryStr;

	private List<DependenciaForm> dependencias;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre_antiguo() {
		return nombre_antiguo;
	}

	public void setNombre_antiguo(String nombre_antiguo) {
		this.nombre_antiguo = nombre_antiguo;
	}

	public List<String> getListaUrls() {
		return listaUrls;
	}

	public void setListaUrls(List<String> listaUrls) {
		this.listaUrls = listaUrls;
	}

	public String getListaUrlsString() {
		return listaUrlsString;
	}

	public void setListaUrlsString(String listaUrlsString) {
		this.listaUrlsString = listaUrlsString;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public String getActivaStr() {
		return activaStr;
	}

	public void setActivaStr(String activaStr) {
		this.activaStr = activaStr;
	}

	public CategoriaForm getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaForm categoria) {
		this.categoria = categoria;
	}

	public boolean isInDirectory() {
		return inDirectory;
	}

	public void setInDirectory(boolean inDirectory) {
		this.inDirectory = inDirectory;
	}

	public String getInDirectoryStr() {
		return inDirectoryStr;
	}

	public void setInDirectoryStr(String inDirectoryStr) {
		this.inDirectoryStr = inDirectoryStr;
	}

	public List<DependenciaForm> getDependencias() {
		return dependencias;
	}

	public void setDependencias(List<DependenciaForm> dependencias) {
		this.dependencias = dependencias;
	}

}
