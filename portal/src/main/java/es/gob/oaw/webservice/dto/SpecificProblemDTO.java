package es.gob.oaw.webservice.dto;

import java.util.List;

public class SpecificProblemDTO {
	/** The line. */
	private String line;
	/** The column. */
	private String column;
	/** The code. */
	private String code;

	/**
	 * Instantiates a new specific problem form.
	 */
	public SpecificProblemDTO() {
	}

	/**
	 * Gets the line.
	 *
	 * @return the line
	 */
	public String getLine() {
		return line;
	}

	/**
	 * Sets the line.
	 *
	 * @param line the new line
	 */
	public void setLine(String line) {
		this.line = line;
	}

	/**
	 * Gets the column.
	 *
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * Sets the column.
	 *
	 * @param column the new column
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(List<String> code) {
		String formatCode = String.join("", code);
		formatCode = formatCode.replace("&lt;", "<");
		formatCode = formatCode.replace("&gt;", ">");
		this.code = formatCode;
	}
}
