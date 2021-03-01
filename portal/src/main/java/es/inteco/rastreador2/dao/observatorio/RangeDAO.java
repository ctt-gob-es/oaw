package es.inteco.rastreador2.dao.observatorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.inteco.common.logging.Logger;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.observatorio.RangeForm;
import es.inteco.rastreador2.dao.etiqueta.EtiquetaDAO;
import es.inteco.rastreador2.utils.DAOUtils;

/**
 * The Class RangeDAO.
 */
public class RangeDAO {
	/**
	 * Count.
	 *
	 * @param c      the c
	 * @param nombre the nombre
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int count(Connection c, String nombre) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) FROM observatorio_range e WHERE 1=1 ";
		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(e.name) like UPPER(?) ";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			if (StringUtils.isNotEmpty(nombre)) {
				ps.setString(count++, "%" + nombre + "%");
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the etiquetas.
	 *
	 * @param c the c
	 * @return the etiquetas
	 * @throws SQLException the SQL exception
	 */
	public static List<RangeForm> findAll(Connection c) throws SQLException {
		final List<RangeForm> results = new ArrayList<>();
		String query = "SELECT id, name, min_value, max_value, min_value_operator, max_value_operator FROM observatorio_range WHERE 1=1  ORDER BY UPPER(name) ASC, UPPER(name) ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final RangeForm form = new RangeForm();
					form.setId(rs.getLong("id"));
					form.setName(rs.getString("name"));
					form.setMinValue(rs.getFloat("min_value"));
					form.setMaxValue(rs.getFloat("max_value"));
					form.setMinValueOperator(rs.getString("min_value_operator"));
					form.setMaxValueOperator(rs.getString("max_value_operator"));
					results.add(form);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Save.
	 *
	 * @param c    the c
	 * @param form the form
	 * @throws SQLException the SQL exception
	 */
	public static void save(Connection c, final RangeForm form) throws SQLException {
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement("INSERT INTO observatorio_range(name, min_value, max_value, min_value_operator, max_value_operator) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, form.getName());
			ps.setFloat(2, form.getMinValue());
			ps.setFloat(3, form.getMaxValue());
			ps.setString(4, form.getMinValueOperator());
			ps.setString(5, form.getMaxValueOperator());
			int affectedRows = ps.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating range failed, no rows affected.");
			}
			c.commit();
		} catch (SQLException e) {
			c.rollback();
			Logger.putLog("SQL_EXCEPTION: ", RangeDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
	 * @throws SQLException the SQL exception
	 */
	public static void update(Connection c, final RangeForm form) throws SQLException {
		final String query = "UPDATE observatorio_range SET name = ?, min_value = ?, max_value = ?, min_value_operator = ?, max_value_operator = ? WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, form.getName());
			ps.setFloat(2, form.getMinValue());
			ps.setFloat(3, form.getMaxValue());
			ps.setString(4, form.getMinValueOperator());
			ps.setString(5, form.getMaxValueOperator());
			ps.setLong(6, form.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", RangeDAO.class, Logger.LOG_LEVEL_ERROR, e);
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
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM observatorio_range WHERE id = ?")) {
			ps.setLong(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", RangeDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Exists etiqueta.
	 *
	 * @param c    the c
	 * @param form the form
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public static boolean exists(Connection c, final RangeForm form) throws SQLException {
		boolean exists = false;
		String query = "SELECT * FROM observatorio_range WHERE UPPER(name) = UPPER(?)";
		if (form.getId() != null) {
			query += " AND id <> ?";
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, form.getName());
			if (form.getId() != null) {
				ps.setLong(2, form.getId());
			}
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				exists = true;
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", EtiquetaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return exists;
	}
}
