package es.gob.oaw;

/**
 * The Class MailException.
 */
public class MailException extends Exception {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7220368241080939782L;

	/**
	 * Instantiates a new mail exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public MailException(String message, Throwable cause) {
		super(message, cause);
	}
}
