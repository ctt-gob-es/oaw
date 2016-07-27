package es.inteco.utils;

import es.inteco.common.logging.Logger;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param dir - directory to delete recursively
     * @return true if all deletions were successful or false if a deletion fails and then the method returns inmediately
     */
    public static boolean deleteDir(final File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                final String[] children = dir.list();
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            // The directory is now empty so delete it
            return dir.delete();
        } else {
            return false;
        }
    }

    public static List<String> listContents(String contentPath) {
        List<String> contents = new ArrayList<>();

        FileInputStream fstream = null;
        DataInputStream in = null;
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(contentPath);
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                contents.add(strLine);
            }
            return contents;
        } catch (Exception e) {
            Logger.putLog("Error al recuperar los contenidos del fichero " + contentPath, FileUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        } finally {
            closeFile(fstream, in, br);
        }
    }

    public static boolean writeContents(String contentPath, String[] contents) {
        return writeContents(contentPath, Arrays.asList(contents));
    }

    public static boolean writeContents(String contentPath, List<String> contents) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(contentPath, false);

            for (String content : contents) {
                writer.write(content + "\n");
            }

            return true;
        } catch (Exception e) {
            Logger.putLog("Error al guardar los contenidos en un fichero", FileUtils.class, Logger.LOG_LEVEL_ERROR, e);
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    Logger.putLog("Excepción: ", FileUtils.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }
        }
    }

    private static void closeFile(FileInputStream fstream, DataInputStream in, BufferedReader br) {
        if (fstream != null) {
            try {
                fstream.close();
            } catch (Exception e) {
                Logger.putLog("Error al intentar cerrar el objeto fstream", FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                Logger.putLog("Error al intentar cerrar el objeto in", FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        }
        if (br != null) {
            try {
                br.close();
            } catch (Exception e) {
                Logger.putLog("Error al intentar cerrar el objeto br", FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        }
    }

    public static List<String> getDomainsFromCrawlDb(String crawlDbPath) {
        List<String> domains = new ArrayList<>();

        FileReader reader = null;
        BufferedReader bfReader = null;
        try {
            reader = new FileReader(crawlDbPath);
            bfReader = new BufferedReader(reader);
            StringBuilder fileStr = new StringBuilder();

            String line;
            while ((line = bfReader.readLine()) != null) {
                fileStr.append(line);
            }

            String regexp = "(https*://[a-zA-Z\\./0-9%-]+)";

            Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

            Matcher matcher = pattern.matcher(fileStr);
            while (matcher.find()) {
                domains.add(matcher.group(1));
            }
        } catch (IOException e) {
            Logger.putLog("No se ha podido leer la base de datos del Crawler", FileUtils.class, Logger.LOG_LEVEL_ERROR);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar el objeto FileReader", FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }

            try {
                if (bfReader != null) {
                    bfReader.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar el objeto BufferedReader", FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        }

        return domains;
    }

    public static void moveFile(String dirFrom, String dirTo, String filename) {
        File file = new File(dirFrom, filename);
        File dirToFile = new File(dirTo);
        if (!dirToFile.exists()) {
            if (!dirToFile.mkdirs()) {
                Logger.putLog(String.format("Error al crear los directorios en la operación moveFile del fichero %s desde %s a %s", filename, dirFrom, dirTo), FileUtils.class, Logger.LOG_LEVEL_ERROR);
            }
        }
        if (!file.renameTo(new File(dirTo, filename))) {
            Logger.putLog(String.format("Error al mover el fichero en la operación moveFile de %s desde %s a %s", filename, dirFrom, dirTo), FileUtils.class, Logger.LOG_LEVEL_ERROR);
        }
    }

    public static void removeFile(String tablePath) {
        File file = new File(tablePath);
        if (file.exists()) {
            FileUtils.deleteDir(file);
        }
    }

    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                if (!targetLocation.mkdirs()) {
                    Logger.putLog(String.format("Error al crear los directorios en la operación copyDirectory de %s a %s", sourceLocation, targetLocation), FileUtils.class, Logger.LOG_LEVEL_ERROR);
                }
            }
            String[] children = sourceLocation.list();
            for (String child : children) {
                copyDirectory(new File(sourceLocation, child), new File(targetLocation, child));
            }
        } else {
            if (!targetLocation.exists()) {
                if (!targetLocation.createNewFile()) {
                    Logger.putLog(String.format("Error al crear el fichero de destino copyDirectory de %s a %s", sourceLocation, targetLocation), FileUtils.class, Logger.LOG_LEVEL_ERROR);
                }
            }
            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(sourceLocation).getChannel();
                destination = new FileOutputStream(targetLocation).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        }
    }

    public static File openDirectory(String name) {
        File file = new File(name);
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

    public static String normalizeFileName(String s) {
        s = s.replaceAll("[èéêë]", "e");
        s = s.replaceAll("[ûùú]", "u");
        s = s.replaceAll("[ïîí]", "i");
        s = s.replaceAll("[àâá]", "a");
        s = s.replaceAll("[ôóò]", "o");
        s = s.replaceAll("[ñ]", "n");


        s = s.replaceAll("[ÈÉÊË]", "E");
        s = s.replaceAll("[ÛÙÚ]", "U");
        s = s.replaceAll("[ÏÎÍ]", "I");
        s = s.replaceAll("[ÀÂÁ]", "A");
        s = s.replaceAll("[ÔÓÒ]", "O");
        s = s.replaceAll("[Ñ]", "N");

        s = s.replaceAll("[ ]", "_");
        s = s.replaceAll("[.,]", "");
        s = s.replaceAll("\\s", "");

        return s;
    }
}
