package es.inteco.common.plugin;

import es.inteco.plugin.FactoryCartuchos;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 */
public class FactoryCartuchosTest {

    @Test
    public void testGetCartuchoNoExists() {
        Assert.assertNull(FactoryCartuchos.getCartucho("no_existe"));
    }

    @Test
    public void testGetCartuchoExists() {
        Assert.assertNotNull(FactoryCartuchos.getCartucho("es.inteco.common.plugin.MockCartucho"));
    }

}
