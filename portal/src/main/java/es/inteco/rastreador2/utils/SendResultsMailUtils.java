package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;

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
	 * @throws Exception the exception
	 */
	public static void generateAndSendData(final Long idObs, final Long idCartucho, final Long idObsExecution, final String emailSubject) throws Exception {
		PropertiesManager pmgr = new PropertiesManager();
		Connection c = DataBaseManager.getConnection();
		final String[] exObsIds = ObservatorioDAO.getExObsIdsConfig(c, idObsExecution);
		final List<ComparisionForm> comparision = ObservatorioDAO.getComparisionConfig(c, idObsExecution);
		// Generate Annex
		final String annexPath = getAnnexes(idObs, idCartucho, idObsExecution, null, exObsIds, comparision);
		// Generate Zip pdf
		final List<FulFilledCrawling> fulfilledCrawlings = ObservatorioDAO.getFulfilledCrawlingByObservatoryExecution(c, idObsExecution);
		final Map<String, String> pdfZipsPath = getPdfs(idObs, idObsExecution, fulfilledCrawlings);
		// Get dependecies info
		// List<DependenciaForm> dependencies = ObservatorioDAO.getDependenciesByIdExObs(c, idObsExecution);
		final List<UraSendResultForm> uras = UraSendResultDAO.findAll(c, idObsExecution);
		final String exportPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path");
		final List<TemplateRangeForm> iterationRanges = TemplateRangeDAO.findAll(c, idObsExecution);
		final Map<Long, TemplateRangeForm> iterationRangesMap = iterationRanges.stream().collect(Collectors.toMap(TemplateRangeForm::getId, template -> template));
		for (UraSendResultForm ura : uras) {
			// Find Dependency
			// TODO GET DEPENDENCY FILE FROM ANNEXPATH
			// TODO GET PDF ZIP FROM ZIPS PATH
			DependenciaForm dependency = DependenciaDAO.findById(c, ura.getUraId());
			if (dependency.isSendAuto() && !StringUtils.isEmpty(dependency.getEmails())) {
				String xlsxFilePath = annexPath + "/Dependencias/" + dependency.getName() + ".xlsx";
				String pdfZipPath = pdfZipsPath.get(PDFUtils.formatSeedName(dependency.getName()));
				File xlsx = new File(xlsxFilePath);
				File pdfZip = new File(pdfZipPath);
				if (xlsx.exists() && pdfZip.exists()) {
					// TODO Make a zip with dependency xlsx and zipPdf
					File zipFileToSend = new File(exportPath + "/resultados_" + PDFUtils.formatSeedName(dependency.getName()) + ".zip");
					FileOutputStream fos = new FileOutputStream(zipFileToSend);
					ZipOutputStream zipOut = new ZipOutputStream(fos);
					List<File> files = new ArrayList<>();
					files.add(pdfZip);
					files.add(xlsx);
					for (File file : files) {
						FileInputStream fis = new FileInputStream(file);
						ZipEntry zipEntry = new ZipEntry(file.getName());
						zipOut.putNextEntry(zipEntry);
						byte[] bytes = new byte[1024];
						int length;
						while ((length = fis.read(bytes)) >= 0) {
							zipOut.write(bytes, 0, length);
						}
						fis.close();
					}
					zipOut.close();
					fos.close();
					sendMailToUra(idObs, dependency, ura, iterationRangesMap, zipFileToSend.getPath(), zipFileToSend.getName(), emailSubject);
				}
			}
		}
		DataBaseManager.closeConnection(c);
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
		for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
			buildPdf(idObservatory, idObservatoryExecution, fulfilledCrawling.getId(), fulfilledCrawling.getIdCrawling());
		}
		PropertiesManager pmgr = new PropertiesManager();
		return ZipUtils.pdfsZipToMap(idObservatory, idObservatoryExecution, pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav"));
	}

	/**
	 * Send mail to ura.
	 *
	 * @param idObservatory the id observatory
	 * @param ura           the ura
	 * @param attachUrl     the attach url
	 * @param attachName    the attach name
	 * @throws Exception
	 */
	private static void sendMailToUra(final Long idObservatory, final DependenciaForm ura, final UraSendResultForm uraCustom, final Map<Long, TemplateRangeForm> iterationRangesMap,
			final String attachUrl, final String attachName, final String emailSubject) throws Exception {
		final String observatoryName = getObservatoryName(idObservatory);
		// TODO Compose boy with template
		// todo MARKS
		TemplateRangeForm template = iterationRangesMap.get(uraCustom.getRange().getId());
		String templateMail = template.getTemplate();
		templateMail.replace("_ura_name_", ura.getName());
		if (ura.isOfficial()) {
			templateMail = templateMail.replace(StringUtils.substringBetween(templateMail, "[oficiosa]", "[/oficiosa]"), "");
			templateMail = templateMail.replace("[oficial]", "");
			templateMail = templateMail.replace("[/oficial]", "");
			templateMail = templateMail.replace("[oficiosa]", "");
			templateMail = templateMail.replace("[/oficiosa]", "");
		} else {
			templateMail = templateMail.replace(StringUtils.substringBetween(templateMail, "[oficial]", "[/oficial]"), "");
			templateMail = templateMail.replace("[oficial]", "");
			templateMail = templateMail.replace("[/oficial]", "");
			templateMail = templateMail.replace("[oficiosa]", "");
			templateMail = templateMail.replace("[/oficiosa]", "");
		}
		templateMail = templateMail.replace("_ura_name_", ura.getName());
		templateMail = templateMail.replace("_ura_custom_text_", uraCustom.getTemplate());
		StringBuilder mailBody = new StringBuilder(templateMail);
		// TODO Email subject
		final MailService mailService = new MailService();
		// TODO Get emails from URA
//		mailsTo.add("alvaro.pelaez@ctic.es");
		List<String> mailsTo = Arrays.asList(ura.getEmails().split(";"));
		mailService.sendMail(mailsTo, emailSubject, mailBody.toString(), attachUrl, attachName, true);
		// TODO Mark as send
		uraCustom.setSend(true);
		Connection c = DataBaseManager.getConnection();
		UraSendResultDAO.markSend(c, uraCustom);
		DataBaseManager.closeConnection(c);
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
			AnnexUtils.generateEmailAnnex(messageResources, idObs, idObsExecution, idOperation, exObsIds, comparision);
			final PropertiesManager pmgr = new PropertiesManager();
			final String exportPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path");
			return exportPath + idOperation;
		} catch (Exception e) {
			Logger.putLog("Exception generando los anexos.", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}
}
