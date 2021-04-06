package es.gob.oaw.basicservice.historico;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;

/**
 * Clase para comprobar el histórico de resultados asociado a una URL para su uso desde el servicio de diagnóstico.
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class CheckHistoricoAction extends Action {

    /**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 */
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
        final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
        // El parametro url debería venir encodeado (ej http%3A%2F%2Fwww.example.com)
        final String url = decodeUrlParam(request.getParameter("url"));
        final String depth = request.getParameter("depth");
        final String width = request.getParameter("width");
        final String report = request.getParameter("report");
        final BasicServiceAnalysisType type = BasicServiceAnalysisType.parseString(request.getParameter("type"));
        final List<BasicServiceResultado> historicoResultados = checkHistoricoService.getHistoricoResultados(url, type, depth, width, report);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("historico", historicoResultados);

        request.setAttribute("JSON", jsonObject.toJSONString());

        return mapping.findForward(Constants.EXITO);
    }

    /**
	 * Decode url param.
	 *
	 * @param urlParam the url param
	 * @return the string
	 */
    private String decodeUrlParam(final String urlParam) {
        String url;
        try {
            url = URLDecoder.decode(urlParam, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            Logger.putLog("No se puede decodificar la url como ISO-8859-1", CheckHistoricoAction.class, Logger.LOG_LEVEL_WARNING, e);
            try {
                url = URLDecoder.decode(urlParam, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                url = "";
                Logger.putLog("No se puede decodificar la url como UTF-8", CheckHistoricoAction.class, Logger.LOG_LEVEL_WARNING, e);
            }
        }
        return url;
    }
}
