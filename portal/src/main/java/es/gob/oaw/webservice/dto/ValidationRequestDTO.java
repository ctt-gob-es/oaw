package es.gob.oaw.webservice.dto;

public class ValidationRequestDTO {
	private String htmlContent;
	private String url;
	private String methodology;
	private boolean brokenLinks;

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethodology() {
		return methodology;
	}

	public void setMethodology(String methodology) {
		this.methodology = methodology;
	}

	public boolean isBrokenLinks() {
		return brokenLinks;
	}

	public void setBrokenLinks(boolean brokenLinks) {
		this.brokenLinks = brokenLinks;
	}
}
