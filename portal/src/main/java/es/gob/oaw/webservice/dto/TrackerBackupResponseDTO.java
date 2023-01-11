package es.gob.oaw.webservice.dto;

public class TrackerBackupResponseDTO {
	private String content;
	private String observations;
	private boolean validExport;

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isValidExport() {
		return validExport;
	}

	public void setValidExport(boolean validExport) {
		this.validExport = validExport;
	}
}
