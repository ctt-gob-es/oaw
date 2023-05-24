package es.gob.oaw.webservice.dto;

public class ImportDataResponseDTO {
	private boolean validImport;
	private String observations;

	public boolean isValidImport() {
		return validImport;
	}

	public void setValidImport(boolean validImport) {
		this.validImport = validImport;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}
}
