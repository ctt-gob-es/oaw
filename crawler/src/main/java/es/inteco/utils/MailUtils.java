package es.inteco.utils;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import javax.net.ssl.*;
import java.net.URL;
import java.util.List;

public final class MailUtils {

    private static final String CRAWLER_CORE_PROPERTIES = "crawler.core.properties";

    private MailUtils() {
    }

    public static void sendMail(String fromAddress, String fromName, List<String> mailTo, String subject, String text, String attachUrl, String attachName, String replyTo, String replyToName, boolean isError) {
        try {
            // Create the email message
            final MultiPartEmail email = createEmail();

            setEmailInfo(email, mailTo, fromAddress, fromName, subject, text);

            if (StringUtils.isNotEmpty(replyTo) && StringUtils.isNotEmpty(replyToName)) {
                email.addReplyTo(replyTo, replyToName);
            }

            if (!isError) {
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(attachUrl);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setName(attachName);
                // add the attachment
                email.attach(attachment);
            }

            // send the email
            email.send();
        } catch (Exception e) {
            Logger.putLog("FALLO No se ha podido enviar el correo", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void sendExecutiveMail(String fromAddress, String fromName, List<String> mailTo, String subject, String text, URL attachUrl, String attachName, String replyTo, String replyToName, boolean isError) {
        try {
            // Create the email message
            final MultiPartEmail email = createEmail();

            setEmailInfo(email, mailTo, fromAddress, fromName, subject, text);

            if (StringUtils.isNotEmpty(replyTo) && StringUtils.isNotEmpty(replyToName)) {
                email.addReplyTo(replyTo, replyToName);
            }

            if (!isError) {
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                attachment.setURL(attachUrl);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setName(attachName);
                // add the attachment
                email.attach(attachment);
            }

            // send the email
            email.send();
        } catch (Exception e) {
            Logger.putLog("FALLO No se han podido enviar los resultados del rastreo por correo.", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static void setEmailInfo(final MultiPartEmail email, final List<String> mailTo, final String mailFrom, final String mailFromName, final String subject, final String text) throws EmailException {
        for (String addressStr : mailTo) {
            email.addBcc(addressStr);
        }

        email.setFrom(mailFrom, mailFromName);
        email.setSubject(subject);
        email.setMsg(text);
    }

    private static MultiPartEmail createEmail() throws Exception {
        final MultiPartEmail email = new MultiPartEmail();
        final PropertiesManager pmgr = new PropertiesManager();

        final String trustStorePath = pmgr.getValue("certificados.properties", "truststore.path");
        final String trustStorePass = pmgr.getValue("certificados.properties", "truststore.pass");
        if (trustStorePath != null && !trustStorePath.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        }
        if (trustStorePass != null && !trustStorePass.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePass);
        }

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
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (Exception e) {
            Logger.putLog("Excepción: ", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (Exception e) {
            Logger.putLog("Excepción: ", MailUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }

        if (pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.host") == null || pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.host").trim().isEmpty()) {
            throw new Exception("No se configurado el servidor de correo");
        }
        email.setHostName(pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.host"));
        if (pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.user") != null && !pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.user").trim().isEmpty()) {
            email.setAuthenticator(new DefaultAuthenticator(pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.user").trim(), pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.pass").trim()));
        }

        final String mailSmtpPort = pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.port");
        if (mailSmtpPort != null && !mailSmtpPort.trim().isEmpty()) {
            Logger.putLog("Configurando el port " + mailSmtpPort, MailUtils.class, Logger.LOG_LEVEL_INFO);
            email.setSmtpPort(Integer.parseInt(mailSmtpPort));
        }

        final String mailSmtpSslPort = pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.sslport");
        if (mailSmtpSslPort != null && !mailSmtpSslPort.trim().isEmpty()) {
            Logger.putLog("Configurando el sslport " + mailSmtpSslPort, MailUtils.class, Logger.LOG_LEVEL_INFO);
            email.setSSLOnConnect(true);
            email.setSslSmtpPort(mailSmtpSslPort.trim());
        }

        Logger.putLog("Configurando TLS a " + pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.tls"), MailUtils.class, Logger.LOG_LEVEL_INFO);
        email.setStartTLSEnabled(Boolean.parseBoolean(pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.tls")));
        email.setStartTLSRequired(Boolean.parseBoolean(pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.smtp.tls")));
        email.setSSLCheckServerIdentity(false);

        return email;
    }

}
