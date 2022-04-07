/*******************************************************************************
* Copyright (C) 2017 MINHAFP, Ministerio de Hacienda y Función Pública, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
******************************************************************************/
package es.inteco.common.properties;

import org.junit.Assert;
import org.junit.Test;

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
