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
public final class Check_2_1_1_JavaScriptAccesibleTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1 = "minhap.observatory.2.0.subgroup.2.1.1";
    private static final int DUPLICATED_EVENTS = 160;
    private static final int ELEMENTS_INTERACTIVE = 432;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateAOnClick() throws Exception {
        checkAccessibility.setContent("<html><p><a onclick=\"foo()\">Lorem ipsum</a></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateImgNoRoleNoTabIndex() throws Exception {
        checkAccessibility.setContent("<html><p><img onclick=\"foo()\" /></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateImgRoleNoTabIndex() throws Exception {
        checkAccessibility.setContent("<html><p><img onclick=\"foo()\" role=\"link\"/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateImgRoleTabIndex() throws Exception {
        checkAccessibility.setContent("<html><p><img onclick=\"foo()\" role=\"link\" tabindex=\"2\"/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), ELEMENTS_INTERACTIVE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_1, TestUtils.OBS_VALUE_GREEN_ONE);
    }

}

