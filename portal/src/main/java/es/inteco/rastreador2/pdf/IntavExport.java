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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
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
import es.inteco.rastreador2.actionform.rastreo.FulfilledCrawlingForm;
import es.inteco.rastreador2.dao.rastreo.RastreoDAO;
import es.inteco.rastreador2.intav.utils.ISPDFGuidelineForm;
import es.inteco.rastreador2.intav.utils.ISPDFPautaForm;
import es.inteco.rastreador2.intav.utils.ISPDFProblemForm;
import es.inteco.rastreador2.intav.utils.IntavSimplePDFForm;
import es.inteco.rastreador2.pdf.template.ExportPageEvents;
import es.inteco.rastreador2.pdf.utils.IndexUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicsUtils;
import es.inteco.utils.FileUtils;

/**
 * The Class IntavExport.
 */
public final class IntavExport {
	
	/** The x ev. */
	// CONSTANTES
	static int xEv = 0;
	
	/** The y ev. */
	static int yEv = 0;
	
	/** The x. */
	static int x = 0;
	
	/** The y. */
	static int y = 0;
	
	/** The color. */
	static String color = "";
	
	/** The color ev. */
	static String colorEv = "";
	
	/** The pmgr. */
	static PropertiesManager pmgr;
	static {
		pmgr = new PropertiesManager();
		xEv = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.intav.evolution.graphic.x"));
		yEv = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.intav.evolution.graphic.y"));
		x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.x"));
		y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.pdf.graphic.y"));
		color = pmgr.getValue(CRAWLER_PROPERTIES, "chart.pdf.intav.colors");
		colorEv = pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.inteco.red.colors");
	}

	/**
	 * Instantiates a new intav export.
	 */
	private IntavExport() {
	}

