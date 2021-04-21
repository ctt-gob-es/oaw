package es.inteco.rastreador2.actionform.observatorio;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts.validator.ValidatorForm;

import es.inteco.rastreador2.actionform.semillas.DependenciaForm;

/**
 * The Class UraSendResultForm.
 */
public class UraSendHistoricResults extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4467598358767303266L;
	/** The id. */
	private Long id;
	/** The id observatory execution. */
	private Long idSendHistoric;
	/** The id ura. */
	private Long idUra;
	/** The id range. */
	private Long idRange;
	/** The template. */
	private String template;
	/** The ura. */
	private DependenciaForm ura;
	/** The range. */
	private RangeForm range;
	/** The ura name. */
	private String uraName;
	/** The range name. */
	private String rangeName;
	/** The ura id. */
	private Long uraId;
	/** The range value. */
	private Float rangeValue;
	/** The send. */
	private boolean send;
	/** The send date. */
	private Date sendDate;
	/** The send error. */
	private String sendError;
	/** The file link. */
	private String fileLink;
	/** The file pass. */
	private String filePass;
	/** The mail. */
	private String mail;
	/** The valid date. */
	private Date validDate;

	/**
	 * Gets the file link.
	 *
	 * @return the file link
	 */
	public String getFileLink() {
		return fileLink;
	}

	/**
	 * Sets the file link.
	 *
	 * @param fileLink the new file link
	 */
	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	/**
	 * Gets the file pass.
	 *
	 * @return the file pass
	 */
	public String getFilePass() {
		return filePass;
	}

	/**
	 * Sets the file pass.
	 *
	 * @param filePass the new file pass
	 */
	public void setFilePass(String filePass) {
		this.filePass = filePass;
	}

	/**
	 * Gets the send error.
	 *
	 * @return the send error
	 */
	public String getSendError() {
		return sendError;
	}

	/**
	 * Sets the send error.
	 *
	 * @param sendError the new send error
	 */
	public void setSendError(String sendError) {
		this.sendError = sendError;
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
	 * Checks if is send.
	 *
	 * @return true, if is send
	 */
	public boolean isSend() {
		return send;
	}

	/**
	 * Sets the send.
	 *
	 * @param send the new send
	 */
	public void setSend(boolean send) {
		this.send = send;
	}

	/**
	 * Gets the id send historic.
	 *
	 * @return the id send historic
	 */
	public Long getIdSendHistoric() {
		return idSendHistoric;
	}

	/**
	 * Sets the id send historic.
	 *
	 * @param idSendHistoric the new id send historic
	 */
	public void setIdSendHistoric(Long idSendHistoric) {
		this.idSendHistoric = idSendHistoric;
	}

	/**
	 * Gets the id ura.
	 *
	 * @return the id ura
	 */
	public Long getIdUra() {
		return idUra;
	}

	/**
	 * Sets the id ura.
	 *
	 * @param idUra the new id ura
	 */
	public void setIdUra(Long idUra) {
		this.idUra = idUra;
	}

	/**
	 * Gets the id range.
	 *
	 * @return the id range
	 */
	public Long getIdRange() {
		return idRange;
	}

	/**
	 * Sets the id range.
	 *
	 * @param idRange the new id range
	 */
	public void setIdRange(Long idRange) {
		this.idRange = idRange;
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Gets the ura.
	 *
	 * @return the ura
	 */
	public DependenciaForm getUra() {
		return ura;
	}

	/**
	 * Sets the ura.
	 *
	 * @param ura the new ura
	 */
	public void setUra(DependenciaForm ura) {
		this.ura = ura;
	}

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public RangeForm getRange() {
		return range;
	}

	/**
	 * Sets the range.
	 *
	 * @param range the new range
	 */
	public void setRange(RangeForm range) {
		this.range = range;
	}

	/**
	 * Gets the ura name.
	 *
	 * @return the ura name
	 */
	public String getUraName() {
		return uraName;
	}

	/**
	 * Sets the ura name.
	 *
	 * @param uraName the new ura name
	 */
	public void setUraName(String uraName) {
		this.uraName = uraName;
	}

	/**
	 * Gets the ura id.
	 *
	 * @return the ura id
	 */
	public Long getUraId() {
		return uraId;
	}

	/**
	 * Sets the ura id.
	 *
	 * @param uraId the new ura id
	 */
	public void setUraId(Long uraId) {
		this.uraId = uraId;
	}

	/**
	 * Gets the range value.
	 *
	 * @return the range value
	 */
	public Float getRangeValue() {
		return rangeValue;
	}

	/**
	 * Sets the range value.
	 *
	 * @param rangeValue the new range value
	 */
	public void setRangeValue(Float rangeValue) {
		this.rangeValue = rangeValue;
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
	 * Gets the mail.
	 *
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Sets the mail.
	 *
	 * @param mail the new mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * Gets the range name.
	 *
	 * @return the range name
	 */
	public String getRangeName() {
		return rangeName;
	}

	/**
	 * Sets the range name.
	 *
	 * @param rangeName the new range name
	 */
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	/**
	 * Gets the valid date.
	 *
	 * @return the valid date
	 */
	public Date getValidDate() {
		return validDate;
	}

	/**
	 * Sets the valid date.
	 *
	 * @param validDate the new valid date
	 */
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
}
