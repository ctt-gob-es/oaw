/*
 * 
 */
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import es.gob.oaw.MailException;
import es.gob.oaw.MailService;
import es.gob.oaw.rastreador2.pdf.PdfGeneratorThread2;
import es.gob.oaw.rastreador2.pdf.SourceFilesManager;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.TemplateRangeForm;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoric;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoricComparision;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoricRange;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoricResults;
import es.inteco.rastreador2.actionform.observatorio.UraSendResultForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.dependencia.DependenciaDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.observatorio.TemplateRangeDAO;
import es.inteco.rastreador2.dao.observatorio.UraSendResultDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfAccesibilidad;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012b;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNEEN2019;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.utils.FileUtils;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

/**
 * The Class SendResultsMailUtils.
 */
public final class SendResultsMailUtils {
	/**
	 * Generate and send data.
	 *
	 * @param idObs          the id obs
	 * @param idCartucho     the id cartucho
	 * @param idObsExecution the id obs execution
	 * @param emailSubject   the email subject
	 * @param cco            the cco
	 * @param notifyMail     the notify mail
	 * @throws Exception the exception
	 */
	public static void generateAndSendData(final Long idObs, final Long idCartucho, final Long idObsExecution, final String emailSubject, final String cco, final String notifyMail) throws Exception {
		PropertiesManager pmgr = new PropertiesManager();
		Connection c = DataBaseManager.getConnection();
		try {
			final String[] exObsIds = ObservatorioDAO.getExObsIdsConfig(c, idObsExecution);
			final List<ComparisionForm> comparision = ObservatorioDAO.getComparisionConfig(c, idObsExecution);
			String[] tagsToFilter = null;
			if (comparision != null && !comparision.isEmpty()) {
				tagsToFilter = new String[comparision.size()];
				for (int i = 0; i < comparision.size(); i++) {
					tagsToFilter[i] = String.valueOf(comparision.get(i).getIdTag());
				}
			}
			// Generate Annex
			final String annexPath = getAnnexes(idObs, idCartucho, idObsExecution, tagsToFilter, exObsIds, comparision);
			// Generate Zip pdf
			final List<FulFilledCrawling> fulfilledCrawlings = ObservatorioDAO.getFulfilledCrawlingByObservatoryExecution(c, idObsExecution);
			final Map<String, String> pdfZipsPath = getPdfs(idObs, idObsExecution, fulfilledCrawlings);
			// Get dependecies info
			List<UraSendResultForm> uras = UraSendResultDAO.findAll(c, idObsExecution, false);
			final List<TemplateRangeForm> iterationRanges = TemplateRangeDAO.findAll(c, idObsExecution);
			final Map<Long, TemplateRangeForm> iterationRangesMap = iterationRanges.stream().collect(Collectors.toMap(TemplateRangeForm::getId, template -> template));
			for (UraSendResultForm ura : uras) {
				ura.setSendError("");
				ura.setSend(false);
				ura.setFileLink("");
				ura.setFilePass("");
				ura.setSendDate(null);
				// Find Dependency
				DependenciaForm dependency = DependenciaDAO.findById(c, ura.getUraId());
				String xlsxFilePath = annexPath + "/Dependencias_v2/" + dependency.getName() + ".xlsx";
				String pdfZipPath = pdfZipsPath.get(PDFUtils.formatSeedName(dependency.getName()));
				if (!StringUtils.isEmpty(pdfZipPath) && !StringUtils.isEmpty(xlsxFilePath)) {
					File xlsx = new File(xlsxFilePath);
					File pdfZip = new File(pdfZipPath);
					if (xlsx.exists() && pdfZip.exists()) {
						// Make a zip with dependency xlsx and zipPdf
						final String passString = generatePassayPassword();
						final String randomUUID = UUID.randomUUID().toString();
						File zipFileToSend = generateProtectedZip(pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.send.path"), dependency, xlsx, pdfZip, passString, randomUUID);
						ura.setFileLink(zipFileToSend.getPath());
						ura.setFilePass(passString);
						Calendar expirationDate = calculateExpirationDate(c);
						ura.setValidDate(expirationDate.getTime());
						String mailBody = sendMailToUra(idObs, dependency, ura, iterationRangesMap, zipFileToSend.getPath(), zipFileToSend.getName(), emailSubject, cco, passString, randomUUID,
								expirationDate);
						ura.setMail(mailBody);
					}
				}
			}
			// Send email to notify end of process
			final MailService mailService = new MailService();
			MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
			StringBuilder mailBody = new StringBuilder("<p>" + messageResources.getMessage("send.mail.end.process.mail.body", new String[] { getObservatoryName(idObs) }) + "</p>");
			Connection conn = DataBaseManager.getConnection();
			List<UraSendResultForm> notSend = UraSendResultDAO.findAllNotSend(conn, idObsExecution);
			if (notSend != null && !notSend.isEmpty()) {
				mailBody.append("<p>" + messageResources.getMessage("send.mail.end.process.mail.notSend") + "</p>");
				mailBody.append("<ul>");
				for (UraSendResultForm uraSend : notSend) {
					if (!StringUtils.isEmpty(uraSend.getSendError())) {
						mailBody.append("<li>");
						mailBody.append(uraSend.getUra().getName() + " (Error: " + uraSend.getSendError() + ")");
						mailBody.append("</li>");
					}
				}
				mailBody.append("</ul>");
			}
			List<String> mailsTo = new ArrayList<>();
			mailsTo.add(notifyMail);
			try {
				mailService.sendMail(mailsTo, messageResources.getMessage("send.mail.end.process.mail.subject"), mailBody.toString(), true);
			} catch (MailException e) {
				Logger.putLog("Fallo al enviar el correo", SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR, e);
			}
			// Registry historic
			registryHistoric(idObsExecution, exObsIds, cco, emailSubject, comparision, iterationRanges, uras);
			DataBaseManager.closeConnection(conn);
			DataBaseManager.closeConnection(c);
		} catch (Exception e) {
			Logger.putLog("Error en el proceso de envío de resultados", SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR, e);
			Connection conn = DataBaseManager.getConnection();
			UraSendResultDAO.updateErrorAll(conn, idObsExecution, e.getLocalizedMessage());
			DataBaseManager.closeConnection(conn);
		} finally {
			DataBaseManager.closeConnection(c);
		}
	}

	/**
	 * Registry historic.
	 *
	 * @param idObsExecution the id obs execution
	 * @param idsExObs       the ids ex obs
	 * @param cco            the cco
	 * @param subject        the subject
	 * @param comparisions   the comparisions
	 * @param ranges         the ranges
	 * @param results        the results
	 * @throws Exception the exception
	 */
	private static void registryHistoric(final Long idObsExecution, final String[] idsExObs, final String cco, final String subject, final List<ComparisionForm> comparisions,
			final List<TemplateRangeForm> ranges, final List<UraSendResultForm> results) throws Exception {
		UraSendHistoric historic = new UraSendHistoric();
		historic.setIdExObs(idObsExecution);
		historic.setCco(cco);
		historic.setSubject(subject);
		// Save
		historic.setIdsExObs(String.join(",", idsExObs));
		List<UraSendHistoricComparision> comparisionsH = new ArrayList<>();
		if (comparisions != null && !comparisions.isEmpty()) {
			for (ComparisionForm comparision : comparisions) {
				UraSendHistoricComparision comparisionH = new UraSendHistoricComparision();
				BeanUtils.copyProperties(comparisionH, comparision);
				comparisionsH.add(comparisionH);
			}
		}
		historic.setComparisions(comparisionsH);
		List<UraSendHistoricRange> rangesH = new ArrayList<>();
		if (ranges != null && !ranges.isEmpty()) {
			for (TemplateRangeForm range : ranges) {
				UraSendHistoricRange rangeH = new UraSendHistoricRange();
				BeanUtils.copyProperties(rangeH, range);
				rangesH.add(rangeH);
			}
		}
		historic.setRanges(rangesH);
		List<UraSendHistoricResults> resultsH = new ArrayList<>();
		if (results != null && !results.isEmpty()) {
			for (UraSendResultForm result : results) {
				UraSendHistoricResults resultH = new UraSendHistoricResults();
				BeanUtils.copyProperties(resultH, result);
				resultsH.add(resultH);
			}
		}
		historic.setResults(resultsH);
		Connection conn = DataBaseManager.getConnection();
		UraSendResultDAO.saveHistoricSend(conn, historic);
		DataBaseManager.closeConnection(conn);
	}

	/**
	 * Generate zip.
	 *
	 * @param exportPath the export path
	 * @param dependency the dependency
	 * @param xlsx       the xlsx
	 * @param pdfZip     the pdf zip
	 * @param passString the pass string
	 * @param randomUUID the random UUID
	 * @return the file
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException           Signals that an I/O exception has occurred.
	 */
	private static File generateProtectedZip(final String exportPath, final DependenciaForm dependency, final File xlsx, final File pdfZip, final String passString, final String randomUUID)
			throws FileNotFoundException, IOException {
		File directory = new File(exportPath);
		if (!directory.exists() && !directory.mkdirs()) {
			Logger.putLog("Fail creating export send file directoru", SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setEncryptFiles(true);
		zipParameters.setEncryptionMethod(EncryptionMethod.AES);
		// Below line is optional. AES 256 is used by default. You can override it to use AES 128. AES 192 is supported only for extracting.
		zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
		List<File> files = new ArrayList<>();
		files.add(pdfZip);
		files.add(xlsx);
		ZipFile zipFile = new ZipFile(exportPath + "/" + randomUUID + ".zip", passString.toCharArray());
		zipFile.addFiles(files, zipParameters);
		return zipFile.getFile();
	}

	/**
	 * Gets the observatory name.
	 *
	 * @param idObservatory the id observatory
	 * @return the observatory name
	 */
	private static String getObservatoryName(final Long idObservatory) {
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			return observatoryForm.getNombre();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Gets the pdfs.
	 *
	 * @param idObservatory          the id observatory
	 * @param idObservatoryExecution the id observatory execution
	 * @param fulfilledCrawlings     the fulfilled crawlings
	 * @return the pdfs
	 */
	private static Map<String, String> getPdfs(final Long idObservatory, final Long idObservatoryExecution, final List<FulFilledCrawling> fulfilledCrawlings) {
		int i = 0;
		for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
			i++;
			Logger.putLog("Generando PDF: " + i + "/" + fulfilledCrawlings.size() + " - " + fulfilledCrawling.getSeed().getNombre(), SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR);
			buildPdf(idObservatory, idObservatoryExecution, fulfilledCrawling.getId(), fulfilledCrawling.getIdCrawling());
		}
		PropertiesManager pmgr = new PropertiesManager();
		return ZipUtils.pdfsZipToMap(idObservatory, idObservatoryExecution, pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav"));
	}

	/**
	 * Send mail to ura.
	 *
	 * @param idObservatory      the id observatory
	 * @param ura                the ura
	 * @param uraCustom          the ura custom
	 * @param iterationRangesMap the iteration ranges map
	 * @param attachUrl          the attach url
	 * @param attachName         the attach name
	 * @param emailSubject       the email subject
	 * @param cco                the cco
	 * @param passString         the pass string
	 * @param randomUUID         the random UUID
	 * @param expirationDate     the expiration date
	 * @return the string
	 * @throws Exception the exception
	 */
	private static String sendMailToUra(final Long idObservatory, final DependenciaForm ura, final UraSendResultForm uraCustom, final Map<Long, TemplateRangeForm> iterationRangesMap,
			final String attachUrl, final String attachName, final String emailSubject, final String cco, final String passString, final String randomUUID, final Calendar expirationDate)
			throws Exception {
		// Compose boDy with template
		TemplateRangeForm template = iterationRangesMap.get(uraCustom.getRange().getId());
		String mailBody = composeMailBody(ura, uraCustom, template, attachName, passString, expirationDate);
		uraCustom.setMail(mailBody);
		// Email subject
		final MailService mailService = new MailService();
		// Get emails from URA
		List<String> mailsTo = new LinkedList<String>();
		if (!StringUtils.isEmpty(ura.getEmails())) {
			mailsTo = new LinkedList<String>(Arrays.asList(StringUtils.split(ura.getEmails(), ";,")));
		}
		// Check if can send as cco
		List<String> mailsToCco = new LinkedList<String>();
		if (!StringUtils.isEmpty(cco)) {
			mailsToCco = new LinkedList<String>(Arrays.asList(StringUtils.split(cco, ";,")));
		}
		List<String> checkMails = new LinkedList<String>();
		checkMails.addAll(mailsTo);
		checkMails.addAll(mailsToCco);
		try {
			if (StringUtils.isEmpty(ura.getEmails()) || !validateEmails(checkMails)) {
				Logger.putLog("No se envía correo a la URA: " + ura.getName(), SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR);
				uraCustom.setSendError("Comprobar correo/s erróneo/s : " + String.join(";", checkMails));
				uraCustom.setSendDate(new Date());
				uraCustom.setSend(false);
			} else if (ura.getSendAuto() && !StringUtils.isEmpty(ura.getEmails())) {
				Logger.putLog("Enviando correo a la URA: " + ura.getName(), SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR);
				if (!StringUtils.isEmpty(ura.getAcronym())) {
					mailService.sendMail(mailsTo, mailsToCco, "[" + ura.getAcronym() + "] " + emailSubject, mailBody, true);
				} else {
					mailService.sendMail(mailsTo, mailsToCco, emailSubject, mailBody, true);
				}
				// Mark as send
				uraCustom.setSend(true);
				uraCustom.setSendError("");
				uraCustom.setSendDate(new Date());
			} else {
				Logger.putLog("No se envía correo a la URA: " + ura.getName(), SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR);
				uraCustom.setMail("");
				uraCustom.setTemplate("");
				uraCustom.setSend(false);
				uraCustom.setSendError("");
				uraCustom.setSendDate(new Date());
			}
		} catch (MailException e) {
			uraCustom.setSendError(e.getCause().getMessage());
			uraCustom.setSendDate(new Date());
			uraCustom.setSend(false);
		}
		Connection c = DataBaseManager.getConnection();
		UraSendResultDAO.markSend(c, uraCustom);
		DataBaseManager.closeConnection(c);
		return mailBody;
	}

	/**
	 * Compose mail body.
	 *
	 * @param ura            the ura
	 * @param uraCustom      the ura custom
	 * @param template       the template
	 * @param fileLink       the file link
	 * @param filePassword   the file password
	 * @param expirationDate the expiration date
	 * @return the string builder
	 */
	public static String composeMailBody(final DependenciaForm ura, final UraSendResultForm uraCustom, final TemplateRangeForm template, final String fileLink, final String filePassword,
			final Calendar expirationDate) {
		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
		PropertiesManager pmgr = new PropertiesManager();
		String templateMail = template.getTemplate();
		templateMail.replace("_ura_name_", ura.getName());
		if (ura.getOfficial()) {
			if (StringUtils.substringBetween(templateMail, "[oficiosa]", "[/oficiosa]") != null) {
				templateMail = templateMail.replace(StringUtils.substringBetween(templateMail, "[oficiosa]", "[/oficiosa]"), "");
				templateMail = templateMail.replace("[oficial]", "");
				templateMail = templateMail.replace("[/oficial]", "");
				templateMail = templateMail.replace("[oficiosa]", "");
				templateMail = templateMail.replace("[/oficiosa]", "");
			}
		} else {
			if (StringUtils.substringBetween(templateMail, "[oficial]", "[/oficial]") != null) {
				templateMail = templateMail.replace(StringUtils.substringBetween(templateMail, "[oficial]", "[/oficial]"), "");
				templateMail = templateMail.replace("[oficial]", "");
				templateMail = templateMail.replace("[/oficial]", "");
				templateMail = templateMail.replace("[oficiosa]", "");
				templateMail = templateMail.replace("[/oficiosa]", "");
			}
		}
		templateMail = templateMail.replace("_ura_name_", ura.getName());
		if (uraCustom.isHasCustomText()) {
			templateMail = templateMail.replace("_ura_custom_text_", uraCustom.getTemplate());
		} else {
			templateMail = templateMail.replace("_ura_custom_text_", "");
			uraCustom.setTemplate("");
		}
		try {
			Connection c = DataBaseManager.getConnection();
			String mapping = ObservatorioDAO.getMappingFromConfig(c);
			final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
			templateMail = templateMail.replace("_ura_download_link_", "<a href='" + mapping + "/" + fileLink + "'>" + mapping + "/" + fileLink + "</a> "
					+ messageResources.getMessage("send.mail.end.process.mail.expiration.date", new String[] { df.format(expirationDate.getTime()) }));
		} catch (Exception e) {
			Logger.putLog("Error getting file config", SendResultsMailUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		templateMail = templateMail.replace("_ura_download_pass_", filePassword);
		StringBuilder mailBody = new StringBuilder(templateMail);
		String mailBodyString = mailBody.toString();
		mailBodyString = mailBodyString.replace("<br />", "").replace("<br/>", "").replace("<br>", "").replace("\n", "");
		return StringEscapeUtils.unescapeHtml(mailBodyString);
	}

	/**
	 * Calculate expiration date.
	 *
	 * @param c the c
	 * @return the calendar
	 * @throws SQLException the SQL exception
	 */
	private static Calendar calculateExpirationDate(Connection c) throws Exception {
		int days = ObservatorioDAO.getFileExpirationFromConfig(c);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_YEAR, days);
		calendar.getTime();
		return calendar;
	}

	/**
	 * Builds the pdf.
	 *
	 * @param idObservatory          the id observatory
	 * @param idObservatoryExecution the id observatory execution
	 * @param idRastreoRealizado     the id rastreo realizado
	 * @param idRastreo              the id rastreo
	 */
	private static void buildPdf(final Long idObservatory, final Long idObservatoryExecution, final long idRastreoRealizado, final long idRastreo) {
		try (Connection c = DataBaseManager.getConnection()) {
			final SemillaForm seed = SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, idRastreo));
			// final File pdfFile = getReportFile(seed);
			List<File> pdfFiles = getReportFiles(idObservatory, idObservatoryExecution, seed);
			if (pdfFiles != null && !pdfFiles.isEmpty()) {
				for (File pdfFile : pdfFiles) {
					final File sources = new File(pdfFile.getParentFile(), "sources.zip");
					// Si el pdf no ha sido creado lo creamos
					if (!pdfFile.exists() || !sources.exists()) {
						final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
						final List<Long> previousEvaluationIds;
						if (evaluationIds != null && !evaluationIds.isEmpty()) {
							if (!pdfFile.exists()) {
								final es.gob.oaw.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.gob.oaw.rastreador2.observatorio.ObservatoryManager();
								previousEvaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(observatoryManager
										.getPreviousIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(idRastreo, ObservatorioDAO.getPreviousObservatoryExecution(c, idObservatoryExecution)));
								final long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();
								String aplicacion = CartuchoDAO.getApplicationFromExecutedObservatoryId(c, idRastreoRealizado, idRastreo);
								// Desdoblamiento nueva metodologia Nuevo fichero con los textos para las
								// exportaciones
								// Añadada UNE-EN2019 y accesibilidad
								if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfAccesibilidad(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution,
											observatoryType);
								} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion)) {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNEEN2019(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution,
											observatoryType);
								} else if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012b(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution,
											observatoryType);
								} else {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources("ApplicationResources"), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution, observatoryType);
								}
							}
							final SourceFilesManager sourceFilesManager = new SourceFilesManager(pdfFile.getParentFile());
							if (!sourceFilesManager.existsSourcesZip()) {
								sourceFilesManager.writeSourceFiles(c, evaluationIds);
								sourceFilesManager.zipSources(true);
							}
							FileUtils.deleteDir(new File(pdfFile.getParent() + File.separator + "temp"));
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", PdfGeneratorThread2.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the report files.
	 *
	 * @param idObservatory          the id observatory
	 * @param idObservatoryExecution the id observatory execution
	 * @param seed                   the seed
	 * @return the report files
	 */
	private static List<File> getReportFiles(final Long idObservatory, final Long idObservatoryExecution, SemillaForm seed) {
		List<File> pdfFiles = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		try (Connection c = DataBaseManager.getConnection()) {
			List<DependenciaForm> dependenciasSemilla = SemillaDAO.getSeedDependenciasById(c, seed.getId());
			if (dependenciasSemilla != null && !dependenciasSemilla.isEmpty()) {
				for (DependenciaForm dependenciaSemilla : dependenciasSemilla) {
					String dependOn = PDFUtils.formatSeedName(dependenciaSemilla.getName());
					if (dependOn == null || dependOn.isEmpty()) {
						dependOn = Constants.NO_DEPENDENCE;
					}
					final String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav") + idObservatory + File.separator + idObservatoryExecution + File.separator + dependOn
							+ File.separator + PDFUtils.formatSeedName(seed.getNombre());
					pdfFiles.add(new File(path + File.separator + PDFUtils.formatSeedName(seed.getNombre()) + ".pdf"));
				}
			}
		} catch (Exception e) {
		}
		return pdfFiles;
	}

	/**
	 * Gets the annexes.
	 *
	 * @param idObs          the id obs
	 * @param idCartucho     the id cartucho
	 * @param idObsExecution the id obs execution
	 * @param tagsToFilter   the tags to filter
	 * @param exObsIds       the ex obs ids
	 * @param comparision    the comparision
	 * @return the annexes
	 * @throws Exception the exception
	 */
	private static String getAnnexes(final Long idObs, final Long idCartucho, final Long idObsExecution, final String[] tagsToFilter, final String[] exObsIds, final List<ComparisionForm> comparision)
			throws Exception {
		try {
			final Long idOperation = System.currentTimeMillis();
			MessageResources messageResources = null;
			final Connection connection = DataBaseManager.getConnection();
			final String application = CartuchoDAO.getApplication(connection, idCartucho);
			DataBaseManager.closeConnection(connection);
			if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(application)) {
				messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
			} else if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(application)) {
				messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
			}
			AnnexUtils.generateEmailAnnex(messageResources, idObs, idObsExecution, idOperation, exObsIds, comparision, tagsToFilter);
			final PropertiesManager pmgr = new PropertiesManager();
			final String exportPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path");
			return exportPath + idOperation;
		} catch (Exception e) {
			Logger.putLog("Exception generando los anexos.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Generate passay password.
	 *
	 * @return the string
	 */
	private static String generatePassayPassword() {
		PasswordGenerator gen = new PasswordGenerator();
		CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
		lowerCaseRule.setNumberOfCharacters(2);
		CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
		upperCaseRule.setNumberOfCharacters(2);
		CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit);
		digitRule.setNumberOfCharacters(2);
		String password = gen.generatePassword(10, lowerCaseRule, upperCaseRule, digitRule);
		return password;
	}

	/**
	 * Email list validation
	 * 
	 * @param emailList
	 * @return Emails validation
	 */
	private static boolean validateEmails(List<String> emailList) {
		for (String email : emailList) {
			if (!validateEmail(email)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Email validation
	 * 
	 * @param email
	 * @return Email validation
	 */
	private static boolean validateEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pattern = Pattern.compile(emailRegex);
		if (pattern.matcher(email).matches()) {
			return true;
		}
		return false;
	}
}
