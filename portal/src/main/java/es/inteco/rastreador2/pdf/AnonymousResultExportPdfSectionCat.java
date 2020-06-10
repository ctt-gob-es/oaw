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

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPTable;

import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.actionform.semillas.CategoriaForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;

public final class AnonymousResultExportPdfSectionCat {
	private AnonymousResultExportPdfSectionCat() {
	}

	protected static void createChapter4(HttpServletRequest request, Chapter chapter) {
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.4.p1"), ConstantsFont.PARAGRAPH, chapter);
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.4.p2"), ConstantsFont.PARAGRAPH, chapter);
	}
	// **************************************************************************************************
	// RESULTADOS: CATEGORY
	// **************************************************************************************************

	protected static void createSectionCat(HttpServletRequest request, Section section, CategoriaForm category) {
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat.p1", category.getName()), ConstantsFont.PARAGRAPH, section);
	}

	protected static void createSectionCat1(HttpServletRequest request, Section section, String graphicPath, String execution_id, CategoriaForm category) throws Exception {
		try {
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat1.p1"), ConstantsFont.PARAGRAPH, section);
			List<ObservatoryEvaluationForm> pageExecutionList = ResultadosAnonimosObservatorioIntavUtils.getGlobalResultData(execution_id, Long.parseLong(category.getId()), null);
			Map<String, Integer> resultsMap = ResultadosAnonimosObservatorioIntavUtils.getResultsBySiteLevel(pageExecutionList);
			List<GraphicData> labelValueBean = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(CrawlerUtils.getResources(request), resultsMap);
			PDFUtils.addImageToSection(section,
					graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.accessibility.level.allocation.segment.name", category.getOrden()) + ".jpg",
					CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat1.img.alt"), 75);
			PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat.tableTitle", category.getName()), section, 380);
			float[] widths = { 33f, 33f, 33f };
			PdfPTable table = new PdfPTable(widths);
			table.addCell(
					PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.porc.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
					Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.num.portales"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
					Element.ALIGN_CENTER, 0));
			for (GraphicData label : labelValueBean) {
				table.addCell(PDFUtils.createTableCell(label.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
				table.addCell(PDFUtils.createTableCell(label.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
				table.addCell(PDFUtils.createTableCell(label.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			}
			table.setSpacingBefore(ConstantsFont.LINE_SPACE);
			section.add(table);
			// AnonymousResultExportPdfVariableText.createVTextCat1(request, labels, section);
		} catch (Exception e) {
			Logger.putLog("Fallo al crear la sección cat1 de la categoría: " + category.getId(), AnonymousResultExportPdfSectionCat.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	protected static void createSectionCat2(HttpServletRequest request, Section section, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, CategoriaForm category)
			throws Exception {
		try {
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p1"), ConstantsFont.PARAGRAPH, section);
			Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
			SpecialChunk anchor = new SpecialChunk(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p12.anchor"),
					CrawlerUtils.getResources(request).getMessage("anchor.PMPO"), false, ConstantsFont.PARAGRAPH_ANCHOR_FONT);
			anchorMap.put(1, anchor);
			section.add(PDFUtils.createParagraphAnchor(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p12"), anchorMap, ConstantsFont.PARAGRAPH));
			PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p13"), ConstantsFont.PARAGRAPH, section);
			PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.mark.allocation.segment.name", category.getId()) + ".jpg",
					CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.img.alt" + category.getName()), 85);
			/*
			 * List<ObservatorySiteEvaluationForm> result = ResultadosAnonimosObservatorioIntavUtils.createOrderFormLevel(ResultadosAnonimosObservatorioIntavUtils.getSitesListByLevel(conn,
			 * pageExecutionList));
			 * 
			 * if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA){ PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.tableTitle1" ,segment +
			 * " "+ category.getName()), section, 400); float[] widths = {50f, 25f, 25f}; PdfPTable table = new PdfPTable(widths);
			 * table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.portal"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
			 * Element.ALIGN_CENTER, 0)); table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP,
			 * ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0)); table.addCell(PDFUtils.createTableCell(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.level"),
			 * Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
			 * 
			 * for (ObservatorySiteEvaluationForm siteForm: result){ table.addCell(PDFUtils.createTableCell(siteForm.getAcronym() + " (" + siteForm.getName() + ")" , Color.white,
			 * ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 5)); table.addCell(PDFUtils.createTableCell(String.valueOf(siteForm.getScore()), Color.white, ConstantsFont.noteCellFont,
			 * Element.ALIGN_CENTER, 0)); table.addCell(PDFUtils.createTableCell(siteForm.getLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0)); }
			 * 
			 * table.setSpacingBefore(ConstantsFont.LINE_SPACE); section.add(table);
			 * 
			 * section.newPage(); }
			 */
		} catch (Exception e) {
			Logger.putLog("Fallo al crear la sección cat2 de la categoría: " + category.getId(), AnonymousResultExportPdfSectionCat.class, Logger.LOG_LEVEL_ERROR, e);
		}
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p15"), ConstantsFont.PARAGRAPH, section, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p16"), ConstantsFont.PARAGRAPH, section, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p17"), ConstantsFont.PARAGRAPH, section, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat2.p18"), ConstantsFont.PARAGRAPH, section, Element.ALIGN_JUSTIFIED, true, false);
		// java.util.List<ObservatorySiteEvaluationForm> result = ResultadosAnonimosObservatorioIntavUtils.getSitesListByLevel(conn, pageExecutionList, idObservatory);
		// java.util.List<ObservatorySiteEvaluationForm> result2 = ResultadosAnonimosObservatorioIntavUtils.createOrderFormLevel(result);
		// AnonymousResultExportPdfVariableText.createVText4x2(request, result2, section);
	}

	protected static void createSectionCat3(HttpServletRequest request, Section section, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, CategoriaForm category)
			throws Exception {
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		try {
			Map<String, BigDecimal> resultL1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_1);
			Map<String, BigDecimal> resultL2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPoint(pageExecutionList, Constants.OBS_PRIORITY_2);
			ArrayList<String> boldWords = new ArrayList<>();
			boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.Cat.segment") + " " + category.getName());
			section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.Cat3.p1"), boldWords, ConstantsFont.paragraphBoldFont,
					ConstantsFont.PARAGRAPH, true));
			Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
			SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.Cat3.p2.anchor"), messageResources.getMessage("anchor.PMV"), false,
					ConstantsFont.PARAGRAPH_ANCHOR_FONT);
			anchorMap.put(1, anchor);
			section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.Cat3.p2"), anchorMap, ConstantsFont.PARAGRAPH));
			section.newPage();
			Section subSection = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.Cat3.sub1"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section, -1, 2);
			PDFUtils.addImageToSection(subSection, graphicPath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.name") + category.getId() + ".jpg",
					messageResources.getMessage("ob.resAnon.intav.report.Cat3.img.alt", category.getName()), 75);
			java.util.List<LabelValueBean> labelsL1 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIVerificationMidsComparison(messageResources, resultL1);
			PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.Cat3.tableTitle1", category.getName()), subSection, 400);
			java.util.List<String> headers = new ArrayList<>();
			headers.add(messageResources.getMessage("resultados.anonimos.punto.verification"));
			headers.add(messageResources.getMessage("resultados.anonimos.punt.media"));
			PdfPTable table = PDFUtils.createResultTable(labelsL1, headers);
			subSection.add(table);
			Section subSection2 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.Cat3.sub2"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section, -1, 2);
			PDFUtils.addImageToSection(subSection2, graphicPath + messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.name") + category.getId() + ".jpg",
					messageResources.getMessage("ob.resAnon.intav.report.Cat3.img.alt.2", category.getName()), 75);
			java.util.List<LabelValueBean> labelsL2 = ResultadosAnonimosObservatorioIntavUtils.infoLevelIIVerificationMidsComparison(messageResources, resultL2);
			PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.Cat3.tableTitle2", category.getName()), subSection2, 400);
			subSection2.add(PDFUtils.createResultTable(labelsL2, headers));
		} catch (Exception e) {
			Logger.putLog("Fallo al crear la sección cat3 de la categoría: " + category.getId(), AnonymousResultExportPdfSectionCat.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	protected static void createSectionCat4(HttpServletRequest request, Section section, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, CategoriaForm category)
			throws Exception {
		final MessageResources messageResources = CrawlerUtils.getResources(request);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Cat4.p1"), ConstantsFont.PARAGRAPH, section, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Cat4.p2"), ConstantsFont.PARAGRAPH, section, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.Cat4.p3"), ConstantsFont.PARAGRAPH, section, Element.ALIGN_JUSTIFIED, true, false);
		section.newPage();
		Map<String, BigDecimal> results1 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_1);
		Map<String, BigDecimal> results2 = ResultadosAnonimosObservatorioIntavUtils.getVerificationResultsByPointAndModality(pageExecutionList, Constants.OBS_PRIORITY_2);
		Section subSection = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.Cat4.sub1"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section, -1, 2);
		PDFUtils.addImageToSection(subSection, graphicPath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.1.name") + category.getId() + ".jpg",
				messageResources.getMessage("ob.resAnon.intav.report.Cat4.img1.alt", category.getName()), 70);
		PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.Cat4.tableTitle1", category.getName()), subSection, 420);
		section.add(PDFUtils.createTableMod(messageResources, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results1)));
		section.newPage();
		Section subSection2 = PDFUtils.createSection(messageResources.getMessage("ob.resAnon.intav.report.Cat4.sub2"), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, section, -1, 2);
		PDFUtils.addImageToSection(subSection2, graphicPath + messageResources.getMessage("observatory.graphic.modality.by.verification.level.2.name") + category.getId() + ".jpg",
				messageResources.getMessage("ob.resAnon.intav.report.Cat4.img2.alt", category.getName()), 70);
		PDFUtils.createTitleTable(messageResources.getMessage("ob.resAnon.intav.report.Cat4.tableTitle2", category.getName()), subSection2, 420);
		section.add(PDFUtils.createTableMod(messageResources, ResultadosAnonimosObservatorioIntavUtils.infoLevelVerificationModalityComparison(results2)));
	}

	protected static void createSectionCat5(HttpServletRequest request, Section section, String graphicPath, List<ObservatoryEvaluationForm> pageExecutionList, CategoriaForm category)
			throws Exception {
		Map<String, BigDecimal> result = ResultadosAnonimosObservatorioIntavUtils.aspectMidsPuntuationGraphicData(CrawlerUtils.getResources(request), pageExecutionList);
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat5.p1"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.addParagraph(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat5.p2"), ConstantsFont.PARAGRAPH, section);
		PDFUtils.addImageToSection(section, graphicPath + CrawlerUtils.getResources(request).getMessage("observatory.graphic.aspect.mid.name") + category.getId() + ".jpg",
				CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat5.img.alt", category.getName()), 75);
		java.util.List<LabelValueBean> labels = ResultadosAnonimosObservatorioIntavUtils.infoAspectMidsComparison(request, result);
		PDFUtils.createTitleTable(CrawlerUtils.getResources(request).getMessage("ob.resAnon.intav.report.Cat5.tableTitle", category.getName()), section, 380);
		java.util.List<String> headers = new ArrayList<>();
		headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.aspect"));
		headers.add(CrawlerUtils.getResources(request).getMessage("resultados.anonimos.punt.media"));
		section.add(PDFUtils.createResultTable(labels, headers));
		// AnonymousResultExportPdfVariableText.createVText425(request, labels, section);
	}
}
