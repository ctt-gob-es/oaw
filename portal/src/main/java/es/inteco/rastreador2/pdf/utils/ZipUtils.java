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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.IntecoFileFilter;
import es.inteco.utils.FileUtils;

/**
 * The Class ZipUtils.
 */
public final class ZipUtils {
	/**
	 * Instantiates a new zip utils.
	 */
	private ZipUtils() {
	}

	/**
	 * Pdfs zip.
	 *
	 * @param mapping       the mapping
	 * @param response      the response
	 * @param idObservatory the id observatory
	 * @param idExecutionOb the id execution ob
	 * @param basePath      the base path
	 * @return the action forward
	 */
	public static ActionForward pdfsZip(final ActionMapping mapping, final HttpServletResponse response, final Long idObservatory, final Long idExecutionOb, final String basePath) {
		final String executionPath = basePath + idObservatory + File.separator + idExecutionOb + File.separator;
		final String finalPath = executionPath + "temp" + File.separator;
		final String finalZipPath = executionPath + Constants.ZIP_PDF_FILE;
		try {
			final File directory = new File(executionPath);
			final File[] directoryFiles = directory.listFiles();
			if (directoryFiles != null) {
				for (File file : directoryFiles) {
					final String path = executionPath + file.getName() + File.separator;
					final String zipPath = executionPath + "temp" + File.separator + file.getName() + ".zip";
					generateZipFile(path, zipPath, false);
				}
			}
			generateZipFile(finalPath, finalZipPath, false);
			// PENDING No eliminamos los temporales
			FileUtils.removeFile(basePath + idObservatory + File.separator + idExecutionOb + File.separator + "temp" + File.separator);
			CrawlerUtils.returnFile(response, finalZipPath, "application/zip", true);
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
			return mapping.findForward(Constants.ERROR);
		}
		return null;
	}

