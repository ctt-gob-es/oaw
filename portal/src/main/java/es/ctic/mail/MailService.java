package es.ctic.mail;

import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.common.Constants;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;

import java.text.MessageFormat;
import java.util.Collections;

import static es.inteco.common.Constants.*;

/**
 * Created by mikunis on 1/24/17.
 */
public class MailService {

    private final PropertiesManager pmgr;
    private final MailProvider mailProvider;

    public MailService() {
        pmgr = new PropertiesManager();
        mailProvider = MailProviderFactory.getMailProvider(pmgr.getValue(Constants.MAIL_PROPERTIES, "mail.transport.protocol"));
    }

    public void sendBasicServiceReport(final BasicServiceForm basicServiceForm, final String attachUrl, final String attachName) {
        mailProvider.setSubject(getMailSubject(basicServiceForm.getReport()));
        mailProvider.setBody(getMailBody(basicServiceForm));
        mailProvider.setMailTo(Collections.singletonList(basicServiceForm.getEmail()));
        mailProvider.setAttachment(attachName, attachUrl);
        mailProvider.sendMail();
    }

    public void sendBasicServiceErrorMessage(final BasicServiceForm basicServiceForm, final String message) {
        final String subject = pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.subject");
        mailProvider.setSubject(subject);
        mailProvider.setBody(message);
        mailProvider.setMailTo(Collections.singletonList(basicServiceForm.getEmail()));
        mailProvider.sendMail();
    }

    private String getMailSubject(final String reportType) {
        final String message = pmgr.getValue(BASIC_SERVICE_PROPERTIES, "basic.service.mail.subject");
        if (REPORT_OBSERVATORY.equals(reportType) || REPORT_OBSERVATORY_FILE.equals(reportType) || REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
            return MessageFormat.format(message, "Observatorio UNE 2004");
        } else if (REPORT_OBSERVATORY_2.equals(reportType) || REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
            return MessageFormat.format(message, "Observatorio UNE 2012");
        } else if ("une".equals(reportType)) {
            return MessageFormat.format(message, "UNE 139803");
        }
        return "Informe de Accesibilidad Web";
    }

    private String getMailBody(final BasicServiceForm basicServiceForm) {
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
        if (REPORT_OBSERVATORY.equals(reportType) || REPORT_OBSERVATORY_FILE.equals(reportType)) {
            return "Observatorio UNE 2004";
        } else if (REPORT_OBSERVATORY_2.equals(reportType)) {
            return "Observatorio UNE 2012";
        } else if (REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
            return "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
        } else if (REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
            return "Observatorio UNE 2012 (sin comprobar enlaces rotos)";
        } else {
            return reportType;
        }
    }
}
