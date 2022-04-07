package es.oaw.wcagem;

import java.io.File;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jopendocument.dom.ODValueType;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.semillas.PlantillaForm;
import es.inteco.rastreador2.dao.plantilla.PlantillaDAO;

/**
 * The Class WcagOdsUtils.
 * 
 * Generates an ods
 * 
 */
public final class WcagOdsUtils {
	/** The Constant MAX_PAGES. */
	private static final int MAX_PAGES = 35;
	/** The Constant EARL_INAPPLICABLE. */
	private static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	private static final String EARL_FAILED = "earl:failed";
	/** The Constant EARL_PASSED. */
	private static final String EARL_PASSED = "earl:passed";
	/** The Constant EARL_CANNOT_TELL. */
	private static final String EARL_CANNOT_TELL = "earl:cantTell";

	/**
	 * Gets the ods template.
	 *
	 * @return the ods template
	 * @throws Exception the exception
	 */
	private static File getOdsTemplate() throws Exception {
		PlantillaForm plantilla = PlantillaDAO.findByType(DataBaseManager.getConnection(), "ods");
		if (plantilla != null && plantilla.getDocumento() != null && plantilla.getDocumento().length > 0) {
			File f = File.createTempFile("tmp_template", ".ods");
			FileUtils.writeByteArrayToFile(f, plantilla.getDocumento());
			return f;
		}
		return null;
	}

	/**
	 * Generate ods.
	 *
	 * @param report the report
	 * @return the spread sheet
	 * @throws Exception the exception
	 */
	public static SpreadSheet generateOds(final WcagEmReport report) throws Exception {
		// Get file from database
		File inputFile = getOdsTemplate();
		// Load template
		final SpreadSheet workbook = SpreadSheet.createFromFile(inputFile);
		// Date
		final Sheet sheetAmbit = workbook.getSheet("01.Definición de ámbito");
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyy");
		sheetAmbit.getCellAt("C35").setValue(fmt.format(OffsetDateTime.now(ZoneId.of("Europe/Madrid"))));
		// filt techs
		final Sheet sheetTech = workbook.getSheet("02.Tecnologías");
		sheetTech.getCellAt("C9").setValue("Sí");
		sheetTech.getCellAt("C12").setValue("Sí");
		sheetTech.getCellAt("C13").setValue("Sí");
		final Sheet sheet = workbook.getSheet("03.Muestra");
		int resultsProcessed = 0;
		int initRow = 8; // Initial rowcount
		final List<Webpage> webpageList = report.getGraph().get(0).getStructuredSample().getWebpage();
		final int totalPages = webpageList.size();
		fillNotTell(workbook, totalPages < MAX_PAGES ? totalPages : MAX_PAGES);
		for (Webpage webpage : webpageList) {
			if (resultsProcessed < MAX_PAGES) {
				resultsProcessed++;
				sheet.getCellAt("C" + initRow).setValue(webpage.getTitle(), ODValueType.STRING, true, false);
				sheet.getCellAt("D" + initRow).setValue("Página Web", ODValueType.STRING, true, false);				
				sheet.getCellAt("E" + initRow).setValue("", ODValueType.STRING, true, false);
				sheet.getCellAt("F" + initRow).setValue(webpage.getSource(), ODValueType.STRING, true, false);
				initRow++;
			}
		}
		final Sheet sheetR9 = workbook.getSheet("R9.Web");
		//final Sheet sheetP2 = workbook.getSheet("P2.Operable");
		//final Sheet sheetP3 = workbook.getSheet("P3.Comprensible");
		//final Sheet sheetP4 = workbook.getSheet("P4.Robusto");
		resultsProcessed = 0;
		for (AuditResult auditResult : report.getGraph().get(0).getAuditResult()) {
			if (resultsProcessed < MAX_PAGES) {
				resultsProcessed++;
				switch (auditResult.getTest()) {
				// P1
				case "WCAG2:non-text-content":
					fillResult(sheetR9, auditResult, 19);
					break;
				case "WCAG2:info-and-relationships":
					fillResult(sheetR9, auditResult, 209);
					break;
				case "WCAG2:orientation":
					fillResult(sheetR9, auditResult, 323);
					break;
				case "WCAG2:identify-input-purpose":
					fillResult(sheetR9, auditResult, 361);
					break;
				case "WCAG2:contrast-minimum":
					fillResult(sheetR9, auditResult, 475);
					break;
				case "WCAG2:reflow":
					fillResult(sheetR9, auditResult, 589);
					break;
				case "WCAG2:text-spacing":
					fillResult(sheetR9, auditResult, 665);
					break;
				// P2
				case "WCAG2:keyboard":
					fillResult(sheetR9, auditResult, 741);
					break;
				case "WCAG2:timing-adjustable":
					fillResult(sheetR9, auditResult, 855);
					break;
				case "WCAG2:pause-stop-hide":
					fillResult(sheetR9, auditResult, 893);
					break;
				case "WCAG2:three-flashes-or-below-threshold":
					fillResult(sheetR9, auditResult, 931);
					break;
				case "WCAG2:bypass-blocks":
					fillResult(sheetR9, auditResult, 969);
					break;
				case "WCAG2:page-titled":
					fillResult(sheetR9, auditResult, 1007);
					break;
				case "WCAG2:focus-order":
					fillResult(sheetR9, auditResult, 1045);
					break;
				case "WCAG2:link-purpose-in-context":
					fillResult(sheetR9, auditResult, 1083);
					break;
				case "WCAG2:multiple-ways":
					fillResult(sheetR9, auditResult, 1121);
					break;
				case "WCAG2:focus-visible":
					fillResult(sheetR9, auditResult, 1197);
					break;
				case "WCAG2:label-in-name":
					fillResult(sheetR9, auditResult, 1311);
					break;
				// P3
				case "WCAG2:language-of-page":
					fillResult(sheetR9, auditResult,1387);
					break;
				case "WCAG2:language-of-parts":
					fillResult(sheetR9, auditResult, 1425);
					break;
				case "WCAG2:on-focus":
					fillResult(sheetR9, auditResult, 1463);
					break;
				case "WCAG2:on-input":
					fillResult(sheetR9, auditResult, 1501);
					break;
				case "WCAG2:consistent-navigation":
					fillResult(sheetR9, auditResult, 1539);
					break;
				case "WCAG2:labels-or-instructions":
					fillResult(sheetR9, auditResult, 1653);
					break;
				// p4
				case "WCAG2:parsing":
					fillResult(sheetR9, auditResult, 1767);
					break;
				case "WCAG2:name-role-value":
					fillResult(sheetR9, auditResult, 1805);
					break;
				default:
					break;
				}
			}
		}
//		FileUtils.doWithLock(f, transf)
		return workbook;
	}

