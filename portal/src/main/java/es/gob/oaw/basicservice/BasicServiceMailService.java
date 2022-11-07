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

import java.sql.Connection;
import java.text.MessageFormat;
import java.util.Collections;

import es.gob.oaw.MailException;
import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.actionform.semillas.ComplejidadForm;
import es.inteco.rastreador2.actionform.semillas.ProxyForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.dao.proxy.ProxyDAO;
import es.inteco.utils.CrawlerUtils;

/**
 * Clase para el envío de correos desde el servicio de diagnóstico. Utiliza a su vez el servicio MailService
 */
public class BasicServiceMailService {
	/** The Constant OBSERVATORIO_UNE_2012_VERSIÓN_2_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_UNE_2012_VERSION_2_SIN_ENLACES_ROTOS = "Observatorio UNE 2012 (versión 2 sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS = "Observatorio UNE 2012 (antigua sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS = "Observatorio UNE 2004 (sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_ACCESIBILIDAD. */
	private static final String OBSERVATORIO_ACCESIBILIDAD = "Comprobación Declaración de Accesibilidad (beta)";
	/** The Constant OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS. */
	private static final String OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS = "Comprobación Declaración de Accesibilidad (beta)";
	/** The Constant OBSERVATORIO_UNE_EN2019. */
//	private static final String OBSERVATORIO_UNE_EN2019 = "Observatorio UNE EN2019";
	private static final String OBSERVATORIO_UNE_EN2019 = "Seguimiento simplificado Directiva";
	/** The Constant OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS. */
//	private static final String OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS = "Observatorio UNE EN2019 (sin comprobar enlaces rotos)";
	private static final String OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS = "Seguimiento simplificado Directiva (sin comprobar enlaces rotos)";
	/** The Constant OBSERVATORIO_UNE_2012_VERSION_2. */
	private static final String OBSERVATORIO_UNE_2012_VERSION_2 = "Observatorio UNE 2012 (versión 2)";
	/** The Constant OBSERVATORIO_UNE_2012_ANTIGUA. */
	private static final String OBSERVATORIO_UNE_2012_ANTIGUA = "Observatorio UNE 2012 (antigua)";
	/** The Constant OBSERVATORIO_UNE_2004. */
	private static final String OBSERVATORIO_UNE_2004 = "Observatorio UNE 2004";
	/** The pmgr. */
	private final PropertiesManager pmgr;
	/** The mail service. */
	private final MailService mailService;

	/**
	 * Instantiates a new basic service mail service.
	 */
	public BasicServiceMailService() {
		pmgr = new PropertiesManager();
		mailService = new MailService();
	}

	/**
	 * Send basic service report.
	 *
	 * @param basicServiceForm the basic service form
	 * @param attachUrl        the attach url
	 * @param attachName       the attach name
	 */
	public void sendBasicServiceReport(final BasicServiceForm basicServiceForm, final String attachUrl, final String attachName) {
		try {
			mailService.sendMail(Collections.singletonList(basicServiceForm.getEmail()), getMailSubject(basicServiceForm.getReport()), getMailBody(basicServiceForm), attachUrl, attachName, true);
		} catch (MailException e) {
			Logger.putLog("Fallo al enviar el correo", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Send basic service error message.
	 *
	 * @param basicServiceForm the basic service form
	 * @param message          the message
	 */
	public void sendBasicServiceErrorMessage(final BasicServiceForm basicServiceForm, final String message) {
		final String subject = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.error.subject");
		try {
			mailService.sendMail(Collections.singletonList(basicServiceForm.getEmail()), subject, message);
		} catch (MailException e) {
			Logger.putLog("Fallo al enviar el correo", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the mail subject.
	 *
	 * @param reportType the report type
	 * @return the mail subject
	 */
	private String getMailSubject(final String reportType) {
		final String message = pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.subject");
		if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType) || Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, OBSERVATORIO_UNE_2004);
		} else if (Constants.REPORT_OBSERVATORY_2.equals(reportType) || Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, OBSERVATORIO_UNE_2012_ANTIGUA);
		} else if (Constants.REPORT_OBSERVATORY_3.equals(reportType) || Constants.REPORT_OBSERVATORY_3_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, OBSERVATORIO_UNE_2012_VERSION_2);
		} else if (Constants.REPORT_OBSERVATORY_4.equals(reportType) || Constants.REPORT_OBSERVATORY_4_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, OBSERVATORIO_UNE_EN2019);
		} else if (Constants.REPORT_OBSERVATORY_5.equals(reportType) || Constants.REPORT_OBSERVATORY_5_NOBROKEN.equals(reportType)) {
			return MessageFormat.format(message, OBSERVATORIO_ACCESIBILIDAD);
		} else if ("une".equals(reportType)) {
			return MessageFormat.format(message, "UNE 139803");
		} else {
			return "Informe de Accesibilidad Web";
		}
	}

