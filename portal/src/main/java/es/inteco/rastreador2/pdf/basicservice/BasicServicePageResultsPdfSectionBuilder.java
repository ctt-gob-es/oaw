package es.inteco.rastreador2.pdf.basicservice;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;
import es.ctic.rastreador2.pdf.utils.CheckDescriptionsManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.form.*;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.rastreador2.pdf.utils.PDFUtils;
import es.inteco.rastreador2.pdf.utils.PdfTocManager;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static es.inteco.common.ConstantsFont.DEFAULT_PADDING;
import static es.inteco.common.ConstantsFont.HALF_LINE_SPACE;

/**
 * Created by mikunis on 1/18/17.
 */
public class BasicServicePageResultsPdfSectionBuilder extends ObservatoryPageResultsPdfSectionBuilder {

    public BasicServicePageResultsPdfSectionBuilder(final List<ObservatoryEvaluationForm> currentEvaluationPageList) {
        super(currentEvaluationPageList);
    }

    @Override
    public void addPageResults(final MessageResources messageResources, final Document document, final PdfTocManager pdfTocManager) throws Exception {
        int counter = 1;
        for (ObservatoryEvaluationForm evaluationForm : currentEvaluationPageList) {
            final String chapterTitle = messageResources.getMessage("observatory.graphic.score.by.page.label", counter);
            final Chapter chapter = PDFUtils.createChapterWithTitle(chapterTitle, pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter(), ConstantsFont.CHAPTER_TITLE_MP_FONT, true, "anchor_resultados_page_" + counter);

            chapter.add(createPaginaTableInfo(messageResources, evaluationForm));

            // Creación de las tablas resumen de resultado por verificación de cada página
            for (ObservatoryLevelForm observatoryLevelForm : evaluationForm.getGroups()) {
                final Paragraph levelTitle = new Paragraph(getPriorityName(messageResources, observatoryLevelForm.getName()), ConstantsFont.CHAPTER_TITLE_MP_FONT_3_L);
                levelTitle.setSpacingBefore(HALF_LINE_SPACE);
                chapter.add(levelTitle);
                chapter.add(createPaginaTableVerificationSummary(messageResources, observatoryLevelForm));
            }

            addCheckCodes(messageResources, evaluationForm, chapter);

            document.add(chapter);
            pdfTocManager.addChapterCount();
            counter++;
        }
    }


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

