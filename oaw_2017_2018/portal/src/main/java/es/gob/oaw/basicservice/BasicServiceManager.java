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
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.mail.EmailException;
import org.apache.struts.util.MessageResources;

import es.gob.oaw.basicservice.historico.CheckHistoricoService;
import es.gob.oaw.rastreador2.observatorio.ObservatoryManager;
import es.gob.oaw.rastreador2.pdf.basicservice.BasicServicePdfReport;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2004;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2017;
//import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceConcurrenceSystem;
import es.inteco.rastreador2.utils.basic.service.BasicServiceQueingThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.utils.FileUtils;

/**
 * Created by mikunis on 1/10/17.
 */
public class BasicServiceManager {

	private final PropertiesManager pmgr = new PropertiesManager();
	private final BasicServiceMailService mailService = new BasicServiceMailService();

	/**
	 *
	 * @param basicServiceForm
	 * @return
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

	private String processTooManyRequests(final BasicServiceForm basicServiceForm) {
		basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_QUEUED));

		// Ponemos el análisis en cola
		final BasicServiceQueingThread basicServiceQueingThread = new BasicServiceQueingThread(basicServiceForm);
		basicServiceQueingThread.start();

		return pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.queued");
	}

	/**
	 *
	 * @param basicServiceForm
	 * @return
	 */
	private String processBasicServiceRequest(final BasicServiceForm basicServiceForm) {
		basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED));

		// Lanzamos el análisis
		final BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
		basicServiceThread.start();

		return pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.launched");
	}

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
						if (checkHistoricoService.isAnalysisOfUrl(basicServiceForm.getAnalysisToDelete(), basicServiceForm.getDomain(), basicServiceForm.getAnalysisType())) {
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

				pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "pdf.basic.service.path") + idCrawling + File.separator + PDFUtils.formatSeedName(basicServiceForm.getName()) + "_" + df.format(new Date())
						+ ".pdf";

				final ObservatoryManager observatoryManager = new ObservatoryManager();

				if (basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE) || basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE_FILE)
						|| basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_1_FILE) || basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_2_FILE)) {
					// Creamos el PDF con los resultados del rastreo
					// final List<Long> evaluationIds =
					// DiagnosisDAO.getEvaluationIds(idCrawling);
					// BasicServiceExport.generatePDF(messageResources,
					// basicServiceForm, evaluationIds, idCrawling, pdfPath);
				} else if (basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) || basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_FILE)
						|| basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_1_NOBROKEN)) {
					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2004() ...", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);

					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = new TreeMap<>();

					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2004(basicServiceForm));
					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
					// .exportToPdf(new AnonymousResultExportPdfUNE2004(),
					// idCrawling, evaluationIds, Collections.<Long>emptyList(),
					// messageResources, pdfPath, basicServiceForm.getName(),
					// content, -System.currentTimeMillis(), 1);
				} else if (Constants.REPORT_OBSERVATORY_2.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(basicServiceForm.getReport())) {
					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2012())", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);

					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);

					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);

					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2012(basicServiceForm));

					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
				} else if (Constants.REPORT_OBSERVATORY_3.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_3_NOBROKEN.equals(basicServiceForm.getReport())) {
					// TODO 2017 Desdoblamiento para nueva metodología

					Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2017())", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);

					final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
					final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);

					final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);

					final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2017(basicServiceForm));
					basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
				}

				// Comprimimos el fichero
				pdfPath = BasicServiceExport.compressReport(pdfPath);

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

}
