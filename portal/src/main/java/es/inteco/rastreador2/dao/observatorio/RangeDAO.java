package es.inteco.rastreador2.dao.observatorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
public class RangeDAO extends DataBaseDAO {
	/**
	 * Count.
	 *
	 * @param c      the c
	 * @param nombre the nombre
	 * @return the int
	 * @throws SQLException the SQL exception
	 */
	public static int count(Connection c, String nombre) throws Exception {

		c = reOpenConnectionIfIsNecessary(c);
		
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
	public static List<RangeForm> findAll(Connection c) throws Exception {

		c = reOpenConnectionIfIsNecessary(c);
		
		final List<RangeForm> results = new ArrayList<>();
		String query = "SELECT id, weight, name, min_value, max_value, min_value_operator, max_value_operator, color FROM observatorio_range WHERE 1=1  ORDER BY weight ASC, UPPER(name) ASC";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					final RangeForm form = new RangeForm();
					form.setId(rs.getLong("id"));
					form.setWeight(rs.getInt("weight"));
					form.setName(rs.getString("name"));
					form.setMinValueOperator(rs.getString("min_value_operator"));
					if (!org.apache.commons.lang3.StringUtils.isEmpty(form.getMinValueOperator())) {
						form.setMinValue(rs.getFloat("min_value"));
					} else {
						form.setMinValue(null);
					}
					form.setMaxValueOperator(rs.getString("max_value_operator"));
					if (!org.apache.commons.lang3.StringUtils.isEmpty(form.getMaxValueOperator())) {
						form.setMaxValue(rs.getFloat("max_value"));
					} else {
						form.setMaxValue(null);
					}
					form.setColor(rs.getString("color"));
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
	public static void save(Connection c, final RangeForm form) throws Exception {

		c = reOpenConnectionIfIsNecessary(c);
		
		PreparedStatement ps = null;
		try {
			c.setAutoCommit(false);
			ps = c.prepareStatement("INSERT INTO observatorio_range(name, min_value, max_value, min_value_operator, max_value_operator, weight, color) VALUES (?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, form.getName());
			ps.setFloat(2, form.getMinValue());
			ps.setFloat(3, form.getMaxValue());
			ps.setString(4, form.getMinValueOperator());
			ps.setString(5, form.getMaxValueOperator());
			ps.setInt(6, form.getWeight());
			ps.setString(7, form.getColor());
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
	public static void update(Connection c, final RangeForm form) throws Exception {

		c = reOpenConnectionIfIsNecessary(c);
		
		final String query = "UPDATE observatorio_range SET name = ?, min_value = ?, max_value = ?, min_value_operator = ?, max_value_operator = ?, weight = ?, color = ? WHERE id = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, form.getName());
			if (!StringUtils.isEmpty(form.getMinValueOperator())) {
				ps.setFloat(2, form.getMinValue());
			} else {
				ps.setNull(2, Types.FLOAT);
			}
			if (!StringUtils.isEmpty(form.getMaxValueOperator())) {
				ps.setFloat(3, form.getMaxValue());
			} else {
				ps.setNull(3, Types.FLOAT);
			}
			ps.setString(4, form.getMinValueOperator());
			ps.setString(5, form.getMaxValueOperator());
			ps.setInt(6, form.getWeight());
			ps.setString(7, form.getColor());
			ps.setLong(8, form.getId());
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
	public static void delete(Connection c, final Long id) throws Exception {

		c = reOpenConnectionIfIsNecessary(c);
		
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
	public static boolean exists(Connection c, final RangeForm form) throws Exception {

		c = reOpenConnectionIfIsNecessary(c);
		
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
