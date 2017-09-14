package es.gob.oaw.smtp;

import es.gob.oaw.MailProvider;
import es.gob.oaw.sim.MailSimProvider;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import javax.net.ssl.*;
import java.util.List;

/**
 * Clase para proporcionar el servicio mail mediante un servidor SMTP
 */
public class MailSmtpProvider implements MailProvider {

    private final PropertiesManager pmgr = new PropertiesManager();

    private String fromAddress;
    private String fromName;
    private List<String> mailTo;
    private String subject;
    private String body;
    private String attachName;
    private String attachUrl;

    public MailSmtpProvider() {
        // Por defecto se envia desde observ.accesibilidad@correo.gob.es
        this.fromAddress = pmgr.getValue(MAIL_PROPERTIES, "mail.address.from");
        this.fromName = pmgr.getValue(MAIL_PROPERTIES, "mail.address.from.name");
    }

    @Override
    public void sendMail() {
        try {
            // Create the email message
            final MultiPartEmail email = createEmail();

            setEmailInfo(email, mailTo, fromAddress, fromName, subject, body);

            if (attachUrl != null && attachName != null) {
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(attachUrl);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setName(attachName);
                // add the attachment
                email.attach(attachment);
            }

            Logger.putLog(String.format("Sending mail from MailSmtpProvider to ", email.getHostName()), MailSmtpProvider.class, Logger.LOG_LEVEL_WARNING);

            // send the email
            email.send();
        } catch (Exception e) {
            Logger.putLog("FALLO No se ha podido enviar el correo", MailSmtpProvider.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    @Override
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @Override
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    @Override
    public void setMailTo(List<String> mailTo) {
        this.mailTo = mailTo;
    }

    @Override
    public void setAttachment(String attachName, String attachUrl) {
        this.attachName = attachName;
        this.attachUrl = attachUrl;
    }

    @Override
    public void setSubject(String mailSubject) {
        this.subject = mailSubject;
    }

    @Override
    public void setBody(String mailBody) {
        this.body = mailBody;
    }

    private void setEmailInfo(final MultiPartEmail email, final List<String> mailTo, final String mailFrom, final String mailFromName, final String subject, final String text) throws EmailException {
        for (String addressStr : mailTo) {
            email.addTo(addressStr);
        }

        email.setFrom(mailFrom, mailFromName);
        email.setSubject(subject);
        email.setMsg(text);
    }

    private MultiPartEmail createEmail() {
        final MultiPartEmail email = new MultiPartEmail();

        initTrustStore();
        initSSLContext();

        if (pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.host") == null || pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.host").trim().isEmpty()) {
            throw new IllegalArgumentException("No se configurado el servidor de correo");
        }
        email.setHostName(pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.host"));
        if (pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.user") != null && !pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.user").trim().isEmpty()) {
            email.setAuthenticator(new DefaultAuthenticator(pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.user").trim(), pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.pass").trim()));
        }

        final String mailSmtpPort = pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.port");
        if (mailSmtpPort != null && !mailSmtpPort.trim().isEmpty()) {
            Logger.putLog("Configurando el port " + mailSmtpPort, MailSmtpProvider.class, Logger.LOG_LEVEL_INFO);
            email.setSmtpPort(Integer.parseInt(mailSmtpPort));
        }

        final String mailSmtpSslPort = pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.sslport");
        if (mailSmtpSslPort != null && !mailSmtpSslPort.trim().isEmpty()) {
            Logger.putLog("Configurando el sslport " + mailSmtpSslPort, MailSmtpProvider.class, Logger.LOG_LEVEL_INFO);
            email.setSSLOnConnect(true);
            email.setSslSmtpPort(mailSmtpSslPort.trim());
        }

        Logger.putLog("Configurando TLS a " + pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.tls"), MailSmtpProvider.class, Logger.LOG_LEVEL_INFO);
        email.setStartTLSEnabled(Boolean.parseBoolean(pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.tls")));
        email.setStartTLSRequired(Boolean.parseBoolean(pmgr.getValue(MAIL_PROPERTIES, "mail.smtp.tls")));
        email.setSSLCheckServerIdentity(false);

        return email;
    }

    private void initTrustStore() {
        final String trustStorePath = pmgr.getValue("certificados.properties", "truststore.path");
        final String trustStorePass = pmgr.getValue("certificados.properties", "truststore.pass");
        if (trustStorePath != null && !trustStorePath.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        }
        if (trustStorePass != null && !trustStorePass.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePass);
        }
    }

    private void initSSLContext() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        // Install the all-trusting trust manager
        final HostnameVerifier noHostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        initSSLContext("SSL", trustAllCerts, noHostnameVerifier);
        initSSLContext("TLS", trustAllCerts, noHostnameVerifier);
    }

    private void initSSLContext(final String protocol, final TrustManager[] trustAllCerts, final HostnameVerifier noHostnameVerifier) {
        try {
            SSLContext sc = SSLContext.getInstance(protocol);
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(noHostnameVerifier);
        } catch (Exception e) {
            Logger.putLog("Excepción: ", MailSmtpProvider.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }
}
