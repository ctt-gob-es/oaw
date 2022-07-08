package es.inteco.rastreador2.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The Class XlsxUtils.
 */
public class XlsxUtils {
	/** The Constant PALE_BLUE_BACKGROUND_WHITE10_FONT. */
	public static final String PALE_BLUE_BACKGROUND_WHITE10_FONT = "paleBlueBackgroundWhite10Font";
	/** The Constant ROYAL_BLUE_BACKGROUND_WHITE10_FONT. */
	public static final String ROYAL_BLUE_BACKGROUND_WHITE10_FONT = "royalBlueBackgroundWhite10Font";
	/** The Constant NORMAL_PERCENT11_CENTER_STYLE. */
	public static final String NORMAL_PERCENT11_CENTER_STYLE = "normalPercent11CenterStyle";
	/** The Constant WHITE_BACKGROUND_BLACK_BOLD10. */
	public static final String WHITE_BACKGROUND_BLACK_BOLD10 = "whiteBackgroundBlackBold10";
	/** The Constant BLUE_BACKGROUND_BLACK_BOLD10_LEFT. */
	public static final String BLUE_BACKGROUND_BLACK_BOLD10_LEFT = "blueBackgroundBlackBold10Left";
	/** The Constant BLUE_BACKGROUND_BLACK_BOLD10_CENTER. */
	public static final String BLUE_BACKGROUND_BLACK_BOLD10_CENTER = "blueBackgroundBlackBold10Center";
	/** The Constant BLUE_BACKGROUND_WHITE_BOLD10_LEFT. */
	public static final String BLUE_BACKGROUND_WHITE_BOLD10_LEFT = "blueBackgroundWhiteBold10Left";
	/** The Constant THIRD_STYLE. */
	public static final String THIRD_STYLE = "thirdStyle";
	/** The Constant SECOND_STYLE. */
	public static final String SECOND_STYLE = "secondStyle";
	/** The Constant FIRST_STYLE. */
	public static final String FIRST_STYLE = "firstStyle";
	/** The Constant WHITE_BACKGROUNT_NORMAL11_DECIMAL_CENTER. */
	public static final String WHITE_BACKGROUNT_NORMAL11_DECIMAL_CENTER = "whiteBackgrountNormal11DecimalCenter";
	/** The Constant WHITE_BACKGROUNT_NORMAL11_CENTER. */
	public static final String WHITE_BACKGROUNT_NORMAL11_CENTER = "whiteBackgrountNormal11Center";
	/** The Constant LIGHT_BLUE_BACKGROUND_BLACK_NORMAL11. */
	public static final String LIGHT_BLUE_BACKGROUND_BLACK_NORMAL11 = "lightBlueBackgroundBlackNormal11";
	/** The Constant BLUE_BACKGROUND_BLACK_BOLD10. */
	public static final String BLUE_BACKGROUND_BLACK_BOLD10 = "blueBackgroundBlackBold10";
	/** The Constant ORANGE_BACKGROUND_BLACK_BOLD10. */
	public static final String ORANGE_BACKGROUND_BLACK_BOLD10 = "orangeBackgroundBlackBold10";
	/** The Constant RED_BACKGROUNF_BLACK_BOLD10. */
	public static final String RED_BACKGROUNF_BLACK_BOLD10 = "redBackgrounfBlackBold10";
	/** The Constant YELLOW_BACKGROUND_BLACK10. */
	public static final String YELLOW_BACKGROUND_BLACK10 = "yellowBackgroundBlack10";
	/** The Constant GREEN_BACKGROUND_WHITE_BOLD16. */
	public static final String GREEN_BACKGROUND_WHITE_BOLD16 = "greenBackgroundWhiteBold16";
	/** The Constant BLUE_BACKGROUND_WHITE_BOLD16. */
	public static final String BLUE_BACKGROUND_WHITE_BOLD16 = "blueBackgroundWhiteBold16";
	/** The Constant BOLD_PERCENT11_CENTER_STYLE. */
	public static final String BOLD_PERCENT11_CENTER_STYLE = "boldPercent11CenterStyle";
	/** The Constant BOLD_DATA11_CENTER_STYLE. */
	public static final String BOLD_DATA11_CENTER_STYLE = "boldData11CenterStyle";
	/** The Constant BOLD_DATA11_LEFT_STYLE. */
	public static final String BOLD_DATA11_LEFT_STYLE = "boldData11LeftStyle";
	/** The Constant TW_DECIMALS_FORMAT. */
	private static final String TWO_DECIMALS_FORMAT = "0.00";
	/** The Constant PERCENT_FORMAT. */
	private static final String PERCENT_FORMAT = "0.00%";
	/** The Constant ARIAL_FONT_NAME. */
	private static final String ARIAL_FONT_NAME = "Arial";
	/** The Constant COLOR_BLACK. */
	private static final short COLOR_BLACK = IndexedColors.BLACK.getIndex();
	/** The Constant COLOR_WHITE. */
	private static final short COLOR_WHITE = IndexedColors.WHITE.getIndex();
	/** The cell styles. */
	private Map<String, CellStyle> cellStyles;

