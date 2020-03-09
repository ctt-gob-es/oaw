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
package es.inteco.plugin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.inteco.common.logging.Logger;
import es.inteco.crawler.common.Constants;
import es.inteco.crawler.dao.Seed;

/**
 * The Class RastreoDAO.
 */
public final class RastreoDAO {
	/**
	 * Instantiates a new rastreo DAO.
	 */
	private RastreoDAO() {
	}

	/**
	 * Actualiza el estado de todos los observatorios que están activos (OBS_LAUNCHED_STATUS) a finalizados con error (OBS_ERROR_STATUS).
//	 *
	 * @param connection conexión (Connection) a la base de datos
	 * @throws SQLException the SQL exception
	 */
	public static void stopRunningObservatories(final Connection connection) throws SQLException {
		try (PreparedStatement pst = connection.prepareStatement("UPDATE observatorios_realizados SET estado = ? WHERE estado = ?")) {
			pst.setInt(1, Constants.OBS_ERROR_STATUS);
			pst.setInt(2, Constants.OBS_LAUNCHED_STATUS);
			pst.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Error al actualizar el estado del rastreo", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Actualiza el estado de un rastreo.
	 *
	 * @param c         conexión (Connection) a la base de datos
	 * @param idRastreo identificador (long) del rastreo a actualizar
	 * @param status    nuevo estado del rastreo
	 */
	public static void actualizarEstadoRastreo(final Connection c, final long idRastreo, final int status) {
		if (idRastreo != -1) {
			try (PreparedStatement pst = c.prepareStatement("UPDATE rastreo SET estado = ? WHERE id_rastreo = ?")) {
				pst.setInt(1, status);
				pst.setLong(2, idRastreo);
				pst.executeUpdate();
			} catch (SQLException e) {
				Logger.putLog("Error al actualizar el estado del rastreo", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			}
		}
	}

	/**
	 * Actualiza el estado de todos los rastreos que están activos (LAUNCHED) a parados (STOPPED).
	 *
	 * @param c conexión (Connection) a la base de datos
	 * @throws SQLException the SQL exception
	 */
	public static void stopRunningCrawlings(final Connection c) throws SQLException {
		try (PreparedStatement pst = c.prepareStatement("UPDATE rastreo SET estado = ? WHERE estado = ?")) {
			pst.setInt(1, Constants.STATUS_STOPPED);
			pst.setInt(2, Constants.STATUS_LAUNCHED);
			pst.executeUpdate();
		} catch (SQLException e) {
			Logger.putLog("Error al parar crawlings activos", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Obtiene las urls (una si es crawling aleatorio o hasta 17 separadas por ';') de una semilla.
	 *
	 * @param conn       conexión (Connection) a la base de datos
	 * @param idCrawling identificador del rastreo
	 * @param type       tipo de lista (semilla, rastreable, no rastreable u observatorio) a la que pertenece la semilla
	 * @return una cadena que contiene la url inicial si es un crawling aleatorio o hasta 17 urls separadas por el token ';'
	 * @throws SQLException the SQL exception
	 */
	public static String getList(final Connection conn, final Long idCrawling, final int type) throws SQLException {
		String query = "";
		if (type == Constants.ID_LISTA_SEMILLA || type == Constants.ID_LISTA_OBSERVATORIO) {
			query = "SELECT l.lista FROM lista l JOIN rastreo r ON (r.semillas = l.id_lista) WHERE id_rastreo = ? and l.id_tipo_lista = ?";
		} else if (type == Constants.ID_LISTA_RASTREABLE) {
			query = "SELECT l.lista FROM lista l JOIN rastreo r ON (r.lista_rastreable = l.id_lista) WHERE id_rastreo = ? and l.id_tipo_lista = ?";
		} else if (type == Constants.ID_LISTA_NO_RASTREABLE) {
			query = "SELECT l.lista FROM lista l JOIN rastreo r ON (r.lista_no_rastreable = l.id_lista) WHERE id_rastreo = ? and l.id_tipo_lista = ?";
		}
		try (PreparedStatement pst = conn.prepareStatement(query)) {
			pst.setLong(1, idCrawling);
			pst.setInt(2, type);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getString("lista");
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al recuperar la semilla del rastreo con id " + idCrawling, RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Obtiene el identificador del cartucho asociado a un rastreo.
	 *
	 * @param c         conexión (Connection) a la base de datos
	 * @param idRastreo identificador (long) del rastreo
	 * @return el identificador (int) del cartucho utilizado en ese rastreo
	 * @throws SQLException the SQL exception
	 */
	public static int recuperarCartuchoPorRastreo(final Connection c, final long idRastreo) throws SQLException {
		try (PreparedStatement pst = c.prepareStatement("SELECT id_cartucho FROM cartucho_rastreo WHERE id_rastreo = ?")) {
			pst.setLong(1, idRastreo);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return -1;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
	}

	/**
	 * Obtiene el identificador de la normativa asociada a un rastreo.
	 *
	 * @param c         conexión (Connection) a la base de datos
	 * @param idRastreo identificador (long) del rastreo
	 * @return el identificador (Long) de la normativa utilizada en ese rastreo
	 * @throws SQLException the SQL exception
	 */
	public static Long recuperarIdNorma(final Connection c, final long idRastreo) throws SQLException {
		try (PreparedStatement pes = c.prepareStatement("SELECT id_guideline FROM rastreo WHERE id_rastreo = ?")) {
			pes.setLong(1, idRastreo);
			try (ResultSet res = pes.executeQuery()) {
				if (res.next()) {
					return res.getLong("id_guideline");
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Obtiene el nombre del fichero de una normativa.
	 *
	 * @param c           conexión (Connection) a la base de datos
	 * @param idGuideline identificador (long) de la normativa
	 * @return el nombre del fichero (String) que de la normativa
	 * @throws SQLException the SQL exception
	 */
	public static String recuperarFicheroNorma(final Connection c, final long idGuideline) throws SQLException {
		try (PreparedStatement pes = c.prepareStatement("SELECT des_guideline FROM tguidelines WHERE cod_guideline = ?")) {
			pes.setLong(1, idGuideline);
			try (ResultSet res = pes.executeQuery()) {
				if (res.next()) {
					return res.getString("des_guideline");
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al cerrar el preparedStament", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Método que dado un id de cartucho indica si el cartucho pertenece a una normativa de accesibilidad o no.
	 *
	 * @param c          conexión (Connection) a la base de datos
	 * @param idCartucho identificador (long) del cartucho
	 * @return true si el cartucho pertenece a un cartucho de accesibilidad
	 * @throws SQLException the SQL exception
	 */
	public static boolean isCartuchoAccesibilidad(final Connection c, final long idCartucho) throws SQLException {
		try (PreparedStatement pes = c.prepareStatement("SELECT nombre FROM cartucho WHERE id_cartucho = ?")) {
			pes.setLong(1, idCartucho);
			try (ResultSet res = pes.executeQuery()) {
				if (res.next()) {
					return "es.inteco.accesibilidad.CartuchoAccesibilidad".equals(res.getString("nombre"));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al invocar isCartuchoAccesibilidad", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return false;
	}

	/**
	 * Gets the extra info.
	 *
	 * @param c                  the c
	 * @param idRastreoRealizado the id rastreo realizado
	 * @return the extra info
	 * @throws SQLException the SQL exception
	 */
	public static ExtraInfo getExtraInfo(final Connection c, final Long idRastreoRealizado) throws SQLException {
		try (PreparedStatement pes = c.prepareStatement("SELECT rr.id_obs_realizado, l.nombre, l.lista FROM rastreos_realizados rr JOIN lista l ON rr.id_lista=l.id_lista WHERE id= ?")) {
			pes.setLong(1, idRastreoRealizado);
			try (ResultSet res = pes.executeQuery()) {
				if (res.next()) {
					return new ExtraInfo(res.getInt(1), res.getString(2), res.getString(3));
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al invocar isCartuchoAccesibilidad", RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
			throw e;
		}
		return null;
	}

	/**
	 * Gets the seed from crawling.
	 *
	 * @param c       the c
	 * @param idCrawl the id crawl
	 * @return the seed from crawling
	 */
	public static Seed getSeedFromCrawling(final Connection c, final Long idCrawl) {
		try (PreparedStatement pes = c.prepareStatement(
				"SELECT l.id_lista, c.profundidad, c.amplitud FROM rastreo r JOIN lista l ON r.semillas = l.id_lista JOIN complejidades_lista c ON c.id_complejidad = l.id_complejidad WHERE r.id_rastreo = ?")) {
			pes.setLong(1, idCrawl);
			try (ResultSet res = pes.executeQuery()) {
				if (res.next()) {
					Seed seed = new Seed();
					seed.setId(res.getLong("id_lista"));
					seed.setDepth(res.getInt("profundidad"));
					seed.setWidth(res.getInt("amplitud"));
					return seed;
				}
			}
		} catch (SQLException e) {
			Logger.putLog("Error al obtener la complejidad de la semilla del rastreo id=" + idCrawl, RastreoDAO.class, Logger.LOG_LEVEL_ERROR, e);
		}
		return null;		
	}
}
