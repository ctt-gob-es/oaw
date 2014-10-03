package es.inteco.intav.checks;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class CheckDataTableTest extends EvaluateCheck {

    private final int TABLE_HEADINGS_ID = 415;
    private final int SAME_CAPTION_SUMMARY_CHECK = 243;
    private final int MISSING_SCOPE_CHECK = 244;
    private final int MISSING_ID_HEADERS_CHECK = 245;

    @Test
    public void evaluateNoScopes() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<table><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID);

        Assert.assertEquals(0, numProblems);
    }

    @Test
    public void evaluateScopesValid() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<table><tr><th scope=\"column\">Header 1</th><th scope=\"row\">Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID);

        Assert.assertEquals(0, numProblems);
    }

    @Test
    public void evaluateScopesInvalid() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<html><body><table><tr><th scope=\"foo\">Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID);

        Assert.assertEquals(1, numProblems);
    }


    @Test
    public void evaluateSameCaptionAndSummary() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><caption>Same text</caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");

        Assert.assertEquals(1, getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK) );
        Assert.assertEquals(0, getNumProblems(evaluation.getProblems(), TABLE_HEADINGS_ID) );
    }

    @Test
    public void evaluateSameCaptionAndSummary2() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<html><body><table summary=\"Same text\"><caption><span><strong>Same</strong> text</span></caption><tr><th>Header 1</th><th>Header 2</th></tr><tr><td>Cell 1:1</td><td>Cell 1:2</td></tr><td>Cell 2:1</td><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), SAME_CAPTION_SUMMARY_CHECK);

        Assert.assertEquals(1, numProblems);
    }

    @Test
    public void evaluateTableComplexNoSummary() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<html><body><table><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), 418);

        Assert.assertEquals(1, numProblems);
    }

    @Test
    public void evaluateTableComplexSummaryBlank() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<html><body><table summary=\"\"><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), 418);

        Assert.assertEquals(1, numProblems);
    }

    @Test
    public void evaluateTableComplexSummary() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0-testing");
        checkAccessibility.setContent("<html><body><table summary=\"Table summary\"><tr><th>Header 1</th><th>Header 2</th></tr><tr><th>Cell 1:1</th><td>Cell 1:2</td></tr><th>Cell 2:1</th><td>Cell 2:2</td></tr></table></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), 418);

        Assert.assertEquals(0, numProblems);
    }

}
