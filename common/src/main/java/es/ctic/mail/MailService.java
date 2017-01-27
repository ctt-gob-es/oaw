package es.ctic.mail;

import es.inteco.common.properties.PropertiesManager;

import java.util.List;

/**
 * Clase para envío de correos. Utiliza el servicio que esté configurado a través de MailProviderFactory y MailProvider
 */
public class MailService {

    private final MailProvider mailProvider;

    public MailService() {
        final PropertiesManager pmgr = new PropertiesManager();
        mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(MailProvider.MAIL_PROPERTIES, "mail.transport.protocol"));
    }

    public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody) {
        mailProvider.setSubject(mailSubject);
        mailProvider.setBody(mailBody);
        mailProvider.setMailTo(mailTo);
        mailProvider.sendMail();
    }

    public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody, final String attachUrl, final String attachName) {
        mailProvider.setSubject(mailSubject);
        mailProvider.setBody(mailBody);
        mailProvider.setMailTo(mailTo);
        mailProvider.setAttachment(attachName, attachUrl);
        mailProvider.sendMail();
    }

}
