package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import es.inteco.intav.form.ObservatoryEvaluationForm;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_2_2_2_FocusTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2 = "minhap.observatory.2.0.subgroup.2.2.2";

    private static final int TABINDEX_USSAGE_LOW = 434;
    private static final int TABINDEX_USSAGE_EXCESSIVE = 435;
    private static final int CSS_OUTLINE = 451;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateZeroTabindex() throws Exception {
        checkAccessibility.setContent("<html><p><a>Lorem ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_LOW));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_EXCESSIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateOneTabindex() throws Exception {
        checkAccessibility.setContent("<html><p><a tabindex=\1\">Lorem ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_LOW));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_EXCESSIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateThreeTabindex() throws Exception {
        checkAccessibility.setContent("<html><p><a tabindex=\"1\">Lorem ipsum</a>, <a tabindex=\"2\">Lorem ipsum</a>, <a tabindex=\"3\">Lorem ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_LOW));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_EXCESSIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateFourTabindex() throws Exception {
        checkAccessibility.setContent("<html><p><a tabindex=\"1\">Lorem ipsum</a>, <a tabindex=\"2\">Lorem ipsum</a>, <a tabindex=\"3\">Lorem ipsum</a>, <a tabindex=\"4\">Lorem ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_LOW));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_EXCESSIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateTenTabindex() throws Exception {
        checkAccessibility.setContent("<html><p><a tabindex=\"1\">Lorem ipsum</a>, <a tabindex=\"2\">Lorem ipsum</a>, <a tabindex=\"3\">Lorem ipsum</a>, <a tabindex=\"4\">Lorem ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getVectorProblems(), TABINDEX_USSAGE_LOW));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_EXCESSIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateMoreTenTabindex() throws Exception {
        checkAccessibility.setContent("<html><p>" +
                "<a tabindex=\"1\">Lorem ipsum</a>," +
                "<a tabindex=\"2\">Lorem ipsum</a>," +
                "<a tabindex=\"3\">Lorem ipsum</a>," +
                "<a tabindex=\"4\">Lorem ipsum</a>," +
                "<a tabindex=\"5\">Lorem ipsum</a>," +
                "<a tabindex=\"6\">Lorem ipsum</a>," +
                "<a tabindex=\"7\">Lorem ipsum</a>," +
                "<a tabindex=\"8\">Lorem ipsum</a>," +
                "<a tabindex=\"9\">Lorem ipsum</a>," +
                "<a tabindex=\"10\">Lorem ipsum</a>," +
                "<a tabindex=\"11\">Lorem ipsum</a>" +
                "</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_LOW));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TABINDEX_USSAGE_EXCESSIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSOutline() throws Exception {
        checkAccessibility.setContent("<html><style>.main:focus { outline: 10px solid none }</style><p>Lorem</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_OUTLINE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_2_2, TestUtils.OBS_VALUE_RED_ZERO);
    }
}

