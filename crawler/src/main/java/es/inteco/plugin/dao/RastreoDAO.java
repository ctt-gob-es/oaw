package es.inteco.plugin.dao;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class RastreoDAO {

    private static final Log LOG = LogFactory.getLog(RastreoDAO.class);

    private RastreoDAO() {
    }

    public static void stopRunningObservatories(final Connection connection) throws Exception {
        try (PreparedStatement pst = connection.prepareStatement("UPDATE observatorios_realizados SET Estado = ? WHERE Estado = ?")) {
            pst.setInt(1, Constants.OBS_ERROR_STATUS);
            pst.setInt(2, Constants.OBS_LAUNCHED_STATUS);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Error al actualizar el estado del rastreo", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static void actualizarEstadoRastreo(Connection c, long idRastreo, int status) {
        if (idRastreo != -1) {
            try (PreparedStatement pst = c.prepareStatement("UPDATE rastreo SET estado = ? WHERE id_rastreo = ?")) {
                pst.setInt(1, status);
                pst.setLong(2, idRastreo);
                pst.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Error al actualizar el estado del rastreo");
            }
        }
    }

    public static boolean isAnyCrawlingActive(Connection c) throws Exception {
        try (PreparedStatement pst = c.prepareStatement("SELECT id_rastreo FROM rastreo WHERE estado = ?")) {
            pst.setInt(1, Constants.STATUS_LAUNCHED);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOG.error("Error al actualizar el estado del rastreo");
            throw e;
        }
    }

    public static void stopRunningCrawlings(Connection c) throws Exception {
        try (PreparedStatement pst = c.prepareStatement("UPDATE rastreo SET estado = ? WHERE estado = ?")) {
            pst.setInt(1, Constants.STATUS_STOPPED);
            pst.setInt(2, Constants.STATUS_LAUNCHED);
            pst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Error al actualizar el estado del rastreo");
            throw e;
        }
    }

    public static String getList(Connection conn, Long idCrawling, int type) throws Exception {
        java.sql.PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String onClause = "";
            if (type == Constants.ID_LISTA_SEMILLA || type == Constants.ID_LISTA_OBSERVATORIO) {
                onClause = "ON (r.semillas = l.id_lista) ";
            } else if (type == Constants.ID_LISTA_RASTREABLE) {
                onClause = "ON (r.lista_rastreable = l.id_lista) ";
            } else if (type == Constants.ID_LISTA_NO_RASTREABLE) {
                onClause = "ON (r.lista_no_rastreable = l.id_lista) ";
            }

            pst = conn.prepareStatement("SELECT l.lista FROM lista l JOIN rastreo r " +
                    onClause +
                    "WHERE id_rastreo = ? and l.id_tipo_lista = ?");
            pst.setLong(1, idCrawling);
            pst.setInt(2, type);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("lista");
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOG.error("Error al recuperar la semilla del rastreo con id " + idCrawling);
            throw e;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                LOG.warn("Error al cerrar el objeto PreparedStatement");
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                LOG.warn("Error al cerrar el objeto ResultSet");
            }
        }
    }

    public static int recuperarCartuchoPorRastreo(Connection c, long idRastreo) throws Exception {
        try (PreparedStatement pst = c.prepareStatement("SELECT id_cartucho FROM cartucho_rastreo WHERE id_rastreo = ?")) {
            pst.setLong(1, idRastreo);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static Long recuperarIdNorma(Connection c, long idRastreo) throws Exception {
        try (PreparedStatement pes = c.prepareStatement("SELECT id_guideline FROM rastreo WHERE id_rastreo = ?")) {
            pes.setLong(1, idRastreo);
            try (ResultSet res = pes.executeQuery()) {
                if (res.next()) {
                    return res.getLong("id_guideline");
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return null;
    }

    public static String recuperarFicheroNorma(Connection c, long idGuideline) throws Exception {
        try (PreparedStatement pes = c.prepareStatement("SELECT des_guideline FROM tguidelines WHERE cod_guideline = ?")) {
            pes.setLong(1, idGuideline);
            try (ResultSet res = pes.executeQuery()) {
                if (res.next()) {
                    return res.getString("des_guideline");
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return null;
    }

    /**
     * Método que dado un id de cartucho indica si el cartucho pertenece a una normativa de accesibilidad o no
     *
     * @param c          Connection a la base de datos
     * @param idCartucho long id del cartucho
     * @return true si el cartucho pertenece a un cartucho de accesibilidad
     * @throws Exception
     */
    public static boolean isCartuchoAccesibilidad(Connection c, long idCartucho) throws Exception {
        try (PreparedStatement pes = c.prepareStatement("SELECT nombre FROM cartucho WHERE id_cartucho = ?")) {
            pes.setLong(1, idCartucho);
            try (ResultSet res = pes.executeQuery()) {
                if (res.next()) {
                    return "es.inteco.accesibilidad.CartuchoAccesibilidad".equals(res.getString("nombre"));
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al invocar isCartuchoAccesibilidad", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return false;
    }
}
