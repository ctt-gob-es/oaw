package es.gob.oaw.rastreador2.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.util.List;

import org.apache.tika.io.FilenameUtils;

import es.inteco.common.logging.Logger;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.datos.CSSDTO;
import es.inteco.intav.persistence.Analysis;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.utils.FileUtils;

/**
 * Clase para manejar los ficheros fuente (HTML y CSS) de una página.
 */
public class SourceFilesManager {
	/** The parent dir. */
	private final File parentDir;
	/** The sources file. */
	private final File sourcesFile;

	/**
	 * Crea una instancia que guardará los ficheros en el directorio que se indica.
	 *
	 * @param parentDir el directorio donde se crearán los ficheros con el código fuente.
	 */
	public SourceFilesManager(final File parentDir) {
		this.parentDir = parentDir;
		this.sourcesFile = new File(parentDir, "codigo_fuente.zip");
	}

	/**
	 * Comprueba si existe el fichero zip que contiene el código fuente.
	 *
	 * @return true si existe el fichero zip o false en caso contrario.
	 */
	public boolean existsSourcesZip() {
		return sourcesFile.exists();
	}

	/**
	 * Escribe en el directorio en el que está inicializado los ficheros de una evaluación de un portal.
	 *
	 * @param c             conexión a la base de datos donde está guardado el código fuente.
	 * @param evaluationIds lista con los identificadores de una evaluación de los que se guardará el código fuente.
	 */
	public void writeSourceFiles(final Connection c, final List<Long> evaluationIds) {
		int index = 1;
		for (Long evaluationId : evaluationIds) {
			final File pageSourcesDirectory = new File(parentDir, "paginas/" + index);
			if (!pageSourcesDirectory.mkdirs()) {
				Logger.putLog("No se ha podido crear el directorio sources - " + pageSourcesDirectory.getAbsolutePath(), PdfGeneratorThread.class, Logger.LOG_LEVEL_ERROR);
			}
			try (PrintWriter fw = new PrintWriter(new FileWriter(new File(pageSourcesDirectory, "references.txt"), true))) {
				final Analysis analysis = AnalisisDatos.getAnalisisFromId(c, evaluationId);
				final File htmlTempFile = File.createTempFile("oaw_", "_html.html", pageSourcesDirectory);
				fw.println(writeTempFile(htmlTempFile, analysis.getSource(), analysis.getUrl()));
				final List<CSSDTO> cssResourcesFromEvaluation = AnalisisDatos.getCSSResourcesFromEvaluation(evaluationId);
				for (CSSDTO cssdto : cssResourcesFromEvaluation) {
					final File stylesheetTempFile = createCSSTempFile(cssdto.getUrl(), pageSourcesDirectory);
					fw.println(writeTempFile(stylesheetTempFile, cssdto.getCodigo(), cssdto.getUrl()));
				}
				index++;
				fw.flush();
			} catch (IOException e) {
				Logger.putLog("Exception al intentar guardar el código fuente", SourceFilesManager.class, Logger.LOG_LEVEL_ERROR, e);
			}
		}
	}

	/**
	 * Write source files content.
	 *
	 * @param c             the c
	 * @param evaluationIds the evaluation ids
	 */
	@SuppressWarnings("deprecation")
	public void writeSourceFilesContent(final Connection c, final List<Long> evaluationIds, final String originalFilename) {
		for (Long evaluationId : evaluationIds) {
			final File pageSourcesDirectory = new File(parentDir, "codigo_fuente");
			if (!pageSourcesDirectory.mkdirs()) {
				Logger.putLog("No se ha podido crear el directorio sources - " + pageSourcesDirectory.getAbsolutePath(), PdfGeneratorThread.class, Logger.LOG_LEVEL_ERROR);
			}
			try {
				final Analysis analysis = AnalisisDatos.getAnalisisFromId(c, evaluationId);
				final File sourceCode = new File(pageSourcesDirectory + "/" + originalFilename);
				org.apache.commons.io.FileUtils.writeStringToFile(sourceCode, analysis.getSource());
			} catch (IOException e) {
				Logger.putLog("Exception al intentar guardar el código fuente", SourceFilesManager.class, Logger.LOG_LEVEL_ERROR, e);
			}
		}
	}

	/**
	 * Creates the CSS temp file.
	 *
	 * @param filename             the filename
	 * @param pageSourcesDirectory the page sources directory
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected File createCSSTempFile(final String filename, final File pageSourcesDirectory) throws IOException {
		try {
			return File.createTempFile("oaw_", "_" + getURLFileName(filename, "css.css"), pageSourcesDirectory);
		} catch (IOException ioe) {
			return File.createTempFile("oaw_", "_css.css", pageSourcesDirectory);
		}
	}

	/**
	 * Gets the URL file name.
	 *
	 * @param url          the url
	 * @param defaultValue the default value
	 * @return the URL file name
	 */
	private String getURLFileName(final String url, final String defaultValue) {
		try {
			// Utilizamos unicamente la parte del path para generar un nombre único y a su vez identificativo.
			final String fileName = FilenameUtils.getName(FilenameUtils.normalize(new URL(url).getPath()));
			return fileName.isEmpty() ? defaultValue : fileName;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Write temp file.
	 *
	 * @param tempFile the temp file
	 * @param source   the source
	 * @param url      the url
	 * @return the string
	 * @throws FileNotFoundException the file not found exception
	 */
	private String writeTempFile(final File tempFile, final String source, final String url) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(tempFile)) {
			writer.print(source);
			writer.flush();
		}
		return tempFile.getName() + " --> " + url;
	}

	/**
	 * Comprime los ficheros con el código fuente en un archivo zip.
	 *
	 * @param deleteFiles flag para indicar si se deben borrar los ficheros una vez comprimidos
	 */
	public void zipSources(final boolean deleteFiles) {
		ZipUtils.generateZipFile(parentDir.toString() + "/paginas", parentDir.toString() + "/codigo_fuente.zip", true);
		if (deleteFiles) {
			FileUtils.deleteDir(new File(parentDir, "paginas"));
		}
	}

	public void zipSourcesContent(final boolean deleteFiles) {
		ZipUtils.generateZipFile(parentDir.toString() + "/codigo_fuente", parentDir.toString() + "/codigo_fuente.zip", true);
		if (deleteFiles) {
			FileUtils.deleteDir(new File(parentDir, "paginas"));
		}
	}
}
