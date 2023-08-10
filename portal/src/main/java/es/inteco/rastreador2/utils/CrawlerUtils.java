/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.Constants.MAIL_PROPERTIES;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.comun.CrawlerLoginAction;
import es.inteco.rastreador2.action.usuario.NuevoUsuarioSistemaAction;
import es.inteco.rastreador2.actionform.cuentausuario.ModificarCuentaUsuarioForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.dao.language.LanguageDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.pdf.ExportAction;

/**
 * The Class CrawlerUtils.
 */
public final class CrawlerUtils {
	/**
	 * Instantiates a new crawler utils.
	 */
	private CrawlerUtils() {
	}

	/**
	 * Gets the crawler data.
	 *
	 * @param dcrForm             the dcr form
	 * @param idFulfilledCrawling the id fulfilled crawling
	 * @param user                the user
	 * @param mailTo              the mail to
	 * @return the crawler data
	 */
	public static CrawlerData getCrawlerData(DatosCartuchoRastreoForm dcrForm, Long idFulfilledCrawling, String user, List<String> mailTo) {
		return getCrawlerData(dcrForm, idFulfilledCrawling, user, mailTo, null);
	}

	/**
	 * Gets the crawler data.
	 *
	 * @param dcrForm             the dcr form
	 * @param idFulfilledCrawling the id fulfilled crawling
	 * @param user                the user
	 * @param mailTo              the mail to
	 * @param responsiblesTo      the responsibles to
	 * @return the crawler data
	 */
	public static CrawlerData getCrawlerData(DatosCartuchoRastreoForm dcrForm, Long idFulfilledCrawling, String user, List<String> mailTo, List<String> responsiblesTo) {
		CrawlerData crawlerData = new CrawlerData();
		crawlerData.setNombreRastreo(dcrForm.getNombre_rastreo());
		crawlerData.setIdCrawling(dcrForm.getId_rastreo());
		crawlerData.setIdCartridge(dcrForm.getId_cartucho());
		crawlerData.setIdFulfilledCrawling(idFulfilledCrawling);
		crawlerData.setUser(user);
		crawlerData.setUsersMail(mailTo);
		crawlerData.setResponsiblesMail(responsiblesTo);
		crawlerData.setIdCustomerAccount(dcrForm.getIdCuentaCliente());
		crawlerData.setIdObservatory(dcrForm.getIdObservatory());
		crawlerData.setPseudoaleatorio(dcrForm.isPseudoaleatorio());
		crawlerData.setCartuchos(dcrForm.getCartuchos());
		crawlerData.setAcronimo(dcrForm.getSeedAcronym());
		crawlerData.setUrls(dcrForm.getUrls());
		if (crawlerData.getUrls() != null && crawlerData.getUrls().size() > 1) {
			crawlerData.setProfundidad(1);
			crawlerData.setTopN(1);
		} else {
			crawlerData.setProfundidad(dcrForm.getProfundidad());
			crawlerData.setTopN(dcrForm.getTopN());
		}
		crawlerData.setDomains(dcrForm.getDomains());
		crawlerData.setExceptions(dcrForm.getExceptions());
		crawlerData.setCrawlingList(dcrForm.getCrawlingList());
		crawlerData.setIdGuideline(dcrForm.getId_guideline());
		crawlerData.setFicheroNorma(dcrForm.getFicheroNorma());
		crawlerData.setExhaustive(dcrForm.isExhaustive());
		crawlerData.setInDirectory(dcrForm.isInDirectory());
		if (dcrForm.getLanguage() != null) {
			crawlerData.setLanguage(dcrForm.getLanguage().getCodice());
		}
		return crawlerData;
	}

	/**
	 * Gets the minutes.
	 *
	 * @return the minutes
	 */
	public static List<String> getMinutes() {
		final PropertiesManager pmgr = new PropertiesManager();
		final String[] arrayMinutes = getArray(pmgr.getValue(CRAWLER_PROPERTIES, "minutes.list"));
		return getList(arrayMinutes);
	}

	/**
	 * Gets the hours.
	 *
	 * @return the hours
	 */
	public static List<String> getHours() {
		final PropertiesManager pmgr = new PropertiesManager();
		final String[] arrayMinutes = getArray(pmgr.getValue(CRAWLER_PROPERTIES, "hours.list"));
		return getList(arrayMinutes);
	}

	/**
	 * Gets the array.
	 *
	 * @param chain the chain
	 * @return the array
	 */
	private static String[] getArray(final String chain) {
		return chain.split(";");
	}

	/**
	 * Gets the list.
	 *
	 * @param array the array
	 * @return the list
	 */
	private static List<String> getList(final String[] array) {
		final List<String> list = new ArrayList<>();
		Collections.addAll(list, array);
		return list;
	}

