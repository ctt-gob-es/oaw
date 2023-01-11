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
@Table(name = "dependencia")
public class Scope {
	@Id
	@Column(name = "id_dependencia")
	private Long id;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "emails")
	private String emails;
	@Column(name = "send_auto")
	private Boolean envioAutomatico;
	@Column(name = "official")
	private Boolean oficial;
	@Column(name = "acronym")
	private String acronimo;
	@ManyToMany(mappedBy = "dependencias")
	private Set<Seed> semillas;
	@ManyToMany
	@JoinTable(name = "dependencia_ambito", joinColumns = @JoinColumn(name = "id_dependencia"), inverseJoinColumns = @JoinColumn(name = "id_ambito"))
	private Set<AdministrativeLevel> ambitos;
	@ManyToOne
	@JoinColumn(name = "id_tag")
	private Label etiqueta;

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

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public Boolean isEnvioAutomatico() {
		return envioAutomatico;
	}

	public void setEnvioAutomatico(Boolean envioAutomatico) {
		this.envioAutomatico = envioAutomatico;
	}

	public Boolean isOficial() {
		return oficial;
	}

	public void setOficial(Boolean oficial) {
		this.oficial = oficial;
	}

	public Set<Seed> getSemillas() {
		return semillas;
	}

	public void setSemillas(Set<Seed> semillas) {
		this.semillas = semillas;
	}

	public Set<AdministrativeLevel> getAmbitos() {
		return ambitos;
	}

	public void setAmbitos(Set<AdministrativeLevel> ambitos) {
		this.ambitos = ambitos;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public Label getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(Label etiqueta) {
		this.etiqueta = etiqueta;
	}
}
