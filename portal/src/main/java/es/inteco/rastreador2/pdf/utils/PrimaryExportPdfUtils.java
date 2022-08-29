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
package es.inteco.rastreador2.pdf.utils;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;
import static es.inteco.common.ConstantsFont.HALF_LINE_SPACE;
import static es.inteco.common.ConstantsFont.LINE_SPACE;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.IndexEvents;

import es.gob.oaw.rastreador2.pdf.basicservice.BasicServiceObservatoryResultsSummaryPdfSectionBuilder;
import es.gob.oaw.rastreador2.pdf.basicservice.ObservatoryPageResultsPdfSectionBuilder;
import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.dao.cartucho.CartuchoDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfAccesibilidad;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2004;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012b;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNEEN2019;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosPrimariosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.oaw.wcagem.WcagEmReport;
import es.oaw.wcagem.WcagEmUtils;
import es.oaw.wcagem.WcagOdsUtils;
import es.oaw.wcagem.WcagXlsxUtils;

/**
 * The Class PrimaryExportPdfUtils.
 */
public final class PrimaryExportPdfUtils {
	/**
	 * Instantiates a new primary export pdf utils.
	 */
	protected PrimaryExportPdfUtils() {
	}

	/**
	 * Export to pdf.
	 *
	 * @param idRastreo              the id rastreo
	 * @param idRastreoRealizado     the id rastreo realizado
	 * @param request                the request
	 * @param generalExpPath         the general exp path
	 * @param seed                   the seed
	 * @param content                the content
	 * @param idObservatoryExecution the id observatory execution
	 * @param observatoryType        the observatory type
	 * @throws Exception the exception
	 */
	public static void exportToPdf(final Long idRastreo, final Long idRastreoRealizado, final HttpServletRequest request, final String generalExpPath, final String seed, final String content,
			final long idObservatoryExecution, final long observatoryType) throws Exception {
		AnonymousResultExportPdf builder = null;
		try (Connection c = DataBaseManager.getConnection()) {
			final FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(c, idRastreoRealizado);
			final String application = CartuchoDAO.getApplication(c, Long.valueOf(crawling.getIdCartridge()));
			Logger.putLog("Normativa " + application, PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_INFO);
			if (Constants.NORMATIVA_UNE_2012.equalsIgnoreCase(application)) {
				builder = new AnonymousResultExportPdfUNE2012();
			} else if ("UNE-2004".equalsIgnoreCase(application)) {
				builder = new AnonymousResultExportPdfUNE2004();
			} else if (Constants.NORMATIVA_UNE_2012_B.equalsIgnoreCase(application)) {
				builder = new AnonymousResultExportPdfUNE2012b();
			} else if (Constants.NORMATIVA_UNE_EN2019.equalsIgnoreCase(application)) {
				builder = new AnonymousResultExportPdfUNEEN2019();
			} else if (Constants.NORMATIVA_ACCESIBILIDAD.equalsIgnoreCase(application)) {
				builder = new AnonymousResultExportPdfAccesibilidad();
			}
		} catch (Exception e) {
			Logger.putLog("Error al preparar el builder de PDF", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
			builder = new AnonymousResultExportPdfUNE2004();
		}
		exportToPdf(builder, idRastreo, idRastreoRealizado, request, generalExpPath, seed, content, idObservatoryExecution, observatoryType);
	}

	/**
	 * Export to pdf.
	 *
	 * @param pdfBuilder             the pdf builder
	 * @param idRastreo              the id rastreo
	 * @param idRastreoRealizado     the id rastreo realizado
	 * @param request                the request
	 * @param generalExpPath         the general exp path
	 * @param seed                   the seed
	 * @param content                the content
	 * @param idObservatoryExecution the id observatory execution
	 * @param observatoryType        the observatory type
	 * @throws Exception the exception
	 */
	public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idRastreo, final Long idRastreoRealizado, final HttpServletRequest request, final String generalExpPath,
			final String seed, final String content, final long idObservatoryExecution, final long observatoryType) throws Exception {
		final boolean isBasicService = idRastreoRealizado < 0;
		try (Connection c = DataBaseManager.getConnection()) {
			final List<Long> evaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(idRastreoRealizado);
			final List<Long> previousEvaluationIds;
			if (!isBasicService) {
				final es.gob.oaw.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.gob.oaw.rastreador2.observatorio.ObservatoryManager();
				previousEvaluationIds = AnalisisDatos.getEvaluationIdsFromRastreoRealizado(
						observatoryManager.getPreviousIdRastreoRealizadoFromIdRastreoAndIdObservatoryExecution(idRastreo, ObservatorioDAO.getPreviousObservatoryExecution(c, idObservatoryExecution)));
			} else {
				previousEvaluationIds = Collections.emptyList();
			}
			exportToPdf(pdfBuilder, idRastreoRealizado, evaluationIds, previousEvaluationIds, request, generalExpPath, seed, content, idObservatoryExecution, observatoryType);
		}
	}

