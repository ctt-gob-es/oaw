package es.gob.oaw;

import java.util.List;

/**
 * Interfaz para proporcionar servicios de envío de correo electrónico
 */
public interface MailProvider {
	String MAIL_PROPERTIES = "mail.properties";

	void setFromAddress(String fromAddress);

	void setFromName(String fromName);

	void sendMail();

	void setMailTo(List<String> mailTo);

	void setAttachment(String attachName, String attachUrl);

	void setSubject(String mailSubject);

	void setBody(String mailBody);

	void setHtml(boolean html);
}
