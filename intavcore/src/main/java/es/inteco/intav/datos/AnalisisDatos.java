package es.inteco.intav.datos;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.inteco.common.CheckAccessibility;
import es.inteco.common.IntavConstants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.intav.form.EvaluationForm;
import es.inteco.intav.persistence.Analysis;
import es.inteco.intav.utils.EvaluatorUtils;
import es.inteco.plugin.dao.DataBaseManager;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class AnalisisDatos {

    private AnalisisDatos(){
    }

    public static int setAnalisis(final Connection conn, final Analysis analisis) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT cod_guideline FROM tguidelines WHERE des_guideline = ?;");
            pstmt.setString(1, getGuideline(analisis.getGuideline()));
            rs = pstmt.executeQuery();
            int codGuideline = 0;
            if (rs.next()) {
                codGuideline = rs.getInt("COD_GUIDELINE");
            }
            pstmt = conn.prepareStatement("INSERT INTO tanalisis (fec_analisis, cod_url, num_duracion, nom_entidad, cod_rastreo, cod_guideline, estado, cod_fuente)" +
                    " VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, analisis.getUrl());
            pstmt.setLong(2, analisis.getTime());
            pstmt.setString(3, analisis.getEntity());
            pstmt.setLong(4, analisis.getTracker());
            pstmt.setInt(5, codGuideline);
            pstmt.setInt(6, IntavConstants.STATUS_EXECUTING);
            pstmt.setString(7, analisis.getSource());
            pstmt.executeUpdate();

            pstmt.getGeneratedKeys();
            int codigoAnalisis =0 ;
            rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                codigoAnalisis=rs.getInt(1);
            }

            return codigoAnalisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return -1;
        } finally {
            close(null, rs, pstmt);
        }
    }

    public static void updateChecksEjecutados(final String updatedChecks, final long idAnalisis) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String query = "UPDATE tanalisis SET CHECKS_EJECUTADOS = ? WHERE COD_ANALISIS = ?;";
        try {
            conn = DataBaseManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, updatedChecks);
            pstmt.setLong(2, idAnalisis);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("SQLException: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            close(conn, null, pstmt);
        }
    }

    public static void endAnalysisSuccess(Evaluation eval) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String query = "UPDATE tanalisis SET CHECKS_EJECUTADOS = ?, ESTADO = ? WHERE COD_ANALISIS = ?;";
        try {
            conn = DataBaseManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, eval.getChecksExecutedStr());
            pstmt.setInt(2, IntavConstants.STATUS_SUCCESS);
            pstmt.setLong(3, eval.getIdAnalisis());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("SQLException: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            close(conn, null, pstmt);
        }
    }

    public static int setAnalysisError(CheckAccessibility checkAccessibility) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DataBaseManager.getConnection();
            pstmt = conn.prepareStatement("SELECT COD_GUIDELINE FROM tguidelines WHERE DES_GUIDELINE = ?;");
            pstmt.setString(1, getGuideline(checkAccessibility.getGuidelineFile().replace("-nobroken","")) );
            rs = pstmt.executeQuery();
            int codGuideline = 0;
            if (rs.next()) {
                codGuideline = rs.getInt("COD_GUIDELINE");
            }
            pstmt = conn.prepareStatement("INSERT INTO tanalisis (FEC_ANALISIS, COD_URL, NUM_DURACION, NOM_ENTIDAD, COD_RASTREO, COD_GUIDELINE, ESTADO)" +
                    " VALUES (NOW(), ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, checkAccessibility.getUrl());
            pstmt.setLong(2, 0);
            pstmt.setString(3, checkAccessibility.getEntity());
            pstmt.setLong(4, checkAccessibility.getIdRastreo());
            pstmt.setInt(5, codGuideline);
            pstmt.setInt(6, IntavConstants.STATUS_ERROR);
            pstmt.executeUpdate();

            pstmt.getGeneratedKeys();
            int codigoAnalisis = 0 ;
            rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                codigoAnalisis=rs.getInt(1);
            }

            return codigoAnalisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return -1;
        } finally {
            close(conn, rs, pstmt);
        }
    }

    public static Analysis getAnalisisFromId(Connection conn, long id) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Analysis analisis = new Analysis();

            pstmt = conn.prepareStatement("SELECT * FROM tanalisis A INNER JOIN tguidelines G ON A.cod_guideline = G.cod_guideline " +
                    "WHERE cod_analisis = ?;");
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                analisis.setCode(rs.getInt("COD_ANALISIS"));
                analisis.setDate(rs.getDate("FEC_ANALISIS"));
                analisis.setUrl(rs.getString("COD_URL"));
                analisis.setEntity(rs.getString("NOM_ENTIDAD"));
                analisis.setTracker(rs.getLong("COD_RASTREO"));
                analisis.setGuideline(rs.getString("DES_GUIDELINE"));
                analisis.setChecksExecutedStr(rs.getString("CHECKS_EJECUTADOS"));
                analisis.setSource(rs.getString("COD_FUENTE"));
            } else {
                return null;
            }

            return analisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        } finally {
            close(null, rs, pstmt);
        }
    }

    public static List<Analysis> getAnalysisByTracking(long idTracking, int pagina, HttpServletRequest request) {
        PropertiesManager pmgr = new PropertiesManager();
        int pagSize = Integer.parseInt(pmgr.getValue("intav.properties", "pagination.size"));
        int resultFrom = pagSize * pagina;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DataBaseManager.getConnection();
            if (pagina == IntavConstants.NO_PAGINATION) {
                pstmt = conn.prepareStatement("SELECT cod_analisis, fec_analisis, cod_url, nom_entidad, cod_rastreo, estado FROM tanalisis WHERE cod_rastreo = ?");
            } else {
                pstmt = conn.prepareStatement("SELECT cod_analisis, fec_analisis, cod_url, nom_entidad, cod_rastreo, estado FROM tanalisis WHERE cod_rastreo = ? ORDER BY FEC_ANALISIS ASC LIMIT ? OFFSET ?");
                pstmt.setInt(2, pagSize);
                pstmt.setInt(3, resultFrom);
            }
            pstmt.setLong(1, idTracking);
            rs = pstmt.executeQuery();

            return getAnalysisList(conn, rs, EvaluatorUtility.getLanguage(request));
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        } finally {
            close(conn, rs, pstmt);
        }
    }


    public static List<Long> getAnalysisIdsByTracking(Connection conn, long idTracking) {
        List<Long> results = new ArrayList<Long>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis WHERE cod_rastreo = ? AND checks_ejecutados IS NOT NULL");
            pstmt.setLong(1, idTracking);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getLong("cod_analisis"));
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        } finally {
            close(null, rs, pstmt);
        }

        return results;
    }


    public static int countAnalysisByTracking(long idTracking) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DataBaseManager.getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM tanalisis WHERE cod_rastreo = ?");
            pstmt.setLong(1, idTracking);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        } finally {
            close(conn, rs, pstmt);
        }
        return 0;
    }

    private static void close(Connection conn, ResultSet rs, PreparedStatement pstmt) {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (Exception ex) {
            Logger.putLog("Error al cerrar la llamada al procedimiento", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
            Logger.putLog("Error al cerrar el resultSet", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        }

        DataBaseManager.closeConnection(conn);
    }

    private static List<Analysis> getAnalysisList(final Connection conn, final ResultSet rs, final String language) throws SQLException {
        final List<Analysis> listAnalysis = new ArrayList<Analysis>();
        while (rs.next()) {
            final Analysis analysis = new Analysis();
            analysis.setCode(rs.getInt("cod_analisis"));
            analysis.setDate(rs.getTimestamp("fec_analisis"));
            analysis.setUrl(rs.getString("cod_url"));
            analysis.setEntity(rs.getString("nom_entidad"));
            analysis.setTracker(rs.getInt("cod_rastreo"));
            analysis.setStatus(rs.getInt("estado"));

            if (analysis.getStatus() == IntavConstants.STATUS_SUCCESS) {
                final Evaluator evaluator = new Evaluator();
                try {
                    final Evaluation evaluation = evaluator.getAnalisisDB(conn, analysis.getCode(), EvaluatorUtils.getDocList(), true);
                    final EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, language);
                    for (int i = 0; i < evaluationForm.getPriorities().size(); i++) {
                        analysis.setProblems(analysis.getProblems() + evaluationForm.getPriorities().get(i).getNumProblems());
                        analysis.setWarnings(analysis.getWarnings() + evaluationForm.getPriorities().get(i).getNumWarnings());
                        analysis.setObservations(analysis.getObservations() + evaluationForm.getPriorities().get(i).getNumInfos());
                    }
                } catch (Exception e) {
                    Logger.putLog("Exception: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }

            listAnalysis.add(analysis);
        }
        return listAnalysis;
    }

    public static List<Long> getEvaluationIds(long idTracking) {
        List<Long> evaluationIds = new ArrayList<Long>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DataBaseManager.getConnection();
            pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis t WHERE cod_rastreo = ? ORDER by cod_analisis");
            pstmt.setLong(1, idTracking);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                evaluationIds.add(rs.getLong(1));
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        } finally {
            close(conn, rs, pstmt);
        }

        return evaluationIds;
    }

    private static String getGuideline(final String guideline) {
        if ( guideline.contains("-nobroken") ) {
            return guideline.replace("-nobroken","");
        } else {
            return guideline;
        }
    }
}