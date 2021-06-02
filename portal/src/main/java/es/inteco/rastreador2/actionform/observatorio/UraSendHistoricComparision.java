/*
 * 
 */
package es.inteco.rastreador2.actionform.observatorio;

import java.io.Serializable;

import org.apache.struts.validator.ValidatorForm;

/**
 * The Class UraSendResultForm.
 */
public class UraSendHistoricComparision extends ValidatorForm implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7136943908871236927L;
	/** The id. */
	private Long id;
	/** The id send historic. */
	private Long idSendHistoric;
	/** The id tag. */
	private int idTag;
	/** The tag name. */
	private String tagName;
	/** The previous date. */
	private String previous;

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
	 * Gets the id tag.
	 *
	 * @return the id tag
	 */
	public int getIdTag() {
		return idTag;
	}

	/**
	 * Sets the id tag.
	 *
	 * @param idTag the new id tag
	 */
	public void setIdTag(int idTag) {
		this.idTag = idTag;
	}

	/**
	 * Gets the tag name.
	 *
	 * @return the tag name
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * Sets the tag name.
	 *
	 * @param tagName the new tag name
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Gets the previous.
	 *
	 * @return the previous
	 */
	public String getPrevious() {
		return previous;
	}

	/**
	 * Sets the previous.
	 *
	 * @param previous the new previous
	 */
	public void setPrevious(String previous) {
		this.previous = previous;
	}
}
