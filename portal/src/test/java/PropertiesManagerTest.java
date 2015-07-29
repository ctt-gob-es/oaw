import es.inteco.common.IntavConstants;
import es.inteco.common.properties.PropertiesManager;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class PropertiesManagerTest {

    @Test
    public void testPM() {
        final PropertiesManager pm = new PropertiesManager();
        Assert.assertNotNull(pm.getProperties("crawler.core.properties"));

        Assert.assertEquals("smtp", pm.getValue("crawler.core.properties", "mail.transport.protocol"));

        Assert.assertEquals("Programado", pm.getValue("crawler.core.properties", "crawler.user.name"));

        Assert.assertEquals("checks/checks.xml", pm.getValue("intav.properties", "check.path"));

        Assert.assertEquals("changeit", pm.getValue("certificados.properties", "truststore.pass"));

        Assert.assertNotNull(pm.getProperties("language.mapping"));

        Assert.assertNotNull(pm.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.css.validator"));
    }

}
