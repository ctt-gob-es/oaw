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
package es.gob.oaw.rastreador2.pdf.basicservice;

import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.util.MessageResources;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.IndexEvents;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceAnalysisType;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfAccesibilidad;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2012b;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNEEN2019;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;

/**
 * Clase para generar el informe PDF del servicio de diagnóstico.
 *
 * @author miguel.garcia <miguel.garcia@fundacionctic.org>
 */
public class BasicServicePdfReport {
	/** The pdf builder. */
	private final AnonymousResultExportPdf pdfBuilder;
	/** The message resources. */
	private final MessageResources messageResources;

	/**
	 * Instantiates a new basic service pdf report.
	 *
	 * @param pdfBuilder the pdf builder
	 */
	public BasicServicePdfReport(final AnonymousResultExportPdf pdfBuilder) {
		// Fichero de textos en fucnión de builder para aplicar los nuevos
		// textos al nuevo builder
		this(extracted(pdfBuilder), pdfBuilder);
	}

	/**
	 * Extracted.
	 *
	 * @param pdfBuilder the pdf builder
	 * @return the message resources
	 */
	private static MessageResources extracted(final AnonymousResultExportPdf pdfBuilder) {
		MessageResources messageResources = null;
		if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
			messageResources = messageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
		}
		if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
			messageResources = messageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
		}
		if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
			messageResources = messageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B);
		}
		if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
			messageResources = MessageResources.getMessageResources("ApplicationResources");
		}
		return messageResources;
	}

	/**
	 * Instantiates a new basic service pdf report.
	 *
	 * @param messageResources the message resources
	 * @param pdfBuilder       the pdf builder
	 */
	public BasicServicePdfReport(final MessageResources messageResources, final AnonymousResultExportPdf pdfBuilder) {
		if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
			this.messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
		} else if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
			this.messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
		} else if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
			this.messageResources = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_2012_B);
		} else {
			this.messageResources = messageResources;
		}
		this.pdfBuilder = pdfBuilder;
		this.pdfBuilder.setBasicService(true);
	}

	/**
	 * Export to pdf.
	 *
	 * @param currentEvaluationPageList   the current evaluation page list
	 * @param historicoEvaluationPageList the historico evaluation page list
	 * @param generalExpPath              the general exp path
	 * @throws Exception the exception
	 */
	public void exportToPdf(final List<ObservatoryEvaluationForm> currentEvaluationPageList, final Map<Date, List<ObservatoryEvaluationForm>> historicoEvaluationPageList, final String generalExpPath)
			throws Exception {
		final File file = new File(generalExpPath);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			if (!file.getParentFile().setReadable(true, false) || !file.getParentFile().setWritable(true, false)) {
				Logger.putLog(String.format("No se ha podido asignar permisos a los directorios de exportación a PDF %s", file.getParentFile().getAbsolutePath()), BasicServicePdfReport.class,
						Logger.LOG_LEVEL_ERROR);
			}
			Logger.putLog("No se ha podido crear los directorios para exportar a PDF", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
		}
		Logger.putLog("Exportando a PDF BasicServicePdfReport.exportToPdf", BasicServicePdfReport.class, Logger.LOG_LEVEL_DEBUG);
		// PENDING Add document metadata (author, creator, subject, title...)
		final Document document = new Document(PageSize.A4, 50, 50, 110, 72);
		// document.addAuthor("Ministerio de Hacienda y Función Pública");
		// document.addCreationDate();
		// document.addCreator("OAW - Observatorio de Accesibilidad Web");
		try (FileOutputStream outputFileStream = new FileOutputStream(file)) {
			try {
				MessageResources messageResources2019 = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_UNE_EN2019);
				MessageResources messageResourcesAccesibility = MessageResources.getMessageResources(Constants.MESSAGE_RESOURCES_ACCESIBILIDAD);
				final PdfWriter writer = PdfWriter.getInstance(document, outputFileStream);
				writer.setTagged(0);
				writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
				writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));
				final String crawlingDate = CrawlerUtils.formatDate(pdfBuilder.getBasicServiceForm().getDate());
				final String footerText = messageResources.getMessage("ob.resAnon.intav.report.foot.basic.service", new String[] { crawlingDate });
				writer.setPageEvent(new ExportPageEventsObservatoryMP(footerText, crawlingDate));
				ExportPageEventsObservatoryMP.setPrintFooter(true);
				final PdfTocManager pdfTocManager = createPdfTocManager(writer);
				document.open();
				// Preserve "old" cover and add new cover for new cartidges
				if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
					PDFUtils.addNewCoverPage(document, messageResourcesAccesibility.getMessage("pdf.accessibility.title.basic.service"),
							messageResourcesAccesibility.getMessage("pdf.accessibility.title.basic.service.2"),
							pdfBuilder.getBasicServiceForm().getAnalysisType() == BasicServiceAnalysisType.URL ? pdfBuilder.getBasicServiceForm().getDomain() : "",
							messageResourcesAccesibility.getMessage("pdf.accessibility.on.demand"), "");
				} else if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
					String subtitle = "";
					switch (pdfBuilder.getBasicServiceForm().getAnalysisType()) {
					case URL:
						subtitle = messageResources2019.getMessage("pdf.accessibility.cover.type.url", new String[] { pdfBuilder.getBasicServiceForm().getDomain() });
						break;
					case CODIGO_FUENTE:
					case CODIGO_FUENTE_MULTIPLE:
						subtitle = messageResources2019.getMessage("pdf.accessibility.cover.type.source", new String[] { pdfBuilder.getBasicServiceForm().getFileName() });
						break;
					case LISTA_URLS:
						subtitle = messageResources2019.getMessage("pdf.accessibility.cover.type.url.list");
						break;
					case MIXTO:
						subtitle = messageResources2019.getMessage("pdf.accessibility.cover.type.mix");
						break;
					default:
						break;
					}
					PDFUtils.addNewCoverPage(document, messageResources2019.getMessage("pdf.accessibility.title.basic.service"),
							messageResources2019.getMessage("pdf.accessibility.title.basic.service.2"), subtitle, messageResources2019.getMessage("pdf.accessibility.cover.notice.1"),
							messageResources2019.getMessage("pdf.accessibility.cover.notice.2"));
				} else {
					PDFUtils.addCoverPage(document,
							messageResources.getMessage("pdf.accessibility.title", new String[] { pdfBuilder.getBasicServiceForm().getName().toUpperCase(), pdfBuilder.getTitle() }),
							pdfBuilder.getBasicServiceForm().getAnalysisType() == BasicServiceAnalysisType.URL ? pdfBuilder.getBasicServiceForm().getDomain() : "", "Informe emitido bajo demanda.");
				}
				pdfBuilder.createIntroductionChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, true);
				pdfTocManager.addChapterCount();
				// Muestra de páginas
				if (!(pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad)) {
					pdfBuilder.createObjetiveChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0);
					pdfTocManager.addChapterCount();
				}
				// Resumen de resultados
				final List<ObservatoryEvaluationForm> previousEvaluation = getPreviousEvaluation(historicoEvaluationPageList);
				final BasicServiceObservatoryScorePdfSectionBuilder observatoryScoreSectionBuilder = new BasicServiceObservatoryScorePdfSectionBuilder(currentEvaluationPageList, previousEvaluation);
				final BasicServicePageResultsPdfSectionBuilder observatoryPageResultsSectionBuilder = new BasicServicePageResultsPdfSectionBuilder(currentEvaluationPageList);
				if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
					observatoryScoreSectionBuilder.addObservatoryScoreSummary(pdfBuilder, messageResources2019, document, pdfTocManager, file);
				} else if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
					observatoryScoreSectionBuilder.addObservatoryScoreSummary(pdfBuilder, messageResourcesAccesibility, document, pdfTocManager, file);
					// observatoryPageResultsSectionBuilder.addPageResultsAccesibility(messageResourcesAccesibility, document, pdfTocManager, true);
				} else {
					observatoryScoreSectionBuilder.addObservatoryScoreSummary(pdfBuilder, messageResources, document, pdfTocManager, file);
				}
				// Resultados por verificación
				final BasicServiceObservatoryResultsSummaryPdfSectionBuilder observatoryResultsSummarySectionBuilder = new BasicServiceObservatoryResultsSummaryPdfSectionBuilder(
						currentEvaluationPageList);
				if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
					observatoryResultsSummarySectionBuilder.addObservatoryResultsSummaryWithCompliance(messageResources, document, pdfTocManager,
							pdfBuilder.generateScores(messageResources, currentEvaluationPageList));
				} else if (!(pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad)) {
					observatoryResultsSummarySectionBuilder.addObservatoryResultsSummary(messageResources, document, pdfTocManager);
				}
				// Evolución resultados servicio diagnóstico
				final Map<Date, List<ObservatoryEvaluationForm>> evolutionObservatoryEvaluation = new TreeMap<>(historicoEvaluationPageList);
				evolutionObservatoryEvaluation.put(pdfBuilder.getBasicServiceForm().getDate(), currentEvaluationPageList);
				final BasicServiceObservatoryEvolutionResultsPdfSectionBuilder observatoryEvolutionResultsSectionBuilder = new BasicServiceObservatoryEvolutionResultsPdfSectionBuilder(
						evolutionObservatoryEvaluation);
				observatoryEvolutionResultsSectionBuilder.addEvolutionResults(pdfBuilder, messageResources, document, pdfTocManager, file);
				// Desdoblamiento para la nueva versión de la metodología
				// UNE-2012 ya que cambian los niveles
				if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
					observatoryPageResultsSectionBuilder.addPageResults(messageResources2019, document, pdfTocManager, true);
					pdfBuilder.createMethodologyChapter(messageResources2019, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0, pdfBuilder.isBasicService());
				} else if (pdfBuilder instanceof AnonymousResultExportPdfUNE2012b) {
					observatoryPageResultsSectionBuilder.addPageResults(messageResources, document, pdfTocManager, true);
					pdfBuilder.createMethodologyChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0, pdfBuilder.isBasicService());
				} else if (!(pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad)) {
					observatoryPageResultsSectionBuilder.addPageResults(messageResources, document, pdfTocManager, false);
					pdfBuilder.createMethodologyChapter(messageResources, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0, pdfBuilder.isBasicService());
				} else if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
					pdfBuilder.createMethodologyChapter(messageResourcesAccesibility, document, pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT, currentEvaluationPageList, 0,
							pdfBuilder.isBasicService());
				}
				// Ponemos la variable a true para que no se escriba el footer
				// en el índice
				IndexUtils.createIndex(writer, document, messageResources.getMessage("pdf.accessibility.index.title"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT);
				ExportPageEventsObservatoryMP.setPrintFooter(true);
			} catch (DocumentException e) {
				Logger.putLog("Error al exportar a pdf", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			} catch (Exception e) {
				Logger.putLog("Excepción genérica al generar el pdf", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			} finally {
				if (document.isOpen()) {
					document.close();
				}
			}
		}
	}

	/**
	 * Gets the previous evaluation.
	 *
	 * @param previousEvaluationPageList the previous evaluation page list
	 * @return the previous evaluation
	 */
	private List<ObservatoryEvaluationForm> getPreviousEvaluation(final Map<Date, List<ObservatoryEvaluationForm>> previousEvaluationPageList) {
		final Iterator<List<ObservatoryEvaluationForm>> iterator = previousEvaluationPageList.values().iterator();
		List<ObservatoryEvaluationForm> previousEvaluation = null;
		while (iterator.hasNext()) {
			previousEvaluation = iterator.next();
		}
		return previousEvaluation;
	}

	/**
	 * Creates the pdf toc manager.
	 *
	 * @param writer the writer
	 * @return the pdf toc manager
	 */
	private PdfTocManager createPdfTocManager(final PdfWriter writer) {
		final IndexEvents index = new IndexEvents();
		writer.setPageEvent(index);
		writer.setLinearPageMode();
		return new PdfTocManager(index);
	}

	/**
	 * Crea una celda PdfPCell para una tabla del informa PDF con la evolución del nivel de accesibilidad.
	 *
	 * @param messageResources the message resources
	 * @param currentLevel     String nivel de accesibilidad actual.
	 * @param previousLevel    String nivel de accesibilidad de la iteración anterior.
	 * @return una celda PdfPCell con una imagen que indica la evolución y una cadena con la misma información complementando la imagen.
	 */
	private PdfPCell createEvolutionLevelCell(final MessageResources messageResources, final String currentLevel, final String previousLevel) {
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
}
