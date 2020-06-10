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

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;
import static es.inteco.common.ConstantsFont.HALF_LINE_SPACE;
import static es.inteco.common.ConstantsFont.LINE_SPACE;

import java.awt.Color;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfAccesibilidad;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNEEN2019;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosPrimariosObservatorioIntavUtils;

/**
 * Clase encargada de construir la sección 'Resumen de Resultados' del informe PDF del Servicio de Diagnóstico.
 */
public class BasicServiceObservatoryScorePdfSectionBuilder {
	/** The current evaluation page list. */
	private final List<ObservatoryEvaluationForm> currentEvaluationPageList;
	/** The previous evaluation page list. */
	private final List<ObservatoryEvaluationForm> previousEvaluationPageList;
	/** The evolution. */
	private final boolean evolution;
	/** The Constant TEMP_SUBDIR. */
	private static final String TEMP_SUBDIR = "temp";

	/**
	 * Instantiates a new basic service observatory score pdf section builder.
	 *
	 * @param currentEvaluationPageList the current evaluation page list
	 */
	public BasicServiceObservatoryScorePdfSectionBuilder(final List<ObservatoryEvaluationForm> currentEvaluationPageList) {
		this(currentEvaluationPageList, null);
	}

	/**
	 * Instantiates a new basic service observatory score pdf section builder.
	 *
	 * @param currentEvaluationPageList  the current evaluation page list
	 * @param previousEvaluationPageList the previous evaluation page list
	 */
	public BasicServiceObservatoryScorePdfSectionBuilder(final List<ObservatoryEvaluationForm> currentEvaluationPageList, final List<ObservatoryEvaluationForm> previousEvaluationPageList) {
		this.currentEvaluationPageList = currentEvaluationPageList;
		this.previousEvaluationPageList = previousEvaluationPageList;
		evolution = previousEvaluationPageList != null && !previousEvaluationPageList.isEmpty();
	}

