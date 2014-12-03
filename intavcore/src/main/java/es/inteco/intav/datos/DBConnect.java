package es.inteco.intav.datos;

import es.inteco.common.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public final class DBConnect {

    private DBConnect() {
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/intav");
            conn = ds.getConnection();
            Logger.putLog("Conectando a la base de datos de INTAV", DBConnect.class, Logger.LOG_LEVEL_DEBUG);
        } catch (Exception e) {
            Logger.putLog("Error al conectar a la base de datos de INTAV", DBConnect.class, Logger.LOG_LEVEL_ERROR, e);
        }

        return conn;
    }

    public static void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                Logger.putLog("Desconectando de la base de datos de INTAV", DBConnect.class, Logger.LOG_LEVEL_DEBUG);
            } catch (Exception ex) {
                Logger.putLog("Error al desconectar de la base de datos", DBConnect.class, Logger.LOG_LEVEL_ERROR, ex);
            }
        }
    }
}