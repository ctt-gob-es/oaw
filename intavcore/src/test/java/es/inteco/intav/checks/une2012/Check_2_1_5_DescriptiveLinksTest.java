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
public final class Check_2_1_5_DescriptiveLinksTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5 = "minhap.observatory.2.0.subgroup.2.1.5";

    private final int IMG_ALT_BLANK_LINKS = 69;
    private final int TOO_MUCH_TEXT_LINKS = 181;
    private final int NO_DESCRIPTIVE_TEXT_LINKS = 79;
    private final int BLANK_TEXT_LINKS = 142;
    private final int REDUNDANT_IMG_ALT_TEXT_LINKS = 428;

    private CheckAccessibility checkAccessibility;
    private Evaluation evaluation;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
    }

    @Test
    public void evaluateImgNoAlt() throws Exception {
        checkAccessibility.setContent("<html><body><a href=\"/foo\"><img /></a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        // Caso especial ya que una imagen sin alt se contabiliza en la verificación 1.1.1
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), IMG_ALT_BLANK_LINKS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateImgAltBlank() throws Exception {
        checkAccessibility.setContent("<html><body><a><img alt=\"\"/></a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), IMG_ALT_BLANK_LINKS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), BLANK_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }


    @Test
    public void evaluateSuspiciousLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a>Haz click aquí</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), NO_DESCRIPTIVE_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><a>Pincha aquí</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), NO_DESCRIPTIVE_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<html><body><a>Pinche aquí</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), NO_DESCRIPTIVE_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateLongLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet.</a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test

    public void evaluateLeyLongLinkText() throws Exception {
        checkAccessibility.setContent("<html><body><a>Real Decreto Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet. </a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateLongLinkImgAltText() throws Exception {
        checkAccessibility.setContent("<html><body><a><img alt=\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultricies ante eget lobortis vestibulum. Maecenas quis mollis metus. Nullam pulvinar nisl eu lorem consequat accumsan. Praesent vel nulla mollis, convallis tellus sit amet, facilisis magna amet.\"/> </a></body></html>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TOO_MUCH_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateWhiteSpaces() throws Exception {
        checkAccessibility.setContent("<a>Lorem <img alt=\"Lorem\"/></a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a><img alt=\"RSS\" title=\"RSS\" /> RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a><img alt=\"RSS\" title=\"RSS\" />     RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a><img alt=\"RSS\" title=\"RSS\" />&nbsp;RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a><img alt=\"RSS \" title=\"RSS\" /> RSS</a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateInlineTags() throws Exception {
        checkAccessibility.setContent("<a><strong>Lorem</strong> <img alt=\"Lorem\"/></a>");
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a><strong>L</strong>orem <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);

        checkAccessibility.setContent("<a><strong><em>Lorem</em></strong> <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), REDUNDANT_IMG_ALT_TEXT_LINKS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_5, TestUtils.OBS_VALUE_RED_ZERO);
    }

}

