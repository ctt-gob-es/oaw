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
package es.gob.oaw.basicservice;

import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;

import java.text.MessageFormat;
import java.util.Collections;

/**
 * Clase para el envío de correos desde el servicio de diagnóstico. Utiliza a su
 * vez el servicio MailService
 */
public class BasicServiceMailService {

	private final PropertiesManager pmgr;
	private final MailService mailService;

	public BasicServiceMailService() {
		pmgr = new PropertiesManager();
		mailService = new MailService();
	}

	public void sendBasicServiceReport(final BasicServiceForm basicServiceForm, final String attachUrl, final String attachName) {
		mailService.sendMail(Collections.singletonList(basicServiceForm.getEmail()), getMailSubject(basicServiceForm.getReport()), getMailBody(basicServiceForm), attachUrl, attachName);
	}

	public void sendBasicServiceErrorMessage(final BasicServiceForm basicServiceForm, final String message) {
		final String subject = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.subject");
		mailService.sendMail(Collections.singletonList(basicServiceForm.getEmail()), subject, message);
	}

	private String getMailSubject(final String reportType) {
		final String message = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.subject");
		if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType) || Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, "Observatorio UNE 2004");
		} else if (Constants.REPORT_OBSERVATORY_2.equals(reportType) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, "Observatorio UNE 2012");
		} else if (Constants.REPORT_OBSERVATORY_3.equals(reportType) || Constants.REPORT_OBSERVATORY_3_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, "Observatorio UNE 2012 Beta");
		} else if ("une".equals(reportType)) {
			return MessageFormat.format(message, "UNE 139803");
		} else {
			return "Informe de Accesibilidad Web";
		}
	}

	private String getMailBody(final BasicServiceForm basicServiceForm) {
		final String text;
		if (basicServiceForm.isContentAnalysis()) {
			text = MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory.content"), basicServiceForm.getUser(),
					reportToString(basicServiceForm.getReport()));
		} else {
			final String inDirectory = basicServiceForm.isInDirectory() ? pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes")
					: pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.no");
			text = MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory"), basicServiceForm.getUser(), basicServiceForm.getDomain(),
					basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), inDirectory, reportToString(basicServiceForm.getReport()));
		}

		return text;
	}

	private String reportToString(final String reportType) {
		if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType)) {
			return "Observatorio UNE 2004";
		} else if (Constants.REPORT_OBSERVATORY_2.equals(reportType)) {
			return "Observatorio UNE 2012";
		} else if (Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
			return "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
		} else if (Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
			return "Observatorio UNE 2012 (sin comprobar enlaces rotos)";
		} else if (Constants.REPORT_OBSERVATORY_3.equals(reportType)) { 
			return "Observatorio UNE 2012 Beta";
		} else if (Constants.REPORT_OBSERVATORY_3_NOBROKEN.equals(reportType)) {
			return "Observatorio UNE 2012 Beta (sin comprobar enlaces rotos)";
		} else {
			return reportType;
		}
	}

}
