package es.inteco.rastreador2.dao.observatorio;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.etiquetas.EtiquetaForm;
import es.inteco.rastreador2.actionform.observatorio.RangeForm;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoric;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoricComparision;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoricRange;
import es.inteco.rastreador2.actionform.observatorio.UraSendHistoricResults;
import es.inteco.rastreador2.actionform.observatorio.UraSendResultForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class RangeDAO.
 */
public class UraSendResultDAO extends DataBaseDAO {
	/**
	 * Count.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int count(Connection c, final Long idExObs) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		String query = "SELECT COUNT(*) FROM observatorio_ura_send_results e WHERE 1=1 AND id_observatory_execution = ? ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Count.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @param ids     the ids
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int count(Connection c, final Long idExObs, final String[] ids) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		int count = 0;
		if (ids != null && ids.length > 0) {
			String query = "SELECT COUNT(*) FROM observatorio_ura_send_results c WHERE 1=1 AND id_observatory_execution = ? ";
			query = query + " AND c.id_ura IN (" + ids[0];
			for (int i = 1; i < ids.length; i++) {
				query = query + "," + ids[i];
			}
			query = query + ")";
			try (PreparedStatement ps = c.prepareStatement(query)) {
				ps.setLong(1, idExObs);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					} else {
						return 0;
					}
				}
			} catch (SQLException e) {
				Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
		return count;
	}

	/**
	 * Gets the etiquetas.
	 *
	 * @param c          the c
	 * @param idExObs    the id ex obs
	 * @param isSendAuto the is send auto
	 * @return the etiquetas
	 * @throws SQLException the SQL exception
	 */
	public static List<UraSendResultForm> findAll(Connection c, final Long idExObs, final boolean isSendAuto) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<UraSendResultForm> results = new ArrayList<>();
		String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.range_value, c.has_custom_text, c.custom_text, c.send, c.send_date, c.send_error, c.file_link, c.file_pass, c.has_custom_text, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto, d.emails FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_observatory_execution = ? ";
		if (isSendAuto) {
			query = query + "AND d.send_auto = 1 ";
		}
		query = query + " ORDER BY c.id ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final UraSendResultForm form = new UraSendResultForm();
					form.setId(rs.getLong("c.id"));
					form.setUraId(rs.getLong("d.id_dependencia"));
					form.setUraName(rs.getString("d.nombre"));
					form.setTemplate(new String(Base64.decodeBase64(rs.getString("custom_text").getBytes())));
					form.setHasCustomText(rs.getBoolean("c.has_custom_text"));
					final RangeForm range = new RangeForm();
					range.setId(rs.getLong("r.id"));
					range.setName(rs.getString("r.name"));
					form.setRange(range);
					final DependenciaForm ura = new DependenciaForm();
					ura.setId(rs.getLong("d.id_dependencia"));
					ura.setName(rs.getString("d.nombre"));
					ura.setSendAuto(rs.getBoolean("d.send_auto"));
					ura.setEmails(formatEmails(rs.getString("d.emails")));
					form.setUra(ura);
					form.setSend(rs.getBoolean("c.send"));
					form.setHasCustomText(rs.getBoolean("c.has_custom_text"));
					Timestamp ts = rs.getTimestamp("c.send_date");
					if (ts != null) {
						form.setSendDate(new Date(ts.getTime()));
					}
					form.setSendError(rs.getString("c.send_error"));
					form.setFileLink(rs.getString("c.file_link"));
					form.setFilePass(rs.getString("c.file_pass"));
					form.setRangeValue(rs.getFloat("c.range_value"));
					results.add(form);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Find all not send.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<UraSendResultForm> findAllNotSend(Connection c, final Long idExObs) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<UraSendResultForm> results = new ArrayList<>();
		String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, c.send, c.send_error, c.has_custom_text, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.send=0 AND c.id_observatory_execution = ? ORDER BY c.id ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final UraSendResultForm form = new UraSendResultForm();
					form.setId(rs.getLong("c.id"));
					form.setUraId(rs.getLong("d.id_dependencia"));
					form.setUraName(rs.getString("d.nombre"));
					form.setHasCustomText(rs.getBoolean("c.has_custom_text"));
					final RangeForm range = new RangeForm();
					range.setId(rs.getLong("r.id"));
					range.setName(rs.getString("r.name"));
					form.setRange(range);
					final DependenciaForm ura = new DependenciaForm();
					ura.setId(rs.getLong("d.id_dependencia"));
					ura.setName(rs.getString("d.nombre"));
					ura.setSendAuto(rs.getBoolean("d.send_auto"));
					form.setUra(ura);
					form.setSend(rs.getBoolean("c.send"));
					form.setSendError(rs.getString("c.send_error"));
					results.add(form);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Find by ids.
	 *
	 * @param c          the c
	 * @param idExObs    the id ex obs
	 * @param ids        the ids
	 * @param isSendAuto the is send auto
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<UraSendResultForm> findByIds(Connection c, final Long idExObs, final String[] ids, final boolean isSendAuto) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<UraSendResultForm> results = new ArrayList<>();
		if (ids != null && ids.length > 0) {
			String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range,c.range_value, c.custom_text, c.send, c.has_custom_text, c.file_link, c.file_pass, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1  AND c.id_observatory_execution = ? ";
			query = query + " AND c.id_ura IN (" + ids[0];
			for (int i = 1; i < ids.length; i++) {
				query = query + "," + ids[i];
			}
			query = query + ")";
			if (isSendAuto) {
				query = query + "AND d.send_auto = 1 ";
			}
			query = query + " ORDER BY c.id ASC";
			try (PreparedStatement ps = c.prepareStatement(query)) {
				ps.setLong(1, idExObs);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final UraSendResultForm form = new UraSendResultForm();
						form.setId(rs.getLong("c.id"));
						form.setTemplate(new String(Base64.decodeBase64(rs.getString("custom_text").getBytes())));
						form.setSend(rs.getBoolean("c.send"));
						form.setHasCustomText(rs.getBoolean("c.has_custom_text"));
						final RangeForm range = new RangeForm();
						range.setId(rs.getLong("r.id"));
						range.setName(rs.getString("r.name"));
						form.setRange(range);
						final DependenciaForm ura = new DependenciaForm();
						ura.setId(rs.getLong("d.id_dependencia"));
						ura.setName(rs.getString("d.nombre"));
						ura.setSendAuto(rs.getBoolean("d.send_auto"));
						form.setFileLink(rs.getString("c.file_link"));
						form.setFilePass(rs.getString("c.file_pass"));
						form.setRangeValue(rs.getFloat("c.range_value"));
						form.setUra(ura);
						results.add(form);
					}
				}
			} catch (SQLException e) {
				Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
		return results;
	}

	/**
	 * Find by id.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @param id      the id
	 * @return the ura send result form
	 * @throws SQLException the SQL exception
	 */
	public static UraSendResultForm findById(Connection c, final Long idExObs, final Long id) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		UraSendResultForm result = null;
		String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, c.send, c.has_custom_text, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_observatory_execution = ? AND c.id = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			ps.setLong(2, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final UraSendResultForm form = new UraSendResultForm();
					form.setId(rs.getLong("c.id"));
					form.setTemplate(new String(Base64.decodeBase64(rs.getString("custom_text").getBytes())));
					form.setSend(rs.getBoolean("c.send"));
					form.setHasCustomText(rs.getBoolean("c.has_custom_text"));
					final RangeForm range = new RangeForm();
					range.setId(rs.getLong("r.id"));
					range.setName(rs.getString("r.name"));
					form.setRange(range);
					final DependenciaForm ura = new DependenciaForm();
					ura.setId(rs.getLong("d.id_dependencia"));
					ura.setName(rs.getString("d.nombre"));
					ura.setSendAuto(rs.getBoolean("d.send_auto"));
					form.setUra(ura);
					return form;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return result;
	}

	/**
	 * Save.
	 *
	 * @param c    the c
	 * @param list the list
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void save(Connection c, final List<UraSendResultForm> list) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		if (list != null && !list.isEmpty()) {
			for (UraSendResultForm u : list) {
				save(c, u);
			}
		}
	}

	/**
	 * Save.
	 *
	 * @param c    the c
	 * @param form the form
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void save(Connection c, final UraSendResultForm form) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement(
					"INSERT INTO observatorio_ura_send_results(id_observatory_execution, id_ura, id_range, custom_text, range_value) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE id_range = ?, range_value = ?;",
					Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, form.getIdObservatoryExecution());
			ps.setLong(2, form.getIdUra());
			ps.setLong(3, form.getIdRange());
			// Encode BASE64 code
			if (form.getTemplate() != null && !org.apache.commons.lang3.StringUtils.isEmpty(form.getTemplate())) {
				String template = new String(form.getTemplate());
				if (!StringUtils.isEmpty(template)) {
					template = new String(Base64.encodeBase64(template.getBytes("UTF-8")));
				}
				ps.setString(4, template);
			}
			ps.setString(4, "");
			ps.setFloat(5, form.getRangeValue());
			ps.setLong(6, form.getIdRange());
			ps.setFloat(7, form.getRangeValue());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating range failed, no rows affected.");
			}
			c.commit();
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog("SQL_EXCEPTION: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Update.
	 *
	 * @param c    the c
	 * @param form the form
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void update(Connection c, final UraSendResultForm form) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final String query = "UPDATE observatorio_ura_send_results SET custom_text= ? WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			// Encode BASE64 code
			String template = new String(form.getTemplate());
			if (!StringUtils.isEmpty(template)) {
				template = new String(Base64.encodeBase64(template.getBytes("UTF-8")));
			}
			ps.setString(1, template);
			ps.setLong(2, form.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update.
	 *
	 * @param c              the c
	 * @param idObsExecution the id obs execution
	 * @param error          the error
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void updateErrorAll(Connection c, final Long idObsExecution, final String error) throws SQLException, UnsupportedEncodingException {
		final String query = "UPDATE observatorio_ura_send_results SET send = 0, send_error = ?  WHERE id_observatory_execution = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, error);
			ps.setLong(2, idObsExecution);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Mark send.
	 *
	 * @param c    the c
	 * @param form the form
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void markSend(Connection c, final UraSendResultForm form) throws Exception, UnsupportedEncodingException {
		c = reOpenConnectionIfIsNecessary(c);
		final String query = "UPDATE observatorio_ura_send_results SET send= ?, send_date =?, send_error = ?, file_link = ?, file_pass = ?  WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setBoolean(1, form.isSend());
			ps.setTimestamp(2, new Timestamp(form.getSendDate().getTime()));
			ps.setString(3, form.getSendError());
			ps.setString(4, form.getFileLink());
			ps.setString(5, form.getFilePass());
			ps.setLong(6, form.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Mark has custom text.
	 *
	 * @param c              the c
	 * @param hasCustomTexts the has custom texts
	 * @param urasIds        the uras ids
	 * @param idObsExecution the id obs execution
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void markHasCustomText(Connection c, final boolean hasCustomTexts, final String[] urasIds, final Long idObsExecution) throws Exception, UnsupportedEncodingException {
		c = reOpenConnectionIfIsNecessary(c);
		String query = "UPDATE observatorio_ura_send_results SET has_custom_text = ?  WHERE id_observatory_execution = ? ";
		if (urasIds != null && urasIds.length > 0) {
			query = query + " AND id_ura IN (" + urasIds[0];
			for (int i = 1; i < urasIds.length; i++) {
				query = query + "," + urasIds[i];
			}
			query = query + ")";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setBoolean(1, hasCustomTexts);
			ps.setLong(2, idObsExecution);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete etiqueta.
	 *
	 * @param c  the c
	 * @param id the id
	 * @throws SQLException the SQL exception
	 */
	public static void delete(Connection c, final Long id) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_ura_send_results WHERE id = ?")) {
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Delete all.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @throws SQLException the SQL exception
	 */
	public static void deleteAll(Connection c, final Long idExObs) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_ura_send_results WHERE id_observatory_execution = ?")) {
			ps.setLong(1, idExObs);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Save historic send.
	 *
	 * @param c        the c
	 * @param historic the historic
	 * @throws SQLException the SQL exception
	 */
	public static void saveHistoricSend(Connection c, final UraSendHistoric historic) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement("INSERT INTO observatorio_send_historic(id_observatory_execution, cco, subject, ids_observatory_execution_evol, send_date) VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, historic.getIdExObs());
			ps.setString(2, historic.getCco());
			ps.setString(3, historic.getSubject());
			ps.setString(4, historic.getIdsExObs());
			ps.setTimestamp(5, new Timestamp(new Date().getTime()));
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					Long idSendHistoric = rs.getLong(1);
					if (historic.getComparisions() != null && !historic.getComparisions().isEmpty()) {
						for (UraSendHistoricComparision comparision : historic.getComparisions()) {
							comparision.setIdSendHistoric(idSendHistoric);
							PreparedStatement psComparision = c.prepareStatement("INSERT INTO observatorio_send_historic_comparision(id_send_historic, id_tag, date_previous) VALUES (?,?,?)");
							psComparision.setLong(1, comparision.getIdSendHistoric());
							psComparision.setInt(2, comparision.getIdTag());
							psComparision.setString(3, comparision.getPrevious());
							psComparision.executeUpdate();
						}
					}
					if (historic.getRanges() != null && !historic.getRanges().isEmpty()) {
						for (UraSendHistoricRange range : historic.getRanges()) {
							range.setIdSendHistoric(idSendHistoric);
							PreparedStatement psRange = c.prepareStatement(
									"INSERT INTO observatorio_send_historic_ranges(id_send_historic, name,min_value,max_value,min_value_operator,max_value_operator,template) VALUES (?,?,?,?,?,?,?)");
							psRange.setLong(1, range.getIdSendHistoric());
							psRange.setString(2, range.getName());
							psRange.setFloat(3, range.getMinValue());
							psRange.setFloat(4, range.getMaxValue());
							psRange.setString(5, range.getMinValueOperator());
							psRange.setString(6, range.getMaxValueOperator());
							// Encode BASE64 code
							String template = new String(range.getTemplate());
							try {
								if (!StringUtils.isEmpty(template)) {
									template = new String(Base64.encodeBase64(template.getBytes("UTF-8")));
								}
							} catch (UnsupportedEncodingException e) {
								Logger.putLog("SQL_EXCEPTION: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
							}
							psRange.setString(7, template);
							psRange.executeUpdate();
						}
					}
					if (historic.getResults() != null && !historic.getResults().isEmpty()) {
						for (UraSendHistoricResults result : historic.getResults()) {
							result.setIdSendHistoric(idSendHistoric);
							PreparedStatement psResult = c.prepareStatement(
									"INSERT INTO observatorio_send_historic_results(id_send_historic, id_ura, range_name, custom_text, range_value, mail,send_date,expiration_date,send_error,file_link,file_pass,send ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
									Statement.RETURN_GENERATED_KEYS);
							psResult.setLong(1, result.getIdSendHistoric());
							psResult.setLong(2, result.getUra().getId());
							psResult.setString(3, result.getRange().getName());
							psResult.setString(4, "");
							try {
								if (result.getTemplate() != null && !org.apache.commons.lang3.StringUtils.isEmpty(result.getTemplate())) {
									String template = new String(result.getTemplate());
									if (!StringUtils.isEmpty(template)) {
										template = new String(Base64.encodeBase64(template.getBytes("UTF-8")));
									}
									psResult.setString(4, template);
								}
							} catch (UnsupportedEncodingException e) {
								Logger.putLog("SQL_EXCEPTION: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
								psResult.setString(4, "");
							}
							psResult.setFloat(5, result.getRangeValue());
							// Mail
							psResult.setString(6, "");
							try {
								if (result.getMail() != null && !org.apache.commons.lang3.StringUtils.isEmpty(result.getMail())) {
									String mail = new String(result.getMail());
									if (!StringUtils.isEmpty(mail)) {
										mail = new String(Base64.encodeBase64(mail.getBytes("UTF-8")));
									}
									psResult.setString(6, mail);
								}
							} catch (UnsupportedEncodingException e) {
								Logger.putLog("SQL_EXCEPTION: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
								psResult.setString(6, "");
							}
							psResult.setNull(7, Types.TIMESTAMP);
							if (result.getSendDate() != null) {
								psResult.setTimestamp(7, new Timestamp(result.getSendDate().getTime()));
							}
							psResult.setNull(8, Types.TIMESTAMP);
							if (result.getValidDate() != null) {
								psResult.setTimestamp(8, new Timestamp(result.getValidDate().getTime()));
							}
							psResult.setString(9, result.getSendError());
							psResult.setString(10, result.getFileLink());
							psResult.setString(11, result.getFilePass());
							psResult.setBoolean(12, result.isSend());
							psResult.executeUpdate();
						}
					}
				}
			}
			c.commit();
		} catch (Exception e) {
			c.rollback();
			Logger.putLog("SQL_EXCEPTION: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DAOUtils.closeQueries(ps, null);
		}
	}

	/**
	 * Gets the send historic.
	 *
	 * @param c             the c
	 * @param idExObs       the id ex obs
	 * @param idObservatory the id observatory
	 * @return the send historic
	 * @throws SQLException the SQL exception
	 */
	public static List<UraSendHistoric> getSendHistoric(Connection c, final Long idExObs, final Long idObservatory) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		List<UraSendHistoric> historics = new ArrayList<>();
		String query = "SELECT * FROM observatorio_send_historic h WHERE h.id_observatory_execution = ? ";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					UraSendHistoric historic = new UraSendHistoric();
					historic.setId(rs.getLong("id"));
					historic.setIdExObs(rs.getLong("id_observatory_execution"));
					historic.setCco(rs.getString("cco"));
					historic.setSubject(rs.getString("subject"));
					historic.setIdsExObs(rs.getString("ids_observatory_execution_evol"));
					Timestamp tsH = rs.getTimestamp("h.send_date");
					if (tsH != null) {
						historic.setSendDate(new Date(tsH.getTime()));
					}
					historic.setObservatories(ObservatorioDAO.getFulfilledObservatories(c, idObservatory, -1, null, historic.getIdsExObs().split(",")));
					// Ranges
					PreparedStatement psRanges = c.prepareStatement("SELECT * FROM observatorio_send_historic_ranges hr WHERE hr.id_send_historic = ? ");
					psRanges.setLong(1, historic.getId());
					ResultSet rsRanges = psRanges.executeQuery();
					List<UraSendHistoricRange> rangeList = new ArrayList<>();
					while (rsRanges.next()) {
						UraSendHistoricRange range = new UraSendHistoricRange();
						range.setId(rsRanges.getLong("id"));
						range.setName(rsRanges.getString("name"));
						range.setMinValue(rsRanges.getFloat("min_value"));
						range.setMaxValue(rsRanges.getFloat("max_value"));
						range.setMinValueOperator(rsRanges.getString("min_value_operator"));
						range.setMaxValueOperator(rsRanges.getString("max_value_operator"));
						range.setTemplate(new String(Base64.decodeBase64(rsRanges.getString("template").getBytes())));
						rangeList.add(range);
					}
					historic.setRanges(rangeList);
					// Comparisions
					PreparedStatement psComparision = c.prepareStatement("SELECT * FROM observatorio_send_historic_comparision hr WHERE hr.id_send_historic = ? ");
					psComparision.setLong(1, historic.getId());
					ResultSet rsComparision = psComparision.executeQuery();
					List<UraSendHistoricComparision> comparisionList = new ArrayList<>();
					while (rsComparision.next()) {
						UraSendHistoricComparision comparision = new UraSendHistoricComparision();
						comparision.setIdTag(rsComparision.getInt("id_tag"));
						comparision.setPrevious(rsComparision.getString("date_previous"));
						EtiquetaForm tag = EtiquetaDAO.getById(c, comparision.getIdTag());
						if (tag != null) {
							comparision.setTagName(tag.getName());
						}
						comparisionList.add(comparision);
					}
					historic.setComparisions(comparisionList);
					// Results
					PreparedStatement psResults = c.prepareStatement(
							"SELECT c.id, c.id_send_historic, c.id_ura, c.range_name, c.range_value, c.custom_text, c.send, c.send_date, c.expiration_date, c.send_error, c.file_link, c.file_pass, c.mail, d.id_dependencia, d.nombre, d.send_auto, d.emails FROM observatorio_send_historic_results c JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_send_historic = ? ORDER BY c.id ASC ");
					psResults.setLong(1, historic.getId());
					ResultSet rsResults = psResults.executeQuery();
					List<UraSendHistoricResults> resultsList = new ArrayList<>();
					while (rsResults.next()) {
						final UraSendHistoricResults result = new UraSendHistoricResults();
						result.setId(rsResults.getLong("c.id"));
						result.setUraId(rsResults.getLong("d.id_dependencia"));
						result.setUraName(rsResults.getString("d.nombre"));
						result.setRangeName(rsResults.getString("c.range_name"));
						result.setTemplate(new String(Base64.decodeBase64(rsResults.getString("c.custom_text").getBytes())));
						result.setMail(new String(Base64.decodeBase64(rsResults.getString("c.mail").getBytes())));
						final DependenciaForm ura = new DependenciaForm();
						ura.setId(rsResults.getLong("d.id_dependencia"));
						ura.setName(rsResults.getString("d.nombre"));
						ura.setSendAuto(rsResults.getBoolean("d.send_auto"));
						ura.setEmails(rsResults.getString("d.emails"));
						result.setUra(ura);
						result.setSend(rsResults.getBoolean("c.send"));
						Timestamp ts = rsResults.getTimestamp("c.send_date");
						if (ts != null) {
							result.setSendDate(new Date(ts.getTime()));
						}
						Timestamp tsv = rsResults.getTimestamp("c.expiration_date");
						if (tsv != null) {
							result.setValidDate(new Date(tsv.getTime()));
						}
						result.setSendError(rsResults.getString("c.send_error"));
						result.setFileLink(rsResults.getString("c.file_link"));
						result.setFilePass(rsResults.getString("c.file_pass"));
						result.setRangeValue(rsResults.getFloat("c.range_value"));
						resultsList.add(result);
					}
					historic.setResults(resultsList);
					// Get sendAuto Not
					historic.setNotAuto(UraSendResultDAO.findAllSendAutoFalseHistoric(c, historic.getId()));
					// Get sended
					historic.setSended(UraSendResultDAO.findAllSendHistoric(c, historic.getId()));
					historics.add(historic);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return historics;
	}

	/**
	 * Find all send.
	 *
	 * @param c          the c
	 * @param idHistoric the id historic
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<UraSendHistoricResults> findAllSendHistoric(Connection c, final Long idHistoric) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<UraSendHistoricResults> results = new ArrayList<>();
		String query = "SELECT c.id, c.id_send_historic, c.id_ura, c.range_name, c.range_value, c.custom_text, c.send, c.send_error, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_send_historic_results c JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.send=1 AND c.id_send_historic = ? ORDER BY c.id ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idHistoric);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final UraSendHistoricResults form = new UraSendHistoricResults();
					form.setId(rs.getLong("c.id"));
					form.setUraId(rs.getLong("d.id_dependencia"));
					form.setUraName(rs.getString("d.nombre"));
					form.setRangeName(rs.getString("c.range_name"));
					final DependenciaForm ura = new DependenciaForm();
					ura.setId(rs.getLong("d.id_dependencia"));
					ura.setName(rs.getString("d.nombre"));
					ura.setSendAuto(rs.getBoolean("d.send_auto"));
					form.setUra(ura);
					form.setSend(rs.getBoolean("c.send"));
					form.setSendError(rs.getString("c.send_error"));
					form.setRangeValue(rs.getFloat("c.range_value"));
					results.add(form);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Find all send auto false.
	 *
	 * @param c          the c
	 * @param idHistoric the id historic
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<UraSendHistoricResults> findAllSendAutoFalseHistoric(Connection c, final Long idHistoric) throws Exception {
		c = reOpenConnectionIfIsNecessary(c);
		final List<UraSendHistoricResults> results = new ArrayList<>();
		String query = "SELECT c.id, c.id_send_historic, c.id_ura, c.range_name, c.range_value, c.custom_text, c.send, c.send_error, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_send_historic_results c JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND d.send_auto=0 AND c.id_send_historic = ? ORDER BY c.id ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idHistoric);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final UraSendHistoricResults form = new UraSendHistoricResults();
					form.setId(rs.getLong("c.id"));
					form.setUraId(rs.getLong("d.id_dependencia"));
					form.setUraName(rs.getString("d.nombre"));
					form.setRangeName(rs.getString("c.range_name"));
					final DependenciaForm ura = new DependenciaForm();
					ura.setId(rs.getLong("d.id_dependencia"));
					ura.setName(rs.getString("d.nombre"));
					ura.setSendAuto(rs.getBoolean("d.send_auto"));
					form.setUra(ura);
					form.setSend(rs.getBoolean("c.send"));
					form.setSendError(rs.getString("c.send_error"));
					form.setRangeValue(rs.getFloat("c.range_value"));
					results.add(form);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Email formatter
	 * 
	 * @param emails
	 * @return Formatted emails
	 */
	private static String formatEmails(String emails) {
		String formattedEmails = "";
		if (emails != null) {
			formattedEmails = emails.trim().replaceAll("\\s+", "");
		}
		return formattedEmails;
	}
}
