package es.inteco.rastreador2.importacion.database.form;

import java.util.Set;

public class SeedForm extends BaseForm {
	private SeedTypeForm tipoSemilla;
	private String acronimo;
	private Boolean activa;
	private Boolean enDirectorio;
	private Boolean eliminada;
	private Boolean rastreador;
	private String observaciones;
	private String observacionesRastreador;
	private String lista;
	private SegmentForm segmento;
	private ComplexityForm complejidad;
	private AdministrativeLevelForm ambito;
	private Set<ScopeForm> dependencias;
	private Set<LabelForm> etiquetas;

	public SeedTypeForm getTipoSemilla() {
		return tipoSemilla;
	}

	public void setTipoSemilla(SeedTypeForm tipoSemilla) {
		this.tipoSemilla = tipoSemilla;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public Boolean isActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}

	public Boolean isEnDirectorio() {
		return enDirectorio;
	}

	public void setEnDirectorio(Boolean enDirectorio) {
		this.enDirectorio = enDirectorio;
	}

	public Boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(Boolean eliminada) {
		this.eliminada = eliminada;
	}

	public Boolean isRastreador() {
		return rastreador;
	}

	public void setRastreador(Boolean rastreador) {
		this.rastreador = rastreador;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservacionesRastreador() {
		return observacionesRastreador;
	}

	public void setObservacionesRastreador(String observacionesRastreador) {
		this.observacionesRastreador = observacionesRastreador;
	}

	public String getLista() {
		return lista;
	}

	public void setLista(String lista) {
		this.lista = lista;
	}

	public SegmentForm getSegmento() {
		return segmento;
	}

	public void setSegmento(SegmentForm segmento) {
		this.segmento = segmento;
	}

	public ComplexityForm getComplejidad() {
		return complejidad;
	}

	public void setComplejidad(ComplexityForm complejidad) {
		this.complejidad = complejidad;
	}

	public AdministrativeLevelForm getAmbito() {
		return ambito;
	}

	public void setAmbito(AdministrativeLevelForm ambito) {
		this.ambito = ambito;
	}

	public Set<ScopeForm> getDependencias() {
		return dependencias;
	}

	public void setDependencias(Set<ScopeForm> dependencias) {
		this.dependencias = dependencias;
	}

	public Set<LabelForm> getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(Set<LabelForm> etiquetas) {
		this.etiquetas = etiquetas;
	}
}
