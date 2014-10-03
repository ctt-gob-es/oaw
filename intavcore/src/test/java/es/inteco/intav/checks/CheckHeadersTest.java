package es.inteco.intav.checks;

import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class CheckHeadersTest extends EvaluateCheck {

    private static final int HEADERS_NESTING_ID = 37;
    private static final int EXISTS_HEADERS_ID = 38;
    private static final int HEADERS_BLANK_ID = 395;
    private static final int EXISTS_H1_ID = 421;
    private static final int SAME_LEVEL_HEADERS_NO_CONTENT_ID = 422;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateCorrectNesting() throws Exception {
        checkAccessibility.setContent("<html><body><h1>Lorem</h1><p>Ipsum></p><h2>Sic semper</h2><p>Foo</p></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, HEADERS_NESTING_ID));
    }

    @Test
    public void evaluateCorrectNestingComplex() throws Exception {
        checkAccessibility.setContent("<html><body><h1>Lorem</h1><p>Ipsum></p><h2>Sic semper</h2><p>Foo</p><h3>Bar</h3><p>Lorem</p><h1>Foo</h1></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, HEADERS_NESTING_ID));
    }

    @Test
    public void evaluateIncorrectNestingStartFirstLevel() throws Exception {
        checkAccessibility.setContent("<html><body><h1>Lorem</h1><p>Ipsum></p><h4>Sic semper</h4><p>Foo</p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, HEADERS_NESTING_ID));
    }

    @Test
    public void evaluateIncorrectNestingStartRandomLevel() throws Exception {
        checkAccessibility.setContent("<html><body><h2>Lorem</h2><p>Ipsum></p><h4>Sic semper</h4><p>Foo</p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, HEADERS_NESTING_ID));
    }

    @Test
    public void evaluateNoHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><p>Ipsum></p><p>Foo</p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, EXISTS_HEADERS_ID));
    }

    @Test
    public void evaluateBlankHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1></h1><p>Ipsum></p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateImgAltHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><img alt=\"Foo bar\"/></h1><p>Ipsum></p></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateImgAltBlankHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><img alt=\"\"/></h1><p>Ipsum></p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateDivHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><div>Foo bar</div></h1><p>Ipsum></p></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateMarkHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1><mark>Foo bar</mark></h1><p>Ipsum></p></body></html>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, HEADERS_BLANK_ID));
    }

    @Test
    public void evaluateNoH1() throws Exception {
        checkAccessibility.setContent("<html><body><h3>Foo bar</h3><p>Ipsum></p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, EXISTS_H1_ID));
    }

    @Test
    public void evaluateAdjacentSameHeadings() throws Exception {
        checkAccessibility.setContent("<html><body><h1>foo</h1><h2>Foo bar</h2><h2>Ipsum</h2><p>Some content<p></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SAME_LEVEL_HEADERS_NO_CONTENT_ID));
    }

    @Test
    public void evaluateLastHeadingNoContent() throws Exception {
        checkAccessibility.setContent("<html><body><h1>foo</h1><h2>Ipsum</h2><p>Some content<p><h2>No content</h2></body></html>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SAME_LEVEL_HEADERS_NO_CONTENT_ID));
    }

}
