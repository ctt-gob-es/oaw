package es.inteco.intav.checks;

import es.inteco.common.CheckAccessibility;
import es.inteco.intav.EvaluateCheck;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class CheckRedundantImgAltTest extends EvaluateCheck {

    private static final int REDUNDANT_IMG_ALT = 428;
    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateCorrectLink() throws Exception {
        checkAccessibility.setContent("<a>Lorem <img alt=\"Ipsum\"/></a>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));
    }

    @Test
    public void evaluateWhiteSpaces() throws Exception {
        checkAccessibility.setContent("<a>Lorem <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));

        checkAccessibility.setContent("<a><img alt=\"RSS\" title=\"RSS\" /> RSS</a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));

        checkAccessibility.setContent("<a><img alt=\"RSS\" title=\"RSS\" />     RSS</a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));

        checkAccessibility.setContent("<a><img alt=\"RSS\" title=\"RSS\" />&nbsp;RSS</a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));

        checkAccessibility.setContent("<a><img alt=\"RSS \" title=\"RSS\" /> RSS</a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));
    }

    @Test
    public void evaluateInlineTags() throws Exception {
        checkAccessibility.setContent("<a><strong>Lorem</strong> <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));

        checkAccessibility.setContent("<a><strong>L</strong>orem <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));

        checkAccessibility.setContent("<a><strong><em>Lorem</em></strong> <img alt=\"Lorem\"/></a>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, REDUNDANT_IMG_ALT));
    }

}
