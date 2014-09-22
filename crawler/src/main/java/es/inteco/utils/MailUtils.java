package es.inteco.utils;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

import java.net.URL;
import java.util.List;

public final class MailUtils {

    private MailUtils() {
    }

    public static void sendMail(String mailFrom, String mailFromName, List<String> mailTo, String subject, String text, String attachUrl, String attachName, String replyTo, String replyToName, boolean isError) {
        try {
            // Create the email message
            final MultiPartEmail email = createEmail();

            for (String addressStr : mailTo) {
                email.addBcc(addressStr);
            }

            email.setFrom(mailFrom, mailFromName);
            email.setSubject(subject);
            email.setMsg(text);

            if (StringUtils.isNotEmpty(replyTo) && StringUtils.isNotEmpty(replyToName)) {
                email.addReplyTo(replyTo, replyToName);
            }

            if (!isError) {
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(attachUrl);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                // attachment.setDescription("Informe PDF");
                attachment.setName(attachName);
                // add the attachment
                email.attach(attachment);
            }

            // send the email
            email.send();
        } catch (Exception e) {
            Logger.putLog("No se ha podido enviar el correo MailUtils.sendMail.", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void sendExecutiveMail(String mailFrom, String mailFromName, List<String> mailTo, String subject, String text, URL attachUrl, String attachName, String replyTo, String replyToName, boolean isError) {
        try {
            // Create the email message
            final MultiPartEmail email = createEmail();

            for (String addressStr : mailTo) {
                email.addBcc(addressStr);
            }

            email.setFrom(mailFrom, mailFromName);
            email.setSubject(subject);
            email.setMsg(text);

            if (StringUtils.isNotEmpty(replyTo) && StringUtils.isNotEmpty(replyToName)) {
                email.addReplyTo(replyTo, replyToName);
            }

            if (!isError) {
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                attachment.setURL(attachUrl);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                // attachment.setDescription("Informe PDF");
                attachment.setName(attachName);
                // add the attachment
                email.attach(attachment);
            }

            // send the email
            email.send();
        } catch (Exception e) {
            Logger.putLog("No se han podido enviar los resultados del rastreo por correo.", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void sendSimpleMail(String fromAddress, String fromName, List<String> mailsTo, String subject, String text) {
        try {
            // Create the email message
            final MultiPartEmail email = createEmail();

            for (String addressStr : mailsTo) {
                email.addBcc(addressStr);
            }

            email.setFrom(fromAddress, fromName);
            email.setSubject(subject);
            email.setMsg(text);

            // send the email
            email.send();
        } catch (Exception e) {
            Logger.putLog("No se han podido enviar el correo correo.", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static MultiPartEmail createEmail() throws Exception {
        final MultiPartEmail email = new MultiPartEmail();
        final PropertiesManager pmgr = new PropertiesManager();

        if (pmgr.getValue("crawler.core.properties", "mail.smtp.host") == null || pmgr.getValue("crawler.core.properties", "mail.smtp.host").trim().isEmpty()) {
            throw new Exception("No se configurado el servidor de correo");
        }
        email.setHostName(pmgr.getValue("crawler.core.properties", "mail.smtp.host"));

        return email;
    }

}
