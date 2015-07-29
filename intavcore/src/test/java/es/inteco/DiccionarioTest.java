package es.inteco;

import es.ctic.language.Diccionario;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;

/**
 *
 */
public class DiccionarioTest {

    @Test
    public void testDiccionarioIsInitialized() throws Exception {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream("words_en.properties");
        final URL url = this.getClass().getClassLoader().getResource("words_en.properties");

        final InputStream is2 = this.getClass().getClassLoader().getResourceAsStream("/words_en.properties");
        final URL url2 = this.getClass().getClassLoader().getResource("/words_en.properties");

        final InputStream is3 = this.getClass().getClassLoader().getResourceAsStream("/languages/words_en.txt");
        final URL url3 = this.getClass().getClassLoader().getResource("/languages/words_en.txt");

        final InputStream is4 = this.getClass().getClassLoader().getResourceAsStream("languages/words_en.txt");
        final URL url4 = this.getClass().getClassLoader().getResource("languages/words_en.txt");

        final InputStream is5 = this.getClass().getClassLoader().getResourceAsStream("es/ctic/language/words_en.properties");
        final URL url5 = this.getClass().getClassLoader().getResource("es/ctic/language/words_en.properties");

        final InputStream is6 = this.getClass().getClassLoader().getResourceAsStream("/es/ctic/language/words_en.properties");
        final URL url6 = this.getClass().getClassLoader().getResource("/es/ctic/language/words_en.properties");

        final InputStream is7 = this.getClass().getClassLoader().getResourceAsStream("es.ctic.language.words_en.properties");
        final URL url7 = this.getClass().getClassLoader().getResource("es.ctic.language.words_en.properties");

        final InputStream is8 = Diccionario.class.getClassLoader().getResourceAsStream("./es/ctic/language/words_en.properties");
        final URL url8 = Diccionario.class.getClassLoader().getResource("./words_en.properties");
    }

}
