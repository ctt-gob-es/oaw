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
package es.inteco.rastreador2.action.basic.service;

import java.text.MessageFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import es.gob.oaw.basicservice.BasicServiceManager;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.dao.basic.service.DiagnosisDAO;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

/**
 * The Class BasicServiceAction.
 */
public class BasicServiceAction extends Action {
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
	@Override
	public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final BasicServiceForm basicServiceFormRequest = BasicServiceUtils.getBasicServiceForm((BasicServiceForm) form, request);
		final ActionErrors errors = validateBasicServiceForm(basicServiceFormRequest, mapping, request);
		if (errors.isEmpty()) {
			final String action = request.getParameter(Constants.ACTION);
			final BasicServiceManager basicServiceManager = new BasicServiceManager();
			if (Constants.EXECUTE.equalsIgnoreCase(action)) {
				final BasicServiceForm basicServiceForm = DiagnosisDAO.getBasicServiceRequestById(DataBaseManager.getConnection(), basicServiceFormRequest.getId());
				if (basicServiceForm.isContentAnalysis()) {
					BasicServiceUtils.getContent(basicServiceForm, basicServiceForm.getFileName(), request.getParameter(Constants.PARAM_CONTENT), true);
				} else if (basicServiceForm.isContentAnalysisMultiple()) {
					BasicServiceUtils.getContent(basicServiceForm, basicServiceForm.getFileName(), basicServiceFormRequest.getContent(), false);
				} else if (basicServiceForm.isAnalysisMix()) {
					if (basicServiceForm.getFileName() != null && (basicServiceForm.getFileName().contains("zip") || basicServiceForm.getFileName().contains("rar")
							|| basicServiceForm.getFileName().contains("tar") || basicServiceForm.getFileName().contains("tar.gz") || basicServiceForm.getFileName().contains("7z"))) {
						BasicServiceUtils.getContent(basicServiceForm, basicServiceForm.getFileName(), basicServiceFormRequest.getContent(), false);
					} else {
						BasicServiceUtils.getContent(basicServiceForm, basicServiceForm.getFileName(), basicServiceFormRequest.getContent(), true);
					}
				}
				basicServiceForm.setAnalysisToDelete(basicServiceFormRequest.getAnalysisToDelete());
				basicServiceManager.executeCrawling(basicServiceForm, CrawlerUtils.getResources(request));
			} else {
				final String serverResponse = basicServiceManager.enqueueCrawling(basicServiceFormRequest);
				CrawlerUtils.returnText(response, serverResponse, false);
			}
		} else {
			final String serverResponse = processValidationErrors(basicServiceFormRequest, errors);
			CrawlerUtils.returnText(response, serverResponse, false);
		}
		return null;
	}

	/**
	 * Validate basic service form.
	 *
	 * @param basicServiceForm the basic service form
	 * @param mapping          the mapping
	 * @param request          the request
	 * @return the action errors
	 */
	private ActionErrors validateBasicServiceForm(final BasicServiceForm basicServiceForm, final ActionMapping mapping, final HttpServletRequest request) {
		final ActionErrors errors = basicServiceForm.validate(mapping, request);
		errors.add(BasicServiceUtils.validateReport(basicServiceForm));
		errors.add(BasicServiceUtils.validateUrlOrContent(basicServiceForm));
		errors.add(BasicServiceUtils.validateUrlLenght(basicServiceForm));
		return errors;
	}

	/**
	 * Process validation errors.
	 *
	 * @param basicServiceForm the basic service form
	 * @param errors           the errors
	 * @return the string
	 */
	private String processValidationErrors(final BasicServiceForm basicServiceForm, final ActionErrors errors) {
		final PropertiesManager pmgr = new PropertiesManager();
		final StringBuilder text = new StringBuilder(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.validation.errors"));
		for (Iterator<ActionMessage> iterator = errors.get(); iterator.hasNext();) {
			final ActionMessage message = iterator.next();
			Logger.putLog(message.getKey(), BasicServiceAction.class, Logger.LOG_LEVEL_ERROR);
			text.append("<br/> - ").append(MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, message.getKey()), message.getValues()));
		}
		basicServiceForm.setId(BasicServiceUtils.saveRequestData(basicServiceForm, Constants.BASIC_SERVICE_STATUS_MISSING_PARAMS));
		return text.toString();
	}
}
