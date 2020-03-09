package es.inteco.rastreador2.dao.ambito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.semillas.AmbitoForm;
import es.inteco.rastreador2.dao.proxy.ProxyDAO;

/**
 * The Class AmbitoDAO.
 */
public class AmbitoDAO {

	/**
	 * Gets the ambit by ID.
	 *
	 * @param c          the c
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
	 * @param c            the c
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

}
