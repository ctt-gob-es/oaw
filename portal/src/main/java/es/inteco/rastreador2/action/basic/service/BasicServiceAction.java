package es.inteco.rastreador2.action.basic.service;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.crawler.job.CrawledLink;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.crawler.job.CrawlerJob;
import es.inteco.plugin.dao.DataBaseManager;
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
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_CORE_PROPERTIES;
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class BasicServiceAction extends Action {

    @Override
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final String action = request.getParameter(Constants.ACTION);

        if (Constants.EXECUTE.equalsIgnoreCase(action)) {
            executeCrawling(form, request);
        } else {
            launchCrawling(mapping, form, request, response);
        }

        return null;
    }


    private ActionForward launchCrawling(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final BasicServiceForm basicServiceForm = BasicServiceUtils.getBasicServiceForm((BasicServiceForm) form, request);

        // Guardamos en base de datos y en los logs las peticiones de análisis
        Logger.putLog("El usuario " + basicServiceForm.getUser() + " ha lanzado una petición de análisis en el servicio básico", BasicServiceAction.class, Logger.LOG_LEVEL_INFO);

        basicServiceForm.setDomain(BasicServiceUtils.checkIDN(basicServiceForm.getDomain()));
        final ActionErrors errors = basicServiceForm.validate(mapping, request);

        if (errors.isEmpty()) {
            errors.add(BasicServiceUtils.validateReport(basicServiceForm));
            errors.add(BasicServiceUtils.validateUrlOrContent(basicServiceForm));
        }

        // ¡No validan que la URL esté bien codificada!
        if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
            basicServiceForm.setDomain(es.inteco.utils.CrawlerUtils.encodeUrl(basicServiceForm.getDomain()));
        }

        if (!BasicServiceConcurrenceSystem.passConcurrence()) {
            basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_QUEUED));

            // Ponemos el análisis en cola
            final BasicServiceQueingThread basicServiceQueingThread = new BasicServiceQueingThread(basicServiceForm);
            basicServiceQueingThread.start();

            CrawlerUtils.returnText(getResources(request).getMessage("basic.service.queued"), response, false);
        } else if (errors.isEmpty()) {
            basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED));

            // Lanzamos el análisis
            final BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
            basicServiceThread.start();

            CrawlerUtils.returnText(getResources(request).getMessage("basic.service.launched"), response, false);
        } else {
            final StringBuilder text = new StringBuilder(getResources(request).getMessage("basic.service.validation.errors"));

            for (Iterator<ActionMessage> iterator = errors.get(); iterator.hasNext(); ) {
                final ActionMessage message = iterator.next();
                text.append("\n - ").append(getResources(request).getMessage(message.getKey(), message.getValues()));
            }

            basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_MISSING_PARAMS));

            CrawlerUtils.returnText(text.toString(), response, false);
        }

        return null;
    }

    private ActionForward executeCrawling(final ActionForm form, final HttpServletRequest request) throws Exception {
        Logger.putLog("executeCrawling", BasicServiceAction.class, Logger.LOG_LEVEL_DEBUG);

        Connection conn = null;

        String pdfPath = null;

        BasicServiceForm basicServiceForm = BasicServiceUtils.getBasicServiceForm((BasicServiceForm) form, request);
        if (basicServiceForm.isContentAnalysis()) {
            basicServiceForm.setName(BasicServiceUtils.getTitleFromContent(basicServiceForm.getContent()));
        } else {
            basicServiceForm.setName(new URL(basicServiceForm.getDomain()).getAuthority());
        }

        try {
            conn = DataBaseManager.getConnection();
            //Lanzamos el rastreo de INTAV
            final CrawlerJob crawlerJob = new CrawlerJob();
            final Long idCrawling = System.nanoTime() * (-1);
            final CrawlerData crawlerData = createCrawlerData(basicServiceForm, BasicServiceUtils.getGuideline(basicServiceForm.getReport()), idCrawling);

            final List<CrawledLink> crawledLinks;
            if (crawlerData.getUrls() != null && !crawlerData.getUrls().isEmpty()) {
                crawledLinks = crawlerJob.testCrawler(crawlerData);
            } else {
                crawledLinks = crawlerJob.runSimpleAnalysis(crawlerData);
            }

            if (!crawledLinks.isEmpty()) {
                final PropertiesManager pmgr = new PropertiesManager();
                final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "file.date.format"));

                pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "pdf.basic.service.path") + idCrawling + File.separator + basicServiceForm.getName() + "_" + df.format(new Date()) + ".pdf";

                //Creamos el PDF con los resultados del rastreo
                final List<Long> evaluationIds = DiagnosisDAO.getEvaluationIds(idCrawling);
                if (basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_1_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_2_FILE)) {
                    BasicServiceExport.generatePDF(request, basicServiceForm, evaluationIds, idCrawling, pdfPath);
                } else if (basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ||
                        basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_FILE) || basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_1_NOBROKEN)) {
                    Logger.putLog("Exportando desde BasicService a PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2004() ...", BasicServiceAction.class, Logger.LOG_LEVEL_DEBUG);
                    final String content = basicServiceForm.isContentAnalysis() ? basicServiceForm.getContent() : null;
                    PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2004(), idCrawling, evaluationIds, request, pdfPath, basicServiceForm.getName(), content, -System.currentTimeMillis(), 1);
                } else if (Constants.REPORT_OBSERVATORY_2.equals(basicServiceForm.getReport()) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(basicServiceForm.getReport())) {
                    Logger.putLog("Exportando desde BasicService a PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012() ...", BasicServiceAction.class, Logger.LOG_LEVEL_DEBUG);
                    final String content = basicServiceForm.isContentAnalysis() ? basicServiceForm.getContent() : null;
                    PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(), idCrawling, evaluationIds, request, pdfPath, basicServiceForm.getName(), content, -System.currentTimeMillis(), 1);
                }

                // Comprimimos el fichero
                pdfPath = BasicServiceExport.compressReport(pdfPath);
                try {
                    //Borramos el análisis
                    DiagnosisDAO.deleteAnalysis(conn, basicServiceForm.getName(), idCrawling);
                } catch (Exception e) {
                    Logger.putLog("Excepcion: ", BasicServiceAction.class, Logger.LOG_LEVEL_WARNING, e);
                }

                //Lo enviamos por correo electrónico
                final String subject = getMailSubject(basicServiceForm.getReport());
                final String text = getMailText(request, basicServiceForm);
                final ArrayList<String> mailTo = new ArrayList<String>();
                mailTo.add(basicServiceForm.getEmail());
                final String mailFrom = pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.address.from");
                Logger.putLog("Enviando correo del servicio de diagnóstico", BasicServiceAction.class, Logger.LOG_LEVEL_INFO);
                MailUtils.sendMail(mailFrom, "Servicio on-line de diagnóstico de Accesibilidad",
                        mailTo, subject, text, pdfPath, new File(pdfPath).getName(), mailFrom, "Servicio on-line de diagnóstico de Accesibilidad", false);
                BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_FINISHED);
            } else {
                // Avisamos de que ha sido imposible acceder a la página a rastrear
                BasicServiceUtils.somethingWasWrongMessage(request, basicServiceForm, getResources(request).getMessage("basic.service.mail.not.crawled.text", basicServiceForm.getUser(), basicServiceForm.getDomain()));

                BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_NOT_CRAWLED);
            }
        } catch (EmailException e) {
            // Intentamos informar a los administradores, aunque seguramente no se pueda
            CrawlerUtils.warnAdministrators(e, BasicServiceAction.class);
            Logger.putLog("Excepcion: ", BasicServiceAction.class, Logger.LOG_LEVEL_ERROR, e);
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_ERROR_SENDING_EMAIL);
        } catch (Exception e) {
            // Avisamos del error al usuario
            BasicServiceUtils.somethingWasWrongMessage(request, basicServiceForm, getResources(request).getMessage("basic.service.mail.error.text", basicServiceForm.getUser()));

            // Informamos de la excepción a los administradores
            CrawlerUtils.warnAdministrators(e, BasicServiceAction.class);
            Logger.putLog("Excepcion: ", BasicServiceAction.class, Logger.LOG_LEVEL_ERROR, e);
            BasicServiceUtils.updateRequestStatus(basicServiceForm, Constants.BASIC_SERVICE_STATUS_ERROR);
        } finally {
            DataBaseManager.closeConnection(conn);

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
        final ArrayList<String> mails = new ArrayList<String>();
        mails.add(basicServiceForm.getEmail());
        crawlerData.setUsersMail(mails);
        crawlerData.setTest(true);
        if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
            final List<String> urls = new ArrayList<String>();
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
        if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType) || Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
            return "Informe de Accesibilidad Web: Observatorio UNE 2004";
        } else if (Constants.REPORT_OBSERVATORY_2.equals(reportType) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
            return "Informe de Accesibilidad Web: Observatorio UNE 2012";
        } else if ("une".equals(reportType)) {
            return "Informe de Accesibilidad Web: UNE 139803";
        }
        return "Informe de Accesibilidad Web";
    }

    private String getMailText(final HttpServletRequest request, final BasicServiceForm basicServiceForm) {
        final String text;
        if (basicServiceForm.isContentAnalysis()) {
            final String[] args = {basicServiceForm.getUser(), BasicServiceUtils.getTitleFromContent(basicServiceForm.getContent()), basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), reportToString(basicServiceForm.getReport())};
            text = basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ? getResources(request).getMessage("basic.service.mail.text.observatory.content", args) : getResources(request).getMessage("basic.service.mail.text.une.content", args);
        } else {
            final String inDirectory = basicServiceForm.isInDirectory() ? getResources(request).getMessage("select.yes") : getResources(request).getMessage("select.no");
            final String[] args = {basicServiceForm.getUser(), basicServiceForm.getDomain(), basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), inDirectory, reportToString(basicServiceForm.getReport())};
            text = basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ? getResources(request).getMessage("basic.service.mail.text.observatory", args) : getResources(request).getMessage("basic.service.mail.text.une", args);
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
