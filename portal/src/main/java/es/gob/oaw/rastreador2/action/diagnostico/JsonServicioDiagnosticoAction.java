package es.gob.oaw.rastreador2.action.diagnostico;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

import com.google.gson.Gson;

import es.gob.oaw.rastreador2.actionform.diagnostico.ServicioDiagnosticoForm;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.PageForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.JsonSemillasObservatorioAction;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class JsonServicioDiagnosticoAction.
 */
public class JsonServicioDiagnosticoAction extends DispatchAction {
	/**
	 * Search.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			ServicioDiagnosticoForm search = new ServicioDiagnosticoForm();
			if (!StringUtils.isEmpty(search.getUser())) {
				search.setUser(request.getParameter("usuario"));
			}
			if (!StringUtils.isEmpty(request.getParameter("url"))) {
				search.setDomain(request.getParameter("url"));
			}
			if (!StringUtils.isEmpty(request.getParameter("email"))) {
				search.setEmail(request.getParameter("email"));
			}
			if (!StringUtils.isEmpty(request.getParameter("profundidad"))) {
				search.setDepth(Integer.parseInt(request.getParameter("profundidad")));
			}
			if (!StringUtils.isEmpty(request.getParameter("anchura"))) {
				search.setWidth(Integer.parseInt(request.getParameter("anchura")));
			}
			if (!StringUtils.isEmpty(request.getParameter("informe"))) {
				search.setReport(request.getParameter("informe"));
			}
			if (!StringUtils.isEmpty(request.getParameter("estado"))) {
				search.setStatus(request.getParameter("estado"));
			}
			if (!StringUtils.isEmpty(request.getParameter("tipo"))) {
				search.setAnalysisType(request.getParameter("tipo"));
			}
			if (!StringUtils.isEmpty(request.getParameter("directorio"))) {
				search.setInDirectory(Integer.parseInt(request.getParameter("directorio")));
			}
			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);
			final int numResult = DiagnosisDAO.count(c, search);
			List<ServicioDiagnosticoForm> listSD = DiagnosisDAO.find(c, (pagina - 1), search);
			String jsonSeeds = new Gson().toJson(listSD);
			response.setContentType("text/json");
			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);
			String jsonPagination = new Gson().toJson(paginas);
			PrintWriter pw = response.getWriter();
			// pw.write(json);
			pw.write("{\"diagnosticos\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Export.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection conn = DataBaseManager.getConnection()) {
			ServicioDiagnosticoForm search = new ServicioDiagnosticoForm();
			if (!StringUtils.isEmpty(search.getUser())) {
				search.setUser(request.getParameter("usuario"));
			}
			if (!StringUtils.isEmpty(request.getParameter("url"))) {
				search.setDomain(request.getParameter("url"));
			}
			if (!StringUtils.isEmpty(request.getParameter("email"))) {
				search.setEmail(request.getParameter("email"));
			}
			if (!StringUtils.isEmpty(request.getParameter("profundidad"))) {
				search.setDepth(Integer.parseInt(request.getParameter("profundidad")));
			}
			if (!StringUtils.isEmpty(request.getParameter("anchura"))) {
				search.setWidth(Integer.parseInt(request.getParameter("anchura")));
			}
			if (!StringUtils.isEmpty(request.getParameter("informe"))) {
				search.setReport(request.getParameter("informe"));
			}
			if (!StringUtils.isEmpty(request.getParameter("estado"))) {
				search.setStatus(request.getParameter("estado"));
			}
			if (!StringUtils.isEmpty(request.getParameter("tipo"))) {
				search.setAnalysisType(request.getParameter("tipo"));
			}
			if (!StringUtils.isEmpty(request.getParameter("directorio"))) {
				search.setInDirectory(Integer.parseInt(request.getParameter("directorio")));
			}
			final String csv = DiagnosisDAO.getCSV(conn, search);
			CrawlerUtils.returnStringAsFile(response, csv, "servicio_diagnostico_" + new Date().getTime() + ".csv", "text/csv");
		} catch (Exception e) {
			Logger.putLog("ERROR al intentar exportar datos del servicio de diagnóstico", ServicioDiagnosticoAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Convert to including end date.
	 *
	 * @param format  the format
	 * @param endDate the end date
	 * @return the date
	 * @throws ParseException the parse exception
	 */
	private Date convertToIncludingEndDate(final DateFormat format, final String endDate) throws ParseException {
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(format.parse(endDate));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * Comprueba que el rango de fechas de la exportación del servicio de diagnóstico sea correcto (la fecha de inicio no puede ser posterior a la final).
	 *
	 * @param errors                  the errors
	 * @param servicioDiagnosticoForm the servicio diagnostico form
	 */
	private void validarRangoFechas(final ActionErrors errors, final ServicioDiagnosticoForm servicioDiagnosticoForm) {
		final DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			final Date startDate = format.parse(servicioDiagnosticoForm.getStartDate());
			final Date endDate = format.parse(servicioDiagnosticoForm.getEndDate());
			// Si la fecha de inicio es posterior a la final es un fallo
			if (startDate.after(endDate)) {
				errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.date.range"));
			}
		} catch (ParseException pe) {
			errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("errors.date.range"));
		}
	}
}
