package es.inteco.rastreador2.openOffice.export;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class ExportOpenOfficeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        long idExecution = 0;
        if (request.getParameter(Constants.ID) != null) {
            idExecution = Long.parseLong(request.getParameter(Constants.ID));
        }

        long idObservatory = 0;
        if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
            idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
        }

        final PropertiesManager pmgr = new PropertiesManager();
        final String basePath = pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office")
                + idObservatory + File.separator + idExecution + File.separator;

        String filePath = null;

        Connection c = null;
        try {
            c = DataBaseManager.getConnection();
            ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
            SimpleDateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
            ObservatorioRealizadoForm observatoryFFForm = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, idExecution);

            filePath = basePath + PDFUtils.formatSeedName(observatoryForm.getNombre()) + ".odt";
            String graphicPath = basePath + "temp" + File.separator;
            int numObs = ObservatorioDAO.getFulfilledObservatories(c, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)), Constants.NO_PAGINACION, observatoryFFForm.getFecha()).size();
            ExportOpenOfficeUtils.createOpenOfficeDocument(request, filePath, graphicPath, df.format(observatoryFFForm.getFecha()), observatoryForm.getTipo(), numObs);
            FileUtils.deleteDir(new File(graphicPath));
        } catch (Exception e) {
            Logger.putLog("Error al exportar a pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR_PAGE);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        try {
            CrawlerUtils.returnFile(filePath, response, "application/vnd.oasis.opendocument.text", false);
        } catch (Exception e) {
            Logger.putLog("Exception al devolver el PDF", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return null;
    }

}
