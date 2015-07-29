package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.modules.analisis.service.base.BaseTest;
import org.junit.Test;

public class AnalyzeServiceFormaUnicaTest extends BaseTest {

    private String texto;

    @Test
    public void testAnalyze01() throws Exception {
        texto = "Los pianistas tienen un cerebro privilegiado";
        this.executeAndValidate(texto, "Pianista");
    }

    @Test
    public void testAnalyze02() throws Exception {
        texto = "Nuestras mejores pianistas han participado...";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze03() throws Exception {
        texto = "Nuestros mejores pianistas han participado...";
        this.executeAndValidate(texto, "Pianista");
    }

    @Test
    public void testAnalyze04() throws Exception {
        texto = "Nuestros mejores pianistas, y nuestras mejores pianistas";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze05() throws Exception {
        texto = "Partitura escrita para listísimas pianistas";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze06() throws Exception {
        texto = "Las mujeres de nuestros atletas han viajado a Pekín";
        this.executeAndValidate(texto, "Atleta");
    }

    @Test
    public void testAnalyze07() throws Exception {
        texto = "Nuestros mejores atletas ya están en Pekín";
        this.executeAndValidate(texto, "Atleta");
    }

    @Test
    public void testAnalyze08() throws Exception {
        texto = "Nuestras mejores atletas ya están en Pekín";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze09() throws Exception {
        texto = "Nuestras atletas ya están en Pekín";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze10() throws Exception {
        texto = "Las chicas pianistas demuestran una mayor...";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze11() throws Exception {
        texto = "Las pianistas de la orquestan actuaron...";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze12() throws Exception {
        texto = "Los pianistas de la orquestas actuaron...";
        this.executeAndValidate(texto, "Pianista");
    }

    /**
     * NOTAS: <nada> por la excepción como
     */
    @Test
    public void testAnalyze13() throws Exception {
        texto = "Tenistas como Nadal o Federer han demostrado";
        this.executeAndValidate(texto);
    }

    /**
     * NOTAS: <nada> por la excepción (madrileño)
     */
    @Test
    public void testAnalyze14() throws Exception {
        texto = "El tenista madrileño ha ganado";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze15() throws Exception {
        texto = "Los tenistas madrileños han conseguido 3 medallas en los ...";
        this.executeAndValidate(texto, "Tenista", "Madrileño");
    }

    @Test
    public void testAnalyze16() throws Exception {
        texto = "Los mayores de Madrid pueden viajar a la costa...";
        this.executeAndValidate(texto, "Los mayores");
    }

    @Test
    public void testAnalyze17() throws Exception {
        texto = "Las y los mayores de Madrid pueden viajar a la costa...";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze18() throws Exception {
        texto = "10 atletas españoles han conseguido medallas.";
        this.executeAndValidate(texto, "Español");
    }

    @Test
    public void testAnalyze19() throws Exception {
        texto = "Sus atletas eran los mejores de la época.";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze20() throws Exception {
        texto = "Algunos equipos nacionales no han logrado clasificarse.";
        this.executeAndValidate(texto);
    }

    @Test //Añadida por GPM
    public void testAnalyze21() throws Exception {
        texto = "Nuestros mejores 1 atletas ya están en Pekín.";
        this.executeAndValidate(texto);
    }

    @Test //Añadida por GPM
    public void testAnalyze22() throws Exception {
        texto = "Nuestros mejores 1001 atletas ya están en Pekín.";
        this.executeAndValidate(texto);
    }

    @Test //Añadida por GPM
    public void testAnalyze23() throws Exception {
        texto = "Nuestros mejores 12.1 atletas ya están en Pekín.";
        this.executeAndValidate(texto);
    }

    @Test //Añadida por GPM
    public void testAnalyze24() throws Exception {
        texto = "Atletas de todo el mundo ...";
        this.executeAndValidate(texto);
    }
}
