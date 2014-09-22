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

    private static final Color PROBLEM_COLOR = new Color(226, 20, 48);
    private static final Color WARNING_COLOR = new Color(196, 126, 5);
    private static final Color CANNOTTELL_COLOR = new Color(39, 102, 196);

    public static BaseFont arial;

    static {
        try {
            PropertiesManager pmgr = new PropertiesManager();
            arial = BaseFont.createFont(pmgr.getValue("pdf.properties", "path.pdf.font"), BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            Logger.putLog("Excepción BaseFont arial: ", IntavExport.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private ConstantsFont() {
    }

    //FUENTES PDF

    public static final Font chapterTitleMPFont = new Font(arial, 14, Font.UNDERLINE, Constants.VERDE_O_MP);
    public static final Font chapterTitleMPFont2L = new Font(arial, 13, Font.NORMAL, Constants.VERDE_O_MP);
    public static final Font chapterTitleMPFont3L = new Font(arial, 12, Font.NORMAL, Constants.VERDE_O_MP);
    public static final Font documentTitleMPFont = new Font(arial, 34, Font.BOLD, Constants.NARANJA_MP);
    public static final Font documentSubtitleMPFont = new Font(arial, 30, Font.BOLD, Constants.VERDE_C_MP);
    public static final Font paragraphTitleTableFont = new Font(arial, 10, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font levelTitleMPFont = new Font(arial, 14, Font.BOLD, Constants.VERDE_C_MP);

    public static final Font chapterTitleFont = new Font(arial, 14, Font.UNDERLINE, Constants.ROJO_INTECO);
    public static final Font chapterTitleFont2L = new Font(arial, 13, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font chapterTitleFont3L = new Font(arial, 12, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font paragraphFont = new Font(arial, 10, Font.NORMAL);
    public static final Font paragraphAnchorFont = new Font(arial, 10, Font.UNDERLINE, Color.BLUE);
    public static final Font paragraphRedFont = new Font(arial, 10, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font paragraphBoldFont = new Font(arial, 10, Font.BOLD);
    public static final Font paragraphBoldTitleFont = new Font(arial, 12, Font.BOLD);
    public static final Font paragraphTitleFont = new Font(arial, 12, Font.NORMAL);
    public static final Font paragraphUnderlinedFont = new Font(arial, 10, Font.UNDERLINE);
    public static final Font listSymbol = new Font(arial, 33, Font.NORMAL, Color.BLACK);

    public static final Font summaryTitleFont = new Font(arial, 14, Font.BOLD, Color.black);
    public static final Font summaryFont = new Font(new Font(arial, 12, Font.BOLD));
    public static final Font summaryFontProblem = new Font(new Font(arial, 12, Font.BOLD, PROBLEM_COLOR));
    public static final Font summaryFontWarning = new Font(new Font(arial, 12, Font.BOLD, WARNING_COLOR));
    public static final Font summaryFontCannottell = new Font(new Font(arial, 12, Font.BOLD, CANNOTTELL_COLOR));
    public static final Font problemFont = new Font(new Font(arial, 10, Font.BOLD, PROBLEM_COLOR));
    public static final Font warningFont = new Font(new Font(arial, 10, Font.BOLD, WARNING_COLOR));
    public static final Font cannottellFont = new Font(new Font(arial, 10, Font.BOLD, CANNOTTELL_COLOR));
    public static final Font summaryScoreFont = new Font(new Font(arial, 10, Font.NORMAL, Color.BLACK));
    public static final Font summaryScoreBoldFont = new Font(new Font(arial, 10, Font.BOLD, Color.BLACK));
    public static final Font documentTitleFont = new Font(arial, 34, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font documentSubtitleFont = new Font(arial, 30, Font.BOLD, new Color(128, 0, 0));
    public static final Font sectionFont = new Font(arial, 16, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font descriptionFont = new Font(arial, 10, Font.NORMAL);
    public static final Font strongDescriptionFont = new Font(arial, 10, Font.BOLD);
    public static final Font descriptionFontGreen = new Font(arial, 10, Font.NORMAL, Constants.VERDE_O_MP);
    public static final Font descriptionFontRed = new Font(arial, 10, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font guidelineDescFont = new Font(arial, 12, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font guidelineDescMPFont = new Font(arial, 12, Font.NORMAL, Constants.ROJO_INTECO);
    public static final Font labelCellFont = new Font(arial, 10, Font.NORMAL, Color.WHITE);
    public static final Font codeCellFont = FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL);
    public static final Font noteCellFont = new Font(arial, 8, Font.NORMAL);
    public static final Font strongNoteCellFont = new Font(arial, 8, Font.BOLD);
    public static final Font noteCellGreenFont = new Font(arial, 8, Font.NORMAL, Color.GREEN);
    public static final Font noteCellRedFont = new Font(arial, 8, Font.NORMAL, Color.RED);
    public static final Font priorityCellFont = new Font(arial, 8, Font.BOLD);
    public static final Font noteAnchorCellFont = new Font(arial, 8, Font.UNDERLINE, new Color(0, 0, 255));
    public static final Font indexItems = new Font(arial, 10, Font.NORMAL);
    public static final Font scoreFont = new Font(arial, 14, Font.NORMAL, Color.BLACK);
    public static final Font scoreBoldFont = new Font(arial, 14, Font.BOLD, Color.BLACK);
    public static final Font levelScoreFont = new Font(arial, 12, Font.NORMAL, Color.BLACK);
    public static final Font suitabilityFont = new Font(arial, 12, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font suitabilityScoreFont = new Font(arial, 12, Font.BOLD, Color.BLACK);
    public static final Font titleLenoxFont = new Font(arial, 12, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font valueLenoxFont = new Font(arial, 12, Font.NORMAL, Color.BLACK);
    public static final Font lenoxContextRedFont = new Font(arial, 8, Font.BOLD, Constants.ROJO_INTECO);
    public static final Font lenoxContextBlackFont = new Font(arial, 8, Font.NORMAL, Color.BLACK);
    public static final Font moreInfoFont = new Font(arial, 8, Font.ITALIC, Color.BLACK);

    public static final int SPACE_LINE = 15;
    public static final int SPACE_TITLE_LINE = 100;
    public static final int SPACE_SUBTITLE_LINE = 100;
    public static final int IDENTATION_LEFT_SPACE = 20;

    public static final Chunk listSymbol1 = new Chunk("\u2022", FontFactory.getFont(FontFactory.HELVETICA, 15));
    public static final Chunk listSymbol2 = new Chunk("\u2022", FontFactory.getFont(FontFactory.HELVETICA, 12));

}
