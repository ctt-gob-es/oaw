package es.inteco.rastreador2.pdf.basicservice;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.builder.AnonymousResultExportPdf;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.GraphicData;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import es.inteco.rastreador2.utils.ResultadosAnonimosObservatorioIntavUtils;
import es.inteco.rastreador2.utils.ResultadosPrimariosObservatorioIntavUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;
import static es.inteco.common.ConstantsFont.*;

/**
 * Clase encargada de construir la sección 'Resumen de Resultados' del informe PDF del Servicio de Diagnóstico.
 */
public class BasicServiceObservatoryScorePdfSectionBuilder {

    private final List<ObservatoryEvaluationForm> currentEvaluationPageList;
    private final List<ObservatoryEvaluationForm> previousEvaluationPageList;
    private final boolean evolution;
    private static final String TEMP_SUBDIR = "temp";

    public BasicServiceObservatoryScorePdfSectionBuilder(final List<ObservatoryEvaluationForm> currentEvaluationPageList) {
        this(currentEvaluationPageList, null);
    }

    public BasicServiceObservatoryScorePdfSectionBuilder(final List<ObservatoryEvaluationForm> currentEvaluationPageList, final List<ObservatoryEvaluationForm> previousEvaluationPageList) {
        this.currentEvaluationPageList = currentEvaluationPageList;
        this.previousEvaluationPageList = previousEvaluationPageList;
        evolution = previousEvaluationPageList != null && !previousEvaluationPageList.isEmpty();
    }

    public void addObservatoryScoreSummary(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, Document document, final PdfTocManager pdfTocManager, final File file) throws Exception {
        final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("observatorio.puntuacion.resultados.resumen").toUpperCase(), pdfTocManager, ConstantsFont.CHAPTER_TITLE_MP_FONT);
        final ArrayList<String> boldWord = new ArrayList<>();
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p1"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWord.clear();
        boldWord.add(messageResources.getMessage("resultados.primarios.4.p2.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p2"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        boldWord.clear();
        boldWord.add(messageResources.getMessage("resultados.primarios.4.p3.bold1"));
        chapter.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("resultados.primarios.4.p3"), boldWord, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

        final ScoreForm currentScore = pdfBuilder.generateScores(messageResources, currentEvaluationPageList);
        final ScoreForm previousScore;
        if (evolution) {
            previousScore = pdfBuilder.generateScores(messageResources, previousEvaluationPageList);
        } else {
            previousScore = null;
        }

        //// TABLA PUNTUACION OBSERVATORIO
        final float[] columnWidths = new float[]{0.6f, 0.4f};
        final PdfPTable tablaRankings = new PdfPTable(columnWidths);
        tablaRankings.setSpacingBefore(LINE_SPACE);
        tablaRankings.setSpacingAfter(HALF_LINE_SPACE);
        tablaRankings.setWidthPercentage(90);
        tablaRankings.setHeaderRows(1);

        tablaRankings.addCell(PDFUtils.createEmptyTableCell());
        tablaRankings.addCell(PDFUtils.createTableCell("Resultado", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        tablaRankings.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatorio.nivel.adecuacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
        tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getLevel(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        tablaRankings.completeRow();

        tablaRankings.addCell(PDFUtils.createTableCell("Puntuación Media del Portal", Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_LEFT, DEFAULT_PADDING, -1));
        tablaRankings.addCell(PDFUtils.createTableCell(currentScore.getTotalScore().toPlainString(), Color.WHITE, ConstantsFont.strongNoteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        tablaRankings.completeRow();

        chapter.add(tablaRankings);

        final String noDataMess = messageResources.getMessage("grafica.sin.datos");
        // Obtener lista resultados iteración anterior

        addLevelAllocationResultsSummary(messageResources, chapter, file, noDataMess);

        Section section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion1"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.41.p1"), ConstantsFont.PARAGRAPH, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_1);
        section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, previousScore, Constants.OBS_PRIORITY_1));

        section = PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuaciones.verificacion2"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.42.p1"), ConstantsFont.PARAGRAPH, section);
        addMidsComparationByVerificationLevelGraphic(pdfBuilder, messageResources, section, file, currentEvaluationPageList, noDataMess, Constants.OBS_PRIORITY_2);
        section.add(createObservatoryVerificationScoreTable(messageResources, currentScore, previousScore, Constants.OBS_PRIORITY_2));

        PDFUtils.createSection(messageResources.getMessage("resultados.primarios.puntuacion.pagina"), pdfTocManager.getIndex(), ConstantsFont.CHAPTER_TITLE_MP_FONT_2_L, chapter, pdfTocManager.addSection(), 1);
        addResultsByPage(messageResources, chapter, file, currentEvaluationPageList, noDataMess);

        document.add(chapter);
        pdfTocManager.addChapterCount();
    }

