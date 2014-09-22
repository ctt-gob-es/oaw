package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class BorradorTerminos {

    private BorradorTerminos() {
    }

    public static void borra() {
        Connection c = null;
        Statement s = null;
        ResultSet rs = null;
        Statement s2 = null;
        ResultSet rs2 = null;
        try {
            c = DataBaseManager.getConnection();
            s = c.createStatement();
            rs = s.executeQuery("SELECT * FROM termino");
            while (rs.next()) {
                int cont = 0;
                int id_termino = rs.getInt(1);
                s2 = c.createStatement();
                String sql2 = "SELECT * FROM categoria_termino WHERE id_termino = " + id_termino;
                rs2 = s2.executeQuery(sql2);
                while (rs2.next()) {
                    cont++;
                }
                DAOUtils.closeQueries(s2, rs2);
                if (cont == 0) {
                    s2 = c.createStatement();
                    sql2 = "DELETE FROM termino WHERE id_termino = " + id_termino;
                    s2.executeUpdate(sql2);
                }
            }
        } catch (SQLException e) {
            Logger.putLog("EXCEPCIÓN accediendo a la BD: " + e.getMessage(), BorradorTerminos.class, Logger.LOG_LEVEL_ERROR);
        } finally {
            try {
                DAOUtils.closeQueries(s, rs);
            } catch (SQLException e) {
                Logger.putLog("EXCEPCIÓN cerrando las consultas: " + e.getMessage(), BorradorTerminos.class, Logger.LOG_LEVEL_ERROR);
            }
            try {
                DAOUtils.closeQueries(s2, rs2);
            } catch (SQLException e) {
                Logger.putLog("EXCEPCIÓN cerrando las consultas: " + e.getMessage(), BorradorTerminos.class, Logger.LOG_LEVEL_ERROR);
            }
            DataBaseManager.closeConnection(c);
        }
    }

}
