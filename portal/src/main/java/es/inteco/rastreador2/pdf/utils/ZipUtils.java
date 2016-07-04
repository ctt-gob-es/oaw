package es.inteco.rastreador2.pdf.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
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
import java.io.IOException;
import java.sql.Connection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {

    private ZipUtils() {
    }

    public static ActionForward pdfsZip(final ActionMapping mapping, final HttpServletResponse response, final Long idObservatory, final Long idExecutionOb, final String initialPath) {
        final String executionPath = initialPath + idObservatory + File.separator + idExecutionOb + File.separator;
        final String finalPath = executionPath + "temp" + File.separator;
        final String finalZipPath = executionPath + Constants.ZIP_PDF_FILE;

        Connection c = null;
        try {
            final File directory = new File(executionPath);
            final File[] directoryFiles = directory.listFiles();
            for (File file : directoryFiles) {
                final String path = executionPath + file.getName() + File.separator;
                final String zipPath = executionPath + "temp" + File.separator + file.getName() + ".zip";
                generateZip(path, zipPath, false);
            }
            generateZip(finalPath, finalZipPath, false);
            FileUtils.removeFile(initialPath + idObservatory + File.separator + idExecutionOb + File.separator + "temp" + File.separator);
            CrawlerUtils.returnFile(finalZipPath, response, "application/zip", true);
        } catch (Exception e) {
            Logger.putLog("Exception: ", ExportAction.class, Logger.LOG_LEVEL_ERROR, e);
            return mapping.findForward(Constants.ERROR);
        } finally {
            DataBaseManager.closeConnection(c);
        }

        return null;
    }

    public static void generateZip(final String directoryPath, final String zipPath, boolean excludeZipFiles) {
        ZipOutputStream zos = null;
        try {
            final File file = new File(zipPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            zos = new ZipOutputStream(new FileOutputStream(file));

            generateZip(directoryPath, "", zos, excludeZipFiles);
            zos.flush();
            zos.finish();
        } catch (Exception e) {
            Logger.putLog("Excepción al crear el zip", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    Logger.putLog("Error al cerrar el fichero", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }

    private static void generateZip(final String filePath, final String zipDirectory, final ZipOutputStream zos, final boolean excludeZipFiles) throws Exception {
        final File directory = new File(filePath);
        //Recuperamos la lista de archivos del directorio
        final File[] directoryFiles;
        if (excludeZipFiles) {
            directoryFiles = directory.listFiles(new IntecoFileFilter("[^(\\.zip)]$"));
        } else {
            directoryFiles = directory.listFiles();
        }
        final byte[] readBuffer = new byte[1024];
        int bytesIn = 0;

        //Recorremos los ficheros del directorio y los vamos metiendo en el zip
        for (File file : directoryFiles) {
            if (!file.isDirectory()) {
                FileInputStream fis = null;
                try {
                    // Si el file es un fichero entonces se mete en el zip
                    fis = new FileInputStream(file);
                    final ZipEntry entry = new ZipEntry(zipDirectory + file.getName());
                    zos.putNextEntry(entry);
                    // Copiamos en el fichero zip el contenido
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                    zos.closeEntry();
                } catch (Exception e) {
                    Logger.putLog("Error al crear el zip", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            Logger.putLog("Error al cerrar el fichero", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
                        }
                    }
                }
            }
        }

        for (File directoryFile : directoryFiles) {
            if (directoryFile.isDirectory()) {
                //Si el file es un directorio llamamos a la misma funcion recursivamente
                final String newDirectory = zipDirectory + directoryFile.getName() + File.separator;
                generateZip(directoryFile.getPath(), newDirectory, zos, excludeZipFiles);
            }
        }
    }

}
