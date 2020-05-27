package es.oaw.wcagem;

import java.io.File;

import org.jopendocument.dom.ODValueType;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 * The Class WcagOdsUtils.
 * 
 * Generates an ods
 * 
 */
public final class WcagOdsUtils {
	/** The Constant MAX_PAGES. */
	private static final int MAX_PAGES = 30;
	/** The Constant EARL_INAPPLICABLE. */
	private static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	private static final String EARL_FAILED = "earl:failed";
	/** The Constant EARL_PASSED. */
	private static final String EARL_PASSED = "earl:passed";
	/** The Constant EARL_CANNOT_TELL. */
	private static final String EARL_CANNOT_TELL = "earl:cantTell";

	/**
	 * Generate ods.
	 *
	 * @param report the report
	 * @throws Exception the exception
	 */
	public static void generateOds(final WcagEmReport report) throws Exception {
		// File inputFile = new File("/home/alvaro/Downloads/Borrador_Informe_Revision_Profunidad_v1.ods");
		File inputFile = new File("/home/alvaro/Downloads/Borrador_Informe_Revision_Profunidad_v1_NF.ods");
		File outputFile = new File("/home/alvaro/Downloads/Borrador_Informe_Revision_Profunidad_v1_M.ods");
		// Load template
		final SpreadSheet workbook = SpreadSheet.createFromFile(inputFile);
		final Sheet sheet = workbook.getSheet("03.Muestra");
		int resultsProcessed = 0;
		int initRow = 8; // Initial rowcount
		for (Webpage_ webpage : report.getGraph().get(0).getRandomSample().getWebpage()) {
			if (resultsProcessed < MAX_PAGES) {
				resultsProcessed++;
				sheet.getCellAt("C" + initRow).setValue(webpage.getTitle(), ODValueType.STRING, true, false);
				sheet.getCellAt("D" + initRow).setValue("Aleatoria", ODValueType.STRING, true, false);
				sheet.getCellAt("E" + initRow).setValue(webpage.getSource(), ODValueType.STRING, true, false);
				initRow++;
			}
		}
		final Sheet sheetP1 = workbook.getSheet("P1.Perceptible");
		final Sheet sheetP2 = workbook.getSheet("P2.Operable");
		final Sheet sheetP3 = workbook.getSheet("P3.Comprensible");
		final Sheet sheetP4 = workbook.getSheet("P4.Robusto");
		resultsProcessed = 0;
		for (AuditResult auditResult : report.getGraph().get(0).getAuditResult()) {
			if (resultsProcessed < MAX_PAGES) {
				resultsProcessed++;
				switch (auditResult.getTest()) {
				// P1
				case "WCAG2:non-text-content":
					extracted(sheetP1, auditResult, 19);
					break;
				case "WCAG2:info-and-relationships":
					extracted(sheetP1, auditResult, 217);
					break;
				case "WCAG2:orientation":
					extracted(sheetP1, auditResult, 316);
					break;
				case "WCAG2:identify-input-purpose":
					extracted(sheetP1, auditResult, 349);
					break;
				case "WCAG2:contrast-minimum":
					extracted(sheetP1, auditResult, 448);
					break;
				case "WCAG2:reflow":
					extracted(sheetP1, auditResult, 548);
					break;
				case "WCAG2:text-spacing":
					extracted(sheetP1, auditResult, 613);
					break;
				// P2
				case "WCAG2:keyboard":
					extracted(sheetP2, auditResult, 19);
					break;
				case "WCAG2:timing-adjustable":
					extracted(sheetP2, auditResult, 119);
					break;
				case "WCAG2:tpause-stop-hide":
					extracted(sheetP2, auditResult, 151);
					break;
				case "WCAG2:three-flashes-or-below-threshold":
					extracted(sheetP2, auditResult, 184);
					break;
				case "WCAG2:bypass-blocks":
					extracted(sheetP2, auditResult, 217);
					break;
				case "WCAG2:page-titled":
					extracted(sheetP2, auditResult, 250);
					break;
				case "WCAG2:focus-order":
					extracted(sheetP2, auditResult, 283);
					break;
				case "WCAG2:link-purpose-in-context":
					extracted(sheetP2, auditResult, 316);
					break;
				case "WCAG2:multiple-ways":
					extracted(sheetP2, auditResult, 349);
					break;
				case "WCAG2:focus-visible":
					extracted(sheetP2, auditResult, 416);
					break;
				case "WCAG2:label-in-name":
					extracted(sheetP2, auditResult, 514);
					break;
				// P3
				case "WCAG2:language-of-page":
					extracted(sheetP3, auditResult, 19);
					break;
				case "WCAG2:language-of-parts":
					extracted(sheetP3, auditResult, 52);
					break;
				case "WCAG2:on-focus":
					extracted(sheetP3, auditResult, 85);
					break;
				case "WCAG2:on-input":
					extracted(sheetP3, auditResult, 119);
					break;
				case "WCAG2:consistent-navigation":
					extracted(sheetP3, auditResult, 151);
					break;
				case "WCAG2:labels-or-instructions":
					extracted(sheetP3, auditResult, 217);
					break;
				// p4
				case "WCAG2:name-role-value":
					extracted(sheetP4, auditResult, 52);
					break;
				default:
					break;
				}
			}
		}
		// Save
		workbook.saveAs(outputFile);
	}

	/**
	 * Extracted.
	 *
	 * @param sheet        the sheet
	 * @param auditResult  the audit result
	 * @param initRowValue the init row value
	 */
	private static void extracted(final Sheet sheet, AuditResult auditResult, final int initRowValue) {
		int initRow = initRowValue;
		int resultsProcessed = 0;
		for (HasPart hasPart : auditResult.getHasPart()) {
			if (resultsProcessed < MAX_PAGES) {
				final MutableCell<SpreadSheet> cellAt = sheet.getCellAt("D" + initRow);
				cellAt.setValue(odsOutcome(hasPart.getResult().getOutcome()));
				resultsProcessed++;
				initRow++;
			}
		}
	}

	/**
	 * Ods outcome.
	 *
	 * @param jsonOutcome the json outcome
	 * @return the string
	 */
	private static String odsOutcome(final String jsonOutcome) {
		switch (jsonOutcome) {
		case EARL_PASSED:
			return "N/D";
		case EARL_FAILED:
			return "Falla";
		case EARL_CANNOT_TELL:
			return "N/T";
		case EARL_INAPPLICABLE:
			return "N/A";
		default:
			return "N/T";
		}
	}
}
