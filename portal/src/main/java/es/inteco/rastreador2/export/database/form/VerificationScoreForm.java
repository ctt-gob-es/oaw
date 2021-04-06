package es.inteco.rastreador2.export.database.form;

import java.math.BigDecimal;

/**
 * The Class VerificationScoreForm.
 */
public class VerificationScoreForm {
	/** The id. */
	private Long id;
	/** The verification. */
	private String verification;
	/** The score. */
	private BigDecimal score;

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
	 * Gets the score.
	 *
	 * @return the score
	 */
	public BigDecimal getScore() {
		return score;
	}

	/**
	 * Sets the score.
	 *
	 * @param score the score to set
	 */
	public void setScore(BigDecimal score) {
		this.score = score;
	}
}
