package es.inteco.crawler.sexista.core.util;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Clase ConfigUtil.
 *
 * @author GPM
 */
public class ConfigUtil {

    /**
     * M&eacute;todo getConfiguracion.
     *
     * @return Properties de la aplicación
     */
    public static Properties getConfiguracion() {

        Properties properties = new Properties();
        InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream("lenox.properties");

        try {

            properties.load(is);

        } catch (IOException e) {
            Logger.getLogger(ConfigUtil.class).error(e.getMessage());
        } finally {

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    Logger.getLogger(ConfigUtil.class).error(e.getMessage());
                }
            }
        }

        return properties;
    }

    /**
     * M&eacute;todo getConfiguracion.
     * Devuelve el properties de la ruta que le pasamos como parámetro.
     *
     * @param ruta Ruta del properties
     * @return Properties de la aplicación
     */
    public static Properties getConfiguracion(String ruta) {

        Properties properties = new Properties();
        InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(ruta);

        try {

            properties.load(is);

        } catch (IOException e) {
            Logger.getLogger(ConfigUtil.class).error(e.getMessage());
        } finally {

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    Logger.getLogger(ConfigUtil.class).error(e.getMessage());
                }
            }
        }

        return properties;
    }

}
