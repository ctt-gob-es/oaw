package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.SubirConclusionesForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.*;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Locale;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ResultadosAnonimosObservatorioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "view.observatory.results")) {
                final String action = request.getParameter(Constants.ACTION);
                if (action.equals(Constants.GET_FULFILLED_OBSERVATORIES)) {
                    request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
                    return getFulfilledObservatories(mapping, request);
                } else if (action.equals(Constants.OBSERVATORY_GRAPHIC)) {
                    return getGraphics(mapping, request, getFilePath(request));
                } else if (action.equals(Constants.OBSERVATORY_GRAPHIC_RGENERATE)) {
                    return regenerateGraphic(mapping, request, getFilePath(request));
                } else if (action.equals(Constants.UPLOAD_PAGE)) {
                    request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
                    request.setAttribute(Constants.ID_EX_OBS, request.getParameter(Constants.ID));
                    return mapping.findForward(Constants.UPLOAD_CONCLUSION_FORM);
                } else if (action.equals(Constants.UPLOAD_FILE)) {
                    return upLoadConclusion(mapping, form, request);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            CrawlerUtils.warnAdministrators(e, this.getClass());
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        return null;
    }

    private ActionForward upLoadConclusion(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
        try {
            SubirConclusionesForm subirConclusionesForm = (SubirConclusionesForm) form;
            ActionForward forward = new ActionForward();
            if (!isCancelled(request)) {
                ActionErrors errors = subirConclusionesForm.validate(mapping, request);
                request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
                request.setAttribute(Constants.ID_EX_OBS, request.getParameter(Constants.ID_EX_OBS));
                if (errors.isEmpty()) {
                    PropertiesManager pmgr = new PropertiesManager();
                    if (subirConclusionesForm.getFile().getFileName().endsWith(".xml") &&
                            subirConclusionesForm.getFile().getFileSize() <= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size"))) {
                        loadFile(request, subirConclusionesForm);
                        forward = new ActionForward(mapping.findForward(Constants.FULFILLED_OBSERVATORIES));
                    } else if (!subirConclusionesForm.getFile().getFileName().endsWith(".xml")) {
                        errors.add("xmlFile", new ActionMessage("no.xml.file"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    } else if (subirConclusionesForm.getFile().getFileSize() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size"))) {
                        errors.add("xmlFile", new ActionMessage("xml.size.error"));
                        saveErrors(request, errors);
                        return mapping.findForward(Constants.VOLVER);
                    }
                } else {
                    saveErrors(request, errors);
                    return mapping.findForward(Constants.VOLVER);
                }
            } else {
                Logger.putLog("Cancelada la acción de subir el fichero de conclusiones", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_INFO);
                forward = new ActionForward(mapping.findForward(Constants.FULFILLED_OBSERVATORIES));
            }
            forward.setPath(forward.getPath() + "&" + Constants.ID_EX_OBS + "=" + request.getParameter(Constants.ID_EX_OBS) +
                    "&" + Constants.ID_OBSERVATORIO + "=" + request.getParameter(Constants.ID_OBSERVATORIO));
            forward.setRedirect(true);
            return forward;
        } catch (Exception e) {
            Logger.putLog("Error al subir el fichero de conclusiones", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return mapping.findForward(Constants.GET_FULFILLED_OBSERVATORIES);
    }

    private static void loadFile(HttpServletRequest request, SubirConclusionesForm subirConclusionesForm) {
        PropertiesManager pmgr = new PropertiesManager();
        String path = pmgr.getValue(CRAWLER_PROPERTIES, "conclusion.path");
        path += request.getParameter(Constants.ID_OBSERVATORIO) + File.separator + request.getParameter(Constants.ID_EX_OBS) + File.separator;
        FileOutputStream fileD = null;
        try {
            File file = new File(path + "conclusion.xml");
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                Logger.putLog("Error al crear los directorios para subir el archivo de conclusiones. ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR);
            }
            fileD = new FileOutputStream(file);
            fileD.write(subirConclusionesForm.getFile().getFileData());
            fileD.flush();
        } catch (Exception e) {
            Logger.putLog("Error al subir el archivo de conclusiones. ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            try {
                if (fileD != null) {
                    fileD.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al subir el archivo de conclusiones. ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    private String getFilePath(HttpServletRequest request) {
        String execution_id = request.getParameter(Constants.ID);
        String observatory_id = request.getParameter(Constants.ID_OBSERVATORIO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        PropertiesManager pmgr = new PropertiesManager();
        String filePath = "";
        if (Integer.valueOf(request.getParameter(Constants.TYPE_OBSERVATORY)) != null) {
            if (request.getParameter(Constants.TYPE_OBSERVATORY).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
                filePath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.intav.files") + observatory_id + File.separator + execution_id + File.separator + language.getLanguage() + File.separator;
            } else if (request.getParameter(Constants.TYPE_OBSERVATORY).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
                filePath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.lenox.files") + observatory_id + File.separator + execution_id + File.separator + language.getLanguage() + File.separator;
            } else if (request.getParameter(Constants.TYPE_OBSERVATORY).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
                filePath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.multilanguage.files") + observatory_id + File.separator + execution_id + File.separator + language.getLanguage() + File.separator;
            }
        }
        return filePath;
    }

    public ActionForward regenerateGraphic(ActionMapping mapping, HttpServletRequest request, String filePath) throws Exception {
        String graphic = request.getParameter(Constants.GRAPHIC);
        PropertiesManager pmgr = new PropertiesManager();

        File file;
        if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_GLOBAL)) {
            file = new File(filePath + pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global"));
        } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_COMPARATIVE)) {
            file = new File(filePath + pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution"));
        } else {
            file = new File(filePath + pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + graphic);
        }

        if (file.isDirectory()) {
            FileUtils.deleteDir(file);
        }

        String idExecution = request.getParameter(Constants.ID);
        CacheUtils.removeFromCache(Constants.OBSERVATORY_KEY_CACHE + idExecution);

        return getGraphics(mapping, request, filePath);
    }

    public ActionForward getGraphics(ActionMapping mapping, HttpServletRequest request, String filePath) throws Exception {
        String graphic = request.getParameter(Constants.GRAPHIC);
        int observatoryType = Integer.parseInt(request.getParameter(Constants.TYPE_OBSERVATORY));
        long idExecutionObservatory = Long.parseLong(request.getParameter(Constants.ID));

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, idExecutionObservatory);
            if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_INITIAL)) {
                return mapping.findForward(Constants.OBSERVATORY_GRAPHIC);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_GLOBAL)) {
                return getGlobalGraphic(request, mapping, filePath, observatoryType, categories);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_CATEGORIES)) {
                request.setAttribute(Constants.CATEGORIES_LIST, categories);
                return mapping.findForward(Constants.SHOW_CATEGORY_LIST);
            } else if (org.apache.commons.lang.StringUtils.isNumeric(graphic)) {
                return getCategoryGraphic(request, mapping, Integer.parseInt(graphic), filePath, observatoryType);
            } else if (graphic.equals(Constants.OBSERVATORY_GRAPHIC_COMPARATIVE)) {
                return getEvolutionGraphic(request, mapping, filePath, observatoryType);
            }
        } catch (Exception e) {
            Logger.putLog("Error al recuperar las categorias del Observatorio.", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.ERROR_PAGE);
    }

    public ActionForward getGlobalGraphic(HttpServletRequest request, ActionMapping mapping, String filePath, int observatoryType, List<CategoriaForm> categories) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();
        filePath += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
        int have_results = 0;
        if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
            have_results = ResultadosAnonimosObservatorioIntavUtils.generateGlobalGraphics(request, filePath, categories, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors"), false);
        } else if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
            have_results = ResultadosAnonimosObservatorioLenoxUtils.generateGlobalGraphics(request, filePath);
        } else if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
            have_results = ResultadosAnonimosObservatorioMultilanguageUtils.generateGlobalGraphics(request, filePath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
        }
        if (have_results == Constants.OBSERVATORY_NOT_HAVE_RESULTS) {
            ActionErrors errors = new ActionErrors();
            errors.add("graficaObservatorio", new ActionMessage("observatorio.no.resultados"));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else if (have_results == Constants.OBSERVATORY_HAVE_ONE_RESULT) {
            ActionErrors errors = new ActionErrors();
            errors.add("graficaObservatorio", new ActionMessage("observatorio.un.resultado"));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else {
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.SI);
        }
        return mapping.findForward(Constants.OBSERVATORY_GRAPHIC_GLOBAL_FORWARD);
    }

    public ActionForward getCategoryGraphic(HttpServletRequest request, ActionMapping mapping, long idCategory, String filePath, int observatoryType) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        filePath += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + idCategory + File.separator;
        int have_results = 0;
        if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
            Connection c = null;
            try {
                c = DataBaseManager.getConnection();
                have_results = ResultadosAnonimosObservatorioIntavUtils.generateCategoryGraphics(request, ObservatorioDAO.getCategoryById(c, idCategory), filePath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors"), false);
            } catch (Exception e) {
                Logger.putLog("Error al cargar la categoria del observatorio.", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
            have_results = ResultadosAnonimosObservatorioLenoxUtils.generateSegmentIGraphics(request, filePath);
        } else if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
            have_results = ResultadosAnonimosObservatorioMultilanguageUtils.generateCategoryGraphics(request, filePath, idCategory, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
        }
        if (have_results == Constants.OBSERVATORY_NOT_HAVE_RESULTS) {
            ActionErrors errors = new ActionErrors();
            errors.add("graficaObservatorio", new ActionMessage("observatorio.no.resultados"));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else if (have_results == Constants.OBSERVATORY_HAVE_ONE_RESULT) {
            ActionErrors errors = new ActionErrors();
            errors.add("graficaObservatorio", new ActionMessage("observatorio.un.resultado"));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else {
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.SI);
        }
        return mapping.findForward(Constants.OBSERVATORY_GRAPHIC_SEGMENT_FORWARD);
    }

    public ActionForward getEvolutionGraphic(HttpServletRequest request, ActionMapping mapping, String filePath, int observatoryType) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();
        filePath += pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
        int have_results = 0;
        if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.intav.id"))) {
            have_results = ResultadosAnonimosObservatorioIntavUtils.generateEvolutionGraphics(request, filePath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), false);
        } else if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
            have_results = ResultadosAnonimosObservatorioLenoxUtils.generateEvolutionGraphics(request, filePath);
        } else if (observatoryType == Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
            ResultadosAnonimosObservatorioMultilanguageUtils.generateEvolutionGraphics(request, filePath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
        }

        if (have_results == Constants.OBSERVATORY_NOT_HAVE_RESULTS) {
            ActionErrors errors = new ActionErrors();
            errors.add("graficaObservatorio", new ActionMessage("observatorio.no.resultados"));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else if (have_results == Constants.OBSERVATORY_HAVE_ONE_RESULT) {
            ActionErrors errors = new ActionErrors();
            errors.add("graficaObservatorio", new ActionMessage("observatorio.un.resultado"));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else {
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.SI);
        }
        return mapping.findForward(Constants.OBSERVATORY_GRAPHIC_EVOLUTION_FORWARD);
    }

    public ActionForward getFulfilledObservatories(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
        //Long observatoryId = Long.valueOf(request.getParameter(Constants.OBSERVATORY_ID));
        Long observatoryId = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));

        Connection c = null;
        //Para mostrar todos los Rastreos del Sistema
        try {
            c = DataBaseManager.getConnection();

            int numResult = ObservatorioDAO.countFulfilledObservatories(c, observatoryId);
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(c, observatoryId, (pagina - 1), null));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
        } catch (Exception e) {
            Logger.putLog("Exception: ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return mapping.findForward(Constants.GET_FULFILLED_OBSERVATORIES);
    }

}