	/**
	 * Inits the styles.
	 *
	 * @param wb the wb
	 */
	public XlsxUtils(final XSSFWorkbook wb) {
		final CreationHelper createHelper = wb.getCreationHelper();
		cellStyles = new HashMap<>();
		// FONTS
		XSSFFont whiteBold16Font = wb.createFont();
		whiteBold16Font.setFontHeightInPoints((short) 16);
		whiteBold16Font.setFontName(ARIAL_FONT_NAME);
		whiteBold16Font.setColor(COLOR_WHITE);
		whiteBold16Font.setBold(true);
		whiteBold16Font.setItalic(false);
		XSSFFont whiteBold10Font = wb.createFont();
		whiteBold10Font.setFontHeightInPoints((short) 10.5);
		whiteBold10Font.setFontName(ARIAL_FONT_NAME);
		whiteBold10Font.setColor(COLOR_WHITE);
		whiteBold10Font.setBold(true);
		whiteBold10Font.setItalic(false);
		XSSFFont blackBold10Font = wb.createFont();
		blackBold10Font.setFontHeightInPoints((short) 10.5);
		blackBold10Font.setFontName(ARIAL_FONT_NAME);
		blackBold10Font.setColor(COLOR_BLACK);
		blackBold10Font.setBold(true);
		blackBold10Font.setItalic(false);
		XSSFFont blackNormal10Font = wb.createFont();
		blackNormal10Font.setFontHeightInPoints((short) 10);
		blackNormal10Font.setFontName(ARIAL_FONT_NAME);
		blackNormal10Font.setColor(COLOR_BLACK);
		blackNormal10Font.setBold(false);
		blackNormal10Font.setItalic(false);
		XSSFFont blackNormal11Font = wb.createFont();
		blackNormal11Font.setFontHeightInPoints((short) 11);
		blackNormal11Font.setFontName(ARIAL_FONT_NAME);
		blackNormal11Font.setColor(COLOR_BLACK);
		blackNormal11Font.setBold(false);
		blackNormal11Font.setItalic(false);
		XSSFFont blackBold11Font = wb.createFont();
		blackBold11Font.setFontHeightInPoints((short) 11);
		blackBold11Font.setFontName(ARIAL_FONT_NAME);
		blackBold11Font.setColor(COLOR_BLACK);
		blackBold11Font.setBold(true);
		blackBold11Font.setItalic(false);
		XSSFFont blackBold16Font = wb.createFont();
		blackBold16Font.setFontHeightInPoints((short) 16);
		blackBold16Font.setFontName(ARIAL_FONT_NAME);
		blackBold16Font.setColor(COLOR_BLACK);
		blackBold16Font.setBold(true);
		blackBold16Font.setItalic(false);
		XSSFFont blackBold12Font = wb.createFont();
		blackBold12Font.setFontHeightInPoints((short) 12);
		blackBold12Font.setFontName(ARIAL_FONT_NAME);
		blackBold12Font.setColor(COLOR_BLACK);
		blackBold12Font.setBold(true);
		blackBold12Font.setItalic(false);
		// STYLES
		CellStyle boldData11LeftStyle = wb.createCellStyle();
		boldData11LeftStyle.setWrapText(true);
		boldData11LeftStyle.setAlignment(HorizontalAlignment.LEFT);
		boldData11LeftStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		boldData11LeftStyle.setFillForegroundColor(COLOR_WHITE);
		boldData11LeftStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		boldData11LeftStyle.setFont(blackBold11Font);
		cellStyles.put(BOLD_DATA11_LEFT_STYLE, boldData11LeftStyle);
		CellStyle boldData11CenterStyle = wb.createCellStyle();
		boldData11CenterStyle.setWrapText(true);
		boldData11CenterStyle.setAlignment(HorizontalAlignment.CENTER);
		boldData11CenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		boldData11CenterStyle.setFillForegroundColor(COLOR_WHITE);
		boldData11CenterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		boldData11CenterStyle.setFont(blackBold11Font);
		cellStyles.put(BOLD_DATA11_CENTER_STYLE, boldData11CenterStyle);
		CellStyle normalPercent11CenterStyle = wb.createCellStyle();
		normalPercent11CenterStyle.setWrapText(true);
		normalPercent11CenterStyle.setAlignment(HorizontalAlignment.CENTER);
		normalPercent11CenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		normalPercent11CenterStyle.setFillForegroundColor(COLOR_WHITE);
		normalPercent11CenterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		normalPercent11CenterStyle.setFont(blackNormal11Font);
		normalPercent11CenterStyle.setDataFormat(createHelper.createDataFormat().getFormat(PERCENT_FORMAT));
		cellStyles.put(NORMAL_PERCENT11_CENTER_STYLE, normalPercent11CenterStyle);
		CellStyle boldPercent11CenterStyle = wb.createCellStyle();
		boldPercent11CenterStyle.setWrapText(true);
		boldPercent11CenterStyle.setAlignment(HorizontalAlignment.CENTER);
		boldPercent11CenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		boldPercent11CenterStyle.setFillForegroundColor(COLOR_WHITE);
		boldPercent11CenterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		boldPercent11CenterStyle.setFont(blackBold11Font);
		boldPercent11CenterStyle.setDataFormat(createHelper.createDataFormat().getFormat(PERCENT_FORMAT));
		cellStyles.put(BOLD_PERCENT11_CENTER_STYLE, boldPercent11CenterStyle);
		CellStyle blueBackgroundWhiteBold16 = wb.createCellStyle();
		blueBackgroundWhiteBold16.setWrapText(true);
		blueBackgroundWhiteBold16.setAlignment(HorizontalAlignment.CENTER);
		blueBackgroundWhiteBold16.setVerticalAlignment(VerticalAlignment.CENTER);
		blueBackgroundWhiteBold16.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		blueBackgroundWhiteBold16.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		blueBackgroundWhiteBold16.setFont(whiteBold16Font);
		cellStyles.put(BLUE_BACKGROUND_WHITE_BOLD16, blueBackgroundWhiteBold16);
		CellStyle greenBackgroundWhiteBold16 = wb.createCellStyle();
		greenBackgroundWhiteBold16.setWrapText(true);
		greenBackgroundWhiteBold16.setAlignment(HorizontalAlignment.CENTER);
		greenBackgroundWhiteBold16.setVerticalAlignment(VerticalAlignment.CENTER);
		greenBackgroundWhiteBold16.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		greenBackgroundWhiteBold16.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		greenBackgroundWhiteBold16.setFont(whiteBold10Font);
		cellStyles.put(GREEN_BACKGROUND_WHITE_BOLD16, greenBackgroundWhiteBold16);
		CellStyle yellowBackgroundBlack10 = wb.createCellStyle();
		yellowBackgroundBlack10.setWrapText(true);
		yellowBackgroundBlack10.setAlignment(HorizontalAlignment.CENTER);
		yellowBackgroundBlack10.setVerticalAlignment(VerticalAlignment.CENTER);
		yellowBackgroundBlack10.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		yellowBackgroundBlack10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		yellowBackgroundBlack10.setFont(blackBold10Font);
		cellStyles.put(YELLOW_BACKGROUND_BLACK10, yellowBackgroundBlack10);
		CellStyle redBackgrounfBlackBold10 = wb.createCellStyle();
		redBackgrounfBlackBold10.setWrapText(true);
		redBackgrounfBlackBold10.setAlignment(HorizontalAlignment.CENTER);
		redBackgrounfBlackBold10.setVerticalAlignment(VerticalAlignment.CENTER);
		redBackgrounfBlackBold10.setFillForegroundColor(IndexedColors.RED.getIndex());
		redBackgrounfBlackBold10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		redBackgrounfBlackBold10.setFont(blackBold10Font);
		cellStyles.put(RED_BACKGROUNF_BLACK_BOLD10, redBackgrounfBlackBold10);
		CellStyle orangeBackgroundBlackBold10 = wb.createCellStyle();
		orangeBackgroundBlackBold10.setWrapText(true);
		orangeBackgroundBlackBold10.setAlignment(HorizontalAlignment.CENTER);
		orangeBackgroundBlackBold10.setVerticalAlignment(VerticalAlignment.CENTER);
		orangeBackgroundBlackBold10.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		orangeBackgroundBlackBold10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		orangeBackgroundBlackBold10.setFont(blackBold10Font);
		cellStyles.put(ORANGE_BACKGROUND_BLACK_BOLD10, orangeBackgroundBlackBold10);
		CellStyle blueBackgroundBlackBold10 = wb.createCellStyle();
		blueBackgroundBlackBold10.setWrapText(true);
		blueBackgroundBlackBold10.setAlignment(HorizontalAlignment.CENTER);
		blueBackgroundBlackBold10.setVerticalAlignment(VerticalAlignment.CENTER);
		blueBackgroundBlackBold10.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		blueBackgroundBlackBold10.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		blueBackgroundBlackBold10.setFont(blackBold10Font);
		cellStyles.put(BLUE_BACKGROUND_BLACK_BOLD10, blueBackgroundBlackBold10);
		CellStyle lightBlueBackgroundBlackNormal11 = wb.createCellStyle();
		lightBlueBackgroundBlackNormal11.setWrapText(true);
		lightBlueBackgroundBlackNormal11.setAlignment(HorizontalAlignment.CENTER);
		lightBlueBackgroundBlackNormal11.setVerticalAlignment(VerticalAlignment.CENTER);
		lightBlueBackgroundBlackNormal11.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		lightBlueBackgroundBlackNormal11.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		lightBlueBackgroundBlackNormal11.setFont(blackNormal11Font);
		cellStyles.put(LIGHT_BLUE_BACKGROUND_BLACK_NORMAL11, lightBlueBackgroundBlackNormal11);
		CellStyle whiteBackgrountNormal11Center = wb.createCellStyle();
		whiteBackgrountNormal11Center.setWrapText(true);
		whiteBackgrountNormal11Center.setAlignment(HorizontalAlignment.CENTER);
		whiteBackgrountNormal11Center.setVerticalAlignment(VerticalAlignment.CENTER);
		whiteBackgrountNormal11Center.setFillForegroundColor(COLOR_WHITE);
		whiteBackgrountNormal11Center.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		whiteBackgrountNormal11Center.setFont(blackNormal11Font);
		cellStyles.put(WHITE_BACKGROUNT_NORMAL11_CENTER, whiteBackgrountNormal11Center);
		CellStyle whiteBackgrountNormal11DecimalCenter = wb.createCellStyle();
		whiteBackgrountNormal11DecimalCenter.setWrapText(true);
		whiteBackgrountNormal11DecimalCenter.setAlignment(HorizontalAlignment.CENTER);
		whiteBackgrountNormal11DecimalCenter.setVerticalAlignment(VerticalAlignment.CENTER);
		whiteBackgrountNormal11DecimalCenter.setFillForegroundColor(COLOR_WHITE);
		whiteBackgrountNormal11DecimalCenter.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		whiteBackgrountNormal11DecimalCenter.setFont(blackNormal11Font);
		whiteBackgrountNormal11DecimalCenter.setDataFormat(createHelper.createDataFormat().getFormat(TWO_DECIMALS_FORMAT));
		cellStyles.put(WHITE_BACKGROUNT_NORMAL11_DECIMAL_CENTER, whiteBackgrountNormal11DecimalCenter);
		CellStyle firstStyle = wb.createCellStyle();
		firstStyle.setWrapText(true);
		firstStyle.setAlignment(HorizontalAlignment.CENTER);
		firstStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		firstStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
		firstStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		firstStyle.setFont(blackBold10Font);
		cellStyles.put(FIRST_STYLE, firstStyle);
		CellStyle secondStyle = wb.createCellStyle();
		secondStyle.setWrapText(true);
		secondStyle.setAlignment(HorizontalAlignment.CENTER);
		secondStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		secondStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		secondStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		secondStyle.setFont(blackBold10Font);
		cellStyles.put(SECOND_STYLE, secondStyle);
		CellStyle thirdStyle = wb.createCellStyle();
		thirdStyle.setWrapText(true);
		thirdStyle.setAlignment(HorizontalAlignment.CENTER);
		thirdStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		thirdStyle.setFillForegroundColor(IndexedColors.BROWN.getIndex());
		thirdStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		thirdStyle.setFont(blackBold10Font);
		cellStyles.put(THIRD_STYLE, thirdStyle);
		CellStyle blueBackgroundWhiteBold10Left = wb.createCellStyle();
		blueBackgroundWhiteBold10Left.setWrapText(true);
		blueBackgroundWhiteBold10Left.setAlignment(HorizontalAlignment.LEFT);
		blueBackgroundWhiteBold10Left.setVerticalAlignment(VerticalAlignment.CENTER);
		blueBackgroundWhiteBold10Left.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		blueBackgroundWhiteBold10Left.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		blueBackgroundWhiteBold10Left.setFont(whiteBold10Font);
		cellStyles.put(BLUE_BACKGROUND_WHITE_BOLD10_LEFT, blueBackgroundWhiteBold10Left);
		CellStyle blueBackgroundBlackBold10Center = wb.createCellStyle();
		blueBackgroundBlackBold10Center.setWrapText(true);
		blueBackgroundBlackBold10Center.setAlignment(HorizontalAlignment.CENTER);
		blueBackgroundBlackBold10Center.setVerticalAlignment(VerticalAlignment.CENTER);
		blueBackgroundBlackBold10Center.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		blueBackgroundBlackBold10Center.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		blueBackgroundBlackBold10Center.setFont(whiteBold10Font);
		cellStyles.put(BLUE_BACKGROUND_BLACK_BOLD10_CENTER, blueBackgroundBlackBold10Center);
		CellStyle blueBackgroundBlackBold10Left = wb.createCellStyle();
		blueBackgroundBlackBold10Left.setWrapText(true);
		blueBackgroundBlackBold10Left.setAlignment(HorizontalAlignment.LEFT);
		blueBackgroundBlackBold10Left.setVerticalAlignment(VerticalAlignment.CENTER);
		blueBackgroundBlackBold10Left.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		blueBackgroundBlackBold10Left.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		blueBackgroundBlackBold10Left.setFont(whiteBold10Font);
		cellStyles.put(BLUE_BACKGROUND_BLACK_BOLD10_LEFT, blueBackgroundBlackBold10Left);
		CellStyle whiteBackgroundBlackBold10 = wb.createCellStyle();
		whiteBackgroundBlackBold10.setWrapText(true);
		whiteBackgroundBlackBold10.setFillForegroundColor(COLOR_WHITE);
		whiteBackgroundBlackBold10.setAlignment(HorizontalAlignment.CENTER);
		whiteBackgroundBlackBold10.setVerticalAlignment(VerticalAlignment.CENTER);
		whiteBackgroundBlackBold10.setFillPattern(FillPatternType.NO_FILL);
		whiteBackgroundBlackBold10.setFont(blackBold12Font);
		cellStyles.put(WHITE_BACKGROUND_BLACK_BOLD10, whiteBackgroundBlackBold10);
		// create header cell style
		CellStyle royalBlueBackgroundWhite10Font = wb.createCellStyle();
		royalBlueBackgroundWhite10Font.setWrapText(true);
		royalBlueBackgroundWhite10Font.setAlignment(HorizontalAlignment.CENTER);
		royalBlueBackgroundWhite10Font.setVerticalAlignment(VerticalAlignment.CENTER);
		royalBlueBackgroundWhite10Font.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
		royalBlueBackgroundWhite10Font.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		royalBlueBackgroundWhite10Font.setFont(whiteBold10Font);
		cellStyles.put(ROYAL_BLUE_BACKGROUND_WHITE10_FONT, royalBlueBackgroundWhite10Font);
		// create light shadow cell style
		CellStyle paleBlueBackgroundWhite10Font = wb.createCellStyle();
		paleBlueBackgroundWhite10Font.setWrapText(true);
		paleBlueBackgroundWhite10Font.setAlignment(HorizontalAlignment.LEFT);
		paleBlueBackgroundWhite10Font.setVerticalAlignment(VerticalAlignment.TOP);
		paleBlueBackgroundWhite10Font.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		paleBlueBackgroundWhite10Font.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		paleBlueBackgroundWhite10Font.setFont(blackNormal10Font);
		paleBlueBackgroundWhite10Font.setBorderBottom(BorderStyle.THIN);
		paleBlueBackgroundWhite10Font.setBorderTop(BorderStyle.THIN);
		paleBlueBackgroundWhite10Font.setBorderRight(BorderStyle.THIN);
		paleBlueBackgroundWhite10Font.setBorderLeft(BorderStyle.THIN);
		cellStyles.put(PALE_BLUE_BACKGROUND_WHITE10_FONT, paleBlueBackgroundWhite10Font);
	}

	/**
	 * Gets the cell style by name.
	 *
	 * @param name the name
	 * @return the cell style by name
	 */
	public CellStyle getCellStyleByName(final String name) {
		return cellStyles.get(name);
	}
}
