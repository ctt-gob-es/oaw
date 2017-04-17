package es.inteco.rastreador2.pdf.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.pdf.PdfChunk;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.events.IndexEvents;
import es.ctic.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSectionEv;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSections;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public final class PDFUtils {

    // Clase que se usará con urls para permitir que se parta el texto y haga retorno de carro en cualquier caracter.
    private static final SplitCharacter ANY_CHARACTER_WORD_SPLITTER = new SplitCharacter() {
        @Override
        public boolean isSplitCharacter(int i, int i1, int i2, char[] chars, PdfChunk[] pdfChunks) {
            return true;
        }
    };

    private PDFUtils() {
    }

    public static void addCoverPage(final Document document, final String titleText, final String subtitleText) throws DocumentException {
        addCoverPage(document, titleText, subtitleText, null);
    }

    public static void addCoverPage(final Document document, final String titleText, final String subtitleText, final String noticeText) throws DocumentException {
        final Paragraph title = new Paragraph(titleText, ConstantsFont.DOCUMENT_TITLE_MP_FONT);
        title.setSpacingBefore(ConstantsFont.TITLE_LINE_SPACE);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        if (subtitleText != null && !subtitleText.isEmpty()) {
            final Paragraph subtitle = new Paragraph(subtitleText, ConstantsFont.DOCUMENT_SUBTITLE_MP_FONT);
            subtitle.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
            subtitle.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(subtitle);
        }

        if (noticeText != null && !noticeText.isEmpty()) {
            final PdfPTable notice = new PdfPTable(1);
            notice.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
            notice.addCell(PDFUtils.createTableCell(noticeText, Constants.GRIS_MUY_CLARO, ConstantsFont.DOCUMENT_NOTICE_MP_FONT, Element.ALIGN_CENTER, ConstantsFont.DEFAULT_PADDING, 50));
            document.add(notice);
        }
    }

    public static Chapter createChapterWithTitle(String title, PdfTocManager pdfTocManager, Font titleFont) {
        return createChapterWithTitle(title, pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter(), titleFont, true);
    }

    public static Chapter createChapterWithTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont) {
        return createChapterWithTitle(title, index, countSections, numChapter, titleFont, true);
    }

    public static Chapter createChapterWithTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont, boolean upperCase) {
        final Chunk chunk = new Chunk(upperCase?title.toUpperCase():title);
        chunk.setLocalDestination(Constants.ANCLA_PDF + (countSections));
        final Paragraph paragraph = new Paragraph("", titleFont);
        paragraph.add(chunk);
        final Chapter chapter = new Chapter(paragraph, numChapter);
        if (index != null) {
            paragraph.add(index.create(" ", countSections + "@&" + title.toUpperCase()));
        }
        return chapter;
    }

    public static Chapter createChapterWithTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont, boolean upperCase, final String anchor) {
        final Chunk chunk = new Chunk(upperCase?title.toUpperCase():title);
        chunk.setLocalDestination(Constants.ANCLA_PDF + (countSections));
        final Paragraph paragraph = new Paragraph("", titleFont);
        paragraph.add(chunk);
        // Si el capítulo requiere un anchor adicional al que se crea desde el índice (tabla contenidos) entonces se
        // crea un trozo de texto adicional (Chunk) con el caracter especial \u200B (ZWSP Zero White Space) para añadir
        // el ancla oculto sin que se añada nuevo texto. Si se intentase con la cadena vacía no se crea el ancla.
        final Chunk aditionalAnchor = new Chunk("\u200B", titleFont);
        aditionalAnchor.setLocalDestination(anchor);
        paragraph.add(aditionalAnchor);
        final Chapter chapter = new Chapter(paragraph, numChapter);
        if (index != null) {
            paragraph.add(index.create(" ", countSections + "@&" + title.toUpperCase()));
        }
        return chapter;
    }

    public static Section createSection(String title, IndexEvents index, Font levelFont, Section section, int countSections, int level) {
        return createSection(title, index, levelFont, section, countSections, level, null);
    }

    public static Section createSection(String title, IndexEvents index, Font levelFont, Section section, int countSections, int level, String anchor) {
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
        paragraph.setSpacingBefore(ConstantsFont.LINE_SPACE);
        Section sectionL2 = section.addSection(paragraph);
        if (index != null) {
            paragraph.add(index.create(" ", countSections + "@" + level + "&" + title.toUpperCase()));
        }
        return sectionL2;
    }

    public static void addParagraph(String text, Font font, Section section) {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingBefore(ConstantsFont.LINE_SPACE);
        p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        section.add(p);
    }

    public static void addParagraph(String text, Font font, Section section, int align) {
        addParagraph(text, font, section, align, true, false);
    }

    public static void addParagraph(String text, Font font, Section section, int align, boolean spaceBefore, boolean spaceAfter) {
        Paragraph p = new Paragraph(text, font);
        if (spaceBefore) {
            p.setSpacingBefore(ConstantsFont.LINE_SPACE);
        }
        if (spaceAfter) {
            p.setSpacingAfter(ConstantsFont.LINE_SPACE);
        }
        p.setAlignment(align);
        section.add(p);
    }

    public static void addCode(final String text, final Section section) {
        float[] widths = {100f};
        final PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        table.setKeepTogether(false);

        for (String linea : text.split(System.lineSeparator())) {
            if ( !linea.trim().isEmpty() ) {
                final PdfPCell labelCell = new PdfPCell();
                labelCell.setBackgroundColor(new Color(255, 244, 223));
                labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                labelCell.setPadding(2f);
                labelCell.setPaddingTop(0);
                labelCell.setBorderWidth(0);
                labelCell.addElement(new Chunk(linea, ConstantsFont.noteCellFont));
                table.addCell(labelCell);
            }
        }

        table.setSpacingBefore(ConstantsFont.THIRD_LINE_SPACE);
        table.setSpacingAfter(ConstantsFont.HALF_LINE_SPACE);


        section.add(table);
    }

    public static void addParagraphRationale(final java.util.List<String> text, final Section section) {
        final float[] widths = {95f};
        final PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(95);
        table.setSpacingBefore(ConstantsFont.HALF_LINE_SPACE);

        final Paragraph paragraph = new Paragraph();
        boolean isFirst = true;
        for (String phraseText : text) {
            if (isFirst) {
                if (StringUtils.isNotEmpty(phraseText)) {
                    paragraph.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.MORE_INFO_FONT));
                }
                isFirst = false;
            } else {
                paragraph.add(new Phrase(StringUtils.removeHtmlTags(phraseText) + "\n", ConstantsFont.MORE_INFO_FONT));
            }
        }

        final PdfPCell labelCell = new PdfPCell(paragraph);
        labelCell.setBackgroundColor(Constants.GRIS_MUY_CLARO);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPadding(ConstantsFont.DEFAULT_PADDING);
        table.addCell(labelCell);

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
        p.setSpacingBefore(ConstantsFont.LINE_SPACE);
        return p;
    }

    public static Paragraph createParagraphAnchor(String text, Map<Integer, SpecialChunk> specialChunkMap, Font font) {
        return createParagraphAnchor(text, specialChunkMap, font, true);
    }

    /**
     * Crea un párrafo (Paragraph) de documento PDF a partir de un texto que contiene las marcas [anchor1]..[anchorX]
     * en el que se sustituyen las marcas por el valor equivalente que se suministra.
     *
     * @param text            El texto que contendrá el párrafo (puede contener marcar [anchor1]..[anchorX].
     * @param specialChunkMap Mapa con las sustituciones para cada una de las marcas [anchorX].
     * @param font            El tipo de fuente que se usará para crear el párrafo.
     * @param spaceBefore     Flag para indicar si se debe dejar espacion (margen) antes del párrafo.
     * @return Un Paragraph de PDF con el texto indicado en el que se han sustituido las marcas por sus valores.
     */
    public static Paragraph createParagraphAnchor(final String text, final Map<Integer, SpecialChunk> specialChunkMap, final Font font, boolean spaceBefore) {
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
            final Chunk finalChunk = new Chunk(text.substring(init, text.length()), font);
            p.add(finalChunk);
        }

        if (spaceBefore) {
            p.setSpacingBefore(ConstantsFont.LINE_SPACE);
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
                    p.setSpacingBefore(ConstantsFont.LINE_SPACE);
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
            p.setSpacingBefore(ConstantsFont.LINE_SPACE);
        }
        ListItem item = new ListItem(p);
        if (!withSymbol) {
            item.setListSymbol(new Chunk(""));
        }
        item.setAlignment(align);
        list.add(item);
    }

    public static ListItem createListItem(final String text, final Font font, final Chunk symbol, final boolean spaceBefore) {
        final ListItem item = new ListItem(text, font);
        if (symbol != null) {
            item.setListSymbol(symbol);
        }
        if (spaceBefore) {
            item.setSpacingBefore(ConstantsFont.LINE_SPACE);
        }
        return item;
    }

    public static ListItem buildLeyendaListItem(final String message, final String symbol) {
        return createListItem(message, ConstantsFont.MORE_INFO_FONT, new Chunk(symbol + " "), false);
    }

    public static Image createImage(final String path, final String alt) {
        try {
            final Image image = Image.getInstance(path);
            image.setAlt(alt);
            image.setAlignment(Element.ALIGN_CENTER);
            return image;
        } catch (Exception e) {
            Logger.putLog("Imagen no encontrada", AnonymousResultExportPdfSections.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return null;
    }

    public static PdfPCell createLinkedTableCell(final String text, final String url, final Color backgroundColor, int horizontalAlignment, int padding) {
        return createLinkedTableCell(text, url, backgroundColor, horizontalAlignment, padding, -1);
    }

    public static PdfPCell createLinkedTableCell(final String text, final String url, final Color backgroundColor, int horizontalAlignment, int padding, int height) {
        final Chunk chunk = new Chunk(text, ConstantsFont.LINK_FONT);
        chunk.setAnchor(url);
        chunk.setSplitCharacter(ANY_CHARACTER_WORD_SPLITTER);
        final PdfPCell labelCell = new PdfPCell(new Paragraph(chunk));
        labelCell.setBackgroundColor(backgroundColor);
        labelCell.setHorizontalAlignment(horizontalAlignment);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPaddingLeft(padding);
        if (height > 0) {
            labelCell.setFixedHeight(height);
        }

        return labelCell;
    }

    public static PdfPCell createTableCell(final Image image, final String text, final Color backgroundColor, final Font font, int horizontalAlignment, int padding, int height) {
        image.scalePercent(40);
        final PdfPCell labelCell = new PdfPCell();
        final Paragraph imageParagraph = PDFUtils.createImageTextParagraph(image, " " + text, font);
        imageParagraph.setLeading(image.getHeight());
        labelCell.addElement(imageParagraph);
        labelCell.setBackgroundColor(backgroundColor);
        labelCell.setHorizontalAlignment(horizontalAlignment);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPadding(padding);
        labelCell.setUseAscender(true);
        labelCell.setUseDescender(true);
        if (height != -1) {
            labelCell.setFixedHeight(height);
        }
        return labelCell;
    }

    public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int padding) {
        return createTableCell(text, backgroundColor, font, align, padding, 17f);
    }

    public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int padding, final float height) {
        final PdfPCell labelCell = new PdfPCell(new Paragraph(text, font));
        labelCell.setBackgroundColor(backgroundColor);
        labelCell.setHorizontalAlignment(align);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPadding(padding);
        if (height != -1) {
            labelCell.setFixedHeight(height);
        }
        return labelCell;
    }

    public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int margin, final String anchorId) {
        final Chunk chunk = new Chunk(text, font);
        chunk.setLocalGoto(anchorId);
        final PdfPCell labelCell = new PdfPCell(new Paragraph(chunk));
        labelCell.setBackgroundColor(backgroundColor);
        labelCell.setHorizontalAlignment(align);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        labelCell.setPaddingLeft(margin);
        labelCell.setFixedHeight(17f);

        return labelCell;
    }

    public static PdfPCell createListTableCell(final List list, final Color backgroundColor, final int horizontalAlign, final int margin) {
        return createListTableCell(list, backgroundColor, horizontalAlign, Element.ALIGN_MIDDLE, margin);
    }

    public static PdfPCell createListTableCell(final List list, final Color backgroundColor, final int horizontalAlign, final int verticalAlign, final int margin) {
        final PdfPCell labelCell = new PdfPCell();
        labelCell.addElement(list);
        labelCell.setBackgroundColor(backgroundColor);
        labelCell.setHorizontalAlignment(horizontalAlign);
        labelCell.setVerticalAlignment(verticalAlign);
        labelCell.setPaddingLeft(margin);
        return labelCell;
    }

    public static PdfPCell createColSpanTableCell(final String text, final Color backgroundColor, final Font font, final int colSpan, final int align) {
        final int margin = (align == Element.ALIGN_LEFT) ? 5 : 0;

        final PdfPCell labelCell = createTableCell(text, backgroundColor, font, align, margin);
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

        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
        table.setSpacingAfter(ConstantsFont.LINE_SPACE);
        return table;
    }

    public static void createTitleTable(final String text, final Section section, final float scaleX) throws BadElementException, IOException {
        final PropertiesManager pmgr = new PropertiesManager();
        final Image img = Image.getInstance(pmgr.getValue("pdf.properties", "path.images") + pmgr.getValue("pdf.properties", "name.table.line.roja.image"));
        img.setAlt("");
        img.scaleAbsolute(scaleX, img.getHeight() / 2);
        img.setAlignment(Element.ALIGN_CENTER);

        section.add(img);
        PDFUtils.addParagraph(text, ConstantsFont.PARAGRAPH_TITLE_TABLE_FONT, section, Element.ALIGN_CENTER, false, false);
        section.add(img);
    }

    public static PdfPTable createTableMod(final MessageResources resources, final java.util.List<ModalityComparisonForm> result) {
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

        table.setSpacingBefore(ConstantsFont.LINE_SPACE);
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
            return replaceAccent(seedName.trim().toLowerCase()).replaceAll("[\\s,./\\\\]+", "_");
        }
        return null;
    }

    public static void addImageToSection(final Section section, final String imagePath, final String imageAlt, final float scale) {
        try {
            final Image graphic = createImage(imagePath, imageAlt);
            if (graphic != null) {
                graphic.scalePercent(scale);
                section.add(graphic);
            }
        } catch (Exception e) {
            Logger.putLog("Error al crear imagen en la exportación anónima de resultados", AnonymousResultExportPdfSectionEv.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    /**
     * Crea una celda vacía para una tabla. Una celda vacía se considera especial y se elimina el borde.
     *
     * @return una celda PdfPCell sin texto y sin borde.
     */
    public static PdfPCell createEmptyTableCell() {
        final PdfPCell celdaVacia = new PdfPCell(new Phrase(""));
        celdaVacia.setBorder(0);
        return celdaVacia;
    }
}
