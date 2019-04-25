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

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.rastreador2.pdf.IntavExport;

import java.awt.*;


public final class ConstantsFont {

    private static final Color PROBLEM_COLOR = new Color(235, 18, 13);
    private static final Color WARNING_COLOR = new Color(245, 164, 55);
    private static final Color CANNOTTELL_COLOR = new Color(39, 102, 196);
    private static final Color LINK_COLOR = new Color(0, 0, 255);

    private static BaseFont arial;
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

    private ConstantsFont() {
    }

    //FUENTES PDF

    public static final Font CHAPTER_TITLE_MP_FONT = new Font(arial, 14, Font.UNDERLINE, Constants.VERDE_O_MP);
    public static final Font CHAPTER_TITLE_MP_FONT_2_L = new Font(arial, 13, Font.NORMAL, Constants.VERDE_O_MP);
    public static final Font CHAPTER_TITLE_MP_FONT_3_L = new Font(arial, 12, Font.NORMAL, Constants.VERDE_O_MP);
    public static final Font DOCUMENT_TITLE_MP_FONT = new Font(arial, 34, Font.BOLD, Constants.VERDE_O_MP);
    public static final Font DOCUMENT_SUBTITLE_MP_FONT = new Font(arial, 20, Font.BOLD, Constants.VERDE_O_MP);
    public static final Font DOCUMENT_NOTICE_MP_FONT = new Font(arial, 20, Font.NORMAL, Color.BLACK);
    public static final Font PARAGRAPH_TITLE_TABLE_FONT = new Font(arial, 10, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font levelTitleMPFont = new Font(arial, 14, Font.BOLD, Constants.VERDE_C_MP);

    public static final Font CHAPTER_TITLE_FONT = new Font(arial, 14, Font.UNDERLINE, Constants.ROJO_INTECO);
    public static final Font chapterTitleFont2L = new Font(arial, 13, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font chapterTitleFont3L = new Font(arial, 12, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font PARAGRAPH = new Font(arial, 10, Font.NORMAL);
    public static final Font PARAGRAPH_ANCHOR_FONT = new Font(arial, 10, Font.UNDERLINE, Color.BLUE);
    public static final Font paragraphRedFont = new Font(arial, 10, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font paragraphBoldFont = new Font(arial, 10, Font.BOLD);
    public static final Font paragraphBoldTitleFont = new Font(arial, 12, Font.BOLD);
    public static final Font paragraphTitleFont = new Font(arial, 12, Font.NORMAL);
    public static final Font paragraphUnderlinedFont = new Font(arial, 10, Font.UNDERLINE);
    public static final Font listSymbol = new Font(arial, 33, Font.NORMAL, Color.BLACK);

    public static final Font SUMMARY_TITLE_FONT = new Font(arial, 14, Font.BOLD, Color.black);
    public static final Font SUMMARY_FONT = new Font(new Font(arial, 12, Font.BOLD));
    public static final Font SUMMARY_FONT_PROBLEM = new Font(new Font(arial, 12, Font.BOLD, PROBLEM_COLOR));
    public static final Font SUMMARY_FONT_WARNING = new Font(new Font(arial, 12, Font.BOLD, WARNING_COLOR));
    public static final Font SUMMARY_FONT_CANNOTTELL = new Font(new Font(arial, 12, Font.BOLD, CANNOTTELL_COLOR));
    public static final Font PROBLEM_FONT = new Font(new Font(arial, 10, Font.BOLD, PROBLEM_COLOR));
    public static final Font WARNING_FONT = new Font(new Font(arial, 10, Font.BOLD, Constants.NARANJA_MP));//WARNING_COLOR));
    public static final Font CANNOTTELL_FONT = new Font(new Font(arial, 10, Font.BOLD, CANNOTTELL_COLOR));
    public static final Font SUMMARY_SCORE_FONT = new Font(new Font(arial, 10, Font.NORMAL, Color.BLACK));
    public static final Font SUMMARY_SCORE_BOLD_FONT = new Font(new Font(arial, 10, Font.BOLD, Color.BLACK));
    public static final Font documentTitleFont = new Font(arial, 34, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font documentSubtitleFont = new Font(arial, 30, Font.BOLD, new Color(128, 0, 0));
    public static final Font sectionFont = new Font(arial, 16, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font descriptionFont = new Font(arial, 10, Font.NORMAL);
    public static final Font strongDescriptionFont = new Font(arial, 10, Font.BOLD);
    public static final Font descriptionFontGreen = new Font(arial, 10, Font.NORMAL, Constants.VERDE_O_MP);
    public static final Font descriptionFontRed = new Font(arial, 10, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font guidelineDescFont = new Font(arial, 12, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font guidelineDescMPFont = new Font(arial, 12, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font labelCellFont = new Font(arial, 10, Font.NORMAL, Color.WHITE);
    public static final Font labelHeaderCellFont = new Font(arial, 10, Font.BOLD, Color.WHITE);
    public static final Font codeCellFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL);
    public static final Font noteCellFont = new Font(arial, 10, Font.NORMAL);
    
    //Fuente más pequeña para las tablas de metodología 
    public static final Font noteCellFont7 = new Font(arial, 7, Font.NORMAL);
    
    
    public static final Font strongNoteCellFont = new Font(arial, 10, Font.BOLD);
    public static final Font noteCellGreenFont = new Font(arial, 8, Font.NORMAL, Color.GREEN);
    public static final Font noteCellRedFont = new Font(arial, 8, Font.NORMAL, Color.RED);
    public static final Font priorityCellFont = new Font(arial, 8, Font.BOLD);

    public static final Font NOTE_ANCHOR_CELL_FONT = new Font(arial, 8, Font.UNDERLINE, LINK_COLOR);
    public static final Font INDEX_ITEMS = new Font(arial, 10, Font.NORMAL);
    public static final Font SCORE_FONT = new Font(arial, 14, Font.NORMAL, Color.BLACK);
    public static final Font SCORE_BOLD_FONT = new Font(arial, 14, Font.BOLD, Color.BLACK);
    public static final Font MORE_INFO_FONT = new Font(arial, 8, Font.ITALIC, Color.BLACK);

    public static final Font ANCHOR_FONT = new Font(arial, 10, Font.ITALIC, LINK_COLOR);
    public static final Font LINK_FONT = new Font(monospaced, 10, Font.ITALIC, LINK_COLOR);

    public static final int LINE_SPACE = 15;
    public static final int HALF_LINE_SPACE = 8;
    public static final int THIRD_LINE_SPACE = 5;
    public static final int TITLE_LINE_SPACE = 100;
    public static final int SUBTITLE_LINE_SPACE = 50;
    public static final int IDENTATION_LEFT_SPACE = 20;
    public static final int DEFAULT_PADDING = 4;

    public static final Chunk LIST_SYMBOL_1 = new Chunk("\u2022", FontFactory.getFont(FontFactory.HELVETICA, 15));
    public static final Chunk LIST_SYMBOL_2 = new Chunk("\u2022", FontFactory.getFont(FontFactory.HELVETICA, 12));

}
