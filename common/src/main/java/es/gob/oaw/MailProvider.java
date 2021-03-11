package es.gob.oaw;

import java.util.List;

/**
 * Interfaz para proporcionar servicios de envío de correo electrónico.
 */
public interface MailProvider {
	/** The mail properties. */
	String MAIL_PROPERTIES = "mail.properties";

	/**
	 * Sets the from address.
	 *
	 * @param fromAddress the new from address
	 */
	void setFromAddress(String fromAddress);

	/**
	 * Sets the from name.
	 *
	 * @param fromName the new from name
	 */
	void setFromName(String fromName);

	/**
	 * Send mail.
	 */
	void sendMail();

	/**
	 * Sets the mail to.
	 *
	 * @param mailTo the new mail to
	 */
	void setMailTo(List<String> mailTo);

	/**
	 * Sets the attachment.
	 *
	 * @param attachName the attach name
	 * @param attachUrl  the attach url
	 */
	void setAttachment(String attachName, String attachUrl);

	/**
	 * Sets the subject.
	 *
	 * @param mailSubject the new subject
	 */
	void setSubject(String mailSubject);

	/**
	 * Sets the body.
	 *
	 * @param mailBody the new body
	 */
	void setBody(String mailBody);

	/**
	 * Sets the html.
	 *
	 * @param html the new html
	 */
	void setHtml(boolean html);
}
