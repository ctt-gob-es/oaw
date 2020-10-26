package es.inteco.rastreador2.action.semillas;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.actionform.semillas.SemillaForm;
import org.apache.commons.digester.Digester;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities to read/write seeds from/to an xlsx file
 * */
public final class SeedExcelUtils {

	private SeedExcelUtils() {
	}

	/**
	 * Enumeration to define column properties in the excel file (column_name, column_number )
	 */
	private enum SeedExcelColumns {
		ID("id", 0),
		NOMBRE("nombre", 1),
		ACTIVA("activa", 2),
		ELIMINADA("eliminada", 3),
		URLS("url", 4),
		ACRONIMO("acronimo", 5),
		DEPENDENCIA("depende_de", 6),
		DIRECTORIO("en_directorio", 7),
		SEGMENTO("segmento", 8),
		AMBITO("ambito", 9),
		COMPLEJIDAD("complejidad", 10),
		ETIQUETAS_TEMATICA("tematica", 11),
		ETIQUETAS_DISTRIBUCION("distribucion", 12),
		ETIQUETAS_RECURRENCIA("recurrencia", 13),
		ETIQUETAS_OTROS("otros", 14),
		OBSERVACIONES("observaciones", 15);

		private final String columnName;
		private final int columnId;

		SeedExcelColumns(String name, int id){
			this.columnName = name;
			this.columnId = id;
		}
	}

	/**
	 * Simple class to store cell information
	 */
	private static class CellInfo {
		SeedExcelColumns columnInfo;
		String value;

		public CellInfo(SeedExcelColumns column, String value){
			this.columnInfo = column;
			this.value = value;
		}
	}

	/**
	 * Generates a list of excel cell info from a seed, parsing all its properties.
	 * Each cell contains its assigned column and its value
	 * @param seed Seed to transform
	 * @return List of cell info
	 */
	public static List<CellInfo> getSeedCellsInfo(SemillaForm seed){
		List<CellInfo> cells = new ArrayList<>();

		String id =String.valueOf(seed.getId());
		cells.add(new CellInfo(SeedExcelColumns.ID, id));

		String name =seed.getNombre();
		cells.add(new CellInfo(SeedExcelColumns.NOMBRE, name));

		String activa = String.valueOf(seed.isActiva());
		cells.add(new CellInfo(SeedExcelColumns.ACTIVA, activa));

		String eliminada = String.valueOf(seed.isEliminar());
		cells.add(new CellInfo(SeedExcelColumns.ELIMINADA, eliminada));

		String urlsFormatted = "";
		if (seed.getListaUrlsString() != null) {
			urlsFormatted = seed.getListaUrlsString().replace(";", "\n");
		}
		cells.add(new CellInfo(SeedExcelColumns.URLS, urlsFormatted));

		String acronimo =  (seed.getAcronimo() != null) ? seed.getAcronimo() : "";
		cells.add(new CellInfo(SeedExcelColumns.ACRONIMO, acronimo));

		String dependenciasFormatted = "";
		if (seed.getDependencias() != null){
			for (DependenciaForm dep : seed.getDependencias()) {
				dependenciasFormatted += dep.getName() + "\n";
			}
			dependenciasFormatted = StringUtils.removeLastChar(dependenciasFormatted);
		}
		cells.add(new CellInfo(SeedExcelColumns.DEPENDENCIA, dependenciasFormatted));

		String directory = String.valueOf(seed.isInDirectory());
		cells.add(new CellInfo(SeedExcelColumns.DIRECTORIO, directory));

		String category = "";
		if (seed.getCategoria() != null && seed.getCategoria().getName() != null){
			category = seed.getCategoria().getName();
		}
		cells.add(new CellInfo(SeedExcelColumns.SEGMENTO, category));

		String ambito = "";
		if (seed.getAmbito() != null && seed.getAmbito().getName() != null) {
			ambito = seed.getAmbito().getName();
		}
		cells.add(new CellInfo(SeedExcelColumns.AMBITO, ambito));

		String complejidad = "";
		if (seed.getComplejidad() != null && seed.getComplejidad().getName() != null) {
			complejidad = seed.getComplejidad().getName();
		}
		cells.add(new CellInfo(SeedExcelColumns.COMPLEJIDAD, complejidad));

		List<EtiquetaForm> etiquetas = seed.getEtiquetas();
		String tematica, distribucion, recurrencia, otros;
		tematica = distribucion = recurrencia = otros = "";
		if (etiquetas != null && !etiquetas.isEmpty()) {
			for (EtiquetaForm tmp: etiquetas) {
				if (tmp.getClasificacion() != null) {
					switch (tmp.getClasificacion().getId()) {
						case "1":
							tematica += tmp.getName() + "\n";
							break;
						case "2":
							distribucion += tmp.getName() + "\n";
							break;
						case "3":
							recurrencia += tmp.getName() + "\n";
							break;
						case "4":
							otros += tmp.getName() + "\n";
							break;
						default:
							break;
					}
				}
			}
			//remove remaining line breaks
			tematica = StringUtils.removeLastChar(tematica);
			distribucion = StringUtils.removeLastChar(distribucion);
			recurrencia = StringUtils.removeLastChar(recurrencia);
			otros = StringUtils.removeLastChar(otros);
		}

		cells.add(new CellInfo(SeedExcelColumns.ETIQUETAS_TEMATICA, tematica));
		cells.add(new CellInfo(SeedExcelColumns.ETIQUETAS_DISTRIBUCION, distribucion));
		cells.add(new CellInfo(SeedExcelColumns.ETIQUETAS_RECURRENCIA, recurrencia));
		cells.add(new CellInfo(SeedExcelColumns.ETIQUETAS_OTROS, otros));

		String observaciones = seed.getObservaciones() != null ? seed.getObservaciones() : "";
		cells.add(new CellInfo(SeedExcelColumns.OBSERVACIONES, observaciones));
		return cells;
	}

