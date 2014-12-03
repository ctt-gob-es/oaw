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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class BasicServiceAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String action = request.getParameter(Constants.ACTION);

        if (!StringUtils.isEmpty(action) && action.equalsIgnoreCase(Constants.EXECUTE)) {
            executeCrawling(form, request);
        } else {
            launchCrawling(mapping, form, request, response);
        }

        return null;
    }


    private ActionForward launchCrawling(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BasicServiceForm basicServiceForm = BasicServiceUtils.getBasicServiceForm((BasicServiceForm) form, request);

        // Guardamos en base de datos y en los logs las peticiones de análisis
        Logger.putLog("El usuario " + basicServiceForm.getUser() + " ha lanzado una petición de análisis en el servicio básico", BasicServiceAction.class, Logger.LOG_LEVEL_INFO);

        basicServiceForm.setDomain(BasicServiceUtils.checkIDN(basicServiceForm.getDomain()));
        ActionErrors errors = basicServiceForm.validate(mapping, request);

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
            BasicServiceQueingThread basicServiceQueingThread = new BasicServiceQueingThread(basicServiceForm);
            basicServiceQueingThread.start();

            CrawlerUtils.returnText(getResources(request).getMessage("basic.service.queued"), response, false);
        } else if (errors.isEmpty()) {
            basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_LAUNCHED));

            // Lanzamos el análisis
            BasicServiceThread basicServiceThread = new BasicServiceThread(basicServiceForm);
            basicServiceThread.start();

            CrawlerUtils.returnText(getResources(request).getMessage("basic.service.launched"), response, false);
        } else {
            StringBuilder text = new StringBuilder(getResources(request).getMessage("basic.service.validation.errors"));

            for (Iterator<ActionMessage> iterator = errors.get(); iterator.hasNext(); ) {
                ActionMessage message = iterator.next();
                text.append("\n - ").append(getResources(request).getMessage(message.getKey(), message.getValues()));
            }

            basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_MISSING_PARAMS));

            CrawlerUtils.returnText(text.toString(), response, false);
        }

        return null;
    }

    private ActionForward executeCrawling(ActionForm form, HttpServletRequest request) throws Exception {
        Logger.putLog("executeCrawling", BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
        PropertiesManager pmgr = new PropertiesManager();

        Connection conn = null;
        Connection c = null;

        String pdfPath = null;

        BasicServiceForm basicServiceForm = BasicServiceUtils.getBasicServiceForm((BasicServiceForm) form, request);
        if (basicServiceForm.isContentAnalysis()) {
            basicServiceForm.setName(BasicServiceUtils.getTitleFromContent(basicServiceForm.getContent()));
        } else {
            basicServiceForm.setName(new URL(basicServiceForm.getDomain()).getAuthority());
        }

        try {
            conn = DataBaseManager.getConnection();
            c = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));
            //Lanzamos el rastreo de INTAV
            CrawlerJob crawlerJob = new CrawlerJob();

            Long idCrawling = System.nanoTime() * (-1);

            CrawlerData crawlerData = createCrawlerData(basicServiceForm, BasicServiceUtils.getGuideline(basicServiceForm.getReport()), idCrawling);

            List<CrawledLink> crawledLinks;
            if (crawlerData.getUrls() != null && !crawlerData.getUrls().isEmpty()) {
                crawledLinks = crawlerJob.testCrawler(crawlerData);
            } else {
                crawledLinks = crawlerJob.runSimpleAnalysis(crawlerData);
            }

            if (!crawledLinks.isEmpty()) {
                DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "file.date.format"));

                pdfPath = pmgr.getValue(CRAWLER_PROPERTIES, "pdf.basic.service.path") + idCrawling + File.separator + basicServiceForm.getName() + "_" + df.format(new Date()) + ".pdf";

                //Creamos el PDF con los resultados del rastreo
                List<Long> evaluationIds = DiagnosisDAO.getEvaluationIds(idCrawling);
                if (basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_1_FILE) ||
                        basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_WCAG_2_FILE)) {
                    BasicServiceExport.generatePDF(request, basicServiceForm, evaluationIds, idCrawling, pdfPath);
                } else if (basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ||
                        basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY_FILE)) {
                    String content = basicServiceForm.isContentAnalysis() ? basicServiceForm.getContent() : null;
                    PrimaryExportPdfUtils.exportToPdf(idCrawling, evaluationIds, request, pdfPath, basicServiceForm.getName(), content, -System.currentTimeMillis(), 1);
                }

                // Comprimimos el fichero
                pdfPath = BasicServiceExport.compressReport(pdfPath);

                //Borramos el análisis
                DiagnosisDAO.deleteAnalysis(c, basicServiceForm.getName(), idCrawling);

                //Lo enviamos por correo electrónico
                String subject = basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ? getResources(request).getMessage("basic.service.mail.subject.observatory") : getResources(request).getMessage("basic.service.mail.subject.une");
                String text = getMailText(request, basicServiceForm);
                ArrayList<String> mailTo = new ArrayList<String>();
                mailTo.add(basicServiceForm.getEmail());
                String replyTo = pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.reply.to.mail");
                String replyToName = pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.reply.to.name");
                Logger.putLog("Enviando correo del servicio de diagnóstico", BasicServiceAction.class, Logger.LOG_LEVEL_INFO);
                MailUtils.sendMail(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.address"), pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.name"),
                        mailTo, subject, text, pdfPath, new File(pdfPath).getName(), replyTo, replyToName, false);
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
        } catch (Throwable t) {
            t.printStackTrace();
            Logger.putLog("Throwable: ", BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
        } finally {
            DataBaseManager.closeConnection(conn);
            DataBaseManager.closeConnection(c);

            if (pdfPath != null && StringUtils.isNotEmpty(pdfPath)) {
                // Borramos la carpeta con el PDF generado
                File pdfFile = new File(pdfPath);
                FileUtils.deleteDir(pdfFile.getParentFile());
            }
        }

        return null;
    }

    private CrawlerData createCrawlerData(BasicServiceForm basicServiceForm, Long idGuideline, Long idCrawling) {
        CrawlerData crawlerData = new CrawlerData();
        String[] cartuchos = new String[1];
        cartuchos[0] = "es.inteco.accesibilidad.CartuchoAccesibilidad";
        crawlerData.setCartuchos(cartuchos);
        crawlerData.setIdCrawling(idCrawling);
        crawlerData.setIdFulfilledCrawling(idCrawling);
        crawlerData.setNombreRastreo(basicServiceForm.getName());
        crawlerData.setLanguage(basicServiceForm.getLanguage());
        crawlerData.setProfundidad(Integer.parseInt(basicServiceForm.getProfundidad()));
        crawlerData.setPseudoaleatorio(true);
        crawlerData.setTopN(Integer.parseInt(basicServiceForm.getAmplitud()));
        crawlerData.setUser(basicServiceForm.getUser());
        ArrayList<String> mails = new ArrayList<String>();
        mails.add(basicServiceForm.getEmail());
        crawlerData.setUsersMail(mails);
        crawlerData.setTest(true);
        if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
            List<String> urls = new ArrayList<String>();
            urls.add(basicServiceForm.getDomain());
            crawlerData.setUrls(urls);
        } else {
            crawlerData.setContent(basicServiceForm.getContent());
        }
        crawlerData.setFicheroNorma(CrawlerUtils.getFicheroNorma(idGuideline));
        crawlerData.setDomains(es.inteco.utils.CrawlerUtils.addDomainsToList(basicServiceForm.getDomain(), true, Constants.ID_LISTA_SEMILLA));
        crawlerData.setInDirectory(basicServiceForm.isInDirectory());

        return crawlerData;
    }

    private String getMailText(HttpServletRequest request, BasicServiceForm basicServiceForm) {
        String text;
        if (basicServiceForm.isContentAnalysis()) {
            String[] args = {basicServiceForm.getUser(), BasicServiceUtils.getTitleFromContent(basicServiceForm.getContent()), basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), basicServiceForm.getReport()};
            text = basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ? getResources(request).getMessage("basic.service.mail.text.observatory.content", args) : getResources(request).getMessage("basic.service.mail.text.une.content", args);
        } else {
            String inDirectory = basicServiceForm.isInDirectory() ? getResources(request).getMessage("select.yes") : getResources(request).getMessage("select.no");
            String[] args = {basicServiceForm.getUser(), basicServiceForm.getDomain(), basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), inDirectory, basicServiceForm.getReport()};
            text = basicServiceForm.getReport().equals(Constants.REPORT_OBSERVATORY) ? getResources(request).getMessage("basic.service.mail.text.observatory", args) : getResources(request).getMessage("basic.service.mail.text.une", args);
        }

        return text;
    }

}
