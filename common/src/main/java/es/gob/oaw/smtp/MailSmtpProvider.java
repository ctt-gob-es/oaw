package es.gob.oaw.smtp;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import es.gob.oaw.MailException;
import es.gob.oaw.MailProvider;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

/**
 * Clase para proporcionar el servicio mail mediante un servidor SMTP.
 */
public class MailSmtpProvider implements MailProvider {
	/** The pmgr. */
	private final PropertiesManager pmgr = new PropertiesManager();
	/** The from address. */
	private String fromAddress;
	/** The from name. */
	private String fromName;
	/** The mail to. */
	private List<String> mailTo;
	/** The mail to cco. */
	private List<String> mailToCco;
	/** The subject. */
	private String subject;
	/** The body. */
	private String body;
	/** The attach name. */
	private String attachName;
	/** The attach url. */
	private String attachUrl;
	/** The html. */
	private boolean html;

	/**
	 * Instantiates a new mail smtp provider.
	 */
	public MailSmtpProvider() {
		// Por defecto se envia desde observ.accesibilidad@correo.gob.es
		this.fromAddress = pmgr.getValue(MAIL_PROPERTIES, "mail.address.from");
		this.fromName = pmgr.getValue(MAIL_PROPERTIES, "mail.address.from.name");
	}

	/**
	 * Send mail.
	 *
	 * @throws MailException the mail exception
	 */
	@Override
	public void sendMail() throws MailException {
		try {
			// Create the email message
			final MultiPartEmail email = createEmail();
			setEmailInfo(email, mailTo, fromAddress, fromName, subject, body);
			if (mailToCco != null && !mailToCco.isEmpty()) {
				for (String addressStr : mailToCco) {
					email.addTo(addressStr);
				}
			}
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
			throw new MailException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Sets the from address.
	 *
	 * @param fromAddress the new from address
	 */
	@Override
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	/**
	 * Sets the from name.
	 *
	 * @param fromName the new from name
	 */
	@Override
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * Sets the mail to.
	 *
	 * @param mailTo the new mail to
	 */
	@Override
	public void setMailTo(List<String> mailTo) {
		this.mailTo = mailTo;
	}

	/**
	 * Sets the attachment.
	 *
	 * @param attachName the attach name
	 * @param attachUrl  the attach url
	 */
	@Override
	public void setAttachment(String attachName, String attachUrl) {
		this.attachName = attachName;
		this.attachUrl = attachUrl;
	}

	/**
	 * Sets the subject.
	 *
	 * @param mailSubject the new subject
	 */
	@Override
	public void setSubject(String mailSubject) {
		this.subject = mailSubject;
	}

	/**
	 * Sets the body.
	 *
	 * @param mailBody the new body
	 */
	@Override
	public void setBody(String mailBody) {
		this.body = mailBody;
	}

	/**
	 * Sets the html.
	 *
	 * @param html the new html
	 */
	@Override
	public void setHtml(boolean html) {
		this.html = html;
	}

	/**
	 * Sets the email info.
	 *
	 * @param email        the email
	 * @param mailTo       the mail to
	 * @param mailFrom     the mail from
	 * @param mailFromName the mail from name
	 * @param subject      the subject
	 * @param text         the text
	 * @throws EmailException the email exception
	 */
	private void setEmailInfo(final MultiPartEmail email, final List<String> mailTo, final String mailFrom, final String mailFromName, final String subject, final String text) throws EmailException {
		for (String addressStr : mailTo) {
			email.addTo(addressStr);
		}
		email.setFrom(mailFrom, mailFromName);
		email.setSubject(subject);
		if (!html) {
			email.setMsg(text);
		} else {
			email.addPart(text, "text/html; charset=UTF-8");
		}
	}

	/**
	 * Creates the email.
	 *
	 * @return the multi part email
	 */
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

	/**
	 * Inits the trust store.
	 */
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

	/**
	 * Inits the SSL context.
	 */
	private void initSSLContext() {
		final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
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

	/**
	 * Inits the SSL context.
	 *
	 * @param protocol           the protocol
	 * @param trustAllCerts      the trust all certs
	 * @param noHostnameVerifier the no hostname verifier
	 */
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

	/**
	 * Sets the mail to cco.
	 *
	 * @param mailToCco the new mail to cco
	 */
	@Override
	public void setMailToCco(List<String> mailToCco) {
		this.mailToCco = mailToCco;
	}
}
