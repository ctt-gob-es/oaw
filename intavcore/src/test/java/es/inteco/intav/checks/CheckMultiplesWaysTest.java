package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class CheckMultiplesWaysTest {

    private final int MULTIPLES_WAYS_ID = 419;

    @BeforeClass
    public static void init() throws Exception {
        EvaluatorUtility.initialize();
    }

    @Test
    public void evaluateNoSiteMapNoSearchForm() throws Exception {
        CheckAccessibility checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));

        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, "minhap.observatory.2.0.subgroup.2.2.1", TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateSiteMapNoSearchForm() throws Exception {
        CheckAccessibility checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p><p><a href=\"http:\\\\www.google.com\">Mapa web</a></p></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));

        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, "minhap.observatory.2.0.subgroup.2.2.1", TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateNoSiteMapSearchForm() throws Exception {
        CheckAccessibility checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p><p><form><input /><input type=\"button\" value=\"Buscar\"></form></p></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));

        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, "minhap.observatory.2.0.subgroup.2.2.1", TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateNoSiteMapSearchFormRole() throws Exception {
        CheckAccessibility checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p><p><form role=\"search\"><input /><input type=\"button\" value=\"Ir\"></form></p></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));

        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, "minhap.observatory.2.0.subgroup.2.2.1", TestUtils.OBS_VALUE_GREEN_ONE);
    }

}

