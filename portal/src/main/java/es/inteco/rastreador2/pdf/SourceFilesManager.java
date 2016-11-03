package es.inteco.rastreador2.pdf;

import es.inteco.common.logging.Logger;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.intav.datos.CSSDTO;
import es.inteco.intav.persistence.Analysis;
import es.inteco.rastreador2.pdf.utils.ZipUtils;
import es.inteco.utils.FileUtils;
import org.apache.tika.io.FilenameUtils;

import java.io.*;
import java.sql.Connection;
import java.util.List;

/**
 * Clase para manejar los ficheros fuente (HTML y CSS) de una página
 */
public class SourceFilesManager {

    private final File parentDir;
    private final File sourcesFile;

    /**
     * Crea una instancia que guardará los ficheros en el directorio que se indica.
     *
     * @param parentDir el directorio donde se crearán los ficheros con el código fuente.
     */
    public SourceFilesManager(final File parentDir) {
        this.parentDir = parentDir;
        this.sourcesFile = new File(parentDir, "sources.zip");
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
            final File pageSourcesDirectory = new File(parentDir, "sources/" + index);
            if (!pageSourcesDirectory.mkdirs()) {
                Logger.putLog("No se ha podido crear el directorio sources - " + pageSourcesDirectory.getAbsolutePath(), PdfGeneratorThread.class, Logger.LOG_LEVEL_ERROR);
            }
            try (PrintWriter fw = new PrintWriter(new FileWriter(new File(pageSourcesDirectory, "references.txt"), true))) {
                final Analysis analysis = AnalisisDatos.getAnalisisFromId(c, evaluationId);
                final File htmlTempFile = File.createTempFile("oaw_", "_" + getURLFileName(analysis.getUrl(), "html.html"), pageSourcesDirectory);
                fw.println(writeTempFile(htmlTempFile, analysis.getSource(), analysis.getUrl()));
                final List<CSSDTO> cssResourcesFromEvaluation = AnalisisDatos.getCSSResourcesFromEvaluation(evaluationId);
                for (CSSDTO cssdto : cssResourcesFromEvaluation) {
                    final File stylesheetTempFile = File.createTempFile("oaw_", "_" + getURLFileName(cssdto.getUrl(), "css.css"), pageSourcesDirectory);
                    fw.println(writeTempFile(stylesheetTempFile, cssdto.getCodigo(), cssdto.getUrl()));
                }
                index++;
                fw.flush();
            } catch (IOException e) {
                Logger.putLog("Exception al intentar guardar el código fuente", SourceFilesManager.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    private String getURLFileName(final String url, final String defaultValue) {
        final String fileName = FilenameUtils.getName(FilenameUtils.normalize(url));
        return fileName.isEmpty() ? defaultValue : fileName;
    }

    private String writeTempFile(final File tempFile, final String source, final String url) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.print(source);
            writer.flush();
        }
        return tempFile.getName() + " --> " + url;
    }

    /**
     * Comprime los ficheros con el código fuente en un archivo zip
     *
     * @param deleteFiles flag para indicar si se deben borrar los ficheros una vez comprimidos
     */
    public void zipSources(boolean deleteFiles) {
        ZipUtils.generateZipFile(parentDir.toString() + "/sources", parentDir.toString() + "/sources.zip", true);
        if (deleteFiles) {
            FileUtils.deleteDir(new File(parentDir, "sources"));
        }
    }
}
