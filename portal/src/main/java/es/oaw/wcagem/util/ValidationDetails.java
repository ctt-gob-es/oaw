package es.oaw.wcagem.util;

import java.util.List;

/**
 * The Class ValidationDetails.
 */
public class ValidationDetails {
	/** The result. */
	private String result;
	/** The results. */
	private List<ValidationResult> results;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ValidationDetails [result=" + result + ", results=" + results + "]";
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
	 * Gets the results.
	 *
	 * @return the results
	 */
	public List<ValidationResult> getResults() {
		return results;
	}

	/**
	 * Sets the results.
	 *
	 * @param results the new results
	 */
	public void setResults(List<ValidationResult> results) {
		this.results = results;
	}
}