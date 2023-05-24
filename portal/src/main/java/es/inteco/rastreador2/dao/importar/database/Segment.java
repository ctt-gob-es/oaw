package es.inteco.rastreador2.dao.importar.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categorias_lista")
public class Segment {
	@Id
	@Column(name = "id_categoria")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "orden")
	private Long orden;
	@Column(name = "clave")
	private String clave;
	@Column(name = "principal")
	private Long principal;
	@OneToMany(mappedBy = "segmento")
	private Set<Seed> semillas;

	public Set<Seed> getSemillas() {
		return semillas;
	}

	public void setSemillas(Set<Seed> semillas) {
		this.semillas = semillas;
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

	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Long getPrincipal() {
		return principal;
	}

	public void setPrincipal(Long principal) {
		this.principal = principal;
	}
}
