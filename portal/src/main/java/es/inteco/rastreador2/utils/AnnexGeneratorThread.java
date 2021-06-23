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
package es.inteco.rastreador2.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.MessageResources;

import es.gob.oaw.MailException;
import es.gob.oaw.MailService;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.export.database.form.ComparisionForm;
import es.inteco.rastreador2.pdf.utils.ZipUtils;

/**
 * Hilo para generar los pdfs de un observatorio de forma asíncrona.
 */
public class AnnexGeneratorThread extends Thread {
	/** The message resources. */
	private final MessageResources messageResources;
	/** The id obs. */
	private final Long idObs;
	/** The id obs execution. */
	private final Long idObsExecution;
	/** The id operation. */
	private final Long idOperation;
	/** The tags to filter. */
	private final String[] tagsToFilter;
	/** The tags to filter fixed. */
	private final String[] tagsToFilterFixed;
	/** The ex obs ids. */
	private final String[] exObsIds;
	/** The comparision. */
	private final List<ComparisionForm> comparision;
	/** The email. */
	private final String email;
	/** The baseurl. */
	private final String baseurl;

	/**
	 * Instantiates a new pdf generator thread.
	 *
	 * @param messageResources  the message resources
	 * @param idObs             the id obs
	 * @param idObsExecution    the id obs execution
	 * @param idOperation       the id operation
	 * @param tagsToFilter      the tags to filter
	 * @param tagsToFilterFixed the tags to filter fixed
	 * @param exObsIds          the ex obs ids
	 * @param comparision       the comparision
	 * @param email             the email
	 * @param baseurl           the baseurl
	 */
	public AnnexGeneratorThread(final MessageResources messageResources, final Long idObs, final Long idObsExecution, final Long idOperation, final String[] tagsToFilter,
			final String[] tagsToFilterFixed, final String[] exObsIds, final List<ComparisionForm> comparision, final String email, final String baseurl) {
		super("AnnexGeneratorThread");
		this.messageResources = messageResources;
		this.idObs = idObs;
		this.idObsExecution = idObsExecution;
		this.idOperation = idOperation;
		this.tagsToFilter = tagsToFilter;
		this.tagsToFilterFixed = tagsToFilterFixed;
		this.exObsIds = exObsIds;
		this.comparision = comparision;
		this.email = email;
		this.baseurl = baseurl;
	}

	/**
	 * Run.
	 */
	@Override
	public final void run() {
		try {
			final String observatoryName = getObservatoryName();
			AnnexUtils.generateAllAnnex(messageResources, idObs, idObsExecution, idOperation, tagsToFilter, tagsToFilterFixed, exObsIds, comparision);
			final PropertiesManager pmgr = new PropertiesManager();
			final String exportPath = pmgr.getValue(CRAWLER_PROPERTIES, "export.annex.path");
			final String zipPath = exportPath + idOperation + File.separator + "anexos.zip";
			ZipUtils.generateZipFile(exportPath + idOperation.toString(), zipPath, true);
			StringBuilder mailBody = new StringBuilder(
					String.format("El proceso de generación de anexos ha finalizado para el observatorio %s. Puede descargar los informes agrupados por dependencia en los siguientes enlaces: <br/>",
							observatoryName));
			StringBuilder url = new StringBuilder(this.baseurl);
			url.append("secure/databaseExportAction.do?action=downloadFile");
			url.append("&file=").append(idOperation + File.separator + "anexos.zip");
			mailBody.append("<a href=\"").append(url.toString()).append("\">").append("anexos.zip").append("</a><br>");
			final MailService mailService = new MailService();
			List<String> mailsTo = new ArrayList<>();
			mailsTo.add(email);
//			try {
//				Connection c = DataBaseManager.getConnection();
//				List<DatosForm> adminData = LoginDAO.getAdminUsers(c);
//				DataBaseManager.closeConnection(c);
//				if (adminData != null && !adminData.isEmpty()) {
//					for (DatosForm data : adminData) {
//						mailsTo.add(data.getEmail());
//					}
//				}
//			} catch (Exception e) {
//				Logger.putLog("Error al cargar los emails de los admin", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
//			}
			mailService.sendMail(mailsTo, "Generación de informes completado", mailBody.toString(), true);
		} catch (MailException e) {
			Logger.putLog("Fallo al enviar el correo", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
		} catch (Exception e) {
			Logger.putLog("Error generando los anexos", this.getClass(), Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the observatory name.
	 *
	 * @return the observatory name
	 */
	private String getObservatoryName() {
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObs);
			return observatoryForm.getNombre();
		} catch (Exception e) {
			return "";
		}
	}
}
