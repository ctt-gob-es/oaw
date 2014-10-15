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
public final class Check_2_1_4_TitleAndFramesTest {

    private static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4 = "minhap.observatory.2.0.subgroup.2.1.4";

    private static int TITLE_EXISTS = 50;
    private static int TITLE_BLANK = 51;
    private static int TITLE_SUSPICIOUS = 53;
    private static int FRAME_TITLE = 31;
    private static int IFRAME_TITLE = 295;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        EvaluatorUtility.initialize();
        checkAccessibility = TestUtils.getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateTitle() throws Exception {
        checkAccessibility.setContent("<html><head><title>Título descriptivo</title></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_GREEN_ONE);
    }

    @Test
    public void evaluateBlank() throws Exception {
        checkAccessibility.setContent("<html><head><title></title></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateTitleWhiteSpaces() throws Exception {
        checkAccessibility.setContent("<html><head><title> </title></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateTitleSuspicious() throws Exception {
        checkAccessibility.setContent("<html><head><title>Untitled</title></head><p>Lorem ipsum</p></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateFrameTitle() throws Exception {
        checkAccessibility.setContent("<html><head><title>Lorem</title></head><p>Lorem ipsum</p>" +
                "<frame title=\"Aplicación embebed\"></frame></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), FRAME_TITLE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateFrameNoTitle() throws Exception {
        checkAccessibility.setContent("<html><head><title>Lorem</title></head><p>Lorem ipsum</p>" +
                "<frame></frame></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), FRAME_TITLE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateIFrameTitle() throws Exception {
        checkAccessibility.setContent("<html><head><title>Lorem</title></head><p>Lorem ipsum</p>" +
                "<iframe title=\"Aplicación embebed\"></iframe></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), IFRAME_TITLE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateIFrameNoTitle() throws Exception {
        checkAccessibility.setContent("<html><head><title>Lorem</title></head><p>Lorem ipsum</p>" +
                "<iframe></iframe></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_EXISTS));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_BLANK));
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), TITLE_SUSPICIOUS));
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), IFRAME_TITLE));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_2_1_4, TestUtils.OBS_VALUE_RED_ZERO);
    }

}