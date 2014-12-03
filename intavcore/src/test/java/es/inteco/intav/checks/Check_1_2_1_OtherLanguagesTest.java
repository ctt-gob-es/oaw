package es.inteco.intav.checks;

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

}