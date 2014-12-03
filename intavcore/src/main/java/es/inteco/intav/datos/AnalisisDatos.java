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

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class AnalisisDatos {

    private AnalisisDatos(){
    }

    public static int setAnalisis(Connection conn, Analysis analisis) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement("SELECT COD_GUIDELINE FROM tguidelines WHERE DES_GUIDELINE = ?;");
            pstmt.setString(1, analisis.getGuideline());
            rs = pstmt.executeQuery();
            int codGuideline = 0;
            if (rs.next()) {
                codGuideline = rs.getInt("COD_GUIDELINE");
            }
            pstmt = conn.prepareStatement("INSERT INTO tanalisis (FEC_ANALISIS, COD_URL, NUM_DURACION, NOM_ENTIDAD, COD_RASTREO, COD_GUIDELINE, ESTADO, COD_FUENTE)" +
                    " VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?);");
            pstmt.setString(1, analisis.getUrl());
            pstmt.setLong(2, analisis.getTime());
            pstmt.setString(3, analisis.getEntity());
            pstmt.setLong(4, analisis.getTracker());
            pstmt.setInt(5, codGuideline);
            pstmt.setInt(6, IntavConstants.STATUS_EXECUTING);
            pstmt.setString(7, analisis.getSource());
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("SELECT MAX(COD_ANALISIS) FROM tanalisis");
            rs = pstmt.executeQuery();

            int codigoAnalisis = 0;
            while (rs.next()) {
                codigoAnalisis = rs.getInt(1);
            }

            return codigoAnalisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return -1;
        } finally {
            close(null, rs, pstmt);
        }
    }

    public static void endAnalysisSuccess(Evaluation eval) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String query = " UPDATE tanalisis SET CHECKS_EJECUTADOS = ?, ESTADO = ? WHERE COD_ANALISIS = ?;";
        try {
            conn = DBConnect.connect();
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
            conn = DBConnect.connect();
            pstmt = conn.prepareStatement("SELECT COD_GUIDELINE FROM tguidelines WHERE DES_GUIDELINE = ?;");
            pstmt.setString(1, checkAccessibility.getGuidelineFile());
            rs = pstmt.executeQuery();
            int codGuideline = 0;
            if (rs.next()) {
                codGuideline = rs.getInt("COD_GUIDELINE");
            }
            pstmt = conn.prepareStatement("INSERT INTO tanalisis (FEC_ANALISIS, COD_URL, NUM_DURACION, NOM_ENTIDAD, COD_RASTREO, COD_GUIDELINE, ESTADO)" +
                    " VALUES (NOW(), ?, ?, ?, ?, ?, ?);");
            pstmt.setString(1, checkAccessibility.getUrl());
            pstmt.setLong(2, 0);
            pstmt.setString(3, checkAccessibility.getEntity());
            pstmt.setLong(4, checkAccessibility.getIdRastreo());
            pstmt.setInt(5, codGuideline);
            pstmt.setInt(6, IntavConstants.STATUS_ERROR);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("SELECT MAX(COD_ANALISIS) FROM tanalisis");
            rs = pstmt.executeQuery();

            int codigoAnalisis = 0;
            while (rs.next()) {
                codigoAnalisis = rs.getInt(1);
            }

            return codigoAnalisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return -1;
        } finally {
            close(conn, rs, pstmt);
        }
    }

    /*public static int countAnalysis(SearchAnalysis searchAnalysis) {
        PropertiesManager pmgr = new PropertiesManager();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM tanalisis WHERE cod_rastreo = 0 ";
        try {
            conn = DBConnect.connect();
            if (searchAnalysis.getEntity() != null && !searchAnalysis.getEntity().isEmpty()) {
                query += " AND NOM_ENTIDAD = '" + searchAnalysis.getEntity() + "'";
            }
            if (searchAnalysis.getDomain() != null && searchAnalysis.getDomain().isEmpty()) {
                query += " AND COD_URL LIKE '%" + searchAnalysis.getDomain() + "%'";
            }
            if (searchAnalysis.getDate() != null && searchAnalysis.getDate().isEmpty()) {
                DateFormat dateF = new SimpleDateFormat(pmgr.getValue("intav.properties", "simple.date.format"));
                Date date = dateF.parse(searchAnalysis.getDate());
                query += " AND FEC_ANALISIS BETWEEN '" + new java.sql.Timestamp(date.getTime()) + "' AND '" + new java.sql.Timestamp(getFinalDate(date).getTime()) + "'";
            }
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            int numRes = 0;
            if (rs.next()) {
                numRes = rs.getInt(1);
            }
            return numRes;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        } finally {
            close(conn, rs, pstmt);
        }
        return 0;
    } //*/

    /*private static Date getFinalDate(Date initialDate) {
        Calendar cl1 = Calendar.getInstance();

        cl1.setTime(initialDate);
        cl1.add(Calendar.DATE, 1);
        cl1.add(Calendar.SECOND, -1);

        return cl1.getTime();
    }//*/

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
            conn = DBConnect.connect();
            if (pagina == IntavConstants.NO_PAGINATION) {
                pstmt = conn.prepareStatement("SELECT * FROM tanalisis WHERE cod_rastreo = ?");
            } else {
                pstmt = conn.prepareStatement("SELECT * FROM tanalisis WHERE cod_rastreo = ? ORDER BY FEC_ANALISIS ASC LIMIT ? OFFSET ?");
                pstmt.setInt(2, pagSize);
                pstmt.setInt(3, resultFrom);
            }
            pstmt.setLong(1, idTracking);
            rs = pstmt.executeQuery();

            return getAnalysisList(conn, rs, request);
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
            conn = DBConnect.connect();
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

        DBConnect.disconnect(conn);
    }

    private static List<Analysis> getAnalysisList(Connection conn, ResultSet rs, HttpServletRequest request) throws SQLException {
        List<Analysis> listAnalysis = new ArrayList<Analysis>();
        while (rs.next()) {
            Analysis analysis = new Analysis();
            analysis.setCode(rs.getInt("COD_ANALISIS"));
            analysis.setDate(rs.getTimestamp("FEC_ANALISIS"));
            analysis.setUrl(rs.getString("COD_URL"));
            analysis.setEntity(rs.getString("NOM_ENTIDAD"));
            analysis.setTracker(rs.getInt("COD_RASTREO"));
            analysis.setStatus(rs.getInt("ESTADO"));

            if (analysis.getStatus() == IntavConstants.STATUS_SUCCESS) {
                Evaluator evaluator = new Evaluator();
                try {
                    Evaluation evaluation = evaluator.getAnalisisDB(conn, analysis.getCode(), EvaluatorUtils.getDocList(), true);
                    EvaluationForm evaluationForm = EvaluatorUtils.generateEvaluationForm(evaluation, EvaluatorUtility.getLanguage(request));
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
            conn = DBConnect.connect();
            pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis t WHERE COD_RASTREO = ? ORDER by cod_analisis");
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

    /*public static List<Long> getEvaluationIds(String entity) {
        List<Long> evaluationIds = new ArrayList<Long>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.connect();
            pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis t WHERE nom_entidad = ?");
            pstmt.setString(1, entity);
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
    }//*/

}
