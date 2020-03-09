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
package es.inteco.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import es.inteco.common.logging.Logger;

public final class FileUtils {

    private FileUtils() {
    }

    public static void deleteDirs(final List<String> paths) {
        for (String path : paths) {
            Logger.putLog("Borrando el contenido del directorio: " + path, FileUtils.class, Logger.LOG_LEVEL_INFO);
            final File dir = new File(path);
            deleteDir(dir);
        }
    }

    /**
     * Deletes recursively all files and subdirectories
     *
     * @param dir - directory to delete recursively
     * @return true if all deletions were successful or false if a deletion fails and then the method returns inmediately
     */
    public static boolean deleteDir(final File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                final String[] children = dir.list();
                if (children != null) {
                    for (String child : children) {
                        boolean success = deleteDir(new File(dir, child));
                        if (!success) {
                            return false;
                        }
                    }
                }
            }
            // The directory is now empty so delete it
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void removeFile(final String tablePath) {
        final File file = new File(tablePath);
        if (file.exists()) {
            FileUtils.deleteDir(file);
        }
    }

    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                Logger.putLog(String.format("Error al crear los directorios en la operación copyDirectory de %s a %s", sourceLocation, targetLocation), FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
            final String[] children = sourceLocation.list();
            if (children != null) {
                for (String child : children) {
                    copyDirectory(new File(sourceLocation, child), new File(targetLocation, child));
                }
            }
        } else {
            if (!targetLocation.exists() && !targetLocation.createNewFile()) {
                Logger.putLog(String.format("Error al crear el fichero de destino copyDirectory de %s a %s", sourceLocation, targetLocation), FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }

            try (FileInputStream fis = new FileInputStream(sourceLocation);
                 FileOutputStream fos = new FileOutputStream(targetLocation);
                 FileChannel source = fis.getChannel();
                 FileChannel destination = fos.getChannel()) {
                destination.transferFrom(source, 0, source.size());
            }
        }
    }

    public static File openDirectory(final String name) {
        final File file = new File(name);
        if (!(name.endsWith("/"))) {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                Logger.putLog(String.format("Error al recuperar los contenidos del fichero %s", name), FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        } else {
            if (!file.exists() && !file.mkdirs()) {
                Logger.putLog(String.format("Error al recuperar los contenidos del directorio %s", name), FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        }
        return file;
    }

    /**
     * Replaces acute vocals and puntuaction characters for safe ones.
     *
     * @param originalFileName a string
     * @return the original file name where problematic characters are replaced by safe ones.
     */
    public static String normalizeFileName(final String originalFileName) {
    	
    	
    	
        return StringEscapeUtils.unescapeHtml(originalFileName)
                .replaceAll("[àâá]", "a")
                .replaceAll("[èéêë]", "e")
                .replaceAll("[ïîí]", "i")
                .replaceAll("[ôóò]", "o")
                .replaceAll("[ûùú]", "u")
                .replaceAll("[ñ]", "n")
                .replaceAll("[ÀÂÁ]", "A")
                .replaceAll("[ÈÉÊË]", "E")
                .replaceAll("[ÏÎÍ]", "I")
                .replaceAll("[ÔÓÒ]", "O")
                .replaceAll("[ÛÙÚ]", "U")
                .replaceAll("[Ñ]", "N")
                .replaceAll("[ ]", "_")
                .replaceAll("[.,]", "")
                .replaceAll("\\s", "");
    }
}
