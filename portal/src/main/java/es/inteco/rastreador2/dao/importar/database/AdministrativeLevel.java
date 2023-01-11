package es.inteco.rastreador2.dao.importar.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ambitos_lista")
public class AdministrativeLevel {
	@Id
	@Column(name = "id_ambito")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "descripcion")
	private String descripcion;
	@OneToMany(mappedBy = "ambito")
	private Set<Seed> semillas;
	@ManyToMany(mappedBy = "ambitos")
	private Set<Scope> dependencias;

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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Set<Seed> getSemillas() {
		return semillas;
	}

	public void setSemillas(Set<Seed> semillas) {
		this.semillas = semillas;
	}

	public Set<Scope> getDependencias() {
		return dependencias;
	}

	public void setDependencias(Set<Scope> dependencias) {
		this.dependencias = dependencias;
	}
}
