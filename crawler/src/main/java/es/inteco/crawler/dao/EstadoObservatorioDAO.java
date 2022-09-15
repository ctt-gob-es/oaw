package es.inteco.crawler.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.job.ObservatoryStatus;
import es.inteco.crawler.job.ObservatorySummary;
import es.inteco.crawler.job.ObservatorySummaryTimes;

/**
 * The Class EstadoObservatorioDAO.
 */
public class EstadoObservatorioDAO {
	/**
	 * Instantiates a new estado observatorio DAO.
	 */
	public EstadoObservatorioDAO() {
	}

	/**
	 * Find estado observatorio.
	 *
	 * @param connection              the connection
	 * @param idObservatorio          the id observatorio
	 * @param idEjecucionObservatorio the id ejecucion observatorio
	 * @return the estado observatorio
	 * @throws SQLException the SQL exception
	 */
	public static ObservatoryStatus findEstadoObservatorio(final Connection connection, final int idObservatorio, final int idEjecucionObservatorio) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT id, id_observatorio, id_ejecucion_observatorio, nombre, url, total_url, total_url_analizadas, ultima_url, fecha_ultima_url, actual_url, tiempo_medio, tiempo_acumulado FROM observatorio_estado WHERE id_observatorio = ? AND id_ejecucion_observatorio = ?")) {
			ps.setInt(1, idObservatorio);
			ps.setInt(2, idEjecucionObservatorio);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ObservatoryStatus estado = new ObservatoryStatus();
					estado.setId(rs.getInt("id"));
					estado.setIdObservatorio(rs.getInt("id_observatorio"));
					estado.setIdEjecucionObservatorio(rs.getInt("id_ejecucion_observatorio"));
					estado.setNombre(rs.getString("nombre"));
					estado.setUrl(rs.getString("url"));
					estado.setTotalUrl(rs.getInt("total_url"));
					estado.setTotalUrlAnalizadas(rs.getInt("total_url_analizadas"));
					estado.setUltimaUrl(rs.getString("ultima_url"));
					estado.setFechaUltimaUrl(rs.getTimestamp("fecha_ultima_url"));
					estado.setActualUrl(rs.getString("actual_url"));
					estado.setTiempoMedio(rs.getInt("tiempo_medio"));
					estado.setTiempoAcumulado(rs.getInt("tiempo_acumulado"));
					// Tiempo estimado
					estado.setTiempoEstimado((estado.getTotalUrl() - estado.getTotalUrlAnalizadas()) * estado.getTiempoMedio());
					// Porcentaje completado
					estado.setPorcentajeCompletado(((float) estado.getTotalUrlAnalizadas() / (float) estado.getTotalUrl()) * 100);
					return estado;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("No se ha podido registrar el estado el análisis actual", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return new ObservatoryStatus();
	}

