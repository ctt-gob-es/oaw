package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.utils.CacheUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.SubirConclusionesForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils;
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

    private static final String ERROR_PROPERTY_GRAFICA_OBSERVATORIO = "graficaObservatorio";
    private static final String OBSERVATORY_NO_RESULTS = "observatorio.no.resultados";
    private static final String OBSERVATORY_ONE_RESULT = "observatorio.un.resultado";

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
        final PropertiesManager pmgr = new PropertiesManager();
        final String path = pmgr.getValue(CRAWLER_PROPERTIES, "conclusion.path") + request.getParameter(Constants.ID_OBSERVATORIO) + File.separator + request.getParameter(Constants.ID_EX_OBS) + File.separator;
        FileOutputStream fileD = null;
        try {
            final File file = new File(path + "conclusion.xml");
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

    private String getFilePath(final HttpServletRequest request) {
        final String executionId = request.getParameter(Constants.ID);
        final String observatoryId = request.getParameter(Constants.ID_OBSERVATORIO);

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        final PropertiesManager pmgr = new PropertiesManager();
        String filePath = "";
        if (Integer.valueOf(request.getParameter(Constants.TYPE_OBSERVATORY)) != null) {
            try (final Connection c = DataBaseManager.getConnection()){
                if (CartuchoDAO.isCartuchoAccesibilidad(c, Long.parseLong(request.getParameter(Constants.TYPE_OBSERVATORY)))) {
                    filePath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.intav.files") + observatoryId + File.separator + executionId + File.separator + language.getLanguage() + File.separator;
                } else if (request.getParameter(Constants.TYPE_OBSERVATORY).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.lenox.id"))) {
                    filePath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.lenox.files") + observatoryId + File.separator + executionId + File.separator + language.getLanguage() + File.separator;
                } else if (request.getParameter(Constants.TYPE_OBSERVATORY).equals(pmgr.getValue(CRAWLER_PROPERTIES, "cartridge.multilanguage.id"))) {
                    filePath = pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.multilanguage.files") + observatoryId + File.separator + executionId + File.separator + language.getLanguage() + File.separator;
                }
            } catch (Exception e) {
                Logger.putLog("Error al recuperar el path de los ficheros temporales.", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
        return filePath;
    }

    private ActionForward regenerateGraphic(ActionMapping mapping, HttpServletRequest request, String filePath) throws Exception {
        final String graphic = request.getParameter(Constants.GRAPHIC);
        final PropertiesManager pmgr = new PropertiesManager();

        final File file;
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

        final String idExecution = request.getParameter(Constants.ID);
        CacheUtils.removeFromCache(Constants.OBSERVATORY_KEY_CACHE + idExecution);

        return getGraphics(mapping, request, filePath);
    }

    private ActionForward getGraphics(final ActionMapping mapping, final HttpServletRequest request, final String filePath) throws Exception {
        final String graphic = request.getParameter(Constants.GRAPHIC);
        final int observatoryType = Integer.parseInt(request.getParameter(Constants.TYPE_OBSERVATORY));
        final long idExecutionObservatory = Long.parseLong(request.getParameter(Constants.ID));

        try (final Connection c = DataBaseManager.getConnection()){
            final List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, idExecutionObservatory);
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
        }

        return mapping.findForward(Constants.ERROR_PAGE);
    }

    private ActionForward getGlobalGraphic(final HttpServletRequest request, final ActionMapping mapping, final String basePath, final int observatoryType, final List<CategoriaForm> categories) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final String graphicsPath = basePath + pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.global") + File.separator;
        int haveResults = 0;
        try (final Connection c = DataBaseManager.getConnection()){
            if (CartuchoDAO.isCartuchoAccesibilidad(c, observatoryType)) {
                final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
                final String application = CartuchoDAO.getApplication(c, observatoryForm.getCartucho().getId());
                if ("UNE-2012".equalsIgnoreCase(application)) {
                    haveResults = ResultadosAnonimosObservatorioUNE2012Utils.generateGlobalGraphics(request, graphicsPath, categories, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors"), false);
                } else {
                    haveResults = ResultadosAnonimosObservatorioIntavUtils.generateGlobalGraphics(request, graphicsPath, categories, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors"), false);
                }
            } else {
                return mapping.findForward(Constants.ERROR);
            }
        } catch (Exception e) {
            Logger.putLog("Error al generar los gráficos globales del Observatorio.", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        }
        if (haveResults == Constants.OBSERVATORY_NOT_HAVE_RESULTS) {
            ActionErrors errors = new ActionErrors();
            errors.add(ERROR_PROPERTY_GRAFICA_OBSERVATORIO, new ActionMessage(OBSERVATORY_NO_RESULTS));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else if (haveResults == Constants.OBSERVATORY_HAVE_ONE_RESULT) {
            ActionErrors errors = new ActionErrors();
            errors.add(ERROR_PROPERTY_GRAFICA_OBSERVATORIO, new ActionMessage(OBSERVATORY_ONE_RESULT));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else {
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.SI);
        }
        return mapping.findForward(Constants.OBSERVATORY_GRAPHIC_GLOBAL_FORWARD);
    }

    private ActionForward getCategoryGraphic(final HttpServletRequest request, final ActionMapping mapping, final long idCategory, final String filePath, final int observatoryType) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final String graphicsPath = filePath + pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.seg") + idCategory + File.separator;
        int haveResults = 0;
        final Connection c = DataBaseManager.getConnection();
        if (CartuchoDAO.isCartuchoAccesibilidad(c, observatoryType)) {
            try {
                final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
                final String application = CartuchoDAO.getApplication(c, observatoryForm.getCartucho().getId());
                if ("UNE-2012".equalsIgnoreCase(application)) {
                    haveResults = ResultadosAnonimosObservatorioUNE2012Utils.generateCategoryGraphics(request, ObservatorioDAO.getCategoryById(c, idCategory), graphicsPath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors"), false);
                } else {
                    haveResults = ResultadosAnonimosObservatorioIntavUtils.generateCategoryGraphics(request, ObservatorioDAO.getCategoryById(c, idCategory), graphicsPath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors"), false);
                }
            } catch (Exception e) {
                Logger.putLog("Error al cargar la categoria del observatorio.", ResultadosAnonimosObservatorioIntavUtils.class, Logger.LOG_LEVEL_ERROR, e);
            } finally {
                DataBaseManager.closeConnection(c);
            }
        } else {
            haveResults = Constants.OBSERVATORY_NOT_HAVE_RESULTS;
        }
        if (haveResults == Constants.OBSERVATORY_NOT_HAVE_RESULTS) {
            ActionErrors errors = new ActionErrors();
            errors.add(ERROR_PROPERTY_GRAFICA_OBSERVATORIO, new ActionMessage(OBSERVATORY_NO_RESULTS));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else if (haveResults == Constants.OBSERVATORY_HAVE_ONE_RESULT) {
            ActionErrors errors = new ActionErrors();
            errors.add(ERROR_PROPERTY_GRAFICA_OBSERVATORIO, new ActionMessage(OBSERVATORY_ONE_RESULT));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else {
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.SI);
        }
        DataBaseManager.closeConnection(c);
        return mapping.findForward(Constants.OBSERVATORY_GRAPHIC_SEGMENT_FORWARD);
    }

    private ActionForward getEvolutionGraphic(final HttpServletRequest request, final ActionMapping mapping, final String filePath, final int observatoryType) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final String fileEvolutionPath = filePath + pmgr.getValue(CRAWLER_PROPERTIES, "path.observatory.chart.evolution") + File.separator;
        final int haveResults;
        final Connection c = DataBaseManager.getConnection();
        String forward = Constants.OBSERVATORY_GRAPHIC_EVOLUTION_FORWARD;
        if (CartuchoDAO.isCartuchoAccesibilidad(c, observatoryType)) {
            final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)));
            final String application = CartuchoDAO.getApplication(c, observatoryForm.getCartucho().getId());
            if ("UNE-2012".equalsIgnoreCase(application)) {
                haveResults = ResultadosAnonimosObservatorioUNE2012Utils.generateEvolutionGraphics(request, fileEvolutionPath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), false);
                forward = "getEvolutionGraphicsUNE2102";
            } else {
                haveResults = ResultadosAnonimosObservatorioIntavUtils.generateEvolutionGraphics(request, fileEvolutionPath, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), false);
            }
        } else {
            haveResults = 0;
        }
        DataBaseManager.closeConnection(c);

        if (haveResults == Constants.OBSERVATORY_NOT_HAVE_RESULTS) {
            ActionErrors errors = new ActionErrors();
            errors.add(ERROR_PROPERTY_GRAFICA_OBSERVATORIO, new ActionMessage(OBSERVATORY_NO_RESULTS));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else if (haveResults == Constants.OBSERVATORY_HAVE_ONE_RESULT) {
            ActionErrors errors = new ActionErrors();
            errors.add(ERROR_PROPERTY_GRAFICA_OBSERVATORIO, new ActionMessage(OBSERVATORY_ONE_RESULT));
            saveErrors(request, errors);
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.NO);
        } else {
            request.setAttribute(Constants.OBSERVATORY_RESULTS, Constants.SI);
        }
        return mapping.findForward(forward);
    }

    public ActionForward getFulfilledObservatories(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
        final Long observatoryId = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));

        //Para mostrar todos los Rastreos del Sistema
        try (final Connection c = DataBaseManager.getConnection()){
            final int numResult = ObservatorioDAO.countFulfilledObservatories(c, observatoryId);
            final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(c, observatoryId, (pagina - 1), null));
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));
        } catch (Exception e) {
            Logger.putLog("Exception: ", ResultadosAnonimosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }

        return mapping.findForward(Constants.GET_FULFILLED_OBSERVATORIES);
    }

}
