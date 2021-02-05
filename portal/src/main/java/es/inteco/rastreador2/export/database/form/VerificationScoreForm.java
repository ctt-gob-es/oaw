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
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the verification
	 */
	public String getVerification() {
		return verification;
	}

	/**
	 * @param verification the verification to set
	 */
	public void setVerification(String verification) {
		this.verification = verification;
	}

	/**
	 * @return the score
	 */
	public BigDecimal getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(BigDecimal score) {
		this.score = score;
	}
}
