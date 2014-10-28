package es.ctic.language;

import java.io.InputStream;
import java.util.*;

/**
 *
 */
public final class Diccionario {

    private static final Map<String, Set<String>> diccionario = new HashMap<String, Set<String>>();

    static {
        Diccionario.init();
    }

    private static void init() {
        final InputStream is = Diccionario.class.getClassLoader().getResourceAsStream("languages/words_en.txt");
        final Scanner scanner = new Scanner(is);
        final Set<String> words = new HashSet<String>();
        while (scanner.hasNext()) {
            words.add(scanner.next());
        }
        scanner.close();
        diccionario.put("en", words);
    }

    private Diccionario() {
    }

    public static boolean containsWord(final String language, final String word) {
        final Set<String> words = diccionario.get(language);
        return words != null && words.contains(word);
    }

}
