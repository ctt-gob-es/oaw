package es.gob.oaw.rastreador2.actionform.diagnostico;

import java.util.Date;

import org.apache.struts.validator.ValidatorForm;

/**
 * Form para obtener los parámetros de exportación de datos de uso del servicio de diagnóstico.
 *
 * @author miguel.garcia
 */
public class ServicioDiagnosticoForm extends ValidatorForm {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4125504135425295622L;
	/** The id. */
	private Long id;
	/** The user. */
	private String user;
	/** The language. */
	private String language;
	/** The domain. */
	private String domain;
	/** The email. */
	private String email;
	/** The depth. */
	private Integer depth;
	/** The width. */
	private Integer width;
	/** The report. */
	private String report;
	/** The date. */
	private Date date;
	/** The status. */
	private String status;
	/** The send date. */
	private Date sendDate;
	/** The scheduling date. */
	private Date schedulingDate;
	/** The analysis type. */
	private String analysisType;
	/** The in directory. */
	private Integer inDirectory;
	/** The register result. */
	private Integer registerResult;
	/** The start date. */
	private String startDate;
	/** The end date. */
	private String endDate;
	/** The type. */
	private String type;

	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language.
	 *
	 * @param language the new language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the depth.
	 *
	 * @return the depth
	 */
	public Integer getDepth() {
		return depth;
	}

	/**
	 * Sets the depth.
	 *
	 * @param depth the new depth
	 */
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * Gets the report.
	 *
	 * @return the report
	 */
	public String getReport() {
		return report;
	}

	/**
	 * Sets the report.
	 *
	 * @param report the new report
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the send date.
	 *
	 * @return the send date
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * Sets the send date.
	 *
	 * @param sendDate the new send date
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * Gets the scheduling date.
	 *
	 * @return the scheduling date
	 */
	public Date getSchedulingDate() {
		return schedulingDate;
	}

	/**
	 * Sets the scheduling date.
	 *
	 * @param schedulingDate the new scheduling date
	 */
	public void setSchedulingDate(Date schedulingDate) {
		this.schedulingDate = schedulingDate;
	}

	/**
	 * Gets the analysis type.
	 *
	 * @return the analysis type
	 */
	public String getAnalysisType() {
		return analysisType;
	}

	/**
	 * Sets the analysis type.
	 *
	 * @param analysisType the new analysis type
	 */
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}

	/**
	 * Gets the in directory.
	 *
	 * @return the in directory
	 */
	public Integer getInDirectory() {
		return inDirectory;
	}

	/**
	 * Sets the in directory.
	 *
	 * @param inDirectory the new in directory
	 */
	public void setInDirectory(Integer inDirectory) {
		this.inDirectory = inDirectory;
	}

	/**
	 * Gets the register result.
	 *
	 * @return the register result
	 */
	public Integer getRegisterResult() {
		return registerResult;
	}

	/**
	 * Sets the register result.
	 *
	 * @param registerResult the new register result
	 */
	public void setRegisterResult(Integer registerResult) {
		this.registerResult = registerResult;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}
}
