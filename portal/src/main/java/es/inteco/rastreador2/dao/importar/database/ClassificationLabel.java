package es.inteco.rastreador2.dao.importar.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "clasificacion_etiqueta")
public class ClassificationLabel {
	@Id
	@Column(name = "id_clasificacion")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@OneToMany(mappedBy = "clasificacionEtiqueta")
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

	public Set<Label> getEtiquetas() {
		return etiquetas;
	}

	public void setEtiquetas(Set<Label> etiquetas) {
		this.etiquetas = etiquetas;
	}
}
