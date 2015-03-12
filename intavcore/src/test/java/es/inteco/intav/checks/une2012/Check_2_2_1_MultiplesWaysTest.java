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
public final class Check_2_2_1_MultiplesWaysTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_1 = "minhap.observatory.2.0.subgroup.2.2.1";

    private final int MULTIPLES_WAYS_ID = 419;
    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateNoSiteMapNoSearchForm() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p></body></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateSiteMapNoSearchForm() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p><p><a href=\"http:\\\\www.google.com\">Mapa web</a></p></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateNoSiteMapSearchForm() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p><p><form><input /><input type=\"button\" value=\"Buscar\"></form></p></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateNoSiteMapSearchFormRole() throws Exception {
        checkAccessibility.setContent("<html><body><p>Lorem ipsum</p><p><form role=\"search\"><input /><input type=\"button\" value=\"Ir\"></form></p></html>");
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MULTIPLES_WAYS_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

}

