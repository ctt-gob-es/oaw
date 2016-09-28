package es.inteco.intav.datos;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import es.inteco.common.logging.Logger;
import es.inteco.intav.comun.Incidencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class IncidenciaDatos {

    private IncidenciaDatos() {
    }

    public static void saveIncidenceList(final Connection conn, final List<Incidencia> incidenceList) {
        long time = System.currentTimeMillis();
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tincidencia (COD_COMPROBACION, COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, DES_FUENTE) VALUES (?, ?, ?, ?, ?)")) {
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
        }
    }

    public static List<Incidencia> getIncidenciasFromAnalisisId(final Connection conn, final long idAnalisis, final boolean getOnlyChecks) {
        if (getOnlyChecks) {
            return getIncidenciasIdFromAnalisisId(conn, idAnalisis);
        } else {
            return getIncidenciasFromAnalisisId(conn, idAnalisis);
        }
    }

    private static List<Incidencia> getIncidenciasIdFromAnalisisId(final Connection conn, final long idAnalisis) {
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT COD_COMPROBACION FROM tincidencia WHERE COD_ANALISIS = ?")) {
            pstmt.setLong(1, idAnalisis);
            try (ResultSet rs = pstmt.executeQuery()) {
                final List<Incidencia> incidencias = new ArrayList<>();

                while (rs.next()) {
                    final Incidencia incidencia = new Incidencia();
                    incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
                    incidencias.add(incidencia);
                }

                return incidencias;
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return Collections.emptyList();
        }
    }

    public static List<Incidencia> getIncidenciasFromAnalisisId(final Connection conn, final long idAnalisis) {
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, COD_COMPROBACION, COD_INCIDENCIA, DES_FUENTE FROM tincidencia WHERE COD_ANALISIS = ?")) {
            pstmt.setLong(1, idAnalisis);
            try (ResultSet rs = pstmt.executeQuery()) {
                final List<Incidencia> incidencias = new ArrayList<>();

                while (rs.next()) {
                    final Incidencia incidencia = new Incidencia();
                    incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));

                    incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
                    incidencia.setCodigoLineaFuente(rs.getInt("COD_LINEA_FUENTE"));
                    incidencia.setCodigoColumnaFuente(rs.getInt("COD_COLUMNA_FUENTE"));
                    incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
                    incidencia.setCodigoFuente(rs.getString("DES_FUENTE"));

                    incidencias.add(incidencia);
                }

                return incidencias;
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return Collections.emptyList();
        }
    }

    public static List<Incidencia> getIncidenciasByAnalisisAndComprobacion(final Connection conn, final long idAnalisis, final long idComprobacion) {
        final String query = "SELECT COD_ANALISIS, COD_LINEA_FUENTE, COD_COLUMNA_FUENTE, COD_COMPROBACION, COD_INCIDENCIA, DES_FUENTE FROM tincidencia WHERE cod_analisis = ? AND cod_comprobacion = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, idAnalisis);
            pstmt.setLong(2, idComprobacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                final ArrayList<Incidencia> arrlist = new ArrayList<>();

                while (rs.next()) {
                    final Incidencia incidencia = new Incidencia();
                    incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
                    incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
                    incidencia.setCodigoLineaFuente(rs.getInt("COD_LINEA_FUENTE"));
                    incidencia.setCodigoColumnaFuente(rs.getInt("COD_COLUMNA_FUENTE"));
                    incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
                    incidencia.setCodigoFuente(rs.getString("DES_FUENTE"));

                    arrlist.add(incidencia);
                }

                return arrlist;
            }

        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        }
    }

    public static void deleteIncidenciasByAnalisisAndComprobacion(final Connection conn, final long idAnalisis, final long idComprobacion) {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM tincidencia WHERE cod_analisis = ? AND cod_comprobacion = ?")) {
            pstmt.setLong(1, idAnalisis);
            pstmt.setLong(2, idComprobacion);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        }
    }

    public static List<Incidencia> getObservatoryIncidenciasFromAnalisisId(Connection conn, long idAnalisis) {
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT COD_ANALISIS, COD_COMPROBACION, COD_INCIDENCIA FROM tincidencia WHERE COD_ANALISIS = ?")) {
            pstmt.setLong(1, idAnalisis);
            try (ResultSet rs = pstmt.executeQuery()) {
                final ArrayList<Incidencia> incidencias = new ArrayList<>();

                while (rs.next()) {
                    final Incidencia incidencia = new Incidencia();
                    incidencia.setCodigoAnalisis(rs.getInt("COD_ANALISIS"));
                    incidencia.setCodigoComprobacion(rs.getInt("COD_COMPROBACION"));
                    incidencia.setCodigoIncidencia(rs.getInt("COD_INCIDENCIA"));
                    incidencias.add(incidencia);
                }

                return incidencias;
            }

        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        }
    }

}
