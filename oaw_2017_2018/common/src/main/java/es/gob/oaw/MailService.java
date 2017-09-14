package es.gob.oaw;

import es.inteco.common.properties.PropertiesManager;

import java.util.List;

/**
 * Clase para envío de correos. Utiliza el servicio que esté configurado a través de MailProviderFactory y MailProvider
 */
public class MailService {

    private final PropertiesManager pmgr = new PropertiesManager();

    public MailService() {
    }

    public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody) {
        final MailProvider mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(MailProvider.MAIL_PROPERTIES, "mail.transport.protocol"));
        mailProvider.setSubject(mailSubject);
        mailProvider.setBody(mailBody);
        mailProvider.setMailTo(mailTo);
        mailProvider.sendMail();
    }

    public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody, final String attachUrl, final String attachName) {
        final MailProvider mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(MailProvider.MAIL_PROPERTIES, "mail.transport.protocol"));
        mailProvider.setSubject(mailSubject);
        mailProvider.setBody(mailBody);
        mailProvider.setMailTo(mailTo);
        mailProvider.setAttachment(attachName, attachUrl);
        mailProvider.sendMail();
    }

}
