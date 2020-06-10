/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.rastreador2.pdf.utils;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.SplitCharacter;
import com.itextpdf.text.pdf.PdfChunk;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.events.IndexEvents;

import es.gob.oaw.rastreador2.pdf.utils.PdfTocManager;
import es.inteco.common.Constants;
import es.inteco.common.ConstantsFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.observatorio.ModalityComparisonForm;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSectionEv;
import es.inteco.rastreador2.pdf.AnonymousResultExportPdfSections;

/**
 * The Class PDFUtils.
 */
public final class PDFUtils {
	/** The Constant ANY_CHARACTER_WORD_SPLITTER. */
	// Clase que se usará con urls para permitir que se parta el texto y haga retorno de carro en cualquier caracter.
	private static final SplitCharacter ANY_CHARACTER_WORD_SPLITTER = new SplitCharacter() {
		@Override
		public boolean isSplitCharacter(int i, int i1, int i2, char[] chars, PdfChunk[] pdfChunks) {
			return true;
		}
	};

	/**
	 * Instantiates a new PDF utils.
	 */
	private PDFUtils() {
	}

	/**
	 * Adds the cover page.
	 *
	 * @param document     the document
	 * @param titleText    the title text
	 * @param subtitleText the subtitle text
	 * @throws DocumentException the document exception
	 */
	public static void addCoverPage(final Document document, final String titleText, final String subtitleText) throws DocumentException {
		addCoverPage(document, titleText, subtitleText, null);
	}

	/**
	 * Adds the cover page.
	 *
	 * @param document     the document
	 * @param titleText    the title text
	 * @param subtitleText the subtitle text
	 * @param noticeText   the notice text
	 * @throws DocumentException the document exception
	 */
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

