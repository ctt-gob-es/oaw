package es.inteco.intav.test;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.intav.TestUtils;
import es.inteco.intav.utils.EvaluatorUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase para comprobar un bug al parsear etiquetas iframe que se utilizan como etiquetas simples <iframe />
 */
public class ParserTest {

    private static final int IMG_ALT_ID = 1;

    @Test
    public void testIframes() throws Exception {
        EvaluatorUtility.initialize();
        CheckAccessibility checkAccessibility = TestUtils.getCheckAccessibility("observatorio-une-2012");
        checkAccessibility.setContent("<iframe /><img />");

        final Evaluation evaluation = EvaluatorUtils.evaluateContent(checkAccessibility, "es");
        Assert.assertEquals(1, TestUtils.getNumProblems(evaluation.getProblems(), IMG_ALT_ID));
    }

}
