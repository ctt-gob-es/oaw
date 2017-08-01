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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.actions.DispatchAction;

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
import es.inteco.rastreador2.utils.Pagination;

/**
 * 
 * Action para el grid de semillas
 * 
 * 
 * @author alvaro.pelaez
 *
 */

public class JsonSemillasObservatorioAction extends DispatchAction {

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

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.getParameter("semilla");

		SemillaForm semilla = new SemillaForm();

		semilla.setId(Long.parseLong(request.getParameter("id")));
		semilla.setNombre(request.getParameter("nombre"));
		semilla.setAcronimo(request.getParameter("acronimo"));
		//semilla.setDependencia(request.getParameter("dependencia"));
		
		//Soporte a múltiples dependencias
		List<DependenciaForm> listaDependencias = new ArrayList<>();
		
		String dependencias = request.getParameter("dependencias");
		if(!StringUtils.isEmpty(dependencias)){
			String[] idsDependencias = dependencias.split(",");
			for(int i=0; i<idsDependencias.length;i++){
				DependenciaForm dependencia = new DependenciaForm();
				dependencia.setId(Long.parseLong(idsDependencias[i]));
				listaDependencias.add(dependencia);
			}
		}
		
		semilla.setDependencias(listaDependencias);
		
		
		semilla.setActiva(Boolean.parseBoolean(request.getParameter("activa")));
		if(request.getParameter("listaUrls")!=null){
		semilla.setListaUrlsString(request.getParameter("listaUrls").replace("\r\n", ";"));
		}
		CategoriaForm categoriaSemilla = new CategoriaForm();
		categoriaSemilla.setId(request.getParameter("categoria"));
		semilla.setCategoria(categoriaSemilla);

		ActionErrors errors = new ActionErrors();

		String idSemilla = request.getParameter("id");
		String modificada = request.getParameter(Constants.ES_PRIMERA);
		try (Connection c = DataBaseManager.getConnection()) {
			SemillaDAO.editSeed(c, semilla);
//			request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
//			if (modificada == null) {
//				SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
//				semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace(";", "\r\n"));
//				semillaForm.setNombre_antiguo(semillaForm.getNombre());
//				request.setAttribute(Constants.OBSERVATORY_SEED_FORM, semillaForm);
//			} else {
//				SemillaForm semillaForm = (SemillaForm) form;
//				semillaForm.setId(Long.parseLong(idSemilla));
//				boolean existSeed = SemillaDAO.existSeed(c, semillaForm.getNombre(),
//						Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
//
//				if (existSeed && !semillaForm.getNombre().equals(request.getParameter(Constants.NOMBRE_ANTIGUO))) {
//					errors.add("semillaDuplicada", new ActionMessage("mensaje.error.nombre.semilla.duplicado"));
//					saveErrors(request.getSession(), errors);
//					return mapping.findForward(Constants.EDIT_SEED);
//				}
//
//				semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace("\r\n", ";"));
//				SemillaDAO.editSeed(c, semillaForm);
//
//				errors.add("semillaModificada", new ActionMessage("mensaje.exito.semilla.modificada"));
//				saveErrors(request.getSession(), errors);
//
//				ActionForward forward = new ActionForward(mapping.findForward(Constants.LOAD_SEEDS_FORWARD));
//				forward.setRedirect(true);
//				return forward;
//			}
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}

		return null;
	}

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
