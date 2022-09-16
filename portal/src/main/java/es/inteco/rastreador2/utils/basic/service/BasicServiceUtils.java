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
package es.inteco.rastreador2.utils.basic.service;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;

import es.gob.oaw.basicservice.historico.CheckHistoricoAction;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceFile;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.ws.CrawlerWS;
import es.inteco.rastreador2.ws.CrawlerWSJob;
import es.inteco.utils.FileUtils;

/**
 * The Class BasicServiceUtils.
 */
public final class BasicServiceUtils {
	/**
	 * Instantiates a new basic service utils.
	 */
	private BasicServiceUtils() {
	}

	/**
	 * Save request data.
	 *
	 * @param basicServiceForm the basic service form
	 * @param status           the status
	 * @return the long
	 */
	public static long saveRequestData(final BasicServiceForm basicServiceForm, final String status) {
		try (Connection conn = DataBaseManager.getConnection()) {
			return DiagnosisDAO.insertBasicServices(conn, basicServiceForm, status);
		} catch (Exception e) {
			Logger.putLog("Excepción: Error al insertar los datos del servicio básico,", BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return 0;
	}

	/**
	 * Update request status.
	 *
	 * @param basicServiceForm the basic service form
	 * @param status           the status
	 */
	public static void updateRequestStatus(final BasicServiceForm basicServiceForm, final String status) {
		try (Connection conn = DataBaseManager.getConnection()) {
			DiagnosisDAO.updateStatus(conn, basicServiceForm.getId(), status);
		} catch (Exception e) {
			Logger.putLog("Excepción: Error al modificar los datos del servicio básico,", BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the basic service request by status.
	 *
	 * @param status the status
	 * @return the basic service request by status
	 */
	public static List<BasicServiceForm> getBasicServiceRequestByStatus(final String status) {
		try (Connection conn = DataBaseManager.getConnection()) {
			return DiagnosisDAO.getBasicServiceRequestByStatus(conn, status);
		} catch (Exception e) {
			Logger.putLog("Excepción: Error al recuperar las peticiones al servicio básico con el estado " + status, BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Gets the basic service form.
	 *
	 * @param basicServiceForm the basic service form
	 * @param request          the request
	 * @return the basic service form
	 */
	public static BasicServiceForm getBasicServiceForm(final BasicServiceForm basicServiceForm, final HttpServletRequest request) {
		Logger.putLog("getBasicServiceForm " + basicServiceForm.toString(), BasicServiceUtils.class, Logger.LOG_LEVEL_ERROR);
		if (request.getParameter(Constants.PARAM_USER) != null) {
			basicServiceForm.setUser(request.getParameter(Constants.PARAM_USER));
		} else {
			basicServiceForm.setUser(request.getParameter(Constants.PARAM_EMAIL));
		}
		String urlParameter = request.getParameter(Constants.PARAM_URL);
		if (urlParameter != null) {
			String url = "";
			try {
				url = URLDecoder.decode(urlParameter, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				Logger.putLog("No se puede decodificar la url como ISO-8859-1", CheckHistoricoAction.class, Logger.LOG_LEVEL_WARNING, e);
				try {
					url = URLDecoder.decode(urlParameter, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					url = "";
					Logger.putLog("No se puede decodificar la url como UTF-8", CheckHistoricoAction.class, Logger.LOG_LEVEL_WARNING, e);
				}
			}
			basicServiceForm.setDomain(url);
			if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
				basicServiceForm.setDomain(es.inteco.utils.CrawlerUtils.encodeUrl(basicServiceForm.getDomain()));
			}
		}
		basicServiceForm.setEmail(request.getParameter(Constants.PARAM_EMAIL));
		basicServiceForm.setProfundidad(request.getParameter(Constants.PARAM_DEPTH));
		basicServiceForm.setAmplitud(request.getParameter(Constants.PARAM_WIDTH));
		basicServiceForm.setLanguage("es");
		basicServiceForm.setReport(request.getParameter(Constants.PARAM_REPORT));
		if (request.getParameter("informe-nobroken") != null && Boolean.parseBoolean(request.getParameter("informe-nobroken"))) {
			basicServiceForm.setReport(basicServiceForm.getReport() + "-nobroken");
		}
		// Prevent full paths
		String parameterFileName = request.getParameter("filename");
		if (!org.apache.commons.lang3.StringUtils.isEmpty(parameterFileName)) {
			String tmp;
			try {
				tmp = StringUtils.corregirEncoding(parameterFileName).replace("\\", "/");
			} catch (UnsupportedEncodingException e) {
				tmp = parameterFileName.replace("\\", "/");
			}
			if (tmp.lastIndexOf("/") > 0) {
				parameterFileName = tmp.substring(tmp.lastIndexOf("/") + 1, tmp.length());
			} else {
				parameterFileName = tmp.toString();
			}
		}
		basicServiceForm.setFileName(parameterFileName);
		final String contentParameter = request.getParameter(Constants.PARAM_CONTENT);
		if (StringUtils.isNotEmpty(contentParameter)) {
			getContent(basicServiceForm, parameterFileName, contentParameter, false);
		}
		if (StringUtils.isNotEmpty(request.getParameter("urls"))) {
			String url = "";
			urlParameter = request.getParameter("urls");
			try {
				url = URLDecoder.decode(urlParameter, "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				Logger.putLog("No se puede decodificar la url como ISO-8859-1", CheckHistoricoAction.class, Logger.LOG_LEVEL_WARNING, e);
				try {
					url = URLDecoder.decode(urlParameter, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					url = "";
					Logger.putLog("No se puede decodificar la url como UTF-8", CheckHistoricoAction.class, Logger.LOG_LEVEL_WARNING, e);
				}
			}
			basicServiceForm.setDomain(url);
			if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
				basicServiceForm.setDomain(es.inteco.utils.CrawlerUtils.encodeUrl(basicServiceForm.getDomain()));
			}
			// basicServiceForm.setDomain(request.getParameter("urls"));
			if (basicServiceForm.getContent() != null && StringUtils.isNotEmpty(basicServiceForm.getContent())) {
				basicServiceForm.setAnalysisType(BasicServiceAnalysisType.MIXTO);
			} else {
				basicServiceForm.setAnalysisType(BasicServiceAnalysisType.LISTA_URLS);
			}
		}
		basicServiceForm.setInDirectory(Boolean.parseBoolean(request.getParameter(Constants.PARAM_IN_DIRECTORY)));
		basicServiceForm.setRegisterAnalysis(Boolean.parseBoolean(request.getParameter("registerAnalysis")));
		basicServiceForm.setAnalysisToDelete(request.getParameter("analysisToDelete"));
		basicServiceForm.setDomain(BasicServiceUtils.checkIDN(basicServiceForm.getDomain()));
		basicServiceForm.setComplexity(request.getParameter(Constants.PARAM_COMPLEXITY));
		basicServiceForm.setDepthReport(request.getParameter(Constants.PARAM_DEPTH_REPORT));
		return basicServiceForm;
	}

	/**
	 * Gets the content.
	 *
	 * @param basicServiceForm  the basic service form
	 * @param parameterFileName the parameter file name
	 * @param contentParameter  the content parameter
	 * @return the content
	 */
	public static void getContent(final BasicServiceForm basicServiceForm, String parameterFileName, final String contentParameter, final boolean decode) {
		if (!org.apache.commons.lang3.StringUtils.isEmpty(parameterFileName) && parameterFileName.toLowerCase().endsWith(".zip")) {
			try {
				File tmp = File.createTempFile("oaw_basic_service_", ".zip");
				org.apache.commons.io.FileUtils.writeByteArrayToFile(tmp, Base64.getUrlDecoder().decode(contentParameter.getBytes(StandardCharsets.ISO_8859_1.name())));
				ZipFile zipFile;
				try {
					zipFile = new ZipFile(tmp, Charset.forName("ISO-8859-1"));
					Enumeration<? extends ZipEntry> entries = zipFile.entries();
					List<BasicServiceFile> files = new ArrayList<>();
					while (entries.hasMoreElements()) {
						try {
							ZipEntry entry = entries.nextElement();
							String name = entry.getName();
//						if (entry.getName().toLowerCase().endsWith(".html")) {
							if (!entry.isDirectory()) {
								try {
									String content = org.apache.commons.io.IOUtils.toString(zipFile.getInputStream(entry), StandardCharsets.UTF_8.name());
									BasicServiceFile file = new BasicServiceFile();
									file.setName(name);
									file.setContent(content);
									files.add(file);
								} catch (UnsupportedEncodingException e) {
									Logger.putLog("No se puede codificar el contenido como UTF-8", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
									try {
										String content = org.apache.commons.io.IOUtils.toString(zipFile.getInputStream(entry), StandardCharsets.ISO_8859_1.name());
										BasicServiceFile file = new BasicServiceFile();
										file.setName(name);
										file.setContent(content);
										files.add(file);
									} catch (UnsupportedEncodingException e1) {
										Logger.putLog("No se puede codificar el contenido como UTF-8", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
									}
								}
							}
						} catch (IllegalArgumentException e) {
							Logger.putLog("No se puede procesar la entrada del fichero zip", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
						}
					}
					basicServiceForm.setContents(files);
				} catch (IOException e) {
					Logger.putLog("No se puede procesar el fichero zip", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
				}
			} catch (IOException e) {
				Logger.putLog("No se puede leer el fichero zip adjuntado", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
			}
			if (basicServiceForm.getDomain() != null && StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
				basicServiceForm.setAnalysisType(BasicServiceAnalysisType.MIXTO);
			} else {
				basicServiceForm.setAnalysisType(BasicServiceAnalysisType.CODIGO_FUENTE_MULTIPLE);
			}
		} else {
			try {
				String content = "";
				if (decode) {
					File tmp = File.createTempFile("oaw_basic_service_", ".txt");
					org.apache.commons.io.FileUtils.writeByteArrayToFile(tmp, Base64.getUrlDecoder().decode(contentParameter.getBytes(StandardCharsets.ISO_8859_1.name())));
					content = org.apache.commons.io.FileUtils.readFileToString(tmp, StandardCharsets.ISO_8859_1.name());
				} else {
					content = new String(contentParameter.getBytes(StandardCharsets.ISO_8859_1.name()));
				}
				basicServiceForm.setContent(content);
				if (basicServiceForm.getDomain() != null && StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
					basicServiceForm.setAnalysisType(BasicServiceAnalysisType.MIXTO);
				} else {
					basicServiceForm.setAnalysisType(BasicServiceAnalysisType.CODIGO_FUENTE);
				}
			} catch (Exception e) {
				Logger.putLog("No se puede procesar la entrada del fichero", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
			}
		}
	}

	/**
	 * Validate report.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the action errors
	 */
	public static ActionErrors validateReport(final BasicServiceForm basicServiceForm) {
		final ActionErrors errors = new ActionErrors();
		final String report = basicServiceForm.getReport();
		if (!report.equalsIgnoreCase(Constants.REPORT_UNE) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_1_NOBROKEN)
				&& !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_2) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_2_NOBROKEN)
				&& !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_3) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_3_NOBROKEN)
				&& !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_4) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_4_NOBROKEN)
				&& !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_5) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_5_NOBROKEN)) {
			errors.add(Globals.ERROR_KEY, new ActionMessage("basic.service.report.not.valid", Constants.REPORT_OBSERVATORY, Constants.REPORT_UNE));
		}
		return errors;
	}

	/**
	 * Validate url or content.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the action errors
	 */
	public static ActionErrors validateUrlOrContent(final BasicServiceForm basicServiceForm) {
		final ActionErrors errors = new ActionErrors();
		if (StringUtils.isEmpty(basicServiceForm.getDomain()) && StringUtils.isEmpty(basicServiceForm.getContent())) {
			errors.add(Globals.ERROR_KEY, new ActionMessage("basic.service.url.or.content"));
		}
		return errors;
	}

	/**
	 * Validate url lenght.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the action errors
	 */
	public static ActionErrors validateUrlLenght(final BasicServiceForm basicServiceForm) {
		final ActionErrors errors = new ActionErrors();
		// basic.service.url.size.max
		final PropertiesManager pmgr = new PropertiesManager();
		String maxSize = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.url.size.max");
		if (!StringUtils.isEmpty(basicServiceForm.getDomain()) && basicServiceForm.getDomain().length() > (maxSize != null ? Integer.parseInt(maxSize) : 12000)) {
			errors.add(Globals.ERROR_KEY, new ActionMessage("basic.service.url.size"));
		}
		return errors;
	}

	/**
	 * Check IDN.
	 *
	 * @param url the url
	 * @return the string
	 */
	public static String checkIDN(final String url) {
		if (url != null && !url.isEmpty()) {
			try {
				return url.replaceFirst(new URL(url).getHost(), IDN.toASCII(new URL(url).getHost()));
			} catch (Exception e) {
				Logger.putLog("Error al verificar el dominio internacionalizado", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
				return url;
			}
		} else {
			return url;
		}
	}

	/**
	 * Gets the guideline.
	 *
	 * @param report the report
	 * @return the guideline
	 */
	public static long getGuideline(final String report) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY) || report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_FILE) || report.equals(Constants.REPORT_OBSERVATORY_1_NOBROKEN)) {
			return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.observatorio.intav.id"));
		} else if (report.equals(Constants.REPORT_UNE) || report.equalsIgnoreCase(Constants.REPORT_UNE_FILE)) {
			return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"));
		} else if (report.equals(Constants.REPORT_WCAG_1_FILE)) {
			return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"));
		} else if (report.equals(Constants.REPORT_WCAG_2_FILE)) {
			return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag2.intav.id"));
		} else if (report.equals(Constants.REPORT_OBSERVATORY_2) || report.equals(Constants.REPORT_OBSERVATORY_2_NOBROKEN)) {
			return 7L;
		} else if (report.equals(Constants.REPORT_OBSERVATORY_3) || report.equals(Constants.REPORT_OBSERVATORY_3_NOBROKEN)) {
			return 8L;
		} else if (report.equals(Constants.REPORT_OBSERVATORY_4) || report.equals(Constants.REPORT_OBSERVATORY_4_NOBROKEN)) {
			return 9L;
		} else if (report.equals(Constants.REPORT_OBSERVATORY_5) || report.equals(Constants.REPORT_OBSERVATORY_5_NOBROKEN)) {
			return 10L;
		} else {
			return -1;
		}
	}

	/**
	 * Schedule basic service job.
	 *
	 * @param basicServiceForm the basic service form
	 * @throws Exception the exception
	 */
	public static void scheduleBasicServiceJob(final BasicServiceForm basicServiceForm) throws Exception {
		Logger.putLog("Programando el rastreo con la fecha: " + basicServiceForm.getSchedulingDate(), CrawlerWS.class, Logger.LOG_LEVEL_INFO);
		final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		final Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		final JobDetailImpl jobDetail = new JobDetailImpl("CrawlerWSJob_" + System.currentTimeMillis(), "CrawlerWSJob", CrawlerWSJob.class);
		if (basicServiceForm.getId() == 0) {
			basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, es.inteco.common.Constants.BASIC_SERVICE_STATUS_SCHEDULED));
		}
		final JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(es.inteco.rastreador2.ws.commons.Constants.BASIC_SERVICE_FORM, basicServiceForm);
		jobDetail.setJobDataMap(jobDataMap);
		final Trigger trigger = new SimpleTriggerImpl("CrawlerWSTrigger", "CrawlerWSGroup", basicServiceForm.getSchedulingDate());
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * Gets the title from content.
	 *
	 * @param content the content
	 * @return the title from content
	 */
	public static String getTitleFromContent(final String content) {
		String result;
		PropertiesManager pmgr = new PropertiesManager();
		Pattern pattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			if (matcher.group(1).length() <= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.max.length"))) {
				result = matcher.group(1);
			} else {
				// si el titulo es muy grande
				String title = matcher.group(1);
				// cortamos el title e insertamos '...'
				title = title.substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.max.length")) - 3);
				if (title.lastIndexOf(' ') > 0) {
					title = title.substring(0, title.lastIndexOf(' '));
				}
				result = title + "...";
			}
		} else {
			result = pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.alternative");
		}
		return cleanTitle(FileUtils.normalizeFileName(result));
	}

	/**
	 * Gets the title doc from content.
	 *
	 * @param content     the content
	 * @param isCompleted the is completed
	 * @return the title doc from content
	 */
	public static String getTitleDocFromContent(final String content, final boolean isCompleted) {
		if (content == null) {
			return "";
		}
		try {
			final PropertiesManager pmgr = new PropertiesManager();
			final Pattern pattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
			final Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				final String title = matcher.group(1).replaceAll("\\s{2,}", " ").trim();
				final int titleMaxLength = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.doc.max.length"));
				if (title.length() <= titleMaxLength) {
					return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(title));
				} else {
					// si el titulo es muy grande
					// si es un lugar del documento donde no tiene que salir
					// entero el titulo
					if (!isCompleted) {
						// cortamos el title e insertamos '...'
						String trunkTitle = title.substring(0, titleMaxLength - 3);
						trunkTitle = trunkTitle.substring(0, trunkTitle.lastIndexOf(' '));
						trunkTitle = trunkTitle + "...";
						return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(trunkTitle));
					} else {
						return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(title));
					}
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			Logger.putLog("Error al acceder al extraer el título del contenido HTML", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
			return "";
		}
	}

	/**
	 * Cleaning title
	 * 
	 * @param title
	 * 
	 * @return Formatted title
	 */
	private static String cleanTitle(String title) {
		String formattedTitle;
		try {
			formattedTitle = new String(title.getBytes("ISO-8859-1"), "UTF-8");
			formattedTitle = Normalizer.normalize(formattedTitle, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			formattedTitle = formattedTitle.replaceAll("[^a-zA-Z0-9]", "_");
		} catch (UnsupportedEncodingException e) {
			formattedTitle = title;
		}
		return formattedTitle;
	}
}
