/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.common;

/**
 * The Class ValidationError.
 */
public class ValidationError {
	/** The type. */
	private int type;
	/** The column. */
	private int column;
	/** The line. */
	private int line;
	/** The code. */
	private String code;
	/** The num errors. */
	private int numErrors;
	/** The num warnings. */
	private int numWarnings;
	/** The summary. */
	private boolean summary;
	/** The message id. */
	private String messageId;

	/**
	 * Gets the column.
	 *
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Sets the column.
	 *
	 * @param column the new column
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * Gets the line.
	 *
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Sets the line.
	 *
	 * @param line the new line
	 */
	public void setLine(int line) {
		this.line = line;
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
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the num errors.
	 *
	 * @return the num errors
	 */
	public int getNumErrors() {
		return numErrors;
	}

	/**
	 * Sets the num errors.
	 *
	 * @param numErrors the new num errors
	 */
	public void setNumErrors(int numErrors) {
		this.numErrors = numErrors;
	}

	/**
	 * Gets the num warnings.
	 *
	 * @return the num warnings
	 */
	public int getNumWarnings() {
		return numWarnings;
	}

	/**
	 * Sets the num warnings.
	 *
	 * @param numWarnings the new num warnings
	 */
	public void setNumWarnings(int numWarnings) {
		this.numWarnings = numWarnings;
	}

	/**
	 * Checks if is summary.
	 *
	 * @return true, if is summary
	 */
	public boolean isSummary() {
		return summary;
	}

	/**
	 * Sets the summary.
	 *
	 * @param summary the new summary
	 */
	public void setSummary(boolean summary) {
		this.summary = summary;
	}

	/**
	 * Gets the message id.
	 *
	 * @return the message id
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Sets the message id.
	 *
	 * @param messageId the new message id
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValidationError [type=").append(type).append(", column=").append(column).append(", line=").append(line).append(", code=").append(code).append(", numErrors=").append(numErrors)
				.append(", numWarnings=").append(numWarnings).append(", summary=").append(summary).append(", messageId=").append(messageId).append("]");
		return builder.toString();
	}
}