	/**
	 * Pdfs zip to list.
	 *
	 * @param idObservatory the id observatory
	 * @param idExecutionOb the id execution ob
	 * @param basePath      the base path
	 * @return the list
	 */
	public static List<String> pdfsZipToList(final Long idObservatory, final Long idExecutionOb, final String basePath) {
		List<String> zipFiles = new ArrayList<>();
		final String executionPath = basePath + idObservatory + File.separator + idExecutionOb + File.separator;
		try {
			final File directory = new File(executionPath);
			final File[] directoryFiles = directory.listFiles();
			if (directoryFiles != null) {
				for (File file : directoryFiles) {
					if (file.isDirectory()) {
						final String path = executionPath + file.getName() + File.separator;
						final String zipPath = executionPath + File.separator + file.getName() + ".zip";
						// final String zipPath = executionPath + "temp" + File.separator + file.getName() + ".zip";
						generateZipFile(path, zipPath, false);
						zipFiles.add(zipPath);
					}
				}
			}
			// NO generate zip with sirectoruies
			// generateZipFile(finalPath, finalZipPath, false);
			// PENDING No eliminamos los temporales
			// FileUtils.removeFile(basePath + idObservatory + File.separator + idExecutionOb + File.separator + "temp" + File.separator);
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return zipFiles;
	}

	/**
	 * Pdfs zip to map.
	 *
	 * @param idObservatory the id observatory
	 * @param idExecutionOb the id execution ob
	 * @param basePath      the base path
	 * @return the map
	 */
	public static Map<String, String> pdfsZipToMap(final Long idObservatory, final Long idExecutionOb, final String basePath) {
		Map<String, String> zipFiles = new HashMap<String, String>();
		final String executionPath = basePath + idObservatory + File.separator + idExecutionOb + File.separator;
		try {
			final File directory = new File(executionPath);
			final File[] directoryFiles = directory.listFiles();
			if (directoryFiles != null) {
				for (File file : directoryFiles) {
					if (file.isDirectory()) {
						final String path = executionPath + file.getName() + File.separator;
						final String zipPath = executionPath + File.separator + file.getName() + ".zip";
						// final String zipPath = executionPath + "temp" + File.separator + file.getName() + ".zip";
						generateZipFile(path, zipPath, false);
						zipFiles.put(file.getName(), zipPath);
					}
				}
			}
			// NO generate zip with sirectoruies
			// generateZipFile(finalPath, finalZipPath, false);
			// PENDING No eliminamos los temporales
			// FileUtils.removeFile(basePath + idObservatory + File.separator + idExecutionOb + File.separator + "temp" + File.separator);
		} catch (Exception e) {
			Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return zipFiles;
	}

	/**
	 * Generate zip file.
	 *
	 * @param directoryPath   the directory path
	 * @param zipPath         the zip path
	 * @param excludeZipFiles the exclude zip files
	 */
	public static void generateZipFile(final String directoryPath, final String zipPath, boolean excludeZipFiles) {
		final File file = new File(zipPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {
			zos.setLevel(Deflater.BEST_COMPRESSION);
			generateZipEntries(directoryPath, "", zos, excludeZipFiles);
			zos.flush();
			zos.finish();
		} catch (Exception e) {
			Logger.putLog("Excepción al crear el zip", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Generate zip file.
	 *
	 * @param directoryPath   the directory path
	 * @param outputStream    the output stream
	 * @param excludeZipFiles the exclude zip files
	 */
	public static void generateZipFile(final String directoryPath, final OutputStream outputStream, boolean excludeZipFiles) {
		try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
			zos.setLevel(Deflater.BEST_COMPRESSION);
			generateZipEntries(directoryPath, "", zos, excludeZipFiles);
			zos.flush();
			zos.finish();
		} catch (Exception e) {
			Logger.putLog("Excepción al crear el zip", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Generate zip entries.
	 *
	 * @param filePath        the file path
	 * @param zipDirectory    the zip directory
	 * @param zos             the zos
	 * @param excludeZipFiles the exclude zip files
	 * @throws Exception the exception
	 */
	private static void generateZipEntries(final String filePath, final String zipDirectory, final ZipOutputStream zos, final boolean excludeZipFiles) throws Exception {
		final File directory = new File(filePath);
		// Recuperamos la lista de archivos del directorio
		final File[] directoryFiles;
		if (excludeZipFiles) {
			directoryFiles = directory.listFiles(new IntecoFileFilter("[^(\\.zip)]$"));
		} else {
			directoryFiles = directory.listFiles();
		}
		// Recorremos los ficheros del directorio y los vamos metiendo en el zip
		if (directoryFiles != null) {
			for (File file : directoryFiles) {
				if (!file.isDirectory()) {
					// Si el file es un fichero entonces se mete en el zip
					putZipEntry(file, zipDirectory, zos);
				}
			}
			for (File directoryFile : directoryFiles) {
				if (directoryFile.isDirectory()) {
					// Si el file es un directorio llamamos a la misma funcion recursivamente
					final String newDirectory = zipDirectory + directoryFile.getName() + File.separator;
					generateZipEntries(directoryFile.getPath(), newDirectory, zos, excludeZipFiles);
				}
			}
		}
	}

	/**
	 * Put zip entry.
	 *
	 * @param file         the file
	 * @param zipDirectory the zip directory
	 * @param zos          the zos
	 */
	private static void putZipEntry(final File file, final String zipDirectory, final ZipOutputStream zos) {
		final byte[] readBuffer = new byte[1024];
		int bytesIn;
		try (FileInputStream fis = new FileInputStream(file)) {
			final ZipEntry entry = new ZipEntry(zipDirectory + file.getName());
			zos.putNextEntry(entry);
			// Copiamos en el fichero zip el contenido
			while ((bytesIn = fis.read(readBuffer)) != -1) {
				zos.write(readBuffer, 0, bytesIn);
			}
			zos.closeEntry();
		} catch (Exception e) {
			Logger.putLog("Error al crear el zip", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}
}
