package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.modules.analisis.service.base.BaseTest;
import org.junit.Test;

/**
 * @author jbernal
 */
public class AnalyzeServiceHeuristicasTest extends BaseTest {

    private String texto;

    @Test
    public void testAnalyze01() throws Exception {
        texto = "El capitán Alatriste es la novela más famosa de Arturo Pérez Reverte";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze02() throws Exception {
        texto = "El capitán de la brigada manda sobre su tropa";
        this.executeAndValidate(texto, "Capitán");
    }

    @Test
    public void testAnalyze03() throws Exception {
        texto = "El suizo Roger Federer recupera el número 1 del tenis mundial después de un año";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze04() throws Exception {
        texto = "El suizo es una persona muy puntual";
        this.executeAndValidate(texto, "Suizo");
    }

    @Test
    public void testAnalyze05() throws Exception {
        texto = "José Luis Rodríguez Zapatero, presidente del gobierno durante el período 2004-2009, ...";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze06() throws Exception {
        texto = "Cristiano Ronaldo será el jugador mejor pagado con unos emonumentos cercanos a los 13 M";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze07() throws Exception {
        texto = "Castellano. Inglés. Chino. Japonés.";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze08() throws Exception {
        texto = "Se requiere conocimiento de Inglés y Alemán";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze09() throws Exception {
        texto = "Ingleses y alemanes encabezan el ranking de";
        this.executeAndValidate(texto, "Inglés", "Alemán");
    }

    @Test
    public void testAnalyze10() throws Exception {
        texto = "El tenista mallorquín Rafael Nadal ha perdido el número uno de la lista ATP al no poder disputar Wimbledom debido a una lesión de rodilla";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze11() throws Exception {
        texto = "El alcalde de Madrid es Alberto Ruiz Gallardón";
        this.executeAndValidate(texto, "Alcalde");
    }

    @Test
    public void testAnalyze12() throws Exception {
        texto = "El tenista madrileño ha ganado";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze13() throws Exception {
        texto = "Los tenistas madrileños continúan obteniendo éxitos";
        this.executeAndValidate(texto, "Tenista", "Madrileño");
    }

    @Test
    public void testAnalyze14() throws Exception {
        texto = "Los tenistas uruguayos no han ganado nunca la Copa Davis";
        this.executeAndValidate(texto, "Tenista", "Uruguayo");
    }

    @Test
    public void testAnalyze15() throws Exception {
        texto = "El Presidente del Gobierno español, José Luis Rodríguez Zapatero, y ...";
        this.executeAndValidate(texto, "Presidente");
    }

    @Test
    public void testAnalyze16() throws Exception {
        texto = "El jugador del Madrid ha declarado";
        this.executeAndValidate(texto, "Jugador");
    }

    @Test
    public void testAnalyze17() throws Exception {
        texto = "El presidente debe ser una persona íntegra";
        this.executeAndValidate(texto, "Presidente");
    }

    @Test
    public void testAnalyze18() throws Exception {
        texto = "El presidente ha declarado que";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze19() throws Exception {
        texto = "el detenido, Pedro Pérez...";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze20() throws Exception {
        texto = "El delegado de la clase deberá ser elegido este viernes";
        this.executeAndValidate(texto, "Delegado");
        // Se ha modificado la prueba porque no detecta "Elegido" por llevar delante "ser" (Excepc. Categoría)
        //this.executeAndValidate(texto, "Delegado", "Elegido");
    }

    @Test
    public void testAnalyze21() throws Exception {
        texto = "El parlamento gallego ha elegido presidente a Feijóo";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze22() throws Exception {
        texto = "ha elegido presidente a Feijóo";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze23() throws Exception {
        texto = "Gallego";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze24() throws Exception {
        texto = "Gallegos";
        this.executeAndValidate(texto, "Gallego");
    }

    @Test
    public void testAnalyze25() throws Exception {
        texto = "Gallego, catalán y castellano";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze26() throws Exception {
        texto = "Gallegos, catalanes y castellanos";
        this.executeAndValidate(texto, "Gallego", "Catalán", "Castellano");
    }

    @Test
    public void testAnalyze27() throws Exception {
        texto = "El gallego, el catalán y el castellano son personas";
        this.executeAndValidate(texto, "Castellano");
    }

    @Test
    public void testAnalyze28() throws Exception {
        texto = "Los gallegos, los catalanes y los castellanos son personas";
        this.executeAndValidate(texto, "Gallego", "Catalán", "Castellano");
    }

    @Test
    public void testAnalyze29() throws Exception {
        texto = "Presidente";
        this.executeAndValidate(texto);
    }

    @Test
    public void testAnalyze30() throws Exception {
        texto = "Presidentes";
        this.executeAndValidate(texto, "Presidente");
    }

    @Test
    public void testAnalyze31() throws Exception {
        texto = "El presidente";
        this.executeAndValidate(texto, "Presidente");
    }
}
