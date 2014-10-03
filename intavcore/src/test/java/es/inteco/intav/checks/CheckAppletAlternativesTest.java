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
public class CheckAppletAlternativesTest extends EvaluateCheck {

    private static final int APPLET_ALTERNATIVES_ID = 414;
    private CheckAccessibility checkAccessibility;

    @Before
    public void setUp() throws Exception {
        checkAccessibility = getCheckAccessibility("observatorio-2.0");
    }

    @Test
    public void evaluateNoAlternatives() throws Exception {
        checkAccessibility.setContent("<applet><param /><param />\r\n</applet>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateOnlyAltAttribute() throws Exception {
        checkAccessibility.setContent("<applet alt=\"Lorem ipsum\"></applet>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateOnlyTextAlternative() throws Exception {
        checkAccessibility.setContent("<applet>Lorem ipsum</applet>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateAltAndTextAlternative() throws Exception {
        checkAccessibility.setContent("<applet alt=\"Sic semper\">Lorem ipsum</applet>");
        Assert.assertEquals(0, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

    @Test
    public void evaluateAltBlankNoTextAlternative() throws Exception {
        checkAccessibility.setContent("<applet alt=\"\"></applet>");
        Assert.assertEquals(1, getNumProblems(checkAccessibility, APPLET_ALTERNATIVES_ID));
    }

}
