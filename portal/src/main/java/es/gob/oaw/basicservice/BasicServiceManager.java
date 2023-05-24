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
package es.gob.oaw.basicservice;

import static es.inteco.common.Constants.BASIC_SERVICE_PROPERTIES;
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.apache.commons.mail.EmailException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts.util.MessageResources;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.odftoolkit.odfdom.doc.OdfTextDocument;

import es.gob.oaw.basicservice.historico.CheckHistoricoService;
import es.gob.oaw.rastreador2.observatorio.ObservatoryManager;
import es.gob.oaw.rastreador2.pdf.SourceFilesManager;
import es.gob.oaw.rastreador2.pdf.basicservice.BasicServicePdfReport;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.intav.dao.TAnalisisAccesibilidadDAO;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfAccesibilidad;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2004;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012b;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNEEN2019;
//import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceConcurrenceSystem;
import es.inteco.rastreador2.utils.basic.service.BasicServiceQueingThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.utils.FileUtils;
import es.oaw.wcagem.NoWebpage;
import es.oaw.wcagem.WcagEmReport;
import es.oaw.wcagem.WcagEmUtils;
import es.oaw.wcagem.WcagOdsUtils;
import es.oaw.wcagem.WcagOdtUtils;
import es.oaw.wcagem.WcagXlsxUtils;

/**
 * Created by mikunis on 1/10/17.
 */
public class BasicServiceManager {
	/** The pmgr. */
	private final PropertiesManager pmgr = new PropertiesManager();
	/** The mail service. */
	private final BasicServiceMailService mailService = new BasicServiceMailService();

	/**
	 * Enqueue crawling.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the string
	 */
	public String enqueueCrawling(final BasicServiceForm basicServiceForm) {
		// Guardamos en base de datos y en los logs las peticiones de análisis
		Logger.putLog("El usuario " + basicServiceForm.getUser() + " ha lanzado una petición de análisis en el servicio básico", BasicServiceManager.class, Logger.LOG_LEVEL_INFO);
		final String serverResponse;
		if (!BasicServiceConcurrenceSystem.passConcurrence()) {
			serverResponse = processTooManyRequests(basicServiceForm);
		} else {
			serverResponse = processBasicServiceRequest(basicServiceForm);
		}
		return serverResponse;
	}

