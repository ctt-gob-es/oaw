import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import es.inteco.common.Constants;
import es.inteco.common.IntavConstants;
import es.inteco.common.properties.PropertiesManager;

/**
 *
 */
public class PropertiesManagerTest {
	@Ignore
	@Test
	public void testPM() {
		final PropertiesManager pm = new PropertiesManager();
		Assert.assertNotNull(pm.getProperties(Constants.CRAWLER_CORE_PROPERTIES));
		Assert.assertNotNull(pm.getProperties(Constants.MAIL_PROPERTIES));
		Assert.assertEquals("sim", pm.getValue(Constants.MAIL_PROPERTIES, "mail.transport.protocol"));
		Assert.assertEquals("Programado", pm.getValue(Constants.CRAWLER_CORE_PROPERTIES, "crawler.user.name"));
		Assert.assertEquals("checks/checks.xml", pm.getValue("intav.properties", "check.path"));
		Assert.assertEquals("changeit", pm.getValue("certificados.properties", "truststore.pass"));
		Assert.assertNotNull(pm.getProperties("language.mapping"));
		Assert.assertNotNull(pm.getValue(IntavConstants.INTAV_PROPERTIES, "url.w3c.css.validator"));
		Assert.assertEquals("alvaro.pelaez@fundacionctic.org", pm.getValue(Constants.MAIL_PROPERTIES, "incomplete.crawler.warning.emails"));
	}

	@Ignore
	@Test
	public void testBasicServiceProperties() {
		final PropertiesManager pm = new PropertiesManager();
		Assert.assertNotNull(pm.getProperties(Constants.BASIC_SERVICE_PROPERTIES));
		Assert.assertNotNull(pm.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes"));
		Assert.assertEquals("Sí", pm.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.yes"));
		Assert.assertEquals("None", pm.getValue(Constants.BASIC_SERVICE_PROPERTIES, "basic.service.indomain.no"));
	}
}
