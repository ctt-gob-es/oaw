package es.inteco.rastreador2.dao.importar.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lista")
public class Seed {
	@Id
	@Column(name = "id_lista")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	// @Column(name = "id_tipo_lista", nullable = false)
	// private SeedType tipoSemilla;
	@Column(name = "acronimo")
	private String acronimo;
	@Column(name = "activa")
	private boolean activa;
	@Column(name = "id_directory")
	private boolean enDirectorio;
	@Column(name = "eliminar")
	private boolean eliminada;
	@Column(name = "observaciones")
	private String observaciones;
	@Column(name = "lista")
	private String lista;
	// @Column(name = "id_complejidad", nullable = false)
	// private Complexity complejidad;

	// @Column(name = "id_ambito", nullable = false)
	// private AdministrativeLevel ambito;
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
	/*
	 * public SeedType getTipoSemilla() { return tipoSemilla; }
	 * 
	 * public void setTipoSemilla(SeedType tipoSemilla) { this.tipoSemilla = tipoSemilla; }
	 */

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

	public boolean isEnDirectorio() {
		return enDirectorio;
	}

	public void setEnDirectorio(boolean enDirectorio) {
		this.enDirectorio = enDirectorio;
	}

	public boolean isEliminada() {
		return eliminada;
	}

	public void setEliminada(boolean eliminada) {
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
	/**
	 * public Complexity getComplejidad() { return complejidad; }
	 * 
	 * public void setComplejidad(Complexity complejidad) { this.complejidad = complejidad; }
	 **/
}
