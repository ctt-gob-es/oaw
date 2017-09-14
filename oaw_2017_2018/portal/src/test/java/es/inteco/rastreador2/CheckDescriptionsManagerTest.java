package es.inteco.rastreador2;

import junit.framework.Assert;
import org.junit.Test;

import es.gob.oaw.rastreador2.pdf.utils.CheckDescriptionsManager;

import java.io.IOException;

/**
 * @author miguel.garcia
 */
public class CheckDescriptionsManagerTest {

    @Test
    public void testFileIsValid() throws IOException {
        final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
    }


    @Test
    public void testValue() throws IOException {
        final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
        final String value = checkDescriptionsManager.getString("a");

        Assert.assertEquals("lorem ipsum", value);
    }

    @Test
    public void testAccentuatedValue() throws IOException {
        final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
        final String value = checkDescriptionsManager.getString("b");

        Assert.assertEquals("á", value);
    }

    @Test
    public void testErrorMessage() throws IOException {
        final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
        final String value = checkDescriptionsManager.getErrorMessage("1");

        Assert.assertEquals("error", value);
    }

    @Test
    public void testRationaleMessage() throws IOException {
        final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
        final String value = checkDescriptionsManager.getRationaleMessage("1");

        Assert.assertEquals("rationale", value);
    }

    @Test
    public void testDefaultErrorMessage() throws IOException {
        final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
        final String value = checkDescriptionsManager.getErrorMessage("2");

        Assert.assertEquals("default error", value);
    }

    @Test
    public void testDefaultRationaleMessage() throws IOException {
        final CheckDescriptionsManager checkDescriptionsManager = new CheckDescriptionsManager();
        final String value = checkDescriptionsManager.getRationaleMessage("2");

        Assert.assertEquals("default rationale", value);
    }

}
