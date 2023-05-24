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
package es.inteco.rastreador2.pdf;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.EvaluationForm;
import es.inteco.intav.form.GuidelineForm;
import es.inteco.intav.form.PautaForm;
import es.inteco.intav.form.PriorityForm;
import es.inteco.intav.form.ProblemForm;
import es.inteco.intav.form.SpecificProblemForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.pdf.template.ExportPageEventsObservatoryMP;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicsUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;
import es.inteco.utils.FileUtils;

/**
 * The Class BasicServiceExport.
 */
public final class BasicServiceExport {
	/** The Constant PMGR. */
	private static final PropertiesManager PMGR;
	/** The Constant SPACE_LINE. */
	private static final int SPACE_LINE = 15;
	/** The Constant X. */
	private static final int X;
	/** The Constant Y. */
	private static final int Y;
	/** The Constant COLOR. */
	private static final String COLOR;
	static {
		PMGR = new PropertiesManager();
		X = Integer.parseInt(PMGR.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.x"));
		Y = Integer.parseInt(PMGR.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.y"));
		COLOR = PMGR.getValue(CRAWLER_PROPERTIES, "chart.pdf.intav.colors");
	}

	/**
	 * Instantiates a new basic service export.
	 */
	private BasicServiceExport() {
	}

	/**
	 * Generate PDF.
	 *
	 * @param messageResources the message resources
	 * @param basicServiceForm the basic service form
	 * @param evaluationIds    the evaluation ids
	 * @param idCrawling       the id crawling
	 * @param pdfPath          the pdf path
	 * @throws Exception the exception
	 */
	public static void generatePDF(final MessageResources messageResources, BasicServiceForm basicServiceForm, List<Long> evaluationIds, Long idCrawling, String pdfPath) throws Exception {
		try {
			if (evaluationIds != null && !evaluationIds.isEmpty()) {
				createPdf(messageResources, evaluationIds, basicServiceForm, idCrawling, pdfPath);
			} else {
				Logger.putLog("No existen analisis en la base de datos de INTAV para la entidad: " + basicServiceForm.getName(), BasicServiceExport.class, Logger.LOG_LEVEL_WARNING);
			}
		} catch (Exception e) {
			Logger.putLog("Excepción al generar el PDF para la entidad: " + basicServiceForm.getName(), BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the result data.
	 *
	 * @param evaluationIds the evaluation ids
	 * @param language      the language
	 * @return the result data
	 * @throws Exception the exception
	 */
	public static Map<String, List<EvaluationForm>> getResultData(final List<Long> evaluationIds, final String language) throws Exception {
		final Map<String, List<EvaluationForm>> evolutionMap = new HashMap<>();
		try (Connection conn = DataBaseManager.getConnection()) {
			// Inicializamos el evaluador si hace falta
			if (!EvaluatorUtility.isInitialized()) {
				try {
					EvaluatorUtility.initialize();
				} catch (Exception e) {
					Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			final List<EvaluationForm> evaList = new ArrayList<>();
			for (Long id : evaluationIds) {
				final Evaluator evaluator = new Evaluator();
				Evaluation evaluation = evaluator.getAnalisisDB(conn, id, EvaluatorUtils.getDocList(), false);
				EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, language);
				evaList.add(evaluationForm);
			}
			final GregorianCalendar calendar = new GregorianCalendar();
			final Date d = calendar.getTime();
			final DateFormat df = new SimpleDateFormat(PMGR.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
			evolutionMap.put(df.format(d), evaList);
			return evolutionMap;
		} catch (Exception e) {
			Logger.putLog("Excepción genérica al generar el pdf", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Compress report.
	 *
	 * @param reportFile the report file
	 * @return the string
	 */
	public static String compressReport(final String reportFile) {
		final String reportCompressFile;
		if (reportFile.endsWith(".pdf")) {
			reportCompressFile = reportFile.substring(0, reportFile.length() - 4) + ".zip";
		} else {
			reportCompressFile = reportFile + ".zip";
		}
		final byte[] buffer = new byte[1024];
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(reportCompressFile)); BufferedInputStream in = new BufferedInputStream(new FileInputStream(reportFile))) {
			final ZipEntry ze = new ZipEntry(new File(reportFile).getName());
			zos.putNextEntry(ze);
			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			zos.closeEntry();
		} catch (Exception e) {
			Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
		}
		Logger.putLog("PDF comprimido a ZIP correctamente", BasicServiceExport.class, Logger.LOG_LEVEL_INFO);
		return reportCompressFile;
	}

	/**
	 * Compress report folder.
	 *
	 * @param reportFile        the report file
	 * @param isContentAnalysis the is content analysis
	 * @param filename          the filename
	 * @param depthReport       the depth report
	 * @return the string
	 */
	public static String compressReportWithCode(final String reportFile, final boolean isContentAnalysis, final String filename, final String depthReport) {
		final String reportCompressFile;
		if (reportFile.endsWith(".pdf")) {
			reportCompressFile = reportFile.substring(0, reportFile.length() - 4) + ".zip";
		} else {
			reportCompressFile = reportFile + ".zip";
		}
		final byte[] buffer = new byte[1024];
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(reportCompressFile)); BufferedInputStream in = new BufferedInputStream(new FileInputStream(reportFile))) {
			final ZipEntry ze = new ZipEntry(new File(reportFile).getName());
			zos.putNextEntry(ze);
			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			zos.closeEntry();
			try {
				File parentFile = new File(reportFile).getParentFile();
				zipFile(new File(parentFile + "/codigo_fuente.zip"), "codigo_fuente.zip", zos);
				// ODT
				File hallazgosFile = new File(parentFile + "/Informe Revision Accesibilidad - Hallazgos.odt");
				if (hallazgosFile.exists()) {
					zipFile(hallazgosFile, "Informe Revision Accesibilidad - Hallazgos.odt", zos);
				}
				// JSON WCAG-EM and ODS
				if ("true".equalsIgnoreCase(depthReport)) {
					// JSON
					zipFile(new File(parentFile + "/wcagem-report.json"), "wcagem-report.json", zos);
					// ODS
					zipFile(new File(parentFile + "/Informe Revision Accesibilidad - Sitios web.ods"), "Informe Revision Accesibilidad - Sitios web.ods", zos);
					// XLSX
					zipFile(new File(parentFile + "/Informe Revision Accesibilidad - Sitios web.xlsx"), "Informe Revision Accesibilidad - Sitios web.xlsx", zos);
				}
				zipFile(new File(parentFile + "/pagina_accesibilidad.html"), "pagina_accesibilidad.html", zos);
			} catch (Exception e) {
				Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
			}
		} catch (Exception e) {
			Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
		}
		Logger.putLog("PDF comprimido a ZIP correctamente", BasicServiceExport.class, Logger.LOG_LEVEL_INFO);
		return reportCompressFile;
	}

	/**
	 * Zip file.
	 *
	 * @param fileToZip the file to zip
	 * @param fileName  the file name
	 * @param zipOut    the zip out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.exists()) {
			if (fileToZip.isHidden()) {
				return;
			}
			if (fileToZip.isDirectory()) {
				if (fileName.endsWith("/")) {
					zipOut.putNextEntry(new ZipEntry(fileName));
					zipOut.closeEntry();
				} else {
					zipOut.putNextEntry(new ZipEntry(fileName + "/"));
					zipOut.closeEntry();
				}
				File[] children = fileToZip.listFiles();
				for (File childFile : children) {
					zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
				}
				return;
			}
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileName);
			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			zipOut.closeEntry();
			fis.close();
		}
	}

	/**
	 * Creates the pdf.
	 *
	 * @param messageResources the message resources
	 * @param evaluationIds    the evaluation ids
	 * @param basicServiceForm the basic service form
	 * @param idCrawling       the id crawling
	 * @param pdfPath          the pdf path
	 * @throws Exception the exception
	 */
	private static void createPdf(final MessageResources messageResources, List<Long> evaluationIds, BasicServiceForm basicServiceForm, Long idCrawling, String pdfPath) throws Exception {
		final Map<String, List<EvaluationForm>> resultData = getResultData(evaluationIds, basicServiceForm.getLanguage());
		final File file = new File(pdfPath);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("No se han podido crear los directorios para la exportación en PDF", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR);
		}
		List<EvaluationForm> evaList = new ArrayList<>();
		String fechaInforme = "";
		for (Map.Entry<String, List<EvaluationForm>> entry : resultData.entrySet()) {
			fechaInforme = entry.getKey();
			evaList = entry.getValue();
		}
		final Document document = new Document(PageSize.A4, 50, 50, 120, 72);
		final String chartsTempPath = new File(pdfPath).getParent() + File.separator + "temp" + File.separator;
		final SimpleDateFormat sdf = new SimpleDateFormat(PMGR.getValue(CRAWLER_PROPERTIES, "date.format.simple.pdf"));
		try {
			final FileOutputStream fileOut = new FileOutputStream(file);
			final PdfWriter writer = PdfWriter.getInstance(document, fileOut);
			writer.setTagged(0);
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			writer.setPageEvent(new ExportPageEventsObservatoryMP(
					messageResources.getMessage("pdf.accessibility.bs.foot.text") + basicServiceForm.getName().toUpperCase() + " (" + sdf.format(new Date()) + ")", sdf.format(new Date())));
			ExportPageEventsObservatoryMP.setPrintFooter(true);
			final IndexEvents index = new IndexEvents();
			writer.setPageEvent(index);
			writer.setLinearPageMode();
			document.open();
			PDFUtils.addCoverPage(document, messageResources.getMessage("pdf.accessibility.bs.title") + basicServiceForm.getName().toUpperCase(),
					messageResources.getMessage("pdf.accessibility.bs.subtitle"));
			int numChapter = 1;
			int countSections = 1;
			final Chapter introChapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("pdf.accessibility.bs.index.introduction"), index, countSections++, numChapter,
					ConstantsFont.CHAPTER_TITLE_MP_FONT);
			countSections = createSection11(messageResources, index, introChapter, countSections, evaList);
			countSections++;
			document.add(introChapter);
			numChapter++;
			countSections = createGlobalChapter(messageResources, document, evaList, chartsTempPath, index, fechaInforme, numChapter, countSections);
			numChapter++;
			Image imgProblemA = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.problem"));
			Image imgWarnings = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.warnings"));
			Image imgInfos = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.infos"));
			imgProblemA.setAlt("");
			imgWarnings.setAlt("");
			imgInfos.setAlt("");
			imgProblemA.scalePercent(60);
			imgWarnings.scalePercent(60);
			imgInfos.scalePercent(60);
			int counter = 1;
			for (EvaluationForm evaluationForm : evaList) {
				final String chapterTitle = messageResources.getMessage("basic.service.url.title", counter++);
				final Chapter chapter = PDFUtils.createChapterWithTitle(chapterTitle.toUpperCase(), index, countSections++, numChapter, ConstantsFont.CHAPTER_TITLE_MP_FONT, false);
				final String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
				final String url = evaluationForm.getUrl();
				final Paragraph urlParagraph = new Paragraph();
				if (title != null && !(title.replace("...", "")).contains(url.replace("...", ""))) {
					final Phrase p1 = PDFUtils.createPhrase(messageResources.getMessage("resultados.observatorio.vista.primaria.url") + ": ", ConstantsFont.SCORE_BOLD_FONT);
					final Font urlFont = url.length() > 50 ? ConstantsFont.descriptionFont : ConstantsFont.SCORE_FONT;
					final String urlTitle = url.length() > 75 ? url.substring(0, 75) + "..." : url;
					final Phrase p2 = PDFUtils.createPhraseLink(urlTitle, url, urlFont);
					urlParagraph.add(p1);
					urlParagraph.add(p2);
				}
				urlParagraph.setSpacingBefore(ConstantsFont.LINE_SPACE);
				chapter.add(urlParagraph);
				if (title != null) {
					final Phrase p3 = PDFUtils.createPhrase(messageResources.getMessage("resultados.observatorio.vista.primaria.title") + ": " + title, ConstantsFont.SCORE_BOLD_FONT);
					final Paragraph titleParagraph = new Paragraph();
					titleParagraph.add(p3);
					chapter.add(titleParagraph);
				}
				com.itextpdf.text.List summaryPriorities1 = addSummary(messageResources, evaluationForm.getPriorities());
				chapter.add(summaryPriorities1);
				generateGraphic(messageResources, evaluationForm.getPriorities(), messageResources.getMessage("pdf.accessibility.bs.priority.incidence"), chartsTempPath);
				Image imgGp = PDFUtils.createImage(chartsTempPath + messageResources.getMessage("pdf.accessibility.bs.priority.incidence") + ".jpg",
						messageResources.getMessage("pdf.accessibility.bs.priority.incidence"));
				if (imgGp != null) {
					chapter.add(imgGp);
				}
				chapter.newPage();
				int totalProblems = 0;
				for (PriorityForm priority : evaluationForm.getPriorities()) {
					totalProblems = priority.getNumInfos() + priority.getNumProblems() + priority.getNumWarnings();
				}
				if (totalProblems > 0) {
					chapter.add(createProblemsIndex(messageResources, evaluationForm.getPriorities()));
				}
				chapter.newPage();
				for (PriorityForm priority : evaluationForm.getPriorities()) {
					Section section = PDFUtils.createSection(getPriorityName(messageResources, priority), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections++, 1);
					for (GuidelineForm guideline : priority.getGuidelines()) {
						for (PautaForm pautaForm : guideline.getPautas()) {
							Section subSection = PDFUtils.createSection(messageResources.getMessage(pautaForm.getName()), null, ConstantsFont.guidelineDescMPFont, section, -1, 1);
							subSection.setNumberDepth(0);
							for (ProblemForm problem : pautaForm.getProblems()) {
								String description = "";
								Image image = null;
								com.itextpdf.text.Font font = null;
								if (problem.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
									description += messageResources.getMessage("pdf.accessibility.bs.warning") + ": ";
									image = imgWarnings;
									font = ConstantsFont.WARNING_FONT;
								} else if (problem.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
									description += messageResources.getMessage("pdf.accessibility.bs.problem") + ": ";
									image = imgProblemA;
									font = ConstantsFont.PROBLEM_FONT;
								} else if (problem.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
									description += messageResources.getMessage("pdf.accessibility.bs.info") + ": ";
									image = imgInfos;
									font = ConstantsFont.CANNOTTELL_FONT;
								}
								Paragraph p = PDFUtils.createImageParagraphWithDiferentFont(image, description, font, StringUtils.removeHtmlTags(messageResources.getMessage(problem.getError())),
										ConstantsFont.strongDescriptionFont, Chunk.ALIGN_LEFT);
								subSection.add(p);
								if (StringUtils.isNotEmpty(problem.getRationale())) {
									PDFUtils.addParagraphRationale(Arrays.asList(messageResources.getMessage(problem.getRationale()).split("<p>|</p>")), subSection);
								}
								addSpecificProblems(messageResources, subSection, problem.getSpecificProblems());
								if (problem.getCheck().equals("232") || // PMGR.getValue("check.properties", "doc.valida.especif")) ||
										EvaluatorUtils.isCssValidationCheck(Integer.parseInt(problem.getCheck()))) {
									addW3CCopyright(subSection, problem.getCheck());
								}
							}
						}
					}
				}
				document.add(chapter);
				numChapter++;
			}
			if (basicServiceForm.isContentAnalysis()) {
				// Añadir el código fuente analizado
				createContentChapter(messageResources, document, basicServiceForm.getContent(), index, numChapter, countSections);
			}
			IndexUtils.createIndex(writer, document, messageResources.getMessage("pdf.accessibility.index.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT);
			ExportPageEventsObservatoryMP.setPrintFooter(true);
			FileUtils.removeFile(chartsTempPath);
			Logger.putLog("PDF generado correctamente.", BasicServiceExport.class, Logger.LOG_LEVEL_INFO);
		} catch (Exception e) {
			Logger.putLog("Excepción genérica al generar el pdf", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			if (document != null && document.isOpen()) {
				try {
					document.close();
				} catch (Exception e) {
					Logger.putLog("Error al cerrar el pdf", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		}
	}

	/**
	 * Creates the priority list.
	 *
	 * @param evaList the eva list
	 * @return the list
	 */
	private static List<PriorityForm> createPriorityList(List<EvaluationForm> evaList) {
		PriorityForm priorityFormA = new PriorityForm();
		priorityFormA.setPriorityName(PMGR.getValue("intav.properties", ("priority.1.name")));
		PriorityForm priorityFormAA = new PriorityForm();
		priorityFormAA.setPriorityName(PMGR.getValue("intav.properties", ("priority.2.name")));
		for (EvaluationForm evaluationForm : evaList) {
			for (PriorityForm priority : evaluationForm.getPriorities()) {
				if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.1.name")))) {
					priorityFormA.setNumProblems(priorityFormA.getNumProblems() + priority.getNumProblems());
					priorityFormA.setNumWarnings(priorityFormA.getNumWarnings() + priority.getNumWarnings());
					priorityFormA.setNumInfos(priorityFormA.getNumInfos() + priority.getNumInfos());
				}
				if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.2.name")))) {
					priorityFormAA.setNumProblems(priorityFormAA.getNumProblems() + priority.getNumProblems());
					priorityFormAA.setNumWarnings(priorityFormAA.getNumWarnings() + priority.getNumWarnings());
					priorityFormAA.setNumInfos(priorityFormAA.getNumInfos() + priority.getNumInfos());
				}
			}
		}
		List<PriorityForm> prioList = new ArrayList<>();
		prioList.add(priorityFormA);
		prioList.add(priorityFormAA);
		return prioList;
	}

	/**
	 * Creates the problems index.
	 *
	 * @param messageResources the message resources
	 * @param priorityList     the priority list
	 * @return the com.itextpdf.text. list
	 * @throws BadElementException the bad element exception
	 * @throws IOException         Signals that an I/O exception has occurred.
	 */
	private static com.itextpdf.text.List createProblemsIndex(final MessageResources messageResources, List<PriorityForm> priorityList) throws BadElementException, IOException {
		final com.itextpdf.text.List priorityTextList = new com.itextpdf.text.List(false, 10);
		for (PriorityForm priority : priorityList) {
			final com.itextpdf.text.List problemList = new com.itextpdf.text.List(false, 10);
			final com.itextpdf.text.List warningList = new com.itextpdf.text.List(false, 10);
			final com.itextpdf.text.List infoList = new com.itextpdf.text.List(false, 10);
			for (GuidelineForm guideline : priority.getGuidelines()) {
				boolean hasProblem = false;
				boolean hasWarning = false;
				boolean hasInfos = false;
				for (PautaForm pautaForm : guideline.getPautas()) {
					for (ProblemForm problemForm : pautaForm.getProblems()) {
						if (problemForm.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high")) && !hasProblem) {
							problemList.add(PDFUtils.createListItem(messageResources.getMessage(pautaForm.getName()), ConstantsFont.PARAGRAPH, ConstantsFont.LIST_SYMBOL_2, false));
							hasProblem = true;
						}
						if (problemForm.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium")) && !hasWarning) {
							warningList.add(PDFUtils.createListItem(messageResources.getMessage(pautaForm.getName()), ConstantsFont.PARAGRAPH, ConstantsFont.LIST_SYMBOL_2, false));
							hasWarning = true;
						}
						if (problemForm.getType().equals(PMGR.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell")) && !hasInfos) {
							infoList.add(PDFUtils.createListItem(messageResources.getMessage(pautaForm.getName()), ConstantsFont.PARAGRAPH, ConstantsFont.LIST_SYMBOL_2, false));
							hasInfos = true;
						}
					}
				}
			}
			final ListItem priorityItem = PDFUtils.createListItem(messageResources.getMessage(priority.getPriorityName().replace(" ", "").toLowerCase() + ".bs"), ConstantsFont.paragraphBoldTitleFont,
					ConstantsFont.LIST_SYMBOL_1, true);
			priorityItem.add(createProblemList(messageResources, problemList, warningList, infoList, priority));
			priorityTextList.add(priorityItem);
		}
		return priorityTextList;
	}

	/**
	 * Creates the problem list.
	 *
	 * @param messageResources the message resources
	 * @param problemList      the problem list
	 * @param warningList      the warning list
	 * @param infoList         the info list
	 * @param priority         the priority
	 * @return the com.itextpdf.text. list
	 * @throws BadElementException the bad element exception
	 * @throws IOException         Signals that an I/O exception has occurred.
	 */
	private static com.itextpdf.text.List createProblemList(final MessageResources messageResources, com.itextpdf.text.List problemList, com.itextpdf.text.List warningList,
			com.itextpdf.text.List infoList, PriorityForm priority) throws BadElementException, IOException {
		com.itextpdf.text.List pwiList = new com.itextpdf.text.List();
		Image imgProblemA = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.problem"));
		Image imgWarnings = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.warnings"));
		Image imgInfos = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.infos"));
		imgProblemA.setAlt("");
		imgWarnings.setAlt("");
		imgInfos.setAlt("");
		imgProblemA.scalePercent(60);
		imgWarnings.scalePercent(60);
		imgInfos.scalePercent(60);
		ListItem problemItem = PDFUtils.createListItem(
				messageResources.getMessage("pdf.accessibility.bs.problems") + ": " + priority.getNumProblems() + messageResources.getMessage("pdf.accessibility.bs.ver.point", problemList.size()),
				ConstantsFont.PROBLEM_FONT, new Chunk(imgProblemA, 0, 0), false);
		ListItem warningItem = PDFUtils.createListItem(
				messageResources.getMessage("pdf.accessibility.bs.warnings") + ": " + priority.getNumWarnings() + messageResources.getMessage("pdf.accessibility.bs.ver.point", warningList.size()),
				ConstantsFont.WARNING_FONT, new Chunk(imgWarnings, 0, 0), false);
		ListItem infoItem = PDFUtils.createListItem(
				messageResources.getMessage("pdf.accessibility.bs.infos") + ": " + priority.getNumInfos() + messageResources.getMessage("pdf.accessibility.bs.ver.point", infoList.size()),
				ConstantsFont.CANNOTTELL_FONT, new Chunk(imgInfos, 0, 0), false);
		problemItem.setSpacingBefore(8);
		warningItem.setSpacingBefore(8);
		infoItem.setSpacingBefore(8);
		problemItem.add(problemList);
		warningItem.add(warningList);
		infoItem.add(infoList);
		pwiList.add(problemItem);
		pwiList.add(warningItem);
		pwiList.add(infoItem);
		pwiList.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
		return pwiList;
	}

	/**
	 * Creates the section 11.
	 *
	 * @param messageResources the message resources
	 * @param index            the index
	 * @param chapter          the chapter
	 * @param countSections    the count sections
	 * @param evalFormList     the eval form list
	 * @return the int
	 * @throws Exception the exception
	 */
	private static int createSection11(final MessageResources messageResources, final IndexEvents index, final Chapter chapter, final int countSections, final List<EvaluationForm> evalFormList)
			throws Exception {
		final Section section = PDFUtils.createSection(messageResources.getMessage("pdf.accessibility.bs.index.introduction.1"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections,
				1);
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("basic.service.introduction.1.p1.bold"));
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("basic.service.introduction.1.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH,
				true));
		PDFUtils.addParagraph(messageResources.getMessage("basic.service.introduction.1.p2"), ConstantsFont.PARAGRAPH, section);
		section.add(addURLTable(messageResources, evalFormList));
		return countSections;
	}

	/**
	 * Adds the URL table.
	 *
	 * @param messageResources      the message resources
	 * @param primaryReportPageList the primary report page list
	 * @return the pdf P table
	 */
	private static PdfPTable addURLTable(final MessageResources messageResources, final java.util.List<EvaluationForm> primaryReportPageList) {
		final float[] widths = { 30f, 70f };
		final PdfPTable table = new PdfPTable(widths);
		table.setSpacingBefore(ConstantsFont.LINE_SPACE);
		table.setWidthPercentage(100);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("basic.service.introduction.1.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("basic.service.introduction.1.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		int i = 1;
		for (EvaluationForm page : primaryReportPageList) {
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", i++), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
			table.addCell(PDFUtils.createLinkedTableCell(page.getUrl(), page.getUrl(), Color.WHITE, Element.ALIGN_LEFT, 0));
		}
		return table;
	}

	/**
	 * Creates the global chapter.
	 *
	 * @param messageResources the message resources
	 * @param d                the d
	 * @param evaList          the eva list
	 * @param globalPath       the global path
	 * @param index            the index
	 * @param fechaInforme     the fecha informe
	 * @param numChapter       the num chapter
	 * @param countSections    the count sections
	 * @return the int
	 * @throws Exception the exception
	 */
	private static int createGlobalChapter(final MessageResources messageResources, Document d, List<EvaluationForm> evaList, String globalPath, IndexEvents index, String fechaInforme, int numChapter,
			int countSections) throws Exception {
		int numSections = countSections;
		Chapter chapter1 = PDFUtils.createChapterWithTitle(messageResources.getMessage("pdf.accessibility.bs.index.global.summary"), index, numSections++, numChapter,
				ConstantsFont.CHAPTER_TITLE_MP_FONT);
		ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("pdf.accessibility.bs.entity"));
		chapter1.add(
				PDFUtils.createParagraphWithDiferentFormatWord("{0}" + EvaluatorUtils.getEntityName(evaList), boldWords, ConstantsFont.paragraphBoldTitleFont, ConstantsFont.paragraphTitleFont, true));
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("pdf.accessibility.bs.domain"));
		chapter1.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + evaList.get(0).getUrl(), boldWords, ConstantsFont.paragraphBoldTitleFont, ConstantsFont.paragraphTitleFont, false));
		boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("pdf.accessibility.bs.date"));
		chapter1.add(PDFUtils.createParagraphWithDiferentFormatWord("{0}" + fechaInforme, boldWords, ConstantsFont.paragraphBoldTitleFont, ConstantsFont.paragraphTitleFont, false));
		List<PriorityForm> prioList = createPriorityList(evaList);
		com.itextpdf.text.List summaryPriorities = addSummary(messageResources, prioList);
		chapter1.add(summaryPriorities);
		generateGraphic(messageResources, prioList, messageResources.getMessage("pdf.accessibility.bs.global.priority.incidence"), globalPath);
		Image globalImgGp = PDFUtils.createImage(globalPath + messageResources.getMessage("pdf.accessibility.bs.global.priority.incidence") + ".jpg",
				messageResources.getMessage("pdf.accessibility.bs.global.priority.incidence"));
		if (globalImgGp != null) {
			chapter1.add(globalImgGp);
		}
		d.add(chapter1);
		return numSections;
	}

	/**
	 * Creates the content chapter.
	 *
	 * @param messageResources the message resources
	 * @param d                the d
	 * @param contents         the contents
	 * @param index            the index
	 * @param numChapter       the num chapter
	 * @param countSections    the count sections
	 * @return the int
	 * @throws Exception the exception
	 */
	private static int createContentChapter(final MessageResources messageResources, Document d, String contents, IndexEvents index, int numChapter, int countSections) throws Exception {
		int numSections = countSections;
		Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("basic.service.content.title"), index, numSections++, numChapter, ConstantsFont.CHAPTER_TITLE_MP_FONT);
		PDFUtils.addParagraph(messageResources.getMessage("basic.service.content.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, true);
		PDFUtils.addCode(HTMLEntities.unhtmlAngleBrackets(contents), chapter);
		d.add(chapter);
		return numSections;
	}

	/**
	 * Adds the summary.
	 *
	 * @param messageResources the message resources
	 * @param prioList         the prio list
	 * @return the com.itextpdf.text. list
	 * @throws BadElementException the bad element exception
	 * @throws IOException         Signals that an I/O exception has occurred.
	 */
	private static com.itextpdf.text.List addSummary(final MessageResources messageResources, final List<PriorityForm> prioList) throws BadElementException, IOException {
		com.itextpdf.text.List summaryPriorities = new com.itextpdf.text.List();
		int count = prioList.size();
		for (PriorityForm priority : prioList) {
			ListItem priorityList = new ListItem(getPriorityName(messageResources, priority), ConstantsFont.SUMMARY_TITLE_FONT);
			priorityList.setSpacingBefore(8);
			priorityList.setListSymbol(new Chunk());
			summaryPriorities.add(priorityList);
			com.itextpdf.text.List summaryStatistics = new com.itextpdf.text.List();
			summaryStatistics.setIndentationLeft(2 * ConstantsFont.IDENTATION_LEFT_SPACE);
			ListItem problemList = new ListItem(messageResources.getMessage("pdf.accessibility.bs.problems") + ": " + priority.getNumProblems(), ConstantsFont.SUMMARY_FONT_PROBLEM);
			ListItem warningsList = new ListItem(messageResources.getMessage("pdf.accessibility.bs.warnings") + ": " + priority.getNumWarnings(), ConstantsFont.SUMMARY_FONT_WARNING);
			ListItem infosList = new ListItem(messageResources.getMessage("pdf.accessibility.bs.infos") + ": " + priority.getNumInfos(), ConstantsFont.SUMMARY_FONT_CANNOTTELL);
			Image imgProblemA = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.problem"));
			Image imgWarnings = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.warnings"));
			Image imgInfos = Image.getInstance(PMGR.getValue(Constants.PDF_PROPERTIES, "path.infos"));
			imgProblemA.setAlt("");
			imgWarnings.setAlt("");
			imgInfos.setAlt("");
			imgProblemA.scalePercent(60);
			imgWarnings.scalePercent(60);
			imgInfos.scalePercent(60);
			problemList.setListSymbol(new Chunk(imgProblemA, 0, 0));
			problemList.setSpacingBefore(8);
			warningsList.setListSymbol(new Chunk(imgWarnings, 0, 0));
			warningsList.setSpacingBefore(8);
			infosList.setListSymbol(new Chunk(imgInfos, 0, 0));
			infosList.setSpacingBefore(8);
			count--;
			if (count == 0) {
				infosList.setSpacingAfter(SPACE_LINE);
			}
			summaryStatistics.add(problemList);
			summaryStatistics.add(warningsList);
			summaryStatistics.add(infosList);
			summaryPriorities.add(summaryStatistics);
		}
		return summaryPriorities;
	}

	/**
	 * Gets the priority name.
	 *
	 * @param messageResources the message resources
	 * @param priority         the priority
	 * @return the priority name
	 */
	private static String getPriorityName(final MessageResources messageResources, final PriorityForm priority) {
		if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", "priority.1.name"))) {
			return messageResources.getMessage("first.level.incidents.bs");
		} else if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", "priority.2.name"))) {
			return messageResources.getMessage("second.level.incidents.bs");
		} else {
			return messageResources.getMessage("third.level.incidents.bs");
		}
	}

	/**
	 * Generate graphic.
	 *
	 * @param messageResources the message resources
	 * @param priorityFormList the priority form list
	 * @param tableName        the table name
	 * @param tempPath         the temp path
	 */
	private static void generateGraphic(final MessageResources messageResources, List<PriorityForm> priorityFormList, String tableName, String tempPath) {
		final DefaultCategoryDataset priorityDataSet = new DefaultCategoryDataset();
		for (PriorityForm priority : priorityFormList) {
			if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.1.name")))) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), messageResources.getMessage("chart.problems.bs"), messageResources.getMessage("first.level.incidents.export.bs"));
					priorityDataSet.addValue(priority.getNumWarnings(), messageResources.getMessage("chart.warnings.bs"), messageResources.getMessage("first.level.incidents.export.bs"));
					priorityDataSet.addValue(priority.getNumInfos(), messageResources.getMessage("chart.infos.bs"), messageResources.getMessage("first.level.incidents.export.bs"));
				}
			} else if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.2.name")))) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), messageResources.getMessage("chart.problems.bs"), messageResources.getMessage("second.level.incidents.export.bs"));
					priorityDataSet.addValue(priority.getNumWarnings(), messageResources.getMessage("chart.warnings.bs"), messageResources.getMessage("second.level.incidents.export.bs"));
					priorityDataSet.addValue(priority.getNumInfos(), messageResources.getMessage("chart.infos.bs"), messageResources.getMessage("second.level.incidents.export.bs"));
				}
			} else if (priority.getPriorityName().equals(PMGR.getValue("intav.properties", ("priority.3.name")))) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), messageResources.getMessage("chart.problems.bs"), messageResources.getMessage("third.level.incidents.export.bs"));
					priorityDataSet.addValue(priority.getNumWarnings(), messageResources.getMessage("chart.warnings.bs"), messageResources.getMessage("third.level.incidents.export.bs"));
					priorityDataSet.addValue(priority.getNumInfos(), messageResources.getMessage("chart.infos.bs"), messageResources.getMessage("third.level.incidents.export.bs"));
				}
			}
		}
		try {
			final String rowTitle = messageResources.getMessage("chart.intav.bs.rowTitle");
			final ChartForm chart = new ChartForm(tableName, "", rowTitle, priorityDataSet, true, true, false, false, true, false, false, X, Y, COLOR);
			GraphicsUtils.createSeriesBarChart(chart, tempPath + tableName + ".jpg", "", messageResources, false);
		} catch (Exception e) {
			Logger.putLog("Exception: ", BasicServiceExport.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Adds the specific problems.
	 *
	 * @param messageResources the message resources
	 * @param subSubSection    the sub sub section
	 * @param specificProblems the specific problems
	 */
	public static void addSpecificProblems(final MessageResources messageResources, final Section subSubSection, final List<SpecificProblemForm> specificProblems) {
		final float[] widths = { 8f, 12f, 80f };
		final PdfPTable table = new PdfPTable(widths);
		table.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.setWidthPercentage(86);
		table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
		table.setSpacingAfter(ConstantsFont.HALF_LINE_SPACE);
		table.addCell(PDFUtils.createTableCell("Línea", Constants.GRIS_MUY_CLARO, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell("Columna", Constants.GRIS_MUY_CLARO, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell("Código", Constants.GRIS_MUY_CLARO, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		// Indicamos que la primera fila es de encabezados para que la repita si la tabla se parte en varias páginas.
		table.setHeaderRows(1);
		int maxNumErrors = Integer.parseInt(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"));
		for (SpecificProblemForm specificProblem : specificProblems) {
			maxNumErrors--;
			if (specificProblem.getCode() != null) {
				final StringBuilder code = new StringBuilder();
				for (int i = 0; i < specificProblem.getCode().size(); i++) {
					code.append(specificProblem.getCode().get(i)).append("\n");
				}
				if (!specificProblem.getLine().isEmpty() && !"-1".equalsIgnoreCase(specificProblem.getLine())) {
					table.addCell(PDFUtils.createTableCell(specificProblem.getLine(), Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
					table.addCell(PDFUtils.createTableCell(specificProblem.getColumn(), Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
				} else {
					table.addCell(PDFUtils.createTableCell("-", Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
					table.addCell(PDFUtils.createTableCell("-", Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
				}
				String text = HTMLEntities.unhtmlAngleBrackets(code.toString());
				final String message = specificProblem.getMessage();
				java.util.List<String> boldWords = new ArrayList<>();
				if (!StringUtils.isEmpty(message)) {
					text = "{0} \n\n" + text.trim();
					boldWords.add(message);
				}
				final PdfPCell labelCell = new PdfPCell(PDFUtils.createParagraphWithDiferentFormatWord(text, boldWords, ConstantsFont.codeCellFont, ConstantsFont.codeCellFont, false));
				labelCell.setPadding(0);
				labelCell.setBackgroundColor(new BaseColor(255, 244, 223));
				labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				labelCell.setPadding(DEFAULT_PADDING);
				table.addCell(labelCell);
			} else if (specificProblem.getNote() != null) {
				final String linkCode = getMatch(specificProblem.getNote().get(0), "(<a.*?</a>)");
				final String paragraphText = specificProblem.getNote().get(0).replace(linkCode, "");
				final String linkHref = getMatch(specificProblem.getNote().get(0), "href='(.*?)'");
				final Paragraph p = new Paragraph(paragraphText, ConstantsFont.noteCellFont);
				final Anchor anchor = new Anchor(getMatch(specificProblem.getNote().get(0), "<a.*?>(.*?)</a>"), ConstantsFont.NOTE_ANCHOR_CELL_FONT);
				anchor.setReference(linkHref);
				p.add(anchor);
				subSubSection.add(p);
			}
			if (maxNumErrors < 0) {
				if (specificProblems.size() > Integer.parseInt(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"))) {
					final String[] arguments = new String[2];
					arguments[0] = PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number");
					arguments[1] = String.valueOf(specificProblems.size());
					final Paragraph p = new Paragraph(messageResources.getMessage("pdf.accessibility.bs.num.errors.summary", arguments), ConstantsFont.MORE_INFO_FONT);
					p.setAlignment(Paragraph.ALIGN_RIGHT);
					subSubSection.add(p);
				}
				break;
			}
		}
		subSubSection.add(table);
	}

	/**
	 * Gets the match.
	 *
	 * @param text   the text
	 * @param regexp the regexp
	 * @return the match
	 */
	private static String getMatch(String text, String regexp) {
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(text);
		return matcher.find() ? matcher.group(1) : null;
	}

	/**
	 * Adds the W 3 C copyright.
	 *
	 * @param subSubSection the sub sub section
	 * @param check         the check
	 */
	private static void addW3CCopyright(Section subSubSection, String check) {
		Paragraph p = new Paragraph();
		p.setAlignment(Paragraph.ALIGN_RIGHT);
		Anchor anchor = null;
		if (check.equals("232")) { // PMGR.getValue("check.properties", "doc.valida.especif"))) {
			anchor = new Anchor(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright"), ConstantsFont.MORE_INFO_FONT);
			anchor.setReference(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
		} else if (EvaluatorUtils.isCssValidationCheck(Integer.parseInt(check))) {
			anchor = new Anchor(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.css.copyright"), ConstantsFont.MORE_INFO_FONT);
			anchor.setReference(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
		}
		p.add(anchor);
		subSubSection.add(p);
	}
}
