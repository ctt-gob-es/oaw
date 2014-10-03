package es.inteco.rastreador2.dao.basic.service;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

public final class DiagnosisDAO {

    private DiagnosisDAO() {
    }

    public static void updateStatus(Connection conn, long idAnalysis, String status) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE basic_service SET status = ?, send_date = ? WHERE id = ?");
            ps.setString(1, status);
            if (status.equals(Constants.BASIC_SERVICE_STATUS_FINISHED)) {
                ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            } else {
                ps.setTimestamp(2, null);
            }
            ps.setLong(3, idAnalysis);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Error al actualizar el estado de la petición del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            close(null, ps);
        }
    }

    public static long insertBasicServices(Connection conn, BasicServiceForm bsForm, String status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("INSERT INTO basic_service (usr, language, domain, email, depth, width, report, date, status, scheduling_date, analysis_type, in_directory) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, bsForm.getUser());
            ps.setString(2, bsForm.getLanguage());
            if (StringUtils.isNotEmpty(bsForm.getDomain())) {
                ps.setString(3, bsForm.getDomain());
                ps.setString(11, Constants.BASIC_SERVICE_ANALYSIS_TYPE_URL);
            } else if (StringUtils.isNotEmpty(bsForm.getContent())) {
                ps.setString(3, BasicServiceUtils.getTitleFromContent(bsForm.getContent()));
                ps.setString(11, Constants.BASIC_SERVICE_ANALYSIS_TYPE_CONTENT);
            }
            ps.setString(4, bsForm.getEmail());
            ps.setString(5, bsForm.getProfundidad());
            ps.setString(6, bsForm.getAmplitud());
            ps.setString(7, bsForm.getReport());
            ps.setTimestamp(8, new Timestamp(new Date().getTime()));
            ps.setString(9, status);
            if (bsForm.getSchedulingDate() != null) {
                ps.setTimestamp(10, new Timestamp(bsForm.getSchedulingDate().getTime()));
            } else {
                ps.setTimestamp(10, null);
            }
            ps.setBoolean(12, bsForm.isInDirectory());
            ps.executeUpdate();
            close(rs, ps);
            ps = conn.prepareStatement("SELECT max(id) FROM basic_service WHERE usr = ? AND domain = ?");
            ps.setString(1, bsForm.getUser());
            ps.setString(2, bsForm.isContentAnalysis() ? BasicServiceUtils.getTitleFromContent(bsForm.getContent()) : bsForm.getDomain());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (Exception e) {
            Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            close(rs, ps);
        }
        return 0;
    }

    public static List<BasicServiceForm> getBasicServiceRequestByStatus(Connection conn, String status) {
        List<BasicServiceForm> results = new ArrayList<BasicServiceForm>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM basic_service WHERE status LIKE ?");
            ps.setString(1, status);
            rs = ps.executeQuery();
            while (rs.next()) {
                BasicServiceForm basicServiceForm = new BasicServiceForm();
                basicServiceForm.setId(rs.getLong("id"));
                basicServiceForm.setDomain(rs.getString("domain"));
                basicServiceForm.setUser(rs.getString("usr"));
                basicServiceForm.setEmail(rs.getString("email"));
                basicServiceForm.setLanguage(rs.getString("language"));
                basicServiceForm.setAmplitud(rs.getString("width"));
                basicServiceForm.setProfundidad(rs.getString("depth"));
                basicServiceForm.setReport(rs.getString("report"));
                basicServiceForm.setSchedulingDate(rs.getTimestamp("scheduling_date"));
                basicServiceForm.setInDirectory(rs.getBoolean("in_directory"));

                results.add(basicServiceForm);
            }
        } catch (Exception e) {
            Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
        } finally {
            close(rs, ps);
        }
        return results;
    }

    public static void deleteAnalysis(Connection conn, String entity, long idCrawler) throws Exception {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM tanalisis WHERE nom_entidad = ? AND cod_rastreo = ?");
            ps.setString(1, entity);
            ps.setLong(2, idCrawler);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Error al actualizar el id del rastreo. ", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            close(null, ps);
        }
    }

    public static List<Long> getEvaluationIds(Long idCrawling) throws Exception {
        List<Long> analysisIds = new ArrayList<Long>();
        PropertiesManager pmgr = new PropertiesManager();
        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection conn = null;
        try {
            conn = DataBaseManager.getConnection(pmgr.getValue(CRAWLER_PROPERTIES, "datasource.name.intav"));

            ps = conn.prepareStatement("SELECT * FROM tanalisis WHERE cod_rastreo = ?");
            ps.setLong(1, idCrawling);
            rs = ps.executeQuery();

            while (rs.next()) {
                analysisIds.add(rs.getLong("cod_analisis"));
            }
        } catch (Exception e) {
            Logger.putLog("Error al actualizar el id del rastreo. ", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        } finally {
            close(rs, ps);
            DataBaseManager.closeConnection(conn);
        }

        return analysisIds;
    }

    public static List<Long> getEvolutionExecutedCrawlerIds(Connection conn, String entity, Long idCrawling) {
        List<Long> evaluationIds = new ArrayList<Long>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT cod_analisis FROM tanalisis t WHERE nom_entidad = ? AND cod_rastreo = ?");
            pstmt.setString(1, entity);
            pstmt.setLong(2, idCrawling);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                evaluationIds.add(rs.getLong(1));
            }
        } catch (Exception ex) {
            Logger.putLog(ex.getMessage(), AnalisisDatos.class, Logger.LOG_LEVEL_ERROR, ex);
            return null;
        } finally {
            close(rs, pstmt);
        }

        return evaluationIds;
    }

    public static String getAnalysisDate(Connection conn, Long id, String entity) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT fec_analisis FROM tanalisis WHERE cod_rastreo = ? AND nom_entidad = ?");
            ps.setLong(1, id);
            ps.setString(2, entity);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("fec_analisis");
            }
            return null;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs, ps);
        }
    }

    private static void close(ResultSet rs, PreparedStatement pstmt) {
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
    }

}
