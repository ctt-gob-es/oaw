package es.inteco.crawler.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class StringUtils {
    // El string no está vacío?
    public static boolean isNotEmpty(String string) {
        return (string != null) && (string.trim().length() > 0);
    }

    // El string está vacío?
    public static boolean isEmpty(String string) {
        return (string == null || (string.trim().length() == 0));
    }

    // Quita la extensión del nombre de un archivo
    public static String removeFileExtension(String filename) {
        int indexDot = filename.indexOf('.');
        if (indexDot != -1) {
            filename = filename.substring(0, indexDot);
        }
        return filename;
    }

    // Comprueba si una cadena está formada únicamente por espacios en blanco
    public static boolean isOnlyBlanks(String string) {
        return (string.length() > 0 && (string.trim().length() == 0 || hasOnlyNbspEntities(string)));
    }

    // Comprueba si una cadena está formada únicamente por entidades &nbsp;
    public static boolean hasOnlyNbspEntities(String string) {
        return string.trim().replaceAll("&nbsp;", "").length() == 0;
    }

    // Normaliza un string
    public static String normalize(String string) {
        return string.toLowerCase().replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").
                replaceAll("ó", "o").replaceAll("ú", "u");
    }

    // Convierte un inputStream en un string
    public static String getContentAsString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static String removeHtmlTags(String htmlCode) {
        return htmlCode.replaceAll("<.*?>", "");
    }

    public static boolean isUrl(String string) {
        try {
            new URL(string);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * El validador CSS de la W3C devuelve un error en el XML que lo hace imposible de parsear. Este
     * método resolverá ese error. Consiste en sustituir el caracter "&" a secas por la entidad "&amp;"
     *
     * @param
     * @return
     * @throws Exception
     */
    public static InputStream fixBugInCssValidator(InputStream content) throws Exception {
        String text = StringUtils.getContentAsString(content);

        text = text.replaceAll("&", "&amp;");

        return new ByteArrayInputStream(text.getBytes());
    }
}
