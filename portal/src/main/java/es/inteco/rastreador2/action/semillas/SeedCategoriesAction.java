/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.action.semillas;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.ResultadosObservatorioAction;
import es.inteco.rastreador2.action.observatorio.SemillasObservatorioAction;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.InsertarRastreoForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.Pagination;

/**
 * The Class SeedCategoriesAction.
 */
public class SeedCategoriesAction extends Action {
	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// Marcamos el menú
		request.getSession().setAttribute(Constants.MENU, Constants.MENU_INTECO_OBS);
		request.getSession().setAttribute(Constants.SUBMENU, Constants.SUBMENU_CATEGORIES);
		try {
			if (CrawlerUtils.hasAccess(request, "categories.seed")) {
				String action = request.getParameter(Constants.ACTION);
				if (action.equals(Constants.SEED_CATEGORIES)) {
					return getSeedCategories(mapping, request);
				} else if (action.equals(Constants.DELETE_CATEGORY_CONFIRMATION)) {
					return deleteCategoryConfirmation(mapping, request);
				} else if (action.equals(Constants.DELETE_CATEGORY)) {
					return deleteCategory(mapping, request);
				} else if (action.equals(Constants.NEW_SEED_CATEGORY)) {
					return newSeedCategory(mapping, request);
				} else if (action.equals(Constants.ADD_SEED_CATEGORY)) {
					return addSeedCategory(mapping, form, request);
				} else if (action.equals(Constants.EDIT_SEED_CATEGORY)) {
					return editSeedCategory(mapping, request);
				} else if (action.equals(Constants.UPDATE_SEED_CATEGORY)) {
					return updateSeedCategory(mapping, form, request);
				} else if (action.equals(Constants.VIEW_SEED_CATEGORY)) {
					return viewSeedCategory(mapping, form, request);
				} else if (action.equals(Constants.DELETE_CATEGORY_SEED_CONFIRMATION)) {
					return deleteSeedConfirmation(mapping, request);
				} else if (action.equals(Constants.DELETE_CATEGORY_SEED)) {
					return deleteSeed(mapping, request);
				} else if (action.equals(Constants.NEW_CATEGORY_SEED)) {
					return newCategorySeed(mapping, request);
				} else if (action.equals(Constants.EDIT_CATEGORY_SEED)) {
					return editCategorySeed(mapping, request);
				} else if (action.equals(Constants.ADD_CATEGORY_SEED)) {
					return addCategorySeed(mapping, form, request);
				} else if (action.equals(Constants.UPDATE_CATEGORY_SEED)) {
					return updateCategorySeed(mapping, form, request);
				} else if (action.equals(Constants.GET_CATEGORY_SEEDS_FILE)) {
					return getCategorySeedsFile(request, response);
				}
			} else {
				return mapping.findForward(Constants.NO_PERMISSION);
			}
		} catch (Exception e) {
			CrawlerUtils.warnAdministrators(e, SeedCategoriesAction.class);
			return mapping.findForward(Constants.ERROR_PAGE);
		}
		return null;
	}

	/**
	 * Gets the seed categories.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the seed categories
	 * @throws Exception the exception
	 */
	private ActionForward getSeedCategories(ActionMapping mapping, HttpServletRequest request) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			int numResult = SemillaDAO.countSeedCategories(c);
			int page = Pagination.getPage(request, Constants.PAG_PARAM);
			request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, (page - 1)));
			request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, page));
		}
		return mapping.findForward(Constants.SEED_CATEGORIES);
	}

	/**
	 * Delete category.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward deleteCategory(ActionMapping mapping, HttpServletRequest request) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
			SemillaDAO.deleteSeedCategory(c, idCategory);
		}
		ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.categoria.semilla.eliminada", "volver.listado.categorias.semilla");
		return mapping.findForward(Constants.EXITO);
	}

	/**
	 * Delete category confirmation.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward deleteCategoryConfirmation(ActionMapping mapping, HttpServletRequest request) throws Exception {
		final String idCategoria = request.getParameter(Constants.ID_CATEGORIA);
		try (Connection c = DataBaseManager.getConnection()) {
			List<ObservatorioForm> observatoryFormList = new ArrayList<>();
			if (idCategoria != null) {
				observatoryFormList = ObservatorioDAO.getObservatoriesFromCategory(c, idCategoria);
			}
			request.setAttribute(Constants.OBSERVATORY_SEED_LIST, observatoryFormList);
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return mapping.findForward(Constants.DELETE_CATEGORY_CONFIRMATION);
	}

	/**
	 * New seed category.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward newSeedCategory(ActionMapping mapping, HttpServletRequest request) throws Exception {
		request.setAttribute(Constants.ACTION, Constants.ADD_SEED_CATEGORY);
		return mapping.findForward(Constants.SEED_CATEGORY_FORM);
	}

	/**
	 * Edits the seed category.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward editSeedCategory(ActionMapping mapping, HttpServletRequest request) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			if (request.getParameter(Constants.ID_CATEGORIA) != null) {
				Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
				CategoriaForm categoriaForm = SemillaDAO.getSeedCategory(c, idCategory);
				int numResult = SemillaDAO.countSeedsByCategory(c, idCategory, new SemillaForm());
				int page = Pagination.getPage(request, Constants.PAG_PARAM);
				categoriaForm.setSeeds(SemillaDAO.getSeedsByCategory(c, idCategory, page - 1, new SemillaForm()));
				request.setAttribute(Constants.CATEGORIA_FORM, categoriaForm);
				request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, page));
			}
		}
		request.setAttribute(Constants.ACTION, Constants.UPDATE_SEED_CATEGORY);
		return mapping.findForward(Constants.SEED_CATEGORY_FORM);
	}

	/**
	 * Adds the seed category.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward addSeedCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
		if (!isCancelled(request)) {
			final PropertiesManager pmgr = new PropertiesManager();
			CategoriaForm categoriaForm = (CategoriaForm) form;
			request.setAttribute(Constants.ACTION, Constants.ADD_SEED_CATEGORY);
			ActionErrors errors = categoriaForm.validate(mapping, request);
			if (errors.isEmpty()) {
				if (categoriaForm.getFileSeeds() == null || StringUtils.isEmpty(categoriaForm.getFileSeeds().getFileName()) || (categoriaForm.getFileSeeds().getFileName().endsWith(".xml")
						&& categoriaForm.getFileSeeds().getFileSize() <= Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size")))) {
					try (Connection c = DataBaseManager.getConnection()) {
						final Long idSeedCategory = SemillaDAO.createSeedCategory(c, categoriaForm);
						String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada", categoriaForm.getName());
						String volver = pmgr.getValue("returnPaths.properties", "volver.listado.categorias.semilla");
						if (categoriaForm.getFileSeeds().getFileData().length > 0) {
							try {
								List<SemillaForm> seeds = SeedUtils.getSeedsFromFile(categoriaForm.getFileSeeds().getInputStream(), false);
								SemillaDAO.saveSeedsCategory(c, seeds, idSeedCategory.toString());
							} catch (Exception e) {
								Logger.putLog("Error en la creación de semillas asociadas al observatorio", SeedCategoriesAction.class, Logger.LOG_LEVEL_ERROR, e);
								mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada.error.fichero.semillas", categoriaForm.getName());
							}
						}
						request.setAttribute("mensajeExito", mensaje);
						request.setAttribute("accionVolver", volver);
						return mapping.findForward(Constants.EXITO);
					}
				} else if (!categoriaForm.getFileSeeds().getFileName().endsWith(".xml")) {
					errors.add("xmlFile", new ActionMessage("no.xml.file"));
					saveErrors(request, errors);
					return mapping.findForward(Constants.VOLVER);
				} else if (categoriaForm.getFileSeeds().getFileSize() > Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "xml.file.max.size"))) {
					errors.add("xmlFile", new ActionMessage("xml.size.error"));
					saveErrors(request, errors);
					return mapping.findForward(Constants.VOLVER);
				}
			} else {
				saveErrors(request, errors);
				return mapping.findForward(Constants.VOLVER);
			}
		} else {
			return mapping.findForward(Constants.GET_SEED_CATEGORIES);
		}
		return null;
	}

	/**
	 * Update seed category.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward updateSeedCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
		if (!isCancelled(request)) {
			final CategoriaForm categoriaForm = (CategoriaForm) form;
			final PropertiesManager pmgr = new PropertiesManager();
			try (Connection c = DataBaseManager.getConnection()) {
				SemillaDAO.updateSeedCategory(c, categoriaForm);
				String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.editada", categoriaForm.getName());
				final String volver = pmgr.getValue("returnPaths.properties", "volver.listado.categorias.semilla");
				if (categoriaForm.getFileSeeds().getFileData().length > 0) {
					try {
						// Semillas que recuperamos del fichero
						final List<SemillaForm> seeds = SeedUtils.getSeedsFromFile(categoriaForm.getFileSeeds().getInputStream(), false);
						// Semillas de la categoria
						final List<SemillaForm> oldSeeds = SemillaDAO.getSeedsByCategory(c, Long.parseLong(categoriaForm.getId()), Constants.NO_PAGINACION, new SemillaForm());
						// Comparamos las semillas (x url)
						compareSeeds(c, categoriaForm.getId(), seeds, oldSeeds);
					} catch (Exception e) {
						Logger.putLog("Error en la creación de semillas asociadas al observatorio", SeedCategoriesAction.class, Logger.LOG_LEVEL_ERROR, e);
						mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada.error.fichero.semillas", categoriaForm.getName());
					}
				}
				request.setAttribute("mensajeExito", mensaje);
				request.setAttribute("accionVolver", volver);
				return mapping.findForward(Constants.EXITO);
			}
		} else {
			return mapping.findForward(Constants.GET_SEED_CATEGORIES);
		}
	}

	/**
	 * Compare seeds.
	 *
	 * @param c          the c
	 * @param idCategory the id category
	 * @param newSeeds   the new seeds
	 * @param oldList    the old list
	 * @throws Exception the exception
	 */
	private static void compareSeeds(final Connection c, final String idCategory, final List<SemillaForm> newSeeds, final List<SemillaForm> oldList) throws Exception {
		final List<SemillaForm> repitSeeds = new ArrayList<>();
		final List<SemillaForm> insertSeeds = new ArrayList<>();
		for (SemillaForm newSemillaForm : newSeeds) {
			boolean found = false;
			for (SemillaForm oldSemillaForm : oldList) {
				if (newSemillaForm.getListaUrlsString() != null && oldSemillaForm.getListaUrlsString() != null
						&& newSemillaForm.getListaUrlsString().equalsIgnoreCase(oldSemillaForm.getListaUrlsString())) {
					newSemillaForm.setId(oldSemillaForm.getId());
					repitSeeds.add(newSemillaForm);
					oldList.remove(oldSemillaForm);
					found = true;
					break;
				}
			}
			if (!found) {
				insertSeeds.add(newSemillaForm);
			}
		}
		final List<ObservatorioForm> observatoryFormList = ObservatorioDAO.getObservatoriesFromCategory(c, idCategory);
		// Añadimos las nuevas Semillas y los nuevos rastreos a los observatorios que
		// correspondan
		insertSeedsFromXml(c, insertSeeds, idCategory, observatoryFormList);
		// Modificamos las que han cambiado
		updateSeedsFromXml(c, repitSeeds, observatoryFormList);
		// Borramos las semillas que no tenemos y los rastreos asociados con resultados
		// y demás
		deleteSeedsFromXml(c, oldList);
	}

	/**
	 * Insert seeds from xml.
	 *
	 * @param c                   the c
	 * @param insertSeeds         the insert seeds
	 * @param idCategory          the id category
	 * @param observatoryFormList the observatory form list
	 * @throws Exception the exception
	 */
	private static void insertSeedsFromXml(final Connection c, final List<SemillaForm> insertSeeds, final String idCategory, final List<ObservatorioForm> observatoryFormList) throws Exception {
		if (insertSeeds != null && !insertSeeds.isEmpty()) {
			SemillaDAO.saveSeedsCategory(c, insertSeeds, idCategory);
			for (SemillaForm semillaForm : insertSeeds) {
				for (ObservatorioForm observatorioForm : observatoryFormList) {
					if (observatorioForm.getCategoria() != null && Arrays.asList(observatorioForm.getCategoria()).contains(idCategory)) {
						final InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
						ObservatorioDAO.putDataToInsert(insertarRastreoForm, observatorioForm);
						insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + semillaForm.getNombre());
						insertarRastreoForm.setId_semilla(SemillaDAO.getIdList(c, semillaForm.getNombre(), Long.parseLong(idCategory)));
						insertarRastreoForm.setId_observatorio(observatorioForm.getId());
						insertarRastreoForm.setActive(true);
						RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
					}
				}
			}
		}
	}

	/**
	 * Update seeds from xml.
	 *
	 * @param c                   the c
	 * @param repitSeeds          the repit seeds
	 * @param observatoryFormList the observatory form list
	 * @throws Exception the exception
	 */
	private static void updateSeedsFromXml(final Connection c, final List<SemillaForm> repitSeeds, final List<ObservatorioForm> observatoryFormList) throws Exception {
		if (repitSeeds != null && !repitSeeds.isEmpty()) {
			SemillaDAO.updateCategorySeeds(c, repitSeeds);
			for (SemillaForm semillaForm : repitSeeds) {
				for (ObservatorioForm observatorioForm : observatoryFormList) {
					final Long crawlerId = RastreoDAO.getCrawlerFromSeedAndObservatory(c, semillaForm.getId(), observatorioForm.getId());
					RastreoDAO.updateCrawlerName(c, observatorioForm.getNombre() + "-" + semillaForm.getNombre(), crawlerId);
				}
			}
		}
	}

	/**
	 * Delete seeds from xml.
	 *
	 * @param c       the c
	 * @param oldList the old list
	 * @throws Exception the exception
	 */
	private static void deleteSeedsFromXml(final Connection c, final List<SemillaForm> oldList) throws Exception {
		if (oldList != null && !oldList.isEmpty()) {
			for (SemillaForm semillaForm : oldList) {
				// Al borrar la semilla ya se eliminan automáticamente los rastreos asociados y
				// los resultados
				SemillaDAO.deleteCategorySeed(c, String.valueOf(semillaForm.getId()));
			}
		}
	}

	/**
	 * View seed category.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward viewSeedCategory(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
		final SemillaForm searchForm = (SemillaForm) form;
		try (Connection c = DataBaseManager.getConnection()) {
			if (request.getParameter(Constants.ID_CATEGORIA) != null) {
				final Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
				final CategoriaForm categoriaForm = SemillaDAO.getSeedCategory(c, idCategory);
				final int numResult = SemillaDAO.countSeedsByCategory(c, idCategory, searchForm);
				final int page = Pagination.getPage(request, Constants.PAG_PARAM);
				categoriaForm.setSeeds(SemillaDAO.getSeedsByCategory(c, idCategory, page - 1, searchForm));
				request.setAttribute(Constants.CATEGORIA_FORM, categoriaForm);
				request.setAttribute(Constants.LIST_PAGE_LINKS, Pagination.createPagination(request, numResult, page));
			}
		}
		return mapping.findForward(Constants.VIEW_SEED_CATEGORY);
	}

	/**
	 * Delete seed confirmation.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward deleteSeedConfirmation(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		final String idSemilla = request.getParameter(Constants.ID_SEMILLA);
		try (Connection c = DataBaseManager.getConnection()) {
			final List<ObservatorioForm> observatoryFormList;
			if (idSemilla != null) {
				observatoryFormList = ObservatorioDAO.getObservatoriesFromSeed(c, idSemilla);
			} else {
				observatoryFormList = new ArrayList<>();
			}
			request.setAttribute(Constants.OBSERVATORY_SEED_LIST, observatoryFormList);
			final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSemilla));
			request.setAttribute(Constants.OBSERVATORY_SEED_FORM, semillaForm);
		} catch (Exception e) {
			Logger.putLog("Error: ", SemillasObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return mapping.findForward(Constants.DELETE_CATEGORY_SEED_CONFIRMATION);
	}

	/**
	 * Delete seed.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward deleteSeed(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		try (Connection c = DataBaseManager.getConnection()) {
			final String idCategory = request.getParameter(Constants.ID_CATEGORIA);
			final String idSemilla = request.getParameter(Constants.ID_SEMILLA);
			SemillaDAO.deleteCategorySeed(c, idSemilla);
			final String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.semilla.borrada");
			final String volver = pmgr.getValue("returnPaths.properties", "volver.editar.categoria.semilla").replace("{0}", idCategory);
			request.setAttribute("mensajeExito", mensaje);
			request.setAttribute("accionVolver", volver);
			return mapping.findForward(Constants.EXITO);
		} catch (Exception e) {
			Logger.putLog("Error: ", ResultadosObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * New category seed.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward newCategorySeed(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		if (isCancelled(request)) {
			return mapping.findForward(Constants.EDIT_SEED_CATEGORY);
		}
		request.setAttribute(Constants.ACTION, Constants.ADD_CATEGORY_SEED);
		return mapping.findForward(Constants.CATEGORY_SEED_FORM);
	}

	/**
	 * Edits the category seed.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward editCategorySeed(final ActionMapping mapping, final HttpServletRequest request) throws Exception {
		if (isCancelled(request)) {
			return mapping.findForward(Constants.EDIT_SEED_CATEGORY);
		}
		try (Connection c = DataBaseManager.getConnection()) {
			final String idSeed = request.getParameter(Constants.ID_SEMILLA);
			final SemillaForm semillaForm = SemillaDAO.getSeedById(c, Long.parseLong(idSeed));
			semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace(";", "\r\n"));
			request.setAttribute(Constants.SEMILLA_FORM, semillaForm);
			request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
			request.setAttribute(Constants.ACTION, Constants.UPDATE_CATEGORY_SEED);
			return mapping.findForward(Constants.CATEGORY_SEED_FORM);
		}
	}

	/**
	 * Adds the category seed.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward addCategorySeed(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
		final SemillaForm semillaForm = (SemillaForm) form;
		if (!isCancelled(request)) {
			try (Connection c = DataBaseManager.getConnection()) {
				semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace("\r\n", ";"));
				final Long idSeed = SemillaDAO.insertList(c, 4, semillaForm.getNombre(), semillaForm.getListaUrlsString(), semillaForm.getCategoria().getId(), null, null, null,
						semillaForm.getAcronimo());
				final List<ObservatorioForm> observatoryIds = ObservatorioDAO.getObservatoriesFromCategory(c, semillaForm.getCategoria().getId());
				for (ObservatorioForm observatorioForm : observatoryIds) {
					if (observatorioForm.getCategoria() != null && Arrays.asList(observatorioForm.getCategoria()).contains(semillaForm.getCategoria().getId())) {
						final InsertarRastreoForm insertarRastreoForm = new InsertarRastreoForm();
						ObservatorioDAO.putDataToInsert(insertarRastreoForm, observatorioForm);
						insertarRastreoForm.setCodigo(observatorioForm.getNombre() + "-" + semillaForm.getNombre());
						insertarRastreoForm.setId_semilla(idSeed);
						insertarRastreoForm.setId_observatorio(observatorioForm.getId());
						insertarRastreoForm.setActive(true);
						RastreoDAO.insertarRastreo(c, insertarRastreoForm, true);
					}
				}
			}
			final PropertiesManager pmgr = new PropertiesManager();
			final String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.creada.semilla");
			final String volver = pmgr.getValue("returnPaths.properties", "volver.editar.categoria.semilla").replace("{0}", semillaForm.getCategoria().getId());
			request.setAttribute("mensajeExito", mensaje);
			request.setAttribute("accionVolver", volver);
			return mapping.findForward(Constants.EXITO);
		} else {
			final ActionForward forward = new ActionForward(mapping.findForward(Constants.EDIT_SEED_CATEGORY));
			forward.setPath(forward.getPath() + "&" + Constants.ID_CATEGORIA + "=" + semillaForm.getCategoria().getId());
			forward.setRedirect(true);
			return forward;
		}
	}

	/**
	 * Update category seed.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward updateCategorySeed(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request) throws Exception {
		final SemillaForm semillaForm = (SemillaForm) form;
		if (!isCancelled(request)) {
			try (Connection c = DataBaseManager.getConnection()) {
				semillaForm.setListaUrlsString(semillaForm.getListaUrlsString().replace("\r\n", ";"));
				SemillaDAO.editSeed(c, semillaForm);
			}
			final PropertiesManager pmgr = new PropertiesManager();
			final String mensaje = getResources(request).getMessage(getLocale(request), "mensaje.exito.categoria.semilla.editada.semilla");
			final String volver = pmgr.getValue("returnPaths.properties", "volver.editar.categoria.semilla").replace("{0}", semillaForm.getCategoria().getId());
			request.setAttribute("mensajeExito", mensaje);
			request.setAttribute("accionVolver", volver);
			return mapping.findForward(Constants.EXITO);
		} else {
			final ActionForward forward = new ActionForward(mapping.findForward(Constants.EDIT_SEED_CATEGORY));
			forward.setPath(forward.getPath() + "&" + Constants.ID_CATEGORIA + "=" + semillaForm.getCategoria().getId());
			forward.setRedirect(true);
			return forward;
		}
	}

	/**
	 * Gets the category seeds file.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the category seeds file
	 * @throws Exception the exception
	 */
	private ActionForward getCategorySeedsFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			final Long idCategory = Long.parseLong(request.getParameter(Constants.ID_CATEGORIA));
			final List<SemillaForm> seeds = SemillaDAO.getSeedsByCategory(c, idCategory, Constants.NO_PAGINACION, new SemillaForm());
			SeedUtils.writeFileToResponse(response, seeds, false);
			return null;
		}
	}
}
