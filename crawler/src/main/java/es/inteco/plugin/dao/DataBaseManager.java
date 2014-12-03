package es.inteco.plugin.dao;

import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.datos.DBConnect;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public class DataBaseManager {

    public static Connection getConnection() {
        PropertiesManager pmgr = new PropertiesManager();

        try {
            return getConnection(pmgr.getValue("crawler.properties", "datasource.name.system"));
        } catch (Exception e) {
            Logger.putLog("Error al conectar a la base de datos del sistema", DBConnect.class, Logger.LOG_LEVEL_ERROR, e);
            return null;
        }
    }

    public static Connection getConnection(String datasourceName) {
        Logger.putLog("Conectando a la base de datos " + datasourceName, DBConnect.class, Logger.LOG_LEVEL_DEBUG);
        Connection conn = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup(datasourceName);
            conn = ds.getConnection();
        } catch (Exception e) {
            Logger.putLog("Error al conectar a la base de datos " + datasourceName, DBConnect.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                Logger.putLog("Desconectando " + conn.getCatalog(), DataBaseManager.class, Logger.LOG_LEVEL_DEBUG);
                conn.close();
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la conexión a base de datos", DataBaseManager.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }
}
