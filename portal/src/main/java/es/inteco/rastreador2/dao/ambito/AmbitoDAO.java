package es.inteco.rastreador2.dao.ambito;

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
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.dao.complejidad.ComplejidadDAO;
import es.inteco.rastreador2.dao.proxy.ProxyDAO;

/**
 * The Class AmbitoDAO.
 */
public class AmbitoDAO {
	/**
	 * Gets the ambit by ID.
	 *
	 * @param c       the c
	 * @param ambitId the ambit id
	 * @return the ambit by ID
	 * @throws Exception the exception
	 */
	public static AmbitoForm getAmbitByID(Connection c, String ambitId) throws Exception {
		AmbitoForm ambit = new AmbitoForm();
		String query = "SELECT c.id_ambito, c.nombre FROM ambitos_lista c WHERE c.id_ambito = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, ambitId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ambit.setId(ambitId);
					ambit.setName(rs.getString("c.nombre"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return ambit;
	}

	/**
	 * Gets the ambit by name.
	 *
	 * @param c         the c
	 * @param ambitName the ambit name
	 * @return the ambit by name
	 * @throws Exception the exception
	 */
	public static AmbitoForm getAmbitByName(Connection c, String ambitName) throws Exception {
		AmbitoForm ambit = null;
		String query = "SELECT c.id_ambito, c.nombre FROM ambitos_lista c WHERE c.nombre = ?";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setString(1, ambitName);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					ambit = new AmbitoForm();
					ambit.setId(rs.getString("c.id_ambito"));
					ambit.setName(rs.getString("c.nombre"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return ambit;
	}

	/**
	 * Gets the complejidades.
	 *
	 * @param c      the c
	 * @param nombre the nombre
	 * @param page   the page
	 * @return the complejidades
	 * @throws SQLException the SQL exception
	 */
	public static List<AmbitoForm> getAmbitos(Connection c, String nombre, int page) throws SQLException {
		final List<AmbitoForm> results = new ArrayList<>();
		String query = "SELECT a.id_ambito, a.nombre, a.descripcion FROM ambitos_lista a WHERE 1=1 ";
		if (StringUtils.isNotEmpty(nombre)) {
			query += " AND UPPER(a.nombre) like UPPER(?) ";
		}
		query += "ORDER BY UPPER(a.id_ambito) ASC ";
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
					AmbitoForm ambitoForm = new AmbitoForm();
					ambitoForm.setId(rs.getString("a.id_ambito"));
					ambitoForm.setName(rs.getString("a.nombre"));
					ambitoForm.setDescripcion(rs.getString("a.descripcion"));
					results.add(ambitoForm);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ComplejidadDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return results;
	}
}
