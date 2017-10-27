package es.gob.oaw.rastreador2.action.comun;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.google.gson.Gson;

import es.gob.oaw.sim.DestinatarioMail;
import es.gob.oaw.sim.Destinatarios;
import es.gob.oaw.sim.DestinatariosMail;
import es.gob.oaw.sim.EnvioMensajesService;
import es.gob.oaw.sim.EnvioMensajesServiceWSBindingPortType;
import es.gob.oaw.sim.MensajeEmail;
import es.gob.oaw.sim.Mensajes;
import es.gob.oaw.sim.ObjectFactory;
import es.gob.oaw.sim.Peticion;
import es.gob.oaw.sim.ResponseStatusType;
import es.gob.oaw.sim.Respuesta;
import es.inteco.common.Constants;
import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;

/**
 * Action para mostrar la conectividad con los sistemas externos como SIM.
 *
 * @author alvaro
 */
public class ConectividadAction extends Action {

	/** The Constant MAIL_PROPERTIES. */
	private static final String MAIL_PROPERTIES = "mail.properties";

	/** Properties Manager. */
	final PropertiesManager pmgr = new PropertiesManager();

	/** The factory. */
	private final ObjectFactory factory = new ObjectFactory();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.
	 * ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_CONECTIVIDAD);

		String action = request.getParameter(Constants.ACTION);
		if (action != null) {

			if ("checkurl".equals(action)) {
				String url = request.getParameter("url");
				if (StringUtils.isEmpty(url)) {
					final ActionErrors errors = new ActionErrors();
					errors.add("urlVacia", new ActionMessage("conectividad.url.vacia.error"));
					saveErrors(request, errors);
				} else {

					return checkUrl(url, request, response);
				}
			} else if ("checksim".equals(action)) {
				String email = request.getParameter("email");
				if (StringUtils.isEmpty(email)) {
					final ActionErrors errors = new ActionErrors();
					errors.add("emailVacia", new ActionMessage("conectividad.url.vacia.error"));
					saveErrors(request, errors);
				} else {
					return checkSIM(request, response, email);
				}

			}
		}

		return mapping.findForward(Constants.EXITO);

	}

	/**
	 * Realiza una llamada a la URL del WSDL de SIM.
	 *
	 * @param request
	 *            Request
	 * @param response
	 *            the response
	 * @param email
	 *            the email
	 * @return the action forward
	 */
	private ActionForward checkSIM(HttpServletRequest request, HttpServletResponse response, String email) {

		String url = "";
		boolean simConnection = false;
		String simError = "";

		try {
			url = pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.wsdl.url");
			URL wsdlURL = new URL(url);
			String charset = "UTF-8";

			URLConnection connection = wsdlURL.openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			connection.getInputStream();
			wsdlURL.openStream();

			final Peticion peticion = factory.createPeticion();

			peticion.setUsuario(pmgr.getValue(MAIL_PROPERTIES, "sim.user.username"));
			peticion.setPassword(pmgr.getValue(MAIL_PROPERTIES, "sim.user.password"));
			peticion.setServicio(pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.id"));
			peticion.setNombreLote("OAW-" + System.currentTimeMillis());
			peticion.setMensajes(createMensajes(email, url));

			url = pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.wsdl.url");

			final EnvioMensajesService service = new EnvioMensajesService(new URL(url));
			final EnvioMensajesServiceWSBindingPortType envioMensajesServicePort = service.getEnvioMensajesServicePort();

			final Respuesta respuesta = envioMensajesServicePort.enviarMensaje(peticion);
			final ResponseStatusType respuestaStatus = respuesta.getStatus();
			if (!"1000".equals(respuestaStatus.getStatusCode())) {
				simConnection = false;
				simError = respuestaStatus.getStatusCode() + " -  " + respuestaStatus.getStatusText() + " - " + respuestaStatus.getDetails();

			} else {
				simConnection = true;
			}

		} catch (MalformedURLException e) {
			simConnection = false;
			simError = e.getMessage();
		} catch (IOException e) {
			simConnection = false;
			simError = e.getMessage();
		} catch (Exception e) {
			simConnection = false;
			simError = e.getMessage();
		}

		Check checkSim = new Check();

		checkSim.setUrl(url);
		checkSim.setConnection(simConnection);
		checkSim.setError(simError);

		String jsonCheck = new Gson().toJson(checkSim);

		response.setContentType("text/json; charset=UTF-8");

		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(jsonCheck);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			Logger.putLog("Error: ", ConectividadAction.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return null;

	}

	/**
	 * Comprueba la conexión a una URL.
	 *
	 * @param urlAdress
	 *            the url adress
	 * @param request
	 *            Request
	 * @param response
	 *            the response
	 * @return the action forward
	 */
	private ActionForward checkUrl(String urlAdress, HttpServletRequest request, HttpServletResponse response) {

		boolean urlConnection = false;

		String urlError = "";

		try {

			URL url = new URL(es.inteco.utils.CrawlerUtils.encodeUrl(urlAdress));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			if (connection instanceof HttpsURLConnection) {
				((HttpsURLConnection) connection).setSSLSocketFactory(getNaiveSSLSocketFactory());
			}

			connection.setInstanceFollowRedirects(false);

			connection.setConnectTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "validator.timeout")));
			connection.setReadTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "validator.timeout")));
			connection.addRequestProperty("Accept-Language", pmgr.getValue("crawler.core.properties", "method.accept.language.header"));
			connection.addRequestProperty("User-Agent", pmgr.getValue("crawler.core.properties", "method.user.agent.header"));

			int responseCode = connection.getResponseCode();

			if (HttpURLConnection.HTTP_OK == responseCode) {
				urlConnection = true;

			} else if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {

				String newUrl = connection.getHeaderField("Location");

				this.checkUrl(newUrl, request, response);

			} else {

				urlError = "Error al conectar a la URL código: " + responseCode;

			}

		} catch (MalformedURLException e) {
			urlError = "URL mal formada";
		} catch (IOException e1) {
			urlError = "Error de conexión" + e1.getMessage() != null ? e1.getMessage() : "";
		}

		response.setContentType("text/json");

		Check checkSim = new Check();

		checkSim.setUrl(urlAdress);
		checkSim.setConnection(urlConnection);
		checkSim.setError(urlError);

		String jsonCheck = new Gson().toJson(checkSim);

		response.setContentType("text/json; charset=UTF-8");

		PrintWriter pw;
		try {
			pw = response.getWriter();
			pw.write(jsonCheck);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			Logger.putLog("Error: ", ConectividadAction.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return null;
	}

	private static SSLSocketFactory getNaiveSSLSocketFactory() {
		// Create a trust manager that does not validate certificate chains
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
		try {
			final SSLContext sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			return sc.getSocketFactory();
		} catch (Exception e) {
			Logger.putLog("Excepción: ", EvaluatorUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Crea los mensajes a enviar por SIM
	 *
	 * @param email
	 *            Dirección de correo electrónico
	 * @param urlWsdlSim
	 *            URL del WSDL de SIM para incorporarlo en el mensaje de correo
	 * @return Mensajes generados.
	 */
	private Mensajes createMensajes(String email, String urlWsdlSim) {
		final Mensajes mensajes = factory.createMensajes();
		final MensajeEmail mensajeEmail = factory.createMensajeEmail();
		mensajeEmail.setAsunto("Test de conectividad SIM");
		mensajeEmail.setCuerpo("Mensaje de prueba de conectividad. \nEste mensaje se ha enviado a través de SIM en: " + urlWsdlSim);

		final DestinatariosMail destinatariosMail = factory.createDestinatariosMail();
		final DestinatarioMail destinatarioMail = factory.createDestinatarioMail();
		final Destinatarios destinatarios = factory.createDestinatarios();

		destinatarios.setTo(email);
		destinatarioMail.setDestinatarios(destinatarios);
		destinatariosMail.getDestinatarioMail().add(destinatarioMail);
		mensajeEmail.setDestinatariosMail(destinatariosMail);
		mensajes.getMensajeEmail().add(mensajeEmail);

		return mensajes;
	}

	/**
	 * The Class Check.
	 */
	public class Check {

		/** The url. */
		private String url;

		/** The connection. */
		private boolean connection;

		/** The error. */
		private String error;

		/**
		 * Gets the url.
		 *
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * Sets the url.
		 *
		 * @param url
		 *            the new url
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		/**
		 * Checks if is connection.
		 *
		 * @return true, if is connection
		 */
		public boolean isConnection() {
			return connection;
		}

		/**
		 * Sets the connection.
		 *
		 * @param connection
		 *            the new connection
		 */
		public void setConnection(boolean connection) {
			this.connection = connection;
		}

		/**
		 * Gets the error.
		 *
		 * @return the error
		 */
		public String getError() {
			return error;
		}

		/**
		 * Sets the error.
		 *
		 * @param error
		 *            the new error
		 */
		public void setError(String error) {
			this.error = error;
		}

	}


}
