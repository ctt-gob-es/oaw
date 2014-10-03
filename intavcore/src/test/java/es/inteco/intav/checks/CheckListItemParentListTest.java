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
public class CheckListItemParentListTest extends EvaluateCheck {

    private static final int LI_PARENT_UL_OL = 311;

    @Test
    public void evaluateLiParent() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "</ul>" +
                "<ol>" +
                "<li>3) Opción 3</li>" +
                "<li>4) Opción 4</li>" +
                "</ol>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems(), LI_PARENT_UL_OL);

        Assert.assertEquals(0, numProblems);
    }

    @Test
    public void evaluateLiNoParent() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<ul>" +
                "<li>1) Opción 1</li>" +
                "<li>2) Opción 2</li>" +
                "</ul>" +
                "<li>Opción sin padre</li>");

        Assert.assertEquals(1, getNumProblems(checkAccessibility, LI_PARENT_UL_OL));
    }

    @Test
    public void evaluateLiBlank() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility("observatorio-2.0");
        checkAccessibility.setContent("<ul>" +
                "<li></li>" +
                "<li>Item 1</li>" +
                "<li>Item 2</li>" +
                "</ul>");

        Assert.assertEquals(1, getNumProblems(checkAccessibility, LI_PARENT_UL_OL));
    }

}

