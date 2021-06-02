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
package es.inteco.common;

import java.awt.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.pdf.IntavExport;

/**
 * The Class ConstantsFont.
 */
public final class ConstantsFont {
	/** The Constant PROBLEM_COLOR. */
	private static final Color PROBLEM_COLOR = new Color(235, 18, 13);
	/** The Constant WARNING_COLOR. */
	private static final Color WARNING_COLOR = new Color(245, 164, 55);
	/** The Constant CANNOTTELL_COLOR. */
	private static final Color CANNOTTELL_COLOR = new Color(39, 102, 196);
	/** The Constant LINK_COLOR. */
	private static final Color LINK_COLOR = new Color(0, 0, 255);
	/** The Constant BC_PROBLEM_COLOR. */
	private static final BaseColor BC_PROBLEM_COLOR = new BaseColor(235, 18, 13);
	/** The Constant BC_WARNING_COLOR. */
	private static final BaseColor BC_WARNING_COLOR = new BaseColor(245, 164, 55);
	/** The Constant BC_CANNOTTELL_COLOR. */
	private static final BaseColor BC_CANNOTTELL_COLOR = new BaseColor(39, 102, 196);
	/** The Constant BC_LINK_COLOR. */
	private static final BaseColor BC_LINK_COLOR = new BaseColor(0, 0, 255);
	/** The arial. */
	private static BaseFont arial;
	/** The monospaced. */
	private static BaseFont monospaced;
	static {
		try {
			PropertiesManager pmgr = new PropertiesManager();
			arial = BaseFont.createFont(pmgr.getValue("pdf.properties", "path.pdf.font"), BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			monospaced = BaseFont.createFont(pmgr.getValue("pdf.properties", "path.pdf.font.monospaced"), BaseFont.CP1252, BaseFont.EMBEDDED);
		} catch (Exception e) {
			Logger.putLog("Excepción BaseFont arial: ", IntavExport.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Instantiates a new constants font.
	 */
	private ConstantsFont() {
	}

	/** The Constant CHAPTER_TITLE_MP_FONT. */
	// NEW ITEXT FONTS
	public static final Font CHAPTER_TITLE_MP_FONT = new Font(arial, 14, Font.UNDERLINE, Constants.BC_VERDE_O_MP);
	/** The Constant CHAPTER_TITLE_MP_FONT_2_L. */
	public static final Font CHAPTER_TITLE_MP_FONT_2_L = new Font(arial, 13, Font.NORMAL, Constants.BC_VERDE_O_MP);
	/** The Constant CHAPTER_TITLE_MP_FONT_3_L. */
	public static final Font CHAPTER_TITLE_MP_FONT_3_L = new Font(arial, 12, Font.NORMAL, Constants.BC_VERDE_O_MP);
	/** The Constant DOCUMENT_TITLE_MP_FONT. */
	public static final Font DOCUMENT_TITLE_MP_FONT = new Font(arial, 34, Font.BOLD, Constants.BC_VERDE_O_MP);
	/** The Constant DOCUMENT_SUBTITLE_MP_FONT. */
	public static final Font DOCUMENT_SUBTITLE_MP_FONT = new Font(arial, 20, Font.BOLD, Constants.BC_VERDE_O_MP);
	/** The Constant DOCUMENT_SUBTITLE_2_MP_FONT. */
	public static final Font DOCUMENT_SUBTITLE_2_MP_FONT = new Font(arial, 18, Font.BOLD, Constants.BC_VERDE_O_MP);
	/** The Constant DOCUMENT_NOTICE_MP_FONT. */
	public static final Font DOCUMENT_NOTICE_MP_FONT = new Font(arial, 20, Font.NORMAL, BaseColor.BLACK);
	/** The Constant PARAGRAPH_TITLE_TABLE_FONT. */
	public static final Font PARAGRAPH_TITLE_TABLE_FONT = new Font(arial, 10, Font.BOLD, Constants.BC_ROJO_INTECO);
	/** The Constant levelTitleMPFont. */
	public static final Font levelTitleMPFont = new Font(arial, 14, Font.BOLD, Constants.BC_VERDE_C_MP);
	/** The Constant CHAPTER_TITLE_FONT. */
	public static final Font CHAPTER_TITLE_FONT = new Font(arial, 14, Font.UNDERLINE, Constants.BC_ROJO_INTECO);
	/** The Constant chapterTitleFont2L. */
	public static final Font chapterTitleFont2L = new Font(arial, 13, Font.NORMAL, Constants.BC_ROJO_INTECO);
	/** The Constant chapterTitleFont3L. */
	public static final Font chapterTitleFont3L = new Font(arial, 12, Font.NORMAL, Constants.BC_ROJO_INTECO);
	/** The Constant PARAGRAPH. */
	public static final Font PARAGRAPH = new Font(arial, 10, Font.NORMAL);
	/** The Constant PARAGRAPH_ANCHOR_FONT. */
	public static final Font PARAGRAPH_ANCHOR_FONT = new Font(arial, 10, Font.UNDERLINE, BaseColor.BLUE);
	/** The Constant paragraphRedFont. */
	public static final Font paragraphRedFont = new Font(arial, 10, Font.NORMAL, Constants.BC_ROJO_INTECO);
	/** The Constant paragraphBoldFont. */
	public static final Font paragraphBoldFont = new Font(arial, 10, Font.BOLD);
	/** The Constant paragraphBoldTitleFont. */
	public static final Font paragraphBoldTitleFont = new Font(arial, 12, Font.BOLD);
	/** The Constant paragraphTitleFont. */
	public static final Font paragraphTitleFont = new Font(arial, 12, Font.NORMAL);
	/** The Constant paragraphUnderlinedFont. */
	public static final Font paragraphUnderlinedFont = new Font(arial, 10, Font.UNDERLINE);
	/** The Constant listSymbol. */
	public static final Font listSymbol = new Font(arial, 33, Font.NORMAL, BaseColor.BLACK);
	/** The Constant SUMMARY_TITLE_FONT. */
	public static final Font SUMMARY_TITLE_FONT = new Font(arial, 14, Font.BOLD, BaseColor.BLACK);
	/** The Constant SUMMARY_FONT. */
	public static final Font SUMMARY_FONT = new Font(new Font(arial, 12, Font.BOLD));
	/** The Constant SUMMARY_FONT_PROBLEM. */
	public static final Font SUMMARY_FONT_PROBLEM = new Font(new Font(arial, 12, Font.BOLD, BC_PROBLEM_COLOR));
	/** The Constant SUMMARY_FONT_WARNING. */
	public static final Font SUMMARY_FONT_WARNING = new Font(new Font(arial, 12, Font.BOLD, BC_WARNING_COLOR));
	/** The Constant SUMMARY_FONT_CANNOTTELL. */
	public static final Font SUMMARY_FONT_CANNOTTELL = new Font(new Font(arial, 12, Font.BOLD, BC_CANNOTTELL_COLOR));
	/** The Constant PROBLEM_FONT. */
	public static final Font PROBLEM_FONT = new Font(new Font(arial, 10, Font.BOLD, BC_PROBLEM_COLOR));
	/** The Constant WARNING_FONT. */
	public static final Font WARNING_FONT = new Font(new Font(arial, 10, Font.BOLD, Constants.BC_NARANJA_MP));// WARNING_COLOR));
	/** The Constant CANNOTTELL_FONT. */
	public static final Font CANNOTTELL_FONT = new Font(new Font(arial, 10, Font.BOLD, BC_CANNOTTELL_COLOR));
	/** The Constant SUMMARY_SCORE_FONT. */
	public static final Font SUMMARY_SCORE_FONT = new Font(new Font(arial, 10, Font.NORMAL, BaseColor.BLACK));
	/** The Constant SUMMARY_SCORE_BOLD_FONT. */
	public static final Font SUMMARY_SCORE_BOLD_FONT = new Font(new Font(arial, 10, Font.BOLD, BaseColor.BLACK));
	/** The Constant documentTitleFont. */
	public static final Font documentTitleFont = new Font(arial, 34, Font.BOLD, Constants.BC_ROJO_INTECO);
	/** The Constant documentSubtitleFont. */
	public static final Font documentSubtitleFont = new Font(arial, 30, Font.BOLD, new BaseColor(128, 0, 0));
	/** The Constant sectionFont. */
	public static final Font sectionFont = new Font(arial, 16, Font.BOLD, Constants.BC_ROJO_INTECO);
	/** The Constant descriptionFont. */
	public static final Font descriptionFont = new Font(arial, 10, Font.NORMAL);
	/** The Constant strongDescriptionFont. */
	public static final Font strongDescriptionFont = new Font(arial, 10, Font.BOLD);
	/** The Constant descriptionFontGreen. */
	public static final Font descriptionFontGreen = new Font(arial, 10, Font.NORMAL, Constants.BC_VERDE_O_MP);
	/** The Constant descriptionFontRed. */
	public static final Font descriptionFontRed = new Font(arial, 10, Font.BOLD, Constants.BC_ROJO_INTECO);
	/** The Constant guidelineDescFont. */
	public static final Font guidelineDescFont = new Font(arial, 12, Font.NORMAL, Constants.BC_ROJO_INTECO);
	/** The Constant guidelineDescMPFont. */
	public static final Font guidelineDescMPFont = new Font(arial, 12, Font.NORMAL, Constants.BC_ROJO_INTECO);
	/** The Constant labelCellFont. */
	public static final Font labelCellFont = new Font(arial, 10, Font.NORMAL, BaseColor.WHITE);
	/** The Constant labelHeaderCellFont. */
	public static final Font labelHeaderCellFont = new Font(arial, 10, Font.BOLD, BaseColor.WHITE);
	/** The Constant codeCellFont. */
	public static final Font codeCellFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL);
	/** The Constant noteCellFont. */
	public static final Font noteCellFont = new Font(arial, 10, Font.NORMAL);
	/** The Constant noteCellFont7. */
	// Fuente más pequeña para las tablas de metodología
	public static final Font noteCellFont7 = new Font(arial, 7, Font.NORMAL);
	/** The Constant strongNoteCellFont. */
	public static final Font strongNoteCellFont = new Font(arial, 10, Font.BOLD);
	/** The Constant noteCellGreenFont. */
	public static final Font noteCellGreenFont = new Font(arial, 8, Font.NORMAL, BaseColor.GREEN);
	/** The Constant noteCellRedFont. */
	public static final Font noteCellRedFont = new Font(arial, 8, Font.NORMAL, BaseColor.RED);
	/** The Constant priorityCellFont. */
	public static final Font priorityCellFont = new Font(arial, 8, Font.BOLD);
	/** The Constant NOTE_ANCHOR_CELL_FONT. */
	public static final Font NOTE_ANCHOR_CELL_FONT = new Font(arial, 8, Font.UNDERLINE, BC_LINK_COLOR);
	/** The Constant INDEX_ITEMS. */
	public static final Font INDEX_ITEMS = new Font(arial, 10, Font.NORMAL);
	/** The Constant SCORE_FONT. */
	public static final Font SCORE_FONT = new Font(arial, 14, Font.NORMAL, BaseColor.BLACK);
	/** The Constant SCORE_BOLD_FONT. */
	public static final Font SCORE_BOLD_FONT = new Font(arial, 14, Font.BOLD, BaseColor.BLACK);
	/** The Constant MORE_INFO_FONT. */
	public static final Font MORE_INFO_FONT = new Font(arial, 8, Font.ITALIC, BaseColor.BLACK);
	/** The Constant MORE_INFO_FONT_BOLD. */
	public static final Font MORE_INFO_FONT_BOLD = new Font(arial, 8, Font.BOLDITALIC, BaseColor.BLACK);
	/** The Constant ANCHOR_FONT. */
	public static final Font ANCHOR_FONT = new Font(arial, 10, Font.ITALIC, BC_LINK_COLOR);
	/** The Constant ANCHOR_FONT_NO_LINK. */
	public static final Font ANCHOR_FONT_NO_LINK = new Font(arial, 10, Font.ITALIC, BaseColor.BLACK);
	/** The Constant LINK_FONT. */
	public static final Font LINK_FONT = new Font(monospaced, 10, Font.ITALIC, BC_LINK_COLOR);
	/** The Constant LINE_SPACE. */
	public static final int LINE_SPACE = 15;
	/** The Constant HALF_LINE_SPACE. */
	public static final int HALF_LINE_SPACE = 8;
	/** The Constant THIRD_LINE_SPACE. */
	public static final int THIRD_LINE_SPACE = 5;
	/** The Constant TITLE_LINE_SPACE. */
	public static final int TITLE_LINE_SPACE = 100;
	/** The Constant SUBTITLE_LINE_SPACE. */
	public static final int SUBTITLE_LINE_SPACE = 50;
	/** The Constant IDENTATION_LEFT_SPACE. */
	public static final int IDENTATION_LEFT_SPACE = 20;
	/** The Constant DEFAULT_PADDING. */
	public static final int DEFAULT_PADDING = 4;
	/** The Constant LIST_SYMBOL_1. */
	public static final Chunk LIST_SYMBOL_1 = new Chunk("\u2022", FontFactory.getFont(FontFactory.HELVETICA, 15));
	/** The Constant LIST_SYMBOL_2. */
	public static final Chunk LIST_SYMBOL_2 = new Chunk("\u2022", FontFactory.getFont(FontFactory.HELVETICA, 12));
}
