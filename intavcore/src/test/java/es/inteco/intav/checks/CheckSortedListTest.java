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
public class CheckSortedListTest extends EvaluateCheck {

    private static final int LI_PARENT_UL_OL = 311;
    private static final int LIST_NESTED_DIRECTLY = 317;
    private static final int INCORRECT_SORTED_LIST = 319;
    private static final int SORTED_EMPTY_LIST = 423;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateLiParent() throws Exception {
        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "<li><ol><li>Subitem 1</li><li>Subitem 2</li></ol></li>" +
                "<li><ul><li>Subitem 1</li><li>Subitem 2</li></ul></li>" +
                "</ol>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, LI_PARENT_UL_OL));
    }

    @Test
    public void evaluateListNestedDirectly() throws Exception {
        checkAccessibility.setContent("<ol>" +
                "<ul><li>Subitem 1</li><li>Subitem 2</li></ul>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ol>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LIST_NESTED_DIRECTLY));

        checkAccessibility.setContent("<ol>" +
                "<ol><li>Subitem 1</li><li>Subitem 2</li></ol>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ol>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, LIST_NESTED_DIRECTLY));
    }

    @Test
    public void evaluateIncorrectOrderedList() throws Exception {
        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<div>Item 2</div>" +
                "<li>Item 3</li>" +
                "</ol>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, INCORRECT_SORTED_LIST));

        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<p>Item 2</p>" +
                "<li>Item 3</li>" +
                "</ol>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, INCORRECT_SORTED_LIST));

        checkAccessibility.setContent("<ol>" +
                "<li>Item 1</li>" +
                "<h2>Item 2</h2>" +
                "<li>Item 3</li>" +
                "</ol>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, INCORRECT_SORTED_LIST));
    }

    @Test
    public void evaluateBlankList() throws Exception {
        checkAccessibility.setContent("<ol></ol>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SORTED_EMPTY_LIST));


        checkAccessibility.setContent("<ol><ol><li>Foo</li></ol></ol>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SORTED_EMPTY_LIST));
    }

}

