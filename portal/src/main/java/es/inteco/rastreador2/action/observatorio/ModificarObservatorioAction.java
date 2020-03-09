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
package es.inteco.rastreador2.action.observatorio;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ModificarObservatorioForm;
import es.inteco.rastreador2.actionform.rastreo.LenguajeForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cuentausuario.CuentaUsuarioDAO;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.servlets.ScheduleObservatoryServlet;
import es.inteco.rastreador2.utils.ActionUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.DAOUtils;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ModificarObservatorioAction.
 */
public class ModificarObservatorioAction extends Action {
//    private Log log = LogFactory.getLog(ModificarObservatorioAction.class);
	/**
	 * Execute.
	 *
	 * @param mapping  the mapping
	 * @param form     the form
	 * @param request  the request
	 * @param response the response
	 * @return the action forward
	 * @throws Exception the exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			if (CrawlerUtils.hasAccess(request, "edit.observatory")) {
				String esPrimera = request.getParameter(Constants.ES_PRIMERA);
				String idObservatorio = request.getParameter(Constants.ID_OBSERVATORIO);
				if (isCancelled(request)) {
					return (mapping.findForward(Constants.VOLVER_CARGA));
				} else {
					List<SemillaForm> otherObservSeedList = (List<SemillaForm>) request.getSession().getAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST);
					List<SemillaForm> addSeeds = (List<SemillaForm>) request.getSession().getAttribute(Constants.ADD_OBSERVATORY_SEED_LIST);
					if ((esPrimera == null || esPrimera.trim().equals("")) && (request.getParameter(Constants.PAG_PARAM) == null) && (request.getParameter(Constants.PAG_PARAM2) == null)) {
						ModificarObservatorioForm modificarObservatorioForm = new ModificarObservatorioForm();
						modificarObservatorioForm = loadData(modificarObservatorioForm, request, true, idObservatorio);
						ObservatoryUtils.removeSessionAttributes(request);
						ObservatoryUtils.putSessionAttributes(request, modificarObservatorioForm);
						return ObservatoryUtils.returnLists(request, mapping, modificarObservatorioForm.getSemillasAnadidas(), modificarObservatorioForm.getSemillasNoAnadidas(), false);
					} else {
						request.setAttribute(Constants.IS_UPDATE, Constants.CONF_SI);
						if ((request.getParameter(Constants.PAG_PARAM) != null) || (request.getParameter(Constants.PAG_PARAM2) != null)) {
							ModificarObservatorioForm modificarObservatorioForm = new ModificarObservatorioForm();
							modificarObservatorioForm = loadData(modificarObservatorioForm, request, false, idObservatorio);
							if ((request.getParameter(Constants.ACTION) == null) || !(request.getParameter(Constants.ACTION)).equals(Constants.ACCION_ACEPTAR)) {
								return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObservSeedList, true);
							} else {
								return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObservSeedList, false);
							}
						} else {
							ModificarObservatorioForm modificarObservatorioForm = (ModificarObservatorioForm) form;
							modificarObservatorioForm = loadData(modificarObservatorioForm, request, false, idObservatorio);
							if ((modificarObservatorioForm.getButtonAction() != null)
									&& (modificarObservatorioForm.getButtonAction().equals(getResources(request).getMessage(getLocale(request), "boton.aceptar.anadir.semilla")))) {
								modificarObservatorioForm.setButtonAction(null);
								return ObservatoryUtils.returnLists(request, mapping, addSeeds, otherObservSeedList, true);
							} else if ((modificarObservatorioForm.getButtonAction() != null)
									&& (modificarObservatorioForm.getButtonAction().equals(getResources(request).getMessage(getLocale(request), "boton.aceptar")))) {
								modificarObservatorioForm = (ModificarObservatorioForm) request.getSession().getAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM);
								return editObservatory(mapping, modificarObservatorioForm, request);
							} else {
								final String idSeed = request.getParameter(Constants.SEMILLA);
								final String action = request.getParameter(Constants.ACTION);
								if ((action != null) && (action.equals(Constants.ACCION_SEPARATE_SEED))) {
									return ObservatoryUtils.separeSeedToObservatory(request, mapping, idSeed, true, addSeeds, otherObservSeedList);
								} else if ((action != null) && (action.equals(Constants.ACCION_ADD_SEED))) {
									return ObservatoryUtils.addSeedToObservatory(request, mapping, idSeed, true, addSeeds, otherObservSeedList);
								} else if ((action != null) && (action.equals(Constants.ACCION_ACEPTAR))) {
									return addListToObservatory(mapping, request);
								}
							}
						}
					}
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

	/**
	 * Adds the list to observatory.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 * @return the action forward
	 */
	private ActionForward addListToObservatory(ActionMapping mapping, HttpServletRequest request) {
		ModificarObservatorioForm modificarObservatorioForm = (ModificarObservatorioForm) request.getSession().getAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM);
		modificarObservatorioForm.setSemillasAnadidas((List<SemillaForm>) request.getSession().getAttribute(Constants.ADD_OBSERVATORY_SEED_LIST));
		modificarObservatorioForm.setSemillasNoAnadidas((List<SemillaForm>) request.getSession().getAttribute(Constants.OTHER_OBSERVATORY_SEED_LIST));
		request.getSession().setAttribute(Constants.MODIFICAR_OBSERVATORIO_FORM, modificarObservatorioForm);
		return ObservatoryUtils.returnLists(request, mapping, modificarObservatorioForm.getSemillasAnadidas(), modificarObservatorioForm.getSemillasNoAnadidas(), false);
	}

	/**
	 * Load data.
	 *
	 * @param modificarObservatorioForm the modificar observatorio form
	 * @param request                   the request
	 * @param esPrimera                 the es primera
	 * @param idObservatorio            the id observatorio
	 * @return the modificar observatorio form
	 * @throws Exception the exception
	 */
	private ModificarObservatorioForm loadData(ModificarObservatorioForm modificarObservatorioForm, HttpServletRequest request, boolean esPrimera, String idObservatorio) throws Exception {
		try (Connection c = DataBaseManager.getConnection()) {
			if (idObservatorio != null) {
				modificarObservatorioForm.setId_observatorio(idObservatorio);
			}
			modificarObservatorioForm.setNormaV(DAOUtils.getNormas(c, false));
			modificarObservatorioForm.setPeriodicidadVector(DAOUtils.getRecurrence(c));
			List<LenguajeForm> lenguajeFormList = DAOUtils.getLenguaje(c);
			modificarObservatorioForm.setLenguajeVector(new ArrayList<LenguajeForm>());
			for (LenguajeForm lenguajeForm : lenguajeFormList) {
				lenguajeForm.setName(getResources(request).getMessage(getLocale(request), lenguajeForm.getKeyName()));
				modificarObservatorioForm.getLenguajeVector().add(lenguajeForm);
			}
			request.setAttribute(Constants.MINUTES, CrawlerUtils.getMinutes());
			request.setAttribute(Constants.HOURS, CrawlerUtils.getHours());
			request.setAttribute(Constants.SEED_CATEGORIES, SemillaDAO.getSeedCategories(c, Constants.NO_PAGINACION));
			request.setAttribute(Constants.CARTUCHOS_VECTOR, LoginDAO.getAllUserCartridge(c));
			request.setAttribute(Constants.TIPOS_VECTOR, ObservatorioDAO.getAllObservatoryTypes(c));
			request.setAttribute(Constants.AMBITOS_VECTOR, ObservatorioDAO.getAllAmbitos(c));
			if (esPrimera) {
				ObservatorioDAO.getObservatoryDataToUpdate(c, modificarObservatorioForm);
			}
			modificarObservatorioForm.setNombre_antiguo(modificarObservatorioForm.getNombre());
			return modificarObservatorioForm;
		} catch (Exception e) {
			Logger.putLog("Excepción genérica al modificar observatorio", ModificarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw new Exception(e);
		}
	}

	/**
	 * Edits the observatory.
	 *
	 * @param mapping the mapping
	 * @param form    the form
	 * @param request the request
	 * @return the action forward
	 * @throws Exception the exception
	 */
	private ActionForward editObservatory(ActionMapping mapping, ActionForm form, HttpServletRequest request) throws Exception {
		final ModificarObservatorioForm modificarObservatorioForm = (ModificarObservatorioForm) form;
		modificarObservatorioForm.setNombre_antiguo(request.getParameter(Constants.NOMBRE_ANTIGUO));
		final String idObservatorio = request.getParameter(Constants.ID_OBSERVATORIO);
		request.setAttribute(Constants.ID_OBSERVATORIO, idObservatorio);
		final ActionErrors errors = modificarObservatorioForm.validate(mapping, request);
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			return mapping.findForward(Constants.VOLVER);
		} else {
			try (Connection c = DataBaseManager.getConnection()) {
				// Comprobamos que el nombre usa caracteres correctos
				if (modificarObservatorioForm.getNombre() != null && !modificarObservatorioForm.getNombre().trim().equals(modificarObservatorioForm.getNombre_antiguo())) {
					if (CuentaUsuarioDAO.existAccount(c, modificarObservatorioForm.getNombre())) {
						Logger.putLog("Usuario Duplicado", ModificarObservatorioAction.class, Logger.LOG_LEVEL_INFO);
						errors.add("usuarioDuplicado", new ActionMessage("usuario.duplicado"));
						saveErrors(request, errors);
						return mapping.findForward(Constants.USUARIO_DUPLICADO);
					}
				}
				modificarObservatorioForm.setId_observatorio(idObservatorio);
				ObservatorioDAO.updateObservatory(c, modificarObservatorioForm);
				// Cambiamos los rastreos programados
				ScheduleObservatoryServlet.deleteJob(Long.parseLong(modificarObservatorioForm.getId_observatorio()));
				if (modificarObservatorioForm.isActivo()) {
					ScheduleObservatoryServlet.scheduleJob(modificarObservatorioForm.getNombre(), Long.parseLong(modificarObservatorioForm.getId_observatorio()), modificarObservatorioForm.getFecha(),
							RastreoDAO.getRecurrence(c, Long.parseLong(modificarObservatorioForm.getPeriodicidad())), modificarObservatorioForm.getCartucho().getId());
				}
			} catch (Exception e) {
				Logger.putLog("Exception al modificar observatorio", ModificarObservatorioAction.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
		ActionUtils.setSuccesActionAttributes(request, "mensaje.exito.observatorio.editado", "volver.carga.observatorio");
		return mapping.findForward(Constants.EXITO);
	}
}