package es.inteco.rastreador2.dao.observatorio;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.observatorio.RangeForm;
import es.inteco.rastreador2.actionform.observatorio.UraCustomTextForm;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class RangeDAO.
 */
public class UraCustomTextDAO {
	/**
	 * Count.
	 *
	 * @param c       the c
	 * @param idExObs the id ex obs
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int count(Connection c, final Long idExObs) throws SQLException {
		String query = "SELECT COUNT(*) FROM observatorio_template_custom_text_ura e WHERE 1=1 AND id_observatory_execution = ? ";
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
			Logger.putLog("SQL Exception: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
			String query = "SELECT COUNT(*) FROM observatorio_template_custom_text_ura c WHERE 1=1 AND id_observatory_execution = ? ";
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
				Logger.putLog("SQL Exception: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
	public static List<UraCustomTextForm> findAll(Connection c, final Long idExObs) throws SQLException {
		final List<UraCustomTextForm> results = new ArrayList<>();
		String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, r.id, r.name, d.id_dependencia, d.nombre FROM observatorio_template_custom_text_ura c JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_observatory_execution = ? ORDER BY c.id ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idExObs);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final UraCustomTextForm form = new UraCustomTextForm();
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
					form.setUra(ura);
					results.add(form);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
	public static List<UraCustomTextForm> findByIds(Connection c, final Long idExObs, final String[] ids) throws SQLException {
		final List<UraCustomTextForm> results = new ArrayList<>();
		if (ids != null && ids.length > 0) {
			String query = "SELECT c.id, c.id_observatory_execution, c.id_ura, c.id_range, c.custom_text, r.id, r.name, d.id_dependencia, d.nombre FROM observatorio_template_custom_text_ura c JOIN observatorio_template_range r ON c.id_range = r.id JOIN dependencia d ON c.id_ura = d.id_dependencia WHERE 1=1 AND c.id_observatory_execution = ? ";
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
						final UraCustomTextForm form = new UraCustomTextForm();
						form.setId(rs.getLong("c.id"));
						form.setTemplate(new String(Base64.decodeBase64(rs.getString("custom_text").getBytes())));
						final RangeForm range = new RangeForm();
						range.setId(rs.getLong("r.id"));
						range.setName(rs.getString("r.name"));
						form.setRange(range);
						final DependenciaForm ura = new DependenciaForm();
						ura.setId(rs.getLong("d.id_dependencia"));
						ura.setName(rs.getString("d.nombre"));
						form.setUra(ura);
						results.add(form);
					}
				}
			} catch (SQLException e) {
				Logger.putLog("SQL Exception: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
		return results;
	}

	/**
	 * Save.
	 *
	 * @param c    the c
	 * @param list the list
	 * @throws SQLException                 the SQL exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static void save(Connection c, final List<UraCustomTextForm> list) throws SQLException, UnsupportedEncodingException {
		if (list != null && !list.isEmpty()) {
			for (UraCustomTextForm u : list) {
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
	public static void save(Connection c, final UraCustomTextForm form) throws SQLException, UnsupportedEncodingException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement(
					"INSERT INTO observatorio_template_custom_text_ura(id_observatory_execution, id_ura, id_range, custom_text, range_value) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE id_range = ?, range_value = ?;",
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
			Logger.putLog("SQL_EXCEPTION: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
	public static void update(Connection c, final UraCustomTextForm form) throws SQLException, UnsupportedEncodingException {
		final String query = "UPDATE observatorio_template_custom_text_ura SET custom_text= ? WHERE id = ?";
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
			Logger.putLog("SQL Exception: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_template_custom_text_ura WHERE id = ?")) {
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_template_custom_text_ura WHERE id_observatory_execution = ?")) {
			ps.setLong(1, idExObs);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", UraCustomTextDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}
}
