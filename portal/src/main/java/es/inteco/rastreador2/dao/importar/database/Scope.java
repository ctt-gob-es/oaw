package es.inteco.rastreador2.dao.importar.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	private boolean envioAutomatico;
	@Column(name = "official")
	private boolean oficial;
	@ManyToMany(mappedBy = "dependencias", fetch = FetchType.EAGER)
	private Set<Seed> semillas;

	/**
	 * @ManyToMany(mappedBy = "dependencias") private Set<AdministrativeLevel> ambitos;
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

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public boolean isEnvioAutomatico() {
		return envioAutomatico;
	}

	public void setEnvioAutomatico(boolean envioAutomatico) {
		this.envioAutomatico = envioAutomatico;
	}

	public boolean isOficial() {
		return oficial;
	}

	public void setOficial(boolean oficial) {
		this.oficial = oficial;
	}

	public Set<Seed> getSemillas() {
		return semillas;
	}

	public void setSemillas(Set<Seed> semillas) {
		this.semillas = semillas;
	}
	/**
	 * public Set<AdministrativeLevel> getAmbitos() { return ambitos; }
	 * 
	 * public void setAmbitos(Set<AdministrativeLevel> ambitos) { this.ambitos = ambitos; }
	 **/
}
