package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_2_2_3_NavigationTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3 = "minhap.observatory.2.0.subgroup.2.2.3";

    private static final int BROKEN_LINKS_WARNING = 176;
    private static final int MORE_THAN_ONE_BROKEN_LINKS = 177;
    private static final int COMBINED_ADJACENT_LINKS = 180;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateNoLinks() throws Exception {
        checkAccessibility.setContent("<html><p></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_NOT_SCORE);
    }

    @Test
    public void evaluateAdjacentSameLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.google.es\">Lorem</a> <a href=\"http://www.google.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_LINKS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateAdjacentNoSameLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.google.es\">Lorem</a> <a href=\"http://www.google.com\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateOnlyOneBrokenLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.google.es\">Lorem</a></p><p><a href=\"http://www.zxcvb.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_LINKS_WARNING));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateMultipleBrokenLinks() throws Exception {
        checkAccessibility.setContent("<html><p><a href=\"http://www.qwertyzxcv.es\">Lorem</a></p><p><a href=\"http://www.zxcvb.es\">ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BROKEN_LINKS_WARNING));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), MORE_THAN_ONE_BROKEN_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMBINED_ADJACENT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_3, TestUtils.OBS_VALUE_RED_ZERO);
    }

}

