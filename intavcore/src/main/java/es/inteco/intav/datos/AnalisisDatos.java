package es.inteco.intav.datos;

import ca.utoronto.atrc.tile.accessibilitychecker.Evaluation;
import ca.utoronto.atrc.tile.accessibilitychecker.Evaluator;
import ca.utoronto.atrc.tile.accessibilitychecker.EvaluatorUtility;
import es.ctic.css.CSSResource;
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

    private AnalisisDatos() {
    }

    public static int setAnalisis(final Connection connection, final Analysis analisis, final List<CSSResource> cssResources) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO tanalisis (fec_analisis, cod_url, num_duracion, nom_entidad, cod_rastreo, cod_guideline, estado, cod_fuente)" +
                " VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            pstmt.setString(1, analisis.getUrl());
            pstmt.setLong(2, analisis.getTime());
            pstmt.setString(3, analisis.getEntity());
            pstmt.setLong(4, analisis.getTracker());
            pstmt.setInt(5, getCodGuideline(connection, analisis.getGuideline()));
            pstmt.setInt(6, IntavConstants.STATUS_EXECUTING);
            pstmt.setString(7, analisis.getSource());
            pstmt.executeUpdate();

            final int codigoAnalisis;
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    codigoAnalisis = rs.getInt(1);
                } else {
                    return 0;
                }
            }

            saveCSSResources(connection, codigoAnalisis, cssResources);
            connection.commit();
            connection.setAutoCommit(true);
            return codigoAnalisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            connection.rollback();
            connection.setAutoCommit(true);
            return -1;
        }
    }

    private static void saveCSSResources(final Connection connection, final int codigoAnalisis, final List<CSSResource> cssResources) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO tanalisis_css (url, codigo, cod_analisis) VALUES (?,?,?);")) {
            for (CSSResource cssResource : cssResources) {
                if(cssResource.isImported()) {
                    pstmt.setString(1, cssResource.getStringSource());
                    pstmt.setString(2, cssResource.getContent());
                    pstmt.setInt(3, codigoAnalisis);

                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            Logger.putLog("Error en saveCSSResources", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static int setAnalysisError(final CheckAccessibility checkAccessibility) {
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO tanalisis (FEC_ANALISIS, COD_URL, NUM_DURACION, NOM_ENTIDAD, COD_RASTREO, COD_GUIDELINE, ESTADO)" +
                     " VALUES (NOW(), ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, checkAccessibility.getUrl());
            pstmt.setLong(2, 0);
            pstmt.setString(3, checkAccessibility.getEntity());
            pstmt.setLong(4, checkAccessibility.getIdRastreo());
            pstmt.setInt(5, getCodGuideline(conn, checkAccessibility.getGuidelineFile()));
            pstmt.setInt(6, IntavConstants.STATUS_ERROR);
            pstmt.executeUpdate();

            int codigoAnalisis = 0;
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    codigoAnalisis = rs.getInt(1);
                }
            }
            return codigoAnalisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return -1;
        }
    }

    private static int getCodGuideline(final Connection connection, final String guideline) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT cod_guideline FROM tguidelines WHERE des_guideline = ?;")) {
            pstmt.setString(1, getGuideline(guideline));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("COD_GUIDELINE");
                } else {
                    return 0;
                }
            }
        }
    }

    public static void updateChecksEjecutados(final String updatedChecks, final long idAnalisis) {
        try (Connection conn = DataBaseManager.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement("UPDATE tanalisis SET CHECKS_EJECUTADOS = ? WHERE COD_ANALISIS = ?;")) {
            pstmt.setString(1, updatedChecks);
            pstmt.setLong(2, idAnalisis);
            pstmt.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("updateChecksEjecutados: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static void endAnalysisSuccess(final Evaluation eval) {
        try (Connection conn = DataBaseManager.getConnection();
             final PreparedStatement pstmt = conn.prepareStatement("UPDATE tanalisis SET CHECKS_EJECUTADOS = ?, ESTADO = ? WHERE COD_ANALISIS = ?;")) {
            pstmt.setString(1, eval.getChecksExecutedStr());
            pstmt.setInt(2, IntavConstants.STATUS_SUCCESS);
            pstmt.setLong(3, eval.getIdAnalisis());
            pstmt.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("endAnalysisSuccess: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static Analysis getAnalisisFromId(Connection conn, long id) {
        final Analysis analisis = new Analysis();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tanalisis A INNER JOIN tguidelines G ON A.cod_guideline = G.cod_guideline " +
                "WHERE cod_analisis = ?;")) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
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
            }
            return analisis;
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        }
    }

    public static List<Analysis> getAnalysisByTracking(long idTracking, int pagina, HttpServletRequest request) {
        final String query;
        if (pagina == IntavConstants.NO_PAGINATION) {
            query = "SELECT cod_analisis, fec_analisis, cod_url, nom_entidad, cod_rastreo, estado FROM tanalisis WHERE cod_rastreo = ?";
        } else {
            query = "SELECT cod_analisis, fec_analisis, cod_url, nom_entidad, cod_rastreo, estado FROM tanalisis WHERE cod_rastreo = ? ORDER BY FEC_ANALISIS ASC LIMIT ? OFFSET ?";
        }
        final PropertiesManager pmgr = new PropertiesManager();
        final int pagSize = Integer.parseInt(pmgr.getValue("intav.properties", "pagination.size"));
        final int resultFrom = pagSize * pagina;
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, idTracking);
            if (pagina != IntavConstants.NO_PAGINATION) {
                pstmt.setInt(2, pagSize);
                pstmt.setInt(3, resultFrom);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                return getAnalysisList(conn, rs, EvaluatorUtility.getLanguage(request));
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        }
    }


    public static List<Long> getAnalysisIdsByTracking(final Connection conn, final long idTracking) {
        final List<Long> results = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis WHERE cod_rastreo = ? AND checks_ejecutados IS NOT NULL")) {
            pstmt.setLong(1, idTracking);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(rs.getLong("cod_analisis"));
                }
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        }

        return results;
    }


    public static int countAnalysisByTracking(long idTracking) {
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM tanalisis WHERE cod_rastreo = ?")) {
            pstmt.setLong(1, idTracking);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        }
        return 0;
    }

    private static List<Analysis> getAnalysisList(final Connection conn, final ResultSet rs, final String language) throws SQLException {
        final List<Analysis> listAnalysis = new ArrayList<>();
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
                    Logger.putLog("getAnalysisList: ", AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, e);
                }
            }

            listAnalysis.add(analysis);
        }
        return listAnalysis;
    }

    public static List<Long> getEvaluationIdsFromRastreoRealizado(long idRastreoRealizado) {
        final List<Long> evaluationIds = new ArrayList<>();

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis t WHERE cod_rastreo = ? ORDER by cod_analisis")) {
            pstmt.setLong(1, idRastreoRealizado);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    evaluationIds.add(rs.getLong(1));
                }
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return evaluationIds;
        }

        return evaluationIds;
    }

    /**
     * Obtiene todos los recursos CSS (CSSDTO) que están asociados a una evaluación, análisis de una página.
     *
     * @param idCodAnalisis el identificador de la evaluación.
     * @return una lista con todos los recursos CSS (CSSDTO) que se analizaron para esa evaluación.
     */
    public static List<CSSDTO> getCSSResourcesFromEvaluation(final long idCodAnalisis) {
        final List<CSSDTO> evaluationIds = new ArrayList<>();

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT url, codigo FROM tanalisis_css t WHERE cod_analisis = ?")) {
            pstmt.setLong(1, idCodAnalisis);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    evaluationIds.add(new CSSDTO(rs.getString(1), rs.getString(2)));
                }
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
        }

        return evaluationIds;
    }

    private static String getGuideline(final String guideline) {
        if (guideline.contains("-nobroken")) {
            return guideline.replace("-nobroken", "");
        } else {
            return guideline;
        }
    }
}