	/**
	 * Extracted.
	 *
	 * @param sheet        the sheet
	 * @param auditResult  the audit result
	 * @param initRowValue the init row value
	 */
	private static void fillResult(final Sheet sheet, AuditResult auditResult, final int initRowValue) {
		int initRow = initRowValue;
		int resultsProcessed = 0;
		for (HasPart hasPart : auditResult.getHasPart()) {
			if (resultsProcessed < MAX_PAGES) {
				final MutableCell<SpreadSheet> cellAt = sheet.getCellAt("D" + initRow);
				cellAt.clearValue();
				cellAt.setValue(odsOutcome(hasPart.getResult().getOutcome()));
				resultsProcessed++;
				initRow++;
			}
		}
	}

	/**
	 * Fill not tell.
	 *
	 * @param workbook   the workbook
	 * @param totalPages the total pages
	 */
	private static void fillNotTell(final SpreadSheet workbook, final int totalPages) {
		final Sheet sheetR5 = workbook.getSheet("R5.Genéricos");
		int tableRowIndex = 19;
		while (tableRowIndex <= 129) {
			for (int i = 0; i < totalPages; i++) {
				sheetR5.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
		}
		final Sheet sheetR6 = workbook.getSheet("R6.Voz");
		tableRowIndex = 19;
		while (tableRowIndex <= 737) {
			for (int i = 0; i < totalPages; i++) {
				sheetR6.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
		}
		final Sheet sheetR9 = workbook.getSheet("R9.Web");
		tableRowIndex = 19;
		while (tableRowIndex <= 1915) {
			for (int i = 0; i < totalPages; i++) {
				sheetR9.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
		}		
		final Sheet sheetR11 = workbook.getSheet("R11.Software");
		tableRowIndex = 19;
		while (tableRowIndex <= 243) {
			for (int i = 0; i < totalPages; i++) {
				sheetR11.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
		}
		final Sheet sheetR12 = workbook.getSheet("R12.ServiciosApoyo");
		tableRowIndex = 19;
		while (tableRowIndex <= 205) {
			for (int i = 0; i < totalPages; i++) {
				sheetR12.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
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
			return "N/D";
		case EARL_INAPPLICABLE:
			return "N/A";
		default:
			return "N/T";
		}
	}
}
