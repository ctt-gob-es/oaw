package es.inteco.rastreador2.dao.importar.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_lista")
public class SeedType {
	@Id
	@Column(name = "id_tipo")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@OneToMany(mappedBy = "tipoSemilla")
	private Set<Seed> semilla;

	public Set<Seed> getSemilla() {
		return semilla;
	}

	public void setSemilla(Set<Seed> semilla) {
		this.semilla = semilla;
	}

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
}
