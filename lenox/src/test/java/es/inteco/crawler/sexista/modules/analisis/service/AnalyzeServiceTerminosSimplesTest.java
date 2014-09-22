package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.modules.analisis.service.base.BaseTest;
import org.junit.Test;

/**
 * Localización de términos simples.
 */
public class AnalyzeServiceTerminosSimplesTest extends BaseTest {

    private String texto;
    private String[] terminosSexistas;

    @Test
    public void testAnalyze01() throws Exception {
        texto = "Los abogados españoles interponen recursos";
        terminosSexistas = new String[]{"Abogado", "Español"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze02() throws Exception {
        texto = "Los alumnos con calificación de notable...";
        terminosSexistas = new String[]{"Alumno"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze03() throws Exception {
        texto = "El español medio gastará 1.200 € en Navidad";
        terminosSexistas = new String[]{"Español"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze04() throws Exception {
        texto = " a los españoles, italianos y suecos.";
        terminosSexistas = new String[]{"Español", "Italiano", "Sueco"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze05() throws Exception {
        texto = "Los hombres tallaron la piedra desde...";
        terminosSexistas = new String[]{"Hombre"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze06() throws Exception {
        texto = "El hombre pisó la luna en...";
        terminosSexistas = new String[]{"Hombre"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze07() throws Exception {
        texto = "Los abogados acusadores";
        terminosSexistas = new String[]{"Abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze08() throws Exception {
        texto = "los abogados de acusadores";
        terminosSexistas = new String[]{"Abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze09() throws Exception {
        texto = "Nuestra amiga la señorita Gutiérrez";
        terminosSexistas = new String[]{"Señorita"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze10() throws Exception {
        texto = "Los españoles";
        terminosSexistas = new String[]{"Español"};
        this.executeAndValidate(texto, terminosSexistas);
    }
}
