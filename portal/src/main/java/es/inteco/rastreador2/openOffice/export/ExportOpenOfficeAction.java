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
package es.inteco.rastreador2.openOffice.export;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.utils.FileUtils;

/**
 * The Class ExportOpenOfficeAction.
 */
public class ExportOpenOfficeAction extends Action {
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
		long idExecution = 0;
		if (request.getParameter(Constants.ID) != null) {
			idExecution = Long.parseLong(request.getParameter(Constants.ID));
		}
		long idObservatory = 0;
		if (request.getParameter(Constants.ID_OBSERVATORIO) != null) {
			idObservatory = Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO));
		}
		long idCartucho = 0;
		if (request.getParameter(Constants.ID_CARTUCHO) != null) {
			idCartucho = Long.parseLong(request.getParameter(Constants.ID_CARTUCHO));
		}
		// TODO Get application
		String application;
		try {
			application = CartuchoDAO.getApplication(DataBaseManager.getConnection(), idCartucho);
			if (Constants.NORMATIVA_UNE_EN2019.equals(application)) {
				request.setAttribute(Constants.ID_OBSERVATORIO, request.getParameter(Constants.ID_OBSERVATORIO));
				request.setAttribute(Constants.FULFILLED_OBSERVATORIES, ObservatorioDAO.getFulfilledObservatories(DataBaseManager.getConnection(), idObservatory, -1, null));
				request.setAttribute(Constants.ID_CARTUCHO, idCartucho);
				return mapping.findForward(Constants.CONFIGURAR_FILTROS_AGREGADOS);
			} else {
				return generateReportWithoutFilters(mapping, request, response, idExecution, idObservatory);
			}
		} catch (SQLException e1) {
			return generateReportWithoutFilters(mapping, request, response, idExecution, idObservatory);
		} catch (Exception e1) {
			return generateReportWithoutFilters(mapping, request, response, idExecution, idObservatory);
		}
	}

	/**
	 * Generate report with filters.
	 *
	 * @param mapping       the mapping
	 * @param request       the request
	 * @param response      the response
	 * @param idExecution   the id execution
	 * @param idObservatory the id observatory
	 * @return the action forward
	 */
	private ActionForward generateReportWithoutFilters(final ActionMapping mapping, final HttpServletRequest request, final HttpServletResponse response, long idExecution, long idObservatory) {
		final PropertiesManager pmgr = new PropertiesManager();
		final String basePath = pmgr.getValue(CRAWLER_PROPERTIES, "export.open.office") + idObservatory + File.separator + idExecution + File.separator;
		String filePath = null;
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			final SimpleDateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
			final ObservatorioRealizadoForm observatoryFFForm = ObservatorioDAO.getFulfilledObservatory(c, idObservatory, idExecution);
			filePath = basePath + PDFUtils.formatSeedName(observatoryForm.getNombre()) + ".odt";
			final String graphicPath = basePath + "temp" + File.separator;
			final int numObs = ObservatorioDAO.getFulfilledObservatories(c, Long.parseLong(request.getParameter(Constants.ID_OBSERVATORIO)), Constants.NO_PAGINACION, observatoryFFForm.getFecha())
					.size();
			ExportOpenOfficeUtils.createOpenOfficeDocument(request, filePath, graphicPath, df.format(observatoryFFForm.getFecha()), observatoryForm.getTipo(), numObs);
			FileUtils.deleteDir(new File(graphicPath));
		} catch (Exception e) {
			Logger.putLog("Error al exportar a pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			return mapping.findForward(Constants.ERROR_PAGE);
		}
		try {
			CrawlerUtils.returnFile(response, filePath, "application/vnd.oasis.opendocument.text", false);
		} catch (Exception e) {
			Logger.putLog("Exception al devolver el PDF", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;
	}
}
