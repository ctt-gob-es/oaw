package es.gob.oaw;

import es.gob.oaw.sim.MailSimProvider;
import es.gob.oaw.smtp.MailSmtpProvider;
import es.inteco.common.logging.Logger;

/**
 * Created by mikunis on 1/23/17.
 */
public final class MailProviderFactory {

    /**
	 * Instantiates a new mail provider factory.
	 */
    private MailProviderFactory() {
    }

    /**
	 * Gets the mail provider.
	 *
	 * @param provider the provider
	 * @return the mail provider
	 */
    public static MailProvider getMailProvider(final String provider) {
        Logger.putLog(String.format("Using %s MailProvider ", provider), MailProviderFactory.class, Logger.LOG_LEVEL_WARNING);
        if ("smtp".equalsIgnoreCase(provider)) {
            return new MailSmtpProvider();
        } else if ("sim".equalsIgnoreCase(provider)) {
            return new MailSimProvider();
        }
        throw new IllegalArgumentException(String.format("Not a valid mail provider %s . Check mail.properties file configuration mail.transport.protocol property should be 'smtp' or 'sim'", provider));
    }
}
