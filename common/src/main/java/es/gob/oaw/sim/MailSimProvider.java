package es.gob.oaw.sim;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import es.gob.oaw.MailException;
import es.gob.oaw.MailProvider;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

/**
 * Clase para proporcionar el servicio mail mediante SIM - Plataforma de Mensajería.
 */
public class MailSimProvider implements MailProvider {
	/** The factory. */
	private final ObjectFactory factory = new ObjectFactory();
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
	 * Send mail.
	 *
	 * @throws MailException the mail exception
	 */
	@Override
	public void sendMail() throws MailException {
		final PropertiesManager pmgr = new PropertiesManager();
		final Peticion peticion = factory.createPeticion();
		peticion.setUsuario(pmgr.getValue(MAIL_PROPERTIES, "sim.user.username"));
		peticion.setPassword(pmgr.getValue(MAIL_PROPERTIES, "sim.user.password"));
		peticion.setServicio(pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.id"));
		peticion.setNombreLote("OAW-" + System.currentTimeMillis());
		peticion.setMensajes(createMensajes());
		final URL wsdlURL;
		try {
			wsdlURL = new URL(pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.wsdl.url"));
			final EnvioMensajesService service = new EnvioMensajesService(wsdlURL);
			final EnvioMensajesServiceWSBindingPortType envioMensajesServicePort = service.getEnvioMensajesServicePort();
			Logger.putLog(String.format("Sending mail from MailSimProvider using %s", pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.wsdl.url")), MailSimProvider.class, Logger.LOG_LEVEL_WARNING);
			final Respuesta respuesta = envioMensajesServicePort.enviarMensaje(peticion);
			final ResponseStatusType respuestaStatus = respuesta.getStatus();
			if (!"1000".equals(respuestaStatus.getStatusCode())) {
				Logger.putLog(String.format("Error SIM response code: %s, text: %s, details: %s", respuestaStatus.getStatusCode(), respuestaStatus.getStatusText(), respuestaStatus.getDetails()),
						MailSimProvider.class, Logger.LOG_LEVEL_ERROR);
			}
		} catch (Exception e) {
			Logger.putLog(String.format("Invalid SIM WSDL URL value of %s", pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.wsdl.url")), MailSimProvider.class, Logger.LOG_LEVEL_ERROR);
			throw new MailException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * Sets the from address.
	 *
	 * @param fromAddress the new from address
	 */
	@Override
	public void setFromAddress(final String fromAddress) {
		Logger.putLog("Trying to set fromAddress for SIM Mail Service. This value is configured in the SIM Mail Service", MailSimProvider.class, Logger.LOG_LEVEL_INFO);
	}

	/**
	 * Sets the from name.
	 *
	 * @param fromName the new from name
	 */
	@Override
	public void setFromName(final String fromName) {
		Logger.putLog("Trying to set fromName for SIM Mail Service. This value is configured in the SIM Mail Service", MailSimProvider.class, Logger.LOG_LEVEL_INFO);
	}

	/**
	 * Sets the mail to.
	 *
	 * @param mailTo the new mail to
	 */
	@Override
	public void setMailTo(final List<String> mailTo) {
		this.mailTo = mailTo;
	}

	/**
	 * Sets the mail to cco.
	 *
	 * @param mailToCco the new mail to cco
	 */
	@Override
	public void setMailToCco(final List<String> mailToCco) {
		this.mailToCco = mailToCco;
	}

	/**
	 * Sets the attachment.
	 *
	 * @param attachName the attach name
	 * @param attachUrl  the attach url
	 */
	@Override
	public void setAttachment(final String attachName, final String attachUrl) {
		this.attachName = attachName;
		this.attachUrl = attachUrl;
	}

	/**
	 * Sets the subject.
	 *
	 * @param mailSubject the new subject
	 */
	@Override
	public void setSubject(final String mailSubject) {
		this.subject = mailSubject;
	}

	/**
	 * Sets the body.
	 *
	 * @param mailBody the new body
	 */
	@Override
	public void setBody(final String mailBody) {
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
	 * Creates the mensajes.
	 *
	 * @return the mensajes
	 */
	private Mensajes createMensajes() {
		final Mensajes mensajes = factory.createMensajes();
		final MensajeEmail mensajeEmail = factory.createMensajeEmail();
		mensajeEmail.setAsunto(subject);
		// Convertimos los saltos de línea en la etiqueta HTML equivalente porque SIM
		// envía el mensaje como text/HTML
		mensajeEmail.setCuerpo(body.replace("\n", "<br>"));
		if (attachName != null && attachUrl != null) {
			createAdjunto(mensajeEmail);
		}
		final DestinatariosMail destinatariosMail = factory.createDestinatariosMail();
		final DestinatarioMail destinatarioMail = factory.createDestinatarioMail();
		final Destinatarios destinatarios = factory.createDestinatarios();
		String emails = String.join(";", mailTo);
		destinatarios.setTo(emails);
		Logger.putLog("Mail to: " + emails, MailSimProvider.class, Logger.LOG_LEVEL_INFO);
		if (mailToCco != null) {
			emails = String.join(";", mailToCco);
			destinatarios.setBcc(emails);
			Logger.putLog("Mail to cco: " + emails, MailSimProvider.class, Logger.LOG_LEVEL_INFO);
		}
		destinatarioMail.setDestinatarios(destinatarios);
		destinatariosMail.getDestinatarioMail().add(destinatarioMail);
		mensajeEmail.setDestinatariosMail(destinatariosMail);
		mensajes.getMensajeEmail().add(mensajeEmail);
		return mensajes;
	}

	/**
	 * Creates the adjunto.
	 *
	 * @param mensajeEmail the mensaje email
	 */
	private void createAdjunto(final MensajeEmail mensajeEmail) {
		try {
			final Adjunto adjunto = factory.createAdjunto();
			adjunto.setNombre(attachName);
			final byte[] bytes = Files.readAllBytes(Paths.get(attachUrl));
			adjunto.setContenido(new String(Base64.encodeBase64(bytes), StandardCharsets.US_ASCII));
			final Adjuntos adjuntos = factory.createAdjuntos();
			adjuntos.getAdjunto().add(adjunto);
			mensajeEmail.setAdjuntos(adjuntos);
		} catch (IOException ioe) {
			Logger.putLog("No se creado el adjunto " + attachUrl, MailSimProvider.class, Logger.LOG_LEVEL_ERROR, ioe);
		}
	}
}