	/**
	 * Export intav to simple pdf.
	 *
	 * @param evaluationIds  the evaluation ids
	 * @param request        the request
	 * @param generalExpPath the general exp path
	 * @param idTracking     the id tracking
	 * @throws Exception the exception
	 */
	// Creación del PDF para el visualizador. PDF Simple
	public static void exportIntavToSimplePdf(Map<Long, List<Long>> evaluationIds, HttpServletRequest request, String generalExpPath, long idTracking) throws Exception {
		String pathExports = generalExpPath + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.simple.file.intav.name");
		File file = new File(pathExports);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("Error al intentar crear los directorios al exportar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR);
		}
		FileOutputStream fileOut = new FileOutputStream(file);
		Document document = new Document(PageSize.A4, 50, 50, 120, 72);
		String globalPath = generalExpPath + File.separator + "temp" + File.separator;
		try {
			HashMap<Long, List<Long>> evaluationIdsMap = new HashMap<>();
			evaluationIdsMap.put(idTracking, evaluationIds.get(idTracking));
			Map<String, List<EvaluationForm>> evolutionMap = generateEvaluationMap(request, evaluationIdsMap, true);
			List<EvaluationForm> evaList = new ArrayList<>();
			String fechaInforme = "";
			for (Map.Entry<String, List<EvaluationForm>> evolutionEntry : evolutionMap.entrySet()) {
				fechaInforme = evolutionEntry.getKey();
				evaList = evolutionEntry.getValue();
			}
			PdfWriter writer = PdfWriter.getInstance(document, fileOut);
			writer.setTagged(0);
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			// writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));
			writer.setPageEvent(new ExportPageEvents(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.foot.text"), fechaInforme));
			document.open();
			PDFUtils.addCoverPage(document,
					CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.title") + EvaluatorUtils.getEntityName(evaList).toUpperCase(), "");
			// Se crea el capítulo de introdución
			Chapter chapter = globalChapter(evaList, request, globalPath, null, fechaInforme);
			document.add(chapter);
			List<IntavSimplePDFForm> results = getResultsToShow(evaList);
			Chunk chunk = new Chunk(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.main.problems"));
			Paragraph cTitle = new Paragraph("", ConstantsFont.CHAPTER_TITLE_FONT);
			cTitle.add(chunk);
			cTitle.setSpacingAfter(20);
			Chapter chapter1 = new Chapter(cTitle, 2);
			chapter1.setNumberDepth(0);
			for (IntavSimplePDFForm ispdfform : results) {
				final Paragraph titleL1 = new Paragraph(getPriorityName(request, ispdfform.getPriority()), ConstantsFont.sectionFont);
				titleL1.setSpacingBefore(10);
				final Section section1 = chapter1.addSection(titleL1);
				for (ISPDFGuidelineForm guideline : ispdfform.getGuidelinesList()) {
					for (ISPDFPautaForm pauta : guideline.getPautaList()) {
						boolean hasProblem = false;
						final Paragraph titleL2 = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), pauta.getPauta().getName()),
								ConstantsFont.guidelineDescFont);
						titleL2.setSpacingBefore(10);
						section1.add(titleL2);
						for (ISPDFProblemForm problem : pauta.getProblemList()) {
							hasProblem = true;
							String description = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.problem") + ": ";
							description += StringUtils.removeHtmlTags(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), problem.getProblem().getError()));
							final Paragraph titleL3 = new Paragraph(description, ConstantsFont.descriptionFont);
							titleL3.setSpacingBefore(10);
							section1.add(titleL3);
							for (String url : problem.getUrls()) {
								final Paragraph titleL4 = new Paragraph(url, ConstantsFont.descriptionFont);
								titleL4.setIndentationLeft(25);
								titleL4.setSpacingBefore(5);
								section1.add(titleL4);
							}
						}
						if (!hasProblem) {
							chapter1.remove(section1);
						}
					}
				}
			}
			document.add(chapter1);
			// Se crea el capitulo de evolución
			Chapter evolutionChapter = evolutionChapter(request, globalPath, null, evaluationIds, 3, true);
			if (evolutionChapter != null) {
				document.add(evolutionChapter);
			}
			FileUtils.removeFile(globalPath);
		} catch (Exception e) {
			Logger.putLog("Excepción genérica al generar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			if (document != null && document.isOpen()) {
				try {
					document.close();
				} catch (Exception e) {
					Logger.putLog("Error al cerrar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		}
	}

	/**
	 * Export intav to pdf.
	 *
	 * @param evaluationIds  the evaluation ids
	 * @param request        the request
	 * @param generalExpPath the general exp path
	 * @param idTracking     the id tracking
	 * @throws Exception the exception
	 */
	// Creación del PDF para el responsable. PDF Detallado
	public static void exportIntavToPdf(Map<Long, List<Long>> evaluationIds, HttpServletRequest request, String generalExpPath, long idTracking) throws Exception {
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		String pathExports = generalExpPath + File.separator + pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.file.intav.name");
		File file = new File(pathExports);
		if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
			Logger.putLog("Excepción al crear los directorios para generar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR);
		}
		FileOutputStream fileOut = new FileOutputStream(file);
		Document document = new Document(PageSize.A4, 50, 50, 120, 72);
		String globalPath = generalExpPath + File.separator + "temp" + File.separator;
		try {
			// Se recuperan los resultados únicamente de la ejecución de la que se pide el informe
			HashMap<Long, List<Long>> tracking = new HashMap<>();
			tracking.put(idTracking, evaluationIds.get(idTracking));
			Map<String, List<EvaluationForm>> evolutionMap = generateEvaluationMap(request, tracking, false);
			List<EvaluationForm> evaList = new ArrayList<>();
			String fechaInforme = "";
			for (Map.Entry<String, List<EvaluationForm>> evolutionEntry : evolutionMap.entrySet()) {
				fechaInforme = evolutionEntry.getKey();
				evaList = evolutionEntry.getValue();
			}
			PdfWriter writer = PdfWriter.getInstance(document, fileOut);
			writer.setTagged(0);
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			// writer.getExtraCatalog().put(new PdfName("Lang"), new PdfString("es"));
			writer.setPageEvent(new ExportPageEvents(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.foot.text"), fechaInforme));
			IndexEvents index = new IndexEvents();
			writer.setPageEvent(index);
			writer.setLinearPageMode();
			document.open();
			PDFUtils.addCoverPage(document,
					CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.title") + EvaluatorUtils.getEntityName(evaList).toUpperCase(), "");
			Chapter chapter1 = globalChapter(evaList, request, globalPath, index, fechaInforme);
			document.add(chapter1);
			int numChapter = 2;
			for (EvaluationForm evaluationForm : evaList) {
				Chunk chunk = new Chunk(evaluationForm.getUrl().toUpperCase());
				chunk.setLocalDestination(Constants.ANCLA_PDF + (numChapter - 1));
				Paragraph cTitle = new Paragraph("", ConstantsFont.CHAPTER_TITLE_FONT);
				cTitle.add(chunk);
				cTitle.setSpacingAfter(20);
				Chapter chapter = new Chapter(cTitle, numChapter);
				chapter.setNumberDepth(0);
				cTitle.add(index.create(" ", evaluationForm.getUrl().toUpperCase()));
				if (evaList.get(0).getGuideline().equals(Constants.WCAG_2)) {
					generateGraphic(evaluationForm.getPriorities(), request, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.priority.incidence"),
							globalPath, true);
				} else {
					generateGraphic(evaluationForm.getPriorities(), request, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.priority.incidence"),
							globalPath, false);
				}
				Image imgGp = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.priority.incidence") + ".jpg");
				imgGp.setAlt(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.priority.incidence"));
				chapter.add(imgGp);
				com.itextpdf.text.List summaryPriorities1 = addSummary(evaluationForm.getPriorities(), request);
				chapter.add(summaryPriorities1);
				chapter.newPage();
				for (PriorityForm priority : evaluationForm.getPriorities()) {
					final Paragraph titleL1 = new Paragraph(getPriorityName(request, priority), ConstantsFont.sectionFont);
					titleL1.setSpacingBefore(10);
					final Section sectionL1 = chapter.addSection(titleL1);
					sectionL1.setNumberDepth(0);
					boolean anyProblem = false;
					for (GuidelineForm guideline : priority.getGuidelines()) {
						for (PautaForm pautaForm : guideline.getPautas()) {
							boolean problems = false;
							final Paragraph titleL2 = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), pautaForm.getName()),
									ConstantsFont.guidelineDescFont);
							titleL2.setSpacingBefore(10);
							final Section sectionL2 = sectionL1.addSection(titleL2);
							sectionL2.setNumberDepth(0);
							for (ProblemForm problem : pautaForm.getProblems()) {
								if (!problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
									problems = true;
									anyProblem = true;
									String description = "";
									if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
										description += CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.warning") + ": ";
									} else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
										description += CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.problem") + ": ";
									} /*
										 * else if(problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) { description +=
										 * CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.info") + ": "; }
										 */
									description += StringUtils.removeHtmlTags(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), problem.getError()));
									final Paragraph titleL3 = new Paragraph(description, ConstantsFont.descriptionFont);
									titleL3.setSpacingBefore(10);
									titleL3.setSpacingAfter(10);
									final Section subSubsection = sectionL2.addSection(titleL3);
									subSubsection.setNumberDepth(0);
									addSpecificProblems(subSubsection, problem.getSpecificProblems(), request);
									if (EvaluatorUtils.isHtmlValidationCheck(Integer.parseInt(problem.getCheck())) || EvaluatorUtils.isCssValidationCheck(Integer.parseInt(problem.getCheck()))) {
										addW3CCopyright(subSubsection, problem.getCheck());
									}
								}
							}
							if (!problems) {
								sectionL1.remove(sectionL2);
							}
						}
					}
					if (!anyProblem) {
						chapter.remove(sectionL1);
					}
				}
				document.add(chapter);
				numChapter++;
			}
			Chapter evolutionChapter = evolutionChapter(request, globalPath, index, evaluationIds, numChapter, false);
			if (evolutionChapter != null) {
				document.add(evolutionChapter);
			}
			ExportPageEvents.setLastPage(true);
			IndexUtils.createIndex(writer, document, messageResources.getMessage("pdf.accessibility.index.title"), index, ConstantsFont.CHAPTER_TITLE_FONT);
			ExportPageEvents.setLastPage(false);
			FileUtils.removeFile(globalPath);
		} catch (Exception e) {
			Logger.putLog("Excepción genérica al generar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			if (document != null && document.isOpen()) {
				try {
					document.close();
				} catch (Exception e) {
					Logger.putLog("Error al cerrar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		}
	}

	/**
	 * Gets the results to show.
	 *
	 * @param evaList the eva list
	 * @return the results to show
	 */
	private static List<IntavSimplePDFForm> getResultsToShow(List<EvaluationForm> evaList) {
		List<IntavSimplePDFForm> results = new ArrayList<>();
		for (EvaluationForm evaluation : evaList) {
			for (PriorityForm priority : evaluation.getPriorities()) {
				IntavSimplePDFForm isForm = new IntavSimplePDFForm();
				isForm.setPriority(priority);
				if (results.contains(isForm)) {
					for (IntavSimplePDFForm form : results) {
						if (form.equals(isForm)) {
							isForm = form;
						}
					}
				}
				for (GuidelineForm guideline : priority.getGuidelines()) {
					ISPDFGuidelineForm ispdfGuideline = new ISPDFGuidelineForm();
					ispdfGuideline.setGuideline(guideline);
					List<ISPDFGuidelineForm> ispdfGuidList = isForm.getGuidelinesList();
					if (ispdfGuidList.contains(ispdfGuideline)) {
						for (ISPDFGuidelineForm guidForm : ispdfGuidList) {
							if (guidForm.equals(ispdfGuideline)) {
								ispdfGuideline = guidForm;
							}
						}
					}
					for (PautaForm pautaForm : guideline.getPautas()) {
						ISPDFPautaForm ispdfPauta = new ISPDFPautaForm();
						ispdfPauta.setPauta(pautaForm);
						List<ISPDFPautaForm> ispdfPautaList = ispdfGuideline.getPautaList();
						if (ispdfPautaList.contains(ispdfPauta)) {
							for (ISPDFPautaForm pauta : ispdfPautaList) {
								if (pauta.equals(ispdfPauta)) {
									ispdfPauta = pauta;
								}
							}
						}
						for (ProblemForm problem : pautaForm.getProblems()) {
							if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
								ISPDFProblemForm ispdfProblem = new ISPDFProblemForm();
								ispdfProblem.setProblem(problem);
								List<ISPDFProblemForm> ispdfProbList = ispdfPauta.getProblemList();
								if (ispdfProbList.contains(ispdfProblem)) {
									for (ISPDFProblemForm proForm : ispdfProbList) {
										if (proForm.equals(ispdfProblem)) {
											ispdfProblem = proForm;
										}
									}
								}
								ispdfProblem.getUrls().add(evaluation.getUrl());
								if (!ispdfProbList.contains(ispdfProblem)) {
									ispdfProbList.add(ispdfProblem);
									ispdfPauta.setProblemList(ispdfProbList);
								}
							}
						}
						if (!ispdfGuidList.contains(ispdfGuideline) && !ispdfPauta.getProblemList().isEmpty()) {
							ispdfGuideline.getPautaList().add(ispdfPauta);
							ispdfGuidList.add(ispdfGuideline);
							isForm.setGuidelinesList(ispdfGuidList);
						}
					}
				}
				if (!results.contains(isForm) && !isForm.getGuidelinesList().isEmpty()) {
					results.add(isForm);
				}
			}
		}
		return results;
	}

	/**
	 * Global chapter.
	 *
	 * @param evaList      the eva list
	 * @param request      the request
	 * @param globalPath   the global path
	 * @param index        the index
	 * @param fechaInforme the fecha informe
	 * @return the chapter
	 * @throws Exception the exception
	 */
	private static Chapter globalChapter(List<EvaluationForm> evaList, HttpServletRequest request, String globalPath, IndexEvents index, String fechaInforme) throws Exception {
		Chunk chunk = new Chunk(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.index.global.summary"));
		Paragraph cTitle = new Paragraph("", ConstantsFont.CHAPTER_TITLE_FONT);
		cTitle.add(chunk);
		cTitle.setSpacingAfter(20);
		Chapter chapter1 = new Chapter(cTitle, 1);
		chapter1.setNumberDepth(0);
		if (index != null) {
			chunk.setLocalDestination(Constants.ANCLA_PDF + "0");
			cTitle.add(index.create(" ", CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.index.global.summary")));
		}
		Paragraph entity = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.entity") + EvaluatorUtils.getEntityName(evaList),
				ConstantsFont.SUMMARY_FONT);
		chapter1.add(entity);
		Paragraph domain = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.domain") + evaList.get(0).getUrl(),
				ConstantsFont.SUMMARY_FONT);
		chapter1.add(domain);
		Paragraph fecha = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.date") + fechaInforme, ConstantsFont.SUMMARY_FONT);
		fecha.setSpacingAfter(20);
		chapter1.add(fecha);
		List<PriorityForm> prioList;
		if (evaList.get(0).getGuideline().equals(Constants.WCAG_2)) {
			prioList = WCAG2GenerateGraphic(request, evaList, globalPath);
		} else {
			prioList = WCAG1GenerateGraphic(request, evaList, globalPath);
		}
		Image globalImgGp = null;
		try {
			globalImgGp = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.global.priority.incidence") + ".jpg");
			globalImgGp.setAlt(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.global.priority.incidence"));
			globalImgGp.scalePercent(90);
			globalImgGp.setIndentationLeft(50);
			globalImgGp.setSpacingAfter(20);
		} catch (Exception e) {
			Logger.putLog("Exception", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		chapter1.add(globalImgGp);
		com.itextpdf.text.List summaryPriorities = addSummary(prioList, request);
		chapter1.add(summaryPriorities);
		return chapter1;
	}

	/**
	 * WCAG 1 generate graphic.
	 *
	 * @param request    the request
	 * @param evaList    the eva list
	 * @param globalPath the global path
	 * @return the list
	 */
	private static List<PriorityForm> WCAG1GenerateGraphic(HttpServletRequest request, List<EvaluationForm> evaList, String globalPath) {
		PriorityForm priorityFormA = new PriorityForm();
		priorityFormA.setPriorityName("first.level.incidents");
		PriorityForm priorityFormAA = new PriorityForm();
		priorityFormAA.setPriorityName("second.level.incidents");
		for (EvaluationForm evaluationForm : evaList) {
			for (PriorityForm priority : evaluationForm.getPriorities()) {
				if (priority.getPriorityName().equals("first.level.incidents")) {
					priorityFormA.setNumProblems(priorityFormA.getNumProblems() + priority.getNumProblems());
					priorityFormA.setNumWarnings(priorityFormA.getNumWarnings() + priority.getNumWarnings());
					priorityFormA.setNumInfos(priorityFormA.getNumInfos() + priority.getNumInfos());
				}
				if (priority.getPriorityName().equals("second.level.incidents")) {
					priorityFormAA.setNumProblems(priorityFormAA.getNumProblems() + priority.getNumProblems());
					priorityFormAA.setNumWarnings(priorityFormAA.getNumWarnings() + priority.getNumWarnings());
					priorityFormAA.setNumInfos(priorityFormAA.getNumInfos() + priority.getNumInfos());
				}
			}
		}
		List<PriorityForm> prioList = new ArrayList<>();
		prioList.add(priorityFormA);
		prioList.add(priorityFormAA);
		generateGraphic(prioList, request, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.global.priority.incidence"), globalPath, false);
		return prioList;
	}

	/**
	 * WCAG 2 generate graphic.
	 *
	 * @param request    the request
	 * @param evaList    the eva list
	 * @param globalPath the global path
	 * @return the list
	 */
	private static List<PriorityForm> WCAG2GenerateGraphic(HttpServletRequest request, List<EvaluationForm> evaList, String globalPath) {
		PriorityForm priorityForm1WCAG2 = new PriorityForm();
		priorityForm1WCAG2.setPriorityName("wcag.2.principle.1.name");
		PriorityForm priorityForm2WCAG2 = new PriorityForm();
		priorityForm2WCAG2.setPriorityName("wcag.2.principle.2.name");
		PriorityForm priorityForm3WCAG2 = new PriorityForm();
		priorityForm3WCAG2.setPriorityName("wcag.2.principle.3.name");
		PriorityForm priorityForm4WCAG2 = new PriorityForm();
		priorityForm4WCAG2.setPriorityName("wcag.2.principle.4.name");
		for (EvaluationForm evaluationForm : evaList) {
			for (PriorityForm priority : evaluationForm.getPriorities()) {
				if (priority.getPriorityName().equals("wcag.2.principle.1.name")) {
					priorityForm1WCAG2.setNumProblems(priorityForm1WCAG2.getNumProblems() + priority.getNumProblems());
					priorityForm1WCAG2.setNumWarnings(priorityForm1WCAG2.getNumWarnings() + priority.getNumWarnings());
					priorityForm1WCAG2.setNumInfos(priorityForm1WCAG2.getNumInfos() + priority.getNumInfos());
				}
				if (priority.getPriorityName().equals("wcag.2.principle.2.name")) {
					priorityForm2WCAG2.setNumProblems(priorityForm2WCAG2.getNumProblems() + priority.getNumProblems());
					priorityForm2WCAG2.setNumWarnings(priorityForm2WCAG2.getNumWarnings() + priority.getNumWarnings());
					priorityForm2WCAG2.setNumInfos(priorityForm2WCAG2.getNumInfos() + priority.getNumInfos());
				}
				if (priority.getPriorityName().equals("wcag.2.principle.3.name")) {
					priorityForm3WCAG2.setNumProblems(priorityForm3WCAG2.getNumProblems() + priority.getNumProblems());
					priorityForm3WCAG2.setNumWarnings(priorityForm3WCAG2.getNumWarnings() + priority.getNumWarnings());
					priorityForm3WCAG2.setNumInfos(priorityForm3WCAG2.getNumInfos() + priority.getNumInfos());
				}
				if (priority.getPriorityName().equals("wcag.2.principle.4.name")) {
					priorityForm4WCAG2.setNumProblems(priorityForm4WCAG2.getNumProblems() + priority.getNumProblems());
					priorityForm4WCAG2.setNumWarnings(priorityForm4WCAG2.getNumWarnings() + priority.getNumWarnings());
					priorityForm4WCAG2.setNumInfos(priorityForm4WCAG2.getNumInfos() + priority.getNumInfos());
				}
			}
		}
		List<PriorityForm> prioList = new ArrayList<>();
		prioList.add(priorityForm1WCAG2);
		prioList.add(priorityForm2WCAG2);
		prioList.add(priorityForm3WCAG2);
		prioList.add(priorityForm4WCAG2);
		generateGraphic(prioList, request, CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.global.priority.incidence"), globalPath, true);
		return prioList;
	}

	/**
	 * Adds the summary.
	 *
	 * @param prioList the prio list
	 * @param request  the request
	 * @return the com.itextpdf.text. list
	 * @throws BadElementException the bad element exception
	 * @throws IOException         Signals that an I/O exception has occurred.
	 */
	private static com.itextpdf.text.List addSummary(List<PriorityForm> prioList, HttpServletRequest request) throws BadElementException, IOException {
		com.itextpdf.text.List summaryPriorities = new com.itextpdf.text.List();
		for (PriorityForm priority : prioList) {
			ListItem priorityList = new ListItem(getPriorityName(request, priority), ConstantsFont.SUMMARY_TITLE_FONT);
			priorityList.setSpacingBefore(8);
			priorityList.setListSymbol(new Chunk());
			summaryPriorities.add(priorityList);
			com.itextpdf.text.List summaryStatistics = new com.itextpdf.text.List();
			summaryStatistics.setIndentationLeft(40);
			ListItem problemList = new ListItem(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.problems") + ": " + priority.getNumProblems(),
					ConstantsFont.SUMMARY_FONT_PROBLEM);
			ListItem warningsList = new ListItem(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.warnings") + ": " + priority.getNumWarnings(),
					ConstantsFont.SUMMARY_FONT_WARNING);
			ListItem infosList = new ListItem(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.infos") + ": " + priority.getNumInfos(),
					ConstantsFont.SUMMARY_FONT_CANNOTTELL);
			Image imgProblemA = Image.getInstance(pmgr.getValue(Constants.PDF_PROPERTIES, "path.problem"));
			Image imgWarnings = Image.getInstance(pmgr.getValue(Constants.PDF_PROPERTIES, "path.warnings"));
			Image imgInfos = Image.getInstance(pmgr.getValue(Constants.PDF_PROPERTIES, "path.infos"));
			imgProblemA.setAlt(CrawlerUtils.getResources(request).getMessage(""));
			imgWarnings.setAlt(CrawlerUtils.getResources(request).getMessage(""));
			imgInfos.setAlt(CrawlerUtils.getResources(request).getMessage(""));
			imgProblemA.scalePercent(60);
			imgWarnings.scalePercent(60);
			imgInfos.scalePercent(60);
			problemList.setListSymbol(new Chunk(imgProblemA, 0, 0));
			problemList.setSpacingBefore(8);
			warningsList.setListSymbol(new Chunk(imgWarnings, 0, 0));
			warningsList.setSpacingBefore(8);
			infosList.setListSymbol(new Chunk(imgInfos, 0, 0));
			infosList.setSpacingBefore(8);
			summaryStatistics.add(problemList);
			summaryStatistics.add(warningsList);
			summaryStatistics.add(infosList);
			summaryPriorities.add(summaryStatistics);
		}
		return summaryPriorities;
	}

	/**
	 * Generate graphic.
	 *
	 * @param priorityFormList the priority form list
	 * @param request          the request
	 * @param tableName        the table name
	 * @param tempPath         the temp path
	 * @param isWCAG2          the is WCAG 2
	 */
	private static void generateGraphic(List<PriorityForm> priorityFormList, HttpServletRequest request, String tableName, String tempPath, boolean isWCAG2) {
		DefaultCategoryDataset priorityDataSet;
		if (!isWCAG2) {
			priorityDataSet = createDataSet(request, priorityFormList);
		} else {
			priorityDataSet = createDataSetWCAG2(request, priorityFormList);
		}
		try {
			String rowTitle = CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.intav.rowTitle");
			ChartForm chart = new ChartForm(tableName, "", rowTitle, priorityDataSet, true, true, false, false, true, false, false, x, y, color);
			GraphicsUtils.createSeriesBarChart(chart, tempPath + tableName + ".jpg", "", CrawlerUtils.getResources(request), false);
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Creates the data set.
	 *
	 * @param request          the request
	 * @param priorityFormList the priority form list
	 * @return the default category dataset
	 */
	private static DefaultCategoryDataset createDataSet(HttpServletRequest request, List<PriorityForm> priorityFormList) {
		DefaultCategoryDataset priorityDataSet = new DefaultCategoryDataset();
		for (PriorityForm priority : priorityFormList) {
			if (priority.getPriorityName().equals("first.level.incidents")) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.problems"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "first.level.incidents.export"));
					priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.warnings"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "first.level.incidents.export"));
					priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.infos"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "first.level.incidents.export"));
				}
			} else if (priority.getPriorityName().equals("second.level.incidents")) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.problems"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "second.level.incidents.export"));
					priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.warnings"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "second.level.incidents.export"));
					priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.infos"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "second.level.incidents.export"));
				}
			} else if (priority.getPriorityName().equals("third.level.incidents")) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.problems"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "third.level.incidents.export"));
					priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.warnings"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "third.level.incidents.export"));
					priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.infos"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "third.level.incidents.export"));
				}
			}
		}
		return priorityDataSet;
	}

	/**
	 * Creates the data set WCAG 2.
	 *
	 * @param request          the request
	 * @param priorityFormList the priority form list
	 * @return the default category dataset
	 */
	private static DefaultCategoryDataset createDataSetWCAG2(HttpServletRequest request, List<PriorityForm> priorityFormList) {
		DefaultCategoryDataset priorityDataSet = new DefaultCategoryDataset();
		for (PriorityForm priority : priorityFormList) {
			if (priority.getPriorityName().equals("wcag.2.principle.1.name")) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.problems"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.1.name"));
					priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.warnings"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.1.name"));
					priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.infos"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.1.name"));
				}
			} else if (priority.getPriorityName().equals("wcag.2.principle.2.name")) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.problems"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.2.name"));
					priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.warnings"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.2.name"));
					priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.infos"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.2.name"));
				}
			} else if (priority.getPriorityName().equals("wcag.2.principle.3.name")) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.problems"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.3.name"));
					priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.warnings"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.3.name"));
					priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.infos"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.3.name"));
				}
			} else if (priority.getPriorityName().equals("wcag.2.principle.4.name")) {
				if ((priority.getNumProblems() != 0) || (priority.getNumWarnings() != 0) || (priority.getNumInfos() != 0)) {
					priorityDataSet.addValue(priority.getNumProblems(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.problems"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.4.name"));
					priorityDataSet.addValue(priority.getNumWarnings(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.warnings"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.4.name"));
					priorityDataSet.addValue(priority.getNumInfos(), CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "chart.infos"),
							CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.4.name"));
				}
			}
		}
		return priorityDataSet;
	}

	/**
	 * Adds the specific problems.
	 *
	 * @param subSubSection    the sub sub section
	 * @param specificProblems the specific problems
	 * @param request          the request
	 */
	private static void addSpecificProblems(Section subSubSection, List<SpecificProblemForm> specificProblems, HttpServletRequest request) {
		int maxNumErrors = Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"));
		for (SpecificProblemForm specificProblem : specificProblems) {
			maxNumErrors--;
			if (specificProblem.getCode() != null) {
				float[] widths = { 0.15f, 0.85f };
				PdfPTable table = new PdfPTable(widths);
				table.setSpacingAfter(10);
				PdfPCell labelCell = new PdfPCell(new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.line"), ConstantsFont.labelCellFont));
				labelCell.setBackgroundColor(Constants.BC_ROJO_INTECO);
				table.addCell(labelCell);
				table.addCell(new Paragraph(specificProblem.getLine(), FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
				labelCell = new PdfPCell(new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.column"), ConstantsFont.labelCellFont));
				labelCell.setBackgroundColor(Constants.BC_ROJO_INTECO);
				table.addCell(labelCell);
				table.addCell(new Paragraph(specificProblem.getColumn(), FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
				if (StringUtils.isNotEmpty(specificProblem.getMessage())) {
					labelCell = new PdfPCell(new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.message"), ConstantsFont.labelCellFont));
					labelCell.setBackgroundColor(Constants.BC_ROJO_INTECO);
					table.addCell(labelCell);
					table.addCell(new Paragraph(specificProblem.getMessage(), FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
				}
				labelCell = new PdfPCell(new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.code"), ConstantsFont.labelCellFont));
				labelCell.setBackgroundColor(Constants.BC_ROJO_INTECO);
				table.addCell(labelCell);
				if (specificProblem.getCode() != null) {
					StringBuilder code = new StringBuilder();
					for (int i = 0; i < specificProblem.getCode().size(); i++) {
						code.append(StringUtils.truncateText(specificProblem.getCode().get(i), 300)).append("\n");
					}
					table.addCell(new Paragraph(HTMLEntities.unhtmlAngleBrackets(code.toString()), ConstantsFont.codeCellFont));
				}
				subSubSection.add(table);
			} else if (specificProblem.getNote() != null) {
				String linkCode = getMatch(specificProblem.getNote().get(0), "(<a.*?</a>)");
				String paragraphText = specificProblem.getNote().get(0).replace(linkCode, "");
				String linkHref = getMatch(specificProblem.getNote().get(0), "href='(.*?)'");
				Paragraph p = new Paragraph(paragraphText, ConstantsFont.noteCellFont);
				Anchor anchor = new Anchor(getMatch(specificProblem.getNote().get(0), "<a.*?>(.*?)</a>"), ConstantsFont.NOTE_ANCHOR_CELL_FONT);
				anchor.setReference(linkHref);
				p.add(anchor);
				subSubSection.add(p);
			}
			if (maxNumErrors < 0) {
				if (specificProblems.size() > Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"))) {
					Paragraph p = new Paragraph(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.num.errors.summary",
							Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number")), specificProblems.size()), ConstantsFont.MORE_INFO_FONT);
					p.setAlignment(Paragraph.ALIGN_RIGHT);
					subSubSection.add(p);
				}
				break;
			}
		}
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
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

	/**
	 * Gets the priority name.
	 *
	 * @param request  the request
	 * @param priority the priority
	 * @return the priority name
	 */
	private static String getPriorityName(HttpServletRequest request, PriorityForm priority) {
		if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "priority.1.name"))) {
			return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "first.level.incidents");
		} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "priority.2.name"))) {
			return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "second.level.incidents");
		} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "priority.3.name"))) {
			return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "third.level.incidents");
		} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "wcag.2.principle.1.name"))) {
			return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.1.name");
		} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "wcag.2.principle.2.name"))) {
			return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.2.name");
		} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "wcag.2.principle.3.name"))) {
			return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.3.name");
		} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "wcag.2.principle.4.name"))) {
			return CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "wcag.2.principle.4.name");
		} else {
			return "";
		}
	}

	/**
	 * Evolution chapter.
	 *
	 * @param request       the request
	 * @param globalPath    the global path
	 * @param index         the index
	 * @param evaluationIds the evaluation ids
	 * @param numChapter    the num chapter
	 * @param onlyProblems  the only problems
	 * @return the chapter
	 * @throws Exception the exception
	 */
	private static Chapter evolutionChapter(HttpServletRequest request, String globalPath, IndexEvents index, Map<Long, List<Long>> evaluationIds, int numChapter, boolean onlyProblems)
			throws Exception {
		if (generateEvolutionGraphics(request, globalPath, evaluationIds, onlyProblems) == 1) {
			Chunk chunk = new Chunk(CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.index.global.evolution"));
			Paragraph cTitle = new Paragraph("", ConstantsFont.CHAPTER_TITLE_FONT);
			cTitle.add(chunk);
			cTitle.setSpacingAfter(20);
			Chapter chapterEv = new Chapter(cTitle, numChapter);
			chapterEv.setNumberDepth(0);
			if (index != null) {
				chunk.setLocalDestination(Constants.ANCLA_PDF + (numChapter - 1));
				cTitle.add(index.create(" ", CrawlerUtils.getResources(request).getMessage(CrawlerUtils.getLocale(request), "pdf.accessibility.index.global.evolution")));
			}
			try {
				Image globalImgGpProblemsA = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemA.title") + ".jpg");
				globalImgGpProblemsA.setSpacingAfter(2 * ConstantsFont.LINE_SPACE);
				globalImgGpProblemsA.scalePercent(60);
				globalImgGpProblemsA.setAlt(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemA.title"));
				Image globalImgGpProblemsAA = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemAA.title") + ".jpg");
				globalImgGpProblemsAA.setSpacingAfter(2 * ConstantsFont.LINE_SPACE);
				globalImgGpProblemsAA.scalePercent(60);
				globalImgGpProblemsAA.setAlt(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemAA.title"));
				chapterEv.add(globalImgGpProblemsA);
				chapterEv.add(globalImgGpProblemsAA);
				if (!onlyProblems) {
					Image globalImgGpWarningsA = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningA.title") + ".jpg");
					globalImgGpWarningsA.setSpacingAfter(2 * ConstantsFont.LINE_SPACE);
					globalImgGpWarningsA.scalePercent(60);
					globalImgGpWarningsA.setAlt(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningA.title"));
					Image globalImgGpWarningsAA = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningAA.title") + ".jpg");
					globalImgGpWarningsAA.setSpacingAfter(2 * ConstantsFont.LINE_SPACE);
					globalImgGpWarningsAA.scalePercent(60);
					globalImgGpWarningsAA.setAlt(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningAA.title"));
					Image globalImgGpCannottellA = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellA.title") + ".jpg");
					globalImgGpCannottellA.setSpacingAfter(2 * ConstantsFont.LINE_SPACE);
					globalImgGpCannottellA.scalePercent(60);
					globalImgGpCannottellA.setAlt(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellA.title"));
					Image globalImgGpCannottellAA = Image.getInstance(globalPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellAA.title") + ".jpg");
					globalImgGpCannottellAA.setSpacingAfter(2 * ConstantsFont.LINE_SPACE);
					globalImgGpCannottellAA.scalePercent(60);
					globalImgGpCannottellA.setAlt(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellAA.title"));
					chapterEv.add(globalImgGpWarningsA);
					chapterEv.add(globalImgGpWarningsAA);
					chapterEv.add(globalImgGpCannottellA);
					chapterEv.add(globalImgGpCannottellAA);
				}
			} catch (IOException e) {
				Logger.putLog("IOException", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			}
			return chapterEv;
		} else {
			return null;
		}
	}

	/**
	 * Generate evolution graphics.
	 *
	 * @param request       the request
	 * @param tempPath      the temp path
	 * @param evaluationIds the evaluation ids
	 * @param onlyProblems  the only problems
	 * @return the int
	 * @throws Exception the exception
	 */
	private static int generateEvolutionGraphics(HttpServletRequest request, String tempPath, Map<Long, List<Long>> evaluationIds, boolean onlyProblems) throws Exception {
		Map<String, List<EvaluationForm>> evolutionMap = generateEvaluationMap(request, evaluationIds, true);
		if (evolutionMap.size() > 1) {
			createProblemsChart(tempPath, request, evolutionMap, xEv, yEv);
			if (!onlyProblems) {
				createWarningsChart(tempPath, request, evolutionMap, xEv, yEv);
				createCannottellChart(tempPath, request, evolutionMap, xEv, yEv);
			}
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Creates the problems chart.
	 *
	 * @param tempPath     the temp path
	 * @param request      the request
	 * @param evolutionMap the evolution map
	 * @param x            the x
	 * @param y            the y
	 */
	private static void createProblemsChart(String tempPath, HttpServletRequest request, Map<String, List<EvaluationForm>> evolutionMap, int x, int y) {
		try {
			DefaultCategoryDataset dataSetA = new DefaultCategoryDataset();
			DefaultCategoryDataset dataSetAA = new DefaultCategoryDataset();
			for (Map.Entry<String, List<EvaluationForm>> evolutionEntry : evolutionMap.entrySet()) {
				BigDecimal problemsA = BigDecimal.ZERO;
				BigDecimal problemsAA = BigDecimal.ZERO;
				for (EvaluationForm evaluationForm : evolutionEntry.getValue()) {
					for (PriorityForm priority : evaluationForm.getPriorities()) {
						if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.1.name")))) {
							problemsA = problemsA.add(new BigDecimal(priority.getNumProblems()));
						} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.2.name")))) {
							problemsAA = problemsAA.add(new BigDecimal(priority.getNumProblems()));
						}
					}
				}
				if (evolutionEntry.getValue().size() != 0) {
					dataSetA.addValue(problemsA.divide(new BigDecimal(evolutionEntry.getValue().size()), 2, BigDecimal.ROUND_HALF_UP), "", evolutionEntry.getKey());
					dataSetAA.addValue(problemsAA.divide(new BigDecimal(evolutionEntry.getValue().size()), 2, BigDecimal.ROUND_HALF_UP), "", evolutionEntry.getKey());
				} else {
					dataSetA.addValue(BigDecimal.ZERO, "", evolutionEntry.getKey());
					dataSetAA.addValue(BigDecimal.ZERO, "", evolutionEntry.getKey());
				}
			}
			String rowTitle = CrawlerUtils.getResources(request).getMessage("chart.intav.evolution.problem.rowTitle");
			String columnTitle = CrawlerUtils.getResources(request).getMessage("chart.intav.evolution.columnTitle");
			ChartForm chartA = new ChartForm(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemA.title"), columnTitle, rowTitle, dataSetA, true, false, false,
					false, false, true, true, x, y, colorEv);
			GraphicsUtils.createStandardBarChart(chartA, tempPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemA.title") + ".jpg", "",
					CrawlerUtils.getResources(request), false);
			ChartForm chartAA = new ChartForm(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemAA.title"), columnTitle, rowTitle, dataSetAA, true, false, false,
					false, false, true, true, x, y, colorEv);
			GraphicsUtils.createStandardBarChart(chartAA, tempPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.problemAA.title") + ".jpg", "",
					CrawlerUtils.getResources(request), false);
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Creates the warnings chart.
	 *
	 * @param tempPath     the temp path
	 * @param request      the request
	 * @param evolutionMap the evolution map
	 * @param x            the x
	 * @param y            the y
	 */
	private static void createWarningsChart(String tempPath, HttpServletRequest request, Map<String, List<EvaluationForm>> evolutionMap, int x, int y) {
		try {
			DefaultCategoryDataset dataSetA = new DefaultCategoryDataset();
			DefaultCategoryDataset dataSetAA = new DefaultCategoryDataset();
			for (Map.Entry<String, List<EvaluationForm>> evolutionEntry : evolutionMap.entrySet()) {
				BigDecimal warningsA = BigDecimal.ZERO;
				BigDecimal warningsAA = BigDecimal.ZERO;
				for (EvaluationForm evaluationForm : evolutionEntry.getValue()) {
					for (PriorityForm priority : evaluationForm.getPriorities()) {
						if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.1.name")))) {
							warningsA = warningsA.add(new BigDecimal(priority.getNumWarnings()));
						} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.2.name")))) {
							warningsAA = warningsAA.add(new BigDecimal(priority.getNumWarnings()));
						}
					}
				}
				if (evolutionEntry.getValue().size() != 0) {
					dataSetA.addValue(warningsA.divide(new BigDecimal(evolutionEntry.getValue().size()), 2, BigDecimal.ROUND_HALF_UP), "", evolutionEntry.getKey());
					dataSetAA.addValue(warningsAA.divide(new BigDecimal(evolutionEntry.getValue().size()), 2, BigDecimal.ROUND_HALF_UP), "", evolutionEntry.getKey());
				} else {
					dataSetA.addValue(BigDecimal.ZERO, "", evolutionEntry.getKey());
					dataSetAA.addValue(BigDecimal.ZERO, "", evolutionEntry.getKey());
				}
			}
			String rowTitle = CrawlerUtils.getResources(request).getMessage("chart.intav.evolution.warning.rowTitle");
			String columnTitle = CrawlerUtils.getResources(request).getMessage("chart.intav.evolution.columnTitle");
			ChartForm chartA = new ChartForm(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningA.title"), columnTitle, rowTitle, dataSetA, true, false, false,
					false, false, true, true, x, y, colorEv);
			GraphicsUtils.createStandardBarChart(chartA, tempPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningA.title") + ".jpg", "",
					CrawlerUtils.getResources(request), false);
			ChartForm chartAA = new ChartForm(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningAA.title"), columnTitle, rowTitle, dataSetAA, true, false, false,
					false, false, true, true, x, y, colorEv);
			GraphicsUtils.createStandardBarChart(chartAA, tempPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.warningAA.title") + ".jpg", "",
					CrawlerUtils.getResources(request), false);
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Creates the cannottell chart.
	 *
	 * @param tempPath     the temp path
	 * @param request      the request
	 * @param evolutionMap the evolution map
	 * @param x            the x
	 * @param y            the y
	 */
	private static void createCannottellChart(String tempPath, HttpServletRequest request, Map<String, List<EvaluationForm>> evolutionMap, int x, int y) {
		try {
			DefaultCategoryDataset dataSetA = new DefaultCategoryDataset();
			DefaultCategoryDataset dataSetAA = new DefaultCategoryDataset();
			for (Map.Entry<String, List<EvaluationForm>> evolutionEntry : evolutionMap.entrySet()) {
				BigDecimal cannottellA = BigDecimal.ZERO;
				BigDecimal cannottellAA = BigDecimal.ZERO;
				for (EvaluationForm evaluationForm : evolutionEntry.getValue()) {
					for (PriorityForm priority : evaluationForm.getPriorities()) {
						if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.1.name")))) {
							cannottellA = cannottellA.add(new BigDecimal(priority.getNumInfos()));
						} else if (priority.getPriorityName().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, ("priority.2.name")))) {
							cannottellAA = cannottellAA.add(new BigDecimal(priority.getNumInfos()));
						}
					}
				}
				if (evolutionEntry.getValue().size() != 0) {
					dataSetA.addValue(cannottellA.divide(new BigDecimal(evolutionEntry.getValue().size()), 2, BigDecimal.ROUND_HALF_UP), "", evolutionEntry.getKey());
					dataSetAA.addValue(cannottellAA.divide(new BigDecimal(evolutionEntry.getValue().size()), 2, BigDecimal.ROUND_HALF_UP), "", evolutionEntry.getKey());
				} else {
					dataSetA.addValue(BigDecimal.ZERO, "", evolutionEntry.getKey());
					dataSetAA.addValue(BigDecimal.ZERO, "", evolutionEntry.getKey());
				}
			}
			String rowTitle = CrawlerUtils.getResources(request).getMessage("chart.intav.evolution.cannottell.rowTitle");
			String columnTitle = CrawlerUtils.getResources(request).getMessage("chart.intav.evolution.columnTitle");
			ChartForm chartA = new ChartForm(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellA.title"), columnTitle, rowTitle, dataSetA, true, false, false,
					false, false, true, true, x, y, colorEv);
			GraphicsUtils.createStandardBarChart(chartA, tempPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellA.title") + ".jpg", "",
					CrawlerUtils.getResources(request), false);
			ChartForm chartAA = new ChartForm(CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellAA.title"), columnTitle, rowTitle, dataSetAA, true, false,
					false, false, false, true, true, x, y, colorEv);
			GraphicsUtils.createStandardBarChart(chartAA, tempPath + CrawlerUtils.getResources(request).getMessage("pdf.accessibility.global.evolution.cannottellAA.title") + ".jpg", "",
					CrawlerUtils.getResources(request), false);
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
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
		if (check.equals("232")) {// pmgr.getValue("check.properties", "doc.valida.especif"))) {
			anchor = new Anchor(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright"), ConstantsFont.MORE_INFO_FONT);
			anchor.setReference(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
		} else if (EvaluatorUtils.isCssValidationCheck(Integer.parseInt(check))) {
			anchor = new Anchor(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.css.copyright"), ConstantsFont.MORE_INFO_FONT);
			anchor.setReference(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
		}
		p.add(anchor);
		subSubSection.add(p);
	}

	/**
	 * Generate evaluation map.
	 *
	 * @param request       the request
	 * @param evaluationIds the evaluation ids
	 * @param getOnlyChecks the get only checks
	 * @return the map
	 * @throws Exception the exception
	 */
	private static Map<String, List<EvaluationForm>> generateEvaluationMap(HttpServletRequest request, Map<Long, List<Long>> evaluationIds, boolean getOnlyChecks) throws Exception {
		TreeMap<String, List<EvaluationForm>> evolutionMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.format.simple"));
				try {
					Date fecha1 = new Date(df.parse(o1).getTime());
					Date fecha2 = new Date(df.parse(o2).getTime());
					return fecha1.compareTo(fecha2);
				} catch (Exception e) {
					Logger.putLog("Error al ordenar fechas de evolución. ", IntavExport.class, Logger.LOG_LEVEL_ERROR, e);
				}
				return 0;
			}
		});
		try (Connection c = DataBaseManager.getConnection()) {
			// Inicializamos el evaluador si hace falta
			if (!EvaluatorUtility.isInitialized()) {
				try {
					EvaluatorUtility.initialize();
				} catch (Exception e) {
					Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
			for (Map.Entry<Long, List<Long>> entry : evaluationIds.entrySet()) {
				List<EvaluationForm> evaList = new ArrayList<>();
				for (Long id : entry.getValue()) {
					Evaluator evaluator = new Evaluator();
					Evaluation evaluation = evaluator.getAnalisisDB(c, id, EvaluatorUtils.getDocList(), getOnlyChecks);
					EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, EvaluatorUtility.getLanguage(request));
					evaList.add(evaluationForm);
				}
				FulfilledCrawlingForm fulfilledCrawlingForm = RastreoDAO.getFullfilledCrawlingExecution(c, entry.getKey());
				if (fulfilledCrawlingForm != null) {
					String date = fulfilledCrawlingForm.getDate();
					evolutionMap.put(date, evaList);
				}
			}
			return evolutionMap;
		} catch (Exception e) {
			Logger.putLog("Excepción genérica al generar el pdf", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}
}
