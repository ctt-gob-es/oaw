package es.inteco.rastreador2.pdf.utils;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.dao.rastreo.FulFilledCrawling;
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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {

    private ZipUtils() {
    }

    private static void generateZip(String filePath, String zipDirectory, ZipOutputStream zos, boolean excludeZipFiles) throws Exception {

        File directory = new File(filePath);
        //Recuperamos la lista de archivos del directorio
        File[] directoryFiles = null;
        if (excludeZipFiles) {
            directoryFiles = directory.listFiles(new IntecoFileFilter("[^(\\.zip)]$"));
        } else {
            directoryFiles = directory.listFiles();
        }
        byte[] readBuffer = new byte[1024];
        int bytesIn = 0;

        //Recorremos los ficheros del directorio y los vamos metiendo en el zip
        for (int i = 0; i < directoryFiles.length; i++) {
            if (!directoryFiles[i].isDirectory()) {
                FileInputStream fis = null;
                try {
                    //Si el file es un fichero entonces se mete en el zip
                    fis = new FileInputStream(directoryFiles[i]);
                    ZipEntry entry = new ZipEntry(zipDirectory + directoryFiles[i].getName());
                    zos.putNextEntry(entry);
                    //Introducimos en el fichero del zip el contenido
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

        for (int i = 0; i < directoryFiles.length; i++) {
            if (directoryFiles[i].isDirectory()) {
                //Si el file es un directorio llamamos a la misma funcion recursivamente
                String newDirectory = zipDirectory + directoryFiles[i].getName() + File.separator;
                generateZip(directoryFiles[i].getPath(), newDirectory, zos, excludeZipFiles);
            }
        }
    }

    public static void generateZip(String directoryPath, String zipPath, boolean excludeZipFiles) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            File file = new File(zipPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            zos = new ZipOutputStream(fos);

            String zipDirectory = "";

            ZipUtils.generateZip(directoryPath, zipDirectory, zos, excludeZipFiles);
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
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Logger.putLog("Error al cerrar el fichero", ZipUtils.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }

    public static ActionForward pdfsZip(ActionMapping mapping, HttpServletResponse response, List<FulFilledCrawling> fulfilledCrawling, Long idObservatory, Long idExecutionOb, String initialPath) {
        Connection c = null;

        String executionPath = initialPath + idObservatory + File.separator + idExecutionOb + File.separator;
        String finalPath = executionPath + "temp" + File.separator;
        String finalZipPath = executionPath + Constants.ZIP_PDF_FILE;
        try {
            File directory = new File(executionPath);
            File[] directoryFiles = directory.listFiles();
            for (File file : directoryFiles) {
                String path = executionPath + file.getName() + File.separator;
                String zipPath = executionPath + "temp" + File.separator + file.getName() + ".zip";
                ZipUtils.generateZip(path, zipPath, false);
            }
            ZipUtils.generateZip(finalPath, finalZipPath, false);
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

}
