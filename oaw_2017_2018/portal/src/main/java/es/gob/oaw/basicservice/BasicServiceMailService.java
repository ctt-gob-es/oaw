package es.gob.oaw.basicservice;

import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * Clase para el envío de correos desde el servicio de diagnóstico. Utiliza a su vez el servicio MailService
 */
public class BasicServiceMailService {

    private final PropertiesManager pmgr;
    private final MailService mailService;

    public BasicServiceMailService() {
        pmgr = new PropertiesManager();
        mailService = new MailService();
    }

    public void sendBasicServiceReport(final BasicServiceForm basicServiceForm, final String attachUrl, final String attachName) {
        mailService.sendMail(Collections.singletonList(basicServiceForm.getEmail()), getMailSubject(basicServiceForm.getReport()), getMailBody(basicServiceForm), attachUrl, attachName);
    }

    public void sendBasicServiceErrorMessage(final BasicServiceForm basicServiceForm, final String message) {
        final String subject = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.subject");
        mailService.sendMail(Collections.singletonList(basicServiceForm.getEmail()), subject, message);
    }


    private String getMailSubject(final String reportType) {
        final String message = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.subject");
        if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType) || Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
            return MessageFormat.format(message, "Observatorio UNE 2004");
        } else if (Constants.REPORT_OBSERVATORY_2.equals(reportType) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
            return MessageFormat.format(message, "Observatorio UNE 2012");
        } else if ("une".equals(reportType)) {
            return MessageFormat.format(message, "UNE 139803");
        } else {
            return "Informe de Accesibilidad Web";
        }
    }

    private String getMailBody(final BasicServiceForm basicServiceForm) {
        final String text;
        if (basicServiceForm.isContentAnalysis()) {
            text = MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory.content"),
                    basicServiceForm.getUser(), reportToString(basicServiceForm.getReport()));
        } else {
            final String inDirectory = basicServiceForm.isInDirectory() ? pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes") : pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.no");
            text = MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory"),
                    basicServiceForm.getUser(), basicServiceForm.getDomain(), basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), inDirectory, reportToString(basicServiceForm.getReport()));
        }

        return text;
    }

    private String reportToString(final String reportType) {
        if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType)) {
            return "Observatorio UNE 2004";
        } else if (Constants.REPORT_OBSERVATORY_2.equals(reportType)) {
            return "Observatorio UNE 2012";
        } else if (Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
            return "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
        } else if (Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
            return "Observatorio UNE 2012 (sin comprobar enlaces rotos)";
        } else {
            return reportType;
        }
    }

}
