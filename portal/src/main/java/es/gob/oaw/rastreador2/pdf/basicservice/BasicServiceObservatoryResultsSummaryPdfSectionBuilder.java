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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNEEN2019Utils;

/**
 * Created by mikunis on 1/16/17.
 */
public class BasicServiceObservatoryResultsSummaryPdfSectionBuilder {
	/** The Constant LEVEL_I_GROUP_INDEX. */
	private static final int LEVEL_I_GROUP_INDEX = 0;
	/** The Constant LEVEL_II_GROUP_INDEX. */
	private static final int LEVEL_II_GROUP_INDEX = 1;
	/** The current evaluation page list. */
	private final List<ObservatoryEvaluationForm> currentEvaluationPageList;

	/**
	 * Instantiates a new basic service observatory results summary pdf section builder.
	 *
	 * @param evaluationPageList the evaluation page list
	 */
	public BasicServiceObservatoryResultsSummaryPdfSectionBuilder(final List<ObservatoryEvaluationForm> evaluationPageList) {
		this.currentEvaluationPageList = evaluationPageList;
	}

	/**
	 * Adds the observatory results summary.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @throws DocumentException the document exception
	 */
	public void addObservatoryResultsSummary(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager) throws DocumentException {
		Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("resultados.primarios.res.verificacion").toUpperCase(), pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p2"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
		addResultsByVerification(messageResources, chapter, currentEvaluationPageList, pdfTocManager);
		document.add(chapter);
		pdfTocManager.addChapterCount();
	}

	/**
	 * Adds the observatory results summary accesibility.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @throws DocumentException the document exception
	 */
	public void addObservatoryResultsSummaryAccesibility(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager) throws DocumentException {
		final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("pdf.accessibility.global.summary.title").toUpperCase(), pdfTocManager.getIndex(),
				pdfTocManager.addSection(), pdfTocManager.getNumChapter(), ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resultados_verificacion");
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p2"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
		addResultsByVerificationAccesibility(messageResources, chapter, currentEvaluationPageList, pdfTocManager);
		document.add(chapter);
		pdfTocManager.addChapterCount();
	}

