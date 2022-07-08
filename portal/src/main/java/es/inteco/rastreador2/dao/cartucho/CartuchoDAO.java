/*******************************************************************************
* Copyright (C) 2012 INTECO, Instituto Nacional de Tecnologías de la Comunicación, 
* This program is licensed and may be used, modified and redistributed under the terms
* of the European Public License (EUPL), either version 1.2 or (at your option) any later 
* version as soon as they are approved by the European Commission.
* Unless required by applicable law or agreed to in writing, software distributed under the 
* License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
* ANY KIND, either express or implied. See the License for the specific language governing 
* permissions and more details.
* You should have received a copy of the EUPL1.2 license along with this program; if not, 
* you may find it at http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=CELEX:32017D0863
* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
* Modificaciones: MINHAFP (Ministerio de Hacienda y Función Pública) 
* Email: observ.accesibilidad@correo.gob.es
******************************************************************************/
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

/**
 * The Class CartuchoDAO.
 */
public final class CartuchoDAO {
	/**
	 * Instantiates a new cartucho DAO.
	 */
	private CartuchoDAO() {
	}

	/**
	 * Gets the application from cartdige id.
	 *
	 * @param c          the c
	 * @param idCartucho the id cartucho
	 * @return the application
	 * @throws SQLException the SQL exception
	 */
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

	/**
	 * Gets the nombre cartucho.
	 *
	 * @param idTracking the id tracking
	 * @return the nombre cartucho
	 * @throws Exception the exception
	 */
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
	 * Método que devuelve el id de la normativa asociada al cartucho, en caso de los cartuchos de accesibilidad y -1 en otros casos.
	 *
	 * @param c          Connection a la base de datos
	 * @param idCartucho id del cartucho
	 * @return el id de la normativa asociada si es un cartucho de accesibilidad y -1 en otros casos
	 * @throws SQLException the SQL exception
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
	 * Método que dado un id de cartucho indica si el cartucho pertenece a una normativa de accesibilidad o no.
	 *
	 * @param c          Connection a la base de datos
	 * @param idCartucho id del cartucho
	 * @return true si el cartucho pertenece a un cartucho de accesibilidad
	 * @throws SQLException the SQL exception
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
	 * Obtiene el nombre de la aplicación del cartucho asociado a un rastreo realizado.
	 *
	 * @param c                  the c
	 * @param idRastreoRealizado the id rastreo realizado
	 * @param idRastreo          the id rastreo
	 * @return the application from executed observatory id
	 * @throws SQLException the SQL exception
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

	/**
	 * Gets the application from crawler exceution id.
	 *
	 * @param c                  the c
	 * @param idRastreoRealizado the id rastreo realizado
	 * @return the application from crawler exceution id
	 * @throws SQLException the SQL exception
	 */
	public static String getApplicationFromCrawlerExceutionId(final Connection c, final Long idRastreoRealizado) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("SELECT aplicacion FROM cartucho c, rastreos_realizados rr where c.id_cartucho=rr.id_cartucho and rr.id=?")) {
			ps.setLong(1, idRastreoRealizado);
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

	/**
	 * Gets the application from analisis id.
	 *
	 * @param c          the c
	 * @param idAnalisis the id analisis
	 * @return the application from analisis id
	 * @throws SQLException the SQL exception
	 */
	public static String getApplicationFromAnalisisId(final Connection c, final Long idAnalisis) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement("select c.aplicacion from cartucho c, tanalisis ta where c.id_guideline=ta.cod_guideline and ta.cod_analisis=?")) {
			ps.setLong(1, idAnalisis);
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

	public static String getApplicationFromIdExObs(final Connection c, final Long idExobs) throws SQLException {
		try (PreparedStatement ps = c.prepareStatement(
				"SELECT c.aplicacion FROM observatorio o JOIN observatorios_realizados obr ON o.id_observatorio = obr.id_observatorio JOIN cartucho c ON o.id_cartucho = c.id_cartucho WHERE obr.id = ?")) {
			ps.setLong(1, idExobs);
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
