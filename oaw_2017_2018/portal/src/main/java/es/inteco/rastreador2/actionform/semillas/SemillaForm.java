package es.inteco.rastreador2.actionform.semillas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class SemillaForm extends ValidatorForm implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String nombre;
	private String nombre_antiguo;
	private List<String> listaUrls;
	private String listaUrlsString;
	private String dependencia;
	private String acronimo;
	private boolean asociada;
	private boolean activa;
	private String activaStr;
	private long rastreoAsociado;
	private CategoriaForm categoria;
	private boolean inDirectory;
	private String inDirectoryStr;

	private List<DependenciaForm> dependencias;

	public SemillaForm() {
		this.activa = true;
	}

	public boolean isAsociada() {
		return asociada;
	}

	public void setAsociada(boolean asociada) {
		this.asociada = asociada;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<String> getListaUrls() {
		return listaUrls;
	}

	public void setListaUrls(List<String> listaUrls) {
		this.listaUrls = listaUrls;
	}

	public void addListUrl(String url) {
		if (this.listaUrls == null) {
			this.listaUrls = new ArrayList<>();
		}
		this.listaUrls.add(url);
	}

	public String getListaUrlsString() {
		return listaUrlsString;
	}

	public void setListaUrlsString(String listaUrlsString) {
		this.listaUrlsString = listaUrlsString;
	}

	public String getNombre_antiguo() {
		return nombre_antiguo;
	}

	public void setNombre_antiguo(String nombre_antiguo) {
		this.nombre_antiguo = nombre_antiguo;
	}

	public long getRastreoAsociado() {
		return rastreoAsociado;
	}

	public void setRastreoAsociado(long rastreoAsociado) {
		this.rastreoAsociado = rastreoAsociado;
	}

	public CategoriaForm getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaForm categoria) {
		this.categoria = categoria;
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		if (categoria == null) {
			categoria = new CategoriaForm();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SemillaForm that = (SemillaForm) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	public String getDependencia() {
		return dependencia;
	}

	public void setDependencia(String dependencia) {
		this.dependencia = dependencia;
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
	
	
	public void addDependencia(DependenciaForm dependencia) {
		if (this.dependencias == null) {
			this.dependencias = new ArrayList<DependenciaForm>();
		}
		this.dependencias.add(dependencia);
	}
	

	@Override
	public String toString() {
		return "SemillaForm [id=" + id + ", nombre=" + nombre + ", nombre_antiguo=" + nombre_antiguo + ", listaUrls="
				+ listaUrls + ", listaUrlsString=" + listaUrlsString + ", dependencia=" + dependencia + ", acronimo="
				+ acronimo + ", asociada=" + asociada + ", activa=" + activa + ", activaStr=" + activaStr
				+ ", rastreoAsociado=" + rastreoAsociado + ", categoria=" + categoria + ", inDirectory=" + inDirectory
				+ ", inDirectoryStr=" + inDirectoryStr + ", dependencias=" + dependencias + "]";
	}

}