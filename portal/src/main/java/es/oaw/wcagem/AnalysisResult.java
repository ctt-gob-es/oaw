package es.oaw.wcagem;

public class AnalysisResult {
	private String url;
	private String error;
	private String description;
	private String solution;

	public String getUrl() {
		return url;
	}

	public String getError() {
		return error;
	}

	public String getDescription() {
		return description;
	}

	public String getSolution() {
		return solution;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}
}