	/**
	 * Adds the observatory results summary with compliance.
	 *
	 * @param messageResources the message resources
	 * @param document         the document
	 * @param pdfTocManager    the pdf toc manager
	 * @param currentScore     the current score
	 * @throws DocumentException the document exception
	 */
	public void addObservatoryResultsSummaryWithCompliance(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, ScoreForm currentScore)
			throws DocumentException {
//		Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("resultados.primarios.res.verificacion").toUpperCase(), pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
		final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("resultados.primarios.res.verificacion").toUpperCase(), pdfTocManager.getIndex(),
				pdfTocManager.addSection(), pdfTocManager.getNumChapter(), ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resultados_verificacion");
//		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
//		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p2"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
		final ArrayList<String> boldWords = new ArrayList<>();
		boldWords.add(messageResources.getMessage("resultados.primarios.res.verificacion.p1.bold1"));
		boldWords.add(messageResources.getMessage("resultados.primarios.res.verificacion.p1.bold2"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.res.verificacion.p1"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(messageResources.getMessage("resultados.primarios.res.verificacion.p2.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.res.verificacion.p2"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(messageResources.getMessage("resultados.primarios.res.verificacion.p3.bold1"));
		boldWords.add(messageResources.getMessage("resultados.primarios.res.verificacion.p3.bold2"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.res.verificacion.p3"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		boldWords.add(messageResources.getMessage("resultados.primarios.res.verificacion.p4.bold"));
		chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.res.verificacion.p4"), boldWords, ConstantsFont.paragraphBoldFont,
				ConstantsFont.PARAGRAPH, true));
		boldWords.clear();
		addResultsByVerificationAndCompliance(messageResources, chapter, currentEvaluationPageList, pdfTocManager, currentScore);
		document.add(chapter);
		pdfTocManager.addChapterCount();
	}

	/**
	 * Adds the results by verification.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param evaList          the eva list
	 * @param pdfTocManager    the pdf toc manager
	 */
	private void addResultsByVerification(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final PdfTocManager pdfTocManager) {
		chapter.newPage();
		createTablaResumenResultadosPorNivel(messageResources, chapter, evaList, LEVEL_I_GROUP_INDEX, pdfTocManager);
		/*
		 * if (evaList.size() > 2 && evaList.size() < 17) { chapter.newPage(); }
		 */
		chapter.newPage();
		createTablaResumenResultadosPorNivel(messageResources, chapter, evaList, LEVEL_II_GROUP_INDEX, pdfTocManager);
	}

	/**
	 * Adds the results by verification accesibility.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param evaList          the eva list
	 * @param pdfTocManager    the pdf toc manager
	 */
	private void addResultsByVerificationAccesibility(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList,
			final PdfTocManager pdfTocManager) {
		// chapter.newPage();
		createTablaResumenResultadosPorNivelAccesibilidad(messageResources, chapter, evaList, LEVEL_I_GROUP_INDEX, pdfTocManager);
	}

	/**
	 * Adds the results by verification and compliance.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param evaList          the eva list
	 * @param pdfTocManager    the pdf toc manager
	 * @param currentScore     the current score
	 */
	private void addResultsByVerificationAndCompliance(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final PdfTocManager pdfTocManager,
			ScoreForm currentScore) {
		chapter.newPage();
		if (evaList != null & evaList.size() > 0) {
			createTablaResumenResultadosPorNivelAndCompliance(messageResources, chapter, evaList, LEVEL_I_GROUP_INDEX, pdfTocManager, currentScore);
			chapter.newPage();
			createTablaResumenResultadosPorNivelAndCompliance(messageResources, chapter, evaList, LEVEL_II_GROUP_INDEX, pdfTocManager, currentScore);
		}
	}

	/**
	 * Creates the tabla resumen resultados por nivel.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param evaList          the eva list
	 * @param groupIndex       the group index
	 * @param pdfTocManager    the pdf toc manager
	 */
	private void createTablaResumenResultadosPorNivel(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final int groupIndex,
			final PdfTocManager pdfTocManager) {
		ObservatoryEvaluationForm observatoryEvaluationForm = evaList.get(0);
		List<ObservatoryLevelForm> groups = observatoryEvaluationForm.getGroups();
		if (groups != null && !groups.isEmpty() && groups.size() > groupIndex) {
			final ObservatoryLevelForm observatoryLevelForm = groups.get(groupIndex);
			final Section section = PDFUtils.createSection(
					messageResources.getMessage("resultados.primarios.res.verificacion.tabla.title") + getPriorityName(messageResources, observatoryLevelForm.getName()), pdfTocManager.getIndex(),
					ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
			final PdfPTable table = createTablaResumenResultadosPorNivelHeader(messageResources, observatoryLevelForm.getSuitabilityGroups());
			int contadorPagina = 1;
			for (ObservatoryEvaluationForm evaluationForm : evaList) {
				table.addCell(PDFUtils.createTableCell(
						messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(contadorPagina), 2, ' ')), Color.WHITE,
						ConstantsFont.ANCHOR_FONT, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + contadorPagina));
				for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(groupIndex).getSuitabilityGroups()) {
					for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
						table.addCell(createTablaResumenResultadosPorNivelCeldaValorModalidad(subgroupForm.getValue(), messageResources));
					}
				}
				contadorPagina++;
			}
			section.add(table);
			section.add(createTablaResumenResultadosPorNivelLeyenda(messageResources, observatoryLevelForm));
		}
	}

	/**
	 * Creates the tabla resumen resultados por nivel accesibilidad.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param evaList          the eva list
	 * @param groupIndex       the group index
	 * @param pdfTocManager    the pdf toc manager
	 */
	private void createTablaResumenResultadosPorNivelAccesibilidad(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final int groupIndex,
			final PdfTocManager pdfTocManager) {
		ObservatoryEvaluationForm observatoryEvaluationForm = evaList.get(0);
		List<ObservatoryLevelForm> groups = observatoryEvaluationForm.getGroups();
		if (groups != null && !groups.isEmpty() && groups.size() > groupIndex) {
			final ObservatoryLevelForm observatoryLevelForm = groups.get(groupIndex);
			final Section section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.res.verificacion.tabla.title"), pdfTocManager.getIndex(),
					ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
			final PdfPTable table = createTablaResumenResultadosPorNivelHeader(messageResources, observatoryLevelForm.getSuitabilityGroups());
			int contadorPagina = 1;
			for (ObservatoryEvaluationForm evaluationForm : evaList) {
				table.addCell(PDFUtils.createTableCell(
						messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(contadorPagina), 2, ' ')), Color.WHITE,
						ConstantsFont.ANCHOR_FONT, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + contadorPagina));
				for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(groupIndex).getSuitabilityGroups()) {
					for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
						table.addCell(createTablaResumenResultadosPorNivelCeldaValorModalidad(subgroupForm.getValue(), messageResources));
					}
				}
				contadorPagina++;
			}
			section.add(table);
			section.add(createTablaResumenResultadosPorNivelLeyenda(messageResources, observatoryLevelForm));
		}
	}

	/**
	 * Creates the tabla resumen resultados por nivel and compliance.
	 *
	 * @param messageResources the message resources
	 * @param chapter          the chapter
	 * @param evaList          the eva list
	 * @param groupIndex       the group index
	 * @param pdfTocManager    the pdf toc manager
	 * @param currentScore     the current score
	 */
	private void createTablaResumenResultadosPorNivelAndCompliance(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final int groupIndex,
			final PdfTocManager pdfTocManager, ScoreForm currentScore) {
		ObservatoryEvaluationForm observatoryEvaluationForm = evaList.get(0);
		List<ObservatoryLevelForm> groups = observatoryEvaluationForm.getGroups();
		if (groups != null && !groups.isEmpty() && groups.size() > groupIndex) {
			final ObservatoryLevelForm observatoryLevelForm = groups.get(groupIndex);
			final Section section = PDFUtils.createSection(
					messageResources.getMessage("resultados.primarios.res.verificacion.tabla.title") + getPriorityName(messageResources, observatoryLevelForm.getName()), pdfTocManager.getIndex(),
					ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
			final PdfPTable table = createTablaResumenResultadosPorNivelHeader(messageResources, observatoryLevelForm.getSuitabilityGroups());
			int contadorPagina = 1;
			for (ObservatoryEvaluationForm evaluationForm : evaList) {
				table.addCell(PDFUtils.createTableCell(
						messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(contadorPagina), 2, ' ')), Color.WHITE,
						ConstantsFont.ANCHOR_FONT, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + contadorPagina));
				for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(groupIndex).getSuitabilityGroups()) {
					for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
						table.addCell(createTablaResumenResultadosPorNivelCeldaValorModalidad(subgroupForm.getValue(), messageResources));
					}
				}
				contadorPagina++;
			}
			// Add final row with compliance
			Map<Long, Map<String, BigDecimal>> results = ResultadosAnonimosObservatorioUNEEN2019Utils.getVerificationResultsByPointAndCrawl(evaList, Constants.OBS_PRIORITY_NONE);
			table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.primarios.res.verificacion.tabla.sitioweb"), Color.WHITE, ConstantsFont.PARAGRAPH, Element.ALIGN_CENTER, 0));
			if (LEVEL_I_GROUP_INDEX == groupIndex) {
				for (LabelValueBean value : currentScore.getVerifications1()) {
					evaluateCompliance(table, value);
				}
			}
			if (LEVEL_II_GROUP_INDEX == groupIndex) {
				for (LabelValueBean value : currentScore.getVerifications2()) {
					evaluateCompliance(table, value);
				}
			}
			section.add(table);
			section.add(createTablaResumenResultadosPorNivelLeyendaWithCompliance(messageResources, observatoryLevelForm));
		}
	}

	/**
	 * Evaluate compliance.
	 *
	 * @param table the table
	 * @param value the value
	 */
	private void evaluateCompliance(final PdfPTable table, LabelValueBean value) {
		try {
			BigDecimal bigDecimal = new BigDecimal(value.getValue());
			if (bigDecimal.compareTo(new BigDecimal(9)) >= 0) {
				table.addCell(PDFUtils.createTableCell("C", Constants.MARRRON_C_NC, ConstantsFont.labelHeaderCellFont, Element.ALIGN_CENTER, 0));
			} else if (bigDecimal.compareTo(new BigDecimal(0)) >= 0) {
				table.addCell(PDFUtils.createTableCell("NC", Constants.MARRRON_C_NC, ConstantsFont.labelHeaderCellFont, Element.ALIGN_CENTER, 0));
			} else {
				table.addCell(PDFUtils.createTableCell("NA", Constants.MARRRON_C_NC, ConstantsFont.labelHeaderCellFont, Element.ALIGN_CENTER, 0));
			}
		} catch (NumberFormatException e) {
			table.addCell(PDFUtils.createTableCell("NA", Constants.MARRRON_C_NC, ConstantsFont.labelHeaderCellFont, Element.ALIGN_CENTER, 0));
		}
	}

	/**
	 * Gets the priority name.
	 *
	 * @param messageResources the message resources
	 * @param name             the name
	 * @return the priority name
	 */
	private String getPriorityName(final MessageResources messageResources, final String name) {
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
	 * Creates the tabla resumen resultados por nivel header.
	 *
	 * @param messageResources  the message resources
	 * @param suitabilityGroups the suitability groups
	 * @return the pdf P table
	 */
	private PdfPTable createTablaResumenResultadosPorNivelHeader(final MessageResources messageResources, final List<ObservatorySuitabilityForm> suitabilityGroups) {
		int subgroupsSize = 0;
		// Calculamos las columnas en función de la normativa en curso
		for (ObservatorySuitabilityForm suitabilityForm : suitabilityGroups) {
			for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
				subgroupsSize++;
			}
		}
		float[] widths = new float[subgroupsSize + 1];
		// El primero más grande
		widths[0] = 0.30f;
		// El resto pequeños
		for (int i = 1; i < widths.length; i++) {
			widths[i] = 0.10f;
		}
		final PdfPTable table = new PdfPTable(widths);
		table.setHeaderRows(1);
		table.setKeepTogether(false);
		table.setWidthPercentage(95);
		table.setSpacingBefore(ConstantsFont.LINE_SPACE);
		table.setSpacingAfter(ConstantsFont.THIRD_LINE_SPACE);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER,
				DEFAULT_PADDING, -1));
		for (ObservatorySuitabilityForm suitabilityForm : suitabilityGroups) {
			for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
				// Hacer el substring para sacar el "numero" que sirva para
				// todas las metodologías...
				String subgroupDescription = messageResources.getMessage(subgroupForm.getDescription());
				table.addCell(PDFUtils.createTableCell(subgroupDescription.substring(0, subgroupDescription.indexOf(" ")), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER,
						DEFAULT_PADDING, -1));
			}
		}
		return table;
	}

	/**
	 * Creates the tabla resumen resultados por nivel celda valor modalidad.
	 *
	 * @param value            the value
	 * @param messageResources the message resources
	 * @return the pdf P cell
	 */
	private PdfPCell createTablaResumenResultadosPorNivelCeldaValorModalidad(final int value, MessageResources messageResources) {
		final String valor;
		final String modalidad;
		final Font fuente;
		final Color backgroundColor;
		switch (value) {
		case Constants.OBS_VALUE_NOT_SCORE:
			valor = "-";
			modalidad = "P";
			fuente = ConstantsFont.descriptionFont;
			backgroundColor = Constants.COLOR_RESULTADO_1_PASA;
			break;
		case Constants.OBS_VALUE_RED_ZERO:
			valor = "0";
			modalidad = "F";
			fuente = ConstantsFont.labelHeaderCellFont;
			backgroundColor = Constants.COLOR_RESULTADO_0_FALLA;
			break;
		case Constants.OBS_VALUE_GREEN_ZERO:
			valor = messageResources.getMessage("resultados.observatorio.vista.primaria.valor.cero.pasa");
			modalidad = "P";
			fuente = ConstantsFont.labelHeaderCellFont;
			backgroundColor = Constants.COLOR_RESULTADO_0_PASA;
			break;
		case Constants.OBS_VALUE_GREEN_ONE:
			valor = "1";
			modalidad = "P";
			fuente = ConstantsFont.descriptionFont;
			backgroundColor = Constants.COLOR_RESULTADO_1_PASA;
			break;
		default:
			valor = "-";
			modalidad = "-";
			fuente = ConstantsFont.descriptionFont;
			backgroundColor = Constants.COLOR_RESULTADO_1_PASA;
			break;
		}
		final PdfPCell labelCell = PDFUtils.createTableCell(valor + " " + modalidad, backgroundColor, fuente, Element.ALIGN_CENTER, 0, -1);
		labelCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return labelCell;
	}

	/**
	 * Creates the tabla resumen resultados por nivel leyenda.
	 *
	 * @param messageResources the message resources
	 * @param evaList          the eva list
	 * @return the pdf P table
	 */
	private PdfPTable createTablaResumenResultadosPorNivelLeyendaWithCompliance(final MessageResources messageResources, final ObservatoryLevelForm evaList) {
		final PdfPTable table = new PdfPTable(new float[] { 0.33f, 0.33f, 0.33f });
		table.setSpacingBefore(1);
		table.setWidthPercentage(90);
		table.setKeepTogether(true);
		final com.itextpdf.text.List leyendaValoresResultados = new com.itextpdf.text.List(false, false);
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItemBold(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.title"), ""));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.np"), "-:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.1"), "1:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.0"), "0:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.pass"), "P:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.nopass"), "F:"));
		final PdfPCell leyendaValoresResultadosTableCell = PDFUtils.createListTableCell(leyendaValoresResultados, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
		leyendaValoresResultadosTableCell.setBorder(0);
		table.addCell(leyendaValoresResultadosTableCell);
		final com.itextpdf.text.List leyendaValoresConformidad = new com.itextpdf.text.List(false, false);
		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItemBold(messageResources.getMessage("pdf.accessibility.page.result.legend.conformance.title"), ""));
		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.conformance.conforming"), "C:"));
		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.conformance.not.conforming"), "NC:"));
		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.conformance.not.apply"), "NA:"));
		final PdfPCell leyendaValoresConformidadTableCell = PDFUtils.createListTableCell(leyendaValoresConformidad, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
		leyendaValoresConformidadTableCell.setBorder(0);
		table.addCell(leyendaValoresConformidadTableCell);
		final com.itextpdf.text.List leyenda = new com.itextpdf.text.List(false, false);
		leyenda.add(PDFUtils.buildLeyendaListItemBold(messageResources.getMessage("pdf.accessibility.page.result.legend.verifications.title"), ""));
		for (ObservatorySuitabilityForm suitabilityForm : evaList.getSuitabilityGroups()) {
			for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
				final String checkId = messageResources.getMessage(subgroupForm.getDescription()).substring(0, 6);
				final String checkDescription = messageResources.getMessage(subgroupForm.getDescription()).substring(6);
				final ListItem listItem = new ListItem(checkDescription, ConstantsFont.MORE_INFO_FONT);
				listItem.setListSymbol(new Chunk(checkId));
				leyenda.add(listItem);
			}
			final PdfPCell listTableCell = PDFUtils.createListTableCell(leyenda, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
			listTableCell.setBorder(0);
			table.addCell(listTableCell);
		}
		return table;
	}

	/**
	 * Creates the tabla resumen resultados por nivel leyenda.
	 *
	 * @param messageResources the message resources
	 * @param evaList          the eva list
	 * @return the pdf P table
	 */
	private PdfPTable createTablaResumenResultadosPorNivelLeyenda(final MessageResources messageResources, final ObservatoryLevelForm evaList) {
		final PdfPTable table = new PdfPTable(new float[] { 0.33f, 0.33f });
		table.setSpacingBefore(1);
		table.setWidthPercentage(90);
		table.setKeepTogether(true);
		final com.itextpdf.text.List leyendaValoresResultados = new com.itextpdf.text.List(false, false);
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItemBold(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.title"), ""));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.np"), "-:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.1"), "1:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.0"), "0:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.pass"), "P:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem(messageResources.getMessage("pdf.accessibility.page.result.legend.modality.nopass"), "F:"));
		final PdfPCell leyendaValoresResultadosTableCell = PDFUtils.createListTableCell(leyendaValoresResultados, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
		leyendaValoresResultadosTableCell.setBorder(0);
		table.addCell(leyendaValoresResultadosTableCell);
//		final com.itextpdf.text.List leyendaValoresConformidad = new com.itextpdf.text.List(false, false);
//		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItemBold("Conformidad de verificación en el sitio web", ""));
//		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItem("Conforme", "C"));
//		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItem("No conforme", "NC:"));
//		leyendaValoresConformidad.add(PDFUtils.buildLeyendaListItem("No puntúa", "N:"));
//		final PdfPCell leyendaValoresConformidadTableCell = PDFUtils.createListTableCell(leyendaValoresConformidad, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
//		leyendaValoresConformidadTableCell.setBorder(0);
//		table.addCell(leyendaValoresConformidadTableCell);
		final com.itextpdf.text.List leyenda = new com.itextpdf.text.List(false, false);
		leyenda.add(PDFUtils.buildLeyendaListItemBold(messageResources.getMessage("pdf.accessibility.page.result.legend.verifications.title"), ""));
		for (ObservatorySuitabilityForm suitabilityForm : evaList.getSuitabilityGroups()) {
			for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
				final String checkId = messageResources.getMessage(subgroupForm.getDescription()).substring(0, 6);
				final String checkDescription = messageResources.getMessage(subgroupForm.getDescription()).substring(6);
				final ListItem listItem = new ListItem(checkDescription, ConstantsFont.MORE_INFO_FONT);
				listItem.setListSymbol(new Chunk(checkId));
				leyenda.add(listItem);
			}
			final PdfPCell listTableCell = PDFUtils.createListTableCell(leyenda, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
			listTableCell.setBorder(0);
			table.addCell(listTableCell);
		}
		return table;
	}
}
