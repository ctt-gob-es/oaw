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
public class CheckUnsortedListTest extends EvaluateCheck {

    private static final int LI_PARENT_UL_OL = 311;
    private static final int LIST_NESTED_DIRECTLY = 318;
    private static final int INCORRECT_UNSORTED_LIST = 320;
    private static final int UNSORTED_EMPTY_LIST = 424;
    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateLiParent() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "</ul>" +
                "<ol>" +
                "<li>3) Opción 3</li>" +
                "<li>4) Opción 4</li>" +
                "</ol>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LI_PARENT_UL_OL));
    }

    @Test
    public void evaluateListNestedDirectly() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<ul><li>Subitem 1</li><li>Subitem 2</li></ul>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LIST_NESTED_DIRECTLY));

        checkAccessibility.setContent("<ul>" +
                "<ol><li>Subitem 1</li><li>Subitem 2</li></ol>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LIST_NESTED_DIRECTLY));
    }

    @Test
    public void evaluateIncorrectOrderedList() throws Exception {
        checkAccessibility.setContent("<ul>" +
                "<li>Item 1</li>" +
                "<div>Item 2</div>" +
                "<li>Item 3</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, INCORRECT_UNSORTED_LIST));

        checkAccessibility.setContent("<ul>" +
                "<li>Item 1</li>" +
                "<p>Item 2</p>" +
                "<li>Item 3</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, INCORRECT_UNSORTED_LIST));

        checkAccessibility.setContent("<ul>" +
                "<li>Item 1</li>" +
                "<h2>Item 2</h2>" +
                "<li>Item 3</li>" +
                "</ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, INCORRECT_UNSORTED_LIST));
    }

    @Test
    public void evaluateBlankList() throws Exception {
        checkAccessibility.setContent("<ul></ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UNSORTED_EMPTY_LIST));

        checkAccessibility.setContent("<ul><ul><li>Foo</li></ul></ul>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, UNSORTED_EMPTY_LIST));
    }

}

