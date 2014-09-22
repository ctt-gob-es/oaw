package es.inteco.intav.datos;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import es.inteco.common.logging.Logger;
import es.inteco.intav.comun.Incidencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class SavingThread extends Thread {
    private List<Incidencia> incidenceList;

    public SavingThread(List<Incidencia> incidenceList) {
        this.incidenceList = incidenceList;
    }

    public void run() {
        long time = System.currentTimeMillis();
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            synchronized (this) {
                conn = DBConnect.connect();

                conn.setAutoCommit(false);

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

                // Ejecutamos el commit manualmente. Esto se hace para mejorar el rendimiento de la aplicación
                conn.commit();
            }
            Logger.putLog("Tiempo de guardado de incidencias en base de datos: " + (System.currentTimeMillis() - time) + " milisegundos", Evaluator.class, Logger.LOG_LEVEL_INFO);
        } catch (Exception ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception e) {
                Logger.putLog("Error al hacer un rollback", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, e);
            }
            Logger.putLog("Exception: ", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        } finally {
            try {
                if (conn != null) {
                    DBConnect.disconnect(conn);
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la conexión a base de datos", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, e);
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
                Logger.putLog("Error al cerrar la llamada al procedimiento", IncidenciaDatos.class, Logger.LOG_LEVEL_ERROR, e);
            }
        }
    }
}