                                    final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
                                    final String rationaleMessage = checkDescriptionsManager.getRationaleMessage(problem.getCheck());
                                    if (rationaleMessage != null && StringUtils.isNotEmpty(rationaleMessage)) {
                                        final Paragraph rationale = new Paragraph();
                                        boolean isFirst = true;
                                        for (String phraseText : Arrays.asList(rationaleMessage.split("<p>|</p>"))) {
                                            if (isFirst) {
                                                if (StringUtils.isNotEmpty(phraseText)) {
                                                    rationale.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.descriptionFont));
                                                }
                                                isFirst = false;
                                            } else {
                                                rationale.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.descriptionFont));
                                            }
                                        }

                                        tablaVerificacionProblema.addCell(PDFUtils.createEmptyTableCell());

                                        final PdfPCell celdaRationale = new PdfPCell(rationale);
                                        celdaRationale.setBorder(0);
                                        celdaRationale.setBackgroundColor(Color.WHITE);
                                        celdaRationale.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                        celdaRationale.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        celdaRationale.setPadding(DEFAULT_PADDING);
                                        tablaVerificacionProblema.addCell(celdaRationale);
                                    }

                                    addSpecificProblems(messageResources, levelSection, problem.getSpecificProblems());

                                    if ("232".equals(problem.getCheck()) || EvaluatorUtils.isCssValidationCheck(Integer.parseInt(problem.getCheck()))) {
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

    private void addSpecificProblems(final MessageResources messageResources, final Section subSubSection, final List<SpecificProblemForm> specificProblems) {
        final PropertiesManager pmgr = new PropertiesManager();
        final float[] widths = {8f, 12f, 80f};
        final PdfPTable table = new PdfPTable(widths);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setWidthPercentage(86);
        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setSpacingAfter(ConstantsFont.HALF_LINE_SPACE);
        table.addCell(PDFUtils.createTableCell("Línea", Constants.GRIS_MUY_CLARO, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell("Columna", Constants.GRIS_MUY_CLARO, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        table.addCell(PDFUtils.createTableCell("Código", Constants.GRIS_MUY_CLARO, ConstantsFont.descriptionFont, Element.ALIGN_CENTER, DEFAULT_PADDING, -1));
        // Indicamos que la primera fila es de  encabezados para que la repita si la tabla se parte en varias páginas.
        table.setHeaderRows(1);
        int maxNumErrors = Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"));
        for (SpecificProblemForm specificProblem : specificProblems) {
            maxNumErrors--;
            if (specificProblem.getCode() != null) {
                final StringBuilder code = new StringBuilder();
                for (int i = 0; i < specificProblem.getCode().size(); i++) {
                    code.append(specificProblem.getCode().get(i)).append("\n");
                }

                if (!specificProblem.getLine().isEmpty() && !"-1".equalsIgnoreCase(specificProblem.getLine())) {
                    table.addCell(PDFUtils.createTableCell(specificProblem.getLine(), Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
                    table.addCell(PDFUtils.createTableCell(specificProblem.getColumn(), Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
                } else {
                    table.addCell(PDFUtils.createTableCell("-", Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
                    table.addCell(PDFUtils.createTableCell("-", Color.WHITE, ConstantsFont.codeCellFont, Element.ALIGN_RIGHT, DEFAULT_PADDING));
                }
                String text = HTMLEntities.unhtmlAngleBrackets(code.toString());
                final String message = specificProblem.getMessage();

                java.util.List<String> boldWords = new ArrayList<>();
                if (!StringUtils.isEmpty(message)) {
                    text = "{0} \n\n" + text.trim();
                    boldWords.add(message);
                }

                final PdfPCell labelCell = new PdfPCell(PDFUtils.createParagraphWithDiferentFormatWord(text, boldWords, ConstantsFont.codeCellFont, ConstantsFont.codeCellFont, false));
                labelCell.setPadding(0);
                labelCell.setBackgroundColor(new Color(255, 244, 223));
                labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                labelCell.setPadding(DEFAULT_PADDING);
                table.addCell(labelCell);
            } else if (specificProblem.getNote() != null) {
                final String linkCode = getMatch(specificProblem.getNote().get(0), "(<a.*?</a>)");
                final String paragraphText = specificProblem.getNote().get(0).replace(linkCode, "");

                final String linkHref = getMatch(specificProblem.getNote().get(0), "href='(.*?)'");

                final Paragraph p = new Paragraph(paragraphText, ConstantsFont.noteCellFont);
                final Anchor anchor = new Anchor(getMatch(specificProblem.getNote().get(0), "<a.*?>(.*?)</a>"), ConstantsFont.NOTE_ANCHOR_CELL_FONT);
                anchor.setReference(linkHref);
                p.add(anchor);
                subSubSection.add(p);
            }

            if (maxNumErrors < 0) {
                if (specificProblems.size() > Integer.parseInt(pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number"))) {
                    final String[] arguments = new String[2];
                    arguments[0] = pmgr.getValue(Constants.PDF_PROPERTIES, "pdf.intav.specific.problems.number");
                    arguments[1] = String.valueOf(specificProblems.size());
                    final Paragraph p = new Paragraph(messageResources.getMessage("pdf.accessibility.bs.num.errors.summary", arguments), ConstantsFont.MORE_INFO_FONT);
                    p.setAlignment(Paragraph.ALIGN_RIGHT);
                    subSubSection.add(p);
                }

                break;
            }
        }
        subSubSection.add(table);
    }

    private String getMatch(final String text, final String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : null;
    }

}
