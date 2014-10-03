package es.inteco.rastreador2.utils.basic.service;

import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class BasicServiceUtils {

    private BasicServiceUtils() {
    }

    public static long saveRequestData(BasicServiceForm basicServiceForm, String status) {
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();
            return DiagnosisDAO.insertBasicServices(conn, basicServiceForm, status);
        } catch (Exception e) {
            Logger.putLog("Excepción: Error al insertar los datos del servicio básico,", BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
        return 0;
    }

    public static void updateRequestStatus(BasicServiceForm basicServiceForm, String status) {
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();
            DiagnosisDAO.updateStatus(conn, basicServiceForm.getId(), status);
        } catch (Exception e) {
            Logger.putLog("Excepción: Error al modificar los datos del servicio básico,", BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
    }

    public static void somethingWasWrongMessage(HttpServletRequest request, BasicServiceForm basicServiceForm, String message) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        String subject = CrawlerUtils.getResources(request).getMessage("basic.service.mail.error.subject");
        ArrayList<String> mailTo = new ArrayList<String>();
        mailTo.add(basicServiceForm.getEmail());
        MailUtils.sendMail(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.address"), pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.name"),
                mailTo, subject, message, null, null, null, null, true);
    }

    public static List<BasicServiceForm> getBasicServiceRequestByStatus(String status) {
        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection();
            return DiagnosisDAO.getBasicServiceRequestByStatus(conn, status);
        } catch (Exception e) {
            Logger.putLog("Excepción: Error al recuperar las peticiones al servicio básico con el estado " + status, BasicServiceThread.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }
        return null;
    }

    public static BasicServiceForm getBasicServiceForm(BasicServiceForm basicServiceForm, HttpServletRequest request) throws Exception {
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

    public static ActionErrors validateReport(BasicServiceForm basicServiceForm) {
        ActionErrors errors = new ActionErrors();

        if (!basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_OBSERVATORY)
                && !basicServiceForm.getReport().equalsIgnoreCase(Constants.REPORT_UNE)) {
            errors.add(Globals.ERROR_KEY, new ActionMessage("basic.service.report.not.valid", Constants.REPORT_OBSERVATORY, Constants.REPORT_UNE));
        }

        return errors;
    }

    public static ActionErrors validateUrlOrContent(BasicServiceForm basicServiceForm) {
        ActionErrors errors = new ActionErrors();

        if (StringUtils.isEmpty(basicServiceForm.getDomain()) && StringUtils.isEmpty(basicServiceForm.getContent())) {
            errors.add(Globals.ERROR_KEY, new ActionMessage("basic.service.url.or.content"));
        }

        return errors;
    }

    public static String checkIDN(String url) {
        try {
            return url.replaceFirst(new URL(url).getHost(), IDN.toASCII(new URL(url).getHost()));
        } catch (Exception e) {
            Logger.putLog("Error al verificar el dominio internacionalidazo", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
            return url;
        }
    }

    public static Long getGuideline(String report) {
        PropertiesManager pmgr = new PropertiesManager();
        if (report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY) || report.equalsIgnoreCase(Constants.REPORT_OBSERVATORY_FILE)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.observatorio.intav.id"));
        } else if (report.equals(Constants.REPORT_UNE) || report.equalsIgnoreCase(Constants.REPORT_UNE_FILE)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.une.intav.id"));
        } else if (report.equals(Constants.REPORT_WCAG_1_FILE)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag1.intav.id"));
        } else if (report.equals(Constants.REPORT_WCAG_2_FILE)) {
            return Long.valueOf(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.wcag2.intav.id"));
        } else {
            return null;
        }
    }

    public static void scheduleBasicServiceJob(BasicServiceForm basicServiceForm) throws Exception {
        Logger.putLog("Programando el rastreo con la fecha: " + basicServiceForm.getSchedulingDate(), CrawlerWS.class, Logger.LOG_LEVEL_INFO);
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        JobDetail jobDetail = new JobDetail("CrawlerWSJob_" + System.currentTimeMillis(), "CrawlerWSJob", CrawlerWSJob.class);

        if (basicServiceForm.getId() == 0) {
            basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, es.inteco.common.Constants.BASIC_SERVICE_STATUS_SCHEDULED));
        }

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(es.inteco.rastreador2.ws.commons.Constants.BASIC_SERVICE_FORM, basicServiceForm);
        jobDetail.setJobDataMap(jobDataMap);

        Trigger trigger = new SimpleTrigger("CrawlerWSTrigger", "CrawlerWSGroup", basicServiceForm.getSchedulingDate());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static String getTitleFromContent(String content) {
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

    public static String getTitleDocFromContent(String content, boolean isCompleted) {
        try {
            PropertiesManager pmgr = new PropertiesManager();
            Pattern pattern = Pattern.compile("<title>(.*?)</title>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                if (matcher.group(1).length() <= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.doc.max.length"))) {
                    return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(matcher.group(1)));
                } else {
                    //si el titulo es muy grande
                    String title = matcher.group(1);
                    //si es un lugar del documento donde no tiene que salir entero el titulo
                    if (!isCompleted) {
                        //	cortamos el title e insertamos '...'
                        title = title.substring(0, Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "basic.service.title.doc.max.length")) - 3);
                        title = title.substring(0, title.lastIndexOf(' '));
                        title = title + "...";
                    }
                    return HTMLEntities.unhtmlQuotes(HTMLEntities.unhtmlentities(title));
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.putLog("Error al acceder al extraer el título del contenido HTML", BasicServiceUtils.class, Logger.LOG_LEVEL_WARNING, e);
            return null;
        }
    }
}
