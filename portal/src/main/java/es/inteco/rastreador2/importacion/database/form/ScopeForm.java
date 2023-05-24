package es.inteco.rastreador2.importacion.database.form;

import java.util.Set;

public class ScopeForm extends BaseForm {
	private LabelForm etiqueta;
	private String acronimo;
	private String emails;
	private Boolean oficial;
	private Boolean envioAutomatico;
	private Set<AdministrativeLevelForm> ambitos;

	public LabelForm getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(LabelForm etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public Boolean isOficial() {
		return oficial;
	}

	public void setOficial(Boolean oficial) {
		this.oficial = oficial;
	}

	public Boolean isEnvioAutomatico() {
		return envioAutomatico;
	}

	public void setEnvioAutomatico(Boolean envioAutomatico) {
		this.envioAutomatico = envioAutomatico;
	}

	public Set<AdministrativeLevelForm> getAmbitos() {
		return ambitos;
	}

	public void setAmbitos(Set<AdministrativeLevelForm> ambitos) {
		this.ambitos = ambitos;
	}
}
