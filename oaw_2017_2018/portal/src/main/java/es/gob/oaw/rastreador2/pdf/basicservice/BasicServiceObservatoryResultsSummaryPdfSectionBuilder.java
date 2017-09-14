package es.gob.oaw.rastreador2.pdf.basicservice;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySubgroupForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.rastreador2.pdf.utils.PDFUtils;

import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.util.List;

import static es.inteco.common.Constants.INTAV_PROPERTIES;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;

/**
 * Created by mikunis on 1/16/17.
 */
public class BasicServiceObservatoryResultsSummaryPdfSectionBuilder {

	private static final int LEVEL_I_GROUP_INDEX = 0;
	private static final int LEVEL_II_GROUP_INDEX = 1;

	private final List<ObservatoryEvaluationForm> currentEvaluationPageList;

	public BasicServiceObservatoryResultsSummaryPdfSectionBuilder(final List<ObservatoryEvaluationForm> evaluationPageList) {
		this.currentEvaluationPageList = evaluationPageList;
	}

	public void addObservatoryResultsSummary(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager) throws DocumentException {
		Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("resultados.primarios.res.verificacion").toUpperCase(), pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
		PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.5.p2"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, false);
		addResultsByVerification(messageResources, chapter, currentEvaluationPageList, pdfTocManager);

		document.add(chapter);
		pdfTocManager.addChapterCount();
	}

	private void addResultsByVerification(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final PdfTocManager pdfTocManager) {
		createTablaResumenResultadosPorNivel(messageResources, chapter, evaList, LEVEL_I_GROUP_INDEX, pdfTocManager);

		if (evaList.size() > 2 && evaList.size() < 17) {
			chapter.newPage();
		}

		createTablaResumenResultadosPorNivel(messageResources, chapter, evaList, LEVEL_II_GROUP_INDEX, pdfTocManager);
	}

	private void createTablaResumenResultadosPorNivel(final MessageResources messageResources, final Chapter chapter, final List<ObservatoryEvaluationForm> evaList, final int groupIndex,
			final PdfTocManager pdfTocManager) {
		final ObservatoryLevelForm observatoryLevelForm = evaList.get(0).getGroups().get(groupIndex);
		final Section section = PDFUtils.createSection("Tabla resumen de resultados " + getPriorityName(messageResources, observatoryLevelForm.getName()), pdfTocManager.getIndex(),
				ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
		final PdfPTable table = createTablaResumenResultadosPorNivelHeader(messageResources, observatoryLevelForm.getSuitabilityGroups());

		int contadorPagina = 1;
		for (ObservatoryEvaluationForm evaluationForm : evaList) {
			table.addCell(PDFUtils.createTableCell(
					messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(contadorPagina), 2, ' ')), Color.WHITE,
					ConstantsFont.ANCHOR_FONT, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + contadorPagina));
			for (ObservatorySuitabilityForm suitabilityForm : evaluationForm.getGroups().get(groupIndex).getSuitabilityGroups()) {
				for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
					table.addCell(createTablaResumenResultadosPorNivelCeldaValorModalidad(subgroupForm.getValue()));
				}
			}
			contadorPagina++;
		}
		section.add(table);
		section.add(createTablaResumenResultadosPorNivelLeyenda(messageResources, observatoryLevelForm));
	}

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

	private PdfPTable createTablaResumenResultadosPorNivelHeader(final MessageResources messageResources, final List<ObservatorySuitabilityForm> suitabilityGroups) {
		// final float[] widths = {0.30f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f,
		// 0.07f, 0.07f, 0.07f, 0.07f, 0.07f};
		
		// TODO 2017 -  Dinámicamente para normalizar a la normativa en curso
		
		float[] widths = new float[suitabilityGroups.get(0).getSubgroups().size()+1];
		
		//El primero más grande
		widths[0] =  0.30f;
		
		//El resto pequeños
		for(int i=1;i<widths.length; i++){
			widths[i] =  0.07f;	
		}

		final PdfPTable table = new PdfPTable(widths);
		table.setKeepTogether(true);
		table.setWidthPercentage(95);
		table.setSpacingBefore(ConstantsFont.LINE_SPACE);
		table.setSpacingAfter(ConstantsFont.THIRD_LINE_SPACE);
		table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER,
				DEFAULT_PADDING, -1));

		for (ObservatorySuitabilityForm suitabilityForm : suitabilityGroups) {
			for (ObservatorySubgroupForm subgroupForm : suitabilityForm.getSubgroups()) {
				table.addCell(PDFUtils.createTableCell(messageResources.getMessage(subgroupForm.getDescription()).substring(0, 6), Constants.VERDE_C_MP, ConstantsFont.labelCellFont,
						Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			}
		}
		return table;
	}

	private PdfPCell createTablaResumenResultadosPorNivelCeldaValorModalidad(final int value) {
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
			valor = "0";
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

	private PdfPTable createTablaResumenResultadosPorNivelLeyenda(final MessageResources messageResources, final ObservatoryLevelForm evaList) {
		final PdfPTable table = new PdfPTable(new float[] { 0.40f, 0.60f });
		table.setSpacingBefore(0);
		table.setWidthPercentage(65);
		table.setKeepTogether(true);

		final com.lowagie.text.List leyendaValoresResultados = new com.lowagie.text.List(false, false);
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Valor No Puntua", "-:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Valor 1", "1:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Valor 0", "0:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Modalidad PASA", "P:"));
		leyendaValoresResultados.add(PDFUtils.buildLeyendaListItem("Modalidad FALLA", "F:"));

		final PdfPCell leyendaValoresResultadosTableCell = PDFUtils.createListTableCell(leyendaValoresResultados, Color.WHITE, Element.ALIGN_LEFT, Element.ALIGN_TOP, 0);
		leyendaValoresResultadosTableCell.setBorder(0);
		table.addCell(leyendaValoresResultadosTableCell);
		final com.lowagie.text.List leyenda = new com.lowagie.text.List(false, false);
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
