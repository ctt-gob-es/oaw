package es.ctic.mail;

import java.util.List;

/**
 * Interfaz para proporcionar servicios de envío de correo electrónico
 */
public interface MailProvider {
    void setFromAddress(String fromAddress);

    void setFromName(String fromName);

    void sendMail();

    void setMailTo(List<String> mailTo);

    void setAttachment(String attachName, String attachUrl);

    void setSubject(String mailSubject);

    void setBody(String mailBody);
}
