package es.inteco.rastreador2.dao.importar.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "etiqueta")
public class Label {
	@Id
	@Column(name = "id_etiqueta")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@ManyToOne
	@JoinColumn(name = "id_clasificacion")
	private ClassificationLabel clasificacionEtiqueta;

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

	public ClassificationLabel getClasificacionEtiqueta() {
		return clasificacionEtiqueta;
	}

	public void setClasificacionEtiqueta(ClassificationLabel clasificacionEtiqueta) {
		this.clasificacionEtiqueta = clasificacionEtiqueta;
	}
}
