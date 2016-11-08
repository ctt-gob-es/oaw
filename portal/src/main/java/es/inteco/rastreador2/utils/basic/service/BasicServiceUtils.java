package es.inteco.rastreador2.utils.basic.service;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.ws.CrawlerWS;
import es.inteco.rastreador2.ws.CrawlerWSJob;
import es.inteco.utils.FileUtils;
import es.inteco.utils.MailUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.IDN;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_CORE_PROPERTIES;
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class BasicServiceUtils {

    private BasicServiceUtils() {
    }

    public static long saveRequestData(final BasicServiceForm basicServiceForm, final String status) {
        try (Connection conn = DataBaseManager.getConnection()) {
            return DiagnosisDAO.insertBasicServices(conn, basicServiceForm, status);
        } catch (Exception e) {
            Logger.putLog("Excepción: Error al insertar los datos del servicio básico,", BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return 0;
    }

    public static void updateRequestStatus(final BasicServiceForm basicServiceForm, final String status) {
        try (Connection conn = DataBaseManager.getConnection()) {
            DiagnosisDAO.updateStatus(conn, basicServiceForm.getId(), status);
        } catch (Exception e) {
            Logger.putLog("Excepción: Error al modificar los datos del servicio básico,", BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void somethingWasWrongMessage(final BasicServiceForm basicServiceForm, final String message) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final String subject = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.subject");
        final ArrayList<String> mailTo = new ArrayList<>();
        mailTo.add(basicServiceForm.getEmail());
        final String mailFrom = pmgr.getValue(CRAWLER_CORE_PROPERTIES, "mail.address.from");
        MailUtils.sendMail(mailFrom, "Servicio on-line de diagnóstico de Accesibilidad", mailTo, subject, message, null, null, null, null, true);
    }

    public static List<BasicServiceForm> getBasicServiceRequestByStatus(final String status) {
        try (Connection conn = DataBaseManager.getConnection()) {
            return DiagnosisDAO.getBasicServiceRequestByStatus(conn, status);
        } catch (Exception e) {
            Logger.putLog("Excepción: Error al recuperar las peticiones al servicio básico con el estado " + status, BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

    public static BasicServiceForm getBasicServiceForm(final BasicServiceForm basicServiceForm, final HttpServletRequest request) throws Exception {
        basicServiceForm.setUser(request.getParameter(Constants.PARAM_USER));
        basicServiceForm.setDomain(request.getParameter(Constants.PARAM_URL));
        basicServiceForm.setEmail(request.getParameter(Constants.PARAM_EMAIL));
        basicServiceForm.setProfundidad(request.getParameter(Constants.PARAM_DEPTH));
        basicServiceForm.setAmplitud(request.getParameter(Constants.PARAM_WIDTH));
        basicServiceForm.setLanguage("es");
        basicServiceForm.setReport(request.getParameter(Constants.PARAM_REPORT));
        if (StringUtils.isNotEmpty(request.getParameter(Constants.PARAM_CONTENT))) {
            basicServiceForm.setContent(new String(request.getParameter(Constants.PARAM_CONTENT).getBytes("ISO-8859-1")));
        }
        if (!StringUtils.isEmpty(request.getParameter(Constants.PARAM_IN_DIRECTORY)) && request.getParameter(Constants.PARAM_IN_DIRECTORY).equals(Boolean.TRUE.toString())) {
            basicServiceForm.setInDirectory(true);
        } else {
            basicServiceForm.setInDirectory(false);
        }

        return basicServiceForm;
    }

    public static ActionErrors validateReport(final BasicServiceForm basicServiceForm) {
        final ActionErrors errors = new ActionErrors();
        final String report = basicServiceForm.getReport();
        if (!report.equalsIgnoreCase(Constants.REPORT_UNE) &&
                !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_1_NOBROKEN) &&
                !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_2) && !report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_2_NOBROKEN)) {
            errors.add(Globals.ERROR_KEY, new ActionMessage("basic.service.report.not.valid", Constants.REPORT_OBSERVATORY, Constants.REPORT_UNE));
        }

        return errors;
    }

    public static ActionErrors validateUrlOrContent(final BasicServiceForm basicServiceForm) {
        final ActionErrors errors = new ActionErrors();

        if (StringUtils.isEmpty(basicServiceForm.getDomain()) && StringUtils.isEmpty(basicServiceForm.getContent())) {
            errors.add(Globals.ERROR_KEY, new ActionMessage("basic.service.url.or.content"));
        }

        return errors;
    }

    public static String checkIDN(final String url) {
        if (url != null && !url.isEmpty()) {
            try {
                return url.replaceFirst(new URL(url).getHost(), IDN.toASCII(new URL(url).getHost()));
            } catch (Exception e) {
                Logger.putLog("Error al verificar el dominio internacionalizado", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
                return url;
            }
        } else {
            return url;
        }
    }

    public static Long getGuideline(final String report) {
        final PropertiesManager pmgr = new PropertiesManager();
        if (report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY) || report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_FILE) || report.equals(Constants.REPORT_OBSERVATORY_1_NOBROKEN)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.observatorio.intav.id"));
        } else if (report.equals(Constants.REPORT_UNE) || report.equalsIgnoreCase(Constants.REPORT_UNE_FILE)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"));
        } else if (report.equals(Constants.REPORT_WCAG_1_FILE)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"));
        } else if (report.equals(Constants.REPORT_WCAG_2_FILE)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag2.intav.id"));
        } else if (report.equals(Constants.REPORT_OBSERVATORY_2) || report.equals(Constants.REPORT_OBSERVATORY_2_NOBROKEN)) {
            return 7L;
        } else {
            return null;
        }
    }

    public static void scheduleBasicServiceJob(final BasicServiceForm basicServiceForm) throws Exception {
        Logger.putLog("Programando el rastreo con la fecha: " + basicServiceForm.getSchedulingDate(), CrawlerWS.class, Logger.LOG_LEVEL_INFO);
        final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        final Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        final JobDetail jobDetail = new JobDetail("CrawlerWSJob_" + System.currentTimeMillis(), "CrawlerWSJob", CrawlerWSJob.class);

        if (basicServiceForm.getId() == 0) {
            basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, es.inteco.common.Constants.BASIC_SERVICE_STATUS_SCHEDULED));
        }

        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(es.inteco.rastreador2.ws.commons.Constants.BASIC_SERVICE_FORM, basicServiceForm);
        jobDetail.setJobDataMap(jobDataMap);

        final Trigger trigger = new SimpleTrigger("CrawlerWSTrigger", "CrawlerWSGroup", basicServiceForm.getSchedulingDate());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static String getTitleFromContent(final String content) {
        String result;
        PropertiesManager pmgr = new PropertiesManager();
        Pattern pattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            if (matcher.group(1).length() <= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.max.length"))) {
                result = matcher.group(1);
            } else {
                //si el titulo es muy grande
                String title = matcher.group(1);
                //cortamos el title e insertamos '...'
                title = title.substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.max.length")) - 3);
                title = title.substring(0, title.lastIndexOf(' '));
                result = title + "...";
            }
        } else {
            result = pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.alternative");
        }
        return FileUtils.normalizeFileName(result);
    }

    public static String getTitleDocFromContent(final String content, final boolean isCompleted) {
        if (content == null) {
            return "";
        }
        try {
            final PropertiesManager pmgr = new PropertiesManager();
            final Pattern pattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                final String title = matcher.group(1).replaceAll("\\s{2,}", " ").trim();
                final int titleMaxLength = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.doc.max.length"));
                if (title.length() <= titleMaxLength) {
                    return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(title));
                } else {
                    //si el titulo es muy grande
                    //si es un lugar del documento donde no tiene que salir entero el titulo
                    if (!isCompleted) {
                        //	cortamos el title e insertamos '...'
                        String trunkTitle = title.substring(0, titleMaxLength - 3);
                        trunkTitle = trunkTitle.substring(0, trunkTitle.lastIndexOf(' '));
                        trunkTitle = trunkTitle + "...";
                        return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(trunkTitle));
                    } else {
                        return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(title));
                    }
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            Logger.putLog("Error al acceder al extraer el título del contenido HTML", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
            return "";
        }
    }
}
