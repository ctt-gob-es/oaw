package es.inteco.rastreador2.html;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.utils.CrawlerUtils;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_CORE_PROPERTIES;
import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ExportHTMLAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();

        List<String> typeList = new ArrayList<>();
        String htmlPages = pmgr.getValue(CRAWLER_PROPERTIES, "html.pages");
        Connection conn = null;
        int numObs = 0;
        List<CategoriaForm> categories = new ArrayList<>();
        try {
            conn = DataBaseManager.getConnection();
            ObservatorioRealizadoForm observatoryForm = ObservatorioDAO.getFulfilledObservatory(conn, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)), Long.parseLong(request.getParameter(Constants.ID)));
            numObs = ObservatorioDAO.getFulfilledObservatories(conn, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)), Constants.NO_PAGINACION, observatoryForm.getFecha()).size();
            if (numObs >= Integer.parseInt(pmgr.getValue("pdf.properties", "pdf.anonymous.results.pdf.min.obser"))) {
                htmlPages += pmgr.getValue(CRAWLER_PROPERTIES, "html.pages.evolution");
            }
            categories = ObservatorioDAO.getExecutionObservatoryCategories(conn, Long.valueOf(request.getParameter(Constants.ID)));
            for (CategoriaForm category : categories) {
                typeList.add(Constants.SEGMENT_RESULTS_1 + category.getId());
                typeList.add(Constants.SEGMENT_RESULTS_2 + category.getId());
                typeList.add(Constants.SEGMENT_RESULTS_3 + category.getId());
                typeList.add(Constants.SEGMENT_RESULTS_4 + category.getId());
                typeList.add(Constants.SEGMENT_RESULTS_5 + category.getId());
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar las categorias.", AnonymousHTMLAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(conn);
        }

        List<String> typeListAux = Arrays.asList(htmlPages.split(";"));
        for (String test : typeListAux) {
            typeList.add(test);
        }


        FileUtils.copyDirectory(FileUtils.openDirectory(pmgr.getValue(CRAWLER_PROPERTIES, "html.path.img")), FileUtils.openDirectory(pmgr.getValue(CRAWLER_PROPERTIES, "html.path.html.img").replace("{0}", request.getParameter(Constants.ID))));
        FileUtils.copyDirectory(FileUtils.openDirectory(pmgr.getValue(CRAWLER_PROPERTIES, "html.path.style")), FileUtils.openDirectory(pmgr.getValue(CRAWLER_PROPERTIES, "html.path.html.style").replace("{0}", request.getParameter(Constants.ID))));
        String params = "";
        SimpleDateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
            ResultadosAnonimosObservatorioIntavUtils.generateGraphics(request, pmgr.getValue(CRAWLER_PROPERTIES, "html.path.graphics").replace("{0}", request.getParameter(Constants.ID)), Constants.MINISTERIO_P, true);
            params = "&" + Constants.OBSERVATORY_DATE + "=" + df.format(ObservatorioDAO.getFulfilledObservatory(c, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)), Long.parseLong(request.getParameter(Constants.ID))).getFecha()) +
                    "&" + Constants.OBSERVATORY_NAME + "=" + URLEncoder.encode(observatoryForm.getNombre(), "ISO-8859-1") + "&" + Constants.OBSERVATORY_T + "=" + observatoryForm.getTipo();
            if (numObs >= Integer.parseInt(pmgr.getValue("pdf.properties", "pdf.anonymous.results.pdf.min.obser"))) {
                params += "&" + Constants.OBSERVATORY_EVOLUTION + "=" + Constants.SI;
            }
            if (categories != null && !categories.isEmpty()) {
                params += "&" + Constants.OBSERVATORY_SEGMENTS + "=" + Constants.SI;
            }
        } catch (Exception e) {
            Logger.putLog("Error al crear las tablas para generar el HTML", ExportHTMLAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR_PAGE);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        FileWriter fW = null;
        for (String type : typeList) {
            try {
                String url = pmgr.getValue(CRAWLER_PROPERTIES, "html.request")
                        .replace("{0}", type)
                        .replace("{1}", pmgr.getValue(CRAWLER_CORE_PROPERTIES, "not.filtered.uris.security.key"))
                        .replace("{2}", request.getParameter(Constants.ID))
                        .replace("{3}", request.getParameter(Constants.ID_OBSERVATORIO)) + params;
                if (type.contains(Constants.SEGMENT_RESULTS_1) || type.contains(Constants.SEGMENT_RESULTS_2) || type.contains(Constants.SEGMENT_RESULTS_3)
                        || type.contains(Constants.SEGMENT_RESULTS_4) || type.contains(Constants.SEGMENT_RESULTS_5)) {
                    url += "&" + Constants.ID_CATEGORIA + "=" + type.subSequence(Constants.SEGMENT_RESULTS_1.length(), type.length());
                }
                Logger.putLog("Pidiendo url de exportación HTML: " + url, ExportHTMLAction.class, Logger.LOG_LEVEL_INFO);
                HttpURLConnection connection = CrawlerUtils.getConnection(url, null, true);
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    File file = FileUtils.openDirectory(filePath(type, request.getParameter(Constants.ID)));
                    fW = new FileWriter(file);
                    InputStream markableInputStream = CrawlerUtils.getMarkableInputStream(connection);
                    String textContent = CrawlerUtils.getTextContent(connection, markableInputStream);
                    fW.write(new String(textContent.getBytes("UTF-8")));
                    fW.flush();
                }
            } catch (Exception e) {
                Logger.putLog("Exception: ", ExportHTMLAction.class, Logger.LOG_LEVEL_ERROR, e);
            } finally {
                if (fW != null) {
                    fW.close();
                }
            }
        }

        String htmlPath = pmgr.getValue(CRAWLER_PROPERTIES, "html.path").replace("{0}", request.getParameter(Constants.ID));
        String zipPath = pmgr.getValue(CRAWLER_PROPERTIES, "html.path").replace("{0}", request.getParameter(Constants.ID)) + Constants.ZIP_FILE;
        ZipUtils.generateZipFile(htmlPath, zipPath, true);
        es.inteco.rastreador2.utils.CrawlerUtils.returnFile(response, zipPath, "application/zip", true);

        return null;
    }

    private static String filePath(String type, String idExecution) {
        PropertiesManager pmgr = new PropertiesManager();
        String filePath = pmgr.getValue(CRAWLER_PROPERTIES, "html.path").replace("{0}", idExecution);
        if (type.equals(Constants.INTRODUCTION)) {
            filePath += Constants.INTRODUCTION_FILE;
        } else if (type.equals(Constants.OBJECTIVE)) {
            filePath += Constants.OBJECTIVE_FILE;
        } else if (type.equals(Constants.METHODOLOGY)) {
            filePath += Constants.METHODOLOGY_FILE;
        } else if (type.equals(Constants.METHODOLOGY_SUB1)) {
            filePath += Constants.METHODOLOGY_SUB1_FILE;
        } else if (type.equals(Constants.METHODOLOGY_SUB2)) {
            filePath += Constants.METHODOLOGY_SUB2_FILE;
        } else if (type.equals(Constants.METHODOLOGY_SUB3)) {
            filePath += Constants.METHODOLOGY_SUB3_FILE;
        } else if (type.equals(Constants.METHODOLOGY_SUB4)) {
            filePath += Constants.METHODOLOGY_SUB4_FILE;
        } else if (type.equals(Constants.GLOBAL_RESULTS)) {
            filePath += Constants.GLOBAL_RESULTS_FILE;
        } else if (type.equals(Constants.GLOBAL_RESULTS2)) {
            filePath += Constants.GLOBAL_RESULTS2_FILE;
        } else if (type.equals(Constants.GLOBAL_RESULTS3)) {
            filePath += Constants.GLOBAL_RESULTS3_FILE;
        } else if (type.equals(Constants.GLOBAL_RESULTS4)) {
            filePath += Constants.GLOBAL_RESULTS4_FILE;
        } else if (type.equals(Constants.GLOBAL_RESULTS5)) {
            filePath += Constants.GLOBAL_RESULTS5_FILE;
        } else if (type.equals(Constants.GLOBAL_RESULTS6)) {
            filePath += Constants.GLOBAL_RESULTS6_FILE;
        } else if (type.subSequence(0, Constants.SEGMENT_RESULTS_1.length()).equals(Constants.SEGMENT_RESULTS_1)) {
            filePath += Constants.SEGMENT_RESULTS_FILE_1.replace("{0}", type.substring(Constants.SEGMENT_RESULTS_1.length(), type.length()));
        } else if (type.subSequence(0, Constants.SEGMENT_RESULTS_2.length()).equals(Constants.SEGMENT_RESULTS_2)) {
            filePath += Constants.SEGMENT_RESULTS_FILE_2.replace("{0}", type.substring(Constants.SEGMENT_RESULTS_2.length(), type.length()));
        } else if (type.subSequence(0, Constants.SEGMENT_RESULTS_3.length()).equals(Constants.SEGMENT_RESULTS_3)) {
            filePath += Constants.SEGMENT_RESULTS_FILE_3.replace("{0}", type.substring(Constants.SEGMENT_RESULTS_3.length(), type.length()));
        } else if (type.subSequence(0, Constants.SEGMENT_RESULTS_4.length()).equals(Constants.SEGMENT_RESULTS_4)) {
            filePath += Constants.SEGMENT_RESULTS_FILE_4.replace("{0}", type.substring(Constants.SEGMENT_RESULTS_4.length(), type.length()));
        } else if (type.subSequence(0, Constants.SEGMENT_RESULTS_5.length()).equals(Constants.SEGMENT_RESULTS_5)) {
            filePath += Constants.SEGMENT_RESULTS_FILE_5.replace("{0}", type.substring(Constants.SEGMENT_RESULTS_5.length(), type.length()));
        } else if (type.equals(Constants.EVOLUTION_RESULTS)) {
            filePath += Constants.EVOLUTION_RESULTS_FILE;
        } else if (type.equals(Constants.EVOLUTION_RESULTS1)) {
            filePath += Constants.EVOLUTION_RESULTS_FILE_1;
        } else if (type.equals(Constants.EVOLUTION_RESULTS2)) {
            filePath += Constants.EVOLUTION_RESULTS_FILE_2;
        } else if (type.equals(Constants.EVOLUTION_RESULTS3)) {
            filePath += Constants.EVOLUTION_RESULTS_FILE_3;
        } else if (type.equals(Constants.EVOLUTION_RESULTS4)) {
            filePath += Constants.EVOLUTION_RESULTS_FILE_4;
        } else if (type.equals(Constants.CONCLUSION)) {
            filePath += Constants.CONCLUSION_FILE;
        } else if (type.equals(Constants.SEGMENT_CONCLUSION)) {
            filePath += Constants.SEGMENT_CONCLUSION_FILE;
        } else if (type.equals(Constants.EVOLUTION_CONCLUSION)) {
            filePath += Constants.EVOLUTION_CONCLUSION_FILE;
        }
        return filePath;
    }

}
