package es.inteco.multilanguage.dao.utils;

import es.inteco.common.logging.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOUtils {
    public static void closeQueries(Statement st, ResultSet rs) throws SQLException {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.putLog("Exception al cerrar el resultset: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                Logger.putLog("Exception al cerrar el statement: ", DAOUtils.class, Logger.LOG_LEVEL_ERROR, e);
                throw e;
            }
        }
    }
}