    private void addLevelAllocationResultsSummary(final MessageResources messageResources, final Section section, final File file,
                                                  final String noDataMess) throws Exception {
        if (previousEvaluationPageList != null && evolution) {
            addLevelAllocationResultsSummaryWithEvolution(messageResources, section, file, noDataMess);
        } else {
            addLevelAllocationResultsSummaryNoEvolution(messageResources, section, file, noDataMess);
        }
    }

    private void addLevelAllocationResultsSummaryNoEvolution(final MessageResources messageResources, final Section section, final File file,
                                                             final String noDataMess) throws Exception {
        final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);

        final List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);

        final String filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test.jpg";
        final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
        ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, currentEvaluationPageList, title, filePath, noDataMess);
        createImage(section, filePath);

        final float[] columnsWidth = new float[]{35f, 30f, 35f};
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

        section.add(table);
    }

    private void createImage(final Section section, final String filePath) {
        final com.lowagie.text.Image image = PDFUtils.createImage(filePath, null);
        if (image != null) {
            image.scalePercent(60);
            image.setAlignment(Element.ALIGN_CENTER);
            image.setSpacingAfter(ConstantsFont.LINE_SPACE);
            section.add(image);
        }
    }

    private void addLevelAllocationResultsSummaryWithEvolution(final MessageResources messageResources, final Section section, final File file,
                                                               final String noDataMess) throws Exception {
        final Map<String, Integer> currentResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(currentEvaluationPageList);
        final Map<String, Integer> previousResultsByLevel = ResultadosPrimariosObservatorioIntavUtils.getResultsByLevel(previousEvaluationPageList);

        final List<GraphicData> currentGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, currentResultsByLevel);
        final List<GraphicData> previousGlobalAccessibilityLevel = ResultadosAnonimosObservatorioIntavUtils.infoGlobalAccessibilityLevel(messageResources, previousResultsByLevel);

        final String filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test.jpg";
        final String title = messageResources.getMessage("observatory.graphic.accessibility.level.allocation.by.page.title");
        ResultadosPrimariosObservatorioIntavUtils.getGlobalAccessibilityLevelAllocationSegmentGraphic(messageResources, currentEvaluationPageList, title, filePath, noDataMess);
        createImage(section, filePath);

        final float[] columnsWidth;
        if (!previousEvaluationPageList.isEmpty()) {
            columnsWidth = new float[]{30f, 25f, 30f, 15f};
        } else {
            columnsWidth = new float[]{35f, 30f, 35f};
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
                    table.addCell(PDFUtils.createTableCell(new DecimalFormat("+0.00;-0.00").format(actualData.getRawPercentage().subtract(previousData.getRawPercentage())) + "%", Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
                } catch (NumberFormatException nfe) {
                    table.addCell(PDFUtils.createTableCell("Errror", Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
                }
            }
            table.completeRow();
        }
        table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);

        section.add(table);
    }

    private void addMidsComparationByVerificationLevelGraphic(final AnonymousResultExportPdf pdfBuilder, final MessageResources messageResources, final Section section, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess, final String level) throws Exception {
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
        pdfBuilder.getMidsComparationByVerificationLevelGraphic(messageResources, level, title, filePath, noDataMess, evaList, pmgr.getValue(CRAWLER_PROPERTIES, "chart.evolution.mp.green.color"), true);
        createImage(section, filePath);
    }

    private PdfPTable createObservatoryVerificationScoreTable(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level) {
        if (previousScore != null && evolution) {
            return createObservatoryVerificationScoreTableWithEvolution(messageResources, actualScore, previousScore, level);
        } else {
            return createObservatoryVerificationScoreTableNoEvolution(messageResources, actualScore, level);
        }
    }

    private PdfPTable createObservatoryVerificationScoreTableNoEvolution(final MessageResources messageResources, final ScoreForm actualScore, final String level) {
        if (level.equals(Constants.OBS_PRIORITY_1)) {
            return createObservatoryVerificationScoreTableNoEvolutionLevel(
                    messageResources,
                    actualScore.getVerifications1(),
                    messageResources.getMessage("observatorio.puntuacion.nivel.1"),
                    actualScore.getScoreLevel1());
        } else {
            return createObservatoryVerificationScoreTableNoEvolutionLevel(
                    messageResources,
                    actualScore.getVerifications2(),
                    messageResources.getMessage("observatorio.puntuacion.nivel.2"),
                    actualScore.getScoreLevel2());
        }
    }

    private PdfPTable createObservatoryVerificationScoreTableNoEvolutionLevel(final MessageResources messageResources, final List<LabelValueBean> verifications, final String scoreLabel, final BigDecimal scoreValue) {
        final float[] columnsWidths = new float[]{0.65f, 0.35f};
        final PdfPTable table = new PdfPTable(columnsWidths);

        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
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

    private PdfPTable createObservatoryVerificationScoreTableWithEvolution(final MessageResources messageResources, final ScoreForm actualScore, final ScoreForm previousScore, final String level) {
        if (level.equals(Constants.OBS_PRIORITY_1)) {
            return createObservatoryVerificationScoreTableWithEvolutionLevel(
                    messageResources,
                    messageResources.getMessage("observatorio.puntuacion.nivel.1"),
                    actualScore.getVerifications1(),
                    previousScore.getVerifications1(),
                    actualScore.getScoreLevel1(),
                    previousScore.getScoreLevel1());
        } else {
            return createObservatoryVerificationScoreTableWithEvolutionLevel(
                    messageResources,
                    messageResources.getMessage("observatorio.puntuacion.nivel.2"),
                    actualScore.getVerifications2(),
                    previousScore.getVerifications2(),
                    actualScore.getScoreLevel2(),
                    previousScore.getScoreLevel2());
        }
    }

    private PdfPTable createObservatoryVerificationScoreTableWithEvolutionLevel(final MessageResources messageResources, final String averageResultLabel, final List<LabelValueBean> actualVerifications, final List<LabelValueBean> previousVerifications, final BigDecimal actualScoreLevel, final BigDecimal previousScoreLevel) {
        final float[] columnsWidths = new float[]{0.55f, 0.25f, 0.20f};
        final PdfPTable table = new PdfPTable(columnsWidths);

        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.puntuacion.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
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

    private PdfPCell createEvolutionDifferenceCellValue(final String actualValue, final String previousValue, final Color color) {
        try {
            return createEvolutionDifferenceCellValue(new BigDecimal(actualValue), new BigDecimal(previousValue), color);
        } catch (NumberFormatException nfe) {
            return createEvolutionDifferenceCellValue(BigDecimal.ZERO, BigDecimal.ZERO, color);
        }
    }

    private PdfPCell createEvolutionDifferenceCellValue(final BigDecimal actualValue, final BigDecimal previousValue, final Color color) {
        return PDFUtils.createTableCell(getEvolutionImage(actualValue, previousValue), new DecimalFormat("+0.00;-0.00").format(actualValue.subtract(previousValue)), color, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, DEFAULT_PADDING / 2, -1);
    }

    private void addResultsByPage(final MessageResources messageResources, final Chapter chapter, final File file, final List<ObservatoryEvaluationForm> evaList, final String noDataMess) throws Exception {
        final Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
        final SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("resultados.primarios.43.p1.anchor"), messageResources.getMessage("anchor.PMP"), false, ConstantsFont.ANCHOR_FONT);
        anchorMap.put(1, anchor);
        chapter.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("resultados.primarios.43.p1"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addParagraph(messageResources.getMessage("resultados.primarios.43.p2"), ConstantsFont.PARAGRAPH, chapter);

        chapter.add(Chunk.NEWLINE);

        final String title = messageResources.getMessage("observatory.graphic.score.by.page.title");
        final String filePath = file.getParentFile().getPath() + File.separator + TEMP_SUBDIR + File.separator + "test4.jpg";
        ResultadosPrimariosObservatorioIntavUtils.getScoreByPageGraphic(messageResources, evaList, title, filePath, noDataMess);

        final Image image = PDFUtils.createImage(filePath, null);
        if (image != null) {
            image.scalePercent(70);
            image.setAlignment(Element.ALIGN_CENTER);
            chapter.add(image);
        }

        final float[] widths = {33f, 33f, 33f};
        final PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.punt.media"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.anonimos.level"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.setHeaderRows(1);
        table.setKeepTogether(true);

        int pageCounter = 1;
        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            table.addCell(PDFUtils.createLinkedTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", pageCounter), evaluationForm.getUrl(), Color.white, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(String.valueOf(evaluationForm.getScore().setScale(evaluationForm.getScore().scale() - 1, RoundingMode.UNNECESSARY)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(ObservatoryUtils.getValidationLevel(messageResources, ObservatoryUtils.pageSuitabilityLevel(evaluationForm)), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            pageCounter++;
        }

        table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);
        table.setSpacingAfter(0);
        chapter.add(table);
    }

    private Image getEvolutionImage(final BigDecimal actualValue, final BigDecimal previousValue) {
        return getEvolutionImage(actualValue.compareTo(previousValue));
    }

//    private Image getEvolutionImage(final int actualValue, final int previousValue) {
//        return getEvolutionImage(actualValue - previousValue);
//    }

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