	/**
	 * Update estado.
	 *
	 * @param connection the connection
	 * @param estado     the estado
	 * @return the long
	 * @throws SQLException the SQL exception
	 */
	public static Integer updateEstado(final Connection connection, ObservatoryStatus estado) throws SQLException {
		if (estado.getId() != null) {
			try (PreparedStatement ps = connection.prepareStatement(
					"UPDATE observatorio_estado SET nombre = ? , url =?, ultima_url = ?, actual_url = ?, fecha_ultima_url = ?, tiempo_medio = ?,  total_url_analizadas= ? , tiempo_acumulado = ?, total_url = ? WHERE id = ?")) {
				ps.setString(1, estado.getNombre());
				ps.setString(2, estado.getUrl());
				ps.setString(3, estado.getUltimaUrl());
				ps.setString(4, estado.getActualUrl());
				if (estado.getFechaUltimaUrl() != null) {
					ps.setTimestamp(5, new Timestamp(estado.getFechaUltimaUrl().getTime()));
				} else {
					ps.setDate(5, null);
				}
				ps.setFloat(6, estado.getTiempoMedio());
				ps.setInt(7, estado.getTotalUrlAnalizadas());
				ps.setFloat(8, estado.getTiempoAcumulado());
				ps.setInt(9, estado.getTotalUrl());
				ps.setInt(10, estado.getId());
				ps.executeUpdate();
			} catch (SQLException e) {
				Logger.putLog("No se ha podido registrar el estado del análisis actual", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
			return estado.getId();
		} else {
			try (PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO observatorio_estado (id_observatorio, id_ejecucion_observatorio, nombre, url, total_url, total_url_analizadas ,ultima_url, fecha_ultima_url, actual_url, tiempo_medio, tiempo_acumulado) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS)) {
				ps.setInt(1, estado.getIdObservatorio());
				ps.setInt(2, estado.getIdEjecucionObservatorio());
				ps.setString(3, estado.getNombre());
				ps.setString(4, estado.getUrl());
				ps.setInt(5, estado.getTotalUrl());
				ps.setInt(6, estado.getTotalUrlAnalizadas());
				ps.setString(7, estado.getUltimaUrl());
				ps.setDate(8, null);
				ps.setString(9, estado.getActualUrl());
				ps.setFloat(10, estado.getTiempoMedio());
				ps.setFloat(11, estado.getTiempoAcumulado());
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			} catch (SQLException e) {
				Logger.putLog("No se ha podido registrar el estado del análisis actual", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
		return null;
	}

	/**
	 * Delete estado.
	 *
	 * @param connection              the connection
	 * @param idObservatorio          the id observatorio
	 * @param idEjecucionObservatorio the id ejecucion observatorio
	 * @throws SQLException the SQL exception
	 */
	public static void deleteEstado(final Connection connection, final int idObservatorio, final int idEjecucionObservatorio) throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM observatorio_estado WHERE 	id_observatorio = ? AND id_ejecucion_observatorio = ?")) {
			ps.setInt(1, idObservatorio);
			ps.setInt(2, idEjecucionObservatorio);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("No se ha podido borrado el estado del análisis actual", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
	}

	/**
	 * Gets the observatory summary.
	 *
	 * @param c                       the c
	 * @param idObservatorio          the id observatorio
	 * @param idEjecucionObservatorio the id ejecucion observatorio
	 * @return the observatory summary
	 * @throws SQLException the SQL exception
	 */
	public static ObservatorySummary getObservatorySummary(final Connection c, final int idObservatorio, final int idEjecucionObservatorio) throws SQLException {
		ObservatorySummary summary = new ObservatorySummary();
		try (PreparedStatement ps = c.prepareStatement("SELECT (SELECT estado FROM observatorios_realizados WHERE id = ?) AS ESTADO_OBS, "
				+ "(SELECT count(*) FROM rastreo r WHERE r.id_observatorio = ? AND r.activo = 1) AS TOTAL_SEMILLAS, "
				+ "(SELECT count(*) FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo in (select id_rastreo from rastreos_realizados rr where rr.id_obs_realizado = ? )) AS TOTAL_ANALIZADAS, "
				+ "(SELECT count(*) FROM rastreo r WHERE r.id_observatorio = ? AND r.id_rastreo in (select id_rastreo from rastreos_realizados rr where rr.id_obs_realizado = ? AND rr.id IN (select ta.cod_rastreo as id_rastreo from tanalisis ta))) AS TOTAL_ANALIZADAS_OK, "
				+ "(SELECT sum(tiempo_rastreo) FROM (SELECT cod_rastreo, TIMESTAMPDIFF(MINUTE, min(fec_analisis),max(fec_analisis)) AS tiempo_rastreo FROM tanalisis WHERE cod_rastreo IN (SELECT id FROM rastreos_realizados WHERE id_obs_realizado = ?) group by cod_rastreo) AS SUMTIEMPOS) AS TIEMPO")) {
			ps.setInt(1, idEjecucionObservatorio);
			ps.setInt(2, idObservatorio);
			ps.setInt(3, idObservatorio);
			ps.setInt(4, idEjecucionObservatorio);
			ps.setInt(5, idObservatorio);
			ps.setInt(6, idEjecucionObservatorio);
			ps.setInt(7, idEjecucionObservatorio);
			// ps.setInt(8, idEjecucionObservatorio);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					summary.setIdEstado(rs.getLong("ESTADO_OBS"));
					switch (rs.getInt("ESTADO_OBS")) {
					case 1:
						summary.setEstado("No lanzado");
						break;
					case 2:
						summary.setEstado("Lanzado");
						break;
					case 3:
						summary.setEstado("Parado");
						break;
					case 0:
						summary.setEstado("Terminado");
						break;
					case 5:
						summary.setEstado("Error");
						break;
					}
					// Total semillas
					summary.setTotalSemillas(rs.getInt("TOTAL_SEMILLAS"));
					summary.setSemillasAnalizadasOk(rs.getInt("TOTAL_ANALIZADAS_OK"));
					// Semillas analizadas correctamente
					summary.setSemillasAnalizadas(rs.getInt("TOTAL_ANALIZADAS"));
					// Porcentaje completado
					if (rs.getLong("ESTADO_OBS") != 0) {
						summary.setPorcentajeCompletado(((float) summary.getSemillasAnalizadas() / (float) summary.getTotalSemillas()) * 100);
						summary.setPorcentajeCompletadoOk(((float) summary.getSemillasAnalizadasOk() / (float) summary.getSemillasAnalizadas()) * 100);
					} else {
						summary.setPorcentajeCompletado(((float) summary.getSemillasAnalizadas() / (float) summary.getSemillasAnalizadas()) * 100);
						summary.setPorcentajeCompletadoOk(((float) summary.getSemillasAnalizadasOk() / (float) summary.getSemillasAnalizadas()) * 100);
					}
					// Diferencia en minutos entre la primera y al última ejecución
					summary.setTiempoTotal(rs.getInt("TIEMPO"));
					summary.setTiempoTotalHoras(rs.getInt("TIEMPO") / 60);
					// Tiempo medio
					if (summary.getSemillasAnalizadas() > 0) {
						summary.setTiempoMedio(summary.getTiempoTotal() / summary.getSemillasAnalizadas());
					} else {
						summary.setTiempoMedio(summary.getTiempoTotal());
					}
					// Tiempo estimado
					final int tiempoEstimado = (summary.getTotalSemillas() - summary.getSemillasAnalizadas()) * summary.getTiempoMedio();
					summary.setTiempoEstimado(tiempoEstimado >= 0 ? tiempoEstimado : 0);
					summary.setTiempoEstimadoHoras(summary.getTiempoEstimado() / 60);
					// Tiempo minimo
					summary.setTiempoMinimo(new ObservatorySummaryTimes(11, "http://www.defensa.gob.es", "Ministerio de Defensa"));
					summary.setTiempoMaximo(new ObservatorySummaryTimes(11, "http://www.mineco.gob.es", "Ministerio de Economía"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("No se ha podido registrar el estado del análisis actual", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return summary;
	}

	/**
	 * Find execution tags.
	 *
	 * @param connection  the connection
	 * @param executionId the id ejecución observatorio
	 * @return Execution tags
	 * @throws SQLException the SQL exception
	 */
	public static String[] getExecutionTags(final Connection connection, final Long executionId) throws SQLException {
		String executionTags = null;
		String query = "SELECT id, tags FROM observatorios_realizados o WHERE id = ?";
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			ps.setLong(1, executionId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					executionTags = rs.getString("tags");
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EstadoObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		if (executionTags != null) {
			return executionTags.split("\\,", -1);
		} else {
			return null;
		}
	}
}
