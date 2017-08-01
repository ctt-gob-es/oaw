package es.inteco.intav.test;

import ca.utoronto.atrc.tile.accessibilitychecker.AllChecks;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import ca.utoronto.atrc.tile.accessibilitychecker.Guideline;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class ResourcesTest {

    @Test
    public void testChecksResource() throws Exception {
        EvaluatorUtility.initialize();
        AllChecks allChecks = EvaluatorUtility.getAllChecks();
        Assert.assertFalse(allChecks.isEmpty());
    }

    @Test
    public void testGuidelinesResource() throws Exception {
        EvaluatorUtility.initialize();
        final Guideline guideline = EvaluatorUtility.loadGuideline("observatorio-inteco-1-0.xml");

        Assert.assertNotNull(guideline);
        Assert.assertEquals("Observatorio INTECO 1.0", guideline.getName());
        Assert.assertEquals("Pautas de verificación del Observatorio de INTECO", guideline.getLongName());
        Assert.assertEquals("observatory", guideline.getType());

        Assert.assertEquals(2, guideline.getGroups().size());
    }

    @Test
    public void testLoadResources() throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("guidelines/observatorio-une-2012.xml");
        Assert.assertNotNull(is);
        is.close();
    }

}
