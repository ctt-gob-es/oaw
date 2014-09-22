package es.inteco.crawler.sexista.core.util;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase ConexionBBDD.
 *
 * @author GPM
 */
public class ConexionBBDD {

    public static Connection conectar() {
        PropertiesManager pmgr = new PropertiesManager();

        try {
            return conectar(pmgr.getValue("lenox.properties", "datasource.name"));
        } catch (Exception e) {
            Logger.putLog("Error al conectar a la base de datos del sistema", ConexionBBDD.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }
    }

    public static Connection conectar(String datasourceName) {
        Logger.putLog("Conectando a la base de datos " + datasourceName, ConexionBBDD.class, Logger.LOG_LEVEL_INFO);
        Connection conn = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup(datasourceName);
            conn = ds.getConnection();
        } catch (Exception e) {
            Logger.putLog("Error al conectar a la base de datos " + datasourceName, ConexionBBDD.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return conn;
    }

    /**
     * M&eacute;todo desconectar.
     *
     * @param con Conexión
     * @throws SQLException SQLException
     */
    public static void desconectar(Connection con) {
        try {
            if (con != null) {
                Logger.putLog("Cerrando conexión a base de datos", ConexionBBDD.class, Logger.LOG_LEVEL_INFO);
                con.close();
            }
        } catch (Exception ex) {
            Logger.putLog("Excepción: ", ConexionBBDD.class, Logger.LOG_LEVEL_ERROR, ex);
        }
    }

}