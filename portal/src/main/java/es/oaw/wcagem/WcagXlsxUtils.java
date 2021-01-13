package es.oaw.wcagem;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.io.FileInputStream;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import es.inteco.common.properties.PropertiesManager;

/**
 * The Class WcagOdsUtils.
 * 
 * Generates an ods
 * 
 */
public final class WcagXlsxUtils {
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
	 * Generate ods.
	 *
	 * @param report the report
	 * @return the spread sheet
	 * @throws Exception the exception
	 */
	public static Workbook generateXlsx(final WcagEmReport report) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		File inputFile = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.xlsx.template"));
		FileInputStream inputStream = new FileInputStream(inputFile);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		// Load template
		final Sheet sheetAmbit = workbook.getSheet("01.Definición de ámbito");
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyy");
		CellReference ref = new CellReference("C35");
		Row r = sheetAmbit.getRow(ref.getRow());
		Cell c = r.getCell(ref.getCol());
		c.setCellValue(fmt.format(OffsetDateTime.now(ZoneId.of("Europe/Madrid"))));
		// filt techs
		final Sheet sheetTech = workbook.getSheet("02.Tecnologías");
		ref = new CellReference("C9");
		r = sheetTech.getRow(ref.getRow());
		c = r.getCell(ref.getCol());
		c.setCellValue("Sí");
		ref = new CellReference("C12");
		r = sheetTech.getRow(ref.getRow());
		c = r.getCell(ref.getCol());
		c.setCellValue("Sí");
		ref = new CellReference("C13");
		r = sheetTech.getRow(ref.getRow());
		c = r.getCell(ref.getCol());
		c.setCellValue("Sí");
		int resultsProcessed = 0;
		// Fill webpages
		Sheet sampleSheet = workbook.getSheet("03.Muestra");
		int initRow = 8; // Initial rowcount
		final List<Webpage> webpageList = report.getGraph().get(0).getStructuredSample().getWebpage();
		final int totalPages = webpageList.size();
		fillNotTell(workbook, totalPages < MAX_PAGES ? totalPages : MAX_PAGES);
		for (Webpage webpage : webpageList) {
			if (resultsProcessed < MAX_PAGES) {
				resultsProcessed++;
				sampleSheet.getRow(initRow - 1).getCell(2).setCellValue(webpage.getTitle());
				sampleSheet.getRow(initRow - 1).getCell(3).setCellValue("");
				sampleSheet.getRow(initRow - 1).getCell(4).setCellValue(webpage.getSource());
				initRow++;
			}
		}
		// Fill results
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
					fillResult(sheetP1, auditResult, 19);
					break;
				case "WCAG2:info-and-relationships":
					fillResult(sheetP1, auditResult, 247);
					break;
				case "WCAG2:orientation":
					fillResult(sheetP1, auditResult, 361);
					break;
				case "WCAG2:identify-input-purpose":
					fillResult(sheetP1, auditResult, 399);
					break;
				case "WCAG2:contrast-minimum":
					fillResult(sheetP1, auditResult, 513);
					break;
				case "WCAG2:reflow":
					fillResult(sheetP1, auditResult, 627);
					break;
				case "WCAG2:text-spacing":
					fillResult(sheetP1, auditResult, 703);
					break;
				// P2
				case "WCAG2:keyboard":
					fillResult(sheetP2, auditResult, 19);
					break;
				case "WCAG2:timing-adjustable":
					fillResult(sheetP2, auditResult, 133);
					break;
				case "WCAG2:pause-stop-hide":
					fillResult(sheetP2, auditResult, 171);
					break;
				case "WCAG2:three-flashes-or-below-threshold":
					fillResult(sheetP2, auditResult, 209);
					break;
				case "WCAG2:bypass-blocks":
					fillResult(sheetP2, auditResult, 247);
					break;
				case "WCAG2:page-titled":
					fillResult(sheetP2, auditResult, 285);
					break;
				case "WCAG2:focus-order":
					fillResult(sheetP2, auditResult, 323);
					break;
				case "WCAG2:link-purpose-in-context":
					fillResult(sheetP2, auditResult, 361);
					break;
				case "WCAG2:multiple-ways":
					fillResult(sheetP2, auditResult, 399);
					break;
				case "WCAG2:focus-visible":
					fillResult(sheetP2, auditResult, 475);
					break;
				case "WCAG2:label-in-name":
					fillResult(sheetP2, auditResult, 589);
					break;
				// P3
				case "WCAG2:language-of-page":
					fillResult(sheetP3, auditResult, 19);
					break;
				case "WCAG2:language-of-parts":
					fillResult(sheetP3, auditResult, 57);
					break;
				case "WCAG2:on-focus":
					fillResult(sheetP3, auditResult, 95);
					break;
				case "WCAG2:on-input":
					fillResult(sheetP3, auditResult, 133);
					break;
				case "WCAG2:consistent-navigation":
					fillResult(sheetP3, auditResult, 171);
					break;
				case "WCAG2:labels-or-instructions":
					fillResult(sheetP3, auditResult, 285);
					break;
				// p4
				case "WCAG2:parsing":
					fillResult(sheetP4, auditResult, 19);
					break;
				case "WCAG2:name-role-value":
					fillResult(sheetP4, auditResult, 57);
					break;
				default:
					break;
				}
			}
		}
		XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		// lock workbook
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			XSSFSheet tmpSheet = (workbook.getSheetAt(i));
			tmpSheet.protectSheet("oawxlsxpassword");
		}
		workbook.setWorkbookPassword("oawxlsxpassword", HashAlgorithm.sha512);
		workbook.lockStructure();
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
				final Cell cell = sheet.getRow(initRow - 1).getCell(3);
				cell.setCellFormula(null);
				cell.setCellValue(odsOutcome(hasPart.getResult().getOutcome()));
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
	private static void fillNotTell(final Workbook workbook, final int totalPages) {
		final Sheet sheetP1 = workbook.getSheet("P1.Perceptible");
		int tableRowIndex = 19;
		while (tableRowIndex <= 776) {
			for (int i = 0; i < totalPages; i++) {
				final Cell cell = sheetP1.getRow(i + tableRowIndex - 1).getCell(3);
//				cell.setCellFormula(null);
				cell.setCellValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
		}
		final Sheet sheetP2 = workbook.getSheet("P2.Operable");
		tableRowIndex = 19;
		while (tableRowIndex <= 661) {
			for (int i = 0; i < totalPages; i++) {
				final Cell cell = sheetP2.getRow(i + tableRowIndex - 1).getCell(3);
				cell.setCellFormula(null);
//				cell.setCellType(CellType.STRING);
				cell.setCellValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
		}
		final Sheet sheetP3 = workbook.getSheet("P3.Comprensible");
		tableRowIndex = 19;
		while (tableRowIndex <= 395) {
			for (int i = 0; i < totalPages; i++) {
				final Cell cell = sheetP3.getRow(i + tableRowIndex - 1).getCell(3);
				cell.setCellFormula(null);
//				cell.setCellType(CellType.STRING);
				cell.setCellValue("N/T");
			}
			tableRowIndex = tableRowIndex + 38;
		}
		final Sheet sheetP4 = workbook.getSheet("P4.Robusto");
		tableRowIndex = 19;
		while (tableRowIndex <= 129) {
			for (int i = 0; i < totalPages; i++) {
				final Cell cell = sheetP4.getRow(i + tableRowIndex - 1).getCell(3);
				cell.setCellFormula(null);
//				cell.setCellType(CellType.STRING);
				cell.setCellValue("N/T");
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
