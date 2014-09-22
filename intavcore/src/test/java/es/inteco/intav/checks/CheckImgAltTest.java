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
public class CheckImgAltTest extends EvaluateCheck {

    @Override
    public int getId() {
        return 1;
    }

    @Test
    public void evaluate_1() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility();
        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\">, <img src=\"\"></p></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems());

        Assert.assertEquals(numProblems, 2);
    }

    @Test
    public void evaluate_2() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility();
        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" alt></p></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems());

        Assert.assertEquals(numProblems, 0);
    }


    @Test
    public void evaluate_3() throws Exception {
        CheckAccessibility checkAccessibility = getCheckAccessibility();
        checkAccessibility.setContent("<html><body><p>Esto es una prueba <img src=\"\" alt=\"\"></p></body></html>");

        Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        int numProblems = getNumProblems(evaluation.getProblems());

        Assert.assertEquals(numProblems, 0);
    }

}
