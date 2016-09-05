package es.inteco.rastreador2.dao.basic.service;

import com.opencsv.CSVWriter;
import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.intav.datos.AnalisisDatos;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.rastreador2.actionform.basic.service.BasicServiceForm;
import es.inteco.rastreador2.utils.basic.service.BasicServiceUtils;

import java.io.StringWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class DiagnosisDAO {

    private DiagnosisDAO() {
    }

    public static void updateStatus(final Connection conn, final long idAnalysis, final String status) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE basic_service SET status = ?, send_date = ? WHERE id = ?")) {
            ps.setString(1, status);
            if (status.equals(Constants.BASIC_SERVICE_STATUS_FINISHED)) {
                ps.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
            } else {
                ps.setTimestamp(2, null);
            }
            ps.setLong(3, idAnalysis);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Error al actualizar el estado de la petición del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
        }
    }

    public static long insertBasicServices(final Connection conn, final BasicServiceForm basicServiceForm, final String status) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO basic_service (usr, language, domain, email, depth, width, report, date, status, scheduling_date, analysis_type, in_directory) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, basicServiceForm.getUser());
            ps.setString(2, basicServiceForm.getLanguage());
            if (StringUtils.isNotEmpty(basicServiceForm.getDomain())) {
                ps.setString(3, basicServiceForm.getDomain());
                ps.setString(11, Constants.BASIC_SERVICE_ANALYSIS_TYPE_URL);
            } else if (StringUtils.isNotEmpty(basicServiceForm.getContent())) {
                ps.setString(3, BasicServiceUtils.getTitleFromContent(basicServiceForm.getContent()));
                ps.setString(11, Constants.BASIC_SERVICE_ANALYSIS_TYPE_CONTENT);
            }
            ps.setString(4, basicServiceForm.getEmail());
            ps.setString(5, basicServiceForm.getProfundidad());
            ps.setString(6, basicServiceForm.getAmplitud());
            ps.setString(7, basicServiceForm.getReport());
            ps.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
            ps.setString(9, status);
            if (basicServiceForm.getSchedulingDate() != null) {
                ps.setTimestamp(10, new Timestamp(basicServiceForm.getSchedulingDate().getTime()));
            } else {
                ps.setTimestamp(10, null);
            }
            ps.setBoolean(12, basicServiceForm.isInDirectory());
            return ps.executeUpdate();
        } catch (Exception e) {
            Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return 0;
    }

    public static List<BasicServiceForm> getBasicServiceRequestByStatus(Connection conn, String status) {
        final List<BasicServiceForm> results = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM basic_service WHERE status LIKE ?")) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return results;
    }

    public static String getBasicServiceRequestCSV(final Connection conn, final java.util.Date startDate, final java.util.Date endDate) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM basic_service WHERE date BETWEEN ? AND ?")) {
            ps.setDate(1, new Date(startDate.getTime()));
            ps.setDate(2, new Date(endDate.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                Locale.setDefault(new Locale("es", "es"));
                final StringWriter stringWriter = new StringWriter();
                final CSVWriter writer = new CSVWriter(stringWriter, ';');
                writer.writeAll(rs, true);
                writer.close();
                return stringWriter.toString();
            }
        } catch (Exception e) {
            Logger.putLog("Error al insertar los datos del servicio básico", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
        }
        return "";
    }

    public static void deleteAnalysis(final Connection conn, final String entity, final long idCrawler) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM tanalisis WHERE nom_entidad = ? AND cod_rastreo = ?")) {
            ps.setString(1, entity);
            ps.setLong(2, idCrawler);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.putLog("Error al actualizar el id del rastreo. ", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
    }

    public static List<Long> getEvaluationIds(final Long idCrawling) throws Exception {
        final List<Long> analysisIds = new ArrayList<>();
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT cod_analisis FROM tanalisis WHERE cod_rastreo = ?")) {
            ps.setLong(1, idCrawling);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    analysisIds.add(rs.getLong("cod_analisis"));
                }
            }
        } catch (Exception e) {
            Logger.putLog("Error al actualizar el id del rastreo. ", DiagnosisDAO.class, Logger.LOG_LEVEL_ERROR, e);
            throw e;
        }
        return analysisIds;
    }

}
