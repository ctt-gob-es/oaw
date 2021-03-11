package es.gob.oaw;

import java.util.List;

import es.inteco.common.properties.PropertiesManager;

/**
 * Clase para envío de correos. Utiliza el servicio que esté configurado a través de MailProviderFactory y MailProvider
 */
public class MailService {
	/** The pmgr. */
	private final PropertiesManager pmgr = new PropertiesManager();

	/**
	 * Instantiates a new mail service.
	 */
	public MailService() {
	}

	/**
	 * Send mail.
	 *
	 * @param mailTo      the mail to
	 * @param mailSubject the mail subject
	 * @param mailBody    the mail body
	 */
	public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody) {
		final MailProvider mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(MailProvider.MAIL_PROPERTIES, "mail.transport.protocol"));
		mailProvider.setSubject(mailSubject);
		mailProvider.setBody(mailBody);
		mailProvider.setMailTo(mailTo);
		mailProvider.sendMail();
	}

	/**
	 * Send mail.
	 *
	 * @param mailTo      the mail to
	 * @param mailSubject the mail subject
	 * @param mailBody    the mail body
	 * @param attachUrl   the attach url
	 * @param attachName  the attach name
	 */
	public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody, final String attachUrl, final String attachName) {
		final MailProvider mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(MailProvider.MAIL_PROPERTIES, "mail.transport.protocol"));
		mailProvider.setSubject(mailSubject);
		mailProvider.setBody(mailBody);
		mailProvider.setMailTo(mailTo);
		mailProvider.setAttachment(attachName, attachUrl);
		mailProvider.sendMail();
	}

	/**
	 * Send mail.
	 *
	 * @param mailTo      the mail to
	 * @param mailSubject the mail subject
	 * @param mailBody    the mail body
	 * @param attachUrl   the attach url
	 * @param attachName  the attach name
	 * @param html        the html
	 */
	public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody, final String attachUrl, final String attachName, final boolean html) {
		final MailProvider mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(MailProvider.MAIL_PROPERTIES, "mail.transport.protocol"));
		mailProvider.setSubject(mailSubject);
		mailProvider.setBody(mailBody);
		mailProvider.setMailTo(mailTo);
		mailProvider.setAttachment(attachName, attachUrl);
		mailProvider.setHtml(html);
		mailProvider.sendMail();
	}

	/**
	 * Send mail.
	 *
	 * @param mailTo      the mail to
	 * @param mailSubject the mail subject
	 * @param mailBody    the mail body
	 * @param html        the html
	 */
	public void sendMail(final List<String> mailTo, final String mailSubject, final String mailBody, boolean html) {
		final MailProvider mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(MailProvider.MAIL_PROPERTIES, "mail.transport.protocol"));
		mailProvider.setSubject(mailSubject);
		mailProvider.setBody(mailBody);
		mailProvider.setMailTo(mailTo);
		mailProvider.setHtml(html);
		mailProvider.sendMail();
	}
}
