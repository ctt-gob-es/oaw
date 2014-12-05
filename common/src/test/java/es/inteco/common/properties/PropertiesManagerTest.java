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
    public void testLoadPropertiesFile() {
        try {
            PropertiesManager.loadProperties("file:///home/mikunis/proyectos/minhap/oaw/portal/profiles/desarrollo/oaw.properties");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Excepción al intentar cargar el fichero de propiedades");
        }
    }

}
