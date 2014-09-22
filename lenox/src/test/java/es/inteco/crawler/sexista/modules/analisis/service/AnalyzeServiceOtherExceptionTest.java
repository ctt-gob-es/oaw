package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.modules.analisis.service.base.BaseTest;
import org.junit.Test;

/**
 * Otras Excepciones.
 */
public class AnalyzeServiceOtherExceptionTest extends BaseTest {

    @Test
    public void testAnalyze01() throws Exception {
        String texto = "Niños y niñas";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze02() throws Exception {
        String texto = "Niños, niñas, bebés y otros seres";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze03() throws Exception {
        String texto = "Señoras y señores diputados";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze04() throws Exception {
        String texto = "Los españoles consumen 1 litro de cerveza a la semana y las españolas medio litro";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze05() throws Exception {
        String texto = "Las españolas consumen 1 litro de cerveza a la semana y los españoles medio litro";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze06() throws Exception {
        String texto = "Los abogados y abogadas acusadores han...";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze07() throws Exception {
        String texto = "Los y las diputados"; // Termino Compuesto
        this.executeAndValidate(texto, "La diputado");
    }

    @Test
    public void testAnalyze08() throws Exception {
        String texto = "Las y los diputados";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze09() throws Exception {
        String texto = "Los atletas y las atletas españoles han ...";
        this.executeAndValidate(texto, "Español");
    }

    @Test
    public void testAnalyze10() throws Exception {
        String texto = "Los directivos españoles ganan una media del 15% más que las directivas";
        this.executeAndValidate(texto, "Español");
    }

    @Test
    public void testAnalyze11() throws Exception {
        String texto = "Los directivos españoles ganan una media del 15% más que sus homólogas";
        this.executeAndValidate(texto);
    }
}
