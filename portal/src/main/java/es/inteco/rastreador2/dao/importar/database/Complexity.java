package es.inteco.rastreador2.dao.importar.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "complejidades_lista")
public class Complexity {
	@Id
	@Column(name = "id_complejidad")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "profundidad")
	private Integer profundidad;
	@Column(name = "amplitud")
	private Integer amplitud;
	@OneToMany(mappedBy = "complejidad")
	private Set<Seed> semillas;

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

	public Integer getProfundidad() {
		return profundidad;
	}

	public void setProfundidad(Integer profundidad) {
		this.profundidad = profundidad;
	}

	public Integer getAmplitud() {
		return amplitud;
	}

	public void setAmplitud(Integer amplitud) {
		this.amplitud = amplitud;
	}

	public Set<Seed> getSemillas() {
		return semillas;
	}

	public void setSemilla(Set<Seed> semillas) {
		this.semillas = semillas;
	}
}
