package es.ctic.mail;

import es.ctic.mail.sim.MailSimProvider;
import es.ctic.mail.smtp.MailSmtpProvider;

/**
 * Created by mikunis on 1/23/17.
 */
public final class MailProviderFactory {

    private MailProviderFactory() {
    }

    public static MailProvider getMailProvider(final String provider) {
        if ("smtp".equalsIgnoreCase(provider)) {
            return new MailSmtpProvider();
        } else if ("sim".equalsIgnoreCase(provider)) {
            return new MailSimProvider();
        }
        throw new IllegalArgumentException(String.format("Not a valid mail provider %s . Check mail.properties file configuration mail.transport.protocol property should be 'smtp' or 'sim'", provider));
    }
}
