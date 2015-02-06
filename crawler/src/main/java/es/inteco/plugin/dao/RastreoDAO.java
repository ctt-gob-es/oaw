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

    public static final Log LOG = LogFactory.getLog(RastreoDAO.class);

    private RastreoDAO() {
    }

    public static void stopRunningObservatories(Connection c) throws Exception {
        java.sql.PreparedStatement pst = null;
        try {
            pst = c.prepareStatement("UPDATE observatorios_realizados SET Estado = ? WHERE Estado = ?");
            pst.setInt(1, Constants.OBS_ERROR_STATUS);
            pst.setInt(2, Constants.OBS_LAUNCHED_STATUS);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Error al actualizar el estado del rastreo", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                Logger.putLog("Error al cerrar el objeto PreparedStatement", RastreoDAO.class, Logger.LOG_LEVEL_WARNING, e);
            }
        }
    }

    public static void actualizarEstadoRastreo(Connection c, long idRastreo, int status) {
        if (idRastreo != -1) {
            java.sql.PreparedStatement pst = null;
            try {
                pst = c.prepareStatement("UPDATE rastreo SET estado = ? WHERE id_rastreo = ?");
                pst.setInt(1, status);
                pst.setLong(2, idRastreo);
                pst.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Error al actualizar el estado del rastreo");
            } finally {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                } catch (SQLException e) {
                    LOG.warn("Error al cerrar el objeto PreparedStatement");
                }
            }
        }
    }

    public static boolean isAnyCrawlingActive(Connection c) throws Exception {
        java.sql.PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = c.prepareStatement("SELECT * FROM rastreo WHERE estado = ?");
            pst.setInt(1, Constants.STATUS_LAUNCHED);
            rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOG.error("Error al actualizar el estado del rastreo");
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

    public static void stopRunningCrawlings(Connection c) throws Exception {
        java.sql.PreparedStatement pst = null;
        try {
            pst = c.prepareStatement("UPDATE rastreo SET estado = ? WHERE estado = ?");
            pst.setInt(1, Constants.STATUS_STOPPED);
            pst.setInt(2, Constants.STATUS_LAUNCHED);
            pst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Error al actualizar el estado del rastreo");
            throw e;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                LOG.warn("Error al cerrar el objeto PreparedStatement");
            }
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
        PreparedStatement pes1 = null;
        ResultSet res1 = null;
        int id_cartucho = -1;
        try {
            pes1 = c.prepareStatement("SELECT * FROM cartucho_rastreo WHERE id_rastreo = ?");
            pes1.setLong(1, idRastreo);
            res1 = pes1.executeQuery();
            if (res1.next()) {
                id_cartucho = res1.getInt(1);
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            try {
                if (pes1 != null) {
                    pes1.close();
                }
                if (res1 != null) {
                    res1.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_WARNING, e);
                throw e;
            }
        }
        return id_cartucho;
    }

    public static Long recuperarIdNorma(Connection c, long idRastreo) throws Exception {
        PreparedStatement pes = null;
        ResultSet res = null;
        try {
            pes = c.prepareStatement("SELECT * FROM rastreo WHERE id_rastreo = ?");
            pes.setLong(1, idRastreo);
            res = pes.executeQuery();
            if (res.next()) {
                return res.getLong("id_guideline");
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            try {
                if (pes != null) {
                    pes.close();
                }
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_WARNING, e);
                throw e;
            }
        }
        return null;
    }

    public static String recuperarFicheroNorma(Connection c, long idGuideline) throws Exception {
        PreparedStatement pes = null;
        ResultSet res = null;
        try {
            pes = c.prepareStatement("SELECT des_guideline FROM tguidelines WHERE cod_guideline = ?");
            pes.setLong(1, idGuideline);
            res = pes.executeQuery();
            if (res.next()) {
                return res.getString("des_guideline");
            }
        } catch (Exception e) {
            Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            try {
                if (pes != null) {
                    pes.close();
                }
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {
                Logger.putLog("Excepcion al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_WARNING, e);
                throw e;
            }
        }
        return null;
    }

    /**
     * Método que dado un id de cartucho indica si el cartucho pertenece a una normativa de accesibilidad o no
     * @param c Connection a la base de datos
     * @param idCartucho long id del cartucho
     * @return true si el cartucho pertenece a un cartucho de accesibilidad
     * @throws Exception
     */
     public static boolean isCartuchoAccesibilidad(Connection c, long idCartucho) throws Exception {
        PreparedStatement pes = null;
        ResultSet res = null;
        try {
            pes = c.prepareStatement("SELECT nombre FROM cartucho WHERE id_cartucho = ?");
            pes.setLong(1, idCartucho);
            res = pes.executeQuery();
            if (res.next()) {
                return "es.inteco.accesibilidad.CartuchoAccesibilidad".equals(res.getString("nombre"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al invocar isCartuchoAccesibilidad", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (pes != null) {
                    pes.close();
                }
            } catch (Exception e) {
                Logger.putLog("Excepcion al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_WARNING, e);
            }
        }
        return false;
    }
}
