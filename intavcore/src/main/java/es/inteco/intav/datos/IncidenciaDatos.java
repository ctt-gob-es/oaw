package es.inteco.intav.datos;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import es.inteco.common.logging.Logger;
import es.inteco.intav.comun.Incidencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public final class IncidenciaDatos {

    private IncidenciaDatos() {
    }

    public static void saveIncidenceList(Connection conn, List<Incidencia> incidenceList) {
        /*Thread savingThread = new SavingThread(incidenceList);
        savingThread.start();
		
		return 0;*/
        long time = System.currentTimeMillis();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO tincidencia (COD_COMPROBACION, COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, DES_FUENTE) VALUES (?, ?, ?, ?, ?)");
            for (Incidencia incidencia : incidenceList) {
                pstmt.setInt(1, incidencia.getCodigoComprobacion());
                pstmt.setLong(2, incidencia.getCodigoAnalisis());
                pstmt.setInt(3, incidencia.getCodigoLineaFuente());
                pstmt.setInt(4, incidencia.getCodigoColumnaFuente());
                pstmt.setString(5, incidencia.getCodigoFuente());
                pstmt.addBatch();
            }

            pstmt.executeBatch();

            Logger.putLog("Tiempo de guardado de incidencias en base de datos: " + (System.currentTimeMillis() - time) + " milisegundos", Evaluator.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception ex) {
            Logger.putLog("Exception: ", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la llamada al procedimiento", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    public static int setIncidencia(Connection conn, Incidencia incidencia) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO tincidencia (COD_COMPROBACION, COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, DES_FUENTE) VALUES (?, ?, ?, ?, ?)");
            pstmt.setInt(1, incidencia.getCodigoComprobacion());
            pstmt.setLong(2, incidencia.getCodigoAnalisis());
            pstmt.setInt(3, incidencia.getCodigoLineaFuente());
            pstmt.setInt(4, incidencia.getCodigoColumnaFuente());
            pstmt.setString(5, incidencia.getCodigoFuente());
            pstmt.executeUpdate();

            return 0;
        } catch (Exception ex) {
            Logger.putLog("Exception: ", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return -1;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la llamada al procedimiento", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }

    public static List<Incidencia> getIncidenciasFromAnalisisId(Connection conn, long id, boolean getOnlyChecks) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = null;
        if (getOnlyChecks) {
            query = "SELECT COD_COMPROBACION FROM tincidencia WHERE COD_ANALISIS = " + id;
        } else {
            query = "SELECT COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, COD_COMPROBACION, COD_INCIDENCIA, SUBSTRING(DES_FUENTE, -LENGTH(DES_FUENTE), 400) AS DES_FUENTE_TRUNC FROM tincidencia WHERE COD_ANALISIS = " + id;
        }

        try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            ArrayList<Incidencia> arrlist = new ArrayList<Incidencia>();

            while (rs.next()) {
                Incidencia incidencia = new Incidencia();
                incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
                if (!getOnlyChecks) {
                    incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
                    incidencia.setCodigoLineaFuente(rs.getInt("COD_LINEA_FUENTE"));
                    incidencia.setCodigoColumnaFuente(rs.getInt("COD_COLUMNA_FUENTE"));
                    incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
                    incidencia.setCodigoFuente(rs.getString("DES_FUENTE_TRUNC"));
                }
                arrlist.add(incidencia);
            }

            return arrlist;

        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception ex) {
                Logger.putLog("Error al cerrar la llamada al procedimiento", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                Logger.putLog("Error al cerrar el resultSet", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            }
        }
    }

    public static List<Incidencia> getObservatoryIncidenciasFromAnalisisId(Connection conn, long id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = " SELECT COD_ANALISIS, COD_COMPROBACION, COD_INCIDENCIA" +
                " FROM tincidencia WHERE COD_ANALISIS = " + id;

        try {
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            ArrayList<Incidencia> arrlist = new ArrayList<Incidencia>();

            while (rs.next()) {
                Incidencia incidencia = new Incidencia();
                incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
                incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
                incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
                arrlist.add(incidencia);
            }

            return arrlist;

        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception ex) {
                Logger.putLog("Error al cerrar la llamada al procedimiento", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            }
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                Logger.putLog("Error al cerrar el resultSet", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            }
        }
    }

}