	/**
	 * Process too many requests.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the string
	 */
	private String processTooManyRequests(final BasicServiceForm basicServiceForm) {
		basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_QUEUED));
		// Ponemos el análisis en cola
		final BasicServiceQueingThread basicServiceQueingThread = new BasicServiceQueingThread(basicServiceForm);
		basicServiceQueingThread.start();
		return pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.queued");
	}

	/**
	 * Process basic service request.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the string
	 */
	private String processBasicServiceRequest(final BasicServiceForm basicServiceForm) {
		basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED));
		// Lanzamos el análisis
		final BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
		basicServiceThread.start();
		return pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.launched");
	}

	/**
	 * Execute crawling.
	 *
	 * @param basicServiceForm the basic service form
	 * @param messageResources the message resources
	 */
	public void executeCrawling(final BasicServiceForm basicServiceForm, final MessageResources messageResources) {
		Logger.putLog("executeCrawling", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);
		String pdfPath = null;
		try {
			// Lanzamos el rastreo de INTAV
			final BasicServiceCrawlingManager basicServiceCrawlingManager = new BasicServiceCrawlingManager();
			final List<CrawledLink> crawledLinks = basicServiceCrawlingManager.getCrawledLinks(basicServiceForm);
			final Long idCrawling = basicServiceForm.getId() * (-1);
			if (!crawledLinks.isEmpty()) {
				final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
				if (basicServiceForm.isRegisterAnalysis()) {
					if (basicServiceForm.isDeleteOldAnalysis()) {
						if (checkHistoricoService.isAnalysisOfUrl(basicServiceForm.getAnalysisToDelete(), basicServiceForm.getDomain(), basicServiceForm.getAnalysisType(),
								basicServiceForm.getAmplitud(), basicServiceForm.getProfundidad(), basicServiceForm.getReport())) {
							// Si el analisis marcado corresponde a un análisis
							// de esa url lo borramos
							Logger.putLog("Borrando analisis antiguo " + basicServiceForm.getAnalysisToDelete(), BasicServiceManager.class, Logger.LOG_LEVEL_ERROR);
							checkHistoricoService.deleteAnalysis(basicServiceForm.getName(), basicServiceForm.getAnalysisToDelete());
						} else {
							Logger.putLog("Los datos indicados no corresponden a ningún analisis registrado para " + basicServiceForm.getDomain(), BasicServiceManager.class, Logger.LOG_LEVEL_ERROR);
						}
					}
				}
				final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "file.date.format"));
				pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "pdf.basic.service.path") + idCrawling + File.separator + PDFUtils.formatSeedName(basicServiceForm.getReportName()) + "_"
						+ df.format(new Date()) + ".pdf";
				final ObservatoryManager observatoryManager = new ObservatoryManager();
				if (basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE) || basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE_FILE)
						|| basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_1_FILE) || basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_2_FILE)) {
				} else if (basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) || basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_FILE)
						|| basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_1_NOBROKEN)) {
					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2004() ...", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);
					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = new TreeMap<>();
					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2004(basicServiceForm));
					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
					// Odt report: Hallazgos
					generateOdtReport(currentEvaluationPageList, pdfPath);
				} else if (Constants.REPORT_OBSERVATORY_2.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(basicServiceForm.getReport())) {
					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2012())", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);
					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);
					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2012(basicServiceForm));
					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
					// Odt report: Hallazgos
					generateOdtReport(currentEvaluationPageList, pdfPath);
				} else if (Constants.REPORT_OBSERVATORY_3.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_3_NOBROKEN.equals(basicServiceForm.getReport())) {
					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2012b())", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);
					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);
					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2012b(basicServiceForm));
					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
					// Odt report: Hallazgos
					generateOdtReport(currentEvaluationPageList, pdfPath);
				} else if (Constants.REPORT_OBSERVATORY_4.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_4_NOBROKEN.equals(basicServiceForm.getReport())) {
					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNEEN2019())", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);
					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);
					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNEEN2019(basicServiceForm));
					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
					// Odt report: Hallazgos
					generateOdtReport(currentEvaluationPageList, pdfPath);
				} else if (Constants.REPORT_OBSERVATORY_5.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_5_NOBROKEN.equals(basicServiceForm.getReport())) {
					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfAccesibilidad())", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);
					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);
					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfAccesibilidad(basicServiceForm));
					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
					// Odt report: Hallazgos
					generateOdtReport(currentEvaluationPageList, pdfPath);
				}
				// JSON WCAG-EM and ODS
				if ("true".equalsIgnoreCase(basicServiceForm.getDepthReport())) {
					WcagEmReport report = WcagEmUtils.generateReport(messageResources, new AnonymousResultExportPdfUNEEN2019(basicServiceForm), basicServiceForm.getName(), idCrawling);
					report.getGraph().get(0).getStructuredSample().setNoWebpage(getNoWebPages(crawledLinks));
					// END PDF FILES
					// ODS REPORT
					SpreadSheet ods = WcagOdsUtils.generateOds(report);
					File outputFile = new File(new File(pdfPath).getParentFile().getPath() + "/Informe Revision Accesibilidad - Sitios web.ods");
					ods.saveAs(outputFile);
					// XLSX REPORT
					Workbook wb = WcagXlsxUtils.generateXlsx(report);
					File outputFilexlsx = new File(new File(pdfPath).getParentFile().getPath() + "/Informe Revision Accesibilidad - Sitios web.xlsx");
					wb.write(new FileOutputStream(outputFilexlsx));
				}
				// Generar código analizado
				final SourceFilesManager sourceFilesManager = new SourceFilesManager(new File(pdfPath).getParentFile());
				final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
				// Source code analysis
				if (basicServiceForm.isContentAnalysisMultiple()) {
					sourceFilesManager.writeSourceFilesContentMultiple(DataBaseManager.getConnection(), analysisIdsByTracking);
					sourceFilesManager.zipSourcesContent(true);
				} else if (basicServiceForm.isContentAnalysis()) {
					sourceFilesManager.writeSourceFilesContent(DataBaseManager.getConnection(), analysisIdsByTracking, basicServiceForm.getFileName());
					sourceFilesManager.zipSourcesContent(true);
				} else {
					if (Constants.REPORT_OBSERVATORY_5.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_5_NOBROKEN.equals(basicServiceForm.getReport())) {
						// Add accesibility page if exists
						String codFuente = TAnalisisAccesibilidadDAO.getSourceCode(DataBaseManager.getConnection(), idCrawling);
						if (!org.apache.commons.lang3.StringUtils.isEmpty(codFuente)) {
							sourceFilesManager.writeSourceFilesAccessibility(DataBaseManager.getConnection(), codFuente);
						}
					}
					sourceFilesManager.writeSourceFiles(DataBaseManager.getConnection(), analysisIdsByTracking);
					sourceFilesManager.zipSources(true);
				}
				// Comprimimos el fichero
				pdfPath = BasicServiceExport.compressReportWithCode(pdfPath, basicServiceForm.isContentAnalysis(), basicServiceForm.getFileName(), basicServiceForm.getDepthReport());
				if (!basicServiceForm.isRegisterAnalysis()) {
					// Si no es necesario registrar el análisis se borra
					Logger.putLog("Borrando analisis " + idCrawling, BasicServiceManager.class, Logger.LOG_LEVEL_INFO);
					checkHistoricoService.deleteAnalysis(basicServiceForm.getName(), idCrawling.toString());
				}
				// Lo enviamos por correo electrónico
				Logger.putLog("Enviando correo del servicio de diagnóstico", BasicServiceManager.class, Logger.LOG_LEVEL_INFO);
				mailService.sendBasicServiceReport(basicServiceForm, pdfPath, new File(pdfPath).getName());
				BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_FINISHED);
			} else {
				// Avisamos de que ha sido imposible acceder a la página a
				// rastrear
				final String message = MessageFormat.format(pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.not.crawled.text"), basicServiceForm.getUser(), basicServiceForm.getDomain());
				mailService.sendBasicServiceErrorMessage(basicServiceForm, message);
				BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_NOT_CRAWLED);
			}
		} catch (EmailException e) {
			// Intentamos informar a los administradores, aunque seguramente no
			// se pueda
			CrawlerUtils.warnAdministrators(e, BasicServiceManager.class);
			Logger.putLog("Excepcion: ", BasicServiceManager.class, Logger.LOG_LEVEL_ERROR, e);
			BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_ERROR_SENDING_EMAIL);
		} catch (Exception e) {
			// Avisamos del error al usuario
			final String message = MessageFormat.format(pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.text"), basicServiceForm.getUser());
			mailService.sendBasicServiceErrorMessage(basicServiceForm, message);
			// Informamos de la excepción a los administradores
			CrawlerUtils.warnAdministrators(e, BasicServiceManager.class);
			Logger.putLog("Excepcion: ", BasicServiceManager.class, Logger.LOG_LEVEL_ERROR, e);
			BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_ERROR);
		} finally {
			if (pdfPath != null && StringUtils.isNotEmpty(pdfPath)) {
				// Borramos la carpeta con el PDF generado
				File pdfFile = new File(pdfPath);
				FileUtils.deleteDir(pdfFile.getParentFile());
			}
		}
	}

	/**
	 * Adding Pdf files to IRA Reports
	 * 
	 * @param crawledLinks
	 * @return
	 */
	private List<NoWebpage> getNoWebPages(List<CrawledLink> crawledLinks) {
		// PDF FILES
		List<NoWebpage> noWebPages = new ArrayList<>();
		int randCounterNoWeb = 0;
		// Iterate currentEvaluationPageList to preserve order
		for (CrawledLink crawledLink : crawledLinks) {
			if (crawledLink.getUrl() != null && crawledLink.getUrl().contains(".pdf")) {
				NoWebpage noWebpage = new NoWebpage();
				noWebpage.setType(Arrays.asList(new String[] { "TestSubject", "WebPage" }));
				noWebpage.setId("_:struct_" + randCounterNoWeb);
				noWebpage.setDescription(crawledLink.getUrl());
				noWebpage.setSource(crawledLink.getUrl());
				noWebpage.setTitle("Doc");
				noWebpage.setTested(false);// false to mark as incomplete un report step
				noWebPages.add(noWebpage);
				randCounterNoWeb++;
			}
		}
		return noWebPages;
	}

	/**
	 * Generate odt report
	 * 
	 * @param currentEvaluationPageList
	 * @param pdfPath
	 * @throws Exception
	 */
	private void generateOdtReport(final List<ObservatoryEvaluationForm> currentEvaluationPageList, final String pdfPath) throws Exception {
		OdfTextDocument document = WcagOdtUtils.generateOdtReport(currentEvaluationPageList);
		if (Objects.nonNull(document)) {
			final FileOutputStream out = new FileOutputStream(new File(pdfPath).getParentFile().getPath() + "/Informe Revision Accesibilidad - Hallazgos.odt");
			document.save(out);
		}
	}
}
