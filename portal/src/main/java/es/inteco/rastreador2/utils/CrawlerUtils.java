package es.inteco.rastreador2.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.job.CrawlerData;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.comun.CrawlerLoginAction;
import es.inteco.rastreador2.action.usuario.NuevoUsuarioSistemaAction;
import es.inteco.rastreador2.actionform.cuentausuario.ModificarCuentaUsuarioForm;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.dao.language.LanguageDAO;
import es.inteco.rastreador2.dao.rastreo.DatosCartuchoRastreoForm;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.utils.MailUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class CrawlerUtils {

    private CrawlerUtils() {
    }

    public static CrawlerData getCrawlerData(DatosCartuchoRastreoForm dcrForm, Long idFulfilledCrawling, String user, List<String> mailTo) {
        return getCrawlerData(dcrForm, idFulfilledCrawling, user, mailTo, null);
    }

    public static CrawlerData getCrawlerData(DatosCartuchoRastreoForm dcrForm, Long idFulfilledCrawling, String user, List<String> mailTo, List<String> responsiblesTo) {
        CrawlerData crawlerData = new CrawlerData();
        crawlerData.setNombreRastreo(dcrForm.getNombre_rastreo());
        crawlerData.setIdCrawling(dcrForm.getId_rastreo());
        crawlerData.setIdCartridge(dcrForm.getId_cartucho());
        crawlerData.setIdFulfilledCrawling(idFulfilledCrawling);
        crawlerData.setUser(user);
        crawlerData.setUsersMail(mailTo);
        crawlerData.setResponsiblesMail(responsiblesTo);
        crawlerData.setIdCustomerAccount(dcrForm.getIdCuentaCliente());
        crawlerData.setIdObservatory(dcrForm.getIdObservatory());
        crawlerData.setPseudoaleatorio(dcrForm.isPseudoaleatorio());
        crawlerData.setCartuchos(dcrForm.getCartuchos());
        crawlerData.setAcronimo(dcrForm.getSeedAcronym());
        crawlerData.setUrls(dcrForm.getUrls());
        if (crawlerData.getUrls().size() > 1) {
            crawlerData.setProfundidad(1);
            crawlerData.setTopN(1);
        } else {
            crawlerData.setProfundidad(dcrForm.getProfundidad());
            crawlerData.setTopN(dcrForm.getTopN());
        }
        crawlerData.setDomains(dcrForm.getDomains());
        crawlerData.setExceptions(dcrForm.getExceptions());
        crawlerData.setCrawlingList(dcrForm.getCrawlingList());
        crawlerData.setIdGuideline(dcrForm.getId_guideline());
        crawlerData.setFicheroNorma(dcrForm.getFicheroNorma());
        crawlerData.setExhaustive(dcrForm.isExhaustive());
        crawlerData.setInDirectory(dcrForm.isInDirectory());
        if (dcrForm.getLanguage() != null) {
            crawlerData.setLanguage(dcrForm.getLanguage().getCodice());
        }
        return crawlerData;
    }

    public static List<String> getMinutes() {
        PropertiesManager pmgr = new PropertiesManager();
        String[] arrayMinutes = getArray(pmgr.getValue(CRAWLER_PROPERTIES, "minutes.list"));
        return getList(arrayMinutes);
    }

    public static List<String> getHours() {
        PropertiesManager pmgr = new PropertiesManager();
        String[] arrayMinutes = getArray(pmgr.getValue(CRAWLER_PROPERTIES, "hours.list"));
        return getList(arrayMinutes);
    }

    private static String[] getArray(String chain) {
        return chain.split(";");
    }

    private static List<String> getList(String[] array) {
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, array);

        return list;
    }

    public static List<FulfilledCrawlingForm> getCrawlingsForm(List<FulFilledCrawling> crawlings) throws Exception {
        List<FulfilledCrawlingForm> crawlingsForm = new ArrayList<FulfilledCrawlingForm>();

        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));

        for (FulFilledCrawling crawling : crawlings) {
            FulfilledCrawlingForm crawlingForm = new FulfilledCrawlingForm();
            BeanUtils.copyProperties(crawlingForm, crawling);
            crawlingForm.setDate(df.format(crawling.getDate()));
            crawlingsForm.add(crawlingForm);
        }

        return crawlingsForm;
    }

    public static Date getFechaInicio(String fecha, String hora, String minutos) throws ParseException {
        PropertiesManager pmgr = new PropertiesManager();
        DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.form.format"));

        return df.parse(fecha + " " + hora + ":" + minutos);
    }

    public static List<String> getRoles(String roleList) {
        PropertiesManager pmgr = new PropertiesManager();
        String[] arrayRoles = getArray(pmgr.getValue("role.properties", roleList));
        return getList(arrayRoles);
    }

    public static boolean hasAccess(HttpServletRequest request, String accessRol) {
        List<String> roleList = CrawlerUtils.getRoles(accessRol);
        List<String> rolesesion = (ArrayList<String>) request.getSession().getAttribute(Constants.ROLE);

        if (rolesesion != null) {
            for (String role : roleList) {
                if (rolesesion.contains(role)) {
                    return true;
                }
            }
        } else {
            Logger.putLog("Error: el usuario no tiene sus roles en session", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR);
        }
        return false;
    }

    public static InsertarRastreoForm insertarDatosAutomaticosCU(InsertarRastreoForm insertarRastreoForm, es.inteco.rastreador2.actionform.cuentausuario.NuevaCuentaUsuarioForm nuevaCuentaUsuarioForm, String cartucho) {
        if (!cartucho.equals("")) {
            insertarRastreoForm.setCodigo(nuevaCuentaUsuarioForm.getNombre() + "-" + cartucho);
        }
        insertarRastreoForm.setProfundidad(Integer.parseInt(nuevaCuentaUsuarioForm.getProfundidad()));
        insertarRastreoForm.setTopN(Integer.parseInt(nuevaCuentaUsuarioForm.getAmplitud()));
        insertarRastreoForm.setCartuchos(nuevaCuentaUsuarioForm.getCartuchos());
        insertarRastreoForm.setId_semilla(nuevaCuentaUsuarioForm.getIdSeed());
        insertarRastreoForm.setId_lista_rastreable(nuevaCuentaUsuarioForm.getIdCrawlableList());
        insertarRastreoForm.setId_lista_no_rastreable(nuevaCuentaUsuarioForm.getIdNoCrawlableList());
        insertarRastreoForm.setPseudoAleatorio(nuevaCuentaUsuarioForm.isPseudoAleatorio());
        insertarRastreoForm.setLenguaje(nuevaCuentaUsuarioForm.getLenguaje());
        insertarRastreoForm.setInDirectory(nuevaCuentaUsuarioForm.isInDirectory());

        return insertarRastreoForm;
    }

    public static InsertarRastreoForm insertarDatosAutomaticos(InsertarRastreoForm insertarRastreoForm, ModificarCuentaUsuarioForm modificarCuentaUsuarioForm, String cartucho) {
        if (!cartucho.equals("")) {
            insertarRastreoForm.setCodigo(modificarCuentaUsuarioForm.getNombre() + "-" + cartucho);
        } else {
            insertarRastreoForm.setCodigo(modificarCuentaUsuarioForm.getNombre());
        }
        insertarRastreoForm.setProfundidad(Integer.parseInt(modificarCuentaUsuarioForm.getProfundidad()));
        insertarRastreoForm.setTopN(Integer.parseInt(modificarCuentaUsuarioForm.getAmplitud()));
        insertarRastreoForm.setSemilla(modificarCuentaUsuarioForm.getDominio());
        insertarRastreoForm.setListaRastreable(modificarCuentaUsuarioForm.getListaRastreable());
        insertarRastreoForm.setListaNoRastreable(modificarCuentaUsuarioForm.getListaNoRastreable());
        insertarRastreoForm.setCartuchos(modificarCuentaUsuarioForm.getCartuchos());
        insertarRastreoForm.setNormaAnalisis(modificarCuentaUsuarioForm.getNormaAnalisis());
        insertarRastreoForm.setNormaAnalisisEnlaces(modificarCuentaUsuarioForm.getNormaAnalisisEnlaces());
        insertarRastreoForm.setId_lista_rastreable(modificarCuentaUsuarioForm.getIdListaRastreable());
        insertarRastreoForm.setId_semilla(modificarCuentaUsuarioForm.getIdSemilla());
        insertarRastreoForm.setId_lista_no_rastreable(modificarCuentaUsuarioForm.getIdListaNoRastreable());
        insertarRastreoForm.setPseudoAleatorio(modificarCuentaUsuarioForm.isPseudoAleatorio());
        insertarRastreoForm.setLenguaje(modificarCuentaUsuarioForm.getLenguaje());
        insertarRastreoForm.setInDirectory(modificarCuentaUsuarioForm.isInDirectory());
        return insertarRastreoForm;
    }

    public static void returnFile(String filename, HttpServletResponse response, String contentype, boolean delete) throws Exception {
        FileInputStream fileIn = null;
        OutputStream out = null;
        File file = new File(filename);
        try {
            fileIn = new FileInputStream(file);
            out = response.getOutputStream();
            response.setContentType(contentype);
            response.setContentLength((int) file.length());
            response.setHeader("Content-disposition", "attachment; filename=\"" + file.getName()+"\"");


            byte[] buffer = new byte[2048];
            int bytesRead = fileIn.read(buffer);
            while (bytesRead >= 0) {
                if (bytesRead > 0) {
                    out.write(buffer, 0, bytesRead);
                    bytesRead = fileIn.read(buffer);
                }
            }

        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            }

            try {
                if (fileIn != null) {
                    fileIn.close();
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }

        if (delete && !file.delete()) {
            Logger.putLog("No se ha podido borrar el fichero: " + file.getAbsolutePath(), ExportAction.class, Logger.LOG_LEVEL_ERROR);
        }
    }

    public static void returnText(String text, HttpServletResponse response, boolean isHtml) throws Exception {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            if (isHtml) {
                response.setContentType("text/html");
            } else {
                response.setContentType("text/plain");
            }

            out.write(text.getBytes());
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    public static void warnAdministrators(Exception exception, Class clazz) {
        Logger.putLog("Excepción: ", clazz, Logger.LOG_LEVEL_ERROR, exception);

        try {
            PropertiesManager pmgr = new PropertiesManager();
            List<String> adminMails = DAOUtils.getMailsByRol(Long.parseLong(pmgr.getValue(CRAWLER_PROPERTIES, "role.administrator.id")));
            String alertSubject = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.subject");
            String alertText = pmgr.getValue(CRAWLER_PROPERTIES, "warning.administrator.message") + exception.getMessage();
            String alertFromAddress = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.address");
            String alertFromName = pmgr.getValue(CRAWLER_PROPERTIES, "alert.from.name");

            MailUtils.sendSimpleMail(alertFromAddress, alertFromName, adminMails, alertSubject, alertText);
        } catch (Exception e) {
            Logger.putLog("No ha sido posible avisar a los administradores: ", NuevoUsuarioSistemaAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static MessageResources getResources(HttpServletRequest request) {
        return (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
    }

    public static Locale getLocale(HttpServletRequest request) {
        return RequestUtils.getUserLocale(request, null);
    }

    public static List<LenguajeForm> getLanguages() {
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            return LanguageDAO.loadLanguages(c);
        } catch (Exception e) {
            Logger.putLog("Exception: ", CrawlerLoginAction.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    public static String getCronExpression(Date date, String cronExpressionPattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return cronExpressionPattern
                .replaceAll("min", String.valueOf(calendar.get(Calendar.MINUTE)))
                .replaceAll("hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)))
                .replaceAll("daymonth", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))
                .replaceAll("month", String.valueOf(getMonthForCronExpression(calendar, cronExpressionPattern)))
                .replaceAll("year", String.valueOf(calendar.get(Calendar.YEAR)));
    }

    private static int getMonthForCronExpression(Calendar calendar, String cronExpressionPattern) {
        String regexp = "month/([\\d]*)";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        int monthPeriod = 0;
        int firstMonth = calendar.get(Calendar.MONTH) + 1;

        Matcher matcher = pattern.matcher(cronExpressionPattern);
        if (matcher.find()) {
            monthPeriod = Integer.parseInt(matcher.group(1));
        }

        while ((monthPeriod != 0) && (firstMonth - monthPeriod > 0)) {
            firstMonth = firstMonth - monthPeriod;
        }

        return firstMonth;
    }

    public static String getFicheroNorma(long idGuideline) {
        Connection c = null;
        try {
            PropertiesManager pmgr = new PropertiesManager();
            c = DataBaseManager.getConnection();
            return es.inteco.plugin.dao.RastreoDAO.recuperarFicheroNorma(c, idGuideline);
        } catch (Exception e) {
            return null;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

    public static String formatDate(Date date) {
        PropertiesManager pmgr = new PropertiesManager();
        SimpleDateFormat sdf = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
        return sdf.format(date);
    }

    public static String getCharset(String source) {
        String charset = Constants.DEFAULT_ENCODING;
        String regexp = "<meta.*charset=(.*?)\"";
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            charset = matcher.group(1);
        }

        if (isValidCharset(charset)) {
            return charset;
        } else {
            return Constants.DEFAULT_ENCODING;
        }
    }

    private static boolean isValidCharset(String charset) {
        try {
            byte[] test = new byte[10];
            new String(test, charset);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }
}
