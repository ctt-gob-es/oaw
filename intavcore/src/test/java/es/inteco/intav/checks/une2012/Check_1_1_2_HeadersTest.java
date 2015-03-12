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
public final class Check_1_1_2_HeadersTest {

    public static final String MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_2 = "minhap.observatory.2.0.subgroup.1.1.2";

    private static final int HEADERS_NESTING_ID = 37;
    private static final int EXISTS_HEADERS_ID = 38;
    private static final int HEADERS_BLANK_ID = 395;
    private static final int EXISTS_H1_ID = 421;
    private static final int SAME_LEVEL_HEADERS_NO_CONTENT_ID = 422;
    private static final int COMPLEX_STRUCTURE = 433;

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
    public void evaluateCorrectNesting() throws Exception {
        checkAccessibility.setContent("<html><body><h1>Lorem</h1><p>Ipsum</p><h2>Sic semper</h2><p>Foo</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_NESTING_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_2, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateCorrectNestingComplex() throws Exception {
        checkAccessibility.setContent("<html><head><title>Lorem</title></head><body><h1>Lorem</h1><p>Ipsum</p><h2>Sic semper</h2><p>Foo</p><h3>Bar</h3><p>Lorem</p><h1>Foo</h1><p>Lorem</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_NESTING_ID));
    }

    @Test
    public void evaluateIncorrectNestingStartFirstLevel() throws Exception {
        checkAccessibility.setContent("<html><body><h1>Lorem</h1><p>Ipsum</p><h4>Sic semper</h4><p>Foo</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_NESTING_ID));
    }

    @Test
    public void evaluateIncorrectNestingStartRandomLevel() throws Exception {
        checkAccessibility.setContent("<html><body><h2>Lorem</h2><p>Ipsum</p><h4>Sic semper</h4><p>Foo</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_NESTING_ID));
    }

    @Test
    public void evaluateNoHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><p>Ipsum</p><p>Foo</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), EXISTS_HEADERS_ID));
    }

    @Test
    public void evaluateBlankHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1></h1><p>Ipsum</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_BLANK_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_2, TestUtils.OBS_VALUE_RED_ZERO);
    }

    @Test
    public void evaluateImgAltHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><img alt=\"Foo bar\"/></h1><p>Ipsum</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateImgAltBlankHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><img alt=\"\"/></h1><p>Ipsum</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateDivHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><div>Foo bar</div></h1><p>Ipsum</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateMarkHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><mark>Foo bar</mark></h1><p>Ipsum</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateNoH1() throws Exception {
        checkAccessibility.setContent("<html><body><h3>Foo bar</h3><p>Ipsum</p></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), EXISTS_H1_ID));
        TestUtils.checkVerificacion(evaluation, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_2, TestUtils.OBS_VALUE_GREEN_ZERO);
    }

    @Test
    public void evaluateAdjacentSameHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1>foo</h1><h2>Foo bar</h2><h2>Ipsum</h2><p>Some content<p></body></html>");
//        Assert.assertEquals(1, getNumProblems(checkAccessibility, SAME_LEVEL_HEADERS_NO_CONTENT_ID));
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SAME_LEVEL_HEADERS_NO_CONTENT_ID));
        ObservatoryEvaluationForm oef = EvaluatorUtils.generateObservatoryEvaluationForm(evaluation, "", true);
        TestUtils.checkVerificacion(oef, MINHAP_OBSERVATORY_2_0_SUBGROUP_1_1_2, TestUtils.OBS_VALUE_RED_ZERO);

    }

    @Test
    public void evaluateLastHeadingNoContent() throws Exception {
        checkAccessibility.setContent("<html><body><h1>foo</h1><h2>Ipsum</h2><p>Some content<p><h2>No content</h2></body></html>");
        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), SAME_LEVEL_HEADERS_NO_CONTENT_ID));
    }

    @Test
    public void evaluateComplexStructure() throws Exception {
        // Solo se contabilizan los párrafos con al menos 80 caracteres
        final String complexParagraph = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut interdum massa nunc semper.</p>";
        final String simpleParagraph = "<p>Lorem ipsum dolor sit amet.</p>";
        final StringBuilder structure = new StringBuilder();

        structure.setLength(0);
        structure.append("<html><body><h1>foo</h1>");
        for (int i = 5; i != 0; i--) {
            structure.append(complexParagraph);
        }
        structure.append("</body></html>");
        checkAccessibility.setContent(structure.toString());
        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_STRUCTURE));

        structure.setLength(0);
        structure.append("<html><body><h1>foo</h1>");
        for (int i = 20; i != 0; i--) {
            structure.append(simpleParagraph);
        }
        structure.append("</body></html>");
        checkAccessibility.setContent(structure.toString());
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_STRUCTURE));

        structure.setLength(0);
        structure.append("<html><body>");
        for (int i = 16; i != 0; i--) {
            structure.append(complexParagraph);
        }
        structure.append("</body></html>");
        checkAccessibility.setContent(structure.toString());
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_STRUCTURE));

        structure.setLength(0);
        structure.append("<html><body><h1>Foo</h1><h2>Bar</h2>");
        for (int i = 16; i != 0; i--) {
            structure.append(complexParagraph);
        }
        structure.append("</body></html>");
        checkAccessibility.setContent(structure.toString());
        evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(0, TestUtils.getNumProblems(evaluation.getProblems(), COMPLEX_STRUCTURE));
    }

}
