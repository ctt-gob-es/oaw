package es.inteco.rastreador2.utils;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public final class NormalizarCats {

    private NormalizarCats() {
    }

    public static void normaliza(int idCategory) {
        Connection c = null;
        Statement s = null;
        ResultSet rs = null;
        Statement s2 = null;
        try {
            c = DataBaseManager.getConnection();
            s = c.createStatement();
            float suma = 0;
            rs = s.executeQuery("SELECT * FROM categoria_termino WHERE id_categoria = " + idCategory);
            while (rs.next()) {
                suma = suma + rs.getFloat(3);
            }
            DAOUtils.closeQueries(s, rs);

            s = c.createStatement();
            rs = s.executeQuery("SELECT * FROM categoria_termino WHERE id_categoria = " + idCategory);
            while (rs.next()) {
                int id_termino = rs.getInt(2);
                float porcentaje = rs.getFloat(3);
                float porcentaje_norm = porcentaje / suma;
                s2 = c.createStatement();
                String sql2 = "UPDATE categoria_termino SET porcentaje_normalizado = " + porcentaje_norm + " WHERE id_categoria = " + idCategory + " AND id_termino = " + id_termino;
                s2.executeUpdate(sql2);
            }
        } catch (SQLException e) {
            Logger.putLog("EXCEPCIÓN accediendo a la BD: " + e.getMessage(), NormalizarCats.class, Logger.LOG_LEVEL_ERROR);
        } finally {
            try {
                DAOUtils.closeQueries(s, rs);
            } catch (SQLException e) {
                Logger.putLog("EXCEPCIÓN accediendo a la BD: " + e.getMessage(), NormalizarCats.class, Logger.LOG_LEVEL_ERROR);
            }
            try {
                DAOUtils.closeQueries(s2, null);
            } catch (SQLException e) {
                Logger.putLog("EXCEPCIÓN accediendo a la BD: " + e.getMessage(), NormalizarCats.class, Logger.LOG_LEVEL_ERROR);
            }
            DataBaseManager.closeConnection(c);
        }
    }

}
