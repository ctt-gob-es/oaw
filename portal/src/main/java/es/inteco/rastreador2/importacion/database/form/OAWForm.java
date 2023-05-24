package es.inteco.rastreador2.importacion.database.form;

import java.util.List;

public class OAWForm {
	private List<ClassificationLabelForm> clasificacionEtiquetas;
	private List<SeedTypeForm> tipoSemillas;
	private List<SegmentForm> segmentos;
	private List<ComplexityForm> complejidades;
	private List<LabelForm> etiquetas;
	private List<AdministrativeLevelForm> ambitos;
	private List<ScopeForm> dependencias;
	private List<SeedForm> semillas;

	public List<ClassificationLabelForm> getClasificacionEtiquetas() {
		return clasificacionEtiquetas;
	}

	public void setClasificacionEtiquetas(List<ClassificationLabelForm> clasificacionEtiquetas) {
		this.clasificacionEtiquetas = clasificacionEtiquetas;
	}

	public List<SeedTypeForm> getTipoSemillas() {
		return tipoSemillas;
	}

	public void setTipoSemillas(List<SeedTypeForm> tipoSemillas) {
		this.tipoSemillas = tipoSemillas;
	}

	public List<SegmentForm> getSegmentos() {
		return segmentos;
	}

	public void setSegmentos(List<SegmentForm> segmentos) {
		this.segmentos = segmentos;
	}

	public List<ComplexityForm> getComplejidades() {
		return complejidades;
	}

	public void setComplejidades(List<ComplexityForm> complejidades) {
		this.complejidades = complejidades;
	}

	public List<LabelForm> getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(List<LabelForm> etiquetas) {
		this.etiquetas = etiquetas;
	}

	public List<AdministrativeLevelForm> getAmbitos() {
		return ambitos;
	}

	public void setAmbitos(List<AdministrativeLevelForm> ambitos) {
		this.ambitos = ambitos;
	}

	public List<ScopeForm> getDependencias() {
		return dependencias;
	}

	public void setDependencias(List<ScopeForm> dependencias) {
		this.dependencias = dependencias;
	}

	public List<SeedForm> getSemillas() {
		return semillas;
	}

	public void setSemillas(List<SeedForm> semillas) {
		this.semillas = semillas;
	}
}
