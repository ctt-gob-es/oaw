package es.inteco.rastreador2.action.basic.service;

import es.ctic.basicservice.historico.CheckHistoricoService;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.pdf.BasicServiceExport;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2004;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceConcurrenceSystem;
import es.inteco.rastreador2.utils.basic.service.BasicServiceQueingThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceThread;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.utils.FileUtils;
import es.inteco.utils.MailUtils;
import org.apache.commons.mail.EmailException;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.inteco.common.Constants.*;

public class BasicServiceAction extends Action {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final String action = request.getParameter(Constants.ACTION);

        final BasicServiceForm basicServiceForm = BasicServiceUtils.getBasicServiceForm((BasicServiceForm) form, request);
        if (basicServiceForm.isContentAnalysis()) {
            basicServiceForm.setName(BasicServiceUtils.getTitleFromContent(basicServiceForm.getContent()));
        } else {
            basicServiceForm.setName(new URL(basicServiceForm.getDomain()).getAuthority());
        }

        basicServiceForm.setDomain(BasicServiceUtils.checkIDN(basicServiceForm.getDomain()));
        // ¡No validan que la URL esté bien codificada!
        if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
            basicServiceForm.setDomain(es.inteco.utils.CrawlerUtils.encodeUrl(basicServiceForm.getDomain()));
        }
        final ActionErrors errors = basicServiceForm.validate(mapping, request);
        errors.add(BasicServiceUtils.validateReport(basicServiceForm));
        errors.add(BasicServiceUtils.validateUrlOrContent(basicServiceForm));

        if (errors.isEmpty()) {
            if (Constants.EXECUTE.equalsIgnoreCase(action)) {
                Logger.putLog("EXECUTE --  " + basicServiceForm.toString(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
                // TODO: Quitar param request
                executeCrawling(basicServiceForm, request);
            } else {
                Logger.putLog("ENQUEUE --  " + basicServiceForm.toString(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
                final String serverResponse = enqueueCrawling(basicServiceForm);
                CrawlerUtils.returnText(response, serverResponse, false);
            }
        } else {
            final String serverResponse = processValidationErrors(basicServiceForm, errors);
            CrawlerUtils.returnText(response, serverResponse, false);
        }

        return null;
    }


    private String enqueueCrawling(final BasicServiceForm basicServiceForm) {
        // Guardamos en base de datos y en los logs las peticiones de análisis
        Logger.putLog("El usuario " + basicServiceForm.getUser() + " ha lanzado una petición de análisis en el servicio básico", BasicServiceAction.class, Logger.LOG_LEVEL_INFO);

        final String serverResponse;
        if (!BasicServiceConcurrenceSystem.passConcurrence()) {
            serverResponse = processTooManyRequests(basicServiceForm);
        } else {
            serverResponse = processBasicServiceRequest(basicServiceForm);
        }

        return serverResponse;
    }

    private String processBasicServiceRequest(final BasicServiceForm basicServiceForm) {
        final PropertiesManager pmgr = new PropertiesManager();
        basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED));

        // Lanzamos el análisis
        final BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
        basicServiceThread.start();

        return pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.launched");
    }

    private String processValidationErrors(final BasicServiceForm basicServiceForm, final ActionErrors errors) {
        final PropertiesManager pmgr = new PropertiesManager();
        final StringBuilder text = new StringBuilder(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.validation.errors"));

        for (Iterator<ActionMessage> iterator = errors.get(); iterator.hasNext(); ) {
            final ActionMessage message = iterator.next();
            Logger.putLog(message.getKey(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
            text.append("\n - ").append(MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, message.getKey()), message.getValues()));
        }

        basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_MISSING_PARAMS));

        return text.toString();
    }

    private String processTooManyRequests(final BasicServiceForm basicServiceForm) {
        final PropertiesManager pmgr = new PropertiesManager();
        basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_QUEUED));

        // Ponemos el análisis en cola
        final BasicServiceQueingThread basicServiceQueingThread = new BasicServiceQueingThread(basicServiceForm);
        basicServiceQueingThread.start();

        return pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.queued");
    }

    private ActionForward executeCrawling(final BasicServiceForm basicServiceForm, final HttpServletRequest request) {
        Logger.putLog("executeCrawling", BasicServiceAction.class, Logger.LOG_LEVEL_DEBUG);

        String pdfPath = null;

        final PropertiesManager pmgr = new PropertiesManager();
        try {
            //Lanzamos el rastreo de INTAV
            final CrawlerJob crawlerJob = new CrawlerJob();
            // La variable idCrawling es el campo cod_rastreo en la tabla tanalisis
            final Long idCrawling = basicServiceForm.getId() * (-1);
            final CrawlerData crawlerData = createCrawlerData(basicServiceForm, BasicServiceUtils.getGuideline(basicServiceForm.getReport()), idCrawling);

            final List<CrawledLink> crawledLinks;
            if (crawlerData.getUrls() != null && !crawlerData.getUrls().isEmpty()) {
                crawledLinks = crawlerJob.testCrawler(crawlerData);
            } else {
                crawledLinks = crawlerJob.runSimpleAnalysis(crawlerData);
            }

            if (!crawledLinks.isEmpty()) {
                final List<?> historicoAnalisis = new LinkedList<>();
                final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
                if (basicServiceForm.isRegisterAnalysis()) {
                    if (basicServiceForm.isDeleteOldAnalysis()) {
                        if (checkHistoricoService.isAnalysisOfUrl(basicServiceForm.getAnalysisToDelete(), basicServiceForm.getDomain())) {
                            // Si el analisis marcado corresponde a un análisis de esa url lo borramos
                            Logger.putLog("Borrando analisis antiguo " + basicServiceForm.getAnalysisToDelete(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
                            checkHistoricoService.deleteAnalysis(basicServiceForm.getName(), basicServiceForm.getAnalysisToDelete());
                        } else {
                            Logger.putLog("Los datos indicados no corresponden a ningún analisis registrado para " + basicServiceForm.getDomain(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
                        }
                    }
                }
                final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "file.date.format"));

                pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "pdf.basic.service.path") + idCrawling + File.separator + basicServiceForm.getName() + "_" + df.format(new Date()) + ".pdf";

                //Creamos el PDF con los resultados del rastreo
                final List<Long> evaluationIds = DiagnosisDAO.getEvaluationIds(idCrawling);
                if (basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_1_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_2_FILE)) {
                    BasicServiceExport.generatePDF(CrawlerUtils.getResources(request), basicServiceForm, evaluationIds, idCrawling, pdfPath);
                } else if (basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ||
                        basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_FILE) || basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_1_NOBROKEN)) {
                    Logger.putLog("Exportando desde BasicService a PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2004() ...", BasicServiceAction.class, Logger.LOG_LEVEL_DEBUG);
                    final String content = basicServiceForm.isContentAnalysis() ? basicServiceForm.getContent() : null;
                    PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2004(), idCrawling, evaluationIds, request, pdfPath, basicServiceForm.getName(), content, -System.currentTimeMillis(), 1);
                } else if (Constants.REPORT_OBSERVATORY_2.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(basicServiceForm.getReport())) {
                    Logger.putLog("Exportando desde BasicService a PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012() ...", BasicServiceAction.class, Logger.LOG_LEVEL_DEBUG);
                    final String content = basicServiceForm.isContentAnalysis() ? basicServiceForm.getContent() : null;
                    PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(basicServiceForm), idCrawling, evaluationIds, request, pdfPath, basicServiceForm.getName(), content, -System.currentTimeMillis(), 1);
                }

                // Comprimimos el fichero
                pdfPath = BasicServiceExport.compressReport(pdfPath);

                if (!basicServiceForm.isRegisterAnalysis()) {
                    // Si no es necesario registrar el análisis se borra
                    Logger.putLog("Borrando analisis " + idCrawling, BasicServiceAction.class, Logger.LOG_LEVEL_INFO);
                    checkHistoricoService.deleteAnalysis(basicServiceForm.getName(), idCrawling.toString());
                }

                //Lo enviamos por correo electrónico
                final String subject = getMailSubject(basicServiceForm.getReport());
                final String text = getMailText(basicServiceForm);
                final ArrayList<String> mailTo = new ArrayList<>();
                mailTo.add(basicServiceForm.getEmail());
                final String mailFrom = pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.address.from");
                Logger.putLog("Enviando correo del servicio de diagnóstico", BasicServiceAction.class, Logger.LOG_LEVEL_INFO);
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
            CrawlerUtils.warnAdministrators(e, BasicServiceAction.class);
            Logger.putLog("Excepcion: ", BasicServiceAction.class, Logger.LOG_LEVEL_ERROR, e);
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_ERROR_SENDING_EMAIL);
        } catch (Exception e) {
            // Avisamos del error al usuario
            final String message = MessageFormat.format(pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.text"), basicServiceForm.getUser());
            BasicServiceUtils.somethingWasWrongMessage(basicServiceForm, message);

            // Informamos de la excepción a los administradores
            CrawlerUtils.warnAdministrators(e, BasicServiceAction.class);
            Logger.putLog("Excepcion: ", BasicServiceAction.class, Logger.LOG_LEVEL_ERROR, e);
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_ERROR);
        } finally {
            if (pdfPath != null && StringUtils.isNotEmpty(pdfPath)) {
                // Borramos la carpeta con el PDF generado
                File pdfFile = new File(pdfPath);
                FileUtils.deleteDir(pdfFile.getParentFile());
            }
        }

        return null;
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
            urls.add(basicServiceForm.getDomain());
            crawlerData.setUrls(urls);
        } else {
            crawlerData.setContent(basicServiceForm.getContent());
        }

        crawlerData.setFicheroNorma(includeBrokenLinksCheck(CrawlerUtils.getFicheroNorma(idGuideline), basicServiceForm.getReport()));
        crawlerData.setDomains(es.inteco.utils.CrawlerUtils.addDomainsToList(basicServiceForm.getDomain(), true, Constants.ID_LISTA_SEMILLA));
        crawlerData.setInDirectory(basicServiceForm.isInDirectory());
        Logger.putLog(crawlerData.getFicheroNorma(), BasicServiceAction.class, Logger.LOG_LEVEL_DEBUG);
        return crawlerData;
    }

    private String includeBrokenLinksCheck(final String ficheroNorma, final String report) {
        if (report.endsWith("-nobroken")) {
            return ficheroNorma.substring(0, ficheroNorma.length() - 4) + "-nobroken.xml";
        } else {
            return ficheroNorma;
        }
    }

    private String getMailSubject(final String reportType) {
        final PropertiesManager pmgr = new PropertiesManager();
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
        final PropertiesManager pmgr = new PropertiesManager();
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

}
