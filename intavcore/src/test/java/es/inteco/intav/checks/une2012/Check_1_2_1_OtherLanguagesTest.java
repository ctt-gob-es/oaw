package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public final class Check_1_2_1_OtherLanguagesTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1 = "minhap.observatory.2.0.subgroup.1.2.1";

    private static final int VALID_LANGUAGE_ELEMENT = 161;
    private static final int LANGUAGE_CHANGE_LINKS = 93;
    private static final int OTHER_LANGUAGES = 460;

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
        checkAccessibility.setContent("<html><body><p>Lorem <strong lang=\"en\">ipsum</strong></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), VALID_LANGUAGE_ELEMENT));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateLangNoValid() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem <strong lang=\"no_existe\">ipsum</strong></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), VALID_LANGUAGE_ELEMENT));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateLangBlank() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem <strong lang=\"\">ipsum</strong></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), VALID_LANGUAGE_ELEMENT));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateLinkLang() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem <a lang=\"en\">Welcome</a></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LANGUAGE_CHANGE_LINKS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateLinkNoLang() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem <a>Welcome</a></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LANGUAGE_CHANGE_LINKS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateLinkWrongLang() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem <a lang=\"es\">Welcome</a></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), LANGUAGE_CHANGE_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateLinkWrappedLang() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem <span lang=\"en\"><a>Welcome</a></span></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LANGUAGE_CHANGE_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><p lang=\"en\">Lorem <span><a>Welcome</a></span></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LANGUAGE_CHANGE_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><p>Lorem <a><span lang=\"en\">Welcome</span></a></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), LANGUAGE_CHANGE_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateOtherLang() throws Exception {
        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem <strong>You are welcome</strong></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateOtherLangMarked() throws Exception {
        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem <strong lang=\"en\">You are welcome</strong></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }


    /**
     * *************
     * ABB     *
     * **************
     */

        /*
            MET 4.8.1 - Se verifica que todos los idiomas especificados por los elementos sean válidos (ID 161)
            MET 4.8.2 - Se verifica que los cambios de idioma más habituales encontrados en un documento se marquen adecuadamente (ID 93)
            MET 4.8.3 - Se verifica que los textos en inglés encontrados en un documento se marquen adecuadamente (NUEVA)

            VALID_LANGUAGE_ELEMENT = 161;
            LANGUAGE_CHANGE_LINKS = 93;
            OTHER_LANGUAGES = 460;

        */
    @Test
    public void MET_4_8_3_evaluateLanguageChanges() throws Exception {

        /* MET 4.8.3
            Title:      Se verifica que los textos en inglés (que contienen alguna de las 100 palabras más habituales) están correctamente identificados. Se busca en textos, alternativas textuales y títulos.
            Subject:    *
            Check:      Si el texto está en inglés debe estar identificado con lang="en" (u otras variedades de inglés).

         */

        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem <strong lang=\"en\">You are welcome</strong></p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);


        // FALLA: Expected 0, Actual 1
        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem <strong lang=\"en-us\">You are welcome</strong></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);


        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem</p><p lang=\"en\">You are <strong>the other</strong></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);


        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem</p><p lang=\"en\">You are <img src=\"img.png\" alt=\"the other\"></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);


        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem</p><p>Tu eres <img src=\"img.png\" alt=\"the other\" lang=\"en\"></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_GREEN_ONE);


        // FALLA: Expected 1, Actual 0
        checkAccessibility.setContent("<html lang=\"es\"><body><p>Lorem</p><p>Tu eres <img src=\"img.png\" alt=\"You are welcome the other\"></p></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), OTHER_LANGUAGES));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_1, TestUtils.OBS_VALUE_RED_ZERO);


    }


}