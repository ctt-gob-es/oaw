package es.inteco.rastreador2.actionform.observatorio;

import java.io.Serializable;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class UraSendResultForm.
 */
public class UraSendHistoric extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7906403455383002683L;
	/** The id. */
	private Long id;
	private String idsExObs;
	private String cco;
	private String subject;

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

	public String getIdsExObs() {
		return idsExObs;
	}

	public void setIdsExObs(String idsExObs) {
		this.idsExObs = idsExObs;
	}

	public String getCco() {
		return cco;
	}

	public void setCco(String cco) {
		this.cco = cco;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
