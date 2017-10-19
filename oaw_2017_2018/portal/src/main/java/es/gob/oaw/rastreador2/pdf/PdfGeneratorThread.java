package es.gob.oaw.rastreador2.pdf;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.struts.util.PropertyMessageResources;

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
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2017;
//import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PrimaryExportPdfUtils;
import es.inteco.utils.FileUtils;

/**
 * Hilo para generar los pdfs de un observatorio de forma asíncrona
 */
public class PdfGeneratorThread extends Thread {

	private final long idObservatory;
	private final long idObservatoryExecution;
	private final List<FulFilledCrawling> fulfilledCrawlings;
	private final String email;

	public PdfGeneratorThread(final long idObservatory, final long idObservatoryExecution, final List<FulFilledCrawling> fulfilledCrawlings, final String email) {
		super("PdfGeneratorThread");
		this.idObservatory = idObservatory;
		this.fulfilledCrawlings = fulfilledCrawlings;
		this.idObservatoryExecution = idObservatoryExecution;
		this.email = email;
	}

	@Override
	public final void run() {
		final String observatoryName = getObservatoryName();

		for (FulFilledCrawling fulfilledCrawling : fulfilledCrawlings) {
			buildPdf(fulfilledCrawling.getId(), fulfilledCrawling.getIdCrawling());
		}

		final MailService mailService = new MailService();
		mailService.sendMail(Collections.singletonList(email), "Generación de informes completado",
				String.format("El proceso de generación de informes ha finalizado para el observatorio %s. Para descargar los informes vuelva a ejecutar la acción", observatoryName));
	}

	private String getObservatoryName() {
		try (Connection c = DataBaseManager.getConnection()) {
			final ObservatorioForm observatoryForm = ObservatorioDAO.getObservatoryForm(c, idObservatory);
			return observatoryForm.getNombre();
		} catch (Exception e) {
			return "";
		}
	}

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

								// TODO 2017 Desdoblamiento nueva metodologia

								if ("UNE-2017".equalsIgnoreCase(aplicacion)) {

									PrimaryExportPdfUtils.exportToPdf(new AnonymousResultExportPdfUNE2017(), idRastreoRealizado, evaluationIds, previousEvaluationIds,
											PropertyMessageResources.getMessageResources("ApplicationResources"), pdfFile.getPath(), seed.getNombre(), "", idObservatoryExecution, observatoryType);
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
			Logger.putLog("Exception: ", PdfGeneratorThread.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

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
