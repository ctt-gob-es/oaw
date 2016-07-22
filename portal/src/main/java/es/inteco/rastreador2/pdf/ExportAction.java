package es.inteco.rastreador2.pdf;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.crawler.sexista.modules.analisis.dto.ResultsByUrlDto;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.VerRastreoForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.lenox.service.InformesService;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ExportAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute(Constants.ROLE) == null ||
                CrawlerUtils.hasAccess(request, "export.crawler.report")) {
            String crawlerType = request.getParameter(Constants.EXPORT_PDF_TYPE);
            return listPdf(mapping, form, request, response, crawlerType);
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }

    }

    public ActionForward listPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response, String crawlerType) {

        String user = (String) request.getSession().getAttribute(Constants.USER);

        long idTracking = 0;
        if (request.getParameter(Constants.ID) != null) {
            idTracking = Long.parseLong(request.getParameter(Constants.ID));
        }
        long idRastreo = 0;
        if (request.getParameter(Constants.ID_RASTREO) != null) {
            idRastreo = Long.parseLong(request.getParameter(Constants.ID_RASTREO));
        }

        Locale language = getLocale(request);
        if (language == null) {
            language = request.getLocale();
        }

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            //Comprobamos que el usuario esta asociado con el rastreo que quiere exportar
            if (user == null || RastreoDAO.crawlerToUser(c, idRastreo, user) || RastreoDAO.crawlerToClientAccount(c, idRastreo, user)) {
                if (crawlerType.equals(Constants.EXPORT_PDF_INTAV) || crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
                    return createIntavPdf(mapping, form, request, response, idRastreo, idTracking, language, crawlerType);
                } else if (crawlerType.equals(Constants.EXPORT_PDF_LENOX)) {
                    return createLenoxPdf(mapping, form, request, response, idRastreo, idTracking, language);
                }
            } else {
                return mapping.findForward(Constants.NO_PERMISSION);
            }
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        } finally {
            DataBaseManager.closeConnection(c);
        }
        return null;
    }

    private ActionForward createLenoxPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response, long idRastreo, long idTracking, Locale language) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();

        String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.lenox")
                + idRastreo + File.separator + idTracking + File.separator + language.getLanguage();

        File checkFile = new File(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.file.lenox.name"));

        InformesService informesService = new InformesService();
        List<ResultsByUrlDto> results = informesService.getResultsByUrl(idTracking, Constants.NO_PAGINACION);
        if (results != null && !results.isEmpty()) {
            // Si el pdf no ha sido creado lo creamos
            if (request.getParameter(Constants.EXPORT_PDF_REGENERATE) != null || !checkFile.exists()) {
                LenoxExport.exportLenoxToPdf(results, request, path);
            }
        } else {
            ActionErrors errors = new ActionErrors();
            errors.add("errorPDF", new ActionMessage("pdf.error.no.results"));
            saveErrors(request, errors);
            return mapping.findForward(Constants.PDF_ERROR_FORWARD);
        }

        CrawlerUtils.returnFile(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.file.lenox.name"), response, "application/pdf", false);

        return null;
    }

    private ActionForward createIntavPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response, long idRastreo, long idTracking, Locale language, String crawlerType) throws Exception {

        PropertiesManager pmgr = new PropertiesManager();

        String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.intav")
                + idRastreo + File.separator + idTracking + File.separator + language.getLanguage();

        File checkFile = null;
        if (crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
            checkFile = new File(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.simple.file.intav.name"));
        } else {
            checkFile = new File(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.file.intav.name"));
        }

        List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idTracking);
        if (evaluationIds != null && !evaluationIds.isEmpty()) {
            // Si el pdf no ha sido creado lo creamos
            if (request.getParameter(Constants.EXPORT_PDF_REGENERATE) != null || !checkFile.exists()) {
                if (crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
                    IntavExport.exportIntavToSimplePdf(resultEvolutionData(idRastreo, idTracking), request, path, idTracking);
                } else {
                    IntavExport.exportIntavToPdf(resultEvolutionData(idRastreo, idTracking), request, path, idTracking);
                }

            }
        } else {
            if (request.getParameter("key") == null) {
                ActionErrors errors = new ActionErrors();
                errors.add("errorPDF", new ActionMessage("pdf.error.no.results"));
                saveErrors(request, errors);
                return mapping.findForward(Constants.PDF_ERROR_FORWARD);
            } else {
                return null;
            }
        }

        if (crawlerType.equals(Constants.EXPORT_PDF_INTAV_SIMPLE)) {
            CrawlerUtils.returnFile(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.simple.file.intav.name"), response, "application/pdf", false);
        } else {
            CrawlerUtils.returnFile(path + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.file.intav.name"), response, "application/pdf", false);
        }

        return null;
    }

    public static Map<Long, List<Long>> resultEvolutionData(Long idRastreo, Long idTracking) throws Exception {
        final Connection c = DataBaseManager.getConnection();
        try {
            VerRastreoForm vrf = new VerRastreoForm();
            vrf = RastreoDAO.cargarRastreoVer(c, idRastreo, vrf);
            final List<Long> trackingsIds = RastreoDAO.getEvolutionExecutedCrawlerIds(c, idRastreo, idTracking, vrf.getId_cartucho());
            final Map<Long, List<Long>> evaluationIdsMap = new HashMap<Long, List<Long>>();
            for (Long idTrack : trackingsIds) {
                List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idTrack);
                evaluationIdsMap.put(idTrack, evaluationIds);
            }

            return evaluationIdsMap;
        } catch (Exception e) {
            Logger.putLog("Excepción genérica al generar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            DataBaseManager.closeConnection(c);
        }
    }

}