	/**
	 * Gets the crawlings form.
	 *
	 * @param crawlings the crawlings
	 * @return the crawlings form
	 * @throws Exception the exception
	 */
	public static List<FulfilledCrawlingForm> getCrawlingsForm(final List<FulFilledCrawling> crawlings) throws Exception {
		final List<FulfilledCrawlingForm> crawlingsForm = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
		for (FulFilledCrawling crawling : crawlings) {
			final FulfilledCrawlingForm crawlingForm = new FulfilledCrawlingForm();
			BeanUtils.copyProperties(crawlingForm, crawling);
			crawlingForm.setDate(df.format(crawling.getDate()));
			crawlingsForm.add(crawlingForm);
		}
		return crawlingsForm;
	}

	/**
	 * Gets the fecha inicio.
	 *
	 * @param fecha   the fecha
	 * @param hora    the hora
	 * @param minutos the minutos
	 * @return the fecha inicio
	 * @throws ParseException the parse exception
	 */
	public static Date getFechaInicio(final String fecha, final String hora, final String minutos) throws ParseException {
		final PropertiesManager pmgr = new PropertiesManager();
		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));
		return df.parse(fecha + " " + hora + ":" + minutos);
	}

	/**
	 * Gets the roles.
	 *
	 * @param roleList the role list
	 * @return the roles
	 */
	public static List<String> getRoles(final String roleList) {
		final PropertiesManager pmgr = new PropertiesManager();
		final String[] arrayRoles = getArray(pmgr.getValue("role.properties", roleList));
		return getList(arrayRoles);
	}

	/**
	 * Checks for access.
	 *
	 * @param request   the request
	 * @param accessRol the access rol
	 * @return true, if successful
	 */
	public static boolean hasAccess(final HttpServletRequest request, final String accessRol) {
		final List<String> roleList = CrawlerUtils.getRoles(accessRol);
		final List<String> rolesesion = (ArrayList<String>) request.getSession().getAttribute(Constants.ROLE);
		if (rolesesion != null) {
			for (String role : roleList) {
				if (rolesesion.contains(role)) {
					return true;
				}
			}
		} else {
			Logger.putLog("Error: el usuario no tiene sus roles en session", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		return false;
	}

	/**
	 * Insertar datos automaticos CU.
	 *
	 * @param insertarRastreoForm    the insertar rastreo form
	 * @param nuevaCuentaUsuarioForm the nueva cuenta usuario form
	 * @param cartucho               the cartucho
	 * @return the insertar rastreo form
	 */
	public static InsertarRastreoForm insertarDatosAutomaticosCU(InsertarRastreoForm insertarRastreoForm, es.inteco.rastreador2.actionform.cuentausuario.NuevaCuentaUsuarioForm nuevaCuentaUsuarioForm,
			String cartucho) {
		if (!cartucho.equals("")) {
			insertarRastreoForm.setCodigo(nuevaCuentaUsuarioForm.getNombre() + "-" + cartucho);
		}
		insertarRastreoForm.setProfundidad(Integer.parseInt(nuevaCuentaUsuarioForm.getProfundidad()));
		insertarRastreoForm.setTopN(Integer.parseInt(nuevaCuentaUsuarioForm.getAmplitud()));
		insertarRastreoForm.setCartuchos(nuevaCuentaUsuarioForm.getCartuchos());
		insertarRastreoForm.setId_semilla(nuevaCuentaUsuarioForm.getIdSeed());
		insertarRastreoForm.setId_lista_rastreable(nuevaCuentaUsuarioForm.getIdCrawlableList());
		insertarRastreoForm.setId_lista_no_rastreable(nuevaCuentaUsuarioForm.getIdNoCrawlableList());
		insertarRastreoForm.setPseudoAleatorio(nuevaCuentaUsuarioForm.isPseudoAleatorio());
		insertarRastreoForm.setLenguaje(nuevaCuentaUsuarioForm.getLenguaje());
		insertarRastreoForm.setInDirectory(nuevaCuentaUsuarioForm.isInDirectory());
		return insertarRastreoForm;
	}

	/**
	 * Insertar datos automaticos.
	 *
	 * @param insertarRastreoForm        the insertar rastreo form
	 * @param modificarCuentaUsuarioForm the modificar cuenta usuario form
	 * @param cartucho                   the cartucho
	 * @return the insertar rastreo form
	 */
	public static InsertarRastreoForm insertarDatosAutomaticos(InsertarRastreoForm insertarRastreoForm, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm, String cartucho) {
		if (!cartucho.equals("")) {
			insertarRastreoForm.setCodigo(modificarCuentaUsuarioForm.getNombre() + "-" + cartucho);
		} else {
			insertarRastreoForm.setCodigo(modificarCuentaUsuarioForm.getNombre());
		}
		insertarRastreoForm.setProfundidad(Integer.parseInt(modificarCuentaUsuarioForm.getProfundidad()));
		insertarRastreoForm.setTopN(Integer.parseInt(modificarCuentaUsuarioForm.getAmplitud()));
		insertarRastreoForm.setSemilla(modificarCuentaUsuarioForm.getDominio());
		insertarRastreoForm.setListaRastreable(modificarCuentaUsuarioForm.getListaRastreable());
		insertarRastreoForm.setListaNoRastreable(modificarCuentaUsuarioForm.getListaNoRastreable());
		insertarRastreoForm.setCartuchos(modificarCuentaUsuarioForm.getCartuchos());
		insertarRastreoForm.setNormaAnalisis(modificarCuentaUsuarioForm.getNormaAnalisis());
		insertarRastreoForm.setNormaAnalisisEnlaces(modificarCuentaUsuarioForm.getNormaAnalisisEnlaces());
		insertarRastreoForm.setId_lista_rastreable(modificarCuentaUsuarioForm.getIdListaRastreable());
		insertarRastreoForm.setId_semilla(modificarCuentaUsuarioForm.getIdSemilla());
		insertarRastreoForm.setId_lista_no_rastreable(modificarCuentaUsuarioForm.getIdListaNoRastreable());
		insertarRastreoForm.setPseudoAleatorio(modificarCuentaUsuarioForm.isPseudoAleatorio());
		insertarRastreoForm.setLenguaje(modificarCuentaUsuarioForm.getLenguaje());
		insertarRastreoForm.setInDirectory(modificarCuentaUsuarioForm.isInDirectory());
		return insertarRastreoForm;
	}

	/**
	 * Return file.
	 *
	 * @param response the response
	 * @param filename the filename
	 * @param mimeType the mime type
	 * @param delete   the delete
	 * @throws Exception the exception
	 */
	public static void returnFile(final HttpServletResponse response, final String filename, final String mimeType, final boolean delete) throws Exception {
		final File file = new File(filename);
		try (FileInputStream fileIn = new FileInputStream(file); final OutputStream out = response.getOutputStream()) {
			response.setContentType(mimeType);
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			byte[] buf = new byte[8192];
			int bytesread = 0, bytesBuffered = 0;
			while ((bytesread = fileIn.read(buf)) > -1) {
				out.write(buf, 0, bytesread);
				bytesBuffered += bytesread;
				if (bytesBuffered > 1024 * 1024) { // flush after 1MB
					bytesBuffered = 0;
					out.flush();
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			if (delete && !file.delete()) {
				Logger.putLog("No se ha podido borrar el fichero: " + file.getAbsolutePath(), ExportAction.class, Logger.LOG_LEVEL_ERROR);
			}
		}
	}

	/**
	 * Return data.
	 *
	 * @param response the response
	 * @param data     the data
	 * @param filename the filename
	 * @param mimeType the mime type
	 * @throws Exception the exception
	 */
	public static void returnData(final HttpServletResponse response, final byte[] data, final String filename, final String mimeType) throws Exception {
		try (ByteArrayInputStream fileIn = new ByteArrayInputStream(data); final OutputStream out = response.getOutputStream()) {
			response.setContentType(mimeType);
			response.setContentLength(data.length);
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", filename));
			final byte[] buffer = new byte[2048];
			int bytesRead = fileIn.read(buffer);
			while (bytesRead >= 0) {
				if (bytesRead > 0) {
					out.write(buffer, 0, bytesRead);
					bytesRead = fileIn.read(buffer);
				}
			}
			out.flush();
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Return string as file.
	 *
	 * @param response the response
	 * @param content  the content
	 * @param filename the filename
	 * @param mimeType the mime type
	 * @throws Exception the exception
	 */
	public static void returnStringAsFile(final HttpServletResponse response, final String content, final String filename, final String mimeType) throws Exception {
		try (PrintWriter out = response.getWriter()) {
			response.setContentType(mimeType);
			response.setContentLength(content.length());
			response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", filename));
			out.print(content);
			out.flush();
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Return text.
	 *
	 * @param response the response
	 * @param text     the text
	 * @param isHtml   the is html
	 * @throws Exception the exception
	 */
	public static void returnText(final HttpServletResponse response, final String text, final boolean isHtml) throws Exception {
		try (OutputStream out = response.getOutputStream()) {
			if (isHtml) {
				response.setContentType("text/html");
			} else {
				response.setContentType("text/plain");
			}
			out.write(text.getBytes());
			out.flush();
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Warn administrators.
	 *
	 * @param exception the exception
	 * @param clazz     the clazz
	 */
	public static void warnAdministrators(final Exception exception, final Class clazz) {
		Logger.putLog("Excepción: ", clazz, Logger.LOG_LEVEL_ERROR, exception);
		try {
			final PropertiesManager pmgr = new PropertiesManager();
			final List<String> adminMails = DAOUtils.getMailsByRol(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.administrator.id")));
			final String alertSubject = pmgr.getValue(MAIL_PROPERTIES, "alert.from.subject");
			final String alertText = pmgr.getValue(MAIL_PROPERTIES, "warning.administrator.message") + exception.getMessage() + "\n\n"
					+ pmgr.getValue(MAIL_PROPERTIES, "warning.administrator.signature");
			MailService mailService = new MailService();
			mailService.sendMail(adminMails, alertSubject, alertText);
		} catch (Exception e) {
			Logger.putLog("No ha sido posible avisar a los administradores: ", NuevoUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the resources.
	 *
	 * @param request the request
	 * @return the resources
	 */
	public static MessageResources getResources(final HttpServletRequest request) {
		return (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
	}

	/**
	 * Gets the locale.
	 *
	 * @param request the request
	 * @return the locale
	 */
	public static Locale getLocale(final HttpServletRequest request) {
		return RequestUtils.getUserLocale(request, null);
	}

	/**
	 * Gets the languages.
	 *
	 * @return the languages
	 */
	public static List<LenguajeForm> getLanguages() {
		try (Connection c = DataBaseManager.getConnection()) {
			return LanguageDAO.loadLanguages(c);
		} catch (Exception e) {
			Logger.putLog("Exception: ", CrawlerLoginAction.class, Logger.LOG_LEVEL_ERROR, e);
			return null;
		}
	}

	/**
	 * Gets the cron expression.
	 *
	 * @param date                  the date
	 * @param cronExpressionPattern the cron expression pattern
	 * @return the cron expression
	 */
	public static String getCronExpression(Date date, String cronExpressionPattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return cronExpressionPattern.replaceAll("min", String.valueOf(calendar.get(Calendar.MINUTE))).replaceAll("hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)))
				.replaceAll("daymonth", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))).replaceAll("month", String.valueOf(getMonthForCronExpression(calendar, cronExpressionPattern)))
				.replaceAll("year", String.valueOf(calendar.get(Calendar.YEAR)));
	}

	/**
	 * Gets the month for cron expression.
	 *
	 * @param calendar              the calendar
	 * @param cronExpressionPattern the cron expression pattern
	 * @return the month for cron expression
	 */
	private static int getMonthForCronExpression(Calendar calendar, String cronExpressionPattern) {
		String regexp = "month/([\\d]*)";
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		int monthPeriod = 0;
		int firstMonth = calendar.get(Calendar.MONTH) + 1;
		Matcher matcher = pattern.matcher(cronExpressionPattern);
		if (matcher.find()) {
			monthPeriod = Integer.parseInt(matcher.group(1));
		}
		while ((monthPeriod != 0) && (firstMonth - monthPeriod > 0)) {
			firstMonth = firstMonth - monthPeriod;
		}
		return firstMonth;
	}

	/**
	 * Gets the fichero norma.
	 *
	 * @param idGuideline the id guideline
	 * @return the fichero norma
	 */
	public static String getFicheroNorma(long idGuideline) {
		try (Connection c = DataBaseManager.getConnection()) {
			return es.inteco.plugin.dao.RastreoDAO.recuperarFicheroNorma(c, idGuideline);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Format date.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String formatDate(final Date date) {
		final PropertiesManager pmgr = new PropertiesManager();
		final SimpleDateFormat sdf = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		return sdf.format(date);
	}

	/**
	 * Gets the charset.
	 *
	 * @param source the source
	 * @return the charset
	 */
	public static String getCharset(final String source) {
		final String charset;
		final String regexp = "<meta.*charset=(.*?)\"";
		final Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		final Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			charset = matcher.group(1);
		} else {
			charset = Constants.DEFAULT_ENCODING;
		}
		if (isValidCharset(charset)) {
			return charset;
		} else {
			return Constants.DEFAULT_ENCODING;
		}
	}

	/**
	 * Checks if is valid charset.
	 *
	 * @param charset the charset
	 * @return true, if is valid charset
	 */
	private static boolean isValidCharset(final String charset) {
		try {
			byte[] test = new byte[10];
			new String(test, charset);
			return true;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}
}
