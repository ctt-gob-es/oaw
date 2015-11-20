package es.inteco.rastreador2.pdf.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSectionEv;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSections;
import es.inteco.rastreador2.utils.CrawlerUtils;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public final class PDFUtils {

    private PDFUtils() {
    }


    public static void addTitlePage(final Document document, final String titleText, final String subtitleText, final Font titleFont) throws DocumentException {
        document.add(Chunk.NEWLINE);
        final Paragraph title = new Paragraph(titleText, titleFont);
        title.setSpacingBefore(ConstantsFont.SPACE_TITLE_LINE);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        if (!subtitleText.equals("")) {
            final Paragraph subtitle = new Paragraph(subtitleText, titleFont);
            subtitle.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(subtitle);
        }
    }

    public static Chapter addChapterTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont) {
        return addChapterTitle(title, index, countSections, numChapter, titleFont, true);
    }

    public static Chapter addChapterTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont, boolean upperCase) {
        if (upperCase) {
            title = title.toUpperCase();
        }
        Chunk chunk = new Chunk(title);
        chunk.setLocalDestination(Constants.ANCLA_PDF + (countSections));
        Paragraph paragraph = new Paragraph("", titleFont);
        paragraph.add(chunk);
        Chapter chapter = new Chapter(paragraph, numChapter);
        if (index != null) {
            paragraph.add(index.create(" ", countSections + "@&" + title));
        }
        return chapter;
    }

    public static Section addSection(String title, IndexEvents index, Font levelFont, Section section, int countSections, int level) {
        return addSection(title, index, levelFont, section, countSections, level, null);
    }

    public static Section addSection(String title, IndexEvents index, Font levelFont, Section section, int countSections, int level, String anchor) {
        Chunk chunk = new Chunk(title.toUpperCase());
        Paragraph paragraph = new Paragraph("", levelFont);
        Chunk whiteChunk = new Chunk(" ");
        if (countSections != -1) {
            chunk.setLocalDestination(Constants.ANCLA_PDF + (countSections));
        }
        paragraph.add(chunk);
        if (anchor != null) {
            whiteChunk.setLocalDestination(anchor);
            paragraph.add(whiteChunk);
        }
        paragraph.setSpacingBefore(ConstantsFont.SPACE_LINE);
        Section sectionL2 = section.addSection(paragraph);
        if (index != null) {
            paragraph.add(index.create(" ", countSections + "@" + level + "&" + title.toUpperCase()));
        }
        return sectionL2;
    }

    public static void addParagraph(String text, Font font, Section section) {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingBefore(ConstantsFont.SPACE_LINE);
        p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        section.add(p);
    }

    public static void addParagraph(String text, Font font, Section section, int align) {
        addParagraph(text, font, section, align, true, false);
    }

    public static void addParagraph(String text, Font font, Section section, int align, boolean spaceBefore, boolean spaceAfter) {
        Paragraph p = new Paragraph(text, font);
        if (spaceBefore) {
            p.setSpacingBefore(ConstantsFont.SPACE_LINE);
        }
        if (spaceAfter) {
            p.setSpacingAfter(ConstantsFont.SPACE_LINE);
        }
        p.setAlignment(align);
        section.add(p);
    }

    public static void addParagraphCode(String text, String message, Section section) {
        float[] widths = {100f};
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        java.util.List<String> boldWords = new ArrayList<String>();
        if (!StringUtils.isEmpty(message)) {
            text = "{0} \n\n" + text.trim();
            boldWords.add(message);
        }

        PdfPCell labelCell = new PdfPCell(PDFUtils.createParagraphWithDiferentFormatWord(text, boldWords, ConstantsFont.strongNoteCellFont, ConstantsFont.noteCellFont, false));
        labelCell.setBackgroundColor(new Color(255, 244, 223));
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPadding(5f);
        table.addCell(labelCell);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 3);
        section.add(table);
    }

    public static void addParagraphRationale(java.util.List<String> text, Section section) {
        float[] widths = {95f};
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(95);

        Paragraph paragraph = new Paragraph();
        boolean isFirst = true;
        for (String phraseText : text) {
            if (isFirst) {
                if (StringUtils.isNotEmpty(phraseText)) {
                    paragraph.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.moreInfoFont));
                }
                isFirst = false;
            } else {
                paragraph.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.moreInfoFont));
            }
        }

        PdfPCell labelCell = new PdfPCell(paragraph);
        labelCell.setBackgroundColor(new Color(245, 245, 245));
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPadding(5f);
        table.addCell(labelCell);
        table.setSpacingBefore(ConstantsFont.SPACE_LINE / 2);
        section.add(table);
    }

    public static Paragraph createImageTextParagraph(Image image, String text, Font font) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(image, 0, 0));
        if (text != null) {
            p.add(new Chunk(text, font));
        }
        p.setAlignment(Element.ALIGN_CENTER);
        return p;
    }

    public static Paragraph createImageParagraphWithDiferentFont(Image image, String text, Font font, String text2, Font font2, int alignment) {
        Paragraph p = new Paragraph();
        if (image != null) {
            p.add(new Chunk(image, 0, 0));
        }
        p.add(new Chunk(text, font));
        if (!StringUtils.isEmpty(text2)) {
            p.add(new Chunk(text2, font2));
        }
        p.setAlignment(alignment);
        p.setSpacingBefore(ConstantsFont.SPACE_LINE);
        return p;
    }

    public static Paragraph createParagraphAnchor(String text, Map<Integer, SpecialChunk> specialChunkMap, Font font) {
        return createParagraphAnchor(text, specialChunkMap, font, true);
    }

    public static Paragraph createParagraphAnchor(String text, Map<Integer, SpecialChunk> specialChunkMap, Font font, boolean spaceBefore) {
        int init = 0;
        int iAnchor = 1;
        int iFormat = 0;
        Paragraph p = new Paragraph();

        for (Map.Entry<Integer, SpecialChunk> specialChunkEntry : specialChunkMap.entrySet()) {
            Chunk chunk;
            int indexOf = 0;
            if ((text.indexOf("[anchor" + iAnchor + "]") > 0) && (text.indexOf("[anchor" + iAnchor + "]") > init)) {
                indexOf = text.indexOf("[anchor" + iAnchor + "]");
            }
            if ((text.indexOf("{" + iFormat + "}") > 0) && ((indexOf != 0 && text.indexOf("{" + iFormat + "}") < indexOf) || indexOf == 0) && (text.indexOf("{" + iFormat + "}") > init)) {
                indexOf = text.indexOf("{" + iFormat + "}");
            }
            if (indexOf != 0) {
                chunk = new Chunk(text.substring(init, indexOf), font);
                p.add(chunk);
            }

            if (specialChunkEntry.getValue().getAnchor() != null && !StringUtils.isEmpty(specialChunkEntry.getValue().getAnchor())) {
                p = createAnchor(specialChunkEntry.getKey(), specialChunkMap, p);
                init = text.indexOf("[anchor" + iAnchor + "]") + 8 + String.valueOf(iAnchor).length();
                iAnchor++;
            } else {
                p = createSpecialFormatText(specialChunkEntry.getKey(), specialChunkMap, p);
                init = text.indexOf("{" + iFormat + "}") + 2 + String.valueOf(iFormat).length();
                iFormat++;
            }
        }
        if (text.length() > init) {
            Chunk finalChunk = new Chunk(text.substring(init, text.length()), font);
            p.add(finalChunk);
        }

        if (spaceBefore) {
            p.setSpacingBefore(ConstantsFont.SPACE_LINE);
        }
        p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        return p;
    }

    private static Paragraph createSpecialFormatText(Integer anchor, Map<Integer, SpecialChunk> anchorMap, Paragraph p) {
        Chunk anchorChunk = new Chunk(anchorMap.get(anchor).getText(), anchorMap.get(anchor).getFont());
        p.add(anchorChunk);
        return p;
    }

    private static Paragraph createAnchor(Integer anchor, Map<Integer, SpecialChunk> anchorMap, Paragraph p) {
        Chunk anchorChunk = new Chunk(anchorMap.get(anchor).getText(), anchorMap.get(anchor).getFont());
        if (anchorMap.get(anchor).isExternalLink()) {
            anchorChunk.setAnchor(anchorMap.get(anchor).getAnchor());
        } else {
            if (!anchorMap.get(anchor).isDestination()) {
                anchorChunk.setLocalGoto(anchorMap.get(anchor).getAnchor());
            } else {
                anchorChunk.setLocalDestination(anchorMap.get(anchor).getAnchor());
            }
        }
        p.add(anchorChunk);
        return p;
    }

    public static Paragraph createParagraphWithDiferentFormatWord(String text, java.util.List<String> boldWords, Font fontB, Font font, boolean spaceBefore) {
        return createParagraphWithDiferentFormatWord(text, boldWords, fontB, font, spaceBefore, Paragraph.ALIGN_JUSTIFIED);
    }

    public static Phrase createPhrase(String text, Font font) {
        return new Phrase(text, font);
    }

    public static Phrase createPhraseLink(String text, String link, Font font) {
        return new Phrase(new Chunk(text, font).setAnchor(link));
    }

    public static Paragraph createParagraphWithDiferentFormatWord(String text, java.util.List<String> boldWords, Font fontB, Font font, boolean spaceBefore, int alignment) {
        Paragraph p = new Paragraph();
        p.setAlignment(alignment);
        if (text != null && text.contains("{0}")) {
            int count = 0;
            int textLength = text.length();
            try {
                for (int i = 0; i < boldWords.size(); i++) {
                    String normalText = text.substring(count, text.indexOf("{" + i + "}"));
                    count = normalText.length() + count + ("{" + i + "}").length();
                    Phrase phraseN = new Phrase(normalText, font);
                    Phrase phraseB = new Phrase(boldWords.get(i), fontB);
                    p.add(phraseN);
                    p.add(phraseB);
                    textLength = textLength + boldWords.get(i).length() - 3;
                }
                if (textLength > p.getContent().length()) {
                    Phrase phraseN = new Phrase(text.substring(count), font);
                    p.add(phraseN);
                }
                if (spaceBefore) {
                    p.setSpacingBefore(ConstantsFont.SPACE_LINE);
                }
                return p;
            } catch (Exception e) {
                Logger.putLog("FALLO faltan parámetros en el texto al generar informe PDF. ", PDFUtils.class, Logger.LOG_LEVEL_ERROR, e);
            }
        } else if (text != null) {
            p.add(new Phrase(text, font));
        }
        return p;
    }

    public static Paragraph addLinkParagraph(String text, String link, Font font) {
        return new Paragraph(new Chunk(text, font).setAnchor(link));
    }

    public static ListItem addMixFormatListItem(String text, java.util.List<String> words, Font fontB, Font font, boolean spaceAfter) {
        Paragraph p = createParagraphWithDiferentFormatWord(text, words, fontB, font, spaceAfter);
        return new ListItem(p);
    }

    public static void addListItem(String text, List list, Font font, boolean spaceBefore, boolean withSymbol) {
        addListItem(text, list, font, spaceBefore, withSymbol, Element.ALIGN_LEFT);
    }

    public static void addListItem(String text, List list, Font font) {
        addListItem(text, list, font, true, true, Element.ALIGN_LEFT);
    }

    public static void addListItem(String text, List list, Font font, boolean spaceBefore, boolean withSymbol, int align) {
        Paragraph p = new Paragraph(text, font);
        if (spaceBefore) {
            p.setSpacingBefore(ConstantsFont.SPACE_LINE);
        }
        ListItem item = new ListItem(p);
        if (!withSymbol) {
            item.setListSymbol(new Chunk(""));
        }
        item.setAlignment(align);
        list.add(item);
    }

    public static ListItem createListItem(String text, Font font, Chunk symbol, boolean spaceBefore) {
        Paragraph p = new Paragraph(text, font);
        ListItem item = new ListItem(p);
        if (symbol != null) {
            item.setListSymbol(symbol);
        }
        if (spaceBefore) {
            p.setSpacingBefore(ConstantsFont.SPACE_LINE);
        }
        return item;
    }

    public static Image createImage(String path, String alt) {
        try {
            Image image = Image.getInstance(path);
            image.setAlt(alt);
            image.setAlignment(Element.ALIGN_CENTER);
            return image;
        } catch (Exception e) {
            Logger.putLog("Imagen no encontrada", AnonymousResultExportPdfSections.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

    public static PdfPCell createTableCell(final MessageResources resources, final String key, final Color backgroundColor, final Font font, final int align, final int margin) {
        return createTableCell(resources.getMessage(key), backgroundColor, font, align, margin);
    }

    public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int margin) {
        return createTableCell(text, backgroundColor, font, align, margin, 17f);
    }

    public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int margin, final float height) {
        final PdfPCell labelCell = new PdfPCell(new Paragraph(text, font));
        labelCell.setBackgroundColor(backgroundColor);
        labelCell.setHorizontalAlignment(align);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPaddingLeft(margin);
        if (height != -1) {
            labelCell.setFixedHeight(height);
        }
        return labelCell;
    }

    public static PdfPCell createListTableCell(final List list, final Color backgroundColor, final int align, final int margin) {
        final PdfPCell labelCell = new PdfPCell();
        labelCell.addElement(list);
        labelCell.setBackgroundColor(backgroundColor);
        labelCell.setHorizontalAlignment(align);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPaddingLeft(margin);
        return labelCell;
    }

    public static PdfPCell createColSpanTableCell(final MessageResources resources, String text, Color backgroundColor, Font font, int colSpan, int align) {
        return createColSpanTableCell(resources.getMessage(text), backgroundColor, font, colSpan, align);
    }

    public static PdfPCell createColSpanTableCell(String text, Color backgroundColor, Font font, int colSpan, int align) {
        int margin = 0;
        if (align == Element.ALIGN_LEFT) {
            margin = 5;
        }
        PdfPCell labelCell = createTableCell(text, backgroundColor, font, align, margin);
        labelCell.setColspan(colSpan);
        return labelCell;
    }

    public static PdfPTable createResultTable(java.util.List<LabelValueBean> results, java.util.List<String> headers) {
        float[] widths = {50f, 50f};
        PdfPTable table = new PdfPTable(widths);

        for (String header : headers) {
            table.addCell(PDFUtils.createTableCell(header, Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        }
        for (LabelValueBean label : results) {
            table.addCell(createTableCell(label.getLabel(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 5));
            table.addCell(createTableCell(label.getValue(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        table.setSpacingAfter(ConstantsFont.SPACE_LINE);
        return table;
    }

    public static void createTitleTable(final String text, final Section section, final float scaleX) throws BadElementException, IOException {
        final PropertiesManager pmgr = new PropertiesManager();
        final Image img = Image.getInstance(pmgr.getValue("pdf.properties", "path.images") + pmgr.getValue("pdf.properties", "name.table.line.roja.image"));
        img.setAlt("");
        img.scaleAbsolute(scaleX, img.getHeight() / 2);
        img.setAlignment(Element.ALIGN_CENTER);

        section.add(img);
        PDFUtils.addParagraph(text, ConstantsFont.paragraphTitleTableFont, section, Element.ALIGN_CENTER, false, false);
        section.add(img);
    }

    public static PdfPTable createTableMod(HttpServletRequest request, java.util.List<ModalityComparisonForm> result) {
        return createTableMod(CrawlerUtils.getResources(request), result);
    }

    public static PdfPTable createTableMod(final MessageResources resources, java.util.List<ModalityComparisonForm> result) {
        final float[] widths = {50f, 25f, 25f};
        final PdfPTable table = new PdfPTable(widths);
        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.puntuacion.verificacion"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.porc.pasa"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));
        table.addCell(PDFUtils.createTableCell(resources.getMessage("resultados.anonimos.porc.falla"), Constants.VERDE_C_MP, ConstantsFont.labelCellFont, Element.ALIGN_CENTER, 0));

        for (ModalityComparisonForm form : result) {
            table.addCell(PDFUtils.createTableCell(resources.getMessage(form.getVerification()), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_LEFT, 5));
            table.addCell(PDFUtils.createTableCell(form.getGreenPercentage(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
            table.addCell(PDFUtils.createTableCell(form.getRedPercentage(), Color.white, ConstantsFont.noteCellFont, Element.ALIGN_CENTER, 0));
        }

        table.setSpacingBefore(ConstantsFont.SPACE_LINE);
        return table;
    }

    public static String replaceAccent(final String phrase) {
        return phrase.replace('á', 'a')
                .replace('à', 'a')
                .replace('â', 'a')
                .replace('ã', 'a')
                .replace('ä', 'a')
                .replace('å', 'a')
                .replace('é', 'e')
                .replace('è', 'e')
                .replace('ê', 'e')
                .replace('ë', 'e')
                .replace('í', 'i')
                .replace('ì', 'i')
                .replace('ï', 'i')
                .replace('î', 'i')
                .replace('ó', 'o')
                .replace('ò', 'o')
                .replace('ô', 'o')
                .replace('ö', 'o')
                .replace('õ', 'o')
                .replace('ú', 'u')
                .replace('ù', 'u')
                .replace('ü', 'u')
                .replace('û', 'u')
                .replace('ý', 'y')
                .replace('ÿ', 'y')
                .replace('ñ', 'n')
                .replace('ç', 'c')
                .replace("ª", "a.")
                .replace("º", "o.");
    }

    public static String formatSeedName(final String seedName) {
        if (seedName != null) {
            return replaceAccent(seedName.trim()).replaceAll("[\\s,.]+", "_").toLowerCase();
        }
        return seedName;
    }

    public static void addImageToSection(final Section section, final String imagePath, final String imageAlt, final float scale) {
        try {
            final Image graphic = createImage(imagePath, imageAlt);
            graphic.scalePercent(scale);
            section.add(graphic);
        } catch (Exception e) {
            Logger.putLog("Error al crear imagen en la exportación anónima de resultados", AnonymousResultExportPdfSectionEv.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

}
