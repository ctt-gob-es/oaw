package es.inteco.crawler.sexista.core.util;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOUtils {
    public static void close(Connection conn, Statement st, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.getRootLogger().error("Exception al cerrar el resultset");
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                Logger.getRootLogger().error("Exception al cerrar el resultset");
            }
        }

        ConexionBBDD.desconectar(conn);
    }
}
