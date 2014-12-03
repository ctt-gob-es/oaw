package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.ctic.language.LanguageChecker;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import org.apache.tika.language.LanguageIdentifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public final class Check_1_1_7_PrimaryLanguageTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7 = "minhap.observatory.2.0.subgroup.1.1.7";

    private static final int HAS_LANGUAGE = 48;
    private static final int VALID_LANGUAGE = 49;
    private static final int GUESSED_LANGUAGE = 442;

    private CheckAccessibility checkAccessibility;

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    @Before
    public void setUp() throws Exception {
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HAS_LANGUAGE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), VALID_LANGUAGE));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateNoLang() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HAS_LANGUAGE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), VALID_LANGUAGE));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateBlankLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"\"><body><p>Lorem</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HAS_LANGUAGE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), VALID_LANGUAGE));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateUnknownLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"no_existe\"><body><p>Lorem</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), VALID_LANGUAGE));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateInlineENTag() throws Exception {
        checkAccessibility.setContent("<html lang=\"es\"><body><p>Fundación CTIC <img lang=\"en\" alt=\"Blank\"/>es una de las entidades que participan y apoyan la  Feria Internacional del Ocio Interactivo, Gamelab, que celebrará su quinta edición los próximos días 1, 2 y 3 de julio en Gijón (Teatro de la Laboral). El Ministerio de Cultura, el Gobierno del Principado de Asturias, a través de la Consejerías de Cultura y de Administraciones Públicas, el Ayuntamiento de Gijón y las principales empresas y asociaciones nacionales e internacionales del sector del Videojuego colaboran con esta iniciativa, ya consolidada, una de las más importantes de Europa.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateIncorrectENLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"en\"><body><p>Fundación CTIC es una de las entidades que participan y apoyan la  Feria Internacional del Ocio Interactivo, Gamelab, que celebrará su quinta edición los próximos días 1, 2 y 3 de julio en Gijón (Teatro de la Laboral). El Ministerio de Cultura, el Gobierno del Principado de Asturias, a través de la Consejerías de Cultura y de Administraciones Públicas, el Ayuntamiento de Gijón y las principales empresas y asociaciones nacionales e internacionales del sector del Videojuego colaboran con esta iniciativa, ya consolidada, una de las más importantes de Europa.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateENLangExclude() throws Exception {
        checkAccessibility.setContent("<html lang=\"en\"><body>" +
                "<p lang=\"es\">Fundación CTIC es una de las entidades que participan y apoyan la  Feria Internacional del Ocio Interactivo, Gamelab, que celebrará su quinta edición los próximos días 1, 2 y 3 de julio en Gijón (Teatro de la Laboral). El Ministerio de Cultura, el Gobierno del Principado de Asturias, a través de la Consejerías de Cultura y de Administraciones Públicas, el Ayuntamiento de Gijón y las principales empresas y asociaciones nacionales e internacionales del sector del Videojuego colaboran con esta iniciativa, ya consolidada, una de las más importantes de Europa.</p>" +
                "</body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateESLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"es\"><body><p>Fundación CTIC es una de las entidades que participan y apoyan la  Feria Internacional del Ocio Interactivo, Gamelab, que celebrará su quinta edición los próximos días 1, 2 y 3 de julio en Gijón (Teatro de la Laboral). El Ministerio de Cultura, el Gobierno del Principado de Asturias, a través de la Consejerías de Cultura y de Administraciones Públicas, el Ayuntamiento de Gijón y las principales empresas y asociaciones nacionales e internacionales del sector del Videojuego colaboran con esta iniciativa, ya consolidada, una de las más importantes de Europa.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateENLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"en\"><body><p>The World Wide Web Consortium (W3C) is an international consortium where Member organizations, a full-time staff, and the public work together to develop Web standards.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateFRLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"fr\"><body><p>La mission du W3C est d’amener le Web à son plein potentiel, en développant des protocoles et des directives permettant une croissance à long terme du Web.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateEULang() throws Exception {
        checkAccessibility.setContent("<html lang=\"eu\"><body><p>Auzolandegietan leku askotako gazteak biltzen dira, euren borondatez eta ordainetan ezer jaso gabe, aldi batez elkarrekin bizi eta gizarte osoarekin zerikusia duen proiektu batean parte hartzeko asmotan.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateCALang() throws Exception {
        checkAccessibility.setContent("<html lang=\"ca\"><body><p>Sistemes amb un balanç energètic zero, que integrin el concepte d’ecodisseny, que siguin més segurs, intel·ligents, accessibles per a l’usuari i que estiguin altament interconnectats amb l’exterior, són característiques exigibles als ascensors del futur.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateVALang() throws Exception {
        checkAccessibility.setContent("<html lang=\"va\"><body><p>L'Acadèmia Valenciana de la Llengua ha introduït en el seu web una sèrie de canvis i novetats amb l’objectiu d’actualitzar al màxim la pàgina d’informació permanent als usuaris. L’activitat de la institució normativa ha augmentat amb el pas dels anys i l'increment es veu reflectit en el contingut dels diferents apartats del web.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateGLLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"gl\"><body><p>Hai xa máis de douscentos anos, os ilustrados enunciaron o principio que mellor define a democracia: o goberno do pobo, para o pobo e polo pobo. Moitas son as cousas que teñen cambiado desde o Século das Luces,  mais o espírito que guiaba os pais do noso sistema político permaneceu inalterable ao paso do tempo.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateASTLang() throws Exception {
        // L'Academia de la Llingua Asturiana (A.Ll.A.) ye una institución del Principáu d'Asturies que se creó en 1980 por Decretu del Conseyu Rexonal d'Asturies 33/1980 de 15 d'avientu y con Estatutos aprobaos pol mesmu muérganu por Decretu 9/1981, modificaos el 12 d'abril de 1995 (BOPA nu 136 de 14.6.1995). L'Academia tien anguaño 23 miembros de númberu, 19 miembros correspondientes y 15 académicos d'honor. Los académicos nun perciben nenguna retribución nin pola so condición de tales nin polos cargos que desempeñen na Academia.
        checkAccessibility.setContent("<html lang=\"ast\"><body><p>L'asturianu ye una llingua romancep ropia d'Asturies, perteneciente al subgrupu asturllionés.</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertTrue("Asturiano no reconocido", LanguageIdentifier.getSupportedLanguages().contains("ast"));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), GUESSED_LANGUAGE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_7, TestUtils.OBS_VALUE_GREEN_ONE);
    }

}