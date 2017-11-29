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
import static es.inteco.common.Constants.OBS_A;
import static es.inteco.common.Constants.OBS_AA;
import static es.inteco.common.Constants.OBS_NV;
import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;
import static es.inteco.common.ConstantsFont.HALF_LINE_SPACE;
import static es.inteco.common.ConstantsFont.LINE_SPACE;
import static es.inteco.rastreador2.openOffice.export.OpenOfficeUNE2012DocumentBuilder.LEVEL_II_VERIFICATIONS;
import static es.inteco.rastreador2.openOffice.export.OpenOfficeUNE2012DocumentBuilder.LEVEL_I_VERIFICATIONS;
import static es.inteco.rastreador2.utils.GraphicsUtils.parseLevelLabel;
import static es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils.getVerificationResultsByPoint;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts.util.MessageResources;
import org.jfree.data.category.DefaultCategoryDataset;

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Section;
import com.lowagie.text.pdf.PdfPTable;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.openOffice.export.OpenOfficeUNE2012DocumentBuilder;
import es.inteco.rastreador2.openOffice.export.OpenOfficeUNE2017DocumentBuilder;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdfUNE2017;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.utils.ChartForm;
import es.inteco.rastreador2.utils.GraphicsUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioUNE2012Utils;

/**
 * Clase para crear la sección Evolución de Resultados de los informes
 * evolutivos del servicio de diagnóstico
 */
public class BasicServiceObservatoryEvolutionResultsPdfSectionBuilder {

	private final Map<Date, List<ObservatoryEvaluationForm>> evolutionResults;
	private final PropertiesManager pmgr;

	public BasicServiceObservatoryEvolutionResultsPdfSectionBuilder(final Map<Date, List<ObservatoryEvaluationForm>> evolutionResults) {
		this.evolutionResults = evolutionResults;
		pmgr = new PropertiesManager();
	}

