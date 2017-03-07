package es.inteco.rastreador2.pdf.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.pdf.ExportAction;
import es.inteco.rastreador2.utils.CrawlerUtils;
import es.inteco.rastreador2.utils.IntecoFileFilter;
import es.inteco.utils.FileUtils;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {

    private ZipUtils() {
    }

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
            FileUtils.removeFile(basePath + idObservatory + File.separator + idExecutionOb + File.separator + "temp" + File.separator);
            CrawlerUtils.returnFile(response, finalZipPath, "application/zip", true);
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        }

        return null;
    }

    public static void generateZipFile(final String directoryPath, final String zipPath, boolean excludeZipFiles) {
        final File file = new File(zipPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {
            generateZipEntries(directoryPath, "", zos, excludeZipFiles);
            zos.flush();
            zos.finish();
        } catch (Exception e) {
            Logger.putLog("Excepción al crear el zip", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void generateZipFile(final String directoryPath, final OutputStream outputStream, boolean excludeZipFiles) {
        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            generateZipEntries(directoryPath, "", zos, excludeZipFiles);
            zos.flush();
            zos.finish();
        } catch (Exception e) {
            Logger.putLog("Excepción al crear el zip", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    private static void generateZipEntries(final String filePath, final String zipDirectory, final ZipOutputStream zos, final boolean excludeZipFiles) throws Exception {
        final File directory = new File(filePath);
        //Recuperamos la lista de archivos del directorio
        final File[] directoryFiles;
        if (excludeZipFiles) {
            directoryFiles = directory.listFiles(new IntecoFileFilter("[^(\\.zip)]$"));
        } else {
            directoryFiles = directory.listFiles();
        }

        //Recorremos los ficheros del directorio y los vamos metiendo en el zip
        if (directoryFiles != null) {
            for (File file : directoryFiles) {
                if (!file.isDirectory()) {
                    // Si el file es un fichero entonces se mete en el zip
                    putZipEntry(file, zipDirectory, zos);
                }
            }

            for (File directoryFile : directoryFiles) {
                if (directoryFile.isDirectory()) {
                    //Si el file es un directorio llamamos a la misma funcion recursivamente
                    final String newDirectory = zipDirectory + directoryFile.getName() + File.separator;
                    generateZipEntries(directoryFile.getPath(), newDirectory, zos, excludeZipFiles);
                }
            }
        }
    }

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
