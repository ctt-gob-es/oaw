package es.inteco.rastreador2.action.observatorio;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;
import com.opensymphony.oscache.util.StringUtil;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.PageForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ResultadoSemillaFullForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.Pagination;

public class JsonResultadoSemillaObservatorioAction extends DispatchAction {

	
    public ActionForward resultados(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        SemillaForm semillaForm = new SemillaForm();
        //SemillaForm semillaForm = (SemillaForm) form;
        
        final Long idObservatoryExecution = Long.parseLong(request.getParameter(Constants.ID_EX_OBS));

        try (Connection c = DataBaseManager.getConnection()) {
            final PropertiesManager pmgr = new PropertiesManager();
            
            
            
            //Nombre
            if(request.getParameter("nombre")!=null && !StringUtils.isEmpty(request.getParameter("nombre").toString())) {
            	semillaForm.setNombre(request.getParameter("nombre").toString());
            }
            
            //URL
            if(request.getParameter("listaUrlsString")!=null && !StringUtils.isEmpty(request.getParameter("listaUrlsString").toString())) {
            	semillaForm.setListaUrlsString(request.getParameter("listaUrlsString").toString());
            }            

            final int numResultA = ObservatorioDAO.countResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE);
            final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
            // Obtenemos las semillas de esa página del listado
            //final List<ResultadoSemillaForm> seedsResults = ObservatorioDAO.getResultSeedsFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE, pagina - 1);

            
            final List<ResultadoSemillaFullForm> seedsResults2 = ObservatorioDAO.getResultSeedsFullFromObservatory(c, semillaForm, idObservatoryExecution, (long) Constants.COMPLEXITY_SEGMENT_NONE, pagina - 1);
            
            // Calculamos la puntuación media de cada semilla y la guardamos en sesion
            ObservatoryUtils.setAvgScore2(c, seedsResults2, idObservatoryExecution);
            
			String jsonResultados = new Gson().toJson(seedsResults2);
            
            List<PageForm> paginas = Pagination.createPagination(request, numResultA, pmgr.getValue(CRAWLER_PROPERTIES, "observatoryListSeed.pagination.size"), pagina, Constants.PAG_PARAM);

			String jsonPagination = new Gson().toJson(paginas);

			PrintWriter pw = response.getWriter();
			// pw.write(json);
			pw.write("{\"resultados\": " + jsonResultados.toString() + ",\"paginador\": {\"total\":" + numResultA
					+ "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
            
        } catch (Exception e) {
            Logger.putLog("Error al cargar el formulario para crear un nuevo rastreo de cliente", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
            throw new Exception(e);
        }

        return null;
    }
	
}


