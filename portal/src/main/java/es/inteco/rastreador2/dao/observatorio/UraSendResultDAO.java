package es.inteco.rastreador2.dao.observatorio;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.observatorio.RangeForm;
import es.inteco.rastreador2.actionform.observatorio.UraSendResultForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class RangeDAO.
 */
public class UraSendResultDAO {
	/**
	 * Count.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int count(Connection c, final Long idExObs) throws SQLException {
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
	public static int count(Connection c, final Long idExObs, final String[] ids) throws SQLException {
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
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @return the etiquetas
	 * @throws SQLException the SQL exception
	 */
//	id_observatory_execution, id_ura, id_range, custom_text
	public static List<UraSendResultForm> findAll(Connection c, final Long idExObs) throws SQLException {
		final List<UraSendResultForm> results = new ArrayList<>();
		String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, c.send, c.send_date, c.send_error, c.file_link, c.file_pass, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto, d.emails FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_observatory_execution = ? ORDER BY c.id ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final UraSendResultForm form = new UraSendResultForm();
					form.setId(rs.getLong("c.id"));
					form.setUraId(rs.getLong("d.id_dependencia"));
					form.setUraName(rs.getString("d.nombre"));
					form.setTemplate(new String(Base64.decodeBase64(rs.getString("custom_text").getBytes())));
					final RangeForm range = new RangeForm();
					range.setId(rs.getLong("r.id"));
					range.setName(rs.getString("r.name"));
					form.setRange(range);
					final DependenciaForm ura = new DependenciaForm();
					ura.setId(rs.getLong("d.id_dependencia"));
					ura.setName(rs.getString("d.nombre"));
					ura.setSendAuto(rs.getBoolean("d.send_auto"));
					ura.setEmails(rs.getString("d.emails"));
					form.setUra(ura);
					form.setSend(rs.getBoolean("c.send"));
					Timestamp ts = rs.getTimestamp("c.send_date");
					if (ts != null) {
						form.setSendDate(new Date(ts.getTime()));
					}
					form.setSendError(rs.getString("c.send_error"));
					form.setFileLink(rs.getString("c.file_link"));
					form.setFilePass(rs.getString("c.file_pass"));
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
	public static List<UraSendResultForm> findAllNotSend(Connection c, final Long idExObs) throws SQLException {
		final List<UraSendResultForm> results = new ArrayList<>();
		String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, c.send, c.send_error, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.send=0 AND c.id_observatory_execution = ? ORDER BY c.id ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final UraSendResultForm form = new UraSendResultForm();
					form.setId(rs.getLong("c.id"));
					form.setUraId(rs.getLong("d.id_dependencia"));
					form.setUraName(rs.getString("d.nombre"));
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
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @param ids     the ids
	 * @return the list
	 * @throws SQLException the SQL exception
	 */
	public static List<UraSendResultForm> findByIds(Connection c, final Long idExObs, final String[] ids) throws SQLException {
		final List<UraSendResultForm> results = new ArrayList<>();
		if (ids != null && ids.length > 0) {
			String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, c.send, c.file_link, c.file_pass, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_observatory_execution = ? ";
			query = query + " AND c.id_ura IN (" + ids[0];
			for (int i = 1; i < ids.length; i++) {
				query = query + "," + ids[i];
			}
			query = query + ")";
			query = query + " ORDER BY c.id ASC";
			try (PreparedStatement ps = c.prepareStatement(query)) {
				ps.setLong(1, idExObs);
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						final UraSendResultForm form = new UraSendResultForm();
						form.setId(rs.getLong("c.id"));
						form.setTemplate(new String(Base64.decodeBase64(rs.getString("custom_text").getBytes())));
						form.setSend(rs.getBoolean("c.send"));
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
	public static UraSendResultForm findById(Connection c, final Long idExObs, final Long id) throws SQLException {
		UraSendResultForm result = null;
		String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, c.send, r.id, r.name, d.id_dependencia, d.nombre, d.send_auto FROM observatorio_ura_send_results c LEFT JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_observatory_execution = ? AND c.id = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			ps.setLong(2, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final UraSendResultForm form = new UraSendResultForm();
					form.setId(rs.getLong("c.id"));
					form.setTemplate(new String(Base64.decodeBase64(rs.getString("custom_text").getBytes())));
					form.setSend(rs.getBoolean("c.send"));
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
	public static void save(Connection c, final List<UraSendResultForm> list) throws SQLException, UnsupportedEncodingException {
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
	public static void save(Connection c, final UraSendResultForm form) throws SQLException, UnsupportedEncodingException {
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
	public static void update(Connection c, final UraSendResultForm form) throws SQLException, UnsupportedEncodingException {
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
	 * Mark send.
	 *
	 * @param c    the c
	 * @param form the form
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void markSend(Connection c, final UraSendResultForm form) throws SQLException, UnsupportedEncodingException {
		final String query = "UPDATE observatorio_ura_send_results SET send= ?, send_date =?, send_error = ?, file_link = ?, file_pass = ?  WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setBoolean(1, form.isSend());
			ps.setTimestamp(2, new Timestamp(new Date().getTime()));
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
	 * Delete etiqueta.
	 *
	 * @param c  the c
	 * @param id the id
	 * @throws SQLException the SQL exception
	 */
	public static void delete(Connection c, final Long id) throws SQLException {
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
	public static void deleteAll(Connection c, final Long idExObs) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_ura_send_results WHERE id_observatory_execution = ?")) {
			ps.setLong(1, idExObs);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraSendResultDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}
}
