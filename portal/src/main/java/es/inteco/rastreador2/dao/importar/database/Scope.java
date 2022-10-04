package es.inteco.rastreador2.dao.importar.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ambitos_lista")
public class Scope {
	@Id
	@Column(name = "id_ambito")
	private Long id;
	@Column(name = "nombre")
	private String nombre;

	/**
	 * @Column(name = "id_etiqueta", nullable = false) private Label etiqueta;
	 * @Column(name = "id_etiqueta", nullable = false) private String acronimo;
	 * @Column(name = "id_etiqueta", nullable = false) private String emails;
	 * @Column(name = "id_etiqueta", nullable = false) private boolean oficial;
	 * @Column(name = "id_etiqueta", nullable = false) private boolean envioAutomatico;
	 * @Column(name = "id_etiqueta", nullable = false) private Set<AdministrativeLevel> ambitos;
	 **/
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
