package es.inteco.rastreador2.pdf.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.form.ObservatoryLevelForm;
import es.inteco.intav.form.ObservatorySuitabilityForm;
import es.inteco.rastreador2.intav.form.ScoreForm;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSection4;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.SpecialChunk;
import es.inteco.rastreador2.utils.ObservatoryUtils;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

/**
 *
 */
public abstract class AnonymousResultExportPdf {

    protected boolean basicService = false;

    public abstract int createIntroductionChapter(MessageResources messageResources, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont) throws Exception;

    public abstract int createObjetiveChapter(MessageResources messageResources, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, List<ObservatoryEvaluationForm> evaList, long observatoryType) throws DocumentException;

    public abstract int createMethodologyChapter(MessageResources messageResources, IndexEvents index, Document document, int countSections, int numChapter, Font titleFont, List<ObservatoryEvaluationForm> primaryReportPageList, long observatoryType, boolean isBasicService) throws Exception;

    protected void createSection34(final MessageResources messageResources, Section section) {
        ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.34.p1.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.34.p1"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.34.p2"), ConstantsFont.PARAGRAPH, section);
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.34.p3"), ConstantsFont.PARAGRAPH, section);
    }

    protected void createSection341(final MessageResources messageResources, final Section section) {
        final PropertiesManager pmgr = new PropertiesManager();

        Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
        SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.341.p1.bold"), messageResources.getMessage("anchor.PMP"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.341.p1"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMP.png", "PMP = SRV/VP*10", 80);

        com.lowagie.text.List list = new com.lowagie.text.List();

        final ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p4"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p6"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<>();
        anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.341.p8.bold"), messageResources.getMessage("anchor.PMPO"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.341.p8"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMPO.png", "PMPO = SPMP/NP", 80);

        list = new com.lowagie.text.List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p9"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p10"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p11"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p12"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p13"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p14"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<>();
        anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.341.p15.bold"), messageResources.getMessage("anchor.PMV"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.341.p15"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMV.png", "PMV = SR/PP*10", 80);

        list = new com.lowagie.text.List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p16"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p17"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p18"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p19"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p20"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p21"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<>();
        anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.341.p22.bold"), messageResources.getMessage("anchor.PMNA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.341.p22"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMNA.png", " PMNA= SPMVN/VN", 80);

        list = new com.lowagie.text.List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p23"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p25"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p26"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p27"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.341.p28"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.341.p29.bold"));
        section.add(PDFUtils.createParagraphWithDiferentFormatWord(messageResources.getMessage("ob.resAnon.intav.report.341.p29"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
    }

    protected void createSection343(final MessageResources messageResources, Section section) {
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.46.p1"), ConstantsFont.PARAGRAPH, section);

        com.lowagie.text.List list = new com.lowagie.text.List();

        final ArrayList<String> boldWords = new ArrayList<>();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p2"));
        ListItem item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p3"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p8"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p9"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p6"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p7"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p10"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p11"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p4"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p5"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.46.p15"), ConstantsFont.PARAGRAPH, section);
        PdfPTable table = createVerificationTable(messageResources);
        table.setSpacingBefore(3 * ConstantsFont.LINE_SPACE);
        section.add(table);
        section.newPage();

        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.46.p16"), ConstantsFont.PARAGRAPH, section);
        PDFUtils.addParagraph(messageResources.getMessage("ob.resAnon.intav.report.46.p17"), ConstantsFont.PARAGRAPH, section);

        Map<Integer, SpecialChunk> anchorMap = new HashMap<>();
        SpecialChunk anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.46.p18.bold"), messageResources.getMessage("anchor.PMPA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.46.p18"), anchorMap, ConstantsFont.PARAGRAPH));

        PropertiesManager pmgr = new PropertiesManager();
        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMAP.png", "PMAP = SPMVA/VA", 75);

        list = new com.lowagie.text.List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p19.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p19"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p20.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p20"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p21.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p21"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);

        anchorMap = new HashMap<>();
        anchor = new SpecialChunk(messageResources.getMessage("ob.resAnon.intav.report.46.p22.bold"), messageResources.getMessage("anchor.PMA"), true, ConstantsFont.paragraphBoldFont);
        anchorMap.put(1, anchor);
        section.add(PDFUtils.createParagraphAnchor(messageResources.getMessage("ob.resAnon.intav.report.46.p22"), anchorMap, ConstantsFont.PARAGRAPH));

        PDFUtils.addImageToSection(section, pmgr.getValue(Constants.PDF_PROPERTIES, "path.images") + "PMA.png", "PMA = SPMA/NP", 75);

        list = new com.lowagie.text.List();

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p23.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p23"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p24.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p24"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        boldWords.clear();
        boldWords.add(messageResources.getMessage("ob.resAnon.intav.report.46.p25.bold"));
        item = PDFUtils.addMixFormatListItem(messageResources.getMessage("ob.resAnon.intav.report.46.p25"), boldWords, ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, false);
        list.add(item);

        list.setIndentationLeft(5 * ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    public int createContentChapter(final MessageResources messageResources, final Document d, final String contents, final IndexEvents index, int numChapter, int countSections) throws DocumentException {
        final Chapter chapter = PDFUtils.createChapterWithTitle(messageResources.getMessage("basic.service.content.title"), index, countSections++, numChapter, ConstantsFont.CHAPTER_TITLE_MP_FONT);

        PDFUtils.addParagraph(messageResources.getMessage("basic.service.content.p1"), ConstantsFont.PARAGRAPH, chapter, Element.ALIGN_JUSTIFIED, true, true);
        PDFUtils.addParagraphCode(HTMLEntities.unhtmlAngleBrackets(contents), "", chapter);
        d.add(chapter);

        return countSections;
    }

    public abstract void getMidsComparationByVerificationLevelGraphic(MessageResources messageResources, String level, String title, String filePath, String noDataMess, List<ObservatoryEvaluationForm> evaList, String value, boolean b) throws Exception;

    public ScoreForm generateScores(final MessageResources messageResources, final java.util.List<ObservatoryEvaluationForm> evaList) {
        final ScoreForm scoreForm = new ScoreForm();

        int suitabilityGroups = 0;

        for (ObservatoryEvaluationForm evaluationForm : evaList) {
            scoreForm.setTotalScore(scoreForm.getTotalScore().add(evaluationForm.getScore()));
// TODO: codigo duplicado en IntavUtils
            final String pageSuitabilityLevel = ObservatoryUtils.pageSuitabilityLevel(evaluationForm);
            if (pageSuitabilityLevel.equals(Constants.OBS_AA)) {
                scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(BigDecimal.TEN));
            } else if (pageSuitabilityLevel.equals(Constants.OBS_A)) {
                scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().add(new BigDecimal(5)));
            }

            for (ObservatoryLevelForm levelForm : evaluationForm.getGroups()) {
                suitabilityGroups = levelForm.getSuitabilityGroups().size();
                if (levelForm.getName().equalsIgnoreCase("priority 1")) {
                    scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().add(levelForm.getScore()));
                } else if (levelForm.getName().equalsIgnoreCase("priority 2")) {
                    scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().add(levelForm.getScore()));
                }
                for (ObservatorySuitabilityForm suitabilityForm : levelForm.getSuitabilityGroups()) {
                    if (suitabilityForm.getName().equalsIgnoreCase("A")) {
                        scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().add(suitabilityForm.getScore()));
                    } else if (suitabilityForm.getName().equalsIgnoreCase("AA")) {
                        scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().add(suitabilityForm.getScore()));
                    }
                }
            }
        }

        generateScoresVerificacion(messageResources, scoreForm, evaList);

        if (!evaList.isEmpty()) {
            scoreForm.setTotalScore(scoreForm.getTotalScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevel1(scoreForm.getScoreLevel1().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevel2(scoreForm.getScoreLevel2().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevelA(scoreForm.getScoreLevelA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setScoreLevelAA(scoreForm.getScoreLevelAA().divide(new BigDecimal(evaList.size()).multiply(new BigDecimal(suitabilityGroups)), 2, BigDecimal.ROUND_HALF_UP));
            scoreForm.setSuitabilityScore(scoreForm.getSuitabilityScore().divide(new BigDecimal(evaList.size()), 2, BigDecimal.ROUND_HALF_UP));
        }

        // El nivel de validación del portal
        scoreForm.setLevel(getValidationLevel(scoreForm, messageResources));

        return scoreForm;
    }

    protected abstract void generateScoresVerificacion(MessageResources messageResources, ScoreForm scoreForm, List<ObservatoryEvaluationForm> evaList);

    public abstract String getTitle();

    public boolean isBasicService() {
        return basicService;
    }

    public void setBasicService(boolean basicService) {
        this.basicService = basicService;
    }

    protected void createSection31(final MessageResources resources, final Section section, final long observatoryType, final String variante) {
        final com.lowagie.text.List list = new com.lowagie.text.List();
        PDFUtils.addParagraph(resources.getMessage("ob.resAnon.intav.report.31.p1." + variante), ConstantsFont.PARAGRAPH, section);

        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p3." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p2." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p5." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p4." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p7." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p6." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p9." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p8." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
        if (observatoryType != Constants.OBSERVATORY_TYPE_EELL) {
            list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p11." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p10." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));

            if (observatoryType == Constants.OBSERVATORY_TYPE_CCAA) {
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p13." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p12." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p15." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p14." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
                list.add(PDFUtils.addMixFormatListItem(resources.getMessage("ob.resAnon.intav.report.31.p17." + variante), Collections.singletonList(resources.getMessage("ob.resAnon.intav.report.31.p16." + variante)), ConstantsFont.paragraphBoldFont, ConstantsFont.PARAGRAPH, true));
            }
        }

        list.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        section.add(list);
    }

    /**
     * Métodos de utilidad
     */
    protected PdfPTable createVerificationTable(final MessageResources messageResources) {
        try {
            final float[] widths = {0.15f, 0.65f, 0.20f};
            final PdfPTable table = new PdfPTable(widths);
            final int margin = 10;

            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.header1"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.header2"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.header3"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

            table.addCell(PDFUtils.createColSpanTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.2header1"), Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification111"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification111.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.alt"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification112"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification112.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.alt"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification113"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification113.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.alt"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification114"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification114.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification121"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification121.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification122"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification122.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.pre"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification123"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification123.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification124"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification124.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification125"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification125.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification126"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification126.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));

            table.addCell(PDFUtils.createColSpanTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.2header2"), Color.GRAY, ConstantsFont.labelCellFont, 3, Element.ALIGN_CENTER));

            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification211"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification211.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification212"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification212.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification213"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification213.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification214"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification214.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification221"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification221.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification222"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification222.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.pre"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification223"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification223.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.gen"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification224"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification224.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification225"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification225.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.nav"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification226"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.verification226.name"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.46.table.aspect.est"), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, margin));

            table.setSpacingAfter(ConstantsFont.LINE_SPACE);

            return table;
        } catch (Exception e) {
            Logger.putLog("Error al crear la tabla 4.5", AnonymousResultExportPdfSection4.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

    protected void createMethodologyHeaderTable(final MessageResources messageResources, final PdfPTable table, final String title) {
        table.addCell(PDFUtils.createColSpanTableCell(title, Constants.VERDE_C_MP, ConstantsFont.labelCellFont, 6, Element.ALIGN_LEFT));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.33.table.header1"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.33.table.header2"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.33.table.header3"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.33.table.header4"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.33.table.header5"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("ob.resAnon.intav.report.33.table.header6"), Color.GRAY, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
    }

    protected void createMethodologyTableRow(final MessageResources messageResources, final PdfPTable table, final String id, final String name, final String question, final com.lowagie.text.List answer, final com.lowagie.text.List value, final com.lowagie.text.List modality) {
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage(id), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage(name), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage(question), Color.WHITE, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0, -1));
        table.addCell(PDFUtils.createListTableCell(answer, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(value, Color.WHITE, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createListTableCell(modality, Color.WHITE, Element.ALIGN_CENTER, 0));
    }

    protected com.lowagie.text.List createTextList(final MessageResources messageResources, final String text) {
        return createTextList(messageResources, text, Element.ALIGN_CENTER);
    }

    protected com.lowagie.text.List createTextList(final MessageResources messageResources, final String text, final int align) {
        final java.util.List<String> list = Arrays.asList(messageResources.getMessage(text).split(";"));
        final com.lowagie.text.List PDFlist = new com.lowagie.text.List();
        for (String str : list) {
            PDFUtils.addListItem(str, PDFlist, ConstantsFont.noteCellFont, false, false, align);
        }
        if (align == Element.ALIGN_LEFT) {
            PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE / 5);
        }

        return PDFlist;
    }

    protected com.lowagie.text.List createImageList(final MessageResources messageResources, final String text) {
        final PropertiesManager pmgr = new PropertiesManager();
        final java.util.List<String> list = Arrays.asList(messageResources.getMessage(text).split(";"));
        final com.lowagie.text.List PDFlist = new com.lowagie.text.List();
        for (String str : list) {
            final com.lowagie.text.Image image;
            if (str.equals("0")) {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.red"), messageResources.getMessage("ob.resAnon.intav.report.33.modality.0.alt"));
            } else {
                image = PDFUtils.createImage(pmgr.getValue(Constants.PDF_PROPERTIES, "path.mode.green"), messageResources.getMessage("ob.resAnon.intav.report.33.modality.1.alt"));
            }
            if (image != null) {
                image.scalePercent(65);
                final ListItem item = new ListItem(new Chunk(image, 0, 0));
                item.setListSymbol(new Chunk(""));
                PDFlist.add(item);
            }
        }
        PDFlist.setIndentationLeft(ConstantsFont.IDENTATION_LEFT_SPACE);
        return PDFlist;
    }

    protected PdfPTable addURLTable(final MessageResources messageResources, final java.util.List<ObservatoryEvaluationForm> primaryReportPageList) {
        final float[] columnsWidths = {20f, 80f};
        final PdfPTable table = new PdfPTable(columnsWidths);
        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setWidthPercentage(100);

        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.pagina"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(messageResources.getMessage("resultados.observatorio.vista.primaria.url"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        int counter = 1;
        for (ObservatoryEvaluationForm page : primaryReportPageList) {
            table.addCell(PDFUtils.createTableCell(messageResources.getMessage("observatory.graphic.score.by.page.label", org.apache.commons.lang3.StringUtils.leftPad(String.valueOf(counter), 2, ' ')), Color.WHITE, ConstantsFont.ANCHOR_FONT, Element.ALIGN_CENTER, 0, "anchor_resultados_page_" + counter));
            table.addCell(PDFUtils.createLinkedTableCell(page.getUrl(), page.getUrl(), Color.WHITE, Element.ALIGN_LEFT, ConstantsFont.DEFAULT_PADDING));
            counter++;
        }

        return table;
    }

    protected String getValidationLevel(final ScoreForm scoreForm, final MessageResources messageResources) {
        if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal(8)) >= 0) {
            return messageResources.getMessage("resultados.anonimos.num.portales.aa");
        } else if (scoreForm.getSuitabilityScore().compareTo(new BigDecimal("3.5")) <= 0) {
            return messageResources.getMessage("resultados.anonimos.num.portales.nv");
        } else {
            return messageResources.getMessage("resultados.anonimos.num.portales.a");
        }
    }


}
