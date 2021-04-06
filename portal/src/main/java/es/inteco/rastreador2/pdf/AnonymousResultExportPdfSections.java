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

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;

import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;

/**
 * The Class AnonymousResultExportPdfSections.
 */
public final class AnonymousResultExportPdfSections {
	
	/**
	 * Instantiates a new anonymous result export pdf sections.
	 */
	private AnonymousResultExportPdfSections() {
	}

	/**
	 * Creates the introduction chapter.
	 *
	 * @param request       the request
	 * @param index         the index
	 * @param document      the document
	 * @param countSections the count sections
	 * @param numChapter    the num chapter
	 * @param titleFont     the title font
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int createIntroductionChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont) throws Exception {
		Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter1.title"), index, countSections++, numChapter, titleFont);
		AnonymousResultExportPdfSection1.createChapter1(request, chapter);
		Section section1 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter11.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSection1.createSection11(request, section1);
		document.add(chapter);
		return countSections;
	}

	/**
	 * Creates the objetive chapter.
	 *
	 * @param request         the request
	 * @param index           the index
	 * @param document        the document
	 * @param countSections   the count sections
	 * @param numChapter      the num chapter
	 * @param titleFont       the title font
	 * @param observatoryType the observatory type
	 * @return the int
	 * @throws DocumentException the document exception
	 */
	public static int createObjetiveChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, long observatoryType)
			throws DocumentException {
		Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter2.title"), index, countSections++, numChapter, titleFont);
		AnonymousResultExportPdfSection2.createChapter2(request, chapter, observatoryType);
		document.add(chapter);
		return countSections;
	}

	/**
	 * Creates the methodology chapter.
	 *
	 * @param request               the request
	 * @param index                 the index
	 * @param document              the document
	 * @param countSections         the count sections
	 * @param numChapter            the num chapter
	 * @param titleFont             the title font
	 * @param primaryReportPageList the primary report page list
	 * @param observatoryType       the observatory type
	 * @param isBasicService        the is basic service
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int createMethodologyChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont,
			List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) throws Exception {
		Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter3.title"), index, countSections++, numChapter, titleFont);
		AnonymousResultExportPdfSection3.createChapter3(request, chapter);
		if (!isBasicService) {
			Section section1 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter31.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
					countSections++, 1);
			if (observatoryType == Constants.OBSERVATORY_TYPE_AGE) {
				AnonymousResultExportPdfSection3.createSection31AGE(request, section1);
			} else if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
				AnonymousResultExportPdfSection3.createSection31CCAA(request, section1);
			} else if (observatoryType == Constants.OBSERVATORY_TYPE_EELL) {
				AnonymousResultExportPdfSection3.createSection31EELL(request, section1);
			} else if (observatoryType == Constants.OBSERVATORY_TYPE_PRENSA) {
				AnonymousResultExportPdfSection3.createSection31PRENSA(request, section1);
			}
		}
		Section section2 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter32.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		if (observatoryType == Constants.OBSERVATORY_TYPE_PRENSA) {
			AnonymousResultExportPdfSection3.createSection32PRENSA(request, section2, primaryReportPageList);
		} else {
			AnonymousResultExportPdfSection3.createSection32(request, section2, primaryReportPageList, observatoryType, isBasicService);
		}
		Section section3 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter33.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1, CrawlerUtils.getResources(request).getMessage("anchor.met.table"));
		AnonymousResultExportPdfSection3.createSection33(request, section3, observatoryType);
		Section section31 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter331.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3,
				countSections++, 2);
		AnonymousResultExportPdfSection3.createSection331(request, section31);
		Section section4 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter34.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSection3.createSection34(request, section4);
		Section section41 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter341.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
				countSections++, 2);
		AnonymousResultExportPdfSection3.createSection341(request, section41);
		Section section42 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter342.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4,
				countSections++, 2);
		AnonymousResultExportPdfSection3.createSection342(request, section42);
		// Solo sale en el agregado
		if (primaryReportPageList == null) {
			Section section43 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter343.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L,
					section4, countSections++, 2);
			AnonymousResultExportPdfSection3.createSection343(request, section43);
		}
		document.add(chapter);
		return countSections;
	}

	/**
	 * Creates the content chapter.
	 *
	 * @param request       the request
	 * @param d             the d
	 * @param contents      the contents
	 * @param index         the index
	 * @param numChapter    the num chapter
	 * @param countSections the count sections
	 * @return the int
	 * @throws Exception the exception
	 */
	public static int createContentChapter(HttpServletRequest request, Document d, String contents, IndexEvents index, int numChapter, int countSections) throws Exception {
		Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("basic.service.content.title"), index, countSections++, numChapter,
				ConstantsFont.CHAPTER_TITLE_MP_FONT);
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("basic.service.content.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, true);
		PDFUtils.addCode(HTMLEntities.unhtmlAngleBrackets(contents), chapter);
		d.add(chapter);
		return countSections;
	}

	/**
	 * Creates the global results chapter.
	 *
	 * @param request         the request
	 * @param index           the index
	 * @param document        the document
	 * @param countSections   the count sections
	 * @param numChapter      the num chapter
	 * @param titleFont       the title font
	 * @param graphicPath     the graphic path
	 * @param execution_id    the execution id
	 * @param idObservatory   the id observatory
	 * @param categories      the categories
	 * @param observatoryType the observatory type
	 * @return the int
	 * @throws Exception the exception
	 */
	protected static int createGlobalResultsChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, String graphicPath,
			String execution_id, Long idObservatory, List<CategoriaForm> categories, long observatoryType) throws Exception {
		java.util.List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(execution_id, Constants.COMPLEXITY_SEGMENT_NONE, null);
		Map<String, Integer> result = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
		Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter4.title"), index, countSections++, numChapter, titleFont);
		AnonymousResultExportPdfSection4.createChapter4(request, chapter);
		chapter.newPage();
		Section section1 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter41.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1, CrawlerUtils.getResources(request).getMessage("anchor.4.1"));
		AnonymousResultExportPdfSection4.createSection41(request, section1, graphicPath, result, observatoryType);
		Section section2 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter42.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSection4.createSection42(request, section2, graphicPath, execution_id, pageExecutionList, categories, observatoryType);
		if (categories.size() != 5) {
			chapter.newPage();
		}
		Section section3 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter43.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1, CrawlerUtils.getResources(request).getMessage("anchor.4.3"));
		AnonymousResultExportPdfSection4.createSection43(request, section3, graphicPath, execution_id, String.valueOf(idObservatory), pageExecutionList, categories);
		if (categories.size() != 5) {
			chapter.newPage();
		}
		Section section4 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter44.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1, CrawlerUtils.getResources(request).getMessage("anchor.PuMe"));
		AnonymousResultExportPdfSection4.createSection44(request, section4, graphicPath, pageExecutionList);
		chapter.newPage();
		Section section5 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter45.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1, CrawlerUtils.getResources(request).getMessage("anchor.PoMo"));
		AnonymousResultExportPdfSection4.createSection45(request, section5, graphicPath, pageExecutionList);
		chapter.newPage();
		Section section6 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapter46.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSection4.createSection46(request, section6, graphicPath, pageExecutionList);
		document.add(chapter);
		return countSections;
	}

	/**
	 * Creates the category results chapter.
	 *
	 * @param request         the request
	 * @param index           the index
	 * @param document        the document
	 * @param countSections   the count sections
	 * @param numChapter      the num chapter
	 * @param titleFont       the title font
	 * @param graphicPath     the graphic path
	 * @param execution_id    the execution id
	 * @param conn            the conn
	 * @param idObservatory   the id observatory
	 * @param category        the category
	 * @param observatoryType the observatory type
	 * @return the int
	 * @throws Exception the exception
	 */
	protected static int createCategoryResultsChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, String graphicPath,
			String execution_id, Connection conn, Long idObservatory, CategoriaForm category, long observatoryType) throws Exception {
		java.util.List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(execution_id, Long.parseLong(category.getId()), null);
		Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapterCat.title") + " " + category.getName().toUpperCase(), index,
				countSections++, numChapter, titleFont);
		AnonymousResultExportPdfSectionCat.createSectionCat(request, chapter, category);
		Section section1 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapterCat1.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSectionCat.createSectionCat1(request, section1, graphicPath, execution_id, category);
		chapter.newPage();
		Section section2 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapterCat2.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSectionCat.createSectionCat2(request, section2, graphicPath, pageExecutionList, category);
		// chapter.newPage();
		Section section3 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapterCat3.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSectionCat.createSectionCat3(request, section3, graphicPath, pageExecutionList, category);
		chapter.newPage();
		Section section4 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapterCat4.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSectionCat.createSectionCat4(request, section4, graphicPath, pageExecutionList, category);
		chapter.newPage();
		Section section5 = PDFUtils.createSection(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.chapterCat5.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections++, 1);
		AnonymousResultExportPdfSectionCat.createSectionCat5(request, section5, graphicPath, pageExecutionList, category);
		document.add(chapter);
		return countSections;
	}

	/**
	 * Creates the evolution results chapter.
	 *
	 * @param request       the request
	 * @param index         the index
	 * @param document      the document
	 * @param countSections the count sections
	 * @param numChapter    the num chapter
	 * @param titleFont     the title font
	 * @param graphicPath   the graphic path
	 * @param idExecution   the id execution
	 * @param idObservatory the id observatory
	 * @return the int
	 * @throws Exception the exception
	 */
	protected static int createEvolutionResultsChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, String graphicPath,
			String idExecution, Long idObservatory) throws Exception {
		Map<Date, List<ObservatoryEvaluationForm>> pageObservatoryMap = ResultadosAnonimosObservatorioIntavUtils.resultEvolutionData(idObservatory, Long.valueOf(idExecution));
		Map<Date, Map<Long, Map<String, Integer>>> evolutionResult = ResultadosAnonimosObservatorioIntavUtils.getEvolutionObservatoriesSitesByType(request.getParameter(Constants.ID_OBSERVATORIO),
				request.getParameter(Constants.ID), pageObservatoryMap);
		Map<String, BigDecimal> resultDataA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_A);
		Map<String, BigDecimal> resultDataAA = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_AA);
		Map<String, BigDecimal> resultDataNV = ResultadosAnonimosObservatorioIntavUtils.calculatePercentageApprovalSiteLevel(evolutionResult, Constants.OBS_NV);
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("ob.resAnon.intav.report.chapterEv.title"), index, countSections, numChapter, titleFont);
		AnonymousResultExportPdfSectionEv.createChapterEvolution(messageResources, chapter);
		chapter.newPage();
		Section section1L1 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv1.level1.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections, 1);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Ev1.p1"), ConstantsFont.PARAGRAPH, section1L1);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Ev1.p2"), ConstantsFont.PARAGRAPH, section1L1, Paragraph.ALIGN_JUSTIFIED, true, true);
		section1L1.newPage();
		Section section1 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv1.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section1L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv1(request, section1, graphicPath, resultDataA);
		section1L1.newPage();
		Section section2 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv2.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section1L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv2(request, section2, graphicPath, resultDataAA);
		section1L1.newPage();
		Section section3 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv3.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section1L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv3(request, section3, graphicPath, resultDataNV);
		section1L1.newPage();
		Section section4 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv4.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, chapter, countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv4(request, section4, graphicPath, pageObservatoryMap);
		section4.newPage();
		Section section3L1 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.verification.level1.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				countSections, 1);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Ev5.p1"), ConstantsFont.PARAGRAPH, section3L1, Paragraph.ALIGN_JUSTIFIED, true, true);
		section3L1.newPage();
		Section section5 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv5.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv5(request, section5, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section6 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv6.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv6(request, section6, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section7 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv7.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv7(request, section7, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section8 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv8.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv8(request, section8, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section9 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv9.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1, countSections++,
				2);
		AnonymousResultExportPdfSectionEv.createSectionEv9(request, section9, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section10 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv10.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv10(request, section10, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section11 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv11.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv11(request, section11, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section12 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv12.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv12(request, section12, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section13 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv13.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv13(request, section13, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section14 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv14.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv14(request, section14, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section15 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv15.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv15(request, section15, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section16 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv16.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv16(request, section16, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section17 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv17.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv17(request, section17, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section18 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv18.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv18(request, section18, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section19 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv19.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv19(request, section19, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section20 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv20.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv20(request, section20, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section21 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv21.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv21(request, section21, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section22 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv22.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv22(request, section22, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section23 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv23.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv23(request, section23, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Section section24 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv24.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section3L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv24(request, section24, graphicPath, pageObservatoryMap);
		section3L1.newPage();
		Map<Date, Map<String, BigDecimal>> resultsByAspect = new HashMap<>();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> pageObservatoryEntry : pageObservatoryMap.entrySet()) {
			resultsByAspect.put(pageObservatoryEntry.getKey(),
					ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(CrawlerUtils.getResources(request), pageObservatoryEntry.getValue()));
		}
		Section section4L1 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.aspect.level1.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, countSections,
				1);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Ev25.p1"), ConstantsFont.PARAGRAPH, section4L1, Paragraph.ALIGN_JUSTIFIED, true, true);
		section4L1.newPage();
		Section section25 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv25.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv25(request, section25, graphicPath, resultsByAspect);
		section4L1.newPage();
		Section section26 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv26.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv26(request, section26, graphicPath, resultsByAspect);
		section4L1.newPage();
		Section section27 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv27.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv27(request, section27, graphicPath, resultsByAspect);
		section4L1.newPage();
		Section section28 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv28.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv28(request, section28, graphicPath, resultsByAspect);
		section4L1.newPage();
		Section section29 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.chapterEv29.title"), index, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section4L1,
				countSections++, 2);
		AnonymousResultExportPdfSectionEv.createSectionEv29(request, section29, graphicPath, resultsByAspect);
		document.add(chapter);
		return countSections;
	}

	/**
	 * Creates the summary chapter.
	 *
	 * @param request       the request
	 * @param index         the index
	 * @param document      the document
	 * @param countSections the count sections
	 * @param numChapter    the num chapter
	 * @param titleFont     the title font
	 * @return the int
	 * @throws Exception the exception
	 */
	protected static int createSummaryChapter(HttpServletRequest request, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont) throws Exception {
		Chapter chapter = PDFUtils.createChapterWithTitle(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.summary.title"), index, countSections++, numChapter, titleFont);
		// Incluir las conclusiones
		document.add(chapter);
		return countSections;
	}
}
