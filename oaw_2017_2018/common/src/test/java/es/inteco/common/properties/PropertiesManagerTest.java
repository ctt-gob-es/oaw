package es.inteco.common.properties;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 *
 */
public class PropertiesManagerTest {

    @Test
    public void testGetPropertiesFileNoExists() {
        PropertiesManager propertiesManager = new PropertiesManager();
        Assert.assertNotNull(propertiesManager.getProperties("no_existe.properties"));
    }

    @Test
    public void testGetValueFileNoExists() {
        PropertiesManager propertiesManager = new PropertiesManager();
        Assert.assertNull(propertiesManager.getValue("no_existe.properties", "no_existe_key"));
    }


    @Test
    public void testGetValueKeyNoExists() {
        PropertiesManager propertiesManager = new PropertiesManager();
        Assert.assertNull(propertiesManager.getValue("example.properties", "no_existe_key"));
    }

    @Test
    public void testGetValueKeyExists() {
        PropertiesManager propertiesManager = new PropertiesManager();
        Assert.assertEquals("passed", propertiesManager.getValue("example.properties", "exists_key"));
    }

    @Test
    public void testPM() {
        final PropertiesManager pm = new PropertiesManager();
        // Se carga el fichero mail.properties con la clave crawler.core.properties (para que sea backwards-compatible)
        // Ver fichero propertiesmanager.properties
        Assert.assertNotNull(pm.getProperties("mail.properties"));
        // Esta cadena la reconoce porque está en el fichero mail.properties
        Assert.assertEquals("test", pm.getValue("mail.properties", "mail.transport.protocol"));
        // Esta cadena no existe porque no está en el fichero mail.properties
        Assert.assertNull(pm.getValue("mail.properties", "crawler.user.name"));
    }

}
