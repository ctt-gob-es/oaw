package es.inteco.common.properties;

import es.inteco.common.logging.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesManager {

    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<String, Properties>();

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

    public final String getValue(final String propertiesFile, final String key) {
        return getProperties(propertiesFile).getProperty(key);
    }

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
        } catch (IOException ioe) {
            Logger.putLog("Excepción de entrada/salida: ", PropertiesManager.class, Logger.LOG_LEVEL_ERROR, ioe);
            throw ioe;
        }
    }

}
