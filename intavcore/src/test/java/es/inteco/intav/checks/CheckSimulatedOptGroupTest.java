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
public class CheckSimulatedOptGroupTest extends EvaluateCheck {

    private static final int SIMULATED_OPTGROUP_ID = 417;

    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateNoOptgroup() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "<option>Opción 4</option>" +
                "<option>Opción 5</option>" +
                "<option>Opción 6</option>" +
                "<option>Opción 7</option>" +
                "<option>Opción 8</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP_ID));
    }

    @Test
    public void evaluate2NoOptgroup() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>* Opción 1</option>" +
                "<option>* Opción 2</option>" +
                "<option>* Opción 3</option>" +
                "</select>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP_ID));
    }

    @Test
    public void evaluateOnly2Items() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>--- Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP_ID));
    }

    @Test
    public void evaluateMixedChars() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>**- Opción 1</option>" +
                "<option>Opción 2</option>" +
                "<option>Opción 3</option>" +
                "</select>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP_ID));
    }

    @Test
    public void evaluateTwoGroups() throws Exception {
        checkAccessibility.setContent("<select>" +
                "<option>***- Opción A</option>" +
                "<option>Opción 1</option>" +
                "<option>***- Opción B</option>" +
                "<option>Opción 2</option>" +
                "</select>");
        Assert.assertEquals(2, getNumProblems(checkAccessibility, SIMULATED_OPTGROUP_ID));
    }

}

