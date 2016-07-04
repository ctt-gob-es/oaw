package es.inteco.intav.checks.une2012;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public final class Check_2_1_2_UserControlTest extends EvaluateCheck {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2 = "minhap.observatory.2_0.subgroup.2.1.2";

    private static final int BLINK_MARQUEE = 130;
    private static final int META_REDIRECT = 71;
    private static final int META_REFRESH = 72;
    /* Propiedad de CSS 'text-decoration: blink' */
    private static final int CSS_BLINK = 449;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateNoBlinkMarqueeRedirect() throws Exception {
        checkAccessibility.setContent("<html><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BLINK_MARQUEE));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REDIRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REFRESH));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_BLINK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateBlinkMarquee() throws Exception {
        checkAccessibility.setContent("<html><p><blink>Lorem</blink> <marquee>ipsum</marquee></p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(2, TestUtils.getNumProblems(evaluation.getProblems(), BLINK_MARQUEE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateMetaAutomaticRedirect() throws Exception {
        checkAccessibility.setContent("<html><head><meta http-equiv=\"refresh\" content=\"0; url=http://example.es/\" /></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REDIRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REFRESH));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateMetaRedirectTimed() throws Exception {
        checkAccessibility.setContent("<html><head><meta http-equiv=\"refresh\" content=\"5; url=http://example.es/\" /></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), META_REDIRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REFRESH));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateMetaRefresh() throws Exception {
        checkAccessibility.setContent("<html><head><meta http-equiv=\"refresh\" content=\"5\" /></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REDIRECT));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), META_REFRESH));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateMetaRefreshColon() throws Exception {
        checkAccessibility.setContent("<html><head><meta http-equiv=\"refresh\" content=\"5;\" /></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REDIRECT));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), META_REFRESH));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateMetaRedirectInvalid() throws Exception {
        checkAccessibility.setContent("<html><head><meta http-equiv=\"refresh\" content=\"5; www.google.es\" /></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), META_REDIRECT));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), META_REFRESH));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSTextDecorationBlink() throws Exception {
        checkAccessibility.setContent("<html><style>p.main { text-decoration: blink; }</style><p class=\"main\">Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_BLINK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSTextDecorationLineBlink() throws Exception {
        checkAccessibility.setContent("<html><style>p { text-decoration-line: blink; }</style><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), CSS_BLINK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateCSSTextDecorationBlinkDotted() throws Exception {
        checkAccessibility.setContent("<html><style>p { text-decoration: blink dotted; }</style><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), CSS_BLINK));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_2, TestUtils.OBS_VALUE_GREEN_ONE);
    }

}