package es.inteco.rastreador2.dao.importar.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lista")
public class Seed {
	@Id
	@Column(name = "id_lista")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "acronimo")
	private String acronimo;
	@Column(name = "activa")
	private Boolean activa;
	@Column(name = "in_directory")
	private Boolean enDirectorio;
	@Column(name = "eliminar")
	private Boolean eliminada;
	@Column(name = "observaciones")
	private String observaciones;
	@Column(name = "lista")
	private String lista;
	@ManyToOne
	@JoinColumn(name = "id_tipo_lista")
	private SeedType tipoSemilla;
	@ManyToOne
	@JoinColumn(name = "id_complejidad")
	private Complexity complejidad;
	@ManyToOne
	@JoinColumn(name = "id_categoria")
	private Segment segmento;
	@ManyToOne
	@JoinColumn(name = "id_ambito")
	private AdministrativeLevel ambito;
	@ManyToMany
	@JoinTable(name = "semilla_dependencia", joinColumns = @JoinColumn(name = "id_lista"), inverseJoinColumns = @JoinColumn(name = "id_dependencia"))
	private Set<Scope> dependencias;
	@ManyToMany
	@JoinTable(name = "semilla_etiqueta", joinColumns = @JoinColumn(name = "id_lista"), inverseJoinColumns = @JoinColumn(name = "id_etiqueta"))
	private Set<Label> etiquetas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public Boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(Boolean eliminada) {
		this.eliminada = eliminada;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getLista() {
		return lista;
	}

	public void setLista(String lista) {
		this.lista = lista;
	}

	public SeedType getTipoSemilla() {
		return tipoSemilla;
	}

	public void setTipoSemilla(SeedType tipoSemilla) {
		this.tipoSemilla = tipoSemilla;
	}

	public Boolean isEnDirectorio() {
		return enDirectorio;
	}

	public void setEnDirectorio(Boolean enDirectorio) {
		this.enDirectorio = enDirectorio;
	}

	public Complexity getComplejidad() {
		return complejidad;
	}

	public void setComplejidad(Complexity complejidad) {
		this.complejidad = complejidad;
	}

	public AdministrativeLevel getAmbito() {
		return ambito;
	}

	public void setAmbito(AdministrativeLevel ambito) {
		this.ambito = ambito;
	}

	public Set<Label> getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(Set<Label> etiquetas) {
		this.etiquetas = etiquetas;
	}

	public Set<Scope> getDependencias() {
		return dependencias;
	}

	public void setDependencias(Set<Scope> dependencias) {
		this.dependencias = dependencias;
	}

	public Segment getSegmento() {
		return segmento;
	}

	public void setSegmento(Segment segmento) {
		this.segmento = segmento;
	}
}
