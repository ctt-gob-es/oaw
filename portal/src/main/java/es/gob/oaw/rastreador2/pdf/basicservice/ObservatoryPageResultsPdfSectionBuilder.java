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

import static es.inteco.common.Constants.INTAV_PROPERTIES;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.MessageResources;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import es.gob.oaw.rastreador2.pdf.utils.CheckDescriptionsManager;
import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.dao.TAnalisisAccesibilidadDAO;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.intav.form.ProblemForm;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

/**
 * Created by mikunis on 1/18/17.
 */
public class ObservatoryPageResultsPdfSectionBuilder {
	/** The current evaluation page list. */
	protected final List<ObservatoryEvaluationForm> currentEvaluationPageList;

	/**
	 * Instantiates a new observatory page results pdf section builder.
	 *
	 * @param currentEvaluationPageList the current evaluation page list
	 */
	public ObservatoryPageResultsPdfSectionBuilder(final List<ObservatoryEvaluationForm> currentEvaluationPageList) {
		this.currentEvaluationPageList = currentEvaluationPageList;
	}

	/**
	 * Adds the W 3 C copyright.
	 *
	 * @param subSubSection the sub sub section
	 * @param check         the check
	 */
	protected static void addW3CCopyright(Section subSubSection, String check) {
		final PropertiesManager PMGR = new PropertiesManager();
		final Paragraph p = new Paragraph();
		p.setAlignment(Paragraph.ALIGN_RIGHT);
		Anchor anchor = null;
		if ("232".equals(check)) { // PMGR.getValue("check.properties",
									// "doc.valida.especif"))) {
			anchor = new Anchor(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright"), ConstantsFont.MORE_INFO_FONT);
			anchor.setReference(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
		} else if (EvaluatorUtils.isCssValidationCheck(Integer.parseInt(check))) {
			anchor = new Anchor(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.css.copyright"), ConstantsFont.MORE_INFO_FONT);
			anchor.setReference(PMGR.getValue(Constants.PDF_PROPERTIES, "pdf.w3c.html.copyright.link"));
		}
		p.add(anchor);
		subSubSection.add(p);
	}

	/**
	 * Adds the page results.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param withOutLevels    the with out levels
	 * @throws Exception the exception
	 */
	public void addPageResults(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, boolean withOutLevels) throws Exception {
		int counter = 1;
		for (ObservatoryEvaluationForm evaluationForm : currentEvaluationPageList) {
			final String chapterTitle = messageResources.getMessage("observatory.graphic.score.by.page.label.extended", counter);
			final Chapter chapter = PDFUtils.createChapterWithTitle(chapterTitle, pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter(),
					ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resultados_page_" + counter);
			chapter.add(createPaginaTableInfo(messageResources, evaluationForm));
			// Creación de las tablas resumen de resultado por verificación de
			// cada página
			for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
				chapter.add(createPaginaTableVerificationSummary(messageResources, observatoryLevelForm,
						messageResources.getMessage("pdf.accessibility.page.result.graphict.title") + " " + getPriorityName(messageResources, observatoryLevelForm.getName())));
			}
			if (withOutLevels) {
				addCheckCodesWithoutLevels(messageResources, evaluationForm, chapter, pdfTocManager);
			} else {
				addCheckCodes(messageResources, evaluationForm, chapter);
			}
			boolean hasProblems = false;
			for (ObservatoryLevelForm priority : evaluationForm.getGroups()) {
				if (hasProblems(priority)) {
					hasProblems = true;
					break;
				}
			}
			if (hasProblems) {
				final SpecialChunk externalLink = new SpecialChunk(messageResources.getMessage("observatory.servicio.diagnostico.url"), ConstantsFont.ANCHOR_FONT);
				externalLink.setExternalLink(true);
				externalLink.setAnchor(messageResources.getMessage("observatory.servicio.diagnostico.url"));
				final Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
				specialChunkMap.put(1, externalLink);
				PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.errores.mas.info"), specialChunkMap, ConstantsFont.PARAGRAPH);
				final PdfPTable notice = new PdfPTable(1);
				notice.setSpacingBefore(25f);
				PdfPCell cell = new PdfPCell();
				cell.setBackgroundColor(Constants.BC_GRIS_MUY_CLARO);
				cell.addElement(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.errores.mas.info"), specialChunkMap, ConstantsFont.PARAGRAPH));
				cell.setPadding(10f);
				notice.addCell(cell);
				notice.setWidthPercentage(100f);
				chapter.add(notice);
			}
			document.add(chapter);
			document.add(Chunk.NEWLINE);
			pdfTocManager.addChapterCount();
			counter++;
		}
	}

	/**
	 * Adds the page results accesibility.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param withOutLevels    the with out levels
	 * @throws Exception the exception
	 */
	public void addPageResultsAccesibility(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, boolean withOutLevels) throws Exception {
		int counter = 1;
		for (ObservatoryEvaluationForm evaluationForm : currentEvaluationPageList) {
			final String chapterTitle = messageResources.getMessage("observatory.graphic.score.by.page.label", counter);
			final Chapter chapter = PDFUtils.createChapterWithTitle(chapterTitle, pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter(),
					ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resultados_page_" + counter);
			chapter.add(createPaginaTableInfoAccesibility(messageResources, evaluationForm));
			// Creación de las tablas resumen de resultado por verificación de
			// cada página
			for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
				Section section = PDFUtils.createSection("Verificaciones", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
				section.add(createPaginaTableVerificationSummary(messageResources, observatoryLevelForm, "Verificaciones"));
			}
			if (withOutLevels) {
				addCheckCodesWithoutLevels(messageResources, evaluationForm, chapter, pdfTocManager);
			} else {
				addCheckCodes(messageResources, evaluationForm, chapter);
			}
			final SpecialChunk externalLink = new SpecialChunk(messageResources.getMessage("observatory.servicio.diagnostico.url"), ConstantsFont.ANCHOR_FONT);
			externalLink.setExternalLink(true);
			externalLink.setAnchor(messageResources.getMessage("observatory.servicio.diagnostico.url"));
			final Map<Integer, SpecialChunk> specialChunkMap = new HashMap<>();
			specialChunkMap.put(1, externalLink);
			chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.errores.mas.info"), specialChunkMap, ConstantsFont.PARAGRAPH));
			document.add(chapter);
			pdfTocManager.addChapterCount();
			counter++;
		}
	}

	/**
	 * Creates the pagina table info.
	 *
	 * @param messageResources the message resources
	 * @param evaluationForm   the evaluation form
	 * @return the pdf P table
	 */
	protected PdfPTable createPaginaTableInfo(final MessageResources messageResources, final ObservatoryEvaluationForm evaluationForm) {
		final String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
		final String url = evaluationForm.getUrl();
		final BigDecimal puntuacionMedia = evaluationForm.getScore();
		final String nivelAdecuacion = ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm));
		final List<BigDecimal> puntuacionesMediasNivel = new ArrayList<>();
		for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
			puntuacionesMediasNivel.add(observatoryLevelForm.getScore());
		}
		final float[] widths = { 0.22f, 0.78f };
		final PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setSpacingAfter(0);
		// Titulo
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.title"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT,
				DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(title, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		// URL
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT,
				DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createLinkedTableCell(url, url, Color.WHITE, Element.ALIGN_LEFT, DEFAULT_PADDING));
		// Puntuación Media Página
		table.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("resultados.pagina.puntuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(puntuacionMedia.toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		// Nivel de Adecuación (a.k.a. Modalidad)
		table.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(nivelAdecuacion, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		// Puntuaciones Medias Nivel Accesibilidad
//		int contador = 1;
//		for (BigDecimal puntuacionMediaNivel : puntuacionesMediasNivel) {
//			if (Constants.MESSAGE_RESOURCES_UNE_EN2019.equals(messageResources.getConfig())) {
//				table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.resultpage.media", new String[] { (contador == 1) ? "A" : "AA" }), Constants.VERDE_C_MP,
//						ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
//			} else {
//				table.addCell(PDFUtils.createTableCell(messageResources.getMessage("pdf.resultpage.media", new String[] { String.valueOf(contador) }), Constants.VERDE_C_MP,
//						ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
//			}
//			table.addCell(PDFUtils.createTableCell(puntuacionMediaNivel.toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
//			contador++;
//		}
		return table;
	}

	/**
	 * Creates the pagina table info accesibility.
	 *
	 * @param messageResources the message resources
	 * @param evaluationForm   the evaluation form
	 * @return the pdf P table
	 */
	protected PdfPTable createPaginaTableInfoAccesibility(final MessageResources messageResources, final ObservatoryEvaluationForm evaluationForm) {
		final String title = BasicServiceUtils.getTitleDocFromContent(evaluationForm.getSource(), false);
		final String url = evaluationForm.getUrl();
		final BigDecimal puntuacionMedia = evaluationForm.getScore();
		final String nivelAdecuacion = ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm));
		final List<BigDecimal> puntuacionesMediasNivel = new ArrayList<>();
		for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
			puntuacionesMediasNivel.add(observatoryLevelForm.getScore());
		}
		final float[] widths = { 0.22f, 0.78f };
		final PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setSpacingAfter(0);
		// URL
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT,
				DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createLinkedTableCell(url, url, Color.WHITE, Element.ALIGN_LEFT, DEFAULT_PADDING));
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.urls.accesibilidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
		String urls = "";
		try {
			// Only returns one value
			urls = TAnalisisAccesibilidadDAO.getUrls(DataBaseManager.getConnection(), evaluationForm.getIdAnalysis());
			table.addCell(PDFUtils.createTableCell(urls, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));// if (urls != null &&
		} catch (Exception e) {
			Logger.putLog("Error al obtener las urls de accesibilidad analizadas", ObservatoryPageResultsPdfSectionBuilder.class, Logger.LOG_LEVEL_ERROR);
		}
		// Puntuación Media Página
		table.addCell(PDFUtils.createTableCell("Puntuación", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(puntuacionMedia.toPlainString(), Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		// Nivel de Adecuación (a.k.a. Modalidad)
		table.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING, -1));
		table.addCell(PDFUtils.createTableCell(nivelAdecuacion, Color.WHITE, ConstantsFont.descriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
		return table;
	}

	/**
	 * Gets the priority name.
	 *
	 * @param messageResources the message resources
	 * @param name             the name
	 * @return the priority name
	 */
	protected String getPriorityName(final MessageResources messageResources, final String name) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (name.equals(pmgr.getValue(INTAV_PROPERTIES, "priority.1"))) {
			return messageResources.getMessage("observatorio.nivel.analisis.1");
		} else if (name.equals(pmgr.getValue(INTAV_PROPERTIES, "priority.2"))) {
			return messageResources.getMessage("observatorio.nivel.analisis.2");
		} else {
			return null;
		}
	}

	/**
	 * Creates the pagina table verification summary.
	 *
	 * @param messageResources     the message resources
	 * @param observatoryLevelForm the observatory level form
	 * @param tableHeader          the table header
	 * @return the pdf P table
	 */
	protected PdfPTable createPaginaTableVerificationSummary(final MessageResources messageResources, final ObservatoryLevelForm observatoryLevelForm, final String tableHeader) {
		final float[] widths = { 0.60f, 0.20f, 0.20f };
		final PdfPTable table = new PdfPTable(widths);
		PdfPCell headerCell = new PdfPCell();
		headerCell.setColspan(3);
		headerCell.setPhrase(new Phrase(tableHeader, ConstantsFont.paragraphBoldFont));
		headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerCell.setBackgroundColor(BaseColor.WHITE);
		headerCell.setBorder(0);
		table.addCell(headerCell);
		table.setSpacingBefore(10);
		table.setSpacingAfter(5);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
				Element.ALIGN_CENTER, 0));
		table.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.valor"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		table.addCell(
				PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
		for (ObservatorySuitabilityForm observatorySuitabilityForm : observatoryLevelForm.getSuitabilityGroups()) {
			for (ObservatorySubgroupForm observatorySubgroupForm : observatorySuitabilityForm.getSubgroups()) {
				String value = null;
				String modality = null;
				Font fuente = null;
				final int observatorySubgroupFormValue = observatorySubgroupForm.getValue();
				try {
					if (observatorySubgroupFormValue == Constants.OBS_VALUE_GREEN_ONE) {
						value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.uno");
						modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
						fuente = ConstantsFont.descriptionFont;
					} else if (observatorySubgroupFormValue == Constants.OBS_VALUE_GREEN_ZERO) {
						// value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
						value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero.pasa");
						modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
						fuente = ConstantsFont.WARNING_FONT;
					} else if (observatorySubgroupFormValue == Constants.OBS_VALUE_RED_ZERO) {
						value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero");
						modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla");
						fuente = ConstantsFont.descriptionFontRed;
					} else if (observatorySubgroupFormValue == Constants.OBS_VALUE_NOT_SCORE) {
						value = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.noPuntua");
						modality = messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa");
						fuente = ConstantsFont.descriptionFont;
					}
				} catch (Exception e) {
					Logger.putLog("Error al crear tabla, resultados primarios.", BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR, e);
				}
				final PdfPCell descriptionCell = new PdfPCell(new Paragraph(messageResources.getMessage(observatorySubgroupForm.getDescription()), ConstantsFont.descriptionFont));
				final PdfPCell valueCell = PDFUtils.createTableCell(value, Color.WHITE, fuente, Element.ALIGN_CENTER, 0);
				final PdfPCell modalityCell;
				if (modality != null && modality.equals(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"))) {
					modalityCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.pasa"), Color.WHITE, fuente, Element.ALIGN_CENTER, 0);
				} else {
					modalityCell = PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.modalidad.falla"), Color.WHITE, fuente, Element.ALIGN_CENTER, 0);
				}
				modalityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				modalityCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(descriptionCell);
				table.addCell(valueCell);
				table.addCell(modalityCell);
			}
		}
		return table;
	}

	/**
	 * Adds the check codes.
	 *
	 * @param messageResources the message resources
	 * @param evaluationForm   the evaluation form
	 * @param chapter          the chapter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void addCheckCodes(final MessageResources messageResources, final ObservatoryEvaluationForm evaluationForm, final Chapter chapter) throws IOException {
		for (ObservatoryLevelForm priority : evaluationForm.getGroups()) {
			if (hasProblems(priority)) {
				final Section prioritySection = PDFUtils.createSection(getPriorityName(messageResources, priority), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, 1, 0);
				for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
					if (hasProblems(level)) {
						final Section levelSection = PDFUtils.createSection(getLevelName(messageResources, level), null, ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L, prioritySection, 1, 0);
						for (ObservatorySubgroupForm verification : level.getSubgroups()) {
							if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
								for (ProblemForm problem : verification.getProblems()) {
									final PdfPTable tablaVerificacionProblema = createTablaVerificacionProblema(messageResources, levelSection, verification, problem);
									if (problem.getCheck().equals("232") || EvaluatorUtils.isCssValidationCheck(Integer.parseInt(problem.getCheck()))) {
										addW3CCopyright(levelSection, problem.getCheck());
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Creates the tabla verificacion problema.
	 *
	 * @param messageResources the message resources
	 * @param levelSection     the level section
	 * @param verification     the verification
	 * @param problem          the problem
	 * @return the pdf P table
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected PdfPTable createTablaVerificacionProblema(final MessageResources messageResources, final Section levelSection, final ObservatorySubgroupForm verification, final ProblemForm problem)
			throws IOException {
		final PropertiesManager pmgr = new PropertiesManager();
		final PdfPTable tablaVerificacionProblema = new PdfPTable(new float[] { 0.13f, 0.87f });
		tablaVerificacionProblema.setSpacingBefore(10);
		tablaVerificacionProblema.setWidthPercentage(100);
		levelSection.add(tablaVerificacionProblema);
		final PdfPCell verificacion = PDFUtils.createTableCell(messageResources.getMessage(verification.getDescription()), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT,
				DEFAULT_PADDING, -1);
		verificacion.setColspan(2);
		tablaVerificacionProblema.addCell(verificacion);
		final String problema;
		final Font font;
		if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.medium"))) {
			problema = messageResources.getMessage("pdf.accessibility.bs.warning");
			font = ConstantsFont.WARNING_FONT;
		} else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.high"))) {
			problema = messageResources.getMessage("pdf.accessibility.bs.problem");
			font = ConstantsFont.PROBLEM_FONT;
		} else if (problem.getType().equals(pmgr.getValue(Constants.INTAV_PROPERTIES, "confidence.level.cannottell"))) {
			problema = messageResources.getMessage("pdf.accessibility.bs.info");
			font = ConstantsFont.CANNOTTELL_FONT;
		} else {
			problema = "";
			font = ConstantsFont.CANNOTTELL_FONT;
		}
		final PdfPCell celdaProblema = PDFUtils.createTableCell(problema, Color.WHITE, font, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
		celdaProblema.setBorder(0);
		celdaProblema.setVerticalAlignment(Element.ALIGN_TOP);
		tablaVerificacionProblema.addCell(celdaProblema);
		final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
		final PdfPCell comprobacion = PDFUtils.createTableCell(StringUtils.removeHtmlTags(checkDescriptionsManager.getErrorMessage(problem.getCheck())), Color.WHITE,
				ConstantsFont.strongDescriptionFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1);
		comprobacion.setBorder(0);
		comprobacion.setVerticalAlignment(Element.ALIGN_TOP);
		tablaVerificacionProblema.addCell(comprobacion);
		return tablaVerificacionProblema;
	}

	/**
	 * Checks for problems.
	 *
	 * @param priority the priority
	 * @return true, if successful
	 */
	protected boolean hasProblems(final ObservatoryLevelForm priority) {
		for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
			for (ObservatorySubgroupForm verification : level.getSubgroups()) {
				if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks for problems.
	 *
	 * @param level the level
	 * @return true, if successful
	 */
	protected boolean hasProblems(final ObservatorySuitabilityForm level) {
		for (ObservatorySubgroupForm verification : level.getSubgroups()) {
			if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the level name.
	 *
	 * @param messageResources the message resources
	 * @param level            the level
	 * @return the level name
	 */
	protected String getLevelName(final MessageResources messageResources, final ObservatorySuitabilityForm level) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (level.getName().equalsIgnoreCase(pmgr.getValue(INTAV_PROPERTIES, "priority.1.level"))) {
			return messageResources.getMessage("first.level.bs");
		} else if (level.getName().equalsIgnoreCase(pmgr.getValue(INTAV_PROPERTIES, "priority.2.level"))) {
			return messageResources.getMessage("second.level.bs");
		} else {
			return "";
		}
	}

	/**
	 * Gets the priority name.
	 *
	 * @param messageResources the message resources
	 * @param priority         the priority
	 * @return the priority name
	 */
	protected String getPriorityName(final MessageResources messageResources, final ObservatoryLevelForm priority) {
		final PropertiesManager pmgr = new PropertiesManager();
		if (priority.getName().equals(pmgr.getValue(INTAV_PROPERTIES, "priority.1"))) {
			return messageResources.getMessage("priority1.bs");
		} else if (priority.getName().equals(pmgr.getValue(INTAV_PROPERTIES, "priority.2"))) {
			return messageResources.getMessage("priority2.bs");
		} else {
			return "";
		}
	}

	/**
	 * Crear las incidencias sin niveles, sólo con prioridades como será en la revisión de la metodología UNE-2012.
	 *
	 * @param messageResources the message resources
	 * @param evaluationForm   the evaluation form
	 * @param chapter          the chapter
	 * @param pdfTocManager    the pdf toc manager
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void addCheckCodesWithoutLevels(final MessageResources messageResources, final ObservatoryEvaluationForm evaluationForm, final Chapter chapter, final PdfTocManager pdfTocManager)
			throws IOException {
		for (ObservatoryLevelForm priority : evaluationForm.getGroups()) {
			if (hasProblems(priority)) {
				chapter.add(Chunk.NEXTPAGE);
				// pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter()
				final Section prioritySection = PDFUtils.createSection(getPriorityName(messageResources, priority), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
						pdfTocManager.addSection(), 1);
				for (ObservatorySuitabilityForm level : priority.getSuitabilityGroups()) {
					if (hasProblems(level)) {
						for (ObservatorySubgroupForm verification : level.getSubgroups()) {
							if (verification.getProblems() != null && !verification.getProblems().isEmpty()) {
								for (ProblemForm problem : verification.getProblems()) {
									final PdfPTable tablaVerificacionProblema = createTablaVerificacionProblema(messageResources, prioritySection, verification, problem);
									if (problem.getCheck().equals("232") || EvaluatorUtils.isCssValidationCheck(Integer.parseInt(problem.getCheck()))) {
										addW3CCopyright(prioritySection, problem.getCheck());
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
