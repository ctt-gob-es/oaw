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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.semillas.SeedCategoriesAction;
import es.inteco.rastreador2.action.semillas.SeedUtils;
import es.inteco.rastreador2.action.semillas.SeedExcelUtils;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaSearchForm;
import es.inteco.rastreador2.dao.ambito.AmbitoDAO;
import es.inteco.rastreador2.dao.categoria.CategoriaDAO;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;

/**
 * The Class SeedMassImportAction.
 * 
 * Action to perfomr export and import seed
 *
 */
public class SeedMassImportAction extends Action {
	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action. ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (CrawlerUtils.hasAccess(request, "delete.observatory")) {
				if ("confirm".equals(request.getParameter(Constants.ACTION))) {
					return confirm(mapping, form, request);
				} else if (Constants.ACCION_IMPORT_ALL.equals(request.getParameter(Constants.ACTION))) {
					if (Constants.CONF_SI.equals(request.getParameter(Constants.CONFIRMACION))) {
						return saveAllSeedsFile(mapping, form, request);
					} else {
						return mapping.findForward(Constants.VOLVER);
					}
				} else if (Constants.ACCION_EXPORT_ALL.equals(request.getParameter(Constants.ACTION))) {
					// return getAllSeedsFile(request, response);
					return getSeedsFile(request, response, form);
				} else {
					return mapping.findForward(Constants.VOLVER);
				}
			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			CrawlerUtils.warnAdministrators(e, this.getClass());
			return mapping.findForward(Constants.ERROR_PAGE);
		}
	}

	/**
	 * Confirmar la acción.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward confirm(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {

		if (isCancelled(request)) {
			return mapping.findForward(Constants.VOLVER);
		}
		final PropertiesManager pmgr = new PropertiesManager();
		SemillaSearchForm semillaSearchForm = (SemillaSearchForm) form;
		request.setAttribute(Constants.ACTION, Constants.ADD_SEED_CATEGORY);

		// Validate form
		ActionErrors errors = semillaSearchForm.validate(mapping, request);
		if (	!errors.isEmpty() ||
				semillaSearchForm.getFileSeeds() == null ||
				StringUtils.isEmpty(semillaSearchForm.getFileSeeds().getFileName())){
			saveErrors(request, errors);
			return mapping.findForward(Constants.VOLVER);
		}

		// Validate format
		String filename = semillaSearchForm.getFileSeeds().getFileName();
		String[] splits = filename.split("\\.");
		String fileExtension = splits[splits.length-1];
		if (!fileExtension.equals("xml") && !fileExtension.equals("xlsx")) {
			errors.add("xmlFile", new ActionMessage("no.xml.file"));
			saveErrors(request, errors);
			return mapping.findForward(Constants.VOLVER);
		}

		// validate file size
		if (semillaSearchForm.getFileSeeds().getFileSize() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size"))) {
			errors.add("xmlFile", new ActionMessage("xml.size.error"));
			saveErrors(request, errors);
			return mapping.findForward(Constants.VOLVER);
		}

		try (Connection c = DataBaseManager.getConnection()) {
			if (semillaSearchForm.getFileSeeds().getFileData().length > 0) {
				try {
					List<SemillaForm> inalterableSeeds = new ArrayList<>();
					List<SeedComparision> updatedSeeds = new ArrayList<>();
					List<SemillaForm> newSeed = new ArrayList<>();
					List<SeedError> errorSeeds = new ArrayList<>();
					List<SemillaForm> updateAndNewSeeds = new ArrayList<>();
					List<SemillaForm> seeds = new ArrayList<>();
					switch (fileExtension){
						case "xml":
							seeds = SeedUtils.getSeedsFromFile(semillaSearchForm.getFileSeeds().getInputStream(), true);
							break;
						case "xlsx":
							seeds = SeedExcelUtils.getSeedsFromXlsxFile(semillaSearchForm.getFileSeeds().getInputStream());
							break;
					}

					// Check for empty file
					if (seeds == null || seeds.isEmpty()) {
						errors.add("xmlFile", new ActionMessage("xml.seed.not.valid"));
						saveErrors(request, errors);
						return mapping.findForward(Constants.VOLVER);
					}

					for (SemillaForm seed : seeds) {
						// Categories retrieve from database
						if (seed.getCategoria() != null && !org.apache.commons.lang3.StringUtils.isEmpty(seed.getCategoria().getName())) {
							CategoriaForm category = CategoriaDAO.getCategoryByName(c, seed.getCategoria().getName());
							if (category != null) {
								seed.setCategoria(category);
							} else {
								seed.getCategoria().setOrden(1);
								Long idCategoria = SemillaDAO.createSeedCategory(c, seed.getCategoria());
								seed.getCategoria().setId(idCategoria.toString());
							}
						}
						// Ambits retrieve from database
						if (seed.getAmbito() != null && !org.apache.commons.lang3.StringUtils.isEmpty(seed.getAmbito().getName())) {
							AmbitoForm ambit = AmbitoDAO.getAmbitByName(c, seed.getAmbito().getName());
							if (ambit != null) {
								seed.setAmbito(ambit);
							} else {
								seed.getAmbito().setOrden(1);
								Long idAmbito = SemillaDAO.createSeedAmbit(c, seed.getAmbito());
								seed.getAmbito().setId(idAmbito.toString());
							}
						}
						// Complexities retrieve from database
						if (seed.getComplejidad() != null && !org.apache.commons.lang3.StringUtils.isEmpty(seed.getComplejidad().getName())) {
							ComplejidadForm complexity = ComplejidadDAO.getComplexityByName(c, seed.getComplejidad().getName());
							if (complexity != null) {
								seed.setComplejidad(complexity);
							} else {
								seed.getComplejidad().setOrden(1);
								Long idComplejidad = SemillaDAO.createSeedComplexity(c, seed.getComplejidad());
								seed.getComplejidad().setId(idComplejidad.toString());
							}
						}
						// TODO Check errors
						List<String> errorsSeed = new ArrayList<>();
						MessageResources messageResources = MessageResources.getMessageResources("ApplicationResources");
						// TODO Duplicate name error
						Long id = SemillaDAO.existOtherSeed(c, seed.getNombre(), Constants.ID_LISTA_SEMILLA_OBSERVATORIO);
						if ((id != null && seed.getId() == null) || (id != null && seed.getId() != null && !id.equals(seed.getId()))) {
							errorsSeed.add(messageResources.getMessage("mensaje.error.nombre.semilla.duplicado"));
						}
						// TODO Max url error
						List<String> xmlUrls = Arrays.asList(seed.getListaUrlsString().split(";"));
						if (!StringUtils.isEmpty(seed.getListaUrlsString()) && seed.getComplejidad() != null) {
							ComplejidadForm complexAux = ComplejidadDAO.getComplexityById(c, seed.getComplejidad().getId());
							int maxUrls = complexAux.getAmplitud() * complexAux.getProfundidad() + 1;
							if (xmlUrls.size() > maxUrls) {
								errorsSeed.add(messageResources.getMessage("semilla.nueva.url.max.superado", new String[] { String.valueOf(maxUrls) }));
							}
						}

						// Comprobar duplicados
						Set<String> testDuplicates = this.findDuplicates(xmlUrls);
						if (!testDuplicates.isEmpty()) {
							String duplicadosStr = "";
							for (String duplicado : testDuplicates) {
								duplicadosStr += duplicado + "\n";
							}
							errorsSeed.add(messageResources.getMessage("semilla.nueva.url.duplicados") + duplicadosStr);
						}
						// TODO Has errores
						if (errorsSeed.size() > 0) {
							SeedError seedError = new SeedError();
							BeanUtils.copyProperties(seedError, seed);
							seedError.setErrors(errorsSeed);
							errorSeeds.add(seedError);
						} else if (seed.getId() != null) {
							SemillaForm seedOld = SemillaDAO.getSeedById(c, seed.getId());
							if (seedOld != null && seedOld.getId() != null) {
								SeedComparision seedComparision = generateComparisionSeed(seedOld, seed);
								if (!seedComparision.isSame()) {
									updatedSeeds.add(seedComparision);
									updateAndNewSeeds.add(seed);
								} else {
									inalterableSeeds.add(seed);
								}
							} else {
								seed.setId(null);
								newSeed.add(seed);
								updateAndNewSeeds.add(seed);
							}
						} else {
							newSeed.add(seed);
							updateAndNewSeeds.add(seed);
						}
					}
					request.setAttribute("errorSeeds", errorSeeds);
					request.setAttribute("inalterableSeeds", inalterableSeeds);
					request.setAttribute("updatedSeeds", updatedSeeds);
					request.setAttribute("newSeedList", newSeed);
					// Store list in session
					HttpSession session = request.getSession();
					// session.setAttribute(Constants.OBSERVATORY_SEED_LIST, seeds);
					session.setAttribute(Constants.OBSERVATORY_SEED_LIST, updateAndNewSeeds);
					session.setAttribute("errorSeeds", errorSeeds);

				} catch (Exception e) {
					Logger.putLog("Error en la creación de semillas asociadas al observatorio", SeedCategoriesAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			return mapping.findForward(Constants.CONFIRMACION_IMPORTAR);
		}
	}

	/**
	 * Gets the all seeds file.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param form     the form
	 * @return the all seeds file
	 * @throws Exception the exception
	 */
	private ActionForward getSeedsFile(HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			SemillaSearchForm searchForm = (SemillaSearchForm) form;
			if (searchForm != null) {
				searchForm = new SemillaSearchForm();
				searchForm.setNombre(request.getParameter("nombre"));
				if (!StringUtils.isEmpty(searchForm.getNombre())) {
					searchForm.setNombre(es.inteco.common.utils.StringUtils.corregirEncoding(searchForm.getNombre()));
				}
				if (!StringUtils.isEmpty(request.getParameter("categoria"))) {
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
			response.setContentType("text/json");
			List<SemillaForm> seeds = SemillaDAO.getObservatorySeedsToExport(c, searchForm);
			SeedExcelUtils.writeSeedsToXlsxResponse(response, seeds);
		} catch (Exception e) {
			Logger.putLog("ERROR al intentar exportar semillas: ", JsonSemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}

	/**
	 * Compose filter from request.
	 *
	 * @param request the request
	 * @return the servicio diagnostico form
	 */
	private SemillaSearchForm composeFilterFromRequest(HttpServletRequest request) {
		SemillaSearchForm search = new SemillaSearchForm();
		if (!StringUtils.isEmpty(request.getParameter("nombre"))) {
			search.setNombre(request.getParameter("nombre"));
		}
		if (!StringUtils.isEmpty(request.getParameter("categoria"))) {
			// search.setCategoria(request.getParameter("categoria"));
		}
		if (!StringUtils.isEmpty(request.getParameter("url"))) {
			search.setUrl(request.getParameter("url"));
		}
		return search;
	}

	/**
	 * Save all seeds file.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward saveAllSeedsFile(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		List<SemillaForm> seeds = (List<SemillaForm>) session.getAttribute(Constants.OBSERVATORY_SEED_LIST);
		ActionErrors errors = form.validate(mapping, request);
		try (Connection c = DataBaseManager.getConnection()) {
			SemillaDAO.saveOrUpdateSeed(c, seeds);
			session.removeAttribute(Constants.OBSERVATORY_SEED_LIST);
			errors.add("xmlFile", new ActionMessage("no.xml.file"));
			ActionMessages messages = new ActionMessages();
			List<SemillaForm> errorSeeds = (List<SemillaForm>) session.getAttribute("errorSeeds");
			if (errorSeeds != null && !errorSeeds.isEmpty()) {
				if (seeds.isEmpty()) {
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("xml.import.success.all.errors"));
				} else {
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("xml.import.success.with.errors"));
				}
			} else {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("xml.import.success"));
			}
			saveMessages(session, messages);
			return mapping.findForward("observatorySeed");
		} catch (Exception e) {
			errors.add("xmlFile", new ActionMessage("no.xml.file"));
			saveErrors(request, errors);
			return mapping.findForward("observatorySeed");
		}
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
		// escape ";" thats used as split in several places
		return urlsSemilla.replace(";", "%3B").replace("\r\n", ";").replace("\n", ";").replaceAll("\\s+", "");
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
	 * Compare seed fileds.
	 *
	 * @param seed1 the seed 1
	 * @param seed2 the seed 2
	 * @return the list
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public SeedComparision generateComparisionSeed(SemillaForm seed1, SemillaForm seed2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SeedComparision seedComparision = new SeedComparision();
		seedComparision.setNombre(seed1.getNombre());
		seedComparision.setNombreNuevo(seed2.getNombre());
		seedComparision.setUrls(seed1.getListaUrls());
		seedComparision.setUrlsNuevo(seed2.getListaUrls());
		seedComparision.setAcronimo(seed1.getAcronimo());
		seedComparision.setAcronimoNuevo(seed2.getAcronimo());
		seedComparision.setActiva(seed1.isActiva());
		seedComparision.setActivaNuevo(seed2.isActiva());
		seedComparision.setCategoria(seed1.getCategoria() != null ? seed1.getCategoria() : new CategoriaForm());
		seedComparision.setCategoriaNuevo(seed2.getCategoria() != null ? seed2.getCategoria() : new CategoriaForm());
		seedComparision.setInDirectory(seed1.isInDirectory());
		seedComparision.setInDirectoryNuevo(seed2.isInDirectory());
		seedComparision.setDependencias(seed1.getDependencias());
		seedComparision.setDependenciasNuevo(seed2.getDependencias());
		seedComparision.setAmbito(seed1.getAmbito());
		seedComparision.setAmbitoNuevo(seed2.getAmbito());
		seedComparision.setComplejidad(seed1.getComplejidad());
		seedComparision.setComplejidadNuevo(seed2.getComplejidad());
		seedComparision.setEtiquetas(seed1.getEtiquetas());
		seedComparision.setEtiquetasNuevo(seed2.getEtiquetas());
		seedComparision.setEliminada(seed1.isEliminar());
		seedComparision.setEliminadaNuevo(seed2.isEliminar());
		seedComparision.setObservaciones(seed1.getObservaciones());
		seedComparision.setObservacionesNuevo(seed2.getObservaciones());
		return seedComparision;
	}

	/**
	 * The Class SeedError.
	 */
	public class SeedError extends SemillaForm {
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 3659271546896392454L;
		/** The errors. */
		private List<String> errors = new ArrayList<>();

		/**
		 * Gets the errors.
		 *
		 * @return the error
		 */
		public List<String> getErrors() {
			return errors;
		}

		/**
		 * Sets the errors.
		 *
		 * @param errors the new errors
		 */
		public void setErrors(List<String> errors) {
			this.errors = errors;
		}
	}

	/**
	 * The Class ResultObject.
	 */
	public class SeedComparision {
		/** The nombre. */
		private String nombre;
		/** The urls. */
		private List<String> urls;
		/** The acronimo. */
		private String acronimo;
		/** The activa. */
		private boolean activa;
		/** The eliminada. */
		private boolean eliminada;
		/** The eliminada nuevo. */
		private boolean eliminadaNuevo;
		/** The categoria. */
		private CategoriaForm categoria;
		/** The in directory. */
		private boolean inDirectory;
		/** The dependencias. */
		private List<DependenciaForm> dependencias;
		/** The nombre N. */
		private String nombreNuevo;
		/** The urls N. */
		private List<String> urlsNuevo;
		/** The acronimo N. */
		private String acronimoNuevo;
		/** The activa N. */
		private boolean activaNuevo;
		/** The categoria N. */
		private CategoriaForm categoriaNuevo;
		/** The in directory N. */
		private boolean inDirectoryNuevo;
		/** The dependencias N. */
		private List<DependenciaForm> dependenciasNuevo;
		/** The categoria. */
		private AmbitoForm ambito;
		/** The ambito nuevo. */
		private AmbitoForm ambitoNuevo;
		/** The categoria. */
		private ComplejidadForm complejidad;
		/** The ambito nuevo. */
		private ComplejidadForm complejidadNuevo;
		/** The observaciones. */
		private String observaciones;
		/** The observaciones nuevo. */
		private String observacionesNuevo;

		/**
		 * Gets the observaciones.
		 *
		 * @return the observaciones
		 */
		public String getObservaciones() {
			return observaciones;
		}

		/**
		 * Sets the observaciones.
		 *
		 * @param observaciones the observaciones to set
		 */
		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}

		/**
		 * Gets the observaciones nuevo.
		 *
		 * @return the observacionesNuevo
		 */
		public String getObservacionesNuevo() {
			return observacionesNuevo;
		}

		/**
		 * Sets the observaciones nuevo.
		 *
		 * @param observacionesNuevo the observacionesNuevo to set
		 */
		public void setObservacionesNuevo(String observacionesNuevo) {
			this.observacionesNuevo = observacionesNuevo;
		}

		/**
		 * Gets the complejidad.
		 *
		 * @return the complejidad
		 */
		public ComplejidadForm getComplejidad() {
			return complejidad;
		}

		/**
		 * Sets the complejidad.
		 *
		 * @param complejidad the new complejidad
		 */
		public void setComplejidad(ComplejidadForm complejidad) {
			this.complejidad = complejidad;
		}

		/**
		 * Gets the complejidad nuevo.
		 *
		 * @return the complejidad nuevo
		 */
		public ComplejidadForm getComplejidadNuevo() {
			return complejidadNuevo;
		}

		/**
		 * Sets the complejidad nuevo.
		 *
		 * @param complejidadNuevo the new complejidad nuevo
		 */
		public void setComplejidadNuevo(ComplejidadForm complejidadNuevo) {
			this.complejidadNuevo = complejidadNuevo;
		}

		/**
		 * Gets the etiquetas.
		 *
		 * @return the etiquetas
		 */
		public List<EtiquetaForm> getEtiquetas() {
			return etiquetas;
		}

		/**
		 * Sets the etiquetas.
		 *
		 * @param etiquetas the new etiquetas
		 */
		public void setEtiquetas(List<EtiquetaForm> etiquetas) {
			this.etiquetas = etiquetas;
		}

		/**
		 * Gets the etiquetas nuevo.
		 *
		 * @return the etiquetas nuevo
		 */
		public List<EtiquetaForm> getEtiquetasNuevo() {
			return etiquetasNuevo;
		}

		/**
		 * Sets the etiquetas nuevo.
		 *
		 * @param etiquetasNuevo the new etiquetas nuevo
		 */
		public void setEtiquetasNuevo(List<EtiquetaForm> etiquetasNuevo) {
			this.etiquetasNuevo = etiquetasNuevo;
		}

		/** The etiquetas. */
		private List<EtiquetaForm> etiquetas;
		/** The etiquetas nuevo. */
		private List<EtiquetaForm> etiquetasNuevo;

		/**
		 * Gets the nombre.
		 *
		 * @return the nombre
		 */
		public String getNombre() {
			return nombre;
		}

		/**
		 * Sets the nombre.
		 *
		 * @param nombre the new nombre
		 */
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		/**
		 * Gets the urls.
		 *
		 * @return the urls
		 */
		public List<String> getUrls() {
			return urls;
		}

		/**
		 * Sets the urls.
		 *
		 * @param urls the new urls
		 */
		public void setUrls(List<String> urls) {
			this.urls = urls;
		}

		/**
		 * Gets the acronimo.
		 *
		 * @return the acronimo
		 */
		public String getAcronimo() {
			return acronimo;
		}

		/**
		 * Sets the acronimo.
		 *
		 * @param acronimo the new acronimo
		 */
		public void setAcronimo(String acronimo) {
			this.acronimo = acronimo;
		}

		/**
		 * Checks if is activa.
		 *
		 * @return true, if is activa
		 */
		public boolean isActiva() {
			return activa;
		}

		/**
		 * Sets the activa.
		 *
		 * @param activa the new activa
		 */
		public void setActiva(boolean activa) {
			this.activa = activa;
		}

		/**
		 * Gets the categoria.
		 *
		 * @return the categoria
		 */
		public CategoriaForm getCategoria() {
			return categoria;
		}

		/**
		 * Sets the categoria.
		 *
		 * @param categoria the new categoria
		 */
		public void setCategoria(CategoriaForm categoria) {
			this.categoria = categoria;
		}

		/**
		 * Checks if is in directory.
		 *
		 * @return true, if is in directory
		 */
		public boolean isInDirectory() {
			return inDirectory;
		}

		/**
		 * Sets the in directory.
		 *
		 * @param inDirectory the new in directory
		 */
		public void setInDirectory(boolean inDirectory) {
			this.inDirectory = inDirectory;
		}

		/**
		 * Gets the dependencias.
		 *
		 * @return the dependencias
		 */
		public List<DependenciaForm> getDependencias() {
			return dependencias;
		}

		/**
		 * Sets the dependencias.
		 *
		 * @param dependencias the new dependencias
		 */
		public void setDependencias(List<DependenciaForm> dependencias) {
			this.dependencias = dependencias;
		}

		/**
		 * Gets the nombre nuevo.
		 *
		 * @return the nombre nuevo
		 */
		public String getNombreNuevo() {
			return nombreNuevo;
		}

		/**
		 * Sets the nombre nuevo.
		 *
		 * @param nombreNuevo the new nombre nuevo
		 */
		public void setNombreNuevo(String nombreNuevo) {
			this.nombreNuevo = nombreNuevo;
		}

		/**
		 * Gets the urls nuevo.
		 *
		 * @return the urls nuevo
		 */
		public List<String> getUrlsNuevo() {
			return urlsNuevo;
		}

		/**
		 * Sets the urls nuevo.
		 *
		 * @param urlsNuevo the new urls nuevo
		 */
		public void setUrlsNuevo(List<String> urlsNuevo) {
			this.urlsNuevo = urlsNuevo;
		}

		/**
		 * Gets the acronimo nuevo.
		 *
		 * @return the acronimo nuevo
		 */
		public String getAcronimoNuevo() {
			return acronimoNuevo;
		}

		/**
		 * Sets the acronimo nuevo.
		 *
		 * @param acronimoNuevo the new acronimo nuevo
		 */
		public void setAcronimoNuevo(String acronimoNuevo) {
			this.acronimoNuevo = acronimoNuevo;
		}

		/**
		 * Checks if is activa nuevo.
		 *
		 * @return true, if is activa nuevo
		 */
		public boolean isActivaNuevo() {
			return activaNuevo;
		}

		/**
		 * Sets the activa nuevo.
		 *
		 * @param activaNuevo the new activa nuevo
		 */
		public void setActivaNuevo(boolean activaNuevo) {
			this.activaNuevo = activaNuevo;
		}

		/**
		 * Gets the categoria nuevo.
		 *
		 * @return the categoria nuevo
		 */
		public CategoriaForm getCategoriaNuevo() {
			return categoriaNuevo;
		}

		/**
		 * Sets the categoria nuevo.
		 *
		 * @param categoriaNuevo the new categoria nuevo
		 */
		public void setCategoriaNuevo(CategoriaForm categoriaNuevo) {
			this.categoriaNuevo = categoriaNuevo;
		}

		/**
		 * Checks if is in directory nuevo.
		 *
		 * @return true, if is in directory nuevo
		 */
		public boolean isInDirectoryNuevo() {
			return inDirectoryNuevo;
		}

		/**
		 * Sets the in directory nuevo.
		 *
		 * @param inDirectoryNuevo the new in directory nuevo
		 */
		public void setInDirectoryNuevo(boolean inDirectoryNuevo) {
			this.inDirectoryNuevo = inDirectoryNuevo;
		}

		/**
		 * Gets the dependencias nuevo.
		 *
		 * @return the dependencias nuevo
		 */
		public List<DependenciaForm> getDependenciasNuevo() {
			return dependenciasNuevo;
		}

		/**
		 * Sets the dependencias nuevo.
		 *
		 * @param dependenciasNuevo the new dependencias nuevo
		 */
		public void setDependenciasNuevo(List<DependenciaForm> dependenciasNuevo) {
			this.dependenciasNuevo = dependenciasNuevo;
		}

		/** The same nombre. */
		private boolean sameNombre;
		/** The same acronimo. */
		private boolean sameAcronimo;
		/** The same categoria. */
		private boolean sameCategoria;
		/** The same activa. */
		private boolean sameActiva;
		/** The same in directory. */
		private boolean sameInDirectory;
		/** The same dependencias. */
		private boolean sameDependencias;
		/** The same lista UR ls. */
		private boolean sameListaURLs;
		/** The same ambito. */
		private boolean sameAmbito;
		/** The same etiquetas. */
		private boolean sameEtiquetas;
		/** The same complejidad. */
		private boolean sameComplejidad;
		/** The same observaciones. */
		private boolean sameObservaciones;

		/**
		 * Checks if is eliminada.
		 *
		 * @return true, if is eliminada
		 */
		public boolean isEliminada() {
			return eliminada;
		}

		/**
		 * Sets the eliminada.
		 *
		 * @param eliminada the new eliminada
		 */
		public void setEliminada(boolean eliminada) {
			this.eliminada = eliminada;
		}

		/**
		 * Checks if is eliminada nuevo.
		 *
		 * @return true, if is eliminada nuevo
		 */
		public boolean isEliminadaNuevo() {
			return eliminadaNuevo;
		}

		/**
		 * Sets the eliminada nuevo.
		 *
		 * @param eliminadaNuevo the new eliminada nuevo
		 */
		public void setEliminadaNuevo(boolean eliminadaNuevo) {
			this.eliminadaNuevo = eliminadaNuevo;
		}

		/**
		 * Sets the same complejidad.
		 *
		 * @param sameComplejidad the new same complejidad
		 */
		public void setSameComplejidad(boolean sameComplejidad) {
			this.sameComplejidad = sameComplejidad;
		}

		/**
		 * Sets the same ambito.
		 *
		 * @param sameAmbito the new same ambito
		 */
		public void setSameAmbito(boolean sameAmbito) {
			this.sameAmbito = sameAmbito;
		}

		/**
		 * Sets the same etiquetas.
		 *
		 * @param sameEtiquetas the new same etiquetas
		 */
		public void setSameEtiquetas(boolean sameEtiquetas) {
			this.sameEtiquetas = sameEtiquetas;
		}

		/**
		 * Sets the same lista UR ls.
		 *
		 * @param sameListaURLs the new same lista UR ls
		 */
		public void setSameListaURLs(boolean sameListaURLs) {
			this.sameListaURLs = sameListaURLs;
		}

		/**
		 * Sets the same nombre.
		 *
		 * @param sameNombre the new same nombre
		 */
		public void setSameNombre(boolean sameNombre) {
			this.sameNombre = sameNombre;
		}

		/**
		 * Sets the same acronimo.
		 *
		 * @param sameAcronimo the new same acronimo
		 */
		public void setSameAcronimo(boolean sameAcronimo) {
			this.sameAcronimo = sameAcronimo;
		}

		/**
		 * Sets the same categoria.
		 *
		 * @param sameCategoria the new same categoria
		 */
		public void setSameCategoria(boolean sameCategoria) {
			this.sameCategoria = sameCategoria;
		}

		/**
		 * Sets the same activa.
		 *
		 * @param sameActiva the new same activa
		 */
		public void setSameActiva(boolean sameActiva) {
			this.sameActiva = sameActiva;
		}

		/**
		 * Sets the same in directory.
		 *
		 * @param sameInDirectory the new same in directory
		 */
		public void setSameInDirectory(boolean sameInDirectory) {
			this.sameInDirectory = sameInDirectory;
		}

		/**
		 * Sets the same dependencias.
		 *
		 * @param sameDependencias the new same dependencias
		 */
		public void setSameDependencias(boolean sameDependencias) {
			this.sameDependencias = sameDependencias;
		}

		/**
		 * Checks if is same nombre.
		 *
		 * @return true, if is same nombre
		 */
		public boolean isSameNombre() {
			if (org.apache.commons.lang3.StringUtils.isEmpty(nombre) && org.apache.commons.lang3.StringUtils.isEmpty(nombreNuevo)) {
				return true;
			}
			return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(nombre, nombreNuevo);
		}

		/**
		 * Checks if is same observaciones.
		 *
		 * @return true, if is same observaciones
		 */
		public boolean isSameObservaciones() {
			if (org.apache.commons.lang3.StringUtils.isEmpty(observaciones) && org.apache.commons.lang3.StringUtils.isEmpty(observacionesNuevo)) {
				return true;
			}
			return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(observaciones, observacionesNuevo);
		}

		/**
		 * Checks if is same acronimo.
		 *
		 * @return true, if is same acronimo
		 */
		public boolean isSameAcronimo() {
			if (org.apache.commons.lang3.StringUtils.isEmpty(acronimo) && org.apache.commons.lang3.StringUtils.isEmpty(acronimoNuevo)) {
				return true;
			}
			return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(acronimo, acronimoNuevo);
		}

		/**
		 * Checks if is same categoria.
		 *
		 * @return true, if is same categoria
		 */
		public boolean isSameCategoria() {
			if (this.categoria != null && this.categoriaNuevo != null) {
				if (org.apache.commons.lang3.StringUtils.isEmpty(categoria.getName()) && org.apache.commons.lang3.StringUtils.isEmpty(categoriaNuevo.getName())) {
					return true;
				}
				return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(categoria.getName(), categoriaNuevo.getName());
			} else if (this.categoria == null && this.categoriaNuevo == null) {
				return true;
			}
			return false;
		}

		/**
		 * Checks if is same ambito.
		 *
		 * @return true, if is same ambito
		 */
		public boolean isSameAmbito() {
			if (this.ambito != null && this.ambitoNuevo != null) {
				if (org.apache.commons.lang3.StringUtils.isEmpty(ambito.getName()) && org.apache.commons.lang3.StringUtils.isEmpty(ambitoNuevo.getName())) {
					return true;
				}
				return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(ambito.getName(), ambitoNuevo.getName());
			} else if (this.ambito == null && this.ambitoNuevo == null) {
				return true;
			}
			return false;
		}

		/**
		 * Checks if is same complejidad.
		 *
		 * @return true, if is same complejidad
		 */
		public boolean isSameComplejidad() {
			if (this.complejidad != null && this.complejidadNuevo != null) {
				if (org.apache.commons.lang3.StringUtils.isEmpty(complejidad.getName()) && org.apache.commons.lang3.StringUtils.isEmpty(complejidadNuevo.getName())) {
					return true;
				}
				return org.apache.commons.lang3.StringUtils.equalsIgnoreCase(complejidad.getName(), complejidadNuevo.getName());
			} else if (this.complejidad == null && this.complejidadNuevo == null) {
				return true;
			}
			return false;
		}

		/**
		 * Gets the ambito.
		 *
		 * @return the ambito
		 */
		public AmbitoForm getAmbito() {
			return ambito;
		}

		/**
		 * Sets the ambito.
		 *
		 * @param ambito the new ambito
		 */
		public void setAmbito(AmbitoForm ambito) {
			this.ambito = ambito;
		}

		/**
		 * Gets the ambito nuevo.
		 *
		 * @return the ambito nuevo
		 */
		public AmbitoForm getAmbitoNuevo() {
			return ambitoNuevo;
		}

		/**
		 * Sets the ambito nuevo.
		 *
		 * @param ambitoNuevo the new ambito nuevo
		 */
		public void setAmbitoNuevo(AmbitoForm ambitoNuevo) {
			this.ambitoNuevo = ambitoNuevo;
		}

		/**
		 * Checks if is same activa.
		 *
		 * @return true, if is same activa
		 */
		public boolean isSameActiva() {
			return activa == activaNuevo;
		}

		/**
		 * Checks if is same eliminada.
		 *
		 * @return true, if is same eliminada
		 */
		public boolean isSameEliminada() {
			return eliminada == eliminadaNuevo;
		}

		/**
		 * Checks if is same in directory.
		 *
		 * @return true, if is same in directory
		 */
		public boolean isSameInDirectory() {
			return inDirectory == inDirectoryNuevo;
		}

		/**
		 * Checks if is same dependencias.
		 *
		 * @return true, if is same dependencias
		 */
		public boolean isSameDependencias() {
			return new HashSet<>(dependencias).equals(new HashSet<>(dependenciasNuevo));
		}

		/**
		 * Checks if is same etiquetas.
		 *
		 * @return true, if is same etiquetas
		 */
		public boolean isSameEtiquetas() {
			return new HashSet<>(etiquetas).equals(new HashSet<>(etiquetasNuevo));
		}

		/**
		 * Checks if is same lista UR ls.
		 *
		 * @return true, if is same lista UR ls
		 */
		public boolean isSameListaURLs() {
			return new HashSet<>(urls).equals(new HashSet<>(urlsNuevo));
		}

		/**
		 * Checks if is same.
		 *
		 * @return true, if is same
		 */
		public boolean isSame() {
			return this.isSameAcronimo() && this.isSameActiva() && this.isSameCategoria() && this.isSameDependencias() && this.isSameInDirectory() && this.isSameListaURLs() && this.isSameNombre()
					&& this.isSameAmbito() && this.isSameEtiquetas() && this.isSameComplejidad() && this.isSameEliminada() && this.isSameObservaciones();
		}
	}
}
