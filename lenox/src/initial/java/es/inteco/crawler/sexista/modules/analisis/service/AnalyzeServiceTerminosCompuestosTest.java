package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.modules.analisis.service.base.BaseTest;
import org.junit.Test;

/**
 * Localización de términos compuestos.
 */
public class AnalyzeServiceTerminosCompuestosTest extends BaseTest {

    private String texto;
    private String[] terminosSexistas;

    @Test
    public void testAnalyze01() throws Exception {
        texto = "La Chacón ha sido elegida";
        terminosSexistas = new String[]{"la Chacón"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze02() throws Exception {
        texto = "La Ministra Chacón visitará las tropas";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze03() throws Exception {
        texto = "La juez ha determinado prisión para los...";
        terminosSexistas = new String[]{"La juez"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze04() throws Exception {
        texto = "La jueza ha determinado prisión para los...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze05() throws Exception {
        texto = "La casa de la pradera";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze06() throws Exception {
        texto = "La abogado de mi juicio";
        terminosSexistas = new String[]{"La abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze07() throws Exception {
        texto = "Las abogados de mi juicio";
        terminosSexistas = new String[]{"La abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze08() throws Exception {
        texto = "La abogada de mi juicio";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze09() throws Exception {
        texto = "La ministro de Interior";
        terminosSexistas = new String[]{"La ministro"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze10() throws Exception {
        texto = "La señora ministro de interior";
        terminosSexistas = new String[]{"Señora ministro"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze11() throws Exception {
        texto = "A la Chacón";
        terminosSexistas = new String[]{"la Chacón"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze12() throws Exception {
        texto = "La Chacón";
        terminosSexistas = new String[]{"la Chacón"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze13() throws Exception {
        texto = "Cada uno de los ganadores tendrá un premio";
        terminosSexistas = new String[]{"Cada uno de los"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze14() throws Exception {
        texto = "Cada una de las ganadoras tendrá";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    // Test movidos desde los Terminos Simples

    @Test
    public void testAnalyze15() throws Exception {
        texto = "Nuestra amiga la De la Vega";
        terminosSexistas = new String[]{"la de la Vega"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze16() throws Exception {
        texto = "Las señoras ministro";
        terminosSexistas = new String[]{"Señora ministro"};
        this.executeAndValidate(texto, terminosSexistas);
    }
}
