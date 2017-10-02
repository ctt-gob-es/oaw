package es.gob.oaw.rastreador2.action.comun;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
			peticion.setMensajes(createMensajes(email));

			url = pmgr.getValue(MAIL_PROPERTIES, "sim.mailservice.wsdl.url");

			final EnvioMensajesService service = new EnvioMensajesService(new URL(url));
			final EnvioMensajesServiceWSBindingPortType envioMensajesServicePort = service.getEnvioMensajesServicePort();

			final Respuesta respuesta = envioMensajesServicePort.enviarMensaje(peticion);
			final ResponseStatusType respuestaStatus = respuesta.getStatus();
			if (!"1000".equals(respuestaStatus.getStatusCode())) {
				// request.setAttribute("simURL", url);
				// request.setAttribute("simConnection", false);
				// request.setAttribute("simError",
				// respuestaStatus.getStatusCode() + " - " +
				// respuestaStatus.getStatusText() + " - " +
				// respuestaStatus.getDetails());
				simConnection = false;
				simError = respuestaStatus.getStatusCode() + " -  " + respuestaStatus.getStatusText() + " - " + respuestaStatus.getDetails();

			} else {
				// request.setAttribute("simURL", url);
				// request.setAttribute("simConnection", true);
				simConnection = true;
			}

		} catch (MalformedURLException e) {
			// request.setAttribute("simURL", url);
			// request.setAttribute("simConnection", false);
			// request.setAttribute("simError", e.getMessage());
			simConnection = false;
			simError = e.getMessage();
		} catch (IOException e) {
			// request.setAttribute("simURL", url);
			// request.setAttribute("simConnection", false);
			// request.setAttribute("simError", e.getMessage());
			simConnection = false;
			simError = e.getMessage();
		}

		Check checkSim = new Check();

		checkSim.setUrl(url);
		checkSim.setConnection(simConnection);
		checkSim.setError(simError);

		String jsonCheck = new Gson().toJson(checkSim);

		response.setContentType("text/json");

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
	 */
	private ActionForward checkUrl(String urlAdress, HttpServletRequest request, HttpServletResponse response) {

		boolean urlConnection = false;

		String urlError = "";

		try {
			URL url = new URL(urlAdress);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setInstanceFollowRedirects(true);
			connection.setConnectTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "validator.timeout")));
			connection.setReadTimeout(Integer.parseInt(pmgr.getValue(IntavConstants.INTAV_PROPERTIES, "validator.timeout")));
			connection.addRequestProperty("Accept-Language", pmgr.getValue("crawler.core.properties", "method.accept.language.header"));
			connection.addRequestProperty("User-Agent", pmgr.getValue("crawler.core.properties", "method.user.agent.header"));

			connection.connect();

			int responseCode = connection.getResponseCode();

			if (HttpURLConnection.HTTP_OK == responseCode) {
				// request.setAttribute("url", url);
				// request.setAttribute("urlConnection", true);

				urlConnection = true;

			} else {
				// request.setAttribute("url", url);
				// request.setAttribute("urlConnection", false);
				// request.setAttribute("urlError", "Error al conectar a la URL
				// código: " + responseCode);

				urlError = "Error al conectar a la URL código: " + responseCode;
			}

		} catch (MalformedURLException e) {
			// request.setAttribute("url", urlAdress);
			// request.setAttribute("urlConnection", false);
			// request.setAttribute("urlError", "URL mal formada");
			urlError = "URL mal formada";
		} catch (IOException e1) {
			// request.setAttribute("url", urlAdress);
			// request.setAttribute("urlConnection", false);
			// request.setAttribute("urlError", "Error de conexión");

			urlError = "Error de conexión";
		}

		response.setContentType("text/json");

		Check checkSim = new Check();

		checkSim.setUrl(urlAdress);
		checkSim.setConnection(urlConnection);
		checkSim.setError(urlError);

		String jsonCheck = new Gson().toJson(checkSim);

		response.setContentType("text/json");

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
	 * Creates the mensajes.
	 *
	 * @return the mensajes
	 */
	private Mensajes createMensajes(String email) {
		final Mensajes mensajes = factory.createMensajes();
		final MensajeEmail mensajeEmail = factory.createMensajeEmail();
		mensajeEmail.setAsunto("Test de conectividad SIM");
		// Convertimos los saltos de línea en la etiqueta HTML equivalente
		// porque SIM envía el mensaje como text/HTML
		mensajeEmail.setCuerpo("Mensaje de prueba de conectividad");

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

	public class Check {

		private String url;
		private boolean connection;
		private String error;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public boolean isConnection() {
			return connection;
		}

		public void setConnection(boolean connection) {
			this.connection = connection;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

	}
}
