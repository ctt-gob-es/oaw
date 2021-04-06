/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
package es.inteco.common.properties;

import es.inteco.common.logging.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The Class PropertiesManager.
 */
public class PropertiesManager {

    /** The Constant PROPERTIES_MAP. */
    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<>();

    static {
        initProperties();
    }

    /**
	 * Gets the properties.
	 *
	 * @param propertiesFile the properties file
	 * @return the properties
	 */
    public final Properties getProperties(final String propertiesFile) {
        try {
            if (!PROPERTIES_MAP.containsKey(propertiesFile)) {
                loadProperties(propertiesFile);
            }

            return PROPERTIES_MAP.get(propertiesFile);
        } catch (IOException e) {
            Logger.putLog("Error al acceder al fichero de propiedades " + propertiesFile, getClass(), Logger.LOG_LEVEL_ERROR, e);
            return new Properties();
        }
    }

    /**
	 * Gets the value.
	 *
	 * @param propertiesFile the properties file
	 * @param key            the key
	 * @return the value
	 */
    public final String getValue(final String propertiesFile, final String key) {
        return getProperties(propertiesFile).getProperty(key);
    }

    /**
	 * Load properties.
	 *
	 * @param propertiesFile the properties file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    private void loadProperties(final String propertiesFile) throws IOException {
        try {
            Properties properties = new Properties();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(propertiesFile);
            if (is != null) {
                properties.load(is);
                PROPERTIES_MAP.put(propertiesFile, properties);
            } else {
                throw new FileNotFoundException("El fichero de propiedades " + propertiesFile + " no se ha encontrado");
            }
            is.close();
        } catch (IOException ioe) {
            Logger.putLog("Excepción de entrada/salida: ", PropertiesManager.class, Logger.LOG_LEVEL_ERROR, ioe);
            throw ioe;
        }
    }

    /**
	 * Inits the properties.
	 */
    private static void initProperties() {
        final InputStream stream = PropertiesManager.class.getResourceAsStream("/propertiesmanager.properties");

        Properties props;
        if (stream != null) {
            try {
                props = new Properties();
                props.load(stream);
                String[] propertiesFiles = props.getProperty("properties.files").split(",");
                for (String propertiesFile : propertiesFiles) {
                    propertiesFile = propertiesFile.trim();
                    final String key = props.getProperty("key." + propertiesFile);
                    final String file = props.getProperty("file." + propertiesFile);
                    try {
                        addPropertyFile(key, file);
                    } catch (Exception e) {
                        Logger.putLog("FALLO PropertiesManager not initialized. File " + propertiesFile + " missing ", PropertiesManager.class, Logger.LOG_LEVEL_ERROR);
                    }
                }
            } catch (IOException e) {
                Logger.putLog("IOException while trying to load property file.", PropertiesManager.class, Logger.LOG_LEVEL_ERROR);
            }
            try {
                stream.close();
            } catch (IOException e) {
            }
        } else {
            Logger.putLog("FALLO no se ha encontrado el fichero de configuración propertiesmanager.properties", PropertiesManager.class, Logger.LOG_LEVEL_ERROR);
        }
    }

    /**
	 * Adds the property file.
	 *
	 * @param key  the key
	 * @param file the file
	 */
    private static void addPropertyFile(final String key, final String file) {
        if (key != null && file != null) {
            final Properties properties = loadPropertyFile(file);
            if (properties != null) {
                if (PROPERTIES_MAP.containsKey(key)) {
                    PROPERTIES_MAP.put(key, mergeProperties(PROPERTIES_MAP.get(key), properties));
                } else {
                    PROPERTIES_MAP.put(key, properties);
                }
            }
        }
    }

    /**
	 * Merge properties.
	 *
	 * @param properties1 the properties 1
	 * @param properties2 the properties 2
	 * @return the properties
	 */
    private static Properties mergeProperties(final Properties properties1, final Properties properties2) {
        final Properties merged = new Properties();
        merged.putAll(properties1);
        merged.putAll(properties2);

        return merged;
    }

    /**
	 * Load property file.
	 *
	 * @param file the file
	 * @return the properties
	 */
    private static Properties loadPropertyFile(final String file) {
        try {
            final Properties properties = new Properties();
            final InputStream is;
            if (file.startsWith("file:/")) {
                is = new URL(file).openStream();
            } else {
                is = PropertiesManager.class.getResourceAsStream(file);
            }
            properties.load(is);
            is.close();
            return properties;
        } catch (IOException ioe) {
            Logger.putLog("Excepción de entrada/salida: ", PropertiesManager.class, Logger.LOG_LEVEL_ERROR, ioe);
        }
        return null;
    }

}
