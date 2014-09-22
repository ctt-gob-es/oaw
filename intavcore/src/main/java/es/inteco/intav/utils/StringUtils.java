package es.inteco.intav.utils;

import es.inteco.common.IntavConstants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {
    }

    // El string no está vacío?
    public static boolean isNotEmpty(String string) {
        return (string != null) && (string.trim().length() > 0);
    }

    // El string está vacío?
    public static boolean isEmpty(String string) {
        return (string == null || (string.trim().length() == 0));
    }

    public static boolean isOnlyWhiteChars(String string) {
        byte[] bytes = string.getBytes();
        for (byte aByte : bytes) {
            boolean isWhite = false;
            for (byte whiteCharsByte : IntavConstants.WHITE_CHARS_BYTE) {
                if (whiteCharsByte == aByte) {
                    isWhite = true;
                }
            }
            if (!isWhite) {
                return false;
            }
        }
        return true;
    }

    // Quita la extensión del nombre de un archivo
    /*public static String removeFileExtension(String filename) {
        int indexDot = filename.indexOf('.');
        if (indexDot != -1) {
            filename = filename.substring(0, indexDot);
        }
        return filename;
    }*/

    // Comprueba si una cadena está formada únicamente por espacios en blanco
    public static boolean isOnlyBlanks(String string) {
        return (string.length() > 0 && (string.trim().length() == 0 || hasOnlyNbspEntities(string)));
    }

    // Cuando en el html detecta un &nbsp;, en lugar de devolver el caracter vacío devuelve un caracter con
    // código -96
    public static boolean hasOnlyNbspEntities(String string) {
        byte[] bytes = string.trim().getBytes();
        for (byte aByte : bytes) {
            boolean isNbsp = false;
            for (byte nbspByte : IntavConstants.NBSP_BYTE) {
                if (nbspByte == aByte) {
                    isNbsp = true;
                }
            }
            if (!isNbsp) {
                return false;
            }
        }

        return true;
    }

    // Comprueba si una cadena está formada únicamente por entidades &nbsp;
    /*public static boolean hasOnlyNbspEntities(String string) {
        return string.trim().replaceAll("&nbsp;", "").length() == 0;
	}*/

    // Normaliza un string
    /*public static String normalize(String string) {
        return string.toLowerCase().replaceAll("á", "a").replaceAll("é", "e").replaceAll("í", "i").
                replaceAll("ó", "o").replaceAll("ú", "u");
    }*/

    // Convierte un inputStream en un string
    public static String getContentAsString(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        int n;
        while ((n = in.read(b)) != -1) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    // Convierte un inputStream en un string
    public static String getContentAsString(InputStream in, String charset) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        int n;
        while ((n = in.read(b)) != -1) {
            out.append(new String(b, 0, n, charset));
        }
        return out.toString();
    }

    public static String removeHtmlTags(String htmlCode) {
        return htmlCode != null ? htmlCode.replaceAll("<.*?>", "") : "";
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
     * @param content
     * @return
     * @throws IOException
     */
    public static InputStream fixBugInCssValidator(InputStream content) throws IOException {
        String text = StringUtils.getContentAsString(content);

        text = text.replaceAll("&", "&amp;");

        return new ByteArrayInputStream(text.getBytes());
    }

    public static boolean textMatchs(String text, String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

    public static String truncateText(String text, int numChars) {
        if (text != null && text.length() > numChars) {
            return text.substring(0, numChars - 1) + " ...";
        } else {
            return text;
        }
    }
}
