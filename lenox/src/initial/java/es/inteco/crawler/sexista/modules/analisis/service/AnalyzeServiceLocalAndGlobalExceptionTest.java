package es.inteco.crawler.sexista.modules.analisis.service;

import es.inteco.crawler.sexista.modules.analisis.service.base.BaseTest;
import org.junit.Test;

/**
 * Excepciones locales y globales.
 */
public class AnalyzeServiceLocalAndGlobalExceptionTest extends BaseTest {

    private String texto;
    private String[] terminosSexistas;

    /**
     * DEBE RETORNAR: <nada por excepción local>
     */
    @Test
    public void testAnalyze01() throws Exception {
        texto = "Los abogados de la Calle Atocha...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze02() throws Exception {
        texto = "Los abogados procedentes de la Calle Atocha...";
        terminosSexistas = new String[]{"Abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze03() throws Exception {
        texto = "Los abogados de la rua atocha";
        terminosSexistas = new String[]{"Abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción local>
     */
    @Test
    public void testAnalyze04() throws Exception {
        texto = "En general";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze05() throws Exception {
        texto = "Tambien general";
        terminosSexistas = new String[]{"General"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción global>
     */
    @Test
    public void testAnalyze06() throws Exception {
        texto = "Los alumnos Juan y Alfonso han obtenido...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción de contexto>
     */
    @Test
    public void testAnalyze07() throws Exception {
        texto = "Se precisan ingenieros e ingenieras";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción de contexto>
     */
    @Test
    public void testAnalyze08() throws Exception {
        texto = "Los alumnos y las alumnas de esta escuela";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción de contexto>
     */
    @Test
    public void testAnalyze09() throws Exception {
        texto = "Las alumnas y los alumnos de esta escuela";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por final genérico>
     */
    @Test
    public void testAnalyze10() throws Exception {
        texto = "Firmado: el vecino/a";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por final genérico>
     */
    @Test
    public void testAnalyze11() throws Exception {
        texto = "Firmado: los vecinos/as";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por final genérico>
     */
    @Test
    public void testAnalyze12() throws Exception {
        texto = "Firmado: el vecino-a";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por final genérico>
     */
    @Test
    public void testAnalyze13() throws Exception {
        texto = "Firmado: los vecinos-as";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción de contexto>
     */
    @Test
    public void testAnalyze14() throws Exception {
        texto = "Las y los españoles gastaremos una media...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción de contexto>
     */
    @Test
    public void testAnalyze15() throws Exception {
        texto = "Nuestros y nuestras ingenieros han recibido...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * DEBE RETORNAR: <nada por excepción de contexto>
     */
    @Test
    public void testAnalyze16() throws Exception {
        texto = "Nuestras y nuestros ingenieros han recibido...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * NOTA: <no se considera la excepción ya que a este término no le aplican las excepciones globales>
     */
    @Test
    public void testAnalyze17() throws Exception {
        texto = "La señorita Pepe vive en Lugo";
        terminosSexistas = new String[]{"Señorita"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze18() throws Exception {
        texto = "Los ingenieros y nuestro ingenio han sido...";
        terminosSexistas = new String[]{"Ingeniero"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * NOTA: <No marcar Arquitectos por el determinante dual>
     */
    @Test
    public void testAnalyze19() throws Exception {
        texto = "Los ingenieros y nuestras y nuestros arquitectos...";
        terminosSexistas = new String[]{"Ingeniero"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze20() throws Exception {
        texto = "... para nuestros ingenieros. Pepe Pérez ...";
        terminosSexistas = new String[]{"Ingeniero"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze21() throws Exception {
        texto = "La pianista del concierto";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze22() throws Exception {
        texto = "Los pianistas son personas...";
        terminosSexistas = new String[]{"Pianista"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze23() throws Exception {
        texto = "Los abogados de la rua Atocha";
        terminosSexistas = new String[]{"Abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por excepción global>
     */
    @Test
    public void testAnalyze24() throws Exception {
        texto = "El abogado Juan Fernández";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por excepción global>
     */
    @Test
    public void testAnalyze25() throws Exception {
        texto = "Los abogados Juan Fernández y Pedro..";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <Nada por excepción local>
     */
    @Test
    public void testAnalyze26() throws Exception {
        texto = "El abogado de la calle atocha";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por contexto>
     */
    @Test
    public void testAnalyze27() throws Exception {
        texto = "Niños, niñas, bebés y otros seres";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por contexto>
     */
    @Test
    public void testAnalyze28() throws Exception {
        texto = "Niñas, niños, bebés y otros seres";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por contexto>
     */
    @Test
    public void testAnalyze29() throws Exception {
        texto = "Bebés, niñas, niños y otros seres";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por contexto>
     */
    @Test
    public void testAnalyze30() throws Exception {
        texto = "Señoras y señores diputados";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por contexto>
     */
    @Test
    public void testAnalyze31() throws Exception {
        texto = "Señores y señoras diputadas";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por excepción global>
     */
    @Test
    public void testAnalyze32() throws Exception {
        texto = "Los diputados José y Pedro son...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * NOTA: <El punto hace cortar el contexto>
     */
    @Test
    public void testAnalyze33() throws Exception {
        texto = "Los abogados defienden casos. Las abogadas...";
        terminosSexistas = new String[]{"Abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por contexto>
     */
    @Test
    public void testAnalyze34() throws Exception {
        texto = "...españoles, españolas...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por contexto> a pesar del .
     * Las excepciones de contexto no tienen en cuenta signos de
     * puntuación (salvo el retorno de carro). Las excepciones locales
     * y globales sí que tienen en cuenta que los signos de puntuación
     * cortan la posible excepción
     */
    @Test
    public void testAnalyze35() throws Exception {
        texto = "... a los españoles. Las españolas";
        terminosSexistas = new String[]{"Español"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze36() throws Exception {
        texto = "La abogado Mª José Pérez";
        terminosSexistas = new String[]{"La abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze37() throws Exception {
        texto = "... a mis abogados. De la Calle Atocha saldrá...";
        terminosSexistas = new String[]{"Abogado"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * <nada> por excepción de contexto
     */
    @Test
    public void testAnalyze38() throws Exception {
        texto = "Los mejores centros hosteleros...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze39() throws Exception {
        texto = "Los mejores recentros hosteleros...";
        terminosSexistas = new String[]{"Hostelero"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada por excepciones locales tanto de rey como de mago>
     */
    @Test
    public void testAnalyze40() throws Exception {
        texto = "Los reyes magos";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    @Test
    public void testAnalyze41() throws Exception {
        texto = "Los reyes magosianos";
        terminosSexistas = new String[]{"Rey"};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada> por excepción global
     */
    @Test
    public void testAnalyze42() throws Exception {
        texto = "Las mujeres policía son más eficientes ...";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }

    /**
     * RESULTADO: <nada> Como es excepción global. Además, los términos de forma única no se marcan a comienzo de frase.
     */
    @Test
    public void testAnalyze43() throws Exception {
        texto = "Atletas como Ana Pérez";
        terminosSexistas = new String[]{};
        this.executeAndValidate(texto, terminosSexistas);
    }
}