	public void addEvolutionResults(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager, final File file)
			throws DocumentException {
		if (evolutionResults.size() > 1) {
			final Chapter chapter = PDFUtils.createChapterWithTitle("Evolución Resultados", pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
			evolutionAverageScore(pdfBuilder, messageResources, pdfTocManager, file, evolutionResults, chapter);
			chapter.newPage();

			evolutionConformanceLevel(messageResources, pdfTocManager, file, evolutionResults, chapter);
			chapter.newPage();

			evolutionAverageScoreByVerification(messageResources, pdfBuilder, pdfTocManager, file, evolutionResults, chapter);

			document.add(chapter);
			pdfTocManager.addChapterCount();
		}
	}

	private void evolutionAverageScore(AnonymousResultExportPdf pdfBuilder, MessageResources messageResources, PdfTocManager pdfTocManager, File file,
			Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Chapter chapter) {
		final Section section = PDFUtils.createSection("Evolución Puntuacion Media del Portal", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.getNumSection(), 1);
		final ArrayList<String> boldWord = new ArrayList<>();
		boldWord.add("Puntuación Media alcanzada por el portal");
		final String evolutionParaIntro = "La siguiente gráfica muestra la {0} en los distintos observatorios "
				+ "realizados hasta la fecha. Éste es un valor que permite observar de forma general si la accesibilidad del sitio " + "web tiende a mejorar o empeorar en el tiempo.";
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(evolutionParaIntro, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

		final Map<String, BigDecimal> resultData = ResultadosAnonimosObservatorioUNE2012Utils.calculateEvolutionPuntuationDataSet(evaluationPageList, df);

		final StringBuilder color = new StringBuilder(50);
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
			final ScoreForm score = pdfBuilder.generateScores(messageResources, observatoryEvaluationForms.getValue());
			if (score.getLevel().equals(messageResources.getMessage("resultados.anonimos.num.portales.aa"))) {
				color.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.green.color"));
			} else if (score.getLevel().equals(messageResources.getMessage("resultados.anonimos.num.portales.a"))) {
				color.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.yellow.color"));
			} else {
				color.append(pmgr.getValue(CRAWLER_PROPERTIES, "chart.graphic.red.color"));
			}
			color.append(',');
		}

		final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + messageResources.getMessage("observatory.graphic.evolution.mid.puntuation.name") + ".jpg";

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, BigDecimal> entry : resultData.entrySet()) {
			dataSet.addValue(entry.getValue(), "", parseLevelLabel(entry.getKey(), messageResources));
		}
		final ChartForm observatoryGraphicsForm = new ChartForm(dataSet, true, true, false, false, true, true, false, 580, 580, color.toString());
		observatoryGraphicsForm.setFixedColorBars(true);
		observatoryGraphicsForm.setFixedLegend(true);
		try {
			GraphicsUtils.createStandardBarChart(observatoryGraphicsForm, filePath, "", messageResources, false);
			final Image image = PDFUtils.createImage(filePath, null);
			if (image != null) {
				image.scalePercent(60);
				image.setAlignment(Element.ALIGN_CENTER);
				image.setSpacingBefore(ConstantsFont.LINE_SPACE);
				image.setSpacingAfter(ConstantsFont.LINE_SPACE);
				section.add(image);
			}
		} catch (IOException e) {
			Logger.putLog("Error al intentar crear la imagen " + filePath, BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
		}

		final float[] columnWidths = new float[] { 0.5f, 0.5f };
		final PdfPTable tablaRankings = new PdfPTable(columnWidths);
		tablaRankings.setSpacingBefore(LINE_SPACE);
		tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
		tablaRankings.setWidthPercentage(80);
		tablaRankings.setHeaderRows(1);

		tablaRankings.addCell(PDFUtils.createTableCell("Fecha", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		tablaRankings.addCell(PDFUtils.createTableCell("Puntuación Media", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		tablaRankings.completeRow();

		for (Map.Entry<String, BigDecimal> dateEvaluationScoreEntry : resultData.entrySet()) {
			tablaRankings.addCell(PDFUtils.createTableCell(dateEvaluationScoreEntry.getKey(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.addCell(PDFUtils.createTableCell(dateEvaluationScoreEntry.getValue().toPlainString(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
			tablaRankings.completeRow();
		}

		section.add(tablaRankings);
	}

	private void evolutionConformanceLevel(MessageResources messageResources, PdfTocManager pdfTocManager, File file, Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Chapter chapter) {
		final Section section = PDFUtils.createSection("Evolución de la distribución del nivel de accesibilidad", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.getNumSection(), 1);

		final ArrayList<String> boldWord = new ArrayList<>();
		boldWord.add("distribución de las páginas del portal en los tres Niveles de Conformidad");
		final String evolutionConformanceParagraph1 = "Mediante las siguientes gráficas se muestra la evolución que ha sufrido la {0} definidos en el observatorio: Prioridad 1 y 2 (nivel óptimo), Prioridad 1 y Parcial.";
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(evolutionConformanceParagraph1, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		boldWord.clear();
		final String evolutionConformanceParagraph2 = "La tendencia ideal a observar en esta evolución sería que el porcentaje de Prioridad 1 y 2 esté siempre en aumento, mientras que el nivel Parcial debería decrecer aproximándose lo máximo posible al valor 0. Si aumenta el porcentaje de Prioridad 1 lo ideal es que sea únicamente a costa de un descenso del nivel Parcial y no de un descenso del nivel de Prioridad 1 y 2.";
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(evolutionConformanceParagraph2, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

		final DateFormat df = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

		final Map<String, Map<String, BigDecimal>> result = new TreeMap<>();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> dateEvaluationPageListEntry : evaluationPageList.entrySet()) {
			final Map<String, List<ObservatoryEvaluationForm>> globalResult = ResultadosAnonimosObservatorioUNE2012Utils.getPagesByType(dateEvaluationPageListEntry.getValue());
			result.put(df.format(dateEvaluationPageListEntry.getKey()), calculatePercentage(globalResult));
		}

		final DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		for (Map.Entry<String, Map<String, BigDecimal>> evolutionSuitabilityEntry : result.entrySet()) {
			dataSet.addValue(evolutionSuitabilityEntry.getValue().get(OBS_NV), parseLevelLabel(OBS_NV, messageResources), evolutionSuitabilityEntry.getKey());
			dataSet.addValue(evolutionSuitabilityEntry.getValue().get(OBS_A), parseLevelLabel(OBS_A, messageResources), evolutionSuitabilityEntry.getKey());
			dataSet.addValue(evolutionSuitabilityEntry.getValue().get(OBS_AA), parseLevelLabel(OBS_AA, messageResources), evolutionSuitabilityEntry.getKey());
		}

		final String noDataMess = "noData";
		int x = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.x"));
		int y = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.y"));
		final ChartForm chartForm = new ChartForm(dataSet, true, false, false, true, true, false, true, x, y, pmgr.getValue(CRAWLER_PROPERTIES, "chart.observatory.graphic.intav.colors"));
		final String filePath = file.getParentFile().getPath() + File.separator + "temp" + File.separator + "EvolucionNivelConformidadCombinada.jpg";
		try {
			GraphicsUtils.createStackedBarChart(chartForm, noDataMess, filePath);
			final Image image = PDFUtils.createImage(filePath, null);
			if (image != null) {
				image.scalePercent(60);
				image.setAlignment(Element.ALIGN_CENTER);
				image.setSpacingBefore(ConstantsFont.LINE_SPACE);
				image.setSpacingAfter(ConstantsFont.LINE_SPACE);
				section.add(image);
			}
		} catch (IOException e) {
			Logger.putLog("Error al intentar crear la imagen " + filePath, BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
		}

		final float[] columnWidths = new float[result.size() + 1];
		Arrays.fill(columnWidths, 1f / (result.size() + 1));
		final PdfPTable tablaRankings = new PdfPTable(columnWidths);
		tablaRankings.setSpacingBefore(LINE_SPACE);
		tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
		tablaRankings.setWidthPercentage(100);
		tablaRankings.setHeaderRows(1);

		tablaRankings.addCell(PDFUtils.createEmptyTableCell());
		for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
			tablaRankings.addCell(PDFUtils.createTableCell(stringMapEntry.getKey(), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		tablaRankings.completeRow();

		tablaRankings.addCell(PDFUtils.createTableCell("Prioridad 1 y 2", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
			tablaRankings.addCell(
					PDFUtils.createTableCell(stringMapEntry.getValue().get(Constants.OBS_AA).toString() + "%", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		tablaRankings.completeRow();

		tablaRankings.addCell(PDFUtils.createTableCell("Prioridad 1", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
			tablaRankings.addCell(
					PDFUtils.createTableCell(stringMapEntry.getValue().get(Constants.OBS_A).toString() + "%", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		tablaRankings.completeRow();

		tablaRankings.addCell(PDFUtils.createTableCell("Parcial", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		for (Map.Entry<String, Map<String, BigDecimal>> stringMapEntry : result.entrySet()) {
			tablaRankings.addCell(
					PDFUtils.createTableCell(stringMapEntry.getValue().get(Constants.OBS_NV).toString() + "%", Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		tablaRankings.completeRow();

		section.add(tablaRankings);
	}

	private void evolutionAverageScoreByVerification(MessageResources messageResources, AnonymousResultExportPdf pdfBuilder, PdfTocManager pdfTocManager, File file,
			Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Chapter chapter) {
		final Section section = PDFUtils.createSection("Evolución de la Puntuación Media por Verificación", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter,
				pdfTocManager.getNumSection(), 1);

		final ArrayList<String> boldWord = new ArrayList<>();
		final String evolutionAverageScoreParagraph1 = "En los apartados siguientes se presenta la variación sufrida por la Puntuación Media de las veinte verificaciones del observatorio.";
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(evolutionAverageScoreParagraph1, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
		final String evolutionAverageScoreParagraph2 = "Mediante estas gráficas se puede obtener de forma rápida una conclusión sobre en qué requisitos de accesibilidad se está mejorando, así como aquellos en los que los problemas detectados han ido en aumento, y de esa forma poder focalizar los esfuerzos más fácilmente.";
		section.add(PDFUtils.createParagraphWithDiferentFormatWord(evolutionAverageScoreParagraph2, boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

		evolutionAverageScoreByVerificationByLevel1(messageResources, pdfBuilder, pdfTocManager, file, evaluationPageList, section);
		// section.newPage();
		evolutionAverageScoreByVerificationByLevel2(messageResources, pdfBuilder, pdfTocManager, file, evaluationPageList, section);
	}

	private void evolutionAverageScoreByVerificationByLevel1(MessageResources messageResources, AnonymousResultExportPdf pdfBuilder, PdfTocManager pdfTocManager, File file,
			Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Section section) {

		final Section subSection = PDFUtils.createSection("Nivel de Accesibilidad I", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, section, pdfTocManager.getNumSection(), 2);

		// TODO 2017 Desdoblamiento para nueva metodologia

		final DefaultCategoryDataset dataSet;

		int numVerficationsLevel;

		if (pdfBuilder instanceof AnonymousResultExportPdfUNE2017) {
			dataSet = createEvolutionAverageScoreByVerificationDataSet(evaluationPageList, OpenOfficeUNE2017DocumentBuilder.LEVEL_I_VERIFICATIONS);
			numVerficationsLevel = OpenOfficeUNE2017DocumentBuilder.LEVEL_I_VERIFICATIONS.size();
		} else {

			dataSet = createEvolutionAverageScoreByVerificationDataSet(evaluationPageList, LEVEL_I_VERIFICATIONS);
			numVerficationsLevel = OpenOfficeUNE2012DocumentBuilder.LEVEL_I_VERIFICATIONS.size();
		}

		addEvolutionAverageScoreByVerificationByLevelChart(messageResources, dataSet, file, "EvolucionPuntuacionMediaVerificacionNAICombinada.jpg", subSection);

		final DateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

		createEvolutionBarDateLegendTable(evaluationPageList, subSection, dateFormat);

		final Map<Date, ScoreForm> scores = new TreeMap<>();
		final PdfPTable tablaRankings = createTablaRankings(messageResources, pdfBuilder, evaluationPageList, scores, dateFormat);

		// TODO 2017 Desdoblamiento para nueva metodologia

		for (int i = 0; i < numVerficationsLevel; i++) {
			// for (int i = 0; i < 10; i++) {
			tablaRankings.addCell(PDFUtils.createTableCell(scores.values().iterator().next().getVerifications1().get(i).getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT,
					DEFAULT_PADDING, -1));
			for (Map.Entry<Date, ScoreForm> dateScoreFormEntry : scores.entrySet()) {
				tablaRankings.addCell(PDFUtils.createTableCell(dateScoreFormEntry.getValue().getVerifications1().get(i).getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER,
						DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
		}
		subSection.add(tablaRankings);
	}

	private void evolutionAverageScoreByVerificationByLevel2(MessageResources messageResources, AnonymousResultExportPdf pdfBuilder, PdfTocManager pdfTocManager, File file,
			Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Section section) {
		final Section subSection = PDFUtils.createSection("Nivel de Accesibilidad II", pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, section, pdfTocManager.getNumSection(), 2);

		// TODO 2017 Desdoblamiento para nueva metodologia

		final DefaultCategoryDataset dataSet;

		int numVerficationsLevel;

		if (pdfBuilder instanceof AnonymousResultExportPdfUNE2017) {
			dataSet = createEvolutionAverageScoreByVerificationDataSet(evaluationPageList, OpenOfficeUNE2017DocumentBuilder.LEVEL_II_VERIFICATIONS);
			numVerficationsLevel = OpenOfficeUNE2017DocumentBuilder.LEVEL_II_VERIFICATIONS.size();
		} else {

			dataSet = createEvolutionAverageScoreByVerificationDataSet(evaluationPageList, LEVEL_II_VERIFICATIONS);
			numVerficationsLevel = OpenOfficeUNE2012DocumentBuilder.LEVEL_II_VERIFICATIONS.size();
		}

		addEvolutionAverageScoreByVerificationByLevelChart(messageResources, dataSet, file, "EvolucionPuntuacionMediaVerificacionNAIICombinada.jpg", subSection);

		final DateFormat dateFormat = new SimpleDateFormat(pmgr.getValue(CRAWLER_PROPERTIES, "date.basicservice.evolutivo.format"));

		createEvolutionBarDateLegendTable(evaluationPageList, subSection, dateFormat);

		final Map<Date, ScoreForm> scores = new TreeMap<>();
		final PdfPTable tablaRankings = createTablaRankings(messageResources, pdfBuilder, evaluationPageList, scores, dateFormat);

		// TODO 2017 Desdoblamiento para nueva metodologia

		for (int i = 0; i < numVerficationsLevel; i++) {
			// for (int i = 0; i < 10; i++) {
			tablaRankings.addCell(PDFUtils.createTableCell(scores.values().iterator().next().getVerifications2().get(i).getLabel(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT,
					DEFAULT_PADDING, -1));
			for (Map.Entry<Date, ScoreForm> dateScoreFormEntry : scores.entrySet()) {
				tablaRankings.addCell(PDFUtils.createTableCell(dateScoreFormEntry.getValue().getVerifications2().get(i).getValue(), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER,
						DEFAULT_PADDING, -1));
			}
			tablaRankings.completeRow();
		}
		subSection.add(tablaRankings);
	}

	private DefaultCategoryDataset createEvolutionAverageScoreByVerificationDataSet(final Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, final List<String> verifications) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> entry : evaluationPageList.entrySet()) {
			final Map<String, BigDecimal> resultsByVerification = getVerificationResultsByPoint(entry.getValue(), Constants.OBS_PRIORITY_NONE);
			for (String verification : verifications) {
				// Para un observatorio en concreto recuperamos la puntuación de
				// una verificación
				final BigDecimal value = resultsByVerification.get(verification);
				dataset.addValue(value, entry.getKey().getTime(), verification);
			}
		}
		return dataset;
	}

	private void addEvolutionAverageScoreByVerificationByLevelChart(final MessageResources messageResources, DefaultCategoryDataset dataSet, final File path, final String filename,
			Section subSection) {
		final ChartForm chartForm = new ChartForm(dataSet, true, true, false, false, false, false, true, 800, 400, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"));
		chartForm.setFixedColorBars(true);
		chartForm.setShowColumsLabels(false);

		final String filePath = path.getParentFile().getPath() + File.separator + "temp" + File.separator + filename;
		try {
			GraphicsUtils.createStandardBarChart(chartForm, filePath, "", messageResources, true);
			final Image image = PDFUtils.createImage(filePath, null);
			if (image != null) {
				image.scalePercent(60);
				image.setAlignment(Element.ALIGN_CENTER);
				image.setSpacingBefore(ConstantsFont.LINE_SPACE);
				image.setSpacingAfter(ConstantsFont.LINE_SPACE);
				subSection.add(image);
			}
		} catch (IOException e) {
			Logger.putLog("Error al intentar crear la imagen " + filePath, BasicServicePdfReport.class, Logger.LOG_LEVEL_ERROR);
		}
	}

	private void createEvolutionBarDateLegendTable(Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList, Section subSection, DateFormat dateFormat) {
		final float[] tablaFechasWidths = new float[evaluationPageList.size() + 1];
		tablaFechasWidths[0] = 0.16f;
		Arrays.fill(tablaFechasWidths, 1, tablaFechasWidths.length, 0.28f);

		final PdfPTable tablaFechas = new PdfPTable(tablaFechasWidths);
		tablaFechas.setSpacingBefore(HALF_LINE_SPACE);
		tablaFechas.setSpacingAfter(HALF_LINE_SPACE);
		tablaFechas.setWidthPercentage(85);
		tablaFechas.setHeaderRows(1);
		tablaFechas.addCell(PDFUtils.createEmptyTableCell());
		for (int i = 1; i < tablaFechasWidths.length; i++) {
			tablaFechas.addCell(PDFUtils.createTableCell(i + "ª Barra", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}

		tablaFechas.completeRow();
		tablaFechas.addCell(PDFUtils.createTableCell("Fecha", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
			tablaFechas.addCell(PDFUtils.createTableCell(dateFormat.format(observatoryEvaluationForms.getKey()), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		tablaFechas.completeRow();
		subSection.add(tablaFechas);
	}

	private PdfPTable createTablaRankings(final MessageResources messageResources, AnonymousResultExportPdf pdfBuilder, Map<Date, List<ObservatoryEvaluationForm>> evaluationPageList,
			final Map<Date, ScoreForm> scores, final DateFormat dateFormat) {
		final float[] columnWidths = new float[evaluationPageList.size() + 1];
		columnWidths[0] = 0.55f;
		Arrays.fill(columnWidths, 1, columnWidths.length, 0.45f / evaluationPageList.size());
		final PdfPTable tablaRankings = new PdfPTable(columnWidths);
		tablaRankings.setSpacingBefore(HALF_LINE_SPACE);
		tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
		tablaRankings.setWidthPercentage(85);
		tablaRankings.setHeaderRows(1);

		tablaRankings.addCell(PDFUtils.createTableCell("Verificación", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));

		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
			tablaRankings.addCell(
					PDFUtils.createTableCell(dateFormat.format(observatoryEvaluationForms.getKey()), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
		}
		tablaRankings.completeRow();

		for (Map.Entry<Date, List<ObservatoryEvaluationForm>> observatoryEvaluationForms : evaluationPageList.entrySet()) {
			scores.put(observatoryEvaluationForms.getKey(), pdfBuilder.generateScores(messageResources, observatoryEvaluationForms.getValue()));
		}
		return tablaRankings;
	}

	private Map<String, BigDecimal> calculatePercentage(final Map<String, List<ObservatoryEvaluationForm>> values) {
		final Map<String, Integer> count = new LinkedHashMap<>();

		count.put(OBS_NV, values.get(OBS_NV).size());
		count.put(OBS_A, values.get(OBS_A).size());
		count.put(OBS_AA, values.get(OBS_AA).size());

		return ResultadosAnonimosObservatorioUNE2012Utils.calculatePercentage(count);
	}
}