	/**
	 * Gets the mail body.
	 *
	 * @param basicServiceForm the basic service form
	 * @return the mail body
	 */
	private String getMailBody(final BasicServiceForm basicServiceForm) {
		final String text;
		String complexName = "";
		if (basicServiceForm.isContentAnalysis() || basicServiceForm.isContentAnalysisMultiple()) {
			final String irap = "true".equalsIgnoreCase(basicServiceForm.getDepthReport()) ? pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes")
					: pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.no");
			text = MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory.content"), basicServiceForm.getUser(),
					reportToString(basicServiceForm.getReport()), irap);
		} else {
			if ("0".equals(basicServiceForm.getComplexity())) {
				basicServiceForm.setAmplitud("-");
				basicServiceForm.setProfundidad("-");
				complexName = "Única";
			} else {
				try (Connection c = DataBaseManager.getConnection()) {
					ComplejidadForm comp = ComplejidadDAO.getById(c, basicServiceForm.getComplexity());
					basicServiceForm.setAmplitud(String.valueOf(comp.getAmplitud()));
					basicServiceForm.setProfundidad(String.valueOf(comp.getProfundidad()));
					complexName = comp.getName();
				} catch (Exception e) {
					Logger.putLog("Error: ", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			final String inDirectory = basicServiceForm.isInDirectory() ? pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes")
					: pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.no");
			String proxyActive = "No";
			try (Connection c = DataBaseManager.getConnection()) {
				ProxyForm proxy = ProxyDAO.getProxy(c);
				proxyActive = proxy.getStatus() > 0 ? "Sí" : "No";
				DataBaseManager.closeConnection(c);
			} catch (Exception e) {
				Logger.putLog("Error: ", CrawlerUtils.class, Logger.LOG_LEVEL_ERROR, e);
			}
			final String irap = "true".equalsIgnoreCase(basicServiceForm.getDepthReport()) ? pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes")
					: pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.no");
			if (basicServiceForm.isAnalysisMix()) {
				text = MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory.mix"), basicServiceForm.getUser(), basicServiceForm.getDomain(),
						complexName, basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), inDirectory, reportToString(basicServiceForm.getReport()), proxyActive, irap);
			} else {
				text = MessageFormat.format(pmgr.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.mail.text.observatory"), basicServiceForm.getUser(), basicServiceForm.getDomain(),
						complexName, basicServiceForm.getProfundidad(), basicServiceForm.getAmplitud(), inDirectory, reportToString(basicServiceForm.getReport()), proxyActive, irap);
			}
		}
		return text;
	}

	/**
	 * Report to string.
	 *
	 * @param reportType the report type
	 * @return the string
	 */
	private String reportToString(final String reportType) {
		if (Constants.REPORT_OBSERVATORY.equals(reportType) || Constants.REPORT_OBSERVATORY_FILE.equals(reportType)) {
			return OBSERVATORIO_UNE_2004;
		} else if (Constants.REPORT_OBSERVATORY_1_NOBROKEN.equals(reportType)) {
			return OBSERVATORIO_UNE_2004_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_2.equals(reportType)) {
			return OBSERVATORIO_UNE_2012_ANTIGUA;
		} else if (Constants.REPORT_OBSERVATORY_2_NOBROKEN.equals(reportType)) {
			return OBSERVATORIO_UNE_2012_ANTIGUA_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_3.equals(reportType)) {
			return OBSERVATORIO_UNE_2012_VERSION_2;
		} else if (Constants.REPORT_OBSERVATORY_3_NOBROKEN.equals(reportType)) {
			return OBSERVATORIO_UNE_2012_VERSION_2_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_4.equals(reportType)) {
			return OBSERVATORIO_UNE_EN2019;
		} else if (Constants.REPORT_OBSERVATORY_4_NOBROKEN.equals(reportType)) {
			return OBSERVATORIO_UNE_UNE_EN2019_SIN_ENLACES_ROTOS;
		} else if (Constants.REPORT_OBSERVATORY_5.equals(reportType)) {
			return OBSERVATORIO_ACCESIBILIDAD;
		} else if (Constants.REPORT_OBSERVATORY_5_NOBROKEN.equals(reportType)) {
			return OBSERVATORIO_ACCESIBILIDAD_SIN_ENLACES_ROTOS;
		} else {
			return reportType;
		}
	}
}
