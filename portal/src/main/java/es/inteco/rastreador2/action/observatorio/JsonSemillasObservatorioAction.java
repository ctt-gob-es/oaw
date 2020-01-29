/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública,
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
* ANY KIND, either express or implied. See the License for the specific language governing
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not,
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.inteco.rastreador2.action.observatorio;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
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
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.json.JsonMessage;
import es.inteco.rastreador2.utils.Pagination;

/**
 * Action para el grid de semillas.
 *
 * @author alvaro.pelaez
 */
public class JsonSemillasObservatorioAction extends DispatchAction {
	/** The message resources. */
	private MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");

	/**
	 * Buscar.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward buscar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			SemillaSearchForm searchForm = (SemillaSearchForm) form;
			if (searchForm != null) {
				searchForm = new SemillaSearchForm();
				searchForm.setNombre(request.getParameter("nombre"));
				if (!StringUtils.isEmpty(searchForm.getNombre())) {
					searchForm.setNombre(es.inteco.common.utils.StringUtils.corregirEncoding(searchForm.getNombre()));
				}
				if (!StringUtils.isEmpty(request.getParameter("categoria"))) {
					// searchForm.setCategoria((request.getParameter("categoria")).split(","));
					searchForm.setCategoria((request.getParameterValues("categoria")));
				}
				if (!StringUtils.isEmpty(request.getParameter("ambito"))) {
					searchForm.setAmbito((request.getParameterValues("ambito")));
				}
				if (!StringUtils.isEmpty(request.getParameter("dependencia"))) {
					searchForm.setDependencia((request.getParameterValues("dependencia")));
				}
				if (!StringUtils.isEmpty(request.getParameter("complejidad"))) {
					searchForm.setComplejidad((request.getParameterValues("complejidad")));
				}
				searchForm.setUrl(request.getParameter("url"));
				searchForm.setinDirectorio(request.getParameter("directorio"));
				searchForm.setisActiva(request.getParameter("activa"));
				searchForm.setEliminada(request.getParameter("eliminada"));
				if (request.getParameter("etiquetas") != "") {
					String[] tagArr = request.getParameter("etiquetas").split(",");
					searchForm.setEtiquetas(tagArr);
				}
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
			pw.write("{\"semillas\": " + jsonSeeds.toString() + ",\"paginador\": {\"total\":" + numResult + "}, \"paginas\": " + jsonPagination.toString() + "}");
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Actualiza la semilla.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SemillaForm semilla = (SemillaForm) form;
		List<JsonMessage> errores = new ArrayList<>();
		ActionErrors errors = semilla.validate(mapping, request);
		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			if (errors.get("nombre").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.nombre.requerido")));
			}
			if (errors.get("listaUrlsString").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.requerido")));
			}
		}
		if (!errores.isEmpty()) {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		} else {
			// Soporte a múltiples dependencias
			List<DependenciaForm> listaDependencias = new ArrayList<>();
			// Si viene de la edición en el grid, el parámetro viene como
			// valores separados por comas, si viene de la edición en los
			// reslutados de observatorio viene el parametro tantas veces como
			// valores tenga
			String[] arrayDependendencias = request.getParameterValues("dependenciasSeleccionadas");
			if (arrayDependendencias != null && arrayDependendencias.length > 1) {
				for (int i = 0; i < arrayDependendencias.length; i++) {
					DependenciaForm dependencia = new DependenciaForm();
					dependencia.setId(Long.parseLong(arrayDependendencias[i]));
					listaDependencias.add(dependencia);
				}
			} else {
				// Solo un parámetro que intentaremos separar por comas, si no
				// las tiene devolverá un único valor
				String dependencias = request.getParameter("dependenciasSeleccionadas");
				if (!StringUtils.isEmpty(dependencias)) {
					String[] idsDependencias = dependencias.split(",");
					for (int i = 0; i < idsDependencias.length; i++) {
						DependenciaForm dependencia = new DependenciaForm();
						dependencia.setId(Long.parseLong(idsDependencias[i]));
						listaDependencias.add(dependencia);
					}
				}
			}
			semilla.setDependencias(listaDependencias);
			// Soporte a múltiples etiquetas
			List<EtiquetaForm> listaEtiquetas = new ArrayList<>();
			// Si viene de la edición en el grid, el parámetro viene como
			// valores separados por comas, si viene de la edición en los
			// reslutados de observatorio viene el parametro tantas veces como
			// valores tenga
			String[] arrayEtiquetas = request.getParameterValues("etiquetasSeleccionadas");
			if (arrayEtiquetas != null && arrayEtiquetas.length > 1) {
				for (int i = 0; i < arrayEtiquetas.length; i++) {
					EtiquetaForm etiqueta = new EtiquetaForm();
					etiqueta.setId(Long.parseLong(arrayEtiquetas[i]));
					listaEtiquetas.add(etiqueta);
				}
			} else {
				// Solo un parámetro que intentaremos separar por comas, si no
				// las tiene devolverá un único valor
				String etiquetas = request.getParameter("etiquetasSeleccionadas");
				if (!StringUtils.isEmpty(etiquetas)) {
					String[] idsEtiquetas = etiquetas.split(",");
					for (int i = 0; i < idsEtiquetas.length; i++) {
						EtiquetaForm etiqueta = new EtiquetaForm();
						etiqueta.setId(Long.parseLong(idsEtiquetas[i]));
						listaEtiquetas.add(etiqueta);
					}
				}
			}
			semilla.setEtiquetas(listaEtiquetas);
			if (!StringUtils.isEmpty(semilla.getListaUrlsString())) {
				semilla.setListaUrlsString(this.normalizarUrl(semilla.getListaUrlsString()));
				semilla.setListaUrls(Arrays.asList(semilla.getListaUrlsString().split(";")));
				// Comprobar que sólo se introduzcen el max.url
				String idComplejidad = request.getParameter("complejidadaux");
				ComplejidadForm complexAux = ComplejidadDAO.getComplexityById(DataBaseManager.getConnection(), idComplejidad);
				int maxUrls = complexAux.getAmplitud() * complexAux.getProfundidad() + 1;
//				PropertiesManager pmgr = new PropertiesManager();
//				int maxUrls = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
				if (semilla.getListaUrls() != null && semilla.getListaUrls().size() > maxUrls) {
					errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.max.superado", new String[] { String.valueOf(maxUrls) })));
					response.setStatus(400);
					response.getWriter().write(new Gson().toJson(errores));
					return null;
				}
			}
			// Comprobar duplicados
			Set<String> testDuplicates = this.findDuplicates(semilla.getListaUrls());
			if (!testDuplicates.isEmpty()) {
				String duplicadosStr = "";
				for (String duplicado : testDuplicates) {
					duplicadosStr += duplicado + "\n";
				}
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.duplicados") + duplicadosStr));
				response.setStatus(400);
				response.getWriter().write(new Gson().toJson(errores));
				return null;
			}
			CategoriaForm categoriaSemilla = new CategoriaForm();
			categoriaSemilla.setId(request.getParameter("segmento"));
			semilla.setCategoria(categoriaSemilla);
			AmbitoForm ambitoSemilla = new AmbitoForm();
			ambitoSemilla.setId(request.getParameter("ambitoaux"));
			semilla.setAmbito(ambitoSemilla);
			ComplejidadForm complejidadSemilla = new ComplejidadForm();
			complejidadSemilla.setId(request.getParameter("complejidadaux"));
			semilla.setComplejidad(complejidadSemilla);
			MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
			try (Connection c = DataBaseManager.getConnection()) {
				// Comprobar que no existe una semilla con el mismo nombre
				boolean existSeed = SemillaDAO.existSeed(c, semilla.getNombre(), Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
				if (existSeed && !semilla.getNombre().equals(request.getParameter(Constants.NOMBRE_ANTIGUO))) {
					response.setStatus(400);
					// response.getWriter().write(messageResources.getMessage("mensaje.error.nombre.semilla.duplicado"));
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.error.nombre.semilla.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					SemillaDAO.editSeed(c, semilla);
					// response.getWriter().write(messageResources.getMessage("mensaje.exito.semilla.editada"));
					errores.add(new JsonMessage(messageResources.getMessage("mensaje.exito.semilla.editada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				response.setStatus(400);
				response.getWriter().write(messageResources.getMessage("mensaje.error.generico"));
			}
		}
		return null;
	}

	/**
	 * Guarda la semilla.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<JsonMessage> errores = new ArrayList<>();
		SemillaForm semilla = (SemillaForm) form;
		ActionErrors errors = semilla.validate(mapping, request);
		if (errors != null && !errors.isEmpty()) {
			// Error de validación
			if (errors.get("nombre").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.nombre.requerido")));
			}
			if (errors.get("listaUrlsString").hasNext()) {
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.requerido")));
			}
		}
		if (!errores.isEmpty()) {
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		} else {
			// Soporte a múltiples dependencias
			List<DependenciaForm> listaDependencias = new ArrayList<>();
			String[] dependencias = request.getParameterValues("dependenciasSeleccionadas");
			if (dependencias != null && dependencias.length > 0) {
				for (int i = 0; i < dependencias.length; i++) {
					DependenciaForm dependencia = new DependenciaForm();
					dependencia.setId(Long.parseLong(dependencias[i]));
					listaDependencias.add(dependencia);
				}
			}
			semilla.setDependencias(listaDependencias);
			// Soporte a etiquetas
			List<EtiquetaForm> listaEtiquetas = new ArrayList<>();
			String[] etiquetas = request.getParameterValues("etiquetasSeleccionadas");
			String[] arrayEtiquetas = etiquetas[0].split(",");
			if (etiquetas[0] != "" && arrayEtiquetas != null && arrayEtiquetas.length > 0) {
				for (int i = 0; i < arrayEtiquetas.length; i++) {
					EtiquetaForm etiqueta = new EtiquetaForm();
					etiqueta.setId(Long.parseLong(arrayEtiquetas[i]));
					listaEtiquetas.add(etiqueta);
				}
			}
			semilla.setEtiquetas(listaEtiquetas);
			if (!StringUtils.isEmpty(semilla.getListaUrlsString())) {
				semilla.setListaUrlsString(this.normalizarUrl(semilla.getListaUrlsString()));
				semilla.setListaUrls(Arrays.asList(semilla.getListaUrlsString().split(";")));
				// Comprobar que sólo se introduzcen el max.url
				String idComplejidad = request.getParameter("complejidadaux");
				ComplejidadForm complexAux = ComplejidadDAO.getComplexityById(DataBaseManager.getConnection(), idComplejidad);
				int maxUrls = complexAux.getAmplitud() * complexAux.getProfundidad() + 1;
//				PropertiesManager pmgr = new PropertiesManager();
//				int maxUrls = Integer.parseInt(pmgr.getValue("intav.properties", "max.url"));
				if (semilla.getListaUrls() != null && semilla.getListaUrls().size() > maxUrls) {
					errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.max.superado", new String[] { String.valueOf(maxUrls) })));
					response.setStatus(400);
					response.getWriter().write(new Gson().toJson(errores));
					return null;
				}
			}
			// Comprobar duplicados
			Set<String> testDuplicates = this.findDuplicates(semilla.getListaUrls());
			if (!testDuplicates.isEmpty()) {
				String duplicadosStr = "";
				for (String duplicado : testDuplicates) {
					duplicadosStr += duplicado + "\n";
				}
				errores.add(new JsonMessage(this.messageResources.getMessage("semilla.nueva.url.duplicados") + duplicadosStr));
				response.setStatus(400);
				response.getWriter().write(new Gson().toJson(errores));
				return null;
			}
			CategoriaForm categoriaSemilla = new CategoriaForm();
			categoriaSemilla.setId(request.getParameter("segmento"));
			semilla.setCategoria(categoriaSemilla);
			AmbitoForm ambitoSemilla = new AmbitoForm();
			ambitoSemilla.setId(request.getParameter("ambitoaux"));
			semilla.setAmbito(ambitoSemilla);
			ComplejidadForm complejidadSemilla = new ComplejidadForm();
			complejidadSemilla.setId(request.getParameter("complejidadaux"));
			semilla.setComplejidad(complejidadSemilla);
			try (Connection c = DataBaseManager.getConnection()) {
				if (SemillaDAO.existSeed(c, semilla.getNombre(), Constants.ID_LISTA_SEMILLA_OBSERVATORIO)) {
					response.setStatus(400);
					errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.error.nombre.semilla.duplicado")));
					response.getWriter().write(new Gson().toJson(errores));
				} else {
					SemillaDAO.saveSeedMultidependencia(c, semilla);
					errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.exito.semilla.generada")));
					response.getWriter().write(new Gson().toJson(errores));
				}
			} catch (Exception e) {
				Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.error.generico")));
				response.setStatus(400);
				response.getWriter().write(new Gson().toJson(errores));
			}
		}
		return null;
	}

	/**
	 * Borra una semilla.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<JsonMessage> errores = new ArrayList<>();
		try (Connection c = DataBaseManager.getConnection()) {
			SemillaDAO.deleteSeed(c, Long.parseLong(request.getParameter("idSemilla")));
			errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.exito.semilla.borrada")));
			response.getWriter().write(new Gson().toJson(errores));
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			errores.add(new JsonMessage(this.messageResources.getMessage("mensaje.error.generico")));
			response.setStatus(400);
			response.getWriter().write(new Gson().toJson(errores));
		}
		return null;
	}

	/**
	 * List observatorios semilla delete.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listObservatoriosSemillaDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final String idSemilla = request.getParameter(Constants.SEMILLA);
			final List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
			String jsonObservatorios = new Gson().toJson(observatoryFormList);
			PrintWriter pw = response.getWriter();
			pw.write(jsonObservatorios);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todas las categorias. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listCategorias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	 * Obtiene un listado de todos los ambitos. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listAmbitos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<AmbitoForm> listAmbitos = SemillaDAO.getSeedAmbits(c, Constants.NO_PAGINACION);
			String jsonAmbitos = new Gson().toJson(listAmbitos);
			PrintWriter pw = response.getWriter();
			pw.write(jsonAmbitos);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todos las complejidades. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listComplejidades(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<ComplejidadForm> listComplejidades = SemillaDAO.getSeedComplexities(c, Constants.NO_PAGINACION);
			String jsonComplejidades = new Gson().toJson(listComplejidades);
			PrintWriter pw = response.getWriter();
			pw.write(jsonComplejidades);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Obtiene un listado de todas las dependencias. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listDependencias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	 * Obtiene un listado de todas las etiquetas. La respuesta se genera como un JSON
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward listEtiquetas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			List<EtiquetaForm> listEtiquetas = SemillaDAO.getSeedEtiquetas(c, Constants.NO_PAGINACION);
			String jsonEtiquetas = new Gson().toJson(listEtiquetas);
			PrintWriter pw = response.getWriter();
			pw.write(jsonEtiquetas);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Normalizar url.
	 *
	 * Elimina los espacios y los saltos de línea para generar una cadena que pueda ser convertida en un array con el método split de {@link String}
	 *
	 * @param urlsSemilla the urls semilla Valor original
	 * @return the string Valor normalizado
	 */
	private String normalizarUrl(String urlsSemilla) {
		return urlsSemilla.replace("\r\n", ";").replace("\n", ";").replaceAll("\\s+", "");
	}

	/**
	 * Comprueba duplicados.
	 *
	 * @param urls the urls
	 * @return the sets the
	 */
	private Set<String> findDuplicates(List<String> urls) {
		final Set<String> setDuplicados = new HashSet<>();
		final Set<String> setUnicos = new HashSet<>();
		for (String url : urls) {
			if (!setUnicos.add(url)) {
				setDuplicados.add(url);
			}
		}
		return setDuplicados;
	}

	/**
	 * Find add observatory canidadte seeds.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward candidates(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Search sedds that not included in observatory
		try (Connection c = DataBaseManager.getConnection()) {
			response.setContentType("text/json");
			List<SemillaForm> observatorySeedsList = SemillaDAO.getCandidateSeeds(c, Long.parseLong(request.getParameter("idObservatorio")), Long.parseLong(request.getParameter("idExObs")));
			String jsonSeeds = new Gson().toJson(observatorySeedsList);
			PrintWriter pw = response.getWriter();
			pw.write(jsonSeeds);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			Logger.putLog("Error: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}
}
