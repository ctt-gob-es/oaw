package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public final class Check_1_2_2_ColorContrast {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2 = "minhap.observatory.2.0.subgroup.1.2.2";

    private static final int CSS_COLOR_CONTRAST= 448;

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
    public void evaluateCSSSameColors() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: #FFF; background-color: #FFF;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation,MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2,TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSSameColorsName() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: white; background-color: white;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation,MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2,TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSMaximumContrast() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: #000; background-color: #FFF;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation,MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2,TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateCSSMaximumContrastColorName() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: black; background-color: #FFF;}</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation,MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2,TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateCSSNoColor() throws Exception {
        checkAccessibility.setContent("<html><head><style>.main { color: black; }</style><title>Lorem</title></head><body><p>Lorem <u>ipsum</u></p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_COLOR_CONTRAST));
        TestUtils.checkVerificacion(evaluation,MINHAP_OBSERVATORY_2_0_SUBGROUP_1_2_2,TestUtils.OBS_VALUE_GREEN_ONE);
    }
}