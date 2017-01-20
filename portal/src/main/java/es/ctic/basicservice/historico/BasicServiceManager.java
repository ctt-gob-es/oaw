package es.ctic.basicservice.historico;


import es.ctic.rastreador2.observatorio.ObservatoryManager;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.basicservice.BasicServicePdfReport;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2004;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceConcurrenceSystem;
import es.inteco.rastreador2.utils.basic.service.BasicServiceQueingThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.utils.FileUtils;
import es.inteco.utils.MailUtils;
import org.apache.commons.mail.EmailException;
import org.apache.struts.util.MessageResources;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.inteco.common.Constants.*;

/**
 * Created by mikunis on 1/10/17.
 */
public class BasicServiceManager {

    private final PropertiesManager pmgr = new PropertiesManager();

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
            //Lanzamos el rastreo de INTAV
            final CrawlerJob crawlerJob = new CrawlerJob();
            // La variable idCrawling es el campo cod_rastreo en la tabla tanalisis
            final Long idCrawling = basicServiceForm.getId() * (-1);
            final CrawlerData crawlerData = createCrawlerData(basicServiceForm, BasicServiceUtils.getGuideline(basicServiceForm.getReport()), idCrawling);

            final List<CrawledLink> crawledLinks;
            if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.URL) {
                Logger.putLog("URL " + basicServiceForm.toString(), BasicServiceManager.class, Logger.LOG_LEVEL_ERROR);
                crawledLinks = crawlerJob.testCrawler(crawlerData);
            } else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.LISTA_URLS) {
                // Si es una lista de urls modificamos la información para que no se realice crawling.
                disableCrawling(crawlerData);
                Logger.putLog("LISTA URLS " + basicServiceForm.toString(), BasicServiceManager.class, Logger.LOG_LEVEL_ERROR);
                crawledLinks = crawlerJob.testCrawler(crawlerData);
            } else if (basicServiceForm.getAnalysisType() == BasicServiceAnalysisType.CODIGO_FUENTE) {
                Logger.putLog("CODIGO FUENTE" + basicServiceForm.toString(), BasicServiceManager.class, Logger.LOG_LEVEL_ERROR);
                crawledLinks = crawlerJob.runSimpleAnalysis(crawlerData);
            } else {
                crawledLinks = Collections.emptyList();
            }

            if (!crawledLinks.isEmpty()) {
                final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
                if (basicServiceForm.isRegisterAnalysis()) {
                    if (basicServiceForm.isDeleteOldAnalysis()) {
                        if (checkHistoricoService.isAnalysisOfUrl(basicServiceForm.getAnalysisToDelete(), basicServiceForm.getDomain())) {
                            // Si el analisis marcado corresponde a un análisis de esa url lo borramos
                            Logger.putLog("Borrando analisis antiguo " + basicServiceForm.getAnalysisToDelete(), BasicServiceManager.class, Logger.LOG_LEVEL_ERROR);
                            checkHistoricoService.deleteAnalysis(basicServiceForm.getName(), basicServiceForm.getAnalysisToDelete());
                        } else {
                            Logger.putLog("Los datos indicados no corresponden a ningún analisis registrado para " + basicServiceForm.getDomain(), BasicServiceManager.class, Logger.LOG_LEVEL_ERROR);
                        }
                    }
                }
                final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "file.date.format"));

                pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "pdf.basic.service.path") + idCrawling + File.separator + PDFUtils.formatSeedName(basicServiceForm.getName()) + "_" + df.format(new Date()) + ".pdf";

                final ObservatoryManager observatoryManager = new ObservatoryManager();

                if (basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_1_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_2_FILE)) {
                    //Creamos el PDF con los resultados del rastreo
                    final List<Long> evaluationIds = DiagnosisDAO.getEvaluationIds(idCrawling);
                    // BasicServiceExport.generatePDF(messageResources, basicServiceForm, evaluationIds, idCrawling, pdfPath);
                } else if (basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ||
                        basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_FILE) || basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_1_NOBROKEN)) {
                    Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2004() ...", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);

                    final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
                    final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);
                    final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = new TreeMap<>();

                    final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2004(basicServiceForm));
                    basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
                    //.exportToPdf(new AnonymousResultExportPdfUNE2004(), idCrawling, evaluationIds, Collections.<Long>emptyList(), messageResources, pdfPath, basicServiceForm.getName(), content, -System.currentTimeMillis(), 1);
                } else if (Constants.REPORT_OBSERVATORY_2.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(basicServiceForm.getReport())) {
                    Logger.putLog("Exportando desde BasicService a BasicServicePdfReport(new AnonymousResultExportPdfUNE2012())", BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);

                    final List<Long> analysisIdsByTracking = AnalisisDatos.getAnalysisIdsByTracking(DataBaseManager.getConnection(), idCrawling);
                    final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(0, analysisIdsByTracking);

                    final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationsPageList = checkHistoricoService.getHistoricoResultadosOfBasicService(basicServiceForm);

                    final BasicServicePdfReport basicServicePdfReport = new BasicServicePdfReport(messageResources, new AnonymousResultExportPdfUNE2012(basicServiceForm));
                    basicServicePdfReport.exportToPdf(currentEvaluationPageList, previousEvaluationsPageList, pdfPath);
                }

                // Comprimimos el fichero
                pdfPath = BasicServiceExport.compressReport(pdfPath);

                if (!basicServiceForm.isRegisterAnalysis()) {
                    // Si no es necesario registrar el análisis se borra
                    Logger.putLog("Borrando analisis " + idCrawling, BasicServiceManager.class, Logger.LOG_LEVEL_INFO);
                    checkHistoricoService.deleteAnalysis(basicServiceForm.getName(), idCrawling.toString());
                }

                //Lo enviamos por correo electrónico
                final String subject = getMailSubject(basicServiceForm.getReport());
                final String text = getMailText(basicServiceForm);
                final ArrayList<String> mailTo = new ArrayList<>();
                mailTo.add(basicServiceForm.getEmail());
                final String mailFrom = pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.address.from");
                Logger.putLog("Enviando correo del servicio de diagnóstico", BasicServiceManager.class, Logger.LOG_LEVEL_INFO);
                MailUtils.sendMail(mailFrom, "Servicio on-line de diagnóstico de Accesibilidad",
                        mailTo, subject, text, pdfPath, new File(pdfPath).getName(), mailFrom, "Servicio on-line de diagnóstico de Accesibilidad", false);
                BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_FINISHED);
            } else {
                // Avisamos de que ha sido imposible acceder a la página a rastrear
                final String message = MessageFormat.format(pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.not.crawled.text"), basicServiceForm.getUser(), basicServiceForm.getDomain());
                BasicServiceUtils.somethingWasWrongMessage(basicServiceForm, message);

                BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_NOT_CRAWLED);
            }
        } catch (EmailException e) {
            // Intentamos informar a los administradores, aunque seguramente no se pueda
            CrawlerUtils.warnAdministrators(e, BasicServiceManager.class);
            Logger.putLog("Excepcion: ", BasicServiceManager.class, Logger.LOG_LEVEL_ERROR, e);
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_ERROR_SENDING_EMAIL);
        } catch (Exception e) {
            // Avisamos del error al usuario
            final String message = MessageFormat.format(pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.text"), basicServiceForm.getUser());
            BasicServiceUtils.somethingWasWrongMessage(basicServiceForm, message);

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

    private String getMailSubject(final String reportType) {

        final String message = pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.subject");
        if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType) || Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
            return MessageFormat.format(message, "Observatorio UNE 2004");
        } else if (Constants.REPORT_OBSERVATORY_2.equals(reportType) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
            return MessageFormat.format(message, "Observatorio UNE 2012");
        } else if ("une".equals(reportType)) {
            return MessageFormat.format(message, "UNE 139803");
        }
        return "Informe de Accesibilidad Web";
    }

    private String getMailText(final BasicServiceForm basicServiceForm) {
        final String text;
        if (basicServiceForm.isContentAnalysis()) {
            text = MessageFormat.format(pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory.content"),
                    basicServiceForm.getUser(), reportToString(basicServiceForm.getReport()));
        } else {
            final String inDirectory = basicServiceForm.isInDirectory() ? pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes") : pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.indomain.no");
            text = MessageFormat.format(pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory"),
                    basicServiceForm.getUser(), basicServiceForm.getDomain(), basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), inDirectory, reportToString(basicServiceForm.getReport()));
        }

        return text;
    }

    private String reportToString(final String reportType) {
        if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType)) {
            return "Observatorio UNE 2004";
        } else if (Constants.REPORT_OBSERVATORY_2.equals(reportType)) {
            return "Observatorio UNE 2012";
        } else if ("observatorio-1-nobroken".equals(reportType)) {
            return "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
        } else if (Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
            return "Observatorio UNE 2012 (sin comprobar enlaces rotos)";
        } else {
            return reportType;
        }
    }

    private void disableCrawling(final CrawlerData crawlerData) {
        crawlerData.setProfundidad(1);
        crawlerData.setTopN(1);
        crawlerData.setPseudoaleatorio(false);
    }

    private CrawlerData createCrawlerData(final BasicServiceForm basicServiceForm, final Long idGuideline, final Long idCrawling) {
        final CrawlerData crawlerData = new CrawlerData();
        final String[] cartuchos = new String[]{"es.inteco.accesibilidad.CartuchoAccesibilidad"};
        crawlerData.setCartuchos(cartuchos);
        crawlerData.setIdCrawling(idCrawling);
        crawlerData.setIdFulfilledCrawling(idCrawling);
        crawlerData.setNombreRastreo(basicServiceForm.getName());
        crawlerData.setLanguage(basicServiceForm.getLanguage());
        crawlerData.setProfundidad(Integer.parseInt(basicServiceForm.getProfundidad()));
        crawlerData.setPseudoaleatorio(true);
        crawlerData.setTopN(Integer.parseInt(basicServiceForm.getAmplitud()));
        crawlerData.setUser(basicServiceForm.getUser());
        final ArrayList<String> mails = new ArrayList<>();
        mails.add(basicServiceForm.getEmail());
        crawlerData.setUsersMail(mails);
        crawlerData.setTest(true);
        if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
            final List<String> urls = new ArrayList<>();
            final String[] split = basicServiceForm.getDomain().split("\r\n");
            Collections.addAll(urls, split);
            crawlerData.setUrls(urls);
        }
        crawlerData.setContent(basicServiceForm.getContent());

        crawlerData.setFicheroNorma(includeBrokenLinksCheck(CrawlerUtils.getFicheroNorma(idGuideline), basicServiceForm.getReport()));
        crawlerData.setDomains(es.inteco.utils.CrawlerUtils.addDomainsToList(basicServiceForm.getDomain(), true, Constants.ID_LISTA_SEMILLA));
        crawlerData.setInDirectory(basicServiceForm.isInDirectory());
        Logger.putLog(crawlerData.getFicheroNorma(), BasicServiceManager.class, Logger.LOG_LEVEL_DEBUG);
        return crawlerData;
    }

    private String includeBrokenLinksCheck(final String ficheroNorma, final String report) {
        if (report.endsWith("-nobroken")) {
            return ficheroNorma.substring(0, ficheroNorma.length() - 4) + "-nobroken.xml";
        } else {
            return ficheroNorma;
        }
    }
}