	/**
	 * Generates an .xlsx file from a list of seeds and  writes it on the
	 * servlet response
	 * @param response
	 * @param seeds
	 * @throws Exception
	 */
	public static void writeSeedsToXlsxResponse(HttpServletResponse response, List<SemillaForm> seeds) throws Exception {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Semillas");

		//create default cell style (aligned top left and allow line wrapping)
		CellStyle defaultStyle = wb.createCellStyle();
		defaultStyle.setWrapText(true);
		defaultStyle.setAlignment(HorizontalAlignment.LEFT);
		defaultStyle.setVerticalAlignment(VerticalAlignment.TOP);

		int rowIndex = 0;

		//first row contains column names
		Row row = sheet.createRow(rowIndex);
		for (SeedExcelColumns col: SeedExcelColumns.values()){
			row.createCell(col.columnId).setCellValue(col.columnName);
		}
		rowIndex++;

		for (SemillaForm seed : seeds) {
			List<CellInfo> cells = getSeedCellsInfo(seed);
			// Create row and cells based on cell info
			row = sheet.createRow(rowIndex);
			for (CellInfo info : cells){
				Cell cell = row.createCell(info.columnInfo.columnId);
				cell.setCellValue(info.value);
				cell.setCellStyle(defaultStyle);
			}
			rowIndex++;
		}

		// Increase width of columns to match content
		for (SeedExcelColumns col: SeedExcelColumns.values()){
			sheet.autoSizeColumn(col.columnId);
		}

		//Download file in browser
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=semillas.xlsx");
		wb.write(response.getOutputStream());
		wb.close();
	}

	/**
	 * Extracts a list of seeds from a .xlsx file encoded in an input stream
	 * @param inputStream .xlsx file in a stream
	 * @return List of decoded seeds
	 * @throws Exception
	 */
	public static List<SemillaForm> getSeedsFromXlsxFile(InputStream inputStream) throws Exception {
		try {
			List<SemillaForm> seeds = new ArrayList<>();
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet seedsSheet = workbook.getSheetAt(0);
			boolean firstRow = true;
			for (Row row : seedsSheet) {
				if (firstRow){
					firstRow = false;
					continue;
				}
				// if row has no name ignore
				if (row.getCell(SeedExcelColumns.NOMBRE.columnId) == null){
					continue;
				}

				SemillaForm seed = new SemillaForm();
				for (SeedExcelColumns col: SeedExcelColumns.values()){
					Cell cell = row.getCell(col.columnId);
					if (cell == null){
						continue;
					}
					String value = cell.getRichStringCellValue().getString();
					// big switch in case we need to do custom cell preprocessing for each column
					switch (col){
						case ID:
							seed.setIdStr(value);
							break;
						case NOMBRE:
							seed.setNombre(value);
							break;
						case ACTIVA:
							seed.setActivaStr(value);
							break;
						case ELIMINADA:
							seed.setEliminarStr(value);
							break;
						case URLS:
							seed.addListUrl(value);
							break;
						case ACRONIMO:
							seed.setAcronimo(value);
							break;
						case DEPENDENCIA:
							seed.addDependenciaPorNombre(value);
							break;
						case DIRECTORIO:
							seed.setInDirectoryStr(value);
							break;
						case SEGMENTO:
							seed.setCategoryName(value);
							break;
						case AMBITO:
							seed.setAmbitName(value);
							break;
						case COMPLEJIDAD:
							seed.setComplexityName(value);
							break;
						case ETIQUETAS_TEMATICA:
							seed.addEtiquetaTematica(value);
							break;
						case ETIQUETAS_DISTRIBUCION:
							seed.addEtiquetaDistribucion(value);
							break;
						case ETIQUETAS_RECURRENCIA:
							seed.addEtiquetaRecurrencia(value);
							break;
						case ETIQUETAS_OTROS:
							seed.addEtiquetaOtros(value);
							break;
						case OBSERVACIONES:
							seed.setObservaciones(value);
							break;
					}
				}
				seeds.add(seed);
			}
			return seeds;
		} catch (Exception e) {
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					Logger.putLog("Error al cerrar el InputStream", SeedUtils.class, Logger.LOG_LEVEL_ERROR, e);
				}
			}
		}
	}
}
