/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.gob.oaw.rastreador2.action.comun;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

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
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.ProxyForm;
import es.inteco.rastreador2.dao.proxy.ProxyDAO;
import es.inteco.utils.CrawlerUtils;

/**
 * ConectividadAction. {@link Action} Para comprobaciones de conectividad.
 *
 * @author alvaro.pelaez
 */
public class ConectividadAction extends Action {
	/** Fichero de propiedades MAIL_PROPERTIES. */
	private static final String MAIL_PROPERTIES = "mail.properties";
	/** Properties Manager. */
	final PropertiesManager pmgr = new PropertiesManager();
	/** The factory. */
	private final ObjectFactory factory = new ObjectFactory();

	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action. ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_OTHER_OPTIONS);
		request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_CONECTIVIDAD);
		try (Connection c = DataBaseManager.getConnection()) {
			ProxyForm proxy = ProxyDAO.getProxy(c);
			request.setAttribute("proxyconfig", proxy);
			DataBaseManager.closeConnection(c);
		} catch (Exception e) {
			Logger.putLog("Error: ", ConectividadAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
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
			} else if ("modifyproxy".equals(action)) {
				String proxyUrl = request.getParameter("proxyUrl");
				String proxyStatus = request.getParameter("proxyStatus");
				String proxyPort = request.getParameter("proxyPort");
				// Save proxy config
				try (Connection c = DataBaseManager.getConnection()) {
					ProxyForm proxy = new ProxyForm();
					proxy.setStatus("true".equals(proxyStatus) ? 1 : 0);
					proxy.setUrl(proxyUrl);
					proxy.setPort(proxyPort);
					ProxyDAO.update(c, proxy);
					DataBaseManager.closeConnection(c);
				} catch (Exception e) {
					Logger.putLog("Error: ", ConectividadAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		}
		return mapping.findForward(Constants.EXITO);
	}

	/**
	 * Change proxy status.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param email    the email
	 * @return the action forward
	 */
	private ActionForward changeProxyStatus(HttpServletRequest request, HttpServletResponse response, String email) {
		return null;
	}

	/**
	 * Realiza una llamada a la URL del WSDL de SIM.
	 *
	 * @param request  Request
	 * @param response the response
	 * @param email    the email
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
	 * @param urlAdress the url adress
	 * @param request   Request
	 * @param response  the response
	 * @return the action forward
	 */
	private ActionForward checkUrl(String urlAdress, HttpServletRequest request, HttpServletResponse response) {
		boolean urlConnection = false;
		String urlError = "";
		try {
			URL url = new URL(es.inteco.utils.CrawlerUtils.encodeUrl(urlAdress));
			HttpURLConnection connection = null;
			connection = (HttpURLConnection) url.openConnection();
			if (connection instanceof HttpsURLConnection) {
				((HttpsURLConnection) connection).setSSLSocketFactory(getNaiveSSLSocketFactory());
			}
			connection.setInstanceFollowRedirects(false);
			connection.setConnectTimeout(Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
			connection.setReadTimeout(Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
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
		boolean urlConnectionProxy = false;
		String urlErrorProxy = "";
		try {
			URL url = new URL(es.inteco.utils.CrawlerUtils.encodeUrl(urlAdress));
			String proxyActive = "";
			String proxyHttpHost = "";
			String proxyHttpPort = "";
			try (Connection c = DataBaseManager.getConnection()) {
				ProxyForm proxy = ProxyDAO.getProxy(c);
				proxyActive = proxy.getStatus() > 0 ? "true" : "false";
				proxyHttpHost = proxy.getUrl();
				proxyHttpPort = proxy.getPort();
				DataBaseManager.closeConnection(c);
			} catch (Exception e) {
				Logger.putLog("Error: ", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR, e);
			}
			HttpURLConnection connection = null;
			if ("true".equals(proxyActive) && proxyHttpHost != null && proxyHttpPort != null) {
				try {
					Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHttpHost, Integer.parseInt(proxyHttpPort)));
					Logger.putLog("1. Aplicando proxy: " + proxyHttpHost + ":" + proxyHttpPort, CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
					connection = (HttpURLConnection) url.openConnection(proxy);
				} catch (NumberFormatException e) {
					Logger.putLog("Error al crear el proxy: " + proxyHttpHost + ":" + proxyHttpPort, CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
				}
				if (connection instanceof HttpsURLConnection) {
					((HttpsURLConnection) connection).setSSLSocketFactory(getNaiveSSLSocketFactory());
				}
				connection.setInstanceFollowRedirects(false);
				connection.setConnectTimeout(Integer.parseInt(pmgr.getValue("crawler.core.properties", "crawler.timeout")));
				connection.setReadTimeout(20000);
				connection.addRequestProperty("Accept-Language", pmgr.getValue("crawler.core.properties", "method.accept.language.header"));
				// connection.addRequestProperty("User-Agent", pmgr.getValue("crawler.core.properties", "method.user.agent.header"));
				connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
				connection.addRequestProperty("cookie", "<cookie value from the browser, from the header on a successful request>");
				int responseCode = connection.getResponseCode();
				if (HttpURLConnection.HTTP_OK == responseCode) {
					urlConnectionProxy = true;
				} else if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
					String newUrl = connection.getHeaderField("Location");
					this.checkUrl(newUrl, request, response);
				} else {
					urlErrorProxy = "Error al conectar a la URL código: " + responseCode;
				}
			} else {
				urlConnectionProxy = false;
				urlErrorProxy = "El proxy no está activo";
			}
		} catch (MalformedURLException e) {
			urlErrorProxy = "URL mal formada";
		} catch (IOException e1) {
			urlErrorProxy = "Error de conexión" + e1.getMessage() != null ? e1.getMessage() : "";
		}
		response.setContentType("text/json");
		CheckConectivity checkConectivity = new CheckConectivity();
		checkConectivity.setUrl(urlAdress);
		checkConectivity.setConnection(urlConnection);
		checkConectivity.setError(urlError);
		checkConectivity.setConnectionProxy(urlConnectionProxy);
		checkConectivity.setErrorProxy(urlErrorProxy);
		String jsonCheck = new Gson().toJson(checkConectivity);
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
	 * Configuración del socket SSL.
	 *
	 * @return the naive SSL socket factory
	 */
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
	 * Crea los mensajes a enviar por SIM.
	 *
	 * @param email      Dirección de correo electrónico
	 * @param urlWsdlSim URL del WSDL de SIM para incorporarlo en el mensaje de correo
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
	 * Check. DTO para resultados de comprobaciones.
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
		 * @param url the new url
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
		 * @param connection the new connection
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
		 * @param error the new error
		 */
		public void setError(String error) {
			this.error = error;
		}
	}

	/**
	 * The Class CheckConectivity.
	 */
	public class CheckConectivity {
		/** The url. */
		private String url;
		/** The connection. */
		private boolean connection;
		/** The connectionproxy. */
		private boolean connectionProxy;
		/** The error. */
		private String error;
		/** The errorproxy. */
		private String errorProxy;

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
		 * @param url the new url
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
		 * @param connection the new connection
		 */
		public void setConnection(boolean connection) {
			this.connection = connection;
		}

		/**
		 * Checks if is connection proxy.
		 *
		 * @return true, if is connection proxy
		 */
		public boolean isConnectionProxy() {
			return connectionProxy;
		}

		/**
		 * Sets the connection proxy.
		 *
		 * @param connectionProxy the new connection proxy
		 */
		public void setConnectionProxy(boolean connectionProxy) {
			this.connectionProxy = connectionProxy;
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
		 * @param error the new error
		 */
		public void setError(String error) {
			this.error = error;
		}

		/**
		 * Gets the error proxy.
		 *
		 * @return the error proxy
		 */
		public String getErrorProxy() {
			return errorProxy;
		}

		/**
		 * Sets the error proxy.
		 *
		 * @param errorProxy the new error proxy
		 */
		public void setErrorProxy(String errorProxy) {
			this.errorProxy = errorProxy;
		}
	}
}
