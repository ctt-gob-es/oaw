package es.inteco.common.properties;

import es.inteco.common.logging.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesManager {

    private static final Map<String, Properties> PROPERTIES_MAP = new HashMap<String, Properties>();

    static {
        initProperties();
    }

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
            is.close();
        } catch (IOException ioe) {
            Logger.putLog("Excepción de entrada/salida: ", PropertiesManager.class, Logger.LOG_LEVEL_ERROR, ioe);
            throw ioe;
        }
    }

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

    private static Properties mergeProperties(final Properties properties1, final Properties properties2) {
        final Properties merged = new Properties();
        merged.putAll(properties1);
        merged.putAll(properties2);

        return merged;
    }

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
