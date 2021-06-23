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
package es.inteco.rastreador2.openOffice.export.util;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

import es.gob.oaw.MailException;
import es.gob.oaw.MailService;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioRealizadoForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.openOffice.export.ExportOpenOfficeUtils;
import es.inteco.utils.FileUtils;

/**
 * Hilo para generar los pdfs de un observatorio de forma asíncrona.
 */
public class OpenOfficeGeneratorThread extends Thread {
	/** The request. */
	private HttpServletRequest request;
	/** The file path. */
	private final String filePath;
	/** The graphic path. */
	private final String graphicPath;
	/** The date. */
	private String date;
	/** The tipo observatorio. */
	private Long tipoObservatorio;
	/** The number observatory executions. */
	private int numberObservatoryExecutions;
	/** The tags to filter. */
	private String[] tagsToFilter;
	/** The tags to filter fixed. */
	private String[] tagsToFilterFixed;
	/** The grpahic conditional. */
	private Map<String, Boolean> grpahicConditional;
	/** The ex obs ids. */
	private String[] exObsIds;
	/** The id base template. */
	private Long idBaseTemplate;
	/** The id segment template. */
	private Long idSegmentTemplate;
	/** The id complexity template. */
	private Long idComplexityTemplate;
	/** The id segment evol template. */
	private Long idSegmentEvolTemplate;
	/** The report title. */
	private String reportTitle;
	/** The observatory FF form. */
	private ObservatorioRealizadoForm observatoryFFForm;
	/** The email. */
	private final String email;
	/** The observatory form. */
	private ObservatorioForm observatoryForm;

	/**
	 * Instantiates a new pdf generator thread.
	 *
	 * @param request                     the request
	 * @param filePath                    the file path
	 * @param graphicPath                 the graphic path
	 * @param date                        the date
	 * @param tipoObservatorio            the tipo observatorio
	 * @param numberObservatoryExecutions the number observatory executions
	 * @param tagsToFilter                the tags to filter
	 * @param tagsToFilterFixed           the tags to filter fixed
	 * @param grpahicConditional          the grpahic conditional
	 * @param exObsIds                    the ex obs ids
	 * @param idBaseTemplate              the id base template
	 * @param idSegmentTemplate           the id segment template
	 * @param idComplexityTemplate        the id complexity template
	 * @param idSegmentEvolTemplate       the id segment evol template
	 * @param reportTitle                 the report title
	 * @param observatoryFFForm           the observatory FF form
	 * @param email                       the email
	 * @param observatoryForm             the observatory form
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	public OpenOfficeGeneratorThread(final HttpServletRequest request, String filePath, String graphicPath, String date, Long tipoObservatorio, int numberObservatoryExecutions, String[] tagsToFilter,
			String[] tagsToFilterFixed, Map<String, Boolean> grpahicConditional, String[] exObsIds, Long idBaseTemplate, Long idSegmentTemplate, Long idComplexityTemplate, Long idSegmentEvolTemplate,
			String reportTitle, ObservatorioRealizadoForm observatoryFFForm, String email, ObservatorioForm observatoryForm) throws IllegalAccessException, InvocationTargetException {
		BeanUtils.populate(this.request, request.getParameterMap());
		this.filePath = filePath;
		this.graphicPath = graphicPath;
		this.date = date;
		this.tipoObservatorio = tipoObservatorio;
		this.numberObservatoryExecutions = numberObservatoryExecutions;
		this.tagsToFilter = tagsToFilter;
		this.tagsToFilterFixed = tagsToFilter;
		this.grpahicConditional = grpahicConditional;
		this.exObsIds = exObsIds;
		this.idBaseTemplate = idBaseTemplate;
		this.idSegmentTemplate = idSegmentTemplate;
		this.idComplexityTemplate = idComplexityTemplate;
		this.reportTitle = reportTitle;
		this.observatoryFFForm = observatoryFFForm;
		this.email = email;
		this.observatoryForm = observatoryForm;
		this.idSegmentEvolTemplate = idSegmentEvolTemplate;
	}

	/**
	 * Run.
	 */
	@Override
	public final void run() {
		final PropertiesManager pmgr = new PropertiesManager();
		final SimpleDateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		ExportOpenOfficeUtils.createOpenOfficeDocumentFiltered(request, filePath, graphicPath, df.format(observatoryFFForm.getFecha()), observatoryForm.getTipo(), exObsIds.length, tagsToFilter,
				tagsToFilterFixed, grpahicConditional, exObsIds, idBaseTemplate, idSegmentTemplate, idComplexityTemplate, idSegmentEvolTemplate, reportTitle);
		FileUtils.deleteDir(new File(graphicPath));
		StringBuilder mailBody = new StringBuilder(
				String.format("El proceso de generación de informes ha finalizado para el observatorio %s. Puede descargar los informes agrupados por dependencia en los siguientes enlaces: <br/>",
						getObservatoryName()));
		String url = request.getRequestURL().toString();
		String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
		// http://oaw.redsara.es/oaw/secure/exportOpenOfficeAction.do?idExObs=132&isPrimary=false&idCartucho=8&id_observatorio=41&id=132&esPrimera=true
		StringBuilder linkUrl = new StringBuilder(baseURL);
		linkUrl.append("secure/exportOpenOfficeAction.do?action=downloadFile");
		linkUrl.append("&idExObs=").append(this.exObsIds);
		linkUrl.append("&id_observatorio=").append(observatoryForm.getId());
		final String filename = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
		linkUrl.append("&file=").append(filename);
		mailBody.append("<a href=\"").append(url.toString()).append("\">").append(filename).append("</a><br>");
		final MailService mailService = new MailService();
		List<String> mailsTo = new ArrayList<>();
		mailsTo.add(email);
//		try {
//			Connection c = DataBaseManager.getConnection();
//			List<DatosForm> adminData = LoginDAO.getAdminUsers(c);
//			DataBaseManager.closeConnection(c);
//			if (adminData != null && !adminData.isEmpty()) {
//				for (DatosForm data : adminData) {
//					mailsTo.add(data.getEmail());
//				}
//			}
//		} catch (Exception e) {
//			Logger.putLog("Error al cargar los emails de los admin", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
//		}
		try {
			mailService.sendMail(mailsTo, "Generación de informes completado", mailBody.toString(), true);
		} catch (MailException e) {
			Logger.putLog("Fallo al enviar el correo", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the observatory name.
	 *
	 * @return the observatory name
	 */
	private String getObservatoryName() {
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, observatoryFFForm.getId());
			return observatoryForm.getNombre();
		} catch (Exception e) {
			return "";
		}
	}
}
