package es.gob.oaw.webservice.dto;

public class ScheduledTaskDTO {
	private String email;
	private String urls;
	private int pages;
	private boolean checkOutside;
	private boolean depthReport;
	private boolean brokenLinks;
	private boolean accesibilityChecks;
	private boolean simplifiedTracking;
	private boolean une2012;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public boolean isCheckOutside() {
		return checkOutside;
	}

	public void setCheckOutside(boolean checkOutside) {
		this.checkOutside = checkOutside;
	}

	public boolean isDepthReport() {
		return depthReport;
	}

	public void setDepthReport(boolean depthReport) {
		this.depthReport = depthReport;
	}

	public boolean isBrokenLinks() {
		return brokenLinks;
	}

	public void setBrokenLinks(boolean brokenLinks) {
		this.brokenLinks = brokenLinks;
	}

	public boolean isAccesibilityChecks() {
		return accesibilityChecks;
	}

	public void setAccesibilityChecks(boolean accesibilityChecks) {
		this.accesibilityChecks = accesibilityChecks;
	}

	public boolean isSimplifiedTracking() {
		return simplifiedTracking;
	}

	public void setSimplifiedTracking(boolean simplifiedTracking) {
		this.simplifiedTracking = simplifiedTracking;
	}

	public boolean isUne2012() {
		return une2012;
	}

	public void setUne2012(boolean une2012) {
		this.une2012 = une2012;
	}
}
