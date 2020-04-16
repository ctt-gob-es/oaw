package es.inteco.intav.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.w3c.dom.Element;

import es.inteco.common.logging.Logger;

/**
 * The Class TAnalisisAccesibilidadDAO.
 */
public class TAnalisisAccesibilidadDAO {
	/**
	 * Update.
	 *
	 * @param c                  the c
	 * @param idAnalisis         the id analisis
	 * @param accessibilityLinks the accessibility links
	 * @throws SQLException the SQL exception
	 */
	public static void insert(Connection c, final Long idAnalisis, final List<Element> accessibilityLinks) throws SQLException {
		// INSERT INTO t1 (a,b,c) VALUES (1,2,3) ON DUPLICATE KEY UPDATE c=c+1;
		final String query = "INSERT INTO tanalisis_accesibilidad(id_analisis, urls) VALUES (?,?) ON DUPLICATE KEY UPDATE  urls = ?";
		StringBuilder urls = new StringBuilder("");
		for (Element link : accessibilityLinks) {
			urls.append(link.getAttribute("href"));
			urls.append(",");
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idAnalisis);
			ps.setString(2, urls.toString());
			ps.setString(3, urls.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Gets the urls.
	 *
	 * @param c          the c
	 * @param idAnalisis the id analisis
	 * @return the urls
	 * @throws SQLException the SQL exception
	 */
	public static String getUrls(Connection c, final Long idAnalisis) throws SQLException {
		String urls = "";
		final String query = "SELECT urls FROM tanalisis_accesibilidad WHERE id_analisis = (SELECT cod_rastreo FROM tanalisis WHERE cod_analisis = ?)";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idAnalisis);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					urls = rs.getString("urls");
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return urls;
	}
}
