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
package es.gob.oaw.rastreador2.pdf;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.PropertyMessageResources;

import es.gob.oaw.MailException;
import es.gob.oaw.MailService;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.observatorio.ObservatorioForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.dao.semilla.SemillaDAO;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfAccesibilidad;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012b;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNEEN2019;
//import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.utils.FileUtils;

/**
 * Hilo para generar los pdfs de un observatorio de forma asíncrona.
 */
public class PdfGeneratorThread2 extends Thread {
	/** The id observatory. */
	private final long idObservatory;
	/** The id observatory execution. */
	private final long idObservatoryExecution;
	/** The fulfilled crawlings. */
	private final List<FulFilledCrawling> fulfilledCrawlings;
	/** The email. */
	private final String email;
	/** The baseurl. */
	private final String baseurl;

	/**
	 * Instantiates a new pdf generator thread.
	 *
	 * @param idObservatory          the id observatory
	 * @param idObservatoryExecution the id observatory execution
	 * @param fulfilledCrawlings     the fulfilled crawlings
	 * @param email                  the email
	 * @param baseurl                the baseurl
	 */
	public PdfGeneratorThread2(final long idObservatory, final long idObservatoryExecution, final List<FulFilledCrawling> fulfilledCrawlings, final String email, final String baseurl) {
		super("PdfGeneratorThread");
		this.idObservatory = idObservatory;
		this.fulfilledCrawlings = fulfilledCrawlings;
		this.idObservatoryExecution = idObservatoryExecution;
		this.email = email;
		this.baseurl = baseurl;
	}

	/**
	 * Run.
	 */
	@Override
	public final void run() {
		final String observatoryName = getObservatoryName();
		for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
			buildPdf(fulfilledCrawling.getId(), fulfilledCrawling.getIdCrawling());
		}
		PropertiesManager pmgr = new PropertiesManager();
		List<String> zips = ZipUtils.pdfsZipToList(this.idObservatory, this.idObservatoryExecution, pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav"));
		StringBuilder mailBody = new StringBuilder(
				String.format("El proceso de generación de informes ha finalizado para el observatorio %s. Puede descargar los informes agrupados por dependencia en los siguientes enlaces: <br/>",
						observatoryName));
		for (String zip : zips) {
			final String filename = zip.substring(zip.lastIndexOf(File.separator) + 1);
			StringBuilder url = new StringBuilder(this.baseurl);
			url.append("secure/primaryExportPdfAction.do?action=downloadFile");
			url.append("&idExObs=").append(idObservatoryExecution);
			url.append("&id_observatorio=").append(idObservatory);
			url.append("&file=").append(filename);
			mailBody.append("<a href=\"").append(url.toString()).append("\">").append(filename).append("</a><br>");
		}
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
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			return observatoryForm.getNombre();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Builds the pdf.
	 *
	 * @param idRastreoRealizado the id rastreo realizado
	 * @param idRastreo          the id rastreo
	 */
	private void buildPdf(final long idRastreoRealizado, final long idRastreo) {
		try (Connection c = DataBaseManager.getConnection()) {
			final SemillaForm seed = SemillaDAO.getSeedById(c, RastreoDAO.getIdSeedByIdRastreo(c, idRastreo));
			// final File pdfFile = getReportFile(seed);
			List<File> pdfFiles = getReportFiles(seed);
			if (pdfFiles != null && !pdfFiles.isEmpty()) {
				for (File pdfFile : pdfFiles) {
					final File sources = new File(pdfFile.getParentFile(), "sources.zip");
					// Si el pdf no ha sido creado lo creamos
					if (!pdfFile.exists() || !sources.exists()) {
						final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
						final List<Long> previousEvaluationIds;
						if (evaluationIds != null && !evaluationIds.isEmpty()) {
							if (!pdfFile.exists()) {
								final es.gob.oaw.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.gob.oaw.rastreador2.observatorio.ObservatoryManager();
								previousEvaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(observatoryManager
										.getPreviousIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(idRastreo, ObservatorioDAO.getPreviousObservatoryExecution(c, idObservatoryExecution)));
								final long observatoryType = ObservatorioDAO.getObservatoryForm(c, idObservatory).getTipo();
								String aplicacion = CartuchoDAO.getApplicationFromExecutedObservatoryId(c, idRastreoRealizado, idRastreo);
								// Desdoblamiento nueva metodologia Nuevo fichero con los textos para las
								// exportaciones
								// Añadada UNE-EN2019 y accesibilidad
								if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(aplicacion)) {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfAccesibilidad(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution,
											observatoryType);
								} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(aplicacion)) {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNEEN2019(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution,
											observatoryType);
								} else if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(aplicacion)) {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012b(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution,
											observatoryType);
								} else {
									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2012(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources("ApplicationResources"), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution, observatoryType);
								}
							}
							final SourceFilesManager sourceFilesManager = new SourceFilesManager(pdfFile.getParentFile());
							if (!sourceFilesManager.existsSourcesZip()) {
								sourceFilesManager.writeSourceFiles(c, evaluationIds);
								sourceFilesManager.zipSources(true);
							}
							FileUtils.deleteDir(new File(pdfFile.getParent() + File.separator + "temp"));
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", PdfGeneratorThread2.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the report files.
	 *
	 * @param seed the seed
	 * @return the report files
	 */
	private List<File> getReportFiles(final SemillaForm seed) {
		List<File> pdfFiles = new ArrayList<>();
		final PropertiesManager pmgr = new PropertiesManager();
		try (Connection c = DataBaseManager.getConnection()) {
			List<DependenciaForm> dependenciasSemilla = SemillaDAO.getSeedDependenciasById(c, seed.getId());
			if (dependenciasSemilla != null && !dependenciasSemilla.isEmpty()) {
				for (DependenciaForm dependenciaSemilla : dependenciasSemilla) {
					String dependOn = PDFUtils.formatSeedName(dependenciaSemilla.getName());
					if (dependOn == null || dependOn.isEmpty()) {
						dependOn = Constants.NO_DEPENDENCE;
					}
					final String path = pmgr.getValue(CRAWLER_PROPERTIES, "path.inteco.exports.observatory.intav") + idObservatory + File.separator + idObservatoryExecution + File.separator + dependOn
							+ File.separator + PDFUtils.formatSeedName(seed.getNombre());
					pdfFiles.add(new File(path + File.separator + PDFUtils.formatSeedName(seed.getNombre()) + ".pdf"));
				}
			}
		} catch (Exception e) {
		}
		return pdfFiles;
	}
}
