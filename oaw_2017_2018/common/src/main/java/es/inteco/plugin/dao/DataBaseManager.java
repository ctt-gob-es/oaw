package es.inteco.plugin.dao;

import es.inteco.common.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public final class DataBaseManager {

    private DataBaseManager() {
    }

    public static Connection getConnection() throws Exception {
        Logger.putLog("Conectando a la base de datos OAW", DataBaseManager.class, Logger.LOG_LEVEL_DEBUG);
        try {
            final Context initContext = new InitialContext();
            final Context envContext = (Context) initContext.lookup("java:/comp/env");
            final DataSource ds = (DataSource) envContext.lookup("jdbc/oaw");
            return ds.getConnection();
        } catch (Exception e) {
            Logger.putLog("Error al conectar a la base de datos OAW", DataBaseManager.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static void closeConnection(final Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la conexión a base de datos OAW", DataBaseManager.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }
}
