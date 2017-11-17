package es.inteco.rastreador2.json;

/**
 * JsonResponse Respuesta básica para los action json.
 * 
 * @author alvaro.pelaez
 */
public class JsonMessage {

	/** The status code. */
	private int code;

	/** The message. */
	private String message;

	/**
	 * Instantiates a new json error.
	 *
	 * @param message
	 *            the message
	 */
	public JsonMessage(String message) {
		super();
		this.message = message;
	}

	/**
	 * Instantiates a new json error.
	 *
	 * @param code
	 *            the code
	 * @param message
	 *            the message
	 */
	public JsonMessage(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code
	 *            the new code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