	/**
	 * Export to pdf.
	 *
	 * @param pdfBuilder             the pdf builder
	 * @param idRastreoRealizado     the id rastreo realizado
	 * @param evaluationIds          the evaluation ids
	 * @param previousEvaluationIds  the previous evaluation ids
	 * @param request                the request
	 * @param generalExpPath         the general exp path
	 * @param seed                   the seed
	 * @param content                the content
	 * @param idObservatoryExecution the id observatory execution
	 * @param observatoryType        the observatory type
	 * @throws Exception the exception
	 */
	public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idRastreoRealizado, final List<Long> evaluationIds, final List<Long> previousEvaluationIds,
			final HttpServletRequest request, final String generalExpPath, final String seed, final String content, final long idObservatoryExecution, final long observatoryType) throws Exception {
		// Nuevos textos UNE-2012-B
		try (Connection c = DataBaseManager.getConnection()) {
			if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
				exportToPdf(pdfBuilder, idRastreoRealizado, evaluationIds, previousEvaluationIds, MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019), generalExpPath, seed,
						content, idObservatoryExecution, observatoryType);
			} else if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
				exportToPdf(pdfBuilder, idRastreoRealizado, evaluationIds, previousEvaluationIds, MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), generalExpPath, seed,
						content, idObservatoryExecution, observatoryType);
			} else {
				exportToPdf(pdfBuilder, idRastreoRealizado, evaluationIds, previousEvaluationIds, CrawlerUtils.getResources(request), generalExpPath, seed, content, idObservatoryExecution,
						observatoryType);
			}
		} catch (Exception e) {
			Logger.putLog("Error al preparar el builder de PDF", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Export to pdf.
	 *
	 * @param pdfBuilder             the pdf builder
	 * @param idRastreoRealizado     the id rastreo realizado
	 * @param evaluationIds          the evaluation ids
	 * @param previousEvaluationIds  the previous evaluation ids
	 * @param messageResources       the message resources
	 * @param generalExpPath         the general exp path
	 * @param seed                   the seed
	 * @param content                the content
	 * @param idObservatoryExecution the id observatory execution
	 * @param observatoryType        the observatory type
	 * @throws Exception the exception
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	public static void exportToPdf(final AnonymousResultExportPdf pdfBuilder, final Long idRastreoRealizado, final List<Long> evaluationIds, final List<Long> previousEvaluationIds,
			final MessageResources messageResources, final String generalExpPath, final String seed, final String content, final long idObservatoryExecution, final long observatoryType)
			throws Exception {
		final File file = new File(generalExpPath);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			if (!file.getParentFile().setReadable(true, false) || !file.getParentFile().setWritable(true, false)) {
				Logger.putLog(String.format("No se ha podido asignar permisos a los directorios de exportación a PDF %s", file.getParentFile().getAbsolutePath()), PrimaryExportPdfUtils.class,
						Logger.LOG_LEVEL_ERROR);
			}
			Logger.putLog("No se ha podido crear los directorios para exportar a PDF", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR);
		}
		Logger.putLog("Exportando a PDF BasicServicePdfReport.exportToPdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_DEBUG);
		final Document document = new Document(PageSize.A4, 50, 50, 110, 72);
		try (FileOutputStream outputFileStream = new FileOutputStream(file)) {
			try (Connection connection = DataBaseManager.getConnection()) {
				final FulfilledCrawlingForm crawling = RastreoDAO.getFullfilledCrawlingExecution(connection, idRastreoRealizado);
				final es.gob.oaw.rastreador2.observatorio.ObservatoryManager observatoryManager = new es.gob.oaw.rastreador2.observatorio.ObservatoryManager();
				final List<ObservatoryEvaluationForm> currentEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(idObservatoryExecution, evaluationIds);
				// Obtenemos el id de la ejecución anterior para cargar la
				// metodología que se usó en ese observatorio (ya que si se
				// modificó altera los resultados)
				final Long previousObservatoryExecution = ObservatorioDAO.getPreviousObservatoryExecution(connection, idObservatoryExecution);
				final List<ObservatoryEvaluationForm> previousEvaluationPageList = observatoryManager.getObservatoryEvaluationsFromObservatoryExecution(previousObservatoryExecution,
						previousEvaluationIds);
				final PdfWriter writer = PdfWriter.getInstance(document, outputFileStream);
				writer.setTagged(0);
				writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
				writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));
				final String crawlingDate = crawling != null ? crawling.getDate() : CrawlerUtils.formatDate(new Date());
				String footerText = "";
				String seedFooter = seed;
				if (seed.length() > 50) {
					seedFooter = seed.substring(0, 50).concat("...");
				}
				if (!pdfBuilder.isBasicService()) {
					footerText = messageResources.getMessage("ob.resAnon.intav.report.foot", new String[] { seedFooter, crawlingDate });
				} else {
					footerText = messageResources.getMessage("ob.resAnon.intav.report.foot.simple");
				}
				writer.setPageEvent(new ExportPageEventsObservatoryMP(footerText, crawlingDate));
				ExportPageEventsObservatoryMP.setPrintFooter(true);
				final IndexEvents index = new IndexEvents();
				writer.setPageEvent(index);
				writer.setLinearPageMode();
				document.open();
				MessageResources messageResources2019 = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
				MessageResources messageResourcesAccesibility = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
				final PdfTocManager pdfTocManager = new PdfTocManager(index);
				// Nuevo informe accesibilidad
				if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
					if (pdfBuilder.isBasicService()) {
						PDFUtils.addCoverPage(document, messageResourcesAccesibility.getMessage("pdf.accessibility.title", new String[] { seed.toUpperCase(), pdfBuilder.getTitle() }),
								pdfBuilder.getBasicServiceForm().getName(), messageResources.getMessage("pdf.accessibility.on.demand"));
					} else {
						AnonymousResultExportPdfAccesibilidad.addCoverPage(document, messageResourcesAccesibility.getMessage("pdf.accessibility.title"),
								messageResourcesAccesibility.getMessage("pdf.accessibility.subtitle"),
								messageResourcesAccesibility.getMessage("pdf.accessibility.portal", new String[] { seed.toUpperCase(), currentEvaluationPageList.get(0).getUrl() }),
								messageResourcesAccesibility.getMessage("pdf.accessibility.info.estimated"));
					}
					// Introduction chapter
					pdfBuilder.createIntroductionChapter(messageResourcesAccesibility, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
					pdfTocManager.addChapterCount();
					// Resumen de resultados
					final RankingInfo rankingActual = crawling != null ? observatoryManager.calculateRankingWithCompliance(idObservatoryExecution, crawling.getSeed()) : null;
					final RankingInfo rankingPrevio = crawling != null ? observatoryManager.calculatePreviousRankingWithCompliance(idObservatoryExecution, crawling.getSeed()) : null;
					AnonymousResultExportPdfAccesibilidad.addObservatoryScoreSummary(pdfBuilder, messageResourcesAccesibility, document, pdfTocManager, currentEvaluationPageList,
							previousEvaluationPageList, file, rankingActual, rankingPrevio);
					pdfTocManager.addChapterCount();
//					// Annex
					pdfBuilder.createMethodologyChapter(messageResourcesAccesibility, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0,
							pdfBuilder.isBasicService());
				} else
				// Nuevo informe 2019
				if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
					if (pdfBuilder.isBasicService()) {
						PDFUtils.addCoverPage(document, messageResources2019.getMessage("pdf.accessibility.title.basic.service", new String[] { seed.toUpperCase(), pdfBuilder.getTitle() }),
								pdfBuilder.getBasicServiceForm().getName(), messageResources2019.getMessage("pdf.accessibility.on.demand"));
					} else {
						AnonymousResultExportPdfUNEEN2019.addCoverPage(document, messageResources2019.getMessage("pdf.accessibility.title"),
								messageResources2019.getMessage("pdf.accessibility.subtitle"),
								messageResources2019.getMessage("pdf.accessibility.portal", new String[] { seed.toUpperCase(), currentEvaluationPageList.get(0).getUrl() }),
								messageResources2019.getMessage("pdf.accessibility.info.estimated"));
					}
					// Introduction chapter
					pdfBuilder.createIntroductionChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
					pdfTocManager.addChapterCount();
					// Seed detail chapter
					((AnonymousResultExportPdfUNEEN2019) pdfBuilder).createSeedDetailsChapter(messageResources2019, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, crawling.getSeed());
					pdfTocManager.addChapterCount();
					// Muestra de páginas
					pdfBuilder.createObjetiveChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, observatoryType);
					pdfTocManager.addChapterCount();
					// Resumen de resultados
					final RankingInfo rankingActual = crawling != null ? observatoryManager.calculateRankingWithCompliance(idObservatoryExecution, crawling.getSeed()) : null;
					final RankingInfo rankingPrevio = crawling != null ? observatoryManager.calculatePreviousRankingWithCompliance(idObservatoryExecution, crawling.getSeed()) : null;
					AnonymousResultExportPdfUNEEN2019.addObservatoryScoreSummary(pdfBuilder, messageResources2019, document, pdfTocManager, currentEvaluationPageList, previousEvaluationPageList, file,
							rankingActual, rankingPrevio);
					pdfTocManager.addChapterCount();
					// Resultados por verificación
					final BasicServiceObservatoryResultsSummaryPdfSectionBuilder observatoryResultsSummarySectionBuilder = new BasicServiceObservatoryResultsSummaryPdfSectionBuilder(
							currentEvaluationPageList);
					final ScoreForm currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
					observatoryResultsSummarySectionBuilder.addObservatoryResultsSummaryWithCompliance(messageResources2019, document, pdfTocManager, currentScore);
					// Detalles por página
					final ObservatoryPageResultsPdfSectionBuilder observatoryPageResultsSectionBuilder = new ObservatoryPageResultsPdfSectionBuilder(currentEvaluationPageList);
					observatoryPageResultsSectionBuilder.addPageResults(messageResources2019, document, pdfTocManager, true);
					// Annex
					pdfBuilder.createMethodologyChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, observatoryType,
							pdfBuilder.isBasicService());
				} else {
					if (pdfBuilder.isBasicService()) {
						PDFUtils.addCoverPage(document, messageResources.getMessage("pdf.accessibility.title", new String[] { seed.toUpperCase(), pdfBuilder.getTitle() }),
								pdfBuilder.getBasicServiceForm().getName(), "Informe emitido bajo demanda.");
					} else {
						PDFUtils.addCoverPage(document, messageResources.getMessage("pdf.accessibility.title", new String[] { seed.toUpperCase(), pdfBuilder.getTitle() }),
								currentEvaluationPageList.get(0).getUrl());
					}
					pdfBuilder.createIntroductionChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
					pdfTocManager.addChapterCount();
					pdfBuilder.createObjetiveChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, observatoryType);
					pdfTocManager.addChapterCount();
					// Resumen de las puntuaciones del Observatorio
					final RankingInfo rankingActual = crawling != null ? observatoryManager.calculateRanking(idObservatoryExecution, crawling.getSeed()) : null;
					final RankingInfo rankingPrevio = crawling != null ? observatoryManager.calculatePreviousRanking(idObservatoryExecution, crawling.getSeed()) : null;
					if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
						addObservatoryScoreSummary(pdfBuilder, messageResources2019, document, pdfTocManager, currentEvaluationPageList, previousEvaluationPageList, file, rankingActual,
								rankingPrevio);
					} else if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
						addObservatoryScoreSummary(pdfBuilder, MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), document, pdfTocManager, currentEvaluationPageList,
								previousEvaluationPageList, file, rankingActual, rankingPrevio);
					} else {
						addObservatoryScoreSummary(pdfBuilder, messageResources, document, pdfTocManager, currentEvaluationPageList, previousEvaluationPageList, file, rankingActual, rankingPrevio);
					}
					pdfTocManager.addChapterCount();
					// Resumen de las puntuaciones del Observatorio
					final BasicServiceObservatoryResultsSummaryPdfSectionBuilder observatoryResultsSummarySectionBuilder = new BasicServiceObservatoryResultsSummaryPdfSectionBuilder(
							currentEvaluationPageList);
					if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
						observatoryResultsSummarySectionBuilder.addObservatoryResultsSummaryAccesibility(MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), document,
								pdfTocManager);
					} else if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
						observatoryResultsSummarySectionBuilder.addObservatoryResultsSummaryWithCompliance(messageResources, document, pdfTocManager,
								pdfBuilder.generateScores(messageResources, currentEvaluationPageList));
					} else if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
						observatoryResultsSummarySectionBuilder.addObservatoryResultsSummary(MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), document, pdfTocManager);
					} else {
						observatoryResultsSummarySectionBuilder.addObservatoryResultsSummary(messageResources, document, pdfTocManager);
					}
					// Desdoblamiento nueva metodologia Nuevo fichero con los textos para las
					// exportaciones
					final ObservatoryPageResultsPdfSectionBuilder observatoryPageResultsSectionBuilder = new ObservatoryPageResultsPdfSectionBuilder(currentEvaluationPageList);
					if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
						observatoryPageResultsSectionBuilder.addPageResults(MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD), document, pdfTocManager, true);
					} else if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
						observatoryPageResultsSectionBuilder.addPageResults(messageResources2019, document, pdfTocManager, true);
					} else if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
						observatoryPageResultsSectionBuilder.addPageResults(MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B), document, pdfTocManager, true);
					} else {
						observatoryPageResultsSectionBuilder.addPageResults(messageResources, document, pdfTocManager, false);
					}
					pdfBuilder.createMethodologyChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, observatoryType,
							pdfBuilder.isBasicService());
				}
				// Ponemos la variable a true para que no se escriba el footer
				// en el índice
				IndexUtils.createIndex(writer, document, messageResources.getMessage("pdf.accessibility.index.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT);
				ExportPageEventsObservatoryMP.setPrintFooter(true);
				// (Disable) JSON WCAG-EM and ODS
				if (false) {
					// JSON
					WcagEmReport report = WcagEmUtils.generateReport(messageResources, pdfBuilder, BasicServiceUtils.getTitleDocFromContent(currentEvaluationPageList.get(0).getSource(), false),
							Long.parseLong(crawling.getId()));
					ObjectMapper mapper = new ObjectMapper();
					String jsonInString2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(report);
					org.apache.commons.io.FileUtils.writeStringToFile(new File(new File(file.getPath()).getParentFile().getPath() + "/wcagem-report.json"), jsonInString2);
					// ODS REPORT
					SpreadSheet ods = WcagOdsUtils.generateOds(report);
					Workbook wb = WcagXlsxUtils.generateXlsx(report);
					File outputFile = new File(new File(file.getPath()).getParentFile().getPath() + "/Informe Revision Accesibilidad - Sitios web.ods");
					ods.saveAs(outputFile);
					File outputFilexlsx = new File(new File(file.getPath()).getParentFile().getPath() + "/Informe Revision Accesibilidad - Sitios web.xlsx");
					final FileOutputStream fos = new FileOutputStream(outputFilexlsx);
					wb.write(fos);
					fos.close();
					// Reload the workbook, workaround for bug 49940
					// https://issues.apache.org/bugzilla/show_bug.cgi?id=49940
					wb = new XSSFWorkbook(new FileInputStream(outputFilexlsx));
				}
			} catch (DocumentException e) {
				Logger.putLog("Error al exportar a pdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			} catch (Exception e) {
				Logger.putLog("Excepción genérica al generar el pdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			} finally {
				if (document.isOpen()) {
					try {
						document.close();
					} catch (Exception e) {
						Logger.putLog("Error al cerrar el pdf", PrimaryExportPdfUtils.class, Logger.LOG_LEVEL_ERROR, e);
					}
				}
			}
		}
	}

	/**
	 * Adds the observatory score summary.
	 *
	 * @param pdfBuilder                 the pdf builder
	 * @param messageResources           the message resources
	 * @param document                   the document
	 * @param pdfTocManager              the pdf toc manager
	 * @param currentEvaluationPageList  the current evaluation page list
	 * @param previousEvaluationPageList the previous evaluation page list
	 * @param file                       the file
	 * @param rankingActual              the ranking actual
	 * @param rankingPrevio              the ranking previo
	 * @throws Exception the exception
	 */
	private static void addObservatoryScoreSummary(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, Document document, PdfTocManager pdfTocManager,
			final List<ObservatoryEvaluationForm> currentEvaluationPageList, List<ObservatoryEvaluationForm> previousEvaluationPageList, final File file, final RankingInfo rankingActual,
			final RankingInfo rankingPrevio) throws Exception {
		final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("observatorio.puntuacion.resultados.resumen").toUpperCase(), pdfTocManager,
				ConstantsFont.CHAPTER_TITLE_MP_FONT);
		final ArrayList<String> boldWord = new ArrayList<>();
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p1"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		boldWord.clear();
		boldWord.add(messageResources.getMessage("resultados.primarios.4.p2.bold1"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p2"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		boldWord.clear();
		boldWord.add(messageResources.getMessage("resultados.primarios.4.p3.bold1"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p3"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		final ScoreForm currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
		ScoreForm previousScore = null;
		if (previousEvaluationPageList != null && !previousEvaluationPageList.isEmpty()) {
			// Recuperamos el cartucho asociado al analsis
			Connection c = DataBaseManager.getConnection();
			previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);
			DataBaseManager.closeConnection(c);
		}
		//// TABLA PUNTUCAION OBSERVATORIO
		final float[] columnWidths;
		if (rankingPrevio != null) {
			columnWidths = new float[] { 0.42f, 0.20f, 0.20f, 0.18f };
		} else {
			columnWidths = new float[] { 0.6f, 0.4f };
		}
		final PdfPTable tablaRankings = new PdfPTable(columnWidths);
		tablaRankings.setSpacingBefore(LINE_SPACE);
		tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
		tablaRankings.setWidthPercentage(90);
		tablaRankings.setHeaderRows(1);
		tablaRankings.addCell(PDFUtils.createEmptyTableCell());
		if (pdfBuilder.isBasicService()) {
			tablaRankings.addCell(PDFUtils.createTableCell("Resultado", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		} else {
			tablaRankings.addCell(PDFUtils.createTableCell("Resultado\n" + rankingActual.getDate(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		if (rankingPrevio != null) {
			tablaRankings.addCell(PDFUtils.createTableCell("Resultado\n" + rankingPrevio.getDate(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		tablaRankings.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		if (rankingPrevio != null) {
			tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getLevel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.addCell(createEvolutionLevelCell(messageResources, currentScore.getLevel(), previousScore.getLevel()));
		}
		tablaRankings.completeRow();
		tablaRankings.addCell(PDFUtils.createTableCell("Puntuación Media del Portal", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		if (rankingPrevio != null) {
			tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(currentScore.getTotalScore(), previousScore.getTotalScore()),
					String.valueOf(currentScore.getTotalScore().subtract(previousScore.getTotalScore()).toPlainString()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING,
					-1));
		}
		tablaRankings.completeRow();
		if (rankingActual != null) {
			tablaRankings.addCell(
					PDFUtils.createTableCell(messageResources.getMessage("observatorio.posicion.global"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(rankingActual.getGlobalRank() + " \n(" + messageResources.getMessage("de.text", rankingActual.getGlobalSeedsNumber()) + ")", Color.WHITE,
					ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (rankingPrevio != null) {
				tablaRankings.addCell(PDFUtils.createTableCell(rankingPrevio.getGlobalRank() + " \n(" + messageResources.getMessage("de.text", rankingPrevio.getGlobalSeedsNumber()) + ")", Color.WHITE,
						ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(rankingActual.getGlobalRank(), rankingPrevio.getGlobalRank(), true),
						String.valueOf(rankingPrevio.getGlobalRank() - rankingActual.getGlobalRank()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
			tablaRankings.addCell(
					PDFUtils.createTableCell("Posición en " + rankingActual.getCategoria().getName(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(rankingActual.getCategoryRank() + " \n(" + messageResources.getMessage("de.text", rankingActual.getCategorySeedsNumber()) + ")", Color.WHITE,
					ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (rankingPrevio != null) {
				tablaRankings.addCell(PDFUtils.createTableCell(rankingPrevio.getCategoryRank() + " \n(" + messageResources.getMessage("de.text", rankingPrevio.getCategorySeedsNumber()) + ")",
						Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(rankingActual.getCategoryRank(), rankingPrevio.getCategoryRank(), true),
						String.valueOf(rankingPrevio.getCategoryRank() - rankingActual.getCategoryRank()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
		}
		chapter.add(tablaRankings);
		final String noDataMess = messageResources.getMessage("grafica.sin.datos");
		// Obtener lista resultados iteración anterior
		addLevelAllocationResultsSummary(messageResources, chapter, file, currentEvaluationPageList, previousEvaluationPageList, noDataMess, pdfBuilder.isBasicService());
		Section section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion1"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
				chapter, pdfTocManager.addSection(), 1);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.41.p1"), ConstantsFont.PARAGRAPH, section);
		addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_1);
		section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, rankingPrevio != null ? previousScore : null, Constants.OBS_PRIORITY_1, pdfBuilder.isBasicService()));
		section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion2"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.addSection(), 1);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.42.p1"), ConstantsFont.PARAGRAPH, section);
		addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_2);
		section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, rankingPrevio != null ? previousScore : null, Constants.OBS_PRIORITY_2, pdfBuilder.isBasicService()));
		PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuacion.pagina"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.addSection(), 1);
		addResultsByPage(messageResources, chapter, file, currentEvaluationPageList, noDataMess);
		document.add(chapter);
	}

	/**
	 * Crea una celda PdfPCell para una tabla del informa PDF con la evolución del nivel de accesibilidad.
	 *
	 * @param messageResources the message resources
	 * @param currentLevel     String nivel de accesibilidad actual.
	 * @param previousLevel    String nivel de accesibilidad de la iteración anterior.
	 * @return una celda PdfPCell con una imagen que indica la evolución y una cadena con la misma información complementando la imagen.
	 */
	private static PdfPCell createEvolutionLevelCell(final MessageResources messageResources, final String currentLevel, final String previousLevel) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (currentLevel.equalsIgnoreCase(previousLevel)) {
			return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene"), "se mantiene", Color.WHITE, ConstantsFont.noteCellFont,
					Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
		} else {
			// Si los valores entre iteraciones han variado
			if (messageResources.getMessage("resultados.anonimos.num.portales.nv").equalsIgnoreCase(previousLevel)
					|| messageResources.getMessage("resultados.anonimos.num.portales.parcial").equalsIgnoreCase(previousLevel)) {
				// Si el valor actual es distinto al anterior y el anterior era
				// "No válido" (o "Parcial") entonces ha mejorado
				return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Mejora"), "mejora", Color.WHITE, ConstantsFont.noteCellFont,
						Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
			} else if (messageResources.getMessage("resultados.anonimos.num.portales.aa").equalsIgnoreCase(previousLevel)) {
				// Si el valor actual es distinto al anterior y el anterior era
				// "Prioridad 1 y 2" entonces ha empeorado
				return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Empeora"), "empeora", Color.WHITE, ConstantsFont.noteCellFont,
						Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
			} else {
				// Si estamos en este punto el valor anterior era "Prioridad 1"
				// y el actual es distinto
				if (messageResources.getMessage("resultados.anonimos.num.portales.aa").equalsIgnoreCase(currentLevel)) {
					return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Mejora"), "mejora", Color.WHITE,
							ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
				} else {
					return PDFUtils.createTableCell(PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Empeora"), "empeora", Color.WHITE,
							ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
				}
			}
		}
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param actualValue       the actual value
	 * @param previousValue     the previous value
	 * @param invertedEvolution the inverted evolution
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final int actualValue, final int previousValue, boolean invertedEvolution) {
		if (invertedEvolution) {
			return getEvolutionImage(previousValue, actualValue);
		} else {
			return getEvolutionImage(actualValue, previousValue);
		}
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final BigDecimal actualValue, final BigDecimal previousValue) {
		return getEvolutionImage(actualValue.compareTo(previousValue));
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final int actualValue, final int previousValue) {
		return getEvolutionImage(actualValue - previousValue);
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param compareValue the compare value
	 * @return the evolution image
	 */
	private static Image getEvolutionImage(final int compareValue) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (compareValue > 0) {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Aumenta");
		} else if (compareValue < 0) {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Disminuye");
		} else {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene");
		}
	}

	/**
	 * Adds the level allocation results summary.
	 *
	 * @param messageResources           the message resources
	 * @param section                    the section
	 * @param file                       the file
	 * @param currentEvaluationPageList  the current evaluation page list
	 * @param previousEvaluationPageList the previous evaluation page list
	 * @param noDataMess                 the no data mess
	 * @param isBasicService             the is basic service
	 * @throws Exception the exception
	 */
	private static void addLevelAllocationResultsSummary(final MessageResources messageResources, final Section section, final File file,
			final List<ObservatoryEvaluationForm> currentEvaluationPageList, final List<ObservatoryEvaluationForm> previousEvaluationPageList, final String noDataMess, final boolean isBasicService)
			throws Exception {
		final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);
		final Map<String, Integer> previousResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(previousEvaluationPageList);
		final List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);
		final List<GraphicData> previousGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, previousResultsByLevel);
		final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test.jpg";
		final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
		ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, currentEvaluationPageList, title, filePath, noDataMess);
		final Image image = PDFUtils.createImage(filePath, title);
		if (image != null) {
			image.scalePercent(60);
			image.setAlignment(Element.ALIGN_CENTER);
			image.setSpacingAfter(ConstantsFont.LINE_SPACE);
			section.add(image);
		}
		final float[] columnsWidth;
		if (!previousEvaluationPageList.isEmpty()) {
			columnsWidth = new float[] { 30f, 25f, 30f, 15f };
		} else {
			columnsWidth = new float[] { 35f, 30f, 35f };
		}
		final PdfPTable table = new PdfPTable(columnsWidth);
		table.setWidthPercentage(90);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.num.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		if (!previousEvaluationPageList.isEmpty()) {
			table.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		}
		table.completeRow();
		final ListIterator<GraphicData> actualDataIterator = currentGlobalAccessibilityLevel.listIterator();
		final ListIterator<GraphicData> previousDataIterator = previousGlobalAccessibilityLevel.listIterator();
		assert currentGlobalAccessibilityLevel.size() == previousGlobalAccessibilityLevel.size();
		while (actualDataIterator.hasNext()) {
			final GraphicData actualData = actualDataIterator.next();
			final GraphicData previousData = previousDataIterator.hasNext() ? previousDataIterator.next() : actualData;
			table.addCell(PDFUtils.createTableCell(actualData.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(actualData.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(actualData.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			if (!previousEvaluationPageList.isEmpty()) {
				try {
					table.addCell(PDFUtils.createTableCell(new DecimalFormat("+0.00;-0.00").format(actualData.getRawPercentage().subtract(previousData.getRawPercentage())) + "%", Color.white,
							ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
				} catch (NumberFormatException nfe) {
					table.addCell(PDFUtils.createTableCell("Errror", Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
				}
			}
			table.completeRow();
		}
		if (!isBasicService) {
			table.setSpacingBefore(ConstantsFont.LINE_SPACE * 2f);
		} else {
			table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
		}
		section.add(table);
	}

	/**
	 * Adds the mids comparation by verification level graphic.
	 *
	 * @param pdfBuilder       the pdf builder
	 * @param messageResources the message resources
	 * @param section          the section
	 * @param file             the file
	 * @param evaList          the eva list
	 * @param noDataMess       the no data mess
	 * @param level            the level
	 * @throws Exception the exception
	 */
	private static void addMidsComparationByVerificationLevelGraphic(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final Section section, final File file,
			final List<ObservatoryEvaluationForm> evaList, final String noDataMess, final String level) throws Exception {
		final String title;
		final String filePath;
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.title");
			filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test2.jpg";
		} else { // if (level.equals(Constants.OBS_PRIORITY_2)) {
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.title");
			filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test3.jpg";
		}
		final PropertiesManager pmgr = new PropertiesManager();
		pdfBuilder.getMidsComparationByVerificationLevelGraphic(messageResources, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"),
				true);
		final Image image = PDFUtils.createImage(filePath, title);
		if (image != null) {
			image.scalePercent(60);
			section.add(image);
		}
	}

	/**
	 * Adds the results by page.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param file             the file
	 * @param evaList          the eva list
	 * @param noDataMess       the no data mess
	 * @throws Exception the exception
	 */
	private static void addResultsByPage(final MessageResources messageResources, final Chapter chapter, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess)
			throws Exception {
		final Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
		final SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("resultados.primarios.43.p1.anchor"), messageResources.getMessage("anchor.PMP"), false, ConstantsFont.ANCHOR_FONT);
		anchorMap.put(1, anchor);
		chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.43.p1"), anchorMap, ConstantsFont.PARAGRAPH));
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.43.p2"), ConstantsFont.PARAGRAPH, chapter);
		chapter.add(Chunk.NEWLINE);
		final String title = messageResources.getMessage("observatory.graphic.score.by.page.title");
		final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "test4.jpg";
		ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(messageResources, evaList, title, filePath, noDataMess);
		final Image image = PDFUtils.createImage(filePath, title);
		if (image != null) {
			image.scalePercent(70);
			image.setAlignment(Element.ALIGN_CENTER);
			chapter.add(image);
		}
		final float[] widths = { 33f, 33f, 33f };
		final PdfPTable table = new PdfPTable(widths);
		table.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.setHeaderRows(1);
		table.setKeepTogether(true);
		int pageCounter = 1;
		for (ObservatoryEvaluationForm evaluationForm : evaList) {
			table.addCell(
					PDFUtils.createLinkedTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", pageCounter), evaluationForm.getUrl(), Color.white, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(String.valueOf(evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1, RoundingMode.UNNECESSARY)), Color.white,
					ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), Color.white,
					ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			pageCounter++;
		}
		table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
		table.setSpacingAfter(0);
		chapter.add(table);
	}

	/**
	 * Creates the observatory verification score table.
	 *
	 * @param messageResources the message resources
	 * @param actualScore      the actual score
	 * @param previousScore    the previous score
	 * @param level            the level
	 * @param basicService     the basic service
	 * @return the pdf P table
	 */
	private static PdfPTable createObservatoryVerificationScoreTable(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level,
			boolean basicService) {
		if (!basicService && previousScore != null) {
			return createObservatoryVerificationScoreTableWithEvolution(messageResources, actualScore, previousScore, level);
		} else {
			return createObservatoryVerificationScoreTableNoEvolution(messageResources, actualScore, level);
		}
	}

	/**
	 * Creates the observatory verification score table no evolution.
	 *
	 * @param messageResources the message resources
	 * @param actualScore      the actual score
	 * @param level            the level
	 * @return the pdf P table
	 */
	private static PdfPTable createObservatoryVerificationScoreTableNoEvolution(final MessageResources messageResources, final ScoreForm actualScore, final String level) {
		final float[] columnsWidths = new float[] { 0.65f, 0.35f };
		final PdfPTable table = new PdfPTable(columnsWidths);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.setHeaderRows(1);
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			for (LabelValueBean actualLabelValueBean : actualScore.getVerifications1()) {
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.1"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel1().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			for (LabelValueBean actualLabelValueBean : actualScore.getVerifications2()) {
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.2"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel2().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		table.setSpacingBefore(ConstantsFont.LINE_SPACE);
		table.setSpacingAfter(0);
		return table;
	}

	/**
	 * Creates the observatory verification score table with evolution.
	 *
	 * @param messageResources the message resources
	 * @param actualScore      the actual score
	 * @param previousScore    the previous score
	 * @param level            the level
	 * @return the pdf P table
	 */
	private static PdfPTable createObservatoryVerificationScoreTableWithEvolution(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore,
			final String level) {
		final float[] columnsWidths = new float[] { 0.55f, 0.25f, 0.20f };
		final PdfPTable table = new PdfPTable(columnsWidths);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.setHeaderRows(1);
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			final ListIterator<LabelValueBean> actualScoreIterator = actualScore.getVerifications1().listIterator();
			final ListIterator<LabelValueBean> previousScoreIterator = previousScore.getVerifications1().listIterator();
			while (actualScoreIterator.hasNext()) {
				final LabelValueBean actualLabelValueBean = actualScoreIterator.next();
				final LabelValueBean previousLabelValueBean = previousScoreIterator.next();
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				table.addCell(createEvolutionDifferenceCellValue(actualLabelValueBean.getValue(), previousLabelValueBean.getValue(), Color.WHITE));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.1"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel1().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(createEvolutionDifferenceCellValue(actualScore.getScoreLevel1(), previousScore.getScoreLevel1(), Color.WHITE));
		} else if (level.equals(Constants.OBS_PRIORITY_2)) {
			final ListIterator<LabelValueBean> actualScoreIterator = actualScore.getVerifications2().listIterator();
			final ListIterator<LabelValueBean> previousScoreIterator = previousScore.getVerifications2().listIterator();
			while (actualScoreIterator.hasNext()) {
				final LabelValueBean actualLabelValueBean = actualScoreIterator.next();
				final LabelValueBean previousLabelValueBean = previousScoreIterator.next();
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				table.addCell(createEvolutionDifferenceCellValue(actualLabelValueBean.getValue(), previousLabelValueBean.getValue(), Color.WHITE));
			}
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.puntuacion.nivel.2"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualScore.getScoreLevel2().toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(createEvolutionDifferenceCellValue(actualScore.getScoreLevel2(), previousScore.getScoreLevel2(), Color.WHITE));
		}
		table.setSpacingBefore(ConstantsFont.LINE_SPACE);
		table.setSpacingAfter(0);
		return table;
	}

	/**
	 * Creates the evolution difference cell value.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @param color         the color
	 * @return the pdf P cell
	 */
	private static PdfPCell createEvolutionDifferenceCellValue(final String actualValue, final String previousValue, final Color color) {
		try {
			return createEvolutionDifferenceCellValue(new BigDecimal(actualValue), new BigDecimal(previousValue), color);
		} catch (NumberFormatException nfe) {
			return createEvolutionDifferenceCellValue(BigDecimal.ZERO, BigDecimal.ZERO, color);
		}
	}

	/**
	 * Creates the evolution difference cell value.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @param color         the color
	 * @return the pdf P cell
	 */
	private static PdfPCell createEvolutionDifferenceCellValue(final BigDecimal actualValue, final BigDecimal previousValue, final Color color) {
		return PDFUtils.createTableCell(getEvolutionImage(actualValue, previousValue), new DecimalFormat("+0.00;-0.00").format(actualValue.subtract(previousValue)), color, ConstantsFont.noteCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1);
	}
}
