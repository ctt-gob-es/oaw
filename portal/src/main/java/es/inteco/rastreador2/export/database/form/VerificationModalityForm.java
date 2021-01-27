package es.inteco.rastreador2.export.database.form;

import java.math.BigDecimal;

/**
 * The Class VerificationModalityForm.
 */
public class VerificationModalityForm {
	/** The id. */
	private Long id;
	/** The verification. */
	private String verification;
	/** The fail percentage. */
	private BigDecimal failPercentage;
	/** The pass percentage. */
	private BigDecimal passPercentage;

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
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the verification.
	 *
	 * @return the verification
	 */
	public String getVerification() {
		return verification;
	}

	/**
	 * Sets the verification.
	 *
	 * @param verification the verification to set
	 */
	public void setVerification(String verification) {
		this.verification = verification;
	}

	/**
	 * Gets the fail percentage.
	 *
	 * @return the failPercentage
	 */
	public BigDecimal getFailPercentage() {
		return failPercentage;
	}

	/**
	 * Sets the fail percentage.
	 *
	 * @param failPercentage the failPercentage to set
	 */
	public void setFailPercentage(BigDecimal failPercentage) {
		this.failPercentage = failPercentage;
	}

	/**
	 * Gets the pass percentage.
	 *
	 * @return the passPercentage
	 */
	public BigDecimal getPassPercentage() {
		return passPercentage;
	}

	/**
	 * Sets the pass percentage.
	 *
	 * @param passPercentage the passPercentage to set
	 */
	public void setPassPercentage(BigDecimal passPercentage) {
		this.passPercentage = passPercentage;
	}
}
