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

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.action.observatorio.utils.RelanzarObservatorioThread;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;

/**
 * RelanzarObservatorioAction. Action para relanzar un observatorio incompleto.
 */
public class RelanzarObservatorioAction extends Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.
	 * ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		try {
			if (CrawlerUtils.hasAccess(request, "delete.observatory")) {
				if ("confirm".equals(request.getParameter(Constants.ACTION))) {
					return confirm(mapping, request);
				} else if ("relanzar".equals(request.getParameter(Constants.ACTION))) {
					if (request.getParameter(Constants.CONFIRMACION).equals(Constants.CONF_SI)) {
						return lanzarRastreosPendientes(mapping, request);
					} else {
						return mapping.findForward(Constants.VOLVER);
					}
				} else {
					return mapping.findForward(Constants.NO_PERMISSION);
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
	 * @param mapping
	 *            the mapping
	 * @param request
	 *            the request
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	private ActionForward confirm(ActionMapping mapping, HttpServletRequest request) throws Exception {
		final Long idObservatory = Long.valueOf(request.getParameter(Constants.ID_OBSERVATORIO));
		try (Connection c = DataBaseManager.getConnection()) {
			ObservatorioForm observatorioForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			request.setAttribute(Constants.OBSERVATORY_FORM, observatorioForm);
			request.setAttribute(Constants.ID_EX_OBS, request.getParameter(Constants.ID_EX_OBS));
		}

		return mapping.findForward(Constants.CONFIRMACION_RELANZAR);
	}

	/**
	 * Lanzar rastreos pendientes.
	 *
	 * @param mapping
	 *            the mapping
	 * @param request
	 *            the request
	 * @return the action forward
	 * @throws Exception
	 *             the exception
	 */
	private ActionForward lanzarRastreosPendientes(ActionMapping mapping, HttpServletRequest request) throws Exception {

		String idObservatorio = request.getParameter(Constants.ID_OBSERVATORIO);
		String idEjecucionObservatorio = request.getParameter(Constants.ID_EX_OBS);
		// Lanzar en un hilo nuevo

		new RelanzarObservatorioThread(idObservatorio, idEjecucionObservatorio).start();

		final PropertiesManager pmgr = new PropertiesManager();
		request.setAttribute("mensajeExito", getResources(request).getMessage("mensaje.exito.relanzar.observatorio"));
		request.setAttribute("accionVolver", pmgr.getValue("returnPaths.properties", "volver.carga.observatorio"));

		return mapping.findForward(Constants.EXITO);
	}

}
