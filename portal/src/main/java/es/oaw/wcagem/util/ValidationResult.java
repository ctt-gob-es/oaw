package es.oaw.wcagem.util;

/**
 * The Class ValidationResult.
 */
public class ValidationResult {
	/** The oaw verification. */
	private String oawVerification;
	/** The oaw description. */
	private String oawDescription;
	/** The result. */
	private String result;

	/**
	 * Gets the oaw verification.
	 *
	 * @return the oaw verification
	 */
	public String getOawVerification() {
		return oawVerification;
	}

	/**
	 * Sets the oaw verification.
	 *
	 * @param oawVerification the new oaw verification
	 */
	public void setOawVerification(String oawVerification) {
		this.oawVerification = oawVerification;
	}

	/**
	 * Gets the oaw description.
	 *
	 * @return the oaw description
	 */
	public String getOawDescription() {
		return oawDescription;
	}

	/**
	 * Sets the oaw description.
	 *
	 * @param oawDescription the new oaw description
	 */
	public void setOawDescription(String oawDescription) {
		this.oawDescription = oawDescription;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ValidationResult [oawVerification=" + oawVerification + ", oawDescription=" + oawDescription + ", result=" + result + "]";
	}
}