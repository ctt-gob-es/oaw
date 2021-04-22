package es.inteco.intav.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Element;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;

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
		final String query = "INSERT INTO tanalisis_accesibilidad(id_analisis, url) VALUES (?,?) ON DUPLICATE KEY UPDATE  url = ?";
		for (Element link : accessibilityLinks) {
			// Max URL size
			String url = link.getAttribute("href");
			if (url.length() > 256) {
				url = link.getAttribute("href").substring(0, 256);
			}
			try (PreparedStatement ps = c.prepareStatement(query)) {
				ps.setLong(1, idAnalisis);
				ps.setString(2, url);
				ps.setString(3, url);
				ps.executeUpdate();
			} catch (SQLException e) {
				Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
				throw e;
			}
		}
		DataBaseManager.closeConnection(c);
	}

	/**
	 * Increment check ok.
	 *
	 * @param c                 the c
	 * @param idAnalisis        the id analisis
	 * @param accessibilityLink the accessibility link
	 * @throws SQLException the SQL exception
	 */
	public static void incrementCheckOk(Connection c, final Long idAnalisis, final Element accessibilityLink) throws SQLException {
		final String query = "UPDATE tanalisis_accesibilidad SET checks_ok = checks_ok +1 WHERE id_analisis = ? AND url = ?";
		String url = accessibilityLink.getAttribute("href");
		if (url.length() > 256) {
			url = accessibilityLink.getAttribute("href").substring(0, 256);
		}
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idAnalisis);
			ps.setString(2, url);
			ps.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DataBaseManager.closeConnection(c);
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
		// Max ok
		final String query = "SELECT url FROM tanalisis_accesibilidad WHERE id_analisis = (SELECT cod_rastreo FROM tanalisis WHERE cod_analisis = ?) ORDER BY checks_ok DESC limit 1";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idAnalisis);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					urls = rs.getString("url");
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DataBaseManager.closeConnection(c);
		}
		return urls;
	}

	/**
	 * Gets the source code.
	 *
	 * @param c          the c
	 * @param idAnalisis the id analisis
	 * @return the source code
	 * @throws SQLException the SQL exception
	 */
	public static String getSourceCode(Connection c, final Long idAnalisis) throws SQLException {
		// Max ok
		final String query = "SELECT cod_fuente FROM tanalisis_accesibilidad WHERE id_analisis = ? ORDER BY checks_ok DESC limit 1";
		try (PreparedStatement ps = c.prepareStatement(query)) {
			ps.setLong(1, idAnalisis);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new String(Base64.decodeBase64(rs.getString("cod_fuente").getBytes()));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("SQL Exception: ", ProxyDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		} finally {
			DataBaseManager.closeConnection(c);
		}
		return null;
	}
}
