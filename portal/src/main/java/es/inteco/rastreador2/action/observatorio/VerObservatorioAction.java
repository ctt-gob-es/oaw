package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.VerObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public class VerObservatorioAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (CrawlerUtils.hasAccess(request, "view.observatory")) {
                if (isCancelled(request)) {
                    return mapping.findForward(Constants.VOLVER_CARGA);
                }

                String action = request.getParameter(Constants.ACCION);
                if (action != null && action.equals(Constants.GET_DETAIL)) {
                    return getDetail(mapping, request);
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

    private ActionForward getDetail(ActionMapping mapping, HttpServletRequest request) throws Exception {
        PropertiesManager pmgr = new PropertiesManager();

        long idObservatorio = 0;
        if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
            idObservatorio = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
        }

        Connection c = null;
        Connection con = null;

        try {
            c = DataBaseManager.getConnection();
            con = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));

            int numResult = ObservatorioDAO.countSeeds(c, idObservatorio);
            int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

            VerObservatorioForm verObservatorioForm = ObservatorioDAO.getObservatoryView(c, idObservatorio, (pagina - 1));
            verObservatorioForm.setNormaAnalisisSt(RastreoDAO.getNombreNorma(con, verObservatorioForm.getNormaAnalisis()));
            request.setAttribute(Constants.VER_OBSERVATORIO_FORM, verObservatorioForm);
            request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, pagina));

        } catch (Exception e) {
            Logger.putLog("Exception: ", VerObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            DataBaseManager.closeConnection(c);
            DataBaseManager.closeConnection(con);
        }

        return mapping.findForward(Constants.EXITO);
    }

}