	/**
	 * Adds the new cover page.
	 *
	 * @param document     the document
	 * @param titleText    the title text
	 * @param subtitleText the subtitle text
	 * @param noticeText   the notice text
	 * @throws DocumentException the document exception
	 */
	public static void addNewCoverPage(final Document document, final String titleText1, final String titleText2, final String subtitleText, final String noticeText, final String noticeText2)
			throws DocumentException {
		final Paragraph title = new Paragraph(titleText1, ConstantsFont.DOCUMENT_TITLE_MP_FONT);
		title.setSpacingBefore(ConstantsFont.TITLE_LINE_SPACE);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		final Paragraph title2 = new Paragraph(titleText2, ConstantsFont.DOCUMENT_SUBTITLE_2_MP_FONT);
		title2.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE / 2);
		title2.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title2);
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
		if (noticeText2 != null && !noticeText2.isEmpty()) {
			final PdfPTable notice2 = new PdfPTable(1);
			notice2.setSpacingBefore(ConstantsFont.SUBTITLE_LINE_SPACE);
			notice2.addCell(PDFUtils.createTableCell(noticeText2, Constants.GRIS_MUY_CLARO, ConstantsFont.paragraphBoldFont, Element.ALIGN_JUSTIFIED, ConstantsFont.DEFAULT_PADDING, 80));
			document.add(notice2);
		}
	}

	/**
	 * Creates the chapter with title.
	 *
	 * @param title         the title
	 * @param pdfTocManager the pdf toc manager
	 * @param titleFont     the title font
	 * @return the chapter
	 */
	public static Chapter createChapterWithTitle(String title, PdfTocManager pdfTocManager, Font titleFont) {
		return createChapterWithTitle(title, pdfTocManager.getIndex(), pdfTocManager.addSection(), pdfTocManager.getNumChapter(), titleFont, true);
	}

	/**
	 * Creates the chapter with title.
	 *
	 * @param title         the title
	 * @param index         the index
	 * @param countSections the count sections
	 * @param numChapter    the num chapter
	 * @param titleFont     the title font
	 * @return the chapter
	 */
	public static Chapter createChapterWithTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont) {
		return createChapterWithTitle(title, index, countSections, numChapter, titleFont, true);
	}

	/**
	 * Creates the chapter with title.
	 *
	 * @param title         the title
	 * @param index         the index
	 * @param countSections the count sections
	 * @param numChapter    the num chapter
	 * @param titleFont     the title font
	 * @param upperCase     the upper case
	 * @return the chapter
	 */
	public static Chapter createChapterWithTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont, boolean upperCase) {
		final Chunk chunk = new Chunk(upperCase ? title.toUpperCase() : title);
		chunk.setLocalDestination(Constants.ANCLA_PDF + (countSections));
		final Paragraph paragraph = new Paragraph("", titleFont);
		paragraph.add(chunk);
		// Line
		Chunk CONNECT = new Chunk(new LineSeparator(0.5f, 95, BaseColor.WHITE, Element.ALIGN_CENTER, -.5f));
		paragraph.add(CONNECT);
		final Chapter chapter = new Chapter(paragraph, numChapter);
		if (index != null) {
			paragraph.add(index.create(" ", countSections + "@&" + title.toUpperCase()));
		}
		return chapter;
	}

	/**
	 * Creates the chapter with title.
	 *
	 * @param title         the title
	 * @param index         the index
	 * @param countSections the count sections
	 * @param numChapter    the num chapter
	 * @param titleFont     the title font
	 * @param upperCase     the upper case
	 * @param anchor        the anchor
	 * @return the chapter
	 */
	public static Chapter createChapterWithTitle(String title, IndexEvents index, int countSections, int numChapter, Font titleFont, boolean upperCase, final String anchor) {
		final Chunk chunk = new Chunk(upperCase ? title.toUpperCase() : title);
		chunk.setLocalDestination(Constants.ANCLA_PDF + (countSections));
		final Paragraph paragraph = new Paragraph("", titleFont);
		paragraph.add(chunk);
		// Si el capítulo requiere un anchor adicional al que se crea desde el índice (tabla contenidos) entonces se
		// crea un trozo de texto adicional (Chunk) con el caracter especial \u200B (ZWSP Zero White Space) para añadir
		// el ancla oculto sin que se añada nuevo texto. Si se intentase con la cadena vacía no se crea el ancla.
		final Chunk aditionalAnchor = new Chunk("\u200B", titleFont);
		aditionalAnchor.setLocalDestination(anchor);
		paragraph.add(aditionalAnchor);
		// Line
		Chunk CONNECT = new Chunk(new LineSeparator(0.5f, 95, BaseColor.WHITE, Element.ALIGN_CENTER, -.5f));
		paragraph.add(CONNECT);
		final Chapter chapter = new Chapter(paragraph, numChapter);
		if (index != null) {
			paragraph.add(index.create(" ", countSections + "@&" + title.toUpperCase()));
		}
		return chapter;
	}

	/**
	 * Creates the section.
	 *
	 * @param title         the title
	 * @param index         the index
	 * @param levelFont     the level font
	 * @param section       the section
	 * @param countSections the count sections
	 * @param level         the level
	 * @return the section
	 */
	public static Section createSection(String title, IndexEvents index, Font levelFont, Section section, int countSections, int level) {
		return createSection(title, index, levelFont, section, countSections, level, null);
	}

	/**
	 * Creates the section.
	 *
	 * @param title         the title
	 * @param index         the index
	 * @param levelFont     the level font
	 * @param section       the section
	 * @param countSections the count sections
	 * @param level         the level
	 * @param anchor        the anchor
	 * @return the section
	 */
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

	/**
	 * Adds the paragraph.
	 *
	 * @param text    the text
	 * @param font    the font
	 * @param section the section
	 */
	public static void addParagraph(String text, Font font, Section section) {
		Paragraph p = new Paragraph(text, font);
		p.setSpacingBefore(ConstantsFont.LINE_SPACE);
		p.setAlignment(Paragraph.ALIGN_JUSTIFIED);
		section.add(p);
	}

	/**
	 * Adds the paragraph.
	 *
	 * @param text    the text
	 * @param font    the font
	 * @param section the section
	 * @param align   the align
	 */
	public static void addParagraph(String text, Font font, Section section, int align) {
		addParagraph(text, font, section, align, true, false);
	}

	/**
	 * Adds the paragraph.
	 *
	 * @param text        the text
	 * @param font        the font
	 * @param section     the section
	 * @param align       the align
	 * @param spaceBefore the space before
	 * @param spaceAfter  the space after
	 */
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

	/**
	 * Adds the code.
	 *
	 * @param text    the text
	 * @param section the section
	 */
	public static void addCode(final String text, final Section section) {
		float[] widths = { 100f };
		final PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100);
		table.setKeepTogether(false);
		for (String linea : text.split(System.lineSeparator())) {
			if (!linea.trim().isEmpty()) {
				final PdfPCell labelCell = new PdfPCell();
				labelCell.setBackgroundColor(new BaseColor(255, 244, 223));
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

	/**
	 * Adds the paragraph rationale.
	 *
	 * @param text    the text
	 * @param section the section
	 */
	public static void addParagraphRationale(final java.util.List<String> text, final Section section) {
		final float[] widths = { 95f };
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
		labelCell.setBackgroundColor(Constants.BC_GRIS_MUY_CLARO);
		labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		labelCell.setPadding(ConstantsFont.DEFAULT_PADDING);
		table.addCell(labelCell);
		section.add(table);
	}

	/**
	 * Creates the image text paragraph.
	 *
	 * @param image the image
	 * @param text  the text
	 * @param font  the font
	 * @return the paragraph
	 */
	public static Paragraph createImageTextParagraph(Image image, String text, Font font) {
		Paragraph p = new Paragraph();
		p.add(new Chunk(image, 0, 0));
		if (text != null) {
			p.add(new Chunk(text, font));
		}
		p.setAlignment(Element.ALIGN_CENTER);
		return p;
	}

	/**
	 * Creates the image paragraph with diferent font.
	 *
	 * @param image     the image
	 * @param text      the text
	 * @param font      the font
	 * @param text2     the text 2
	 * @param font2     the font 2
	 * @param alignment the alignment
	 * @return the paragraph
	 */
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

	/**
	 * Creates the paragraph anchor.
	 *
	 * @param text            the text
	 * @param specialChunkMap the special chunk map
	 * @param font            the font
	 * @return the paragraph
	 */
	public static Paragraph createParagraphAnchor(String text, Map<Integer, SpecialChunk> specialChunkMap, Font font) {
		return createParagraphAnchor(text, specialChunkMap, font, true);
	}

	/**
	 * Crea un párrafo (Paragraph) de documento PDF a partir de un texto que contiene las marcas [anchor1]..[anchorX] en el que se sustituyen las marcas por el valor equivalente que se suministra.
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
			// Is anchor
			if (specialChunkEntry.getValue().getAnchor() != null && !StringUtils.isEmpty(specialChunkEntry.getValue().getAnchor())) {
				indexOf = text.indexOf("[anchor" + iAnchor + "]");
				if (indexOf >= 0) {
					chunk = new Chunk(text.substring(init, indexOf), font);
					p.add(chunk);
				}
				p = createAnchor(specialChunkEntry.getKey(), specialChunkMap, p);
				init = text.indexOf("[anchor" + iAnchor + "]") + 8 + String.valueOf(iAnchor).length();
				iAnchor++;
			} else {// Is bold
				indexOf = text.indexOf("{" + iFormat + "}");
				if (indexOf >= 0) {
					chunk = new Chunk(text.substring(init, indexOf), font);
					p.add(chunk);
				}
				p = createSpecialFormatText(specialChunkEntry.getKey(), specialChunkMap, p);
				init = text.indexOf("{" + iFormat + "}") + 2 + String.valueOf(iFormat).length();
				iFormat++;
			}
			/*
			 * if ((text.indexOf("[anchor" + iAnchor + "]") > 0) && (text.indexOf("[anchor" + iAnchor + "]") > init)) { indexOf = text.indexOf("[anchor" + iAnchor + "]"); } if ((text.indexOf("{" +
			 * iFormat + "}") > 0) && ((indexOf != 0 && text.indexOf("{" + iFormat + "}") < indexOf) || indexOf == 0) && (text.indexOf("{" + iFormat + "}") > init)) { indexOf = text.indexOf("{" +
			 * iFormat + "}"); } if (indexOf != 0) { chunk = new Chunk(text.substring(init, indexOf), font); p.add(chunk); } if (specialChunkEntry.getValue().getAnchor() != null &&
			 * !StringUtils.isEmpty(specialChunkEntry.getValue().getAnchor())) { p = createAnchor(specialChunkEntry.getKey(), specialChunkMap, p); init = text.indexOf("[anchor" + iAnchor + "]") + 8 +
			 * String.valueOf(iAnchor).length(); iAnchor++; } else { p = createSpecialFormatText(specialChunkEntry.getKey(), specialChunkMap, p); init = text.indexOf("{" + iFormat + "}") + 2 +
			 * String.valueOf(iFormat).length(); iFormat++; }
			 */
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

	/**
	 * Creates the paragraph anchor.
	 *
	 * @param text            the text
	 * @param specialChunkMap the special chunk map
	 * @param font            the font
	 * @param spaceBefore     the space before
	 * @param alignment       the alignment
	 * @return the paragraph
	 */
	public static Paragraph createParagraphAnchor(final String text, final Map<Integer, SpecialChunk> specialChunkMap, final Font font, boolean spaceBefore, int alignment) {
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
		p.setAlignment(alignment);
		return p;
	}

	/**
	 * Creates the special format text.
	 *
	 * @param anchor    the anchor
	 * @param anchorMap the anchor map
	 * @param p         the p
	 * @return the paragraph
	 */
	private static Paragraph createSpecialFormatText(Integer anchor, Map<Integer, SpecialChunk> anchorMap, Paragraph p) {
		Chunk anchorChunk = new Chunk(anchorMap.get(anchor).getText(), anchorMap.get(anchor).getFont());
		p.add(anchorChunk);
		return p;
	}

	/**
	 * Creates the anchor.
	 *
	 * @param anchor    the anchor
	 * @param anchorMap the anchor map
	 * @param p         the p
	 * @return the paragraph
	 */
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

	/**
	 * Creates the paragraph with diferent format word.
	 *
	 * @param text        the text
	 * @param boldWords   the bold words
	 * @param fontB       the font B
	 * @param font        the font
	 * @param spaceBefore the space before
	 * @return the paragraph
	 */
	public static Paragraph createParagraphWithDiferentFormatWord(String text, java.util.List<String> boldWords, Font fontB, Font font, boolean spaceBefore) {
		return createParagraphWithDiferentFormatWord(text, boldWords, fontB, font, spaceBefore, Paragraph.ALIGN_JUSTIFIED);
	}

	/**
	 * Creates the phrase.
	 *
	 * @param text the text
	 * @param font the font
	 * @return the phrase
	 */
	public static Phrase createPhrase(String text, Font font) {
		return new Phrase(text, font);
	}

	/**
	 * Creates the phrase link.
	 *
	 * @param text the text
	 * @param link the link
	 * @param font the font
	 * @return the phrase
	 */
	public static Phrase createPhraseLink(String text, String link, Font font) {
		return new Phrase(new Chunk(text, font).setAnchor(link));
	}

	/**
	 * Creates the paragraph with diferent format word.
	 *
	 * @param text        the text
	 * @param boldWords   the bold words
	 * @param fontB       the font B
	 * @param font        the font
	 * @param spaceBefore the space before
	 * @param alignment   the alignment
	 * @return the paragraph
	 */
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

	/**
	 * Adds the link paragraph.
	 *
	 * @param text the text
	 * @param link the link
	 * @param font the font
	 * @return the paragraph
	 */
	public static Paragraph addLinkParagraph(String text, String link, Font font) {
		return new Paragraph(new Chunk(text, font).setAnchor(link));
	}

	/**
	 * Adds the mix format list item.
	 *
	 * @param text       the text
	 * @param words      the words
	 * @param fontB      the font B
	 * @param font       the font
	 * @param spaceAfter the space after
	 * @return the list item
	 */
	public static ListItem addMixFormatListItem(String text, java.util.List<String> words, Font fontB, Font font, boolean spaceAfter) {
		Paragraph p = createParagraphWithDiferentFormatWord(text, words, fontB, font, spaceAfter);
		return new ListItem(p);
	}

	/**
	 * Adds the list item.
	 *
	 * @param text        the text
	 * @param list        the list
	 * @param font        the font
	 * @param spaceBefore the space before
	 * @param withSymbol  the with symbol
	 */
	public static void addListItem(String text, List list, Font font, boolean spaceBefore, boolean withSymbol) {
		addListItem(text, list, font, spaceBefore, withSymbol, Element.ALIGN_LEFT);
	}

	/**
	 * Adds the list item.
	 *
	 * @param text the text
	 * @param list the list
	 * @param font the font
	 */
	public static void addListItem(String text, List list, Font font) {
		addListItem(text, list, font, true, true, Element.ALIGN_LEFT);
	}

	/**
	 * Adds the list item.
	 *
	 * @param text        the text
	 * @param list        the list
	 * @param font        the font
	 * @param spaceBefore the space before
	 * @param withSymbol  the with symbol
	 * @param align       the align
	 */
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

	/**
	 * Adds the list item.
	 *
	 * @param p           the p
	 * @param list        the list
	 * @param font        the font
	 * @param spaceBefore the space before
	 * @param withSymbol  the with symbol
	 * @param align       the align
	 */
	public static void addListItem(Paragraph p, List list, Font font, boolean spaceBefore, boolean withSymbol, int align) {
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

	/**
	 * Creates the list item.
	 *
	 * @param text        the text
	 * @param font        the font
	 * @param symbol      the symbol
	 * @param spaceBefore the space before
	 * @return the list item
	 */
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

	/**
	 * Builds the leyenda list item.
	 *
	 * @param message the message
	 * @param symbol  the symbol
	 * @return the list item
	 */
	public static ListItem buildLeyendaListItem(final String message, final String symbol) {
		return createListItem(message, ConstantsFont.MORE_INFO_FONT, new Chunk(symbol + " "), false);
	}

	/**
	 * Builds the leyenda list item bold.
	 *
	 * @param message the message
	 * @param symbol  the symbol
	 * @return the list item
	 */
	public static ListItem buildLeyendaListItemBold(final String message, final String symbol) {
		return createListItem(message, ConstantsFont.MORE_INFO_FONT_BOLD, new Chunk(symbol + " "), false);
	}

	/**
	 * Creates the image.
	 *
	 * @param path the path
	 * @param alt  the alt
	 * @return the image
	 */
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

	/**
	 * Creates the linked table cell.
	 *
	 * @param text                the text
	 * @param url                 the url
	 * @param backgroundColor     the background color
	 * @param horizontalAlignment the horizontal alignment
	 * @param padding             the padding
	 * @return the pdf P cell
	 */
	public static PdfPCell createLinkedTableCell(final String text, final String url, final Color backgroundColor, int horizontalAlignment, int padding) {
		return createLinkedTableCell(text, url, backgroundColor, horizontalAlignment, padding, -1);
	}

	/**
	 * Creates the linked table cell.
	 *
	 * @param text                the text
	 * @param url                 the url
	 * @param backgroundColor     the background color
	 * @param horizontalAlignment the horizontal alignment
	 * @param padding             the padding
	 * @param height              the height
	 * @return the pdf P cell
	 */
	public static PdfPCell createLinkedTableCell(final String text, final String url, final Color backgroundColor, int horizontalAlignment, int padding, int height) {
		final Chunk chunk = new Chunk(text, ConstantsFont.LINK_FONT);
		chunk.setAnchor(url);
		chunk.setSplitCharacter(ANY_CHARACTER_WORD_SPLITTER);
		final PdfPCell labelCell = new PdfPCell(new Paragraph(chunk));
		// PENDING
		// labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBackgroundColor(new BaseColor(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));
		labelCell.setHorizontalAlignment(horizontalAlignment);
		labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		labelCell.setPaddingLeft(padding);
		if (height > 0) {
			labelCell.setFixedHeight(height);
		}
		return labelCell;
	}

	/**
	 * Creates the table cell.
	 *
	 * @param image               the image
	 * @param text                the text
	 * @param backgroundColor     the background color
	 * @param font                the font
	 * @param horizontalAlignment the horizontal alignment
	 * @param padding             the padding
	 * @param height              the height
	 * @return the pdf P cell
	 */
	public static PdfPCell createTableCell(final Image image, final String text, final Color backgroundColor, final Font font, int horizontalAlignment, int padding, int height) {
		image.scalePercent(40);
		final PdfPCell labelCell = new PdfPCell();
		final Paragraph imageParagraph = PDFUtils.createImageTextParagraph(image, " " + text, font);
		imageParagraph.setLeading(image.getHeight());
		labelCell.addElement(imageParagraph);
		// PENDING
		// labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBackgroundColor(new BaseColor(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));
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

	/**
	 * Creates the table cell.
	 *
	 * @param text            the text
	 * @param backgroundColor the background color
	 * @param font            the font
	 * @param align           the align
	 * @param padding         the padding
	 * @return the pdf P cell
	 */
	public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int padding) {
		return createTableCell(text, backgroundColor, font, align, padding, 17f);
	}

	/**
	 * Creates the table cell.
	 *
	 * @param text            the text
	 * @param backgroundColor the background color
	 * @param font            the font
	 * @param align           the align
	 * @param padding         the padding
	 * @param height          the height
	 * @return the pdf P cell
	 */
	public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int padding, final float height) {
		final PdfPCell labelCell = new PdfPCell(new Paragraph(text, font));
		// PENDING
		// labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBackgroundColor(new BaseColor(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));
		labelCell.setHorizontalAlignment(align);
		labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		labelCell.setPadding(padding);
		if (height != -1) {
			labelCell.setFixedHeight(height);
		}
		return labelCell;
	}

	/**
	 * Creates the table cell.
	 *
	 * @param text            the text
	 * @param backgroundColor the background color
	 * @param font            the font
	 * @param align           the align
	 * @param margin          the margin
	 * @param anchorId        the anchor id
	 * @return the pdf P cell
	 */
	public static PdfPCell createTableCell(final String text, final Color backgroundColor, final Font font, final int align, final int margin, final String anchorId) {
		final Chunk chunk = new Chunk(text, font);
		chunk.setLocalGoto(anchorId);
		final PdfPCell labelCell = new PdfPCell(new Paragraph(chunk));
		// PENDING
		// labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBackgroundColor(new BaseColor(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));
		labelCell.setHorizontalAlignment(align);
		labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		labelCell.setPaddingLeft(margin);
		labelCell.setFixedHeight(17f);
		return labelCell;
	}

	/**
	 * Creates the list table cell.
	 *
	 * @param list            the list
	 * @param backgroundColor the background color
	 * @param horizontalAlign the horizontal align
	 * @param margin          the margin
	 * @return the pdf P cell
	 */
	public static PdfPCell createListTableCell(final List list, final Color backgroundColor, final int horizontalAlign, final int margin) {
		return createListTableCell(list, backgroundColor, horizontalAlign, Element.ALIGN_MIDDLE, margin);
	}

	/**
	 * Creates the list table cell.
	 *
	 * @param list            the list
	 * @param backgroundColor the background color
	 * @param horizontalAlign the horizontal align
	 * @param verticalAlign   the vertical align
	 * @param margin          the margin
	 * @return the pdf P cell
	 */
	public static PdfPCell createListTableCell(final List list, final Color backgroundColor, final int horizontalAlign, final int verticalAlign, final int margin) {
		final PdfPCell labelCell = new PdfPCell();
		labelCell.addElement(list);
		// PENDING
		// labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBackgroundColor(new BaseColor(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue()));
		labelCell.setHorizontalAlignment(horizontalAlign);
		labelCell.setVerticalAlignment(verticalAlign);
		labelCell.setPaddingLeft(margin);
		return labelCell;
	}

	/**
	 * Creates the col span table cell.
	 *
	 * @param text            the text
	 * @param backgroundColor the background color
	 * @param font            the font
	 * @param colSpan         the col span
	 * @param align           the align
	 * @return the pdf P cell
	 */
	public static PdfPCell createColSpanTableCell(final String text, final Color backgroundColor, final Font font, final int colSpan, final int align) {
		final int margin = (align == Element.ALIGN_LEFT) ? 5 : 0;
		final PdfPCell labelCell = createTableCell(text, backgroundColor, font, align, margin);
		labelCell.setColspan(colSpan);
		return labelCell;
	}

	/**
	 * Creates the result table.
	 *
	 * @param results the results
	 * @param headers the headers
	 * @return the pdf P table
	 */
	public static PdfPTable createResultTable(java.util.List<LabelValueBean> results, java.util.List<String> headers) {
		float[] widths = { 50f, 50f };
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

	/**
	 * Creates the title table.
	 *
	 * @param text    the text
	 * @param section the section
	 * @param scaleX  the scale X
	 * @throws BadElementException the bad element exception
	 * @throws IOException         Signals that an I/O exception has occurred.
	 */
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

	/**
	 * Creates the table mod.
	 *
	 * @param resources the resources
	 * @param result    the result
	 * @return the pdf P table
	 */
	public static PdfPTable createTableMod(final MessageResources resources, final java.util.List<ModalityComparisonForm> result) {
		final float[] widths = { 50f, 25f, 25f };
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

	/**
	 * Replace accent.
	 *
	 * @param phrase the phrase
	 * @return the string
	 */
	public static String replaceAccent(final String phrase) {
		return phrase.replace('á', 'a').replace('à', 'a').replace('â', 'a').replace('ã', 'a').replace('ä', 'a').replace('å', 'a').replace('é', 'e').replace('è', 'e').replace('ê', 'e')
				.replace('ë', 'e').replace('í', 'i').replace('ì', 'i').replace('ï', 'i').replace('î', 'i').replace('ó', 'o').replace('ò', 'o').replace('ô', 'o').replace('ö', 'o').replace('õ', 'o')
				.replace('ú', 'u').replace('ù', 'u').replace('ü', 'u').replace('û', 'u').replace('ý', 'y').replace('ÿ', 'y').replace('ñ', 'n').replace('ç', 'c').replace("ª", "a.").replace("º", "o.");
	}

	/**
	 * Format seed name.
	 *
	 * @param seedName the seed name
	 * @return the string
	 */
	public static String formatSeedName(final String seedName) {
		if (seedName != null) {
			return replaceAccent(seedName.trim().toLowerCase()).replaceAll("[\\s,./\\\\]+", "_");
		}
		return null;
	}

	/**
	 * Adds the image to section.
	 *
	 * @param section   the section
	 * @param imagePath the image path
	 * @param imageAlt  the image alt
	 * @param scale     the scale
	 */
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
