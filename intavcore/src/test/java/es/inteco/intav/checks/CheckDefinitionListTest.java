package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class CheckDefinitionListTest extends EvaluateCheck {

    private static final int DL_INCORRECT = 427;
    private static final int DT_OUTSIDE_DL = 313;
    private static final int DD_OUTSIDE_DL = 314;
    private static final int EMPTY_DEFINITION_LIST = 425;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateDlCorrectList() throws Exception {
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, DL_INCORRECT));

        // Es válido usar dos dt seguidos para definir dos términos a la vez (sinónimos)
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "<dt>Term 3</dt>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, DL_INCORRECT));
    }

    @Test
    public void evaluateDlIncorrectList() throws Exception {
        checkAccessibility.setContent("<dl>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "<dt>Term 3</dt>" +
                "<dd>Definition 2</dd>" +
                "</dl>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dt>Term 2</dt>" +
                "</dl>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, DL_INCORRECT));

        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<div>Lorem</div>" +
                "</dl>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, DL_INCORRECT));

        // Atención este error es una diferencia respecto a la misma comprobación en la v1.0
        checkAccessibility.setContent("<dl>" +
                "<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<ul><li>Lorem</li></ul>" +
                "</dl>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, DL_INCORRECT));
    }

    @Test
    public void evaluateDtOutsideDl() throws Exception {
        checkAccessibility.setContent("<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>");

        Assert.assertEquals(1, getNumProblems(checkAccessibility, DT_OUTSIDE_DL));
    }

    @Test
    public void evaluateDdOutsideDl() throws Exception {
        checkAccessibility.setContent("<dt>Term 1</dt>" +
                "<dd>Definition 1</dd>" +
                "<dd>Definition 2</dd>");

        Assert.assertEquals(2, getNumProblems(checkAccessibility, DD_OUTSIDE_DL));
    }

    @Test
    public void evaluateBlankList() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<dl></dl>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, EMPTY_DEFINITION_LIST));


        checkAccessibility.setContent("<dl><dl><dt>Foo</dt><dd>Bar</dd></dl></dl>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, EMPTY_DEFINITION_LIST));
    }

}

