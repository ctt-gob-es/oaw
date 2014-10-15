package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
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
public final class Check_1_1_5_StructureTest extends EvaluateCheck {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5 = "minhap.observatory.2.0.subgroup.1.1.5";
    private static final int MORE_TEN_BRS = 436;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateZeroBRs() throws Exception {
        checkAccessibility.setContent("<html><p>Lorem ipsum<br/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateFourBRs() throws Exception {
        checkAccessibility.setContent("<html><p>Lorem<br/>ipsum<br/>Lorem<br/>Ipsum<br/></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateTenBRs() throws Exception {
        checkAccessibility.setContent("<html><p>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateMoreTenBRs() throws Exception {
        checkAccessibility.setContent("<html><p>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "Lorem ipsum<br/>" +
                "</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), MORE_TEN_BRS));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

}