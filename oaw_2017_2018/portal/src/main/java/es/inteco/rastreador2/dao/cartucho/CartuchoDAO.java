package es.inteco.rastreador2.dao.cartucho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import es.inteco.common.logging.Logger;
import es.inteco.plugin.dao.DataBaseManager;
import es.inteco.plugin.dao.WebAnalyzerDao;
import es.inteco.rastreador2.dao.login.LoginDAO;
import es.inteco.rastreador2.dao.observatorio.ObservatorioDAO;

public final class CartuchoDAO {

	private CartuchoDAO() {
	}

	public static String getApplication(final Connection c, final Long idCartucho) throws SQLException {
		String application = "";
		try (PreparedStatement ps = c.prepareStatement("SELECT aplicacion FROM cartucho WHERE id_cartucho = ?")) {
			ps.setLong(1, idCartucho);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					application = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return application;
	}

	public static String[] getNombreCartucho(long idTracking) throws Exception {
		try (Connection conn = DataBaseManager.getConnection()) {
			final List<String> cartridgeNames = WebAnalyzerDao.getCartridgeNames(conn, idTracking);
			return cartridgeNames.toArray(new String[cartridgeNames.size()]);
		} catch (SQLException e) {
			Logger.putLog("Exception: ", CartuchoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Método que devuelve el id de la normativa asociada al cartucho, en caso
	 * de los cartuchos de accesibilidad y -1 en otros casos
	 *
	 * @param c
	 *            Connection a la base de datos
	 * @param idCartucho
	 *            id del cartucho
	 * @return el id de la normativa asociada si es un cartucho de accesibilidad
	 *         y -1 en otros casos
	 * @throws SQLException
	 */
	public static int getGuideline(final Connection c, long idCartucho) throws SQLException {
		int idGuideline = -1;
		try (PreparedStatement preparedStatement = c.prepareStatement("SELECT id_guideline FROM cartucho WHERE id_cartucho = ?")) {
			preparedStatement.setLong(1, idCartucho);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					idGuideline = resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", LoginDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return idGuideline;
	}

	/**
	 * Método que dado un id de cartucho indica si el cartucho pertenece a una
	 * normativa de accesibilidad o no
	 *
	 * @param c
	 *            Connection a la base de datos
	 * @param idCartucho
	 *            id del cartucho
	 * @return true si el cartucho pertenece a un cartucho de accesibilidad
	 * @throws SQLException
	 */
	public static boolean isCartuchoAccesibilidad(Connection c, long idCartucho) throws SQLException {
		try (PreparedStatement pes = c.prepareStatement("SELECT nombre FROM cartucho WHERE id_cartucho = ?")) {
			pes.setLong(1, idCartucho);
			try (ResultSet res = pes.executeQuery()) {
				if (res.next()) {
					return "es.inteco.accesibilidad.CartuchoAccesibilidad".equals(res.getString("nombre"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al isCartuchoAccesibilidad", CartuchoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return false;
	}

	/**
	 * TODO 2017 - Obtiene el nombre de la aplicación del cartucho asociado a un
	 * rastreo realizado
	 * 
	 * @param c
	 * @param idRastreoRealizado
	 * @param idRastreo
	 * @return
	 * @throws SQLException
	 */
	public static String getApplicationFromExecutedObservatoryId(final Connection c, final Long idRastreoRealizado, final Long idRastreo) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT aplicacion FROM cartucho c, rastreos_realizados rr where c.id_cartucho=rr.id_cartucho and rr.id_rastreo=? and rr.id=?")) {
			ps.setLong(1, idRastreo);
			ps.setLong(2, idRastreoRealizado);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("aplicacion");
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Exception: ", ObservatorioDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

}
