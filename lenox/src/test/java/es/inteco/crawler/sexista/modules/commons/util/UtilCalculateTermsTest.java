package es.inteco.crawler.sexista.modules.commons.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UtilCalculateTermsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void asegurarArrayAcentuacionTest() throws Exception {

        // Recuperamos los arrays
        String[] vocalesAcentuadas = UtilCalculateTerms.VOCALES_ACENTUADAS;
        String[] vocalesNoAcentuadas = UtilCalculateTerms.VOCALES_NO_ACENTUADAS;

        // Verificamos
        Assert.assertEquals(vocalesAcentuadas.length,
                vocalesNoAcentuadas.length);
        Assert.assertEquals(vocalesAcentuadas[0], "Á");
        Assert.assertEquals(vocalesAcentuadas[1], "É");
        Assert.assertEquals(vocalesAcentuadas[2], "Í");
        Assert.assertEquals(vocalesAcentuadas[3], "Ó");
        Assert.assertEquals(vocalesAcentuadas[4], "Ú");
        Assert.assertEquals(vocalesNoAcentuadas[0], "A");
        Assert.assertEquals(vocalesNoAcentuadas[1], "E");
        Assert.assertEquals(vocalesNoAcentuadas[2], "I");
        Assert.assertEquals(vocalesNoAcentuadas[3], "O");
        Assert.assertEquals(vocalesNoAcentuadas[4], "U");
    }

    @Test
    public void removeAccentsTest() throws Exception {

        // Datos Iniciales
        String palabra = "ÁÉÍÓÚ";
        String expected = "AEIOU";

        // Invocamos el metodo
        String actual = UtilCalculateTerms.removeAccents(palabra);

        // Verificamos
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void esMonosilaboTest() throws Exception {

        // Datos Iniciales
        String monosilabo = "BIEN";
        String monosilabo2 = "QUIÉN";
        String monosilabo3 = "GUIÓN";
        String noMonosilabo = "SILABA";

        // Invocamos el metodo
        boolean ret1 = UtilCalculateTerms.esMonosilabo(monosilabo);
        boolean ret2 = UtilCalculateTerms.esMonosilabo(noMonosilabo);
        boolean ret3 = UtilCalculateTerms.esMonosilabo(monosilabo2);
        boolean ret4 = UtilCalculateTerms.esMonosilabo(monosilabo3);

        // Verificamos
        Assert.assertTrue(ret1);
        Assert.assertFalse(ret2);
        Assert.assertTrue(ret3);
        Assert.assertTrue(ret4);
    }

    @Test
    public void calculaNumeroSilabasTest() throws Exception {

        // Datos Iniciales
        String palabra0 = ""; // palabra vacía
        int numSilabas0 = 0;
        String palabra10 = "bie"; // 1 sola silaba con diptongo sin acentuar
        String palabra11 = "cié"; // 1sola sílaba con diptongo acentuado
        String palabra12 = "báb"; // 1 sola silaba con vocal acentuada
        String palabra13 = "co"; // 1 sola silaba con vocal sin acentuar
        int numSilabas1 = 1;
        String palabra4 = "bubiébabo"; // 4 sílabas con diptongo acentuado
        int numSilabas4 = 4;
        String vocalesSin = "bAbEbIbObU"; // Las 5 vocales sin acentuar
        String vocalesCon = "cÁcÉcÍcÓcÚ"; // Las 5 vocales acentuadas
        int numVocales = 5;
        String triptongosSin = "biaibiaubieibuaibuaubuei"; // 6 triptongos sin
        // acentuar
        String triptongosCon = "ciáiciáuciéicuáicuáucuéi"; // 6 triptongos
        // acentuados
        int numSilabasTriptongos = 6;
        String diptongosSin = "bAUbEUbOUbUAbUEbU0bAIbEIbOIbIAbIEbIObUIbIU"; // 14
        // diptongos
        // sin
        // acentuar
        int numSilabasDiptongosSin = 14;
        String diptongosCon = "cÁUcÉUcÓUcUÁcUÉcUÓcÁIcÉIcÓIcIÁcIÉcIÓ"; // 12
        // diptongos
        // con
        // acento
        int numSilabasDiptongosCon = 12;

        // Invocamos el metodo
        int ret0 = UtilCalculateTerms.calculaNumeroSilabas(palabra0);
        int ret10 = UtilCalculateTerms.calculaNumeroSilabas(palabra10);
        int ret11 = UtilCalculateTerms.calculaNumeroSilabas(palabra11);
        int ret12 = UtilCalculateTerms.calculaNumeroSilabas(palabra12);
        int ret13 = UtilCalculateTerms.calculaNumeroSilabas(palabra13);
        int ret4 = UtilCalculateTerms.calculaNumeroSilabas(palabra4);
        int retVS = UtilCalculateTerms.calculaNumeroSilabas(vocalesSin);
        int retVC = UtilCalculateTerms.calculaNumeroSilabas(vocalesCon);
        int retDS = UtilCalculateTerms.calculaNumeroSilabas(diptongosSin);
        int retDC = UtilCalculateTerms.calculaNumeroSilabas(diptongosCon);
        int retTS = UtilCalculateTerms.calculaNumeroSilabas(triptongosSin);
        int retTC = UtilCalculateTerms.calculaNumeroSilabas(triptongosCon);

        // Verificamos
        Assert.assertEquals(ret0, numSilabas0);
        Assert.assertEquals(ret10, numSilabas1);
        Assert.assertEquals(ret11, numSilabas1);
        Assert.assertEquals(ret12, numSilabas1);
        Assert.assertEquals(ret13, numSilabas1);
        Assert.assertEquals(ret4, numSilabas4);
        Assert.assertEquals(retVS, numVocales);
        Assert.assertEquals(retVC, numVocales);
        Assert.assertEquals(retDS, numSilabasDiptongosSin);
        Assert.assertEquals(retDC, numSilabasDiptongosCon);
        Assert.assertEquals(retTS, numSilabasTriptongos);
        Assert.assertEquals(retTC, numSilabasTriptongos);
    }

    @Test
    public void calculaNumeroSilabasYAcentuacionTest() throws Exception {

        // Datos Iniciales
        String palabra0 = ""; // palabra vacía
        int retEsperado0[] = {0, 0};
        String palabra10 = "bie"; // 1 sola silaba con diptongo sin acentuar
        String palabra11 = "co"; // 1 sola silaba con vocal sin acentuar
        int retEsperado10[] = {1, 0};
        String palabra12 = "cié"; // 1sola sílaba con diptongo acentuado
        String palabra13 = "báb"; // 1 sola silaba con vocal acentuada
        int retEsperado12[] = {1, 1};
        String palabra40 = "bubiébabo"; // 4 sílabas con diptongo acentuado
        int retEsperado40[] = {4, 3};
        String palabra41 = "cacecíco"; // 4 sílabas con vocal acentuada
        int retEsperado41[] = {4, 2};
        String palabra42 = "cácecico"; // 4 sílabas con vocal acentuada
        int retEsperado42[] = {4, 4};
        String vocalesSin = "bAbEbIbObU"; // Las 5 vocales sin acentuar
        int retEsperadoVS[] = {5, 0};
        String vocalesCon = "cÁcÉcÍcÓcÚ"; // Las 5 vocales acentuadas, no
        // comprueba que haya más de 1
        // acento, devuelve 1.
        int retEsperadoVC[] = {5, 1};
        String diptongosSin = "bAUbEUbOUbUAbUEbU0bAIbEIbOIbIAbIEbIObUIbIU"; // 14
        // diptongos
        // sin
        // acentuar
        int retEsperadoDS[] = {14, 0};
        String triptongosSin = "biaibiaubieibuaibuaubuei"; // 6 triptongos sin
        // acentuar
        int retEsperadoTS[] = {6, 0};
        String triptongosCon = "ciáiciaucieicuaicuaucuei"; // 6 triptongos el
        // primero acentuado
        int retEsperadoTC[] = {6, 6};

        // Invocamos al método
        int retObtenido0[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra0);
        int retObtenido10[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra10);
        int retObtenido11[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra11);
        int retObtenido12[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra12);
        int retObtenido13[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra13);
        int retObtenido40[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra40);
        int retObtenido41[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra41);
        int retObtenido42[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(palabra42);
        int retObtenidoVS[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(vocalesSin);
        int retObtenidoVC[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(vocalesCon);
        int retObtenidoDS[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(diptongosSin);
        int retObtenidoTS[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(triptongosSin);
        int retObtenidoTC[] = UtilCalculateTerms
                .calculaNumeroSilabasYAcentuacion(triptongosCon);

        // Verificamos
        Assert.assertArrayEquals(retObtenido0, retEsperado0);
        Assert.assertArrayEquals(retObtenido10, retEsperado10);
        Assert.assertArrayEquals(retObtenido11, retEsperado10);
        Assert.assertArrayEquals(retObtenido12, retEsperado12);
        Assert.assertArrayEquals(retObtenido13, retEsperado12);
        Assert.assertArrayEquals(retObtenido40, retEsperado40);
        Assert.assertArrayEquals(retObtenido41, retEsperado41);
        Assert.assertArrayEquals(retObtenido42, retEsperado42);
        Assert.assertArrayEquals(retObtenidoVS, retEsperadoVS);
        Assert.assertArrayEquals(retObtenidoVC, retEsperadoVC);
        Assert.assertArrayEquals(retObtenidoDS, retEsperadoDS);
        Assert.assertArrayEquals(retObtenidoTS, retEsperadoTS);
        Assert.assertArrayEquals(retObtenidoTC, retEsperadoTC);

    }

    @Test
    public void estaYaAcentuadaTest() throws Exception {

        // Datos Iniciales
        String conAcento = "Ratón";
        String sinAcento = "Teclado";

        // Invocamos el metodo
        boolean ret1 = UtilCalculateTerms.estaYaAcentuada(conAcento);
        boolean ret2 = UtilCalculateTerms.estaYaAcentuada(sinAcento);

        // Verificamos
        Assert.assertTrue(ret1);
        Assert.assertFalse(ret2);
    }

    /**
     * Se prueban los determinantes que cumplen la regla del femenino, pero que
     * no denotan necesariamente un femenino y la cadena vacía.
     *
     * @throws Exception en caso de fallo
     */
    @Test
    public void isFemaleTest01() throws Exception {

        // Datos iniciales
        String vacia = "";
        String cada = "cada";
        String cuya = "cuya";
        String treinta = "atreinta";
        String cuarenta = "acuarenta";
        String cincuenta = "acincuenta";
        String sesenta = "asesenta";
        String setenta = "asetenta";
        String ochenta = "aochenta";
        String noventa = "anoventa";
        String agricola = "agricolab";
        String agriicola = "agrícolab";

        // Invocamos al método
        boolean ret0 = UtilCalculateTerms.isFemale(vacia);
        boolean ret1 = UtilCalculateTerms.isFemale(cada);
        boolean ret2 = UtilCalculateTerms.isFemale(cuya);
        boolean ret3 = UtilCalculateTerms.isFemale(treinta);
        boolean ret4 = UtilCalculateTerms.isFemale(cuarenta);
        boolean ret5 = UtilCalculateTerms.isFemale(cincuenta);
        boolean ret6 = UtilCalculateTerms.isFemale(sesenta);
        boolean ret7 = UtilCalculateTerms.isFemale(setenta);
        boolean ret8 = UtilCalculateTerms.isFemale(ochenta);
        boolean ret9 = UtilCalculateTerms.isFemale(noventa);
        boolean ret10 = UtilCalculateTerms.isFemale(agricola);
        boolean ret11 = UtilCalculateTerms.isFemale(agriicola);

        // Verificamos
        Assert.assertFalse(ret0);
        Assert.assertFalse(ret1);
        Assert.assertFalse(ret2);
        Assert.assertFalse(ret3);
        Assert.assertFalse(ret4);
        Assert.assertFalse(ret5);
        Assert.assertFalse(ret6);
        Assert.assertFalse(ret7);
        Assert.assertFalse(ret8);
        Assert.assertFalse(ret9);
        Assert.assertFalse(ret10);
        Assert.assertFalse(ret11);

    }

    /**
     * Se prueban las terminaciones que cumplen la regla (-triz, -dad, -tad,
     * -ión, -sis(excepción en énfasis), -esa, -isa, -ina, -ie, -ez(que no sea
     * juez), -as, -a (Excepto las excepciones terminadas en -a o -as).
     *
     * @throws Exception en caso de fallo
     */
    @Test
    public void isFemaleTest02() throws Exception {

        // Datos iniciales
        String triz = "actriz";
        String dad = "maldad";
        String tad = "lealtad";
        String ion = "opinión";
        String sis = "parálisis";
        String excpSis = "énfasis";
        String esa = "pesa";
        String isa = "brisa";
        String ina = "fina";
        String ie = "barbarie";
        String ez = "nuez";
        String excpEz = "juez";
        String as = "mesas";
        String a = "mesa";
        String excpArtista = "artista";
        String excpAstronauta = "astronauta";
        String excpAtleta = "atleta";
        String excpGuia = "guía";
        String excpPeriodista = "periodista";
        String excpTurista = "turista";
        String excpPianista = "pianista";
        String excpViolinista = "violinista";
        String excpTenista = "tenista";
        String excpArtistas = "artistas";
        String excpAstronautas = "astronautas";
        String excpAtletas = "atletas";
        String excpGuias = "guías";
        String excpPeriodistas = "periodistas";
        String excpTuristas = "turistas";
        String excpPianistas = "pianistas";
        String excpViolinistas = "violinistas";
        String excpTenistas = "tenistas";
        String noFemenino = "oso";

        // Invocamos al método
        boolean ret0 = UtilCalculateTerms.isFemale(triz);
        boolean ret1 = UtilCalculateTerms.isFemale(dad);
        boolean ret2 = UtilCalculateTerms.isFemale(tad);
        boolean ret3 = UtilCalculateTerms.isFemale(ion);
        boolean ret4 = UtilCalculateTerms.isFemale(sis);
        boolean ret5excp = UtilCalculateTerms.isFemale(excpSis);
        boolean ret6 = UtilCalculateTerms.isFemale(esa);
        boolean ret7 = UtilCalculateTerms.isFemale(isa);
        boolean ret8 = UtilCalculateTerms.isFemale(ina);
        boolean ret9 = UtilCalculateTerms.isFemale(ie);
        boolean ret10 = UtilCalculateTerms.isFemale(ez);
        boolean ret11excp = UtilCalculateTerms.isFemale(excpEz);
        boolean ret12 = UtilCalculateTerms.isFemale(as);
        boolean ret13 = UtilCalculateTerms.isFemale(a);
        boolean ret14excp = UtilCalculateTerms.isFemale(excpArtista);
        boolean ret15excp = UtilCalculateTerms.isFemale(excpAstronauta);
        boolean ret16excp = UtilCalculateTerms.isFemale(excpAtleta);
        boolean ret17excp = UtilCalculateTerms.isFemale(excpGuia);
        boolean ret18excp = UtilCalculateTerms.isFemale(excpPeriodista);
        boolean ret19excp = UtilCalculateTerms.isFemale(excpTurista);
        boolean ret20excp = UtilCalculateTerms.isFemale(excpPianista);
        boolean ret21excp = UtilCalculateTerms.isFemale(excpViolinista);
        boolean ret22excp = UtilCalculateTerms.isFemale(excpTenista);
        boolean ret23excp = UtilCalculateTerms.isFemale(excpArtistas);
        boolean ret24excp = UtilCalculateTerms.isFemale(excpAstronautas);
        boolean ret25excp = UtilCalculateTerms.isFemale(excpAtletas);
        boolean ret26excp = UtilCalculateTerms.isFemale(excpGuias);
        boolean ret27excp = UtilCalculateTerms.isFemale(excpPeriodistas);
        boolean ret28excp = UtilCalculateTerms.isFemale(excpTuristas);
        boolean ret29excp = UtilCalculateTerms.isFemale(excpPianistas);
        boolean ret30excp = UtilCalculateTerms.isFemale(excpViolinistas);
        boolean ret31excp = UtilCalculateTerms.isFemale(excpTenistas);
        boolean ret32No = UtilCalculateTerms.isFemale(noFemenino);

        // Verificamos
        Assert.assertTrue(ret0);
        Assert.assertTrue(ret1);
        Assert.assertTrue(ret2);
        Assert.assertTrue(ret3);
        Assert.assertTrue(ret4);
        Assert.assertFalse(ret5excp);
        Assert.assertTrue(ret6);
        Assert.assertTrue(ret7);
        Assert.assertTrue(ret8);
        Assert.assertTrue(ret9);
        Assert.assertTrue(ret10);
        Assert.assertFalse(ret11excp);
        Assert.assertTrue(ret12);
        Assert.assertTrue(ret13);
        Assert.assertFalse(ret14excp);
        Assert.assertFalse(ret15excp);
        Assert.assertFalse(ret16excp);
        Assert.assertFalse(ret17excp);
        Assert.assertFalse(ret18excp);
        Assert.assertFalse(ret19excp);
        Assert.assertFalse(ret20excp);
        Assert.assertFalse(ret21excp);
        Assert.assertFalse(ret22excp);
        Assert.assertFalse(ret23excp);
        Assert.assertFalse(ret24excp);
        Assert.assertFalse(ret25excp);
        Assert.assertFalse(ret26excp);
        Assert.assertFalse(ret27excp);
        Assert.assertFalse(ret28excp);
        Assert.assertFalse(ret29excp);
        Assert.assertFalse(ret30excp);
        Assert.assertFalse(ret31excp);
        Assert.assertFalse(ret32No);

    }

    /**
     * Prueba el método calculateFemale.
     *
     * @throws Exception
     */
    @Test
    public void calculateFemaleTest() throws Exception {

        // Datos iniciales.
        String palabras[] = {"", "emperador", "actor", "generador", "pie",
                "pez", "bbbbdad", "bbbbtad", "énfasis", "sultán", "bbbbón",
                "bbbbín", "marqués", "bbbbes", "monos", "bbbba", "bbbbe",
                "bbbbi", "bbbbo", "bbbbu", "aaaaab", "camión"};
        String esperadas[] = {"", "emperatriz", "actriz", "generatriz", "",
                "", "", "", "", "sultana", "bbbbona", "bbbbina", "marquesa",
                "bbbbas", "monas", "bbbba", "bbbba", "bbbba", "bbbba", "bbbba",
                "aaaaaba", ""};
        String resultantes[] = new String[esperadas.length];

        // Invocamos al método
        int i = 0;
        for (String palabra : palabras) {
            resultantes[i] = UtilCalculateTerms.calculateFemale(palabra)
                    .toLowerCase();
            i++;
        }

        // Verificamos
        Assert.assertArrayEquals(esperadas, resultantes);

    }

    /**
     * Prueba el método calculatePlural.
     *
     * @throws Exception
     */
    @Test
    public void calculatePluralTest() throws Exception {

        // Datos iniciales.
        String palabras[] = {"", "gris", "marqués", "autobús", "arrayán",
                "belén", "motín", "cajón", "atún", "ciudad", "rey", "altar",
                "tos", "pez", "maniquí", "zulú", "ventana"};
        String esperadas[] = {"", "grises", "marqueses", "autobuses",
                "arrayanes", "belenes", "motines", "cajones", "atunes",
                "ciudades", "reyes", "altares", "toses", "peces", "maniquíes",
                "zulúes", "ventanas"};
        String resultantes[] = new String[esperadas.length];

        // Invocamos al método
        int i = 0;
        for (String palabra : palabras) {
            resultantes[i] = UtilCalculateTerms.calculatePlural(palabra)
                    .toLowerCase();
            i++;
        }

        // Verificamos
        Assert.assertArrayEquals(esperadas, resultantes);

    }

    /**
     * Prueba el método calculateSingular.
     *
     * @throws Exception
     */
    @Test
    public void calculateSingularTest() throws Exception {

        // Datos iniciales.
        String palabras[] = {"", "solidarios", "listas", "pacientes",
                "verdades", "placeres", "soldados", "misioneros", "gases",
                "barrabases", "bbbbeses", "bbbbebeses", "bises", "bbbbbibises",
                "boses", "bbbbboboses", "buses", "bbbbbubuses", "panes",
                "arrayanes", "trenes", "belenes", "crines", "motines", "sones",
                "botones", "funes", "atunes", "bábases", "bábeses", "bábises",
                "báboses", "bábuses", "calores", "cachalotes", "capaces",
                "deslices", "perdices", "vocales", "leyes", "maniquíes",
                "zulúes", "bababaes", "bababees", "monos", "cosas",
                "murciélagos", "pápases"};
        String esperadas[] = {"", "solidario", "lista", "paciente", "verdad",
                "placer", "soldado", "misionero", "gas", "barrabás", "bbbbes",
                "bbbbebés", "bis", "bbbbbibís", "bos", "bbbbbobós", "bus",
                "bbbbbubús", "pan", "arrayán", "tren", "belén", "crin",
                "motín", "son", "botón", "fun", "atún", "babas", "babes",
                "babis", "babos", "babus", "calor", "cachalote", "capaz",
                "desliz", "perdiz", "vocal", "ley", "maniquí", "zulú",
                "bababá", "bababé", "mono", "cosa", "murciélago", "papas"};
        String resultantes[] = new String[esperadas.length];

        // Invocamos al método
        int i = 0;
        for (String palabra : palabras) {
            resultantes[i] = UtilCalculateTerms.calculateSingular(palabra)
                    .toLowerCase();
            i++;
        }

        // Verificamos
        Assert.assertArrayEquals(esperadas, resultantes);

    }

    /**
     * Prueba el método calculateSingular, para las excepciones encontradas en los plurales.
     *
     * @throws Exception
     */
    @Test
    public void calculateSingularTest2() throws Exception {

        // Datos iniciales.
        String palabras[] = {"clases", "frases", "muelles"};
        String esperadas[] = {"clase", "frase", "muelle"};
        String resultantes[] = new String[esperadas.length];

        // Invocamos al método
        int i = 0;
        for (String palabra : palabras) {
            resultantes[i] = UtilCalculateTerms.calculateSingular(palabra)
                    .toLowerCase();
            i++;
        }

        // Verificamos
        Assert.assertArrayEquals(esperadas, resultantes);

    }
}
