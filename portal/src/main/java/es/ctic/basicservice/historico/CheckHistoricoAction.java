package es.ctic.basicservice.historico;

import es.inteco.common.Constants;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Clase para comprobar el histórico de resultados asociado a una URL para su uso desde el servicio de diagnóstico.
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class CheckHistoricoAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        final CheckHistoricoService checkHistoricoService = new CheckHistoricoService();
        final List<BasicServiceResultado> historicoResultados = checkHistoricoService.getHistoricoResultados(request.getParameter("url"));

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("historico", historicoResultados);

        request.setAttribute("JSON", jsonObject.toJSONString());

        return mapping.findForward(Constants.EXITO);
    }
}
