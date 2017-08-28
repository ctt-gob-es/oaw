package es.inteco.rastreador2.dao.dependencia;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.inteco.common.Constants;
import es.inteco.common.logging.Logger;
import es.inteco.common.properties.PropertiesManager;
import es.inteco.common.utils.StringUtils;
import es.inteco.rastreador2.actionform.semillas.DependenciaForm;

/**
 * The Class DependenciaDAO.
 */
public final class DependenciaDAO {

	/**
	 * Instantiates a new dependencia DAO.
	 */
	private DependenciaDAO() {
	}

	/**
	 * Count dependencias.
	 *
	 * @param c
	 *            the c
	 * @param nombre
	 *            the nombre
	 * @return the int
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static int countDependencias(Connection c, String nombre) throws SQLException {
		int count = 1;
		String query = "SELECT COUNT(*) FROM dependencia d WHERE 1=1 ";

		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
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
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the dependencias.
	 *
	 * @param c
	 *            the c
	 * @param nombre
	 *            the nombre
	 * @param page
	 *            the page
	 * @return the dependencias
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static List<DependenciaForm> getDependencias(Connection c, String nombre, int page) throws SQLException {
		final List<DependenciaForm> results = new ArrayList<>();
		String query = "SELECT d.id_dependencia, d.nombre FROM dependencia d WHERE 1=1 ";

		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(d.nombre) like UPPER(?) ";
		}

		query += "ORDER BY UPPER(d.nombre) ASC ";

		if (page != Constants.NO_PAGINACION) {
			query += "LIMIT ? OFFSET ?";
		}

		try (PreparedStatement ps = c.prepareStatement(query)) {
			int count = 1;

			if (StringUtils.isNotEmpty(nombre)) {
				ps.setString(count++, "%" + nombre + "%");
			}

			if (page != Constants.NO_PAGINACION) {
				PropertiesManager pmgr = new PropertiesManager();
				int pagSize = Integer.parseInt(pmgr.getValue(CRAWLER_PROPERTIES, "pagination.size"));
				int resultFrom = pagSize * page;
				ps.setInt(count++, pagSize);
				ps.setInt(count, resultFrom);
			}
			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					DependenciaForm dependenciaForm = new DependenciaForm();
					dependenciaForm.setId(rs.getLong("d.id_dependencia"));
					dependenciaForm.setName(rs.getString("d.nombre"));

					results.add(dependenciaForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}

	/**
	 * Exists dependencia.
	 *
	 * @param c
	 *            the c
	 * @param dependencia
	 *            the dependencia
	 * @return true, if successful
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static boolean existsDependencia(Connection c, DependenciaForm dependencia) throws SQLException {

		boolean exists = false;

		final String query = "SELECT * FROM dependencia WHERE UPPER(nombre) = UPPER(?)";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, dependencia.getName());
			ResultSet result = ps.executeQuery();

			if (result != null && result.next()) {
				exists = true;
			}

		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

		return exists;

	}

	/**
	 * Save.
	 *
	 * @param c
	 *            the c
	 * @param dependencia
	 *            the dependencia
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static void save(Connection c, DependenciaForm dependencia) throws SQLException {
		final String query = "INSERT INTO dependencia(nombre) VALUES (?)";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, dependencia.getName());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Update.
	 *
	 * @param c
	 *            the c
	 * @param dependencia
	 *            the dependencia
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static void update(Connection c, DependenciaForm dependencia) throws SQLException {
		final String query = "UPDATE dependencia SET nombre = ? WHERE id_dependencia = ?";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, dependencia.getName());
			ps.setLong(2, dependencia.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

	}

	/**
	 * Delete.
	 *
	 * @param c
	 *            the c
	 * @param idDependencia
	 *            the id dependencia
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static void delete(Connection c, Long idDependencia) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("DELETE FROM dependencia WHERE id_dependencia = ?")) {
			ps.setLong(1, idDependencia);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", DependenciaDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

}