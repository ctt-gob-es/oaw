package es.inteco.rastreador2.dao.proxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.inteco.common.logging.Logger;
import es.inteco.rastreador2.actionform.semillas.ProxyForm;

/**
 * The Class ProxyDAO.
 */
public class ProxyDAO {
	
	
	/**
	 * Gets the proxy.
	 *
	 * @param c the c
	 * @return the proxy
	 * @throws Exception the exception
	 */
	public static ProxyForm getProxy(Connection c) throws Exception{
		
		ProxyForm proxy = new ProxyForm();
		
		String query = "SELECT p.status, p.url, p.port FROM observatorio_proxy p WHERE 1=1";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			
			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					proxy.setStatus(rs.getInt("p.status"));
					proxy.setUrl(rs.getString("p.url"));
					proxy.setPort(rs.getString("p.port"));

				}
			}
			
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		
		return proxy;
		
	
		
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
	public static void update(Connection c, ProxyForm proxy) throws SQLException {
		final String query = "UPDATE observatorio_proxy SET status = ?, url = ?, port = ? WHERE 1=1";

		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setInt(1, proxy.getStatus());
			ps.setString(2, proxy.getUrl());
			ps.setString(3, proxy.getPort());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}

	}

}