	/**
	 * Adds the observatory score summary.
	 *
	 * @param pdfBuilder       the pdf builder
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param file             the file
	 * @throws Exception the exception
	 */
	public void addObservatoryScoreSummary(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, Document document, final PdfTocManager pdfTocManager, final File file)
			throws Exception {
		ScoreForm currentScore = null;
		ScoreForm previousScore = null;
		final Chapter chapter;
		if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad || pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
			chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("pdf.accessibility.global.summary.title").toUpperCase(), pdfTocManager.getIndex(), pdfTocManager.addSection(),
					pdfTocManager.getNumChapter(), ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resumen_resultados");
			final ArrayList<String> boldWords = new ArrayList<>();
			if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
				Section section31 = PDFUtils.createSection(messageResources.getMessage("pdf.accessibility.global.summary.globals"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
						chapter, pdfTocManager.addSection(), 1);
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p1.bold"));
				section31.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p1"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p2.bold"));
				section31.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p2"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p3.bold"));
				section31.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p3"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p4.bold"));
				section31.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p4"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				// Info
				final PdfPTable notice = new PdfPTable(new float[] { 100f });
				notice.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
				notice.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.summary.info"), Constants.GRIS_MUY_CLARO, ConstantsFont.paragraphBoldFont,
						Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, 50));
				section31.add(notice);
				currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
				if (evolution) {
					previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);
				} else {
					previousScore = null;
				}
				section31.add(Chunk.NEWLINE);
			} else if (pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad) {
				chapter.add(Chunk.NEWLINE);
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p1.bold"));
				chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p1"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p2.bold"));
				chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p2"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				boldWords.clear();
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p3.bold"));
				chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p3"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				// Info
				final PdfPTable notice = new PdfPTable(new float[] { 100f });
				notice.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
				notice.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.summary.info"), Constants.GRIS_MUY_CLARO, ConstantsFont.paragraphBoldFont,
						Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, 50));
				chapter.add(notice);
				currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
				if (evolution) {
					previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);
				} else {
					previousScore = null;
				}
				chapter.add(Chunk.NEWLINE);
				final BasicServicePageResultsPdfSectionBuilder observatoryPageResultsSectionBuilder = new BasicServicePageResultsPdfSectionBuilder(currentEvaluationPageList);
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p4.bold"));
				chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p4"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				chapter.add(Chunk.NEWLINE);
				for (ObservatoryEvaluationForm evaluationForm : currentEvaluationPageList) {
					chapter.add(observatoryPageResultsSectionBuilder.createPaginaTableInfoAccesibility(messageResources, evaluationForm));
					for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
						chapter.add(observatoryPageResultsSectionBuilder.createPaginaTableVerificationSummary(messageResources, observatoryLevelForm, ""));
					}
					document.add(chapter);
					pdfTocManager.addChapterCount();
					observatoryPageResultsSectionBuilder.addCheckCodesWithoutLevelsWithoutSection(messageResources, evaluationForm, document, pdfTocManager);
				}
			} else {
				chapter.add(Chunk.NEWLINE);
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p1.bold"));
				chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p1"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p2.bold"));
				chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p2"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				boldWords.clear();
				boldWords.clear();
				boldWords.add(messageResources.getMessage("pdf.accessibility.global.summary.p3.bold"));
				chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("pdf.accessibility.global.summary.p3"), boldWords, ConstantsFont.paragraphBoldFont,
						ConstantsFont.PARAGRAPH, true));
				// Info
				final PdfPTable notice = new PdfPTable(new float[] { 100f });
				notice.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
				notice.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.accessibility.summary.info"), Constants.GRIS_MUY_CLARO, ConstantsFont.paragraphBoldFont,
						Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, 50));
				chapter.add(notice);
				currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
				if (evolution) {
					previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);
				} else {
					previousScore = null;
				}
				chapter.add(Chunk.NEWLINE);
			}
			//// TABLA PUNTUCAION OBSERVATORIO
			final float[] columnWidths;
			if (previousScore != null) {
				columnWidths = new float[] { 0.42f, 0.20f, 0.20f, 0.18f };
			} else {
				columnWidths = new float[] { 0.6f, 0.4f };
			}
			if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
				final PdfPTable tablaRankings = new PdfPTable(columnWidths);
				tablaRankings.setSpacingBefore(LINE_SPACE);
				tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
				tablaRankings.setWidthPercentage(90);
				tablaRankings.setHeaderRows(1);
				tablaRankings.addCell(PDFUtils.createEmptyTableCell());
				tablaRankings.addCell(PDFUtils.createTableCell("Resultado", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				// AVG Score
				tablaRankings.addCell(PDFUtils.createTableCell("Puntuación media del sitio web", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				if (previousScore != null) {
					tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
					tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(currentScore.getTotalScore(), previousScore.getTotalScore()),
							String.valueOf(currentScore.getTotalScore().subtract(previousScore.getTotalScore()).toPlainString()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT,
							DEFAULT_PADDING, -1));
				}
				tablaRankings.completeRow();
				// Adecuación
				tablaRankings.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT,
						DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				if (previousScore != null) {
					tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getLevel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
					tablaRankings.addCell(createEvolutionLevelCell(messageResources, currentScore.getLevel(), previousScore.getLevel()));
				}
				tablaRankings.completeRow();
				// Cumplimiento
				tablaRankings.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.cumplimiento"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT,
						DEFAULT_PADDING, -1));
				// Cumplimiento
				tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getCompliance(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				if (previousScore != null) {
					tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getCompliance(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
					tablaRankings.addCell(createEvolutionLevelCell(messageResources, previousScore.getCompliance(), previousScore.getCompliance()));
				}
				tablaRankings.completeRow();
				chapter.add(tablaRankings);
				chapter.add(Chunk.NEXTPAGE);
				chapter.add(new Paragraph(messageResources.getMessage("observatorio.nivel.cumplimiento.p1.grafica"), ConstantsFont.PARAGRAPH));
				// Gráfica nivel de adecuación
				final String noDataMess = messageResources.getMessage("grafica.sin.datos");
				addLevelAllocationResultsSummary(messageResources, chapter, file, noDataMess);
				chapter.add(Chunk.NEXTPAGE);
				// Puntuación media y nivel de adecuación por página
				PDFUtils.createSection(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.pagina.title"), pdfTocManager.getIndex(),
						ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
				addResultsByPage(messageResources, chapter, file, currentEvaluationPageList, noDataMess);
				// Puntuación media por verificación
				chapter.add(Chunk.NEXTPAGE);
				Section section = PDFUtils.createSection(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.title"), pdfTocManager.getIndex(),
						ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
				section.add(new Paragraph(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.p1.basic.service"), ConstantsFont.PARAGRAPH));
				section.add(Chunk.NEWLINE);
				section.add(new Paragraph(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.p2"), ConstantsFont.PARAGRAPH));
				section.add(Chunk.NEWLINE);
				section.add(Chunk.NEXTPAGE);
				if (pdfBuilder instanceof AnonymousResultExportPdfUNEEN2019) {
					Section section1 = PDFUtils.createSection(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.title.level1"), pdfTocManager.getIndex(),
							ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, section, pdfTocManager.addSection(), 1);
					addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section1, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_1);
					section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, previousScore, Constants.OBS_PRIORITY_1));
					Section section2 = PDFUtils.createSection(messageResources.getMessage("observatorio.nivel.cumplimiento.media.verificacion.title.level2"), pdfTocManager.getIndex(),
							ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, section, pdfTocManager.addSection(), 1);
					addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section2, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_2);
					section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, previousScore, Constants.OBS_PRIORITY_2));
				}
			} else if (!(pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad)) {
				final PdfPTable tablaRankings = new PdfPTable(columnWidths);
				tablaRankings.setSpacingBefore(LINE_SPACE);
				tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
				tablaRankings.setWidthPercentage(90);
				tablaRankings.setHeaderRows(1);
				tablaRankings.addCell(PDFUtils.createEmptyTableCell());
				tablaRankings.addCell(PDFUtils.createTableCell("Resultado", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				// AVG Score
				tablaRankings.addCell(PDFUtils.createTableCell("Puntuación media del sitio web", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.completeRow();
				// Adecuación
				tablaRankings.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT,
						DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				chapter.add(tablaRankings);
				chapter.add(Chunk.NEWLINE);
			}
			// This chapter was added previously in this type of report
			if (!(pdfBuilder instanceof AnonymousResultExportPdfAccesibilidad)) {
				document.add(chapter);
			}
			pdfTocManager.addChapterCount();
		} else {
			chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("observatorio.puntuacion.resultados.resumen").toUpperCase(), pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
			final ArrayList<String> boldWord = new ArrayList<>();
			chapter.add(
					PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p1"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
			boldWord.clear();
			boldWord.add(messageResources.getMessage("resultados.primarios.4.p2.bold1"));
			chapter.add(
					PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p2"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
			boldWord.clear();
			boldWord.add(messageResources.getMessage("resultados.primarios.4.p3.bold1"));
			chapter.add(
					PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p3"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
			currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
			if (evolution) {
				previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);
			} else {
				previousScore = null;
			}
			// TABLA PUNTUACION OBSERVATORIO
			// Tabla con evolucion
			final float[] columnWidths;
			if (previousScore != null) {
				columnWidths = new float[] { 0.6f, 0.4f, 0.4f, 0.4f };
			} else {
				columnWidths = new float[] { 0.6f, 0.4f };
			}
			final PdfPTable tablaRankings = new PdfPTable(columnWidths);
			tablaRankings.setSpacingBefore(LINE_SPACE);
			tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
			tablaRankings.setWidthPercentage(90);
			tablaRankings.setHeaderRows(1);
			tablaRankings.addCell(PDFUtils.createEmptyTableCell());
			tablaRankings.addCell(PDFUtils.createTableCell("Resultado Actual", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (previousScore != null) {
				tablaRankings.addCell(PDFUtils.createTableCell("Resultado Anterior", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
			tablaRankings.addCell(
					PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (previousScore != null) {
				tablaRankings.addCell(PDFUtils.createTableCell(previousScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(createEvolutionLevelCell(messageResources, currentScore.getLevel(), previousScore.getLevel()));
			}
			tablaRankings.completeRow();
			tablaRankings.addCell(PDFUtils.createTableCell("Puntuación Media del Portal", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			if (previousScore != null) {
				tablaRankings
						.addCell(PDFUtils.createTableCell(previousScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
				tablaRankings.addCell(PDFUtils.createTableCell(getEvolutionImage(currentScore.getTotalScore(), previousScore.getTotalScore()),
						String.valueOf(currentScore.getTotalScore().subtract(previousScore.getTotalScore()).toPlainString()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT,
						DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
			chapter.add(tablaRankings);
			final String noDataMess = messageResources.getMessage("grafica.sin.datos");
			// Obtener lista resultados iteración anterior
			addLevelAllocationResultsSummary(messageResources, chapter, file, noDataMess);
			Section section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion1"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L,
					chapter, pdfTocManager.addSection(), 1);
			PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.41.p1"), ConstantsFont.PARAGRAPH, section);
			addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_1);
			section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, previousScore, Constants.OBS_PRIORITY_1));
			section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion2"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
					pdfTocManager.addSection(), 1);
			PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.42.p1"), ConstantsFont.PARAGRAPH, section);
			addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_2);
			section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, previousScore, Constants.OBS_PRIORITY_2));
			PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuacion.pagina"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
					pdfTocManager.addSection(), 1);
			addResultsByPage(messageResources, chapter, file, currentEvaluationPageList, noDataMess);
			document.add(chapter);
			pdfTocManager.addChapterCount();
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
			final java.util.List<ObservatoryEvaluationForm> currentEvaluationPageList, final java.util.List<ObservatoryEvaluationForm> previousEvaluationPageList, final String noDataMess,
			final boolean isBasicService) throws Exception {
		final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);
		final Map<String, Integer> previousResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(previousEvaluationPageList);
		final java.util.List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);
		final java.util.List<GraphicData> previousGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, previousResultsByLevel);
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
	 * Creates the evolution level cell.
	 *
	 * @param messageResources the message resources
	 * @param currentLevel     the current level
	 * @param previousLevel    the previous level
	 * @return the pdf P cell
	 */
	/*
	 * Crea una celda PdfPCell para una tabla del informa PDF con la evolución del nivel de accesibilidad.
	 *
	 * @param messageResources
	 * 
	 * @param currentLevel String nivel de accesibilidad actual.
	 * 
	 * @param previousLevel String nivel de accesibilidad de la iteración anterior.
	 * 
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
	 * Adds the level allocation results summary.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
	 * @param file             the file
	 * @param noDataMess       the no data mess
	 * @throws Exception the exception
	 */
	private void addLevelAllocationResultsSummary(final MessageResources messageResources, final Section section, final File file, final String noDataMess) throws Exception {
		if (previousEvaluationPageList != null && evolution) {
			addLevelAllocationResultsSummaryWithEvolution(messageResources, section, file, noDataMess);
		} else {
			addLevelAllocationResultsSummaryNoEvolution(messageResources, section, file, noDataMess);
		}
	}

	/**
	 * Adds the level allocation results summary no evolution.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
	 * @param file             the file
	 * @param noDataMess       the no data mess
	 * @throws Exception the exception
	 */
	private void addLevelAllocationResultsSummaryNoEvolution(final MessageResources messageResources, final Section section, final File file, final String noDataMess) throws Exception {
		final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);
		final List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);
		final String filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test.jpg";
		final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
		ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, currentEvaluationPageList, title, filePath, noDataMess);
		final float[] columnsWidth = new float[] { 35f, 30f, 35f };
		final PdfPTable table = new PdfPTable(columnsWidth);
		table.setWidthPercentage(90);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.num.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.porc.paginas"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.completeRow();
		for (GraphicData actualData : currentGlobalAccessibilityLevel) {
			table.addCell(PDFUtils.createTableCell(actualData.getAdecuationLevel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(actualData.getNumberP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.addCell(PDFUtils.createTableCell(actualData.getPercentageP(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
			table.completeRow();
		}
		table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
		createImage(section, filePath, title);
		section.add(table);
	}

	/**
	 * Creates the image.
	 *
	 * @param section  the section
	 * @param filePath the file path
	 */
	private void createImage(final Section section, final String filePath, final String alternativeText) {
		// TODO add alt
		final com.itextpdf.text.Image image = PDFUtils.createImage(filePath, alternativeText);
		if (image != null) {
			image.scalePercent(60);
			image.setAlignment(Element.ALIGN_CENTER);
			image.setSpacingAfter(ConstantsFont.LINE_SPACE);
			image.setAlt(alternativeText);
			section.add(image);
		}
	}

	/**
	 * Adds the level allocation results summary with evolution.
	 *
	 * @param messageResources the message resources
	 * @param section          the section
	 * @param file             the file
	 * @param noDataMess       the no data mess
	 * @throws Exception the exception
	 */
	private void addLevelAllocationResultsSummaryWithEvolution(final MessageResources messageResources, final Section section, final File file, final String noDataMess) throws Exception {
		final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);
		final Map<String, Integer> previousResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(previousEvaluationPageList);
		final List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);
		final List<GraphicData> previousGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, previousResultsByLevel);
		final String filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test.jpg";
		final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
		ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, currentEvaluationPageList, title, filePath, noDataMess);
		createImage(section, filePath, title);
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
		table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
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
	private void addMidsComparationByVerificationLevelGraphic(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final Section section, final File file,
			final List<ObservatoryEvaluationForm> evaList, final String noDataMess, final String level) throws Exception {
		final String title;
		final String filePath;
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.1.title");
			filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test2.jpg";
		} else {
			title = messageResources.getMessage("observatory.graphic.verification.mid.comparation.level.2.title");
			filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test3.jpg";
		}
		final PropertiesManager pmgr = new PropertiesManager();
		pdfBuilder.getMidsComparationByVerificationLevelGraphic(messageResources, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"),
				true);
		createImage(section, filePath, title);
	}

	/**
	 * Creates the observatory verification score table.
	 *
	 * @param messageResources the message resources
	 * @param actualScore      the actual score
	 * @param previousScore    the previous score
	 * @param level            the level
	 * @return the pdf P table
	 */
	private PdfPTable createObservatoryVerificationScoreTable(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level) {
		if (previousScore != null && evolution) {
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
	private PdfPTable createObservatoryVerificationScoreTableNoEvolution(final MessageResources messageResources, final ScoreForm actualScore, final String level) {
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			return createObservatoryVerificationScoreTableNoEvolutionLevel(messageResources, actualScore.getVerifications1(), messageResources.getMessage("observatorio.puntuacion.nivel.1"),
					actualScore.getScoreLevel1());
		} else {
			return createObservatoryVerificationScoreTableNoEvolutionLevel(messageResources, actualScore.getVerifications2(), messageResources.getMessage("observatorio.puntuacion.nivel.2"),
					actualScore.getScoreLevel2());
		}
	}

	/**
	 * Creates the observatory verification score table no evolution level.
	 *
	 * @param messageResources the message resources
	 * @param verifications    the verifications
	 * @param scoreLabel       the score label
	 * @param scoreValue       the score value
	 * @return the pdf P table
	 */
	private PdfPTable createObservatoryVerificationScoreTableNoEvolutionLevel(final MessageResources messageResources, final List<LabelValueBean> verifications, final String scoreLabel,
			final BigDecimal scoreValue) {
		final float[] columnsWidths = new float[] { 0.65f, 0.35f };
		final PdfPTable table = new PdfPTable(columnsWidths);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.setHeaderRows(1);
		for (LabelValueBean actualLabelValueBean : verifications) {
			table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		table.addCell(PDFUtils.createTableCell(scoreLabel, Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(scoreValue.toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
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
	private PdfPTable createObservatoryVerificationScoreTableWithEvolution(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level) {
		if (level.equals(Constants.OBS_PRIORITY_1)) {
			return createObservatoryVerificationScoreTableWithEvolutionLevel(messageResources, messageResources.getMessage("observatorio.puntuacion.nivel.1"), actualScore.getVerifications1(),
					previousScore.getVerifications1(), actualScore.getScoreLevel1(), previousScore.getScoreLevel1());
		} else {
			return createObservatoryVerificationScoreTableWithEvolutionLevel(messageResources, messageResources.getMessage("observatorio.puntuacion.nivel.2"), actualScore.getVerifications2(),
					previousScore.getVerifications2(), actualScore.getScoreLevel2(), previousScore.getScoreLevel2());
		}
	}

	/**
	 * Creates the observatory verification score table with evolution level.
	 *
	 * @param messageResources      the message resources
	 * @param averageResultLabel    the average result label
	 * @param actualVerifications   the actual verifications
	 * @param previousVerifications the previous verifications
	 * @param actualScoreLevel      the actual score level
	 * @param previousScoreLevel    the previous score level
	 * @return the pdf P table
	 */
	private PdfPTable createObservatoryVerificationScoreTableWithEvolutionLevel(final MessageResources messageResources, final String averageResultLabel,
			final List<LabelValueBean> actualVerifications, final List<LabelValueBean> previousVerifications, final BigDecimal actualScoreLevel, final BigDecimal previousScoreLevel) {
		final float[] columnsWidths = new float[] { 0.55f, 0.25f, 0.20f };
		final PdfPTable table = new PdfPTable(columnsWidths);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell("Evolución", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.setHeaderRows(1);
		final ListIterator<LabelValueBean> actualScoreIterator = actualVerifications.listIterator();
		final ListIterator<LabelValueBean> previousScoreIterator = previousVerifications.listIterator();
		while (actualScoreIterator.hasNext()) {
			final LabelValueBean actualLabelValueBean = actualScoreIterator.next();
			final LabelValueBean previousLabelValueBean = previousScoreIterator.next();
			table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
			table.addCell(PDFUtils.createTableCell(actualLabelValueBean.getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			table.addCell(createEvolutionDifferenceCellValue(actualLabelValueBean.getValue(), previousLabelValueBean.getValue(), Color.WHITE));
		}
		table.addCell(PDFUtils.createTableCell(averageResultLabel, Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(actualScoreLevel.toString(), Constants.GRIS_MUY_CLARO, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		table.addCell(createEvolutionDifferenceCellValue(actualScoreLevel, previousScoreLevel, Color.WHITE));
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
	private PdfPCell createEvolutionDifferenceCellValue(final String actualValue, final String previousValue, final Color color) {
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
	private PdfPCell createEvolutionDifferenceCellValue(final BigDecimal actualValue, final BigDecimal previousValue, final Color color) {
		return PDFUtils.createTableCell(getEvolutionImage(actualValue, previousValue), new DecimalFormat("+0.00;-0.00").format(actualValue.subtract(previousValue)), color, ConstantsFont.noteCellFont,
				Element.ALIGN_CENTER, DEFAULT_PADDING / 2, -1);
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
	private void addResultsByPage(final MessageResources messageResources, final Chapter chapter, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess)
			throws Exception {
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.43.p1"), ConstantsFont.PARAGRAPH, chapter);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.43.p2"), ConstantsFont.PARAGRAPH, chapter);
		chapter.add(Chunk.NEWLINE);
		final String title = messageResources.getMessage("observatory.graphic.score.by.page.title");
		final String filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test4.jpg";
		ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(messageResources, evaList, title, filePath, noDataMess);
		final Image image = PDFUtils.createImage(filePath, title);
		if (image != null) {
			image.scalePercent(70);
			image.setAlignment(Element.ALIGN_CENTER);
			chapter.add(image);
		}
		chapter.add(Chunk.NEWLINE);
		final float[] widths = { 30f, 30f, 40f };
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
	 * Gets the evolution image.
	 *
	 * @param actualValue   the actual value
	 * @param previousValue the previous value
	 * @return the evolution image
	 */
	private Image getEvolutionImage(final BigDecimal actualValue, final BigDecimal previousValue) {
		return getEvolutionImage(actualValue.compareTo(previousValue));
	}

	/**
	 * Gets the evolution image.
	 *
	 * @param compareValue the compare value
	 * @return the evolution image
	 */
	private Image getEvolutionImage(final int compareValue) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (compareValue > 0) {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.increase"), "Aumenta");
		} else if (compareValue < 0) {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.decrease"), "Disminuye");
		} else {
			return PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.evolution.same"), "Se mantiene");
		}
	}
}
