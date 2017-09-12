package es.inteco.rastreador2.pdf;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.pdf.builder.AnonymousResultPdfBuilder;
import es.inteco.rastreador2.pdf.builder.AnonymousResultPdfUNE2004Builder;
import es.inteco.rastreador2.pdf.builder.AnonymousResultPdfUNE2012Builder;
import es.inteco.rastreador2.pdf.builder.AnonymousResultPdfUNE2017Builder;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.utils.FileUtils;

public class AnonymousResultExportPdfAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession().getAttribute(Constants.ROLE) == null || CrawlerUtils.hasAccess(request, "export.anonymous.observatory")) {
            return listPdf(mapping, request, response);
        } else {
            return mapping.findForward(Constants.NO_PERMISSION);
        }
    }

    private ActionForward listPdf(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) {
        PropertiesManager pmgr = new PropertiesManager();

        long idExecution = request.getParameter(Constants.ID) != null ? Long.parseLong(request.getParameter(Constants.ID)) : 0;
        long idObservatory = request.getParameter(Constants.ID_OBSERVATORIO) != null ? Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)) : 0;

        String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav") + idObservatory + File.separator + idExecution + File.separator + "anonymous" + File.separator;

        String filePath = null;


        try (Connection c = DataBaseManager.getConnection()) {
            ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
            filePath = path + PDFUtils.formatSeedName(observatoryForm.getNombre()) + ".pdf";
            String graphicPath = path + "temp" + File.separator;

            File checkFile = new File(filePath);

            final String version = CartuchoDAO.getApplication(c, observatoryForm.getCartucho().getId());
            if (hasResults(idExecution)) {
                // Si el pdf no ha sido creado o lo queremos regenerar lo creamos
                if (request.getParameter(Constants.EXPORT_PDF_REGENERATE) != null || !checkFile.exists()) {
                    checkFile.createNewFile();
                    final boolean includeEvolution = includeEvolution(c, idObservatory, idExecution);
                    final AnonymousResultPdfBuilder pdfBuilder = getPdfBuilder(checkFile, observatoryForm.getTipo(), version);
                    pdfBuilder.generateGraphics(CrawlerUtils.getResources(request), request.getParameter(Constants.ID), idExecution, request.getParameter(Constants.ID_OBSERVATORIO), graphicPath);
                    List<CategoriaForm> categories = ObservatorioDAO.getExecutionObservatoryCategories(c, idExecution);
                    pdfBuilder.buildDocument(CrawlerUtils.getResources(request), null, graphicPath, Long.toString(idObservatory), Long.toString(idExecution), categories, includeEvolution);
                    FileUtils.deleteDir(new File(graphicPath));
                }
            } else {
                ActionErrors errors = new ActionErrors();
                errors.add("errorPDF", new ActionMessage("error.pdf.noResults"));
                saveErrors(request, errors);
                return mapping.findForward(Constants.GET_FULFILLED_OBSERVATORIES);
            }
        } catch (Exception e) {
            Logger.putLog("Error al exportar a pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR_PAGE);
        }

        try {
            CrawlerUtils.returnFile(response, filePath, "application/pdf", false);
        } catch (Exception e) {
            Logger.putLog("Exception al devolver el PDF", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return null;
    }

    private AnonymousResultPdfBuilder getPdfBuilder(final File file, final long tipo, final String version) throws Exception {
        if ("UNE-2012".equalsIgnoreCase(version)) {
            return new AnonymousResultPdfUNE2012Builder(file, tipo);
        } else if ("UNE-2017".equalsIgnoreCase(version)) { 		// TODO 2017 Desdoblamiento para nueva metodología
        	 return new  AnonymousResultPdfUNE2017Builder(file, tipo);
        } else {
            return new AnonymousResultPdfUNE2004Builder(file, tipo);
        }
    }

    private boolean hasResults(long idExecution) throws Exception {
        return ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(String.valueOf(idExecution), Constants.COMPLEXITY_SEGMENT_NONE, null).size() != 0;
    }

    private boolean includeEvolution(final Connection c, long idObservatory, long idExecution) throws Exception {
        final PropertiesManager pmgr = new PropertiesManager();
        final ObservatorioRealizadoForm observatoryRealizadoForm = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, idExecution);
        return ObservatorioDAO.getFulfilledObservatories(c, idObservatory, Constants.NO_PAGINACION, observatoryRealizadoForm.getFecha()).size() >= Integer.parseInt(pmgr.getValue("pdf.properties", "pdf.anonymous.results.pdf.min.obser"));
    }

}
