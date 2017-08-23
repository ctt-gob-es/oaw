package es.inteco.rastreador2.action.observatorio;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.google.gson.Gson;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.PageForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * Action para el grid de semillas.
 *
 * @author alvaro.pelaez
 */

public class JsonSemillasObservatorioAction extends DispatchAction {

	/**
	 * Buscar.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward buscar(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try (Connection c = DataBaseManager.getConnection()) {
			SemillaSearchForm searchForm = (SemillaSearchForm) form;

			if (searchForm == null) {
				searchForm = new SemillaSearchForm();
				searchForm.setNombre(request.getParameter("nombre"));

				if (!StringUtils.isEmpty(searchForm.getNombre())) {

					corregirEncoding(searchForm);
				}

				searchForm.setCategoria(request.getParameter("categoria"));
				searchForm.setUrl(request.getParameter("url"));

			}

			final int pagina = Pagination.getPage(request, Constants.PAG_PARAM);

			final int numResult = SemillaDAO.countObservatorySeeds(c, searchForm);

			response.setContentType("text/json");

			List<SemillaForm> observatorySeedsList = SemillaDAO.getObservatorySeeds(c, (pagina - 1), searchForm);

			String jsonSeeds = new Gson().toJson(observatorySeedsList);

			// Paginacion
			List<PageForm> paginas = Pagination.createPagination(request, numResult, pagina);

			String jsonPagination = new Gson().toJson(paginas);

			PrintWriter pw = response.getWriter();
			// pw.write(json);
			pw.write("{\"semillas\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult
					+ "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;

	}

	/**
	 * Update.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// request.getParameter("semilla");

		SemillaForm semilla = new SemillaForm();

		semilla.setId(Long.parseLong(request.getParameter("id")));
		semilla.setNombre(request.getParameter("nombre"));
		semilla.setAcronimo(request.getParameter("acronimo"));
		// semilla.setDependencia(request.getParameter("dependencia"));

		// Soporte a múltiples dependencias
		List<DependenciaForm> listaDependencias = new ArrayList<>();

		String dependencias = request.getParameter("dependencias");
		if (!StringUtils.isEmpty(dependencias)) {
			String[] idsDependencias = dependencias.split(",");
			for (int i = 0; i < idsDependencias.length; i++) {
				DependenciaForm dependencia = new DependenciaForm();
				dependencia.setId(Long.parseLong(idsDependencias[i]));
				listaDependencias.add(dependencia);
			}
		}

		semilla.setDependencias(listaDependencias);

		semilla.setActiva(Boolean.parseBoolean(request.getParameter("activa")));
		if (request.getParameter("listaUrls") != null) {
			semilla.setListaUrlsString(request.getParameter("listaUrls").replace("\r\n", ";"));
		}
		CategoriaForm categoriaSemilla = new CategoriaForm();
		categoriaSemilla.setId(request.getParameter("categoria"));
		semilla.setCategoria(categoriaSemilla);

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		try (Connection c = DataBaseManager.getConnection()) {

			// Comprobar que no existe una semilla con el mismo nombre
			boolean existSeed = SemillaDAO.existSeed(c, semilla.getNombre(), Constants.ID_LISTA_SEMILLA_OBSERVATORIO);

			if (existSeed && !semilla.getNombre().equals(request.getParameter(Constants.NOMBRE_ANTIGUO))) {

				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.semilla.duplicado"));

			}

			else {

				SemillaDAO.editSeed(c, semilla);

			}

		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			response.setStatus(400);
			response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
		}

		return null;
	}

	/**
	 * Save.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

		List<JsonMessage> errores = new ArrayList<>();

		if (StringUtils.isEmpty(request.getParameter("nombre"))) {

			errores.add(new JsonMessage(messageResources.getMessage("semilla.nueva.nombre.requerido")));
		}

		if (StringUtils.isEmpty(request.getParameter("urls"))) {
			errores.add(new JsonMessage(messageResources.getMessage("semilla.nueva.url.requerido")));
		}

		if (!errores.isEmpty()) {

			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));

		} else {

			SemillaForm semilla = new SemillaForm();

			semilla.setNombre(request.getParameter("nombre"));
			semilla.setAcronimo(request.getParameter("acronimo"));

			// Soporte a múltiples dependencias
			List<DependenciaForm> listaDependencias = new ArrayList<>();

			String[] dependencias = request.getParameterValues("dependencias");
			if (dependencias != null && dependencias.length > 0) {
				for (int i = 0; i < dependencias.length; i++) {
					DependenciaForm dependencia = new DependenciaForm();
					dependencia.setId(Long.parseLong(dependencias[i]));
					listaDependencias.add(dependencia);
				}
			}

			semilla.setDependencias(listaDependencias);

			semilla.setActiva(Boolean.parseBoolean(request.getParameter("activa")));
			if (request.getParameter("urls") != null) {

				semilla.setListaUrls(Arrays.asList(request.getParameter("urls").replace("\r\n", ";").split(";")));
			}
			CategoriaForm categoriaSemilla = new CategoriaForm();
			categoriaSemilla.setId(request.getParameter("segmento"));
			semilla.setCategoria(categoriaSemilla);

			try (Connection c = DataBaseManager.getConnection()) {
				// TODO Comporbar que no existe otra con el mismo nombre

				if (SemillaDAO.existSeed(c, semilla.getNombre(), Constants.ID_LISTA_SEMILLA_OBSERVATORIO)) {
					response.setStatus(400);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.semilla.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					SemillaDAO.saveSeedMultidependencia(c, semilla);
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.semilla.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}

			} catch (Exception e) {
				Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);

				errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.generico")));
				response.setStatus(400);
				response.getWriter().write(new Gson().toJson(errores));
			}
		}

		return null;
	}

	/**
	 * List categorias.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward listCategorias(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {

			List<CategoriaForm> listCategorias = SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION);

			String jsonCategorias = new Gson().toJson(listCategorias);

			PrintWriter pw = response.getWriter();
			pw.write(jsonCategorias);
			pw.flush();
			pw.close();

		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return null;
	}

	/**
	 * List dependencias.
	 *
	 * @param mapping
	 *            the mapping
	 * @param form
	 *            the form
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	public ActionForward listDependencias(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {

			List<DependenciaForm> listDependencias = SemillaDAO.getSeedDependencias(c, Constants.NO_PAGINACION);

			String jsonDependencias = new Gson().toJson(listDependencias);

			PrintWriter pw = response.getWriter();
			pw.write(jsonDependencias);
			pw.flush();
			pw.close();

		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return null;
	}

	/**
	 * Corregir encoding.
	 *
	 * @param searchForm
	 *            the search form
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	private void corregirEncoding(SemillaSearchForm searchForm) throws UnsupportedEncodingException {
		Charset utf8charset = Charset.forName("UTF-8");
		Charset iso88591charset = Charset.forName("ISO-8859-1");

		// decode UTF-8
		CharBuffer data = utf8charset.decode(ByteBuffer.wrap(searchForm.getNombre().getBytes("UTF-8")));

		// encode ISO-8559-1
		ByteBuffer outputBuffer = iso88591charset.encode(data);
		byte[] outputData = outputBuffer.array();

		String nombreCorregido = new String(outputData);

		searchForm.setNombre(nombreCorregido);
	}

}
